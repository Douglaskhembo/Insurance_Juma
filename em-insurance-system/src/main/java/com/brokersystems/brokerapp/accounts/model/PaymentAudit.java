package com.brokersystems.brokerapp.accounts.model;

import com.brokersystems.brokerapp.setup.model.User;
import com.brokersystems.brokerapp.trans.model.ReceiptSettlementDetails;
import com.brokersystems.brokerapp.trans.model.SystemTransactions;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by peter on 3/26/2017.
 */

@Entity
@Table(name="sys_brk_payment_audit")
public class PaymentAudit {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="pa_Id")
    private Long paId;


    @XmlTransient
    @ManyToOne
    @JoinColumn(name="pa_trans_no")
    private SystemTransactions transNo;

    @XmlTransient
    @ManyToOne
    @JoinColumn(name="pa_settle_cr_id")
    private ReceiptSettlementDetails settlements;

    @Column(name = "pa_settle_rec_ref_no")
    private String receiptTransNo;

    @Column(name = "pa_settle_dr_ref_no")
    private String debitTransNo;

    @XmlTransient
    @ManyToOne
    @JoinColumn(name="pa_other_trans")
    private SystemTransactions otherTransNo;

    @Column(name="pa_comm")
    private BigDecimal commAmount;

    @Column(name="pa_whtx")
    private BigDecimal whtxAmount;

    @Column(name="pa_amount")
    private BigDecimal paymentAmount;

    @Column(name="pa_posted")
    private String posted;

    @ManyToOne
    @JoinColumn(name="pa_posted_by")
    private User postedBy;

    @Column(name="pa_post_dt")
    private Date postDate;

    @Column(name="pa_cancelled")
    private String cancelled;

    @ManyToOne
    @JoinColumn(name="pa_cancelled_by")
    private User cancelledBy;

    @Column(name="pa_canc_dt")
    private Date cancDate;

    @Column(name="pa_trans_type")
    private String transType;

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public Long getPaId() {
        return paId;
    }

    public void setPaId(Long paId) {
        this.paId = paId;
    }

    public SystemTransactions getTransNo() {
        return transNo;
    }

    public void setTransNo(SystemTransactions transNo) {
        this.transNo = transNo;
    }

    public SystemTransactions getOtherTransNo() {
        return otherTransNo;
    }

    public void setOtherTransNo(SystemTransactions otherTransNo) {
        this.otherTransNo = otherTransNo;
    }

    public BigDecimal getCommAmount() {
        return commAmount;
    }

    public void setCommAmount(BigDecimal commAmount) {
        this.commAmount = commAmount;
    }

    public BigDecimal getWhtxAmount() {
        return whtxAmount;
    }

    public void setWhtxAmount(BigDecimal whtxAmount) {
        this.whtxAmount = whtxAmount;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPosted() {
        return posted;
    }

    public void setPosted(String posted) {
        this.posted = posted;
    }

    public User getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(User postedBy) {
        this.postedBy = postedBy;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public String getCancelled() {
        return cancelled;
    }

    public void setCancelled(String cancelled) {
        this.cancelled = cancelled;
    }

    public User getCancelledBy() {
        return cancelledBy;
    }

    public void setCancelledBy(User cancelledBy) {
        this.cancelledBy = cancelledBy;
    }

    public Date getCancDate() {
        return cancDate;
    }

    public void setCancDate(Date cancDate) {
        this.cancDate = cancDate;
    }

    public ReceiptSettlementDetails getSettlements() {
        return settlements;
    }

    public void setSettlements(ReceiptSettlementDetails settlements) {
        this.settlements = settlements;
    }

    public String getReceiptTransNo() {
        return receiptTransNo;
    }

    public void setReceiptTransNo(String receiptTransNo) {
        this.receiptTransNo = receiptTransNo;
    }

    public String getDebitTransNo() {
        return debitTransNo;
    }

    public void setDebitTransNo(String debitTransNo) {
        this.debitTransNo = debitTransNo;
    }

    @Override
    public String toString() {
        return "PaymentAudit{" +
                "paId=" + paId +
                ", receiptTransNo='" + receiptTransNo + '\'' +
                ", debitTransNo='" + debitTransNo + '\'' +
                ", commAmount=" + commAmount +
                ", whtxAmount=" + whtxAmount +
                ", paymentAmount=" + paymentAmount +
                ", posted='" + posted + '\'' +
                ", postDate=" + postDate +
                ", cancelled='" + cancelled + '\'' +
                ", cancDate=" + cancDate +
                ", transType='" + transType + '\'' +
                '}';
    }
}
