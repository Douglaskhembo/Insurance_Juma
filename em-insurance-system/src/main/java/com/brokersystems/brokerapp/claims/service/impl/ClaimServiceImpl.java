package com.brokersystems.brokerapp.claims.service.impl;

import com.brokersystems.brokerapp.accounts.model.CoaSubAccounts;
import com.brokersystems.brokerapp.accounts.model.PayeeAccounts;
import com.brokersystems.brokerapp.accounts.model.Payees;
import com.brokersystems.brokerapp.accounts.repository.BankBranchRepo;
import com.brokersystems.brokerapp.accounts.repository.PayeeAccountsRepo;
import com.brokersystems.brokerapp.accounts.repository.PayeesRepo;
import com.brokersystems.brokerapp.claims.dtos.*;
import com.brokersystems.brokerapp.claims.exception.ClaimException;
import com.brokersystems.brokerapp.claims.model.*;
import com.brokersystems.brokerapp.claims.repository.*;
import com.brokersystems.brokerapp.claims.service.ClaimService;
import com.brokersystems.brokerapp.enums.RevenueItems;
import com.brokersystems.brokerapp.medical.model.ServiceProviderContracts;
import com.brokersystems.brokerapp.medical.repository.ServiceProviderContractRepo;
import com.brokersystems.brokerapp.server.datatables.DataTablesRequest;
import com.brokersystems.brokerapp.server.datatables.DataTablesResult;
import com.brokersystems.brokerapp.server.exception.BadRequestException;
import com.brokersystems.brokerapp.server.utils.DateUtilities;
import com.brokersystems.brokerapp.server.utils.TemplateMerger;
import com.brokersystems.brokerapp.server.utils.UserUtils;
import com.brokersystems.brokerapp.server.utils.ValidatorUtils;
import com.brokersystems.brokerapp.setup.model.*;
import com.brokersystems.brokerapp.setup.repository.*;
import com.brokersystems.brokerapp.setup.service.ParamService;
import com.brokersystems.brokerapp.trans.dtos.ChequeTransDTO;
import com.brokersystems.brokerapp.trans.dtos.ChequeTransDtlsDTO;
import com.brokersystems.brokerapp.trans.model.GlTransactions;
import com.brokersystems.brokerapp.trans.model.HomePremiumBean;
import com.brokersystems.brokerapp.trans.model.QSystemTrans;
import com.brokersystems.brokerapp.trans.model.SystemTrans;
import com.brokersystems.brokerapp.trans.repository.GlTransRepo;
import com.brokersystems.brokerapp.trans.repository.SystemTransRepo;
import com.brokersystems.brokerapp.trans.service.AccountsUtilities;
import com.brokersystems.brokerapp.trans.service.PaymentService;
import com.brokersystems.brokerapp.uw.dtos.WIBABeneficiariesDTO;
import com.brokersystems.brokerapp.uw.model.*;
import com.brokersystems.brokerapp.uw.repository.PolicyTransRepo;
import com.brokersystems.brokerapp.uw.repository.RiskTransRepo;
import com.brokersystems.brokerapp.uw.repository.SectionTransRepo;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.security.claims.authorization.Claim;
import org.hibernate.annotations.OrderBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by peter on 3/5/2017.
 */
@Service
public class ClaimServiceImpl implements ClaimService {

    @Autowired
    private RiskTransRepo riskTransRepo;

    @Autowired
    private ClmStatusRepo clmStatusRepo;

    @Autowired
    private ClaimantDefRepo claimantDefRepo;

    @Autowired
    private OccupationRepo occupRepo;

    @Autowired
    private ValidatorUtils validator;

    @Autowired
    private SectionTransRepo sectionTransRepo;

    @Autowired
    private BinderSectPerilsRepo binderSectPerilsRepo;

    @Autowired
    private DateUtilities dateUtilities;

    @Autowired
    private SequenceRepository sequenceRepo;

    @Autowired
    private ClaimsBookingRepo claimsBookingRepo;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private ClaimClaimantsRepo claimClaimantsRepo;

    @Autowired
    private SubclassReqDocRepo requiredDocsRepo;

    @Autowired
    private ClaimRequiredDocsRepo claimRequiredDocsRepo;

    @Autowired
    private PayeesRepo payeesRepo;
    @Autowired
    private PayeeAccountsRepo payeeAccountsRepo;
    @Autowired
    private BankBranchRepo bankBranchRepo;
    @Autowired
    private PaymentModeRepo paymentModeRepo;
    @Autowired
    private BankAccountsRepo bankAccountsRepo;
    @Autowired
    private CurrencyRepository currencyRepository;
    @Autowired
    private ClaimPerilsRepo claimPerilsRepo;

    @Autowired
    private ClaimRevisionsRepo claimRevisionsRepo;

    @Autowired
    private ClaimRevisionTransRepo claimRevisionTransRepo;

    @Autowired
    private ClaimUploadRepo uploadRepo;

    @Autowired
    private ClmActivitiesRepo activitiesRepo;

    @Autowired
    private PolicyTransRepo transRepo;

    @Autowired
    private SystemTransRepo systemTransRepo;

    @Autowired
    private ParamService paramService;

    @Autowired
    private TemplateMerger templateMerger;

    @Autowired
    private ServiceProviderContractRepo serviceProviderContractRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    ClaimPerilPaymentsRepo perilPaymentsRepo;

    @Autowired
    GIBServiceProviderTypesRepo serviceProviderTypesRepo;

    @Autowired
    ServiceProviderRepo serviceProviderRepo;

    @Autowired
    ClaimStatusesRepo claimStatusesRepo;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private AccountsUtilities accountsUtilities;

    @Autowired
    private GlTransRepo glTransRepo;
    @Autowired
    private DateUtilities dateUtils;
    @Autowired
    private ClaimPaymentsRepo claimPaymentsRepo;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private ClaimPaymentDetailsRepo paymentDetailsRepo;
    @Autowired
    private ClaimServiceProviderRepo claimServiceProviderRepo;

    @Override
    @Transactional(readOnly = true)
    public Page<ClaimRisksDTO> findRisksToClaim(String searchValue, Date lossDate, Pageable pageable) {
        if(searchValue==null) searchValue="%%";
        else searchValue = "%"+searchValue+"%";
        List<Object[]> risksTrans = riskTransRepo.findClaimRisks(lossDate,searchValue, pageable.getPageNumber(), pageable.getPageSize());
        long rowCount = 0L;
        if(!risksTrans.isEmpty()) rowCount = ((BigInteger)risksTrans.get(0)[7]).intValue();
        List<ClaimRisksDTO> dtoList = new ArrayList<>();
        for(Object[] risk:risksTrans){
            ClaimRisksDTO risksDTO = ClaimRisksDTO.instance(((BigInteger)risk[0]).longValue(),(String)risk[2],(String) risk[1],(String) risk[3],
                    ((BigInteger)risk[5]).longValue(),((BigInteger)risk[4]).longValue(),((BigInteger)risk[6]).longValue());
            dtoList.add(risksDTO);
        }
        return new PageImpl<>(dtoList,pageable, rowCount);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<ClmCausations> findClaimStatuses(String searchValue, Pageable pageable) {
            if(searchValue==null) searchValue="";
            Predicate pred = QClmCausations.clmCausations.activityDesc.containsIgnoreCase(searchValue);
            return clmStatusRepo.findAll(pred,pageable);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
    public void defineClaimant(ClaimantsDef claimantsDef) {
        claimantsDef.setCreatedDate(new Date());
        claimantsDef.setCreatedUser(userUtils.getCurrentUser());
        claimantDefRepo.save(claimantsDef);
    }



    @Override
    @Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
    public void capturePerilPayment(ClaimPerilPayments perilPayments) throws BadRequestException {
        if(perilPayments.getClmPymntAmount()==null || perilPayments.getClmPymntAmount().compareTo(BigDecimal.ZERO) <=0)
            throw new BadRequestException("Payment Amount cannot be zero or negative");
        ClaimPerils claimPeril = claimPerilsRepo.findOne(QClaimPerils.claimPerils.clmPerilId.eq(perilPayments.getClaimPerils().getClmPerilId()));

        ClaimClaimants claimant=claimPeril.getClmClaimant();
        claimPeril.setClaimPaidAmount((claimPeril.getClaimPaidAmount()!=null)?claimPeril.getClaimPaidAmount().add(perilPayments.getClmPymntAmount()):perilPayments.getClmPymntAmount());
        claimant.setClaimPaidAmount((claimant.getClaimPaidAmount()!=null)?claimant.getClaimPaidAmount().add(perilPayments.getClmPymntAmount()):perilPayments.getClmPymntAmount());
        claimClaimantsRepo.save(claimant);
        claimPerilsRepo.save(claimPeril);
        perilPayments.setCaptureDate(new Date());
        perilPayments.setCapturedBy(userUtils.getCurrentUser());
        perilPaymentsRepo.save(perilPayments);
        BigDecimal ost = (claimRevisionsRepo.getTotalRevisions(claimPeril.getClaimBookings().getClmId(),claimPeril.getClmPerilId())).
                subtract(claimRevisionsRepo.getTotalPayments(claimPeril.getClaimBookings().getClmId()));
        System.out.println("Outstanding reserve "+ost);
        if(ost.compareTo(BigDecimal.ZERO) <0){
            throw new BadRequestException("The payment will result in negative reserve. Cannot continue");
        }
 }

    @Override
    @Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
    public void captureClaimPerils(ClaimPerils claimPeril) throws BadRequestException {

        if (claimPeril.getClmPerilId()!=null){
            ClaimPerils prevClaimPeril = claimPerilsRepo.findOne(claimPeril.getClmPerilId());
            Iterable<ClaimRevisionTrans> revisions = claimRevisionTransRepo.findAll(QClaimRevisionTrans.claimRevisionTrans.claimPeril.clmPerilId.eq(claimPeril.getClmPerilId()));

            ClaimRevisions claimRevisions = null;
            for(ClaimRevisionTrans revisionTrans:revisions){
                ClaimRevisions revisions1 = revisionTrans.getClaimRevision();
                if(!revisions1.getAuthStatus().equalsIgnoreCase("A")){
                    claimRevisions = revisions1;
                    break;
                }
            }



            if(claimRevisions!=null && claimRevisions.getRevisionId()!=null){
                claimRevisions.setRevAmount(claimPeril.getReserve());
                claimRevisions.setCreatedUser(userUtils.getCurrentUser());
                claimRevisionsRepo.save(claimRevisions);
                revisions.forEach(a -> {
                    a.setAmount(claimPeril.getReserve());
                    claimRevisionTransRepo.save(a);
                });
            }
            else {
                claimRevisions = new ClaimRevisions();
                claimRevisions.setClaimBookings(prevClaimPeril.getClaimBookings());
                claimRevisions.setTransType("LR");
                claimRevisions.setCreatedUser(userUtils.getCurrentUser());
                claimRevisions.setRevDate(new Date());
                claimRevisions.setRevAmount(claimPeril.getReserve());
                claimRevisions.setAuthStatus("D");
                ClaimRevisions savedRevision = claimRevisionsRepo.save(claimRevisions);
                ClaimRevisionTrans revisionTrans = new ClaimRevisionTrans();
                revisionTrans.setAmount(claimPeril.getReserve());
                revisionTrans.setClaimPeril(prevClaimPeril);
                revisionTrans.setClaimRevision(savedRevision);
                revisionTrans.setType("LR");
                claimRevisionTransRepo.save(revisionTrans);
            }
            BigDecimal ost = (claimRevisionsRepo.getTotalRevisionsUnauth(prevClaimPeril.getClaimBookings().getClmId(),prevClaimPeril.getClmPerilId())).
                    subtract(claimRevisionsRepo.getTotalPayments(prevClaimPeril.getClaimBookings().getClmId()));
            System.out.println("Outstanding reserve "+ost);
            if(ost.compareTo(BigDecimal.ZERO) <0){
                throw new BadRequestException("The payment will result in negative reserve. Cannot continue");
            }

        }else {
            throw  new BadRequestException("Select a peril to edit");
        }

    }


    @Override
    public Page<ClaimantsDTO> findAllClaimants(String searchValue, Pageable pageable) {
        final String search = (searchValue!=null)?"%"+searchValue+"%":"%%";
        List<Object[]> claimants = claimantDefRepo.findClmants(search.toLowerCase(),pageable.getPageNumber(), pageable.getPageSize());
        final List<ClaimantsDTO> claimantsDTOList = new ArrayList<>();
        long rowCount = 0L;
        if(!claimants.isEmpty()) rowCount = ((BigInteger)claimants.get(0)[10]).intValue();
        for(Object[] claimant:claimants){
            ClaimantsDTO claimantsDTO = new ClaimantsDTO();
            claimantsDTO.setClaimantId(((BigInteger)claimant[0]).longValue());
            claimantsDTO.setOtherNames((String)claimant[4]);
            claimantsDTO.setSurname((String)claimant[5]);
            claimantsDTOList.add(claimantsDTO);
        }
        return new PageImpl<>(claimantsDTOList,pageable,rowCount);
    }

    @Override
    @Transactional(readOnly = true)
    public DataTablesResult<ClaimantsDTO> findAllClaimants(DataTablesRequest request) {
        final String search = ( request.getSearch()!=null && request.getSearch().getValue()!=null)?"%"+request.getSearch().getValue().toLowerCase()+"%":"%%";
        List<Object[]> claimants = claimantDefRepo.findClmants(search.toLowerCase(),request.getPageNumber(), request.getPageSize());
        final List<ClaimantsDTO> claimantsDTOList = new ArrayList<>();
        long rowCount = 0l;
        if(!claimants.isEmpty()) rowCount = ((BigInteger)claimants.get(0)[10]).intValue();
        for(Object[] claimant:claimants){
            ClaimantsDTO claimantsDTO = new ClaimantsDTO();
            claimantsDTO.setClaimantId(((BigInteger)claimant[0]).longValue());
            claimantsDTO.setAddress((String)claimant[1]);
            claimantsDTO.setIdNumber((String)claimant[2]);
            claimantsDTO.setMobileNo((String)claimant[3]);
            claimantsDTO.setOtherNames((String)claimant[4]);
            claimantsDTO.setSurname((String)claimant[5]);
            claimantsDTO.setOccupation((String)claimant[6]);
            if(claimant[7]!=null) {
                claimantsDTO.setOccupId(((BigInteger) claimant[7]).longValue());
            }
            claimantsDTO.setCreatedDate((Date) claimant[8]);
            claimantsDTO.setCreatedBy((String)claimant[9]);
            claimantsDTOList.add(claimantsDTO);
        }
        Page<ClaimantsDTO>  page = new PageImpl<>(claimantsDTOList,request, rowCount);
        return new DataTablesResult<>(request, page);
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteClaimant(Long clmntId) {
        claimantDefRepo.delete(clmntId);
    }


    @Override
    @Transactional(readOnly = false)
    public void deleteClaimantPeril(Long clmPerilId) {

        ClaimPerils peril= claimPerilsRepo.findOne(clmPerilId);
        ClaimClaimants claimant=peril.getClmClaimant();
        BigDecimal changeAmt = BigDecimal.ZERO;
        changeAmt = changeAmt.add(peril.getChangeAmount());

        claimant.setClaimAmount((claimant.getClaimAmount()!=null)?claimant.getClaimAmount().subtract((changeAmt!=null)?changeAmt:BigDecimal.ZERO):claimant.getClaimAmount());

        ClaimRevisions claimRevisions = new ClaimRevisions();
        claimRevisions.setClaimBookings(peril.getClaimBookings());
        claimRevisions.setRevAmount(changeAmt.negate());
        claimRevisions.setRevDate(new Date());
        claimRevisions.setTransType("LO");
        claimRevisionsRepo.save(claimRevisions);
        claimClaimantsRepo.save(claimant);
        Iterable<ClaimRevisionTrans> revisions= claimRevisionTransRepo.findAll(QClaimRevisionTrans.claimRevisionTrans.claimPeril.eq(peril));
        claimRevisionTransRepo.delete(revisions);
        Iterable<ClaimPerilPayments> perilPayments= perilPaymentsRepo.findAll(QClaimPerilPayments.claimPerilPayments.claimPerils.clmPerilId.eq(clmPerilId));
        for (ClaimPerilPayments payment:perilPayments){
            deletePerilPayment(payment.getClmPymntId());
        }
        claimPerilsRepo.delete(clmPerilId);
    }

    @Override
    @Transactional(readOnly = false)
    public void deletePerilPayment(Long clmPymntId) {

        ClaimPerilPayments perilPayments= perilPaymentsRepo.findOne(clmPymntId);
        BigDecimal changeAmt = BigDecimal.ZERO;
        changeAmt = changeAmt.add(perilPayments.getClmPymntAmount());
        ClaimPerils claimPeril = perilPayments.getClaimPerils();
        ClaimClaimants claimant=claimPeril.getClmClaimant();

        claimPeril.setClaimPaidAmount((claimPeril.getClaimPaidAmount()!=null)?claimPeril.getClaimPaidAmount().subtract(perilPayments.getClmPymntAmount()):BigDecimal.ZERO);
        claimant.setClaimPaidAmount((claimant.getClaimPaidAmount()!=null)?claimant.getClaimPaidAmount().subtract(perilPayments.getClmPymntAmount()):BigDecimal.ZERO);
        claimPerilsRepo.save(claimPeril);
        claimClaimantsRepo.save(claimant);
        perilPaymentsRepo.delete(clmPymntId);

    }



    @Override
    @Transactional(readOnly = false)
    public void deleteClaimClaimant(Long clmntId) {
        Iterable<ClaimPerils> perils= claimPerilsRepo.findAll(QClaimPerils.claimPerils.clmClaimant.claimantId.eq(clmntId));


        for (ClaimPerils peril:perils){
            deleteClaimantPeril(peril.getClmPerilId());
        }

        claimClaimantsRepo.delete(clmntId);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<Occupation> findOccupations(String paramString, Pageable paramPageable) {
        Predicate pred = null;
        if (paramString == null || StringUtils.isBlank(paramString)) {
            pred =QOccupation.occupation.isNotNull();
        } else {
            pred = QOccupation.occupation.name.containsIgnoreCase(paramString);
        }
        return occupRepo.findAll(pred, paramPageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClaimantsDef> findClaimants(String searchValue, Pageable pageable) {
        if(searchValue==null) searchValue="";
        Predicate pred = QClaimantsDef.claimantsDef.surname.containsIgnoreCase(searchValue).or(QClaimantsDef.claimantsDef.otherNames.containsIgnoreCase(searchValue));
        return claimantDefRepo.findAll(pred,pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClaimPerilDTO> findSubclassPerils(String searchValue, Pageable pageable, Long riskId) {
        long start2 = System.currentTimeMillis();
        final String search = (searchValue!=null)?"%"+searchValue+"%":"%%";
        List<Object[]> perils = binderSectPerilsRepo.searchBinderPerils(riskId, search.toLowerCase(),pageable.getPageNumber(), pageable.getPageSize());
        long count = 0L;
        if(!perils.isEmpty()) count = ((BigInteger)perils.get(0)[2]).intValue();
        final List<ClaimPerilDTO> claimPerilDTOS = new ArrayList<>();
        for(Object[] peril:perils){
            ClaimPerilDTO perilDTO = new ClaimPerilDTO();
            perilDTO.setBindPerilCode(((BigInteger)peril[0]).longValue());
            perilDTO.setPerilDesc((String)peril[1]);
            claimPerilDTOS.add(perilDTO);
        }
        long end2 = System.currentTimeMillis();
        System.out.println("Elapsed Time in milli seconds: "+ (end2-start2));
        return new PageImpl<>(claimPerilDTOS,pageable, count);
    }

    public boolean checkPerilExists(List<PerilsDef> listPerils, PerilsDef perils){
        for(PerilsDef peril:listPerils){
            if(peril.getPerilCode() == perils.getPerilCode()) return true;
        }
        return false;
    }

    @Override
    @Transactional(readOnly = false,rollbackFor = { ClaimException.class })
    public Long createClaim(ClaimForm claimForm) throws ClaimException {
        if(claimForm.getPerils()==null ||claimForm.getPerils().size()==0)
            throw  new ClaimException("Cannot Create A Claim without perils...");
        if(claimForm.getLossDate()==null || claimForm.getNotificationDate()==null)
            throw new ClaimException("You cannot create A Claim without Loss Date or Notification Date");

        if(claimForm.getNotificationDate().after(new Date())){
            throw new ClaimException("The date you are adviced of the claim cannot be after Today's Date");
        }

        if(claimForm.getLossDate().after(claimForm.getNotificationDate())){
            throw new ClaimException("There is No Way you would be advised of a claim that has not happened. Please check that the date adviced is not before the Loss Date");
        }
        int clmUwYear = dateUtilities.getUwYear(claimForm.getLossDate());
        Predicate seqPredicate = QSystemSequence.systemSequence.transType.eq("CL");
        if (sequenceRepo.count(seqPredicate) == 0)
            throw new ClaimException("Sequence for Claims Transactions has not been defined");
        SystemSequence sequence = sequenceRepo.findOne(seqPredicate);
        Long seqNumber = sequence.getNextNumber();
        sequence.setLastNumber(seqNumber);
        sequence.setNextNumber(seqNumber + 1);
        sequenceRepo.save(sequence);
        final String claimNumber = sequence.getSeqPrefix() + String.format("%05d", seqNumber);
        Long clmCount = claimsBookingRepo.count(QClaimBookings.claimBookings.risk.riskId.eq(claimForm.getRiskId()).and(QClaimBookings.claimBookings.lossDate.eq(claimForm.getLossDate())));
        if(clmCount > 0) throw new ClaimException("Another claim already exists for the loss date selected");
        RiskTrans riskTrans  = riskTransRepo.findOne(QRiskTrans.riskTrans.riskId.eq(claimForm.getRiskId()));
        ClaimBookings bookings = new ClaimBookings();
        bookings.setBookedBy(userUtils.getCurrentUser());
        bookings.setBookedDate(new Date());
        bookings.setClaimNo(claimNumber);
        bookings.setClaimRejected(false);
        bookings.setClmDate(claimForm.getNotificationDate());
        bookings.setClaimStatus("B");
        bookings.setClaimTime(new SimpleDateFormat("h:m").format(new Date()));
        bookings.setLossDate(claimForm.getLossDate());
        bookings.setLossDesc(claimForm.getLossDesc());
        bookings.setNextReviewDate(claimForm.getNextReviewDate());
        bookings.setRisk(riskTransRepo.findOne(QRiskTrans.riskTrans.riskId.eq(claimForm.getRiskId())));
        bookings.setStatusDate(new Date());
        bookings.setActivity(clmStatusRepo.findOne(claimForm.getActivityId()));
        bookings.setLiabilityAdmission(claimForm.isLiabilityAdmission());
        bookings.setPartyToBlame(claimForm.getPartyToBlame());
        bookings.setInsurerDate(claimForm.getInsurerDate());
        ClaimBookings booking = claimsBookingRepo.save(bookings);

        ClaimActivities activities = new ClaimActivities();
        activities.setActivity(clmStatusRepo.findOne(claimForm.getActivityId()));
        activities.setActivityDate(new Date());
        activities.setClaimBookings(booking);
        activities.setRemDate(claimForm.getNextReviewDate());
        activities.setUserCreated(userUtils.getCurrentUser());
        activities.setCurrentActivity("Y");
        activitiesRepo.save(activities);

        List<PerilBean> perils = claimForm.getPerils();
        List<ClaimClaimants> claimants = new ArrayList<>();
        List<ClaimPerils> clmPerils = new ArrayList<>();
        for(PerilBean perilBean:perils){
            BinderSectionPerils binderSectionPeril = binderSectPerilsRepo.findOne(QBinderSectionPerils.binderSectionPerils.bspId.eq(perilBean.getPerilCode()));
            ClaimClaimants claimClaimant = new ClaimClaimants();
            ClaimPerils clmPeril = new ClaimPerils();

            clmPeril.setClaimBookings(booking);
            clmPeril.setClaimant("Y");
            clmPeril.setClmClaimant(claimClaimant);
            clmPeril.setExcessAmt(binderSectionPeril.getSubclassPeril().getExcess());
            clmPeril.setType(binderSectionPeril.getSubclassPeril().getSiOrLimit());
            clmPeril.setLimitAmt(binderSectionPeril.getSubclassPeril().getClaimLimit());
            clmPeril.setReserve(perilBean.getPerilEstimate());
            clmPeril.setChangeAmount(perilBean.getPerilEstimate());
            clmPeril.setRevisionBy(userUtils.getCurrentUser());
            clmPeril.setTransType("LO");
            clmPeril.setPerilsDef(binderSectionPeril.getSubclassPeril().getPeril());
            clmPeril.setTotalReserve(perilBean.getPerilEstimate());
            clmPeril.setOriginalreserve(binderSectionPeril.getSubclassPeril().getClaimLimit());
            clmPeril.setBinderSectionPerils(binderSectionPeril);
            claimClaimant.setClaimAmount(perilBean.getPerilEstimate());
            claimClaimant.setClaimantStatus("1");
            claimClaimant.setClaimBookings(booking);
            System.out.println("Self as Claimant..."+perilBean.getSelfAsClaimant());
            if(perilBean.getSelfAsClaimant()==null || "off".equalsIgnoreCase(perilBean.getSelfAsClaimant())){
                claimClaimant.setThirdParty("T");
                ClaimantsDef claimantsDef = claimantDefRepo.findOne(perilBean.getClaimantCode());
                claimClaimant.setClaimant(claimantsDef);
            }
            else if("on".equalsIgnoreCase(perilBean.getSelfAsClaimant())) {
                claimClaimant.setThirdParty("S");
                claimClaimant.setClient(riskTrans.getInsured());
            }
            claimants.add(claimClaimant);
            clmPerils.add(clmPeril);
        }
        claimClaimantsRepo.save(claimants);
        Iterable<ClaimPerils> savedPerils  = claimPerilsRepo.save(clmPerils);
        Iterable<SubClassReqdDocs> requiredDocs = requiredDocsRepo.findAll(QSubClassReqdDocs.subClassReqdDocs.subclass.subId.eq(riskTrans.getSubclass().getSubId())
                .and(QSubClassReqdDocs.subClassReqdDocs.requiredDoc.appliesLossOpening.eq(true))
                .and(QSubClassReqdDocs.subClassReqdDocs.requiredDoc.mandatory.eq(true)));
        List<ClaimRequiredDocs> claimRequiredDocs = new ArrayList<>();
        for(SubClassReqdDocs subClassReqdDocs:requiredDocs){
            ClaimRequiredDocs claimRequiredDoc = new ClaimRequiredDocs();
            claimRequiredDoc.setClaimBookings(booking);
            claimRequiredDoc.setRequiredDoc(subClassReqdDocs.getRequiredDoc());
            claimRequiredDoc.setSubmitted("N");
            claimRequiredDoc.setUserReceived(userUtils.getCurrentUser());
            claimRequiredDocs.add(claimRequiredDoc);
        }
        claimRequiredDocsRepo.save(claimRequiredDocs);

        BigDecimal changeAmt = BigDecimal.ZERO;
        for(ClaimPerils clmprl:savedPerils){
            changeAmt = changeAmt.add(clmprl.getChangeAmount());
        }

        ClaimRevisions claimRevisions = new ClaimRevisions();
        claimRevisions.setClaimBookings(booking);
        claimRevisions.setRevAmount(changeAmt);
        claimRevisions.setRevDate(new Date());
        claimRevisions.setTransType("LO");
        ClaimRevisions savedRevision = claimRevisionsRepo.save(claimRevisions);
        List<ClaimRevisionTrans> revisionTranses = new ArrayList<>();
        for(ClaimPerils peril:savedPerils){
            ClaimRevisionTrans revisionTrans = new ClaimRevisionTrans();
            revisionTrans.setAmount(peril.getChangeAmount());
            revisionTrans.setClaimPeril(peril);
            revisionTrans.setClaimRevision(savedRevision);
            revisionTrans.setType(peril.getType());
            revisionTranses.add(revisionTrans);
        }

        claimRevisionTransRepo.save(revisionTranses);
        return booking.getClmId();
    }


    @Override
    @Transactional(readOnly = false,rollbackFor = { ClaimException.class })
    public void createClaimantPeril(PerilBean perilBean) throws ClaimException,BadRequestException {
        if(perilBean==null)
            throw  new BadRequestException("Provide Peril to proceed...");
        if (perilBean.getClaimId()==null)
            throw  new BadRequestException("No claim selected...");
        if (perilBean.getPerilCode()==null)
            throw  new BadRequestException("Select the peril to proceed...");
        ClaimBookings booking = claimsBookingRepo.findOne(perilBean.getClaimId());
        RiskTrans riskTrans  = booking.getRisk();
        BinderSectionPerils binderSectionPeril = binderSectPerilsRepo.findOne(perilBean.getPerilCode());
        ClaimClaimants claimClaimant = new ClaimClaimants();
        claimClaimant.setCreatedUser(userUtils.getCurrentUser());
        claimClaimant.setCreatedDate(new Date());
        ClaimPerils clmPeril = new ClaimPerils();
        clmPeril.setClaimBookings(booking);
        clmPeril.setClaimant("Y");
        clmPeril.setExcessAmt(binderSectionPeril.getSubclassPeril().getExcess());
        clmPeril.setType(binderSectionPeril.getSubclassPeril().getSiOrLimit());
        clmPeril.setLimitAmt(binderSectionPeril.getSubclassPeril().getClaimLimit());
        clmPeril.setReserve(perilBean.getPerilEstimate());
        clmPeril.setChangeAmount(perilBean.getPerilEstimate());
        clmPeril.setRevisionBy(userUtils.getCurrentUser());
        clmPeril.setTransType("LO");
        clmPeril.setPerilsDef(binderSectionPeril.getSubclassPeril().getPeril());
        clmPeril.setTotalReserve(perilBean.getPerilEstimate());
        clmPeril.setOriginalreserve(binderSectionPeril.getSubclassPeril().getClaimLimit());
        clmPeril.setBinderSectionPerils(binderSectionPeril);
//        if(perilBean.getExpireSectionId()!=null) {
//            clmPeril.setExpiringSection(sectionTransRepo.findOne(perilBean.getExpireSectionId()));
//        }
//        if(perilBean.getSelfAsClaimant()==null ||perilBean.getSelfAsClaimant()=="" || "off".equalsIgnoreCase(perilBean.getSelfAsClaimant())){
//            if (claimClaimantsRepo.count(QClaimClaimants.claimClaimants.claimant.claimantId.eq(perilBean.getClaimantCode()
//            ).and(QClaimClaimants.claimClaimants.claimBookings.clmId.eq(booking.getClmId())))==0){
//                claimClaimant.setClaimAmount(perilBean.getPerilEstimate());
//                claimClaimant.setClaimantStatus("1");
//                claimClaimant.setClaimBookings(booking);
//                claimClaimant.setThirdParty("T");
//                ClaimantsDef claimantsDef = claimantDefRepo.findOne(perilBean.getClaimantCode());
//                claimClaimant.setClaimant(claimantsDef);
//            }else {
//                claimClaimant=  claimClaimantsRepo.findOne(QClaimClaimants.claimClaimants.claimant.claimantId.eq(perilBean.getClaimantCode()
//                ).and(QClaimClaimants.claimClaimants.claimBookings.clmId.eq(booking.getClmId())));
//                if (claimPerilsRepo.count(QClaimPerils.claimPerils.claimBookings.clmId.eq(booking.getClmId())
//                .and(QClaimPerils.claimPerils.binderSectionPerils.eq(binderSectionPeril)).and(QClaimPerils.claimPerils.clmClaimant.eq(claimClaimant)))>0){
//                    throw new BadRequestException("Peril being added already exists under this claimant");
//                }
//                claimClaimant.setClaimAmount((claimClaimant.getClaimAmount()!=null)?claimClaimant.getClaimAmount().add((perilBean.getPerilEstimate()!=null)?perilBean.getPerilEstimate():BigDecimal.ZERO):perilBean.getPerilEstimate());
//            }
//
//        }
//        else if("on".equalsIgnoreCase(perilBean.getSelfAsClaimant())) {
//            if (claimClaimantsRepo.count(QClaimClaimants.claimClaimants.client.tenId.eq(riskTrans.getInsured().getTenId()
//            ).and(QClaimClaimants.claimClaimants.claimBookings.clmId.eq(booking.getClmId())))==0) {
//                claimClaimant.setClaimAmount(perilBean.getPerilEstimate());
//                claimClaimant.setClaimantStatus("1");
//                claimClaimant.setClaimBookings(booking);
//                claimClaimant.setThirdParty("S");
//                claimClaimant.setClient(riskTrans.getInsured());
//            }else {
//                claimClaimant = claimClaimantsRepo.findOne(QClaimClaimants.claimClaimants.client.tenId.eq(riskTrans.getInsured().getTenId()
//                ).and(QClaimClaimants.claimClaimants.claimBookings.clmId.eq(booking.getClmId())));
//                if (claimPerilsRepo.count(QClaimPerils.claimPerils.claimBookings.clmId.eq(booking.getClmId())
//                        .and(QClaimPerils.claimPerils.binderSectionPerils.eq(binderSectionPeril)).and(QClaimPerils.claimPerils.clmClaimant.eq(claimClaimant)))>0){
//                    throw new BadRequestException("Peril being added already exists under this claimant");
//                }
//                claimClaimant.setClaimAmount((claimClaimant.getClaimAmount()!=null)?claimClaimant.getClaimAmount().add((perilBean.getPerilEstimate()!=null)?perilBean.getPerilEstimate():BigDecimal.ZERO):perilBean.getPerilEstimate());
//            }
//        }
//        clmPeril.setClmClaimant(claimClaimant);
//        claimClaimantsRepo.save(claimClaimant);
//        ClaimPerils savedPerils=claimPerilsRepo.save(clmPeril);
//        ClaimRevisions claimRevisions = new ClaimRevisions();
//        claimRevisions.setClaimBookings(booking);
//        claimRevisions.setRevAmount(perilBean.getPerilEstimate());
//        claimRevisions.setRevDate(new Date());
//        claimRevisions.setTransType("LR");
//        ClaimRevisions savedRevision = claimRevisionsRepo.save(claimRevisions);
//
//        ClaimRevisionTrans revisionTrans = new ClaimRevisionTrans();
//        revisionTrans.setAmount(perilBean.getPerilEstimate());
//        revisionTrans.setClaimPeril(savedPerils);
//        revisionTrans.setClaimRevision(savedRevision);
//        revisionTrans.setType(savedPerils.getType());
//        claimRevisionTransRepo.save(revisionTrans);
    }


    @Override
    public ClaimBookings getOne(Long clmId) {
        return claimsBookingRepo.findOne(clmId);
    }

    @Override
    @Transactional(readOnly = true)
    public ClaimDetailsDTO getClaimInformation(Long clmId) throws BadRequestException {
        if(clmId==null) throw  new BadRequestException("Error Getting Claim Information");
        ClaimDetailsDTO booking = new ClaimDetailsDTO();
        List<Object[]> detailsList = claimsBookingRepo.getClaimDetails(clmId);
        if(!detailsList.isEmpty()){
            Object[] details = detailsList.get(0);
            booking.setClmId(clmId);
            booking.setClaimNo((String) details[1]);
            booking.setInsured((String) details[2]);
            booking.setLossDesc((String) details[3]);
            booking.setCausation((String) details[4]);
            booking.setLossDate((Date) details[5]);
            booking.setNextRvwDate((Date) details[6]);
            booking.setBookedDate((Date) details[7]);
            booking.setNotificationDate((Date) details[8]);
            booking.setLiabilityAdmission((Boolean) details[9]);
            booking.setPolicyNo((String)details[10]);
            booking.setClient((String)details[11]);
            booking.setProduct((String)details[12]);
            booking.setRiskId((String)details[13]);
            booking.setRiskValue((BigDecimal) details[14]);
            booking.setRiskWef((Date) details[15]);
            booking.setRiskWet((Date) details[16]);
            booking.setRiskBindId(((BigDecimal)details[17]).longValue());
            booking.setPolicyBindId(((BigDecimal)details[18]).longValue());
            booking.setClaimStatus((String)details[19]);
            booking.setClmRiskId(((BigDecimal)details[20]).longValue());
            booking.setTotalReserve(claimRevisionsRepo.getClaimTotalRevisions(clmId));
            booking.setTotalPayments(claimRevisionsRepo.getTotalPayments(clmId));
            booking.setOstReserve((claimRevisionsRepo.getClaimTotalRevisions(clmId).subtract(claimRevisionsRepo.getClaimTotalPayments(clmId))));
            return booking;
        }
        else return booking;

    }

    @Override
    @Transactional(readOnly = true)
    public DataTablesResult<ClaimEnquiryDTO> enquireClaims(DataTablesRequest request,
                                                           Long clientCode, String polNo, String riskId, String claimNo) throws IllegalAccessException {
        if (polNo == null || StringUtils.isBlank(polNo)) {
            polNo = "%";
        }
        else{
            polNo = "%"+polNo.toLowerCase()+"%";
        }

        if (claimNo == null || StringUtils.isBlank(claimNo)) {
            claimNo = "%";
        }
        else{
            claimNo = "%"+claimNo.toLowerCase()+"%";
        }

        if (riskId == null || StringUtils.isBlank(riskId)) {
            riskId = "%";
        }
        else{
            riskId = "%"+riskId.toLowerCase()+"%";
        }

        List<Object[]> claimBookingsList = claimsBookingRepo.getClaimBookings(riskId,polNo,clientCode,claimNo,request.getPageNumber(), request.getPageSize());
        final List<ClaimEnquiryDTO> claimEnquiryDTOList = new ArrayList<>();
        long rowCount = 0L;
        if(!claimBookingsList.isEmpty()) rowCount = ((BigInteger)claimBookingsList.get(0)[11]).intValue();
        for(Object[] obj:claimBookingsList){
            ClaimEnquiryDTO enquiryDTO = new ClaimEnquiryDTO();
            enquiryDTO.setClmId(((BigInteger)obj[0]).longValue());
            enquiryDTO.setUsername((String) obj[1]);
            enquiryDTO.setLossDate((Date) obj[2]);
            enquiryDTO.setClmDate((Date) obj[3]);
            final String status = (String)obj[4];
            if("O".equalsIgnoreCase(status)){
                enquiryDTO.setClmStatus("Open");
            }else  if("C".equalsIgnoreCase(status)){
                enquiryDTO.setClmStatus("Closed");
            }
            enquiryDTO.setClaimNo((String) obj[5]);
            enquiryDTO.setRiskId((String) obj[6]);
            enquiryDTO.setNextRevDate((Date) obj[7]);
            enquiryDTO.setPolicyNo((String) obj[8]);
            final String name = obj[9] + "  "+ obj[10];
            enquiryDTO.setInsuredName(name);
            claimEnquiryDTOList.add(enquiryDTO);
        }

        Page<ClaimEnquiryDTO>  page = new PageImpl<>(claimEnquiryDTOList,request, rowCount);
        return new DataTablesResult<>(request, page);
    }

    @Override
    @Transactional(readOnly = true)
    public DataTablesResult<ClaimPerilReserveDTO> getClaimPerils(DataTablesRequest request,Long clmId) {
        List<Object[]> claimants = claimPerilsRepo.findClmPerils(clmId,request.getPageNumber(), request.getPageSize());
        long rowCount = 0L;
        if(!claimants.isEmpty()) rowCount = ((BigInteger)claimants.get(0)[6]).intValue();
        final List<ClaimPerilReserveDTO> claimPerilReserveDTOList = new ArrayList<>();
        for(Object[] claimant:claimants){
            ClaimPerilReserveDTO claimantsDTO = new ClaimPerilReserveDTO();
            claimantsDTO.setPerilDesc((String)claimant[0]);
            claimantsDTO.setType((String)claimant[1]);
            claimantsDTO.setLimitAmt((BigDecimal) claimant[2]);
            claimantsDTO.setExcessAmt((BigDecimal)claimant[3]);
            claimantsDTO.setRemarks((String)claimant[4]);
            claimantsDTO.setClmPerilId(((BigInteger)claimant[5]).longValue());
            claimPerilReserveDTOList.add(claimantsDTO);
        }
        Page<ClaimPerilReserveDTO>  page = new PageImpl<>(claimPerilReserveDTOList,request, rowCount);
        return new DataTablesResult<>(request, page);
    }

    @Override
    public DataTablesResult<ClaimPaymentsDTO> getClaimPayments(DataTablesRequest request, Long clmId, Long sprId) {
        final String search = ( request.getSearch()!=null && request.getSearch().getValue()!=null)?"%"+request.getSearch().getValue()+"%":"%%";
        List<Object[]> payments = claimPaymentsRepo.getClmPayments(search.toLowerCase(),sprId, clmId,request.getPageNumber(), request.getPageSize());
        long rowCount = 0L;
        if(!payments.isEmpty()) rowCount = ((BigInteger)payments.get(0)[12]).intValue();
        final List<ClaimPaymentsDTO> paymentsDTOList = new ArrayList<>();
        for(Object[] payment:payments){
            ClaimPaymentsDTO paymentsDTO = new ClaimPaymentsDTO();
            paymentsDTO.setClmPymntId(((BigInteger)payment[0]).longValue());
            paymentsDTO.setPayee((String)payment[1]);
            paymentsDTO.setReference((String)payment[2]);
            paymentsDTO.setPaymentMode((String)payment[3]);
            paymentsDTO.setTransType((String)payment[4]);
            paymentsDTO.setCurrency((String)payment[5]);
            paymentsDTO.setAmount((BigDecimal) payment[6]);
            paymentsDTO.setStatus((String)payment[7]);
            paymentsDTO.setRaisedBy((String)payment[8]);
            paymentsDTO.setRaisedDate((Date) payment[9]);
            paymentsDTO.setAuthDate((Date) payment[10]);
            paymentsDTO.setAuthBy((String) payment[11]);
            paymentsDTOList.add(paymentsDTO);
        }
        Page<ClaimPaymentsDTO>  page = new PageImpl<>(paymentsDTOList,request, rowCount);
        return new DataTablesResult<>(request, page);
    }

    @Override
    @Transactional(readOnly = true)
    public DataTablesResult<ClaimPerilPayments> getPerilPayments(DataTablesRequest request,Long perilId) throws IllegalAccessException {
        BooleanExpression pred =QClaimPerilPayments.claimPerilPayments.claimPerils.clmPerilId.eq(perilId);
        Page<ClaimPerilPayments> page = perilPaymentsRepo.findAll(pred.and(request.searchPredicate(QClaimPerilPayments.claimPerilPayments)), request);
        return new DataTablesResult<>(request, page);
    }

    @Override
    @Transactional(readOnly = true)
    public DataTablesResult<ClaimClaimantsDTO> getClaimClaimants(DataTablesRequest request, Long clmId)  {
        List<Object[]> claimantsList = claimClaimantsRepo.findClmClaimants(clmId,request.getPageNumber(), request.getPageSize());
        List<ClaimClaimantsDTO> claimClaimantsDTOS = new ArrayList<>();
        long rowCount = 0l;
        if(!claimantsList.isEmpty()) rowCount = ((BigInteger)claimantsList.get(0)[10]).intValue();
        for(Object[] claimants:claimantsList){
            ClaimClaimantsDTO claimantsDTO = new ClaimClaimantsDTO();
            claimantsDTO.setClaimantId(((BigInteger)claimants[0]).longValue());
            claimantsDTO.setThirdParty((String) claimants[1]);
            claimantsDTO.setSelfClaimant((String) claimants[2]);
            claimantsDTO.setTpClaimant((String) claimants[3]);
            claimantsDTO.setClaimantStatus((String) claimants[4]);
            claimantsDTO.setCreatedDate((Date) claimants[5]);
            claimantsDTO.setCreatedBy((String) claimants[6]);
            claimantsDTO.setLimitAmount((BigDecimal) claimants[7]);
            claimantsDTO.setPeril((String) claimants[8]);
            claimantsDTO.setPerilType((String) claimants[9]);
            claimClaimantsDTOS.add(claimantsDTO);
        }
        Page<ClaimClaimantsDTO>  page = new PageImpl<>(claimClaimantsDTOS,request, rowCount);
        return new DataTablesResult<>(request, page);
    }




    @Override
    @Transactional(readOnly = true)
    public DataTablesResult<ClaimStatuses> getClaimStatuses(DataTablesRequest request, Long clmId) throws IllegalAccessException {
        BooleanExpression pred = QClaimStatuses.claimStatuses.claimBookings.clmId.eq(clmId);
        Page<ClaimStatuses> page = claimStatusesRepo.findAll(pred.and(request.searchPredicate(QClaimStatuses.claimStatuses)), request);
        return new DataTablesResult<>(request, page);
    }

    @Override
    @Transactional(readOnly = true)
    public DataTablesResult<ClaimRequiredDocsDTO> getRequiredDocs(DataTablesRequest request, Long clmId) throws IllegalAccessException {
        final String search = (request.getSearch()!=null)?"%"+request.getSearch().getValue()+"%":"%%";
        List<Object[]> claimsDocs = claimRequiredDocsRepo.findClaimDocs(clmId,search,request.getPageNumber(), request.getPageSize());
        List<ClaimRequiredDocsDTO> requiredDocsDTOS = new ArrayList<>();
        long rowCount = 0l;
        if(!claimsDocs.isEmpty()) rowCount = ((BigInteger)claimsDocs.get(0)[8]).intValue();
        for(Object[] doc:claimsDocs){
            ClaimRequiredDocsDTO requiredDoc = new ClaimRequiredDocsDTO();
            requiredDoc.setClmRequiredId(((BigInteger)doc[0]).longValue());
            requiredDoc.setDocRefNo((String) doc[1]);
            requiredDoc.setFileName((String) doc[2]);
            requiredDoc.setDateReceived((Date) doc[3]);
            requiredDoc.setUsername((String) doc[4]);
            requiredDoc.setRemarks((String) doc[5]);
            requiredDoc.setDocName((String) doc[6]);
            requiredDoc.setClaimStatus((String) doc[7]);
            requiredDocsDTOS.add(requiredDoc);
        }
        Page<ClaimRequiredDocsDTO>  page = new PageImpl<>(requiredDocsDTOS,request, rowCount);
        return new DataTablesResult<>(request, page);
    }

    @Override
    public void createclaimsRequiredDocs(RequiredDocBean requiredDocBean, Long clmId) {

        ClaimBookings claim = claimsBookingRepo.findOne(clmId);

        List<ClaimRequiredDocs> claimRequiredDocs =
                requiredDocBean.getRequiredDocs().stream().map(reqId -> {
                    ClaimRequiredDocs claimRequiredDoc = new ClaimRequiredDocs();
                    claimRequiredDoc.setRequiredDoc(requiredDocsRepo.findOne(reqId).getRequiredDoc());
                    claimRequiredDoc.setClaimBookings(claim);
                    claimRequiredDoc.setUserReceived(userUtils.getCurrentUser());
                    return claimRequiredDoc;
                }).collect(Collectors.toList());
        claimRequiredDocsRepo.save(claimRequiredDocs);
    }

    @Override
    @Transactional(readOnly = true)
    public DataTablesResult<ClaimBookings> getClaimBookings(DataTablesRequest request, Long clmId) throws IllegalAccessException {
        BooleanExpression pred = QClaimBookings.claimBookings.clmId.eq(clmId);
        Page<ClaimBookings> page = claimsBookingRepo.findAll(pred.and(request.searchPredicate(QClaimBookings.claimBookings)), request);
        return new DataTablesResult<>(request, page);
    }

    @Override
    @Transactional(readOnly = true)
    public DataTablesResult<ClaimUploadsDTO> getClaimUploads(DataTablesRequest request, Long clmId) throws IllegalAccessException {
        final String search = ( request.getSearch()!=null && request.getSearch().getValue()!=null)?"%"+request.getSearch().getValue()+"%":"%%";
        final List<ClaimUploadsDTO> claimUploadsDTOList = new ArrayList<>();
        List<Object[]> trans = uploadRepo.findSearchClaimUploads(clmId,search,request.getPageNumber(), request.getPageSize());
        long rowCount = 0L;
        if(!trans.isEmpty()) rowCount = (Integer)trans.get(0)[7];
        for(Object[] tran:trans){
            ClaimUploadsDTO claimUploadsDTO = new ClaimUploadsDTO();
            claimUploadsDTO.setUploadId(((BigDecimal)tran[0]).longValue());
            claimUploadsDTO.setFileId((String) tran[1]);
            claimUploadsDTO.setFileName((String) tran[2]);
            claimUploadsDTO.setDateUploaded((Date) tran[3]);
            claimUploadsDTO.setUploadedComment((String) tran[4]);
            claimUploadsDTO.setUploadedBy((String) tran[5]);
            claimUploadsDTO.setClaimStatus((String) tran[6]);
            claimUploadsDTOList.add(claimUploadsDTO);
        }
        Page<ClaimUploadsDTO>  page = new PageImpl<>(claimUploadsDTOList,request, rowCount);
        return new DataTablesResult<>(request, page);
    }

    @Override
    public DataTablesResult<ClaimActivityDTO> getClaimAcitivities(DataTablesRequest request, Long clmId) throws IllegalAccessException {
        final String search = ( request.getSearch()!=null && request.getSearch().getValue()!=null)?"%"+request.getSearch().getValue()+"%":"%%";
        final List<ClaimActivityDTO> activityDTOList = new ArrayList<>();
        List<Object[]> trans = activitiesRepo.getClmActivities(clmId,request.getPageNumber(), search,request.getPageSize());
        long rowCount = 0L;
        if(!trans.isEmpty()) rowCount = ((BigInteger)trans.get(0)[6]).intValue();
        for(Object[] tran:trans){
            ClaimActivityDTO activityDTO = new ClaimActivityDTO();
            activityDTO.setActivityId(((BigInteger)tran[0]).longValue());
            activityDTO.setActivityDesc((String) tran[1]);
            activityDTO.setUsername((String) tran[2]);
            activityDTO.setActivityDate((Date) tran[3]);
            activityDTO.setCurrentActivity((String) tran[4]);
            activityDTO.setRemDate((Date) tran[5]);
            activityDTOList.add(activityDTO);
        }
        Page<ClaimActivityDTO>  page = new PageImpl<>(activityDTOList,request, rowCount);
        return new DataTablesResult<>(request, page);

    }

    @Override
    @Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
    public void addActivity(ClaimActivities activity) throws BadRequestException {

        if (activity.getActivityId()==null){
            Iterable<ClaimActivities> currentActivities = activitiesRepo.findAll(QClaimActivities.claimActivities.claimBookings.clmId.eq(activity.getClaimBookings().getClmId()));
            for(ClaimActivities curactivity:currentActivities){
                if(curactivity.getRemDate().after(activity.getRemDate()))
                    throw new BadRequestException("Error creating activity. Check reminder date");
                if(curactivity.getActivity().getCaId()==activity.getActivity().getCaId())
                    throw new BadRequestException("Activity already exists..Select another activity");
                curactivity.setCurrentActivity("N");
            }
                activitiesRepo.save(currentActivities);
                activity.setCurrentActivity("Y");
                activity.setUserCreated(userUtils.getCurrentUser());
                activity.setActivityDate(new Date());
        }
        else {
            ClaimActivities preActivity = activitiesRepo.findOne(QClaimActivities.claimActivities.activityId.eq(activity.getActivityId()));
            activity.setCurrentActivity(preActivity.getCurrentActivity());
            activity.setUserCreated(preActivity.getUserCreated());
            activity.setActivityDate(preActivity.getActivityDate());
        }
        activitiesRepo.save(activity);
    }


    @Override
    @Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
    public void addClaimStatus(ClaimStatuses claimStatuses, Long clmId) throws BadRequestException {
        Iterable<ClaimStatuses> currentStatuses = claimStatusesRepo.findAll(QClaimStatuses.claimStatuses.claimBookings.clmId.eq(clmId));
        ClaimBookings bookings = claimsBookingRepo.findOne(clmId);
        for (ClaimStatuses curStatus : currentStatuses) {
            curStatus.setCurrentActivity("N");
        }
        if (claimStatuses.getRemarks() == null) {
            throw new BadRequestException("Remarks must be provided");
        }
        if (claimStatuses.getNewStatus() == "C" && claimStatuses.getCloseReason() == null) {
            throw new BadRequestException("Reason for Closure must be provided");
        }
        if (claimStatuses.getNewStatus() == null) {
            throw new BadRequestException("Error changing claims status");
        }

        if (claimStatuses.getNewStatus().equalsIgnoreCase("C")|| claimStatuses.getNewStatus().equalsIgnoreCase("R")) {
            Iterable<ClaimPerils> perils = claimPerilsRepo.findAll(QClaimPerils.claimPerils.claimBookings.eq(bookings)
                    .and(QClaimPerils.claimPerils.expiringSection.isNotNull()));
            if (claimStatuses.getCloseReason()!=null && claimStatuses.getCloseReason().equalsIgnoreCase("ST")) {
                for (ClaimPerils perils1 : perils) {
                    SectionTrans sectionTrans = perils1.getExpiringSection();
                    sectionTrans.setExpired("Y");
                    sectionTransRepo.save(sectionTrans);
                }
            } else {
                for (ClaimPerils perils1 : perils) {
                    SectionTrans sectionTrans = perils1.getExpiringSection();
                    sectionTrans.setExpired("N");
                    sectionTransRepo.save(sectionTrans);
                }
            }
        }
        claimStatusesRepo.save(currentStatuses);
        claimStatuses.setCurrentActivity("Y");
        claimStatuses.setCurrentStatus(claimStatuses.getNewStatus());
        claimStatuses.setCloseReason(claimStatuses.getCloseReason());
        claimStatuses.setOldStatus(bookings.getClaimStatus());
        claimStatuses.setClaimBookings(bookings);
        claimStatuses.setCapturedBy(userUtils.getCurrentUser());
        claimStatuses.setDateCaptured(new Date());
        claimStatusesRepo.save(claimStatuses);
        bookings.setClaimStatus(claimStatuses.getNewStatus());
        claimsBookingRepo.save(bookings);


    }

    @Override
    public ClaimBalanceBean getBalance(Long clmId) {
        List<Object[]> result = transRepo.getClaimDetails(clmId);
        ClaimBalanceBean balanceBean = new ClaimBalanceBean();
        for (Object[] object : result) {
                if(object[1]!=null){
                    balanceBean.setInsBalance((BigDecimal)object[1]);
                }
            if(object[2]!=null){
                balanceBean.setClientBalance((BigDecimal)object[2]);
            }
        }

        return balanceBean;
    }

    @Override
    public Set<SectionTransBean> getExpireSections(Long perilId, Long riskId) {
        Set<SectionTransBean> toExpireSection = new HashSet<>();
        List<Object[]> sections = sectionTransRepo.findExpireSection(riskId, perilId);
        List<SectionTransBean> sectionTrans = new ArrayList<>();
        for (Object[] sectran:sections){
            SectionTransBean  sect = new SectionTransBean();
            if(sectran[0] instanceof  BigInteger){
                sect.setSectId(((BigInteger)sectran[0]).longValue());
            }
            else  if(sectran[0] instanceof  BigDecimal){
                sect.setSectId(((BigDecimal)sectran[0]).longValue());
            }
            sect.setSection(sectran[1].toString());
            sectionTrans.add(sect);
        }
        for (SectionTransBean  sect : sectionTrans) {
            SectionTrans sectionTrans1 = sectionTransRepo.findOne(sect.getSectId());
            if (sectionTrans1.getSection().getType().getCode().equalsIgnoreCase("SI")) {
                SectionTrans excess = sectionTransRepo.findOne(QSectionTrans.sectionTrans.risk.riskId.eq(riskId)
                        .and(QSectionTrans.sectionTrans.section.shtDesc.containsIgnoreCase("EX")));
                if(excess!=null){
                    sect.setSectId(excess.getSectId());
                    sect.setSection(excess.getSection().getDesc());
                    toExpireSection.add(sect);
                }
            } else toExpireSection.add(sect);
        }
        return toExpireSection;
    }

    @Override
    public DataTablesResult<ClaimsTransDto> getClaimTransactions(DataTablesRequest request, Long clmId)  {
        final List<ClaimsTransDto> claimTransList = new ArrayList<>();
        List<Object[]> trans = claimRevisionsRepo.getClaimTransactions(clmId,request.getPageNumber(), request.getPageSize());
        long rowCount = 0L;
        if(!trans.isEmpty()) rowCount = ((BigInteger)trans.get(0)[8]).intValue();
        for(Object[] tran:trans){
            ClaimsTransDto claimsTransDto = new ClaimsTransDto();
            claimsTransDto.setTransId(((BigInteger)tran[0]).longValue());
            claimsTransDto.setTransAmount((BigDecimal) tran[1]);
            claimsTransDto.setTransDate((Date) tran[2]);
            final String type = (String) tran[3];;
            if(type.equalsIgnoreCase("CP"))
                claimsTransDto.setTransType("Claimant Payment");
            else if(type.equalsIgnoreCase("SP"))
                claimsTransDto.setTransType("Service Provider Payment");
            claimsTransDto.setAuthDate((Date) tran[4]);
            claimsTransDto.setCreatedBy((String) tran[5]);
            claimsTransDto.setAuthBy((String) tran[6]);
            final String status=(String) tran[7];
            if(status==null || status.equalsIgnoreCase("N"))
            claimsTransDto.setTransStatus("Draft");
            else if(status.equalsIgnoreCase("R")){
                claimsTransDto.setTransStatus("Ready");
            }
            else if(status.equalsIgnoreCase("P")){
                claimsTransDto.setTransStatus("Pending Finance");
            }
            else if(status.equalsIgnoreCase("A")){
                claimsTransDto.setTransStatus("Authorised");
            }
            claimTransList.add(claimsTransDto);
        }
        Page<ClaimsTransDto>  page = new PageImpl<>(claimTransList,request,rowCount);
        return new DataTablesResult<>(request, page);
    }

    public void makeReady(Long transId) {
        ClaimPayments revisionTrans = claimPaymentsRepo.findOne(transId);
        revisionTrans.setAuthorised("R");
        claimPaymentsRepo.save(revisionTrans);
    }

    @Override
    public void makeUndo(Long transId) {
        ClaimPayments revisionTrans = claimPaymentsRepo.findOne(transId);
        revisionTrans.setAuthorised(null);
        claimPaymentsRepo.save(revisionTrans);
    }

    @Override
    @Transactional(rollbackFor = BadRequestException.class)
    public void authoriseTransaction(Long transId) throws BadRequestException {

        ClaimPayments revisionTrans = claimPaymentsRepo.findOne(transId);
        revisionTrans.setAuthorised("P");
        revisionTrans.setAuthDate(new Date());
        revisionTrans.setAuthBy(userUtils.getCurrentUser());
        claimPaymentsRepo.save(revisionTrans);


        final ChequeTransDTO chequeTransDTO = new ChequeTransDTO();
        chequeTransDTO.setInvoiceNo(revisionTrans.getInvoiceNo());
        chequeTransDTO.setSource("CLM");
        chequeTransDTO.setPaymentType("GL");
        chequeTransDTO.setSourcePostedUser(userUtils.getCurrentUser().getId());
        chequeTransDTO.setSourcePostedDate(new Date());
        chequeTransDTO.setAmount(revisionTrans.getClmPymntAmount());
        chequeTransDTO.setOriginType("PYMT");
        chequeTransDTO.setRefNo(""+transId);
        List<Object[]> pymtDetails = claimPaymentsRepo.getPymentDetails(transId);
        Long subclassId = null;
        for(Object[] detail:pymtDetails){
            final Long payeeId = ((BigDecimal) detail[0]).longValue();
            final Long curId = ((BigDecimal) detail[1]).longValue();
            final Long pmId = ((BigDecimal) detail[2]).longValue();
            final Long branchId = ((BigDecimal) detail[3]).longValue();
            final Long acctId = ((BigDecimal) detail[4]).longValue();
           subclassId = ((BigDecimal) detail[5]).longValue();
            chequeTransDTO.setPayee(payeeId);
            chequeTransDTO.setCurId(curId);
            chequeTransDTO.setPaymentModeId(pmId);
            chequeTransDTO.setBranchCode(branchId);
            chequeTransDTO.setBankActCode(acctId);
            chequeTransDTO.setNarration("Claim Payment for "+detail[6]);
        }
        final BigDecimal poolAmount = claimPaymentsRepo.getAccountBalance(accountsUtilities.getGlCreditAccount(RevenueItems.CPL,subclassId).getCoId());
        if(poolAmount.compareTo(revisionTrans.getClmPymntAmount()) <0 ){
            throw new BadRequestException("The Pool amount "+poolAmount+" is not exhaustive enough to pay this claim");
        }
        chequeTransDTO.setRequistionDate(new Date());
        chequeTransDTO.setInvoiceDate(revisionTrans.getInvoiceDate());

        final List<ChequeTransDtlsDTO> chequeTransList = new ArrayList<>();
        List<BigDecimal> amounts = claimPaymentsRepo.findPymentDetailsAmounts(transId);
        for(BigDecimal amount: amounts){
            ChequeTransDtlsDTO dtlsDTO = new ChequeTransDtlsDTO();
            dtlsDTO.setDrcr("D");
            dtlsDTO.setTransAmount(amount);
            dtlsDTO.setBranchCode(chequeTransDTO.getBranchCode());
            dtlsDTO.setGlId(accountsUtilities.getGlDebitAccount(RevenueItems.CPL,subclassId).getCoId());
            dtlsDTO.setNarrative(chequeTransDTO.getNarration()+" of Payment of "+amount);
            chequeTransList.add(dtlsDTO);
        }
        chequeTransDTO.setGlTrans(chequeTransList);
        paymentService.createRequistion(chequeTransDTO);
    }

    @Override
    public Page<ServiceProviderTypesDTO> findServiceProviderTypes(String searchValue, Pageable pageable) {
        final List<ServiceProviderTypesDTO> serviceProviderTypes = new ArrayList<>();
        searchValue  = (searchValue!=null)?"%"+searchValue.toLowerCase()+"%":"%%";
        List<Object[]> types = serviceProviderTypesRepo.findServProviderTypes(searchValue,pageable.getPageNumber(), pageable.getPageSize());
        long rowCount = 0L;
        if(!types.isEmpty()) rowCount = ((BigInteger)types.get(0)[2]).intValue();
        for(Object[] tran:types){
            ServiceProviderTypesDTO typesDTO = new ServiceProviderTypesDTO();
            typesDTO.setTypeId(((BigInteger) tran[0]).longValue());
            typesDTO.setProviderType((String) tran[1]);
            serviceProviderTypes.add(typesDTO);
        }
        return new PageImpl<>(serviceProviderTypes,pageable, rowCount);
    }

    @Override
    public Page<ServiceProviderDTO> findServiceProviders(String searchValue, Pageable pageable) {
        final List<ServiceProviderDTO> serviceProviderTypes = new ArrayList<>();
        searchValue  = (searchValue!=null)?"%"+searchValue.toLowerCase()+"%":"%%";
        List<Object[]> types = serviceProviderRepo.findServProvidersLov(searchValue,pageable.getPageNumber(), pageable.getPageSize());
        long rowCount = 0L;
        if(!types.isEmpty()) rowCount = ((BigInteger)types.get(0)[2]).intValue();
        for(Object[] tran:types){
            ServiceProviderDTO typesDTO = new ServiceProviderDTO();
            typesDTO.setProviderId(((BigInteger) tran[0]).longValue());
            typesDTO.setName((String) tran[1]);
            serviceProviderTypes.add(typesDTO);
        }
        return new PageImpl<>(serviceProviderTypes,pageable, rowCount);
    }

    @Override
    public void createServiceProviderTypes(ServiceProviderTypesDTO serviceProviderTypes) throws BadRequestException {
        if(serviceProviderTypes.getProviderType()==null && serviceProviderTypes.getProviderType().length() < 5){
            throw new BadRequestException("Enter Valid Service Provider Type");
        }
        ServiceProviderTypes providerTypes;
        if(serviceProviderTypes.getTypeId()!=null){
            providerTypes = serviceProviderTypesRepo.findOne(serviceProviderTypes.getTypeId());
            if(providerTypes==null) {
                throw new BadRequestException("Invalid Service Provider to update...");
            }
        }
        else{
            long count = serviceProviderTypesRepo.countExactServProviderTypes(serviceProviderTypes.getProviderType());
            if(count==1){
                throw new BadRequestException("Service Provider Exists....");
            }
            providerTypes = new ServiceProviderTypes();
        }
        providerTypes.setProviderType(serviceProviderTypes.getProviderType());
        serviceProviderTypesRepo.save(providerTypes);
    }

    @Override
    public void createServiceProviders(ServiceProviderDTO serviceProviderDTO) throws BadRequestException {
        if(serviceProviderDTO.getProviderTypeId()==null){
            throw new BadRequestException("Invalid Service Provider Type...");
        }
        ServiceProviderTypes providerTypes = serviceProviderTypesRepo.findOne(serviceProviderDTO.getProviderTypeId());
        ServiceProviderDef serviceProviderDef = null;
        if(serviceProviderDTO.getProviderId()!=null){
            serviceProviderDef = serviceProviderRepo.findOne(serviceProviderDTO.getProviderId());
            if(serviceProviderDef==null){
                throw new BadRequestException("Invalid Service Provider to update...");
            }
        }
        else{
            serviceProviderDef = new ServiceProviderDef();
        }
        serviceProviderDef.setCreatedDate(new Date());
        serviceProviderDef.setEmail(serviceProviderDTO.getEmail());
        serviceProviderDef.setName(serviceProviderDTO.getName());
        serviceProviderDef.setPhoneNumber(serviceProviderDTO.getPhoneNumber());
        serviceProviderDef.setCreatedUser(userUtils.getCurrentUser());
        serviceProviderDef.setProviderTypes(providerTypes);
        serviceProviderRepo.save(serviceProviderDef);
    }

    @Override
    public DataTablesResult<ServiceProviderDTO> getServiceProviders(Long id,DataTablesRequest request) {
        final String search = ( request.getSearch()!=null && request.getSearch().getValue()!=null)?"%"+request.getSearch().getValue()+"%":"%%";
        final List<ServiceProviderDTO> serviceProviders = new ArrayList<>();
        List<Object[]> trans = serviceProviderRepo.findServProviders(search,id,request.getPageNumber(), request.getPageSize());
        long rowCount = 0L;
        if(!trans.isEmpty()) rowCount = ((BigInteger)trans.get(0)[7]).intValue();
        for(Object[] tran:trans){
            ServiceProviderDTO serviceProviderDTO = new ServiceProviderDTO();
            serviceProviderDTO.setProviderId(((BigInteger)tran[0]).longValue());
            serviceProviderDTO.setEmail((String) tran[1]);
            serviceProviderDTO.setName((String) tran[2]);
            serviceProviderDTO.setPhoneNumber((String) tran[3]);
            serviceProviderDTO.setCreatedDate((Date) tran[4]);
            serviceProviderDTO.setProviderTypeId(((BigInteger)tran[5]).longValue());
            serviceProviderDTO.setCreatedBy((String) tran[6]);
            serviceProviders.add(serviceProviderDTO);
        }
        Page<ServiceProviderDTO>  page = new PageImpl<>(serviceProviders,request, rowCount);
        return new DataTablesResult<>(request, page);
    }

    @Override
    public void deleteServiceProvider(Long providerId) throws BadRequestException {
        ServiceProviderDef serviceProviderDef = serviceProviderRepo.findOne(providerId);
        if(serviceProviderDef==null){
            throw new BadRequestException("Invalid Service Provider to delete...");
        }
        serviceProviderRepo.delete(providerId);
    }

    @Override
    public void deleteServiceProviderType(Long providerId) throws BadRequestException {
        ServiceProviderTypes serviceProviderDef = serviceProviderTypesRepo.findOne(providerId);
        if(serviceProviderDef==null){
            throw new BadRequestException("Invalid Service Provider Type to delete...");
        }
        serviceProviderTypesRepo.delete(providerId);
    }
}