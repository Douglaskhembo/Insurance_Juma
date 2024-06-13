package com.brokersystems.brokerapp.trans.dtos;

import java.math.BigDecimal;

public class PettyCashDtlsDTO {

    private Long ptdNo;
    private BigDecimal transAmount;
    private String narrative;
    private String drcr;
    private Long bankAcctId;

    public Long getPtdNo() {
        return ptdNo;
    }

    public void setPtdNo(Long ptdNo) {
        this.ptdNo = ptdNo;
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

    public String getDrcr() {
        return drcr;
    }

    public void setDrcr(String drcr) {
        this.drcr = drcr;
    }

    public Long getBankAcctId() {
        return bankAcctId;
    }

    public void setBankAcctId(Long bankAcctId) {
        this.bankAcctId = bankAcctId;
    }
}
