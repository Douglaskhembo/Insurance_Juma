<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="modal fade" id="revPoliciesModal" tabindex="-1" role="dialog"
	aria-labelledby="revPoliciesModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="revPoliciesModalLabel">Select A	Policy</h4>
			</div>
			<div class="modal-body">
				<div class="box-body">
					<form id="search-form" class="form-horizontal">
					<div class="form-group">
							<div class="col-md-6">
								<label for="brn-id" class="col-md-4 control-label">Policy No.</label>
								<div class="col-md-8">
									<input type='text' class="form-control pull-right"
										id="pol-search-number" />
								</div>
							</div>
							<div class="col-md-6">
								<label for="brn-id" class="col-md-4 control-label">Plate Number.</label>
								<div class="col-md-8">
									<input type='text' class="form-control pull-right"
										id="rev-search-number" />
								</div>
							</div>
						</div>
						<div class="form-group">
                            <div class="col-md-6">
								<label for="brn-id" class="col-md-4 control-label">DR No.</label>
								<div class="col-md-8">
									<input type='text' class="form-control pull-right"
										id="dr-search-number" />
								</div>
							</div>
							<div class="col-md-6 col-xs-12">
								<label for="rev-search-name" class="col-md-4 control-label">Client</label>
								<div class="col-md-8 col-xs-12">
									<input type='hidden' class="form-control pull-right"
										   id="rev-search-name" />
									<div id="client-frm" class="form-control"
										 select2-url="<c:url value="/protected/uw/policies/uwClients"/>" >
									</div>
								</div>
							</div>
							
						</div>
						<div class="form-group">
							<div class="col-md-6 col-xs-12">
								<label for="agent-search-number" class="col-md-4 control-label">Intermediary</label>
								<div class="col-md-8 col-xs-12">
									<input type='hidden' class="form-control pull-right"
										   id="agent-search-number" />
									<div id="acc-frm" class="form-control"
										 select2-url="<c:url value="/protected/setups/binders/selAccounts"/>" >
									</div>
								</div>
							</div>
						</div>

						<div class="form-group">
							<input type="button" class="btn btn-info pull-right"
								style="margin-right: 10px;" value="Search"
								id="btn-search-policies">
								<input type="button" class="btn btn-info pull-right"
								style="margin-right: 10px;" value="Search"
								id="btn-search-med-policies">
							<input type="button" class="btn btn-info pull-right"
								   style="margin-right: 10px;" value="Search"
								   id="btn-search-life-policies">
						</div>


					</form>
				</div>

				<table id="revtranstbl" class="table table-hover table-bordered">
					<thead>
						<tr>

							<th>Policy No.</th>
							<th>Client Policy</th>
							<th>Endors. No.</th>
							<th>Client</th>
							<th>Agent</th>
							<th>UW Year</th>
							<th>Binder/Mask</th>
						</tr>
					</thead>
				</table>
			</div>
			<div class="modal-footer">
				<button data-loading-text="Saving..." id="selectauthtrans"
					type="button" class="btn btn-success">OK</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">
					Cancel</button>
			</div>
		</div>
	</div>
</div>




