package com.example.ig.controller;

import com.example.ig.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GroupController {
    private final GroupRepository groupRepository;
    @Autowired
    public GroupController(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }
    @GetMapping("/group")
    public String groupPage(Model model){
        model.addAttribute("groups", groupRepository.findAll());
        return "groups";
    }
}
