package com.brokersystems.brokerapp.quotes.dto;

import java.math.BigDecimal;

public class QuotTaxesDTO {

    private Long polTaxId;
    private String revItemCode;
    private BigDecimal taxAmount;
    private BigDecimal taxRate;
    private BigDecimal divFactor;
    private String rateType;
    private String taxLevel;
    private String quotStatus;

    public Long getPolTaxId() {
        return polTaxId;
    }

    public void setPolTaxId(Long polTaxId) {
        this.polTaxId = polTaxId;
    }

    public String getRevItemCode() {
        return revItemCode;
    }

    public void setRevItemCode(String revItemCode) {
        this.revItemCode = revItemCode;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimal getDivFactor() {
        return divFactor;
    }

    public void setDivFactor(BigDecimal divFactor) {
        this.divFactor = divFactor;
    }

    public String getRateType() {
        return rateType;
    }

    public void setRateType(String rateType) {
        this.rateType = rateType;
    }

    public String getTaxLevel() {
        return taxLevel;
    }

    public void setTaxLevel(String taxLevel) {
        this.taxLevel = taxLevel;
    }

    public String getQuotStatus() {
        return quotStatus;
    }

    public void setQuotStatus(String quotStatus) {
        this.quotStatus = quotStatus;
    }
}
