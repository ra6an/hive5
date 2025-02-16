package com.hive5.hive5.repository;

import com.hive5.hive5.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);

    @Query(value = """
        SELECT * FROM users 
        WHERE id NOT IN (:friendIds) 
        ORDER BY RANDOM() 
        LIMIT 20
    """, nativeQuery = true)
    List<User> findRandomUsersExcludingFriends(@Param("friendIds") List<UUID> friendIds);
}
