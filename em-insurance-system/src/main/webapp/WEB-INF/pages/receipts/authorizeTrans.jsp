<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript"
        src="<c:url value="/js/modules/utils/select2builder.js"/>"></script>
<script type="text/javascript"
        src="<c:url value="/libs/rivets/rivets.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/modules/receipts/authTrans.js"/>"></script>
<div class="x_panel">
        <div class="x_title">
            <h2>Accounts Transactions</h2>
            <ul class="nav navbar-right panel_toolbox">
                <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
                </li>
            </ul>
            <div class="clearfix"></div>
        </div>
 </div>
<div class="col-md-12 col-xs-12 table-responsive">
    <table id="accounts-tbl" class="table table-hover table-bordered">
        <thead>
        <tr>

            <th>Trans Date</th>
            <th>Account</th>
            <th>Ref No</th>
            <th>Tran Type</th>
            <th>Debit/Credit</th>
            <th>Amount</th>
            <th>Net Due</th>
            <th>Payee Name</th>
            <th></th>
        </tr>
        </thead>
    </table>
</div>