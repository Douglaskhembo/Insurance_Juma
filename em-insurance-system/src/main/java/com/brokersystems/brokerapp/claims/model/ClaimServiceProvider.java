package com.brokersystems.brokerapp.claims.model;

import com.brokersystems.brokerapp.setup.model.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "sys_brk_clm_srv_provider")
public class ClaimServiceProvider {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "csp_id")
    private Long sprId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="csp_clm_id",nullable=false)
    private ClaimBookings claimBookings;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="csp_spr_id",nullable=false)
    private ServiceProviderDef serviceProvider;

    @Column(name = "csp_created_dt")
    private Date createdDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="csp_created_user")
    private User createdUser;


    public Long getSprId() {
        return sprId;
    }

    public void setSprId(Long sprId) {
        this.sprId = sprId;
    }

    public ClaimBookings getClaimBookings() {
        return claimBookings;
    }

    public void setClaimBookings(ClaimBookings claimBookings) {
        this.claimBookings = claimBookings;
    }

    public ServiceProviderDef getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProviderDef serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public User getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(User createdUser) {
        this.createdUser = createdUser;
    }
}
