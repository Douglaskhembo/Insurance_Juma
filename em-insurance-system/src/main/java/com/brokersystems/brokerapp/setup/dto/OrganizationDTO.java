package com.brokersystems.brokerapp.setup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Transient;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationDTO {
    private Long orgCode;
    private String orgDesc;
    private String orgFax;
    private String orgLogo;
    private String orgLogoContentType;
    private String orgMobile;
    private String orgName;
    private String orgPhone;
    private String orgShtDesc;
    private String orgWebsite;
    private Date dateIncorp;
    private String certNumber;
    private String addAddress;
    private String phyAddress;
    private String emailAddress;
    private Long couCode;
    private String couName;
    private Long curCode;
    private String curName;
    private String pinNo;
    private String createdBy;
    private Date createdDate;
    private String modifiedBy;
    private String formAction;
    private Date modifiedDate;
    private MultipartFile file;
}
