package com.brokersystems.brokerapp.setup.repository;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.brokersystems.brokerapp.setup.model.ProductsDef;

import java.util.List;


public interface ProductsRepo extends  PagingAndSortingRepository<ProductsDef, Long>, QueryDslPredicateExecutor<ProductsDef> {

    List<ProductsDef> findProductsDefByProShtDesc(String shtDesc);
}
