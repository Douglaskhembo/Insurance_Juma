
package com.brokersystems.brokerapp.trans.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.brokersystems.brokerapp.accounts.model.PaymentAudit;
import com.brokersystems.brokerapp.accounts.model.QPaymentAudit;
import com.brokersystems.brokerapp.accounts.repository.PaymentAuditRepo;
import com.brokersystems.brokerapp.enums.RevenueItems;
import com.brokersystems.brokerapp.life.model.LifeReceipts;
import com.brokersystems.brokerapp.life.model.QLifeReceiptAllocations;
import com.brokersystems.brokerapp.life.model.QLifeReceipts;
import com.brokersystems.brokerapp.life.repository.LifeReceiptAllocationsRepo;
import com.brokersystems.brokerapp.life.repository.LifeReceiptsRepo;
import com.brokersystems.brokerapp.life.service.LifeService;
import com.brokersystems.brokerapp.medical.model.SelfFundParams;
import com.brokersystems.brokerapp.server.utils.DateUtilities;
import com.brokersystems.brokerapp.setup.model.User;
import com.brokersystems.brokerapp.trans.model.*;
import com.brokersystems.brokerapp.trans.repository.*;
import com.brokersystems.brokerapp.trans.service.AccountsUtilities;
import com.brokersystems.brokerapp.trans.service.PolicyAuthorization;
import com.brokersystems.brokerapp.uw.model.PolicyTrans;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.brokersystems.brokerapp.server.exception.BadRequestException;
import com.brokersystems.brokerapp.server.utils.UserUtils;
import com.brokersystems.brokerapp.setup.model.AccountDef;
import com.brokersystems.brokerapp.setup.model.ClientDef;
import com.brokersystems.brokerapp.setup.model.Currencies;
import com.brokersystems.brokerapp.trans.service.AllocationService;

@Service
public class AllocationServiceImpl implements AllocationService {

	@Autowired
	private SystemTransactionsRepo transRepo;
	
	@Autowired
	private SystemTransRepo systransRepo;

	@Autowired
	private UserUtils userUtils;
	
	@Autowired
	private ReceiptRepository receiptRepo;
	
	@Autowired
	private SettlementRepo settlementRepo;

	@Autowired
	private LifeReceiptAllocationsRepo lifeallocationsRepo;

	@Autowired
	private ReceiptDetailsRepository receiptDetailsRepository;

	@Autowired
	private DateUtilities dateUtils;

	@Autowired
	private GlTransRepo glTransRepo;

	@Autowired
	private AccountsUtilities accountsUtilities;

	@Autowired
	private PaymentAuditRepo auditRepo;

	@Autowired
	private LifeReceiptsRepo lifeReceiptsRepo;

	@Autowired
	private LifeService lifeService;

	@Autowired
	private SystemTransactionsTempRepo systemTransactionsTempRepo;

	@Autowired
	private PolicyAuthorization policyAuthorization;


	@Override
	@Modifying
	@Transactional(readOnly = false, rollbackFor = { BadRequestException.class }, propagation = Propagation.REQUIRED)
	public void allocateCredit(Long transId,Long debitTransId) throws BadRequestException {
		if(transId==null) throw new BadRequestException("Credit Transaction to Allocate Cannot be null");
		SystemTransactions credit =  transRepo.findOne(transId);
		if(!"C".equalsIgnoreCase(credit.getTransdc())){
			throw  new BadRequestException("Transaction to Allocate can only be only be Credit Transaction");
		}
		if(debitTransId==null)
			throw new BadRequestException("Debit Transaction to Allocate Cannot be null");
		SystemTransactions debit = transRepo.findOne(debitTransId);
		BigDecimal receiptBalance = credit.getBalance();
		BigDecimal debitBalance = debit.getBalance();
		if(debitBalance.compareTo(BigDecimal.ZERO)==1) {
			if (!debit.getTransdc().equalsIgnoreCase("D"))
				throw new BadRequestException("Can only allocate against a debit transaction. Contact Admin on Trans " + debit.getRefNo());
			if(receiptBalance.abs().compareTo(BigDecimal.ZERO)!=1) throw new BadRequestException("Credit Allocation Amount cannot be zero or less than zero "+receiptBalance);
			if (receiptBalance.abs().compareTo(debitBalance) == 1 || receiptBalance.abs().compareTo(debitBalance) == 0) {
				debit.setBalance(BigDecimal.ZERO);
				debit.setSettleAmt(debitBalance.abs());
				credit.setBalance(credit.getBalance().add(debitBalance.abs()));
			}
			else {
				if(debit.getSettleAmt()==null){
					debit.setSettleAmt(receiptBalance.abs());
				}else
					debit.setSettleAmt(debit.getSettleAmt().add(receiptBalance.abs()));
				debit.setBalance(debit.getBalance().subtract(receiptBalance.abs()));
				credit.setBalance(BigDecimal.ZERO);
			}
			transRepo.save(debit);
			transRepo.save(credit);
		}
		else{
			throw new BadRequestException("Debit Transaction has Zero Balance...Cannot allocate");
		}
	}

	@Override
	@Modifying
	@Transactional(readOnly = false, rollbackFor = { BadRequestException.class }, propagation = Propagation.REQUIRED)
	public void autoAllocateContra(Long transId, Long debitTransId) throws BadRequestException {
		if(transId==null) throw new BadRequestException("Credit Transaction to Allocate Cannot be null");
		SystemTransactions credit =  transRepo.findOne(transId);
		if(!"C".equalsIgnoreCase(credit.getTransdc())){
			throw  new BadRequestException("Transaction to Allocate can only be only be Credit Transaction");
		}
		if(debitTransId==null)
			throw new BadRequestException("Debit Transaction to Allocate Cannot be null");
		SystemTransactions debit = transRepo.findOne(debitTransId);
		BigDecimal receiptBalance = credit.getBalance();
		BigDecimal debitBalance = debit.getBalance();
		List<SystemTransactions> trans = new ArrayList<>();
		List<ReceiptSettlementDetails> settlementDetails = new ArrayList<>();
		Iterable<ReceiptSettlementDetails> settlements = settlementRepo.findAll(QReceiptSettlementDetails.receiptSettlementDetails.debit.transno.eq(debit.getTransno()));
		for(ReceiptSettlementDetails credits:settlements){
			SystemTransactions creditTrans = credits.getCredit();
			creditTrans.setBalance(creditTrans.getBalance().abs().add(credits.getDebit().getBalance().abs()));
			trans.add(creditTrans);
			ReceiptSettlementDetails settlement = new ReceiptSettlementDetails();
			settlement.setSettlementId(null);
			settlement.setAllocatedAmt(credits.getAllocatedAmt());
			settlement.setClientCredit(credits.getClientCredit());
			settlement.setDrCr((credits.getAllocatedAmt().compareTo(BigDecimal.ZERO)==-1)?"C":"D");
			settlement.setAlloUser(userUtils.getCurrentUser());
			settlement.setAllocDate(new Date());
			settlement.setCredit(credits.getCredit());
			settlement.setDebit(credits.getDebit());
			settlement.setCreditRefNo(credits.getCreditRefNo());
			settlement.setDebitRefNo(credits.getDebitRefNo());
			settlementDetails.add(settlement);
		}
		settlementRepo.save(settlementDetails);
		if(debitBalance.compareTo(BigDecimal.ZERO)==1) {
			if (!debit.getTransdc().equalsIgnoreCase("D"))
				throw new BadRequestException("Can only allocate against a debit transaction. Contact Admin on Trans " + debit.getRefNo());
			if(receiptBalance.abs().compareTo(BigDecimal.ZERO)!=1) throw new BadRequestException("Credit Allocation Amount cannot be zero or less than zero "+receiptBalance);
//			if (receiptBalance.abs().compareTo(debitBalance) == 1 || receiptBalance.abs().compareTo(debitBalance) == 0) {
			    BigDecimal settledAmt = debit.getNetAmount();
			    BigDecimal crSettledAmt  = credit.getNetAmount();
				debit.setBalance(BigDecimal.ZERO);
				debit.setSettleAmt(settledAmt);
			   if(credit.getTransType().equalsIgnoreCase("RC")){
				   credit.setBalance(crSettledAmt);
				   credit.setSettleAmt(BigDecimal.ZERO);
			   }
			  else{
				   credit.setBalance(BigDecimal.ZERO);
				   credit.setSettleAmt(crSettledAmt);
			   }

//				}else
//					credit.setSettleAmt((credit.getSettleAmt().add(settledAmt)));
//			}
//			else {
//				if(debit.getSettleAmt()==null){
//					debit.setSettleAmt(receiptBalance.abs());
//				}else
//					debit.setSettleAmt(debit.getSettleAmt().add(receiptBalance.abs()));
//				debit.setBalance(debit.getBalance().subtract(receiptBalance.abs()));
//				credit.setBalance(BigDecimal.ZERO);
//				if(credit.getSettleAmt()==null){
//					credit.setSettleAmt(receiptBalance.abs());
//				}else
//					credit.setSettleAmt((credit.getSettleAmt().add(receiptBalance.abs())).negate());
//			}
			trans.add(debit);
			trans.add(credit);
			transRepo.save(trans);


		}
	}

	@Override
	@Modifying
	@Transactional(readOnly = false, rollbackFor = { BadRequestException.class }, propagation = Propagation.REQUIRED)
	public void allocateLifeReceipt(Long receiptId, User user) throws BadRequestException {
		ReceiptTrans receipt = receiptRepo.getReceiptDetails(receiptId);
		SystemTrans transaction = new SystemTrans();
		transaction.setDoneDate(new Date());
		transaction.setDoneBy(user);
		transaction.setTransLevel("U");
		transaction.setTransCode("RC"); //A way to setup and look up for transaction transcode
		transaction.setTransAuthorised("N");
		systransRepo.save(transaction);
		Iterable<ReceiptTransDtls> rctDetails =receiptDetailsRepository.findAllReceipts(receiptId);
		BigDecimal settleAmt =BigDecimal.ZERO;
		for (ReceiptTransDtls tran : rctDetails) {
			System.out.println(tran);
			PolicyTrans policyTrans = tran.getPolicy();
//			System.out.println(policyTrans);
			BigDecimal receiptBalance = tran.getRctAmount();
			SystemTransactions transact = createReceiptTransaction(receipt.getReceiptNo(), tran.getRctAmount(), receiptBalance, receipt.getCollectionAccount().getCurrencies(), policyTrans.getClient(), policyTrans.getAgent(), null, transaction, tran.getNarration(),settleAmt,user,policyTrans,"C",null);
			LifeReceipts lifeReceipt = new LifeReceipts();
			lifeReceipt.setPolicyTrans(policyTrans);
			lifeReceipt.setDrCr(tran.getRctDC());
			lifeReceipt.setReceiptAmt(tran.getRctAmount());
			lifeReceipt.setReceiptDate(receipt.getReceiptDate());
			lifeReceipt.setReceiptTrans(receipt);
			lifeReceipt.setBalanceAmt(tran.getRctAmount());
			lifeReceipt.setDoneBy(user);
			lifeReceipt.setDoneDate(new Date());
			lifeReceipt.setSystemTransaction(transact);
			LifeReceipts saveLifeReceipt = lifeReceiptsRepo.save(lifeReceipt);
			if (policyTrans.getAuthStatus().equalsIgnoreCase("A")||policyTrans.getAuthStatus().equalsIgnoreCase("CV")) {
				lifeService.allocateLifeRcptBalance(policyTrans.getPolicyId());

			}




		}
	}

	@Override
	@Modifying
	@Transactional(readOnly = false, rollbackFor = { BadRequestException.class }, propagation = Propagation.REQUIRED)
	public void deallocateLifeReceipt(Long receiptId, User user,SystemTransactions transact) throws BadRequestException {
		ReceiptTrans receipt = receiptRepo.getReceiptDetails(receiptId);
        boolean allocated = true;
		Iterable<LifeReceipts> rctDetails = lifeReceiptsRepo.findAll(QLifeReceipts.lifeReceipts.receiptTrans.receiptId.eq(receiptId));
		if (lifeallocationsRepo.count(QLifeReceiptAllocations.lifeReceiptAllocations.lifeReceipts.receiptId.eq(receiptId))<=0){
			allocated =false;
		}
		BigDecimal settleAmt =BigDecimal.ZERO;
		for (LifeReceipts tran : rctDetails) {
			System.out.println(tran);
			LifeReceipts lifeReceipt = new LifeReceipts();
			lifeReceipt.setPolicyTrans(tran.getPolicyTrans());
			lifeReceipt.setDrCr("D");
			lifeReceipt.setReceiptAmt(tran.getReceiptAmt());
			lifeReceipt.setReceiptDate(receipt.getReceiptDate());
			lifeReceipt.setReceiptTrans(receipt);
			lifeReceipt.setBalanceAmt(tran.getBalanceAmt());
			lifeReceipt.setDoneBy(user);
			lifeReceipt.setDoneDate(new Date());
			lifeReceipt.setSystemTransaction(transact);
			lifeReceipt.setOriginalLifeReceipts(tran);
			LifeReceipts saveLifeReceipt = lifeReceiptsRepo.save(lifeReceipt);
			if (allocated){
				lifeService.deallocateLifeRcpt(saveLifeReceipt,user);
			}

		}
	}

	@Override
	@Modifying
	@Transactional(readOnly = false, rollbackFor = { BadRequestException.class }, propagation = Propagation.REQUIRED)
	public void allocateCashBasisTrans(Long polCode, User user) throws BadRequestException {
		Iterable<ReceiptTransDtls> receiptTransDtls = receiptDetailsRepository.findAll(QReceiptTransDtls.receiptTransDtls.policy.policyId.eq(polCode));
		for(ReceiptTransDtls receiptTransDtls1:receiptTransDtls){
			boolean directReceipt = receiptTransDtls1.getReceipt().getDirectReceipt()!=null && "Y".equalsIgnoreCase(receiptTransDtls1.getReceipt().getDirectReceipt());
			processReceiptDtls(receiptTransDtls1.getReceipt(),directReceipt,user,false);
		}


	}

	@Override
	@Modifying
	@Transactional(readOnly = false, rollbackFor = { BadRequestException.class }, propagation = Propagation.REQUIRED)
	public void allocateReceipt(Long receiptId, User user) throws BadRequestException {
		ReceiptTrans receipt = receiptRepo.getReceiptDetails(receiptId);
		boolean directReceipt = receipt.getDirectReceipt()!=null && "Y".equalsIgnoreCase(receipt.getDirectReceipt());
		processReceiptDtls(receipt,directReceipt,user,true);
		
	}

	@Override
	public void allocateCommReceipt(Long receiptId, User user) throws BadRequestException {
		ReceiptTrans receipt = receiptRepo.getReceiptDetails(receiptId);
		Iterable<ReceiptTransDtls> rctDetails = receipt.getReceiptDtls();
		List<SystemTransactions> transactions = new ArrayList<>();
		SystemTrans transaction = new SystemTrans();
		transaction.setDoneDate(new Date());
		transaction.setDoneBy(user);
		transaction.setTransLevel("U");
		transaction.setTransCode("RC"); //A way to setup and look up for transaction transcode
		transaction.setTransAuthorised("N");
		systransRepo.save(transaction);
		createReceiptTransaction(receipt.getReceiptNo(), receipt.getReceiptAmount(), receipt.getReceiptAmount(), receipt.getCollectionAccount().getCurrencies(),null, receipt.getInsurance(), null, transaction, receipt.getReceiptDesc(), BigDecimal.ZERO, user, null,"A", receipt.getInsurance().getShtDesc());

	}

	@Transactional(readOnly = false, rollbackFor = { BadRequestException.class }, propagation = Propagation.REQUIRED)
	void processReceiptDtls(ReceiptTrans receipt, final boolean directReceipt, final User user, final boolean newReceipt) throws BadRequestException{
		SystemTrans transaction = new SystemTrans();
		transaction.setDoneDate(new Date());
		transaction.setDoneBy(user);
		transaction.setTransLevel("U");
		transaction.setTransCode("RC"); //A way to setup and look up for transaction transcode
		transaction.setTransAuthorised("N");
		systransRepo.save(transaction);
		Iterable<ReceiptTransDtls> rctDetails = receipt.getReceiptDtls() != null ? receipt.getReceiptDtls() : receipt.getDetails();
		List<SystemTransactions> transactions = new ArrayList<>();
		for (ReceiptTransDtls tran : rctDetails) {
			SystemTransactionsTemp systemTransactionsTemp = tran.getTransactionsTemp();
			if(newReceipt && (systemTransactionsTemp!=null && systemTransactionsTemp.getBalance().compareTo(BigDecimal.ZERO) > 0)){
				BigDecimal debitBalances = systemTransactionsTemp.getBalance();
				if (debitBalances.compareTo(BigDecimal.ZERO) == 1) {
					if (!systemTransactionsTemp.getTransdc().equalsIgnoreCase("D"))
						throw new BadRequestException("Can only allocate against a debit transaction. Contact Admin on Trans " + systemTransactionsTemp.getRefNo());
					BigDecimal receiptAmt = tran.getRctAmount().abs();

					if (receiptAmt.compareTo(BigDecimal.ZERO) != 1)
						throw new BadRequestException("Receipt Allocation Amount cannot be zero or less than zero " + receiptAmt);
					if (receiptAmt.compareTo(debitBalances) == 1 || receiptAmt.compareTo(debitBalances) == 0) {
						systemTransactionsTemp.setBalance(BigDecimal.ZERO);
						systemTransactionsTemp.setSettleAmt((systemTransactionsTemp.getSettleAmt() != null) ? systemTransactionsTemp.getSettleAmt().add(debitBalances.abs()) : debitBalances.abs());
//						receiptBalance = receiptAmt.subtract(debitBalances);
//						settleAmt = debitBalance.negate();
						//prorataAgBalance = 1;
					} else {
						if (systemTransactionsTemp.getSettleAmt() == null) {
							systemTransactionsTemp.setSettleAmt(receiptAmt.abs());
						} else
							systemTransactionsTemp.setSettleAmt(systemTransactionsTemp.getSettleAmt().add(receiptAmt.abs()));
						systemTransactionsTemp.setBalance(systemTransactionsTemp.getBalance().subtract(receiptAmt.abs()));

//						receiptBalance = BigDecimal.ZERO;
//						settleAmt = receiptAmt;
						//prorataAgBalance = receiptAmt.doubleValue()/debitTrans.getNetAmount().doubleValue();
					}
					systemTransactionsTempRepo.save(systemTransactionsTemp);
					if(systemTransactionsTemp.getBalance()!=null && systemTransactionsTemp.getBalance().compareTo(BigDecimal.ZERO)==0){
						policyAuthorization.authorizePolicy(systemTransactionsTemp.getPolicy().getPolicyId(),systemTransactionsTemp.getPostedUser());
					}
				}

			}
			else {
				SystemTransactions debitTrans = null;
				if(systemTransactionsTemp!=null){
					debitTrans = transRepo.findOne(QSystemTransactions.systemTransactions.refNo.eq(systemTransactionsTemp.getRefNo()).and(QSystemTransactions.systemTransactions.transdc.eq("D"))
					              .and(QSystemTransactions.systemTransactions.clientType.eq("C")));

				}else if(tran.getTransaction()!=null) {
					debitTrans = tran.getTransaction();
				}
				if(debitTrans==null){
					throw new BadRequestException("Unable to get debit transaction for allocation");
				}
				BigDecimal debitBalance = debitTrans.getBalance();
				SystemTransactions agentTrans = transRepo.findOne(QSystemTransactions.systemTransactions.refNo.eq(debitTrans.getRefNo())
						.and(QSystemTransactions.systemTransactions.clientType.eq("A").and(QSystemTransactions.systemTransactions.transType.in("NBD", "RND", "APD")))
						.and(QSystemTransactions.systemTransactions.transdc.eq("C")));
				BigDecimal receiptBalance = tran.getRctAmount();
				Iterable<ReceiptSettlementDetails> settlementDetails = settlementRepo.findAll(QReceiptSettlementDetails.receiptSettlementDetails.debit.refNo.eq(debitTrans.getRefNo()));
				if (debitBalance.compareTo(BigDecimal.ZERO) == 1) {
					if (!debitTrans.getTransdc().equalsIgnoreCase("D"))
						throw new BadRequestException("Can only allocate against a debit transaction. Contact Admin on Trans " + debitTrans.getRefNo());
					BigDecimal receiptAmt = tran.getRctAmount().abs();
					BigDecimal settleAmt = BigDecimal.ZERO;

					double prorataAgBalance = receiptAmt.doubleValue() / debitTrans.getNetAmount().doubleValue();

					if (receiptAmt.compareTo(BigDecimal.ZERO) != 1)
						throw new BadRequestException("Receipt Allocation Amount cannot be zero or less than zero " + receiptAmt);
					if (receiptAmt.compareTo(debitBalance) == 1 || receiptAmt.compareTo(debitBalance) == 0) {
						debitTrans.setBalance(BigDecimal.ZERO);
						debitTrans.setSettleAmt((debitTrans.getSettleAmt() != null) ? debitTrans.getSettleAmt().add(debitBalance.abs()) : debitBalance.abs());
						receiptBalance = receiptAmt.subtract(debitBalance);
						settleAmt = debitBalance.negate();
						//prorataAgBalance = 1;
					} else {
						if (debitTrans.getSettleAmt() == null) {
							debitTrans.setSettleAmt(receiptAmt.abs());
						} else
							debitTrans.setSettleAmt(debitTrans.getSettleAmt().add(receiptAmt.abs()));
						debitTrans.setBalance(debitTrans.getBalance().subtract(receiptAmt.abs()));
						receiptBalance = BigDecimal.ZERO;
						settleAmt = receiptAmt;
						//prorataAgBalance = receiptAmt.doubleValue()/debitTrans.getNetAmount().doubleValue();
					}

					//postReceiptAccount(tran,transaction);
//				System.out.println("Prorata..."+prorataAgBalance);
					SystemTransactions transact = createReceiptTransaction(receipt.getReceiptNo(), tran.getRctAmount(), receiptBalance, receipt.getCollectionAccount().getCurrencies(), debitTrans.getClient(), debitTrans.getAgent(), debitTrans, transaction, tran.getNarration(), settleAmt, user, null,"C",null);
					if (directReceipt) {
						Currencies currencies = debitTrans.getCurrency();
						BigDecimal agencyAllocAmt = agentTrans.getNetAmount().multiply(BigDecimal.valueOf(prorataAgBalance)).setScale(currencies.getRoundOff(), BigDecimal.ROUND_HALF_EVEN);
						BigDecimal commAmt = (agentTrans.getCommission().abs().subtract(agentTrans.getWhtx().abs())).multiply(BigDecimal.valueOf(prorataAgBalance)).setScale(currencies.getRoundOff(), BigDecimal.ROUND_HALF_EVEN);
						agentTrans.setBalance(agentTrans.getBalance().add(agencyAllocAmt.abs()));
						agentTrans.setSettleAmt((agentTrans.getSettleAmt() == null) ? agencyAllocAmt.abs() : agentTrans.getSettleAmt().add(agencyAllocAmt.abs()));
						transactions.add(agentTrans);
						SystemTransactions commTrans = createCommissionTrans(commAmt, currencies, agentTrans.getAgent(), agentTrans, transaction, "Agent Commission Transaction", transact.getRefNo());
						createSettlements(commTrans, transact, agencyAllocAmt, user);
					}
					createSettlements(debitTrans, transact, settleAmt, user);
					if (debitTrans.getBalance().compareTo(BigDecimal.ZERO) == 0)
						if (debitTrans.getPolicy().getSubAgentComm() != null && debitTrans.getPolicy().getSubAgentComm().compareTo(BigDecimal.ZERO) != 0) {
//							PolicyTrans policy =  tran.getTransaction().getPolicy();
							PolicyTrans policy = tran.getPolicy();
							SystemTransactions trans = new SystemTransactions();
							trans.setAmount(policy.getSubAgentComm().abs().multiply(sign("C")));
							trans.setAuthDate(new Date());
							trans.setAuthorised("Y");
							trans.setBalance(policy.getSubAgentComm().abs().multiply(sign("C")));
							trans.setBranch(policy.getBranch());
							trans.setClientType("C");
							trans.setControlAcc(policy.getSubAgent().getShtDesc());
							trans.setClient(policy.getClient());
							trans.setCurrRate(new BigDecimal(1));
							trans.setCurrency(policy.getTransCurrency());
							trans.setNarrations("Sub Agent Commission Trans");
							trans.setNetAmount(policy.getSubAgentComm().abs().multiply(sign("C")));
							trans.setOrigin("U");
							trans.setPolicy(policy);
							trans.setRefNo(policy.getRefNo());
							trans.setTransDate(new Date());
							trans.setTransdc("C");
							trans.setTransType("SAG"); //Should not be hardcorded
							trans.setUserAuth(user.getUsername());
							trans.setWhtx(BigDecimal.ZERO);
							trans.setTransaction(transaction);
							trans.setPostedDate(new Date());
							trans.setPostedUser(user);
							trans.setAgent(policy.getSubAgent());
							transactions.add(trans);


						}
					transactions.add(debitTrans);
				}
				transRepo.save(transactions);
			}
		}
	}
	
	@Override
	@Transactional(readOnly = false)
	public SystemTransactions createReceiptTransaction(String refNo, BigDecimal receiptAmt, BigDecimal receiptbalance,
			Currencies currency,ClientDef client,AccountDef accnt, SystemTransactions prevTrans,SystemTrans transaction,
			String narration,BigDecimal settleAmt, User user, PolicyTrans policy, String type, String controlAcc){
		SystemTransactions trans = new SystemTransactions();
		trans.setAmount(receiptAmt.abs().multiply(sign("C")));
		trans.setAuthDate(new Date());
		trans.setAuthorised("Y");
		trans.setBalance(receiptbalance.abs().multiply(sign("C")));
		if (prevTrans!=null){
			trans.setBranch(prevTrans.getBranch());
			trans.setControlAcc(prevTrans.getControlAcc());
			trans.setPolicy(prevTrans.getPolicy());
		}else if (policy!=null){
			trans.setBranch(policy.getBranch());
			trans.setControlAcc(policy.getPolNo());
			trans.setPolicy(policy);
		}

		if(controlAcc!=null){
			trans.setControlAcc(controlAcc);
			trans.setClientType("A");
			trans.setAgent(accnt);
		}
        else
		trans.setClientType("C");

		trans.setClient(client);
		trans.setCurrRate(new BigDecimal(1));
		trans.setCurrency(currency);
		trans.setNarrations(narration);
		trans.setNetAmount(receiptAmt.abs().multiply(sign("C")));
		trans.setSettleAmt(settleAmt.abs().multiply(sign("C")));
		trans.setOrigin("U");

		trans.setRefNo(refNo);
		trans.setTransDate(new Date());
		trans.setTransdc("C");
		trans.setTransType("RC"); //Should not be hardcorded
		trans.setUserAuth(user.getUsername());
		trans.setWhtx(BigDecimal.ZERO);
		trans.setTransaction(transaction);
		trans.setPostedDate(new Date());
		trans.setPostedUser(user);
		SystemTransactions createdTrans =  transRepo.save(trans);
		return createdTrans;
	}

	@Override
	@Transactional(readOnly = false)
	public SystemTransactions createInstalmentTransaction(String refNo, BigDecimal AllocAmount, SystemTrans transaction,
													   String narration, PolicyTrans policy){
		SystemTransactions trans = new SystemTransactions();
		trans.setAmount(AllocAmount.abs().multiply(sign("D")));
		trans.setAuthDate(new Date());
		trans.setAuthorised("Y");
		trans.setBalance(AllocAmount.abs().multiply(sign("D")));
		trans.setBranch(policy.getBranch());
		trans.setControlAcc(policy.getPolNo());
		trans.setPolicy(policy);
		trans.setClientType("A");
		trans.setClient(policy.getClient());
		trans.setCurrRate(new BigDecimal(1));
		trans.setCurrency(policy.getTransCurrency());
		trans.setNarrations(narration);
		trans.setNetAmount(AllocAmount.abs().multiply(sign("D")));
		trans.setSettleAmt(BigDecimal.ZERO);
		trans.setOrigin("U");
		trans.setRefNo(refNo);
		trans.setTransDate(new Date());
		trans.setTransdc("D");
		trans.setTransType("NBD"); //Should not be hardcorded
		trans.setUserAuth(userUtils.getCurrentUser().getUsername());
		trans.setWhtx(BigDecimal.ZERO);
		trans.setTransaction(transaction);
		trans.setPostedDate(new Date());
		trans.setPostedUser(userUtils.getCurrentUser());
		SystemTransactions createdTrans =  transRepo.save(trans);
		return createdTrans;
	}


	@Transactional(readOnly = false)
	private SystemTransactions createCommissionTrans(BigDecimal commAmt,
													   Currencies currency,AccountDef accnt, SystemTransactions prevTrans,SystemTrans transaction,
													   String narration,String receipt){
		SystemTransactions trans = new SystemTransactions();
		trans.setAmount(commAmt.abs());
		trans.setAuthDate(new Date());
		trans.setAuthorised("Y");
		trans.setBalance(commAmt.abs().multiply(sign("D")));
		trans.setBranch(prevTrans.getBranch());
		trans.setClientType("A");
		trans.setControlAcc(prevTrans.getControlAcc());
		trans.setOtherRef(receipt);
		trans.setAgent(accnt);
		trans.setCurrRate(new BigDecimal(1));
		trans.setCurrency(currency);
		trans.setNarrations(narration);
		trans.setNetAmount(commAmt.abs().multiply(sign("D")));
		trans.setSettleAmt(BigDecimal.ZERO);
		trans.setOrigin("U");
		trans.setPolicy(prevTrans.getPolicy());
		trans.setRefNo(prevTrans.getRefNo());
		trans.setTransDate(new Date());
		trans.setTransdc("D");
		trans.setTransType("COMM"); //Should not be hardcorded
		trans.setUserAuth(userUtils.getCurrentUser().getUsername());
		trans.setWhtx(BigDecimal.ZERO);
		trans.setTransaction(transaction);
		trans.setPostedDate(new Date());
		trans.setPostedUser(userUtils.getCurrentUser());
		SystemTransactions createdTrans =  transRepo.save(trans);
		return createdTrans;
	}

	@Override
	@Modifying
	@Transactional(readOnly = false, rollbackFor = {
			BadRequestException.class }, propagation = Propagation.REQUIRES_NEW)
	public void createSettlements(SystemTransactions debit, SystemTransactions credit, BigDecimal amount,User user)
			throws BadRequestException {
		String refno = debit.getRefNo();
		SystemTransactions creditTrans = transRepo.findOne(QSystemTransactions.systemTransactions.refNo.eq(refno)
		                                .and(QSystemTransactions.systemTransactions.clientType.in("A"))
		                                 .and(QSystemTransactions.systemTransactions.transType.in("NBD","APD","RND")));
		if(creditTrans==null){
			throw new BadRequestException("Error getting insurance transactions for allocation "+refno +";client="+debit.getClientType()+";transtype="+debit.getTransType());
		}
		ReceiptSettlementDetails settlement = new ReceiptSettlementDetails();
		settlement.setCredit(credit);
		settlement.setCreditRefNo(credit.getRefNo());
		settlement.setDebit(debit);
		settlement.setDebitRefNo(debit.getRefNo());
		settlement.setAllocatedAmt(amount.abs());
		settlement.setDrCr("C");
		settlement.setAllocDate(new Date());
		settlement.setClientCredit(creditTrans);
		settlement.setAlloUser(user);
		settlementRepo.save(settlement);
	}

	@Override
	@Modifying
	@Transactional(readOnly = false, rollbackFor = {
			BadRequestException.class }, propagation = Propagation.REQUIRED)
	public void createLifeSettlements(SystemTransactions debit, SystemTransactions credit, BigDecimal amount,User user)
			throws BadRequestException {
		String refno = debit.getRefNo();
		SystemTransactions creditTrans = transRepo.findOne(QSystemTransactions.systemTransactions.refNo.eq(refno)
				.and(QSystemTransactions.systemTransactions.clientType.in("A"))
				.and(QSystemTransactions.systemTransactions.transType.in("NBD","APD","RND")));
		if(creditTrans==null){
			throw new BadRequestException("Error getting insurance transactions for allocation "+refno +";client="+debit.getClientType()+";transtype="+debit.getTransType());
		}
		ReceiptSettlementDetails settlement = new ReceiptSettlementDetails();
		settlement.setCredit(credit);
		settlement.setCreditRefNo(credit.getRefNo());
		settlement.setDebit(debit);
		settlement.setDebitRefNo(debit.getRefNo());
		settlement.setAllocatedAmt(amount.abs());
		settlement.setDrCr("C");
		settlement.setAllocDate(new Date());
		settlement.setClientCredit(creditTrans);
		settlement.setAlloUser(user);
		settlementRepo.save(settlement);
	}

	@Override
	@Transactional(readOnly = false)
	public void createFundReceiptTransaction(long receiptId) {
         ReceiptTrans receiptTrans = receiptRepo.findOne(receiptId);
		SystemTrans transaction = new SystemTrans();
		transaction.setDoneDate(new Date());
		transaction.setDoneBy(userUtils.getCurrentUser());
		transaction.setTransLevel("U");
		transaction.setTransCode("RC"); //A way to setup and look up for transaction transcode
		transaction.setTransAuthorised("N");
		SystemTrans savedTrans = systransRepo.save(transaction);
		Iterable<ReceiptTransDtls> receiptTransDtls = receiptDetailsRepository.findAll(QReceiptTransDtls.receiptTransDtls.receipt.receiptId.eq(receiptId));
		List<SystemTransactions> systemTransactions = new ArrayList<>();
		List<SelfFundParams> fundParameters = new ArrayList<>();
		for(ReceiptTransDtls dtls:receiptTransDtls){
			SelfFundParams fundParams = dtls.getFundParams();
			SystemTransactions trans = new SystemTransactions();
			trans.setAmount(dtls.getRctAmount().abs().multiply(sign("C")));
			trans.setAuthDate(new Date());
			trans.setAuthorised("Y");
			if(fundParams.getSelfFundBalance()==null){
				trans.setBalance(fundParams.getFundDepositAmount().subtract(dtls.getRctAmount()));
			}
			else{
                trans.setBalance(fundParams.getSelfFundBalance().subtract(dtls.getRctAmount()));
			}
			trans.setBranch(dtls.getFundParams().getPolicyTrans().getBranch());
			trans.setClientType("C");
			trans.setControlAcc(dtls.getFundParams().getPolicyTrans().getClient().getTenantNumber());
			trans.setClient(dtls.getFundParams().getPolicyTrans().getClient());
			trans.setCurrRate(new BigDecimal(1));
			trans.setCurrency(receiptTrans.getCollectionAccount().getCurrencies());
			trans.setNarrations(dtls.getNarration());
			trans.setNetAmount(fundParams.getFundDepositAmount().abs().multiply(sign("C")));
			trans.setSettleAmt(dtls.getRctAmount().abs().multiply(sign("C")));
			trans.setOrigin("U");
			trans.setPolicy(dtls.getFundParams().getPolicyTrans());
			trans.setRefNo(receiptTrans.getReceiptNo());
			trans.setTransDate(new Date());
			trans.setTransdc("C");
			trans.setTransType("RC"); //Should not be hardcorded
			trans.setUserAuth(userUtils.getCurrentUser().getUsername());
			trans.setWhtx(BigDecimal.ZERO);
			trans.setTransaction(savedTrans);
			trans.setPostedDate(new Date());
			trans.setFundParams(fundParams);
			trans.setPostedUser(userUtils.getCurrentUser());
			systemTransactions.add(trans);
			if(fundParams.getSelfFundBalance()==null){
				fundParams.setSelfFundBalance(fundParams.getFundDepositAmount().subtract(dtls.getRctAmount()));
			}
			else{
				if(dtls.getRctAmount().compareTo(fundParams.getSelfFundBalance())==1){
				fundParams.setSelfFundBalance(BigDecimal.ZERO);
				}else
				fundParams.setSelfFundBalance(fundParams.getSelfFundBalance().subtract(dtls.getRctAmount()));
			}
		}
		transRepo.save(systemTransactions);
	}


	private BigDecimal sign(String type){
		return ("C".equalsIgnoreCase(type)?BigDecimal.ONE.multiply(BigDecimal.valueOf(-1)):BigDecimal.ONE.multiply(BigDecimal.valueOf(1)));
	}

	@Override
	@Modifying
	@Transactional(readOnly = false, rollbackFor = { BadRequestException.class }, propagation = Propagation.REQUIRED)
	public void deallocateReceipt(Long receiptId, String comment) throws BadRequestException {
		ReceiptTrans receiptTrans = receiptRepo.findOne(receiptId);
		if(receiptTrans.getDirectReceipt()!=null && "Y".equalsIgnoreCase(receiptTrans.getDirectReceipt())){
			throw new BadRequestException("Cannot cancel Direct Receipt....");
		}
		if(receiptTrans.getCancelled()!=null && "Y".equalsIgnoreCase(receiptTrans.getCancelled()) ){
			throw new BadRequestException("Receipt already Cancelled...Cannot cancel");
		}
		Iterable<PaymentAudit> paymentAudits = auditRepo.findAll(QPaymentAudit.paymentAudit.receiptTransNo.eq(receiptTrans.getReceiptNo()).
                and(QPaymentAudit.paymentAudit.posted.isNull().or(QPaymentAudit.paymentAudit.posted.ne("Y"))));
		if(paymentAudits.spliterator().getExactSizeIfKnown() > 0)
			throw new BadRequestException("Cannot cancel this Receipt..There is a pending insurance payment transaction on this receipt");
		SystemTransactions ctrans =null;
		String cancelRef = receiptTrans.getReceiptNo()+"/CN";
		receiptTrans.setCancelled("Y");
		receiptTrans.setCancelledBy(userUtils.getCurrentUser());
		receiptTrans.setCancelledRef(cancelRef);
		receiptTrans.setCancelledDate(new Date());
		receiptTrans.setCancelComment(comment);
		Iterable<SystemTransactions> toCancelTrans = transRepo.findAll(QSystemTransactions.systemTransactions.refNo.eq(receiptTrans.getReceiptNo()));
		if(toCancelTrans.spliterator().getExactSizeIfKnown()==0){
			Iterable<ReceiptTransDtls> receiptTransDtls = receiptDetailsRepository.findAll(QReceiptTransDtls.receiptTransDtls.receipt.receiptId.eq(receiptId));
			List<SystemTransactionsTemp> transactionsTempList = new ArrayList<>();
			for(ReceiptTransDtls transDtls:receiptTransDtls){
				if(transDtls.getTransactionsTemp()!=null){
					SystemTransactionsTemp transactionsTemp = transDtls.getTransactionsTemp();
					transactionsTemp.setBalance(transactionsTemp.getBalance().add(transDtls.getRctAmount()));
					transactionsTemp.setSettleAmt(transactionsTemp.getSettleAmt().subtract(transDtls.getRctAmount()));
					transactionsTempList.add(transactionsTemp);
				}
			}
			systemTransactionsTempRepo.save(transactionsTempList);
			receiptRepo.save(receiptTrans);
			return;
		}
		receiptRepo.save(receiptTrans);
		SystemTrans transaction = new SystemTrans();
		transaction.setDoneDate(new Date());
		transaction.setDoneBy(userUtils.getCurrentUser());
		transaction.setTransLevel("U");
		transaction.setTransCode("RC/N"); //A way to setup and look up for transaction transcode
		transaction.setTransAuthorised("N");
		SystemTrans savedTrans = systransRepo.save(transaction);
		List<SystemTransactions> systemTransactions = new ArrayList<>();
		List<ReceiptSettlementDetails> settlements = new ArrayList<>();
		Iterable<SystemTransactions> commissions = transRepo.findAll(QSystemTransactions.systemTransactions.otherRef.eq(receiptTrans.getReceiptNo()).and(QSystemTransactions.systemTransactions.transType.eq("COMM"))
		                                                     .and(QSystemTransactions.systemTransactions.transdc.eq("D")).and(QSystemTransactions.systemTransactions.clientType.eq("A")));



		for(SystemTransactions comm:commissions){
			SystemTransactions newComm = new SystemTransactions();
			newComm.setAgent(comm.getAgent());
			newComm.setAmount((comm.getAmount().abs().multiply(sign("C"))).setScale(2, BigDecimal.ROUND_HALF_EVEN));
			newComm.setAuthDate(new Date());
			newComm.setAuthorised("Y");
			newComm.setBalance((comm.getBalance().abs().multiply(sign("C"))).setScale(2, BigDecimal.ROUND_HALF_EVEN));
			newComm.setBranch(comm.getBranch());
			newComm.setClient(comm.getClient());
			newComm.setClientType(comm.getClientType());
			newComm.setCommission(((comm.getCommission()==null)?BigDecimal.ZERO :comm.getCommission().abs().multiply(sign("C"))).setScale(2, BigDecimal.ROUND_HALF_EVEN));
			newComm.setControlAcc(comm.getControlAcc());
			newComm.setCurrency(comm.getCurrency());
			newComm.setCurrRate(comm.getCurrRate());
			newComm.setExtras(((comm.getExtras()==null)?BigDecimal.ZERO :comm.getExtras().abs().multiply(sign("C"))).setScale(2, BigDecimal.ROUND_HALF_EVEN));
			newComm.setFundParams(comm.getFundParams());
			newComm.setIssueCardFee(((comm.getIssueCardFee()==null)?BigDecimal.ZERO :comm.getIssueCardFee().abs().multiply(sign("C"))).setScale(2, BigDecimal.ROUND_HALF_EVEN));
			newComm.setNarrations("Commission Claw Back");
			newComm.setNetAmount(((comm.getNetAmount()==null)?BigDecimal.ZERO :comm.getNetAmount().abs().multiply(sign("C"))).setScale(2, BigDecimal.ROUND_HALF_EVEN));
			newComm.setOrigin(comm.getOrigin());
			newComm.setOtherRef(comm.getOrigin()+"/CN");
			newComm.setPayeeName(comm.getPayeeName());
			newComm.setPhfund(((comm.getPhfund()==null)?BigDecimal.ZERO :comm.getPhfund().abs().multiply(sign("C"))).setScale(2, BigDecimal.ROUND_HALF_EVEN));
			newComm.setPolicy(comm.getPolicy());
			newComm.setPostedDate(new Date());
			newComm.setPostedUser(userUtils.getCurrentUser());
			newComm.setRefNo(comm.getRefNo());
			newComm.setReIssueCardFee(((comm.getReIssueCardFee()==null)?BigDecimal.ZERO :comm.getReIssueCardFee().abs().multiply(sign("C"))).setScale(2, BigDecimal.ROUND_HALF_EVEN));
			newComm.setSd(((comm.getSd()==null)?BigDecimal.ZERO :comm.getSd().negate()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
			newComm.setServiceCharge(((comm.getServiceCharge()==null)?BigDecimal.ZERO :comm.getServiceCharge().abs().multiply(sign("C"))).setScale(2, BigDecimal.ROUND_HALF_EVEN));
			newComm.setSettleAmt(((comm.getSettleAmt()==null)?BigDecimal.ZERO :comm.getSettleAmt().abs().multiply(sign("C"))).setScale(2, BigDecimal.ROUND_HALF_EVEN));
			newComm.setTl(((comm.getTl()==null)?BigDecimal.ZERO :comm.getTl().negate()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
			newComm.setTransaction(savedTrans);
			newComm.setTransDate(new Date());
			newComm.setTransdc("C");
			newComm.setTransType(comm.getTransType());
			newComm.setWhtx(((comm.getWhtx()==null)?BigDecimal.ZERO :comm.getWhtx().abs().multiply(sign("C"))).setScale(2, BigDecimal.ROUND_HALF_EVEN));
			systemTransactions.add(newComm);
		}
		for(SystemTransactions trans : toCancelTrans){
			SystemTransactions newTrans = new SystemTransactions();
			newTrans.setAgent(trans.getAgent());
			newTrans.setAmount((trans.getAmount().abs().multiply(sign("D"))).setScale(2, BigDecimal.ROUND_HALF_EVEN));
			newTrans.setAuthDate(new Date());
			newTrans.setAuthorised("Y");
			newTrans.setBalance(BigDecimal.ZERO);
			newTrans.setBranch(trans.getBranch());
			newTrans.setClient(trans.getClient());
			newTrans.setClientType(trans.getClientType());
			newTrans.setCommission(((trans.getCommission()!=null)? trans.getCommission().abs().multiply(sign("D")):BigDecimal.ZERO).setScale(2, BigDecimal.ROUND_HALF_EVEN));
			newTrans.setControlAcc(trans.getControlAcc());
			newTrans.setCurrency(trans.getCurrency());
			newTrans.setCurrRate(trans.getCurrRate());
			newTrans.setExtras(((trans.getExtras()!=null)? trans.getExtras().abs().multiply(sign("D")):BigDecimal.ZERO).setScale(2, BigDecimal.ROUND_HALF_EVEN));
			newTrans.setFundParams(trans.getFundParams());
			newTrans.setIssueCardFee(((trans.getIssueCardFee()!=null)?trans.getIssueCardFee().abs().multiply(sign("D")):BigDecimal.ZERO).setScale(2, BigDecimal.ROUND_HALF_EVEN));
			newTrans.setNarrations(trans.getNarrations());
			newTrans.setNetAmount((trans.getNetAmount().abs().multiply(sign("D"))).setScale(2, BigDecimal.ROUND_HALF_EVEN));
			newTrans.setOrigin(trans.getOrigin());
			newTrans.setOtherRef(trans.getOtherRef());
			newTrans.setPayeeName(trans.getPayeeName());
			newTrans.setPhfund( ((trans.getPhfund()!=null)? trans.getPhfund().abs().multiply(sign("D")):BigDecimal.ZERO).setScale(2, BigDecimal.ROUND_HALF_EVEN));
			newTrans.setPolicy(trans.getPolicy());
			newTrans.setPostedDate(new Date());
			newTrans.setPostedUser(userUtils.getCurrentUser());
			newTrans.setRefNo(cancelRef);
			newTrans.setReIssueCardFee( ((trans.getReIssueCardFee()!=null)? trans.getReIssueCardFee().abs().multiply(sign("D")):BigDecimal.ZERO).setScale(2, BigDecimal.ROUND_HALF_EVEN));
			newTrans.setSd((trans.getSd()!=null)? trans.getSd().abs():BigDecimal.ZERO);
			newTrans.setServiceCharge( ((trans.getServiceCharge()!=null)? trans.getServiceCharge().abs().multiply(sign("D")):BigDecimal.ZERO).setScale(2, BigDecimal.ROUND_HALF_EVEN));
			newTrans.setSettleAmt((trans.getSettleAmt().abs().multiply(sign("D"))).setScale(2, BigDecimal.ROUND_HALF_EVEN));
			newTrans.setTl(((trans.getTl()!=null)? trans.getTl().abs().multiply(sign("D")):BigDecimal.ZERO).setScale(2, BigDecimal.ROUND_HALF_EVEN));
			newTrans.setTransaction(savedTrans);
			newTrans.setTransDate(new Date());
			newTrans.setTransdc("D");
			newTrans.setTransType("CN");
			newTrans.setUserAuth(userUtils.getCurrentUser().getUsername());
			newTrans.setWhtx((trans.getWhtx().abs()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
			Iterable<ReceiptSettlementDetails> settlementDetails = settlementRepo.findAll(QReceiptSettlementDetails.receiptSettlementDetails.credit.transno.eq(trans.getTransno()));
			for(ReceiptSettlementDetails settlementDetail:settlementDetails){
				SystemTransactions debit = settlementDetail.getDebit();
				debit.setSettleAmt(debit.getSettleAmt().subtract(settlementDetail.getAllocatedAmt().abs()));
				debit.setBalance(debit.getBalance().add(settlementDetail.getAllocatedAmt().abs()));
				systemTransactions.add(debit);



				long authCount = auditRepo.count(QPaymentAudit.paymentAudit.receiptTransNo.eq(settlementDetail.getCreditRefNo()).and(QPaymentAudit.paymentAudit.posted.eq("Y")));
				if(authCount > 1)
					throw new BadRequestException("Cannot have more than paid insurance payment transaction...Contact System Admin");
				if(authCount > 0) {
                    SystemTransactions credit = transRepo.findOne(QSystemTransactions.systemTransactions.transno.eq(settlementDetail.getClientCredit().getTransno())
                                                                  .and(QSystemTransactions.systemTransactions.refNo.eq(debit.getRefNo()))
                                                                   .and(QSystemTransactions.systemTransactions.clientType.eq("A"))
                                                                    .and(QSystemTransactions.systemTransactions.transdc.eq("C")));
                    PaymentAudit audit = auditRepo.findOne(QPaymentAudit.paymentAudit.receiptTransNo.eq(settlementDetail.getCreditRefNo()).and(QPaymentAudit.paymentAudit.posted.eq("Y")));

//					credit.setSettleAmt(credit.getSettleAmt().abs().subtract(audit.getPaymentAmount().abs()).negate());
//					credit.setBalance(credit.getBalance().abs().add(audit.getPaymentAmount().abs()).negate());
					//systemTransactions.add(credit);
//                    double prorataAgBalance = settlementDetail.getAllocatedAmt().doubleValue()/debit.getNetAmount().doubleValue();
//                    BigDecimal prorata = new BigDecimal(prorataAgBalance);
                     ctrans = new SystemTransactions();
                    ctrans.setAgent(credit.getAgent());
                    ctrans.setAmount((audit.getPaymentAmount().abs().setScale(2, BigDecimal.ROUND_HALF_EVEN)));
                    ctrans.setAuthDate(new Date());
                    ctrans.setAuthorised("Y");
                    ctrans.setBalance((audit.getPaymentAmount().abs().setScale(2, BigDecimal.ROUND_HALF_EVEN)));
                    ctrans.setBranch(credit.getBranch());
                    ctrans.setClient(credit.getClient());
                    ctrans.setClientType(credit.getClientType());
                    ctrans.setCommission((audit.getCommAmount().abs().negate().setScale(2, BigDecimal.ROUND_HALF_EVEN)));
                    ctrans.setControlAcc(credit.getControlAcc());
                    ctrans.setCurrency(credit.getCurrency());
                    ctrans.setCurrRate(credit.getCurrRate());
//                    ctrans.setExtras((audit.getExtras().multiply(prorata)).setScale(2, BigDecimal.ROUND_HALF_EVEN));
//                    ctrans.setFundParams(credit.getFundParams());
//                    ctrans.setIssueCardFee((credit.getIssueCardFee()!=null?credit.getIssueCardFee().multiply(prorata):BigDecimal.ZERO).setScale(2, BigDecimal.ROUND_HALF_EVEN));
                    ctrans.setNarrations("Agency Transaction Clawback");
                    ctrans.setNetAmount((audit.getPaymentAmount().abs().setScale(2, BigDecimal.ROUND_HALF_EVEN)));
                    ctrans.setOrigin(credit.getOrigin());
                    ctrans.setOtherRef(credit.getOtherRef());
                    ctrans.setPayeeName(credit.getPayeeName());
//                    ctrans.setPhfund((credit.getPhfund()!=null?credit.getPhfund().multiply(prorata):BigDecimal.ZERO).setScale(2, BigDecimal.ROUND_HALF_EVEN));
                    ctrans.setPolicy(credit.getPolicy());
                    ctrans.setPostedDate(new Date());
                    ctrans.setPostedUser(userUtils.getCurrentUser());
                    ctrans.setRefNo(credit.getRefNo());
//                    ctrans.setReIssueCardFee((credit.getReIssueCardFee()!=null?credit.getReIssueCardFee().multiply(prorata):BigDecimal.ZERO).setScale(2, BigDecimal.ROUND_HALF_EVEN));
//                    ctrans.setSd((credit.getSd()!=null?credit.getSd().multiply(prorata):BigDecimal.ZERO).setScale(2, BigDecimal.ROUND_HALF_EVEN));
//                    ctrans.setServiceCharge((credit.getServiceCharge()!=null?credit.getServiceCharge().multiply(prorata):BigDecimal.ZERO).setScale(2, BigDecimal.ROUND_HALF_EVEN));
                    ctrans.setSettleAmt(BigDecimal.ZERO);
//                    ctrans.setTl((credit.getTl()!=null?credit.getTl().multiply(prorata):BigDecimal.ZERO).setScale(2, BigDecimal.ROUND_HALF_EVEN));
                    ctrans.setTransaction(transaction);
                    ctrans.setWhtx(audit.getWhtxAmount().abs().setScale(2, BigDecimal.ROUND_HALF_EVEN));
                    ctrans.setTransDate(new Date());
                    ctrans.setTransdc("D");
                    ctrans.setTransType("AGR");
                    ctrans.setUserAuth(userUtils.getCurrentUser().getUsername());
                    systemTransactions.add(ctrans);
                }
                else{
					Iterable<ReceiptTransDtls> receiptTransDtls = receiptDetailsRepository.findAll(QReceiptTransDtls.receiptTransDtls.receipt.receiptId.eq(receiptId));
					for(ReceiptTransDtls transDtls:receiptTransDtls){
						transDtls.setWithdrawn("Y");
						transDtls.setWithdrawnDate(new Date());
						transDtls.setUserWithdrawn(userUtils.getCurrentUser());
						receiptDetailsRepository.save(transDtls);
					}

					settlementDetail.setWithdrawn("Y");
					settlementDetail.setWithdrawnDate(new Date());
					settlementDetail.setUserWithdrawn(userUtils.getCurrentUser());
				}

				Iterable<SystemTransactions> subAgentTrans = transRepo.findAll(QSystemTransactions.systemTransactions.transType.eq("SAG")
				                                             .and(QSystemTransactions.systemTransactions.refNo.eq(debit.getRefNo())
															  .and(QSystemTransactions.systemTransactions.transdc.eq("C"))));

				for(SystemTransactions subAgent:subAgentTrans){
					SystemTransactions sgTrans = new SystemTransactions();
					sgTrans.setAgent(subAgent.getAgent());
					sgTrans.setAmount((subAgent.getAmount().abs().multiply(sign("D"))).setScale(2, BigDecimal.ROUND_HALF_EVEN));
					sgTrans.setAuthDate(new Date());
					sgTrans.setAuthorised("Y");
					sgTrans.setBalance((subAgent.getBalance().abs().multiply(sign("D"))).setScale(2, BigDecimal.ROUND_HALF_EVEN));
					sgTrans.setBranch(subAgent.getBranch());
					sgTrans.setClient(subAgent.getClient());
					sgTrans.setClientType(subAgent.getClientType());
					sgTrans.setCommission(((subAgent.getCommission()==null)?BigDecimal.ZERO :subAgent.getCommission().abs().multiply(sign("D"))).setScale(2, BigDecimal.ROUND_HALF_EVEN));
					sgTrans.setControlAcc(subAgent.getControlAcc());
					sgTrans.setCurrency(subAgent.getCurrency());
					sgTrans.setCurrRate(subAgent.getCurrRate());
					sgTrans.setExtras(((subAgent.getExtras()==null)?BigDecimal.ZERO :subAgent.getExtras().abs().multiply(sign("D"))).setScale(2, BigDecimal.ROUND_HALF_EVEN));
					sgTrans.setFundParams(subAgent.getFundParams());
					sgTrans.setIssueCardFee((subAgent.getIssueCardFee()==null)?BigDecimal.ZERO :subAgent.getIssueCardFee().abs().multiply(sign("D")));
					sgTrans.setNarrations("Sub Agent Commission");
					sgTrans.setNetAmount(((subAgent.getNetAmount()==null)?BigDecimal.ZERO :subAgent.getNetAmount().abs().multiply(sign("D"))).setScale(2, BigDecimal.ROUND_HALF_EVEN));
					sgTrans.setOrigin(subAgent.getOrigin());
//					sgTrans.setOtherRef(subAgent.getOrigin()+"/CN");
					sgTrans.setPayeeName(subAgent.getPayeeName());
					sgTrans.setPhfund(((subAgent.getPhfund()==null)?BigDecimal.ZERO :subAgent.getPhfund().abs().multiply(sign("D"))).setScale(2, BigDecimal.ROUND_HALF_EVEN));
					sgTrans.setPolicy(subAgent.getPolicy());
					sgTrans.setPostedDate(new Date());
					sgTrans.setPostedUser(userUtils.getCurrentUser());
					sgTrans.setRefNo(subAgent.getRefNo());
					sgTrans.setReIssueCardFee(((subAgent.getReIssueCardFee()==null)?BigDecimal.ZERO :subAgent.getReIssueCardFee().abs().multiply(sign("D"))).setScale(2, BigDecimal.ROUND_HALF_EVEN));
					sgTrans.setSd(((subAgent.getSd()==null)?BigDecimal.ZERO :subAgent.getSd().negate()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
					sgTrans.setServiceCharge(((subAgent.getServiceCharge()==null)?BigDecimal.ZERO :subAgent.getServiceCharge().abs().multiply(sign("D"))).setScale(2, BigDecimal.ROUND_HALF_EVEN));
					sgTrans.setSettleAmt(((subAgent.getSettleAmt()==null)?BigDecimal.ZERO :subAgent.getSettleAmt().abs().multiply(sign("D")).setScale(2, BigDecimal.ROUND_HALF_EVEN)));
					sgTrans.setTl((subAgent.getTl()==null)?BigDecimal.ZERO :subAgent.getTl().abs().multiply(sign("D")));
					sgTrans.setTransaction(savedTrans);
					sgTrans.setTransDate(new Date());
					sgTrans.setTransdc("D");
					sgTrans.setTransType(subAgent.getTransType());
					sgTrans.setWhtx(((subAgent.getWhtx()==null)?BigDecimal.ZERO :subAgent.getWhtx().abs().multiply(sign("D"))).setScale(2, BigDecimal.ROUND_HALF_EVEN));
					systemTransactions.add(sgTrans);
				}
			}
			trans.setBalance(BigDecimal.ZERO);
			trans.setSettleAmt(newTrans.getSettleAmt());
			systemTransactions.add(newTrans);
			systemTransactions.add(trans);
			ReceiptSettlementDetails settlement = new ReceiptSettlementDetails();
			settlement.setSettlementId(null);
			settlement.setAllocatedAmt((newTrans.getSettleAmt().abs()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
			if(ctrans!=null)
			settlement.setClientCredit(ctrans);
			settlement.setDrCr("D");
			settlement.setAlloUser(userUtils.getCurrentUser());
			settlement.setAllocDate(new Date());
			settlement.setCredit(trans);
			settlement.setDebit(newTrans);
			settlement.setCreditRefNo(trans.getRefNo());
			settlement.setDebitRefNo(newTrans.getRefNo());
			settlements.add(settlement);
			SystemTransactions savedSysTrans= transRepo.save(newTrans);
			if (receiptTrans.getReceiptType()!=null && "L".equalsIgnoreCase(receiptTrans.getReceiptType())){
				deallocateLifeReceipt(receiptId,userUtils.getCurrentUser(),savedSysTrans);
				// consider adding receipt reversal logic for the allocated  instalments
			}
		}
		settlementRepo.save(settlements);


	}

	@Override
	@Modifying
	@Transactional(readOnly = false, rollbackFor = {
			BadRequestException.class }, propagation = Propagation.REQUIRES_NEW)
	public void createClientSettlements(SystemTransactions debit, SystemTransactions credit, BigDecimal amount,User user)
			throws BadRequestException {
		ReceiptSettlementDetails settlement = new ReceiptSettlementDetails();
		settlement.setCredit(credit);
		settlement.setCreditRefNo(credit.getRefNo());
		settlement.setDebit(debit);
		settlement.setDebitRefNo(debit.getRefNo());
		settlement.setAllocatedAmt(amount.abs());
		settlement.setDrCr("C");
		settlement.setAllocDate(new Date());
		settlement.setClientCredit(credit);
		settlement.setAlloUser(user);
		settlementRepo.save(settlement);
	}

}