package com.brokersystems.brokerapp.claims.dtos;

import lombok.Data;

public class ClaimRisksDTO {

    private final Long riskId;
    private final String riskShtDesc;
    private final String polno;
    private final String clientname;
    private final Long binderId;
    private final Long binderDetId;
    private final Long polId;

    private ClaimRisksDTO(final Long riskId, final String riskShtDesc, final String polno, final String clientname, final Long binderId, final Long binderDetId, final Long polId) {
        this.riskId = riskId;
        this.riskShtDesc = riskShtDesc;
        this.polno = polno;
        this.clientname = clientname;
        this.binderId = binderId;
        this.binderDetId = binderDetId;
        this.polId = polId;
    }

    public static ClaimRisksDTO instance(final Long riskId, final String riskShtDesc, final String polno, final String clientname, final Long binderId, final Long binderDetId, final Long polId) {
        return new ClaimRisksDTO(riskId, riskShtDesc, polno, clientname, binderId, binderDetId, polId);
    }

    public Long getRiskId() {
        return riskId;
    }

    public String getRiskShtDesc() {
        return riskShtDesc;
    }

    public String getPolno() {
        return polno;
    }

    public String getClientname() {
        return clientname;
    }

    public Long getBinderId() {
        return binderId;
    }

    public Long getBinderDetId() {
        return binderDetId;
    }

    public Long getPolId() {
        return polId;
    }
}
