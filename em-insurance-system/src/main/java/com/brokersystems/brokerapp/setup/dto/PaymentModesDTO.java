package com.brokersystems.brokerapp.setup.dto;

public class PaymentModesDTO {

    private Long pmId;
    private String pmDesc;

    public Long getPmId() {
        return pmId;
    }

    public void setPmId(Long pmId) {
        this.pmId = pmId;
    }

    public String getPmDesc() {
        return pmDesc;
    }

    public void setPmDesc(String pmDesc) {
        this.pmDesc = pmDesc;
    }
}
