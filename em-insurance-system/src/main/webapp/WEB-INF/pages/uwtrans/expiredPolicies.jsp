<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript" src="<c:url value="/js/modules/utils/select2builder.js"/>"></script>
<script type="text/javascript" src="<c:url value="/libs/rivets/rivets.js"/>"></script>
<div class="x_panel">
    <div class="x_title">
        <h4>Expired Policies Enquiry</h4>
    </div>
    <div class="cutom-container">
        <table id="quot_enquiry" class="table table-hover table-bordered">
            <thead>
            <tr>
                <th>Policy No</th>
                <th>Premium</th>
                <th>Cover From</th>
                <th>Cover To</th>
                <th>Authorised By</th>
                <th>Authorised Date</th>
                <th></th>
            </tr>
            </thead>
        </table>
        </div>
</div>

<script>
    $(function() {

        $(document).ready(function () {
            getExpiredPolicies();
        });

        function getExpiredPolicies(){

                var url = SERVLET_CONTEXT+"/protected/home/expiredPolicies";
            return $('#quot_enquiry').DataTable({
                    "processing": true,
                    "serverSide": true,
                    autoWidth: true,
                    dom: "Bfrtip",
                    "deferRender": true,
                    "ajax": {
                        'url': url
                    },
                    buttons: [
                        'excel', 'pdf', 'print'
                    ],
                    lengthMenu: [[10, 15, 20], [10, 15, 20]],
                    pageLength: 10,
                    destroy: true,
                    "columns": [
                        {"data": "polNo"},
                        {
                            "data": "premium",
                            "render": function (data, type, full, meta) {
                                return UTILITIES.currencyFormat(full.premium);
                            }
                        },
                        {
                            "data": "wef",
                            "render": function (data, type, full, meta) {
                                return moment(full.wef).format('DD/MM/YYYY');
                            }
                        },
                        {
                            "data": "wet",
                            "render": function (data, type, full, meta) {
                                return moment(full.wet).format('DD/MM/YYYY');
                            }
                        },
                        {"data": "user"},
                        {
                            "data": "authDate",
                            "render": function (data, type, full, meta) {
                                return moment(full.authDate).format('DD/MM/YYYY');
                            }
                        },
                        {
                            "data": "transactionId",
                            "render": function ( data, type, full, meta ) {
                                    return '<form action="edituwtrans" method="post"><input type="hidden" name="id" value=' + full.transactionId + '><input type="submit"  class="btn btn-success btn btn-info btn-xs" value="View" ></form>';

                            }

                        },
                    ]
                });
        }

    });
</script>