package com.brokersystems.brokerapp.accounts.model;

import com.brokersystems.brokerapp.setup.model.OrgBranch;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="sys_brk_account_years")
public class AccountYears {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="bay_Id")
    private Long bayId;

    @Column(name = "bay_year")
    private Long year;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="bay_ob_id",nullable = false)
    private OrgBranch branch;

    @Column(name="bay_wef",nullable=false)
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date yearStart;

    @Column(name="bay_wet",nullable=false)
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date yearEnd;

    @Column(name="bay_prds",nullable = false)
    private Integer totalPeriods;

    @Column(name="bay_state",nullable = false,length = 1)
    private String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getBayId() {
        return bayId;
    }

    public void setBayId(Long bayId) {
        this.bayId = bayId;
    }

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public OrgBranch getBranch() {
        return branch;
    }

    public void setBranch(OrgBranch branch) {
        this.branch = branch;
    }

    public Date getYearStart() {
        return yearStart;
    }

    public void setYearStart(Date yearStart) {
        this.yearStart = yearStart;
    }

    public Date getYearEnd() {
        return yearEnd;
    }

    public void setYearEnd(Date yearEnd) {
        this.yearEnd = yearEnd;
    }

    public Integer getTotalPeriods() {
        return totalPeriods;
    }

    public void setTotalPeriods(Integer totalPeriods) {
        this.totalPeriods = totalPeriods;
    }
}
