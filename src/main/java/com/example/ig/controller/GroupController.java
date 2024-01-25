package com.example.ig.controller;

import com.example.ig.entity.Group;
import com.example.ig.repository.GroupRepository;
import com.example.ig.repository.SubscriptionsRepository;
import com.example.ig.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

import static com.example.ig.IgApplication.user;

@Controller
public class GroupController {
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final SubscriptionsRepository subscriptionsRepository;

    @Autowired
    public GroupController(GroupRepository groupRepository, UserRepository userRepository, SubscriptionsRepository subscriptionsRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.subscriptionsRepository = subscriptionsRepository;
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
        if (userId != null) user = userRepository.getById(userId);
        model.addAttribute("groups", groupRepository.findAll());
        model.addAttribute("userLogin", user);
        model.addAttribute("group", groupRepository.getById(id));
        return "group";
    }
    @GetMapping("/groups")
    public String groupPage( Model model, HttpServletRequest request, HttpServletResponse response) {
        Long userId = getUserFromCookie(request, response);
        if (userId != null) user = userRepository.getById(userId);
        model.addAttribute("groups", groupRepository.findAll());
        model.addAttribute("userLogin", user);
        model.addAttribute("groups", groupRepository.findAll());
        model.addAttribute("myGroups", getMyGroup(subscriptionsRepository.getAllGroupsOfUser(user.getId())));
        return "groups";
    }

    public List<Group> getMyGroup(List<Long> myGroupsLong){
        List<Group> myGroups = new ArrayList<>();
        for(long idGroup : myGroupsLong){
            myGroups.add(groupRepository.getById(idGroup));
        }
        return myGroups;
    }
}
