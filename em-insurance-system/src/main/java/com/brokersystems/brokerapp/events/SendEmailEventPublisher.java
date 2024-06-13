package com.brokersystems.brokerapp.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class SendEmailEventPublisher {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void sendEmail(String message, String email, String subject){
        SendEmailEvent emailEvent = new SendEmailEvent(this,message,email, subject);
        applicationEventPublisher.publishEvent(emailEvent);
    }

}
