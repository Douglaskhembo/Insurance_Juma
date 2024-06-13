package com.brokersystems.brokerapp.trans.dtos;

import java.math.BigDecimal;

public class ChequeTransDtlsDTO {

    private Long ctdNo;
    private BigDecimal transAmount;
    private String narrative;
    private Long glId;
    private String drcr;
    private Long branchCode;
    private String glAcctNo;
    private String glAcctName;
    private String branchName;

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getGlAcctNo() {
        return glAcctNo;
    }

    public void setGlAcctNo(String glAcctNo) {
        this.glAcctNo = glAcctNo;
    }

    public String getGlAcctName() {
        return glAcctName;
    }

    public void setGlAcctName(String glAcctName) {
        this.glAcctName = glAcctName;
    }

    public Long getCtdNo() {
        return ctdNo;
    }

    public void setCtdNo(Long ctdNo) {
        this.ctdNo = ctdNo;
    }

    public BigDecimal getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(BigDecimal transAmount) {
        this.transAmount = transAmount;
    }

    public String getNarrative() {
        return narrative;
    }

    public void setNarrative(String narrative) {
        this.narrative = narrative;
    }

    public Long getGlId() {
        return glId;
    }

    public void setGlId(Long glId) {
        this.glId = glId;
    }

    public String getDrcr() {
        return drcr;
    }

    public void setDrcr(String drcr) {
        this.drcr = drcr;
    }

    public Long getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(Long branchCode) {
        this.branchCode = branchCode;
    }

    @Override
    public String toString() {
        return "ChequeTransDtlsDTO{" +
                "ctdNo=" + ctdNo +
                ", transAmount=" + transAmount +
                ", narrative='" + narrative + '\'' +
                ", glId=" + glId +
                ", drcr='" + drcr + '\'' +
                ", branchCode=" + branchCode +
                '}';
    }
}
