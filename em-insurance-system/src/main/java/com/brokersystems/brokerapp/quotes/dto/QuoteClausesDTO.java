package com.brokersystems.brokerapp.quotes.dto;

public class QuoteClausesDTO {

    private Long qpClauId;
    private Long quotProdCode;
    private String clauHeading;
    private String editable;
    private String clauWording;
    private Long subclauseId;

    public Long getQpClauId() {
        return qpClauId;
    }

    public void setQpClauId(Long qpClauId) {
        this.qpClauId = qpClauId;
    }

    public Long getQuotProdCode() {
        return quotProdCode;
    }

    public void setQuotProdCode(Long quotProdCode) {
        this.quotProdCode = quotProdCode;
    }

    public String getClauHeading() {
        return clauHeading;
    }

    public void setClauHeading(String clauHeading) {
        this.clauHeading = clauHeading;
    }

    public String getEditable() {
        return editable;
    }

    public void setEditable(String editable) {
        this.editable = editable;
    }

    public String getClauWording() {
        return clauWording;
    }

    public void setClauWording(String clauWording) {
        this.clauWording = clauWording;
    }

    public Long getSubclauseId() {
        return subclauseId;
    }

    public void setSubclauseId(Long subclauseId) {
        this.subclauseId = subclauseId;
    }
}
