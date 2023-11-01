package com.example.ig.controller;

import com.example.ig.Mail;
import com.example.ig.repository.MailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MailController {
    Model model;
    @RequestMapping(value="/sendMailCode", method= RequestMethod.POST)
    public String sendMail(Model model){
        this.model = model;
        //JOptionPane.showMessageDialog(null, "My Goodness, this is so concise");
        Mail mail = new Mail();
        mail.setCode();
        System.err.println(mail.getCode());
        model.addAttribute("mail", mail);
        mail.sendMail("sendCodeConfirm.html", "515nonia515@gmail.com", "IG Подтверждение почты", model);
        return "index";
    }
    @PostMapping("/checkMailCode")
    public String checkMailCOde(Model model, @RequestParam String code){
        Mail mail = (Mail) this.model.getAttribute("mail");

        if (code.equals(mail != null ? mail.getCode() : null)){
            System.out.println("true");
        }
        else System.out.println("false");
        return "index";
    }
}
