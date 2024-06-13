package com.brokersystems.brokerapp.claims.dtos;

import java.util.Date;

public class ClaimUploadsDTO {

    private Long uploadId;
    private String fileId;
    private String fileName;
    private Date dateUploaded;
    private String uploadedBy;
    private String uploadedComment;
    private String claimStatus;

    public String getClaimStatus() {
        return claimStatus;
    }

    public void setClaimStatus(String claimStatus) {
        this.claimStatus = claimStatus;
    }

    public Long getUploadId() {
        return uploadId;
    }

    public void setUploadId(Long uploadId) {
        this.uploadId = uploadId;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getDateUploaded() {
        return dateUploaded;
    }

    public void setDateUploaded(Date dateUploaded) {
        this.dateUploaded = dateUploaded;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public String getUploadedComment() {
        return uploadedComment;
    }

    public void setUploadedComment(String uploadedComment) {
        this.uploadedComment = uploadedComment;
    }
}
