package com.brokersystems.brokerapp.trans.service;

import com.brokersystems.brokerapp.server.datatables.DataTablesRequest;
import com.brokersystems.brokerapp.server.datatables.DataTablesResult;
import com.brokersystems.brokerapp.server.exception.BadRequestException;
import com.brokersystems.brokerapp.trans.dtos.CommissionDTO;
import com.brokersystems.brokerapp.trans.model.CommissionData;
import com.brokersystems.brokerapp.trans.model.CommissionTrans;
import com.brokersystems.brokerapp.uw.model.RiskUploadForm;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;

public interface CommissionsPayinsService {

    void importCommissions(MultipartFile file, RiskUploadForm uploadForm) throws IOException,BadRequestException;

    DataTablesResult<CommissionTrans> findAllLoadedCommissions(DataTablesRequest request,Long acct) throws IllegalAccessException;

    void processCommissions(CommissionData commissionData) throws BadRequestException;

    void confirmCommissions(Long commReceipt) throws BadRequestException;

    void undoProcessCommissions(Long receiptId) throws BadRequestException;

    public File getCommissionsTemplate() throws BadRequestException;

    void authorizeCommissions(Long transId) throws BadRequestException;

//    DataTablesResult<CommissionTrans> findAllConfirmedCommissions(DataTablesRequest pageable) throws IllegalAccessException;

    DataTablesResult<CommissionDTO> findAllConfirmedCommissions(Long agent, DataTablesRequest pageable) throws IllegalAccessException ;
}
