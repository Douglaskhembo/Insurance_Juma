package com.brokersystems.brokerapp.setup.controllers;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.brokersystems.brokerapp.life.model.LifeCommissionRates;
import com.brokersystems.brokerapp.life.repository.LifeCommissionRatesRepo;
import com.brokersystems.brokerapp.medical.model.*;
import com.brokersystems.brokerapp.medical.service.MedicalSetupsService;
import com.brokersystems.brokerapp.server.utils.AuditTrailLogger;
import com.brokersystems.brokerapp.server.utils.LocationUtils;
import com.brokersystems.brokerapp.server.utils.UploadDocumentForm;
import com.brokersystems.brokerapp.setup.dto.PremRatesDTO;
import com.brokersystems.brokerapp.setup.repository.AccountRepo;
import com.brokersystems.brokerapp.uw.model.PolicyTrans;
import com.brokersystems.brokerapp.setup.repository.*;
import com.brokersystems.brokerapp.setup.service.SetupAudit;
import org.apache.tiles.request.RequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import com.brokersystems.brokerapp.server.datatables.DataTable;
import com.brokersystems.brokerapp.server.datatables.DataTablesRequest;
import com.brokersystems.brokerapp.server.datatables.DataTablesResult;
import com.brokersystems.brokerapp.server.exception.BadRequestException;
import com.brokersystems.brokerapp.setup.model.*;
import com.brokersystems.brokerapp.setup.service.BinderSetupService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping({ "/protected/setups/binders" })
public class BinderController {

    @Autowired
    private BinderSetupService service;

    @Autowired
    private MedicalSetupsService medicalSetupsService;

    @Autowired
    private AuditTrailLogger auditTrailLogger;

    BindersRepo bindersRepo;

    @Autowired
    AccountRepo accountRepo;

    @Autowired
    BinderDetRepo binderDetRepo;

    @Autowired
    PremRatesRepo premRatesRepo;

    @Autowired
    LifeCommissionRatesRepo lifeCommissionRatesRepo;

    @Autowired
    CommRatesRepo commRatesRepo;

    @Autowired
    SectionRepo sectionRepo;

    @Autowired
    RequiredDocsRepo requiredDocsRepo;

    @Autowired
    SubclassReqDocRepo subclassReqDocRepo;

    @Autowired
   SubClassCoverRepo subClassCoverRepo;

    @Autowired
    BinderSectPerilsRepo binderSectPerilsRepo;

    @Autowired
    SubClassPerilsRepo subClassPerilsRepo;

    @Autowired
    SetupAudit auditClass;

    @Autowired
    LocationUtils locationUtils;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @RequestMapping(value = "bindersHome",method={org.springframework.web.bind.annotation.RequestMethod.GET})
    public String classHome(Model model, HttpServletRequest request)
    {
        auditTrailLogger.log("Accessed binders screen",request);
        return "bindershome";
    }

    @RequestMapping(value = { "selproducts" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
    @ResponseBody
    public Page<ProductsDef> selProducts(@RequestParam(value = "term", required = false) String term, Pageable pageable)
            throws IllegalAccessException {
        return service.findProductsSel(term, pageable);
    }

    @RequestMapping(value = { "selAccounts" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
    @ResponseBody
    public Page<AccountDef> selAccounts(@RequestParam(value = "term", required = false) String term, Pageable pageable)
            throws IllegalAccessException {
        return service.findInsuranceAccounts(term, pageable);
    }

    @RequestMapping(value = { "selSubclass" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
    @ResponseBody
    public Page<ProductSubclasses> selSubclass(@RequestParam(value = "term", required = false) String term, Pageable pageable,@RequestParam("proCode")Long proCode)
            throws IllegalAccessException {
        return service.findProdSubclassSel(term, pageable,proCode);
    }

    @RequestMapping(value = { "selCoverTypes" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
    @ResponseBody
    public Page<CoverTypesDef> selCoverTypes(@RequestParam(value = "term", required = false) String term, Pageable pageable,@RequestParam("subCode")Long subCode)
            throws IllegalAccessException {
        return service.findCoverTypesSel(term, pageable, subCode);
    }


    @RequestMapping(value = { "selSections" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
    @ResponseBody
    public Page<SubCoverTypeSections> selSections(@RequestParam(value = "term", required = false) String term, Pageable pageable,@RequestParam("covCode")Long covCode)
            throws IllegalAccessException {
        return service.findCoverSectionSel(term, pageable, covCode);
    }

    @RequestMapping(value = { "selCardTypes" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
    @ResponseBody
    public Page<CardTypes> selCardTypes(@RequestParam(value = "term", required = false) String term, Pageable pageable,@RequestParam("cardId")Long cardId)
            throws IllegalAccessException {
        return service.findCardTypesSel(term, pageable, cardId);
    }

    @RequestMapping(value = { "activeCurrencies" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
    @ResponseBody
    public Page<Currencies> selCurrencies(@RequestParam(value = "term", required = false) String term, Pageable pageable)
            throws IllegalAccessException {
        return service.findActiveCurrencies(term, pageable);
    }

    @RequestMapping(value = { "binders/{accCode}" }, method = { RequestMethod.GET })
    @ResponseBody
    public DataTablesResult<BindersDef> getBinders(@DataTable DataTablesRequest pageable,@PathVariable Long accCode)
            throws IllegalAccessException {
        return service.findInsuranceBinders(pageable,accCode);
    }



    @RequestMapping(value = { "createBinder" }, method = { RequestMethod.POST })
    @ResponseBody
    public void createBinder(BindersDef binder, HttpServletRequest request) throws IllegalAccessException, IOException, BadRequestException {
       String message=auditClass.logBinder(binder);
        service.createBinder(binder);
       auditTrailLogger.log(message,request);
    }
    @RequestMapping(value ={ "makeBinUndo/{id}" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.GET })
    @ResponseBody
    public void makeBinUndo(@PathVariable Long id,HttpServletRequest request){
        String message=auditClass.logUndoBinder(id);
        service.makeBinUndo(id);
        auditTrailLogger.log(message,request);
    }
    @RequestMapping(value ={ "makeBinReady/{id}" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.GET })
    @ResponseBody
    public void makeBinReady(@PathVariable Long id,HttpServletRequest request){
        String message=auditClass.logMakeReady(id);
        service.makeReady(id);
        auditTrailLogger.log(message,request);
    }
    @RequestMapping(value ={ "getDeactivate/{id}" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.GET })
    @ResponseBody
    public BindersDef makeDeactivate(@PathVariable Long id){
        return  service.getDeactivate(id);
    }
    @RequestMapping(value ={ "makeBinAuthorise/{id}" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.GET })
    @ResponseBody
    public void makeBinAuthorise(@PathVariable Long id,HttpServletRequest request){
        String message=auditClass.logAuthorise(id);
        service.makeBinAuthorise(id);
        auditTrailLogger.log(message,request);
    }
    @RequestMapping(value ={ "makeBinUnAuthorise/{id}" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.GET })
    @ResponseBody
    public void makeBinUnAuthorise(@PathVariable Long id,HttpServletRequest request){
        String message=auditClass.logUnAuthorise(id);
        service.makeBinUnAuthorise(id);
        auditTrailLogger.log(message,request);
    }
    @RequestMapping(value = { "cloneBinder" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.POST })
    @ResponseBody
    public void cloneBinder(BindersDef binder) throws IllegalAccessException, IOException, BadRequestException {
        service.cloneBinder(binder);
    }

    @RequestMapping(value = { "questionnaire/{binCode}" }, method = { RequestMethod.GET })
    @ResponseBody
    public DataTablesResult<BinderQuestionnaire> getQuestions(@DataTable DataTablesRequest pageable,@PathVariable Long binCode)
            throws IllegalAccessException {
        return service.findQuestionnaire(pageable,binCode);
    }


    @RequestMapping(value = { "questionnairechoices/{questionId}" }, method = { RequestMethod.GET })
    @ResponseBody
    public DataTablesResult<BinderQuestionnaireChoices> getQuestionsChoices(@DataTable DataTablesRequest pageable,@PathVariable Long questionId)
            throws IllegalAccessException {
        return service.findQuestionsChoices(pageable,questionId);
    }

    @RequestMapping(value = { "createQuestionnaire" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.POST })
    @ResponseStatus(HttpStatus.CREATED)
    public void createQuestions(BinderQuestionnaire questionnaire) throws IllegalAccessException, BadRequestException {
        service.defineQuestionnaire(questionnaire);
    }

    @RequestMapping(value = { "createQuestionnaireChoices" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.POST })
    @ResponseStatus(HttpStatus.CREATED)
    public void createQuestionsChoices(BinderQuestionnaireChoices questionnaireChoices) throws IllegalAccessException, BadRequestException {
        service.defineQuestionnaireChoices(questionnaireChoices);
    }

    @RequestMapping(value = { "binderdetails/{binCode}" }, method = { RequestMethod.GET })
    @ResponseBody
    public DataTablesResult<BinderDetails> getBinderDetails(@DataTable DataTablesRequest pageable,@PathVariable Long binCode)
            throws IllegalAccessException {
        return service.findBinderDetails(pageable,binCode);
    }

    @RequestMapping(value = { "selGrpProducts" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
    @ResponseBody
    public Page<ProductsDef> selGrpProducts(@RequestParam(value = "term", required = false) String term,@RequestParam("grpType") String grpType, Pageable pageable)
            throws IllegalAccessException {
        return service.findGrpProductsSel(term,grpType, pageable);
    }

    @RequestMapping(value = { "createBinderDet" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.POST })
    @ResponseBody
    public void createBinderDet(BinderDetails det,HttpServletRequest request) throws IllegalAccessException, IOException, BadRequestException {
        String message=auditClass.logBinderDetailsUpdate(det);
        service.createBinderDetails(det);
       auditTrailLogger.log(message,request);
    }

    @RequestMapping(value = { "premrates/{detCode}" }, method = { RequestMethod.GET })
    @ResponseBody
    public DataTablesResult<PremRatesDef> getPremRates(@DataTable DataTablesRequest pageable,@PathVariable Long detCode)
            throws IllegalAccessException {
        return service.findDetPremRates(pageable, detCode);
    }

    @RequestMapping(value = { "createPremRates" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.POST })
    @ResponseBody
    public void createPremRates(PremRatesDef rates,HttpServletRequest request) throws IllegalAccessException, IOException, BadRequestException {
        String message=auditClass.logPremiumRates(rates);
        service.createPremRates(rates);
        auditTrailLogger.log(message,request);
    }

    @RequestMapping(value = { "deleteBinder/{binCode}" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.GET })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBinder(@PathVariable Long binCode,HttpServletRequest request) {
        String message=auditClass.logDeleteBinder(binCode);
        service.deleteBinder(binCode);
        auditTrailLogger.log(message,request);
    }



    @RequestMapping(value = { "deleteQuestion/{quizCode}" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.GET })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteQuestion(@PathVariable Long quizCode) throws BadRequestException {
        service.deleteQuestion(quizCode);
    }

    @RequestMapping(value = { "deleteChoice/{choiceCode}" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.GET })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteQuestionChoice(@PathVariable Long choiceCode) throws BadRequestException {
        service.deleteQuestionChoice(choiceCode);
    }


    @RequestMapping(value = { "bindSubclassCovts" }, method = { RequestMethod.GET })
    @ResponseBody
    public List<Object[]> getBinderSubclassCov(@RequestParam(value = "prodCode", required = false) Long prodCode,@RequestParam(value = "bindCode", required = false) Long bindCode )
            throws IllegalAccessException {
        return service.findUnassignedSubCoverTypes(prodCode, bindCode);
    }
    @RequestMapping(value = { "createBinderDetails" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public ResponseEntity<String> createCoverTypeSections(@RequestBody BinderDetailsBean binderBean,HttpServletRequest request) throws IllegalAccessException, IOException, BadRequestException {

        service.createBinderDetails(binderBean);
        auditClass.logBinderDetailsCreate(binderBean,request);
        return new ResponseEntity<String>("OK",HttpStatus.OK);
    }

    @RequestMapping(value = { "deleteBinderDetails/{detId}" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.GET })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBinderDetails(@PathVariable Long detId,HttpServletRequest request) {
        String message=auditClass.logDelBinderDetails(detId);
        service.deleteBinderDetails(detId);
        auditTrailLogger.log(message,request);
    }

    @RequestMapping(value = { "deletePremRates/{detId}" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.GET })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePremRates(@PathVariable Long detId,HttpServletRequest request) {
       String message=auditClass.logDelPremRates(detId);
        service.deletePremRates(detId);
        auditTrailLogger.log(message,request);
    }

    @RequestMapping(value = { "binderClauses/{detCode}" }, method = { RequestMethod.GET })
    @ResponseBody
    public DataTablesResult<BinderClauses> getBinderClauses(@DataTable DataTablesRequest pageable,@PathVariable Long detCode)
            throws IllegalAccessException {
        return service.findBinderClauses(pageable, detCode);
    }

    @RequestMapping(value = { "getUnassignedClauses" }, method = { RequestMethod.GET })
    @ResponseBody
    public List<SubclassClauses> getUnassignedClauses(@RequestParam(value = "detId", required = false) Long detId,@RequestParam(value = "subCode", required = false) Long subCode,@RequestParam(value = "search", required = false) String search )
            throws IllegalAccessException {
        return service.findUnassignedSubClauses(detId, subCode, search);
    }

    @RequestMapping(value = { "createBinderClauses" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public ResponseEntity<String> createBinderClauses(@RequestBody SubclassClauseBean subclassClause) throws IllegalAccessException, IOException, BadRequestException {
        service.createBinderClauses(subclassClause);
        return new ResponseEntity<String>("OK",HttpStatus.OK);
    }

    @RequestMapping(value = { "deleteBinderClause/{claCode}" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.GET })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBinderClause(@PathVariable Long claCode) {
        service.deleteBinderClause(claCode);
    }

    @RequestMapping(value = { "commRates/{detCode}" }, method = { RequestMethod.GET })
    @ResponseBody
    public DataTablesResult<CommissionRates> getCommRates(@DataTable DataTablesRequest pageable,@PathVariable Long detCode)
            throws IllegalAccessException {
        return service.findCommRates(pageable, detCode);
    }

    @RequestMapping(value = { "commRatesLife/{premItemCode}" }, method = { RequestMethod.GET })
    @ResponseBody
    public DataTablesResult<LifeCommissionRates> getLifeCommRates(@DataTable DataTablesRequest pageable, @PathVariable Long premItemCode)
            throws IllegalAccessException {
        return service.findLifeCommRates(pageable, premItemCode);
    }

    @RequestMapping(value = { "createCommRates" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.POST })
    @ResponseBody
    public void createCommRates(CommissionRates rates,HttpServletRequest request) throws IllegalAccessException, IOException, BadRequestException {
      String message=auditClass.logCommissionRates(rates);
        service.createCommissionRates(rates);
        auditTrailLogger.log(message,request);
    }

    @RequestMapping(value = { "createLifeCommRates" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.POST })
    @ResponseBody
    public void createLifeCommRates(LifeCommissionRates rates,HttpServletRequest request) throws IllegalAccessException, IOException, BadRequestException {
       String message=auditClass.logLifeCommissionRates(rates);
        service.createLifeCommissionRates(rates);
        auditTrailLogger.log(message,request);
    }


    @RequestMapping(value = { "deleteCommRates/{commCode}" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.GET })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommRates(@PathVariable Long commCode,HttpServletRequest request) {
        String message=auditClass.logDelCommissionRates(commCode);
        service.deleteCommRates(commCode);
        auditTrailLogger.log(message,request);
    }

    @RequestMapping(value = { "deleteLifeCommRates/{commCode}" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.GET })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLifeCommRates(@PathVariable Long commCode,HttpServletRequest request) {
        String message=auditClass.logDelLifeCommissionRates(commCode);
        service.deleteLifeCommRates(commCode);
        auditTrailLogger.log(message,request);
    }

    @RequestMapping(value = { "selCommRevenueItems" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
    @ResponseBody
    public Page<RevenueItemsDef> selCommRevenueItems(@RequestParam(value = "term", required = false) String term,@RequestParam("proCode")Long proCode, Pageable pageable)
            throws IllegalAccessException {
        return service.getCommRatesRevenueItems(term, pageable,proCode);
    }

    @RequestMapping(value = { "binderSectPerils" }, method = { RequestMethod.GET })
    @ResponseBody
    public DataTablesResult<BinderSectionPerils> getBinderSectPerils(@DataTable DataTablesRequest pageable,
                                                                     @RequestParam(value = "detCode", required = false) Long detCode,
                                                                     @RequestParam(value = "sectCode", required = false) Long sectCode) throws IllegalAccessException {
        return service.findBinderSectionPerils(pageable,detCode,sectCode);
    }

    @RequestMapping(value = { "selPerilSections" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
    @ResponseBody
    public Page<SectionsDef> selPerilSections(@RequestParam(value = "term", required = false) String term, Pageable pageable,@RequestParam("subCode")Long subCode)
            throws IllegalAccessException {
        return service.findSubclassSections(term,pageable,subCode);
    }

    @RequestMapping(value = { "binderperils" }, method = { RequestMethod.GET })
    @ResponseBody
    public List<SubclassPerils> getUnassignedPerils(@RequestParam(value = "detCode", required = false) Long detCode,
                                                    @RequestParam(value = "subCode", required = false) Long subCode,
                                                    @RequestParam(value = "sectCode", required = false) Long sectCode,
                                                    @RequestParam(value = "perilName", required = false) String perilName )
            throws IllegalAccessException {
        return service.findUnassignedPerils(detCode,subCode,sectCode,perilName);
    }

    @RequestMapping(value = { "createBinderSectPerils" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public ResponseEntity<String> createBinderSectPerils(@RequestBody BinderPerilsBean perilsBean,HttpServletRequest request) throws IllegalAccessException, IOException, BadRequestException {
            service.createBinderSectPerils(perilsBean);
            auditClass.logBinderSectPerils(perilsBean,request);
        return new ResponseEntity<String>("OK",HttpStatus.OK);
    }

    @RequestMapping(value = { "deletBinderSectPeril/{secPerilCode}" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.GET })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletBinderSectPeril(@PathVariable Long secPerilCode,HttpServletRequest request) {
        BinderSectionPerils binderSectionPerils=binderSectPerilsRepo.findOne(secPerilCode);
        String section=binderSectionPerils.getSectionsDef().getDesc();
        String binder=binderSectionPerils.getBinderDetail().getBinder().getBinName();
        String message="Deleted Peril For Section: "+section+" For Binder: "+binder;
        service.deleteBinderSectPeril(secPerilCode);
        auditTrailLogger.log(message,request);
    }


    @RequestMapping(value = { "bindRatesTable/{binCode}" }, method = { RequestMethod.GET })
    @ResponseBody
    public DataTablesResult<BinderRatesTable> getBinderRatesTbl(@DataTable DataTablesRequest pageable, @PathVariable Long binCode)
            throws IllegalAccessException {
        return service.findBinderRates(pageable,binCode);
    }

    @RequestMapping(value = { "createRatedTbl" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.POST })
    @ResponseStatus(HttpStatus.CREATED)
    public void createRatedTbl(BinderRatesTable ratesTable) throws BadRequestException, IOException {

        if ((ratesTable.getFile() != null) && (!ratesTable.getFile().isEmpty())) {
            if (ratesTable.getFile().getSize() != 0) {
                ratesTable.setRate_table(ratesTable.getFile().getBytes());
                ratesTable.setTableType(ratesTable.getBinder().getMedicalCoverType());
            }
            service.defineBinderRatesTable(ratesTable);
        }
    }

    @RequestMapping(value = { "createPremRatedTbl" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.POST })
    @ResponseStatus(HttpStatus.CREATED)
    public void createPremRatedTbl(PremRatesTable ratesTable) throws BadRequestException, IOException {

        if ((ratesTable.getFile() != null) && (!ratesTable.getFile().isEmpty())) {
            if (ratesTable.getFile().getSize() != 0) {
                UploadDocumentForm uploadDocumentForm = new UploadDocumentForm();
                uploadDocumentForm.setFile(ratesTable.getFile());
                uploadDocumentForm.setEntityId(100L);
                uploadDocumentForm.setEntityType("rates_table");
                final String loc = locationUtils.saveFile(uploadDocumentForm);
                ratesTable.setRate_table(loc);
                if(ratesTable.getBinderDetails().getBinder().getMedicalCoverType()!=null)
                    ratesTable.setTableType(ratesTable.getBinderDetails().getBinder().getMedicalCoverType());
            }
            service.defineBinderPremRatesTable(ratesTable);
        }
    }


    @RequestMapping(value = { "bindPremRatesTable/{detCode}" }, method = { RequestMethod.GET })
    @ResponseBody
    public DataTablesResult<PremRatesDTO> getBinderPremRatesTbl(@DataTable DataTablesRequest pageable, @PathVariable Long detCode)
            throws IllegalAccessException {
        return service.findBinderPremRates(pageable,detCode);
    }

    @RequestMapping(value = { "medcovers/{detCode}" }, method = { RequestMethod.GET })
    @ResponseBody
    public DataTablesResult<MedicalCovers> getMedicalCovers(@DataTable DataTablesRequest pageable, @PathVariable Long detCode)
            throws IllegalAccessException {
        return service.findDetMedCovers(pageable, detCode);
    }

    @RequestMapping(value = { "binderCardTypes/{detCode}" }, method = { RequestMethod.GET })
    @ResponseBody
    public DataTablesResult<BinderMedicalCards> getBinderCardTypes(@DataTable DataTablesRequest pageable, @PathVariable Long detCode)
            throws IllegalAccessException {
        return service.findDetBinCardTypes(pageable, detCode);
    }

    @RequestMapping(value = { "createMedCover" }, method = {org.springframework.web.bind.annotation.RequestMethod.POST })
    @ResponseStatus(HttpStatus.CREATED)
    public void createMedCover(MedicalCovers covers) throws IllegalAccessException, IOException, BadRequestException {
        service.createMedicalCover(covers);
    }


    @RequestMapping(value = { "createBinderCardTypes" }, method = {org.springframework.web.bind.annotation.RequestMethod.POST })
    @ResponseStatus(HttpStatus.CREATED)
    public void createBinderCardTypes(BinderMedicalCards medicalCards) throws IllegalAccessException, IOException, BadRequestException {
        service.createBinderCardTypes(medicalCards);
    }

    @RequestMapping(value = { "deleteMedCover/{coverId}" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.GET })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMedCover(@PathVariable Long coverId) throws BadRequestException {
        service.deleteMedCover(coverId);
    }

    @RequestMapping(value = { "deleteBinderCard/{binCardId}" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.GET })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBinderCard(@PathVariable Long binCardId) throws BadRequestException {
        service.deleteBinderCard(binCardId);
    }

    @RequestMapping(value = { "medbinderRules/{bindCode}" }, method = { RequestMethod.GET })
    @ResponseBody
    public DataTablesResult<MedicalBinderRules> getBinderRules(@DataTable DataTablesRequest pageable, @PathVariable Long bindCode)
            throws IllegalAccessException {
        return medicalSetupsService.findMedBinderRules(pageable,bindCode);
    }

    @RequestMapping(value = { "createBinderRules" }, method = {org.springframework.web.bind.annotation.RequestMethod.POST })
    @ResponseStatus(HttpStatus.CREATED)
    public void createBinderRules(MedicalBinderRules rules) throws IllegalAccessException, IOException, BadRequestException {
        medicalSetupsService.defineBinderRules(rules);
    }

    @RequestMapping(value = { "deleteBinderRules/{ruleId}" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.GET })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBinderRules(@PathVariable Long ruleId) throws BadRequestException {
        medicalSetupsService.deleteBinderRules(ruleId);
    }

    @RequestMapping(value = { "binLoadings/{bindCode}" }, method = { RequestMethod.GET })
    @ResponseBody
    public DataTablesResult<BinderLoadings> getBinderLoadings(@DataTable DataTablesRequest pageable, @PathVariable Long bindCode)
            throws IllegalAccessException {
        return medicalSetupsService.findBinderLoadings(pageable,bindCode);
    }

    @RequestMapping(value = { "binExclusions/{bindCode}" }, method = { RequestMethod.GET })
    @ResponseBody
    public DataTablesResult<BinderExclusions> getBinderExclusions(@DataTable DataTablesRequest pageable, @PathVariable Long bindCode)
            throws IllegalAccessException {
        return medicalSetupsService.findBinderExclusions(pageable,bindCode);
    }

    @RequestMapping(value = { "unassignLoadings" }, method = { RequestMethod.GET })
    @ResponseBody
    public List<Object[]> getUnassignedLoadings(@RequestParam(value = "bindCode", required = false) Long bindCode )
            throws IllegalAccessException {
        return medicalSetupsService.getUnassignedLoadings( bindCode);
    }

    @RequestMapping(value = { "unassignExclusions" }, method = { RequestMethod.GET })
    @ResponseBody
    public List<Object[]> getUnassignedExclusions(@RequestParam(value = "bindCode", required = false) Long bindCode,@RequestParam(value = "search", required = false) String search )
            throws IllegalAccessException {
        return medicalSetupsService.getUnassignedExclusions( bindCode,search);
    }
    @RequestMapping(value = {"unassignNetworks"}, method = {RequestMethod.GET})
    @ResponseBody
    public List<Object[]> getUnassignedNetworks(@RequestParam(value = "bindCode",required = false) Long bindCode,@RequestParam(value = "search", required = false) String search )
            throws IllegalAccessException{
        return medicalSetupsService.getUnassignedNetworks(bindCode,search);
    }

    @RequestMapping(value = {"unassignClauseExclusions"},method = {RequestMethod.GET})
    @ResponseBody
    public List<Object[]> getUnassignedClauseExclusions(@RequestParam(value = "bindCode",required = false) Long bindCode,@RequestParam(value = "search", required = false) String search )
            throws IllegalAccessException{
        return medicalSetupsService.getUnassignedClauseExclusions(bindCode,search);
    }

    @RequestMapping(value = { "createBinderLoadings" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public ResponseEntity<String> createBinderLoadings(@RequestBody AilmentsBean ailmentsBean) throws IllegalAccessException, IOException, BadRequestException {
        medicalSetupsService.createLoadings(ailmentsBean);
        return new ResponseEntity<String>("OK",HttpStatus.OK);
    }

    @RequestMapping(value = { "createBinderExclusions" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public ResponseEntity<String> createBinderExclusions(@RequestBody AilmentsBean ailmentsBean) throws IllegalAccessException, IOException, BadRequestException {
        medicalSetupsService.createExclusions(ailmentsBean);
        return new ResponseEntity<String>("OK",HttpStatus.OK);
    }

    @RequestMapping(value = { "createBinderNetExclusions" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public ResponseEntity<String> createBinderNetExclusions(@RequestBody AilmentsBean ailmentsBean) throws IllegalAccessException, IOException, BadRequestException {
        medicalSetupsService.createNetworks(ailmentsBean);
        return new ResponseEntity<String>("OK",HttpStatus.OK);
    }

    @RequestMapping(value = { "createBinderClauseExclusions" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public ResponseEntity<String> createBinderClauseExclusions(@RequestBody AilmentsBean ailmentsBean) throws IllegalAccessException, IOException, BadRequestException {
        medicalSetupsService.createClauseExclusions(ailmentsBean);
        return new ResponseEntity<String>("OK",HttpStatus.OK);
    }


    @RequestMapping(value = { "binProviders/{bindCode}" }, method = { RequestMethod.GET })
    @ResponseBody
    public DataTablesResult<BinderProviders> getBinderProviders(@DataTable DataTablesRequest pageable, @PathVariable Long bindCode)
            throws IllegalAccessException {
        return medicalSetupsService.findBinderProviders(pageable,bindCode);
    }

    @RequestMapping(value={"binProviderTypes"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
    @ResponseBody
    public Page<MedServiceProviderTypes> getBinderProviderTypes(@RequestParam(value="term", required=false) String term, Pageable pageable, @RequestParam("bindCode") Long bindCode)
            throws IllegalAccessException
    {
        return medicalSetupsService.findBinderProviderTypes(bindCode,pageable);
    }

    @RequestMapping(value={"binProviderByType"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
    @ResponseBody
    public Page<ServiceProviderContracts> getBinderProviderByType(@RequestParam(value="term", required=false) String term, Pageable pageable, @RequestParam("bindCode") Long bindCode, @RequestParam("typeCode") Long typeCode)
            throws IllegalAccessException
    {
        return medicalSetupsService.findBinderProvidersByType(bindCode,typeCode,pageable);
    }

    @RequestMapping(value = { "unassignProviders" }, method = { RequestMethod.GET })
    @ResponseBody
    public List<Object[]> getUnassignedProviders(@RequestParam(value = "bindCode", required = false) Long bindCode )
            throws IllegalAccessException {
        return medicalSetupsService.getUnassignedProviders( bindCode);
    }

    @RequestMapping(value = { "createBinderProviders" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public ResponseEntity<String> createBinderProviders(@RequestBody ProviderBean providerBean) throws IllegalAccessException, IOException, BadRequestException {
        medicalSetupsService.createProviders(providerBean);
        return new ResponseEntity<String>("OK",HttpStatus.OK);
    }

    @RequestMapping(value = { "createBinderProviderContract" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.POST })
    @ResponseBody
    public void createProviderContract(ServiceProviderContracts providerContract) throws IllegalAccessException, IOException, BadRequestException {
        medicalSetupsService.defineProviderContract(providerContract);
    }
    @RequestMapping(value = { "providerbindercontracts/{bindCode}" }, method = { RequestMethod.GET })
    @ResponseBody
    public DataTablesResult<ServiceProviderContracts> providercontracts(@DataTable DataTablesRequest pageable, @PathVariable Long bindCode)
            throws IllegalAccessException {
        return medicalSetupsService.findAllProviderContracts(pageable,bindCode);
    }
    @RequestMapping(value = { "coverlimits/{medId}" }, method = { RequestMethod.GET })
    @ResponseBody
    public DataTablesResult<CoverLimits> getCoverLimits(@DataTable DataTablesRequest pageable, @PathVariable Long medId)
            throws IllegalAccessException {
        return medicalSetupsService.findCoverLimits(pageable,medId);
    }

    @RequestMapping(value = { "createCoverLimits" }, method = {org.springframework.web.bind.annotation.RequestMethod.POST })
    @ResponseStatus(HttpStatus.CREATED)
    public void createCoverLimits(CoverLimits limits) throws IllegalAccessException, IOException, BadRequestException {
        medicalSetupsService.defineCoverLimits(limits);
    }

    @RequestMapping(value = { "deleteCoverLimits/{medId}" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.GET })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCoverLimits(@PathVariable Long medId) throws BadRequestException {
        medicalSetupsService.deleteCoverLimits(medId);
    }

    @RequestMapping(value = { "deleteSubLimits/{subLimitId}" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.GET })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSubLimits(@PathVariable Long subLimitId) throws BadRequestException {
        medicalSetupsService.deleteSubLimits(subLimitId);
    }


    @RequestMapping(value = { "deleteProviderContract/{spcId}" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.GET })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProvideContract(@PathVariable Long spcId) throws BadRequestException {
        medicalSetupsService.deleteProvideContract(spcId);
    }

    @RequestMapping(value = { "deleteBinderExclusions/{beId}"}, method = {
            RequestMethod.GET
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBinderExclusions(@PathVariable Long beId) throws RequestException {
        medicalSetupsService.deleteBinderExclusions(beId);
    }

    @RequestMapping(value = { "sublimits/{limitId}" }, method = { RequestMethod.GET })
    @ResponseBody
    public DataTablesResult<SubLimits> getSubLimits(@DataTable DataTablesRequest pageable, @PathVariable Long limitId)
            throws IllegalAccessException {
        return medicalSetupsService.findCoverSubLimits(pageable,limitId);
    }


    @RequestMapping(value = { "importSubLimits" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.POST })
    @ResponseStatus(HttpStatus.CREATED)
    public void importSubLimits(SubLimitsImport subLimitsImport) throws BadRequestException, IOException {

        if ((subLimitsImport.getFile() != null) && (!subLimitsImport.getFile().isEmpty())) {
            if (subLimitsImport.getFile().getSize() != 0) {
                medicalSetupsService.importSubLimits(subLimitsImport);
            }
        }
    }

    @RequestMapping(value = { "updateBinderClauses" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.POST })
    @ResponseBody
    public void updateBinderClauses(BinderClauses binderClause) throws IllegalAccessException, IOException, BadRequestException {
        service.updateBinderClause(binderClause);
    }

    @RequestMapping(value = { "updateBinderLoadings" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.POST })
    @ResponseBody
    public void updateBinderLoadings(BinderLoadings binderLoadings) throws IllegalAccessException, IOException, BadRequestException {
        medicalSetupsService.defineBinderLoadings(binderLoadings);
    }

    @RequestMapping(value = { "updateBinderExclusion" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.POST })
    @ResponseBody
    public void updateBinderExclusion(BinderExclusions binderExclusions) throws IllegalAccessException, IOException, BadRequestException {
        medicalSetupsService.defineBinderExclusions(binderExclusions);
    }


    @RequestMapping(value = { "requiredDocs/{detCode}" }, method = { RequestMethod.GET })
    @ResponseBody
    public DataTablesResult<BinderReqrdDocs> getBinderReqrdDocs(@DataTable DataTablesRequest pageable, @PathVariable Long detCode)
            throws IllegalAccessException {
        return service.findBinderReqDocs(pageable, detCode);
    }

    @RequestMapping(value = { "subRequiredDocs" }, method = { RequestMethod.GET })
    @ResponseBody
    public List<Object[]> getSubclassReqDocs(@RequestParam(value = "subCode", required = false) Long subCode,@RequestParam(value = "detCode", required = false) Long detCode )
            throws IllegalAccessException {
        return service.findUnassignedReqDocs(subCode, detCode);
    }

    @RequestMapping(value = { "createBinderReqrdDocs" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public ResponseEntity<String> createBinderReqrdDocs(@RequestBody RequiredDocsBean docsBean,HttpServletRequest request) throws IllegalAccessException, IOException, BadRequestException {

        service.createBinderRequiredDocs(docsBean);
        auditClass.logBinderRequiredDocs(docsBean,request);
        return new ResponseEntity<String>("OK",HttpStatus.OK);


    }

    @RequestMapping(value = { "updateBinderReqrdDocs" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.POST })
    @ResponseBody
    public void updateBinderReqrdDocs(BinderReqrdDocs reqrdDocs,HttpServletRequest request) throws IllegalAccessException, IOException, BadRequestException {
      String message=auditClass.logUpdateBinderRequiredDocs(reqrdDocs);
        service.updateBinderReqrdDocs(reqrdDocs);
        auditTrailLogger.log(message,request);
    }
    @RequestMapping(value = { "deleteBinderReqrdDocs/{docId}" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.GET })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBinderReqrdDocs(@PathVariable Long docId,HttpServletRequest request) throws BadRequestException {
       String message=auditClass.logDelBinderRequiredDocs(docId);
        service.deleteBinderReqDoc(docId);
        auditTrailLogger.log(message,request);
    }

    @RequestMapping(value = { "selChecks" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
    @ResponseBody
    public Page<Checks> selChecks(@RequestParam(value = "term", required = false) String term, Pageable pageable,@RequestParam("bindCode")Long bindCode)
            throws IllegalAccessException {
        return service.findBinderChecks(term, pageable,bindCode);
    }

    @RequestMapping(value = { "uanssignedSectLimits" }, method = { RequestMethod.GET })
    @ResponseBody
    public List<Object[]> getUnassignedPremLimits(@RequestParam(value = "sectCode", required = false) Long sectCode,@RequestParam(value = "subId", required = false) Long subId,@RequestParam(value = "search", required = false) String search )
            throws IllegalAccessException {
        return service.findClausesDef(search,subId,sectCode);
    }


    @RequestMapping(value = { "sectLimits/{premId}" }, method = { RequestMethod.GET })
    @ResponseBody
    public DataTablesResult<SectionLimits> getPremLimits(@DataTable DataTablesRequest pageable, @PathVariable Long premId)
            throws IllegalAccessException {
        return service.findPremRatesSectionLimits(pageable, premId);
    }

    @RequestMapping(value = { "createSectLimits" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public ResponseEntity<String> createSectLimits(@RequestBody ClausesBean clausesBean) throws IllegalAccessException, IOException, BadRequestException {
        service.createPremLimits(clausesBean);
        return new ResponseEntity<String>("OK",HttpStatus.OK);
    }

    @RequestMapping(value = { "saveCoverSectLimits" }, method = {org.springframework.web.bind.annotation.RequestMethod.POST })
    @ResponseStatus(HttpStatus.CREATED)
    public void saveCoverSectLimits(SectionLimits limits) throws IllegalAccessException, IOException, BadRequestException {
        service.updatePremLimits(limits);
    }

    @RequestMapping(value = { "deletePremLimits/{limitId}" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.GET })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePremLimits(@PathVariable Long limitId) throws BadRequestException {
        service.deletePremLimits(limitId);
    }

    @RequestMapping(value = { "allBinderQuestions/{binCode}" }, method = { RequestMethod.GET })
    @ResponseBody
    public List<SingleQuizModel>  getAllQuestions(@PathVariable Long binCode)
            throws IllegalAccessException {
        return service.findAllQuestions(binCode);
    }

    @RequestMapping(value = { "selQuestion" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
    @ResponseBody
    public Page<BinderQuestionnaire> selQuestion(@RequestParam(value = "term", required = false) String term,@RequestParam("BinCode") Long binCode, Pageable pageable)
            throws IllegalAccessException {
        return service.findBinderQuestions(term,binCode, pageable);
    }

    @RequestMapping(value = { "selQuestionChoice" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
    @ResponseBody
    public Page<BinderQuestionnaireChoices> selQuestionChoice(@RequestParam(value = "term", required = false) String term,@RequestParam("quizCode") Long quizCode, Pageable pageable)
            throws IllegalAccessException {
        return service.findBinderQuestionChoice(term,quizCode, pageable);
    }

    @RequestMapping(value = { "allBinders" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
    @ResponseBody
    public Page<BindersDef> selectBinders(@RequestParam(value = "term", required = false) String term, Pageable pageable)
            throws IllegalAccessException {
        return service.findAllBinders(term, pageable);
    }

    @RequestMapping(value = "premiumRatesTable/{premRateId}",method={org.springframework.web.bind.annotation.RequestMethod.GET})
    @ResponseBody
    public void  premiumRatesTable(@PathVariable Long premRateId,HttpServletRequest request, HttpServletResponse response) throws BadRequestException,IOException
    {
       service.downloadRatesTable(premRateId,response);
    }

}
