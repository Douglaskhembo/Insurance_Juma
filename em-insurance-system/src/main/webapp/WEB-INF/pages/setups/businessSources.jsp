<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script>
    var requestContextPath = '${pageContext.request.contextPath}';
</script>
<script type="text/javascript" src="<c:url value="/js/modules/setups/setups.js"/>"></script>
<div class="x_panel">
    <div class="x_title">
        <h4>Business Source Groups</h4>
    </div>

    <button type="button" class="btn btn-success btn btn-info pull-right" data-toggle="modal"
            data-target="#businessGroupModal">New</button>
    <table id="groupList" class="table table-hover table-bordered">
        <thead>
        <tr>
            <th width="30%">Short Desc</th>
            <th width="30%">Description</th>
            <th width="5%"></th>
            <th width="5%"></th>
        </tr>
        </thead>
    </table>
    <div class="x_title">
        <h4>Business Sources</h4>
    </div>

    <button type="button" class="btn btn-success btn btn-info pull-right" id="btn-add-source">New</button>

    <table id="sourceList" class="table table-hover table-bordered">
        <thead>
        <tr>

            <th width="30%">Short Desc</th>
            <th width="30%">Description</th>
            <th width="5%"></th>
            <th width="5%"></th>
        </tr>
        </thead>
    </table>

</div>
<jsp:include page="businesssourcemodals/busSourceModals.jsp"></jsp:include>