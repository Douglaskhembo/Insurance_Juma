package com.brokersystems.brokerapp.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;

@Component
@Async
public class SendEmailEventListener implements ApplicationListener<SendEmailEvent> {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(SendEmailEvent sendEmailEvent) {
        System.out.println("Event published..");
        MimeMessagePreparator mimeMessagePreparator = new MimeMessagePreparator() {

            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {

                MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
                if(sendEmailEvent.getSubject()!=null){
                    messageHelper.setSubject(sendEmailEvent.getSubject());
                }
                messageHelper.setTo(sendEmailEvent.getEmail());
                messageHelper.setSubject("Password reset request");
                messageHelper.setText(sendEmailEvent.getMessage(), true);
                System.out.println(sendEmailEvent.getEmail());
                System.out.println(sendEmailEvent.getSubject());

            }
        };
        System.out.println("Sending...the email");
        mailSender.send(mimeMessagePreparator);
    }
}
