package com.brokersystems.brokerapp.accounts.model;

import com.brokersystems.brokerapp.setup.model.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="sys_brk_payees")
public class Payees {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="pay_Id")
    private Long payId;

    @Column(name="pay_full_name")
    private String fullName;

    @Column(name="pay_tel_no",length = 20)
    private String telNo;

    @Column(name="pay_email",length = 50)
    private String email;

    @Column(name="pay_mobile",length = 20)
    private String mobileNo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="pay_created_by")
    private User createdBy;

    @Column(name="pay_created_dt")
    private Date createdDate;

    @Column(name="pay_status", length = 1)
    private String status;

    public Long getPayId() {
        return payId;
    }

    public void setPayId(Long payId) {
        this.payId = payId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
