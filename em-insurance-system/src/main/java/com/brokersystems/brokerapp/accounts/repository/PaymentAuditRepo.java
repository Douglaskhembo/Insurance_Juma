package com.brokersystems.brokerapp.accounts.repository;

import com.brokersystems.brokerapp.accounts.model.PaymentAudit;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by peter on 3/27/2017.
 */
public interface PaymentAuditRepo extends PagingAndSortingRepository<PaymentAudit, Long>, QueryDslPredicateExecutor<PaymentAudit> {

    @Query(value = "select distinct pa_settle_id from sys_brk_payment_audit",nativeQuery = true)
    public List<BigInteger> getAudits();

}
