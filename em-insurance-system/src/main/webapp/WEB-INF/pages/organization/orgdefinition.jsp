<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script>
    var action = '${organization.formAction}';
</script>
<style>
	.checkout-step {

		border-top: 1px solid #f2f2f2;
		color: #AF144B;
		font-size: 14px;
		padding: 30px;
		position: relative;
	}

	.checkout{    background-color: #fff;
		border:1px solid #eaefe9;

		font-size: 14px;}
</style>
<script type="text/javascript"
	src="<c:url value="/js/modules/organization/organization.js"/>"></script>

                <div id="organization_model">
                
                <div>
                  <div id="accordion" class="checkout">
                    <!-- we are adding the .panel class so bootstrap.js collapse plugin detects it -->
                    <div class="panel panel-default checkout-step">
                    <div class="panel-heading">
                      <h4 class="panel-title">
                        <a data-toggle="collapse" data-parent="#accordion" href="#collapseOne">
                          Organization Definition
                        </a>
                      </h4>
                    </div>
                    <div id="collapseOne" class="panel-collapse collapse in">
                      <div class="panel-body">
                          <h4>Organization Details</h4>
                          
                          
                          <c:choose>
						    <c:when test="${organization.formAction=='A'}">
						        <jsp:include page="orgForm.jsp"></jsp:include>
						    </c:when>
						    <c:otherwise>
						         <jsp:include page="orgFormDetails.jsp"></jsp:include>
						    </c:otherwise>
						</c:choose>
                      </div>
                    </div>
                  </div>
                  <div class="panel panel-default checkout-step">
                    <div class="panel-heading">
                      <h4 class="panel-title">
                        <a data-toggle="collapse" data-parent="#accordion" href="#collapseTwo">
                          Organization Regions
                        </a>
                      </h4>
                    </div>
                    <div id="collapseTwo" class="panel-collapse collapse">
                      <div class="panel-body">
                        
                         <div class="tab-content">
                           <div class="tab-pane active" id="tab_1">
                               <h4>Region Details</h4>
		                         <div class="spacer"></div>
		                          <c:choose>
						       <c:when test="${organization.formAction=='A'}">
		                           <button type="button" class="btn btn-info" data-toggle="modal" data-target="#regModal">New</button>
		                        </c:when>
		                        </c:choose>
		                        <input type="hidden" id="selOrgReg">
		                         <hr>
		                         <div class="x_content">
									 <div class="cutom-container">
		                          <table id="orgRegion" class="table table-hover table-bordered" width="100%"
									>
									<thead>
										<tr>
											
											<th>Region ID</th>
											<th>Region Name</th>
											<th>WEF</th>
											<th>WET</th>
											<th width="5%"> </th>
											<th width="5%"> </th>
										</tr>
									</thead>
								</table>
										 </div>
								</div>
								<div class="spacer"></div>
								 <h4>Branch Details</h4>
		                         <div class="spacer"></div>
		                          <c:choose>
						        <c:when test="${organization.formAction=='A'}">
		                          <button type="button" class="btn btn-info" data-toggle="modal" data-target="#branchModal">New</button>
		                        </c:when>
		                        </c:choose>
		                        <hr>
							   <div class="cutom-container">
		                          <table id="orgBranches" class="table table-hover table-bordered"
									>
									<thead>
										<tr>
											
											<th>Branch ID</th>
											<th>Branch Name</th>
											<th>Address</th>
											<th>Tel. Number</th>
											<th>Branch Manager</th>
											<th>Is Head Office</th>
											<th width="5%"> </th>
											<th width="5%"> </th>
										</tr>
									</thead>
								</table>
								   </div>
                           </div>
                          
                          </div>
                      
                        
                      </div>
                    </div>
                  </div>
                  
                 
                 
                </div>
                     <button type="button" class="btn btn-info" data-toggle="modal" data-target="#reportModal">Report</button>
		                       
                </div>
                </div> 
                 <jsp:include page="modals.jsp"></jsp:include>