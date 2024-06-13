<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="modal fade" id="sectModal" tabindex="-1" role="dialog"
		aria-labelledby="sectModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="sectModalLabel">
						Edit/Add Risk Section
					</h4>
				</div>
				<div class="modal-body">
				   
					<form id="risk-sect-form" class="form-horizontal">
						<input type="hidden" class="form-control" id="sect-code-pk" name="sectId">
						<input type="hidden" class="form-control" id="risk-sect-code-pk" name="riskId">
						<input type="hidden" class="form-control" id="sect-prem-id-pk" name="premRates">
						<div class="form-group">
							<label for="brn-id" class="col-md-3 control-label">Section</label>

							<div class="col-md-8">
								<input type="hidden" id="risk-sect-id" name="section"/>
		                     <input type="hidden" id="risk-sect-name">
		                        <div id="risk-sect-frm" class="form-control" 
				                                 select2-url="<c:url value="/protected/uw/policies/riskselectsections"/>" >
				                                 
				               </div> 
							</div>
						</div>
						<div class="form-group">
							<label for="cou-name" class="col-md-3 control-label">Limit Amount</label>

							<div class="col-md-8">
								<input type="text" class="editUserCntrls form-control"
									id="sect-limit-amt" name="amount" 
									required>
							</div>
						</div>	
						
						<div class="form-group">
							<label for="cou-name" class="col-md-3 control-label">Rate</label>

							<div class="col-md-8">
								<input type="text" class="editUserCntrls form-control"
									id="sect-rate" name="rate" 
									required>
							</div>
						</div>	
						
						<div class="form-group">
							<label for="cou-name" class="col-md-3 control-label">Rate Type</label>

							<div class="col-md-8">
								<select class="form-control" id="sect-div-fact" name="divFactor" required>
									<option value="">Select Rate Type</option>
									<option value="100">Percentage</option>
									<option value="1000">Per Mille</option>
									<option value="1">Amount</option>
								</select>
							</div>
						</div>
<%--						<div class="form-group">--%>
<%--							<label for="cou-name" class="col-md-3 control-label">Number of Earnings</label>--%>

<%--							<div class="col-md-8">--%>
<%--								<input type="text" class="editUserCntrls form-control"--%>
<%--									   id="sect-annual-earning" name="annualEarnings"--%>
<%--									   required>--%>
<%--							</div>--%>
<%--						</div>--%>
<%--						--%>
						<div class="form-group">
							<label for="cou-name" class="col-md-3 control-label">Free Limit</label>

							<div class="col-md-8">
								<input type="text" class="editUserCntrls form-control"
									id="sect-free-limit" name="freeLimit" 
									required>
							</div>
						</div>
						
					</form>
				</div>
				<div class="modal-footer">
					<button data-loading-text="Saving..." id="saveRiskSection"
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


<div class="modal fade" id="beneficiaryModals" tabindex="-1" role="dialog"
	 aria-labelledby="beneficiaryModalsLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="beneficiaryModalsLabel">
					Edit/Add Beneficiary
				</h4>
			</div>
			<div class="modal-body">

				<form id="risk-ben-form" class="form-horizontal">
					<input type="hidden" class="form-control" id="risk-ben-code-pk" name="riskId">
					<input type="hidden" class="form-control" id="risk-bencode-pk" name="bwbId">
					<div class="form-group">
						<label for="cou-name" class="col-md-3 control-label">Full Name</label>

						<div class="col-md-8">
							<input type="text" class="editUserCntrls form-control" id="fullName"
								    name="fullName"
								   required>
						</div>
					</div>

					<div class="form-group">
						<label for="cou-name" class="col-md-3 control-label">Occupation</label>

						<div class="col-md-8">
							<input type="text" class="editUserCntrls form-control" id="occupation"
								   name="occupation"
								   required>
						</div>
					</div>
					<div class="form-group">
						<label for="cou-name" class="col-md-3 control-label">Salary</label>

						<div class="col-md-8">
							<input type="text" class="editUserCntrls form-control" id="salary"
								   name="salary"
								   required>
						</div>
					</div>


				</form>
			</div>
			<div class="modal-footer">
				<button data-loading-text="Saving..." id="saveRiskBeneficiary"
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


<div class="modal fade" id="commisionsModal" tabindex="-1" role="dialog"
	 aria-labelledby="commisionsModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="commisionsModalLabel">
					Commission Breakdown
				</h4>
			</div>
			<div class="modal-body">
				<table id="allocationcomm_tbl" class="table table-hover table-bordered">
					<thead>
					<tr>
						<th>Receipt No</th>
						<th>Instalment No</th>
						<th>Cover</th>
						<th>Cover Premium</th>
						<th>Allocated Premium</th>
						<th>Comm Rate</th>
						<th>Comm Amount</th>
					</tr>
					</thead>
					<tbody>

					</tbody>
				</table>



			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">
					OK
				</button>
			</div>
		</div>
	</div>
</div>




<div class="modal fade" id="questionnaireModal" tabindex="-1" role="dialog"
	 aria-labelledby="questionnaireModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="questionnaireModalLabel">
					Questionnaire
				</h4>
			</div>
			<div class="modal-body">
				<div id="surveyElement">
				</div>

				<div id="surveyResult">

				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">
					OK
				</button>
			</div>
		</div>
	</div>
</div>




<div class="modal fade" id="beneficiaryModal" tabindex="-1" role="dialog"
	 aria-labelledby="beneficiaryModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="beneficiaryModalLabel">Edit/Add Beneficiary</h4>
			</div>
			<div class="modal-body">

				<form id="beneficiary-form" class="form-horizontal">
					<input type="hidden" class="form-control" id="part-code" name="beneficiaryCode">
					<input type="hidden" class="form-control" id="part-pol-code" name="policy">
					<div class="form-group">
						<div class="col-md-6 col-xs-12">
							<label for="houseId" class="control-label col-md-5">
								Name<span class="required">*</span></label>
							<div class="col-md-7 col-xs-12">
								<input type="text" name="beneficiaryName" id="part-name" class="form-control"
									   placeholder="Name" required>
							</div>
						</div>
						<div class="col-md-6 col-xs-12">
							<label for="houseId" class="control-label col-md-5">
								Type<span class="required">*</span></label>
							<div class="col-md-7 col-xs-12">
								<select class="form-control" id="part-type" name="beneficiaryType" required>
									<option value="">Select Type</option>
									<option value="C">Contigent Beneficiary</option>
									<option value="B">Beneficiary</option>
								</select>
							</div>
						</div>

					</div>
					<div class="form-group">
						<div class="col-md-6 col-xs-12">
							<label for="houseId" class="control-label col-md-5">
								Postal Address</label>
							<div class="col-md-7 col-xs-12">
								<input type="text" name="beneficiarypostalAddress" id="part-post-address" class="form-control"
									   placeholder="Postal Address" required>
							</div>
						</div>
						<div class="col-md-6 col-xs-12">
							<label for="houseId" class="control-label col-md-5">
								Email Address</label>
							<div class="col-md-7 col-xs-12">
								<input type="email" name="beneficiaryemailAddress" id="part-email" class="form-control"
									   placeholder="Email Address">
							</div>
						</div>

					</div>
					<div class="form-group">
						<div class="col-md-6 col-xs-12">
							<label for="houseId" class="control-label col-md-5">
								Share(%)<span class="required">*</span></label>
							<div class="col-md-7 col-xs-12">
								<input type="number" name="benAllocation" id="ben-allocation" class="form-control"
									   placeholder="Share" required>
							</div>
						</div>
						<div class="col-md-6 col-xs-12">
							<label for="houseId" class="control-label col-md-5">
								Tel No</label>
							<div class="col-md-7 col-xs-12">
								<input type="text" name="beneficiarytelNo" id="part-tel" class="form-control"
									   placeholder="Tel No">
							</div>
						</div>

					</div>
					<div class="form-group">
						<div class="col-md-6 col-xs-12">
							<label for="houseId" class="control-label col-md-5 dob">
								Date of Birth<span class="required">*</span></label>
							<div class="col-md-7 col-xs-12">
								<div class='input-group date datepicker-input'>
									<input type='text' class="form-control pull-right" name="dateRegistered"
										   id="date-reg" required />
									<div class="input-group-addon">
										<span class="glyphicon glyphicon-calendar"></span>
									</div>
								</div>
							</div>
						</div>
						<div class="col-md-6 col-xs-12">
							<label for="houseId" class="control-label col-md-5 idno">
								ID No<span class="required">*</span></label>
							<div class="col-md-7 col-xs-12">
								<input type="text" name="beneficiaryregNo" id="part-id-no" class="form-control"
									   placeholder="ID No" required>
							</div>
						</div>

					</div>

					<div class="form-group">
						<div class="col-md-6 col-xs-12">
							<label for="houseId" class="control-label col-md-5 dob">
								RelationShip<span class="required">*</span></label>

							<div class="col-md-7 col-xs-12">
								<input type="hidden" id="relation-type-id" name="relationshipType"/>
								<input type="hidden" id="relation-type">
								<input type="hidden" id="relation-desc">
								<div id="relationship-type" class="form-control"
									 select2-url="<c:url value="/protected/setups/selRelationTypes"/>" >

								</div>

							</div>
						</div>
						<div class="col-md-6 col-xs-12">

						</div>

					</div>

				</form>
			</div>
			<div class="modal-footer">
				<button data-loading-text="Saving..." id="saveBeneficiaryBtn"
						type="button" class="btn btn-success">Save</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">
					Cancel</button>
			</div>
		</div>
	</div>
</div>
	
<div class="modal fade" id="reportsModal" tabindex="-1" role="dialog"
	aria-labelledby="reportsModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="reportsModalLabel">Reports</h4>
			</div>
			<div class="modal-body">
				<div class="row">
					<div class="col-md-6">

						<ul style="list-style-type: none;" class="reports-links">
							<li class="risk-note-dip"><a
									href="<c:url value='/protected/uw/policies/rpt_risk_note'/> "
									target="_blank">Risk Note</a></li>
							<li><a
									href="<c:url value='/protected/uw/policies/rpt_debit_note'/> "
									target="_blank">Debit/Credit Note</a></li>
							<li><a
									href="<c:url value='/protected/uw/policies/rpt_prem_working'/> "
									target="_blank">Premium Working</a></li>
							<li class="endorse-disp"><a
									href="<c:url value='/protected/uw/policies/rpt_endorse'/> "
									target="_blank">Endorsement Report</a></li>
							<li class="motor-disp"><a
									href="<c:url value='/protected/uw/policies/rpt_renewal_notice_motor'/> "
									target="_blank">Renewal Notice</a></li>
							<li class="motor-disp"><a
									href="<c:url value='/protected/uw/policies/rpt_valuation_rpt'/> "
									target="_blank">Valuation Report</a></li>
<%--							<li ><a href="javascript:void(0);"  onclick="UTILITIES.downloadPolicyDoc();">Policy Document</a></li>--%>
							<li class="non-motor-disp"><a
									href="<c:url value='/protected/uw/policies/rpt_renewal_notice_non_motor'/> "
									target="_blank">Renewal Notice</a></li>
							<li ><a href="javascript:void(0);"  onclick="UTILITIES.sendEmail();">Send Email</a></li>
						</ul>
					</div>
					<div class="col-md-6">

						<ul style="list-style-type: none;">
							<!--<li><a href="#">Invoice Note</a></li> -->
						</ul>

					</div>
				</div>


			</div>
			<div class="modal-footer">

				<button type="button" class="btn btn-default" data-dismiss="modal">
					Close</button>
			</div>
		</div>
	</div>
</div>



<div class="modal fade" id="lifereportsModal" tabindex="-1" role="dialog"
	 aria-labelledby="lifereportsModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="lifereportsModalLabel">Reports</h4>
			</div>
			<div class="modal-body">
				<div class="row">
					<div class="col-md-6">

						<ul style="list-style-type: none;" class="reports-links">
							<li><a
									href="<c:url value='/protected/uw/policies/rpt_risk_note'/> "
									target="_blank">Policy Schedule</a></li>

							<li ><a href="javascript:void(0);"  onclick="UTILITIES.sendEmail();">Send Email</a></li>
						</ul>
					</div>
					<div class="col-md-6">

						<ul style="list-style-type: none;">
							<!--<li><a href="#">Invoice Note</a></li> -->
						</ul>

					</div>
				</div>


			</div>
			<div class="modal-footer">

				<button type="button" class="btn btn-default" data-dismiss="modal">
					Close</button>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="emailModal" tabindex="-1" role="dialog"
	 aria-labelledby="emailModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="emailModalLabel">Send Email</h4>
			</div>
			<div class="modal-body">
				<form id="email-form"  class="form-horizontal">
					<div class="form-group">
						<label for="cou-name" class="col-md-2 control-label">Email To</label>

						<div class="col-md-10">
							<select class="form-control" id="email-to" name="receiverType">
								<option value="">Select Email To</option>
								<option value="C">Client</option>
								<option value="A">Intermediary</option>
								<option value="IN">Inhouse</option>
								<option value="B">Both</option>

							</select>
						</div>
					</div>
					<div class="form-group">
						<label for="cou-name" class="col-md-2 control-label">Send To</label>

						<div class="col-md-10">
							<input type="text" class="editUserCntrls form-control"
								   id="email-send-to" name="sendTo"
								   readonly>
						</div>
					</div>
					<div class="form-group">
						<label for="cou-name" class="col-md-2 control-label">CC</label>

						<div class="col-md-10">
							<input type="text" class="editUserCntrls form-control"
								   id="email-cc" name="sendCC"
								   >
						</div>
					</div>
					<div class="form-group">
						<label for="cou-name" class="col-md-2 control-label">BCC</label>

						<div class="col-md-10">
							<input type="text" class="editUserCntrls form-control"
								   id="email-bcc" name="sendBcc"
								   >
						</div>
					</div>
					<div class="form-group">
						<label for="cou-name" class="col-md-2 control-label">Reports</label>

						<div class="col-md-10">
							<label class="checkbox-inline risk-note-dip"><input type="checkbox" value="RN">Risk Note</label>
							<label class="checkbox-inline"><input type="checkbox" value="DN">Debit Note</label>
							<label class="checkbox-inline"><input type="checkbox" value="PW">Premium Working</label>
							<label class="checkbox-inline"><input type="checkbox" value="RT">Renewal Notice</label>
							<label class="checkbox-inline"><input type="checkbox" value="VR">Valuation Report</label>
							<label class="checkbox-inline endorse-disp"><input type="checkbox" value="ER">Endorsement Report</label>
						</div>
					</div>
					<div class="form-group">
						<label for="email-subject" class="col-md-2 control-label">Subject</label>

						<div class="col-md-10">
							<input type="text" class="editUserCntrls form-control"
								   id="email-subject" name="subject"
								   required>
						</div>
					</div>
					<div class="form-group">
						<label for="email-template" class="col-md-2 control-label">Email Template</label>

						<div class="col-md-10">
							<textarea class="form-control" rows="10" cols="20" id="email-template" name="message"></textarea>
							</div>
						</div>

				</form>

			</div>
			<div class="modal-footer">
				<button data-loading-text="Saving..." id="sendEmailForm"
						type="button" class="btn btn-success">Send</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">
					Close</button>
			</div>
		</div>
	</div>
</div>


<div class="modal fade" id="premItemsModal" tabindex="-1" role="dialog"
	aria-labelledby="premItemsModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="premItemsModalLabel">Add A Premium Item</h4>
			</div>
			<div class="modal-body">
				 <table id="new_prem_items_form" class="table table-hover table-bordered">
					<thead>
						<tr>
						    <th width="1%"></th>
			                <th>Premium Item</th>
							<th>Limit Amount</th>
							<th>Rate</th>
							<th>Div Factor</th>
							<th>Free Limit</th>
						</tr>
					</thead>
				   </table>
                    <form id="new-prem-items-form">
				     <input type="hidden" id="prem-item-risk-id" name="riskId"/>
				    </form>

			</div>
			<div class="modal-footer">
                <button data-loading-text="Saving..." id="savenewPremItem"
					type="button" class="btn btn-success">Save</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">
					Close</button>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="newclausesModal" tabindex="-1" role="dialog"
	aria-labelledby="newclausesModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="newclausesModalLabel">Add A Clause</h4>
			</div>
			<div class="modal-body">
				<div style="height: 300px !important; overflow: scroll;">
				 <table id="new_clause_tbl" class="table table-hover table-bordered">
					<thead>
						<tr>
						    <th width="1%"></th>
			                <th>Clause Header</th>
						</tr>
					</thead>
				   </table>
					</div>
                    <form id="new-clause-form">
				    <input type="hidden" id="clause-pol-id" name="polId"/>
				    </form>

			</div>
			<div class="modal-footer">
                <button data-loading-text="Saving..." id="savenewClause"
					type="button" class="btn btn-success">Save</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">
					Close</button>
			</div>
		</div>
	</div>
</div>


<div class="modal fade" id="clauseModal" tabindex="-1" role="dialog"
		aria-labelledby="clauseModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="clauseModalLabel">
						Edit A Clause
					</h4>
				</div>
				<div class="modal-body">
				   
					<form id="pol-clause-form" class="form-horizontal">
						<input type="hidden" class="form-control" id="pol-clause-code" name="polClauseId">
						<input type="hidden" class="form-control" id="sub-clause-code" name="clause">
						<input type="hidden" class="form-control" id="pol-new-clause" name="newClause">
						<input type="hidden" class="form-control" id="clause-pol-code" name="policy">
						<div class="form-group">
							<label for="brn-id" class="col-md-3 control-label">Clause ID</label>

							<div class="col-md-8">
								<input type="text" class="form-control" id="sub-clau-id"
									name="clauShtDesc"  readonly>
							</div>
						</div>
						<div class="form-group">
							<label for="cou-name" class="col-md-3 control-label">Clause Heading</label>

							<div class="col-md-8">
								<input type="text" class="editUserCntrls form-control"
									id="sub-clause-name" name="clauHeading" 
									required>
							</div>
						</div>	
						<div class="form-group">
							<label for="rate-taxable" class="col-md-3 control-label">Editable</label>
							<div class="col-md-9 checkbox">
							     <label>
								 <input type="checkbox" name="editable" id="chk-cl-editable" onclick="return false;">
								 </label>
							</div>
						</div>
						<div class="form-group">
							<label for="cou-name" class="col-md-3 control-label">Clause Wording</label>

							<div class="col-md-8">
								<textarea rows="7" cols="20" class="form-control" id="sub-cla-wording" name="clauWording"></textarea>
							</div>
						</div>	
						
						
						
						
					</form>
				</div>
				<div class="modal-footer">
					<button data-loading-text="Saving..." id="savepolClauseBtn"
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
						Edit Tax Rates
					</h4>
				</div>
				<div class="modal-body">
				   
					<form id="pol-taxes-form" class="form-horizontal">
						<input type="hidden" class="form-control" id="tax-code" name="polTaxId">
						<input type="hidden" class="form-control" id="tax-pol-code" name="policy">
						<input type="hidden" class="form-control" id="tax-rev-code" name="revenueItems">
						<input type="hidden" class="form-control" id="pol-tax-sub-code" name="subclass">
						<div class="form-group">
							<label for="cou-name" class="col-md-3 control-label">Trans Code</label>

							<div class="col-md-8">
								<p class="form-control-static" id="tax-trans-code"> </p>
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
						
							
						
						
					</form>
				</div>
				<div class="modal-footer">
					<button data-loading-text="Saving..." id="savepoltaxesBtn"
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
	
	<div class="modal fade" id="endorseRemarksModal" tabindex="-1" role="dialog"
	aria-labelledby="endorseRemarksModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="endorseRemarksModalLabel">Select Remarks</h4>
			</div>
			<div class="modal-body">
				 <table id="remarks_tbl" class="table table-hover table-bordered">
					<thead>
						<tr>
			                <th>Remark ID</th>
			                <th>Remarks</th>
						</tr>
					</thead>
				   </table>
                    <form id="new-remark-form">
				         <input type="hidden" id="remark-pol-id" name="polId"/>
				    </form>

			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">
					Close</button>
			</div>
		</div>
	</div>
</div>




<div class="modal fade" id="riskCertModal" tabindex="-1" role="dialog"
		   aria-labelledby="riskCertModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="riskCertModalLabel">Add Risk Certificate</h4>
			</div>
			<div class="modal-body">

				<form id="new-cert-form" class="form-horizontal">
					<input type="hidden" id="cert-risk-id" name="riskId"/>
					<div class="form-group">
						<label for="brn-id" class="col-md-4 control-label">Certificate Type</label>

						<div class="col-md-8">
							<input type="hidden" id="subcls-cert-id" name="subclasscertId"/>
							<div id="risk-cert-div" class="form-control"
								 select2-url="<c:url value="/protected/uw/policies/selectSubclassCertTypes"/>" >

							</div>
						</div>
					</div>
					<div class="form-group">

						<label for="brn-id" class="col-md-4 control-label">Date
							From</label>

						<div class="col-md-8">
							<div class='input-group date datepicker-input' id="wef-date">
								<input type='text' class="form-control pull-right" name="wefDate"
									   id="cert-from-date" required />
								<div class="input-group-addon">
									<span class="glyphicon glyphicon-calendar"></span>
								</div>
							</div>
						</div>
					</div>
					<div class="form-group">
						<label for="noOfUnits" class="control-label col-md-4">Date
							To</label>
						<div class="col-md-8">
							<div class='input-group date datepicker-input' id="cover-to-date">
								<input type='text' class="form-control pull-right" name="wetDate"
									   id="cert-wet-date" required />
								<div class="input-group-addon">
									<span class="glyphicon glyphicon-calendar"></span>
								</div>
							</div>

						</div>


					</div>
				</form>

			</div>
			<div class="modal-footer">
				<button data-loading-text="Saving..." id="saveRiskCert"
						type="button" class="btn btn-success">
					Save
				</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">
					Close</button>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="editRiskCertModal" tabindex="-1" role="dialog"
	 aria-labelledby="editRiskCertModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="editRiskCertModalLabel">Edit Risk Certificate</h4>
			</div>
			<div class="modal-body">

				<form id="edit-cert-form" class="form-horizontal">
					<input type="hidden" id="cert-pc-id" name="pcId"/>
					<div class="form-group">
						<label for="brn-id" class="col-md-3 control-label">Certificate Type</label>

						<div class="col-md-8">
							<p class="form-control-static" id="risk-certtype-name"> </p>
						</div>
					</div>
					<div class="form-group">
						<label for="cou-name" class="col-md-3 control-label">Status</label>

						<div class="col-md-8">
							<select class="form-control" id="risk-cert-status" name="status">
								<option value="">Select Status</option>
								<option value="A">Active</option>
								<option value="C">Cancelled</option>
								<option value="I">Inactive</option>
							</select>
						</div>
					</div>
					<div class="form-group">

						<label for="brn-id" class="col-md-3 control-label">Date
							From</label>

						<div class="col-md-8">
							<div class='input-group date datepicker-input' id="risk-cover-from-date">
								<input type='text' class="form-control pull-right" name="wefDate"
									   id="risk-cert-from-date" required />
								<div class="input-group-addon">
									<span class="glyphicon glyphicon-calendar"></span>
								</div>
							</div>
						</div>
					</div>
					<div class="form-group">
						<label for="noOfUnits" class="control-label col-md-3">Date
							To</label>
						<div class="col-md-8">
							<div class='input-group date datepicker-input' id="risk-cover-to-date">
								<input type='text' class="form-control pull-right" name="wetDate"
									   id="risk-cert-wet-date" required />
								<div class="input-group-addon">
									<span class="glyphicon glyphicon-calendar"></span>
								</div>
							</div>
						</div>
					</div>
					<div class="form-group cert-cancellation">
						<label for="cou-name" class="col-md-3 control-label">Reason Cancelled</label>

						<div class="col-md-8">
							<textarea rows="7" cols="20" class="form-control" id="risk-cancelled-reason" name="reasonCancelled"></textarea>
						</div>
					</div>
					<div class="form-group cert-cancellation">
						<label for="noOfUnits" class="control-label col-md-3">Date
							Cancelled</label>
						<div class="col-md-8">
							<div class='input-group date datepicker-input' id="risk-cancelled-date">
								<input type='text' class="form-control pull-right" name="dateCancelled"
									   id="risk-canc-date" required />
								<div class="input-group-addon">
									<span class="glyphicon glyphicon-calendar"></span>
								</div>
							</div>

						</div>


					</div>
				</form>

			</div>
			<div class="modal-footer">
				<button data-loading-text="Saving..." id="updateRiskCert"
						type="button" class="btn btn-success">
					Save
				</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">
					Close</button>
			</div>
		</div>
	</div>
</div>



<div class="modal fade" id="newtaxesModal" tabindex="-1" role="dialog"
	 aria-labelledby="newtaxesModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-md">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="newtaxesModalLabel">Add A Tax</h4>
			</div>
			<div class="modal-body">
				<table id="new_taxes_tbl" class="table table-hover table-bordered">
					<thead>
					<tr>
						<th width="1%"></th>
						<th>Tax Name</th>
						<th>Tax Rate</th>
						<th>Rate Type</th>
					</tr>
					</thead>
				</table>
				<form id="new-taxes-form">
					<input type="hidden" id="new-tax-pol-id" name="polId"/>
				</form>

			</div>
			<div class="modal-footer">
				<button data-loading-text="Saving..." id="savenewTaxes"
						type="button" class="btn btn-success">Save</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">
					Close</button>
			</div>
		</div>
	</div>
</div>


<div class="modal fade" id="riskscheduleModal" tabindex="-1" role="dialog"
	 aria-labelledby="riskscheduleModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="riskscheduleModalLabel">Add/Edit Vehicle details</h4>
			</div>
			<div class="modal-body">

				<form id="new-risk-schedule-form" class="form-horizontal">
					<input type="hidden" id="schedule-pk-id" name="vdId"/>
					<input type="hidden" id="schedule-risk-id" name="riskId"/>

					<div class="form-group m-b-10">
						<label class="col-md-3 control-label">Chasis No<span class="text-danger">*</span></label>
						<div class="col-md-9">
							<input type="text" class="form-control" id="chasisno" name="ChassisNo" required>
						</div>
					</div>
					<div class="form-group m-b-10">
						<label class="col-md-3 control-label">Engine No</label>
						<div class="col-md-9">
							<input type="text" class="form-control" id="engineno" name="engineNumber">
						</div>
					</div>
					<div class="form-group m-b-10">
						<label class="col-md-3 control-label">Body Type<span class="text-danger">*</span></label>
						<div class="col-md-9">
							<input type="text" class="form-control" id="body-type" name="bodyType" required>
						</div>
					</div>
					<div class="form-group m-b-10">
						<label class="col-md-3 control-label">Make<span class="text-danger">*</span></label>
						<div class="col-md-9">
							<input type="text" class="form-control" id="make" name="carMake" required>
						</div>
					</div>
					<div class="form-group m-b-10">
						<label class="col-md-3 control-label">Model<span class="text-danger">*</span></label>
						<div class="col-md-9">
							<input type="text" class="form-control" id="model" name="carModel" required>
						</div>
					</div>
					<div class="form-group m-b-10">
						<label class="col-md-3 control-label">YOM<span class="text-danger">*</span></label>
						<div class="col-md-9">
							<input type="number" class="form-control" id="yom" name="yearOfManufacture" maxlength="4" minlength="4"
								   required>
						</div>
					</div>
					<div class="form-group m-b-10">
						<label class="col-md-3 control-label">Color<span class="text-danger">*</span></label>
						<div class="col-md-9">
							<input type="text" class="form-control" id="color" name="bodyColor" required>
						</div>
					</div>
					<div class="form-group m-b-10">
						<label class="col-md-3 control-label">Log Book No</label>
						<div class="col-md-9">
							<input type="text" class="form-control" id="logbookno" name="logbookNumber">
						</div>
					</div>
					<div class="form-group m-b-10">
						<label class="col-md-3 control-label">Tonnage</label>
						<div class="col-md-9">
							<input type="number" class="form-control" id="tonnage" name="tonnage">
						</div>
					</div>
					<div class="form-group m-b-10">
						<label class="col-md-3 control-label">Rating/Engine Size<span
								class="text-danger">*</span></label>
						<div class="col-md-9">
							<input type="number" class="form-control" id="cubiccapacity" name="engineCapacity">
						</div>
					</div>
					<div class="form-group m-b-10">
						<label class="col-md-3 control-label">Seating Capacity<span class="text-danger">*</span></label>
						<div class="col-md-9">
							<input type="number" class="form-control" id="cc" name="carryCapacity" required>
						</div>
					</div>

				</form>

			</div>
			<div class="modal-footer">
				<button data-loading-text="Saving..." id="saveRiskSchedules"
						type="button" class="btn btn-success">
					Save
				</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">
					Close</button>
			</div>
		</div>
	</div>
</div>


<div class="modal fade" id="assignModal" tabindex="-1" role="dialog"
	 aria-labelledby="assignModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="assignModalLabel">Assign Ticket to User</h4>
			</div>
			<div class="modal-body">

				<form id="assign-user-form" class="form-horizontal">
					<div class="form-group">
						<label for="brn-id" class="col-md-4 control-label">Select User</label>

						<div class="col-md-8">
							<input type="hidden" id="assign-user-name" name="username"/>
							<div id="user-assignee-div" class="form-control"
								 select2-url="<c:url value="/protected/organization/managers"/>" >

							</div>
						</div>
					</div>

				</form>

			</div>
			<div class="modal-footer">
				<button data-loading-text="Saving..." id="saveTicket"
						type="button" class="btn btn-success">
					OK
				</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">
					Close</button>
			</div>
		</div>
	</div>
</div>


<div class="modal fade" id="riskdocModal" tabindex="-1" role="dialog"
	 aria-labelledby="riskdocModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<form id="risk-doc-form" class="form-horizontal" enctype="multipart/form-data">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="riskdocModalLabel">Upload Risk Document</h4>
			</div>
			<div class="modal-body">

					<input type="hidden" id="risk-doc-id" name="docId"/>
					<div class="form-group">
						<label for="cou-name" class="col-md-3 control-label">Document Type</label>

						<div class="col-md-8">
							<p class="form-control-static" id="risk-doc-name"></p>
						</div>
					</div>
					<div class="form-group">
						<label for="cou-name" class="col-md-3 control-label">Uploaded File Name</label>

						<div class="col-md-8">
							<p class="form-control-static" id="risk-upload-name"></p>
						</div>
					</div>
					<div class="form-group">
						<label for="brn-id" class="col-md-4 control-label">Document</label>

						<div class="col-md-8">
							<div class="input-group col-xs-12">
								<input name="file" type="file" id="avatar" required>
							</div>
						</div>
					</div>



			</div>
			<div class="modal-footer">
				<input  value="Upload"
						type="submit" class="btn btn-success">

				</input>
				<button type="button" class="btn btn-default" data-dismiss="modal">
					Close</button>
			</div>
		</div>
		</form>
	</div>
</div>



<div class="modal fade" id="intPartiesModal" tabindex="-1" role="dialog"
	 aria-labelledby="intPartiesModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-md">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="intPartiesModalLabel">Add Interested Parties</h4>
			</div>
			<div class="modal-body">
				<table id="interested_party_tbl" class="table table-hover table-bordered">
					<thead>
					<tr>
						<th width="1%"></th>
						<th>Name</th>
						<th>Type</th>
						<th>Pin</th>
						<th>ID No/Reg No</th>
					</tr>
					</thead>
				</table>
				<form id="new-intparties-form">
					<input type="hidden" id="new-part-risk-id" name="riskId"/>
				</form>

			</div>
			<div class="modal-footer">
				<button data-loading-text="Saving..." id="saveNewIntParties"
						type="button" class="btn btn-success">Save</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">
					Close</button>
			</div>
		</div>
	</div>
</div>


<div class="modal fade" id="importRisksModal" tabindex="-1" role="dialog"
	 aria-labelledby="importRisksModalLabel" aria-hidden="true">
	<form id="risks-upload-form" class="form-horizontal" enctype="multipart/form-data">
		<div class="modal-dialog modal-md">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="importRisksModalLabel">Import Risks</h4>
				</div>
				<div class="modal-body">

					<input type="hidden" class="form-control" id="rates-bind-code" name="binder">
					<div class="form-group">
						<label for="houseName" class="control-label col-md-5">
							Excel File<span class="required">*</span></label>
						<div class="col-md-7 col-xs-12">
							<input name="file" type="file" id="file-avatar" required>
						</div>
					</div>
					<div class="form-group">
						<label for="houseName" class="control-label col-md-5">
							Cover Type/Sub Class<span class="required">*</span></label>
						<div class="col-md-7 col-xs-12">
							<input type="hidden" id="excel-binder-det-id" name="binderDetails"/>
							<div id="import-covertypes-frm" class="form-control"
								 select2-url="<c:url value="/protected/uw/policies/riskSubCoverTypes"/>" >

							</div>
						</div>
					</div>
					<div class="modal-footer">
						<input type="submit" class="btn btn-success" style="margin-right: 10px;" value="Upload">
						<button type="button" class="btn btn-default" data-dismiss="modal">
							Close</button>
					</div>
				</div>
			</div>
			</div>
	</form>
</div>

<div class="modal fade" id="riskReqDocsModal" tabindex="-1" role="dialog"
	 aria-labelledby="riskReqDocsModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="riskReqDocsModalLabel">Select Required Docs</h4>
			</div>
			<div class="modal-body">
				<form class="form-horizontal">
					<div class="form-group">
						<label for="brn-id" class="col-md-3 control-label">Document Name</label>

						<div class="col-md-6">
							<input type="text" class="form-control" id="doc-name-search"
							>
						</div>
						<div class="col-md-1">
							<button  id="searchDocuments"
									 type="button" class="btn btn-primary">
								Search
							</button>
						</div>
					</div>
				</form>
				<div style="height: 300px !important; overflow: scroll;">
					<table class="table table-striped table-hover table-bordered table-fixed" id="risksReqDocsTbl">
						<thead>
						<tr>
							<th width="1%"></th>
							<th width="4%">Document Id</th>
							<th width="12%">Document Name</th>
						</tr>
						</thead>
						<tbody>

						</tbody>
					</table>
				</div>
				<form id="risk-docs-form">
					<input type="hidden" id="req-risk-code" name="subCode"/>
				</form>
			</div>
			<div class="modal-footer">
				<button data-loading-text="Saving..." id="saveRiskDocsBtn"
						type="button" class="btn btn-success">Save</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">
					Cancel</button>
			</div>
		</div>
	</div>
</div>


<div class="modal fade" id="riskimportLogsModal" tabindex="-1" role="dialog"
	 aria-labelledby="riskimportLogsModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="riskimportLogsModalLabel">Risk Import Logs</h4>
			</div>
			<div class="modal-body">
				<table id="riskImportLogTbl" class="table table-hover table-bordered">
					<thead>
					<tr>
						<th>Log</th>
					</tr>
					</thead>
				</table>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">
					Cancel</button>
			</div>
		</div>
	</div>
</div>


<div class="modal fade" id="premLimitsModal" tabindex="-1" role="dialog"
	 aria-labelledby="premLimitsModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="premLimitsModalLabel">
					Premium Item Limits
				</h4>
			</div>
			<div class="modal-body">
				<table class="table table-striped table-hover table-bordered table-fixed" id="premLimitsTbl">
					<thead>
					<tr>
						<th>Limit ID</th>
						<th>Limit Desc</th>
						<th width="4%">Value</th>
					</tr>
					</thead>
					<tbody>

					</tbody>
				</table>


			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">
					OK
				</button>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="refundModal" tabindex="-1" role="dialog"
	 aria-labelledby="refundModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-md">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="refundModalLabel" >Confirm The Amount To be Refunded To Client</h4>
			</div>
			<div class="modal-body">
				<form id = "refund-amount-form" class="form-horizontal">
					<div class="col-md-7 col-xs-12">
						<label for="brn-id" class="control-label col-md-5">Refund Amount</label>
						<div class="col-md-7 col-xs-12">
							<input type="number"   id="refund-amount" class="form-control"
								   placeholder="Refund Amount"  />
						</div>
					</div>
				</form>


			</div>
			<div class="modal-footer">
				<button data-loading-text="Saving..." id="proceedTorefund"
						type="button" class="btn btn-success">Continue</button>
				<button type="button" class="btn btn-default" data-dismiss="modal" id="stoprefund">
					Close</button>
			</div>
		</div>
	</div>
</div>