package com.example.ig.controller;

import com.example.ig.entity.User;
import com.example.ig.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
    private final UserRepository userRepository;
    @Autowired
    public UserController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String homePage(Model model){
        model.addAttribute("users", userRepository.findAll());
        return "index";
    }

    @GetMapping("/log")
    public String accountForm(){
        return "account";
    }
    @PostMapping("/auth")
    public String userAuth(@RequestParam String email, @RequestParam String password, Model model) {
        User user = userRepository.findByEmail(email);
        //if (bCryptPasswordEncoder.matches(currentPassword, user.getPassword()))
        if (user.getPassword().equals(password)){
            System.err.println(password);
            model.addAttribute("userLogin", user);
            return "index";
        }
        System.err.println(password);
        return "account";
    }
    @GetMapping("/new")
    public String showSignUpForm(Model model) {
        return "add-user";
    }
    @PostMapping("/new")
    public String postAdd(@RequestParam String login, @RequestParam String email, @RequestParam String password, Model model ){
        User user = new User(login, email, password);
        userRepository.save(user);
        return "index";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        User user =
                userRepository
                        .findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id:" + id));
        model.addAttribute("user", user);
        return "update-user";
    }
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) {
        User user =
                userRepository
                        .findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id:" + id));
        userRepository.delete(user);
        model.addAttribute("user", userRepository.findAll());
        return "index";
    }

    @PostMapping("/update/{id}")
    public String updateUser(
            @PathVariable("id") long id, @Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            user.setId(id);
            return "update-user";
        }
        userRepository.save(user);
        model.addAttribute("user", userRepository.findAll());
        return "index";
    }
}
