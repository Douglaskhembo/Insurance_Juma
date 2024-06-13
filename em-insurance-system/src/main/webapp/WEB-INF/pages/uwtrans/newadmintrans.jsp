<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript"
        src="<c:url value="/js/modules/utils/select2builder.js"/>"></script>
<script type="text/javascript"
        src="<c:url value="/libs/rivets/rivets.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/modules/uwtrans/adminfeetrans.js"/>"></script>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="x_panel">
    <div class="x_title">
        <h4>Admin Fee Transaction</h4>
    </div>
    <div class="x_content">
        <c:if test="${error != null}">
            <div class="alert alert-error alert-dismissible">
                <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
                    ${error}
            </div>
        </c:if>


        <form:form class="form-horizontal" action="startAdminFee"
                   modelAttribute="adminFeeForm">
           <div class="form-group form-required">
            <div class="col-md-6 col-xs-12">
                <label for="houseId" class="control-label col-md-5">
                    As At Date</label>
                <div class="col-md-7 col-xs-12">
                <div class='input-group date datepicker-input' id="risk-cover-from">
                    <form:input  class="form-control pull-right" path="processDate"
                           id="risk-wef-date" />
                    <div class="input-group-addon">
                        <span class="glyphicon glyphicon-calendar"></span>
                    </div>
                </div>
                    </div>
            </div>
               <div class="col-md-6 col-xs-12">

               </div>
            </div>
            <div class="form-group form-required">
                <div class="col-md-6 col-xs-12">
                    <label for="brn-id" class="col-md-5 control-label">Client</label>

                    <div class="col-md-7 col-xs-12">
                        <form:hidden id="client-id" path="clientId"/>
                        <div id="client-frm" class="form-control"
                             select2-url="<c:url value="/protected/uw/policies/uwClients"/>" >

                        </div>
                    </div>
                </div>
                <div class="col-md-6 col-xs-12">

                </div>
            </div>
            <div class="form-group form-required">
                <div class="col-md-6 col-xs-12">
                    <label for="brn-id" class="col-md-5 control-label">Branch</label>

                    <div class="col-md-7 col-xs-12">
                        <form:hidden id="branch-id" path="brnCode"/>
                        <div id="brn-frm" class="form-control"
                             select2-url="<c:url value="/protected/uw/policies/uwbranches"/>" >

                        </div>
                    </div>
                </div>
                <div class="col-md-6 col-xs-12">

                </div>
            </div>
            <div class="form-group form-required">
                <div class="col-md-6 col-xs-12">
                    <label for="brn-id" class="col-md-5 control-label">Currency</label>

                    <div class="col-md-7 col-xs-12">
                        <form:hidden id="currency-id" path="currencyId"/>
                        <div id="curr-frm" class="form-control"
                             select2-url="<c:url value="/protected/uw/policies/uwcurrencies"/>" >

                        </div>
                    </div>
                </div>
                <div class="col-md-6 col-xs-12">

                </div>
            </div>
            <div class="form-group form-required">
                <div class="col-md-6 col-xs-12">
                    <label for="houseId" class="control-label col-md-5">
                        Remarks</label>
                    <div class="col-md-7 col-xs-12">
                        <form:textarea class="form-control" rows="5" path="remarks"></form:textarea>
                    </div>
                </div>
                <div class="col-md-6 col-xs-12">

                </div>
            </div>


            <div class="box-footer">
                <input type="submit" class="btn btn-info pull-left"
                       style="margin-right: 10px;" value="Next">
            </div>

        </form:form>

    </div>
</div>
