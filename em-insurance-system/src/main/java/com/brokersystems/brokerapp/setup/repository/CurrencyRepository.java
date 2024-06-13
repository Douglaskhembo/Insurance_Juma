package com.brokersystems.brokerapp.setup.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.brokersystems.brokerapp.setup.model.Currencies;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CurrencyRepository
  extends JpaRepository<Currencies, Long>, QueryDslPredicateExecutor<Currencies>
{
	
    Page<Currencies> findByCurNameLikeIgnoreCaseAndEnabled(String paramString, Pageable paramPageable,boolean enabled);

  @Query(value = "select cur_code,cur_name,COUNT(*) OVER() AS total_rows  from sys_brk_currencies \n" +
          "where (lower(cur_name) like :search)\n" +
          "and cur_code in (select nullif(org_cur_code,cur_code) from sys_brk_organization )\n" +
          "ORDER  by cur_name \n" +
          "OFFSET :pageNo*:limit LIMIT :limit ",nativeQuery = true)
  List<Object[]> findSystemCurrencies(@Param("search") String search,
                                 @Param("pageNo") int pageNo,
                                 @Param("limit") int limit);

    @Query(value = "select cur_code,cur_name,COUNT(*) OVER() as total_rows  from sys_brk_currencies \n" +
            "where (lower(cur_name) like :search)\n" +
            "ORDER  by cur_name \n" +
            "OFFSET :pageNo*:limit LIMIT :limit",nativeQuery = true)
    List<Object[]> findOtherSystemCurrencies(@Param("search") String search,
                                        @Param("pageNo") int pageNo,
                                        @Param("limit") int limit);

  List<Currencies> findCurrenciesByCurIsoCode(String isoCode);

}
