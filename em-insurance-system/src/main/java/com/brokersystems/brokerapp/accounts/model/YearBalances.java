package com.brokersystems.brokerapp.accounts.model;

import com.brokersystems.brokerapp.setup.model.OrgBranch;
import com.brokersystems.brokerapp.uw.model.PolicyTrans;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name="sys_brk_yr_balances")
public class YearBalances {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="yb_id")
    private Long ybId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="yb_yr_id",nullable=false)
    private AccountYears accountYears;
    @Column(name="yb_op_balance")
    private BigDecimal opBalance;
    @Column(name="yb_curr_balance")
    private BigDecimal currBalance;
    @Column(name="yb_gl_code",length = 50)
    private String accntNumber;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="yb_ob_id",nullable=false)
    private OrgBranch branch;

    public Long getYbId() {
        return ybId;
    }

    public void setYbId(Long ybId) {
        this.ybId = ybId;
    }

    public AccountYears getAccountYears() {
        return accountYears;
    }

    public void setAccountYears(AccountYears accountYears) {
        this.accountYears = accountYears;
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
