package com.brokersystems.brokerapp.soapws;

import com.brokersystems.brokerapp.accounts.repository.CollectionAcctsRepo;
import com.brokersystems.brokerapp.setup.repository.AccountRepo;
import com.brokersystems.brokerapp.setup.repository.OrgBranchRepository;
import com.brokersystems.brokerapp.setup.repository.SequenceRepository;
import com.brokersystems.brokerapp.setup.service.UserService;
import com.brokersystems.brokerapp.soapws.model.WebServiceReceiptRepo;
import com.brokersystems.brokerapp.soapws.service.ReceiptingAllocation;
import com.brokersystems.brokerapp.trans.repository.*;
import com.brokersystems.brokerapp.trans.service.AllocationService;
import com.brokersystems.brokerapp.trans.service.PolicyAuthorization;
import com.brokersystems.brokerapp.trans.service.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jws.WebService;
import java.util.Random;


@Service
@WebService(endpointInterface = "com.brokersystems.brokerapp.soapws.ReceiptingService")
public class ReceiptingServiceImpl implements ReceiptingService {

    @Autowired
    private IntegrationDtlsRepo integrationDtlsRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private ReceiptRepository receiptRepo;

    @Autowired
    private ReceiptDetailsRepository rctDetailsRepo;

    @Autowired
    private SequenceRepository sequenceRepo;

    @Autowired
    private SystemTransactionsRepo transRepo;

    @Autowired
    private OrgBranchRepository branchRepository;

    @Autowired
    private ReceiptService receiptService;

    @Autowired
    private SystemTransRepo systemTransRepo;

    @Autowired
    private CollectionAcctsRepo collectionAcctsRepo;

    @Autowired
    private SystemTransRepo systransRepo;

    @Autowired
    private AllocationService allocationService;

    @Autowired
    private SettlementRepo settlementRepo;

    @Autowired
    private WebServiceReceiptRepo webServiceReceiptRepo;

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private SystemTransactionsTempRepo systemTransactionsTempRepo;

    @Autowired
    private PolicyAuthorization policyAuthorization;


    @Autowired
    private ReceiptingAllocation receiptingAllocation;


    private static final Random random = new Random();

//    @WebMethod
    public FormResult processReceipts(final ReceiptForm receiptForm) {
         return receiptingAllocation.processReceipts(receiptForm);
    }

}
