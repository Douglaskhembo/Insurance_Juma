package com.brokersystems.brokerapp.setup.dto;

import java.math.BigDecimal;
import java.util.Date;

public class BankAccountDTO {


    private Long baId;
    private Long branchId;
    private Long bankBranchId;
    private String branchName;
    private String bankBranchName;
    private String bankAcctNumber;
    private String bankAcctName;
    private String currentChequeNo;
    private Long glId;
    private String glCode;
    private String glName;
    private String status;
    private BigDecimal maximumAmt;
    private BigDecimal minimumAmt;

    public String getGlCode() {
        return glCode;
    }

    public void setGlCode(String glCode) {
        this.glCode = glCode;
    }

    public Long getBankBranchId() {
        return bankBranchId;
    }

    public void setBankBranchId(Long bankBranchId) {
        this.bankBranchId = bankBranchId;
    }

    public String getBankBranchName() {
        return bankBranchName;
    }

    public void setBankBranchName(String bankBranchName) {
        this.bankBranchName = bankBranchName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getGlName() {
        return glName;
    }

    public void setGlName(String glName) {
        this.glName = glName;
    }

    public Long getBaId() {
        return baId;
    }

    public void setBaId(Long baId) {
        this.baId = baId;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public String getBankAcctNumber() {
        return bankAcctNumber;
    }

    public void setBankAcctNumber(String bankAcctNumber) {
        this.bankAcctNumber = bankAcctNumber;
    }

    public String getBankAcctName() {
        return bankAcctName;
    }

    public void setBankAcctName(String bankAcctName) {
        this.bankAcctName = bankAcctName;
    }

    public String getCurrentChequeNo() {
        return currentChequeNo;
    }

    public void setCurrentChequeNo(String currentChequeNo) {
        this.currentChequeNo = currentChequeNo;
    }

    public Long getGlId() {
        return glId;
    }

    public void setGlId(Long glId) {
        this.glId = glId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getMaximumAmt() {
        return maximumAmt;
    }

    public void setMaximumAmt(BigDecimal maximumAmt) {
        this.maximumAmt = maximumAmt;
    }

    public BigDecimal getMinimumAmt() {
        return minimumAmt;
    }

    public void setMinimumAmt(BigDecimal minimumAmt) {
        this.minimumAmt = minimumAmt;
    }
}
