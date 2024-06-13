<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript" src="<c:url value="/js/modules/utils/select2builder.js"/>"></script>
<div class="x_panel">
    <div class="x_title">
        <h4>Prospects List</h4>
    </div>

    <button id="btn-add-prs" class="btn btn-info pull-right">New</button>
    <div class="cutom-container">
    <table id="tenTbl" class="table table-hover table-bordered">
        <thead>
        <tr>
            <th>Prospect ID</th>
            <th>Name</th>
            <th>Phone</th>
            <th>Prospect Type</th>
            <th>Date of Birth</th>
            <th>Status</th>
            <th>Category</th>
            <th>Branch</th>
            <th>Sub Agent</th>
            <th>Comment</th>
            <th width="5%"></th>
            <th width="5%"></th>
        </tr>
        </thead>
    </table>
        </div>
</div>

<jsp:include page="../modals/prospectmodals.jsp"></jsp:include>
<script type="text/javascript" src="<c:url value="/js/modules/clients/prospectsHelper.js"/>"></script>

<script>
    /**
     *
     */

    $(function() {

        $(document).ready(function () {

            PROSPECTS_UTILITIES.createProspectList();
            PROSPECTS_UTILITIES.addProspects("P");
            PROSPECTS_UTILITIES.populateClientTypeLov();

            $(".datepicker-input").each(function() {
                $(this).datetimepicker({
                    format: 'DD/MM/YYYY'
                });

            });


        });

    });
</script>