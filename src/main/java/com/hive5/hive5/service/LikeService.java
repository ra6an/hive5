package com.hive5.hive5.service;

import com.hive5.hive5.dto.Like.LikeDTO;
import com.hive5.hive5.exception.CustomException;
import com.hive5.hive5.model.Comment;
import com.hive5.hive5.model.Like;
import com.hive5.hive5.model.Post;
import com.hive5.hive5.model.User;
import com.hive5.hive5.repository.CommentRepository;
import com.hive5.hive5.repository.LikeRepository;
import com.hive5.hive5.repository.PostRepository;
import com.hive5.hive5.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public Map<String, Object> like(@PathVariable long commentId, @PathVariable long postId, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("User not found!", "No user with that username.", 404));

        Like like = new Like();
        like.setUser(user);

        if(commentId != 0) {
            like.setPost(null);
            Comment comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new CustomException("Comment not found!", "No comment with that ID.", 404));

            if(checkIfUserAlreadyLikedEntity(user, comment, null)) {
                throw new CustomException("Bad Request!", "You already liked this comment.", 404);
            }

            like.setComment(comment);
            addLikeToEntity(like, comment, null);
        } else if (postId != 0) {
            like.setComment(null);
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new CustomException("Post not found!", "No post with that ID.", 404));

            if(checkIfUserAlreadyLikedEntity(user, null, post)) {
                throw new CustomException("Bad Request!", "You already liked this post.", 404);
            }

            like.setPost(post);
            addLikeToEntity(like, null, post);
        }

        likeRepository.save(like);

        LikeDTO likeDTO = new LikeDTO(like);
        Map<String, Object> data = new HashMap<>();
        data.put("data", likeDTO);

        return data;
    }

    private boolean checkIfUserAlreadyLikedEntity(User user, Comment comment, Post post) {
        if(comment != null) {
            Optional<Like> alreadyExistComment = likeRepository.findByUserAndComment(user, comment);
            return alreadyExistComment.isPresent();
        }

        if(post != null) {
            Optional<Like> alreadyExistPost = likeRepository.findByUserAndPost(user, post);
            return alreadyExistPost.isPresent();
        }

        return false;
    }

    private void addLikeToEntity(Like like, Comment comment, Post post) {
        if (comment != null) {
            if (comment.getLikes() == null) {
                comment.setLikes(new ArrayList<>());
            }
            comment.getLikes().add(like);
//            commentRepository.save(comment);
        } else if (post != null) {
            if (post.getLikes() == null) {
                post.setLikes(new ArrayList<>());
            }
            post.getLikes().add(like);
//            postRepository.save(post);
        }
    }
}
