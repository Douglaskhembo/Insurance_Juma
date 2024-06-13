package com.brokersystems.brokerapp.setup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private String username;
    private String password;
    private String confirmPassword;
    private String status;
    private Long id;
    private String name;
    private String email;
    private Long acctId;
    private String accountType;
    private Long acctTypeId;
    private String signature;
    private String signatureContentType;
    private String resetPass;
    private String marketer;
    private MultipartFile file;
    private String accType;
    private String sendEmail;
    private String userLocked;

}
