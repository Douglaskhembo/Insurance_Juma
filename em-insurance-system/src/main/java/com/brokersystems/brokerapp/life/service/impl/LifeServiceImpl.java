package com.brokersystems.brokerapp.life.service.impl;

import com.brokersystems.brokerapp.accounts.service.AccountsService;
import com.brokersystems.brokerapp.life.model.QLifeCommissionRates;
import com.brokersystems.brokerapp.life.repository.*;
import com.brokersystems.brokerapp.life.model.PolicyBeneficiaries;
import com.brokersystems.brokerapp.life.model.PolicyBenefitsDistribution;
import com.brokersystems.brokerapp.life.model.QPolicyBeneficiaries;
import com.brokersystems.brokerapp.life.model.*;
import com.brokersystems.brokerapp.life.service.LifeService;
import com.brokersystems.brokerapp.server.datatables.DataTablesRequest;
import com.brokersystems.brokerapp.server.datatables.DataTablesResult;
import com.brokersystems.brokerapp.server.exception.BadRequestException;
import com.brokersystems.brokerapp.server.utils.DateUtilities;
import com.brokersystems.brokerapp.server.utils.UserUtils;
import com.brokersystems.brokerapp.setup.model.*;
import com.brokersystems.brokerapp.setup.repository.*;
import com.brokersystems.brokerapp.trans.model.SystemTrans;
import com.brokersystems.brokerapp.trans.model.SystemTransactions;
import com.brokersystems.brokerapp.trans.repository.SystemTransRepo;
import com.brokersystems.brokerapp.trans.service.AllocationService;
import com.brokersystems.brokerapp.uw.model.*;
import com.brokersystems.brokerapp.uw.repository.PolicyQuestionnaireRepo;
import com.brokersystems.brokerapp.uw.repository.PolicyTransRepo;
import com.brokersystems.brokerapp.uw.repository.SectionTransRepo;
import com.brokersystems.brokerapp.uw.service.PremComputeService;
import com.mysema.query.types.expr.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



/**
 * Created by waititu on 25/11/2017.
 */
@Service
public class LifeServiceImpl implements LifeService {

    @Autowired
    private BindersRepo bindersRepo;

    @Autowired
    private ReceiptAllocationCommissionsRepo lifeReceiptCommRepo;

    @Autowired
    private PolicyTransRepo policyRepo;


    @Autowired
    private PolicyBeneficiariesRepo beneficiariesRepo;

    @Autowired
    private PolicyBenefitsDistributionRepo maturityRepo;

    @Autowired
    private LifeCommissionRatesRepo lifeCommissionRatesRepo;

    @Autowired
    private LifeReceiptAllocationsRepo lifeallocationsRepo;

    @Autowired
    private PremRatesRepo premRatesRepo;

    @Autowired
    private  ReceiptAllocationCommissionsRepo allocationCommissionsRepo;

    @Autowired
    private SystemTransRepo transRepo;

    @Autowired
    private AccountsService accountsService;

    @Autowired
    private  PolicyQuestionnaireRepo policyQuestionnaireRepo;





    @Autowired
    private UserUtils userUtils;

    @Autowired
    private DateUtilities dateUtilities;

    @Autowired
    private SectionTransRepo sectionRepo;

    @Autowired
    private LifeReceiptsRepo lifeReceiptsRepo;

    @Autowired
    private AllocationService allocationService;

    @Override
    @Transactional(readOnly = true)
    public List<BinderPolTerms> getPolTerms(Long binCode) throws BadRequestException {
        BindersDef binder=bindersRepo.findOne(binCode);
        Integer minTerm = binder.getMinTerm();
        Integer maxTerm =binder.getMaxTerm();
        List<BinderPolTerms> polTerms = new ArrayList<>();
        while (minTerm<=maxTerm){
            polTerms.add(new BinderPolTerms(minTerm,minTerm));
            minTerm++;
        }
        return polTerms;
    }

//    @Override
//    public DataTablesResult<LifeReceiptCommissions> findAllReceipts(DataTablesRequest request, Long polCode) throws IllegalAccessException {
//        BooleanExpression pred = QLifeReceiptCommissions.lifeReceiptCommissions.policyTrans.policyId.eq(polCode);
//        Page<LifeReceiptCommissions> page = lifeReceiptCommRepo.findAll(pred.and(request.searchPredicate(QLifeReceiptCommissions.lifeReceiptCommissions)), request);
//        return new DataTablesResult<>(request, page);
//    }
    public void deletePolBeneficiary(Long benCode) {
        beneficiariesRepo.delete(benCode);
    }

    @Override
    public void deleteCommissionRates(Long commId) {
        lifeCommissionRatesRepo.delete(commId);
    }

    @Override
    public DataTablesResult<PolicyBeneficiaries> findPolBeneficiaries(DataTablesRequest request, Long polCode) throws IllegalAccessException {
        BooleanExpression pred = QPolicyBeneficiaries.policyBeneficiaries.policy.policyId.eq(polCode);
        Page<PolicyBeneficiaries>  page = beneficiariesRepo.findAll(pred.and(request.searchPredicate(QPolicyBeneficiaries.policyBeneficiaries)),request);
        return new DataTablesResult<>(request, page);
    }

    @Override
    public DataTablesResult<LifeReceipts> findPolReceipts(DataTablesRequest request, Long polCode) throws IllegalAccessException{
        BooleanExpression pred =QLifeReceipts.lifeReceipts.policyTrans.policyId.eq(polCode);
                Page<LifeReceipts> page = lifeReceiptsRepo.findAll(pred.and(request.searchPredicate(QLifeReceipts.lifeReceipts)), request);
        return new DataTablesResult<>(request,page);
    }

    @Override
    public DataTablesResult<LifeReceiptAllocations> findReceiptsAllocations(DataTablesRequest request, Long receiptCode) throws IllegalAccessException{
//        BooleanExpression pred =QLifeReceiptAllocations.lifeReceiptAllocations.lifeReceipts.receiptTrans.receiptId.eq(receiptCode);
        Page<LifeReceiptAllocations> page = lifeallocationsRepo.findAll((request.searchPredicate(QLifeReceiptAllocations.lifeReceiptAllocations)), request);
        return new DataTablesResult<>(request,page);
    }

    @Override
    public DataTablesResult<ReceiptAllocationCommissions> findReceiptsCommissions(DataTablesRequest request, Long receiptCode) throws IllegalAccessException {
        BooleanExpression pred =QReceiptAllocationCommissions.receiptAllocationCommissions.lifeReceipts.receiptTrans.receiptId.eq(receiptCode);
        Page<ReceiptAllocationCommissions> page = allocationCommissionsRepo.findAll(pred.and(request.searchPredicate(QReceiptAllocationCommissions.receiptAllocationCommissions)),request);
        System.out.println(page.getTotalElements());
        return new DataTablesResult<>(request,page);
    }

    @Override
    public DataTablesResult<ReceiptAllocationCommissions> findAllocationComm(DataTablesRequest request, Long allocId) throws IllegalAccessException{
       // BooleanExpression pred =QReceiptAllocationCommissions.receiptAllocationCommissions.allocations.allocId.eq(allocId);
       // Page<ReceiptAllocationCommissions> page = allocationCommissionsRepo.findAll(pred.and(request.searchPredicate(QReceiptAllocationCommissions.receiptAllocationCommissions)), request);
        List<Object[]> com  = allocationCommissionsRepo.findAllocCommisions(allocId);
        List<ReceiptAllocationCommissions> comms = new ArrayList<>();
        for (Object[] com1:com){
            ReceiptAllocationCommissions allocComm = new ReceiptAllocationCommissions();
            if (com1[0] instanceof BigInteger){
                allocComm.setAllocCommId(((BigInteger)com1[0]).longValue());
                allocComm.setAllocations(lifeallocationsRepo.findOne(((BigInteger)com1[1]).longValue()));
                allocComm.setPremRatesDef(premRatesRepo.findOne(((BigInteger)com1[8]).longValue()));




            }
            if (com1[0] instanceof BigDecimal){
                allocComm.setAllocCommId(((BigDecimal)com1[0]).longValue());
                allocComm.setAllocations(lifeallocationsRepo.findOne(((BigDecimal)com1[1]).longValue()));
                allocComm.setPremRatesDef(premRatesRepo.findOne(((BigDecimal)com1[8]).longValue()));
            }
            allocComm.setSectionPrem((BigDecimal)com1[2]);
            allocComm.setCommissionAmt((BigDecimal)com1[3]);
            allocComm.setSectionAlloc((BigDecimal)com1[4]);
            allocComm.setCoverPremium((BigDecimal)com1[5]);
            allocComm.setCommRate((BigDecimal)com1[6]);
            allocComm.setCommDivfact((BigDecimal)com1[6]);


            comms.add(allocComm);
        }
        Page<ReceiptAllocationCommissions> page = new PageImpl<ReceiptAllocationCommissions>(comms);
        return new DataTablesResult<>(request,page);
    }


    @Override
    public DataTablesResult<PolicyBenefitsDistribution> findPolBenefits(DataTablesRequest request, Long polCode) throws IllegalAccessException {
        BooleanExpression pred = QPolicyBenefitsDistribution.policyBenefitsDistribution.policyId.policyId.eq(polCode);
        Page<PolicyBenefitsDistribution>  page = maturityRepo.findAll(pred.and(request.searchPredicate(QPolicyBenefitsDistribution.policyBenefitsDistribution)),request);
        return new DataTablesResult<>(request, page);
    }

    @Override
    public DataTablesResult<PolicyQuestionnaire> findPolQuiz(DataTablesRequest request, Long polCode) throws IllegalAccessException {
        BooleanExpression pred = QPolicyQuestionnaire.policyQuestionnaire.policy.policyId.eq(polCode);
        Iterable<PolicyQuestionnaire> policyQuestionnaires = policyQuestionnaireRepo.findAll(pred);
        List<PolicyQuestionnaire> quiz = new ArrayList<>();
        for (PolicyQuestionnaire polquiz:policyQuestionnaires){
            PolicyQuestionnaire polquiz1 = new PolicyQuestionnaire();
            if (polquiz.getChoice()!=null){
                polquiz1 =polquiz;
                quiz.add(polquiz1);
            }

        }
        Page<PolicyQuestionnaire>  page = new PageImpl<PolicyQuestionnaire>(quiz);
        return new DataTablesResult<>(request, page);
    }

    @Override
    public Iterable<PolicyQuestionnaire> findPolQuizList(Long polCode) throws IllegalAccessException {
        BooleanExpression pred = QPolicyQuestionnaire.policyQuestionnaire.policy.policyId.eq(polCode);
        Iterable<PolicyQuestionnaire> policyQuestionnaires = policyQuestionnaireRepo.findAll(pred);


        return policyQuestionnaires;
    }


    @Override
    @Transactional(readOnly = false)
    public void definePolicyBeneficiary(PolicyBeneficiaries beneficiaries ) throws BadRequestException {
        if(beneficiaries.getDateRegistered().after(new Date())){
            throw new BadRequestException("Date of Birth cannot be after today..");
        }
        beneficiaries.setDateCreated(new Date());
        beneficiaries.setUser(userUtils.getCurrentUser());
        beneficiaries.setPolicy(policyRepo.findOne(beneficiaries.getPolicy().getPolicyId()));
        beneficiariesRepo.save(beneficiaries);
    }

    @Override
    @Modifying
    @Transactional(readOnly = false, rollbackFor = { BadRequestException.class }, propagation = Propagation.REQUIRED)
    public void allocateLifeRcptComm(LifeReceiptAllocations savedAlloc,int paidInsts) throws BadRequestException {
        if (savedAlloc!=null){
            PolicyTrans policyTrans =  savedAlloc.getLifeReceipts().getPolicyTrans();
            double paidYears = Math.ceil(((double) paidInsts/12));
            Iterable<SectionTrans> sections = sectionRepo.findAll(QSectionTrans.sectionTrans.risk.policy.policyId.eq(policyTrans.getPolicyId()));
            List<ReceiptAllocationCommissions> comms = new ArrayList<>();
            BigDecimal totComm = new BigDecimal(BigInteger.ZERO);
            for (SectionTrans section :sections){
                if (lifeCommissionRatesRepo.count(QLifeCommissionRates.lifeCommissionRates.premRatesDef.eq(section.getPremRates())
                        .and(QLifeCommissionRates.lifeCommissionRates.commTermFrom.loe(policyTrans.getPolTerm()))
                        .and(QLifeCommissionRates.lifeCommissionRates.commTermTo.goe(policyTrans.getPolTerm()))
                        .and(QLifeCommissionRates.lifeCommissionRates.wefDate.loe(policyTrans.getCoverFrom()))
                        .and(QLifeCommissionRates.lifeCommissionRates.wetDate.goe(policyTrans.getCoverFrom()).or(QLifeCommissionRates.lifeCommissionRates.wetDate.isNull()))
                        .and(QLifeCommissionRates.lifeCommissionRates.commYearFrom.loe(paidYears))
                        .and(QLifeCommissionRates.lifeCommissionRates.frequency.equalsIgnoreCase(policyTrans.getFrequency()))
                        .and(QLifeCommissionRates.lifeCommissionRates.commYearTo.goe(paidYears)))<=0) {
                    throw new BadRequestException("No commission rate define...."+policyTrans.getPolTerm() +";"+section.getPremRates().getId()+";"+policyTrans.getCoverFrom()+";"+policyTrans.getCoverFrom()+";"+paidYears+";"+policyTrans.getFrequency());

                }
                if (lifeCommissionRatesRepo.count(QLifeCommissionRates.lifeCommissionRates.premRatesDef.eq(section.getPremRates())
                        .and(QLifeCommissionRates.lifeCommissionRates.commTermFrom.loe(policyTrans.getPolTerm()))
                        .and(QLifeCommissionRates.lifeCommissionRates.commTermTo.goe(policyTrans.getPolTerm()))
                        .and(QLifeCommissionRates.lifeCommissionRates.wefDate.loe(policyTrans.getCoverFrom()))
                        .and(QLifeCommissionRates.lifeCommissionRates.wetDate.goe(policyTrans.getCoverFrom()).or(QLifeCommissionRates.lifeCommissionRates.wetDate.isNull()))
                        .and(QLifeCommissionRates.lifeCommissionRates.commYearFrom.loe(paidYears))
                        .and(QLifeCommissionRates.lifeCommissionRates.frequency.equalsIgnoreCase(policyTrans.getFrequency()))
                        .and(QLifeCommissionRates.lifeCommissionRates.commYearTo.goe(paidYears)))>1) {
                    throw new BadRequestException("More than one commission rate exists....");
                }

                LifeCommissionRates commissionRates = lifeCommissionRatesRepo.findOne(QLifeCommissionRates.lifeCommissionRates.premRatesDef.eq(section.getPremRates())
                        .and(QLifeCommissionRates.lifeCommissionRates.commTermFrom.loe(policyTrans.getPolTerm()))
                        .and(QLifeCommissionRates.lifeCommissionRates.commTermTo.goe(policyTrans.getPolTerm()))
                        .and(QLifeCommissionRates.lifeCommissionRates.wefDate.loe(policyTrans.getCoverFrom()))
                        .and(QLifeCommissionRates.lifeCommissionRates.wetDate.goe(policyTrans.getCoverFrom()).or(QLifeCommissionRates.lifeCommissionRates.wetDate.isNull()))
                        .and(QLifeCommissionRates.lifeCommissionRates.commYearFrom.loe(paidYears))
                        .and(QLifeCommissionRates.lifeCommissionRates.frequency.equalsIgnoreCase(policyTrans.getFrequency()))
                        .and(QLifeCommissionRates.lifeCommissionRates.commYearTo.goe(paidYears)));

                System.out.println("commissionRates="+commissionRates);
                if (commissionRates!=null) {
                    ReceiptAllocationCommissions com = new ReceiptAllocationCommissions();
                    BigDecimal commRate = commissionRates.getCommRate();
                    BigDecimal comm = BigDecimal.ZERO;
                    BigDecimal sectionAlloc = BigDecimal.ZERO;
                    if (section.getPrem()!=null) {
                        sectionAlloc = savedAlloc.getAllocAmount().multiply(section.getPrem()).divide(savedAlloc.getInstalmentPremium()).setScale(2, BigDecimal.ROUND_HALF_EVEN);
                        comm = (commRate.multiply(sectionAlloc).divide(commissionRates.getCommDivFactor())).setScale(2, BigDecimal.ROUND_HALF_EVEN);
                    }

                    totComm = totComm.add(comm);
                    com.setLifeReceipts(savedAlloc.getLifeReceipts());
                    com.setAllocations(savedAlloc);
                    com.setCommDivfact(commissionRates.getCommDivFactor());
                    com.setCommissionAmt(comm);
                    com.setCommissionRates(commissionRates);
                    com.setCommRate(commissionRates.getCommRate());
                    com.setPremRatesDef(section.getPremRates());
                    com.setSectionTrans(section);
                    com.setCoverPremium(section.getPrem());
                    com.setSectionAlloc(sectionAlloc);
                    comms.add(com);
                }

            }
            lifeReceiptCommRepo.save(comms);
            savedAlloc.setCommAmount(totComm);
            lifeallocationsRepo.save(savedAlloc);
        }
    }

    @Override
    @Modifying
    @Transactional(readOnly = false, rollbackFor = { BadRequestException.class }, propagation = Propagation.REQUIRED)
    public void deallocateLifeRcpt(LifeReceipts lifeReceipts, User user) throws BadRequestException {

        if (lifeReceipts ==null) {
            throw new BadRequestException("No Receipt transaction Selected....");
        }

        LifeReceipts originalRcpt = lifeReceiptsRepo.findOne(lifeReceipts.getOriginalLifeReceipts().getReceiptId());
        System.out.println("Policy="+lifeReceipts.getPolicyTrans().getPolicyId());
        PolicyTrans policyTrans = lifeReceipts.getPolicyTrans();
        int fullInst=0;
        if(originalRcpt.getAllocatedAmt()==null || originalRcpt.getAllocatedAmt().compareTo(BigDecimal.ZERO)==0){
            return;
        }
        if (lifeReceipts.getDrCr().equalsIgnoreCase("D")){
           // int  fullInsts = (totBalance.divide(policyTrans.getNetPrem(),BigDecimal.ROUND_FLOOR)).intValue();
            fullInst = (originalRcpt.getAllocatedAmt().divide(lifeReceipts.getPolicyTrans().getNetPrem(),BigDecimal.ROUND_FLOOR)).intValue();

            if (fullInst>0) { // allocates only full instalments
                int counter=0;
                int paidInsts =policyTrans.getPaidInsts();
                int prevpaidInsts =0;

                while (counter<fullInst && policyTrans.getPaidInsts()<policyTrans.getTotalInstalments()){
                    counter++;
                    LifeReceiptAllocations alloc = new LifeReceiptAllocations();
                    alloc.setAllocDate(new Date());
                    alloc.setInstalmentPremium(policyTrans.getNetPrem());
                    alloc.setLifeReceipts(lifeReceipts);
                    alloc.setAllocAmount(policyTrans.getNetPrem());
                    alloc.setInstallNo(paidInsts);
                    alloc.setDoneDate(new Date());
                    alloc.setDoneBy(user);
                    alloc.setPaidToDate(dateUtilities.getPaidToDate(policyTrans.getCoverFrom(),(paidInsts-1)));
                    LifeReceiptAllocations savedAlloc = lifeallocationsRepo.save(alloc);
                    //lifeReceipts.setBalanceAmt(lifeReceipts.getBalanceAmt().subtract(policyTrans.getNetPrem()));
                    if (lifeReceipts.getAllocatedAmt()!=null) {
                        lifeReceipts.setAllocatedAmt(lifeReceipts.getAllocatedAmt().add(savedAlloc.getAllocAmount()));
                    }else {
                        lifeReceipts.setAllocatedAmt(savedAlloc.getAllocAmount());
                    }
                    lifeReceiptsRepo.save(lifeReceipts);
                    prevpaidInsts= paidInsts;
                    paidInsts--;
                    policyTrans.setPaidInsts(paidInsts);
                    policyTrans.setPolPaidToDate( dateUtilities.getPaidToDate(policyTrans.getCoverFrom(),paidInsts) );
                    //System.out.println("xxx="+policyTrans);
                    policyTrans=policyRepo.save(policyTrans);
                    allocateLifeRcptComm(savedAlloc,prevpaidInsts);

                }

            }

        }
    }

    @Override
    @Modifying
    @Transactional(readOnly = false, rollbackFor = { BadRequestException.class }, propagation = Propagation.REQUIRED)
    public void allocateLifeRcpt(LifeReceipts lifeReceipts) throws BadRequestException {

        if (lifeReceipts ==null) {
            throw new BadRequestException("No Receipt transaction Selected....");
        }
        System.out.println("Policy="+lifeReceipts.getPolicyTrans().getPolicyId());
        PolicyTrans policyTrans = lifeReceipts.getPolicyTrans();
        int fullInst=0;
        if(lifeReceipts.getBalanceAmt()==null || lifeReceipts.getBalanceAmt().compareTo(BigDecimal.ZERO)==0){
            throw new BadRequestException("Receipt transaction Selected is fully allocated....");
        }
        if (lifeReceipts.getDrCr().equalsIgnoreCase("C")){
            fullInst = (lifeReceipts.getBalanceAmt().divide(lifeReceipts.getPolicyTrans().getNetPrem())).intValue();

            if (fullInst>0) { // allocates only full instalments
                int counter=0;
                int paidInsts =0;
                if (policyTrans.getPaidInsts()!=null ) {
                    paidInsts =policyTrans.getPaidInsts();
                }else
                policyTrans.setPaidInsts(0);

                while (counter<fullInst && policyTrans.getPaidInsts()<policyTrans.getTotalInstalments()){
                    counter++;
                    paidInsts++;
                    LifeReceiptAllocations alloc = new LifeReceiptAllocations();
                    alloc.setAllocDate(new Date());
                    alloc.setInstalmentPremium(policyTrans.getNetPrem());
                    alloc.setLifeReceipts(lifeReceipts);
                    alloc.setAllocAmount(policyTrans.getNetPrem());
                    alloc.setPaidToDate(dateUtilities.getPaidToDate(policyTrans.getCoverFrom(),paidInsts));
                    alloc.setInstallNo(paidInsts);
                    alloc.setDoneDate(new Date());
                    alloc.setDoneBy(userUtils.getCurrentUser());
                    LifeReceiptAllocations savedAlloc = lifeallocationsRepo.save(alloc);
                    lifeReceipts.setBalanceAmt(lifeReceipts.getBalanceAmt().subtract(policyTrans.getNetPrem()));
                    if (lifeReceipts.getAllocatedAmt()!=null) {
                        lifeReceipts.setAllocatedAmt(lifeReceipts.getAllocatedAmt().add(savedAlloc.getAllocAmount()));
                    }else {
                        lifeReceipts.setAllocatedAmt(savedAlloc.getAllocAmount());
                    }
                    lifeReceiptsRepo.save(lifeReceipts);
                    allocateLifeRcptComm(savedAlloc,paidInsts);
                    policyTrans.setPaidInsts(paidInsts);
                    policyTrans.setPolPaidToDate( dateUtilities.getPaidToDate(policyTrans.getCoverFrom(),paidInsts) );
                    //System.out.println("xxx="+policyTrans);
                    policyTrans=policyRepo.save(policyTrans);

                }

            }

        }
    }

    @Override
    @Modifying
    @Transactional(readOnly = false, rollbackFor = { BadRequestException.class }, propagation = Propagation.REQUIRED)
    public void settlePartialInstalment(LifeReceipts lifeReceipts ,
                                        BigDecimal allocAmount,
                                        int paidInsts ,
                                        String refNo,
                                        SystemTrans trans) throws BadRequestException {
        if (lifeReceipts ==null) {
            throw new BadRequestException("No Receipt transaction Selected....");
        }
        PolicyTrans policyTrans = lifeReceipts.getPolicyTrans();
        if (lifeReceipts.getDrCr().equalsIgnoreCase("C")){

                    LifeReceiptAllocations alloc = new LifeReceiptAllocations();
                    alloc.setAllocDate(new Date());
                    alloc.setInstalmentPremium(policyTrans.getNetPrem());
                    alloc.setLifeReceipts(lifeReceipts);
                    alloc.setAllocAmount(allocAmount);
                    alloc.setRefNo(refNo);
                    alloc.setTransaction(trans);
                    alloc.setPaidToDate(dateUtilities.getPaidToDate(policyTrans.getCoverFrom(),paidInsts));
                    alloc.setInstallNo(paidInsts);
                    alloc.setDoneDate(new Date());
                    alloc.setDoneBy(userUtils.getCurrentUser());

                    LifeReceiptAllocations savedAlloc = lifeallocationsRepo.save(alloc);
                    lifeReceipts.setBalanceAmt(lifeReceipts.getBalanceAmt().subtract(allocAmount));
                    if (lifeReceipts.getAllocatedAmt()!=null) {
                        lifeReceipts.setAllocatedAmt(lifeReceipts.getAllocatedAmt().add(savedAlloc.getAllocAmount()));
                    }else {
                        lifeReceipts.setAllocatedAmt(savedAlloc.getAllocAmount());
                    }
                    lifeReceiptsRepo.save(lifeReceipts);
                    allocateLifeRcptComm(savedAlloc,paidInsts);

        }
    }
    @Override
    @Modifying
    @Transactional(readOnly = false, rollbackFor = { BadRequestException.class }, propagation = Propagation.REQUIRED)
    public void allocateLifeRcptBalance(Long polId) throws BadRequestException {
        Iterable<LifeReceipts> lifeReceipts = lifeReceiptsRepo.findAll(QLifeReceipts.lifeReceipts.policyTrans.policyId.eq(polId)
                .and(QLifeReceipts.lifeReceipts.balanceAmt.gt(0))
                .and(QLifeReceipts.lifeReceipts.receiptTrans.cancelled.equalsIgnoreCase("N").or(QLifeReceipts.lifeReceipts.receiptTrans.cancelled.isNull())),new Sort(Sort.Direction.ASC, "receiptId"));
        BigDecimal totBalance = BigDecimal.ZERO;
        BigDecimal balToAllocate;
        BigDecimal AllocatedAmount ;
        BigDecimal instBal;

        System.out.println("Receipt with Balances="+lifeReceipts.toString());

        for (LifeReceipts rct:lifeReceipts){
//            if(rct.getReceiptTrans().getCancelled()!=null && "Y".equalsIgnoreCase(rct.getReceiptTrans().getCancelled()) ){
//                throw new BadRequestException("Receipt already Cancelled...Cannot cancel");
//            }
            totBalance = totBalance.add(rct.getBalanceAmt());
        }

        PolicyTrans policyTrans = policyRepo.findOne(polId);



        if (totBalance.compareTo(policyTrans.getNetPrem())<0) {
            throw new BadRequestException("Receipt balance should be greater than instalment premium....receipt ="+totBalance+";premium="+policyTrans.getNetPrem());
        }
        int  fullInsts = (totBalance.divide(policyTrans.getNetPrem(),BigDecimal.ROUND_FLOOR)).intValue();
        int paidInst = 0;
        String revisionFormat=policyTrans.getRevisionFormat();
        String refNo="";
        if (policyTrans.getPaidInsts()!=null ) {
            paidInst =policyTrans.getPaidInsts();
        }else
            policyTrans.setPaidInsts(0);


        if (totBalance.compareTo(policyTrans.getNetPrem())>=0 && totBalance.compareTo(BigDecimal.ZERO)>0 ) {
            SystemTrans transaction = new SystemTrans();
            transaction.setDoneDate(new Date());
            transaction.setDoneBy(userUtils.getCurrentUser());
            transaction.setPolicy(policyTrans);
            transaction.setTransLevel("U");
            transaction.setTransCode("NBD"); //A way to setup and look up for transaction transcode
            transaction.setAuthBy(userUtils.getCurrentUser());
            transaction.setAuthDate(new Date());
            transaction.setTransAuthorised("Y");
            SystemTrans savedTrans=transRepo.save(transaction);
            balToAllocate =  policyTrans.getNetPrem().multiply(new BigDecimal(fullInsts));

            instBal = policyTrans.getNetPrem();
            SystemTransactions trans= new SystemTransactions();
            for (LifeReceipts rct:lifeReceipts){
                BigDecimal rctBalance = rct.getBalanceAmt();
                while (balToAllocate.compareTo(BigDecimal.ZERO)==1 &&
                        rctBalance.compareTo(BigDecimal.ZERO)==1 &&
                        policyTrans.getPaidInsts()<policyTrans.getTotalInstalments() ){
                    while (rctBalance.compareTo(BigDecimal.ZERO)==1 && balToAllocate.compareTo(BigDecimal.ZERO)==1 && policyTrans.getPaidInsts()<policyTrans.getTotalInstalments() ){
                        if (instBal.compareTo(BigDecimal.ZERO)==0){
                            instBal = policyTrans.getNetPrem();
                        }


                        if (instBal.equals(policyTrans.getNetPrem())){
                            paidInst++;
                            refNo =revisionFormat+"/INST/"+paidInst;
                            trans  = allocationService.createInstalmentTransaction(refNo,policyTrans.getNetPrem(),
                                    savedTrans,"POLICY INSTALMENT", policyTrans );
                        }
                        if (rctBalance.compareTo(instBal)==1){
                            settlePartialInstalment(rct,instBal,paidInst,refNo,savedTrans);
                            AllocatedAmount = instBal;
                            System.out.println(trans.toString());
                            accountsService.allocateCreditTrans(rct.getSystemTransaction().getTransno(),trans.getTransno(),AllocatedAmount);
                        }else {
                            settlePartialInstalment(rct,rctBalance,paidInst,refNo,savedTrans);
                            AllocatedAmount = rctBalance;
                            accountsService.allocateCreditTrans(rct.getSystemTransaction().getTransno(),trans.getTransno(),AllocatedAmount);
                        }
                        instBal=instBal.subtract(AllocatedAmount);
                        rctBalance=rctBalance.subtract(AllocatedAmount);
                        balToAllocate=balToAllocate.subtract(AllocatedAmount);
                        if (instBal.compareTo(BigDecimal.ZERO)==0){
                            policyTrans.setPaidInsts(paidInst);
                            policyTrans.setPolPaidToDate( dateUtilities.getPaidToDate(policyTrans.getCoverFrom(),paidInst) );
                            //System.out.println("xxx="+policyTrans);
                            policyTrans=policyRepo.save(policyTrans);
                            //add debit posting here

                           }

                    }


            }
        }
          ///// settlement concept here
        }
    }



}
