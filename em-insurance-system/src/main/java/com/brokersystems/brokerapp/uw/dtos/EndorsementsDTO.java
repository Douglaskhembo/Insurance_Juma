package com.brokersystems.brokerapp.uw.dtos;

import lombok.Data;

import java.sql.Date;

@Data
public class EndorsementsDTO {

    private Long policyId;
	private String policyNo;
	private String clientPolNo;
	private String polRevNo;
	private String clientName;
	private String agentName;
	private String binderName;
    private Long polUwYr;
    private Boolean renewable;
    private String product;
    private Date wefDate;
    private Date wetDate;
    private String currency;
    private String username;

    private EndorsementsDTO(final Long policyId, final String policyNo, final String clientPolNo, final String polRevNo,
                            final String clientName, final String agentName, final String binderName, final Long polUwYr,
                            final Boolean renewable , final String product, final Date wefDate, final Date wetDate,
                            final String currency, final String username) {
        this.policyId = policyId;
        this.policyNo = policyNo;
        this.clientPolNo =clientPolNo;
        this.polRevNo = polRevNo;
        this.clientName = clientName;
        this.agentName = agentName;
        this.binderName = binderName;
        this.polUwYr = polUwYr;
        this.renewable = renewable;
        this.product = product;
        this.wefDate = wefDate;
        this.wetDate = wetDate;
        this.currency = currency;
        this.username = username;

    }

    public static EndorsementsDTO instance(final Long policyId, final String policyNo, final String clientPolNo, final String polRevNo,
                                           final String clientName, final String agentName, final String binderName, final Long polUwYr,
                                           final Boolean renewable , final String product, final Date wefDate, final Date wetDate,
                                           final String currency, final String username)  {
        return new EndorsementsDTO(policyId,policyNo,clientPolNo,polRevNo,clientName,agentName,binderName,polUwYr,renewable,product,wefDate,wetDate,currency,username);
    }
}
