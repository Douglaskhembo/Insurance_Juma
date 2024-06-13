package com.brokersystems.brokerapp.security;


import com.brokersystems.brokerapp.server.utils.AuditTrailLogger;
import com.brokersystems.brokerapp.setup.model.User;
import com.brokersystems.brokerapp.setup.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


@Component
public class AuthenticationSuccessHandler extends
		SavedRequestAwareAuthenticationSuccessHandler {



	private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	public AuthenticationSuccessHandler() {
		super();
		this.setAlwaysUseDefaultTargetUrl(true);
		this.setDefaultTargetUrl("/protected/home");
	}
	

 @Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException {
		redirectStrategy.sendRedirect(request, response, "/protected/home");
	}



}
