package com.brokersystems.brokerapp.quotes.dto;

public class QuoteClauseDTO {

    private Long qpClauId;
    private Long clauseId;
    private Long quoteProductId;
    private String clauHeading;
    private String clauShtDesc;
    private String clauseType;
    private String editable;
    private String clauWording;
    private String quotStatus;

    public String getQuotStatus() {
        return quotStatus;
    }

    public void setQuotStatus(String quotStatus) {
        this.quotStatus = quotStatus;
    }

    public Long getQuoteProductId() {
        return quoteProductId;
    }

    public void setQuoteProductId(Long quoteProductId) {
        this.quoteProductId = quoteProductId;
    }

    public String getClauShtDesc() {
        return clauShtDesc;
    }

    public void setClauShtDesc(String clauShtDesc) {
        this.clauShtDesc = clauShtDesc;
    }

    public Long getClauseId() {
        return clauseId;
    }

    public void setClauseId(Long clauseId) {
        this.clauseId = clauseId;
    }

    public Long getQpClauId() {
        return qpClauId;
    }

    public void setQpClauId(Long qpClauId) {
        this.qpClauId = qpClauId;
    }

    public String getClauHeading() {
        return clauHeading;
    }

    public void setClauHeading(String clauHeading) {
        this.clauHeading = clauHeading;
    }

    public String getClauseType() {
        return clauseType;
    }

    public void setClauseType(String clauseType) {
        this.clauseType = clauseType;
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
}
