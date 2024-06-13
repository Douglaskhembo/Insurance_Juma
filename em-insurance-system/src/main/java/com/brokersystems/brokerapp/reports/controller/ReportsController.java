package com.brokersystems.brokerapp.reports.controller;

import com.brokersystems.brokerapp.reports.model.ReportData;
import com.brokersystems.brokerapp.reports.model.ReportDefinition;
import com.brokersystems.brokerapp.reports.model.ReportHeaders;
import com.brokersystems.brokerapp.reports.model.ReportParameters;
import com.brokersystems.brokerapp.reports.service.ReportService;
import com.brokersystems.brokerapp.server.exception.BadRequestException;
import com.brokersystems.brokerapp.setup.dto.OrganizationDTO;
import com.brokersystems.brokerapp.setup.model.*;
import com.brokersystems.brokerapp.setup.service.BinderSetupService;
import com.brokersystems.brokerapp.setup.service.ClientService;
import com.brokersystems.brokerapp.setup.service.OrganizationService;
import com.brokersystems.brokerapp.setup.service.UserService;
import com.brokersystems.brokerapp.trans.model.SystemTrans;
import com.brokersystems.brokerapp.trans.model.SystemTransactions;
import com.brokersystems.brokerapp.users.model.ModulesDef;
import com.brokersystems.brokerapp.users.model.PermissionsDef;
import com.brokersystems.brokerapp.uw.dtos.ClientsDto;
import com.brokersystems.brokerapp.uw.model.PolicyTrans;
import com.brokersystems.brokerapp.uw.service.PolicyTransService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping({ "/protected/reports" })
public class ReportsController {

	@Autowired
	private ReportService reportService;

	@Autowired
	private OrganizationService orgService;

	@Autowired
	private DataSource datasource;

	@Autowired
	private PolicyTransService policyService;

	@Autowired
	private BinderSetupService service;

	@Autowired
	private ClientService clientService;



	@InitBinder({"reportData"})
	protected void initBinder(WebDataBinder binder)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}

	@ModelAttribute
	public ReportData getReportData()
	{
		ReportData reportData = new ReportData();
		return reportData;
	}
	
	@RequestMapping(value = "claimsrep",method={org.springframework.web.bind.annotation.RequestMethod.GET})
	  public String claimsrep(Model model)
	  {
		  model.addAttribute("reportName","Claims Reports");
		  model.addAttribute("modules", reportService.findReportsByModule("C"));
		  return "reports";
	  }
	
	
	@RequestMapping(value = "uwrep",method={org.springframework.web.bind.annotation.RequestMethod.GET})
	  public String uwrep(Model model)
	  {
		  model.addAttribute("reportName","Underwriting Reports");
		  Iterable<ReportDefinition> headers = reportService.findReportsByModule("U");
		  model.addAttribute("modules", headers);
		  return "reports";
	  }
	  
	@RequestMapping(value = "medreports",method={org.springframework.web.bind.annotation.RequestMethod.GET})
	public String medreports(Model model)
	{
		model.addAttribute("reportName","Medical Reports");
		Iterable<ReportDefinition> headers = reportService.findReportsByModule("M");
		model.addAttribute("modules", headers);
		return "reports";
	}
	
	@RequestMapping(value = "accountrep",method={org.springframework.web.bind.annotation.RequestMethod.GET})
	  public String classHome(Model model)
	  {
		  model.addAttribute("reportName","Accounts Reports");
		  model.addAttribute("modules", reportService.findReportsByModule("A"));
		  return "reports";
	  }


	@RequestMapping(value = "report/{reportName}", method = RequestMethod.GET)
	public ModelAndView endorsementReport(ModelMap modelMap, HttpServletRequest request, ModelAndView modelAndView, @PathVariable String reportName)
			throws BadRequestException, IOException {
		OrganizationDTO organization = orgService.getOrganizationLogoDetails();
		InputStream in = new ByteArrayInputStream(Files.readAllBytes(Paths.get(organization.getOrgLogo())));
		BufferedImage image = ImageIO.read(in);
		modelMap.put("logo", image );
		modelMap.put("datasource", datasource);
		modelMap.put("format", "pdf");
		modelAndView = new ModelAndView(reportName, modelMap);
		return modelAndView;
	}

	@RequestMapping(value = "printReport", method = RequestMethod.POST)
	public ModelAndView printReport(@ModelAttribute ReportData reportData, HttpServletRequest request, ModelAndView modelAndView,ModelMap modelMap)
			throws BadRequestException, IOException {
		System.out.println(reportData);
		OrganizationDTO organization = orgService.getOrganizationLogoDetails();
		InputStream in = new ByteArrayInputStream(Files.readAllBytes(Paths.get(organization.getOrgLogo())));
		BufferedImage image = ImageIO.read(in);
		modelMap.put("logo", image );
		modelMap.put("datasource", datasource);

//		boolean passwordProtected = reportService.getReportByCode(reportData.getReportCode()).getPasswordProtect()!=null
//				                    && "Y".equalsIgnoreCase(reportService.getReportByCode(reportData.getReportCode()).getPasswordProtect());
		//modelMap.put("JRPdfExporterParameter.USER_PASSWORD", "user_pwd");
		if(reportData.getParamName1()!=null && !StringUtils.isBlank(reportData.getParamName1())){
			String value=reportData.getParamValue1();
			if(StringUtils.isBlank(value)) value=null;
			try{
				Date dt = new SimpleDateFormat("dd/MM/yyyy").parse(value);
				modelMap.put(reportData.getParamName1(),dt);
			}
			catch (Exception e){
				modelMap.put(reportData.getParamName1(),value);
//				if(reportData.getParamType1()!=null && "C".equalsIgnoreCase(reportData.getParamType1())){
//					 ClientDef clientDef = clientService.getClientDetails(Long.valueOf(value));
//
//				}
			}

		}

		if(reportData.getParamName2()!=null && !StringUtils.isBlank(reportData.getParamName2())){
			String value=reportData.getParamValue2();
			if(StringUtils.isBlank(value)) value=null;
			try{
				Date dt = new SimpleDateFormat("dd/MM/yyyy").parse(value);
				modelMap.put(reportData.getParamName2(),dt);
			}
			catch (Exception e){
				modelMap.put(reportData.getParamName2(),value);
			}

		}

		if(reportData.getParamName3()!=null && !StringUtils.isBlank(reportData.getParamName3())){
			String value=reportData.getParamValue3();
			if(StringUtils.isBlank(value)) value=null;
			try{
				Date dt = new SimpleDateFormat("dd/MM/yyyy").parse(value);
				modelMap.put(reportData.getParamName3(),dt);
			}
			catch (Exception e){
				modelMap.put(reportData.getParamName3(),value);
			}

		}

		if(reportData.getParamName4()!=null && !StringUtils.isBlank(reportData.getParamName4())){
			String value=reportData.getParamValue4();
			System.out.println("Parameter Name == "+reportData.getParamName4()+" ; "+"Parameter Value == "+value);
			if(StringUtils.isBlank(value)) value=null;
			try{
				Date dt = new SimpleDateFormat("dd/MM/yyyy").parse(value);
				modelMap.put(reportData.getParamName4(),dt);
			}
			catch (Exception e){
				modelMap.put(reportData.getParamName4(),value);
			}

		}

		if(reportData.getParamName5()!=null && !StringUtils.isBlank(reportData.getParamName5())){
			String value=reportData.getParamValue5();
			if(StringUtils.isBlank(value)) value=null;
			try{
				Date dt = new SimpleDateFormat("dd/MM/yyyy").parse(value);
				modelMap.put(reportData.getParamName5(),dt);
			}
			catch (Exception e){
				modelMap.put(reportData.getParamName5(),value);
			}

		}

		if(reportData.getParamName6()!=null && !StringUtils.isBlank(reportData.getParamName6())){
			String value=reportData.getParamValue6();
			if(StringUtils.isBlank(value)) value=null;
			try{
				Date dt = new SimpleDateFormat("dd/MM/yyyy").parse(value);
				modelMap.put(reportData.getParamName6(),dt);
			}
			catch (Exception e){
				modelMap.put(reportData.getParamName6(),value);
			}

		}

		if(reportData.getParamName7()!=null && !StringUtils.isBlank(reportData.getParamName7())){
			String value=reportData.getParamValue7();
			if(StringUtils.isBlank(value)) value=null;
			try{
				Date dt = new SimpleDateFormat("dd/MM/yyyy").parse(value);
				modelMap.put(reportData.getParamName7(),dt);
			}
			catch (Exception e){
				modelMap.put(reportData.getParamName7(),value);
			}

		}

		if(reportData.getParamName8()!=null && !StringUtils.isBlank(reportData.getParamName8())){
			String value=reportData.getParamValue8();
			if(StringUtils.isBlank(value)) value=null;
			try{
				Date dt = new SimpleDateFormat("dd/MM/yyyy").parse(value);
				modelMap.put(reportData.getParamName8(),dt);
			}
			catch (Exception e){
				modelMap.put(reportData.getParamName8(),value);
			}

		}

		if(reportData.getParamName9()!=null && !StringUtils.isBlank(reportData.getParamName9())){
			String value=reportData.getParamValue9();
			if(StringUtils.isBlank(value)) value=null;
			try{
				Date dt = new SimpleDateFormat("dd/MM/yyyy").parse(value);
				modelMap.put(reportData.getParamName9(),dt);
			}
			catch (Exception e){
				modelMap.put(reportData.getParamName9(),value);
			}

		}

		if(reportData.getParamName10()!=null && !StringUtils.isBlank(reportData.getParamName10())){
			String value=reportData.getParamValue10();
			if(StringUtils.isBlank(value)) value=null;
			try{
				Date dt = new SimpleDateFormat("dd/MM/yyyy").parse(value);
				modelMap.put(reportData.getParamName10(),dt);

			}
			catch (Exception e){
				modelMap.put(reportData.getParamName10(),value);
			}

		}

		if(reportData.getParamName11()!=null && !StringUtils.isBlank(reportData.getParamName11())){
			String value=reportData.getParamValue11();
			if(StringUtils.isBlank(value)) value=null;
			try{
				Date dt = new SimpleDateFormat("dd/MM/yyyy").parse(value);
				modelMap.put(reportData.getParamName11(),dt);
			}
			catch (Exception e){
				modelMap.put(reportData.getParamName11(),value);
			}

		}

		if(reportData.getParamName12()!=null && !StringUtils.isBlank(reportData.getParamName12())){
			String value=reportData.getParamValue12();
			if(StringUtils.isBlank(value)) value=null;
			try{
				Date dt = new SimpleDateFormat("dd/MM/yyyy").parse(value);
				modelMap.put(reportData.getParamName12(),dt);
			}
			catch (Exception e){
				modelMap.put(reportData.getParamName12(),value);
			}

		}

		if(reportData.getParamName13()!=null && !StringUtils.isBlank(reportData.getParamName13())){
			String value=reportData.getParamValue13();
			if(StringUtils.isBlank(value)) value=null;
			try{
				Date dt = new SimpleDateFormat("dd/MM/yyyy").parse(value);
				modelMap.put(reportData.getParamName13(),dt);
			}
			catch (Exception e){
				modelMap.put(reportData.getParamName13(),value);
			}
		}

		if(reportData.getParamName14()!=null && !StringUtils.isBlank(reportData.getParamName14())){
			String value=reportData.getParamValue14();
			if(StringUtils.isBlank(value)) value=null;
			try{
				Date dt = new SimpleDateFormat("dd/MM/yyyy").parse(value);
				modelMap.put(reportData.getParamName14(),dt);
			}
			catch (Exception e){
				modelMap.put(reportData.getParamName14(),value);
			}
		}

		if(reportData.getParamName15()!=null && !StringUtils.isBlank(reportData.getParamName15())){
			String value=reportData.getParamValue15();
			if(StringUtils.isBlank(value)) value=null;
			try{
				Date dt = new SimpleDateFormat("dd/MM/yyyy").parse(value);
				modelMap.put(reportData.getParamName15(),dt);
			}
			catch (Exception e){
				modelMap.put(reportData.getParamName15(),value);
			}
		}

		if(reportData.getParamName16()!=null && !StringUtils.isBlank(reportData.getParamName16())){
			String value=reportData.getParamValue16();
			if(StringUtils.isBlank(value)) value=null;
			try{
				Date dt = new SimpleDateFormat("dd/MM/yyyy").parse(value);
				modelMap.put(reportData.getParamName16(),dt);
			}
			catch (Exception e){
				modelMap.put(reportData.getParamName16(),value);
			}
		}
		if(!modelMap.containsAttribute("format")){
			modelMap.addAttribute("format","pdf");
		}
         modelMap.entrySet().forEach(System.out::println);
		modelAndView = new ModelAndView(reportData.getReportCode(), modelMap);
		return modelAndView;
	}

	@RequestMapping(value = { "getReportParams" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET })
	public ResponseEntity<Iterable<ReportParameters>> getReportParams(@RequestParam(value = "rptId", required = false) Long rptId) throws BadRequestException {
		Iterable<ReportParameters> params = reportService.getParametersByReport(rptId);
		return new ResponseEntity<Iterable<ReportParameters>>(params, HttpStatus.OK);
	}

//	@RequestMapping(value = { "clients" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
//	@ResponseBody
//	public Page<ClientDef> selectClients(@RequestParam(value = "term", required = false) String term, Pageable pageable)
//			throws IllegalAccessException {
//		return policyService.findActiveClients(term, pageable);
//	}

	@RequestMapping(value = { "clients" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public Page<ClientsDto> selectClients(@RequestParam(value = "term", required = false) String term, Pageable pageable)
			throws IllegalAccessException {
		return policyService.findActiveClients(term, pageable);
	}

	@RequestMapping(value = { "accounts" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public Page<AccountDef> selAccounts(@RequestParam(value = "term", required = false) String term, Pageable pageable)
			throws IllegalAccessException {
		return service.findInsuranceAccounts(term, pageable);
	}

	@RequestMapping(value={"users"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
	@ResponseBody
	public Page<User> branchManagers(@RequestParam(value="term", required=false) String term, Pageable pageable)
			throws IllegalAccessException
	{
		return this.orgService.findUsersForSelect(term, pageable);
	}
	@RequestMapping(value={"permissionRep"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
	@ResponseBody
	public Page<PermissionsDef> selPerms(@RequestParam(value="term", required=false) String term, Pageable pageable)
			throws IllegalAccessException
	{
		return this.orgService.findPerm(term, pageable);
	}
	@RequestMapping(value = { "policies" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public Page<PolicyTrans> selectPolicies(@RequestParam(value = "term", required = false) String term, Pageable pageable)
			throws IllegalAccessException {
		return policyService.findAllPolicies(term, pageable);
	}

	@RequestMapping(value = { "uwBinders" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public Page<BindersDef> selectBinders(@RequestParam(value = "term", required = false) String term, Pageable pageable)
			throws IllegalAccessException {
		return policyService.findAllBinders(term, pageable);
	}


	@RequestMapping(value = { "remmittances" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public Page<SystemTransactions> selectRemmitances(@RequestParam(value = "term", required = false) String term, Pageable pageable)
			throws IllegalAccessException {
		return reportService.findRemittances(term, pageable);
	}

}
