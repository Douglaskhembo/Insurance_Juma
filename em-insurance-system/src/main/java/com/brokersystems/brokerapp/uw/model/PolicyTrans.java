package com.brokersystems.brokerapp.uw.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;

import com.brokersystems.brokerapp.medical.model.BinderMedicalCards;
import com.brokersystems.brokerapp.setup.model.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
//@Cacheable(true)
@Table(name="sys_brk_policies")
public class PolicyTrans {
	
	@Id
	@SequenceGenerator(name = "myPolicySeq",sequenceName = "policy_seq",allocationSize=1)
	@GeneratedValue(generator = "myPolicySeq")
	@Column(name="pol_id")
	private Long policyId;
	
	@Column(name="pol_no",nullable=true)
	private String polNo;

	@Column(name="pol_proposal_no")
	private String proposalNo;

	@Column(name="pol_total_insts")
	private Integer totalInstalments;

	@Column(name="pol_paid_insts")
	private Integer paidInsts;

	@Column(name="pol_term")
	private Integer polTerm;
	
	@Column(name="pol_frequency")
	private String frequency;

	@Column(name="pol_coinsurance")
	private String coinsuranceBusiness;

	@Column(name="pol_error_message")
	private String error;

	public String getCoinsuranceBusiness() {
		return coinsuranceBusiness;
	}

	public void setCoinsuranceBusiness(String coinsuranceBusiness) {
		this.coinsuranceBusiness = coinsuranceBusiness;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Integer getPolTerm() {
		return polTerm;
	}

	public void setPolTerm(Integer polTerm) {
		this.polTerm = polTerm;
	}

	@Column(name="pol_old_no")

	private String OldpolNo;
	
	@Column(name="pol_client_pol_no")
	private String clientPolNo;
	
	@Column(name="pol_rev_no",nullable=false)
	private String polRevNo;
	
	@Column(name="pol_rev_status",nullable=false)
	private String polRevStatus;
	
	@Column(name="pol_date",nullable=false)
	@JsonFormat(pattern = "dd/MM/yyyy")
	@Temporal(TemporalType.DATE)
	private Date polCreateddt;
	
	@Column(name="pol_wef_date", nullable=false)
	@JsonFormat(pattern = "dd/MM/yyyy")
	@Temporal(TemporalType.DATE)
	private Date wefDate;

	@Column(name="pol_paidToDate", nullable=true)
	@JsonFormat(pattern = "dd/MM/yyyy")
	@Temporal(TemporalType.DATE)
	private Date polPaidToDate;
	
	@Column(name="pol_wet_date",nullable=false)
	@JsonFormat(pattern = "dd/MM/yyyy")
	@Temporal(TemporalType.DATE)
	private Date wetDate;
	
	@Column(name="pol_cover_from")
	@JsonFormat(pattern = "dd/MM/yyyy")
	@Temporal(TemporalType.DATE)
	private Date coverFrom;
	
	@Column(name="pol_cover_to")
	@JsonFormat(pattern = "dd/MM/yyyy")
	@Temporal(TemporalType.DATE)
	private Date coverTo;
	
	@Column(name="pol_ren_date")
	@JsonFormat(pattern = "dd/MM/yyyy")
	@Temporal(TemporalType.DATE)
	private Date renewalDate;

	@Transient
	private boolean importRisks;
	
	@XmlTransient
	@ManyToOne
	@JoinColumn(name="pol_client_id")
	private ClientDef client;
	
	@Transient
	private Long clientId;
	
	@XmlTransient
	@ManyToOne
	@JoinColumn(name="pol_agent_id")
	private AccountDef agent;

	@XmlTransient
	@ManyToOne
	@JoinColumn(name="pol_sub_agent_id")
	private AccountDef subAgent;


	@Column(name="pol_comm_rate")
	private BigDecimal commRate;
	
	@Transient
	private Long agentId;
	
	@XmlTransient
	@ManyToOne
	@JoinColumn(name="pol_branch_id")
	private OrgBranch branch;
	
	@Transient
	private Long branchId;

	
	@XmlTransient
	@ManyToOne
	@JoinColumn(name="pol_pmode_id")
	private PaymentModes paymentMode;
	
	@Transient
	private Long paymentId;
	
	@XmlTransient
	@ManyToOne
	@JoinColumn(name="pol_curr_id")
	private Currencies transCurrency;
	
	@Transient
	private Long currencyId;

	public Date getPolPaidToDate() {
		return polPaidToDate;
	}

	public void setPolPaidToDate(Date polPaidToDate) {
		this.polPaidToDate = polPaidToDate;
	}

	@Transient
	private Long subAgentId;
	
	@XmlTransient
	@ManyToOne
	@JoinColumn(name="pol_prod_id")
	private ProductsDef product;
	
	@Transient
	private Long prodId;
	
	@XmlTransient
	@ManyToOne
	@JoinColumn(name="pol_binder_id")
	private BindersDef binder;
	
	@Transient
	private Long bindCode;


	@Transient
	private Long[] bindCodes;

	@Transient
	private String bindName;

	@Transient
	private String productName;

	@Transient
	private Long cardId;

	@Transient
	private String quizTaken;

	@Transient
	private String insuranceCompany;
	
	@Column(name="pol_auth_status")
	private String authStatus;
	
	@Column(name="pol_auth_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date authDate;
	
	@Column(name="pol_ref_no")
	private String refNo;

	@Column(name="pol_revision_format")
	private String revisionFormat;
	
	@XmlTransient
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name="pol_prev_policy")
	private PolicyTrans previousTrans;
	
	@Column(name="pol_current_status")
	private String currentStatus;
	
	@XmlTransient
	@ManyToOne
	@JoinColumn(name="pol_auth_user")
	private User authBy;
	
	@XmlTransient
	@ManyToOne
	@JoinColumn(name="pol_created_user",nullable=false)
	private User createdUser;
	
	@Column(name="pol_sum_insur_amt")
	private BigDecimal sumInsured;
	
	@Column(name="pol_calc_premium_amt")
	private BigDecimal premium;
	
	@Column(name="pol_future_prem")
	private BigDecimal futurePrem;


	@Column(name="pol_prevfuture_prem")
	private BigDecimal prevFuturePrem;

	@Column(name="pol_prev_prem")
	private BigDecimal prevPrem;

	@Column(name="pol_com_allowed")
	private boolean commAllowed;

	@Column(name="pol_renewable")
	private boolean renewable;

	public Integer getTotalInstalments() {
		return totalInstalments;
	}

	public void setTotalInstalments(Integer totalInstalments) {
		this.totalInstalments = totalInstalments;
	}

	@Column(name="pol_uw_yr")
	private Integer uwYear;
	
	@Column(name="pol_interface_type")
	private String interfaceType;
	
	@Column(name="pol_trans_type")
	private String transType;
	
	@Column(name="pol_comm_amt")
	private BigDecimal commAmt;

	@Column(name="pol_sub_agent_amt")
	private BigDecimal subAgentComm;
	
	@Column(name="pol_basic_premium_amt")
	private BigDecimal basicPrem;

	@Column(name="pol_prevbasic_prem_amt")
	private BigDecimal prevBasicPrem;
	
	@Column(name="pol_net_premium_amt")
	private BigDecimal netPrem;

	@Column(name="pol_prevnet_premium_amt")
	private BigDecimal prevNetPrem;
	
	@Column(name="pol_stamp_duty")
	private BigDecimal stampDuty;
	
	@Column(name="pol_training_levy")
	private BigDecimal trainingLevy;
	
	@Column(name="pol_tot_train_levy")
	private BigDecimal totTrainingLevy;
	
	@Column(name="pol_whtx")
	private BigDecimal whtx;
	
	@Column(name="pol_extras")
	private BigDecimal extras;
	
	@Column(name="pol_phcf")
	private BigDecimal phcf;
	
	@Column(name="pol_tot_phcf")
	private BigDecimal totPhcf;
	
	@Column(name="pol_endos_basic_prem")
	private BigDecimal endosbasicPremium;

	@Column(name="pol_endos_gross_prem")
	private BigDecimal endosgrossPremium;

	@Column(name="pol_paid_prem")
	private BigDecimal paidPremium;

	@Column(name="pol_prevendos_basic_prem")
	private BigDecimal prevEndosbasicPremium;
	
	@Column(name="pol_endos_comm")
	private BigDecimal endosCommissions;

	@Column(name="pol_installment_no")
	private Long installmentNo;
	
	
	@JsonIgnore
	@OneToMany(mappedBy="policy")
	private List<RiskTrans> riskTrans;
	
	@JsonIgnore
	@OneToMany(mappedBy="policy")
	private List<PolicyTaxes> policyTaxes;
	
	@JsonIgnore
	@OneToMany(mappedBy="policy")
	private List<PolicyClauses> clauses;
	
	@Transient
	private RiskTransBean riskBean;
	
	@Transient
	private List<RiskSectionBean> sections;
	
	@Transient
	private Long prevPolicy;
	
	@Column(name="pol_business_type")
	private String businessType;

	@Column(name="pol_cancellation_type")
	private String cancellationType;

	@Column(name="pol_renewed")
	private boolean renewed=false;
	
	@XmlTransient
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name="pol_reucontra_policy")
	private PolicyTrans reusecontraPolicy;
	
	@Column(name="pol_renewal_error")
	private String renewalError;

	@Column(name="pol_beneficiary")
	private String beneficiary;

	@Column(name="pol_medical_type")
	private String medicalCoverType;

//	@Column(name = "pol_issue_card")
//	private String issueCard;

//	@Column(name = "pol_card_type")
//	private String cardType;

	@XmlTransient
	@ManyToOne
	@JoinColumn(name="pol_crd_id")
	private BinderMedicalCards binCardType;

	@Column(name = "pol_issue_card_fee")
	private BigDecimal issueCardFee;

	@Column(name = "pol_service_charge")
	private BigDecimal serviceCharge;

	@Column(name = "pol_reissue_card_fee")
	private BigDecimal reissueCardFee;

	@Column(name = "pol_vat_amt")
	private BigDecimal vatAmount;


	@Column(name="pol_total_sum_insured")
	private BigDecimal polTotSI;

	@Column(name="pol_total_prem")
	private BigDecimal polTotPrem;

	@Column(name="pol_total_SD")
	private BigDecimal polTotSD;

	@Column(name="pol_total_extras")
	private BigDecimal polTotExtras;

	@Column(name="pol_total_comm")
	private BigDecimal polTotComm;

	@Column(name="pol_total_whtx")
	private BigDecimal polTotWhtx;

	@Column(name="pol_refundable_premium")
	private BigDecimal refundablePremium;

	@Column(name="pol_refund_amount")
	private BigDecimal refundAmount;

	@Transient
	private String status;

	public BigDecimal getPolTotPrem() {
		return polTotPrem;
	}

	public void setPolTotPrem(BigDecimal polTotPrem) {
		this.polTotPrem = polTotPrem;
	}

	public BigDecimal getPolTotSI() {
		return polTotSI;
	}

	public void setPolTotSI(BigDecimal polTotSI) {
		this.polTotSI = polTotSI;
	}

	public BigDecimal getPolTotSD() {
		return polTotSD;
	}

	public void setPolTotSD(BigDecimal polTotSD) {
		this.polTotSD = polTotSD;
	}


	public BigDecimal getPolTotExtras() {
		return polTotExtras;
	}

	public void setPolTotExtras(BigDecimal polTotExtras) {
		this.polTotExtras = polTotExtras;
	}

	public BigDecimal getPolTotComm() {
		return polTotComm;
	}

	public void setPolTotComm(BigDecimal polTotComm) {
		this.polTotComm = polTotComm;
	}

	public BigDecimal getPolTotWhtx() {
		return polTotWhtx;
	}

	public void setPolTotWhtx(BigDecimal polTotWhtx) {
		this.polTotWhtx = polTotWhtx;
	}


	public Long getInstallmentNo() {
		return installmentNo;
	}

	public void setInstallmentNo(Long installmentNo) {
		this.installmentNo = installmentNo;
	}

	public boolean isCommAllowed() {
		return commAllowed;
	}

	public void setCommAllowed(boolean commAllowed) {
		this.commAllowed = commAllowed;
	}

	public Integer getUwYear() {
		return uwYear;
	}

	public void setUwYear(Integer uwYear) {
		this.uwYear = uwYear;
	}

	public Long getPolicyId() {
		return policyId;
	}

	public void setPolicyId(Long policyId) {
		this.policyId = policyId;
	}

	public String getPolNo() {
		return polNo;
	}

	public void setPolNo(String polNo) {
		this.polNo = polNo;
	}

	public String getOldpolNo() {
		return OldpolNo;
	}

	public void setOldpolNo(String oldpolNo) {
		OldpolNo = oldpolNo;
	}

	public String getClientPolNo() {
		return clientPolNo;
	}

	public void setClientPolNo(String clientPolNo) {
		this.clientPolNo = clientPolNo;
	}

	public String getPolRevNo() {
		return polRevNo;
	}

	public void setPolRevNo(String polRevNo) {
		this.polRevNo = polRevNo;
	}

	
	public Date getPolCreateddt() {
		return polCreateddt;
	}

	public void setPolCreateddt(Date polCreateddt) {
		this.polCreateddt = polCreateddt;
	}

	public Date getWefDate() {
		return wefDate;
	}

	public void setWefDate(Date wefDate) {
		this.wefDate = wefDate;
	}

	public Date getWetDate() {
		return wetDate;
	}

	public void setWetDate(Date wetDate) {
		this.wetDate = wetDate;
	}

	public Date getRenewalDate() {
		return renewalDate;
	}

	public void setRenewalDate(Date renewalDate) {
		this.renewalDate = renewalDate;
	}

	public ClientDef getClient() {
		return client;
	}

	public void setClient(ClientDef client) {
		this.client = client;
	}

	public AccountDef getAgent() {
		return agent;
	}

	public void setAgent(AccountDef agent) {
		this.agent = agent;
	}

	public OrgBranch getBranch() {
		return branch;
	}

	public void setBranch(OrgBranch branch) {
		this.branch = branch;
	}

	public PaymentModes getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(PaymentModes paymentMode) {
		this.paymentMode = paymentMode;
	}

	public Currencies getTransCurrency() {
		return transCurrency;
	}

	public void setTransCurrency(Currencies transCurrency) {
		this.transCurrency = transCurrency;
	}

	public ProductsDef getProduct() {
		return product;
	}

	public void setProduct(ProductsDef product) {
		this.product = product;
	}

	public BindersDef getBinder() {
		return binder;
	}

	public void setBinder(BindersDef binder) {
		this.binder = binder;
	}

	

	public String getAuthStatus() {
		return authStatus;
	}

	public void setAuthStatus(String authStatus) {
		this.authStatus = authStatus;
	}

	public String getRefNo() {
		return refNo;
	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}

	public PolicyTrans getPreviousTrans() {
		return previousTrans;
	}

	public void setPreviousTrans(PolicyTrans previousTrans) {
		this.previousTrans = previousTrans;
	}

	public String getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	public User getAuthBy() {
		return authBy;
	}

	public void setAuthBy(User authBy) {
		this.authBy = authBy;
	}

	public BigDecimal getSumInsured() {
		return sumInsured;
	}

	public void setSumInsured(BigDecimal sumInsured) {
		this.sumInsured = sumInsured;
	}

	public BigDecimal getPremium() {
		return premium;
	}

	public void setPremium(BigDecimal premium) {
		this.premium = premium;
	}

	public BigDecimal getFuturePrem() {
		return futurePrem;
	}

	public void setFuturePrem(BigDecimal futurePrem) {
		this.futurePrem = futurePrem;
	}

	public BigDecimal getPrevPrem() {
		return prevPrem;
	}

	public void setPrevPrem(BigDecimal prevPrem) {
		this.prevPrem = prevPrem;
	}

	public BigDecimal getPrevBasicPrem() {
		return prevBasicPrem;
	}

	public void setPrevBasicPrem(BigDecimal prevBasicPrem) {
		this.prevBasicPrem = prevBasicPrem;
	}

	public BigDecimal getPrevNetPrem() {
		return prevNetPrem;
	}

	public void setPrevNetPrem(BigDecimal prevNetPrem) {
		this.prevNetPrem = prevNetPrem;
	}

	public BigDecimal getPrevEndosbasicPremium() {
		return prevEndosbasicPremium;
	}

	public void setPrevEndosbasicPremium(BigDecimal prevEndosbasicPremium) {
		this.prevEndosbasicPremium = prevEndosbasicPremium;
	}

	public String getPolRevStatus() {
		return polRevStatus;
	}

	public void setPolRevStatus(String polRevStatus) {
		this.polRevStatus = polRevStatus;
	}

	public User getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(User createdUser) {
		this.createdUser = createdUser;
	}

	public String getInterfaceType() {
		return interfaceType;
	}

	public void setInterfaceType(String interfaceType) {
		this.interfaceType = interfaceType;
	}

	public RiskTransBean getRiskBean() {
		return riskBean;
	}

	public void setRiskBean(RiskTransBean riskBean) {
		this.riskBean = riskBean;
	}

	public List<RiskSectionBean> getSections() {
		return sections;
	}

	public void setSections(List<RiskSectionBean> sections) {
		this.sections = sections;
	}

	public List<RiskTrans> getRiskTrans() {
		return riskTrans;
	}

	public void setRiskTrans(List<RiskTrans> riskTrans) {
		this.riskTrans = riskTrans;
	}

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public Long getAgentId() {
		return agentId;
	}

	public void setAgentId(Long agentId) {
		this.agentId = agentId;
	}

	public Long getBranchId() {
		return branchId;
	}

	public void setBranchId(Long branchId) {
		this.branchId = branchId;
	}

	public Long getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(Long paymentId) {
		this.paymentId = paymentId;
	}

	public Long getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Long currencyId) {
		this.currencyId = currencyId;
	}

	public Long getProdId() {
		return prodId;
	}

	public void setProdId(Long prodId) {
		this.prodId = prodId;
	}

	public Long getBindCode() {
		return bindCode;
	}

	public void setBindCode(Long bindCode) {
		this.bindCode = bindCode;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public Long getPrevPolicy() {
		return prevPolicy;
	}

	public void setPrevPolicy(Long prevPolicy) {
		this.prevPolicy = prevPolicy;
	}


	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public BigDecimal getCommAmt() {
		return commAmt;
	}

	public void setCommAmt(BigDecimal commAmt) {
		this.commAmt = commAmt;
	}

	public boolean isRenewable() {
		return renewable;
	}

	public void setRenewable(boolean renewable) {
		this.renewable = renewable;
	}

	public BigDecimal getBasicPrem() {
		return basicPrem;
	}

	public void setBasicPrem(BigDecimal basicPrem) {
		this.basicPrem = basicPrem;
	}

	public BigDecimal getNetPrem() {
		return netPrem;
	}

	public void setNetPrem(BigDecimal netPrem) {
		this.netPrem = netPrem;
	}

	public BigDecimal getStampDuty() {
		return stampDuty;
	}

	public void setStampDuty(BigDecimal stampDuty) {
		this.stampDuty = stampDuty;
	}

	public BigDecimal getTrainingLevy() {
		return trainingLevy;
	}

	public void setTrainingLevy(BigDecimal trainingLevy) {
		this.trainingLevy = trainingLevy;
	}

	public BigDecimal getWhtx() {
		return whtx;
	}

	public void setWhtx(BigDecimal whtx) {
		this.whtx = whtx;
	}

	public BigDecimal getExtras() {
		return extras;
	}

	public void setExtras(BigDecimal extras) {
		this.extras = extras;
	}

	public BigDecimal getPhcf() {
		return phcf;
	}

	public void setPhcf(BigDecimal phcf) {
		this.phcf = phcf;
	}

	public List<PolicyTaxes> getPolicyTaxes() {
		return policyTaxes;
	}

	public void setPolicyTaxes(List<PolicyTaxes> policyTaxes) {
		this.policyTaxes = policyTaxes;
	}

	public Date getAuthDate() {
		return authDate;
	}

	public void setAuthDate(Date authDate) {
		this.authDate = authDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((policyId == null) ? 0 : policyId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PolicyTrans other = (PolicyTrans) obj;
		if (policyId == null) {
			if (other.policyId != null)
				return false;
		} else if (!policyId.equals(other.policyId))
			return false;
		return true;
	}

	public List<PolicyClauses> getClauses() {
		return clauses;
	}

	public void setClauses(List<PolicyClauses> clauses) {
		this.clauses = clauses;
	}

	public BigDecimal getEndosbasicPremium() {
		return endosbasicPremium;
	}

	public void setEndosbasicPremium(BigDecimal endosbasicPremium) {
		this.endosbasicPremium = endosbasicPremium;
	}

	public BigDecimal getEndosCommissions() {
		return endosCommissions;
	}

	public void setEndosCommissions(BigDecimal endosCommissions) {
		this.endosCommissions = endosCommissions;
	}

	public Date getCoverFrom() {
		return coverFrom;
	}

	public void setCoverFrom(Date coverFrom) {
		this.coverFrom = coverFrom;
	}

	public Date getCoverTo() {
		return coverTo;
	}

	public void setCoverTo(Date coverTo) {
		this.coverTo = coverTo;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public PolicyTrans getReusecontraPolicy() {
		return reusecontraPolicy;
	}

	public void setReusecontraPolicy(PolicyTrans reusecontraPolicy) {
		this.reusecontraPolicy = reusecontraPolicy;
	}

	public boolean isRenewed() {
		return renewed;
	}

	public void setRenewed(boolean renewed) {
		this.renewed = renewed;
	}

	public String getRenewalError() {
		return renewalError;
	}

	public void setRenewalError(String renewalError) {
		this.renewalError = renewalError;
	}


	public BigDecimal getTotTrainingLevy() {
		return totTrainingLevy;
	}

	public void setTotTrainingLevy(BigDecimal totTrainingLevy) {
		this.totTrainingLevy = totTrainingLevy;
	}

	public BigDecimal getTotPhcf() {
		return totPhcf;
	}

	public void setTotPhcf(BigDecimal totPhcf) {
		this.totPhcf = totPhcf;
	}

	public String getRevisionFormat() {
		return revisionFormat;
	}

	public void setRevisionFormat(String revisionFormat) {
		this.revisionFormat = revisionFormat;
	}

	public String getBeneficiary() {
		return beneficiary;
	}

	public void setBeneficiary(String beneficiary) {
		this.beneficiary = beneficiary;
	}

	public String getBindName() {
		return bindName;
	}

	public void setBindName(String bindName) {
		this.bindName = bindName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getInsuranceCompany() {
		return insuranceCompany;
	}

	public void setInsuranceCompany(String insuranceCompany) {
		this.insuranceCompany = insuranceCompany;
	}

	public String getMedicalCoverType() {
		return medicalCoverType;
	}

	public void setMedicalCoverType(String medicalCoverType) {
		this.medicalCoverType = medicalCoverType;
	}

//	public String getIssueCard() {
//		return issueCard;
//	}
//
//	public void setIssueCard(String issueCard) {
//		this.issueCard = issueCard;
//	}



//	public String getCardType() {
//		return cardType;
//	}
//
//	public void setCardType(String cardType) {
//		this.cardType = cardType;
//	}

	public BigDecimal getIssueCardFee() {
		return issueCardFee;
	}

	public void setIssueCardFee(BigDecimal issueCardFee) {
		this.issueCardFee = issueCardFee;
	}

	public BigDecimal getServiceCharge() {
		return serviceCharge;
	}

	public void setServiceCharge(BigDecimal serviceCharge) {
		this.serviceCharge = serviceCharge;
	}

	public BigDecimal getReissueCardFee() {
		return reissueCardFee;
	}

	public void setReissueCardFee(BigDecimal reissueCardFee) {
		this.reissueCardFee = reissueCardFee;
	}

	public BigDecimal getVatAmount() {
		return vatAmount;
	}

	public void setVatAmount(BigDecimal vatAmount) {
		this.vatAmount = vatAmount;
	}

	public AccountDef getSubAgent() {
		return subAgent;
	}

	public void setSubAgent(AccountDef subAgent) {
		this.subAgent = subAgent;
	}

	public Long getSubAgentId() {
		return subAgentId;
	}

	public void setSubAgentId(Long subAgentId) {
		this.subAgentId = subAgentId;
	}

	public BigDecimal getSubAgentComm() {
		return subAgentComm;
	}

	public void setSubAgentComm(BigDecimal subAgentComm) {
		this.subAgentComm = subAgentComm;
	}

	public boolean isImportRisks() {
		return importRisks;
	}

	public void setImportRisks(boolean importRisks) {
		this.importRisks = importRisks;
	}

	public String getProposalNo() {
		return proposalNo;
	}

	public void setProposalNo(String proposalNo) {
		this.proposalNo = proposalNo;
	}

	public BigDecimal getCommRate() {
		return commRate;
	}

	public void setCommRate(BigDecimal commRate) {
		this.commRate = commRate;
	}

	public BigDecimal getPrevFuturePrem() {
		return prevFuturePrem;
	}

	public void setPrevFuturePrem(BigDecimal prevFuturePrem) {
		this.prevFuturePrem = prevFuturePrem;
	}

	public BinderMedicalCards getBinCardType() {
		return binCardType;
	}

	public void setBinCardType(BinderMedicalCards binCardType) {
		this.binCardType = binCardType;
	}


	public Long getCardId() {
		return cardId;
	}

	public void setCardId(Long cardId) {
		this.cardId = cardId;
	}

	public String getCancellationType() {
		return cancellationType;
	}

	public void setCancellationType(String cancellationType) {
		this.cancellationType = cancellationType;
	}



	public BigDecimal getRefundablePremium() {
		return refundablePremium;
	}

	public void setRefundablePremium(BigDecimal refundablePremium) {
		this.refundablePremium = refundablePremium;
	}

	public BigDecimal getEndosgrossPremium() {
		return endosgrossPremium;
	}

	public void setEndosgrossPremium(BigDecimal endosgrossPremium) {
		this.endosgrossPremium = endosgrossPremium;
	}

	public BigDecimal getPaidPremium() {
		return paidPremium;
	}

	public void setPaidPremium(BigDecimal paidPremium) {
		this.paidPremium = paidPremium;
	}

	public BigDecimal getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(BigDecimal refundAmount) {
		this.refundAmount = refundAmount;
	}

	public Integer getPaidInsts() {
		return paidInsts;
	}

	public void setPaidInsts(Integer paidInsts) {
		this.paidInsts = paidInsts;
	}

	public Long[] getBindCodes() {
		return bindCodes;
	}

	public void setBindCodes(Long[] bindCodes) {
		this.bindCodes = bindCodes;
	}

	public String getQuizTaken() {
		return quizTaken;
	}

	public void setQuizTaken(String quizTaken) {
		this.quizTaken = quizTaken;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
