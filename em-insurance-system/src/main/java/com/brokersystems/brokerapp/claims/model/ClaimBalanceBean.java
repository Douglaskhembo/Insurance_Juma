package com.brokersystems.brokerapp.claims.model;

import java.math.BigDecimal;

/**
 * Created by HP on 9/29/2017.
 */
public class ClaimBalanceBean {

    private BigDecimal clientBalance;
    private BigDecimal insBalance;

    public BigDecimal getClientBalance() {
        return clientBalance;
    }

    public void setClientBalance(BigDecimal clientBalance) {
        this.clientBalance = clientBalance;
    }

    public BigDecimal getInsBalance() {
        return insBalance;
    }

    public void setInsBalance(BigDecimal insBalance) {
        this.insBalance = insBalance;
    }

    @Override
    public String toString() {
        return "ClaimBalanceBean{" +
                "clientBalance=" + clientBalance +
                ", insBalance=" + insBalance +
                '}';
    }
}
