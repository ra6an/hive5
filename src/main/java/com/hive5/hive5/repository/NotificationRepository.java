package com.hive5.hive5.repository;

import com.hive5.hive5.model.Notification;
import com.hive5.hive5.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query("""
    SELECT n FROM Notification n
    WHERE n.receiver = :user
    ORDER BY n.createdAt DESC
    """)
    List<Notification> findByReceiver(@Param("user") User user);
}
