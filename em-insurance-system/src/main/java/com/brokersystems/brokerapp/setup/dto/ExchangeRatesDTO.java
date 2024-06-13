package com.brokersystems.brokerapp.setup.dto;

public class ExchangeRatesDTO {

    private boolean success;
    private int timestamp;
    private String base;
    private String date;
    private RatesDTO rates;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public RatesDTO getRates() {
        return rates;
    }

    public void setRates(RatesDTO rates) {
        this.rates = rates;
    }
}
