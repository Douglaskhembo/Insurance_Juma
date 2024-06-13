package com.brokersystems.brokerapp.setup.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class UserBranchesDTO {

    private Long userBranchId;
    private Long userId;
    private Long branchId;
    private String username;
    private String branchName;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dateAssigned;
    private String userAssigned;

    public Long getUserBranchId() {
        return userBranchId;
    }

    public void setUserBranchId(Long userBranchId) {
        this.userBranchId = userBranchId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public Date getDateAssigned() {
        return dateAssigned;
    }

    public void setDateAssigned(Date dateAssigned) {
        this.dateAssigned = dateAssigned;
    }

    public String getUserAssigned() {
        return userAssigned;
    }

    public void setUserAssigned(String userAssigned) {
        this.userAssigned = userAssigned;
    }
}
