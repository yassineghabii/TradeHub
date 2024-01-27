package com.example.pifinance_back.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailSenderService {
    @Autowired
    private JavaMailSender mailSender;
    public void sendSimpleEmail(String toEmail, String htmlBody, String subject) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHealper = new MimeMessageHelper(mimeMessage,true);
        mimeMessageHealper.setFrom("mohamedaziz.jaziri1@esprit.tn");
        mimeMessageHealper.setTo(toEmail);
        mimeMessageHealper.setText(htmlBody,true);
        mimeMessageHealper.setSubject(subject);
        mailSender.send(mimeMessage);
        System.out.println("Mail sent successfully!");
    }
}
