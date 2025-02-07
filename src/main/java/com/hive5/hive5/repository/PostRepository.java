package com.hive5.hive5.repository;

import com.hive5.hive5.model.Post;
import com.hive5.hive5.model.PostStatus;
import com.hive5.hive5.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findPostsByStatusOrderByCreatedAtDesc(PostStatus status);
    List<Post> findPostsByUserAndStatusOrderByCreatedAtDesc(User user, PostStatus status);
}
