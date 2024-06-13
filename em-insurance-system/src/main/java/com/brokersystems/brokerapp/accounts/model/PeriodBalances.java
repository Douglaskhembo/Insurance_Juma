package com.brokersystems.brokerapp.accounts.model;

import com.brokersystems.brokerapp.setup.model.OrgBranch;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name="sys_brk_prd_balances")
public class PeriodBalances {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="pb_id")
    private Long pbId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="pb_yp_id",nullable=false)
    private AccountYearPeriods accountYearPeriods;
    @Column(name="pb_op_balance")
    private BigDecimal opBalance;
    @Column(name="pb_curr_balance")
    private BigDecimal currBalance;
    @Column(name="pb_gl_code",length = 50)
    private String accntNumber;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="pb_ob_id",nullable=false)
    private OrgBranch branch;

    public Long getPbId() {
        return pbId;
    }

    public void setPbId(Long pbId) {
        this.pbId = pbId;
    }

    public AccountYearPeriods getAccountYearPeriods() {
        return accountYearPeriods;
    }

    public void setAccountYearPeriods(AccountYearPeriods accountYearPeriods) {
        this.accountYearPeriods = accountYearPeriods;
    }

    public BigDecimal getOpBalance() {
        return opBalance;
    }

    public void setOpBalance(BigDecimal opBalance) {
        this.opBalance = opBalance;
    }

    public BigDecimal getCurrBalance() {
        return currBalance;
    }

    public void setCurrBalance(BigDecimal currBalance) {
        this.currBalance = currBalance;
    }

    public String getAccntNumber() {
        return accntNumber;
    }

    public void setAccntNumber(String accntNumber) {
        this.accntNumber = accntNumber;
    }

    public OrgBranch getBranch() {
        return branch;
    }

    public void setBranch(OrgBranch branch) {
        this.branch = branch;
    }
}
