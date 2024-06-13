package com.brokersystems.brokerapp.trans.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;

import com.brokersystems.brokerapp.accounts.model.CoaSubAccounts;
import com.brokersystems.brokerapp.setup.model.Currencies;
import com.brokersystems.brokerapp.setup.model.OrgBranch;

@Entity
@Table(name="sys_brk_gl_trans")
public class GlTransactions {
	
	@Id
	@SequenceGenerator(name = "glTransSeq",sequenceName = "gl_trans_seq",allocationSize=1)
	@GeneratedValue(generator = "glTransSeq")
	@Column(name="gl_no")
	private Long glNo;
	
	@Column(name="gl_auth_date")
	private Date authDate;

	@Column(name="gl_year")
	private Integer glYear;

	@Column(name="gl_month")
	private String glMonth;
	
	@Column(name="gl_dc")
	private String gldc;
	
	@Column(name="gl_trnt_code")
	private String trntCode;
	
	@Column(name="gl_amount")
	private BigDecimal amount;

	@ManyToOne
	@JoinColumn(name="gl_account")
	private CoaSubAccounts glAcc;

	@Column(name="gl_bcur_amount")
	private BigDecimal bCuramount;
	
	@Column(name="gl_trans_level")
	private String transLevel;
	
	@ManyToOne
	@JoinColumn(name="gl_curr_id",nullable=false)
	private Currencies currency;
	
	@Column(name="gl_cur_rate")
	private BigDecimal currate;
	
	@ManyToOne
	@JoinColumn(name="gl_brn_id",nullable=false)
	private OrgBranch branch;
	
	@ManyToOne
	@JoinColumn(name="gl_trans_no",nullable=false)
	private SystemTrans transaction;

	@Column(name="gl_narration",length = 1000)
	private String narration;

	public Long getGlNo() {
		return glNo;
	}

	public void setGlNo(Long glNo) {
		this.glNo = glNo;
	}

	public Date getAuthDate() {
		return authDate;
	}

	public void setAuthDate(Date authDate) {
		this.authDate = authDate;
	}

	public String getGldc() {
		return gldc;
	}

	public void setGldc(String gldc) {
		this.gldc = gldc;
	}

	public String getTrntCode() {
		return trntCode;
	}

	public void setTrntCode(String trntCode) {
		this.trntCode = trntCode;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public CoaSubAccounts getGlAcc() {
		return glAcc;
	}

	public void setGlAcc(CoaSubAccounts glAcc) {
		this.glAcc = glAcc;
	}

	public BigDecimal getbCuramount() {
		return bCuramount;
	}

	public void setbCuramount(BigDecimal bCuramount) {
		this.bCuramount = bCuramount;
	}

	public String getTransLevel() {
		return transLevel;
	}

	public void setTransLevel(String transLevel) {
		this.transLevel = transLevel;
	}

	public Currencies getCurrency() {
		return currency;
	}

	public void setCurrency(Currencies currency) {
		this.currency = currency;
	}

	public BigDecimal getCurrate() {
		return currate;
	}

	public void setCurrate(BigDecimal currate) {
		this.currate = currate;
	}

	public OrgBranch getBranch() {
		return branch;
	}

	public void setBranch(OrgBranch branch) {
		this.branch = branch;
	}

	public SystemTrans getTransaction() {
		return transaction;
	}

	public void setTransaction(SystemTrans transaction) {
		this.transaction = transaction;
	}

	public Integer getGlYear() {
		return glYear;
	}

	public void setGlYear(Integer glYear) {
		this.glYear = glYear;
	}

	public String getGlMonth() {
		return glMonth;
	}

	public void setGlMonth(String glMonth) {
		this.glMonth = glMonth;
	}

	public String getNarration() {
		return narration;
	}

	public void setNarration(String narration) {
		this.narration = narration;
	}
}
