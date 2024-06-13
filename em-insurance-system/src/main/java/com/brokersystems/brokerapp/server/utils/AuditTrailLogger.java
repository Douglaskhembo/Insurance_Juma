package com.brokersystems.brokerapp.server.utils;

import com.brokersystems.brokerapp.accounts.controllers.AccountsController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
public class AuditTrailLogger {

    private final Logger logger = LoggerFactory.getLogger(AuditTrailLogger.class);


    @Autowired
    private UserUtils userUtils;

    public void log(String message, HttpServletRequest request) {
        InetAddress addr = null;
        String ipAddress = "";
        String hostname = "localhost";
//        String ipAddress =  request.getSession().getAttribute("ipAddress").toString();
        try {
            ipAddress =  StringUtils.isBlank(userUtils.getCurrentUser().getLastIP())? request.getRemoteAddr(): userUtils.getCurrentUser().getLastIP();
            addr = InetAddress.getByName(ipAddress);
            hostname = addr.getHostName();
        }
        catch (UnknownHostException ex){

        }
        MDC.put("username", userUtils.getCurrentUser().getUsername());
        // client's IP address
        MDC.put("address", ipAddress);
        MDC.put("machinename", hostname);
        logger.info(message);
        MDC.remove("username");
        MDC.remove("address");
        MDC.remove("machinename");
    }

//    public void log(String message) {
//        MDC.put("username", userUtils.getCurrentUser().getUsername());
//        logger.info(message);
//        MDC.remove("username");
//    }
}
