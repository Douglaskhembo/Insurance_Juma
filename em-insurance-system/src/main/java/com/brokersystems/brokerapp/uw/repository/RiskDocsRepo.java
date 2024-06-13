package com.brokersystems.brokerapp.uw.repository;

import com.brokersystems.brokerapp.uw.model.RiskDocs;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by HP on 8/14/2017.
 */
public interface RiskDocsRepo extends PagingAndSortingRepository<RiskDocs, Long>, QueryDslPredicateExecutor<RiskDocs> {


    @Query("select d from RiskDocs d where d.rdId =:docId")
    public RiskDocs getRiskDocsVal(@Param("docId")Long docId);


    @Query(value = "select a.rd_id,a.rd_member_id,a.rd_risk_id,c.pol_id,a.rd_req_id ,a.rd_loc_name, a.rd_content_type,b.risk_binder_det_id " +
            "from sys_brk_rsk_docs a ,sys_brk_risks b,sys_brk_policies c " +
            "where a.rd_id =:docId and b.risk_id= a.rd_risk_id " +
            "and b.risk_pol_id = c.pol_id",nativeQuery = true)
    List<Object[]>  getRiskDocsVal2(@Param("docId")Long docId);

    @Modifying
    @Query(value = " delete from sys_brk_rsk_docs   where rd_id =:docId ",nativeQuery = true)
    void   deleteRiskDocs(@Param("docId")Long docId);

    @Query(value = "select a.rd_id,a.rd_req_id,a.rd_verifier,c.pol_rev_no,a.rd_loc_name " +
            "from sys_brk_rsk_docs a ,sys_brk_risks b,sys_brk_policies c " +
            "where a.rd_risk_id =:riskId and b.risk_id= a.rd_risk_id " +
            "and b.risk_pol_id = c.pol_id",nativeQuery = true)
    List<Object[]> getRiskDocs(@Param("riskId")Long riskId);


}
