package com.example.ig.controller;

import com.example.ig.Mail;
import com.example.ig.repository.MailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
@Controller
public class MailController {
    @RequestMapping(value="/sendMailCode", method= RequestMethod.POST)
    public String sendMail(Model model){
        //JOptionPane.showMessageDialog(null, "My Goodness, this is so concise");
        Mail mail = new Mail();
        mail.setCode();
        System.err.println(mail.getCode());
        model.addAttribute("mail", mail);
        mail.sendMail("sendCodeConfirm.html", "515nonia515@gmail.com", "IG Подтверждение почты", model);
        return "index";
    }
}
