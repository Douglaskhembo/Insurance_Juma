package com.brokersystems.brokerapp.claims.model;

import com.brokersystems.brokerapp.setup.model.Occupation;
import com.brokersystems.brokerapp.setup.model.User;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by peter on 3/5/2017.
 */
@Entity
@Table(name = "sys_brk_serv_providers")
public class ServiceProviderDef {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "provd_id")
    private Long providerId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="provd_type_id")
    private ServiceProviderTypes providerTypes ;

    @Column(name = "provd_name",nullable = false)
    private String name;

    @Column(name = "provd_mobile",nullable = false)
    private String phoneNumber;

    @Column(name = "provd_email")
    private String email;
    private Date createdDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="created_user")
    private User createdUser;

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

    public Long getProviderId() {
        return providerId;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }

    public ServiceProviderTypes getProviderTypes() {
        return providerTypes;
    }

    public void setProviderTypes(ServiceProviderTypes providerTypes) {
        this.providerTypes = providerTypes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
