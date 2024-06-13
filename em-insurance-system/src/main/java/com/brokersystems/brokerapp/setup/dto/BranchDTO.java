package com.brokersystems.brokerapp.setup.dto;

public class BranchDTO {

    private Long obId;
    private String obName;
    private String obShtDesc;
    private String address;
    private String telNumber;
    private Long userCode;
    private Long regCode;
    private String username;
    private String headoffice;

    public Long getRegCode() {
        return regCode;
    }

    public void setRegCode(Long regCode) {
        this.regCode = regCode;
    }

    public String getObShtDesc() {
        return obShtDesc;
    }

    public void setObShtDesc(String obShtDesc) {
        this.obShtDesc = obShtDesc;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public Long getUserCode() {
        return userCode;
    }

    public void setUserCode(Long userCode) {
        this.userCode = userCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHeadoffice() {
        return headoffice;
    }

    public void setHeadoffice(String headoffice) {
        this.headoffice = headoffice;
    }

    public Long getObId() {
        return obId;
    }

    public void setObId(Long obId) {
        this.obId = obId;
    }

    public String getObName() {
        return obName;
    }

    public void setObName(String obName) {
        this.obName = obName;
    }
}
