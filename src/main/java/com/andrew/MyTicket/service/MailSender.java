package com.andrew.MyTicket.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;

@Service
public class MailSender {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String username;

    public void send(String emailTo, String subject, String message) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setFrom(username);
        simpleMailMessage.setTo(emailTo);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);

        javaMailSender.send(simpleMailMessage);

    }


    public void sendPdf(String emailTo, String subject, String fileName) throws MessagingException, IOException {
        Multipart multipart = new MimeMultipart();

        MimeBodyPart attachPart = new MimeBodyPart();
        String attachFile = "C:\\Users\\Andrew\\IdeaProjects\\NewProjects\\MyTicket\\src\\main\\resources\\tempPdf\\"+fileName+".pdf";
        attachPart.attachFile(attachFile);
        multipart.addBodyPart(attachPart);


        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        mimeMessage.setFrom(username);
        mimeMessage.setRecipients(Message.RecipientType.TO, emailTo);
        mimeMessage.setSubject(subject);
        mimeMessage.setContent(multipart);
        javaMailSender.send(mimeMessage);
        File file = new File(attachFile);
        file.delete();
    }
}
