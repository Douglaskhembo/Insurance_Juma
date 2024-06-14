package com.brokersystems.brokerapp.claims.repository;

import com.brokersystems.brokerapp.claims.model.ClaimBookings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by peter on 3/4/2017.
 */
public interface ClaimsBookingRepo extends PagingAndSortingRepository<ClaimBookings, Long>, QueryDslPredicateExecutor<ClaimBookings> {

    @Query(value = "select clm_id,sbu.user_username,clm_loss_date,clm_date,clm_status,clm_no,sbr.risk_sht_desc,clm_next_rvw_dt,sbp.pol_no,\n" +
            "sbc.client_fname,sbc.client_onames,COUNT(*) OVER() AS total_rows  from sys_brk_clm_bookings \n" +
            "join sys_brk_users sbu on clm_booked_by = sbu.user_id \n" +
            "join sys_brk_risks sbr on clm_risk_id = sbr.risk_id \n" +
            "join sys_brk_policies sbp on sbr.risk_pol_id  = sbp.pol_id \n" +
            "join sys_brk_clients sbc on sbc.client_id =sbr.risk_insured_id  where lower(sbr.risk_sht_desc) like :riskId " +
            "and lower(sbp.pol_no) like :polNo " +
            "and (sbc.client_id = :clientCode or :clientCode is null )  and lower(clm_no) like :claimNo order by clm_date desc  OFFSET :pageNo*:limit LIMIT :limit",nativeQuery = true)
     List<Object[]> getClaimBookings(@Param("riskId") String riskId,
                                            @Param("polNo") String polNo,
                                            @Param("clientCode") Long clientCode,
                                            @Param("claimNo") String claimNo,
                                          @Param("pageNo") int pageNo,
                                          @Param("limit") int limit);



    @Query(value = "select clm_id , clm_no,concat(sbc.client_fname,' ',isnull(sbc.client_onames,''))insured,clm_loss_desc,cs.ca_desc,clm_loss_date,\n" +
            "clm_next_rvw_dt,clm_booked_dt,clm_date,clm_liability_adm,sbp.pol_no,concat(sbc2.client_fname,' ',isnull(sbc2.client_onames,''))client,\n" +
            "sbp2.pr_desc,sbr.risk_sht_desc,sbr.risk_sum_insur_amt,sbr.risk_wef_date,sbr.risk_wet_date,sbr.risk_binder_id,sbp.pol_binder_id,clm_status,clm_risk_id  from sys_brk_clm_bookings \n" +
            "left join sys_brk_risks sbr on clm_risk_id =sbr.risk_id \n" +
            "left join sys_brk_clients sbc on sbc.client_id =sbr.risk_insured_id\n" +
            "left join sys_brk_causations cs on cs.ca_id =clm_status_id\n" +
            "left join sys_brk_policies sbp on sbp.pol_id =sbr.risk_pol_id \n" +
            "left join sys_brk_clients sbc2 on sbc2.client_id =sbp.pol_client_id \n" +
            "left join sys_brk_products sbp2 on sbp2.pr_code =sbp.pol_prod_id where clm_id=:clmId",nativeQuery = true)

    List<Object[]> getClaimDetails(@Param("clmId") Long clmId);
}
