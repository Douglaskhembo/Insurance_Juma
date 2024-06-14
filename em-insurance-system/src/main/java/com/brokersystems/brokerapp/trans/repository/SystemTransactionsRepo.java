package com.brokersystems.brokerapp.trans.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.brokersystems.brokerapp.trans.model.SystemTransactions;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface SystemTransactionsRepo extends  PagingAndSortingRepository<SystemTransactions, Long>, QueryDslPredicateExecutor<SystemTransactions> {

    String query = "select pol_no,trans_ref_no,client_fname,client_onames,pol_comm_amt,trans_settle_amt ,pol_sub_agent_amt,trans_balance,pol_sub_agent_id \n" +
            "from sys_brk_main_transactions join sys_brk_policies on sys_brk_main_transactions.trans_pol_id = sys_brk_policies.pol_id\n" +
            "join sys_brk_accounts on sys_brk_policies.pol_sub_agent_id = sys_brk_accounts.acct_id\n" +
            "join sys_brk_clients on sys_brk_policies.pol_client_id = sys_brk_clients.client_id\n" +
            "where trans_agent_code in (select pol_sub_agent_id\n" +
            "from sys_brk_policies join  sys_brk_main_transactions on pol_id = trans_pol_id\n" +
            "where pol_auth_status = 'A'\n" +
            "and pol_sub_agent_id is not null\n" +
            "and trans_auth_date between :wef and :wet\n" +
            "and trans_clnt_type='C'\n" +
            "and trans_type = 'RC'\n" +
            "group by  pol_sub_agent_id\n" +
            "having  abs(sum(trans_settle_amt)) >=100)\n" +
            "and trans_auth_date between :wef and :wet\n" +
            "and  pol_sub_agent_id = case when :agent=-2000 then pol_sub_agent_id else :agent end \n" +
            " and trans_ref_no not in (select pa.pa_settle_dr_ref_no from sys_brk_payment_audit pa where pa.pa_trans_type = 'SAG')\n"+
            "and sys_brk_accounts.acct_sub_account = :subacct";

    @Query(value =query, nativeQuery = true)
    List<Object[]>   getSubAgentTrans(@Param("agent")Long acctId, @Param("wef") Date wef, @Param("wet")Date wet, @Param("subacct")Long subacct);

    String querysqlServer = "select pol_no,trans_ref_no,client_fname,client_onames,pol_comm_amt,trans_settle_amt ,pol_sub_agent_amt,trans_balance,pol_sub_agent_id \n" +
            "from sys_brk_main_transactions join sys_brk_policies on sys_brk_main_transactions.trans_pol_id = sys_brk_policies.pol_id\n" +
            "join sys_brk_accounts on sys_brk_policies.pol_sub_agent_id = sys_brk_accounts.acct_id\n" +
            "join sys_brk_clients on sys_brk_policies.pol_client_id = sys_brk_clients.client_id\n" +
            "where trans_agent_code in (select pol_sub_agent_id\n" +
            "from sys_brk_policies join  sys_brk_main_transactions on pol_id = trans_pol_id\n" +
            "where pol_auth_status = 'A'\n" +
            "and pol_sub_agent_id is not null\n" +
            "and trans_auth_date between :wef and :wet\n" +
            "and trans_clnt_type='C'\n" +
            "and trans_type = 'RC'\n" +
            "group by  pol_sub_agent_id\n" +
            "having  abs(sum(trans_settle_amt)) >=100)\n" +
            "and trans_auth_date between :wef and :wet\n" +
            "and   pol_sub_agent_id = coalesce(:agent,pol_sub_agent_id)\n" +
            " and trans_ref_no not in (select pa.pa_settle_dr_ref_no from sys_brk_payment_audit pa where pa.pa_trans_type = 'SAG')\n"+
            "and sys_brk_accounts.acct_sub_account = :subacct";

    @Query(value =querysqlServer, nativeQuery = true)
    List<Object[]> getSqlServerSubAgentTrans(@Param("agent")Long acctId, @Param("wef") Date wef, @Param("wet")Date wet, @Param("subacct")Long subacct);

    @Query(value = "select *, COUNT(*) OVER() AS total_rows from(\n" +
            "select trans_no                               transno,\n" +
            "                               trans_date             transDate,\n" +
            "                               trans_origin                           origin,\n" +
            "                               c.client_fname + ' ' + c.client_onames client,\n" +
            "                               a.acct_name                            agent,\n" +
            "                               trans_control_acc                      controlAcc,\n" +
            "                               trans_ref_no                           refNo,\n" +
            "                               b.ob_name                              branch,\n" +
            "                               trans_type                             transType,\n" +
            "                               trans_dc                               transdc,\n" +
            "                               trans_amount                           amount,\n" +
            "                               trans_net_amt                          netAmount,\n" +
            "                               trans_balance                          balance,\n" +
            "                               p.pol_no                               polNo,\n" +
            "                               trans_payee                            payeeName\n" +
            "                        from sys_brk_main_transactions sbmt\n" +
            "                               left join sys_brk_policies p on sbmt.trans_pol_id = p.pol_id\n" +
            "                               inner join sys_brk_accounts a on sbmt.trans_agent_code = a.acct_id\n" +
            "                               left outer join sys_brk_branches b on sbmt.trans_brn_id = b.ob_id\n" +
            "                               left outer join sys_brk_clients c on sbmt.trans_clnt_code = c.client_id\n" +
            "                        where (trans_authorised is null or trans_authorised = 'N')\n" +
            "                          and trans_type not in ('RFC', 'RFD')\n" +
            "                          union ALL \n" +
            "                           select trans_no                  transno,\n" +
            "                               max(cast(sbcpt.trans_date as date)) transDate,\n" +
            "                               'COMM'                        origin,\n" +
            "                               ''                            client,\n" +
            "                               a.acct_name                   agent,\n" +
            "                               a.acct_sht_desc               controlAcc,\n" +
            "                               trans_receipt_no              refNo,\n" +
            "                               sbb.ob_name                   branch,\n" +
            "                               'CP'                          transType,\n" +
            "                               'C'                           transdc,\n" +
            "                               SUM(sbcpt.trans_amount)             amount,\n" +
            "                               R.receipt_amount              netAmount,\n" +
            "                                0                             balance,\n" +
            "                               ''                            polNo,\n" +
            "                               org.org_sht_desc              payeeName\n" +
            "                        from sys_brk_organization org,\n" +
            "                             sys_brk_comm_process_trans sbcpt\n" +
            "                               inner join sys_brk_accounts a ON a.acct_id = sbcpt.trans_insurance\n" +
            "                               inner join sys_brk_receipts r on sbcpt.trans_receipt_no = r.receipt_no\n" +
            "                               inner join sys_brk_branches sbb on r.receipt_brn_code = sbb.ob_id\n" +
            "                               inner join sys_brk_main_transactions sbmt2 on trans_ref_no = r.receipt_no\n" +
            "                        where trans_status = 'R'\n" +
            "                        group by trans_no, a.acct_name, a.acct_sht_desc, trans_receipt_no, sbb.ob_name, R.receipt_amount,\n" +
            "                                 org.org_sht_desc ) as result order by transDate desc\n" +
            "OFFSET :pageNo*:limit LIMIT :limit", nativeQuery = true)
    List<Object[]> findAuthTransactions(@Param("pageNo") int pageNo,
                                         @Param("limit") int limit);

}
