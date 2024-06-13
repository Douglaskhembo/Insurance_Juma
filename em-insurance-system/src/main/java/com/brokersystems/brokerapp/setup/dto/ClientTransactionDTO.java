package com.brokersystems.brokerapp.setup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientTransactionDTO {

    private String policyNo;
    private String transactionRef;
    private String transactionType;
    private BigDecimal grossAmount;
    private BigDecimal netAmount;
    private BigDecimal transactionBalance;
    private Date transDate;
    private Date wefDate;
    private Date wetDate;
    private String product;





}
