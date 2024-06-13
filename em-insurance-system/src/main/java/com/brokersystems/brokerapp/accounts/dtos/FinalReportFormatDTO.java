package com.brokersystems.brokerapp.accounts.dtos;

import java.math.BigDecimal;

public class FinalReportFormatDTO {

    private String rowCode;
    private String description;
    private String detailFormat;
    private String summaryFormat;
    private String type;
    private Integer order;
    private String pickedFrom;
    private String dependsOn;
    private String assetLiability;
    private Integer assetLiabilitySign;
    private BigDecimal allocation;
    private Long rfId;

    public Long getRfId() {
        return rfId;
    }

    public void setRfId(Long rfId) {
        this.rfId = rfId;
    }

    public String getRowCode() {
        return rowCode;
    }
    public void setRowCode(String rowCode) {
        this.rowCode = rowCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDetailFormat() {
        return detailFormat;
    }

    public void setDetailFormat(String detailFormat) {
        this.detailFormat = detailFormat;
    }

    public String getSummaryFormat() {
        return summaryFormat;
    }

    public void setSummaryFormat(String summaryFormat) {
        this.summaryFormat = summaryFormat;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getPickedFrom() {
        return pickedFrom;
    }

    public void setPickedFrom(String pickedFrom) {
        this.pickedFrom = pickedFrom;
    }

    public String getDependsOn() {
        return dependsOn;
    }

    public void setDependsOn(String dependsOn) {
        this.dependsOn = dependsOn;
    }

    public String getAssetLiability() {
        return assetLiability;
    }

    public void setAssetLiability(String assetLiability) {
        this.assetLiability = assetLiability;
    }

    public Integer getAssetLiabilitySign() {
        return assetLiabilitySign;
    }

    public void setAssetLiabilitySign(Integer assetLiabilitySign) {
        this.assetLiabilitySign = assetLiabilitySign;
    }

    public BigDecimal getAllocation() {
        return allocation;
    }

    public void setAllocation(BigDecimal allocation) {
        this.allocation = allocation;
    }
}
