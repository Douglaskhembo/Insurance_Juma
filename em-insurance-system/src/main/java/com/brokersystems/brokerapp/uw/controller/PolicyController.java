
package com.brokersystems.brokerapp.uw.controller;

import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.validation.Valid;

import com.brokersystems.brokerapp.accounts.service.AccountsService;
import com.brokersystems.brokerapp.aki.dto.DigitalObject;
import com.brokersystems.brokerapp.aki.service.AkiAuthenticationService;
import com.brokersystems.brokerapp.certs.dto.CertTypesDTO;
import com.brokersystems.brokerapp.certs.dto.PolicyCertificateDTO;
import com.brokersystems.brokerapp.certs.model.*;
import com.brokersystems.brokerapp.certs.repository.BranchCertsRepo;
import com.brokersystems.brokerapp.certs.repository.PolicyCertsRepo;
import com.brokersystems.brokerapp.certs.repository.PrintQueueRepo;
import com.brokersystems.brokerapp.certs.service.CertService;
import com.brokersystems.brokerapp.claims.dtos.ClaimDetailsDTO;
import com.brokersystems.brokerapp.claims.model.ClaimBookings;
import com.brokersystems.brokerapp.claims.model.ClaimPerils;
import com.brokersystems.brokerapp.claims.service.ClaimService;
import com.brokersystems.brokerapp.dms.UploadService;
import com.brokersystems.brokerapp.dms.model.UploadBean;
import com.brokersystems.brokerapp.life.model.*;
import com.brokersystems.brokerapp.life.service.LifeService;
import com.brokersystems.brokerapp.mail.model.MailMessageBean;
import com.brokersystems.brokerapp.mail.model.MailTemplates;
import com.brokersystems.brokerapp.mail.service.MailTemplateService;
import com.brokersystems.brokerapp.mail.service.Mailer;
import com.brokersystems.brokerapp.medical.model.SubLimitsImport;
import com.brokersystems.brokerapp.quotes.services.QuotationService;
import com.brokersystems.brokerapp.schedules.model.ScheduleBean;
import com.brokersystems.brokerapp.schedules.model.ScheduleTrans;
import com.brokersystems.brokerapp.schedules.service.ScheduleService;
import com.brokersystems.brokerapp.server.utils.AuditTrailLogger;
import com.brokersystems.brokerapp.server.utils.UserUtils;
import com.brokersystems.brokerapp.setup.dto.*;
import com.brokersystems.brokerapp.setup.model.*;
import com.brokersystems.brokerapp.setup.repository.BindersRepo;
import com.brokersystems.brokerapp.setup.repository.ClientRepository;
import com.brokersystems.brokerapp.setup.service.SetupsService;
import com.brokersystems.brokerapp.trans.model.ReceiptTrans;
import com.brokersystems.brokerapp.trans.model.ReceiptTransDtls;
import com.brokersystems.brokerapp.trans.model.TransChecks;
import com.brokersystems.brokerapp.uw.dtos.*;
import com.brokersystems.brokerapp.uw.model.*;
import com.brokersystems.brokerapp.uw.repository.RiskDocsRepo;
import com.brokersystems.brokerapp.uw.repository.RiskTransRepo;
import com.brokersystems.brokerapp.webservices.model.NTSADataObject;
import com.brokersystems.brokerapp.webservices.model.VehicleDetails;
import com.brokersystems.brokerapp.webservices.service.NtsaService;
import com.fasterxml.jackson.databind.JsonNode;
import com.mysema.query.types.Predicate;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.brokersystems.brokerapp.server.datatables.DataTable;
import com.brokersystems.brokerapp.server.datatables.DataTablesRequest;
import com.brokersystems.brokerapp.server.datatables.DataTablesResult;
import com.brokersystems.brokerapp.server.exception.BadRequestException;
import com.brokersystems.brokerapp.server.utils.DateUtilities;
import com.brokersystems.brokerapp.setup.service.OrganizationService;
import com.brokersystems.brokerapp.trans.service.PolicyAuthorization;
import com.brokersystems.brokerapp.uw.service.EndorseService;
import com.brokersystems.brokerapp.uw.service.PolicyTransService;
import com.brokersystems.brokerapp.uw.service.PremComputeService;

@Controller
@RequestMapping({ "/protected/uw/policies" })
public class PolicyController {
	
	@Autowired
	private PolicyTransService policyService;


	@Autowired
	private AkiAuthenticationService akiAuthenticationService;
	
	@Autowired
	private DateUtilities dateUtils;
	
	@Autowired
	private PremComputeService premiumService;
	
	@Autowired
	private PolicyAuthorization authService;
	
	@Autowired
	private OrganizationService orgService;
	
	@Autowired
	private DataSource datasource;
	
	@Autowired
	private EndorseService endorseService;

	@Autowired
	private CertService certService;

	@Autowired
	private ScheduleService scheduleService;

	@Autowired
	private SetupsService setupsService;

	@Autowired
	private Mailer mailer;

	@Autowired
	private MailTemplateService templateService;

    @Autowired
	private UploadService uploadService;

	@Autowired
	private RiskDocsRepo riskDocsRepo;

	@Autowired
	private DateUtilities dateUtilities;

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private PrintQueueRepo printQueueRepo;

	@Autowired
	private PolicyCertsRepo policyCertsRepo;

	@Autowired
	private BindersRepo bindersRepo;

	@Autowired
	private LifeService lifeService;

	@Autowired
	private QuotationService quotationService;

	@Autowired
	private NtsaService ntsaService;

	@Autowired
	private BranchCertsRepo branchCertsRepo;

	@Autowired
	private UserUtils userUtils;

    @Autowired
	private AccountsService accountsService;

	@Autowired
	private AuditTrailLogger auditTrailLogger;

	@Autowired
	private ClaimService claimService;

	
//	@InitBinder
//	protected void initBinder(WebDataBinder binder) {
//		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//		dateFormat.setLenient(false);
//		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
//	}

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

	@RequestMapping(value = "policyEnquiry", method = {org.springframework.web.bind.annotation.RequestMethod.GET})
	public String policyEnquiry(Model model) {
		return "polenquiry";
	}

	@RequestMapping(value = "masterenq",method={org.springframework.web.bind.annotation.RequestMethod.GET})
	public String masterEnquiry(Model model,@RequestParam(required = false) String clntId,
								@RequestParam(required = false) String polId,
								@RequestParam(required = false) String group,
								@RequestParam(required = false) String claimId)
	{
		model.addAttribute("clntId",clntId);
        model.addAttribute("polIds",polId);
		model.addAttribute("group",group);
		model.addAttribute("claimId",claimId);
		return "mstrenquiry";
	}
	@RequestMapping(value = "lapsepolicies",method={org.springframework.web.bind.annotation.RequestMethod.GET})
	public String lapsepolicies(Model model)
	{
		return "lapsepolicies";
	}

	@RequestMapping(value = "quickUw",method={org.springframework.web.bind.annotation.RequestMethod.GET})
	public String quickUw(Model model)
	{
		return "quickw";
	}


	@RequestMapping(value = "pendingTrans",method={org.springframework.web.bind.annotation.RequestMethod.GET})
	public String pendingTrans(Model model)
	{
		return "pendingUwTrans";
	}

	@RequestMapping(value = { "getNtsaDetails/{regno}" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET })
	public ResponseEntity<NTSADataObject> getNtsaDetails(@PathVariable String regno)  {
		NTSADataObject ntsaDataObject = ntsaService.getNtsaDetails(regno);
		return new ResponseEntity<NTSADataObject>(ntsaDataObject, HttpStatus.OK);
	}


	
	@RequestMapping(value = "uwform", method = RequestMethod.GET)
	public String tenantForm(Model model,HttpServletRequest request) {
		request.getSession().removeAttribute("policyCode");
		model.addAttribute("policyId", -2000);
		model.addAttribute("policyStatus", 0);
		auditTrailLogger.log("Accessed General Insurance Underwriting Screen ",request);
		return "policyuwform";
	}

	@RequestMapping(value = "editpolicy", method = RequestMethod.GET)
	public String editpolicy(Model model,HttpServletRequest request) throws BadRequestException {
		Long polCode = (Long) request.getSession().getAttribute("policyCode");
		if(polCode==null){
			model.addAttribute("policyId", -2000);
            model.addAttribute("policyStatus", 0);
		}else {
            model.addAttribute("policyId", polCode);
        }
		if(polCode!=null) {
            PolicyTrans poltrans = policyService.getPolicyDetails(polCode);
            if (poltrans.getAuthStatus() != null && poltrans.getAuthStatus().equalsIgnoreCase("A")) {
                model.addAttribute("policyStatus", 2);
            } else if (poltrans.getAuthStatus() != null && poltrans.getAuthStatus().equalsIgnoreCase("R"))
                model.addAttribute("policyStatus", 1);
			else if (poltrans.getAuthStatus()!=null  && poltrans.getAuthStatus().equalsIgnoreCase("CV"))
				model.addAttribute("policyStatus", 3);
            else model.addAttribute("policyStatus", 0);
        }else
			model.addAttribute("policyStatus", 0);
		return "policyuwform";
	}


    @RequestMapping(value = "edituwpolicy", method = RequestMethod.GET)
    public String edituwpolicy(Model model, HttpServletRequest request) throws BadRequestException {
        Long polCode = (Long) request.getSession().getAttribute("policyCode");
        model.addAttribute("policyId", polCode);
        if (polCode != null) {
            PolicyTrans poltrans = policyService.getPolicyDetails(polCode);
            if (poltrans.getAuthStatus() != null && poltrans.getAuthStatus().equalsIgnoreCase("A")) {
                model.addAttribute("policyStatus", 2);
            } else if (poltrans.getAuthStatus() != null && poltrans.getAuthStatus().equalsIgnoreCase("R"))
                model.addAttribute("policyStatus", 1);
            else if (poltrans.getAuthStatus() != null && poltrans.getAuthStatus().equalsIgnoreCase("CV"))
                model.addAttribute("policyStatus", 3);
            else model.addAttribute("policyStatus", 0);

            if (poltrans.getPolRevStatus().equalsIgnoreCase("RS")) {
                model.addAttribute("policyStatus", 1);
            }

        }
        return "policyuwform";
    }

	@RequestMapping(value = "editlifepolicy", method = RequestMethod.GET)
	public String editLifepolicy(Model model,HttpServletRequest request) throws BadRequestException {
		Long polCode = (Long) request.getSession().getAttribute("policyCode");
		PolicyTrans policyTrans = policyService.getPolicyDetails(polCode);
		model.addAttribute("policyId", polCode);
		PolicyTrans poltrans =policyService.getPolicyDetails(polCode);
		if ( poltrans.getAuthStatus()!=null && poltrans.getAuthStatus().equalsIgnoreCase("A")){
			model.addAttribute("policyStatus", 2);
		}else if (poltrans.getAuthStatus()!=null  && poltrans.getAuthStatus().equalsIgnoreCase("R"))
			model.addAttribute("policyStatus", 1);
		else if (poltrans.getAuthStatus()!=null  && poltrans.getAuthStatus().equalsIgnoreCase("CV"))
			model.addAttribute("policyStatus", 3);
		else model.addAttribute("policyStatus", 0);
		return "lifeuwform";
	}
	
	@RequestMapping(value = "/edituwtrans", method = RequestMethod.POST)
	public String editPolicyForm(@Valid @ModelAttribute ModelHelperForm helperForm, Model model,HttpServletRequest request,@RequestParam(required = false) String polId) throws BadRequestException {

		request.getSession().setAttribute("policyCode", helperForm.getId());
		PolicyTrans poltrans = policyService.getPolicyDetails(helperForm.getId());
        if ( poltrans.getAuthStatus()!=null && poltrans.getAuthStatus().equalsIgnoreCase("A")){
            model.addAttribute("policyStatus", 2);
        }else if (poltrans.getAuthStatus()!=null  && poltrans.getAuthStatus().equalsIgnoreCase("R"))
            model.addAttribute("policyStatus", 1);
        else if (poltrans.getAuthStatus()!=null  && poltrans.getAuthStatus().equalsIgnoreCase("CV"))
            model.addAttribute("policyStatus", 3);
        else model.addAttribute("policyStatus", 0);
		if("MD".equalsIgnoreCase(poltrans.getProduct().getProGroup().getPrgType())) {
			model.addAttribute("policyId", helperForm.getId());
			model.addAttribute("polId",helperForm.getId());
			model.addAttribute("polIds",polId);
			return "redirect:/protected/medical/policies/edituwpolicy";
		}
		else if("L".equalsIgnoreCase(poltrans.getProduct().getProGroup().getPrgType())){

			model.addAttribute("policyId", helperForm.getId());
			model.addAttribute("polId",helperForm.getId());
			model.addAttribute("polIds",polId);
			return "lifeuwform";
		}
		else{
			model.addAttribute("policyId", helperForm.getId());
			model.addAttribute("polId",helperForm.getId());
			model.addAttribute("polIds",polId);

			return "policyuwform";
		}

	}
	@RequestMapping(value = "/groupedituwtrans", method = RequestMethod.POST)
	public String editGroupPolicyForm(@Valid @ModelAttribute ModelHelperForm helperForm, Model model,HttpServletRequest request,@RequestParam(required = false) String polId) throws BadRequestException {

		request.getSession().setAttribute("policyCode", helperForm.getId());
		PolicyTrans poltrans = policyService.getPolicyDetails(helperForm.getId());
		if ( poltrans.getAuthStatus()!=null && poltrans.getAuthStatus().equalsIgnoreCase("A")){
			model.addAttribute("policyStatus", 2);
		}else if (poltrans.getAuthStatus()!=null  && poltrans.getAuthStatus().equalsIgnoreCase("R"))
			model.addAttribute("policyStatus", 1);
		else if (poltrans.getAuthStatus()!=null  && poltrans.getAuthStatus().equalsIgnoreCase("CV"))
			model.addAttribute("policyStatus", 3);
		else model.addAttribute("policyStatus", 0);
		if("MD".equalsIgnoreCase(poltrans.getProduct().getProGroup().getPrgType())) {
			model.addAttribute("policyId", helperForm.getId());
			model.addAttribute("polId",helperForm.getId());
			model.addAttribute("group","group");

			model.addAttribute("polIds",polId);
			return "redirect:/protected/medical/policies/edituwpolicy";
		}
		else if("L".equalsIgnoreCase(poltrans.getProduct().getProGroup().getPrgType())){

			model.addAttribute("policyId", helperForm.getId());
			model.addAttribute("polId",helperForm.getId());
			model.addAttribute("polIds",polId);
			model.addAttribute("group","group");

			return "lifeuwform";
		}
		else{
			model.addAttribute("policyId", helperForm.getId());
			model.addAttribute("polId",helperForm.getId());
			model.addAttribute("polIds",polId);
			model.addAttribute("group","group");

			return "policyuwform";
		}

	}
	@RequestMapping(value = { "getLifeClientAge" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET })
	public ResponseEntity<Integer> getClientAge(
			@RequestParam(value = "clientId", required = false) Long clientId,@RequestParam(value = "binCode", required = false) Long binCode) throws BadRequestException {
		if(clientId==null)
			throw new BadRequestException("Client cannot be null");
		ClientDef clientDef = clientRepository.findOne(clientId);
		BindersDef bindersDef =bindersRepo.findOne(binCode);
		Integer age = dateUtils.getAge(clientDef.getDob());
		if(bindersDef.getPremiumAgeType()!=null && "N".equalsIgnoreCase(bindersDef.getPremiumAgeType()))
			age=age+1;
		return new ResponseEntity<Integer>(age, HttpStatus.OK);
	}
//	@RequestMapping(value = { "uwClients" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
//	@ResponseBody
//	public Page<ClientDef> selectClients(@RequestParam(value = "term", required = false) String term, Pageable pageable)
//			throws IllegalAccessException {
//		return policyService.findActiveClients(term, pageable);
//	}

	@RequestMapping(value = { "uwClients" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public Page<ClientsDto> selectClients(@RequestParam(value = "term", required = false) String term, Pageable pageable)
			throws IllegalAccessException {
		return policyService.findActiveClients(term, pageable);
	}
	@RequestMapping(value = { "allClients" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public Page<ClientDef> selectAllClients(@RequestParam(value = "term", required = false) String term, Pageable pageable)
			throws IllegalAccessException {
		return policyService.findAllClients(term, pageable);
	}
	@RequestMapping(value = { "allPolicies" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public Page<PolicyTrans> selectAllPolicies(@RequestParam(value = "term", required = false) String term, Pageable pageable)
			throws IllegalAccessException {
		return policyService.findAllPols(term, pageable);
	}
	@RequestMapping(value = { "allRisksLov" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public Page<RiskTrans> allRisksLov(@RequestParam(value = "term", required = false) String term, Pageable pageable)
			throws IllegalAccessException {
		return policyService.allRisksLov(term, pageable);
	}
	@RequestMapping(value = { "clientPolicies" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public Page<PolicyTrans> selectClientPolicy(@RequestParam(value = "term", required = false) String term,@RequestParam("clientId") Long clientId, Pageable pageable)
			throws IllegalAccessException {
		return policyService.findClientPolicies(term, pageable,clientId);
	}


	
	@RequestMapping(value = { "uwBinders" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public Page<BinderDTO> selectBinders(@RequestParam(value = "term", required = false) String term, Pageable pageable)
			throws IllegalAccessException {
		return policyService.findInsuranceBinder(term, pageable,null,null);
	}

	@RequestMapping(value = { "uwMultiProducts" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public Page<ProductsDef> selectMultiProducts(@RequestParam(value = "term", required = false) String term, Pageable pageable)
			throws IllegalAccessException {
		return policyService.findMultiProducts(term, pageable);
	}



//	@RequestMapping(value = { "binderCardTypes" }, method = { RequestMethod.GET })
//	@ResponseBody
//	public List<BinderCardTypes> getBinderCardTypes(@RequestParam(value = "binCode", required = false) Long binCode, @RequestParam(value = "wefDate", required = false) Date wefDate)
//			throws IllegalAccessException {
//
//		return policyService.getBinderCardTypes(terbinCode,wefDate);
//	}


	
	@RequestMapping(value = { "uwcurrencies" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public Page<CurrencyDTO> selectCurrencies(@RequestParam(value = "term", required = false) String term, Pageable pageable)
			throws IllegalAccessException {
		return policyService.findCurrencies(term, pageable);
	}

	@RequestMapping(value = { "othercurrencies" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public Page<CurrencyDTO> selectOtherCurrencies(@RequestParam(value = "term", required = false) String term, Pageable pageable)
			throws IllegalAccessException {
		return policyService.findOtherCurrencies(term, pageable);
	}
	
	@RequestMapping(value = { "uwpaymentmodes" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public Page<PaymentModesDTO> selectPaymentModes(@RequestParam(value = "term", required = false) String term, Pageable pageable)
			 {
		return policyService.findPaymentModes(term, pageable);
	}

	@RequestMapping(value = { "inhouseagents" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public Page<AccountDef> selInhouseAgents(@RequestParam(value = "term", required = false) String term, Pageable pageable)
			throws IllegalAccessException {
		return policyService.findInhouseAgents(term, pageable);
	}

	@RequestMapping(value = { "uwbranches" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public Page<BranchDTO> selectBranches(@RequestParam(value = "term", required = false) String term, Pageable pageable) {
		return policyService.findUserBranches(term, pageable);
	}

	@RequestMapping(value = { "allbranches" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public Page<BranchDTO> selectAllBranches(@RequestParam(value = "term", required = false) String term, Pageable pageable) {
		return policyService.findAllBranches(term, pageable);
	}
	
	@RequestMapping(value = { "getWetDate" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public Date getPolicyWetDate(@RequestParam(value = "wefDate", required = false) Date wef){
		return dateUtils.getWetDate(wef);
	}

	@RequestMapping(value = { "getMaturityDate" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public Date getPolicyWetDate(@RequestParam(value = "wefDate", required = false) Date wef,@RequestParam(value = "polTerm", required = false) Integer polTerm){
		if (polTerm!=null) {
			return dateUtils.getMaturityDate(wef, polTerm);
		} else return null;

	}
	
	@RequestMapping(value = { "uwsubclasses" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public Page<SubClassDef> selectSubclasses(@RequestParam(value = "term", required = false) String term, @RequestParam("bindCode") Long bindCode,Pageable pageable)
			throws IllegalAccessException {
		return policyService.findBinderSubclasses(term, pageable, bindCode);
	}
	
	@RequestMapping(value = { "riskCoverTypes" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public Page<CoverTypesDef> selectCoverTypes(@RequestParam(value = "term", required = false) String term, @RequestParam("bindCode") Long bindCode, @RequestParam("subCode") Long subCode,Pageable pageable)
			throws IllegalAccessException {
		return policyService.findBinderCoverTypes(term, pageable, bindCode,subCode);
	}

	@RequestMapping(value = { "riskSubCoverTypes" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public Page<CoverTypesDef> selectCoverTypes(@RequestParam(value = "term", required = false) String term, @RequestParam("bindCode") Long bindCode,Pageable pageable)
			throws IllegalAccessException {
		return policyService.findBinderSubCoverTypes(term, pageable, bindCode);
	}
	
	@RequestMapping(value = { "getBinderPremRates" }, method = {org.springframework.web.bind.annotation.RequestMethod.GET })
	public ResponseEntity<Set<RiskSectionBean>> getBinderPremRates(
			@RequestParam(value = "detId", required = false) Long detId) throws BadRequestException {
		Set<RiskSectionBean> rates = policyService.getBinderPremRates(detId);
		return new ResponseEntity<Set<RiskSectionBean>>(rates, HttpStatus.OK);
	}

	@RequestMapping(value = { "getClientAge" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET })
	public ResponseEntity<Integer> getClientAge(
			@RequestParam(value = "clientId", required = false) Long clientId) throws BadRequestException {
		if(clientId==null)
			throw new BadRequestException("Client cannot be null");
		ClientDef clientDef = clientRepository.findOne(clientId);
        if(clientDef.getDob()==null)
            throw new BadRequestException("Client Date Of Birth Is Required...");
		Integer age = dateUtilities.getAge(clientDef.getDob());
		return new ResponseEntity<Integer>(age, HttpStatus.OK);
	}


	@RequestMapping(value = { "getBinderClientPremRates" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET })
	public ResponseEntity<Set<RiskSectionBean>> getBinderClientPremRates(
			@RequestParam(value = "detId", required = false) Long detId,@RequestParam(value = "age", required = false) Long age) throws BadRequestException {
		Set<RiskSectionBean> rates = policyService.getBinderClientPremRates(detId,age);
		return new ResponseEntity<Set<RiskSectionBean>>(rates, HttpStatus.OK);
	}

	@RequestMapping(value = { "getNewClientPremiumItems" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET })
	public ResponseEntity<List<PremRatesDef>> getNewClientPremiumItems(
			@RequestParam(value = "detId", required = false) Long detId,@RequestParam(value = "riskId", required = false) Long riskId,
			@RequestParam(value = "secName", required = false) String secName,@RequestParam(value = "age", required = false) Long age) throws BadRequestException {
		List<PremRatesDef> rates = policyService.getNewSectPremiumItems(detId,riskId,secName,age);
		return new ResponseEntity<List<PremRatesDef>>(rates, HttpStatus.OK);
	}
	
	@RequestMapping(value = { "getNewPremiumItems" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET })
	public ResponseEntity<List<PremRatesDef>> getNewPremItems(
			@RequestParam(value = "detId", required = false) Long detId,@RequestParam(value = "riskId", required = false) Long riskId,
			@RequestParam(value = "secName", required = false) String secName) throws BadRequestException {
		List<PremRatesDef> rates = policyService.getNewPremiumItems(detId,riskId,secName);
		return new ResponseEntity<List<PremRatesDef>>(rates, HttpStatus.OK);
	}

	@RequestMapping(value = { "deletePolRecord" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletePolRecord(@RequestParam(value = "policyId", required = false) Long policyId,HttpServletRequest request) throws BadRequestException {
		endorseService.deletePolicyRecord(policyId,false);
	}

	@RequestMapping(value = { "createPolicy" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.POST })
	@Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
	public ResponseEntity<PolicyTrans> createPolicyTrans(@RequestBody PolicyTrans policy,
			HttpServletRequest request) throws BadRequestException {
		PolicyTrans created = policyService.createPolicy(policy);
		Long polCode =created.getPolicyId();
		request.getSession().setAttribute("policyCode", polCode);;
		 if("NB".equalsIgnoreCase( created.getTransType()) || "SP".equalsIgnoreCase( created.getTransType())|| "EX".equalsIgnoreCase( created.getTransType())|| "RN".equalsIgnoreCase( created.getTransType()))
			 try {
				 premiumService.computePrem(polCode);
			 } catch (IOException e) {
				 e.printStackTrace();
				 throw new BadRequestException(e.getMessage());
			 }
		 else if("EN".equalsIgnoreCase( created.getTransType())){
			 try {
				 premiumService.computeEndorsePremium(polCode);
			 } catch (IOException e) {
				 throw new BadRequestException(e.getMessage());
			 }
		 }

		return new ResponseEntity<PolicyTrans>(created, HttpStatus.OK);
	}

	@RequestMapping(value = "/claimtrans", method = RequestMethod.POST)
	public String editClaimForm(@Valid @ModelAttribute ModelHelperForm helperForm, Model model, HttpServletRequest request) {
		model.addAttribute("clmId", helperForm.getId());
		request.getSession().setAttribute("claimId", helperForm.getId());
		model.addAttribute("claimId",helperForm.getId());
		return "claimdetails";
	}
	@RequestMapping(value = { "getClaimDetails/{clmId}" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET })
	public ResponseEntity<ClaimDetailsDTO> getClmBookings(@PathVariable Long clmId) throws BadRequestException {
		ClaimDetailsDTO booking = claimService.getClaimInformation(clmId);
		return new ResponseEntity<>(booking, HttpStatus.OK);
	}

	@RequestMapping(value = { "issueCertificate" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.POST })
	@Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
	public ResponseEntity<String> issueCertificte( HttpServletRequest request) throws BadRequestException {
		Long polCode = (Long) request.getSession().getAttribute("policyCode");
		authService.generateCert(polCode);
		return new ResponseEntity<String>("created", HttpStatus.OK);
	}

	@RequestMapping(value = { "createPolicyMakeReady" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.POST })
	@Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
	public ResponseEntity<PolicyTrans> createPolicyMakeReady(@RequestBody PolicyTrans policy,
														 HttpServletRequest request) throws BadRequestException {
		Long polCode = (Long) request.getSession().getAttribute("policyCode");
		PolicyTrans created = policyService.getPolicyDetails(polCode);
	request.getSession().setAttribute("policyCode", polCode);
		//Long polCode = (Long) request.getSession().getAttribute("policyCode");
		//PolicyTrans created = policyService.getPolicyDetails(polCode);
		if("NB".equalsIgnoreCase( created.getTransType()) || "SP".equalsIgnoreCase( created.getTransType())|| "EX".equalsIgnoreCase( created.getTransType())|| "RN".equalsIgnoreCase( created.getTransType()))
			try {
				premiumService.computePrem(polCode);
			} catch (IOException e) {
				throw new BadRequestException(e.getMessage());
			}
		else if("EN".equalsIgnoreCase( created.getTransType())){
			try {
				premiumService.computeEndorsePremium(polCode);
			} catch (IOException e) {
				throw new BadRequestException(e.getMessage());
			}
		}
		String response = policyService.makeReady(polCode);
		created.setStatus(response);
		return new ResponseEntity<PolicyTrans>(created, HttpStatus.OK);
	}


	@RequestMapping(value = { "createLifePolMakeReady" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.POST })
	@Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
	public ResponseEntity<PolicyTrans> createLifePolMakeReady(@RequestBody PolicyTrans policy,
															 HttpServletRequest request) throws BadRequestException {
		PolicyTrans created = policyService.createLifePolicy(policy);
		Long polCode =created.getPolicyId();
		request.getSession().setAttribute("policyCode", polCode);;
		System.out.println("Trans Type..."+created.getTransType());
		if("NB".equalsIgnoreCase( created.getTransType()) || "SP".equalsIgnoreCase( created.getTransType())|| "EX".equalsIgnoreCase( created.getTransType())|| "RN".equalsIgnoreCase( created.getTransType()))
			try {
				premiumService.computeLifePrem(polCode);
			} catch (IOException e) {
				throw new BadRequestException(e.getMessage());
			}
		policyService.makeReady(polCode);
		return new ResponseEntity<PolicyTrans>(created, HttpStatus.OK);
	}



	@RequestMapping(value = { "createLifePolicy" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.POST })
	@Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
	public ResponseEntity<PolicyTrans> createLifePolicyTrans(@RequestBody PolicyTrans policy, HttpServletRequest request) throws BadRequestException {
		PolicyTrans created = policyService.createLifePolicy(policy);
		Long polCode =created.getPolicyId();
		request.getSession().setAttribute("policyCode", polCode);
		if("NB".equalsIgnoreCase( created.getTransType()) || "SP".equalsIgnoreCase( created.getTransType())|| "EX".equalsIgnoreCase( created.getTransType())|| "RN".equalsIgnoreCase( created.getTransType()))
			try {
				premiumService.computeLifePrem(polCode);
			} catch (IOException e) {
				e.printStackTrace();
				throw new BadRequestException(e.getMessage());
			}
		return new ResponseEntity<PolicyTrans>(created, HttpStatus.OK);
	}

	@RequestMapping(value = { "binderPolTerms/{binCode}" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public List<BinderPolTerms> selectageBracket(@PathVariable long binCode)
			throws BadRequestException {
		return  lifeService.getPolTerms(binCode);
	}

	@RequestMapping(value = { "createRisk" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.POST })
	@Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
	public ResponseEntity<String> createRiskTrans(@RequestBody RiskTrans risk,
			HttpServletRequest request) throws BadRequestException {
		Long polCode = (Long) request.getSession().getAttribute("policyCode");
		risk.setPolicy(policyService.getPolicyDetails(polCode));
		policyService.createRisk(risk);
		PolicyTrans policy = policyService.getPolicyDetails(polCode);
		if("L".equalsIgnoreCase(policy.getProduct().getProGroup().getPrgType())){
			try {
				premiumService.computeLifePrem(polCode);
			} catch (IOException e) {
				throw new BadRequestException(e.getMessage());
			}
		}else {
			if ("NB".equalsIgnoreCase(policy.getTransType()) || "SP".equalsIgnoreCase(policy.getTransType()) || "EX".equalsIgnoreCase(policy.getTransType()) || "RN".equalsIgnoreCase(policy.getTransType()))
				try {
					premiumService.computePrem(polCode);
				} catch (IOException e) {
					throw new BadRequestException(e.getMessage());
				}
			else if ("EN".equalsIgnoreCase(policy.getTransType()) || "CN".equalsIgnoreCase(policy.getTransType())) {
				try {
					premiumService.computeEndorsePremium(polCode);
				} catch (IOException e) {
					throw new BadRequestException(e.getMessage());
				}
			}
		}
		return new ResponseEntity<String>("Ok", HttpStatus.OK);
	}
	
	@RequestMapping(value = { "policyRisks/{policyCode}/{polBindCode}" }, method = { RequestMethod.GET })
	@ResponseBody
	public DataTablesResult<RiskTrans> getPolicyRisks(@DataTable DataTablesRequest pageable,@PathVariable Long policyCode,@PathVariable Long polBindCode)
			throws IllegalAccessException {
		return policyService.findRiskTransactions(pageable,policyCode,polBindCode);
	}
	
	
	
	@RequestMapping(value = { "risksSections/{riskId}" }, method = { RequestMethod.GET })
	@ResponseBody
	public DataTablesResult<SectionTrans> getRiskSections(@DataTable DataTablesRequest pageable,@PathVariable Long riskId)
			throws IllegalAccessException {
		return policyService.findRiskSections(pageable,riskId);
	}


	@RequestMapping(value = { "risksIntParties/{riskId}" }, method = { RequestMethod.GET })
	@ResponseBody
	public DataTablesResult<RiskInterestedParties> getRiskIntParties(@DataTable DataTablesRequest pageable,@PathVariable Long riskId)
			throws IllegalAccessException {
		return policyService.findRiskInterestedParties(pageable,riskId);
	}
	

		@RequestMapping(value = { "getPolicyDetails" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET })
	public ResponseEntity<PolicyTrans> getPolicyDetails(HttpServletRequest request) throws BadRequestException {
		Long polCode = (Long) request.getSession().getAttribute("policyCode");
		PolicyTrans created = policyService.getPolicyDetails(polCode);

		return new ResponseEntity<PolicyTrans>(created, HttpStatus.OK);
	}
	

	@RequestMapping(value = { "riskselectsections" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public Page<SectionBean> selectRiskSections(@RequestParam(value = "term", required = false) String term, @RequestParam("detId") Long detId,Pageable pageable)
			throws IllegalAccessException {
		return policyService.findPremSections(term, pageable, detId);
	}

	@RequestMapping(value = { "saveRiskSections" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	@Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
	public void createPremRates(SectionTrans section,HttpServletRequest request) throws IllegalAccessException, IOException, BadRequestException {
		policyService.createRiskSection(section);
		Long polCode = (Long) request.getSession().getAttribute("policyCode");
		PolicyTrans policy = policyService.getPolicyDetails(polCode);
		if("NB".equalsIgnoreCase( policy.getTransType()) || "SP".equalsIgnoreCase( policy.getTransType())|| "EX".equalsIgnoreCase( policy.getTransType())|| "RN".equalsIgnoreCase( policy.getTransType()))
		 premiumService.computePrem(polCode);
		 else if("EN".equalsIgnoreCase( policy.getTransType())){
			 premiumService.computeEndorsePremium(polCode);
		 }
	}
	
	 @RequestMapping(value = { "deleteRiskSection/{sectCode}" }, method = {
				org.springframework.web.bind.annotation.RequestMethod.GET })
		@ResponseStatus(HttpStatus.NO_CONTENT)
	 @Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
		public void deleteRiskSection(@PathVariable Long sectCode,HttpServletRequest request) throws BadRequestException {
		 policyService.deleteRiskSection(sectCode);
		 Long polCode = (Long) request.getSession().getAttribute("policyCode");
		 PolicyTrans policy = policyService.getPolicyDetails(polCode);
		 if("NB".equalsIgnoreCase( policy.getTransType()) || "SP".equalsIgnoreCase( policy.getTransType())|| "EX".equalsIgnoreCase( policy.getTransType())|| "RN".equalsIgnoreCase( policy.getTransType()))
			 try {
				 premiumService.computePrem(polCode);
			 } catch (IOException e) {
				 throw new BadRequestException(e.getMessage());
			 }
		 else if("EN".equalsIgnoreCase( policy.getTransType())){
			 try {
				 premiumService.computeEndorsePremium(polCode);
			 } catch (IOException e) {
				 throw new BadRequestException(e.getMessage());
			 }
		 }
		}
	 
	 @RequestMapping(value = { "policyClauses" }, method = { RequestMethod.GET })
		@ResponseBody
		public DataTablesResult<PolicyClauses> getPolicyClauses(@DataTable DataTablesRequest pageable,HttpServletRequest request)
				throws IllegalAccessException {
		     Long policyCode = (Long) request.getSession().getAttribute("policyCode");
			return policyService.findPolicyClauses(pageable,policyCode);
		}
	
	 @RequestMapping(value = { "policyTaxes" }, method = { RequestMethod.GET })
		@ResponseBody
		public DataTablesResult<PolicyTaxes> getPolicyTaxes(@DataTable DataTablesRequest pageable,HttpServletRequest request)
				throws IllegalAccessException {
		 Long policyCode = (Long) request.getSession().getAttribute("policyCode");
			return policyService.findPolicyTaxes(pageable,policyCode);
		}
	 
	 @RequestMapping(value = { "deleteRisk/{riskId}" }, method = {
				org.springframework.web.bind.annotation.RequestMethod.GET })
		@ResponseStatus(HttpStatus.NO_CONTENT)
		public void deleteRisk(@PathVariable Long riskId,HttpServletRequest request) throws BadRequestException {
		 policyService.deleteRisk(riskId);
		 Long polCode = (Long) request.getSession().getAttribute("policyCode");
		 PolicyTrans policy = policyService.getPolicyDetails(polCode);
		 if("NB".equalsIgnoreCase( policy.getTransType()) || "SP".equalsIgnoreCase( policy.getTransType())|| "EX".equalsIgnoreCase( policy.getTransType())|| "RN".equalsIgnoreCase( policy.getTransType()))
			 try {
				 premiumService.computePrem(polCode);
			 } catch (IOException e) {
				 throw new BadRequestException(e.getMessage());
			 }
		 else if("EN".equalsIgnoreCase( policy.getTransType())){
			 try {
				 premiumService.computeEndorsePremium(polCode);
			 } catch (IOException e) {
				 throw new BadRequestException(e.getMessage());
			 }
		 }
		}
	 
	 @RequestMapping(value = { "makePolicyReady" }, method = {RequestMethod.POST })
	 @ResponseStatus(HttpStatus.CREATED)
		public ResponseForm makePolicyReady(HttpServletRequest request) throws BadRequestException {
		 Long polCode = (Long) request.getSession().getAttribute("policyCode");
		 PolicyTrans policy = policyService.getPolicyDetails(polCode);
		 if("NB".equalsIgnoreCase( policy.getTransType()) || "SP".equalsIgnoreCase( policy.getTransType())|| "EX".equalsIgnoreCase( policy.getTransType())|| "RN".equalsIgnoreCase( policy.getTransType()))
			 try {
				 premiumService.computePrem(polCode);
			 } catch (IOException e) {
				 throw new BadRequestException(e.getMessage());
			 }
			 else if("EN".equalsIgnoreCase( policy.getTransType())){
			 try {
				 premiumService.computeEndorsePremium(polCode);
			 } catch (IOException e) {
				 throw new BadRequestException(e.getMessage());
			 }
		 }
			 String response =  policyService.makeReady(polCode);
			 return new ResponseForm(response);
			
		}
	 
	 @RequestMapping(value = { "undoMakeReady" }, method = {
				org.springframework.web.bind.annotation.RequestMethod.GET })
		@ResponseStatus(HttpStatus.NO_CONTENT)
		public void undoMakePolicyReady(HttpServletRequest request) throws BadRequestException {
		 Long polCode = (Long) request.getSession().getAttribute("policyCode");
		 policyService.undoMakeReady(polCode);
			
		}
	 
	 @RequestMapping(value = { "authorizePolicy" }, method = {
				org.springframework.web.bind.annotation.RequestMethod.GET })
		@ResponseStatus(HttpStatus.NO_CONTENT)
		public void authorizePolicy(HttpServletRequest request,@RequestParam(value = "refundAmt", required = false) BigDecimal refundAmt) throws BadRequestException {
		 Long polCode = (Long) request.getSession().getAttribute("policyCode");
		 authService.authorizePolicy(polCode,refundAmt);
			
		}


	@RequestMapping(value = { "authorizeLifePolicy" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void authorizeLifePolicy(HttpServletRequest request,@RequestParam(value = "refundAmt", required = false) BigDecimal refundAmt) throws BadRequestException {
		Long polCode = (Long) request.getSession().getAttribute("policyCode");
		authService.authorizeLifePolicy(polCode);

	}

	@RequestMapping(value = { "policyReceipts/{policyCode}" }, method = { RequestMethod.GET })
	@ResponseBody
	public DataTablesResult<LifeReceipts> getPolicyReceipts(@DataTable DataTablesRequest pageable, @PathVariable Long policyCode)
			throws IllegalAccessException {
		return lifeService.findPolReceipts(pageable,policyCode);
	}


	@RequestMapping(value = { "receiptAllocs/{receiptCode}" }, method = { RequestMethod.GET })
	@ResponseBody
	public DataTablesResult<LifeReceiptAllocations> getReceiptAllocs(@DataTable DataTablesRequest pageable, @PathVariable Long receiptCode)
			throws IllegalAccessException {
		return lifeService.findReceiptsAllocations(pageable,receiptCode);
	}


	@RequestMapping(value = { "allocationCommission/{AllocId}" }, method = { RequestMethod.GET })
	@ResponseBody
	public DataTablesResult<ReceiptAllocationCommissions> getAllocCommission(@DataTable DataTablesRequest pageable, @PathVariable Long AllocId)
			throws IllegalAccessException {
		return lifeService.findAllocationComm(pageable,AllocId);
	}


	@RequestMapping(value = { "dispatchDocs" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void dispatchDocs(HttpServletRequest request) throws BadRequestException {
		Long polCode = (Long) request.getSession().getAttribute("policyCode");
		policyService.dispatchDocuments(polCode);

	}
	 
	 @RequestMapping(value = { "createPremiumItems" }, method = {
				org.springframework.web.bind.annotation.RequestMethod.POST })
		@Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
		public ResponseEntity<String> createPremiumItems(@RequestBody RiskBean sections,
				HttpServletRequest request) throws BadRequestException {
			 policyService.createRiskSections(sections);
			 Long polCode = (Long) request.getSession().getAttribute("policyCode");
			 PolicyTrans policy = policyService.getPolicyDetails(polCode);
			 if("NB".equalsIgnoreCase( policy.getTransType()) || "SP".equalsIgnoreCase( policy.getTransType())|| "EX".equalsIgnoreCase( policy.getTransType())|| "RN".equalsIgnoreCase( policy.getTransType()))
				 try {
					 premiumService.computePrem(polCode);
				 } catch (IOException e) {
					 throw new BadRequestException(e.getMessage());
				 }
			 else if("EN".equalsIgnoreCase( policy.getTransType())){
				 try {
					 premiumService.computeEndorsePremium(polCode);
				 } catch (IOException e) {
					 throw new BadRequestException(e.getMessage());
				 }
			 }
			return new ResponseEntity<String>("Created", HttpStatus.OK);
		}
	 
	 @RequestMapping(value = { "getNewClauses" }, method = {
				org.springframework.web.bind.annotation.RequestMethod.GET })
		public ResponseEntity<Set<PolicyClauses>> getNewClauses(HttpServletRequest request) throws BadRequestException {
		 Long polCode = (Long) request.getSession().getAttribute("policyCode");
		 Set<PolicyClauses> clauses = policyService.getNewClauses(polCode);
			return new ResponseEntity<Set<PolicyClauses>>(clauses, HttpStatus.OK);
		}

	@RequestMapping(value = { "getNewTaxes" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET })
	public ResponseEntity<Set<PolicyTaxes>> getNewTaxes(HttpServletRequest request) throws BadRequestException {
		Long polCode = (Long) request.getSession().getAttribute("policyCode");
		Set<PolicyTaxes> taxes = policyService.getNewTaxes(polCode);
		return new ResponseEntity<Set<PolicyTaxes>>(taxes, HttpStatus.OK);
	}
	 
	    @RequestMapping(value = { "deletePolClause/{clauseId}" }, method = {
				org.springframework.web.bind.annotation.RequestMethod.GET })
		@ResponseStatus(HttpStatus.NO_CONTENT)
		public void deletePolClause(@PathVariable Long clauseId) throws BadRequestException {
		 policyService.deletePolicyClause(clauseId);
		}
	 
	 @RequestMapping(value = { "deletePolTaxes/{taxCode}" }, method = {
				org.springframework.web.bind.annotation.RequestMethod.GET })
		@ResponseStatus(HttpStatus.NO_CONTENT)
	   @Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
		public void deletePolTaxes(@PathVariable Long taxCode,HttpServletRequest request) throws BadRequestException {
			 policyService.deletePolicyTax(taxCode);
			 Long polCode = (Long) request.getSession().getAttribute("policyCode");
			 PolicyTrans policy = policyService.getPolicyDetails(polCode);
			 if("NB".equalsIgnoreCase( policy.getTransType()) || "SP".equalsIgnoreCase( policy.getTransType())|| "EX".equalsIgnoreCase( policy.getTransType())|| "RN".equalsIgnoreCase( policy.getTransType()))
				 try {
					 premiumService.computePrem(polCode);
				 } catch (IOException e) {
					 throw new BadRequestException(e.getMessage());
				 }
			 else if("EN".equalsIgnoreCase( policy.getTransType())){
				 try {
					 premiumService.computeEndorsePremium(polCode);
				 } catch (IOException e) {
					 throw new BadRequestException(e.getMessage());
				 }
			 }
		}
	 
	 @RequestMapping(value = { "createNewClause" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public ResponseEntity<String> createNewClause(@RequestBody PolicyClausesBean clause,
												  HttpServletRequest request) throws BadRequestException {
		Long polCode = (Long) request.getSession().getAttribute("policyCode");
		clause.setPolId(polCode);
		policyService.createClause(clause);
		return new ResponseEntity<String>("Created", HttpStatus.OK);
	}

	@RequestMapping(value = { "createNewTax" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public ResponseEntity<String> createNewTax(@RequestBody PolicyTaxBean tax,
												  HttpServletRequest request) throws BadRequestException {
		Long polCode = (Long) request.getSession().getAttribute("policyCode");
		tax.setPolId(polCode);
		policyService.createTaxes(tax);
		PolicyTrans policy = policyService.getPolicyDetails(polCode);
		if("NB".equalsIgnoreCase( policy.getTransType()) || "SP".equalsIgnoreCase( policy.getTransType())|| "EX".equalsIgnoreCase( policy.getTransType())|| "RN".equalsIgnoreCase( policy.getTransType()))
			try {
				premiumService.computePrem(polCode);
			} catch (IOException e) {
				throw new BadRequestException(e.getMessage());
			}
		else if("EN".equalsIgnoreCase( policy.getTransType())){
			try {
				premiumService.computeEndorsePremium(polCode);
			} catch (IOException e) {
				throw new BadRequestException(e.getMessage());
			}
		}
		return new ResponseEntity<String>("Created", HttpStatus.OK);
	}


	@RequestMapping(value = { "createNewIntParties" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public ResponseEntity<String> createNewIntParties(@RequestBody RiskIntPartiesBean partiesBean,
											   HttpServletRequest request) throws BadRequestException {
		policyService.createIntParties(partiesBean);
		return new ResponseEntity<String>("Created", HttpStatus.OK);
	}
	 
	 @RequestMapping(value = { "createPolicyClause" }, method = {
				org.springframework.web.bind.annotation.RequestMethod.POST })
		@ResponseBody
		public void createPolicyClause(PolicyClauses clause) throws IllegalAccessException, IOException, BadRequestException {
		 policyService.createPolicyClause(clause);
		}
	 
	 @RequestMapping(value = { "createPolicyTax" }, method = {
				org.springframework.web.bind.annotation.RequestMethod.POST })
		@ResponseBody
		@Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
		public void createPolicyTax(PolicyTaxes tax,HttpServletRequest request) throws IllegalAccessException, IOException, BadRequestException {
		 policyService.createPolicyTaxes(tax);
		 Long polCode = (Long) request.getSession().getAttribute("policyCode");
		 PolicyTrans policy = policyService.getPolicyDetails(polCode);
		 if("NB".equalsIgnoreCase( policy.getTransType()) || "SP".equalsIgnoreCase( policy.getTransType())|| "EX".equalsIgnoreCase( policy.getTransType())|| "RN".equalsIgnoreCase( policy.getTransType()))
			 premiumService.computePrem(polCode);
			 else if("EN".equalsIgnoreCase( policy.getTransType())){
				 premiumService.computeEndorsePremium(polCode);
			 }
		}
	 
	 @RequestMapping(value = "rpt_debit_note", method = RequestMethod.GET)
		public ModelAndView invoiceRpt(ModelMap modelMap, HttpServletRequest request, ModelAndView modelAndView)
				throws BadRequestException, IOException {
		    Long polCode = (Long) request.getSession().getAttribute("policyCode");
		 OrganizationDTO organization = orgService.getOrganizationLogoDetails();
		 InputStream in = new ByteArrayInputStream(Files.readAllBytes(Paths.get(organization.getOrgLogo())));
		    BufferedImage image = ImageIO.read(in);
		    modelMap.put("logo", image );
			modelMap.put("datasource", datasource);
			modelMap.put("format", "pdf");
			modelMap.put("polId", polCode);
			modelAndView = new ModelAndView("rpt_debit_note", modelMap);
			return modelAndView;
		}

	@RequestMapping(value = "rpt_renewal_notice_non_motor", method = RequestMethod.GET)
	public ModelAndView renewalNoticeNonMotor(ModelMap modelMap, HttpServletRequest request, ModelAndView modelAndView)
			throws BadRequestException, IOException {
		Long polCode = (Long) request.getSession().getAttribute("policyCode");
		OrganizationDTO organization = orgService.getOrganizationLogoDetails();
		InputStream in = new ByteArrayInputStream(Files.readAllBytes(Paths.get(organization.getOrgLogo())));
		BufferedImage image = ImageIO.read(in);
		modelMap.put("logo", image );
		modelMap.put("datasource", datasource);
		modelMap.put("format", "pdf");
		modelMap.put("polId", polCode);
		modelAndView = new ModelAndView("rpt_renewal_notice_non_motor", modelMap);
		return modelAndView;
	}

	@RequestMapping(value = "rpt_renewal_notice_motor", method = RequestMethod.GET)
	public ModelAndView renewalNoticeMotor(ModelMap modelMap, HttpServletRequest request, ModelAndView modelAndView)
			throws BadRequestException, IOException {
		Long polCode = (Long) request.getSession().getAttribute("policyCode");
		OrganizationDTO organization = orgService.getOrganizationLogoDetails();
		InputStream in = new ByteArrayInputStream(Files.readAllBytes(Paths.get(organization.getOrgLogo())));
		BufferedImage image = ImageIO.read(in);
		modelMap.put("logo", image );
		modelMap.put("datasource", datasource);
		modelMap.put("format", "pdf");
		modelMap.put("polId", polCode);

		modelAndView = new ModelAndView("rpt_renewal_notice_motor", modelMap);
		return modelAndView;
	}

	@RequestMapping(value = "rpt_valuation_rpt", method = RequestMethod.GET)
	public ModelAndView rpt_valuation_rpt(ModelMap modelMap, HttpServletRequest request, ModelAndView modelAndView)
			throws BadRequestException, IOException {
		Long polCode = (Long) request.getSession().getAttribute("policyCode");
		PolicyTrans policyTrans = policyService.getPolicyDetails(polCode);
		File file = new File(orgService.getOrganizationLogoDetails().getOrgLogo());
		InputStream in = new ByteArrayInputStream(Files.readAllBytes(file.toPath()));
		BufferedImage image = ImageIO.read(in);
		modelMap.put("logo", image );
		modelMap.put("datasource", datasource);
		modelMap.put("format", "pdf");
		modelMap.put("polId", polCode);
		modelAndView = new ModelAndView("rpt_valuation_rpt", modelMap);
		return modelAndView;
	}
	 
	 @RequestMapping(value = "rpt_prem_working", method = RequestMethod.GET)
		public ModelAndView premWorking(ModelMap modelMap, HttpServletRequest request, ModelAndView modelAndView)
				throws BadRequestException, IOException {
		    Long polCode = (Long) request.getSession().getAttribute("policyCode");
		 OrganizationDTO organization = orgService.getOrganizationLogoDetails();
		 InputStream in = new ByteArrayInputStream(Files.readAllBytes(Paths.get(organization.getOrgLogo())));
		    BufferedImage image = ImageIO.read(in);
		    modelMap.put("logo", image );
			modelMap.put("datasource", datasource);
			modelMap.put("format", "pdf");
			modelMap.put("polId", polCode);
			modelAndView = new ModelAndView("rpt_prem_working", modelMap);
			return modelAndView;
		}
	 
	 @RequestMapping(value = "rpt_risk_note", method = RequestMethod.GET)
		public ModelAndView riskNote(ModelMap modelMap, HttpServletRequest request, ModelAndView modelAndView)
				throws BadRequestException, IOException {
		    Long polCode = (Long) request.getSession().getAttribute("policyCode");
		    OrganizationDTO organization = orgService.getOrganizationLogoDetails();
		    InputStream in = new ByteArrayInputStream(Files.readAllBytes(Paths.get(organization.getOrgLogo())));
		    BufferedImage image = ImageIO.read(in);
		    modelMap.put("logo", image );
			modelMap.put("datasource", datasource);
			modelMap.put("format", "pdf");
			modelMap.put("polId", polCode);
			modelAndView = new ModelAndView("rpt_risk_note", modelMap);
			return modelAndView;
		}

	 @RequestMapping(value = "rpt_endorse", method = RequestMethod.GET)
		public ModelAndView endorsementReport(ModelMap modelMap, HttpServletRequest request, ModelAndView modelAndView)
				throws BadRequestException, IOException {
		    Long polCode = (Long) request.getSession().getAttribute("policyCode");
		 OrganizationDTO organization = orgService.getOrganizationLogoDetails();
		 InputStream in = new ByteArrayInputStream(Files.readAllBytes(Paths.get(organization.getOrgLogo())));
		    BufferedImage image = ImageIO.read(in);
		    modelMap.put("logo", image );
			modelMap.put("datasource", datasource);
			modelMap.put("format", "pdf");
			modelMap.put("polId", polCode);
			modelAndView = new ModelAndView("rpt_endorse", modelMap);
			return modelAndView;
		}


	 
	 
	 @RequestMapping(value = { "polactiverisks" }, method = { RequestMethod.GET })
		@ResponseBody
		public DataTablesResult<PolicyActiveRisks> getActiveRisks(@DataTable DataTablesRequest pageable,@RequestParam(value = "riskId", required = false) String riskId,
				@RequestParam(value = "insuredId", required = false) Long insuredId,
				@RequestParam(value = "policyCode", required = false) Long policyCode)
				throws IllegalAccessException {
			return endorseService.findActiveRisks(pageable, insuredId, riskId, policyCode);
		}
	 
	 
	 @RequestMapping(value = { "endorseRisk" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	 @Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
	 @ResponseStatus(HttpStatus.NO_CONTENT)
		public void endorseRisk(@RequestParam(value = "activeRiskCode", required = false) Long activeRiskCode,@RequestParam(value = "endorseType", required = false) String endorseType,  HttpServletRequest request) throws BadRequestException{
		 Long polCode = (Long) request.getSession().getAttribute("policyCode");

         Long countRisks = endorseService.findRiskExpiredSections(polCode);

         if(countRisks>0 && !"S".equalsIgnoreCase(endorseType)){
             throw new BadRequestException("The Risk Being Endorsed Has Expired Sections That need to Be Reinstated. Reinstate the sections first before proceeding.");
         } if(countRisks<1 && "S".equalsIgnoreCase(endorseType) ){
			 throw new BadRequestException("The Risk Being Reinstated Has No Expired Sections... ");
		 }
		 endorseService.endorseRisk(activeRiskCode,endorseType,null);
		 try {
			 premiumService.computeEndorsePremium(polCode);
		 } catch (IOException e) {
			 throw new BadRequestException(e.getMessage());
		 }
	 }
	 
	 
	 @RequestMapping(value = { "getPolicyRemarks" }, method = {
				org.springframework.web.bind.annotation.RequestMethod.GET })
		public ResponseEntity<PolicyRemarks> getPolicyRemarks(HttpServletRequest request) throws BadRequestException {
		 Long polCode = (Long) request.getSession().getAttribute("policyCode");
		PolicyRemarks remarks = policyService.getPolicyRemarks(polCode);
			return new ResponseEntity<PolicyRemarks>(remarks, HttpStatus.OK);
		}
	 
	 
	 @RequestMapping(value = { "getNewPolicyRemarks" }, method = { RequestMethod.GET })
		@ResponseBody
		public DataTablesResult<EndorsementRemarks> getNewPolicyRemarks(@DataTable DataTablesRequest pageable,@RequestParam(value = "policyCode", required = false) Long policyCode)
				throws IllegalAccessException {
			return policyService.findEndorsementRemarks(pageable, policyCode);
		}
	 
	 
	 @RequestMapping(value = { "createPolicyRemarks" }, method = {
				org.springframework.web.bind.annotation.RequestMethod.POST })
		@ResponseStatus(HttpStatus.CREATED)
		public void createPolicyRemarks(PolicyRemarks remarks) throws IllegalAccessException, BadRequestException, IOException {
		 
			policyService.saveEndorsementRemarks(remarks);
		}
	 
	 
	 @RequestMapping(value = { "enquiryPolicies" }, method = { RequestMethod.GET })
		@ResponseBody
		public DataTablesResult<EndorsementsDTO> getEnquiryPolicies(@DataTable DataTablesRequest pageable,
																	@RequestParam(value = "policyNo", required = false) String policyNo,
																	@RequestParam(value = "riskShtDesc", required = false) String riskShtDesc,
																	@RequestParam(value = "refno", required = false) String refno,
																	@RequestParam(value = "clientCode", required = false) Long clientCode,
																	@RequestParam(value = "agentCode", required = false) Long agentCode,
																	@RequestParam(value = "prodCode", required = false) Long prodCode){
			return policyService.findEnquiryPolicies(pageable, refno, clientCode, policyNo, riskShtDesc,agentCode,prodCode);
		}

	@RequestMapping(value = { "enquiryActivePolicies" }, method = { RequestMethod.GET })
	@ResponseBody
	public DataTablesResult<PolicyTrans> getEnquiryActivePolicies(@DataTable DataTablesRequest pageable,
															@RequestParam(value = "policyNo", required = false) String policyNo,
															@RequestParam(value = "endorseNumber", required = false) String endorseNumber,
															@RequestParam(value = "refno", required = false) String refno,
															@RequestParam(value = "clientCode", required = false) Long clientCode,
															@RequestParam(value = "agentCode", required = false) Long agentCode,
															@RequestParam(value = "prodCode", required = false) Long prodCode) throws IllegalAccessException {
		return policyService.findActiveEnquiryPolicies(pageable, refno, clientCode, policyNo, endorseNumber,agentCode,prodCode);
	}

	@RequestMapping(value = { "enquiryMedPolicies" }, method = { RequestMethod.GET })
	@ResponseBody
	public DataTablesResult<PolicyTrans> getEnquiryMedPolicies(@DataTable DataTablesRequest pageable,
															@RequestParam(value = "policyNo", required = false) String policyNo,
															@RequestParam(value = "endorseNumber", required = false) String endorseNumber,
															@RequestParam(value = "refno", required = false) String refno,
															@RequestParam(value = "clientCode", required = false) Long clientCode,
															@RequestParam(value = "agentCode", required = false) Long agentCode,
															@RequestParam(value = "prodCode", required = false) Long prodCode) throws IllegalAccessException {
		return policyService.findEnquiryMedPolicies(pageable, refno, clientCode, policyNo, endorseNumber,agentCode,prodCode);
	}

	@RequestMapping(value = { "enquiryActiveMedPolicies" }, method = { RequestMethod.GET })
	@ResponseBody
	public DataTablesResult<PolicyTrans> getEnquiryActiveMedPolicies(@DataTable DataTablesRequest pageable,
															   @RequestParam(value = "policyNo", required = false) String policyNo,
															   @RequestParam(value = "endorseNumber", required = false) String endorseNumber,
															   @RequestParam(value = "refno", required = false) String refno,
															   @RequestParam(value = "clientCode", required = false) Long clientCode,
															   @RequestParam(value = "agentCode", required = false) Long agentCode,
															   @RequestParam(value = "prodCode", required = false) Long prodCode) throws IllegalAccessException {
		return policyService.findEnquiryActiveorLapsedMedPolicies(pageable, refno, clientCode, policyNo, endorseNumber,agentCode,prodCode);
	}

	@RequestMapping(value = { "pendingPolicies" }, method = { RequestMethod.GET })
	@ResponseBody
	public DataTablesResult<PolicyTrans> getPendingPolicies(@DataTable DataTablesRequest pageable,
															@RequestParam(value = "policyNo", required = false) String policyNo,
															@RequestParam(value = "endorseNumber", required = false) String endorseNumber,
															@RequestParam(value = "refno", required = false) String refno,
															@RequestParam(value = "clientCode", required = false) Long clientCode,
															@RequestParam(value = "agentCode", required = false) Long agentCode,
															@RequestParam(value = "prodCode", required = false) Long prodCode) throws IllegalAccessException {
		return policyService.findPendingPolicies(pageable, refno, clientCode, policyNo, endorseNumber,agentCode,prodCode);
	}

	@RequestMapping(value = { "riskCerts/{riskCode}" }, method = { RequestMethod.GET })
	@ResponseBody
	public DataTablesResult<PolicyCerts> getRiskCerts(@DataTable DataTablesRequest pageable, @PathVariable Long riskCode)
			throws IllegalAccessException {
		return certService.findPrintQueue(pageable,riskCode);
	}

	@RequestMapping(value = { "riskBeneficiaries/{riskCode}" }, method = { RequestMethod.GET })
	@ResponseBody
	public DataTablesResult<WIBABeneficiariesDTO> getRiskBeneficiries(@DataTable DataTablesRequest pageable, @PathVariable Long riskCode)
			throws IllegalAccessException {
		return certService.findBeneficiries(pageable,riskCode);
	}

	@RequestMapping(value = { "vehicleDetails/{riskCode}" }, method = { RequestMethod.GET })
	@ResponseBody
	public DataTablesResult<VehicleDetails> getVehicleDetails(@DataTable DataTablesRequest pageable, @PathVariable Long riskCode)
			throws IllegalAccessException {
		return policyService.findVehicleDetails(pageable,riskCode);
	}


	@RequestMapping(value = { "selectBranchCerts" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public Page<BranchCerts> selectBranchCerts(@RequestParam(value = "term", required = false) String term, @RequestParam("riskId") Long riskId, Pageable pageable)
			throws IllegalAccessException {
		return certService.findActiveLots(term, pageable, riskId);
	}

	@RequestMapping(value = { "selectSubclassCertTypes" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public Page<CertTypesDTO> selectSubclassCertTypes(@RequestParam(value = "term", required = false) String term, @RequestParam("riskId") Long riskId, Pageable pageable)
			throws IllegalAccessException {

		return certService.findSubclassCertTypes(term, pageable, riskId);
	}

	@RequestMapping(value = { "saveRiskCertificate" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseStatus(HttpStatus.CREATED)
	public void saveRiskCertificate(RiskCertForm certForm) throws BadRequestException {
		certService.createRiskCert(certForm);
	}

	@RequestMapping(value = { "saveRiskBeneficiary" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseStatus(HttpStatus.CREATED)
	public void saveRiskBeneficiary(WIBABeneficiariesDTO beneficiariesDTO) throws BadRequestException {
		certService.defineWibaBeneficiary(beneficiariesDTO);
	}


	@RequestMapping(value = { "updateRiskCertificate" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseStatus(HttpStatus.CREATED)
	public void updateRiskCertificate(EditRiskCertForm certForm) throws IllegalAccessException, BadRequestException, IOException {
		certService.updateRiskCertificate(certForm);
	}

	@RequestMapping(value = { "deleteRiskCert/{certId}" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteRiskCert(@PathVariable Long certId) throws BadRequestException {
		certService.deleteRiskCertificate(certId);
	}

	@RequestMapping(value = { "deleteRiskBeneficiary/{benId}" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteRiskBeneficiary(@PathVariable Long benId) throws BadRequestException {
		certService.deleteRiskBeneficiary(benId);
	}

	@RequestMapping(value = { "importRiskBeneficiaries" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseStatus(HttpStatus.CREATED)
	public void importRiskBeneficiaries(RiskBeneficiariesImport beneficiariesImport) throws BadRequestException, IOException {

		if ((beneficiariesImport.getFile() != null) && (!beneficiariesImport.getFile().isEmpty())) {
			if (beneficiariesImport.getFile().getSize() != 0) {
				certService.importRiskBeneficiaries(beneficiariesImport);
			}
		}
	}
	@RequestMapping(value = { "allocateRiskCert/{certId}" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void allocateRiskCert(@PathVariable Long certId) throws BadRequestException {
		List<Long> certs = new ArrayList<>();
		PrintCertificateQueue queue = printQueueRepo.findOne(QPrintCertificateQueue.printCertificateQueue.policyCerts.pcId.eq(certId));
		Predicate pred = QBranchCerts.branchCerts.branch.obId.eq(queue.getPolicyCerts().getRisk().getPolicy().getBranch().getObId())
				.and(QBranchCerts.branchCerts.user.id.eq(userUtils.getCurrentUser().getId()))
				.and(QBranchCerts.branchCerts.certLots.underwriter.acctId.eq(queue.getPolicyCerts().getRisk().getPolicy().getAgent().getAcctId()))
				.and(QBranchCerts.branchCerts.certLots.subclass.subId.eq(queue.getPolicyCerts().getRisk().getSubclass().getSubId()))
				.and(QBranchCerts.branchCerts.currentLot.equalsIgnoreCase("Y"));

		BranchCerts  branchCerts = branchCertsRepo.findOne(pred);
		certs.add(queue.getCqId());
		PrintCertBean certBean = new PrintCertBean();
		certBean.setCerts(certs);
		certBean.setBranchCert(branchCerts.getBrnCertId());
		certService.allocateCerts(certBean);
	}

	@RequestMapping(value = { "deallocateRiskCert/{certId}" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deallocateRiskCert(@PathVariable Long certId) throws BadRequestException {
		List<Long> certs = new ArrayList<>();
		PrintCertificateQueue queue = printQueueRepo.findOne(QPrintCertificateQueue.printCertificateQueue.policyCerts.pcId.eq(certId));
		certs.add(queue.getCqId());
		PrintCertBean certBean = new PrintCertBean();
		certBean.setCerts(certs);
		certBean.setBranchCert(queue.getPolicyCerts().getCert().getBrnCertId());
		certService.deallocateCerts(certBean);
	}


	@RequestMapping(value = {"allocatePolCerts"}, method = {
			org.springframework.web.bind.annotation.RequestMethod.POST})
	public ResponseEntity<String> allocateCerts(@RequestBody PrintCertBean certBean) throws  BadRequestException {
		certService.allocatePolCerts(certBean);
		return new ResponseEntity<String>("OK",HttpStatus.OK);
	}

	@RequestMapping(value = "printPolCertificate", method = RequestMethod.POST)
	public ResponseEntity<String> printReport(@RequestBody PrintCerts printCerts)
			throws BadRequestException, IOException {
		certService.createBatchCerts(printCerts.getCertCodes());
		return new ResponseEntity<String>("OK",HttpStatus.OK);
	}

	@RequestMapping(value = "printDigitalCertificate/{ipuCode}", method = RequestMethod.GET)
	public ResponseEntity<DigitalObject> printDigitalCertificate(@PathVariable Long ipuCode)
			throws BadRequestException, IOException {
		 if(ipuCode==null){
			 throw new BadRequestException("Please select only certificate to print....");
		 }
		 DigitalObject digitalObject =  akiAuthenticationService.printCert(ipuCode);
		return new ResponseEntity<>(digitalObject,HttpStatus.OK);
	}

	@RequestMapping(value = "markPrintedPolCerts", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void markPrintedCerts()
			throws BadRequestException, IOException {
		certService.markCertPrinted();
	}

	@RequestMapping(value = {"deallocatePolCerts"}, method = {
			org.springframework.web.bind.annotation.RequestMethod.POST})
	public ResponseEntity<String> deallocateCerts(@RequestBody PrintCertBean certBean) throws  BadRequestException {
		certService.deallocatePolCerts(certBean);
		return new ResponseEntity<String>("OK",HttpStatus.OK);
	}

	@RequestMapping(value = {"getpolicyprintcerts"}, method = {RequestMethod.GET})
	@ResponseBody
	public DataTablesResult<PolicyCertificateDTO> getPrintCerts(@DataTable DataTablesRequest pageable,
																@RequestParam(value = "polId", required = false) Long polId)
	{
		return certService.findPolCertToPrint(pageable,polId);
	}

	@RequestMapping(value = { "riskSchedules/{riskCode}" }, method = { RequestMethod.GET })
	@ResponseBody
	public DataTablesResult<ScheduleTrans> getRiskSchedules(@DataTable DataTablesRequest pageable, @PathVariable Long riskCode)
			throws IllegalAccessException {
		return policyService.findRiskSchedules(pageable,riskCode);
	}

	@RequestMapping(value = { "getRiskSchedules/{riskCode}" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public ResponseEntity<ScheduleBean> getRiskSchedules( @PathVariable Long riskCode) throws BadRequestException {
		ScheduleBean scheduleBean = scheduleService.generateScheduleColumns(riskCode);
		return new ResponseEntity<ScheduleBean>(scheduleBean, HttpStatus.OK);
	}

	@RequestMapping(value = { "saveRiskSchedule" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseStatus(HttpStatus.CREATED)
	public void saveRiskSchedule(VehicleDetails scheduleTrans) throws IllegalAccessException, BadRequestException, IOException {
		policyService.createRiskSchedules(scheduleTrans);
	}

	@RequestMapping(value = { "deleteRiskSchedule/{scheduleId}" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteRiskSchedule(@PathVariable Long scheduleId) throws BadRequestException {
		policyService.deleteRiskSchedule(scheduleId);
	}

	@RequestMapping(value = { "createClient" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public String createClient(ClientDTO tenDef) throws IllegalAccessException, BadRequestException {
		tenDef.setDateregistered(new Date());
		return setupsService.defineClient(tenDef);
	}

	@RequestMapping(value = { "getCommissionRate" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET })
	public ResponseEntity<BigDecimal> getCommissionRate(@RequestParam(value = "detId", required = false) Long detId) throws BadRequestException {
		BigDecimal rates = policyService.getCommissionRate(detId);
		return new ResponseEntity<BigDecimal>(rates, HttpStatus.OK);
	}



	@RequestMapping(value = { "sendEmail" }, method = {org.springframework.web.bind.annotation.RequestMethod.POST })
	public ResponseEntity<String>  sendEmail(@RequestBody MailMessageBean messageBean,HttpServletRequest request) throws BadRequestException {
		System.out.println(messageBean);
		Long polCode = (Long) request.getSession().getAttribute("policyCode");
		mailer.sendEmailAttachments(messageBean,polCode,"P",request);
		return new ResponseEntity<String>("OK", HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping(value ="mailtemplate" )
	public String getMailTemplate(HttpServletResponse response, HttpServletRequest request) throws BadRequestException{
		return templateService.getMailTemplate(MailTemplates.POLICY_TEMPLATE,request);
	}

	@RequestMapping(value = { "getReceiverEmail" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET })
	public ResponseEntity<String> getReceiverEmail(@RequestParam(value = "receiver", required = false) String receiver, HttpServletRequest request) throws BadRequestException {
		Long polCode = (Long) request.getSession().getAttribute("policyCode");
		String email = mailer.getEmailReceivers(polCode,"P",receiver);
		return new ResponseEntity<String>(email, HttpStatus.OK);
	}

	@RequestMapping(value = { "riskDocs/{riskCode}" }, method = { RequestMethod.GET })
	@ResponseBody
	public DataTablesResult<RiskDocs> getRiskDocs(@DataTable DataTablesRequest pageable, @PathVariable Long riskCode)
			throws IllegalAccessException {
		return policyService.findRiskDocs(pageable,riskCode);
	}

	@RequestMapping(value = { "uploadRequiredDocs" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseStatus(HttpStatus.CREATED)
	public void uploadRequiredDocs(UploadBean uploadBean) throws BadRequestException {
		uploadService.uploadRiskDocument(uploadBean);
	}

	@RequestMapping(value = "/riskdocument/{docId}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> thumbnail(@PathVariable Long docId ) throws BadRequestException {
		RiskDocs riskDoc = riskDocsRepo.getRiskDocsVal(docId);
		if(riskDoc.getUrl()!=null){
			InputStream is = null;
			try {
				URL url = new URL(riskDoc.getUrl());
				is = url.openStream ();
				byte[] imageBytes = IOUtils.toByteArray(is);
			} catch (MalformedURLException e) {
				return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
			}
			finally {
				return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
			}
		}
		else {
			byte[] content = uploadService.getRiskDocFileDetails(docId);
			if (content.length > 0) {
				String contentType = uploadService.getDocContentTyoe(docId);
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.parseMediaType(contentType));
				headers.setContentLength(content.length);
				return new ResponseEntity<byte[]>(content, headers, HttpStatus.OK);
			} else {
				return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
			}
		}
	}

	@RequestMapping(value = { "deleteRiskDoc/{docId}" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteRiskDoc(@PathVariable Long docId) throws BadRequestException {
		uploadService.deleteRiskDoc(docId);
	}

	@RequestMapping(value = { "validateRisk" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public String validateRisk(@RequestParam(value = "riskId", required = false) String riskId,@RequestParam(value = "sclCode", required = false) Long sclCode) throws BadRequestException {
		policyService.validateRiskIdFormat(sclCode,riskId);
		return "Y";
	}

	@RequestMapping(value = { "getNewIntParties/{riskId}" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET })
	public ResponseEntity<List<InterestedParties>> getNewIntParties(@PathVariable Long riskId) throws BadRequestException {
		List<InterestedParties> interestedParties = policyService.getNewInterestedParties(riskId);
		return new ResponseEntity<List<InterestedParties>>(interestedParties, HttpStatus.OK);
	}



	@RequestMapping(value = { "deleteIntParties/{partId}" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteIntParties(@PathVariable Long partId) throws BadRequestException {
		policyService.deleteRiskIntParty(partId);
	}

	@RequestMapping(value = { "importRisks" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseStatus(HttpStatus.CREATED)
	public void importRisks(RiskUploadForm uploadForm, HttpServletRequest request) throws BadRequestException, IOException {
		Long polCode = (Long) request.getSession().getAttribute("policyCode");
		uploadForm.setPolCode(polCode);
		policyService.importExcelRiskTemplate(uploadForm);

	}


	@RequestMapping(value = { "createUwPolicy" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.POST })
	@Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
	@ResponseStatus(HttpStatus.CREATED)
	public void createUwPolicy(PolicyTrans policy,@RequestPart(value = "riskBean") RiskBean riskBean, HttpServletRequest request) throws BadRequestException {
		PolicyTrans created = null;
		Long polCode =null;
			try {
				created = policyService.createPolicy(policy);
				if("NB".equalsIgnoreCase( created.getTransType()) || "SP".equalsIgnoreCase( created.getTransType())|| "EX".equalsIgnoreCase( created.getTransType())|| "RN".equalsIgnoreCase( created.getTransType()))
				{
					premiumService.computePrem(created.getPolicyId());
				}
				else{
					premiumService.computeEndorsePremium(created.getPolicyId());
				}
			} catch (IOException e) {
				throw new BadRequestException(e.getMessage());
			}
		polCode = created.getPolicyId();
		request.getSession().setAttribute("policyCode", polCode);
	}

	@RequestMapping(value = { "policyChecks" }, method = { RequestMethod.GET })
	@ResponseBody
	public DataTablesResult<TransChecks> getPolicyChecks(@DataTable DataTablesRequest pageable, HttpServletRequest request)
			throws IllegalAccessException {
		Long policyCode = (Long) request.getSession().getAttribute("policyCode");
		if(policyCode==null) policyCode=-2000l;
		return policyService.findPolicyChecks(pageable,policyCode);
	}

	@RequestMapping(value = { "authChecks/{checkId}" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void authChecks(@PathVariable Long checkId, HttpServletRequest request) throws BadRequestException {
		Long policyCode = (Long) request.getSession().getAttribute("policyCode");
		policyService.approveException(checkId,policyCode);
	}

	@RequestMapping(value = { "getriskreqdocs" }, method = { RequestMethod.GET })
	@ResponseBody
	public List<SubClassReqdDocs> getRiskUnassignedDocs(@RequestParam(value = "riskId", required = false) Long riskId, @RequestParam(value = "docName", required = false) String docName )
			throws IllegalAccessException {
		return policyService.findUnassignedRiskDocs(riskId,docName);
	}


	@RequestMapping(value = { "createRiskDocs" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public ResponseEntity<String> createClientDocs(@RequestBody RequiredDocBean requiredDocBean) throws IllegalAccessException, IOException, BadRequestException {
		policyService.createRiskRequiredDocs(requiredDocBean);
		return new ResponseEntity<String>("OK",HttpStatus.OK);
	}


	@RequestMapping(value = { "riskImportLogs" }, method = { RequestMethod.GET })
	@ResponseBody
	public DataTablesResult<RiskImportationLog> getRiskImportLogs(@DataTable DataTablesRequest pageable, HttpServletRequest request)
			throws IllegalAccessException {
		Long policyCode = (Long) request.getSession().getAttribute("policyCode");
		if(policyCode==null) policyCode=-2000l;
		return policyService.findPolicyImportationLog(pageable,policyCode);
	}


	@RequestMapping(value = { "createBeneficiary" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseStatus(HttpStatus.CREATED)
	public void createBeneficiary(PolicyBeneficiaries beneficiaries) throws IllegalAccessException, BadRequestException {
		lifeService.definePolicyBeneficiary(beneficiaries);
	}

	@RequestMapping(value = { "deleteBeneficiary/{benCode}" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteBeneficiary(@PathVariable Long benCode) {
		lifeService.deletePolBeneficiary(benCode);
	}


	@RequestMapping(value = { "policyBeneficiary/{policyCode}" }, method = { RequestMethod.GET })
	@ResponseBody
	public DataTablesResult<PolicyBeneficiaries> getPolicyBeneficiaries(@DataTable DataTablesRequest pageable,@PathVariable Long policyCode)
			throws IllegalAccessException {
		return lifeService.findPolBeneficiaries(pageable,policyCode);
	}

	@RequestMapping(value = { "policyBenefits/{policyCode}" }, method = { RequestMethod.GET })
	@ResponseBody
	public DataTablesResult<PolicyBenefitsDistribution> getPolicyBenefits(@DataTable DataTablesRequest pageable, @PathVariable Long policyCode)
			throws IllegalAccessException {
		return lifeService.findPolBenefits(pageable,policyCode);
	}

	@RequestMapping(value = { "policyBinders/{policyCode}" }, method = { RequestMethod.GET })
	@ResponseBody
	public DataTablesResult<PolicyBinders> getPolicyBinders(@DataTable DataTablesRequest pageable, @PathVariable Long policyCode)
			throws IllegalAccessException {
		return policyService.findPolicyBinders(pageable,policyCode);
	}

	@RequestMapping(value = { "printCertificates" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void printCertificates(HttpServletRequest request) throws BadRequestException {
		Long polCode = (Long) request.getSession().getAttribute("policyCode");
		certService.batchPolicyCerts(polCode);

	}

	@RequestMapping(value = { "getInhouseEmail" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET })
	public ResponseEntity<String> getInhouseEmail() throws BadRequestException {
		String email = quotationService.getInhouseEmail();
		return new ResponseEntity<String>(email, HttpStatus.OK);
	}


	@RequestMapping(value = { "proposalConversion" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.POST })
	@Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
	public ResponseEntity<PolicyTrans> proposalConversion(@RequestBody PolicyTrans policy,
														  HttpServletRequest request) throws BadRequestException {

		policyService.convertPropToPolicy(policy.getPolicyId());
		PolicyTrans coverted = policyService.getPolicyDetails(policy.getPolicyId());
		return new ResponseEntity<PolicyTrans>(coverted, HttpStatus.OK);
	}

	@RequestMapping(value = { "debitreceipts" }, method = { RequestMethod.GET })
	@ResponseBody
	public DataTablesResult<ReceiptTransDtls> getDebitTrans(@DataTable DataTablesRequest pageable, HttpServletRequest request)
			throws IllegalAccessException {
		Long polCode = (Long) request.getSession().getAttribute("policyCode");
		return accountsService.findPolicyCreditTrans(pageable,polCode);
	}

	@RequestMapping(value = { "lapsePolicy/{polCode}" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public Long lapsePolicy(@PathVariable Long polCode) throws BadRequestException{
		 policyService.lapsePolicy(polCode);
		return -2000l;
	}

	@RequestMapping(value = { "unlapsePolicy/{polCode}" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public Long unlapsePolicy(@PathVariable Long polCode) throws BadRequestException{
		policyService.unLapsePolicy(polCode);
		return -2000l;
	}

	@RequestMapping(value = { "SavePolicyQuiz" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.POST })
	@Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
	public ResponseEntity<String> savePolicyQuiz(@RequestBody JsonNode questionnaireDTO) throws BadRequestException {

		Iterator<JsonNode> jsonNodeIterator = questionnaireDTO.get("quizandAnswers").elements();
		QuestionnaireDTO questionnaireDTO1 = new QuestionnaireDTO();
		questionnaireDTO1.setQuizPolicyCode(questionnaireDTO.get("quizPolicyCode").asLong());
		List<QuestionnaireBean> questionnaireBeanList = new ArrayList<>();
		while(jsonNodeIterator.hasNext()){
			QuestionnaireBean questionnaireBean = new QuestionnaireBean();
			JsonNode question = jsonNodeIterator.next();
			System.out.println("question "+question.get("question").asText());
			questionnaireBean.setQuestion(question.get("question").asText());
			JsonNode jsonNode = question.get("answer");
			List<String> answers = new ArrayList<>();
			if(jsonNode.isArray()){

				Iterator<JsonNode> array = jsonNode.elements();
				while(array.hasNext()){
					answers.add(array.next().asText());
				}

			}
			else{
				answers.add(jsonNode.asText());
			}
			questionnaireBean.setAnswer(answers);
			questionnaireBeanList.add(questionnaireBean);
		}
		questionnaireDTO1.setQuizandAnswers(questionnaireBeanList);


		policyService.savePolicyQuiz(questionnaireDTO1);
		System.out.println(questionnaireDTO);
		//policyService.savePolicyQuiz(questionnaireDTO);
		return new ResponseEntity<String>("Ok", HttpStatus.OK);
	}


	@RequestMapping(value = { "policyQuiz/{policyCode}" }, method = { RequestMethod.GET })
	@ResponseBody
	public DataTablesResult<PolicyQuestionnaire> getPolicyQuiz(@DataTable DataTablesRequest pageable,@PathVariable Long policyCode)
			throws IllegalAccessException {
		return lifeService.findPolQuiz(pageable,policyCode);
	}

	@RequestMapping(value = { "policyQuizList" }, method = { RequestMethod.GET })
	@ResponseBody
	public Iterable<PolicyQuestionnaire> getPolicyQuizList(HttpServletRequest request)
			throws IllegalAccessException {
		Long polCode = (Long) request.getSession().getAttribute("policyCode");
		return lifeService.findPolQuizList(polCode);
	}

	@RequestMapping(value = { "deletePolicyQuiz/{policyCode}" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletePolicyQuiz(@PathVariable Long policyCode) {policyService.deletePolicyQuiz(policyCode);
	}

	@RequestMapping(value = { "checkPolId" }, method = { RequestMethod.GET })
	@ResponseBody
	public PolicyTrans getPol(@RequestParam(value = "idNo") Long idNo) {
		return policyService.findEnquiryId(idNo);
	}
	@RequestMapping(value = { "checkPol" }, method = { RequestMethod.GET })
	@ResponseBody
	public PolicyTrans getPolicyUn(@RequestParam(value = "polNo") Long polNo) {
		return policyService.findEnquiryPol(polNo);
	}
	@RequestMapping(value = { "checkClaim" }, method = { RequestMethod.GET })
	@ResponseBody
	public ClaimBookings checkClaim(@RequestParam(value = "claim") Long claim) {
		return policyService.checkClaim(claim);
	}
	@RequestMapping(value = { "checkRiskId" }, method = { RequestMethod.GET })
	@ResponseBody
	public RiskTrans getRiskUn(@RequestParam(value = "riskId") Long riskId) {
		return policyService.findEnquiryRisk(riskId);
	}
	@RequestMapping(value = { "checkAllParam" }, method = { RequestMethod.GET })
	@ResponseBody
	public RiskTrans checkAllParam(@RequestParam(value = "polNo") String polNo,
								   @RequestParam(value = "idNo") Long idNo,
								   @RequestParam(value = "riskId") String riskId){
		return policyService.checkAllParam(polNo,idNo,riskId);
	}
	@RequestMapping(value = { "checkPolAndIdParam" }, method = { RequestMethod.GET })
	@ResponseBody
	public PolicyTrans getPolAndId(@RequestParam(value = "polNo") String polNo,
								   @RequestParam(value = "idNo") Long idNo) {
		return policyService.findEnquiryPolAndId(polNo,idNo);
	}
	@RequestMapping(value = { "checkIdAndRiskParam" }, method = { RequestMethod.GET })
	@ResponseBody
	public RiskTrans getRiskId(@RequestParam(value = "riskId") String riskId,
							   @RequestParam(value = "idNo") Long idNo) {
		return policyService.findEnquiryRiskId(riskId,idNo);
	}
	@RequestMapping(value = { "checkPolAndRiskParam" }, method = { RequestMethod.GET })
	@ResponseBody
	public RiskTrans getRiskPol(@RequestParam(value = "riskId") String riskId,
								@RequestParam(value = "polNo") String polNo) {
		return policyService.findEnquiryRiskPol(riskId,polNo);
	}

	@RequestMapping(value = { "masterEnqAll" }, method = { RequestMethod.GET })
	@ResponseBody
	public DataTablesResult<RiskTrans> getMasterPolicies(@DataTable DataTablesRequest pageable,
															@RequestParam(value = "polNo", required = false) Long polNo,
															@RequestParam(value = "idNo", required = false) Long idNo,
															@RequestParam(value = "riskId", required = false) Long riskId
															) throws IllegalAccessException {
		return policyService.findEnquiryMaster(pageable,polNo,riskId,idNo);
	}
	@RequestMapping(value = { "masterEnqPI" }, method = { RequestMethod.GET })
	@ResponseBody
	public DataTablesResult<PolicyTrans> masterEnqPI(@DataTable DataTablesRequest pageable,
														 @RequestParam(value = "polNo", required = false) Long polNo,
														 @RequestParam(value = "idNo", required = false) Long idNo
	) throws IllegalAccessException {
		return policyService.masterEnqPI(pageable,polNo,idNo);
	}
	@RequestMapping(value = { "masterEnqPR" }, method = { RequestMethod.GET })
	@ResponseBody
	public DataTablesResult<RiskTrans> masterEnqPR(@DataTable DataTablesRequest pageable,
													 @RequestParam(value = "polNo", required = false) Long polNo,
													 @RequestParam(value = "riskId", required = false) Long riskId
	) throws IllegalAccessException {
		return policyService.findEnquiryPR(pageable,polNo,riskId);
	}
	@RequestMapping(value = { "masterEnqRI" }, method = { RequestMethod.GET })
	@ResponseBody
	public DataTablesResult<RiskTrans> masterEnqRI(@DataTable DataTablesRequest pageable,
												   @RequestParam(value = "idNo", required = false) Long idNo,
												   @RequestParam(value = "riskId", required = false) Long riskId
	) throws IllegalAccessException {
		return policyService.findEnquiryRI(pageable,idNo,riskId);
	}
    @RequestMapping(value = { "masterEnqPol" }, method = { RequestMethod.GET })
    @ResponseBody
    public DataTablesResult<PolicyTrans> masterEnqPol(@DataTable DataTablesRequest pageable,
                                                   @RequestParam(value = "polNo", required = false) Long polNo,
													  @RequestParam(required=false) String group)
     throws IllegalAccessException {
        return policyService.masterEnqPol(pageable,polNo);
    }
	@RequestMapping(value = { "masterEnqIdNo" }, method = { RequestMethod.GET })
	@ResponseBody
	public DataTablesResult<PolicyTrans> masterEnqIdNo(@DataTable DataTablesRequest pageable,
													   	   @RequestParam(value = "idNo", required = false) Long idNo
	) throws IllegalAccessException {
		return policyService.masterEnqIdNo(pageable,idNo);
	}
	@RequestMapping(value = { "searchPC" }, method = { RequestMethod.GET })
	@ResponseBody
	public DataTablesResult<ClientDef> masterIdNo(@DataTable DataTablesRequest pageable,
													   @RequestParam(value = "idNo", required = false) Long idNo
	) throws IllegalAccessException {
		return policyService.masterIdNo(pageable,idNo);
	}
	@RequestMapping(value = { "searchPolRisk" }, method = { RequestMethod.GET })
	@ResponseBody
	public DataTablesResult<RiskTrans> masterEnqRisk(@DataTable DataTablesRequest pageable,
													   @RequestParam(value = "policyId", required = false) Long policyId
	) throws IllegalAccessException {
		return policyService.masterEnqRisk(pageable,policyId);
	}
    @RequestMapping(value = { "masterEnqRisk" }, method = { RequestMethod.GET })
    @ResponseBody
    public DataTablesResult<RiskTrans> masterEnqUniqueRisk(@DataTable DataTablesRequest pageable,
                                                     @RequestParam(value = "riskId", required = false) Long riskId
    ) throws IllegalAccessException {
        return policyService.masterEnqUniqueRisk(pageable,riskId);
    }
	@RequestMapping(value = { "masterEnqUniqueId" }, method = { RequestMethod.GET })
	@ResponseBody
	public DataTablesResult<RiskTrans> masterEnqUniqueId(@DataTable DataTablesRequest pageable,
													 @RequestParam(value = "riskId", required = false) Long riskId
	) throws IllegalAccessException {
		return policyService.masterEnqUniqueId(pageable,riskId);
	}
	@RequestMapping(value = { "masterEnqUniqueClaim" }, method = { RequestMethod.GET })
	@ResponseBody
	public DataTablesResult<ClaimPerils> masterEnqUniqueClaim(@DataTable DataTablesRequest pageable,
															  @RequestParam(value = "riskId", required = false) Long riskId
	) throws IllegalAccessException {
		return policyService.masterEnqUniqueClaim(pageable,riskId);
	}
	@RequestMapping(value = { "getReceipts" }, method = { RequestMethod.GET })
	@ResponseBody
	public DataTablesResult<ReceiptTrans> masterReceipts(@DataTable DataTablesRequest pageable,
															   @RequestParam(value = "idNo", required = false) Long idNo,
														 @RequestParam(value = "", required = false) Long polNo
	) throws IllegalAccessException {
		return policyService.masterReceipts(pageable,idNo);
	}
	@RequestMapping(value = { "getReceiptsDets/{receiptId}" }, method = { RequestMethod.GET })
	@ResponseBody
	public DataTablesResult<ReceiptTransDtls> getReceiptsDets(@DataTable DataTablesRequest pageable,
														 @PathVariable Long receiptId
	) throws IllegalAccessException {
		return policyService.getReceiptsDets(pageable,receiptId);
	}
	@RequestMapping(value = "/viewClients", method = RequestMethod.POST)
	public String editRentalForm(@Valid @ModelAttribute ModelHelperForm helperForm, Model model) {
		model.addAttribute("tenId", helperForm.getId());
		model.addAttribute("clntId",helperForm.getId());
		return "clientsform";
	}
	@RequestMapping(value = { "tenants/{tenId}" }, method = { RequestMethod.GET })
	@ResponseBody
	public ClientDTO getAccountDetails(@PathVariable Long tenId) {
		ClientDTO tenant =  setupsService.getClientDetails(tenId);
		return tenant;
	}

	@RequestMapping(value = "/viewReceipt", method = RequestMethod.POST)
	public String editReceiptForm(@Valid @ModelAttribute ModelHelperForm helperForm, Model model,HttpServletRequest request) {
		model.addAttribute("receiptId",helperForm.getId());
		return "receiptsform";
	}
	@RequestMapping(value = { "getRcpts/{receiptCode}" }, method = { RequestMethod.GET })
	@ResponseBody
	public ReceiptTrans getAccountReceipts(@PathVariable Long receiptCode) {
		ReceiptTrans receipts =  setupsService.getReceipts(receiptCode);
		return receipts;
	}

	@RequestMapping(value = { "mileageDetails" }, method = { RequestMethod.GET })
	@ResponseBody
	public MileageDTO getMileageDetails(@RequestParam("riskId") String riskId) {
		final String trimmedRisk = riskId.replaceAll(" ","");
		return policyService.findMileageDetails(riskId);
	}

	@RequestMapping(value = "/policydocument", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getclmreqdocument(HttpServletRequest request ) throws BadRequestException {
		Long polCode = (Long) request.getSession().getAttribute("policyCode");
		System.out.println("found.."+polCode);
		final PolicyTrans policyTrans = policyService.getPolicyDetails(polCode);
		System.out.println("Policy found...."+policyTrans.getCurrentStatus());
		byte[] content = policyService.getPolicyDocument(policyTrans.getProduct().getProCode());
		System.out.println("Content Length..."+content.length);
		if (content.length>0) {
			String contentType = policyService.getPolicyDocumentType(policyTrans.getProduct().getProCode());
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.parseMediaType(contentType));
			headers.setContentLength(content.length);
			return new ResponseEntity<byte[]>(content, headers, HttpStatus.OK);
		} else {
			return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
		}
	}


}