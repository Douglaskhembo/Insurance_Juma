package com.brokersystems.brokerapp.trans.model;

import java.util.List;

public class CancelRecData {

    private List<CancelData> receipts;

    public List<CancelData> getReceipts() {
        return receipts;
    }

    public void setReceipts(List<CancelData> receipts) {
        this.receipts = receipts;
    }

}
