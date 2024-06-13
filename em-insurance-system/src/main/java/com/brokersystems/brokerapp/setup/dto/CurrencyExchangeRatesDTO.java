package com.brokersystems.brokerapp.setup.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.math.BigDecimal;
import java.util.Date;

public class CurrencyExchangeRatesDTO {

    private Long ceCode;
    private BigDecimal rate;
    private Date createdDate;
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date exchangeDate;
    private Long curCode;
    private Long baseCurCode;
    private String currency;

    public Long getBaseCurCode() {
        return baseCurCode;
    }

    public void setBaseCurCode(Long baseCurCode) {
        this.baseCurCode = baseCurCode;
    }

    public Long getCeCode() {
        return ceCode;
    }

    public void setCeCode(Long ceCode) {
        this.ceCode = ceCode;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getExchangeDate() {
        return exchangeDate;
    }

    public void setExchangeDate(Date exchangeDate) {
        this.exchangeDate = exchangeDate;
    }

    public Long getCurCode() {
        return curCode;
    }

    public void setCurCode(Long curCode) {
        this.curCode = curCode;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
