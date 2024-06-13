package com.brokersystems.brokerapp.claims.dtos;

import java.util.Date;

public class ServiceProviderDTO {

    private Long providerId;
    private Long providerTypeId;
    private String name;
    private String phoneNumber;
    private String email;
    private Date createdDate;
    private String createdBy;

    public Long getProviderTypeId() {
        return providerTypeId;
    }

    public void setProviderTypeId(Long providerTypeId) {
        this.providerTypeId = providerTypeId;
    }

    public Long getProviderId() {
        return providerId;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
