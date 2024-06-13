package com.brokersystems.brokerapp.uw.repository;

import com.brokersystems.brokerapp.trans.model.HomePremiumBean;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.brokersystems.brokerapp.uw.model.PolicyTrans;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;


public interface PolicyTransRepo extends  PagingAndSortingRepository<PolicyTrans, Long>, QueryDslPredicateExecutor<PolicyTrans>{

    String postgres_query = "select  to_char(trans_date,'MM') as mont,to_char(trans_date,'Mon') as mon, "+
            " extract(year from trans_date) as yyyy,sum(case when trans_dc='C' then -trans_amount else trans_amount "+
            " end )as amount "+
            " from sys_brk_main_transactions where trans_clnt_type='C' and trans_fund_id is null "+
            " group by 1,2,3 "+
            " order by 1";

    String oracle_query = "select  to_char(trans_date,'MM') as mont,to_char(trans_date,'Mon') as mon, \n" +
            "             extract(year from trans_date) as yeard,sum(case when trans_dc='C' then -trans_amount else trans_amount \n" +
            "             end )as amount \n" +
            "             from sys_brk_main_transactions where trans_clnt_type='C' and trans_fund_id is null and trans_date > TRUNC(SysDate,'YEAR')\n" +
            "            group by to_char(trans_date,'MM'),to_char(trans_date,'Mon'),extract(year from trans_date)\n" +
            "             order by 1";

    String sqlServerQuery = "   select  FORMAT(trans_date,'MM') as mont,FORMAT(trans_date,'MMM') as mon, \n" +
            "                    FORMAT(trans_date, 'yyyy') as yeard,sum(case when trans_dc='C' then -trans_amount else trans_amount \n" +
            "                         end )as amount\n" +
            "                        from sys_brk_main_transactions where trans_user_auth=? \n" +
            "                        group by FORMAT(trans_date,'MM'),FORMAT(trans_date,'MMM'),FORMAT(trans_date, 'yyyy')\n" +
            "                         order by yeard,mont";

    String prodQuery = "select sum(pol_net_premium_amt)amt,pr_desc from sys_brk_policies\n" +
            " inner join sys_brk_products on pol_prod_id = pr_code where pol_auth_status='A' and pol_auth_user =:userId and pol_current_status not in ('CO') \n" +
            " group by pr_desc\n" +
            " order by 1 desc";

    String branchQuery="select sum(pol_net_premium_amt)amt,ob_name from sys_brk_policies\n" +
            "             inner join sys_brk_branches on pol_branch_id = ob_id\n" +
            "            where pol_auth_status='A' and pol_current_status not in ('CO')\n" +
            "            and pol_auth_user =:userId\n" +
            "            group by ob_name\n" +
            "             order by 1 desc";


    @Query(value = postgres_query,nativeQuery = true)
    public List<Object[]> getPostgresPremiumProduction();

    @Query(value = " select  sum(case when extract(year from trans_auth_date) = extract(year from current_date) then trans_amount else 0 end)\n" +
            "            from sys_brk_main_transactions where trans_clnt_type ='C'\n" +
            "            and trans_authorised ='Y'", nativeQuery = true)
    BigDecimal ytdPremium();

    @Query(value = "select sum(case when extract(year from pol_auth_date) = extract(year from current_date) then pol_total_sum_insured  else 0 end)   from sys_brk_policies  lp \n" +
            "where lp.pol_auth_status ='A'\n" +
            "and lp.pol_current_status  = 'A'", nativeQuery = true)
    BigDecimal ytdSumAssured();

    @Query(value = "select  count(case when extract(year from pol_auth_date) = extract(year from current_date) then 1 else 0 end)   from sys_brk_policies  lp \n" +
            "            where lp.pol_auth_status ='A'\n" +
            "            and lp.pol_current_status  = 'A'", nativeQuery = true)
    Long countYtpPolicies();


    @Query(value = oracle_query,nativeQuery = true)
    public List<Object[]> getOraclePremiumProduction();

    @Query(value = prodQuery,nativeQuery = true)
    public List<Object[]> getProductsPremium(@Param("userId") Long userId);

    @Query(value = branchQuery,nativeQuery = true)
    public List<Object[]> getBranchPremium(@Param("userId") Long userId);

    @Query(value = "select s5.pol_sum_insur_amt,\n" +
            "sum (case when s7.trans_dc = 'D' \n" +
            "then s7.trans_balance\n" +
            "end) as Ins_bal,\n" +
            "sum (case when s7.trans_dc = 'C' \n" +
            "then s7.trans_balance\n" +
            "end)*-1 as client_bal\n" +
            "FROM  sys_brk_risks s2\n" +
            "inner join sys_brk_policies s5 on s5.pol_id = s2.risk_pol_id\n" +
            "inner join sys_brk_main_transactions s7 on s5.pol_id = s7.trans_pol_id\n" +
            "where s5.POL_ID = :clmCode\n" +
            "group by pol_sum_insur_amt,trans_dc",nativeQuery = true)
     List<Object[]> getClaimDetails(@Param("clmCode")Long clmCode);
     PolicyTrans findFirstByClient_TenId(Long idNo);

     PolicyTrans findFirstByPolNo(String polNo);
     PolicyTrans findFirstByPolNoAndClient_TenId(String polNo,Long idNo);

    PolicyTrans findFirstByPolicyId(Long polNo);

    @Query(value = "select pol_no,pol_wef_date,pol_wet_date,sbu.user_username,pol_auth_date,pol_basic_premium_amt,pol_id,total_rows=COUNT(*) OVER()  from sys_brk_policies pol\n" +
            "join sys_brk_users sbu on sbu.user_id = pol.pol_created_user \n" +
            "where pol_current_status ='A' and pol_wet_date < GETDATE() and pol_no like :search" +
            " order by pol_no desc OFFSET :pageNo*:limit ROWS FETCH NEXT :limit ROWS ONLY",nativeQuery = true)
    List<Object[]> searchExpiredPolicies(@Param("search") String search,
                                         @Param("pageNo") int pageNo,
                                         @Param("limit") int limit);

    @Query(value = "select pol_no,pol_wef_date,pol_wet_date,sbu.user_username,pol_date ,pol_basic_premium_amt,pol_id,total_rows=COUNT(*) OVER()  from sys_brk_policies pol\n" +
            "join sys_brk_users sbu on sbu.user_id = pol.pol_created_user \n" +
            "where (pol_trans_type ='EN' or pol_trans_type ='EX') and pol_current_status not in ('A') and pol_no like :search " +
            " order by pol_no desc OFFSET :pageNo*:limit ROWS FETCH NEXT :limit ROWS ONLY ",nativeQuery = true)
    List<Object[]> searchPendingEndorsements(@Param("search") String search,
                                             @Param("pageNo") int pageNo,
                                             @Param("limit") int limit);

    @Query(value = "select pol_no,pol_wef_date,pol_wet_date,sbu.user_username,pol_date ,pol_basic_premium_amt,pol_id,total_rows=COUNT(*) OVER()  from sys_brk_policies pol\n" +
            "join sys_brk_users sbu on sbu.user_id = pol.pol_created_user \n" +
            "where (pol_trans_type ='RN') and pol_current_status not in ('A') and pol_no like :search " +
            " order by pol_no desc OFFSET :pageNo*:limit ROWS FETCH NEXT :limit ROWS ONLY ",nativeQuery = true)
    List<Object[]> searchPendingRenewals(@Param("search") String search,
                                         @Param("pageNo") int pageNo,
                                         @Param("limit") int limit);
}
