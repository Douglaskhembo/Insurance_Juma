<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript"
        src="<c:url value="/js/modules/utils/select2builder.js"/>"></script>
<script type="text/javascript"
        src="<c:url value="/libs/rivets/rivets.js"/>"></script>
<script type="text/javascript"
        src="<c:url value="/js/modules/uwtrans/quickuw.js"/>"></script>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="x_panel">
    <div class="x_title">
        <h4>Quick Motor Underwriting</h4>
    </div>
    <div class="x_content">
        <form id="ntsa-form" class="form-horizontal">
            <input type="hidden" class="form-control" id="mpesa-code-pk" name="bidId">
             <div class="form-group form-required">
                 <div class="col-md-6 col-xs-12">
                <label for="unit-id" class="col-md-3 control-label">Pin No</label>

                <div class="col-md-8">
                    <input type="text" class="form-control" id="reg-no"
                           name="regNo"  required>

                </div>
                 </div>
                 <div class="col-md-6 col-xs-12">
                     <input type="button" class="btn btn-info pull-left"
                            value="Search" id="btn-search-ntsa">
                     <input type="button" class="btn btn-info pull-left"
                            value="Clear" id="btn-clear">
                 </div>
            </div>


        </form>

        <div id="vehicle-details">
            <div class="form-group form-required">
                <div class="col-md-6 col-xs-12">
                    <label for="houseId" class="control-label col-md-5">
                        Reg Number</label>
                    <div class="col-md-7 col-xs-12">
                        <p class="form-control-static" id="search-reg-no"></p>
                    </div>
                </div>
                <div class="col-md-6 col-xs-12">
                    <label for="houseName" class="control-label col-md-5">
                        Registration Date</label>
                    <div class="col-md-7 col-xs-12">
                        <p class="form-control-static" id="reg-date"></p>
                    </div>
                </div>
            </div>
            <div class="form-group form-required">
                <div class="col-md-6 col-xs-12">
                    <label for="houseId" class="control-label col-md-5">
                        Chasis Number</label>
                    <div class="col-md-7 col-xs-12">
                        <p class="form-control-static" id="chasis-no"></p>
                    </div>
                </div>
                <div class="col-md-6 col-xs-12">
                    <label for="houseName" class="control-label col-md-5">
                        Body Type</label>
                    <div class="col-md-7 col-xs-12">
                        <p class="form-control-static" id="body-type"></p>
                    </div>
                </div>
            </div>
            <div class="form-group form-required">
                <div class="col-md-6 col-xs-12">
                    <label for="houseId" class="control-label col-md-5">
                        Car Make</label>
                    <div class="col-md-7 col-xs-12">
                        <p class="form-control-static" id="car-make"></p>
                    </div>
                </div>
                <div class="col-md-6 col-xs-12">
                    <label for="houseName" class="control-label col-md-5">
                        Body Color</label>
                    <div class="col-md-7 col-xs-12">
                        <p class="form-control-static" id="body-color"></p>
                    </div>
                </div>
            </div>
            <div class="form-group form-required">
                <div class="col-md-6 col-xs-12">
                    <label for="houseId" class="control-label col-md-5">
                        Engine Number</label>
                    <div class="col-md-7 col-xs-12">
                        <p class="form-control-static" id="engine-no"></p>
                    </div>
                </div>
                <div class="col-md-6 col-xs-12">
                    <label for="houseName" class="control-label col-md-5">
                        Year of Manufacture</label>
                    <div class="col-md-7 col-xs-12">
                        <p class="form-control-static" id="yom"></p>
                    </div>
                </div>
            </div>
            <div class="form-group form-required">
                <div class="col-md-6 col-xs-12">
                    <label for="houseId" class="control-label col-md-5">
                       Car Model</label>
                    <div class="col-md-7 col-xs-12">
                        <p class="form-control-static" id="car-model"></p>
                    </div>
                </div>
                <div class="col-md-6 col-xs-12">
                    <label for="houseName" class="control-label col-md-5">
                        Log Book Number</label>
                    <div class="col-md-7 col-xs-12">
                        <p class="form-control-static" id="log-book"></p>
                    </div>
                </div>
            </div>


<hr>
            <h4>Enter Cover Details</h4>
            <hr>
            <div class="form-group form-required">
                <div class="col-md-6 col-xs-12">
                    <label for="houseName" class="control-label col-md-5">
                       Contract<span class="required">*</span></label>
                    <div class="col-md-7 col-xs-12">
                        <div id="edit-binder">
                            <input type="hidden" id="binder-id" name="bindCode"/>
                            <input type="hidden" id="product-id" name="prodId"/>
                            <input type="hidden" id="pol-agent-id" name="agentId"/>
                            <input type="hidden" id="pol-bind-age-appli"/>
                            <input type="hidden" id="bind-name">
                            <div id="binder-frm" class="form-control"
                                 select2-url="<c:url value="/protected/uw/policies/uwBinders"/>">

                            </div>
                        </div>
                    </div>


                </div>
                <div class="col-md-6 col-xs-12">


                </div>
            </div>
            <div class="form-group form-required">
                <div class="col-md-6 col-xs-12">
                    <label for="houseId" class="control-label col-md-5">
                        Intermediary</label>
                    <div class="col-md-7 col-xs-12">
                        <p class="form-control-static" id="pol-ins-comp"></p>
                    </div>
                </div>
                <div class="col-md-6 col-xs-12">
                    <label for="houseName" class="control-label col-md-5">
                        Product</label>
                    <div class="col-md-7 col-xs-12">
                        <p class="form-control-static" id="pol-prod-name"></p>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <div class="col-md-6 col-xs-12  form-required">
                    <label for="brn-id" class="col-md-5 control-label">Date
                        From</label>

                    <div class="col-md-7 col-xs-12">
                        <div class='input-group date datepicker-input' id="wef-date">
                            <input type='text' class="form-control pull-right" name="wefDate"
                                   id="from-date" required/>
                            <div class="input-group-addon">
                                <span class="glyphicon glyphicon-calendar"></span>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-6 col-xs-12">
                    <label for="noOfUnits" class="control-label col-md-5">Date
                        To</label>
                    <div class="col-md-7 col-xs-12">
                        <div class='input-group date datepicker-input' id="cover-to-date">
                            <input type='text' class="form-control pull-right" name="wetDate"
                                   id="wet-date" required/>
                            <div class="input-group-addon">
                                <span class="glyphicon glyphicon-calendar"></span>
                            </div>
                        </div>
                    </div>

                </div>


            </div>
            <div class="form-group form-required">
                <div class="col-md-6 col-xs-12">
                    <label for="houseId" class="control-label col-md-5">
                        Sub Class<span class="required">*</span></label>
                    <div class="col-md-7 col-xs-12">
                        <input type="hidden" id="risk-sub-code" name="sclCode"/>
                        <input type="hidden" id="sub-name">
                        <div id="subclass-frm" class="form-control"
                             select2-url="<c:url value="/protected/uw/policies/uwsubclasses"/>">

                        </div>
                    </div>
                </div>
                <div class="col-md-6 col-xs-12">
                    <label for="houseName" class="control-label col-md-5">
                        Cover Type<span class="required">*</span></label>
                    <div class="col-md-7 col-xs-12">
                        <input type="hidden" id="risk-cov-code" name="coverCode"/>
                        <input type="hidden" id="binder-det-id" name="binderDet"/>
                        <input type="hidden" id="cover-name">
                        <div id="covertypes-frm" class="form-control"
                             select2-url="<c:url value="/protected/uw/policies/riskCoverTypes"/>">

                        </div>
                    </div>
                </div>
            </div>
            <div class="form-group form-required">
                <div class="col-md-6 col-xs-12">
                    <label for="houseId" class="control-label col-md-5">
                        Interface Type<span class="required">*</span></label>
                    <div class="col-md-7 col-xs-12">
                        <select class="form-control" id="pol-interface-type" name="interfaceType"
                                required>
                            <option value="">Select Interface Type</option>
                            <option value="A">Accrual</option>
                            <option value="C">Cash</option>
                        </select>
                    </div>
                </div>
                <div class="col-md-6 col-xs-12">
                    <label for="houseName" class="control-label col-md-5">
                        Payment Mode<span class="required">*</span></label>
                    <div class="col-md-7 col-xs-12">
                        <div id="edit-payment-mode">
                            <input type="hidden" id="pm-id" name="paymentId"/>
                            <input type="hidden" id="pm-name">
                            <div id="pm-mode-frm" class="form-control"
                                 select2-url="<c:url value="/protected/uw/policies/uwpaymentmodes"/>">

                            </div>
                        </div>
                        <div id="display-payment-mode">
                            <p class="form-control-static" id="pay-mode-info"></p>
                        </div>
                    </div>
                </div>
            </div>
            <div class="form-group form-required">
                <div class="col-md-6 col-xs-12">
                    <label for="houseId" class="control-label col-md-5">
                        Payment Frequency<span class="required">*</span></label>
                    <div class="col-md-7 col-xs-12">
                        <select class="form-control" id="pol-frequency" name="frequency"
                                required>
                            <option value="">Select Payment Frequency</option>
                            <option value="M">Monthly</option>
                            <option value="Q">Quartely</option>
                            <option value="S">Semi-Annually</option>
                            <option value="A">Annually</option>
                        </select>
                    </div>
                </div>
                <div class="col-md-6 col-xs-12">
                    <label for="houseName" class="control-label col-md-5">
                        Branch<span class="required">*</span></label>
                    <div class="col-md-7 col-xs-12">
                        <div id="edit-branch">
                            <input type="hidden" id="brn-id" name="branchId"/>
                            <input type="hidden" id="brn-name">
                            <div id="brn-frm" class="form-control"
                                 select2-url="<c:url value="/protected/uw/policies/uwbranches"/>">

                            </div>
                        </div>
                        <div id="display-branch">
                            <p class="form-control-static" id="branch-info"></p>
                        </div>
                    </div>
                </div>
            </div>
            <div class="form-group form-required">
                <div class="col-md-6 col-xs-12">
                    <label for="houseId" class="control-label col-md-5">
                        Client Policy No<span class="required">*</span></label>
                    <div class="col-md-7 col-xs-12">
                        <input type="text" name="clientPolNo" id="client-pol-no" class="form-control"
                               placeholder="Client Policy Number" required>
                    </div>
                </div>
                <div class="col-md-6 col-xs-12">
                    <label for="houseId" class="control-label col-md-5">
                        Currency<span class="required">*</span></label>
                    <div class="col-md-7 col-xs-12">
                        <div id="edit-currency">
                            <input type="hidden" id="cur-id" name="currencyId"/>
                            <input type="hidden" id="cur-name">
                            <div id="curr-frm" class="form-control"
                                 select2-url="<c:url value="/protected/uw/policies/uwcurrencies"/>">

                            </div>
                        </div>
                        <div id="display-currency">
                            <p class="form-control-static" id="currency-info"></p>
                        </div>
                    </div>
                </div>
            </div>
            <div class="form-group form-required">
                <div class="col-md-6 col-xs-12">
                    <label for="houseId" class="control-label col-md-5">
                        Sub-Agent</label>
                    <div class="col-md-7 col-xs-12">
                        <input type="hidden" id="sub-agent-id" name="subAgentId"/>
                        <input type="hidden" id="sub-agent-name">
                        <div id="sub-agent-frm" class="form-control"
                             select2-url="<c:url value="/protected/uw/policies/inhouseagents"/>">

                        </div>
                    </div>
                </div>
                <div class="col-md-6 col-xs-12">

                </div>
                <div class="form-group form-required">
                <div class="col-md-6 col-xs-12">
                    <sec:authorize access="hasAnyAuthority('SAVE_POLICY')">
                        <input type="button" class="btn btn-info pull-right"
                               value="Create Policy" id="btn-add-policy"
                               >
                    </sec:authorize>
                </div>
                </div>

            </div>
        </div>
    </div>
</div>


