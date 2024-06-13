package com.brokersystems.brokerapp.uw.repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.brokersystems.brokerapp.uw.model.PolicyTrans;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.brokersystems.brokerapp.uw.model.RiskTrans;
import com.brokersystems.brokerapp.uw.model.SectionTrans;


public interface RiskTransRepo  extends  PagingAndSortingRepository<RiskTrans, Long>, QueryDslPredicateExecutor<RiskTrans>{

	   @Query("select t from RiskTrans t where t.policy.policyId=:polId")
		public List<RiskTrans> getRiskDetails(@Param("polId") Long polId);

	@Query("select t from RiskTrans t where t.riskId=:riskId")
	  RiskTrans QueryRiskTrans(@Param("riskId") Long riskId);
	   
	   @Query(value = "select t.* from sys_brk_rsk_limits t where t.sect_risk_id=:riskId", nativeQuery = true)
		public List<Object[]> getSectionDetails(@Param("riskId") Long riskId);

	@Query(value = "select t.risk_subclass_id from sys_brk_risks t where t.risk_id=:riskId",nativeQuery = true)
	 Long getSubclassCode(@Param("riskId") Long riskId);

	@Query(value = "select t.risk_code from sys_brk_risks t where t.risk_id=:riskId",nativeQuery = true)
	Long getRiskIdentifier(@Param("riskId") Long riskId);

	@Query(value = "select risk_id,risk_code,risk_pol_id,risk_subclass_id from sys_brk_risks s where s.risk_id=:riskId ",nativeQuery = true)
	List<Object[]> findRiskTrans(@Param("riskId") Long riskId);
	public RiskTrans findFirstByRiskShtDesc(String riskId);
	public RiskTrans findFirstByInsured_TenIdAndPolicy_PolNoAndRiskShtDesc(Long idNo,String polNo,String riskId);
	public RiskTrans findFirstByPolicy_PolNoAndRiskShtDesc(String polNo,String riskId);
	public RiskTrans findFirstByInsured_TenIdAndRiskShtDesc(Long idNo,String riskId);

    RiskTrans findFirstByRiskId(Long riskId);

	@Query(value = "select risk_id,pol_no,risk_sht_desc,concat(client_fname,' ',client_onames) name,risk_binder_det_id,pol_binder_id,pol_id,total_rows=COUNT(*) OVER()  from  sys_brk_policies\n" +
			"                    join sys_brk_risks s2 on sys_brk_policies.pol_id = s2.risk_pol_id\n" +
			"                     join sys_brk_clients client on s2.risk_insured_id = client.client_id\n" +
			"                     where pol_current_status not in ('CO','D','CN')\n" +
			"                     and pol_rev_status not in ('CN','CO','DC')\n" +
			"                     and :clmdate between risk_wef_date and risk_wet_date\n" +
			"                     and risk_sht_desc is not nulL\n" +
			"                     and (risk_sht_desc like :search or pol_no like :search or  concat(client.client_fname,' ',client_onames) like :search)\n" +
			"                     order by risk_sht_desc DESC \n" +
			"                     OFFSET :pageNo*:limit ROWS FETCH NEXT :limit ROWS ONLY ", nativeQuery = true)
	List<Object[]> findClaimRisks(@Param("clmdate") Date clmdate,
									@Param("search") String search,
									@Param("pageNo") int pageNo,
									@Param("limit") int limit);

	@Query(value = "select count(1) from sys_brk_risks where upper(risk_sht_desc) = :riskId",nativeQuery = true)
	Long checkDuplicateRisks(@Param("riskId") String riskId);

	@Query(value = "select pol_id,pol_no from sys_brk_risks join sys_brk_policies on risk_pol_id = pol_id  where upper(risk_sht_desc)= :riskId",nativeQuery = true)
	List<Object[]> getDuplicateRisks(@Param("riskId") String riskId);

	@Query(value = "select risk_id from sys_brk_risks where upper(risk_sht_desc) = :riskId",nativeQuery = true)
	List<BigDecimal> findDuplicateRisks(@Param("riskId") String riskId);

	@Query(value = "select risk_id,risk_sht_desc from sys_brk_risks where upper(risk_sht_desc) = :riskId and :dt between risk_wef_date and risk_wet_date ",nativeQuery = true)
	List<Object[]> getPolicyRisks(@Param("riskId") String riskId, @Param("dt") Date dt);


	@Query(value = "select risk_sht_desc from sys_brk_risks where risk_pol_id =:policyId",nativeQuery = true)
	List<String> getPolicyRegistrations(@Param("policyId") Long policyId);

}