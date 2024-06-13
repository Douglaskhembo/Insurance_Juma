<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script type="text/javascript" src="<c:url value="/js/modules/setups/endorseremarks.js"/>"></script>

  <div class="x_panel">
     <button class="btn btn-success btn btn-info pull-right" id="btn-add-end-remarks">New</button>
       <div class="x_title">
	<h4>Endorsement Remarks List</h4>
	</div>
	<table id="endorse-remarks-tbl" class="table table-hover table-bordered">
		<thead>
			<tr>

				<th width="20%">Remark Sht Desc</th>
				<th>Remarks</th>
				<th width="5%"></th>
				<th width="5%"></th>
			</tr>
		</thead>
	</table>
	</div>
  <div class="modal fade" id="endRemarksModal" tabindex="-1" role="dialog"
		aria-labelledby="endRemarksModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="endRemarksModalLabel">
						Edit/Add Endorsement Remarks
					</h4>
				</div>
				<div class="modal-body" id="branch_model">
					<form id="end-remarks-form" class="form-horizontal">
						<input type="hidden" class="form-control" id="remark-pk" name="remarkId">
						
						<div class="form-group">
							<label for="unit-id" class="col-md-3 control-label">Remark ID</label>

							<div class="col-md-8">
							    <input type="text" class="form-control" id="remark-id"
									name="remarkShtDesc"  required>
							</div>
						</div>
						<div class="form-group">
							<label for="unit-id" class="col-md-3 control-label">Remarks</label>

							<div class="col-md-8">
							    <textarea class="form-control" rows="7" name="remarks" id="remarks" required></textarea>
							</div>
						</div>
						
					</form>
				</div>
				<div class="modal-footer">
					<button data-loading-text="Saving..." id="saveEndorseRemarks"
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
	