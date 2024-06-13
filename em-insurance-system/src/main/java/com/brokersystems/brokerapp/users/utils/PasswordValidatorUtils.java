package com.brokersystems.brokerapp.users.utils;

import com.brokersystems.brokerapp.server.exception.BadRequestException;
import com.brokersystems.brokerapp.setup.service.ParamService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.StringTokenizer;

@Component
public class PasswordValidatorUtils {



    @Autowired
    private ParamService paramService;

    public boolean validatePassword(String password)  {
        if(password==null || StringUtils.isBlank(password)){
            return false;
        }
        boolean matchesPass = false;
        String passValidator = null;
        try {
            passValidator = paramService.getParameterString("PASSWORD_FORMAT");
        } catch (BadRequestException e) {
            return false;
        }
        StringTokenizer token = new StringTokenizer(passValidator, ";");
        while (token.hasMoreTokens()) {
            if (password.matches(token.nextToken())) {
                matchesPass=true;
            }
        }
        return matchesPass;
    }

}
