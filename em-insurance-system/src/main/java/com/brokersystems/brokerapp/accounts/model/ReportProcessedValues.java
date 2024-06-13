package com.brokersystems.brokerapp.accounts.model;

import com.brokersystems.brokerapp.setup.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name="sys_brk_rpt_processed_vals")
public class ReportProcessedValues {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="rpv_Id")
    private Long refId;

    @Column(name="rpv_row_Id")
    private Long refRowId;

    @Column(name="rpv_amount")
    private BigDecimal refAmount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="rpv_user_Id",nullable=false)
    private User user;

    @Column(name="rpv_bgt_amount")
    private BigDecimal budgetAmt;

    @Column(name="rpv_var_amount")
    private BigDecimal varianceAmt;

    @Column(name="rpv_var_perc")
    private BigDecimal variancePercent;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="rpv_clas_id")
    private AccountsBusinessClasses financialClasses;

    @Column(name="rpv_prev_amount")
    private BigDecimal refPrevAmount;

    @Column(name="rpv_prev_bgt_amount")
    private BigDecimal prevBudgetAmt;

    @Column(name="rpv_month_amount")
    private BigDecimal monthAmt;

    @Column(name="rpv_prev_month_amount")
    private BigDecimal prevMonthAmt;

    @Column(name="rpv_prev_mon_bgt")
    private BigDecimal prevMonthBudgetAmt;

    public Long getRefId() {
        return refId;
    }

    public void setRefId(Long refId) {
        this.refId = refId;
    }

    public Long getRefRowId() {
        return refRowId;
    }

    public void setRefRowId(Long refRowId) {
        this.refRowId = refRowId;
    }

    public BigDecimal getRefAmount() {
        return refAmount;
    }

    public void setRefAmount(BigDecimal refAmount) {
        this.refAmount = refAmount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getBudgetAmt() {
        return budgetAmt;
    }

    public void setBudgetAmt(BigDecimal budgetAmt) {
        this.budgetAmt = budgetAmt;
    }

    public BigDecimal getVarianceAmt() {
        return varianceAmt;
    }

    public void setVarianceAmt(BigDecimal varianceAmt) {
        this.varianceAmt = varianceAmt;
    }

    public BigDecimal getVariancePercent() {
        return variancePercent;
    }

    public void setVariancePercent(BigDecimal variancePercent) {
        this.variancePercent = variancePercent;
    }

    public AccountsBusinessClasses getFinancialClasses() {
        return financialClasses;
    }

    public void setFinancialClasses(AccountsBusinessClasses financialClasses) {
        this.financialClasses = financialClasses;
    }

    public BigDecimal getRefPrevAmount() {
        return refPrevAmount;
    }

    public void setRefPrevAmount(BigDecimal refPrevAmount) {
        this.refPrevAmount = refPrevAmount;
    }

    public BigDecimal getPrevBudgetAmt() {
        return prevBudgetAmt;
    }

    public void setPrevBudgetAmt(BigDecimal prevBudgetAmt) {
        this.prevBudgetAmt = prevBudgetAmt;
    }

    public BigDecimal getMonthAmt() {
        return monthAmt;
    }

    public void setMonthAmt(BigDecimal monthAmt) {
        this.monthAmt = monthAmt;
    }

    public BigDecimal getPrevMonthAmt() {
        return prevMonthAmt;
    }

    public void setPrevMonthAmt(BigDecimal prevMonthAmt) {
        this.prevMonthAmt = prevMonthAmt;
    }

    public BigDecimal getPrevMonthBudgetAmt() {
        return prevMonthBudgetAmt;
    }

    public void setPrevMonthBudgetAmt(BigDecimal prevMonthBudgetAmt) {
        this.prevMonthBudgetAmt = prevMonthBudgetAmt;
    }
}
