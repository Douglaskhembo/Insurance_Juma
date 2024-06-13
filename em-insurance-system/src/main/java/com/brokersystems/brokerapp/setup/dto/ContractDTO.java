package com.brokersystems.brokerapp.setup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractDTO {

    private Long contractId;
    private String contractName;
    private String contractPolicyNo;
    private String currency;
    private String intermediary;
    private String intermediaryType;
    private String status;
    private String authStatus;

}
