package com.hive5.hive5.repository;

import com.hive5.hive5.model.Post;
import com.hive5.hive5.model.enums.PostStatus;
import com.hive5.hive5.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findPostsByStatusOrderByCreatedAtDesc(PostStatus status);
    List<Post> findPostsByUserAndStatusOrderByCreatedAtDesc(User user, PostStatus status);

    @Query("SELECT p FROM Post p WHERE p.user.id IN :friendIds ORDER BY p.createdAt DESC")
    List<Post> findPostsByFriendIds(@Param("friendIds") List<UUID> friendIds);
}
