package com.brokersystems.brokerapp.setup.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDTO {

    private String tenantNumber;
    private String clientName;
    private String fname;
    private String otherNames;
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date dob;
    private String dateOfBirth;
    private String gender;
    private String idNo;
    private String pinNo;
    private String emailAddress;
    private String phoneNo;
    private Long clientTypeId;
    private String clientType;
    private String clientTypeDesc;
    private String status;
    private Date dateCreated;
    private Long tenId;
    private String createdBy;
    private String authBy;
    private String username;
    private String passportNo;
    private Long obId;
    private String obName;
    private String comment;
    private Date dateterminated;
    private Date dateregistered;
    private String address;
    private String clientRef;
    private String authStatus;
    private String officeTel;
    private Long occCode;
    private String occName;
    private Long sectCode;
    private String sectName;
    private Long titleId;
    private String titleName;
    private Long phonePrefixId;
    private String phonePrefixName;
    private Long smsPrefixId;
    private String smsPrefixName;
    private Long pcode;
    private String postalName;
    private Long ctCode;
    private String ctName;
    MultipartFile file;
    private String photoUrl;
    private String smsNumber;
    private Long couCode;
    private String couName;
    private String hashCode;

}
