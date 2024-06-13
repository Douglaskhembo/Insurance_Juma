<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript"
        src="<c:url value="/js/modules/utils/select2builder.js"/>"></script>
<script type="text/javascript"
        src="<c:url value="/libs/rivets/rivets.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/modules/receipts/commissionpymt.js"/>"></script>
<div class="x_panel">
    <div class="x_title">
        <h2>Sub Agent Commission Payments</h2>
        <ul class="nav navbar-right panel_toolbox">
            <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
            </li>
        </ul>
        <div class="clearfix"></div>
    </div>
    <div class="x_content">
        <form id="credits-form" class="form-horizontal">
            <div class="form-group">

                <div class="col-md-6 col-xs-12">
                    <label for="brn-id" class="col-md-5 control-label">Date
                        From</label>

                    <div class="col-md-7 col-xs-12">
                        <div class='input-group date datepicker-input' id="wef-date">
                            <input type='text' class="form-control pull-right" name="wefDate"
                                   id="from-date" required />
                            <div class="input-group-addon">
                                <span class="glyphicon glyphicon-calendar"></span>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-6 col-xs-12">
                    <label for="brn-id" class="col-md-5 control-label">Date To
                    </label>

                    <div class="col-md-7 col-xs-12">
                        <div class='input-group date datepicker-input' id="cover-to-date">
                            <input type='text' class="form-control pull-right" name="wetDate"
                                   id="wet-date" required />
                            <div class="input-group-addon">
                                <span class="glyphicon glyphicon-calendar"></span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <div class="col-md-6 col-xs-12">
                    <label for="brn-id" class="col-md-5 control-label">Sub Agent
                    </label>

                    <div class="col-md-7 col-xs-12">
                        <input type='hidden' class="form-control pull-right"
                               id="agent-search-number" name="accountCode" />
                        <div id="acc-frm" class="form-control"
                             select2-url="<c:url value="/protected/accounts/subagentAccounts"/>" >
                        </div>
                    </div>
                </div>
                <div class="col-md-6 col-xs-12">
                    <label for="brn-id" class="col-md-5 control-label">Marketer Type</label>
                    <div class="col-md-7">
                        <input type="hidden" id="sub-id" name="subaccountType"/>
                        <input type="hidden" id="sub-acct-id" name="accountDef"/>
                        <input type="hidden" id="sub-name">
                        <div id="sub-account-frm" class="form-control"
                             select2-url="<c:url value="/protected/users/subaccounts"/>" >

                        </div>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <div class="col-md-6 col-xs-12">
                    <label for="brn-id" class="col-md-5 control-label">Currency
                    </label>

                    <div class="col-md-7 col-xs-12">
                        <input type='hidden' class="form-control pull-right"
                               id="cur-id" name="curCode" />
                        <div id="curr-frm" class="form-control"
                             select2-url="<c:url value="/protected/uw/policies/uwcurrencies"/>" >
                        </div>
                    </div>
                </div>
                <div class="col-md-6 col-xs-12">

                </div>
            </div>
            <div class="form-group">
                <input type="button" class="btn btn-info pull-right"
                       style="margin-right: 10px;" value="Search"
                       id="btn-search-trans">
            </div>

        </form>
        <div class="clearfix"></div>
        <div class="card-box table-responsive" style="height: 300px !important; overflow: scroll;">
            <table id="credits-tbl" class="table table-hover table-bordered">
                <thead>
                <tr>
                    <th>Policy</th>
                    <th>Client</th>
                    <th>DR. No.</th>
                    <th>Commission</th>
                    <th>Settlement</th>
                    <th>DR Bal.</th>
                    <th>Sub Agent Commission</th>
                    <th>Payable Amt</th>
                    <th></th>
                </tr>
                </thead>
            </table>
        </div>
        <span class="pull-right"> <input type="text" class="form-control total" disabled></span>
        <input type="hidden" id="total-val" value="0">
        <button class="btn btn-info pull-right" id="process_trans">Process Transactions</button>
    </div>
</div>
<div class="x_panel">
    <div class="x_title">
        <h2>Transactions</h2>
        <ul class="nav navbar-right panel_toolbox">
            <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
            </li>
        </ul>
        <div class="clearfix"></div>
    </div>
    <div class="x_content">
        <div class="card-box table-responsive">
            <table id="instrans-tbl" class="table table-hover table-bordered">
                <thead>
                <tr>

                    <th>Trans Date</th>
                    <th>Sub Agent ID</th>
                    <th>Sub Agent</th>
                    <th>Account</th>
                    <th>Amount</th>
                    <th>Remittance No</th>
                    <th></th>
                </tr>
                </thead>
            </table>
        </div>
    </div>
</div>

<div class="x_panel">
    <div class="x_title">
        <h2>Transaction Log</h2>
        <ul class="nav navbar-right panel_toolbox">
            <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
            </li>
        </ul>
        <div class="clearfix"></div>
    </div>
    <div class="x_content">
        <div class="card-box table-responsive">
            <table id="payment-trans-tbl" class="table table-hover table-bordered">
                <thead>
                <tr>

                    <th>Reference No</th>
                    <th>Amount</th>
                    <th></th>
                </tr>
                </thead>
            </table>
        </div>
    </div>
</div>



