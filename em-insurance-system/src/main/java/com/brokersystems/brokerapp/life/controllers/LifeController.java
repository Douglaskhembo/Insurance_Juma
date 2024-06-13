package com.brokersystems.brokerapp.life.controllers;

import com.brokersystems.brokerapp.dms.UploadService;
import com.brokersystems.brokerapp.dms.model.UploadBean;
import com.brokersystems.brokerapp.life.model.*;
import com.brokersystems.brokerapp.life.service.LifeService;
import com.brokersystems.brokerapp.server.datatables.DataTable;
import com.brokersystems.brokerapp.server.datatables.DataTablesRequest;
import com.brokersystems.brokerapp.server.datatables.DataTablesResult;
import com.brokersystems.brokerapp.server.exception.BadRequestException;
import com.brokersystems.brokerapp.server.utils.DateUtilities;
import com.brokersystems.brokerapp.setup.dto.OrganizationDTO;
import com.brokersystems.brokerapp.setup.model.*;
import com.brokersystems.brokerapp.setup.repository.BindersRepo;
import com.brokersystems.brokerapp.setup.repository.ClientRepository;
import com.brokersystems.brokerapp.setup.service.OrganizationService;
import com.brokersystems.brokerapp.trans.dtos.LifeReceiptsDTO;
import com.brokersystems.brokerapp.trans.model.TransChecks;
import com.brokersystems.brokerapp.trans.service.PolicyAuthorization;
import com.brokersystems.brokerapp.trans.service.ReceiptService;
import com.brokersystems.brokerapp.uw.model.*;
import com.brokersystems.brokerapp.uw.service.PolicyTransService;
import com.brokersystems.brokerapp.uw.service.PremComputeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by waititu on 24/11/2017.
 */
@Controller
@RequestMapping({ "/protected/life/policies" })
public class LifeController {

    @Autowired
    private LifeService lifeService;

    @Autowired
    private PolicyTransService policyService;

    @Autowired
    private ReceiptService receiptService;


    @Autowired
	private PolicyAuthorization authService;

    @Autowired
    private DateUtilities dateUtils;

    @Autowired
    private DataSource datasource;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PremComputeService premiumService;

    @Autowired
    private BindersRepo bindersRepo;

    @Autowired
    private OrganizationService orgService;

    @Autowired
    private UploadService uploadService;


    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
    @RequestMapping(value = "lifeuwform", method = RequestMethod.GET)
    public String lifeForm(Model model, HttpServletRequest request) {
        model.addAttribute("policyId", -2000);
        model.addAttribute("policyStatus", 0);
        return "lifeuwform";
    }



    @RequestMapping(value = "edituwpolicy", method = RequestMethod.GET)
    public String edituwpolicy(Model model,HttpServletRequest request) throws BadRequestException {
        Long polCode = (Long) request.getSession().getAttribute("policyCode");
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

    @RequestMapping(value = { "binderPolTerms/{binCode}" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
    @ResponseBody
    public List<BinderPolTerms> selectageBracket(@PathVariable long binCode)
            throws BadRequestException {
        return  lifeService.getPolTerms(binCode);
    }


    @RequestMapping(value = "rpt_risk_note", method = RequestMethod.GET)
    public ModelAndView riskNote(ModelMap modelMap, HttpServletRequest request, ModelAndView modelAndView)
            throws BadRequestException, IOException {
        Long polCode = (Long) request.getSession().getAttribute("policyCode");
        PolicyTrans policyTrans = policyService.getPolicyDetails(polCode);

        OrganizationDTO organization = orgService.getOrganizationLogoDetails();
        InputStream in = new ByteArrayInputStream(Files.readAllBytes(Paths.get(organization.getOrgLogo())));
        BufferedImage image = ImageIO.read(in);
        modelMap.put("logo", image );
        modelMap.put("datasource", datasource);
        modelMap.put("format", "pdf");
        modelMap.put("polId", polCode);
        if(policyTrans.getProduct().getRiskNote()!=null || !StringUtils.isBlank(policyTrans.getProduct().getRiskNote())){
            modelAndView = new ModelAndView(policyTrans.getProduct().getRiskNote(), modelMap);
        }else
            modelAndView = new ModelAndView("rpt_risk_note", modelMap);
        return modelAndView;
    }
    @RequestMapping(value = { "lifeBinders" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
    @ResponseBody
    public Page<BindersDef> selectLifeBinders(@RequestParam(value = "term", required = false) String term, @RequestParam("bindType") String bindType, Pageable pageable)
            throws IllegalAccessException {
        return policyService.findLifeBinder(term, pageable,bindType);
    }


    @RequestMapping(value = { "getpolicyReceipts/{policyCode}" }, method = { RequestMethod.GET })
    @ResponseBody
    public DataTablesResult<LifeReceiptsDTO> getPolicyReceiptss(@DataTable DataTablesRequest pageable, @PathVariable Long policyCode)
    {
        return receiptService.findPolicyReceipts(policyCode, pageable);
    }

    @RequestMapping(value = { "policyRisks/{policyCode}" }, method = { RequestMethod.GET })
    @ResponseBody
    public DataTablesResult<RiskTrans> getPolicyRisks(@DataTable DataTablesRequest pageable, @PathVariable Long policyCode)
            throws IllegalAccessException {
        DataTablesResult<RiskTrans> result = policyService.findRiskTransactions(pageable,policyCode,-2000l);
        System.out.println(result);
        return result;
    }

    @RequestMapping(value = { "createRisk" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.POST })
    @Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
    public ResponseEntity<String> createRiskTrans(@RequestBody RiskTrans risk,
                                                  HttpServletRequest request) throws BadRequestException {
        Long polCode = (Long) request.getSession().getAttribute("policyCode");
        risk.setPolicy(policyService.getPolicyDetails(polCode));
        policyService.createLifeRisk(risk);
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

    @RequestMapping(value = { "deleteRiskSection/{sectCode}" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.GET })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
    public void deleteBinder(@PathVariable Long sectCode,HttpServletRequest request) throws BadRequestException {
        policyService.deleteRiskSection(sectCode);
        Long polCode = (Long) request.getSession().getAttribute("policyCode");
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
            else if ("EN".equalsIgnoreCase(policy.getTransType())) {
                try {
                    premiumService.computeEndorsePremium(polCode);
                } catch (IOException e) {
                    throw new BadRequestException(e.getMessage());
                }
            }
        }
    }
    @RequestMapping(value = { "risksSections/{riskId}" }, method = { RequestMethod.GET })
    @ResponseBody
    public DataTablesResult<SectionTrans> getRiskSections(@DataTable DataTablesRequest pageable, @PathVariable Long riskId)
            throws IllegalAccessException {
        return policyService.findRiskSections(pageable,riskId);
    }

    @RequestMapping(value = { "riskDocs/{riskCode}" }, method = { RequestMethod.GET })
    @ResponseBody
    public DataTablesResult<RiskDocs> getRiskDocs(@DataTable DataTablesRequest pageable, @PathVariable Long riskCode)
            throws IllegalAccessException {
        return policyService.findRiskDocs(pageable,riskCode);
    }

    @RequestMapping(value = { "getMaturityDate" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
    @ResponseBody
    public Date getPolicyWetDate(@RequestParam(value = "wefDate", required = false) Date wef,@RequestParam(value = "polTerm", required = false) Integer polTerm){
        if (polTerm!=null) {
            return dateUtils.getMaturityDate(wef, polTerm);
        } else return null;

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

    @RequestMapping(value = { "createLifePolicy" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.POST })
    @Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
    public ResponseEntity<PolicyTrans> createLifePolicyTrans(@RequestBody PolicyTrans policy,
                                                             HttpServletRequest request) throws BadRequestException {
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

    @RequestMapping(value = { "policyChecks" }, method = { RequestMethod.GET })
    @ResponseBody
    public DataTablesResult<TransChecks> getPolicyChecks(@DataTable DataTablesRequest pageable, HttpServletRequest request)
            throws IllegalAccessException {
        Long policyCode = (Long) request.getSession().getAttribute("policyCode");
        if(policyCode==null) policyCode=-2000l;
        return policyService.findPolicyChecks(pageable,policyCode);
    }

    @RequestMapping(value = { "policyClauses" }, method = { RequestMethod.GET })
    @ResponseBody
    public DataTablesResult<PolicyClauses> getPolicyClauses(@DataTable DataTablesRequest pageable,HttpServletRequest request)
            throws IllegalAccessException {
        Long policyCode = (Long) request.getSession().getAttribute("policyCode");
        return policyService.findPolicyClauses(pageable,policyCode);
    }

    @RequestMapping(value = { "policyInstallments" }, method = { RequestMethod.GET })
    @ResponseBody
    public DataTablesResult<PolicyInstallments> getPolicyInstallments(@DataTable DataTablesRequest pageable,HttpServletRequest request)
            throws IllegalAccessException {
        Long policyCode = (Long) request.getSession().getAttribute("policyCode");
        return policyService.findPolicyInstallments(pageable,policyCode);
    }

    @RequestMapping(value = { "createRiskDocs" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public ResponseEntity<String> createClientDocs(@RequestBody RequiredDocBean requiredDocBean) throws IllegalAccessException, IOException, BadRequestException {
        policyService.createRiskRequiredDocs(requiredDocBean);
        return new ResponseEntity<String>("OK",HttpStatus.OK);
    }

    @RequestMapping(value = { "uploadRequiredDocs" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.POST })
    @ResponseStatus(HttpStatus.CREATED)
    public void uploadRequiredDocs(UploadBean uploadBean) throws BadRequestException {
        uploadService.uploadRiskDocument(uploadBean);
    }


    @RequestMapping(value = { "deleteRiskDoc/{docId}" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.GET })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRiskDoc(@PathVariable Long docId) throws BadRequestException {
        uploadService.deleteRiskDoc(docId);
    }
    @RequestMapping(value = { "getPolicyDetails" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.GET })
    public ResponseEntity<PolicyTrans> getPolicyDetails(HttpServletRequest request) throws BadRequestException {
        Long polCode = (Long) request.getSession().getAttribute("policyCode");
        PolicyTrans created = policyService.getPolicyDetails(polCode);
        return new ResponseEntity<PolicyTrans>(created, HttpStatus.OK);
    }


    @RequestMapping(value = { "createLifePolMakeReady" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.POST })
    @Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
    public ResponseEntity<PolicyTrans> createLifePolMakeReady(HttpServletRequest request) throws BadRequestException {
        Long polCode = (Long) request.getSession().getAttribute("policyCode");
        PolicyTrans created = policyService.getPolicyDetails(polCode);
        // CHECK IF QUESTIONNAIRE HAS BEEN COMPLETED

//        try {
//            policyService.questionnaireCompleted(created);
//        } catch (BadRequestException e) {
//            throw new BadRequestException(e.getMessage());
//        }
        //

//        request.getSession().setAttribute("policyCode", polCode);;
//        System.out.println("Trans Type..."+created.getTransType());
//        if("NB".equalsIgnoreCase( created.getTransType()) || "SP".equalsIgnoreCase( created.getTransType())|| "EX".equalsIgnoreCase( created.getTransType())|| "RN".equalsIgnoreCase( created.getTransType()))
//            try {
//                premiumService.computeLifePrem(polCode);
//            } catch (IOException e) {
//                throw new BadRequestException(e.getMessage());
//            }
        policyService.makeLifeReady(polCode);
        return new ResponseEntity<PolicyTrans>(created, HttpStatus.OK);
    }

    @RequestMapping(value = { "proposalConversion" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.POST })
    @Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void proposalConversion(HttpServletRequest request) throws BadRequestException {

        Long polCode = (Long) request.getSession().getAttribute("policyCode");
        policyService.convertPropToPolicy(polCode);
    }

    @RequestMapping(value = { "getPolicyRemarks" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.GET })
    public ResponseEntity<PolicyRemarks> getPolicyRemarks(HttpServletRequest request) throws BadRequestException {
        Long polCode = (Long) request.getSession().getAttribute("policyCode");
        PolicyRemarks remarks = policyService.getPolicyRemarks(polCode);
        return new ResponseEntity<PolicyRemarks>(remarks, HttpStatus.OK);
    }

    @RequestMapping(value = { "getBinderClientPremRates" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.GET })
    public ResponseEntity<Set<RiskSectionBean>> getBinderClientPremRates(
            @RequestParam(value = "detId", required = false) Long detId,@RequestParam(value = "age", required = false) Long age) throws BadRequestException {
        Set<RiskSectionBean> rates = policyService.getBinderClientPremRates(detId,age);
        return new ResponseEntity<Set<RiskSectionBean>>(rates, HttpStatus.OK);
    }

    @RequestMapping(value = { "getBinderPremRates" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.GET })
    public ResponseEntity<Set<RiskSectionBean>> getBinderPremRates(
            @RequestParam(value = "detId", required = false) Long detId) throws BadRequestException {
        Set<RiskSectionBean> rates = policyService.getBinderPremRates(detId);
        return new ResponseEntity<Set<RiskSectionBean>>(rates, HttpStatus.OK);
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


    @RequestMapping(value = { "allocationCommission/{receiptId}" }, method = { RequestMethod.GET })
    @ResponseBody
    public DataTablesResult<ReceiptAllocationCommissions> getAllocCommission(@DataTable DataTablesRequest pageable, @PathVariable Long receiptId)
            throws IllegalAccessException {
        return lifeService.findReceiptsCommissions(pageable,receiptId);
    }
    
    @RequestMapping(value = { "getriskreqdocs" }, method = { RequestMethod.GET })
	@ResponseBody
	public List<SubClassReqdDocs> getRiskUnassignedDocs(@RequestParam(value = "riskId", required = false) Long riskId, @RequestParam(value = "docName", required = false) String docName )
			throws IllegalAccessException {
		return policyService.findUnassignedRiskDocs(riskId,docName);
	}
    
    @RequestMapping(value = { "authChecks/{checkId}" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void authChecks(@PathVariable Long checkId, HttpServletRequest request) throws BadRequestException {
		Long policyCode = (Long) request.getSession().getAttribute("policyCode");
		policyService.approveException(checkId,policyCode);
	}
    
    @RequestMapping(value = { "undoMakeReady" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void undoMakePolicyReady(HttpServletRequest request) throws BadRequestException {
	 Long polCode = (Long) request.getSession().getAttribute("policyCode");
	 policyService.undoMakeReady(polCode);
		
	}

    @RequestMapping(value = { "authorizeLifePolicy" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.GET })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void authorizeLifePolicy(HttpServletRequest request,@RequestParam(value = "refundAmt", required = false) BigDecimal refundAmt) throws BadRequestException {
        Long polCode = (Long) request.getSession().getAttribute("policyCode");
        authService.authorizeLifePolicy(polCode);

    }

@RequestMapping(value = { "dispatchDocs" }, method = {
		org.springframework.web.bind.annotation.RequestMethod.GET })
@ResponseStatus(HttpStatus.NO_CONTENT)
public void dispatchDocs(HttpServletRequest request) throws BadRequestException {
	Long polCode = (Long) request.getSession().getAttribute("policyCode");
	policyService.dispatchDocuments(polCode);

}
 


    @RequestMapping(value = { "policyBenefits/{policyCode}" }, method = { RequestMethod.GET })
    @ResponseBody
    public DataTablesResult<PolicyBenefitsDistribution> getPolicyBenefits(@DataTable DataTablesRequest pageable, @PathVariable Long policyCode)
            throws IllegalAccessException {
        return lifeService.findPolBenefits(pageable,policyCode);
    }

}
