package com.example.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;

@Service
@Slf4j
public class Mail_Service {

    @Autowired
    private JavaMailSender javaMailSender  ;

    @Autowired
    private TemplateEngine templateEngine ;

    public void sendEmail(String To, String Name, String status, File attachment) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage() ;

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage) ;

        mimeMessageHelper.setSubject("Job Offer from Our Company");
        mimeMessageHelper.setTo(To);

        //Prepare template Content
        Context context = new Context() ;
        context.setVariable("name", Name);
        context.setVariable("status", status);
        String htmlContent = templateEngine.process("Job_Offer", context) ;

        mimeMessageHelper.setText(htmlContent,true);

        boolean hasAttachment = false ;

        if (attachment!=null){
            hasAttachment = attachment.exists();
        }
        //adding file attachment to the template
        if (hasAttachment){
            FileSystemResource fileSystemResource = new FileSystemResource(attachment) ;
            mimeMessageHelper.addAttachment(attachment.getName(), fileSystemResource);
        }

        javaMailSender.send(mimeMessage);

        log.info("Job Offer sent to: {}",To);
    }
}
