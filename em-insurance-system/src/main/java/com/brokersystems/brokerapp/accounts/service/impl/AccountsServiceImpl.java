package com.brokersystems.brokerapp.accounts.service.impl;

import com.brokersystems.brokerapp.accounts.dtos.*;
import com.brokersystems.brokerapp.accounts.dtos.SettlementDTO;
import com.brokersystems.brokerapp.accounts.model.*;
import com.brokersystems.brokerapp.accounts.repository.*;
import com.brokersystems.brokerapp.accounts.service.AccountsService;
import com.brokersystems.brokerapp.claims.dtos.ClaimClaimantsDTO;
import com.brokersystems.brokerapp.claims.dtos.ClaimantsDTO;
import com.brokersystems.brokerapp.enums.AccountTypeEnum;
import com.brokersystems.brokerapp.security.CheckAuthLimits;
import com.brokersystems.brokerapp.server.datatables.DataTablesRequest;
import com.brokersystems.brokerapp.server.datatables.DataTablesResult;
import com.brokersystems.brokerapp.server.exception.AdminFeeException;
import com.brokersystems.brokerapp.server.exception.BadRequestException;
import com.brokersystems.brokerapp.server.utils.DateUtilities;
import com.brokersystems.brokerapp.server.utils.UserUtils;
import com.brokersystems.brokerapp.setup.dto.BranchDTO;
import com.brokersystems.brokerapp.setup.dto.CoaSubAccountsDTO;
import com.brokersystems.brokerapp.setup.model.*;
import com.brokersystems.brokerapp.setup.repository.*;
import com.brokersystems.brokerapp.setup.service.ParamService;
import com.brokersystems.brokerapp.trans.model.*;
import com.brokersystems.brokerapp.trans.repository.*;
import com.brokersystems.brokerapp.trans.service.AllocationService;
import com.brokersystems.brokerapp.trans.service.CommissionsPayinsService;
import com.brokersystems.brokerapp.uw.model.PolicyTrans;
import com.brokersystems.brokerapp.uw.model.QPolicyTrans;
import com.brokersystems.brokerapp.uw.repository.PolicyTransRepo;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.NumberExpression;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by peter on 3/25/2017.
 */
@Service
public class AccountsServiceImpl implements AccountsService {


    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private SettlementRepo settlementRepo;
    @Autowired
    private PaymentAuditRepo auditRepo;
    @Autowired
    private CheckAuthLimits authLimits;
    @Autowired
    private CurrencyRepository currencyRepo;
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private SystemTransRepo systransRepo;
    @Autowired
    private SystemTransactionsRepo transRepo;
    @Autowired
    private DateUtilities dateUtilities;
    @Autowired
    private CoaMainAccountsRepo mainAccountsRepo;
    @Autowired
    private CoaSubAccountsRepo subAccountsRepo;
    @Autowired
    private  RefundRepo refundRepo;
    @Autowired
    private BanksRepo banksRepo;
    @Autowired
    private BankBranchRepo bankBranchRepo;
    @Autowired
    private CollectionAcctsRepo acctsRepo;
    @Autowired
    private PaymentModeRepo paymentModeRepo;
    @Autowired
    private AllocationService allocationService;
    @Autowired
    private Environment env;
    @Autowired
    private SequenceRepository sequenceRepo;
    @Autowired
    private ReceiptDetailsRepository receiptDetailsRepository;
    @Autowired
    private TransMappingRepo mappingRepo;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ParamService paramService;
    @Autowired
    private PolicyTransRepo policyRepo;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private CommissionsPayinsService commissionsPayinsService;
    @Autowired
    private SystemTransactionsRepo systemTransactionsRepo;
    @Autowired
    private PayeesRepo payeesRepo;
    @Autowired
    private PayeeAccountsRepo payeeAccountsRepo;
    @Autowired
    private OpeningBalancesRepo balancesRepo;
    @Autowired
    private FinalReportFormatsRepo finalReportFormatsRepo;
    @Autowired
    private FinalReportFormatTotalsRepo reportFormatTotalsRepo;
    @Autowired
    private FinalReportFormatAccountsRepo finalReportFormatAccountsRepo;
    @Autowired
    private FinalReportFormatGroupAccountsRepo finalReportFormatGroupAccountsRepo;
    @Autowired
    private AccountYearsRepo yearsRepo;
    @Autowired
    private AccountYearPeriodsRepo periodsRepo;
    @Autowired
    private OrgBranchRepository branchRepository;

    @Autowired
    private AccountYearPeriodsRepo accountYearPeriodsRepo;


    @Override
    @Transactional(readOnly = true)
    public List<SettlementDTO> findAllocationDetails(Long agentCode, Long currencyCode, Date wefDate, Date wetDate, String pstatus) throws IllegalAccessException {
        List<Object[]> pages = null;
        Currencies currencies = currencyRepo.findOne(currencyCode);
        String databaseType = env.getProperty("database_type");
        if(org.apache.commons.lang.StringUtils.equalsIgnoreCase("postgres",databaseType)) {
            if (wefDate == null && wetDate == null)
                pages = settlementRepo.getPostgresSettlementDetails(agentCode, new Date(), new Date(), currencies.getRoundOff(),pstatus);
            else {

                pages = settlementRepo.getPostgresSettlementDetails(agentCode, wefDate, DateUtils.addDays(wetDate, 1), currencies.getRoundOff(),pstatus);
            }
        }
        else if(org.apache.commons.lang.StringUtils.equalsIgnoreCase("mssql",databaseType)){
            if (wefDate == null && wetDate == null)
                pages = settlementRepo.getSqlServerSettlementDetails(agentCode, new Date(), new Date(), currencies.getRoundOff(),pstatus);
            else {

                pages = settlementRepo.getSqlServerSettlementDetails(agentCode, wefDate, DateUtils.addDays(wetDate, 1), currencies.getRoundOff(),pstatus);
            }
        }

        List<SettlementDTO> settlementDTOs = new ArrayList<>();
        for(Object[] page:pages){
            SettlementDTO settlementDTO = new SettlementDTO();
            settlementDTO.setPolNo((String)page[0]);
            settlementDTO.setClientPolNo((String)page[1]);
            settlementDTO.setClientName((String)page[2]+" "+(String)page[3]);
            settlementDTO.setDrNo((String)page[4]);
            settlementDTO.setCrNo((String)page[5]);
            settlementDTO.setPayStatus((String)page[7]);
            settlementDTO.setBasicPrem((BigDecimal) page[8]);
            settlementDTO.setCommAmt((BigDecimal) page[9]);
            settlementDTO.setWhtx((BigDecimal) page[10]);
            settlementDTO.setSettleAmt((BigDecimal) page[13]);
            settlementDTO.setDebitBal((BigDecimal) page[12]);
            settlementDTO.setAllocAmt((BigDecimal) page[11]);
            settlementDTO.setTransType((String)page[15]);
            settlementDTOs.add(settlementDTO);
        }
        return settlementDTOs;
    }


    @Override
    public List<SettlementDTO> findSubAgentTransactions(Long agentCode, Date wefDate, Date wetDate, Long subAcctId) throws IllegalAccessException {
        List<Object[]> pages = null;
        String databaseType = env.getProperty("database_type");
        if(org.apache.commons.lang.StringUtils.equalsIgnoreCase("postgres",databaseType)) {

            pages = transRepo.getSubAgentTrans(agentCode, wefDate, wetDate, subAcctId);
        }
        else{
            if(agentCode!=null && agentCode==-2000){
                agentCode = null;
            }
            pages = transRepo.getSqlServerSubAgentTrans(agentCode,wefDate,wetDate,subAcctId);
        }
        List<SettlementDTO> settlementDTOs = new ArrayList<>();
        for(Object[] page:pages){
            SettlementDTO settlementDTO = new SettlementDTO();
            settlementDTO.setPolNo((String)page[0]);
            settlementDTO.setClientName((String)page[2]+" "+(String)page[3]);
            settlementDTO.setDrNo((String)page[1]);
            settlementDTO.setCommAmt((BigDecimal) page[4]);
            settlementDTO.setSettleAmt((BigDecimal) page[5]);
            settlementDTO.setDebitBal((BigDecimal) page[7]);
            settlementDTO.setAllocAmt((BigDecimal) page[6]);
            if(page[8] instanceof Long)
            settlementDTO.setAcctCode((Long) page[8]);
            else if(page[8]!=null && page[8] instanceof BigInteger){
                settlementDTO.setAcctCode(((BigInteger) page[8]).longValue());
            }
            else if(page[8]!=null && page[8] instanceof BigDecimal){
                settlementDTO.setAcctCode(((BigDecimal) page[8]).longValue());
            }
            settlementDTOs.add(settlementDTO);
        }
        return settlementDTOs;
    }

    private static NumberExpression emptyIfNull(NumberExpression expression) {
        return expression.coalesce(BigDecimal.ZERO).asNumber();
    }


    @Override
    @Transactional(readOnly = true)
    public DataTablesResult<SystemTransactions> findAgentsTransactions(DataTablesRequest request, Long agentCode, Long currencyCode) throws IllegalAccessException {
        AccountDef accountDef = null;
        String accShtDesc = "";
        if(agentCode!=null){
            accountDef = accountRepo.findOne(agentCode);
            accShtDesc = accountDef.getShtDesc();
        }
        if(agentCode==null) agentCode=-2000l;
        if(currencyCode==null) currencyCode=-2000l;

        Predicate pred = QSystemTransactions.systemTransactions.agent.acctId.eq(agentCode)
                         .and(QSystemTransactions.systemTransactions.currency.curCode.eq(currencyCode))
                         .and(QSystemTransactions.systemTransactions.clientType.eq("I"))
                         .and(QSystemTransactions.systemTransactions.transType.eq("PM"))
                         .and(QSystemTransactions.systemTransactions.controlAcc.eq(accShtDesc))
                         .and(QSystemTransactions.systemTransactions.authorised.eq("N").or(QSystemTransactions.systemTransactions.authorised.isNull()))
                         .and(QSystemTransactions.systemTransactions.narrations.eq("Creditor Payment"));
        Page<SystemTransactions> page = transRepo.findAll(pred, request);
        return new DataTablesResult(request, page);
    }

    @Override
    public DataTablesResult<SystemTransactions> findIAAgentsTransactions(DataTablesRequest request, Long agentCode, Long currencyCode) throws IllegalAccessException {
        AccountDef accountDef = null;
        String accShtDesc = "";
        if(agentCode!=null){
            accountDef = accountRepo.findOne(agentCode);
            accShtDesc = accountDef.getShtDesc();
        }
        if(agentCode==null) agentCode=-2000l;
        if(currencyCode==null) currencyCode=-2000l;
        Predicate pred =  QSystemTransactions.systemTransactions.currency.curCode.eq(currencyCode)
                .and(QSystemTransactions.systemTransactions.clientType.eq("I"))
                .and(QSystemTransactions.systemTransactions.transType.eq("PM"))
                .and(QSystemTransactions.systemTransactions.authorised.eq("N").or(QSystemTransactions.systemTransactions.authorised.isNull()))
                .and(QSystemTransactions.systemTransactions.narrations.eq("Sub Agent Payment"));
        if(agentCode!=-2000l){
            pred = QSystemTransactions.systemTransactions.agent.acctId.eq(agentCode)
                    .and(QSystemTransactions.systemTransactions.currency.curCode.eq(currencyCode))
                    .and(QSystemTransactions.systemTransactions.clientType.eq("I"))
                    .and(QSystemTransactions.systemTransactions.transType.eq("PM"))
                    .and(QSystemTransactions.systemTransactions.controlAcc.eq(accShtDesc))
                    .and(QSystemTransactions.systemTransactions.authorised.eq("N").or(QSystemTransactions.systemTransactions.authorised.isNull()))
                    .and(QSystemTransactions.systemTransactions.narrations.eq("Sub Agent Payment"));
        }

        Page<SystemTransactions> page = transRepo.findAll(pred, request);
        return new DataTablesResult(request, page);
    }

    @Override
    @Transactional(readOnly = true)
    public DataTablesResult<PaymentAudit> findTransPaymentAudits(DataTablesRequest request, Long transId) throws IllegalAccessException {
        if(transId==null) transId=-2000l;
        BooleanExpression pred = QPaymentAudit.paymentAudit.otherTransNo.transno.eq(transId);
        Page<PaymentAudit> page = auditRepo.findAll(pred.and(request.searchPredicate(QPaymentAudit.paymentAudit)), request);
        return new DataTablesResult(request, page);
    }


    @Override
    @Transactional(readOnly = true)
    public DataTablesResult<CoaMainAccounts> findChartOfAccounts(DataTablesRequest request) throws IllegalAccessException {
        Page<CoaMainAccounts> page = mainAccountsRepo.findAll(request.searchPredicate(QCoaMainAccounts.coaMainAccounts), request);
        return new DataTablesResult<>(request, page);
    }

    @Override
    @Transactional(readOnly = true)
    public DataTablesResult<CoaSubAccountsDTO> findChartSubAccounts(DataTablesRequest request, Long mainAccId) throws IllegalAccessException {
        if(mainAccId==null) mainAccId=-2000l;
        final String search = ( request.getSearch()!=null && request.getSearch().getValue()!=null)?"%"+request.getSearch().getValue().toLowerCase()+"%":"%%";
        List<Object[]> accounts = subAccountsRepo.findMainSubAccounts(mainAccId, search.toLowerCase(),request.getPageNumber(), request.getPageSize());
        final List<CoaSubAccountsDTO> accountsDTOList = new ArrayList<>();
        long rowCount = 0L;
        if(!accounts.isEmpty()) rowCount = (Integer)accounts.get(0)[12];
        for(Object[] account:accounts){
            CoaSubAccountsDTO accountsDTO = new CoaSubAccountsDTO();
            accountsDTO.setCoId(((BigDecimal)account[0]).longValue());
            accountsDTO.setAccountsOrder((String)account[1]);
            accountsDTO.setCode((String)account[2]);
            accountsDTO.setName((String)account[3]);
            accountsDTO.setIntegration((String)account[4]);
            accountsDTO.setControlAccount((String)account[5]);
            if(account[6]!=null){
                accountsDTO.setAcctTypeId(((BigDecimal)account[6]).longValue());
            }
            accountsDTO.setAccTypeName((String)account[7]);
            accountsDTO.setMainAcctId(((BigDecimal)account[8]).longValue());
            accountsDTO.setApplicableToScl((String)account[9]);
            accountsDTO.setSublass((String)account[10]);
            if(account[11]!=null){
                accountsDTO.setScId(((BigDecimal)account[11]).longValue());
            }
            accountsDTOList.add(accountsDTO);
        }
        Page<CoaSubAccountsDTO>  page = new PageImpl<>(accountsDTOList,request, rowCount);
        return new DataTablesResult<>(request, page);
    }

    @Transactional(readOnly = true)
    public BigDecimal getPayableAmount(String receiptRef,String debitRef, BigDecimal allocAmt) throws BadRequestException{
        if(receiptRef==null) throw new BadRequestException("Trans Alloc Id Cannot be null");
        if(debitRef==null) throw new BadRequestException("Debit Transaction cannot be null");
        if(allocAmt==null) throw new BadRequestException("Allocation Amount cannot be null......");
//        SystemTransactions debitTrans = transRepo.findOne(QSystemTransactions.systemTransactions.refNo.eq(debitRef)
//                .and(QSystemTransactions.systemTransactions.clientType.eq("C").and(QSystemTransactions.systemTransactions.transType.in("NBD","APD","RND","RFC")))
//                .and(QSystemTransactions.systemTransactions.transdc.eq("D")));
        SystemTransactions debitTrans =null;
        if (transRepo.count(QSystemTransactions.systemTransactions.refNo.eq(debitRef)
                .and(QSystemTransactions.systemTransactions.transType.in("APC")))==0) {
            debitTrans = transRepo.findOne(QSystemTransactions.systemTransactions.refNo.eq(debitRef)
                    .and(QSystemTransactions.systemTransactions.clientType.eq("C").and(QSystemTransactions.systemTransactions.transType.in("NBD", "APD", "RND")))
                    .and(QSystemTransactions.systemTransactions.transdc.eq("D")));
        }else
        {
            debitTrans = transRepo.findOne(QSystemTransactions.systemTransactions.refNo.eq(debitRef)
                    .and(QSystemTransactions.systemTransactions.clientType.eq("A").and(QSystemTransactions.systemTransactions.transType.in("APC")))
                    .and(QSystemTransactions.systemTransactions.transdc.eq("D")));
            debitTrans.setNetAmount(debitTrans.getAmount());
        }
        SystemTransactions creditTrans = null;
          if(transRepo.count(QSystemTransactions.systemTransactions.refNo.eq(receiptRef).and(QSystemTransactions.systemTransactions.transType.in("RC","APC"))
                  .and(QSystemTransactions.systemTransactions.transdc.eq("C")))==1)
            creditTrans = transRepo.findOne(QSystemTransactions.systemTransactions.refNo.eq(receiptRef).and(QSystemTransactions.systemTransactions.transType.in("RC","APC"))
                    .and(QSystemTransactions.systemTransactions.transdc.eq("C")));
          else
             creditTrans = settlementRepo.findOne(QReceiptSettlementDetails.receiptSettlementDetails.debit.refNo.eq(debitRef).and(QReceiptSettlementDetails.receiptSettlementDetails.credit.refNo.eq(receiptRef)
                             .and(QReceiptSettlementDetails.receiptSettlementDetails.credit.transType.in("RC","APC")))).getCredit();

        if(creditTrans==null)
            throw new BadRequestException("Unable to get Receipt Transaction to allocate...");
        BigDecimal receiptSettlement = BigDecimal.ZERO;
        if(auditRepo.count(QPaymentAudit.paymentAudit.receiptTransNo.eq(receiptRef).and(QPaymentAudit.paymentAudit.debitTransNo.eq(debitRef)))>0){
        Iterable<PaymentAudit> paymentAudit = auditRepo.findAll(QPaymentAudit.paymentAudit.receiptTransNo.eq(receiptRef).and(QPaymentAudit.paymentAudit.debitTransNo.eq(debitRef)));
            for (PaymentAudit paymentAudit1:paymentAudit){
                receiptSettlement = receiptSettlement.add(paymentAudit1.getPaymentAmount());
            }
        }
        SystemTransactions agentTrans = transRepo.findOne(QSystemTransactions.systemTransactions.refNo.eq(debitRef)
                .and(QSystemTransactions.systemTransactions.clientType.eq("A").and(QSystemTransactions.systemTransactions.transType.in("NBD","APD","RND")))
                .and(QSystemTransactions.systemTransactions.transdc.eq("C")));
        //double prorationRate = allocAmt.doubleValue()/(debitTrans.getNetAmount().doubleValue());
        double prorationRate = allocAmt.doubleValue()/(debitTrans.getNetAmount().doubleValue());


        Currencies currencies = debitTrans.getCurrency();

        BigDecimal prorata = new BigDecimal(prorationRate).abs();
        System.out.println("prorata="+prorata);
        System.out.println("comm="+debitTrans.getPolicy().getCommAmt());
        System.out.println("whtx="+debitTrans.getPolicy().getWhtx());
        System.out.println("temp="+creditTrans.getTempSettleAmt());
        System.out.println("SettleAmt="+receiptSettlement);

        BigDecimal locAmt = allocAmt.add(debitTrans.getPolicy().getCommAmt().multiply(prorata)).
                add(debitTrans.getPolicy().getWhtx().multiply(prorata))
                .add(creditTrans.getTempSettleAmt()==null?BigDecimal.ZERO:creditTrans.getTempSettleAmt())
                .add(receiptSettlement)
                .abs().setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN);
        System.out.println(locAmt);
//        return allocAmt.add(debitTrans.getPolicy().getCommAmt().multiply(prorata)).
//                            add(debitTrans.getPolicy().getWhtx().multiply(prorata))
//                                .add(creditTrans.getTempSettleAmt()==null?BigDecimal.ZERO:creditTrans.getTempSettleAmt())
//                                .add(agentTrans.getSettleAmt()==null?BigDecimal.ZERO:agentTrans.getSettleAmt())
//                                .abs().setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN);
        return allocAmt.add(debitTrans.getPolicy().getCommAmt().multiply(prorata)).
                add(debitTrans.getPolicy().getWhtx().multiply(prorata))
                .add(creditTrans.getTempSettleAmt()==null?BigDecimal.ZERO:creditTrans.getTempSettleAmt())
                .add(receiptSettlement)
                .setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN);

    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getSumAuditAmt(Long allocId) throws BadRequestException {
        if(allocId==null) throw new BadRequestException("Trans Alloc Id Cannot be null");
        ReceiptSettlementDetails settlementDetails = settlementRepo.findOne(allocId);
        BigDecimal sumAudit = BigDecimal.ZERO;
        Iterable<PaymentAudit> audits = auditRepo.findAll(QPaymentAudit.paymentAudit.settlements.settlementId.eq(allocId));
        for(PaymentAudit audit:audits){
            sumAudit = sumAudit.add(audit.getPaymentAmount());
        }
        return sumAudit;
    }

    @Override
    public void consolidateCommissionPayments(ProcessingBean processingBean) throws BadRequestException {
        if(processingBean.getCredits().size()==0) throw new BadRequestException("No Credits to Process....");
        if(processingBean.getSubaccountType()==null) throw new BadRequestException("Select a Sub Account Type to Process ...");
        BigDecimal totalCredit = BigDecimal.ZERO;
        for(InsPaymentBean insPaymentBean:processingBean.getCredits()){
            totalCredit = totalCredit.add(insPaymentBean.getAmount());
        }
//        if(totalCredit.compareTo(BigDecimal.ZERO)==-1 || totalCredit.compareTo(BigDecimal.ZERO)==0)
//            throw new BadRequestException("Total Credits cannot be negative or zero");
        Iterable<SystemTransactions> existingTrans = null;
        if(processingBean.getAccountCode()!=null){
            AccountDef accountDef = accountRepo.findOne(processingBean.getAccountCode());
             existingTrans = transRepo.findAll(QSystemTransactions.systemTransactions.clientType.eq("I")
                    .and(QSystemTransactions.systemTransactions.transType.eq("PM"))
                    .and(QSystemTransactions.systemTransactions.controlAcc.eq(accountDef.getShtDesc()))
                    .and(QSystemTransactions.systemTransactions.agent.acctId.eq(accountDef.getAcctId()))
                    .and(QSystemTransactions.systemTransactions.narrations.eq("Sub Agent Payment"))
                    .and((QSystemTransactions.systemTransactions.authorised.eq("N")).or(QSystemTransactions.systemTransactions.authorised.isNull()))
                    .and(QSystemTransactions.systemTransactions.currency.curCode.eq(processingBean.getCurCode())));

            processCommissionTransactions(existingTrans,totalCredit,accountDef,currencyRepo.findOne(processingBean.getCurCode()),processingBean.getCredits(),"Sub Agent Payment");
        }
//        else {
//            Iterable<AccountDef> accountDefs = accountRepo.findAll(QAccountDef.accountDef.subAccountTypes.subAcctId.eq(processingBean.getSubaccountType()));
//            for(AccountDef accountDef:accountDefs){
//                List<InsPaymentBean> credits = processingBean.getCredits().stream().filter(a -> a.getAcctCode().intValue()==accountDef.getAcctId().intValue()).collect(Collectors.toList());
//                existingTrans = transRepo.findAll(QSystemTransactions.systemTransactions.clientType.eq("I")
//                        .and(QSystemTransactions.systemTransactions.transType.eq("PM"))
//                        .and(QSystemTransactions.systemTransactions.controlAcc.eq(accountDef.getShtDesc()))
//                        .and(QSystemTransactions.systemTransactions.agent.acctId.eq(accountDef.getAcctId()))
//                        .and(QSystemTransactions.systemTransactions.narrations.eq("Sub Agent Payment"))
//                        .and((QSystemTransactions.systemTransactions.authorised.eq("N")).or(QSystemTransactions.systemTransactions.authorised.isNull()))
//                        .and(QSystemTransactions.systemTransactions.currency.curCode.eq(processingBean.getCurCode())));
//                BigDecimal totCredit = BigDecimal.ZERO;
//                for(InsPaymentBean insPaymentBean:credits){
//                    totCredit = totCredit.add(insPaymentBean.getAmount());
//                }
//                if(totCredit.compareTo(BigDecimal.ZERO)!=0)
//                    processCommissionTransactions(existingTrans,totCredit,accountDef,currencyRepo.findOne(processingBean.getCurCode()),credits,"Sub Agent Payment");
//            }
//        }

    }

    @Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
    public void consolidatePayments(ProcessingBean processingBean) throws BadRequestException{
        if(processingBean.getCredits().size()==0) throw new BadRequestException("No Credits to Process....");
        if(processingBean.getAccountCode()==null) throw new BadRequestException("Select an Agent to Process ...");
        if(processingBean.getCurCode()==null)throw new BadRequestException("Select Currency to continue ...");
        BigDecimal totalCredit = BigDecimal.ZERO;
        for(InsPaymentBean insPaymentBean:processingBean.getCredits()){
            totalCredit = totalCredit.add(insPaymentBean.getAmount());
        }
        if(totalCredit.compareTo(BigDecimal.ZERO)==-1 || totalCredit.compareTo(BigDecimal.ZERO)==0)
            throw new BadRequestException("Total Credits cannot be negative or zero");
        AccountDef accountDef = accountRepo.findOne(processingBean.getAccountCode());
        Iterable<SystemTransactions> existingTrans = transRepo.findAll(QSystemTransactions.systemTransactions.clientType.eq("I")
                                                                        .and(QSystemTransactions.systemTransactions.transType.eq("PM"))
                                                                        .and(QSystemTransactions.systemTransactions.controlAcc.eq(accountDef.getShtDesc()))
                                                                        .and(QSystemTransactions.systemTransactions.agent.acctId.eq(accountDef.getAcctId()))
                                                                        .and(QSystemTransactions.systemTransactions.narrations.eq("Creditor Payment"))
                                                                        .and((QSystemTransactions.systemTransactions.authorised.eq("N")).or(QSystemTransactions.systemTransactions.authorised.isNull()))
                                                                        .and(QSystemTransactions.systemTransactions.currency.curCode.eq(processingBean.getCurCode())));
        processTransactions(existingTrans,totalCredit,accountDef,currencyRepo.findOne(processingBean.getCurCode()),processingBean.getCredits(),"Creditor Payment");


    }


    private void processCommissionTransactions(Iterable<SystemTransactions> existingTrans, BigDecimal totalCredit, AccountDef accountDef, Currencies currency, final List<InsPaymentBean> paymentBeanList, final String type) throws BadRequestException {
        if(existingTrans.spliterator().getExactSizeIfKnown() > 1) throw  new BadRequestException("There can be only one unauthorised transactions to be processed. Consult the admin..");
        if(existingTrans.spliterator().getExactSizeIfKnown()==0){
            Predicate adminPredicate = QSystemSequence.systemSequence.transType.eq("RM");
            if (sequenceRepo.count(adminPredicate) == 0)
                throw new AdminFeeException("Sequence for Remittance Transactions has not been defined");
            SystemSequence sequence = sequenceRepo.findOne(adminPredicate);
            Long seqNumber = sequence.getNextNumber();
            sequence.setLastNumber(seqNumber);
            sequence.setNextNumber(seqNumber + 1);
            sequenceRepo.save(sequence);
            SystemTrans transaction = new SystemTrans();
            transaction.setDoneDate(new Date());
            transaction.setDoneBy(userUtils.getCurrentUser());
            transaction.setTransLevel("U");
            transaction.setTransCode("PM"); //A way to setup and look up for transaction transcode
            transaction.setTransAuthorised("N");
            SystemTrans createdTrans = systransRepo.save(transaction);
            SystemTransactions trans = new SystemTransactions();
            trans.setAmount(totalCredit);
            trans.setBalance(totalCredit);
            trans.setClientType("I");
            trans.setAgent(accountDef);
            trans.setTransDate(new Date());
            trans.setRefNo(accountDef.getShtDesc()+"/"+String.format("%04d",seqNumber));
            trans.setTransType("PM");
            trans.setTransdc("D");
            trans.setControlAcc(accountDef.getShtDesc());
            trans.setCurrency(currencyRepo.findOne(currency.getCurCode()));
            trans.setCurrRate(BigDecimal.ONE);
            trans.setNarrations(type);
            trans.setAuthorised("N");
            trans.setPostedDate(new Date());
            trans.setPostedUser(userUtils.getCurrentUser());
            trans.setNetAmount(totalCredit);
            trans.setTransaction(createdTrans);
            trans.setPayeeName(accountDef.getName());
            trans.setOrigin("U");
            SystemTransactions savedTrans= transRepo.save(trans);
            Currencies currencies = savedTrans.getCurrency();
            List<PaymentAudit> audits = new ArrayList<>();
            List<SystemTransactions> receipts = new ArrayList<>();
            System.out.println(paymentBeanList);
            for(InsPaymentBean insPaymentBean:paymentBeanList){
                Iterable<PaymentAudit> paymentAudits =  auditRepo.findAll(QPaymentAudit.paymentAudit.settlements.debitRefNo.eq(insPaymentBean.getDebiTrans()).and(QPaymentAudit.paymentAudit.transType.eq("SAG")));
                if(paymentAudits.spliterator().getExactSizeIfKnown() > 1){
                    throw new BadRequestException("Unable to process. More than one settlement transaction generated... Contact Admin......");
                }
                PaymentAudit audit = null;

                if(paymentAudits.spliterator().getExactSizeIfKnown() ==0){
                    audit = new PaymentAudit();
                }
                else{
                    for(PaymentAudit paymentAudit:paymentAudits){
                        audit = paymentAudit;
                        break;
                    }
                }

                audit.setTransType("SAG");
                audit.setSettlements(null);
                audit.setDebitTransNo(insPaymentBean.getDebiTrans());
                audit.setReceiptTransNo(insPaymentBean.getCreditTrans());
//                audit.setCommAmount( (audit.getCommAmount()!=null)? audit.getCommAmount().add(sysTrans.getCommission().multiply(prorata).negate().setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN)):sysTrans.getCommission().multiply(prorata).negate().setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
                audit.setPaymentAmount((audit.getPaymentAmount()!=null)?audit.getPaymentAmount().add(insPaymentBean.getAmount().setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN)):insPaymentBean.getAmount().setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
                audit.setOtherTransNo(savedTrans);
//                audit.setTransNo(sysTrans);
                audit.setWhtxAmount( (BigDecimal.ZERO));
                audits.add(audit);
            }
            auditRepo.save(audits);
            transRepo.save(receipts);
        }
        else{
            SystemTransactions transactions = null;
            for (SystemTransactions trans:existingTrans){
                transactions = trans;
            }
            if(transactions==null) throw  new BadRequestException("Error getting commissions transaction to process. Processing consult System Admin");
            Currencies currencies = transactions.getCurrency();
            transactions.setAmount(transactions.getAmount().add(totalCredit));
            transactions.setBalance(transactions.getBalance().add(totalCredit));
            transactions.setNetAmount(transactions.getNetAmount().add(totalCredit));
            transRepo.save(transactions);
            List<PaymentAudit> audits = new ArrayList<>();
            List<SystemTransactions> receipts = new ArrayList<>();
            for(InsPaymentBean insPaymentBean:paymentBeanList){

                Iterable<PaymentAudit> paymentAudits =  auditRepo.findAll(QPaymentAudit.paymentAudit.settlements.debitRefNo.eq(insPaymentBean.getDebiTrans()).and(QPaymentAudit.paymentAudit.transType.eq("SAG")));
                if(paymentAudits.spliterator().getExactSizeIfKnown() > 1){
                    throw new BadRequestException("Unable to process. More than one settlement transaction generated... Contact Admin......");
                }
                PaymentAudit audit = null;

                if(paymentAudits.spliterator().getExactSizeIfKnown() ==0){
                    audit = new PaymentAudit();
                }
                else{
                    for(PaymentAudit paymentAudit:paymentAudits){
                        audit = paymentAudit;
                        break;
                    }
                }

                audit.setTransType("SAG");
                audit.setDebitTransNo(insPaymentBean.getDebiTrans());
                audit.setReceiptTransNo(insPaymentBean.getCreditTrans());
                audit.setPaymentAmount((audit.getPaymentAmount()!=null)?audit.getPaymentAmount().add(insPaymentBean.getAmount().setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN)):insPaymentBean.getAmount().setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
                audit.setOtherTransNo(transactions);
//                audit.setTransNo(sysTrans);
                audits.add(audit);
            }
            auditRepo.save(audits);
            transRepo.save(receipts);
        }

    }


        private void processTransactions(Iterable<SystemTransactions> existingTrans, BigDecimal totalCredit, AccountDef accountDef, Currencies currency, final List<InsPaymentBean> paymentBeanList, final String type) throws BadRequestException{
        if(existingTrans.spliterator().getExactSizeIfKnown() > 1) throw  new BadRequestException("There can be only one unauthorised transactions to be processed. Consult the admin..");
        if(existingTrans.spliterator().getExactSizeIfKnown()==0){
            Predicate adminPredicate = QSystemSequence.systemSequence.transType.eq("RM");
            if (sequenceRepo.count(adminPredicate) == 0)
                throw new AdminFeeException("Sequence for Remittance Transactions has not been defined");
            SystemSequence sequence = sequenceRepo.findOne(adminPredicate);
            Long seqNumber = sequence.getNextNumber();
            sequence.setLastNumber(seqNumber);
            sequence.setNextNumber(seqNumber + 1);
            sequenceRepo.save(sequence);
            SystemTrans transaction = new SystemTrans();
            transaction.setDoneDate(new Date());
            transaction.setDoneBy(userUtils.getCurrentUser());
            transaction.setTransLevel("U");
            transaction.setTransCode("PM"); //A way to setup and look up for transaction transcode
            transaction.setTransAuthorised("N");
            SystemTrans createdTrans = systransRepo.save(transaction);
            SystemTransactions trans = new SystemTransactions();
            trans.setAmount(totalCredit.abs());
            trans.setBalance(totalCredit.abs());
            trans.setClientType("I");
            trans.setAgent(accountDef);
            trans.setTransDate(new Date());
            trans.setRefNo(accountDef.getShtDesc()+"/"+String.format("%04d",seqNumber));
            trans.setTransType("PM");
            trans.setTransdc("D");
            trans.setControlAcc(accountDef.getShtDesc());
            trans.setCurrency(currencyRepo.findOne(currency.getCurCode()));
            trans.setCurrRate(BigDecimal.ONE);
            trans.setNarrations(type);
            trans.setAuthorised("N");
            trans.setPostedDate(new Date());
            trans.setPostedUser(userUtils.getCurrentUser());
            trans.setNetAmount(totalCredit.abs());
            trans.setTransaction(createdTrans);
            trans.setPayeeName(accountDef.getName());
            trans.setOrigin("U");
            SystemTransactions savedTrans= transRepo.save(trans);
            Currencies currencies = savedTrans.getCurrency();
            List<PaymentAudit> audits = new ArrayList<>();
            List<SystemTransactions> receipts = new ArrayList<>();
            for(InsPaymentBean insPaymentBean:paymentBeanList){
//                ReceiptSettlementDetails settlementDetails = settlementRepo.findOne(insPaymentBean.getSettlementId());
//                if(insPaymentBean.getAmount().setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN).compareTo(settlementDetails.getAllocatedAmt())==1){
//                    throw  new BadRequestException("Entered Allocation Amount: "+insPaymentBean.getAmount()+" ;cannot be greater than expected allocated amount: "+settlementDetails.getAllocatedAmt());
//                }
                Iterable<PaymentAudit> prevAudits = auditRepo.findAll(QPaymentAudit.paymentAudit.settlements.debitRefNo.eq(insPaymentBean.getDebiTrans()).and(QPaymentAudit.paymentAudit.debitTransNo.eq(insPaymentBean.getCreditTrans())));
                BigDecimal prevSettledAmt = BigDecimal.ZERO;
                for(PaymentAudit paymentAudit:prevAudits){
                    prevSettledAmt = prevSettledAmt.add(paymentAudit.getPaymentAmount());
                }
//                SystemTransactions agentTrans = transRepo.findOne(QSystemTransactions.systemTransactions.refNo.eq(insPaymentBean.getDebiTrans())
//                        .and(QSystemTransactions.systemTransactions.clientType.eq("A").and(QSystemTransactions.systemTransactions.transType.in("NBD","APD","RND")))
//                        .and(QSystemTransactions.systemTransactions.transdc.eq("C")));
                SystemTransactions agentTrans=null;
                if (transRepo.count(QSystemTransactions.systemTransactions.refNo.eq(insPaymentBean.getDebiTrans())
                        .and(QSystemTransactions.systemTransactions.transType.in("APC")))==0)
                {
                    agentTrans = transRepo.findOne(QSystemTransactions.systemTransactions.refNo.eq(insPaymentBean.getDebiTrans())
                            .and(QSystemTransactions.systemTransactions.clientType.eq("A").and(QSystemTransactions.systemTransactions.transType.in("NBD", "APD", "RND", "APC")))
                            .and(QSystemTransactions.systemTransactions.transdc.eq("C"))
                    );
                }else {
                    agentTrans = transRepo.findOne(QSystemTransactions.systemTransactions.refNo.eq(insPaymentBean.getDebiTrans())
                            .and(QSystemTransactions.systemTransactions.clientType.eq("A").and(QSystemTransactions.systemTransactions.transType.in("APC")))
                            .and(QSystemTransactions.systemTransactions.transdc.eq("D"))
                    );
                }
                SystemTransactions creditTrans =null;
                ReceiptSettlementDetails settlements = null;
                long counts = settlementRepo.count(QReceiptSettlementDetails.receiptSettlementDetails.debit.refNo.eq(insPaymentBean.getDebiTrans()).and(QReceiptSettlementDetails.receiptSettlementDetails.credit.refNo.eq(insPaymentBean.getCreditTrans())
                        .and(QReceiptSettlementDetails.receiptSettlementDetails.credit.transType.eq("RC"))));
                if(counts==1) {
                    settlements = settlementRepo.findOne(QReceiptSettlementDetails.receiptSettlementDetails.debit.refNo.eq(insPaymentBean.getDebiTrans()).and(QReceiptSettlementDetails.receiptSettlementDetails.credit.refNo.eq(insPaymentBean.getCreditTrans())
                            .and(QReceiptSettlementDetails.receiptSettlementDetails.credit.transType.eq("RC"))));
                }
                if(transRepo.count(QSystemTransactions.systemTransactions.refNo.eq(insPaymentBean.getCreditTrans()).and(QSystemTransactions.systemTransactions.transType.eq("RC"))
                        .and(QSystemTransactions.systemTransactions.transdc.eq("C")))==1)
                    creditTrans = transRepo.findOne(QSystemTransactions.systemTransactions.refNo.eq(insPaymentBean.getCreditTrans()).and(QSystemTransactions.systemTransactions.transType.eq("RC"))
                            .and(QSystemTransactions.systemTransactions.transdc.eq("C")));
                else {
                    long count = settlementRepo.count(QReceiptSettlementDetails.receiptSettlementDetails.debit.refNo.eq(insPaymentBean.getDebiTrans()).and(QReceiptSettlementDetails.receiptSettlementDetails.credit.refNo.eq(insPaymentBean.getCreditTrans())
                            .and(QReceiptSettlementDetails.receiptSettlementDetails.credit.transType.eq("RC"))));
                    if(count==1) {
                        creditTrans = settlementRepo.findOne(QReceiptSettlementDetails.receiptSettlementDetails.debit.refNo.eq(insPaymentBean.getDebiTrans()).and(QReceiptSettlementDetails.receiptSettlementDetails.credit.refNo.eq(insPaymentBean.getCreditTrans())
                                .and(QReceiptSettlementDetails.receiptSettlementDetails.credit.transType.eq("RC")))).getCredit();

                    }
                }
                if(!"CLB".equalsIgnoreCase(insPaymentBean.getTransType())) {
                    if (creditTrans == null)
                        throw new BadRequestException("Unable to get Receipt Transaction to allocate...");
                }
                double prorationRate = insPaymentBean.getAmount().doubleValue()/(agentTrans.getNetAmount().abs().doubleValue());
                SystemTransactions sysTrans = transRepo.findOne(agentTrans.getTransno());
                BigDecimal prorata = new BigDecimal(prorationRate);
                Iterable<PaymentAudit> paymentAudits =  auditRepo.findAll(QPaymentAudit.paymentAudit.settlements.debitRefNo.eq(insPaymentBean.getDebiTrans()).and(QPaymentAudit.paymentAudit.debitTransNo.eq(insPaymentBean.getCreditTrans())));
                if(paymentAudits.spliterator().getExactSizeIfKnown() > 1){
                    throw new BadRequestException("Unable to process. More than one settlement transaction generated... Contact Admin......");
                }
                PaymentAudit audit = null;

                if(paymentAudits.spliterator().getExactSizeIfKnown() ==0){
                    audit = new PaymentAudit();
                }
                else{
                    for(PaymentAudit paymentAudit:paymentAudits){
                        audit = paymentAudit;
                        break;
                    }
                }
                if("CLB".equalsIgnoreCase(insPaymentBean.getTransType())) {
                    audit.setTransType("CLB");
                }
                else{
                    audit.setTransType("NML");
                }
                audit.setSettlements(settlements);
                audit.setDebitTransNo(insPaymentBean.getDebiTrans());
                audit.setReceiptTransNo(insPaymentBean.getCreditTrans());
                audit.setCommAmount( (audit.getCommAmount()!=null)? audit.getCommAmount().add(sysTrans.getCommission().multiply(prorata).negate().setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN)):sysTrans.getCommission().multiply(prorata).negate().setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
                audit.setPaymentAmount((audit.getPaymentAmount()!=null)?audit.getPaymentAmount().add(insPaymentBean.getAmount().setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN)):insPaymentBean.getAmount().setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
                audit.setOtherTransNo(savedTrans);
                audit.setTransNo(sysTrans);
                audit.setWhtxAmount( (audit.getWhtxAmount()!=null)?audit.getWhtxAmount().add(sysTrans.getWhtx().multiply(prorata).negate().setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN)):sysTrans.getWhtx().multiply(prorata).negate().setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
                audits.add(audit);
//                audits.add(audit);
                if(!"CLB".equalsIgnoreCase(insPaymentBean.getTransType())) {
                    creditTrans.setTempSettleAmt((creditTrans.getTempSettleAmt() == null) ? insPaymentBean.getAmount() : creditTrans.getTempSettleAmt().add(insPaymentBean.getAmount()).setScale(currencies.getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
                    receipts.add(creditTrans);
                }
            }
            auditRepo.save(audits);
            transRepo.save(receipts);
        }
        else{
            SystemTransactions transactions = null;
            for (SystemTransactions trans:existingTrans){
                transactions = trans;
            }
            if(transactions==null) throw  new BadRequestException("Error getting insurance transaction to process. Processing consult System Admin");
            Currencies currencies = transactions.getCurrency();
            transactions.setAmount(transactions.getAmount().add(totalCredit.abs()));
            transactions.setBalance(transactions.getBalance().add(totalCredit.abs()));
            transactions.setNetAmount(transactions.getNetAmount().add(totalCredit.abs()));
            transRepo.save(transactions);
            List<PaymentAudit> audits = new ArrayList<>();
            List<SystemTransactions> receipts = new ArrayList<>();
            for(InsPaymentBean insPaymentBean:paymentBeanList){
                Iterable<PaymentAudit> prevAudits = auditRepo.findAll(QPaymentAudit.paymentAudit.settlements.debitRefNo.eq(insPaymentBean.getDebiTrans()).and(QPaymentAudit.paymentAudit.debitTransNo.eq(insPaymentBean.getCreditTrans())));
                BigDecimal prevSettledAmt = BigDecimal.ZERO;
                for(PaymentAudit paymentAudit:prevAudits){
                    prevSettledAmt = prevSettledAmt.add(paymentAudit.getPaymentAmount());
                }
                SystemTransactions sysTrans = transRepo.findOne(QSystemTransactions.systemTransactions.refNo.eq(insPaymentBean.getDebiTrans())
                        .and(QSystemTransactions.systemTransactions.clientType.eq("A").and(QSystemTransactions.systemTransactions.transType.in("NBD","APD")))
                        .and(QSystemTransactions.systemTransactions.transdc.eq("C")));
                double prorationRate = insPaymentBean.getAmount().doubleValue()/(sysTrans.getNetAmount().abs().doubleValue());
                BigDecimal prorata = new BigDecimal(prorationRate);
                SystemTransactions creditTrans =null;
                ReceiptSettlementDetails settlements = null;
                long counts = settlementRepo.count(QReceiptSettlementDetails.receiptSettlementDetails.debit.refNo.eq(insPaymentBean.getDebiTrans()).and(QReceiptSettlementDetails.receiptSettlementDetails.credit.refNo.eq(insPaymentBean.getCreditTrans())
                        .and(QReceiptSettlementDetails.receiptSettlementDetails.credit.transType.eq("RC"))));
                if(counts==1) {
                    settlements = settlementRepo.findOne(QReceiptSettlementDetails.receiptSettlementDetails.debit.refNo.eq(insPaymentBean.getDebiTrans()).and(QReceiptSettlementDetails.receiptSettlementDetails.credit.refNo.eq(insPaymentBean.getCreditTrans())
                            .and(QReceiptSettlementDetails.receiptSettlementDetails.credit.transType.eq("RC"))));
                    System.out.println("Settlement found...." + settlements);
                }
                if(transRepo.count(QSystemTransactions.systemTransactions.refNo.eq(insPaymentBean.getCreditTrans()).and(QSystemTransactions.systemTransactions.transType.eq("RC"))
                        .and(QSystemTransactions.systemTransactions.transdc.eq("C")))==1)
                    creditTrans = transRepo.findOne(QSystemTransactions.systemTransactions.refNo.eq(insPaymentBean.getCreditTrans()).and(QSystemTransactions.systemTransactions.transType.eq("RC"))
                            .and(QSystemTransactions.systemTransactions.transdc.eq("C")));
                else
                    creditTrans = settlementRepo.findOne(QReceiptSettlementDetails.receiptSettlementDetails.debit.refNo.eq(insPaymentBean.getDebiTrans()).and(QReceiptSettlementDetails.receiptSettlementDetails.credit.refNo.eq(insPaymentBean.getCreditTrans())
                            .and(QReceiptSettlementDetails.receiptSettlementDetails.credit.transType.eq("RC")))).getCredit();

                if(creditTrans==null)
                    throw new BadRequestException("Unable to get Receipt Transaction to allocate...");

                Iterable<PaymentAudit> paymentAudits =auditRepo.findAll(QPaymentAudit.paymentAudit.settlements.debitRefNo.eq(insPaymentBean.getDebiTrans()).and(QPaymentAudit.paymentAudit.debitTransNo.eq(insPaymentBean.getCreditTrans())));
                if(paymentAudits.spliterator().getExactSizeIfKnown() > 1){
                    throw new BadRequestException("Unable to process. More than one settlement transaction generated... Contact Admin......");
                }
                PaymentAudit audit = null;

                if(paymentAudits.spliterator().getExactSizeIfKnown() ==0){
                    audit = new PaymentAudit();
                }
                else{
                    for(PaymentAudit paymentAudit:paymentAudits){
                        audit = paymentAudit;
                        break;
                    }
                }
                audit.setDebitTransNo(insPaymentBean.getDebiTrans());
                audit.setReceiptTransNo(insPaymentBean.getCreditTrans());
                audit.setCommAmount( (audit.getCommAmount()!=null)? audit.getCommAmount().add(sysTrans.getCommission().multiply(prorata).negate().setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN)):sysTrans.getCommission().multiply(prorata).negate().setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
                audit.setPaymentAmount((audit.getPaymentAmount()!=null)?audit.getPaymentAmount().add(insPaymentBean.getAmount().setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN)):insPaymentBean.getAmount().setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
                audit.setOtherTransNo(transactions);
                audit.setTransNo(sysTrans);
                audit.setWhtxAmount( (audit.getWhtxAmount()!=null)?audit.getWhtxAmount().add(sysTrans.getWhtx().multiply(prorata).negate().setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN)):sysTrans.getWhtx().multiply(prorata).negate().setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
                audit.setSettlements(settlements);
                audits.add(audit);
                creditTrans.setTempSettleAmt((creditTrans.getTempSettleAmt()==null)?insPaymentBean.getAmount(): creditTrans.getTempSettleAmt().add(insPaymentBean.getAmount()).setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
                receipts.add(creditTrans);
            }
            auditRepo.save(audits);
            transRepo.save(receipts);
        }
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
    public void deleteAudit(Long auditId) throws BadRequestException {
        if(auditId==null) throw new BadRequestException("Transaction to delete not found.....");
        PaymentAudit audit = auditRepo.findOne(auditId);

        SystemTransactions creditTrans =null;
        if("NML".equalsIgnoreCase(audit.getTransType())) {
            if (transRepo.count(QSystemTransactions.systemTransactions.refNo.eq(audit.getReceiptTransNo()).and(QSystemTransactions.systemTransactions.transType.in("RC","APC"))
                    .and(QSystemTransactions.systemTransactions.transdc.eq("C"))) == 1)
                creditTrans = transRepo.findOne(QSystemTransactions.systemTransactions.refNo.eq(audit.getReceiptTransNo()).and(QSystemTransactions.systemTransactions.transType.in("RC","APC"))
                        .and(QSystemTransactions.systemTransactions.transdc.eq("C")));
            else
                creditTrans = settlementRepo.findOne(QReceiptSettlementDetails.receiptSettlementDetails.debit.refNo.eq(audit.getDebitTransNo()).and(QReceiptSettlementDetails.receiptSettlementDetails.credit.refNo.eq(audit.getReceiptTransNo())
                        .and(QReceiptSettlementDetails.receiptSettlementDetails.credit.transType.in("RC","APC")))).getCredit();

            if (creditTrans == null)
                throw new BadRequestException("Unable to get Receipt Transaction to allocate...");
        }
        SystemTransactions transactions = audit.getOtherTransNo();
        if(transactions.getAuthorised()!=null){
            if("Y".equalsIgnoreCase(transactions.getAuthorised())){
                throw new BadRequestException("Cannot Undo..The Transaction is already authorized...");
            }
        }

        transactions.setBalance(transactions.getBalance().subtract(audit.getPaymentAmount()));
        transactions.setAmount(transactions.getAmount().subtract(audit.getPaymentAmount()));
        transactions.setNetAmount(transactions.getNetAmount().subtract(audit.getPaymentAmount()));
        if("NML".equalsIgnoreCase(audit.getTransType())) {
            creditTrans.setTempSettleAmt(creditTrans.getTempSettleAmt().subtract(audit.getPaymentAmount()));
            transRepo.save(creditTrans);
        }
        SystemTransactions trans = transRepo.save(transactions);
        auditRepo.delete(auditId);

        if(trans.getBalance().compareTo(BigDecimal.ZERO)==0){
            transRepo.delete(trans.getTransno());
        }
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
    public void cancelPaymentTrans(Long transNo) throws BadRequestException {
        if(transNo==null) throw new BadRequestException("Transaction to Cancel not found.....");
        SystemTransactions trans = transRepo.findOne(transNo);
        if(trans.getAuthorised()!=null){
            if("Y".equalsIgnoreCase(trans.getAuthorised())){
                throw new BadRequestException("Cannot Cancel..The Transaction is already authorized...");
            }
        }
        Iterable<PaymentAudit> audits = auditRepo.findAll(QPaymentAudit.paymentAudit.otherTransNo.transno.eq(transNo));
        List<SystemTransactions> creditTransactions = new ArrayList<>();

        for(PaymentAudit paymentAudit:audits){
            if("NML".equalsIgnoreCase(paymentAudit.getTransType())) {
                SystemTransactions creditTrans = null;
                if (transRepo.count(QSystemTransactions.systemTransactions.refNo.eq(paymentAudit.getReceiptTransNo()).and(QSystemTransactions.systemTransactions.transType.in("RC","APC"))
                        .and(QSystemTransactions.systemTransactions.transdc.eq("C"))) == 1)
                    creditTrans = transRepo.findOne(QSystemTransactions.systemTransactions.refNo.eq(paymentAudit.getReceiptTransNo()).and(QSystemTransactions.systemTransactions.transType.in("RC","APC"))
                            .and(QSystemTransactions.systemTransactions.transdc.eq("C")));
                else
                    creditTrans = settlementRepo.findOne(QReceiptSettlementDetails.receiptSettlementDetails.debit.refNo.eq(paymentAudit.getDebitTransNo()).and(QReceiptSettlementDetails.receiptSettlementDetails.credit.refNo.eq(paymentAudit.getReceiptTransNo())
                            .and(QReceiptSettlementDetails.receiptSettlementDetails.credit.transType.in("RC","APC")))).getCredit();

                if (creditTrans == null)
                    throw new BadRequestException("Unable to get Receipt Transaction to allocate...");
                creditTrans.setTempSettleAmt(BigDecimal.ZERO);
                creditTransactions.add(creditTrans);
            }
        }
        auditRepo.delete(audits);
        if(creditTransactions.size()>0)
        transRepo.save(creditTransactions);
        transRepo.delete(trans);
    }

//    @Override
//    @Transactional(readOnly = true)
//    public DataTablesResult<SystemTransactions> findAuthorizeTrans(DataTablesRequest request) throws IllegalAccessException {
//        BooleanExpression pred = (QSystemTransactions.systemTransactions.authorised.isNull().or(QSystemTransactions.systemTransactions.authorised.eq("N")))
//                .and(QSystemTransactions.systemTransactions.transType.notIn("RFC","RFD"));
//        Page<SystemTransactions> page = transRepo.findAll(pred.and(request.searchPredicate(QSystemTransactions.systemTransactions)), request);
//        return new DataTablesResult<>(request, page);
//    }

    @Override
    @Transactional(readOnly = true)
    public DataTablesResult<SystemTransDTO> findAuthorizeTrans(DataTablesRequest request)  {
        List<Object[]> authList = transRepo.findAuthTransactions(request.getPageNumber(), request.getPageSize());
        List<SystemTransDTO> transList = new ArrayList<>();
        long rowCount = 0L;
        if(!authList.isEmpty()) rowCount = (Integer)authList.get(0)[15];
        for(Object[] trans:authList){
            SystemTransDTO transDTO =  SystemTransDTO.instance( ((BigDecimal)trans[0]).longValue(),
                    null, (Date) trans[1],
                    (String) trans[2], (String) trans[3],
                    (String) trans[4], (String) trans[5], (String) trans[6], (String) trans[7], (String) trans[8], (String) trans[9], (BigDecimal) trans[10], (BigDecimal) trans[11],
                    (BigDecimal) trans[12],(String) trans[13], (String) trans[14]);
            transList.add(transDTO);

        }
        Page<SystemTransDTO> page = new PageImpl<>(transList, request, rowCount);
        return new DataTablesResult(request, page);
    }

    @PreAuthorize("hasAnyAuthority('AUTHORIZE_ACCOUNT_TRANS')")
    @Override
    @Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
    public void authAccountTrans(Long transId) throws BadRequestException {
        if (transId == null) throw new BadRequestException("No Transaction to Authorize");
        Iterable<PaymentAudit> audits = auditRepo.findAll(QPaymentAudit.paymentAudit.otherTransNo.transno.eq(transId));
        List<SystemTransactions> transactions = new ArrayList<>();
        BigDecimal amount = BigDecimal.ZERO;
            for (PaymentAudit audit : audits) {
                audit.setPostDate(new Date());
                audit.setPosted("Y");
                audit.setPostedBy(userUtils.getCurrentUser());
                if ("NML".equalsIgnoreCase(audit.getTransType())) {
                    SystemTransactions credit = audit.getTransNo();
                    credit.setBalance(credit.getBalance().add(audit.getPaymentAmount().abs()));
                    if (credit.getSettleAmt() == null)
                        credit.setSettleAmt(audit.getPaymentAmount());
                    else
                        credit.setSettleAmt(credit.getSettleAmt().add(audit.getPaymentAmount()));
                    amount = amount.add(audit.getPaymentAmount());
                    SystemTransactions creditTrans = null;
                    if (transRepo.count(QSystemTransactions.systemTransactions.refNo.eq(audit.getReceiptTransNo()).and(QSystemTransactions.systemTransactions.transType.in("RC", "APC"))
                            .and(QSystemTransactions.systemTransactions.transdc.eq("C"))) == 1)
                        creditTrans = transRepo.findOne(QSystemTransactions.systemTransactions.refNo.eq(audit.getReceiptTransNo()).and(QSystemTransactions.systemTransactions.transType.in("RC", "APC"))
                                .and(QSystemTransactions.systemTransactions.transdc.eq("C")));
                    else
                        creditTrans = settlementRepo.findOne(QReceiptSettlementDetails.receiptSettlementDetails.debit.refNo.eq(audit.getDebitTransNo()).and(QReceiptSettlementDetails.receiptSettlementDetails.credit.refNo.eq(audit.getReceiptTransNo())
                                .and(QReceiptSettlementDetails.receiptSettlementDetails.credit.transType.in("RC", "APC")))).getCredit();

                    if (creditTrans == null)
                        throw new BadRequestException("Unable to get Receipt Transaction to allocate...");
//            SystemTransactions creditTrans = transRepo.findOne(QSystemTransactions.systemTransactions.refNo.eq(audit.getReceiptTransNo()).and(QSystemTransactions.systemTransactions.transType.eq("RC"))
//                    .and(QSystemTransactions.systemTransactions.transdc.eq("C")));
                    creditTrans.setTempSettleAmt(BigDecimal.ZERO);
                    transactions.add(credit);
                    transactions.add(creditTrans);
                } else if ("CLB".equalsIgnoreCase(audit.getTransType())) {
                    long count = transRepo.count(QSystemTransactions.systemTransactions.refNo.eq(audit.getDebitTransNo()).and(QSystemTransactions.systemTransactions.clientType.eq("A"))
                            .and(QSystemTransactions.systemTransactions.transdc.eq("D"))
                            .and(QSystemTransactions.systemTransactions.transType.eq("AGR")));
                    if (count == 1) {
                        SystemTransactions crTrans = transRepo.findOne(QSystemTransactions.systemTransactions.refNo.eq(audit.getDebitTransNo()).and(QSystemTransactions.systemTransactions.clientType.eq("A"))
                                .and(QSystemTransactions.systemTransactions.transdc.eq("D"))
                                .and(QSystemTransactions.systemTransactions.transType.eq("AGR")));
                        crTrans.setBalance(crTrans.getBalance().subtract(audit.getPaymentAmount().abs()));
                        crTrans.setSettleAmt(audit.getPaymentAmount().abs());
                        transactions.add(crTrans);
                    } else
                        throw new BadRequestException("Error getting Agent Transaction to allocate...Contact System Admin");
                }
            }
            if (!authLimits.checkAuthorizationLimits("AUTHORIZE_ACCOUNT_TRANS", amount)) {
                throw new BadRequestException("You have no rights to authorize the transaction...Check your authorization limits..");
            }
            auditRepo.save(audits);
            SystemTransactions trans = transRepo.findOne(transId);
            trans.setAuthorised("Y");
            trans.setAuthDate(new Date());
            trans.setUserAuth(userUtils.getCurrentUser().getName());
            transactions.add(trans);
            transRepo.save(transactions);
    }

    @Override
    @Transactional(readOnly = true)
    public DataTablesResult<SystemTransactions> findCreditTrans(DataTablesRequest request, Long clientCode, Long curCode,Long brnCode,String allTrans) throws IllegalAccessException {
        if(allTrans==null)
            allTrans="A";
        if(clientCode==null ){
            clientCode = -2000l;
        }
        QClientDef clientDef = QSystemTransactions.systemTransactions.client;
        QCurrencies currencies = QSystemTransactions.systemTransactions.currency;
        QOrgBranch branch = QSystemTransactions.systemTransactions.branch;
        BooleanExpression pred = null;
        if("Y".equalsIgnoreCase(allTrans)){
            pred = QSystemTransactions.systemTransactions.transdc.eq("C")
                    .and(QSystemTransactions.systemTransactions.transType.notIn("SAG","RFC"))
                    .and(QSystemTransactions.systemTransactions.balance.ne(BigDecimal.ZERO))
                    .and((clientCode == null) ? clientDef.isNotNull() : clientDef.tenId.eq(clientCode))
                    .and((curCode == null) ? currencies.isNotNull() : currencies.curCode.eq(curCode))
                    .and((brnCode == null) ? branch.isNotNull() : branch.obId.eq(brnCode));
        }
        else {
             pred = QSystemTransactions.systemTransactions.transdc.eq("C")
                     .and(QSystemTransactions.systemTransactions.transType.notIn("SAG","RFC"))
                    .and(QSystemTransactions.systemTransactions.isNotNull())
                     .and((clientCode == null) ? clientDef.isNotNull() : clientDef.tenId.eq(clientCode))
                    .and((curCode == null) ? currencies.isNotNull() : currencies.curCode.eq(curCode))
                     .and((brnCode == null) ? branch.isNotNull() : branch.obId.eq(brnCode));
        }
        Iterable<SystemTransactions> credits = transRepo.findAll(pred.and(request.searchPredicate(QSystemTransactions.systemTransactions)), request);
        List<SystemTransactions> newCredits =new ArrayList<>();
        for (SystemTransactions cr:credits){
            if(transRepo.count(QSystemTransactions.systemTransactions.refundTransaction.transno.eq(cr.getTransno())
            .and(QSystemTransactions.systemTransactions.refundTransaction.authorised.ne("R")))==0){
                newCredits.add(cr);
            }
        }

        Page<SystemTransactions> page = new PageImpl<SystemTransactions>(newCredits);
        return new DataTablesResult(request, page);
    }

    @Override
    public DataTablesResult<ReceiptTransDtls> findPolicyCreditTrans(DataTablesRequest request, Long policyId) throws IllegalAccessException {
        Long pol = (policyId!=null)?policyId:-2000;
        Page<ReceiptTransDtls> page = receiptDetailsRepository.findAll(QReceiptTransDtls.receiptTransDtls.transactionsTemp.isNotNull()
                                                 .and(QReceiptTransDtls.receiptTransDtls.transactionsTemp.policy.policyId.eq(pol)),request);
        return new DataTablesResult(request, page);
    }

    @Override
    @Transactional(readOnly = true)
    public DataTablesResult<SystemTransactions> findDebitTrans(DataTablesRequest request, Long clientCode, Long curCode, Long brnCode, String allTrans) throws IllegalAccessException {
        if(allTrans==null)
            allTrans="A";
        if(clientCode==null ){
            clientCode = -2000l;
        }
        QClientDef clientDef = QSystemTransactions.systemTransactions.client;
        QCurrencies currencies = QSystemTransactions.systemTransactions.currency;
        QOrgBranch branch = QSystemTransactions.systemTransactions.branch;
        BooleanExpression pred = QSystemTransactions.systemTransactions.transdc.eq("D")
                .and(QSystemTransactions.systemTransactions.transType.notIn("SAG","RFC"))
                .and((allTrans.equalsIgnoreCase("N"))?QSystemTransactions.systemTransactions.isNotNull():QSystemTransactions.systemTransactions.balance.ne(BigDecimal.ZERO))
                .and((clientCode==null)?clientDef.isNotNull():clientDef.tenId.eq(clientCode))
                .and((curCode==null)?currencies.isNotNull():currencies.curCode.eq(curCode))
                .and((brnCode==null)?branch.isNotNull():branch.obId.eq(brnCode));
        Page<SystemTransactions> page = transRepo.findAll(pred.and(request.searchPredicate(QSystemTransactions.systemTransactions)), request);
        return new DataTablesResult(request, page);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getAllocateAmount(Long transno) throws BadRequestException {
        if(transno==null) throw new BadRequestException("Trans Alloc Id Cannot be null");
        SystemTransactions transactions = transRepo.findOne(transno);
        return transactions.getBalance();
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
    public void allocateCreditTrans(Long crTransNo, Long drTransNo, BigDecimal allocAmount) throws BadRequestException {
        if(crTransNo==null) throw new BadRequestException("Credit Transaction not found...");
        if(drTransNo==null) throw new BadRequestException("Debit Transaction not found");
        if(allocAmount==null || allocAmount.compareTo(BigDecimal.ZERO)==0) throw  new BadRequestException("Allocation Amount cannot be zero");
        SystemTransactions crTrans = transRepo.findOne(crTransNo);
        SystemTransactions drTrans = transRepo.findOne(drTransNo);
        if(crTrans.getBalance()==null || crTrans.getBalance().compareTo(BigDecimal.ZERO)==0){
            throw new BadRequestException("Credit Transaction Balance is  fully depleted. Select another Credit Transaction");
        }
        if(drTrans.getBalance()==null || drTrans.getBalance().compareTo(BigDecimal.ZERO)==0){
            throw new BadRequestException("Debit Transaction Selected is fully allocated....");
        }
        if(allocAmount.compareTo(drTrans.getBalance().abs())==1){
            throw new BadRequestException("Allocation Amount cannot be greater than Debit Balance.....");
        }

        if(allocAmount.compareTo(crTrans.getBalance().abs()) ==1){
            throw new BadRequestException("Allocation Amount cannot be greater than credit amount");
        }
        drTrans.setBalance(drTrans.getBalance().subtract(allocAmount.abs()));
        if(drTrans.getSettleAmt()==null)
            drTrans.setSettleAmt(allocAmount.abs());
        else
            drTrans.setSettleAmt(drTrans.getSettleAmt().add(allocAmount.abs()));

        if(crTrans.getSettleAmt()==null){
            crTrans.setSettleAmt(allocAmount.abs().negate());
        }
        else{
            crTrans.setSettleAmt(crTrans.getSettleAmt().add(allocAmount.abs().negate()));
        }
        crTrans.setBalance(crTrans.getBalance().subtract(allocAmount.abs().negate()));
        if ("L".equalsIgnoreCase(drTrans.getPolicy().getProduct().getProGroup().getPrgType())){
            allocationService.createLifeSettlements(drTrans, crTrans, allocAmount, userUtils.getCurrentUser());
        }else {
            allocationService.createSettlements(drTrans, crTrans, allocAmount, userUtils.getCurrentUser());
        }
        transRepo.save(drTrans);
        transRepo.save(crTrans);
        if(crTrans.getTransType()!=null && "RC".equalsIgnoreCase(crTrans.getTransType())) {
            if (drTrans.getBalance().compareTo(BigDecimal.ZERO) == 0)
                if (drTrans.getPolicy().getSubAgentComm() != null && drTrans.getPolicy().getSubAgentComm().compareTo(BigDecimal.ZERO) != 0) {
                    SystemTrans transaction = new SystemTrans();
                    transaction.setDoneDate(new Date());
                    transaction.setDoneBy(userUtils.getCurrentUser());
                    transaction.setTransLevel("U");
                    transaction.setTransCode("CR"); //A way to setup and look up for transaction transcode
                    transaction.setTransAuthorised("N");
                    systransRepo.save(transaction);
                    PolicyTrans policy = drTrans.getTransaction().getPolicy();
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
                    trans.setUserAuth(userUtils.getCurrentUser().getUsername());
                    trans.setWhtx(BigDecimal.ZERO);
                    trans.setTransaction(transaction);
                    trans.setPostedDate(new Date());
                    trans.setPostedUser(userUtils.getCurrentUser());
                    trans.setAgent(policy.getSubAgent());
                    transRepo.save(trans);
                }
        }
        else if(crTrans.getTransType()!=null && !"RC".equalsIgnoreCase(crTrans.getTransType())) {
            if (crTrans.getBalance().compareTo(BigDecimal.ZERO) == 0){
                if (crTrans.getPolicy()!=null && crTrans.getPolicy().getSubAgentComm() != null && crTrans.getPolicy().getSubAgentComm().compareTo(BigDecimal.ZERO) != 0) {
                    SystemTrans transaction = new SystemTrans();
                    transaction.setDoneDate(new Date());
                    transaction.setDoneBy(userUtils.getCurrentUser());
                    transaction.setTransLevel("U");
                    transaction.setTransCode("CR"); //A way to setup and look up for transaction transcode
                    transaction.setTransAuthorised("N");
                    systransRepo.save(transaction);
                    PolicyTrans policy = crTrans.getPolicy();
                    SystemTransactions trans = new SystemTransactions();
                    trans.setAmount(policy.getSubAgentComm().abs().multiply(sign("D")));
                    trans.setAuthDate(new Date());
                    trans.setAuthorised("Y");
                    trans.setBalance(policy.getSubAgentComm().abs().multiply(sign("D")));
                    trans.setBranch(policy.getBranch());
                    trans.setClientType("C");
                    trans.setControlAcc(policy.getSubAgent().getShtDesc());
                    trans.setClient(policy.getClient());
                    trans.setCurrRate(new BigDecimal(1));
                    trans.setCurrency(policy.getTransCurrency());
                    trans.setNarrations("Sub Agent Commission Trans");
                    trans.setNetAmount(policy.getSubAgentComm().abs().multiply(sign("D")));
                    trans.setOrigin("U");
                    trans.setPolicy(policy);
                    trans.setRefNo(policy.getRefNo());
                    trans.setTransDate(new Date());
                    trans.setTransdc("D");
                    trans.setTransType("SAG"); //Should not be hardcorded
                    trans.setUserAuth(userUtils.getCurrentUser().getUsername());
                    trans.setWhtx(BigDecimal.ZERO);
                    trans.setTransaction(transaction);
                    trans.setPostedDate(new Date());
                    trans.setPostedUser(userUtils.getCurrentUser());
                    trans.setAgent(policy.getSubAgent());
                    transRepo.save(trans);
                }
            }
        }
    }

    private BigDecimal sign(String type){
        return ("C".equalsIgnoreCase(type)?BigDecimal.ONE.multiply(BigDecimal.valueOf(-1)):BigDecimal.ONE.multiply(BigDecimal.valueOf(1)));
    }



    @Override
    @Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
    public void allocateCreditTransRfnd(Long crTransNo, Long drTransNo, BigDecimal allocAmount) throws BadRequestException {
        if(crTransNo==null) throw new BadRequestException("Credit Transaction not found...");
        if(drTransNo==null) throw new BadRequestException("Debit Transaction not found");
        if(allocAmount==null || allocAmount.compareTo(BigDecimal.ZERO)==0) throw  new BadRequestException("Allocation Amount cannot be zero");
        SystemTransactions crTrans = transRepo.findOne(crTransNo);
        SystemTransactions drTrans = transRepo.findOne(drTransNo);
        if(crTrans.getBalance()==null || crTrans.getBalance().compareTo(BigDecimal.ZERO)==0){
            throw new BadRequestException("Credit Transaction Balance is  fully depleted. Select another Credit Transaction");
        }
        if(drTrans.getBalance()==null || drTrans.getBalance().compareTo(BigDecimal.ZERO)==0){
            throw new BadRequestException("Debit Transaction Selected is fully allocated....");
        }
        if(allocAmount.compareTo(drTrans.getBalance().abs())==1){
            throw new BadRequestException("Allocation Amount cannot be greater than Debit Balance.....");
        }

        if(allocAmount.compareTo(crTrans.getBalance().abs()) ==1){
            throw new BadRequestException("Allocation Amount cannot be greater than credit amount");
        }
        drTrans.setBalance(drTrans.getBalance().subtract(allocAmount.abs()));
        if(drTrans.getSettleAmt()==null)
            drTrans.setSettleAmt(allocAmount.abs());
        else
            drTrans.setSettleAmt(drTrans.getSettleAmt().add(allocAmount.abs()));

        if(crTrans.getSettleAmt()==null){
            crTrans.setSettleAmt(allocAmount.abs().negate());
        }
        else{
            crTrans.setSettleAmt(crTrans.getSettleAmt().add(allocAmount.abs().negate()));
        }
        crTrans.setBalance(crTrans.getBalance().subtract(allocAmount.abs().negate()));
        allocationService.createClientSettlements(drTrans,crTrans,allocAmount,userUtils.getCurrentUser());
        transRepo.save(drTrans);
        transRepo.save(crTrans);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
    public void defineCoa(CoaMainAccounts mainAccount) throws BadRequestException {
        if(!mainAccount.getAccountsOrder().matches("\\d+")){
          throw new BadRequestException("Account Order must match a number...");
        }
        if(mainAccount.getCoId()==null)
        if(mainAccountsRepo.count(QCoaMainAccounts.coaMainAccounts.code.eq(StringUtils.trim(mainAccount.getCode()))) > 0){
            throw new BadRequestException("Account Code already exist...");
        }
        mainAccountsRepo.save(mainAccount);
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteCoa(Long coId) {
       mainAccountsRepo.delete(coId);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
    public void defineSubCoa(CoaSubAccounts subAccount) throws BadRequestException {
       if(subAccount.getMainAccounts().getCoId()==null){
           throw new BadRequestException("Main Account cannot be null");
       }
        if(!subAccount.getAccountsOrder().matches("\\d+")){
            throw new BadRequestException("Account Order must match a number...");
        }
        if(subAccount.getCoId()==null)
        if(subAccountsRepo.count(QCoaSubAccounts.coaSubAccounts.code.eq(StringUtils.trim(subAccount.getCode()))) > 0){
            throw new BadRequestException("Account Code already exist...");
        }
        subAccountsRepo.save(subAccount);
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteSubCoa(Long subCoaId) {
        subAccountsRepo.delete(subCoaId);
    }



    @Override
    public DataTablesResult<Banks> findBanks(DataTablesRequest request) throws IllegalAccessException {
        Page<Banks> page = banksRepo.findAll(request.searchPredicate(QBanks.banks), request);
        return new DataTablesResult<>(request, page);
    }

    @Override
    public DataTablesResult<BankBranches> findBankBranches(DataTablesRequest request, Long bnId) throws IllegalAccessException {
        if(bnId==null) bnId=-2000l;
        BooleanExpression pred = QBankBranches.bankBranches.bank.bnId.eq(bnId);
        Page<BankBranches> page = bankBranchRepo.findAll(pred.and(request.searchPredicate(QBankBranches.bankBranches)), request);
        return new DataTablesResult<>(request, page);
    }

    @Override
    @Transactional(readOnly = false)
    public void defineBank(Banks bank) throws BadRequestException {
         banksRepo.save(bank);
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteBank(Long bnId) {
        banksRepo.delete(bnId);
    }

    @Override
    @Transactional(readOnly = false)
    public void defineBankBranch(BankBranches bankBranch) throws BadRequestException {
           bankBranchRepo.save(bankBranch);
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteBankBranch(Long bbId) {
       bankBranchRepo.delete(bbId);
    }

    @Override
    public DataTablesResult<CollectionAccounts> findCollectionAccts(DataTablesRequest request, Long pmId) throws IllegalAccessException {
        if(pmId==null) pmId=-2000l;
        BooleanExpression pred = QCollectionAccounts.collectionAccounts.paymentModes.pmId.eq(pmId);
        Page<CollectionAccounts> page = acctsRepo.findAll(pred.and(request.searchPredicate(QCollectionAccounts.collectionAccounts)), request);
        return new DataTablesResult<>(request, page);
    }

    @Override
    public void defineCollectAccount(CollectionAccounts collectionAccounts) throws BadRequestException {
        acctsRepo.save(collectionAccounts);
    }

    @Override
    public void deleteCollectAcct(Long caId) {
        acctsRepo.delete(caId);
    }

    @Override
    public Page<PaymentModes> findPaymentModes(String paramString, Pageable paramPageable) {
        Predicate pred=null;
        if (paramString == null || StringUtils.isBlank(paramString)) {
            pred = QPaymentModes.paymentModes.isNotNull();
        } else {
            pred = QPaymentModes.paymentModes.pmDesc.containsIgnoreCase(paramString).or(QPaymentModes.paymentModes.pmShtDesc.containsIgnoreCase(paramString));
        }
        return paymentModeRepo.findAll(pred, paramPageable);
    }

    @Override
    public Page<BankBranchDTO> findBankBranches(String paramString, Pageable paramPageable) {
        final String search = (paramString!=null)?"%"+paramString+"%":"%%";
        List<Object[]> branches = bankBranchRepo.findBnkBranches(search.toLowerCase(),paramPageable.getPageNumber(), paramPageable.getPageSize());
        final List<BankBranchDTO> branchDTOList = new ArrayList<>();
        long rowCount = 0l;
        if(!branches.isEmpty()) rowCount = (Integer)branches.get(0)[2];
        for(Object[] branch:branches){
            BankBranchDTO branchDTO = new BankBranchDTO();
            branchDTO.setBbId(((BigDecimal)branch[0]).longValue());
            branchDTO.setBranchName((String)branch[1]);
            branchDTOList.add(branchDTO);
        }
        return new PageImpl<>(branchDTOList,paramPageable,rowCount);
    }

    @Override
    public Page<AccountingPeriodDTO> findAccountingPeriods(String paramString, Pageable paramPageable) {
        final String search = (paramString!=null)?"%"+paramString+"%":"%%";
        final Long headOfficeId = branchRepository.findHeadOffice();
        List<Object[]> periods = balancesRepo.searchAccountingPeriods(search.toLowerCase(),headOfficeId,paramPageable.getPageNumber(), paramPageable.getPageSize());
        final List<AccountingPeriodDTO> acountingPeriods = new ArrayList<>();
        long rowCount = 0l;
        if(!periods.isEmpty()) rowCount = (Integer)periods.get(0)[1];
        for(Object[] period:periods){
            final AccountingPeriodDTO accountingPeriod = new AccountingPeriodDTO();
            accountingPeriod.setPeriodName((String) period[0]);
            acountingPeriods.add(accountingPeriod);
        }
        return new PageImpl<>(acountingPeriods,paramPageable,rowCount);
    }

    @Override
    public DataTablesResult<ReceiptSettlementDetails> findCreditSettlements(DataTablesRequest request, Long crCode) throws IllegalAccessException {
        if(crCode==null) crCode=-2000l;
        BooleanExpression pred = QReceiptSettlementDetails.receiptSettlementDetails.credit.transno.eq(crCode);
        Page<ReceiptSettlementDetails> page = settlementRepo.findAll(pred.and(request.searchPredicate(QReceiptSettlementDetails.receiptSettlementDetails)), request);
        return new DataTablesResult<>(request, page);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PolicyTrans> findClientPolicies(String paramString, Pageable paramPageable, Long clientId) {
        Predicate pred = null;
        if (paramString == null || StringUtils.isBlank(paramString)) {
            pred = QPolicyTrans.policyTrans.client.tenId.eq(clientId).and(QPolicyTrans.policyTrans.isNotNull())
                    .and(QPolicyTrans.policyTrans.authStatus.equalsIgnoreCase("A"));
        } else {
            pred =QPolicyTrans.policyTrans.client.tenId.eq(clientId).and(QPolicyTrans.policyTrans.polNo.containsIgnoreCase(paramString)
                    .or(QPolicyTrans.policyTrans.clientPolNo.containsIgnoreCase(paramString)))
                    .and(QPolicyTrans.policyTrans.authStatus.equalsIgnoreCase("A"));
        }
        return policyRepo.findAll(pred, paramPageable);
    }
    @Override
    @Transactional(readOnly = true)
    public Page<SystemTransactions> findPolicyTrans(String paramString, Pageable paramPageable, Long policyId) {
        Predicate pred = null;
        if (paramString == null || StringUtils.isBlank(paramString)) {
            pred =QSystemTransactions.systemTransactions.policy.policyId.eq(policyId)
                    .and(QSystemTransactions.systemTransactions.isNotNull()
                    .and(QSystemTransactions.systemTransactions.transType.notIn("RFC","SAG"))
                            .and(QSystemTransactions.systemTransactions.clientType.equalsIgnoreCase("C"))
                    .and(QSystemTransactions.systemTransactions.transdc.equalsIgnoreCase("C"))
                            .and(QSystemTransactions.systemTransactions.balance.lt(BigDecimal.ZERO)));
        } else {
            pred =QSystemTransactions.systemTransactions.policy.policyId.eq(policyId)
                    .and(QSystemTransactions.systemTransactions.refNo.containsIgnoreCase(paramString)
                    .or(QSystemTransactions.systemTransactions.otherRef.containsIgnoreCase(paramString))
                            .and(QSystemTransactions.systemTransactions.clientType.equalsIgnoreCase("C"))
                            .and(QSystemTransactions.systemTransactions.transType.ne("RFC"))
                            .and(QSystemTransactions.systemTransactions.transdc.equalsIgnoreCase("C"))
                    .and(QSystemTransactions.systemTransactions.balance.lt(BigDecimal.ZERO)));
        }
        return transRepo.findAll(pred, paramPageable);
    }

    @PreAuthorize("hasAnyAuthority('CREATE_REFUND')")
    @Override
    @Modifying
    @Transactional(readOnly = false, rollbackFor = { BadRequestException.class }, propagation = Propagation.REQUIRED)
    public void createRefund(Refunds refund , String refundStatus) throws BadRequestException {
        Long creditTransNo =null;
        if (refund.getTransactions()==null && refund.getTransNoToRefund()==null){
            throw new BadRequestException("Select credit note/receipt");
        }else {
            if (refund.getRefId()==null) {
                if (refund.getTransactions()!=null){
                    creditTransNo = refund.getTransactions().getRefundTransaction().getTransno();
                }
                if (refund.getTransNoToRefund()!=null){
                    creditTransNo = refund.getTransNoToRefund();
                }
                System.out.println("Trano = "+creditTransNo);
                if (refundRepo.count(QRefunds.refunds.transactions.refundTransaction.transno.eq(creditTransNo)
                        .and(QRefunds.refunds.refundStatus.eq("D"))) > 0) {
                    throw new BadRequestException("There is a pending refund on the same credit note/Receipt");
                }
            }
        }
        if (refund.getAmount()==null){
            throw new BadRequestException("Amount is mandatory");
        }
        if (refund.getClient()==null){
            throw new BadRequestException("Client is mandatory");
        }
        if (refund.getNarrations()==null){
            throw new BadRequestException("Narration is mandatory");
        }
        if (refund.getPayee()==null){
            throw new BadRequestException("Payee is mandatory");
        }

        if (refund.getPolicy()==null){
            throw new BadRequestException("Select policy");
        }

        if (refund.getRefId()==null){
            refund.setRefundCaptureDate(new Date());
            refund.setRefundStatus(refundStatus);
            refund.setCreatedUser(userUtils.getCurrentUser());
            if (refund.getTransactions()==null && refund.getTransNoToRefund()!=null){

                PolicyTrans policy = refund.getPolicy();
                BigDecimal prems = (policy.getPremium()==null)?BigDecimal.ZERO:policy.getPremium();
                if(mappingRepo.count(QTransactionMapping.transactionMapping.transType.eq("RF")) ==0)
                    throw new BadRequestException("Error getting Transaction Mapping Setups For Refund..Contact System Administrator");
                TransactionMapping mapping = mappingRepo.findOne(QTransactionMapping.transactionMapping.transType.eq("RF"));
                Predicate refSeqPredicate = QSystemSequence.systemSequence.transType.eq("RFD");
                if (sequenceRepo.count(refSeqPredicate) == 0)
                    throw new BadRequestException("Sequence for refund has not been defined");
                SystemSequence refundSequence = sequenceRepo.findOne(refSeqPredicate);
                Long refSeqNumber = refundSequence.getNextNumber();
                final String refundCode = ((prems.compareTo(BigDecimal.ZERO)>=0)?mapping.getDebitCode():mapping.getCreditCode());
                final String refundRefNo =((prems.compareTo(BigDecimal.ZERO)>=0)?mapping.getDebitCode():mapping.getCreditCode()) + String.format("%05d", refSeqNumber);

                refundSequence.setLastNumber(refSeqNumber);
                refundSequence.setNextNumber(refSeqNumber + 1);
                sequenceRepo.save(refundSequence);
                SystemTrans transaction = new SystemTrans();
                transaction.setDoneDate(new Date());
                transaction.setDoneBy(userUtils.getCurrentUser());
                transaction.setPolicy(policy);
                transaction.setTransLevel("U");
                transaction.setTransCode("RFD");
                transaction.setTransAuthorised("N");
                SystemTrans savedTrans =systransRepo.save(transaction);
                BigDecimal basicPrem = (policy.getPremium()==null)?BigDecimal.ZERO:policy.getPremium();
                BigDecimal extras = (policy.getExtras()==null)?BigDecimal.ZERO:policy.getExtras();
                BigDecimal phcf = (policy.getPhcf()==null)?BigDecimal.ZERO:policy.getPhcf();
                BigDecimal tl = (policy.getTrainingLevy()==null)?BigDecimal.ZERO:policy.getTrainingLevy();
                BigDecimal sd = (policy.getStampDuty()==null)?BigDecimal.ZERO:policy.getStampDuty();
                BigDecimal amountWithTaxes =basicPrem.add(extras).add(phcf).add(tl).add(sd);
                String type = (amountWithTaxes.compareTo(BigDecimal.ZERO)==1)?"D":"C";
                BigDecimal refundAmount =refund.getAmount();
                // client
                SystemTransactions trans1 = new SystemTransactions();
                trans1.setAmount(refundAmount.abs().multiply(sign(type)).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
                trans1.setAuthDate(new Date());
                trans1.setAuthorised("N");
                trans1.setBalance(refundAmount.abs().multiply(sign(type)).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
                trans1.setBranch(policy.getBranch());
                trans1.setClientType("C");
                trans1.setControlAcc(policy.getClient().getTenantNumber());
                trans1.setClient(policy.getClient());
                trans1.setCurrRate(new BigDecimal(1));
                trans1.setCurrency(policy.getTransCurrency());
                trans1.setNarrations("Posting client underwriting refund");
                trans1.setNetAmount(refundAmount.abs().multiply(sign(type)).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
                trans1.setOrigin("U");
                trans1.setPolicy(policy);
                trans1.setRefNo(refundRefNo);
                trans1.setTransDate(new Date());
                trans1.setTransdc((amountWithTaxes.compareTo(BigDecimal.ZERO)==1)?"C":"D");
                trans1.setTransType(refundCode); //Should not be hardcorded
                //trans.setUserAuth(userUtils.getCurrentUser().getUsername());
                trans1.setWhtx(BigDecimal.ZERO);
                //trans.setExtras(extras.abs().multiply(sign(type)).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
                // trans.setPostedDate(new Date());
                //trans.setPostedUser(userUtils.getCurrentUser());
                trans1.setTransaction(savedTrans);
                trans1.setRefundTransaction(transRepo.findOne(QSystemTransactions.systemTransactions.transno.eq(refund.getTransNoToRefund())));
                SystemTransactions savedRefundTrans =  transRepo.save(trans1);
                refund.setTransactions(savedRefundTrans);
            }
        }else {
            Refunds existingRefund = refundRepo.findOne(refund.getRefId());
            refund.setRefundCaptureDate(existingRefund.getRefundCaptureDate());
            refund.setRefundStatus(existingRefund.getRefundStatus());
            refund.setCreatedUser(existingRefund.getCreatedUser());
            refund.setTransactions(existingRefund.getTransactions());
        }

        refundRepo.save(refund);
    }


    @PreAuthorize("hasAnyAuthority('MAKE_REFUND_READY')")
    @Override
    @Modifying
    @Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
    public void makeRefundReady(Long refundId) throws BadRequestException {
        Refunds refund = refundRepo.findOne(QRefunds.refunds.refId.eq(refundId));
        if (refund.getAmount()==null){
            throw new BadRequestException("Amount is mandatory");
        }
        if (refund.getClient()==null){
            throw new BadRequestException("Client is mandatory");
        }
        if (refund.getNarrations()==null){
            throw new BadRequestException("Narration is mandatory");
        }
        if (refund.getPayee()==null){
            throw new BadRequestException("Payee is mandatory");
        }
        if (refund.getPaymentMode()==null){
            throw new BadRequestException("Pay mode is mandatory");
        }
        if (refund.getPolicy()==null){
            throw new BadRequestException("Select policy");
        }
        if (refund.getTransactions()==null){
            throw new BadRequestException("Select credit note/receipt");
        }

        if (!refund.getRefundStatus().equalsIgnoreCase("D")){
            throw new BadRequestException("Can only make ready a draft transaction");
        }
        refund.setRefundStatus("R");
        refund.setMakeReadyDate(new Date());
        refund.setMadeReadyBy(userUtils.getCurrentUser());
        refundRepo.save(refund);
    }


    @PreAuthorize("hasAnyAuthority('AUTHORIZE_REFUND')")
    @Override
    @Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
    public void    rejectRefund(Long refundId, String remarks) throws BadRequestException {
        System.out.println("Refund Id="+refundId);
        Refunds refund = refundRepo.findOne(QRefunds.refunds.refId.eq(refundId));
        if(refund==null) throw new BadRequestException("No refund transaction to reject");
        if (remarks==null){
            throw new BadRequestException("Please provide rejection remarks");

        }
        SystemTransactions refundTran = transRepo.findOne(refund.getTransactions().getTransno()) ;
        refundTran.setAuthorised("R");
        refundTran.setUserAuth(userUtils.getCurrentUser().getUsername());
        refundTran.setPostedDate(new Date());
        refundTran.setPostedUser(userUtils.getCurrentUser());

        SystemTrans transaction = refundTran.getTransaction();
        transaction.setAuthBy(userUtils.getCurrentUser());
        transaction.setAuthDate(new Date());
        transaction.setTransAuthorised("R");
        systransRepo.save(transaction);

        transRepo.save(refundTran);
        refund.setRefundStatus("J");
        refund.setRejectionRemarks(remarks);
        refund.setRejectedDate(new Date());
        refund.setRejectedBy(userUtils.getCurrentUser());
        refundRepo.save(refund);
    }


    @PreAuthorize("hasAnyAuthority('AUTHORIZE_REFUND')")
    @Override
    @Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
    public void    authorizeRefund(Long refundId, String accoutno) throws BadRequestException {
         Refunds refund = refundRepo.findOne(QRefunds.refunds.refId.eq(refundId));
        if(refund==null) throw new BadRequestException("No refund transaction to Authorize");
        SystemTransactions refundTran = transRepo.findOne(refund.getTransactions().getTransno()) ;
        refundTran.setAuthorised("Y");
        refundTran.setUserAuth(userUtils.getCurrentUser().getUsername());
        refundTran.setPostedDate(new Date());
        refundTran.setPostedUser(userUtils.getCurrentUser());
        transRepo.save(refundTran);

        SystemTrans transaction = refundTran.getTransaction();
        transaction.setAuthBy(userUtils.getCurrentUser());
        transaction.setAuthDate(new Date());
        transaction.setTransAuthorised("Y");
        systransRepo.save(transaction);
        SystemTransactions creditTran = transRepo.findOne(refund.getTransactions().getRefundTransaction().getTransno());
            // creditTran.setBalance();
        allocateCreditTransRfnd(creditTran.getTransno(), refundTran.getTransno(), refundTran.getAmount());

        refund.setRefundStatus("A");
        refund.setRefundAuthDate(new Date());
        refund.setAuthBy(userUtils.getCurrentUser());
        refundRepo.save(refund);


    }



    @Override
    public DataTablesResult<Refunds> findRefundTrans(DataTablesRequest request, Date from, Date to,String refNo,String status,String policyNo,Long clientId) throws IllegalAccessException {
        QRefunds refund = QRefunds.refunds;
        to = dateUtilities.removeTime(DateUtils.addDays(to, 1));
        System.out.println("to="+to);
         BooleanExpression pred =
                QRefunds.refunds.refundCaptureDate.between(dateUtilities.removeTime(from),dateUtilities.removeTime(to))
        .and((status==null || StringUtils.isEmpty(status))?refund.isNotNull():refund.refundStatus.eq(status))
                .and((policyNo==null|| StringUtils.isEmpty(policyNo))?refund.isNotNull():refund.policy.polNo.eq(policyNo))
                .and((refNo==null || StringUtils.isEmpty(refNo))?refund.isNotNull():refund.transactions.refNo.containsIgnoreCase(refNo))
        .and((clientId==null)?refund.isNotNull():refund.client.tenId.eq(clientId));
        Page<Refunds> page = refundRepo.findAll(pred.and(request.searchPredicate(QRefunds.refunds)), request);
        return new DataTablesResult(request, page);
    }

    @Override
    public Page<AccountDef> findSubAgentAccounts(String paramString, Pageable paramPageable) {
        Predicate pred = null;
        if (paramString == null || StringUtils.isBlank(paramString)) {
            pred = QAccountDef.accountDef.isNotNull().and(QAccountDef.accountDef.accountType.accountType.eq(AccountTypeEnum.IA));
        } else {
            pred =  QAccountDef.accountDef.name.containsIgnoreCase(paramString).and(QAccountDef.accountDef.accountType.accountType.eq(AccountTypeEnum.IA));
        }
        return accountRepo.findAll(pred, paramPageable);
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteRefund(Long refId) throws BadRequestException {
        Refunds refund = refundRepo.findOne(refId);
        if ("RFD".equalsIgnoreCase(refund.getTransactions().getTransaction().getTransCode()) && "N".equalsIgnoreCase(refund.getTransactions().getTransaction().getTransAuthorised())){
            systransRepo.delete(refund.getTransactions().getTransaction().getTransNo());
            transRepo.delete(refund.getTransactions().getTransno());
            refundRepo.delete(refId);
        }else {
            throw new BadRequestException("Cannot delete this refund, Reject it instead");
        }


    }


    @Override
    public Page<SystemTransactions> findInsuranceReceipts(String paramString,Long accountId, Pageable paramPageable) {
        Predicate pred=null;
        AccountDef agent = accountRepo.findByAcctId(accountId);
        if(agent!=null){
            if (paramString == null || StringUtils.isBlank(paramString)) {
                pred =QSystemTransactions.systemTransactions.isNotNull().and(QSystemTransactions.systemTransactions.agent.eq(agent)).and(QSystemTransactions.systemTransactions.clientType.eq("A"))
                        .and(QSystemTransactions.systemTransactions.balance.ne(BigDecimal.ZERO).and(QSystemTransactions.systemTransactions.transType.eq("RC")).and(QSystemTransactions.systemTransactions.balance.abs().gt(BigDecimal.ZERO))
                                .and(QSystemTransactions.systemTransactions.transdc.eq("C")));
            } else {
                pred = QSystemTransactions.systemTransactions.isNotNull().and(QSystemTransactions.systemTransactions.agent.eq(agent)).and(QSystemTransactions.systemTransactions.clientType.eq("A"))
                        .and(QSystemTransactions.systemTransactions.balance.ne(BigDecimal.ZERO)).and(QSystemTransactions.systemTransactions.transType.eq("RC"))
                        .and(QSystemTransactions.systemTransactions.transdc.eq("C")).and(QSystemTransactions.systemTransactions.refNo.contains(paramString)).and(QSystemTransactions.systemTransactions.balance.abs().gt(BigDecimal.ZERO))
                        .and(QSystemTransactions.systemTransactions.agent.name.contains(paramString));
            }
        }
        return transRepo.findAll(pred, paramPageable);
    }

    @Override
    public DataTablesResult<FinalReportFormatDTO> findFinalReportFormats(String type,DataTablesRequest request) {
        List<FinalReportFormatDTO> reportFormatDTOList = new ArrayList<>();
        List<Object[]> reportFormats = finalReportFormatsRepo.searchAllReportFormats(type,(request.getSearch()!=null && request.getSearch().getValue()!=null)?"%"+request.getSearch().getValue()+"%":"%%"
                ,request.getPageNumber(), request.getPageSize());
        long rowCount = 0L;
        if(!reportFormats.isEmpty()) rowCount = (Integer)reportFormats.get(0)[11];
        for(Object[] format:reportFormats) {
            FinalReportFormatDTO reportFormatDTO = new FinalReportFormatDTO();
            reportFormatDTO.setRowCode((String) format[0]);
            reportFormatDTO.setDescription((String) format[1]);
            reportFormatDTO.setDetailFormat((String) format[2]);
            reportFormatDTO.setSummaryFormat((String) format[3]);
            reportFormatDTO.setType((String) format[4]);
            reportFormatDTO.setOrder((Integer) format[5]);
            reportFormatDTO.setPickedFrom((String) format[6]);
            reportFormatDTO.setAssetLiability((String) format[7]);
            if(format[8]!=null)
            reportFormatDTO.setAssetLiabilitySign((Integer) format[8]);
            reportFormatDTO.setAllocation(((BigDecimal) format[9]));
            reportFormatDTO.setRfId(((BigDecimal) format[10]).longValue());
            reportFormatDTOList.add(reportFormatDTO);
        }
        Page<FinalReportFormatDTO>  page = new PageImpl<>(reportFormatDTOList,request, rowCount);
        return new DataTablesResult<>(request, page);
    }

    @Override
    public DataTablesResult<FinalReportFormatTotalDTO> findFinalReportFormatsTotals(String type, DataTablesRequest request) {
        List<FinalReportFormatTotalDTO> reportFormatDTOList = new ArrayList<>();
        List<Object[]> reportFormats = reportFormatTotalsRepo.searchAllReportTotals(type,(request.getSearch()!=null && request.getSearch().getValue()!=null)?"%"+request.getSearch().getValue()+"%":"%%"
                ,request.getPageNumber(), request.getPageSize());
        long rowCount = 0L;
        if(!reportFormats.isEmpty()) rowCount = (Integer)reportFormats.get(0)[4];
        for(Object[] format:reportFormats) {
            final FinalReportFormatTotalDTO formatTotalDTO = new FinalReportFormatTotalDTO();
            formatTotalDTO.setRftId(((BigDecimal) format[0]).longValue());
            formatTotalDTO.setSign((Boolean) format[1]);
            formatTotalDTO.setColumn((String) format[2]);
            formatTotalDTO.setTotal((String) format[3]);
            reportFormatDTOList.add(formatTotalDTO);
        }
        Page<FinalReportFormatTotalDTO>  page = new PageImpl<>(reportFormatDTOList,request, rowCount);
        return new DataTablesResult<>(request, page);
    }

    @Override
    public DataTablesResult<FinalReportFormatAcctsDTO> findFinalReportFormatsGroupAccts(Long formatId, DataTablesRequest request) {
        List<FinalReportFormatAcctsDTO> reportFormatDTOList = new ArrayList<>();
        List<Object[]> reportFormatAccounts = finalReportFormatGroupAccountsRepo.searchAllReportFormatsGroupAccts(formatId,(request.getSearch()!=null && request.getSearch().getValue()!=null)?"%"+request.getSearch().getValue()+"%":"%%"
                ,request.getPageNumber(), request.getPageSize());
        long rowCount = 0L;
        if(!reportFormatAccounts.isEmpty()) rowCount = (Integer)reportFormatAccounts.get(0)[3];
        for(Object[] format:reportFormatAccounts) {
            FinalReportFormatAcctsDTO reportFormatAcctsDTO = new FinalReportFormatAcctsDTO();
            reportFormatAcctsDTO.setRfaId(((BigDecimal) format[0]).longValue());
            reportFormatAcctsDTO.setAccountNo((String) format[1]);
            reportFormatAcctsDTO.setSign((Boolean) format[2]);
            reportFormatDTOList.add(reportFormatAcctsDTO);
        }
        Page<FinalReportFormatAcctsDTO>  page = new PageImpl<>(reportFormatDTOList,request, rowCount);
        return new DataTablesResult<>(request, page);
    }

    @Override
    public DataTablesResult<FinalReportFormatAcctsDTO> findFinalReportFormatsAccts(Long formatId, DataTablesRequest request) {
        List<FinalReportFormatAcctsDTO> reportFormatDTOList = new ArrayList<>();
        List<Object[]> reportFormatAccounts = finalReportFormatAccountsRepo.searchAllReportFormatsAccts(formatId,(request.getSearch()!=null && request.getSearch().getValue()!=null)?"%"+request.getSearch().getValue()+"%":"%%"
                ,request.getPageNumber(), request.getPageSize());
        long rowCount = 0L;
        if(!reportFormatAccounts.isEmpty()) rowCount = (Integer)reportFormatAccounts.get(0)[4];
        for(Object[] format:reportFormatAccounts) {
            FinalReportFormatAcctsDTO reportFormatAcctsDTO = new FinalReportFormatAcctsDTO();
            reportFormatAcctsDTO.setRfaId(((BigDecimal) format[0]).longValue());
            reportFormatAcctsDTO.setAccountNo((String) format[1]);
            reportFormatAcctsDTO.setSign((Boolean) format[2]);
            reportFormatAcctsDTO.setAccountName((String) format[3]);
            reportFormatDTOList.add(reportFormatAcctsDTO);
        }
        Page<FinalReportFormatAcctsDTO>  page = new PageImpl<>(reportFormatDTOList,request, rowCount);
        return new DataTablesResult<>(request, page);
    }

    @Override
    public DataTablesResult<PayeesDTO> findAllPayees(DataTablesRequest request) {
        List<Object[]> payeesList = payeesRepo.searchAllPayes((request.getSearch()!=null && request.getSearch().getValue()!=null)?"%"+request.getSearch().getValue()+"%":"%%"
                                                                ,request.getPageNumber(), request.getPageSize());
        List<PayeesDTO> payeesDTOList = new ArrayList<>();
        long rowCount = 0L;
        if(!payeesList.isEmpty()) rowCount = (Integer)payeesList.get(0)[8];
        for(Object[] payee:payeesList) {
            PayeesDTO payeesDTO = new PayeesDTO();
            payeesDTO.setPayId(((BigDecimal) payee[0]).longValue());
            payeesDTO.setCreatedDate((Date)payee[1]);
            payeesDTO.setEmail((String) payee[2]);
            payeesDTO.setFullName((String) payee[3]);
            payeesDTO.setMobileNo((String) payee[4]);
            payeesDTO.setTelNo((String) payee[5]);
            payeesDTO.setCreatedUser((String) payee[6]);
            payeesDTO.setStatus((String) payee[7]);
            payeesDTOList.add(payeesDTO);
        }
        Page<PayeesDTO>  page = new PageImpl<>(payeesDTOList,request, rowCount);
        return new DataTablesResult<>(request, page);
    }

    @Override
    public DataTablesResult<PayeeAccountsDTO> findAllPayeesAccounts(Long payeeId,DataTablesRequest request) {
        List<Object[]> payeesAccountsList = payeeAccountsRepo.searchAllPayesAccounts(payeeId,
                (request.getSearch()!=null && request.getSearch().getValue()!=null)?"%"+request.getSearch().getValue()+"%":"%%"
                ,request.getPageNumber(), request.getPageSize());
        List<PayeeAccountsDTO> payeesDTOList = new ArrayList<>();
        long rowCount = 0L;
        if(!payeesAccountsList.isEmpty()) rowCount = (Integer)payeesAccountsList.get(0)[6];
        for(Object[] payee:payeesAccountsList) {
            PayeeAccountsDTO payeesDTO = new PayeeAccountsDTO();
            payeesDTO.setPaycId(((BigDecimal) payee[0]).longValue());
            payeesDTO.setAccountNo((String) payee[1]);
            payeesDTO.setBankBranch((String) payee[2]);
            payeesDTO.setBankBranchId(((BigDecimal) payee[3]).longValue());
            payeesDTO.setBank((String) payee[4]);
            payeesDTO.setStatus((String) payee[5]);
            payeesDTOList.add(payeesDTO);
        }
        Page<PayeeAccountsDTO>  page = new PageImpl<>(payeesDTOList,request, rowCount);
        return new DataTablesResult<>(request, page);
    }

    private Date constructDate(Long start, Long year, String month){
        try {
            if(start==null)
            return new SimpleDateFormat("dd/MMMM/yyyy").parse("01/"+month+"/"+year);
            else
                return new SimpleDateFormat("dd/MMMM/yyyy").parse(start+"/"+month+"/"+year);
        } catch (ParseException e) {
            return null;
        }
    }

    @Override
    public DataTablesResult<OpeningBalanceDTO> findAllOpeningBalances(DataTablesRequest request) throws BadRequestException {
        Long currentYear = Long.parseLong(new SimpleDateFormat("yyyy").format(new Date()));
        final Long headOfficeId = branchRepository.findHeadOffice();
        if(headOfficeId==null){
            throw new BadRequestException("Unable to get Head Office. Cannot continue...");
        }
        String firstPeriod = accountYearPeriodsRepo.firstPeriodOfYr(headOfficeId,currentYear);
        String lastPeriod = accountYearPeriodsRepo.lastPeriodOfYr(headOfficeId,currentYear);
        Date wefDate = constructDate(null,currentYear,firstPeriod);
        if(wefDate==null){
            throw new BadRequestException("Unable to get Accounting Start Period of the current year....");
        }
        Date lastDate = constructDate(null,currentYear,lastPeriod);
        if(lastDate==null){
            throw new BadRequestException("Unable to get Accounting End Period of the current year....");
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(lastDate);
        int last  = cal.getActualMaximum(Calendar.DATE);
        Date wetDate = constructDate((long) last,currentYear,lastPeriod);
        List<Object[]> openingBalancesList = balancesRepo.queryAcctYearOpeningBalances(null,currentYear,wefDate,wetDate ,request.getPageNumber(), request.getPageSize());
        List<OpeningBalanceDTO> balanceDTOList = new ArrayList<>();
        long rowCount = 0L;
        if(!openingBalancesList.isEmpty()) rowCount = (Integer)openingBalancesList.get(0)[5];
        for(Object[] bal:openingBalancesList) {
            final OpeningBalanceDTO balanceDTO = new OpeningBalanceDTO();
            balanceDTO.setAccountName(balancesRepo.getAccountName((String)bal[4]));
            balanceDTO.setAccountNo((String)bal[4]);
            balanceDTO.setBalance((BigDecimal) bal[0]);
//            balanceDTO.setCrBalance((BigDecimal) bal[2]);
//            balanceDTO.setDrBalance((BigDecimal) bal[1]);
            balanceDTO.setCurrBalance((BigDecimal) bal[3]);
            balanceDTOList.add(balanceDTO);
        }
        Page<OpeningBalanceDTO>  page = new PageImpl<>(balanceDTOList,request, rowCount);
        return new DataTablesResult<>(request, page);
    }

    @Override
    @Transactional
    public void createPayee(PayeesDTO payeesDTO) throws BadRequestException {
        if(payeesDTO.getFullName()==null){
            throw new BadRequestException("Enter Account Full Name");
        }
        if(payeesDTO.getEmail()==null){
            throw new BadRequestException("Enter Account Email Address");
        }
        if(payeesDTO.getMobileNo()==null){
            throw new BadRequestException("Enter Account Phone Number");
        }
        Payees payees = new Payees();
        if(payeesDTO.getPayId()==null){
            if(payeesRepo.countPayesByEmail(payeesDTO.getEmail()) > 0){
                throw new BadRequestException("Payee with Email Address Exists..");
            }
            payees.setStatus("A");
        }
        else{
            if(payeesRepo.countPayesByEmail(payeesDTO.getEmail()) > 1){
                throw new BadRequestException("Payee with Email Address Exists..");
            }
            payees.setStatus(payeesDTO.getStatus());
            payees.setPayId(payeesDTO.getPayId());
        }
        payees.setCreatedBy(userUtils.getCurrentUser());
        payees.setCreatedDate(new Date());
        payees.setEmail(payeesDTO.getEmail());
        payees.setMobileNo(payeesDTO.getMobileNo());
        payees.setTelNo(payeesDTO.getTelNo());
        payees.setFullName(payeesDTO.getFullName());
        Payees savedPayee = payeesRepo.save(payees);
        if(payeesDTO.getPayId()==null){
            if(payeesDTO.getAccountNo()==null){
                throw new BadRequestException("Please Provide Account No");
            }
            if(payeesDTO.getBankBranchId()==null){
                throw new BadRequestException("Please Select Bank Branch");
            }
            PayeeAccounts payeeAccounts = new PayeeAccounts();
            payeeAccounts.setAccountNo(payeesDTO.getAccountNo());
            payeeAccounts.setBankBranches(bankBranchRepo.findOne(payeesDTO.getBankBranchId()));
            payeeAccounts.setPayees(savedPayee);
            payeeAccounts.setStatus("A");
            payeeAccountsRepo.save(payeeAccounts);
        }
    }

    @Override
    @Transactional(rollbackFor = {BadRequestException.class})
    public void createPayeeAccount(PayeeAccountsDTO payeesDTO) throws BadRequestException {
        if(payeesDTO.getPayId()==null){
            throw new BadRequestException("Select Payee to Add Account");
        }
        if(payeesDTO.getAccountNo()==null){
            throw new BadRequestException("Enter Account No to continue...");
        }
        if(payeesDTO.getBankBranchId()==null){
            throw new BadRequestException("Please Select Bank Branch");
        }
        PayeeAccounts payeeAccounts = new PayeeAccounts();
        if(payeesDTO.getPaycId()==null) {
            List<PayeeAccounts> payeeAccountsList = payeeAccountsRepo.findOtherAccounts(payeesDTO.getPayId());
            for (PayeeAccounts accounts : payeeAccountsList) {
                accounts.setStatus("I");
            }
            payeeAccountsRepo.save(payeeAccountsList);
            payeeAccounts.setStatus("A");
        }
        else{
            payeeAccounts.setStatus(payeesDTO.getStatus());
            payeeAccounts.setPaycId(payeesDTO.getPaycId());

        }

        payeeAccounts.setPayees(payeesRepo.findOne(payeesDTO.getPayId()));
        payeeAccounts.setBankBranches(bankBranchRepo.findOne(payeesDTO.getBankBranchId()));
        payeeAccounts.setAccountNo(payeesDTO.getAccountNo());

        payeeAccountsRepo.save(payeeAccounts);
        if(payeeAccountsRepo.countAccountExists(payeesDTO.getPayId(),payeesDTO.getAccountNo()) > 1){
            throw new BadRequestException("Account Number is duplicated for Payee..");
        }
    }

    @Override
    public void createAccountingYear(AccountYearDTO accountYearDTO) throws BadRequestException {
        if(accountYearDTO.getYear()==null){
            throw new BadRequestException("Please enter a valid year");
        }
        if(accountYearDTO.getYear().longValue()!= yearsRepo.getCurrentAccountingYear().longValue()){
            throw new BadRequestException("Unable to generate accounting periods for the selected Period. Consult Administrator");
        }
//        if(yearsRepo.countCurrentAccountingYear() > 0){
//            throw new BadRequestException("An existing open accounting period exists..Close the period to open a new one...");
//        }
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, accountYearDTO.getYear().intValue());
        cal.set(Calendar.MONTH,0);
        cal.set(Calendar.DAY_OF_MONTH,1);
        Date startdate = cal.getTime();

        Calendar calendarEnd=Calendar.getInstance();
        calendarEnd.set(Calendar.YEAR,accountYearDTO.getYear().intValue());
        calendarEnd.set(Calendar.MONTH,11);
        calendarEnd.set(Calendar.DAY_OF_MONTH,31);
        Date enddate = calendarEnd.getTime();
        final Iterable<OrgBranch> branches = branchRepository.findAll();
        final List<AccountYearPeriods> periodsList = new ArrayList<>();
        for(OrgBranch branch:branches){
            final AccountYears accountYears = new AccountYears();
            accountYears.setYear(accountYearDTO.getYear());
            accountYears.setYearStart(startdate);
            accountYears.setYearEnd(enddate);
            accountYears.setState("O");
            accountYears.setTotalPeriods(12);
            accountYears.setBranch(branch);
            AccountYears savedYear = yearsRepo.save(accountYears);
            for (int currentMonth = 0; currentMonth < 12; currentMonth++) {
                Calendar cal2 = Calendar.getInstance();
                cal2.set(Calendar.MONTH, currentMonth);
                AccountYearPeriods periods = new AccountYearPeriods();
                periods.setAccountYears(savedYear);
                periods.setBranch(branch);
                cal2.set(Calendar.DAY_OF_MONTH, 1);
                periods.setMonthStart(cal2.getTime());
                cal2.set(Calendar.DAY_OF_MONTH, cal2.getActualMaximum(Calendar.DAY_OF_MONTH));
                periods.setMonthEnd(cal2.getTime());
                final String month = new SimpleDateFormat("MMMM").format(cal2.getTime());
                periods.setPeriodName(month);
                periods.setState("O");
                periodsList.add(periods);
            }
        }
        periodsRepo.save(periodsList);
    }
    @Override
    public Map<String, Long> getCurrentAccountPeriod() {
        final Long year = yearsRepo.getCurrentAccountingYear();
        Map<String,Long> map = new HashMap<>();
        map.put("currentYear",year);
        return map;
    }

    @Override
    public DataTablesResult<AccountYearDTO> findAccountingYears(Long year,Long branchCode, DataTablesRequest request) {
        List<Object[]> accountingYears = yearsRepo.searchAccountingYears(branchCode,year,request.getPageNumber(), request.getPageSize());
        List<AccountYearDTO> accountYearDTOList = new ArrayList<>();
        long rowCount = 0L;
        if(!accountingYears.isEmpty()) rowCount = (Integer)accountingYears.get(0)[6];
        for(Object[] years:accountingYears) {
            AccountYearDTO accountYearDTO = new AccountYearDTO();
            accountYearDTO.setYearId(((BigDecimal) years[0]).longValue());
            final String state = (String) years[1];
            if(state==null || state.equalsIgnoreCase("O")){
                accountYearDTO.setStatus("Open");
            }
            else accountYearDTO.setStatus("Closed");
            accountYearDTO.setYear(((BigDecimal) years[2]).longValue());
            accountYearDTO.setWef((Date) years[3]);
            accountYearDTO.setWet((Date) years[4]);
            accountYearDTO.setNoofMonths((Integer) years[5]);
            accountYearDTOList.add(accountYearDTO);
        }
        Page<AccountYearDTO>  page = new PageImpl<>(accountYearDTOList,request, rowCount);
        return new DataTablesResult<>(request, page);
    }

    @Override
    public DataTablesResult<AccountingPeriodDTO> findAccountingYearsPeriods(Long yearId, DataTablesRequest request) {
        List<Object[]> accountingYearPeriods = periodsRepo.searchAccountingYearsPeriods(yearId,request.getPageNumber(), request.getPageSize());
        List<AccountingPeriodDTO> accountingPeriodDTOList = new ArrayList<>();
        long rowCount = 0L;
        if(!accountingYearPeriods.isEmpty()) rowCount = (Integer)accountingYearPeriods.get(0)[9];
        for(Object[] yearPrd:accountingYearPeriods) {
            AccountingPeriodDTO periodDTO = new AccountingPeriodDTO();
            periodDTO.setPeriodId(((BigDecimal) yearPrd[0]).longValue());
            final String state = (String) yearPrd[4];
            if(state==null || state.equalsIgnoreCase("O")){
                periodDTO.setStatus("Open");
            }
            else periodDTO.setStatus("Closed");
            periodDTO.setPeriodName((String) yearPrd[1]);
            periodDTO.setWef((Date) yearPrd[2]);
            periodDTO.setWet((Date) yearPrd[3]);
            periodDTO.setClosedDate((Date) yearPrd[5]);
            periodDTO.setUserClosed((String) yearPrd[6]);
            periodDTO.setTransacted((String) yearPrd[7]);
            periodDTO.setBranch((String) yearPrd[8]);
            accountingPeriodDTOList.add(periodDTO);
        }
        Page<AccountingPeriodDTO>  page = new PageImpl<>(accountingPeriodDTOList,request, rowCount);
        return new DataTablesResult<>(request, page);
    }

    @Override
    public void createReportFormatAccount(FinalReportFormatAcctsDTO acctsDTO) throws BadRequestException {
        if(acctsDTO.getAffsign()==null){
            throw new BadRequestException("Select Account Sign to continue....");
        }
        if(acctsDTO.getAccountNo()==null){
            throw new BadRequestException("Select A valid account to continue...");
        }

        if(acctsDTO.getRfId()==null){
            throw new BadRequestException("Select Report Format to Attach an account...");
        }
        if(finalReportFormatAccountsRepo.countDuplicateAccts(acctsDTO.getAccountNo(),acctsDTO.getRfId()) > 0){
            throw new BadRequestException("Account Already Mapped..Cannot remap");
        }

        FinalReportFormatAccounts accounts = new FinalReportFormatAccounts();
        accounts.setAccountNo(acctsDTO.getAccountNo());
        accounts.setAccountName(acctsDTO.getAccountName());
        accounts.setSign(acctsDTO.getAffsign().equalsIgnoreCase("P"));
        accounts.setReportFormats(finalReportFormatsRepo.findOne(acctsDTO.getRfId()));
        finalReportFormatAccountsRepo.save(accounts);
    }

    @Override
    public void createReportFormatGroupAccount(FinalReportFormatAcctsDTO acctsDTO) throws BadRequestException {
        if(acctsDTO.getAffsign()==null){
            throw new BadRequestException("Select Account Sign to continue....");
        }
        if(acctsDTO.getAccountNo()==null){
            throw new BadRequestException("Select A valid account to continue...");
        }

        if(finalReportFormatGroupAccountsRepo.countDuplicateAccts(acctsDTO.getAccountNo(),acctsDTO.getRfId())> 0){
            throw new BadRequestException("Group Account Already Mapped..Cannot remap");
        }

        if(finalReportFormatGroupAccountsRepo.validateAccount(acctsDTO.getAccountNo())!=1){
            throw new BadRequestException("Account No has been mapped incorrectly..Please check you account set ups...");
        }

        FinalReportFormatGroupAccounts groupAccounts = new FinalReportFormatGroupAccounts();
        groupAccounts.setAccountNo(acctsDTO.getAccountNo());
        groupAccounts.setSign(acctsDTO.getAffsign().equalsIgnoreCase("P"));
        groupAccounts.setReportFormats(finalReportFormatsRepo.findOne(acctsDTO.getRfId()));
        finalReportFormatGroupAccountsRepo.save(groupAccounts);
    }
}
