package com.brokersystems.brokerapp.uw.repository;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.brokersystems.brokerapp.uw.model.PolicyTaxes;

public interface PolTaxesRepo extends  PagingAndSortingRepository<PolicyTaxes, Long>, QueryDslPredicateExecutor<PolicyTaxes> {



}
