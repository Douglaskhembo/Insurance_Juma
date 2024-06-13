package com.brokersystems.brokerapp.quotes.dto;

import java.math.BigDecimal;
import java.util.Date;

public class QuoteRiskDTO {

    private String riskShtDesc;
    private String riskDesc;
    private Date wefDate;
    private Date wetDate;
    private Long subId;
    private String subDesc;
    private Long covId;
    private String covName;
    private BigDecimal sumInsured;
    private BigDecimal premium;
    private String quotStatus;
    private Long riskId;
    private BigDecimal butchargePrem;
    private String prorata;
    private Long binderId;
    private Long tenId;
    private String fname;
    private String otherNames;
    private BigDecimal commRate;
    private String clientType;

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public BigDecimal getCommRate() {
        return commRate;
    }

    public void setCommRate(BigDecimal commRate) {
        this.commRate = commRate;
    }

    public BigDecimal getButchargePrem() {
        return butchargePrem;
    }

    public void setButchargePrem(BigDecimal butchargePrem) {
        this.butchargePrem = butchargePrem;
    }

    public String getProrata() {
        return prorata;
    }

    public void setProrata(String prorata) {
        this.prorata = prorata;
    }

    public Long getBinderId() {
        return binderId;
    }

    public void setBinderId(Long binderId) {
        this.binderId = binderId;
    }

    public Long getTenId() {
        return tenId;
    }

    public void setTenId(Long tenId) {
        this.tenId = tenId;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getOtherNames() {
        return otherNames;
    }

    public void setOtherNames(String otherNames) {
        this.otherNames = otherNames;
    }

    public String getRiskShtDesc() {
        return riskShtDesc;
    }

    public void setRiskShtDesc(String riskShtDesc) {
        this.riskShtDesc = riskShtDesc;
    }

    public String getRiskDesc() {
        return riskDesc;
    }

    public void setRiskDesc(String riskDesc) {
        this.riskDesc = riskDesc;
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

    public Long getSubId() {
        return subId;
    }

    public void setSubId(Long subId) {
        this.subId = subId;
    }

    public String getSubDesc() {
        return subDesc;
    }

    public void setSubDesc(String subDesc) {
        this.subDesc = subDesc;
    }

    public Long getCovId() {
        return covId;
    }

    public void setCovId(Long covId) {
        this.covId = covId;
    }

    public String getCovName() {
        return covName;
    }

    public void setCovName(String covName) {
        this.covName = covName;
    }

    public BigDecimal getSumInsured() {
        return sumInsured;
    }

    public void setSumInsured(BigDecimal sumInsured) {
        this.sumInsured = sumInsured;
    }

    public BigDecimal getPremium() {
        return premium;
    }

    public void setPremium(BigDecimal premium) {
        this.premium = premium;
    }

    public String getQuotStatus() {
        return quotStatus;
    }

    public void setQuotStatus(String quotStatus) {
        this.quotStatus = quotStatus;
    }

    public Long getRiskId() {
        return riskId;
    }

    public void setRiskId(Long riskId) {
        this.riskId = riskId;
    }
}
