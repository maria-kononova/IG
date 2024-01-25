package com.example.ig.repository;

import com.example.ig.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p where p.idGroup=:groupId")
    List<Post> getAllPostsOfGroup(long groupId);
}
