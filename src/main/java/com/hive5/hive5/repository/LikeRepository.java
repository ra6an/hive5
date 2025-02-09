package com.hive5.hive5.repository;

import com.hive5.hive5.model.Comment;
import com.hive5.hive5.model.Like;
import com.hive5.hive5.model.Post;
import com.hive5.hive5.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndPost(User user, Post post);
    Optional<Like> findByUserAndComment(User user, Comment comment);
}
