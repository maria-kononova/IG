package com.example.ig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Properties;

@SpringBootApplication
public class IgApplication {
    String name = "IG";
    static final String MAIL_USERNAME = "mail.imperialgames@gmail.com";
    static final String MAIL_PASSWORD = "fnhc xkhn xwie njsp";
    static final Properties PROPERTIES = new Properties();
    public static void main(String[] args) {
        SpringApplication.run(IgApplication.class, args);
    }

}
