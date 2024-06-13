package com.brokersystems.brokerapp.setup.repository;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.brokersystems.brokerapp.setup.model.CommissionRates;

public interface CommRatesRepo extends  PagingAndSortingRepository<CommissionRates, Long>, QueryDslPredicateExecutor<CommissionRates> {

}
