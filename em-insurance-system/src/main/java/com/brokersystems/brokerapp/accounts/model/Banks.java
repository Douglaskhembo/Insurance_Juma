package com.brokersystems.brokerapp.accounts.model;

import javax.persistence.*;

/**
 * Created by peter on 4/10/2017.
 */
@Entity
@Table(name="sys_brk_banks")
public class Banks {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="bn_Id")
    private Long bnId;

    @Column(name="bn_name",nullable = false,unique = true)
    private String bankName;

    @Column(name="bn_sht_desc",nullable = false,unique = true)
    private String bankShtDesc;

    @Column(name="bn_remarks",length = 1500)
    private String bankRemarks;

    @Column(name="bn_eft_support")
    private boolean eftSupported;

    @Column(name="bn_active")
    private boolean active;

    public Long getBnId() {
        return bnId;
    }

    public void setBnId(Long bnId) {
        this.bnId = bnId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankShtDesc() {
        return bankShtDesc;
    }

    public void setBankShtDesc(String bankShtDesc) {
        this.bankShtDesc = bankShtDesc;
    }

    public String getBankRemarks() {
        return bankRemarks;
    }

    public void setBankRemarks(String bankRemarks) {
        this.bankRemarks = bankRemarks;
    }

    public boolean isEftSupported() {
        return eftSupported;
    }

    public void setEftSupported(boolean eftSupported) {
        this.eftSupported = eftSupported;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
