package com.example.ig.repository;

import org.springframework.ui.Model;

import javax.mail.Session;

public interface MailRepository {
     void sendMail(String fileName, String email, String subject, Model model);
     Session createSession();
     String generateCode();
}
