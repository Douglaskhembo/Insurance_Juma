package com.brokersystems.brokerapp.accounts.dtos;

public class FinalReportFormatTotalDTO {

    private Long rftId;
    private Boolean sign;
    private String affsign;
    private String column;
    private String total;
    private Long totalColId;
    private Long columnColId;

    public Long getRftId() {
        return rftId;
    }

    public void setRftId(Long rftId) {
        this.rftId = rftId;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public Long getTotalColId() {
        return totalColId;
    }

    public void setTotalColId(Long totalColId) {
        this.totalColId = totalColId;
    }

    public Long getColumnColId() {
        return columnColId;
    }

    public void setColumnColId(Long columnColId) {
        this.columnColId = columnColId;
    }

    public String getAffsign() {
        return affsign;
    }
    public void setAffsign(String affsign) {
        this.affsign = affsign;
    }

    public Boolean getSign() {
        return sign;
    }

    public void setSign(Boolean sign) {
        this.sign = sign;
    }
}
