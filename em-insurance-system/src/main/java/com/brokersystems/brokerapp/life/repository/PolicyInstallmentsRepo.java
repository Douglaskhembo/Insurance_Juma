package com.brokersystems.brokerapp.life.repository;

import com.brokersystems.brokerapp.life.model.PolicyBenefitsDistribution;
import com.brokersystems.brokerapp.life.model.PolicyInstallments;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PolicyInstallmentsRepo extends PagingAndSortingRepository<PolicyInstallments, Long>, QueryDslPredicateExecutor<PolicyInstallments> {



}
