package com.brokersystems.brokerapp.workflow.dto;

import java.util.Date;

public class WorkFlowDTO {

    private Long taskId;
    private String activeProcess;
    private String refNo;
    private String clientName;
    private String username;
    private Date createdDate;
    private Long transactionId;

    private String transType;

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getActiveProcess() {
        return activeProcess;
    }

    public void setActiveProcess(String activeProcess) {
        this.activeProcess = activeProcess;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

}
