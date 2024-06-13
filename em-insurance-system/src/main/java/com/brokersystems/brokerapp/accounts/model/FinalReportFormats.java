package com.brokersystems.brokerapp.accounts.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name="sys_brk_rpt_formats")
public class FinalReportFormats {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="rf_id")
    private Long rfId;

    @Column(name = "rf_row_code",length = 10)
    private String rowCode;
    @Column(name="rf_rpt_type",length = 2)
    @Enumerated(EnumType.STRING)
    private FinalReportTypes reportTypes;
    @Column(name = "rf_description")
    private String description;
    @Column(name = "rf_detail_format")
    private String detailFormat;
    @Column(name = "rf_summary_format")
    private String summaryFormat;
    @Column(name = "rf_type")
    private String type;
    @Column(name = "rf_order")
    private Integer order;
    @Column(name = "rf_picked_from")
    private String pickedFrom;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="rf_depends_on")
    private FinalReportFormats dependsOn;
    @Column(name = "rf_asst_liabl")
    private String assetLiability;
    @Column(name = "rf_asst_liabl_sign")
    private Integer assetLiabilitySign;
    @Column(name = "rf_allocation_perc")
    private BigDecimal allocation;

    public String getRowCode() {
        return rowCode;
    }

    public void setRowCode(String rowCode) {
        this.rowCode = rowCode;
    }

    public Long getRfId() {
        return rfId;
    }

    public void setRfId(Long rfId) {
        this.rfId = rfId;
    }

    public FinalReportTypes getReportTypes() {
        return reportTypes;
    }

    public void setReportTypes(FinalReportTypes reportTypes) {
        this.reportTypes = reportTypes;
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

    public FinalReportFormats getDependsOn() {
        return dependsOn;
    }

    public void setDependsOn(FinalReportFormats dependsOn) {
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
