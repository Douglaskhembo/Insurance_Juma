package com.brokersystems.brokerapp.users.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChangePasswordDTO implements Serializable {

    private String currentPassword;
    private String newPass;
    private String confirmPass;
    private String username;


}
