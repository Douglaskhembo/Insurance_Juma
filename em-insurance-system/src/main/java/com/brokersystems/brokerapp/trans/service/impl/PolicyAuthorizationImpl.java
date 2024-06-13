package com.brokersystems.brokerapp.trans.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import com.brokersystems.brokerapp.accounts.model.CoaSubAccounts;
import com.brokersystems.brokerapp.accounts.model.QPaymentAudit;
import com.brokersystems.brokerapp.accounts.model.Refunds;
import com.brokersystems.brokerapp.accounts.repository.CoaSubAccountsRepo;
import com.brokersystems.brokerapp.accounts.repository.PaymentAuditRepo;
import com.brokersystems.brokerapp.accounts.service.AccountsService;
import com.brokersystems.brokerapp.accounts.utils.AccountsPostingUtilities;
import com.brokersystems.brokerapp.aki.dto.TypeCIssueDTO;
import com.brokersystems.brokerapp.aki.dto.TypeCertificateIssueDTO;
import com.brokersystems.brokerapp.aki.service.AkiAuthenticationService;
import com.brokersystems.brokerapp.certs.model.PrintCertificateQueue;
import com.brokersystems.brokerapp.certs.repository.PrintQueueRepo;
import com.brokersystems.brokerapp.enums.RevenueItems;
import com.brokersystems.brokerapp.life.service.LifeService;
import com.brokersystems.brokerapp.medical.model.*;
import com.brokersystems.brokerapp.medical.repository.CategoryMembersRepo;
import com.brokersystems.brokerapp.medical.repository.MedicalCategoryRepo;
import com.brokersystems.brokerapp.medical.repository.SelfFundParamsRepo;
import com.brokersystems.brokerapp.medical.service.MedicalCardsService;
import com.brokersystems.brokerapp.security.CheckAuthLimits;
import com.brokersystems.brokerapp.server.utils.DateUtilities;
import com.brokersystems.brokerapp.server.utils.Streamable;
import com.brokersystems.brokerapp.setup.model.*;
import com.brokersystems.brokerapp.setup.repository.*;
import com.brokersystems.brokerapp.trans.model.*;
import com.brokersystems.brokerapp.trans.repository.*;
import com.brokersystems.brokerapp.trans.service.AccountsUtilities;
import com.brokersystems.brokerapp.trans.service.AllocationService;
import com.brokersystems.brokerapp.uw.model.*;
import com.brokersystems.brokerapp.uw.repository.*;
import com.brokersystems.brokerapp.uw.service.PolicyTransService;
import com.brokersystems.brokerapp.webservices.model.VehicleDetails;
import com.brokersystems.brokerapp.webservices.portalmodel.Product;
import com.brokersystems.brokerapp.workflow.docs.DocType;
import com.brokersystems.brokerapp.workflow.docs.QSysWfDocs;
import com.brokersystems.brokerapp.workflow.repository.SysWfDocsRepo;
import com.brokersystems.brokerapp.workflow.utils.WorkflowService;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.brokersystems.brokerapp.server.exception.BadRequestException;
import com.brokersystems.brokerapp.server.utils.UserUtils;
import com.brokersystems.brokerapp.trans.service.PolicyAuthorization;
import com.mysema.query.types.Predicate;

import javax.sql.DataSource;

@Service
public class PolicyAuthorizationImpl implements PolicyAuthorization {

    @Autowired
    private PolicyTransRepo policyRepo;

    @Autowired
    private GlTransRepo glTransRepo;

    @Autowired
    private SystemTransactionsRepo sysTransRepo;

    @Autowired
    private SettlementRepo settlementRepo;

    @Autowired
    private SystemTransRepo transRepo;

    @Autowired
    private PaymentAuditRepo auditRepo;

    @Autowired
    private UserUtils userUtils;


    @Autowired
    private DataSource dataSource;

    @Autowired
    private SequenceRepository sequenceRepo;

    @Autowired
    private RiskTransRepo riskRepo;

    @Autowired
    private SectionTransRepo sectionRepo;

    @Autowired
    private PrintQueueRepo printQueueRepo;

    @Autowired
    private DateUtilities dateUtils;

    @Autowired
    private AllocationService allocationService;

    @Autowired
    private CheckAuthLimits authLimits;

    @Autowired
    private TransMappingRepo mappingRepo;

    @Autowired
    private MotorVehicleDetailsRepo motorVehicleDetailsRepo;

    @Autowired
    private MedicalCategoryRepo categoryRepo;

    @Autowired
    private MedicalCardsService cardsService;

    @Autowired
    private SelfFundParamsRepo selfFundParamsRepo;

    @Autowired
    private CategoryMembersRepo membersRepo;

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private AccountsUtilities accountsUtilities;

    @Autowired
    private AccountsPostingUtilities accountsPostingUtilities;

    @Autowired
    private RiskDocsRepo riskDocsRepo;

    @Autowired
    private TransChecksRepo transChecksRepo;

    @Autowired
    private PolicyRemarksRepo policyRemarksRepo;

    @Autowired
    private AccountsService accountsService;

    @Autowired
    private LifeService lifeService;

    @Autowired
    private PolicyBindersRepo policyBindersRepo;

    @Autowired
    private SystemTransactionsTempRepo systemTransactionsTempRepo;
    @Autowired
    private CoaSubAccountsRepo accountsRepo;
    @Autowired
    private AkiAuthenticationService authenticationService;



    public void generateCert(Long polCode) throws BadRequestException {
        PolicyTrans policy = policyRepo.findOne(polCode);
        Iterable<RiskTrans> risks = riskRepo.findAll(QRiskTrans.riskTrans.policy.policyId.eq(polCode));
        for(RiskTrans riskBean:risks){
            final List<PrintCertificateQueue> printCertificateQueue = printQueueRepo.findAllByRisk(riskBean);
//            try {

                if(printCertificateQueue.size()==1) {
                    generateCert(riskBean, policy.getPolNo());
                }
                else{
                    throw new BadRequestException("No Certificate Available. Add A Certificate to continue....");
                }
//            }
//            catch (BadRequestException ex){
//                final PrintCertificateQueue printCertificateQueue1 = printCertificateQueue.get(0);
//                printCertificateQueue1.setErrorMessage(ex.getMessage());
//                printCertificateQueue1.setIssued("N");
//                printQueueRepo.save(printCertificateQueue1);
//            }
        }
    }

    @PreAuthorize("hasAnyAuthority('AUTHORIZE_POLICY')")
    @Override
    @Modifying
    @Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
    public void authorizePolicy(Long polCode,BigDecimal refundAmount) throws BadRequestException{

        PolicyTrans policy = policyRepo.findOne(polCode);
        boolean cashBasis = policy.getInterfaceType()!=null && "C".equalsIgnoreCase(policy.getInterfaceType());

        if(!"R".equalsIgnoreCase(policy.getAuthStatus())){
            throw new BadRequestException("Can only authorize ready policies");
        }
        if(!authLimits.checkAuthorizationLimits("AUTHORIZE_POLICY",policy.getBasicPrem())){
            throw new BadRequestException("You have no rights to authorize the transaction...Check your authorization limits..");
        }
        long riskcount = riskRepo.count(QRiskTrans.riskTrans.policy.policyId.eq(polCode));

        if(riskcount==0) throw new BadRequestException("Cannot Authorize Transaction Without Risk Details");

        Iterable<RiskTrans> risks = riskRepo.findAll(QRiskTrans.riskTrans.policy.policyId.eq(polCode));

        long checkCount = transChecksRepo.count(QTransChecks.transChecks.policyTrans.policyId.eq(polCode).and(QTransChecks.transChecks.authorised.eq("N")));

        if(checkCount > 0){
            throw new BadRequestException("Cannot Authorize when there is unauthorized checks....");
        }
        BigDecimal prems = (policy.getPremium()==null)?BigDecimal.ZERO:policy.getPremium();
        if(cashBasis && prems.compareTo(BigDecimal.ZERO)==1) {
            if (cashBasisBalance(polCode).compareTo(BigDecimal.ZERO) > 0) {
                throw new BadRequestException("Cannot authorize the Transaction..This is a cash basis transaction with balance of " + cashBasisBalance(polCode));
            }
        }

        Date polWef = dateUtils.removeTime(policy.getWefDate());
        Date polWet = dateUtils.removeTime(policy.getWetDate());
        for(RiskTrans riskBean:risks){

            long sectCount = sectionRepo.count(QSectionTrans.sectionTrans.risk.riskId.eq(riskBean.getRiskId()));
            if(sectCount==0) throw new BadRequestException("Risk "+riskBean.getRiskShtDesc()+" has no sections..Cannot Authorize the transaction");

//            Iterable<RiskDocs> riskDocs = riskDocsRepo.findAll(QRiskDocs.riskDocs.risk.riskId.eq(riskBean.getRiskId()));
//            if(policy.getTransType()!=null && !"CN".equalsIgnoreCase(policy.getTransType())) {
//                for (RiskDocs riskDoc : riskDocs) {
//                    if (riskDoc.getCheckSum() == null || StringUtils.isBlank(riskDoc.getCheckSum())) {
//                        throw new BadRequestException(String.format("Cannot authorize policy without uploading documents for Risk %s", riskBean.getRiskShtDesc()));
//                    }
//                }
//            }

            Date riskWef = dateUtils.removeTime(riskBean.getWefDate());
            Date riskWet = dateUtils.removeTime(riskBean.getWetDate());
            if(riskWef.before(polWef) || riskWef.after(polWet)
                    || riskWet.before(polWef) || riskWet.after(polWet)){
                throw new BadRequestException("Risk Cover Dates outside Policy Cover Periods ");
            }
            if(riskBean.getBinderDetails().getBinder().getBinId()!=riskBean.getBinder().getBinId()){
                throw new BadRequestException("Cannot Make Ready...Sub Class and Cover Type Details do not match the binder selected");
            }
        }

        if(policy.getFuturePrem()!=null)
            if(policy.getFuturePrem().compareTo(BigDecimal.ZERO)==-1) throw new BadRequestException("Policy Future Annual Premium Cannot be negative...");

        String refNo =null;
        String debitCode = null;
        if(cashBasis && prems.compareTo(BigDecimal.ZERO)==1) {
            SystemTransactionsTemp systemTransactionsTemp = systemTransactionsTempRepo.findOne(QSystemTransactionsTemp.systemTransactionsTemp.policy.policyId.eq(polCode));
            if(systemTransactionsTemp==null)
                throw new BadRequestException("Unable to authorize without payment of the installment premium");
            refNo = systemTransactionsTemp.getRefNo();
            debitCode = systemTransactionsTemp.getTransType();
        }
        else {
            Predicate seqPredicate = QSystemSequence.systemSequence.transType.eq("D");
            if (sequenceRepo.count(seqPredicate) == 0)
                throw new BadRequestException("Sequence for Debit Notes has not been defined");
            SystemSequence sequence = sequenceRepo.findOne(seqPredicate);
            Long seqNumber = sequence.getNextNumber();
            String transType = policy.getTransType();
            if ("CO".equalsIgnoreCase(policy.getTransType())) {
                transType = policy.getPreviousTrans().getTransType();
            }
            if ("NB".equalsIgnoreCase(transType)) {
                transType = "NB";
            } else if ("RN".equalsIgnoreCase(transType)) {
                transType = "RN";
            } else {
                transType = "EN";
            }
            if (mappingRepo.count(QTransactionMapping.transactionMapping.transType.eq(transType)) == 0)
                throw new BadRequestException("Error getting Transaction Mapping Setups..Contact System Administrator");
            TransactionMapping mapping = mappingRepo.findOne(QTransactionMapping.transactionMapping.transType.eq(transType));
             refNo = ((prems.compareTo(BigDecimal.ZERO) >= 0) ? mapping.getDebitCode() : mapping.getCreditCode()) + String.format("%05d", seqNumber);
             debitCode = ((prems.compareTo(BigDecimal.ZERO) >= 0) ? mapping.getDebitCode() : mapping.getCreditCode());
            sequence.setLastNumber(seqNumber);
            sequence.setNextNumber(seqNumber + 1);
            sequenceRepo.save(sequence);
        }

        policy.setAuthBy(userUtils.getCurrentUser());
        policy.setAuthStatus("A");
        policy.setAuthDate(new Date());
        if ("CO".equalsIgnoreCase(policy.getTransType()) || "CN".equalsIgnoreCase(policy.getTransType()) || "RS".equalsIgnoreCase(policy.getTransType())) {
            Iterable<PolicyRemarks> policyRemarks = policyRemarksRepo.findAll(QPolicyRemarks.policyRemarks.policy.policyId.eq(policy.getPolicyId()));
            if (policyRemarks.spliterator().getExactSizeIfKnown() == 0) {
                throw new BadRequestException("Input Policy Remarks first....");
            }
            if ("CN".equalsIgnoreCase(policy.getTransType())) {
                policy.setCurrentStatus("CN");
            }
            if ("CO".equalsIgnoreCase(policy.getTransType())) {
                policy.setCurrentStatus("CO");
            } else policy.setCurrentStatus("A");
        }
        else
            policy.setCurrentStatus("A");
        policy.setRefNo(refNo);
        policyRepo.save(policy);

        if(!("NB".equalsIgnoreCase(policy.getTransType()) || "SP".equalsIgnoreCase(policy.getTransType()))){
            PolicyTrans prevPolicy = policy.getPreviousTrans();
            if("CO".equalsIgnoreCase(policy.getTransType()) && prevPolicy.getPreviousTrans().getPolicyId()!=prevPolicy.getPolicyId()){
                prevPolicy.setCurrentStatus(policy.getTransType());
                PolicyTrans prevPolicy1 = prevPolicy.getPreviousTrans();
                if (!prevPolicy1.getTransType().equalsIgnoreCase("CO")) {
                    prevPolicy1.setCurrentStatus("A");
                    policyRepo.save(prevPolicy1);
                }
            }else {
                prevPolicy.setCurrentStatus(policy.getTransType());
            }
            policyRepo.save(prevPolicy);
        }

        if("RE".equalsIgnoreCase(policy.getPolRevStatus())){
            PolicyTrans prevPolicy = policy.getPreviousTrans();
            prevPolicy.setCurrentStatus(policy.getTransType());
            policyRepo.save(prevPolicy);
        }


        if(transRepo.count(QSystemTrans.systemTrans.policy.policyId.eq(polCode).and(QSystemTrans.systemTrans.transAuthorised.eq("N"))) > 1){
            throw new BadRequestException("More than one Unauthorized Transactions for the Policy..Contact System Admin");
        }

        if(transRepo.count(QSystemTrans.systemTrans.policy.policyId.eq(polCode).and(QSystemTrans.systemTrans.transAuthorised.eq("N")))== 0){
            throw new BadRequestException("No Transaction to Authorize..Restart the Endorsement");
        }

        //accountsUtilities.validatePolicyAccounts(policy);

        SystemTrans transaction = transRepo.findOne(QSystemTrans.systemTrans.policy.policyId.eq(polCode).and(QSystemTrans.systemTrans.transAuthorised.eq("N")));
        transaction.setAuthBy(userUtils.getCurrentUser());
        transaction.setAuthDate(new Date());
        transaction.setTransAuthorised("Y");
        transRepo.save(transaction);
        BigDecimal basicPrem = (policy.getPremium()==null)?BigDecimal.ZERO:policy.getPremium();
        BigDecimal extras = (policy.getExtras()==null)?BigDecimal.ZERO:policy.getExtras();
        BigDecimal phcf = (policy.getPhcf()==null)?BigDecimal.ZERO:policy.getPhcf();
        BigDecimal tl = (policy.getTrainingLevy()==null)?BigDecimal.ZERO:policy.getTrainingLevy();
        BigDecimal sd = (policy.getStampDuty()==null)?BigDecimal.ZERO:policy.getStampDuty();
        BigDecimal subAgentComm = (policy.getSubAgentComm()==null)?BigDecimal.ZERO:policy.getSubAgentComm();
        BigDecimal amountWithTaxes =basicPrem.add(extras).add(phcf).add(tl).add(sd);

        SystemTransactions savedAgentTrans = null;
//       if (policy.getPolicyId()!=null) {
//            throw new BadRequestException("Refund amount="+refundAmount );
//        }
        //Client Transaction
        if(basicPrem.compareTo(BigDecimal.ZERO)!=0){
            String type = (amountWithTaxes.compareTo(BigDecimal.ZERO)==1)?"D":"C";
            SystemTransactions trans = new SystemTransactions();
            trans.setAmount(basicPrem.abs().multiply(sign(type)).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
            trans.setAuthDate(new Date());
            trans.setAuthorised("Y");
            trans.setBalance(amountWithTaxes.abs().multiply(sign(type)).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
            trans.setBranch(policy.getBranch());
            trans.setClientType("C");
            trans.setControlAcc(policy.getClient().getTenantNumber());
            trans.setClient(policy.getClient());
            trans.setCurrRate(new BigDecimal(1));
            trans.setCurrency(policy.getTransCurrency());
            trans.setNarrations("Posting client Debit Note");
            trans.setNetAmount(amountWithTaxes.abs().multiply(sign(type)).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
            trans.setOrigin("U");
            trans.setPhfund(phcf.setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
            trans.setPolicy(policy);
            trans.setRefNo(refNo);
            trans.setSd(sd.abs().multiply(sign(type)).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
            trans.setTl(tl.abs().multiply(sign(type)).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
            trans.setTransDate(new Date());
            trans.setTransdc((amountWithTaxes.compareTo(BigDecimal.ZERO)==1)?"D":"C");
            trans.setTransType(debitCode); //Should not be hardcorded
            trans.setUserAuth(userUtils.getCurrentUser().getUsername());
            trans.setWhtx(BigDecimal.ZERO);
            trans.setExtras(extras.abs().multiply(sign(type)).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
            trans.setPostedDate(new Date());
            trans.setPostedUser(userUtils.getCurrentUser());
            trans.setTransaction(transaction);
            SystemTransactions savedClientTrans =  sysTransRepo.save(trans);
            if("CO".equalsIgnoreCase(policy.getTransType())){
                String refno = policy.getPreviousTrans().getRefNo();
                System.out.println("Ref no "+refno);
                SystemTransactions clientTrans = sysTransRepo.findOne(QSystemTransactions.systemTransactions.refNo.eq(refno)
                        .and(QSystemTransactions.systemTransactions.transdc.eq("D"))
                             .and(QSystemTransactions.systemTransactions.transType.in("NBD","APD","RND"))
                              .and(QSystemTransactions.systemTransactions.clientType.eq("C")));
                allocationService.autoAllocateContra(savedClientTrans.getTransno(),clientTrans.getTransno());
            }

            final boolean multiproduct = policyBindersRepo.count(QPolicyBinders.policyBinders.policyTrans.policyId.eq(policy.getPolicyId())) > 0;
            final String agntSign = (basicPrem.compareTo(BigDecimal.ZERO) == -1) ? "D" : "C";
            if(multiproduct){
                Iterable<PolicyBinders> policyBinders = policyBindersRepo.findAll(QPolicyBinders.policyBinders.policyTrans.policyId.eq(polCode));
                int counter = 0;
                for(PolicyBinders binders:policyBinders){
                    counter++;
                    BigDecimal commamt = (binders.getCommission() == null) ? BigDecimal.ZERO : binders.getCommission();
                    BigDecimal whtx = (binders.getWhtx() == null) ? BigDecimal.ZERO : binders.getWhtx();
                     phcf = (binders.getPhcf() == null) ? BigDecimal.ZERO : binders.getPhcf();
                    tl = (binders.getTl() == null) ? BigDecimal.ZERO : binders.getTl();
                    sd = BigDecimal.ZERO;
                    BigDecimal agentAmt = (binders.getBasicPrem().abs().multiply(sign(agntSign)).add(phcf.abs().multiply(sign(agntSign))).
                            add(tl.abs().multiply(sign(agntSign))).subtract(commamt.abs().multiply(sign(agntSign))).add(whtx.abs().multiply(sign(agntSign))));
                    //Agent Transaction
                    SystemTransactions atrans = new SystemTransactions();
                    atrans.setAmount(binders.getBasicPrem().abs().multiply(sign(agntSign)).setScale(policy.getTransCurrency().getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
                    atrans.setAuthDate(new Date());
                    atrans.setAuthorised("Y");
                    atrans.setBalance(agentAmt.setScale(policy.getTransCurrency().getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
                    atrans.setBranch(policy.getBranch());
                    atrans.setClientType("A");
                    atrans.setControlAcc(binders.getBinder().getAccount().getShtDesc());
                    atrans.setAgent(binders.getBinder().getAccount());
                    atrans.setCommission(commamt.abs().multiply(sign(agntSign)).setScale(policy.getTransCurrency().getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
                    atrans.setCurrRate(new BigDecimal(1));
                    atrans.setCurrency(policy.getTransCurrency());
                    atrans.setNarrations("Posting Agent Credit Note");
                    atrans.setNetAmount(agentAmt.setScale(policy.getTransCurrency().getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
                    atrans.setOrigin("U");
                    atrans.setPhfund(phcf.abs().multiply(sign(agntSign)).setScale(policy.getTransCurrency().getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
                    atrans.setPolicy(policy);
                    atrans.setRefNo(refNo+"/"+counter);
                    atrans.setSd(sd.abs().multiply(sign(agntSign)).setScale(policy.getTransCurrency().getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
                    atrans.setTl(tl.abs().multiply(sign(agntSign)).setScale(policy.getTransCurrency().getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
                    atrans.setTransDate(new Date());
                    atrans.setTransdc(agntSign);
                    atrans.setTransType(debitCode); //Should not be hardcorded
                    atrans.setUserAuth(userUtils.getCurrentUser().getUsername());
                    atrans.setWhtx(whtx.abs().multiply(sign(agntSign)).setScale(policy.getTransCurrency().getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
                    atrans.setExtras(extras.abs().multiply(sign(agntSign)).setScale(policy.getTransCurrency().getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
                    atrans.setPostedDate(new Date());
                    atrans.setPostedUser(userUtils.getCurrentUser());
                    atrans.setTransaction(transaction);
                    savedAgentTrans = sysTransRepo.save(atrans);
                }
                postUwTransactions(policy, transaction, BigDecimal.ZERO);
            }
            else {


                BigDecimal commamt = (policy.getCommAmt() == null) ? BigDecimal.ZERO : policy.getCommAmt();
                BigDecimal whtx = (policy.getWhtx() == null) ? BigDecimal.ZERO : policy.getWhtx();
                BigDecimal agentAmt = (basicPrem.abs().multiply(sign(agntSign)).add(extras.abs().multiply(sign(agntSign))).add(phcf.abs().multiply(sign(agntSign))).
                        add(tl.abs().multiply(sign(agntSign))).add(sd.abs().multiply(sign(agntSign))).subtract(commamt.abs().multiply(sign(agntSign))).add(whtx.abs().multiply(sign(agntSign))));
                //Agent Transaction
                SystemTransactions atrans = new SystemTransactions();
                atrans.setAmount(basicPrem.abs().multiply(sign(agntSign)).setScale(policy.getTransCurrency().getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
                atrans.setAuthDate(new Date());
                atrans.setAuthorised("Y");
                atrans.setBalance(agentAmt.setScale(policy.getTransCurrency().getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
                atrans.setBranch(policy.getBranch());
                atrans.setClientType("A");
                atrans.setControlAcc(policy.getAgent().getShtDesc());
                atrans.setAgent(policy.getAgent());
                atrans.setCommission(commamt.abs().multiply(sign(agntSign)).setScale(policy.getTransCurrency().getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
                atrans.setCurrRate(new BigDecimal(1));
                atrans.setCurrency(policy.getTransCurrency());
                atrans.setNarrations("Posting Agent Credit Note");
                atrans.setNetAmount(agentAmt.setScale(policy.getTransCurrency().getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
                atrans.setOrigin("U");
                atrans.setPhfund(phcf.abs().multiply(sign(agntSign)).setScale(policy.getTransCurrency().getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
                atrans.setPolicy(policy);
                atrans.setRefNo(refNo);
                atrans.setSd(sd.abs().multiply(sign(agntSign)).setScale(policy.getTransCurrency().getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
                atrans.setTl(tl.abs().multiply(sign(agntSign)).setScale(policy.getTransCurrency().getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
                atrans.setTransDate(new Date());
                atrans.setTransdc(agntSign);
                atrans.setTransType(debitCode); //Should not be hardcorded
                atrans.setUserAuth(userUtils.getCurrentUser().getUsername());
                atrans.setWhtx(whtx.abs().multiply(sign(agntSign)).setScale(policy.getTransCurrency().getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
                atrans.setExtras(extras.abs().multiply(sign(agntSign)).setScale(policy.getTransCurrency().getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
                atrans.setPostedDate(new Date());
                atrans.setPostedUser(userUtils.getCurrentUser());
                atrans.setTransaction(transaction);
                postUwTransactions(policy, transaction, commamt);
                 savedAgentTrans = sysTransRepo.save(atrans);
            }
            if(!multiproduct && "CO".equalsIgnoreCase(policy.getTransType())){
                String refno = policy.getPreviousTrans().getRefNo();
                SystemTransactions agentTrans = sysTransRepo.findOne(QSystemTransactions.systemTransactions.refNo.eq(refno)
                        .and(QSystemTransactions.systemTransactions.transdc.eq("C")));
                long authcount = auditRepo.count(QPaymentAudit.paymentAudit.transNo.transno.eq(agentTrans.getTransno()).and(QPaymentAudit.paymentAudit.posted.eq("Y")));
                if(authcount==0)
                    allocationService.autoAllocateContra(agentTrans.getTransno(),savedAgentTrans.getTransno());

                else{
                    SystemTransactions clientTrans = sysTransRepo.findOne(QSystemTransactions.systemTransactions.refNo.eq(refno)
                            .and(QSystemTransactions.systemTransactions.transdc.eq("D")));
                    Iterable<ReceiptSettlementDetails> settlements = settlementRepo.findAll(QReceiptSettlementDetails.receiptSettlementDetails.drCr.eq("C")
                            .and(QReceiptSettlementDetails.receiptSettlementDetails.debit.transno.eq(clientTrans.getTransno())));

                    BigDecimal totalReceipted = BigDecimal.ZERO;
                    for(ReceiptSettlementDetails details:settlements){
                        totalReceipted = totalReceipted.add(details.getAllocatedAmt());
                    }
                    double prorationRate = totalReceipted.doubleValue()/(agentTrans.getNetAmount().doubleValue());
                    BigDecimal prorata = new BigDecimal(prorationRate);
                    if(prorata.compareTo(BigDecimal.ONE)==1) throw new BadRequestException("Error Doing contra...Contact Admin to check the settlement details");
                    SystemTransactions revTrans = new SystemTransactions();
                    revTrans.setAmount(agentTrans.getAmount().abs().multiply(sign("D")).multiply(prorata).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
                    revTrans.setAuthDate(new Date());
                    revTrans.setAuthorised("Y");
                    revTrans.setBalance(agentTrans.getBalance().abs().multiply(sign("D")).multiply(prorata).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
                    revTrans.setBranch(policy.getBranch());
                    revTrans.setClientType("A");
                    revTrans.setControlAcc(agentTrans.getControlAcc());
                    revTrans.setAgent(agentTrans.getAgent());
                    revTrans.setCommission(agentTrans.getCommission().abs().multiply(sign("D")).multiply(prorata).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
                    revTrans.setCurrRate(new BigDecimal(1));
                    revTrans.setCurrency(policy.getTransCurrency());
                    revTrans.setNarrations("Posting Agent Debit Note");
                    revTrans.setNetAmount(agentTrans.getNetAmount().abs().multiply(sign("D")).multiply(prorata).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
                    revTrans.setOrigin("U");
                    revTrans.setPhfund(agentTrans.getPhfund().abs().multiply(sign("D")).multiply(prorata).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
                    revTrans.setPolicy(policy);
                    revTrans.setRefNo(refNo);
                    revTrans.setSd(agentTrans.getSd().abs().multiply(sign("D")).multiply(prorata).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
                    revTrans.setTl(agentTrans.getTl().abs().multiply(sign("D")).multiply(prorata).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
                    revTrans.setTransDate(new Date());
                    revTrans.setTransdc("D");
                    revTrans.setTransType(debitCode); //Should not be hardcorded
                    revTrans.setUserAuth(userUtils.getCurrentUser().getUsername());
                    revTrans.setWhtx(agentTrans.getWhtx().abs().multiply(sign("D")).multiply(prorata).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
                    revTrans.setExtras(agentTrans.getExtras().abs().multiply(sign("D")).multiply(prorata).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
                    revTrans.setTransaction(transaction);
                    sysTransRepo.save(revTrans);
                }
            }

            // add refund here
            if (refundAmount!=null && refundAmount.compareTo(BigDecimal.ZERO)!=0) {
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

                type = (amountWithTaxes.compareTo(BigDecimal.ZERO)==1)?"C":"D";
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
                trans1.setTransaction(transaction);
                trans1.setRefundTransaction(savedClientTrans);
                SystemTransactions savedRefundTrans =  sysTransRepo.save(trans1);

//                // agent
//                agntSign =(basicPrem.compareTo(BigDecimal.ZERO)==-1)?"D":"C";
//                SystemTransactions atrans1 = new SystemTransactions();
//                atrans1.setAmount(refundAmount.abs().multiply(sign(agntSign)).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
//                atrans1.setAuthDate(new Date());
//                atrans1.setAuthorised("N");
//                atrans1.setBalance(refundAmount.abs().multiply(sign(agntSign)).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
//                atrans1.setBranch(policy.getBranch());
//                atrans1.setClientType("A");
//                atrans1.setControlAcc(policy.getAgent().getShtDesc());
//                atrans1.setAgent(policy.getAgent());
//                atrans1.setCurrRate(new BigDecimal(1));
//                atrans1.setCurrency(policy.getTransCurrency());
//                atrans1.setNarrations("Posting agent underwriting refund");
//                atrans1.setNetAmount(refundAmount.abs().multiply(sign(agntSign)).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
//                atrans1.setOrigin("U");
//                atrans1.setPolicy(policy);
//                atrans1.setRefNo(refundRefNo);
//                atrans1.setTransDate(new Date());
//                atrans1.setTransdc((amountWithTaxes.compareTo(BigDecimal.ZERO)==1)?"C":"D");
//                atrans1.setTransType(refundCode); //Should not be hardcorded
//                //trans.setUserAuth(userUtils.getCurrentUser().getUsername());
//                atrans1.setWhtx(BigDecimal.ZERO);
//                //trans.setExtras(extras.abs().multiply(sign(type)).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
//                // trans.setPostedDate(new Date());
//                //trans.setPostedUser(userUtils.getCurrentUser());
//                atrans1.setTransaction(transaction);
//                SystemTransactions savedAgentRefundTrans =  sysTransRepo.save(atrans1);


                //-----refunds ----------
                Refunds refund = new Refunds();
                refund.setRefundCaptureDate(new Date());
                refund.setCreatedUser(userUtils.getCurrentUser());
                refund.setMadeReadyBy(userUtils.getCurrentUser());
                refund.setMakeReadyDate(new Date());
                refund.setClient(policy.getClient());
                refund.setRefundStatus("R");
                refund.setAmount(refundAmount);
                refund.setNarrations("Posting client underwriting refund");
                refund.setPayee(policy.getClient().getFname().concat(policy.getClient().getOtherNames()));
                refund.setPolicy(policy);
                refund.setTransactions(savedRefundTrans);
                accountsService.createRefund(refund,"R");
            }
        }

        if(cashBasis && prems.compareTo(BigDecimal.ZERO)==1) {
            allocationService.allocateCashBasisTrans(polCode,userUtils.getCurrentUser());
        }
        boolean medicalProduct = false;
        if(policy.getProduct().getProGroup().getPrgType()==null || !policy.getProduct().getProGroup().getPrgType().equalsIgnoreCase("MD")){
            medicalProduct = false;
        }
        else if(policy.getProduct().getProGroup().getPrgType().equalsIgnoreCase("MD")){
            medicalProduct=true;
        }
        try {
            Map<String, Object> processVariables = Maps.newHashMap();
            processVariables.put("confirmAuth", true);
            workflowService.completeTask(String.valueOf(polCode), processVariables, policy, DocType.GEN_UW_DOCUMENT, (medicalProduct) ? "Y" : "N", null, null, null);
        }
        catch (Exception e){
            throw new BadRequestException("Authorize Checks first....");
        }

        //generateCert(polCode);
//        ExecutorService executor = Executors.newFixedThreadPool(1);
//        executor.submit(() ->  generateCert(polCode));

    }

    private VehicleDetails findSingleVehicleDetails(Long ipuCode) {
        List<Object[]> motorDetails = motorVehicleDetailsRepo.getVehicleDetails(ipuCode);
        for(Object[] motorDetail:motorDetails){
            VehicleDetails vehicleDetails = new VehicleDetails();
            vehicleDetails.setBodyColor((String) motorDetail[0]);
            vehicleDetails.setBodyType((String) motorDetail[1]);
            vehicleDetails.setCarModel((String) motorDetail[3]);
            vehicleDetails.setCarryCapacity(((BigDecimal)motorDetail[4]));
            vehicleDetails.setChassisNo((String) motorDetail[5]);
            vehicleDetails.setEngineCapacity(((BigDecimal)motorDetail[6]));
            vehicleDetails.setEngineNumber((String) motorDetail[7]);
            vehicleDetails.setLogbookNumber((String) motorDetail[8]);
            vehicleDetails.setYearOfManufacture((motorDetail[9]!=null)?motorDetail[9].toString():null);
            return vehicleDetails;
        }
        return null;
    }

    private void generateCert(final RiskTrans riskTrans, final String policyNumber) throws BadRequestException {
            final VehicleDetails details = findSingleVehicleDetails(riskTrans.getRiskId());
            if(details==null){
                throw new BadRequestException("Unable to get Vehicle details for this Risk with Registration number "+riskTrans.getRiskShtDesc());
            }
            final TypeCertificateIssueDTO typeCIssueDTO = new TypeCertificateIssueDTO();
            typeCIssueDTO.setBodytype((details.getBodyType()!=null)?details.getBodyType():"saloon");
            typeCIssueDTO.setChassisnumber(details.getChassisNo());
            if(riskTrans.getWefDate().compareTo(dateUtils.removeTime(new Date())) < 0){
                typeCIssueDTO.setCommencingdate(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
            }
            else{
                typeCIssueDTO.setCommencingdate(new SimpleDateFormat("dd/MM/yyyy").format(riskTrans.getWefDate()));
            }

            typeCIssueDTO.setExpiringdate(new SimpleDateFormat("dd/MM/yyyy").format(riskTrans.getWetDate()));
            typeCIssueDTO.setEnginenumber(details.getEngineNumber());
            typeCIssueDTO.setEmail(riskTrans.getInsured().getEmailAddress());
            typeCIssueDTO.setInsuredPIN(riskTrans.getInsured().getPinNo());
            typeCIssueDTO.setPolicyholder(riskTrans.getInsured().getFname()+" "+riskTrans.getInsured().getOtherNames());
            typeCIssueDTO.setPolicynumber(policyNumber);
            final String phone = riskTrans.getInsured().getPhoneNo();
            final String clientPhone = (phone != null && phone.length() > 9) ? org.apache.commons.lang.StringUtils.substring(phone, phone.length() - 9) : phone;
            typeCIssueDTO.setPhonenumber(clientPhone);
            typeCIssueDTO.setRegistrationnumber(riskTrans.getRiskShtDesc());
            typeCIssueDTO.setSumInsured(riskTrans.getSumInsured());
            typeCIssueDTO.setYearofmanufacture(Integer.parseInt(details.getYearOfManufacture()));
            typeCIssueDTO.setTypeOfCertificate(3);
            typeCIssueDTO.setVehiclemake(details.getCarMake());
            typeCIssueDTO.setVehiclemodel(details.getCarModel());
            if(riskTrans.getCovertype().getCovShtDesc().equalsIgnoreCase("COMP")){
                typeCIssueDTO.setTypeofcover(Integer.valueOf(100));
            }
            else   if(riskTrans.getCovertype().getCovShtDesc().equalsIgnoreCase("TPO")){
                typeCIssueDTO.setTypeofcover(Integer.valueOf(200));
            }
           // try {
                this.authenticationService.issueTypeCerts(typeCIssueDTO,riskTrans.getRiskId());
           // }
//            catch (BadRequestException ex){
//                throw new BadRequestException(ex.getMessage());
//            }
    }


    @PreAuthorize("hasAnyAuthority('AUTHORIZE_POLICY')")
    @Override
    @Modifying
    @Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
    public void authorizeLifePolicy(Long polCode) throws BadRequestException{

        PolicyTrans policy = policyRepo.findOne(polCode);
        if(policy==null) throw new BadRequestException("Invalid Policy to authorize");

        if(!"R".equalsIgnoreCase(policy.getAuthStatus())&& !"NB".equalsIgnoreCase(policy.getTransType())){
            throw new BadRequestException("Can only authorize ready policy");
        }
        if(!"CV".equalsIgnoreCase(policy.getAuthStatus())&& "NB".equalsIgnoreCase(policy.getTransType())){
            throw new BadRequestException("Can only authorize converted policy");
        }
        if(!authLimits.checkAuthorizationLimits("AUTHORIZE_POLICY",policy.getBasicPrem())){
            throw new BadRequestException("You have no rights to authorize the transaction...Check your authorization limits..");
        }
        long riskcount = riskRepo.count(QRiskTrans.riskTrans.policy.policyId.eq(polCode));

        if(riskcount==0) throw new BadRequestException("Cannot Authorize Transaction Without Risk Details");

        Iterable<RiskTrans> risks = riskRepo.findAll(QRiskTrans.riskTrans.policy.policyId.eq(polCode));

        long checkCount = transChecksRepo.count(QTransChecks.transChecks.policyTrans.policyId.eq(polCode).and(QTransChecks.transChecks.authorised.eq("N")));

        if(checkCount > 0){
            throw new BadRequestException("Cannot Authorize when there is unauthorized checks....");
        }


        BigDecimal prems = (policy.getPremium()==null)?BigDecimal.ZERO:policy.getPremium();
        Predicate seqPredicate = QSystemSequence.systemSequence.transType.eq("D");
        if (sequenceRepo.count(seqPredicate) == 0)
            throw new BadRequestException("Sequence for Debit Notes has not been defined");
        SystemSequence sequence = sequenceRepo.findOne(seqPredicate);
        Long seqNumber = sequence.getNextNumber();
        String transType = policy.getTransType();
        if("CO".equalsIgnoreCase(policy.getTransType())){
            transType = policy.getPreviousTrans().getTransType();
        }
        if("NB".equalsIgnoreCase(transType)){
            transType = "NB";
        }
        else if("RN".equalsIgnoreCase(transType)){
            transType = "RN";
        }
        else{
            transType = "EN";
        }


        policy.setAuthBy(userUtils.getCurrentUser());
        policy.setAuthStatus("A");
        policy.setAuthDate(new Date());
        if("CN".equalsIgnoreCase(policy.getTransType())){
            policy.setCurrentStatus("CN");
        }if("CO".equalsIgnoreCase(policy.getTransType())){
            Iterable<PolicyRemarks> policyRemarks = policyRemarksRepo.findAll(QPolicyRemarks.policyRemarks.policy.policyId.eq(policy.getPolicyId()));
            if(policyRemarks.spliterator().getExactSizeIfKnown() == 0){
                throw new BadRequestException("Input Remarks first....");
            }
            policy.setCurrentStatus("CO");
        }
        else
            policy.setCurrentStatus("A");


        policyRepo.save(policy);

        if(!("NB".equalsIgnoreCase(policy.getTransType()) || "SP".equalsIgnoreCase(policy.getTransType()))){
            PolicyTrans prevPolicy = policy.getPreviousTrans();
            if("CO".equalsIgnoreCase(policy.getTransType()) && prevPolicy.getPreviousTrans().getPolicyId()!=prevPolicy.getPolicyId()){
                prevPolicy.setCurrentStatus(policy.getTransType());
                PolicyTrans prevPolicy1 = prevPolicy.getPreviousTrans();
                if (!prevPolicy1.getTransType().equalsIgnoreCase("CO")) {
                    prevPolicy1.setCurrentStatus("A");
                    policyRepo.save(prevPolicy1);
                }
            }else {
                prevPolicy.setCurrentStatus(policy.getTransType());
            }
            policyRepo.save(prevPolicy);
        }

        if("RE".equalsIgnoreCase(policy.getPolRevStatus())){
            PolicyTrans prevPolicy = policy.getPreviousTrans();
            prevPolicy.setCurrentStatus(policy.getTransType());
            policyRepo.save(prevPolicy);
        }



        accountsUtilities.validatePolicyAccounts(policy);
        lifeService.allocateLifeRcptBalance(policy.getPolicyId());
        boolean medicalProduct = false;
        if(policy.getProduct().getProGroup().getPrgType()==null || !policy.getProduct().getProGroup().getPrgType().equalsIgnoreCase("MD")){
            medicalProduct = false;
        }
        else if(policy.getProduct().getProGroup().getPrgType().equalsIgnoreCase("MD")){
            medicalProduct=true;
        }
        try {
            Map<String, Object> processVariables = Maps.newHashMap();
            processVariables.put("confirmAuth", true);
            workflowService.completeTask(String.valueOf(polCode), processVariables, policy, DocType.GEN_UW_DOCUMENT, (medicalProduct) ? "Y" : "N", null, null, null);
        }
        catch (Exception e){
            throw new BadRequestException("Authorize Checks first....");
        }

    }

    private BigDecimal sign(String type){
        return ("C".equalsIgnoreCase(type)?BigDecimal.ONE.multiply(BigDecimal.valueOf(-1)):BigDecimal.ONE.multiply(BigDecimal.valueOf(1)));
    }

    @Override
    @Modifying
    @Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
    public void authorizePolicy(Long polCode, User user) throws BadRequestException {
        PolicyTrans policy = policyRepo.findOne(polCode);
        boolean cashBasis = policy.getInterfaceType()!=null && "C".equalsIgnoreCase(policy.getInterfaceType());
        if(policy==null) throw new BadRequestException("Invalid Policy to authorize");

        if(!"R".equalsIgnoreCase(policy.getAuthStatus())){
            throw new BadRequestException("Can only authorize ready policies");
        }
        long riskcount = riskRepo.count(QRiskTrans.riskTrans.policy.policyId.eq(polCode));

        if(riskcount==0) throw new BadRequestException("Cannot Authorize Transaction Without Risk Details");

        Iterable<RiskTrans> risks = riskRepo.findAll(QRiskTrans.riskTrans.policy.policyId.eq(polCode));

        long checkCount = transChecksRepo.count(QTransChecks.transChecks.policyTrans.policyId.eq(polCode).and(QTransChecks.transChecks.authorised.eq("N")));

        if(checkCount > 0){
            throw new BadRequestException("Cannot Authorize when there is unauthorized checks....");
        }
        BigDecimal prems = (policy.getPremium()==null)?BigDecimal.ZERO:policy.getPremium();
        if(cashBasis && prems.compareTo(BigDecimal.ZERO)==1) {
            if (cashBasisBalance(polCode).compareTo(BigDecimal.ZERO) > 0) {
                throw new BadRequestException("Cannot authorize the Transaction..This is a cash basis transaction with balance of " + cashBasisBalance(polCode));
            }
        }

        Date polWef = dateUtils.removeTime(policy.getWefDate());
        Date polWet = dateUtils.removeTime(policy.getWetDate());
        for(RiskTrans riskBean:risks){
            long sectCount = sectionRepo.count(QSectionTrans.sectionTrans.risk.riskId.eq(riskBean.getRiskId()));
            if(sectCount==0) throw new BadRequestException("Risk "+riskBean.getRiskShtDesc()+" has no sections..Cannot Authorize the transaction");

//            Iterable<RiskDocs> riskDocs = riskDocsRepo.findAll(QRiskDocs.riskDocs.risk.riskId.eq(riskBean.getRiskId()));
//            if(policy.getTransType()!=null && !"CN".equalsIgnoreCase(policy.getTransType())) {
//                for (RiskDocs riskDoc : riskDocs) {
//                    if (riskDoc.getCheckSum() == null || StringUtils.isBlank(riskDoc.getCheckSum())) {
//                        throw new BadRequestException(String.format("Cannot authorize policy without uploading documents for Risk %s", riskBean.getRiskShtDesc()));
//                    }
//                }
//            }

            Date riskWef = dateUtils.removeTime(riskBean.getWefDate());
            Date riskWet = dateUtils.removeTime(riskBean.getWetDate());
            if(riskWef.before(polWef) || riskWef.after(polWet)
                    || riskWet.before(polWef) || riskWet.after(polWet)){
                throw new BadRequestException("Risk Cover Dates outside Policy Cover Periods ");
            }
            if(riskBean.getBinderDetails().getBinder().getBinId()!=riskBean.getBinder().getBinId()){
                throw new BadRequestException("Cannot Make Ready...Sub Class and Cover Type Details do not match the binder selected");
            }
        }

        if(policy.getFuturePrem()!=null)
            if(policy.getFuturePrem().compareTo(BigDecimal.ZERO)==-1) throw new BadRequestException("Policy Future Annual Premium Cannot be negative...");

        String refNo =null;
        String debitCode = null;
        if(cashBasis && prems.compareTo(BigDecimal.ZERO)==1) {
            SystemTransactionsTemp systemTransactionsTemp = systemTransactionsTempRepo.findOne(QSystemTransactionsTemp.systemTransactionsTemp.policy.policyId.eq(polCode));
            if(systemTransactionsTemp==null)
                throw new BadRequestException("Unable to authorize without payment of the installment premium");
            refNo = systemTransactionsTemp.getRefNo();
            debitCode = systemTransactionsTemp.getTransType();
        }
        else {
            Predicate seqPredicate = QSystemSequence.systemSequence.transType.eq("D");
            if (sequenceRepo.count(seqPredicate) == 0)
                throw new BadRequestException("Sequence for Debit Notes has not been defined");
            SystemSequence sequence = sequenceRepo.findOne(seqPredicate);
            Long seqNumber = sequence.getNextNumber();
            String transType = policy.getTransType();
            if ("CO".equalsIgnoreCase(policy.getTransType())) {
                transType = policy.getPreviousTrans().getTransType();
            }
            if ("NB".equalsIgnoreCase(transType)) {
                transType = "NB";
            } else if ("RN".equalsIgnoreCase(transType)) {
                transType = "RN";
            } else {
                transType = "EN";
            }
            if (mappingRepo.count(QTransactionMapping.transactionMapping.transType.eq(transType)) == 0)
                throw new BadRequestException("Error getting Transaction Mapping Setups..Contact System Administrator");
            TransactionMapping mapping = mappingRepo.findOne(QTransactionMapping.transactionMapping.transType.eq(transType));
            refNo = ((prems.compareTo(BigDecimal.ZERO) >= 0) ? mapping.getDebitCode() : mapping.getCreditCode()) + String.format("%05d", seqNumber);
            debitCode = ((prems.compareTo(BigDecimal.ZERO) >= 0) ? mapping.getDebitCode() : mapping.getCreditCode());
            sequence.setLastNumber(seqNumber);
            sequence.setNextNumber(seqNumber + 1);
            sequenceRepo.save(sequence);
        }

        policy.setAuthBy(user);
        policy.setAuthStatus("A");
        policy.setAuthDate(new Date());
        if("CN".equalsIgnoreCase(policy.getTransType())){
            policy.setCurrentStatus("CN");
        }if("CO".equalsIgnoreCase(policy.getTransType())){
            Iterable<PolicyRemarks> policyRemarks = policyRemarksRepo.findAll(QPolicyRemarks.policyRemarks.policy.policyId.eq(policy.getPolicyId()));
            if(policyRemarks.spliterator().getExactSizeIfKnown() == 0){
                throw new BadRequestException("Input Remarks first....");
            }
            policy.setCurrentStatus("CO");
        }
        else
            policy.setCurrentStatus("A");
        policy.setRefNo(refNo);
        policyRepo.save(policy);

        if(!("NB".equalsIgnoreCase(policy.getTransType()) || "SP".equalsIgnoreCase(policy.getTransType()))){
            PolicyTrans prevPolicy = policy.getPreviousTrans();
            if("CO".equalsIgnoreCase(policy.getTransType()) && prevPolicy.getPreviousTrans().getPolicyId()!=prevPolicy.getPolicyId()){
                prevPolicy.setCurrentStatus(policy.getTransType());
                PolicyTrans prevPolicy1 = prevPolicy.getPreviousTrans();
                if (!prevPolicy1.getTransType().equalsIgnoreCase("CO")) {
                    prevPolicy1.setCurrentStatus("A");
                    policyRepo.save(prevPolicy1);
                }
            }else {
                prevPolicy.setCurrentStatus(policy.getTransType());
            }
            policyRepo.save(prevPolicy);
        }

        if("RE".equalsIgnoreCase(policy.getPolRevStatus())){
            PolicyTrans prevPolicy = policy.getPreviousTrans();
            prevPolicy.setCurrentStatus(policy.getTransType());
            policyRepo.save(prevPolicy);
        }


        if(transRepo.count(QSystemTrans.systemTrans.policy.policyId.eq(polCode).and(QSystemTrans.systemTrans.transAuthorised.eq("N"))) > 1){
            throw new BadRequestException("More than Unauthorized Transactions for the Policy..Contact System Admin");
        }

        if(transRepo.count(QSystemTrans.systemTrans.policy.policyId.eq(polCode).and(QSystemTrans.systemTrans.transAuthorised.eq("N")))== 0){
            throw new BadRequestException("Not Transaction to Authorize..Restart the Endorsement");
        }

        accountsUtilities.validatePolicyAccounts(policy);

        SystemTrans transaction = transRepo.findOne(QSystemTrans.systemTrans.policy.policyId.eq(polCode).and(QSystemTrans.systemTrans.transAuthorised.eq("N")));
        transaction.setAuthBy(userUtils.getCurrentUser());
        transaction.setAuthDate(new Date());
        transaction.setTransAuthorised("Y");
        transRepo.save(transaction);
        BigDecimal basicPrem = (policy.getPremium()==null)?BigDecimal.ZERO:policy.getPremium();
        BigDecimal extras = (policy.getExtras()==null)?BigDecimal.ZERO:policy.getExtras();
        BigDecimal phcf = (policy.getPhcf()==null)?BigDecimal.ZERO:policy.getPhcf();
        BigDecimal tl = (policy.getTrainingLevy()==null)?BigDecimal.ZERO:policy.getTrainingLevy();
        BigDecimal sd = (policy.getStampDuty()==null)?BigDecimal.ZERO:policy.getStampDuty();
        BigDecimal amountWithTaxes =basicPrem.add(extras).add(phcf).add(tl).add(sd);

        SystemTransactions savedAgentTrans = null;
//       if (policy.getPolicyId()!=null) {
//            throw new BadRequestException("Refund amount="+refundAmount );
//        }
        //Client Transaction
        if(basicPrem.compareTo(BigDecimal.ZERO)!=0){
            String type = (amountWithTaxes.compareTo(BigDecimal.ZERO)==1)?"D":"C";
            SystemTransactions trans = new SystemTransactions();
            trans.setAmount(basicPrem.abs().multiply(sign(type)).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
            trans.setAuthDate(new Date());
            trans.setAuthorised("Y");
            trans.setBalance(amountWithTaxes.abs().multiply(sign(type)).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
            trans.setBranch(policy.getBranch());
            trans.setClientType("C");
            trans.setControlAcc(policy.getClient().getTenantNumber());
            trans.setClient(policy.getClient());
            trans.setCurrRate(new BigDecimal(1));
            trans.setCurrency(policy.getTransCurrency());
            trans.setNarrations("Posting client Debit Note");
            trans.setNetAmount(amountWithTaxes.abs().multiply(sign(type)).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
            trans.setOrigin("U");
            trans.setPhfund(phcf.setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
            trans.setPolicy(policy);
            trans.setRefNo(refNo);
            trans.setSd(sd.abs().multiply(sign(type)).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
            trans.setTl(tl.abs().multiply(sign(type)).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
            trans.setTransDate(new Date());
            trans.setTransdc((amountWithTaxes.compareTo(BigDecimal.ZERO)==1)?"D":"C");
            trans.setTransType(debitCode); //Should not be hardcorded
            trans.setUserAuth(user.getUsername());
            trans.setWhtx(BigDecimal.ZERO);
            trans.setExtras(extras.abs().multiply(sign(type)).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
            trans.setPostedDate(new Date());
            trans.setPostedUser(user);
            trans.setTransaction(transaction);
            SystemTransactions savedClientTrans =  sysTransRepo.save(trans);
            if("CO".equalsIgnoreCase(policy.getTransType())){
                String refno = policy.getPreviousTrans().getRefNo();
                System.out.println("Ref no "+refno);
                SystemTransactions clientTrans = sysTransRepo.findOne(QSystemTransactions.systemTransactions.refNo.eq(refno)
                        .and(QSystemTransactions.systemTransactions.transdc.eq("D"))
                        .and(QSystemTransactions.systemTransactions.transType.in("NBD","APD","RND"))
                        .and(QSystemTransactions.systemTransactions.clientType.eq("C")));
                allocationService.autoAllocateContra(savedClientTrans.getTransno(),clientTrans.getTransno());
            }

            boolean multiproduct = policyBindersRepo.count(QPolicyBinders.policyBinders.policyTrans.policyId.eq(policy.getPolicyId())) > 0;
            String agntSign = (basicPrem.compareTo(BigDecimal.ZERO) == -1) ? "D" : "C";
            if(multiproduct){
                Iterable<PolicyBinders> policyBinders = policyBindersRepo.findAll(QPolicyBinders.policyBinders.policyTrans.policyId.eq(polCode));
                int counter = 0;
                for(PolicyBinders binders:policyBinders){
                    counter++;
                    BigDecimal commamt = (binders.getCommission() == null) ? BigDecimal.ZERO : binders.getCommission();
                    BigDecimal whtx = (binders.getWhtx() == null) ? BigDecimal.ZERO : binders.getWhtx();
                    phcf = (binders.getPhcf() == null) ? BigDecimal.ZERO : binders.getPhcf();
                    tl = (binders.getTl() == null) ? BigDecimal.ZERO : binders.getTl();
                    sd = BigDecimal.ZERO;
                    BigDecimal agentAmt = (binders.getBasicPrem().abs().multiply(sign(agntSign)).add(phcf.abs().multiply(sign(agntSign))).
                            add(tl.abs().multiply(sign(agntSign))).subtract(commamt.abs().multiply(sign(agntSign))).add(whtx.abs().multiply(sign(agntSign))));
                    //Agent Transaction
                    SystemTransactions atrans = new SystemTransactions();
                    atrans.setAmount(binders.getBasicPrem().abs().multiply(sign(agntSign)).setScale(policy.getTransCurrency().getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
                    atrans.setAuthDate(new Date());
                    atrans.setAuthorised("Y");
                    atrans.setBalance(agentAmt.setScale(policy.getTransCurrency().getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
                    atrans.setBranch(policy.getBranch());
                    atrans.setClientType("A");
                    atrans.setControlAcc(binders.getBinder().getAccount().getShtDesc());
                    atrans.setAgent(binders.getBinder().getAccount());
                    atrans.setCommission(commamt.abs().multiply(sign(agntSign)).setScale(policy.getTransCurrency().getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
                    atrans.setCurrRate(new BigDecimal(1));
                    atrans.setCurrency(policy.getTransCurrency());
                    atrans.setNarrations("Posting Agent Credit Note");
                    atrans.setNetAmount(agentAmt.setScale(policy.getTransCurrency().getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
                    atrans.setOrigin("U");
                    atrans.setPhfund(phcf.abs().multiply(sign(agntSign)).setScale(policy.getTransCurrency().getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
                    atrans.setPolicy(policy);
                    atrans.setRefNo(refNo+"/"+counter);
                    atrans.setSd(sd.abs().multiply(sign(agntSign)).setScale(policy.getTransCurrency().getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
                    atrans.setTl(tl.abs().multiply(sign(agntSign)).setScale(policy.getTransCurrency().getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
                    atrans.setTransDate(new Date());
                    atrans.setTransdc(agntSign);
                    atrans.setTransType(debitCode); //Should not be hardcorded
                    atrans.setUserAuth(user.getUsername());
                    atrans.setWhtx(whtx.abs().multiply(sign(agntSign)).setScale(policy.getTransCurrency().getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
                    atrans.setExtras(extras.abs().multiply(sign(agntSign)).setScale(policy.getTransCurrency().getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
                    atrans.setPostedDate(new Date());
                    atrans.setPostedUser(user);
                    atrans.setTransaction(transaction);
                    savedAgentTrans = sysTransRepo.save(atrans);
                }
                postUwTransactions(policy, transaction, BigDecimal.ZERO);
            }
            else {


                BigDecimal commamt = (policy.getCommAmt() == null) ? BigDecimal.ZERO : policy.getCommAmt();
                BigDecimal whtx = (policy.getWhtx() == null) ? BigDecimal.ZERO : policy.getWhtx();
                BigDecimal agentAmt = (basicPrem.abs().multiply(sign(agntSign)).add(extras.abs().multiply(sign(agntSign))).add(phcf.abs().multiply(sign(agntSign))).
                        add(tl.abs().multiply(sign(agntSign))).add(sd.abs().multiply(sign(agntSign))).subtract(commamt.abs().multiply(sign(agntSign))).add(whtx.abs().multiply(sign(agntSign))));
                //Agent Transaction
                SystemTransactions atrans = new SystemTransactions();
                atrans.setAmount(basicPrem.abs().multiply(sign(agntSign)).setScale(policy.getTransCurrency().getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
                atrans.setAuthDate(new Date());
                atrans.setAuthorised("Y");
                atrans.setBalance(agentAmt.setScale(policy.getTransCurrency().getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
                atrans.setBranch(policy.getBranch());
                atrans.setClientType("A");
                atrans.setControlAcc(policy.getAgent().getShtDesc());
                atrans.setAgent(policy.getAgent());
                atrans.setCommission(commamt.abs().multiply(sign(agntSign)).setScale(policy.getTransCurrency().getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
                atrans.setCurrRate(new BigDecimal(1));
                atrans.setCurrency(policy.getTransCurrency());
                atrans.setNarrations("Posting Agent Credit Note");
                atrans.setNetAmount(agentAmt.setScale(policy.getTransCurrency().getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
                atrans.setOrigin("U");
                atrans.setPhfund(phcf.abs().multiply(sign(agntSign)).setScale(policy.getTransCurrency().getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
                atrans.setPolicy(policy);
                atrans.setRefNo(refNo);
                atrans.setSd(sd.abs().multiply(sign(agntSign)).setScale(policy.getTransCurrency().getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
                atrans.setTl(tl.abs().multiply(sign(agntSign)).setScale(policy.getTransCurrency().getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
                atrans.setTransDate(new Date());
                atrans.setTransdc(agntSign);
                atrans.setTransType(debitCode); //Should not be hardcorded
                atrans.setUserAuth(user.getUsername());
                atrans.setWhtx(whtx.abs().multiply(sign(agntSign)).setScale(policy.getTransCurrency().getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
                atrans.setExtras(extras.abs().multiply(sign(agntSign)).setScale(policy.getTransCurrency().getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
                atrans.setPostedDate(new Date());
                atrans.setPostedUser(user);
                atrans.setTransaction(transaction);
                postUwTransactions(policy, transaction, commamt);
                savedAgentTrans = sysTransRepo.save(atrans);
            }
            if(!multiproduct && "CO".equalsIgnoreCase(policy.getTransType())){
                String refno = policy.getPreviousTrans().getRefNo();
                SystemTransactions agentTrans = sysTransRepo.findOne(QSystemTransactions.systemTransactions.refNo.eq(refno)
                        .and(QSystemTransactions.systemTransactions.transdc.eq("C")));
                long authcount = auditRepo.count(QPaymentAudit.paymentAudit.transNo.transno.eq(agentTrans.getTransno()).and(QPaymentAudit.paymentAudit.posted.eq("Y")));
                if(authcount==0)
                    allocationService.autoAllocateContra(agentTrans.getTransno(),savedAgentTrans.getTransno());

                else{
                    SystemTransactions clientTrans = sysTransRepo.findOne(QSystemTransactions.systemTransactions.refNo.eq(refno)
                            .and(QSystemTransactions.systemTransactions.transdc.eq("D")));
                    Iterable<ReceiptSettlementDetails> settlements = settlementRepo.findAll(QReceiptSettlementDetails.receiptSettlementDetails.drCr.eq("C")
                            .and(QReceiptSettlementDetails.receiptSettlementDetails.debit.transno.eq(clientTrans.getTransno())));

                    BigDecimal totalReceipted = BigDecimal.ZERO;
                    for(ReceiptSettlementDetails details:settlements){
                        totalReceipted = totalReceipted.add(details.getAllocatedAmt());
                    }
                    double prorationRate = totalReceipted.doubleValue()/(agentTrans.getNetAmount().doubleValue());
                    BigDecimal prorata = new BigDecimal(prorationRate);
                    if(prorata.compareTo(BigDecimal.ONE)==1) throw new BadRequestException("Error Doing contra...Contact Admin to check the settlement details");
                    SystemTransactions revTrans = new SystemTransactions();
                    revTrans.setAmount(agentTrans.getAmount().abs().multiply(sign("D")).multiply(prorata).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
                    revTrans.setAuthDate(new Date());
                    revTrans.setAuthorised("Y");
                    revTrans.setBalance(agentTrans.getBalance().abs().multiply(sign("D")).multiply(prorata).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
                    revTrans.setBranch(policy.getBranch());
                    revTrans.setClientType("A");
                    revTrans.setControlAcc(agentTrans.getControlAcc());
                    revTrans.setAgent(agentTrans.getAgent());
                    revTrans.setCommission(agentTrans.getCommission().abs().multiply(sign("D")).multiply(prorata).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
                    revTrans.setCurrRate(new BigDecimal(1));
                    revTrans.setCurrency(policy.getTransCurrency());
                    revTrans.setNarrations("Posting Agent Debit Note");
                    revTrans.setNetAmount(agentTrans.getNetAmount().abs().multiply(sign("D")).multiply(prorata).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
                    revTrans.setOrigin("U");
                    revTrans.setPhfund(agentTrans.getPhfund().abs().multiply(sign("D")).multiply(prorata).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
                    revTrans.setPolicy(policy);
                    revTrans.setRefNo(refNo);
                    revTrans.setSd(agentTrans.getSd().abs().multiply(sign("D")).multiply(prorata).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
                    revTrans.setTl(agentTrans.getTl().abs().multiply(sign("D")).multiply(prorata).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
                    revTrans.setTransDate(new Date());
                    revTrans.setTransdc("D");
                    revTrans.setTransType(debitCode); //Should not be hardcorded
                    revTrans.setUserAuth(user.getUsername());
                    revTrans.setWhtx(agentTrans.getWhtx().abs().multiply(sign("D")).multiply(prorata).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
                    revTrans.setExtras(agentTrans.getExtras().abs().multiply(sign("D")).multiply(prorata).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
                    revTrans.setTransaction(transaction);
                    sysTransRepo.save(revTrans);
                }
            }

        }

        if(cashBasis && prems.compareTo(BigDecimal.ZERO)==1) {
            allocationService.allocateCashBasisTrans(polCode,user);
        }
        boolean medicalProduct = false;
        if(policy.getProduct().getProGroup().getPrgType()==null || !policy.getProduct().getProGroup().getPrgType().equalsIgnoreCase("MD")){
            medicalProduct = false;
        }
        else if(policy.getProduct().getProGroup().getPrgType().equalsIgnoreCase("MD")){
            medicalProduct=true;
        }
        try {
            Map<String, Object> processVariables = Maps.newHashMap();
            processVariables.put("confirmAuth", true);
            workflowService.completeTask(String.valueOf(polCode), processVariables, policy, DocType.GEN_UW_DOCUMENT, (medicalProduct) ? "Y" : "N", null, null, null);
        }
        catch (Exception e){
            throw new BadRequestException("Authorize Checks first....");
        }

    }




    @PreAuthorize("hasAnyAuthority('AUTHORIZE_POLICY')")
    @Override
    @Modifying
    @Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
    public void authorizeMedicalPolicy(Long polCode) throws BadRequestException {
        PolicyTrans policy = policyRepo.findOne(polCode);
        if(policy==null) throw new BadRequestException("Invalid Policy to authorize");

        if(!"R".equalsIgnoreCase(policy.getAuthStatus())){
            throw new BadRequestException("Can only authorize ready policies");
        }
        if(!authLimits.checkAuthorizationLimits("AUTHORIZE_POLICY",policy.getBasicPrem())){
            throw new BadRequestException("You have no rights to authorize the transaction...Check your authorization limits..");
        }

        if("CR".equalsIgnoreCase(policy.getTransType())){
            if(policy.getReissueCardFee().compareTo(BigDecimal.ZERO)==0){
                throw new BadRequestException("Cannot authorise...Reissue Charge fee is zero");
            }
        }

        long riskcount = categoryRepo.count(QMedicalCategory.medicalCategory.policy.policyId.eq(polCode));

        if(riskcount==0) throw new BadRequestException("Cannot Authorize Transaction Without Category Details");

        if(policy.getFuturePrem()!=null)
            if(policy.getFuturePrem().compareTo(BigDecimal.ZERO)==-1) throw new BadRequestException("Policy Future Annual Premium Cannot be negative...");

        long selfParamsCount = selfFundParamsRepo.count(QSelfFundParams.selfFundParams.policyTrans.policyId.eq(polCode));

        boolean selffundPolicy = (policy.getBinder().getFundBinder()!=null && "Y".equalsIgnoreCase(policy.getBinder().getFundBinder()));

        if(selffundPolicy){
            if(selfParamsCount ==0)
                throw new BadRequestException("Fund Parameters Record is Mandatory for Fund Transactions");
        }

        Iterable<MedicalCategory> risks  = categoryRepo.findAll(QMedicalCategory.medicalCategory.policy.policyId.eq(polCode));
        for(MedicalCategory category:risks){
            if(membersRepo.count(QCategoryMembers.categoryMembers.category.id.eq(category.getId()))==0){
                throw new BadRequestException("Cannot Authorize the policy...No Category Members Specified for "+category.getDesc());
            }
            Iterable<CategoryMembers> categoryMembers = membersRepo.findAll(QCategoryMembers.categoryMembers.category.id.eq(category.getId()));
            for(CategoryMembers categoryMember:categoryMembers){
                Iterable<RiskDocs> riskDocs = riskDocsRepo.findAll(QRiskDocs.riskDocs.member.sectId.eq(categoryMember.getSectId()));
                for(RiskDocs riskDoc:riskDocs){
                    if(riskDoc.getCheckSum()==null || StringUtils.isBlank(riskDoc.getCheckSum())){
                        throw new BadRequestException(String.format("Cannot authorize policy without uploading documents for Member No %s",categoryMember.getMemberShipNo()));
                    }
                }
                if (!(categoryMember.getMemberStatus().equalsIgnoreCase("D"))) {
                    categoryMember.setMemberStatus("A");
                    membersRepo.save(categoryMember);
                }

            }

        }
        BigDecimal prems = (policy.getPremium()==null)?BigDecimal.ZERO:policy.getPremium();
        BigDecimal reIssueCardFee = BigDecimal.ZERO;
        if("CR".equalsIgnoreCase(policy.getTransType())){
            prems= policy.getReissueCardFee();
             reIssueCardFee = (policy.getReissueCardFee()==null)?BigDecimal.ZERO:policy.getReissueCardFee();
        }
        Predicate seqPredicate = QSystemSequence.systemSequence.transType.eq("D");
        if (sequenceRepo.count(seqPredicate) == 0)
            throw new BadRequestException("Sequence for Debit Notes has not been defined");
        SystemSequence sequence = sequenceRepo.findOne(seqPredicate);
        Long seqNumber = sequence.getNextNumber();
        String transType = policy.getTransType();
        if("CO".equalsIgnoreCase(policy.getTransType())){
            transType = policy.getPreviousTrans().getTransType();
        }
        if("NB".equalsIgnoreCase(transType)){
            transType = "NB";
        }
        else if("RN".equalsIgnoreCase(transType)){
            transType = "RN";
        }
        else{
            transType = "EN";
        }
        if(mappingRepo.count(QTransactionMapping.transactionMapping.transType.eq(transType)) ==0)
            throw new BadRequestException("Error getting Transaction Mapping Setups..Contact System Administrator");
        TransactionMapping mapping = mappingRepo.findOne(QTransactionMapping.transactionMapping.transType.eq(transType));
        final String refNo =((prems.compareTo(BigDecimal.ZERO)>=0)?mapping.getDebitCode():mapping.getCreditCode()) + String.format("%05d", seqNumber);
        final String debitCode = ((prems.compareTo(BigDecimal.ZERO)>=0)?mapping.getDebitCode():mapping.getCreditCode());
        sequence.setLastNumber(seqNumber);
        sequence.setNextNumber(seqNumber + 1);
        sequenceRepo.save(sequence);
        if (!"CN".equalsIgnoreCase(policy.getTransType())) {
            cardsService.generateMedicalCards(polCode);
        }


        policy.setAuthBy(userUtils.getCurrentUser());
        policy.setAuthStatus("A");
        policy.setAuthDate(new Date());
        if("CN".equalsIgnoreCase(policy.getTransType())){
            policy.setCurrentStatus("CN");
        }if("CO".equalsIgnoreCase(policy.getTransType())){
            policy.setCurrentStatus("CO");
        }
        else
            policy.setCurrentStatus("A");
        policy.setRefNo(refNo);

        policyRepo.save(policy);

        if(!("NB".equalsIgnoreCase(policy.getTransType()) || "SP".equalsIgnoreCase(policy.getTransType()))){
            PolicyTrans prevPolicy = policy.getPreviousTrans();
            prevPolicy.setCurrentStatus(policy.getTransType());
            policyRepo.save(prevPolicy);
        }

        if("RE".equalsIgnoreCase(policy.getPolRevStatus())){
            PolicyTrans prevPolicy = policy.getPreviousTrans();
            prevPolicy.setCurrentStatus(policy.getTransType());
            policyRepo.save(prevPolicy);
        }


        if(transRepo.count(QSystemTrans.systemTrans.policy.policyId.eq(polCode).and(QSystemTrans.systemTrans.transAuthorised.eq("N"))) > 1){
            throw new BadRequestException("More than Unauthorized Transactions for the Policy..Contact System Admin");
        }

        if(transRepo.count(QSystemTrans.systemTrans.policy.policyId.eq(polCode).and(QSystemTrans.systemTrans.transAuthorised.eq("N")))== 0){
            throw new BadRequestException("Not Transaction to Authorize..Restart the Endorsement");
        }

        SystemTrans transaction = transRepo.findOne(QSystemTrans.systemTrans.policy.policyId.eq(polCode).and(QSystemTrans.systemTrans.transAuthorised.eq("N")));
        transaction.setAuthBy(userUtils.getCurrentUser());
        transaction.setAuthDate(new Date());
        transaction.setTransAuthorised("Y");
        transRepo.save(transaction);

        accountsUtilities.validatePolicyAccounts(policy);


        BigDecimal basicPrem = (policy.getPremium()==null)?BigDecimal.ZERO:policy.getPremium();
        BigDecimal extras = (policy.getExtras()==null)?BigDecimal.ZERO:policy.getExtras();
        BigDecimal phcf = (policy.getPhcf()==null)?BigDecimal.ZERO:policy.getPhcf();
        BigDecimal tl = (policy.getTrainingLevy()==null)?BigDecimal.ZERO:policy.getTrainingLevy();
        BigDecimal sd = (policy.getStampDuty()==null)?BigDecimal.ZERO:policy.getStampDuty();
        BigDecimal serviceCharge = (policy.getServiceCharge()==null)?BigDecimal.ZERO:policy.getServiceCharge();
        BigDecimal issueFee = (policy.getIssueCardFee()==null)?BigDecimal.ZERO:policy.getIssueCardFee();
        BigDecimal reissueFee = (policy.getReissueCardFee()==null)?BigDecimal.ZERO:policy.getReissueCardFee();
        BigDecimal vat  =(policy.getVatAmount()==null)?BigDecimal.ZERO:policy.getVatAmount();
        BigDecimal amountWithTaxes =basicPrem.add(extras).add(phcf).add(tl).add(sd).add(vat).add(serviceCharge).add(issueFee).add(reissueFee);
        String sign = (amountWithTaxes.compareTo(BigDecimal.ZERO)==1)?"D":"C";
        //Client Transaction
        if(basicPrem.compareTo(BigDecimal.ZERO)!=0 ||reIssueCardFee.compareTo(BigDecimal.ZERO)!=0 ){
            SystemTransactions trans = new SystemTransactions();
            trans.setAmount(basicPrem.abs().multiply(sign(sign)).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
            trans.setAuthDate(new Date());
            trans.setAuthorised("Y");
            trans.setBalance(amountWithTaxes.abs().multiply(sign(sign)).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
            trans.setBranch(policy.getBranch());
            trans.setClientType("C");
            trans.setControlAcc(policy.getClient().getTenantNumber());
            trans.setClient(policy.getClient());
            trans.setCurrRate(new BigDecimal(1));
            trans.setCurrency(policy.getTransCurrency());
            trans.setNarrations("Posting client Debit Note");
            trans.setNetAmount(amountWithTaxes.abs().multiply(sign(sign)).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
            trans.setOrigin("U");
            trans.setPhfund(phcf.abs().multiply(sign(sign)).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
            trans.setPolicy(policy);
            trans.setRefNo(refNo);
            trans.setSd(sd.abs().multiply(sign(sign)).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
            trans.setTl(tl.abs().multiply(sign(sign)).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
            trans.setServiceCharge(serviceCharge.abs().multiply(sign(sign)).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
            trans.setVatAmount(vat.abs().multiply(sign(sign)).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
            trans.setIssueCardFee(issueFee.abs().multiply(sign(sign)).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
            trans.setReIssueCardFee(reissueFee.abs().multiply(sign(sign)).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
            trans.setTransDate(new Date());
            trans.setTransdc((amountWithTaxes.compareTo(BigDecimal.ZERO)==1)?"D":"C");
            trans.setTransType(debitCode); //Should not be hardcorded
            trans.setUserAuth(userUtils.getCurrentUser().getUsername());
            trans.setWhtx(BigDecimal.ZERO);
            trans.setExtras(extras.abs().multiply(sign(sign)).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
            trans.setPostedDate(new Date());
            trans.setPostedUser(userUtils.getCurrentUser());
            trans.setTransaction(transaction);
            SystemTransactions savedClientTrans =  sysTransRepo.save(trans);
            if("CO".equalsIgnoreCase(policy.getTransType())){
                String refno = policy.getPreviousTrans().getRefNo();
                SystemTransactions clientTrans = sysTransRepo.findOne(QSystemTransactions.systemTransactions.refNo.eq(refno)
                        .and(QSystemTransactions.systemTransactions.transdc.eq("D")));
                allocationService.autoAllocateContra(savedClientTrans.getTransno(),clientTrans.getTransno());
            }

            String agSign = (basicPrem.compareTo(BigDecimal.ZERO)==-1)?"D":"C";
            BigDecimal commamt = (policy.getCommAmt()==null)?BigDecimal.ZERO:policy.getCommAmt().abs().multiply(sign(agSign));
            BigDecimal whtx = (policy.getWhtx()==null)?BigDecimal.ZERO:policy.getWhtx().abs().multiply(sign(agSign));
            BigDecimal agentAmt =(basicPrem.abs().multiply(sign(agSign)).add(extras.abs().multiply(sign(agSign))).add(phcf.abs().multiply(sign(agSign))).
                    add(tl.abs().multiply(sign(agSign))).add(sd.abs().multiply(sign(agSign))).subtract(commamt.abs().multiply(sign(agSign))).add(whtx.abs().multiply(sign(agSign)))
            .add(vat.abs().multiply(sign(agSign))).add(serviceCharge.abs().multiply(sign(agSign))).add(reissueFee.abs().multiply(sign(agSign))).add(issueFee.abs().multiply(sign(agSign))));

            //Agent Transaction
            SystemTransactions atrans = new SystemTransactions();
            atrans.setAmount(basicPrem.abs().multiply(sign(agSign)).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
            atrans.setAuthDate(new Date());
            atrans.setAuthorised("Y");
            atrans.setBalance(agentAmt.abs().multiply(sign(agSign)).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
            atrans.setBranch(policy.getBranch());
            atrans.setClientType("A");
            atrans.setControlAcc(policy.getAgent().getShtDesc());
            atrans.setAgent(policy.getAgent());
            atrans.setCommission(commamt.abs().multiply(sign(agSign)).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
            atrans.setCurrRate(new BigDecimal(1));
            atrans.setCurrency(policy.getTransCurrency());
            atrans.setNarrations("Posting Agent Credit Note");
            atrans.setNetAmount(agentAmt.negate().setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
            atrans.setOrigin("U");
            atrans.setPhfund(phcf.abs().multiply(sign(agSign)).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
            atrans.setPolicy(policy);
            atrans.setRefNo(refNo);
            atrans.setSd(sd.abs().multiply(sign(agSign)).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
            atrans.setTl(tl.abs().multiply(sign(agSign)).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
            trans.setServiceCharge(serviceCharge.abs().multiply(sign(agSign)).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
            trans.setVatAmount(vat.abs().multiply(sign(agSign)).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
            trans.setIssueCardFee(issueFee.abs().multiply(sign(agSign)).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
            trans.setReIssueCardFee(reissueFee.abs().multiply(sign(agSign)).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
            atrans.setTransDate(new Date());
            atrans.setTransdc(agSign);
            atrans.setTransType(debitCode); //Should not be hardcorded
            atrans.setUserAuth(userUtils.getCurrentUser().getUsername());
            atrans.setWhtx(whtx.abs().multiply(sign(agSign)).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
            atrans.setExtras(extras.abs().multiply(sign(agSign)).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
            atrans.setPostedDate(new Date());
            atrans.setPostedUser(userUtils.getCurrentUser());
            atrans.setTransaction(transaction);
            SystemTransactions savedAgentTrans = sysTransRepo.save(atrans);

            postUwTransactions(policy,transaction,commamt);

            if("CO".equalsIgnoreCase(policy.getTransType())){
                String refno = policy.getPreviousTrans().getRefNo();
                SystemTransactions agentTrans = sysTransRepo.findOne(QSystemTransactions.systemTransactions.refNo.eq(refno)
                        .and(QSystemTransactions.systemTransactions.transdc.eq("C")));
                long authcount = auditRepo.count(QPaymentAudit.paymentAudit.transNo.transno.eq(agentTrans.getTransno()).and(QPaymentAudit.paymentAudit.posted.eq("Y")));
                if(authcount==0)
                    allocationService.autoAllocateContra(agentTrans.getTransno(),savedAgentTrans.getTransno());
                else{
                    SystemTransactions clientTrans = sysTransRepo.findOne(QSystemTransactions.systemTransactions.refNo.eq(refno)
                            .and(QSystemTransactions.systemTransactions.transdc.eq("D")));
                    Iterable<ReceiptSettlementDetails> settlements = settlementRepo.findAll(QReceiptSettlementDetails.receiptSettlementDetails.drCr.eq("C")
                            .and(QReceiptSettlementDetails.receiptSettlementDetails.debit.transno.eq(clientTrans.getTransno())));

                    BigDecimal totalReceipted = BigDecimal.ZERO;
                    for(ReceiptSettlementDetails details:settlements){
                        totalReceipted = totalReceipted.add(details.getAllocatedAmt());
                    }
                    double prorationRate = totalReceipted.doubleValue()/(agentTrans.getNetAmount().doubleValue());
                    BigDecimal prorata = new BigDecimal(prorationRate);
                    if(prorata.compareTo(BigDecimal.ONE)==1) throw new BadRequestException("Error Doing contra...Contact Admin to check the settlement details");
                    SystemTransactions revTrans = new SystemTransactions();
                    revTrans.setAmount(agentTrans.getAmount().abs().multiply(sign("D")).multiply(prorata).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
                    revTrans.setAuthDate(new Date());
                    revTrans.setAuthorised("Y");
                    revTrans.setBalance(agentTrans.getBalance().abs().multiply(sign("D")).multiply(prorata).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
                    revTrans.setBranch(policy.getBranch());
                    revTrans.setClientType("A");
                    revTrans.setControlAcc(agentTrans.getControlAcc());
                    revTrans.setAgent(agentTrans.getAgent());
                    revTrans.setCommission(agentTrans.getCommission().abs().multiply(sign("D")).multiply(prorata).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
                    revTrans.setCurrRate(new BigDecimal(1));
                    revTrans.setCurrency(policy.getTransCurrency());
                    revTrans.setNarrations("Posting Agent Debit Note");
                    revTrans.setNetAmount(agentTrans.getNetAmount().abs().multiply(sign("D")).multiply(prorata).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
                    revTrans.setOrigin("U");
                    revTrans.setPhfund(agentTrans.getPhfund().abs().multiply(sign("D")).multiply(prorata).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
                    revTrans.setPolicy(policy);
                    revTrans.setRefNo(refNo);
                    revTrans.setSd(agentTrans.getSd().abs().multiply(sign("D")).multiply(prorata).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
                    revTrans.setTl(agentTrans.getTl().abs().multiply(sign("D")).multiply(prorata).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
                    revTrans.setTransDate(new Date());
                    revTrans.setTransdc("D");
                    revTrans.setTransType(debitCode); //Should not be hardcorded
                    revTrans.setUserAuth(userUtils.getCurrentUser().getUsername());
                    revTrans.setWhtx(agentTrans.getWhtx().abs().multiply(sign("D")).multiply(prorata).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
                    revTrans.setExtras(agentTrans.getExtras().abs().multiply(sign("D")).multiply(prorata).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
                    revTrans.setTransaction(transaction);
                    sysTransRepo.save(revTrans);
                }
            }
        }
        boolean medicalProduct = false;
        if(policy.getProduct().getProGroup().getPrgType()==null || !policy.getProduct().getProGroup().getPrgType().equalsIgnoreCase("MD")){
            medicalProduct = false;
        }
        else if(policy.getProduct().getProGroup().getPrgType().equalsIgnoreCase("MD")){
            medicalProduct=true;
        }
        Map<String, Object> processVariables = Maps.newHashMap();
        processVariables.put("confirmAuth",true);
        workflowService.completeTask(String.valueOf(polCode),processVariables,policy,DocType.GEN_UW_DOCUMENT,(medicalProduct)?"Y":"N",null,null,null);
    }

    @Modifying
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { BadRequestException.class })
     void postUwTransactions(PolicyTrans policy,SystemTrans transaction, BigDecimal commamt) throws BadRequestException{
        List<RiskTrans> riskTransList = riskRepo.getRiskDetails(policy.getPolicyId());
        ProductsDef product = policy.getProduct();
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
        List<BigDecimal> controlAccts = accountsRepo.getControlAccounts(policy.getAgent().getAccountType().getAccId());
        if(controlAccts.size()!=1){
            throw new BadRequestException("Control Account has not been set up. Get assistance from Administrator");
        }
        Date postDate = accountsPostingUtilities.getDefaultDate(new Date(),policy.getBranch().getObId(),Long.parseLong(new SimpleDateFormat("yyyy").format(new Date())));
        final CoaSubAccounts accounts = accountsRepo.findOne( controlAccts.get(0).longValue());
        List<GlTransactions> glTransactions = new ArrayList<>();
                for (RiskTrans riskTrans : riskTransList) {
                    if(riskTrans.getPremium()!=null && riskTrans.getPremium().compareTo(BigDecimal.ZERO)!=0){
                        if(accountsUtilities.getGlCreditAccount(RevenueItems.UP, riskTrans.getSubclass().getSubId())==null){
                            throw new BadRequestException("Unable to get Credit Account for UW Premium..");
                        }
                         GlTransactions debit = new GlTransactions();
                        debit.setAmount(riskTrans.getPremium().abs());
                        debit.setAuthDate(postDate);
                        debit.setbCuramount(riskTrans.getPremium().abs());
                        debit.setBranch(policy.getBranch());
                        debit.setCurrency(policy.getTransCurrency());
                        debit.setGlAcc(accounts);
                        debit.setGldc((riskTrans.getPremium().signum() == 1) ? "D" : "C");
                        debit.setTransaction(transaction);
                        debit.setTransLevel("U");
                        debit.setTrntCode(policy.getTransType());
                        debit.setGlYear(dateUtils.getUwYear(postDate));
                        debit.setGlMonth(dateUtils.getMonth(postDate));
                        debit.setNarration(String.format("Posting premium for Policy No: %s, Ref No: %s", policy.getPolNo(), policy.getPolRevNo()));
                        glTransactions.add(debit);
                        GlTransactions credit = new GlTransactions();
                        credit.setAmount(riskTrans.getPremium().abs());
                        credit.setAuthDate(postDate);
                        credit.setbCuramount(riskTrans.getPremium().abs());
                        credit.setBranch(policy.getBranch());
                        credit.setCurrency(policy.getTransCurrency());
                        credit.setGlAcc(accountsUtilities.getGlCreditAccount(RevenueItems.UP, riskTrans.getSubclass().getSubId()));
                        credit.setGldc((riskTrans.getPremium().signum() == 1) ? "C" : "D");
                        credit.setTransaction(transaction);
                        credit.setTransLevel("U");
                        credit.setTrntCode(policy.getTransType());
                        credit.setGlYear(dateUtils.getUwYear(postDate));
                        credit.setGlMonth(dateUtils.getMonth(postDate));
                        credit.setNarration(String.format("Posting premium for Policy No: %s, Ref No: %s", policy.getPolNo(), policy.getPolRevNo()));
                        glTransactions.add(credit);
                }
                if(riskTrans.getWhtax()!=null && riskTrans.getWhtax().compareTo(BigDecimal.ZERO)!=0){
                    if(accountsUtilities.getGlCreditAccount(RevenueItems.WHTX, riskTrans.getSubclass().getSubId())==null){
                        throw new BadRequestException("Unable to get Credit Account for UWHTX ..");
                    }
                    GlTransactions debit = new GlTransactions();
                    debit.setAmount(riskTrans.getWhtax().abs());
                    debit.setAuthDate(postDate);
                    debit.setbCuramount(riskTrans.getWhtax().abs());
                    debit.setBranch(policy.getBranch());
                    debit.setCurrency(policy.getTransCurrency());
                    debit.setGlAcc(accounts);
                    debit.setGldc((riskTrans.getWhtax().signum() == 1) ? "D" : "C");
                    debit.setTransaction(transaction);
                    debit.setTransLevel("U");
                    debit.setTrntCode(policy.getTransType());
                    debit.setGlYear(dateUtils.getUwYear(postDate));
                    debit.setGlMonth(dateUtils.getMonth(postDate));
                    debit.setNarration(String.format("Posting WHTX for Policy No: %s, Ref No: %s", policy.getPolNo(), policy.getPolRevNo()));
                    glTransactions.add(debit);
                    GlTransactions credit = new GlTransactions();
                    credit.setAmount(riskTrans.getWhtax().abs());
                    credit.setAuthDate(postDate);
                    credit.setbCuramount(riskTrans.getWhtax().abs());
                    credit.setBranch(policy.getBranch());
                    credit.setCurrency(policy.getTransCurrency());
                    credit.setGlAcc(accountsUtilities.getGlCreditAccount(RevenueItems.WHTX, riskTrans.getSubclass().getSubId()));
                    credit.setGldc((riskTrans.getWhtax().signum() == 1) ? "C" : "D");
                    credit.setTransaction(transaction);
                    credit.setTransLevel("U");
                    credit.setTrntCode(policy.getTransType());
                    credit.setGlYear(dateUtils.getUwYear(postDate));
                    credit.setGlMonth(dateUtils.getMonth(postDate));
                    credit.setNarration(String.format("Posting WHTX for Policy No: %s, Ref No: %s", policy.getPolNo(), policy.getPolRevNo()));
                    glTransactions.add(credit);
                }
                if(riskTrans.getPhfFund()!=null && riskTrans.getPhfFund().compareTo(BigDecimal.ZERO)!=0){
                    if(accountsUtilities.getGlCreditAccount(RevenueItems.PHCF, riskTrans.getSubclass().getSubId())==null){
                        throw new BadRequestException("Unable to get Credit Account for PHF Fund..");
                    }
                    GlTransactions debit = new GlTransactions();
                    debit.setAmount(riskTrans.getPhfFund().abs());
                    debit.setAuthDate(postDate);
                    debit.setbCuramount(riskTrans.getPhfFund().abs());
                    debit.setBranch(policy.getBranch());
                    debit.setCurrency(policy.getTransCurrency());
                    debit.setGlAcc(accounts);
                    debit.setGldc((policy.getPhcf().signum() == 1) ? "D" : "C");
                    debit.setTransaction(transaction);
                    debit.setTransLevel("U");
                    debit.setTrntCode(policy.getTransType());
                    debit.setGlYear(dateUtils.getUwYear(postDate));
                    debit.setGlMonth(dateUtils.getMonth(postDate));
                    debit.setNarration(String.format("Posting PHCF for Policy No: %s, Ref No: %s", policy.getPolNo(), policy.getPolRevNo()));
                    glTransactions.add(debit);
                    GlTransactions credit = new GlTransactions();
                    credit.setAmount(riskTrans.getPhfFund().abs());
                    credit.setAuthDate(postDate);
                    credit.setbCuramount(riskTrans.getPhfFund().abs());
                    credit.setBranch(policy.getBranch());
                    credit.setCurrency(policy.getTransCurrency());
                    credit.setGlAcc(accountsUtilities.getGlCreditAccount(RevenueItems.PHCF, riskTrans.getSubclass().getSubId()));
                    credit.setGldc((policy.getPhcf().signum() == 1) ? "C" : "D");
                    credit.setTransaction(transaction);
                    credit.setTransLevel("U");
                    credit.setTrntCode(policy.getTransType());
                    credit.setGlYear(dateUtils.getUwYear(postDate));
                    credit.setGlMonth(dateUtils.getMonth(postDate));
                    credit.setNarration(String.format("Posting PHCF for Policy No: %s, Ref No: %s", policy.getPolNo(), policy.getPolRevNo()));
                    glTransactions.add(credit);
                }
                if(riskTrans.getTrainingLevy()!=null && riskTrans.getTrainingLevy().compareTo(BigDecimal.ZERO)!=0){
                    if(accountsUtilities.getGlCreditAccount(RevenueItems.TL, riskTrans.getSubclass().getSubId())==null){
                        throw new BadRequestException("Unable to get Credit Account for Training Levy..");
                    }
                    boolean sign  = riskTrans.getTrainingLevy().signum()==1;
                    GlTransactions debit = new GlTransactions();
                    debit.setAmount(riskTrans.getTrainingLevy().abs());
                    debit.setAuthDate(postDate);
                    debit.setbCuramount(riskTrans.getTrainingLevy().abs());
                    debit.setBranch(policy.getBranch());
                    debit.setCurrency(policy.getTransCurrency());
                    debit.setGlAcc(accounts);
                    debit.setGldc((sign) ? "D" : "C");
                    debit.setTransaction(transaction);
                    debit.setTransLevel("U");
                    debit.setTrntCode(policy.getTransType());
                    debit.setGlYear(dateUtils.getUwYear(postDate));
                    debit.setGlMonth(dateUtils.getMonth(postDate));
                    debit.setNarration(String.format("Posting Training Levy for Policy No: %s, Ref No: %s", policy.getPolNo(), policy.getPolRevNo()));
                    glTransactions.add(debit);
                    GlTransactions credit = new GlTransactions();
                    credit.setAmount(riskTrans.getTrainingLevy().abs());
                    credit.setAuthDate(postDate);
                    credit.setbCuramount(riskTrans.getTrainingLevy().abs());
                    credit.setBranch(policy.getBranch());
                    credit.setCurrency(policy.getTransCurrency());
                    credit.setGlAcc(accountsUtilities.getGlCreditAccount(RevenueItems.TL, riskTrans.getSubclass().getSubId()));
                    credit.setGldc((sign) ? "C" : "D");
                    credit.setTransaction(transaction);
                    credit.setTransLevel("U");
                    credit.setTrntCode(policy.getTransType());
                    credit.setGlYear(dateUtils.getUwYear(postDate));
                    credit.setGlMonth(dateUtils.getMonth(postDate));
                    credit.setNarration(String.format("Posting Training Levy for Policy No: %s, Ref No: %s", policy.getPolNo(), policy.getPolRevNo()));
                    glTransactions.add(credit);
                }
                if(riskTrans.getStampDuty()!=null && riskTrans.getStampDuty().compareTo(BigDecimal.ZERO)!=0){
                    if(accountsUtilities.getGlCreditAccount(RevenueItems.SD, riskTrans.getSubclass().getSubId())==null){
                        throw new BadRequestException("Unable to get Credit Account for Stamp Duty..");
                    }
                    GlTransactions debit = new GlTransactions();
                    debit.setAmount(riskTrans.getStampDuty().abs());
                    debit.setAuthDate(postDate);
                    debit.setbCuramount(riskTrans.getStampDuty().abs());
                    debit.setBranch(policy.getBranch());
                    debit.setCurrency(policy.getTransCurrency());
                    debit.setGlAcc(accounts);
                    debit.setGldc((riskTrans.getStampDuty().signum() == 1) ? "D" : "C");
                    debit.setTransaction(transaction);
                    debit.setTransLevel("U");
                    debit.setTrntCode(policy.getTransType());
                    debit.setGlYear(dateUtils.getUwYear(postDate));
                    debit.setGlMonth(dateUtils.getMonth(postDate));
                    debit.setNarration(String.format("Posting Stamp Duty for Policy No: %s, Ref No: %s", policy.getPolNo(), policy.getPolRevNo()));
                    glTransactions.add(debit);
                    GlTransactions credit = new GlTransactions();
                    credit.setAmount(riskTrans.getStampDuty().abs());
                    credit.setAuthDate(postDate);
                    credit.setbCuramount(riskTrans.getStampDuty().abs());
                    credit.setBranch(policy.getBranch());
                    credit.setCurrency(policy.getTransCurrency());
                    credit.setGlAcc(accountsUtilities.getGlCreditAccount(RevenueItems.SD, riskTrans.getSubclass().getSubId()));
                    credit.setGldc((riskTrans.getStampDuty().signum() == 1) ? "C" : "D");
                    credit.setTransaction(transaction);
                    credit.setTransLevel("U");
                    credit.setTrntCode(policy.getTransType());
                    credit.setGlYear(dateUtils.getUwYear(postDate));
                    credit.setGlMonth(dateUtils.getMonth(postDate));
                    credit.setNarration(String.format("Posting Stamp Duty for Policy No: %s, Ref No: %s", policy.getPolNo(), policy.getPolRevNo()));
                    glTransactions.add(credit);
                }

                if(riskTrans.getExtras()!=null && riskTrans.getExtras().compareTo(BigDecimal.ZERO)!=0){
                    if(accountsUtilities.getGlCreditAccount(RevenueItems.EX, riskTrans.getSubclass().getSubId())==null){
                        throw new BadRequestException("Unable to get Credit Account for Extras..");
                    }
                    GlTransactions debit = new GlTransactions();
                    debit.setAmount(riskTrans.getExtras().abs());
                    debit.setAuthDate(postDate);
                    debit.setbCuramount(riskTrans.getExtras().abs());
                    debit.setBranch(policy.getBranch());
                    debit.setCurrency(policy.getTransCurrency());
                    debit.setGlAcc(accounts);
                    debit.setGldc((riskTrans.getExtras().signum() == 1) ? "D" : "C");
                    debit.setTransaction(transaction);
                    debit.setTransLevel("U");
                    debit.setTrntCode(policy.getTransType());
                    debit.setGlYear(dateUtils.getUwYear(postDate));
                    debit.setGlMonth(dateUtils.getMonth(postDate));
                    debit.setNarration(String.format("Posting Extras for Policy No: %s, Ref No: %s", policy.getPolNo(), policy.getPolRevNo()));
                    glTransactions.add(debit);
                    GlTransactions credit = new GlTransactions();
                    credit.setAmount(riskTrans.getExtras().abs());
                    credit.setAuthDate(postDate);
                    credit.setbCuramount(riskTrans.getExtras().abs());
                    credit.setBranch(policy.getBranch());
                    credit.setCurrency(policy.getTransCurrency());
                    credit.setGlAcc(accountsUtilities.getGlCreditAccount(RevenueItems.EX, riskTrans.getSubclass().getSubId()));
                    credit.setGldc((riskTrans.getExtras().signum() == 1) ? "C" : "D");
                    credit.setTransaction(transaction);
                    credit.setTransLevel("U");
                    credit.setTrntCode(policy.getTransType());
                    credit.setGlYear(dateUtils.getUwYear(postDate));
                    credit.setGlMonth(dateUtils.getMonth(postDate));
                    credit.setNarration(String.format("Posting Extras for Policy No: %s, Ref No: %s", policy.getPolNo(), policy.getPolRevNo()));
                    glTransactions.add(credit);
                }
                if(riskTrans.getSubAgentComm()!=null && riskTrans.getSubAgentComm().compareTo(BigDecimal.ZERO)!=0){
                    if(accountsUtilities.getGlCreditAccount(RevenueItems.SAC, riskTrans.getSubclass().getSubId())==null){
                        throw new BadRequestException("Unable to get Credit Account for Sub Agent Commission..");
                    }
                    GlTransactions debit = new GlTransactions();
                    debit.setAmount(riskTrans.getSubAgentComm().abs());
                    debit.setAuthDate(postDate);
                    debit.setbCuramount(riskTrans.getSubAgentComm().abs());
                    debit.setBranch(policy.getBranch());
                    debit.setCurrency(policy.getTransCurrency());
                    debit.setGlAcc(accounts);
                    debit.setGldc((riskTrans.getSubAgentComm().signum() == 1) ? "C" : "D");
                    debit.setTransaction(transaction);
                    debit.setTransLevel("U");
                    debit.setTrntCode(policy.getTransType());
                    debit.setGlYear(dateUtils.getUwYear(postDate));
                    debit.setGlMonth(dateUtils.getMonth(postDate));
                    debit.setNarration(String.format("Posting Sub Agent Comm for Policy No: %s, Ref No: %s", policy.getPolNo(), policy.getPolRevNo()));
                    glTransactions.add(debit);
                    GlTransactions credit = new GlTransactions();
                    credit.setAmount(riskTrans.getSubAgentComm().abs());
                    credit.setAuthDate(postDate);
                    credit.setbCuramount(riskTrans.getSubAgentComm().abs());
                    credit.setBranch(policy.getBranch());
                    credit.setCurrency(policy.getTransCurrency());
                    credit.setGlAcc(accountsUtilities.getGlCreditAccount(RevenueItems.SAC, riskTrans.getSubclass().getSubId()));
                    credit.setGldc((riskTrans.getSubAgentComm().signum() == 1) ? "D" : "C");
                    credit.setTransaction(transaction);
                    credit.setTransLevel("U");
                    credit.setTrntCode(policy.getTransType());
                    credit.setGlYear(dateUtils.getUwYear(postDate));
                    credit.setGlMonth(dateUtils.getMonth(postDate));
                    credit.setNarration(String.format("Posting  Sub Agent Comm for Policy No: %s, Ref No: %s", policy.getPolNo(), policy.getPolRevNo()));
                    glTransactions.add(credit);
                }
                if(commamt!=null && commamt.compareTo(BigDecimal.ZERO)!=0){
                    if(accountsUtilities.getGlDebitAccount(RevenueItems.UC, riskTrans.getSubclass().getSubId())==null){
                        throw new BadRequestException("Unable to get Credit Account for Commission..");
                    }
                    GlTransactions debit = new GlTransactions();
                    debit.setAmount(riskTrans.getCommAmt().abs());
                    debit.setAuthDate(postDate);
                    debit.setbCuramount(riskTrans.getCommAmt().abs());
                    debit.setBranch(policy.getBranch());
                    debit.setCurrency(policy.getTransCurrency());
                    debit.setGlAcc(accountsUtilities.getGlDebitAccount(RevenueItems.UC, riskTrans.getSubclass().getSubId()));
                    debit.setGldc((riskTrans.getCommAmt().signum() == 1) ? "C" : "D");
                    debit.setTransaction(transaction);
                    debit.setTransLevel("U");
                    debit.setTrntCode(policy.getTransType());
                    debit.setGlYear(dateUtils.getUwYear(postDate));
                    debit.setGlMonth(dateUtils.getMonth(postDate));
                    debit.setNarration(String.format("Posting Commission for Policy No: %s, Ref No: %s", policy.getPolNo(), policy.getPolRevNo()));
                    glTransactions.add(debit);
                    GlTransactions credit = new GlTransactions();
                    credit.setAmount(riskTrans.getCommAmt().abs());
                    credit.setAuthDate(postDate);
                    credit.setbCuramount(riskTrans.getCommAmt().abs());
                    credit.setBranch(policy.getBranch());
                    credit.setCurrency(policy.getTransCurrency());
                    credit.setGlAcc(accounts);
                    credit.setGldc((riskTrans.getCommAmt().signum() == 1) ? "D" : "C");
                    credit.setTransaction(transaction);
                    credit.setTransLevel("U");
                    credit.setTrntCode(policy.getTransType());
                    credit.setGlYear(dateUtils.getUwYear(postDate));
                    credit.setGlMonth(dateUtils.getMonth(postDate));
                    credit.setNarration(String.format("Posting Commission for Policy No: %s, Ref No: %s", policy.getPolNo(), policy.getPolRevNo()));
                    glTransactions.add(credit);
                }
                final BigDecimal poolPercent = riskTrans.getSubclass().getClaimPoolPercentage();
                if(poolPercent!=null && poolPercent.compareTo(BigDecimal.ZERO)> 0){
                    if(accountsUtilities.getGlDebitAccount(RevenueItems.CPL, riskTrans.getSubclass().getSubId())==null){
                        throw new BadRequestException("Unable to get Debit Account for Claim pool Premium..");
                    }
                    if(accountsUtilities.getGlCreditAccount(RevenueItems.CPL, riskTrans.getSubclass().getSubId())==null){
                        throw new BadRequestException("Unable to get Credit Account for Claim pool Premium..");
                    }
                    BigDecimal poolAmt = (poolPercent.multiply(riskTrans.getPremium())).divide(BigDecimal.valueOf(100L), RoundingMode.FLOOR);
                    GlTransactions debit = new GlTransactions();
                    debit.setAmount(poolAmt.abs());
                    debit.setAuthDate(postDate);
                    debit.setbCuramount(poolAmt.abs());
                    debit.setBranch(policy.getBranch());
                    debit.setCurrency(policy.getTransCurrency());
                    debit.setGlAcc(accountsUtilities.getGlCreditAccount(RevenueItems.CPL, riskTrans.getSubclass().getSubId()));
                    debit.setGldc((riskTrans.getPremium().signum() == 1) ? "C" : "D");
                    debit.setTransaction(transaction);
                    debit.setTransLevel("U");
                    debit.setTrntCode(policy.getTransType());
                    debit.setGlYear(dateUtils.getUwYear(postDate));
                    debit.setGlMonth(dateUtils.getMonth(postDate));
                    debit.setNarration(String.format("Posting Claim Pool for Policy No: %s, Ref No: %s", policy.getPolNo(), policy.getPolRevNo()));
                    glTransactions.add(debit);
                    GlTransactions credit = new GlTransactions();
                    credit.setAmount(poolAmt.abs());
                    credit.setAuthDate(postDate);
                    credit.setbCuramount(poolAmt.abs());
                    credit.setBranch(policy.getBranch());
                    credit.setCurrency(policy.getTransCurrency());
                    credit.setGlAcc(accountsUtilities.getGlDebitAccount(RevenueItems.CPL, riskTrans.getSubclass().getSubId()));
                    credit.setGldc((riskTrans.getPremium().signum() == 1) ? "D" : "C");
                    credit.setTransaction(transaction);
                    credit.setTransLevel("U");
                    credit.setTrntCode(policy.getTransType());
                    credit.setGlYear(dateUtils.getUwYear(postDate));
                    credit.setGlMonth(dateUtils.getMonth(postDate));
                    credit.setNarration(String.format("Posting Claim Pool for Policy No: %s, Ref No: %s", policy.getPolNo(), policy.getPolRevNo()));
                    glTransactions.add(credit);
                }
           }
        glTransRepo.save(glTransactions);
    }

    private BigDecimal cashBasisBalance(Long polCode){
        SystemTransactionsTemp systemTransactionsTemp = systemTransactionsTempRepo.findOne(QSystemTransactionsTemp.systemTransactionsTemp.policy.policyId.eq(polCode));
        if(systemTransactionsTemp!=null && systemTransactionsTemp.getBalance().compareTo(BigDecimal.ZERO)==1){
            return systemTransactionsTemp.getBalance();
        }
        return BigDecimal.ZERO;
    }


}