package com.brokersystems.brokerapp.accounts.dtos;

import java.util.Date;

public class AccountingPeriodDTO {

    private String branch;
    private String periodName;
    private String transacted;
    private Long periodId;
    private Date wef;
    private Date wet;
    private String status;
    private String userClosed;
    private Date closedDate;

    public String getPeriodName() {
        return periodName;
    }

    public void setPeriodName(String periodName) {
        this.periodName = periodName;
    }

    public String getTransacted() {
        return transacted;
    }

    public void setTransacted(String transacted) {
        this.transacted = transacted;
    }

    public Long getPeriodId() {
        return periodId;
    }

    public void setPeriodId(Long periodId) {
        this.periodId = periodId;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public Date getWef() {
        return wef;
    }

    public void setWef(Date wef) {
        this.wef = wef;
    }

    public Date getWet() {
        return wet;
    }

    public void setWet(Date wet) {
        this.wet = wet;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserClosed() {
        return userClosed;
    }

    public void setUserClosed(String userClosed) {
        this.userClosed = userClosed;
    }

    public Date getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(Date closedDate) {
        this.closedDate = closedDate;
    }
}
