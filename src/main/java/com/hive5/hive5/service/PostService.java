package com.hive5.hive5.service;

import com.hive5.hive5.dto.Post.CreatePostRequest;
import com.hive5.hive5.dto.Post.PostDTO;
import com.hive5.hive5.exception.CustomException;
import com.hive5.hive5.exception.InvalidStatusException;
import com.hive5.hive5.model.FriendRequest;
import com.hive5.hive5.model.Post;
import com.hive5.hive5.model.enums.FriendRequestStatus;
import com.hive5.hive5.model.enums.PostStatus;
import com.hive5.hive5.model.User;
import com.hive5.hive5.repository.FriendRequestRepository;
import com.hive5.hive5.repository.PostRepository;
import com.hive5.hive5.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FriendRequestRepository friendRequestRepository;

    public Map<String, Object> getPosts(String path, Principal principal) {
        String username = principal.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("Unauthorized!", "You are not authorized.", 401));

        List<Post> posts = new ArrayList<>();
        if(path.equals("explore")) {
            posts = postRepository.findPostsByStatusOrderByCreatedAtDesc(PostStatus.PUBLIC);
        }

        if(path.equals("home")) {
            List<FriendRequest> acceptedFriends = friendRequestRepository.findAllFriendRequestByUser(user, FriendRequestStatus.ACCEPTED);
            List<UUID> friendIds = acceptedFriends.stream()
                    .map(fr -> fr.getSender().getId().equals(user.getId()) ?
                            fr.getReceiver().getId() :
                            fr.getSender().getId())
                    .toList();

            posts = postRepository.findPostsByFriendIds(friendIds);
        }

        List<PostDTO> postDTOs = posts.stream()
                .map(PostDTO::new)
                .toList();

        Map<String, Object> data = new HashMap<>();
        data.put("data", postDTOs);
        data.put("postsCount", postDTOs.size());

        return data;
    }

    public Map<String, Object> getPostsFromUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("User not found!", "No user with that username.", 400));

        List<Post> posts = postRepository.findPostsByUserAndStatusOrderByCreatedAtDesc(user, PostStatus.PUBLIC);

        List<PostDTO> postDTOs = posts.stream().map(PostDTO::new).toList();

        Map<String, Object> data = new HashMap<>();
        data.put("data", postDTOs);
        data.put("postsCount", postDTOs.size());

        return data;
    }

    public Map<String, Object> getPost(long id, Principal principal) {
        Optional<Post> post = postRepository.findById(id);

        Post postObj = post.orElseThrow(() -> new CustomException("Invalid post ID!", "There is no post with that id.", 400));
        PostDTO postDTO = new PostDTO(postObj);

        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("Unauthorized!", "You are not authorized.", 403));

        if(!postObj.getUser().equals(user) && postObj.getStatus().equals(PostStatus.PRIVATE)) {
            List<FriendRequest> _friendRequest = friendRequestRepository.findByUsersAndStatus(user, postObj.getUser(), FriendRequestStatus.ACCEPTED);

            if(_friendRequest.isEmpty())
                throw new CustomException("Unauthorized!", "You are not allowed to see this post.", 403);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("data", postDTO);

        return data;
    }

    public Map<String, Object> createPost(@RequestBody CreatePostRequest createPostRequest, Principal principal) {
        // Uzeti username iz tokena
        String username = principal.getName();

        if(createPostRequest.getContent().isEmpty())
            throw new CustomException("Bad Request", "Post content can't be empty.", 404);

        // Instancirati Post
        Post post = new Post();

        // Naci usera u DB na osnovu usernamea
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("User not found!", "No user with that username.", 400));

        post.setUser(user);
        post.setContent(createPostRequest.getContent());

        // Dodati status posta i checkirati ako je korisnik poslao pogresan value
        try {
            post.setStatus(PostStatus.valueOf(createPostRequest.getStatus().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new InvalidStatusException("Invalid status: " + createPostRequest.getStatus());
        }

        // Spasiti post u DB
        postRepository.save(post);

        PostDTO postData = new PostDTO(post);

        Map<String, Object> mappedData = new HashMap<>();
        mappedData.put("data", postData);

        return mappedData;
    }
}
