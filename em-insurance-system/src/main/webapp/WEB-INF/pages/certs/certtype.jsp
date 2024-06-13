<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script type="text/javascript" src="<c:url value="/js/modules/certs/certs.js"/>"></script>

  <div class="x_panel">
     <button class="btn btn-success btn btn-info pull-right" id="btn-add-cert-types">New</button>
       <div class="x_title">
	<h4>Certificate Types</h4>
	</div>
	  <div class="cutom-container">
	<table id="certtbl" class="table table-hover table-bordered">
		<thead>
			<tr>

				<th>Sht Desc</th>
				<th>Description</th>
				<th>Prefix</th>
				<th>Template</th>
				<th>Min Capacity</th>
				<th>Max Capacity</th>
				<th>Reorder Level</th>
				<th width="5%"></th>
				<th width="5%"></th>
			</tr>
		</thead>
	</table>
		  </div>
	</div>
<div class="x_panel">
		<button class="btn btn-success btn btn-info pull-right" id="btn-add-subclass-certs">New</button>

	<div class="x_title">
		<h4>Applicable Subclasses</h4>
	</div>
	<div class="cutom-container">
		<table id="certsubclasses" class="table table-hover table-bordered">
			<thead>
			<tr>
				<th>Sub Class Id</th>
				<th>Sub Class Name</th>
				<th width="5%"></th>
			</tr>
			</thead>
		</table>
	</div>
</div>
  <div class="modal fade" id="certtypeModal" tabindex="-1" role="dialog"
		aria-labelledby="certtypeModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="certtypeModalLabel">
						Define Certificate Type
					</h4>
				</div>
				<div class="modal-body">
					<form id="cert-type-form" class="form-horizontal">
						<input type="hidden" class="form-control" id="cert-pk" name="certId">
						
						<div class="form-group">
							<label for="brn-id" class="col-md-3 control-label">Sht Desc</label>

							<div class="col-md-8">
								<input type="text" class="form-control" id="cert-id"
									name="certShtDesc"  required style="text-transform:uppercase">
							</div>
						</div>
						<div class="form-group">
							<label for="unit-id" class="col-md-3 control-label">Cert Desc.</label>

							<div class="col-md-8">
							    <input type="text" class="form-control" id="cert-desc"
									name="certDesc"  required style="text-transform:uppercase">
							</div>
						</div>
						<div class="form-group">
							<label for="unit-id" class="col-md-3 control-label">Prefix</label>

							<div class="col-md-8">
							 <input type="text" class="form-control" id="cert-prefix"
									name="certPrefix"  required style="text-transform:uppercase">
							</div>
						</div>
						<div class="form-group">
							<label for="unit-id" class="col-md-3 control-label">Template</label>

							<div class="col-md-8">
							 <input type="text" class="form-control" id="cert-template"
									name="certTemplate">
							</div>
						</div>
						<div class="form-group">
							<label for="unit-id" class="col-md-3 control-label">Min Capacity</label>

							<div class="col-md-8">
							 <input type="number" class="form-control" id="cert-min-cap"
									name="minCapacity">
							</div>
						</div>
						<div class="form-group">
							<label for="unit-id" class="col-md-3 control-label">Max Capacity</label>

							<div class="col-md-8">
							 <input type="number" class="form-control" id="cert-max-cap"
									name="maxCapacity">
							</div>
						</div>
						<div class="form-group">
							<label for="unit-id" class="col-md-3 control-label">Reorder Level</label>

							<div class="col-md-8">
							 <input type="number" class="form-control" id="cert-reorder-level"
									name="reorderLevel">
							</div>
						</div>
						
					</form>
				</div>
				<div class="modal-footer">
					<button data-loading-text="Saving..." id="saveCertType"
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

<div class="modal fade" id="subclasscerttypeModal" tabindex="-1" role="dialog"
	 aria-labelledby="subclassModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="subclassModalLabel">Select Sub Class</h4>
			</div>
			<div class="modal-body">
				<div style="height: 300px !important; overflow: scroll;">
					<table class="table table-striped table-hover table-bordered table-fixed" id="subclassestbl">
						<thead>
						<tr>
							<th width="1%"></th>
							<th width="4%">Sub Class Id</th>
							<th width="12%">Sub Class Name</th>
						</tr>
						</thead>
						<tbody>

						</tbody>
					</table>
				</div>
				<form id="sub-class-form">
					<input type="hidden" id="certtype-code" name="certTypeId"/>
				</form>
			</div>
			<div class="modal-footer">
				<button data-loading-text="Saving..." id="saveSubclassBtn"
						type="button" class="btn btn-success">Save</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">
					Cancel</button>
			</div>
		</div>
	</div>
</div>
	