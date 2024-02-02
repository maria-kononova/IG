package com.example.ig.controller;

import com.example.ig.entity.Likes;
import com.example.ig.entity.Post;
import com.example.ig.entity.User;
import com.example.ig.repository.GroupRepository;
import com.example.ig.repository.LikesRepository;
import com.example.ig.repository.PostRepository;
import com.example.ig.repository.UserRepository;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.example.ig.IgApplication.*;

@Controller
public class UserController {
    Model model;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final PostRepository postRepository;
    private final LikesRepository likesRepository;
    private static final String FILE_IMAGE = "src/main/resources/image/";
    //авбдыаы
    @Autowired
    public UserController(UserRepository userRepository, GroupRepository groupRepository, PostRepository postRepository, LikesRepository likesRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.postRepository = postRepository;
        this.likesRepository = likesRepository;
    }
    @GetMapping("/")
    public String homePage(Model model, HttpServletRequest request, HttpServletResponse response) {
        if (user == null) {
            Long userId = getUserFromCookie(request, response);
            if (userId != null) user = userRepository.getById(userId);
        } else {
            model.addAttribute("users", userRepository.findAll());
        }
        model.addAttribute("userLogin",user);
        model.addAttribute("posts", postRepository.findAll());
        model.addAttribute("groups", groupRepository.findAll());
        model.addAttribute("likes", likesRepository.findAll());
        return "index";
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

    @GetMapping("/news")
    public String newsPage(Model model) {
        return "index";
    }

    @GetMapping("/exit")
    public String exit(Model model, HttpServletResponse response, HttpServletRequest request) {
        user = null;
        model.addAttribute("userLogin", null);
        Cookie[] cookies = request.getCookies();
        String cookieName = "userId";
        Long userIdFromCookie = null;
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (cookieName.equals(c.getName())) {
                    response.addCookie(new Cookie("userId", ""));
                    break;
                }
            }
        }
        model.addAttribute("posts", postRepository.findAll());
        model.addAttribute("groups", groupRepository.findAll());
        return "index";
    }

    @GetMapping("/acc")
    public String accountInput() {
        return "account";
    }

    @GetMapping("/account")
    public String accountPage(Model model, HttpServletRequest request, HttpServletResponse response) {
        Long userId = getUserFromCookie(request, response);
        if (userId != null) user = userRepository.getById(userId);
        model.addAttribute("groups", groupRepository.findAll());
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
    public String userAuth(Model model, @RequestParam String email, @RequestParam String password, HttpServletResponse response) {
        User user1 = userRepository.findByEmail(email);
        if (user1.checkPassword(password)) {
            user = user1;
            model.addAttribute("userLogin", user);
            response.addCookie(new Cookie("userId", String.valueOf(user.getId())));
            System.out.println(user.getLogin());
            return "Success";
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
    public String checkMailCode(Model model, @RequestParam String login, @RequestParam String email, @RequestParam String password, HttpServletResponse response) {
        user = new User(login, email, password);
        userRepository.save(user);
        System.out.println("пользователь успешно зарегистрирован");
        model.addAttribute("userLogin", user);
        response.addCookie(new Cookie("userId", String.valueOf(user.getId())));
        //this.model.addAttribute("user", user);
        return "account";
    }

    @PostMapping("/changeImg")
    @ResponseBody
    public String changeImg(Model model, @RequestParam String url){
        //copyDirectory(new File(getUrl(url)), file);
        user.setImg(url);
        userRepository.save(user);
        model.addAttribute("userLogin", user);
        return "account";
    }

    public void copyDirectory(File sourceLocation, File targetLocation) throws IOException {
        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }

            String[] children = sourceLocation.list();
            for (int i = 0; i < children.length; i++) {
                copyDirectory(new File(sourceLocation, children[i]),
                        new File(targetLocation, children[i]));
            }
        } else {

            InputStream in = new FileInputStream(sourceLocation);
            OutputStream out = new FileOutputStream(targetLocation);

            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
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

    @PostMapping("/updatePassword")
    @ResponseBody
    public String updatePassword(@RequestParam String oldPassword, @RequestParam String newPassword, Model model) {
        if (!oldPassword.equals(newPassword)) {
            if (user.checkPassword(oldPassword)) {
                user.updatePassword(newPassword);
                userRepository.save(user);
            } else {
                return "notValid";
            }
        } else {
            return "Match";
        }
        return "Success";
    }

    @PostMapping("/update/{id}")
    public String updateUser(
            @PathVariable("id") long id, @RequestParam String login, @RequestParam String email, Model model) {
        if (!user.getLogin().equals(login)) {
            user.setLogin(login);
            userRepository.save(user);
            model.addAttribute("userLogin", user);
        }
        return "account";
    }

    public String getUrl(String... path) {
        return UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .pathSegment(path)
                .build().toUriString();
    }

}
