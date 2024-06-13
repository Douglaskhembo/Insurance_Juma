package com.brokersystems.brokerapp.workflow.docs;

import java.util.Arrays;
import java.util.List;

/**
 * Created by HP on 7/25/2017.
 */
public enum DocType {

    QUOTATION_DOCUMENT("Quotation Process","genQuotationProcess.bpmn20"),
    GEN_UW_DOCUMENT("General Underwrite Process","genUnderwriteProcess.bpmn20"),
    GEN_CLAIMS_DOCUMENT("General Claims Process","genClaimsProcess.bpmn20"),
    MED_CLAIMS_DOCUMENT("Medical Claims Process","medClaimsProcess.bpmn20");

    private String value;

    private String fileName;

    private DocType(String value,String fileName) {
        this.value = value;
        this.fileName = fileName;
    }


    public String getValue() {
        return value;
    }

    public String getFileName() {
        return fileName;
    }

    public static List<DocType> asList(){
        return Arrays.asList(DocType.values());
    }

}
