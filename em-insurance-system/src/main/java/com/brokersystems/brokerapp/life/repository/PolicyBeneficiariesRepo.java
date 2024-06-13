package com.brokersystems.brokerapp.life.repository;

import com.brokersystems.brokerapp.life.model.PolicyBeneficiaries;
import com.graphbuilder.org.apache.harmony.awt.gl.Crossing;
import org.drools.compiler.lang.api.PackageDescrBuilder;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by waititu on 03/12/2017.
 */
public interface PolicyBeneficiariesRepo extends PagingAndSortingRepository<PolicyBeneficiaries,Long>,QueryDslPredicateExecutor<PolicyBeneficiaries> {
}
