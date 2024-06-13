package com.brokersystems.brokerapp.accounts.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name="sys_brk_rpt_format_totals")
public class FinalReportFormatTotals {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="rft_id")
    private Long rftId;

    @Column(name="rft_sign")
    private Boolean sign;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="rft_total")
    private FinalReportFormats total;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="rft_column")
    private FinalReportFormats column;

    public Long getRftId() {
        return rftId;
    }

    public void setRftId(Long rftId) {
        this.rftId = rftId;
    }

    public Boolean getSign() {
        return sign;
    }

    public void setSign(Boolean sign) {
        this.sign = sign;
    }

    public FinalReportFormats getTotal() {
        return total;
    }

    public void setTotal(FinalReportFormats total) {
        this.total = total;
    }

    public FinalReportFormats getColumn() {
        return column;
    }

    public void setColumn(FinalReportFormats column) {
        this.column = column;
    }
}
