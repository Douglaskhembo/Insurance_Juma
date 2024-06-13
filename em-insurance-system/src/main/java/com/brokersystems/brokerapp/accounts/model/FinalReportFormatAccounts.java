package com.brokersystems.brokerapp.accounts.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name="sys_brk_rpt_format_accts")
public class FinalReportFormatAccounts {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="rfa_id")
    private Long rfaId;

    @Column(name="rfa_sign")
    private Boolean sign;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="rfa_rf_id")
    private FinalReportFormats reportFormats;

    @Column(name="rfa_acct_no",length = 50)
    private String accountNo;

    @Column(name="rfa_acct_name",length = 300)
    private String accountName;

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Long getRfaId() {
        return rfaId;
    }

    public void setRfaId(Long rfaId) {
        this.rfaId = rfaId;
    }

    public Boolean getSign() {
        return sign;
    }

    public void setSign(Boolean sign) {
        this.sign = sign;
    }

    public FinalReportFormats getReportFormats() {
        return reportFormats;
    }

    public void setReportFormats(FinalReportFormats reportFormats) {
        this.reportFormats = reportFormats;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }
}
