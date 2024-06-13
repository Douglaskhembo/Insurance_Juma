package com.brokersystems.brokerapp.setup.service.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.brokersystems.brokerapp.server.exception.BadRequestException;
import com.brokersystems.brokerapp.setup.model.*;
import com.brokersystems.brokerapp.setup.service.ParamService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brokersystems.brokerapp.server.datatables.DataTablesRequest;
import com.brokersystems.brokerapp.server.datatables.DataTablesResult;
import com.brokersystems.brokerapp.setup.repository.ClassesRepo;
import com.brokersystems.brokerapp.setup.repository.ClausesRepo;
import com.brokersystems.brokerapp.setup.repository.CoverTypesRepo;
import com.brokersystems.brokerapp.setup.repository.ProdSubclassRepo;
import com.brokersystems.brokerapp.setup.repository.ProductGroupRepo;
import com.brokersystems.brokerapp.setup.repository.ProductsRepo;
import com.brokersystems.brokerapp.setup.repository.SectionRepo;
import com.brokersystems.brokerapp.setup.repository.SubClassCoverRepo;
import com.brokersystems.brokerapp.setup.repository.SubClassRepo;
import com.brokersystems.brokerapp.setup.repository.SubClausesRepo;
import com.brokersystems.brokerapp.setup.repository.SubCoverSectRepo;
import com.brokersystems.brokerapp.setup.repository.SubSectionRepo;
import com.brokersystems.brokerapp.setup.service.ClassesService;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;

@Service
public  class ClassesServiceImpl implements ClassesService {
	
	@Autowired
	private ClassesRepo classRepo;
	
	@Autowired
	private SubClassRepo subclassRepo;
	
	@Autowired
	private SubClassCoverRepo subCoverRepo;
	
	@Autowired
	private CoverTypesRepo coverRepo;
	
	@Autowired
	private SectionRepo sectionRepo;
	
	@Autowired
	private SubSectionRepo subSectionRepo;
	
	@Autowired
	private SubCoverSectRepo subcoverSecRepo;
	
	@Autowired
	private ProductGroupRepo groupRepo;
	
	@Autowired
	private ProductsRepo prodRepo;

	@Autowired
	private ParamService paramService;
	
	@Autowired
	private ProdSubclassRepo prodSubRepo;
	
	@Autowired
	private ClausesRepo clauseRepo;
	
	@Autowired
	private SubClausesRepo subclauseRepo;
	
	

	@Override
	@Transactional(readOnly = true)
	public DataTablesResult<ClassesDef> findAllClasses(DataTablesRequest request) throws IllegalAccessException {
		Page<ClassesDef> page = classRepo.findAll(request.searchPredicate(QClassesDef.classesDef), request);
		return new DataTablesResult<>(request, page);
	}

	@Override
	@Transactional(readOnly = false)
	public void createClass(ClassesDef classDef) {
		classRepo.save(classDef);
	}
	
	@Override
	@Modifying
	@Transactional(readOnly = false)
	public void deleteClass(Long classCode) {
		classRepo.delete(classCode);

	}

	@Override
	@Transactional(readOnly = true)
	public DataTablesResult<SubClassDef> findAllSubclass(DataTablesRequest request, Long classId)
			throws IllegalAccessException {
		BooleanExpression pred = QSubClassDef.subClassDef.classDef.clId.eq(classId);
		Page<SubClassDef> page = subclassRepo.findAll(pred.and(request.searchPredicate(QSubClassDef.subClassDef)), request);
		return new DataTablesResult<>(request, page);
	}

	@Override
	@Transactional(readOnly = false)
	public void createSubClass(SubClassDef subclassDef) {
		subclassRepo.save(subclassDef);
	}

	@Transactional(readOnly = true)
	@Override
	public Page<ClassesDef> findClassesForSelect(String term, Pageable pageable) {
		term = "%" + StringUtils.defaultString(term) + "%";
	    return this.classRepo.findByClDescLikeIgnoreCase(term, pageable);
	}

	@Override
	@Modifying
	@Transactional(readOnly = false)
	public void deleteSubclass(long subId) {
		subclassRepo.delete(subId);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<CoverTypesDef> findCoverTypesForSel(String term, Pageable pageable,Long subId) {
			return coverRepo.getUnassignedCoverTypes(subId,term, pageable);
	}

	@Override
	@Transactional(readOnly = false)
	public void createCoverType(CoverTypesDef coverType) {
		coverType.setCovShtDesc(StringUtils.trim(coverType.getCovShtDesc()));
		coverRepo.save(coverType);
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteCoverType(long coverId) {
		coverRepo.delete(coverId);
	}

	@Override
	@Transactional(readOnly = true)
	public DataTablesResult<SubclassCoverTypes> findSubclassCoverTypes(DataTablesRequest request, Long subId)
			throws IllegalAccessException {
		BooleanExpression pred = QSubclassCoverTypes.subclassCoverTypes.subclass.subId.eq(subId);
		Page<SubclassCoverTypes> page = subCoverRepo.findAll(pred.and(request.searchPredicate(QSubclassCoverTypes.subclassCoverTypes)), request);
		return new DataTablesResult<>(request, page);
	}

	@Override
	@Transactional(readOnly = false)
	public void createSubClassCoverType(SubclassCoverTypes subclassCover) throws BadRequestException {
		if(subCoverRepo.countDefault(subclassCover.getSubclass().getSubId(),subclassCover.getCoverTypes().getCovId()) > 0){
			throw new BadRequestException("Another Default Cover Types is defined for this sub class...");
		}
		subCoverRepo.save(subclassCover);
		
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteSubCoverType(long subCoverId) {
		subCoverRepo.delete(subCoverId);
	}

	@Override
	@Transactional(readOnly = true)
	public DataTablesResult<SubclassSections> findSubclassSections(DataTablesRequest request, Long subId)
			throws IllegalAccessException {
		BooleanExpression pred = QSubclassSections.subclassSections.subclass.subId.eq(subId);
		Page<SubclassSections> page = subSectionRepo.findAll(pred.and(request.searchPredicate(QSubclassSections.subclassSections)), request);
		return new DataTablesResult<>(request, page);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<SectionsDef> findSectionsForSel(String term, Pageable pageable, Long subId) {
		return sectionRepo.getUnassignedSections(subId, term, pageable);
	}

	@Override
	@Transactional(readOnly = false)
	public void createSection(SectionsDef section) {

		section.setShtDesc(StringUtils.trim(section.getShtDesc()));
		sectionRepo.save(section);
	}

	@Override
	@Transactional(readOnly = false)
	public void createSubclassSection(SubclassSections section) {
		if(section.getRefundable()!=null && "on".equalsIgnoreCase(section.getRefundable())){
			section.setRefundable("Y");
		}
		else section.setRefundable("N");
		subSectionRepo.save(section);
		
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteSection(Long id) {
		sectionRepo.delete(id);
		
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteSubSection(Long id) {
		subSectionRepo.delete(id);
	}

	@Override
	@Transactional(readOnly = true)
	public DataTablesResult<SubCoverTypeSections> findSubCoverTypesSections(DataTablesRequest request, Long scsCode)
			throws IllegalAccessException {
		BooleanExpression pred = QSubCoverTypeSections.subCoverTypeSections.subcoverType.scId.eq(scsCode);
		Page<SubCoverTypeSections> page = subcoverSecRepo.findAll(pred.and(request.searchPredicate(QSubCoverTypeSections.subCoverTypeSections)), request);
		return new DataTablesResult<>(request, page);
	}

	@Override
	@Transactional(readOnly = true)
	public List<SubclassSections> findUnassignedSections(Long scsCode,Long subId)  throws IllegalAccessException{
		return subcoverSecRepo.getUnassignedSections(scsCode,subId,"");
	}

	@Override
	@Transactional(readOnly = false)
	public void createCoverSections(CoverSectionBean section) {
		List<SubCoverTypeSections> sections = new ArrayList<>();
		for(Long sect:section.getSections()){
			SubCoverTypeSections coverSection = new SubCoverTypeSections();
			coverSection.setSubcoverType(subCoverRepo.findOne(section.getCoverCode()));
			coverSection.setSubSections(subSectionRepo.findOne(sect));
			sections.add(coverSection);
		}
		subcoverSecRepo.save(sections);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void createCoverSection(SubCoverTypeSections section){
		if("on".equalsIgnoreCase(section.getIntegration())){
			section.setIntegration("Y");
		}
		else section.setIntegration("N");
		if("on".equalsIgnoreCase(section.getSupportsEarnings())){
			section.setSupportsEarnings("Y");
		}
		else section.setSupportsEarnings("N");
		subcoverSecRepo.save(section);
	}
	
	
	@Override
	@Transactional(readOnly = false)
	public void deleteCoverSection(Long id){
		subcoverSecRepo.delete(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ProductGroupDef> findProductGroupforSel(String paramString, Pageable paramPageable) {
		Predicate pred = null;
		if (paramString == null || StringUtils.isBlank(paramString)) {
			pred = QProductGroupDef.productGroupDef.isNotNull();
		} else {
			pred = QProductGroupDef.productGroupDef.prgDesc.containsIgnoreCase(paramString);
		}
		return groupRepo.findAll(pred, paramPageable);
	}

	@Override
	@Transactional(readOnly = false)
	public void createProductGroup(ProductGroupDef group) {
		groupRepo.save(group);
		
	}

	@Override
	@Transactional(readOnly = true)
	public DataTablesResult<ProductsDef> findAllProducts(DataTablesRequest request, Long prgCode)
			throws IllegalAccessException {
		BooleanExpression pred = QProductsDef.productsDef.proGroup.prgCode.eq(prgCode);
		Page<ProductsDef> page = prodRepo.findAll(pred.and(request.searchPredicate(QProductsDef.productsDef)), request);
		return new DataTablesResult<>(request, page);
	}

	@Override
	@Transactional(readOnly = false)
	public void createProduct(ProductsDef product) throws BadRequestException {
//		if(product.getFile().isEmpty())
//			throw new BadRequestException("Policy Document File is Empty...");
//		String uploadFolder = paramService.getParameterString("APP_UPLOAD_FOLDER");
		if(product.getAgeApplicable()!=null && "on".equalsIgnoreCase(product.getAgeApplicable())){
			product.setAgeApplicable("Y");
		}
		else product.setAgeApplicable("N");
		if(product.getWibaProduct()!=null && "on".equalsIgnoreCase(product.getWibaProduct())){
			product.setWibaProduct("Y");
		}
		else product.setWibaProduct("N");
		prodRepo.save(product);
	}

	@Override
	@Transactional(readOnly = true)
	public DataTablesResult<ProductSubclasses> findProdSubClass(DataTablesRequest request, Long prodCode)
			throws IllegalAccessException {
		BooleanExpression pred = QProductSubclasses.productSubclasses.product.proCode.eq(prodCode);
		Page<ProductSubclasses> page = prodSubRepo.findAll(pred.and(request.searchPredicate(QProductSubclasses.productSubclasses)), request);
		return new DataTablesResult<>(request, page);
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
	@Transactional(readOnly = false)
	public void deleteProductGroup(Long groupId) {
		groupRepo.delete(groupId);
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteProduct(Long prodId) {
		prodRepo.delete(prodId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<SubClassDef> findUnassignedSubclasses(Long prodCode,String subName) throws IllegalAccessException {
		return prodSubRepo.getUnassignedSubclasses(prodCode,subName);
	}

	@Override
	@Transactional(readOnly = false)
	public void createProdSubclasses(ProductSubcBean prodSubclass) {
		List<ProductSubclasses> prodSubclasses = new ArrayList<>();
		for(Long subCode:prodSubclass.getSubclasses()){
			ProductSubclasses subclass = new ProductSubclasses();
			subclass.setActive(true);
			subclass.setSubclass(subclassRepo.findOne(subCode));
			subclass.setProduct(prodRepo.findOne(prodSubclass.getProCode()));
			prodSubclasses.add(subclass);
		}
		prodSubRepo.save(prodSubclasses);
	}

	@Override
	@Transactional(readOnly = false)
	public void createProductClass(ProductSubclasses prodSubclass) {
		prodSubRepo.save(prodSubclass);
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteProdSubclass(Long subId) {
		prodSubRepo.delete(subId);
	}

	@Override
	@Transactional(readOnly = true)
	public DataTablesResult<ClausesDef> findAllClauses(DataTablesRequest request,String type) throws IllegalAccessException {
		BooleanExpression pred = QClausesDef.clausesDef.clauseType.eq(type);
		Page<ClausesDef> page = clauseRepo.findAll(pred.and(request.searchPredicate(QClausesDef.clausesDef)), request);
		return new DataTablesResult<>(request, page);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
	public void createClauses(ClausesDef clause) throws BadRequestException {
		if(clause.getClauShtDesc()==null || StringUtils.isBlank(clause.getClauShtDesc())){
			throw new BadRequestException("Clause ID is Mandatory");
		}

		if(clause.getClauseType()==null){
			throw new BadRequestException("Specify the Type...");
		}

		String type = "";
		if("C".equalsIgnoreCase(clause.getClauseType())) type="Clause";
		else if("L".equalsIgnoreCase(clause.getClauseType())) type="Limit";
		else if("E".equalsIgnoreCase(clause.getClauseType())) type="Excess";
		Long count = clauseRepo.count(QClausesDef.clausesDef.clauShtDesc.equalsIgnoreCase(StringUtils.trim(clause.getClauShtDesc()))
		                              .and(QClausesDef.clausesDef.clauseType.eq(clause.getClauseType())));
		if(clause.getClauId()==null){
			if(count > 0) throw  new BadRequestException(type+" with ID Exists...");
		}
		else if(count > 1) throw  new BadRequestException(type+" with ID Exists...");
		clauseRepo.save(clause);
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteClause(Long clauseId) {
		clauseRepo.delete(clauseId);
	}

	@Override
	@Transactional(readOnly = true)
	public DataTablesResult<SubclassClauses> findSubClassClauses(DataTablesRequest request, Long subCode , String subName)
			throws IllegalAccessException {
		BooleanExpression pred = QSubclassClauses.subclassClauses.subclass.subId.eq(subCode);
		System.out.println("SEARCH = "+subName);
		if(subName!=null &&  !StringUtils.isBlank(subName)) {
			subName = StringUtils.lowerCase("%"+subName+"%");
			 pred = QSubclassClauses.subclassClauses.subclass.subId.eq(subCode).and((QSubclassClauses.subclassClauses.clause.clauHeading.toLowerCase().like(subName)).or(QSubclassClauses.subclassClauses.clause.clauWording.toLowerCase().like(subName)).or(QSubclassClauses.subclassClauses.clause.clauShtDesc.toLowerCase().like(subName)));
		}
		Page<SubclassClauses> page = subclauseRepo.findAll(pred.and(request.searchPredicate(QSubclassClauses.subclassClauses)), request);
		return new DataTablesResult<>(request, page);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<SubClassDef> findSubclassSelect(String term, Pageable pageable) {
		Predicate pred = null;
		if (term == null || StringUtils.isBlank(term)) {
			pred = QSubClassDef.subClassDef.isNotNull();
		} else {
			pred = QSubClassDef.subClassDef.subDesc.containsIgnoreCase(term);
		}
		return subclassRepo.findAll(pred, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ClausesDef> findUnassignedClauses(Long subCode, String subName) throws IllegalAccessException {
		if(subName!=null &&  !StringUtils.isBlank(subName)) {
			subName = StringUtils.lowerCase(subName);
		}
		return subclauseRepo.getUnassignedClauses(subCode, subName);
	}

	@Override
	@Transactional(readOnly = false)
	public void createSubClauses(SubclassClauseBean subclassClause) {
		List<SubclassClauses> subclassClauses = new ArrayList<>();
		for(Long claCode:subclassClause.getClauses()){
			SubclassClauses clause = new SubclassClauses();
			clause.setSubclass(subclassRepo.findOne(subclassClause.getSubCode()));
			clause.setClause(clauseRepo.findOne(claCode));
			subclassClauses.add(clause);
		}
		subclauseRepo.save(subclassClauses);
		
	}

	@Override
	@Transactional(readOnly = false)
	public void createSubClause(SubclassClauses subclassClause) {
		subclauseRepo.save(subclassClause);
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteSubClause(Long clauseId) {
		subclauseRepo.delete(clauseId);
		
	}


	@Override
	public Page<ProductSubclasses> findProductSubclasses(String term, Pageable pageable) {
		Predicate pred = null;
		if (term == null || StringUtils.isBlank(term)) {
			pred = QProductSubclasses.productSubclasses.isNotNull();
		} else {
			pred = QProductSubclasses.productSubclasses.product.proDesc.containsIgnoreCase(term)
					.or(QProductSubclasses.productSubclasses.subclass.subDesc.containsIgnoreCase(term));
		}
		return prodSubRepo.findAll(pred, pageable);
	}

	@Override
	public DataTablesResult<ProductsDef> findAllProduct(DataTablesRequest request) throws IllegalAccessException {
		Page<ProductsDef> page=prodRepo.findAll(request.searchPredicate(QProductsDef.productsDef),request);
		return new DataTablesResult<>(request,page);
	}

	@Override
	public DataTablesResult<ProductsDef> findAllProductPrg(DataTablesRequest pageable, Long prg) {
		BooleanExpression booleanExpression=QProductsDef.productsDef.productReportGroup.rptId.eq(prg);
		Page<ProductsDef> page=prodRepo.findAll(booleanExpression,pageable);
		return new DataTablesResult<>(pageable,page);

	}

	@Override
	public void addPrgProducts(Long product, Long group) {
		ProductsDef productsDef=prodRepo.findOne(product);
		ProductGroupDef productGroupDef=groupRepo.findOne(group);
		productsDef.setProGroup(productGroupDef);
		prodRepo.save(productsDef);
	}

	@Override
	public void remPrgProducts(Long product, Long group) {
		BooleanExpression booleanExpression=QProductsDef.productsDef.proGroup.prgCode.eq(group).and(
				QProductsDef.productsDef.proCode.eq(product));
		ProductsDef productGroupDef=prodRepo.findOne(booleanExpression);
		prodRepo.delete(productGroupDef);
	}
}
