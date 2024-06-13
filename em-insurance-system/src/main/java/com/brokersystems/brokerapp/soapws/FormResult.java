package com.brokersystems.brokerapp.soapws;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "result")
public class FormResult {


    private String code;
    private String referenceCode;
    private String message;


    @XmlElement(name = "code")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @XmlElement(name = "reference")
    public String getReferenceCode() {
        return referenceCode;
    }


    public void setReferenceCode(String referenceCode) {
        this.referenceCode = referenceCode;
    }

    @XmlElement(name = "message")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
