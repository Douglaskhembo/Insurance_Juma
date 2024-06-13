package com.brokersystems.brokerapp.claims.model;

import com.brokersystems.brokerapp.setup.model.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by peter on 3/11/2017.
 */
@Entity
@Table(name = "sys_brk_clm_uploads")
public class ClaimUploads {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "cu_id")
    private Long uploadId;

    @ManyToOne
    @JoinColumn(name="cu_clm_id",nullable=false)
    private ClaimBookings claimBookings;

    @Column(name = "cu_file_id")
    private String fileId;

    @Column(name = "cu_file_name")
    private String fileName;

    @ManyToOne
    @JoinColumn(name="cu_uploaded_by",nullable=false)
    private User uploadedBy;

    @Column(name = "cu_uploaded_dt")
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date dateUploaded;

    @Column(name = "cu_comment",length = 2000)
    private String uploadedComment;

    @Column(name = "cu_verifier")
    private String checkSum;

    @Column(name = "rd_content_type")
    private String contentType;

    @Transient
    MultipartFile file;

    public Long getUploadId() {
        return uploadId;
    }

    public void setUploadId(Long uploadId) {
        this.uploadId = uploadId;
    }

    public ClaimBookings getClaimBookings() {
        return claimBookings;
    }

    public void setClaimBookings(ClaimBookings claimBookings) {
        this.claimBookings = claimBookings;
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

    public User getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(User uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public Date getDateUploaded() {
        return dateUploaded;
    }

    public void setDateUploaded(Date dateUploaded) {
        this.dateUploaded = dateUploaded;
    }

    public String getUploadedComment() {
        return uploadedComment;
    }

    public void setUploadedComment(String uploadedComment) {
        this.uploadedComment = uploadedComment;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(String checkSum) {
        this.checkSum = checkSum;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
