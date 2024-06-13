<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript" src="<c:url value="/js/modules/utils/select2builder.js"/>"></script>
<script type="text/javascript" src="<c:url value="/libs/rivets/rivets.js"/>"></script>
<script type="text/javascript"
	src="<c:url value="/js/modules/products/product.js"/>"></script>
<div class="x_panel">
   <div class="x_title">
		<h2>Product Groups</h2>
		<ul class="nav navbar-right panel_toolbox">
			<li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
			</li>
		</ul>
		<div class="clearfix"></div>
	</div>
	<div class="x_content">
	<form id="prg-grp-form" class="form-horizontal">
	    <div class="form-group form-required">
				<div class="col-md-6">
				   <label for="brn-id" class="col-md-5 control-label">Select
					Product Group</label>

				<div class="col-md-7">
		                     <input type="hidden" id="prg-id" rv-value="prggrp.prgCode"/>
		                     <input type="hidden" id="prg-name">
		                        <div id="prd-group" class="form-control" 
				                                 select2-url="<c:url value="/protected/setups/products/selprodgroups"/>" >
				                                 
				               </div> 
				               
				</div>
				</div>
				<div class="col-md-2">
				    <button type="button" class="btn btn-info" id="btn-add-group">New</button>
			        <button type="button" class="btn btn-info" id="btn-edit-group">Edit</button>
				    
				</div>
				
				<div class="col-md-2">
				    
				</div>
				
				</div>
			</form>
			</div>
		</div>
		<div class="x_panel">
		<div class="x_title">
		<h2>Products</h2>
		<ul class="nav navbar-right panel_toolbox">
			<li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
			</li>
		</ul>
		<div class="clearfix"></div>
	</div>
	<div class="x_content">
         <input type="hidden" id="prg-pk">
		<button type="button" class="btn btn-info" id="btn-add-prod">New</button>
		<div class="cutom-container">
		<table id="prodList" class="table table-hover table-bordered">
			<thead>
				<tr>

					<th>ID</th>
					<th>Product</th>
					<th>Policy Prefix</th>
					<th>Install Allowed</th>
					<th>Min Prem</th>
					<th>Risk Note Template</th>
					<th>Renewable?</th>
					<th>Active?</th>
					<th>Age Applicable?</th>
					<th width="5%"></th>
					<th width="5%"></th>
				</tr>
			</thead>
		</table>
			</div>
		<div class="x_title">
		<h4>Product Sub Classes</h4>
		</div>
         <input type="hidden" id="prg-prod-code">
		<button type="button" class="btn btn-info" id="btn-add-prod-sub">New</button>
		<div class="cutom-container">
		<table id="prodSubclassList" class="table table-hover table-bordered">
			<thead>
				<tr>

					<th>Subclass ID</th>
					<th>SubClass Name</th>
					<th>Mandatory</th>
					<th>Active</th>
					<th width="5%"></th>
					<th width="5%"></th>
				</tr>
			</thead>
		</table>
			</div>
	</div>
	</div>
	<jsp:include page="prodmodals/modals.jsp"></jsp:include>