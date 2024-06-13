package com.brokersystems.brokerapp.accounts.dtos;

import java.math.BigDecimal;

public class OpeningBalanceDTO {

    private String accountName;
    private String accountNo;
    private String period;
    private BigDecimal balance;
    private BigDecimal drBalance;
    private BigDecimal currBalance;
    private BigDecimal crBalance;
    private String processedType;

    public BigDecimal getCurrBalance() {
        return currBalance;
    }

    public void setCurrBalance(BigDecimal currBalance) {
        this.currBalance = currBalance;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getDrBalance() {
        return drBalance;
    }

    public void setDrBalance(BigDecimal drBalance) {
        this.drBalance = drBalance;
    }

    public BigDecimal getCrBalance() {
        return crBalance;
    }

    public void setCrBalance(BigDecimal crBalance) {
        this.crBalance = crBalance;
    }

    public String getProcessedType() {
        return processedType;
    }

    public void setProcessedType(String processedType) {
        this.processedType = processedType;
    }
}
