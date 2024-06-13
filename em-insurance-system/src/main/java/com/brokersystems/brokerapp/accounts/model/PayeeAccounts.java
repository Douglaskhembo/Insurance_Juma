package com.brokersystems.brokerapp.accounts.model;

import javax.persistence.*;

@Entity
@Table(name="sys_brk_payee_accounts")
public class PayeeAccounts {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="payc_Id")
    private Long paycId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="payc_bb_id")
    private BankBranches bankBranches;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="payc_pay_id")
    private Payees payees;

    @Column(name="payc_account_no")
    private String accountNo;

    @Column(name="payc_status")
    private String status;

    public Payees getPayees() {
        return payees;
    }

    public void setPayees(Payees payees) {
        this.payees = payees;
    }

    public Long getPaycId() {
        return paycId;
    }

    public void setPaycId(Long paycId) {
        this.paycId = paycId;
    }

    public BankBranches getBankBranches() {
        return bankBranches;
    }

    public void setBankBranches(BankBranches bankBranches) {
        this.bankBranches = bankBranches;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
