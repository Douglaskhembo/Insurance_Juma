package com.brokersystems.brokerapp.trans.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ChequeTransDTO {
    private Long paymentModeId;
    private BigDecimal amount;
    private Long ctNo;
    private Long payee;
    private Long bankActCode;
    private Long branchCode;
    private Long curId;
    private String paymentType;
    private String narration;
    private String invoiceNo;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date invoiceDate;
    private Date requistionDate;
    private String refNo;
    private String bankAcctName;
    private String currency;
    private String branch;
    private String raisedUser;
    private String paymentMethod;
    private String payeeName;
    private Long sourcePostedUser;
    private Date sourcePostedDate;
    private String accountNo;
    private String origin;
    private String originType;

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getOriginType() {
        return originType;
    }

    public void setOriginType(String originType) {
        this.originType = originType;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public Long getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(Long branchCode) {
        this.branchCode = branchCode;
    }

    public Date getRequistionDate() {
        return requistionDate;
    }

    public void setRequistionDate(Date requistionDate) {
        this.requistionDate = requistionDate;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getBankAcctName() {
        return bankAcctName;
    }

    public void setBankAcctName(String bankAcctName) {
        this.bankAcctName = bankAcctName;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getRaisedUser() {
        return raisedUser;
    }

    public void setRaisedUser(String raisedUser) {
        this.raisedUser = raisedUser;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    private List<PettyCashDtlsDTO> pettyCashTrans;
    private List<ChequeTransDtlsDTO> glTrans;

    public List<PettyCashDtlsDTO> getPettyCashTrans() {
        return pettyCashTrans;
    }

    public void setPettyCashTrans(List<PettyCashDtlsDTO> pettyCashTrans) {
        this.pettyCashTrans = pettyCashTrans;
    }

    public List<ChequeTransDtlsDTO> getGlTrans() {
        return glTrans;
    }

    public void setGlTrans(List<ChequeTransDtlsDTO> glTrans) {
        this.glTrans = glTrans;
    }

    private String source;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Long getSourcePostedUser() {
        return sourcePostedUser;
    }

    public void setSourcePostedUser(Long sourcePostedUser) {
        this.sourcePostedUser = sourcePostedUser;
    }

    public Date getSourcePostedDate() {
        return sourcePostedDate;
    }

    public void setSourcePostedDate(Date sourcePostedDate) {
        this.sourcePostedDate = sourcePostedDate;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Long getCurId() {
        return curId;
    }

    public void setCurId(Long curId) {
        this.curId = curId;
    }

    public Long getBankActCode() {
        return bankActCode;
    }

    public void setBankActCode(Long bankActCode) {
        this.bankActCode = bankActCode;
    }

    public Long getPayee() {
        return payee;
    }

    public void setPayee(Long payee) {
        this.payee = payee;
    }

    public Long getCtNo() {
        return ctNo;
    }

    public void setCtNo(Long ctNo) {
        this.ctNo = ctNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getPaymentModeId() {
        return paymentModeId;
    }

    public void setPaymentModeId(Long paymentModeId) {
        this.paymentModeId = paymentModeId;
    }

    @Override
    public String toString() {
        return "ChequeTransDTO{" +
                "paymentModeId=" + paymentModeId +
                ", amount=" + amount +
                ", ctNo=" + ctNo +
                ", payee=" + payee +
                ", bankActCode=" + bankActCode +
                ", curId=" + curId +
                ", paymentType='" + paymentType + '\'' +
                ", narration='" + narration + '\'' +
                ", invoiceNo='" + invoiceNo + '\'' +
                ", invoiceDate=" + invoiceDate +
                ", sourcePostedUser=" + sourcePostedUser +
                ", sourcePostedDate=" + sourcePostedDate +
                ", pettyCashTrans=" + pettyCashTrans +
                ", glTrans=" + glTrans +
                ", source='" + source + '\'' +
                '}';
    }
}
