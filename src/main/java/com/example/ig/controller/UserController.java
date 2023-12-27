package com.example.ig.controller;

import com.example.ig.IgApplication;
import com.example.ig.Mail;
import com.example.ig.entity.User;
import com.example.ig.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.ig.IgApplication.BASE_URL;
import static com.example.ig.IgApplication.user;

@Controller
public class UserController {
    Model model;
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String homePage(Model model) {
        if(user == null){
            model.addAttribute("users", userRepository.findAll());
            this.model = model;
        }
        model.addAttribute("userLogin", user);
        return "index";
    }

    @GetMapping("/news")
    public String newsPage(Model model) {
        return "index";
    }

    @GetMapping("/exit")
    public String exit(Model model) {
        user = null;
        model.addAttribute("userLogin", null);
        return "index";
    }

    @GetMapping("/acc")
    public String accountInput() {
        return "account";
    }

    @GetMapping("/account")
    public String accountPage(Model model, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String data = "";
        if (cookies != null) {
            data = Arrays.stream(cookies)
                    .map(c -> c.getName() + "=" + c.getValue()).collect(Collectors.joining(", "));
        }
        model.addAttribute("userLogin", user);
        return "account";
    }

    @GetMapping("/users/{userId}")
    public String user(Model model) {
        return "index";
    }


    @GetMapping("/log")
    public String accountForm() {
        return "authorization";
    }

    @GetMapping("/auth")
    @ResponseBody
    public String userAuth(Model model, @RequestParam String email, @RequestParam String password) {
        User user1 = userRepository.findByEmail(email);
        System.out.println("тут");
        //if (bCryptPasswordEncoder.matches(currentPassword, user.getPassword()))
        if (user1.checkPassword(password)) {
            //System.err.println(passwordInput);
            user = user1;
            model.addAttribute("userLogin", user);
            /*Cookie cookie = new Cookie("username", user.getLogin());
            cookie.setAttribute("email", user.getEmail());

            //add a cookie to the response
            response.addCookie(cookie);*/
            System.out.println(user.getLogin());
            //this.model.addAttribute("userLogin", user);
            return "Success";
            //return "redirect:" + getUrl();
        }
        return "NoSuccess";
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
    public String checkMailCode(Model model, @RequestParam String login, @RequestParam String email, @RequestParam String password) {
        User user = new User(login, email, password);
        userRepository.save(user);
        System.out.println("пользователь успешно зарегистрирован");
        model.addAttribute("userLogin", user);
        //this.model.addAttribute("user", user);
        this.model.addAllAttributes(model.asMap());
        return "account";
    }


    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        user =
                userRepository
                        .findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id:" + id));
        model.addAttribute("user", user);
        return "update-user";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) {
        user =
                userRepository
                        .findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id:" + id));
        userRepository.delete(user);
        model.addAttribute("user", userRepository.findAll());
        return "redirect:" + getUrl();
    }

    @PostMapping("/update/{id}")
    public String updateUser(
            @PathVariable("id") long id, @RequestParam String login, @RequestParam String email, Model model) {
        System.out.println(id);
        System.out.println(email);
        System.out.println(login);
        user = userRepository.getById(id);
        user.setLogin(login);
        userRepository.save(user);
        model.addAttribute("userLogin", user);
        System.out.println(user.getId());
        /*if (result.hasErrors()) {
            user.setId(id);
            return "account";
        }
        userRepository.save(user);
        model.addAttribute("user", user);*/
        return "account";
    }

    public String getUrl(String... path) {
        return UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .pathSegment(path)
                .build().toUriString();
    }

}
