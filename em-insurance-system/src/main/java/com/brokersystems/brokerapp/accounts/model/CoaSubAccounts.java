package com.brokersystems.brokerapp.accounts.model;

import com.brokersystems.brokerapp.setup.model.AccountTypes;
import com.brokersystems.brokerapp.setup.model.SubClassDef;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * Created by peter on 4/7/2017.
 */
@Entity
@Table(name="sys_brk_coa_sub")
public class CoaSubAccounts {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="co_Id")
    private Long coId;

    @Column(name="co_code",nullable = false,unique = true)
    private String code;

    @Column(name="co_name",nullable = false)
    private String name;

    @Column(name="co_int_account",nullable = false)
    private String integration;

    @Column(name="co_accounts_order",nullable = false)
    private String accountsOrder;

    @ManyToOne
    @JoinColumn(name="co_main_acc_id")
    private CoaMainAccounts mainAccounts;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="co_acct_type_id")
    private AccountTypes accountTypes;

    @Column(name="co_control_acc",length = 1)
    private String controlAccount;

    @Column(name="co_mapped_to_scl",length = 1)
    private String mappedToSubclass;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="co_sc_id")
    private SubClassDef subClassDef;

    @Transient
    private String applLevel;

    public String getMappedToSubclass() {
        return mappedToSubclass;
    }

    public void setMappedToSubclass(String mappedToSubclass) {
        this.mappedToSubclass = mappedToSubclass;
    }

    public SubClassDef getSubClassDef() {
        return subClassDef;
    }

    public void setSubClassDef(SubClassDef subClassDef) {
        this.subClassDef = subClassDef;
    }

    public AccountTypes getAccountTypes() {
        return accountTypes;
    }

    public void setAccountTypes(AccountTypes accountTypes) {
        this.accountTypes = accountTypes;
    }

    public String getControlAccount() {
        return controlAccount;
    }

    public void setControlAccount(String controlAccount) {
        this.controlAccount = controlAccount;
    }

    public Long getCoId() {
        return coId;
    }

    public void setCoId(Long coId) {
        this.coId = coId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntegration() {
        return integration;
    }

    public void setIntegration(String integration) {
        this.integration = integration;
    }

    public String getAccountsOrder() {
        return accountsOrder;
    }

    public void setAccountsOrder(String accountsOrder) {
        this.accountsOrder = accountsOrder;
    }

    public CoaMainAccounts getMainAccounts() {
        return mainAccounts;
    }

    public void setMainAccounts(CoaMainAccounts mainAccounts) {
        this.mainAccounts = mainAccounts;
    }

    public String getApplLevel() {
        return applLevel;
    }

    public void setApplLevel(String applLevel) {
        this.applLevel = applLevel;
    }
}
