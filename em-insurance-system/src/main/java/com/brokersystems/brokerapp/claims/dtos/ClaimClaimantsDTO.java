package com.brokersystems.brokerapp.claims.dtos;

import java.math.BigDecimal;
import java.util.Date;

public class ClaimClaimantsDTO {

    private Long claimantId;
    private String thirdParty;
    private String selfClaimant;
    private String tpClaimant;
    private String claimantStatus;
    private Date createdDate;
    private String createdBy;
    private BigDecimal limitAmount;
    private String peril;
    private String perilType;

    public String getPerilType() {
        return perilType;
    }

    public void setPerilType(String perilType) {
        this.perilType = perilType;
    }

    public BigDecimal getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(BigDecimal limitAmount) {
        this.limitAmount = limitAmount;
    }

    public String getPeril() {
        return peril;
    }

    public void setPeril(String peril) {
        this.peril = peril;
    }

    public Long getClaimantId() {
        return claimantId;
    }

    public void setClaimantId(Long claimantId) {
        this.claimantId = claimantId;
    }

    public String getThirdParty() {
        return thirdParty;
    }

    public void setThirdParty(String thirdParty) {
        this.thirdParty = thirdParty;
    }

    public String getSelfClaimant() {
        return selfClaimant;
    }

    public void setSelfClaimant(String selfClaimant) {
        this.selfClaimant = selfClaimant;
    }

    public String getTpClaimant() {
        return tpClaimant;
    }

    public void setTpClaimant(String tpClaimant) {
        this.tpClaimant = tpClaimant;
    }

    public String getClaimantStatus() {
        return claimantStatus;
    }

    public void setClaimantStatus(String claimantStatus) {
        this.claimantStatus = claimantStatus;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
