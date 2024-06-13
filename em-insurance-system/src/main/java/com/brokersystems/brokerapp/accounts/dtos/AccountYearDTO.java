package com.brokersystems.brokerapp.accounts.dtos;

import java.util.Date;

public class AccountYearDTO {

    private Long yearId;
    private Long year;
    private String branch;
    private Date wef;
    private Date wet;
    private String status;
    private Integer noofMonths;

    public Long getYearId() {
        return yearId;
    }

    public void setYearId(Long yearId) {
        this.yearId = yearId;
    }

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
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

    public Integer getNoofMonths() {
        return noofMonths;
    }

    public void setNoofMonths(Integer noofMonths) {
        this.noofMonths = noofMonths;
    }
}
