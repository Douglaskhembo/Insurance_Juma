package com.brokersystems.brokerapp.setup.repository;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.brokersystems.brokerapp.setup.model.ProductGroupDef;


public interface ProductGroupRepo extends  PagingAndSortingRepository<ProductGroupDef, Long>, QueryDslPredicateExecutor<ProductGroupDef> {

}
