package com.brokersystems.brokerapp.uw.dtos;

import java.math.BigDecimal;

public class WIBABeneficiariesDTO {

    private Long bwbId;
    private String fullName;
    private String occupation;
    private BigDecimal salary;
    private Long riskId;

    public Long getRiskId() {
        return riskId;
    }

    public void setRiskId(Long riskId) {
        this.riskId = riskId;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public Long getBwbId() {
        return bwbId;
    }

    public void setBwbId(Long bwbId) {
        this.bwbId = bwbId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }
}
