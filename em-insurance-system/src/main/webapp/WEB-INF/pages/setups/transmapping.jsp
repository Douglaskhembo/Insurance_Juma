<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script type="text/javascript" src="<c:url value="/js/modules/setups/transmapping.js"/>"></script>

<div class="x_panel">
    <button class="btn btn-success btn btn-info pull-right" id="btn-add-mapping">New</button>
    <div class="x_title">
        <h4>Transaction Mapping</h4>
    </div>
    <div class="cutom-container">
    <table id="trans-mapping-tbl" class="table table-hover table-bordered">
        <thead>
        <tr>

            <th>Transaction Type</th>
            <th>Debit Code</th>
            <th>Credit Code</th>
            <th width="5%"></th>
            <th width="5%"></th>
        </tr>
        </thead>
    </table>
        </div>
</div>
<div class="modal fade" id="transMappingModal" tabindex="-1" role="dialog"
     aria-labelledby="transMappingModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="transMappingModalLabel">
                    Edit/Add Transaction Types Mapping
                </h4>
            </div>
            <div class="modal-body">
                <form id="mapping-form" class="form-horizontal">
                    <input type="hidden" class="form-control" id="mapping-id-pk" name="tmNo">

                    <div class="form-group">
                        <label for="unit-id" class="col-md-3 control-label">Transaction Type</label>

                        <div class="col-md-8">
                            <select class="form-control" id="trans-type" name="transType" required>
                                <option value="">Select Transaction Type</option>
                                <option value="NB">New Business</option>
                                <option value="RN">Renewal</option>
                                <option value="EN">Endorsement</option>
                                <option value="RF">Refund</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="unit-id" class="col-md-3 control-label">Debit Code</label>

                        <div class="col-md-8">
                            <input type="text" class="form-control" id="debit-code"
                                   name="debitCode"  required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="unit-id" class="col-md-3 control-label">Credit Code</label>

                        <div class="col-md-8">
                            <input type="text" class="form-control" id="credit-code"
                                   name="creditCode"  required>
                        </div>
                    </div>

                </form>
            </div>
            <div class="modal-footer">
                <button data-loading-text="Saving..." id="saveMapping"
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
