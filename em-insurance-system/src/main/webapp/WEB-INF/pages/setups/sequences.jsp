<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript"
	src="<c:url value="/js/modules/setups/sequences.js"/>"></script>
	
	<div class="x_panel">
	 <div class="x_title">
	<h4>System Sequences Set Up</h4>
	</div>
	<form id="seqform" class="form-horizontal form-label-left input_mask">
		
		    <input type="hidden" name="seqId" id="seq-code">
			<div class="form-group">
				<label for="brn-id" class="col-md-3 control-label">Prefix</label>

				<div class="col-md-4 col-xs-12">
					<input type="text" class="form-control" id="prefix-name"
						name="seqPrefix" required>
				</div>
			</div>
			<div class="form-group">
				<label for="frac-units" class="col-md-3 control-label">Last
					Value</label>

				<div class="col-md-4 col-xs-12">
					<input type="number" class="editUserCntrls form-control"
						id="last-number" name="lastNumber" disabled>
				</div>
			</div>
			
			<div class="form-group">
				<label for="fraction" class="col-md-3 control-label">Next Value</label>

				<div class="col-md-4 col-xs-12">
					<input type="number" class="editUserCntrls form-control"
						id="next-number" name="nextNumber" required>
				</div>
			</div>
			

			<div class="form-group">
				<label for="curr-symbol" class="col-md-3 control-label">Sequence Type</label>

				<div class="col-md-4 col-xs-12">
					<select class="form-control" id="sel3" name="seqType" required>
							        <option value="">Select Sequence Type</option>
							        <option value="PBY">Per Branch Per Year</option>
								    <option value="PBA">Per Branch</option>
								    <option value="PAY">All Branches Per Year</option>
								     <option value="PAA">All Branches</option>
								     <option value="PPA">Per Product Per Branch</option>
								  </select>
				</div>
			</div>
			<div class="form-group">
				<label for="curr-symbol" class="col-md-3 control-label">Transaction Type</label>

				<div class="col-md-4 col-xs-12">
					<select class="form-control" id="sel2" name="transType" required>
							        <option value="">Select Transaction Type</option>
							        <option value="C">Clients Definition</option>
							        <option value="A">Agents</option>
								    <option value="P">Policies</option>
								    <option value="E">Endorsements</option>
								    <option value="R">Receipts</option>
								    <option value="D">Debit Note</option>
						            <option value="CL">Claim Trans</option>
						            <option value="CP">Claim Payments</option>
						            <option value="RQ">Requisition Payments</option>
						            <option value="M">Medical Membership</option>
						            <option value="Q">Quotation Trans</option>
						            <option value="AD">Admin Fee Trans</option>
									<option value="PR">Proposals</option>
						            <option value="CRD">Medical Cards</option>
									<option value="RFD">Refund</option>
									<option value="RM">Remittance Trans</option>
								  </select>
				</div>
			</div>

		<div class="ln_solid"></div>
                      <div class="form-group">
                      <div class="col-md-6 col-sm-6 col-xs-12 col-md-offset-3">
		<button type="button" class="btn btn-default" id="newSequency">New</button>
			<button data-loading-text="Saving..." id="saveSequencyBtn"
				type="button" class="btn btn-primary">Save</button>
				</div>
		</div>

	</form>
	 <div class="x_title">
	<h4>Sequences List</h4>
	</div>
		<div class="cutom-container">
	<table id="seqList" class="table table-hover table-bordered">
		<thead>
			<tr>

				<th>Prefix</th>
				<th>Last Value</th>
				<th>Next Value</th>
				<th>Sequence Type</th>
				<th>Transaction Type</th>
				<th width="5%"></th>
				<th width="5%"></th>
			</tr>
		</thead>
	</table>
			</div>
</div>