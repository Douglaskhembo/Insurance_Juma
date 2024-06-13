<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript"
        src="<c:url value="/js/modules/utils/select2builder.js"/>"></script>
<script type="text/javascript"
        src="<c:url value="/libs/rivets/rivets.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/modules/receipts/coa.js"/>"></script>
<div class="x_panel">
    <div class="x_title">
        <h2>Chart of Accounts</h2>
        <ul class="nav navbar-right panel_toolbox">
            <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
            </li>
        </ul>
        <div class="clearfix"></div>
    </div>
    <div class="x_content">
        <div class="x_panel">
            <div class="x_title">
                <h2>Main Accounts</h2>
                <ul class="nav navbar-right panel_toolbox">
                    <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
                    </li>
                </ul>
                <div class="clearfix"></div>
            </div>
            <div class="x_content">
                <div class="card-box table-responsive">
                <input type="button" class="btn btn-info pull-right"
                       style="margin-right: 10px;" value="New"
                       id="btn-add-coa">

                <table id="coa-tbl" class="table table-hover table-bordered">
                    <thead>
                    <tr>
                        <th>Code</th>
                        <th>Name</th>
                        <th>Header</th>
                        <th>Account Type</th>
                        <th>BS/PL</th>
                        <th>Accounts Order</th>
                        <th width="5%"></th>
                        <th width="5%"></th>
                    </tr>
                    </thead>
                </table>
                    </div>
                <input type="hidden" id="coa-trans-pk">
                <input type="hidden" id="coa-sub-trans-pk">
            </div>
            <div class="x_title">
                <h2>Sub Accounts</h2>
                <ul class="nav navbar-right panel_toolbox">
                    <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
                    </li>
                </ul>
                <div class="clearfix"></div>
            </div>
            <div class="x_content">
                <div class="card-box table-responsive">
                <input type="button" class="btn btn-info pull-right"
                       style="margin-right: 10px;" value="New"
                       id="btn-add-subcoa">

                <table id="coa-sub-tbl" class="table table-hover table-bordered">
                    <thead>
                    <tr>
                        <th>Code</th>
                        <th>Name</th>
                        <th>Integration Account</th>
                        <th>Accounts Order</th>
                        <th>Control Account</th>
                        <th>Account Type</th>
                        <th>Applicable to Class</th>
                        <th>Class of Business</th>
                        <th width="5%"></th>
                        <th width="5%"></th>
                    </tr>
                    </thead>
                </table>
                    </div>
            </div>
        </div>
        </div>
    </div>


<div class="modal fade" id="coaModal" tabindex="-1" role="dialog"
     aria-labelledby="coaModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="coaModalLabel">
                    Edit/Add Main Accounts
                </h4>
            </div>
            <div class="modal-body">
                <form id="coa-form" class="form-horizontal">
                    <input type="hidden" class="form-control" id="acc-code-pk" name="coId">

                    <div class="form-group">
                        <label for="unit-id" class="col-md-3 control-label">Code</label>

                        <div class="col-md-8">
                            <input type="text" class="form-control" id="acc-code-sht-desc"
                                   name="code"  required>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="unit-id" class="col-md-3 control-label">Name</label>

                        <div class="col-md-8">
                            <input type="text" class="form-control" id="acc-code-name"
                                   name="name"  required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="unit-id" class="col-md-3 control-label">Header</label>

                        <div class="col-md-8">
                            <input type="text" class="form-control" id="acc-code-header"
                                   name="header"  required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="unit-id" class="col-md-3 control-label">Footer</label>

                        <div class="col-md-8">
                            <input type="text" class="form-control" id="acc-code-footer"
                                   name="footer"  required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="unit-id" class="col-md-3 control-label">Account Type</label>

                        <div class="col-md-8">
                            <select class="form-control" id="sel3" name="accountType" required>
                                <option value="">Select Account Type</option>
                                <option value="I">Income</option>
                                <option value="E">Expense</option>
                                <option value="A">Asset</option>
                                <option value="L">Liability</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="unit-id" class="col-md-3 control-label">Balance Sheet/PL</label>

                        <div class="col-md-8">
                            <select class="form-control" id="blpl" name="plBalSheet" required>
                                <option value="">Select  Type</option>
                                <option value="B">Balance Sheet</option>
                                <option value="P">Profit and Loss</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="unit-id" class="col-md-3 control-label">Accounts Order</label>

                        <div class="col-md-8">
                            <input type="text" class="form-control" id="acc-code-order"
                                   name="accountsOrder"  required>
                        </div>
                    </div>

                </form>
            </div>
            <div class="modal-footer">
                <button data-loading-text="Saving..." id="saveCoaAccounts"
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

<div class="modal fade" id="subcoaModal" tabindex="-1" role="dialog"
     aria-labelledby="subcoaModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="subcoaModalLabel">
                    Edit/Add Sub Accounts
                </h4>
            </div>
            <div class="modal-body" id="branch_model">
                <form id="sub-coa-form" class="form-horizontal">
                    <input type="hidden" class="form-control" id="sub-acc-pk" name="coId">
                    <input type="hidden" class="form-control" id="sub-acc-main-pk" name="mainAccounts">

                    <div class="form-group">
                        <label for="coa-sub-code" class="col-md-3 control-label">Code</label>

                        <div class="col-md-8">
                            <input type="text" class="form-control" id="coa-sub-code"
                                   name="code"  required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="coa-sub-name" class="col-md-3 control-label">Name</label>

                        <div class="col-md-8">
                            <input type="text" class="form-control" id="coa-sub-name"
                                   name="name"  required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="integrat-acc" class="col-md-3 control-label">Integration Account?</label>

                        <div class="col-md-8">
                            <select class="form-control" id="integrat-acc" name="integration" required>
                                <option value="">Select</option>
                                <option value="Y">Yes</option>
                                <option value="N">No</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="sub-acc-code-order" class="col-md-3 control-label">Accounts Order</label>

                        <div class="col-md-8">
                            <input type="text" class="form-control" id="sub-acc-code-order"
                                   name="accountsOrder"  required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="control-acc" class="col-md-3 control-label">Control Account?</label>

                        <div class="col-md-8">
                            <select class="form-control" id="control-acc" name="controlAccount" required>
                                <option value="">Select</option>
                                <option value="Y">Yes</option>
                                <option value="N">No</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group control-acc" style="display: none">
                            <label for="brn-id" class="col-md-3 control-label">Account Type<span class="required">*</span></label>

                            <div class="col-md-8 col-xs-12">
                                <input type="hidden" id="acc-id" name="accountTypes"/>
                                <input type="hidden" id="acc-name">
                                <div id="accounttypes" class="form-control"
                                     select2-url="<c:url value="/protected/setups/selAcctTypes"/>" >

                                </div>
                            </div>
                    </div>
                    <div class="form-group">
                        <label for="business-class-acc" class="col-md-3 control-label">Class Applicable?</label>

                        <div class="col-md-8">
                            <select class="form-control" id="business-class-acc" name="mappedToSubclass" required>
                                <option value="">Select</option>
                                <option value="Y">Yes</option>
                                <option value="N">No</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group business-class-acc" style="display: none">
                        <label for="brn-id" class="col-md-3 control-label">Select Class</label>

                        <div class="col-md-8">
                            <input type="hidden" id="sub-code" name="subClassDef"/>
                            <input type="hidden" id="sub-desc"/>
                            <div id="sub-class-def" class="form-control"
                                 select2-url="<c:url value="/protected/setups/clauses/subclassSelect"/>">

                            </div>

                        </div>
                    </div>

                </form>
            </div>
            <div class="modal-footer">
                <button data-loading-text="Saving..." id="saveSubAccounts"
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