package com.brokersystems.brokerapp.claims.model;

import javax.persistence.*;

@Entity
@Table(name = "sys_brk_provider_types")
public class ServiceProviderTypes {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "spr_tp_id")
    private Long typeId;

    @Column(name = "spr_type")
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
