package com.brokersystems.brokerapp.soapws.service.impl;

import com.brokersystems.brokerapp.accounts.model.CollectionAccounts;
import com.brokersystems.brokerapp.accounts.model.QCollectionAccounts;
import com.brokersystems.brokerapp.accounts.repository.CollectionAcctsRepo;
import com.brokersystems.brokerapp.server.exception.BadRequestException;
import com.brokersystems.brokerapp.server.utils.NumberToWordsUtils;
import com.brokersystems.brokerapp.setup.dto.UserDTO;
import com.brokersystems.brokerapp.setup.model.*;
import com.brokersystems.brokerapp.setup.repository.AccountRepo;
import com.brokersystems.brokerapp.setup.repository.OrgBranchRepository;
import com.brokersystems.brokerapp.setup.repository.SequenceRepository;
import com.brokersystems.brokerapp.setup.service.OrganizationService;
import com.brokersystems.brokerapp.setup.service.UserService;
import com.brokersystems.brokerapp.soapws.FormResult;
import com.brokersystems.brokerapp.soapws.ReceiptForm;
import com.brokersystems.brokerapp.soapws.model.QWebServiceReceipt;
import com.brokersystems.brokerapp.soapws.model.WebServiceReceipt;
import com.brokersystems.brokerapp.soapws.model.WebServiceReceiptRepo;
import com.brokersystems.brokerapp.soapws.service.ReceiptingAllocation;
import com.brokersystems.brokerapp.trans.model.*;
import com.brokersystems.brokerapp.trans.repository.*;
import com.brokersystems.brokerapp.trans.service.AllocationService;
import com.brokersystems.brokerapp.trans.service.PolicyAuthorization;
import com.brokersystems.brokerapp.trans.service.ReceiptService;
import com.brokersystems.brokerapp.uw.model.PolicyTrans;
import com.mysema.query.types.Predicate;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class ReceiptingAllocationImpl implements ReceiptingAllocation {

    @Autowired
    private IntegrationDtlsRepo integrationDtlsRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private ReceiptRepository receiptRepo;

    @Autowired
    private ReceiptDetailsRepository rctDetailsRepo;

    @Autowired
    private SequenceRepository sequenceRepo;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private SystemTransactionsRepo transRepo;

    @Autowired
    private OrgBranchRepository branchRepository;

    @Autowired
    private ReceiptService receiptService;

    @Autowired
    private SystemTransRepo systemTransRepo;

    @Autowired
    private CollectionAcctsRepo collectionAcctsRepo;

    @Autowired
    private SystemTransRepo systransRepo;

    @Autowired
    private AllocationService allocationService;

    @Autowired
    private SettlementRepo settlementRepo;

    @Autowired
    private WebServiceReceiptRepo webServiceReceiptRepo;

    @Autowired
    private OrgBranchRepository orgBranchRepository;

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private SystemTransactionsTempRepo systemTransactionsTempRepo;

    @Autowired
    private PolicyAuthorization policyAuthorization;


    private static final Random random = new Random();

    @Modifying
    @Override
    @Transactional(readOnly = false, propagation = Propagation.NEVER)
    public String createWebReceipt(final ReceiptForm receiptForm) {
        System.out.println("create web receipt  " + receiptForm);
        WebServiceReceipt webServiceReceipt = new WebServiceReceipt();
        OrgBranch branch =receiptForm.getBankBranch()!=null? branchRepository.findOne(QOrgBranch.orgBranch.obShtDesc.eq(receiptForm.getBankBranch())):null;
        try {
            BeanUtils.copyProperties(webServiceReceipt, receiptForm);
            webServiceReceipt.setRefno(generateRandomString());
            webServiceReceipt.setBranchName(branch.getObName());
            webServiceReceiptRepo.save(webServiceReceipt);
            return webServiceReceipt.getRefno();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (DataAccessException | ConstraintViolationException | PersistenceException ex) {
            return "E";
        }
        return null;
    }

    @Override
    public FormResult processReceipts(final ReceiptForm receiptForm) {
        FormResult result = new FormResult();
        System.out.println("process receipt  " + receiptForm);
        String refno = createWebReceipt(receiptForm);
        try {
            if (refno != null && "E".equalsIgnoreCase(refno)) {
                result.setMessage("The Reference Code has been used before! ");
                result.setCode(refno);
                result.setReferenceCode(refno);
                return result;
            }
            String ref = autoReceipt(receiptForm);
            result.setMessage("Successful");
            result.setCode(refno);
            result.setReferenceCode(ref);
            WebServiceReceipt webServiceReceipt = webServiceReceiptRepo.findOne(QWebServiceReceipt.webServiceReceipt.refno.eq(refno));
            webServiceReceipt.setStatus("S");
            webServiceReceiptRepo.save(webServiceReceipt);
            return result;
        } catch (BadRequestException e) {
            e.printStackTrace();
            result.setMessage("Successful");
            result.setCode(refno);
            result.setReferenceCode(refno);
            return result;
        } catch (ParseException e) {
            e.printStackTrace();
            result.setMessage("Successful");
            result.setCode(refno);
            result.setReferenceCode(refno);
            return result;
        } catch (DataAccessException | ConstraintViolationException | PersistenceException ex) {
            ex.printStackTrace();
            result.setMessage("Failure");
            result.setCode(refno);
            result.setReferenceCode("Mandatory Fields not provided.....");
            return result;
        }

    }

    @Override
    @Transactional(readOnly = false, rollbackFor = {BadRequestException.class}, propagation = Propagation.REQUIRES_NEW)
    public String autoReceipt(final ReceiptForm receiptForm) throws BadRequestException, ParseException {
        System.out.println("auto receipt 1 " + receiptForm);
        long countIns = accountRepo.count(QAccountDef.accountDef.shtDesc.eq(receiptForm.getRiskNote()));
        String receiptType = countIns != 0 ? "C" : "";
//        if(receiptForm.getType()==null || !receiptForm.getType().equalsIgnoreCase("C")) {
//            long count = transRepo.count(QSystemTransactions.systemTransactions.refNo.eq(receiptForm.getRiskNote()).and(QSystemTransactions.systemTransactions.clientType.eq("C"))
//                    .and(QSystemTransactions.systemTransactions.transdc.eq("D")));
//            if (count == 0 || count > 1) throw new BadRequestException("Transaction not found");
//        }
        if (receiptForm.getBankBranch() == null) throw new BadRequestException("Branch has not been defined..");
        Long count1 = branchRepository.count(QOrgBranch.orgBranch.obShtDesc.eq(receiptForm.getBankBranch()));
        if (count1 == 0 || count1 > 1) throw new BadRequestException("Wrong Branch has not been defined..");
        SystemTransactions transaction = transRepo.findOne(QSystemTransactions.systemTransactions.refNo.eq(receiptForm.getRiskNote()).and(QSystemTransactions.systemTransactions.clientType.eq("C"))
                .and(QSystemTransactions.systemTransactions.transdc.eq("D")));
        SystemTransactionsTemp transactionsTemp = null;
        if (!receiptType.equalsIgnoreCase("C") && transaction == null) {
            transactionsTemp = systemTransactionsTempRepo.findOne(QSystemTransactionsTemp.systemTransactionsTemp.refNo.eq((receiptForm.getRiskNote())).and(QSystemTransactionsTemp.systemTransactionsTemp.clientType.eq("C"))
                    .and(QSystemTransactionsTemp.systemTransactionsTemp.transdc.eq("D")).and(QSystemTransactionsTemp.systemTransactionsTemp.authorised.eq("Y")));
        }
        UserDTO user = userService.findByUserName("AutoReceipting");
        CollectionAccounts collectionAccounts = collectionAcctsRepo.findOne(QCollectionAccounts.collectionAccounts.name.eq("T24"));
        OrgBranch branch = branchRepository.findOne(QOrgBranch.orgBranch.obShtDesc.eq(receiptForm.getBankBranch()));

        ReceiptTrans receiptTrans = new ReceiptTrans();
        if (receiptType.equalsIgnoreCase("C")) {
            if (countIns > 1)
                throw new BadRequestException("Invalid Intermediary...");
            receiptTrans.setInsurance(accountRepo.findOne(QAccountDef.accountDef.shtDesc.eq(receiptForm.getRiskNote())));
        }
//        if(receiptForm.getType()!=null && receiptForm.getType().equalsIgnoreCase("C")){
//            long countaccts = accountRepo.count(QAccountDef.accountDef.shtDesc.eq(receiptForm.getInsuranceId()));
//            if(countaccts==0 || countaccts > 1)
//                throw new BadRequestException("Invalid Intermediary...");
//         receiptTrans.setInsurance(accountRepo.findOne(QAccountDef.accountDef.shtDesc.eq(receiptForm.getInsuranceId())));
//        }
//        receiptTrans.setReceiptType(receiptForm.getType());
        receiptTrans.setReceiptType(receiptType);
        receiptTrans.setPayId(collectionAccounts.getCaId());
        receiptTrans.setBrnCode(branch.getObId());
        receiptTrans.setPaymentRef(receiptForm.getTransactionCode());
        receiptTrans.setPaidBy(receiptForm.getReceivedFrom());
        receiptTrans.setReceiptAmount(receiptForm.getAmount());
        receiptTrans.setReceiptDate(new SimpleDateFormat("yyyyMMddHHmmss").parse("20" + receiptForm.getTimeStamp() + "00"));
        receiptTrans.setReceiptDesc("Premium Payment via Bank Integration");
        List<ReceiptTransDtls> receiptTransDtlses = new ArrayList<>();
        ReceiptTransDtls transDtls = new ReceiptTransDtls();
//        transDtls.setTransNo((transaction!=null)?transaction.getTransno():null);
        transDtls.setRctAmount(receiptForm.getAmount());
        transDtls.setTransaction(transaction);
        transDtls.setTransactionsTemp(transactionsTemp);
        transDtls.setPolicy((transaction != null) ? transaction.getPolicy() : (transactionsTemp != null) ? transactionsTemp.getPolicy() : null);
        transDtls.setEndorsementNumber((transaction != null) ? transaction.getPolicy().getPolRevNo() : transactionsTemp != null ? transactionsTemp.getPolicy().getPolRevNo() : "");
        receiptTransDtlses.add(transDtls);
        receiptTrans.setDetails(receiptTransDtlses);
        return createReceipt(receiptTrans, userService.findById(user.getId()));

    }

    @Override
    public String createReceipt(final ReceiptTrans receipt, final User user) throws BadRequestException {
        System.out.println("auto receipt 1 " + receipt);
        if (receipt.getPayId() == null) {
            throw new BadRequestException("Collection Account is mandatory");
        }

        if (receipt.getBrnCode() == null) {
            throw new BadRequestException("Receipt Branch is mandatory...");
        }

        if (!StringUtils.isBlank(receipt.getPaymentRef())) {
            if (receiptRepo.count(QReceiptTrans.receiptTrans.paymentRef.eq(receipt.getPaymentRef())) > 0) {
                throw new BadRequestException("Receipt Reference Exists......");
            }
        }
        Predicate seqPredicate = QSystemSequence.systemSequence.transType.eq("R");
        if (sequenceRepo.count(seqPredicate) == 0)
            throw new BadRequestException("Sequence for Receipt Transactions has not been setup");
        SystemSequence sequence = sequenceRepo.findOne(seqPredicate);
        Long seqNumber = sequence.getNextNumber();
        final String receiptNo = sequence.getSeqPrefix() + String.format("%05d", seqNumber);
        receipt.setReceiptNo(receiptNo);
        receipt.setCounter(BigInteger.valueOf(seqNumber));
        List<ReceiptTransDtls> transDtls = new ArrayList<>();
        BigDecimal totalAllocAmount = BigDecimal.ZERO;
        SystemTrans transact = new SystemTrans();
        transact.setDoneDate(new Date());
        transact.setDoneBy(user);
        transact.setTransLevel("U");
        transact.setTransCode("RCT"); //A way to setup and look up for transaction transcode
        transact.setTransAuthorised("N");
        SystemTrans systemTrans = systemTransRepo.save(transact);
        if (receipt.getReceiptType() != null && !receipt.getReceiptType().equalsIgnoreCase("C")) {
            for (ReceiptTransDtls tran : receipt.getDetails()) {
                if (tran.getTransNo() == null && tran.getTransTempNo() == null)
                    throw new BadRequestException("Outstanding transaction for the debit cannot be found. Select another Transaction to continue...");
                if (tran.getRctAmount().compareTo(BigDecimal.ZERO) <= 0)
                    throw new BadRequestException("Receipt Amount cannot be zero or less than Zero...");


                SystemTransactions transaction = null;
                SystemTransactionsTemp transactionsTemp =null;
                if (tran.getTransNo() != null)
                    transaction = transRepo.findOne(tran.getTransNo());
                else if (tran.getTransTempNo() != null)
                    transactionsTemp = systemTransactionsTempRepo.findOne(tran.getTransTempNo());
                tran.setReceipt(receipt);
                tran.setTransaction(transaction);
                tran.setTransactionsTemp(transactionsTemp);
                tran.setEndorsementNumber((transaction != null) ? transaction.getPolicy().getPolRevNo() : transactionsTemp != null ? transactionsTemp.getPolicy().getPolRevNo() : "");
                tran.setRctType("INV");
                tran.setRctDC("C");
                transDtls.add(tran);
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
                receiptService.postReceiptAccount(receipt,systemTrans, subcode,tran.getRctAmount());
            }
            if (totalAllocAmount.compareTo(receipt.getReceiptAmount()) != 0) {
                throw new BadRequestException("Total Allocation Amount " + totalAllocAmount + " and Receipt amount "
                        + receipt.getReceiptAmount() + " doesn't tally....");
            }
        }

        final OrgBranch branch = branchRepository.findOne(receipt.getBrnCode());
        receipt.setBranch(branch);
        receipt.setCollectionAccount(collectionAcctsRepo.findOne(receipt.getPayId()));
        receipt.setReceiptUser(user);
        receipt.setReceiptTransDate(new Date());
        sequence.setLastNumber(seqNumber);
        sequence.setNextNumber(seqNumber + 1);
        if (receipt.getDirectReceipt() == null || "off".equalsIgnoreCase(receipt.getDirectReceipt()))
            receipt.setDirectReceipt("N");
        else if ("on".equalsIgnoreCase(receipt.getDirectReceipt()))
            receipt.setDirectReceipt("Y");
        else
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
        receipt.setPrinted("Y");
        ReceiptTrans trans = receiptRepo.save(receipt);
        rctDetailsRepo.save(transDtls);
        if (receipt.getReceiptType() != null && "C".equalsIgnoreCase(receipt.getReceiptType())) {
            allocateCommReceipt(receipt.getReceiptId(), user, branch);
        } else {
            allocateReceipt(receipt, user);
        }
        return trans.getReceiptNo();
    }

    @Override
    @Modifying
    @Transactional(readOnly = false, rollbackFor = {BadRequestException.class})
    public void allocateCommReceipt(final Long receiptId, final User user, final OrgBranch branch) throws BadRequestException {
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
        createReceiptTransaction(receipt.getReceiptNo(), receipt.getReceiptAmount(), receipt.getReceiptAmount(), receipt.getCollectionAccount().getCurrencies(), null, receipt.getInsurance(), null, transaction, receipt.getReceiptDesc(), BigDecimal.ZERO, user, branch);

    }

    @Override
    @Modifying
    @Transactional(readOnly = false, rollbackFor = {BadRequestException.class})
    public void allocateReceipt(final ReceiptTrans receipt, final User user) throws BadRequestException {
        List<SystemTransactions> transactions = new ArrayList<>();
        SystemTrans transaction = new SystemTrans();
        transaction.setDoneDate(new Date());
        transaction.setDoneBy(user);
        transaction.setTransLevel("U");
        transaction.setTransCode("RC"); //A way to setup and look up for transaction transcode
        transaction.setTransAuthorised("N");
        systransRepo.save(transaction);
        Iterable<ReceiptTransDtls> rctDetails = receipt.getDetails();
        boolean directReceipt = receipt.getDirectReceipt() != null && "Y".equalsIgnoreCase(receipt.getDirectReceipt());
        for (ReceiptTransDtls tran : rctDetails) {
            SystemTransactions debitTrans = tran.getTransaction();
            SystemTransactionsTemp systemTransactionsTemp = tran.getTransactionsTemp();
            if (systemTransactionsTemp != null && systemTransactionsTemp.getBalance().compareTo(BigDecimal.ZERO) > 0) {
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
                    if (systemTransactionsTemp.getBalance() != null && systemTransactionsTemp.getBalance().compareTo(BigDecimal.ZERO) == 0) {
                        policyAuthorization.authorizePolicy(systemTransactionsTemp.getPolicy().getPolicyId(), systemTransactionsTemp.getPostedUser());
                    }
                }

            } else {

                BigDecimal debitBalance = debitTrans != null ? debitTrans.getBalance() : systemTransactionsTemp.getBalance();
                SystemTransactions agentTrans = transRepo.findOne(QSystemTransactions.systemTransactions.refNo.eq(debitTrans.getRefNo())
                        .and(QSystemTransactions.systemTransactions.clientType.eq("A").and(QSystemTransactions.systemTransactions.transType.eq("NBD")))
                        .and(QSystemTransactions.systemTransactions.transdc.eq("C")));
                BigDecimal receiptBalance = tran.getRctAmount();
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
                    } else {
                        if (debitTrans.getSettleAmt() == null) {
                            debitTrans.setSettleAmt(receiptAmt.abs());
                        } else
                            debitTrans.setSettleAmt(debitTrans.getSettleAmt().add(receiptAmt.abs()));
                        debitTrans.setBalance(debitTrans.getBalance().subtract(receiptAmt.abs()));
                        receiptBalance = BigDecimal.ZERO;
                        settleAmt = receiptAmt;
                    }

                    SystemTransactions transact = createReceiptTransaction(receipt.getReceiptNo(), tran.getRctAmount(), receiptBalance, receipt.getCollectionAccount().getCurrencies(), debitTrans.getClient(), debitTrans.getAgent(), debitTrans, transaction, tran.getNarration(), settleAmt, user, debitTrans.getBranch());
                    if (directReceipt) {
                        Currencies currencies = debitTrans.getCurrency();
                        BigDecimal agencyAllocAmt = agentTrans.getNetAmount().multiply(BigDecimal.valueOf(prorataAgBalance)).setScale(currencies.getRoundOff(), BigDecimal.ROUND_HALF_EVEN);
                        BigDecimal commAmt = agentTrans.getCommission().multiply(BigDecimal.valueOf(prorataAgBalance)).setScale(currencies.getRoundOff(), BigDecimal.ROUND_HALF_EVEN);
                        agentTrans.setBalance(agentTrans.getBalance().add(agencyAllocAmt.abs()));
                        agentTrans.setSettleAmt((agentTrans.getSettleAmt() == null) ? agencyAllocAmt.abs() : agentTrans.getSettleAmt().add(agencyAllocAmt.abs()));
                        transactions.add(agentTrans);
                        SystemTransactions commTrans = createCommissionTrans(commAmt, currencies, agentTrans.getAgent(), agentTrans, transaction, "Agent Commission Transaction", transact.getRefNo(), user);
                        createSettlements(commTrans, transact, agencyAllocAmt, user);
                    }
                    createSettlements(debitTrans, transact, settleAmt, user);
                    if (debitTrans.getBalance().compareTo(BigDecimal.ZERO) == 0)
                        if (tran.getTransaction().getPolicy().getSubAgentComm() != null && tran.getTransaction().getPolicy().getSubAgentComm().compareTo(BigDecimal.ZERO) != 0) {
                            PolicyTrans policy = tran.getTransaction().getPolicy();
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
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public SystemTransactions createReceiptTransaction(final String refNo, final BigDecimal receiptAmt, final BigDecimal receiptbalance,
                                                        final Currencies currency, final ClientDef client, final AccountDef accnt, final SystemTransactions prevTrans, final SystemTrans transaction,
                                                        final String narration, final BigDecimal settleAmt, final User user, final OrgBranch branch) {
        SystemTransactions trans = new SystemTransactions();
        trans.setAmount(receiptAmt.abs().multiply(sign("C")));
        trans.setAuthDate(new Date());
        trans.setAuthorised("Y");
        trans.setBalance(receiptbalance.abs().multiply(sign("C")));
        trans.setBranch(branch);
        trans.setClientType("C");
        trans.setControlAcc((prevTrans != null && prevTrans.getControlAcc() != null) ? prevTrans.getControlAcc() : accnt.getShtDesc());
        trans.setClient(client);
        trans.setCurrRate(new BigDecimal(1));
        trans.setCurrency(currency);
        trans.setNarrations(narration);
        trans.setNetAmount(receiptAmt.abs().multiply(sign("C")));
        trans.setSettleAmt(settleAmt.abs().multiply(sign("C")));
        trans.setOrigin("U");
        trans.setPolicy((prevTrans != null && prevTrans.getPolicy() != null) ? prevTrans.getPolicy() : null);
        trans.setRefNo(refNo);
        trans.setTransDate(new Date());
        trans.setTransdc("C");
        trans.setTransType("RC"); //Should not be hardcorded
        trans.setUserAuth(user.getUsername());
        trans.setWhtx(BigDecimal.ZERO);
        trans.setTransaction(transaction);
        trans.setPostedDate(new Date());
        trans.setPostedUser(user);
        SystemTransactions createdTrans = transRepo.save(trans);
        return createdTrans;
    }

    @Override
    @Modifying
    @Transactional(readOnly = false, rollbackFor = {
            BadRequestException.class}, propagation = Propagation.REQUIRED)
    public void createSettlements(final SystemTransactions debit, final SystemTransactions credit, final BigDecimal amount, final User user)
            throws BadRequestException {
        String refno = debit.getRefNo();
        SystemTransactions creditTrans = transRepo.findOne(QSystemTransactions.systemTransactions.refNo.eq(refno)
                .and(QSystemTransactions.systemTransactions.clientType.in("A"))
                .and(QSystemTransactions.systemTransactions.transType.in("NBD", "APD")));
        if (creditTrans == null) {
            throw new BadRequestException("Error getting insurance transactions for allocation");
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
    public SystemTransactions createCommissionTrans(final BigDecimal commAmt,
                                             final Currencies currency, final AccountDef accnt, final SystemTransactions prevTrans, final SystemTrans transaction,
                                             final String narration, final String receipt, final User user) {
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
        trans.setUserAuth(user.getUsername());
        trans.setWhtx(BigDecimal.ZERO);
        trans.setTransaction(transaction);
        trans.setPostedDate(new Date());
        trans.setPostedUser(user);
        SystemTransactions createdTrans = transRepo.save(trans);
        return createdTrans;
    }

    private BigDecimal sign(String type) {
        return ("C".equalsIgnoreCase(type) ? BigDecimal.ONE.multiply(BigDecimal.valueOf(-1)) : BigDecimal.ONE.multiply(BigDecimal.valueOf(1)));
    }

    /**
     * Generate a random String
     *
     * @return
     */
    public static String generateRandomString() {
        final String characters = "abcdefghijklmnopqrstuvwxyz123456789";
        final int length = generateRandomNumber();
        final char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = characters.charAt(random.nextInt(characters.length()));
        }
        return new String(text);
    }

    /**
     * Generate a random number between 5 to 16
     *
     * @return
     */
    public static int generateRandomNumber() {
        final Random randomGenerator = new Random();
        return randomGenerator.nextInt(11) + 5;
    }
}
