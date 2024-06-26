package com.brokersystems.brokerapp.schedules.model;

import com.brokersystems.brokerapp.uw.model.RiskTrans;

import javax.persistence.*;

/**
 * Created by peter on 2/20/2017.
 */
@Entity
@Table(name="sys_brk_risk_scheds")
public class ScheduleTrans {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="sched_id")
    private Long scheduleId;

    @ManyToOne
    @JoinColumn(name="sched_rsk_id",nullable=false)
    private RiskTrans risk;

    @Column(name="sched_col_1")
    private String column1;

    @Column(name="sched_col_2")
    private String column2;

    @Column(name="sched_col_3")
    private String column3;

    @Column(name="sched_col_4")
    private String column4;

    @Column(name="sched_col_5")
    private String column5;

    @Column(name="sched_col_6")
    private String column6;

    @Column(name="sched_col_7")
    private String column7;

    @Column(name="sched_col_8")
    private String column8;

    @Column(name="sched_col_9")
    private String column9;

    @Column(name="sched_col_10")
    private String column10;

    @Column(name="sched_col_11")
    private String column11;

    @Column(name="sched_col_12")
    private String column12;

    @Column(name="sched_col_13")
    private String column13;

    @Column(name="sched_col_14")
    private String column14;

    @Column(name="sched_col_15")
    private String column15;

    @Column(name="sched_col_16")
    private String column16;

    @Column(name="sched_col_17")
    private String column17;

    @Column(name="sched_col_18")
    private String column18;

    @Column(name="sched_col_19")
    private String column19;

    @Column(name="sched_col_20")
    private String column20;

    @Column(name="sched_col_21")
    private String column21;

    @Column(name="sched_col_22")
    private String column22;

    @Column(name="sched_col_23")
    private String column23;

    @Column(name="sched_col_24")
    private String column24;

    @Column(name="sched_col_25")
    private String column25;

    @Column(name="sched_col_26")
    private String column26;

    @Column(name="sched_col_27")
    private String column27;

    @Column(name="sched_col_28")
    private String column28;

    @Column(name="sched_col_29")
    private String column29;

    @Column(name="sched_col_30")
    private String column30;


    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public RiskTrans getRisk() {
        return risk;
    }

    public void setRisk(RiskTrans risk) {
        this.risk = risk;
    }

    public String getColumn1() {
        return column1;
    }

    public void setColumn1(String column1) {
        this.column1 = column1;
    }

    public String getColumn2() {
        return column2;
    }

    public void setColumn2(String column2) {
        this.column2 = column2;
    }

    public String getColumn3() {
        return column3;
    }

    public void setColumn3(String column3) {
        this.column3 = column3;
    }

    public String getColumn4() {
        return column4;
    }

    public void setColumn4(String column4) {
        this.column4 = column4;
    }

    public String getColumn5() {
        return column5;
    }

    public void setColumn5(String column5) {
        this.column5 = column5;
    }

    public String getColumn6() {
        return column6;
    }

    public void setColumn6(String column6) {
        this.column6 = column6;
    }

    public String getColumn7() {
        return column7;
    }

    public void setColumn7(String column7) {
        this.column7 = column7;
    }

    public String getColumn8() {
        return column8;
    }

    public void setColumn8(String column8) {
        this.column8 = column8;
    }

    public String getColumn9() {
        return column9;
    }

    public void setColumn9(String column9) {
        this.column9 = column9;
    }

    public String getColumn10() {
        return column10;
    }

    public void setColumn10(String column10) {
        this.column10 = column10;
    }

    public String getColumn11() {
        return column11;
    }

    public void setColumn11(String column11) {
        this.column11 = column11;
    }

    public String getColumn12() {
        return column12;
    }

    public void setColumn12(String column12) {
        this.column12 = column12;
    }

    public String getColumn13() {
        return column13;
    }

    public void setColumn13(String column13) {
        this.column13 = column13;
    }

    public String getColumn14() {
        return column14;
    }

    public void setColumn14(String column14) {
        this.column14 = column14;
    }

    public String getColumn15() {
        return column15;
    }

    public void setColumn15(String column15) {
        this.column15 = column15;
    }

    public String getColumn16() {
        return column16;
    }

    public void setColumn16(String column16) {
        this.column16 = column16;
    }

    public String getColumn17() {
        return column17;
    }

    public void setColumn17(String column17) {
        this.column17 = column17;
    }

    public String getColumn18() {
        return column18;
    }

    public void setColumn18(String column18) {
        this.column18 = column18;
    }

    public String getColumn19() {
        return column19;
    }

    public void setColumn19(String column19) {
        this.column19 = column19;
    }

    public String getColumn20() {
        return column20;
    }

    public void setColumn20(String column20) {
        this.column20 = column20;
    }

    public String getColumn21() {
        return column21;
    }

    public void setColumn21(String column21) {
        this.column21 = column21;
    }

    public String getColumn22() {
        return column22;
    }

    public void setColumn22(String column22) {
        this.column22 = column22;
    }

    public String getColumn23() {
        return column23;
    }

    public void setColumn23(String column23) {
        this.column23 = column23;
    }

    public String getColumn24() {
        return column24;
    }

    public void setColumn24(String column24) {
        this.column24 = column24;
    }

    public String getColumn25() {
        return column25;
    }

    public void setColumn25(String column25) {
        this.column25 = column25;
    }

    public String getColumn26() {
        return column26;
    }

    public void setColumn26(String column26) {
        this.column26 = column26;
    }

    public String getColumn27() {
        return column27;
    }

    public void setColumn27(String column27) {
        this.column27 = column27;
    }

    public String getColumn28() {
        return column28;
    }

    public void setColumn28(String column28) {
        this.column28 = column28;
    }

    public String getColumn29() {
        return column29;
    }

    public void setColumn29(String column29) {
        this.column29 = column29;
    }

    public String getColumn30() {
        return column30;
    }

    public void setColumn30(String column30) {
        this.column30 = column30;
    }

    @Override
    public String toString() {
        return "ScheduleTrans{" +
                "scheduleId=" + scheduleId +
                ", risk=" + risk +
                ", column1='" + column1 + '\'' +
                ", column2='" + column2 + '\'' +
                ", column3='" + column3 + '\'' +
                ", column4='" + column4 + '\'' +
                ", column5='" + column5 + '\'' +
                ", column6='" + column6 + '\'' +
                ", column7='" + column7 + '\'' +
                ", column8='" + column8 + '\'' +
                ", column9='" + column9 + '\'' +
                ", column10='" + column10 + '\'' +
                ", column11='" + column11 + '\'' +
                ", column12='" + column12 + '\'' +
                ", column13='" + column13 + '\'' +
                ", column14='" + column14 + '\'' +
                ", column15='" + column15 + '\'' +
                ", column16='" + column16 + '\'' +
                ", column17='" + column17 + '\'' +
                ", column18='" + column18 + '\'' +
                ", column19='" + column19 + '\'' +
                ", column20='" + column20 + '\'' +
                ", column21='" + column21 + '\'' +
                ", column22='" + column22 + '\'' +
                ", column23='" + column23 + '\'' +
                ", column24='" + column24 + '\'' +
                ", column25='" + column25 + '\'' +
                ", column26='" + column26 + '\'' +
                ", column27='" + column27 + '\'' +
                ", column28='" + column28 + '\'' +
                ", column29='" + column29 + '\'' +
                ", column30='" + column30 + '\'' +
                '}';
    }
}
