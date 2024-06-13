package com.brokersystems.brokerapp.soapws.service;

import com.brokersystems.brokerapp.server.exception.BadRequestException;
import com.brokersystems.brokerapp.setup.model.*;
import com.brokersystems.brokerapp.soapws.FormResult;
import com.brokersystems.brokerapp.soapws.ReceiptForm;
import com.brokersystems.brokerapp.trans.model.ReceiptTrans;
import com.brokersystems.brokerapp.trans.model.SystemTrans;
import com.brokersystems.brokerapp.trans.model.SystemTransactions;

import javax.jws.WebService;
import java.math.BigDecimal;
import java.text.ParseException;


public interface ReceiptingAllocation {

     public String createWebReceipt(ReceiptForm receiptForm);

     public FormResult processReceipts(ReceiptForm receiptForm);

     public String createReceipt(ReceiptTrans receipt, User user) throws BadRequestException;

    public String autoReceipt(ReceiptForm receiptForm) throws BadRequestException, ParseException;

    public void allocateCommReceipt(Long receiptId, User user, OrgBranch branch) throws BadRequestException;

    public void allocateReceipt(ReceiptTrans receipt, User user) throws BadRequestException;

    public SystemTransactions createReceiptTransaction(String refNo, BigDecimal receiptAmt, BigDecimal receiptbalance,  Currencies currency, ClientDef client,
                                                       AccountDef accnt, SystemTransactions prevTrans, SystemTrans transaction,
                                                       String narration, BigDecimal settleAmt, User user, OrgBranch branch);

    public void createSettlements(SystemTransactions debit, SystemTransactions credit, BigDecimal amount, User user)
            throws BadRequestException;

    SystemTransactions createCommissionTrans(BigDecimal commAmt, Currencies currency, AccountDef accnt, SystemTransactions prevTrans, SystemTrans transaction, String narration, String receipt, User user);
}
