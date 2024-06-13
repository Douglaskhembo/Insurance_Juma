package com.brokersystems.brokerapp.setup.model;

import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

import javax.persistence.*;

@Entity
@Table(name="sys_brk_binders")
public class BindersDef extends AuditBaseEntity{
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="bin_id")
	private Long binId;
	
	@ManyToOne
	@JoinColumn(name="bin_acct_code",nullable=false)
	private AccountDef account;
	
	@Column(name="bin_status",nullable=false)
	private boolean active;
	
	@Column(name="bin_sht_desc",nullable=false)
	private String binShtDesc;
	
	@Column(name="bin_name",nullable=false)
	private String binName;

	@Column(name="bin_auth_status")
	private String binStatus;

	@Column(name="bin_pol_no",nullable=false)
	private String binPolNo;
	
	@ManyToOne
	@JoinColumn(name="bin_pr_code",nullable=false)
	private ProductsDef product;
	
	@Column(name="bin_default",nullable=false)
	private boolean binDefault;
	
	@Column(name="bin_remarks",nullable=false)
	private String binRemarks;
	
	@Column(name="bin_type",nullable=false)
	private String binType;
	
	@ManyToOne
	@JoinColumn(name="bin_cur_code",nullable=false)
	private Currencies currency;
	
	@Column(name="bin_min_prem")
	private BigDecimal minPrem;

	@Column(name="bin_fund_binder",length = 1)
	private String fundBinder;

	@Column(name="bin_min_term")
	private Integer minTerm;


	@Column(name="bin_max_term")
	private Integer maxTerm;

	@Column(name="bin_prem_age_type",length = 1)
	private String premiumAgeType;

	@Column(name="bin_medical_type",length = 1)
	private String medicalCoverType;

	@Column(name = "bin_max_exposure")
	private BigDecimal maxExposure;

	@Transient
	private String cloneBinRemarks;

	@Transient
	private String cloneBinType;

	@Transient
	private String cloneBinShtDesc;

	@Transient
	private String cloneBinName;

	@Transient
	private Long cloneProdId;

	@Transient
	private Long cloneAccId;

	@Transient
	private String cloneBinPolNo;

	@Transient
	private Long cloneFromBinCode;

	public BigDecimal getMaxExposure() {
		return maxExposure;
	}

	public void setMaxExposure(BigDecimal maxExposure) {
		this.maxExposure = maxExposure;
	}

	public String getBinStatus() {
		return binStatus;
	}

	public void setBinStatus(String binStatus) {
		this.binStatus = binStatus;
	}

	public Long getCloneFromBinCode() {
		return cloneFromBinCode;
	}

	public void setCloneFromBinCode(Long cloneFromBinCode) {
		this.cloneFromBinCode = cloneFromBinCode;
	}

	public String getCloneBinRemarks() {
		return cloneBinRemarks;
	}

	public void setCloneBinRemarks(String cloneBinRemarks) {
		this.cloneBinRemarks = cloneBinRemarks;
	}

	public String getCloneBinType() {
		return cloneBinType;
	}

	public void setCloneBinType(String cloneBinType) {
		this.cloneBinType = cloneBinType;
	}

	public String getCloneBinShtDesc() {
		return cloneBinShtDesc;
	}

	public void setCloneBinShtDesc(String cloneBinShtDesc) {
		this.cloneBinShtDesc = cloneBinShtDesc;
	}

	public String getCloneBinName() {
		return cloneBinName;
	}

	public void setCloneBinName(String cloneBinName) {
		this.cloneBinName = cloneBinName;
	}

	public Long getCloneProdId() {
		return cloneProdId;
	}

	public String getPremiumAgeType() {
		return premiumAgeType;
	}

	public void setPremiumAgeType(String premiumAgeType) {
		this.premiumAgeType = premiumAgeType;
	}

	public void setCloneProdId(Long cloneProdId) {
		this.cloneProdId = cloneProdId;
	}

	public Long getCloneAccId() {
		return cloneAccId;
	}

	public void setCloneAccId(Long cloneAccId) {
		this.cloneAccId = cloneAccId;
	}

	public String getCloneBinPolNo() {
		return cloneBinPolNo;
	}

	public void setCloneBinPolNo(String cloneBinPolNo) {
		this.cloneBinPolNo = cloneBinPolNo;
	}

	public Long getBinId() {
		return binId;
	}

	public void setBinId(Long binId) {
		this.binId = binId;
	}

	public AccountDef getAccount() {
		return account;
	}

	public void setAccount(AccountDef account) {
		this.account = account;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getBinShtDesc() {
		return binShtDesc;
	}

	public void setBinShtDesc(String binShtDesc) {
		this.binShtDesc = binShtDesc;
	}

	public String getBinName() {
		return binName;
	}

	public void setBinName(String binName) {
		this.binName = binName;
	}

	public String getBinPolNo() {
		return binPolNo;
	}

	public void setBinPolNo(String binPolNo) {
		this.binPolNo = binPolNo;
	}

	public ProductsDef getProduct() {
		return product;
	}

	public void setProduct(ProductsDef product) {
		this.product = product;
	}

	public boolean isBinDefault() {
		return binDefault;
	}

	public void setBinDefault(boolean binDefault) {
		this.binDefault = binDefault;
	}

	public String getBinRemarks() {
		return binRemarks;
	}

	public void setBinRemarks(String binRemarks) {
		this.binRemarks = binRemarks;
	}

	public String getBinType() {
		return binType;
	}

	public void setBinType(String binType) {
		this.binType = binType;
	}

	public Currencies getCurrency() {
		return currency;
	}

	public void setCurrency(Currencies currency) {
		this.currency = currency;
	}

	public BigDecimal getMinPrem() {
		return minPrem;
	}

	public void setMinPrem(BigDecimal minPrem) {
		this.minPrem = minPrem;
	}

	public String getFundBinder() {
		return fundBinder;
	}

	public void setFundBinder(String fundBinder) {
		this.fundBinder = fundBinder;
	}

	public Integer getMinTerm() {
		return minTerm;
	}

	public void setMinTerm(Integer minTerm) {
		this.minTerm = minTerm;
	}

	public Integer getMaxTerm() {
		return maxTerm;
	}

	public void setMaxTerm(Integer maxTerm) {
		this.maxTerm = maxTerm;
	}

	public String getMedicalCoverType() {
		return medicalCoverType;
	}

	public void setMedicalCoverType(String medicalCoverType) {
		this.medicalCoverType = medicalCoverType;
	}
}
