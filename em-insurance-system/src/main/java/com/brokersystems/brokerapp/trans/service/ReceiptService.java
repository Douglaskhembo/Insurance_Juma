package com.brokersystems.brokerapp.trans.service;

import com.brokersystems.brokerapp.accounts.dtos.SystemTransDTO;
import com.brokersystems.brokerapp.accounts.model.CollectionAccounts;
import com.brokersystems.brokerapp.accounts.model.Refunds;
import com.brokersystems.brokerapp.medical.model.SelfFundParams;
import com.brokersystems.brokerapp.setup.model.User;
import com.brokersystems.brokerapp.soapws.model.WebServiceReceipt;
import com.brokersystems.brokerapp.trans.dtos.LifeReceiptsDTO;
import com.brokersystems.brokerapp.trans.model.*;
import com.brokersystems.brokerapp.uw.model.PolicyTrans;
import com.brokersystems.brokerapp.workflow.dto.WorkFlowDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.brokersystems.brokerapp.server.datatables.DataTablesRequest;
import com.brokersystems.brokerapp.server.datatables.DataTablesResult;
import com.brokersystems.brokerapp.server.exception.BadRequestException;
import com.brokersystems.brokerapp.setup.model.Currencies;
import com.brokersystems.brokerapp.setup.model.PaymentModes;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


public interface ReceiptService {
	
	DataTablesResult<ReceiptTrans> findAllReceipts(DataTablesRequest request) throws IllegalAccessException;

	Page<SystemTransDTO>  findReceiptTransactions(String paramString, Pageable request) throws IllegalAccessException;

	Page<SystemTransactions> findAgentCommisionTrans(String paramString, Pageable paramPageable, Long insuranceId) throws IllegalAccessException;

	Long createReceipt(ReceiptTrans receipt) throws BadRequestException;
	
	void markReceiptPrinted(Long receiptId, User user) throws BadRequestException;
	DataTablesResult<LifeReceiptsDTO> findPolicyReceipts(Long polId, DataTablesRequest request);

	public Page<CollectionAccounts> findCollectionAccts(String paramString, Pageable paramPageable);

	Page<SelfFundParams> findSelfFundTransactions(String paramString, Pageable paramPageable) throws IllegalAccessException;

	Page<PolicyTrans> findLifeTransactions(String paramString, Pageable paramPageable) throws IllegalAccessException;

	 void postReceiptAccount(ReceiptTrans receiptTrans, SystemTrans systemTrans, long subCode, BigDecimal amount) throws BadRequestException;

	Long createFundReceipt(ReceiptTrans receipt) throws BadRequestException;

	DataTablesResult<ReceiptTrans> findPrintedReceipts(DataTablesRequest request, Date from,Date to) throws IllegalAccessException;

	DataTablesResult<ReceiptTrans> findUnPrintedReceipts(DataTablesRequest request, Date from,Date to) throws IllegalAccessException;

	void createReceipts(List<Long> receipts);

	void deleteCertTrans();

	void markReceiptsPrinted(List<Long> receipts) throws BadRequestException;

	DataTablesResult<ReceiptTrans> findReceiptsToCancel(DataTablesRequest request, Date from,Date to,String refNo,String receiptNo,String policyNo,Long clientId) throws IllegalAccessException;

	void cancelReceipts(List<CancelData> receipts) throws BadRequestException;

	DataTablesResult<IntegrationDtls> findIntegrationDtls(DataTablesRequest request, String receipted) throws IllegalAccessException;

	void updateIntegrationDtls(IntegrationDtls integrationDtls);

	public BigDecimal getPolicyTotalRcptAmount(String policyno) throws BadRequestException;

	DataTablesResult<WebServiceReceipt> findBankIntegrationReceipts(DataTablesRequest request, String status) throws IllegalAccessException;

	void updateBankIntegrationReceipts(WebServiceReceipt webServiceReceipt);



}
