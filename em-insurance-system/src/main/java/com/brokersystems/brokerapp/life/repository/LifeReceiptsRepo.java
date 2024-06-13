package com.brokersystems.brokerapp.life.repository;

import com.brokersystems.brokerapp.life.model.LifeReceipts;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by waititu on 18/03/2019.
 */
public interface LifeReceiptsRepo extends PagingAndSortingRepository<LifeReceipts,Long>, QueryDslPredicateExecutor<LifeReceipts> {
}
