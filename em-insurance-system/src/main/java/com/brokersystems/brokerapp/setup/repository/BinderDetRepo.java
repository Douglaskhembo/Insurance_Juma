package com.brokersystems.brokerapp.setup.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.brokersystems.brokerapp.setup.model.BinderDetails;

public interface BinderDetRepo
		extends PagingAndSortingRepository<BinderDetails, Long>, QueryDslPredicateExecutor<BinderDetails> {

	@Query(nativeQuery = true, value = "select a.sc_id,b.sub_sht_desc,b.sub_desc,c.cov_sht_desc,c.cov_desc from sys_brk_sub_covertypes a,sys_brk_subclasses b,sys_brk_covertypes c " +
			"where a.sc_sub_code=b.sub_id and a.sc_cov_code=c.cov_id and sc_sub_code in (select ps_sub_code from sys_brk_prod_subcls where ps_pr_code=:proCode) " +
			"and sc_id not in (select bdet_sub_covt_code from sys_brk_binder_det where bdet_bin_code=:binCode)")
	public List<Object[]> getBinderSubclassCoverTypes(@Param("proCode") Long proCode, @Param("binCode") Long binCode);


	@Query(value = "select comm_rate  from sys_brk_comm_rates sbcr where 0 BETWEEN comm_range_from and comm_range_to and comm_bd_code=:binCode",nativeQuery = true)
	List<BigDecimal> getDefaultComm(@Param("binCode") Long binCode);

}
