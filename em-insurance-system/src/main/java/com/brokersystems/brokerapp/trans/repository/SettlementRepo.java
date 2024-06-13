package com.brokersystems.brokerapp.trans.repository;

import com.brokersystems.brokerapp.trans.model.ReceiptSettlementDetails;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;


public interface SettlementRepo extends  PagingAndSortingRepository<ReceiptSettlementDetails, Long>, QueryDslPredicateExecutor<ReceiptSettlementDetails> {

    String postgresQuery = "select x.* from (SELECT pol_no,pol_client_pol_no,client_fname,client_onames,trans.trans_ref_no,trans.rtransrefno,balances.trans_auth_date,\n" +
            "  case  balances.trans_balance\n" +
            "  when 0 then\n" +
            "    'FULL'\n" +
            "  else\n" +
            "    'PARTIAL'\n" +
            "    end as pay_status,balances.trans_net_amt,\n" +
            "  trans.trans_comm *  (rsettledamount/balances.trans_net_amt) as commission ,\n" +
            "  trans.trans_whtx *  (rsettledamount/balances.trans_net_amt) as whtx,trans.rsettledamount,balances.trans_balance,trans.trans_settle_amt,\n" +
            "  balances.trans_pol_id,'NML'\n" +
            "from (select A.trans_type,A.trans_dc,A.trans_ref_no,A.trans_balance ,A.trans_comm, A.trans_whtx,receipt.*,A.trans_agent_code,A.trans_settle_amt\n" +
            "      from sys_brk_main_transactions A,\n" +
            "(SELECT replace(rec_settle_cr_ref,'/CN','')rtransrefno ,a.trans_pol_id POLID, b.trans_auth_date transauthdate,\n" +
            "                            SUM(CASE WHEN b.trans_dc= 'C' THEN 1*rec_alloc_amt ELSE -1*rec_alloc_amt END) rsettledamount\n" +
            "                          FROM sys_receipts_settlements,sys_brk_main_transactions a , sys_brk_main_transactions b\n" +
            "                          WHERE rec_settle_dr = a.trans_no\n" +
            "                                and rec_settle_cr =b.trans_no\n" +
            "                                and B.trans_clnt_type = 'C'\n" +
            "                                and B.trans_type not in ('NBD','APD','RND')\n" +
            "                                and  B.trans_type='RC'\n" +
            "                                and B.trans_ref_no not in (select  receipt_no from sys_brk_receipts where coalesce(receipt_direct,'N')   = 'Y')\n" +
            "                          group by replace(rec_settle_cr_ref,'/CN','') ,a.trans_pol_id,b.trans_auth_date" +
            "union "+
            " SELECT replace(rec_settle_cr_ref,'/CN','')rtransrefno ,a.trans_pol_id POLID, b.trans_auth_date transauthdate,\n" +
            "        SUM(CASE WHEN b.trans_dc= 'C' THEN -1*rec_alloc_amt ELSE 1*rec_alloc_amt END) rsettledamount\n" +
            " FROM sys_receipts_settlements,sys_brk_main_transactions a , sys_brk_main_transactions b\n" +
            " WHERE rec_settle_dr = a.trans_no\n" +
            "       and rec_settle_cr =b.trans_no\n" +
            "       and B.trans_clnt_type = 'C'\n" +
            "       and B.trans_type not in ('NBD','APD','RND')\n" +
            "       and  B.trans_type ='APC'\n" +
            "       and B.trans_ref_no not in (select  receipt_no from sys_brk_receipts where coalesce(receipt_direct,'N')   = 'Y')\n" +
            " group by replace(rec_settle_cr_ref,'/CN','') ,a.trans_pol_id,b.trans_auth_date) receipt \n" +
            "      where (A.trans_balance < 0 OR (A.trans_balance > 0 AND A.trans_type IN ('APC') ))\n" +
            "            and A.trans_type not in ('SAG')\n" +
            "            and A.trans_clnt_type = 'A'\n" +
            "          --  and trans_pol_id = 305\n" +
            "            and a.trans_pol_id = receipt.POLID)trans,\n" +
            "  sys_brk_policies,sys_brk_clients,(select * from sys_brk_main_transactions where\n" +
            "    trans_clnt_type = 'C'  and  trans_type in ('NBD','APD','RND','APC') ) balances \n" +
            "where trans.polid=pol_id\n" +
            "      and pol_client_id = client_id\n" +
            "      and balances.trans_pol_id = trans.polid\n" +
            "      and trans.rsettledamount <> 0\n" +
            "      and trans.trans_agent_code = :acctId\n" +
            "      and transauthdate between :wef  and :wet  " +
            "       and round(rsettledamount,:round)<> 0 " +
            "      AND rtransrefno NOT IN (SELECT pa_settle_rec_ref_no\n" +
            "                        FROM sys_brk_payment_audit a\n" +
            "                               inner join sys_receipts_settlements s on s.rec_settle_id = a.pa_settle_cr_id\n" +
            "                               inner join sys_brk_main_transactions s1 on s1.trans_no = s.rec_settle_client_cr\n" +
            "                        WHERE  pa_posted = 'Y'" +
            "                        AND pa_settle_dr_ref_no= trans.trans_ref_no )\n " +
            "      union\n" +
            "select pol_no,pol_client_pol_no,client_fname,client_onames,trans_ref_no,trans_ref_no,trans_auth_date,'FULL' as pay_status, trans_net_amt, trans_comm,trans_whtx,trans_settle_amt,trans_balance,trans_settle_amt,trans_pol_id,'CLB' from sys_brk_main_transactions,\n" +
            "  sys_brk_policies,sys_brk_clients\n" +
            "where trans_clnt_type='A' and trans_dc='D' and trans_type='AGR'\n" +
            "      and trans_pol_id = pol_id\n" +
            "      and pol_client_id= client_id\n" +
            "      AND trans_agent_code = :acctId\n" +
            "      AND trans_balance > 0\n" +
            "      and trans_auth_date between :wef  and :wet\n" +
            "      AND trans_ref_no NOT IN (SELECT  pa_settle_dr_ref_no FROM sys_brk_payment_audit WHERE pa_settle_rec_ref_no=trans_ref_no))x where x.pay_status=coalesce(CASE WHEN :pstatus = 'ALL' then NULL else :pstatus end, pay_status)\n" +
            "\n";


    @Query(value =postgresQuery, nativeQuery = true)
    List<Object[]> getPostgresSettlementDetails(@Param("acctId")Long acctId, @Param("wef")Date wef, @Param("wet")Date wet,@Param("round")Integer round,
                                                @Param("pstatus") String pstatus);


    @Query(value ="SELECT x.* FROM (SELECT pol_no,pol_client_pol_no,client_fname,client_onames,trans.trans_ref_no,trans.rtransrefno,balances.trans_auth_date,\n" +
            "case  balances.trans_balance\n" +
            "when 0 then\n" +
            "'FULL'\n" +
            "else\n" +
            "'PARTIAL'\n" +
            "\tend as pay_status,balances.trans_net_amt,\n" +
            "\ttrans.trans_comm *  (rsettledamount/balances.trans_net_amt) as commission ,\n" +
            "\ttrans.trans_whtx *  (rsettledamount/balances.trans_net_amt) as whtx,trans.rsettledamount,balances.trans_balance,trans.trans_settle_amt,balances.trans_pol_id,'NML' as ttype\n" +
            "from (select A.trans_type,A.trans_dc,A.trans_ref_no,A.trans_balance ,A.trans_comm, A.trans_whtx,receipt.*,A.trans_agent_code,A.trans_settle_amt\n" +
            "from sys_brk_main_transactions A,\n" +
            "(SELECT replace(rec_settle_cr_ref,'/CN','')rtransrefno ,a.trans_pol_id POLID, b.trans_auth_date transauthdate,\n" +
            "                            SUM(CASE WHEN b.trans_dc= 'C' THEN 1*rec_alloc_amt ELSE -1*rec_alloc_amt END) rsettledamount\n" +
            "                          FROM sys_receipts_settlements,sys_brk_main_transactions a , sys_brk_main_transactions b\n" +
            "                          WHERE rec_settle_dr = a.trans_no\n" +
            "                                and rec_settle_cr =b.trans_no\n" +
            "                                and B.trans_clnt_type = 'C'\n" +
            "                                and B.trans_type not in ('NBD','APD','RND')\n" +
            "                                and  B.trans_type='RC'\n" +
            "                                and B.trans_ref_no not in (select  receipt_no from sys_brk_receipts where coalesce(receipt_direct,'N')   = 'Y')\n" +
            "                          group by replace(rec_settle_cr_ref,'/CN','') ,a.trans_pol_id,b.trans_auth_date " +
            " union " +
            "SELECT replace(rec_settle_cr_ref,'/CN','')rtransrefno ,a.trans_pol_id POLID,b.trans_auth_date transauthdate ,\n" +
            "        SUM(CASE WHEN b.trans_dc= 'C' THEN -1*rec_alloc_amt ELSE 1*rec_alloc_amt END) rsettledamount\n" +
            " FROM sys_receipts_settlements,sys_brk_main_transactions a , sys_brk_main_transactions b\n" +
            " WHERE rec_settle_dr = a.trans_no\n" +
            "       and rec_settle_cr =b.trans_no\n" +
            "       and B.trans_clnt_type = 'C'\n" +
            "       and B.trans_type not in ('NBD','APD','RND')\n" +
            "       and  B.trans_type ='APC'\n" +
            "       and B.trans_ref_no not in (select  receipt_no from sys_brk_receipts where coalesce(receipt_direct,'N')   = 'Y')\n" +
            " group by replace(rec_settle_cr_ref,'/CN','') ,a.trans_pol_id,b.trans_auth_date) receipt\n" +
            "where (A.trans_balance < 0 OR (A.trans_balance > 0 AND A.trans_type IN ('APC') )) \n" +
            "and A.trans_type not in ('SAG')\n" +
            "and A.trans_clnt_type = 'A'\n" +
            "and a.trans_pol_id = receipt.POLID)trans,\n" +
            "sys_brk_policies,sys_brk_clients,(select * from sys_brk_main_transactions where \n" +
            " trans_clnt_type = 'C'  and  trans_type in ('NBD','APD','RND','APC') ) balances \n" +
            "where trans.polid=pol_id\n" +
            "and pol_client_id = client_id " +
            "and balances.trans_pol_id = trans.polid\n" +
            "and trans.rsettledamount <> 0\n" +
            "and trans.trans_agent_code = :acctId\n" +
            "and transauthdate between :wef  and :wet  " +
            "and round(rsettledamount,:round)<> 0 " +
            "AND rtransrefno NOT IN (SELECT pa_settle_rec_ref_no\n" +
            "                        FROM sys_brk_payment_audit a\n" +
            "                               inner join sys_receipts_settlements s on s.rec_settle_id = a.pa_settle_cr_id\n" +
            "                               inner join sys_brk_main_transactions s1 on s1.trans_no = s.rec_settle_client_cr\n" +
            "                        WHERE  pa_posted = 'Y'" +
            "                       AND pa_settle_dr_ref_no= trans.trans_ref_no) " +
            "union "+
            "select pol_no,pol_client_pol_no,client_fname,client_onames,trans_ref_no,trans_ref_no,trans_auth_date,'FULL' as pay_status, trans_net_amt, trans_comm,trans_whtx,trans_settle_amt,trans_balance,trans_settle_amt,trans_pol_id,'CLB' as ttype " +
            "from sys_brk_main_transactions,\n" +
            "  sys_brk_policies,sys_brk_clients\n" +
            "where trans_clnt_type='A' and trans_dc='D' and trans_type='AGR'\n" +
            "      and trans_pol_id = pol_id\n" +
            "      and pol_client_id= client_id\n" +
            "      AND trans_agent_code = :acctId\n" +
            "      AND trans_balance > 0\n" +
            "      AND trans_ref_no NOT IN (SELECT  pa_settle_dr_ref_no FROM sys_brk_payment_audit WHERE pa_settle_rec_ref_no=trans_ref_no)\n" +
            "      and trans_auth_date between :wef  and :wet)x where x.pay_status=coalesce(CASE WHEN :pstatus = 'ALL' then NULL else :pstatus end, pay_status) ", nativeQuery = true)
    List<Object[]> getSqlServerSettlementDetails(@Param("acctId")Long acctId, @Param("wef")Date wef, @Param("wet")Date wet,@Param("round")Integer round,
                                                 @Param("pstatus") String pstatus);
	
	
}
