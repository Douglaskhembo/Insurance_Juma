package com.brokersystems.brokerapp.accounts.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name="sys_brk_rpt_format_grp_accts")
public class FinalReportFormatGroupAccounts {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="rfga_id")
    private Long rfgaId;

    @Column(name="rfga_sign")
    private Boolean sign;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="rfga_rf_id")
    private FinalReportFormats reportFormats;

    @Column(name="rfa_acct_no",length = 50)
    private String accountNo;

    public Long getRfgaId() {
        return rfgaId;
    }

    public void setRfgaId(Long rfgaId) {
        this.rfgaId = rfgaId;
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
