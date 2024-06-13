package com.brokersystems.brokerapp.claims.model;

import com.brokersystems.brokerapp.setup.model.RequiredDocs;
import com.brokersystems.brokerapp.setup.model.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by peter on 3/8/2017.
 */
@Entity
@Table(name = "sys_brk_clm_req_docs")
public class ClaimRequiredDocs {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "clm_reqd_id")
    private Long clmRequiredId;

    @ManyToOne
    @JoinColumn(name="clm_reqrd_req_id",nullable=false)
    private RequiredDocs requiredDoc;

    @ManyToOne
    @JoinColumn(name="clm_reqrd_clm_id",nullable=false)
    private ClaimBookings claimBookings;

    @Column(name = "clm_reqd_submitted")
    private String submitted;

    @ManyToOne
    @JoinColumn(name="clm_reqrd_user_id")
    private User userReceived;

    @Column(name = "clm_reqd_dt_received")
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date dateReceived;

    @Column(name = "clm_reqd_doc_ref")
    private String docRefNo;

    @Column(name = "clm_reqd_dt_submit")
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date dateSubmitted;

    @Column(name = "clm_reqd_remarks")
    private String remarks;

    @Column(name = "clm_reqd_file_name")
    private String fileName;

    @Column(name = "clm_reqd_verifier")
    private String checkSum;

    @Column(name = "clm_reqd_content_type")
    private String contentType;

    @Transient
    private String transType;

    @Transient
    MultipartFile file;


    public Long getClmRequiredId() {
        return clmRequiredId;
    }

    public void setClmRequiredId(Long clmRequiredId) {
        this.clmRequiredId = clmRequiredId;
    }

    public RequiredDocs getRequiredDoc() {
        return requiredDoc;
    }

    public void setRequiredDoc(RequiredDocs requiredDoc) {
        this.requiredDoc = requiredDoc;
    }

    public ClaimBookings getClaimBookings() {
        return claimBookings;
    }

    public void setClaimBookings(ClaimBookings claimBookings) {
        this.claimBookings = claimBookings;
    }

    public String getSubmitted() {
        return submitted;
    }

    public void setSubmitted(String submitted) {
        this.submitted = submitted;
    }

    public User getUserReceived() {
        return userReceived;
    }

    public void setUserReceived(User userReceived) {
        this.userReceived = userReceived;
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

    public Date getDateSubmitted() {
        return dateSubmitted;
    }

    public void setDateSubmitted(Date dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }



    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }
}
