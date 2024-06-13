package com.brokersystems.brokerapp.mail.model;

/**
 * Created by HP on 8/10/2017.
 */
public enum MailTemplates {

    POLICY_TEMPLATE("mail_templates/policy_template.vm"),
    QUOTE_TEMPLATE("mail_templates/quote_template.vm"),
    QUOTE_SMS_TEMPLATE("mail_templates/quote_sms_template.vm"),
    CLAIMS_TEMPLATE("mail_templates/claims_template.vm"),
    EMAIL_TEMPLATE("mail_templates/forgot_password.vm"),
    EMAIL_RESET_TEMPLATE("mail_templates/reset_password.vm"),
    MED_CLAIMS_TEMPLATE("mail_templates/med_claims_template.vm"),
    CERT_ALLOC_TEMPLATE("mail_templates/cert_alloc_template.vm"),
    CLAIMS_REMINDER_TEMPLATE("mail_templates/claims_reminder_template.vm");

    private String fileName;

    MailTemplates(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
