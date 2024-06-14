package com.brokersystems.brokerapp.setup.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.brokersystems.brokerapp.setup.model.AccountDef;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface AccountRepo extends  PagingAndSortingRepository<AccountDef, Long>, QueryDslPredicateExecutor<AccountDef> {
    AccountDef findByAcctId(Long agent);

    List<AccountDef> findAccountDefByPinNo(String pinNo);

    @Query(value = "select acct_id,acct_sht_desc,acct_name,acct_phone,acct_pin,acct_status,acct_email,sys_brk_accounts.created_by,\n" +
            "     sys_brk_accounts.modified_by,sbat.acc_name , COUNT(*) OVER() as total_rows\n" +
            "            from sys_brk_accounts\n" +
            "            join sys_brk_account_types sbat on sbat.acc_id =acct_acc_code\n" +
            "            where  (lower(acct_name) like :search or lower(acct_sht_desc) like :search or lower(acc_name) like :search)\n" +
            "            order by acct_name \n" +
            "OFFSET :pageNo*:limit LIMIT :limit", nativeQuery = true)
    List<Object[]> findAllAccounts( @Param("search") String search,
                                       @Param("pageNo") int pageNo,
                                       @Param("limit") int limit);

    @Query(value = "select acct_id,acct_name,COUNT(*) OVER() AS total_rows\n" +
            "from sys_brk_accounts  \n" +
            "where acct_acc_code in (select sbat.acc_id  from sys_brk_account_types sbat where sbat.acc_type in ('INS','IA'))\n" +
            "and  (lower(acct_name) like :search or lower(acct_sht_desc) like :search)\n" +
            "order by acct_name \n" +
            "OFFSET :pageNo*:limit LIMIT :limit", nativeQuery = true)
    List<Object[]> findParentAccountTypes( @Param("search") String search,
                                           @Param("pageNo") int pageNo,
                                           @Param("limit") int limit);

    @Query(value = "select acct_id,acct_name,COUNT(*) OVER() AS total_rows\n" +
            "from sys_brk_accounts  \n" +
            "where acct_acc_code in (select sbat.acc_id  from sys_brk_account_types sbat where sbat.acc_type in ('BRK'))\n" +
            "and  (lower(acct_name) like :search or lower(acct_sht_desc) like :search)\n" +
            "order by acct_name \n" +
            "OFFSET :pageNo*:limit LIMIT :limit", nativeQuery = true)
    List<Object[]> findBrokerAccountTypes( @Param("search") String search,
                                           @Param("pageNo") int pageNo,
                                           @Param("limit") int limit);

    @Query(value = "select acct_id,acct_name,COUNT(*) OVER() AS total_rows\n" +
            "from sys_brk_accounts  \n" +
            "where acct_acc_code in (select sbat.acc_id  from sys_brk_account_types sbat where sbat.acc_type in ('RN'))\n" +
            "and  (lower(acct_name) like :search or lower(acct_sht_desc) like :search)\n" +
            "order by acct_name \n" +
            "OFFSET :pageNo*:limit LIMIT :limit", nativeQuery = true)
    List<Object[]> findReinsuranceAccountTypes( @Param("search") String search,
                                           @Param("pageNo") int pageNo,
                                           @Param("limit") int limit);

    @Query(value = "SELECT sys_brk_accounts.acct_id, sys_brk_accounts.created_by, sys_brk_accounts.created_date, sys_brk_accounts.modified_by, sys_brk_accounts.modified_date, \n" +
            "sys_brk_accounts.acct_address, sys_brk_accounts.acct_bnk_account, \n" +
            "sys_brk_accounts.acct_contact_person, sys_brk_accounts.acct_contact_title, sys_brk_accounts.acct_dob, sys_brk_accounts.acct_email, \n" +
            "sys_brk_accounts.acct_license_no, sys_brk_accounts.acct_name, sys_brk_accounts.acct_pay_tel_no, \n" +
            "sys_brk_accounts.acct_paybill, sys_brk_accounts.acct_phone, sys_brk_accounts.acct_phys_address,sys_brk_accounts.acct_pin,\n" +
            "sys_brk_accounts.acct_sht_desc, sys_brk_accounts.acct_status, sys_brk_accounts.acct_wef, \n" +
            "sys_brk_accounts.acct_wet, sys_brk_accounts.acct_acc_code, sys_brk_accounts.acct_branch_code, sys_brk_accounts.acct_bnk_code,\n" +
            "sys_brk_accounts.acct_brn_code, sys_brk_accounts.acct_pmode_id, sys_brk_accounts.acct_logo_url, sys_brk_accounts.acct_parent_acct_id,\n" +
            "sbb.bn_name,sbbb.bb_name,sbpm.pm_desc,sbb2.ob_name ,sbat.acc_name,sbat.acc_type ,sba.acct_name acctypeName \n" +
            "FROM sys_brk_accounts\n" +
            "left join sys_brk_banks sbb ON sbb.bn_id  = sys_brk_accounts.acct_bnk_code\n" +
            "left join sys_brk_bank_branches sbbb on sbbb.bb_id  = sys_brk_accounts.acct_branch_code\n" +
            "left join sys_brk_payment_modes sbpm on sbpm.pm_id  = sys_brk_accounts.acct_pmode_id\n" +
            "left join sys_brk_branches sbb2 on sbb2.ob_id  = sys_brk_accounts.acct_brn_code\n" +
            "left join sys_brk_account_types sbat on sbat.acc_id  = sys_brk_accounts.acct_acc_code\n" +
            "left join sys_brk_accounts sba on sba.acct_id  = sys_brk_accounts.acct_parent_acct_id\n" +
            "where sys_brk_accounts.acct_id =:acctId", nativeQuery = true)
    List<Object[]> findOneAccount( @Param("acctId") Long acctId);
}
