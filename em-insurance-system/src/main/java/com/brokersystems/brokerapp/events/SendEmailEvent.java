package com.brokersystems.brokerapp.events;

import org.springframework.context.ApplicationEvent;

import java.io.Serializable;

public class SendEmailEvent extends ApplicationEvent implements Serializable {

    private String message;
    private String email;
    private String subject;



    public SendEmailEvent(Object source, String message, String email,String subject) {
        super(source);
        this.message = message;
        this.email = email;
        this.subject = subject;
    }

    public String getEmail() {
        return email;
    }

    public String getMessage() {
        return message;
    }

    public String getSubject() {
        return subject;
    }
}
