package com.brokersystems.brokerapp.uw.dtos;

import java.math.BigDecimal;
import java.util.Date;

public class PolicyEnquiryDTO {

    private String polNo;
    private Date wef;
    private Date wet;
    private String user;
    private Date authDate;
    private BigDecimal premium;
    private Long transactionId;

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public String getPolNo() {
        return polNo;
    }

    public void setPolNo(String polNo) {
        this.polNo = polNo;
    }

    public Date getWef() {
        return wef;
    }

    public void setWef(Date wef) {
        this.wef = wef;
    }

    public Date getWet() {
        return wet;
    }

    public void setWet(Date wet) {
        this.wet = wet;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Date getAuthDate() {
        return authDate;
    }

    public void setAuthDate(Date authDate) {
        this.authDate = authDate;
    }

    public BigDecimal getPremium() {
        return premium;
    }

    public void setPremium(BigDecimal premium) {
        this.premium = premium;
    }
}
