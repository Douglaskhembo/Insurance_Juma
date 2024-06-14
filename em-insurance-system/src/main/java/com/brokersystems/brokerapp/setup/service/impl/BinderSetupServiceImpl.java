
package com.brokersystems.brokerapp.setup.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.brokersystems.brokerapp.claims.dtos.ClaimClaimantsDTO;
import com.brokersystems.brokerapp.enums.AccountTypeEnum;
import com.brokersystems.brokerapp.life.model.LifeCommissionRates;
import com.brokersystems.brokerapp.life.model.QLifeCommissionRates;
import com.brokersystems.brokerapp.life.repository.LifeCommissionRatesRepo;
import com.brokersystems.brokerapp.medical.model.*;
import com.brokersystems.brokerapp.medical.repository.*;
import com.brokersystems.brokerapp.server.utils.Streamable;
import com.brokersystems.brokerapp.server.utils.UserUtils;
import com.brokersystems.brokerapp.setup.dto.PremRatesDTO;
import com.brokersystems.brokerapp.setup.model.*;
import com.brokersystems.brokerapp.setup.repository.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brokersystems.brokerapp.enums.RevenueItems;
import com.brokersystems.brokerapp.server.datatables.DataTablesRequest;
import com.brokersystems.brokerapp.server.datatables.DataTablesResult;
import com.brokersystems.brokerapp.server.exception.BadRequestException;
import com.brokersystems.brokerapp.setup.service.BinderSetupService;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;

import javax.servlet.http.HttpServletResponse;

@Service
public class BinderSetupServiceImpl implements BinderSetupService {

	@Autowired
	private BindersRepo binderRepo;

	@Autowired
	private BinderDetRepo binderDetRepo;

	@Autowired
	private PremRatesRepo premRepo;

	@Autowired
	private AccountRepo accountRepo;

	@Autowired
	private CoverTypesRepo coverRepo;

	@Autowired
	private ProdSubclassRepo prodSubRepo;

	@Autowired
	private ProductsRepo prodRepo;

	@Autowired
	private CardTypesRepo cardTypesRepo;

	@Autowired
	private CurrencyRepository currencyRepo;

	@Autowired
	private SubClassCoverRepo subCoverRepo;

	@Autowired
	private SubCoverSectRepo subcoverSecRepo;

	@Autowired
	private BinderClauseRepo clauseRepo;

	@Autowired
	private SubClausesRepo subclausRepo;

	@Autowired
	private CommRatesRepo commRatesRepo;

	@Autowired
	private RevenueItemsRepo revenueItemsRepo;

	@Autowired
	private SubSectionRepo subSectionRepo;

	@Autowired
	private BinderSectPerilsRepo binderSectPerilsRepo;

	@Autowired
	private SectionRepo sectionRepo;

	@Autowired
	private BinderLoadingsRepo loadingsRepo;

	@Autowired
	private ServiceProviderContractRepo providerContractRepo;


	@Autowired
	private SubClassPerilsRepo subClassPerilsRepo;

	@Autowired
	private BinderRatesTblRepo binderRatesTblRepo;

	@Autowired
	private MedicalCoversRepo medicalCoversRepo;

	@Autowired
	private BinderReqrdDocsRepo reqrdDocsRepo;

	@Autowired
	private SubclassReqDocRepo subclassReqDocRepo;

	@Autowired
	private PremRatesTableRepo premRatesTableRepo;

	@Autowired
	private BinderExclusionsRepo exclusionsRepo;


	@Autowired
	private UserUtils userUtils;

	@Autowired
	private MedicalRulesRepo medicalRulesRepo;

	@Autowired
	private ChecksRepo checksRepo;

	@Autowired
	private MedicalRulesRepo rulesRepo;

	@Autowired
	private ProductChecksRepo productChecksRepo;

	@Autowired
	private SectionLimitsRepo sectionLimitsRepo;


	@Autowired
	private BinderMedicalCardsRepo binderCardsRepo;

	@Autowired
	private LifeCommissionRatesRepo lifeCommissionRatesRepo;

	@Autowired
	private BinderQuestionnaireChoicesRepo questionnaireChoicesRepo;

	@Autowired

	private BinderQuestionnaireRepo questionnaireRepo;


	@Override
	@Transactional(readOnly = true)
	public DataTablesResult<PremRatesDef> findDetPremRates(DataTablesRequest request, Long detCode)
			throws IllegalAccessException {
		BooleanExpression pred = QPremRatesDef.premRatesDef.binderDet.detId.eq(detCode);
		Page<PremRatesDef> page = premRepo.findAll(pred.and(request.searchPredicate(QPremRatesDef.premRatesDef)), request);
		return new DataTablesResult<>(request, page);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = {BadRequestException.class})
	public void createPremRates(PremRatesDef rates) throws BadRequestException {
		if (rates.getRatesApplicable() != null && "on".equalsIgnoreCase(rates.getRatesApplicable())) {
			rates.setRatesApplicable("Y");
		} else rates.setRatesApplicable("N");
		if (rates.getMandatory() != null && "on".equalsIgnoreCase(rates.getMandatory())) {
			rates.setMandatory("Y");
		} else rates.setMandatory("N");
		long count = premRepo.count(QPremRatesDef.premRatesDef.active.eq(true)
                .and(QPremRatesDef.premRatesDef.section.id.eq(rates.getSection().getId()))
				.and(QPremRatesDef.premRatesDef.binderDet.eq(rates.getBinderDet()))
				.and(QPremRatesDef.premRatesDef.ratesApplicable.eq("N")));
		if ("Y".equalsIgnoreCase(rates.getRatesApplicable()) && count > 0) {
			throw new BadRequestException("Cannot Create Premium With Rate Applicable when A rate not applicable record already exists...");
		}

		count = premRepo.count(QPremRatesDef.premRatesDef.active.eq(true)
                .and(QPremRatesDef.premRatesDef.section.id.eq(rates.getSection().getId()))
				.and(QPremRatesDef.premRatesDef.binderDet.eq(rates.getBinderDet()))
				.and(QPremRatesDef.premRatesDef.ratesApplicable.eq("Y")));
		if (("N".equalsIgnoreCase(rates.getRatesApplicable())||rates.getRatesApplicable() == null) && count > 0) {
			throw new BadRequestException("Cannot Create Premium With Rate Not Applicable when A rate applicable record already exists...");
		}

		if ("Y".equalsIgnoreCase(rates.getRatesApplicable())) {
			if (rates.getRangeFrom().compareTo(rates.getRangeTo()) == 1)
				throw new BadRequestException("Range from cannot be greater than Range to....");
		}
		if (rates.getRatesApplicable() != null && "Y".equalsIgnoreCase(rates.getRatesApplicable())) {
			if (rates.getRangeFrom().compareTo(BigDecimal.ZERO) == -1 || rates.getRangeTo().compareTo(BigDecimal.ZERO) == -1)
				throw new BadRequestException("Range From or Range To cannot be negative....");
			Predicate pred = QPremRatesDef.premRatesDef.binderDet.eq(rates.getBinderDet()).and(QPremRatesDef.premRatesDef.section.id.eq(rates.getSection().getId()))
					.and(QPremRatesDef.premRatesDef.rangeFrom.between(rates.getRangeFrom(), rates.getRangeTo())
							.or(QPremRatesDef.premRatesDef.rangeTo.between(rates.getRangeFrom(), rates.getRangeTo())));
			if (rates.getId() == null) {
				if (premRepo.count(pred) > 0) {
					throw new BadRequestException("Selected Section Premium Rates has already been setup");
				}
			} else {
				if (premRepo.count(pred) > 1) {
					throw new BadRequestException("Selected Section Premium Rates has already been setup");
				}
			}
		}else {
			 count = premRepo.count(QPremRatesDef.premRatesDef.active.eq(true)
					.and(QPremRatesDef.premRatesDef.section.id.eq(rates.getSection().getId()))
					.and(QPremRatesDef.premRatesDef.binderDet.eq(rates.getBinderDet()))
					.and(QPremRatesDef.premRatesDef.ratesApplicable.eq("N")));
			if (rates.getId() == null) {
				if (count > 0) {
					throw new BadRequestException("Cannot Create Premium.Record already exists...");
				}
			}else
			if (count > 1 && rates.isActive()) {
				throw new BadRequestException("Cannot Create Premium.Record already exists...");
			}
		}
		if (rates.getRate().compareTo(BigDecimal.ZERO) == 0 || rates.getRate().compareTo(BigDecimal.ZERO) == -1) {
			throw new BadRequestException("Premium Rate cannot be zero or less than Zero");
		}

		premRepo.save(rates);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<AccountDef> findInsuranceAccounts(String paramString, Pageable paramPageable) {
		Predicate pred = null;
		if (paramString == null || StringUtils.isBlank(paramString)) {
			pred = QAccountDef.accountDef.isNotNull().and(QAccountDef.accountDef.accountType.accountType.ne(AccountTypeEnum.IA));
		} else {
			pred = QAccountDef.accountDef.name.containsIgnoreCase(paramString).and(QAccountDef.accountDef.accountType.accountType.ne(AccountTypeEnum.IA));
		}
		return accountRepo.findAll(pred, paramPageable);
	}

	@Override
	@Transactional(readOnly = true)
	public DataTablesResult<BindersDef> findInsuranceBinders(DataTablesRequest request, Long accCode)
			throws IllegalAccessException {
		BooleanExpression pred = QBindersDef.bindersDef.account.acctId.eq(accCode);
		Page<BindersDef> page = binderRepo.findAll(pred.and(request.searchPredicate(QBindersDef.bindersDef)), request);
		return new DataTablesResult<>(request, page);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = {BadRequestException.class})
	public void createBinder(BindersDef binder) throws BadRequestException {
		if (binder.getFundBinder() == null) {
			binder.setFundBinder("N");
		} else if ("on".equalsIgnoreCase(binder.getFundBinder())) {
			if (binder.getBinId() == null) {
				if (binderRepo.count(QBindersDef.bindersDef.fundBinder.equalsIgnoreCase("Y")) > 0) {
					throw new BadRequestException("Default Medical Binder for Fund already defined");
				}
			} else {
				BindersDef bindersDef = binderRepo.findOne(binder.getBinId());
				if (bindersDef.getFundBinder() != null && "N".equalsIgnoreCase(bindersDef.getFundBinder())) {
					if (binderRepo.count(QBindersDef.bindersDef.fundBinder.equalsIgnoreCase("Y")) > 0) {
						throw new BadRequestException("Default Medical Binder for Fund already defined");
					}
				}

			}
			binder.setFundBinder("Y");
		} else binder.setFundBinder("N");

		if (binder.getProduct() == null) {
			throw new BadRequestException("Binder Product is compulsory");
		}
		if (!"MD".equalsIgnoreCase(binder.getProduct().getProGroup().getPrgType())) {
			if (binder.getFundBinder() != null && "Y".equalsIgnoreCase(binder.getFundBinder()))
				throw new BadRequestException("Fund is only applicable to Medical Product");
		}

		binderRepo.save(binder);

	}

	@Override
	public DataTablesResult<BinderQuestionnaire> findQuestionnaire(DataTablesRequest request, Long binCode) throws IllegalAccessException {
		BooleanExpression pred = QBinderQuestionnaire.binderQuestionnaire.binder.binId.eq(binCode);
		Page<BinderQuestionnaire> page = questionnaireRepo.findAll(pred.and(request.searchPredicate(QBinderQuestionnaire.binderQuestionnaire)), request);
		return new DataTablesResult<>(request, page);
	}


	@Override
	public DataTablesResult<BinderQuestionnaireChoices> findQuestionsChoices(DataTablesRequest request, Long quizId) throws IllegalAccessException {
		BooleanExpression pred = QBinderQuestionnaireChoices.binderQuestionnaireChoices.questions.bqdid.eq(quizId);
		Page<BinderQuestionnaireChoices> page = questionnaireChoicesRepo.findAll(pred.and(request.searchPredicate(QBinderQuestionnaireChoices.binderQuestionnaireChoices)), request);
		return new DataTablesResult<>(request, page);
	}


	@Override
	public void defineQuestionnaire(BinderQuestionnaire questionnaire) throws BadRequestException {
		if (questionnaire.getQuestionShtDesc() == null) {
			throw new BadRequestException("Provide question Id");
		}
		if (questionnaire.getQuestionname() == null) {
			throw new BadRequestException("Provide question");
		}
		if (questionnaire.getQuestionMandatory() == null) {
			questionnaire.setQuestionMandatory("N");
		}
		questionnaireRepo.save(questionnaire);
	}

	@Override
	public void defineQuestionnaireChoices(BinderQuestionnaireChoices questionnaireChoices) {
		questionnaireChoicesRepo.save(questionnaireChoices);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = {BadRequestException.class})
	public void cloneBinder(BindersDef binder) throws BadRequestException {
		BindersDef copyfrom = binderRepo.findOne(binder.getCloneFromBinCode());


		if (binder.getCloneProdId() == null) {
			throw new BadRequestException("Binder Product is compulsory");
		} else {
			binder.setProduct(prodRepo.findOne(binder.getCloneProdId()));
		}
		binder.setFundBinder(copyfrom.getFundBinder());
		if (!"MD".equalsIgnoreCase(binder.getProduct().getProGroup().getPrgType())) {
			if (binder.getFundBinder() != null && "Y".equalsIgnoreCase(binder.getFundBinder()))
				throw new BadRequestException("Fund is only applicable to Medical Product");
		}
		binder.setAccount(accountRepo.findOne(binder.getCloneAccId()));
		binder.setBinName(binder.getCloneBinName());
		binder.setBinPolNo(binder.getCloneBinPolNo());
		binder.setBinRemarks(binder.getCloneBinRemarks());
		binder.setBinShtDesc(binder.getCloneBinShtDesc());
		binder.setBinType(binder.getCloneBinType());
		binder.setCreatedDate(new Date());
		binder.setCurrency(copyfrom.getCurrency());
		BindersDef createdBin = binderRepo.save(binder);
		Iterable<BinderDetails> copyfromdetails = binderDetRepo.findAll(QBinderDetails.binderDetails.binder.binId.eq(binder.getCloneFromBinCode()));
		List<BinderDetails> newBinderDetails = new ArrayList<>();
		for (BinderDetails details : copyfromdetails) {
			BinderDetails newBinDetails = new BinderDetails();
			newBinDetails.setCreatedDate(new Date());
			newBinDetails.setBinder(binderRepo.findOne(createdBin.getBinId()));
			newBinDetails.setRemarks(details.getRemarks());
			newBinDetails.setSubCoverTypes(details.getSubCoverTypes());
			BinderDetails createdBinderDetails = binderDetRepo.save(newBinDetails);
			Iterable<PremRatesDef> premRatesDefs = premRepo.findAll(QPremRatesDef.premRatesDef.binderDet.detId.eq(details.getDetId()));
			List<PremRatesDef> premRt = new ArrayList<>();
			for (PremRatesDef premRates : premRatesDefs) {
				PremRatesDef newpremRatesDefs = new PremRatesDef();
				newpremRatesDefs.setCreatedDate(new Date());
				newpremRatesDefs.setActive(premRates.isActive());
				newpremRatesDefs.setBinderDet(binderDetRepo.findOne(createdBinderDetails.getDetId()));
				newpremRatesDefs.setDivFactor(premRates.getDivFactor());
				newpremRatesDefs.setFreeLimit(premRates.getFreeLimit());
				newpremRatesDefs.setMandatory(premRates.getMandatory());
				newpremRatesDefs.setMaxrate(premRates.getMaxrate());
				newpremRatesDefs.setMinPremium(premRates.getMinPremium());
				newpremRatesDefs.setMinRate(premRates.getMinRate());
				newpremRatesDefs.setMultiDivFactor(premRates.getMultiDivFactor());
				newpremRatesDefs.setMultiRate(premRates.getMultiRate());
				newpremRatesDefs.setProratedFull(premRates.getProratedFull());
				newpremRatesDefs.setRangeFrom(premRates.getRangeFrom());
				newpremRatesDefs.setRangeTo(premRates.getRangeTo());
				newpremRatesDefs.setRate(premRates.getRate());
				newpremRatesDefs.setRatesApplicable(premRates.getRatesApplicable());
				newpremRatesDefs.setRateType(premRates.getRateType());
				newpremRatesDefs.setSection(premRates.getSection());
				newpremRatesDefs.setSubSections(premRates.getSubSections());
				premRt.add(newpremRatesDefs);

			}
			Iterable<PremRatesTable> ratesTables = premRatesTableRepo.findAll(QPremRatesTable.premRatesTable.binderDetails.detId.eq(details.getDetId()));
			List<PremRatesTable> ratesTables1 = new ArrayList<>();
			for (PremRatesTable rateTable : ratesTables) {
				PremRatesTable newRateTable = new PremRatesTable();
				newRateTable.setBinderDetails(binderDetRepo.findOne(createdBinderDetails.getDetId()));
				newRateTable.setEffDate(rateTable.getEffDate());
				newRateTable.setFile(rateTable.getFile());
				newRateTable.setFileName(rateTable.getFileName());
				newRateTable.setRate_table(rateTable.getRate_table());
				ratesTables1.add(newRateTable);


			}
			Iterable<MedicalCovers> medicalCov = medicalCoversRepo.findAll(QMedicalCovers.medicalCovers.binderDet.detId.eq(details.getDetId()));
			List<MedicalCovers> medicalCoversList = new ArrayList<>();
			for (MedicalCovers medcovers : medicalCov) {
				MedicalCovers newMedCovers = new MedicalCovers();
				newMedCovers.setBinderDet(binderDetRepo.findOne(createdBinderDetails.getDetId()));
				newMedCovers.setMinPremium(medcovers.getMinPremium());
				newMedCovers.setSection(medcovers.getSection());
				newMedCovers.setApplicableAt(medcovers.getApplicableAt());
				newMedCovers.setDependsOnGender(medcovers.isDependsOnGender());
				newMedCovers.setFundCoverMand(medcovers.getFundCoverMand());
				newMedCovers.setGender(medcovers.getGender());
				newMedCovers.setLimitAmount(medcovers.getLimitAmount());
				newMedCovers.setMainCover(medcovers.getMainCover());
				newMedCovers.setMainSection(medcovers.getMainSection());
				newMedCovers.setProratedFull(medcovers.getProratedFull());
				newMedCovers.setWaitPeriod(medcovers.getWaitPeriod());
				medicalCoversList.add(newMedCovers);

			}
			Iterable<BinderReqrdDocs> reqrdDocs = reqrdDocsRepo.findAll(QBinderReqrdDocs.binderReqrdDocs.binderDetail.detId.eq(details.getDetId()));
			List<BinderReqrdDocs> binderReqrdDocs = new ArrayList<>();
			for (BinderReqrdDocs reqrdDocs1 : reqrdDocs) {
				BinderReqrdDocs newReqrsDocs = new BinderReqrdDocs();
				newReqrsDocs.setBinderDetail(binderDetRepo.findOne(createdBinderDetails.getDetId()));
				newReqrsDocs.setBrdId(reqrdDocs1.getBrdId());
				newReqrsDocs.setMandatory(reqrdDocs1.isMandatory());
				newReqrsDocs.setRequiredDocs(reqrdDocs1.getRequiredDocs());
				binderReqrdDocs.add(newReqrsDocs);

			}
			Iterable<BinderClauses> clauses = clauseRepo.findAll(QBinderClauses.binderClauses.binderDet.detId.eq(details.getDetId()));
			List<BinderClauses> binderClauses1 = new ArrayList<>();

			for (BinderClauses binderClauses : clauses) {
				BinderClauses newClauses = new BinderClauses();
				newClauses.setCreatedDate(new Date());
				newClauses.setBinderDet(binderDetRepo.findOne(createdBinderDetails.getDetId()));
				newClauses.setClauHeading(binderClauses.getClauHeading());
				newClauses.setMandatory(binderClauses.getMandatory());
				newClauses.setClause(binderClauses.getClause());
				newClauses.setClauWording(binderClauses.getClauWording());
				newClauses.setEditable(binderClauses.getEditable());
				binderClauses1.add(newClauses);

			}
			Iterable<CommissionRates> commRates = commRatesRepo.findAll(QCommissionRates.commissionRates.binderDet.detId.eq(details.getDetId()));
			List<CommissionRates> commissionRates1 = new ArrayList<>();
			for (CommissionRates commissionRates : commRates) {
				CommissionRates newCommRates = new CommissionRates();
				newCommRates.setCreatedDate(new Date());
				newCommRates.setBinderDet(binderDetRepo.findOne(createdBinderDetails.getDetId()));
				newCommRates.setActive(commissionRates.isActive());
				newCommRates.setCommDivFactor(commissionRates.getCommDivFactor());
				newCommRates.setCommRangeFrom(commissionRates.getCommRangeFrom());
				newCommRates.setRateType(commissionRates.getRateType());
				newCommRates.setCommRangeTo(commissionRates.getCommRangeTo());
				newCommRates.setCommRate(commissionRates.getCommRate());
				newCommRates.setRateDesc(commissionRates.getRateDesc());
				newCommRates.setRevenueItems(commissionRates.getRevenueItems());
				commissionRates1.add(newCommRates);

			}
			Iterable<BinderSectionPerils> perils = binderSectPerilsRepo.findAll(QBinderSectionPerils.binderSectionPerils.binderDetail.detId.eq(details.getDetId()));
			List<BinderSectionPerils> binderPerils = new ArrayList<>();
			for (BinderSectionPerils peril : perils) {
				BinderSectionPerils newPeril = new BinderSectionPerils();
				newPeril.setBinderDetail(binderDetRepo.findOne(createdBinderDetails.getDetId()));
				newPeril.setSectionsDef(peril.getSectionsDef());
				newPeril.setSubclassPeril(peril.getSubclassPeril());
				binderPerils.add(newPeril);

			}
			premRepo.save(premRt);
			premRatesTableRepo.save(ratesTables1);
			medicalCoversRepo.save(medicalCoversList);
			reqrdDocsRepo.save(binderReqrdDocs);
			clauseRepo.save(binderClauses1);
			commRatesRepo.save(commissionRates1);
			binderSectPerilsRepo.save(binderPerils);
		}
		Iterable<BinderRatesTable> binderRatesTables = binderRatesTblRepo.findAll(QBinderRatesTable.binderRatesTable.binder.binId.eq(binder.getCloneFromBinCode()));
		List<BinderRatesTable> newnewRateTables = new ArrayList<>();
		for (BinderRatesTable ratesTable : binderRatesTables) {
			BinderRatesTable newRateTable = new BinderRatesTable();
			newRateTable.setBinder(binderRepo.findOne(createdBin.getBinId()));
			newRateTable.setEffDate(ratesTable.getEffDate());
			newRateTable.setFile(ratesTable.getFile());
			newRateTable.setFileName(ratesTable.getFileName());
			newRateTable.setRate_table(ratesTable.getRate_table());
			newRateTable.setTableType(ratesTable.getTableType());
			newnewRateTables.add(newRateTable);
		}
		Iterable<MedicalBinderRules> binderRules = medicalRulesRepo.findAll(QMedicalBinderRules.medicalBinderRules.binder.binId.eq(binder.getCloneFromBinCode()));
		List<MedicalBinderRules> newBinderRules = new ArrayList<>();
		for (MedicalBinderRules binRules : binderRules) {
			MedicalBinderRules newBinRules = new MedicalBinderRules();
			newBinRules.setBinder(binderRepo.findOne(createdBin.getBinId()));
			newBinRules.setDesc(binRules.getDesc());
			newBinRules.setMandatory(binRules.getMandatory());
			newBinRules.setShtDesc(binRules.getShtDesc());
			newBinRules.setValue(binRules.getValue());
			newBinderRules.add(newBinRules);
		}
		Iterable<BinderExclusions> exclusions = exclusionsRepo.findAll(QBinderExclusions.binderExclusions.binder.binId.eq(binder.getCloneFromBinCode()));
		List<BinderExclusions> newExclusions = new ArrayList<>();
		for (BinderExclusions exclusions1 : exclusions) {
			BinderExclusions newExclusion = new BinderExclusions();
			newExclusion.setClausesDef(exclusions1.getClausesDef());
			newExclusion.setMedicalnetworks(exclusions1.getMedicalnetworks());
			newExclusion.setAilment(exclusions1.getAilment());
			newExclusion.setBinder(binderRepo.findOne(createdBin.getBinId()));
			newExclusion.setAilmentDesc(exclusions1.getAilmentDesc());
			newExclusion.setBenDesc(exclusions1.getBenDesc());
			newExclusion.setChronic(exclusions1.getChronic());
			newExclusion.setClauWording(exclusions1.getClauWording());
			newExclusion.setCostPerClaim(exclusions1.getCostPerClaim());
			newExclusion.setUpperLimit(exclusions1.getUpperLimit());
			newExclusion.setWaitingDays(exclusions1.getWaitingDays());
			newExclusions.add(newExclusion);
		}
		Iterable<BinderLoadings> loadings = loadingsRepo.findAll(QBinderLoadings.binderLoadings.binder.binId.eq(binder.getCloneFromBinCode()));
		List<BinderLoadings> newBinderLoading = new ArrayList<>();
		for (BinderLoadings load : loadings) {
			BinderLoadings newLoad = new BinderLoadings();
			newLoad.setBinder(binderRepo.findOne(createdBin.getBinId()));
			newLoad.setWaitingDays(load.getWaitingDays());
			newLoad.setUpperLimit(load.getUpperLimit());
			newLoad.setCostPerClaim(load.getCostPerClaim());
			newLoad.setChronic(load.getChronic());
			newLoad.setAilment(load.getAilment());
			newLoad.setAilmentDesc(load.getAilmentDesc());
			newLoad.setLoadingAmt(load.getLoadingAmt());
			newLoad.setRate(load.getRate());
			newLoad.setRateType(load.getRateType());
			newBinderLoading.add(newLoad);
		}
		Iterable<ServiceProviderContracts> contracts = providerContractRepo.findAll(QServiceProviderContracts.serviceProviderContracts.binder.binId.eq(binder.getCloneFromBinCode()));
		List<ServiceProviderContracts> newContracts = new ArrayList<>();
		for (ServiceProviderContracts contract : contracts) {
			ServiceProviderContracts newContract = new ServiceProviderContracts();
			newContract.setBinder(binderRepo.findOne(createdBin.getBinId()));
			newContract.setApprovalDate(contract.getApprovalDate());
			newContract.setApproved(contract.isApproved());
			newContract.setContractNo(contract.getContractNo());
			newContract.setContractType(contract.getContractType());
			newContract.setIssueDate(contract.getIssueDate());
			newContract.setNotes(contract.getNotes());
			newContract.setServiceProviders(contract.getServiceProviders());
			newContract.setStatus(contract.getStatus());
			newContract.setWefDate(contract.getWefDate());
			newContract.setWetDate(contract.getWetDate());
			newContracts.add(newContract);
		}

		medicalRulesRepo.save(newBinderRules);
		binderRatesTblRepo.save(newnewRateTables);
		exclusionsRepo.save(newExclusions);
		loadingsRepo.save(newBinderLoading);
		providerContractRepo.save(newContracts);


	}


	@Override
	@Transactional(readOnly = true)
	public DataTablesResult<BinderDetails> findBinderDetails(DataTablesRequest request, Long bindCode)
			throws IllegalAccessException {
		BooleanExpression pred = QBinderDetails.binderDetails.binder.binId.eq(bindCode);
		Page<BinderDetails> page = binderDetRepo.findAll(pred.and(request.searchPredicate(QBinderDetails.binderDetails)), request);
		return new DataTablesResult<>(request, page);
	}

	@Override
	public Page<BindersDef> findAllBinders(String paramString, Pageable paramPageable) {
		Predicate pred = null;
		if (paramString == null || StringUtils.isBlank(paramString)) {
			pred = QBindersDef.bindersDef.isNotNull().and(QBindersDef.bindersDef.active.eq(Boolean.TRUE));

		} else {
			pred = QBindersDef.bindersDef.binName.containsIgnoreCase(paramString)
					.or(QBindersDef.bindersDef.binShtDesc.containsIgnoreCase(paramString))
					.and(QBindersDef.bindersDef.active.eq(Boolean.TRUE));
		}
		return binderRepo.findAll(pred, paramPageable);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = {BadRequestException.class})
	public void createBinderDetails(BinderDetails det) throws BadRequestException {

		BinderDetails details = binderDetRepo.findOne(det.getDetId());
		if (details.getInstallmentsNo() != null) {
			if (det.getDistribution() == null)
				throw new BadRequestException("Provide Installment Percentages");

			if (!det.getDistribution().contains(":")) {
				try {
					BigDecimal val = new BigDecimal(det.getDistribution());
					if (val.compareTo(BigDecimal.valueOf(100)) != 0) {
						throw new BadRequestException("Percentage Value must be 100");
					}
					if (val.compareTo(BigDecimal.valueOf(100)) == 0 && det.getInstallmentsNo() != 1) {
						throw new BadRequestException("Percentage Value must match the number of installments");
					}
				} catch (NumberFormatException ex) {
					throw new BadRequestException("Invalid Number " + det.getDistribution());
				}
			}
			String[] percentages = det.getDistribution().split(":");
			if (percentages.length != det.getInstallmentsNo()) {
				throw new BadRequestException("Installment Percentage must match the number of installments...");
			}
			BigDecimal total = BigDecimal.ZERO;
			for (String perc : percentages) {
				try {
					BigDecimal dist = new BigDecimal(perc);
					if (dist.compareTo(BigDecimal.ZERO) == 0)
						throw new BadRequestException("Installment Percentage cannot be zero");
					total = total.add(dist);
				} catch (NumberFormatException ex) {
					throw new BadRequestException("Invalid Number " + perc);
				}
			}

			if (total.compareTo(BigDecimal.valueOf(100)) != 0) {
				throw new BadRequestException("Installment Percentage Value must add up to 100");
			}
		}
		details.setDistribution(det.getDistribution());
		details.setInstallmentsNo(det.getInstallmentsNo());
		details.setRemarks(det.getRemarks());
		details.setMinPrem(det.getMinPrem());
		if ("on".equalsIgnoreCase(det.getLimitsAllowed())) {
			details.setLimitsAllowed("Y");
		} else details.setLimitsAllowed("N");
		if ("on".equalsIgnoreCase(det.getSingleSectionCover())) {
			details.setSingleSectionCover("Y");
		} else details.setSingleSectionCover("N");
		if ("on".equalsIgnoreCase(det.getPrimary())) {
			details.setPrimary("Y");
		} else details.setPrimary("N");
		binderDetRepo.save(details);

	}

	@Override
	@Transactional(readOnly = true)
	public Page<ProductSubclasses> findProdSubclassSel(String paramString, Pageable paramPageable, Long prodCode) {
		Predicate pred = null;
		if (paramString == null || StringUtils.isBlank(paramString)) {
			pred = QProductSubclasses.productSubclasses.product.proCode.eq(prodCode).and(QProductSubclasses.productSubclasses.isNotNull());
		} else {
			pred = QProductSubclasses.productSubclasses.product.proCode.eq(prodCode).and(QProductSubclasses.productSubclasses.subclass.subDesc.containsIgnoreCase(paramString));
		}
		return prodSubRepo.findAll(pred, paramPageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<CoverTypesDef> findCoverTypesSel(String paramString, Pageable paramPageable, Long subCode) {
		Predicate pred = null;
		if (paramString == null || StringUtils.isBlank(paramString)) {
			pred = QCoverTypesDef.coverTypesDef.isNotNull();
		} else {
			pred = QCoverTypesDef.coverTypesDef.covName.containsIgnoreCase(paramString);
		}
		return coverRepo.findAll(pred, paramPageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<CardTypes> findCardTypesSel(String paramString, Pageable paramPageable, Long cardId) {
		Predicate pred = null;
		if (paramString == null || StringUtils.isBlank(paramString)) {
			pred = QCardTypes.cardTypes.isNotNull();
		} else {
			pred = QCardTypes.cardTypes.cardDesc.containsIgnoreCase(paramString);
		}
		return cardTypesRepo.findAll(pred, paramPageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<SubCoverTypeSections> findCoverSectionSel(String paramString, Pageable paramPageable, Long covCode) {
		Predicate pred = null;
		if (paramString == null || StringUtils.isBlank(paramString)) {
			pred = QSubCoverTypeSections.subCoverTypeSections.subcoverType.scId.eq(covCode).and(QSubCoverTypeSections.subCoverTypeSections.isNotNull());
		} else {
			pred = QSubCoverTypeSections.subCoverTypeSections.subcoverType.scId.eq(covCode).
					and(QSubCoverTypeSections.subCoverTypeSections.subcoverType.coverTypes.covName.containsIgnoreCase(paramString));
		}
		return subcoverSecRepo.findAll(pred, paramPageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ProductsDef> findProductsSel(String paramString, Pageable paramPageable) {
		Predicate pred = null;
		if (paramString == null || StringUtils.isBlank(paramString)) {
			pred = QProductsDef.productsDef.isNotNull();
		} else {
			pred = QProductsDef.productsDef.proDesc.containsIgnoreCase(paramString).or(QProductsDef.productsDef.proShtDesc.containsIgnoreCase(paramString));
		}
		return prodRepo.findAll(pred, paramPageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ProductsDef> findGrpProductsSel(String paramString, String grpType, Pageable paramPageable) {
		Predicate pred = null;
		System.out.println("grpType=" + grpType);
		if (grpType == null || StringUtils.isBlank(grpType)) {
			if (paramString == null || StringUtils.isBlank(paramString)) {
				pred = QProductsDef.productsDef.isNotNull();
			} else
				pred = QProductsDef.productsDef.proDesc.containsIgnoreCase(paramString).or(QProductsDef.productsDef.proShtDesc.containsIgnoreCase(paramString));
		} else {
			System.out.println("here=" + grpType);
			if ("M".equalsIgnoreCase(grpType))
				pred = QProductsDef.productsDef.proGroup.prgType.eq("MD").and(QProductsDef.productsDef.proDesc.containsIgnoreCase(paramString).or(QProductsDef.productsDef.proShtDesc.containsIgnoreCase(paramString)));
			else
				pred = QProductsDef.productsDef.proGroup.prgType.ne("MD").and(QProductsDef.productsDef.proDesc.containsIgnoreCase(paramString).or(QProductsDef.productsDef.proShtDesc.containsIgnoreCase(paramString)));
		}

		return prodRepo.findAll(pred, paramPageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Currencies> findActiveCurrencies(String paramString, Pageable paramPageable) {
		Predicate pred = null;
		if (paramString == null || StringUtils.isBlank(paramString)) {
			pred = QCurrencies.currencies.isNotNull();
		} else {
			pred = QCurrencies.currencies.curName.containsIgnoreCase(paramString).or(QCurrencies.currencies.curIsoCode.containsIgnoreCase(paramString));
		}
		return currencyRepo.findAll(pred, paramPageable);
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteBinder(long binCode) {
		binderRepo.delete(binCode);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Object[]> findUnassignedSubCoverTypes(Long prodCode, Long bindCode)
			throws IllegalAccessException {
		return binderDetRepo.getBinderSubclassCoverTypes(prodCode, bindCode);
	}

	@Override
	@Transactional(readOnly = false)
	public void createBinderDetails(BinderDetailsBean binderBean) {
		List<BinderDetails> binderDetails = new ArrayList<>();
		for (Long covtType : binderBean.getCoverTypes()) {
			BinderDetails binder = new BinderDetails();
			binder.setSubCoverTypes(subCoverRepo.findOne(covtType));
			binder.setBinder(binderRepo.findOne(binderBean.getBindCode()));
			binderDetails.add(binder);
		}
		binderDetRepo.save(binderDetails);

	}

	@Override
	@Transactional(readOnly = false)
	public void deleteBinderDetails(long detId) {
		binderDetRepo.delete(detId);
	}

	@Override
	@Transactional(readOnly = false)
	public void deletePremRates(long premId) {
		premRepo.delete(premId);
	}

	@Override
	@Transactional(readOnly = true)
	public DataTablesResult<BinderClauses> findBinderClauses(DataTablesRequest request, Long detCode)
			throws IllegalAccessException {
		BooleanExpression pred = QBinderClauses.binderClauses.binderDet.detId.eq(detCode);
		Page<BinderClauses> page = clauseRepo.findAll(pred.and(request.searchPredicate(QBinderClauses.binderClauses)), request);
		return new DataTablesResult<>(request, page);
	}

	@Override
	@Transactional(readOnly = true)
	public List<SubclassClauses> findUnassignedSubClauses(Long detCode, Long subCode, String subName)
			throws IllegalAccessException {
		if (subName != null)
			subName = StringUtils.lowerCase(subName);
		return clauseRepo.getUnassignedClauses(detCode, subCode, subName);
	}

	@Override
	@Transactional(readOnly = false)
	public void createBinderClauses(SubclassClauseBean subclassClause) {
		List<BinderClauses> binderClauses = new ArrayList<>();
		for (Long claCode : subclassClause.getClauses()) {
			BinderClauses clause = new BinderClauses();
			clause.setBinderDet(binderDetRepo.findOne(subclassClause.getDetCode()));
			clause.setClause(subclausRepo.findOne(claCode));
			binderClauses.add(clause);
		}

		clauseRepo.save(binderClauses);

	}

	@Override
	@Transactional(readOnly = false)
	public void deleteBinderClause(long clauseId) {
		clauseRepo.delete(clauseId);
	}

	@Override
	@Transactional(readOnly = true)
	public DataTablesResult<CommissionRates> findCommRates(DataTablesRequest request, Long detCode)
			throws IllegalAccessException {
		BooleanExpression pred = QCommissionRates.commissionRates.binderDet.detId.eq(detCode);
		Page<CommissionRates> page = commRatesRepo.findAll(pred.and(request.searchPredicate(QCommissionRates.commissionRates)), request);
		return new DataTablesResult<>(request, page);
	}


	@Override
	@Transactional(readOnly = true)
	public DataTablesResult<LifeCommissionRates> findLifeCommRates(DataTablesRequest request, Long premItemCode)
			throws IllegalAccessException {
		BooleanExpression pred = QLifeCommissionRates.lifeCommissionRates.premRatesDef.Id.eq(premItemCode);
		Page<LifeCommissionRates> page = lifeCommissionRatesRepo.findAll(pred.and(request.searchPredicate(QLifeCommissionRates.lifeCommissionRates)), request);
		return new DataTablesResult<>(request, page);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = {BadRequestException.class})
	public void createCommissionRates(CommissionRates commission) throws BadRequestException {
		if (commission.getRevenueItems() == null)
			throw new BadRequestException("Revenue item is mandatory");
		commRatesRepo.save(commission);

	}

	@Override
	@Transactional(readOnly = false, rollbackFor = {BadRequestException.class})
	public void createLifeCommissionRates(LifeCommissionRates commission) throws BadRequestException {
		if (commission.getPremRatesDef() == null)
			throw new BadRequestException("Revenue item is mandatory");
		lifeCommissionRatesRepo.save(commission);

	}

	@Override
	@Transactional(readOnly = false)
	public void deleteCommRates(long commId) {
		commRatesRepo.delete(commId);

	}

	@Override
	@Transactional(readOnly = false)
	public void deleteLifeCommRates(long commId) {
		lifeCommissionRatesRepo.delete(commId);

	}

	@Override
	@Transactional(readOnly = true)
	public Page<RevenueItemsDef> getCommRatesRevenueItems(String paramString, Pageable paramPageable, Long prodCode) {
//		ProductGroupDef productGroupDef =prodRepo.findOne(prodCode).getProGroup();
		Predicate pred = null;
		if (paramString == null || StringUtils.isBlank(paramString)) {
			pred = QRevenueItemsDef.revenueItemsDef.prodGroup.subId.eq(prodCode).and(QRevenueItemsDef.revenueItemsDef.item.eq(RevenueItems.UC).and(QRevenueItemsDef.revenueItemsDef.isNotNull()));
		} else {
			pred = QRevenueItemsDef.revenueItemsDef.prodGroup.subId.eq(prodCode).and(QRevenueItemsDef.revenueItemsDef.item.eq(RevenueItems.UC).and(QRevenueItemsDef.revenueItemsDef.item.stringValue().containsIgnoreCase(paramString)));
		}
		return revenueItemsRepo.findAll(pred, paramPageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<SectionsDef> findSubclassSections(String paramString, Pageable paramPageable, Long subCode) {
		Predicate pred = null;
		if (paramString == null || StringUtils.isBlank(paramString)) {
			pred = QSubclassSections.subclassSections.subclass.subId.eq(subCode).and(QSubclassSections.subclassSections.isNotNull());
		} else {
			pred = QSubclassSections.subclassSections.subclass.subId.eq(subCode).and(QSubclassSections.subclassSections.section.desc.containsIgnoreCase(paramString).
					or(QSubclassSections.subclassSections.section.shtDesc.containsIgnoreCase(paramString)));
		}
		List<SubclassSections> subclassSections = subSectionRepo.findAll(pred, paramPageable).getContent();
		List<SectionsDef> sections = new ArrayList<>();
		for (SubclassSections subclassSection : subclassSections) {
			SectionsDef sectionsDef = subclassSection.getSection();
			sections.add(sectionsDef);
		}
		Page<SectionsDef> sectionPage = new PageImpl<>(sections);
		return sectionPage;
	}

	@Override
	public Page<Checks> findBinderChecks(String paramString, Pageable paramPageable, Long bindCode) {
		Predicate pred = null;
		BindersDef bindersDef = binderRepo.findOne(bindCode);
		Iterable<MedicalBinderRules> binderRules = medicalRulesRepo.findAll(QMedicalBinderRules.medicalBinderRules.binder.binId.eq(bindCode));
		List<Long> checks = new ArrayList<>();
		if (binderRules.spliterator().getExactSizeIfKnown() > 0)
			checks = Streamable.streamOf(binderRules).filter(a -> a.getChecks() != null).map(a -> a.getChecks().getCheckId()).collect(Collectors.toList());
		if (paramString == null || StringUtils.isBlank(paramString)) {
			pred = QProductChecks.productChecks.checkId.notIn(checks).and(QProductChecks.productChecks.isNotNull()).and(QProductChecks.productChecks.product.proCode.eq(bindersDef.getProduct().getProCode()));
		} else {
			pred = QProductChecks.productChecks.checkId.notIn(checks).and(QProductChecks.productChecks.checks.checkName.containsIgnoreCase(paramString)).and(QProductChecks.productChecks.product.proCode.eq(bindersDef.getProduct().getProCode()));
		}
		List<ProductChecks> productChecks = productChecksRepo.findAll(pred, paramPageable).getContent();
		List<Checks> checkss = new ArrayList<>();
		for (ProductChecks productCheck : productChecks) {
			Checks checks1 = productCheck.getChecks();
			checks1.setProdCheckId(productCheck.getCheckId());
			checkss.add(checks1);
		}
		Page<Checks> checksPage = new PageImpl<>(checkss);
		return checksPage;
	}

	@Override
	@Transactional(readOnly = true)
	public DataTablesResult<BinderSectionPerils> findBinderSectionPerils(DataTablesRequest request, Long detCode, Long sectCode) throws IllegalAccessException {
		BooleanExpression pred = QBinderSectionPerils.binderSectionPerils.binderDetail.detId.eq(detCode).
				and(QBinderSectionPerils.binderSectionPerils.sectionsDef.id.eq(sectCode));
		Page<BinderSectionPerils> page = binderSectPerilsRepo.findAll(pred.and(request.searchPredicate(QBinderSectionPerils.binderSectionPerils)), request);
		return new DataTablesResult<>(request, page);
	}


	@Override
	@Transactional(readOnly = true)
	public List<SubclassPerils> findUnassignedPerils(Long detCode, Long subCode, Long sectCode, String perilName) throws IllegalAccessException {
		return binderSectPerilsRepo.getUnassignedPerils(detCode, subCode, sectCode, perilName);
	}

	@Override
	@Transactional(readOnly = false)
	public void createBinderSectPerils(BinderPerilsBean perilsBean) {
		List<BinderSectionPerils> binderSectPerils = new ArrayList<>();
		for (Long perilCode : perilsBean.getPerils()) {
			BinderSectionPerils sectionPeril = new BinderSectionPerils();
			sectionPeril.setBinderDetail(binderDetRepo.findOne(perilsBean.getBinderDetail()));
			sectionPeril.setSectionsDef(sectionRepo.findOne(perilsBean.getSectId()));
			sectionPeril.setSubclassPeril(subClassPerilsRepo.findOne(perilCode));
			binderSectPerils.add(sectionPeril);
		}
		binderSectPerilsRepo.save(binderSectPerils);
	}

	@Override
	@Transactional(readOnly = false)
	public void createBinderRequiredDocs(RequiredDocsBean docsBean) {
		List<BinderReqrdDocs> binderReqrdDocs = new ArrayList<>();
		for (Long docs : docsBean.getRequiredDocs()) {
			BinderReqrdDocs reqrdDocs = new BinderReqrdDocs();
			reqrdDocs.setBinderDetail(binderDetRepo.findOne(docsBean.getBinderDetail()));
			reqrdDocs.setRequiredDocs(subclassReqDocRepo.findOne(docs));
			reqrdDocs.setMandatory(false);
			binderReqrdDocs.add(reqrdDocs);
		}
		reqrdDocsRepo.save(binderReqrdDocs);
	}

	@Override
	public void updateBinderReqrdDocs(BinderReqrdDocs reqrdDocs) throws BadRequestException {
		reqrdDocs.setBinderDetail(reqrdDocsRepo.findOne(reqrdDocs.getBrdId()).getBinderDetail());
		reqrdDocsRepo.save(reqrdDocs);
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteBinderSectPeril(long sectPerilId) {
		binderSectPerilsRepo.delete(sectPerilId);
	}

	@Override
	@Transactional(readOnly = true)
	public DataTablesResult<BinderRatesTable> findBinderRates(DataTablesRequest request, Long bindCode) throws IllegalAccessException {
		BooleanExpression pred = QBinderRatesTable.binderRatesTable.binder.binId.eq(bindCode);
		Page<BinderRatesTable> page = binderRatesTblRepo.findAll(pred.and(request.searchPredicate(QBinderRatesTable.binderRatesTable)), request);
		return new DataTablesResult<>(request, page);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = {BadRequestException.class})
	public void defineBinderRatesTable(BinderRatesTable ratesTable) throws BadRequestException {
		ratesTable.setEffDate(new Date());
		ratesTable.setFileName(ratesTable.getFile().getOriginalFilename());
		binderRatesTblRepo.save(ratesTable);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = {BadRequestException.class})
	public void defineBinderPremRatesTable(PremRatesTable ratesTable) throws BadRequestException {
		ratesTable.setEffDate(new Date());
		ratesTable.setFileName(ratesTable.getFile().getOriginalFilename());
		premRatesTableRepo.save(ratesTable);

	}

	@Override
	@Transactional(readOnly = true)
	public DataTablesResult<MedicalCovers> findDetMedCovers(DataTablesRequest request, Long detCode) throws IllegalAccessException {
		BooleanExpression pred = QMedicalCovers.medicalCovers.binderDet.detId.eq(detCode);
		Page<MedicalCovers> page = medicalCoversRepo.findAll(pred.and(request.searchPredicate(QMedicalCovers.medicalCovers)), request);
		return new DataTablesResult<>(request, page);
	}

	@Override
	@Transactional(readOnly = true)
	public DataTablesResult<BinderMedicalCards> findDetBinCardTypes(DataTablesRequest request, Long detCode) throws IllegalAccessException {
		BooleanExpression pred = QBinderMedicalCards.binderMedicalCards.binder.binId.eq(detCode);
		Page<BinderMedicalCards> page = binderCardsRepo.findAll(pred.and(request.searchPredicate(QBinderMedicalCards.binderMedicalCards)), request);
		return new DataTablesResult<>(request, page);
	}

	@Override
	@Transactional(readOnly = true)
	public DataTablesResult<PremRatesDTO> findBinderPremRates(DataTablesRequest request, Long detCode) throws IllegalAccessException {
		List<Object[]> premratesList = premRatesTableRepo.findPremiumRatesTbl(detCode,request.getPageNumber(), request.getPageSize());
		List<PremRatesDTO> premRatesDTOList = new ArrayList<>();
		long rowCount = 0l;
		if(!premratesList.isEmpty()) rowCount = ((BigInteger)premratesList.get(0)[3]).intValue();
		for(Object[] premrate:premratesList){
			PremRatesDTO premRatesDTO = new PremRatesDTO();
			premRatesDTO.setId(((BigInteger)premrate[0]).longValue());
			premRatesDTO.setEffDate((Date) premrate[1]);
			premRatesDTO.setFileName((String) premrate[2]);
			premRatesDTOList.add(premRatesDTO);
		}
		Page<PremRatesDTO>  page = new PageImpl<>(premRatesDTOList,request, rowCount);
		return new DataTablesResult<>(request, page);
	}

	@Override
	@Transactional(readOnly = true)
	public DataTablesResult<BinderReqrdDocs> findBinderReqDocs(DataTablesRequest request, Long detCode) throws IllegalAccessException {
		BooleanExpression pred = QBinderReqrdDocs.binderReqrdDocs.binderDetail.detId.eq(detCode);
		Page<BinderReqrdDocs> page = reqrdDocsRepo.findAll(pred.and(request.searchPredicate(QBinderReqrdDocs.binderReqrdDocs)), request);
		return new DataTablesResult<>(request, page);
	}


	@Override
	@Transactional(readOnly = false, rollbackFor = {BadRequestException.class})
	public void createBinderCardTypes(BinderMedicalCards medicalCards) throws BadRequestException {
		if (medicalCards.getId() == null) {
			if (medicalCards.getCardTypes() == null)
				throw new BadRequestException("Select card type to continue...");
			if (medicalCards.getWefDate() == null)
				throw new BadRequestException("Select Date From to continue...");
			Long count = binderCardsRepo.count(QBinderMedicalCards.binderMedicalCards.cardTypes.cardId.eq(medicalCards.getCardTypes().getCardId())
					.and(QBinderMedicalCards.binderMedicalCards.binder.binId.eq(medicalCards.getBinder().getBinId())));
			if (count > 0) {
				Iterable<BinderMedicalCards> bincards = binderCardsRepo.findAll(QBinderMedicalCards.binderMedicalCards.cardTypes.cardId.eq(medicalCards.getCardTypes().getCardId())
						.and(QBinderMedicalCards.binderMedicalCards.binder.binId.eq(medicalCards.getBinder().getBinId())));
				for (BinderMedicalCards cards : bincards) {

					if (medicalCards.getWefDate().before(cards.getWefDate())) {
						if (medicalCards.getWetDate() == null)
							throw new BadRequestException("Enter Date To for this card type to continue ");

						if (medicalCards.getWetDate().after(cards.getWefDate()))
							throw new BadRequestException("Date To overlaps with the existing card types ");

					}
					if (cards.getWetDate() == null) {
						throw new BadRequestException("Enter Date To for the existing card types before adding a new card type...");

					}
					if (medicalCards.getWefDate().before(cards.getWetDate()) && medicalCards.getWefDate().after(cards.getWefDate())) {
						throw new BadRequestException("Date From for the new card type is overlapping with existing card types...");

					}


				}
			}
		} else {
			BinderMedicalCards existingCard = binderCardsRepo.findOne(medicalCards.getId());
			medicalCards.setCardTypes(existingCard.getCardTypes());
		}
		if (medicalCards.getWetDate() == null)
			medicalCards.setWetDate(null);


		binderCardsRepo.save(medicalCards);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = {BadRequestException.class})
	public void createMedicalCover(MedicalCovers medicalCover) throws BadRequestException {
		if (medicalCover.getMainCover() == null || StringUtils.isBlank(medicalCover.getMainCover()))
			throw new BadRequestException("Select Main Cover Option to continue..");
		if (medicalCover.isDependsOnGender()) {
			if (medicalCover.getGender() == null) {
				throw new BadRequestException("Select Gender to continue....");
			}
		}
		String fundBinder = "N";
		if (medicalCover.getBinderDet().getBinder().getFundBinder() != null) {
			fundBinder = medicalCover.getBinderDet().getBinder().getFundBinder();
		}
		String fundCover = "N";
		if ("Y".equalsIgnoreCase(fundBinder)) {
			if (medicalCover.getFundCoverMand() != null && "on".equalsIgnoreCase(medicalCover.getFundCoverMand())) {
				fundCover = "Y";
			}
		}
		if (medicalCover.getId() == null) {
			if (medicalCover.getSection() == null)
				throw new BadRequestException("Select Cover to continue...");
			if ("Y".equalsIgnoreCase(medicalCover.getMainCover())) {
				medicalCover.setMainSection(medicalCover.getSection());
			} else if ("N".equalsIgnoreCase(medicalCover.getMainCover())) {
				if (medicalCover.getMainSection() != null) {
					if (medicalCover.getMainSection().getId() == medicalCover.getSection().getId()) {
						throw new BadRequestException("Cover and Main Cover cannot be the Same.....");
					}
				} else throw new BadRequestException("Select Main Cover to continue..");
			}
			Long count = medicalCoversRepo.count(QMedicalCovers.medicalCovers.binderDet.detId.eq(medicalCover.getBinderDet().getDetId()).and(QMedicalCovers.medicalCovers.section.id.eq(medicalCover.getSection().getId())));
			if (count > 0) throw new BadRequestException("Cover Details already exists..");
			if ("N".equalsIgnoreCase(medicalCover.getMainCover())) {
				Long mainCount = medicalCoversRepo.count(QMedicalCovers.medicalCovers.binderDet.detId.eq(medicalCover.getBinderDet().getDetId())
						.and(QMedicalCovers.medicalCovers.section.id.eq(medicalCover.getMainSection().getId()))
						.and(QMedicalCovers.medicalCovers.mainCover.eq("Y")));
				if (mainCount == 0)
					throw new BadRequestException("The main section selected has not been setup..Select another main section");
			}
			medicalCover.setFundCoverMand(fundCover);
		} else {
			MedicalCovers covers = medicalCoversRepo.findOne(medicalCover.getId());
			medicalCover.setMainSection(covers.getMainSection());
			medicalCover.setSection(covers.getSection());
			medicalCover.setBinderDet(covers.getBinderDet());
			medicalCover.setFundCoverMand(fundCover);
		}
		if (medicalCover.getApplicableAt() == null) throw new BadRequestException("Select Applicable At");
		//if(medicalCover.getLimitAmount()==null) throw new BadRequestException("Select Limit Amount");

		medicalCoversRepo.save(medicalCover);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = {BadRequestException.class})
	public void deleteMedCover(long coverId) throws BadRequestException {
		MedicalCovers covers = medicalCoversRepo.findOne(coverId);
		if (("Y".equalsIgnoreCase(covers.getMainCover()))) {
			Long count = medicalCoversRepo.count(QMedicalCovers.medicalCovers.mainSection.id.eq(covers.getSection().getId()));
			if (count > 1)
				throw new BadRequestException("Cannot delete Main Cover. There are dependent covers on this cover");
		}
		medicalCoversRepo.delete(coverId);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = {BadRequestException.class})
	public void deleteBinderCard(long binCardId) throws BadRequestException {
		binderCardsRepo.delete(binCardId);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = {BadRequestException.class})
	public void updateBinderClause(BinderClauses binderClauses) throws BadRequestException {
		if (binderClauses.getMandatory() != null && "on".equalsIgnoreCase(binderClauses.getMandatory())) {
			binderClauses.setMandatory("Y");
		} else binderClauses.setMandatory("N");
		if (binderClauses.getEditable() != null && "on".equalsIgnoreCase(binderClauses.getEditable())) {
			binderClauses.setEditable("Y");
		} else binderClauses.setEditable("N");
		binderClauses.setBinderDet(clauseRepo.findOne(binderClauses.getClauId()).getBinderDet());
		clauseRepo.save(binderClauses);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Object[]> findUnassignedReqDocs(Long subCode, Long detCode) throws IllegalAccessException {
		return reqrdDocsRepo.getUnassignedReqDocs(subCode, detCode);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = {BadRequestException.class})
	public void deleteBinderReqDoc(long docId) throws BadRequestException {
		reqrdDocsRepo.delete(docId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Object[]> findClausesDef(String paramString, long subId, long sectCode) {
		System.out.println("Sect Code " + sectCode);
		return sectionLimitsRepo.getUnassignedClauses(sectCode, subId);
	}

	@Override
	@Transactional(readOnly = true)
	public DataTablesResult<SectionLimits> findPremRatesSectionLimits(DataTablesRequest request, Long premId) throws IllegalAccessException {
		BooleanExpression pred = QSectionLimits.sectionLimits.premRatesDef.Id.eq(premId);
		Page<SectionLimits> page = sectionLimitsRepo.findAll(pred.and(request.searchPredicate(QSectionLimits.sectionLimits)), request);
		return new DataTablesResult<>(request, page);
	}

	@Override
	public void createPremLimits(ClausesBean docsBean) {
		List<SectionLimits> sectionLimitses = new ArrayList<>();
		System.out.println(docsBean.getClauses());
		for (Long clId : docsBean.getClauses()) {
			System.out.println("Clause id " + clId);
			SectionLimits sectionLimits = new SectionLimits();
			sectionLimits.setClausesDef(subclausRepo.findOne(clId));
			sectionLimits.setPremRatesDef(premRepo.findOne(docsBean.getPremRatesId()));
			sectionLimitses.add(sectionLimits);
		}
		sectionLimitsRepo.save(sectionLimitses);
	}

	@Override
	public void updatePremLimits(SectionLimits limits) throws BadRequestException {
		sectionLimitsRepo.save(limits);
	}

	@Override
	public void deletePremLimits(long limitId) throws BadRequestException {
		sectionLimitsRepo.delete(limitId);
	}

	@Override
	public void deleteQuestion(Long quizCode) throws BadRequestException {
		Iterable<BinderQuestionnaireChoices> choices = questionnaireChoicesRepo.findAll(QBinderQuestionnaireChoices.binderQuestionnaireChoices.questions.bqdid.eq(quizCode));
		questionnaireChoicesRepo.delete(choices);
		questionnaireRepo.delete(quizCode);
	}

	@Override
	public void deleteQuestionChoice(Long choiceCode) throws BadRequestException {
		questionnaireChoicesRepo.delete(choiceCode);
	}

	@Override
	public List<SingleQuizModel> findAllQuestions(Long binCode) throws IllegalAccessException {
		List<SingleQuizModel> questionChoiceModels = new ArrayList<>();
		BooleanExpression pred = QBinderQuestionnaire.binderQuestionnaire.binder.binId.eq(binCode);
		Iterable<BinderQuestionnaire> binderQuestions = questionnaireRepo.findAll(pred, new Sort(Sort.Direction.ASC, "questionOrder"));
		for (BinderQuestionnaire questions : binderQuestions) {
			SingleQuizModel singleQuizModel = new SingleQuizModel();
			singleQuizModel.setName(questions.getQuestionname());
			singleQuizModel.setType(questions.getQuestiontype());
			singleQuizModel.setQuizCode(questions.getBqdid());

			if (questions.getQuestionMandatory() != null && questions.getQuestionMandatory().equalsIgnoreCase("Y")) {
				singleQuizModel.setIsRequired("True");
			} else {
				singleQuizModel.setIsRequired("False");
			}
			if (questions.getTriggerByQuiz() != null) {
				if (questions.getTriggerByQuizAnswer() != null) {
					singleQuizModel.setVisibleIf("{" + questions.getTriggerByQuiz().getQuestionname() + "}=" + questions.getTriggerByQuizAnswer().getChoice());
				}
			}

			Iterable<BinderQuestionnaireChoices> QuestionsChoices = questionnaireChoicesRepo.findAll(QBinderQuestionnaireChoices.binderQuestionnaireChoices.questions.bqdid.eq(questions.getBqdid()));
			Long size = QuestionsChoices.spliterator().getExactSizeIfKnown();
			String[] arr = new String[size.intValue()];
			int i = 0;
			for (BinderQuestionnaireChoices questionChoices : QuestionsChoices) {
				arr[i] = questionChoices.getChoice();
				i++;
			}
			singleQuizModel.setChoices(arr);
			questionChoiceModels.add(singleQuizModel);
		}
		return questionChoiceModels;
	}

	@Override
	@Transactional(readOnly = true)
	public Page<BinderQuestionnaire> findBinderQuestions(String paramString, Long binCode, Pageable paramPageable) {
		Predicate pred = null;
		if (paramString == null || StringUtils.isBlank(paramString)) {
			pred = QBinderQuestionnaire.binderQuestionnaire.isNotNull().and(QBinderQuestionnaire.binderQuestionnaire.binder.binId.eq(binCode));
		} else {
			pred = QBinderQuestionnaire.binderQuestionnaire.questionname.containsIgnoreCase(paramString).and(QBinderQuestionnaire.binderQuestionnaire.binder.binId.eq(binCode));
		}
		return questionnaireRepo.findAll(pred, paramPageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<BinderQuestionnaireChoices> findBinderQuestionChoice(String paramString, Long quizCode, Pageable paramPageable) {
		Predicate pred = null;
		if (paramString == null || StringUtils.isBlank(paramString)) {
			pred = QBinderQuestionnaireChoices.binderQuestionnaireChoices.isNotNull().and(QBinderQuestionnaireChoices.binderQuestionnaireChoices.questions.bqdid.eq(quizCode));
		} else {
			pred = QBinderQuestionnaireChoices.binderQuestionnaireChoices.choice.containsIgnoreCase(paramString).and(QBinderQuestionnaireChoices.binderQuestionnaireChoices.questions.bqdid.eq(quizCode));
		}
		return questionnaireChoicesRepo.findAll(pred, paramPageable);
	}

	@Override
	public void makeReady(Long binId) {
		BindersDef bindersDef = binderRepo.findOne(binId);
		String status = "Ready";
		bindersDef.setBinStatus(status);
		binderRepo.save(bindersDef);
	}

	@Override
	public void makeBinUndo(Long binId) {
		BindersDef bindersDef = binderRepo.findOne(binId);
		String status = null;
		bindersDef.setBinStatus(status);
		binderRepo.save(bindersDef);
	}

	@Override
	public void makeBinAuthorise(Long binId) {
		BindersDef bindersDef = binderRepo.findOne(binId);
		String status = "Authorised";
		bindersDef.setBinStatus(status);
		binderRepo.save(bindersDef);
	}

	@Override
	public void makeBinUnAuthorise(Long id) {
		BindersDef bindersDef = binderRepo.findOne(id);
		String status = "Unauthorised";
		bindersDef.setBinStatus(status);
		binderRepo.save(bindersDef);
	}

	@Override
	public BindersDef getDeactivate(Long id) {
		return binderRepo.findOne(id);
	}

	@Override
	public void downloadRatesTable(Long id, HttpServletResponse response) throws BadRequestException {
	PremRatesTable premRatesTable=  premRatesTableRepo.findOne(id);
		if (premRatesTable!=null) {
			String name = premRatesTable.getFileName().substring(0,premRatesTable.getFileName().indexOf("."))+".xls";
			final String excel = premRatesTable.getRate_table();//Here.......   extract byte data from resultSet
			if(excel==null)
				throw new BadRequestException("Unable to get Premium Template to Download");
            // copy it to response's OutputStream
			try {
			byte[] bytes = Files.readAllBytes(new File(excel).toPath());
			response.setHeader("Content-Disposition", "attachment; filename=\"" + name);
			response.setContentType("application/vnd.ms-excel");

			OutputStream os = response.getOutputStream();
			os.write(bytes);
			os.flush();
			os.close();
			} catch (IOException ex) {
				ex.printStackTrace();
				//log.info("Error writing file to output stream. Filename was '{}'", fileName, ex);
				throw new RuntimeException("IOError writing file to output stream");
			}
		}
	}

}
