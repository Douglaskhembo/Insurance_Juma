package com.brokersystems.brokerapp.reinsurance.dtos;

import java.math.BigDecimal;

public class TreatyClassesDTO {

    private  Long treatyClassId;
    private  BigDecimal retentionLimit;
    private  BigDecimal minEml;
    private  BigDecimal facCedeRate;
    private  BigDecimal claimLimit;
    private  BigDecimal insuredLimit;
    private  BigDecimal riPremTaxRate;
    private  Long subclassId;
    private  Long treatyId;
    private  String subclassDesc;

    public TreatyClassesDTO() {
    }

    private TreatyClassesDTO(final Long treatyClassId,
                             final BigDecimal retentionLimit,
                             final BigDecimal minEml,
                             final BigDecimal facCedeRate,
                             final BigDecimal riPremTaxRate,
                             final Long subclassId,
                             final String subclassDesc) {
        this.treatyClassId = treatyClassId;
        this.retentionLimit = retentionLimit;
        this.minEml = minEml;
        this.facCedeRate = facCedeRate;
        this.riPremTaxRate = riPremTaxRate;
        this.subclassId = subclassId;
        this.subclassDesc = subclassDesc;
    }

    public static TreatyClassesDTO data(final Long treatyClassId,
                                        final BigDecimal retentionLimit,
                                        final BigDecimal minEml,
                                        final BigDecimal facCedeRate,
                                        final BigDecimal riPremTaxRate,
                                        final Long subclassId,
                                        final String subclassDesc){
        return new TreatyClassesDTO(treatyClassId, retentionLimit, minEml, facCedeRate, riPremTaxRate, subclassId, subclassDesc);
    }

    public BigDecimal getClaimLimit() {
        return claimLimit;
    }

    public void setClaimLimit(BigDecimal claimLimit) {
        this.claimLimit = claimLimit;
    }

    public BigDecimal getInsuredLimit() {
        return insuredLimit;
    }

    public void setInsuredLimit(BigDecimal insuredLimit) {
        this.insuredLimit = insuredLimit;
    }

    public void setTreatyClassId(Long treatyClassId) {
        this.treatyClassId = treatyClassId;
    }

    public void setRetentionLimit(BigDecimal retentionLimit) {
        this.retentionLimit = retentionLimit;
    }

    public void setMinEml(BigDecimal minEml) {
        this.minEml = minEml;
    }

    public void setFacCedeRate(BigDecimal facCedeRate) {
        this.facCedeRate = facCedeRate;
    }

    public void setRiPremTaxRate(BigDecimal riPremTaxRate) {
        this.riPremTaxRate = riPremTaxRate;
    }

    public void setSubclassId(Long subclassId) {
        this.subclassId = subclassId;
    }

    public Long getTreatyId() {
        return treatyId;
    }

    public void setTreatyId(Long treatyId) {
        this.treatyId = treatyId;
    }

    public void setSubclassDesc(String subclassDesc) {
        this.subclassDesc = subclassDesc;
    }

    public Long getTreatyClassId() {
        return treatyClassId;
    }

    public BigDecimal getRetentionLimit() {
        return retentionLimit;
    }

    public BigDecimal getMinEml() {
        return minEml;
    }

    public BigDecimal getFacCedeRate() {
        return facCedeRate;
    }

    public BigDecimal getRiPremTaxRate() {
        return riPremTaxRate;
    }

    public Long getSubclassId() {
        return subclassId;
    }

    public String getSubclassDesc() {
        return subclassDesc;
    }
}
