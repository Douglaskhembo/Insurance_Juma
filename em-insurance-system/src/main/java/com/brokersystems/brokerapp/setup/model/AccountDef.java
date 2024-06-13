
package com.brokersystems.brokerapp.setup.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;

import com.brokersystems.brokerapp.accounts.model.BankBranches;
import com.brokersystems.brokerapp.accounts.model.Banks;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "sys_brk_accounts")

public class AccountDef extends AuditBaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	public AccountDef(String acctId) {
		this.acctId = new Long(acctId);
	}

	public AccountDef() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "acct_id")
	private Long acctId;

	@XmlTransient
	@ManyToOne
	@JoinColumn(name = "acct_acc_code")
	private AccountTypes accountType;

	@Column(name="acct_logo_url")
	private String logo;

	@Column(name = "acct_name", nullable = false)
	private String name;

	@Column(name = "acct_sht_desc")
	private String shtDesc;

	@Column(name = "acct_address")
	private String address;
	
	@Column(name = "acct_phys_address")
	private String physaddress;
	
	@Column(name = "acct_contact_title")
	private String contactTitle;
	
	@Column(name = "acct_contact_person")
	private String contactPerson;

	@Column(name = "acct_pin")
	private String pinNo;

	@Column(name = "acct_license_no")
	private String licenseNumber;


	@Column(name = "acct_email")
	private String email;

	@Column(name = "acct_phone", nullable = false)
	private String phoneNo;

	@JsonIgnore
	@XmlTransient
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "acct_brn_code")
	private OrgBranch branch;

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Temporal(TemporalType.DATE)
	@Column(name = "acct_dob")
	private Date dob;

	@Column(name = "acct_status")
	private String status;

	@JsonIgnore
	@XmlTransient
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "acct_bnk_code")
	private Banks banks;

	@JsonIgnore
	@XmlTransient
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "acct_branch_code")
	private BankBranches bankBranches;

	@Column(name = "acct_bnk_account")
	private String bankAccount;

	@Column(name = "acct_paybill")
	private String paybillNumber;

	@JsonIgnore
	@XmlTransient
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="acct_pmode_id")
	private PaymentModes paymentMode;

	@Column(name = "acct_pay_tel_no")
	private String payTelNo;

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Temporal(TemporalType.DATE)
	@Column(name = "acct_wef")
	private Date wef;


	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Temporal(TemporalType.DATE)
	@Column(name = "acct_wet")
	private Date wet;

	@JsonIgnore
	@ManyToOne(fetch= FetchType.EAGER)
	@JoinColumn(name="acct_parent_acct_id")
	private AccountDef parentAccount;

	@Column(name = "acct_deactivated")
	private Date deActivatedDate;
	@JsonIgnore
	@ManyToOne(fetch= FetchType.EAGER)
	@JoinColumn(name="acct_activated_user")
	private User activatedUser;

	@JsonIgnore
	@ManyToOne(fetch= FetchType.EAGER)
	@JoinColumn(name="acct_deactivated_user")
	private User deActivatedUser;

	@Column(name = "acct_uw_integration_url", length = 200)
	private String underwriterApiUrl;
	@Column(name = "acct_uw_integration_type", length = 20)
	private String integrationType;
	@Column(name = "acct_uw_integration_username", length = 100)
	private String underwriterApiUsername;
	@Column(name = "acct_uw_integration_password", length = 100)
	private String underwriterApiPassword;

	public String getUnderwriterApiUrl() {
		return underwriterApiUrl;
	}

	public void setUnderwriterApiUrl(String underwriterApiUrl) {
		this.underwriterApiUrl = underwriterApiUrl;
	}

	public String getIntegrationType() {
		return integrationType;
	}

	public void setIntegrationType(String integrationType) {
		this.integrationType = integrationType;
	}

	public String getUnderwriterApiUsername() {
		return underwriterApiUsername;
	}

	public void setUnderwriterApiUsername(String underwriterApiUsername) {
		this.underwriterApiUsername = underwriterApiUsername;
	}

	public String getUnderwriterApiPassword() {
		return underwriterApiPassword;
	}

	public void setUnderwriterApiPassword(String underwriterApiPassword) {
		this.underwriterApiPassword = underwriterApiPassword;
	}

	public Date getDeActivatedDate() {
		return deActivatedDate;
	}

	public void setDeActivatedDate(Date deActivatedDate) {
		this.deActivatedDate = deActivatedDate;
	}

	public User getActivatedUser() {
		return activatedUser;
	}

	public void setActivatedUser(User activatedUser) {
		this.activatedUser = activatedUser;
	}

	public User getDeActivatedUser() {
		return deActivatedUser;
	}

	public void setDeActivatedUser(User deActivatedUser) {
		this.deActivatedUser = deActivatedUser;
	}

	public Long getAcctId() {
		return acctId;
	}

	public void setAcctId(Long acctId) {
		this.acctId = acctId;
	}

	public AccountTypes getAccountType() {
		return accountType;
	}

	public void setAccountType(AccountTypes accountType) {
		this.accountType = accountType;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShtDesc() {
		return shtDesc;
	}

	public void setShtDesc(String shtDesc) {
		this.shtDesc = shtDesc;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhysaddress() {
		return physaddress;
	}

	public void setPhysaddress(String physaddress) {
		this.physaddress = physaddress;
	}

	public String getContactTitle() {
		return contactTitle;
	}

	public void setContactTitle(String contactTitle) {
		this.contactTitle = contactTitle;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getPinNo() {
		return pinNo;
	}

	public void setPinNo(String pinNo) {
		this.pinNo = pinNo;
	}

	public String getLicenseNumber() {
		return licenseNumber;
	}

	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public OrgBranch getBranch() {
		return branch;
	}

	public void setBranch(OrgBranch branch) {
		this.branch = branch;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	public Banks getBanks() {
		return banks;
	}

	public void setBanks(Banks banks) {
		this.banks = banks;
	}

	public BankBranches getBankBranches() {
		return bankBranches;
	}

	public void setBankBranches(BankBranches bankBranches) {
		this.bankBranches = bankBranches;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getPaybillNumber() {
		return paybillNumber;
	}

	public void setPaybillNumber(String paybillNumber) {
		this.paybillNumber = paybillNumber;
	}

	public PaymentModes getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(PaymentModes paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getPayTelNo() {
		return payTelNo;
	}

	public void setPayTelNo(String payTelNo) {
		this.payTelNo = payTelNo;
	}

	public Date getWef() {
		return wef;
	}

	public void setWef(Date wef) {
		this.wef = wef;
	}

	public Date getWet() {
		return wet;
	}

	public void setWet(Date wet) {
		this.wet = wet;
	}

	public AccountDef getParentAccount() {
		return parentAccount;
	}

	public void setParentAccount(AccountDef parentAccount) {
		this.parentAccount = parentAccount;
	}
}
