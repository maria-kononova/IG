package com.example.ig.controller;

import com.example.ig.Mail;
import com.example.ig.repository.MailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MailController {
    Model model;
    @RequestMapping(value="/sendMailCode", method= RequestMethod.POST)
    public void sendMail(Model model){
        this.model = model;
        Mail mail = new Mail();
        mail.setCode();
        System.err.println(mail.getCode());
        model.addAttribute("mail", mail);
        mail.sendMail("sendCodeConfirm.html", "515nonia515@gmail.com", "IG Подтверждение почты", model);
    }

    @RequestMapping(value="/send-email", method= RequestMethod.POST)
    @ResponseBody
    public String sendMail(@RequestParam String email, Model model) {
        this.model = model;
        Mail mail = new Mail();
        mail.setCode();
        System.err.println(mail.getCode());
        model.addAttribute("mail", mail);
        mail.sendMail("sendCodeConfirm.html", email, "IG Подтверждение почты", model);
        return "Success";
    }
    @PostMapping("/checkMailCode")
    @ResponseBody
    public String checkMailCOde(Model model, @RequestParam String code){
        Mail mail = (Mail) this.model.getAttribute("mail");

        if (code.equals(mail != null ? mail.getCode() : null)){
            System.out.println("true");
        }
        else System.out.println("false");
        return "Success";
    }
}
