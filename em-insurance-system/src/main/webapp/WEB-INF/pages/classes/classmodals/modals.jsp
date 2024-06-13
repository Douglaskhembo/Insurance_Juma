<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="modal fade" id="classModal" tabindex="-1" role="dialog"
	aria-labelledby="classModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="classModalLabel">Edit/Add Class</h4>
			</div>
			<div class="modal-body">

				<form id="class-form" class="form-horizontal">
					<input type="hidden" class="form-control" id="cl-code" name="clId">
					<div class="form-group">
						<label for="brn-id" class="col-md-3 control-label">Class
							ID</label>

						<div class="col-md-8">
							<input type="text" class="form-control" id="class-id"
								name="clShtDesc" required>
						</div>
					</div>
					<div class="form-group">
						<label for="cou-name" class="col-md-3 control-label">Class
							Name</label>

						<div class="col-md-8">
							<input type="text" class="editUserCntrls form-control"
								id="class-name" name="clDesc" required>
						</div>
					</div>

					<div class="form-group">
						<label for="rate-taxable" class="col-md-3 control-label">Active
							Indicator</label>
						<div class="col-md-9 checkbox">
							<label> <input type="checkbox" name="clactive"
								id="chk-active">
							</label>
						</div>
					</div>

				</form>
			</div>
			<div class="modal-footer">
				<button id="delClassBtn" type="button" class="btn btn-danger">
					Delete</button>
				<button data-loading-text="Saving..." id="saveClassBtn"
					type="button" class="btn btn-success">Save</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">
					Cancel</button>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="subclassModal" tabindex="-1" role="dialog"
	aria-labelledby="subclassModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="subclassModalLabel">Edit/Add Sub
					Class</h4>
			</div>
			<div class="modal-body">

				<form id="sub-class-form" class="form-horizontal">
					<input type="hidden" id="clasdef-pk" name="classDef"> <input
						type="hidden" class="form-control" id="scl-pk" name="subId">
					<div class="form-group">
						<label for="brn-id" class="col-md-3 control-label">Sub
							Class ID</label>

						<div class="col-md-8">
							<input type="text" class="form-control" id="sub-class-id"
								name="subShtDesc" required>
						</div>
					</div>
					<div class="form-group">
						<label for="cou-name" class="col-md-3 control-label">Sub
							Class Name</label>

						<div class="col-md-8">
							<input type="text" class="editUserCntrls form-control"
								id="sub-class-name" name="subDesc" required>
						</div>
					</div>
					
					<div class="form-group">
						<label for="cou-name" class="col-md-3 control-label">Risk ID Format</label>

						<div class="col-md-8">
							<input type="text" class="editUserCntrls form-control"
								id="risk-id-format" name="riskFormat">
						</div>
					</div>

					<div class="form-group">
						<label for="rate-taxable" class="col-md-3 control-label">Risk
							Unique?</label>
						<div class="col-md-9 checkbox">
							<label> <input type="checkbox" name="riskUnique"
								id="chk-unique-risk"></label>

						</div>
					</div>

					<div class="form-group">
						<label for="rate-taxable" class="col-md-3 control-label">Active
							Indicator?</label>
						<div class="col-md-9 checkbox">
							<label> <input type="checkbox" name="active"
								id="chk-sub-active"></label>

						</div>
					</div>

				</form>
			</div>
			<div class="modal-footer">
				<button data-loading-text="Saving..." id="saveSubClassBtn"
					type="button" class="btn btn-success">Save</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">
					Cancel</button>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="coverTypeModal" tabindex="-1" role="dialog"
	aria-labelledby="coverTypeModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="coverTypeModalLabel">Edit/Add
					SubClass Cover Type</h4>
			</div>
			<div class="modal-body">

				<form id="cover-form" class="form-horizontal">
					<input type="hidden" class="form-control" id="cov-code"
						name="scId"> <input type="hidden" id="sub-code-pk"
						name="subclass">
					<div class="form-group">
						<label for="brn-id" class="col-md-3 control-label">Cover
							Type</label>

						<div class="col-md-6">
							<input type="hidden" id="sub-cov-pk" name="coverTypes"/>
                            <input type="hidden" id="sub-cov-name"/>
							<div id="sub-cov-def" class="form-control"
								select2-url="<c:url value="/protected/setups/classes/sclcoverTypes"/>">
							</div>
						</div>
						<div class="col-md-3">
							<button type="button" class="btn btn-info" id="btn-add-new-cov">New</button>
							<button type="button" class="btn btn-info" id="btn-edit-old-cov">Edit</button>
						</div>
						</div>
						<div class="form-group">
							<label for="unit-id" class="col-md-3 control-label">Min Premium</label>

							<div class="col-md-5">
							    <input type="text" class="form-control number" id="min-prem"
									name="minPrem" required>
							</div>
						</div>



					<div class="form-group">
							<label for="rate-taxable" class="col-md-3 control-label">Default</label>
							<div class="col-md-5 checkbox">
							<label>
								<input type="checkbox" name="defaultCoverType" id="chk-sub-cov-def"></label>

							</div>
						</div>
				</form>
			</div>
			<div class="modal-footer">
				<button data-loading-text="Saving..." id="saveCoverTypeBtn"
					type="button" class="btn btn-success">Save</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">
					Cancel</button>
			</div>
		</div>
	</div>
</div>


<div class="modal fade" id="newcoverTypeModal" tabindex="-1" role="dialog"
		aria-labelledby="newcoverTypeModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="newcoverTypeModalLabel">
						Edit/Add Cover Type
					</h4>
				</div>
				<div class="modal-body">
				   
					<form id="new-cover-form" class="form-horizontal">
						<input type="hidden" class="form-control" id="new-cov-code" name="covId">
						<div class="form-group">
							<label for="brn-id" class="col-md-3 control-label">Cover Type ID</label>

							<div class="col-md-8">
								<input type="text" class="form-control" id="new-cover-id"
									name="covShtDesc"  required>
							</div>
						</div>
						<div class="form-group">
							<label for="cou-name" class="col-md-3 control-label">Cover Type Name</label>

							<div class="col-md-8">
								<input type="text" class="editUserCntrls form-control"
									id="new-cov-name" name="covName" 
									required>
							</div>
						</div>	
						<div class="form-group">
							<label for="unit-id" class="col-md-3 control-label">Min SI</label>

							<div class="col-md-5">
							    <input type="text" class="form-control number" id="min-si"
									name="minSi" required>
							</div>
						</div>
						
						<div class="form-group">
							<label for="rate-taxable" class="col-md-3 control-label">Active Indicator</label>
							<div class="col-md-9 checkbox">
							   <label>
								 <input type="checkbox" name="active" id="new-cov-active"></label>
								 
							</div>
						</div>
						
					</form>
				</div>
				<div class="modal-footer">
				    <button data-loading-text="Saving..." id="delnewCoverTypeBtn"
						type="button" class="btn btn-danger">
						Delete
					</button>
					<button data-loading-text="Saving..." id="savenewCoverTypeBtn"
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

<div class="modal fade" id="sectionsModal" tabindex="-1" role="dialog"
	aria-labelledby="sectionsModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="sectionsModalLabel">Edit/Add
					Sub Class Premium Items</h4>
			</div>
			<div class="modal-body">

				<form id="sect-form" class="form-horizontal">
					<input type="hidden" class="form-control" id="sec-code" name="ssId">
					<input type="hidden" class="form-control" id="sec-sub-code-pk"
						name="subclass">
					<div class="form-group">
						<label for="brn-id" class="col-md-3 control-label">Premium Item
							</label>

						<div class="col-md-6">
							<input type="hidden" id="sect-pk-code" name="section"
						/>
                        <input type="hidden" id="sect-pk-name"/>
					<div id="sect-def" class="form-control"
						select2-url="<c:url value="/protected/setups/classes/sclsections"/>">

					</div>
						</div>
						<div class="col-md-3">
							<button type="button" class="btn btn-info" id="btn-add-new-sect">New</button>
							<button type="button" class="btn btn-info" id="btn-edit-old-sect">Edit</button>
						</div>
					</div>
					<div class="form-group">
							<label for="rate-taxable" class="col-md-3 control-label">Declaration</label>
							<div class="col-md-9 checkbox">
							   <label>
								 <input type="checkbox" name="declaration" id="decl-sect"></label>
								 
							</div>
						</div>
					<div class="form-group">
						<label for="rate-taxable" class="col-md-3 control-label">Refundable?</label>
						<div class="col-md-9 checkbox">
							<label>
								<input type="checkbox" name="refundable" id="refund-sect"></label>

						</div>
					</div>
                       <div class="form-group">
							<label for="rate-taxable" class="col-md-3 control-label">Active Indicator</label>
							<div class="col-md-9 checkbox">
							   <label>
								 <input type="checkbox" name="active" id="sect-active"></label>
								 
							</div>
						</div>


				</form>
			</div>
			<div class="modal-footer">
				<button data-loading-text="Saving..." id="saveSectBtn" type="button"
					class="btn btn-success">Save</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">
					Cancel</button>
			</div>
		</div>
	</div>
</div>


<div class="modal fade" id="newsectionsModal" tabindex="-1" role="dialog"
		aria-labelledby="newsectionsModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="newsectionsModalLabel">
						Edit/Add Premium Item
					</h4>
				</div>
				<div class="modal-body">
				   
					<form id="new-sect-form" class="form-horizontal">
						<input type="hidden" class="form-control" id="new-sec-code" name="id">
						<div class="form-group">
							<label for="brn-id" class="col-md-3 control-label">Premium Item ID</label>

							<div class="col-md-8">
								<input type="text" class="form-control" id="new-sec-id"
									name="shtDesc"  required>
							</div>
						</div>
						<div class="form-group">
							<label for="cou-name" class="col-md-3 control-label">Premium Item Name</label>

							<div class="col-md-8">
								<input type="text" class="editUserCntrls form-control"
									id="new-sect-name" name="desc" 
									required>
							</div>
						</div>	
						<div class="form-group">
							<label for="sel2" class="col-md-3 control-label">Premium Item Type</label>
							<div class="col-md-8">
							      <select class="form-control" id="new-sect-type" name="type">
							        <option value="">Select Premium Item Type</option>
							        <option value="SI">Section Sum Insured</option>
							        <option value="SL">Section Limit</option>
									  <option value="RD">Section Rider</option>
									  <option value="DS">Discount Rider</option>
								  </select>
							</div>
						</div>

						
						
					</form>
				</div>
				<div class="modal-footer">
				    <button data-loading-text="Saving..." id="newdelSectBtn"
						type="button" class="btn btn-danger">
						Delete
					</button>
					<button data-loading-text="Saving..." id="newsavenewSectBtn"
						type="button" class="btn btn-success">
						Save
					</button>
					<button data-loading-text="Saving..." id="newsaveSectBtn"
						type="button" class="btn btn-success">
						Save and Close
					</button>
					<button type="button" class="btn btn-default" data-dismiss="modal">
						Cancel
					</button>
				</div>
			</div>
		</div>
	</div>

<div class="modal fade" id="covSectModal" tabindex="-1" role="dialog"
	aria-labelledby="covSectModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="covSectModalLabel">Select Premium Item</h4>
			</div>
			<div class="modal-body" id="branch_model">

		  <table class="table table-striped table-hover table-bordered" id="covtSectTbl">
			<thead>
				<tr>
				   <th width="1%"></th>
					<th width="4%">Premium Item Id</th>
					<th width="12%">Premium Item</th>
				</tr>
			</thead>
			<tbody>
			
			</tbody>
			</table>
			  <form id="cover-section-form">
			     <input type="hidden" id="sect-cover-code-pk" name="coverCode"/>
			  </form>
			</div>
			<div class="modal-footer">
				<button data-loading-text="Saving..." id="savecoverSectionBtn"
					type="button" class="btn btn-success">Save</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">
					Cancel</button>
			</div>
		</div>
	</div>
</div>



<div class="modal fade" id="editcovSectModal" tabindex="-1" role="dialog"
	aria-labelledby="covSectModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title">Select Premium Item</h4>
			</div>
			<div class="modal-body">
			  <form id="edit-cover-section-form" class="form-horizontal">
			     <input type="hidden" id="edit-scs-code-pk" name="scsId"/>
			      <input type="hidden" id="edit-scs-cov-pk" name="subcoverType"/>
			      <input type="hidden" id="edit-scs-sec-pk" name="subSections"/>
			       <div class="form-group">
							<label for="cou-name" class="col-md-3 control-label">Premium Item Name</label>

							<div class="col-md-8">
								<input type="text" class="editUserCntrls form-control"
									 name="desc" id="edit-scs-sec-name"
									readonly>
							</div>
						</div>	
						<div class="form-group">
							<label for="cou-name" class="col-md-3 control-label">Order</label>

							<div class="col-md-8">
								<input type="number" class="editUserCntrls form-control"
									id="edit-cov-sect-order" name="order" 
									>
							</div>
						</div>
						<div class="form-group">
							<label for="rate-taxable" class="col-md-3 control-label">Mandatory</label>
							<div class="col-md-9 checkbox">
							   <label>
								 <input type="checkbox" name="mandatory" id="edit-cov-sect-mand"></label>
								 
							</div>
						</div>
			  </form>
			 
						
			</div>
			<div class="modal-footer">
				<button data-loading-text="Saving..." id="saveEditcoverSectionBtn"
					type="button" class="btn btn-success">Save</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">
					Cancel</button>
			</div>
		</div>
	</div>
</div>