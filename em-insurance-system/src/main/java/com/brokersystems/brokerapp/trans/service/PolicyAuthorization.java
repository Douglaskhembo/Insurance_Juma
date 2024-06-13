package com.brokersystems.brokerapp.trans.service;

import com.brokersystems.brokerapp.server.exception.BadRequestException;
import com.brokersystems.brokerapp.setup.model.User;

import java.math.BigDecimal;

public interface PolicyAuthorization {
	
	 void authorizePolicy(Long polCode, BigDecimal refundAmount) throws BadRequestException;

	 void authorizeMedicalPolicy(Long polCode ) throws BadRequestException;
	
	 void authorizePolicy(Long polCode,User user) throws BadRequestException;

	 void authorizeLifePolicy(Long polCode) throws BadRequestException;

	public void generateCert(Long polCode) throws BadRequestException;





}
