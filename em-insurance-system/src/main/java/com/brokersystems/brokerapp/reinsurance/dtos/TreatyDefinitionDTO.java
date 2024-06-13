package com.brokersystems.brokerapp.reinsurance.dtos;

import java.math.BigDecimal;
import java.util.Date;


public class TreatyDefinitionDTO {

    private  Long treatyId;
    private  String treatyType;
    private  Date wef;
    private  Date wet;
    private  BigDecimal cashCall;
    private  BigDecimal cessionRate;
    private  String rateType;
    private  BigDecimal profitCommission;
    private  BigDecimal managementFeeRate;
    private  BigDecimal premiumPortfolio;
    private  BigDecimal claimsPortfolio;
    private  BigDecimal limit;
    private  BigDecimal sumInsuredFrom;
    private  BigDecimal commRate;
    private  BigDecimal facCedeRate;
    private  Long currencyId;
    private  String currencyDesc;
    private  String raisedBy;
    private  String status;
    private  String authBy;


    public TreatyDefinitionDTO() {
    }

    private TreatyDefinitionDTO(final Long treatyId,
                                final String treatyType,
                                final Date wef,
                                final Date wet,
                                final BigDecimal cashCall,
                                final BigDecimal cessionRate,
                                final String rateType,
                                final BigDecimal profitCommission,
                                final BigDecimal managementFeeRate,
                                final BigDecimal premiumPortfolio,
                                final BigDecimal claimsPortfolio,
                                final BigDecimal limit,
                                final BigDecimal commRate,
                                final BigDecimal facCedeRate,
                                final Long currencyId,
                                final String currencyDesc,
                                final String raisedBy,
                                final String status,
                                final String authBy) {
        this.treatyId = treatyId;
        this.treatyType = treatyType;
        this.wef = wef;
        this.wet = wet;
        this.cashCall = cashCall;
        this.cessionRate = cessionRate;
        this.rateType = rateType;
        this.profitCommission = profitCommission;
        this.managementFeeRate = managementFeeRate;
        this.premiumPortfolio = premiumPortfolio;
        this.claimsPortfolio = claimsPortfolio;
        this.limit = limit;
        this.commRate = commRate;
        this.facCedeRate = facCedeRate;
        this.currencyId = currencyId;
        this.currencyDesc = currencyDesc;
        this.raisedBy = raisedBy;
        this.status = status;
        this.authBy = authBy;
    }

    public static TreatyDefinitionDTO data(final Long treatyId,
                                           final String treatyType,
                                           final Date wef,
                                           final Date wet,
                                           final BigDecimal cashCall,
                                           final BigDecimal cessionRate,
                                           final String rateType,
                                           final BigDecimal profitCommission,
                                           final BigDecimal managementFeeRate,
                                           final BigDecimal premiumPortfolio,
                                           final BigDecimal claimsPortfolio,
                                           final BigDecimal limit,
                                           final BigDecimal commRate,
                                           final BigDecimal facCedeRate,
                                           final Long currencyId,
                                           final String currencyDesc,
                                           final String raisedBy,
                                           final String status,
                                           final String authBy){
        return new TreatyDefinitionDTO(treatyId, treatyType, wef, wet, cashCall, cessionRate, rateType, profitCommission,
                managementFeeRate, premiumPortfolio, claimsPortfolio, limit, commRate, facCedeRate, currencyId,
                currencyDesc, raisedBy, status, authBy);

    }

    public BigDecimal getSumInsuredFrom() {
        return sumInsuredFrom;
    }

    public void setSumInsuredFrom(BigDecimal sumInsuredFrom) {
        this.sumInsuredFrom = sumInsuredFrom;
    }

    public Long getTreatyId() {
        return treatyId;
    }

    public String getTreatyType() {
        return treatyType;
    }

    public Date getWef() {
        return wef;
    }

    public Date getWet() {
        return wet;
    }

    public BigDecimal getCashCall() {
        return cashCall;
    }

    public BigDecimal getCessionRate() {
        return cessionRate;
    }

    public String getRateType() {
        return rateType;
    }

    public BigDecimal getProfitCommission() {
        return profitCommission;
    }

    public BigDecimal getManagementFeeRate() {
        return managementFeeRate;
    }

    public BigDecimal getPremiumPortfolio() {
        return premiumPortfolio;
    }

    public BigDecimal getClaimsPortfolio() {
        return claimsPortfolio;
    }

    public BigDecimal getLimit() {
        return limit;
    }

    public BigDecimal getCommRate() {
        return commRate;
    }

    public BigDecimal getFacCedeRate() {
        return facCedeRate;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public String getCurrencyDesc() {
        return currencyDesc;
    }

    public String getRaisedBy() {
        return raisedBy;
    }

    public String getStatus() {
        return status;
    }

    public String getAuthBy() {
        return authBy;
    }


    public void setTreatyId(Long treatyId) {
        this.treatyId = treatyId;
    }

    public void setTreatyType(String treatyType) {
        this.treatyType = treatyType;
    }

    public void setWef(Date wef) {
        this.wef = wef;
    }

    public void setWet(Date wet) {
        this.wet = wet;
    }

    public void setCashCall(BigDecimal cashCall) {
        this.cashCall = cashCall;
    }

    public void setCessionRate(BigDecimal cessionRate) {
        this.cessionRate = cessionRate;
    }

    public void setRateType(String rateType) {
        this.rateType = rateType;
    }

    public void setProfitCommission(BigDecimal profitCommission) {
        this.profitCommission = profitCommission;
    }

    public void setManagementFeeRate(BigDecimal managementFeeRate) {
        this.managementFeeRate = managementFeeRate;
    }

    public void setPremiumPortfolio(BigDecimal premiumPortfolio) {
        this.premiumPortfolio = premiumPortfolio;
    }

    public void setClaimsPortfolio(BigDecimal claimsPortfolio) {
        this.claimsPortfolio = claimsPortfolio;
    }

    public void setLimit(BigDecimal limit) {
        this.limit = limit;
    }

    public void setCommRate(BigDecimal commRate) {
        this.commRate = commRate;
    }

    public void setFacCedeRate(BigDecimal facCedeRate) {
        this.facCedeRate = facCedeRate;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    public void setCurrencyDesc(String currencyDesc) {
        this.currencyDesc = currencyDesc;
    }

    public void setRaisedBy(String raisedBy) {
        this.raisedBy = raisedBy;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAuthBy(String authBy) {
        this.authBy = authBy;
    }
}
