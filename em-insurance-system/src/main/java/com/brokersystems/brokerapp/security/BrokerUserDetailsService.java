package com.brokersystems.brokerapp.security;

import com.brokersystems.brokerapp.reports.service.ReportGenerator;
import com.brokersystems.brokerapp.server.exception.BadRequestException;
import com.brokersystems.brokerapp.setup.dto.UserDTO;
import com.brokersystems.brokerapp.setup.model.User;
import com.brokersystems.brokerapp.setup.model.UserAuthority;
import com.brokersystems.brokerapp.setup.service.UserService;
import com.brokersystems.brokerapp.users.model.RolePermissions;
import com.brokersystems.brokerapp.users.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by HP on 11/4/2017.
 */
@Service("brokerUserDetailsService")
public class BrokerUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Autowired
    private ReportGenerator reportGenerator;

    @Autowired
    public BrokerUserDetailsService(final UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(final String s) throws UsernameNotFoundException {

        final UserDTO user = userService.findByUserName(s);
        if(user==null){
            throw new UsernameNotFoundException("Username not found");
        }
        final boolean enabled = user.getStatus()!=null && user.getStatus().equals("1");
        final boolean changePassword = user.getResetPass()!=null && user.getResetPass().equals("Y");
        return new BrokerUser(user.getUsername(), (user.getPassword()!=null)?user.getPassword():"",
                enabled, true, !changePassword, true, getGrantedAuthorities(user.getId()));
    }

    private Set<UserAuthority> getGrantedAuthorities(final Long uid){
        return userService.getUserAuthorities(uid);
    }
}
