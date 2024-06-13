package com.brokersystems.brokerapp.setup.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class ProspectsDTO {

    private Long tenId;

    private String prospShtDesc;
    private String fname;
    private String otherNames;
    private String phoneNo;
    private String clientType;
    private Long clientTypeId;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dob;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date polStartDate;
    private String comment;
    private String status;
    private String category;
    private Long acctId;
    private String acctName;
    private Long branchId;
    private String branchName;

    private String emailAddress;

    private Long prefixId;
    private String prefix;
    private String gender;
    private Long prodId;
    private Long quotationId;
    private Long clientTitle;

    public Long getClientTitle() {
        return clientTitle;
    }

    public void setClientTitle(Long clientTitle) {
        this.clientTitle = clientTitle;
    }

    public Date getPolStartDate() {
        return polStartDate;
    }

    public void setPolStartDate(Date polStartDate) {
        this.polStartDate = polStartDate;
    }

    public String getProspShtDesc() {
        return prospShtDesc;
    }

    public void setProspShtDesc(String prospShtDesc) {
        this.prospShtDesc = prospShtDesc;
    }

    public Long getProdId() {
        return prodId;
    }

    public void setProdId(Long prodId) {
        this.prodId = prodId;
    }

    public Long getQuotationId() {
        return quotationId;
    }

    public void setQuotationId(Long quotationId) {
        this.quotationId = quotationId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Long getPrefixId() {
        return prefixId;
    }

    public void setPrefixId(Long prefixId) {
        this.prefixId = prefixId;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public Long getClientTypeId() {
        return clientTypeId;
    }

    public void setClientTypeId(Long clientTypeId) {
        this.clientTypeId = clientTypeId;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getAcctId() {
        return acctId;
    }

    public void setAcctId(Long acctId) {
        this.acctId = acctId;
    }

    public String getAcctName() {
        return acctName;
    }

    public void setAcctName(String acctName) {
        this.acctName = acctName;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public Long getTenId() {
        return tenId;
    }

    public void setTenId(Long tenId) {
        this.tenId = tenId;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getOtherNames() {
        return otherNames;
    }

    public void setOtherNames(String otherNames) {
        this.otherNames = otherNames;
    }

}
