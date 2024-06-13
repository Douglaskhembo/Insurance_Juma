package com.brokersystems.brokerapp.claims.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by peter on 3/6/2017.
 */
public class ClaimForm {

    private List<PerilBean> perils;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lossDate;

    private Long currencyId;
    private Long paymentModeId;
    private Long bankBranchId;
    private String accountNo;

    private String comments;
    private String invoiceNo;
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date invoiceDate;

    private BigDecimal invoiceAmount;
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date notificationDate;

    private Long claimantCode;
    private Long sprCode;

    private Long riskId;

    private String riskDesc;
    private String paymentType;
    private Long bankActCode;

    private String riskShtDesc;

    private Boolean selfAsClaimant;

    private String lossDesc;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date insurerDate;

    private Long activityId;

    private String activityDesc;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date nextReviewDate;

    private Long nextReviewUser;

    private Long providerType;

    private String providerTypeDesc;

    private Long serviceProvider;

    private String serviceProviderName;
    private String reviewUser;

    private boolean liabilityAdmission;

    private String partyToBlame;

    private Long expireSectionId;

    public Long getBankActCode() {
        return bankActCode;
    }

    public void setBankActCode(Long bankActCode) {
        this.bankActCode = bankActCode;
    }

    public Long getBankBranchId() {
        return bankBranchId;
    }

    public void setBankBranchId(Long bankBranchId) {
        this.bankBranchId = bankBranchId;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    private String expireSection;

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public BigDecimal getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Long getPaymentModeId() {
        return paymentModeId;
    }

    public void setPaymentModeId(Long paymentModeId) {
        this.paymentModeId = paymentModeId;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Long getSprCode() {
        return sprCode;
    }

    public void setSprCode(Long sprCode) {
        this.sprCode = sprCode;
    }

    public Long getClaimantCode() {
        return claimantCode;
    }

    public void setClaimantCode(Long claimantCode) {
        this.claimantCode = claimantCode;
    }

    public Boolean getSelfAsClaimant() {
        return selfAsClaimant;
    }

    public void setSelfAsClaimant(Boolean selfAsClaimant) {
        this.selfAsClaimant = selfAsClaimant;
    }

    public List<PerilBean> getPerils() {
        return perils;
    }

    public void setPerils(List<PerilBean> perils) {
        this.perils = perils;
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

    public Long getRiskId() {
        return riskId;
    }

    public void setRiskId(Long riskId) {
        this.riskId = riskId;
    }

    public String getLossDesc() {
        return lossDesc;
    }

    public void setLossDesc(String lossDesc) {
        this.lossDesc = lossDesc;
    }

    public Date getInsurerDate() {
        return insurerDate;
    }

    public void setInsurerDate(Date insurerDate) {
        this.insurerDate = insurerDate;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Date getNextReviewDate() {
        return nextReviewDate;
    }

    public void setNextReviewDate(Date nextReviewDate) {
        this.nextReviewDate = nextReviewDate;
    }

    public Long getNextReviewUser() {
        return nextReviewUser;
    }

    public void setNextReviewUser(Long nextReviewUser) {
        this.nextReviewUser = nextReviewUser;
    }

    public boolean isLiabilityAdmission() {
        return liabilityAdmission;
    }

    public void setLiabilityAdmission(boolean liabilityAdmission) {
        this.liabilityAdmission = liabilityAdmission;
    }

    public String getPartyToBlame() {
        return partyToBlame;
    }

    public void setPartyToBlame(String partyToBlame) {
        this.partyToBlame = partyToBlame;
    }

    public String getRiskDesc() {
        return riskDesc;
    }

    public void setRiskDesc(String riskDesc) {
        this.riskDesc = riskDesc;
    }

    public String getActivityDesc() {
        return activityDesc;
    }

    public void setActivityDesc(String activityDesc) {
        this.activityDesc = activityDesc;
    }

    public String getReviewUser() {
        return reviewUser;
    }

    public void setReviewUser(String reviewUser) {
        this.reviewUser = reviewUser;
    }

    public String getRiskShtDesc() {
        return riskShtDesc;
    }

    public void setRiskShtDesc(String riskShtDesc) {
        this.riskShtDesc = riskShtDesc;
    }

    public Long getProviderType() {
        return providerType;
    }

    public void setProviderType(Long providerType) {
        this.providerType = providerType;
    }

    public String getProviderTypeDesc() {
        return providerTypeDesc;
    }

    public void setProviderTypeDesc(String providerTypeDesc) {
        this.providerTypeDesc = providerTypeDesc;
    }

    public Long getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(Long serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public String getServiceProviderName() {
        return serviceProviderName;
    }

    public void setServiceProviderName(String serviceProviderName) {
        this.serviceProviderName = serviceProviderName;
    }

    public String getExpireSection() {
        return expireSection;
    }

    public void setExpireSection(String expireSection) {
        this.expireSection = expireSection;
    }

    public Long getExpireSectionId() {
        return expireSectionId;
    }

    public void setExpireSectionId(Long expireSectionId) {
        this.expireSectionId = expireSectionId;
    }

    @Override
    public String toString() {

        return "ClaimForm{" +
                "partyToBlame='" + partyToBlame + '\'' +
                ", lossDate=" + lossDate +
                ", notificationDate=" + notificationDate +
                ", riskId=" + riskId +
                ", lossDesc='" + lossDesc + '\'' +
                ", insurerDate=" + insurerDate +
                ", activityId=" + activityId +
                ", nextReviewDate=" + nextReviewDate +
                ", nextReviewUser=" + nextReviewUser +
                ", liabilityAdmission=" + liabilityAdmission +
                ", providerType=" + providerType +
                ", serviceProvider=" + serviceProvider +
                ", expireSectionId=" + expireSectionId +
                ", expireSection=" + expireSection +
                '}';
    }
}
