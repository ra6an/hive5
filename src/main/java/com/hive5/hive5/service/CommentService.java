package com.hive5.hive5.service;

import com.hive5.hive5.dto.Comment.CommentDTO;
import com.hive5.hive5.dto.Comment.CreateCommentRequest;
import com.hive5.hive5.dto.Notification.CreateNotificationRequest;
import com.hive5.hive5.dto.Post.PostDTO;
import com.hive5.hive5.exception.CustomException;
import com.hive5.hive5.model.Comment;
import com.hive5.hive5.model.Post;
import com.hive5.hive5.model.User;
import com.hive5.hive5.model.enums.NotificationType;
import com.hive5.hive5.model.enums.TargetType;
import com.hive5.hive5.repository.CommentRepository;
import com.hive5.hive5.repository.PostRepository;
import com.hive5.hive5.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

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
    private final NotificationService notificationService;
    private final WSNotificationService wsNotificationService;

    public Map<String, Object> getPostByCommentId(@PathVariable long commentId, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("Unauthorized!", "You are not authorized.", 401));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException("Bad Request!", "Comment not found.", 404));

        PostDTO postDTO = new PostDTO(comment.getPost());

        Map<String, Object> data = new HashMap<>();
        data.put("data", postDTO);

        return data;
    }

    public Map<String, Object> createComment(CreateCommentRequest createCommentRequest, Principal principal) {
        // Uzeti username iz tokena
        String username = principal.getName();
//
        // Instancirati Comment
        Comment comment = new Comment();

        // Naci usera u DB na osnovu usernamea
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("Unauthorized!", "You are not authorized.", 401));

        // Naci post u DB
        Post post = postRepository.findById(createCommentRequest.getPost())
                .orElseThrow(() -> new CustomException("Post doesn't exist!", "No post with that ID.", 400));

        comment.setUser(user);
        comment.setContent(createCommentRequest.getContent());
        comment.setPost(post);

        CreateNotificationRequest createNotificationRequest = new CreateNotificationRequest();
        createNotificationRequest.setSender(user);
        createNotificationRequest.setType(NotificationType.COMMENT);

        if(createCommentRequest.getParentComment() != 0) {
            Comment parentComment = commentRepository.findById(createCommentRequest.getParentComment())
                    .orElseThrow(() -> new CustomException("Comment doesn't exist!", "No comment with that ID.", 400));

            if(parentComment.getReplies() == null) {
                parentComment.setReplies(new ArrayList<>());
            }

            comment.setParentComment(parentComment);

            parentComment.getReplies().add(comment);

            createNotificationRequest.setReceiver(parentComment.getUser());
            createNotificationRequest.setTargetType(TargetType.COMMENT);
            createNotificationRequest.setTargetId(parentComment.getId());
        } else {
            comment.setParentComment(null);

            createNotificationRequest.setReceiver(post.getUser());
            createNotificationRequest.setTargetType(TargetType.POST);
            createNotificationRequest.setTargetId(post.getId());
        }

        commentRepository.save(comment);

        createNotificationRequest.setSecondaryTargetId(comment.getId());

        if(post.getComments() == null) {
            post.setComments(new ArrayList<>());
        }

        post.getComments().add(comment);
        postRepository.save(post);

        if(!createNotificationRequest.getSender().getId().equals(createNotificationRequest.getReceiver().getId())) {
            Map<String, Object> notificationData = notificationService.createNotification(createNotificationRequest);

            // TODO WEBSOCKET:: poslati receiveru notifikaciju
            wsNotificationService.sendDataViaWS(createNotificationRequest.getReceiver().getId(), notificationData, WSType.NOTIFICATION);
        }

        CommentDTO commentDTO = new CommentDTO(comment);

        Map<String, Object> data = new HashMap<>();
        data.put("data", commentDTO);

        return data;
    }
}
