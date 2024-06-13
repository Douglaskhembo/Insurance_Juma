package com.brokersystems.brokerapp.setup.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.brokersystems.brokerapp.setup.model.CoverTypesDef;
import com.brokersystems.brokerapp.setup.model.SectionsDef;


public interface SectionRepo extends  PagingAndSortingRepository<SectionsDef, Long>, QueryDslPredicateExecutor<SectionsDef> {
	
	@Query("select c from SectionsDef c where c.desc like %:sectName% and NOT EXISTS(select s from c.sections s where s.subclass.subId=:subId)")
	public Page<SectionsDef> getUnassignedSections(@Param("subId") Long subId,@Param("sectName")String sectName,Pageable paramPageable);

	@Query("select c from SectionsDef c where c.desc like %:sectName% and  EXISTS(select s from c.sections s where s.subclass.subId=:subId)")
	public Page<SectionsDef> getSubclassSections(@Param("subId") Long subId,@Param("sectName")String sectName,Pageable paramPageable);


}
