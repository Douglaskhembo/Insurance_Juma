<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="modal fade" id="rptHeaderModal" tabindex="-1" role="dialog"
     aria-labelledby="rptHeaderModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="rptHeaderModalLabel">
                    Edit/Add Report Header
                </h4>
            </div>
            <div class="modal-body">
                <form id="rpt-header-form" class="form-horizontal">
                    <input type="hidden" class="form-control" id="rh-id" name="rhId">
                    <div class="form-group">
                        <label for="brn-id" class="col-md-3 control-label">Module Name</label>

                        <div class="col-md-8">
                            <select class="form-control" id="module-name" name="moduleCode" required>
                                <option value="">Select Module</option>
                                <option value="U">Underwriting</option>
                                <option value="A">Accounts</option>
                                <option value="C">Claims</option>
                                <option value="M">Medical</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="cou-name" class="col-md-3 control-label">Report Header</label>

                        <div class="col-md-8">
                            <input type="text" class="editUserCntrls form-control"
                                   id="rpt-header" name="moduleName"
                                   required>
                        </div>
                    </div>

                </form>
            </div>
            <div class="modal-footer">
                <button data-loading-text="Saving..." id="saveReportHeader"
                        type="button" class="btn btn-success">
                    Save
                </button>
                <button type="button" class="btn btn-default" data-dismiss="modal">
                    Cancel
                </button>
            </div>
        </div>
    </div>
</div>



<div class="modal fade" id="rptParamModal" tabindex="-1" role="dialog"
     aria-labelledby="rptParamModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="rptParamModalLabel">
                    Edit/Add Report Parameter
                </h4>
            </div>
            <div class="modal-body">
                <form id="rpt-param-form" class="form-horizontal">
                    <input type="hidden" class="form-control" id="rp-param-id" name="rpId">
                    <input type="hidden" class="form-control" id="rp-reportdef-id" name="reportDef">
                    <div class="form-group">
                        <label for="rp-param-id" class="col-md-3 control-label">Display Name</label>

                        <div class="col-md-8">
                            <input type="text" class="editUserCntrls form-control"
                                   id="rpt-param-name" name="paramName"
                                   required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="rp-param-id" class="col-md-3 control-label">Parameter Name</label>

                        <div class="col-md-8">
                            <input type="text" class="editUserCntrls form-control"
                                   id="rpt-param-act-name" name="paramActualName"
                                   required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="param-type" class="col-md-3 control-label">Parameter Type</label>

                        <div class="col-md-8">
                            <select class="form-control" id="param-type" name="paramType" required>
                                <option value="">Select Parameter Type</option>
                                <option value="D">Date</option>
                                <option value="N">Number</option>
                                <option value="T">Text</option>
                                <option value="L">LOV</option>
                                <option value="O">Options</option>
                            </select>

                        </div>
                    </div>
                    <div class="form-group">
                        <label for="cou-name" class="col-md-3 control-label">Options(separated by commas)</label>

                        <div class="col-md-8">
                            <textarea class="form-control" rows="5" id="option-name" name="options" disabled required></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="lov-name" class="col-md-3 control-label">Lov Name</label>

                        <div class="col-md-8">
                            <select class="form-control" id="lov-name" name="lovName" disabled required>
                                <option value="">Select LOV Name</option>
                                <option value="C">Client</option>
                                <option value="A">Agent</option>
                                <option value="SA">Sub Agent</option>
                                <option value="U">User</option>
                                <option value="P">Policy</option>
                                <option value="B">Branch</option>
                                <option value="PR">Product</option>
                                <option value="BI">Binder</option>
                                <option value="CR">Currency</option>
                                <option value="R">Remmittance</option>
                                <option value="CT">Certificate Type</option>
                                <option value="PM">Payment Mode</option>
                            </select>

                        </div>
                    </div>

                    <div class="form-group">
                        <label for="lov-name" class="col-md-3 control-label">Password Field</label>

                        <div class="col-md-8">
                            <select class="form-control" id="pass-name" name="passwordField" disabled required>
                                <option value="">Select Password Field</option>
                                <option value="idNo">ID Number</option>
                                <option value="pinNo">Pin Number</option>
                                <option value="passportNo">Passport Number</option>
                                <option value="phoneNo">Phone Number</option>
                            </select>

                        </div>
                    </div>

                </form>
            </div>
            <div class="modal-footer">
                <button data-loading-text="Saving..." id="saveReportParams"
                        type="button" class="btn btn-success">
                    Save
                </button>
                <button type="button" class="btn btn-default" data-dismiss="modal">
                    Cancel
                </button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="rptDefModal" tabindex="-1" role="dialog"
     aria-labelledby="rptDefModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close btn-can" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="rptDefModalLabel">
                    Edit/Add Report
                </h4>
            </div>
            <div class="modal-body">
                <form id="rpt-def-form" class="form-horizontal">
                    <input type="hidden" class="form-control" id="rp-id" name="rptId">
                    <input type="hidden" class="form-control" id="rp-header-id" name="reportHeader">
                    <div class="form-group">
                        <label for="rpt-name" class="col-md-3 control-label">Report Name</label>

                        <div class="col-md-8">
                            <input type="text" class="editUserCntrls form-control"
                                   id="rpt-name" name="rptName"
                                   required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="rpt-template" class="col-md-3 control-label">Report Template</label>

                        <div class="col-md-8">
                            <input type="text" class="editUserCntrls form-control"
                                   id="rpt-template" name="rptTemplateName"
                                   required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="rpt-desc" class="col-md-3 control-label">Report Description</label>

                        <div class="col-md-8">
								<textarea rows="3" cols="20" name="rptDescription" id="rpt-desc"
                                          class="editUserCntrls form-control"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="cou-name" class="col-md-3 control-label">Permissions</label>

                        <div class="col-md-7">

                            <input type="hidden" id="perm-id" name="permissionsDef"/>
                            <input type="hidden" id="perm-name">

                            <div id="perm-def" class="form-control"
                                 select2-url="<c:url value="/protected/setup/reports/permReports"/>" >
                            </div>
                        </div>
                        <div class="col-md-1">
                            <button type="button" class="btn btn-success btn btn-info btn-xs" id="btn-add-permissions">New</button>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="rpt-active" class="col-md-3 control-label">Active Indicator</label>
                        <div class="col-md-9 checkbox">
                            <label>

                                <input type="checkbox" name="active" id="rpt-active">
                            </label>

                        </div>
                    </div>
                    <div class="form-group">
                        <label for="rpt-active" class="col-md-3 control-label">Password Protect</label>
                        <div class="col-md-9 checkbox">
                            <label>
                                <input type="checkbox" name="passwordProtect" id="rpt-password-protect">
                            </label>

                        </div>
                    </div>

                </form>
            </div>
            <div class="modal-footer">
                <button data-loading-text="Saving..." id="saveReportDef"
                        type="button" class="btn btn-success">
                    Save
                </button>
                <button type="button" class="btn btn-default btn-can" data-dismiss="modal">
                    Cancel
                </button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="permissionsModal" tabindex="-1" role="dialog"
     aria-labelledby="permissionsModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="permissionsModalLabel">
                    Add Report Permissions
                </h4>
            </div>
            <div class="modal-body" id="branch_models">
                <form id="permissions-form" class="form-horizontal">
                    <input type="hidden" class="form-control" id="moduleId" name="module">

                    <div class="form-group">
                        <label for="unit-id" class="col-md-3 control-label">Permission Description</label>

                        <div class="col-md-8">
                            <input type="text" class="form-control" id="permission-desc"
                                   name="permDesc"  required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="unit-id" class="col-md-3 control-label">Permission Name</label>

                        <div class="col-md-8">
                            <input type="text" class="form-control" id="permission-name"
                                   name="permName"  required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="cou-name" class="col-md-3 control-label">Description</label>

                        <div class="col-md-8">
                            <select class="form-control" id="access-type" name="accessType" required>
                                <option value="">Select Access Type</option>
                                <option value="A">Access</option>
                                <option value="L">Limit</option>
                            </select>
                        </div>
                    </div>

                </form>
            </div>
            <div class="modal-footer">
                <button data-loading-text="Saving..." id="save-permission"
                        type="button" class="btn btn-success">
                    Save
                </button>
                <button type="button" class="btn btn-default" data-dismiss="modal">
                    Cancel
                </button>
            </div>
        </div>
    </div>
</div>