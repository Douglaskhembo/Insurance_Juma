package com.brokersystems.brokerapp.quotes.dto;

public class QuoteProspectResult {

    private String result;
    private Long prospectId;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Long getProspectId() {
        return prospectId;
    }

    public void setProspectId(Long prospectId) {
        this.prospectId = prospectId;
    }
}
