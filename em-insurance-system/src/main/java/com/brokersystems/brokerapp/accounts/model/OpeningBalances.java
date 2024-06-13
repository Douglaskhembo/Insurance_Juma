package com.brokersystems.brokerapp.accounts.model;

import com.brokersystems.brokerapp.setup.model.OrgBranch;
import com.brokersystems.brokerapp.setup.model.User;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name="sys_brk_opening_bals")
public class OpeningBalances {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="opb_Id")
    private Long opbId;

    @Column(name="opb_acct_no",length = 50)
    private String accountNo;

    @Column(name="opb_acct_name")
    private String accountName;

    @Column(name="opb_dr_amt")
    private BigDecimal debit;

    @Column(name="opb_cr_amt")
    private BigDecimal credit;

    @Column(name="opb_acct_yr")
    private Long acctYear;

    @Column(name="opb_balance")
    private BigDecimal balance;

    @Column(name="opb_acct_month",length = 20)
    private String acctPeriod;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="rpv_ob_Id",nullable=false)
    private OrgBranch branch;

    @Column(name="opb_processed_type",length = 1)
    private String type;

    public Long getOpbId() {
        return opbId;
    }

    public void setOpbId(Long opbId) {
        this.opbId = opbId;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public BigDecimal getDebit() {
        return debit;
    }

    public void setDebit(BigDecimal debit) {
        this.debit = debit;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public Long getAcctYear() {
        return acctYear;
    }

    public void setAcctYear(Long acctYear) {
        this.acctYear = acctYear;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getAcctPeriod() {
        return acctPeriod;
    }

    public void setAcctPeriod(String acctPeriod) {
        this.acctPeriod = acctPeriod;
    }

    public OrgBranch getBranch() {
        return branch;
    }

    public void setBranch(OrgBranch branch) {
        this.branch = branch;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
