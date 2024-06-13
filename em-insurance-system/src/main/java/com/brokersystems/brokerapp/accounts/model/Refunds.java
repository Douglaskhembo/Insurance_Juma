package com.brokersystems.brokerapp.accounts.model;

import com.brokersystems.brokerapp.setup.model.ClientDef;
import com.brokersystems.brokerapp.setup.model.PaymentModes;
import com.brokersystems.brokerapp.setup.model.User;
import com.brokersystems.brokerapp.trans.model.SystemTransactions;
import com.brokersystems.brokerapp.uw.model.PolicyTrans;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by waititu on 29/11/2018.
 */
@Entity
@Table(name="sys_brk_refunds")
public class Refunds {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="ref_Id")
    private Long refId;

    @ManyToOne
    @JoinColumn(name="ref_pol_Id",nullable=false)
    private PolicyTrans policy;

    @ManyToOne
    @JoinColumn(name="ref_trans_no",nullable=false)
    private SystemTransactions transactions;

    @Column(name="ref_amount",nullable=false)
    private BigDecimal amount;


    @Column(name="ref_narrations",nullable=false)
    private String narrations;

    @Column(name="ref_reject_remarks")
    private String rejectionRemarks;

    @Column(name="ref_payee",nullable=false)
    private String payee;


    @ManyToOne
    @JoinColumn(name="ref_client_id",nullable=false)
    private ClientDef client;

    @Column(name="ref_capture_date",nullable=false)
    private Date refundCaptureDate;

    @Column(name="ref_authorized_date")
    private Date refundAuthDate;

    @XmlTransient
    @ManyToOne
    @JoinColumn(name="ref_auth_user")
    private User authBy;

    @XmlTransient
    @ManyToOne
    @JoinColumn(name="ref_makeready_user")
    private User madeReadyBy;

    @XmlTransient
    @ManyToOne
    @JoinColumn(name="ref_reject_user")
    private User rejectedBy;


    @Column(name="ref_rejected_date" )
    private Date rejectedDate;

    @XmlTransient
    @ManyToOne
    @JoinColumn(name="ref_capture_user",nullable=false)
    private User createdUser;



    @Column(name="ref_makeready_date" )
    private Date makeReadyDate;


    @Column(name="ref_status",nullable=false)
    private String refundStatus;

    @ManyToOne
    @JoinColumn(name="ref_pm_id")
    private PaymentModes paymentMode;


    @Transient
    private Long transNoToRefund;

    @Column(name="ref_ft_number",length = 50)
    private String ftNo;

    public String getFtNo() {
        return ftNo;
    }

    public void setFtNo(String ftNo) {
        this.ftNo = ftNo;
    }

    public Long getRefId() {
        return refId;
    }

    public void setRefId(Long refId) {
        this.refId = refId;
    }

    public PolicyTrans getPolicy() {
        return policy;
    }

    public void setPolicy(PolicyTrans policy) {
        this.policy = policy;
    }

    public SystemTransactions getTransactions() {
        return transactions;
    }

    public void setTransactions(SystemTransactions transactions) {
        this.transactions = transactions;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getNarrations() {
        return narrations;
    }

    public void setNarrations(String narrations) {
        this.narrations = narrations;
    }

    public ClientDef getClient() {
        return client;
    }

    public void setClient(ClientDef client) {
        this.client = client;
    }

    public Date getRefundCaptureDate() {
        return refundCaptureDate;
    }

    public void setRefundCaptureDate(Date refundCaptureDate) {
        this.refundCaptureDate = refundCaptureDate;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    public PaymentModes getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(PaymentModes paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public Date getMakeReadyDate() {
        return makeReadyDate;
    }

    public void setMakeReadyDate(Date makeReadyDate) {
        this.makeReadyDate = makeReadyDate;
    }

    public String getRejectionRemarks() {
        return rejectionRemarks;
    }

    public void setRejectionRemarks(String rejectionRemarks) {
        this.rejectionRemarks = rejectionRemarks;
    }

    public User getAuthBy() {
        return authBy;
    }

    public void setAuthBy(User authBy) {
        this.authBy = authBy;
    }

    public User getMadeReadyBy() {
        return madeReadyBy;
    }

    public void setMadeReadyBy(User madeReadyBy) {
        this.madeReadyBy = madeReadyBy;
    }

    public User getRejectedBy() {
        return rejectedBy;
    }

    public void setRejectedBy(User rejectedBy) {
        this.rejectedBy = rejectedBy;
    }

    public User getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(User createdUser) {
        this.createdUser = createdUser;
    }

    public Date getRejectedDate() {
        return rejectedDate;
    }

    public void setRejectedDate(Date rejectedDate) {
        this.rejectedDate = rejectedDate;
    }

    public Date getRefundAuthDate() {
        return refundAuthDate;
    }

    public void setRefundAuthDate(Date refundAuthDate) {
        this.refundAuthDate = refundAuthDate;
    }

    public Long getTransNoToRefund() {
        return transNoToRefund;
    }

    public void setTransNoToRefund(Long transNoToRefund) {
        this.transNoToRefund = transNoToRefund;
    }
}
