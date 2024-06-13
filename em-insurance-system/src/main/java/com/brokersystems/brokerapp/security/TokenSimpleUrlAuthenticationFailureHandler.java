package com.brokersystems.brokerapp.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class TokenSimpleUrlAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	@Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        setUseForward(true);
        saveException(request,exception);
        if (exception.getClass().equals(CredentialsExpiredException.class)){
            final String username = (String) request.getAttribute("username");
            if(username!=null) {
                request.setAttribute("username", username);
            }
            setDefaultFailureUrl("/changepass");
        } else {
            System.out.println("Passed through here...");
            setDefaultFailureUrl("/login?error=true");
        }
        super.onAuthenticationFailure(request, response, exception);
    }

}
