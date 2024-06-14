package com.brokersystems.brokerapp.trans.service.impl;

import com.brokersystems.brokerapp.accounts.dtos.SystemTransDTO;
import com.brokersystems.brokerapp.accounts.model.CollectionAccounts;
import com.brokersystems.brokerapp.accounts.model.QCollectionAccounts;
import com.brokersystems.brokerapp.accounts.repository.CollectionAcctsRepo;
import com.brokersystems.brokerapp.accounts.repository.RefundRepo;
import com.brokersystems.brokerapp.enums.RevenueItems;
import com.brokersystems.brokerapp.medical.model.QSelfFundParams;
import com.brokersystems.brokerapp.medical.model.SelfFundParams;
import com.brokersystems.brokerapp.medical.repository.SelfFundParamsRepo;
import com.brokersystems.brokerapp.security.CheckAuthLimits;
import com.brokersystems.brokerapp.server.datatables.DataTablesRequest;
import com.brokersystems.brokerapp.server.datatables.DataTablesResult;
import com.brokersystems.brokerapp.server.exception.BadRequestException;
import com.brokersystems.brokerapp.server.utils.DateUtilities;
import com.brokersystems.brokerapp.server.utils.NumberToWordsUtils;
import com.brokersystems.brokerapp.server.utils.UserUtils;
import com.brokersystems.brokerapp.setup.dto.CurrencyDTO;
import com.brokersystems.brokerapp.setup.dto.OrganizationDTO;
import com.brokersystems.brokerapp.setup.model.*;
import com.brokersystems.brokerapp.setup.repository.*;
import com.brokersystems.brokerapp.setup.service.OrganizationService;
import com.brokersystems.brokerapp.soapws.model.QWebServiceReceipt;
import com.brokersystems.brokerapp.soapws.model.WebServiceReceipt;
import com.brokersystems.brokerapp.soapws.model.WebServiceReceiptRepo;
import com.brokersystems.brokerapp.trans.dtos.LifeReceiptsDTO;
import com.brokersystems.brokerapp.trans.model.*;
import com.brokersystems.brokerapp.trans.repository.*;
import com.brokersystems.brokerapp.trans.service.AccountsUtilities;
import com.brokersystems.brokerapp.trans.service.AllocationService;
import com.brokersystems.brokerapp.trans.service.ReceiptService;
import com.brokersystems.brokerapp.uw.model.PolicyTrans;
import com.brokersystems.brokerapp.uw.model.QPolicyTrans;
import com.brokersystems.brokerapp.uw.repository.PolicyTransRepo;
import com.brokersystems.brokerapp.webservices.service.MobileMoneyService;
import com.brokersystems.brokerapp.workflow.dto.WorkFlowDTO;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ReceiptServiceImpl implements ReceiptService {
	
	@Autowired
	private ReceiptRepository receiptRepo;
	
	@Autowired
	private SystemTransactionsRepo transRepo;
	
	@Autowired
	private ReceiptDetailsRepository rctDetailsRepo;
	
	@Autowired
	private CurrencyRepository currencyRepo;

	@Autowired
	private OrgBranchRepository branchRepository;

	@Autowired
	private SequenceRepository sequenceRepo;

	@Autowired
	private UserUtils userUtils;

	@Autowired
	private CheckAuthLimits authLimits;
	
	@Autowired
	private AllocationService allocService;
	
	@Autowired
	private OrganizationService orgService;
	
	@Autowired
	private PaymentModeRepo paymentRepo;

	@Autowired
	private SelfFundParamsRepo fundParamsRepo;

	@Autowired
	private CollectionAcctsRepo collectionAcctsRepo;

	@Autowired
	private DateUtilities dateUtils;

	@Autowired
	private SystemTransRepo systemTransRepo;

	@Autowired
	private GlTransRepo glTransRepo;

	@Autowired
	private AccountsUtilities accountsUtilities;

	@Autowired
	private ReceiptPrintRepo printRepo;

	@Autowired
	private IntegrationDtlsRepo integrationDtlsRepo;

	@Autowired
	private MobileMoneyService mobileMoneyService;

	@Autowired
	private RefundRepo refundRepo;

	@Autowired
	private SettlementRepo settlementRepo;

	@Autowired
	private PolicyTransRepo policyTransRepo;


	@Autowired
	private DateUtilities dateUtilities;

	@Autowired
	private SystemTransactionsTempRepo systemTransactionsTempRepo;

	@Autowired
	private AccountRepo accountRepo;

	@Autowired
	private WebServiceReceiptRepo webServiceReceiptRepo;


    @Autowired
    private DataSource dataSource;

	@Override
	@Transactional(readOnly = true)
	public DataTablesResult<ReceiptTrans> findAllReceipts(DataTablesRequest request) throws IllegalAccessException {
		BooleanExpression pred = QReceiptTrans.receiptTrans.printed.isNull();
		Page<ReceiptTrans> page = receiptRepo.findAll(pred.and(request.searchPredicate(QReceiptTrans.receiptTrans)), request);
		return new DataTablesResult(request, page);
	}

	@Override
	public Page<SystemTransactions> findAgentCommisionTrans(String paramString, Pageable paramPageable, Long insuranceId) throws IllegalAccessException {
		Predicate pred =null;
		if (paramString == null || StringUtils.isBlank(paramString)) {
			pred = QSystemTransactions.systemTransactions.transdc.eq("D")
					.and(QSystemTransactions.systemTransactions.isNotNull())
					.and(QSystemTransactions.systemTransactions.transType.in("COMM"))
					.and(QSystemTransactions.systemTransactions.clientType.eq("A"))
					.and(QSystemTransactions.systemTransactions.agent.acctId.eq(insuranceId))
					.and(QSystemTransactions.systemTransactions.balance.gt(BigDecimal.ZERO));
		}
		else{
			pred = QSystemTransactions.systemTransactions.transdc.eq("D")
					.and(QSystemTransactions.systemTransactions.isNotNull())
					.and(QSystemTransactions.systemTransactions.transType.in("COMM"))
					.and(QSystemTransactions.systemTransactions.refNo.containsIgnoreCase(paramString))
					.and(QSystemTransactions.systemTransactions.clientType.eq("A"))
					.and(QSystemTransactions.systemTransactions.agent.acctId.eq(insuranceId))
					.and(QSystemTransactions.systemTransactions.balance.gt(BigDecimal.ZERO));
		}
		return transRepo.findAll(pred, paramPageable);
	}

//	@Override
//	@Transactional(readOnly = true)
//	public DataTablesResult<SystemTransDTO> findReceiptTransactions(String paramString, DataTablesRequest request) throws IllegalAccessException {
//		Predicate pred = null;
//		Predicate pred2 = null;
//		if (paramString == null || StringUtils.isBlank(paramString)) {
//			 pred = QSystemTransactions.systemTransactions.transdc.eq("D")
//					.and(QSystemTransactions.systemTransactions.isNotNull())
//					 .and(QSystemTransactions.systemTransactions.transType.notIn("SAG"))
//					 .and(QSystemTransactions.systemTransactions.clientType.eq("C"))
//					.and(QSystemTransactions.systemTransactions.balance.gt(BigDecimal.ZERO));
//
//			pred2 = QSystemTransactionsTemp.systemTransactionsTemp.transdc.eq("D")
//					.and(QSystemTransactionsTemp.systemTransactionsTemp.isNotNull())
//					.and(QSystemTransactionsTemp.systemTransactionsTemp.transType.notIn("SAG"))
//					.and(QSystemTransactionsTemp.systemTransactionsTemp.clientType.eq("C"))
//					.and(QSystemTransactionsTemp.systemTransactionsTemp.balance.gt(BigDecimal.ZERO))
//					.and(QSystemTransactionsTemp.systemTransactionsTemp.authorised.eq("Y"));
//		}
//		else{
//			pred = (QSystemTransactions.systemTransactions.transdc.eq("D")
//					.and(QSystemTransactions.systemTransactions.refNo.containsIgnoreCase(paramString))
//							.and(QSystemTransactions.systemTransactions.clientType.eq("C"))
//					.and(QSystemTransactions.systemTransactions.balance.gt(BigDecimal.ZERO))
//					.and(QSystemTransactions.systemTransactions.transType.notIn("SAG")))
//			        .or(QSystemTransactions.systemTransactions.transdc.eq("D")
//							.and(QSystemTransactions.systemTransactions.policy.polNo.containsIgnoreCase(paramString))
//							.and(QSystemTransactions.systemTransactions.clientType.eq("C"))
//							.and(QSystemTransactions.systemTransactions.balance.gt(BigDecimal.ZERO))
//							.and(QSystemTransactions.systemTransactions.transType.notIn("SAG")));
//
//			pred2 = (QSystemTransactionsTemp.systemTransactionsTemp.transdc.eq("D")
//					.and(QSystemTransactionsTemp.systemTransactionsTemp.refNo.containsIgnoreCase(paramString))
//					.and(QSystemTransactionsTemp.systemTransactionsTemp.clientType.eq("C"))
//					.and(QSystemTransactionsTemp.systemTransactionsTemp.balance.gt(BigDecimal.ZERO))
//					.and(QSystemTransactionsTemp.systemTransactionsTemp.transType.notIn("SAG")))
//					.and(QSystemTransactionsTemp.systemTransactionsTemp.authorised.eq("Y"))
//					.or(QSystemTransactionsTemp.systemTransactionsTemp.transdc.eq("D")
//							.and(QSystemTransactionsTemp.systemTransactionsTemp.policy.polNo.containsIgnoreCase(paramString))
//							.and(QSystemTransactionsTemp.systemTransactionsTemp.clientType.eq("C"))
//							.and(QSystemTransactionsTemp.systemTransactionsTemp.balance.gt(BigDecimal.ZERO))
//							.and(QSystemTransactionsTemp.systemTransactionsTemp.transType.notIn("SAG"))
//							.and(QSystemTransactionsTemp.systemTransactionsTemp.authorised.eq("Y")));
//		}
//		List<SystemTransactionsTemp> transactionsListTemp = systemTransactionsTempRepo.findAll(pred2, paramPageable).getContent();
//		List<SystemTransactions> transactionsList = transRepo.findAll(pred, paramPageable).getContent();
//		List<SystemTransactions> systemTransactionsList = new ArrayList<>();
//		transactionsListTemp.forEach(a -> {
//			SystemTransactions transactions = new SystemTransactions();
//			try {
//				BeanUtils.copyProperties(transactions,a);
//				systemTransactionsList.add(transactions);
//			} catch (IllegalAccessException e) {
//				e.printStackTrace();
//			} catch (InvocationTargetException e) {
//				e.printStackTrace();
//			}
//		});
//		systemTransactionsList.addAll(transactionsList);
//		Page<SystemTransactions> systemTransactionsPage = new PageImpl<>(systemTransactionsList);
//		return systemTransactionsPage;
//	}


    @Override
    @Transactional(readOnly = true)
    public Page<SystemTransDTO> findReceiptTransactions(String param, Pageable request) throws IllegalAccessException {
        String paramString= StringUtils.isBlank(param)? null:param;
		List<Object[]> receiptList = receiptRepo.findDebitTransactions(paramString,request.getPageNumber(), request.getPageSize());
		List<SystemTransDTO> debitTransList = new ArrayList<>();
		long rowCount = 0L;
		if(!receiptList.isEmpty()) rowCount = ((BigInteger)receiptList.get(0)[9]).intValue();
		for(Object[] trans:receiptList){
			SystemTransDTO transDTO =  SystemTransDTO.instance( (trans[0]!=null)?((BigInteger)trans[0]).longValue():null,
					(trans[1]!=null)?((BigInteger)trans[1]).longValue():null, (Date) trans[2],
																null, (String) trans[3],
					null, (String) trans[4], (String) trans[5], null, (String) trans[6], null, null, null,
					(BigDecimal) trans[7],(String) trans[8], null);
			debitTransList.add(transDTO);

		}

		return new PageImpl<>(debitTransList, request, rowCount);
    }


	@PreAuthorize("hasAnyAuthority('CREATE_RECEIPT')")
	@Override
	@Modifying
	@Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
	public Long createFundReceipt(ReceiptTrans receipt) throws BadRequestException {
		if (receipt.getBrnCode() == null) {
			throw new BadRequestException("Receipt Branch is mandatory");
		}
		if (receipt.getPayId() == null) {
			throw new BadRequestException("Payment Mode is mandatory");
		}

		if(!authLimits.checkAuthorizationLimits("CREATE_RECEIPT",receipt.getReceiptAmount())){
			throw new BadRequestException("You have no rights to create the transaction...Check your  limits..");
		}
		User user = userUtils.getCurrentUser();
		Predicate seqPredicate = QSystemSequence.systemSequence.transType.eq("R");
		if (sequenceRepo.count(seqPredicate) == 0)
			throw new BadRequestException("Sequence for Receipt Transactions has not been setup");
		SystemSequence sequence = sequenceRepo.findOne(seqPredicate);
		Long seqNumber = sequence.getNextNumber();
		final String receiptNo = sequence.getSeqPrefix() + String.format("%05d", seqNumber);
		receipt.setReceiptNo(receiptNo);
		receipt.setCounter(BigInteger.valueOf(seqNumber));
		SystemTrans transaction = new SystemTrans();
		transaction.setDoneDate(new Date());
		transaction.setDoneBy(userUtils.getCurrentUser());
		transaction.setTransLevel("U");
		transaction.setTransCode("RCT"); //A way to setup and look up for transaction transcode
		transaction.setTransAuthorised("N");
		SystemTrans systemTrans = systemTransRepo.save(transaction);
		List<ReceiptTransDtls> transDtls = new ArrayList<>();
		BigDecimal totalAllocAmount = BigDecimal.ZERO;
		for (ReceiptTransDtls tran : receipt.getDetails()) {
			SelfFundParams selfFundParams = fundParamsRepo.findOne(tran.getTransNo());
			tran.setFundParams(selfFundParams);
			tran.setReceipt(receipt);
			tran.setEndorsementNumber(selfFundParams.getPolicyTrans().getPolRevNo());
			tran.setRctType("INV");
			tran.setRctDC("C");
			transDtls.add(tran);
			totalAllocAmount = totalAllocAmount.add(tran.getRctAmount());
			ProductsDef product =selfFundParams.getPolicyTrans().getProduct();
			if(product==null) throw new BadRequestException("Error getting policy product");
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			List<Long> subcodes = jdbcTemplate.query("SELECT ps_sub_code  FROM sys_brk_prod_subcls sbps  WHERE ps_pr_code  = ?", new Object[]{product.getProCode()}, new RowMapper<Long>() {
				@Override
				public Long mapRow(ResultSet resultSet, int i) throws SQLException {
					return resultSet.getLong(1);
				}
			});
			if(subcodes.size()!=1){
				throw new BadRequestException("Error getting sub classes");
			}
			long subcode = subcodes.get(0);
			postReceiptAccount(receipt,systemTrans, subcode,tran.getRctAmount());
		}
		if (totalAllocAmount.compareTo(receipt.getReceiptAmount()) != 0) {
			throw new BadRequestException("Total Allocation Amount " + totalAllocAmount + " and Receipt amount "
					+ receipt.getReceiptAmount() + " doesnt tally....");
		}
		rctDetailsRepo.save(transDtls);
		receipt.setBranch(branchRepository.findOne(receipt.getBrnCode()));
		receipt.setCollectionAccount(collectionAcctsRepo.findOne(receipt.getPayId()));
		receipt.setReceiptUser(user);
		receipt.setReceiptTransDate(new Date());
		sequence.setLastNumber(seqNumber);
		sequence.setNextNumber(seqNumber + 1);
		receipt.setDirectReceipt("N");
		sequenceRepo.save(sequence);
		Float amount = receipt.getReceiptAmount().floatValue();
		int figure = (int) Math.floor(amount);
		int cent = (int) Math.floor((amount - figure) * 100.0f);
		String words = "";
		if (cent > 0) {
			words = NumberToWordsUtils.convert(figure) + " and " + NumberToWordsUtils.convert(cent) + " cents";
		} else {
			words = NumberToWordsUtils.convert(figure);
		}
		receipt.setAmountWords(words);

		ReceiptTrans trans = receiptRepo.save(receipt);
		return trans.getReceiptId();
	}

	@PreAuthorize("hasAnyAuthority('CREATE_RECEIPT')")
	@Override
	@Modifying
	@Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
	public Long createReceipt(ReceiptTrans receipt) throws BadRequestException {


		if (receipt.getPayId() == null) {
			throw new BadRequestException("Collection Account is mandatory");
		}

		if(receipt.getBrnCode()==null){
			throw new BadRequestException("Receipt Branch is mandatory...");
		}

		if(!StringUtils.isBlank(receipt.getPaymentRef())){
			if(receiptRepo.count(QReceiptTrans.receiptTrans.paymentRef.eq(receipt.getPaymentRef()).and(QReceiptTrans.receiptTrans.cancelled.ne("Y").or(QReceiptTrans.receiptTrans.cancelled.isNull()))) > 0){
				throw new BadRequestException("Receipt Reference Exists......");
			}
		}

		if(receipt.getReceiptType()!=null && receipt.getReceiptType().equalsIgnoreCase("C")){
			if(receipt.getInsuranceId()==null)
				throw new BadRequestException("Invalid Intermediary...");
			receipt.setInsurance(accountRepo.findOne(receipt.getInsuranceId()));
		}

		if(!authLimits.checkAuthorizationLimits("CREATE_RECEIPT",receipt.getReceiptAmount())){
			throw new BadRequestException("You have no rights to create the transaction...Check your  limits..");
		}
		User user  = userUtils.getCurrentUser();
		Predicate seqPredicate = QSystemSequence.systemSequence.transType.eq("R");
		if (sequenceRepo.count(seqPredicate) == 0)
			throw new BadRequestException("Sequence for Receipt Transactions has not been setup");
		SystemSequence sequence = sequenceRepo.findOne(seqPredicate);
		Long seqNumber = sequence.getNextNumber();
		final String receiptNo = sequence.getSeqPrefix() + String.format("%05d", seqNumber);
		receipt.setReceiptNo(receiptNo);
		receipt.setCounter(BigInteger.valueOf(seqNumber));
		receipt.setBranch(branchRepository.findOne(receipt.getBrnCode()));
		receipt.setCollectionAccount(collectionAcctsRepo.findOne(receipt.getPayId()));
		receipt.setReceiptUser(user);
		receipt.setReceiptTransDate(new Date());
		sequence.setLastNumber(seqNumber);
		sequence.setNextNumber(seqNumber + 1);
		if(receipt.getDirectReceipt()==null || "off".equalsIgnoreCase(receipt.getDirectReceipt()))
			receipt.setDirectReceipt("N");
		else if("on".equalsIgnoreCase(receipt.getDirectReceipt()))
			receipt.setDirectReceipt("Y");
		else
			receipt.setDirectReceipt("N");
		List<ReceiptTransDtls> transDtls = new ArrayList<>();
		SystemTrans trans = new SystemTrans();
		trans.setDoneDate(new Date());
		trans.setDoneBy(userUtils.getCurrentUser());
		trans.setTransLevel("U");
		trans.setTransCode("RCT"); //A way to setup and look up for transaction transcode
		trans.setTransAuthorised("N");
		SystemTrans systemTrans = systemTransRepo.save(trans);
		BigDecimal totalAllocAmount = BigDecimal.ZERO;
		if(receipt.getReceiptType()!=null && !receipt.getReceiptType().equalsIgnoreCase("C")) {
			for (ReceiptTransDtls tran : receipt.getDetails()) {
				if (tran.getTransNo() == null && tran.getTransTempNo()== null )
					throw new BadRequestException("Select Transaction to continue...");
				if (tran.getRctAmount().compareTo(BigDecimal.ZERO) <= 0)
					throw new BadRequestException("Receipt Amount cannot be zero or less than Zero...");
				if (receipt.getReceiptType() != null && "L".equalsIgnoreCase(receipt.getReceiptType())) {
					PolicyTrans transaction = policyTransRepo.findOne(tran.getTransNo());
//				System.out.println("Transaction found..."+transaction);
					if (transaction != null) {
						tran.setReceipt(receipt);
						tran.setPolicy(transaction);
						tran.setEndorsementNumber(transaction.getPolRevNo());
						tran.setRctType("INV");
						tran.setRctDC("C");
						transDtls.add(tran);
					}
				} else {

					SystemTransactions transaction = null;
					SystemTransactionsTemp transactionsTemp =null;
					if (tran.getTransNo() != null)
						transaction = transRepo.findOne(tran.getTransNo());
					else if (tran.getTransTempNo() != null)
						transactionsTemp = systemTransactionsTempRepo.findOne(tran.getTransTempNo());
					//	System.out.println("Transaction found..."+transaction);
					if (transaction != null || transactionsTemp != null) {
						tran.setReceipt(receipt);
						tran.setTransaction(transaction);
						tran.setTransactionsTemp(transactionsTemp);
						if (transaction != null)
							tran.setEndorsementNumber(transaction.getPolicy().getPolRevNo());
						else if (transactionsTemp != null)
							tran.setEndorsementNumber(transactionsTemp.getPolicy().getPolRevNo());
						tran.setRctType("INV");
						tran.setRctDC("C");
						if (transaction != null)
							tran.setPolicy(transaction.getPolicy());
						else if (transactionsTemp != null) {
							tran.setPolicy(transactionsTemp.getPolicy());
						}
						transDtls.add(tran);
					}

				}
				totalAllocAmount = totalAllocAmount.add(tran.getRctAmount());
				ProductsDef product =tran.getPolicy().getProduct();
				if(product==null) throw new BadRequestException("Error getting policy product");
				JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
				List<Long> subcodes = jdbcTemplate.query("SELECT ps_sub_code  FROM sys_brk_prod_subcls sbps  WHERE ps_pr_code  = ?", new Object[]{product.getProCode()}, new RowMapper<Long>() {
					@Override
					public Long mapRow(ResultSet resultSet, int i) throws SQLException {
						return resultSet.getLong(1);
					}
				});
				if(subcodes.size()!=1){
					throw new BadRequestException("Error getting sub classes");
				}
				long subcode = subcodes.get(0);
				postReceiptAccount(receipt,systemTrans, subcode,tran.getRctAmount());
			}
			if (totalAllocAmount.compareTo(receipt.getReceiptAmount()) != 0) {
				throw new BadRequestException("Total Allocation Amount " + totalAllocAmount + " and Receipt amount "
						+ receipt.getReceiptAmount() + " doesn't tally....");
			}
		rctDetailsRepo.save(transDtls);
		}

		sequenceRepo.save(sequence);
		Float amount = receipt.getReceiptAmount().floatValue();
		int figure = (int) Math.floor(amount);
		int cent = (int) Math.floor((amount - figure) * 100.0f);
		String words = "";
		if (cent > 0) {
			words = NumberToWordsUtils.convert(figure) + " and " + NumberToWordsUtils.convert(cent) + " cents";
		} else {
			words = NumberToWordsUtils.convert(figure);
		}
		receipt.setAmountWords(words);
		ReceiptTrans transs = receiptRepo.save(receipt);
		if (receipt.getReceiptType() != null && "L".equalsIgnoreCase(receipt.getReceiptType())) {
			allocService.allocateLifeReceipt(transs.getReceiptId(),user);
		}
		return transs.getReceiptId();
	}

	@Override
	@Modifying
	@Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
	public void markReceiptPrinted(Long receiptId, User user) throws BadRequestException {
		ReceiptTrans receipt = receiptRepo.findOne(receiptId);
		if(receipt.getReceiptType()!=null && "N".equalsIgnoreCase(receipt.getReceiptType())) {
			receipt.setPrinted("Y");
			receiptRepo.save(receipt);
			if (receipt.getReceiptType()!=null && "L".equalsIgnoreCase(receipt.getReceiptType())){
				allocService.allocateLifeReceipt(receiptId,user);
			}
			else {
			if (receipt.getFundReceipt() != null && "Y".equalsIgnoreCase(receipt.getFundReceipt())) {
				allocService.createFundReceiptTransaction(receiptId);
			} else
				allocService.allocateReceipt(receiptId, user);
		}
		}
		if (receipt.getReceiptType()!=null && "C".equalsIgnoreCase(receipt.getReceiptType())){
			allocService.allocateCommReceipt(receiptId,user);
		}
		
	}

	@Override
	public DataTablesResult<LifeReceiptsDTO> findPolicyReceipts(Long polId, DataTablesRequest request) {
		List<Object[]> receiptsList = receiptRepo.findPolicyReceipts(polId,request.getPageNumber(), request.getPageSize());
		long rowCount = 0L;
		if(!receiptsList.isEmpty()) rowCount = ((BigInteger)receiptsList.get(0)[7]).intValue();
		final List<LifeReceiptsDTO> receiptsDTOList = new ArrayList<>();
		for(Object[] ticket:receiptsList){
			LifeReceiptsDTO workFlowDTO = new LifeReceiptsDTO();
			workFlowDTO.setReceiptNo((String)ticket[0]);
			workFlowDTO.setReceiptDate((Date) ticket[1]);
			workFlowDTO.setDc((String)ticket[2]);
			workFlowDTO.setReceiptAmount((BigDecimal) ticket[3]);
			workFlowDTO.setAllocationAmount((BigDecimal) ticket[4]);
			workFlowDTO.setBalance((BigDecimal) ticket[5]);
			workFlowDTO.setReceiptd(((BigInteger) ticket[6]).longValue());
			receiptsDTOList.add(workFlowDTO);
		}
		Page<LifeReceiptsDTO>  page = new PageImpl<>(receiptsDTOList,request, rowCount);
		return new DataTablesResult<>(request, page);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<CollectionAccounts> findCollectionAccts(String paramString, Pageable paramPageable) {
		Predicate pred = null;
		if (paramString == null || StringUtils.isBlank(paramString)) {
			pred = QCollectionAccounts.collectionAccounts.isNotNull();
		} else {
			pred = QCollectionAccounts.collectionAccounts.name.containsIgnoreCase(paramString);
		}
		return collectionAcctsRepo.findAll(pred, paramPageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<SelfFundParams> findSelfFundTransactions(String paramString, Pageable paramPageable)  {
		Predicate pred = null;
		if (paramString == null || StringUtils.isBlank(paramString)) {
			 pred = QSelfFundParams.selfFundParams.policyTrans.authStatus.eq("A").and(
					QSelfFundParams.selfFundParams.selfFundBalance.isNull().or(QSelfFundParams.selfFundParams.fundDepositAmount.subtract(QSelfFundParams.selfFundParams.selfFundBalance).gt(BigDecimal.ZERO)));
		}else{
			pred = QSelfFundParams.selfFundParams.policyTrans.authStatus.eq("A").and(QSelfFundParams.selfFundParams.policyTrans.refNo.containsIgnoreCase(paramString)
			                                                                        .or(QSelfFundParams.selfFundParams.policyTrans.polNo.containsIgnoreCase(paramString))).and(
					QSelfFundParams.selfFundParams.selfFundBalance.isNull().or(QSelfFundParams.selfFundParams.fundDepositAmount.subtract(QSelfFundParams.selfFundParams.selfFundBalance).gt(BigDecimal.ZERO)));

		}
		return fundParamsRepo.findAll(pred, paramPageable);
	}

	@Override
	public Page<PolicyTrans> findLifeTransactions(String paramString, Pageable paramPageable) throws IllegalAccessException {
		Predicate pred = null;
		if (paramString == null || StringUtils.isBlank(paramString)) {
			pred = QPolicyTrans.policyTrans.isNotNull().and(QPolicyTrans.policyTrans.businessType.eq("L"));
		} else {
			pred = QPolicyTrans.policyTrans.businessType.eq("L").and(QPolicyTrans.policyTrans.polNo.containsIgnoreCase(paramString).or(QPolicyTrans.policyTrans.proposalNo.containsIgnoreCase(paramString)));
		}
		System.out.println(policyTransRepo.findAll(pred, paramPageable).getTotalElements());
		return policyTransRepo.findAll(pred, paramPageable);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { BadRequestException.class })
	public void postReceiptAccount(ReceiptTrans receiptTrans, SystemTrans systemTrans, long subCode, BigDecimal amount) throws BadRequestException {
		List<GlTransactions> glTransactions = new ArrayList<>();
		GlTransactions debit = new GlTransactions();
		debit.setAmount(amount.abs());
		debit.setAuthDate(new Date());
		debit.setbCuramount(amount.abs());
		debit.setBranch(receiptTrans.getBranch());
		debit.setCurrency(receiptTrans.getCollectionAccount().getCurrencies());
		debit.setGlAcc(receiptTrans.getCollectionAccount().getAccounts());
		debit.setGldc((receiptTrans.getReceiptAmount().signum()==1)?"D":"C");
		debit.setTransaction(systemTrans);
		debit.setTransLevel("U");
		debit.setTrntCode("RCT");
		debit.setGlYear(dateUtils.getUwYear(new Date()));
		debit.setGlMonth(dateUtils.getMonth(new Date()));
		debit.setNarration(String.format("Posting Receipt Transaction for  %s, Ref No: %s",receiptTrans.getPaidBy(),receiptTrans.getReceiptNo()));
		glTransactions.add(debit);
		GlTransactions credit = new GlTransactions();
		credit.setAmount(amount.abs());
		credit.setAuthDate(new Date());
		credit.setbCuramount(amount.abs());
		credit.setBranch(receiptTrans.getBranch());
		credit.setCurrency(receiptTrans.getCollectionAccount().getCurrencies());
		credit.setGlAcc(accountsUtilities.getGlCreditAccount(RevenueItems.UP, subCode));
		credit.setGldc((receiptTrans.getReceiptAmount().signum()==1)?"C":"D");
		credit.setTransaction(systemTrans);
		credit.setTransLevel("U");
		credit.setTrntCode("RCT");
		credit.setGlYear(dateUtils.getUwYear(new Date()));
		credit.setGlMonth(dateUtils.getMonth(new Date()));
		credit.setNarration(String.format("Posting Receipt Transaction for  %s, Ref No: %s",receiptTrans.getPaidBy(),receiptTrans.getReceiptNo()));
		glTransactions.add(credit);
		glTransRepo.save(glTransactions);
	}



	@Override
	public DataTablesResult<ReceiptTrans> findPrintedReceipts(DataTablesRequest request, Date from, Date to) throws IllegalAccessException {
		BooleanExpression pred = QReceiptTrans.receiptTrans.receiptDate.eq(new Date()).and(QReceiptTrans.receiptTrans.printed.eq("Y"))
				.and(QReceiptTrans.receiptTrans.cancelled.isNull().or(QReceiptTrans.receiptTrans.cancelled.ne("Y")));
		if(from!=null && to!=null){
			pred  = QReceiptTrans.receiptTrans.receiptDate.between(from,to).and(QReceiptTrans.receiptTrans.printed.eq("Y"))
					.and(QReceiptTrans.receiptTrans.cancelled.isNull().or(QReceiptTrans.receiptTrans.cancelled.ne("Y")));

		}
		Page<ReceiptTrans> page = receiptRepo.findAll(pred.and(request.searchPredicate(QReceiptTrans.receiptTrans)), request);
		return new DataTablesResult(request, page);
	}

	@Override
	public DataTablesResult<ReceiptTrans> findReceiptsToCancel(DataTablesRequest request, Date from, Date to,String refNo,String receiptNo,String policyNo,Long clientId) throws IllegalAccessException {
		QReceiptTrans receipt = QReceiptTrans.receiptTrans;
		to = dateUtilities.removeTime(DateUtils.addDays(to, 1));
		from= dateUtilities.removeTime(from);
			BooleanExpression pred = QReceiptTrans.receiptTrans.receiptDate.between(from,to).and(QReceiptTrans.receiptTrans.cancelled.isNull()
					.or(QReceiptTrans.receiptTrans.cancelled.eq("N")))
					.and((receiptNo==null || StringUtils.isEmpty(receiptNo))?receipt.isNotNull():receipt.receiptNo.eq(receiptNo))
					.and((policyNo==null|| StringUtils.isEmpty(policyNo))?receipt.isNotNull():receipt.receiptDtls.any().transaction.policy.polNo.eq(policyNo))
					.and((refNo==null || StringUtils.isEmpty(refNo))?receipt.isNotNull():receipt.receiptDtls.any().transaction.refNo.containsIgnoreCase(refNo));
					//.and((clientId==null)?receipt.isNotNull():receipt.receiptDtls.any().transaction.policy.client.tenId.eq(clientId));
		Page<ReceiptTrans> page = receiptRepo.findAll(pred.and(request.searchPredicate(QReceiptTrans.receiptTrans)), request);
		return new DataTablesResult(request, page);
	}

	@Override
	public DataTablesResult<ReceiptTrans> findUnPrintedReceipts(DataTablesRequest request, Date from, Date to) throws IllegalAccessException {
		BooleanExpression pred = QReceiptTrans.receiptTrans.receiptDate.eq(new Date()).and((QReceiptTrans.receiptTrans.printed.isNull().or(QReceiptTrans.receiptTrans.printed.eq("N"))))
				.and(QReceiptTrans.receiptTrans.cancelled.isNull().or(QReceiptTrans.receiptTrans.cancelled.ne("Y")));
		if(from!=null && to!=null){
			pred  = QReceiptTrans.receiptTrans.receiptDate.between(from,to).and((QReceiptTrans.receiptTrans.printed.isNull().or(QReceiptTrans.receiptTrans.printed.eq("N"))))
					.and(QReceiptTrans.receiptTrans.cancelled.isNull().or(QReceiptTrans.receiptTrans.cancelled.ne("Y")));

		}
		Page<ReceiptTrans> page = receiptRepo.findAll(pred.and(request.searchPredicate(QReceiptTrans.receiptTrans)), request);
		return new DataTablesResult(request, page);
	}

	@Transactional(readOnly = false)
	public void deleteCertTrans(){
		Iterable<ReceiptPrint> prints = printRepo.findAll(QReceiptPrint.receiptPrint.user.id.eq(userUtils.getCurrentUser().getId()));
		printRepo.delete(prints);
	}

	@Override

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void createReceipts(List<Long> receipts) {
		List<ReceiptPrint> certPrints = new ArrayList<>();
		for(Long receiptCode:receipts){
		    	ReceiptTrans receiptTrans = receiptRepo.findOne(receiptCode);
		    	ReceiptPrint print = new ReceiptPrint();
				print.setReceiptTrans(receiptTrans);
				print.setUser(userUtils.getCurrentUser());
				certPrints.add(print);

		}
		printRepo.save(certPrints);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
	public void markReceiptsPrinted(List<Long> receipts) throws BadRequestException {
		for(Long receiptCode:receipts) {
			ReceiptTrans receipt = receiptRepo.findOne(receiptCode);
			receipt.setPrinted("Y");
			receiptRepo.save(receipt);
			if (receipt.getReceiptType() != null && "L".equalsIgnoreCase(receipt.getReceiptType())) {
				allocService.allocateLifeReceipt(receiptCode, userUtils.getCurrentUser());
			} else {
				if (receipt.getFundReceipt() != null && "Y".equalsIgnoreCase(receipt.getFundReceipt())) {
					allocService.createFundReceiptTransaction(receiptCode);
				} else
					allocService.allocateReceipt(receiptCode, userUtils.getCurrentUser());
			}
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
	public void cancelReceipts(List<CancelData> receipts) throws BadRequestException {
		for(CancelData receiptCode:receipts) {
			if(receiptCode.getCommentl()==null || StringUtils.isEmpty(receiptCode.getCommentl())){
				throw new BadRequestException("Cannot cancel receipt without comment");
			}
		}
		for(CancelData receiptCode:receipts) {
			allocService.deallocateReceipt(receiptCode.getReceiptId(), receiptCode.getCommentl());
		}
	}

	@Override
	public DataTablesResult<IntegrationDtls> findIntegrationDtls(DataTablesRequest request, String receipted) throws IllegalAccessException {
		BooleanExpression pred = QIntegrationDtls.integrationDtls.receipted.eq(receipted);
		Page<IntegrationDtls> page = integrationDtlsRepo.findAll(pred.and(request.searchPredicate(QIntegrationDtls.integrationDtls)), request);
		return new DataTablesResult(request, page);
	}

	@Override
	public void updateIntegrationDtls(IntegrationDtls integrationDtls) {
		IntegrationDtls dtls = integrationDtlsRepo.findOne(integrationDtls.getBidId());
		dtls.setBillNewRfNumber(integrationDtls.getBillNewRfNumber());
		integrationDtlsRepo.save(dtls);
	}

@Override
	public BigDecimal getPolicyTotalRcptAmount( String policyno) throws BadRequestException{
	BigDecimal totalReceipt = BigDecimal.ZERO;
	BooleanExpression pred = QReceiptSettlementDetails.receiptSettlementDetails.debit.policy.polNo.eq(policyno);
	Iterable<ReceiptSettlementDetails> settlementDetails = settlementRepo.findAll(pred);
	for(ReceiptSettlementDetails settlementDetail:settlementDetails){
		long multiplier = 1;
		if (settlementDetail.getDrCr().equalsIgnoreCase("D"))
			multiplier=-1;

		totalReceipt=totalReceipt.add(settlementDetail.getAllocatedAmt().abs().multiply(BigDecimal.valueOf(multiplier)));
	}
	return totalReceipt;


}

    @Override
    public DataTablesResult<WebServiceReceipt> findBankIntegrationReceipts(DataTablesRequest request, String status) throws IllegalAccessException {
        BooleanExpression pred = null;
        if(status.equalsIgnoreCase("S")){
            pred=  QWebServiceReceipt.webServiceReceipt.status.equalsIgnoreCase("S");
        } else {
            pred= QWebServiceReceipt.webServiceReceipt.status.isNull();
        }
        Page<WebServiceReceipt> page = webServiceReceiptRepo.findAll(pred.and(request.searchPredicate(QWebServiceReceipt.webServiceReceipt)), request);
        return new DataTablesResult(request, page);
    }

    @Override
    public void updateBankIntegrationReceipts(WebServiceReceipt webServiceReceipt) {
        WebServiceReceipt dtls = webServiceReceiptRepo.findOne(webServiceReceipt.getSrId());
        dtls.setNewTransRefNumber(webServiceReceipt.getNewTransRefNumber());
        webServiceReceiptRepo.save(dtls);
    }


}
