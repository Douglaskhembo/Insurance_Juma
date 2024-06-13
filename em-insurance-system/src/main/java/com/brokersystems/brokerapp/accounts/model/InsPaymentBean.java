package com.brokersystems.brokerapp.accounts.model;

import java.math.BigDecimal;

/**
 * Created by peter on 3/27/2017.
 */
public class InsPaymentBean {

    private String debiTrans;
    private BigDecimal amount;
    private String creditTrans;
    private String transType;
    private Long acctCode;


    public Long getAcctCode() {
        return acctCode;
    }

    public void setAcctCode(Long acctCode) {
        this.acctCode = acctCode;
    }

    public String getDebiTrans() {
        return debiTrans;
    }

    public void setDebiTrans(String debiTrans) {
        this.debiTrans = debiTrans;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCreditTrans() {
        return creditTrans;
    }

    public void setCreditTrans(String creditTrans) {
        this.creditTrans = creditTrans;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    @Override
    public String toString() {
        return "InsPaymentBean{" +
                "debiTrans='" + debiTrans + '\'' +
                ", amount=" + amount +
                ", creditTrans='" + creditTrans + '\'' +
                ", transType='" + transType + '\'' +
                '}';
    }
}
