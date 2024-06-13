<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script type="text/javascript" src="<c:url value="/js/modules/setups/utilitysetups.js"/>"></script>

  <div class="x_panel">
     <button class="btn btn-success btn btn-info pull-right" id="btn-add-clnt-types">New</button>
       <div class="x_title">
	<h4>Client Types List</h4>
	</div>
	<table id="client-type-tbl" class="table table-hover table-bordered">
		<thead>
			<tr>

				<th>Client Type</th>
				<th>Client Type Desc</th>
				<th width="5%"></th>
				<th width="5%"></th>
			</tr>
		</thead>
	</table>
	</div>
  <div class="modal fade" id="clntTypeModesModal" tabindex="-1" role="dialog"
		aria-labelledby="clntTypeModesModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="clntTypeModesModalLabel">
						Edit/Add Client Type
					</h4>
				</div>
				<div class="modal-body" id="branch_model">
					<form id="client-type-form" class="form-horizontal">
						<input type="hidden" class="form-control" id="type-id" name="typeId">
						<div class="form-group">
							<label for="brn-id" class="col-md-3 control-label">Client Type</label>

							<div class="col-md-8">
								 <select class="form-control" id="clnt-type" name="clientType" required>
							        <option value="">Select Client Type</option>
							        <option value="I">Individual</option>
								    <option value="C">Corporate </option>
								  </select>
							</div>
						</div>
						<div class="form-group">
							<label for="unit-id" class="col-md-3 control-label">Client Type Desc</label>

							<div class="col-md-8">
							    <input type="text" class="form-control" id="type-desc"
									name="typeDesc"  required>
							</div>
						</div>
						
					</form>
				</div>
				<div class="modal-footer">
					<button data-loading-text="Saving..." id="saveClientType"
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
	