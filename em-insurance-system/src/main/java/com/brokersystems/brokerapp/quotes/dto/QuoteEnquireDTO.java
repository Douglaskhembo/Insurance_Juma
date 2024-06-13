package com.brokersystems.brokerapp.quotes.dto;

import java.util.Date;

public class QuoteEnquireDTO {

    private Long quoteId;
    private String quotNo;
    private String quotRevNo;
    private Date wefDate;
    private Date wetDate;
    private String client;
    private String curIsoCode;
    private String quotStatus;
    private String username;

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

    public String getQuotRevNo() {
        return quotRevNo;
    }

    public void setQuotRevNo(String quotRevNo) {
        this.quotRevNo = quotRevNo;
    }

    public Date getWefDate() {
        return wefDate;
    }

    public void setWefDate(Date wefDate) {
        this.wefDate = wefDate;
    }

    public Date getWetDate() {
        return wetDate;
    }

    public void setWetDate(Date wetDate) {
        this.wetDate = wetDate;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getCurIsoCode() {
        return curIsoCode;
    }

    public void setCurIsoCode(String curIsoCode) {
        this.curIsoCode = curIsoCode;
    }

    public String getQuotStatus() {
        return quotStatus;
    }

    public void setQuotStatus(String quotStatus) {
        this.quotStatus = quotStatus;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
