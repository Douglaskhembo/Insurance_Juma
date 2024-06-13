package com.brokersystems.brokerapp.claims.dtos;

import java.util.Date;

public class ClaimRequiredDocsDTO {

    private Long clmRequiredId;
    private String remarks;
    private Date dateReceived;
    private String docRefNo;
    private String fileName;
    private String username;
    private String docName;
    private String claimStatus;

    public String getClaimStatus() {
        return claimStatus;
    }

    public void setClaimStatus(String claimStatus) {
        this.claimStatus = claimStatus;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public Long getClmRequiredId() {
        return clmRequiredId;
    }

    public void setClmRequiredId(Long clmRequiredId) {
        this.clmRequiredId = clmRequiredId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Date getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(Date dateReceived) {
        this.dateReceived = dateReceived;
    }

    public String getDocRefNo() {
        return docRefNo;
    }

    public void setDocRefNo(String docRefNo) {
        this.docRefNo = docRefNo;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
