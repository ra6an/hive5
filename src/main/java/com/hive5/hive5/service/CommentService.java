package com.hive5.hive5.service;

import com.hive5.hive5.dto.Comment.CommentDTO;
import com.hive5.hive5.dto.Comment.CreateCommentRequest;
import com.hive5.hive5.exception.CustomException;
import com.hive5.hive5.model.Comment;
import com.hive5.hive5.model.Post;
import com.hive5.hive5.model.User;
import com.hive5.hive5.repository.CommentRepository;
import com.hive5.hive5.repository.PostRepository;
import com.hive5.hive5.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public Map<String, Object> createComment(CreateCommentRequest createCommentRequest, Principal principal) {
        // Uzeti username iz tokena
        String username = principal.getName();
//
        // Instancirati Comment
        Comment comment = new Comment();

        // Naci usera u DB na osnovu usernamea
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("User not found!", "No user with that username.", 400));

        // Naci post u DB
        Post post = postRepository.findById(createCommentRequest.getPost())
                .orElseThrow(() -> new CustomException("Post doesn't exist!", "No post with that ID.", 400));

        comment.setUser(user);
        comment.setContent(createCommentRequest.getContent());
        comment.setPost(post);

        if(createCommentRequest.getParentComment() != 0) {
            Comment parentComment = commentRepository.findById(createCommentRequest.getParentComment())
                    .orElseThrow(() -> new CustomException("Comment doesn't exist!", "No comment with that ID.", 400));

            if(parentComment.getReplies() == null) {
                parentComment.setReplies(new ArrayList<>());
            }

            comment.setParentComment(parentComment);

            parentComment.getReplies().add(comment);
        } else {
            comment.setParentComment(null);
        }

        commentRepository.save(comment);

        if(post.getComments() == null) {
            post.setComments(new ArrayList<>());
        }

        post.getComments().add(comment);
        postRepository.save(post);

        CommentDTO commentDTO = new CommentDTO(comment);

        Map<String, Object> data = new HashMap<>();
        data.put("data", commentDTO);

        return data;
    }
}
