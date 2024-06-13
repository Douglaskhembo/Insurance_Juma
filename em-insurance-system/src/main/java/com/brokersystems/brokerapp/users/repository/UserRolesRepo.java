package com.brokersystems.brokerapp.users.repository;

import com.brokersystems.brokerapp.users.model.UserRole;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by peter on 4/17/2017.
 */
public interface UserRolesRepo extends PagingAndSortingRepository<UserRole, Long>, QueryDslPredicateExecutor<UserRole> {
}
