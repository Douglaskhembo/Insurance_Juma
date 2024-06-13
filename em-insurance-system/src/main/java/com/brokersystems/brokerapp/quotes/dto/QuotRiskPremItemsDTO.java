package com.brokersystems.brokerapp.quotes.dto;

import java.math.BigDecimal;

public class QuotRiskPremItemsDTO {

    private Long sectId;
    private BigDecimal prem;
    private BigDecimal amount;
    private BigDecimal rate;
    private BigDecimal divFactor;
    private BigDecimal freeLimit;
    private BigDecimal annualEarnings;
    private Long secId;
    private Long rateId;
    private Long riskId;
    private String secName;
    private String quotStatus;

    public BigDecimal getAnnualEarnings() {
        return annualEarnings;
    }

    public void setAnnualEarnings(BigDecimal annualEarnings) {
        this.annualEarnings = annualEarnings;
    }

    public Long getRiskId() {
        return riskId;
    }

    public void setRiskId(Long riskId) {
        this.riskId = riskId;
    }

    public Long getRateId() {
        return rateId;
    }

    public void setRateId(Long rateId) {
        this.rateId = rateId;
    }

    public String getQuotStatus() {
        return quotStatus;
    }

    public void setQuotStatus(String quotStatus) {
        this.quotStatus = quotStatus;
    }

    public Long getSectId() {
        return sectId;
    }

    public void setSectId(Long sectId) {
        this.sectId = sectId;
    }

    public BigDecimal getPrem() {
        return prem;
    }

    public void setPrem(BigDecimal prem) {
        this.prem = prem;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getDivFactor() {
        return divFactor;
    }

    public void setDivFactor(BigDecimal divFactor) {
        this.divFactor = divFactor;
    }

    public BigDecimal getFreeLimit() {
        return freeLimit;
    }

    public void setFreeLimit(BigDecimal freeLimit) {
        this.freeLimit = freeLimit;
    }

    public Long getSecId() {
        return secId;
    }

    public void setSecId(Long secId) {
        this.secId = secId;
    }

    public String getSecName() {
        return secName;
    }

    public void setSecName(String secName) {
        this.secName = secName;
    }
}
