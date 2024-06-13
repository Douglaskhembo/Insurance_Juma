package com.brokersystems.brokerapp.claims.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "sys_brk_clm_pymnts_dtls")
public class ClaimPaymentDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "cpd_id")
    private Long paymentDetailId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="cpd_peril_id",nullable=false)
    private ClaimPerils claimPerils;

    @Column(name = "cpd_amount")
    private BigDecimal clmPymntAmount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="clm_pymt_spd_id")
    private ClaimPayments claimPayments;

    public Long getPaymentDetailId() {
        return paymentDetailId;
    }

    public void setPaymentDetailId(Long paymentDetailId) {
        this.paymentDetailId = paymentDetailId;
    }

    public ClaimPerils getClaimPerils() {
        return claimPerils;
    }

    public void setClaimPerils(ClaimPerils claimPerils) {
        this.claimPerils = claimPerils;
    }

    public BigDecimal getClmPymntAmount() {
        return clmPymntAmount;
    }

    public void setClmPymntAmount(BigDecimal clmPymntAmount) {
        this.clmPymntAmount = clmPymntAmount;
    }

    public ClaimPayments getClaimPayments() {
        return claimPayments;
    }

    public void setClaimPayments(ClaimPayments claimPayments) {
        this.claimPayments = claimPayments;
    }
}
