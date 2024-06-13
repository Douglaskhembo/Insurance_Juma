package com.brokersystems.brokerapp.accounts.model;

import com.brokersystems.brokerapp.setup.model.OrgBranch;
import com.brokersystems.brokerapp.setup.model.User;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="sys_brk_account_yr_prds")
public class AccountYearPeriods {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="yp_Id")
    private Long ypId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="yp_bay_id",nullable = false)
    private AccountYears accountYears;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="yp_ob_id",nullable = false)
    private OrgBranch branch;

    @Column(name="yp_wef",nullable=false)
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date monthStart;

    @Column(name="yp_wet",nullable=false)
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date monthEnd;

    @Column(name="yp_state",nullable = false,length = 1)
    private String state;

    @Column(name="yp_transacted",length = 1)
    private String transacted;

    @Column(name = "yp_period_name",length = 20)
    private String periodName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="yp_closed_by")
    private User closedBy;

    @Column(name="yp_closed_date")
    private Date closedDate;

    public String getPeriodName() {
        return periodName;
    }

    public void setPeriodName(String periodName) {
        this.periodName = periodName;
    }

    public Date getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(Date closedDate) {
        this.closedDate = closedDate;
    }

    public Long getYpId() {
        return ypId;
    }

    public void setYpId(Long ypId) {
        this.ypId = ypId;
    }

    public AccountYears getAccountYears() {
        return accountYears;
    }

    public void setAccountYears(AccountYears accountYears) {
        this.accountYears = accountYears;
    }

    public OrgBranch getBranch() {
        return branch;
    }

    public void setBranch(OrgBranch branch) {
        this.branch = branch;
    }

    public Date getMonthStart() {
        return monthStart;
    }

    public void setMonthStart(Date monthStart) {
        this.monthStart = monthStart;
    }

    public Date getMonthEnd() {
        return monthEnd;
    }

    public void setMonthEnd(Date monthEnd) {
        this.monthEnd = monthEnd;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTransacted() {
        return transacted;
    }

    public void setTransacted(String transacted) {
        this.transacted = transacted;
    }

    public User getClosedBy() {
        return closedBy;
    }

    public void setClosedBy(User closedBy) {
        this.closedBy = closedBy;
    }
}
