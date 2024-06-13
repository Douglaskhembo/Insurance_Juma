package com.brokersystems.brokerapp.setup.dto;

public class CoaSubAccountsDTO {

    private Long coId;
    private String code;
    private String name;
    private String integration;
    private String accountsOrder;
    private Long mainAcctId;
    private Long mainAcctName;
    private Long acctTypeId;
    private String accTypeName;
    private String controlAccount;
    private String applicableToScl;
    private String sublass;
    private Long scId;

    public Long getScId() {
        return scId;
    }

    public void setScId(Long scId) {
        this.scId = scId;
    }

    public String getApplicableToScl() {
        return applicableToScl;
    }

    public void setApplicableToScl(String applicableToScl) {
        this.applicableToScl = applicableToScl;
    }

    public String getSublass() {
        return sublass;
    }

    public void setSublass(String sublass) {
        this.sublass = sublass;
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

    public Long getMainAcctId() {
        return mainAcctId;
    }

    public void setMainAcctId(Long mainAcctId) {
        this.mainAcctId = mainAcctId;
    }

    public Long getMainAcctName() {
        return mainAcctName;
    }

    public void setMainAcctName(Long mainAcctName) {
        this.mainAcctName = mainAcctName;
    }

    public Long getAcctTypeId() {
        return acctTypeId;
    }

    public void setAcctTypeId(Long acctTypeId) {
        this.acctTypeId = acctTypeId;
    }

    public String getAccTypeName() {
        return accTypeName;
    }

    public void setAccTypeName(String accTypeName) {
        this.accTypeName = accTypeName;
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
}
