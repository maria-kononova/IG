package com.example.ig;

import com.example.ig.entity.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.ui.Model;

import java.util.Collection;
import java.util.Map;
import java.util.Properties;

@SpringBootApplication
public class IgApplication {
    String name = "IG";
    static final String MAIL_USERNAME = "mail.imperialgames@gmail.com";
    static final String MAIL_PASSWORD = "fnhc xkhn xwie njsp";
    static final Properties PROPERTIES = new Properties();
    public static  final String BASE_URL = "http://localhost:8080/";
    public static User user;
    public static void main(String[] args) {
        SpringApplication.run(IgApplication.class, args);

    }

}
