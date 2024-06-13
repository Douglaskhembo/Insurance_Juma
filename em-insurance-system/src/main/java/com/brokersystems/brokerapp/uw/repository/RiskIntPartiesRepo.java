package com.brokersystems.brokerapp.uw.repository;

import com.brokersystems.brokerapp.uw.model.RiskInterestedParties;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by HP on 9/13/2017.
 */
public interface RiskIntPartiesRepo extends PagingAndSortingRepository<RiskInterestedParties, Long>, QueryDslPredicateExecutor<RiskInterestedParties> {
}
