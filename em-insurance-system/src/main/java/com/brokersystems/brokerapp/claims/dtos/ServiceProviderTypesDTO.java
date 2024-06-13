package com.brokersystems.brokerapp.claims.dtos;

public class ServiceProviderTypesDTO {

    private Long typeId;
    private String providerType;


    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getProviderType() {
        return providerType;
    }

    public void setProviderType(String providerType) {
        this.providerType = providerType;
    }
}
