package com.brokersystems.brokerapp.accounts.model;

import javax.persistence.*;

/**
 * Created by peter on 4/10/2017.
 */
@Entity
@Table(name="sys_brk_bank_branches")
public class BankBranches {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="bb_Id")
    private Long bbId;

    @ManyToOne
    @JoinColumn(name="bb_bn_id")
    private Banks bank;

    @Column(name="bb_name",nullable = false,unique = true)
    private String branchName;

    @Column(name="bb_sht_desc",nullable = false,unique = true)
    private String branchShtDesc;

    @Column(name="bb_ref_code",nullable = false,unique = true)
    private String branchRefCode;

    @Column(name="bb_remarks",length = 1000)
    private String branchRemarks;

    @Column(name="bb_eft_support")
    private boolean eftSupported;

    @Column(name="bb_physical_address")
    private String physicalAddress;

    @Column(name="bb_postal_address")
    private String postalAddress;

    public Long getBbId() {
        return bbId;
    }

    public void setBbId(Long bbId) {
        this.bbId = bbId;
    }

    public Banks getBank() {
        return bank;
    }

    public void setBank(Banks bank) {
        this.bank = bank;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBranchShtDesc() {
        return branchShtDesc;
    }

    public void setBranchShtDesc(String branchShtDesc) {
        this.branchShtDesc = branchShtDesc;
    }

    public String getBranchRefCode() {
        return branchRefCode;
    }

    public void setBranchRefCode(String branchRefCode) {
        this.branchRefCode = branchRefCode;
    }

    public String getBranchRemarks() {
        return branchRemarks;
    }

    public void setBranchRemarks(String branchRemarks) {
        this.branchRemarks = branchRemarks;
    }

    public boolean isEftSupported() {
        return eftSupported;
    }

    public void setEftSupported(boolean eftSupported) {
        this.eftSupported = eftSupported;
    }

    public String getPhysicalAddress() {
        return physicalAddress;
    }

    public void setPhysicalAddress(String physicalAddress) {
        this.physicalAddress = physicalAddress;
    }

    public String getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(String postalAddress) {
        this.postalAddress = postalAddress;
    }
}
