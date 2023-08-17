package com.example.practice.util;

import com.example.practice.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import static com.example.practice.util.AuthUtil.getRolesFromAuthServer;

@Service
public class AppMailSender {

    private final JavaMailSender mailSender;

    public AppMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    public void sendMessage(String to,
                            String subject,
                            String text,
                            HttpServletRequest req){
        var res=getRolesFromAuthServer(req);
        if (!res.isAuthenticated()|| !res.getRole().equals("ADMIN"))
            throw new UserNotFoundException();


        SimpleMailMessage message=new SimpleMailMessage();
        message.setSubject(subject);
        message.setText(text);
        message.setTo(to);
        mailSender.send(message);
    }
}
