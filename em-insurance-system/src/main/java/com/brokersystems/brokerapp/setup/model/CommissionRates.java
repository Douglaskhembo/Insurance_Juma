package com.brokersystems.brokerapp.setup.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="sys_brk_comm_rates")
public class CommissionRates extends AuditBaseEntity{
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="comm_id")
	private Long commId;
	
	@Column(name="comm_rate",nullable=false)
	private BigDecimal commRate;
	
	@Column(name="comm_active")
	private boolean active;
	
	@Column(name="comm_rate_desc")
	private String rateDesc;
	
	@Column(name="comm_div_fact",nullable=false)
	private BigDecimal commDivFactor;
	
	@Column(name="comm_rate_type")
	private String rateType;
	
	@Column(name="comm_range_from",nullable=false)
	private BigDecimal commRangeFrom;
	
	@Column(name="comm_range_to",nullable=false)
	private BigDecimal commRangeTo;
	
	@ManyToOne
	@JoinColumn(name="comm_bd_code",nullable=false)
	private BinderDetails binderDet;
	
	@ManyToOne
	@JoinColumn(name="comm_rev_code")
	private RevenueItemsDef revenueItems;

	public Long getCommId() {
		return commId;
	}

	public void setCommId(Long commId) {
		this.commId = commId;
	}

	public BigDecimal getCommRate() {
		return commRate;
	}

	public void setCommRate(BigDecimal commRate) {
		this.commRate = commRate;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getRateDesc() {
		return rateDesc;
	}

	public void setRateDesc(String rateDesc) {
		this.rateDesc = rateDesc;
	}

	public BigDecimal getCommDivFactor() {
		return commDivFactor;
	}

	public void setCommDivFactor(BigDecimal commDivFactor) {
		this.commDivFactor = commDivFactor;
	}

	public String getRateType() {
		return rateType;
	}

	public void setRateType(String rateType) {
		this.rateType = rateType;
	}

	public BigDecimal getCommRangeFrom() {
		return commRangeFrom;
	}

	public void setCommRangeFrom(BigDecimal commRangeFrom) {
		this.commRangeFrom = commRangeFrom;
	}

	public BigDecimal getCommRangeTo() {
		return commRangeTo;
	}

	public void setCommRangeTo(BigDecimal commRangeTo) {
		this.commRangeTo = commRangeTo;
	}

	public BinderDetails getBinderDet() {
		return binderDet;
	}

	public void setBinderDet(BinderDetails binderDet) {
		this.binderDet = binderDet;
	}

	public RevenueItemsDef getRevenueItems() {
		return revenueItems;
	}

	public void setRevenueItems(RevenueItemsDef revenueItems) {
		this.revenueItems = revenueItems;
	}
	
	

}
