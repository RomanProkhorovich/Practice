package com.example.practice.util;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class AppMailSender {

    private final JavaMailSender mailSender;

    public AppMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    public void sendMessage(String to,
                            String subject,
                            String text){
        SimpleMailMessage message=new SimpleMailMessage();
        message.setSubject(subject);
        message.setText(text);
        message.setTo(to);
        mailSender.send(message);
    }
}
