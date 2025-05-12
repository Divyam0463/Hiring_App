package com.example.Service;

import com.example.Config.EmailConfig;
import com.example.DTO.EmailStatus;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class Consumer_Service{

    @Autowired
    private RabbitTemplate rabbitTemplate ;

    public void sendEmailToQueue(EmailStatus emailStatus){
        rabbitTemplate.convertAndSend(EmailConfig.EXCHANGE,EmailConfig.ROUTING_KEY,emailStatus);
    }

    public boolean isValidEmail(EmailStatus emailStatus){
        String target_email = emailStatus.getEmail();
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

        return Pattern.compile(regex,Pattern.CASE_INSENSITIVE)
                .matcher(target_email)
                .matches() ;
    }

    @RabbitListener(queues = EmailConfig.QUEUE )
    public void consumeMessageFromQueue(EmailStatus emailStatus){
        if (isValidEmail(emailStatus)){
            System.out.println("Consumed: "+emailStatus.getEmail());
        }else{
            System.out.println("❌ Invalid email: " + emailStatus.getEmail());
            throw new IllegalArgumentException("invalid email ........ sending to Dead_Queue");
        }
    }

    @RabbitListener(queues = EmailConfig.DEAD_QUEUE)
    public void putMessageInQueue(EmailStatus emailStatus){
        System.out.println("Processed in dead_queue: "+emailStatus.getEmail());
    }

}
