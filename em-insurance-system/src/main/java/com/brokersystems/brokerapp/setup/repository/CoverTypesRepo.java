package com.brokersystems.brokerapp.setup.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.brokersystems.brokerapp.setup.model.CoverTypesDef;

import java.util.List;


public interface CoverTypesRepo  extends  PagingAndSortingRepository<CoverTypesDef, Long>, QueryDslPredicateExecutor<CoverTypesDef> {
	
	@Query("select c from CoverTypesDef c where c.covName like %:coverName% and NOT EXISTS(select s from c.coverTypes s where s.subclass.subId=:subId)")
	public Page<CoverTypesDef> getUnassignedCoverTypes(@Param("subId") Long subId,@Param("coverName")String coverName,Pageable paramPageable);

	List<CoverTypesDef> findCoverTypesDefByCovShtDesc(String coverType);

}
