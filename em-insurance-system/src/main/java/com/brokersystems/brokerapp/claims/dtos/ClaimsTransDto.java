package com.brokersystems.brokerapp.claims.dtos;

import java.math.BigDecimal;
import java.util.Date;

public class ClaimsTransDto {

    private Long transId;
    private BigDecimal transAmount;
    private Date transDate;
    private String transType;
    private Date authDate;
    private String createdBy;
    private String authBy;
    private String transStatus;

    public Long getTransId() {
        return transId;
    }

    public void setTransId(Long transId) {
        this.transId = transId;
    }

    public BigDecimal getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(BigDecimal transAmount) {
        this.transAmount = transAmount;
    }

    public Date getTransDate() {
        return transDate;
    }

    public void setTransDate(Date transDate) {
        this.transDate = transDate;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public Date getAuthDate() {
        return authDate;
    }

    public void setAuthDate(Date authDate) {
        this.authDate = authDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getAuthBy() {
        return authBy;
    }

    public void setAuthBy(String authBy) {
        this.authBy = authBy;
    }

    public String getTransStatus() {
        return transStatus;
    }

    public void setTransStatus(String transStatus) {
        this.transStatus = transStatus;
    }
}
