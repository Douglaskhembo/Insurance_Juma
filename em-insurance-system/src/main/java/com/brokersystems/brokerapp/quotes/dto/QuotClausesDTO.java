package com.brokersystems.brokerapp.quotes.dto;

public class QuotClausesDTO {

    private Long subClauseId;
    private String header;


    public Long getSubClauseId() {
        return subClauseId;
    }

    public void setSubClauseId(Long subClauseId) {
        this.subClauseId = subClauseId;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
}
