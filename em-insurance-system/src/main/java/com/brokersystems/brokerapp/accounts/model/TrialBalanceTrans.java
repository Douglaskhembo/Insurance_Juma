package com.brokersystems.brokerapp.accounts.model;

import com.brokersystems.brokerapp.setup.model.User;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name="sys_brk_trial_trans")
public class TrialBalanceTrans {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="tbt_id")
    private Long tbtId;

    @Column(name = "tbt_gl_code",length = 30)
    private String glcode;

    @Column(name = "tbt_dr")
    private BigDecimal debits;
    @Column(name = "tbt_cr")
    private BigDecimal credits;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="tbt_usr_id",nullable=false)
    private User user;

    public Long getTbtId() {
        return tbtId;
    }

    public void setTbtId(Long tbtId) {
        this.tbtId = tbtId;
    }

    public String getGlcode() {
        return glcode;
    }

    public void setGlcode(String glcode) {
        this.glcode = glcode;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
