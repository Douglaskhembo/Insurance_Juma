package com.brokersystems.brokerapp.soapws.service.impl;

import com.brokersystems.brokerapp.accounts.model.CollectionAccounts;
import com.brokersystems.brokerapp.accounts.model.QCollectionAccounts;
import com.brokersystems.brokerapp.accounts.repository.CollectionAcctsRepo;
import com.brokersystems.brokerapp.server.exception.BadRequestException;
import com.brokersystems.brokerapp.setup.dto.UserDTO;
import com.brokersystems.brokerapp.setup.model.OrgBranch;
import com.brokersystems.brokerapp.setup.model.QAccountDef;
import com.brokersystems.brokerapp.setup.model.QOrgBranch;
import com.brokersystems.brokerapp.setup.model.User;
import com.brokersystems.brokerapp.setup.repository.AccountRepo;
import com.brokersystems.brokerapp.setup.repository.OrgBranchRepository;
import com.brokersystems.brokerapp.setup.service.UserService;
import com.brokersystems.brokerapp.soapws.model.QWebServiceReceipt;
import com.brokersystems.brokerapp.soapws.model.WebServiceReceipt;
import com.brokersystems.brokerapp.soapws.model.WebServiceReceiptRepo;
import com.brokersystems.brokerapp.soapws.service.BankReceiptService;
import com.brokersystems.brokerapp.soapws.service.ReceiptingAllocation;
import com.brokersystems.brokerapp.soapws.ReceiptingServiceImpl;
import com.brokersystems.brokerapp.trans.model.*;
import com.brokersystems.brokerapp.trans.repository.SystemTransactionsRepo;
import com.brokersystems.brokerapp.trans.repository.SystemTransactionsTempRepo;
import com.brokersystems.brokerapp.uw.model.QRiskTrans;
import com.brokersystems.brokerapp.uw.model.RiskTrans;
import com.brokersystems.brokerapp.uw.repository.RiskTransRepo;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP on 5/27/2018.
 */
@Service
public class BankReceiptServiceImpl implements BankReceiptService {


    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private WebServiceReceiptRepo webServiceReceiptRepo;

    @Autowired
    private OrgBranchRepository branchRepository;

    @Autowired
    private SystemTransactionsRepo transRepo;

    @Autowired
    private SystemTransactionsTempRepo systemTransactionsTempRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private CollectionAcctsRepo collectionAcctsRepo;

    @Autowired
    private ReceiptingAllocation receiptingAllocation;

    @Autowired
    private RiskTransRepo riskTransRepo;

    @Override
    @Modifying
    @Transactional(readOnly = false, rollbackFor = {BadRequestException.class}, propagation = Propagation.REQUIRES_NEW)
    public String autoReceipt(String srId) throws BadRequestException {
        System.out.println("auto receipt 1 " + srId);

        ReceiptingServiceImpl receiptingService = new ReceiptingServiceImpl();

        WebServiceReceipt webServiceReceipt = webServiceReceiptRepo.findOne(QWebServiceReceipt.webServiceReceipt.srId.eq(Long.parseLong(srId)));
        long countIns = accountRepo.count(QAccountDef.accountDef.shtDesc.eq(webServiceReceipt.getNewTransRefNumber()));
        String receiptType = countIns != 0 ? "C" : "";
        if (webServiceReceipt.getBankBranch() == null) throw new BadRequestException("Branch has not been defined..");
        Long count1 = branchRepository.count(QOrgBranch.orgBranch.obShtDesc.eq(webServiceReceipt.getBankBranch()));
        if (count1 == 0 || count1 > 1)
            throw new BadRequestException("Receipt Branch does not exist. Define the branch correctly to proceed!!!");
//        SystemTransactions transaction = transRepo.findOne(QSystemTransactions.systemTransactions.refNo.eq(webServiceReceipt.getNewTransRefNumber()).and(QSystemTransactions.systemTransactions.clientType.eq("C"))
//                .and(QSystemTransactions.systemTransactions.transdc.eq("D")));
//        SystemTransactionsTemp transactionsTemp = null;

//        if (!receiptType.equalsIgnoreCase("C") && transaction == null) {
//            transactionsTemp = systemTransactionsTempRepo.findOne(QSystemTransactionsTemp.systemTransactionsTemp.refNo.eq((webServiceReceipt.getNewTransRefNumber())).and(QSystemTransactionsTemp.systemTransactionsTemp.clientType.eq("C"))
//                    .and(QSystemTransactionsTemp.systemTransactionsTemp.transdc.eq("D")).and(QSystemTransactionsTemp.systemTransactionsTemp.authorised.eq("Y")));
//        }
        SystemTransactions transaction = null;
        SystemTransactionsTemp transactionsTemp = null;
        Predicate pred = null;
        Predicate pred2 = null;

        RiskTrans risk = riskTransRepo.findFirstByRiskShtDesc(webServiceReceipt.getNewTransRefNumber());

        if (risk != null) {
            pred = (QSystemTransactions.systemTransactions.transdc.eq("D")
                    .and(QSystemTransactions.systemTransactions.policy.riskTrans.contains(riskTransRepo.findFirstByRiskShtDesc(webServiceReceipt.getNewTransRefNumber())))
                    .and(QSystemTransactions.systemTransactions.clientType.eq("C"))
                    .and(QSystemTransactions.systemTransactions.balance.gt(BigDecimal.ZERO))
                    .and(QSystemTransactions.systemTransactions.transType.notIn("SAG")));

            pred2 = (QSystemTransactionsTemp.systemTransactionsTemp.transdc.eq("D")
                    .and(QSystemTransactionsTemp.systemTransactionsTemp.policy.riskTrans.contains(riskTransRepo.findFirstByRiskShtDesc(webServiceReceipt.getNewTransRefNumber())))
                    .and(QSystemTransactionsTemp.systemTransactionsTemp.clientType.eq("C"))
                    .and(QSystemTransactionsTemp.systemTransactionsTemp.balance.gt(BigDecimal.ZERO))
                    .and(QSystemTransactionsTemp.systemTransactionsTemp.transType.notIn("SAG"))
                    .and(QSystemTransactionsTemp.systemTransactionsTemp.authorised.eq("Y")));
        } else {
            pred = (QSystemTransactions.systemTransactions.transdc.eq("D")
                    .and(QSystemTransactions.systemTransactions.refNo.containsIgnoreCase(webServiceReceipt.getNewTransRefNumber()))
                    .and(QSystemTransactions.systemTransactions.clientType.eq("C"))
                    .and(QSystemTransactions.systemTransactions.balance.gt(BigDecimal.ZERO))
                    .and(QSystemTransactions.systemTransactions.transType.notIn("SAG")))
                    .or(QSystemTransactions.systemTransactions.transdc.eq("D")
                            .and(QSystemTransactions.systemTransactions.policy.polNo.containsIgnoreCase(webServiceReceipt.getNewTransRefNumber()))
                            .and(QSystemTransactions.systemTransactions.clientType.eq("C"))
                            .and(QSystemTransactions.systemTransactions.balance.gt(BigDecimal.ZERO))
                            .and(QSystemTransactions.systemTransactions.transType.notIn("SAG")));

            pred2 = (QSystemTransactionsTemp.systemTransactionsTemp.transdc.eq("D")
                    .and(QSystemTransactionsTemp.systemTransactionsTemp.refNo.containsIgnoreCase(webServiceReceipt.getNewTransRefNumber()))
                    .and(QSystemTransactionsTemp.systemTransactionsTemp.clientType.eq("C"))
                    .and(QSystemTransactionsTemp.systemTransactionsTemp.balance.gt(BigDecimal.ZERO))
                    .and(QSystemTransactionsTemp.systemTransactionsTemp.transType.notIn("SAG")))
                    .and(QSystemTransactionsTemp.systemTransactionsTemp.authorised.eq("Y"))
                    .or(QSystemTransactionsTemp.systemTransactionsTemp.transdc.eq("D")
                            .and(QSystemTransactionsTemp.systemTransactionsTemp.policy.polNo.containsIgnoreCase(webServiceReceipt.getNewTransRefNumber()))
                            .and(QSystemTransactionsTemp.systemTransactionsTemp.clientType.eq("C"))
                            .and(QSystemTransactionsTemp.systemTransactionsTemp.balance.gt(BigDecimal.ZERO))
                            .and(QSystemTransactionsTemp.systemTransactionsTemp.transType.notIn("SAG"))
                            .and(QSystemTransactionsTemp.systemTransactionsTemp.authorised.eq("Y")));
        }

         transaction = transRepo.findOne(pred);
         transactionsTemp =  systemTransactionsTempRepo.findOne(pred2);

        UserDTO user = userService.findByUserName("AutoReceipting");
        CollectionAccounts collectionAccounts = collectionAcctsRepo.findOne(QCollectionAccounts.collectionAccounts.name.eq("T24"));
        OrgBranch branch = branchRepository.findOne(QOrgBranch.orgBranch.obShtDesc.eq(webServiceReceipt.getBankBranch()));

        ReceiptTrans receiptTrans = new ReceiptTrans();
        if (receiptType.equalsIgnoreCase("C")) {
            if (countIns > 1)
                throw new BadRequestException("Invalid Intermediary...");
            receiptTrans.setInsurance(accountRepo.findOne(QAccountDef.accountDef.shtDesc.eq(webServiceReceipt.getNewTransRefNumber())));
        }
        receiptTrans.setReceiptType(receiptType);
        receiptTrans.setPayId(collectionAccounts.getCaId());
        receiptTrans.setBrnCode(branch.getObId());
        receiptTrans.setPaymentRef(webServiceReceipt.getTransactionCode());
        receiptTrans.setPaidBy(webServiceReceipt.getReceivedFrom());
        receiptTrans.setReceiptAmount(webServiceReceipt.getAmount());
        try {
            receiptTrans.setReceiptDate(new SimpleDateFormat("yyyyMMddHHmmss").parse("20" + webServiceReceipt.getTimeStamp() + "00"));
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        receiptTrans.setReceiptDesc("Premium Payment via Bank Integration");
        List<ReceiptTransDtls> receiptTransDtlses = new ArrayList<>();
        ReceiptTransDtls transDtls = new ReceiptTransDtls();
        transDtls.setTransNo((transaction!=null)?transaction.getTransno():null);
        transDtls.setTransTempNo((transactionsTemp!=null)?transactionsTemp.getTempTransno():null);
        transDtls.setRctAmount(webServiceReceipt.getAmount());
        transDtls.setTransaction(transaction);
        transDtls.setTransactionsTemp(transactionsTemp);
        transDtls.setPolicy((transaction != null) ? transaction.getPolicy() : (transactionsTemp != null) ? transactionsTemp.getPolicy() : null);
        transDtls.setEndorsementNumber((transaction != null) ? transaction.getPolicy().getPolRevNo() : transactionsTemp != null ? transactionsTemp.getPolicy().getPolRevNo() : "");
        receiptTransDtlses.add(transDtls);
        receiptTrans.setDetails(receiptTransDtlses);
        String generatedReceipt = receiptingAllocation.createReceipt(receiptTrans, userService.findById(user.getId()));
        webServiceReceipt.setStatus("Y");
        webServiceReceiptRepo.save(webServiceReceipt);
        System.out.println("Receipt Generated: >>> " + generatedReceipt);
        return generatedReceipt;
    }


}
