package com.example.ig.controller;

import com.example.ig.entity.*;
import com.example.ig.repository.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.ig.IgApplication.group;
import static com.example.ig.IgApplication.user;

@Controller
public class GroupController {
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final SubscriptionsRepository subscriptionsRepository;
    private final PostRepository postRepository;
    private final LikesRepository likesRepository;
    private final CommentsRepository commentsRepository;

    @Autowired
    public GroupController(GroupRepository groupRepository, UserRepository userRepository, SubscriptionsRepository subscriptionsRepository, PostRepository postRepository, LikesRepository likesRepository, CommentsRepository commentsRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.subscriptionsRepository = subscriptionsRepository;
        this.postRepository = postRepository;
        this.likesRepository = likesRepository;
        this.commentsRepository = commentsRepository;
    }
    public Long getUserFromCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String cookieName = "userId";
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (cookieName.equals(c.getName())) {
                    if (!c.getValue().equals("")) {
                        return Long.valueOf(c.getValue());
                    }
                }
            }
        }
        return null;
    }
    @GetMapping("/group/{id}")
    public String groupPage(@PathVariable("id") long id, Model model, HttpServletRequest request, HttpServletResponse response){
        Long userId = getUserFromCookie(request, response);
        group = groupRepository.getById(id);
        if (userId != null) {
            user = userRepository.getById(userId);
            model.addAttribute("userLogin", user);
            model.addAttribute("sub", isSubscribes(id));
        }
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("groups", groupRepository.findAll());
        model.addAttribute("group", group);
        model.addAttribute("posts", postRepository.getAllPostsOfGroup(group.getId()));
        model.addAttribute("likes", sortLikesByGroupId());
        model.addAttribute("comments", commentsRepository.findAll());
        return "group";
    }
    @GetMapping("/likeUpdate")
    @ResponseBody
    public String likeUpdate(Model model){
        model.addAttribute("likes", sortLikesByGroupId());
        return "Success";
    }

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

    @RequestMapping(value="/updateGroupList/{query}", method= RequestMethod.GET)
    public String updateGroupList(Model model, @PathVariable("query") String query) {
        model.addAttribute("groups", searchSort(query));
        return "groups :: #groupList";
    }

    @RequestMapping(value="/getGroupList", method= RequestMethod.GET)
    public String getGroupList(Model model) {
        model.addAttribute("groups", groupRepository.findAll());
        return "groups :: #groupList";
    }


    @GetMapping("/groups")
    public String groupPage(Model model, HttpServletRequest request, HttpServletResponse response) {
        Long userId = getUserFromCookie(request, response);
        List<Group> myGroup = new ArrayList<>();
        if (userId != null) {
            user = userRepository.getById(userId);
            model.addAttribute("userLogin", user);
            myGroup = getMyGroup(subscriptionsRepository.getAllGroupsOfUser(user.getId()));
        }
        model.addAttribute("myGroups", myGroup);
        model.addAttribute("groups", groupRepository.findAll());
        return "groups";
    }

    @PostMapping("/sub")
    @ResponseBody
    public String subscribe(Model model){
        GroupUser groupUser = new GroupUser(user.getId(), group.getId());
        Subscriptions subscriptions = new Subscriptions(groupUser);
        subscriptionsRepository.save(subscriptions);
        group.setSubscribes(group.getSubscribes()+1);
        groupRepository.save(group);
        model.addAttribute("sub", true);
        return "Success";
    }

    @PostMapping("/unsub")
    @ResponseBody
    public String unsubscribe(Model model){
        GroupUser groupUser = new GroupUser(user.getId(), group.getId());
        group.setSubscribes(group.getSubscribes()-1);
        Subscriptions subscriptions = new Subscriptions(groupUser);
        subscriptionsRepository.delete(subscriptions);
        groupRepository.save(group);
        model.addAttribute("sub", false);
        return "Success";
    }

    @GetMapping("/createGroup")
    @ResponseBody
    public String createGroup(@RequestParam String imgGroup, @RequestParam String nameGroup, @RequestParam String desGroup){
        long id = getMaxId() + 1;
        Group newGroup = new Group(id, nameGroup, desGroup, "http://localhost:8080/file/group/group" + id + ".jpg", "1",  new Date(), 0, user.getId());
        groupRepository.save(newGroup);
        group = groupRepository.getById(newGroup.getId());
        return "http://localhost:8080/group/" + group.getId() + "?";
    }

    public long getMaxId(){
        long max = 0;
        for(Group group1 : groupRepository.findAll()){
            if(group1.getId() > max) max = group1.getId();
        }
        return max;
    }

    public List<Group> searchSort(String query){
        List<Group> groupsQuery = new ArrayList<>();
        for(Group gr : groupRepository.findAll()){
            if(gr.getName().toLowerCase().contains(query)){
                groupsQuery.add(gr);
                System.out.println(gr.getName());
            }
        }
        if(query.equals("")) { groupsQuery = groupRepository.findAll();}
        return groupsQuery;
    }
   /* @PostMapping("/searchGroup")
    @ResponseBody
    public String searchGroup(Model model, @RequestParam String query){
        List<Group> groupsQuery = new ArrayList<>();
        for(Group gr : groupRepository.findAll()){
            if(gr.getName().toLowerCase().contains(query)){
                groupsQuery.add(gr);
                System.out.println(gr.getName());
            }
        }
        if(query.equals("")) { groupsQuery = groupRepository.findAll();}
        model.addAttribute("groups", groupsQuery);
        return "Success";
    }*/


    public List<Group> getMyGroup(List<Long> myGroupsLong){
        List<Group> myGroups = new ArrayList<>();
        for(long idGroup : myGroupsLong){
            myGroups.add(groupRepository.getById(idGroup));
        }
        return myGroups;
    }

    public Boolean isSubscribes(long idGroupForCheck){
        System.out.println(subscriptionsRepository.getAllGroupsOfUser(user.getId()).contains(idGroupForCheck));
        return subscriptionsRepository.getAllGroupsOfUser(user.getId()).contains(idGroupForCheck);
    }

}
