package com.brokersystems.brokerapp.trans.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class LifeReceiptsDTO {

    private String receiptNo;
    private Date receiptDate;
    private String dc;
    private BigDecimal receiptAmount;
    private BigDecimal allocationAmount;
    private BigDecimal balance;
    private Long receiptd;

}
