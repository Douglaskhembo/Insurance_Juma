package com.brokersystems.brokerapp.claims.model;

import com.brokersystems.brokerapp.medical.model.ServiceProviderContracts;
import com.brokersystems.brokerapp.setup.model.ClmCausations;
import com.brokersystems.brokerapp.setup.model.User;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by peter on 4/7/2017.
 */
@Entity
@Table(name = "sys_brk_clm_activities")
public class ClaimActivities {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "clm_act_id")
    private Long activityId;

    @ManyToOne
    @JoinColumn(name="clm_act_clm_id",nullable=false)
    private ClaimBookings claimBookings;

    @ManyToOne
    @JoinColumn(name="clm_act_user_id",nullable=false)
    private User userCreated;

    @ManyToOne
    @JoinColumn(name="clm_act_status_id",nullable=false)
    private ClmCausations activity;

    @Column(name = "clm_act_rem_dt")
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date remDate;

    @Column(name = "clm_act_dt")
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date activityDate;

    @Column(name = "clm_act_status")
    private String currentActivity;

    @ManyToOne
    @JoinColumn(name="clm_act_review_user")
    private User reviewUser;

    @ManyToOne
    @JoinColumn(name="clm_act_spc_id")
    private ServiceProviderContracts serviceProvider;


    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public ClaimBookings getClaimBookings() {
        return claimBookings;
    }

    public void setClaimBookings(ClaimBookings claimBookings) {
        this.claimBookings = claimBookings;
    }

    public User getUserCreated() {
        return userCreated;
    }

    public void setUserCreated(User userCreated) {
        this.userCreated = userCreated;
    }

    public ClmCausations getActivity() {
        return activity;
    }

    public void setActivity(ClmCausations activity) {
        this.activity = activity;
    }

    public Date getRemDate() {
        return remDate;
    }

    public void setRemDate(Date remDate) {
        this.remDate = remDate;
    }

    public Date getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(Date activityDate) {
        this.activityDate = activityDate;
    }

    public String getCurrentActivity() {
        return currentActivity;
    }

    public ServiceProviderContracts getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProviderContracts serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public void setCurrentActivity(String currentActivity) {
        this.currentActivity = currentActivity;
    }

    public User getReviewUser() {
        return reviewUser;
    }

    public void setReviewUser(User reviewUser) {
        this.reviewUser = reviewUser;
    }
}
