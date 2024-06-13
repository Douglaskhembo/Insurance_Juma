package com.brokersystems.brokerapp.accounts.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class PayeesDTO {

    private Long payId;
    private String fullName;
    private String telNo;
    private String email;
    private String mobileNo;
    private String createdUser;
    private Date createdDate;
    private String status;

    private String accountNo;

    private Long bankBranchId;
}
