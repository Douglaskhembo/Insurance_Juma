package com.brokersystems.brokerapp.claims.dtos;

import java.math.BigDecimal;
import java.util.Date;

public class ClaimDetailsDTO {

    private Long clmId;
    private String claimNo;
    private String insured;
    private String lossDesc;
    private String claimStatus;
    private Date lossDate;
    private Date notificationDate;
    private Date bookedDate;
    private Date nextRvwDate;
    private Boolean liabilityAdmission;
    private String policyNo;
    private String client;
    private String product;
    private String riskId;
    private BigDecimal riskValue;
    private Date riskWef;
    private Date riskWet;

    private String causation;

    private Long riskBindId;
    private Long policyBindId;
    private Long clmRiskId;

    public Long getClmRiskId() {
        return clmRiskId;
    }

    public void setClmRiskId(Long clmRiskId) {
        this.clmRiskId = clmRiskId;
    }

    public Long getRiskBindId() {
        return riskBindId;
    }

    public void setRiskBindId(Long riskBindId) {
        this.riskBindId = riskBindId;
    }

    public Long getPolicyBindId() {
        return policyBindId;
    }

    public void setPolicyBindId(Long policyBindId) {
        this.policyBindId = policyBindId;
    }

    public String getCausation() {
        return causation;
    }

    public void setCausation(String causation) {
        this.causation = causation;
    }

    private BigDecimal totalReserve;

    private BigDecimal totalPayments;

    private BigDecimal ostReserve;

    public BigDecimal getTotalReserve() {
        return totalReserve;
    }

    public void setTotalReserve(BigDecimal totalReserve) {
        this.totalReserve = totalReserve;
    }

    public BigDecimal getTotalPayments() {
        return totalPayments;
    }

    public void setTotalPayments(BigDecimal totalPayments) {
        this.totalPayments = totalPayments;
    }

    public BigDecimal getOstReserve() {
        return ostReserve;
    }

    public void setOstReserve(BigDecimal ostReserve) {
        this.ostReserve = ostReserve;
    }

    public Long getClmId() {
        return clmId;
    }

    public void setClmId(Long clmId) {
        this.clmId = clmId;
    }

    public String getClaimNo() {
        return claimNo;
    }

    public void setClaimNo(String claimNo) {
        this.claimNo = claimNo;
    }

    public String getInsured() {
        return insured;
    }

    public void setInsured(String insured) {
        this.insured = insured;
    }

    public String getLossDesc() {
        return lossDesc;
    }

    public void setLossDesc(String lossDesc) {
        this.lossDesc = lossDesc;
    }

    public String getClaimStatus() {
        return claimStatus;
    }

    public void setClaimStatus(String claimStatus) {
        this.claimStatus = claimStatus;
    }

    public Date getLossDate() {
        return lossDate;
    }

    public void setLossDate(Date lossDate) {
        this.lossDate = lossDate;
    }

    public Date getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(Date notificationDate) {
        this.notificationDate = notificationDate;
    }

    public Date getBookedDate() {
        return bookedDate;
    }

    public void setBookedDate(Date bookedDate) {
        this.bookedDate = bookedDate;
    }

    public Date getNextRvwDate() {
        return nextRvwDate;
    }

    public void setNextRvwDate(Date nextRvwDate) {
        this.nextRvwDate = nextRvwDate;
    }

    public Boolean getLiabilityAdmission() {
        return liabilityAdmission;
    }

    public void setLiabilityAdmission(Boolean liabilityAdmission) {
        this.liabilityAdmission = liabilityAdmission;
    }

    public String getPolicyNo() {
        return policyNo;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getRiskId() {
        return riskId;
    }

    public void setRiskId(String riskId) {
        this.riskId = riskId;
    }

    public BigDecimal getRiskValue() {
        return riskValue;
    }

    public void setRiskValue(BigDecimal riskValue) {
        this.riskValue = riskValue;
    }

    public Date getRiskWef() {
        return riskWef;
    }

    public void setRiskWef(Date riskWef) {
        this.riskWef = riskWef;
    }

    public Date getRiskWet() {
        return riskWet;
    }

    public void setRiskWet(Date riskWet) {
        this.riskWet = riskWet;
    }
}
