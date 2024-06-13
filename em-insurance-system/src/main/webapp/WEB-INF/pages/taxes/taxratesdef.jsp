<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script type="text/javascript"
	src="<c:url value="/js/modules/utils/select2builder.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/modules/taxes/taxes.js"/>"></script>

  <div class="x_panel">
    <div class="x_title">
	<h4>Tax Rates</h4>
	</div>
	<form class="form-horizontal">
			<div class="form-group">
				<label for="brn-id" class="col-md-3 control-label">Select
					Sub Class</label>

				<div class="col-md-4">
					<input type="hidden" id="sub-code"/>
					<input type="hidden" id="prod-code"/>
					<div id="sub-class-def" class="form-control"
						select2-url="<c:url value="/protected/setups/taxes/selProdSubClasses"/>">

					</div>
					<input type="hidden" id="class-pk">

				</div>
			</div>

		</form>

       
	<button class="btn btn-success btn btn-info pull-right" id="btn-add-tax-rates">New</button>
	  <div class="cutom-container">
	<table id="tax-rates-tbl" class="table table-hover table-bordered">
		<thead>
			<tr>

				<th>Rate Type</th>
				<th>Range From</th>
				<th>Range To</th>
				<th>Rate</th>
				<th>Div Factor</th>
				<th>Tax Level</th>
				<th>Trans Code</th>
				<th>Mandatory?</th>
				<th>Active?</th>
				<th width="5%"></th>
				<th width="5%"></th>
			</tr>
		</thead>
	</table>
		  </div>
	</div>
	
	
	
	
<div class="modal fade" id="taxRatesModal" tabindex="-1" role="dialog"
		aria-labelledby="taxRatesModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="taxRatesModalLabel">
						Edit/Add Tax Rates
					</h4>
				</div>
				<div class="modal-body">
				   
					<form id="tax-rates-form" class="form-horizontal">
						<input type="hidden" class="form-control" id="tax-code" name="taxId">
						<input type="hidden" class="form-control" id="tax-sub-det" name="subclass">
						<input type="hidden" class="form-control" id="tax-prod-det" name="productsDef">
						
						<div class="form-group">
							<label for="brn-id" class="col-md-3 control-label">Range From</label>

							<div class="col-md-8">
								<input type="text" class="form-control" id="tax-range-from"
									name="rangeFrom"  required>
							</div>
						</div>
						<div class="form-group">
							<label for="cou-name" class="col-md-3 control-label">Range To</label>

							<div class="col-md-8">
								<input type="text" class="editUserCntrls form-control"
									id="tax-range-to" name="rangeTo" 
									required>
							</div>
						</div>	
						<div class="form-group">
							<label for="cou-name" class="col-md-3 control-label">Rate Type</label>

							<div class="col-md-8">
								<select class="form-control" id="tax-rate-type" name="rateType">
							        <option value="">Select Rate Type</option>
							        <option value="P">Percentage</option>
							        <option value="M">Per Mille</option>
								    <option value="A">Amount</option>
								    
								  </select>
							</div>
						</div>	
						
						<div class="form-group">
							<label for="cou-name" class="col-md-3 control-label">Rate</label>

							<div class="col-md-8">
								<input type="text" class="editUserCntrls form-control"
									id="tax-rate" name="taxRate" 
									required>
							</div>
						</div>	
						
						
						<div class="form-group">
							<label for="cou-name" class="col-md-3 control-label">Div Factor</label>

							<div class="col-md-8">
								<input type="number" class="editUserCntrls form-control"
									id="tax-div-fact" name="divFactor" 
									required>
							</div>
						</div>	
						<div class="form-group">
							<label for="cou-name" class="col-md-3 control-label">Tax Level</label>

							<div class="col-md-8">
								<select class="form-control" id="tax-level" name="taxLevel">
							        <option value="">Select Tax Level</option>
							        <option value="P">Policy</option>
							        <option value="R">Risk</option>
								  </select>
							</div>
						</div>	
						<div class="form-group">
							<label for="cou-name" class="col-md-3 control-label">Revenue Item</label>

							<div class="col-md-8">
								<input type="hidden" id="rev-item-id"  name="revenueItems"/>
		                     <input type="hidden" id="rev-item-name">
		                        <div id="rev-item-def" class="form-control" 
				                                 select2-url="<c:url value="/protected/setups/taxes/selTaxRevenueItems"/>" >
				                                 
				               </div> 
							</div>
						</div>
						<div class="form-group">
							<label for="rate-taxable" class="col-md-3 control-label">Active?</label>
							<div class="col-md-9 checkbox">
							    <label>
								 <input type="checkbox" name="active" id="tax-active">
								 </label>
							</div>
						</div>
						<div class="form-group">
							<label for="rate-taxable" class="col-md-3 control-label">Mandatory?</label>
							<div class="col-md-9 checkbox">
								<label>
									<input type="checkbox" name="mandatory" id="tax-mandatory">
								</label>
							</div>
						</div>


					</form>
				</div>
				<div class="modal-footer">
					<button data-loading-text="Saving..." id="savetaxratesBtn"
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