<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<script type="text/javascript" src="<c:url value="/js/modules/utils/select2builder.js"/>"></script>
<script type="text/javascript" src="<c:url value="/libs/rivets/rivets.js"/>"></script>
	<script type="text/javascript">
		var accIdpk = ${accId};
	</script>
<div class="row no-print">
	<div class="col-xs-12">
		<h4 class="pull-left blue" style="font-weight: bolder" id="h4pol">Intermediary Name: </h4>
		<input type="button" class="btn btn-danger pull-right"
			   value="Deactivate" id="btn-deactivate">

		<input type="button" class="btn btn-danger pull-right"
			   value="Reject" id="btn-reject">
		<input type="button" class="btn btn-primary pull-right"
			   value="Approve" id="btn-approve">

	</div>
</div>
<div class="x_panel" id="acct_model">
	<div class="" role="tabpanel" data-example-id="togglable-tabs">
		<ul id="myTab" class="nav nav-tabs bar_tabs" role="tablist">
			<li role="presentation" class="active"><a href="#tab_content1"
													  id="home-tab" role="tab" data-toggle="tab"
													  aria-expanded="true">Intermediary Details</a>
			</li>
			<li role="presentation" id="show-docs" style="display:none"><a href="#tab_content2"
																role="tab" id="profile-tab" data-toggle="tab"
																aria-expanded="false">Intermediary Documents</a>
			</li>
			</ul>
			<div id="myTabContent" class="tab-content">
				<div role="tabpanel" class="tab-pane fade active in"
					 id="tab_content1" aria-labelledby="home-tab">
	<form id="account-form" class="form-horizontal" enctype="multipart/form-data">
		<div class="x_panel">
			<div class="form-group">
				<a href="<c:url value='/protected/setups/accts'/> " class="btn btn-default pull-right">Back</a>
				<input type="submit" class="btn btn-success pull-right" style="margin-right: 10px;" value="Save">


			</div>
		</div>
		    <input type="hidden" name="acctId" id="acctId-pk">
			<div class="form-group form-required">
				<div class="col-md-6 col-xs-12">
					<label for="brn-id" class="col-md-5 control-label">Select
						Intermediary Type<span class="required">*</span></label>

					<div class="col-md-7 col-xs-12">
						<input type="hidden" id="acc-id" name="acctTypeId"/>
						<input type="hidden" id="acc-name">
						<input type="hidden" id="acc-type-desc">
						<div id="accounttypes" class="form-control"
							 select2-url="<c:url value="/protected/setups/selAcctTypes"/>" >

						</div>
					</div>

				</div>
				<div class="col-md-6 col-xs-12">
				<label for="parent-account-lov" class="control-label col-md-5">Select Parent Intermediary</label>
				<div class="col-md-7 col-xs-12">
					<input type="hidden" id="parent-account-id" name="parentAcctId"/>
					<input type="hidden" id="parent-account-name">
					<div id="parent-account-lov" class="form-control"
						 select2-url="<c:url value="/protected/setups/selParentAccts"/>" >

					</div>
				</div>
				</div>


			</div>
		<div class="form-group">
			<div class="col-md-6 col-xs-12">
				<label for="houseName" class="control-label col-md-5">
					Sht Desc</label>
				<div class="col-md-7 col-xs-12">
					<input type="text" name="shtDesc" id="other-names" class="form-control"
						   placeholder="Sht Desc">
				</div>
			</div>
			<div class="col-md-6 col-xs-12">
				<label for="houseId" class="control-label col-md-5">
					Name<span class="required">*</span></label>
				<div class="col-md-7 col-xs-12">
					<input type="text" name="name" id="fname" class="form-control"
						   placeholder="Account Name" required>
				</div>


			</div>


		</div>
		<div class="form-group form-required">
			<div class="col-md-6 col-xs-12">
				<label for="noOfUnits" class="control-label col-md-5">Bank</label>
				<div class="col-md-7 col-xs-12">
					<input type="hidden" id="acct-bank-id" name="bankId"/>
					<input type="hidden" id="acct-bank-name">
					<div id="bank-lov" class="form-control"
						 select2-url="<c:url value="/protected/setups/selBanks"/>" >

					</div>
				</div>

			</div>
			<div class="col-md-6 col-xs-12">
				<label for="bank-branch-lov" class="control-label col-md-5">Bank Branch</label>
				<div class="col-md-7 col-xs-12">
					<input type="hidden" id="bank-branch-id" name="bankBranchId"/>
					<input type="hidden" id="bank-branch-name">
					<div id="bank-branch-lov" class="form-control"
						 select2-url="<c:url value="/protected/setups/selBankBranches"/>" >

					</div>
				</div>

			</div>


		</div>
			<div class="form-group form-required">
				<div class="col-md-6 col-xs-12">
					<label for="houseId" class="control-label col-md-5">Account Number</label>
					<div class="col-md-7 col-xs-12">
						<input type="text" name="bankAccount" id="acct-acct-no" class="form-control"
							   placeholder="Bank Account No.">
					</div>
				</div>

				<div class="col-md-6 col-xs-12">
					<label for="noOfUnits" class="control-label col-md-5">Pin No</label>
					<div class="col-md-7 col-xs-12">
						<input type="text" name="pinNo" id="pinNo" class="form-control"
							placeholder="Pin No">
					</div>
				</div>

			</div>
			<div class="form-group form-required">
				<div class="col-md-6 col-xs-12">
					<label for="houseId" class="control-label col-md-5">Email</label>
					<div class="col-md-7 col-xs-12">
						<input type="email" name="email" id="email" class="form-control"
							   placeholder="Email">
					</div>
				</div>

				<div class="col-md-6 col-xs-12">
					<label for="noOfUnits" class="control-label col-md-5">Phone No<span class="required">*</span></label>
					<div class="col-md-7 col-xs-12">
						<input type="text" name="phoneNo" id="phone-no" class="form-control"
							placeholder="Phone No" required>
					</div>
				</div>
			</div>
			<div class="form-group">
			    <div class="col-md-6 col-xs-12 form-required">
					<label for="noOfUnits" class="control-label col-md-5">Postal Address</label>
					<div class="col-md-7 col-xs-12">
						<textarea rows="3" cols=30 class="form-control" name="address" id="address"></textarea>
					</div>
				
				</div>
				<div class="col-md-6 col-xs-12">
				    <label for="noOfUnits" class="control-label col-md-5">Physical Address</label>
					<div class="col-md-7 col-xs-12">
						<textarea rows="3" cols=30 class="form-control" name="physaddress" id="phy-address"></textarea>
					</div>
					
				</div>
				
			</div>

			<div class="form-group inhouse-agents">
			    <div class="col-md-6 col-xs-12">
					<label for="houseId" class="control-label col-md-5">Contact Title</label>
					<div class="col-md-7 col-xs-12">
						<input type="text" name="contactTitle" id="cont-title" class="form-control"
							placeholder="Contact Title">
					</div>
				
				</div>
				<div class="col-md-6 col-xs-12">
					<label for="houseId" class="control-label col-md-5">Contact Person</label>
					<div class="col-md-7 col-xs-12">
						<input type="text" name="contactPerson" id="contact-person" class="form-control"
							placeholder="Contact Person">
					</div>
				</div>
				
			</div>
			<div class="form-group form-required">
				<div class="col-md-6 col-xs-12">
					<label for="houseId" class="control-label col-md-5">Status</label>
					<div class="col-md-7 col-xs-12">
						 <select class="form-control" id="sel2" name="status" disabled>
							        <option value="">Select Status</option>
							        <option value="D">Draft</option>
							        <option value="A">Active</option>
								    <option value="I">Inactive</option>
								    <option value="DA">Deactivated</option>
								    <option value="NA">Not Approved</option>
						 </select>
					</div>
				</div>
				<div class="col-md-6 col-xs-12">
						<label for="houseId" class="control-label col-md-5">Branch</label>
					<div class="col-md-7 col-xs-12">
						 <input type="hidden" id="obId" name="branchId"/>
		                       <input type="hidden" id="ob-name">
		                        <div id="acct-branch" class="form-control" 
				                                 select2-url="<c:url value="/protected/uw/policies/allbranches"/>" >
				                                 
				               </div> 
					</div>
				</div>
			</div>
		<div class="form-group form-required">
			<div class="col-md-6 col-xs-12">
				<label for="houseId" class="control-label col-md-5">Pay Bill Number</label>
				<div class="col-md-7 col-xs-12">
					<input type="text" name="paybillNumber" id="acct-paybill" class="form-control"
						   placeholder="MPESA Paybill No.">
				</div>

			</div>
			<div class="col-md-6 col-xs-12">
				<label for="houseId" class="control-label col-md-5">Payment Tel Number</label>
				<div class="col-md-7 col-xs-12">
					<input type="text" name="payTelNo" id="acct-mob-tel" class="form-control"
						   placeholder="Mobile Tel Number">
				</div>
			</div>
		</div>
		<div class="form-group">
			<div class="col-md-6 col-xs-12">
				<label for="reg-wet" class="col-md-5 control-label" id="acct-date-reg">Date of Reg.</label>

				<div class="col-md-7 col-xs-12">
					<div class='input-group date datepicker-input'>
						<input type='text' class="form-control pull-right" name="dob" id="dob" />
						<div class="input-group-addon">
							<span class="glyphicon glyphicon-calendar"></span>
						</div>
					</div>
				</div>
			</div>
			<div class="col-md-6 col-xs-12">
				<label for="houseName" class="control-label col-md-5">
					Payment Mode</label>
				<div class="col-md-7 col-xs-12">
					<div id="edit-payment-mode">
						<input type="hidden" id="pm-id" name="paymentModeId"/>
						<input type="hidden" id="pm-name">
						<div id="pm-mode-frm" class="form-control"
							 select2-url="<c:url value="/protected/uw/policies/uwpaymentmodes"/>">

						</div>
					</div>
				</div>

			</div>
		</div>
		<div class="form-group form-required">
			<div class="col-md-6 col-xs-12">
				<label for="integration-type" class="control-label col-md-5">Integration Type</label>
				<div class="col-md-7 col-xs-12">
					<select class="form-control" id="integration-type" name="integrationType">
						<option value="">Select Integration Type</option>
						<option value="D">JWT</option>
						<option value="A">OAUTH2</option>
						<option value="I">File Upload</option>
					</select>
				</div>

			</div>
			<div class="col-md-6 col-xs-12">
				<label for="integration-url" class="control-label col-md-5">Integration URL</label>
				<div class="col-md-7 col-xs-12">
					<input type="text" name="underwriterApiUrl" id="integration-url" class="form-control"
						   placeholder="Integration URL">
				</div>
			</div>
		</div>
		<div class="form-group form-required">
			<div class="col-md-6 col-xs-12">
				<label for="integration-username" class="control-label col-md-5">Integration Username</label>
				<div class="col-md-7 col-xs-12">
					<input type="text" name="underwriterApiUsername" id="integration-username" class="form-control"
						   placeholder="Integration Username">
				</div>

			</div>
			<div class="col-md-6 col-xs-12">
				<label for="integration-password" class="control-label col-md-5">Integration Password</label>
				<div class="col-md-7 col-xs-12">
					<input type="password" name="underwriterApiPassword" id="integration-password" class="form-control"
						   placeholder="Integration Password">
				</div>
			</div>
		</div>
		<div class="form-group">
			<div class="col-md-6 col-xs-12  form-required">
				<label for="brn-id" class="col-md-5 control-label">Date
					Activated</label>

				<div class="col-md-7 col-xs-12">
					<div class='input-group date datepicker-input' id="wef-date">
						<input type='text' class="form-control pull-right" name="wef"
							   id="from-date" disabled/>
						<div class="input-group-addon">
							<span class="glyphicon glyphicon-calendar"></span>
						</div>
					</div>
				</div>
			</div>
			<div class="col-md-6 col-xs-12">
				<label for="noOfUnits" class="control-label col-md-5">Date
					Deactivated</label>
				<div class="col-md-7 col-xs-12">
					<div class='input-group date datepicker-input' id="cover-to-date">
						<input type='text' class="form-control pull-right" name="wet"
							   id="wet-date" disabled/>
						<div class="input-group-addon">
							<span class="glyphicon glyphicon-calendar"></span>
						</div>
					</div>
				</div>

			</div>


		</div>
		<div class="form-group">
			<div class="col-md-6 col-xs-12">
				<label for="file" class="control-label col-md-5" id="acct-photo">
					Logo</label>
				<div class="col-md-7 col-xs-12">
					<div class="kv-avatar center-block" style="width: 200px">
						<input name="file" type="file" id="avatar" class="file-loading">

					</div>
				</div>
			</div>
			<div class="col-md-6 col-xs-12">

			</div>
		</div>

	 
	</form>
	</div>
				<div role="tabpanel" class="tab-pane fade"
					 id="tab_content2" aria-labelledby="profile-tab" style="display:none">
					<button class="btn btn-success btn btn-info" id="btn-add-docs">New</button>
					<div class="card-box table-responsive">
						<table id="accDocsList" class="table table-hover table-bordered">
							<thead>
							<tr>
								<th>Document ID</th>
								<th>Document Desc</th>
								<th>File Name</th>
								<th>File Verifier</th>
								<th width="5%"></th>
								<th width="5%"></th>
								<th width="5%"></th>
							</tr>
							</thead>
						</table>
					</div>
				</div>
				</div>
			</div>
				</div>


<div class="modal fade" id="acctReqDocsModal" tabindex="-1" role="dialog"
	 aria-labelledby="acctReqDocsModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="acctReqDocsModalLabel">Select Required Docs</h4>
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
					<table class="table table-striped table-hover table-bordered table-fixed" id="acctDocsTbl">
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
				<form id="acct-doc-form">
					<input type="hidden" id="req-acct-code" name="subCode"/>
				</form>
			</div>
			<div class="modal-footer">
				<button data-loading-text="Saving..." id="saveAcctDocsBtn"
						type="button" class="btn btn-success">Save</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">
					Cancel</button>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="acctdocModal" tabindex="-1" role="dialog"
	 aria-labelledby="acctdocModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<form id="accts-doc-form" class="form-horizontal" enctype="multipart/form-data">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="acctdocModalLabel">Upload Account Document</h4>
				</div>
				<div class="modal-body">

					<input type="hidden" id="acct-doc-id" name="docId"/>
					<input type="hidden" id="reqd-doc-id" name="requiredDoc"/>
					<div class="form-group">
						<label for="cou-name" class="col-md-3 control-label">Document Type</label>

						<div class="col-md-8">
							<p class="form-control-static" id="acct-doc-name"></p>
						</div>
					</div>
					<div class="form-group">
						<label for="cou-name" class="col-md-3 control-label">Uploaded File Name</label>

						<div class="col-md-8">
							<p class="form-control-static" id="acct-upload-name"></p>
						</div>
					</div>
					<div class="form-group">
						<label for="brn-id" class="col-md-4 control-label">Document</label>

						<div class="col-md-8">
							<div class="input-group col-xs-12">
								<input name="file" type="file" id="acct-avatar" required>
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

<script>

	$(function(){

		$(document).ready(function() {
			AccountDef.init();
		});

	});

	var AccountDef = (function($){
		'use strict';

		var pinNoElement = $('#pinNo');
		var model = {
			accounts: {
				accType:{
					accId:"",
				},
				branch:{
					brnCode:"",
				},
			}
		};

		var accountImage = function(id){
			$("#avatar").fileinput('refresh',{
				overwriteInitial: true,
				maxFileSize: 1500,
				showClose: false,
				showCaption: false,
				browseLabel: '',
				removeLabel: '',
				browseIcon: '<i class="fa fa-folder-open"></i>',
				removeIcon: '<i class="fa fa-times"></i>',
				removeTitle: 'Cancel or reset changes',
				elErrorContainer: '#kv-avatar-errors',
				msgErrorClass: 'alert alert-block alert-danger',
				defaultPreviewContent: '<img src="'+SERVLET_CONTEXT+'/protected/setups/accountImage/'+id+'"  style="width:180px">',
				layoutTemplates: {main2: '{preview} ' + ' {remove} {browse}'},
				allowedFileExtensions: ["jpg", "png", "gif"]
			});
		};

		var createAccount = function(){
			var $form = $("#account-form");
			var validator = $form.validate();
			$('form#account-form')
					.submit( function( e ) {
						e.preventDefault();
						if (!$form.valid()) {
							return;
						}
						var data = new FormData( this );
						data.append( 'file', $( '#avatar' )[0].files[0] );
						$.ajax( {
							url: 'createAccount',
							type: 'POST',
							data: data,
							processData: false,
							contentType: false,
							success: function (s ) {
								accIdpk = s;
								$("#acctId-pk").val(s);
								getAccountDetails();
								new PNotify({
									title: 'Success',
									text: 'Record created/updated Successfully',
									type: 'success',
									styling: 'bootstrap3'
								});
							},
							error: function(jqXHR, textStatus, errorThrown){
								new PNotify({
									title: 'Error',
									text: jqXHR.responseText,
									type: 'error',
									styling: 'bootstrap3'
								});
							}
						});
					});
		};

		var createBranchSelect = function(){
			if($("#acct-branch").filter("div").html() != undefined)
			{
				Select2Builder.initAjaxSelect2({
					containerId : "acct-branch",
					sort : 'obName',
					change: function(e, a, v){
						$("#obId").val(e.added.obId);
					},
					formatResult : function(a)
					{
						return a.obName
					},
					formatSelection : function(a)
					{
						return a.obName
					},
					initSelection: function (element, callback) {
						var code = $("#obId").val();
						var name = $("#ob-name").val();
						model.accounts.branch.brnCode = code;
						var data = {obName:name,obId:code};
						callback(data);
					},
					id: "obId",
					placeholder:"Select Branch",
				});
			}
		};

		var populateParentControlAcc = function(){
			if($("#parent-account-lov").filter("div").html() != undefined)
			{
				Select2Builder.initAjaxSelect2({
					containerId : "parent-account-lov",
					sort : 'name',
					change: function(e, a, v){
						$("#parent-account-id").val(e.added.acctId);
					},
					formatResult : function(a)
					{
						return a.name;
					},
					formatSelection : function(a)
					{
						return a.name;
					},
					initSelection: function (element, callback) {
						var code  = $('#parent-account-id').val();
						var name = $("#parent-account-name").val();
						var data = {name:name,acctId:code};
						callback(data);
					},
					id: "acctId",
					width:"250px",
					placeholder:"Select Parent Account"

				});
			}
		};

		var createAccountTypeSelect = function(){
			if($("#accounttypes").filter("div").html() != undefined)
			{
				Select2Builder.initAjaxSelect2({
					containerId : "accounttypes",
					sort : 'accName',
					change:  function(e, a, v){
						$("#acc-id").val(e.added.accId);
						if(e.added.accountType){
							$("#acc-type-desc").val(e.added.accountType);
							if(e.added.accountType==="IA" || e.added.accountType ==='SUB'){
								$(".inhouse-agents").hide();
								$(".insurance-co").show();
								$("#acct-photo").text("Photo");
							}
							else{
								$(".inhouse-agents").show();
								$("#acct-photo").text("Photo");
								$(".insurance-co").hide();
							}


							if(e.added.accountType ==='SUB'){
								$("#parent-account-lov").select2("readonly", false).select2("val", "");
							}
							else{
								$("#parent-account-lov").select2("readonly", true).select2("val", "");
							}
						}
					},
					formatResult : function(a)
					{
						return a.accName
					},
					formatSelection : function(a)
					{
						return a.accName
					},
					initSelection: function (element, callback) {
						var code = $("#acc-id").val();
						var name = $("#acc-name").val();
						model.accounts.accType.accId = code;
						var data = {accName:name,accId:code};
						callback(data);
					},
					id: "accId",
					placeholder:"Select Account Type",
				});
			}
		};

		var confirmAccountDel = function(button){
			var account = JSON.parse(decodeURI($(button).data("account")));
			bootbox.confirm("Are you sure want to delete "+account["fname"]+" "+account["otherNames"]+"?", function(result) {
				if(result){
					$.ajax({
						type: 'GET',
						url:  'deleteAccount/' + account["acctId"],
						dataType: 'json',
						async: true,
						success: function(result) {
							bootbox.alert("Record deleted Successfully");
							$('#acctbl').DataTable().ajax.reload();
						},
						error: function(jqXHR, textStatus, errorThrown) {
							bootbox.alert(jqXHR.responseText);
						}
					});
				}

			});
		};



		var populatePaymentModes = function(){
			if($("#pm-mode-frm").filter("div").html() != undefined)
			{
				Select2Builder.initAjaxSelect2({
					containerId : "pm-mode-frm",
					sort : 'pmDesc',
					change: function(e, a, v){
						$("#pm-id").val(e.added.pmId);
					},
					formatResult : function(a)
					{
						return a.pmDesc;
					},
					formatSelection : function(a)
					{
						return a.pmDesc;
					},
					initSelection: function (element, callback) {
						var code  = $('#pm-id').val();
						var name = $("#pm-name").val();
						var data = {pmDesc:name,pmId:code};
						callback(data);
					},
					id: "pmId",
					width:"250px",
					placeholder:"Select Payment Mode"

				});
			}
		};

		var populateAccountDetails = function(data){
			$("#acctId-pk").val(data.acctId);
			$("#fname").val(data.name);
			$("#other-names").val(data.shtDesc);
			$("#idno").val(data.idPassportNo);
			$("#pinNo").val(data.pinNo);
			$("#email").val(data.email);
			$("#phone-no").val(data.phoneNo);
			$("#address").val(data.address);
			$("#sel2").val(data.status);
			if(data.status && data.status==='A'){
				$("#btn-deactivate").css('display','block').val('Deactivate');
				$("#btn-approve").css('display','none');
				$("#btn-reject").css('display','none');
				$('form *').prop('disabled', true);
				$('#btn-add-docs').prop('disabled', true);
			}
			else if(data.status && data.status==='D'){
				$("#btn-deactivate").css('display','none');
				$("#btn-approve").css('display','display').val('Approve');
				$("#btn-reject").css('display','block');
				$('form *').prop('disabled', false);
				$('#btn-add-docs').prop('disabled', false);

			}
			else if(data.status && data.status==='DA'){
				$("#btn-deactivate").css('display','block').val('Activate');
				$("#btn-approve").css('display','none');
				$("#btn-reject").css('display','none');
				$('form *').prop('disabled', true);
				$('#btn-add-docs').prop('disabled', true);

			}
			else if(data.status && data.status==='NA'){
				$("#btn-deactivate").css('display','none');
				$("#btn-approve").css('display','none');
				$("#btn-reject").css('display','none');
				$('form *').prop('disabled', true);
				$('#btn-add-docs').prop('disabled', true);

			}
			$("#h4pol").text("Intermediary Name: "+data.name);
			if(data.dob)
				$("#dob").val(moment(data.dob).format('DD/MM/YYYY'));
			if(data.wef)
				$("#from-date").val(moment(data.wef).format('DD/MM/YYYY'));
			if(data.wet)
				$("#wet-date").val(moment(data.wet).format('DD/MM/YYYY'));
			$("#acc-id").val(data.acctTypeId);
			$("#acc-name").val(data.accountTypeName);
			createAccountTypeSelect();
			if(data.branchId) {
				$("#obId").val(data.branchId);
				$("#ob-name").val(data.branchName);
			}
			if(data.parentAcctId){
				$("#parent-account-id").val(data.parentAcctId);
				$("#parent-account-name").val(data.parentAcctName);
				populateParentControlAcc();
			}
			$("#phy-address").val(data.physaddress);
			$("#cont-title").val(data.contactTitle);
			$("#acct-acct-no").val(data.bankAccount);
			$("#acct-paybill").val(data.paybillNumber);
			$("#contact-person").val(data.contactPerson);
			$("#acct-mob-tel").val(data.payTelNo);
			if(data.paymentMode){
				$("#pm-id").val(data.paymentModeId);
				$("#pm-name").val(data.paymentMode);
			}
			populatePaymentModes();
			if(data.accountTypeId){
				if(data.accountTypeId==="IA" || data.accountTypeId==="SUB"){
					$(".inhouse-agents").hide();
					$("#acct-photo").text("Photo");
					$("#acct-date-reg").text("Date of Birth");
					$(".insurance-co").show();
				}
				else{
					$(".inhouse-agents").show();
					$("#acct-photo").text("Logo");
					$("#acct-date-reg").text("Date of Reg");
					$(".insurance-co").hide();
				}
			}
			if(data.bankName) {
				$("#acct-bank-id").val(data.bankId);
				$("#acct-bank-name").val(data.bankName);
				createBanksForSel();
				if(data.bankBranchName) {
					$("#bank-branch-id").val(data.bankBranchId);
					$("#bank-branch-name").val(data.bankBranchName);
					populateBankBranches(data.bankId);
				}
				else{
					populateBankBranches(data.bankId);
				}
			}

			createBranchSelect();
			accountImage(data.acctId);
			getAccountDocs(data.acctId);
		};

		var getAccountDetails = function(){
			if(typeof accIdpk!== 'undefined'){
				if(accIdpk!==-2000){
					$.ajax( {
						url: 'accounts/'+accIdpk,
						type: 'GET',
						processData: false,
						contentType: false,
						success: function (s ) {
							$("#h4pol").show();
							populateAccountDetails(s);
							$("#show-docs").css("display","block");
							$("#tab_content2").css("display","block");
						},
						error: function(xhr, error){
							bootbox.alert(xhr.responseText);
						}
					});
				}
				else{
					$("#h4pol").hide();
					accountImage(-2000);
					$("#show-docs").css("display","none");
					$("#tab_content2").css("display","none");
				}

			}
		};

		var validateDetails = function(){
			pinNoElement.on('change', function () {
				var pinNo = pinNoElement.val();
				if (pinNo !== '') {
					$.ajax({
						type: 'GET',
						url: 'validatePin',
						dataType: 'json',
						data: {"pinNo": pinNo},
						async: true,
						success: function (result) {

						},
						error: function (jqXHR, textStatus, errorThrown) {
							new PNotify({
								title: 'Error',
								text: jqXHR.responseText,
								type: 'error',
								styling: 'bootstrap3'
							});
							pinNoElement.val("");
						}
					});
				}
			});
		};

		var createAccounts = function(){
			var url = "allaccounts/0";
			if ($("#accId").val() != ''){
				url = "allaccounts/"+$("#accId").val();
			}
			return $('#acctbl').DataTable(UTILITIES.extendsOpts({
				"ajaxUrl": url,
				"columns": [
					{"data": "shtDesc"},
					{"data": "name"},
					{"data": "email"},
					{"data": "phoneNo"},
					{"data": "pinNo"},
					{
						"data": "status",
						"render": function (data, type, full, meta) {
							if (!full.status || full.status === "I") {
								return "Inactive";
							} else if (full.status === "A")
								return "Active";
							else if (full.status === "DA")
								return "Deactivated";
							else if (full.status === "DR")
								return "Draft";
						}
					},
					{"data": "createdBy"},
					{"data": "updatedBy"},
					{
						"data": "acctId",
						"render": function (data, type, full, meta) {
							return '<form action="editAcctForm" method="post"><input type="hidden" name="id" value=' + full.acctId + '><input type="submit"  class="btn btn-success btn btn-info btn-xs" value="Edit" ></form>';
						}

					},
					{
						"data": "acctId",
						"render": function (data, type, full, meta) {
							return '<input type="button" class="btn btn-danger btn btn-info btn-xs" data-account=' + encodeURI(JSON.stringify(full)) + ' value="Delete" onclick="confirmAccountDel(this);"/>';
						}

					},
				]
			}));
		};


		var createBanksForSel = function(){
			if($("#bank-lov").filter("div").html() != undefined)
			{
				Select2Builder.initAjaxSelect2({
					containerId : "bank-lov",
					sort : 'bankName',
					change: function(e, a, v){
						$("#acct-bank-id").val(e.added.bnId);
						populateBankBranches(e.added.bnId);
					},
					formatResult : function(a)
					{
						return a.bankName
					},
					formatSelection : function(a)
					{
						return a.bankName
					},
					initSelection: function (element, callback) {
						var code = $("#acct-bank-id").val();
						var name = $("#acct-bank-name").val();
						var data = {bankName:name,bnId:code};
						callback(data);
					},
					id: "bnId",
					placeholder:"Select Bank",
				});
			}
		};

		var populateBankBranches = function(bankCode){
			if($("#bank-branch-lov").filter("div").html() != undefined)
			{
				Select2Builder.initAjaxSelect2({
					containerId : "bank-branch-lov",
					sort : 'branchName',
					change: function(e, a, v){
						$("#bank-branch-id").val(e.added.bbId);
					},
					formatResult : function(a)
					{
						return a.branchName
					},
					formatSelection : function(a)
					{
						return a.branchName
					},
					initSelection: function (element, callback) {
						var code = $("#bank-branch-id").val();
						var name = $("#bank-branch-name").val();
						var data = {branchName:name,bbId:code};
						callback(data);
					},
					id: "bbId",
					placeholder:"Select Bank Branch",
					params: {bankCode: bankCode}
				});
			}
		};


		var getAccountDocs = function(acctId){
			var ajaxUrl = "accountDocs/"+acctId;
			var currTable = $('#accDocsList').DataTable(UTILITIES.extendsOpts({
				"ajaxUrl":ajaxUrl,
				"columns": [
					{ "data": "requiredDoc",
						"render": function ( data, type, full, meta ) {

							return full.requiredDoc.reqShtDesc;
						}
					},
					{ "data": "requiredDoc",
						"render": function ( data, type, full, meta ) {

							return full.requiredDoc.reqDesc;
						}
					},
					{ "data": "uploadedFileName" },
					{ "data": "checkSum" },
					{
						"data": "adId",
						"render": function ( data, type, full, meta ) {
							return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-docs='+encodeURI(JSON.stringify(full)) + ' onclick="AccountDef.editAcctDocs(this);"><i class="fa fa-pencil-square-o"></button>';
						}

					},
					{
						"data": "adId",
						"render": function ( data, type, full, meta ) {
							return '<button type="button" class="btn btn-success btn btn-info btn-xs" type="button" data-docs=' + encodeURI(JSON.stringify(full)) + ' onclick="AccountDef.downloadAcctDoc(this);"><i class="fa fa-file-archive-o"></button>';

						}

					},
					{
						"data": "adId",
						"render": function ( data, type, full, meta ) {
							return '<button type="button" class="btn btn-danger btn btn-info btn-xs" data-docs='+encodeURI(JSON.stringify(full)) + ' onclick="AccountDef.deleteAcctDoc(this);"><i class="fa fa-trash-o"></button>';
						}

					},
				]
			}));
			return currTable;
		};

		var deleteAcctDoc = function(button){
			var docs = JSON.parse(decodeURI($(button).data("docs")));
			bootbox.confirm("Are you sure want to delete "+docs['requiredDoc'].reqDesc+"?", function(result) {
				if(result){
					$('#myPleaseWait').modal({
						backdrop: 'static',
						keyboard: true
					});
					$.ajax({
						type: 'GET',
						url:  'deleteAcctDoc/' + docs['adId'],
						dataType: 'json',
						async: true,
						success: function(result) {
							$('#myPleaseWait').modal('hide');
							new PNotify({
								title: 'Success',
								text: 'Record Deleted Successfully',
								type: 'success',
								styling: 'bootstrap3'
							});
							$('#accDocsList').DataTable().ajax.reload();
						},
						error: function(jqXHR, textStatus, errorThrown) {
							$('#myPleaseWait').modal('hide');
							new PNotify({
								title: 'Error',
								text: jqXHR.responseText,
								type: 'error',
								styling: 'bootstrap3'
							});
						}
					});
				}

			});
		};
		var downloadAcctDoc = function(button){
			var docs = JSON.parse(decodeURI($(button).data("docs")));
			window.open(SERVLET_CONTEXT+"/protected/setups/acctDocument/"+docs['adId'],
					'_blank' // <- This is what makes it open in a new window.
			);
		};

		var editAcctDocs = function(button){
			var docs = JSON.parse(decodeURI($(button).data("docs")));
			$("#acct-doc-name").text(docs["requiredDoc"].reqDesc);
			$("#acct-upload-name").text(docs['uploadedFileName']);
			$("#acct-doc-id").val(docs['adId']);
			$('#acctdocModal').modal({
				backdrop: 'static',
				keyboard: true
			});
		};

		var uploadAcctDocument = function(){
			var $form = $("#accts-doc-form");
			var validator = $form.validate();
			$('form#accts-doc-form')
					.submit( function( e ) {
						e.preventDefault();

						if (!$form.valid()) {
							return;
						}
						$('#myPleaseWait').modal({
							backdrop: 'static',
							keyboard: true
						});
						var data = new FormData( this );
						data.append( 'file', $( '#acct-avatar' )[0].files[0] );
						$.ajax( {
							url: 'uploadAccountDocs',
							type: 'POST',
							data: data,
							processData: false,
							contentType: false,
							success: function (s ) {
								$('#myPleaseWait').modal('hide');
								new PNotify({
									title: 'Success',
									text: 'File Uploaded Successfully',
									type: 'success',
									styling: 'bootstrap3'
								});
								$('#acctdocModal').modal('hide');
								var $el = $('#acct-avatar');
								$el.wrap('<form>').closest('form').get(0).reset();
								$el.unwrap();
								$('#accDocsList').DataTable().ajax.reload();

							},
							error: function(xhr, error){
								$('#myPleaseWait').modal('hide');
								new PNotify({
									title: 'Error',
									text: xhr.responseText,
									type: 'error',
									styling: 'bootstrap3'
								});
							}
						});
					});
		};

		var searchReqDocs = function(search){
			if($("#acctId-pk").val() != ''){
				$.ajax({
					type: 'GET',
					url:  'acctreqdocs',
					dataType: 'json',
					data: {"acctCode": $("#acctId-pk").val(),"docName":search},
					async: true,
					success: function(result) {
						$("#acctDocsTbl tbody").each(function(){
							$(this).remove();
						});
						for(var res in result){
							var markup = "<tr><td><input type='checkbox' name='record' id='"+result[res].reqId+"'></td><td>" + result[res].reqShtDesc + "</td><td>" + result[res].reqDesc + "</td></tr>";
							$("#acctDocsTbl").append(markup);
						}
						$("#req-acct-code").val($("#acctId-pk").val());
						$('#acctReqDocsModal').modal({
							backdrop: 'static',
							keyboard: true
						})
					},
					error: function(jqXHR, textStatus, errorThrown) {
						new PNotify({
							title: 'Error',
							text: jqXHR.responseText,
							type: 'error',
							styling: 'bootstrap3'
						});
					}
				});
			}
			else{
				bootbox.alert("No Account to attach Documents")
			}
		};


		var approveAccount = function(){
			var stack_bottomleft = {"dir1": "up", "dir2": "left", "firstpos1": 25, "firstpos2": 25};
			$('#myPleaseWait').modal({
				backdrop: 'static',
				keyboard: true
			});
			$.ajax({
				type: 'GET',
				url:  'approveAccount/'+accIdpk,
				dataType: 'json',
				async: true,
				success: function(result) {
					$('#myPleaseWait').modal('hide');
					new PNotify({
						title: 'Success',
						text: 'Intermediary Approved Successfully',
						type: 'success',
						styling: 'bootstrap3',
						addclass: 'stack-bottomright',
						stack: stack_bottomleft
					});
					AccountDef.getAccountDetails();
				},
				error: function(jqXHR, textStatus, errorThrown) {
					$('#myPleaseWait').modal('hide');
					new PNotify({
						title: 'Error',
						text: jqXHR.responseText,
						type: 'error',
						styling: 'bootstrap3',
						addclass: 'stack-bottomright',
						stack: stack_bottomleft
					});
				}
			});
		}
		var rejectAccount = function(){
			var stack_bottomleft = {"dir1": "up", "dir2": "left", "firstpos1": 25, "firstpos2": 25};
			$('#myPleaseWait').modal({
				backdrop: 'static',
				keyboard: true
			});
			$.ajax({
				type: 'GET',
				url:  'rejectAccount/'+accIdpk,
				dataType: 'json',
				async: true,
				success: function(result) {
					$('#myPleaseWait').modal('hide');
					new PNotify({
						title: 'Success',
						text: 'Intermediary Rejected Successfully',
						type: 'success',
						styling: 'bootstrap3',
						addclass: 'stack-bottomright',
						stack: stack_bottomleft
					});
					AccountDef.getAccountDetails();
				},
				error: function(jqXHR, textStatus, errorThrown) {
					$('#myPleaseWait').modal('hide');
					new PNotify({
						title: 'Error',
						text: jqXHR.responseText,
						type: 'error',
						styling: 'bootstrap3',
						addclass: 'stack-bottomright',
						stack: stack_bottomleft
					});
				}
			});
		}

		var deactivateAccount = function(){
			var stack_bottomleft = {"dir1": "up", "dir2": "left", "firstpos1": 25, "firstpos2": 25};
			$('#myPleaseWait').modal({
				backdrop: 'static',
				keyboard: true
			});
			$.ajax({
				type: 'GET',
				url:  'deactivateAccount/'+accIdpk,
				dataType: 'json',
				async: true,
				success: function(result) {
					$('#myPleaseWait').modal('hide');
					new PNotify({
						title: 'Success',
						text: 'Intermediary Activated/Deactivated Successfully',
						type: 'success',
						styling: 'bootstrap3',
						addclass: 'stack-bottomright',
						stack: stack_bottomleft
					});
					AccountDef.getAccountDetails();
				},
				error: function(jqXHR, textStatus, errorThrown) {
					$('#myPleaseWait').modal('hide');
					new PNotify({
						title: 'Error',
						text: jqXHR.responseText,
						type: 'error',
						styling: 'bootstrap3',
						addclass: 'stack-bottomright',
						stack: stack_bottomleft
					});
				}
			});
		}


		var saveAcctDocsList = function(){
			var arr = [];
			$("#saveAcctDocsBtn").click(function(){
				$("#acctDocsTbl tbody").find('input[name="record"]').each(function(){
					if($(this).is(":checked")){
						arr.push($(this).attr("id"));
					}
				});
				if(arr.length==0){
					bootbox.alert("No Documents Selected to attach..");
					return;
				}

				var $currForm = $('#acct-doc-form');
				var currValidator = $currForm.validate();
				if (!$currForm.valid()) {
					return;
				}

				var data = {};
				$currForm.serializeArray().map(function(x) {
					data[x.name] = x.value;
				});
				var url = "createAcctDocs";
				data.requiredDocs = arr;


				$.ajax({
					url : url,
					type : "POST",
					data : JSON.stringify(data),
					success : function(s) {
						new PNotify({
							title: 'Success',
							text: 'Records Created Successfully',
							type: 'success',
							styling: 'bootstrap3'
						});
						$('#accDocsList').DataTable().ajax.reload();
						$('#acctReqDocsModal').modal('hide');
						arr = [];
					},
					error : function(jqXHR, textStatus, errorThrown) {
						console.log(jqXHR);
						new PNotify({
							title: 'Error',
							text: jqXHR.responseText,
							type: 'error',
							styling: 'bootstrap3'
						});
					},
					dataType : "json",
					contentType : "application/json"
				});
			})
		};


		var init = function(){
			$("#sel2").val("D");
			$("#sel2").prop("disabled", true);
			$(".datepicker-input").each(function() {
				$(this).datetimepicker({
					format: 'DD/MM/YYYY'
				});

			});
			$("#h4pol").hide();

			$("#btn-deactivate").click(function(){
				deactivateAccount();
			});
			$("#btn-approve").click(function(){
				approveAccount();
			});
			$("#btn-reject").click(function(){
				rejectAccount();
			});

			createBranchSelect();
			createAccountTypeSelect();
			populatePaymentModes();
			getAccountDetails();
			createBanksForSel();
			validateDetails();
			saveAcctDocsList();
			createAccounts();
			uploadAcctDocument();
			populateParentControlAcc();
			createAccount();
			$("#btn-add-docs").click(function(){
				searchReqDocs("");
			});
			$("#searchDocuments").click(function(){
				searchReqDocs($("#doc-name-search").val());
			});
			var genShtDesc = UTILITIES.getParamValue("AUTO_ACCOUNT_ID_GEN");
			if(genShtDesc && genShtDesc==="Y"){
				$("#other-names").prop("readonly", true);
			}
			else{
				$("#other-names").removeAttr('readonly');
			}

			if($("#acc-type-desc").val()==='SUB'){
				$("#parent-account-lov").select2("readonly", false);
			}
			else{
				$("#parent-account-lov").select2("val", "").select2("readonly", true);
			}
		};

		return {
			init: init,
			editAcctDocs:editAcctDocs,
			downloadAcctDoc:downloadAcctDoc,
			deleteAcctDoc:deleteAcctDoc,
			getAccountDetails: getAccountDetails
		};

	})(jQuery);
</script>