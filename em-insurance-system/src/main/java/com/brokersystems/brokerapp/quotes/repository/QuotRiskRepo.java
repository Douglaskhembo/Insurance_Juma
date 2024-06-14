package com.brokersystems.brokerapp.quotes.repository;

import com.brokersystems.brokerapp.quotes.model.QuoteRiskTrans;
import com.brokersystems.brokerapp.uw.model.PolicyActiveRisks;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by peter on 3/12/2017.
 */
public interface QuotRiskRepo extends PagingAndSortingRepository<QuoteRiskTrans, Long>, QueryDslPredicateExecutor<QuoteRiskTrans> {


    @Query(value = "select quot_rsk_id,quote_rsk_sht_desc,quot_rsk_desc,quot_rsk_wef,quot_rsk_wet,sbs.sub_id,sbs.sub_desc,\n" +
            "sbc.cov_id,sbc.cov_desc,quot_rsk_value, quot_rsk_premium,sbq.quot_status,risk_prorata,quot_rsk_com_rate,quot_risk_but_charge,sbcp.client_id,sbcp.client_fname,sbcp.client_onames,\n" +
            "sbp.prs_id,sbp.prs_fname,sbp.prs_onames,quot_rsk_bin_det,COUNT(*) OVER() AS total_rows   \n" +
            "from sys_brk_quot_risks sbqr\n" +
            "join sys_brk_subclasses sbs on sbs.sub_id =sbqr.quot_rsk_sub_id \n" +
            "join sys_brk_covertypes sbc on sbc.cov_id =sbqr.quot_rsk_cov_id \n" +
            "join sys_brk_quot_products sbqp ON sbqp.quot_pr_id =sbqr.quot_rsk_pr_id \n" +
            "join sys_brk_quotations sbq on sbq.quot_id =sbqp.quot_pr_quot_id " +
            "left join sys_brk_clients sbcp on sbcp.client_id  = quot_rsk_insured_id\n" +
            "left join sys_brk_prospects sbp on sbp.prs_id =quot_risk_prs_id\n" +
            "where sbqr.quot_rsk_pr_id  =:prodId\n" +
            "and (quote_rsk_sht_desc like :search or quot_rsk_desc like :search)\n" +
            "order by quote_rsk_sht_desc\n" +
            "OFFSET :pageNo*:limit LIMIT :limit",nativeQuery = true)
    List<Object[]> enquireQuoteProducts(@Param("prodId") Long prodId,
                                        @Param("search") String search,
                                        @Param("pageNo") int pageNo,
                                        @Param("limit") int limit);

}
