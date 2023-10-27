package com.example.ig;

import org.apache.commons.io.IOUtils;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Random;

import static com.example.ig.IgApplication.*;

public class Mail {
    public Mail(){
        PROPERTIES.put("mail.smtp.host", "smtp.gmail.com"); // Замените на адрес вашего почтового сервера
        PROPERTIES.put("mail.smtp.port", "587"); // Замените на порт вашего почтового сервера
        PROPERTIES.put("mail.smtp.auth", "true"); // Установите в "true", если требуется аутентификация
        PROPERTIES.put("mail.smtp.starttls.enable", "true"); // Установите в "true", если требуется TLS

    }

    public void sendMail(String fileName, String email, String subject){
        try {
            // Создание объекта сообщения
            Message message = new MimeMessage(createSession());
            message.setFrom(new InternetAddress(MAIL_USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject(subject);
            StringWriter writer = new StringWriter();
            IOUtils.copy(new FileInputStream("src/main/resources/mailForms/" + fileName), writer);

            message.setContent(writer.toString(), "text/html; charset=utf-8");
            //message.setText("Здравствуйте! \n\nСпасибо за регистрацию на нашем сайте IG.\n\nДля подтверждения почтового адреса введите на сайте следующий код:\n\n"+generateCode()); // Замените на текст вашего письма

            // Отправка сообщения
            Transport.send(message);

            System.out.println("Письмо успешно отправлено.");
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Session createSession(){
        //Создание объекта сессии
        return Session.getInstance(PROPERTIES,
                new javax.mail.Authenticator() {
                    protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                        return new javax.mail.PasswordAuthentication(MAIL_USERNAME, MAIL_PASSWORD);
                    }
                });
    }

    public String generateCode(){
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(0, 10));
        }
        return sb.toString();
    }
}
