package com.hive5.hive5.repository;

import com.hive5.hive5.model.Message;
import com.hive5.hive5.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderAndReceiver(User sender, User receiver);

    @Query("""
    SELECT DISTINCT 
        CASE 
            WHEN m.sender = :user THEN m.receiver.id
            ELSE m.sender.id
        END
    FROM Message m
    WHERE m.sender = :user OR m.receiver = :user
    """)
    List<UUID> findChatParticipants(@Param("user") User user);

    @Query("""
    SELECT m FROM Message m 
    WHERE (m.sender = :user1 AND m.receiver = :user2) 
       OR (m.sender = :user2 AND m.receiver = :user1)
    ORDER BY m.createdAt ASC
    """)
    List<Message> findMessagesBetweenUsers(@Param("user1") User user1, @Param("user2") User user2);
}
