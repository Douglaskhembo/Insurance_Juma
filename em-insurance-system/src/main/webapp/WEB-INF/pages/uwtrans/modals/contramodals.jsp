<div class="modal fade" id="contraPoliciesModal" tabindex="-1" role="dialog"
	aria-labelledby="contraPoliciesModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">

				<h4 class="modal-title" id="contraPoliciesModalLabel">Select A
					Policy</h4>
			</div>
			<div class="modal-body">
				<div class="box-body">
					<form id="search-form" class="form-horizontal">
					<div class="form-group">

							<div class="col-md-6">
								<label for="brn-id" class="col-md-4 control-label">Policy
									No.</label>

								<div class="col-md-8">
									<input type='text' class="form-control pull-right"
										id="pol-search-number" />
								</div>
							</div>
							<div class="col-md-6">
								<label for="brn-id" class="col-md-4 control-label">Risk
									ID.</label>

								<div class="col-md-8">
									<input type='text' class="form-control pull-right"
										id="rev-search-number" />
								</div>
							</div>
						</div>
						<div class="form-group">
                            <div class="col-md-6">
								<label for="brn-id" class="col-md-4 control-label">DR
									No.</label>

								<div class="col-md-8">
									<input type='text' class="form-control pull-right"
										id="dr-search-number" />
								</div>
							</div>
							<div class="col-md-6">
								<label for="brn-id" class="col-md-4 control-label">Client
									 </label>

								<div class="col-md-8">
									<input type='text' class="form-control pull-right"
										id="rev-search-name" />
								</div>
							</div>
							
						</div>
						<div class="form-group">
                            <div class="col-md-6">
								<label for="brn-id" class="col-md-4 control-label">Agent
									</label>

								<div class="col-md-8">
									<input type='text' class="form-control pull-right"
										id="agent-search-number" />
								</div>
							</div>
							<div class="col-md-6">
								
							</div>
							
						</div>
						<div class="form-group">
							<input type="button" class="btn btn-info pull-right"
								style="margin-right: 10px;" value="Search"
								id="btn-search-policies">
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

