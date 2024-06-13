package com.brokersystems.brokerapp.soapws.model;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface WebServiceReceiptRepo extends PagingAndSortingRepository<WebServiceReceipt, Long>, QueryDslPredicateExecutor<WebServiceReceipt> {
}
