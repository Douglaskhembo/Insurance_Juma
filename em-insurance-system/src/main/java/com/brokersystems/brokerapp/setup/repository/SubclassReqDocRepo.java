package com.brokersystems.brokerapp.setup.repository;

import com.brokersystems.brokerapp.setup.model.SubClassDef;
import com.brokersystems.brokerapp.setup.model.SubClassReqdDocs;
import com.brokersystems.brokerapp.setup.model.SubclassClauses;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by peter on 3/5/2017.
 */
public interface SubclassReqDocRepo extends PagingAndSortingRepository<SubClassReqdDocs, Long>, QueryDslPredicateExecutor<SubClassReqdDocs> {


    @Query("select s from RequiredDocs s where (lower(s.reqShtDesc) like %:reqsearch% or lower(s.reqDesc) like %:reqsearch%) " +
            "and NOT EXISTS(select p from s.requiredDocs p where p.subclass.subId=:subCode)")
    public List<SubClassReqdDocs> getUnassignedReqDocs(@Param("subCode")Long subCode, @Param("reqsearch")String reqsearch);


    @Query("select s from SubClassReqdDocs s where s.subclass.subId=:sclCode and (lower(s.requiredDoc.reqShtDesc) like %:reqsearch% or lower(s.requiredDoc.reqDesc) like %:reqsearch%) and NOT EXISTS(select p from s.riskDocs p where p.risk.riskIdentifier=:riskCode)")
    public List<SubClassReqdDocs> getUnassignedRiskReqDocs(@Param("riskCode")Long riskCode,@Param("sclCode")Long sclCode, @Param("reqsearch")String reqsearch);

    @Query("select s from SubClassReqdDocs s where s.subclass.subId=:sclCode and (lower(s.requiredDoc.reqShtDesc) like %:reqsearch% or lower(s.requiredDoc.reqDesc) like %:reqsearch%) and NOT EXISTS(select p from s.riskDocs p where p.member.sectId=:riskCode)")
    public List<SubClassReqdDocs> getUnassignedMemberReqDocs(@Param("riskCode")Long riskCode,@Param("sclCode")Long sclCode, @Param("reqsearch")String reqsearch);

    @Query(value = "select srq_id,sbrd.req_sht_desc , sbrd.req_desc  from sys_brk_subclass_req_docs\n" +
            "join sys_brk_req_docs sbrd on sbrd.req_id =srq_req_code\n" +
            "where srq_id  not in (select clm_reqrd_req_id from sys_brk_clm_req_docs where clm_reqrd_clm_id =:clmId)\n" +
            "and sbrd.req_lop  =1\n" +
            "and srq_sub_code in (select risk_subclass_id from sys_brk_clm_bookings join sys_brk_risks sbr on clm_risk_id = sbr.risk_id\n" +
            " where clm_id = :clmId)\n" +
            " and (req_sht_desc like :reqsearch or req_desc like :reqsearch)", nativeQuery = true)
    List<Object[]> getUnassignedClaimsReqDocs(@Param("clmId")Long clmId, @Param("reqsearch")String reqsearch);

}
