package com.brokersystems.brokerapp.claims.dtos;

import java.math.BigDecimal;

public class ClaimPerilReserveDTO {

    private String perilDesc;
    private String type;
    private BigDecimal limitAmt;
    private BigDecimal excessAmt;
    private BigDecimal reserve;
    private String remarks;
    private Long clmPerilId;

    public String getPerilDesc() {
        return perilDesc;
    }

    public void setPerilDesc(String perilDesc) {
        this.perilDesc = perilDesc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getLimitAmt() {
        return limitAmt;
    }

    public void setLimitAmt(BigDecimal limitAmt) {
        this.limitAmt = limitAmt;
    }

    public BigDecimal getExcessAmt() {
        return excessAmt;
    }

    public void setExcessAmt(BigDecimal excessAmt) {
        this.excessAmt = excessAmt;
    }

    public BigDecimal getReserve() {
        return reserve;
    }

    public void setReserve(BigDecimal reserve) {
        this.reserve = reserve;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Long getClmPerilId() {
        return clmPerilId;
    }

    public void setClmPerilId(Long clmPerilId) {
        this.clmPerilId = clmPerilId;
    }
}
