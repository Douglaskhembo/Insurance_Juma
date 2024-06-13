var RECEIPTS = RECEIPTS || {};
var UTILITIES = UTILITIES || {};
$(function(){
	$(document).ready(function() {

		$(".datepicker-input").each(function() {
			$(this).datetimepicker({
				format: 'DD/MM/YYYY'
			});
		});
		$("#rec-type").val('');
		RECEIPTS.collectionAcctsLov();
		RECEIPTS.populateUserBranches();
		$("#rct-amount").number(true, 2);
		$("#btn-add-receipt").on('click', function() {
			RECEIPTS.createandPrintReceipt();

		});

		$("#btn-print-receipt").on('click', function() {
			RECEIPTS.createReceipt();
		});

		$("#add-det-btn").on('click', function () {
			if($("#rec-type").val()){
				RECEIPTS.addReceiptRecord();
			}
			else{
				new PNotify({
					title: 'Error',
					text: "Select the Receipt Type to Proceed!!",
					type: 'error',
					styling: 'bootstrap3'
				});
				$("#rec-type").val('');
				$("#rct-detail-tbl").clear().draw();
				// console.log("Select A receipt Type to Proceed!!"+$("#receipt-type").val());
			}
		});

		$("#rct-detail-tbl").on('click','.hyperlink-btn',function() {
			$(this).closest('tr').remove();
			var row_index = $('#rct-detail-tbl tr').length;
			if(row_index ===1){
				$("#rec-type").attr('disabled',false);
			}
		});

		$("#printReceipt").on('click', function () {
			RECEIPTS.doAllocation($("#print-receipt-id").val());
		});

		function agentLov(){
			if($("#insurance-div").filter("div").html() != undefined)
			{
				Select2Builder.initAjaxSelect2({
					containerId : "insurance-div",
					sort : 'name',
					change: function(e, a, v){
						$("#insurance-id").val(e.added.acctId);
						$("#rct-detail-tbl > tbody").empty();
					},
					formatResult : function(a)
					{
						return a.name
					},
					formatSelection : function(a)
					{
						return a.name
					},
					initSelection: function (element, callback) {

					},
					id: "acctId",
					placeholder:"Select Account",
				});
			}
		}


		$("#rec-type").change(function(){
			var _val = $(this).val();
			$("#receipt-type").val(_val);
			if(_val==='C'){
				$(".insurance-co").css('display','block');
				$("#add-det-btn").hide();
				$("#rct-detail-tbl").hide();
				agentLov();
			}
			else {
				$(".insurance-co").css('display', 'none');
				$("#add-det-btn").show();
				$("#rct-detail-tbl").show();
			}
		});

		$(document).ajaxStart(function () {
			$("#btn-add-receipt,#btn-print-receipt,#printReceipt").attr("disabled", true);
		});
		$(document).ajaxComplete(function () {
			$("#btn-add-receipt,#btn-print-receipt,#printReceipt").attr("disabled", false);
		});
	})
});

RECEIPTS.doAllocation = function(receiptId){
	$('#myPleaseWait').modal({
		backdrop: 'static',
		keyboard: true
	});
	$.ajax({
		type: 'GET',
		url:  'allocateReceipt',
		dataType: 'json',
		data: {"receiptCode": receiptId},
		async: true,
		success: function(result) {
			$('#myPleaseWait').modal('hide');
			$('#printReceiptModal').modal('hide');
			new PNotify({
				title: 'Success',
				text: 'Receipt created Successfully',
				type: 'success',
				styling: 'bootstrap3'
			});

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

RECEIPTS.createAllocation = function() {
	var arr = [];
	$('#rct-detail-tbl > tbody  > tr').each(
		function() {
			var data = {};
			$(this).find(":input[type='text'],:input[type='hidden']").serializeArray()
				.map(function(x) {
					data[x.name] = x.value;
				});
			arr.push(data);
		});
	return arr;
}

RECEIPTS.addReceiptRecord = function(){
	// Use a selector that will select all the rows and take the length.
	// Note: this approach also counts all trs of every nested table!
	var row_index = $('#rct-detail-tbl tr').length;
	if(row_index > 0){
		$("#rec-type").attr('disabled',true);
	}

	var transType = $("#rec-type").val();
	if(transType ==='N') {
		var trans = '';
		if ($('#chk-fund-receipt').is(':checked')) {
			trans = '<input type="hidden" id="fund-debit-code' + row_index + '" name="transNo">' +
				'  <div id="fund-debit-frm' + row_index + '" class="form-control"  select2-url="' + SERVLET_CONTEXT + '/protected/uw/receipts/selfundtrans" </div> ';
			var markup = "<tr><td>" + trans + "</td><td><p class='form-control-static' id='rec-policy-" + row_index + "'> </p></td>" +
				"<td><p class='form-control-static' id='trans-date-" + row_index + "'> </p></td>" +
				"<td><p class='form-control-static' id='trans-client-" + row_index + "'> </p></td>" +
				"<td><p class='form-control-static' id='trans-balance-" + row_index + "'> </p></td>" +
				"<td><input type='text' size='11' id='rctamt-" + row_index + "'  name='rctAmount'/></td><td><button type='button' class='btn btn-danger btn btn-danger btn-xs hyperlink-btn'><i class='fa fa-trash-o'></button></td></tr>";
			$("#rct-detail-tbl").append(markup);
			$('[id^=rctamt-]').number(true, 2);
			Select2Builder.initAjaxSelect2({
				containerId: "fund-debit-frm" + row_index,
				sort: 'sfpId',
				counter: row_index,
				change: function (e, a, v) {
					$.each($(this), function () {
						var key = Object.keys(this)[2];
						var value = this[key];
						var drId = "#fund-debit-code" + value;
						console.log(drId);
						$(drId).val(e.added.sfpId);
						$("#rec-policy-" + value).text(e.added.policyTrans.polNo);
						$("#trans-date-" + value).text(moment(e.added.policyTrans.authDate).format('DD/MM/YYYY'));
						$("#trans-client-" + value).text(e.added.policyTrans.client.fname + " " + e.added.policyTrans.client.otherNames);
						$("#trans-balance-" + value).text(UTILITIES.currencyFormat(e.added.fundDepositAmount));
						$("#rctamt-" + value).val(UTILITIES.currencyFormat(e.added.fundDepositAmount));
					});
				},
				formatResult: function (a) {
					return a.policyTrans.refNo;
				},
				formatSelection: function (a) {
					return a.policyTrans.refNo;
				},
				initSelection: function (element, callback) {

				},
				id: "sfpId",
				width: "300px",
				placeholder: "Select Transaction"

			});
		} else {
			trans = '<input type="hidden" id="cr-debit-code' + row_index + '" name="transNo">' +
				'<input type="hidden" id="cr-temp-code' + row_index + '" name="transTempNo">' +
				' <div id="debit-frm' + row_index + '" class="form-control"  select2-url="' + SERVLET_CONTEXT + '/protected/uw/receipts/selclienttrans" </div> ';
			var markup = "<tr><td>" + trans + "</td><td><p class='form-control-static' id='rec-policy-" + row_index + "'> </p></td>" +
				"<td><p class='form-control-static' id='trans-date-" + row_index + "'> </p></td>" +
				"<td><p class='form-control-static' id='trans-client-" + row_index + "'> </p></td>" +
				"<td><p class='form-control-static' id='trans-balance-" + row_index + "'> </p></td>" +
				"<td><input type='text' size='11' id='rctamt-" + row_index + "'  name='rctAmount'/></td><td><button type='button' class='btn btn-danger btn btn-danger btn-xs hyperlink-btn'><i class='fa fa-trash-o'></button></td></tr>";
			$("#rct-detail-tbl").append(markup);
			$('[id^=rctamt-]').number(true, 2);
			Select2Builder.initAjaxSelect2({
				containerId: "debit-frm" + row_index,
				sort: 'refNo',
				counter: row_index,
				change: function (e, a, v) {
					$.each($(this), function () {
						var transId = (e.added.transno)&& e.added.transno!==0?e.added.transno:null;
						var tempId =  (e.added.transNoTemp)?e.added.transNoTemp:null;
						var key = Object.keys(this)[2];
						var value = this[key];
						var drId = "#cr-debit-code" + value;
						var drIdTemp = "#cr-temp-code" + value;
						$(drId).val(transId);
						$(drIdTemp).val(tempId);
						$("#rec-policy-" + value).text(e.added.polNo);
						$("#trans-date-" + value).text(moment(e.added.transDate).format('DD/MM/YYYY'));
						$("#trans-client-" + value).text(e.added.client);
						$("#trans-balance-" + value).text(UTILITIES.currencyFormat(e.added.balance));
						$("#rctamt-" + value).val(UTILITIES.currencyFormat(e.added.balance));
						console.log("transNo is: "+$(drId).val());
						console.log("transTempNo is: "+$(drIdTemp).val());
					});
				},
				formatResult: function (a) {
					return a.refNo+' - '+a.controlAcc+' - '+a.client;
				},
				formatSelection: function (a) {
					return a.refNo+' - '+a.controlAcc+' - '+a.client;
				},
				initSelection: function (element, callback) {

				},
				id: "transno",
				width: "300px",
				placeholder: "Select Transaction"

			});
		}
	}
	else if(transType ==='C') {
		trans = '<input type="hidden" id="cr-debit-code' + row_index + '" name="transNo">' +
			'  <div id="debit-frm' + row_index + '" class="form-control"  select2-url="' + SERVLET_CONTEXT + '/protected/uw/receipts/selcommtrans" </div> ';
		var markup = "<tr><td>" + trans + "</td><td><p class='form-control-static' id='rec-policy-" + row_index + "'> </p></td>" +
			"<td><p class='form-control-static' id='trans-date-" + row_index + "'> </p></td>" +
			"<td><p class='form-control-static' id='trans-client-" + row_index + "'> </p></td>" +
			"<td><p class='form-control-static' id='trans-balance-" + row_index + "'> </p></td>" +
			"<td><input type='text' size='11' id='rctamt-" + row_index + "'  name='rctAmount'/></td><td><button type='button' class='btn btn-danger btn btn-danger btn-xs hyperlink-btn'><i class='fa fa-trash-o'></button></td></tr>";
		$("#rct-detail-tbl").append(markup);
		$('[id^=rctamt-]').number(true, 2);
		Select2Builder.initAjaxSelect2({
			containerId: "debit-frm" + row_index,
			sort: 'refNo',
			counter: row_index,
			change: function (e, a, v) {
				$.each($(this), function () {
					var key = Object.keys(this)[2];
					var value = this[key];
					var drId = "#cr-debit-code" + value;
					$(drId).val(e.added.transno);
					$("#rec-policy-" + value).text(e.added.policy.polNo);
					$("#trans-date-" + value).text(moment(e.added.transDate).format('DD/MM/YYYY'));
					$("#trans-client-" + value).text(e.added.policy.client.fname + " " + e.added.policy.client.otherNames);
					$("#trans-balance-" + value).text(UTILITIES.currencyFormat(e.added.balance));
					$("#rctamt-" + value).val(UTILITIES.currencyFormat(e.added.balance));
				});
			},
			formatResult: function (a) {
				return a.refNo;
			},
			formatSelection: function (a) {
				return a.refNo;
			},
			initSelection: function (element, callback) {

			},
			id: "transno",
			width: "300px",
			placeholder: "Select Transaction",
			params: {acctId: $("#insurance-id").val()},

		});
	}
	else{
		trans = '<input type="hidden" id="cr-debit-code' + row_index + '" name="transNo">' +
			'  <div id="debit-frm' + row_index + '" class="form-control"  select2-url="' + SERVLET_CONTEXT + '/protected/uw/receipts/lifepolicies" </div> ';
		var markup = "<tr><td>" + trans + "</td><td><p class='form-control-static' id='rec-policy-" + row_index + "'> </p></td>" +
			"<td><p class='form-control-static' id='trans-date-" + row_index + "'> </p></td>" +
			"<td><p class='form-control-static' id='trans-client-" + row_index + "'> </p></td>" +
			"<td><p class='form-control-static' id='trans-balance-" + row_index + "'> </p></td>" +
			"<td><input type='text' size='11' id='rctamt-" + row_index + "'  name='rctAmount'/></td><td><button type='button' class='btn btn-danger btn btn-danger btn-xs hyperlink-btn'><i class='fa fa-trash-o'></button></td></tr>";
		$("#rct-detail-tbl").append(markup);
		$('[id^=rctamt-]').number(true, 2);
		Select2Builder.initAjaxSelect2({
			containerId: "debit-frm" + row_index,
			sort: 'refNo',
			counter: row_index,
			change: function (e, a, v) {
				$.each($(this), function () {
					var key = Object.keys(this)[2];
					var value = this[key];
					var drId = "#cr-debit-code" + value;
					$(drId).val(e.added.policyId);
					console.log(e.added);
					//$("#rec-policy-" + value).text(e.added.polNo);
					if (e.added.polNo){
						$("#rec-policy-" + value).text(e.added.polNo);
					} else {
						$("#rec-policy-" + value).text(e.added.proposalNo);
					}
					if (e.added.authDate) {
						$("#trans-date-" + value).text(moment(e.added.authDate).format('DD/MM/YYYY'));
					} else{
						$("#trans-date-" + value).text(moment(e.added.polCreateddt).format('DD/MM/YYYY'));
					}
					//$("#trans-date-" + value).text(moment(e.added.authDate).format('DD/MM/YYYY'));
					$("#trans-client-" + value).text(e.added.client.fname + " " + e.added.client.otherNames);
					$("#trans-balance-" + value).text(UTILITIES.currencyFormat(e.added.netPrem));
					$("#rctamt-" + value).val(UTILITIES.currencyFormat(e.added.netPrem));
				});
			},
			formatResult: function (a) {
				if (a.polNo)
					return a.polNo;
				else return a.proposalNo;
			},
			formatSelection: function (a) {
				if (a.polNo)
					return a.polNo;
				else return a.proposalNo;
			},
			initSelection: function (element, callback) {

			},
			id: "policyId",
			width: "300px",
			placeholder: "Select Transaction"

		});
	}

}



RECEIPTS.printPdf = function(url){
	$("#receipt-div").attr('src', url);
	$('#printReceiptModal').modal({
		backdrop: 'static',
		keyboard: true
	});
}

RECEIPTS.createReceipt = function() {
	var $currForm = $('#receipt-form');
	var currValidator = $currForm.validate();
	if (!$currForm.valid()) {
		return;
	}

	var data = {};
	$currForm.serializeArray().map(function(x) {
		data[x.name] = x.value;
	});
	var url = "createReceipt";
	var arr = RECEIPTS.createAllocation();
	data.details = arr;
	$.ajax({
		url : url,
		type : "POST",
		data : JSON.stringify(data),
		success : function(s) {
			$('#myPleaseWait').modal('hide');
			$('#receipt-form').find("input[type=text],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden],input[type=number], textarea").val("");
			arr = {};
			$('#rct-detail-tbl tbody').remove();
			RECEIPTS.collectionAcctsLov();
			RECEIPTS.populateUserBranches();
			$('#myPleaseWait').modal({
				backdrop: 'static',
				keyboard: true
			});
			$.ajax({
				type: 'GET',
				url:  'allocateReceipt',
				dataType: 'json',
				data: {"receiptCode": s},
				async: true,
				success: function(result) {
					$('#myPleaseWait').modal('hide');
					new PNotify({
						title: 'Success',
						text: 'Receipt created Successfully',
						type: 'success',
						styling: 'bootstrap3'
					});
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

		},
		error : function(jqXHR, textStatus, errorThrown) {
			//$('#myPleaseWait').modal('hide');
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
}

RECEIPTS.createandPrintReceipt = function() {
	var $currForm = $('#receipt-form');
	var currValidator = $currForm.validate();
	if (!$currForm.valid()) {
		return;
	}

	var data = {};
	$currForm.serializeArray().map(function(x) {
		data[x.name] = x.value;
	});
	var url = "createReceipt";
	var arr = RECEIPTS.createAllocation();
	data.details = arr;
	$('#myPleaseWait').modal({
		backdrop: 'static',
		keyboard: true
	});
	$.ajax({
		url : url,
		type : "POST",
		data : JSON.stringify(data),
		success : function(s) {
			$('#myPleaseWait').modal('hide');
			$('#receipt-form').find("input[type=text],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden],input[type=number], textarea").val("");
			arr = {};
			$('#rct-detail-tbl tbody').remove();
			RECEIPTS.collectionAcctsLov();
			RECEIPTS.populateUserBranches();
			$("#print-receipt-id").val(s);
			RECEIPTS.printPdf(SERVLET_CONTEXT+"/protected/uw/receipts/receipt_rpt/"+s);
		},
		error : function(jqXHR, textStatus, errorThrown) {
			$('#myPleaseWait').modal('hide');
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
}


RECEIPTS.collectionAcctsLov = function() {
	if ($("#coll-div").filter("div").html() != undefined) {
		Select2Builder.initAjaxSelect2({
			containerId : "coll-div",
			sort : 'name',
			change : function(e, a, v) {
				$("#coll-id").val(e.added.caId);
				if(e.added.bankBranches)
					$("#bank-desc").text(e.added.bankBranches.branchName+" - "+e.added.bankBranches.bank.bankName);
				else $("#bank-desc").text("No Bank Account");
				$("#currency-desc").text(e.added.currencies.curName);
				$("#pymt-mode").text(e.added.paymentModes.pmDesc);
			},
			formatResult : function(a) {
				return a.name
			},
			formatSelection : function(a) {
				return a.name
			},
			initSelection : function(element, callback) {

			},
			id : "caId",
			placeholder:"Select Collection Account",
			width : "220px"
		});
	}
}

RECEIPTS.populateUserBranches = function(){
	if($("#brn-frm").filter("div").html() != undefined)
	{
		Select2Builder.initAjaxSelect2({
			containerId : "brn-frm",
			sort : 'obName',
			change: function(e, a, v){
				$("#brn-id").val(e.added.obId);
			},
			formatResult : function(a)
			{
				return a.obName;
			},
			formatSelection : function(a)
			{
				return a.obName;
			},
			initSelection: function (element, callback) {

			},
			id: "obId",
			width:"250px",
			placeholder:"Select Branch"

		});
	}
}