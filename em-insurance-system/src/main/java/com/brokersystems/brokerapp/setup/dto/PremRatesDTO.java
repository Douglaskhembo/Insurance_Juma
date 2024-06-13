package com.brokersystems.brokerapp.setup.dto;

import java.util.Date;

public class PremRatesDTO {

    private Long id;
    private String rate_table;
    private Date effDate;
    private String fileName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRate_table() {
        return rate_table;
    }

    public void setRate_table(String rate_table) {
        this.rate_table = rate_table;
    }

    public Date getEffDate() {
        return effDate;
    }

    public void setEffDate(Date effDate) {
        this.effDate = effDate;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
