package com.brokersystems.brokerapp.claims.model;

import com.brokersystems.brokerapp.setup.model.User;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by peter on 3/8/2017.
 */
@Entity
@Table(name = "sys_brk_clm_revisions")
public class ClaimRevisions {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "clm_rev_id")
    private Long revisionId;

    @Column(name = "clm_rev_date")
    private Date revDate;

    @ManyToOne
    @JoinColumn(name="clm_rev_clm_id",nullable=false)
    private ClaimBookings claimBookings;

    @Column(name = "clm_rev_amt")
    private BigDecimal revAmount;

    @Column(name = "clm_rev_trans_type")
    private String transType;

    @Column(name="clm_rev_auth_status")
    private String authStatus;

    @Column(name="clm_rev_auth_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date authDate;

    @XmlTransient
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="clm_rev_create_user")
    private User createdUser;

    @XmlTransient
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="clm_rev_auth_user")
    private User authUser;

    public String getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(String authStatus) {
        this.authStatus = authStatus;
    }

    public Date getAuthDate() {
        return authDate;
    }

    public void setAuthDate(Date authDate) {
        this.authDate = authDate;
    }

    public User getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(User createdUser) {
        this.createdUser = createdUser;
    }

    public User getAuthUser() {
        return authUser;
    }

    public void setAuthUser(User authUser) {
        this.authUser = authUser;
    }

    public Long getRevisionId() {
        return revisionId;
    }

    public void setRevisionId(Long revisionId) {
        this.revisionId = revisionId;
    }

    public Date getRevDate() {
        return revDate;
    }

    public void setRevDate(Date revDate) {
        this.revDate = revDate;
    }

    public ClaimBookings getClaimBookings() {
        return claimBookings;
    }

    public void setClaimBookings(ClaimBookings claimBookings) {
        this.claimBookings = claimBookings;
    }

    public BigDecimal getRevAmount() {
        return revAmount;
    }

    public void setRevAmount(BigDecimal revAmount) {
        this.revAmount = revAmount;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }
}
