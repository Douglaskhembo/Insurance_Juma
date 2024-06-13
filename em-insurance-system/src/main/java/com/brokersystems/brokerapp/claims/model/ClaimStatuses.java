package com.brokersystems.brokerapp.claims.model;

import com.brokersystems.brokerapp.setup.model.User;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by waititu on 10/09/2019.
 */
@Entity
@Table(name = "sys_brk_clm_statuses")
public class ClaimStatuses {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "clm_sts_id")
    private Long clmStatusId;

    @Column(name = "clm_sts_status")
    private String currentStatus;

    @Column(name = "clm_close_reason")
    private String closeReason;

    @Column(name = "clm_sts_current")
    private String currentActivity;

    @Column(name = "clm_sts_oldstatus")
    private String oldStatus;

    @Column(name = "clm_sts_date",nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCaptured;

    @ManyToOne
    @JoinColumn(name="clm_sts_user")
    private User capturedBy;

    @ManyToOne
    @JoinColumn(name="clm_sts_clm_id",nullable=false)
    private ClaimBookings claimBookings;

    @Column(name = "clm_sts_remarks",length = 2000)
    private String remarks;


    @Transient
    private String newStatus;


    public Long getClmStatusId() {
        return clmStatusId;
    }

    public void setClmStatusId(Long clmStatusId) {
        this.clmStatusId = clmStatusId;
    }

    public String getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(String currentActivity) {
        this.currentActivity = currentActivity;
    }

    public Date getDateCaptured() {
        return dateCaptured;
    }

    public void setDateCaptured(Date dateCaptured) {
        this.dateCaptured = dateCaptured;
    }

    public User getCapturedBy() {
        return capturedBy;
    }

    public void setCapturedBy(User capturedBy) {
        this.capturedBy = capturedBy;
    }

    public ClaimBookings getClaimBookings() {
        return claimBookings;
    }

    public void setClaimBookings(ClaimBookings claimBookings) {
        this.claimBookings = claimBookings;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(String oldStatus) {
        this.oldStatus = oldStatus;
    }

    public String getCloseReason() {
        return closeReason;
    }

    public void setCloseReason(String closeReason) {
        this.closeReason = closeReason;
    }
}
