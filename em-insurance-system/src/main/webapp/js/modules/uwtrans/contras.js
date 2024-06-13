$(function(){

	$(document).ready(function() {

		$.ajaxSetup({
			cache: false
		});

		checkExistingTrans();
		loadContraModal();
		confirmSelectedTrans();
		selectEndorseType();

		if($("#rev-type").val()==="RE"){

			$("#endorse-details").show();
		}
		else{
			$("#endorse-details").hide();
		}
	});
});


function selectEndorseType(){
	$("#rev-type").on('change', function(){
		if($("#rev-type").val()==="RE"){

			$("#endorse-details").show();
		}
		else{
			$("#endorse-details").hide();
		}
	})
}


function confirmSelectedTrans(){
	$("#selectauthtrans").on('click', function(){
		if ($("#pol-number").val() != ''){
			$('#contraPoliciesModal').modal('hide');
		}
		else{
			bootbox.alert('Select a Policy to continue');
		}
	});
}

function loadContraModal(){
	$("#btn-show-search-pol").on('click', function(){
		if($("#rev-type").val() != ''){
			createContraPolicies($("#rev-type").val());
			$('#contraPoliciesModal').modal({
				backdrop: 'static',
				keyboard: true
			})
		}
		else{
			bootbox.alert('Select Transaction Type');
		}

	});

	$("#btn-search-policies").on('click', function(){
		createContraPolicies($("#rev-type").val());

	});


}

function createContraPolicies(transtype){
	var url = "contraPolicies";
	if(transtype ==="CO"){
		url = "contraPolicies";
	}
	else if(transtype ==="RE"){
		url = "reuseofcontraPolicies";
	}
	var currTable = $('#revtranstbl').DataTable( {
		"processing": true,
		"serverSide": true,
		"ajax": {
			'url': url,
			'data':{
				'clientName': $("#rev-search-name").val(),
				'policyNo':  $("#pol-search-number").val(),
				'agent':  $("#agent-search-number").val(),
				'endorseNumber':  $("#rev-search-number").val(),
				'refno': $("#dr-search-number").val(),
			},
		},
		autoWidth: true,
		lengthMenu: [ [10], [10] ],
		pageLength: 10,
		destroy: true,
		searching: false,
		"columns": [
			{ "data": "policyNo" },
			{ "data": "clientPolNo" },
			{ "data": "polRevNo" },
			{ "data": "clientName",
				"render": function ( data, type, full, meta ) {
					return full.clientName;
				}
			},
			{ "data": "agentName",
				"render": function ( data, type, full, meta ) {
					return full.agentName;
				}
			},
			{ "data": "polUwYr" },
			{ "data": "binderName",
				"render": function ( data, type, full, meta ) {
					return full.binderName;
				}
			},

		]
	} );

	$('#revtranstbl tbody').on( 'click', 'tr', function () {

		$(this).addClass('active').siblings().removeClass('active');

		var d = currTable.row( this ).data();
		if(d){
			$("#rev-pol-id").val(d.policyId);
			$("#pol-number").val(d.policyNo);
			$("#pol-endorse-no").val(d.polRevNo);
			$.ajax({
				type: 'GET',
				url:  'countUnauthPolicies',
				dataType: 'json',
				data: {"policyNumber": $("#pol-number").val()},
				async: true,
				success: function(result) {
					if(result > 0){
						createUnauthPolicies();
						$("#existing-trans").show();
					}
					else{
						$("#existing-trans").hide();
					}
				},
				error: function(jqXHR, textStatus, errorThrown) {

				}
			});


		}


	} );

	return currTable;
}


function checkExistingTrans(){
	$.ajax({
		type: 'GET',
		url:  'countUnauthPolicies',
		dataType: 'json',
		data: {"policyNumber": $("#pol-number").val()},
		async: true,
		success: function(result) {

			if(result > 0){
				createUnauthPolicies();
				$("#existing-trans").show();
			}
			else{
				$("#existing-trans").hide();
			}
		},
		error: function(jqXHR, textStatus, errorThrown) {

		}
	});
}


function createUnauthPolicies(){
	var url = "unauthPolicies";
	var currTable = $('#poltrans').DataTable( {
		"processing": true,
		"serverSide": true,
		"ajax": {
			'url': url,
			'data':{
				'policyNumber': $("#pol-number").val(),
			},
		},
		autoWidth: true,
		lengthMenu: [ [10], [10] ],
		pageLength: 10,
		destroy: true,
		searching: false,
		"columns": [
			{ "data": "polNo" },
			{ "data": "polCreateddt",
				"render": function ( data, type, full, meta ) {
					return moment(full.polCreateddt).format('DD/MM/YYYY');
				}
			},
			{ "data": "wefDate",
				"render": function ( data, type, full, meta ) {
					return moment(full.wefDate).format('DD/MM/YYYY');
				}
			},
			{ "data": "wetDate",
				"render": function ( data, type, full, meta ) {
					return moment(full.wetDate).format('DD/MM/YYYY');
				}
			},
			{ "data": "policyId",
				"render": function ( data, type, full, meta ) {
					return full.createdUser.username;
				}
			},
			{ "data": "currentStatus",
				"render": function ( data, type, full, meta ) {
					if(full.currentStatus ==='D') return "Draft";
					else  return full.currentStatus;
				}
			},
			{
				"data": "policyId",
				"render": function ( data, type, full, meta ) {
					if(full.status==="A"){
						return '<form action="edituwtrans" method="post"><input type="hidden" name="id" value='+full.policyId+'><input type="submit"  class="btn btn-success" value="View" ></form>';

					}else
						return '<form action="edituwtrans" method="post"><input type="hidden" name="id" value='+full.policyId+'><input type="submit"  class="btn btn-success" value="Edit" ></form>';

				}

			},
			{
				"data": "policyId",
				"render": function ( data, type, full, meta ) {
					return '<input type="button" class="btn btn-success" data-policy='+encodeURI(JSON.stringify(full)) + ' value="Delete" onclick="confirmPolicyDelete(this);"/>';
				}

			},


		]
	} );
	return currTable;
}


function confirmPolicyDelete(button){
	var policy = JSON.parse(decodeURI($(button).data("policy")));
	bootbox.confirm("Are you sure want to delete "+policy["polNo"]+"?", function(result) {
		if(result){
			$('#myPleaseWait').modal({
				backdrop: 'static',
				keyboard: true
			});
			$.ajax({
				type: 'GET',
				url:  'deletePolRecord',
				data: {"policyId": policy["policyId"]},
				dataType: 'json',
				async: true,
				success: function(result) {
					$('#myPleaseWait').modal('hide');
					new PNotify({
						title: 'Success',
						text: 'Transaction Deleted Successfully',
						type: 'success',
						styling: 'bootstrap3'
					});
					$('#poltrans').DataTable().ajax.reload();
					$("#existing-trans").hide();
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
}