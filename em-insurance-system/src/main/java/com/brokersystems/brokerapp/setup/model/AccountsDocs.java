package com.brokersystems.brokerapp.setup.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Created by HP on 10/7/2017.
 */
@Entity
@Table(name="sys_brk_acct_docs")
public class AccountsDocs {

    @Id
    @SequenceGenerator(name = "acctDocSeq",sequenceName = "account_docs_seq",allocationSize=1)
    @GeneratedValue(generator = "acctDocSeq")
    @Column(name="ad_id")
    private Long adId;

    @XmlTransient
    @ManyToOne
    @JoinColumn(name="ad_client_id")
    private AccountDef accountDef;

    @Column(name = "ad_loc_name")
    private String uploadedFileName;

    @Column(name = "ad_verifier")
    private String checkSum;

    @Column(name = "ad_content_type")
    private String contentType;

    @ManyToOne
    @JoinColumn(name="ad_req_code",nullable=false)
    private RequiredDocs requiredDoc;

    public Long getAdId() {
        return adId;
    }

    public void setAdId(Long adId) {
        this.adId = adId;
    }

    public AccountDef getAccountDef() {
        return accountDef;
    }

    public void setAccountDef(AccountDef accountDef) {
        this.accountDef = accountDef;
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

    public RequiredDocs getRequiredDoc() {
        return requiredDoc;
    }

    public void setRequiredDoc(RequiredDocs requiredDoc) {
        this.requiredDoc = requiredDoc;
    }
}
