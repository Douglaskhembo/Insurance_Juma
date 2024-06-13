package com.brokersystems.brokerapp.accounts.dtos;

import java.math.BigDecimal;

/**
 * Created by HP on 5/13/2018.
 */
public class SettlementDTO {

    private String polNo;
    private String clientPolNo;
    private String clientName;
    private String drNo;
    private String crNo;
    private String payStatus;
    private BigDecimal basicPrem;
    private BigDecimal commAmt;
    private BigDecimal whtx;
    private BigDecimal debitBal;
    private BigDecimal allocAmt;
    private BigDecimal settleAmt;
    private String transType;
    private Long acctCode;


    public Long getAcctCode() {
        return acctCode;
    }

    public void setAcctCode(Long acctCode) {
        this.acctCode = acctCode;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public BigDecimal getSettleAmt() {
        return settleAmt;
    }

    public void setSettleAmt(BigDecimal settleAmt) {
        this.settleAmt = settleAmt;
    }

    public String getPolNo() {
        return polNo;
    }

    public void setPolNo(String polNo) {
        this.polNo = polNo;
    }

    public String getClientPolNo() {
        return clientPolNo;
    }

    public void setClientPolNo(String clientPolNo) {
        this.clientPolNo = clientPolNo;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getDrNo() {
        return drNo;
    }

    public void setDrNo(String drNo) {
        this.drNo = drNo;
    }

    public String getCrNo() {
        return crNo;
    }

    public void setCrNo(String crNo) {
        this.crNo = crNo;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public BigDecimal getBasicPrem() {
        return basicPrem;
    }

    public void setBasicPrem(BigDecimal basicPrem) {
        this.basicPrem = basicPrem;
    }

    public BigDecimal getCommAmt() {
        return commAmt;
    }

    public void setCommAmt(BigDecimal commAmt) {
        this.commAmt = commAmt;
    }

    public BigDecimal getWhtx() {
        return whtx;
    }

    public void setWhtx(BigDecimal whtx) {
        this.whtx = whtx;
    }

    public BigDecimal getDebitBal() {
        return debitBal;
    }

    public void setDebitBal(BigDecimal debitBal) {
        this.debitBal = debitBal;
    }

    public BigDecimal getAllocAmt() {
        return allocAmt;
    }

    public void setAllocAmt(BigDecimal allocAmt) {
        this.allocAmt = allocAmt;
    }
}
