package com.brokersystems.brokerapp.setup.dto;

public class SubClassReqdDocsDTO {

    private Long sclReqrdId;
    private String reqShtDesc;
    private String reqDesc;

    public Long getSclReqrdId() {
        return sclReqrdId;
    }

    public void setSclReqrdId(Long sclReqrdId) {
        this.sclReqrdId = sclReqrdId;
    }

    public String getReqShtDesc() {
        return reqShtDesc;
    }

    public void setReqShtDesc(String reqShtDesc) {
        this.reqShtDesc = reqShtDesc;
    }

    public String getReqDesc() {
        return reqDesc;
    }

    public void setReqDesc(String reqDesc) {
        this.reqDesc = reqDesc;
    }
}
