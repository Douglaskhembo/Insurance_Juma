package com.brokersystems.brokerapp.accounts.model;

import com.brokersystems.brokerapp.setup.model.Currencies;
import com.brokersystems.brokerapp.setup.model.PaymentModes;

import javax.persistence.*;

/**
 * Created by HP on 8/20/2017.
 */
@Entity
@Table(name="sys_brk_collect_accts")
public class CollectionAccounts {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="ca_Id")
    private Long caId;

    @ManyToOne
    @JoinColumn(name="ca_bb_id")
    private BankBranches bankBranches;

    @Column(name="ca_name",nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name="ca_acc_id",nullable = false)
    private CoaSubAccounts accounts;

    @ManyToOne
    @JoinColumn(name="ca_pm_id",nullable = false)
    private PaymentModes paymentModes;

    @ManyToOne
    @JoinColumn(name="ca_cur_id",nullable = false)
    private Currencies currencies;

    @Column(name="ca_status")
    private boolean status;



    public Long getCaId() {
        return caId;
    }

    public void setCaId(Long caId) {
        this.caId = caId;
    }

    public BankBranches getBankBranches() {
        return bankBranches;
    }

    public void setBankBranches(BankBranches bankBranches) {
        this.bankBranches = bankBranches;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CoaSubAccounts getAccounts() {
        return accounts;
    }

    public void setAccounts(CoaSubAccounts accounts) {
        this.accounts = accounts;
    }

    public PaymentModes getPaymentModes() {
        return paymentModes;
    }

    public void setPaymentModes(PaymentModes paymentModes) {
        this.paymentModes = paymentModes;
    }

    public Currencies getCurrencies() {
        return currencies;
    }

    public void setCurrencies(Currencies currencies) {
        this.currencies = currencies;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
