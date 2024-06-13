<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<script type="text/javascript"
        src="<c:url value="/js/modules/utils/select2builder.js"/>"></script>
<script type="text/javascript"
        src="<c:url value="/libs/rivets/rivets.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/modules/receipts/receipts.js"/>"></script>

<div class="container">
    <div class="x_panel">
        <a href="<c:url value='/protected/uw/receipts/receiptList'/> "
           class="btn btn-primary pull-right" style="margin-right: 10px;">Back</a>
        <sec:authorize access="hasAnyAuthority('CREATE_RECEIPT')">
            <input type="button" class="btn btn-primary pull-right"
                   style="margin-right: 10px;" value="Print" id="btn-add-receipt">
            <input type="button" id="btn-print-receipt"
                   class="btn btn-primary pull-right" style="margin-right: 10px;"
                   value="Save Unprinted">
        </sec:authorize>
        <div class="x_title">
            <h2>Receipt Entry</h2>
            <%--<ul class="nav navbar-right panel_toolbox">--%>
            <%--<li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>--%>
            <%--</li>--%>
            <%--</ul>--%>
            <div class="clearfix"></div>
        </div>
    </div>

    <div class="x_panel">
        <form id="receipt-form" class="form-horizontal">
            <div class="form-group" style="display: none">
                <input type=" hidden" id="receipt-type" name="receiptType" class="form-control">
            </div>

            <div class="form-group form-required col-md-6 col-xs-12">
                <label for="rec-type" class="control-label col-md-3 col-xs-12"> Receipt Type: <span
                        class="required">*</span></label>
                <div class="col-md-6 col-xs-12">
                    <select class="form-control" id="rec-type" name="selreceiptType" required>
                        <option value="">Select Receipt Type</option>
                        <option value="N">General Insurance</option>
                        <option value="L">Life Insurance</option>
                    </select>
                </div>
                <div class="col-md-3">
                </div>
            </div>

            <div class="form-group form-required col-md-6 col-xs-12">
                <label for="coll-id" class="col-md-3 col-xs-12  control-label">Collection Account<span
                        class="required">*</span>
                </label>
                <div class="col-md-6 col-xs-12">
                    <input type="hidden" id="coll-id" name="payId"/>
                    <div id="coll-div" class="form-control"
                         select2-url="<c:url value="/protected/uw/receipts/collectAccts"/>">
                    </div>
                </div>
                <div class="col-md-3">
                </div>
            </div>

<%--            <div class="form-group form-required col-md-6 col-xs-12 insurance-co" style="display: none">--%>
<%--                <label for="insurance-div" class="col-md-3 col-xs-12 control-label">Intermediary<span--%>
<%--                        class="required">*</span>--%>
<%--                </label>--%>
<%--                <div class="col-md-6 col-xs-12">--%>
<%--                    <input type="hidden" id="insurance-id" name="insuranceId"/>--%>
<%--                    <div id="insurance-div" class="form-control"--%>
<%--                         select2-url="<c:url value="/protected/setups/binders/selAccounts"/>">--%>
<%--                    </div>--%>
<%--                </div>--%>
<%--                <div class="col-md-3">--%>
<%--                </div>--%>
<%--            </div>--%>



            <div class="form-group form-required col-md-6 col-xs-12">
                <label for="pymt-mode" class="control-label col-md-3 col-xs-12">Payment Mode</label>
                <div class="col-md-6 col-xs-12">
                    <p class="form-control-static" id="pymt-mode"></p>
                </div>
                <div class="col-md-3">
                </div>
            </div>

            <div class="form-group form-required col-md-6 col-xs-12">
                <label for="bank-desc" class="control-label col-md-3 col-xs-12">Currency
                </label>
                <div class="col-md-6 col-xs-12">
                    <p class="form-control-static" id="currency-desc"></p>
                </div>
                <div class="col-md-3">
                </div>
            </div>

            <div class="form-group form-required col-md-6 col-xs-12">
                <label for="bank-desc" class="control-label col-md-3 col-xs-12">Bank Info<span class="required">*</span>
                </label>
                <div class="col-md-6 col-xs-12">
                    <p readonly class="form-control-static" id="bank-desc"></p>
                </div>
                <div class="col-md-3">
                </div>
            </div>


            <div class="form-group form-required col-md-6 col-xs-12">
                <label for="bank-desc" class="control-label col-md-3 col-xs-12">Branch
                </label>
                <div class="col-md-6 col-xs-12">
                    <input type="hidden" id="brn-id" name="brnCode"/>
                    <div id="brn-frm" class="form-control"
                         select2-url="<c:url value="/protected/uw/policies/uwbranches"/>">
                    </div>
                </div>
                <div class="col-md-3">
                </div>
            </div>


            <div class="form-group form-required col-md-6 col-xs-12">
                <label for="rct-amount" class="control-label col-md-3 col-xs-12">Amount<span class="required">*</span>
                </label>
                <div class="col-md-6 col-xs-12">
                    <input type="text" class="form-control" id="rct-amount"
                           name="receiptAmount" required>
                </div>
                <div class="col-md-3">
                </div>
            </div>
            <div class="form-group form-required col-md-6 col-xs-12">
                <label for="paid-by" class="control-label col-md-3 col-xs-12">Paid By<span class="required">*</span>
                </label>
                <div class="col-md-6 col-xs-12">
                    <input type="text" class="form-control" id="paid-by"
                           name="paidBy" required>
                </div>
                <div class="col-md-3">
                </div>
            </div>
            <div class="form-group form-required col-md-6 col-xs-12">
                <label for="narration" class="control-label col-md-3 col-xs-12">Narration<span class="required">*</span>
                </label>
                <div class="col-md-6 col-xs-12">
                    <textarea rows="2" cols=30 class="form-control" name="receiptDesc" id="narration"
                              REQUIRED></textarea>
                </div>
                <div class="col-md-3">
                </div>
            </div>
            <div class="form-group form-required col-md-6 col-xs-12">
                <label for="receipt-date" class="control-label col-md-3 col-xs-12">Receipt Date<span class="required">*</span></label>
                <div class="col-md-6 col-xs-12">
                    <div class='input-group date datepicker-input'>
                        <input type='text' class="form-control" name="receiptDate" id="receipt-date"/>
                        <span class="input-group-addon">
                        <span class="glyphicon glyphicon-calendar"></span>
                    </span>
                    </div>
                    <div class="col-md-3">
                    </div>
                </div>
                <div class="col-md-3">
                </div>
            </div>
            <div class="form-group form-required col-md-6 col-xs-12">
                <label for="payment-ref" class="control-label col-md-3 col-xs-12">Reference</label>

                <div class="col-md-6 col-xs-12">
                    <input type="text" class="form-control" id="payment-ref"
                           name="paymentRef">
                </div>
                <div class="col-md-3">
                </div>
            </div>
            <div class="form-group form-required col-md-6 col-xs-12">
                <label for="doc-date" class="control-label col-md-3 col-xs-12">Document Date</label>

                <div class="col-md-6 col-xs-12">
                    <div class='input-group date datepicker-input'>
                        <input type='text' class="form-control" name="documentDate" id="doc-date"/>
                        <span class="input-group-addon">
                        <span class="glyphicon glyphicon-calendar"></span>
                    </span>
                    </div>
                    <div class="col-md-3">
                    </div>
                </div>
                <div class="col-md-3">
                </div>
            </div>
            <div class="form-group form-required col-md-6 col-xs-12">
                <label for="manual-ref" class="control-label col-md-3 col-xs-12">Manual Ref </label>
                <div class="col-md-6 col-xs-12">
                    <input type="text" class="form-control" id="manual-ref" name="manualRef">
                </div>
                <div class="col-md-3">
                </div>
            </div>
<%--            <div class="form-group form-required col-md-6 col-xs-12">--%>
<%--                <label for="chk-cl-drreceipt" class="control-label col-md-3 col-xs-12"> Direct Receipt?</label>--%>
<%--                <div class="col-md-6 col-xs-12 checkbox">--%>
<%--                    <label>--%>
<%--                        <input type="checkbox" name="directReceipt" id="chk-cl-drreceipt">--%>
<%--                    </label>--%>
<%--                </div>--%>
<%--                <div class="col-md-3">--%>
<%--                </div>--%>
<%--            </div>--%>
<%--            <div class="form-group form-required col-md-6 col-xs-12">--%>
<%--                <label for="chk-fund-receipt" class="control-label col-md-3 col-xs-12">Fund Receipt?</label>--%>
<%--                <div class="col-md-6 col-xs-12 checkbox">--%>
<%--                    <label>--%>
<%--                        <input type="checkbox" name="fundReceipt" id="chk-fund-receipt">--%>
<%--                    </label>--%>
<%--                </div>--%>
<%--                <div class="col-md-3">--%>
<%--                </div>--%>
<%--            </div>--%>
        </form>
    </div>
    <div class="x_panel">
        <div id="rates-details-div">
            <input type="button" id="add-det-btn"
                   class="btn btn-primary pull-left" style="margin-right: 10px;"
                   value="Add">
        </div>
        <div class="box-body">
            <div class="cutom-container">
                <table id="rct-detail-tbl" class="table table-hover table-bordered">
                    <thead>
                    <tr>

                        <th>Trans No</th>
                        <th>Policy Number</th>
                        <th>Date</th>
                        <th>Client</th>
                        <th>Balance</th>
                        <th>Allocated Amount</th>
                        <th></th>
                    </tr>
                    </thead>

                </table>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="clientTransModal" tabindex="-1" role="dialog"
     aria-labelledby="clientTransModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="clientTransModalLabel">
                    Add new Debit Transaction
                </h4>
            </div>
            <div class="modal-body">
                <form id="search-form" class="form-horizontal pull-left">
                    <div class="form-group">

                        <div class="col-md-12">
                            <label for="brn-id" class="col-md-6 control-label">Debit
                                Number</label>

                            <div class="col-md-6">
                                <input type='text' class="form-control pull-right"
                                       id="inv-search-number"/>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-md-12">
                            <label for="brn-id" class="col-md-6 control-label">Client
                                First Name</label>

                            <div class="col-md-6">
                                <input type='text' class="form-control pull-right"
                                       id="inv-search-name"/>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-md-12">
                            <label for="brn-id" class="col-md-6 control-label">Client
                                Other Names</label>

                            <div class="col-md-6">
                                <input type='text' class="form-control pull-right"
                                       id="inv-search-other-names"/>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <input type="button" class="btn btn-info pull-right"
                               style="margin-right: 10px;" value="Search"
                               id="btn-search-invoice">
                    </div>


                </form>
                <div class="cutom-container">
                    <table id="modal-rct-detail-tbl" class="table table-hover table-bordered">
                        <thead>
                        <tr>
                            <th></th>
                            <th>Trans No</th>
                            <th>Ref No</th>
                            <th>Date</th>
                            <th>Client</th>
                            <th>Amount</th>
                            <th>Balance</th>

                        </tr>
                        </thead>
                    </table>
                </div>
            </div>
            <div class="modal-footer">
                <button data-loading-text="Saving..." id="btn-add-selected-exit"
                        type="button" class="btn btn-primary">
                    Add Selected and Close
                </button>
                <button data-loading-text="Saving..." id="btn-add-selected"
                        type="button" class="btn btn-primary">
                    Add Selected
                </button>
                <button type="button" class="btn btn-default" data-dismiss="modal">
                    Cancel
                </button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="printReceiptModal" tabindex="-1" role="dialog"
     aria-labelledby="printReceiptModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="printReceiptModalLabel">
                    Print Receipt
                </h4>
            </div>
            <div class="modal-body">
                <input type="hidden" id="print-receipt-id">
                <div class="media">
                    <iframe class="col-lg-12 col-md-12 col-sm-12" id="receipt-div" height="600">

                    </iframe>
                </div>

            </div>
            <div class="modal-footer">
                <button id="printReceipt"
                        type="button" class="btn btn-success">
                    Printed Successfully
                </button>
                <button type="button" class="btn btn-success" data-dismiss="modal">
                    Receipt Not Printed
                </button>
            </div>
        </div>
    </div>
</div>