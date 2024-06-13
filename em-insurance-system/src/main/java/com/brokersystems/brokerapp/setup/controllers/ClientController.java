package com.brokersystems.brokerapp.setup.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.brokersystems.brokerapp.dms.UploadService;
import com.brokersystems.brokerapp.dms.model.UploadBean;
import com.brokersystems.brokerapp.integrations.service.ClientIntegrationService;
import com.brokersystems.brokerapp.medical.model.BinderRatesTable;
import com.brokersystems.brokerapp.server.utils.AuditTrailLogger;
import com.brokersystems.brokerapp.server.utils.LocationUtils;
import com.brokersystems.brokerapp.server.utils.UploadDocumentForm;
import com.brokersystems.brokerapp.server.utils.ValidatorUtils;
import com.brokersystems.brokerapp.setup.dto.*;
import com.brokersystems.brokerapp.setup.model.*;
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
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import com.brokersystems.brokerapp.server.datatables.DataTable;
import com.brokersystems.brokerapp.server.datatables.DataTablesRequest;
import com.brokersystems.brokerapp.server.datatables.DataTablesResult;
import com.brokersystems.brokerapp.server.exception.BadRequestException;
import com.brokersystems.brokerapp.setup.service.ClientService;
import com.brokersystems.brokerapp.setup.service.ParamService;
import com.brokersystems.brokerapp.setup.service.SetupsService;
import com.mchange.util.Base64FormatException;


@Controller
@RequestMapping({ "/protected/clients/setups" })
public class ClientController {
	
	@Autowired
	private ClientService tenService;
	
	@Autowired
	private SetupsService setupsService;

	@Autowired
	private ValidatorUtils validator;
	@Autowired
	LocationUtils locationUtils;

	@Autowired
	private ClientService clientService;

	@Autowired
	private UploadService uploadService;

	@Autowired
	private ClientIntegrationService integrationService;

    @Autowired
	private AuditTrailLogger auditTrailLogger;


	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	    dateFormat.setLenient(false);
	    binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
	
	@ModelAttribute
	public ModelHelperForm createHelperForm() {
		return new ModelHelperForm();
	}
	
	
	@RequestMapping(value = "clientslist", method = RequestMethod.GET)
	public String tenantList(Model model,HttpServletRequest request) {
		String message="Accessed Clients screen";
		auditTrailLogger.log(message,request);
		return "clients";
	}

	@RequestMapping(value = "prospectlist", method = RequestMethod.GET)
	public String prospectList(Model model,HttpServletRequest request) {
		String message="Accessed Prospects screen";
		auditTrailLogger.log(message,request);
		return "prospects";
	}


	@RequestMapping(value = "clientsform", method = RequestMethod.GET)
	public String tenantForm(Model model) {
		model.addAttribute("tenId", -2000);
		return "clientsform";
	}
	
	@RequestMapping(value = { "tenants" }, method = { RequestMethod.GET })
	@ResponseBody
	public DataTablesResult<ClientDTO> getClients(@DataTable DataTablesRequest pageable, @RequestParam(value = "search", required = false) String search)
	{
		return tenService.findAllClients(pageable, search);
	}

	@RequestMapping(value = { "transactions/{tenId}" }, method = { RequestMethod.GET })
	@ResponseBody
	public DataTablesResult<ClientTransactionDTO> getClientTransactions(@DataTable DataTablesRequest pageable,
																		@PathVariable Long tenId)
	{
		return tenService.findClientTransactions(pageable, tenId);
	}



	@RequestMapping(value = "/tenantImage/{tenId}")
	public void getImage(HttpServletResponse response, @PathVariable Long tenId)
			throws IOException, ServletException {
		ClientDef tenant = tenService.getClientDetails(tenId);
		if (tenant.getTenId()!=null && tenant.getPhotoUrl()!=null ) {
				File file = new File(tenant.getPhotoUrl());
				response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
				response.getOutputStream().write(Files.readAllBytes(file.toPath()));
				response.getOutputStream().close();

		}
	}
	
	@RequestMapping(value = { "createClient" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public String createTenant(ClientDTO tenDef) throws IllegalAccessException, IOException, BadRequestException {
		
		if ((tenDef.getFile() != null) && (!tenDef.getFile().isEmpty())) {
			if (tenDef.getFile().getSize() != 0) {
				UploadDocumentForm uploadDocumentForm = new UploadDocumentForm();
				uploadDocumentForm.setFile(tenDef.getFile());
				uploadDocumentForm.setEntityId(100L);
				uploadDocumentForm.setEntityType("client_image");
				final String loc = locationUtils.saveFile(uploadDocumentForm);
				tenDef.setPhotoUrl(loc);
			} else {

				if (tenDef.getTenId() != null) {
					tenDef.setPhotoUrl(
							setupsService.getClientDetails(tenDef.getTenId()).getPhotoUrl());

				}
			}
		} else {

			if (tenDef.getTenId() != null) {
				tenDef.setPhotoUrl(setupsService.getClientDetails(tenDef.getTenId()).getPhotoUrl());

			}
		}
		return setupsService.defineClient(tenDef);
	}
	
	@RequestMapping(value = { "tenants/{tenId}" }, method = { RequestMethod.GET })
	@ResponseBody
	public ClientDTO getAccountDetails(@PathVariable String tenId) {
		try {
			Long id = Long.parseLong(tenId);
			return setupsService.getClientDetails(id);
		}
		catch (Exception ex){
		}
		return setupsService.getClientDetailsByHash(tenId);
	}

	@RequestMapping(value = "/editClients/{tenId}", method = RequestMethod.GET)
	public String editRentalForm(@PathVariable String tenId) {
		return "viewclientsform";
	}

	
	 @RequestMapping(value={"selMobilePrefix"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
	  @ResponseBody
	  public Page<MobilePrefixDef> selectSections(@RequestParam(value="term", required=false) String term, @RequestParam("couCode") Long couCode, Pageable pageable)
	    throws IllegalAccessException, BadRequestException
	  {
	    return setupsService.findSelPrefixes(term, pageable, couCode);
	  }
	 
	 @RequestMapping(value = { "createPrefix" }, method = {
				org.springframework.web.bind.annotation.RequestMethod.POST })
		@ResponseStatus(HttpStatus.CREATED)
		public void createPrefix(MobilePrefixDef prefix) throws IllegalAccessException, BadRequestException {
			setupsService.definePrefix(prefix);
		}
	 
	 @RequestMapping(value={"selClientTypes"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
	  @ResponseBody
	  public Page<ClientTypes> selClientTypes(@RequestParam(value="term", required=false) String term,Pageable pageable)
	    throws IllegalAccessException, BadRequestException
	  {
	    return setupsService.findSelClientTypes(term, pageable);
	  }
	 
	 @RequestMapping(value = { "creatClientType" }, method = {
				org.springframework.web.bind.annotation.RequestMethod.POST })
		@ResponseStatus(HttpStatus.CREATED)
		public void creatClientType(ClientTypes clntType) throws IllegalAccessException, BadRequestException {
			setupsService.defineClientType(clntType);
		}
	 
	 @RequestMapping(value={"selClientTitles"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
	  @ResponseBody
	  public Page<ClientTitle> selClientTitles(@RequestParam(value="term", required=false) String term,Pageable pageable)
	    throws IllegalAccessException, BadRequestException
	  {
	    return setupsService.findSelClientTitles(term, pageable);
	  }
	 
	 @RequestMapping(value = { "creatClientTitle" }, method = {
				org.springframework.web.bind.annotation.RequestMethod.POST })
		@ResponseStatus(HttpStatus.CREATED)
		public void creatClientTitle(ClientTitle clientTitle) throws IllegalAccessException, BadRequestException {
			setupsService.defineClientTitle(clientTitle);
		}

	@RequestMapping(value = { "prospects" }, method = { RequestMethod.GET })
	@ResponseBody
	public DataTablesResult<ProspectsDTO> getProspects(@DataTable DataTablesRequest pageable)
			throws IllegalAccessException {
		return tenService.findAllProspects(pageable);
	}

	@RequestMapping(value = { "createProspect" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseStatus(HttpStatus.CREATED)
	public void createProspect(ProspectsDTO prospectDef) throws IllegalAccessException, BadRequestException {
		tenService.defineProspect(prospectDef);
	}

	@RequestMapping(value = { "deleteProspect/{prsId}" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteClause(@PathVariable Long prsId) {
		tenService.deleteProspect(prsId);
	}

	@RequestMapping(value = { "deleteClient/{prsId}" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteClient(@PathVariable Long prsId) {
		tenService.deleteClient(prsId);
	}

	@RequestMapping(value = { "getTodaysDate" }, method = { RequestMethod.GET })
	@ResponseBody
	public Date getTodaysDate() {
		return setupsService.getTodayDate();
	}

	@RequestMapping(value={"selClientTown"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
	@ResponseBody
	public Page<Town> selClientTown(@RequestParam(value="term", required=false) String term, Pageable pageable)
			throws IllegalAccessException, BadRequestException
	{
		return setupsService.findTownForSelect(term,pageable);
	}

	@RequestMapping(value={"selClientPostalCode"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
	@ResponseBody
	public Page<PostalCodesDef> selClientPostalCode(@RequestParam(value="term", required=false) String term, @RequestParam("townCode") Long townCode, Pageable pageable)
			throws IllegalAccessException, BadRequestException
	{
		return setupsService.findTownPostalCOdes(term,pageable,townCode);
	}


	@RequestMapping(value = { "getDefaultCountry" }, method = { RequestMethod.GET })
	@ResponseBody
	public CountryDTO getDefaultCountry() {
		return setupsService.getDefaultCountry();
	}

	@RequestMapping(value = { "validateIDNo" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public String validateIDNo(@RequestParam(value = "idNo", required = false) String idNo) throws BadRequestException {
		validator.validateIdNo(idNo);
		return "Y";
	}

	@RequestMapping(value = { "validatePassport" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public String validatePassport(@RequestParam(value = "passportNo", required = false) String passportNo) throws BadRequestException {
		validator.validatePassport(passportNo);
		return "Y";
	}

	@RequestMapping(value = { "validatePin" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public String validatePin(@RequestParam(value = "pinNo", required = false) String pinNo) throws BadRequestException {
		validator.validatePinNo(pinNo);
		return "Y";
	}

	@RequestMapping(value = { "createMobProvider" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseStatus(HttpStatus.CREATED)
	public void createMobProvider(MobProviders mobProvider) throws IllegalAccessException, BadRequestException {
		setupsService.defineMobProviders(mobProvider);
	}

	@RequestMapping(value={"selMobProviders"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
	@ResponseBody
	public Page<MobProviders> selMobProviders(@RequestParam(value="term", required=false) String term, Pageable pageable)
			throws IllegalAccessException, BadRequestException
	{
		return setupsService.findMobProviders(term,pageable);
	}

	@RequestMapping(value = { "clientDocs/{clientId}" }, method = { RequestMethod.GET })
	@ResponseBody
	public DataTablesResult<ClientDocsDTO> getClientDocs(@DataTable DataTablesRequest pageable, @PathVariable Long clientId)
	{
		return clientService.findClientDOcs(pageable,clientId);
	}



	@RequestMapping(value = { "uploadClientDocs" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseStatus(HttpStatus.CREATED)
	public void uploadClientDocs(UploadBean uploadBean) throws BadRequestException {
		uploadService.uploadClientDocument(uploadBean);
	}


	@RequestMapping(value = { "clientreqdocs" }, method = { RequestMethod.GET })
	@ResponseBody
	public List<RequiredDocs> getUnassignedDocs(@RequestParam(value = "clientCode", required = false) Long clientCode, @RequestParam(value = "docName", required = false) String docName )
			throws IllegalAccessException {
		return clientService.findUnassignedClientDocs(clientCode,docName);
	}




	@RequestMapping(value = { "createClientDocs" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public ResponseEntity<String> createClientDocs(@RequestBody RequiredDocBean requiredDocBean) throws IllegalAccessException, IOException, BadRequestException {
		clientService.createClientRequiredDocs(requiredDocBean);
		return new ResponseEntity<String>("OK",HttpStatus.OK);
	}



//	@RequestMapping(value = { "integrationClients" }, method = { RequestMethod.GET })
//	@ResponseBody
//	public List<EWSBANCASSURECUSTINFOType.GEWSBANCASSURECUSTINFODetailType.MEWSBANCASSURECUSTINFODetailType> getIntegrationClients(@RequestParam(value = "custId", required = false) String custId, @RequestParam(value = "legalId", required = false) String legalId )
//			throws IllegalAccessException {
//		return integrationService.pullDetails(legalId,custId);
//	}



	@RequestMapping(value = { "authorizeClient/{clientId}" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void authorizePolicy(HttpServletRequest request,@PathVariable Long clientId) throws BadRequestException {
		clientService.authorizeClient(clientId,request);

	}

	@RequestMapping(value = "/clientDocument/{adId}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> thumbnail(@PathVariable Long adId ) throws BadRequestException {
		byte[] content = uploadService.getClientDocument(adId);
		if (content.length>0) {
			String contentType = uploadService.getClientDocumentType(adId);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.parseMediaType(contentType));
			headers.setContentLength(content.length);
			return new ResponseEntity<byte[]>(content, headers, HttpStatus.OK);
		} else {
			return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(value = "/prospectDocument/{adId}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> prospectthumbnail(@PathVariable Long adId ) throws BadRequestException {
		byte[] content = uploadService.getProspectDocument(adId);
		if (content.length>0) {
			String contentType = uploadService.getClientDocumentType(adId);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.parseMediaType(contentType));
			headers.setContentLength(content.length);
			return new ResponseEntity<byte[]>(content, headers, HttpStatus.OK);
		} else {
			return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(value = { "deleteClientDoc/{adId}" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteClientDoc(@PathVariable Long adId) throws BadRequestException {
		uploadService.deleteClntDocument(adId);
	}




}
