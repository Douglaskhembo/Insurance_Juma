package com.brokersystems.brokerapp.accounts.model;

import com.brokersystems.brokerapp.setup.model.AccountTypes;
import com.brokersystems.brokerapp.setup.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name="sys_brk_trial_balances")
public class AccountsTrialBalances {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="tb_id")
    private Long tbId;

    @Column(name = "tb_acc_no",length = 50, nullable = false)
    private String accountNo;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="tb_user_id")
    private User generatedBy;

    @Column(name="tb_bal")
    private BigDecimal balance;

    @Column(name="tb_dr")
    private BigDecimal debits;

    @Column(name="tb_cr")
    private BigDecimal credits;

    @Column(name = "tb_bal_op")
    private boolean yearOperatingBal;

    @Column(name="tb__p_bal")
    private BigDecimal prevBalance;

    @Column(name="tb_p_dr")
    private BigDecimal prevDebits;

    @Column(name="tb_p_cr")
    private BigDecimal prevCredits;

    public Long getTbId() {
        return tbId;
    }

    public void setTbId(Long tbId) {
        this.tbId = tbId;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public User getGeneratedBy() {
        return generatedBy;
    }

    public void setGeneratedBy(User generatedBy) {
        this.generatedBy = generatedBy;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getDebits() {
        return debits;
    }

    public void setDebits(BigDecimal debits) {
        this.debits = debits;
    }

    public BigDecimal getCredits() {
        return credits;
    }

    public void setCredits(BigDecimal credits) {
        this.credits = credits;
    }

    public boolean isYearOperatingBal() {
        return yearOperatingBal;
    }

    public void setYearOperatingBal(boolean yearOperatingBal) {
        this.yearOperatingBal = yearOperatingBal;
    }

    public BigDecimal getPrevBalance() {
        return prevBalance;
    }

    public void setPrevBalance(BigDecimal prevBalance) {
        this.prevBalance = prevBalance;
    }

    public BigDecimal getPrevDebits() {
        return prevDebits;
    }

    public void setPrevDebits(BigDecimal prevDebits) {
        this.prevDebits = prevDebits;
    }

    public BigDecimal getPrevCredits() {
        return prevCredits;
    }

    public void setPrevCredits(BigDecimal prevCredits) {
        this.prevCredits = prevCredits;
    }
}
