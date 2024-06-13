<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script type="text/javascript" src="<c:url value="/js/modules/setups/utilitysetups.js"/>"></script>

  <div class="x_panel">
     <button class="btn btn-success btn btn-info pull-right" id="btn-add-clnt-title">New</button>
       <div class="x_title">
	<h4>Client Titles List</h4>
	</div>
	<table id="client-title-tbl" class="table table-hover table-bordered">
		<thead>
			<tr>

				<th>Client Title</th>
				<th width="5%"></th>
				<th width="5%"></th>
			</tr>
		</thead>
	</table>
	</div>
  <div class="modal fade" id="clntTitleModesModal" tabindex="-1" role="dialog"
		aria-labelledby="clntTitleModesModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="clntTitleModesModalLabel">
						Edit/Add Client Title
					</h4>
				</div>
				<div class="modal-body" id="branch_model">
					<form id="client-title-form" class="form-horizontal">
						<input type="hidden" class="form-control" id="title-id" name="titleId">
						
						<div class="form-group">
							<label for="unit-id" class="col-md-3 control-label">Client Title</label>

							<div class="col-md-8">
							    <input type="text" class="form-control" id="title-desc"
									name="titleName"  required>
							</div>
						</div>
						
					</form>
				</div>
				<div class="modal-footer">
					<button data-loading-text="Saving..." id="saveClientTitle"
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
	