package com.brokersystems.brokerapp.server.utils;

import com.brokersystems.brokerapp.life.model.PolicyBenefitsDistribution;
import com.brokersystems.brokerapp.life.model.QPolicyBenefitsDistribution;
import com.brokersystems.brokerapp.life.repository.PolicyBenefitsDistributionRepo;
import com.brokersystems.brokerapp.server.exception.BadRequestException;
import com.brokersystems.brokerapp.uw.model.PolicyTrans;
import com.brokersystems.brokerapp.uw.repository.PolicyTransRepo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by waititu on 04/12/2017.
 */

@Component
public class LifeExcelUtils {



    @Autowired
    private PolicyTransRepo policyRepo;

    @Autowired
    private DateUtilities dateUtils;

    @Autowired
    private PolicyBenefitsDistributionRepo maturityRepo;

    public  void getPolMaturities( InputStream excelFile, Long polCode) throws IOException,BadRequestException {
        String sheetName ="MATURITY";
        //FileInputStream excelFile = new FileInputStream(new File(FILE_NAME));
        Workbook workbook = new HSSFWorkbook(excelFile);
        sheetName = sheetName.toUpperCase();
        Sheet sheet = workbook.getSheet(sheetName);
        if(sheet==null) throw new BadRequestException("Error getting estimated benefits...Error on Sheet "+sheetName);
        Iterator<Row> iterator = sheet.iterator();
       // List<MaturitiesItemsBean> data = new ArrayList<MaturitiesItemsBean>();
        List<MaturitiesBean> data = new ArrayList<>();
        double benefitAmt = 0;
        double benefitYr = 0;

        String type = "";
        while (iterator.hasNext()) {
            Row currentRow = iterator.next();
            MaturitiesBean maturities = new MaturitiesBean();
            Iterator<Cell> cellIterator = currentRow.iterator();
            while (cellIterator.hasNext()) {
                Cell currentCell = cellIterator.next();
                int columnIndex = currentCell.getColumnIndex();


                switch(columnIndex){

                    case 0:
                        if(currentCell.getCellType() == Cell.CELL_TYPE_FORMULA){
                            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                            evaluator.evaluateFormulaCell(currentCell);
                                maturities.setBenefitYear(currentCell.getNumericCellValue());
                                //benefitYr=currentCell.getNumericCellValue();


                        }
                        break;
                    case 1:
                        if(currentCell.getCellType() == Cell.CELL_TYPE_FORMULA){
                            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                            evaluator.evaluateFormulaCell(currentCell);
                                maturities.setEstBenefit(currentCell.getNumericCellValue());
                               // benefitAmt=currentCell.getNumericCellValue();


                            }
                        break;
                        }
                maturities.setPolicyId(polCode);




                }
            data.add(maturities);
            }
        Iterable<PolicyBenefitsDistribution> existingMaturitie = maturityRepo.findAll(QPolicyBenefitsDistribution.policyBenefitsDistribution.policyId.policyId.eq(polCode));
       for (PolicyBenefitsDistribution premat:existingMaturitie){
           maturityRepo.delete(premat.getMaturityId());
       }
        ArrayList<PolicyBenefitsDistribution> newMaturities = new ArrayList<>();
        PolicyTrans policy = policyRepo.findOne(polCode);
        for(MaturitiesBean bean:data){

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
