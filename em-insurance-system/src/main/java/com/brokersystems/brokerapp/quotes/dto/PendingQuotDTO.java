package com.brokersystems.brokerapp.quotes.dto;

import java.math.BigDecimal;
import java.util.Date;

public class PendingQuotDTO {

    private String quotNo;
    private BigDecimal premium;
    private Date expiryDate;
    private String status;
    private Date quotDate;
    private String product;
    private Long quoteId;


    public Long getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(Long quoteId) {
        this.quoteId = quoteId;
    }

    public String getQuotNo() {
        return quotNo;
    }

    public void setQuotNo(String quotNo) {
        this.quotNo = quotNo;
    }

    public BigDecimal getPremium() {
        return premium;
    }

    public void setPremium(BigDecimal premium) {
        this.premium = premium;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getQuotDate() {
        return quotDate;
    }

    public void setQuotDate(Date quotDate) {
        this.quotDate = quotDate;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }
}
