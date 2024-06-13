package com.brokersystems.brokerapp.trans.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.brokersystems.brokerapp.trans.model.ReceiptTransDtls;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ReceiptDetailsRepository extends  PagingAndSortingRepository<ReceiptTransDtls, Long>, QueryDslPredicateExecutor<ReceiptTransDtls> {


    @Query(value = "select * from sys_brk_receipt_dtls where rect_receipt_no=:receiptNo",nativeQuery = true)
    List<ReceiptTransDtls> findAllReceipts(@Param("receiptNo") Long receiptNo);

}
