package com.brokersystems.brokerapp.claims.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class ClaimActivityDTO {
    private Long activityId;
    private String activityDesc;
    private String username;
    private Date activityDate;
    private Date remDate;
    private String currentActivity;
}
