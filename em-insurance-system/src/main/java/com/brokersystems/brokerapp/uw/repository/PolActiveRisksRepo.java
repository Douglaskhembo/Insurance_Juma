package com.brokersystems.brokerapp.uw.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.brokersystems.brokerapp.uw.model.PolicyActiveRisks;
import com.brokersystems.brokerapp.uw.model.RiskTrans;

public interface PolActiveRisksRepo extends  PagingAndSortingRepository<PolicyActiveRisks, Long>, QueryDslPredicateExecutor<PolicyActiveRisks> {
	
	@Query("select s from PolicyActiveRisks s where lower(s.risk.riskShtDesc) like %:riskId% and s.policy.policyId=:polCode  and s.risk.riskId NOT IN (select p.riskId from RiskTrans p where p.policy.policyId=:polCode)")
	public Page<PolicyActiveRisks> getUnendorsedRisks(@Param("polCode")Long polCode,@Param("riskId")String riskId,Pageable pageable);
	
	@Query("select s from PolicyActiveRisks s where lower(s.risk.riskShtDesc) like %:riskId% and s.policy.policyId=:polCode and s.risk.insured.tenId = :insured and s.risk.riskId NOT IN(select p.riskId from RiskTrans p where p.policy.policyId=:polCode)")
	public Page<PolicyActiveRisks> getUnendorsedRisksByInsured(@Param("insured")Long insured,@Param("polCode")Long polCode,@Param("riskId")String riskId,Pageable pageable);

	@Query("select s from PolicyActiveRisks s where s.arId = :riskId")
	public PolicyActiveRisks getActiveRisks(@Param("riskId")Long riskId);




}
