package com.brokersystems.brokerapp.life.service;

import com.brokersystems.brokerapp.life.model.*;
import com.brokersystems.brokerapp.server.datatables.DataTablesRequest;
import com.brokersystems.brokerapp.server.datatables.DataTablesResult;
import com.brokersystems.brokerapp.server.exception.BadRequestException;
import com.brokersystems.brokerapp.setup.model.User;
import com.brokersystems.brokerapp.trans.model.SystemTrans;
import com.brokersystems.brokerapp.uw.model.PolicyQuestionnaire;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by waititu on 25/11/2017.
 */
public interface LifeService {

    public List<BinderPolTerms> getPolTerms(Long binCode) throws BadRequestException;


    public DataTablesResult<PolicyBeneficiaries> findPolBeneficiaries(DataTablesRequest request, Long polCode)
            throws IllegalAccessException;
    public DataTablesResult<PolicyBenefitsDistribution> findPolBenefits(DataTablesRequest request, Long polCode) throws IllegalAccessException;

    DataTablesResult<PolicyQuestionnaire> findPolQuiz(DataTablesRequest request, Long polCode) throws IllegalAccessException;

    public Iterable<PolicyQuestionnaire> findPolQuizList(Long polCode) throws IllegalAccessException;

    public void definePolicyBeneficiary(PolicyBeneficiaries beneficiaries ) throws BadRequestException;

    public void allocateLifeRcptComm(LifeReceiptAllocations savedAlloc,int paidInsts) throws BadRequestException;

    public void deletePolBeneficiary(Long benCode);

    public void deleteCommissionRates(Long commId);
    public void deallocateLifeRcpt(LifeReceipts lifeReceipts, User user) throws BadRequestException;

    public void allocateLifeRcpt(LifeReceipts lifeReceipts) throws  BadRequestException;
    public void settlePartialInstalment(LifeReceipts lifeReceipts , BigDecimal allocAmount, int paidInsts ,String refNo,
                                        SystemTrans trans) throws BadRequestException;

    public void allocateLifeRcptBalance(Long polId) throws BadRequestException ;

    public DataTablesResult<LifeReceipts> findPolReceipts(DataTablesRequest request, Long polCode) throws IllegalAccessException;

    public DataTablesResult<LifeReceiptAllocations> findReceiptsAllocations(DataTablesRequest request, Long receiptCode) throws IllegalAccessException;
    public DataTablesResult<ReceiptAllocationCommissions> findReceiptsCommissions(DataTablesRequest request, Long receiptCode) throws IllegalAccessException;

    public DataTablesResult<ReceiptAllocationCommissions> findAllocationComm(DataTablesRequest request, Long allocId) throws IllegalAccessException;





}
