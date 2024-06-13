package com.brokersystems.brokerapp.setup.service;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.brokersystems.brokerapp.server.datatables.DataTablesRequest;
import com.brokersystems.brokerapp.server.datatables.DataTablesResult;
import com.brokersystems.brokerapp.server.exception.BadRequestException;
import com.brokersystems.brokerapp.setup.model.ClassesDef;
import com.brokersystems.brokerapp.setup.model.ClausesDef;
import com.brokersystems.brokerapp.setup.model.Country;
import com.brokersystems.brokerapp.setup.model.CoverSectionBean;
import com.brokersystems.brokerapp.setup.model.CoverTypesDef;
import com.brokersystems.brokerapp.setup.model.ProductGroupDef;
import com.brokersystems.brokerapp.setup.model.ProductSubcBean;
import com.brokersystems.brokerapp.setup.model.ProductSubclasses;
import com.brokersystems.brokerapp.setup.model.ProductsDef;
import com.brokersystems.brokerapp.setup.model.SectionsDef;
import com.brokersystems.brokerapp.setup.model.SubClassDef;
import com.brokersystems.brokerapp.setup.model.SubCoverTypeSections;
import com.brokersystems.brokerapp.setup.model.SubclassClauseBean;
import com.brokersystems.brokerapp.setup.model.SubclassClauses;
import com.brokersystems.brokerapp.setup.model.SubclassCoverTypes;
import com.brokersystems.brokerapp.setup.model.SubclassSections;


public interface ClassesService {
	
	DataTablesResult<ClassesDef> findAllClasses(DataTablesRequest request)  throws IllegalAccessException;
	
	void createClass(ClassesDef classDef);
	
	DataTablesResult<SubClassDef> findAllSubclass(DataTablesRequest request,Long classId)  throws IllegalAccessException;
	
	void createSubClass(SubClassDef subclassDef);
	
	public Page<ClassesDef> findClassesForSelect(String term, Pageable pageable);
	
	public void deleteClass(Long classCode);
	
	public void deleteSubclass(long subId);
	
	public Page<CoverTypesDef> findCoverTypesForSel(String term, Pageable pageable,Long subId);
	
	void createCoverType(CoverTypesDef coverType);
	
	void deleteCoverType(long coverId);
	
	void createSubClassCoverType(SubclassCoverTypes subclassCover) throws BadRequestException;
	
	void deleteSubCoverType(long subCoverId);
	
	DataTablesResult<SubclassCoverTypes> findSubclassCoverTypes(DataTablesRequest request,Long subId)  throws IllegalAccessException;
	
	DataTablesResult<SubclassSections> findSubclassSections(DataTablesRequest request,Long subId)  throws IllegalAccessException;
	
	public Page<SectionsDef> findSectionsForSel(String term, Pageable pageable,Long subId);
	
	void createSection(SectionsDef section);
	
	void createSubclassSection(SubclassSections section);
	
	void deleteSection(Long id);
	
	void deleteSubSection(Long id);
	
	DataTablesResult<SubCoverTypeSections> findSubCoverTypesSections(DataTablesRequest request,Long scsCode)  throws IllegalAccessException;
	
	List<SubclassSections> findUnassignedSections(Long scsCode,Long SubId)  throws IllegalAccessException;
	
	void createCoverSections(CoverSectionBean section);
	
	void createCoverSection(SubCoverTypeSections section);
	
	void deleteCoverSection(Long id);
	
    public Page<ProductGroupDef> findProductGroupforSel(String paramString, Pageable paramPageable);
	
	void createProductGroup(ProductGroupDef group);
	
	DataTablesResult<ProductsDef> findAllProducts(DataTablesRequest request,Long prgCode)  throws IllegalAccessException;
	
	void createProduct(ProductsDef product) throws BadRequestException;
	
	DataTablesResult<ProductSubclasses> findProdSubClass(DataTablesRequest request,Long prodCode)  throws IllegalAccessException;
	
	public Page<ProductsDef> findProductsSel(String paramString, Pageable paramPageable);
	
	void deleteProductGroup(Long groupId);
	
	void deleteProduct(Long prodId);
	
	List<SubClassDef> findUnassignedSubclasses(Long prodCode,String subName)  throws IllegalAccessException;
	
	void createProdSubclasses(ProductSubcBean section);
	
	void createProductClass(ProductSubclasses prodSubclass);
	
	void deleteProdSubclass(Long subId);
	
	DataTablesResult<ClausesDef> findAllClauses(DataTablesRequest request,String type)  throws IllegalAccessException;
	
	void createClauses(ClausesDef clause) throws BadRequestException;
	
	void deleteClause(Long clauseId);
	
	DataTablesResult<SubclassClauses> findSubClassClauses(DataTablesRequest request,Long subCode, String subName)  throws IllegalAccessException;
	
	public Page<SubClassDef> findSubclassSelect(String term, Pageable pageable);
	
	List<ClausesDef> findUnassignedClauses(Long subCode,String subName)  throws IllegalAccessException;
	
	public void createSubClauses(SubclassClauseBean subclassClause);
	
	public void createSubClause(SubclassClauses subclassClause);
	
	void deleteSubClause(Long clauseId);

	public Page<ProductSubclasses> findProductSubclasses(String term, Pageable pageable);

    DataTablesResult<ProductsDef> findAllProduct(DataTablesRequest pageable) throws IllegalAccessException;

    DataTablesResult<ProductsDef> findAllProductPrg(DataTablesRequest pageable, Long prg);

    void addPrgProducts(Long product, Long group);

	void remPrgProducts(Long product, Long group);
}
