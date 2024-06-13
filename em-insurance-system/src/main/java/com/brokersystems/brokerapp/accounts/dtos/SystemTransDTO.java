package com.brokersystems.brokerapp.accounts.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class SystemTransDTO {
    private final Long transno;
    private final Long transNoTemp;
    private final Date transDate;
    private final String origin;
    private final String client;
    private final String agent;
    private final String controlAcc;
    private final String refNo;
    private final String branch;
    private final String transType;
    private final String transdc;
    private final BigDecimal amount;
    private final BigDecimal netAmount;
    private final BigDecimal balance;
    private final String polNo;
    private final String payeeName;

    private SystemTransDTO(final Long transno,final Long  transNoTemp,final Date transDate, final String origin, final String client, final String agent, final String controlAcc, final String refNo, final String branch, final String transType,
                           final String transdc, final BigDecimal amount, final BigDecimal netAmount, final BigDecimal balance, final String polNo, final String payeeName) {
        this.transno = transno;
        this.transNoTemp = transNoTemp;
        this.transDate = transDate;
        this.origin = origin;
        this.client = client;
        this.agent = agent;
        this.controlAcc = controlAcc;
        this.refNo = refNo;
        this.branch = branch;
        this.transType = transType;
        this.transdc = transdc;
        this.amount = amount;
        this.netAmount = netAmount;
        this.balance = balance;
        this.polNo = polNo;
        this.payeeName = payeeName;
    }

    public static SystemTransDTO instance(final Long transno,final Long transNoTemp, final Date transDate, final String origin, final String client, final String agent, final String controlAcc, final String refNo, final String branch, final String transType,
                                          final String transdc, final BigDecimal amount, final BigDecimal netAmount,final BigDecimal balance, final String polNo, final String payeeName) {
        return new SystemTransDTO(transno,transNoTemp, transDate, origin, client, agent, controlAcc, refNo, branch, transType, transdc, amount, netAmount,balance,polNo, payeeName);
    }
}
