package com.brokersystems.brokerapp.trans.repository;

import com.brokersystems.brokerapp.trans.model.SystemTransactionsTemp;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SystemTransactionsTempRepo extends PagingAndSortingRepository<SystemTransactionsTemp, Long>, QueryDslPredicateExecutor<SystemTransactionsTemp> {


}
