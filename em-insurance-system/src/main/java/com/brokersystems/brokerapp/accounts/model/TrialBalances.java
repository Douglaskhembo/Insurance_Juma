package com.brokersystems.brokerapp.accounts.model;

import com.brokersystems.brokerapp.setup.model.User;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name="sys_brk_trial_bals")
public class TrialBalances {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="tb_id")
    private Long tbId;
    @Column(name = "tb_gl_code",length = 30)
    private String glcode;
    @Column(name = "tb_balance")
    private BigDecimal balance;
    @Column(name = "tb_dr")
    private BigDecimal debits;
    @Column(name = "tb_cr")
    private BigDecimal credits;
    @Column(name = "tb_yr_bal")
    private BigDecimal yopbalance;
    @Column(name = "tb_pre_bal")
    private BigDecimal prevbalance;
    @Column(name = "tb_pre_drs")
    private BigDecimal prevdebits;
    @Column(name = "tb_pre_crs")
    private BigDecimal prevcredits;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="tb_usr_id",nullable=false)
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getTbId() {
        return tbId;
    }

    public void setTbId(Long tbId) {
        this.tbId = tbId;
    }

    public String getGlcode() {
        return glcode;
    }

    public void setGlcode(String glcode) {
        this.glcode = glcode;
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

    public BigDecimal getYopbalance() {
        return yopbalance;
    }

    public void setYopbalance(BigDecimal yopbalance) {
        this.yopbalance = yopbalance;
    }

    public BigDecimal getPrevbalance() {
        return prevbalance;
    }

    public void setPrevbalance(BigDecimal prevbalance) {
        this.prevbalance = prevbalance;
    }

    public BigDecimal getPrevdebits() {
        return prevdebits;
    }

    public void setPrevdebits(BigDecimal prevdebits) {
        this.prevdebits = prevdebits;
    }

    public BigDecimal getPrevcredits() {
        return prevcredits;
    }

    public void setPrevcredits(BigDecimal prevcredits) {
        this.prevcredits = prevcredits;
    }
}
