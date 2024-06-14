package com.brokersystems.brokerapp.trans.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.brokersystems.brokerapp.trans.model.ReceiptTrans;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ReceiptRepository extends  PagingAndSortingRepository<ReceiptTrans, Long>, QueryDslPredicateExecutor<ReceiptTrans> {

    @Query("select t from ReceiptTrans t where t.receiptId=:receiptId")
    ReceiptTrans getReceiptDetails(@Param("receiptId")Long receiptId);

    @Query(value = "select *, COUNT(*) OVER() AS total_rows from (select trans_no    transno,\n" +
            "                      null                     transNoTemp,\n" +
            "                      trans_date              transDate,\n" +
            "                      c.client_fname + ' ' + c.client_onames client,\n" +
            "                      trans_control_acc                      controlAcc,\n" +
            "                      trans_ref_no                           refNo,\n" +
            "                      trans_type                             transType,\n" +
            "                      trans_balance                           balance,\n" +
            "                      p.pol_no                               polNo\n" +
            "                       from sys_brk_main_transactions sbmt\n" +
            "                               join sys_brk_policies p on sbmt.trans_pol_id = p.pol_id\n" +
            "                               join sys_brk_clients c on p.pol_client_id = c.client_id\n" +
            "                       where sbmt.trans_dc = 'D'\n" +
            "                         and sbmt.trans_clnt_type = 'C'\n" +
            "                         and (sbmt.trans_ref_no = coalesce(:search, sbmt.trans_ref_no) or p.pol_no = coalesce(:search, p.pol_no))\n" +
            "                         and sbmt.trans_type != 'SAG'\n" +
            "                        and sbmt.trans_balance > 0\n" +
            "    UNION ALL \n" +
            "      select null                                   transno,\n" +
            "                              trans_no                               transNoTemp,\n" +
            "                              trans_date               transDate,\n" +
            "                              c.client_fname + ' ' + c.client_onames client,\n" +
            "                              trans_control_acc                      controlAcc,\n" +
            "                              trans_ref_no                           refNo,\n" +
            "                              trans_type                             transType,\n" +
            "                              trans_balance                          balance,\n" +
            "                              p.pol_no                               polNo\n" +
            "                       from sys_brk_temp_transactions sbmt\n" +
            "                              inner join sys_brk_policies p on sbmt.trans_pol_id = p.pol_id\n" +
            "                              inner join sys_brk_risks s2 on p.pol_id = s2.risk_pol_id\n" +
            "                              inner join sys_brk_clients c on p.pol_client_id = c.client_id\n" +
            "                       where sbmt.trans_dc = 'D'\n" +
            "                         and sbmt.trans_clnt_type = 'C'\n" +
            "                         and sbmt.trans_type != 'SAG'\n" +
            "                         and sbmt.trans_balance > 0\n" +
            "                         and (sbmt.trans_ref_no = coalesce(:search, sbmt.trans_ref_no) or p.pol_no = coalesce(:search, p.pol_no))\n" +
            "                         and sbmt.trans_authorised = 'Y') as result order by transDate desc\n" +
            "OFFSET :pageNo*:limit LIMIT :limit", nativeQuery = true)
    List<Object[]> findDebitTransactions(@Param("search") String search,
                                    @Param("pageNo") int pageNo,
                                    @Param("limit") int limit);

    @Query(value = "select sbr.receipt_no,sbr.receipt_date,rf.lrct_dc ,sbr.receipt_amount,rf.lrct_alloc_amt,rf.lrct_balance,sbr.receipt_id, COUNT(*) OVER() AS total_rows  from sys_brk_life_rcts rf\n" +
            "join sys_brk_receipts sbr on sbr.receipt_id =rf.lrct_receipt_id  \n" +
            "where lrct_policy_id =:polId\n" +
            "order by 1 desc OFFSET :pageNo*:limit LIMIT :limit", nativeQuery = true)
    List<Object[]> findPolicyReceipts(@Param("polId") Long polId,
                                      @Param("pageNo") int pageNo,
                                      @Param("limit") int limit);

}
