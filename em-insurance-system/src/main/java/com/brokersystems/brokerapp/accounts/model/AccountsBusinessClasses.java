package com.brokersystems.brokerapp.accounts.model;

import javax.persistence.*;

@Entity
@Table(name="sys_brk_acct_classes")
public class AccountsBusinessClasses {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="bac_Id")
    private Long bacId;
    @Column(name="bac_desc")
    private String bclDesc;
    @Column(name="bac_order")
    private Integer order;


    public String getBclDesc() {
        return bclDesc;
    }

    public void setBclDesc(String bclDesc) {
        this.bclDesc = bclDesc;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Long getBacId() {
        return bacId;
    }

    public void setBacId(Long bacId) {
        this.bacId = bacId;
    }
}
