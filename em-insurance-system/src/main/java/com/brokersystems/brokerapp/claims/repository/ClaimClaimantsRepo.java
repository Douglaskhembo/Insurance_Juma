package com.brokersystems.brokerapp.claims.repository;

import com.brokersystems.brokerapp.claims.model.ClaimClaimants;
import com.brokersystems.brokerapp.claims.model.ClaimantsDef;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by peter on 3/8/2017.
 */
public interface ClaimClaimantsRepo extends PagingAndSortingRepository<ClaimClaimants, Long>, QueryDslPredicateExecutor<ClaimClaimants> {


    @Query(value = "select x.*,  COUNT(*) OVER() AS total_rows from(\n" +
            "select clm_clmnt_id,clm_clmnt_tp,concat(sbc.client_fname,' ',isnull(sbc.client_onames,'')) self_claimant,\n" +
            "                        concat(clmnt_surname,' ',clmnt_othernames) tp_claimant,clm_clmnt_status,sys_brk_clm_claimants.created_date,sbu.user_username,\n" +
            "                        sbcp.clm_prl_limit_amt  ,sbp.p_desc,clm_prl_type\n" +
            "                        from sys_brk_clm_claimants\n" +
            "                        left join sys_brk_clients sbc on sbc.client_id =clm_clmnt_client_id \n" +
            "                        left join sys_brk_claimants clmants on clmants.clmnt_id  = clm_clmnt_clmnt_id\n" +
            "                        left join sys_brk_users sbu on sbu.user_id =clm_created_user\n" +
            "                        join sys_brk_clm_perils sbcp on sbcp.clm_prl_clmnt_id  = clm_clmnt_id\n" +
            "                        join sys_brk_perils sbp on sbp.p_code  =sbcp.clm_prl_peril_id\n" +
            "                        where clm_clmnt_clm_id=:clmId\n" +
            "                        union \n" +
            "select csp_spr_id,'S', sbsp.provd_name,null,'1' spr_status,csp_created_dt,sbu.user_username,sbcp.clm_prl_limit_amt  ,sbp.p_desc,clm_prl_type\n" +
            "from sys_brk_clm_srv_provider\n" +
            "join sys_brk_serv_providers sbsp on sbsp.provd_id =csp_spr_id\n" +
            "join sys_brk_clm_perils sbcp on sbcp.clm_prl_spr_id =csp_id\n" +
            "join sys_brk_perils sbp on sbp.p_code  =sbcp.clm_prl_peril_id\n" +
            "left join sys_brk_users sbu on sbu.user_id =csp_created_user\n" +
            "where csp_clm_id=:clmId)x\n" +
            "order by x.created_date desc\n" +
            "OFFSET :pageNo*:limit LIMITT :limit", nativeQuery = true)
    List<Object[]> findClmClaimants(@Param("clmId") Long clmId,
                                    @Param("pageNo") int pageNo,
                                    @Param("limit") int limit);


}
