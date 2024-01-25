package com.example.ig.repository;

import com.example.ig.entity.Comments;
import com.example.ig.entity.Subscriptions;
import jakarta.persistence.JoinColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubscriptionsRepository extends JpaRepository<Subscriptions, Long> {
    @Query("SELECT sub.groupUserId.groupId FROM Subscriptions sub where sub.groupUserId.userId=:userId")
    List<Long> getAllGroupsOfUser(long userId);
}
