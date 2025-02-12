package com.hive5.hive5.repository;

import com.hive5.hive5.model.FriendRequest;
import com.hive5.hive5.model.enums.FriendRequestStatus;
import com.hive5.hive5.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    List<FriendRequest> findByReceiverAndStatus(User receiver, FriendRequestStatus status);
    List<FriendRequest> findBySenderAndStatus(User sender, FriendRequestStatus status);
    Optional<FriendRequest> findBySenderAndReceiver(User sender, User receiver);
    Optional<FriendRequest> findBySenderAndReceiverAndStatus(User sender, User receiver, FriendRequestStatus status);

    // Lista svih friend requestova za usera sa xxx statusom
    @Query("SELECT fr FROM FriendRequest fr " +
            "WHERE fr.status = :status " +
            "AND (fr.sender = :user OR fr.receiver = :user)")
    List<FriendRequest> findAllFriendRequestByUser(@Param("user") User user, @Param("status") FriendRequestStatus status);
}
