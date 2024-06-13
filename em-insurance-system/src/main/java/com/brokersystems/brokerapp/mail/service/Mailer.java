package com.brokersystems.brokerapp.mail.service;

import com.brokersystems.brokerapp.events.SendEmailEvent;
import com.brokersystems.brokerapp.mail.model.*;
import com.brokersystems.brokerapp.server.exception.BadRequestException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by HP on 8/9/2017.
 */
public interface Mailer {

    void sendSimpleEmail(MailMessageBean message,Long transCode, String transType) throws BadRequestException;

    void sendEmailAttachments(MailMessageBean message,Long transCode, String transType,HttpServletRequest request) throws BadRequestException;

    String getEmailReceivers(Long transCode, String transType, String receiver) throws BadRequestException;

    String getSMSReceivers(Long transCode, String transType, String receiver) throws BadRequestException;

    void sendEmail(MailMessageBean messageBean) throws BadRequestException;

    void sendSmsAttachments(MailMessageBean message,Long transCode, String transType,HttpServletRequest request) throws BadRequestException;


}
