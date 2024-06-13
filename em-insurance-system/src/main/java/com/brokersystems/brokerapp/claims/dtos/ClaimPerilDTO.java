package com.brokersystems.brokerapp.claims.dtos;

public class ClaimPerilDTO {
    private Long bindPerilCode;
    private String perilDesc;

    public Long getBindPerilCode() {
        return bindPerilCode;
    }

    public void setBindPerilCode(Long bindPerilCode) {
        this.bindPerilCode = bindPerilCode;
    }

    public String getPerilDesc() {
        return perilDesc;
    }

    public void setPerilDesc(String perilDesc) {
        this.perilDesc = perilDesc;
    }
}
