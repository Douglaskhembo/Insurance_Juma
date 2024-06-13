package com.brokersystems.brokerapp.trans.service;

import com.brokersystems.brokerapp.quotes.dto.PendingQuotDTO;
import com.brokersystems.brokerapp.quotes.dto.QuoteEnquireDTO;
import com.brokersystems.brokerapp.server.datatables.DataTablesRequest;
import com.brokersystems.brokerapp.server.datatables.DataTablesResult;
import com.brokersystems.brokerapp.trans.model.HomeAggregateBean;
import com.brokersystems.brokerapp.trans.model.HomePremiumBean;
import com.brokersystems.brokerapp.uw.dtos.PolicyEnquiryDTO;

import java.util.List;

/**
 * Created by peter on 2/22/2017.
 */
public interface HomeService {

    HomeAggregateBean getDashBoardDetails();
    List<HomePremiumBean> getPremiumProduction();
    List<HomePremiumBean> getProductPremium();
    List<HomePremiumBean> getBranchPremium();
    DataTablesResult<PendingQuotDTO> getPendingQuotes(DataTablesRequest request);
    DataTablesResult<PolicyEnquiryDTO> getExpiredPolicies(DataTablesRequest request);
    DataTablesResult<PolicyEnquiryDTO> getPendingEndorsements(DataTablesRequest request);
    DataTablesResult<PolicyEnquiryDTO> getPendingRenewals(DataTablesRequest request);
}
