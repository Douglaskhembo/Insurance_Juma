package com.brokersystems.brokerapp.uw.dtos;
import lombok.Data;

import java.util.Date;

@Data
public class ClientsDto {

    private  Long tenId;
    private  String tenantNumber;
    private  String fname;
    private  String otherNames;

    public Long getTenId() {
        return tenId;
    }

    public void setTenId(Long tenId) {
        this.tenId = tenId;
    }

    public String getTenantNumber() {
        return tenantNumber;
    }

    public void setTenantNumber(String tenantNumber) {
        this.tenantNumber = tenantNumber;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getOtherNames() {
        return otherNames;
    }

    public void setOtherNames(String otherNames) {
        this.otherNames = otherNames;
    }
}