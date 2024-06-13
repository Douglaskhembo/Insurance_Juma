package com.brokersystems.brokerapp.claims.model;

import java.math.BigDecimal;

/**
 * Created by peter on 3/6/2017.
 */
public class PerilBean {

    private Long perilCode;

    private String selfAsClaimant;
    private BigDecimal perilEstimate;

    private Long claimId;
    private Long claimantCode;

    public Long getClaimantCode() {
        return claimantCode;
    }

    public void setClaimantCode(Long claimantCode) {
        this.claimantCode = claimantCode;
    }

    public String getSelfAsClaimant() {
        return selfAsClaimant;
    }

    public void setSelfAsClaimant(String selfAsClaimant) {
        this.selfAsClaimant = selfAsClaimant;
    }

    public Long getPerilCode() {
        return perilCode;
    }

    public void setPerilCode(Long perilCode) {
        this.perilCode = perilCode;
    }

    public BigDecimal getPerilEstimate() {
        return perilEstimate;
    }

    public void setPerilEstimate(BigDecimal perilEstimate) {
        this.perilEstimate = perilEstimate;
    }

    public Long getClaimId() {
        return claimId;
    }

    public void setClaimId(Long claimId) {
        this.claimId = claimId;
    }

}
