package com.brokersystems.brokerapp.setup.controllers;

import java.io.IOException;
import java.util.List;

import com.brokersystems.brokerapp.claims.model.ClaimRequiredDocs;
import com.brokersystems.brokerapp.server.utils.AuditTrailLogger;
import com.brokersystems.brokerapp.setup.service.SetupsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.brokersystems.brokerapp.server.datatables.DataTable;
import com.brokersystems.brokerapp.server.datatables.DataTablesRequest;
import com.brokersystems.brokerapp.server.datatables.DataTablesResult;
import com.brokersystems.brokerapp.server.exception.BadRequestException;
import com.brokersystems.brokerapp.setup.model.*;
import com.brokersystems.brokerapp.setup.service.ClassesService;

import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping({ "/protected/setups/products" })
public class ProductController {
	
	@Autowired
	private ClassesService service;

	@Autowired
	SetupsService setupsService;

	@Autowired
	private AuditTrailLogger auditTrailLogger;

	@RequestMapping(value = "productsHome",method={org.springframework.web.bind.annotation.RequestMethod.GET})
	  public String classHome(Model model, HttpServletRequest request)
	  {
		  auditTrailLogger.log("Accessed Products screen",request);
	    return "productsform";
	  }
	
	@RequestMapping(value = { "selprodgroups" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public Page<ProductGroupDef> selProdGroups(@RequestParam(value = "term", required = false) String term, Pageable pageable)
			throws IllegalAccessException {
		return service.findProductGroupforSel(term, pageable);
	}
	
	@RequestMapping(value = { "createGroup" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public void createClass(ProductGroupDef group) throws IllegalAccessException, IOException, BadRequestException {
		service.createProductGroup(group);
	}
	
	
	@RequestMapping(value = { "products/{prodCode}" }, method = { RequestMethod.GET })
	@ResponseBody
	public DataTablesResult<ProductsDef> getProducts(@DataTable DataTablesRequest pageable,@PathVariable Long prodCode)
			throws IllegalAccessException {
		return service.findAllProducts(pageable,prodCode);
	}

	@RequestMapping(value = { "createProduct" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseStatus(HttpStatus.CREATED)
	public void createProduct(ProductsDef product) throws BadRequestException {
		service.createProduct(product);
	}

	
	@RequestMapping(value = { "subclasses/{prodCode}" }, method = { RequestMethod.GET })
	@ResponseBody
	public DataTablesResult<ProductSubclasses> getProductSubclasses(@DataTable DataTablesRequest pageable,@PathVariable Long prodCode)
			throws IllegalAccessException {
		return service.findProdSubClass(pageable, prodCode);
	}
	
	@RequestMapping(value = { "deleteProduct/{prodId}" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteProduct(@PathVariable Long prodId) {
		service.deleteProduct(prodId);
	}
	
	@RequestMapping(value = { "deleteProductGroup/{prodId}" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteProductGroup(@PathVariable Long prodId) {
		service.deleteProductGroup(prodId);
	}
	
	@RequestMapping(value = { "prodsubclasses" }, method = { RequestMethod.GET })
	@ResponseBody
	public List<SubClassDef> getUnassignedSubclasses(@RequestParam(value = "prodCode", required = false) Long prodCode,@RequestParam(value = "subName", required = false) String subName )
			throws IllegalAccessException {
		return service.findUnassignedSubclasses(prodCode,subName);
	}
	
	    @RequestMapping(value = { "createProductSubclass" }, method = {
				org.springframework.web.bind.annotation.RequestMethod.POST })
		public ResponseEntity<String> createProdSubclass(@RequestBody ProductSubcBean prodSubcl) throws IllegalAccessException, IOException, BadRequestException {
		    service.createProdSubclasses(prodSubcl);
			return new ResponseEntity<String>("OK",HttpStatus.OK);
		}
	 
	 @RequestMapping(value = { "createProductSubcl" }, method = {
				org.springframework.web.bind.annotation.RequestMethod.POST })
		@ResponseBody
		public void createProduct(ProductSubclasses subclass) throws IllegalAccessException, IOException, BadRequestException {
			service.createProductClass(subclass);
		}
	 
	 @RequestMapping(value = { "deleteProductSubclass/{sclCode}" }, method = {
				org.springframework.web.bind.annotation.RequestMethod.GET })
		@ResponseStatus(HttpStatus.NO_CONTENT)
		public void deleteProductSubclass(@PathVariable Long sclCode) {
			service.deleteProdSubclass(sclCode);
		}

	@RequestMapping(value = { "createRptGroup" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseStatus(HttpStatus.CREATED)
	public void createRptGroup(ProductReportGroup productReportGroup) throws IllegalAccessException, BadRequestException {
		setupsService.createRptGroup(productReportGroup);
	}

	@RequestMapping(value = { "prdrptgroups" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public Page<ProductReportGroup> selRptGrps(@RequestParam(value = "term", required = false) String term, Pageable pageable)
			throws IllegalAccessException {
		return setupsService.selRptGrps(term, pageable);
	}

}
