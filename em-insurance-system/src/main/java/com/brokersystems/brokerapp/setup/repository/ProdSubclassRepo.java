package com.brokersystems.brokerapp.setup.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.brokersystems.brokerapp.setup.model.ProductSubclasses;
import com.brokersystems.brokerapp.setup.model.SubClassDef;


public interface ProdSubclassRepo extends  PagingAndSortingRepository<ProductSubclasses, Long>, QueryDslPredicateExecutor<ProductSubclasses> {

	@Query("select s from SubClassDef s where (lower(s.subDesc) like %:subName% or lower(s.subShtDesc) like %:subName%) and NOT EXISTS(select p from s.prodSubclasses p where p.product.proCode=:prodCode)")
	public List<SubClassDef> getUnassignedSubclasses(@Param("prodCode")Long prodCode,@Param("subName")String subName);
	
	
}
