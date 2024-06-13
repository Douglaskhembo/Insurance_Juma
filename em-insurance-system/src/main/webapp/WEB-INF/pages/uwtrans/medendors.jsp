<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript"
        src="<c:url value="/js/modules/utils/select2builder.js"/>"></script>
<script type="text/javascript"
        src="<c:url value="/libs/rivets/rivets.js"/>"></script>
<script type="text/javascript"
        src="<c:url value="/js/modules/uwtrans/endorse.js"/>"></script>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="x_panel">
    <div class="x_title">
        <h4>Medical Endorsements Transactions</h4>
    </div>
    <div class="x_content">
        <c:if test="${error != null}">
            <div class="alert alert-error alert-dismissible">
                <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
                    ${error}
            </div>
        </c:if>


        <form:form class="form-horizontal" action="reviseMedTransaction"
                   modelAttribute="revisionForm">
            <spring:hasBindErrors name="revisionForm">
                <div class="alert alert-error alert-dismissible">
                    <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
                    <form:errors path="effectiveDate"/>
                </div>
            </spring:hasBindErrors>
            <div class="form-group form-required">
                <div class="col-md-6 col-xs-12">
                    <label for="brn-id" class="col-md-6 control-label">Type of
                        Revision</label>

                    <div class="col-md-6 col-xs-12">
                        <form:select cssClass="form-control" id="rev-type"
                                     path="revisionType" required="required">
                            <form:option value="">Select Endorsement Type</form:option>
                            <form:option value="AD">Policy Revisions</form:option>
                            <form:option value="CN">Cancellation</form:option>
                            <form:option value="CR">Card Replacement</form:option>
                        </form:select>
                    </div>
                </div>
                <div class="col-md-6 col-xs-12"></div>


            </div>
            <div class="form-group form-required cancel-Type">
                <div class="col-md-6 col-xs-12">
                    <label for="brn-id" class="col-md-6 control-label">Cancellation Type</label>

                    <div class="col-md-6 col-xs-12">
                        <div class="radio">
                            <label>
                                    <form:radiobutton path="cancellationType" id="pCancel" value="PR"/> Prorata
                        </div>
                        <div class="radio">
                            <label><form:radiobutton path="cancellationType" id="sCancel" value="SP"/>Short Period
                        </div>
                        <div class="radio">
                            <label><form:radiobutton path="cancellationType" id="nCancel" value="NR"/>Cancellation with no refund
                        </div>
                    </div>
                </div>
                <div class="col-md-6 col-xs-12"></div>


            </div>

            <div class="form-group form-required">
                <div class="col-md-6 col-xs-12">
                    <label for="brn-id" class="col-md-6 control-label">Select a
                        Policy</label>

                    <div class="col-md-6 col-xs-12">
                        <div class='input-group'>
                            <form:hidden id="rev-pol-id" path="policyId" />
                            <form:input cssClass="form-control pull-right" id="pol-number"
                                        path="policyNumber" required="required" readonly="true"/>
                            <div class="input-group-addon">
								<span class="glyphicon glyphicon-chevron-down"
                                      id="btn-show-search-med" style="cursor: pointer"></span>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-6 col-xs-12"></div>


            </div>

            <div class="form-group form-required">
                <div class="col-md-6 col-xs-12">
                    <label for="brn-id" class="col-md-6 control-label">Effective
                        Date</label>

                    <div class="col-md-6 col-xs-12">
                        <div class='input-group date datepicker-input' id="eff-date-from">
                            <form:input cssClass="form-control pull-right"
                                        path="effectiveDate" id="eff-date" required="required"/>
                            <div class="input-group-addon">
                                <span class="glyphicon glyphicon-calendar"></span>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-6 col-xs-12"></div>


            </div>
            <div class="form-group form-required" id="eff-date-to" style="display: none">
                <div class="col-md-6 col-xs-12">
                    <label for="brn-id" class="col-md-6 control-label">Date
                        To</label>

                    <div class="col-md-6 col-xs-12">
                        <div class='input-group date datepicker-input'>
                            <form:input cssClass="form-control pull-right"
                                        path="effToDate" id="eff-to-date"/>
                            <div class="input-group-addon">
                                <span class="glyphicon glyphicon-calendar"></span>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-6 col-xs-12"></div>


            </div>

            <div class="box-footer">
                <input type="submit" class="btn btn-info pull-left"
                       style="margin-right: 10px;" value="Next">
            </div>

        </form:form>
        <div class="spacer"></div>
        <div id="existing-trans" style="display: none">
            <h3>Existing unfinished transactions</h3>
            <hr>
            <div class="cutom-container">
            <table id="poltrans" class="table table-hover table-bordered">
                <thead>
                <tr>
                    <th width="10%">Policy. No.</th>
                    <th width="10%">Trans. Date</th>
                    <th width="10%">WEF</th>
                    <th width="10%">WET</th>
                    <th width="10%">Initiated By</th>
                    <th width="10%">Status</th>
                    <th width="2%"></th>
                    <th width="2%"></th>
                </tr>
                </thead>
            </table>
                </div>
        </div>
    </div>
</div>

<jsp:include page="modals/endorsemodals.jsp"></jsp:include>