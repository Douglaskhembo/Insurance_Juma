package com.brokersystems.brokerapp.setup.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

import com.brokersystems.brokerapp.accounts.dtos.BankBranchDTO;
import com.brokersystems.brokerapp.accounts.model.*;
import com.brokersystems.brokerapp.accounts.repository.BankBranchRepo;
import com.brokersystems.brokerapp.accounts.repository.BanksRepo;
import com.brokersystems.brokerapp.accounts.repository.CoaSubAccountsRepo;
import com.brokersystems.brokerapp.claims.dtos.ClaimantsDTO;
import com.brokersystems.brokerapp.enums.AccountTypeEnum;
import com.brokersystems.brokerapp.kie.rules.AccountsRulesExecutor;
import com.brokersystems.brokerapp.kie.rules.ClientRulesExecutor;
import com.brokersystems.brokerapp.security.DefaultPlatformObjectEncoder;
import com.brokersystems.brokerapp.server.utils.UserUtils;
import com.brokersystems.brokerapp.setup.dto.*;
import com.brokersystems.brokerapp.setup.service.OrganizationService;
import com.brokersystems.brokerapp.trans.model.QReceiptTrans;
import com.brokersystems.brokerapp.trans.model.QTransactionMapping;
import com.brokersystems.brokerapp.trans.model.ReceiptTrans;
import com.brokersystems.brokerapp.trans.model.TransactionMapping;
import com.brokersystems.brokerapp.trans.repository.ReceiptRepository;
import com.brokersystems.brokerapp.trans.repository.TransMappingRepo;
import com.brokersystems.brokerapp.users.model.ModulesDef;
import com.brokersystems.brokerapp.users.model.PermissionsDef;
import com.brokersystems.brokerapp.users.model.QModulesDef;
import com.brokersystems.brokerapp.users.model.QPermissionsDef;
import com.brokersystems.brokerapp.users.repository.ModulesRepo;
import com.brokersystems.brokerapp.users.repository.PermissionsRepo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brokersystems.brokerapp.enums.RevenueItemFinder;
import com.brokersystems.brokerapp.enums.RevenueItems;
import com.brokersystems.brokerapp.server.datatables.DataTablesRequest;
import com.brokersystems.brokerapp.server.datatables.DataTablesResult;
import com.brokersystems.brokerapp.server.exception.BadRequestException;
import com.brokersystems.brokerapp.server.utils.ValidatorUtils;
import com.brokersystems.brokerapp.setup.model.*;
import com.brokersystems.brokerapp.setup.repository.*;
import com.brokersystems.brokerapp.setup.service.ParamService;
import com.brokersystems.brokerapp.setup.service.SetupsService;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;

import javax.sql.DataSource;

@Service
public class SetupsServiceImpl implements SetupsService {

	@Autowired
	private CurrencyRepository currRepo;

	@Autowired
	private ProductReportRepo productReportRepo;

	@Autowired
	private CurrencyExchangeRateRepo exchangeRateRepo;
	@Autowired
	private CountryRepository countryRepo;

	@Autowired
	private CountyRepository countyRepo;

	@Autowired
	private TownRepository townRepo;

	@Autowired
    private ModulesRepo modulesRepo;

	@Autowired
	private OrgBranchRepository branchRepo;

	@Autowired
	private ReceiptRepository receiptRepository;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private PaymentModeRepo payRepo;

	@Autowired
	private AccountTypeRepo acctypeRepo;

	@Autowired
	private SubAccountTypesRepo subAccountTypesRepo;

	@Autowired
	private AccountRepo accountRepo;

	@Autowired
	private ClientRepository clientRepo;
	@Autowired
	private DefaultPlatformObjectEncoder encoder;

	@Autowired
	private SequenceRepository sequenceRepo;

	@Autowired
	private ValidatorUtils validator;

	@Autowired
	private MobPrefixRepo mobileRepo;

	@Autowired
	private BindersRepo bindersRepo;

	@Autowired
	private ClientTypeRepo clientTypeRepo;

	@Autowired
	private ClientTitleRepo clientTitleRepo;

	@Autowired
	private PostalCodeRepo postalRepo;

	@Autowired
	private SectorRepo sectorRepo;

	@Autowired
	private OccupationRepo occupRepo;

	@Autowired
	private TaxRatesRepo taxRatesRepo;

	@Autowired
	private PerilsRepo perilsRepo;

	@Autowired
	private RevenueItemsRepo revenueItemsRepo;

	@Autowired
	private RevenueItemFinder revItemFinder;

	@Autowired
	private ProductGroupRepo prodGroupRepo;

	@Autowired
	private ShortPeriodRepo shortPeriodRepo;

	@Autowired
	private SecShortPeriodsRepo secShortPeriodRepo;

	@Autowired
	private SubSectionRepo subSectionsRepo;

	@Autowired
	private EndorseRemarksRepo endorseRemarksRepo;

	@Autowired
	private SubClassPerilsRepo subClassPerilsRepo;

	@Autowired
	private ClmStatusRepo clmStatusRepo;

	@Autowired
	private TransMappingRepo mappingRepo;

	@Autowired
	private ProductsRepo productsRepo;

	@Autowired
	private CoaSubAccountsRepo subAccountsRepo;

	@Autowired
	private BanksRepo banksRepo;

	@Autowired
	private BankBranchRepo bankBranchRepo;

	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private MobProvidersRepo mobProvidersRepo;

	@Autowired
	private InterestedPartiesRepo interestedPartiesRepo;

	@Autowired
	private UserUtils userUtils;

	@Autowired

	private BusinessSourceGroupsRepo businessSourceGroupsRepo;

	@Autowired
	private BusinessSourcesRepo businessSourcesRepo;

	@Autowired
	private ClientRulesExecutor clientRulesExecutor;

	@Autowired
	private ChecksRepo checksRepo;

	@Autowired
	private PermissionsRepo permissionsRepo;

	@Autowired
	private AccountsRulesExecutor accountsRulesExecutor;

	@Autowired
	private ParamService paramService;

	@Autowired
	private AccountDocsRepo accountDocsRepo;

	@Autowired
	private RequiredDocsRepo requiredDocsRepo;

	@Autowired
	private ProductChecksRepo productChecksRepo;

	@Autowired
	private LifeQuestionsChoicesRepo lifeQuestionsChoicesRepo;

	@Autowired
	private LifeQuestionsRepo lifeQuestionsRepo;

	@Autowired
	private RelationshipTypesRepo relationshipTypesRepo;

	@Autowired
	private SubClassRepo subClassRepo;

	@Autowired
	UnloadedBudgetsRepo unloadedBudgetsRepo;

	@Override
	@Transactional(readOnly = true)
	public DataTablesResult<Currencies> findAllCurrencies(DataTablesRequest request) throws IllegalAccessException {
		Page<Currencies> page = currRepo.findAll(request.searchPredicate(QCurrencies.currencies), request);
		return new DataTablesResult<>(request, page);
	}


	@Override
	@Transactional(readOnly = true)
	public DataTablesResult<BusinessSourceGroups> findAllBusSrcGroups(DataTablesRequest request) throws IllegalAccessException{
		Page<BusinessSourceGroups> page = businessSourceGroupsRepo.findAll(request.searchPredicate(QBusinessSourceGroups.businessSourceGroups),request);
		return  new DataTablesResult<>(request,page);
	}
	@Override
	@Modifying
	@Transactional(readOnly = false)
	public void deleteBusSrcGroup(Long busSrcGrpId ){
		Iterable<BusinessSources> businessSources = businessSourcesRepo.findAll(QBusinessSources.businessSources.businessSourceGroup.srcGroupId.eq(busSrcGrpId));
		for (BusinessSources bizSource:businessSources){
			businessSourcesRepo.delete(bizSource.getSrcId());
		}
		businessSourceGroupsRepo.delete(busSrcGrpId);
	}

	@Override
	@Modifying
	@Transactional(readOnly = false)
	public void deleteBusSrc(Long busSrcId ){
		businessSourcesRepo.delete(busSrcId);
	}

	@Override
	@Modifying
	@Transactional(readOnly = false)
	public void defineBusSrcGroup(BusinessSourceGroups businessSourceGroups) {

		businessSourceGroupsRepo.save(businessSourceGroups);

	}

	@Override
	@Modifying
	@Transactional(readOnly = false)
	public void defineBusSource(BusinessSources businessSources) {

		businessSourcesRepo.save(businessSources);

	}

	@Override
	@Transactional(readOnly = true)
	public DataTablesResult<BusinessSources> findAllBusinessSources(long srcGroupId,DataTablesRequest request) throws IllegalAccessException{
		QBusinessSourceGroups businessSourceGroup = QBusinessSources.businessSources.businessSourceGroup;
		BooleanExpression pred = businessSourceGroup.srcGroupId.eq(srcGroupId);
		Page<BusinessSources> page = businessSourcesRepo.findAll(pred.and(request.searchPredicate(QBusinessSources.businessSources)),request);
		return  new DataTablesResult<>(request,page);
	}

	@Override
	@Transactional(readOnly = true)
	public DataTablesResult<SystemSequence> findAllSequences(DataTablesRequest request) throws IllegalAccessException {
		Page<SystemSequence> page = sequenceRepo.findAll(request.searchPredicate(QSystemSequence.systemSequence),
				request);
		return new DataTablesResult<>(request, page);
	}



	@Override
	@Modifying
	@Transactional(readOnly = false)
	public void defineCurrency(Currencies currency) {

		currRepo.save(currency);

	}

	@Override
	@Modifying
	@Transactional(readOnly = false)
	public void deleteCurrency(Long currCode) {
		currRepo.delete(currCode);

	}

	@Override
	@Transactional(readOnly = true)
	public DataTablesResult<Country> findAllCountries(DataTablesRequest request) throws IllegalAccessException {
		Page<Country> page = countryRepo.findAll(request.searchPredicate(QCountry.country), request);
		return new DataTablesResult<>(request, page);
	}

	@Override
	@Modifying
	@Transactional(readOnly = false)
	public void defineCountry(Country country) {
		countryRepo.save(country);

	}

	@Override
	@Modifying
	@Transactional(readOnly = false)
	public void deleteCountry(Long couCode) {
		countryRepo.delete(couCode);

	}

	@Override
	@Transactional(readOnly = true)
	public DataTablesResult<County> findCountiesByCountry(long couCode, DataTablesRequest request)
			throws IllegalAccessException {
		QCountry country = QCounty.county.country;
		BooleanExpression pred = country.couCode.eq(couCode);
		Page<County> page = countyRepo.findAll(pred.and(request.searchPredicate(QCounty.county)), request);
		return new DataTablesResult(request, page);
	}

	@Override
	@Modifying
	@Transactional(readOnly = false)
	public void defineCounty(County county) {
		countyRepo.save(county);

	}

	@Override
	@Modifying
	@Transactional(readOnly = false)
	public void deleteCounty(Long countyCode) {
		countyRepo.delete(countyCode);

	}

	@Override
	@Transactional(readOnly = true)
	public DataTablesResult<Town> findTownsByCounty(long countyCode, DataTablesRequest request)
			throws IllegalAccessException {
		QCounty county = QTown.town.county;
		BooleanExpression pred = county.countyId.eq(countyCode);
		Page<Town> page = townRepo.findAll(pred.and(request.searchPredicate(QTown.town)), request);
		return new DataTablesResult(request, page);
	}

	@Override
	@Modifying
	@Transactional(readOnly = false)
	public void defineTown(Town town) {
		townRepo.save(town);
	}

	@Override
	@Modifying
	@Transactional(readOnly = false)
	public void deleteTown(Long townCode) {
		townRepo.delete(townCode);
	}


	@Override
	@Transactional(readOnly = true)
	public Page<OrgBranch> findBranchForSelect(String paramString, Pageable paramPageable) {
		Predicate pred = null;
		if (paramString == null || StringUtils.isBlank(paramString)) {
			pred = QOrgBranch.orgBranch.isNotNull();
		} else {
			pred = QOrgBranch.orgBranch.obName.containsIgnoreCase(paramString);
		}
		return branchRepo.findAll(pred, paramPageable);
	}

	@Override
	@Transactional(readOnly = true)
	public DataTablesResult<PaymentModes> findAllPaymentModes(DataTablesRequest request) throws IllegalAccessException {
		Page<PaymentModes> page = payRepo.findAll(request.searchPredicate(QPaymentModes.paymentModes), request);
		return new DataTablesResult<>(request, page);
	}

	@Override
	@Modifying
	@Transactional(readOnly = false)
	public void definePaymentMode(PaymentModes mode) throws BadRequestException {
		if (mode.getPmMaxValue().compareTo(mode.getPmMinValue()) == -1) {
			throw new BadRequestException("Max Value cannot be less than min Value");
		}
		payRepo.save(mode);

	}

	@Override
	@Modifying
	@Transactional(readOnly = false)
	public void deletePaymentMode(Long pmId) {
		payRepo.delete(pmId);

	}

	@Override
	@Transactional(readOnly = true)
	public DataTablesResult<AccountTypes> findAllAccountTypes(DataTablesRequest request) throws IllegalAccessException {
		Page<AccountTypes> page = acctypeRepo.findAll(request.searchPredicate(QAccountTypes.accountTypes), request);
		return new DataTablesResult<>(request, page);
	}

	@Override
	public DataTablesResult<SubAccountTypes> findAllSubAccountTypes(DataTablesRequest request, Long acctypeId) throws IllegalAccessException {
		BooleanExpression pred = QSubAccountTypes.subAccountTypes.accountTypes.accId.eq(acctypeId);
		Page<SubAccountTypes> page = subAccountTypesRepo.findAll(pred.and(request.searchPredicate(QSubAccountTypes.subAccountTypes)), request);
		return new DataTablesResult<>(request, page);
	}

	@Override
	public void defineSubAccountType(SubAccountTypes subAccountTypes) throws BadRequestException {
            if(subAccountTypes.getAccShtDesc()==null){
				throw new BadRequestException("Sub Account Type cannot be null");
			}
		if(subAccountTypes.getAccShtDesc()==null){
			if(subAccountTypesRepo.count(QSubAccountTypes.subAccountTypes.accShtDesc.eq(StringUtils.trim(subAccountTypes.getAccShtDesc()))) > 0)
				throw new BadRequestException("Sub Account Type already exists...");
		}
		subAccountTypesRepo.save(subAccountTypes);
	}

	@Override
	public void deleteSubAccountType(Long subAcctype) {
		subAccountTypesRepo.delete(subAcctype);
	}

	@Override
	@Modifying
	@Transactional(readOnly = false)
	public void defineAccountType(AccountTypes acctypes) throws BadRequestException {
		if(acctypes.getAccountType()==null)
			throw new BadRequestException("Account Type cannot be null");
		//System.out.println("Count..."+acctypeRepo.count(QAccountTypes.accountTypes.accountType.eq(acctypes.getAccountType())));
		if(acctypes.getAccId()==null){
			if(acctypeRepo.count(QAccountTypes.accountTypes.accountType.eq(acctypes.getAccountType())) > 0)
				throw new BadRequestException("Account Type already exists...");
		}
		else{
			if(acctypeRepo.count(QAccountTypes.accountTypes.accountType.eq(acctypes.getAccountType())) > 1)
				throw new BadRequestException("Account Type already exists...");
		}
		if (acctypes.isVatAppli()) {
			if (acctypes.getVatRate() == null || acctypes.getVatRate().compareTo(BigDecimal.ZERO) <= 0) {
				throw new BadRequestException("Vat Rate cannot be zero or less than zero if Vat is applicable");
			}
			if (acctypes.getVatRate().compareTo(new BigDecimal(100)) == 1) {
				throw new BadRequestException("VAT Rate cannot cannot be greater than 100");
			}
		}
		if (acctypes.isWhtxAppl()) {
			if (acctypes.getWhtaxVal() == null || acctypes.getWhtaxVal().compareTo(BigDecimal.ZERO) <= 0) {
				throw new BadRequestException("Whtx Rate cannot be zero or less than zero if Whtx is applicable");
			}
			if (acctypes.getWhtaxVal().compareTo(new BigDecimal(100)) == 1) {
				throw new BadRequestException("Whtx Rate cannot cannot be greater than 100");
			}
		}
		acctypeRepo.save(acctypes);

	}

	@Override
	@Modifying
	@Transactional(readOnly = false)
	public void deleteAccountType(Long acctId) {
		acctypeRepo.delete(acctId);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<AccountTypesDTO> findAccountTypesforSelect(String searchValue, Pageable paramPageable) {
		if(searchValue==null) searchValue="%%";
		else searchValue = "%"+searchValue+"%";
		List<Object[]> accountTypesList = acctypeRepo.findAllAccountTypes(searchValue, paramPageable.getPageNumber(), paramPageable.getPageSize());
		long rowCount = 0L;
		if(!accountTypesList.isEmpty()) rowCount = (Integer)accountTypesList.get(0)[3];
		List<AccountTypesDTO> accountTypes = new ArrayList<>();
		for(Object[] accountType:accountTypesList){
			AccountTypesDTO accountTypesDTO = new AccountTypesDTO();
			accountTypesDTO.setAccId(((BigDecimal)accountType[0]).longValue());
			accountTypesDTO.setAccName((String) accountType[1]);
			accountTypesDTO.setAccountType((String) accountType[2]);
			accountTypes.add(accountTypesDTO);
		}
		return new PageImpl<>(accountTypes,paramPageable, rowCount);
	}

	@Override
	public void approveAccount(Long acctId) throws BadRequestException {
		final AccountDef accountDef = accountRepo.findOne(acctId);
		if(accountDef.getStatus()==null && !"D".equalsIgnoreCase(accountDef.getStatus())){
			throw new BadRequestException("Intermediary should be draft status to be approved...");
		}
		accountDef.setStatus("A");
		accountDef.setWef(new Date());
		accountDef.setActivatedUser(userUtils.getCurrentUser());
		accountRepo.save(accountDef);
	}

	@Override
	public void rejectAccount(Long acctId) throws BadRequestException {
		final AccountDef accountDef = accountRepo.findOne(acctId);
		if(accountDef.getStatus()==null && !"D".equalsIgnoreCase(accountDef.getStatus())){
			throw new BadRequestException("Intermediary should be draft status to be Rejected...");
		}
		accountDef.setStatus("NA");
		accountDef.setWef(new Date());
		accountDef.setActivatedUser(userUtils.getCurrentUser());
		accountRepo.save(accountDef);
	}

	@Override
	public void deactivateAccount(Long acctId) throws BadRequestException {
		final AccountDef accountDef = accountRepo.findOne(acctId);
		if(accountDef.getStatus()==null && !("A".equalsIgnoreCase(accountDef.getStatus()) || "DA".equalsIgnoreCase(accountDef.getStatus()))){
			throw new BadRequestException("Intermediary should be active status to be deactivated...");
		}
		if(accountDef.getStatus().equalsIgnoreCase("A")){
			accountDef.setWet(new Date());
			accountDef.setStatus("DA");
		}
		else {
			accountDef.setWef(new Date());
			accountDef.setStatus("D");
		}

		accountDef.setDeActivatedUser(userUtils.getCurrentUser());
		accountRepo.save(accountDef);
	}

	@Override
	public Page<AccountsDTO> findParentAccountsforSelect(String searchValue, Pageable paramPageable) {
		if(searchValue==null) searchValue="%%";
		else searchValue = "%"+searchValue+"%";
		List<Object[]> accountTypesList = accountRepo.findParentAccountTypes(searchValue, paramPageable.getPageNumber(), paramPageable.getPageSize());
		long rowCount = 0L;
		if(!accountTypesList.isEmpty()) rowCount = ((BigInteger)accountTypesList.get(0)[2]).intValue();
		List<AccountsDTO> accountTypes = new ArrayList<>();
		for(Object[] accountType:accountTypesList){
			AccountsDTO accounts = new AccountsDTO();
			accounts.setAcctId(((BigInteger)accountType[0]).longValue());
			accounts.setName((String) accountType[1]);
			accountTypes.add(accounts);
		}
		return new PageImpl<>(accountTypes,paramPageable, rowCount);
	}

	@Override
	public Page<AccountsDTO> findReinsuranceforSelect(String searchValue, Pageable paramPageable) {
		if(searchValue==null) searchValue="%%";
		else searchValue = "%"+searchValue+"%";
		List<Object[]> accountTypesList = accountRepo.findReinsuranceAccountTypes(searchValue, paramPageable.getPageNumber(), paramPageable.getPageSize());
		long rowCount = 0L;
		if(!accountTypesList.isEmpty()) rowCount = (Integer)accountTypesList.get(0)[2];
		List<AccountsDTO> accountTypes = new ArrayList<>();
		for(Object[] accountType:accountTypesList){
			AccountsDTO accounts = new AccountsDTO();
			accounts.setAcctId(((BigDecimal)accountType[0]).longValue());
			accounts.setName((String) accountType[1]);
			accountTypes.add(accounts);
		}
		return new PageImpl<>(accountTypes,paramPageable, rowCount);
	}

	@Override
	public Page<AccountsDTO> findBrokersforSelect(String searchValue, Pageable paramPageable) {
		if(searchValue==null) searchValue="%%";
		else searchValue = "%"+searchValue+"%";
		List<Object[]> accountTypesList = accountRepo.findBrokerAccountTypes(searchValue, paramPageable.getPageNumber(), paramPageable.getPageSize());
		long rowCount = 0L;
		if(!accountTypesList.isEmpty()) rowCount = (Integer)accountTypesList.get(0)[2];
		List<AccountsDTO> accountTypes = new ArrayList<>();
		for(Object[] accountType:accountTypesList){
			AccountsDTO accounts = new AccountsDTO();
			accounts.setAcctId(((BigDecimal)accountType[0]).longValue());
			accounts.setName((String) accountType[1]);
			accountTypes.add(accounts);
		}
		return new PageImpl<>(accountTypes,paramPageable, rowCount);
	}

	@Override
	@Transactional(readOnly = true)
	public DataTablesResult<AccountsDTO> findAllAccounts(DataTablesRequest request) {
		final String search = ( request.getSearch()!=null && request.getSearch().getValue()!=null)?"%"+request.getSearch().getValue().toLowerCase()+"%":"%%";
		List<Object[]> allAccounts = accountRepo.findAllAccounts(search, request.getPageNumber(), request.getPageSize());
		final List<AccountsDTO> accountsList = new ArrayList<>();
		long rowCount = 0L;
		if(!allAccounts.isEmpty()) rowCount = ((BigInteger)allAccounts.get(0)[10]).intValue();
		for(Object[] accounts:allAccounts){
			AccountsDTO account = new AccountsDTO();
			account.setAcctId(((BigInteger)accounts[0]).longValue());
			account.setShtDesc((String)accounts[1]);
			account.setName((String)accounts[2]);
			account.setPhoneNo((String)accounts[3]);
			account.setPinNo((String)accounts[4]);
			account.setStatus((String)accounts[5]);
			account.setEmail((String)accounts[6]);
			account.setCreatedBy((String)accounts[7]);
			account.setUpdatedBy((String)accounts[8]);
			account.setAccountTypeName((String)accounts[9]);
			accountsList.add(account);
		}
		Page<AccountsDTO>  page = new PageImpl<>(accountsList,request, rowCount);
		return new DataTablesResult<>(request, page);
	}

	@Override
	@Modifying
	@Transactional(readOnly = false)
	public AccountsDTO getAccountDetails(Long acctId) {
		List<Object[]> allAccounts = accountRepo.findOneAccount(acctId);
		if(allAccounts.isEmpty()) return null;
		Object[] accounts = allAccounts.get(0);
		final AccountsDTO accountsDTO = new AccountsDTO();
		accountsDTO.setAcctId(((BigDecimal)accounts[0]).longValue());
		accountsDTO.setCreatedBy((String) accounts[1]);
		accountsDTO.setUpdatedBy((String) accounts[3]);
		accountsDTO.setAddress((String) accounts[5]);
		accountsDTO.setBankAccount((String) accounts[6]);
		accountsDTO.setContactPerson((String) accounts[7]);
		accountsDTO.setContactTitle((String) accounts[8]);
		accountsDTO.setDob((Date) accounts[9]);
		accountsDTO.setEmail((String) accounts[10]);
		accountsDTO.setLicenseNumber((String) accounts[11]);
		accountsDTO.setName((String) accounts[12]);
		accountsDTO.setPayTelNo((String) accounts[13]);
		accountsDTO.setPaybillNumber((String) accounts[14]);
		accountsDTO.setPhoneNo((String) accounts[15]);
		accountsDTO.setPhysaddress((String) accounts[16]);
		accountsDTO.setPinNo((String) accounts[17]);
		accountsDTO.setShtDesc((String) accounts[18]);
		accountsDTO.setStatus((String) accounts[19]);
		accountsDTO.setWef((Date) accounts[20]);
		accountsDTO.setWet((Date) accounts[21]);
		accountsDTO.setAcctTypeId(((BigDecimal)accounts[22]).longValue());
		if(accounts[23]!=null){
			accountsDTO.setBankBranchId(((BigDecimal)accounts[23]).longValue());
		}
		if(accounts[24]!=null){
			accountsDTO.setBankId(((BigDecimal)accounts[24]).longValue());
		}
		if(accounts[25]!=null){
			accountsDTO.setBranchId(((BigDecimal)accounts[25]).longValue());
		}
		if(accounts[26]!=null){
			accountsDTO.setPaymentModeId(((BigDecimal)accounts[26]).longValue());
		}
		accountsDTO.setLogo((String) accounts[27]);
		if(accounts[28]!=null){
			accountsDTO.setParentAcctId(((BigDecimal)accounts[28]).longValue());
		}
		accountsDTO.setBankName((String) accounts[29]);
		accountsDTO.setBankBranchName((String) accounts[30]);
		accountsDTO.setPaymentMode((String) accounts[31]);
		accountsDTO.setBranchName((String) accounts[32]);
		accountsDTO.setAccountTypeName((String) accounts[33]);
		accountsDTO.setAccountTypeId((String) accounts[34]);
		accountsDTO.setParentAcctName((String) accounts[35]);
		return accountsDTO;
	}

	@Override
	@Modifying
	@Transactional(readOnly = false)
	public Long defineAccount(AccountsDTO accounts) throws BadRequestException {
		AccountDef account = new AccountDef();
		BeanUtils.copyProperties(accounts,account);
		if (accounts.getAcctTypeId() == null) {
			throw new BadRequestException("Select Account Type to continue...");
		}

		if (null == accounts.getStatus() || "".equals(accounts.getStatus())) {
			if (account.getAcctId() != null) {
				AccountDef savedAccount = accountRepo.findOne(accounts.getAcctId());
				if(savedAccount.getStatus()!=null){
					account.setStatus(savedAccount.getStatus());
				}
			} else {
				account.setStatus("D");
			}
		}
		final AccountTypes accountTypes = acctypeRepo.findOne(accounts.getAcctTypeId());
		if(accountTypes==null){
			throw new BadRequestException("Select a Valid Account Type to continue...");
		}
		if(accountTypes.getAccountType()==AccountTypeEnum.IA || accountTypes.getAccountType()==AccountTypeEnum.SUB){
			if(accounts.getWef()==null)
				throw new BadRequestException("Wef Date is Mandatory for In House Agents and Sub Agents");

			if(accounts.getWet()!=null){
				if(accounts.getWet().before(accounts.getWef()))
					throw new BadRequestException("Wef Date cannot be greater than Wet Date");
			}
		}
       if(accounts.getAcctId()==null) {
		   Predicate seqPredicate = QSystemSequence.systemSequence.transType.eq("A");
		   if (sequenceRepo.count(seqPredicate) == 0)
			   throw new BadRequestException("Sequence for Intermediaries Definition has not been setup");
		   SystemSequence sequence = sequenceRepo.findOne(seqPredicate);
		   Long seqNumber = sequence.getNextNumber();
		   final String accountNumber = sequence.getSeqPrefix() + String.format("%03d", seqNumber);
		   sequence.setLastNumber(seqNumber);
		   sequence.setNextNumber(seqNumber + 1);
		   sequenceRepo.save(sequence);
		   account.setShtDesc(accountNumber);
		   account.setWef(null);
	   }

		account.setPinNo(StringUtils.upperCase(accounts.getPinNo()));
		account.setStatus("D");
		account.setPhoneNo(accounts.getPhoneNo());
		account.setAccountType(accountTypes);
		if(accounts.getParentAcctId()!=null){
			final AccountDef accountDef = accountRepo.findOne(accounts.getParentAcctId());
			if(accountDef==null){
				throw new BadRequestException("Invalid Parent Account Selected...");
			}
			account.setParentAccount(accountDef);
		}
		if(accounts.getBankId()!=null){
			final Banks banks = banksRepo.findOne(accounts.getBankId());
			if(banks==null){
				throw new BadRequestException("Invalid Bank Selected...");
			}
			account.setBanks(banks);
		}
		if(accounts.getBankBranchId()!=null){
			final BankBranches bankBranches = bankBranchRepo.findOne(accounts.getBankBranchId());
			if(bankBranches==null){
				throw new BadRequestException("Invalid Bank Branch Selected...");
			}
			account.setBankBranches(bankBranches);
			if(accounts.getBankAccount()==null){
				throw new BadRequestException("Bank Account number is mandatory..");
			}
		}
		if(accounts.getBranchId()!=null){
			final OrgBranch branch = branchRepo.findOne(accounts.getBranchId());
			if(branch==null){
				throw new BadRequestException("Invalid  Branch Selected...");
			}
			account.setBranch(branch);
		}
		if(accounts.getPaymentModeId()!=null){
			final PaymentModes paymentModes = payRepo.findOne(accounts.getPaymentModeId());
			if(paymentModes==null){
				throw new BadRequestException("Invalid  Payment  Mode Selected...");
			}
			account.setPaymentMode(paymentModes);
		}
		accountsRulesExecutor.handleAccountChecks(account);
		AccountDef saved = accountRepo.save(account);
		return saved.getAcctId();
	}

	@Override
	@Modifying
	@Transactional(readOnly = false)
	public void deleteAccount(Long acctId) {
		accountRepo.delete(acctId);

	}



	@Override
	@Modifying
	@Transactional(readOnly = false)
	public void defineSequence(SystemSequence sequence) throws BadRequestException {
		Predicate pred = QSystemSequence.systemSequence.transType.eq(sequence.getTransType());
		if(sequence.getSeqId()==null)
		if (sequenceRepo.count(pred) > 0) {
			throw new BadRequestException("The Sequence for the transaction already exists..");
		}
		sequenceRepo.save(sequence);

	}

	@Override
	@Modifying
	@Transactional(readOnly = false)
	public void deleteSequence(Long seqCode) {
		sequenceRepo.delete(seqCode);

	}



	@Override
	@Modifying
	@Transactional(readOnly = false)
	public String defineClient(ClientDTO clientDef) throws BadRequestException {
		ClientDef client = new ClientDef();
		BeanUtils.copyProperties(clientDef,client);
		if(client.getClientRef()==null) {
			clientRulesExecutor.handleClientChecks(client);
		}
		validator.validatePassport(client.getPassportNo());
		if(client.getPinNo()!=null){
			validator.validatePinNo(client.getPinNo());
		}
		if(clientDef.getClientTypeId()==null){
			throw new BadRequestException("Please select Client Type to continue..");
		}
		client.setTenantType(clientTypeRepo.findOne(clientDef.getClientTypeId()));
		if(clientDef.getCtCode()!=null){
			client.setTown(townRepo.findOne(clientDef.getCtCode()));
		}
		if(clientDef.getCouCode()==null){
			throw new BadRequestException("Select Country to continue...");
		}
		client.setCountry(countryRepo.findOne(clientDef.getCouCode()));
		if(clientDef.getSmsPrefixId()!=null){
			client.setSmsPrefix(mobileRepo.findOne(clientDef.getSmsPrefixId()));
		}
		if(clientDef.getPhonePrefixId()!=null){
			client.setPhonePrefix(mobileRepo.findOne(clientDef.getPhonePrefixId()));
		}
		if(clientDef.getSectCode()!=null){
			client.setClientSector(sectorRepo.findOne(clientDef.getSectCode()));
		}
		if(clientDef.getOccCode()!=null){
			client.setOccupation(occupRepo.findOne(clientDef.getOccCode()));
		}
		if(clientDef.getTitleId()!=null){
			client.setClientTitle(clientTitleRepo.findOne(clientDef.getTitleId()));
		}
		if(client.getTenId()==null) {
			client.setDateCreated(new Date());
			client.setCreatedBy(userUtils.getCurrentUser());
		}
		else {
			ClientDef existingClient = clientRepo.findOne(client.getTenId());
			if(client.getDateCreated()!=null)
			client.setDateCreated(client.getDateCreated());
			else client.setDateCreated(new Date());
			client.setClientRef(existingClient.getClientRef());
			if(StringUtils.isBlank(existingClient.getClientHash())){
				client.setClientHash(encoder.encodeExistingClient(client));
			}
			client.setClientHash(existingClient.getClientHash());

			if(client.getCreatedBy()!=null){
				client.setCreatedBy(client.getCreatedBy());
			}
			else client.setCreatedBy(userUtils.getCurrentUser());
		}

//		if(!client.getSmsNumber().matches("^[0-9]{6}$")){
//			throw new BadRequestException("Invalid Sms Number,should be exactly six digits only");
//		}
//
//		if(!client.getPhoneNo().matches("^[0-9]{6}$")){
//			throw new BadRequestException("Invalid Phone Number,should be exactly six digits only");
//		}
//		if(client.getAddress()!=null && !StringUtils.startsWith(StringUtils.upperCase(client.getAddress()),"P.O. Box".toUpperCase()) ){
//			throw new BadRequestException("Postal Address is invalid.");
//		}
//		if(client.getTenId()==null) {
//			if (client.getIdNo() != null) {
//				if (clientRepo.count(QClientDef.clientDef.idNo.eq(client.getIdNo())) > 0) {
//                    throw new BadRequestException("Client With ID Number already exists...");
//				}
//			}
//			if (client.getPinNo() != null) {
//				if (clientRepo.count(QClientDef.clientDef.pinNo.eq(client.getPinNo())) > 0) {
//					throw new BadRequestException("Client With Pin Number already exists...");
//				}
//			}
//			if (client.getPassportNo()!=null && !StringUtils.isBlank(client.getPassportNo())) {
//				if (clientRepo.count(QClientDef.clientDef.passportNo.eq(client.getPassportNo())) > 0) {
//					long cnt = clientRepo.count(QClientDef.clientDef.passportNo.eq(client.getPassportNo()));
//					throw new BadRequestException("Client With Passport Number: "+client.getPassportNo()+" already exists..."+cnt+" times");
//				}
//			}
//		}
//		else {
//			if (client.getIdNo() != null) {
//				if (clientRepo.count(QClientDef.clientDef.idNo.eq(client.getIdNo())) > 1) {
//					throw new BadRequestException("Client With ID Number already exists...");
//				}
//			}
//			if (client.getPinNo() != null) {
//				if (clientRepo.count(QClientDef.clientDef.pinNo.eq(client.getPinNo())) > 1) {
//					throw new BadRequestException("Client With Pin Number already exists...");
//				}
//			}
//			if (!StringUtils.isBlank(client.getPassportNo())) {
//				if (clientRepo.count(QClientDef.clientDef.passportNo.eq(client.getPassportNo())) > 1) {
//					throw new BadRequestException("Client With Passport Number already exists...");
//				}
//			}
//		}

		if(client.getTenId()==null){
			if(client.getDateregistered()==null){
				throw new BadRequestException("Enter Date Registered");
			}
			if (client.getIdNo() != null) {
				if (clientRepo.count(QClientDef.clientDef.idNo.eq(client.getIdNo())) > 1) {
					throw new BadRequestException("Client With ID Number already exists...");
				}
			}
			if (client.getPinNo() != null) {
				if (clientRepo.count(QClientDef.clientDef.pinNo.eq(client.getPinNo())) > 1) {
					throw new BadRequestException("Client With Pin Number already exists...");
				}
			}
			if (!StringUtils.isBlank(client.getPassportNo())) {
				if (clientRepo.count(QClientDef.clientDef.passportNo.eq(client.getPassportNo())) > 1) {
					throw new BadRequestException("Client With Passport Number already exists...");
				}
			}
			Predicate seqPredicate = QSystemSequence.systemSequence.transType.eq("C");
			if (sequenceRepo.count(seqPredicate) == 0)
				throw new BadRequestException("Sequence for Client Definition has not been setup");
		SystemSequence sequence = sequenceRepo.findOne(seqPredicate);
		Long seqNumber = sequence.getNextNumber();
		final String clientNumber = sequence.getSeqPrefix() + String.format("%06d", seqNumber);
		sequence.setLastNumber(seqNumber);
		sequence.setNextNumber(seqNumber + 1);
		sequenceRepo.save(sequence);
		client.setStatus("A");

		if(clientDef.getObId()!=null){
			client.setRegisteredbrn(branchRepo.findOne(clientDef.getObId()));
		}
		client.setTenantNumber(clientNumber);
		client.setClientHash(encoder.encodeExistingClient(client));
		}else {
			ClientDef existingClient = clientRepo.findOne(client.getTenId());
			if (client.getStatus()!=null ){
				if (client.getStatus()!=existingClient.getStatus()&& "T".equalsIgnoreCase(client.getStatus()) ){
					client.setDateterminated(new Date());
				}else {
					if (client.getStatus()!=existingClient.getStatus()&& !("T".equalsIgnoreCase(client.getStatus())) ){
						client.setDateterminated(null);
					}
				}


			}
		}

		if(client.getOtherNames()==null || StringUtils.isBlank(client.getOtherNames()))
			client.setOtherNames(" ");
		client.setFname(StringUtils.upperCase(client.getFname()));
		client.setOtherNames(StringUtils.upperCase(client.getOtherNames()));
		client.setPassportNo(StringUtils.upperCase(client.getPassportNo()));
		client.setPinNo(StringUtils.upperCase(client.getPinNo()));
		if(client.getTenantType()!=null && client.getTenantType().getClientType().equalsIgnoreCase("I")){
			if(StringUtils.isBlank(client.getPhoneNo())){
				throw new BadRequestException("Phone Number is Mandatory for individual clients..");
			}
			if(StringUtils.isBlank(client.getEmailAddress())){
				throw new BadRequestException("Email Address is Mandatory for individual clients..");
			}
		}
		clientRulesExecutor.handleClientChecks(client);
		ClientDef savedClient = clientRepo.save(client);
		return savedClient.getClientHash();
	}


	@Override
	public ClientDTO getClientDetailsByHash(String hash) {
		List<Object[]> clientDetails = clientRepo.findClientDetailsByHash(hash);
		if(clientDetails.isEmpty()) return null;
		Object[] dtls = clientDetails.get(0);
		ClientDTO clientDTO = new ClientDTO();
		clientDTO.setTenId(((BigDecimal)dtls[0]).longValue());
		clientDTO.setAddress((String) dtls[1]);
		clientDTO.setAuthStatus((String) dtls[2]);
		clientDTO.setClientRef((String) dtls[3]);
		clientDTO.setComment((String) dtls[4]);
		clientDTO.setDateCreated((Date) dtls[5]);
		clientDTO.setDateregistered((Date) dtls[6]);
		clientDTO.setDateterminated((Date) dtls[7]);
		clientDTO.setDob((Date) dtls[8]);
		clientDTO.setEmailAddress((String) dtls[9]);
		clientDTO.setFname((String) dtls[10]);
		clientDTO.setGender((String) dtls[11]);
		clientDTO.setIdNo((String) dtls[12]);
		clientDTO.setOfficeTel((String) dtls[13]);
		clientDTO.setOtherNames((String) dtls[14]);
		clientDTO.setPassportNo((String) dtls[15]);
		clientDTO.setPhoneNo((String) dtls[16]);
		clientDTO.setPinNo((String) dtls[17]);
		clientDTO.setSmsNumber((String) dtls[18]);
		clientDTO.setStatus((String) dtls[19]);
		clientDTO.setTenantNumber((String) dtls[20]);
		if(dtls[22]!=null) {
			clientDTO.setSectCode(((BigDecimal) dtls[22]).longValue());
		}
		if(dtls[23]!=null) {
			clientDTO.setTitleId(((BigDecimal) dtls[23]).longValue());
		}
		if(dtls[24]!=null) {
			clientDTO.setCouCode(((BigDecimal) dtls[24]).longValue());
		}
		if(dtls[26]!=null) {
			clientDTO.setOccCode(((BigDecimal) dtls[26]).longValue());
		}
		if(dtls[27]!=null) {
			clientDTO.setPhonePrefixId(((BigDecimal) dtls[27]).longValue());
		}
		if(dtls[28]!=null) {
			clientDTO.setPcode(((BigDecimal) dtls[28]).longValue());
		}
		if(dtls[29]!=null) {
			clientDTO.setObId(((BigDecimal) dtls[29]).longValue());
		}
		if(dtls[30]!=null) {
			clientDTO.setSmsPrefixId(((BigDecimal) dtls[30]).longValue());
		}
		if(dtls[31]!=null) {
			clientDTO.setClientTypeId(((BigDecimal) dtls[31]).longValue());
		}
		if(dtls[32]!=null) {
			clientDTO.setCtCode(((BigDecimal) dtls[32]).longValue());
		}
		clientDTO.setPhotoUrl((String) dtls[33]);
		clientDTO.setObName((String) dtls[34]);
		clientDTO.setCtName((String) dtls[35]);
		clientDTO.setPhonePrefixName((String) dtls[36]);
		clientDTO.setSmsPrefixName((String) dtls[37]);
		clientDTO.setClientType((String) dtls[38]);
		clientDTO.setOccName((String) dtls[39]);
		clientDTO.setCouName((String) dtls[40]);
		clientDTO.setTitleName((String) dtls[41]);
		clientDTO.setSectName((String) dtls[42]);
		clientDTO.setPostalName((String) dtls[43]);
		clientDTO.setClientTypeDesc((String) dtls[44]);
		clientDTO.setHashCode((String) dtls[45]);
		return clientDTO;
	}

	@Override
	@Transactional(readOnly = true)
	public ClientDTO getClientDetails(Long tenId) {
		List<Object[]> clientDetails = clientRepo.findClientDetails(tenId);
		if(clientDetails.isEmpty()) return null;
		Object[] dtls = clientDetails.get(0);
		ClientDTO clientDTO = new ClientDTO();
		clientDTO.setTenId(((BigDecimal)dtls[0]).longValue());
		clientDTO.setAddress((String) dtls[1]);
		clientDTO.setAuthStatus((String) dtls[2]);
		clientDTO.setClientRef((String) dtls[3]);
		clientDTO.setComment((String) dtls[4]);
		clientDTO.setDateCreated((Date) dtls[5]);
		clientDTO.setDateregistered((Date) dtls[6]);
		clientDTO.setDateterminated((Date) dtls[7]);
		clientDTO.setDob((Date) dtls[8]);
		clientDTO.setEmailAddress((String) dtls[9]);
		clientDTO.setFname((String) dtls[10]);
		clientDTO.setGender((String) dtls[11]);
		clientDTO.setIdNo((String) dtls[12]);
		clientDTO.setOfficeTel((String) dtls[13]);
		clientDTO.setOtherNames((String) dtls[14]);
		clientDTO.setPassportNo((String) dtls[15]);
		clientDTO.setPhoneNo((String) dtls[16]);
		clientDTO.setPinNo((String) dtls[17]);
		clientDTO.setSmsNumber((String) dtls[18]);
		clientDTO.setStatus((String) dtls[19]);
		clientDTO.setTenantNumber((String) dtls[20]);
		if(dtls[22]!=null) {
			clientDTO.setSectCode(((BigDecimal) dtls[22]).longValue());
		}
		if(dtls[23]!=null) {
			clientDTO.setTitleId(((BigDecimal) dtls[23]).longValue());
		}
		if(dtls[24]!=null) {
			clientDTO.setCouCode(((BigDecimal) dtls[24]).longValue());
		}
		if(dtls[26]!=null) {
			clientDTO.setOccCode(((BigDecimal) dtls[26]).longValue());
		}
		if(dtls[27]!=null) {
			clientDTO.setPhonePrefixId(((BigDecimal) dtls[27]).longValue());
		}
		if(dtls[28]!=null) {
			clientDTO.setPcode(((BigDecimal) dtls[28]).longValue());
		}
		if(dtls[29]!=null) {
			clientDTO.setObId(((BigDecimal) dtls[29]).longValue());
		}
		if(dtls[30]!=null) {
			clientDTO.setSmsPrefixId(((BigDecimal) dtls[30]).longValue());
		}
		if(dtls[31]!=null) {
			clientDTO.setClientTypeId(((BigDecimal) dtls[31]).longValue());
		}
		if(dtls[32]!=null) {
			clientDTO.setCtCode(((BigDecimal) dtls[32]).longValue());
		}
		clientDTO.setPhotoUrl((String) dtls[33]);
		clientDTO.setObName((String) dtls[34]);
		clientDTO.setCtName((String) dtls[35]);
		clientDTO.setPhonePrefixName((String) dtls[36]);
		clientDTO.setSmsPrefixName((String) dtls[37]);
		clientDTO.setClientType((String) dtls[38]);
		clientDTO.setOccName((String) dtls[39]);
		clientDTO.setCouName((String) dtls[40]);
		clientDTO.setTitleName((String) dtls[41]);
		clientDTO.setSectName((String) dtls[42]);
		clientDTO.setPostalName((String) dtls[43]);
		clientDTO.setClientTypeDesc((String) dtls[44]);
		clientDTO.setHashCode((String) dtls[45]);
		 return clientDTO;
	}

	@Override
	@Transactional(readOnly = true)
	public DataTablesResult<MobilePrefixDef> findAllPrefixes(long couCode,DataTablesRequest request) throws IllegalAccessException {
		QMobilePrefixDef prefix =QMobilePrefixDef.mobilePrefixDef;
		BooleanExpression pred = prefix.country.couCode.eq(couCode);
		Page<MobilePrefixDef> page = mobileRepo.findAll(pred.and(request.searchPredicate(QMobilePrefixDef.mobilePrefixDef)), request);
		return new DataTablesResult<>(request, page);
	}


	@Override
	public DataTablesResult<ContractDTO> findAllContracts(DataTablesRequest request) {
		final String search = (request.getSearch()!=null && request.getSearch().getValue()!=null)?"%"+request.getSearch().getValue()+"%":"%%";
		List<Object[]> contracts = bindersRepo.searchAllBinders(search.toLowerCase(),request.getPageNumber(), request.getPageSize());
		final List<ContractDTO> contractList = new ArrayList<>();
		long rowCount = 0l;
		if(!contracts.isEmpty()) rowCount = ((BigInteger)contracts.get(0)[9]).intValue();
		for(Object[] obj:contracts){
			final ContractDTO contract = new ContractDTO();
			contract.setContractId(((BigInteger)obj[0]).longValue());
			contract.setContractName((String) obj[1]);
			contract.setContractPolicyNo((String) obj[3]);
			contract.setCurrency((String) obj[4]);
			contract.setIntermediary((String) obj[5]);
			contract.setIntermediaryType((String) obj[6]);
			contract.setStatus((String) obj[7]);
			contract.setAuthStatus((String) obj[8]);
			contractList.add(contract);
		}
		Page<ContractDTO>  page = new PageImpl<>(contractList,request, rowCount);
		return new DataTablesResult<>(request, page);
	}

	@Override
	@Transactional(readOnly = false)
	public void definePrefix(MobilePrefixDef prefix) throws BadRequestException {
		if(prefix.getProviders()==null)
			throw new BadRequestException("Mobile Provider is Mandatory...");
		//validator.validatePhonePrefix(prefix.getPrefixName());
		mobileRepo.save(prefix);
	}

	@Override
	@Transactional(readOnly = false)
	public void deletePrefix(Long prefCode) {
		mobileRepo.delete(prefCode);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<MobilePrefixDef> findSelPrefixes(String paramString, Pageable paramPageable, long coucode) {
		Predicate pred = null;
		if (paramString == null || StringUtils.isBlank(paramString)) {
			pred = QMobilePrefixDef.mobilePrefixDef.country.couCode.eq(coucode).and(QMobilePrefixDef.mobilePrefixDef.isNotNull());
		} else {
			pred = QMobilePrefixDef.mobilePrefixDef.country.couCode.eq(coucode).and(QMobilePrefixDef.mobilePrefixDef.prefixName.containsIgnoreCase(paramString));
		}
		return mobileRepo.findAll(pred, paramPageable);
	}

	@Override
	@Transactional(readOnly = true)
	public DataTablesResult<ClientTypes> findAllClientTypes(DataTablesRequest request) throws IllegalAccessException {
	    Page<ClientTypes> page = clientTypeRepo.findAll(request.searchPredicate(QClientTypes.clientTypes), request);
		return new DataTablesResult<>(request, page);

	}

	@Override
	@Transactional(readOnly = true)
	public DataTablesResult<RelationshipTypes> findAllRelationshipTypes(DataTablesRequest request) throws IllegalAccessException {
		Page<RelationshipTypes> page = relationshipTypesRepo.findAll(request.searchPredicate(QRelationshipTypes.relationshipTypes), request);
		return new DataTablesResult<>(request, page);
	}

	@Override
	@Transactional(readOnly = false)
	public void defineClientType(ClientTypes clientType) throws BadRequestException {
		if(clientType.getTypeId()==null){
			Predicate pred = QClientTypes.clientTypes.clientType.eq(clientType.getClientType()).and(QClientTypes.clientTypes.typeDesc.eq(StringUtils.upperCase(clientType.getTypeDesc())));
			if(clientTypeRepo.count(pred) > 0){
				throw new BadRequestException("Client Type Desc for the client Type already exists...");
			}
		}
		clientType.setTypeDesc(StringUtils.upperCase(clientType.getTypeDesc()));
		clientTypeRepo.save(clientType);
	}


	@Override
	@Transactional(readOnly = false)
	public void defineRelationshipType(RelationshipTypes relationshipTypes) throws BadRequestException {
		if(relationshipTypes.getTypeId()==null){
			Predicate pred = QRelationshipTypes.relationshipTypes.relationType.eq(relationshipTypes.getRelationType()).and(QRelationshipTypes.relationshipTypes.relationDesc.eq(StringUtils.upperCase(relationshipTypes.getRelationDesc())));
			if(relationshipTypesRepo.count(pred) > 0){
				throw new BadRequestException("This relationship Type already exists...");
			}
		}
		relationshipTypes.setRelationDesc(StringUtils.upperCase(relationshipTypes.getRelationDesc()));
		relationshipTypesRepo.save(relationshipTypes);
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteRelationshipType(Long relationCode) {
		relationshipTypesRepo.delete(relationCode);
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteClientType(Long typeCode) {
		clientTypeRepo.delete(typeCode);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ClientTypes> findSelClientTypes(String paramString, Pageable paramPageable) {
		Predicate pred = null;
		if (paramString == null || StringUtils.isBlank(paramString)) {
			pred = QClientTypes.clientTypes.isNotNull();
		} else {
			pred = QClientTypes.clientTypes.clientType.containsIgnoreCase(paramString).or(QClientTypes.clientTypes.typeDesc.containsIgnoreCase(paramString));
		}
		return clientTypeRepo.findAll(pred, paramPageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<RelationshipTypes> findSelRelationTypes(String paramString, Pageable paramPageable) {
		Predicate pred = null;
		if (paramString == null || StringUtils.isBlank(paramString)) {
			pred = QRelationshipTypes.relationshipTypes.isNotNull();
		} else {
			pred = QRelationshipTypes.relationshipTypes.relationType.containsIgnoreCase(paramString).or(QRelationshipTypes.relationshipTypes.relationDesc.containsIgnoreCase(paramString));
		}
		return relationshipTypesRepo.findAll(pred, paramPageable);
	}

	@Override
	@Transactional(readOnly = true)
	public DataTablesResult<ClientTitle> findAllClientTitles(DataTablesRequest request) throws IllegalAccessException {
		Page<ClientTitle> page = clientTitleRepo.findAll(request.searchPredicate(QClientTitle.clientTitle), request);
		return new DataTablesResult<>(request, page);
	}

	@Override
	@Transactional(readOnly = false)
	public void defineClientTitle(ClientTitle clientTitle) throws BadRequestException {
		clientTitleRepo.save(clientTitle);
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteClientTitle(Long titleCode) {
		clientTitleRepo.delete(titleCode);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ClientTitle> findSelClientTitles(String paramString, Pageable paramPageable) {
		Predicate pred = null;
		if (paramString == null || StringUtils.isBlank(paramString)) {
			pred = QClientTitle.clientTitle.isNotNull();
		} else {
			pred = QClientTitle.clientTitle.titleName.containsIgnoreCase(paramString);
		}
		return clientTitleRepo.findAll(pred, paramPageable);
	}

	@Override
	@Transactional(readOnly = true)
	public DataTablesResult<PostalCodesDef> findPostalCodesByTown(long townCode, DataTablesRequest request)
			throws IllegalAccessException {
		QPostalCodesDef postalCodes =QPostalCodesDef.postalCodesDef;
		BooleanExpression pred = postalCodes.town.ctCode.eq(townCode);
		Page<PostalCodesDef> page = postalRepo.findAll(pred.and(request.searchPredicate(QPostalCodesDef.postalCodesDef)), request);
		return new DataTablesResult<>(request, page);
	}

	@Override
	@Transactional(readOnly = false)
	public void definePostalCode(PostalCodesDef postalCode) throws BadRequestException {
		postalRepo.save(postalCode);
	}

	@Override
	@Transactional(readOnly = false)
	public void deletePostalCode(Long pCode) {
		postalRepo.delete(pCode);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<SectorDef> findSectorForSelect(String paramString, Pageable paramPageable) {
		Predicate pred = null;
		if (paramString == null || StringUtils.isBlank(paramString)) {
			pred = QSectorDef.sectorDef.isNotNull();
		} else {
			pred = QSectorDef.sectorDef.name.containsIgnoreCase(paramString);
		}
		return sectorRepo.findAll(pred, paramPageable);
	}

	@Override
	@Transactional(readOnly = true)
	public DataTablesResult<Occupation> findOccupationBySector(long sectorCode, DataTablesRequest request)
			throws IllegalAccessException {
		QOccupation occupation =QOccupation.occupation;
		BooleanExpression pred = occupation.sector.code.eq(sectorCode);
		Page<Occupation> page = occupRepo.findAll(pred.and(request.searchPredicate(QOccupation.occupation)), request);
		return new DataTablesResult<>(request, page);
	}

	@Override
	@Transactional(readOnly = false)
	public void defineSector(SectorDef sector) throws BadRequestException {
		sectorRepo.save(sector);
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteSector(Long sectCode) {
		sectorRepo.delete(sectCode);
	}

	@Override
	@Transactional(readOnly = false)
	public void defineOccupation(Occupation occup) throws BadRequestException {
		occupRepo.save(occup);
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteOccupation(Long occCode) {
		occupRepo.delete(occCode);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Occupation> findSectorOccupations(String paramString, Pageable paramPageable, long sectCode) {
		Predicate pred = null;
		if (paramString == null || StringUtils.isBlank(paramString)) {
			pred =QOccupation.occupation.sector.code.eq(sectCode).and(QOccupation.occupation.isNotNull());
		} else {
			pred = QOccupation.occupation.sector.code.eq(sectCode).and(QOccupation.occupation.name.containsIgnoreCase(paramString));
		}
		return occupRepo.findAll(pred, paramPageable);
	}

	@Override
	@Transactional(readOnly = true)
	public DataTablesResult<TaxRates> findTaxRates(Long subCode,Long prodCode, DataTablesRequest request)
			throws IllegalAccessException {
		QTaxRates taxRates =QTaxRates.taxRates;
		BooleanExpression pred = taxRates.subclass.subId.eq(subCode).and(taxRates.productsDef.proCode.eq(prodCode));
		Page<TaxRates> page = taxRatesRepo.findAll(pred.and(request.searchPredicate(QTaxRates.taxRates)), request);
		return new DataTablesResult<>(request, page);
	}

	@Override
	@Transactional(readOnly = false)
	public void defineTaxRates(TaxRates tax) throws BadRequestException {
		BooleanExpression pred = QTaxRates.taxRates.revenueItems.eq(tax.getRevenueItems()).and(QTaxRates.taxRates.subclass.eq(tax.getSubclass()))
				.and(QTaxRates.taxRates.productsDef.proCode.eq(tax.getProductsDef().getProCode()));
		if(tax.getTaxId()==null){
			if(taxRatesRepo.count(pred)>0){
				throw new BadRequestException("Revenue Item For sub class has already been setup");
			}
		}
		taxRatesRepo.save(tax);
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteTaxRates(Long taxCode) {
		taxRatesRepo.delete(taxCode);
	}

	@Override
	@Transactional(readOnly = true)
	public DataTablesResult<PerilsDef> findPerils(DataTablesRequest request) throws IllegalAccessException {
		Page<PerilsDef> page = perilsRepo.findAll(request.searchPredicate(QPerilsDef.perilsDef), request);
		return new DataTablesResult<>(request, page);
	}

	@Override
	@Transactional(readOnly = false)
	public void definePerils(PerilsDef peril) throws BadRequestException {
		perilsRepo.save(peril);
	}

	@Override
	@Transactional(readOnly = false)
	public void deletePerils(Long perilCode) {
		perilsRepo.delete(perilCode);
	}

	@Override
	@Transactional(readOnly = true)
	public DataTablesResult<RevenueItemsDef> findPrgRevenueItems(long prgCode, DataTablesRequest request)
			throws IllegalAccessException {
		QRevenueItemsDef revItems =QRevenueItemsDef.revenueItemsDef;
		BooleanExpression pred = revItems.prodGroup.subId.eq(prgCode);
		Page<RevenueItemsDef> page = revenueItemsRepo.findAll(pred.and(request.searchPredicate(QRevenueItemsDef.revenueItemsDef)), request);
		return new DataTablesResult<>(request, page);
	}

	@Override
	public DataTablesResult<RevenueItemsDef> findPrgRevenueItems(DataTablesRequest request) throws IllegalAccessException {
		QRevenueItemsDef revItems =QRevenueItemsDef.revenueItemsDef;
		BooleanExpression pred = revItems.prodGroup.isNull();
		Page<RevenueItemsDef> page = revenueItemsRepo.findAll(pred.and(request.searchPredicate(QRevenueItemsDef.revenueItemsDef)), request);
		return new DataTablesResult<>(request, page);
	}

	@Override
	@Transactional(readOnly = true)
	public Set<RevenueItemBean> findUnassignedRevItems(Long prgCode) throws IllegalAccessException {
		Set<RevenueItemBean> revItems = new HashSet<>();
		if(prgCode!=null) {
			Iterable<RevenueItemsDef> items = revenueItemsRepo.findAll(QRevenueItemsDef.revenueItemsDef.prodGroup.subId.eq(prgCode));
			if (items.spliterator().getExactSizeIfKnown() > 0) {
				for (RevenueItemsDef revItem : items) {
					RevenueItemBean bean = new RevenueItemBean();
					bean.setCode(revItem.getItem().getCode());
					bean.setValue(revItem.getItem().getValue());
					bean.setCrAccount(revItem.getCrAccount());
					bean.setDrAccount(revItem.getDrAccount());
					bean.setChecked(true);
					bean.setRevCode(revItem.getRevenueId());
					revItems.add(bean);
				}
			}
				for (RevenueItems item : RevenueItems.values()) {
					RevenueItemBean bean = new RevenueItemBean();
					bean.setCode(item.getCode());
					bean.setValue(item.getValue());
					bean.setCrAccount(null);
					bean.setDrAccount(null);
					bean.setChecked(false);
					revItems.add(bean);
				}

		}
		else
		for(RevenueItems item:RevenueItems.values()){
			RevenueItemBean bean = new RevenueItemBean();
			bean.setCode(item.getCode());
			bean.setValue(item.getValue());
			bean.setCrAccount(null);
			bean.setDrAccount(null);
			revItems.add(bean);

		}
		System.out.println(revItems);
		return revItems;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
	public void createBulkRevenueItems(RevenueItemModel revenueModel) throws BadRequestException{
		List<RevenueItemsDef> revenueItemsList = new ArrayList<>();
		for(RevenueItemBean bean:revenueModel.getItems()){
			if(bean.isChecked() && bean.getRevCode()==null) {
				RevenueItemsDef revItem = new RevenueItemsDef();
				if(bean.getDrAccountId()!=null){
					revItem.setDrAccount(subAccountsRepo.findOne(bean.getDrAccountId()));
				}
				if(bean.getCrAccountId()!=null) {
					revItem.setCrAccount(subAccountsRepo.findOne(bean.getCrAccountId()));
				}
				if (revItemFinder.getRevenueItem(bean.getCode()) == null)
					throw new BadRequestException("Revenue Item Cannot be null");
				revItem.setItem(revItemFinder.getRevenueItem(bean.getCode()));
				if(revenueModel.getPrgCode()!=null)
				revItem.setProdGroup(subClassRepo.findOne(revenueModel.getPrgCode()));
				revenueItemsList.add(revItem);
			}
			else if(bean.isChecked() && bean.getRevCode()!=null){
				RevenueItemsDef revItem = revenueItemsRepo.findOne(bean.getRevCode());
				if(bean.getDrAccountId()!=null){
					revItem.setDrAccount(subAccountsRepo.findOne(bean.getDrAccountId()));
				}
				if(bean.getCrAccountId()!=null) {
					revItem.setCrAccount(subAccountsRepo.findOne(bean.getCrAccountId()));
				}
				revenueItemsList.add(revItem);
			}
		}
		revenueItemsRepo.save(revenueItemsList);
	}

	@Override
	@Transactional(readOnly = false)
	public void updateRevenueItems(RevenueItemsDef revItem) {
		revenueItemsRepo.save(revItem);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<RevenueItemsDef> getTaxRevenueItems(String paramString, Pageable paramPageable,long prodCode) {
		Predicate pred = null;
//		ProductGroupDef productGroupDef = productsRepo.findOne(prodCode).getProGroup();
		if (paramString == null || StringUtils.isBlank(paramString)) {
			pred = QRevenueItemsDef.revenueItemsDef.prodGroup.subId.eq(prodCode).and(QRevenueItemsDef.revenueItemsDef.item.eq(RevenueItems.PHCF).
					or(QRevenueItemsDef.revenueItemsDef.item.eq(RevenueItems.EX)).
					or(QRevenueItemsDef.revenueItemsDef.item.eq(RevenueItems.TL)).
					or(QRevenueItemsDef.revenueItemsDef.item.eq(RevenueItems.WHTX)).
					or(QRevenueItemsDef.revenueItemsDef.item.eq(RevenueItems.SD)).
					or(QRevenueItemsDef.revenueItemsDef.item.eq(RevenueItems.CF)).
					or(QRevenueItemsDef.revenueItemsDef.item.eq(RevenueItems.RE)).
					or(QRevenueItemsDef.revenueItemsDef.item.eq(RevenueItems.SC))).
					and(QRevenueItemsDef.revenueItemsDef.isNotNull());
		} else {
			pred =QRevenueItemsDef.revenueItemsDef.prodGroup.subId.eq(prodCode).and(QRevenueItemsDef.revenueItemsDef.item.eq(RevenueItems.PHCF).
					or(QRevenueItemsDef.revenueItemsDef.item.eq(RevenueItems.EX)).
					or(QRevenueItemsDef.revenueItemsDef.item.eq(RevenueItems.TL)).
					or(QRevenueItemsDef.revenueItemsDef.item.eq(RevenueItems.WHTX)).
					or(QRevenueItemsDef.revenueItemsDef.item.eq(RevenueItems.SD)).
					or(QRevenueItemsDef.revenueItemsDef.item.eq(RevenueItems.CF)).
					or(QRevenueItemsDef.revenueItemsDef.item.eq(RevenueItems.RE)).
					or(QRevenueItemsDef.revenueItemsDef.item.eq(RevenueItems.SC)))
					.and(QRevenueItemsDef.revenueItemsDef.item.stringValue().containsIgnoreCase(paramString));
		}
		return revenueItemsRepo.findAll(pred, paramPageable);
	}

	@Override
	@Transactional(readOnly = true)
	public DataTablesResult<ShortPeriodRates> findShortPeriodRates(DataTablesRequest request)
			throws IllegalAccessException {
		Page<ShortPeriodRates> page = shortPeriodRepo.findAll(request.searchPredicate(QShortPeriodRates.shortPeriodRates), request);
		return new DataTablesResult<>(request, page);
	}

	@Override
	@Transactional(readOnly = false)
	public void defineShortPeriodRates(ShortPeriodRates shtPeriods) throws BadRequestException {
		if(shtPeriods.getPeriodFrom() > shtPeriods.getPeriodTo())
			throw new BadRequestException("Period From Cannot be greater than Period to");
		Long count = shortPeriodRepo.count(QShortPeriodRates.shortPeriodRates.periodFrom.between(shtPeriods.getPeriodFrom(), shtPeriods.getPeriodTo())
				.or(QShortPeriodRates.shortPeriodRates.periodTo.between(shtPeriods.getPeriodFrom(), shtPeriods.getPeriodTo())));
		if(count > 0 && shtPeriods.getSpCode()==null) throw new BadRequestException("Range For the Short Period Exists");
		if(shtPeriods.getRate().compareTo(BigDecimal.ZERO)==0) throw new BadRequestException("Rate Cannot be Zero");
		if(shtPeriods.getRate().compareTo(BigDecimal.ZERO)==-1) throw new BadRequestException("Rate Cannot be less Zero");
		if(shtPeriods.getDivFactor().compareTo(BigDecimal.ZERO)==0) throw new BadRequestException("Division Factor Cannot be Zero");
		shortPeriodRepo.save(shtPeriods);

	}

	@Override
	@Transactional(readOnly = false)
	public void deleteShortPrdRates(Long periodCode) {
		shortPeriodRepo.delete(periodCode);

	}

	@Override
	@Transactional(readOnly = true)
	public DataTablesResult<SecShortPeriodRates> findSecShortPeriodRates(long subsecCode,DataTablesRequest request)
			throws IllegalAccessException {
		QSecShortPeriodRates shortPeriods = QSecShortPeriodRates.secShortPeriodRates;
		BooleanExpression pred = shortPeriods.section.ssId.eq(subsecCode);
		Page<SecShortPeriodRates> page = secShortPeriodRepo.findAll(pred.and(request.searchPredicate(QSecShortPeriodRates.secShortPeriodRates)), request);
		return new DataTablesResult<>(request, page);
	}

	@Override
	@Transactional(readOnly = false)
	public void defineSecShortPeriodRts(SecShortPeriodRates shtPeriods) throws BadRequestException {
		if(shtPeriods.getPeriodFrom() > shtPeriods.getPeriodTo())
			throw new BadRequestException("Period From Cannot be greater than Period to");
		Predicate seqPredicate = QSecShortPeriodRates.secShortPeriodRates.section.ssId.eq(shtPeriods.getSection().getSsId())
				.and(QSecShortPeriodRates.secShortPeriodRates.periodFrom.between(shtPeriods.getPeriodFrom(), shtPeriods.getPeriodTo()))
				.or(QSecShortPeriodRates.secShortPeriodRates.section.ssId.eq(shtPeriods.getSection().getSsId()).and(QSecShortPeriodRates.secShortPeriodRates.periodTo.between(shtPeriods.getPeriodFrom(), shtPeriods.getPeriodTo())));
		Long count = secShortPeriodRepo.count(seqPredicate);
		if(count > 0 && shtPeriods.getSpCode()==null) throw new BadRequestException("Range For the Short Period Exists");
		if(shtPeriods.getRate().compareTo(BigDecimal.ZERO)==0) throw new BadRequestException("Rate Cannot be Zero");
		if(shtPeriods.getRate().compareTo(BigDecimal.ZERO)==-1) throw new BadRequestException("Rate Cannot be less Zero");
		if(shtPeriods.getDivFactor().compareTo(BigDecimal.ZERO)==0) throw new BadRequestException("Division Factor Cannot be Zero");
		secShortPeriodRepo.save(shtPeriods);
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteSecShortPrdRts(Long periodCode) {
		secShortPeriodRepo.delete(periodCode);

	}

	@Override
	@Transactional(readOnly = true)
	public Page<SubclassSections> findAllSubClassSections(String paramString, Pageable paramPageable) {
		Predicate pred = null;
		if (paramString == null || StringUtils.isBlank(paramString)) {
			pred = QSubclassSections.subclassSections.isNotNull();
		} else {
			pred = QSubclassSections.subclassSections.subclass.subDesc.containsIgnoreCase(paramString).or(QSubclassSections.subclassSections.section.desc.containsIgnoreCase(paramString));
		}
		return subSectionsRepo.findAll(pred, paramPageable);
	}

	@Override
	@Transactional(readOnly = false)
	public void defineEndorseRemarks(EndorsementRemarks remarks) throws BadRequestException {
		endorseRemarksRepo.save(remarks);

	}

	@Override
	@Transactional(readOnly = false)
	public void deleteEndorseRemarks(Long remarkCode) {
		endorseRemarksRepo.delete(remarkCode);

	}

	@Override
	@Transactional(readOnly = true)
	public DataTablesResult<EndorsementRemarks> findAllEndorseRemarks(DataTablesRequest request)
			throws IllegalAccessException {
		Page<EndorsementRemarks> page = endorseRemarksRepo.findAll(request.searchPredicate(QEndorsementRemarks.endorsementRemarks), request);
		return new DataTablesResult<>(request, page);
	}

	@Override
	@Transactional(readOnly = false)
	public void defineSubClassPerils(SubclassPerils subclassPeril) {
		subClassPerilsRepo.save(subclassPeril);
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteSubPerils(Long subPerilId) {
		subClassPerilsRepo.delete(subPerilId);
	}

	@Override
	@Transactional(readOnly = true)
	public DataTablesResult<SubclassPerils> findSubclassPerils(Long subCode, DataTablesRequest request) throws IllegalAccessException {
		QSubclassPerils subclassPerils = QSubclassPerils.subclassPerils;
		BooleanExpression pred = subclassPerils.subclass.subId.eq(subCode);
		Page<SubclassPerils> page = subClassPerilsRepo.findAll(pred.and(request.searchPredicate(QSubclassPerils.subclassPerils)), request);
		return new DataTablesResult<>(request, page);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<PerilsDef> findSelectPerils(String paramString, Pageable paramPageable, long subCode) {
		return subClassPerilsRepo.getUnassignedPerils(subCode,paramString,paramPageable);
	}

	@Override
	@Transactional(readOnly = true)
	public DataTablesResult<ClmCausations> findAllActivities(DataTablesRequest request) throws IllegalAccessException {
		Page<ClmCausations> page = clmStatusRepo.findAll(request.searchPredicate(QClmCausations.clmCausations), request);
		return new DataTablesResult<>(request, page);
	}

	@Override
	@Transactional(readOnly = false)
	public void defineActivity(ClmCausations clmStatus) {
		clmStatusRepo.save(clmStatus);
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteActivity(Long activityId) {
		clmStatusRepo.delete(activityId);
	}

	@Override
	@Transactional(readOnly = true)
	public DataTablesResult<TransactionMapping> findTransMapping(DataTablesRequest request) throws IllegalAccessException {
		Page<TransactionMapping> page = mappingRepo.findAll(request.searchPredicate(QTransactionMapping.transactionMapping), request);
		return new DataTablesResult<>(request, page);
	}

	@Override
	@Transactional(readOnly = false)
	public void defineTransMapping(TransactionMapping mapping) throws BadRequestException {
		if(mapping.getTmNo()==null) {
			Long count = mappingRepo.count(QTransactionMapping.transactionMapping.transType.eq(mapping.getTransType()));
			if (count > 0) throw new BadRequestException("Transaction Type already Exists...");
		}
		mappingRepo.save(mapping);
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteMapping(Long mappingId) {
		mappingRepo.delete(mappingId);
	}

	@Override
	public void deleteRevenueItem(Long revenueId) {
		revenueItemsRepo.delete(revenueId);
	}

	@Override
	public Page<CoaSubAccountsDTO> findGLAccounts(String paramString, Pageable paramPageable) {
		final String search = (paramString!=null)?"%"+paramString+"%":"%%";
		List<Object[]> accounts = subAccountsRepo.findAllSubAccounts(search.toLowerCase(),paramPageable.getPageNumber(), paramPageable.getPageSize());
		final List<CoaSubAccountsDTO> accountsDTOList = new ArrayList<>();
		long rowCount = 0l;
		if(!accounts.isEmpty()) rowCount = (Integer)accounts.get(0)[3];
		for(Object[] account:accounts){
			CoaSubAccountsDTO accountsDTO = new CoaSubAccountsDTO();
			accountsDTO.setCoId(((BigDecimal)account[0]).longValue());
			accountsDTO.setCode((String)account[1]);
			accountsDTO.setName((String)account[2]);
			accountsDTOList.add(accountsDTO);
		}
		return new PageImpl<>(accountsDTOList,paramPageable,rowCount);
	}

	@Override
	public Date getTodayDate() {
		return new Date();
	}

	@Override
	public Page<Town> findTownForSelect(String paramString, Pageable paramPageable) {
		Predicate pred = null;
		if (paramString == null || StringUtils.isBlank(paramString)) {
			pred = QTown.town.isNotNull();
		} else {
			pred = QTown.town.ctShtDesc.containsIgnoreCase(paramString).or(QTown.town.ctName.containsIgnoreCase(paramString));
		}
		return townRepo.findAll(pred, paramPageable);
	}

	@Override
	public Page<PostalCodesDef> findTownPostalCOdes(String paramString, Pageable paramPageable, long townCode) {
		Predicate pred = null;
		if (paramString == null || StringUtils.isBlank(paramString)) {
			pred = QPostalCodesDef.postalCodesDef.isNotNull().and(QPostalCodesDef.postalCodesDef.town.ctCode.eq(townCode));
		} else {
			pred = QPostalCodesDef.postalCodesDef.town.ctCode.eq(townCode).and(QPostalCodesDef.postalCodesDef.zipCode.containsIgnoreCase(paramString)
					.or(QPostalCodesDef.postalCodesDef.postalName.containsIgnoreCase(paramString)));
		}
		return postalRepo.findAll(pred, paramPageable);
	}

	@Override
	public Page<Banks> findBanksForSelect(String paramString, Pageable paramPageable) {
		Predicate pred = null;
		if (paramString == null || StringUtils.isBlank(paramString)) {
			pred = QBanks.banks.isNotNull();
		} else {
			pred = QBanks.banks.bankName.containsIgnoreCase(paramString).or(QBanks.banks.bankShtDesc.containsIgnoreCase(paramString));
		}
		return banksRepo.findAll(pred, paramPageable);
	}

	@Override
	public Page<BankBranchDTO> findBankBranchesForSel(String paramString, Pageable paramPageable, long bankCode) {
		final String search = (paramString!=null)?"%"+paramString+"%":"%%";
		List<Object[]> branches = bankBranchRepo.findBnkBranchesByBank(search.toLowerCase(),bankCode,
				paramPageable.getPageNumber(), paramPageable.getPageSize());
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
	public CountryDTO getDefaultCountry() {
		OrganizationDTO organization = organizationService.getOrganizationDetails();
		if(organization.getCouCode()!=null){
			final CountryDTO countryDTO = new CountryDTO();
			countryDTO.setCouCode(organization.getCouCode());
			countryDTO.setCouName(organization.getCouName());
			return countryDTO;
		}
		return new CountryDTO();
	}

	@Override
	public Page<MobProviders> findMobProviders(String paramString, Pageable paramPageable) {
		Predicate pred = null;
		if (paramString == null || StringUtils.isBlank(paramString)) {
			pred = QMobProviders.mobProviders.isNotNull();
		} else {
			pred = QMobProviders.mobProviders.providerName.containsIgnoreCase(paramString);
		}
		return mobProvidersRepo.findAll(pred, paramPageable);
	}

	@Override
	@Transactional(readOnly = false)
	public void defineMobProviders(MobProviders provider) throws BadRequestException {
		mobProvidersRepo.save(provider);
	}

	@Override
	@Transactional(readOnly = true)
	public DataTablesResult<InterestedParties> findInterestedParties(DataTablesRequest request) throws IllegalAccessException {
		Page<InterestedParties> page = interestedPartiesRepo.findAll(request.searchPredicate(QInterestedParties.interestedParties), request);
		return new DataTablesResult<>(request, page);
	}

	@Override
	@Transactional(readOnly = false)
	public void defineInterestedParties(InterestedParties interestedParties) throws BadRequestException {
		   if(interestedParties.getDateRegistered().after(new Date())){
			   throw new BadRequestException("Date Registered/Date of Birth cannot be after today..");
		   }
		   interestedParties.setDateCreated(new Date());
		   interestedParties.setUser(userUtils.getCurrentUser());
		   validator.validatePinNo(interestedParties.getPinNumber());
           interestedPartiesRepo.save(interestedParties);
	}

	@Override
	@Transactional(readOnly = false)
	public void deletInterestedParties(Long interestId) {
         interestedPartiesRepo.delete(interestId);
	}

	@Override
	@Transactional(readOnly = true)
	public DataTablesResult<ProductChecks> findProductChecks(DataTablesRequest request, Long prodId) throws IllegalAccessException {
		BooleanExpression pred = QProductChecks.productChecks.product.proCode.eq(prodId);
		Page<ProductChecks> page = productChecksRepo.findAll(pred.and(request.searchPredicate(QProductChecks.productChecks)), request);
		return new DataTablesResult<>(request, page);
	}

	@Override
	public DataTablesResult<Checks> findAllChecks(DataTablesRequest request) throws IllegalAccessException {
		Page<Checks> page = checksRepo.findAll(request.searchPredicate(QChecks.checks), request);
		return new DataTablesResult<>(request, page);
	}

	@Override
	@Transactional(readOnly = false)
	public void defineChecks(Checks checks) throws BadRequestException {
		  if(checks.getCheckType()!=null && "EX".equalsIgnoreCase(checks.getCheckType())){
			  if(checks.getPermissionsDef()==null){
				  throw new BadRequestException("Permission is Mandatory for Exceptions");
			  }
		  }
		if(checks.getCheckApplicable()!=null && "on".equalsIgnoreCase(checks.getCheckApplicable())){
			checks.setCheckApplicable("Y");
		}
		if(checks.getCheckCommon()!=null && "on".equalsIgnoreCase(checks.getCheckCommon())){
			checks.setCheckCommon("Y");
		}
		else checks.setCheckCommon("N");
          checksRepo.save(checks);
	}

	@Override
	public Iterable<Checks> findUnassginedChecks(long prodId) {
		Iterable<ProductChecks> availableChecks = productChecksRepo.findAll(QProductChecks.productChecks.product.proCode.eq(prodId));
		List<Long> longChecks = new ArrayList<>();
		for(ProductChecks productChecks:availableChecks){
			longChecks.add(productChecks.getChecks().getCheckId());
		}
		Iterable<Checks> checks = checksRepo.findAll(QChecks.checks.checkId.notIn(longChecks).and(QChecks.checks.checkCommon.ne("Y")));
		return checks;
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteChecks(Long checkCode) {
         checksRepo.delete(checkCode);
	}

	@Override
	public void deleteProdChecks(Long checkCode) {
		productChecksRepo.delete(checkCode);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<PermissionsDef> findSelPermissions(String paramString, Pageable paramPageable) {
		Predicate pred = null;
		if (paramString == null || StringUtils.isBlank(paramString)) {
			pred = QPermissionsDef.permissionsDef.isNotNull();
		} else {
			pred = QPermissionsDef.permissionsDef.permName.containsIgnoreCase(paramString);
		}
		return permissionsRepo.findAll(pred, paramPageable);
	}

	@Override
	public Page<ProductsDef> findSelProducts(String paramString, Pageable paramPageable) {
		Predicate pred = null;
		if (paramString == null || StringUtils.isBlank(paramString)) {
			pred = QProductsDef.productsDef.isNotNull();
		} else {
			pred = QProductsDef.productsDef.proDesc.containsIgnoreCase(paramString);
		}
		return productsRepo.findAll(pred, paramPageable);
	}

	@Override
	@Transactional(readOnly = true)
	public DataTablesResult<AccountsDocs> findAccountDocs(DataTablesRequest request, Long accountId) throws IllegalAccessException {
		BooleanExpression pred = QAccountsDocs.accountsDocs.accountDef.acctId.eq(accountId);
		Page<AccountsDocs> page = accountDocsRepo.findAll(pred.and(request.searchPredicate(QAccountsDocs.accountsDocs)), request);
		return new DataTablesResult<>(request, page);
	}

	@Override
	public List<RequiredDocs> findUnassignedAcctDocs(Long acctId, String docName) throws IllegalAccessException {
		if(acctId==null)
			throw new IllegalArgumentException("Account is Mandatory....");
		AccountDef accountDef = accountRepo.findOne(acctId);
		if(accountDef.getAccountType().getAccountType()==AccountTypeEnum.INS)
		return requiredDocsRepo.getUnassignedAccountDocs(acctId, docName);
		else  if(accountDef.getAccountType().getAccountType()==AccountTypeEnum.IA)
			return requiredDocsRepo.getUnassignedSubAgentAccountDocs(acctId, docName);
		else return new ArrayList<>();
	}

	@Override
	public void createAcctRequiredDocs(RequiredDocBean requiredDocBean) {
		List<AccountsDocs> accountsDocs = new ArrayList<>();
		for(Long reqId:requiredDocBean.getRequiredDocs()){
			AccountsDocs accountsDoc = new AccountsDocs();
			accountsDoc.setRequiredDoc(requiredDocsRepo.findOne(reqId));
			accountsDoc.setAccountDef(accountRepo.findOne(requiredDocBean.getSubCode()));
			accountsDocs.add(accountsDoc);
		}
		accountDocsRepo.save(accountsDocs);
	}

	@Override
	public void createProdChecks(ChecksBean checksBean) {
		List<ProductChecks> productChecks = new ArrayList<>();
		for(Long check:checksBean.getChecks()){
			ProductChecks productCheck = new ProductChecks();
			productCheck.setChecks(checksRepo.findOne(check));
			productCheck.setProduct(productsRepo.findOne(checksBean.getProCode()));
			productChecks.add(productCheck);
		}
		productChecksRepo.save(productChecks);
	}

	@Override
	public List<SingleQuizModel> findAllQuestions() throws IllegalAccessException {
		List<SingleQuizModel> questionChoiceModels = new ArrayList<>();
		Iterable<LifeQuestions> lifeQuestions = lifeQuestionsRepo.findAll();
		for(LifeQuestions questions:lifeQuestions){
			SingleQuizModel singleQuizModel = new SingleQuizModel();
			singleQuizModel.setName(questions.getQuestionname());
			singleQuizModel.setType(questions.getQuestiontype());
			Iterable<LifeQuestionsChoices> lifeQuestionsChoices = lifeQuestionsChoicesRepo.findAll(QLifeQuestionsChoices.lifeQuestionsChoices.questions.bqdid.eq(questions.getBqdid()));
			Long size = lifeQuestionsChoices.spliterator().getExactSizeIfKnown();
			String[] arr = new String[size.intValue()];
			int i = 0;
			for(LifeQuestionsChoices questionsChoices:lifeQuestionsChoices){
				arr[i] = questionsChoices.getChoice();
				i++;
			}
			singleQuizModel.setChoices(arr);
			questionChoiceModels.add(singleQuizModel);
		}
		return questionChoiceModels;
	}

	@Override
	public DataTablesResult<LifeQuestions> findQuestions(DataTablesRequest request) throws IllegalAccessException {
		Page<LifeQuestions> page = lifeQuestionsRepo.findAll(request.searchPredicate(QLifeQuestions.lifeQuestions), request);
		return new DataTablesResult<>(request, page);
	}

	@Override
	public void defineQuestions(LifeQuestions questions) {
        lifeQuestionsRepo.save(questions);
	}

	@Override
	public DataTablesResult<LifeQuestionsChoices> findQuestionsChoices(DataTablesRequest request, Long quizId) throws IllegalAccessException {
		BooleanExpression pred = QLifeQuestionsChoices.lifeQuestionsChoices.questions.bqdid.eq(quizId);
		Page<LifeQuestionsChoices> page = lifeQuestionsChoicesRepo.findAll(pred.and(request.searchPredicate(QLifeQuestionsChoices.lifeQuestionsChoices)), request);
		return new DataTablesResult<>(request, page);
	}

	@Override
	public void defineQuestionsChoices(LifeQuestionsChoices questionsChoices) {
		lifeQuestionsChoicesRepo.save(questionsChoices);
	}

	@Override
	public Page<SubClassDef> findSubclassSel(String paramString, Pageable paramPageable) {
		Predicate pred = null;
		if (paramString == null || StringUtils.isBlank(paramString)) {
			pred = QSubClassDef.subClassDef.isNotNull();
		} else {
			pred =  QSubClassDef.subClassDef.subDesc.containsIgnoreCase(paramString);
		}
		return subClassRepo.findAll(pred, paramPageable);
	}

	@Override
	public Page<Year> findYearForSelect(String term, Pageable pageable) {
    List<Year> years = new ArrayList<>(Arrays.asList(
    		new Year("2018",2018), new Year("2019",2019),
		new Year("2020",2020),
		new Year("2021",2021),
		new Year("2022",2022),
		new Year("2023",2023),
		new Year("2024",2024),
		new Year("2025",2025),
		new Year("2026",2026),
		new Year("2027",2027),
		new Year("2028",2028),
		new Year("2029",2029),
		new Year("2030",2030),
		new Year("2031",2031),
		new Year("2032",2032),
		new Year("2033",2033),
			new Year("2034",2034),
			new Year("2035",2035),
		new Year("2036",2036),
		new Year("2037",2037),
		new Year("2038",2038),
			new Year("2039",2039),
			new Year("2040",2040)
	));

		return new PageImpl<>(years);
	}

	@Override
	public OrgBranch findThisBranchS(String branch) throws BadRequestException {
		Predicate predicate=QOrgBranch.orgBranch.obName.equalsIgnoreCase(branch);
		Long myBranch=branchRepo.count(predicate);
		if(myBranch<0)throw new BadRequestException("No Branch exists with that name,format yor Excel sheet well");
		return branchRepo.findOne(predicate);
	}

	@Override
	public AccountDef findThisAgentS(String user) throws BadRequestException {
		Predicate predicate=QAccountDef.accountDef.name.equalsIgnoreCase(user);
		Long myUser=accountRepo.count(predicate);
		if(myUser<0)throw new BadRequestException("No User exists with that name,format yor Excel sheet well");
		return accountRepo.findOne(predicate);
	}

	@Override
	public ProductReportGroup findThisProductS(String product) throws BadRequestException{
		Predicate predicate=QProductReportGroup.productReportGroup.repDesc.equalsIgnoreCase(product);
		ProductReportGroup productReportGroup=productReportRepo.findOne(predicate);
		return productReportRepo.findOne(predicate);
	}

	@Override
	public Page<PermissionsDef> findSelPerm(String term, Long exception, Pageable pageable) {
		BooleanExpression pred = QPermissionsDef.permissionsDef.module.moduleId.eq(exception).and(QPermissionsDef.permissionsDef.permName.containsIgnoreCase(term));
		/* term=term.toUpperCase();
      if(term==null || StringUtils.isBlank(term)) {
		  List<PermissionsDef> page = permissionsRepo.findByModule_ModuleId(exception);
		  Page<PermissionsDef> pages = new PageImpl<>(page);
		  return pages;
	  }
      else{
		  List<PermissionsDef> page = permissionsRepo.findByModule_ModuleId(exception);
		  String finalTerm = term;
		  page=page.stream().filter(p->p.getPermName().contains(finalTerm)).collect(Collectors.toList());

		  Page<PermissionsDef> pages = new PageImpl<>(page);
      return pages;

	  }

     */
    return permissionsRepo.findAll(pred,pageable);
	}

    @Override
    public Long getId(String exception) {
	    Predicate predicate= QModulesDef.modulesDef.moduleName.eq(exception);
        ModulesDef modulesDef=modulesRepo.findOne(predicate);
		return modulesDef.getModuleId();
    }

	@Override
	public ReceiptTrans getReceipts(Long receiptCode) {
		Predicate predicate= QReceiptTrans.receiptTrans.receiptId.eq(receiptCode);
		return receiptRepository.findOne(predicate);
	}

	@Override
	public Page<ProductReportGroup> selRptGrps(String paramString, Pageable pageable) {
		Predicate pred = null;
		if (paramString == null || StringUtils.isBlank(paramString)) {
			pred = QProductReportGroup.productReportGroup.isNotNull();
		} else {
			pred =  QProductReportGroup.productReportGroup.repShtDesc.containsIgnoreCase(paramString);
		}
		return productReportRepo.findAll(pred,pageable);
	}

	@Override
	public void createRptGroup(ProductReportGroup productReportGroup) throws BadRequestException {

		if(productReportGroup.getRptId()==null) {
			Predicate predicate = QProductReportGroup.productReportGroup.repDesc.equalsIgnoreCase(productReportGroup.getRepDesc())
					.and(QProductReportGroup.productReportGroup.repShtDesc.equalsIgnoreCase(productReportGroup.getRepShtDesc()));
			Long bud2 = productReportRepo.count(predicate);
			if (bud2 > 0) {
				throw new BadRequestException("A similar report group exists");
			} else {
				productReportRepo.save(productReportGroup);
			}
		}
		else{
			ProductReportGroup reportGroup=productReportRepo.findOne(productReportGroup.getRptId());
				reportGroup.setRepDesc(productReportGroup.getRepDesc());
				reportGroup.setRepShtDesc(productReportGroup.getRepShtDesc());
				productReportRepo.save(reportGroup);
			}
	}

	@Override
	public OrgBranch findThisBranch(String product, String name, String branch, String agent, Integer year, BigDecimal janPol, BigDecimal janProd, BigDecimal febProd, BigDecimal febPol, BigDecimal marProd, BigDecimal marPol, BigDecimal aprProd, BigDecimal aprPol, BigDecimal mayProd, BigDecimal mayPol, BigDecimal junProd, BigDecimal junPol, BigDecimal julProd, BigDecimal julPol, BigDecimal augProd, BigDecimal augPol, BigDecimal sepProd, BigDecimal sepPol, BigDecimal octProd, BigDecimal octPol, BigDecimal novProd, BigDecimal novPol, BigDecimal decProd, BigDecimal decPol) throws BadRequestException {
		Predicate predicate=QOrgBranch.orgBranch.obName.equalsIgnoreCase(branch);
		OrgBranch orgBranch=branchRepo.findOne(predicate);
		if(orgBranch==null){
			UnloadedBudgets unloadedBudgets = new UnloadedBudgets();
			unloadedBudgets.setName(name);
			unloadedBudgets.setProductReportGroup(product);
			unloadedBudgets.setAccountDef(agent);
			unloadedBudgets.setBranch(branch);
			unloadedBudgets.setYear(year==null?null:year.toString());
			unloadedBudgets.setJanProd(janProd==null?null:janProd.toString());
			unloadedBudgets.setFebProd(febProd==null?null:febProd.toString());
			unloadedBudgets.setMarProd(marProd==null?null:marProd.toString());
			unloadedBudgets.setAprProd(aprProd==null?null:aprProd.toString());
			unloadedBudgets.setMayProd(mayProd==null?null:mayProd.toString());
			unloadedBudgets.setJunProd(junProd==null?null:junProd.toString());
			unloadedBudgets.setJulProd(julProd==null?null:julProd.toString());
			unloadedBudgets.setAugProd(augProd==null?null:augProd.toString());
			unloadedBudgets.setSepProd(sepProd==null?null:sepProd.toString());
			unloadedBudgets.setOctProd(octProd==null?null:octProd.toString());
			unloadedBudgets.setNovProd(novProd==null?null:novProd.toString());
			unloadedBudgets.setDecProd(decProd==null?null:decProd.toString());
			unloadedBudgets.setJanPol(janPol==null?null:janPol.toString());
			unloadedBudgets.setFebPol(febPol==null?null:febPol.toString());
			unloadedBudgets.setMarPol(marPol==null?null:marPol.toString());
			unloadedBudgets.setAprPol(aprPol==null?null:aprPol.toString());
			unloadedBudgets.setMayPol(mayPol==null?null:mayPol.toString());
			unloadedBudgets.setJunPol(junPol==null?null:junPol.toString());
			unloadedBudgets.setJulPol(julPol==null?null:julPol.toString());
			unloadedBudgets.setAugPol(augPol==null?null:augPol.toString());
			unloadedBudgets.setSepPol(sepPol==null?null:sepPol.toString());
			unloadedBudgets.setOctPol(octPol==null?null:octPol.toString());
			unloadedBudgets.setNovPol(novPol==null?null:novPol.toString());
			unloadedBudgets.setDecPol(decPol==null?null:decPol.toString());
			unloadedBudgets.setError("Input a valid branch and ensure no blanks in the field");
			unloadedBudgetsRepo.save(unloadedBudgets);
			throw new BadRequestException("No Branch exists with that name,format yor Excel sheet well");
		}
		else{
			return branchRepo.findOne(predicate);
		}
	}

	@Override
	public AccountDef findThisAgent(String product, String name, String branch, String agent, Integer year, BigDecimal janPol, BigDecimal janProd, BigDecimal febProd, BigDecimal febPol, BigDecimal marProd, BigDecimal marPol, BigDecimal aprProd, BigDecimal aprPol, BigDecimal mayProd, BigDecimal mayPol, BigDecimal junProd, BigDecimal junPol, BigDecimal julProd, BigDecimal julPol, BigDecimal augProd, BigDecimal augPol, BigDecimal sepProd, BigDecimal sepPol, BigDecimal octProd, BigDecimal octPol, BigDecimal novProd, BigDecimal novPol, BigDecimal decProd, BigDecimal decPol) throws BadRequestException {
		Predicate predicate=QAccountDef.accountDef.name.equalsIgnoreCase(agent);
		AccountDef accountDef=accountRepo.findOne(predicate);
		if(accountDef==null){
			UnloadedBudgets unloadedBudgets = new UnloadedBudgets();
			unloadedBudgets.setName(name);
			unloadedBudgets.setProductReportGroup(product);
			unloadedBudgets.setAccountDef(agent);
			unloadedBudgets.setBranch(branch);
			unloadedBudgets.setYear(year==null?null:year.toString());
			unloadedBudgets.setJanProd(janProd==null?null:janProd.toString());
			unloadedBudgets.setFebProd(febProd==null?null:febProd.toString());
			unloadedBudgets.setMarProd(marProd==null?null:marProd.toString());
			unloadedBudgets.setAprProd(aprProd==null?null:aprProd.toString());
			unloadedBudgets.setMayProd(mayProd==null?null:mayProd.toString());
			unloadedBudgets.setJunProd(junProd==null?null:junProd.toString());
			unloadedBudgets.setJulProd(julProd==null?null:julProd.toString());
			unloadedBudgets.setAugProd(augProd==null?null:augProd.toString());
			unloadedBudgets.setSepProd(sepProd==null?null:sepProd.toString());
			unloadedBudgets.setOctProd(octProd==null?null:octProd.toString());
			unloadedBudgets.setNovProd(novProd==null?null:novProd.toString());
			unloadedBudgets.setDecProd(decProd==null?null:decProd.toString());
			unloadedBudgets.setJanPol(janPol==null?null:janPol.toString());
			unloadedBudgets.setFebPol(febPol==null?null:febPol.toString());
			unloadedBudgets.setMarPol(marPol==null?null:marPol.toString());
			unloadedBudgets.setAprPol(aprPol==null?null:aprPol.toString());
			unloadedBudgets.setMayPol(mayPol==null?null:mayPol.toString());
			unloadedBudgets.setJunPol(junPol==null?null:junPol.toString());
			unloadedBudgets.setJulPol(julPol==null?null:julPol.toString());
			unloadedBudgets.setAugPol(augPol==null?null:augPol.toString());
			unloadedBudgets.setSepPol(sepPol==null?null:sepPol.toString());
			unloadedBudgets.setOctPol(octPol==null?null:octPol.toString());
			unloadedBudgets.setNovPol(novPol==null?null:novPol.toString());
			unloadedBudgets.setDecPol(decPol==null?null:decPol.toString());
			unloadedBudgets.setError("Input a valid user and ensure no blanks in the field");
			unloadedBudgetsRepo.save(unloadedBudgets);
			throw new BadRequestException("No User exists with that name,format yor Excel sheet well");
		}
		else{
			return accountRepo.findOne(predicate);
		}
	}

	@Override
	public DataTablesResult<CurrencyExchangeRatesDTO> findCurrencyExchangeRates(DataTablesRequest request, Long curCode) {
		final String search = ( request.getSearch()!=null && request.getSearch().getValue()!=null)?"%"+request.getSearch().getValue().toLowerCase()+"%":"%%";
		List<Object[]> rates = exchangeRateRepo.findExchangeRates(curCode, search.toLowerCase(),request.getPageNumber(), request.getPageSize());
		final List<CurrencyExchangeRatesDTO> exchangeRatesList = new ArrayList<>();
		long rowCount = 0l;
		if(!rates.isEmpty()) rowCount = (Integer)rates.get(0)[6];
		for(Object[] rate:rates){
			CurrencyExchangeRatesDTO exchangeRates = new CurrencyExchangeRatesDTO();
			exchangeRates.setCeCode(((BigDecimal)rate[0]).longValue());
			exchangeRates.setRate(((BigDecimal)rate[1]));
			exchangeRates.setCreatedDate(((Date)rate[2]));
			exchangeRates.setExchangeDate(((Date)rate[3]));
			exchangeRates.setCurrency(((String)rate[5]));
			exchangeRatesList.add(exchangeRates);
		}
		Page<CurrencyExchangeRatesDTO>  page = new PageImpl<>(exchangeRatesList,request, rowCount);
		return new DataTablesResult<>(request, page);
	}

	@Override
	public void createCurExchangeRate(CurrencyExchangeRatesDTO exchangeRates) throws BadRequestException {
		if(exchangeRates.getRate()==null){
			throw new BadRequestException("Exchange rate cannot be null");
		}
		if(exchangeRates.getRate().compareTo(BigDecimal.ZERO) <=0){
			throw new BadRequestException("Exchange Rate cannot be zero or less..");
		}
		final JdbcTemplate jdbcTemplate = new JdbcTemplate(this.dataSource);
		Long baseCurrency = jdbcTemplate.queryForObject("select sbc.cur_code  from sys_brk_organization sbo join sys_brk_currencies sbc on sbo.org_cur_code  = sbc.cur_code", Long.class);
		if(baseCurrency.longValue()==exchangeRates.getCurCode().longValue()){
			throw new BadRequestException("Exchange Rate cannot apply to base currrency...");
		}
		Long count = jdbcTemplate.queryForObject("select count(1) from sys_brk_currency_rates where ce_exchange_date = ? and ce_base_cur_code=? and ce_cur_code=?", Long.class, new Object[]{exchangeRates.getExchangeDate(),exchangeRates.getBaseCurCode(),exchangeRates.getCurCode()});
		if(count > 0){
			throw new BadRequestException("Exchange Rate for the day already exists...");
		}
		final Currencies basecurrencies = currRepo.findOne(exchangeRates.getBaseCurCode());
		if(basecurrencies==null){
			throw new BadRequestException("Base Currency is mandatory...");
		}
		final Currencies currencies = currRepo.findOne(exchangeRates.getCurCode());
		if(currencies==null){
			throw new BadRequestException("Currency is mandatory...");
		}
		final CurrencyExchangeRate exchangeRate = new CurrencyExchangeRate();
		exchangeRate.setRate(exchangeRates.getRate());
		exchangeRate.setCreatedDate(new Date());
		exchangeRate.setExchageDate(exchangeRates.getExchangeDate());
		exchangeRate.setCreatedBy(userUtils.getCurrentUser());
		exchangeRate.setCurrencies(currencies);
		exchangeRate.setCeCode(exchangeRates.getCeCode());
		exchangeRate.setBaseCurrency(basecurrencies);
		exchangeRateRepo.save(exchangeRate);
	}

	@Override
	public ProductReportGroup findThisProduct(String product, String name, String branch, String agent, Integer year, BigDecimal janPol, BigDecimal janProd, BigDecimal febProd, BigDecimal febPol, BigDecimal marProd, BigDecimal marPol, BigDecimal aprProd, BigDecimal aprPol, BigDecimal mayProd, BigDecimal mayPol, BigDecimal junProd, BigDecimal junPol, BigDecimal julProd, BigDecimal julPol, BigDecimal augProd, BigDecimal augPol, BigDecimal sepProd, BigDecimal sepPol, BigDecimal octProd, BigDecimal octPol, BigDecimal novProd, BigDecimal novPol, BigDecimal decProd, BigDecimal decPol) throws BadRequestException {
		Predicate predicate = QProductReportGroup.productReportGroup.repDesc.equalsIgnoreCase(product);
		ProductReportGroup productReportGroup = productReportRepo.findOne(predicate);
		if (productReportGroup == null) {
			UnloadedBudgets unloadedBudgets = new UnloadedBudgets();
			unloadedBudgets.setName(name);
			unloadedBudgets.setProductReportGroup(product);
			unloadedBudgets.setAccountDef(agent);
			unloadedBudgets.setBranch(branch);
			unloadedBudgets.setYear(year==null?null:year.toString());
			unloadedBudgets.setJanProd(janProd==null?null:janProd.toString());
			unloadedBudgets.setFebProd(febProd==null?null:febProd.toString());
			unloadedBudgets.setMarProd(marProd==null?null:marProd.toString());
			unloadedBudgets.setAprProd(aprProd==null?null:aprProd.toString());
			unloadedBudgets.setMayProd(mayProd==null?null:mayProd.toString());
			unloadedBudgets.setJunProd(junProd==null?null:junProd.toString());
			unloadedBudgets.setJulProd(julProd==null?null:julProd.toString());
			unloadedBudgets.setAugProd(augProd==null?null:augProd.toString());
			unloadedBudgets.setSepProd(sepProd==null?null:sepProd.toString());
			unloadedBudgets.setOctProd(octProd==null?null:octProd.toString());
			unloadedBudgets.setNovProd(novProd==null?null:novProd.toString());
			unloadedBudgets.setDecProd(decProd==null?null:decProd.toString());
			unloadedBudgets.setJanPol(janPol==null?null:janPol.toString());
			unloadedBudgets.setFebPol(febPol==null?null:febPol.toString());
			unloadedBudgets.setMarPol(marPol==null?null:marPol.toString());
			unloadedBudgets.setAprPol(aprPol==null?null:aprPol.toString());
			unloadedBudgets.setMayPol(mayPol==null?null:mayPol.toString());
			unloadedBudgets.setJunPol(junPol==null?null:junPol.toString());
			unloadedBudgets.setJulPol(julPol==null?null:julPol.toString());
			unloadedBudgets.setAugPol(augPol==null?null:augPol.toString());
			unloadedBudgets.setSepPol(sepPol==null?null:sepPol.toString());
			unloadedBudgets.setOctPol(octPol==null?null:octPol.toString());
			unloadedBudgets.setNovPol(novPol==null?null:novPol.toString());
			unloadedBudgets.setDecPol(decPol==null?null:decPol.toString());
			unloadedBudgets.setError("Input a valid product group and ensure no blanks in the field");
			unloadedBudgetsRepo.save(unloadedBudgets);
			throw new BadRequestException("No Product Group exists with that name,format yor Excel sheet well");

		}
		else{
			return productReportRepo.findOne(predicate);
		}
	}

	@Override
	public Page<SubclassDTO> selectSubclasses(String searchValue, Pageable pageable) {
		final String search = (searchValue!=null)?"%"+searchValue+"%":"%%";
		List<Object[]> subclasses = subClassRepo.findSearchSubclasses(search.toLowerCase(),pageable.getPageNumber(), pageable.getPageSize());
		final List<SubclassDTO> subclassDTOList = new ArrayList<>();
		long rowCount = 0L;
		if(!subclasses.isEmpty()) rowCount = (Integer)subclasses.get(0)[2];
		for(Object[] subclass:subclasses){
			final SubclassDTO subclassDTO = new SubclassDTO();
			subclassDTO.setSubId(((BigDecimal)subclass[0]).longValue());
			subclassDTO.setSubDesc((String) subclass[1]);
			subclassDTOList.add(subclassDTO);
		}
		return new PageImpl<>(subclassDTOList,pageable,rowCount);
	}

	@Override
	public Page<RevenueItemsDTO> selectAllRevenueItems(String term, Pageable pageable) {
		final String search = (term!=null)?"%"+term+"%":"%%";
		List<Object[]> revenueItems = revenueItemsRepo.findAllRevItems(search.toLowerCase(),pageable.getPageNumber(), pageable.getPageSize());
		final List<RevenueItemsDTO> revenueItemsList = new ArrayList<>();
		long rowCount = 0L;
		if(!revenueItems.isEmpty()) rowCount = (Integer)revenueItems.get(0)[2];
		for(Object[] obj:revenueItems){
			final RevenueItemsDTO revenueItem = new RevenueItemsDTO();
			revenueItem.setRevenueId(((BigDecimal)obj[0]).longValue());
			final String rev = (String) obj[1];
			revenueItem.setRevenueItem(RevenueItems.valueOf(rev).getValue());
			revenueItemsList.add(revenueItem);
		}
		return new PageImpl<>(revenueItemsList,pageable,rowCount);
	}
}
