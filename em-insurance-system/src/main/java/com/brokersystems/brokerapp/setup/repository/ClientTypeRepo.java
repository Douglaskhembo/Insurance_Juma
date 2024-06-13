package com.brokersystems.brokerapp.setup.repository;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.brokersystems.brokerapp.setup.model.ClientTypes;

public interface ClientTypeRepo extends  PagingAndSortingRepository<ClientTypes, Long>, QueryDslPredicateExecutor<ClientTypes> {

}
