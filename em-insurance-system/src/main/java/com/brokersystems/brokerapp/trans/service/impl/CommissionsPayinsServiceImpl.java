package com.brokersystems.brokerapp.trans.service.impl;

import com.brokersystems.brokerapp.server.datatables.DataTablesRequest;
import com.brokersystems.brokerapp.server.datatables.DataTablesResult;
import com.brokersystems.brokerapp.server.exception.BadRequestException;
import com.brokersystems.brokerapp.setup.model.AccountDef;
import com.brokersystems.brokerapp.trans.dtos.CommissionDTO;
import com.brokersystems.brokerapp.trans.mappers.CommDtoMapper;
import com.brokersystems.brokerapp.trans.model.*;
import com.brokersystems.brokerapp.trans.repository.CommissionQueries;
import com.brokersystems.brokerapp.trans.repository.CommissionTransRepo;
import com.brokersystems.brokerapp.trans.repository.ReceiptRepository;
import com.brokersystems.brokerapp.trans.repository.SystemTransactionsRepo;
import com.brokersystems.brokerapp.trans.service.CommissionsPayinsService;
import com.brokersystems.brokerapp.uw.model.RiskUploadForm;
import com.mysema.query.types.expr.BooleanExpression;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;
import javax.ws.rs.core.Response;
import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class CommissionsPayinsServiceImpl implements CommissionsPayinsService {

    @Autowired
    private CommissionTransRepo commissionTransRepo;

    @Autowired
    private SystemTransactionsRepo systemTransactionsRepo;

    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private DataSource dataSource;

    @Override
    public DataTablesResult<CommissionTrans> findAllLoadedCommissions(DataTablesRequest request, Long acct) throws IllegalAccessException {
        BooleanExpression pred = QCommissionTrans.commissionTrans.status.eq("D");
        Page<CommissionTrans> page = null;
        if(acct!=null)
        page = commissionTransRepo.findAll(pred.and(request.searchPredicate(QCommissionTrans.commissionTrans)).and(QCommissionTrans.commissionTrans.insurance.eq(acct)), request);
        return new DataTablesResult(request, page);
    }

    @Override
    public void importCommissions(MultipartFile file, RiskUploadForm uploadForm)throws BadRequestException, IOException {
        if(uploadForm.getReceipt()==null ){
            throw new BadRequestException("Select A commission Receipt to Continue!");
        }
        Long receipt = systemTransactionsRepo.findOne(uploadForm.getReceipt()).getTransno();
        String receiptNo = systemTransactionsRepo.findOne(uploadForm.getReceipt()).getRefNo();
        Long insurance = uploadForm.getInsurance().getAcctId();
        List<CommissionTrans> commissionTransList = new ArrayList<>();
        byte [] byteArr=file.getBytes();
        InputStream excelFile = new ByteArrayInputStream(byteArr);
        Workbook workbook = new HSSFWorkbook(excelFile);
        for(int i=0;i<workbook.getNumberOfSheets();i++) {
            Sheet sheet = workbook.getSheetAt(i);
            if (i == 0) {
                Iterator<Row> iterator = sheet.iterator();
                while (iterator.hasNext()) {
                    Row currentRow = iterator.next();

                    if (currentRow.getRowNum() != 0) {
                        CommissionTrans commissionTrans = new CommissionTrans();
                        commissionTrans.setStatus("D");
                        commissionTrans.setInsurance(insurance);
                        commissionTrans.setReceipt(receipt);
                        commissionTrans.setReceiptNo(receiptNo);
                        commissionTrans.setDate(new Date());
                        Iterator<Cell> cellIterator = currentRow.iterator();
                        while (cellIterator.hasNext()) {
                            Cell currentCell = cellIterator.next();
                            int columnIndex = currentCell.getColumnIndex();
                            switch (columnIndex) {
                                case 0:
                                    if (currentCell.getCellType() == Cell.CELL_TYPE_STRING) {
                                        commissionTrans.setDebitNoteNo(currentCell.getStringCellValue());
                                    }
                                    break;
                                case 1:
                                    if (currentCell.getCellType() == Cell.CELL_TYPE_STRING) {
                                        System.out.println(currentCell.getStringCellValue());
                                        commissionTrans.setAmount(new BigDecimal(currentCell.getStringCellValue()));
                                    }
                                    else  if (currentCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                        commissionTrans.setAmount(BigDecimal.valueOf(currentCell.getNumericCellValue()));
                                    }
                                    break;
                                case 2:
                                    if (currentCell.getCellType() == Cell.CELL_TYPE_STRING) {
                                        commissionTrans.setPolicyNo(currentCell.getStringCellValue());
                                    }
                                    break;
                                case 3:
                                    if (currentCell.getCellType() == Cell.CELL_TYPE_STRING) {
                                        commissionTrans.setInspolicyNo(currentCell.getStringCellValue());
                                    }
                                    break;
                            }
                        }
                        commissionTransList.add(commissionTrans);
                    }

                }

            }
        }
        commissionTransRepo.save(commissionTransList);

    }

    @Override
    public void processCommissions(CommissionData commissionData) throws BadRequestException {
        if(commissionData.getCommissionids()==null || commissionData.getCommissionids().size()==0 ){
            throw new BadRequestException("No commission to process...");
        }

        if(commissionData.getTransId()==null){ throw new BadRequestException("Select Insurance Receipt to Process...."); }

        SystemTransactions receipt = systemTransactionsRepo.findOne(commissionData.getTransId());

        if(receipt==null)  throw new BadRequestException("Select a Commission Receipt to Process");

        for(Long a:commissionData.getCommissionids()) {
            CommissionTrans commissionTrans = commissionTransRepo.findOne(a);
            Iterable<SystemTransactions> systemTransactions = systemTransactionsRepo.findAll(QSystemTransactions.systemTransactions.transType.eq("COMM").and(QSystemTransactions.systemTransactions.transdc.eq("D"))
                    .and(QSystemTransactions.systemTransactions.refNo.eq(commissionTrans.getDebitNoteNo())));
            BigDecimal receiptAmt = receipt.getBalance().abs();
            BigDecimal initialreceiptAmt = receipt.getBalance().abs();
            for (SystemTransactions transactions : systemTransactions) {
                BigDecimal balance = transactions.getBalance();
                    receiptAmt = receiptAmt.subtract(balance);
            }
            if (receiptAmt.compareTo(BigDecimal.ZERO) < 0 || receiptAmt.compareTo(initialreceiptAmt) == 0)
                throw new BadRequestException("The Receipt Amount is not enough to process selected debits or there are no matching commission records to process");

        commissionTrans.setStatus("P");
        commissionTransRepo.save(commissionTrans);
    }
    }

    @Override
    public void confirmCommissions(Long commReceipt) throws BadRequestException {
        if (commReceipt == null) {
            throw new BadRequestException("Select A record to Process....");
        }
        SystemTransactions receipt = systemTransactionsRepo.findOne(commReceipt);
        if (receipt == null) throw new BadRequestException("Select a Commission Receipt Record to Process...");
        Iterable<CommissionTrans> commissionData = commissionTransRepo.findAll(QCommissionTrans.commissionTrans.receiptNo.eq(receipt.getRefNo()).and(QCommissionTrans.commissionTrans.status.eq("P")));
        for (CommissionTrans commissionTrans : commissionData) {
            commissionTrans.setStatus("R");
            commissionTransRepo.save(commissionTrans);
        }
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
    public void undoProcessCommissions(Long receiptId) throws BadRequestException {
        if (receiptId== null) {
            throw new BadRequestException("Select records to undo commission processing...");
        }
        Iterable<CommissionTrans> commissionTrans = commissionTransRepo.findAll(QCommissionTrans.commissionTrans.receipt.eq(receiptId).and(QCommissionTrans.commissionTrans.status.equalsIgnoreCase("P")));
        for (CommissionTrans comm : commissionTrans) {
            comm.setStatus("D");
        }
        commissionTransRepo.save(commissionTrans);
    }

    @Override
    public void authorizeCommissions(Long transId) throws BadRequestException {

        SystemTransactions receiptTrans = systemTransactionsRepo.findOne(transId);

        ReceiptTrans receipt = receiptRepository.findOne(QReceiptTrans.receiptTrans.receiptNo.equalsIgnoreCase(receiptTrans.getRefNo()));

        Iterable<CommissionTrans> commissionTrans = commissionTransRepo.findAll(QCommissionTrans.commissionTrans.receiptNo.eq(receipt.getReceiptNo()).and(QCommissionTrans.commissionTrans.status.equalsIgnoreCase("R")));

        if(receipt==null)
            throw new BadRequestException("Select a Commission Receipt to Process");

        List<SystemTransactions> transactionsList = new ArrayList<>();

        for(CommissionTrans comm :commissionTrans){
            Iterable<SystemTransactions> systemTransactions = systemTransactionsRepo.findAll(QSystemTransactions.systemTransactions.transType.eq("COMM").and(QSystemTransactions.systemTransactions.transdc.eq("D"))
            .and(QSystemTransactions.systemTransactions.refNo.eq(comm.getDebitNoteNo())));

             BigDecimal receiptAmt = receiptTrans.getBalance().abs();
             BigDecimal initialreceiptAmt = receiptTrans.getBalance().abs();

            for(SystemTransactions transactions:systemTransactions){
                BigDecimal balance = transactions.getBalance();
                if (receiptAmt.compareTo(transactions.getBalance()) == 1 || receiptAmt.compareTo(transactions.getBalance()) == 0) {
                    transactions.setSettleAmt((balance != null) ? transactions.getSettleAmt().add(balance.abs()) : balance.abs());
                    transactions.setBalance(BigDecimal.ZERO);
                    receiptAmt = receiptAmt.subtract(balance);
                    //prorataAgBalance = 1;
                } else {
                    if (transactions.getSettleAmt() == null) {
                        transactions.setSettleAmt(balance.abs());
                    } else
                        transactions.setSettleAmt(transactions.getSettleAmt().add(balance.abs()));
                    transactions.setBalance(transactions.getBalance().subtract(balance.abs()));
                    receiptAmt = receiptAmt.subtract(balance);
                }
                transactionsList.add(transactions);
            }

            if(receiptAmt.compareTo(BigDecimal.ZERO) < 0  || receiptAmt.compareTo(initialreceiptAmt)==0)
                throw new BadRequestException("Insurance Receipt Amount is not enough to process selected debits or no matching commission records to process");
            receiptTrans.setBalance(receiptAmt.negate());
            receiptTrans.setSettleAmt((receiptTrans.getSettleAmt() != null) ? receiptTrans.getSettleAmt().abs().add(initialreceiptAmt.subtract(receiptAmt)): initialreceiptAmt.subtract(receiptAmt));
            transactionsList.add(receiptTrans);
            comm.setStatus("A");
            commissionTransRepo.save(comm);
        }
        systemTransactionsRepo.save(transactionsList);
    }

//    @Override
//    public DataTablesResult<CommissionTrans> findAllConfirmedCommissions(DataTablesRequest request) throws IllegalAccessException {
//        BooleanExpression pred = QCommissionTrans.commissionTrans.status.eq("P");
//        Page<CommissionTrans> page = commissionTransRepo.findAll(pred.and(request.searchPredicate(QCommissionTrans.commissionTrans)), request);
//        return new DataTablesResult(request, page);
//    }
    @Override
    public DataTablesResult<CommissionDTO> findAllConfirmedCommissions(Long agent, DataTablesRequest pageable) throws IllegalAccessException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        CommDtoMapper mapper = new CommDtoMapper();
        long countProcessedComm = 0;
        List<CommissionDTO> commissionDTOList=null;
        if(agent!=null) {
            countProcessedComm = jdbcTemplate.queryForObject(CommissionQueries.countProcessedCommissions,Long.class,new Object[]{agent});
            commissionDTOList = jdbcTemplate.query(CommissionQueries.processedCommQuery,mapper,new Object[]{agent, pageable.getPageNumber(),pageable.getPageSize(),pageable.getPageNumber(),pageable.getPageSize()});
        }
        Page<CommissionDTO> page = new PageImpl<>(commissionDTOList, pageable, countProcessedComm);

        return new DataTablesResult(pageable, page);
    }


    @Override
    public File getCommissionsTemplate() throws BadRequestException{
        File file;
        try {
            file = ResourceUtils.getFile("classpath:templates/commission_upload_template.xls");
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
        return file;
    }
}
