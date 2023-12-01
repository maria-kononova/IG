package com.example.ig.controller;

import com.example.ig.Mail;
import com.example.ig.entity.User;
import com.example.ig.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import static com.example.ig.IgApplication.BASE_URL;

@Controller
public class UserController {
    Model model;
    private final UserRepository userRepository;
    @Autowired
    public UserController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String homePage(Model model){
        model.addAttribute("users", userRepository.findAll());
        this.model = model;
        this.model.addAllAttributes(model.asMap());
        return "index";
    }

    @GetMapping("/account")
    public String accountPage(Model model){
        User user = (User) this.model.getAttribute("userLogin");
        assert user != null;
        System.out.println(user.getEmail());
        return "account";
    }

    @GetMapping("/users/{userId}")
    public String user(Model model){
        model = this.model;
        return "index";
    }

    @GetMapping("/log")
    public String accountForm(){
        return "authorization";
    }

    @PostMapping("/auth")
    public String userAuth(@RequestParam String email, @RequestParam String password, Model model) {
        User user = userRepository.findByEmail(email);
        //if (bCryptPasswordEncoder.matches(currentPassword, user.getPassword()))
        if (user.checkPassword(password)) {
            //System.err.println(passwordInput);
            model.addAttribute("userLogin", user);
            this.model.addAllAttributes(model.asMap());
            //this.model.addAllAttributes(model.asMap());
            return "index";
            //return "redirect:" + getUrl();
        }
        return "authorization";
    }
    @GetMapping("/new")
    public String showSignUpForm(Model model) {
        return "registration";
    }

    @GetMapping("/checkMail")
    @ResponseBody
    public String checkMail(Model model, @RequestParam String email) {
        for (int i = 0; i < userRepository.findAll().size(); i++) {
            if (userRepository.findAll().get(i).getEmail().equals(email)) {
                System.out.println("почта уже зарегистрирована");
                return "NotSuccess";
            }
        }
        System.out.println("почта свободна");
        return "Success";
    }


    @PostMapping("/saveNewUser")
    @ResponseBody
    public String checkMailCode(Model model, @RequestParam  String login, @RequestParam String email, @RequestParam String password){
            User user = new User(login, email, password);
            userRepository.save(user);
            System.out.println("пользователь успешно зарегистрирован");
            model.addAttribute("userLogin", user);
            //this.model.addAttribute("user", user);
            this.model.addAllAttributes(model.asMap());
            return "account";
    }

    @GetMapping("/acc")
    public String accountInput(){
        return "account";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        User user =
                userRepository
                        .findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id:" + id));
        model.addAttribute("user", user);
        this.model.addAllAttributes(model.asMap());
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
        return "redirect:" + getUrl();
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
        this.model.addAllAttributes(model.asMap());

        return "redirect:" + getUrl();
    }

    public String getUrl(String ... path){
        return UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .pathSegment(path)
                .build().toUriString();
    }
}
