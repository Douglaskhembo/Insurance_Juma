package com.brokersystems.brokerapp.mail.model;

import java.util.List;

/**
 * Created by HP on 8/10/2017.
 */
public class MailMessageBean {

    private String subject;
    private String message;
    private String sendTo;
    private String sendCC;
    private String sendBcc;
    private String receiverType;
    private List<String> reports;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSendTo() {
        return sendTo;
    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }

    public String getSendCC() {
        return sendCC;
    }

    public void setSendCC(String sendCC) {
        this.sendCC = sendCC;
    }

    public String getSendBcc() {
        return sendBcc;
    }

    public void setSendBcc(String sendBcc) {
        this.sendBcc = sendBcc;
    }

    public String getReceiverType() {
        return receiverType;
    }

    public void setReceiverType(String receiverType) {
        this.receiverType = receiverType;
    }

    public List<String> getReports() {
        return reports;
    }

    public void setReports(List<String> reports) {
        this.reports = reports;
    }

    @Override
    public String toString() {
        return "MailMessageBean{" +
                "subject='" + subject + '\'' +
                ", message='" + message + '\'' +
                ", sendTo='" + sendTo + '\'' +
                ", sendCC='" + sendCC + '\'' +
                ", sendBcc='" + sendBcc + '\'' +
                ", receiverType='" + receiverType + '\'' +
                ", reports=" + reports +
                '}';
    }
}
