package com.brokersystems.brokerapp.soapws;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@XmlRootElement(name = "receipt")
public class ReceiptForm {

    private String username;
    private String timeStamp;
    private String transactionCode;
    private String serialNumber;
    private String custId;
    private String receivedFrom;
    private String transactionType;
    private String riskNote;
    private String paymentMode;
    private BigDecimal amount;
    private String bankBranch;
    private String paymentMemo;
    private String accountNumber;
    private String token;
    private String type;
    private String insuranceId;

    @XmlElement(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @XmlElement(name = "insuranceid")
    public String getInsuranceId() {
        return insuranceId;
    }

    public void setInsuranceId(String insuranceId) {
        this.insuranceId = insuranceId;
    }

    @XmlElement(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @XmlElement(name = "timestamp")
    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    @XmlElement(name = "transactioncode")
    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    @XmlElement(name = "serialnumber")
    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @XmlElement(name = "custid")
    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    @XmlElement(name = "receivedfrom")
    public String getReceivedFrom() {
        return receivedFrom;
    }

    public void setReceivedFrom(String receivedFrom) {
        this.receivedFrom = receivedFrom;
    }

    @XmlElement(name = "transactiontype")
    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    @XmlElement(name = "risknote")
    public String getRiskNote() {
        return riskNote;
    }

    public void setRiskNote(String riskNote) {
        this.riskNote = riskNote;
    }

    @XmlElement(name = "paymentmode")
    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    @XmlElement(name = "amount")
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @XmlElement(name = "bankbranch")
    public String getBankBranch() {
        return bankBranch;
    }

    public void setBankBranch(String bankBranch) {
        this.bankBranch = bankBranch;
    }

    @XmlElement(name = "paymentmemo")
    public String getPaymentMemo() {
        return paymentMemo;
    }

    public void setPaymentMemo(String paymentMemo) {
        this.paymentMemo = paymentMemo;
    }

    @XmlElement(name = "bankaccountnumber")
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @XmlElement(name = "token")
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "ReceiptForm{" +
                "username='" + username + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", transactionCode='" + transactionCode + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", custId='" + custId + '\'' +
                ", receivedFrom='" + receivedFrom + '\'' +
                ", transactionType='" + transactionType + '\'' +
                ", riskNote='" + riskNote + '\'' +
                ", paymentMode='" + paymentMode + '\'' +
                ", amount=" + amount +
                ", bankBranch='" + bankBranch + '\'' +
                ", paymentMemo='" + paymentMemo + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", token='" + token + '\'' +
                ", type='" + type + '\'' +
                ", insuranceId='" + insuranceId + '\'' +
                '}';
    }
}
