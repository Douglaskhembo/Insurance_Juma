package com.brokersystems.brokerapp.trans.repository;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.brokersystems.brokerapp.trans.model.GlTransactions;

public interface GlTransRepo extends  PagingAndSortingRepository<GlTransactions, Long>, QueryDslPredicateExecutor<GlTransactions> {

}
