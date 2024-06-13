package com.brokersystems.brokerapp.setup.repository;

import com.brokersystems.brokerapp.setup.model.ClientDef;
import com.brokersystems.brokerapp.setup.model.ProspectDef;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by peter on 4/9/2017.
 */
public interface ProspectsRepo extends PagingAndSortingRepository<ProspectDef, Long>, QueryDslPredicateExecutor<ProspectDef> {

    @Query(value = "select prs_id,prs_fname,prs_onames,COUNT(*) OVER() AS total_rows from sys_brk_prospects\n" +
            "where prs_status = 'A' and  (lower(prs_fname) like :search or lower(prs_onames) like :search)\n" +
            "order by prs_fname asc\n" +
            "OFFSET :pageNo*:limit LIMIT :limit",nativeQuery = true)
    List<Object[]> findProspcts(@Param("search") String search,
                               @Param("pageNo") int pageNo,
                               @Param("limit") int limit);

    @Query(value = "select prs_id,prs_fname,prs_onames,prs_phone,prs_dob,  sbc.cnt_type_desc,sbc.cnt_type_id,prs_status,prs_category,sbb.ob_id,\n" +
            "sbb.ob_name,sba.acct_id,sba.acct_name,prs_comment,prs_sht_desc,prs_email,prs_gender,COUNT(*) OVER() AS total_rows  from sys_brk_prospects sbp\n" +
            "join sys_brk_client_types sbc on sbp.prs_clnt_type  = sbc.cnt_type_id \n" +
            "left join sys_brk_branches sbb on sbb.ob_id  = sbp.prs_branch \n" +
            "left join sys_brk_accounts sba on sba.acct_id  = sbp.prs_sub_agent \n" +
            "where (lower(prs_fname) like :search or lower(prs_onames) like :search)\n" +
            "order by prs_fname asc\n" +
            "OFFSET :pageNo*:limit LIMIT :limit",nativeQuery = true)
    List<Object[]> findProspctsListing(@Param("search") String search,
                                @Param("pageNo") int pageNo,
                                @Param("limit") int limit);

    @Query(value = "select prs_id,prs_fname,prs_onames,prs_phone,prs_dob,  sbc.cnt_type_desc,sbc.cnt_type_id,prs_status,prs_category,sbb.ob_id,\n" +
            "sbb.ob_name,sba.acct_id,sba.acct_name,prs_comment,prs_sht_desc,prs_email,prs_gender  from sys_brk_prospects sbp\n" +
            "join sys_brk_client_types sbc on sbp.prs_clnt_type  = sbc.cnt_type_id \n" +
            "left join sys_brk_branches sbb on sbb.ob_id  = sbp.prs_branch \n" +
            "left join sys_brk_accounts sba on sba.acct_id  = sbp.prs_sub_agent where prs_id=:prsId",nativeQuery = true)
    List<Object[]> findProspctsId(@Param("prsId") long prsId);

}
