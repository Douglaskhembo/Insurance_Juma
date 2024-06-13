package com.brokersystems.brokerapp.claims.controllers;

import com.brokersystems.brokerapp.claims.dtos.*;
import com.brokersystems.brokerapp.claims.exception.ClaimException;
import com.brokersystems.brokerapp.claims.model.*;
import com.brokersystems.brokerapp.claims.service.ClaimService;
import com.brokersystems.brokerapp.dms.UploadService;
import com.brokersystems.brokerapp.mail.model.MailMessageBean;
import com.brokersystems.brokerapp.mail.model.MailTemplates;
import com.brokersystems.brokerapp.mail.service.MailTemplateService;
import com.brokersystems.brokerapp.mail.service.Mailer;
import com.brokersystems.brokerapp.quotes.services.QuotationService;
import com.brokersystems.brokerapp.server.datatables.DataTable;
import com.brokersystems.brokerapp.server.datatables.DataTablesRequest;
import com.brokersystems.brokerapp.server.datatables.DataTablesResult;
import com.brokersystems.brokerapp.server.exception.BadRequestException;
import com.brokersystems.brokerapp.server.utils.AuditTrailLogger;
import com.brokersystems.brokerapp.setup.dto.OrganizationDTO;
import com.brokersystems.brokerapp.setup.dto.SubClassReqdDocsDTO;
import com.brokersystems.brokerapp.setup.model.*;
import com.brokersystems.brokerapp.setup.service.OrganizationService;
import com.brokersystems.brokerapp.uw.repository.SectionTransRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by peter on 3/5/2017.
 */
@Controller
@RequestMapping({ "/protected/claims" })
public class ClaimController {

    @Autowired
    private ClaimService claimService;

    @Autowired
    private OrganizationService orgService;

    @Autowired
    private DataSource datasource;

    @Autowired
    private UploadService uploadService;

    @Autowired
    private AuditTrailLogger auditTrailLogger;

    @Autowired
    private Mailer mailer;

    @Autowired
    private MailTemplateService templateService;

    @Autowired
    private QuotationService quotationService;

    @Autowired
    private SectionTransRepo sectionTransRepo;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }

    @ModelAttribute
    public ClaimForm getClaimForm(){
        return new ClaimForm();
    }

    @RequestMapping(value = "newclaim",method={RequestMethod.GET})
    public String newClaim(Model model, HttpServletRequest request)
    {
        auditTrailLogger.log("Accessed New Claim Screen ",request);
        return "newclaim";
    }

    @RequestMapping(value = "newclaim2",method={RequestMethod.GET})
    public String newclaim2(Model model, HttpServletRequest request)
    {
        auditTrailLogger.log("Accessed New Claim Screen ",request);
        return "newclaim2";
    }

    @RequestMapping(value = "enquireclaims",method={RequestMethod.GET})
    public String enquireClaims(Model model, HttpServletRequest request)
    {
        auditTrailLogger.log("Accessed Claim Transactions Screen ",request);
        return "clmEnquiry";
    }

    @RequestMapping(value = "claimants",method={RequestMethod.GET})
    public String newClaimants(Model model, HttpServletRequest request)
    {
        auditTrailLogger.log("Accessed Claimants Screen ",request);
        return "claimants";
    }

    @RequestMapping(value = "serviceproviders",method={RequestMethod.GET})
    public String newserviceProviders(Model model, HttpServletRequest request)
    {
        auditTrailLogger.log("Accessed Service Providers Screen ",request);
        return "serviceproviderform";
    }

    @RequestMapping(value = { "selprovidertypes" }, method = { RequestMethod.GET })
    @ResponseBody
    public Page<ServiceProviderTypesDTO> selectProviderTypes(@RequestParam(value = "term", required = false) String term, Pageable pageable) {
        return claimService.findServiceProviderTypes(term, pageable);
    }

    @RequestMapping(value = { "selproviders" }, method = { RequestMethod.GET })
    @ResponseBody
    public Page<ServiceProviderDTO> selectProviders(@RequestParam(value = "term", required = false) String term, Pageable pageable) {
        return claimService.findServiceProviders(term, pageable);
    }

    @RequestMapping(value = { "selLossRisks" }, method = { RequestMethod.GET })
    @ResponseBody
    public Page<ClaimRisksDTO> selRisks(@RequestParam(value = "term", required = false) String term, Pageable pageable, @RequestParam("lossDate")Date lossDate)
            throws IllegalAccessException {
        return claimService.findRisksToClaim(term,lossDate,pageable);
    }

    @RequestMapping(value = { "selclmActivity" }, method = { RequestMethod.GET })
    @ResponseBody
    public Page<ClmCausations> selclmActivity(@RequestParam(value = "term", required = false) String term, Pageable pageable)
            throws IllegalAccessException {
        return claimService.findClaimStatuses(term,pageable);
    }

    @RequestMapping(value = { "claimantdefs" }, method = { RequestMethod.GET })
    @ResponseBody
    public DataTablesResult<ClaimantsDTO> getAllClaimants(@DataTable DataTablesRequest pageable) throws IllegalAccessException {
        return claimService.findAllClaimants(pageable);
    }

    @RequestMapping(value = { "createClaimant" }, method = {
            RequestMethod.POST })
    @ResponseBody
    public void createClaimant(ClaimantsDef claimantsDef) throws IllegalAccessException, IOException, BadRequestException {
        claimService.defineClaimant(claimantsDef);
    }

    @RequestMapping(value = { "createPerilPayment" }, method = {
            RequestMethod.POST })
    @ResponseBody
    public void createClaimant(ClaimPerilPayments perilPayments) throws IllegalAccessException, IOException, BadRequestException {
        claimService.capturePerilPayment(perilPayments);
    }

    @RequestMapping(value = { "createClaimPeril" }, method = {
            RequestMethod.POST })
    @ResponseBody
    public void createClaimant(ClaimPerils claimPerils) throws IllegalAccessException, IOException, BadRequestException {
        claimService.captureClaimPerils(claimPerils);
    }

    @RequestMapping(value = { "sendEmail" }, method = {
            RequestMethod.POST })
    public ResponseEntity<String>  sendEmail(@RequestBody MailMessageBean messageBean, HttpServletRequest request) throws BadRequestException {
        System.out.println(messageBean);
        Long clmId = (Long) request.getSession().getAttribute("claimId");
        mailer.sendEmailAttachments(messageBean,clmId,"C",request);
        return new ResponseEntity<String>("OK", HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value ="mailtemplate" )
    public String getMailTemplate(HttpServletResponse response, HttpServletRequest request) throws BadRequestException{
        return templateService.getMailTemplate(MailTemplates.CLAIMS_TEMPLATE,request);
    }


    @RequestMapping(value = { "getReceiverEmail" }, method = {
            RequestMethod.GET })
    public ResponseEntity<String> getReceiverEmail(@RequestParam(value = "receiver", required = false) String receiver, HttpServletRequest request) throws BadRequestException {
        Long clmId = (Long) request.getSession().getAttribute("claimId");
        String email = mailer.getEmailReceivers(clmId,"C",receiver);
        return new ResponseEntity<String>(email, HttpStatus.OK);
    }



    @RequestMapping(value = { "createClaimantPeril" }, method = {
            RequestMethod.POST })
    @ResponseBody
    public void createClaimantPeril(PerilBean perilBean,HttpServletRequest request) throws IllegalAccessException, IOException, BadRequestException {
        Long clmId = (Long) request.getSession().getAttribute("claimId");
        perilBean.setClaimId(clmId);
        claimService.createClaimantPeril(perilBean);
    }



    @RequestMapping(value = { "deleteClaimant/{claimantId}" }, method = {
            RequestMethod.GET })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteClaimant(@PathVariable Long claimantId) {
        claimService.deleteClaimant(claimantId);
    }

    @RequestMapping(value = { "deleteClaimantPeril/{clmPerilId}" }, method = {
            RequestMethod.GET })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteClaimantPeril(@PathVariable Long clmPerilId) {
        claimService.deleteClaimantPeril(clmPerilId);
    }

    @RequestMapping(value = { "deletePerilPayment/{clmPymntId}" }, method = {
            RequestMethod.GET })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePerilPayment(@PathVariable Long clmPymntId) {
        claimService.deletePerilPayment(clmPymntId);
    }



    @RequestMapping(value = { "deleteClaimClaimant/{claimantId}" }, method = {
            RequestMethod.GET })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteClaimClaimant(@PathVariable Long claimantId) {
        claimService.deleteClaimClaimant(claimantId);
    }



    @RequestMapping(value = { "selOccupations" }, method = { RequestMethod.GET })
    @ResponseBody
    public Page<Occupation> selOccupations(@RequestParam(value = "term", required = false) String term, Pageable pageable)
            throws IllegalAccessException {
        return claimService.findOccupations(term,pageable);
    }


    @RequestMapping(value = "/claimtrans", method = RequestMethod.POST)
    public String editPolicyForm(@Valid @ModelAttribute ModelHelperForm helperForm, Model model, HttpServletRequest request) {
        model.addAttribute("clmId", helperForm.getId());
        request.getSession().setAttribute("claimId", helperForm.getId());
        return "claimdetails";
    }

    @RequestMapping(value = { "getInhouseEmail" }, method = {
            RequestMethod.GET })
    public ResponseEntity<String> getInhouseEmail() throws BadRequestException {
        String email = quotationService.getInhouseEmail();
        return new ResponseEntity<String>(email, HttpStatus.OK);
    }

    @RequestMapping(value = "newclaim",method = RequestMethod.POST)
    public ModelAndView createClaim(@ModelAttribute("claimForm")ClaimForm claimForm, BindingResult result, RedirectAttributes redirectAttrs,HttpServletRequest request) throws BadRequestException {
        Long claimId = null;
        try{
          claimId = claimService.createClaim(claimForm);
        }
        catch (ClaimException ex){
            redirectAttrs.addFlashAttribute("error", ex.getMessage());
            redirectAttrs.addFlashAttribute("claimForm", claimForm);
            return new ModelAndView("redirect:/protected/claims/newclaim2");
        }
        request.getSession().setAttribute("claimId", claimId);
        return new ModelAndView("claimdetails","clmId",claimId);
    }


    @ExceptionHandler(ClaimException.class)
    public ModelAndView getSuperheroesUnavailable(ClaimException ex) {
        ModelAndView mv = new ModelAndView("newclaim", "error", ex.getMessage());
        mv.addObject("claimForm", new ClaimForm());
        return mv;
    }

    @RequestMapping(value = { "selClaimants" }, method = { RequestMethod.GET })
    @ResponseBody
    public Page<ClaimantsDTO> selClaimants(@RequestParam(value = "term", required = false) String term, Pageable pageable) {
        return claimService.findAllClaimants(term, pageable);
    }

    @RequestMapping(value = { "selSubclassPerils" }, method = { RequestMethod.GET })
    @ResponseBody
    public Page<ClaimPerilDTO> selSubclassPerils(@RequestParam(value = "term", required = false) String term, Pageable pageable, @RequestParam("riskId")Long riskId)
            throws IllegalAccessException {
        return claimService.findSubclassPerils(term, pageable, riskId);
    }


    @RequestMapping(value = { "getClaimDetails/{clmId}" }, method = {
            RequestMethod.GET })
    public ResponseEntity<ClaimDetailsDTO> getClmBookings(@PathVariable Long clmId) throws BadRequestException {
        ClaimDetailsDTO booking = claimService.getClaimInformation(clmId);
        return new ResponseEntity<>(booking, HttpStatus.OK);
    }

    @RequestMapping(value = { "enquireClaims" }, method = { RequestMethod.GET })
    @ResponseBody
    public DataTablesResult<ClaimEnquiryDTO> enquireClaims(@DataTable DataTablesRequest pageable,
                                                           @RequestParam(value = "policyNo", required = false) String policyNo,
                                                           @RequestParam(value = "riskId", required = false) String riskId,
                                                           @RequestParam(value = "clientCode", required = false) Long clientCode,
                                                           @RequestParam(value = "claimNo", required = false) String claimNo) throws IllegalAccessException {
        return claimService.enquireClaims(pageable,  clientCode, policyNo, riskId,claimNo);
    }

    @RequestMapping(value = { "getClmClaimants" }, method = { RequestMethod.GET })
    @ResponseBody
    public DataTablesResult<ClaimClaimantsDTO> getClaimClaimants(@DataTable DataTablesRequest pageable, HttpServletRequest request) {
        Long clmId = (Long) request.getSession().getAttribute("claimId");
        return claimService.getClaimClaimants(pageable, clmId);
    }


    @RequestMapping(value = { "getClaimPerils" }, method = { RequestMethod.GET })
    @ResponseBody
    public DataTablesResult<ClaimPerilReserveDTO> getClaimPerils(@DataTable DataTablesRequest pageable,HttpServletRequest request) {
        Long clmId = (Long) request.getSession().getAttribute("claimId");
        return claimService.getClaimPerils(pageable,clmId);
    }

    @RequestMapping(value = { "getClaimPayments/{sprId}" }, method = { RequestMethod.GET })
    @ResponseBody
    public DataTablesResult<ClaimPaymentsDTO> getClaimPayments(@DataTable DataTablesRequest pageable, @PathVariable(value = "sprId") Long sprId,
                                                               HttpServletRequest request) {
        Long clmId = (Long) request.getSession().getAttribute("claimId");
        return claimService.getClaimPayments(pageable,clmId,sprId);
    }

    @RequestMapping(value = { "getPerilPayments" }, method = { RequestMethod.GET })
    @ResponseBody
    public DataTablesResult<ClaimPerilPayments> getPerilPayments(@DataTable DataTablesRequest pageable,
                                                        @RequestParam(value = "perilId", required = false) Long perilId,HttpServletRequest request)
            throws IllegalAccessException {
        return claimService.getPerilPayments(pageable,perilId);
    }

    @RequestMapping(value = { "getClmRequiredDocs" }, method = { RequestMethod.GET })
    @ResponseBody
    public DataTablesResult<ClaimRequiredDocsDTO> getClmRequiredDocs(@DataTable DataTablesRequest pageable,HttpServletRequest request)
            throws IllegalAccessException {
        Long clmId = (Long) request.getSession().getAttribute("claimId");
        return claimService.getRequiredDocs(pageable,clmId);
    }

    @RequestMapping(value = { "getClaimActivities" }, method = { RequestMethod.GET })
    @ResponseBody
    public DataTablesResult<ClaimActivityDTO> getClaimActivities(@DataTable DataTablesRequest pageable,HttpServletRequest request)
            throws IllegalAccessException {
        Long clmId = (Long) request.getSession().getAttribute("claimId");
        return  claimService.getClaimAcitivities(pageable,clmId);
    }

    @RequestMapping(value = { "getClaimStatuses" }, method = { RequestMethod.GET })
    @ResponseBody
    public DataTablesResult<ClaimStatuses> getClaimStatuses(@DataTable DataTablesRequest pageable,HttpServletRequest request)
            throws IllegalAccessException {
        Long clmId = (Long) request.getSession().getAttribute("claimId");
        return  claimService.getClaimStatuses(pageable,clmId);
    }

    @RequestMapping(value = { "getClaimUploads" }, method = { RequestMethod.GET })
    @ResponseBody
    public DataTablesResult<ClaimUploadsDTO> getClaimUploads(@DataTable DataTablesRequest pageable,HttpServletRequest request)
            throws IllegalAccessException {
        Long clmId = (Long) request.getSession().getAttribute("claimId");
        return claimService.getClaimUploads(pageable,clmId);
    }

    @RequestMapping(value = { "getClaimTransactions" }, method = { RequestMethod.GET })
    @ResponseBody
    public DataTablesResult<ClaimsTransDto> getClaimTransactions(@DataTable DataTablesRequest pageable, HttpServletRequest request)
            throws IllegalAccessException {
        Long clmId = (Long) request.getSession().getAttribute("claimId");
        return claimService.getClaimTransactions(pageable,clmId);
    }

    @RequestMapping(value = { "uploadClaimDocument" }, method = {
            RequestMethod.POST })
    @ResponseStatus(HttpStatus.CREATED)
    public void saveOrUpdateAccount(ClaimUploads upload) throws BadRequestException {
        uploadService.uploadGeneralClaimDoc(upload);
    }


    @RequestMapping(value = { "uploadClaimReqDocument" }, method = {
            RequestMethod.POST })
    @ResponseStatus(HttpStatus.CREATED)
    public void uploadClaimReqDocument(ClaimRequiredDocs upload) throws BadRequestException {
        uploadService.uploadClaimReqDoc(upload);
    }



    @RequestMapping(value = "rpt_claim_synopsis", method = RequestMethod.GET)
    public ModelAndView renewalNoticeNonMotor(ModelMap modelMap, HttpServletRequest request, ModelAndView modelAndView)
            throws BadRequestException, IOException {
        Long clmId = (Long) request.getSession().getAttribute("claimId");
        OrganizationDTO organization = orgService.getOrganizationLogoDetails();
        InputStream in = new ByteArrayInputStream(Files.readAllBytes(Paths.get(organization.getOrgLogo())));
        BufferedImage image = ImageIO.read(in);
        modelMap.put("logo", image );
        modelMap.put("datasource", datasource);
        modelMap.put("format", "pdf");
        modelMap.put("clmid", clmId);
        modelAndView = new ModelAndView("rpt_claim_synopsis", modelMap);
        return modelAndView;
    }

    @RequestMapping(value = { "createClmActivity" }, method = {
            RequestMethod.POST })
    @ResponseBody
    public void createClmActivity(ClaimActivities activities,HttpServletRequest request) throws IllegalAccessException, IOException, BadRequestException {
        Long clmId = (Long) request.getSession().getAttribute("claimId");
        ClaimBookings booking = claimService.getOne(clmId);
        activities.setClaimBookings(booking);
        claimService.addActivity(activities);
    }

    @RequestMapping(value = { "createClmStatus" }, method = {
            RequestMethod.POST })
    @ResponseBody
    public void createClmStatus(ClaimStatuses claimStatuses,HttpServletRequest request) throws IllegalAccessException, IOException, BadRequestException {
        Long clmId = (Long) request.getSession().getAttribute("claimId");
        claimService.addClaimStatus(claimStatuses,clmId);
    }


    @RequestMapping(value = "/clmdocument/{docId}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> thumbnail(@PathVariable Long docId ) throws BadRequestException {
        byte[] content = uploadService.getGeneralClaimDoc(docId);
        if (content.length>0) {
            String contentType = uploadService.getGeneralClmContentType(docId);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            headers.setContentLength(content.length);
            return new ResponseEntity<byte[]>(content, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/clmreqdocument/{docId}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getclmreqdocument(@PathVariable Long docId ) throws BadRequestException {
        byte[] content = uploadService.getRequiredClaimDoc(docId);
        if (content.length>0) {
            String contentType = uploadService.getReqClmContentType(docId);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            headers.setContentLength(content.length);
            return new ResponseEntity<byte[]>(content, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
        }
    }


    @RequestMapping(value = { "claimBalance/{polId}" }, method = {
            RequestMethod.GET })
    public ResponseEntity<ClaimBalanceBean> getPremiumProduction(@PathVariable Long polId) throws BadRequestException {
        ClaimBalanceBean aggregateBean = claimService.getBalance(polId);
        return new ResponseEntity<ClaimBalanceBean>(aggregateBean, HttpStatus.OK);
    }

    @RequestMapping(value = { "deleteClmReqDoc/{clmRequiredId}" }, method = {
            RequestMethod.GET })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteClmReqDoc(@PathVariable Long clmRequiredId) throws BadRequestException {
        uploadService.deleteClmReqDoc(clmRequiredId);
    }


    @RequestMapping(value = { "deleteClmUploadDoc/{uploadId}" }, method = {
            RequestMethod.GET })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteClmUploadDoc(@PathVariable Long uploadId) throws BadRequestException {
        uploadService.deleteClmUploadDoc(uploadId);
    }

    @RequestMapping(value = {"getClmReqDocs"}, method = {RequestMethod.GET})
    @ResponseBody
    public List<SubClassReqdDocsDTO> getRiskUnassignedDocs(@RequestParam(value = "docName", required = false) String docName, HttpServletRequest request)
            throws IllegalAccessException {
        Long clmId = (Long) request.getSession().getAttribute("claimId");
        return uploadService.findUnassignedRiskDocs(clmId, docName);
    }

    @RequestMapping(value = {"getExpireRiskSection/{peril}/{risk}"}, method = {RequestMethod.GET})
    public ResponseEntity<Set<SectionTransBean>>  getExpireRiskSection(@PathVariable(value = "peril") Long perilId, @PathVariable(value = "risk") Long riskId)  throws BadRequestException {
        Set<SectionTransBean> sectionToExpire = claimService.getExpireSections(perilId, riskId);
         return new ResponseEntity<>(sectionToExpire, HttpStatus.OK);
    }

    @RequestMapping(value = { "createClaimReqDocs" }, method = {
            RequestMethod.POST })
    public ResponseEntity<String> createClaimReqDocs(@RequestBody RequiredDocBean requiredDocBean ,
                                                     HttpServletRequest request) throws IllegalAccessException, IOException, BadRequestException {
        Long clmId = (Long) request.getSession().getAttribute("claimId");
        claimService.createclaimsRequiredDocs(requiredDocBean,clmId);
        return new ResponseEntity<String>("OK",HttpStatus.OK);
    }

    @RequestMapping(value ={ "makeRevTransReady/{id}" }, method = {
            RequestMethod.GET })
    @ResponseBody
    public void makeReady(@PathVariable Long id,HttpServletRequest request){
        claimService.makeReady(id);
    }

    @RequestMapping(value ={ "undoRevTransReady/{id}" }, method = {
            RequestMethod.GET })
    @ResponseBody
    public void undoMakeReady(@PathVariable Long id,HttpServletRequest request){
        claimService.makeUndo(id);
    }

    @RequestMapping(value ={ "authoriseRevision/{id}" }, method = {
            RequestMethod.GET })
    @ResponseBody
    public void authoriseRevision(@PathVariable Long id,HttpServletRequest request) throws BadRequestException {
        claimService.authoriseTransaction(id);
    }

    @RequestMapping(value = { "createServProviderTypes" }, method = {
            RequestMethod.POST })
    @ResponseBody
    public void createServProviderTypes(ServiceProviderTypesDTO providerTypesDTO) throws BadRequestException {
        claimService.createServiceProviderTypes(providerTypesDTO);
    }


    @RequestMapping(value = { "createServProviders" }, method = {
            RequestMethod.POST })
    @ResponseBody
    public void createServProviders(ServiceProviderDTO serviceProviderDTO) throws BadRequestException {
        claimService.createServiceProviders(serviceProviderDTO);
    }


    @RequestMapping(value = { "getServiceProviders/{id}" }, method = { RequestMethod.GET })
    @ResponseBody
    public DataTablesResult<ServiceProviderDTO> getServiceProviders(@PathVariable Long id,@DataTable DataTablesRequest pageable) {
        return claimService.getServiceProviders(id, pageable);
    }


    @RequestMapping(value = { "deleteServiceProvider/{providerId}" }, method = {
            RequestMethod.GET })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteServiceProvider(@PathVariable Long providerId) throws BadRequestException {
        claimService.deleteServiceProvider(providerId);
    }

    @RequestMapping(value = { "deleteServiceProviderType/{providerId}" }, method = {
            RequestMethod.GET })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteServiceProviderType(@PathVariable Long providerId) throws BadRequestException {
        claimService.deleteServiceProviderType(providerId);
    }

    @RequestMapping(value = "rpt_claims_req/{polId}", method = RequestMethod.GET)
    public ModelAndView rpt_claims_req(ModelMap modelMap,@PathVariable Long polId,
                                       HttpServletRequest request, ModelAndView modelAndView)
            throws BadRequestException, IOException {
        OrganizationDTO organization = orgService.getOrganizationLogoDetails();
        InputStream in = new ByteArrayInputStream(Files.readAllBytes(Paths.get(organization.getOrgLogo())));
        BufferedImage image = ImageIO.read(in);
        modelMap.put("logo", image );
        modelMap.put("datasource", datasource);
        modelMap.put("format", "pdf");
        modelMap.put("polId", polId);
        modelAndView = new ModelAndView("rpt_claims_req", modelMap);
        return modelAndView;
    }

}
