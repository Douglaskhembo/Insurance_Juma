package com.brokersystems.brokerapp.claims.model;

import com.brokersystems.brokerapp.setup.model.BinderSectionPerils;
import com.brokersystems.brokerapp.setup.model.PerilsDef;
import com.brokersystems.brokerapp.setup.model.User;
import com.brokersystems.brokerapp.uw.model.SectionTrans;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by peter on 3/8/2017.
 */
@Entity
@Table(name = "sys_brk_clm_perils")
public class ClaimPerils {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "clm_prl_id")
    private Long clmPerilId;

    @ManyToOne
    @JoinColumn(name="clm_prl_clm_id",nullable=false)
    private ClaimBookings claimBookings;

    @ManyToOne
    @JoinColumn(name="clm_prl_peril_id",nullable=false)
    private PerilsDef perilsDef;

    @Column(name = "clm_prl_type")
    private String type;

    @Column(name = "clm_prl_limit_amt")
    private BigDecimal limitAmt;

    @Column(name = "clm_prl_excess")
    private BigDecimal excessAmt;

    @Column(name = "clm_prl_reserve")
    private BigDecimal reserve;

    @Column(name = "clm_prl_remark", length = 2000)
    private String remarks;

    @ManyToOne
    @JoinColumn(name="clm_prl_user",nullable=false)
    private User revisionBy;

    @Column(name = "clm_prl_transtype")
    private String transType;

    @Column(name = "clm_prl_total_reserve")
    private BigDecimal totalReserve;

    @Column(name = "clm_prl_orig_reserve")
    private BigDecimal Originalreserve;

    @ManyToOne
    @JoinColumn(name="clm_prl_clmnt_id")
    private ClaimClaimants clmClaimant;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="clm_prl_spr_id")
    private ClaimServiceProvider serviceProviderDef;

    @Column(name = "clm_prl_claimant")
    private String claimant;

    @ManyToOne
    @JoinColumn(name="clm_prl_bsp_id")
    private BinderSectionPerils binderSectionPerils;

    @Column(name = "clm_change_amt")
    private BigDecimal changeAmount;

    @Column(name = "clm_prl_paid_amount")
    private BigDecimal claimPaidAmount;

    @ManyToOne
    @JoinColumn(name="clm_expiring_sect_id")
    private SectionTrans expiringSection;

    public ClaimServiceProvider getServiceProviderDef() {
        return serviceProviderDef;
    }

    public void setServiceProviderDef(ClaimServiceProvider serviceProviderDef) {
        this.serviceProviderDef = serviceProviderDef;
    }

    public Long getClmPerilId() {
        return clmPerilId;
    }

    public void setClmPerilId(Long clmPerilId) {
        this.clmPerilId = clmPerilId;
    }

    public ClaimBookings getClaimBookings() {
        return claimBookings;
    }

    public void setClaimBookings(ClaimBookings claimBookings) {
        this.claimBookings = claimBookings;
    }

    public PerilsDef getPerilsDef() {
        return perilsDef;
    }

    public void setPerilsDef(PerilsDef perilsDef) {
        this.perilsDef = perilsDef;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getLimitAmt() {
        return limitAmt;
    }

    public void setLimitAmt(BigDecimal limitAmt) {
        this.limitAmt = limitAmt;
    }

    public BigDecimal getExcessAmt() {
        return excessAmt;
    }

    public void setExcessAmt(BigDecimal excessAmt) {
        this.excessAmt = excessAmt;
    }

    public BigDecimal getReserve() {
        return reserve;
    }

    public void setReserve(BigDecimal reserve) {
        this.reserve = reserve;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public User getRevisionBy() {
        return revisionBy;
    }

    public void setRevisionBy(User revisionBy) {
        this.revisionBy = revisionBy;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public BigDecimal getTotalReserve() {
        return totalReserve;
    }

    public void setTotalReserve(BigDecimal totalReserve) {
        this.totalReserve = totalReserve;
    }

    public BigDecimal getOriginalreserve() {
        return Originalreserve;
    }

    public void setOriginalreserve(BigDecimal originalreserve) {
        Originalreserve = originalreserve;
    }

    public ClaimClaimants getClmClaimant() {
        return clmClaimant;
    }

    public void setClmClaimant(ClaimClaimants clmClaimant) {
        this.clmClaimant = clmClaimant;
    }

    public String getClaimant() {
        return claimant;
    }

    public void setClaimant(String claimant) {
        this.claimant = claimant;
    }

    public BinderSectionPerils getBinderSectionPerils() {
        return binderSectionPerils;
    }

    public void setBinderSectionPerils(BinderSectionPerils binderSectionPerils) {
        this.binderSectionPerils = binderSectionPerils;
    }

    public BigDecimal getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(BigDecimal changeAmount) {
        this.changeAmount = changeAmount;
    }

    public BigDecimal getClaimPaidAmount() {
        return claimPaidAmount;
    }

    public void setClaimPaidAmount(BigDecimal claimPaidAmount) {
        this.claimPaidAmount = claimPaidAmount;
    }

    public SectionTrans getExpiringSection() {
        return expiringSection;
    }

    public void setExpiringSection(SectionTrans expiringSection) {
        this.expiringSection = expiringSection;
    }
}
