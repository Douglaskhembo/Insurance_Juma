package com.brokersystems.brokerapp.accounts.dtos;

public class FinalReportFormatAcctsDTO {

    private Long rfaId;
    private String accountNo;

    private String accountName;
    private Boolean sign;
    private String affsign;

    private Long rfId;

    public String getAffsign() {
        return affsign;
    }

    public void setAffsign(String affsign) {
        this.affsign = affsign;
    }

    public Long getRfId() {
        return rfId;
    }

    public void setRfId(Long rfId) {
        this.rfId = rfId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Long getRfaId() {
        return rfaId;
    }

    public void setRfaId(Long rfaId) {
        this.rfaId = rfaId;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public Boolean getSign() {
        return sign;
    }

    public void setSign(Boolean sign) {
        this.sign = sign;
    }
}
