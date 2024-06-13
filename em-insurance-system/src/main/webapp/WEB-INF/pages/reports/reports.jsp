<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<script type="text/javascript" src="<c:url value="/js/modules/utils/select2builder.js"/>"></script>
<script type="text/javascript" src="<c:url value="/libs/rivets/rivets.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/modules/reports/report.js"/>"></script>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<script>
    var requestContextPath = '${pageContext.request.contextPath}';
</script>
<div class="x_panel">
    <div class="x_title">
        <h2>${reportName}</h2>
        <ul class="nav navbar-right panel_toolbox">
            <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
            </li>
        </ul>
        <div class="clearfix"></div>
    </div>
    <div class="x_content">
           <c:forEach var="module" items="${modules}">
               <c:set var="search" value="'" />
               <div class="col-md-6">

                   <ul style="list-style-type: none;" class="reports-links">
                       <sec:authorize access="hasAnyAuthority('${module.permissionsDef.permName}')">
                       <li><a data-report="${module.rptId}" data-template="${module.rptTemplateName}" data-url="<c:url value='/protected/reports/report/${module.rptTemplateName}'/> " onclick="reportModule.callReportModal(this);"
                               href="#">${module.rptName}</a></li>
                       </sec:authorize>
                   </ul>
               </div>
           </c:forEach>

        </div>
    </div>

<div class="modal fade" id="printReportModal" tabindex="-1" role="dialog"
     aria-labelledby="printReportModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
         <form:form id="report-form" target="_blank"  class="form-horizontal" modelAttribute="reportData" action="printReport" method="post">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="printReportModalLabel">
                    Print Report
                </h4>
            </div>
            <div class="modal-body">

                           <div class="data-inf">

                           </div>
                       
                  

            </div>
            <div class="modal-footer">
                 <input type="submit" value="Print" class="btn btn-success">
                <button type="button" class="btn btn-default" data-dismiss="modal">
                    Cancel
                </button>
            </div>
              </form:form>
        </div>
    </div>
</div>