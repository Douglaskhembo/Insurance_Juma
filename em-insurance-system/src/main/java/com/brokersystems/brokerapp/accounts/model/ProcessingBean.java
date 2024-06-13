package com.brokersystems.brokerapp.accounts.model;

import java.util.List;

/**
 * Created by peter on 3/27/2017.
 */
public class ProcessingBean {

    private List<InsPaymentBean> credits;
    private Long accountCode;
    private Long subaccountType;
    private Long curCode;

    public List<InsPaymentBean> getCredits() {
        return credits;
    }

    public void setCredits(List<InsPaymentBean> credits) {
        this.credits = credits;
    }

    public Long getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(Long accountCode) {
        this.accountCode = accountCode;
    }

    public Long getCurCode() {
        return curCode;
    }

    public void setCurCode(Long curCode) {
        this.curCode = curCode;
    }

    public Long getSubaccountType() {
        return subaccountType;
    }

    public void setSubaccountType(Long subaccountType) {
        this.subaccountType = subaccountType;
    }
}

