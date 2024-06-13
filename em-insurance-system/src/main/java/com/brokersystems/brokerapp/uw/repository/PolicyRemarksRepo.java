package com.brokersystems.brokerapp.uw.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import com.brokersystems.brokerapp.setup.model.EndorsementRemarks;
import com.brokersystems.brokerapp.uw.model.PolicyRemarks;

public interface PolicyRemarksRepo extends JpaRepository<PolicyRemarks, Long>, QueryDslPredicateExecutor<PolicyRemarks> {
	
	@Query("select s from EndorsementRemarks s where lower(s.remarkShtDesc) like %:remark%  and s.remarkId NOT IN (select p.endRemarks.remarkId from PolicyRemarks p where p.policy.policyId=:polCode)")
	public Page<EndorsementRemarks> getEndorsementRemarks(@Param("polCode")Long polCode,@Param("remark")String remark,Pageable pageable);
	

}
