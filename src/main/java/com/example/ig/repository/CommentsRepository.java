package com.example.ig.repository;

import com.example.ig.entity.Comments;
import com.example.ig.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentsRepository extends JpaRepository<Comments, Long> {
}
