
package com.brokersystems.brokerapp.server.utils;

import com.brokersystems.brokerapp.life.model.PolicyBenefitsDistribution;
import com.brokersystems.brokerapp.life.model.QPolicyBenefitsDistribution;
import com.brokersystems.brokerapp.life.repository.PolicyBenefitsDistributionRepo;
import com.brokersystems.brokerapp.quotes.model.QuotRiskLimits;
import com.brokersystems.brokerapp.quotes.repository.QuotRiskLimitsRepo;
import com.brokersystems.brokerapp.server.exception.BadRequestException;
import com.brokersystems.brokerapp.setup.model.SysLocale;
import com.brokersystems.brokerapp.uw.model.*;
import com.brokersystems.brokerapp.uw.repository.PolicyQuestionnaireRepo;
import com.brokersystems.brokerapp.uw.repository.PolicyTransRepo;
import com.brokersystems.brokerapp.uw.repository.RiskTransRepo;
import com.brokersystems.brokerapp.uw.repository.SectionTransRepo;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.eval.*;
import org.apache.poi.ss.formula.functions.Function;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.joda.time.DateTime;
import org.joda.time.Months;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by HP on 9/20/2017.
 */
@Component
public class PremExcelUtils {

    @Autowired
    private SectionTransRepo sectionRepo;

    @Autowired
    private QuotRiskLimitsRepo quotRiskLimitsRepo;

    @Autowired
    private DateUtilities dateUtils;

    @Autowired
    private PolicyBenefitsDistributionRepo maturityRepo;

    @Autowired
    private PolicyTransRepo policyRepo;

    @Autowired
    private RiskTransRepo riskTransRepo;

    @Autowired
    private PolicyQuestionnaireRepo policyQuestionnaireRepo;



    public   PremiumResultBean getPremium(List<PremiumItemsBean> premItems,final String path) throws IOException {
        Workbook workbook = new HSSFWorkbook(Files.newInputStream(Paths.get(path)));
        System.out.println(premItems);
//        FunctionEval.registerFunction("DATEDIF", new DateIfFunction());
        List<SectionTrans> sectionTrans = new ArrayList<>();
        double sumInsured = 0;
        double totalPrem = 0;
        double fullPrem = 0;
        Long polCode=null;
        for(int i=0;i<workbook.getNumberOfSheets();i++){
            Sheet sheet = workbook.getSheetAt(i);

            Optional<PremiumItemsBean> premItem = premItems.stream().filter(a -> a.getPremiumId().equals(StringUtils.trim(sheet.getSheetName()))).findAny();
            PremiumItemsBean item = new PremiumItemsBean(sheet.getSheetName(), 0, 0, 0, 0,0l,0,"SI",0, new ArrayList<>(),null);
            SectionTrans section = null;
            if(premItem.isPresent()) {
                item = premItem.get();
                section = sectionRepo.findOne(item.getSectId());
                polCode =item.getPolId();
            }
            if (polCode!=null){
                if (i==0 && policyQuestionnaireRepo.count(QPolicyQuestionnaire.policyQuestionnaire.policy.policyId.eq(polCode))>0 ) {
                    String sheetName ="QUESTIONNAIRE";
                    sheetName = sheetName.toUpperCase();
                    Sheet quizsheet = workbook.getSheet(sheetName);
                    if(quizsheet!=null && policyQuestionnaireRepo.count(QPolicyQuestionnaire.policyQuestionnaire.policy.policyId.eq(polCode))>0){
                        Iterable<PolicyQuestionnaire> questions = policyQuestionnaireRepo.findAll(QPolicyQuestionnaire.policyQuestionnaire.policy.policyId.eq(polCode));

                        // Iterator<Row> iterator = quizsheet.iterator();
                        int quizcount=1;
                        for (PolicyQuestionnaire quiz : questions) {
                            Row currentRow = quizsheet.getRow(quizcount);
                            System.out.println("currentRow="+currentRow);
                            Cell currentCell=null;
                            if (currentRow==null) {
                                currentRow = quizsheet.createRow(quizcount);
                            }
                            currentCell = currentRow.getCell(0,Row.CREATE_NULL_AS_BLANK);


                            System.out.println("quiz id="+quiz.getQuestion().getQuestionShtDesc());
                            currentCell.setCellValue(quiz.getQuestion().getQuestionShtDesc());

                            currentCell = currentRow.getCell(1,Row.CREATE_NULL_AS_BLANK);
                            System.out.println("quiz ="+quiz.getQuestion().getQuestionname());
                            currentCell.setCellValue(quiz.getQuestion().getQuestionname());

                           StringTokenizer tokenizer = new StringTokenizer(quiz.getChoice(),",");
                            int column = 2;
                            while(tokenizer.hasMoreTokens()){
                                String token  = tokenizer.nextToken();
                                currentCell = currentRow.getCell(column,Row.CREATE_NULL_AS_BLANK);
                                System.out.println("column="+column+";token ="+token);
                                currentCell.setCellValue(token);
                                column++;
                            }

                            quizcount++;
                        }
                    }
                }
            }
            Iterator<Row> iterator = sheet.iterator();
            int count=0;
            while (iterator.hasNext()) {
                if(count==2)
                    break;
                Row currentRow = iterator.next();
                Iterator<Cell> cellIterator = currentRow.iterator();
                if(count==0) {
                    count++;
                    continue;
                }
                while (cellIterator.hasNext()) {
                    Cell currentCell = cellIterator.next();
                    int columnIndex = currentCell.getColumnIndex();
                    switch (columnIndex) {
                        case 1:
                            if (currentCell.getCellType() == Cell.CELL_TYPE_NUMERIC ||currentCell.getCellType() == Cell.CELL_TYPE_STRING) {
                                if(currentCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                    if ("SI".equalsIgnoreCase(item.getSectType())) {
                                        sumInsured += item.getValue();
                                    }
                                    currentCell.setCellValue(item.getValue());//Setting Value
                                }
                                else{
                                    if ("SI".equalsIgnoreCase(item.getSectType())) {
                                        sumInsured += item.getValue();
                                    }
                                    currentCell.setCellValue((Double)item.getValue());//Setting Value;
                                }

                            }
                            break;

                        case 2:
                            if (currentCell.getCellType() == Cell.CELL_TYPE_NUMERIC ||currentCell.getCellType() == Cell.CELL_TYPE_STRING ) {
                                if(currentCell.getCellType() == Cell.CELL_TYPE_NUMERIC)
                                    currentCell.setCellValue(item.getRate());
                                else{
                                    currentCell.setCellValue( (Double)item.getRate());
                                    //Setting Rate
                                }


                            }
                            break;

                        case 3:
                            if (currentCell.getCellType() == Cell.CELL_TYPE_NUMERIC ||currentCell.getCellType() == Cell.CELL_TYPE_STRING ) {
                                currentCell.setCellValue((item.getDivFactor() <= 0) ? 1 : item.getDivFactor()); //Setting Division Factor
                            }
                            break;

                        case 4:
                            if (currentCell.getCellType() == Cell.CELL_TYPE_NUMERIC||currentCell.getCellType() == Cell.CELL_TYPE_STRING) {
                                currentCell.setCellValue(item.getFreeLimit()); //Setting Free Limit

                            }
                            break;
                        case 6:
                            if (currentCell.getCellType() == Cell.CELL_TYPE_NUMERIC||currentCell.getCellType() == Cell.CELL_TYPE_STRING) {
                                currentCell.setCellValue(item.getMinPrem()); //Setting Minimum Premium

                            }
                            break;
                        case 8:
                            if (currentCell.getCellType() == Cell.CELL_TYPE_NUMERIC||currentCell.getCellType() == Cell.CELL_TYPE_STRING) {
                                currentCell.setCellValue(item.getAge()); //Setting Minimum Premium

                            }
                            break;
                        case 9:
                            if (currentCell.getCellType() == Cell.CELL_TYPE_NUMERIC||currentCell.getCellType() == Cell.CELL_TYPE_STRING) {

                                if(section!=null) {
                                    System.out.println(section);
                                    currentCell.setCellValue(riskTransRepo.count(QRiskTrans.riskTrans.policy.policyId.eq(section.getRisk().getPolicy().getPolicyId()))); //Setting Minimum Premium
                                }
                                if(section!=null)
                                    currentCell.setCellValue(riskTransRepo.count(QRiskTrans.riskTrans.policy.policyId.eq(section.getRisk().getPolicy().getPolicyId()))); //Setting Minimum Premium

                            }
                            break;
                        case 10:

                            if (currentCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                if (item.getScheduleItems().size() > 0) {
                                    currentCell.setCellValue(Double.valueOf(item.getScheduleItems().get(0)));
                                } else {
                                    currentCell.setCellValue(0);
                                }
                            }
                            else if (currentCell.getCellType() == Cell.CELL_TYPE_STRING) {
                                if (item.getScheduleItems().size() > 0) {
                                    currentCell.setCellValue(item.getScheduleItems().get(0));
                                } else {
                                    currentCell.setCellValue("");
                                }
                            }
                            break;
                        case 11:
                            if (currentCell.getCellType() == Cell.CELL_TYPE_STRING) {
                                if (item.getScheduleItems().size() > 1) {
                                    currentCell.setCellValue(item.getScheduleItems().get(1));
                                } else {
                                    currentCell.setCellValue("");
                                }
                            }
                            else   if (currentCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                if (item.getScheduleItems().size() > 1) {
                                    currentCell.setCellValue(Double.valueOf(item.getScheduleItems().get(1)));
                                } else {
                                    currentCell.setCellValue(0);
                                }
                            }
                            break;
                        case 12:
                            if (currentCell.getCellType() == Cell.CELL_TYPE_STRING) {
                                if (item.getScheduleItems().size() > 2) {
                                    currentCell.setCellValue(item.getScheduleItems().get(2));
                                } else {
                                    currentCell.setCellValue("");
                                }
                            }
                            else   if (currentCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                if (item.getScheduleItems().size() > 2) {
                                    currentCell.setCellValue(Double.valueOf(item.getScheduleItems().get(2)));
                                } else {
                                    currentCell.setCellValue(0);
                                }
                            }
                            break;
                        case 13:
                            if (currentCell.getCellType() == Cell.CELL_TYPE_STRING) {
                                if (item.getScheduleItems().size() > 3) {
                                    currentCell.setCellValue(item.getScheduleItems().get(3));
                                } else {
                                    currentCell.setCellValue("");
                                }
                            }
                            else   if (currentCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                if (item.getScheduleItems().size() > 3) {
                                    currentCell.setCellValue(Double.valueOf(item.getScheduleItems().get(3)));
                                } else {
                                    currentCell.setCellValue(0);
                                }
                            }
                            break;
                        case 14:
                            if (currentCell.getCellType() == Cell.CELL_TYPE_STRING) {
                                if (item.getScheduleItems().size() > 4) {
                                    currentCell.setCellValue(item.getScheduleItems().get(4));
                                } else {
                                    currentCell.setCellValue("");
                                }
                            }
                            else   if (currentCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                if (item.getScheduleItems().size() > 4) {
                                    currentCell.setCellValue(Double.valueOf(item.getScheduleItems().get(4)));
                                } else {
                                    currentCell.setCellValue(0);
                                }
                            }
                            break;
                        case 15:
                            if (currentCell.getCellType() == Cell.CELL_TYPE_STRING) {
                                if (item.getScheduleItems().size() > 5) {
                                    currentCell.setCellValue(item.getScheduleItems().get(5));
                                } else {
                                    currentCell.setCellValue("");
                                }
                            }
                            else   if (currentCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                if (item.getScheduleItems().size() > 5) {
                                    currentCell.setCellValue(Double.valueOf(item.getScheduleItems().get(5)));
                                } else {
                                    currentCell.setCellValue(0);
                                }
                            }
                            break;
                        default:
                    }

                }
                count++;
            }

            Iterator<Row> resultiterator = sheet.iterator();
            int counter = 0;
            while (resultiterator.hasNext()) {
                Row currentRow = resultiterator.next();
                Iterator<Cell> cellIterator = currentRow.iterator();
                while (cellIterator.hasNext()) {
                    Cell currentCell = cellIterator.next();
                    int columnIndex = currentCell.getColumnIndex();
                    if(columnIndex== 7) {
                        if (currentCell.getCellType() == Cell.CELL_TYPE_FORMULA) {
                            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                            evaluator.evaluateFormulaCell(currentCell);
                            CellValue cellValue = evaluator.evaluate(currentCell);
                            if (cellValue.getNumberValue() != 0) {
                                if (section != null) {
                                    if(section.getPremRates().getProratedFull()!=null && section.getPremRates().getProratedFull().equalsIgnoreCase("P")) {
                                        section.setPrem(BigDecimal.valueOf(cellValue.getNumberValue()));
                                        totalPrem += cellValue.getNumberValue();
                                    }else{
                                        section.setPrem(BigDecimal.valueOf(cellValue.getNumberValue()));
                                        fullPrem += cellValue.getNumberValue();
                                    }
                                }
                            }
                        }
                    }
                    if(columnIndex== 5) {
                        if (currentCell.getCellType() == Cell.CELL_TYPE_FORMULA) {
                            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                            evaluator.evaluateFormulaCell(currentCell);

                            if (currentCell.getNumericCellValue() != 0) {
                                if (section != null) {
                                    section.setCalcprem(BigDecimal.valueOf(currentCell.getNumericCellValue()));
                                }
                            }
                        }
                    }
                }
                counter++;
            }

            if(section!=null)
                sectionTrans.add(section);
        }

        sectionRepo.save(sectionTrans);
        return new PremiumResultBean(totalPrem,fullPrem,sumInsured);
    }

    public   PremiumResultBean getLifePremium(List<PremiumItemsBean> premItems,final String path) throws IOException {
        Workbook workbook = new HSSFWorkbook(Files.newInputStream(Paths.get(path)));
        List<SectionTrans> sectionTrans = new ArrayList<>();
        Long polCode=null;
        double sumInsured = 0;
        double totalPrem = 0;
        for(int i=0;i<workbook.getNumberOfSheets();i++){
            Sheet sheet = workbook.getSheetAt(i);
            Optional<PremiumItemsBean> premItem = premItems.stream().filter(a ->{
                return a.getPremiumId().equals(StringUtils.trim(sheet.getSheetName()));
            }).findAny();
            PremiumItemsBean item = new PremiumItemsBean(sheet.getSheetName(), 0, 0, 0, 0,0l,0,"SI",0, new ArrayList<>(),null);
            SectionTrans section = null;
            boolean present = false;
            if(premItem.isPresent()) {
                item = premItem.get();
                section = sectionRepo.findOne(item.getSectId());
                System.out.println(section.getSection().getDesc());
                present =true;
                polCode =item.getPolId();
            }

            if(present) {
                if (i==0 && policyQuestionnaireRepo.count(QPolicyQuestionnaire.policyQuestionnaire.policy.policyId.eq(polCode))>0 ) {
                    String sheetName ="QUESTIONNAIRE";
                    sheetName = sheetName.toUpperCase();
                    Sheet quizsheet = workbook.getSheet(sheetName);
                    if(quizsheet!=null && policyQuestionnaireRepo.count(QPolicyQuestionnaire.policyQuestionnaire.policy.policyId.eq(polCode))>0){
                        Iterable<PolicyQuestionnaire> questions = policyQuestionnaireRepo.findAll(QPolicyQuestionnaire.policyQuestionnaire.policy.policyId.eq(polCode));

                        // Iterator<Row> iterator = quizsheet.iterator();
                        int quizcount=1;
                        for (PolicyQuestionnaire quiz : questions) {
                            Row currentRow = quizsheet.getRow(quizcount);
                            System.out.println("currentRow="+currentRow);
                            Cell currentCell=null;
                            if (currentRow==null) {
                                currentRow = quizsheet.createRow(quizcount);
                            }
                            currentCell = currentRow.getCell(0,Row.CREATE_NULL_AS_BLANK);


                            System.out.println("quiz id="+quiz.getQuestion().getQuestionShtDesc());
                            currentCell.setCellValue(quiz.getQuestion().getQuestionShtDesc());

                            currentCell = currentRow.getCell(1,Row.CREATE_NULL_AS_BLANK);
                            System.out.println("quiz ="+quiz.getQuestion().getQuestionname());
                            currentCell.setCellValue(quiz.getQuestion().getQuestionname());

                            StringTokenizer tokenizer = new StringTokenizer(quiz.getChoice(),",");
                            int column = 2;
                            while(tokenizer.hasMoreTokens()){
                                String token  = tokenizer.nextToken();
                                currentCell = currentRow.getCell(column,Row.CREATE_NULL_AS_BLANK);
                                System.out.println("column="+column+";token ="+token);
                                currentCell.setCellValue(token);
                                column++;
                            }
//                            currentCell = currentRow.getCell(2,Row.CREATE_NULL_AS_BLANK);
//                            System.out.println("quiz ="+quiz.getChoice());
//                            currentCell.setCellValue(quiz.getChoice());
                            quizcount++;
                        }
                    }
                }

                Iterator<Row> iterator = sheet.iterator();
                int count=0;
                while (iterator.hasNext()) {
                    if(count==2)
                        break;
                    Row currentRow = iterator.next();
                    Iterator<Cell> cellIterator = currentRow.iterator();
                    if(count==0) {
                        count++;
                        continue;
                    }
                    while (cellIterator.hasNext()) {
                        Cell currentCell = cellIterator.next();

                        int columnIndex = currentCell.getColumnIndex();
                        switch (columnIndex) {

                            case 1:
                                currentCell.setCellType(Cell.CELL_TYPE_NUMERIC);
                                System.out.println( "column =" + columnIndex+";"+currentCell.getCellType()+";"+(currentCell.getCellType() == Cell.CELL_TYPE_NUMERIC ));
                                if (currentCell.getCellType() == Cell.CELL_TYPE_NUMERIC  ) {
                                    System.out.println("here two "+item.getPremium()+" ...");
                                    //evaluator.clearAllCachedResultValues();
                                    //CellReference cellReference = new CellReference("L2");
                                    //Row row = sheet.getRow(cellReference.getRow());
                                    // Cell formulacell = row.getCell(cellReference.getCol());
                                    if (item.getPremium() != null && item.getPremium().compareTo(BigDecimal.ZERO) > 0) {
                                        // System.out.println("here 3="+item.getPremium().intValue());

                                        currentCell.setCellValue(item.getPremium().doubleValue());//Setting Value

//                                        System.out.println("here 4");
//                                        System.out.println("Sheet name= " + currentCell.getSheet().getSheetName());
                                    }
                                    else {
                                        currentCell.setCellValue(0);
                                    }
//                                    evaluator.notifySetFormula(formulacell);
//                                    evaluator.notifyUpdateCell(currentCell);
//                                    evaluator.notifyUpdateCell(formulacell);

                                }
                                break;
                            case 2:
                                currentCell.setCellType(Cell.CELL_TYPE_NUMERIC);
                                System.out.println( "column =" + columnIndex+";"+currentCell.getCellType()+";"+(currentCell.getCellType() == Cell.CELL_TYPE_NUMERIC ));
                                if (currentCell.getCellType() == Cell.CELL_TYPE_NUMERIC  ) {
                                    System.out.println("here three "+item.getRate()+" ...");
                                    //evaluator.clearAllCachedResultValues();
                                    //CellReference cellReference = new CellReference("L2");
                                    //Row row = sheet.getRow(cellReference.getRow());
                                    // Cell formulacell = row.getCell(cellReference.getCol());

                                        // System.out.println("here 3="+item.getPremium().intValue());

                                        currentCell.setCellValue(item.getRate());//Setting Value

//                                        System.out.println("here 4");
//                                        System.out.println("Sheet name= " + currentCell.getSheet().getSheetName());

//                                    evaluator.notifySetFormula(formulacell);
//                                    evaluator.notifyUpdateCell(currentCell);
//                                    evaluator.notifyUpdateCell(formulacell);

                                }
                                break;
                            case 3:
                                currentCell.setCellType(Cell.CELL_TYPE_NUMERIC);
                                if (currentCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
//                                    evaluator.clearAllCachedResultValues();
//                                    CellReference cellReference = new CellReference("L2");
//                                    Row row = sheet.getRow(cellReference.getRow());
//                                    Cell formulacell = row.getCell(cellReference.getCol());
                                    currentCell.setCellValue(item.getFreeLimit()); //Setting Free Limit

//                                    evaluator.notifySetFormula(formulacell);
//                                    evaluator.notifyUpdateCell(currentCell);
//                                    evaluator.notifyUpdateCell(formulacell);
                                }
                                break;
//                            case 5:
//                                currentCell.setCellType(Cell.CELL_TYPE_NUMERIC);
//                                if (currentCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
////                                    evaluator.clearAllCachedResultValues();
////                                    CellReference cellReference = new CellReference("L2");
////                                    Row row = sheet.getRow(cellReference.getRow());
////                                    Cell formulacell = row.getCell(cellReference.getCol());
//                                    currentCell.setCellValue(item.getMinPrem()); //Setting Minimum Premium
//
////                                    evaluator.notifySetFormula(formulacell);
////                                    evaluator.notifyUpdateCell(currentCell);
////                                    evaluator.notifyUpdateCell(formulacell);
//
//                                }
//                                break;
                            case 8:
                                currentCell.setCellType(Cell.CELL_TYPE_NUMERIC);
                                if (currentCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
//                                    evaluator.clearAllCachedResultValues();
//                                    CellReference cellReference = new CellReference("L2");
//                                    Row row = sheet.getRow(cellReference.getRow());
//                                    Cell formulacell = row.getCell(cellReference.getCol());
//                                    if (item.isMainSection())
                                    System.out.println("Sum assured..."+item.getSumAssured()+" Main Section.."+item.isMainSection());
                                    if (item.isMainSection()) {
                                        System.out.println("Sum assured..."+item.getSumAssured());
                                        if (item.getSumAssured() != null && item.getSumAssured().compareTo(BigDecimal.ZERO) > 0) {
                                            currentCell.setCellValue(item.getSumAssured().doubleValue()); //Setting Age
                                            sumInsured =sumInsured + item.getSumAssured().doubleValue();
                                        }
                                    }
//                                    evaluator.notifySetFormula(formulacell);
//                                    evaluator.notifyUpdateCell(currentCell);
//                                    evaluator.notifyUpdateCell(formulacell);
                                }
                                break;
                            case 9:
                                currentCell.setCellType(Cell.CELL_TYPE_NUMERIC);
                                if (currentCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {

//                                    evaluator.clearAllCachedResultValues();
//                                    CellReference cellReference = new CellReference("L2");
//                                    Row row = sheet.getRow(cellReference.getRow());
//                                    Cell formulacell = row.getCell(cellReference.getCol());
                                    if (item.isMainSection())
                                            currentCell.setCellValue(item.getTerm());

//                                    evaluator.notifySetFormula(formulacell);
//                                    evaluator.notifyUpdateCell(currentCell);
//                                    evaluator.notifyUpdateCell(formulacell);
                                }
                                break;
                            case 10:
                                currentCell.setCellType(Cell.CELL_TYPE_NUMERIC);
                                if (currentCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {

//                                    evaluator.clearAllCachedResultValues();
//                                    CellReference cellReference = new CellReference("L2");
//                                    Row row = sheet.getRow(cellReference.getRow());
//                                    Cell formulacell = row.getCell(cellReference.getCol());
                                    if (item.isMainSection())
                                        currentCell.setCellValue(item.getTerm());//Setting Value

//                                    evaluator.notifySetFormula(formulacell);
//                                    evaluator.notifyUpdateCell(currentCell);
//                                    evaluator.notifyUpdateCell(formulacell);
                                }
                                break;
                            case 11:
                                if (currentCell.getCellType() == Cell.CELL_TYPE_STRING) {

//                                    evaluator.clearAllCachedResultValues();
//                                    CellReference cellReference = new CellReference("L2");
//                                    Row row = sheet.getRow(cellReference.getRow());
//                                    Cell formulacell = row.getCell(cellReference.getCol());
                                    if (item.isMainSection())
                                        currentCell.setCellValue(item.getRate());//Setting Value
//                                    evaluator.notifySetFormula(formulacell);
//                                    evaluator.notifyUpdateCell(currentCell);
//                                    evaluator.notifyUpdateCell(formulacell);
                                }
                                break;
                            default:
                        }

                    }
                    count++;
                }

                Iterator<Row> resultiterator = sheet.iterator();
                count=0;
                while (resultiterator.hasNext()) {
                    if(count==2)
                        break;
                    Row currentRow = resultiterator.next();
                    Iterator<Cell> cellIterator = currentRow.iterator();
                    if(count==0) {
                        count++;
                        continue;
                    }

                    while (cellIterator.hasNext()) {
                        Cell currentCell = cellIterator.next();
                        int columnIndex = currentCell.getColumnIndex();
                        if (columnIndex == 6) {

                            if (currentCell.getCellType() == Cell.CELL_TYPE_FORMULA) {
                                FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                                evaluator.clearAllCachedResultValues();
                                workbook.setForceFormulaRecalculation(true);

                                CellValue cellValue = evaluator.evaluate(currentCell);
                                if (cellValue.getNumberValue() != 0) {
                                    if (section != null) {
                                        section.setPrem(BigDecimal.valueOf(cellValue.getNumberValue()));
                                        section.setCalcprem(BigDecimal.valueOf(currentCell.getNumericCellValue()));
                                        System.out.println("Actual Premium " + cellValue.getNumberValue());
                                        totalPrem += cellValue.getNumberValue();
                                    }
                                }
                            }
                        }
//                        if (columnIndex == 4) {
//                            if (currentCell.getCellType() == Cell.CELL_TYPE_FORMULA) {
//                                FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
//                                evaluator.clearAllCachedResultValues();
//                                evaluator.notifySetFormula(currentCell);
//                                evaluator.evaluateFormulaCell(currentCell);
//                                evaluator.evaluateInCell(currentCell);
////                                System.out.println(currentCell.getCellFormula());
//                                if (currentCell.getNumericCellValue() != 0) {
//                                    if (section != null) {
//                                        System.out.println("Calc Premium " + currentCell.getNumericCellValue());
//                                        section.setCalcprem(BigDecimal.valueOf(currentCell.getNumericCellValue()));
//                                    }
//                                }
//                            }
//                        }
                        if (columnIndex == 5) {
                            if (currentCell.getCellType() == Cell.CELL_TYPE_FORMULA) {
                                FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                                evaluator.clearAllCachedResultValues();
                                evaluator.notifySetFormula(currentCell);
                                evaluator.evaluateFormulaCell(currentCell);
                                System.out.println("Entering here...");
                                if (currentCell.getNumericCellValue() != 0) {
                                    if (section != null) {
                                        sumInsured += currentCell.getNumericCellValue();
                                        System.out.println("Sum Assured..."+sumInsured);
                                       // section.setAmount(BigDecimal.valueOf(sumInsured));
                                    }
                                    System.out.println("Sheet " + sheet.getSheetName() + "   Formula");
                                    System.out.println(currentCell.getCellFormula() + " Value..." + currentCell.getNumericCellValue());
                                }
                            }
                        }
//                        if (item.isMainSection()) {
//                            if (columnIndex == 5) {
//                                if (currentCell.getCellType() == Cell.CELL_TYPE_FORMULA) {
//                                    FormulaEvaluator evaluator = sheet.getWorkbook().getCreationHelper().createFormulaEvaluator();
//                                    evaluator.clearAllCachedResultValues();
//                                    try {
//                                        evaluator.notifySetFormula(currentCell);
//                                        evaluator.evaluateFormulaCell(currentCell);
//                                    } catch (Exception e) {
//                                        System.out.println("Error occured in 'EvaluateFormulas' : " + e);
//                                    }
//                                    //FormulaError.forInt(currentCell.getErrorCellValue());
//
//
//                                    System.out.println(" COLUMN 11 CELL FORMULA= " + currentCell.getCellFormula());
//                                    System.out.println("COLUMN 11  CELL VALUE= " + currentCell.getNumericCellValue());
//                                    if (currentCell.getNumericCellValue() != 0) {
//                                        if (section != null) {
//                                            sumInsured += currentCell.getNumericCellValue();
//                                        }
//                                    }
//                                }
//                            }
//                        }
                    }
                    count++;
                }

                if(section!=null)
                    sectionTrans.add(section);
            }


        }

        sectionRepo.save(sectionTrans);
        if (polCode!=null) {
            String sheetName ="MATURITY";
            //FileInputStream excelFile = new FileInputStream(new File(FILE_NAME));
            sheetName = sheetName.toUpperCase();
            Sheet sheet = workbook.getSheet(sheetName);
            int count=0;
            if(sheet!=null){
                Iterator<Row> iterator = sheet.iterator();
                // List<MaturitiesItemsBean> data = new ArrayList<MaturitiesItemsBean>();
                List<MaturitiesBean> data = new ArrayList<>();
                double benefitAmt = 0;
                double benefitYr = 0;

                String type = "";
                boolean exec = true;
                while (iterator.hasNext() && exec) {
                    Row currentRow = iterator.next();
                    MaturitiesBean maturities = new MaturitiesBean();
                    Iterator<Cell> cellIterator = currentRow.iterator();
                    if(count==0) {
                        count++;
                        continue;
                    }
                    System.out.println("Maturities Iterator");
                    while (cellIterator.hasNext()) {

                        Cell currentCell = cellIterator.next();
                        int columnIndex = currentCell.getColumnIndex();
                        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                        System.out.println("Maturities columnIndex=" + columnIndex);
                        switch (columnIndex) {

                            case 0:
                                if (currentCell.getCellType() == Cell.CELL_TYPE_FORMULA) {
                                    evaluator.evaluateFormulaCell(currentCell);
//                                    if (currentCell.getNumericCellValue() == -1) {
//                                        break;
//                                    }
                                    if (evaluator.evaluate(currentCell).getCellType() == Cell.CELL_TYPE_STRING) {
                                        exec = false;
                                        break;
                                    }
                                    maturities.setBenefitYear(currentCell.getNumericCellValue());
//benefitYr=currentCell.getNumericCellValue();


                                } else {
//                                    if (currentCell.getNumericCellValue() == -1) {
////                                        break;
////                                    }
                                    if (evaluator.evaluate(currentCell).getCellType() == Cell.CELL_TYPE_STRING) {
                                        exec = false;
                                        break;
                                    }
                                    if (currentCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                        maturities.setBenefitYear(currentCell.getNumericCellValue());
                                    }
                                }
                                break;
                            case 1:
                                if (currentCell.getCellType() == Cell.CELL_TYPE_FORMULA) {
                                    evaluator.evaluateFormulaCell(currentCell);
                                    if (evaluator.evaluate(currentCell).getCellType() == Cell.CELL_TYPE_STRING) {
                                        exec = false;
                                        break;
                                    }
                                    maturities.setEstBenefit(currentCell.getNumericCellValue());
// benefitAmt=currentCell.getNumericCellValue();
                                } else {
//                                    if (currentCell.getNumericCellValue() == -1) {
////                                        break;
////                                    }
                                    if (evaluator.evaluate(currentCell).getCellType() == Cell.CELL_TYPE_STRING) {
                                        exec = false;
                                        break;
                                    }
                                    if (currentCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                        maturities.setEstBenefit(currentCell.getNumericCellValue());

                                    }
                                }
                                break;
                        }
                        maturities.setPolicyId(polCode);

                    }
                    data.add(maturities);
                    count++;
                }
                System.out.println("maturities="+data.toString());
                ArrayList<PolicyBenefitsDistribution> newMaturities = new ArrayList<>();
                PolicyTrans policy = policyRepo.findOne(polCode);
                System.out.println(polCode+"=polcode = "+policy.toString());
                for(MaturitiesBean bean:data){
                    System.out.println(bean.getEstBenefit()+"=benefits = "+bean.getBenefitYear());
                    PolicyBenefitsDistribution newMat = new PolicyBenefitsDistribution();
                    if(bean.getEstBenefit()!=0 || bean.getBenefitYear()!=0) {
                        int yr = (int) bean.getBenefitYear();
                        newMat.setEstBenefit(bean.getEstBenefit());
                        newMat.setMaturityYear(yr);
                        newMat.setPolicyId(policy);
                        newMat.setMaturityExpDate(dateUtils.getMaturityDate(policy.getWefDate(), yr));
                        newMaturities.add(newMat);
                    }
                }
                maturityRepo.save(newMaturities);

            }
        }

        return new PremiumResultBean(sumInsured,totalPrem);
    }

    public   PremiumResultBean getQuotPremium(List<PremiumItemsBean> premItems,final String path) throws IOException {
        Workbook workbook = new HSSFWorkbook(Files.newInputStream(Paths.get(path)));
        List<QuotRiskLimits> sectionTrans = new ArrayList<>();
        double sumInsured = 0;
        double totalPrem = 0;
        //System.out.println(premItems);
        for(int i=0;i<workbook.getNumberOfSheets();i++){
            Sheet sheet = workbook.getSheetAt(i);
            Optional<PremiumItemsBean> premItem = premItems.stream().filter(a -> a.getPremiumId().equals(StringUtils.trim(sheet.getSheetName()))).findAny();
            PremiumItemsBean item = new PremiumItemsBean(sheet.getSheetName(), 0, 0, 0, 0,0l,0,"SI",0, new ArrayList<>(),null);
            QuotRiskLimits section = null;
            boolean present = false;
            if(premItem.isPresent()) {
                item = premItem.get();
                section = quotRiskLimitsRepo.findOne(item.getSectId());
                present =true;
            }
            // System.out.println(section);
            if(present) {
                Iterator<Row> iterator = sheet.iterator();
                while (iterator.hasNext()) {
                    Row currentRow = iterator.next();
                    Iterator<Cell> cellIterator = currentRow.iterator();
                    while (cellIterator.hasNext()) {
                        Cell currentCell = cellIterator.next();
                        int columnIndex = currentCell.getColumnIndex();
                        switch (columnIndex) {
                            case 1:
                                if (currentCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                    if ("SI".equalsIgnoreCase(item.getSectType())) {
                                        sumInsured += item.getValue();
                                    }
                                    currentCell.setCellValue(item.getValue());//Setting Value

                                }
                                break;

                            case 2:
                                if (currentCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                    currentCell.setCellValue(item.getRate()); //Setting Rate

                                }
                                break;

                            case 3:
                                if (currentCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                    currentCell.setCellValue((item.getDivFactor() <= 0) ? 1 : item.getDivFactor()); //Setting Division Factor

                                }
                                break;

                            case 4:
                                if (currentCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                    currentCell.setCellValue(item.getFreeLimit()); //Setting Free Limit


                                }
                                break;
                            case 6:
                                if (currentCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                    currentCell.setCellValue(item.getMinPrem()); //Setting Minimum Premium


                                }
                                break;
                            case 8:
                                if (currentCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                    if (item.getScheduleItems().size() > 0) {
                                        currentCell.setCellValue(Double.valueOf(item.getScheduleItems().get(0)));
                                    } else {
                                        currentCell.setCellValue(0);
                                    }
                                } else if (currentCell.getCellType() == Cell.CELL_TYPE_STRING) {
                                    if (item.getScheduleItems().size() > 0) {
                                        currentCell.setCellValue(item.getScheduleItems().get(0));
                                    } else {
                                        currentCell.setCellValue("");
                                    }
                                }
                                break;
                            case 9:
                                if (currentCell.getCellType() == Cell.CELL_TYPE_STRING) {
                                    if (item.getScheduleItems().size() > 1) {
                                        currentCell.setCellValue(item.getScheduleItems().get(1));
                                    } else {
                                        currentCell.setCellValue("");
                                    }
                                } else if (currentCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                    if (item.getScheduleItems().size() > 1) {
                                        currentCell.setCellValue(Double.valueOf(item.getScheduleItems().get(1)));
                                    } else {
                                        currentCell.setCellValue(0);
                                    }
                                }
                                break;
                            case 10:
                                if (currentCell.getCellType() == Cell.CELL_TYPE_STRING) {
                                    if (item.getScheduleItems().size() > 2) {
                                        currentCell.setCellValue(item.getScheduleItems().get(2));
                                    } else {
                                        currentCell.setCellValue("");
                                    }
                                } else if (currentCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                    if (item.getScheduleItems().size() > 2) {
                                        currentCell.setCellValue(Double.valueOf(item.getScheduleItems().get(2)));
                                    } else {
                                        currentCell.setCellValue(0);
                                    }
                                }
                                break;
                            case 11:
                                if (currentCell.getCellType() == Cell.CELL_TYPE_STRING) {
                                    if (item.getScheduleItems().size() > 3) {
                                        currentCell.setCellValue(item.getScheduleItems().get(3));
                                    } else {
                                        currentCell.setCellValue("");
                                    }
                                } else if (currentCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                    if (item.getScheduleItems().size() > 3) {
                                        currentCell.setCellValue(Double.valueOf(item.getScheduleItems().get(3)));
                                    } else {
                                        currentCell.setCellValue(0);
                                    }
                                }
                                break;
                            case 12:
                                if (currentCell.getCellType() == Cell.CELL_TYPE_STRING) {
                                    if (item.getScheduleItems().size() > 4) {
                                        currentCell.setCellValue(item.getScheduleItems().get(4));
                                    } else {
                                        currentCell.setCellValue("");
                                    }
                                } else if (currentCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                    if (item.getScheduleItems().size() > 4) {
                                        currentCell.setCellValue(Double.valueOf(item.getScheduleItems().get(4)));
                                    } else {
                                        currentCell.setCellValue(0);
                                    }
                                }
                                break;
                            default:
                        }

                    }
                }

                Iterator<Row> resultiterator = sheet.iterator();
                while (resultiterator.hasNext()) {
                    Row currentRow = resultiterator.next();
                    Iterator<Cell> cellIterator = currentRow.iterator();
                    while (cellIterator.hasNext()) {
                        Cell currentCell = cellIterator.next();
                        int columnIndex = currentCell.getColumnIndex();
                        if (columnIndex == 7) {
                            if (currentCell.getCellType() == Cell.CELL_TYPE_FORMULA) {
                                FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                                CellValue cellValue = evaluator.evaluate(currentCell);
                                if (cellValue.getNumberValue() != 0) {
                                    if (section != null) {
                                        section.setPrem(BigDecimal.valueOf(cellValue.getNumberValue()));
                                        totalPrem += cellValue.getNumberValue();
                                    }
                                }
                            }
                        }
                        if (columnIndex == 5) {
                            if (currentCell.getCellType() == Cell.CELL_TYPE_FORMULA) {
                                FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                                evaluator.evaluateFormulaCell(currentCell);
                                if (currentCell.getNumericCellValue() != 0) {
                                    if (section.getSectId() == 477) {
                                        System.out.println(" prem..." + currentCell.getNumericCellValue());
                                    }
                                    if (section != null) {
                                        section.setCalcprem(BigDecimal.valueOf(currentCell.getNumericCellValue()));
                                    }
                                }
                            }
                        }
                    }
                }

                if (section != null)
                    sectionTrans.add(section);
            }
        }

        quotRiskLimitsRepo.save(sectionTrans);
        return new PremiumResultBean(sumInsured,totalPrem);
    }
}
