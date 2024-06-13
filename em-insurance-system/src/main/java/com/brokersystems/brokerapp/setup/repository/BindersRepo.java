package com.brokersystems.brokerapp.setup.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.brokersystems.brokerapp.setup.model.BindersDef;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface BindersRepo extends  PagingAndSortingRepository<BindersDef, Long>, QueryDslPredicateExecutor<BindersDef> {


    @Query(value = "select bin_id,sba.acct_name,sbp.pr_desc,sba.acct_id,sbp.pr_code,sbb.bin_pol_no,sbb.bin_name,sbp.pr_age_appli,sbp.pr_motor_product,\n" +
            "COUNT(*) OVER() as total_rows\n" +
            "from sys_brk_binders sbb \n" +
            "join sys_brk_products sbp on sbp.pr_code =sbb.bin_pr_code \n" +
            "join sys_brk_product_grp sbpg on sbpg.bpg_code  = sbp.pr_bpg_code  \n" +
            "join sys_brk_accounts sba on sba.acct_id  = sbb.bin_acct_code \n" +
            "where sbb.bin_auth_status ='Authorised'\n" +
            "and sbb.bin_status  = true\n" +
            "and sbpg.bpg_type not in ('MD')\n" +
            "AND (sbb.bin_name like :search or sbp.pr_desc like :search)\n" +
            "order by sbp.pr_desc OFFSET :pageNo*:limit LIMIT :limit", nativeQuery = true)
    List<Object[]> searchProductBinders(@Param("search") String search,
                                      @Param("pageNo") int pageNo,
                                      @Param("limit") int limit);

    @Query(value = "select bin_id , bin_name,bin_sht_desc, bin_pol_no,sbc.cur_name,sba.acct_name,sbat.acc_name,\n" +
            "case when bin_status=true then 'Active' else 'Inactive' end status,\n" +
            "isnull(bin_auth_status,'Draft') bin_auth_status,COUNT(*) OVER() as total_rows  from sys_brk_binders sbb \n" +
            "join sys_brk_currencies sbc on sbb.bin_cur_code = sbc.cur_code \n" +
            "join sys_brk_accounts sba  on sbb.bin_acct_code  = sba.acct_id \n" +
            "join sys_brk_account_types sbat on sbat.acc_id  = sba.acct_acc_code \n" +
            "where (sbb.bin_name like :search)\n" +
            "order by sbb.bin_name desc\n" +
            "OFFSET :pageNo*:limit limit :limit", nativeQuery = true)
    List<Object[]> searchAllBinders(@Param("search") String search,
                                        @Param("pageNo") int pageNo,
                                        @Param("limit") int limit);



}
