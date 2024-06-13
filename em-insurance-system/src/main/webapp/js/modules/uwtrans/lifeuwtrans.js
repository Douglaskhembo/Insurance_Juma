var UWScreen = (function($){
	'use strict';
	var selRiskCode = -2000;
	var createSectorSelect = function(){
		if($("#sect-def").filter("div").html() != undefined)
		{
			Select2Builder.initAjaxSelect2({
				containerId : "sect-def",
				sort : 'name',
				change: function(e, a, v){
					$("#sect-id").val(e.added.code);
					populateOccupations(e.added.code);
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
				id: "code",
				placeholder:"Select Sector",
			});
		}
	};

var enablepremsa= function(){

	if($("#pcompute").is(":checked")){
		console.log("radio button  test 1111")
		$("#sumassured-amt").val("");
		$(".sumassured-disp").hide();
		$(".premium-disp").show();

	}
	if($("#scompute").is(":checked")){
		console.log("radio button  test 2222")
		$("#premium-amt").val("");
		$(".premium-disp").hide();
		$(".sumassured-disp").show();
	}
	$('input[type=radio][name=computeType]').change(function() {
		if (this.value == 'P') {
			$("#sumassured-amt").val("");
			$(".sumassured-disp").hide();
			$(".premium-disp").show();
		}
		else if (this.value == 'S') {
			$("#premium-amt").val("");
			$(".premium-disp").hide();
			$(".sumassured-disp").show();
		}
	});
};
	function getPolicyTerms(binCode) {
		$('#pol-term option').remove();
		$('#pol-term').append($('<option>', {
			value: "",
			text : "Select Term"
		}));
		$.ajax({
			type: 'GET',
			url: 'binderPolTerms/'+binCode,
			dataType: 'json',
			async: true,
			success: function (result) {
				console.log(result);
				for(var res in result){
					$('#pol-term').append($('<option>', {
						value: result[res].term,
						text : result[res].termDisplay
					}));
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				new PNotify({
					title: 'Error',
					text: jqXHR.responseText,
					type: 'error',
					styling: 'bootstrap3'
				});
			}
		});
	};
	var populateOccupations = function(){
		if($("#occ-def").filter("div").html() != undefined)
		{
			Select2Builder.initAjaxSelect2({
				containerId : "occ-def",
				sort : 'name',
				change: function(e, a, v){
					$("#occ-id").val(e.added.code);
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
				id: "code",
				placeholder:"Select Occupation",
				params: {sectCode: sectCode}

			});
		}
	};

	var populateBranchLov1 = function(){
		if($("#ten-branch").filter("div").html() != undefined)
		{
			Select2Builder.initAjaxSelect2({
				containerId : "ten-branch",
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
				},
				id: "obId",
				placeholder:"Select Branch",

			});
		}
	};

	var populateClientTypeLov2 = function(){
		if($("#clnt-client-type-1").filter("div").html() != undefined)
		{
			Select2Builder.initAjaxSelect2({
				containerId : "clnt-client-type-1",
				sort : 'clientType',
				change: function(e, a, v){
					$("#clnt-type-id-1").val(e.added.typeId);
					$("#clnt-type-name-1").val(e.added.typeDesc);
				},
				formatResult : function(a)
				{
					return  a.typeDesc
				},
				formatSelection : function(a)
				{
					return  a.typeDesc
				},
				initSelection: function (element, callback) {
				},
				id: "typeId",
				placeholder:"Select Client Type",

			});
		}
		if($("#clnt-client-type").filter("div").html() != undefined)
		{
			Select2Builder.initAjaxSelect2({
				containerId : "clnt-client-type",
				sort : 'clientType',
				change: function(e, a, v){
					$("#clnt-type-id").val(e.added.typeId);
					$("#clnt-type-name").val(e.added.typeDesc);
				},
				formatResult : function(a)
				{
					return  a.typeDesc
				},
				formatSelection : function(a)
				{
					return  a.typeDesc
				},
				initSelection: function (element, callback) {
				},
				id: "typeId",
				placeholder:"Select Client Type",

			});
		}
	};

	var populateCountryLov = function(){
		if($("#clnt-country").filter("div").html() != undefined)
		{
			Select2Builder.initAjaxSelect2({
				containerId : "clnt-country",
				sort : 'couName',
				change: function(e, a, v){
					$("#cou-id").val(e.added.couCode);
					$("#cou-prefix").val(e.added.prefix);
					populateSmsPrefix(e.added.couCode);
				},
				formatResult : function(a)
				{
					return a.couName
				},
				formatSelection : function(a)
				{
					return a.couName
				},
				initSelection: function (element, callback) {
				},
				id: "couCode",
				placeholder:"Select Country",

			});
		}
	};

	var populateTitlesLov = function(){
		if($("#clnt-title").filter("div").html() != undefined)
		{
			Select2Builder.initAjaxSelect2({
				containerId : "clnt-title",
				sort : 'titleName',
				change: function(e, a, v){
					$("#clnt-title-id").val(e.added.titleId);
					$("#clnt-title-name").val(e.added.titleName);
				},
				formatResult : function(a)
				{
					return a.titleName
				},
				formatSelection : function(a)
				{
					return a.titleName
				},
				initSelection: function (element, callback) {
				},
				id: "titleId",
				placeholder:"Select Title",

			});
		}
	};

	var populateSmsPrefix = function(couCode){
		if($("#sms-pref").filter("div").html() != undefined)
		{
			Select2Builder.initAjaxSelect2({
				containerId : "sms-pref",
				sort : 'prefixName',
				change: function(e, a, v){
					console.log(e.added.prefixId);
					$("#pref-sms-id").val(e.added.prefixId);
				},
				formatResult : function(a)
				{
					return a.prefixName
				},
				formatSelection : function(a)
				{
					return a.prefixName
				},
				initSelection: function (element, callback) {
				},
				id: "prefixId",
				placeholder:"Prefix",
				params: {couCode: couCode}

			});
		}

		if($("#phone-pref").filter("div").html() != undefined)
		{
			Select2Builder.initAjaxSelect2({
				containerId : "phone-pref",
				sort : 'prefixName',
				change: function(e, a, v){
					console.log(e.added.prefixId);
					$("#pref-phone-id").val(e.added.prefixId);
				},
				formatResult : function(a)
				{
					return a.prefixName
				},
				formatSelection : function(a)
				{
					return a.prefixName
				},
				initSelection: function (element, callback) {
				},
				id: "prefixId",
				placeholder:"Prefix",
				params: {couCode: couCode}

			});
		}
	};

	var populateClientTypeLov2 = function(){
		if($("#clnt-client-type-1").filter("div").html() != undefined)
		{
			Select2Builder.initAjaxSelect2({
				containerId : "clnt-client-type-1",
				sort : 'clientType',
				change: function(e, a, v){
					$("#clnt-type-id-1").val(e.added.typeId);
					$("#clnt-type-name-1").val(e.added.typeDesc);
				},
				formatResult : function(a)
				{
					return  a.typeDesc
				},
				formatSelection : function(a)
				{
					return  a.typeDesc
				},
				initSelection: function (element, callback) {
				},
				id: "typeId",
				placeholder:"Select Client Type",

			});
		}
		if($("#clnt-client-type").filter("div").html() != undefined)
		{
			Select2Builder.initAjaxSelect2({
				containerId : "clnt-client-type",
				sort : 'clientType',
				change: function(e, a, v){
					$("#clnt-type-id").val(e.added.typeId);
					$("#clnt-type-name").val(e.added.typeDesc);
				},
				formatResult : function(a)
				{
					return  a.typeDesc
				},
				formatSelection : function(a)
				{
					return  a.typeDesc
				},
				initSelection: function (element, callback) {
				},
				id: "typeId",
				placeholder:"Select Client Type",

			});
		}
	};

	var saveClientDetails = function(){
		$('#saveClientBtn').click(function(){
			var $classForm = $('#tenant-form');
			var validator = $classForm.validate();
			if (!$classForm.valid()) {
				return;
			}
			var $btn = $(this).button('Saving');
			var data = {};
			$classForm.serializeArray().map(function(x){data[x.name] = x.value;});
			var url = "createClient";
			var request = $.post(url, data );
			request.success(function(result){
				$("#client-id").val(result.tenId);
				$("#client-f-name").val(result.fname);
				$("#client-other-name").val(result.otherNames);
				populateClientLov();
				$("#insured-name").val(result.fname);
				$("#insured-code").val(result.tenId);
				$("#insured-other-name").val(result.otherNames);
				populateInsuredLov();
				new PNotify({
					title: 'Success',
					text: 'Record created/updated Successfully',
					type: 'success',
					styling: 'bootstrap3'
				});
				validator.resetForm();
				$('#tenant-form').find("input[type=text],input[type=number],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden], textarea,select").val("");
				$('#createClientModal').modal('hide');
			});

			request.error(function(jqXHR, textStatus, errorThrown){
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
	};

	var addNewClient = function(){
		$("#btn-add-client").click(function(){
			$(".clnt-status").hide();
			$('#tenant-form').find("input[type=text],input[type=number],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden], textarea,select").val("");
			$('#createClientModal').modal({
				backdrop: 'static',
				keyboard: true
			})
		})
	}

	var populatePolicyDetails = function(){

		if(typeof polCode!== 'undefined'){
			if(polCode!==-2000){
				console.log('polCode '+polCode);
				$("#btn-uw-reports").show();
				$("#risk-form").hide();
				$("#btn-save-risk").hide();
				$("#btn-save-cancel").hide();

				$("#btn-add-risk").show();
				$("#risk-div").show();
				$("#sect-div").show();
				$("#prem-rates-div").hide();
				$("#btn-import-risk").show();
				getPolicyDetails();
				getPolicyRemakrs();

				$("#myTab #show-taxes,#show-clauses").show();
				$("#display-client").css('display','block');
				$("#pol-term").css('display','none');
				$("#pol-term-display").css('display','block');
				createPolicyClauses();
				createPolicyTaxes();
				$(".import-risks").hide();
				$(".computetype").hide();
				$(".premium-disp").show();
				$(".sumassured-disp").show();
				createPolicyChecks();
				$("#other-pol-details").show();
			}
			else{
				$("#btn-auth-policy").css("display","none");
				$("#btn-dispatch-trans").hide();
				$("#btn-import-risk").hide();
				$("#btn-uw-reports").hide();
				$("#risk-form").show();
				$("#risk-div").hide();
				$("#other-pol-details").hide();
				$("#prem-rates-div").show();
				$("#btn-add-policy").show();
				$("#pol-term").css('display','block');
				$("#pol-term-display").css('display','none');
				$("#sect-div").hide();
				$("#btn-save-risk").hide();
				$("#btn-save-cancel").hide();
				$("#btn-add-risk").hide();
				$("#myTab #show-taxes,#show-clauses").hide();
				$(".hide-details").hide();
				$("#btn-add-new-section").hide();
				$("#display-client").css('display','none');
				$("#display-binder").hide();
				$("#display-payment-mode").hide();
				$("#display-branch").hide();
				$("#display-currency").hide();
				$(".import-risks").show();
				$(".computetype").show();
				$(".premium-disp").hide();
				$(".sumassured-disp").hide();

			}
		}
	};

	var getPolicyRemakrs = function(){
		if(typeof polCode!== 'undefined'){
			if(polCode!==-2000){
				$.ajax( {
					url: 'getPolicyRemarks',
					type: 'GET',
					processData: false,
					contentType: false,
					success: function (s ) {

						$("#poli-remarks").val(s.polRemarks);
						$("#pol-remark-pk").val(s.remarksId);
						if(s.endRemarks)
							$("#remark-pk").val(s.endRemarks.remarkId);
						$("#remark-pol-id").val(polCode);
					},
					error: function(xhr, error){
						bootbox.alert(xhr.responseText);
					}
				});
			}
		}
	};

	var getPolicyDetails = function(){
		if(typeof polCode!== 'undefined'){
			if(polCode!==-2000){
				$.ajax( {
					url: 'getPolicyDetails',
					type: 'GET',
					processData: false,
					contentType: false,
					success: function (s ) {

						if(s.authStatus){
							if(s.authStatus==="D"){
								$("#btn-assign-trans").show();
								$("#btn-dispatch-trans").hide();
								$("#btn-import-risk").show();
								$("#btn-add-docs").show();
								$("#btn-add-new-ips").show();
								$("#btn-add-policy").css("display","block");
								$("#btn-auth-policy").css("display","none");
								$("#btn-undo-make-ready").css("display","none");
								$("#btn-make-ready-policy").css("display","block");
								$("#pol-status").text("Draft");
								$("#edit-currency").show();
								$("#display-currency").hide();
								$("#edit-branch").show();
								$("#display-branch").hide();
								$("#edit-payment-mode").show();
								$("#display-payment-mode").hide();
								$("#edit-binder").show();
								$("#display-binder").hide();
								$(".edit-client").show();
								$("#display-client").hide();
								$("#policy-remarks").removeAttr('disabled');
								$("#pol-bin-type").removeAttr('disabled');
								$("#pol-buss-type").removeAttr('disabled');
								$("#from-date").removeAttr('disabled');
								$("#wet-date").removeAttr('disabled');
								$("#pol-interface-type").removeAttr('disabled');
								$("#pol-frequency").removeAttr('disabled');
								$("#client-pol-no").removeAttr('disabled');
								$("#poli-remarks").removeAttr('disabled');
								$("#btn-add-risk").show();
								$("#btn-add-new-clause").show();
								$("#btn-add-new-tax").show();
								$("#btn-add-new-section").show();
								$("#btn-add-new-remark").show();
								$("#btn-save-remark").show();
								$("#btn-add-new-sched").show();
								$("#btn-add-edit-sched").show();
								$("#btn-add-del-sched").show();
								$("#sub-agent-frm").select2("enable", true);
							}
							else if(s.authStatus==="R"){
								$("#btn-assign-trans").show();
								$("#btn-import-risk").hide();
								$("#btn-add-docs").hide();
								$("#btn-add-new-ips").hide();
								$("#btn-add-policy").css("display","none");
								$("#btn-dispatch-trans").hide();
								$("#btn-auth-policy").css("display","block");
								$("#btn-undo-make-ready").css("display","block");
								$("#btn-make-ready-policy").css("display","none");
								$("#pol-status").text("Ready");
								$("#edit-currency").hide();
								$("#display-currency").show();
								$("#edit-branch").hide();
								$("#display-branch").show();
								$("#edit-payment-mode").hide();
								$("#display-payment-mode").show();
								$("#edit-binder").hide();
								$("#display-binder").show();
								$(".edit-client").hide();
								$("#display-client").show();
								$("#sub-agent-frm").select2("enable", false);
								$("#poli-remarks").attr("disabled", "disabled");
								$("#pol-buss-type").prop("disabled", true);
								$("#pol-bin-type").prop("disabled", true);
								$("#from-date").prop("disabled", true);
								$("#wet-date").prop("disabled", true);
								$("#pol-interface-type").prop("disabled", true);
								$("#pol-frequency").prop("disabled", true);
								$("#client-pol-no").prop("disabled", true);
								$("#btn-add-risk").hide();
								$("#btn-add-new-clause").hide();
								$("#btn-add-new-tax").hide();
								$("#btn-add-new-section").hide();
								$("#btn-add-new-remark").hide();
								$("#btn-save-remark").hide();
								$("#btn-add-new-sched").hide();
								$("#btn-add-edit-sched").hide();
								$("#btn-add-del-sched").hide();
							}
							else if(s.authStatus==="A"){
								$("#btn-assign-trans").show();
								$("#btn-import-risk").hide();
								$("#btn-add-docs").hide();
								$("#btn-add-new-ips").hide();
								$("#btn-dispatch-trans").show();
								$("#btn-add-policy").css("display","none");
								$("#btn-auth-policy").css("display","none");
								$("#btn-undo-make-ready").css("display","none");
								$("#btn-make-ready-policy").css("display","none");
								$("#pol-status").text("Authorised");
								$("#sub-agent-frm").select2("enable", false);
								$("#edit-currency").hide();
								$("#display-currency").show();
								$("#edit-branch").hide();
								$("#display-branch").show();
								$("#edit-payment-mode").hide();
								$("#display-payment-mode").show();
								$("#edit-binder").hide();
								$("#display-binder").show();
								$(".edit-client").hide();
								$("#display-client").show();
								$("#poli-remarks").prop("disabled", true);
								$("#policy-remarks").prop("disabled", true);
								$("#pol-buss-type").prop("disabled", true);
								$("#pol-bin-type").prop("disabled", true);
								$("#from-date").prop("disabled", true);
								$("#wet-date").prop("disabled", true);
								$("#pol-interface-type").prop("disabled", true);
								$("#pol-frequency").prop("disabled", true);
								$("#client-pol-no").prop("disabled", true);
								$("#btn-add-risk").hide();
								$("#btn-add-new-clause").hide();
								$("#btn-add-new-tax").hide();
								$("#btn-add-new-section").hide();
								$("#btn-add-new-remark").hide();
								$("#btn-save-remark").hide();
								$("#btn-add-new-sched").hide();
								$("#btn-add-edit-sched").hide();
								$("#btn-add-del-sched").hide();
							}

						}
						polCode = s.policyId;
						getUWPolicyRisks(s.policyId);
						getUWPolicyReceipts(s.policyId);
						getPolicybeneficiaries();
						getPolicybenefits();
						getPolicyInstallments();
						UTILITIES.getProcessActiveDiagram(s.policyId);
						UTILITIES.getTaskActive(s.policyId);
						UTILITIES.getProcessHistory(s.policyId);
						$("#client-info").text(s.client.fname+" "+s.client.otherNames);
						$("#binder-info").text(s.binder.binName);
						if(s.paymentMode)
						$("#pay-mode-info").text(s.paymentMode.pmDesc);
						$("#branch-info").text(s.branch.obName);
						$("#currency-info").text(s.transCurrency.curName);
						console.log("s.totalInstalments="+s.totalInstalments)
						$("#pol-tot-inst").text(s.totalInstalments);
						$("#pol-no").text(s.polNo);
						$("#pol-term-display").css('display','block').text(s.polTerm);
						$("#prop-no").text(s.proposalNo);
						$("#div-pol-no").val(s.polNo);
						$("#div-endos-no").val(s.polRevNo);
						$("#policy-id").val(s.policyId);
						$("#pol-rev-no").text(s.polRevNo);
						$("#pol-bind-age-appli").val(s.product.ageApplicable);
						$("#pol-sub-agent-comm").text(UTILITIES.currencyFormat(s.subAgentComm));
						$("#pol-sum-insured").text(UTILITIES.currencyFormat(s.sumInsured));
						$("#pol-premium").text(UTILITIES.currencyFormat(s.premium));
						$("#pol-basic-prem").text(UTILITIES.currencyFormat(s.basicPrem));
						$("#pol-net-prem").text(UTILITIES.currencyFormat(s.netPrem));
						$("#pol-taxes-amt").text(s.taxes);

						if(s.product.motorProduct){
							$(".motor-disp").show();
							$(".non-motor-disp").hide();
						}
						else{
							$(".motor-disp").hide();
							$(".non-motor-disp").show();
						}
						$("#pol-prod-name").text(s.product.proDesc);
						$("#from-date").val(moment(s.wefDate).format('DD/MM/YYYY'));
						$("#wet-date").val(moment(s.wetDate).format('DD/MM/YYYY'));
						$('#pol-ins-comp').text(s.agent.name)
						$("#pol-interface-type").val(s.interfaceType);
						$("#pol-frequency").val(s.frequency);
						$("#client-pol-no").val(s.clientPolNo);
						$("#pol-buss-type").val(s.businessType);
						if(s.subAgent){
							$("#sub-agent-id").val(s.subAgent.acctId);
							$("#sub-agent-name").val(s.subAgent.name);
							populateSubAgentsLov();
						}
						$("#cur-id").val(s.transCurrency.curCode);
						$("#cur-name").val(s.transCurrency.curName);
						populateCurrencyLov();
						$("#client-id").val(s.client.tenId);
						$("#client-f-name").val(s.client.fname);
						$("#client-other-name").val(s.client.otherNames);
						populateClientLov();
						getPolicyTerms(s.binder.binId);
						console.log(s.polTerm);
						$("#pol-term").val(s.polTerm);
						$("#risk-binder").text(s.binder.binName);
						$("#risk-binder-id").val(s.binder.binId);
						$("#risk-binder-code").val(s.binder.binId);
						$("#risk-bind-code").val(s.binder.binId);
						$("#binder-id").val(s.binder.binId);
						$("#product-id").val(s.product.proCode);
						$("#pol-agent-id").val(s.agent.acctId);
						$("#bind-name").val(s.binder.binName);
						$("#pol-binder-policy").val(s.binder.binType);
						populateBinderLov();
						$("#pm-id").val(s.paymentMode.pmId);
						$("#pm-name").val(s.paymentMode.pmDesc);
						populatePaymentModes();
						$("#brn-id").val(s.branch.obId);
						$("#brn-name").val(s.branch.obName);
						$('#pol-comm-amt').text(UTILITIES.currencyFormat(s.commAmt));
						$('#pol-tl').text(UTILITIES.currencyFormat(s.trainingLevy));
						$('#pol-phcf').text(UTILITIES.currencyFormat(s.phcf));
						$('#pol-sd').text(UTILITIES.currencyFormat(s.stampDuty));
						$('#pol-whtx').text(UTILITIES.currencyFormat(s.whtx));
						$('#pol-extras').text(UTILITIES.currencyFormat(s.extras));
						$("#pol-fap").text(UTILITIES.currencyFormat(s.futurePrem));
						$("#pol-bin-type").val(s.binder.binType);
						$('#pol-tran-type-disp').text(s.transType);
						$('#pol-trans-type').val(s.transType);
						$('#pol-prev-policy').val(s.prevPolicy);
						$('#pol-prev-policy').val(s.prevPolicy);
						$('#pol-reuse-contra-policy').val(s.reusecontraPolicy);

						if(s.transType==="EN"){
							if(s.authStatus==="D"){
								$("#btn-endors-risk").show();
							}
							else{
								$("#btn-endors-risk").hide();
							}
							$("ul.reports-links li.endorse-disp").show();

						}
						else{
							$("#btn-endors-risk").hide();
							$("ul.reports-links li.endorse-disp").hide();
						}

						if(s.transType==="CO" || s.transType==="CN"){
							$("#btn-undo-make-ready").css("display","none");
							$("#btn-make-ready-policy").css("display","none");
						}

						if(s.transType==="RN"){
							$("#renbtn").show();
						}
						else{
							$("#renbtn").hide();
						}
						$("#binder-frm").select2("enable", false);
						$("#pol-bintype").prop("disabled", true);
						populateUserBranches();
						if(s.renewalDate)
							$("#pol-ren-date").text(moment(s.renewalDate).format('DD/MM/YYYY'));
					},
					error: function(xhr, error){
						bootbox.alert(xhr.responseText);
					}
				});
			}
			else{
				$("#display-client").hide();
				$("#display-binder").hide();
				$("#display-payment-mode").hide();
				$("#display-branch").hide();
				$("#pol-term-display").css('display','none')
			}
		}
	};

	var changePolicyWetDt = function(){
		$('#cover-to-date').on('dp.change', function (ev) {
			var curDate = ev.date;
			var dt = moment(curDate).format('DD/MM/YYYY');
			$("#risk-wet-date").val(dt);
		});
		$('#risk-cover-from').on('dp.change', function (ev) {
			var curDate = ev.date;
			var dt = moment(curDate);
			var polwef = $("#from-date").val();
			var wet = $("#wet-date").val();

		});
		$('#risk-cover-to').on('dp.change', function (ev) {
			var curDate = ev.date;
			var dt = moment(curDate).format('DD/MM/YYYY');;
			var polwef = $("#from-date").val();
			var polwet = $("#wet-date").val();

		});
	};

	var validateRisk = function(){
		$('#risk-id').on('change', function () {
			var riskId = $("#risk-id").val();
			var subCode = $("#risk-sub-code").val();
			$.ajax({
				type: 'GET',
				url:  'validateRisk',
				dataType: 'json',
				data: {"riskId":riskId,"sclCode":subCode},
				async: true,
				success: function(result) {

				},
				error: function(jqXHR, textStatus, errorThrown) {
					new PNotify({
						title: 'Error',
						text: jqXHR.responseText,
						type: 'error',
						styling: 'bootstrap3'
					});
					$("#risk-id").val("");
				}
			});
		});
	};

	var getPolicyWet = function(){
		$('#wef-date').on('dp.change', function (ev) {
			var curDate = ev.date;
			var dt = moment(curDate).format('DD/MM/YYYY');
			$("#risk-wef-date").val(dt);
			$.ajax({
				type: 'GET',
				url:  SERVLET_CONTEXT+'/protected/uw/policies/getMaturityDate',
				dataType: 'json',
				data: {"wefDate":dt,"polTerm":$('#pol-term').val()},
				async: true,
				success: function(result) {
					$("#wet-date").val(moment(result).format('DD/MM/YYYY'));
					$("#risk-wet-date").val(moment(result).format('DD/MM/YYYY'));


				},
				error: function(jqXHR, textStatus, errorThrown) {

				}
			});
		});
		$("#pol-term").on('change', function(){

			if (!($("#from-date").val()===null)) {
			$.ajax({
				type: 'GET',
				url:  SERVLET_CONTEXT+'/protected/uw/policies/getMaturityDate',
				dataType: 'json',
				data: {"wefDate":$("#from-date").val(),"polTerm":$('#pol-term').val()},
				async: true,
				success: function(result) {
					$("#wet-date").val(moment(result).format('DD/MM/YYYY'));
					$("#risk-wet-date").val(moment(result).format('DD/MM/YYYY'));


				},
				error: function(jqXHR, textStatus, errorThrown) {

				}
			});
		}
		});
	};



	var populateInsuredLov = function(){

		if($("#lifeassured-frm").filter("div").html() != undefined)
		{
			Select2Builder.initAjaxSelect2({
				containerId : "lifeassured-frm",
				sort : 'fname',
				change: function(e, a, v){

					if($("#pol-bind-age-appli").val() && $("#pol-bind-age-appli").val()==="Y"){

						$.ajax({
							type: 'GET',
							url:  'getLifeClientAge',
							dataType: 'json',
							data: {"clientId": e.added.tenId ,"binCode" :$("#binder-id").val()},
							async: true,
							success: function(result) {

								$("#lifeassured-age").val(result);
								$("#lifeassured-code").val(e.added.tenId);
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
					}else
						$("#lifeassured-code").val(e.added.tenId);
					//$("#insured-id").val(e.added.tenId);
				},
				formatResult : function(a)
				{
					return a.fname+" "+a.otherNames;
				},
				formatSelection : function(a)
				{
					return a.fname+" "+a.otherNames;
				},
				initSelection: function (element, callback) {
					var code = $("#lifeassured-code").val();
					var name = $("#lifeassured-name").val();
					var othernames = $("#lifeassured-other-name").val();
					var data = {fname:name,otherNames:othernames,tenId:code};
					callback(data);
				},
				id: "tenId",
				width:"250px",
				placeholder:"Select Insured"

			});
		}
	}

	var populateClientLov = function(){
		if($("#client-frm").filter("div").html() != undefined)
		{
			Select2Builder.initAjaxSelect2({
				containerId : "client-frm",
				sort : 'fname',
				change: function(e, a, v){
					$("#client-id").val(e.added.tenId);
						$("#lifeassured-age").val("");
						populateInsuredLov();


				},
				formatResult : function(a)
				{
					return a.fname+" "+a.otherNames;
				},
				formatSelection : function(a)
				{
					return a.fname+" "+a.otherNames;
				},
				initSelection: function (element, callback) {
					var code = $("#client-id").val();
					var name = $("#client-f-name").val();
					var othernames = $("#client-other-name").val();
					var data = {fname:name,otherNames:othernames,tenId:code};
					callback(data);
				},
				id: "tenId",
				placeholder:"Select Assured"

			});
		}
	};




	var populateSubAgentsLov = function(){
		if($("#sub-agent-frm").filter("div").html() != undefined)
		{
			Select2Builder.initAjaxSelect2({
				containerId : "sub-agent-frm",
				sort : 'name',
				change: function(e, a, v){
					$("#sub-agent-id").val(e.added.acctId);

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
					var code  = $('#sub-agent-id').val();
					var name = $("#sub-agent-name").val();
					var data = {name:name,acctId:code};
					callback(data);
				},
				id: "acctId",
				width:"250px",
				placeholder:"Select Sub Agent"

			});

			$("#sub-agent-frm").on("select2-removed", function(e) {
				$("#sub-agent-id").val('');
			})
		}
	};

	var populateCurrencyLov = function(){
		if($("#curr-frm").filter("div").html() != undefined)
		{
			Select2Builder.initAjaxSelect2({
				containerId : "curr-frm",
				sort : 'curName',
				change: function(e, a, v){
					$("#cur-id").val(e.added.curCode);

				},
				formatResult : function(a)
				{
					return a.curName;
				},
				formatSelection : function(a)
				{
					return a.curName;
				},
				initSelection: function (element, callback) {
					var code  = $('#cur-id').val();
					var name = $("#cur-name").val();
					var data = {curName:name,curCode:code};
					callback(data);
				},
				id: "curCode",
				width:"250px",
				placeholder:"Select Currency"

			});
		}
	};

	var populateSubclassLov = function(){
		if($("#subclass-frm").filter("div").html() != undefined)
		{
			Select2Builder.initAjaxSelect2({
				containerId : "subclass-frm",
				sort : 'detId',
				change: function(e, a, v){
					$("#risk-sub-code").val(e.added.subId);
					populateCoverTypesLov();

				},
				formatResult : function(a)
				{
					return a.subDesc;
				},
				formatSelection : function(a)
				{
					return a.subDesc;
				},
				initSelection: function (element, callback) {
					var code  = $('#risk-sub-code').val();
					var name = $("#sub-name").val();
					var data = {subDesc:name,subId:code};
					callback(data);
				},
				id: "subDesc",
				width:"250px",
				params: {bindCode: $("#risk-bind-code").val()},
				placeholder:"Select Sub Class"

			});
		}
	};

	var populateCoverTypesLov = function(){
		if($("#covertypes-frm").filter("div").html() != undefined)
		{
			Select2Builder.initAjaxSelect2({
				containerId : "covertypes-frm",
				sort : 'detId',
				change: function(e, a, v){
					$("#risk-cov-code").val(e.added.covId);
					$("#binder-det-id").val(e.added.detId);
					//getCommissionRate(e.added.detId);
					getPremiumRates(e.added.detId);
				},
				formatResult : function(a)
				{
					return a.covName;
				},
				formatSelection : function(a)
				{
					return a.covName;
				},
				initSelection: function (element, callback) {
					var code  = $('#risk-cov-code').val();
					var name = $("#cover-name").val();
					var data = {covName:name,covId:code};
					callback(data);
				},
				id: "covName",
				width:"250px",
				params: {bindCode: $("#risk-bind-code").val(),subCode: $("#risk-sub-code").val()},
				placeholder:"Select Cover Type"

			});
		}
	};

	var getPremiumRates = function(detId){

		if($("#pol-bind-age-appli").val() && $("#pol-bind-age-appli").val()==="Y"){


			$.ajax({
				type: 'GET',
				url: 'getBinderClientPremRates',
				dataType: 'json',
				data: {"detId": detId,"age":$("#lifeassured-age").val()},
				async: true,
				success: function (result) {
					$("#section_form_tbl tbody").each(function () {
						$(this).remove();
					});
					for (var res in result) {
						var markup = "<tr><td><input type='hidden' class='mandatory form-control' value='" + result[res].mandatory + "'><input type='hidden' class='section form-control' value='" + result[res].premId +
							"'><input type='hidden' class='ratesApplicable form-control' value='" + result[res].ratesApplicable + "'>" + result[res].sectionDesc + "</td><td>" + ' <input type="text" class="amount form-control" required>' +
							"</td><td><input type='text' class='rate form-control' value='" + result[res].rate + "'></td><td><input type='text' class='divFactor form-control'  value='" + result[res].divFactor + "'></td></td><td>" +
							"<input type='text' class='freeLimit form-control'  value='" + result[res].freeLimit + "'></td>" +
							"<td><button type='button' class='btn btn-danger btn btn-danger btn-xs hyperlink-btn'><i class='fa fa-trash-o'></button></td></tr>";
						if($("#pol-bin-type").val()==="B"){
							if (result[res].ratesApplicable && result[res].ratesApplicable === "Y") {
								markup = "<tr><td><input type='hidden' class='mandatory form-control' value='" + result[res].mandatory + "'><input type='hidden' class='section form-control' value='" + result[res].premId +
									"'><input type='hidden' class='ratesApplicable form-control' value='" + result[res].ratesApplicable + "'>" + result[res].sectionDesc + "</td><td>" + ' <input type="text" class="amount form-control" value="' + $("#lifeassured-age").val() + '" readonly>' +
									"</td><td><input type='text' class='rate form-control' readonly value='" + result[res].rate + "'></td><td><input type='text' class='divFactor form-control' readonly value='" + result[res].divFactor + "'></td></td><td>" +
									"<input type='text' class='freeLimit form-control' readonly  value='" + result[res].freeLimit + "'></td>" +
									"<td><button type='button' class='btn btn-danger btn btn-danger btn-xs hyperlink-btn'><i class='fa fa-trash-o'></button></td></tr>";
							}
							else if (result[res].rider && result[res].rider === "Y") {
								markup = "<tr><td><input type='hidden' class='mandatory form-control' value='" + result[res].mandatory + "'><input type='hidden' class='section form-control' value='" + result[res].premId +
									"'><input type='hidden' class='ratesApplicable form-control' value='" + result[res].ratesApplicable + "'>" + result[res].sectionDesc + "</td><td>" + ' <input type="text" class="amount form-control" readonly>' +
									"</td><td><input type='text' class='rate form-control' value='" + result[res].rate + "'></td><td><input type='text' class='divFactor form-control' value='" + result[res].divFactor + "'></td></td><td>" +
									"<input type='text' class='freeLimit form-control'  value='" + result[res].freeLimit + "'></td>" +
									"<td><button type='button' class='btn btn-danger btn btn-danger btn-xs hyperlink-btn'><i class='fa fa-trash-o'></button></td></tr>";
							}else {
							markup = "<tr><td><input type='hidden' class='mandatory form-control' value='" + result[res].mandatory + "'><input type='hidden' class='section form-control' value='" + result[res].premId +
								"'><input type='hidden' class='ratesApplicable form-control' value='" + result[res].ratesApplicable + "'>" + result[res].sectionDesc + "</td><td>" + ' <input type="text" class="amount form-control" required>' +
								"</td><td><input type='text' class='rate form-control' readonly value='" + result[res].rate + "'></td><td><input type='text' class='divFactor form-control' readonly value='" + result[res].divFactor + "'></td></td><td>" +
								"<input type='text' class='freeLimit form-control' readonly  value='" + result[res].freeLimit + "'></td>" +
								"<td><button type='button' class='btn btn-danger btn btn-danger btn-xs hyperlink-btn'><i class='fa fa-trash-o'></button></td></tr>";
							}
						}
						else {
							if (result[res].ratesApplicable && result[res].ratesApplicable === "Y") {
								markup = "<tr><td><input type='hidden' class='mandatory form-control' value='" + result[res].mandatory + "'><input type='hidden' class='section form-control' value='" + result[res].premId +
									"'><input type='hidden' class='ratesApplicable form-control' value='" + result[res].ratesApplicable + "'>" + result[res].sectionDesc + "</td><td>" + ' <input type="text" class="amount form-control" value="' + $("#lifeassured-age").val() + '" readonly>' +
									"</td><td><input type='text' class='rate form-control' readonly value='" + result[res].rate + "'></td><td><input type='text' class='divFactor form-control' readonly value='" + result[res].divFactor + "'></td></td><td>" +
									"<input type='text' class='freeLimit form-control' readonly  value='" + result[res].freeLimit + "'></td>" +
									"<td><button type='button' class='btn btn-danger btn btn-danger btn-xs hyperlink-btn'><i class='fa fa-trash-o'></button></td></tr>";
							}
							else if (result[res].rider && result[res].rider === "Y") {
								markup = "<tr><td><input type='hidden' class='mandatory form-control' value='" + result[res].mandatory + "'><input type='hidden' class='section form-control' value='" + result[res].premId +
									"'><input type='hidden' class='ratesApplicable form-control' value='" + result[res].ratesApplicable + "'>" + result[res].sectionDesc + "</td><td>" + ' <input type="text" class="amount form-control" readonly>' +
									"</td><td><input type='text' class='rate form-control' value='" + result[res].rate + "'></td><td><input type='text' class='divFactor form-control' value='" + result[res].divFactor + "'></td></td><td>" +
									"<input type='text' class='freeLimit form-control'  value='" + result[res].freeLimit + "'></td>" +
									"<td><button type='button' class='btn btn-danger btn btn-danger btn-xs hyperlink-btn'><i class='fa fa-trash-o'></button></td></tr>";
							}
						}
						$("#section_form_tbl").append(markup);
					}

					$("#section_form_tbl tr").find("input[type=text]").number(true, 2);
				},
				error: function (jqXHR, textStatus, errorThrown) {

				}
			});
		}
		else {
			$.ajax({
				type: 'GET',
				url: 'getBinderPremRates',
				dataType: 'json',
				data: {"detId": detId},
				async: true,
				success: function (result) {
					$("#section_form_tbl tbody").each(function () {
						$(this).remove();
					});
					for (var res in result) {
						var markup = "<tr><td><input type='hidden' class='mandatory form-control' value='" + result[res].mandatory + "'><input type='hidden' class='section form-control' value='" + result[res].premId +
							"'><input type='hidden' class='ratesApplicable form-control' value='" + result[res].ratesApplicable + "'>" + result[res].sectionDesc + "</td><td>" + ' <input type="text" class="amount form-control" required>' +
							"</td><td><input type='text' class='rate form-control' value='" + result[res].rate + "'></td><td><input type='text' class='divFactor form-control'  value='" + result[res].divFactor + "'></td></td><td>" +
							"<input type='text' class='freeLimit form-control'  value='" + result[res].freeLimit + "'></td>" +
							"<td><button type='button' class='btn btn-danger btn btn-danger btn-xs hyperlink-btn'><i class='fa fa-trash-o'></button></td></tr>";
						if($("#pol-bin-type").val()==="B"){
							markup = "<tr><td><input type='hidden' class='mandatory form-control' value='" + result[res].mandatory + "'><input type='hidden' class='section form-control' value='" + result[res].premId +
								"'><input type='hidden' class='ratesApplicable form-control' value='" + result[res].ratesApplicable + "'>" + result[res].sectionDesc + "</td><td>" + ' <input type="text" class="amount form-control" required>' +
								"</td><td><input type='text' class='rate form-control' readonly value='" + result[res].rate + "'></td><td><input type='text' class='divFactor form-control' readonly value='" + result[res].divFactor + "'></td></td><td>" +
								"<input type='text' class='freeLimit form-control' readonly  value='" + result[res].freeLimit + "'></td>" +
								"<td><button type='button' class='btn btn-danger btn btn-danger btn-xs hyperlink-btn'><i class='fa fa-trash-o'></button></td></tr>";
						}
						else {
							if (result[res].ratesApplicable && result[res].ratesApplicable === "Y") {
								markup = "<tr><td><input type='hidden' class='mandatory form-control' value='" + result[res].mandatory + "'><input type='hidden' class='section form-control' value='" + result[res].premId +
									"'><input type='hidden' class='ratesApplicable form-control' value='" + result[res].ratesApplicable + "'>" + result[res].sectionDesc + "</td><td>" + ' <input type="text" class="amount form-control">' +
									"</td><td><input type='text' class='rate form-control' readonly value='" + result[res].rate + "'></td><td><input type='text' class='divFactor form-control' readonly value='" + result[res].divFactor + "'></td></td><td>" +
									"<input type='text' class='freeLimit form-control' readonly  value='" + result[res].freeLimit + "'></td>" +
									"<td><button type='button' class='btn btn-danger btn btn-danger btn-xs hyperlink-btn'><i class='fa fa-trash-o'></button></td></tr>";
							}
							else if (result[res].rider && result[res].rider === "Y") {
								markup = "<tr><td><input type='hidden' class='mandatory form-control' value='" + result[res].mandatory + "'><input type='hidden' class='section form-control' value='" + result[res].premId +
									"'><input type='hidden' class='ratesApplicable form-control' value='" + result[res].ratesApplicable + "'>" + result[res].sectionDesc + "</td><td>" + ' <input type="text" class="amount form-control" readonly>' +
									"</td><td><input type='text' class='rate form-control' value='" + result[res].rate + "'></td><td><input type='text' class='divFactor form-control' value='" + result[res].divFactor + "'></td></td><td>" +
									"<input type='text' class='freeLimit form-control'  value='" + result[res].freeLimit + "'></td>" +
									"<td><button type='button' class='btn btn-danger btn btn-danger btn-xs hyperlink-btn'><i class='fa fa-trash-o'></button></td></tr>";
							}
						}
						$("#section_form_tbl").append(markup);
					}

					$("#section_form_tbl tr").find("input[type=text]").number(true, 2);
				},
				error: function (jqXHR, textStatus, errorThrown) {

				}
			});
		}
	};

	var populateBinderLov = function(){
		if($("#binder-frm").filter("div").html() != undefined)
		{
			Select2Builder.initAjaxSelect2({
				containerId : "binder-frm",
				sort : 'binName',
				change: function(e, a, v){
					populateImportSubCovers(e.added.binId);
					$("#binder-id").val(e.added.binId);
					$("#risk-binder-code").val(e.added.binId);
					$("#risk-bind-code").val(e.added.binId);
					$("#pol-ins-comp").text(e.added.account.name);
					$("#pol-prod-name").text(e.added.product.proDesc);
					$("#product-id").val(e.added.product.proCode);
					$("#pol-agent-id").val(e.added.account.acctId);
					$("#client-pol-no").val(e.added.binPolNo);
					$("#risk-binder").text(e.added.binName);
					$("#pol-bind-age-appli").val(e.added.product.ageApplicable);
					$("#pol-buss-type").val("L");
					if(e.added.product.motorProduct){
						$(".motor-disp").show();
						$(".non-motor-disp").hide();
					}
					else{
						$(".motor-disp").hide();
						$(".non-motor-disp").show();
					}
					populateSubclassLov();
					getPolicyTerms(e.added.binId);
				},
				formatResult : function(a)
				{
					return a.binName;
				},
				formatSelection : function(a)
				{
					return a.binName;
				},
				initSelection: function (element, callback) {
					var code  = $('#binder-id').val();
					var name = $("#bind-name").val();
					var data = {binName:name,binId:code};
					callback(data);
				},
				id: "binId",
				width:"250px",
				params: {bindType: $("#pol-bin-type").val()},
				placeholder:"Select Contract"

			});
		}
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

	var populateUserBranches = function(){
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
					var code  = $('#brn-id').val();
					var name = $("#brn-name").val();
					var data = {obName:name,obId:code};
					callback(data);
				},
				id: "obId",
				width:"250px",
				placeholder:"Select Branch"

			});
		}
	};

	var createNewPolicy = function(){
		$("#btn-add-policy").click(function(){
			if(typeof polCode!== 'undefined'){
				if(polCode!==-2000){
					updatePolicy();
				}
				else{
					createPolicy();
				}
			}
			else{
				createPolicy();
			}
		});

		$("#btn-save-risk").click(function(){
			if($("#risk-code-pk").val() != ''){
				updateRisk();
			}
			else
				createRisk();
		});

		$("#btn-make-ready-policy").click(function(){
			console.log('passed here...');
			makeReady();
		});

		$("#btn-undo-make-ready").click(function(){
			undoMakeReady();
		});

		$("#btn-auth-policy").click(function(){
			authorizePolicy();
		});

		$("#btn-dispatch-trans").click(function(){
			dispatchDocuments();
		});
		$("#btn-convert-policy").click(function(){
			convertToPolicy();
		});
	};

	var authorizePolicy = function(){
		var stack_bottomleft = {"dir1": "up", "dir2": "left", "firstpos1": 25, "firstpos2": 25};
		$('#myPleaseWait').modal({
			backdrop: 'static',
			keyboard: true
		});
		$.ajax({
			type: 'GET',
			url:  'authorizeLifePolicy',
			dataType: 'json',
			async: true,
			success: function(result) {
				$('#myPleaseWait').modal('hide');
				new PNotify({
					title: 'Success',
					text: 'Policy Authorized Successfully',
					type: 'success',
					styling: 'bootstrap3',
					addclass: 'stack-bottomright',
					stack: stack_bottomleft
				});
				populatePolicyDetails();
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
	};

	var dispatchDocuments = function(){
		var stack_bottomleft = {"dir1": "up", "dir2": "left", "firstpos1": 25, "firstpos2": 25};
		$('#myPleaseWait').modal({
			backdrop: 'static',
			keyboard: true
		});
		$.ajax({
			type: 'GET',
			url:  'dispatchDocs',
			dataType: 'json',
			async: true,
			success: function(result) {
				$('#myPleaseWait').modal('hide');
				new PNotify({
					title: 'Success',
					text: 'Document Dispatched Successfully',
					type: 'success',
					styling: 'bootstrap3',
					addclass: 'stack-bottomright',
					stack: stack_bottomleft
				});
				populatePolicyDetails();
				$("#btn-dispatch-trans").hide();
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
	};

	var convertToPolicy = function(){
		var stack_bottomleft = {"dir1": "up", "dir2": "left", "firstpos1": 25, "firstpos2": 25};
		$('#myPleaseWait').modal({
			backdrop: 'static',
			keyboard: true
		});
		$.ajax({
			type: 'POST',
			url:   SERVLET_CONTEXT+'/protected/life/policies/proposalConversion',
			dataType: 'json',
			async: true,
			success: function(result) {
				$('#myPleaseWait').modal('hide');
				new PNotify({
					title: 'Success',
					text: 'Make Ready Process Successfully',
					type: 'success',
					styling: 'bootstrap3',
					addclass: 'stack-bottomright',
					stack: stack_bottomleft
				});
				populatePolicyDetails();
				$('#polChecksList').DataTable().ajax.reload();
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
	};

	var makeReady = function(){
		var stack_bottomleft = {"dir1": "up", "dir2": "left", "firstpos1": 25, "firstpos2": 25};
		$('#myPleaseWait').modal({
			backdrop: 'static',
			keyboard: true
		});
		$.ajax({
			type: 'POST',
			url:  SERVLET_CONTEXT+'/protected/life/policies/createLifePolMakeReady',
			dataType: 'json',
			async: true,
			success: function(result) {
				$('#myPleaseWait').modal('hide');
				new PNotify({
					title: 'Success',
					text: 'Make Ready Process Successfully',
					type: 'success',
					styling: 'bootstrap3',
					addclass: 'stack-bottomright',
					stack: stack_bottomleft
				});
				populatePolicyDetails();
				$('#polChecksList').DataTable().ajax.reload();
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
	};

	var undoMakeReady = function(){
		var stack_bottomleft = {"dir1": "up", "dir2": "left", "firstpos1": 25, "firstpos2": 25};
		$('#myPleaseWait').modal({
			backdrop: 'static',
			keyboard: true
		});
		$.ajax({
			type: 'GET',
			url:  'undoMakeReady',
			dataType: 'json',
			async: true,
			success: function(result) {
				$('#myPleaseWait').modal('hide');
				new PNotify({
					title: 'Success',
					text: 'Make Ready Process Successfully',
					type: 'success',
					styling: 'bootstrap3',
					addclass: 'stack-bottomright',
					stack: stack_bottomleft
				});
				populatePolicyDetails();
				$('#polChecksList').DataTable().ajax.reload();
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
	};

	var createRisk = function(){
		var arr = getRskSections();
		if(arr.length==0){
			bootbox.alert("Cannot create Risk without sections")
			return false;
		}
		arr.shift();
		var $currForm = $('#risk-form');
		var currValidator = $currForm.validate();
		if (!$currForm.valid()) {
			return;
		}

		var data = {};
		$currForm.serializeArray().map(function(x){data[x.name] = x.value;});
		var url = "createRisk";
		data.sections = arr;
		$('#myPleaseWait').modal({
			backdrop: 'static',
			keyboard: true
		});

		$.ajax(
			{
				url:url,
				type: "POST",
				data: JSON.stringify(data),
				success: function(s){
					$('#myPleaseWait').modal('hide');
					new PNotify({
						title: 'Success',
						text: 'Risk Transaction created Successfully',
						type: 'success',
						styling: 'bootstrap3'
					});
					$('#insured-frm').select2('val', null);
					populateInsuredLov();
					$('#subclass-frm').select2('val', null);
					populateSubclassLov();
					$('#covertypes-frm').select2('val', null);
					populateCoverTypesLov();
					populatePolicyDetails();
					$("#sect-div").show();
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
	};

	var updateRisk = function(){
		var $currForm = $('#risk-form');
		var currValidator = $currForm.validate();
		if (!$currForm.valid()) {
			return;
		}

		var data = {};
		$currForm.serializeArray().map(function(x){data[x.name] = x.value;});
		var url = "createRisk";
		$('#myPleaseWait').modal({
			backdrop: 'static',
			keyboard: true
		});

		$.ajax(
			{
				url:url,
				type: "POST",
				data: JSON.stringify(data),
				success: function(s){
					$('#myPleaseWait').modal('hide');
					new PNotify({
						title: 'Success',
						text: 'Policy Transaction created Successfully',
						type: 'success',
						styling: 'bootstrap3'
					});
					$('#insured-frm').select2('val', null);
					populateInsuredLov();
					$('#subclass-frm').select2('val', null);
					populateSubclassLov();
					$('#covertypes-frm').select2('val', null);
					populateCoverTypesLov();
					populatePolicyDetails();
					$("#sect-div").show();
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
	};

	var updateMakeReadyPolicy = function(){
		var stack_bottomleft = {"dir1": "up", "dir2": "left", "firstpos1": 25, "firstpos2": 25};
		var $currForm = $('#policy-form');
		var currValidator = $currForm.validate();
		if (!$currForm.valid()) {
			return;
		}

		$('#policy-form input[type=checkbox]').each(function(e){
			$(this).val($(this).is(':checked'));
		});

		var data = {};
		$currForm.serializeArray().map(function(x){data[x.name] = x.value;});
		var url = "createPolicyMakeReady";

		$('#myPleaseWait').modal({
			backdrop: 'static',
			keyboard: true
		});
		$.ajax({
			url:url,
			type: "POST",
			data: JSON.stringify(data),
			success: function(s){
				$('#myPleaseWait').modal('hide');
				new PNotify({
					title: 'Success',
					text: 'Policy Transaction created Successfully',
					type: 'success',
					styling: 'bootstrap3',
					addclass: 'stack-bottomright',
					stack: stack_bottomleft
				});
				$('#insured-frm').select2('val', null);
				populateInsuredLov();
				$('#subclass-frm').select2('val', null);
				populateSubclassLov();
				$('#covertypes-frm').select2('val', null);
				populateCoverTypesLov();
				polCode = s.policyId;
				populatePolicyDetails();
			},
			error: function(jqXHR, textStatus, errorThrown){
				$('#myPleaseWait').modal('hide');
				new PNotify({
					title: 'Error',
					text: jqXHR.responseText,
					type: 'error',
					styling: 'bootstrap3',
					addclass: 'stack-bottomright',
					stack: stack_bottomleft
				});
			},
			dataType: "json",
			contentType: "application/json"
		} );
	};

	var updatePolicy = function(){
		var stack_bottomleft = {"dir1": "up", "dir2": "left", "firstpos1": 25, "firstpos2": 25};
		var $currForm = $('#policy-form');
		var currValidator = $currForm.validate();
		if (!$currForm.valid()) {
			return;
		}

		$('#policy-form input[type=checkbox]').each(function(e){
			$(this).val($(this).is(':checked'));
		});

		var data = {};
		$currForm.serializeArray().map(function(x){data[x.name] = x.value;});
		var url = "createLifePolicy";

		$('#myPleaseWait').modal({
			backdrop: 'static',
			keyboard: true
		});
		$.ajax({
			url:url,
			type: "POST",
			data: JSON.stringify(data),
			success: function(s){
				$('#myPleaseWait').modal('hide');
				new PNotify({
					title: 'Success',
					text: 'Policy Transaction created Successfully',
					type: 'success',
					styling: 'bootstrap3',
					addclass: 'stack-bottomright',
					stack: stack_bottomleft
				});
				$('#insured-frm').select2('val', null);
				populateInsuredLov();
				$('#subclass-frm').select2('val', null);
				populateSubclassLov();
				$('#covertypes-frm').select2('val', null);
				populateCoverTypesLov();
				polCode = s.policyId;
				populatePolicyDetails();
			},
			error: function(jqXHR, textStatus, errorThrown){
				$('#myPleaseWait').modal('hide');
				new PNotify({
					title: 'Error',
					text: jqXHR.responseText,
					type: 'error',
					styling: 'bootstrap3',
					addclass: 'stack-bottomright',
					stack: stack_bottomleft
				});
			},
			dataType: "json",
			contentType: "application/json"
		} );
	};

	var createPolicy = function(){
		var stack_bottomleft = {"dir1": "up", "dir2": "left", "firstpos1": 25, "firstpos2": 25};
		var $currForm = $('#policy-form');
		var currValidator = $currForm.validate();
		if (!$currForm.valid()) {
			return;
		}

		$('#policy-form input[type=checkbox]').each(function(e){
			$(this).val($(this).is(':checked'));
		});

		var data = {};
		$currForm.serializeArray().map(function(x){data[x.name] = x.value;});
		var url = "createLifePolicy";
		if(!$('#chk-import-risks').is(':checked')) {
			var riskForm = $("#risk-form");
			var riskValidator = riskForm.validate();
			if (!riskForm.valid()) {
				return;
			}
			var arr = getRskSections();
			if(arr.length==0){
				bootbox.alert("Cannot create policy without sections")
				return false;
			}
			arr.shift();
			data.sections = arr;
			data.riskBean = getRiskDetails();
		}
		$('#myPleaseWait').modal({
			backdrop: 'static',
			keyboard: true
		});
		$.ajax(
			{
				url:url,
				type: "POST",
				data: JSON.stringify(data),
				success: function(s){
					$('#myPleaseWait').modal('hide');
					$(".risk-detail-tab").show();
					new PNotify({
						title: 'Success',
						text: 'Policy Transaction created Successfully',
						type: 'success',
						styling: 'bootstrap3',
						addclass: 'stack-bottomright',
						stack: stack_bottomleft
					});
					$('#lifeassured-frm').select2('val', null);
					populateInsuredLov();
					$('#subclass-frm').select2('val', null);
					populateSubclassLov();
					$('#covertypes-frm').select2('val', null);
					populateCoverTypesLov();
					polCode = s.policyId;
					populatePolicyDetails();
				},
				error: function(jqXHR, textStatus, errorThrown){
					$('#myPleaseWait').modal('hide');
					new PNotify({
						title: 'Error',
						text: jqXHR.responseText,
						type: 'error',
						styling: 'bootstrap3',
						addclass: 'stack-bottomright',
						stack: stack_bottomleft
					});
				},
				dataType: "json",
				contentType: "application/json"
			} );
	};

	var getRiskDetails = function(){
		var $currForm = $('#risk-form');
		var currValidator = $currForm.validate();
		if (!$currForm.valid()) {
			return;
		}
		$('#risk-form input[type=checkbox]').each(function(e){
			$(this).val($(this).is(':checked'));
		});
		var data = {};
		$currForm.serializeArray().map(function(x){data[x.name] = x.value;});
		return data;
	};

	var getRskSections = function(){
		var arr = [];
		$("#section_form_tbl tr").each(function(row,tr){
			var section   = $(this).find('.section').eq(0).val();
			var ratesApplicable   = $(this).find('.ratesApplicable').eq(0).val();
			var rate   = $(this).find('.rate').eq(0).val();
			var divFactor   = $(this).find('.divFactor').eq(0).val();
			var freeLimit   = $(this).find('.freeLimit').eq(0).val();
			var amount = $(this).find('.amount').eq(0).val();
			var multiplierRate= $(this).find('.multiplierRate').eq(0).val();
			arr.push({
				section: section,
				ratesApplicable: ratesApplicable,
				rate:rate,
				divFactor:divFactor,
				freeLimit:freeLimit,
				amount: amount,
				multiplierRate:multiplierRate
			});
		});
		return arr;
	};

	var getRiskCertificates = function(){
		var url = "riskCerts/"+selRiskCode;
		var currTable = $('#cert_tbl').DataTable(UTILITIES.extendsOpts({
			"ajaxUrl":url,
			"columns": [
				{ "data": "cert",
					"render": function ( data, type, full, meta ) {
						return full.cert.certLots.certTypes.certDesc;
					}
				},
				{ "data": "status" ,
					"render": function ( data, type, full, meta ) {
						if(full.status){
							if(full.status==="A"){
								return "Active";
							}
							else if(full.status==="C"){
								return "Cancelled";
							}
							else if(full.status==="I"){
								return "Inactive";
							}
						}else
							return full.status;
					}
				},
				{ "data": "printStatus",
					"render": function ( data, type, full, meta ) {
						if(full.printStatus){
							if(full.printStatus==="P"){
								return "Printed";
							}
							else if(full.printStatus==="R"){
								return "Ready";
							}
						}else
							return full.printStatus;
					}
				},
				{ "data": "certWef" ,
					"render": function ( data, type, full, meta ) {
						return moment(full.certWef).format('DD/MM/YYYY');
					}
				},
				{ "data": "certWet" ,
					"render": function ( data, type, full, meta ) {
						return moment(full.certWet).format('DD/MM/YYYY');
					}
				},
				{ "data": "certNo" },
				{ "data": "reasonCancelled" },
				{
					"data": "pcId",
					"render": function ( data, type, full, meta ) {
						if(full.risk.policy.authStatus){
							if(full.risk.policy.authStatus ==="D")
								return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-certs='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.editRiskCerts(this);"><i class="fa fa-pencil-square-o"></button>';
							else
								return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-certs='+encodeURI(JSON.stringify(full)) + ' disabled><i class="fa fa-pencil-square-o"></button>';

						}
						else
							return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-certs='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.editRiskCerts(this);"><i class="fa fa-pencil-square-o"></button>';
					}

				},
				{
					"data": "pcId",
					"render": function ( data, type, full, meta ) {
						if(full.risk.policy.authStatus){
							if(full.risk.policy.authStatus ==="D")
								return '<button type="button" class="btn btn-danger btn btn-info btn-xs" data-certs='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.deleteRiskCerts(this);"><i class="fa fa-trash-o"></button>';
							else
								return '<button type="button" class="btn btn-danger btn btn-info btn-xs" data-certs='+encodeURI(JSON.stringify(full)) + '  disabled><i class="fa fa-trash-o"></button>';

						}
						else
							return '<button type="button" class="btn btn-danger btn btn-info btn-xs" data-certs='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.deleteRiskCerts(this);"><i class="fa fa-trash-o"></button>';
					}

				},
			]
		}));
		return currTable;
	};

	var getRiskSections = function(){
		var url = "risksSections/"+selRiskCode;
		var currTable = $('#section_tbl').DataTable(UTILITIES.extendsOpts({
			"ajaxUrl":url,
			"columns": [
				{ "data": "section",
					"render": function ( data, type, full, meta ) {

						return full.section.desc;
					}
				},
				{ "data": "rate" },
				{ "data": "calcprem"  ,
					"render": function ( data, type, full, meta ) {
						return UTILITIES.currencyFormat(full.calcprem);
					}
				},
				{ "data": "prem"  ,
					"render": function ( data, type, full, meta ) {

						return UTILITIES.currencyFormat(full.prem);
					}
				},
				{
					"data": "sectId",
					"render": function ( data, type, full, meta ) {
						// if(full.risk.policy.authStatus){
						// 	if(full.risk.policy.authStatus ==="D")
						// 		return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-risksections='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.editRiskSection(this);"><i class="fa fa-pencil-square-o"></button>';
						// 	else
						// 		return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-risksections='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.editRiskSection(this);" disabled><i class="fa fa-pencil-square-o"></button>';
						//
						// }
						// else
							return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-risksections='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.editRiskSection(this);"><i class="fa fa-pencil-square-o"></button>';
					}

				},
				{
					"data": "sectId",
					"render": function ( data, type, full, meta ) {
						// if(full.risk.policy.authStatus){
						// 	if(full.risk.policy.authStatus ==="D")
						// 		return '<button type="button" class="btn btn-danger btn btn-info btn-xs" data-risksections='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.deleteRiskSection(this);"><i class="fa fa-trash-o"></button>';
						// 	else
						// 		return '<button type="button" class="btn btn-danger btn btn-info btn-xs" data-risksections='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.deleteRiskSection(this);" disabled><i class="fa fa-trash-o"></button>';
						//
						// }
						// else
							return '<button type="button" class="btn btn-danger btn btn-info btn-xs" data-risksections='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.deleteRiskSection(this);"><i class="fa fa-trash-o"></button>';
					}

				},
			]
		}) );
		return currTable;
	};

	var getCommissionAllocs = function(receiptId){
		var url =SERVLET_CONTEXT+ "/protected/life/policies/allocationCommission/"+receiptId;
		var currTable = $('#receiptalloc_tbl').DataTable(UTILITIES.extendsOpts({
			"ajaxUrl":url,
			"columns": [
				{ "data": "allocations",
					"render": function ( data, type, full, meta ) {

						return full.allocations.installNo;
					}
				},
				{ "data": "lifeReceipts",
					"render": function ( data, type, full, meta ) {

						return UTILITIES.currencyFormat(full.allocations.instalmentPremium);
					}
				},
				{ "data": "lifeReceipts",
					"render": function ( data, type, full, meta ) {

						return UTILITIES.currencyFormat(full.commissionAmt);
					}
				},
				{
					"data": "allocations",
					"render": function (data, type, full, meta) {

						return moment(full.allocations.paidToDate).format('DD/MM/YYYY');

					}
				},
			]
		}) );
		return currTable;
	};

	var deleteBeneficiary = function(button){
		var beneficiaries = JSON.parse(decodeURI($(button).data("beneficiary")));
		bootbox.confirm("Are you sure want to delete "+beneficiaries['beneficiaryName']+"?", function(result) {
			if(result){
				$('#myPleaseWait').modal({
					backdrop: 'static',
					keyboard: true
				});
				$.ajax({
					type: 'GET',
					url:  'deleteBeneficiary/' + beneficiaries['beneficiaryCode'],
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
						$('#benefeciary_tbl').DataTable().ajax.reload();

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





	var getPolicybeneficiaries  = function(){
		var url = "policyBeneficiary/"+polCode;
		var currTable = $('#benefeciary_tbl').DataTable(UTILITIES.extendsOpts({
			"ajaxUrl":url,
			"columns": [
				{ "data": "beneficiaryName",
					"render": function ( data, type, full, meta ) {

						return full.beneficiaryName;
					}
				},
				{ "data": "beneficiaryType" ,
					"render": function ( data, type, full, meta ) {

						if(full.beneficiaryType){
							if(full.beneficiaryType==="B"){
								return "Beneficiary";
							}
							else if(full.beneficiaryType==="C"){
								return "Contigent Beneficiary";
							}
						}
						else
							return "";
					}
				},
				{ "data": "benAllocation"  ,
					"render": function ( data, type, full, meta ) {
						return full.benAllocation;
					}
				},
				{ "data": "beneficiaryregNo"  ,
					"render": function ( data, type, full, meta ) {

						return full.beneficiaryregNo;
					}
				},
				{
					"data": "beneficiaryCode",
					"render": function ( data, type, full, meta ) {
						if(full.policy.authStatus){
							if(full.policy.authStatus ==="D")
								return '<button type="button" class="btn btn-danger btn btn-info btn-xs" data-beneficiary='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.deleteBeneficiary(this);"><i class="fa fa-trash-o"></button>';
							else
								return '<button type="button" class="btn btn-danger btn btn-info btn-xs" data-beneficiary='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.deleteBeneficiary(this);" disabled><i class="fa fa-trash-o"></button>';

						}
						else
							return '<button type="button" class="btn btn-danger btn btn-info btn-xs" data-beneficiary='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.deleteBeneficiary(this);"><i class="fa fa-trash-o"></button>';
					}

				},
			]
		}) );
		return currTable;
	};


	var getPolicybenefits  = function(){
		var url = "policyBenefits/"+polCode;
		var currTable = $('#benefit_tbl').DataTable(UTILITIES.extendsOpts({
			"ajaxUrl":url,
			"columns": [
				{ "data": "maturityYear"
				},
				{ "data": "maturityExpDate" ,
					"render": function ( data, type, full, meta ) {

						return moment(full.maturityExpDate).format('DD/MM/YYYY');
					}
				},
				{ "data": "estBenefit"  ,
					"render": function ( data, type, full, meta ) {
						return UTILITIES.currencyFormat(full.estBenefit);
					}
				}
			]
		}) );
		return currTable;
	};

	var getPolicyInstallments  = function(){
		var url = SERVLET_CONTEXT+"/protected/life/policies/policyInstallments";
		var currTable = $('#installments_tbl').DataTable(UTILITIES.extendsOpts({
			"ajaxUrl":url,
			"columns": [
				{ "data": "installPrem"  ,
					"render": function ( data, type, full, meta ) {
						return UTILITIES.currencyFormat(full.installPrem);
					}
				},
				{ "data": "dueDate" ,
					"render": function ( data, type, full, meta ) {

						return moment(full.dueDate).format('DD/MM/YYYY');
					}
				}
			]
		}) );
		return currTable;
	};

	var getRiskDocs = function(){
		var url = "riskDocs/"+selRiskCode;
		var currTable = $('#risk_docs_tbl').DataTable(UTILITIES.extendsOpts({
			"ajaxUrl":url,
			"columns": [
				{ "data": "reqdDocs",
					"render": function ( data, type, full, meta ) {

						return full.risk.policy.polRevNo;
					}
				},
				{ "data": "reqdDocs",
					"render": function ( data, type, full, meta ) {

						return full.reqdDocs.requiredDoc.reqShtDesc;
					}
				},
				{ "data": "reqdDocs",
					"render": function ( data, type, full, meta ) {

						return full.reqdDocs.requiredDoc.reqDesc;
					}
				},
				{ "data": "uploadedFileName" },
				{ "data": "checkSum" },
				{
					"data": "rdId",
					"render": function ( data, type, full, meta ) {
						if(full.risk.policy.authStatus){
							if(full.risk.policy.authStatus ==="D")
								return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-docs='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.editRiskDocs(this);"><i class="fa fa-pencil-square-o"></button>';
							else
								return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-docs='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.editRiskDocs(this);" disabled><i class="fa fa-pencil-square-o"></button>';

						}
						else
							return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-docs='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.editRiskDocs(this);"><i class="fa fa-pencil-square-o"></button>';
					}

				},
				{
					"data": "rdId",
					"render": function ( data, type, full, meta ) {
						return '<button type="button" class="btn btn-success btn btn-info btn-xs" type="button" data-docs=' + encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.downloadRiskDoc(this);"><i class="fa fa-file-archive-o"></button>';

					}

				},
				{
					"data": "rdId",
					"render": function ( data, type, full, meta ) {
						if(full.risk.policy.authStatus){
							if(full.risk.policy.authStatus ==="D")
								return '<button type="button" class="btn btn-danger btn btn-info btn-xs" data-docs='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.deleteRiskDoc(this);"><i class="fa fa-trash-o"></button>';
							else
								return '<button type="button" class="btn btn-danger btn btn-info btn-xs" data-docs='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.deleteRiskDoc(this);" disabled><i class="fa fa-trash-o"></button>';

						}
						else
							return '<button type="button" class="btn btn-danger btn btn-info btn-xs" data-docs='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.deleteRiskDoc(this);"><i class="fa fa-trash-o"></button>';
					}

				},
			]
		}));
		return currTable;
	};

	var getUWPolicyReceipts = function(policyCode) {
		var url = SERVLET_CONTEXT + "/protected/life/policies/getpolicyReceipts/" + policyCode;
		var currTable = $('#receipts_tbl').DataTable(UTILITIES.extendsOpts({
			"ajaxUrl": url,
			"columns": [
				{
					"data": "receiptNo",
					"render": function (data, type, full, meta) {

						return (full.receiptNo);
					}
				},
				{
					"data": "receiptDate",
					"render": function (data, type, full, meta) {

						return moment(full.receiptDate).format('DD/MM/YYYY');
					}
				},
				{
					"data": "dc",
					"render": function (data, type, full, meta) {

						return full.dc;;
					}
				},
				{
					"data": "receiptAmount",
					"render": function (data, type, full, meta) {
						return UTILITIES.currencyFormat(full.receiptAmount);
					}
				},
				{
					"data": "allocationAmount",
					"render": function (data, type, full, meta) {
						if(full.allocationAmount){
							return UTILITIES.currencyFormat(full.allocationAmount);
						}
					}

				},
				{
					"data": "balance",
					"render": function (data, type, full, meta) {
						if(full.balance){
							return UTILITIES.currencyFormat(full.balance);
						}
					}

				},
			]
		}));

		$('#receipts_tbl tbody').on( 'click', 'tr', function () {
			$(this).addClass('active').siblings().removeClass('active');
			var aData = currTable.rows('.active').data();
			if (aData[0] === undefined || aData[0] === null){

			}
			else{
				console.log(aData[0]);
				getCommissionAllocs(aData[0].receiptd);
			}
		} );
	}

	var getUWPolicyRisks = function(policyCode){
		console.log('policyCode ',policyCode);
		var url = SERVLET_CONTEXT+"/protected/life/policies/policyRisks/"+policyCode;
		var currTable = $('#risk_tbl').DataTable(UTILITIES.extendsOpts({
			"ajaxUrl":url,
			"columns": [
				{ "data": "insured",
					"render": function ( data, type, full, meta ) {

						return (full.insured.fname+" "+full.insured.otherNames);
					}
				},
				{ "data": "subclass",
					"render": function ( data, type, full, meta ) {

						return full.subclass.subDesc;
					}
				},
				{ "data": "covertype",
					"render": function ( data, type, full, meta ) {

						return full.covertype.covName;
					}
				},
				{ "data": "workingAge",
					"render": function ( data, type, full, meta ) {
						if (full.workingAge){
							return  full.workingAge;
						} else
						return "";
				}
				},
				{
					"data": "riskId",
					"render": function ( data, type, full, meta ) {
						if(full.policy.authStatus){
							if(full.policy.authStatus==="D")
								return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-policyrisks='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.editLifePolicyRisk(this);"><i class="fa fa-pencil-square-o"></button>';
							else
								return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-policyrisks='+encodeURI(JSON.stringify(full)) + '  disabled><i class="fa fa-pencil-square-o"></button>';

						}
						else
							return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-policyrisks='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.editLifePolicyRisk(this);"><i class="fa fa-pencil-square-o"></button>';
					}

				},
				{
					"data": "riskId",
					"render": function ( data, type, full, meta ) {
						if(full.policy.authStatus){
							if(full.policy.authStatus==="D")
								return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-policyrisks='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.deleteRisk(this);"><i class="fa fa-pencil-square-o"></button>';
							else
								return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-policyrisks='+encodeURI(JSON.stringify(full)) + '  disabled><i class="fa fa-pencil-square-o"></button>';

						}
						else
							return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-policyrisks='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.deleteRisk(this);"><i class="fa fa-pencil-square-o"></button>';

					}

				},
			]
		}) );

		$('#risk_tbl tbody').on( 'click', 'tr', function () {
			$(this).addClass('active').siblings().removeClass('active');
			var aData = currTable.rows('.active').data();
			if (aData[0] === undefined || aData[0] === null){

			}
			else{
				selRiskCode = aData[0].riskId;
				$("#risk-code-pk").val(selRiskCode);
				getRiskSections();
				//getRiskIntParties();
				getRiskDocs();
				getRiskCertificates();
				//getRiskSchedules();
				getClientDocs(aData[0].insured.tenId);
				$("#risk-det-id-pk").val(aData[0].binderDetails.detId);
				populateRiskSections(aData[0].binderDetails.detId);
				$("#cert-from-date").val(moment(aData[0].wefDate).format('DD/MM/YYYY'));
				$("#cert-wet-date").val(moment(aData[0].wetDate).format('DD/MM/YYYY'));
				console.log("binder="+$("#binder-id").val())
				if($("#pol-bind-age-appli").val() && $("#pol-bind-age-appli").val()==="Y"){
					$.ajax({
						type: 'GET',
						url:  'getLifeClientAge',
						dataType: 'json',
						data: {"clientId": aData[0].insured.tenId,"binCode" :$("#binder-id").val()},
						async: true,
						success: function(result) {
							$("#lifeassured-age").val(result);
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
			}
		} );
		return currTable;
	};

	var deleteRiskCerts = function(){
		var certs = JSON.parse(decodeURI($(button).data("certs")));
		bootbox.confirm("Are you sure want to delete "+certs['cert'].certLots.certTypes.certDesc+"?", function(result) {
			if(result){
				$('#myPleaseWait').modal({
					backdrop: 'static',
					keyboard: true
				});
				$.ajax({
					type: 'GET',
					url:  'deleteRiskCert/' + certs['pcId'],
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
						$('#cert_tbl').DataTable().ajax.reload();

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
	var editRiskCerts = function(button){
			var certs = JSON.parse(decodeURI($(button).data("certs")));
			$("#cert-pc-id").val(certs['pcId']);
			$("#risk-certtype-name").text(certs['cert'].certLots.certTypes.certDesc);
			$("#risk-cert-status").val(certs['status']);
			$("#risk-cert-from-date").val(moment(certs['certWef']).format('DD/MM/YYYY'));
			$("#risk-cert-wet-date").val(moment(certs['certWet']).format('DD/MM/YYYY'));
			$("#risk-canc-date").val(moment(certs['certWet']).format('DD/MM/YYYY'));
			if(certs['status']){
				if(certs['status']==="A"){
					$(".cert-cancellation").hide();
				}
				else if(certs['status']==="C"){
					$(".cert-cancellation").show();
				}
				else{
					$(".cert-cancellation").hide();
				}

			}
			$('#editRiskCertModal').modal({
				backdrop: 'static',
				keyboard: true
			});
	};

	var downloadRiskDoc = function(button){
		var docs = JSON.parse(decodeURI($(button).data("docs")));
		window.open(SERVLET_CONTEXT+"/protected/uw/policies/riskdocument/"+docs['rdId'],
			'_blank' // <- This is what makes it open in a new window.
		);
	}


	var deleteRiskDoc= function(button){
		var docs = JSON.parse(decodeURI($(button).data("docs")));
		bootbox.confirm("Are you sure want to delete "+docs['reqdDocs'].requiredDoc.reqDesc+"?", function(result) {
			if(result){
				$('#myPleaseWait').modal({
					backdrop: 'static',
					keyboard: true
				});
				$.ajax({
					type: 'GET',
					url:  'deleteRiskDoc/' + docs['rdId'],
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
						$('#risk_docs_tbl').DataTable().ajax.reload();
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

	var editRiskDocs= function(button){
		var docs = JSON.parse(decodeURI($(button).data("docs")));
		$("#risk-doc-name").text(docs["reqdDocs"].requiredDoc.reqDesc);
		$("#risk-upload-name").text(docs['uploadedFileName']);
		$("#risk-doc-id").val(docs['rdId']);
		$('#riskdocModal').modal({
			backdrop: 'static',
			keyboard: true
		});
	}

	var uploadRiskDocument= function(){
		var $form = $("#risk-doc-form");
		var validator = $form.validate();
		$('form#risk-doc-form')
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
				data.append( 'file', $( '#avatar' )[0].files[0] );
				$.ajax( {
					url: 'uploadRequiredDocs',
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
						$('#riskdocModal').modal('hide');
						var $el = $('#avatar');
						$el.wrap('<form>').closest('form').get(0).reset();
						$el.unwrap();
						$('#risk_docs_tbl').DataTable().ajax.reload();

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
	}

	var editRiskSection= function(button){
		var section = JSON.parse(decodeURI($(button).data("risksections")));
		console.log(section);
		$("#sect-code-pk").val(section['sectId']);
		$("#sect-limit-amt").val(section['amount']);
		$("#sect-rate").val(section['rate']);
		$("#sect-free-limit").val(section['freeLimit']);
		$("#sect-div-fact").val(section['divFactor']);
		$("#sect-multi-rate").val(section['multiRate']);
		//$("#chk-compute").prop("checked", section["compute"]);
		$("#risk-sect-id").val(section['section'].id);
		$("#risk-sect-name").val(section['section'].desc);
		$("#sect-prem-id-pk").val(section['premRates'].id);
		if(section['risk']){
			$("#risk-sect-code-pk").val(section['risk'].riskId);
		}
		else{
			$("#risk-sect-code-pk").val(section['riskId']);
		}
		populateRiskSections($("#risk-det-id-pk").val());
		$('#sect-limit-amt,#sect-free-limit').number( true, 2 );
		$("#risk-sect-frm").select2("readonly", true);
		if($("#pol-bin-type").val()==="B"){
			$("#sect-rate").prop("readonly", true);
			$("#sect-free-limit").prop("readonly", true);
			$("#sect-div-fact").attr("style", "pointer-events: none;");
		}
		else{
			$("#sect-rate").prop("readonly", false);
			$("#sect-free-limit").prop("readonly", false);
			//$("#sect-div-fact").prop('disabled', false);
		}

		$('#sectModal').modal({
			backdrop: 'static',
			keyboard: true
		});
	}

	var deleteRiskSection= function(button){
		var section = JSON.parse(decodeURI($(button).data("risksections")));
		bootbox.confirm("Are you sure want to delete "+section['section'].desc+"?", function(result) {
			if(result){
				$('#myPleaseWait').modal({
					backdrop: 'static',
					keyboard: true
				});
				$.ajax({
					type: 'GET',
					url:  'deleteRiskSection/' + section['sectId'],
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
						$('#section_tbl').DataTable().ajax.reload();
						populatePolicyDetails();
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


	var populateRiskSections= function(detCode){
		if($("#risk-sect-frm").filter("div").html() != undefined)
		{
			Select2Builder.initAjaxSelect2({
				containerId : "risk-sect-frm",
				sort : 'desc',
				change: function(e, a, v){
					//$("#pm-id").val(e.added.pmId);
				},
				formatResult : function(a)
				{
					return a.desc;
				},
				formatSelection : function(a)
				{
					return a.desc;
				},
				initSelection: function (element, callback) {
					var code  = $('#risk-sect-id').val();
					var name = $("#risk-sect-name").val();
					var data = {desc:name,id:code};
					callback(data);
				},
				id: "id",
				width:"250px",
				params: {detId: detCode},
				placeholder:"Select Section"

			});
		}
	}

	var saveRiskSections= function(){
		var $classForm = $('#risk-sect-form');
		var validator = $classForm.validate();
		$('#saveRiskSection').click(function(){
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
			var url = "saveRiskSections";
			var request = $.post(url, data );
			request.success(function(){
				$('#myPleaseWait').modal('hide');
				new PNotify({
					title: 'Success',
					text: 'Record Created/Updated Successfully',
					type: 'success',
					styling: 'bootstrap3'
				});
				$('#section_tbl').DataTable().ajax.reload();
				populatePolicyDetails();
				validator.resetForm();
				$('#risk-sect-form').find("input[type=text],input[type=number],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden], textarea,select").val("");
				$('#sectModal').modal('hide');
			});

			request.error(function(jqXHR, textStatus, errorThrown){
				$('#myPleaseWait').modal('hide');
				bootbox.alert(jqXHR.responseText);
			});
			request.always(function(){
				$btn.button('reset');
			});
		});
	}


	var createPolicyClauses= function(){
		var url = "policyClauses";
		var currTable = $('#polclausesList').DataTable( UTILITIES.extendsOpts({
			"ajaxUrl":url,
			"columns": [
				{ "data": "clauHeading" },
				{ "data": "clause",
					"render": function ( data, type, full, meta ) {
						if(full.clause.clause.clauseType){
							if(full.clause.clause.clauseType ==='E') return "Excess";
							else if(full.clause.clause.clauseType ==='L') return "Limits";
							else if(full.clause.clause.clauseType ==='C') return "Clause";
							else if(full.clause.clause.clauseType ==='X') return "Exclusion";
							else return "";
						}
						else{
							return "";
						}
					}
				},
				{ "data": "editable" },
				{ "data": "clauWording" },
				{
					"data": "polClauseId",
					"render": function ( data, type, full, meta ) {
						if(full.policy.authStatus){
							if(full.policy.authStatus==="D"){
								if(full.editable) {
									return '<button type="button" class="btn btn-success btn btn-info btn-xs" type="button" data-clauses=' + encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.editPolicyClause(this);"><i class="fa fa-pencil-square-o"></button>';
								}else {
									return '<button type="button" class="btn btn-success btn btn-info btn-xs" type="button" data-clauses=' + encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.editPolicyClause(this);" disabled><i class="fa fa-pencil-square-o"></button>';
								}
							}
							else
								return '<button type="button" class="btn btn-success btn btn-info btn-xs" type="button" data-clauses='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.editPolicyClause(this);" disabled><i class="fa fa-pencil-square-o"></button>';

						}
						else
							return '<button type="button" class="btn btn-success btn btn-info btn-xs" type="button" data-clauses='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.editPolicyClause(this);" disabled><i class="fa fa-pencil-square-o"></button>';

					}

				},
				{
					"data": "polClauseId",
					"render": function ( data, type, full, meta ) {
						if(full.policy.authStatus) {
							if (full.policy.authStatus === "D" && $("#pol-bin-type").val() === "M") {

								return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-clauses=' + encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.deletePolicyClause(this);"><i class="fa fa-trash-o"></button>';


							}
							else
								return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-clauses='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.deletePolicyClause(this);" disabled><i class="fa fa-trash-o"></button>';

						}
						else
							return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-clauses='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.deletePolicyClause(this);" disabled><i class="fa fa-trash-o"></button>';

					}

				},
			]
		}));
		return currTable;
	}

	var editPolicyClause= function(button){
		var clause = JSON.parse(decodeURI($(button).data("clauses")));
		if(!clause['editable']){
			bootbox.alert("The Selected Clause is not Editable");
			return;
		}
		$("#pol-clause-code").val(clause['polClauseId']);
		$("#sub-clause-code").val(clause['clause'].clauId);
		$("#pol-new-clause").val(clause['newClause']);
		$("#sub-clau-id").val(clause['clause'].clause.clauShtDesc);
		$("#sub-clause-name").val(clause['clauHeading']);
		$("#chk-cl-editable").prop("checked", clause['editable']);
		$("#sub-cla-wording").val(clause['clauWording']);
		$("#clause-pol-code").val(clause['policy'].policyId)
		$('#clauseModal').modal({
			backdrop: 'static',
			keyboard: true
		})
	}


	var createPolClauses= function(){
		var $classForm = $('#pol-clause-form');
		var validator = $classForm.validate();
		$('#savepolClauseBtn').click(function(){
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
			var url = "createPolicyClause";
			var request = $.post(url, data );
			request.success(function(){
				$('#myPleaseWait').modal('hide');
				new PNotify({
					title: 'Success',
					text: 'Record created/updated Successfully',
					type: 'success',
					styling: 'bootstrap3'
				});
				$('#polclausesList').DataTable().ajax.reload();
				validator.resetForm();
				$('#pol-clause-form').find("input[type=text],input[type=number],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden], textarea,select").val("");
				$("#chk-cl-editable").prop("checked", false);
				$('#clauseModal').modal('hide');
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

	var deletePolicyClause= function(button){
		var stack_bottomleft = {"dir1": "up", "dir2": "left", "firstpos1": 25, "firstpos2": 25};
		var clause = JSON.parse(decodeURI($(button).data("clauses")));
		bootbox.confirm("Are you sure want to delete "+clause['clauHeading']+"?", function(result) {
			if(result){
				$('#myPleaseWait').modal({
					backdrop: 'static',
					keyboard: true
				});
				$.ajax({
					type: 'GET',
					url:  'deletePolClause/' + clause['polClauseId'],
					dataType: 'json',
					async: true,
					success: function(result) {
						$('#myPleaseWait').modal('hide');
						new PNotify({
							title: 'Success',
							text: 'Record Deleted Successfully',
							type: 'success',
							styling: 'bootstrap3',
							addclass: 'stack-bottomright',
							stack: stack_bottomleft
						});
						$('#polclausesList').DataTable().ajax.reload();
						populatePolicyDetails();
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

		});
	}


	var createPolicyChecks= function(){
		var url = "policyChecks";
		var currTable = $('#polChecksList').DataTable(UTILITIES.extendsOpts({
			"ajaxUrl":url,
			"columns": [

				{ "data": "tcNo",
					"render": function ( data, type, full, meta ) {
						return full.checks.checkName;
					}
				},
				{ "data": "authorised",
					"render": function ( data, type, full, meta ) {
						if(full.authorised)
							return full.authorised;
						else return "No";
					}
				},
				{ "data": "authBy",
					"render": function ( data, type, full, meta ) {
						if(full.authBy)
							return full.authBy.username;
						else return "";
					}
				},
				{ "data": "authDate",
					"render": function ( data, type, full, meta ) {
						if(full.authDate)
							return moment(full.authDate).format('DD/MM/YYYY');
						else return "";
					}
				},
				{
					"data": "checks",
					"render": function ( data, type, full, meta ) {
						if(full.authorised && full.authorised==="Y"){

						}else
							return '<button type="button" class="btn btn-info btn btn-info btn-xs" data-checks='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.authChecks(this);">Approve</button>';


					}

				},
			]
		}) );
		return currTable;
	}

	var authChecks= function(button){
		var stack_bottomleft = {"dir1": "up", "dir2": "left", "firstpos1": 25, "firstpos2": 25};
		var checks = JSON.parse(decodeURI($(button).data("checks")));
		bootbox.confirm("Are you sure want to Approve?", function(result) {
			if(result){
				$('#myPleaseWait').modal({
					backdrop: 'static',
					keyboard: true
				});
				$.ajax({
					type: 'GET',
					url:  'authChecks/' + checks["tcNo"],
					dataType: 'json',
					async: true,
					success: function(result) {
						$('#myPleaseWait').modal('hide');
						new PNotify({
							title: 'Success',
							text: 'Check Approved Successfully...',
							type: 'success',
							styling: 'bootstrap3',
							addclass: 'stack-bottomright',
							stack: stack_bottomleft
						});
						$('#polChecksList').DataTable().ajax.reload();
						populatePolicyDetails();
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
		});
	}


	var createPolicyTaxes= function(){
		var url = "policyTaxes";
		var currTable = $('#polTaxesList').DataTable( UTILITIES.extendsOpts({
			"ajaxUrl":url,
			"columns": [
				{ "data": "revenueItems",
					"render": function ( data, type, full, meta ) {
						return UTILITIES.getRevDesc(full.revenueItems.item);
					}
				},
				{ "data": "taxRate" },
				{ "data": "divFactor" },
				{ "data": "rateType" },
				{ "data": "taxAmount",
					"render": function ( data, type, full, meta ) {

						return UTILITIES.currencyFormat(full.taxAmount);
					}
				},
				{ "data": "taxLevel",
					"render": function ( data, type, full, meta ) {
						if(full.taxLevel){
							if(full.taxLevel==="R")
								return "Risk";
							else if(full.taxLevel==="P")
								return "Policy";
						}
						else
							return "Policy";

					}
				},
				{
					"data": "polTaxId",
					"render": function ( data, type, full, meta ) {
						if(full.policy.authStatus){
							if(full.policy.authStatus==="D")
								return '<button type="button" class="btn btn-success btn btn-info btn-xs" type="button" data-poltaxes='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.editPolTaxes(this);"><i class="fa fa-pencil-square-o"></button>';
							else
								return '<button type="button" class="btn btn-success btn btn-info btn-xs" type="button" data-poltaxes='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.editPolTaxes(this);" disabled><i class="fa fa-pencil-square-o"></button>';

						}
						else
							return '<button type="button" class="btn btn-success btn btn-info btn-xs" type="button" data-poltaxes='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.editPolTaxes(this);" disabled><i class="fa fa-pencil-square-o"></button>';

					}

				},
				{
					"data": "polTaxId",
					"render": function ( data, type, full, meta ) {
						if(full.policy.authStatus){
							if(full.policy.authStatus==="D")
								return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-poltaxes='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.deletePolTaxes(this);"><i class="fa fa-trash-o"></button>';
							else
								return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-poltaxes='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.deletePolTaxes(this);" disabled><i class="fa fa-trash-o"></button>';

						}
						else
							return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-poltaxes='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.deletePolTaxes(this);" disabled><i class="fa fa-trash-o"></button>';


					}

				},
			]
		}));
		return currTable;
	}

	var editPolTaxes= function(button){
		var tax = JSON.parse(decodeURI($(button).data("poltaxes")));
		$("#tax-code").val(tax["polTaxId"]);
		$("#tax-pol-code").val(tax["policy"].policyId);
		$("#tax-rev-code").val(tax["revenueItems"].revenueId);
		$("#tax-trans-code").text(UTILITIES.getRevDesc(tax["revenueItems"].item));
		$("#pol-tax-sub-code").val(tax["subclass"].subId);
		$("#tax-rate-type").val(tax["rateType"]);
		$("#tax-rate").val(tax["taxRate"]);
		$("#tax-div-fact").val(tax["divFactor"]);
		if(tax["taxLevel"])
			$("#tax-level").val(tax["taxLevel"]);
		else
			$("#tax-level").val("P");
		$('#taxRatesModal').modal({
			backdrop: 'static',
			keyboard: true
		})
	}

	var updatePolicyTaxes= function(){
		var $classForm = $('#pol-taxes-form');
		var validator = $classForm.validate();
		$('#savepoltaxesBtn').click(function(){
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
			var url = "createPolicyTax";
			var request = $.post(url, data );
			request.success(function(){
				$('#myPleaseWait').modal('hide');
				new PNotify({
					title: 'Success',
					text: 'Record created/updated Successfully',
					type: 'success',
					styling: 'bootstrap3'
				});
				$('#polTaxesList').DataTable().ajax.reload();
				populatePolicyDetails();
				validator.resetForm();
				$('#pol-taxes-form').find("input[type=text],input[type=number],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden], textarea,select").val("");
				$('#taxRatesModal').modal('hide');
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

	var deletePolTaxes= function(button){
		var stack_bottomleft = {"dir1": "up", "dir2": "left", "firstpos1": 25, "firstpos2": 25};
		var tax = JSON.parse(decodeURI($(button).data("poltaxes")));
		bootbox.confirm("Are you sure want to delete "+UTILITIES.getRevDesc(tax["revenueItems"].item)+"?", function(result) {
			if(result){
				$('#myPleaseWait').modal({
					backdrop: 'static',
					keyboard: true
				});
				$.ajax({
					type: 'GET',
					url:  'deletePolTaxes/' + tax["polTaxId"],
					dataType: 'json',
					async: true,
					success: function(result) {
						$('#myPleaseWait').modal('hide');
						new PNotify({
							title: 'Success',
							text: 'Record Deleted Successfully',
							type: 'success',
							styling: 'bootstrap3',
							addclass: 'stack-bottomright',
							stack: stack_bottomleft
						});
						$('#polTaxesList').DataTable().ajax.reload();
						populatePolicyDetails();
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
		});
	}



	var newRisk= function(){
		$("#btn-add-risk").on('click', function(){
			$("#insured-code").val("");
			$("#insured-name").val("");
			$("#insured-other-name").val("");
			$('#lifeassured-frm').select2('val', null);

			populateInsuredLov();
			$('#subclass-frm').select2('val', null);
			populateSubclassLov();
			$('#covertypes-frm').select2('val', null);
			populateCoverTypesLov();
			$('#risk-form').find("input[type=text],input[type=number],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden], textarea,select").val("");
			$("#risk-binder-code").val($("#risk-bind-code").val());
			if($("#pol-buss-type").val()==="S"){
				$("#prorated-full").val("S");
			}
			else{
				$("#prorated-full").val("P");
			}
			$("#risk-form").show();
			$("#risk-div").hide();
			$("#sect-div").hide();
			$("#prem-rates-div").show();
			$("#btn-save-risk").show();
			$("#btn-save-cancel").show();

			$("#myTab #show-taxes,#show-clauses").hide();
			$("#binder-id").val($("#risk-bind-code").val());
			populateSubclassLov();
			var fromDate = $("#from-date").val();
			var toDate = $("#wet-date").val();
			$("#risk-wef-date").val(fromDate);
			$("#risk-wet-date").val(toDate);
			$("#section_form_tbl tbody").each(function(){
				$(this).remove();
			});
			$("#btn-add-new-section").hide();
		});

		$("#btn-save-cancel").on('click', function(){
			populatePolicyDetails();
		})
	}

	var deleteRisk= function(button){
		var risks = JSON.parse(decodeURI($(button).data("policyrisks")));
		bootbox.confirm("Are you sure want to delete "+risks['riskShtDesc']+"?", function(result) {
			if(result){
				$('#myPleaseWait').modal({
					backdrop: 'static',
					keyboard: true
				});
				$.ajax({
					type: 'GET',
					url:  'deleteRisk/' + risks['riskId'],
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
						$('#risk_tbl').DataTable().ajax.reload();
						getRiskSections(-2000);
						//getRiskIntParties(-2000);
						getRiskDocs(-2000);
						$('#section_tbl').DataTable().ajax.reload();
						getRiskCertificates(-2000);
						//getRiskSchedules(-2000);
						$("#risk-sched_tbl").DataTable().ajax.reload();
						$('#cert_tbl').DataTable().ajax.reload();
						populatePolicyDetails();
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

	var editLifePolicyRisk= function(button){
		var risks = JSON.parse(decodeURI($(button).data("policyrisks")));

		$("#risk-binder-code").val($("#risk-bind-code").val());
		$("#risk-id").val(risks["riskShtDesc"]);
		$("#risk-desc").val(risks["riskDesc"]);
		$("#risk-wef-date").val(moment(risks["wefDate"]).format('DD/MM/YYYY'));
		$("#risk-wet-date").val(moment(risks["wetDate"]).format('DD/MM/YYYY'));
		$("#prorated-full").val(risks["prorata"]);
		$("#comm-rate").val(risks["commRate"]);
		$("#overrid-prem").val(risks["butchargePrem"]);
		if(risks["autogenCert"]){
			if(risks["autogenCert"]==="Y"){
				$("#chk-autogen").prop("checked", true);
			}
			else{
				$("#chk-autogen").prop("checked", false);
			}
		}else
			$("#chk-autogen").prop("checked", false);
		$("#lifeassured-name").val(risks["insured"].fname);
		$("#lifeassured-code").val(risks["insured"].tenId);
		$("#lifeassured-other-name").val(risks["insured"].otherNames);

		populateInsuredLov();
		$("#risk-sub-code").val(risks["subclass"].subId);
		$("#sub-name").val(risks["subclass"].subDesc);
		populateSubclassLov();

		$("#risk-cov-code").val(risks["covertype"].covId);
		$("#cover-name").val(risks["covertype"].covName);
		populateCoverTypesLov();
		$("#binder-det-id").val(risks["binderDetails"].detId);
		$("#risk-code-pk").val(risks["riskId"]);
		$("#risk-trans-type").val(risks["transType"]);
		$("#risk-ident-code").val(risks["riskIdentifier"]);
		$("#risk-form").show();
		$("#risk-div").hide();
		$("#btn-save-risk").show();
		$("#btn-save-cancel").show();
		$("#myTab #show-taxes,#show-clauses").hide();
	}

	function getNewPremItems(sectdesc){
		$('#myPleaseWait').modal({
			backdrop: 'static',
			keyboard: true
		});
		if($("#pol-bind-age-appli").val() && $("#pol-bind-age-appli").val()==="Y"){
			$.ajax({
				type: 'GET',
				url: 'getNewClientPremiumItems',
				dataType: 'json',
				data: {"detId": $("#risk-det-id-pk").val(), "riskId": $("#risk-code-pk").val(), "secName": sectdesc,"age":$("#lifeassured-age").val()},
				async: true,
				success: function (result) {
					$('#myPleaseWait').modal('hide');
					$("#new_prem_items_form tbody").each(function () {
						$(this).remove();
					});
					for (var res in result) {
						var markup = "<tr><td><input type='checkbox' class='section-check' id='" + result[res].id + "'></td><td><input type='hidden' class='section form-control' value='" + result[res].section.id +
							"'><input type='hidden' class='ratesApplicable form-control' value='" + result[res].ratesApplicable + "'><input type='hidden' class='premId form-control' value='" + result[res].id + "'>" + result[res].section.desc + "</td><td>" + ' <input type="text" class="amount form-control" required>' +
							"</td><td><input type='text' class='rate form-control' value='" + result[res].rate + "'></td><td><input type='text' class='divFactor form-control'  value='" + result[res].divFactor + "'></td></td><td>" +
							"<input type='text' class='freeLimit form-control'  value='" + result[res].freeLimit + "'></td></tr>";
						if($("#pol-bin-type").val()==="B"){
							markup = "<tr><td><input type='checkbox' class='section-check' id='" + result[res].id + "'></td><td><input type='hidden' class='section form-control' value='" + result[res].section.id +
								"'><input type='hidden' class='ratesApplicable form-control' value='" + result[res].ratesApplicable + "'><input type='hidden' class='premId form-control' value='" + result[res].id + "'>" + result[res].section.desc + "</td><td>" + ' <input type="text" class="amount form-control">' +
								"</td><td><input type='text' class='rate form-control' readonly value='" + result[res].rate + "'></td><td><input type='text' class='divFactor form-control'  readonly value='" + result[res].divFactor + "'></td></td><td>" +
								"<input type='text' class='freeLimit form-control' readonly  value='" + result[res].freeLimit + "'></td></tr>";
						}
						else {
							if (result[res].ratesApplicable && result[res].ratesApplicable === "Y") {
								markup = "<tr><td><input type='checkbox' class='section-check' id='" + result[res].id + "'></td><td><input type='hidden' class='section form-control' value='" + result[res].section.id +
									"'><input type='hidden' class='ratesApplicable form-control' value='" + result[res].ratesApplicable + "'><input type='hidden' class='premId form-control' value='" + result[res].id + "'>" + result[res].section.desc + "</td><td>" + ' <input type="text" class="amount form-control" value="' + $("#lifeassured-age").val() + '" readonly>' +
									"</td><td><input type='text' class='rate form-control' readonly value='" + result[res].rate + "'></td><td><input type='text' class='divFactor form-control'  readonly value='" + result[res].divFactor + "'></td></td><td>" +
									"<input type='text' class='freeLimit form-control' readonly  value='" + result[res].freeLimit + "'></td></tr>";
							}
							else if (result[res].section && result[res].section.type === "RD") {
								markup = "<tr><td><input type='checkbox' class='section-check' id='" + result[res].id + "'></td><td><input type='hidden' class='section form-control' value='" + result[res].section.id +
									"'><input type='hidden' class='ratesApplicable form-control' value='" + result[res].ratesApplicable + "'><input type='hidden' class='premId form-control' value='" + result[res].id + "'>" + result[res].section.desc + "</td><td>" + ' <input type="text" class="amount form-control" readonly>' +
									"</td><td><input type='text' class='rate form-control'  value='" + result[res].rate + "'></td><td><input type='text' class='divFactor form-control'   value='" + result[res].divFactor + "'></td></td><td>" +
									"<input type='text' class='freeLimit form-control'   value='" + result[res].freeLimit + "'></td></tr>";
							}
						}
						$("#new_prem_items_form").append(markup);
					}

					$("#new_prem_items_form tr").find("input[type=text]").number(true, 2);
				},
				error: function (jqXHR, textStatus, errorThrown) {
					$('#myPleaseWait').modal('hide');
				}
			});
		}
		else {
			$.ajax({
				type: 'GET',
				url: 'getNewPremiumItems',
				dataType: 'json',
				data: {"detId": $("#risk-det-id-pk").val(), "riskId": $("#risk-code-pk").val(), "secName": sectdesc},
				async: true,
				success: function (result) {
					$('#myPleaseWait').modal('hide');
					$("#new_prem_items_form tbody").each(function () {
						$(this).remove();
					});
					for (var res in result) {
						var markup = "<tr><td><input type='checkbox' class='section-check' id='" + result[res].id + "'></td><td><input type='hidden' class='section form-control' value='" + result[res].section.id +
							"'><input type='hidden' class='ratesApplicable form-control' value='" + result[res].ratesApplicable + "'><input type='hidden' class='premId form-control' value='" + result[res].id + "'>" + result[res].section.desc + "</td><td>" + ' <input type="text" class="amount form-control" required>' +
							"</td><td><input type='text' class='rate form-control' value='" + result[res].rate + "'></td><td><input type='text' class='divFactor form-control'  value='" + result[res].divFactor + "'></td></td><td>" +
							"<input type='text' class='freeLimit form-control'  value='" + result[res].freeLimit + "'></td></tr>";
						if($("#pol-bin-type").val()==="B"){
							markup = "<tr><td><input type='checkbox' class='section-check' id='" + result[res].id + "'></td><td><input type='hidden' class='section form-control' value='" + result[res].section.id +
								"'><input type='hidden' class='ratesApplicable form-control' value='" + result[res].ratesApplicable + "'><input type='hidden' class='premId form-control' value='" + result[res].id + "'>" + result[res].section.desc + "</td><td>" + ' <input type="text" class="amount form-control" required>' +
								"</td><td><input type='text' class='rate form-control' readonly value='" + result[res].rate + "'></td><td><input type='text' class='divFactor form-control'  readonly value='" + result[res].divFactor + "'></td></td><td>" +
								"<input type='text' class='freeLimit form-control' readonly  value='" + result[res].freeLimit + "'></td></tr>";
						}
						else {
							if (result[res].ratesApplicable && result[res].ratesApplicable === "Y") {
								markup = "<tr><td><input type='checkbox' class='section-check' id='" + result[res].id + "'></td><td><input type='hidden' class='section form-control' value='" + result[res].section.id +
									"'><input type='hidden' class='ratesApplicable form-control' value='" + result[res].ratesApplicable + "'><input type='hidden' class='premId form-control' value='" + result[res].id + "'>" + result[res].section.desc + "</td><td>" + ' <input type="text" class="amount form-control" required>' +
									"</td><td><input type='text' class='rate form-control' readonly value='" + result[res].rate + "'></td><td><input type='text' class='divFactor form-control'  readonly value='" + result[res].divFactor + "'></td></td><td>" +
									"<input type='text' class='freeLimit form-control' readonly  value='" + result[res].freeLimit + "'></td></tr>";
							}
							else if (result[res].section && result[res].section.type === "RD") {
								markup = "<tr><td><input type='checkbox' class='section-check' id='" + result[res].id + "'></td><td><input type='hidden' class='section form-control' value='" + result[res].section.id +
									"'><input type='hidden' class='ratesApplicable form-control' value='" + result[res].ratesApplicable + "'><input type='hidden' class='premId form-control' value='" + result[res].id + "'>" + result[res].section.desc + "</td><td>" + ' <input type="text" class="amount form-control" readonly>' +
									"</td><td><input type='text' class='rate form-control'  value='" + result[res].rate + "'></td><td><input type='text' class='divFactor form-control'   value='" + result[res].divFactor + "'></td></td><td>" +
									"<input type='text' class='freeLimit form-control'   value='" + result[res].freeLimit + "'></td></tr>";
							}
						}
						$("#new_prem_items_form").append(markup);
					}

					$("#new_prem_items_form tr").find("input[type=text]").number(true, 2);
				},
				error: function (jqXHR, textStatus, errorThrown) {
					$('#myPleaseWait').modal('hide');
				}
			});
		}

	}





	var getNewPremItemsModal= function(){

		$("#btn-add-new-section").click(function(){
			if($("#risk-code-pk").val() != ''){
				$("#prem-item-risk-id").val($("#risk-code-pk").val());
				getNewPremItems("");
				$('#premItemsModal').modal({
					backdrop: 'static',
					keyboard: true
				});
			}
			else{
				bootbox.alert("Select Risk to add new Premium Item")
			}
		});

		$("#savenewPremItem").on('click', function(){
			var items = getNewCreatePremItems();
			if(items.length==0){
				bootbox.alert("No Section Selected to add..");
				return;
			}

			var $currForm = $('#new-prem-items-form');
			var currValidator = $currForm.validate();
			if (!$currForm.valid()) {
				return;
			}
			var data = {};
			$currForm.serializeArray().map(function(x){data[x.name] = x.value;});
			var url = "createPremiumItems";

			data.sections = items;
			$('#myPleaseWait').modal({
				backdrop: 'static',
				keyboard: true
			});
			$.ajax(
				{
					url:url,
					type: "POST",
					data: JSON.stringify(data),
					success: function(s){
						$('#myPleaseWait').modal('hide');
						new PNotify({
							title: 'Success',
							text: 'Selected sections added Successfully',
							type: 'success',
							styling: 'bootstrap3'
						});
						$('#section_tbl').DataTable().ajax.reload();
						populatePolicyDetails();
						$('#premItemsModal').modal('hide');
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


	var getNewCreatePremItems= function(){
		var arr = [];
		$("#new_prem_items_form tr").each(function(row,tr){
			var checked   = $(this).find('.section-check').eq(0).is(":checked");
			var section   = $(this).find('.section').eq(0).val();
			var premId   = $(this).find('.premId').eq(0).val();
			var ratesApplicable =$(this).find('.ratesApplicable').eq(0).val();
			var rate   = $(this).find('.rate').eq(0).val();
			var divFactor   = $(this).find('.divFactor').eq(0).val();
			var freeLimit   = $(this).find('.freeLimit').eq(0).val();
			var amount = $(this).find('.amount').eq(0).val();
			var multiplierRate= $(this).find('.multiplierRate').eq(0).val();
			if(checked){
				arr.push({
					section: section,
					premId: premId,
					rate:rate,
					divFactor:divFactor,
					freeLimit:freeLimit,
					ratesApplicable:ratesApplicable,
					amount: amount,
					multiplierRate:multiplierRate
				});
			}

		});

		return arr;
	}

	var getNewClausesModal= function(){
		$("#btn-add-new-clause").click(function(){
			if($("#policy-id").val() != ''){
				$("#clause-pol-id").val($("#policy-id").val());
				getNewClauses();
				$('#newclausesModal').modal({
					backdrop: 'static',
					keyboard: true
				});
			}
			else{
				bootbox.alert("Policy Details are not available to add A clause")
			}
		});

		$("#savenewClause").on('click', function(){
			var items = getCreateNewClauses();
			if(items.length==0){
				bootbox.alert("No Clauses Selected to add..");
				return;
			}



			var $currForm = $('#new-clause-form');
			var currValidator = $currForm.validate();
			if (!$currForm.valid()) {
				return;
			}
			var data = {};
			$currForm.serializeArray().map(function(x){data[x.name] = x.value;});
			var url = "createNewClause";

			data.clauses = items;
			$('#myPleaseWait').modal({
				backdrop: 'static',
				keyboard: true
			});
			$.ajax(
				{
					url:url,
					type: "POST",
					data: JSON.stringify(data),
					success: function(s){
						$('#myPleaseWait').modal('hide');
						new PNotify({
							title: 'Success',
							text: 'Selected Clauses added Successfully',
							type: 'success',
							styling: 'bootstrap3'
						});
						$('#polclausesList').DataTable().ajax.reload();
						populatePolicyDetails();
						$('#newclausesModal').modal('hide');
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

	var getNewTaxesModal= function() {
		$("#btn-add-new-tax").click(function () {
			if ($("#policy-id").val() != '') {
				$("#clause-pol-id").val($("#policy-id").val());
				getNewTaxes();
				$('#newtaxesModal').modal({
					backdrop: 'static',
					keyboard: true
				});
			}
			else {
				bootbox.alert("Policy Details are not available to add A Tax")
			}
		});
		$("#savenewTaxes").click(function () {
			var items = getCreateNewTaxes();
			if(items.length==0){
				bootbox.alert("No Tax Selected")
				return;
			}
			var $currForm = $('#new-taxes-form');
			var currValidator = $currForm.validate();
			if (!$currForm.valid()) {
				return;
			}
			var data = {};
			$currForm.serializeArray().map(function(x){data[x.name] = x.value;});
			var url = "createNewTax";

			data.taxes = items;
			$('#myPleaseWait').modal({
				backdrop: 'static',
				keyboard: true
			});
			$.ajax(
				{
					url:url,
					type: "POST",
					data: JSON.stringify(data),
					success: function(s){
						$('#myPleaseWait').modal('hide');
						new PNotify({
							title: 'Success',
							text: 'Selected Clauses added Successfully',
							type: 'success',
							styling: 'bootstrap3'
						});
						$('#polTaxesList').DataTable().ajax.reload();
						populatePolicyDetails();
						$('#newtaxesModal').modal('hide');
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

	var getRateType= function(type){
		if(type){
			if(type==="A") return "Amount";
			else if(type==="P") return "Percentage";
			else if(type==="M") return "Per Milli";
		}
		else {
			return "Amount";
		}
	}

	var getCreateNewTaxes= function(){
		var arr = [];
		$("#new_taxes_tbl tr").each(function(row,tr){
			var checked   = $(this).find('.tax-check').eq(0).is(":checked");
			var taxid   = $(this).find('.tax-id').eq(0).val();
			if(checked){
				arr.push(taxid);
			}

		});

		return arr;
	}

	var getNewTaxes= function(){

		$('#myPleaseWait').modal({
			backdrop: 'static',
			keyboard: true
		});
		$.ajax({
			type: 'GET',
			url:  'getNewTaxes',
			dataType: 'json',
			async: true,
			success: function(result) {
				$('#myPleaseWait').modal('hide');
				$("#new_taxes_tbl tbody").each(function(){
					$(this).remove();
				});
				for(var res in result){
					var markup = "<tr><td><input type='checkbox' class='tax-check'><input type='hidden' class='tax-id form-control' value='"+result[res].polTaxId+
						"'></td><td>"
						+ UTILITIES.getRevDesc(result[res].revenueItems.item) + "</td><td>"
						+result[res].taxRate +"</td><td>"
						+getRateType(result[res].rateType) +"</td></tr>";
					$("#new_taxes_tbl").append(markup);
				}

			},
			error: function(jqXHR, textStatus, errorThrown) {
				$('#myPleaseWait').modal('hide');
			}
		});

	}

	var getNewClauses= function(){
		$('#myPleaseWait').modal({
			backdrop: 'static',
			keyboard: true
		});
		$.ajax({
			type: 'GET',
			url:  'getNewClauses',
			dataType: 'json',
			async: true,
			success: function(result) {
				$('#myPleaseWait').modal('hide');
				$("#new_clause_tbl tbody").each(function(){
					$(this).remove();
				});
				for(var res in result){
					var markup = "<tr><td><input type='checkbox' class='clause-check'><input type='hidden' class='clause-id form-control' value='"+result[res].clause.clauId+
						"'></td><td>"
						+ result[res].clauHeading + "</td></tr>";
					$("#new_clause_tbl").append(markup);
				}

			},
			error: function(jqXHR, textStatus, errorThrown) {
				$('#myPleaseWait').modal('hide');
			}
		});

	}


	var getCreateNewClauses= function(){
		var arr = [];
		$("#new_clause_tbl tr").each(function(row,tr){
			var checked   = $(this).find('.clause-check').eq(0).is(":checked");
			var clauseId   = $(this).find('.clause-id').eq(0).val();
			if(checked){
				arr.push(clauseId);
			}

		});

		return arr;
	}

	var endorseriskModal= function(){
		$("#btn-endors-risk").on('click', function(){
			populateEndorsRisks(polCode);
			createActiveRisksTbl(polCode);
			$('#endorseRiskModal').modal({
				backdrop: 'static',
				keyboard: true
			});
		});

		$("#btn-search-endos-risks").on('click', function(){
			createActiveRisksTbl(polCode);
		})
	}


	var createCertypeLov= function(riskCode){
		if($("#risk-cert-div").filter("div").html() != undefined)
		{
			Select2Builder.initAjaxSelect2({
				containerId : "risk-cert-div",
				sort : 'brnCertId',
				change: function(e, a, v){
					$("#risk-cert-id").val(e.added.brnCertId);
				},
				formatResult : function(a)
				{
					return a.certLots.certTypes.certDesc;
				},
				formatSelection : function(a)
				{
					return a.certLots.certTypes.certDesc;
				},
				initSelection: function (element, callback) {

				},
				id: "brnCertId",
				width:"250px",
				params: {riskId: riskCode},
				placeholder:"Select Cert Type"

			});
		}
	}

	var updateRiskCertTypes= function(){
		var $classForm = $('#edit-cert-form');
		var validator = $classForm.validate();
		$('#updateRiskCert').click(function(){
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
			var url = "updateRiskCertificate";
			var request = $.post(url, data );
			request.success(function(){
				$('#myPleaseWait').modal('hide');
				new PNotify({
					title: 'Success',
					text: 'Record created/updated Successfully',
					type: 'success',
					styling: 'bootstrap3'
				});
				$('#cert_tbl').DataTable().ajax.reload();
				validator.resetForm();
				$('#editRiskCertModal').modal('hide');
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

	var saveRiskCertTypes= function(){
		var $classForm = $('#new-cert-form');
		var validator = $classForm.validate();
		$('#saveRiskCert').click(function(){
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
			var url = "saveRiskCertificate";
			var request = $.post(url, data );
			request.success(function(){
				$('#myPleaseWait').modal('hide');
				new PNotify({
					title: 'Success',
					text: 'Record created/updated Successfully',
					type: 'success',
					styling: 'bootstrap3'
				});
				$('#cert_tbl').DataTable().ajax.reload();
				validator.resetForm();
				$('#riskCertModal').modal('hide');
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

	var newCertTypeModal= function(){
		$("#risk-cert-status").on('change', function(){
			if ($(this).val()==='C'){
				$(".cert-cancellation").show();
			}
			else{
				$(".cert-cancellation").hide();
			}
		});


		$("#btn-add-new-cert").on("click", function () {
			if($("#risk-code-pk").val() !== '') {
				$("#cert-risk-id").val($("#risk-code-pk").val());
				createCertypeLov($("#risk-code-pk").val());
				$('#riskCertModal').modal('show');

			}else{
				bootbox.alert("Select Risk to add Certificate");
				return;
			}
		});

	}


	var populateEndorsRisks= function(policyCode){
		if($("#endos-insured-frm").filter("div").html() != undefined)
		{
			Select2Builder.initAjaxSelect2({
				containerId : "endos-insured-frm",
				sort : 'arId',
				change: function(e, a, v){
					$("#endorse-insured-id").val(e.added.risk.insured.tenId);
				},
				formatResult : function(a)
				{
					return a.risk.insured.fname+" "+a.risk.insured.otherNames;
				},
				formatSelection : function(a)
				{
					return a.risk.insured.fname+" "+a.risk.insured.otherNames;
				},
				initSelection: function (element, callback) {

				},
				id: "arId",
				width:"250px",
				params: {polCode: policyCode},
				placeholder:"Select Insured"

			});
		}
	}


	var createActiveRisksTbl= function(policyCode){
		var url = "polactiverisks";
		var currTable = $('#endorserisktbl').DataTable( {
			"processing": true,
			"serverSide": true,
			"ajax": {
				'url': url,
				'data':{
					'riskId': $("#endorse-risk-search").val(),
					'insuredId':  $("#endorse-insured-id").val(),
					'policyCode':  policyCode
				},
			},
			autoWidth: true,
			lengthMenu: [ [10], [10] ],
			pageLength: 10,
			destroy: true,
			searching: false,
			"columns": [
				{ "data": "arId",
					"render": function ( data, type, full, meta ) {
						return full.risk.riskShtDesc;
					}
				},
				{ "data": "arId",
					"render": function ( data, type, full, meta ) {
						return full.risk.riskDesc;
					}
				},
				{ "data": "arId" ,
					"render": function ( data, type, full, meta ) {
						return moment(full.risk.wefDate).format('DD/MM/YYYY');
					}
				},
				{ "data": "arId" ,
					"render": function ( data, type, full, meta ) {
						return moment(full.risk.wetDate).format('DD/MM/YYYY');
					}
				},
				{ "data": "arId" ,
					"render": function ( data, type, full, meta ) {
						return '<select class="form-control" id="bin-type" name="binType" required> '+
							' <option value="R">Revise</option> '+
							' <option value="C">Cancel</option>'+
							' <option value="E">Extend</option>'+
							' </select>';
					}
				},
				{ "data": "arId" ,
					"render": function ( data, type, full, meta ) {
						return '<button  class="editor_edit btn btn-success">Endorse</button>';
					}
				},

			]
		} );


		$('#endorserisktbl').on('click', '.editor_edit', function (e) {
			var combo = $(this).closest('tr').find("select");
			var data = currTable.row($(this).closest('tr')).data();
			if(combo.val()==="R"){
				if (data === undefined || data === null){

				}
				else{
					$('#myPleaseWait').modal({
						backdrop: 'static',
						keyboard: true
					});
					$("#myPleaseWait").css("z-index", "1500");
					$.ajax({
						type: 'GET',
						url:  'endorseRisk',
						data: {"activeRiskCode": data.arId},
						dataType: 'json',
						async: true,
						success: function(result) {
							$('#myPleaseWait').modal('hide');
							$('#endorserisktbl').DataTable().ajax.reload();
							$('#risk_tbl').DataTable().ajax.reload();
							populatePolicyDetails();
						},
						error: function(jqXHR, textStatus, errorThrown) {
							$('#myPleaseWait').modal('hide');

							bootbox.alert(jqXHR.responseText);
						}
					});
				}
			}
		} );



		return currTable;
	}

	var newPolicyRemarksModal= function(){
		$("#btn-add-new-remark").on('click', function(){
			createPolicyRemarksTbl(polCode);
			$('#endorseRemarksModal').modal({
				backdrop: 'static',
				keyboard: true
			});
		});

	}

	var createPolicyRemarksTbl= function(policyCode){
		var url = "getNewPolicyRemarks";
		var currTable = $('#remarks_tbl').DataTable( {
			"processing": true,
			"serverSide": true,
			"ajax": {
				'url': url,
				'data':{
					'policyCode':  policyCode
				},
			},
			autoWidth: true,
			lengthMenu: [ [10], [10] ],
			pageLength: 10,
			destroy: true,
			searching: true,
			"columns": [
				{ "data": "remarkShtDesc",
					"render": function ( data, type, full, meta ) {
						return full.remarkShtDesc;
					}
				},
				{ "data": "remarks",
					"render": function ( data, type, full, meta ) {
						return full.remarks;
					}
				},


			]
		} );

		$('#remarks_tbl tbody').on( 'click', 'tr', function () {

			$(this).addClass('active').siblings().removeClass('active');

			var d = currTable.row( this ).data();
			if(d){
				$("#poli-remarks").val(d.remarks);
				$("#remark-pk").val(d.remarkId);
				$('#endorseRemarksModal').modal('hide');
				$("#remark-pol-id").val(policyCode);
			}


		} );


		return currTable;
	}


	var saveEndorsementRemakrs= function(){
		var $paymodesForm = $('#frm-pol-remarks');
		var validator = $paymodesForm.validate();

		$('#btn-save-remark').click(function(){
			if (!$paymodesForm.valid()) {
				return;
			}
			var $btn = $(this).button('Saving');
			var data = {};
			$paymodesForm.serializeArray().map(function(x){data[x.name] = x.value;});
			var url = "createPolicyRemarks";
			console.log(data);
			var request = $.post(url, data );

			request.success(function(){
				new PNotify({
					title: 'Success',
					text: 'Record created/updated Successfully',
					type: 'success',
					styling: 'bootstrap3'
				});
				getPolicyRemakrs();

			});
			request.error(function(jqXHR, textStatus, errorThrown){
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


	var getRiskSchedules= function(){
		$.ajax( {
			url: 'getRiskSchedules/'+selRiskCode,
			type: 'GET',
			processData: false,
			contentType: false,
			success: function (s ) {
				getRiskScheduleDetails(s.mappings);
			},
			error: function(xhr, error){
				bootbox.alert(xhr.responseText);
			}
		});
	}

	var addNewRiskSchedule= function(){
		$("#btn-add-new-sched").on('click', function(){
			$("#schedule-risk-id").val($("#risk-code-pk").val());
			$("#schedule-pk-id").val("");
			$.ajax( {
				url: 'getRiskSchedules/'+$("#risk-code-pk").val(),
				type: 'GET',
				processData: false,
				contentType: false,
				success: function (s ) {
					$("form#new-risk-schedule-form > div ").each(function(){
						$(this).remove();
					});
					for(var i=0;i< s.mappings.length;i++){
						if(s.mappings[i].colType==="T"){
							var data='<div class="form-group"> '+
								' <label for="cou-name" class="col-md-3 control-label">'+ s.mappings[i].column+'</label> '+
								'	<div class="col-md-8"> '+
								'	<input type="text" class="editUserCntrls form-control" name="column'+s.mappings[i].key+'" '+
								' required> '+
								' </div> '+
								' </div>	';
							$("form#new-risk-schedule-form").append(data);
						}
						else if(s.mappings[i].colType==="N"){
							var data='<div class="form-group"> '+
								' <label for="cou-name" class="col-md-3 control-label">'+ s.mappings[i].column+'</label> '+
								'	<div class="col-md-8"> '+
								'	<input type="number" class="editUserCntrls form-control" name="column'+s.mappings[i].key+'" '+
								' required> '+
								' </div> '+
								' </div>	';
							$("form#new-risk-schedule-form").append(data);
						}
						else if(s.mappings[i].colType==="D"){
							var data='<div class="form-group"> '+
								' <label for="cou-name" class="col-md-3 control-label">'+ s.mappings[i].column+'</label> '+
								'	<div class="col-md-8"> '+
								'<div class="input-group date datepicker-input"> '+
								' <input type="text" name="column'+s.mappings[i].key+'" class="form-control pull-right"'+
								' required /> '+
								'  <div class="input-group-addon"> '+
								'      <span class="glyphicon glyphicon-calendar"></span> '+
								'     </div> '+
								'    </div>'+
								' </div> '+
								' </div>	';
							$("form#new-risk-schedule-form").append(data);
						}
						else if(s.mappings[i].colType==="O"){
							var arr = s.mappings[i].options.split(",");
							var opts="";
							for(var x=0;x<arr.length;x++){
								opts+=' <option value="'+arr[x]+'">'+arr[x]+'</option> ';
							}
							var data='<div class="form-group"> '+
								' <label for="cou-name" class="col-md-3 control-label">'+ s.mappings[i].column+'</label> '+
								'	<div class="col-md-8"> '+
								' <select class="form-control" name="column' + s.mappings[i].key + '" '+
								' required> '+
								' <option value="">Select Option Value</option> '+opts+
								' </select> '+
								' </div> '+
								' </div>	';
							$("form#new-risk-schedule-form").append(data);
						}

					}
					$(".datepicker-input").each(function() {
						$(this).datetimepicker({
							format: 'DD/MM/YYYY'
						});

					});
				},
				error: function(xhr, error){
					bootbox.alert(xhr.responseText);
				}
			});
			$('#riskscheduleModal').modal({
				backdrop: 'static',
				keyboard: true
			});
		})
	}


	var getRiskScheduleDetails= function(cols){
		var arr = [];
		for(var i=0;i<cols.length;i++){
			arr.push({
				title: cols[i].column,
				data:"column"+(cols[i].key)
			});
		}
		var url = "riskSchedules/"+selRiskCode;
		var currTable = $('#risk-sched_tbl').DataTable(UTILITIES.extendsOpts({
			"ajaxUrl":url,
			"columns": arr
		}));

		$('#risk-sched_tbl tbody').on( 'click', 'tr', function () {
			currTable.$('tr.active').removeClass('active');
			$(this).addClass('active');
		} );
		$("#btn-add-del-sched").on('click', function(){
			var d = currTable.row('.active').data();
			if(d){
				bootbox.confirm("Are you sure want to delete selected Schedule Record?", function(result) {
					if(result){
						$.ajax({
							type: 'GET',
							url:  'deleteRiskSchedule/' + d.scheduleId,
							dataType: 'json',
							async: true,
							success: function(result) {
								new PNotify({
									title: 'Success',
									text: 'Record Deleted Successfully',
									type: 'success',
									styling: 'bootstrap3'
								});
								$('#risk-sched_tbl').DataTable().ajax.reload();

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

				});
			}
		});

		$("#btn-add-edit-sched").on('click', function(){
			var d = currTable.row('.active').data();
			if(d) {
				$("#schedule-risk-id").val($("#risk-code-pk").val());
				$("#schedule-pk-id").val(d.scheduleId);
				$.ajax({
					url: 'getRiskSchedules/' + $("#risk-code-pk").val(),
					type: 'GET',
					processData: false,
					contentType: false,
					success: function (s) {
						$("form#new-risk-schedule-form > div ").each(function () {
							$(this).remove();
						});
						for (var i = 0; i < s.mappings.length; i++) {
							var value;
							if(s.mappings[i].key==='1') {
								value = d.column1;
							}
							else if(s.mappings[i].key==='2') {
								value = d.column2;
							}
							else if(s.mappings[i].key==='3') {
								value = d.column3;
							}
							else if(s.mappings[i].key==='4') {
								value = d.column4;
							}
							else if(s.mappings[i].key==='5') {
								value = d.column5;
							}
							else if(s.mappings[i].key==='6') {
								value = d.column6;
							}
							else if(s.mappings[i].key==='7') {
								value = d.column7;
							}
							else if(s.mappings[i].key==='8') {
								value = d.column8;
							}
							else if(s.mappings[i].key==='9') {
								value = d.column9;
							}
							else if(s.mappings[i].key==='10') {
								value = d.column10;
							}
							else if(s.mappings[i].key==='11') {
								value = d.column11;
							}
							else if(s.mappings[i].key==='12') {
								value = d.column12;
							}
							else if(s.mappings[i].key==='13') {
								value = d.column13;
							}
							else if(s.mappings[i].key==='14') {
								value = d.column14;
							}
							else if(s.mappings[i].key==='15') {
								value = d.column15;
							}
							else if(s.mappings[i].key==='16') {
								value = d.column16;
							}
							else if(s.mappings[i].key==='17') {
								value = d.column17;
							}
							else if(s.mappings[i].key==='18') {
								value = d.column18;
							}else if(s.mappings[i].key==='19') {
								value = d.column19;
							}
							else if(s.mappings[i].key==='20') {
								value = d.column20;
							}
							else if(s.mappings[i].key==='21') {
								value = d.column21;
							}
							else if(s.mappings[i].key==='22') {
								value = d.column22;
							}
							else if(s.mappings[i].key==='23') {
								value = d.column23;
							}
							else if(s.mappings[i].key==='24') {
								value = d.column24;
							}
							else if(s.mappings[i].key==='25') {
								value = d.column25;
							}
							else if(s.mappings[i].key==='26') {
								value = d.column26;
							}
							else if(s.mappings[i].key==='27') {
								value = d.column27;
							}
							else if(s.mappings[i].key==='28') {
								value = d.column28;
							}
							else if(s.mappings[i].key==='29') {
								value = d.column29;
							}
							else if(s.mappings[i].key==='30') {
								value = d.column30;
							}
							if (s.mappings[i].colType === "T") {
								var data = '<div class="form-group"> ' +
									' <label for="cou-name" class="col-md-3 control-label">' + s.mappings[i].column + '</label> ' +
									'	<div class="col-md-8"> ' +
									'	<input type="text" class="editUserCntrls form-control" value="'+value+'" name="column' + s.mappings[i].key + '" ' +
									' required> ' +
									' </div> ' +
									' </div>	';
								$("form#new-risk-schedule-form").append(data);
							}
							else if (s.mappings[i].colType === "N") {
								var data = '<div class="form-group"> ' +
									' <label for="cou-name" class="col-md-3 control-label">' + s.mappings[i].column + '</label> ' +
									'	<div class="col-md-8"> ' +
									'	<input type="number" class="editUserCntrls form-control" value="'+value+'" name="column' + s.mappings[i].key + '" ' +
									' required> ' +
									' </div> ' +
									' </div>	';
								$("form#new-risk-schedule-form").append(data);
							}
							else if (s.mappings[i].colType === "D") {
								var data = '<div class="form-group"> ' +
									' <label for="cou-name" class="col-md-3 control-label">' + s.mappings[i].column + '</label> ' +
									'	<div class="col-md-8"> ' +
									'<div class="input-group date datepicker-input"> ' +
									' <input type="text" value="'+value+'" name="column' + s.mappings[i].key + '" class="form-control pull-right"' +
									' required /> ' +
									'  <div class="input-group-addon"> ' +
									'      <span class="glyphicon glyphicon-calendar"></span> ' +
									'     </div> ' +
									'    </div>' +
									' </div> ' +
									' </div>	';
								$("form#new-risk-schedule-form").append(data);
							}
							else if(s.mappings[i].colType==="O"){
								console.log(s.mappings[i].options);
								var arr = s.mappings[i].options.split(",");
								var opts="";
								for(var x=0;x<arr.length;x++){
									if(arr[x]===value)
										opts+=' <option value="'+arr[x]+'" selected>'+arr[x]+'</option> ';
									else opts+=' <option value="'+arr[x]+'">'+arr[x]+'</option> ';
								}
								var data='<div class="form-group"> '+
									' <label for="cou-name" class="col-md-3 control-label">'+ s.mappings[i].column+'</label> '+
									'	<div class="col-md-8"> '+
									' <select class="form-control" id="opts-column'+ s.mappings[i].key + '"  name="column' + s.mappings[i].key + '" '+
									' required> '+
									' <option value="">Select Option Value</option> '+opts+
									' </select> '+
									' </div> '+
									' </div>	';
								$("form#new-risk-schedule-form").append(data);
							}

						}
						$(".datepicker-input").each(function () {
							$(this).datetimepicker({
								format: 'DD/MM/YYYY'
							});

						});
					},
					error: function (xhr, error) {
						bootbox.alert(xhr.responseText);
					}
				});
				$('#riskscheduleModal').modal({
					backdrop: 'static',
					keyboard: true
				});
			}
		})



		return currTable;
	}




	var saveRiskSchedules= function(){
		var $classForm = $('#new-risk-schedule-form');
		var validator = $classForm.validate();
		$('#saveRiskSchedules').click(function(){
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
			var url = "saveRiskSchedule";
			var request = $.post(url, data );
			request.success(function(){
				$('#myPleaseWait').modal('hide');
				new PNotify({
					title: 'Success',
					text: 'Record created/updated Successfully',
					type: 'success',
					styling: 'bootstrap3'
				});
				$('#risk-sched_tbl').DataTable().ajax.reload();
				$('#section_tbl').DataTable().ajax.reload();
				populatePolicyDetails();
				validator.resetForm();
				$('#riskscheduleModal').modal('hide');
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


	var getCommissionRate= function(bindDetCode){
		$.ajax({
			type: 'GET',
			url:  'getCommissionRate',
			dataType: 'json',
			data: {"detId": bindDetCode},
			async: true,
			success: function(result) {
				$("#comm-rate").val(result);
			},
			error: function(jqXHR, textStatus, errorThrown) {

			}
		});
	}

	var getCreateNewIntParties= function(){
		var arr = [];
		$("#interested_party_tbl tr").each(function(row,tr){
			var checked   = $(this).find('.int-check').eq(0).is(":checked");
			var partId   = $(this).find('.int-part-id').eq(0).val();
			if(checked){
				arr.push(partId);
			}

		});

		return arr;
	}

	var getNewIntParties= function() {
		$("#btn-add-new-ips").click(function () {
			if ($("#policy-id").val() != '') {
				$("#part-pol-code").val($("#policy-id").val());
				$('#beneficiaryModal').modal({
					backdrop: 'static',
					keyboard: true
				});
			}
			else {
				bootbox.alert("Select policy to Add Beneficiaries")
			}
		});

		var $form = $('#beneficiary-form');
		var validator = $form.validate();
		$('#saveBeneficiaryBtn').click(function(){
			if (!$form.valid()) {
				return;
			}
			var $btn = $(this).button('Saving');
			var data = {};
			$form.serializeArray().map(function(x){data[x.name] = x.value;});
			var url = "createBeneficiary";
			var request = $.post(url, data );
			request.success(function(){
				new PNotify({
					title: 'Success',
					text: 'Record created/updated Successfully',
					type: 'success',
					styling: 'bootstrap3'
				});
				$('#benefeciary_tbl').DataTable().ajax.reload();
				validator.resetForm();
				$('#beneficiaryModal').modal('hide');
			});
			request.error(function(jqXHR, textStatus, errorThrown){
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





	var populateImportCoverTypesLov= function(){
		if($("#import-covertypes-frm").filter("div").html() != undefined)
		{
			Select2Builder.initAjaxSelect2({
				containerId : "import-covertypes-frm",
				sort : 'detId',
				change: function(e, a, v){
					$("#excel-binder-det-id").val(e.added.detId);
				},
				formatResult : function(a)
				{
					return a.covName+" - "+ a.subclassName;
				},
				formatSelection : function(a)
				{
					return a.covName+" - "+ a.subclassName;
				},
				initSelection: function (element, callback) {
					//var code  = $('#risk-cov-code').val();
					//var name = $("#cover-name").val();
					//var data = {covName:name,covId:code};
					//callback(data);
				},
				id: "covName",
				width:"250px",
				params: {bindCode: $("#binder-id").val()},
				placeholder:"Select Cover Type"

			});
		}

	}


	var populateImportSubCovers= function(binId){
		if($("#import-pol-covertypes-frm").filter("div").html() != undefined)
		{
			console.log($("#risk-bind-code").val());
			Select2Builder.initAjaxSelect2({
				containerId : "import-pol-covertypes-frm",
				sort : 'detId',
				change: function(e, a, v){
					$("#pol-excel-det-id").val(e.added.detId);
				},
				formatResult : function(a)
				{
					return a.covName+" - "+ a.subclassName;
				},
				formatSelection : function(a)
				{
					return a.covName+" - "+ a.subclassName;
				},
				initSelection: function (element, callback) {
					//var code  = $('#risk-cov-code').val();
					//var name = $("#cover-name").val();
					//var data = {covName:name,covId:code};
					//callback(data);
				},
				id: "covName",
				width:"250px",
				params: {bindCode: binId},
				placeholder:"Select Cover Type"

			});
		}
	}


	var importuWRisks= function(){
		$("#btn-import-risk").on('click', function(){
			populateImportCoverTypesLov();
			$('#importRisksModal').modal({
				backdrop: 'static',
				keyboard: true
			});
		});

		var $form = $("#risks-upload-form");
		var validator = $form.validate();
		$('form#risks-upload-form')
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
				data.append( 'file', $( '#file-avatar' )[0].files[0] );
				$.ajax( {
					url: 'importRisks',
					type: 'POST',
					data: data,
					processData: false,
					contentType: false,
					success: function (s ) {
						$('#importRisksModal').modal('hide');
						$('#myPleaseWait').modal('hide');
						$("#btn-import-logs").show();
						new PNotify({
							title: 'Success',
							text: 'Risks Uploaded Successfully',
							type: 'success',
							styling: 'bootstrap3'
						});
						populatePolicyDetails();
					},
					error: function(jqXHR, textStatus, errorThrown){
						$('#myPleaseWait').modal('hide');
						new PNotify({
							title: 'Error',
							text: jqXHR.responseText,
							type: 'error',
							styling: 'bootstrap3'
						});
					}
				});
			});
	}


	var getClientDocs= function(clientId){
		var url = SERVLET_CONTEXT+"/protected/clients/setups/clientDocs/"+clientId;
		var currTable = $('#clientDocsList').DataTable( {
			"processing": true,
			"serverSide": true,
			autoWidth: true,
			"ajax": {
				'url': url,
			},
			lengthMenu: [ [10,15,20], [10,15,20] ],
			pageLength: 10,
			destroy: true,
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
					"data": "cdId",
					"render": function ( data, type, full, meta ) {
						return '<button type="button" class="btn btn-success btn btn-info btn-xs" type="button" data-docs=' + encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.downloadClientDoc(this);"><i class="fa fa-file-archive-o"></button>';

					}

				},
			]
		} );
		return currTable;
	}

	var downloadClientDoc= function(button){
		var docs = JSON.parse(decodeURI($(button).data("docs")));
		window.open(SERVLET_CONTEXT+"/protected/clients/setups/clientDocument/"+docs['cdId'],
			'_blank' // <- This is what makes it open in a new window.
		);
	}

	var searchReqDocs= function(search){
		if($("#risk-code-pk").val() != ''){
			$.ajax({
				type: 'GET',
				url:  'getriskreqdocs',
				dataType: 'json',
				data: {"riskId": $("#risk-code-pk").val(),"docName":search},
				async: true,
				success: function(result) {
					$("#risksReqDocsTbl tbody").each(function(){
						$(this).remove();
					});
					for(var res in result){
						var markup = "<tr><td><input type='checkbox' name='record' id='"+result[res].sclReqrdId+"'></td><td>" + result[res].requiredDoc.reqShtDesc + "</td><td>" + result[res].requiredDoc.reqDesc + "</td></tr>";
						$("#risksReqDocsTbl").append(markup);
					}
					$("#req-risk-code").val($("#risk-code-pk").val());
					$('#riskReqDocsModal').modal({
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
			bootbox.alert("Select Risk to attach Documents")
		}
	}

	var saveRiskDocsList= function(){
		var arr = [];
		$("#saveRiskDocsBtn").click(function(){
			$("#risksReqDocsTbl tbody").find('input[name="record"]').each(function(){
				if($(this).is(":checked")){
					arr.push($(this).attr("id"));
				}
			});
			if(arr.length==0){
				bootbox.alert("No Documents Selected to attach..");
				return;
			}

			var $currForm = $('#risk-docs-form');
			var currValidator = $currForm.validate();
			if (!$currForm.valid()) {
				return;
			}

			var data = {};
			$currForm.serializeArray().map(function(x) {
				data[x.name] = x.value;
			});
			var url = "createRiskDocs";
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
					$('#risk_docs_tbl').DataTable().ajax.reload();
					$('#riskReqDocsModal').modal('hide');
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
	}

	var getRiskImportLogs = function(){
		var url = "riskImportLogs";
		var currTable = $('#riskImportLogTbl').DataTable( UTILITIES.extendsOpts({
			"ajaxUrl":url,
			"columns": [
				{ "data": "errorMessage" },
			]
		}));
		return currTable;
	};

	var init = function(){
		populatePolicyDetails();
		$('#overrid-prem').number( true, 2 );
		$(".datepicker-input").each(function() {
			$(this).datetimepicker({
				format: 'DD/MM/YYYY'
			});

		});
		populateClientLov();
		$("#pol-bin-type").on('change', function(){
			populateBinderLov();
			if($(this).val()==="B"){
				$("#comm-rate").attr("readonly", "true");
			}
			else{
				$("#comm-rate").attr("readonly", "false");
			}

		});

		$("#pol-buss-type").on('change', function(){
			if($(this).val()==="S"){
				$("#prorated-full").val("S");
			}
			else{
				$("#prorated-full").val("P");
			}
		});

		$("#chk-import-risks").change(function() {
			if(this.checked) {
				$(".risk-detail-tab").hide();
			}
			else{
				$(".risk-detail-tab").show();
			}
		});

		$("#section_form_tbl").on('click','.hyperlink-btn',function() {
			var mandatory = $(this).closest('tr').find(".mandatory").val();
			if(mandatory && mandatory==="Y"){
				bootbox.alert("Cannot delete a Mandatory Premium Item");
				return;
			}
			$(this).closest('tr').remove();
		});




		$("#email-to").on('change', function(){
			$.ajax({
				type: 'GET',
				url:  'getReceiverEmail',
				dataType: 'json',
				data: {"receiver": $(this).val()},
				async: true,
				success: function(result) {
					$("#email-send-to").val(result);
				},
				error: function(jqXHR, textStatus, errorThrown) {

				}
			});
		});

		populateCurrencyLov();
		populateSubAgentsLov();
		populatePaymentModes();
		populateUserBranches();
		populateInsuredLov();
		populateSubclassLov();
		populateCoverTypesLov();
		//$("#other-pol-details").hide();
		//getPolicyWet();
		changePolicyWetDt();
		createNewPolicy();
		saveRiskSections();
		newRisk();
		getNewPremItemsModal();
		getNewClausesModal();
		createPolClauses();
		createPolicyChecks();
		updatePolicyTaxes();
		endorseriskModal();
		newPolicyRemarksModal();
		saveEndorsementRemakrs();
		newCertTypeModal();
		saveRiskCertTypes();
		updateRiskCertTypes();
		getNewTaxesModal();
		addNewRiskSchedule();
		saveRiskSchedules();
		addNewClient();
		createSectorSelect();
		populateBranchLov1();
		populateClientTypeLov2();
		populateTitlesLov();
		populateCountryLov();
		saveClientDetails();
		validateRisk();
		getPolicyWet();
		UTILITIES.createAssignee();
		UTILITIES.emailReports();
		uploadRiskDocument();
		getNewIntParties();
		importuWRisks();
		enablepremsa();
		saveRiskDocsList();
		$("#btn-add-docs").click(function(){
			searchReqDocs("");
		});
		$("#btn-import-logs").hide();
		$("#btn-import-logs").click(function(){
			getRiskImportLogs();
			$('#riskimportLogsModal').modal({
				backdrop: 'static',
				keyboard: true
			});
		});
	}

	return {
		init: init,
		editRiskCerts:editRiskCerts,
		//deleteRiskCerts:deleteRiskCerts,
		editRiskSection:editRiskSection,
		deleteRiskSection:deleteRiskSection,
		deleteBeneficiary:deleteBeneficiary,
		editRiskDocs:editRiskDocs,
		downloadRiskDoc:downloadRiskDoc,
		deleteRiskDoc:deleteRiskDoc,
		editLifePolicyRisk:editLifePolicyRisk,
		deleteRisk:deleteRisk,
		editPolicyClause:editPolicyClause,
		deletePolicyClause:deletePolicyClause,
		authChecks:authChecks,
		editPolTaxes:editPolTaxes,
		deletePolTaxes:deletePolTaxes,
		getPolicyRemakrs:getPolicyRemakrs,
		downloadClientDoc:downloadClientDoc
	}


})(jQuery);

jQuery(UWScreen.init);
