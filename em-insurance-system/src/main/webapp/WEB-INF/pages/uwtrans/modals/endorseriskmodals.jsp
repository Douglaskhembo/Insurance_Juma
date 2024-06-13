<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="modal fade" id="endorseRiskModal" tabindex="-1" role="dialog"
	aria-labelledby="endorseRiskModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">

				<h4 class="modal-title" id="endorseRiskModalLabel">Select A
					Risk</h4>
			</div>
			<div class="modal-body">
				<div class="box-body">
					<form id="search-form" class="form-horizontal">
					<div class="form-group">

							<div class="col-md-6">
								<label for="brn-id" class="col-md-4 control-label">Select
									Insured</label>

								<div class="col-md-8">
									<input type="hidden" id="endorse-insured-id"/>
		                        <div id="endos-insured-frm" class="form-control" 
				                                 select2-url="<c:url value="/protected/uw/endorsements/endorsinsureds"/>" >
				                                 
				               </div> 
								</div>
							</div>
							
						</div>
						<div class="form-group">
						<div class="col-md-6">
								<label for="brn-id" class="col-md-4 control-label">Risk
									ID</label>

								<div class="col-md-8">
									<input type='text' class="form-control pull-right"
										id="endorse-risk-search" />
								</div>
							</div>
						</div>
						<div class="form-group">
							<input type="button" class="btn btn-info pull-right"
								style="margin-right: 10px;" value="Search"
								id="btn-search-endos-risks">
						</div>


					</form>
				</div>

				<table id="endorserisktbl" class="table table-hover table-bordered">
					<thead>
						<tr>
							<th>Plate Number</th>
							<th>Model</th>
							<th>WEF</th>
							<th>WET</th>
							<th>Action</th>
							<th></th>
						</tr>
					</thead>
				</table>
			</div>
			<div class="modal-footer">
			<!-- <button data-loading-text="Saving..." id="processendorserisks"
					type="button" class="btn btn-success">OK</button> -->	
				<button type="button" class="btn btn-default" data-dismiss="modal">
					Cancel</button>
			</div>
		</div>
	</div>
</div>