package com.brokersystems.brokerapp.setup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDocsDTO {
    private Long cdId;
    private String fileId;
    private String uploadedFileName;
    private String checkSum;
    private String contentType;
    private Long reqId;
    private String reqShtDesc;
    private String reqDesc;
    private String authStatus;

}
