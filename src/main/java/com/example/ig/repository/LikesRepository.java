package com.example.ig.repository;

import com.example.ig.entity.Comments;
import com.example.ig.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository  extends JpaRepository<Likes, Long> {
}
