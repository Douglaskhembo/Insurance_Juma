package com.brokersystems.brokerapp.uw.model;

import com.brokersystems.brokerapp.medical.model.CategoryMembers;
import com.brokersystems.brokerapp.setup.model.SubClassReqdDocs;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Created by HP on 8/14/2017.
 */
@Entity
@Table(name="sys_brk_rsk_docs")
public class RiskDocs {

    @Id
    @SequenceGenerator(name = "riskDocSeq",sequenceName = "risk_docs_seq",allocationSize=1)
    @GeneratedValue(generator = "riskDocSeq")
    @Column(name="rd_id")
    private Long rdId;

    @XmlTransient
    @ManyToOne
    @JoinColumn(name="rd_risk_id")
    private RiskTrans risk;

    @XmlTransient
    @ManyToOne
    @JoinColumn(name="rd_member_id")
    private CategoryMembers member;

    @XmlTransient
    @ManyToOne
    @JoinColumn(name="rd_req_id")
    private SubClassReqdDocs reqdDocs;

    @Column(name = "rd_loc_name")
    private String uploadedFileName;

    @Column(name = "rd_verifier")
    private String checkSum;

    @Column(name = "rd_content_type")
    private String contentType;

    @Transient
    private String polRevNo;

    @Transient
    private Long polId;

    @Transient
    private Long riskId;

    @Transient
    private  Long binderDetId;

    @Column(name = "rd_url")
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getRdId() {
        return rdId;
    }

    public void setRdId(Long rdId) {
        this.rdId = rdId;
    }

    public RiskTrans getRisk() {
        return risk;
    }

    public void setRisk(RiskTrans risk) {
        this.risk = risk;
    }

    public SubClassReqdDocs getReqdDocs() {
        return reqdDocs;
    }

    public void setReqdDocs(SubClassReqdDocs reqdDocs) {
        this.reqdDocs = reqdDocs;
    }

    public String getUploadedFileName() {
        return uploadedFileName;
    }

    public void setUploadedFileName(String uploadedFileName) {
        this.uploadedFileName = uploadedFileName;
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

    public CategoryMembers getMember() {
        return member;
    }

    public void setMember(CategoryMembers member) {
        this.member = member;
    }

    public String getPolRevNo() {
        return polRevNo;
    }

    public void setPolRevNo(String polRevNo) {
        this.polRevNo = polRevNo;
    }

    public Long getPolId() {
        return polId;
    }

    public void setPolId(Long polId) {
        this.polId = polId;
    }

    public Long getRiskId() {
        return riskId;
    }

    public void setRiskId(Long riskId) {
        this.riskId = riskId;
    }

    public Long getBinderDetId() {
        return binderDetId;
    }

    public void setBinderDetId(Long binderDetId) {
        this.binderDetId = binderDetId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RiskDocs riskDocs = (RiskDocs) o;

        return reqdDocs.equals(riskDocs.reqdDocs);

    }

    @Override
    public int hashCode() {
        return reqdDocs.hashCode();
    }
}
