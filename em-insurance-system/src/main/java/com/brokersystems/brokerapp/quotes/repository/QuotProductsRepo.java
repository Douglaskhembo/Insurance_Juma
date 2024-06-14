package com.brokersystems.brokerapp.quotes.repository;

import com.brokersystems.brokerapp.quotes.model.QuoteProTrans;
import com.brokersystems.brokerapp.uw.model.PolicyActiveRisks;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by peter on 3/12/2017.
 */
public interface QuotProductsRepo extends PagingAndSortingRepository<QuoteProTrans, Long>, QueryDslPredicateExecutor<QuoteProTrans> {

    @Query(value = "select sbq.quot_pr_id,  sbq2.quot_id,sbq2.quot_no,sbp.pr_desc,sbq.quot_pr_wef,sbq.quot_pr_wet,sbp2.prs_fname,sbp2.prs_onames,sbq2.quot_prs_id,prs_sht_desc,\n" +
            "            sbq2.quot_clnt_type,sbc.client_fname, sbc.client_onames,sbq2.quot_client_id,sbc2.cur_iso_code,sbq.quot_pr_converted,sbp3.pol_id,sbp3.pol_no,sba.acct_name\n" +
            "            ,COUNT(*) OVER() AS total_rows  \n" +
            "            from sys_brk_quot_products sbq \n" +
            "            join sys_brk_products sbp  on quot_pr_pro_id  = pr_code \n" +
            "            join sys_brk_accounts sba on sba.acct_id  = sbq.quot_pr_agent_id \n" +
            "            join sys_brk_quotations sbq2 on sbq2.quot_id  = sbq.quot_pr_quot_id  \n" +
            "            left join sys_brk_clients sbc on sbc.client_id  = sbq2.quot_client_id \n" +
            "            left join sys_brk_prospects sbp2 on sbp2.prs_id = sbq2.quot_prs_id \n" +
            "            join sys_brk_currencies sbc2 on sbc2.cur_code  = sbq2.quot_curr_id \n" +
            "            left join sys_brk_policies sbp3 on sbp3.pol_id  = sbq.quot_policy \n" +
            "            where isnull(sbq2.quot_prs_id,-2000) = isnull(:prsId,isnull(sbq2.quot_prs_id,-2000))\n" +
            "            and isnull(sbq2.quot_client_id ,-2000) = isnull(:clientId,isnull(sbq2.quot_client_id,-2000))\n" +
            "            and sbq.quot_pr_pro_id  = isnull(:productCode,quot_pr_pro_id)\n" +
            "            and sbq.quot_pr_agent_id  = isnull(:agentCode,quot_pr_agent_id)\n" +
            "            and sbq2.quot_no like :quotNo\n" +
            "            order by quot_date desc\n" +
            "OFFSET :pageNo*:limit LIMIT :limit",nativeQuery = true)
    List<Object[]> enquireQuotes(@Param("quotNo") String quotNo,
                                 @Param("clientId") Long clientId,
                                 @Param("prsId") Long prsId,
                                 @Param("productCode") Long productCode,
                                 @Param("agentCode") Long agentCode,
                                 @Param("pageNo") int pageNo,
                                 @Param("limit") int limit);



    @Query(value = "select sbq.quot_pr_id, sbq.quot_pr_wef,sbq.quot_pr_wet, sbb.bin_name,sbb.bin_id ,sba.acct_id ,sba.acct_name,quot_pr_sum_insured,\n" +
            "quot_pr_basic_prem,quot_pr_comm_amt,sbq2.quot_status,sbp.pr_code,sbp.pr_desc,sbb.bin_type,  COUNT(*) OVER() AS total_rows  \n" +
            "            from sys_brk_quot_products sbq \n" +
            "            join sys_brk_products sbp  on quot_pr_pro_id  = pr_code \n" +
            "            join sys_brk_quotations sbq2 on sbq2.quot_id  = sbq.quot_pr_quot_id \n" +
            "            join sys_brk_binders sbb on sbb.bin_id =quot_pr_bind_id \n" +
            "            join sys_brk_accounts sba on sba.acct_id  = sbq.quot_pr_agent_id \n" +
            "            where quot_pr_quot_id =:quotId and (acct_name like :search or bin_name like :search )\n" +
            "            order by quot_pr_id desc\n" +
            "            OFFSET :pageNo*:limit LIMIT :limit",nativeQuery = true)
    List<Object[]> enquireQuoteProducts( @Param("quotId") Long quotId,
                                 @Param("search") String search,
                                 @Param("pageNo") int pageNo,
                                 @Param("limit") int limit);




}
