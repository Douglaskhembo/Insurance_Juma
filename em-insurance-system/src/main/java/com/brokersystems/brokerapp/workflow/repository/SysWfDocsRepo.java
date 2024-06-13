package com.brokersystems.brokerapp.workflow.repository;

import com.brokersystems.brokerapp.workflow.docs.SysWfDocs;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by HP on 7/30/2017.
 */
public interface SysWfDocsRepo extends PagingAndSortingRepository<SysWfDocs, Long>, QueryDslPredicateExecutor<SysWfDocs> {

    @Query(value = "select x.*,count(*) OVER() AS total_count from (\n" +
            "select bwd_id,bwd_doc_active_process,case when sbp.pol_no = '' then sbp.pol_proposal_no else pol_no end pol_no,sbc.client_fname,sbc.client_onames,sbu.user_username,bwd_created_dt,pol_id,'P' as pol_type\n" +
            "            from sys_brk_wf_docs  join sys_brk_policies sbp \n" +
            "            on bwd_pol_id = sbp.pol_id \n" +
            "            left join sys_brk_clients sbc on sbc.client_id =sbp.pol_client_id \n" +
            "            left join sys_brk_users sbu on sbu.user_id  = sbp.pol_created_user \n" +
            "            where bwd_active=true  and bwd_user_id = :userid and (lower(pol_no) like :search or lower(concat(client_fname,' ',NULLIF(client_onames,''))) like :search \n" +
            "             or sbu.user_username like :search )\n" +
            "             union ALL \n" +
            "           select bwd_id,bwd_doc_active_process,quot_no pol_no,case when quot_clnt_type='C' then sbc.client_fname else sbcp.prs_fname end fname," +
            "            case when quot_clnt_type='C' then  sbc.client_onames else sbcp.prs_onames end onames,sbu.user_username,bwd_created_dt,quot_id pol_id,'Q' as pol_type\n" +
            "            from sys_brk_wf_docs  join sys_brk_quotations  sbp \n" +
            "            on bwd_quot_id  = sbp.quot_id \n" +
            "            left join sys_brk_clients sbc on sbc.client_id =sbp.quot_client_id \n" +
            "            left join sys_brk_prospects sbcp on sbcp.prs_id =sbp.quot_prs_id \n" +
            "            left join sys_brk_users sbu on sbu.user_id  = sbp.quot_prep_user \n" +
            "            where bwd_active=true  and bwd_user_id = :userid and (lower(quot_no) like :search or lower(concat(client_fname,' ',NULLIF(client_onames,''))) like :search \n" +
            "             or sbu.user_username like :search ))x order by 1 desc OFFSET :pageNo*:limit LIMIT :limit",nativeQuery = true)
    List<Object[]> getDashBoardTickets(@Param("search") String search,
                                       @Param("userid") Long userid,
                                    @Param("pageNo") int pageNo,
                                    @Param("limit") int limit);


}
