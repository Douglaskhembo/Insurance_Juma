package com.brokersystems.brokerapp.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.brokersystems.brokerapp.setup.model.User;


public class SpringSecurityAuditorAware implements AuditorAware<String> {

    public String getCurrentAuditor() {
       return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
