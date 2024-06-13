<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript"
        src="<c:url value="/js/modules/utils/select2builder.js"/>"></script>
<script type="text/javascript"
        src="<c:url value="/libs/rivets/rivets.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/modules/receipts/creditor.js"/>"></script>
<div class="x_panel">
    <div class="x_title">
        <h2>Creditor Payments</h2>
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
                <label for="brn-id" class="col-md-5 control-label">Intermediary
                </label>

                <div class="col-md-7 col-xs-12">
                    <input type='hidden' class="form-control pull-right"
                           id="agent-search-number" name="accountCode" />
                    <div id="acc-frm" class="form-control"
                         select2-url="<c:url value="/protected/setups/binders/selAccounts"/>" >
                    </div>
                </div>
            </div>
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
        </div>
        <div class="form-group">
            <div class="col-md-6 col-xs-12">
                <label for="brn-id" class="col-md-5 control-label">Payment Status
                </label>

                <div class="col-md-7 col-xs-12">
                    <select class="form-control" id="payment-status" name="paymentStatus"
                            required>
                        <option value="ALL">All</option>
                        <option value="FULL">Full Paid</option>
                        <option value="PARTIAL">Partial Paid</option>
                    </select>
                </div>
            </div>
            <div class="col-md-6 col-xs-12">

            </div>
        </div>
        <div class="form-group">
            <input type="button" class="btn btn-primary pull-right"
                   style="margin-right: 10px;" value="Search"
                   id="btn-search-trans">
            <a class="btn btn-primary pull-right"
                    href="<c:url value='/protected/accounts/rpt_inspayment_prelist.pdf'/> "
                    target="_blank">Insurance payment Prelist</a>
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
            <th>CR. No.</th>
            <th>Payment</th>
            <th>Premium</th>
            <th>Commission</th>
            <th>WHTX</th>
            <th>Settlement</th>
            <th>DR Bal.</th>
            <th>Alloc. Amt.</th>
            <th>Payable Amt</th>
            <th></th>
        </tr>
        </thead>
    </table>
            </div>
  <span class="pull-right"> <input type="text" class="form-control total" disabled></span>
    <input type="hidden" id="total-val" value="0">
    <button class="btn btn-primary pull-right" id="process_trans">Process Transactions</button>
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
            <th>Insurance ID</th>
            <th>Intermediary</th>
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
            <th>Policy No</th>
            <th>Client Name</th>
            <th>Client Account</th>
            <th>Product</th>
            <th>Amount</th>
            <th>Commission</th>
            <th>WHTX</th>
            <th></th>
        </tr>
        </thead>
    </table>
            </div>
        </div>
</div>



