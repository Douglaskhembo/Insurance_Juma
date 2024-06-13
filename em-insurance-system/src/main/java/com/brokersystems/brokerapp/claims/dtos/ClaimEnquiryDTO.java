package com.brokersystems.brokerapp.claims.dtos;

import java.util.Date;

public class ClaimEnquiryDTO {

    private Long clmId;
    private String username;
    private Date lossDate;
    private Date clmDate;
    private String clmStatus;
    private String riskId;
    private Date nextRevDate;
    private String policyNo;
    private String insuredName;
    private String claimNo;
    private String insuredDate;

    public String getInsuredName() {
        return insuredName;
    }

    public void setInsuredName(String insuredName) {
        this.insuredName = insuredName;
    }

    public String getClaimNo() {
        return claimNo;
    }

    public void setClaimNo(String claimNo) {
        this.claimNo = claimNo;
    }

    public Long getClmId() {
        return clmId;
    }

    public void setClmId(Long clmId) {
        this.clmId = clmId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getLossDate() {
        return lossDate;
    }

    public void setLossDate(Date lossDate) {
        this.lossDate = lossDate;
    }

    public Date getClmDate() {
        return clmDate;
    }

    public void setClmDate(Date clmDate) {
        this.clmDate = clmDate;
    }

    public String getClmStatus() {
        return clmStatus;
    }

    public void setClmStatus(String clmStatus) {
        this.clmStatus = clmStatus;
    }

    public String getRiskId() {
        return riskId;
    }

    public void setRiskId(String riskId) {
        this.riskId = riskId;
    }

    public Date getNextRevDate() {
        return nextRevDate;
    }

    public void setNextRevDate(Date nextRevDate) {
        this.nextRevDate = nextRevDate;
    }

    public String getPolicyNo() {
        return policyNo;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    public String getInsuredDate() {
        return insuredDate;
    }

    public void setInsuredDate(String insuredDate) {
        this.insuredDate = insuredDate;
    }
}
