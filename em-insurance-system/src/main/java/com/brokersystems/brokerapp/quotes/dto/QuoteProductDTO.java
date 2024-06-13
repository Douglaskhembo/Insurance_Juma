package com.brokersystems.brokerapp.quotes.dto;

import java.math.BigDecimal;
import java.util.Date;

public class QuoteProductDTO {

    private Long quoteProductId;
    private Long quoteId;
    private String quoteNo;
    private String insuranceCompany;
    private String product;
    private Date wef;
    private Date wet;
    private String fname;
    private String otherNames;

    private Long tenId;
    private String clientType;
    private String currency;
    private String converted;
    private Long polId;
    private String policyNo;
    private String binder;
    private String bindType;
    private Long binderId;
    private Long productId;
    private String account;
    private String quotStatus;
    private Long acctId;
    private BigDecimal sumInsured;
    private BigDecimal premium;
    private BigDecimal commAmt;
    private String prospShtDesc;

    public String getInsuranceCompany() {
        return insuranceCompany;
    }

    public void setInsuranceCompany(String insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
    }

    public String getProspShtDesc() {
        return prospShtDesc;
    }

    public void setProspShtDesc(String prospShtDesc) {
        this.prospShtDesc = prospShtDesc;
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

    public String getBindType() {
        return bindType;
    }

    public void setBindType(String bindType) {
        this.bindType = bindType;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getBinder() {
        return binder;
    }

    public void setBinder(String binder) {
        this.binder = binder;
    }

    public Long getBinderId() {
        return binderId;
    }

    public void setBinderId(Long binderId) {
        this.binderId = binderId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getQuotStatus() {
        return quotStatus;
    }

    public void setQuotStatus(String quotStatus) {
        this.quotStatus = quotStatus;
    }

    public Long getAcctId() {
        return acctId;
    }

    public void setAcctId(Long acctId) {
        this.acctId = acctId;
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

    public BigDecimal getCommAmt() {
        return commAmt;
    }

    public void setCommAmt(BigDecimal commAmt) {
        this.commAmt = commAmt;
    }

    public Long getQuoteProductId() {
        return quoteProductId;
    }

    public void setQuoteProductId(Long quoteProductId) {
        this.quoteProductId = quoteProductId;
    }

    public Long getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(Long quoteId) {
        this.quoteId = quoteId;
    }

    public String getQuoteNo() {
        return quoteNo;
    }

    public void setQuoteNo(String quoteNo) {
        this.quoteNo = quoteNo;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Date getWef() {
        return wef;
    }

    public void setWef(Date wef) {
        this.wef = wef;
    }

    public Date getWet() {
        return wet;
    }

    public void setWet(Date wet) {
        this.wet = wet;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getConverted() {
        return converted;
    }

    public void setConverted(String converted) {
        this.converted = converted;
    }

    public Long getPolId() {
        return polId;
    }

    public void setPolId(Long polId) {
        this.polId = polId;
    }

    public String getPolicyNo() {
        return policyNo;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }
}
