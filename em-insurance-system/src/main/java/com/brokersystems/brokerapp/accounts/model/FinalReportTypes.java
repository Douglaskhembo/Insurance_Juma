package com.brokersystems.brokerapp.accounts.model;

public enum FinalReportTypes {
    TB("TB","Trial Balance","Y","N",3,"N","N","N","N","N",true),
    BL("BL","Balance Sheet","Y","N",3,"N","N","N","N","N",false),
    ME("ME","Management Expenses","Y","N",3,"N","N","N","N","N",false),
    CF("CF","Cash Flow Statement","Y","N",3,"N","N","N","N","N",false),
    PL("PL","Profit and Loss","Y","N",3,"N","N","N","N","N",false),
    ;

    private String code;
    private String value;
    private String cummulative;
    private String showAllmonths;
    private Integer prevYearsToShow;
    private String showMonthlyTotal;
    private String showPrevMonthlyTotal;
    private String showBusinessClasses;
    private String showEquity;
    private String consolidated;

    private boolean tb;

    FinalReportTypes(String code, String value, String cummulative, String showAllmonths,
                     Integer prevYearsToShow, String showMonthlyTotal, String showPrevMonthlyTotal,
                     String showBusinessClasses, String showEquity, String consolidated, boolean tb) {
        this.code = code;
        this.value = value;
        this.cummulative = cummulative;
        this.showAllmonths = showAllmonths;
        this.prevYearsToShow = prevYearsToShow;
        this.showMonthlyTotal = showMonthlyTotal;
        this.showPrevMonthlyTotal = showPrevMonthlyTotal;
        this.showBusinessClasses = showBusinessClasses;
        this.showEquity = showEquity;
        this.consolidated = consolidated;
        this.tb = tb;
    }


    public boolean isTb() {
        return tb;
    }

    public void setTb(boolean tb) {
        this.tb = tb;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCummulative() {
        return cummulative;
    }

    public void setCummulative(String cummulative) {
        this.cummulative = cummulative;
    }

    public String getShowAllmonths() {
        return showAllmonths;
    }

    public void setShowAllmonths(String showAllmonths) {
        this.showAllmonths = showAllmonths;
    }

    public Integer getPrevYearsToShow() {
        return prevYearsToShow;
    }

    public void setPrevYearsToShow(Integer prevYearsToShow) {
        this.prevYearsToShow = prevYearsToShow;
    }

    public String getShowMonthlyTotal() {
        return showMonthlyTotal;
    }

    public void setShowMonthlyTotal(String showMonthlyTotal) {
        this.showMonthlyTotal = showMonthlyTotal;
    }

    public String getShowPrevMonthlyTotal() {
        return showPrevMonthlyTotal;
    }

    public void setShowPrevMonthlyTotal(String showPrevMonthlyTotal) {
        this.showPrevMonthlyTotal = showPrevMonthlyTotal;
    }

    public String getShowBusinessClasses() {
        return showBusinessClasses;
    }

    public void setShowBusinessClasses(String showBusinessClasses) {
        this.showBusinessClasses = showBusinessClasses;
    }

    public String getShowEquity() {
        return showEquity;
    }

    public void setShowEquity(String showEquity) {
        this.showEquity = showEquity;
    }

    public String getConsolidated() {
        return consolidated;
    }

    public void setConsolidated(String consolidated) {
        this.consolidated = consolidated;
    }
}
