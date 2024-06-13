package com.brokersystems.brokerapp.claims.dtos;

import java.math.BigDecimal;
import java.util.Date;

public class ClaimPaymentsDTO {

    private Long clmPymntId;
    private String payee;
    private String reference;
    private String paymentMode;
    private String transType;
    private String currency;
    private BigDecimal amount;
    private String status;
    private String raisedBy;
    private Date raisedDate;
    private Date authDate;
    private String authBy;

    public Long getClmPymntId() {
        return clmPymntId;
    }

    public void setClmPymntId(Long clmPymntId) {
        this.clmPymntId = clmPymntId;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRaisedBy() {
        return raisedBy;
    }

    public void setRaisedBy(String raisedBy) {
        this.raisedBy = raisedBy;
    }

    public Date getRaisedDate() {
        return raisedDate;
    }

    public void setRaisedDate(Date raisedDate) {
        this.raisedDate = raisedDate;
    }

    public Date getAuthDate() {
        return authDate;
    }

    public void setAuthDate(Date authDate) {
        this.authDate = authDate;
    }

    public String getAuthBy() {
        return authBy;
    }

    public void setAuthBy(String authBy) {
        this.authBy = authBy;
    }
}
