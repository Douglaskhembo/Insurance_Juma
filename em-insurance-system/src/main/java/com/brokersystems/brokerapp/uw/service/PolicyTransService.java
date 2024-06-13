package com.brokersystems.brokerapp.uw.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import com.brokersystems.brokerapp.claims.model.ClaimBookings;
import com.brokersystems.brokerapp.claims.model.ClaimPerils;
import com.brokersystems.brokerapp.life.model.PolicyInstallments;
import com.brokersystems.brokerapp.schedules.model.ScheduleTrans;
import com.brokersystems.brokerapp.server.exception.AdminFeeException;
import com.brokersystems.brokerapp.setup.dto.BranchDTO;
import com.brokersystems.brokerapp.setup.dto.CurrencyDTO;
import com.brokersystems.brokerapp.setup.dto.PaymentModesDTO;
import com.brokersystems.brokerapp.setup.model.*;
import com.brokersystems.brokerapp.trans.model.ReceiptTrans;
import com.brokersystems.brokerapp.trans.model.ReceiptTransDtls;
import com.brokersystems.brokerapp.trans.model.TransChecks;
import com.brokersystems.brokerapp.uw.dtos.*;
import com.brokersystems.brokerapp.uw.model.*;
import com.brokersystems.brokerapp.webservices.model.VehicleDetails;
import com.brokersystems.brokerapp.workflow.docs.SysWfDocs;
import com.brokersystems.brokerapp.workflow.dto.WorkFlowDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.brokersystems.brokerapp.server.datatables.DataTablesRequest;
import com.brokersystems.brokerapp.server.datatables.DataTablesResult;
import com.brokersystems.brokerapp.server.exception.BadRequestException;

public interface PolicyTransService {

	MileageDTO findMileageDetails(String riskId);
	
	 DataTablesResult<WorkFlowDTO> findUserPolicies(DataTablesRequest request);

	public DataTablesResult<SysWfDocs> findSearchTickets(DataTablesRequest request, String policyNo,String quoteNo, String preparedBy)
			throws IllegalAccessException;

	public DataTablesResult<PolicyTrans> findUserMedicalTrans(DataTablesRequest request)
			throws IllegalAccessException;
	
	public DataTablesResult<EndorsementsDTO> findEnquiryPolicies(DataTablesRequest request, String drNumber,
																 Long clientCode, String polNo, String riskShtDesc, Long agentCode, Long prodCode);

	DataTablesResult<PolicyTrans> findActiveEnquiryPolicies(DataTablesRequest request, String drNumber,
													  Long clientCode,String polNo,String endorseNumber,Long agentCode,Long prodCode) throws IllegalAccessException;


	public DataTablesResult<PolicyTrans> findEnquiryMedPolicies(DataTablesRequest request, String drNumber,
																Long clientCode, String polNo, String endorseNumber, Long agentCode,
																Long prodCode) throws IllegalAccessException;

	DataTablesResult<PolicyTrans> findEnquiryActiveorLapsedMedPolicies(DataTablesRequest request, String drNumber,
														 Long clientCode, String polNo, String endorseNumber, Long agentCode,
														 Long prodCode) throws IllegalAccessException;

	public DataTablesResult<PolicyTrans> findPendingPolicies(DataTablesRequest request, String drNumber,
															 Long clientCode,String polNo,String endorseNumber,Long agentCode,Long prodCode) throws IllegalAccessException;
	
	Page<ClientsDto> findActiveClients(String paramString, Pageable paramPageable);
	
	public Page<BinderDTO> findInsuranceBinder(String paramString, Pageable paramPageable,String bindType, Long productId);

	public Page<ProductsDef> findMultiProducts(String paramString, Pageable paramPageable);

	public Page<PolicyTrans> findClientPolicies(String paramString, Pageable paramPageable, Long clientId);

	public Page<BindersDef> findLifeBinder(String paramString, Pageable paramPageable, String bindType);

	public Page<BindersDef> findAllBinders(String paramString, Pageable paramPageable);

	 Page<CurrencyDTO> findCurrencies(String paramString, Pageable paramPageable);

	 Page<CurrencyDTO> findOtherCurrencies(String paramString, Pageable paramPageable);

	 Page<PaymentModesDTO> findPaymentModes(String paramString, Pageable paramPageable);

	public Page<AccountDef> findInhouseAgents(String paramString, Pageable paramPageable);
	
	Page<BranchDTO> findUserBranches(String paramString, Pageable paramPageable);
	Page<BranchDTO> findAllBranches(String paramString, Pageable paramPageable);

	public Page<PolicyTrans> findAllPolicies(String paramString, Pageable paramPageable);
	
	public Page<SubClassDef> findBinderSubclasses(String paramString, Pageable paramPageable,Long bindCode);
	
	public Page<CoverTypesDef> findBinderCoverTypes(String paramString, Pageable paramPageable,Long bindCode,Long subCode);

	public Page<CoverTypesDef> findBinderSubCoverTypes(String paramString, Pageable paramPageable,Long bindCode);
	
	public Set<RiskSectionBean> getBinderPremRates(Long detId);

	public Set<RiskSectionBean> getBinderClientPremRates(Long detId,Long insuredAge);

	public List<PremRatesDef> getNewSectPremiumItems(Long detId,Long riskId,String searchVal,Long insuredAge);

	public List<PremRatesDef> getNewPremiumItems(Long detId,Long riskId,String searchVal); 
	
	public PolicyTrans createPolicy(PolicyTrans policy) throws BadRequestException;

	public PolicyTrans createLifePolicy(PolicyTrans policy) throws BadRequestException;

	public DataTablesResult<PolicyBinders> findPolicyBinders(DataTablesRequest request, Long polCode)
			throws IllegalAccessException;
	
	public DataTablesResult<RiskTrans> findRiskTransactions(DataTablesRequest request, Long polCode, Long bindCode)
			throws IllegalAccessException;
	
	public DataTablesResult<SectionTrans> findRiskSections(DataTablesRequest request, Long riskId)
			throws IllegalAccessException;

    
	public PolicyTrans getPolicyDetails(Long polCode) throws BadRequestException;
	
	public Page<SectionBean> findPremSections(String paramString, Pageable paramPageable,Long detId);
	
	public void createRiskSection(SectionTrans section) throws BadRequestException;
	
	public void deleteRiskSection(Long sectid) throws BadRequestException;
	
	public DataTablesResult<PolicyTaxes> findPolicyTaxes(DataTablesRequest request, Long polCode)
			throws IllegalAccessException;

	 DataTablesResult<PolicyClauses> findPolicyClauses(DataTablesRequest request, Long polCode)
			throws IllegalAccessException;
	
	 void populateTaxes(PolicyTrans policy) throws BadRequestException;
	
	public Set<PolicyClauses> getNewClauses(Long polId) throws BadRequestException;

	public Set<PolicyTaxes> getNewTaxes(Long polId) throws BadRequestException;
	
	public void populateClauses(PolicyTrans policy) throws BadRequestException;
	
	void createRisk(RiskTrans risk) throws BadRequestException;

	public void createLifeRisk(RiskTrans risk) throws BadRequestException;

	public void deleteRisk(Long riskId) throws BadRequestException;
	
	public String makeReady(Long polCode) throws BadRequestException;
	public String makeLifeReady(Long polCode) throws BadRequestException;

	public void makeRenewalReady(Long polCode) throws BadRequestException;

	public void makeMedicalReady(Long polCode) throws BadRequestException;
	
	public void undoMakeReady(Long polCode) throws BadRequestException;

	public void convertPropToPolicy(Long polCode) throws BadRequestException ;
	
	public void createRiskSections(RiskBean sections) throws BadRequestException;
	
	public void deletePolicyClause(Long clauseId) throws BadRequestException;
	
	public void deletePolicyTax(Long taxId);
	
	public void createClause(PolicyClausesBean clause) throws BadRequestException;

	public void createTaxes(PolicyTaxBean taxBean) throws BadRequestException;
	
	public void createPolicyClause(PolicyClauses clause) throws BadRequestException;
	
	public void createPolicyTaxes(PolicyTaxes policyTax) throws BadRequestException;
	
	public DataTablesResult<EndorsementRemarks> findEndorsementRemarks(DataTablesRequest request, Long polCode)
			throws IllegalAccessException;

	DataTablesResult<PolicyInstallments> findPolicyInstallments(DataTablesRequest request, Long polCode)
			throws IllegalAccessException;
	
	public PolicyRemarks getPolicyRemarks(Long polCode);
	
	public void saveEndorsementRemarks(PolicyRemarks remarks) throws BadRequestException;

	public DataTablesResult<ScheduleTrans> findRiskSchedules(DataTablesRequest request, Long riskId)
			throws IllegalAccessException;

	void createRiskSchedules(VehicleDetails scheduleTrans) throws BadRequestException;

	public void deleteRiskSchedule(Long scheduleId);

	public void populateRiskScheduleDetails(Long riskId) throws BadRequestException;

	public BigDecimal getCommissionRate(Long bindDetCode) throws BadRequestException;

	Long createAdminFeeTrans(AdminFeeForm adminFeeForm) throws AdminFeeException;

	public DataTablesResult<AdminFee> findUnauthTrans(DataTablesRequest request)
			throws IllegalAccessException;

	public DataTablesResult<AdminFee> findAuthorisedTrans(DataTablesRequest request)
			throws IllegalAccessException;

	public AdminFee getAdminFeeDetails(Long adminFeeId);

	public DataTablesResult<AdminFeePolicies> findAdminFeePolicies(DataTablesRequest request, Long adminFeeId)
			throws IllegalAccessException;

	public void addAdminFeePolicies(AdminFeePolicyBean feePolicyBean) throws BadRequestException;

	public List<Object[]> getAdminFeePolicies(Long clientId,Long adminFeeId) throws IllegalAccessException;

	public void authorizeAdminFee(Long adminFeeId) throws BadRequestException;

	public void dispatchDocuments(Long polCode);

	public DataTablesResult<RiskDocs> findRiskDocs(DataTablesRequest request, Long riskId)
			throws IllegalAccessException;

	public void validateRiskIdFormat(Long subCode,String riskId) throws BadRequestException;

	public DataTablesResult<RiskInterestedParties> findRiskInterestedParties(DataTablesRequest request, Long riskId)
			throws IllegalAccessException;

	 List<InterestedParties> getNewInterestedParties(Long riskId) throws BadRequestException;

	public void createIntParties(RiskIntPartiesBean partiesBean) throws BadRequestException;

	public void deleteRiskIntParty(Long partId);

	void importExcelRiskTemplate(RiskUploadForm uploadForm) throws BadRequestException;

	public DataTablesResult<TransChecks> findPolicyChecks(DataTablesRequest request, Long polCode)
			throws IllegalAccessException;

	public void approveException(Long checkId,Long PolicyId) throws BadRequestException;

	List<SubClassReqdDocs> findUnassignedRiskDocs(Long riskId, String docName)  throws IllegalAccessException;

	void createRiskRequiredDocs(RequiredDocBean requiredDocBean);

	public DataTablesResult<RiskImportationLog> findPolicyImportationLog(DataTablesRequest request, Long policyId)
			throws IllegalAccessException;

	int countPolicies(Long clientCode) throws BadRequestException;

	public void questionnaireCompleted(PolicyTrans policy) throws BadRequestException;

	void lapsePolicy(Long polCode) throws BadRequestException;

	void unLapsePolicy(Long polCode) throws BadRequestException;

	public void savePolicyQuiz(QuestionnaireDTO questionnaireDTO) throws BadRequestException;
	public void deletePolicyQuiz(Long polCode);

    DataTablesResult<RiskTrans> findEnquiryMaster(DataTablesRequest pageable, Long polNo, Long riskId, Long idNo) throws IllegalAccessException;

    DataTablesResult<PolicyTrans> masterEnqPI(DataTablesRequest pageable, Long polNo, Long idNo) throws  IllegalAccessException;

	DataTablesResult<RiskTrans> findEnquiryPR(DataTablesRequest request, Long polNo, Long riskId) throws IllegalAccessException;

	DataTablesResult<RiskTrans> findEnquiryRI(DataTablesRequest pageable, Long idNo, Long riskId);

    DataTablesResult<PolicyTrans> masterEnqPol(DataTablesRequest pageable, Long polNo);

	DataTablesResult<PolicyTrans> masterEnqIdNo(DataTablesRequest pageable, Long idNo);

	DataTablesResult<RiskTrans> masterEnqRisk(DataTablesRequest pageable, Long policyId);

	DataTablesResult<RiskTrans> masterEnqUniqueId(DataTablesRequest pageable, Long riskId);

    DataTablesResult<ClaimPerils> masterEnqUniqueClaim(DataTablesRequest pageable, Long riskId);

    DataTablesResult<RiskTrans> masterEnqUniqueRisk(DataTablesRequest pageable, Long riskId);

	PolicyTrans findEnquiryId(Long idNo);

	PolicyTrans findEnquiryPol(Long polNo);

	RiskTrans findEnquiryRisk(Long riskId);

    RiskTrans findEnquiryRiskPol(String riskId, String polNo);

    RiskTrans findEnquiryRiskId(String riskId, Long idNo);

	PolicyTrans findEnquiryPolAndId(String polNo, Long idNo);

	RiskTrans checkAllParam(String polNo, Long idNo, String riskId);

    ClientDef findClient(Long clId);

	DataTablesResult<ClientDef> masterIdNo(DataTablesRequest pageable, Long idNo);

	Page<ClientDef> findAllClients(String term, Pageable pageable);

    Page<PolicyTrans> findAllPols(String term, Pageable pageable);

	Page<RiskTrans> allRisksLov(String term, Pageable pageable);

    DataTablesResult<ReceiptTrans> masterReceipts(DataTablesRequest pageable, Long idNo);

    ReceiptTrans getReceiptDetails(Long id);

    DataTablesResult<ReceiptTransDtls> getReceiptsDets(DataTablesRequest pageable, Long receiptId);

    ClaimBookings checkClaim(Long claim);

	 byte[] getPolicyDocument(Long prodCode) throws BadRequestException;

	String getPolicyDocumentType(Long docId) throws BadRequestException;

	DataTablesResult<VehicleDetails> findVehicleDetails(DataTablesRequest request, Long ipuCode) throws IllegalAccessException;

}
