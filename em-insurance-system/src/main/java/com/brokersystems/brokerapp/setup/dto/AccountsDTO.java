package com.brokersystems.brokerapp.setup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountsDTO {
    private Long acctId;
    private Long acctTypeId;
    private String name;
    private String shtDesc;
    private String address;
    private String physaddress;
    private String contactTitle;
    private String contactPerson;
    private String pinNo;
    private String licenseNumber;
    private String email;
    private String phoneNo;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date dob;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date wef;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date wet;
    private String status;
    private String bankAccount;
    private String paybillNumber;
    private String payTelNo;
    private String createdBy;
    private String updatedBy;
    private String logo;
    MultipartFile file;
    private Long bankId;
    private Long bankBranchId;
    private Long branchId;
    private Long parentAcctId;
    private String parentAcctName;
    private Long paymentModeId;
    private String bankBranchName;
    private String bankName;
    private String paymentMode;
    private String branchName;
    private String accountTypeName;
    private String accountTypeId;
    private String underwriterApiUrl;
    private String integrationType;
    private String underwriterApiUsername;
    private String underwriterApiPassword;

}
