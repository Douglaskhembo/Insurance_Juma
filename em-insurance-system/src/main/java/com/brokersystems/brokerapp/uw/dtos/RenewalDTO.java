package com.brokersystems.brokerapp.uw.dtos;

import org.easybatch.core.record.GenericRecord;
import org.easybatch.core.record.Header;

import com.brokersystems.brokerapp.setup.model.User;

public class RenewalDTO  {
	
	
	private Long policyId;
	
	private User user;
	
	private String policyNo;
	

	public Long getPolicyId() {
		return policyId;
	}

	public void setPolicyId(Long policyId) {
		this.policyId = policyId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	@Override
	public String toString() {
		return "RenewalDTO [policyId=" + policyId + ", user=" + user.getUsername() + "]";
	}
	
	

}
