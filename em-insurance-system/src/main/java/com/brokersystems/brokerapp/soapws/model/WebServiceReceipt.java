package com.brokersystems.brokerapp.soapws.model;

import com.brokersystems.brokerapp.setup.model.OrgBranch;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "sys_brk_web_service_rcpt")
public class WebServiceReceipt {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "sr_id")
    private Long srId;

    private String username;

    @Column(name = "time_stamp", nullable = false)
    private String timeStamp;

    @Column(unique = true, name="transaction_code")
    private String transactionCode;

    private String serialNumber;

    @Column(name = "cust_id", nullable = false)
    private String custId;

    private String receivedFrom;

    private String transactionType;

    @Column(name = "risk_note", nullable = false)
    private String riskNote;

    private String paymentMode;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    private String bankBranch;

    @Column(name = "branch_name")
    private String branchName;

    private String paymentMemo;

    private String accountNumber;

    private String token;

    @Column(name = "refno",unique = true,nullable = false)
    private String refno;

    private String status;

    private String type;

    private String insuranceId;

    @Column(name="new_ref_no",length = 70)
    private String newTransRefNumber;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInsuranceId() {
        return insuranceId;
    }

    public void setInsuranceId(String insuranceId) {
        this.insuranceId = insuranceId;
    }

    public Long getSrId() {
        return srId;
    }

    public void setSrId(Long srId) {
        this.srId = srId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getReceivedFrom() {
        return receivedFrom;
    }

    public void setReceivedFrom(String receivedFrom) {
        this.receivedFrom = receivedFrom;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getRiskNote() {
        return riskNote;
    }

    public void setRiskNote(String riskNote) {
        this.riskNote = riskNote;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getBankBranch() {
        return bankBranch;
    }

    public void setBankBranch(String bankBranch) {
        this.bankBranch = bankBranch;
    }

    public String getPaymentMemo() {
        return paymentMemo;
    }

    public void setPaymentMemo(String paymentMemo) {
        this.paymentMemo = paymentMemo;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefno() {
        return refno;
    }

    public void setRefno(String refno) {
        this.refno = refno;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNewTransRefNumber() {
        return newTransRefNumber;
    }

    public void setNewTransRefNumber(String newTransRefNumber) {
        this.newTransRefNumber = newTransRefNumber;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }
}
