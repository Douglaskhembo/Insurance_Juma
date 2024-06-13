package com.brokersystems.brokerapp.uw.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BinderDTO {

    private Long binId;
    private String binName;
    private String binPolNo;
    private Long proCode;
    private String proDesc;
    private String ageApplicable;
    private Long acctId;
    private String name;

    private boolean motorProduct;



}
