package com.brokersystems.brokerapp.trans.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class CommissionDTO {
    private final Long transId;
    private final String receiptNo;
    private final Date receiptDate;
    private final String insuranceCo;
    private final BigDecimal totalCommission;
    private final String status;
    private final BigDecimal originalAmount;
    private final BigDecimal balance;


    private CommissionDTO(final Long transId, final String receiptNo, final Date receiptDate, final String insuranceCo, final BigDecimal totalCommission, final String status, final BigDecimal originalAmount, final BigDecimal balance) {
        this.transId = transId;
        this.receiptNo = receiptNo;
        this.receiptDate = receiptDate;
        this.insuranceCo = insuranceCo;
        this.totalCommission = totalCommission;
        this.status = status;
        this.originalAmount = originalAmount;
        this.balance = balance;
    }

    public static CommissionDTO instance(final Long transId,final String receiptNo, final Date receiptDate, final String insuranceCo, final BigDecimal totalCommission, final String status, final BigDecimal originalAmount, final BigDecimal balance) {
        return new CommissionDTO(transId,receiptNo, receiptDate, insuranceCo, totalCommission, status, originalAmount, balance);
    }

}
