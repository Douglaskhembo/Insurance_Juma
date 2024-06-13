package com.brokersystems.brokerapp.soapws.service;

import com.brokersystems.brokerapp.server.exception.BadRequestException;
import com.brokersystems.brokerapp.webservices.model.MobMoneydto;

/**
 * Created by HP on 5/27/2018.
 */
public interface BankReceiptService {

    String autoReceipt(String refNo) throws BadRequestException;


}
