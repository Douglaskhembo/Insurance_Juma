package com.brokersystems.brokerapp;


import com.brokersystems.brokerapp.soapws.ReceiptingService;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.xml.ws.Endpoint;

@Configuration
@Import({ AppWebMVCConfig.class })
public class SoapWebServiceServiceConfig {

    @Autowired
    private ReceiptingService receiptingService;

//    @Autowired
//    private HttpServletRequest request;

    @Bean(name = Bus.DEFAULT_BUS_ID)
    public SpringBus springBus() {
        return new SpringBus();
    }

    @Bean
    public Endpoint endpoint() {
        EndpointImpl endpoint = new EndpointImpl(springBus(),receiptingService);

//        System.out.println("Host = " + request.getServerName());
//        System.out.println("Port = " + request.getServerPort());
        endpoint.publish("/ReceiptingService");
        return endpoint;
    }

}
