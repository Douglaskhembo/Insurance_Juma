package com.brokersystems.brokerapp.uw.repository;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.brokersystems.brokerapp.uw.model.PolicyClauses;

public interface PolClausesRepo extends  PagingAndSortingRepository<PolicyClauses, Long>, QueryDslPredicateExecutor<PolicyClauses> {

}
