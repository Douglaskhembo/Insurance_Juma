package com.brokersystems.brokerapp.claims.model;

import com.brokersystems.brokerapp.accounts.model.Payees;
import com.brokersystems.brokerapp.setup.model.BankAccounts;
import com.brokersystems.brokerapp.setup.model.Currencies;
import com.brokersystems.brokerapp.setup.model.PaymentModes;
import com.brokersystems.brokerapp.setup.model.User;
import com.brokersystems.brokerapp.trans.model.ChequeTrans;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "sys_brk_clm_pymnts")
public class ClaimPayments {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "clm_pymnt_id")
    private Long clmPymntId;

    @Column(name = "clm_pymt_created_dt",nullable=false)
    private Date transDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="clm_pymt_created_by",nullable=false)
    private User user;

    @Column(name = "clm_pymt_auth")
    private String authorised;

    @Column(name = "clm_pymt_amount")
    private BigDecimal clmPymntAmount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="clm_pymt_auth_by")
    private User authBy;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="clm_pymt_clmnt_id")
    private ClaimClaimants claimClaimants;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="clm_pymt_spd_id")
    private ServiceProviderDef serviceProviderDef;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="clm_pymt_clm_id",nullable=false)
    private ClaimBookings claimBookings;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="clm_pymt_ba_acc")
    private BankAccounts bankAccounts;

    @Column(name = "clm_pymt_trans_type", length = 10)
    private String tranType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="clm_pymt_pm_id")
    private PaymentModes paymentModes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="clm_pymt_payee")
    private Payees payee;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="clm_pymt_cur_id")
    private Currencies currencies;

    @Column(name = "clm_pymt_bcur_amount")
    private BigDecimal clmPymntBcurAmount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="clm_pymt_chq_id")
    private ChequeTrans chequeTrans;

    @Column(name = "clm_pymt_invoice_no", length = 20)
    private String invoiceNo;
    @Column(name = "clm_pymt_inv_date")
    private Date invoiceDate;

    @Column(name = "clm_pymt_auth_date")
    private Date authDate;

    public Date getAuthDate() {
        return authDate;
    }

    public void setAuthDate(Date authDate) {
        this.authDate = authDate;
    }

    public BankAccounts getBankAccounts() {
        return bankAccounts;
    }

    public void setBankAccounts(BankAccounts bankAccounts) {
        this.bankAccounts = bankAccounts;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Long getClmPymntId() {
        return clmPymntId;
    }

    public void setClmPymntId(Long clmPymntId) {
        this.clmPymntId = clmPymntId;
    }

    public Date getTransDate() {
        return transDate;
    }

    public void setTransDate(Date transDate) {
        this.transDate = transDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAuthorised() {
        return authorised;
    }

    public void setAuthorised(String authorised) {
        this.authorised = authorised;
    }

    public BigDecimal getClmPymntAmount() {
        return clmPymntAmount;
    }

    public void setClmPymntAmount(BigDecimal clmPymntAmount) {
        this.clmPymntAmount = clmPymntAmount;
    }

    public User getAuthBy() {
        return authBy;
    }

    public void setAuthBy(User authBy) {
        this.authBy = authBy;
    }

    public ClaimClaimants getClaimClaimants() {
        return claimClaimants;
    }

    public void setClaimClaimants(ClaimClaimants claimClaimants) {
        this.claimClaimants = claimClaimants;
    }

    public ServiceProviderDef getServiceProviderDef() {
        return serviceProviderDef;
    }

    public void setServiceProviderDef(ServiceProviderDef serviceProviderDef) {
        this.serviceProviderDef = serviceProviderDef;
    }

    public ClaimBookings getClaimBookings() {
        return claimBookings;
    }

    public void setClaimBookings(ClaimBookings claimBookings) {
        this.claimBookings = claimBookings;
    }

    public String getTranType() {
        return tranType;
    }

    public void setTranType(String tranType) {
        this.tranType = tranType;
    }

    public PaymentModes getPaymentModes() {
        return paymentModes;
    }

    public void setPaymentModes(PaymentModes paymentModes) {
        this.paymentModes = paymentModes;
    }

    public Payees getPayee() {
        return payee;
    }

    public void setPayee(Payees payee) {
        this.payee = payee;
    }

    public Currencies getCurrencies() {
        return currencies;
    }

    public void setCurrencies(Currencies currencies) {
        this.currencies = currencies;
    }

    public BigDecimal getClmPymntBcurAmount() {
        return clmPymntBcurAmount;
    }

    public void setClmPymntBcurAmount(BigDecimal clmPymntBcurAmount) {
        this.clmPymntBcurAmount = clmPymntBcurAmount;
    }

    public ChequeTrans getChequeTrans() {
        return chequeTrans;
    }

    public void setChequeTrans(ChequeTrans chequeTrans) {
        this.chequeTrans = chequeTrans;
    }
}
