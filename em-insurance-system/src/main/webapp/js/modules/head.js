var UTILITIES = UTILITIES || {};

UTILITIES.currencyFormat  =function(num) {
	if(num)
    return  num.toFixed(2).replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,");
    else
    	return 0;
};

UTILITIES.roundNumber = function(num, scale) {
	if(!("" + num).includes("e")) {
		return +(Math.round(num + "e+" + scale)  + "e-" + scale);
	} else {
		var arr = ("" + num).split("e");
		var sig = "";
		if(+arr[1] + scale > 0) {
			sig = "+";
		}
		return +(Math.round(+arr[0] + "e" + sig + (+arr[1] + scale)) + "e-" + scale);
	}
};

UTILITIES.getProcessActiveDiagram = function(docId){
	var rand =Math.floor((Math.random() * 100000000) + 1);
	var url = SERVLET_CONTEXT + '/protected/workflow/documents/'+docId;
	url += ('?rand=' + rand);
	$('#proc-main-diagram').attr('src', url);
	$(".proc-main-diagram").attr('src', url);
}

UTILITIES.getTaskActive = function(docId){
	var url = SERVLET_CONTEXT + '/protected/workflow/taskName/'+docId;
	$.ajax({
		type: 'GET',
		url:  url,
		dataType: 'json',
		async: true,
		success: function(result) {
			//console.log(result);
			if(result){
				$("#wkflow-task-name").text("Task: "+result);
			}
			else{
				$("#wkflow-task-name").text("Transaction Details");
			}
		},
		error: function(jqXHR, textStatus, errorThrown) {

		}
	});
}


UTILITIES.getRevDesc = function(code) {
	var ret;
	switch(code){
	case "UP":
		ret = "Underwriting Premium";
		break;
	case "UC":
		ret = "Underwriting Commission";
		break;
	case "AF":
		ret = "Admin Fees";
		break;
	case "CP":
		ret = "Creditor Payment";
		break;
	case "SD":
		ret = "Stamp Duty";
		break;
	case "PHCF":
		ret = "PHF Fund";
		break;
	case "CPL":
		ret = "Claims Payment Pool";
		break;
	case "TL":
		ret = "Traninig Levy";
		break;
	case "WI":
		ret = "Write Ins";
		break;
	case "WO":
		ret = "Write Offs";
		break;
	case "NR":
		ret = "Normal Receipts";
		break;
	case "DR":
		ret = "Direct Receipts";
		break;
	case "DC":
		ret = "Document Charge";
		break;
	case "LTA":
		ret = "Long Term Agreements";
		break;
	case "SAC":
		ret = "Sub Agent Commission";
		break;
	case "EX":
		ret = "Extras";
		break;
	case "WHTX":
		ret = "Withholding Tax";
		break;
	case "CF":
		ret = "Issue Card Fee";
		break;
	case "SC":
		ret = "Card Service Charge";
		break;
	case "RE":
		ret = "Re-issue Card Fee";
		break;
	case "VAT":
		ret = "Value Added Tax";
		break;
	default:
		ret = "Underwriting Premium";
		break;
	}
	return ret;
}

UTILITIES.createAssignee = function(){
	$("#btn-assign-trans").click(function(){
		UTILITIES.createAssigneeLov();
		$('#assign-user-form').find("input[type=text],input[type=number],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden], textarea,select").val("");
		$('#assignModal').modal({
			backdrop: 'static',
			keyboard: true
		})
	});
	var $classForm = $('#assign-user-form');
	var validator = $classForm.validate();
	$('#saveTicket').click(function(){
		if (!$classForm.valid()) {
			return;
		}
		$('#myPleaseWait').modal({
			backdrop: 'static',
			keyboard: true
		});
		var $btn = $(this).button('Saving');
		var data = {};
		$classForm.serializeArray().map(function(x){data[x.name] = x.value;});
		var url = SERVLET_CONTEXT+"/protected/workflow/assigneeTicket";
		var request = $.post(url, data );
		request.success(function(){
			$('#myPleaseWait').modal('hide');
			new PNotify({
				title: 'Success',
				text: 'Record created/updated Successfully',
				type: 'success',
				styling: 'bootstrap3'
			});
			window.location.href = SERVLET_CONTEXT+"/protected/home";
		});

		request.error(function(jqXHR, textStatus, errorThrown){
			$('#myPleaseWait').modal('hide');
			new PNotify({
				title: 'Error',
				text: jqXHR.responseText,
				type: 'error',
				styling: 'bootstrap3'
			});
		});
		request.always(function(){
			$btn.button('reset');
		});
	});
}

UTILITIES.downloadPolicyDoc = function (){
	window.open(SERVLET_CONTEXT+"/protected/uw/policies/policydocument",
		'_blank'
	);
}


UTILITIES.sendEmail = function(){
	$.ajax({
		type: 'GET',
		url:   'mailtemplate',
		dataType: 'json',
		async: true,
		success: function(result) {
			$('#email-form').find("input[type=text],input[type=number],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden], textarea,select").val("");
			$("#email-template").val(result);
			$('#email-template').trumbowyg({
				svgPath : SERVLET_CONTEXT+"/libs/css/icons.svg",
				autogrow: false
			});
			$('#emailModal').modal({
				backdrop: 'static',
				keyboard: true
			});

		},
		error: function(jqXHR, textStatus, errorThrown) {

		}
	});
}


UTILITIES.emailReports = function(){
	$(document).ajaxStart(function () {
		$("#sendEmailForm").attr("disabled", true);
	});
	$(document).ajaxComplete(function () {
		$("#sendEmailForm").attr("disabled", false);
	});
	$('#sendEmailForm').click(function(){
		var $classForm = $('#email-form');
		var validator = $classForm.validate();
		if (!$classForm.valid()) {
			return;
		}
		$('#myPleaseWait').modal({
			backdrop: 'static',
			keyboard: true
		});
		var reports = $("#email-form input:checkbox:checked").map(function(){
			return $(this).val();
		}).get();

		var $btn = $(this).button('Saving');
		var data = {};
		$classForm.serializeArray().map(function(x){data[x.name] = x.value;});
		data.reports = reports;
		var url = "sendEmail";
		$.ajax(
			{
				url:url,
				type: "POST",
				async: true,
				data: JSON.stringify(data),
				success: function(s){
					$('#myPleaseWait').modal('hide');
					new PNotify({
						title: 'Success',
						text: 'Email Send',
						type: 'success',
						styling: 'bootstrap3'
					});
					$('#email-form').find("input[type=text],input[type=number],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden], textarea,select").val("");
					$('#emailModal').modal('hide');
					items = [];
				},
				error: function(jqXHR, textStatus, errorThrown){
					$('#myPleaseWait').modal('hide');
					new PNotify({
						title: 'Error',
						text: jqXHR.responseText,
						type: 'error',
						styling: 'bootstrap3'
					});
				},
				dataType: "json",
				contentType: "application/json"
			} );
	});

}


UTILITIES.createAssigneeLov = function(){
	if($("#user-assignee-div").filter("div").html() != undefined)
	{
		Select2Builder.initAjaxSelect2({
			containerId : "user-assignee-div",
			sort : 'id',
			change: function(e, a, v){
				$("#assign-user-name").val(e.added.username);
			},
			formatResult : function(a)
			{
				return a.username;
			},
			formatSelection : function(a)
			{
				return a.username;
			},
			initSelection: function (element, callback) {

			},
			id: "id",
			width:"250px",
			placeholder:"Select Assignee"

		});
	}
}


UTILITIES.getProcessHistory = function(docId){
	if(docId){
		$.ajax({
			type: 'GET',
			url:  SERVLET_CONTEXT+'/protected/workflow/dochistory/'+docId,
			dataType: 'json',
			async: true,
			success: function(result) {
				$("#ticketHistoryTbl tbody").each(function(){
					$(this).remove();
				});
				for(var res in result){
					var markup = "<tr><td>" + result[res].id + "</td><td>" + result[res].name + "</td><td>" + result[res].assignee + "</td><td>" +moment( result[res].startTime).format('DD/MM/YYYY') + "</td><td>" + moment( result[res].endTime).format('DD/MM/YYYY')  + "</td></tr>";
					$("#ticketHistoryTbl").append(markup);
				}
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

		$("#btn-show-task-history").click(function(){
			$('#taskHistModal').modal({
				backdrop: 'static',
				keyboard: true
			})
		});
	}
}

UTILITIES.getParamValue = function(paramName){
	var res = "N";
	$.ajax({
		type: 'GET',
		url: SERVLET_CONTEXT+'/protected/setups/params',
		dataType: 'json',
		data: {"paramName": paramName},
		async: false,
		success: function (result) {
			res = result;
		},
		error: function (jqXHR, textStatus, errorThrown) {
		//	console.log(jqXHR);
		}
	});
	return res;
}

UTILITIES.extendsOpts = function(opts) {
	$.extend(true, opts,
		{
			"processing": true,
			"serverSide": true,
			autoWidth: true,
			"ajax": {
				'url': opts.ajaxUrl,
			},
			lengthMenu: [ opts.arrLength||[10], opts.arrLength||[10]],
			pageLength: 10,
			destroy: true
		}
	);
	return opts;
}


$(document).ready(function(){

	$.ajaxSetup({ cache: false });
	$.fn.dataTableExt.sErrMode = "console";
	$.fn.dataTableExt.oApi._fnLog = function (oSettings, iLevel, sMesg, tn) {
		var sAlert = (oSettings === null)
				? "DataTables warning: "+sMesg
				: "DataTables warning (table id = '"+oSettings.sTableId+"'): "+sMesg
			;

		if (tn) {
			sAlert += ". For more information about this error, please see "+
				"http://datatables.net/tn/"+tn
			;
		}

		if (iLevel === 0) {
			if ($.fn.dataTableExt.sErrMode == "alert") {
				alert(sAlert);
			} else if ($.fn.dataTableExt.sErrMode == "thow") {
				throw sAlert;
			} else  if ($.fn.dataTableExt.sErrMode == "console") {
				console.log(sAlert);
			} else  if ($.fn.dataTableExt.sErrMode == "mute") {}


		} else if (console !== undefined && console.log) {

		}
	}
});


$(document).ajaxError( function(event, request, settings, exception) {
	console.log(event);
	if(String.prototype.indexOf.call(request.responseText, "j_username") != -1) {
		window.location.reload(document.URL);

	}
});