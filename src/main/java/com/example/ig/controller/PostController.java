package com.example.ig.controller;

import com.example.ig.entity.LikeUser;
import com.example.ig.entity.Likes;
import com.example.ig.entity.Post;
import com.example.ig.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

import static com.example.ig.IgApplication.group;
import static com.example.ig.IgApplication.user;

@Controller
public class PostController {
    private final PostRepository postRepository;
    private final LikesRepository likesRepository;

    @Autowired
    public PostController(PostRepository postRepository, LikesRepository likesRepository) {
        this.postRepository = postRepository;
        this.likesRepository = likesRepository;
    }

    @PostMapping("/like")
    @ResponseBody
    public String like(Model model, @RequestParam String postId) {
        LikeUser likeUser = new LikeUser(user.getId(), Long.valueOf(postId));
        Likes likes = new Likes(likeUser);
        likesRepository.save(likes);
        Post post = postRepository.getById(Long.valueOf(postId));
        post.setLikes(post.getLikes() + 1);
        postRepository.save(post);
        //model.addAttribute("posts", postRepository.getAllPostsOfGroup(group.getId()));
        return "Success";
    }

    @PostMapping("/unlike")
    @ResponseBody
    public String unlike(Model model, @RequestParam String postId) {
        LikeUser likeUser = new LikeUser(user.getId(), Long.valueOf(postId));
        Likes likes = new Likes(likeUser);
        likesRepository.delete(likes);
        Post post = postRepository.getById(Long.valueOf(postId));
        post.setLikes(post.getLikes() - 1);
        postRepository.save(post);
        //model.addAttribute("posts", postRepository.getAllPostsOfGroup(group.getId()));
        return "Success";
    }

}
