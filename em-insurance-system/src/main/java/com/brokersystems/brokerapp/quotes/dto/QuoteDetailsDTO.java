package com.brokersystems.brokerapp.quotes.dto;

import java.math.BigDecimal;
import java.util.Date;

public class QuoteDetailsDTO {
    private Long quoteId;

    private String quotStatus;
    private String fname;
    private String otherNames;
    private Long tenId;
    private Long pmId;
    private String paymentMode;
    private Long sourceGroupId;
    private String sourceGroupName;
    private Long sourceId;
    private String sourceName;
    private Long obId;
    private String branch;
    private String currency;
    private Long curCode;
    private String quotNo;
    private String quotRevNo;
    private BigDecimal sumInsured;
    private BigDecimal premium;
    private BigDecimal basicPrem;
    private BigDecimal netPrem;
    private BigDecimal commAmt;
    private BigDecimal trainingLevy;
    private BigDecimal phcf;
    private BigDecimal stampDuty;
    private BigDecimal whtx;
    private BigDecimal extras;
    private Date quoteWef;
    private Date quoteWet;
    private Date expiryDate;
    private String clientType;

    public Long getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(Long quoteId) {
        this.quoteId = quoteId;
    }

    public String getQuotStatus() {
        return quotStatus;
    }

    public void setQuotStatus(String quotStatus) {
        this.quotStatus = quotStatus;
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

    public Long getTenId() {
        return tenId;
    }

    public void setTenId(Long tenId) {
        this.tenId = tenId;
    }

    public Long getPmId() {
        return pmId;
    }

    public void setPmId(Long pmId) {
        this.pmId = pmId;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public Long getSourceGroupId() {
        return sourceGroupId;
    }

    public void setSourceGroupId(Long sourceGroupId) {
        this.sourceGroupId = sourceGroupId;
    }

    public String getSourceGroupName() {
        return sourceGroupName;
    }

    public void setSourceGroupName(String sourceGroupName) {
        this.sourceGroupName = sourceGroupName;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public Long getObId() {
        return obId;
    }

    public void setObId(Long obId) {
        this.obId = obId;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Long getCurCode() {
        return curCode;
    }

    public void setCurCode(Long curCode) {
        this.curCode = curCode;
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

    public BigDecimal getBasicPrem() {
        return basicPrem;
    }

    public void setBasicPrem(BigDecimal basicPrem) {
        this.basicPrem = basicPrem;
    }

    public BigDecimal getNetPrem() {
        return netPrem;
    }

    public void setNetPrem(BigDecimal netPrem) {
        this.netPrem = netPrem;
    }

    public BigDecimal getCommAmt() {
        return commAmt;
    }

    public void setCommAmt(BigDecimal commAmt) {
        this.commAmt = commAmt;
    }

    public BigDecimal getTrainingLevy() {
        return trainingLevy;
    }

    public void setTrainingLevy(BigDecimal trainingLevy) {
        this.trainingLevy = trainingLevy;
    }

    public BigDecimal getPhcf() {
        return phcf;
    }

    public void setPhcf(BigDecimal phcf) {
        this.phcf = phcf;
    }

    public BigDecimal getStampDuty() {
        return stampDuty;
    }

    public void setStampDuty(BigDecimal stampDuty) {
        this.stampDuty = stampDuty;
    }

    public BigDecimal getWhtx() {
        return whtx;
    }

    public void setWhtx(BigDecimal whtx) {
        this.whtx = whtx;
    }

    public BigDecimal getExtras() {
        return extras;
    }

    public void setExtras(BigDecimal extras) {
        this.extras = extras;
    }

    public Date getQuoteWef() {
        return quoteWef;
    }

    public void setQuoteWef(Date quoteWef) {
        this.quoteWef = quoteWef;
    }

    public Date getQuoteWet() {
        return quoteWet;
    }

    public void setQuoteWet(Date quoteWet) {
        this.quoteWet = quoteWet;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }
}
