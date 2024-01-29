package com.example.ig.controller;

import com.example.ig.entity.Group;
import com.example.ig.entity.GroupUser;
import com.example.ig.entity.Subscriptions;
import com.example.ig.repository.GroupRepository;
import com.example.ig.repository.PostRepository;
import com.example.ig.repository.SubscriptionsRepository;
import com.example.ig.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.example.ig.IgApplication.group;
import static com.example.ig.IgApplication.user;

@Controller
public class GroupController {
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final SubscriptionsRepository subscriptionsRepository;
    private final PostRepository postRepository;

    @Autowired
    public GroupController(GroupRepository groupRepository, UserRepository userRepository, SubscriptionsRepository subscriptionsRepository, PostRepository postRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.subscriptionsRepository = subscriptionsRepository;
        this.postRepository = postRepository;
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
        model.addAttribute("groups", groupRepository.findAll());
        model.addAttribute("group", group);
        model.addAttribute("posts", postRepository.getAllPostsOfGroup(group.getId()));
        return "group";
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

    @PostMapping("/searchGroup")
    @ResponseBody
    public String searchGroup(Model model, @RequestParam String query){
        List<Group> groupsQuery = new ArrayList<>();
        for(Group gr : groupRepository.findAll()){
            if(gr.getName().toLowerCase().contains(query)){
                groupsQuery.add(gr);
                System.out.println(gr.getName());
            }
        }
        System.out.println("ok");
        //if(query.equals("")) { groupsQuery = groupRepository.findAll();}
        model.addAttribute("groups", groupsQuery);
        System.out.println(model.getAttribute("groups"));
        return "Success";
    }


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
