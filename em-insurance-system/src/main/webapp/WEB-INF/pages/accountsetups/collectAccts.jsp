<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript" src="<c:url value="/js/modules/utils/select2builder.js"/>"></script>
<script type="text/javascript" src="<c:url value="/libs/rivets/rivets.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/modules/accountsetups/collAccts.js"/>"></script>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div class="x_panel" id="acct_model">
    <div class="x_title">
        <h4>Collection Accounts List</h4>
    </div>
    <form class="form-horizontal">
        <div class="form-group">
            <label for="brn-id" class="col-md-3 control-label">Select
                Payment Mode</label>

            <div class="col-md-4">
                <input type="hidden" id="pmId"/>
                <div id="payment-mode" class="form-control"
                     select2-url="<c:url value="/protected/accounts/selPaymentModes"/>" >

                </div>
            </div>
        </div>

    </form>
    <button class="btn btn-success btn btn-info pull-left" id="btn-add-coll-acct">New</button>
        <table id="collAcctsTbl" class="table table-hover table-bordered">
            <thead>
            <tr>
                <th>Name</th>
                <th>Account</th>
                <th>Bank</th>
                <th>Bank Branch</th>
                <th>Currency</th>
                <th>Active</th>
                <th width="5%"></th>
                <th width="5%"></th>
            </tr>
            </thead>
        </table>
    </div>

<div class="modal fade" id="collectAcctsModal" tabindex="-1" role="dialog"
     aria-labelledby="collectAcctsModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="collectAcctsModalLabel">
                    Edit/Add Collection Account
                </h4>
            </div>
            <div class="modal-body">
                <form id="collect-acct-form" class="form-horizontal">
                    <input type="hidden" class="form-control" id="ca-id" name="caId">
                    <input type="hidden" class="form-control" id="ca-payment-modes" name="paymentModes">
                    <div class="form-group">
                        <label for="unit-id" class="col-md-3 control-label">Collection Name</label>

                        <div class="col-md-8">
                            <input type="text" class="form-control" id="collect-name"
                                   name="name"  required>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="unit-id" class="col-md-3 control-label">Account</label>

                        <div class="col-md-8">
                            <input type="hidden" id="acct-code"/>
                            <input type="hidden" id="acct-name"/>
                            <input type="hidden" id="acct-id" name="accounts"/>
                            <div id="acct-form" class="form-control"
                                 select2-url="<c:url value="/protected/setups/revitems/selGlAccount"/>" >
                                </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="unit-id" class="col-md-3 control-label">Bank Branch</label>

                        <div class="col-md-8">
                            <input type="hidden" id="bbr-desc"/>
                            <input type="hidden" id="bank-br-id" name="bankBranches"/>
                            <div id="bank-branch-form" class="form-control"
                                 select2-url="<c:url value="/protected/accounts/selBankBranches"/>" >
                                </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="unit-id" class="col-md-3 control-label">Currency</label>

                        <div class="col-md-8">
                            <input type="hidden" id="curr-desc"/>
                            <input type="hidden" id="curr-id" name="currencies"/>
                            <div id="currency-form" class="form-control"
                                 select2-url="<c:url value="/protected/organization/currencies"/>" >
                                </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="rate-taxable" class="col-md-3 control-label">Active</label>
                        <div class="col-md-9 checkbox">
                            <label>
                                <input type="checkbox" name="status" id="acct-status">
                            </label>
                        </div>
                    </div>

                </form>
            </div>
            <div class="modal-footer">
                <button data-loading-text="Saving..." id="saveCollectAcct"
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