package com.brokersystems.brokerapp.uw.service;

import java.io.IOException;
import java.math.BigDecimal;

import com.brokersystems.brokerapp.server.exception.BadRequestException;

public interface PremComputeService {
	
	public void computePrem(Long polCode) throws BadRequestException, IOException;
	
	public void computeCancelPrem(Long polCode) throws BadRequestException;
	
	public BigDecimal computeRiskPrem(Long riskCode);
	
	public BigDecimal calculateTax(BigDecimal premAmount,BigDecimal rate, BigDecimal divFactor,String rateType);
	
	public BigDecimal getCommissionRate(long BinderDet,BigDecimal premAmount);
	
	public void computeEndorsePremium(Long polCode) throws BadRequestException,IOException;

	public void computeLifePrem(Long polCode) throws BadRequestException, IOException;
	
	

}
