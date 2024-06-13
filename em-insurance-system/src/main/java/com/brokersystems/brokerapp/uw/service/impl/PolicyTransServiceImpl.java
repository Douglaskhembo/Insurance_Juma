
package com.brokersystems.brokerapp.uw.service.impl;

import com.brokersystems.brokerapp.certs.model.*;
import com.brokersystems.brokerapp.certs.repository.PolicyCertsRepo;
import com.brokersystems.brokerapp.certs.repository.PrintQueueRepo;
import com.brokersystems.brokerapp.certs.repository.SubclassCertTypesRepo;
import com.brokersystems.brokerapp.claims.dtos.ClaimantsDTO;
import com.brokersystems.brokerapp.claims.model.*;
import com.brokersystems.brokerapp.claims.repository.ClaimPerilsRepo;
import com.brokersystems.brokerapp.claims.repository.ClaimsBookingRepo;
import com.brokersystems.brokerapp.enums.AccountTypeEnum;
import com.brokersystems.brokerapp.enums.RevenueItems;
import com.brokersystems.brokerapp.enums.SectionTypes;
import com.brokersystems.brokerapp.kie.rules.GeneralTransRulesExecutor;
import com.brokersystems.brokerapp.life.model.LifeReceipts;
import com.brokersystems.brokerapp.life.model.PolicyInstallments;
import com.brokersystems.brokerapp.life.model.QLifeReceipts;
import com.brokersystems.brokerapp.life.model.QPolicyInstallments;
import com.brokersystems.brokerapp.life.repository.LifeReceiptsRepo;
import com.brokersystems.brokerapp.life.repository.PolicyInstallmentsRepo;
import com.brokersystems.brokerapp.life.service.LifeService;
import com.brokersystems.brokerapp.medical.model.*;
import com.brokersystems.brokerapp.medical.repository.*;
import com.brokersystems.brokerapp.schedules.model.*;
import com.brokersystems.brokerapp.schedules.repository.ScheduleMappingRepo;
import com.brokersystems.brokerapp.schedules.repository.ScheduleTransRepo;
import com.brokersystems.brokerapp.schedules.service.ScheduleService;
import com.brokersystems.brokerapp.security.CheckAuthLimits;
import com.brokersystems.brokerapp.server.datatables.DataTablesRequest;
import com.brokersystems.brokerapp.server.datatables.DataTablesResult;
import com.brokersystems.brokerapp.server.exception.AdminFeeException;
import com.brokersystems.brokerapp.server.exception.BadRequestException;
import com.brokersystems.brokerapp.server.utils.*;
import com.brokersystems.brokerapp.setup.dto.BranchDTO;
import com.brokersystems.brokerapp.setup.dto.CurrencyDTO;
import com.brokersystems.brokerapp.setup.dto.PaymentModesDTO;
import com.brokersystems.brokerapp.setup.model.*;

import com.brokersystems.brokerapp.setup.repository.*;
import com.brokersystems.brokerapp.setup.service.OrganizationService;
import com.brokersystems.brokerapp.setup.service.ParamService;
import com.brokersystems.brokerapp.trans.model.*;
import com.brokersystems.brokerapp.trans.repository.*;
import com.brokersystems.brokerapp.uw.dtos.*;
import com.brokersystems.brokerapp.uw.mappers.EndorsementsDtoMapper;
import com.brokersystems.brokerapp.uw.model.*;
import com.brokersystems.brokerapp.uw.repository.*;
import com.brokersystems.brokerapp.uw.service.PolicyTransService;
import com.brokersystems.brokerapp.uw.service.PremComputeService;
import com.brokersystems.brokerapp.webservices.model.VehicleDetails;
import com.brokersystems.brokerapp.workflow.docs.DocType;
import com.brokersystems.brokerapp.workflow.docs.QSysWfDocs;
import com.brokersystems.brokerapp.workflow.docs.SysWfDocs;
import com.brokersystems.brokerapp.workflow.dto.WorkFlowDTO;
import com.brokersystems.brokerapp.workflow.repository.SysWfDocsRepo;
import com.brokersystems.brokerapp.workflow.utils.WorkflowService;
import com.google.common.collect.Maps;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PolicyTransServiceImpl implements PolicyTransService {

    @Autowired
    private ClaimPerilsRepo claimPerilsRepo;

    @Autowired
    private PolicyTransRepo policyRepo;

    @Autowired
    private ClientRepository clientRepo;

    @Autowired
    private BindersRepo binderRepo;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private OrganizationService orgRepo;

    @Autowired
    private CurrencyRepository currencyRepo;

    @Autowired
    private PaymentModeRepo paymentModeRepo;

    @Autowired
    private OrgBranchRepository branchRepo;

    @Autowired
    private BinderDetRepo binderDetRepo;

    @Autowired
    private BinderClauseRepo binderClauseRepo;

    @Autowired
    private PremComputeService premComputeService;

    @Autowired
    private SystemTransRepo transRepo;


    @Autowired
    private PremRatesRepo premRatesRepo;

    @Autowired
    private RiskTransRepo riskRepo;

    @Autowired
    private SubclassCertTypesRepo subclassCertTypesRepo;

    @Autowired
    private SectionTransRepo sectionRepo;

    @Autowired
    private SectionRepo setupSectionRepo;


    @Autowired
    private ScheduleMappingRepo mappingRepo;

    @Autowired
    private TransMappingRepo transMappingRepo;

    @Autowired
    private SequenceRepository sequenceRepo;

    @Autowired
    private CoverTypesRepo coverRepo;

    @Autowired
    private SubClassRepo subclassRepo;

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private ProductsRepo productRepo;

    @Autowired
    private PolTaxesRepo polTaxesRepo;

    @Autowired
    private PolClausesRepo polClausesRepo;

    @Autowired
    private TaxRatesRepo taxRatesRepo;

    @Autowired
    private SubClausesRepo subclauseRepo;
    @Autowired
    private DateUtilities dateUtils;

    @Autowired
    private LifeService lifeService;

    @Autowired
    private PolActiveRisksRepo activeRisksRepo;

    @Autowired
    private TemplateMerger templateMerger;

    @Autowired
    private PolicyRemarksRepo policyRemarksRepo;

    @Autowired
    private PolicyInstallmentsRepo policyInstallmentsRepo;

    @Autowired
    private com.brokersystems.brokerapp.certs.service.CertService certService;

    @Autowired
    private ScheduleTransRepo scheduleTransRepo;

    @Autowired
    private CheckAuthLimits authLimits;

    @Autowired
    private CommRatesRepo commRatesRepo;

    @Autowired
    private ParamService paramService;

    @Autowired
    private AdminFeeRepo adminFeeRepo;

    @Autowired
    private AdminFeePolRepo adminFeePolRepo;

    @Autowired
    private SelfFundParamsRepo selfFundParamsRepo;

    @Autowired
    private CategoryMembersRepo membersRepo;

    @Autowired
    private MedicalCategoryRepo categoryRepo;

    @Autowired
    private CategoryBenefitRepo categoryBenefitRepo;

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private SysWfDocsRepo sysWfDocsRepo;

    @Autowired
    private RiskDocsRepo riskDocsRepo;

    @Autowired
    private ValidatorUtils validatorUtils;

    @Autowired
    private RiskIntPartiesRepo riskIntPartiesRepo;

    @Autowired
    private InterestedPartiesRepo interestedPartiesRepo;

    @Autowired
    private BinderReqrdDocsRepo reqrdDocsRepo;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private RiskImportExcelUtils importExcelUtils;

    @Autowired
    private ClientTypeRepo clientTypeRepo;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private GeneralTransRulesExecutor rulesExecutor;

    @Autowired
    private TransChecksRepo transChecksRepo;

    @Autowired
    private PrintQueueRepo queueRepo;

    @Autowired
    private PolicyCertsRepo certsRepo;

    @Autowired
    private SubclassReqDocRepo subclassReqDocRepo;

    @Autowired
    private RiskImportLogRepo importLogRepo;

    @Autowired
    private BinderMedicalCardsRepo binderMedicalCardsRepo;

    @Autowired
    private PolicyBindersRepo policyBindersRepo;

    @Autowired
    private LifeReceiptsRepo lifeReceiptsRepo;

    @Autowired
    private BinderQuestionnaireRepo questionnaireRepo;

    @Autowired
    private ReceiptDetailsRepository receiptDetailsRepository;

    @Autowired
    private PolicyQuestionnaireRepo policyQuestionnaireRepo;

    @Autowired
    private SystemTransactionsTempRepo systemTransactionsTempRepo;

    @Autowired
    private BinderQuestionnaireRepo binderQuestionnaireRepo;

    @Autowired
    private PremComputeService premiumService;

    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private ClaimsBookingRepo claimsBookingRepo;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MotorVehicleDetailsRepo motorVehicleDetailsRepo;

    @Override
    public MileageDTO findMileageDetails(String riskId) {
        try {
            String url = "https://api.uat.aicare.co.ke/api/v1/products/switch/{id}/mileage";
            HttpHeaders headers = new HttpHeaders();
            headers.set("x-email", "speedflakes@gmail.com");
            headers.set("x-api-key", "API_KEY_p7xdXO68V79lqz4jWZHrnDfuruZPTUlLFDhP8Yh6kWCmtkbtm5");
            HttpEntity requestEntity = new HttpEntity<>(headers);
            ResponseEntity<String> response = new RestTemplate().exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    String.class,
                    riskId
            );
            String responseBody = response.getBody();
            JSONObject jsonObject = new JSONObject(responseBody);
            JSONObject resultOb = jsonObject.getJSONObject("switch");
            final MileageDTO mileageDTO = new MileageDTO();
            if (resultOb.has("mileage")) {
                double mileage = resultOb.getDouble("mileage");
                mileageDTO.setMileage(BigDecimal.valueOf(mileage));
            }
            if (resultOb.has("start_mileage")) {
                double mileage = resultOb.getDouble("start_mileage");
                mileageDTO.setStart_mileage(BigDecimal.valueOf(mileage));
            }
            if (resultOb.has("used_mileage")) {
                double mileage = resultOb.getDouble("used_mileage");
                mileageDTO.setUsed_mileage(BigDecimal.valueOf(mileage));
            }
            System.out.println(responseBody);
            return mileageDTO;
        }
        catch (Exception ex){

        }
        return new MileageDTO();
    }

    @Override
    @Transactional(readOnly = true)
    public DataTablesResult<WorkFlowDTO> findUserPolicies(DataTablesRequest request) {
        final String search = ( request.getSearch()!=null && request.getSearch().getValue()!=null)?"%"+request.getSearch().getValue()+"%":"%%";
        List<Object[]> ticketsList = sysWfDocsRepo.getDashBoardTickets(search.toLowerCase(),userUtils.getCurrentUser().getId(),request.getPageNumber(), request.getPageSize());
        long rowCount = 0L;
        if(!ticketsList.isEmpty()) rowCount = (Integer)ticketsList.get(0)[9];
        final List<WorkFlowDTO> workFlowDTOList = new ArrayList<>();
        for(Object[] ticket:ticketsList){
            WorkFlowDTO workFlowDTO = new WorkFlowDTO();
            workFlowDTO.setTaskId(((BigDecimal)ticket[0]).longValue());
            workFlowDTO.setActiveProcess((String)ticket[1]);
            workFlowDTO.setRefNo((String)ticket[2]);
            workFlowDTO.setClientName(ticket[3]+" "+ticket[4]);
            workFlowDTO.setUsername((String)ticket[5]);
            workFlowDTO.setCreatedDate((Date) ticket[6]);
            workFlowDTO.setTransactionId(((BigDecimal)ticket[7]).longValue());
            workFlowDTO.setTransType((String)ticket[8]);
            workFlowDTOList.add(workFlowDTO);
        }
        Page<WorkFlowDTO>  page = new PageImpl<>(workFlowDTOList,request, rowCount);
        return new DataTablesResult<>(request, page);

    }

    @Override
    public DataTablesResult<SysWfDocs> findSearchTickets(DataTablesRequest request, String policyNo, String quoteNo, String preparedBy) throws IllegalAccessException {
        if (policyNo == null) policyNo = "";
        if (quoteNo == null) quoteNo = "";
        if (preparedBy == null) preparedBy = "";
        BooleanExpression pred = null;
        if (policyNo.length() > 0)
            pred = QSysWfDocs.sysWfDocs.policyTrans.polNo.contains(policyNo).and(QSysWfDocs.sysWfDocs.active.eq(true));
        else if (quoteNo.length() > 0)
            pred = QSysWfDocs.sysWfDocs.quoteTrans.quotNo.contains(quoteNo).and(QSysWfDocs.sysWfDocs.active.eq(true));
        else if (preparedBy.length() > 0)
            pred = QSysWfDocs.sysWfDocs.userId.username.contains(preparedBy).and(QSysWfDocs.sysWfDocs.active.eq(true));

        else pred = QSysWfDocs.sysWfDocs.active.eq(true);

        Page<SysWfDocs> page = sysWfDocsRepo.findAll(pred.and(request.searchPredicate(QSysWfDocs.sysWfDocs)), request);
        return new DataTablesResult<>(request, page);
    }

    @Override
    @Transactional(readOnly = true)
    public DataTablesResult<PolicyTrans> findUserMedicalTrans(DataTablesRequest request) throws IllegalAccessException {
        BooleanExpression pred = QPolicyTrans.policyTrans.createdUser.eq(userUtils.getCurrentUser()).and(QPolicyTrans.policyTrans.authStatus.eq("D").or(QPolicyTrans.policyTrans.authStatus.eq("R"))).and(QPolicyTrans.policyTrans.product.proGroup.prgType.equalsIgnoreCase("MD"));
        Page<PolicyTrans> page = policyRepo.findAll(pred.and(request.searchPredicate(QPolicyTrans.policyTrans)),
                request);
        return new DataTablesResult<>(request, page);
    }

//    @Override
//    @Transactional(readOnly = true)
//    public Page<ClientDef> findActiveClients(String paramString, Pageable paramPageable) {
//        Predicate pred = null;
//        if (paramString == null || StringUtils.isBlank(paramString)) {
//            pred = QClientDef.clientDef.isNotNull().and(QClientDef.clientDef.authStatus.eq("Y"));
//        } else {
//            pred = (QClientDef.clientDef.authStatus.eq("Y") .and(QClientDef.clientDef.status.notIn("T")))
//                    .and(QClientDef.clientDef.fname.containsIgnoreCase(paramString)
//                            .or(QClientDef.clientDef.otherNames.concat(QClientDef.clientDef.fname).containsIgnoreCase(paramString))
//                            .or(QClientDef.clientDef.idNo.contains(paramString))
//                            .or(QClientDef.clientDef.passportNo.contains(paramString))
//                            .or(QClientDef.clientDef.tenantNumber.containsIgnoreCase(paramString)));
//        }
//        return clientRepo.findAll(pred, paramPageable);
//    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClientsDto> findActiveClients(String paramString, Pageable pageable) {
        StringBuilder searchValue = new StringBuilder();
        if (paramString == null) searchValue = new StringBuilder("%%");
        else {
            String[] splitStr = paramString.trim().split("\\s+");
            for (String search : splitStr) {
                searchValue.append("%").append(search).append("%");
            }
        }
        final List<ClientsDto> clientDTOList = new ArrayList<>();
        List<Object[]> clientsList = clientRepo.searchClientsLists(searchValue.toString(), pageable.getPageNumber(),pageable.getPageSize());
        long rowCount = 0L;
        if(!clientsList.isEmpty()) rowCount = ((BigInteger)clientsList.get(0)[4]).intValue();
        for(Object[] client:clientsList ){
            ClientsDto clientDTO = new ClientsDto();
            clientDTO.setTenId(((BigInteger) client[0]).longValue());
            clientDTO.setFname((String) client[2]);
            clientDTO.setOtherNames((String) client[3]);
            clientDTO.setTenantNumber((String) client[1]);
            clientDTOList.add(clientDTO);
        }
        return new PageImpl<>(clientDTOList, pageable, rowCount);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BinderDTO> findInsuranceBinder(String paramString, Pageable pageable, String bindType, Long productId) {
        StringBuilder searchValue = new StringBuilder();
        if (paramString == null) searchValue = new StringBuilder("%%");
        else {
            String[] splitStr = paramString.trim().split("\\s+");
            for (String search : splitStr) {
                searchValue.append("%").append(search).append("%");
            }
        }
        final List<BinderDTO> binders = new ArrayList<>();
        List<Object[]> binderList = binderRepo.searchProductBinders(searchValue.toString(), pageable.getPageNumber(),pageable.getPageSize());
        long rowCount = 0L;
        if(!binderList.isEmpty()) rowCount = ((BigInteger)binderList.get(0)[9]).intValue();
        for(Object[] binder:binderList ){
            BinderDTO binderDTO = new BinderDTO();
            binderDTO.setBinId(((BigInteger) binder[0]).longValue());
            binderDTO.setName((String) binder[1]);
            binderDTO.setProDesc((String) binder[2]);
            binderDTO.setAcctId(((BigInteger) binder[3]).longValue());
            binderDTO.setProCode(((BigInteger) binder[4]).longValue());
            binderDTO.setBinPolNo((String) binder[5]);
            binderDTO.setBinName((String) binder[6]);
            binderDTO.setAgeApplicable((String) binder[7]);
            binderDTO.setMotorProduct((Boolean) binder[8]);
            binders.add(binderDTO);
        }
        return new PageImpl<>(binders, pageable, rowCount);
    }


    @Override
    public Page<ProductsDef> findMultiProducts(String paramString, Pageable paramPageable) {
        Predicate pred = null;
        if (paramString == null || StringUtils.isBlank(paramString)) {
            pred = QProductsDef.productsDef.isNotNull()
                    .and(QProductsDef.productsDef.proGroup.prgType.equalsIgnoreCase("MP"))
                    .and(QProductsDef.productsDef.active.eq(Boolean.TRUE));

        } else {
            pred = QProductsDef.productsDef.isNotNull().and(QProductsDef.productsDef.proShtDesc.equalsIgnoreCase(paramString).or(QProductsDef.productsDef.proDesc.equalsIgnoreCase(paramString)))
                    .and(QProductsDef.productsDef.proGroup.prgType.equalsIgnoreCase("MP"))
                    .and(QProductsDef.productsDef.active.eq(Boolean.TRUE));
        }
        return productRepo.findAll(pred, paramPageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PolicyTrans> findClientPolicies(String paramString, Pageable paramPageable, Long clientId) {
        Predicate pred = null;
        if (paramString == null || StringUtils.isBlank(paramString)) {
            pred = QPolicyTrans.policyTrans.client.tenId.eq(clientId).and(QPolicyTrans.policyTrans.isNotNull()).and(QPolicyTrans.policyTrans.currentStatus.equalsIgnoreCase("A"));
        } else {
            pred = QPolicyTrans.policyTrans.client.tenId.eq(clientId).and(QPolicyTrans.policyTrans.polNo.containsIgnoreCase(paramString)
                    .or(QPolicyTrans.policyTrans.clientPolNo.containsIgnoreCase(paramString)))
                    .and(QPolicyTrans.policyTrans.currentStatus.equalsIgnoreCase("A"));
        }
        return policyRepo.findAll(pred, paramPageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<BindersDef> findLifeBinder(String paramString, Pageable paramPageable, String bindType) {
        Predicate pred = null;
        if (paramString == null || StringUtils.isBlank(paramString)) {
            pred = QBindersDef.bindersDef.binType.eq(bindType).and(QBindersDef.bindersDef.isNotNull()).and(QBindersDef.bindersDef.product.proGroup.prgType.equalsIgnoreCase("L").
                    and(QBindersDef.bindersDef.binStatus.equalsIgnoreCase("Authorised"))
                    .and(QBindersDef.bindersDef.active.eq(Boolean.TRUE)));
        } else {
            pred = QBindersDef.bindersDef.binType.eq(bindType).and(QBindersDef.bindersDef.binName.containsIgnoreCase(paramString)
                    .or(QBindersDef.bindersDef.binShtDesc.containsIgnoreCase(paramString)))
                    .and(QBindersDef.bindersDef.product.proGroup.prgType.equalsIgnoreCase("L")
                            .and(QBindersDef.bindersDef.binStatus.equalsIgnoreCase("Authorised"))
                            .and(QBindersDef.bindersDef.active.eq(Boolean.TRUE)));
        }
        return binderRepo.findAll(pred, paramPageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BindersDef> findAllBinders(String paramString, Pageable paramPageable) {
        Predicate pred = null;
        if (paramString == null || StringUtils.isBlank(paramString)) {
            pred = QBindersDef.bindersDef.binStatus.equalsIgnoreCase("Authorised")
                    .and(QBindersDef.bindersDef.active.eq(Boolean.TRUE));
        } else {
            pred = QBindersDef.bindersDef.binName.containsIgnoreCase(paramString)
                    .or(QBindersDef.bindersDef.binShtDesc.containsIgnoreCase(paramString))
                    .and(QBindersDef.bindersDef.binStatus.equalsIgnoreCase("Authorised"))
                    .and(QBindersDef.bindersDef.active.eq(Boolean.TRUE));
        }
        return binderRepo.findAll(pred, paramPageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<CurrencyDTO> findCurrencies(String paramString, Pageable pageable) {
        final String search = (paramString!=null)?"%"+paramString+"%":"%%";
        List<Object[]> currencies = currencyRepo.findSystemCurrencies(search.toLowerCase(),pageable.getPageNumber(), pageable.getPageSize());
        final List<CurrencyDTO> currencyDTOList = new ArrayList<>();
        long rowCount = 0L;
        if(!currencies.isEmpty()) rowCount = (Integer)currencies.get(0)[2];
        for(Object[] currency:currencies){
            CurrencyDTO currencyDTO = new CurrencyDTO();
            currencyDTO.setCurCode(((BigInteger)currency[0]).longValue());
            currencyDTO.setCurName((String)currency[1]);
            currencyDTOList.add(currencyDTO);
        }
        return new PageImpl<>(currencyDTOList,pageable,rowCount);
    }

    @Override
    public Page<CurrencyDTO> findOtherCurrencies(String paramString, Pageable pageable) {
        final String search = (paramString!=null)?"%"+paramString+"%":"%%";
        List<Object[]> currencies = currencyRepo.findOtherSystemCurrencies(search.toLowerCase(),pageable.getPageNumber(), pageable.getPageSize());
        final List<CurrencyDTO> currencyDTOList = new ArrayList<>();
        long rowCount = 0L;
        if(!currencies.isEmpty()) rowCount = ((BigInteger)currencies.get(0)[2]).intValue();
        for(Object[] currency:currencies){
            CurrencyDTO currencyDTO = new CurrencyDTO();
            currencyDTO.setCurCode(((BigInteger)currency[0]).longValue());
            currencyDTO.setCurName((String)currency[1]);
            currencyDTOList.add(currencyDTO);
        }
        return new PageImpl<>(currencyDTOList,pageable,rowCount);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentModesDTO> findPaymentModes(String paramString, Pageable pageable) {
        final String search = (paramString!=null)?"%"+paramString+"%":"%%";
        List<Object[]> paymentmodes = paymentModeRepo.findPymentModes(search.toLowerCase(),pageable.getPageNumber(), pageable.getPageSize());
        final List<PaymentModesDTO> paymentModesDTOList = new ArrayList<>();
        long rowCount = 0L;
        if(!paymentmodes.isEmpty()) rowCount = ((BigInteger)paymentmodes.get(0)[2]).intValue();
        for(Object[] paymentmode:paymentmodes){
            PaymentModesDTO paymentModesDTO = new PaymentModesDTO();
            paymentModesDTO.setPmId(((BigInteger)paymentmode[0]).longValue());
            paymentModesDTO.setPmDesc((String)paymentmode[1]);
            paymentModesDTOList.add(paymentModesDTO);
        }
        return new PageImpl<>(paymentModesDTOList,pageable,rowCount);
    }


    @Override
    public Page<AccountDef> findInhouseAgents(String paramString, Pageable paramPageable) {
        Predicate pred = null;
        if (paramString == null || StringUtils.isBlank(paramString)) {
            pred = QAccountDef.accountDef.isNotNull().and(QAccountDef.accountDef.accountType.accountType.eq(AccountTypeEnum.IA))
                    .and(QAccountDef.accountDef.wef.loe(new Date())).and(QAccountDef.accountDef.wet.coalesce(DateUtils.addDays(new Date(), 1)).asDate().after(new Date()));
        } else {
            pred = QAccountDef.accountDef.name.containsIgnoreCase(paramString).and(QAccountDef.accountDef.accountType.accountType.eq(AccountTypeEnum.IA))
                    .and(QAccountDef.accountDef.wef.loe(new Date())).and(QAccountDef.accountDef.wet.coalesce(DateUtils.addDays(new Date(), 1)).asDate().after(new Date()));
        }
        return accountRepo.findAll(pred, paramPageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BranchDTO> findUserBranches(String paramString, Pageable paramPageable) {
        final Long userLogged = userUtils.getCurrentUser().getId();
        final String search = (paramString!=null)?"%"+paramString+"%":"%%";
        List<Object[]> branches = branchRepo.findBranches(search.toLowerCase(),userLogged,paramPageable.getPageNumber(), paramPageable.getPageSize());
        final List<BranchDTO> branchDTOList = new ArrayList<>();
        long rowCount = 0l;
        if(!branches.isEmpty()) rowCount = ((BigInteger)branches.get(0)[2]).intValue();
        for(Object[] branch:branches){
            BranchDTO branchDTO = new BranchDTO();
            branchDTO.setObId(((BigInteger)branch[0]).longValue());
            branchDTO.setObName((String)branch[1]);
            branchDTOList.add(branchDTO);
        }
        return new PageImpl<>(branchDTOList,paramPageable,rowCount);
    }

    @Override
    public Page<BranchDTO> findAllBranches(String paramString, Pageable paramPageable) {
        final String search = (paramString!=null)?"%"+paramString+"%":"%%";
        List<Object[]> branches = branchRepo.findAllBranches(search.toLowerCase(),paramPageable.getPageNumber(), paramPageable.getPageSize());
        final List<BranchDTO> branchDTOList = new ArrayList<>();
        long rowCount = 0l;
        if(!branches.isEmpty()) rowCount = ((BigInteger)branches.get(0)[2]).intValue();
        for(Object[] branch:branches){
            BranchDTO branchDTO = new BranchDTO();
            branchDTO.setObId(((BigInteger)branch[0]).longValue());
            branchDTO.setObName((String)branch[1]);
            branchDTOList.add(branchDTO);
        }
        return new PageImpl<>(branchDTOList,paramPageable,rowCount);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SubClassDef> findBinderSubclasses(String paramString, Pageable paramPageable, Long bindCode) {
        Predicate pred = null;
        if (paramString == null || StringUtils.isBlank(paramString)) {
            pred = QBinderDetails.binderDetails.binder.binId.eq(bindCode).and(QBinderDetails.binderDetails.isNotNull());
        } else {
            pred = QBinderDetails.binderDetails.binder.binId.eq(bindCode).and(QBinderDetails.binderDetails.subCoverTypes.subclass.subDesc.containsIgnoreCase(paramString)
                    .or(QBinderDetails.binderDetails.subCoverTypes.subclass.subShtDesc.containsIgnoreCase(paramString)));
        }
        List<BinderDetails> bindeDetails = binderDetRepo.findAll(pred, paramPageable).getContent();
        List<SubClassDef> subclassList =
                bindeDetails.stream().map(det -> {
                    SubClassDef subclass = det.getSubCoverTypes().getSubclass();
                    return subclass;
                }).distinct().collect(Collectors.toList());
        Page<SubClassDef> subclasses = new PageImpl<>(subclassList);
        return subclasses;
    }


    @Override
    @Transactional(readOnly = true)
    public Page<CoverTypesDef> findBinderCoverTypes(String paramString, Pageable paramPageable, Long bindCode, Long subCode) {
        Predicate pred = null;
        if (paramString == null || StringUtils.isBlank(paramString)) {
            pred = QBinderDetails.binderDetails.subCoverTypes.subclass.subId.eq(subCode).and(QBinderDetails.binderDetails.binder.binId.eq(bindCode))
                    .and(QBinderDetails.binderDetails.isNotNull());
        } else {
            pred = QBinderDetails.binderDetails.subCoverTypes.subclass.subId.eq(subCode).and(QBinderDetails.binderDetails.binder.binId.eq(bindCode))
                    .and(QBinderDetails.binderDetails.subCoverTypes.coverTypes.covName.containsIgnoreCase(paramString)
                            .or(QBinderDetails.binderDetails.subCoverTypes.coverTypes.covShtDesc.containsIgnoreCase(paramString)));
        }
        List<BinderDetails> bindeDetails = binderDetRepo.findAll(pred, paramPageable).getContent();
        List<CoverTypesDef> coverTypesList = bindeDetails.stream().map(det -> {
            List<BigDecimal> commissRates = binderDetRepo.getDefaultComm(det.getDetId());

            CoverTypesDef coverType = det.getSubCoverTypes().getCoverTypes();
            coverType.setDetId(det.getDetId());
            final String distribution = det.getDistribution();
            String firstInstallment = null;
            if (distribution != null && distribution.contains(":")) {
                firstInstallment = distribution.split(":")[0];
            } else firstInstallment = distribution;
            coverType.setDistribution(firstInstallment);
            coverType.setInstallmentsNo(det.getInstallmentsNo());
            if(commissRates.size()==1){
                coverType.setCommRate(commissRates.get(0));
            }
            return coverType;
        }).collect(Collectors.toList());
        Page<CoverTypesDef> covertypes = new PageImpl<>(coverTypesList);
        return covertypes;
    }

    @Override
    public Page<CoverTypesDef> findBinderSubCoverTypes(String paramString, Pageable paramPageable, Long bindCode) {
        Predicate pred = null;
        if (paramString == null || StringUtils.isBlank(paramString)) {
            pred = (QBinderDetails.binderDetails.binder.binId.eq(bindCode))
                    .and(QBinderDetails.binderDetails.isNotNull());
        } else {
            pred = (QBinderDetails.binderDetails.binder.binId.eq(bindCode))
                    .and(QBinderDetails.binderDetails.subCoverTypes.coverTypes.covName.containsIgnoreCase(paramString)
                            .or(QBinderDetails.binderDetails.subCoverTypes.coverTypes.covShtDesc.containsIgnoreCase(paramString)));
        }
        List<BinderDetails> bindeDetails = binderDetRepo.findAll(pred, paramPageable).getContent();
        List<CoverTypesDef> coverTypesList = bindeDetails.stream().map(det -> {
            CoverTypesDef coverType = det.getSubCoverTypes().getCoverTypes();
            coverType.setDetId(det.getDetId());
            coverType.setSubclassName(det.getSubCoverTypes().getSubclass().getSubDesc());
            return coverType;
        }).collect(Collectors.toList());
        Page<CoverTypesDef> covertypes = new PageImpl<>(coverTypesList);
        return covertypes;
    }

    @Override
    @Transactional(readOnly = true)
    public Set<RiskSectionBean> getBinderPremRates(Long detId) {
        Iterable<PremRatesDef> premiumRates = premRatesRepo.findAll(QPremRatesDef.premRatesDef.binderDet.detId.eq(detId).and(QPremRatesDef.premRatesDef.active.eq(true)));
        Set<RiskSectionBean> premRatesDefs = Streamable.streamOf(premiumRates).map(a -> {
            RiskSectionBean sectionBean = new RiskSectionBean();
            sectionBean.setPk(a.getId());
            sectionBean.setPremId(a.getSection().getId().longValue());
            sectionBean.setDivFactor(a.getDivFactor());
            sectionBean.setSectionDesc(a.getSection().getDesc());
            sectionBean.setRate(a.getRate());
            sectionBean.setLimitsAllowed(a.getBinderDet().getLimitsAllowed());
            sectionBean.setFreeLimit(a.getFreeLimit());
            sectionBean.setRatesApplicable(a.getRatesApplicable());
            if (a.getSection().getType() == SectionTypes.RD) {
                sectionBean.setRider("Y");
            } else sectionBean.setRider("N");
            sectionBean.setMandatory((a.getMandatory() != null && "Y".equalsIgnoreCase(a.getMandatory())) ? "Y" : "N");
            return sectionBean;
        }).collect(Collectors.toSet());
        return premRatesDefs;
    }

    @Override
    @Transactional(readOnly = true)
    public Set<RiskSectionBean> getBinderClientPremRates(Long detId, Long insuredAge) {
        Iterable<PremRatesDef> premiumRates = premRatesRepo.getPremiumRates(detId, BigDecimal.valueOf(insuredAge));
        Set<RiskSectionBean> premRatesDefs = Streamable.streamOf(premiumRates).map(a -> {
            RiskSectionBean sectionBean = new RiskSectionBean();
            sectionBean.setPk(a.getId());
            sectionBean.setPremId(a.getSection().getId().longValue());
            sectionBean.setDivFactor(a.getDivFactor());
            sectionBean.setSectionDesc(a.getSection().getDesc());
            sectionBean.setRate(a.getRate());
            sectionBean.setLimitsAllowed(a.getBinderDet().getLimitsAllowed());
            sectionBean.setFreeLimit(a.getFreeLimit());
            sectionBean.setRatesApplicable(a.getRatesApplicable());
            sectionBean.setRangeType(a.getRangeType());
            if (a.getSection().getType() == SectionTypes.RD) {
                sectionBean.setRider("Y");
            } else sectionBean.setRider("N");
            sectionBean.setMandatory((a.getMandatory() != null && "Y".equalsIgnoreCase(a.getMandatory())) ? "Y" : "N");
            return sectionBean;
        }).collect(Collectors.toSet());
        return premRatesDefs;
    }


    @Override
    public PolicyTrans createLifePolicy(PolicyTrans policy) throws BadRequestException {
        if (policy.getBinder() != null && "S".equalsIgnoreCase(policy.getBinder().getBinType())) {
            if (binderRepo.count(QBindersDef.bindersDef.fundBinder.equalsIgnoreCase("Y")) != 1) {
                throw new BadRequestException("A Fund Binder has not been set up ..Check your configuration");
            }
            BindersDef fundBinder = binderRepo.findOne(QBindersDef.bindersDef.fundBinder.equalsIgnoreCase("Y"));
            policy.setBindCode(fundBinder.getBinId());
            policy.setProdId(fundBinder.getProduct().getProCode());
            policy.setAgentId(fundBinder.getAccount().getAcctId());
        }
        PolicyTrans editPolicy = new PolicyTrans();
        if (policy.getPolicyId() != null) {
            editPolicy = policyRepo.findOne(policy.getPolicyId());
        }
        if (policy.getClientId() == null) throw new BadRequestException("Client is Mandatory");
        if (policy.getBindCode() == null) throw new BadRequestException("Binder is Mandatory ");
        if (policy.getAgentId() == null) throw new BadRequestException("Intermediary is Mandatory");
        if (policy.getProdId() == null) throw new BadRequestException("Product is Mandatory");
        if (policy.getPaymentId() == null) throw new BadRequestException("Payment Mode is Mandatory");
        if (policy.getBranchId() == null) throw new BadRequestException("Branch is Mandatory");
        if (policy.getCurrencyId() == null) throw new BadRequestException("Currency is Mandatory");
        if (policy.getWefDate() == null) throw new BadRequestException("Policy Wef Date From is Mandatory");
        if (policy.getWetDate() == null) throw new BadRequestException("Policy Wet Date From is Mandatory");
        if (policy.getWefDate().after(policy.getWetDate()))
            throw new BadRequestException("Wef Date cannot be greater than Wet Date");
        System.out.println(policy.getPolTerm());
//        Date polWetDate = dateUtils.removeTime(policy.getWetDate());
        User user = userUtils.getCurrentUser();
        if (policy.getBusinessType() == null) throw new BadRequestException("Select Business Type");
        if ("N".equalsIgnoreCase(policy.getBusinessType())) {
            if (policy.getTransType() == null || "NB".equalsIgnoreCase(policy.getTransType())) {
//                if (policy.getWetDate().after(polWetDate) || policy.getWetDate().before(polWetDate))
//                    throw new BadRequestException("The Policy Cover Period Cannot be more than a year");
            } else if ("EN".equalsIgnoreCase(policy.getTransType()) || "CN".equalsIgnoreCase(policy.getTransType())) {
                PolicyTrans currentTrans = policyRepo.findOne(policy.getPolicyId());
                PolicyTrans prevTrans = currentTrans.getPreviousTrans();
                Date currentWet = dateUtils.removeTime(policy.getWetDate());
                Date prevWet = dateUtils.removeTime(prevTrans.getWetDate());
                if (currentWet.after(prevWet) || currentWet.before(prevWet))
                    throw new BadRequestException("Policy WET Date Cannot change for endorsements...");
            }

        }

//        if ("S".equalsIgnoreCase(policy.getBusinessType()))
//            if (policy.getWetDate().after(polWetDate))
//                throw new BadRequestException("The Short Period Policy Cover Period Cannot be more than a year");

        boolean newTrans = false;
        Iterable<BinderReqrdDocs> reqdDocs = new ArrayList<>();
        if (policy.getRiskBean() != null)
            if (policy.getRiskBean().getSclCode() != null) {
                String transType = "";
                if (policy.getTransType() == null || StringUtils.isBlank(policy.getTransType())) {
                    transType = "NB";
                } else
                    transType = policy.getTransType();
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
                if ("NB".equalsIgnoreCase(transType)) {
                    reqdDocs = reqrdDocsRepo.findAll(QBinderReqrdDocs.binderReqrdDocs.binderDetail.detId.eq(policy.getRiskBean().getBinderDet())
                            .and(QBinderReqrdDocs.binderReqrdDocs.mandatory.eq(true))
                            .and(QBinderReqrdDocs.binderReqrdDocs.requiredDocs.requiredDoc.appliesNewBusiness.eq(true)));
                } else if ("EN".equalsIgnoreCase(transType)) {
                    reqdDocs = reqrdDocsRepo.findAll(QBinderReqrdDocs.binderReqrdDocs.binderDetail.detId.eq(policy.getRiskBean().getBinderDet())
                            .and(QBinderReqrdDocs.binderReqrdDocs.mandatory.eq(true))
                            .and(QBinderReqrdDocs.binderReqrdDocs.requiredDocs.requiredDoc.appliesEndorsement.eq(true)));
                } else if ("RN".equalsIgnoreCase(transType)) {
                    reqdDocs = reqrdDocsRepo.findAll(QBinderReqrdDocs.binderReqrdDocs.binderDetail.detId.eq(policy.getRiskBean().getBinderDet())
                            .and(QBinderReqrdDocs.binderReqrdDocs.mandatory.eq(true))
                            .and(QBinderReqrdDocs.binderReqrdDocs.requiredDocs.requiredDoc.appliesRenewal.eq(true)));
                }

            }
        ProductsDef policyProduct = productRepo.findOne(policy.getProdId());
        policy.setCurrentStatus("D");
        policy.setAuthStatus("D");
        // SystemTrans transaction = null;
        boolean medicalProduct = false;
        boolean lifeProduct = false;
        boolean motorProduct = policyProduct.isMotorProduct();
        if (policyProduct.getProGroup().getPrgType() == null || !policyProduct.getProGroup().getPrgType().equalsIgnoreCase("MD")) {
            medicalProduct = false;
            lifeProduct = false;
        } else if (policyProduct.getProGroup().getPrgType().equalsIgnoreCase("MD")) {
            medicalProduct = true;
            lifeProduct = false;

        } else if (policyProduct.getProGroup().getPrgType().equalsIgnoreCase("L")) {
            medicalProduct = false;
            lifeProduct = true;
        }
        if (policy.getPolicyId() == null) {

            newTrans = true;
            String policyNumberFormat = paramService.getParameterString("POLICY_NO_FORMAT");
            String endorsementFormat = paramService.getParameterString("ENDORSE_NO_FORMAT");
            String proposalFormat = paramService.getParameterString("PROPOSAL_NO_FORMAT");
            //Predicate seqPredicate = QSystemSequence.systemSequence.transType.eq("P");
            Predicate propPredicate = QSystemSequence.systemSequence.transType.eq("PR");
//            if (sequenceRepo.count(seqPredicate) == 0)
//                throw new BadRequestException("Sequence for New Business Transactions has not been defined");
            if (sequenceRepo.count(propPredicate) == 0)
                throw new BadRequestException("Sequence for Proposal Transactions has not been defined");
//            SystemSequence sequence = sequenceRepo.findOne(seqPredicate);
//            Long seqNumber = sequence.getNextNumber();
//            final String policyNumber = templateMerger.generateFormat(policyNumberFormat, policy.getBranchId(), policy.getProdId(), policy.getWefDate(), sequence.getSeqPrefix() + String.format("%05d", seqNumber));
//            policy.setPolNo(policyNumber);
//            sequence.setLastNumber(seqNumber);
//            sequence.setNextNumber(seqNumber + 1);
//            sequenceRepo.save(sequence);
            SystemSequence lifeSequence = sequenceRepo.findOne(propPredicate);
            Long lifeSeqNumber = lifeSequence.getNextNumber();
            final String proposalNo = templateMerger.generateFormat(proposalFormat, policy.getBranchId(), policy.getProdId(), policy.getWefDate(), lifeSequence.getSeqPrefix() + String.format("%05d", lifeSeqNumber), null);
            if (proposalNo == null) {
                throw new BadRequestException("Proposal no has not been generated");
            }
            System.out.println("Proposal No "+proposalNo);
            policy.setProposalNo(proposalNo);
            lifeSequence.setLastNumber(lifeSeqNumber);
            lifeSequence.setNextNumber(lifeSeqNumber + 1);
            sequenceRepo.save(lifeSequence);
            Predicate endorsePredicate = QSystemSequence.systemSequence.transType.eq("E");
            if (sequenceRepo.count(endorsePredicate) == 0)
                throw new BadRequestException("Sequence for Endorsement Transactions has not been defined");
            SystemSequence endorseSequence = sequenceRepo.findOne(endorsePredicate);
            Long endosseqNumber = endorseSequence.getNextNumber();
            final String revNumber = endorseSequence.getSeqPrefix() + String.format("%05d", endosseqNumber);
            final String endorseNumber = templateMerger.generateFormat(endorsementFormat, policy.getBranchId(), policy.getProdId(), policy.getWefDate(), revNumber, null);
            policy.setPolRevNo(endorseNumber + "/1");
            policy.setRevisionFormat(endorseNumber);
            endorseSequence.setLastNumber(endosseqNumber);
            endorseSequence.setNextNumber(endosseqNumber + 1);
            sequenceRepo.save(endorseSequence);
//            transaction = new SystemTrans();
//            transaction.setDoneDate(new Date());
//            transaction.setDoneBy(userUtils.getCurrentUser());
//            transaction.setPolicy(policy);
//            transaction.setTransLevel("U");
//            transaction.setTransCode("NBD"); //A way to setup and look up for transaction transcode
//            transaction.setTransAuthorised("N");


            if ("S".equalsIgnoreCase(policy.getBusinessType())) {

                policy.setTransType("SP");
                policy.setPolRevStatus("SP");
            } else {
                policy.setTransType("NB");
                policy.setPolRevStatus("NB");
            }
            policy.setCurrentStatus("D");
            policy.setAuthStatus("D");
        }
        else{
            final PolicyTrans existing = policyRepo.findOne(policy.getPolicyId());
            policy.setPolTerm(existing.getPolTerm());
            policy.setProposalNo(existing.getProposalNo());
        }


        if (policy.getPrevPolicy() == null) {
            policy.setPreviousTrans(policy);
        } else {
            policy.setPreviousTrans(policyRepo.findOne(policy.getPrevPolicy()));
        }
        policy.setPolTerm(policy.getPolTerm());

        if (policy.getPolicyId() != null) {
            if (editPolicy == null)
                throw new BadRequestException("The Policy does not exist. Cannot Authorize");
            if ("A".equalsIgnoreCase(editPolicy.getAuthStatus())) {
                throw new BadRequestException("Cannot Save..Policy already authorized");
            }

            if (policy.getPolRevStatus() == null || StringUtils.isBlank(policy.getPolRevStatus())) {
                policy.setPolRevStatus(editPolicy.getPolRevStatus());
            }

            BindersDef binder = editPolicy.getBinder();
            BindersDef newBinder = binderRepo.findOne(policy.getBindCode());
            if (binder.getBinId() != newBinder.getBinId()) {
                Iterable<RiskTrans> binderRisks = riskRepo.findAll(QRiskTrans.riskTrans.policy.policyId.eq(policy.getPolicyId()));
                List<RiskTrans> newRisks = new ArrayList<>();
                for (RiskTrans risk : binderRisks) {
                    risk.setBinder(newBinder);
                    newRisks.add(risk);
                    Iterable<SectionTrans> sections = sectionRepo.findAll(QSectionTrans.sectionTrans.risk.riskId.eq(risk.getRiskId()));
                    sectionRepo.delete(sections);
                }
                riskRepo.save(newRisks);
            }


        }

        RiskTransBean riskBean = policy.getRiskBean();
        if (policy.getPolicyId() == null && !medicalProduct && !policy.isImportRisks()) {
            Date polWef = dateUtils.removeTime(policy.getWefDate());
            Date polWet = dateUtils.removeTime(policy.getWetDate());
            Date riskWef = dateUtils.removeTime(riskBean.getWefDate());
            Date riskWet = dateUtils.removeTime(riskBean.getWetDate());
            if (riskWef.before(polWef) || riskWef.after(polWet)
                    || riskWet.before(polWef) || riskWet.after(polWet)) {
                throw new BadRequestException("Risk Cover Dates outside Policy Cover Periods ");
            }
            RiskTrans risk = new RiskTrans();
            if (riskBean.getBindCode() == null) throw new BadRequestException("Binder is Mandatory");
            if (riskBean.getSclCode() == null) throw new BadRequestException("Sub Class is Mandatory");
            if (riskBean.getCoverCode() == null) throw new BadRequestException("Cover Type is Mandatory");
            if (riskBean.getInsuredCode() == null) throw new BadRequestException("Life assured is Mandatory");
            if (riskBean.getWorkingAge() == null) throw new BadRequestException("Life assured age is Mandatory");
            SubClassDef subClassDef = subclassRepo.findOne(riskBean.getSclCode());
            risk.setBinder(binderRepo.findOne(riskBean.getBindCode()));
            risk.setCovertype(coverRepo.findOne(riskBean.getCoverCode()));
            risk.setSubclass(subClassDef);
            risk.setInsured(clientRepo.findOne(riskBean.getInsuredCode()));
            risk.setWorkingAge(riskBean.getWorkingAge());
            risk.setBinderDetails(binderDetRepo.findOne(riskBean.getBinderDet()));
            risk.setCommRate(riskBean.getCommRate());
            Boolean autogen = Boolean.parseBoolean(riskBean.getAutogenCert());
            if (autogen) {
                risk.setAutogenCert("Y");
            } else risk.setAutogenCert("N");
            risk.setPolicy(policy);
            risk.setProrata(riskBean.getProrata());
            risk.setRiskDesc(riskBean.getRiskDesc());
            risk.setRiskShtDesc(riskBean.getRiskShtDesc());
            risk.setWefDate(riskBean.getWefDate());
            risk.setWetDate(riskBean.getWetDate());
            risk.setPremium(riskBean.getPremium());
            risk.setSumInsured(riskBean.getSumInsured());
            risk.setComputeType(riskBean.getComputeType());
            risk.setTransType("NB");

            List<SectionTrans> sectionTransactions = policy.getSections().stream().map(sectionbean -> {
                SectionsDef sectiondef = setupSectionRepo.findOne(sectionbean.getSection());
                SectionTrans section = new SectionTrans();
                section.setAmount((sectionbean.getAmount() == null) ? BigDecimal.ZERO : sectionbean.getAmount());
                section.setCompute(sectionbean.isCompute());
                section.setDivFactor(sectionbean.getDivFactor());
                section.setFreeLimit(sectionbean.getFreeLimit());
                if (sectionbean.getRatesApplicable() != null && "Y".equalsIgnoreCase(sectionbean.getRatesApplicable())) {
                    List<PremRatesDef> premRates = premRatesRepo.getSectPremiumRates(riskBean.getBinderDet(), sectionbean.getAmount(), sectiondef.getId());
                    if (premRates.size() == 1) {
                        section.setPremRates(premRates.get(0));
                        section.setRate(premRates.get(0).getRate());
                    }
                } else {
                    List<PremRatesDef> premRates = premRatesRepo.getSectPremiumRates(riskBean.getBinderDet(), sectiondef.getId());
                    if (premRates.size() == 1) {
                        section.setPremRates(premRates.get(0));
                        section.setRate(sectionbean.getRate());
                    }
                }
                section.setCompute(true);
                section.setSection(sectiondef);
                section.setRisk(risk);
                return section;
            }).collect(Collectors.toList());
            sectionRepo.save(sectionTransactions);
            RiskTrans savedRisk = riskRepo.save(risk);
            long riskIdentifier = Long.valueOf(String.valueOf(dateUtils.getUwYear(policy.getWefDate())) + String.valueOf(savedRisk.getRiskId()));
            PolicyActiveRisks activeRisk = new PolicyActiveRisks();
            activeRisk.setPolicy(policy);
            activeRisk.setRisk(risk);
            activeRisk.setRiskIdentifier(riskIdentifier);
            activeRisksRepo.save(activeRisk);
            savedRisk.setRiskIdentifier(riskIdentifier);
            riskRepo.save(savedRisk);
        }
        if (policy.getPolicyId() != null) {
            policy.setRevisionFormat(editPolicy.getRevisionFormat());
        }
        policy.setCommAllowed(true);
        if (policy.getSubAgentId() != null) {
            policy.setSubAgent(accountRepo.findOne(policy.getSubAgentId()));
        }
        policy.setAgent(accountRepo.findOne(policy.getAgentId()));
        policy.setBinder(binderRepo.findOne(policy.getBindCode()));
        policy.setBranch(branchRepo.findOne(policy.getBranchId()));
        policy.setClient(clientRepo.findOne(policy.getClientId()));
        policy.setPaymentMode(paymentModeRepo.findOne(policy.getPaymentId()));
        policy.setProduct(policyProduct);
        policy.setCreatedUser(user);
        policy.setTransCurrency(currencyRepo.findOne(policy.getCurrencyId()));
        policy.setPolCreateddt(new Date());
        if ("N".equalsIgnoreCase(policy.getBusinessType()))
            policy.setRenewable(policyProduct.isRenewable());
        else
            policy.setRenewable(false);
        policy.setUwYear(dateUtils.getUwYear(policy.getWefDate()));
        if (policyProduct.isRenewable() && "N".equalsIgnoreCase(policy.getBusinessType())) {
            policy.setRenewalDate(DateUtils.addDays(policy.getWetDate(), 1));
        } else
            policy.setRenewalDate(null);
        if (policy.getTransType().equalsIgnoreCase("NB") || policy.getTransType().equalsIgnoreCase("RN")) {
            policy.setCoverFrom(policy.getWefDate());
            policy.setCoverTo(policy.getWetDate());
        }
        System.out.println("frequency=" + policy.getFrequency() + ";term=" + policy.getPolTerm());
        if ("M".equalsIgnoreCase(policy.getFrequency()))
            policy.setTotalInstalments(12 * policy.getPolTerm());
        if ("A".equalsIgnoreCase(policy.getFrequency()))
            policy.setTotalInstalments(policy.getPolTerm());
        if ("S".equalsIgnoreCase(policy.getFrequency()))
            policy.setTotalInstalments(6 * policy.getPolTerm());
        if ("Q".equalsIgnoreCase(policy.getFrequency()))
            policy.setTotalInstalments(4 * policy.getPolTerm());
//        if (newTrans)
//            transRepo.save(transaction); edituwpolicy
        PolicyTrans savedTrans = policyRepo.save(policy);

        if (newTrans) {
            workflowService.startNewWorkFlow(DocType.GEN_UW_DOCUMENT, String.valueOf(savedTrans.getPolicyId()), policy, (medicalProduct) ? "Y" : "N", null, null, null);
        }
        Iterable<RiskTrans> risks = riskRepo.findAll(QRiskTrans.riskTrans.policy.policyId.eq(savedTrans.getPolicyId()));
        if (!medicalProduct && !lifeProduct) {
            for (RiskTrans risk : risks) {
                if(motorProduct){
                    final List<BigDecimal> codes = subclassCertTypesRepo.getCertificateCode(risk.getSubclass().getSubId());
                    if(codes.size()==1) {
                        final RiskCertForm riskCertForm = new RiskCertForm();
                        riskCertForm.setSubclasscertId( ((BigDecimal)codes.get(0)).longValue());
                        riskCertForm.setWefDate(risk.getWefDate());
                        riskCertForm.setWetDate(risk.getWetDate());
                        riskCertForm.setRiskId(risk.getRiskId());
                        try {
                            certService.createRiskCert(riskCertForm);
                        } catch (BadRequestException ex) {
                            System.out.println("Error creating cert....");
                        }
                    }
                }
                List<RiskDocs> riskDocs = Streamable.streamOf(reqdDocs)
                        .filter(reqdDoc -> riskDocsRepo.count(QRiskDocs.riskDocs.risk.riskIdentifier.eq(risk.getRiskIdentifier()).and(QRiskDocs.riskDocs.reqdDocs.sclReqrdId.eq(reqdDoc.getRequiredDocs().getSclReqrdId()))) == 0)
                        .map(reqdDoc -> {
                            RiskDocs riskDoc = new RiskDocs();
                            riskDoc.setReqdDocs(reqdDoc.getRequiredDocs());
                            riskDoc.setRisk(risk);
                            return riskDoc;
                        }).collect(Collectors.toList());

                riskDocsRepo.save(riskDocs);
            }
        }
        return savedTrans;
    }

    @PreAuthorize("hasAnyAuthority('SAVE_POLICY')")
    @Override
    @Modifying
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = {BadRequestException.class})
    public PolicyTrans createPolicy(PolicyTrans policy) throws BadRequestException {
        if (policy.getBinder() != null && "S".equalsIgnoreCase(policy.getBinder().getBinType())) {
            if (binderRepo.count(QBindersDef.bindersDef.fundBinder.equalsIgnoreCase("Y")) != 1) {
                throw new BadRequestException("A Fund Binder has not been set up ..Check your configuration");
            }
            BindersDef fundBinder = binderRepo.findOne(QBindersDef.bindersDef.fundBinder.equalsIgnoreCase("Y"));
            policy.setBindCode(fundBinder.getBinId());
            policy.setProdId(fundBinder.getProduct().getProCode());
            policy.setAgentId(fundBinder.getAccount().getAcctId());
        }
        boolean multiproduct = false;
        if (policy.getBindCodes() != null && policy.getBindCodes().length > 0) {
            multiproduct = true;
        }

        if (policy.getPolicyId() != null)
            multiproduct = policyBindersRepo.count(QPolicyBinders.policyBinders.policyTrans.policyId.eq(policy.getPolicyId())) > 0;
        if (policy.getClientId() == null) throw new BadRequestException("Client is Mandatory");
        if (policy.getBindCode() == null && !multiproduct) throw new BadRequestException("Binder is Mandatory ");
        if (policy.getAgentId() == null && !multiproduct)
            throw new BadRequestException("Intermediary is Mandatory");
        if (policy.getProdId() == null) throw new BadRequestException("Product is Mandatory");
//        if (policy.getPaymentId() == null) throw new BadRequestException("Payment Mode is Mandatory");

        if (policy.getBranchId() == null) throw new BadRequestException("Branch is Mandatory");
        if (policy.getCurrencyId() == null) throw new BadRequestException("Currency is Mandatory");
        if (policy.getWefDate() == null) throw new BadRequestException("Policy Wef Date From is Mandatory");
        if (policy.getWetDate() == null) throw new BadRequestException("Policy Wet Date From is Mandatory");

        if(policy.getCoinsuranceBusiness()!=null && policy.getCoinsuranceBusiness().equalsIgnoreCase("on")){
            policy.setCoinsuranceBusiness("Y");
        }
        else{
            policy.setCoinsuranceBusiness("N");
        }

        List<PolicyBinders> policyBindersList = new ArrayList<>();

        if (policy.getWefDate().after(policy.getWetDate()))
            throw new BadRequestException("Wef Date cannot be greater than Wet Date");
        Date polWetDate = dateUtils.getWetDate(policy.getWefDate());
        User user = userUtils.getCurrentUser();
        if (policy.getBusinessType() == null) throw new BadRequestException("Select Business Type");
        if ("N".equalsIgnoreCase(policy.getBusinessType())) {
            if (policy.getTransType() == null || "NB".equalsIgnoreCase(policy.getTransType())) {
                if (policy.getWetDate().after(polWetDate) || policy.getWetDate().before(polWetDate))
                    throw new BadRequestException("The Policy Cover Period Cannot be more than a year");
            } else if ("EN".equalsIgnoreCase(policy.getTransType()) || "CN".equalsIgnoreCase(policy.getTransType())) {
                PolicyTrans currentTrans = policyRepo.findOne(policy.getPolicyId());
                PolicyTrans prevTrans = currentTrans.getPreviousTrans();
                Date currentWet = dateUtils.removeTime(policy.getWetDate());
                Date prevWet = dateUtils.removeTime(prevTrans.getWetDate());
                if (currentWet.after(prevWet) || currentWet.before(prevWet))
                    throw new BadRequestException("Policy WET Date Cannot change for endorsements...");
            }

        }

        if ("S".equalsIgnoreCase(policy.getBusinessType()))
            if (policy.getWetDate().after(polWetDate))
                throw new BadRequestException("The Short Period Policy Cover Period Cannot be more than a year");

        boolean newTrans = false;
        Iterable<BinderReqrdDocs> reqdDocs = new ArrayList<>();
        if (policy.getRiskBean() != null)
            if (policy.getRiskBean().getSclCode() != null) {
                String transType = "";
                if (policy.getTransType() == null || StringUtils.isBlank(policy.getTransType())) {
                    transType = "NB";
                } else
                    transType = policy.getTransType();
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
                if ("NB".equalsIgnoreCase(transType)) {
                    reqdDocs = reqrdDocsRepo.findAll(QBinderReqrdDocs.binderReqrdDocs.binderDetail.detId.eq(policy.getRiskBean().getBinderDet())
                            .and(QBinderReqrdDocs.binderReqrdDocs.mandatory.eq(true))
                            .and(QBinderReqrdDocs.binderReqrdDocs.requiredDocs.requiredDoc.appliesNewBusiness.eq(true)));
                } else if ("EN".equalsIgnoreCase(transType)) {
                    reqdDocs = reqrdDocsRepo.findAll(QBinderReqrdDocs.binderReqrdDocs.binderDetail.detId.eq(policy.getRiskBean().getBinderDet())
                            .and(QBinderReqrdDocs.binderReqrdDocs.mandatory.eq(true))
                            .and(QBinderReqrdDocs.binderReqrdDocs.requiredDocs.requiredDoc.appliesEndorsement.eq(true)));
                } else if ("RN".equalsIgnoreCase(transType)) {
                    reqdDocs = reqrdDocsRepo.findAll(QBinderReqrdDocs.binderReqrdDocs.binderDetail.detId.eq(policy.getRiskBean().getBinderDet())
                            .and(QBinderReqrdDocs.binderReqrdDocs.mandatory.eq(true))
                            .and(QBinderReqrdDocs.binderReqrdDocs.requiredDocs.requiredDoc.appliesRenewal.eq(true)));
                }

            }

        SystemTrans transaction = null;
        if (policy.getPolicyId() == null) {

            newTrans = true;
            String policyNumberFormat = paramService.getParameterString("POLICY_NO_FORMAT");
            String endorsementFormat = paramService.getParameterString("ENDORSE_NO_FORMAT");
            Predicate seqPredicate = QSystemSequence.systemSequence.transType.eq("P");
            if (sequenceRepo.count(seqPredicate) == 0)
                throw new BadRequestException("Sequence for New Business Transactions has not been defined");
            SystemSequence sequence = sequenceRepo.findOne(seqPredicate);
            Long seqNumber = sequence.getNextNumber();
            final String policyNumber = templateMerger.generateFormat(policyNumberFormat, policy.getBranchId(), policy.getProdId(), policy.getWefDate(), sequence.getSeqPrefix() + String.format("%05d", seqNumber), null);
            policy.setPolNo(policyNumber);

            sequence.setLastNumber(seqNumber);
            sequence.setNextNumber(seqNumber + 1);
            sequenceRepo.save(sequence);
            Predicate endorsePredicate = QSystemSequence.systemSequence.transType.eq("E");
            if (sequenceRepo.count(endorsePredicate) == 0)
                throw new BadRequestException("Sequence for Endorsement Transactions has not been defined");
            SystemSequence endorseSequence = sequenceRepo.findOne(endorsePredicate);
            Long endosseqNumber = endorseSequence.getNextNumber();
            final String revNumber = endorseSequence.getSeqPrefix() + String.format("%05d", endosseqNumber);
            final String endorseNumber = templateMerger.generateFormat(endorsementFormat, policy.getBranchId(), policy.getProdId(), policy.getWefDate(), revNumber, null);
            policy.setPolRevNo(endorseNumber + "/1");
            policy.setRevisionFormat(endorseNumber);
            endorseSequence.setLastNumber(endosseqNumber);
            endorseSequence.setNextNumber(endosseqNumber + 1);
            sequenceRepo.save(endorseSequence);
            transaction = new SystemTrans();
            transaction.setDoneDate(new Date());
            transaction.setDoneBy(userUtils.getCurrentUser());
            transaction.setPolicy(policy);
            transaction.setTransLevel("U");
            transaction.setTransCode("NBD"); //A way to setup and look up for transaction transcode
            transaction.setTransAuthorised("N");

            if ("S".equalsIgnoreCase(policy.getBusinessType())) {
                policy.setTransType("SP");
                policy.setPolRevStatus("SP");
            } else {
                policy.setTransType("NB");
                policy.setPolRevStatus("NB");
            }
        }

        policy.setCurrentStatus("D");
        policy.setAuthStatus("D");
//
        if (policy.getPrevPolicy() == null) {
            policy.setPreviousTrans(policy);
        } else {
            policy.setPreviousTrans(policyRepo.findOne(policy.getPrevPolicy()));
        }

        if (policy.getPolicyId() != null) {
            PolicyTrans editPolicy = policyRepo.findOne(policy.getPolicyId());
            if (editPolicy == null)
                throw new BadRequestException("The Policy does not exist. Cannot Authorize");
            if ("A".equalsIgnoreCase(editPolicy.getAuthStatus())) {
                throw new BadRequestException("Cannot Save..Policy already authorized");
            }


            if (policy.getPolRevStatus() == null || StringUtils.isBlank(policy.getPolRevStatus())) {
                policy.setPolRevStatus(editPolicy.getPolRevStatus());
            }
            if (policy.getPolRevStatus() != null && "EN".equalsIgnoreCase(policy.getPolRevStatus())) {
                policy.setInstallmentNo(editPolicy.getInstallmentNo());
                policy.setTotalInstalments(editPolicy.getTotalInstalments());
            }

            if (policy.getBindCode() != null && !multiproduct) {
                BindersDef binder = editPolicy.getBinder();
                BindersDef newBinder = binderRepo.findOne(policy.getBindCode());
                if (binder.getBinId() != newBinder.getBinId()) {
                    Iterable<RiskTrans> binderRisks = riskRepo.findAll(QRiskTrans.riskTrans.policy.policyId.eq(policy.getPolicyId()));
                    List<RiskTrans> newRisks = new ArrayList<>();
                    for (RiskTrans risk : binderRisks) {
                        risk.setBinder(newBinder);
                        newRisks.add(risk);
                        Iterable<SectionTrans> sections = sectionRepo.findAll(QSectionTrans.sectionTrans.risk.riskId.eq(risk.getRiskId()));
                        sectionRepo.delete(sections);
                    }
                    riskRepo.save(newRisks);
                }
            } else if (policy.getBindCodes() != null) {
                //to do
            }


        }
        boolean medicalProduct = false;
        boolean lifeProduct = false;
        boolean renewable = false;
        ProductsDef policyProduct = productRepo.findOne(policy.getProdId());
        boolean cashBasis = policy.getInterfaceType() != null && "C".equalsIgnoreCase(policy.getInterfaceType());
        renewable = policyProduct.isRenewable();
        final boolean motorProduct = policyProduct.isMotorProduct();
        if (policyProduct.getProGroup().getPrgType() == null || !policyProduct.getProGroup().getPrgType().equalsIgnoreCase("MD")) {
            medicalProduct = false;
            lifeProduct = false;
        } else if (policyProduct.getProGroup().getPrgType().equalsIgnoreCase("MD")) {
            medicalProduct = true;
            lifeProduct = false;
            if (policy.getMedicalCoverType() == null || policy.getMedicalCoverType().equalsIgnoreCase(""))
                throw new BadRequestException("Type is Mandatory");

        } else if (policyProduct.getProGroup().getPrgType().equalsIgnoreCase("L")) {
            medicalProduct = false;
            lifeProduct = true;
        }
        policy.setProduct(policyProduct);

        if (medicalProduct) {
            if (policy.getMedicalCoverType() == null || StringUtils.isBlank(policy.getMedicalCoverType())) {
                throw new BadRequestException("Select Medical Cover Type....");
            }
            Predicate cardPred = QBinderMedicalCards.binderMedicalCards.binder.binId.eq(policy.getBindCode());
            Long cardCount = binderMedicalCardsRepo.count(cardPred);
            if (cardCount != 0 && policy.getCardId() == null) {
                throw new BadRequestException("Card type is Mandatory");
            } else {
                if (cardCount == 0) policy.setBinCardType(null);
            }
            if (!(policy.getCardId() == null)) {
                policy.setBinCardType(binderMedicalCardsRepo.findOne(policy.getCardId()));
            }
        }

        RiskTransBean riskBean = policy.getRiskBean();
        if (policy.getPolicyId() == null && !medicalProduct && !policy.isImportRisks() && !multiproduct) {
            Date polWef = dateUtils.removeTime(policy.getWefDate());
            Date polWet = dateUtils.removeTime(policy.getWetDate());
            Date riskWef = dateUtils.removeTime(riskBean.getWefDate());
            Date riskWet = dateUtils.removeTime(riskBean.getWetDate());
            if (riskWef.before(polWef) || riskWef.after(polWet)
                    || riskWet.before(polWef) || riskWet.after(polWet)) {
                throw new BadRequestException("Risk Cover Dates outside Policy Cover Periods ");
            }
            RiskTrans risk = new RiskTrans();
            if (riskBean.getBindCode() == null) throw new BadRequestException("Binder is Mandatory");
            if (riskBean.getSclCode() == null) throw new BadRequestException("Sub Class is Mandatory");
            if (riskBean.getCoverCode() == null) throw new BadRequestException("Cover Type is Mandatory");
            if (riskBean.getInsuredCode() == null) throw new BadRequestException("Insured is Mandatory");
            if (riskRepo.count(QRiskTrans.riskTrans.riskShtDesc.eq(riskBean.getRiskShtDesc())
                    .and(QRiskTrans.riskTrans.subclass.riskUnique.isTrue())
                    .and(QRiskTrans.riskTrans.policy.currentStatus.eq("A"))
                    .and(QRiskTrans.riskTrans.wefDate.between(riskBean.getWefDate(), riskBean.getWetDate()).or(QRiskTrans.riskTrans.wetDate.between(riskBean.getWefDate(), riskBean.getWetDate())))
                    .and(QRiskTrans.riskTrans.policy.transType.in("NB", "RN"))) > 0) {
                throw new BadRequestException("Risk Already Exists in the system...." +
                        riskRepo.count(QRiskTrans.riskTrans.riskShtDesc.eq(riskBean.getRiskShtDesc())
                                .and(QRiskTrans.riskTrans.subclass.riskUnique.isTrue())
                                .and(QRiskTrans.riskTrans.policy.currentStatus.eq("A"))
                                .and(QRiskTrans.riskTrans.wefDate.between(riskBean.getWefDate(), riskBean.getWetDate())).or(QRiskTrans.riskTrans.wetDate.between(riskBean.getWefDate(), riskBean.getWetDate()))
                                .and(QRiskTrans.riskTrans.policy.transType.in("NB", "RN")))
                );
            }
            SubClassDef subClassDef = subclassRepo.findOne(riskBean.getSclCode());
            risk.setBinder(binderRepo.findOne(riskBean.getBindCode()));
            risk.setCovertype(coverRepo.findOne(riskBean.getCoverCode()));
            risk.setSubclass(subClassDef);
            risk.setInsured(clientRepo.findOne(riskBean.getInsuredCode()));
            risk.setBinderDetails(binderDetRepo.findOne(riskBean.getBinderDet()));
            risk.setCommRate(riskBean.getCommRate());
            Boolean autogen = Boolean.parseBoolean(riskBean.getAutogenCert());
            if (autogen) {
                risk.setAutogenCert("Y");
            } else risk.setAutogenCert("N");
            risk.setPolicy(policy);
            risk.setProrata(riskBean.getProrata());
            risk.setRiskDesc(riskBean.getRiskDesc());
            risk.setRiskShtDesc(riskBean.getRiskShtDesc());
            risk.setWefDate(riskBean.getWefDate());
            risk.setWetDate(riskBean.getWetDate());
            risk.setButchargePrem(riskBean.getButchargePrem());
            risk.setInstallAmount(riskBean.getInstallAmount());
            risk.setInstallmentNo(riskBean.getInstallmentNo());
            risk.setInstallmentPerc(riskBean.getInstallmentPerc());
            risk.setTransType("NB");
            if (binderDetRepo.findOne(riskBean.getBinderDet()).getSingleSectionCover() != null && binderDetRepo.findOne(riskBean.getBinderDet()).getSingleSectionCover().equalsIgnoreCase("Y")) {
                if (policy.getSections().size() > 1) {
                    throw new BadRequestException("Only one Option is allowed for this contract");
                }
            }
            List<SectionTrans> sectionTransactions = policy.getSections().stream().map(sectionbean -> {
                SectionsDef sectiondef = setupSectionRepo.findOne(sectionbean.getSection());
                SectionTrans section = new SectionTrans();
                section.setAmount((sectionbean.getAmount() == null) ? BigDecimal.ZERO : sectionbean.getAmount());
                section.setCompute(sectionbean.isCompute());
                section.setDivFactor(sectionbean.getDivFactor());
                section.setFreeLimit(sectionbean.getFreeLimit());
                if (sectionbean.getRatesApplicable() != null && "Y".equalsIgnoreCase(sectionbean.getRatesApplicable())) {
                    List<PremRatesDef> premRates = premRatesRepo.getSectPremiumRates(riskBean.getBinderDet(), sectionbean.getAmount(), sectiondef.getId());
                    if (premRates.size() == 1) {
                        section.setPremRates(premRates.get(0));
                        section.setRate(premRates.get(0).getRate());
                    }
                } else {
                    List<PremRatesDef> premRates = premRatesRepo.getSectPremiumRates(riskBean.getBinderDet(), sectiondef.getId());
                    if (premRates.size() == 1) {
                        section.setPremRates(premRates.get(0));
                        section.setRate(sectionbean.getRate());
                    }
                }
                section.setCompute(true);
                section.setSection(sectiondef);
                section.setRisk(risk);
                return section;
            }).collect(Collectors.toList());
            sectionRepo.save(sectionTransactions);
            RiskTrans savedRisk = riskRepo.save(risk);
            long riskIdentifier = Long.valueOf(String.valueOf(dateUtils.getUwYear(policy.getWefDate())) + String.valueOf(savedRisk.getRiskId()));
            PolicyActiveRisks activeRisk = new PolicyActiveRisks();
            activeRisk.setPolicy(policy);
            activeRisk.setRisk(risk);
            activeRisk.setRiskIdentifier(riskIdentifier);
            activeRisksRepo.save(activeRisk);
            savedRisk.setRiskIdentifier(riskIdentifier);
            riskRepo.save(savedRisk);



        }
        if (policy.getPolicyId() != null) {
            policy.setRevisionFormat(policy.getRevisionFormat());
        }
        policy.setCommAllowed(true);
        if (policy.getSubAgentId() != null) {
            policy.setSubAgent(accountRepo.findOne(policy.getSubAgentId()));
        }
        if (!multiproduct)
            policy.setAgent(accountRepo.findOne(policy.getAgentId()));
        if (policy.getBindCode() != null) {
            policy.setBinder(binderRepo.findOne(policy.getBindCode()));

        } else if (policy.getBindCodes() != null) {
            for (Long bindCode : policy.getBindCodes()) {
                PolicyBinders policyBinders = new PolicyBinders();
                policyBinders.setBinder(binderRepo.findOne(bindCode));
                policyBinders.setPolicyTrans(policy);
                policyBindersList.add(policyBinders);
            }
            renewable = true;
        }
        policy.setBranch(branchRepo.findOne(policy.getBranchId()));
        policy.setClient(clientRepo.findOne(policy.getClientId()));
     //   policy.setPaymentMode(paymentModeRepo.findOne(policy.getPaymentId()));


        policy.setCreatedUser(user);
        policy.setTransCurrency(currencyRepo.findOne(policy.getCurrencyId()));
        policy.setPolCreateddt(new Date());
        if ("N".equalsIgnoreCase(policy.getBusinessType()))
            policy.setRenewable(renewable);
        else
            policy.setRenewable(false);
        policy.setUwYear(dateUtils.getUwYear(policy.getWefDate()));
        if (renewable && "N".equalsIgnoreCase(policy.getBusinessType())) {
            policy.setRenewalDate(DateUtils.addDays(policy.getWetDate(), 1));
        } else
            policy.setRenewalDate(null);
        if (policy.getTransType().equalsIgnoreCase("NB") || policy.getTransType().equalsIgnoreCase("RN") || policy.getTransType().equalsIgnoreCase("SP")) {
            policy.setCoverFrom(policy.getWefDate());
            policy.setCoverTo(policy.getWetDate());
        } else {
            PolicyTrans currentTrans = policyRepo.findOne(policy.getPolicyId());
            policy.setCoverFrom(currentTrans.getCoverFrom());
            policy.setCoverTo(currentTrans.getCoverTo());
        }

        if (newTrans)
            transRepo.save(transaction);
        PolicyTrans savedTrans = policyRepo.save(policy);
        this.populateClauses(savedTrans);
        this.populateTaxes(savedTrans);
        if (policyBindersList.size() > 0) {
            policyBindersRepo.save(policyBindersList);
        }

        if (newTrans) {
            workflowService.startNewWorkFlow(DocType.GEN_UW_DOCUMENT, String.valueOf(savedTrans.getPolicyId()), policy, (medicalProduct) ? "Y" : "N", null, null, null);
        }
        Iterable<RiskTrans> risks = riskRepo.findAll(QRiskTrans.riskTrans.policy.policyId.eq(savedTrans.getPolicyId()));
        if (!medicalProduct && !lifeProduct) {
            for (RiskTrans risk : risks) {
                List<RiskDocs> riskDocs = Streamable.streamOf(reqdDocs)
                        .filter(reqdDoc -> riskDocsRepo.count(QRiskDocs.riskDocs.risk.riskIdentifier.eq(risk.getRiskIdentifier()).and(QRiskDocs.riskDocs.reqdDocs.sclReqrdId.eq(reqdDoc.getRequiredDocs().getSclReqrdId()))) == 0)
                        .map(reqdDoc -> {
                            RiskDocs riskDoc = new RiskDocs();
                            riskDoc.setReqdDocs(reqdDoc.getRequiredDocs());
                            riskDoc.setRisk(risk);
                            return riskDoc;
                        }).collect(Collectors.toList());

                riskDocsRepo.save(riskDocs);
                if(motorProduct){
                    final List<BigDecimal> codes = subclassCertTypesRepo.getCertificateCode(risk.getSubclass().getSubId());
                    if(codes.size()==1) {
                        final RiskCertForm riskCertForm = new RiskCertForm();
                        riskCertForm.setSubclasscertId( ((BigDecimal)codes.get(0)).longValue());
                        riskCertForm.setWefDate(risk.getWefDate());
                        riskCertForm.setWetDate(risk.getWetDate());
                        riskCertForm.setRiskId(risk.getRiskId());
                        try {
                            certService.createRiskCert(riskCertForm);
                        } catch (BadRequestException ex) {
                            System.out.println("Error creating cert....");
                        }
                    }
                }
            }
        }
        return savedTrans;

    }

    @Override
    @Transactional(readOnly = true)
    public DataTablesResult<RiskTrans> findRiskTransactions(DataTablesRequest request, Long polCode, Long bindCode)
            throws IllegalAccessException {
        BooleanExpression pred = QRiskTrans.riskTrans.policy.policyId.eq(polCode);
//        if (bindCode != null && bindCode != -2000l)
//            pred = QRiskTrans.riskTrans.policy.policyId.eq(polCode).and(QRiskTrans.riskTrans.policyBinders.policyBindId.eq(bindCode));

        Page<RiskTrans> page = riskRepo.findAll(pred.and(request.searchPredicate(QRiskTrans.riskTrans)), request);
        return new DataTablesResult<>(request, page);
    }

    @Override
    @Transactional(readOnly = true)
    public DataTablesResult<PolicyBinders> findPolicyBinders(DataTablesRequest request, Long polCode) throws IllegalAccessException {
        BooleanExpression pred = QPolicyBinders.policyBinders.policyTrans.policyId.eq(polCode);
        Page<PolicyBinders> page = policyBindersRepo.findAll(pred.and(request.searchPredicate(QPolicyBinders.policyBinders)), request);
        return new DataTablesResult<>(request, page);
    }

    @Override
    @Transactional(readOnly = true)
    public DataTablesResult<SectionTrans> findRiskSections(DataTablesRequest request, Long riskId) {

        List<Object[]> pages = sectionRepo.findRiskSectionTrans(riskId);
        List<SectionTrans> sectionTrans = new ArrayList<>();
        for (Object[] sectran : pages) {
            SectionTrans sec = new SectionTrans();
            if (sectran[0] instanceof BigInteger) {
                sec.setSectId(((BigInteger) sectran[0]).longValue());
            } else if (sectran[0] instanceof BigDecimal) {
                sec.setSectId(((BigDecimal) sectran[0]).longValue());
            }

            sec.setAmount((BigDecimal) sectran[1]);
            sec.setCalcprem((BigDecimal) sectran[2]);
            sec.setDivFactor((BigDecimal) sectran[3]);
            sec.setFreeLimit((BigDecimal) sectran[4]);
            sec.setMultiRate((BigDecimal) sectran[5]);
            sec.setPrem((BigDecimal) sectran[6]);
            sec.setRate((BigDecimal) sectran[7]);
            if (sectran[8] instanceof BigInteger)
                sec.setSection(setupSectionRepo.findOne(((BigInteger) sectran[8]).longValue()));
            else if (sectran[8] instanceof BigDecimal)
                sec.setSection(setupSectionRepo.findOne(((BigDecimal) sectran[8]).longValue()));
            if (sectran[9] instanceof BigInteger)
                sec.setPremRates(premRatesRepo.findOne(((BigInteger) sectran[9]).longValue()));
            else if (sectran[9] instanceof BigDecimal)
                sec.setPremRates(premRatesRepo.findOne(((BigDecimal) sectran[9]).longValue()));
            if (sectran[10] instanceof BigInteger)
                sec.setRiskId(((BigInteger) sectran[10]).longValue());
            else if (sectran[10] instanceof BigDecimal)
                sec.setRiskId(((BigDecimal) sectran[10]).longValue());
            sec.setDesc((String) sectran[11]);
            sectionTrans.add(sec);

        }
        Page<SectionTrans> page = new PageImpl<SectionTrans>(sectionTrans);
        return new DataTablesResult<>(request, page);
    }


    @Override
    public DataTablesResult<RiskInterestedParties> findRiskInterestedParties(DataTablesRequest request, Long riskId) throws IllegalAccessException {
        BooleanExpression pred = QRiskInterestedParties.riskInterestedParties.risk.riskId.eq(riskId);
        Page<RiskInterestedParties> page = riskIntPartiesRepo.findAll(pred.and(request.searchPredicate(QRiskInterestedParties.riskInterestedParties)), request);
        return new DataTablesResult<>(request, page);
    }

    @Override
    @Transactional(readOnly = true)
    public PolicyTrans getPolicyDetails(Long polCode) throws BadRequestException {
        BooleanExpression pred = QPolicyTrans.policyTrans.policyId.eq(polCode);
        PolicyTrans policy = policyRepo.findOne(pred);
        policy.setPrevPolicy(policy.getPreviousTrans().getPolicyId());
        Iterable<PolicyBinders> policyBinders = policyBindersRepo.findAll(QPolicyBinders.policyBinders.policyTrans.policyId.eq(polCode));
        String binderName = "";
        for (PolicyBinders binders : policyBinders) {
            binderName = binderName + binders.getBinder().getBinName() + ",";
        }
        if (binderName.length() > 1) {

        }

        if (policyQuestionnaireRepo.count(QPolicyQuestionnaire.policyQuestionnaire.policy.policyId.eq(polCode)) > 0) {
            policy.setQuizTaken("Y");
        } else {
            policy.setQuizTaken("N");
        }
        return policy;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SectionBean> findPremSections(String paramString, Pageable paramPageable, Long detId) {
        Predicate pred = null;
        if (paramString == null || StringUtils.isBlank(paramString)) {
            pred = QPremRatesDef.premRatesDef.binderDet.detId.eq(detId)
                    .and(QPremRatesDef.premRatesDef.isNotNull());
        } else {
            pred = QPremRatesDef.premRatesDef.binderDet.detId.eq(detId)
                    .and(QPremRatesDef.premRatesDef.section.desc.containsIgnoreCase(paramString)
                            .or(QPremRatesDef.premRatesDef.section.shtDesc.containsIgnoreCase(paramString)));
        }
        List<PremRatesDef> premrates = premRatesRepo.findAll(pred, paramPageable).getContent();
        List<SectionBean> sections = premrates.stream().map(det -> {
            SectionBean section = new SectionBean();
            section.setDesc(det.getSection().getDesc());
            section.setDivFactor(det.getDivFactor());
            section.setFreeLimit(det.getFreeLimit());
            section.setId(det.getSection().getId());
            section.setPremId(det.getId());
            section.setRate(det.getRate());
            section.setRateType(det.getRateType());
            section.setSectType(det.getSection().getType().getCode());
            section.setShtDesc(det.getSection().getShtDesc());
            return section;
        }).collect(Collectors.toList());

        Page<SectionBean> retSections = new PageImpl<>(sections);
        return retSections;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = {BadRequestException.class})
    public void createRiskSection(SectionTrans section) throws BadRequestException {
        section.setRisk(riskRepo.findOne(section.getRiskId()));
        if (section.getRisk().getBinderDetails().getBinder().getProduct().getAgeApplicable() != null && "Y".equalsIgnoreCase(section.getRisk().getBinderDetails().getBinder().getProduct().getAgeApplicable())) {

        } else {
            if (!StringUtils.isBlank(section.getPremRates().getRatesApplicable()) && "Y".equalsIgnoreCase(section.getPremRates().getRatesApplicable())) {

                Iterable<PremRatesDef> premRatesDefs = premRatesRepo.findAll(QPremRatesDef.premRatesDef.rangeFrom.loe(section.getAmount()).and(QPremRatesDef.premRatesDef.rangeTo.goe(section.getAmount()))
                        .and(QPremRatesDef.premRatesDef.binderDet.detId.eq(section.getRisk().getBinderDetails().getDetId())));
                if (premRatesDefs.spliterator().getExactSizeIfKnown() > 1) {
                    throw new BadRequestException("More than one premium Rates setup for the binder...");
                }
                if (premRatesDefs.spliterator().getExactSizeIfKnown() == 0) {
                    throw new BadRequestException("No premium Rates setup for the binder...");
                }

                for (PremRatesDef premRatesDef : premRatesDefs) {
                    section.setPremRates(premRatesDef);
                    section.setRate(premRatesDef.getRate());
                }

            }
        }

        sectionRepo.save(section);
        if (section.getRisk().getBinderDetails().getLimitsAllowed() != null && section.getRisk().getBinderDetails().getLimitsAllowed().equalsIgnoreCase("Y")) {
            if (sectionRepo.findAll(QSectionTrans.sectionTrans.risk.riskId.eq(section.getRisk().getRiskId())).spliterator().getExactSizeIfKnown() > 1) {
                throw new BadRequestException("Only one Option is allowed for this contract");
            }
        }
    }


    @PreAuthorize("hasAnyAuthority('SAVE_POLICY')")
    @Transactional(readOnly = false)
    public void deleteRiskSection(Long sectid) throws BadRequestException {
        SectionTrans sectionTrans = this.sectionRepo.findSectionTransInfo(sectid);
        BindersDef bindersDef = sectionTrans.getRisk().getPolicy().getBinder();
        boolean var10000;
        if (bindersDef.getBinType() != null && "B".equalsIgnoreCase(bindersDef.getBinType())) {
            var10000 = true;
        } else {
            var10000 = false;
        }

        if (sectionTrans.getPremRates() != null) {
            PremRatesDef premRatesDef = sectionTrans.getPremRates();
            if (premRatesDef.getMandatory() != null && "Y".equalsIgnoreCase(premRatesDef.getMandatory())) {
                throw new BadRequestException("Cannot delete A Mandatory Premium Item...");
            }
        }

        this.sectionRepo.delete(sectid);
    }

    @Override
    @Transactional(readOnly = true)
    public DataTablesResult<PolicyTaxes> findPolicyTaxes(DataTablesRequest request, Long polCode)
            throws IllegalAccessException {
        BooleanExpression pred = QPolicyTaxes.policyTaxes.policy.policyId.eq(polCode);
        Page<PolicyTaxes> page = polTaxesRepo.findAll(pred.and(request.searchPredicate(QPolicyTaxes.policyTaxes)), request);
        return new DataTablesResult<>(request, page);
    }

    @Override
    @Transactional(readOnly = true)
    public DataTablesResult<PolicyClauses> findPolicyClauses(DataTablesRequest request, Long polCode)
            throws IllegalAccessException {
        BooleanExpression pred = QPolicyClauses.policyClauses.policy.policyId.eq(polCode);
        Page<PolicyClauses> page = polClausesRepo.findAll(pred.and(request.searchPredicate(QPolicyClauses.policyClauses)), request);
        return new DataTablesResult<>(request, page);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = {BadRequestException.class})
    public void populateTaxes(PolicyTrans policy) throws BadRequestException {
        Set<PolicyTaxes> policyTaxes = new HashSet<>();
        if (policy == null) throw new BadRequestException("Policy Cannot be null");
        Iterable<PolicyTaxes> polTaxes = polTaxesRepo.findAll(QPolicyTaxes.policyTaxes.policy.policyId.eq(policy.getPolicyId()));
        polTaxes.forEach(policyTaxes::add);
        Iterable<RiskTrans> risks = riskRepo.findAll(QRiskTrans.riskTrans.policy.policyId.eq(policy.getPolicyId()));
        for (RiskTrans risk : risks) {
            Iterable<TaxRates> taxRates = taxRatesRepo.findAll((QTaxRates.taxRates.active.eq(true).and(QTaxRates.taxRates.mandatory.eq(Boolean.TRUE))).and(QTaxRates.taxRates.subclass.eq(risk.getSubclass()))
                    .and(QTaxRates.taxRates.productsDef.proCode.eq(risk.getPolicy().getProduct().getProCode())));
            for (TaxRates tax : taxRates) {
                if ((policy.getBinder() != null && "B".equalsIgnoreCase(policy.getBinder().getBinType())) || ("EN".equalsIgnoreCase(policy.getTransType())) || ("RN".equalsIgnoreCase(policy.getTransType())) || ("EX".equalsIgnoreCase(policy.getTransType()))) {
                    if (tax.getRevenueItems().getItem() == RevenueItems.SD)
                        continue;
                }
                PolicyTaxes policyTax = new PolicyTaxes();
                policyTax.setPolicy(policy);
                policyTax.setRateType(tax.getRateType());
                policyTax.setRevenueItems(tax.getRevenueItems());
                policyTax.setSubclass(tax.getSubclass());
                policyTax.setTaxLevel(tax.getTaxLevel());
                policyTax.setTaxRate(tax.getTaxRate());
                policyTax.setDivFactor(tax.getDivFactor());
                policyTax.setTaxAmount(premComputeService.calculateTax(policy.getPremium(), tax.getTaxRate(), tax.getDivFactor(), tax.getRateType()));
                policyTaxes.add(policyTax);
            }
        }

        polTaxesRepo.save(policyTaxes);

    }

    @Override
    @Modifying
    @Transactional(readOnly = false, rollbackFor = {BadRequestException.class})
    public void populateClauses(PolicyTrans policy) throws BadRequestException {
        Set<PolicyClauses> policyClauses = new HashSet<>();
        Iterable<PolicyClauses> polClauses = polClausesRepo.findAll(QPolicyClauses.policyClauses.policy.policyId.eq(policy.getPolicyId()));
        polClauses.forEach(policyClauses::add);
        Iterable<RiskTrans> risks = riskRepo.findAll(QRiskTrans.riskTrans.policy.policyId.eq(policy.getPolicyId()));
        for (RiskTrans risk : risks) {
            Iterable<BinderClauses> binderClauses = binderClauseRepo.findAll(QBinderClauses.binderClauses.binderDet.detId.eq(risk.getBinderDetails().getDetId()).and(QBinderClauses.binderClauses.mandatory.eq("Y")));
            for (BinderClauses clause : binderClauses) {
                PolicyClauses polClause = new PolicyClauses();
                polClause.setClauHeading((clause.getClauHeading() != null) ? clause.getClauHeading() : clause.getClause().getClause().getClauHeading());
                polClause.setClause(clause.getClause());
                polClause.setClauWording((clause.getClauWording() != null) ? clause.getClauWording() : clause.getClause().getClause().getClauWording());
                if (clause.getEditable() != null && "Y".equalsIgnoreCase(clause.getEditable())) {
                    polClause.setEditable(true);
                }
                polClause.setNewClause("Y");
                polClause.setPolicy(policy);
                policyClauses.add(polClause);
            }
            Iterable<SubclassClauses> subClauses = subclauseRepo.findAll(QSubclassClauses.subclassClauses.subclass.subId.eq(risk.getSubclass().getSubId()).and(QSubclassClauses.subclassClauses.mandatory.eq(true)));
            for (SubclassClauses clause : subClauses) {
                PolicyClauses polClause = new PolicyClauses();
                polClause.setClauHeading(clause.getClause().getClauHeading());
                polClause.setClause(clause);
                polClause.setClauWording(clause.getClause().getClauWording());
                polClause.setEditable(clause.getClause().isEditable());
                polClause.setNewClause("Y");
                polClause.setPolicy(policy);
                policyClauses.add(polClause);
            }
        }

        polClausesRepo.save(policyClauses);

    }


    @PreAuthorize("hasAnyAuthority('SAVE_POLICY')")
    @Override
    @Modifying
    @Transactional(readOnly = false, rollbackFor = {BadRequestException.class})
    public void createRisk(RiskTrans risk) throws BadRequestException {
        if (risk.getBindCode() == null) throw new BadRequestException("Binder is Mandatory");
        if (risk.getSclCode() == null) throw new BadRequestException("Sub Class is Mandatory");
        if (risk.getCoverCode() == null) throw new BadRequestException("Cover Type is Mandatory");
        if (risk.getInsuredCode() == null) throw new BadRequestException("Insured is Mandatory");
        boolean newRisk = risk.getRiskId() == null;
        if (newRisk) {
            if (riskRepo.count(QRiskTrans.riskTrans.riskShtDesc.eq(risk.getRiskShtDesc())
                    .and(QRiskTrans.riskTrans.subclass.riskUnique.isTrue())
                    .and(QRiskTrans.riskTrans.policy.currentStatus.eq("A"))
                    .and((QRiskTrans.riskTrans.wefDate.between(risk.getWefDate(), risk.getWetDate())).or(QRiskTrans.riskTrans.wetDate.between(risk.getWefDate(), risk.getWetDate())))
                    .and(QRiskTrans.riskTrans.policy.transType.in("NB", "RN"))) > 0) {
                throw new BadRequestException("Risk Already Exists in the system...." +
                        riskRepo.count(QRiskTrans.riskTrans.riskShtDesc.eq(risk.getRiskShtDesc())
                                .and(QRiskTrans.riskTrans.subclass.riskUnique.isTrue())
                                .and(QRiskTrans.riskTrans.policy.currentStatus.eq("A"))
                                .and((QRiskTrans.riskTrans.wefDate.between(risk.getWefDate(), risk.getWetDate())).or(QRiskTrans.riskTrans.wetDate.between(risk.getWefDate(), risk.getWetDate())))
                                .and(QRiskTrans.riskTrans.policy.transType.in("NB", "RN"))));
            }
        } else {
            if (riskRepo.count(QRiskTrans.riskTrans.riskShtDesc.eq(risk.getRiskShtDesc())
                    .and(QRiskTrans.riskTrans.subclass.riskUnique.isTrue())
                    .and(QRiskTrans.riskTrans.policy.currentStatus.eq("A"))
                    .and((QRiskTrans.riskTrans.wefDate.between(risk.getWefDate(), risk.getWetDate())).or(QRiskTrans.riskTrans.wetDate.between(risk.getWefDate(), risk.getWetDate())))) > 1) {
                throw new BadRequestException("Risk Already Exists in the system...." +
                        riskRepo.count(QRiskTrans.riskTrans.riskShtDesc.eq(risk.getRiskShtDesc())
                                .and(QRiskTrans.riskTrans.subclass.riskUnique.isTrue())
                                .and(QRiskTrans.riskTrans.policy.currentStatus.eq("A"))
                                .and((QRiskTrans.riskTrans.wefDate.between(risk.getWefDate(), risk.getWetDate())).or(QRiskTrans.riskTrans.wetDate.between(risk.getWefDate(), risk.getWetDate())))));
            }
        }
        if (risk.getWefDate().after(risk.getWetDate())) {
            throw new BadRequestException("Risk Wef Date Cannot be greater than Risk Wet");
        }
        Long riskId = null;
        if (!newRisk) {
            riskId = riskRepo.findOne(risk.getRiskId()).getRiskIdentifier();
        }
        PolicyTrans policy = risk.getPolicy();
        Date polWef = dateUtils.removeTime(policy.getWefDate());
        Date polWet = dateUtils.removeTime(policy.getWetDate());
        Date riskWef = dateUtils.removeTime(risk.getWefDate());
        Date riskWet = dateUtils.removeTime(risk.getWetDate());
        if (riskWef.before(polWef) || riskWef.after(polWet)
                || riskWet.before(polWef) || riskWet.after(polWet)) {
            throw new BadRequestException("Risk Cover Dates outside Policy Cover Periods ");
        }
        risk.setBinder(binderRepo.findOne(risk.getBindCode()));
        risk.setCovertype(coverRepo.findOne(risk.getCoverCode()));
        risk.setSubclass(subclassRepo.findOne(risk.getSclCode()));
        risk.setInsured(clientRepo.findOne(risk.getInsuredCode()));
        risk.setBinderDetails(binderDetRepo.findOne(risk.getBinderDet()));

        Integer installmentNo = risk.getBinderDetails().getInstallmentsNo();
        if (installmentNo == null) installmentNo = 1;
        policy.setTotalInstalments(installmentNo);
        String distribution = risk.getBinderDetails().getDistribution();
        String firstInstallment = "";
        if (distribution != null && distribution.contains(":")) {
            firstInstallment = distribution.split(":")[0];
        } else firstInstallment = distribution;
        risk.setInstallmentPerc(firstInstallment);
        if (firstInstallment != null) {
            BigDecimal installment = new BigDecimal(firstInstallment);
            if (installment.compareTo(BigDecimal.valueOf(100l)) < 0) {
                risk.setWetDate(DateUtils.addMonths(risk.getWefDate(), 1));
            }
        }
        risk.setInstallmentNo(policy.getInstallmentNo());

        if (policy.getTransType() == null)

            if (risk.getPolBindCode() != null) {
                risk.setPolicyBinders(policyBindersRepo.findOne(risk.getPolBindCode()));
            }
        risk.setRiskIdentifier(riskId);
        if (risk.getAutogenCert() == null)
            risk.setAutogenCert("N");
        else if ("on".equalsIgnoreCase(risk.getAutogenCert())) {
            risk.setAutogenCert("Y");
        } else risk.setAutogenCert("N");
        if (newRisk) {
            List<SectionTrans> sectionTransactions = risk.getSections().stream().map(sectionbean -> {
                SectionsDef sectiondef = setupSectionRepo.findOne(sectionbean.getSection());
                SectionTrans section = new SectionTrans();
                section.setAmount((sectionbean.getAmount() != null) ? sectionbean.getAmount() : BigDecimal.ZERO);
                section.setCompute(sectionbean.isCompute());
                section.setDivFactor(sectionbean.getDivFactor());
                section.setFreeLimit(sectionbean.getFreeLimit());
                if (sectionbean.getRatesApplicable() != null && "Y".equalsIgnoreCase(sectionbean.getRatesApplicable())) {
                    List<PremRatesDef> premRates = premRatesRepo.getSectPremiumRates(risk.getBinderDet(), sectionbean.getAmount(), sectiondef.getId());
                    if (premRates.size() == 1) {
                        section.setPremRates(premRates.get(0));
                        section.setRate(premRates.get(0).getRate());
                    }
                } else {
                    List<PremRatesDef> premRates = premRatesRepo.getSectPremiumRates(risk.getBinderDet(), sectiondef.getId());
                    if (premRates.size() == 1) {
                        section.setPremRates(premRates.get(0));
                        section.setRate(sectionbean.getRate());
                    }
                }
                section.setMultiRate(sectionbean.getMultiplierRate());
                section.setCompute(true);
                section.setRate(sectionbean.getRate());
                section.setSection(sectiondef);
                section.setRisk(risk);
                return section;
            }).filter(a -> a.getPremRates() != null).collect(Collectors.toList());
            sectionRepo.save(sectionTransactions);
        }

        RiskTrans savedRisk = riskRepo.save(risk);

        Iterable<BinderReqrdDocs> reqdDocs = reqrdDocsRepo.findAll(QBinderReqrdDocs.binderReqrdDocs.binderDetail.detId.eq(savedRisk.getBinderDetails().getDetId())
                .and(QBinderReqrdDocs.binderReqrdDocs.mandatory.eq(true))
                .and(QBinderReqrdDocs.binderReqrdDocs.requiredDocs.requiredDoc.appliesNewBusiness.eq(true)));


        long riskIdentifier = Long.valueOf(String.valueOf(dateUtils.getUwYear(policy.getWefDate())) + String.valueOf(savedRisk.getRiskId()));
        if (newRisk) {
            PolicyActiveRisks activeRisk = new PolicyActiveRisks();
            activeRisk.setPolicy(policy);
            activeRisk.setRisk(risk);
            activeRisk.setRiskIdentifier(riskIdentifier);
            activeRisk.setStatus("NB");
            activeRisksRepo.save(activeRisk);
            savedRisk.setRiskIdentifier(riskIdentifier);
            riskRepo.save(savedRisk);
        }
        List<RiskDocs> riskDocs = new ArrayList<>();
        for (BinderReqrdDocs reqdDoc : reqdDocs) {
            if (riskDocsRepo.count(QRiskDocs.riskDocs.risk.riskIdentifier.eq(savedRisk.getRiskIdentifier()).and(QRiskDocs.riskDocs.reqdDocs.sclReqrdId.eq(reqdDoc.getRequiredDocs().getSclReqrdId()))) == 0) {
                RiskDocs riskDoc = new RiskDocs();
                riskDoc.setReqdDocs(reqdDoc.getRequiredDocs());
                riskDoc.setRisk(savedRisk);
                riskDocs.add(riskDoc);
            }
        }
        Iterable<SubClassReqdDocs> subClassReqDocs = subclassReqDocRepo.findAll(QSubClassReqdDocs.subClassReqdDocs.subclass.subId.eq(savedRisk.getBinderDetails().getSubCoverTypes().getSubclass().getSubId())
                .and(QSubClassReqdDocs.subClassReqdDocs.mandatory.eq(true))
                .and(QSubClassReqdDocs.subClassReqdDocs.requiredDoc.appliesEndorsement.eq(true)));
        for (SubClassReqdDocs reqdDoc : subClassReqDocs) {
            if (riskDocsRepo.count(QRiskDocs.riskDocs.risk.riskIdentifier.eq(savedRisk.getRiskIdentifier()).and(QRiskDocs.riskDocs.reqdDocs.sclReqrdId.eq(reqdDoc.getSclReqrdId()))) == 0
            ) {
                if (riskDocs.stream().filter(a -> a.getReqdDocs().getSclReqrdId() == reqdDoc.getSclReqrdId()).count() == 0) {
                    RiskDocs riskDoc = new RiskDocs();
                    riskDoc.setReqdDocs(reqdDoc);
                    riskDoc.setRisk(savedRisk);
                    riskDocs.add(riskDoc);
                }
            }
        }
        riskDocsRepo.save(riskDocs);
//        if(policy.getProduct().isMotorProduct()){
//            final List<BigDecimal> codes = subclassCertTypesRepo.getCertificateCode(risk.getSubclass().getSubId());
//            if(codes.size()==1) {
//                final RiskCertForm riskCertForm = new RiskCertForm();
//                riskCertForm.setSubclasscertId( ((BigDecimal)codes.get(0)).longValue());
//                riskCertForm.setWefDate(risk.getWefDate());
//                riskCertForm.setWetDate(risk.getWetDate());
//                riskCertForm.setRiskId(risk.getRiskId());
//                try {
//                    certService.createRiskCert(riskCertForm);
//                } catch (BadRequestException ex) {
//                    System.out.println("Error creating cert....");
//                }
//            }
//        }

    }


    @PreAuthorize("hasAnyAuthority('SAVE_POLICY')")
    @Override
    @Modifying
    @Transactional(readOnly = false, rollbackFor = {BadRequestException.class})
    public void createLifeRisk(RiskTrans risk) throws BadRequestException {
        if (risk.getBindCode() == null) throw new BadRequestException("Binder is Mandatory");
        if (risk.getSclCode() == null) throw new BadRequestException("Sub Class is Mandatory");
        if (risk.getCoverCode() == null) throw new BadRequestException("Cover Type is Mandatory");
        if (risk.getInsuredCode() == null) throw new BadRequestException("Insured is Mandatory");
        boolean newRisk = risk.getRiskId() == null;
        if (risk.getWefDate().after(risk.getWetDate())) {
            throw new BadRequestException("Risk Wef Date Cannot be greater than Risk Wet");
        }
        Long riskId = null;
        if (!newRisk) {
            riskId = riskRepo.QueryRiskTrans(risk.getRiskId()).getRiskIdentifier();
        }
        PolicyTrans policy = risk.getPolicy();
        Date polWef = dateUtils.removeTime(policy.getWefDate());
        Date polWet = dateUtils.removeTime(policy.getWetDate());
        Date riskWef = dateUtils.removeTime(risk.getWefDate());
        Date riskWet = dateUtils.removeTime(risk.getWetDate());
        if (riskWef.before(polWef) || riskWef.after(polWet)
                || riskWet.before(polWef) || riskWet.after(polWet)) {
            throw new BadRequestException("Risk Cover Dates outside Policy Cover Periods Risk Wef "+riskWef+" Risk Wet "+riskWet+" Policy Wef "+polWef+" Policy Wet "+polWet);
        }
        risk.setBinder(binderRepo.findOne(risk.getBindCode()));
        risk.setCovertype(coverRepo.findOne(risk.getCoverCode()));
        risk.setSubclass(subclassRepo.findOne(risk.getSclCode()));
        risk.setInsured(clientRepo.findOne(risk.getInsuredCode()));
        risk.setBinderDetails(binderDetRepo.findOne(risk.getBinderDet()));
        if (risk.getPolBindCode() != null) {
            risk.setPolicyBinders(policyBindersRepo.findOne(risk.getPolBindCode()));
        }
        risk.setRiskIdentifier(riskId);
        if (risk.getAutogenCert() == null)
            risk.setAutogenCert("N");
        else if ("on".equalsIgnoreCase(risk.getAutogenCert())) {
            risk.setAutogenCert("Y");
        } else risk.setAutogenCert("N");
        if (newRisk) {
            List<SectionTrans> sectionTransactions = risk.getSections().stream().map(sectionbean -> {
                SectionsDef sectiondef = setupSectionRepo.findOne(sectionbean.getSection());
                SectionTrans section = new SectionTrans();
                section.setAmount((sectionbean.getAmount() != null) ? sectionbean.getAmount() : BigDecimal.ZERO);
                section.setCompute(sectionbean.isCompute());
                section.setDivFactor(sectionbean.getDivFactor());
                section.setFreeLimit(sectionbean.getFreeLimit());
                if (sectionbean.getRatesApplicable() != null && "Y".equalsIgnoreCase(sectionbean.getRatesApplicable())) {
                    List<PremRatesDef> premRates = premRatesRepo.getSectPremiumRates(risk.getBinderDet(), sectionbean.getAmount(), sectiondef.getId());
                    if (premRates.size() == 1) {
                        section.setPremRates(premRates.get(0));
                        section.setRate(premRates.get(0).getRate());
                    }
                } else {
                    List<PremRatesDef> premRates = premRatesRepo.getSectPremiumRates(risk.getBinderDet(), sectiondef.getId());
                    if (premRates.size() == 1) {
                        section.setPremRates(premRates.get(0));
                        section.setRate(sectionbean.getRate());
                    }
                }
                section.setMultiRate(sectionbean.getMultiplierRate());
                section.setCompute(true);
                section.setRate(sectionbean.getRate());
                section.setSection(sectiondef);
                section.setRisk(risk);
                return section;
            }).filter(a -> a.getPremRates() != null).collect(Collectors.toList());
            sectionRepo.save(sectionTransactions);
        }

        RiskTrans savedRisk = riskRepo.save(risk);

        Iterable<BinderReqrdDocs> reqdDocs = reqrdDocsRepo.findAll(QBinderReqrdDocs.binderReqrdDocs.binderDetail.detId.eq(savedRisk.getBinderDetails().getDetId())
                .and(QBinderReqrdDocs.binderReqrdDocs.mandatory.eq(true))
                .and(QBinderReqrdDocs.binderReqrdDocs.requiredDocs.requiredDoc.appliesNewBusiness.eq(true)));


        long riskIdentifier = Long.valueOf(String.valueOf(dateUtils.getUwYear(policy.getWefDate())) + String.valueOf(savedRisk.getRiskId()));
        if (newRisk) {
            PolicyActiveRisks activeRisk = new PolicyActiveRisks();
            activeRisk.setPolicy(policy);
            activeRisk.setRisk(risk);
            activeRisk.setRiskIdentifier(riskIdentifier);
            activeRisk.setStatus("NB");
            activeRisksRepo.save(activeRisk);
            savedRisk.setRiskIdentifier(riskIdentifier);
            riskRepo.save(savedRisk);
        }
        List<RiskDocs> riskDocs = new ArrayList<>();
        for (BinderReqrdDocs reqdDoc : reqdDocs) {
            if (riskDocsRepo.count(QRiskDocs.riskDocs.risk.riskIdentifier.eq(savedRisk.getRiskIdentifier()).and(QRiskDocs.riskDocs.reqdDocs.sclReqrdId.eq(reqdDoc.getRequiredDocs().getSclReqrdId()))) == 0) {
                RiskDocs riskDoc = new RiskDocs();
                riskDoc.setReqdDocs(reqdDoc.getRequiredDocs());
                riskDoc.setRisk(savedRisk);
                riskDocs.add(riskDoc);
            }
        }
        Iterable<SubClassReqdDocs> subClassReqDocs = subclassReqDocRepo.findAll(QSubClassReqdDocs.subClassReqdDocs.subclass.subId.eq(savedRisk.getBinderDetails().getSubCoverTypes().getSubclass().getSubId())
                .and(QSubClassReqdDocs.subClassReqdDocs.mandatory.eq(true))
                .and(QSubClassReqdDocs.subClassReqdDocs.requiredDoc.appliesEndorsement.eq(true)));
        for (SubClassReqdDocs reqdDoc : subClassReqDocs) {
            if (riskDocsRepo.count(QRiskDocs.riskDocs.risk.riskIdentifier.eq(savedRisk.getRiskIdentifier()).and(QRiskDocs.riskDocs.reqdDocs.sclReqrdId.eq(reqdDoc.getSclReqrdId()))) == 0
            ) {
                if (riskDocs.stream().filter(a -> a.getReqdDocs().getSclReqrdId() == reqdDoc.getSclReqrdId()).count() == 0) {
                    RiskDocs riskDoc = new RiskDocs();
                    riskDoc.setReqdDocs(reqdDoc);
                    riskDoc.setRisk(savedRisk);
                    riskDocs.add(riskDoc);
                }
            }
        }
        riskDocsRepo.save(riskDocs);
        if(policy.getProduct().isMotorProduct()){
            final List<BigDecimal> codes = subclassCertTypesRepo.getCertificateCode(risk.getSubclass().getSubId());
            if(codes.size()==1) {
                final RiskCertForm riskCertForm = new RiskCertForm();
                riskCertForm.setSubclasscertId( ((BigDecimal)codes.get(0)).longValue());
                riskCertForm.setWefDate(risk.getWefDate());
                riskCertForm.setWetDate(risk.getWetDate());
                riskCertForm.setRiskId(risk.getRiskId());
                try {
                    certService.createRiskCert(riskCertForm);
                } catch (BadRequestException ex) {
                    System.out.println("Error creating cert....");
                }
            }
        }

    }


    @PreAuthorize("hasAnyAuthority('SAVE_POLICY')")
    @Override
    @Modifying
    @Transactional(readOnly = false, rollbackFor = {BadRequestException.class})
    public void deleteRisk(Long riskId) throws BadRequestException {
        RiskTrans risk = riskRepo.findOne(riskId);
        if (risk == null) throw new BadRequestException("No Risk to delete");
        List<SectionTrans> sections = risk.getSectionTrans();
        Iterable<ScheduleTrans> scheduleTrans = scheduleTransRepo.findAll(QScheduleTrans.scheduleTrans.risk.riskId.eq(riskId));
        scheduleTransRepo.delete(scheduleTrans);
        Iterable<PrintCertificateQueue> certificateQueues = queueRepo.findAll(QPrintCertificateQueue.printCertificateQueue.risk.riskId.eq(riskId));
        queueRepo.delete(certificateQueues);
        Iterable<PolicyCerts> policyCerts = certsRepo.findAll(QPolicyCerts.policyCerts.risk.riskId.eq(riskId));
        certsRepo.delete(policyCerts);
        Iterable<RiskDocs> riskDocs = riskDocsRepo.findAll(QRiskDocs.riskDocs.risk.riskId.eq(riskId));
        riskDocsRepo.delete(riskDocs);
        Iterable<RiskInterestedParties> riskInterestedParties = riskIntPartiesRepo.findAll(QRiskInterestedParties.riskInterestedParties.risk.riskId.eq(riskId));
        riskIntPartiesRepo.delete(riskInterestedParties);
        sectionRepo.delete(sections);
        if ("NB".equalsIgnoreCase(risk.getTransType())) {
            PolicyActiveRisks activeRisk = activeRisksRepo.findOne(QPolicyActiveRisks.policyActiveRisks.risk.riskId.eq(riskId));
            if (activeRisk != null)
                activeRisksRepo.delete(activeRisk);
        } else {
            PolicyActiveRisks activeRisk = activeRisksRepo.findOne(QPolicyActiveRisks.policyActiveRisks.risk.riskId.eq(riskId));
            if (activeRisk.getPrevRisk() != null) {
                activeRisk.setRisk(activeRisk.getPrevRisk());
                activeRisk.setPrevRisk(null);
                activeRisksRepo.save(activeRisk);
            } else {
                activeRisksRepo.delete(activeRisk);
            }
        }
        riskRepo.delete(riskId);

    }

    @PreAuthorize("hasAnyAuthority('MAKE_POLICY_READY')")
    @Override
    @Transactional(readOnly = false, rollbackFor = {BadRequestException.class})
    public void makeMedicalReady(Long polCode) throws BadRequestException {
        PolicyTrans policy = policyRepo.findOne(polCode);
        if (policy == null) throw new BadRequestException("No Policy Transaction to make Ready");
        if ("CR".equalsIgnoreCase(policy.getTransType())) {
            if (policy.getReissueCardFee() == null || policy.getReissueCardFee().compareTo(BigDecimal.ZERO) == 0) {
                throw new BadRequestException("Cannot authorise...Reissue Charge fee is zero");
            }
        }
        if (!authLimits.checkAuthorizationLimits("MAKE_POLICY_READY", policy.getBasicPrem())) {
            throw new BadRequestException("You have no rights to make ready the transaction...Check your authorization limits..");
        }
        long selfParamsCount = selfFundParamsRepo.count(QSelfFundParams.selfFundParams.policyTrans.policyId.eq(polCode));

        boolean selffundPolicy = (policy.getBinder().getFundBinder() != null && "Y".equalsIgnoreCase(policy.getBinder().getFundBinder()));

        if (selffundPolicy) {
            if (selfParamsCount == 0)
                throw new BadRequestException("Fund Parameters Record is Mandatory for Fund Transactions");
        }

        Iterable<MedicalCategory> risks = categoryRepo.findAll(QMedicalCategory.medicalCategory.policy.policyId.eq(polCode));
        for (MedicalCategory category : risks) {
            if (membersRepo.count(QCategoryMembers.categoryMembers.category.id.eq(category.getId())) == 0) {
                throw new BadRequestException("Cannot Make Ready the policy...No Category Members Specified for " + category.getDesc());
            }
            Iterable<CategoryMembers> categoryMembers = membersRepo.findAll(QCategoryMembers.categoryMembers.category.id.eq(category.getId()));
            for (CategoryMembers categoryMember : categoryMembers) {
                Iterable<RiskDocs> riskDocs = riskDocsRepo.findAll(QRiskDocs.riskDocs.member.sectId.eq(categoryMember.getSectId()));
                for (RiskDocs riskDoc : riskDocs) {
                    if (riskDoc.getCheckSum() == null || org.apache.commons.lang.StringUtils.isBlank(riskDoc.getCheckSum())) {
                        throw new BadRequestException(String.format("Cannot authorize policy without uploading documents for Member No %s", categoryMember.getMemberShipNo()));
                    }
                }
            }
        }
        Date polWef = dateUtils.removeTime(policy.getWefDate());
        Date polWet = dateUtils.removeTime(policy.getWetDate());
        Iterable<MedicalCategory> categories = categoryRepo.findAll(QMedicalCategory.medicalCategory.policy.policyId.eq(polCode));
        Iterable<SelfFundParams> selfFundParams = selfFundParamsRepo.findAll(QSelfFundParams.selfFundParams.policyTrans.policyId.eq(polCode));
        if (selffundPolicy) {
            for (SelfFundParams selfFundParam : selfFundParams) {
                if ((selfFundParam.getFundDepositAmount() == null || selfFundParam.getFundDepositAmount().compareTo(BigDecimal.ZERO) <= 0) && (policy.getTransType() == "NB" || policy.getTransType() == "RN"))
                    throw new BadRequestException("The self fund deposit amount cannot be Zero for this transaction");
            }
        } else {
            if ((policy.getBasicPrem() == null || policy.getBasicPrem().compareTo(BigDecimal.ZERO) <= 0) && (policy.getTransType() == "NB" || policy.getTransType() == "RN"))
                throw new BadRequestException("Premium amount cannot be Zero for this transaction");
        }
        for (MedicalCategory category : categories) {
            Iterable<MedCategoryBenefits> categoryBenefits = categoryBenefitRepo.findAll(QMedCategoryBenefits.medCategoryBenefits.category.id.eq(category.getId()));
            if (selffundPolicy) {
                for (MedCategoryBenefits benefit : categoryBenefits) {
                    if (benefit.getFundLimit() == null || benefit.getFundLimit().compareTo(BigDecimal.ZERO) <= 0)
                        throw new BadRequestException("Limit amount is mandatory for all benefits in this policy");
                }
            } else {
                for (MedCategoryBenefits benefit : categoryBenefits) {
                    if (benefit.getLimit() == null)
                        throw new BadRequestException("Limit amount is mandatory for all benefits in this policy");
                    if ((benefit.getPremium() == null || benefit.getPremium().compareTo(BigDecimal.ZERO) <= 0) && (policy.getTransType() == "NB" || policy.getTransType() == "RN")) {
                        throw new BadRequestException("Premium for benefit " + benefit.getCover().getSection().getDesc() + " cannot be zero or negative");
                    }
                }

            }
            Iterable<CategoryMembers> categoryMembers = membersRepo.findAll(QCategoryMembers.categoryMembers.category.id.eq(category.getId()));
            for (CategoryMembers member : categoryMembers) {
                Date riskWef = dateUtils.removeTime(member.getWefDate());
                Date riskWet = dateUtils.removeTime(member.getWetDate());
                if ((riskWef.before(polWef) || riskWef.after(polWet)
                        || riskWet.before(polWef) || riskWet.after(polWet)) && (!(member.getMemberStatus().equalsIgnoreCase("A")) && !(member.getMemberStatus().equalsIgnoreCase("E")))) {
                    throw new BadRequestException("Member Cover Dates outside Policy Cover Periods ");
                }
            }

        }
        boolean medicalProduct = false;
        if (policy.getProduct().getProGroup().getPrgType() == null || !policy.getProduct().getProGroup().getPrgType().equalsIgnoreCase("MD")) {
            medicalProduct = false;
        } else if (policy.getProduct().getProGroup().getPrgType().equalsIgnoreCase("MD")) {
            medicalProduct = true;
        }
        rulesExecutor.handlePolicyChecks(policy);
        if ("D".equalsIgnoreCase(policy.getAuthStatus())) {
            policy.setAuthStatus("R");
            policyRepo.save(policy);
            Map<String, Object> processVariables = Maps.newHashMap();
            processVariables.put("canAuthorize", true);
            workflowService.completeTask(String.valueOf(polCode), processVariables, policy, DocType.GEN_UW_DOCUMENT, (medicalProduct) ? "Y" : "N", null, null, null);
        }
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = {BadRequestException.class})
    public void makeRenewalReady(Long polCode) throws BadRequestException {
        PolicyTrans policy = policyRepo.findOne(polCode);
        if (policy == null) throw new BadRequestException("No Policy Transaction to make Ready");
        Date polWef = dateUtils.removeTime(policy.getWefDate());
        Date polWet = dateUtils.removeTime(policy.getWetDate());
        Iterable<RiskTrans> risks = riskRepo.findAll(QRiskTrans.riskTrans.policy.policyId.eq(polCode));
        for (RiskTrans riskBean : risks) {
            Date riskWef = dateUtils.removeTime(riskBean.getWefDate());
            Date riskWet = dateUtils.removeTime(riskBean.getWetDate());
            if (riskWef.before(polWef) || riskWef.after(polWet)
                    || riskWet.before(polWef) || riskWet.after(polWet)) {
                throw new BadRequestException("Risk Cover Dates outside Policy Cover Periods ");
            }

            if (riskBean.getBinderDetails().getBinder().getBinId() != riskBean.getBinder().getBinId()) {
                throw new BadRequestException("Cannot Make Ready...Sub Class and Cover Type Details do not match the binder selected");
            }
        }

        if ("EN".equalsIgnoreCase(policy.getTransType()) || "RS".equalsIgnoreCase(policy.getTransType()) || "CO".equalsIgnoreCase(policy.getTransType())) {
            Long remarkCount = policyRemarksRepo.count(QPolicyRemarks.policyRemarks.policy.policyId.eq(polCode));
            if (remarkCount == 0)
                throw new BadRequestException("Endorsement Remarks are mandatory");
            PolicyRemarks remarks = policyRemarksRepo.findOne(QPolicyRemarks.policyRemarks.policy.policyId.eq(polCode));
            if (remarks.getPolRemarks() == null || StringUtils.isBlank(remarks.getPolRemarks()))
                throw new BadRequestException("Endorsement Remarks are mandatory");
        }
        if ("D".equalsIgnoreCase(policy.getAuthStatus())) {
            policy.setAuthStatus("R");
            policyRepo.save(policy);
        }
    }

    private BigDecimal sign(String type) {
        return ("C".equalsIgnoreCase(type) ? BigDecimal.ONE.multiply(BigDecimal.valueOf(-1)) : BigDecimal.ONE.multiply(BigDecimal.valueOf(1)));
    }

    @PreAuthorize("hasAnyAuthority('MAKE_POLICY_READY')")
    @Override
    @Transactional(readOnly = false, rollbackFor = {BadRequestException.class}, propagation = Propagation.REQUIRED)
    public String makeLifeReady(Long polCode) throws BadRequestException {
        PolicyTrans policy = policyRepo.findOne(polCode);
        if (policy == null) throw new BadRequestException("No Policy Transaction to make Ready");
        if (!authLimits.checkAuthorizationLimits("MAKE_POLICY_READY", policy.getBasicPrem())) {
            throw new BadRequestException("You have no rights to make ready the transaction...Check your authorization limits..");
        }

        boolean checks = false;// rulesExecutor.handlePolicyChecks(policy);
        long sectCount = sectionRepo.count(QSectionTrans.sectionTrans.risk.policy.policyId.eq(polCode));
        if (sectCount == 0)
            throw new BadRequestException("Cannot Make Ready the Policy without Premium Items..");
        Date polWef = dateUtils.removeTime(policy.getWefDate());
        Date polWet = dateUtils.removeTime(policy.getWetDate());

        Iterable<RiskTrans> risks = riskRepo.findAll(QRiskTrans.riskTrans.policy.policyId.eq(polCode));
        for (RiskTrans riskBean : risks) {
            Date riskWef = dateUtils.removeTime(riskBean.getWefDate());
            Date riskWet = dateUtils.removeTime(riskBean.getWetDate());
            if (riskWef.before(polWef) || riskWef.after(polWet)
                    || riskWet.before(polWef) || riskWet.after(polWet)) {
                throw new BadRequestException("Risk Cover Dates outside Policy Cover Periods ");
            }

//            Iterable<RiskDocs> riskDocs = riskDocsRepo.findAll(QRiskDocs.riskDocs.risk.riskId.eq(riskBean.getRiskId()));
//            for (RiskDocs riskDoc : riskDocs) {
//                if (riskDoc.getCheckSum() == null || org.apache.commons.lang.StringUtils.isBlank(riskDoc.getCheckSum())) {
//                    throw new BadRequestException(String.format("Cannot authorize policy without uploading documents for Risk %s", riskBean.getRiskShtDesc()));
//                }
//            }

            if (riskBean.getBinderDetails().getBinder().getBinId() != riskBean.getBinder().getBinId()) {
                throw new BadRequestException("Cannot Make Ready...Sub Class and Cover Type Details do not match the binder selected");
            }
        }
        policy.setAuthStatus("R");
        policyRepo.save(policy);
        Map<String, Object> processVariables = Maps.newHashMap();
        processVariables.put("canAuthorize", !checks);
        workflowService.completeTask(String.valueOf(polCode), processVariables, policy, DocType.GEN_UW_DOCUMENT, "N", null, null, null);
        return (!checks) ? "Y" : "N";
    }

    @PreAuthorize("hasAnyAuthority('MAKE_POLICY_READY')")
    @Override
    @Transactional(readOnly = false, rollbackFor = {BadRequestException.class}, propagation = Propagation.REQUIRED)
    public String makeReady(Long polCode) throws BadRequestException {
        PolicyTrans policy = policyRepo.findOne(polCode);
        if (policy == null) throw new BadRequestException("No Policy Transaction to make Ready");
        if (!authLimits.checkAuthorizationLimits("MAKE_POLICY_READY", policy.getBasicPrem())) {
            throw new BadRequestException("You have no rights to make ready the transaction...Check your authorization limits..");
        }

        boolean checks = false;// rulesExecutor.handlePolicyChecks(policy);
        long sectCount = sectionRepo.count(QSectionTrans.sectionTrans.risk.policy.policyId.eq(polCode));
        if (sectCount == 0)
            throw new BadRequestException("Cannot Make Ready the Policy without Premium Items..");
        Date polWef = dateUtils.removeTime(policy.getWefDate());
        Date polWet = dateUtils.removeTime(policy.getWetDate());

        Iterable<RiskTrans> risks = riskRepo.findAll(QRiskTrans.riskTrans.policy.policyId.eq(polCode));
        for (RiskTrans riskBean : risks) {
            Date riskWef = dateUtils.removeTime(riskBean.getWefDate());
            Date riskWet = dateUtils.removeTime(riskBean.getWetDate());
            if (riskWef.before(polWef) || riskWef.after(polWet)
                    || riskWet.before(polWef) || riskWet.after(polWet)) {
                throw new BadRequestException("Risk Cover Dates outside Policy Cover Periods ");
            }

//            Iterable<RiskDocs> riskDocs = riskDocsRepo.findAll(QRiskDocs.riskDocs.risk.riskId.eq(riskBean.getRiskId()));
//            for (RiskDocs riskDoc : riskDocs) {
//                if (riskDoc.getCheckSum() == null || org.apache.commons.lang.StringUtils.isBlank(riskDoc.getCheckSum())) {
//                    throw new BadRequestException(String.format("Cannot authorize policy without uploading documents for Risk %s", riskBean.getRiskShtDesc()));
//                }
//            }

            if (riskBean.getBinderDetails().getBinder().getBinId() != riskBean.getBinder().getBinId()) {
                throw new BadRequestException("Cannot Make Ready...Sub Class and Cover Type Details do not match the binder selected");
            }
        }

        if ("EN".equalsIgnoreCase(policy.getTransType()) || "CO".equalsIgnoreCase(policy.getTransType())) {
            Long remarkCount = policyRemarksRepo.count(QPolicyRemarks.policyRemarks.policy.policyId.eq(polCode));
            if (remarkCount == 0)
                throw new BadRequestException("Cannot Make Ready Endorsement Without Endorsement Remarks");
            PolicyRemarks remarks = policyRemarksRepo.findOne(QPolicyRemarks.policyRemarks.policy.policyId.eq(polCode));
            if (remarks.getPolRemarks() == null || StringUtils.isBlank(remarks.getPolRemarks()))
                throw new BadRequestException("Cannot Make Ready Endorsement Without Endorsement Remarks");
        }
        policy.setAuthStatus("R");
        policyRepo.save(policy);
        boolean cashBasis = policy.getInterfaceType() != null && "C".equalsIgnoreCase(policy.getInterfaceType());
        SystemTransactionsTemp trans = systemTransactionsTempRepo.findOne(QSystemTransactionsTemp.systemTransactionsTemp.policy.policyId.eq(policy.getPolicyId()));

        if (!cashBasis && trans != null) {
            systemTransactionsTempRepo.delete(trans);
        }
        if (!checks) {
            if (cashBasis) {
                BigDecimal basicPrem = (policy.getPremium() == null) ? BigDecimal.ZERO : policy.getPremium();
                if (trans == null) {
                    trans = new SystemTransactionsTemp();
                    Predicate seqPredicate = QSystemSequence.systemSequence.transType.eq("D");
                    if (sequenceRepo.count(seqPredicate) == 0)
                        throw new BadRequestException("Sequence for Debit Notes has not been defined");
                    SystemSequence sequence = sequenceRepo.findOne(seqPredicate);
                    Long seqNumber = sequence.getNextNumber();
                    String transType = policy.getTransType();
                    if ("NB".equalsIgnoreCase(transType)) {
                        transType = "NB";
                    } else if ("RN".equalsIgnoreCase(transType)) {
                        transType = "RN";
                    } else {
                        transType = "EN";
                    }
                    TransactionMapping mapping = transMappingRepo.findOne(QTransactionMapping.transactionMapping.transType.eq(transType));
                    final String refNo = ((basicPrem.compareTo(BigDecimal.ZERO) >= 0) ? mapping.getDebitCode() : mapping.getCreditCode()) + String.format("%05d", seqNumber);
                    final String debitCode = ((basicPrem.compareTo(BigDecimal.ZERO) >= 0) ? mapping.getDebitCode() : mapping.getCreditCode());
                    sequence.setLastNumber(seqNumber);
                    sequence.setNextNumber(seqNumber + 1);
                    sequenceRepo.save(sequence);
                    trans.setRefNo(refNo);
                    trans.setTransType(debitCode);
                }


                BigDecimal extras = (policy.getExtras() == null) ? BigDecimal.ZERO : policy.getExtras();
                BigDecimal phcf = (policy.getPhcf() == null) ? BigDecimal.ZERO : policy.getPhcf();
                BigDecimal tl = (policy.getTrainingLevy() == null) ? BigDecimal.ZERO : policy.getTrainingLevy();
                BigDecimal sd = (policy.getStampDuty() == null) ? BigDecimal.ZERO : policy.getStampDuty();
                BigDecimal amountWithTaxes = basicPrem.add(extras).add(phcf).add(tl).add(sd);

                if (basicPrem.compareTo(BigDecimal.ZERO) == 1) {
                    String type = (amountWithTaxes.compareTo(BigDecimal.ZERO) == 1) ? "D" : "C";

                    trans.setAmount(basicPrem.abs().multiply(sign(type)).setScale(policy.getTransCurrency().getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
                    trans.setAuthDate(new Date());
                    trans.setAuthorised("Y");
                    trans.setBalance(amountWithTaxes.abs().multiply(sign(type)).setScale(policy.getTransCurrency().getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
                    trans.setBranch(policy.getBranch());
                    trans.setClientType("C");
                    trans.setControlAcc(policy.getClient().getTenantNumber());
                    trans.setClient(policy.getClient());
                    trans.setCurrRate(new BigDecimal(1));
                    trans.setCurrency(policy.getTransCurrency());
                    trans.setNarrations("Posting client Debit Note");
                    trans.setNetAmount(amountWithTaxes.abs().multiply(sign(type)).setScale(policy.getTransCurrency().getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
                    trans.setOrigin("U");
                    trans.setPhfund(phcf.setScale(policy.getTransCurrency().getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
                    trans.setPolicy(policy);
                    trans.setSd(sd.abs().multiply(sign(type)).setScale(policy.getTransCurrency().getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
                    trans.setTl(tl.abs().multiply(sign(type)).setScale(policy.getTransCurrency().getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
                    trans.setTransDate(new Date());
                    trans.setTransdc((amountWithTaxes.compareTo(BigDecimal.ZERO) == 1) ? "D" : "C");
                    trans.setUserAuth(userUtils.getCurrentUser().getUsername());
                    trans.setWhtx(BigDecimal.ZERO);
                    trans.setExtras(extras.abs().multiply(sign(type)).setScale(policy.getTransCurrency().getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
                    trans.setPostedDate(new Date());
                    trans.setPostedUser(userUtils.getCurrentUser());
                    systemTransactionsTempRepo.save(trans);
                }
            }
        }
        //  if ("D".equalsIgnoreCase(policy.getAuthStatus())) {
        boolean medicalProduct = false;
        if (policy.getProduct().getProGroup().getPrgType() == null || !policy.getProduct().getProGroup().getPrgType().equalsIgnoreCase("MD")) {
            medicalProduct = false;
        } else if (policy.getProduct().getProGroup().getPrgType().equalsIgnoreCase("MD")) {
            medicalProduct = true;
        }

        Map<String, Object> processVariables = Maps.newHashMap();
        processVariables.put("canAuthorize", !checks);
        workflowService.completeTask(String.valueOf(polCode), processVariables, policy, DocType.GEN_UW_DOCUMENT, (medicalProduct) ? "Y" : "N", null, null, null);
        return (!checks) ? "Y" : "N";
        //}
    }

    @PreAuthorize("hasAnyAuthority('PROPOSAL_CONVERSION')")
    @Override
    @Transactional(readOnly = false, rollbackFor = {BadRequestException.class})
    public void convertPropToPolicy(Long polCode) throws BadRequestException {
        PolicyTrans policy = policyRepo.findOne(polCode);
        if (policy == null) throw new BadRequestException("No Proposal Transaction to convert");
        if (!authLimits.checkAuthorizationLimits("PROPOSAL_CONVERSION", policy.getBasicPrem())) {
            throw new BadRequestException("You have no rights to convert the transaction...Check your authorization limits..");
        }
        if (!"R".equalsIgnoreCase(policy.getAuthStatus())) {
            throw new BadRequestException("Can only convert proposal after Make Ready Process..");
        }
        Iterable<LifeReceipts> lifeReceipts = lifeReceiptsRepo.findAll(QLifeReceipts.lifeReceipts.policyTrans.policyId.eq(policy.getPolicyId())
                .and(QLifeReceipts.lifeReceipts.balanceAmt.gt(0)));
        BigDecimal totRcptAmount = BigDecimal.ZERO;


        for (LifeReceipts rct : lifeReceipts) {
            if ("D".equalsIgnoreCase(rct.getDrCr()))
                totRcptAmount = totRcptAmount.subtract(rct.getReceiptAmt());
            else totRcptAmount = totRcptAmount.add(rct.getReceiptAmt());
            lifeService.allocateLifeRcptBalance(polCode);
        }
        if(policy.getNetPrem()==null){
            throw new BadRequestException("Policy Premium cannot be null");
        }
        if ( totRcptAmount.compareTo(policy.getNetPrem()) < 0) {
            throw new BadRequestException("Receipt amount must be atleast equal to one instalment premium..");
        }
        boolean checks = false; // rulesExecutor.handlePolicyChecks(policy);
        long sectCount = sectionRepo.count(QSectionTrans.sectionTrans.risk.policy.policyId.eq(polCode));
        if (sectCount == 0)
            throw new BadRequestException("Cannot convert proposal without Premium Items..");
        Date polWef = dateUtils.removeTime(policy.getWefDate());
        Date polWet = dateUtils.removeTime(policy.getWetDate());
        Iterable<RiskTrans> risks = riskRepo.findAll(QRiskTrans.riskTrans.policy.policyId.eq(polCode));
        for (RiskTrans riskBean : risks) {
            Date riskWef = dateUtils.removeTime(riskBean.getWefDate());
            Date riskWet = dateUtils.removeTime(riskBean.getWetDate());
            if (riskWef.before(polWef) || riskWef.after(polWet)
                    || riskWet.before(polWef) || riskWet.after(polWet)) {
                throw new BadRequestException("Risk Cover Dates outside Policy Cover Periods ");
            }

            Iterable<RiskDocs> riskDocs = riskDocsRepo.findAll(QRiskDocs.riskDocs.risk.riskId.eq(riskBean.getRiskId()));
            for (RiskDocs riskDoc : riskDocs) {
                if (riskDoc.getCheckSum() == null || org.apache.commons.lang.StringUtils.isBlank(riskDoc.getCheckSum())) {
                    throw new BadRequestException(String.format("Cannot convert to policy without uploading documents for Risk %s", riskBean.getRiskShtDesc()));
                }
            }

            if (riskBean.getBinderDetails().getBinder().getBinId() != riskBean.getBinder().getBinId()) {
                throw new BadRequestException("Cannot convert...Sub Class and Cover Type Details do not match the binder selected");
            }
        }
        if ("R".equalsIgnoreCase(policy.getAuthStatus())) {
            boolean medicalProduct = false;
            if (policy.getProduct().getProGroup().getPrgType() == null || !policy.getProduct().getProGroup().getPrgType().equalsIgnoreCase("MD")) {
                medicalProduct = false;
            } else if (policy.getProduct().getProGroup().getPrgType().equalsIgnoreCase("MD")) {
                medicalProduct = true;
            }
            String policyNumberFormat = paramService.getParameterString("POLICY_NO_FORMAT");
            Predicate seqPredicate = QSystemSequence.systemSequence.transType.eq("P");
            if (sequenceRepo.count(seqPredicate) == 0)
                throw new BadRequestException("Sequence for New Business Transactions has not been defined");
            SystemSequence sequence = sequenceRepo.findOne(seqPredicate);
            Long seqNumber = sequence.getNextNumber();
            //System.out.println("branch="+policy.getBranchId()+";prod="+policy.getProdId());
            final String policyNumber = templateMerger.generateFormat(policyNumberFormat, policy.getBranch().getObId(), policy.getProduct().getProCode(), policy.getWefDate(), sequence.getSeqPrefix() + String.format("%05d", seqNumber), null);
            policy.setPolNo(policyNumber);
            sequence.setLastNumber(seqNumber);
            sequence.setNextNumber(seqNumber + 1);
            sequenceRepo.save(sequence);
            policy.setAuthStatus("CV");
            policyRepo.save(policy);
//            Map<String, Object> processVariables = Maps.newHashMap();
//            processVariables.put("canAuthorize", (checks) ? false : true);
//            workflowService.completeTask(String.valueOf(polCode), processVariables, policy, DocType.GEN_UW_DOCUMENT, (medicalProduct) ? "Y" : "N", null, null, null);
        }
    }

    @PreAuthorize("hasAnyAuthority('MAKE_POLICY_READY')")
    @Override
    @Transactional(readOnly = false, rollbackFor = {BadRequestException.class})
    public void undoMakeReady(Long polCode) throws BadRequestException {
        PolicyTrans policy = policyRepo.findOne(polCode);
        if (policy == null) throw new BadRequestException("No Policy Transaction to Undo make Ready");
        if ("CN".equalsIgnoreCase(policy.getTransType())) {
            throw new BadRequestException("Cannot Undo Make Ready A Cancellation Transaction");
        }
        if ("RS".equalsIgnoreCase(policy.getTransType())) {
            throw new BadRequestException("Cannot Undo Make Ready A Section Reinstatement Transaction");
        }
        boolean cashBasis = policy.getInterfaceType() != null && "C".equalsIgnoreCase(policy.getInterfaceType());
        if (cashBasis) {
            SystemTransactionsTemp systemTransactionsTemp = systemTransactionsTempRepo.findOne(QSystemTransactionsTemp.systemTransactionsTemp.policy.policyId.eq(polCode));
            if (systemTransactionsTemp != null && systemTransactionsTemp.getBalance().compareTo(BigDecimal.ZERO) == 1) {
                if (systemTransactionsTemp.getSettleAmt() != null && systemTransactionsTemp.getSettleAmt().compareTo(BigDecimal.ZERO) == 1) {
                    throw new BadRequestException("Cannot undo make ready to already paid transaction....");
                }

                systemTransactionsTemp.setAuthorised("N");
                systemTransactionsTempRepo.save(systemTransactionsTemp);
            }


        }
        boolean medicalProduct = false;
        if (policy.getProduct().getProGroup().getPrgType() == null || !policy.getProduct().getProGroup().getPrgType().equalsIgnoreCase("MD")) {
            medicalProduct = false;
        } else if (policy.getProduct().getProGroup().getPrgType().equalsIgnoreCase("MD")) {
            medicalProduct = true;
        }
        policy.setAuthStatus("D");
        policyRepo.save(policy);
        transChecksRepo.delete(transChecksRepo.findAll(QTransChecks.transChecks.policyTrans.policyId.eq(polCode)));
        Map<String, Object> processVariables = Maps.newHashMap();
        processVariables.put("confirmAuth", false);
        processVariables.put("rejectTrans", true);
        processVariables.put("hasAuthority", false);
        workflowService.completeTask(String.valueOf(polCode), processVariables, policy, DocType.GEN_UW_DOCUMENT, (medicalProduct) ? "Y" : "N", null, null, null);

    }

    @Override
    @Transactional(readOnly = true)
    public List<PremRatesDef> getNewPremiumItems(Long detId, Long riskId, String searchVal) {
        if (riskId == null || detId == null)
            return new ArrayList<>();
        return sectionRepo.getUnassignedPremItems(detId, riskId, searchVal);

    }

    @Override
    public List<PremRatesDef> getNewSectPremiumItems(Long detId, Long riskId, String searchVal, Long insuredAge) {
        if (riskId == null || detId == null || insuredAge == null)
            return new ArrayList<>();
        return sectionRepo.getRangePremRates(detId, riskId, searchVal, BigDecimal.valueOf(insuredAge));
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = {BadRequestException.class})
    public void createRiskSections(RiskBean sections) throws BadRequestException {
        if (sections.getRiskId() == null)
            throw new BadRequestException("Cannot save a section without Risk Transaction");
        RiskTrans risk = riskRepo.findOne(sections.getRiskId());
        if (risk == null) throw new BadRequestException("Cannot save a section without Risk Transaction");
        List<SectionTrans> sectionTransactions = new ArrayList<>();
        for (RiskSectionBean sectionbean : sections.getSections()) {
            SectionsDef sectiondef = setupSectionRepo.findOne(sectionbean.getSection());
            SectionTrans section = new SectionTrans();
            if (sectionbean.getRatesApplicable() != null && "Y".equalsIgnoreCase(sectionbean.getRatesApplicable())) {
                List<PremRatesDef> premRates = premRatesRepo.getSectPremiumRates(risk.getBinderDetails().getDetId(), sectionbean.getAmount(), sectiondef.getId());
                if (premRates.size() == 0)
                    throw new BadRequestException("No Premium Rates for the Value entered...");
                if (premRates.size() > 1)
                    throw new BadRequestException("Duplicate Prem Rates set up for the Section..." + sectionbean.getSectionDesc());
                section.setPremRates(premRates.get(0));
                section.setRate(premRates.get(0).getRate());
            } else {
                List<PremRatesDef> premRates = premRatesRepo.getSectPremiumRates(risk.getBinderDetails().getDetId(), sectiondef.getId());
                if (premRates.size() == 0)
                    throw new BadRequestException("No Premium Rates for the Value entered...");
                if (premRates.size() > 1)
                    throw new BadRequestException("Duplicate Prem Rates set up for the Section..." + sectionbean.getSectionDesc());
                section.setPremRates(premRates.get(0));
                section.setRate(sectionbean.getRate());
            }
            section.setAmount((sectionbean.getAmount() == null) ? BigDecimal.ZERO : sectionbean.getAmount());
            section.setCompute(sectionbean.isCompute());
            section.setDivFactor(sectionbean.getDivFactor());
            section.setFreeLimit(sectionbean.getFreeLimit());
            section.setMultiRate(sectionbean.getMultiplierRate());
            section.setCompute(true);
            section.setSection(sectiondef);
            section.setRisk(risk);
            sectionTransactions.add(section);
        }
        sectionRepo.save(sectionTransactions);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<PolicyTaxes> getNewTaxes(Long polId) throws BadRequestException {
        PolicyTrans policy = policyRepo.findOne(polId);
        if (policy == null) throw new BadRequestException("No Policy Transaction");
        Set<PolicyTaxes> policyTaxes = new HashSet<>();
        Iterable<PolicyTaxes> polTaxes = polTaxesRepo.findAll(QPolicyTaxes.policyTaxes.policy.policyId.eq(polId));
        polTaxes.forEach(policyTaxes::add);
        Set<PolicyTaxes> newTaxes = new HashSet<>();
        Iterable<RiskTrans> risks = riskRepo.findAll(QRiskTrans.riskTrans.policy.policyId.eq(policy.getPolicyId()));
        for (RiskTrans risk : risks) {
            Iterable<TaxRates> taxes = taxRatesRepo.findAll(QTaxRates.taxRates.subclass.subId.eq(risk.getSubclass().getSubId()).and(QTaxRates.taxRates.active.eq(true)));
            for (TaxRates tax : taxes) {
                PolicyTaxes polTax = new PolicyTaxes();
                polTax.setTaxLevel(tax.getTaxLevel());
                polTax.setDivFactor(tax.getDivFactor());
                polTax.setRateType(tax.getRateType());
                polTax.setRevenueItems(tax.getRevenueItems());
                polTax.setPolicy(policy);
                polTax.setTaxRate(tax.getTaxRate());
                polTax.setPolTaxId(tax.getTaxId());
                newTaxes.add(polTax);
            }
        }
        newTaxes.removeAll(policyTaxes);
        return newTaxes;
    }

    @Override
    public List<InterestedParties> getNewInterestedParties(Long riskId) throws BadRequestException {
        RiskTrans riskTrans = riskRepo.findOne(riskId);
        if (riskTrans == null) throw new BadRequestException("No Risk Transaction");
        return interestedPartiesRepo.searchRiskInterestedParties(riskId);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<PolicyClauses> getNewClauses(Long polId) throws BadRequestException {
        PolicyTrans policy = policyRepo.findOne(polId);
        if (policy == null) throw new BadRequestException("No Policy Transaction");
        Set<PolicyClauses> policyClauses = new HashSet<>();
        Iterable<PolicyClauses> polClauses = polClausesRepo.findAll(QPolicyClauses.policyClauses.policy.policyId.eq(policy.getPolicyId()));
        polClauses.forEach(policyClauses::add);
        Set<PolicyClauses> newClauses = new HashSet<>();
        Iterable<RiskTrans> risks = riskRepo.findAll(QRiskTrans.riskTrans.policy.policyId.eq(policy.getPolicyId()));
        for (RiskTrans risk : risks) {
            Iterable<BinderClauses> binderClauses = binderClauseRepo.findAll(QBinderClauses.binderClauses.binderDet.detId.eq(risk.getBinderDetails().getDetId()).and(QBinderClauses.binderClauses.mandatory.eq("Y")));
            for (BinderClauses clause : binderClauses) {
                PolicyClauses polClause = new PolicyClauses();
                polClause.setClauHeading(clause.getClause().getClause().getClauHeading());
                polClause.setClause(clause.getClause());
                polClause.setClauWording((clause.getClauWording() != null) ? clause.getClauWording() : clause.getClause().getClause().getClauWording());
                polClause.setEditable(clause.getClause().getClause().isEditable());
                polClause.setNewClause("Y");
                polClause.setPolicy(policy);
                newClauses.add(polClause);
            }
            Iterable<SubclassClauses> subClauses = subclauseRepo.findAll(QSubclassClauses.subclassClauses.subclass.subId.eq(risk.getSubclass().getSubId()));
            for (SubclassClauses clause : subClauses) {
                PolicyClauses polClause = new PolicyClauses();
                polClause.setClauHeading(clause.getClause().getClauHeading());
                polClause.setClause(clause);
                polClause.setClauWording(clause.getClause().getClauWording());
                polClause.setEditable(clause.getClause().isEditable());
                polClause.setNewClause("Y");
                polClause.setPolicy(policy);
                newClauses.add(polClause);
            }
        }
        newClauses.removeAll(policyClauses);
        return newClauses;
    }

    @PreAuthorize("hasAnyAuthority('SAVE_POLICY')")
    @Override
    @Transactional(readOnly = false)
    public void deletePolicyClause(Long clauseId) throws BadRequestException {
        PolicyClauses clause = polClausesRepo.findOne(clauseId);
        if (clause.getClause().isMandatory()) {
            throw new BadRequestException("Cannot delete Mandatory Clause");
        }
        List<BinderClauses> binderClauses = clause.getClause().getBinderClauses();
        for (BinderClauses binClauses : binderClauses) {
            if (binClauses.getMandatory() != null && "Y".equalsIgnoreCase(binClauses.getMandatory())) {
                throw new BadRequestException("Cannot delete Mandatory Clause");
            }

        }
        polClausesRepo.delete(clauseId);

    }

    @PreAuthorize("hasAnyAuthority('SAVE_POLICY')")
    @Override
    @Transactional(readOnly = false)
    public void deletePolicyTax(Long taxId) {
        polTaxesRepo.delete(taxId);

    }

    @PreAuthorize("hasAnyAuthority('SAVE_POLICY')")
    @Override
    @Transactional(readOnly = false, rollbackFor = {BadRequestException.class})
    public void createClause(PolicyClausesBean clause) throws BadRequestException {
        PolicyTrans policy = policyRepo.findOne(clause.getPolId());
        List<PolicyClauses> createdClauses = new ArrayList<>();
        if (policy == null) throw new BadRequestException("No Policy Transaction");
        for (Long clauseId : clause.getClauses()) {
            for (Iterator<PolicyClauses> it = getNewClauses(clause.getPolId()).iterator(); it.hasNext(); ) {
                PolicyClauses cl = (PolicyClauses) it.next();
                if (cl.getClause().getClauId().longValue() == clauseId.longValue()) {
                    createdClauses.add(cl);
                }

            }
        }
        polClausesRepo.save(createdClauses);

    }

    @PreAuthorize("hasAnyAuthority('SAVE_POLICY')")
    @Override
    @Transactional(readOnly = false)
    public void createPolicyClause(PolicyClauses clause) throws BadRequestException {
        polClausesRepo.save(clause);

    }

    @PreAuthorize("hasAnyAuthority('SAVE_POLICY')")
    @Override
    @Transactional(readOnly = false)
    public void createPolicyTaxes(PolicyTaxes policyTax) throws BadRequestException {
        polTaxesRepo.save(policyTax);
    }

    @Override
    @Transactional(readOnly = true)
    public DataTablesResult<EndorsementRemarks> findEndorsementRemarks(DataTablesRequest request, Long polCode)
            throws IllegalAccessException {
        Page<EndorsementRemarks> page = policyRemarksRepo.getEndorsementRemarks(polCode, "", request);
        return new DataTablesResult<>(request, page);
    }

    @Override
    public DataTablesResult<PolicyInstallments> findPolicyInstallments(DataTablesRequest request, Long polCode) throws IllegalAccessException {
        Page<PolicyInstallments> page = policyInstallmentsRepo.findAll(QPolicyInstallments.policyInstallments.policyTrans.policyId.eq(polCode), request);
        return new DataTablesResult<>(request, page);
    }

    @Override
    @Transactional(readOnly = true)
    public PolicyRemarks getPolicyRemarks(Long polCode) {
        Iterable<PolicyRemarks> policyRemarks = policyRemarksRepo.findAll(QPolicyRemarks.policyRemarks.policy.policyId.eq(polCode));
        PolicyRemarks remark = null;
        for (PolicyRemarks remarks : policyRemarks) {
            remark = remarks;
            break;
        }
        return remark;
    }

    @PreAuthorize("hasAnyAuthority('SAVE_POLICY')")
    @Override
    @Transactional(readOnly = false)
    public void saveEndorsementRemarks(PolicyRemarks remarks) throws BadRequestException {
        if (remarks.getRemarks() == null || StringUtils.isBlank(remarks.getRemarks()))
            throw new BadRequestException("Please Provide Endorsement Remarks to continue");
        if (remarks.getEndRemarks().getRemarkId() == null)
            throw new BadRequestException("Select Existing Endorsement Remarks to Modify and Save");
        PolicyTrans policy = policyRepo.findOne(remarks.getPolicy().getPolicyId());
        String remark = templateMerger.mergePolicyDetails(policy, remarks.getRemarks());
        remarks.setPolRemarks(remark);
        policyRemarksRepo.save(remarks);

    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public DataTablesResult<PolicyTrans> findEnquiryPolicies(DataTablesRequest request, String drNumber,
//                                                             Long clientCode, String polNo, String endorseNumber, Long agentCode,
//                                                             Long prodCode) throws IllegalAccessException {
//        QClientDef client = QPolicyTrans.policyTrans.client;
//        QAccountDef account = QPolicyTrans.policyTrans.agent;
//        QProductsDef product = QPolicyTrans.policyTrans.product;
//        Predicate pred = null;
//
//        if (drNumber == null || StringUtils.isBlank(drNumber)) {
//            drNumber = null;
//        } else {
//            drNumber = drNumber.toLowerCase();
//        }
//
//        if (polNo == null || StringUtils.isBlank(polNo)) {
//            polNo = "";
//        } else {
//            polNo = polNo.toLowerCase();
//        }
//
//        if (endorseNumber == null || StringUtils.isBlank(endorseNumber)) {
//            endorseNumber = "";
//        } else {
//            endorseNumber = endorseNumber.toLowerCase();
//        }
//
//        //System.out.println("polNo "+polNo);
//        pred = QPolicyTrans.policyTrans.polNo.lower().containsIgnoreCase(StringUtils.lowerCase(polNo))
//                .and(QPolicyTrans.policyTrans.riskTrans.any().riskShtDesc.containsIgnoreCase(endorseNumber))
//                .and(QPolicyTrans.policyTrans.product.proGroup.prgType.upper().notEqualsIgnoreCase("MD"))
////                .and(QPolicyTrans.policyTrans.authStatus.notEqualsIgnoreCase("D"))
//                .and((drNumber == null) ? QPolicyTrans.policyTrans.isNotNull() : QPolicyTrans.policyTrans.refNo.lower().containsIgnoreCase(StringUtils.lowerCase(drNumber)))
//                .and((agentCode == null) ? account.isNotNull() : account.acctId.eq(agentCode))
//                .and((prodCode == null) ? product.isNotNull() : product.proCode.eq(prodCode))
//                .and((clientCode == null) ? client.isNotNull() : client.tenId.eq(clientCode));
//        Page<PolicyTrans> page = policyRepo.findAll(pred, request);
//        return new DataTablesResult(request, page);
//    }

    @Override
    @Transactional(readOnly = true)
    public DataTablesResult<EndorsementsDTO> findEnquiryPolicies(DataTablesRequest request, String drNumber,
                                                                 Long clientCode, String polNo, String riskShtDesc, Long agentCode,
                                                                 Long prodCode)  {

        if (drNumber == null || StringUtils.isBlank(drNumber)) {
            drNumber = null;
        } else {
            drNumber = drNumber.toLowerCase();
        }

        if (polNo == null || StringUtils.isBlank(polNo)) {
            polNo = null;
        } else {
            polNo = polNo.toLowerCase();
        }

        if (riskShtDesc == null || StringUtils.isBlank(riskShtDesc)) {
            riskShtDesc = null;
        } else {
            riskShtDesc = riskShtDesc.toLowerCase();
        }
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        EndorsementsDtoMapper mapper = new EndorsementsDtoMapper();
        long countPolicies = jdbcTemplate.queryForObject(EndorsementPolicyQueries.countPolicyQuery, Long.class,
                new Object[]{polNo, riskShtDesc, drNumber, agentCode, agentCode, prodCode, clientCode});
        List<EndorsementsDTO> endorsementsDTOList = jdbcTemplate.query(EndorsementPolicyQueries.getPolicyEnquiryQuery, mapper,
                new Object[]{polNo, riskShtDesc, drNumber, agentCode, agentCode, prodCode, clientCode,
                        request.getPageNumber(), request.getPageSize(), request.getPageNumber(), request.getPageSize()});
        Page<EndorsementsDTO> page = new PageImpl<>(endorsementsDTOList, request, countPolicies);
        return new DataTablesResult(request, page);
    }

    @Override
    public DataTablesResult<PolicyTrans> findActiveEnquiryPolicies(DataTablesRequest request, String drNumber, Long clientCode, String polNo, String endorseNumber, Long agentCode, Long prodCode) throws IllegalAccessException {
        QClientDef client = QPolicyTrans.policyTrans.client;
        QAccountDef account = QPolicyTrans.policyTrans.agent;
        QProductsDef product = QPolicyTrans.policyTrans.product;
        Predicate pred = null;

        if (drNumber == null || StringUtils.isBlank(drNumber)) {
            drNumber = null;
        } else {
            drNumber = drNumber.toLowerCase();
        }

        if (polNo == null || StringUtils.isBlank(polNo)) {
            polNo = "";
        } else {
            polNo = polNo.toLowerCase();
        }

        if (endorseNumber == null || StringUtils.isBlank(endorseNumber)) {
            endorseNumber = "";
        } else {
            endorseNumber = endorseNumber.toLowerCase();
        }

        //System.out.println("polNo "+polNo);
        pred = QPolicyTrans.policyTrans.polNo.lower().containsIgnoreCase(StringUtils.lowerCase(polNo))
                .and(QPolicyTrans.policyTrans.riskTrans.any().riskShtDesc.containsIgnoreCase(endorseNumber))
                .and(QPolicyTrans.policyTrans.product.proGroup.prgType.upper().notEqualsIgnoreCase("MD"))
                .and(QPolicyTrans.policyTrans.currentStatus.in("A", "L"))
                .and((drNumber == null) ? QPolicyTrans.policyTrans.isNotNull() : QPolicyTrans.policyTrans.refNo.lower().containsIgnoreCase(StringUtils.lowerCase(drNumber)))
                .and((agentCode == null) ? account.isNotNull() : account.acctId.eq(agentCode))
                .and((prodCode == null) ? product.isNotNull() : product.proCode.eq(prodCode))
                .and((clientCode == null) ? client.isNotNull() : client.tenId.eq(clientCode));
        Page<PolicyTrans> page = policyRepo.findAll(pred, request);
        return new DataTablesResult(request, page);
    }

    @Override
    @Transactional(readOnly = true)
    public DataTablesResult<PolicyTrans> findEnquiryMedPolicies(DataTablesRequest request, String drNumber,
                                                                Long clientCode, String polNo, String endorseNumber, Long agentCode,
                                                                Long prodCode) throws IllegalAccessException {
        QClientDef client = QPolicyTrans.policyTrans.client;
        QAccountDef account = QPolicyTrans.policyTrans.agent;
        QProductsDef product = QPolicyTrans.policyTrans.product;
        Predicate pred = null;

        if (drNumber == null || StringUtils.isBlank(drNumber)) {
            drNumber = null;
        } else {
            drNumber = drNumber.toLowerCase();
        }

        if (polNo == null || StringUtils.isBlank(polNo)) {
            polNo = "";
        } else {
            polNo = polNo.toLowerCase();
        }

        if (endorseNumber == null || StringUtils.isBlank(endorseNumber)) {
            endorseNumber = "";
        } else {
            endorseNumber = endorseNumber.toLowerCase();
        }
        pred = QPolicyTrans.policyTrans.polNo.lower().containsIgnoreCase(StringUtils.lowerCase(polNo))
                // .and(QPolicyTrans.policyTrans.riskTrans.any().riskShtDesc.containsIgnoreCase(endorseNumber))
                .and(QPolicyTrans.policyTrans.product.proGroup.prgType.equalsIgnoreCase("MD"))
//                .and(QPolicyTrans.policyTrans.authStatus.notEqualsIgnoreCase("D"))
                .and((drNumber == null) ? QPolicyTrans.policyTrans.isNotNull() : QPolicyTrans.policyTrans.refNo.lower().containsIgnoreCase(StringUtils.lowerCase(drNumber)))
                .and((agentCode == null) ? account.isNotNull() : account.acctId.eq(agentCode))
                .and((prodCode == null) ? product.isNotNull() : product.proCode.eq(prodCode))
                .and((clientCode == null) ? client.isNotNull() : client.tenId.eq(clientCode));
        Page<PolicyTrans> page = policyRepo.findAll(pred, request);
        return new DataTablesResult(request, page);
    }

    @Override
    public DataTablesResult<PolicyTrans> findEnquiryActiveorLapsedMedPolicies(DataTablesRequest request, String drNumber, Long clientCode, String polNo, String endorseNumber, Long agentCode, Long prodCode) throws IllegalAccessException {
        QClientDef client = QPolicyTrans.policyTrans.client;
        QAccountDef account = QPolicyTrans.policyTrans.agent;
        QProductsDef product = QPolicyTrans.policyTrans.product;
        Predicate pred = null;

        if (drNumber == null || StringUtils.isBlank(drNumber)) {
            drNumber = null;
        } else {
            drNumber = drNumber.toLowerCase();
        }

        if (polNo == null || StringUtils.isBlank(polNo)) {
            polNo = "";
        } else {
            polNo = polNo.toLowerCase();
        }

        if (endorseNumber == null || StringUtils.isBlank(endorseNumber)) {
            endorseNumber = "";
        } else {
            endorseNumber = endorseNumber.toLowerCase();
        }
        pred = QPolicyTrans.policyTrans.polNo.lower().containsIgnoreCase(StringUtils.lowerCase(polNo))
                // .and(QPolicyTrans.policyTrans.riskTrans.any().riskShtDesc.containsIgnoreCase(endorseNumber))
                .and(QPolicyTrans.policyTrans.product.proGroup.prgType.equalsIgnoreCase("MD"))
                .and(QPolicyTrans.policyTrans.currentStatus.in("A", "L"))
                .and((drNumber == null) ? QPolicyTrans.policyTrans.isNotNull() : QPolicyTrans.policyTrans.refNo.lower().containsIgnoreCase(StringUtils.lowerCase(drNumber)))
                .and((agentCode == null) ? account.isNotNull() : account.acctId.eq(agentCode))
                .and((prodCode == null) ? product.isNotNull() : product.proCode.eq(prodCode))
                .and((clientCode == null) ? client.isNotNull() : client.tenId.eq(clientCode));
        Page<PolicyTrans> page = policyRepo.findAll(pred, request);
        return new DataTablesResult(request, page);
    }

    @Override
    public DataTablesResult<PolicyTrans> findPendingPolicies(DataTablesRequest request, String drNumber, Long clientCode, String polNo, String endorseNumber, Long agentCode, Long prodCode) throws IllegalAccessException {
        QClientDef client = QPolicyTrans.policyTrans.client;
        QAccountDef account = QPolicyTrans.policyTrans.agent;
        QProductsDef product = QPolicyTrans.policyTrans.product;
        Predicate pred = null;

        if (drNumber == null || StringUtils.isBlank(drNumber)) {
            drNumber = "";
        }

        if (polNo == null || StringUtils.isBlank(polNo)) {
            polNo = "";
        }

        if (endorseNumber == null || StringUtils.isBlank(endorseNumber)) {
            endorseNumber = "";
            pred = QPolicyTrans.policyTrans.polNo.containsIgnoreCase(polNo)
                    .and(QPolicyTrans.policyTrans.authStatus.ne("A"))
                    .and((agentCode == null) ? account.isNotNull() : account.acctId.eq(agentCode))
                    .and((prodCode == null) ? product.isNotNull() : product.proCode.eq(prodCode))
                    .and((clientCode == null) ? client.isNotNull() : client.tenId.eq(clientCode));
        } else {
            pred = QPolicyTrans.policyTrans.polNo.containsIgnoreCase(polNo)
                    .and(QPolicyTrans.policyTrans.authStatus.ne("A"))
                    .and(QPolicyTrans.policyTrans.riskTrans.any().riskShtDesc.containsIgnoreCase(endorseNumber))
                    .and(QPolicyTrans.policyTrans.refNo.containsIgnoreCase(drNumber)
                            .or(QPolicyTrans.policyTrans.refNo.isNull()))
                    .and((agentCode == null) ? account.isNotNull() : account.acctId.eq(agentCode))
                    .and((prodCode == null) ? product.isNotNull() : product.proCode.eq(prodCode))
                    .and((clientCode == null) ? client.isNotNull() : client.tenId.eq(clientCode));
        }
        Page<PolicyTrans> page = policyRepo.findAll(pred, request);
        return new DataTablesResult(request, page);
    }

    @PreAuthorize("hasAnyAuthority('SAVE_POLICY')")
    @Override
    @Transactional(readOnly = false)
    public void createTaxes(PolicyTaxBean taxBean) throws BadRequestException {
        PolicyTrans policy = policyRepo.findOne(taxBean.getPolId());
        List<PolicyTaxes> createdTaxes = new ArrayList<>();
        if (policy == null) throw new BadRequestException("No Policy Transaction");
        for (Long taxId : taxBean.getTaxes()) {
            TaxRates tax = taxRatesRepo.findOne(taxId);
            PolicyTaxes polTax = new PolicyTaxes();
            polTax.setTaxLevel(tax.getTaxLevel());
            polTax.setDivFactor(tax.getDivFactor());
            polTax.setRateType(tax.getRateType());
            polTax.setRevenueItems(tax.getRevenueItems());
            polTax.setTaxRate(tax.getTaxRate());
            polTax.setSubclass(tax.getSubclass());
            polTax.setPolicy(policy);
            createdTaxes.add(polTax);
        }
        polTaxesRepo.save(createdTaxes);
    }

    @Override
    @Transactional(readOnly = false)
    public void createIntParties(RiskIntPartiesBean partiesBean) throws BadRequestException {
        RiskTrans riskTrans = riskRepo.findOne(partiesBean.getRiskId());
        List<RiskInterestedParties> interestedParties = new ArrayList<>();
        if (riskTrans == null) throw new BadRequestException("No Risk Transaction");
        for (Long partId : partiesBean.getParties()) {
            InterestedParties parties = interestedPartiesRepo.findOne(partId);
            RiskInterestedParties riskInterestedParties = new RiskInterestedParties();
            riskInterestedParties.setInterestedParties(parties);
            riskInterestedParties.setRisk(riskTrans);
            interestedParties.add(riskInterestedParties);
        }

        riskIntPartiesRepo.save(interestedParties);
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteRiskIntParty(Long partId) {
        riskIntPartiesRepo.delete(partId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PolicyTrans> findAllPolicies(String paramString, Pageable paramPageable) {
        Predicate pred = null;
        if (paramString == null || StringUtils.isBlank(paramString)) {
            pred = QPolicyTrans.policyTrans.isNotNull().and(QPolicyTrans.policyTrans.authStatus.eq("A"));
        } else {
            pred = (QPolicyTrans.policyTrans.authStatus.eq("A")).and(QPolicyTrans.policyTrans.polNo.containsIgnoreCase(paramString)
                    .or(QPolicyTrans.policyTrans.polRevNo.containsIgnoreCase(paramString)));
        }
        return policyRepo.findAll(pred, paramPageable);
    }

    @Override
    @Transactional(readOnly = true)
    public DataTablesResult<ScheduleTrans> findRiskSchedules(DataTablesRequest request, Long riskId) throws IllegalAccessException {
        BooleanExpression pred = QScheduleTrans.scheduleTrans.risk.riskId.eq(riskId);
        Page<ScheduleTrans> page = scheduleTransRepo.findAll(pred.and(request.searchPredicate(QScheduleTrans.scheduleTrans)), request);
        return new DataTablesResult<>(request, page);
    }

    @PreAuthorize("hasAnyAuthority('SAVE_POLICY')")
    @Override
    @Transactional(readOnly = false)
    public void createRiskSchedules(VehicleDetails scheduleTrans) throws BadRequestException {
        if(scheduleTrans.getRiskId()==null){
            throw new BadRequestException("Please Select Risk ID to continue....");
        }
        final RiskTrans riskTrans = riskRepo.findOne(scheduleTrans.getRiskId());
        if(riskTrans==null){
            throw new BadRequestException("Please Select Risk ID to continue....");
        }
        MotorVehicleDetails vehicleDetails = new MotorVehicleDetails();
        vehicleDetails.setRisk(riskTrans);
        vehicleDetails.setVdId(scheduleTrans.getVdId());
        vehicleDetails.setBodyColor(scheduleTrans.getBodyColor());
        vehicleDetails.setBodyType(scheduleTrans.getBodyType());
        vehicleDetails.setCarMake(scheduleTrans.getCarMake());
        vehicleDetails.setCarModel(scheduleTrans.getCarModel());
        vehicleDetails.setCarryCapacity(scheduleTrans.getCarryCapacity());
        vehicleDetails.setEngineNumber(scheduleTrans.getEngineNumber());
        vehicleDetails.setChassisNumber(scheduleTrans.getChassisNo());
        vehicleDetails.setYearOfManufacture( Long.valueOf(scheduleTrans.getYearOfManufacture()));
        vehicleDetails.setEngineCapacity(scheduleTrans.getEngineCapacity());
        motorVehicleDetailsRepo.save(vehicleDetails);
    }

    @PreAuthorize("hasAnyAuthority('SAVE_POLICY')")
    @Override
    @Transactional(readOnly = false)
    public void deleteRiskSchedule(Long scheduleId) {
        scheduleTransRepo.delete(scheduleId);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = {BadRequestException.class})
    public void populateRiskScheduleDetails(Long riskId) throws BadRequestException {
        RiskTrans riskTrans = riskRepo.findOne(riskId);
        Long subId = riskTrans.getSubclass().getSubId();
        Long colCount = mappingRepo.count(QScheduleMapping.scheduleMapping.subclass.subId.eq(subId));
        if (colCount == 0) return;
        Iterable<ScheduleMapping> mappings = mappingRepo.findAll(QScheduleMapping.scheduleMapping.subclass.subId.eq(subId));
        Long count = scheduleTransRepo.count(QScheduleTrans.scheduleTrans.risk.riskId.eq(riskId));
        ScheduleTrans scheduleTrans = null;
        if (count > 1) return;
        else if (count == 1) {
            scheduleTrans = scheduleTransRepo.findOne(QScheduleTrans.scheduleTrans.risk.riskId.eq(riskId));
        } else if (count == 0)
            scheduleTrans = new ScheduleTrans();
        scheduleTrans.setRisk(riskTrans);

        boolean createRecord = false;
        for (ScheduleMapping mapping : mappings) {
            if (mapping.getColumnIndex().equalsIgnoreCase("1")) {
                if (mapping.getMappedRiskColumn() != null && mapping.getMappedRiskColumn().equalsIgnoreCase("riskId")) {
                    createRecord = true;
                    scheduleTrans.setColumn1(riskTrans.getRiskShtDesc());
                } else if (mapping.getMappedRiskColumn() != null && mapping.getMappedRiskColumn().equalsIgnoreCase("riskDesc")) {
                    createRecord = true;
                    scheduleTrans.setColumn1(riskTrans.getRiskDesc());
                } else if (mapping.getMappedRiskColumn() != null && mapping.getMappedRiskColumn().equalsIgnoreCase("riskDesc")) {
                    createRecord = true;
                    scheduleTrans.setColumn1(riskTrans.getRiskDesc());
                } else if (mapping.getMappedRiskColumn() != null && mapping.getMappedRiskColumn().equalsIgnoreCase("riskPrem")) {
                    createRecord = true;
                    scheduleTrans.setColumn1(riskTrans.getPremium() == null ? "0" : riskTrans.getPremium().toString());
                } else if (mapping.getMappedRiskColumn() != null && mapping.getMappedRiskColumn().equalsIgnoreCase("riskValue")) {
                    createRecord = true;
                    scheduleTrans.setColumn1(riskTrans.getSumInsured() == null ? "0" : riskTrans.getSumInsured().toString());
                }
                if (mapping.getMappedSections() != null) {
                    createRecord = true;
                    Stream<SectionTrans> sections = Streamable.streamOf(sectionRepo.findAll(QSectionTrans.sectionTrans.risk.riskId.eq(riskId)));
                    Optional<SectionTrans> sectionTrans = sections.filter(a -> a.getSection().getId() == mapping.getMappedSections().getId()).findFirst();
                    if (sectionTrans.isPresent())
                        scheduleTrans.setColumn1(String.valueOf(sectionTrans.get().getAmount()));
                }
            } else if (mapping.getColumnIndex().equalsIgnoreCase("2")) {
                if (mapping.getMappedRiskColumn() != null && mapping.getMappedRiskColumn().equalsIgnoreCase("riskId")) {
                    createRecord = true;
                    scheduleTrans.setColumn2(riskTrans.getRiskShtDesc());
                } else if (mapping.getMappedRiskColumn() != null && mapping.getMappedRiskColumn().equalsIgnoreCase("riskDesc")) {
                    createRecord = true;
                    scheduleTrans.setColumn2(riskTrans.getRiskDesc());
                } else if (mapping.getMappedRiskColumn() != null && mapping.getMappedRiskColumn().equalsIgnoreCase("riskDesc")) {
                    createRecord = true;
                    scheduleTrans.setColumn2(riskTrans.getRiskDesc());
                } else if (mapping.getMappedRiskColumn() != null && mapping.getMappedRiskColumn().equalsIgnoreCase("riskPrem")) {
                    createRecord = true;
                    scheduleTrans.setColumn2(riskTrans.getPremium() == null ? "0" : riskTrans.getPremium().toString());
                } else if (mapping.getMappedRiskColumn() != null && mapping.getMappedRiskColumn().equalsIgnoreCase("riskValue")) {
                    createRecord = true;
                    scheduleTrans.setColumn2(riskTrans.getSumInsured() == null ? "0" : riskTrans.getSumInsured().toString());
                }
                if (mapping.getMappedSections() != null) {
                    createRecord = true;
                    Stream<SectionTrans> sections = Streamable.streamOf(sectionRepo.findAll(QSectionTrans.sectionTrans.risk.riskId.eq(riskId)));
                    Optional<SectionTrans> sectionTrans = sections.filter(a -> a.getSection().getId() == mapping.getMappedSections().getId()).findFirst();
                    if (sectionTrans.isPresent())
                        scheduleTrans.setColumn2(String.valueOf(sectionTrans.get().getAmount()));
                }
            } else if (mapping.getColumnIndex().equalsIgnoreCase("3")) {
                if (mapping.getMappedRiskColumn() != null && mapping.getMappedRiskColumn().equalsIgnoreCase("riskId")) {
                    createRecord = true;
                    scheduleTrans.setColumn3(riskTrans.getRiskShtDesc());
                } else if (mapping.getMappedRiskColumn() != null && mapping.getMappedRiskColumn().equalsIgnoreCase("riskDesc")) {
                    createRecord = true;
                    scheduleTrans.setColumn3(riskTrans.getRiskDesc());
                } else if (mapping.getMappedRiskColumn() != null && mapping.getMappedRiskColumn().equalsIgnoreCase("riskDesc")) {
                    createRecord = true;
                    scheduleTrans.setColumn3(riskTrans.getRiskDesc());
                } else if (mapping.getMappedRiskColumn() != null && mapping.getMappedRiskColumn().equalsIgnoreCase("riskPrem")) {
                    createRecord = true;
                    scheduleTrans.setColumn3(riskTrans.getPremium() == null ? "0" : riskTrans.getPremium().toString());
                } else if (mapping.getMappedRiskColumn() != null && mapping.getMappedRiskColumn().equalsIgnoreCase("riskValue")) {
                    createRecord = true;
                    scheduleTrans.setColumn3(riskTrans.getSumInsured() == null ? "0" : riskTrans.getSumInsured().toString());
                }
                if (mapping.getMappedSections() != null) {
                    createRecord = true;
                    Stream<SectionTrans> sections = Streamable.streamOf(sectionRepo.findAll(QSectionTrans.sectionTrans.risk.riskId.eq(riskId)));
                    Optional<SectionTrans> sectionTrans = sections.filter(a -> a.getSection().getId() == mapping.getMappedSections().getId()).findFirst();
                    if (sectionTrans.isPresent())
                        scheduleTrans.setColumn3(String.valueOf(sectionTrans.get().getAmount()));
                }
            } else if (mapping.getColumnIndex().equalsIgnoreCase("4")) {
                if (mapping.getMappedRiskColumn() != null && mapping.getMappedRiskColumn().equalsIgnoreCase("riskId")) {
                    createRecord = true;
                    scheduleTrans.setColumn4(riskTrans.getRiskShtDesc());
                } else if (mapping.getMappedRiskColumn() != null && mapping.getMappedRiskColumn().equalsIgnoreCase("riskDesc")) {
                    createRecord = true;
                    scheduleTrans.setColumn4(riskTrans.getRiskDesc());
                } else if (mapping.getMappedRiskColumn() != null && mapping.getMappedRiskColumn().equalsIgnoreCase("riskDesc")) {
                    createRecord = true;
                    scheduleTrans.setColumn4(riskTrans.getRiskDesc());
                } else if (mapping.getMappedRiskColumn() != null && mapping.getMappedRiskColumn().equalsIgnoreCase("riskPrem")) {
                    createRecord = true;
                    scheduleTrans.setColumn4(riskTrans.getPremium() == null ? "0" : riskTrans.getPremium().toString());
                } else if (mapping.getMappedRiskColumn() != null && mapping.getMappedRiskColumn().equalsIgnoreCase("riskValue")) {
                    createRecord = true;
                    scheduleTrans.setColumn4(riskTrans.getSumInsured() == null ? "0" : riskTrans.getSumInsured().toString());
                }
                if (mapping.getMappedSections() != null) {
                    createRecord = true;
                    Stream<SectionTrans> sections = Streamable.streamOf(sectionRepo.findAll(QSectionTrans.sectionTrans.risk.riskId.eq(riskId)));
                    Optional<SectionTrans> sectionTrans = sections.filter(a -> a.getSection().getId() == mapping.getMappedSections().getId()).findFirst();
                    if (sectionTrans.isPresent())
                        scheduleTrans.setColumn4(String.valueOf(sectionTrans.get().getAmount()));
                }
            } else if (mapping.getColumnIndex().equalsIgnoreCase("5")) {
                if (mapping.getMappedRiskColumn() != null && mapping.getMappedRiskColumn().equalsIgnoreCase("riskId")) {
                    createRecord = true;
                    scheduleTrans.setColumn5(riskTrans.getRiskShtDesc());
                } else if (mapping.getMappedRiskColumn() != null && mapping.getMappedRiskColumn().equalsIgnoreCase("riskDesc")) {
                    createRecord = true;
                    scheduleTrans.setColumn5(riskTrans.getRiskDesc());
                } else if (mapping.getMappedRiskColumn() != null && mapping.getMappedRiskColumn().equalsIgnoreCase("riskDesc")) {
                    createRecord = true;
                    scheduleTrans.setColumn5(riskTrans.getRiskDesc());
                } else if (mapping.getMappedRiskColumn() != null && mapping.getMappedRiskColumn().equalsIgnoreCase("riskPrem")) {
                    createRecord = true;
                    scheduleTrans.setColumn5(riskTrans.getPremium() == null ? "0" : riskTrans.getPremium().toString());
                } else if (mapping.getMappedRiskColumn() != null && mapping.getMappedRiskColumn().equalsIgnoreCase("riskValue")) {
                    createRecord = true;
                    scheduleTrans.setColumn5(riskTrans.getSumInsured() == null ? "0" : riskTrans.getSumInsured().toString());
                }
                if (mapping.getMappedSections() != null) {
                    createRecord = true;
                    Stream<SectionTrans> sections = Streamable.streamOf(sectionRepo.findAll(QSectionTrans.sectionTrans.risk.riskId.eq(riskId)));
                    Optional<SectionTrans> sectionTrans = sections.filter(a -> a.getSection().getId() == mapping.getMappedSections().getId()).findFirst();
                    if (sectionTrans.isPresent())
                        scheduleTrans.setColumn5(String.valueOf(sectionTrans.get().getAmount()));
                }
            } else if (mapping.getColumnIndex().equalsIgnoreCase("6")) {
                if (mapping.getMappedRiskColumn() != null && mapping.getMappedRiskColumn().equalsIgnoreCase("riskId")) {
                    createRecord = true;
                    scheduleTrans.setColumn6(riskTrans.getRiskShtDesc());
                } else if (mapping.getMappedRiskColumn() != null && mapping.getMappedRiskColumn().equalsIgnoreCase("riskDesc")) {
                    createRecord = true;
                    scheduleTrans.setColumn6(riskTrans.getRiskDesc());
                } else if (mapping.getMappedRiskColumn() != null && mapping.getMappedRiskColumn().equalsIgnoreCase("riskDesc")) {
                    createRecord = true;
                    scheduleTrans.setColumn6(riskTrans.getRiskDesc());
                } else if (mapping.getMappedRiskColumn() != null && mapping.getMappedRiskColumn().equalsIgnoreCase("riskPrem")) {
                    createRecord = true;
                    scheduleTrans.setColumn6(riskTrans.getPremium() == null ? "0" : riskTrans.getPremium().toString());
                } else if (mapping.getMappedRiskColumn() != null && mapping.getMappedRiskColumn().equalsIgnoreCase("riskValue")) {
                    createRecord = true;
                    scheduleTrans.setColumn6(riskTrans.getSumInsured() == null ? "0" : riskTrans.getSumInsured().toString());
                }
                if (mapping.getMappedSections() != null) {
                    createRecord = true;
                    Stream<SectionTrans> sections = Streamable.streamOf(sectionRepo.findAll(QSectionTrans.sectionTrans.risk.riskId.eq(riskId)));
                    Optional<SectionTrans> sectionTrans = sections.filter(a -> a.getSection().getId() == mapping.getMappedSections().getId()).findFirst();
                    if (sectionTrans.isPresent())
                        scheduleTrans.setColumn6(String.valueOf(sectionTrans.get().getAmount()));
                }
            } else if (mapping.getColumnIndex().equalsIgnoreCase("7")) {
                if (mapping.getMappedRiskColumn() != null && mapping.getMappedRiskColumn().equalsIgnoreCase("riskId")) {
                    createRecord = true;
                    scheduleTrans.setColumn7(riskTrans.getRiskShtDesc());
                } else if (mapping.getMappedRiskColumn() != null && mapping.getMappedRiskColumn().equalsIgnoreCase("riskDesc")) {
                    createRecord = true;
                    scheduleTrans.setColumn7(riskTrans.getRiskDesc());
                } else if (mapping.getMappedRiskColumn() != null && mapping.getMappedRiskColumn().equalsIgnoreCase("riskDesc")) {
                    createRecord = true;
                    scheduleTrans.setColumn7(riskTrans.getRiskDesc());
                } else if (mapping.getMappedRiskColumn() != null && mapping.getMappedRiskColumn().equalsIgnoreCase("riskPrem")) {
                    createRecord = true;
                    scheduleTrans.setColumn7(riskTrans.getPremium() == null ? "0" : riskTrans.getPremium().toString());
                } else if (mapping.getMappedRiskColumn() != null && mapping.getMappedRiskColumn().equalsIgnoreCase("riskValue")) {
                    createRecord = true;
                    scheduleTrans.setColumn7(riskTrans.getSumInsured() == null ? "0" : riskTrans.getSumInsured().toString());
                }
                if (mapping.getMappedSections() != null) {
                    createRecord = true;
                    Stream<SectionTrans> sections = Streamable.streamOf(sectionRepo.findAll(QSectionTrans.sectionTrans.risk.riskId.eq(riskId)));
                    Optional<SectionTrans> sectionTrans = sections.filter(a -> a.getSection().getId() == mapping.getMappedSections().getId()).findFirst();
                    if (sectionTrans.isPresent())
                        scheduleTrans.setColumn7(String.valueOf(sectionTrans.get().getAmount()));
                }
            } else if (mapping.getColumnIndex().equalsIgnoreCase("8")) {
                if (mapping.getMappedRiskColumn() != null && mapping.getMappedRiskColumn().equalsIgnoreCase("riskId")) {
                    createRecord = true;
                    scheduleTrans.setColumn8(riskTrans.getRiskShtDesc());
                } else if (mapping.getMappedRiskColumn() != null && mapping.getMappedRiskColumn().equalsIgnoreCase("riskDesc")) {
                    createRecord = true;
                    scheduleTrans.setColumn8(riskTrans.getRiskDesc());
                } else if (mapping.getMappedRiskColumn() != null && mapping.getMappedRiskColumn().equalsIgnoreCase("riskDesc")) {
                    createRecord = true;
                    scheduleTrans.setColumn8(riskTrans.getRiskDesc());
                } else if (mapping.getMappedRiskColumn() != null && mapping.getMappedRiskColumn().equalsIgnoreCase("riskPrem")) {
                    createRecord = true;
                    scheduleTrans.setColumn8(riskTrans.getPremium() == null ? "0" : riskTrans.getPremium().toString());
                } else if (mapping.getMappedRiskColumn() != null && mapping.getMappedRiskColumn().equalsIgnoreCase("riskValue")) {
                    createRecord = true;
                    scheduleTrans.setColumn8(riskTrans.getSumInsured() == null ? "0" : riskTrans.getSumInsured().toString());
                }
                if (mapping.getMappedSections() != null) {
                    createRecord = true;
                    Stream<SectionTrans> sections = Streamable.streamOf(sectionRepo.findAll(QSectionTrans.sectionTrans.risk.riskId.eq(riskId)));
                    Optional<SectionTrans> sectionTrans = sections.filter(a -> a.getSection().getId() == mapping.getMappedSections().getId()).findFirst();
                    if (sectionTrans.isPresent())
                        scheduleTrans.setColumn8(String.valueOf(sectionTrans.get().getAmount()));
                }
            } else if (mapping.getColumnIndex().equalsIgnoreCase("9")) {
                if (mapping.getMappedRiskColumn() != null && mapping.getMappedRiskColumn().equalsIgnoreCase("riskId")) {
                    createRecord = true;
                    scheduleTrans.setColumn9(riskTrans.getRiskShtDesc());
                } else if (mapping.getMappedRiskColumn() != null && mapping.getMappedRiskColumn().equalsIgnoreCase("riskDesc")) {
                    createRecord = true;
                    scheduleTrans.setColumn9(riskTrans.getRiskDesc());
                } else if (mapping.getMappedRiskColumn() != null && mapping.getMappedRiskColumn().equalsIgnoreCase("riskDesc")) {
                    createRecord = true;
                    scheduleTrans.setColumn9(riskTrans.getRiskDesc());
                } else if (mapping.getMappedRiskColumn() != null && mapping.getMappedRiskColumn().equalsIgnoreCase("riskPrem")) {
                    createRecord = true;
                    scheduleTrans.setColumn9(riskTrans.getPremium() == null ? "0" : riskTrans.getPremium().toString());
                } else if (mapping.getMappedRiskColumn() != null && mapping.getMappedRiskColumn().equalsIgnoreCase("riskValue")) {
                    createRecord = true;
                    scheduleTrans.setColumn9(riskTrans.getSumInsured() == null ? "0" : riskTrans.getSumInsured().toString());
                }
                if (mapping.getMappedSections() != null) {
                    createRecord = true;
                    Stream<SectionTrans> sections = Streamable.streamOf(sectionRepo.findAll(QSectionTrans.sectionTrans.risk.riskId.eq(riskId)));
                    Optional<SectionTrans> sectionTrans = sections.filter(a -> a.getSection().getId() == mapping.getMappedSections().getId()).findFirst();
                    if (sectionTrans.isPresent())
                        scheduleTrans.setColumn9(String.valueOf(sectionTrans.get().getAmount()));
                }
            } else if (mapping.getColumnIndex().equalsIgnoreCase("10")) {
                if (mapping.getMappedRiskColumn() != null && mapping.getMappedRiskColumn().equalsIgnoreCase("riskId")) {
                    createRecord = true;
                    scheduleTrans.setColumn10(riskTrans.getRiskShtDesc());
                } else if (mapping.getMappedRiskColumn() != null && mapping.getMappedRiskColumn().equalsIgnoreCase("riskDesc")) {
                    createRecord = true;
                    scheduleTrans.setColumn10(riskTrans.getRiskDesc());
                } else if (mapping.getMappedRiskColumn() != null && mapping.getMappedRiskColumn().equalsIgnoreCase("riskDesc")) {
                    createRecord = true;
                    scheduleTrans.setColumn10(riskTrans.getRiskDesc());
                } else if (mapping.getMappedRiskColumn() != null && mapping.getMappedRiskColumn().equalsIgnoreCase("riskPrem")) {
                    createRecord = true;
                    scheduleTrans.setColumn10(riskTrans.getPremium() == null ? "0" : riskTrans.getPremium().toString());
                } else if (mapping.getMappedRiskColumn() != null && mapping.getMappedRiskColumn().equalsIgnoreCase("riskValue")) {
                    createRecord = true;
                    scheduleTrans.setColumn10(riskTrans.getSumInsured() == null ? "0" : riskTrans.getSumInsured().toString());
                }
                if (mapping.getMappedSections() != null) {
                    createRecord = true;
                    Stream<SectionTrans> sections = Streamable.streamOf(sectionRepo.findAll(QSectionTrans.sectionTrans.risk.riskId.eq(riskId)));
                    Optional<SectionTrans> sectionTrans = sections.filter(a -> a.getSection().getId() == mapping.getMappedSections().getId()).findFirst();
                    if (sectionTrans.isPresent())
                        scheduleTrans.setColumn10(String.valueOf(sectionTrans.get().getAmount()));
                }
            }
        }
        if (createRecord)
            scheduleTransRepo.save(scheduleTrans);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getCommissionRate(Long bindDetCode) throws BadRequestException {
        Iterable<CommissionRates> commissionRates = commRatesRepo.findAll(QCommissionRates.commissionRates.binderDet.detId.eq(bindDetCode));
        BigDecimal commRate = BigDecimal.ZERO;
        for (CommissionRates commissionRate : commissionRates) {
            commRate = commissionRate.getCommRate();
            break;
        }
        if (commRate.compareTo(BigDecimal.ZERO) == 0) {
            BinderDetails details = binderDetRepo.findOne(bindDetCode);
            commRate = details.getBinder().getAccount().getAccountType().getCommRate();
        }
        return commRate;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = {AdminFeeException.class})
    public Long createAdminFeeTrans(AdminFeeForm adminFeeForm) throws AdminFeeException {
        AdminFee adminFee = new AdminFee();
        adminFee.setApplicableAt("P");
        adminFee.setPreparedBy(userUtils.getCurrentUser());
        adminFee.setClientDef(clientRepo.findOne(adminFeeForm.getClientId()));
        adminFee.setCurrencies(currencyRepo.findOne(adminFeeForm.getCurrencyId()));
        adminFee.setBranch(branchRepo.findOne(adminFeeForm.getBrnCode()));
        adminFee.setRemarks(adminFeeForm.getRemarks());
        adminFee.setAsAtDate(adminFeeForm.getProcessDate());
        Predicate adminPredicate = QSystemSequence.systemSequence.transType.eq("AD");
        if (sequenceRepo.count(adminPredicate) == 0)
            throw new AdminFeeException("Sequence for Admin Fee Transactions has not been defined");
        SystemSequence adminSequence = sequenceRepo.findOne(adminPredicate);
        Long adminSequenceNextNumber = adminSequence.getNextNumber();
        final String refNo = adminSequence.getSeqPrefix() + String.format("%05d", adminSequenceNextNumber);
        adminFee.setRefNo(refNo);
        adminSequence.setLastNumber(adminSequenceNextNumber);
        adminSequence.setNextNumber(adminSequenceNextNumber + 1);
        adminFee.setAuthorised("N");
        adminFee.setPreparedDate(new Date());
        sequenceRepo.save(adminSequence);
        AdminFee savedAdminFee = adminFeeRepo.save(adminFee);
        return savedAdminFee.getAdminFeeId();
    }

    @Override
    @Transactional(readOnly = true)
    public DataTablesResult<AdminFee> findUnauthTrans(DataTablesRequest request) throws IllegalAccessException {
        BooleanExpression pred = (QAdminFee.adminFee.authorised.eq("N"));
        Page<AdminFee> page = adminFeeRepo.findAll(pred.and(request.searchPredicate(QAdminFee.adminFee)), request);
        return new DataTablesResult<>(request, page);
    }

    @Override
    @Transactional(readOnly = true)
    public DataTablesResult<AdminFee> findAuthorisedTrans(DataTablesRequest request) throws IllegalAccessException {
        BooleanExpression pred = (QAdminFee.adminFee.authorised.eq("Y"));
        Page<AdminFee> page = adminFeeRepo.findAll(pred.and(request.searchPredicate(QAdminFee.adminFee)), request);
        return new DataTablesResult<>(request, page);
    }

    @Override
    @Transactional(readOnly = true)
    public AdminFee getAdminFeeDetails(Long adminFeeId) {
        return adminFeeRepo.findOne(adminFeeId);
    }

    @Override
    @Transactional(readOnly = true)
    public DataTablesResult<AdminFeePolicies> findAdminFeePolicies(DataTablesRequest request, Long adminFeeId) throws IllegalAccessException {
        BooleanExpression pred = QAdminFeePolicies.adminFeePolicies.adminFee.adminFeeId.eq(adminFeeId);
        Page<AdminFeePolicies> page = adminFeePolRepo.findAll(pred.and(request.searchPredicate(QAdminFeePolicies.adminFeePolicies)), request);
        return new DataTablesResult<>(request, page);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = {BadRequestException.class})
    public void addAdminFeePolicies(AdminFeePolicyBean feePolicyBean) throws BadRequestException {
        if (feePolicyBean.getAdminFeeId() == null)
            throw new BadRequestException("Admin Fee Transaction required");

        if (feePolicyBean.getPolicies().size() == 0)
            throw new BadRequestException("No Policies to Add");

        BigDecimal vatOnExciseDutyRate = paramService.getParamValue("VAT_ON_EXCISE_DUTY");
        BigDecimal exciseDutyRate = paramService.getParamValue("EXCISE_DUTY_RATE");
        AdminFee adminFee = adminFeeRepo.findOne(feePolicyBean.getAdminFeeId());

        BigDecimal totalAdminFee = BigDecimal.ZERO;
        BigDecimal totalVatAmt = BigDecimal.ZERO;
        BigDecimal totalExciseDuty = BigDecimal.ZERO;
        BigDecimal totalVatOnExciseDuty = BigDecimal.ZERO;
        List<AdminFeePolicies> adminFeePoliciesList = new ArrayList<>();
        Iterable<AdminFeePolicies> savedAdminFeePolicies = adminFeePolRepo.findAll(QAdminFeePolicies.adminFeePolicies.adminFee.adminFeeId.eq(feePolicyBean.getAdminFeeId()));
        for (AdminFeePolicies adminFeePolicy : savedAdminFeePolicies) {
            PolicyTrans policyTrans = adminFeePolicy.getPolicy();
            Iterable<SelfFundParams> selfFundParams = selfFundParamsRepo.findAll(QSelfFundParams.selfFundParams.policyTrans.policyId.eq(policyTrans.getPolicyId()));
            if (selfFundParams.spliterator().getExactSizeIfKnown() == 1) {
                BigDecimal vatTotal = BigDecimal.ZERO;
                SelfFundParams selfFundParam = null;
                for (SelfFundParams fundParams : selfFundParams) {
                    selfFundParam = fundParams;
                    break;
                }
                if ("FFS".equalsIgnoreCase(selfFundParam.getApplicableLevel())) {
                    totalAdminFee = totalAdminFee.add(selfFundParam.getApplicableValue());
                    vatTotal = selfFundParam.getApplicableValue();
                } else if ("FFP".equalsIgnoreCase(selfFundParam.getApplicableLevel())) {
                    Long memberCount = membersRepo.count(QCategoryMembers.categoryMembers.category.policy.policyId.eq(policyTrans.getPolicyId()));
                    totalAdminFee = totalAdminFee.add(selfFundParam.getApplicableValue().multiply(BigDecimal.valueOf(memberCount)));
                    vatTotal = selfFundParam.getApplicableValue().multiply(BigDecimal.valueOf(memberCount));
                } else if ("FFF".equalsIgnoreCase(selfFundParam.getApplicableLevel())) {
                    Long familyCount = membersRepo.count(QCategoryMembers.categoryMembers.category.policy.policyId.eq(policyTrans.getPolicyId())
                            .and(QCategoryMembers.categoryMembers.dependentTypes.eq("P")));
                    totalAdminFee = totalAdminFee.add(selfFundParam.getApplicableValue().multiply(BigDecimal.valueOf(familyCount)));
                    vatTotal = selfFundParam.getApplicableValue().multiply(BigDecimal.valueOf(familyCount));
                }

                totalVatAmt = totalVatAmt.add(policyTrans.getAgent().getAccountType().getVatRate().multiply(vatTotal).divide(BigDecimal.valueOf(100)));
                totalExciseDuty = totalExciseDuty.add(exciseDutyRate.multiply(vatTotal).divide(BigDecimal.valueOf(100)));
            }
        }
        for (long policyId : feePolicyBean.getPolicies()) {
            PolicyTrans policyTrans = policyRepo.findOne(policyId);
            Calendar wetDate = Calendar.getInstance();
            wetDate.setTime(policyTrans.getWetDate());

            Calendar currentDate = Calendar.getInstance();
            currentDate.setTime(new Date());
            if (policyTrans.getWetDate().before(new Date())) {
                if (wetDate.get(Calendar.MONTH) < currentDate.get(Calendar.MONTH)) {
                    continue;
                }

            }
//            Iterable<SelfFundParams> selfFundParams = selfFundParamsRepo.findAll(QSelfFundParams.selfFundParams.policyTrans.policyId.eq(policyId));
//            if (selfFundParams.spliterator().getExactSizeIfKnown() == 1) {
                BigDecimal vatTotal = BigDecimal.ZERO;
                BigDecimal adminFeeTotal = BigDecimal.ZERO;
                SelfFundParams selfFundParam = null;
//                for (SelfFundParams fundParams : selfFundParams) {
//                    selfFundParam = fundParams;
//                    break;
//                }
//				 if("M".equalsIgnoreCase(selfFundParam.getBillingFrequency())){
//					 //to be implemented
//				 }
            totalAdminFee = totalAdminFee.add(BigDecimal.valueOf(1000));
            adminFeeTotal = BigDecimal.valueOf(10000);
            vatTotal = BigDecimal.valueOf(1400);
//                if ("FFS".equalsIgnoreCase(selfFundParam.getApplicableLevel())) {
//                    totalAdminFee = totalAdminFee.add(selfFundParam.getApplicableValue());
//                    adminFeeTotal = selfFundParam.getApplicableValue();
//                    vatTotal = selfFundParam.getApplicableValue();
//                } else if ("FFP".equalsIgnoreCase(selfFundParam.getApplicableLevel())) {
//                    Long memberCount = membersRepo.count(QCategoryMembers.categoryMembers.category.policy.policyId.eq(policyId));
//                    totalAdminFee = totalAdminFee.add(selfFundParam.getApplicableValue().multiply(BigDecimal.valueOf(memberCount)));
//                    adminFeeTotal = selfFundParam.getApplicableValue().multiply(BigDecimal.valueOf(memberCount));
//                    vatTotal = selfFundParam.getApplicableValue().multiply(BigDecimal.valueOf(memberCount));
//                } else if ("FFF".equalsIgnoreCase(selfFundParam.getApplicableLevel())) {
//                    Long familyCount = membersRepo.count(QCategoryMembers.categoryMembers.category.policy.policyId.eq(policyId)
//                            .and(QCategoryMembers.categoryMembers.dependentTypes.eq("P")));
//                    totalAdminFee = totalAdminFee.add(selfFundParam.getApplicableValue().multiply(BigDecimal.valueOf(familyCount)));
//                    adminFeeTotal = selfFundParam.getApplicableValue().multiply(BigDecimal.valueOf(familyCount));
//                    vatTotal = selfFundParam.getApplicableValue().multiply(BigDecimal.valueOf(familyCount));
//                }

                totalVatAmt = totalVatAmt.add(policyTrans.getAgent().getAccountType().getVatRate().multiply(vatTotal).divide(BigDecimal.valueOf(100)));
                totalExciseDuty = totalExciseDuty.add(exciseDutyRate.multiply(vatTotal).divide(BigDecimal.valueOf(100)));

                AdminFeePolicies adminFeePolicies = new AdminFeePolicies();
                adminFeePolicies.setPolicy(policyTrans);
                adminFeePolicies.setAdminFee(adminFeeRepo.findOne(feePolicyBean.getAdminFeeId()));
                adminFeePolicies.setProcessedDate(new Date());
                adminFeePolicies.setAdminFeeAmt(adminFeeTotal);
                adminFeePolicies.setVatAmt(policyTrans.getAgent().getAccountType().getVatRate().multiply(vatTotal).divide(BigDecimal.valueOf(100)));
                adminFeePolicies.setExciseDuty(exciseDutyRate.multiply(vatTotal).divide(BigDecimal.valueOf(100)));
                adminFeePolicies.setVatExciseDuty(vatOnExciseDutyRate.multiply(exciseDutyRate.multiply(vatTotal).divide(BigDecimal.valueOf(100))).divide(BigDecimal.valueOf(100)));
                adminFeePolicies.setAdminNetAmt(adminFeeTotal.add(vatTotal.add(exciseDutyRate.multiply(vatTotal).divide(BigDecimal.valueOf(100)))).add(vatOnExciseDutyRate.multiply(exciseDutyRate.multiply(vatTotal).divide(BigDecimal.valueOf(100))).divide(BigDecimal.valueOf(100))));
                if (!checkDuplicate(adminFeePoliciesList, adminFeePolicies))
                    adminFeePoliciesList.add(adminFeePolicies);

            }
//        }
        adminFeePolRepo.save(adminFeePoliciesList);
        adminFee.setAdminFeeAmt(totalAdminFee);
        adminFee.setVatAmt(totalVatAmt);
        adminFee.setExciseDuty(totalExciseDuty);
        adminFee.setExciseDutyRate(exciseDutyRate);
        adminFee.setVatExciseDutyRate(vatOnExciseDutyRate);
        adminFee.setVatExciseDuty(vatOnExciseDutyRate.multiply(totalExciseDuty).divide(BigDecimal.valueOf(100)));
        adminFee.setAdminNetAmt(totalAdminFee.add(totalVatAmt).add(totalExciseDuty).add(vatOnExciseDutyRate.multiply(totalExciseDuty).divide(BigDecimal.valueOf(100))));
        adminFeeRepo.save(adminFee);

    }

    private boolean checkDuplicate(List<AdminFeePolicies> adminFeePolicies, AdminFeePolicies policyToCheck) {
        return adminFeePolicies.stream().map(a -> a.getPolicy().getPolicyId()).filter(a -> a == policyToCheck.getPolicy().getPolicyId()).count() > 0;
    }


    @Override
    @Transactional(readOnly = true)
    public List<Object[]> getAdminFeePolicies(Long clientId, Long adminFeeId) throws IllegalAccessException {
        return adminFeePolRepo.getAdminFeePolicies(clientId, adminFeeId);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = {BadRequestException.class})
    public void authorizeAdminFee(Long adminFeeId) throws BadRequestException {
        AdminFee adminFee = adminFeeRepo.findOne(adminFeeId);
        if ("Y".equalsIgnoreCase(adminFee.getAuthorised())) {
            throw new BadRequestException("Transaction already authorised");
        }

        if (adminFeePolRepo.count(QAdminFeePolicies.adminFeePolicies.adminFee.adminFeeId.eq(adminFeeId)) == 0)
            throw new BadRequestException("Cannot authorised without admin Fee Policies");

        if (adminFee.getAdminFeeAmt().compareTo(BigDecimal.ZERO) == 0)
            throw new BadRequestException("Cannot authorise the Transaction. Admin Fee Gross Amount is Zero");

        adminFee.setAuthorised("Y");
        adminFee.setAuthorisedBy(userUtils.getCurrentUser());
        adminFee.setAuthDate(new Date());
        adminFeeRepo.save(adminFee);
    }

    @Override
    @Transactional(readOnly = false)
    public void dispatchDocuments(Long polCode) {
        PolicyTrans policyTrans = policyRepo.findOne(polCode);
        boolean medicalProduct = false;
        if (policyTrans.getProduct().getProGroup().getPrgType() == null || !policyTrans.getProduct().getProGroup().getPrgType().equalsIgnoreCase("MD")) {
            medicalProduct = false;
        } else if (policyTrans.getProduct().getProGroup().getPrgType().equalsIgnoreCase("MD")) {
            medicalProduct = true;
        }
        workflowService.completeTask(String.valueOf(polCode), policyTrans, DocType.GEN_UW_DOCUMENT, (medicalProduct) ? "Y" : "N", null, null, null);
    }

    @Override
    @Transactional(readOnly = true)
    public DataTablesResult<RiskDocs> findRiskDocs(DataTablesRequest request, Long riskId) throws IllegalAccessException {
        Long riskIdentifier = riskRepo.getRiskIdentifier(riskId);
        BooleanExpression pred = QRiskDocs.riskDocs.risk.riskIdentifier.eq(-2000L);
        Page<RiskDocs> page = riskDocsRepo.findAll(pred.and(request.searchPredicate(QRiskDocs.riskDocs)), request);
        if(riskIdentifier!=null) {
             pred = QRiskDocs.riskDocs.risk.riskIdentifier.eq(riskIdentifier);
             page = riskDocsRepo.findAll(pred.and(request.searchPredicate(QRiskDocs.riskDocs)), request);
        }
        return new DataTablesResult<>(request, page);
    }

    @Override
    @Transactional(readOnly = true)
    public DataTablesResult<TransChecks> findPolicyChecks(DataTablesRequest request, Long polCode) throws IllegalAccessException {
        BooleanExpression pred = QTransChecks.transChecks.policyTrans.policyId.eq(polCode);
        Page<TransChecks> page = transChecksRepo.findAll(pred.and(request.searchPredicate(QTransChecks.transChecks)), request);
        return new DataTablesResult<>(request, page);
    }

    @Override
    public DataTablesResult<RiskImportationLog> findPolicyImportationLog(DataTablesRequest request, Long policyId) throws IllegalAccessException {
        BooleanExpression pred = QRiskImportationLog.riskImportationLog.policyTrans.policyId.eq(policyId);
        Page<RiskImportationLog> page = importLogRepo.findAll(pred.and(request.searchPredicate(QRiskImportationLog.riskImportationLog)), request);
        return new DataTablesResult<>(request, page);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = {BadRequestException.class})
    public void validateRiskIdFormat(Long subCode, String riskId) throws BadRequestException {
        if (subCode == null)
            throw new BadRequestException("Sub Class Not Selected...");
        SubClassDef subClassDef = subclassRepo.findOne(subCode);
        if (subClassDef == null)
            throw new BadRequestException("Sub Class Not Selected...");
        if (!validatorUtils.validate(riskId, subClassDef.getRiskFormat())) {
            throw new BadRequestException("Enter Valid Plate Number...");
        }
    }


    @Override
    @Transactional(readOnly = false, rollbackFor = {BadRequestException.class})
    public void importExcelRiskTemplate(RiskUploadForm uploadForm) throws BadRequestException {
        try {
            if (uploadForm.getPolCode() == null) throw new BadRequestException("Cannot Upload Risk without Policy....");
            if (uploadForm.getBinderDetails() == null)
                throw new BadRequestException("Cannot Upload....Select Sub Class And Cover to upload....");
            PolicyTrans policyTrans = policyRepo.findOne(uploadForm.getPolCode());
            RiskSections riskSections = importExcelUtils.importRisks(uploadForm.getFile());
            if (riskSections.getRisks().isEmpty())
                throw new BadRequestException("No Risks to import....");
            Set<RiskImportBean> risks = riskSections.getRisks();
            List<RiskTrans> policyRisks = new ArrayList<>();
            List<ScheduleTrans> scheduleTransactions = new ArrayList<>();
            List<SectionTrans> sectionTransactions = new ArrayList<>();
            List<RiskImportationLog> importationLogs = new ArrayList<>();
            for (RiskImportBean riskImportBean : risks) {
                ClientDef client = null;
                if (riskImportBean.getIdNo() == null || StringUtils.isBlank(riskImportBean.getIdNo())) {
                    RiskImportationLog log = new RiskImportationLog();
                    log.setErrorMessage("No Id No For Plate Number " + riskImportBean.getRiskId());
                    log.setPolicyTrans(policyTrans);
                    importationLogs.add(log);
                    continue;
                }
                long clientCount = clientRepo.count(QClientDef.clientDef.idNo.eq(riskImportBean.getIdNo()));
                if (clientCount == 0) {
                    String names = riskImportBean.getInsuredName();
                    StringTokenizer tokenizer = new StringTokenizer(names, " ");
                    Predicate seqPredicate = QSystemSequence.systemSequence.transType.eq("C");
                    if (sequenceRepo.count(seqPredicate) == 0)
                        throw new BadRequestException("Sequence for Client Definition has not been setup");
                    SystemSequence sequence = sequenceRepo.findOne(seqPredicate);
                    Long seqNumber = sequence.getNextNumber();
                    final String clientNumber = sequence.getSeqPrefix() + String.format("%06d", seqNumber);
                    sequence.setLastNumber(seqNumber);
                    sequence.setNextNumber(seqNumber + 1);
                    sequenceRepo.save(sequence);
                    client = new ClientDef();
                    if (tokenizer.hasMoreTokens())
                        client.setFname(tokenizer.nextToken());
                    client.setGender(riskImportBean.getGender());
                    client.setIdNo(riskImportBean.getIdNo());
                    if (tokenizer.hasMoreTokens())
                        client.setOtherNames(tokenizer.nextToken() + ((tokenizer.hasMoreTokens()) ? " " + tokenizer.nextToken() : ""));
                    client.setPhoneNo(riskImportBean.getMobileNumber());
                    client.setPinNo(riskImportBean.getPinNo());
                    client.setRegisteredbrn(policyTrans.getBranch());
                    client.setDateregistered(new Date());
                    long clientTypeCount = clientTypeRepo.count(QClientTypes.clientTypes.clientType.eq("I").and(QClientTypes.clientTypes.typeDesc.containsIgnoreCase("INDIV")));
                    if (clientTypeCount == 1)
                        client.setTenantType(clientTypeRepo.findOne(QClientTypes.clientTypes.clientType.eq("I").and(QClientTypes.clientTypes.typeDesc.containsIgnoreCase("INDIV"))));
                    else {
                        Iterable<ClientTypes> clientTypes = clientTypeRepo.findAll(QClientTypes.clientTypes.clientType.eq("I"));
                        if (clientTypes.spliterator().getExactSizeIfKnown() == 0)
                            throw new BadRequestException("Client Type Not defined in the system...");
                        for (ClientTypes clientType : clientTypes) {
                            client.setTenantType(clientType);
                            break;
                        }
                    }
                    long countryCount = countryRepository.count(QCountry.country.couShtDesc.eq(riskImportBean.getCountry()));
                    if (countryCount == 1)
                        client.setCountry(countryRepository.findOne(QCountry.country.couShtDesc.eq(riskImportBean.getCountry())));
                    client.setDob(riskImportBean.getDateOfBirth());
                    client.setStatus("A");
                    client.setTenantNumber(clientNumber);
                    client = clientRepo.save(client);
                } else if (clientCount == 1) {
                    client = clientRepo.findOne(QClientDef.clientDef.idNo.eq(riskImportBean.getIdNo()));
                } else {
                    RiskImportationLog log = new RiskImportationLog();
                    log.setErrorMessage("More than one Client Exists in the System with ID " + riskImportBean.getIdNo() + " for Risk " + riskImportBean.getRiskId());
                    log.setPolicyTrans(policyTrans);
                    importationLogs.add(log);
                    continue;
                }
                Date polWef = dateUtils.removeTime(policyTrans.getWefDate());
                Date polWet = dateUtils.removeTime(policyTrans.getWetDate());
                Date riskWef = dateUtils.removeTime(riskImportBean.getEffDate());
                Date riskWet = dateUtils.removeTime(riskImportBean.getExpDate());
                if (riskWef.before(polWef) || riskWef.after(polWet)
                        || riskWet.before(polWef) || riskWet.after(polWet)) {
                    RiskImportationLog log = new RiskImportationLog();
                    log.setErrorMessage("Risk WEF and WET outside policy period for Risk " + riskImportBean.getRiskId());
                    log.setPolicyTrans(policyTrans);
                    importationLogs.add(log);
                    continue;
                }
                RiskTrans risk = new RiskTrans();
                risk.setWefDate(riskWef);
                risk.setWetDate(riskWet);
                risk.setCovertype(uploadForm.getBinderDetails().getSubCoverTypes().getCoverTypes());
                risk.setBinderDetails(uploadForm.getBinderDetails());
                risk.setBinder(policyTrans.getBinder());
                risk.setSubclass(uploadForm.getBinderDetails().getSubCoverTypes().getSubclass());
                risk.setAutogenCert("N");
                risk.setCommRate(policyTrans.getAgent().getAccountType().getCommRate());
                risk.setPolicy(policyTrans);
                risk.setProrata("F");
                risk.setRiskDesc(riskImportBean.getRiskdesc());
                risk.setRiskShtDesc(riskImportBean.getRegno());
                risk.setTransType("NB");
                risk.setInsured(client);
                risk.setButchargePrem(riskImportBean.getButCharge());
                riskSections.getSections().stream().filter(a -> a.getRiskId().equals(riskImportBean.getRiskId())).forEach(a -> {
                    long count = setupSectionRepo.count(QSectionsDef.sectionsDef.shtDesc.eq(a.getSectionId()));
                    if (count == 1) {
                        SectionsDef sectiondef = setupSectionRepo.findOne(QSectionsDef.sectionsDef.shtDesc.eq(a.getSectionId()));
                        SectionTrans section = new SectionTrans();
                        List<PremRatesDef> premRates = premRatesRepo.getSectPremiumRates(uploadForm.getBinderDetails().getDetId(), sectiondef.getId());
                        if (premRates.size() == 1) {
                            section.setPremRates(premRates.get(0));
                            section.setRate(BigDecimal.valueOf(a.getRate()));
                            section.setAmount(a.getLimit());
                            section.setCompute(true);
                            section.setDivFactor(BigDecimal.valueOf(a.getDivFactor()));
                            section.setFreeLimit(premRates.get(0).getFreeLimit());
                            section.setCompute(true);
                            section.setSection(sectiondef);
                            section.setRisk(risk);
                            sectionTransactions.add(section);
                        }

                    }
                });

                Long subId = risk.getSubclass().getSubId();
                Long colCount = mappingRepo.count(QScheduleMapping.scheduleMapping.subclass.subId.eq(subId));
                if (colCount == 0) {
                    RiskImportationLog log = new RiskImportationLog();
                    log.setErrorMessage("No Schedule Mapping for Sub Class " + risk.getSubclass().getSubDesc() + " for Plate Number " + riskImportBean.getRiskId());
                    log.setPolicyTrans(policyTrans);
                    importationLogs.add(log);
                    continue;
                }
                Iterable<ScheduleMapping> mappings = mappingRepo.findAll(QScheduleMapping.scheduleMapping.subclass.subId.eq(subId));
                ScheduleTrans scheduleTrans = new ScheduleTrans();
                scheduleTrans.setRisk(risk);
                for (ScheduleMapping mapping : mappings) {
                    if (mapping.getColumnIndex().equalsIgnoreCase("1")) {
                        scheduleTrans.setColumn1(riskImportBean.getColumn1());
                    } else if (mapping.getColumnIndex().equalsIgnoreCase("2")) {
                        scheduleTrans.setColumn2(riskImportBean.getColumn2());
                    } else if (mapping.getColumnIndex().equalsIgnoreCase("3")) {
                        scheduleTrans.setColumn3(riskImportBean.getColumn3());
                    } else if (mapping.getColumnIndex().equalsIgnoreCase("4")) {
                        scheduleTrans.setColumn4(riskImportBean.getColumn4());
                    } else if (mapping.getColumnIndex().equalsIgnoreCase("5")) {
                        scheduleTrans.setColumn5(riskImportBean.getColumn5());
                    } else if (mapping.getColumnIndex().equalsIgnoreCase("6")) {
                        scheduleTrans.setColumn6(riskImportBean.getColumn6());
                    } else if (mapping.getColumnIndex().equalsIgnoreCase("7")) {
                        scheduleTrans.setColumn7(riskImportBean.getColumn7());
                    } else if (mapping.getColumnIndex().equalsIgnoreCase("8")) {
                        scheduleTrans.setColumn8(riskImportBean.getColumn8());
                    } else if (mapping.getColumnIndex().equalsIgnoreCase("9")) {
                        scheduleTrans.setColumn9(riskImportBean.getColumn9());
                    } else if (mapping.getColumnIndex().equalsIgnoreCase("10")) {
                        scheduleTrans.setColumn10(riskImportBean.getColumn10());
                    } else if (mapping.getColumnIndex().equalsIgnoreCase("11")) {
                        scheduleTrans.setColumn11(riskImportBean.getColumn11());
                    } else if (mapping.getColumnIndex().equalsIgnoreCase("12")) {
                        scheduleTrans.setColumn12(riskImportBean.getColumn12());
                    } else if (mapping.getColumnIndex().equalsIgnoreCase("13")) {
                        scheduleTrans.setColumn13(riskImportBean.getColumn13());
                    } else if (mapping.getColumnIndex().equalsIgnoreCase("14")) {
                        scheduleTrans.setColumn14(riskImportBean.getColumn14());
                    } else if (mapping.getColumnIndex().equalsIgnoreCase("15")) {
                        scheduleTrans.setColumn15(riskImportBean.getColumn15());
                    } else if (mapping.getColumnIndex().equalsIgnoreCase("16")) {
                        scheduleTrans.setColumn16(riskImportBean.getColumn16());
                    } else if (mapping.getColumnIndex().equalsIgnoreCase("17")) {
                        scheduleTrans.setColumn17(riskImportBean.getColumn17());
                    } else if (mapping.getColumnIndex().equalsIgnoreCase("18")) {
                        scheduleTrans.setColumn18(riskImportBean.getColumn18());
                    } else if (mapping.getColumnIndex().equalsIgnoreCase("19")) {
                        scheduleTrans.setColumn19(riskImportBean.getColumn19());
                    } else if (mapping.getColumnIndex().equalsIgnoreCase("20")) {
                        scheduleTrans.setColumn20(riskImportBean.getColumn20());
                    }
                }
                scheduleTransactions.add(scheduleTrans);
                policyRisks.add(risk);
            }
            Iterable<RiskTrans> savedRisks = riskRepo.save(policyRisks);
            importLogRepo.save(importationLogs);
            sectionRepo.save(sectionTransactions);
            scheduleTransRepo.save(scheduleTransactions);
            premComputeService.computePrem(uploadForm.getPolCode());
            for (RiskTrans savedRisk : savedRisks) {
                long riskIdentifier = Long.valueOf(String.valueOf(dateUtils.getUwYear(savedRisk.getWefDate())) + String.valueOf(savedRisk.getRiskId()));
                PolicyActiveRisks activeRisk = new PolicyActiveRisks();
                activeRisk.setPolicy(policyTrans);
                activeRisk.setRisk(savedRisk);
                activeRisk.setRiskIdentifier(riskIdentifier);
                activeRisksRepo.save(activeRisk);
                savedRisk.setRiskIdentifier(riskIdentifier);


            }


        } catch (IOException e) {
            throw new BadRequestException("Unable to Upload Excel File...Check the Excel file and try again....");
        }
    }

    @Override
    public void approveException(Long checkId, Long policyId) throws BadRequestException {
        PolicyTrans policy = policyRepo.findOne(policyId);
        TransChecks checks = transChecksRepo.findOne(checkId);
        if (checks.getAuthorised() != null || "Y".equalsIgnoreCase(checks.getAuthorised())) {
            throw new BadRequestException("Exception already authorized...");
        }
        if (!authLimits.checkAuthorizationLimits(checks.getPermission().getPermName())) {
            throw new BadRequestException("You have no rights to Authorize the Exception....");
        }
        checks.setAuthBy(userUtils.getCurrentUser());
        checks.setAuthDate(new Date());
        checks.setAuthorised("Y");
        transChecksRepo.save(checks);
        long count = transChecksRepo.count(QTransChecks.transChecks.policyTrans.policyId.eq(policyId).and((QTransChecks.transChecks.authorised.isNull().or(QTransChecks.transChecks.authorised.eq("N")))));
        if (count == 0) {
            boolean medicalProduct = false;
            if (policy.getProduct().getProGroup().getPrgType() == null || !policy.getProduct().getProGroup().getPrgType().equalsIgnoreCase("MD")) {
                medicalProduct = false;
            } else if (policy.getProduct().getProGroup().getPrgType().equalsIgnoreCase("MD")) {
                medicalProduct = true;
            }

            boolean cashBasis = policy.getInterfaceType() != null && "C".equalsIgnoreCase(policy.getInterfaceType());

            SystemTransactionsTemp trans = systemTransactionsTempRepo.findOne(QSystemTransactionsTemp.systemTransactionsTemp.policy.policyId.eq(policyId));

            if (!cashBasis && trans != null) {
                systemTransactionsTempRepo.delete(trans);
            }

            if (cashBasis) {
                BigDecimal basicPrem = (policy.getPremium() == null) ? BigDecimal.ZERO : policy.getPremium();
                if (trans == null) {
                    trans = new SystemTransactionsTemp();
                    Predicate seqPredicate = QSystemSequence.systemSequence.transType.eq("D");
                    if (sequenceRepo.count(seqPredicate) == 0)
                        throw new BadRequestException("Sequence for Debit Notes has not been defined");
                    SystemSequence sequence = sequenceRepo.findOne(seqPredicate);
                    Long seqNumber = sequence.getNextNumber();
                    String transType = policy.getTransType();
                    if ("NB".equalsIgnoreCase(transType)) {
                        transType = "NB";
                    } else if ("RN".equalsIgnoreCase(transType)) {
                        transType = "RN";
                    } else {
                        transType = "EN";
                    }
                    TransactionMapping mapping = transMappingRepo.findOne(QTransactionMapping.transactionMapping.transType.eq(transType));
                    final String refNo = ((basicPrem.compareTo(BigDecimal.ZERO) >= 0) ? mapping.getDebitCode() : mapping.getCreditCode()) + String.format("%05d", seqNumber);
                    final String debitCode = ((basicPrem.compareTo(BigDecimal.ZERO) >= 0) ? mapping.getDebitCode() : mapping.getCreditCode());
                    sequence.setLastNumber(seqNumber);
                    sequence.setNextNumber(seqNumber + 1);
                    sequenceRepo.save(sequence);
                    trans.setRefNo(refNo);
                    trans.setTransType(debitCode);
                }


                BigDecimal extras = (policy.getExtras() == null) ? BigDecimal.ZERO : policy.getExtras();
                BigDecimal phcf = (policy.getPhcf() == null) ? BigDecimal.ZERO : policy.getPhcf();
                BigDecimal tl = (policy.getTrainingLevy() == null) ? BigDecimal.ZERO : policy.getTrainingLevy();
                BigDecimal sd = (policy.getStampDuty() == null) ? BigDecimal.ZERO : policy.getStampDuty();
                BigDecimal amountWithTaxes = basicPrem.add(extras).add(phcf).add(tl).add(sd);

                if (basicPrem.compareTo(BigDecimal.ZERO) == 1) {
                    String type = (amountWithTaxes.compareTo(BigDecimal.ZERO) == 1) ? "D" : "C";

                    trans.setAmount(basicPrem.abs().multiply(sign(type)).setScale(policy.getTransCurrency().getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
                    trans.setAuthDate(new Date());
                    trans.setAuthorised("Y");
                    trans.setBalance(amountWithTaxes.abs().multiply(sign(type)).setScale(policy.getTransCurrency().getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
                    trans.setBranch(policy.getBranch());
                    trans.setClientType("C");
                    trans.setControlAcc(policy.getClient().getTenantNumber());
                    trans.setClient(policy.getClient());
                    trans.setCurrRate(new BigDecimal(1));
                    trans.setCurrency(policy.getTransCurrency());
                    trans.setNarrations("Posting client Debit Note");
                    trans.setNetAmount(amountWithTaxes.abs().multiply(sign(type)).setScale(policy.getTransCurrency().getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
                    trans.setOrigin("U");
                    trans.setPhfund(phcf.setScale(policy.getTransCurrency().getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
                    trans.setPolicy(policy);
                    trans.setSd(sd.abs().multiply(sign(type)).setScale(policy.getTransCurrency().getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
                    trans.setTl(tl.abs().multiply(sign(type)).setScale(policy.getTransCurrency().getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
                    trans.setTransDate(new Date());
                    trans.setTransdc((amountWithTaxes.compareTo(BigDecimal.ZERO) == 1) ? "D" : "C");
                    trans.setUserAuth(userUtils.getCurrentUser().getUsername());
                    trans.setWhtx(BigDecimal.ZERO);
                    trans.setExtras(extras.abs().multiply(sign(type)).setScale(policy.getTransCurrency().getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
                    trans.setPostedDate(new Date());
                    trans.setPostedUser(userUtils.getCurrentUser());
                    systemTransactionsTempRepo.save(trans);
                }
            }
            Map<String, Object> processVariables = Maps.newHashMap();
            processVariables.put("confirmAuth", true);
            processVariables.put("rejectTrans", false);
            processVariables.put("hasAuthority", true);
            workflowService.completeTask(String.valueOf(policyId), processVariables, policy, DocType.GEN_UW_DOCUMENT, (medicalProduct) ? "Y" : "N", null, null, null);
        }

    }

    @Override
    public List<SubClassReqdDocs> findUnassignedRiskDocs(Long riskId, String docName) throws IllegalAccessException {
        //RiskTrans riskTrans = riskRepo.findOne(riskId);
        List<Object[]> risk = riskRepo.findRiskTrans(riskId);
        //List<RiskTrans> riskTrans = new ArrayList<>();
        RiskTrans riskTrans = new RiskTrans();
        for (Object[] risktrn : risk) {

            if (risktrn[1] instanceof BigInteger) {
                riskTrans.setRiskIdentifier(((BigInteger) risktrn[1]).longValue());
                riskTrans.setPolicy(policyRepo.findOne(((BigInteger) risktrn[2]).longValue()));
                riskTrans.setSubclass(subclassRepo.findOne(((BigInteger) risktrn[3]).longValue()));
            } else if (risktrn[1] instanceof BigDecimal) {
                riskTrans.setRiskIdentifier(((BigDecimal) risktrn[1]).longValue());
                riskTrans.setPolicy(policyRepo.findOne(((BigDecimal) risktrn[2]).longValue()));
                riskTrans.setSubclass(subclassRepo.findOne(((BigDecimal) risktrn[3]).longValue()));

            }

            //riskTrans.add(rsktran);
        }
        PolicyTrans policy = riskTrans.getPolicy();
        String transType = "";
        if (policy.getTransType() == null || StringUtils.isBlank(policy.getTransType())) {
            transType = "NB";
        } else
            transType = policy.getTransType();
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
        List<SubClassReqdDocs> reqdDocses = subclassReqDocRepo.getUnassignedRiskReqDocs(riskTrans.getRiskIdentifier(), riskTrans.getSubclass().getSubId(), docName);

        if ("NB".equalsIgnoreCase(transType)) {
            return reqdDocses.stream().filter(a -> a.getRequiredDoc().isAppliesNewBusiness()).collect(Collectors.toList());
        } else if ("EN".equalsIgnoreCase(transType)) {
            return reqdDocses.stream().filter(a -> a.getRequiredDoc().isAppliesEndorsement()).collect(Collectors.toList());
        } else if ("RN".equalsIgnoreCase(transType)) {
            return reqdDocses.stream().filter(a -> a.getRequiredDoc().isAppliesRenewal()).collect(Collectors.toList());
        } else return new ArrayList<>();
    }

    @Override
    public void createRiskRequiredDocs(RequiredDocBean requiredDocBean) {

        List<Object[]> risk = riskRepo.findRiskTrans(requiredDocBean.getSubCode());
        RiskTrans riskTrans = new RiskTrans();
        for (Object[] risktrn : risk) {
            if (risktrn[1] instanceof BigInteger) {
                riskTrans.setRiskIdentifier(((BigInteger) risktrn[1]).longValue());
                riskTrans.setPolicy(policyRepo.findOne(((BigInteger) risktrn[2]).longValue()));
                riskTrans.setSubclass(subclassRepo.findOne(((BigInteger) risktrn[3]).longValue()));
            } else if (risktrn[1] instanceof BigDecimal) {
                riskTrans.setRiskIdentifier(((BigDecimal) risktrn[1]).longValue());
                riskTrans.setPolicy(policyRepo.findOne(((BigDecimal) risktrn[2]).longValue()));
                riskTrans.setSubclass(subclassRepo.findOne(((BigDecimal) risktrn[3]).longValue()));
            }
        }
        List<RiskDocs> riskDocs =
                requiredDocBean.getRequiredDocs().stream().map(reqId -> {
                    RiskDocs riskDoc = new RiskDocs();
                    riskDoc.setReqdDocs(subclassReqDocRepo.findOne(reqId));
                    //riskDoc.setRisk(riskTrans);
                    riskDoc.setRisk(riskRepo.QueryRiskTrans(requiredDocBean.getSubCode()));
                    return riskDoc;
                }).collect(Collectors.toList());
        riskDocsRepo.save(riskDocs);
    }

    @Override
    public int countPolicies(Long clientCode) throws BadRequestException {
        return 0;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = {BadRequestException.class})
    public void questionnaireCompleted(PolicyTrans policy) throws BadRequestException {
        if ((questionnaireRepo.count(QBinderQuestionnaire.binderQuestionnaire.binder.binId.eq(policy.getBinder().getBinId())) > 0) && (policyQuestionnaireRepo.count(QPolicyQuestionnaire.policyQuestionnaire.policy.policyId.eq(policy.getPolicyId())) <= 0)) {
            throw new BadRequestException("The questionnaire for this haven't been completed");
        }
    }

    @Override
    public void lapsePolicy(Long polCode) throws BadRequestException {
        PolicyTrans policy = policyRepo.findOne(polCode);
        if (policy == null) throw new BadRequestException("No Policy Transaction");
        if (policy.getCurrentStatus() == null)
            throw new BadRequestException("Cannot Lapse a Policy which is not active");

        if (!policy.getCurrentStatus().equalsIgnoreCase("A"))
            throw new BadRequestException("Cannot Lapse a Policy which is not active");
        policy.setCurrentStatus("L");
        policyRepo.save(policy);
    }

    @Override
    public void unLapsePolicy(Long polCode) throws BadRequestException {
        PolicyTrans policy = policyRepo.findOne(polCode);
        if (policy == null) throw new BadRequestException("No Policy Transaction");
        if (policy.getCurrentStatus() == null)
            throw new BadRequestException("Cannot UnLapse a Policy which is not lapsed");

        if (!policy.getCurrentStatus().equalsIgnoreCase("L"))
            throw new BadRequestException("Cannot Lapse a Policy which is not lapsed");
        policy.setCurrentStatus("A");
        policyRepo.save(policy);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = {BadRequestException.class})
    public void savePolicyQuiz(QuestionnaireDTO questionnaireDTO) throws BadRequestException {
        List<PolicyQuestionnaire> polquiz = new ArrayList<>();
        Long policyCode = null;
        for (QuestionnaireBean quiz : questionnaireDTO.getQuizandAnswers()) {
            // if (quiz.getAnswer()!=null) {
            PolicyTrans policyTrans = policyRepo.findOne(questionnaireDTO.getQuizPolicyCode());
            policyCode = policyTrans.getPolicyId();
            BinderQuestionnaire question = binderQuestionnaireRepo.findOne(QBinderQuestionnaire.binderQuestionnaire.binder.binId.eq(policyTrans.getBinder().getBinId())
                    .and(QBinderQuestionnaire.binderQuestionnaire.questionname.equalsIgnoreCase(quiz.getQuestion())));
            PolicyQuestionnaire policyQuestionnaire = new PolicyQuestionnaire();
            policyQuestionnaire.setChoice(quiz.getAnswer().stream().collect(Collectors.joining(",")));
            policyQuestionnaire.setPolicy(policyRepo.findOne(questionnaireDTO.getQuizPolicyCode()));
            policyQuestionnaire.setQuestion(question);
            polquiz.add(policyQuestionnaire);
            System.out.println("quiz=" + quiz.getQuestion() + ";answer=" + quiz.getAnswer() + ";Policy=" + questionnaireDTO.getQuizPolicyCode());
            // }
        }
        deletePolicyQuiz(policyCode);
        policyQuestionnaireRepo.save(polquiz);
        PolicyTrans policyTrans = policyRepo.findOne(policyCode);
        try {
            if (policyTrans.getProduct().getProGroup().getPrgType().equalsIgnoreCase("L")) {
                premiumService.computeLifePrem(policyCode);
            } else {
                premiumService.computePrem(policyCode);
            }


        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }

    }

    @Override
    @Transactional(readOnly = false, rollbackFor = {BadRequestException.class})
    public void deletePolicyQuiz(Long polCode) {
        Iterable<PolicyQuestionnaire> policyQuiz = policyQuestionnaireRepo.findAll(QPolicyQuestionnaire.policyQuestionnaire.policy.policyId.eq(polCode));
        policyQuestionnaireRepo.delete(policyQuiz);
    }

    @Override
    public DataTablesResult<RiskTrans> findEnquiryMaster(DataTablesRequest request, Long polNo, Long riskId, Long idNo) throws IllegalAccessException {
        BooleanExpression pred = QRiskTrans.riskTrans.insured.tenId.eq(idNo).and(QRiskTrans.riskTrans
                .policy.policyId.eq(polNo).and(QRiskTrans.riskTrans.riskId.eq(riskId)));

        Page<RiskTrans> page = riskRepo.findAll(pred, request);
        return new DataTablesResult<>(request, page);
    }

    @Override
    public DataTablesResult<PolicyTrans> masterEnqPI(DataTablesRequest request, Long polNo, Long idNo) {
        BooleanExpression pred = QPolicyTrans.policyTrans.policyId.eq(polNo).and(
                QPolicyTrans.policyTrans.client.tenId.eq(idNo));
        Page<PolicyTrans> page = policyRepo.findAll(pred, request);
        return new DataTablesResult<>(request, page);
    }

    @Override
    public DataTablesResult<RiskTrans> findEnquiryPR(DataTablesRequest request, Long polNo, Long riskId) throws IllegalAccessException {
        BooleanExpression pred = QRiskTrans.riskTrans
                .policy.policyId.eq(polNo).and(QRiskTrans.riskTrans.riskId.eq(riskId));

        Page<RiskTrans> page = riskRepo.findAll(pred, request);
        return new DataTablesResult<>(request, page);
    }

    @Override
    public DataTablesResult<RiskTrans> findEnquiryRI(DataTablesRequest request, Long idNo, Long riskId) {
        BooleanExpression pred = QRiskTrans.riskTrans.insured.tenId.eq(idNo).and(QRiskTrans.riskTrans.riskId.eq(riskId));

        Page<RiskTrans> page = riskRepo.findAll(pred, request);
        return new DataTablesResult<>(request, page);
    }

    @Override
    public DataTablesResult<PolicyTrans> masterEnqPol(DataTablesRequest request, Long polNo) {
        Predicate pred = QPolicyTrans.policyTrans.policyId.eq(polNo);
        Page<PolicyTrans> page = policyRepo.findAll(pred, request);
        return new DataTablesResult<>(request, page);

    }

    @Override
    public DataTablesResult<PolicyTrans> masterEnqIdNo(DataTablesRequest request, Long idNo) {
        BooleanExpression pred = QPolicyTrans.policyTrans.client.tenId.eq(idNo);
        Page<PolicyTrans> page = policyRepo.findAll(pred, request);
        return new DataTablesResult<>(request, page);
    }

    @Override
    public DataTablesResult<RiskTrans> masterEnqRisk(DataTablesRequest request, Long policyId) {
        BooleanExpression pred = QRiskTrans.riskTrans.policy.policyId.eq(policyId);
        Page<RiskTrans> page = riskRepo.findAll(pred, request);
        return new DataTablesResult<>(request, page);
    }

    @Override
    public DataTablesResult<RiskTrans> masterEnqUniqueId(DataTablesRequest pageable, Long riskId) {
        Predicate predicate = QRiskTrans.riskTrans.riskId.eq(riskId);
        Page<RiskTrans> page = riskRepo.findAll(predicate, pageable);
        return new DataTablesResult<>(pageable, page);
    }

    @Override
    public DataTablesResult<ClaimPerils> masterEnqUniqueClaim(DataTablesRequest pageable, Long riskId) {
        BooleanExpression bool = QClaimPerils.claimPerils.claimBookings.risk.riskId.eq(riskId);
        Page<ClaimPerils> page = claimPerilsRepo.findAll(bool, pageable);
        return new DataTablesResult<>(pageable, page);
    }

    @Override
    public DataTablesResult<RiskTrans> masterEnqUniqueRisk(DataTablesRequest pageable, Long riskId) {
        Predicate pred = QRiskTrans.riskTrans.riskId.eq(riskId);
        Page<RiskTrans> page = riskRepo.findAll(pred, pageable);
        return new DataTablesResult<>(pageable, page);
    }

    @Override
    public PolicyTrans findEnquiryId(Long idNo) {

        return policyRepo.findFirstByClient_TenId(idNo);
    }

    @Override
    public PolicyTrans findEnquiryPol(Long polNo) {

        return policyRepo.findFirstByPolicyId(polNo);
    }

    @Override
    public RiskTrans findEnquiryRisk(Long riskId) {

        return riskRepo.findFirstByRiskId(riskId);
    }

    @Override
    public RiskTrans findEnquiryRiskPol(String riskId, String polNo) {

        return riskRepo.findFirstByPolicy_PolNoAndRiskShtDesc(polNo, riskId);

    }

    @Override
    public RiskTrans findEnquiryRiskId(String riskId, Long idNo) {
        return riskRepo.findFirstByInsured_TenIdAndRiskShtDesc(idNo, riskId);
    }

    @Override
    public PolicyTrans findEnquiryPolAndId(String polNo, Long idNo) {

        return policyRepo.findFirstByPolNoAndClient_TenId(polNo, idNo);
    }

    @Override
    public RiskTrans checkAllParam(String polNo, Long idNo, String riskId) {

        return riskRepo.findFirstByInsured_TenIdAndPolicy_PolNoAndRiskShtDesc(idNo, polNo, riskId);
    }

    @Override
    public ClientDef findClient(Long clId) {
        Predicate predicate = QClientDef.clientDef.tenId.eq(clId);

        return clientRepo.findOne(predicate);
    }

    @Override
    public DataTablesResult<ClientDef> masterIdNo(DataTablesRequest pageable, Long idNo) {
        Predicate predicate = QClientDef.clientDef.tenId.eq(idNo);
        Page<ClientDef> page = clientRepo.findAll(predicate, pageable);
        return new DataTablesResult<>(pageable, page);
    }

    @Override
    public Page<ClientDef> findAllClients(String paramString, Pageable pageable) {
        Predicate pred = null;
        if (paramString == null || StringUtils.isBlank(paramString)) {
            pred = QClientDef.clientDef.isNotNull();
        } else {
            pred = QClientDef.clientDef.fname.containsIgnoreCase(paramString).or(QClientDef.clientDef.otherNames.containsIgnoreCase(paramString));
        }
        return clientRepo.findAll(pred, pageable);
    }

    @Override
    public Page<PolicyTrans> findAllPols(String paramString, Pageable pageable) {
        Predicate pred = null;
        if (paramString == null || StringUtils.isBlank(paramString)) {
            pred = QPolicyTrans.policyTrans.isNotNull();
        } else {
            pred = QPolicyTrans.policyTrans.polNo.containsIgnoreCase(paramString);
        }
        return policyRepo.findAll(pred, pageable);
    }

    @Override
    public Page<RiskTrans> allRisksLov(String paramString, Pageable pageable) {
        Predicate pred = null;
        if (paramString == null || StringUtils.isBlank(paramString)) {
            pred = QRiskTrans.riskTrans.isNotNull();
        } else {
            pred = QRiskTrans.riskTrans.riskShtDesc.containsIgnoreCase(paramString);
        }
        return riskRepo.findAll(pred, pageable);
    }

    @Override
    public DataTablesResult<ReceiptTrans> masterReceipts(DataTablesRequest pageable, Long idNo) {
//        BooleanExpression booleanExpression = QReceiptTrans.receiptTrans.client.tenId.eq(idNo);
        BooleanExpression booleanExpression = QReceiptTrans.receiptTrans.receiptDtls.any().policy.client.tenId.eq(idNo);
        Page<ReceiptTrans> page = receiptRepository.findAll(booleanExpression, pageable);
        return new DataTablesResult<>(pageable, page);
    }

    @Override
    public ReceiptTrans getReceiptDetails(Long id) {
        Predicate predicate = QReceiptTrans.receiptTrans.receiptId.eq(id);
        return receiptRepository.findOne(predicate);
    }

    @Override
    public DataTablesResult<ReceiptTransDtls> getReceiptsDets(DataTablesRequest pageable, Long receiptId) {
        BooleanExpression bool = QReceiptTransDtls.receiptTransDtls
                .receipt.receiptId.eq(receiptId);
        Page<ReceiptTransDtls> page = receiptDetailsRepository.findAll(bool, pageable);
        return new DataTablesResult<>(pageable, page);
    }

    @Override
    public ClaimBookings checkClaim(Long claim) {
        Predicate predicate= QClaimBookings.claimBookings.clmId.eq(claim);
        return claimsBookingRepo.findOne(predicate);
    }

    @Override
    public byte[] getPolicyDocument(Long prodCode) throws BadRequestException {
        byte[] arr = new byte[0];
        System.out.println("Product Code "+prodCode);
        if(prodCode==null)
            throw new BadRequestException("Policy Document does not exist...");
        ProductsDef upload = productRepo.findOne(prodCode);
        String uploadFolder = paramService.getParameterString("APP_UPLOAD_FOLDER");
        String folderName = uploadFolder+"/"+upload.getProCode();
        System.out.println("Folder name..."+folderName);
        Path path = Paths.get(folderName+"/"+upload.getProductPolicyDocument());
        if(path.toFile().exists()){
            try {
                arr = Files.readAllBytes(path);
            } catch (IOException e) {
                e.printStackTrace();
                throw new BadRequestException(e.getMessage());
            }
        }
        return arr;
    }

    @Override
    public String getPolicyDocumentType(Long docId) throws BadRequestException {
        if(docId==null)
            throw new BadRequestException("Document does not exist...");
        ProductsDef upload = productRepo.findOne(docId);
        return upload.getContentType();
    }

    @Override
    public DataTablesResult<VehicleDetails> findVehicleDetails(DataTablesRequest request, Long ipuCode) throws IllegalAccessException {
        List<Object[]> motorDetails = motorVehicleDetailsRepo.getRiskVehicleDetails(ipuCode, request.getPageNumber(), request.getPageSize());
        final List<VehicleDetails> vehicleDetailsList = new ArrayList<>();
        long rowCount = 0l;
        if(!motorDetails.isEmpty()) rowCount = (Integer)motorDetails.get(0)[12];
        for(Object[] motorDetail:motorDetails){
            VehicleDetails vehicleDetails = new VehicleDetails();
            vehicleDetails.setBodyColor((String) motorDetail[0]);
            vehicleDetails.setBodyType((String) motorDetail[1]);
            vehicleDetails.setCarMake((String) motorDetail[2]);
            vehicleDetails.setCarModel((String) motorDetail[3]);
            vehicleDetails.setCarryCapacity(((BigDecimal)motorDetail[4]));
            vehicleDetails.setChassisNo((String) motorDetail[5]);
            vehicleDetails.setEngineCapacity(((BigDecimal)motorDetail[6]));
            vehicleDetails.setEngineNumber((String) motorDetail[7]);
            vehicleDetails.setLogbookNumber( (motorDetail[9]!=null)?motorDetail[9].toString():null);
            vehicleDetails.setYearOfManufacture((motorDetail[8]!=null)?motorDetail[8].toString():null);
            vehicleDetails.setRiskId(((BigDecimal)motorDetail[10]).longValue());
            vehicleDetails.setVdId(((BigDecimal)motorDetail[11]).longValue());
            vehicleDetailsList.add(vehicleDetails);
        }
        Page<VehicleDetails>  page = new PageImpl<>(vehicleDetailsList,request, rowCount);
        return new DataTablesResult<>(request, page);
    }


}