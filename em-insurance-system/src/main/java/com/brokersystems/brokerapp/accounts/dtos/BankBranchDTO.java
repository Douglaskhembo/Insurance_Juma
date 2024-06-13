package com.brokersystems.brokerapp.accounts.dtos;

public class BankBranchDTO {

    private Long bbId;
    private String branchName;

    public Long getBbId() {
        return bbId;
    }

    public void setBbId(Long bbId) {
        this.bbId = bbId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

}
