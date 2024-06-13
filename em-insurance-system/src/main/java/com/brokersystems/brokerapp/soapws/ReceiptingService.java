package com.brokersystems.brokerapp.soapws;

import com.brokersystems.brokerapp.soapws.FormResult;
import com.brokersystems.brokerapp.soapws.ReceiptForm;


import javax.jws.WebService;


@WebService
public interface ReceiptingService {


     FormResult processReceipts(ReceiptForm receiptForm);


}
