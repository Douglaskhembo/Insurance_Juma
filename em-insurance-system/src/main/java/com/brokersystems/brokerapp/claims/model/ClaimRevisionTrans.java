package com.brokersystems.brokerapp.claims.model;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by peter on 3/8/2017.
 */
@Entity
@Table(name = "sys_brk_clm_rev_trans")
public class ClaimRevisionTrans {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "clm_rv_trans_id")
    private Long revTransId;

    @ManyToOne
    @JoinColumn(name="clm_rv_prl_id",nullable=false)
    private ClaimPerils claimPeril;

    @Column(name = "clm_rv_type")
    private String type;

    @Column(name = "clm_rv_amount",nullable = false)
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name="clm_rv_rev_id",nullable=false)
    private ClaimRevisions claimRevision;



    public Long getRevTransId() {
        return revTransId;
    }

    public void setRevTransId(Long revTransId) {
        this.revTransId = revTransId;
    }

    public ClaimPerils getClaimPeril() {
        return claimPeril;
    }

    public void setClaimPeril(ClaimPerils claimPeril) {
        this.claimPeril = claimPeril;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public ClaimRevisions getClaimRevision() {
        return claimRevision;
    }

    public void setClaimRevision(ClaimRevisions claimRevision) {
        this.claimRevision = claimRevision;
    }
}
