package com.brokersystems.brokerapp.uw.service.impl;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

import com.brokersystems.brokerapp.kie.rules.GeneralTransRulesExecutor;
import com.brokersystems.brokerapp.life.model.PolicyBenefitsDistribution;
import com.brokersystems.brokerapp.life.model.PolicyInstallments;
import com.brokersystems.brokerapp.life.model.QPolicyBenefitsDistribution;
import com.brokersystems.brokerapp.life.model.QPolicyInstallments;
import com.brokersystems.brokerapp.life.repository.PolicyBenefitsDistributionRepo;
import com.brokersystems.brokerapp.life.repository.PolicyInstallmentsRepo;
import com.brokersystems.brokerapp.schedules.model.QScheduleTrans;
import com.brokersystems.brokerapp.schedules.model.ScheduleBean;
import com.brokersystems.brokerapp.schedules.model.ScheduleTrans;
import com.brokersystems.brokerapp.schedules.repository.ScheduleTransRepo;
import com.brokersystems.brokerapp.schedules.service.ScheduleService;
import com.brokersystems.brokerapp.server.utils.*;
import com.brokersystems.brokerapp.setup.model.*;
import com.brokersystems.brokerapp.setup.repository.PremRatesTableRepo;
import com.brokersystems.brokerapp.setup.service.ParamService;
import com.brokersystems.brokerapp.trans.model.QTransChecks;
import com.brokersystems.brokerapp.trans.repository.TransChecksRepo;
import com.brokersystems.brokerapp.trans.service.ReceiptService;
import com.brokersystems.brokerapp.uw.model.*;
import com.brokersystems.brokerapp.uw.repository.*;
import com.brokersystems.brokerapp.webservices.portalmodel.Product;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brokersystems.brokerapp.enums.RevenueItems;
import com.brokersystems.brokerapp.enums.SectionTypes;
import com.brokersystems.brokerapp.server.exception.BadRequestException;
import com.brokersystems.brokerapp.setup.repository.AccountRepo;
import com.brokersystems.brokerapp.setup.repository.ShortPeriodRepo;
import com.brokersystems.brokerapp.uw.service.PolicyTransService;
import com.brokersystems.brokerapp.uw.service.PremComputeService;

@Service
public class PremComputeServiceImpl implements PremComputeService {
	
	@Autowired
	private PolicyTransRepo policyRepo;
	
	@Autowired
	private RiskTransRepo riskRepo;
	
	@Autowired
	private SectionTransRepo sectionRepo;

	@Autowired
	private PolTaxesRepo polTaxesRepo;
	
	@Autowired
	private PolicyTransService policyService;
	
	@Autowired
	private ShortPeriodRepo shortPeriodRepo;

	@Autowired
	private PremExcelUtils premExcelUtils;

	@Autowired
	private PremRatesTableRepo premRatesTableRepo;

	@Autowired
	private ScheduleService scheduleService;

	@Autowired
	private ScheduleTransRepo scheduleTransRepo;

	@Autowired
	private ReceiptService receiptService;

	@Autowired
	private ParamService paramService;

	@Autowired
	private PolicyInstallmentsRepo policyInstallmentsRepo;

	@Autowired
	private LifeExcelUtils lifeExcelUtils;


	@Autowired
	private GeneralTransRulesExecutor rulesExecutor;

	@Autowired
	private PolActiveRisksRepo activeRisksRepo;

	@Autowired
	private PolicyBenefitsDistributionRepo maturityRepo;

	@Autowired
	private PolicyBindersRepo policyBindersRepo;


	/**
	 * Premium Computation at policy level
	 */
	@Override
	@Modifying
	@Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
	public void computePrem(Long polCode) throws BadRequestException, IOException {
		PolicyTrans policy = policyRepo.findOne(polCode);
		policyService.populateTaxes(policy);
		ProductsDef product = policy.getProduct();
		Iterable<RiskTrans> risks = riskRepo.findAll(QRiskTrans.riskTrans.policy.policyId.eq(policy.getPolicyId()));
		Iterable<PolicyTaxes> policyTaxes = polTaxesRepo.findAll(QPolicyTaxes.policyTaxes.policy.policyId.eq(policy.getPolicyId()));
		BigDecimal premium = BigDecimal.ZERO;
		BigDecimal fullpremium = BigDecimal.ZERO;
		BigDecimal sumInsured = BigDecimal.ZERO;
		BigDecimal totalCommission =BigDecimal.ZERO;
		BigDecimal totalSubAgentComm = BigDecimal.ZERO;
		BigDecimal polstampDuty = BigDecimal.ZERO;
		BigDecimal polphfFund = BigDecimal.ZERO;
		BigDecimal polwhtxAmt = BigDecimal.ZERO;
		BigDecimal polextras = BigDecimal.ZERO;
		BigDecimal polTl = BigDecimal.ZERO;
		BigDecimal totSuminsured = BigDecimal.ZERO;
		BigDecimal totalPrem = BigDecimal.ZERO;
		BigDecimal totalFapPrem = BigDecimal.ZERO;
		BigDecimal sumPolicytaxAmount = BigDecimal.ZERO;
		AccountTypes accType = ( policy.getAgent()!=null)?policy.getAgent().getAccountType():null;
		boolean multiProduct = policyBindersRepo.count(QPolicyBinders.policyBinders.policyTrans.policyId.eq(policy.getPolicyId())) > 0;
		long calDays = TimeUnit.DAYS.convert((policy.getWetDate().getTime() -policy.getWefDate().getTime()) , TimeUnit.MILLISECONDS)+1;
//		long totalInstallment = policy.getTotalInstalments();
		boolean cashBasis = policy.getInterfaceType()!=null && "C".equalsIgnoreCase(policy.getInterfaceType());
		 BigDecimal futurePrem = BigDecimal.ZERO;
		for(RiskTrans risk:risks){
			 BigDecimal riskFuturePrem = BigDecimal.ZERO;
			Iterable<SectionTrans> sections  = sectionRepo.findAll(QSectionTrans.sectionTrans.risk.riskId.eq(risk.getRiskId()));
			BigDecimal riskPrem = BigDecimal.ZERO;
			BigDecimal riskInsured = BigDecimal.ZERO;
			List<PremiumItemsBean> itemsBeen = new ArrayList<>();
			List<PremiumItemsBean> discountItems = new ArrayList<>();
			final String ratesTable = premRatesTableRepo.getRatesLocation(risk.getBinderDetails().getDetId());
			if (ratesTable == null)
				throw new BadRequestException("Rates Table for the Binder has not been setup");
			ScheduleBean scheduleBean = scheduleService.generateScheduleColumns(risk.getRiskId());
			long count = 0;
			if(scheduleBean.getMappings()!=null) {
				count = scheduleBean.getMappings().stream().filter(a -> a.getPremItem() != null && "Y".equalsIgnoreCase(a.getPremItem())).count();
			}
			if(count > 0) {
				if (scheduleTransRepo.count(QScheduleTrans.scheduleTrans.risk.riskId.eq(risk.getRiskId())) > 1)
					throw new BadRequestException("Cannot compute premium. You have entered mutliple schedule details...");
			}
			long counter = count;
			for(SectionTrans section:sections){
				List<String> items = new ArrayList<>();
				double minPrem = 0;
				if(scheduleBean.getMappings()!=null) {
					scheduleBean.getMappings().stream().filter(a -> a.getPremItem() != null && "Y".equalsIgnoreCase(a.getPremItem()))
							.filter(a -> a.getPremCode() != null && a.getPremCode() == section.getSection().getId()).forEach(a -> {
						if (counter > 0) {
							ScheduleTrans scheduleTrans = scheduleTransRepo.findOne(QScheduleTrans.scheduleTrans.risk.riskId.eq(risk.getRiskId()));
							if (scheduleTrans != null)
								switch (a.getKey()) {
									case "1":
										if (scheduleTrans.getColumn1() != null)
											items.add(scheduleTrans.getColumn1());
										break;
									case "2":
										if (scheduleTrans.getColumn2() != null)
											items.add(scheduleTrans.getColumn2());
										break;
									case "3":
										if (scheduleTrans.getColumn3() != null)
											items.add(scheduleTrans.getColumn3());
										break;
									case "4":
										if (scheduleTrans.getColumn4() != null)
											items.add(scheduleTrans.getColumn4());
										break;
									case "5":
										if (scheduleTrans.getColumn5() != null)
											items.add(scheduleTrans.getColumn5());
										break;
									case "6":
										if (scheduleTrans.getColumn6() != null)
											items.add(scheduleTrans.getColumn6());
										break;
									case "7":
										if (scheduleTrans.getColumn7() != null)
											items.add(scheduleTrans.getColumn7());
										break;
									case "8":
										if (scheduleTrans.getColumn8() != null)
											items.add(scheduleTrans.getColumn8());
										break;
									case "9":
										if (scheduleTrans.getColumn9() != null)
											items.add(scheduleTrans.getColumn9());
										break;
									case "10":
										if (scheduleTrans.getColumn10() != null)
											items.add(scheduleTrans.getColumn10());
										break;
									case "11":
										if (scheduleTrans.getColumn11() != null)
											items.add(scheduleTrans.getColumn11());
										break;
									case "12":
										if (scheduleTrans.getColumn12() != null)
											items.add(scheduleTrans.getColumn12());
										break;
									case "13":
										if (scheduleTrans.getColumn13() != null)
											items.add(scheduleTrans.getColumn13());
										break;
									case "14":
										if (scheduleTrans.getColumn14() != null)
											items.add(scheduleTrans.getColumn14());
										break;
									case "15":
										if (scheduleTrans.getColumn15() != null)
											items.add(scheduleTrans.getColumn15());
										break;
								}
						}

					});

					if (section.getPremRates() != null) {
						PremRatesDef premRatesDef = section.getPremRates();
						if (premRatesDef.getMinPremium() != null && premRatesDef.getMinPremium().compareTo(BigDecimal.ZERO) == 1) {
							minPrem = premRatesDef.getMinPremium().doubleValue();
						}
					}
				}
				if(section==null){
					throw new BadRequestException("Premium Sections have not been mapped correctly to the rates table. Contact Your System Administrator");
				}
				PremiumItemsBean itemsBean= new PremiumItemsBean(section.getSection().getShtDesc(),
						(section.getRate()!=null)?section.getRate().doubleValue():0d,
						section.getFreeLimit()!=null?section.getFreeLimit().doubleValue():0d,minPrem,
						section.getAmount()!=null?section.getAmount().doubleValue():0d,
						section.getSectId(),section.getDivFactor()!=null?section.getDivFactor().doubleValue():0d,
						section.getSection().getType().getCode(),section.getSection().getType().getOrder(),  items,policy.getPolicyId());
				if(product.getAgeApplicable()!=null && "Y".equalsIgnoreCase(product.getAgeApplicable())) {
					if(risk.getInsured().getDob()!=null)
					itemsBean.setAge(DateUtilities.computeAge(risk.getInsured().getDob()));
				}
				if(!section.getSection().getType().equals(SectionTypes.DS)){
					itemsBeen.add(itemsBean);
				}
				else if(section.getSection().getType().equals(SectionTypes.DS)){
					discountItems.add(itemsBean);
				}




			}

			itemsBeen.stream().sorted(Comparator.comparing(PremiumItemsBean::getOrder));
			PremiumResultBean resultBean =premExcelUtils.getPremium(itemsBeen,ratesTable);
			Optional<PremiumItemsBean> premiumItemsBean = discountItems.stream().filter(a -> a.getSectType().equalsIgnoreCase("DS")).findFirst();
			riskPrem = BigDecimal.valueOf(resultBean.getPremium());
			if(premiumItemsBean.isPresent()){
				PremiumItemsBean itemsBean = premiumItemsBean.get();
				final SectionTrans  section = sectionRepo.findOne(itemsBean.getSectId());
				final BigDecimal discountPrem = BigDecimal.valueOf(itemsBean.getRate()).multiply(riskPrem).divide(BigDecimal.valueOf(itemsBean.getDivFactor()),2, RoundingMode.HALF_EVEN);
				section.setCalcprem(discountPrem.negate());
				section.setPrem(discountPrem.negate());
				sectionRepo.save(section);
				riskPrem = riskPrem.subtract(discountPrem);
			}

			sumInsured = BigDecimal.valueOf(resultBean.getSumInsured());
			riskInsured = sumInsured;
			premium = riskPrem;
			fullpremium = BigDecimal.valueOf(resultBean.getPremiumFull());
			riskFuturePrem = fullpremium;


			String proratedFull = risk.getProrata();
			if(!cashBasis) {
				if ("F".equalsIgnoreCase(proratedFull)) {
					riskPrem = riskPrem.multiply(BigDecimal.ONE);
					premium = premium.multiply(BigDecimal.ONE);
				} else if ("S".equalsIgnoreCase(proratedFull)) {
					long datediff = risk.getWetDate().getTime() - risk.getWefDate().getTime();
					long daysDiff = TimeUnit.DAYS.convert(datediff, TimeUnit.MILLISECONDS) + 1;
					List<ShortPeriodRates> periodRates = shortPeriodRepo.getShortPeriodRates(daysDiff);
					if (periodRates.size() == 0)
						throw new BadRequestException("Short Period Rates For the Cover Period has not been setup");
					if (periodRates.size() > 1)
						throw new BadRequestException("More than one Short Period Rates For the Cover Period have been setup");
					ShortPeriodRates rates = periodRates.get(0);
					riskPrem = riskPrem.multiply(rates.getRate().divide(rates.getDivFactor()));
					premium = premium.multiply(rates.getRate().divide(rates.getDivFactor()));
				} else if ("P".equalsIgnoreCase(proratedFull)) {
					long datediff = risk.getWetDate().getTime() - risk.getWefDate().getTime();
					long daysDiff = TimeUnit.DAYS.convert(datediff, TimeUnit.MILLISECONDS) + 1;
					double rata = (double) daysDiff / calDays;
					BigDecimal prorata = new BigDecimal(rata).setScale(9, BigDecimal.ROUND_HALF_EVEN);
					riskPrem = riskPrem.multiply(prorata).setScale(2, BigDecimal.ROUND_HALF_EVEN);
					premium = premium.multiply(prorata).setScale(2, BigDecimal.ROUND_HALF_EVEN);
				}
			}

			riskPrem = riskPrem.add(fullpremium);
			premium = premium.add(fullpremium);

			if(risk.getBinderDetails().getMinPrem()!=null && risk.getBinderDetails().getMinPrem().compareTo(riskPrem) ==1){
				riskPrem = risk.getBinderDetails().getMinPrem();
				premium = risk.getBinderDetails().getMinPrem();
			}

			if(risk.getButchargePrem()==null || risk.getButchargePrem().compareTo(BigDecimal.ZERO)==0){ }
			else
			{
				riskPrem=risk.getButchargePrem().setScale(2, BigDecimal.ROUND_HALF_EVEN);
				premium =risk.getButchargePrem().setScale(2, BigDecimal.ROUND_HALF_EVEN);
			}

			totSuminsured = totSuminsured.add(riskInsured);
			if(risk.getPremium()!=null && risk.getPremium().compareTo(riskPrem)!=0){
				risk.setInstallAmount(null);
			}
			risk.setPremium(riskPrem);
			risk.setCalcPremium(riskPrem);
			risk.setSumInsured(riskInsured);
			BigDecimal comm = BigDecimal.ZERO;
			BigDecimal subAgentComm = BigDecimal.ZERO;
			if(risk.getCommRate()!=null) {
				comm = riskPrem.multiply(risk.getCommRate()).divide(BigDecimal.valueOf(100));
				if(policy.getSubAgent()!=null){
					if(policy.getSubAgent().getAccountType().getCommRate()==null || policy.getSubAgent().getAccountType().getCommRate().compareTo(BigDecimal.ZERO)==0){
						throw new BadRequestException("Sub Agent Commission Rate not setup");
					}
						subAgentComm = comm.multiply(policy.getSubAgent().getAccountType().getCommRate().divide(BigDecimal.valueOf(100)));


				}
			}


			BigDecimal sumRisktaxAmount = BigDecimal.ZERO;
			BigDecimal riskstampDuty = BigDecimal.ZERO;
			BigDecimal riskphfFund = BigDecimal.ZERO;
			BigDecimal riskwhtxAmt = BigDecimal.ZERO;
			BigDecimal riskextras = BigDecimal.ZERO;
			BigDecimal riskTl = BigDecimal.ZERO;
			BigDecimal cashBasisPrem = premium;

			BinderDetails coverTypes = risk.getBinderDetails();
			String distribution = coverTypes.getDistribution();
			String firstInstallment = "";

			if (distribution != null && distribution.contains(":")) {
				firstInstallment = distribution.split(":")[0];
			} else firstInstallment = distribution;

			if(distribution==null){
				firstInstallment= "100";
			}

			if(risk.getInstallmentPerc()!=null && cashBasis && !firstInstallment.equalsIgnoreCase("100")){
				BigDecimal perc = BigDecimal.ONE;
				BigDecimal finstalment = BigDecimal.ZERO;



				try {
					 perc =new BigDecimal(risk.getInstallmentPerc()).divide(BigDecimal.valueOf(100));
					 finstalment = new BigDecimal(firstInstallment).divide(BigDecimal.valueOf(100));
//					 if(perc.compareTo(finstalment)==-1){
//					 	throw new BadRequestException("Instalment Percentage is below the required percentage of "+firstInstallment);
//					 }


				}
				catch (NumberFormatException ex){
					throw new BadRequestException("Invalid Instalment Percentage");
				}

				Integer installmentNo = coverTypes.getInstallmentsNo();
				if (installmentNo == null) installmentNo = 1;
				if(distribution==null){
					installmentNo=1;
					risk.setInstallAmount(null);
				}
				cashBasisPrem = cashBasisPrem.multiply(perc);
				policy.setTotalInstalments(installmentNo);
				BigDecimal finstallmentPrem = premium.multiply(finstalment);


				if(risk.getInstallAmount()==null){
					risk.setInstallAmount(cashBasisPrem);
				}

				if(risk.getInstallAmount()!=null){
					cashBasisPrem = risk.getInstallAmount();
				}

				if(cashBasisPrem.compareTo(finstallmentPrem)==-1){
					throw new BadRequestException("Instalment Amount is below the required Minimum amount of "+finstallmentPrem+"....."+cashBasisPrem);
				}

				if(risk.getInstallAmount().compareTo(premium)==1){
					throw new BadRequestException("Installment amount is above the the maximum premium required for this risk of "+premium);
				}

				if (risk.getInstallAmount().compareTo(premium) < 0) {
					risk.setWetDate(org.apache.commons.lang.time.DateUtils.addDays(DateUtils.addMonths(risk.getWefDate(), 1), -1));
				}
				else if (risk.getInstallAmount().compareTo(premium) == 0){
					policy.setTotalInstalments(1);
				}

				cashBasisPrem = risk.getInstallAmount();

				BigDecimal percentage = cashBasisPrem.divide(premium, 2, RoundingMode.HALF_EVEN).multiply(BigDecimal.valueOf(100));
				risk.setInstallmentPerc(String.valueOf(percentage));
				policy.setInstallmentNo(1l);
				risk.setTotalPercentage(percentage);



				comm = cashBasisPrem.multiply(risk.getCommRate()).divide(BigDecimal.valueOf(100));
				if(policy.getSubAgent()!=null){
					BigDecimal commRate = BigDecimal.ZERO;
//					if(policy.getSubAgent().getAccountType().getBindersDef()!=null && policy.getSubAgent().getAccountType().getBindersDef().getBinId() ==policy.getBinder().getBinId() && policy.getSubAgent().getSubAccountTypes().getCommRate().compareTo(BigDecimal.ZERO)!=0){
//						commRate = policy.getSubAgent().getAccountType().getCommRate();
//					}
//					else
//					if(policy.getSubAgent().getSubAccountTypes()!=null && policy.getSubAgent().getSubAccountTypes().getCommRate().compareTo(BigDecimal.ZERO)!=0){
//						commRate = policy.getSubAgent().getSubAccountTypes().getCommRate();
//					}
					if(commRate.compareTo(BigDecimal.ZERO)==0) {
						if (policy.getSubAgent().getAccountType().getCommRate() != null || policy.getSubAgent().getAccountType().getCommRate().compareTo(BigDecimal.ZERO) != 0) {
							commRate = policy.getSubAgent().getAccountType().getCommRate();
						}
					}
					if(commRate.compareTo(BigDecimal.ZERO)==0){
						throw new BadRequestException("Sub Agent Commission Rate not setup");
					}
					subAgentComm = comm.multiply(commRate.divide(BigDecimal.valueOf(100)));


				}
			}
			if(riskPrem.compareTo(BigDecimal.ZERO)==1){
				comm = comm.negate();
				subAgentComm = subAgentComm.negate();
			}
			else if(riskPrem.compareTo(BigDecimal.ZERO)==-1){
				comm = comm.abs();
				subAgentComm = subAgentComm.abs();
			}
			risk.setCommAmt(comm);
			risk.setSubAgentComm(subAgentComm);
			totalCommission = totalCommission.add(comm);
			totalSubAgentComm = totalSubAgentComm.add(subAgentComm);

			
			for(PolicyTaxes policyTax:policyTaxes){
				BigDecimal computedTax = calculateTax(cashBasisPrem, policyTax.getTaxRate(), policyTax.getDivFactor(),policyTax.getRateType());
				policyTax.setTaxAmount(computedTax);
				sumPolicytaxAmount = sumPolicytaxAmount.add(computedTax);
				if(policyTax.getRevenueItems().getItem()== RevenueItems.EX){
					if("R".equalsIgnoreCase(policyTax.getTaxLevel())){
						riskextras = riskextras.add(computedTax);
						sumRisktaxAmount = sumRisktaxAmount.add(computedTax);
					}
				}
				else if(policyTax.getRevenueItems().getItem() == RevenueItems.SD){
					if("R".equalsIgnoreCase(policyTax.getTaxLevel())){
						riskstampDuty = riskstampDuty.add(computedTax);
						sumRisktaxAmount = sumRisktaxAmount.add(computedTax);
					}
				}
				else if(policyTax.getRevenueItems().getItem() == RevenueItems.PHCF){
					if("R".equalsIgnoreCase(policyTax.getTaxLevel())){
						riskphfFund = riskphfFund.add(computedTax);
						sumRisktaxAmount = sumRisktaxAmount.add(computedTax);
					}
				}
				else if(policyTax.getRevenueItems().getItem() ==RevenueItems.WHTX){
					if("R".equalsIgnoreCase(policyTax.getTaxLevel())){
						riskwhtxAmt = riskwhtxAmt.add(computedTax);
						sumRisktaxAmount = sumRisktaxAmount.add(computedTax);
					}
				}
				else if(policyTax.getRevenueItems().getItem()==RevenueItems.TL){
					if("R".equalsIgnoreCase(policyTax.getTaxLevel())){
						riskTl = riskTl.add(computedTax);
						sumRisktaxAmount = sumRisktaxAmount.add(computedTax);
					}
				}
			}

			if(multiProduct) {
				if (accType == null) {
					accType = (risk.getPolicyBinders().getBinder().getAccount().getAccountType() != null) ? risk.getPolicyBinders().getBinder().getAccount().getAccountType() : null;
				}
			}
			BigDecimal riskWhtx = BigDecimal.ZERO;

			if (accType!=null && accType.isWhtxAppl()) {
				if (accType.getWhtaxVal().compareTo(BigDecimal.ZERO) == 1) {
					riskWhtx = accType.getWhtaxVal().divide(new BigDecimal(100)).multiply(comm);
					risk.setWhtax(riskWhtx);
				}
			}

			
			risk.setNetpremium(cashBasisPrem.add(sumRisktaxAmount).subtract(comm));
			risk.setExtras(riskextras);

			risk.setPhfFund(riskphfFund);
			risk.setTrainingLevy(riskTl);
			risk.setStampDuty(riskstampDuty);
			risk.setFuturePrem(riskFuturePrem);
			totalPrem = totalPrem.add(cashBasisPrem);
			futurePrem = futurePrem.add(riskFuturePrem);
			polwhtxAmt = polwhtxAmt.add(riskWhtx);
		}
		BigDecimal policyLevelCommRate = BigDecimal.ZERO;
		if(totalPrem.compareTo(BigDecimal.ZERO)!=0)
		  policyLevelCommRate = totalCommission.divide(totalPrem, 2, RoundingMode.HALF_EVEN);

		if(!multiProduct) {
			if (policy.getBinder().getMinPrem() != null) {
				if (policy.getBinder().getMinPrem().compareTo(totalPrem) == 1) {
					totalPrem = policy.getBinder().getMinPrem();
					totalCommission = policyLevelCommRate.multiply(totalPrem);
					if (policy.getSubAgent() != null) {
						BigDecimal commRate = BigDecimal.ZERO;
//						if(policy.getSubAgent().getSubAccountTypes().getBindersDef()!=null && policy.getSubAgent().getSubAccountTypes().getBindersDef().getBinId() ==policy.getBinder().getBinId() && policy.getSubAgent().getSubAccountTypes().getCommRate().compareTo(BigDecimal.ZERO)!=0){
//							commRate = policy.getSubAgent().getSubAccountTypes().getCommRate();
//						}
						if(commRate.compareTo(BigDecimal.ZERO)==0) {
							if (policy.getSubAgent().getAccountType().getCommRate() != null || policy.getSubAgent().getAccountType().getCommRate().compareTo(BigDecimal.ZERO) != 0) {
								commRate = policy.getSubAgent().getAccountType().getCommRate();
							}
						}
						if(commRate.compareTo(BigDecimal.ZERO)==0){
							throw new BadRequestException("Sub Agent Commission Rate not setup");
						}
						totalSubAgentComm = totalCommission.multiply(commRate.divide(BigDecimal.valueOf(100)));

					}
				}
			}

			if (policy.getProduct().getMinPrem() != null) {
				if (policy.getProduct().getMinPrem().compareTo(totalPrem) == 1) {
					totalPrem = policy.getProduct().getMinPrem();
					totalCommission = policyLevelCommRate.multiply(totalPrem);
					if (policy.getSubAgent() != null) {
						BigDecimal commRate = BigDecimal.ZERO;
//						if(policy.getSubAgent().getSubAccountTypes().getBindersDef()!=null && policy.getSubAgent().getSubAccountTypes().getBindersDef().getBinId() ==policy.getBinder().getBinId() && policy.getSubAgent().getSubAccountTypes().getCommRate().compareTo(BigDecimal.ZERO)!=0){
//							commRate = policy.getSubAgent().getSubAccountTypes().getCommRate();
//						}
						if(commRate.compareTo(BigDecimal.ZERO)==0) {
							if (policy.getSubAgent().getAccountType().getCommRate() != null || policy.getSubAgent().getAccountType().getCommRate().compareTo(BigDecimal.ZERO) != 0) {
								commRate = policy.getSubAgent().getAccountType().getCommRate();
							}
						}
						if(commRate.compareTo(BigDecimal.ZERO)==0){
							throw new BadRequestException("Sub Agent Commission Rate not setup");
						}
						totalSubAgentComm = totalCommission.multiply(commRate.divide(BigDecimal.valueOf(100)));
					}

				}
			}
		}
		
		polTaxesRepo.save(policyTaxes);
		BigDecimal whtxAmt = BigDecimal.ZERO;
		if(!multiProduct) {
			if (accType!=null && accType.isWhtxAppl()) {
				if (accType.getWhtaxVal().compareTo(BigDecimal.ZERO) == 1) {
					whtxAmt = accType.getWhtaxVal().divide(new BigDecimal(100)).multiply(totalCommission);
					polwhtxAmt = whtxAmt.negate();
				}
			}
		}

		if(StringUtils.equalsIgnoreCase(paramService.getParameterString("SUB_AGENT_COMM_PARAM"),"N")){
			if(policy.getSubAgent()!=null) {
				BigDecimal commRate = BigDecimal.ZERO;
//				if(policy.getSubAgent().getSubAccountTypes().getBindersDef()!=null && policy.getSubAgent().getSubAccountTypes().getBindersDef().getBinId() ==policy.getBinder().getBinId() && policy.getSubAgent().getSubAccountTypes().getCommRate().compareTo(BigDecimal.ZERO)!=0){
//					commRate = policy.getSubAgent().getSubAccountTypes().getCommRate();
//				}

				if(commRate.compareTo(BigDecimal.ZERO)==0) {
					if (policy.getSubAgent().getAccountType().getCommRate() != null || policy.getSubAgent().getAccountType().getCommRate().compareTo(BigDecimal.ZERO) != 0) {
						commRate = policy.getSubAgent().getAccountType().getCommRate();
					}
				}
				if(commRate.compareTo(BigDecimal.ZERO)==0){
					throw new BadRequestException("Sub Agent Commission Rate not setup");
				}
			  totalSubAgentComm =	(totalCommission.abs().subtract(whtxAmt.abs())).multiply(commRate.divide(BigDecimal.valueOf(100)));
			}
		}

		if(totalPrem.compareTo(BigDecimal.ZERO)==1){
			totalCommission = totalCommission.abs().negate();
			totalSubAgentComm = totalSubAgentComm.abs().negate();
		}
		else if(totalPrem.compareTo(BigDecimal.ZERO)==-1){
			totalCommission = totalCommission.abs();
			totalSubAgentComm = totalSubAgentComm.abs();
		}
		
		sumPolicytaxAmount = sumPolicytaxAmount.add(polwhtxAmt);
		 BigDecimal futureTotalTax = BigDecimal.ZERO;
		for(PolicyTaxes policyTax:policyTaxes){
			BigDecimal computedTax = calculateTax(totalPrem, policyTax.getTaxRate(), policyTax.getDivFactor(),policyTax.getRateType());
			BigDecimal computedFutureTax = calculateTax(futurePrem, policyTax.getTaxRate(), policyTax.getDivFactor(),policyTax.getRateType());
			if(policyTax.getRevenueItems().getItem()== RevenueItems.EX){
				
				polextras = polextras.add(computedTax);
				futureTotalTax = futureTotalTax.add(computedFutureTax);
			}
			else if(policyTax.getRevenueItems().getItem() == RevenueItems.SD){
				
				polstampDuty = polstampDuty.add(computedTax);

			}
			else if(policyTax.getRevenueItems().getItem() == RevenueItems.PHCF){
				
				polphfFund = polphfFund.add(computedTax);
				futureTotalTax = futureTotalTax.add(computedFutureTax);

			}
			else if(policyTax.getRevenueItems().getItem()==RevenueItems.TL){
				
				polTl = polTl.add(computedTax);
				futureTotalTax = futureTotalTax.add(computedFutureTax);

			}
		}
		riskRepo.save(risks);
		if(multiProduct) {
			Iterable<PolicyBinders> policyBinders = policyBindersRepo.findAll(QPolicyBinders.policyBinders.policyTrans.policyId.eq(polCode));
			for(PolicyBinders binders:policyBinders){
				BigDecimal totalBinderRisk =Streamable.streamOf(riskRepo.findAll(QRiskTrans.riskTrans.policyBinders.policyBindId.eq(binders.getPolicyBindId()))).map(a -> a.getCalcPremium()).reduce(BigDecimal.ZERO,BigDecimal::add);
				BigDecimal totaltl =Streamable.streamOf(riskRepo.findAll(QRiskTrans.riskTrans.policyBinders.policyBindId.eq(binders.getPolicyBindId()))).map(a -> a.getTrainingLevy()).reduce(BigDecimal.ZERO,BigDecimal::add);
				BigDecimal totalPhcf =Streamable.streamOf(riskRepo.findAll(QRiskTrans.riskTrans.policyBinders.policyBindId.eq(binders.getPolicyBindId()))).map(a -> a.getPhfFund()).reduce(BigDecimal.ZERO,BigDecimal::add);
				BigDecimal totalComm =Streamable.streamOf(riskRepo.findAll(QRiskTrans.riskTrans.policyBinders.policyBindId.eq(binders.getPolicyBindId()))).map(a -> a.getCommAmt()).reduce(BigDecimal.ZERO,BigDecimal::add);
				BigDecimal totalWhtx =Streamable.streamOf(riskRepo.findAll(QRiskTrans.riskTrans.policyBinders.policyBindId.eq(binders.getPolicyBindId()))).map(a -> ( a.getWhtax()!=null)?a.getWhtax():BigDecimal.ZERO).reduce(BigDecimal.ZERO,BigDecimal::add);
				binders.setBasicPrem(totalBinderRisk);
				binders.setCommission(totalComm);
				binders.setTl(totaltl);
				binders.setPhcf(totalPhcf);
				binders.setWhtx(totalWhtx);
			}
			policyBindersRepo.save(policyBinders);
		}
		Currencies currencies = policy.getTransCurrency();
		totalPrem = totalPrem.setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN);
		totalCommission = totalCommission.setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN);
		totalSubAgentComm = totalSubAgentComm.setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN);
		policy.setPremium(totalPrem);
		policy.setBasicPrem((totalPrem.add(polextras).add(polstampDuty).add(polphfFund).add(polTl)).setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
		policy.setEndosbasicPremium(totalPrem);
		policy.setEndosgrossPremium((totalPrem.add(polextras).add(polstampDuty).add(polphfFund).add(polTl)).setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
		policy.setPaidPremium(receiptService.getPolicyTotalRcptAmount(policy.getPolNo()));
		policy.setEndosCommissions(totalCommission);
		policy.setCommAmt(totalCommission);
		policy.setSubAgentComm(totalSubAgentComm);
		policy.setExtras(polextras.setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
		policy.setWhtx(polwhtxAmt.setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
		policy.setPhcf(polphfFund.setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
		policy.setTrainingLevy(polTl.setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
		policy.setTotTrainingLevy(polTl.setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
		policy.setTotPhcf(polphfFund.setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
		policy.setPolTotComm(totalCommission);
		policy.setPolTotExtras(polextras.setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
		policy.setPolTotPrem(totalPrem);
		policy.setPolTotSD(polstampDuty.setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
		policy.setPolTotSI(totSuminsured);
		policy.setPolTotWhtx(polwhtxAmt.setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
		policy.setStampDuty(polstampDuty.setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
		policy.setSumInsured(totSuminsured);
		policy.setNetPrem((totalPrem.add(polextras).add(polstampDuty).add(polphfFund).add(polTl).add(polwhtxAmt).add(totalCommission)).setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
		if(policy.isRenewable())
		 policy.setFuturePrem(futurePrem.add(futureTotalTax).setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
		else
		 policy.setFuturePrem(BigDecimal.ZERO);
		PolicyTrans savedPolicy =  policyRepo.save(policy);
		Iterable<RiskTrans> savedRisks = riskRepo.findAll(QRiskTrans.riskTrans.policy.policyId.eq(savedPolicy.getPolicyId()));
		for(RiskTrans savedRisk:savedRisks){
			policyService.populateRiskScheduleDetails(savedRisk.getRiskId());
		}
		
	}
	
    /**
     * Premium Computation at Risk Level
     * @return Risk Premium
     */
	@Override
	public BigDecimal computeRiskPrem(Long riskCode) {
		return null;
	}

	@Override
	public BigDecimal calculateTax(BigDecimal premAmount, BigDecimal rate,
			BigDecimal divFactor,String rateType) {
		if("P".equalsIgnoreCase(rateType)){
			if(premAmount==null || premAmount.compareTo(BigDecimal.ZERO)==0){
				 return BigDecimal.ZERO;
			}else
			return premAmount.multiply(rate).divide(divFactor);
		}
		
		else if("A".equalsIgnoreCase(rateType)){
			if(premAmount==null || premAmount.compareTo(BigDecimal.ZERO)==0){
				 return BigDecimal.ZERO;
			}
			else {
				if(premAmount.compareTo(BigDecimal.ZERO)==1) return rate;
				else
					return rate.negate();
			}
				
		}
		else return BigDecimal.ZERO;
	}


	@Override
	public BigDecimal getCommissionRate(long BinderDet, BigDecimal premAmount) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Modifying
	@Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
	public void computeEndorsePremium(Long polCode) throws BadRequestException,IOException {
		PolicyTrans policy = policyRepo.findOne(polCode);
		if(policy == null){
			throw new BadRequestException("Error getting Policy Details");
		}
		
		if("NB".equalsIgnoreCase(policy.getTransType())||"SP".equalsIgnoreCase(policy.getTransType())){
			throw new BadRequestException("Error Computing Endorsement Premium for "+policy.getTransType());
		}
		
		PolicyTrans prevPolicy =null;
		if("RE".equalsIgnoreCase(policy.getPolRevStatus())){
			prevPolicy = policy.getReusecontraPolicy().getPreviousTrans();
		}else
	    prevPolicy = policy.getPreviousTrans();
		if(prevPolicy==null){
			throw new BadRequestException("Error getting Previous Policy Transaction Details...");
		}
		long calDays = TimeUnit.DAYS.convert((policy.getCoverTo().getTime() -policy.getCoverFrom().getTime()) , TimeUnit.MILLISECONDS)+1;
		boolean cashBasis = policy.getInterfaceType()!=null && "C".equalsIgnoreCase(policy.getInterfaceType());
		
		Iterable<RiskTrans> prevRisks = riskRepo.findAll(QRiskTrans.riskTrans.policy.policyId.eq(prevPolicy.getPolicyId()));
		
		
		Iterable<RiskTrans> currRisks = riskRepo.findAll(QRiskTrans.riskTrans.policy.policyId.eq(polCode));
		Iterable<PolicyTaxes> policyTaxes = polTaxesRepo.findAll(QPolicyTaxes.policyTaxes.policy.policyId.eq(polCode));
		BigDecimal totalCommission =BigDecimal.ZERO;
		BigDecimal sumPolicytaxAmount = BigDecimal.ZERO;
		BigDecimal polstampDuty = BigDecimal.ZERO;
		BigDecimal polphfFund = BigDecimal.ZERO;
		BigDecimal polwhtxAmt = BigDecimal.ZERO;
		BigDecimal polextras = BigDecimal.ZERO;
		BigDecimal polTl = BigDecimal.ZERO;
		BigDecimal totalEndosPremium = BigDecimal.ZERO;
		BigDecimal sumInsured = BigDecimal.ZERO;
		BigDecimal totalFullPrem = BigDecimal.ZERO;
		BigDecimal totalSubAgentComm = BigDecimal.ZERO;
		BigDecimal futurePremium = BigDecimal.ZERO;
		AccountTypes accType =  ( policy.getAgent()!=null)?policy.getAgent().getAccountType():null;
		for(RiskTrans currRisk:currRisks){

			BigDecimal prevriskPrem = BigDecimal.ZERO;
			BigDecimal currRiskPrem = BigDecimal.ZERO;
			BigDecimal riskInsured = BigDecimal.ZERO;
			BigDecimal newRiskPrem =  BigDecimal.ZERO;
			BigDecimal futureRiskPrem =  BigDecimal.ZERO;
			Iterable<SectionTrans> sections  = sectionRepo.findAll(QSectionTrans.sectionTrans.risk.riskId.eq(currRisk.getRiskId()));
			List<PremiumItemsBean> itemsBeen = new ArrayList<>();
			List<PremiumItemsBean> discountItems = new ArrayList<>();
			final String ratesTable = premRatesTableRepo.getRatesLocation(currRisk.getBinderDetails().getDetId());
			if (ratesTable==null)
				throw new BadRequestException("Rates Table for the Binder has not been setup");
			ScheduleBean scheduleBean = scheduleService.generateScheduleColumns(currRisk.getRiskId());
			long count = 0;
			if(scheduleBean.getMappings()!=null) {
				count = scheduleBean.getMappings().stream().filter(a -> a.getPremItem() != null && "Y".equalsIgnoreCase(a.getPremItem())).count();
			}
			if(count > 0) {
				if (scheduleTransRepo.count(QScheduleTrans.scheduleTrans.risk.riskId.eq(currRisk.getRiskId())) > 1)
					throw new BadRequestException("Cannot compute premium. You have entered mutliple schedule details...");
			}
			long counter = count;
			for(SectionTrans section:sections){
				List<String> items = new ArrayList<>();
				double minPrem = 0;
				if(scheduleBean.getMappings()!=null) {
					scheduleBean.getMappings().stream().filter(a -> a.getPremItem() != null && "Y".equalsIgnoreCase(a.getPremItem()))
							.filter(a -> a.getPremCode() != null && a.getPremCode() == section.getSection().getId()).forEach(a -> {
						if (counter > 1) {
							ScheduleTrans scheduleTrans = scheduleTransRepo.findOne(QScheduleTrans.scheduleTrans.risk.riskId.eq(currRisk.getRiskId()));
							if (scheduleTrans != null)
								switch (a.getKey()) {
									case "1":
										if (scheduleTrans.getColumn1() != null)
											items.add(scheduleTrans.getColumn1());
										break;
									case "2":
										if (scheduleTrans.getColumn2() != null)
											items.add(scheduleTrans.getColumn2());
										break;
									case "3":
										if (scheduleTrans.getColumn3() != null)
											items.add(scheduleTrans.getColumn3());
										break;
									case "4":
										if (scheduleTrans.getColumn4() != null)
											items.add(scheduleTrans.getColumn4());
										break;
									case "5":
										if (scheduleTrans.getColumn5() != null)
											items.add(scheduleTrans.getColumn5());
										break;
									case "6":
										if (scheduleTrans.getColumn6() != null)
											items.add(scheduleTrans.getColumn6());
										break;
									case "7":
										if (scheduleTrans.getColumn7() != null)
											items.add(scheduleTrans.getColumn7());
										break;
									case "8":
										if (scheduleTrans.getColumn8() != null)
											items.add(scheduleTrans.getColumn8());
										break;
									case "9":
										if (scheduleTrans.getColumn9() != null)
											items.add(scheduleTrans.getColumn9());
										break;
									case "10":
										if (scheduleTrans.getColumn10() != null)
											items.add(scheduleTrans.getColumn10());
										break;
									case "11":
										if (scheduleTrans.getColumn11() != null)
											items.add(scheduleTrans.getColumn11());
										break;
									case "12":
										if (scheduleTrans.getColumn12() != null)
											items.add(scheduleTrans.getColumn12());
										break;
									case "13":
										if (scheduleTrans.getColumn13() != null)
											items.add(scheduleTrans.getColumn13());
										break;
									case "14":
										if (scheduleTrans.getColumn14() != null)
											items.add(scheduleTrans.getColumn14());
										break;
									case "15":
										if (scheduleTrans.getColumn15() != null)
											items.add(scheduleTrans.getColumn15());
										break;
								}
						}

					});

					if (section.getPremRates() != null) {
						PremRatesDef premRatesDef = section.getPremRates();
						if (premRatesDef.getMinPremium() != null && premRatesDef.getMinPremium().compareTo(BigDecimal.ZERO) == 1) {
							minPrem = premRatesDef.getMinPremium().doubleValue();
						}
					}
				}
				if(!section.getSection().getType().equals(SectionTypes.DS)){
					itemsBeen.add(new PremiumItemsBean(section.getSection().getShtDesc(),section.getRate().doubleValue(),
							section.getFreeLimit().doubleValue(),minPrem,
							section.getAmount().doubleValue(),section.getSectId(),section.getDivFactor().doubleValue(),
							section.getSection().getType().getCode(),section.getSection().getType().getOrder(),  items,policy.getPolicyId()));
				}
				else if(section.getSection().getType().equals(SectionTypes.DS)){
					discountItems.add(new PremiumItemsBean(section.getSection().getShtDesc(),section.getRate().doubleValue(),
							section.getFreeLimit().doubleValue(),minPrem,
							section.getAmount().doubleValue(),section.getSectId(),section.getDivFactor().doubleValue(),
							section.getSection().getType().getCode(),section.getSection().getType().getOrder(),  items,policy.getPolicyId()));
				}
			}

			itemsBeen.stream().sorted(Comparator.comparing(PremiumItemsBean::getOrder));
			PremiumResultBean resultBean =premExcelUtils.getPremium(itemsBeen,ratesTable);
			currRiskPrem = BigDecimal.valueOf(resultBean.getPremium());
			sumInsured = BigDecimal.valueOf(resultBean.getSumInsured());
			riskInsured = sumInsured;
			futureRiskPrem = currRiskPrem;

			System.out.println("Current risk prem.."+currRiskPrem);

//			if(currRisk.getBinderDetails().getMinPrem()!=null && currRisk.getBinderDetails().getMinPrem().compareTo(currRiskPrem) ==1){
//				currRiskPrem = currRisk.getBinderDetails().getMinPrem();
//			}
			if("NB".equalsIgnoreCase(currRisk.getTransType())) {
				newRiskPrem = currRiskPrem;
			}
			RiskTrans foundRisk = null;
			Iterator<RiskTrans> riskIterator = prevRisks.iterator();
			while(riskIterator.hasNext()){
				RiskTrans risk = (RiskTrans)riskIterator.next();
				if(currRisk.getRiskIdentifier().longValue()==risk.getRiskIdentifier().longValue()){
					foundRisk = risk;
					break;
				}
			}
			if(("EN".equalsIgnoreCase(currRisk.getTransType()))&& foundRisk==null)
				throw new BadRequestException("Error Occured Populating Endorsements");
			
			if("EN".equalsIgnoreCase(currRisk.getTransType()) || "EX".equalsIgnoreCase(currRisk.getTransType())){
			Iterable<SectionTrans> prevSections  = sectionRepo.findAll(QSectionTrans.sectionTrans.risk.riskId.eq(foundRisk.getRiskId()));
				long counter2 = count;
				RiskTrans foundRisk2 = foundRisk;
				itemsBeen = new ArrayList<>();
			for(SectionTrans section:prevSections){
				List<String> items = new ArrayList<>();
				double minPrem = 0;
				if(scheduleBean.getMappings()!=null) {
					scheduleBean.getMappings().stream().filter(a -> a.getPremItem() != null && "Y".equalsIgnoreCase(a.getPremItem()))
							.filter(a -> a.getPremCode() != null && a.getPremCode() == section.getSection().getId()).forEach(a -> {
						if (counter2 > 1) {
							ScheduleTrans scheduleTrans = scheduleTransRepo.findOne(QScheduleTrans.scheduleTrans.risk.riskId.eq(foundRisk2.getRiskId()));
							if (scheduleTrans != null)
								switch (a.getKey()) {
									case "1":
										if (scheduleTrans.getColumn1() != null)
											items.add(scheduleTrans.getColumn1());
										break;
									case "2":
										if (scheduleTrans.getColumn2() != null)
											items.add(scheduleTrans.getColumn2());
										break;
									case "3":
										if (scheduleTrans.getColumn3() != null)
											items.add(scheduleTrans.getColumn3());
										break;
									case "4":
										if (scheduleTrans.getColumn4() != null)
											items.add(scheduleTrans.getColumn4());
										break;
									case "5":
										if (scheduleTrans.getColumn5() != null)
											items.add(scheduleTrans.getColumn5());
										break;
									case "6":
										if (scheduleTrans.getColumn6() != null)
											items.add(scheduleTrans.getColumn6());
										break;
									case "7":
										if (scheduleTrans.getColumn7() != null)
											items.add(scheduleTrans.getColumn7());
										break;
									case "8":
										if (scheduleTrans.getColumn8() != null)
											items.add(scheduleTrans.getColumn8());
										break;
									case "9":
										if (scheduleTrans.getColumn9() != null)
											items.add(scheduleTrans.getColumn9());
										break;
									case "10":
										if (scheduleTrans.getColumn10() != null)
											items.add(scheduleTrans.getColumn10());
										break;
									case "11":
										if (scheduleTrans.getColumn11() != null)
											items.add(scheduleTrans.getColumn11());
										break;
									case "12":
										if (scheduleTrans.getColumn12() != null)
											items.add(scheduleTrans.getColumn12());
										break;
									case "13":
										if (scheduleTrans.getColumn13() != null)
											items.add(scheduleTrans.getColumn13());
										break;
									case "14":
										if (scheduleTrans.getColumn14() != null)
											items.add(scheduleTrans.getColumn14());
										break;
									case "15":
										if (scheduleTrans.getColumn15() != null)
											items.add(scheduleTrans.getColumn15());
										break;
								}
						}

					});

					if (section.getPremRates() != null) {
						PremRatesDef premRatesDef = section.getPremRates();
						if (premRatesDef.getMinPremium() != null && premRatesDef.getMinPremium().compareTo(BigDecimal.ZERO) == 1) {
							minPrem = premRatesDef.getMinPremium().doubleValue();
						}
					}
				}
				itemsBeen.add(new PremiumItemsBean(section.getSection().getShtDesc(),section.getRate().doubleValue(),
						section.getFreeLimit().doubleValue(),minPrem,
						section.getAmount().doubleValue(),section.getSectId(),section.getDivFactor().doubleValue(),
						section.getSection().getType().getCode(),section.getSection().getType().getOrder(),  items,policy.getPolicyId()));
			}

				itemsBeen.stream().sorted(Comparator.comparing(PremiumItemsBean::getOrder));
			    resultBean =premExcelUtils.getPremium(itemsBeen,ratesTable);
				prevriskPrem = BigDecimal.valueOf(resultBean.getPremium());

//				if(foundRisk2.getBinderDetails().getMinPrem()!=null && currRisk.getBinderDetails().getMinPrem().compareTo(prevriskPrem) ==1){
//					prevriskPrem = foundRisk2.getBinderDetails().getMinPrem();
//				}
//				System.out.println("Prev Risk Prem 5"+prevriskPrem);
			}
			long datediff = currRisk.getWetDate().getTime() - currRisk.getWefDate().getTime();
			long daysDiff = TimeUnit.DAYS.convert(datediff, TimeUnit.MILLISECONDS)+1;


			double rata = (double)daysDiff/calDays;


			BigDecimal prorata = new BigDecimal(rata);
			BigDecimal currprorataPrem = currRiskPrem;
			BigDecimal endosPrem = BigDecimal.ZERO;
			BigDecimal totEndosPrem = BigDecimal.ZERO;
			BigDecimal endosSI= BigDecimal.ZERO;
			System.out.println("Current risk prem again..."+currprorataPrem);
			if(currRisk.getButchargePrem()==null || currRisk.getButchargePrem().compareTo(BigDecimal.ZERO)==0){

			}
			else
			{
				currprorataPrem=currRisk.getButchargePrem();
			}

			if((currRisk.getTransType()!=null && "EX".equalsIgnoreCase(currRisk.getTransType()))){
                if(policy.getTotalInstalments()!=null && policy.getTotalInstalments() > 1){
					if (newRiskPrem.compareTo(BigDecimal.ZERO) != 0) {
						endosPrem = newRiskPrem;
						totEndosPrem = newRiskPrem;
					} else {
						endosPrem = currprorataPrem.subtract(prevriskPrem);
						totEndosPrem = currprorataPrem.subtract(prevriskPrem);

					}
					BigDecimal cashBasisPrem = endosPrem;
					BigDecimal endorseCashBasis = BigDecimal.ZERO;
					BigDecimal cashPrem = BigDecimal.ZERO;

					try {
						endorseCashBasis = endosPrem.multiply(currRisk.getTotalPercentage().divide(BigDecimal.valueOf(100)));
						cashBasisPrem = prevriskPrem.multiply(new BigDecimal(currRisk.getInstallmentPerc()).divide(BigDecimal.valueOf(100)));
						cashPrem = endorseCashBasis.add(cashBasisPrem);

					} catch (NumberFormatException ex) {

					}

					BigDecimal installAmount = cashPrem;

					if (currRisk.getInstallAmount() == null) {
						//currRisk.setInstallAmount(cashPrem);
					} else installAmount = currRisk.getInstallAmount();


					BigDecimal remPercentage = BigDecimal.valueOf(100).subtract(currRisk.getTotalPercentage());
					BigDecimal remPrem = remPercentage.multiply(currprorataPrem.divide(BigDecimal.valueOf(100)));
					if (currRisk.getInstallAmount() != null) {
						if (cashPrem.compareTo(currRisk.getInstallAmount()) > 0) {
							throw new BadRequestException("Instalment Amount is below the required Minimum amount of " + cashPrem);
						}

						if (currRisk.getInstallAmount().compareTo(remPrem.add(currRisk.getInstallAmount())) == 1) {
							throw new BadRequestException("Installment amount is above the the maximum premium required for this risk of " + (remPrem.add(currRisk.getInstallAmount())));
						}

					}

					if (remPrem.compareTo(BigDecimal.ZERO) > 0) {
						currRisk.setWetDate(org.apache.commons.lang.time.DateUtils.addDays(DateUtils.addMonths(currRisk.getWefDate(), 1), -1));
					} else if (remPrem.compareTo(BigDecimal.ZERO) == 0) {
						policy.setTotalInstalments(currRisk.getInstallmentNo().intValue());
						currRisk.setWetDate(policy.getWetDate());
					}
					if (currRisk.getInstallAmount() != null) {
						BigDecimal percentage = (currRisk.getInstallAmount()).divide(currprorataPrem).multiply(BigDecimal.valueOf(100));
						currRisk.setPrevPercentage(new BigDecimal(currRisk.getInstallmentPerc()));
						currRisk.setInstallmentPerc(String.valueOf(percentage));
						currRisk.setTotalPercentage(currRisk.getTotalPercentage().add(percentage).subtract(currRisk.getPrevPercentage()));
					}


					endosPrem = installAmount;
				}
				else{
					endosPrem = currprorataPrem;
					if(currRisk.getButchargePrem()!=null && currRisk.getButchargePrem().compareTo(BigDecimal.ZERO) > 0){
						endosPrem = currRisk.getButchargePrem();
					}
					System.out.println("Current premium "+endosPrem);
				}
			}
			else if("RS".equalsIgnoreCase(policy.getPolRevStatus())){
			    if(currprorataPrem.compareTo(BigDecimal.ZERO)<=0) throw new BadRequestException("Invalid Reinstatement Transaction Premium Amount ");
                endosPrem = currprorataPrem;
                totEndosPrem = currprorataPrem;
                double endPrem = endosPrem.doubleValue();
                endosPrem  = BigDecimal.valueOf(endPrem);
            }
			else {
				newRiskPrem = newRiskPrem.multiply(prorata);
				if (newRiskPrem.compareTo(BigDecimal.ZERO) != 0) {
					endosPrem = newRiskPrem;
					totEndosPrem = newRiskPrem;
				}
				else {
					endosPrem = currprorataPrem.subtract(prevriskPrem).multiply(prorata);
					totEndosPrem = currprorataPrem.subtract(prevriskPrem);

				}
				double endPrem = endosPrem.doubleValue();
				endosPrem  = BigDecimal.valueOf(endPrem);
			}

			if ("F".equalsIgnoreCase(currRisk.getProrata())) {
				endosPrem = endosPrem.multiply(BigDecimal.ONE);
			} else if ("S".equalsIgnoreCase(currRisk.getProrata())) {
				List<ShortPeriodRates> periodRates = shortPeriodRepo.getShortPeriodRates(daysDiff);
				if (periodRates.isEmpty())
					throw new BadRequestException("Short Period Rates For the Cover Period has not been setup");
				if (periodRates.size() > 1)
					throw new BadRequestException("More than one Short Period Rates For the Cover Period have been setup");
				ShortPeriodRates rates = periodRates.get(0);
				endosPrem = endosPrem.multiply(rates.getRate().divide(rates.getDivFactor()));
			} else if ("P".equalsIgnoreCase(currRisk.getProrata())) {
				endosPrem = endosPrem.multiply(prorata).setScale(2, BigDecimal.ROUND_HALF_EVEN);
			}

				Optional<PremiumItemsBean> premiumItemsBean = discountItems.stream().filter(a -> a.getSectType().equalsIgnoreCase("DS")).findFirst();
				if(premiumItemsBean.isPresent()){
					PremiumItemsBean itemsBean = premiumItemsBean.get();
					final SectionTrans  section = sectionRepo.findOne(itemsBean.getSectId());
					final BigDecimal discountPrem = BigDecimal.valueOf(itemsBean.getRate()).multiply(endosPrem).divide(BigDecimal.valueOf(itemsBean.getDivFactor()),2, RoundingMode.HALF_EVEN);
					section.setCalcprem(discountPrem.negate());
					section.setPrem(discountPrem.negate());
					sectionRepo.save(section);
					endosPrem = endosPrem.subtract(discountPrem);
					System.out.println("Premium before discount..."+endosPrem+" Discount "+discountPrem);
				}
			System.out.println("Endos prem..."+endosPrem);

			totalEndosPremium = totalEndosPremium.add(endosPrem);
			totalFullPrem = totalFullPrem.add(totEndosPrem);
			currRisk.setPremium(endosPrem);
			currRisk.setCalcPremium(endosPrem);
			currRisk.setSumInsured(riskInsured);
			BigDecimal comm = BigDecimal.ZERO;
			if(endosPrem.compareTo(BigDecimal.ZERO)!=0){
				if(currRisk.getCommRate()!=null)
				comm = endosPrem.multiply(currRisk.getCommRate()).divide(new BigDecimal(100));
				if(endosPrem.compareTo(BigDecimal.ZERO)==1) comm = comm.negate();
				else if(endosPrem.compareTo(BigDecimal.ZERO)==-1) comm = comm.abs();
				currRisk.setCommAmt(comm);
				totalCommission = totalCommission.add(comm);
			}

            BigDecimal subAgentComm = BigDecimal.ZERO;
			if(policy.getSubAgent()!=null){
				BigDecimal commRate = BigDecimal.ZERO;
//				if(policy.getSubAgent().getSubAccountTypes().getBindersDef()!=null && policy.getSubAgent().getSubAccountTypes().getBindersDef().getBinId() ==policy.getBinder().getBinId() && policy.getSubAgent().getSubAccountTypes().getCommRate().compareTo(BigDecimal.ZERO)!=0){
//					commRate = policy.getSubAgent().getSubAccountTypes().getCommRate();
//				}
				if(commRate.compareTo(BigDecimal.ZERO)==0) {
					if (policy.getSubAgent().getAccountType().getCommRate() != null || policy.getSubAgent().getAccountType().getCommRate().compareTo(BigDecimal.ZERO) != 0) {
						commRate = policy.getSubAgent().getAccountType().getCommRate();
					}
				}
				if(commRate.compareTo(BigDecimal.ZERO)==0){
					throw new BadRequestException("Sub Agent Commission Rate not setup");
				}
				subAgentComm = comm.multiply(commRate.divide(BigDecimal.valueOf(100)));
			}
			currRisk.setSubAgentComm(subAgentComm);
			totalSubAgentComm = totalSubAgentComm.add(subAgentComm);
			BigDecimal sumRisktaxAmount = BigDecimal.ZERO;
			BigDecimal riskstampDuty = BigDecimal.ZERO;
			BigDecimal riskphfFund = BigDecimal.ZERO;
			BigDecimal riskwhtxAmt = BigDecimal.ZERO;
			BigDecimal riskextras = BigDecimal.ZERO;
			BigDecimal riskTl = BigDecimal.ZERO;

			for(PolicyTaxes policyTax:policyTaxes){
				BigDecimal computedTax = calculateTax(endosPrem, policyTax.getTaxRate(), policyTax.getDivFactor(),policyTax.getRateType());
				policyTax.setTaxAmount(computedTax);
				sumPolicytaxAmount = sumPolicytaxAmount.add(computedTax);
				if(policyTax.getRevenueItems().getItem()== RevenueItems.EX){
					if("R".equalsIgnoreCase(policyTax.getTaxLevel())){
						riskextras = riskextras.add(computedTax);
						sumRisktaxAmount = sumRisktaxAmount.add(computedTax);
					}
				}
//				else if(policyTax.getRevenueItems().getItem() == RevenueItems.SD){
//					if("R".equalsIgnoreCase(policyTax.getTaxLevel())){
//						riskstampDuty = riskstampDuty.add(computedTax);
//						sumRisktaxAmount = sumRisktaxAmount.add(computedTax);
//					}
//				}
				else if(policyTax.getRevenueItems().getItem() == RevenueItems.PHCF){
					if("R".equalsIgnoreCase(policyTax.getTaxLevel())){
						riskphfFund = riskphfFund.add(computedTax);
						sumRisktaxAmount = sumRisktaxAmount.add(computedTax);
					}
				}
				else if(policyTax.getRevenueItems().getItem() ==RevenueItems.WHTX){
					if("R".equalsIgnoreCase(policyTax.getTaxLevel())){
						riskwhtxAmt = riskwhtxAmt.add(computedTax);
						sumRisktaxAmount = sumRisktaxAmount.add(computedTax);
					}
				}
				else if(policyTax.getRevenueItems().getItem()==RevenueItems.TL){
					if("R".equalsIgnoreCase(policyTax.getTaxLevel())){
						riskTl = riskTl.add(computedTax);
						sumRisktaxAmount = sumRisktaxAmount.add(computedTax);
					}
				}
			}

			if (accType!=null && accType.isWhtxAppl()) {
				if (accType.getWhtaxVal().compareTo(BigDecimal.ZERO) == 1) {
					BigDecimal riskWhtx = accType.getWhtaxVal().divide(new BigDecimal(100)).multiply(comm);
					currRisk.setWhtax(riskWhtx);
				}
			}

			//currRisk.setNetpremium(endosPrem.add(sumRisktaxAmount).subtract(comm));
			currRisk.setExtras(riskextras);
//			currRisk.setWhtax(riskwhtxAmt);
			currRisk.setPhfFund(riskphfFund);
			currRisk.setTrainingLevy(riskTl);
			currRisk.setStampDuty(riskstampDuty);
			currRisk.setFuturePrem(futureRiskPrem);
			futurePremium = futurePremium.add(futureRiskPrem);
		}
		
		polTaxesRepo.save(policyTaxes);
		BigDecimal whtxAmt = BigDecimal.ZERO;	
		if(accType!=null && accType.isWhtxAppl()){
			if(accType.getWhtaxVal().compareTo(BigDecimal.ZERO)==1){
				whtxAmt = accType.getWhtaxVal().divide(new BigDecimal(100)).multiply(totalCommission);
				 polwhtxAmt = whtxAmt.negate();
			}
		}


		
		sumPolicytaxAmount = sumPolicytaxAmount.add(polwhtxAmt);
		BigDecimal futureTax = BigDecimal.ZERO;
		for(PolicyTaxes policyTax:policyTaxes){
			BigDecimal computedTax = calculateTax(totalEndosPremium, policyTax.getTaxRate(), policyTax.getDivFactor(),policyTax.getRateType());
			BigDecimal computedFutureTax = calculateTax(futurePremium, policyTax.getTaxRate(), policyTax.getDivFactor(),policyTax.getRateType());
			if(policyTax.getRevenueItems().getItem()== RevenueItems.EX){
				
				polextras = polextras.add(computedTax);
				futureTax = futureTax.add(computedFutureTax);
			}
//			else if(policyTax.getRevenueItems().getItem() == RevenueItems.SD){
//				
//				polstampDuty = polstampDuty.add(computedTax);
//			}
			else if(policyTax.getRevenueItems().getItem() == RevenueItems.PHCF){
				futureTax = futureTax.add(computedFutureTax);
				polphfFund = polphfFund.add(computedTax);
			}
			else if(policyTax.getRevenueItems().getItem() ==RevenueItems.WHTX){
				futureTax = futureTax.add(computedFutureTax);
				polwhtxAmt = polwhtxAmt.add(computedTax);
			}
			else if(policyTax.getRevenueItems().getItem()==RevenueItems.TL){
				futureTax = futureTax.add(computedFutureTax);
				polTl = polTl.add(computedTax);
			}
		}
		riskRepo.save(currRisks);
		Currencies currencies = policy.getTransCurrency();
		totalEndosPremium = totalEndosPremium.setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN);
		totalCommission = totalCommission.setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN);
		policy.setPremium(totalEndosPremium);
		policy.setBasicPrem((totalEndosPremium.add(polextras).add(polstampDuty).add(polphfFund).add(polTl)).setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
		if (prevPolicy.getEndosbasicPremium()==null) {
			policy.setEndosbasicPremium(totalEndosPremium);
		}
		else{
			policy.setEndosbasicPremium(prevPolicy.getEndosbasicPremium().add(totalEndosPremium));
		}
		if (prevPolicy.getEndosgrossPremium()==null) {
			policy.setEndosgrossPremium((totalEndosPremium.add(polextras).add(polstampDuty).add(polphfFund).add(polTl)).setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
		}
		else{
			policy.setEndosgrossPremium(prevPolicy.getEndosgrossPremium().add((totalEndosPremium.add(polextras).add(polstampDuty).add(polphfFund).add(polTl)).setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN)));}

		if (prevPolicy.getEndosCommissions()==null)
			policy.setEndosCommissions(totalCommission);
		else
			policy.setEndosCommissions(prevPolicy.getEndosCommissions().add(totalCommission));

		if(StringUtils.equalsIgnoreCase(paramService.getParameterString("SUB_AGENT_COMM_PARAM"),"N")){
			if(policy.getSubAgent()!=null) {
				BigDecimal commRate = BigDecimal.ZERO;
//				if(policy.getSubAgent().getSubAccountTypes().getBindersDef()!=null && policy.getSubAgent().getSubAccountTypes().getBindersDef().getBinId() ==policy.getBinder().getBinId() && policy.getSubAgent().getSubAccountTypes().getCommRate().compareTo(BigDecimal.ZERO)!=0){
//					commRate = policy.getSubAgent().getSubAccountTypes().getCommRate();
//				}
				if(commRate.compareTo(BigDecimal.ZERO)==0) {
					if (policy.getSubAgent().getAccountType().getCommRate() != null || policy.getSubAgent().getAccountType().getCommRate().compareTo(BigDecimal.ZERO) != 0) {
						commRate = policy.getSubAgent().getAccountType().getCommRate();
					}
				}
				if(commRate.compareTo(BigDecimal.ZERO)==0){
					throw new BadRequestException("Sub Agent Commission Rate not setup");
				}
				totalSubAgentComm =	(totalCommission.abs().subtract(whtxAmt.abs())).multiply(commRate.divide(BigDecimal.valueOf(100)));
			}
		}



		if(totalEndosPremium.compareTo(BigDecimal.ZERO)==1){
			totalCommission = totalCommission.abs().negate();
			totalSubAgentComm = totalSubAgentComm.abs().negate();
		}
		else if(totalEndosPremium.compareTo(BigDecimal.ZERO)==-1){
			totalCommission = totalCommission.abs();
			totalSubAgentComm = totalSubAgentComm.abs();
		}

		policy.setCommAmt(totalCommission.setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
		policy.setSubAgentComm(totalSubAgentComm);
		policy.setExtras(polextras.setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
		policy.setWhtx(polwhtxAmt.setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
		policy.setPhcf(polphfFund.setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
		policy.setTrainingLevy(polTl.setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
//		policy.setTotTrainingLevy(polTl.setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
//		policy.setTotPhcf(polphfFund.setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
		policy.setStampDuty(polstampDuty.setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
		policy.setSumInsured(sumInsured.setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
		policy.setNetPrem((totalEndosPremium.add(polextras).add(polstampDuty).add(polphfFund).add(polTl).add(polwhtxAmt).add(totalCommission)).setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));

        Iterable<PolicyActiveRisks> activeRiskses = activeRisksRepo.findAll(QPolicyActiveRisks.policyActiveRisks.policy.policyId.eq(policy.getPolicyId())
		                                                                    .and(QPolicyActiveRisks.policyActiveRisks.prevRisk.isNull())
		                                                                    .and(QPolicyActiveRisks.policyActiveRisks.status.in("NB","NR")));
		BigDecimal totTl = BigDecimal.ZERO;
		BigDecimal totPhf = BigDecimal.ZERO;
		BigDecimal totComm = BigDecimal.ZERO;
		BigDecimal totExtras = BigDecimal.ZERO;
		BigDecimal totPrem = BigDecimal.ZERO;
		BigDecimal totSi = BigDecimal.ZERO;
		BigDecimal totWhtx = BigDecimal.ZERO;
		for(PolicyActiveRisks activeRisks:activeRiskses) {
			totTl  = totTl.add(activeRisks.getRisk().getTrainingLevy());
			totPhf = totPhf.add(activeRisks.getRisk().getPhfFund());
			totComm = totComm.add((activeRisks.getRisk().getCommAmt()!=null)? activeRisks.getRisk().getCommAmt():BigDecimal.ZERO);
			totExtras = totExtras.add((activeRisks.getRisk().getExtras()!=null)?activeRisks.getRisk().getExtras():BigDecimal.ZERO);
			totPrem = totPrem.add(activeRisks.getRisk().getPremium());
			totSi = totSi.add(activeRisks.getRisk().getSumInsured());
			totWhtx = totWhtx.add((activeRisks.getRisk().getWhtax()!=null)?activeRisks.getRisk().getWhtax():BigDecimal.ZERO);
		}

		Iterable<PolicyActiveRisks> activeRiskes = activeRisksRepo.findAll(QPolicyActiveRisks.policyActiveRisks.policy.policyId.eq(policy.getPolicyId())
				.and(QPolicyActiveRisks.policyActiveRisks.prevRisk.isNull())
				.and(QPolicyActiveRisks.policyActiveRisks.status.in("ER")));

		for(PolicyActiveRisks activeRisks:activeRiskes) {
			totTl  = totTl.add(activeRisks.getRisk().getTrainingLevy());
			totPhf = totPhf.add(activeRisks.getRisk().getPhfFund());
			totComm = totComm.add((activeRisks.getRisk().getCommAmt()!=null)? activeRisks.getRisk().getCommAmt():BigDecimal.ZERO);
			totExtras = totExtras.add((activeRisks.getRisk().getExtras()!=null)?activeRisks.getRisk().getExtras():BigDecimal.ZERO);
			totPrem = totPrem.add(activeRisks.getRisk().getPremium());
			totSi = totSi.add(activeRisks.getRisk().getSumInsured());
			totWhtx = totWhtx.add((activeRisks.getRisk().getWhtax()!=null)?activeRisks.getRisk().getWhtax():BigDecimal.ZERO);
		}


		policy.setTotTrainingLevy( totTl.setScale(currencies.getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
		policy.setTotPhcf(totPhf.setScale(currencies.getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
		policy.setPolTotComm(totComm.setScale(currencies.getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
		policy.setPolTotExtras(totExtras.setScale(currencies.getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
		policy.setPolTotPrem(totPrem.setScale(currencies.getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
		policy.setPolTotSD(polstampDuty.setScale(currencies.getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
		policy.setPolTotSI(totSi.setScale(currencies.getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
		policy.setPolTotWhtx(totWhtx.setScale(currencies.getRoundOff(), BigDecimal.ROUND_HALF_EVEN));
		policy.setPaidPremium(receiptService.getPolicyTotalRcptAmount(policy.getPolNo()));
		if (policy.getPaidPremium().compareTo(policy.getEndosgrossPremium())==1){
			BigDecimal refundableAmt =policy.getPaidPremium().subtract(policy.getEndosgrossPremium());
			if (refundableAmt.compareTo(policy.getBasicPrem().abs())==1){
				policy.setRefundablePremium(policy.getBasicPrem().abs());
			}else {
				policy.setRefundablePremium(refundableAmt);
			}

		}else {
			policy.setRefundablePremium(BigDecimal.ZERO);
		}
		if(prevPolicy.getFuturePrem()==null) throw new BadRequestException("Error Doing an Endorsement on this policy");
		policy.setFuturePrem(futurePremium.add(futureTax).setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
		PolicyTrans savedPolicy = policyRepo.save(policy);
		Iterable<RiskTrans> savedRisks = riskRepo.findAll(QRiskTrans.riskTrans.policy.policyId.eq(savedPolicy.getPolicyId()));
		for(RiskTrans savedRisk:savedRisks){
			policyService.populateRiskScheduleDetails(savedRisk.getRiskId());
		}

		
	}

	@Override
	@Modifying
	@Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
	public void computeCancelPrem(Long polCode) throws BadRequestException {
		PolicyTrans policy = policyRepo.findOne(polCode);
		if(policy == null){
			throw new BadRequestException("Error getting Policy Details");
		}
		
		if("NB".equalsIgnoreCase(policy.getTransType())||"SP".equalsIgnoreCase(policy.getTransType())){
			throw new BadRequestException("Error Computing Cancellation Premium for "+policy.getTransType());
		}
		
		PolicyTrans prevPolicy = policy.getPreviousTrans();
		if(prevPolicy==null){
			throw new BadRequestException("Error getting Previous Policy Transaction Details...");
		}
		long calDays = TimeUnit.DAYS.convert((policy.getCoverTo().getTime() -policy.getCoverFrom().getTime()) , TimeUnit.MILLISECONDS)+1;
		
		long datediff = policy.getWetDate().getTime() - policy.getWefDate().getTime();
		long daysDiff = TimeUnit.DAYS.convert(datediff, TimeUnit.MILLISECONDS)+1;


		Iterable<PolicyTaxes> policyTaxes = polTaxesRepo.findAll(QPolicyTaxes.policyTaxes.policy.policyId.eq(polCode));
		
		BigDecimal endorsePrem = prevPolicy.getEndosbasicPremium();
		BigDecimal endorseComm = prevPolicy.getEndosCommissions();
		System.out.println("endorseComm = "+endorseComm+" endorsePrem = "+endorsePrem);
		if(endorsePrem.compareTo(BigDecimal.ZERO)==0 || endorsePrem.compareTo(BigDecimal.ZERO)==0) throw new BadRequestException("Error Computing Cancellation Premium. Endorsement Premium cannot be negative or zero");
		double rata = (double)daysDiff/calDays; 
		BigDecimal prorata = new BigDecimal(rata);
		BigDecimal refundPrem = endorsePrem.multiply(prorata).setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN).negate();
		
		BigDecimal totalCommission = endorseComm.multiply(prorata).negate();
		BigDecimal polstampDuty = BigDecimal.ZERO;
		BigDecimal polphfFund = BigDecimal.ZERO;
		BigDecimal polwhtxAmt = BigDecimal.ZERO;
		BigDecimal polextras = BigDecimal.ZERO;
		BigDecimal polTl = BigDecimal.ZERO;

		Iterable<PolicyActiveRisks> activeRisks = activeRisksRepo.findAll(QPolicyActiveRisks.policyActiveRisks.policy.policyId.eq(polCode));
		//AccountTypes accType = policy.getAgent().getAccountType();
		AccountTypes accType = (policy.getAgent()!=null)?policy.getAgent().getAccountType():null;
		for (PolicyActiveRisks risk:activeRisks){
			BigDecimal riskstampDuty = BigDecimal.ZERO;
			BigDecimal riskphfFund = BigDecimal.ZERO;
			BigDecimal riskwhtxAmt = BigDecimal.ZERO;
			BigDecimal riskextras = BigDecimal.ZERO;
			BigDecimal riskTl = BigDecimal.ZERO;
			BigDecimal endosPrem = risk.getPrevRisk().getPremium().negate().multiply(prorata);
			BigDecimal comm=risk.getPrevRisk().getCommAmt().negate().multiply(prorata);
			BigDecimal subAgentComm = risk.getPrevRisk().getSubAgentComm().negate().multiply(prorata);
			RiskTrans currRisk = risk.getRisk();
			currRisk.setPremium(endosPrem);
			currRisk.setCalcPremium(endosPrem);
			currRisk.setSumInsured(risk.getPrevRisk().getSumInsured());
			currRisk.setCommAmt(comm);
			currRisk.setSubAgentComm(subAgentComm);
			Iterable<SectionTrans> sections = sectionRepo.findAll(QSectionTrans.sectionTrans.risk.riskId.eq(currRisk.getRiskId()));
			for (SectionTrans section:sections){
				BigDecimal sectPrem = section.getPrevSection().getPrem().negate().multiply(prorata);
				section.setPrem(sectPrem);
				section.setCalcprem(sectPrem);
				sectionRepo.save(section);
			}
			for(PolicyTaxes policyTax:policyTaxes){
				BigDecimal computedTax = calculateTax(endosPrem, policyTax.getTaxRate(), policyTax.getDivFactor(),policyTax.getRateType());
				if(policyTax.getRevenueItems().getItem()== RevenueItems.EX){
					if("R".equalsIgnoreCase(policyTax.getTaxLevel())){
						riskextras = riskextras.add(computedTax);
					}
				}
				else if(policyTax.getRevenueItems().getItem() == RevenueItems.PHCF){
					if("R".equalsIgnoreCase(policyTax.getTaxLevel())){
						riskphfFund = riskphfFund.add(computedTax);
					}
				}
				else if(policyTax.getRevenueItems().getItem() ==RevenueItems.WHTX){
					if("R".equalsIgnoreCase(policyTax.getTaxLevel())){
						riskwhtxAmt = riskwhtxAmt.add(computedTax);
					}
				}
				else if(policyTax.getRevenueItems().getItem()==RevenueItems.TL){
					if("R".equalsIgnoreCase(policyTax.getTaxLevel())){
						riskTl = riskTl.add(computedTax);
					}
				}
			}
			if (accType!=null && accType.isWhtxAppl()) {
				if (accType.getWhtaxVal().compareTo(BigDecimal.ZERO) == 1) {
					BigDecimal riskWhtx = accType.getWhtaxVal().divide(new BigDecimal(100)).multiply(comm);
					currRisk.setWhtax(riskWhtx);
				}
			}
			currRisk.setExtras(riskextras);
			currRisk.setPhfFund(riskphfFund);
			currRisk.setTrainingLevy(riskTl);
			currRisk.setStampDuty(riskstampDuty);
			riskRepo.save(currRisk);
		}
		
		for(PolicyTaxes policyTax:policyTaxes){
			BigDecimal computedTax = calculateTax(refundPrem, policyTax.getTaxRate(), policyTax.getDivFactor(),policyTax.getRateType());
			policyTax.setTaxAmount(computedTax.setScale(policy.getTransCurrency().getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
			if(policyTax.getRevenueItems().getItem()== RevenueItems.EX){
				
				polextras = polextras.add(computedTax);
			}
//			else if(policyTax.getRevenueItems().getItem() == RevenueItems.SD){
//				
//				polstampDuty = polstampDuty.add(computedTax);
//			}
			else if(policyTax.getRevenueItems().getItem() == RevenueItems.PHCF){
				
				polphfFund = polphfFund.add(computedTax);
			}
			else if(policyTax.getRevenueItems().getItem() ==RevenueItems.WHTX){
				
				polwhtxAmt = polwhtxAmt.add(computedTax);
			}
			else if(policyTax.getRevenueItems().getItem()==RevenueItems.TL){
				
				polTl = polTl.add(computedTax);
			}
		}
		
		polTaxesRepo.save(policyTaxes);
		
		BigDecimal whtxAmt = BigDecimal.ZERO;	

		if(accType!=null && accType.isWhtxAppl()){
			if(accType.getWhtaxVal().compareTo(BigDecimal.ZERO)==1){
				whtxAmt = accType.getWhtaxVal().divide(new BigDecimal(100)).multiply(totalCommission);
				 polwhtxAmt = whtxAmt.negate();
			}
		}
		Currencies currencies = policy.getTransCurrency();
		refundPrem = refundPrem.setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN);
		totalCommission = totalCommission.setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN);
		policy.setPremium(refundPrem);
		policy.setBasicPrem((refundPrem.add(polextras).add(polstampDuty).add(polphfFund).add(polTl)).setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
		policy.setEndosbasicPremium((prevPolicy.getEndosbasicPremium().add(refundPrem)).setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
		policy.setCommAmt(totalCommission);
		policy.setExtras(polextras.setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
		policy.setWhtx(polwhtxAmt.setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
		policy.setPhcf(polphfFund.setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
		policy.setTrainingLevy(polTl.setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
		policy.setStampDuty(polstampDuty.setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
		policy.setSumInsured(prevPolicy.getSumInsured());
		policy.setNetPrem(refundPrem.add((polextras).add(polstampDuty).add(polphfFund).add(polTl).add(polwhtxAmt).add(totalCommission)).setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
		policy.setTotTrainingLevy(polTl.setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
		policy.setTotPhcf(polphfFund.setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));

		policy.setPaidPremium(receiptService.getPolicyTotalRcptAmount(policy.getPolNo()));

		if (prevPolicy.getEndosgrossPremium()==null) {
			policy.setEndosgrossPremium((refundPrem.add(polextras).add(polstampDuty).add(polphfFund).add(polTl)).setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
		}
		else{
			policy.setEndosgrossPremium(prevPolicy.getEndosgrossPremium().add((refundPrem.add(polextras).add(polstampDuty).add(polphfFund).add(polTl)).setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN)));}

		if (policy.getPaidPremium().compareTo(policy.getEndosgrossPremium())==1){
			BigDecimal refundableAmt =policy.getPaidPremium().subtract(policy.getEndosgrossPremium());
			if (refundableAmt.compareTo(policy.getBasicPrem().abs())==1){
				policy.setRefundablePremium(policy.getBasicPrem().abs());
			}else {
				policy.setRefundablePremium(refundableAmt);
			}

		}else {
			policy.setRefundablePremium(BigDecimal.ZERO);
		}
		policyRepo.save(policy);
		
	}


	@Override
	@Modifying
	@Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
	public void computeLifePrem(Long polCode) throws BadRequestException, IOException {
		PolicyTrans policy = policyRepo.findOne(polCode);
		policyService.populateClauses(policy);
		policyService.populateTaxes(policy);
		Iterable<RiskTrans> risks = riskRepo.findAll(QRiskTrans.riskTrans.policy.policyId.eq(policy.getPolicyId()));
		Iterable<PolicyTaxes> policyTaxes = polTaxesRepo.findAll(QPolicyTaxes.policyTaxes.policy.policyId.eq(policy.getPolicyId()));
		BigDecimal premium = BigDecimal.ZERO;
		BigDecimal sumInsured = BigDecimal.ZERO;
		BigDecimal totalCommission =BigDecimal.ZERO;
		BigDecimal totalSubAgentComm = BigDecimal.ZERO;
		BigDecimal polstampDuty = BigDecimal.ZERO;
		BigDecimal polphfFund = BigDecimal.ZERO;
		BigDecimal polwhtxAmt = BigDecimal.ZERO;
		BigDecimal polextras = BigDecimal.ZERO;
		BigDecimal polTl = BigDecimal.ZERO;
		BigDecimal totalPrem = BigDecimal.ZERO;
		BigDecimal sumPolicytaxAmount = BigDecimal.ZERO;
		for(RiskTrans risk:risks){
			Iterable<SectionTrans> sections  = sectionRepo.findAll(QSectionTrans.sectionTrans.risk.riskId.eq(risk.getRiskId()));
			BigDecimal riskPrem = BigDecimal.ZERO;
			BigDecimal riskInsured = BigDecimal.ZERO;
			List<PremiumItemsBean> itemsBeen = new ArrayList<>();
			final String ratesTable = premRatesTableRepo.getRatesLocation(risk.getBinderDetails().getDetId());
			if (ratesTable==null)
				throw new BadRequestException("Rates Table for the Binder has not been setup");


			for(SectionTrans section:sections){
				List<String> items = new ArrayList<>();
				double minPrem = 0;
				if (section.getPremRates() != null) {
					PremRatesDef premRatesDef = section.getPremRates();
					if (premRatesDef.getMinPremium() != null && premRatesDef.getMinPremium().compareTo(BigDecimal.ZERO) == 1) {
						minPrem = premRatesDef.getMinPremium().doubleValue();
					}
				}

				PremiumItemsBean premiumItemsBean =	new PremiumItemsBean(section.getSection().getShtDesc(),section.getRate().doubleValue(),
						section.getFreeLimit().doubleValue(),minPrem,
						section.getAmount().doubleValue(),section.getSectId(),section.getDivFactor().doubleValue(),
						section.getSection().getType().getCode(),section.getSection().getType().getOrder(),  items,policy.getPolicyId());
				if(section.getSection().getType() ==SectionTypes.SI){
					premiumItemsBean.setMainSection(true);
				}
				else premiumItemsBean.setMainSection(false);
				premiumItemsBean.setAge(risk.getWorkingAge());
				premiumItemsBean.setFrequency(policy.getFrequency());
				System.out.println("Compute Type..."+risk.getComputeType()+" Sum Assured..."+risk.getSumInsured());
				if("P".equalsIgnoreCase(risk.getComputeType())) {
					premiumItemsBean.setPremium(risk.getPremium());
					premiumItemsBean.setSumAssured(BigDecimal.ZERO);
				}
				else if("B".equalsIgnoreCase(risk.getComputeType())) {
					premiumItemsBean.setSumAssured(risk.getSumInsured());
					premiumItemsBean.setPremium(risk.getPremium());
				}
				else {
					premiumItemsBean.setSumAssured(risk.getSumInsured());
					premiumItemsBean.setPremium(BigDecimal.ZERO);
				}
				premiumItemsBean.setTerm(policy.getPolTerm());
				itemsBeen.add(premiumItemsBean);
			}

			itemsBeen.stream().sorted(Comparator.comparing(PremiumItemsBean::getOrder));
			Iterable<PolicyBenefitsDistribution> existingMaturitie = maturityRepo.findAll(QPolicyBenefitsDistribution.policyBenefitsDistribution.policyId.policyId.eq(polCode));
			for (PolicyBenefitsDistribution premat:existingMaturitie){
				maturityRepo.delete(premat.getMaturityId());
			}
			PremiumResultBean resultBean =premExcelUtils.getLifePremium(itemsBeen,ratesTable);
			sumInsured = BigDecimal.valueOf(resultBean.getSumInsured());
			System.out.println("Sum assured..."+sumInsured);
			riskPrem = BigDecimal.valueOf(resultBean.getPremium());
			riskInsured = sumInsured;
			premium = riskPrem;
			risk.setPremium(riskPrem);
			risk.setCalcPremium(riskPrem);
			risk.setSumInsured(sumInsured);
			BigDecimal comm = BigDecimal.ZERO;
			BigDecimal subAgentComm = BigDecimal.ZERO;
			//totSuminsured = totSuminsured.add(riskInsured);
			if(risk.getCommRate()!=null) {
				comm = riskPrem.multiply(risk.getCommRate()).divide(BigDecimal.valueOf(100));
				if(policy.getSubAgent()!=null){
					if(policy.getSubAgent().getAccountType().getCommRate()==null || policy.getSubAgent().getAccountType().getCommRate().compareTo(BigDecimal.ZERO)==0){
						throw new BadRequestException("Sub Agent Commission Rate not setup");
					}
					subAgentComm = comm.multiply(policy.getSubAgent().getAccountType().getCommRate().divide(BigDecimal.valueOf(100)));


				}
			}

			if(riskPrem.compareTo(BigDecimal.ZERO)==1){
				comm = comm.negate();
				subAgentComm = subAgentComm.negate();
			}
			else if(riskPrem.compareTo(BigDecimal.ZERO)==-1){
				comm = comm.abs();
				subAgentComm = subAgentComm.abs();
			}
			risk.setCommAmt(comm);
			risk.setSubAgentComm(subAgentComm);
			totalCommission = totalCommission.add(comm);
			totalSubAgentComm = totalSubAgentComm.add(subAgentComm);

			BigDecimal sumRisktaxAmount = BigDecimal.ZERO;
			BigDecimal riskstampDuty = BigDecimal.ZERO;
			BigDecimal riskphfFund = BigDecimal.ZERO;
			BigDecimal riskwhtxAmt = BigDecimal.ZERO;
			BigDecimal riskextras = BigDecimal.ZERO;
			BigDecimal riskTl = BigDecimal.ZERO;

			for(PolicyTaxes policyTax:policyTaxes){
				BigDecimal computedTax = calculateTax(premium, policyTax.getTaxRate(), policyTax.getDivFactor(),policyTax.getRateType());
				policyTax.setTaxAmount(computedTax);
				sumPolicytaxAmount = sumPolicytaxAmount.add(computedTax);
				if(policyTax.getRevenueItems().getItem()== RevenueItems.EX){
					if("R".equalsIgnoreCase(policyTax.getTaxLevel())){
						riskextras = riskextras.add(computedTax);
						sumRisktaxAmount = sumRisktaxAmount.add(computedTax);
					}
				}
				else if(policyTax.getRevenueItems().getItem() == RevenueItems.SD){
					if("R".equalsIgnoreCase(policyTax.getTaxLevel())){
						riskstampDuty = riskstampDuty.add(computedTax);
						sumRisktaxAmount = sumRisktaxAmount.add(computedTax);
					}
				}
				else if(policyTax.getRevenueItems().getItem() == RevenueItems.PHCF){
					if("R".equalsIgnoreCase(policyTax.getTaxLevel())){
						riskphfFund = riskphfFund.add(computedTax);
						sumRisktaxAmount = sumRisktaxAmount.add(computedTax);
					}
				}
				else if(policyTax.getRevenueItems().getItem() ==RevenueItems.WHTX){
					if("R".equalsIgnoreCase(policyTax.getTaxLevel())){
						riskwhtxAmt = riskwhtxAmt.add(computedTax);
						sumRisktaxAmount = sumRisktaxAmount.add(computedTax);
					}
				}
				else if(policyTax.getRevenueItems().getItem()==RevenueItems.TL){
					if("R".equalsIgnoreCase(policyTax.getTaxLevel())){
						riskTl = riskTl.add(computedTax);
						sumRisktaxAmount = sumRisktaxAmount.add(computedTax);
					}
				}
			}

			risk.setNetpremium(premium.add(sumRisktaxAmount).subtract(comm));
			risk.setExtras(riskextras);
			risk.setWhtax(riskwhtxAmt);
			risk.setPhfFund(riskphfFund);
			risk.setTrainingLevy(riskTl);
			risk.setStampDuty(riskstampDuty);
			risk.setFuturePrem(premium);
			totalPrem = totalPrem.add(premium);
		}
		BigDecimal policyLevelCommRate = BigDecimal.ZERO;
		if(totalPrem.compareTo(BigDecimal.ZERO)!=0)
			policyLevelCommRate = totalCommission.divide(totalPrem, 2, RoundingMode.HALF_EVEN);
		if(policy.getBinder().getMinPrem()!=null){
			if(policy.getBinder().getMinPrem().compareTo(totalPrem)==1){
				totalPrem = policy.getBinder().getMinPrem();
				totalCommission = policyLevelCommRate.multiply(totalPrem);
				if(policy.getSubAgent()!=null){
					if(policy.getSubAgent().getAccountType().getCommRate()==null || policy.getSubAgent().getAccountType().getCommRate().compareTo(BigDecimal.ZERO)==0){
						throw new BadRequestException("Sub Agent Commission Rate not setup");
					}
					totalSubAgentComm = totalCommission.multiply(policy.getSubAgent().getAccountType().getCommRate().divide(BigDecimal.valueOf(100)));

				}
			}
		}

		if(policy.getProduct().getMinPrem()!=null){
			if(policy.getProduct().getMinPrem().compareTo(totalPrem)==1){
				totalPrem = policy.getProduct().getMinPrem();
				totalCommission = policyLevelCommRate.multiply(totalPrem);
				if(policy.getSubAgent()!=null){
					if(policy.getSubAgent().getAccountType().getCommRate()==null || policy.getSubAgent().getAccountType().getCommRate().compareTo(BigDecimal.ZERO)==0){
						throw new BadRequestException("Sub Agent Commission Rate not setup");
					}
					totalSubAgentComm = totalCommission.multiply(policy.getSubAgent().getAccountType().getCommRate().divide(BigDecimal.valueOf(100)));
				}

			}
		}

		polTaxesRepo.save(policyTaxes);
		BigDecimal whtxAmt = BigDecimal.ZERO;
		AccountTypes accType = policy.getAgent()!=null ? policy.getAgent().getAccountType():null;
		if(accType!=null && accType.isWhtxAppl()){
			if(accType.getWhtaxVal().compareTo(BigDecimal.ZERO)==1){
				whtxAmt = accType.getWhtaxVal().divide(new BigDecimal(100)).multiply(totalCommission);
				polwhtxAmt = whtxAmt.negate();
			}
		}

		if(StringUtils.equalsIgnoreCase(paramService.getParameterString("SUB_AGENT_COMM_PARAM"),"N")){
			if(policy.getSubAgent()!=null) {
				totalSubAgentComm =	(totalCommission.abs().subtract(whtxAmt.abs())).multiply(policy.getSubAgent().getAccountType().getCommRate().divide(BigDecimal.valueOf(100)));
			}
		}

		if(totalPrem.compareTo(BigDecimal.ZERO)==1){
			totalCommission = totalCommission.abs().negate();
			totalSubAgentComm = totalSubAgentComm.abs().negate();
		}
		else if(totalPrem.compareTo(BigDecimal.ZERO)==-1){
			totalCommission = totalCommission.abs();
			totalSubAgentComm = totalSubAgentComm.abs();
		}

		sumPolicytaxAmount = sumPolicytaxAmount.add(polwhtxAmt);

		for(PolicyTaxes policyTax:policyTaxes){
			BigDecimal computedTax = calculateTax(totalPrem, policyTax.getTaxRate(), policyTax.getDivFactor(),policyTax.getRateType());
			if(policyTax.getRevenueItems().getItem()== RevenueItems.EX){

				polextras = polextras.add(computedTax);
			}
			else if(policyTax.getRevenueItems().getItem() == RevenueItems.SD){

				polstampDuty = polstampDuty.add(computedTax);
			}
			else if(policyTax.getRevenueItems().getItem() == RevenueItems.PHCF){

				polphfFund = polphfFund.add(computedTax);
			}
			else if(policyTax.getRevenueItems().getItem()==RevenueItems.TL){

				polTl = polTl.add(computedTax);
			}
		}
		riskRepo.save(risks);
		Currencies currencies = policy.getTransCurrency();
		totalPrem = totalPrem.setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN);
		totalCommission = totalCommission.setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN);
		policy.setPremium(totalPrem);
		policy.setBasicPrem((totalPrem.add(polextras).add(polstampDuty).add(polphfFund).add(polTl)).setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
		policy.setEndosbasicPremium(totalPrem);
		policy.setEndosCommissions(totalCommission);
		policy.setCommAmt(totalCommission);
		policy.setSubAgentComm(totalSubAgentComm.setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
		policy.setExtras(polextras.setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
		policy.setWhtx(polwhtxAmt.setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
		policy.setPhcf(polphfFund.setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
		policy.setTrainingLevy(polTl.setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
		policy.setTotTrainingLevy(polTl.setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
		policy.setTotPhcf(polphfFund.setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
		policy.setStampDuty(polstampDuty.setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
		policy.setSumInsured(sumInsured);
		policy.setNetPrem((totalPrem.add(polextras).add(polstampDuty).add(polphfFund).add(polTl).add(polwhtxAmt).add(totalCommission)).setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
		if(policy.isRenewable())
			policy.setFuturePrem((totalPrem.add(polextras).add(polphfFund).add(polTl)).setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN));
		else
			policy.setFuturePrem(BigDecimal.ZERO);
		Iterable<PolicyInstallments> installments = policyInstallmentsRepo.findAll(QPolicyInstallments.policyInstallments.policyTrans.policyId.eq(polCode));
		policyInstallmentsRepo.delete(installments);

		final BigDecimal payablePrem =(totalPrem.add(polextras).add(polstampDuty).add(polphfFund).add(polTl).add(polwhtxAmt).add(totalCommission)).setScale(currencies.getRoundOff(),BigDecimal.ROUND_HALF_EVEN);
		final List<PolicyInstallments> installmentsList = new ArrayList<>();
		for(int i=1; i <= policy.getTotalInstalments();i++){
			final PolicyInstallments policyInstallments = new PolicyInstallments();
			policyInstallments.setInstallPaid("N");
			policyInstallments.setInstallPrem(payablePrem);
			policyInstallments.setPolicyTrans(policy);
			if(policy.getFrequency().equalsIgnoreCase("M")){
				policyInstallments.setDueDate( Date.from(LocalDate.now().plus(i, ChronoUnit.MONTHS).atStartOfDay(ZoneId.systemDefault()).toInstant()));
			}
			else if(policy.getFrequency().equalsIgnoreCase("A")){
				policyInstallments.setDueDate( Date.from(LocalDate.now().plus(i, ChronoUnit.YEARS).atStartOfDay(ZoneId.systemDefault()).toInstant()));
			}
			installmentsList.add(policyInstallments);
		}
		policyInstallmentsRepo.save(installmentsList);
		PolicyTrans savedPolicy =  policyRepo.save(policy);
		Iterable<RiskTrans> savedRisks = riskRepo.findAll(QRiskTrans.riskTrans.policy.policyId.eq(savedPolicy.getPolicyId()));
		for(RiskTrans savedRisk:savedRisks){
			policyService.populateRiskScheduleDetails(savedRisk.getRiskId());
		}



	}


}
