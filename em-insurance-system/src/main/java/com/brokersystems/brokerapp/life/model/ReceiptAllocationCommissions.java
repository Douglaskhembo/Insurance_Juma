package com.brokersystems.brokerapp.life.model;

import com.brokersystems.brokerapp.setup.model.PremRatesDef;
import com.brokersystems.brokerapp.uw.model.SectionTrans;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.math.BigDecimal;

/**
 * Created by waititu on 18/03/2019.
 */
@Entity
@Table(name = "sys_brk_rct_alloc_comms")
public class ReceiptAllocationCommissions {

    @Id
    @SequenceGenerator(name = "receiptAllocCommSeq",sequenceName = "receipt_alloc_comm_seq",allocationSize=1)
    @GeneratedValue(generator = "receiptAllocCommSeq")
    @Column(name="acomm_id")
    private Long allocCommId;

    @XmlTransient
    @ManyToOne
    @JoinColumn(name="acomm_alloc_id")
    private LifeReceiptAllocations allocations;


    @XmlTransient
    @ManyToOne
    @JoinColumn(name="acomm_comm_id")
    private LifeCommissionRates commissionRates;

    @XmlTransient
    @ManyToOne
    @JoinColumn(name="acomm_prem_id")
    private PremRatesDef premRatesDef;

    @XmlTransient
    @ManyToOne
    @JoinColumn(name="acomm_sect_id")
    private SectionTrans sectionTrans;

    @XmlTransient
    @ManyToOne
    @JoinColumn(name="acomm_lrct_id")
    private LifeReceipts lifeReceipts;

    @Column(name="acomm_cover_comm")
    private BigDecimal commissionAmt;

    @Column(name="acomm_alloc")
    private BigDecimal sectionAlloc;

    @Column(name="acomm__cover_prem")
    private BigDecimal coverPremium;

    @Column(name="acomm_comm_rate")
    private BigDecimal commRate;

    @Column(name="acomm_div_fact")
    private BigDecimal commDivfact;

    @Transient
    private BigDecimal sectionPrem;


    public Long getAllocCommId() {
        return allocCommId;
    }

    public void setAllocCommId(Long allocCommId) {
        this.allocCommId = allocCommId;
    }

    public LifeReceiptAllocations getAllocations() {
        return allocations;
    }

    public void setAllocations(LifeReceiptAllocations allocations) {
        this.allocations = allocations;
    }

    public LifeCommissionRates getCommissionRates() {
        return commissionRates;
    }

    public void setCommissionRates(LifeCommissionRates commissionRates) {
        this.commissionRates = commissionRates;
    }

    public PremRatesDef getPremRatesDef() {
        return premRatesDef;
    }

    public void setPremRatesDef(PremRatesDef premRatesDef) {
        this.premRatesDef = premRatesDef;
    }

    public LifeReceipts getLifeReceipts() {
        return lifeReceipts;
    }

    public void setLifeReceipts(LifeReceipts lifeReceipts) {
        this.lifeReceipts = lifeReceipts;
    }

    public BigDecimal getCommissionAmt() {
        return commissionAmt;
    }

    public void setCommissionAmt(BigDecimal commissionAmt) {
        this.commissionAmt = commissionAmt;
    }

    public BigDecimal getCoverPremium() {
        return coverPremium;
    }

    public void setCoverPremium(BigDecimal coverPremium) {
        this.coverPremium = coverPremium;
    }

    public BigDecimal getCommRate() {
        return commRate;
    }

    public void setCommRate(BigDecimal commRate) {
        this.commRate = commRate;
    }

    public BigDecimal getCommDivfact() {
        return commDivfact;
    }

    public void setCommDivfact(BigDecimal commDivfact) {
        this.commDivfact = commDivfact;
    }

    public SectionTrans getSectionTrans() {
        return sectionTrans;
    }

    public void setSectionTrans(SectionTrans sectionTrans) {
        this.sectionTrans = sectionTrans;
    }

    public BigDecimal getSectionAlloc() {
        return sectionAlloc;
    }

    public void setSectionAlloc(BigDecimal sectionAlloc) {
        this.sectionAlloc = sectionAlloc;
    }

    public BigDecimal getSectionPrem() {
        return sectionPrem;
    }

    public void setSectionPrem(BigDecimal sectionPrem) {
        this.sectionPrem = sectionPrem;
    }
}
