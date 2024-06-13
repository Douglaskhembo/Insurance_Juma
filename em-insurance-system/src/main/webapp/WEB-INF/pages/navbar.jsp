<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="col-md-3 left_col menu_fixed">
    <div class="left_col scroll-view">
        <div class="navbar nav_title" style="border: 0;">
            <spring:message code='project.name'/>
        </div>

        <div class="clearfix"></div>

        <!-- sidebar menu -->
        <div id="sidebar-menu" class="main_menu_side hidden-print main_menu" >
            <div class="menu_section">
                <ul class="nav side-menu">
                    <li><a href="<c:url value="/protected/home"/>"><i class="fa fa-home"></i>Home</a></li>
                    <sec:authorize access="hasAnyAuthority('ACCESS_SETUPS')">
                        <li><a><i class="fa fa-cogs"></i> Set ups <span class="fa fa-chevron-down"></span></a>
                            <ul class="nav child_menu">
                                <li><a href="<c:url value="/protected/home/orgsetups"/>">Organization setups</a></li>
                                <li><a href="<c:url value="/protected/home/setups"/>">Underwriting Setups</a></li>
<%--                                <li><a href="<c:url value="/protected/setups/contract"/>">Contracts</a></li>--%>
                                <li><a href="<c:url value="/protected/home/financesetups"/>">Finance Setups</a></li>
<%--                                <li><a href="<c:url value="/protected/home/medicalsetups"/>">Medical Setups</a></li>--%>
                            </ul>
                        </li>
                    </sec:authorize>
                    <sec:authorize access="hasAnyAuthority('ACCESS_USER_ADMIN')">
                        <li><a><i class="fa fa-user"></i>User Administration<span class="fa fa-chevron-down"></span></a>
                            <ul class="nav child_menu">
                                <sec:authorize access="hasAnyAuthority('ACCESS_USERS')">
                                    <li><a href="<c:url value="/protected/users/usersHome"/>">Users and Roles</a></li>
                                </sec:authorize>
                                <sec:authorize access="hasAnyAuthority('ACCESS_ROLE_PERMISSIONS')">
                                    <li><a href="<c:url value="/protected/users/roles"/>">Roles and Permissions</a></li>
                                </sec:authorize>

                            </ul>
                        </li>
                    </sec:authorize>
                    <sec:authorize access="hasAnyAuthority('ACCESS_QUOTES')">
                        <li><a><i class="fa fa-slack"></i> Quotes <span class="fa fa-chevron-down"></span></a>
                            <ul class="nav child_menu">
                                <sec:authorize access="hasAnyAuthority('ACCESS_PROSPECTS')">
                                    <li><a
                                            href="<c:url value="/protected/clients/setups/prospectlist"/>">Prospects</a></li>
                                </sec:authorize>
                                <sec:authorize access="hasAnyAuthority('NEW_QUOTE')">
                                    <li><a href="<c:url value="/protected/quotes/quotform"/>">New Quote</a></li>
                                </sec:authorize>
<%--                                <sec:authorize access="hasAnyAuthority('NEW_QUOTE')">--%>
<%--                                    <li><a href="<c:url value="/protected/quotes/medquotform"/>">Medical Quote</a></li>--%>
<%--                                </sec:authorize>--%>
                                <sec:authorize access="hasAnyAuthority('QUOTE_ENQUIRY')">
                                    <li><a href="<c:url value="/protected/quotes/enquiryquotes"/>">Quote Enquiry</a></li>
                                </sec:authorize>
                                <sec:authorize access="hasAnyAuthority('ACCESS_CONVERT_QUOTE')">
                                    <li><a href="<c:url value="/protected/quotes/convertquote"/>">Convert Quotes</a></li>
                                </sec:authorize>

                            </ul>
                        </li>
                    </sec:authorize>
                    <sec:authorize access="hasAnyAuthority('ACCESS_UW')">
                        <li><a><i class="fa fa-flickr"></i> Transactions <span class="fa fa-chevron-down"></span></a>
                            <ul class="nav child_menu">
                                <sec:authorize access="hasAnyAuthority('ACCESS_INSURANCE_COMPANIES')">
                                    <li><a href="<c:url value="/protected/setups/accts"/>">Underwriters</a></li>
                                </sec:authorize>
                                <sec:authorize access="hasAnyAuthority('ACCESS_CLIENTS')">
                                    <li><a href="<c:url value="/protected/clients/setups/clientslist"/>">Clients</a></li>
                                </sec:authorize>
                                <sec:authorize access="hasAnyAuthority('ACCESS_NEW_BUSINESS')">
                                    <li><a href="<c:url value="/protected/uw/policies/uwform"/>">General Business</a></li>
                                </sec:authorize>
<%--                                <sec:authorize access="hasAnyAuthority('ACCESS_NEW_BUSINESS')">--%>
<%--                                    <li><a href="<c:url value="/protected/medical/policies/uwform"/>">Medical Business</a></li>--%>
<%--                                </sec:authorize>--%>
                                <sec:authorize access="hasAnyAuthority('ACCESS_NEW_BUSINESS')">
                                    <li><a href="<c:url value="/protected/uw/policies/quickUw"/>">Quick Motor Business</a></li>
                                </sec:authorize>
                                <sec:authorize access="hasAnyAuthority('ACCESS_NEW_BUSINESS')">
                                    <li><a href="<c:url value="/protected/life/policies/lifeuwform"/>">Individual Life Business</a></li>
                                </sec:authorize>
                                <sec:authorize access="hasAnyAuthority('POLICY_ENQUIRY')">

                                    <li><a href="<c:url value="/protected/uw/policies/policyEnquiry"/>">Policy Enquiry</a></li>
                                </sec:authorize>
                                <sec:authorize access="hasAnyAuthority('POLICY_ENQUIRY')">
                                    <li><a href="<c:url value="/protected/uw/policies/pendingTrans"/>">Pending Trans</a></li>
                                </sec:authorize>
                                <sec:authorize access="hasAnyAuthority('ACCESS_ENDORSEMENTS')">
                                    <li><a href="<c:url value="/protected/uw/endorsements/endorstrans"/>">Endorsements</a></li>
                                </sec:authorize>
                                <sec:authorize access="hasAnyAuthority('ACCESS_ENDORSEMENTS')">
                                    <li><a href="<c:url value="/protected/uw/endorsements/lifeendors"/>">Life Endorsements</a></li>
                                </sec:authorize>
                                <sec:authorize access="hasAnyAuthority('ACCESS_REVERSALS')">
                                    <li><a href="<c:url value="/protected/uw/endorsements/contras"/>">Reversals</a></li>
                                </sec:authorize>
                                <sec:authorize access="hasAnyAuthority('ACCESS_RENEWALS')">
                                    <li><a href="<c:url value="/protected/uw/renewals/renewalsprocess"/>">Renewals</a></li>
                                </sec:authorize>
<%--                                <li><a href="<c:url value="/protected/medical/claims/medcards"/>">Medical Cards</a></li>--%>
                            </ul>
                        </li>
                    </sec:authorize>
                    <sec:authorize access="hasAnyAuthority('ACCESS_CERTS')">
                        <li><a><i class="fa fa-certificate"></i>Certificates <span class="fa fa-chevron-down"></span></a>
                            <ul class="nav child_menu">
                                <sec:authorize access="hasAnyAuthority('ACCESS_CERT_TYPES')">
                                    <li><a href="<c:url value="/protected/certs/certtype"/>">Certificate Types</a></li>
                                </sec:authorize>
                                    <%--<sec:authorize access="hasAnyAuthority('ACCESS_CERT_LOTS')">--%>
                                    <%--<li><a href="<c:url value="/protected/certs/certlots"/>">Certificates Lots</a></li>--%>
                                    <%--</sec:authorize>--%>
<%--                                <sec:authorize access="hasAnyAuthority('BRANCH_CERTS')">--%>
<%--                                    <li><a href="<c:url value="/protected/certs/brncerts"/>">Certificates Administration</a></li>--%>
<%--                                </sec:authorize>--%>
<%--                                <sec:authorize access="hasAnyAuthority('PRINT_CERTS')">--%>
<%--                                    <li><a href="<c:url value="/protected/certs/prntcerts"/>">Print Certificate</a></li>--%>
<%--                                </sec:authorize>--%>
                            </ul>
                        </li>
                    </sec:authorize>
                    <sec:authorize access="hasAnyAuthority('ACCESS_CLAIMS')">
                        <li><a><i class="fa fa-stack-exchange"></i> Claim Transactions <span class="fa fa-chevron-down"></span></a>
                            <ul class="nav child_menu">
                                <sec:authorize access="hasAnyAuthority('NEW_CLAIM')">
                                    <li><a href="<c:url value="/protected/claims/newclaim2"/>">New Claim</a></li>
                                </sec:authorize>
<%--                                <sec:authorize access="hasAnyAuthority('NEW_CLAIM')">--%>
<%--                                    <li><a href="<c:url value="/protected/claims/newclaim"/>">New Claim Payment</a></li>--%>
<%--                                </sec:authorize>--%>
                                <sec:authorize access="hasAnyAuthority('CLAIM_TRANS')">
                                    <li><a href="<c:url value="/protected/claims/enquireclaims"/>">Claim Transactions</a></li>
                                </sec:authorize>
                                <sec:authorize access="hasAnyAuthority('CREATE_CLAIMANTS')">
                                    <li><a href="<c:url value="/protected/claims/claimants"/>">Claimants</a></li>
                                </sec:authorize>
                                <sec:authorize access="hasAnyAuthority('CREATE_CLAIMANTS')">
                                    <li><a href="<c:url value="/protected/claims/serviceproviders"/>">Service Providers</a></li>
                                </sec:authorize>
<%--                                <sec:authorize access="hasAnyAuthority('NEW_CLAIM')">--%>
<%--                                    <li><a href="<c:url value="/protected/medical/claims/clmprocess"/>">Medical Claims</a></li>--%>
<%--                                </sec:authorize>--%>
<%--                                <li><a href="<c:url value="/protected/medical/claims/smartcards"/>">Smart Claims</a></li>--%>
                            </ul>
                        </li>
                    </sec:authorize>
                    <sec:authorize access="hasAnyAuthority('ACCESS_FINANCE')">
                        <li><a><i class="fa fa-money"></i> Finance Transactions <span class="fa fa-chevron-down"></span></a>
                            <ul class="nav child_menu">
                                <sec:authorize access="hasAnyAuthority('ACCESS_RECEIPT')">
                                    <li><a href="<c:url value="/protected/uw/receipts/receiptentry"/>">New Receipts</a></li>
                                </sec:authorize>
                                <sec:authorize access="hasAnyAuthority('ACCESS_RECEIPT')">
                                    <li><a href="<c:url value="/protected/uw/receipts/reprintrecpt"/>">Reprint Receipt</a></li>
                                </sec:authorize>
                                <sec:authorize access="hasAnyAuthority('ACCESS_RECEIPT')">
                                    <li><a href="<c:url value="/protected/uw/receipts/uprintedrcts"/>">Unprinted Receipts</a></li>
                                </sec:authorize>
                                <sec:authorize access="hasAnyAuthority('ACCESS_RECEIPT')">
                                    <li><a href="<c:url value="/protected/uw/receipts/cancelrec"/>">Cancel Receipts</a></li>
                                </sec:authorize>
                                <sec:authorize access="hasAnyAuthority('ACCESS_ALLOCATIONS')">
                                    <li><a href="<c:url value="/protected/accounts/alloctrans"/>">Allocations</a></li>
                                </sec:authorize>
                                <sec:authorize access="hasAnyAuthority('ACCESS_CREDITOR_PAYMENTS')">
                                    <li><a href="<c:url value="/protected/accounts/creditpaymt"/>">Creditor Payment</a></li>
                                </sec:authorize>
                                <li><a href="<c:url value="/protected/trans/adminfee/newtrans"/>">Admin Fee Trans</a></li>
                                <sec:authorize access="hasAnyAuthority('ACCESS_ACOUNT_TRANS')">
                                    <li><a href="<c:url value="/protected/accounts/authTrans"/>">Account Transactions</a></li>
                                </sec:authorize>
<%--                                <li><a href="<c:url value="/protected/medical/claims/batchpartrans"/>">Batch Claims Trans</a></li>--%>
                                <sec:authorize access="hasAnyAuthority('ACCESS_RECEIPT')">
                                    <li><a href="<c:url value="/protected/uw/receipts/mobmoney"/>">Mpesa Integration</a></li>
                                </sec:authorize>
                            </ul>
                        </li>
                    </sec:authorize>
                    <sec:authorize access="hasAnyAuthority('ACCESS_FINANCE')">
                        <li><a><i class="fa fa-paypal"></i>Payments <span class="fa fa-chevron-down"></span></a>
                            <ul class="nav child_menu">
                                <sec:authorize access="hasAnyAuthority('ACCESS_RECEIPT')">
                                    <li><a href="<c:url value="/protected/accounts/payeesvw"/>">Payees</a></li>
                                </sec:authorize>
                                <sec:authorize access="hasAnyAuthority('ACCESS_RECEIPT')">
                                    <li><a href="<c:url value="/protected/accounts/payments/createrequistion"/>">New Requisition</a></li>
                                </sec:authorize>
                                <sec:authorize access="hasAnyAuthority('ACCESS_RECEIPT')">
                                    <li><a href="<c:url value="/protected/accounts/payments/requistion"/>">Pending Requisition</a></li>
                                </sec:authorize>
                                <sec:authorize access="hasAnyAuthority('ACCESS_RECEIPT')">
                                    <li><a href="<c:url value="/protected/accounts/payments/approvedtrans"/>">Approved Requisitions</a></li>
                                </sec:authorize>
                            </ul>
                        </li>
                    </sec:authorize>
                    <sec:authorize access="hasAnyAuthority('ACCESS_REPORTS')">
                        <li><a><i class="fa fa-tachometer"></i>Reports <span class="fa fa-chevron-down"></span></a>
                            <ul class="nav child_menu">
                                <sec:authorize access="hasAnyAuthority('REPORT_SETUPS')">
                                    <li><a href="<c:url value="/protected/setup/reports/rptsetups"/>">Reports Setups</a></li>
                                </sec:authorize>
                                <sec:authorize access="hasAnyAuthority('UW_REPORTS')">
                                    <li><a href="<c:url value="/protected/reports/uwrep"/>">UW Reports</a></li>
                                </sec:authorize>
                                <sec:authorize access="hasAnyAuthority('ACCOUNTS_REPORTS')">
                                    <li><a href="<c:url value="/protected/reports/accountrep"/>">Accounts Reports</a></li>
                                </sec:authorize>
                                <sec:authorize access="hasAnyAuthority('CLAIM_REPORTS')">
                                    <li><a href="<c:url value="/protected/reports/claimsrep"/>">Claims Reports</a></li>
                                </sec:authorize>
<%--                                <sec:authorize access="hasAnyAuthority('CLAIM_REPORTS')">--%>
<%--                                    <li><a href="<c:url value="/protected/reports/medreports"/>">Medical Reports</a></li>--%>
<%--                                </sec:authorize>--%>
                            </ul>
                        </li>
                    </sec:authorize>
                </ul>
            </div>


        </div>
    </div>
</div>