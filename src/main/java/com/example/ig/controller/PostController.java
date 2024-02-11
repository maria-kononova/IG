package com.example.ig.controller;

import com.example.ig.entity.*;
import com.example.ig.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.ig.IgApplication.*;

@Controller
public class PostController {
    private final PostRepository postRepository;
    private final LikesRepository likesRepository;
    private final CommentsRepository commentsRepository;

    @Autowired
    public PostController(PostRepository postRepository, LikesRepository likesRepository, CommentsRepository commentsRepository) {
        this.postRepository = postRepository;
        this.likesRepository = likesRepository;
        this.commentsRepository = commentsRepository;
    }

    @PostMapping("/like")
    @ResponseBody
    public String like(Model model, @RequestParam String postId) {
        if(user!=null) {
            LikeUser likeUser = new LikeUser(user.getId(), Long.valueOf(postId));
            Likes likes = new Likes(likeUser);
            likesRepository.save(likes);
            Post post = postRepository.getById(Long.valueOf(postId));
            post.setLikes(post.getLikes() + 1);
            postRepository.save(post);
            //model.addAttribute("posts", postRepository.getAllPostsOfGroup(group.getId()));
            return "Success";
        }
        return "noSuccess";
    }

    @PostMapping("/unlike")
    @ResponseBody
    public String unlike(Model model, @RequestParam String postId) {
        if(user!=null) {
            LikeUser likeUser = new LikeUser(user.getId(), Long.valueOf(postId));
            Likes likes = new Likes(likeUser);
            likesRepository.delete(likes);
            Post post = postRepository.getById(Long.valueOf(postId));
            post.setLikes(post.getLikes() - 1);
            postRepository.save(post);
            //model.addAttribute("posts", postRepository.getAllPostsOfGroup(group.getId()));
            return "Success";
        }
        return "noSuccess";
    }

    @PostMapping("/sendComment")
    @ResponseBody
    public String comment(Model model, @RequestParam String postId, @RequestParam String textComment, @RequestParam String idComments) {
        System.out.println("send");
        if(user!=null) {
            Comments comments = new Comments(Long.parseLong(postId), user.getId(), Long.parseLong(idComments), textComment, 0, new Date());
            commentsRepository.save(comments);
            Post post = postRepository.getById(Long.valueOf(postId));
            post.setComments(post.getComments() + 1);
            postRepository.save(post);
            model.addAttribute("comments", commentsRepository.findAll());
            return "Success";
        }
        return "noSuccess";
    }

    @GetMapping("/publicPost")
    @ResponseBody
    public String comment(Model model, @RequestParam String text, @RequestParam int countImage) {
        if(user!=null) {
            count = 0;
            //long idGroup, int type, String description, String img, Date datePublication, int likes, int views, int comments)
            List<String> images = new ArrayList<>();
            long newId = getMaxId() + 1;
            Post post1 = new Post(newId, group.getId(), 1, text, images, new Date(), 0, 0, 0);
            for(int i = 0; i < countImage; i++){
                images.add("http://localhost:8080/file/post/post" + post1.getId() + "_"+ i +".jpg");
            }
            post1.setImg(images);
            postRepository.save(post1);
            post = post1;
            return "Success";
        }
        return "noSuccess";
    }

    public long getMaxId(){
        long max = 0;
        for(Post post1 : postRepository.findAll()){
            if(post1.getId() > max) max = post1.getId();
        }
        return max;
    }

    /*@RequestMapping(value="/updatePostList", method=RequestMethod.GET)
    public String recSort(Model model) {
        System.out.println("2");
        return "index :: #postList";
    }*/

    public List<Likes> sortLikesByGroupId() {
        List<Likes> likes = new ArrayList<>();
        for (Post post : postRepository.getAllPostsOfGroup(group.getId())) {
            for (Likes like : likesRepository.findAll()) {
                if (post.getId() == like.getPostUserId().getPostId() && user.getId() == like.getPostUserId().getUserId()) {
                    likes.add(like);
                }
            }
        }
        return likes;
    }
}
