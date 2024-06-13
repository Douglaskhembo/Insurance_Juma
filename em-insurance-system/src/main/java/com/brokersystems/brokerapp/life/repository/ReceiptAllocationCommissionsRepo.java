package com.brokersystems.brokerapp.life.repository;

import com.brokersystems.brokerapp.life.model.LifeReceiptAllocations;
import com.brokersystems.brokerapp.life.model.ReceiptAllocationCommissions;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by waititu on 18/03/2019.
 */
public interface ReceiptAllocationCommissionsRepo extends PagingAndSortingRepository<ReceiptAllocationCommissions, Long>, QueryDslPredicateExecutor<ReceiptAllocationCommissions> {

    @Query(value = "select acomm_id,acomm_alloc_id,sect_prem,acomm_cover_comm,acomm_alloc,acomm__cover_prem,acomm_comm_rate," +
            "acomm_div_fact,acomm_prem_id " +
            "from sys_brk_rct_alloc_comms s,sys_brk_rsk_limits r where s.acomm_alloc_id=:allocId and r.sect_id =s.acomm_sect_id ",nativeQuery = true)
    List<Object[]> findAllocCommisions(@Param("allocId") Long allocId);
}
