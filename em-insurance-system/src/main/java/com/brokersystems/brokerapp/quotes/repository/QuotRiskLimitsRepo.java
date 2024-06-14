package com.brokersystems.brokerapp.quotes.repository;

import com.brokersystems.brokerapp.quotes.model.QuotRiskLimits;
import com.brokersystems.brokerapp.setup.model.PremRatesDef;
import com.brokersystems.brokerapp.uw.model.PolicyActiveRisks;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by peter on 3/12/2017.
 */
public interface QuotRiskLimitsRepo extends PagingAndSortingRepository<QuotRiskLimits, Long>, QueryDslPredicateExecutor<QuotRiskLimits> {

    @Query("select p from PremRatesDef p where p.binderDet.detId=:detId and p.active =true and lower(p.section.desc) like %:searchVal% and NOT EXISTS(select s from p.quotRiskLimits s where s.risk.riskId=:riskId)")
    List<PremRatesDef> getUnassignedPremItems(@Param("detId")Long detId, @Param("riskId")Long riskId, @Param("searchVal")String searchVal);


    @Query(value = "select quot_sect_id ,sbs.sc_id,dbo.get_sect_description(sbqr.quot_rsk_sub_id,sbqr.quot_rsk_cov_id ,sbs.sc_id , sbs.sc_desc,isnull(quot_annual_earnings,0)),quot_sect_amount ,quot_sect_rate ,quot_sect_prem,quot_sect_div_fact,quot_sect_free_limit,sbq.quot_status,quot_sect_prem_id,quot_sect_rsk_id \n" +
            ",quot_annual_earnings,COUNT(*) AS total_rows OVER()\n" +
            "from sys_brk_quot_limits sbrl \n" +
            "join sys_brk_sections sbs ON sbs.sc_id =sbrl.quot_sect_sec_id  \n" +
            "join sys_brk_quot_risks sbqr on sbqr.quot_rsk_id =sbrl.quot_sect_rsk_id \n" +
            "join sys_brk_quot_products sbqp on sbqp.quot_pr_id =sbqr.quot_rsk_pr_id \n" +
            "join sys_brk_quotations sbq on sbq.quot_id =sbqp.quot_pr_quot_id \n" +
            "where sbrl.quot_sect_rsk_id =:riskId\n" +
            "and (sc_desc like :search)\n" +
            "order by quot_sect_id\n" +
            "OFFSET :pageNo*:limit LIMIT :limit",nativeQuery = true)
    List<Object[]> enquireQuoteRiskSections(@Param("riskId") Long riskId,
                                        @Param("search") String search,
                                        @Param("pageNo") int pageNo,
                                        @Param("limit") int limit);



}
