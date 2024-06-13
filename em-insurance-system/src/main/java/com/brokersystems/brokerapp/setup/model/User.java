package com.brokersystems.brokerapp.setup.model;

import java.io.Serializable;
import java.util.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

import com.brokersystems.brokerapp.setup.service.UserService;
import com.brokersystems.brokerapp.setup.service.impl.UserServiceImpl;
import com.brokersystems.brokerapp.users.model.RolePermissions;
import com.brokersystems.brokerapp.users.model.RolesDef;
import com.brokersystems.brokerapp.users.model.UserRole;
import com.brokersystems.brokerapp.users.repository.UserRolesRepo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
//@Cacheable(true)
@Table(name ="sys_brk_users")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class User extends AuditBaseEntity  implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Id
    @GeneratedValue
    @Column(name="user_id")
    private Long id;
    
    @Column(name="user_username")
    private String username;

    @Column(name="user_email")
    private String email;
    
    @Column(name="user_name")
    private String name;
    
    @Column(name="user_status")
    private String enabled;
    
    @Column(name="user_password")
    private String password;

    @Column(name = "user_reset_pwd")
    private String resetPass;

    @ManyToOne(fetch =FetchType.EAGER)
    @JoinColumn(name="user_sub_account")
    private AccountTypes subAccountTypes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_sub_agent")
    private AccountDef accountDef;

    @Column(name="user_last_login")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLogin;

    @Column(name="user_last_ip")
    private String lastIP;

    @Column(name="user_signature", length = 160)
    private String signature;

    @Column(name="user_content_type",length = 20)
    private String signatureContentType;
    @Column(name="user_send_email",length = 5)
    private String sendEmail;

    public User(User user) {
        this.id = user.id;
        this.username = user.username;
        this.name = user.name;       
        this.password = user.password;
        this.enabled=user.enabled;
        this.email = user.email;
        this.lastLogin=new Date();
        this.lastIP = user.lastIP;
}

    public User() {
    	
	}


    public AccountTypes getSubAccountTypes() {
        return subAccountTypes;
    }

    public void setSubAccountTypes(AccountTypes subAccountTypes) {
        this.subAccountTypes = subAccountTypes;
    }

    public AccountDef getAccountDef() {
        return accountDef;
    }

    public void setAccountDef(AccountDef accountDef) {
        this.accountDef = accountDef;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}


    public String getResetPass() {
        return resetPass;
    }

    public void setResetPass(String resetPass) {
        this.resetPass = resetPass;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    @Override
	public String toString() {
		return username;
	}

    public String getLastIP() {
        return lastIP;
    }

    public void setLastIP(String lastIP) {
        this.lastIP = lastIP;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getSignatureContentType() {
        return signatureContentType;
    }

    public void setSignatureContentType(String signatureContentType) {
        this.signatureContentType = signatureContentType;
    }

    public String getSendEmail() {
        return sendEmail;
    }

    public void setSendEmail(String sendEmail) {
        this.sendEmail = sendEmail;
    }
}