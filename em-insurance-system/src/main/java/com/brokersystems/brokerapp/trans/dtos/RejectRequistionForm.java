package com.brokersystems.brokerapp.trans.dtos;

public class RejectRequistionForm {

    private Long reqId;
    private String reason;

    public Long getReqId() {
        return reqId;
    }

    public void setReqId(Long reqId) {
        this.reqId = reqId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
