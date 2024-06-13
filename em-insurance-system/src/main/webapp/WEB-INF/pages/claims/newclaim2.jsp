<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<script type="text/javascript" src="<c:url value="/js/modules/utils/select2builder.js"/>"></script>
<script type="text/javascript" src="<c:url value="/libs/rivets/rivets.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/modules/claims/claims2.js"/>"></script>


<div class="x_panel">
    <div class="x_title">
        <h4>Enter Claim Details</h4>

        <ul class="nav navbar-right panel_toolbox">
            <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
            </li>
        </ul>
    </div>

    <div class="x_content">
        <c:if test="${error != null}">
            <div class="alert alert-error alert-dismissible">
                <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
                    ${error}
            </div>
        </c:if>
        <form:form class="form-horizontal form-label-left" method="post" action="newclaim" modelAttribute="claimForm">
            <div class="form-group form-required">
                <div class="col-md-6 col-xs-12">
                    <label for="houseId" class="control-label col-md-5">
                        Loss Date<span class="required">*</span></label>
                    <div class="col-md-7 col-xs-12">
                        <div class='input-group date datepicker-input' id="los-date">
                            <form:input path="lossDate" class="form-control pull-right"  id="loss-date" required="true"/>
                            <div class="input-group-addon">
                                <span class="glyphicon glyphicon-calendar"></span>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-6 col-xs-12">
                    <label for="houseId" class="control-label col-md-5">
                        Notification Date<span class="required">*</span></label>
                    <div class="col-md-7 col-xs-12">
                        <div class='input-group date datepicker-input' id="not-date">
                            <form:input path="notificationDate" class="form-control pull-right"  id="notification-date" required="true"/>
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
                        Select A Risk<span class="required">*</span></label>
                    <div class="col-md-7 col-xs-12">
                        <form:hidden path="riskId"  id="risk-id"/>
                        <form:hidden path="riskDesc"  id="risk-desc"/>
                        <form:hidden path="riskShtDesc"  id="risk-sht-desc"/>
                        <div id="risk-frm" class="form-control"
                             select2-url="<c:url value="/protected/claims/selLossRisks"/>" >

                        </div>
                    </div>
                </div>
                <div class="col-md-6 col-xs-12">
                    <label for="houseId" class="control-label col-md-5">
                        Loss Description<span class="required">*</span></label>
                    <div class="col-md-7 col-xs-12">
                    <form:textarea class="form-control" rows="2" id="loss-desc" path="lossDesc"></form:textarea>
                        </div>
                </div>
                </div>
            <div class="form-group form-required">
                <div class="col-md-6 col-xs-12">
                    <label for="houseId" class="control-label col-md-5">
                        Insurance Balance</label>
                    <div class="col-md-7 col-xs-12">
                        <p class="form-control-static" id="pol-client-balance"></p>
                    </div>
                </div>
                <div class="col-md-6 col-xs-12">
                    <label for="houseName" class="control-label col-md-5">
                        Client Balance</label>
                    <div class="col-md-7 col-xs-12">

                        <p class="form-control-static" id="pol-ins-balance"></p>
                    </div>
                </div>
            </div>

            <div class="form-group form-required">
                <div class="col-md-6 col-xs-12">
                    <label for="houseId" class="control-label col-md-5">
                        Submit Insurer Date</label>
                    <div class="col-md-7 col-xs-12">
                        <div class='input-group date datepicker-input'>
                            <form:input  class="form-control pull-right" path="insurerDate"
                                   id="insurer-date" />
                            <div class="input-group-addon">
                                <span class="glyphicon glyphicon-calendar"></span>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-6 col-xs-12">
                    <label for="houseId" class="control-label col-md-5">
                        Select Activity<span class="required">*</span></label>
                    <div class="col-md-7 col-xs-12">
                        <form:hidden  id="activity-code" path="activityId"/>
                        <form:hidden id="activity-desc" path="activityDesc"/>
                        <div id="clm-activity" class="form-control"
                             select2-url="<c:url value="/protected/claims/selclmActivity"/>" >

                        </div>
                    </div>
                </div>

            </div>

            <div class="form-group form-required">
                <div class="col-md-6 col-xs-12">
                    <label for="houseId" class="control-label col-md-5">
                        Next Review Date<span class="required">*</span></label>
                    <div class="col-md-7 col-xs-12">
                        <div class='input-group date datepicker-input' id="next-rev-date">
                            <form:input  class="form-control pull-right" path="nextReviewDate"
                                   id="net-review-date" required="true" />
                            <div class="input-group-addon">
                                <span class="glyphicon glyphicon-calendar"></span>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-6 col-xs-12">
                    <label for="houseId" class="control-label col-md-5">
                        Next Review User<span class="required">*</span></label>
                    <div class="col-md-7 col-xs-12">
                        <form:hidden  id="next-rev-user" path="nextReviewUser"/>
                        <form:hidden  id="next-rev-user-desc" path="reviewUser"/>
                        <div id="review-user" class="form-control"
                             select2-url="<c:url value="/protected/organization/managers"/>" >

                        </div>
                    </div>
                </div>

            </div>
            <div class="form-group form-required">
                <div class="col-md-6 col-xs-12">
                    <label for="rate-taxable" class="col-md-5 control-label">Liability Admission</label>
                    <div class="col-md-7 checkbox">
                        <label>
                            <form:checkbox path="liabilityAdmission" id="chk-active"/>
                        </label>

                    </div>
                </div>
                <div class="col-md-6 col-xs-12">
                    <label for="cou-name" class="col-md-5 control-label">Party to Blame</label>

                    <div class="col-md-7">
                        <form:select class="form-control" id="bin-type" path="partyToBlame">
                            <form:option value="">Select Party to Blame</form:option>
                            <form:option value="N">Non</form:option>
                            <form:option value="I">Insured</form:option>
                            <form:option value="T">Third party</form:option>
                        </form:select>
                    </div>

                </div>
            </div>


                <div class="x_title">
                    <h4>Perils</h4>

                </div>

            <input type="button" id="add-peril-btn"
                   class="btn btn-info pull-left" style="margin-right: 10px;"
                   value="Add">

            <table id="peril-table-id" class="table table-hover table-bordered">
                <thead>
                <tr>
                    <th>Claimant</th>
                    <th>Peril</th>
                    <th>Estimated Amount</th>
                    <th></th>
                </tr>
                </thead>

            </table>
            <input type="submit" value="Next" class="btn btn-success" />
        </form:form>
    </div>

</div>




<div class="modal fade" id="perilTransModal" tabindex="-1" role="dialog"
     aria-labelledby="perilTransModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="perilTransModalLabel">
                    Add A new Peril
                </h4>
            </div>
            <div class="modal-body">
                <form id="new-peril-form" class="form-horizontal">
                    <div class="form-group">

                            <label for="brn-id" class="col-md-4 control-label">Self as
                                Claimant</label>

                            <div class="col-md-8 checkbox">
                                <label>
                                    <input type="checkbox" name="selfAsClaimant" id="self-as-clmnt">
                                </label>
                            </div>
                    </div>
                    <div class="form-group">
                            <label for="brn-id" class="col-md-4 control-label">Claimant</label>

                            <div class="col-md-8">
                                <input type="hidden" id="clmt-code"/>
                                <input type="hidden" id="clmt-name">
                                <div id="clmant-def" class="form-control"
                                     select2-url="<c:url value="/protected/claims/selClaimants"/>" >

                                </div>
                            </div>
                    </div>
                    <div class="form-group">
                            <label for="brn-id" class="col-md-4 control-label">Peril</label>

                            <div class="col-md-8">
                                <input type="hidden" id="peril-code"/>
                                <input type="hidden" id="peril-name"/>
                                <div id="peril-def" class="form-control"
                                     select2-url="<c:url value="/protected/claims/selSubclassPerils"/>" >

                                </div>
                            </div>
                    </div>
                    <div class="form-group">
                            <label for="brn-id" class="col-md-4 control-label">Claim Estimate</label>

                            <div class="col-md-8">
                                <input type='text' class="form-control pull-right"
                                       id="clm-estimate" />
                            </div>
                    </div>

                </form>
            </div>
            <div class="modal-footer">
                <button data-loading-text="Saving..." id="btn-add-peril-selected"
                        type="button" class="btn btn-primary">
                    Save
                </button>
                <button type="button" class="btn btn-default" data-dismiss="modal">
                    Cancel
                </button>
            </div>
        </div>
    </div>
</div>
