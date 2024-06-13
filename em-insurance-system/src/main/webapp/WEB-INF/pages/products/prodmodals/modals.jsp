<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="modal fade" id="prodGrpModal" tabindex="-1" role="dialog"
		aria-labelledby="prodGrpModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="prodGrpModalLabel">
						Add Product Group
					</h4>
				</div>
				<div class="modal-body">
				   
					<form id="prg-group-form" class="form-horizontal">
						<input type="hidden" class="form-control" id="prg-code" name="prgCode">
						<div class="form-group">
							<label for="brn-id" class="col-md-3 control-label">Product Group</label>

							<div class="col-md-8">
								<input type="text" class="form-control" id="grp-desc"
									name="prgDesc"  required>
							</div>
						</div>
						<div class="form-group">
							<label for="cou-name" class="col-md-3 control-label">Type</label>

							<div class="col-md-8">
								 <select class="form-control" id="prg-type" name="prgType">
							        <option value="">Select Product Group</option>
							        <option value="M">Motor</option>
								    <option value="F">Non Motor</option>
								    <option value="MD">Medical</option>
								    <option value="T">Travel</option>
									 <option value="L">Life</option>
									 <option value="MP">Multi Product</option>
								  </select>
							</div>
						</div>	
						
						
					</form>
				</div>
				<div class="modal-footer">
				    <button  id="delproductGrpBtn"
						type="button" class="btn btn-danger">
						Delete
					</button>
					<button data-loading-text="Saving..." id="saveproductGrpBtn"
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
<div class="modal fade" id="prodRptGrpModal" tabindex="-1" role="dialog"
	 aria-labelledby="prodRptGrpModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="prodRptGrpModalLabel">
					Add Product Report Group
				</h4>
			</div>
			<div class="modal-body">

				<form id="prgrpt-group-form" class="form-horizontal">
					<input type="hidden" class="form-control" id="prgrptId" name="rptId">
					<div class="form-group">
						<label for="brn-id" class="col-md-3 control-label">Report Group Name</label>

						<div class="col-md-8">
							<input type="text" class="form-control" id="rpt-desc"
								   name="repDesc"  required>
						</div>
					</div>
					<div class="form-group">
						<label for="brn-id" class="col-md-3 control-label">Report Group Desc</label>

						<div class="col-md-8">
							<input type="text" class="form-control" id="rpt-shtdesc"
								   name="repShtDesc"  required>
						</div>
					</div>


				</form>
			</div>
			<div class="modal-footer">
				<button data-loading-text="Saving..." id="saveproductRptGrpBtn"
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
	<div class="modal fade" id="prodSubclModal" tabindex="-1" role="dialog" aria-labelledby="prodSubclModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="prodSubclModalLabel">Select Sub Class</h4>
			</div>
			<div class="modal-body">
			  <form class="form-horizontal">
			    <div class="form-group">
							<label for="brn-id" class="col-md-3 control-label">Sub Class</label>

							<div class="col-md-6">
								<input type="text" class="form-control" id="sub-name-search"
									 >
							</div>
							<div class="col-md-1">
							     <button  id="searchSubclasses"
						type="button" class="btn btn-primary">
						Search
					</button>
							</div>
						</div>
			  </form>
          <div style="height: 300px !important; overflow: scroll;">
		  <table class="table table-striped table-hover table-bordered table-fixed" id="prodsubclassTbl">
			<thead>
				<tr>
				   <th width="1%"></th>
					<th width="4%">Subclass Id</th>
					<th width="12%">Sub Class</th>
				</tr>
			</thead>
			<tbody>
			
			</tbody>
			</table>
			</div>
			  <form id="prod-subcl-form">
			     <input type="hidden" id="sub-scl-pro-code" name="proCode"/>
			  </form>
			</div>
			<div class="modal-footer">
				<button data-loading-text="Saving..." id="saveProdSubclassesBtn"
					type="button" class="btn btn-success">Save</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">
					Cancel</button>
			</div>
		</div>
	</div>
</div>
	
	<div class="modal fade" id="prodModal" tabindex="-1" role="dialog"
		aria-labelledby="prodModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<form id="product-form" class="form-horizontal">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close dismiss" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="prodModalLabel">
						Edit/Add Product
					</h4>
				</div>
				<div class="modal-body">
					<input type="hidden" id="prg-group-pks" name="proGroup">
						<input type="hidden" class="form-control" id="pro-code" name="proCode">
						<div class="form-group">
							<label for="brn-id" class="col-md-3 control-label">Product ID</label>

							<div class="col-md-8">
								<input type="text" class="form-control" id="pro-sht-desc"
									name="proShtDesc"  required>
							</div>
						</div>
						<div class="form-group">
							<label for="cou-name" class="col-md-3 control-label">Product Name</label>

							<div class="col-md-8">
								<input type="text" class="form-control" id="pro-name"
									name="proDesc"  required>
							</div>
						</div>	
						<div class="form-group">
							<label for="cou-name" class="col-md-3 control-label">Policy Prefix</label>

							<div class="col-md-8">
								<input type="text" class="form-control" id="pro-pol-prefix"
									name="proPolPrefix"  required>
							</div>
						</div>	
						<div class="form-group">
							<label for="cou-name" class="col-md-3 control-label">Claim Prefix</label>

							<div class="col-md-8">
								<input type="text" class="form-control" id="pro-clm-prefix"
									name="proClmPrefix"  required>
							</div>
						</div>s
						<div class="form-group">
							<label for="cou-name" class="col-md-3 control-label">Min Premium</label>

							<div class="col-md-8">
								<input type="text" class="form-control" id="pro-min-prem"
									name="minPrem">
							</div>
						</div>
						<div class="form-group">
							<label for="cou-name" class="col-md-3 control-label">Product Category</label>

							<div class="col-md-8">
								 <select class="form-control" id="pro-inter-type" name="interfaceType">
							        <option value="">Select Product Category</option>
							        <option value="A">Accrual</option>
								    <option value="C">Cash</option>
								  </select>
							</div>
						</div>
						<div class="form-group">
							<label for="cou-name" class="col-md-3 control-label">Risk Note Template</label>

							<div class="col-md-8">
								<input type="text" class="form-control" id="pro-risk-note"
									   name="riskNote">
							</div>
						</div>
						<div class="form-group">
							<label for="rate-taxable" class="col-md-3 control-label">Renewable?</label>
							<div class="col-md-9 checkbox">
							    <label>
								 <input type="checkbox" name="renewable" id="chk-renewable">
								 </label>
							</div>
						</div>	
						<div class="form-group">
							<label for="rate-taxable" class="col-md-3 control-label">Motor Product</label>
							<div class="col-md-9 checkbox">
							    <label>
								 <input type="checkbox" name="motorProduct" id="chk-motor-prod">
								 </label>
							</div>
						</div>
						<div class="form-group">
							<label for="rate-taxable" class="col-md-3 control-label">Installment Allowed</label>
							<div class="col-md-9 checkbox">
							    <label>
								 <input type="checkbox" name="installAllowed" id="chk-motor-install-allowd">
								 </label>
							</div>
						</div>
						<div class="form-group">
							<label for="rate-taxable" class="col-md-3 control-label">Midnight Expiry</label>
							<div class="col-md-9 checkbox">
							    <label>
								 <input type="checkbox" name="midnightExp" id="chk-mid-exp">
								 </label>
							</div>
						</div>
						<div class="form-group">
							<label for="rate-taxable" class="col-md-3 control-label">Age Applicable</label>
							<div class="col-md-9 checkbox">
								<label>
									<input type="checkbox" name="ageApplicable" id="chk-age-applicable">
								</label>
							</div>
						</div>
						<div class="form-group">
							<label for="rate-taxable" class="col-md-3 control-label">Active Indicator</label>
							<div class="col-md-9 checkbox">
							    <label>
								 <input type="checkbox" name="active" id="chk-active">
								 </label>
							</div>
						</div>
						

				</div>
				<div class="modal-footer">
					<input type="submit" class="btn btn-success" value="Save">
					<button type="button" class="btn btn-default dismiss" data-dismiss="modal">
						Cancel
					</button>
				</div>
			</div>
			</form>
		</div>
	</div>
	
	
	
	
	<div class="modal fade" id="editProdSubclassModal" tabindex="-1" role="dialog"
		aria-labelledby="editProdSubclassModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="editProdSubclassModalLabel">
						Edit Product Sub Class
					</h4>
				</div>
				<div class="modal-body">
				   
					<form id="update-prod-sub-form" class="form-horizontal">
					<input type="hidden" id="prod-sub-pk" name="psId">
						<input type="hidden" class="form-control" id="prod-sub-pro-code" name="product">
						<input type="hidden" class="form-control" id="prod-sub-code-pk" name="subclass">
						<div class="form-group">
							<label for="brn-id" class="col-md-3 control-label">Sub Class ID</label>

							<div class="col-md-8">
								<input type="text" class="form-control" id="prod-sub-class-id"
									  disabled>
							</div>
						</div>
						<div class="form-group">
							<label for="cou-name" class="col-md-3 control-label">Sub Class Name</label>

							<div class="col-md-8">
								<input type="text" class="form-control" id="prod-sub-class-name"
									 disabled>
							</div>
						</div>	
						
						<div class="form-group">
							<label for="rate-taxable" class="col-md-3 control-label">Mandatory</label>
							<div class="col-md-9 checkbox">
							    <label>
								 <input type="checkbox" name="mandatory" id="prod-sub-mandatory">
								 </label>
							</div>
						</div>
						<div class="form-group">
							<label for="rate-taxable" class="col-md-3 control-label">Active Indicator</label>
							<div class="col-md-9 checkbox">
							    <label>
								 <input type="checkbox" name="active" id="prod-sub-active">
								 </label>
							</div>
						</div>
						
					</form>
				</div>
				<div class="modal-footer">
					<button data-loading-text="Saving..." id="updateprodSubclassBtn"
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