package com.brokersystems.brokerapp.claims.model;

import com.brokersystems.brokerapp.setup.model.User;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by waititu on 09/09/2019.
 */
@Entity
@Table(name = "sys_brk_clm_peril_pymnts")
public class ClaimPerilPayments {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "clm_pymnt_id")
    private Long clmPymntId;

    @ManyToOne
    @JoinColumn(name="clm_pymnt_peril_id",nullable=false)
    private ClaimPerils claimPerils;

    @Column(name = "clm_pymnt_amount")
    private BigDecimal clmPymntAmount;

    @Column(name = "clm_pymnt_payee",nullable = false)
    private String payee;

    @Column(name = "clm_pymnt_ref",nullable = false)
    private String pymntRef;

    @Column(name = "clm_pymnt_type",nullable = false)
    private String pymntType;

    @Column(name = "clm_pymnt_date",nullable = false)
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date pymntDate;

    @ManyToOne
    @JoinColumn(name="clm_pymnt_by",nullable=false)
    private User capturedBy;

    @Column(name = "clm_pymnt_capture_date",nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date captureDate;

    @Column(name="clm_pymnt_auth_status")
    private String authStatus;

    @Column(name="clm_pymnt_auth_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date authDate;

    @XmlTransient
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="clm_pymnt_auth_user")
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


    public User getAuthUser() {
        return authUser;
    }

    public void setAuthUser(User authUser) {
        this.authUser = authUser;
    }

    public Long getClmPymntId() {
        return clmPymntId;
    }

    public void setClmPymntId(Long clmPymntId) {
        this.clmPymntId = clmPymntId;
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

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public String getPymntRef() {
        return pymntRef;
    }

    public void setPymntRef(String pymntRef) {
        this.pymntRef = pymntRef;
    }

    public Date getPymntDate() {
        return pymntDate;
    }

    public void setPymntDate(Date pymntDate) {
        this.pymntDate = pymntDate;
    }

    public User getCapturedBy() {
        return capturedBy;
    }

    public void setCapturedBy(User capturedBy) {
        this.capturedBy = capturedBy;
    }

    public Date getCaptureDate() {
        return captureDate;
    }

    public void setCaptureDate(Date captureDate) {
        this.captureDate = captureDate;
    }

    public String getPymntType() {
        return pymntType;
    }

    public void setPymntType(String pymntType) {
        this.pymntType = pymntType;
    }
}
