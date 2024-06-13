package com.brokersystems.brokerapp.setup.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.brokersystems.brokerapp.setup.model.SubClassDef;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface SubClassRepo extends  PagingAndSortingRepository<SubClassDef, Long>, QueryDslPredicateExecutor<SubClassDef> {

    @Query(value = "select sub_id,sub_desc,total_rows=COUNT(*) OVER()   from sys_brk_subclasses \n" +
            "where (lower(sub_desc)) like :search\n" +
            "order by sub_desc\n" +
            "OFFSET :pageNo*:limit ROWS FETCH NEXT :limit ROWS ONLY",nativeQuery = true)
    List<Object[]> findSearchSubclasses(@Param("search") String search,
                               @Param("pageNo") int pageNo,
                               @Param("limit") int limit);

}
