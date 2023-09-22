package com.example.ig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IgApplication {
    String name = "IG";
    public static void main(String[] args) {
        SpringApplication.run(IgApplication.class, args);
    }

}
