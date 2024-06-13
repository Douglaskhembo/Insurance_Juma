var UWScreen = (function($,printCerts){
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

	function populateMultiProduct() {
		if($("#product-frm").filter("div").html() != undefined) {
			Select2Builder.initAjaxSelect2({
				containerId: "product-frm",
				sort: 'proDesc',
				change: function (e, a, v) {
					// $("#sect-id").val(e.added.code);
					// populateOccupations(e.added.code);
					$("#product-id").val(e.added.proCode);
					populateBinderLov();
				},
				formatResult: function (a) {
					return a.proDesc
				},
				formatSelection: function (a) {
					return a.proDesc
				},
				initSelection: function (element, callback) {
					var code = $("#product-id").val();
					var name = $("#bind-product-desc").val();
					var data = {proDesc:name,proCode:code};
					callback(data);
				},
				id: "proCode",
				placeholder: "Select Product",
			});
		}
	}

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
		//$("#btn-add-client").click(function(){
		//	$(".clnt-status").hide();
		//	$('#tenant-form').find("input[type=text],input[type=number],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden], textarea,select").val("");
		//	$('#createClientModal').modal({
		//		backdrop: 'static',
		//		keyboard: true
		//	})
		//})
	}
	var getQuestionnairedtls = function(){
		var url = SERVLET_CONTEXT+"/protected/setups/binders/allBinderQuestions/"+ $("#binder-id").val();
		$.ajax({
			type: 'GET',
			url: url,
			dataType: 'json',
			async: true,
			success: function (result) {
				for(var res in result){
					console.log("Mandatory="+result[res].isRequired)
				}
				getObject(result);
			},
			error: function (jqXHR, textStatus, errorThrown) {
				console.log(jqXHR);
				new PNotify({
					title: 'Error',
					text: jqXHR.responseText,
					type: 'error',
					styling: 'bootstrap3'
				});
			}
		});
		function getObject(quiz){
			var pages = [];
			var batches = [];
			var count = 0;
			for(var i=0;i<quiz.length;i++){
				batches.push(quiz[i]);
				count++;
				if(count===5 || (count < 5 && i===quiz.length-1)){
					pages.push({
						questions:batches
					})
					batches = [];
					count=0;
				}
			}

			console.log(pages);

			var json = {
				title: "Questionnaire",
				showProgressBar: "top",
				pages: pages
			};
			console.log(json);
			window.survey = new Survey.Model(json);

			survey.onComplete.add(function(result) {
				//document.querySelector("#surveyResult").innerHTML =
				//	"result: " + JSON.stringify(result.data);
				var questions = result.getAllQuestions();
				var arr = [];
				for (var i = 0; i < questions.length; i++) {
					result.validateQuestion(questions[i].name );
					console.log(questions[i].name +'='+ questions[i].value);
					var quizname = questions[i].name;
					var quizanswer = questions[i].value;
					arr.push({
						question: quizname,
						answer: quizanswer
					});
				}
				if(arr.length==0){
					bootbox.alert("No Records Selected to Process")
					return;
				}
				var data = {};
				data.quizPolicyCode=polCode;
				data.quizandAnswers = arr;
				console.log(data);

				var url = SERVLET_CONTEXT+"/protected/uw/policies/SavePolicyQuiz";
				//var request = $.post(url, data );

				$.ajax(
					{
						url:url,
						type: "POST",
						data: JSON.stringify(data),
						success: function(s){
							$('#myPleaseWait').modal('hide');
							new PNotify({
								title: 'Success',
								text: 'Processing Successfully',
								type: 'success',
								styling: 'bootstrap3'
							});
							$('#questionnaire_tbl').DataTable().ajax.reload();
							getPolicyDetails();
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
			var surveyValueChanged = function (sender, options) {
				var el = document.getElementById(options.name);
				if (el) {
					el.value = options.value;

				}
			};
			survey.onValueChanged.add(function (sender, options) {
				var mySurvey = sender;
				var questionName = options.name;
				var newValue = options.value;
			});

			var quizdata = {};
			$.ajax({
				type: 'GET',
				url:  SERVLET_CONTEXT+'/protected/uw/policies/policyQuizList',
				dataType: 'json',
				async: false,
				success: function(result) {
					for(var i=0;i<result.length;i++){
						var key = result[i].question.questionname;
						var obj = {};
						var SplitChar = ',';

						if(result[i].choice.indexOf(SplitChar) > 0){
							var multipleChoices = [];
							multipleChoices = result[i].choice.split(SplitChar);
							obj[key] = multipleChoices;
						}else {
							obj[key] = result[i].choice
						}
						//obj[key] = result[i].choice
						$.extend(quizdata,obj );
					}
				},
				error: function(jqXHR, textStatus, errorThrown) {
					console.log(jqXHR);
				}
			});

			//console.log(quizdata);
			survey.data = quizdata;
			console.log(survey.data);

			$("#surveyElement").Survey({
				model: survey, onValueChanged: surveyValueChanged
			});
		}
	}
	var getPolicyQuestionnaire  = function(){
		var url = SERVLET_CONTEXT+"/protected/uw/policies/policyQuiz/"+polCode;

		var currTable = $('#questionnaire_tbl').DataTable(UTILITIES.extendsOpts({
			"ajaxUrl":url,
			"columns": [
				{ "data": "quizId",
					"render": function ( data, type, full, meta ) {
						return full.question.questionname;
					}
				},
				{ "data": "choice"
				},

			]
		}) );
		return currTable;
	};
	var deletePolicyQuiz= function(){
		bootbox.confirm("Are you sure want to delete this questionnaire?", function(result) {
			var url = SERVLET_CONTEXT+"/protected/uw/policies/deletePolicyQuiz/" + polCode;
			if(result){
				$('#myPleaseWait').modal({
					backdrop: 'static',
					keyboard: true
				});
				$.ajax({
					type: 'GET',
					url:  url,
					dataType: 'json',
					async: true,
					success: function(result) {
						$('#myPleaseWait').modal('hide');
						new PNotify({
							title: 'Success',
							text: 'Questionnaire Deleted Successfully',
							type: 'success',
							styling: 'bootstrap3'
						});
						$('#questionnaire_tbl').DataTable().ajax.reload();
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
	var fillQuestionnaire = function(){

		$("#btn-questionnaire").click(function () {
			getQuestionnairedtls();
			$('#questionnaireModal').modal({
				backdrop: 'static',
				keyboard: true
			});

		});

		$("#btn-edit-questionnaire").click(function () {
			getQuestionnairedtls();
			$('#questionnaireModal').modal({
				backdrop: 'static',
				keyboard: true
			});

		});

		$("#btn-del-questionnaire").click(function () {
			deletePolicyQuiz();
		});


	}

	var populatePolicyDetails = function(){
		console.log('polCode ',polCode);
		if(typeof polCode!== 'undefined'){
			if(polCode!==-2000){
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
				getPolicybeneficiaries();
				getPolicyRemakrs();
				$("#other-pol-details").show();
				$("#questionnaire-tabb").show();
				$("#myTab,#show-taxes,#show-clauses,#end-remarks,#checks-tab,#cert-tab,#receipts-tab,#workflow").show();
				createPolicyClauses();
				createPolicyTaxes();
				$(".import-risks").hide();
				createPolicyChecks();
			}
			else{
				$("#btn-auth-policy").css("display","none");
				$("#btn-dispatch-trans").hide();
				$("#btn-print-certs").hide();
				$("#btn-import-risk").hide();
				$("#btn-uw-reports").hide();
				$("#risk-form").show();
				$("#risk-div").hide();
				$("#other-pol-details").hide();
				$("#prem-rates-div").show();
				$("#btn-add-policy").show();
				$("#sect-div").hide();
				$("#btn-save-risk").hide();
				$("#btn-save-cancel").hide();
				$("#btn-add-risk").hide();
				$("#questionnaire-tabb").hide();
				$("#myTab,#show-taxes,#show-clauses,#end-remarks,#checks-tab,#cert-tab,#receipts-tab,#workflow").hide();
				$("#btn-add-new-section").hide();
				$("#display-client").hide();
				$("#display-binder").hide();
				$("#display-payment-mode").hide();
				$("#display-branch").hide();
				$("#display-currency").hide();
				$(".import-risks").show();
				populateSubAgentsLov();

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
								$("#btn-print-certs").hide();
								$("#pol-status").text("Draft");
								$("#edit-currency").show();
								$("#display-currency").hide();
								$("#edit-branch").show();
								$("#display-branch").hide();
								$("#edit-payment-mode").show();
								$("#display-payment-mode").hide();
								$("#edit-binder").show();
								$("#display-binder").hide();
								//$(".edit-client").show();
								//$("#display-client").hide();
								$("#client-frm").select2("enable", true);
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
								$('#client-input').addClass('col-md-6 col-xs-9').removeClass('col-md-7 col-xs-12');
								$('#new-client').css('visibility', 'visible');
								if(s.quizTaken==="Y"){
									$("#btn-questionnaire").hide();
									$("#btn-del-questionnaire").show();
								}else {
									$("#btn-del-questionnaire").hide();
									$("#btn-questionnaire").show();

								}
							}
							else if(s.authStatus==="R"){
								$("#btn-assign-trans").show();
								$("#btn-import-risk").hide();
								$("#btn-add-docs").hide();
								$("#btn-add-new-ips").hide();
								$("#btn-add-policy").css("display","none");
								$("#btn-dispatch-trans").hide();
								$("#btn-auth-policy").css("display","block");
								var count=0;
								$('#polReceipts tr').each(function(){
									count++;
								});
								console.log(count);

								if ( count > 3 ) {
									$("#btn-make-ready-policy").css("display","none");
								}
								else {
									$("#btn-undo-make-ready").css("display", "block");
									$("#btn-make-ready-policy").css("display", "none");
								}
								$("#btn-print-certs").hide();
								$("#pol-status").text("Ready");
								$("#edit-currency").hide();
								$("#display-currency").show();
								$("#edit-branch").hide();
								$("#display-branch").show();
								$("#edit-payment-mode").hide();
								$("#display-payment-mode").show();
								$("#edit-binder").hide();
								$("#display-binder").show();
								$("#client-frm").select2("enable", false);
								//$(".edit-client").hide();
								//$("#display-client").show();
								$("#sub-agent-frm").select2("enable", false);
								$('#client-input').addClass('col-md-7 col-xs-12').removeClass('col-md-6 col-xs-9');
								$('#new-client').css('visibility', 'hidden');
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
								if(s.transType==='CO'||s.transType==='CN'||s.transType==='RS'){
									$("#btn-add-new-remark").show();
									$("#btn-save-remark").show();
									$("#poli-remarks").attr("disabled",false);
								}
								else {
									$("#btn-add-new-remark").hide();
									$("#btn-save-remark").hide();
									$("#poli-remarks").attr("disabled",true);
								}
								$("#btn-add-new-sched").hide();
								$("#btn-add-edit-sched").hide();
								$("#btn-add-del-sched").hide();
								$("#btn-questionnaire").hide();
								$("#btn-del-questionnaire").hide();
							}
							else if(s.authStatus==="A"){
								$("#btn-assign-trans").show();
								$("#btn-import-risk").hide();
								$("#btn-add-docs").hide();
								$("#btn-add-new-ips").hide();
								$("#btn-dispatch-trans").show();
								$("#btn-print-certs").show();
								$("#btn-add-policy").css("display","none");
								$("#btn-auth-policy").css("display","none");
								$("#btn-undo-make-ready").css("display","none");
								$("#btn-make-ready-policy").css("display","none");
								$("#pol-status").text("Authorised");
								$("#sub-agent-frm").select2("enable", false);
								$('#client-input').addClass('col-md-7 col-xs-12').removeClass('col-md-6 col-xs-9');
								$('#new-client').css('visibility', 'hidden');
								$("#edit-currency").hide();
								$("#display-currency").show();
								$("#edit-branch").hide();
								$("#display-branch").show();
								$("#edit-payment-mode").hide();
								$("#display-payment-mode").show();
								$("#edit-binder").hide();
								$("#display-binder").show();
								$("#client-frm").select2("enable", false);
								//$(".edit-client").hide();
								//$("#display-client").show();
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
								$("#btn-questionnaire").hide();
								$("#btn-del-questionnaire").hide();
							}

						}
						polCode = s.policyId;
						UTILITIES.getProcessActiveDiagram(s.policyId);
						UTILITIES.getTaskActive(s.policyId);
						UTILITIES.getProcessHistory(s.policyId);
						// $("#client-info").text(s.client.fname+" "+s.client.otherNames);
						if(s.binder)
							$("#binder-info").text(s.binder.binName);
					//	$("#pay-mode-info").text(s.paymentMode.pmDesc);
						$("#branch-info").text(s.branch.obName);
						$("#currency-info").text(s.transCurrency.curName);
						$("#pol-no").text(s.polNo);
						$("#h4pol").text("Policy No: "+s.polNo);
						$("#div-pol-no").val(s.polNo);
						$("#div-endos-no").val(s.polRevNo);
						$("#policy-id").val(s.policyId);
						$("#pol-rev-no").text(s.polRevNo);
						if(s.product)
							$("#pol-bind-age-appli").val(s.product.ageApplicable);
						$("#pol-sub-agent-comm").text(UTILITIES.currencyFormat(s.subAgentComm));
						$("#pol-endos-gross-premium").text(UTILITIES.currencyFormat(s.endosgrossPremium));
						$("#pol-paid-premium").text(UTILITIES.currencyFormat(s.paidPremium));
						if (s.refundablePremium){
							if (s.refundablePremium!=0){
								$("#pol-refundable-amount").text(UTILITIES.currencyFormat(s.refundablePremium));
								$("#refund-amount1").val(s.refundablePremium);
								$("#hasrefund").val("Y");
							}else {
								$("#hasrefund").val("N");
							}
						}

						$("#pol-sum-insured").text(UTILITIES.currencyFormat(s.sumInsured));
						$("#pol-premium").text(UTILITIES.currencyFormat(s.premium));
						$("#pol-basic-prem").text(UTILITIES.currencyFormat(s.basicPrem));
						$("#pol-net-prem").text(UTILITIES.currencyFormat(s.netPrem));
						$("#pol-taxes-amt").text(s.taxes);
						if(s.product) {
							if (s.product.motorProduct) {
								$(".motor-disp").show();
								$(".non-motor-disp").hide();
							} else {
								$(".motor-disp").hide();
								$(".non-motor-disp").show();
							}
							$(".wiba-disp").hide();
							// if (s.product.wibaProduct && s.product.wibaProduct==='Y') {
							// 	$(".motor-disp").hide();
							// 	$(".non-motor-disp").show();
							// 	$(".wiba-disp").show();
							// } else {
							// 	$(".motor-disp").hide();
							// 	$(".non-motor-disp").show();
							//
							// }


						}
						else {
							$(".motor-disp").hide();
							$(".non-motor-disp").show();
						}
						if(s.product)
							$("#pol-prod-name").text(s.product.proDesc);
						$("#from-date").val(moment(s.wefDate).format('DD/MM/YYYY'));
						$("#wet-date").val(moment(s.wetDate).format('DD/MM/YYYY'));
						if(s.agent)
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
						$("#product-id").val(s.product.proCode);
						if(s.product.proGroup.prgType ==='MP'){
							$("#pol-bin-type").val('MP');
							$(".multi-product").css("display","block");
							$(".multi-product-uw").css("display","block");
							$(".bind-insurance").css("display","none");
							$(".bind-info").css("display","none");
							$("#bind-product-desc").val(s.product.proDesc);
							populateMultiProduct();
							getPolicyBinders(s.policyId);
						}
						else {
							$(".bind-info").css("display","block");
							$(".multi-product").css("display","none");
							$(".bind-insurance").css("display","block");
							$(".multi-product-uw").css("display","none");
							$("#risk-binder").text(s.binder.binName);
							$("#risk-binder-id").val(s.binder.binId);
							$("#risk-binder-code").val(s.binder.binId);
							$("#risk-bind-code").val(s.binder.binId);
							$("#binder-id").val(s.binder.binId);
							$("#pol-agent-id").val(s.agent.acctId);
							$("#bind-name").val(s.binder.binName);
							$("#pol-binder-policy").val(s.binder.binType);
							$("#pol-bin-type").val(s.binder.binType);
							populateBinderLov();
							getUWPolicyRisks(s.policyId);

						}

						//
						// $("#pm-id").val(s.paymentMode.pmId);
						// $("#pm-name").val(s.paymentMode.pmDesc);
						// populatePaymentModes();
						$("#brn-id").val(s.branch.obId);
						$("#brn-name").val(s.branch.obName);
						populateUserBranches();
						$('#pol-comm-amt').text(UTILITIES.currencyFormat(s.commAmt));
						$('#pol-tl').text(UTILITIES.currencyFormat(s.trainingLevy));
						$('#pol-phcf').text(UTILITIES.currencyFormat(s.phcf));
						$('#pol-sd').text(UTILITIES.currencyFormat(s.stampDuty));
						$('#pol-whtx').text(UTILITIES.currencyFormat(s.whtx));
						$('#pol-extras').text(UTILITIES.currencyFormat(s.extras));
						$("#pol-fap").text(UTILITIES.currencyFormat(s.futurePrem));
						console.log(s);
						$('#pol-tran-type-disp').text(s.transType);
						$('#pol-trans-type').val(s.transType);
						$('#pol-prev-policy').val(s.prevPolicy);
						$('#pol-reuse-contra-policy').val(s.reusecontraPolicy);
						if(s.transType==="EN"||s.transType==="RS"){
							if(s.authStatus==="D"){
								$("#btn-endors-risk").show();
							}
							else {
								$("#btn-endors-risk").hide();
							}
							$(".endorse-disp").show();
							$(".risk-note-dip").hide();
							if(s.transType==="RS"){
								$("#btn-add-risk").hide();
								$("#btn-import-risk").hide();
							}

						}else if(s.transType==="CO" || s.transType==="CN"){
							$(".endorse-disp").attr("id","endrsrpt");
							if(s.transType==="CO"){
								$("#endrsrpt").html("<a href="+SERVLET_CONTEXT+"/protected/uw/policies/rpt_endorse ' target='_blank'>Reversal Report</a>");
							}
							else if(s.transType==="CN"){
								$("#endrsrpt").html("<a href="+SERVLET_CONTEXT+"/protected/uw/policies/rpt_endorse ' target='_blank'>Cancellation Report</a>");
							}
							$("#btn-endors-risk").hide();
							$(".endorse-disp").show();
							$(".risk-note-dip").hide();
							//$(".motor-disp").hide();
						}
						else{
							$("#btn-endors-risk").hide();
							$(".endorse-disp").hide();
							$(".risk-note-dip").show();
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

						if(s.currentStatus==="A" || s.currentStatus==="D"  ){
							$("#btn-add-new-cert").show();
						}
						else{
							$("#btn-add-new-cert").hide();
						}

						$("#binder-frm").select2("enable", false);
						$("#product-frm").select2("enable", false);
						$("#pol-bintype").prop("disabled", true);
						$("#pol-bin-type").prop("disabled", true);


						getPolicybeneficiaries();
						printCerts.createCertTable(s.policyId);
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
				url:  'getWetDate',
				dataType: 'json',
				data: {"wefDate":dt},
				async: true,
				success: function(result) {
					$("#wet-date").val(moment(result).format('DD/MM/YYYY'));
					$("#risk-wet-date").val(moment(result).format('DD/MM/YYYY'));


				},
				error: function(jqXHR, textStatus, errorThrown) {

				}
			});
		});
	};

	var populateInsuredLov = function(){
		if($("#insured-frm").filter("div").html() != undefined)
		{
			Select2Builder.initAjaxSelect2({
				containerId : "insured-frm",
				sort : 'fname',
				change: function(e, a, v){
					if($("#pol-bind-age-appli").val() && $("#pol-bind-age-appli").val()==="Y"){
						$.ajax({
							type: 'GET',
							url:  'getClientAge',
							dataType: 'json',
							data: {"clientId": e.added.tenId},
							async: true,
							success: function(result) {
								$("#insured-client-age").val(result);
								$("#insured-code").val(e.added.tenId);
								$('#subclass-frm').select2('val', null);
								$('#covertypes-frm').select2('val', null);
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
						$("#insured-code").val(e.added.tenId);
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
					var code = $("#insured-code").val();
					var name = $("#insured-name").val();
					var othernames = $("#insured-other-name").val();
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
					$("#insured-name").val(e.added.fname);
					$("#insured-code").val(e.added.tenId);
					$("#insured-other-name").val(e.added.otherNames);
					if($("#pol-bind-age-appli").val() && $("#pol-bind-age-appli").val()==="Y"){
						$.ajax({
							type: 'GET',
							url:  'getClientAge',
							dataType: 'json',
							data: {"clientId": e.added.tenId},
							async: true,
							success: function(result) {
								$("#insured-client-age").val(result);
								populateInsuredLov();
							},
							error: function(jqXHR, textStatus, errorThrown) {
							}
						});
					}else{
						populateInsuredLov();
					}


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
				placeholder:"Select Client"

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
					getClientAge();
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
				placeholder:"Select Classification"

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
					console.log(e.added);
					$("#risk-cov-code").val(e.added.covId);
					$("#binder-det-id").val(e.added.detId);
					$("#install-perc").val(e.added.distribution);
					$("#install-no").val(1);
					getCommissionRate(e.added.detId);
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
		console.log( $("#pol-bind-age-appli").val());
		if($("#pol-bind-age-appli").val() && $("#pol-bind-age-appli").val()==="Y"){
			$.ajax({
				type: 'GET',
				url: 'getBinderClientPremRates',
				dataType: 'json',
				data: {"detId": detId,"age":$("#insured-client-age").val()},
				async: true,
				success: function (result) {
					$("#section_form_tbl tbody").each(function () {
						$(this).remove();
					});
					for (var res in result) {
						var allowLimit = "N";
						if (result[res].limitsAllowed){
							allowLimit = result[res].limitsAllowed ;
						}
						var markup = "<tr><td><input type='hidden' class='mandatory form-control' value='" + result[res].mandatory + "'><input type='hidden' class='section form-control' value='" + result[res].premId +
							"'><input type='hidden' class='ratesApplicable form-control' value='" + result[res].ratesApplicable + "'>" + result[res].sectionDesc + "</td><td>" + ' <input type="text" class="amount form-control" required>' +
							"</td><td><input type='text' class='rate form-control' value='" + result[res].rate + "'></td><td><input type='text' class='divFactor form-control'  value='" + result[res].divFactor + "'></td></td><td>" +
							"<input type='text' class='freeLimit form-control'  value='" + result[res].freeLimit + "'></td>" +
							"<td><button type='button' class='btn btn-danger btn btn-danger btn-xs hyperlink-btn'><i class='fa fa-trash-o'></button></td><td></td></tr>";
						if($("#pol-bin-type").val()==="B"){
							// if ((result[res].rider && result[res].rider === "Y") || (result[res].ratesApplicable && result[res].ratesApplicable === "Y") ){
							// console.log($("#insured-client-age").val());
							var str = (result[res].rangeType && result[res].rangeType === "AG")?" <input type='text' class='amount form-control' value='" + $("#insured-client-age").val() + "' readonly>" : ((result[res].ratesApplicable && result[res].ratesApplicable === "Y")?" <input type='text' class='amount form-control' required>":" <input type='text' class='amount form-control' disabled required>");
							var limitStr = (result[res].limitsAllowed ==="Y")? "<button type='button' class='btn btn-primary btn btn-info btn-xs'  onclick='UWScreen.viewPremLimits("+result[res].pk+");'>Limits</button>":"";
							console.log(limitStr);
							markup = "<tr><td><input type='hidden' class='mandatory form-control' value='" + result[res].mandatory + "'><input type='hidden' class='section form-control' value='" + result[res].premId +
								"'><input type='hidden' class='ratesApplicable form-control' value='" + result[res].ratesApplicable + "'>" + result[res].sectionDesc + "</td>" +
								"<td>"+str+"</td>" +
								"<td><input type='text' class='rate form-control' readonly value='" + result[res].rate + "'></td>" +
								"<td><input type='text' class='divFactor form-control' readonly value='" + result[res].divFactor + "'></td>" +
								"<td><input type='text' class='freeLimit form-control' readonly  value='" + result[res].freeLimit + "'></td>" +
								"<td><button type='button' class='btn btn-danger btn btn-danger btn-xs hyperlink-btn'><i class='fa fa-trash-o'></button></td>" +
								"<td>" +limitStr+"</td></tr>";


						}
						else {
							markup = "<tr><td><input type='hidden' class='mandatory form-control' value='" + result[res].mandatory + "'><input type='hidden' class='section form-control' value='" + result[res].premId +
							"'<input type='hidden' class='ratesApplicable form-control' value='" + result[res].ratesApplicable + "'>" + result[res].sectionDesc + "</td><td>" + (result[res].rangeType && result[res].rangeType === "AG")?' <input type="text" class="amount form-control" value="' + $("#insured-client-age").val() + '" readonly>':' <input type="text" class="amount form-control" required>' + "</td>" +
							"<td>"+(result[res].rider && result[res].rider === "Y")?"<input type='text' class='rate form-control' value='" + result[res].rate + "'>":"<input type='text' class='rate form-control' readonly value='" + result[res].rate + "'>" + "</td>" +
							"<td>" + (result[res].rider && result[res].rider === "Y")?"<input type='text' class='divFactor form-control' value='" + result[res].divFactor + "'>":"<input type='text' class='divFactor form-control' readonly value='" + result[res].divFactor + "'></td>" +
								"<td><input type='text' class='freeLimit form-control' readonly  value='" + result[res].freeLimit + "'></td>" +
								"<td><button type='button' class='btn btn-danger btn btn-danger btn-xs hyperlink-btn'><i class='fa fa-trash-o'></button></td>" +
								"<td>" +limitStr+"</td></tr>";


						}
						$("#section_form_tbl").append(markup);
					}

					$("#section_form_tbl tr").find("input[type=text]").number(true, 3);
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
							"<td><button type='button' class='btn btn-danger btn btn-danger btn-xs hyperlink-btn'><i class='fa fa-trash-o'></button></td><td></td></tr>";

						if($("#pol-bin-type").val()==="B"){
							if (result[res].rider && result[res].rider === "Y") {
								markup = "<tr><td><input type='hidden' class='mandatory form-control' value='" + result[res].mandatory + "'><input type='hidden' class='section form-control' value='" + result[res].premId +
									"'><input type='hidden' class='ratesApplicable form-control' value='" + result[res].ratesApplicable + "'>" + result[res].sectionDesc + "</td><td>" + ' <input type="text" class="amount form-control" required disabled>' +
									"</td><td><input type='text' class='rate form-control' readonly value='" + result[res].rate + "'></td><td><input type='text' class='divFactor form-control' readonly value='" + result[res].divFactor + "'></td></td><td>" +
									"<input type='text' class='freeLimit form-control' readonly  value='" + result[res].freeLimit + "'></td>" +
									"<td><button type='button' class='btn btn-danger btn btn-danger btn-xs hyperlink-btn'><i class='fa fa-trash-o'></button></td><td></td></td></tr>";

							}
							else {
								if(result[res].limitsAllowed ==="Y") {
									var input = ' <input type="text" class="amount form-control" required disabled>';
									if(result[res].ratesApplicable === "Y"){
										input = ' <input type="text" class="amount form-control" required>';
									}
									markup = "<tr><td><input type='hidden' class='mandatory form-control' value='" + result[res].mandatory + "'><input type='hidden' class='section form-control' value='" + result[res].premId +
										"'><input type='hidden' class='ratesApplicable form-control' value='" + result[res].ratesApplicable + "'>" + result[res].sectionDesc + "</td><td>" + input +
										"</td><td><input type='text' class='rate form-control' readonly value='" + result[res].rate + "'></td><td><input type='text' class='divFactor form-control' readonly value='" + result[res].divFactor + "'></td></td><td>" +
										"<input type='text' class='freeLimit form-control' readonly  value='" + result[res].freeLimit + "'></td>" +
										"<td><button type='button' class='btn btn-danger btn btn-danger btn-xs hyperlink-btn'><i class='fa fa-trash-o'></button></td><td><input type='button' class='btn btn-primary btn btn-info btn-xs' value='Limits' onclick='UWScreen.viewPremLimits("+result[res].pk+");'></td></tr>";
								}
								else{
									markup = "<tr><td><input type='hidden' class='mandatory form-control' value='" + result[res].mandatory + "'><input type='hidden' class='section form-control' value='" + result[res].premId +
										"'><input type='hidden' class='ratesApplicable form-control' value='" + result[res].ratesApplicable + "'>" + result[res].sectionDesc + "</td><td>" + ' <input type="text" class="amount form-control" required>' +
										"</td><td><input type='text' class='rate form-control' readonly value='" + result[res].rate + "'></td><td><input type='text' class='divFactor form-control' readonly value='" + result[res].divFactor + "'></td></td><td>" +
										"<input type='text' class='freeLimit form-control' readonly  value='" + result[res].freeLimit + "'></td>" +
										"<td><button type='button' class='btn btn-danger btn btn-danger btn-xs hyperlink-btn'><i class='fa fa-trash-o'></button></td><td></td></tr>";
								}
							}
						}
						else {

							if (result[res].ratesApplicable && result[res].ratesApplicable === "Y") {
								markup = "<tr><td><input type='hidden' class='mandatory form-control' value='" + result[res].mandatory + "'><input type='hidden' class='section form-control' value='" + result[res].premId +
									"'><input type='hidden' class='ratesApplicable form-control' value='" + result[res].ratesApplicable + "'>" + result[res].sectionDesc + "</td><td>" + ' <input type="text" class="amount form-control">' +
									"</td><td><input type='text' class='rate form-control' readonly value='" + result[res].rate + "'></td><td><input type='text' class='divFactor form-control' readonly value='" + result[res].divFactor + "'></td></td><td>" +
									"<input type='text' class='freeLimit form-control' readonly  value='" + result[res].freeLimit + "'></td>" +
									"<td><button type='button' class='btn btn-danger btn btn-danger btn-xs hyperlink-btn'><i class='fa fa-trash-o'></button></td><td></td></tr>";
							}
							else if (result[res].rider && result[res].rider === "Y") {
								markup = "<tr><td><input type='hidden' class='mandatory form-control' value='" + result[res].mandatory + "'><input type='hidden' class='section form-control' value='" + result[res].premId +
									"'><input type='hidden' class='ratesApplicable form-control' value='" + result[res].ratesApplicable + "'>" + result[res].sectionDesc + "</td><td>" + ' <input type="text" class="amount form-control" readonly>' +
									"</td><td><input type='text' class='rate form-control' value='" + result[res].rate + "'></td><td><input type='text' class='divFactor form-control' value='" + result[res].divFactor + "'></td></td><td>" +
									"<input type='text' class='freeLimit form-control'  value='" + result[res].freeLimit + "'></td>" +
									"<td><button type='button' class='btn btn-danger btn btn-danger btn-xs hyperlink-btn'><i class='fa fa-trash-o'></button></td><td></td></tr>";
							}
							if(result[res].limitsAllowed ==="Y") {
								markup = "<tr><td><input type='hidden' class='mandatory form-control' value='" + result[res].mandatory + "'><input type='hidden' class='section form-control' value='" + result[res].premId +
									"'><input type='hidden' class='ratesApplicable form-control' value='" + result[res].ratesApplicable + "'>" + result[res].sectionDesc + "</td><td>" + ' <input type="text" class="amount form-control" required disabled>' +
									"</td><td><input type='text' class='rate form-control' readonly value='" + result[res].rate + "'></td><td><input type='text' class='divFactor form-control' readonly value='" + result[res].divFactor + "'></td></td><td>" +
									"<input type='text' class='freeLimit form-control' readonly  value='" + result[res].freeLimit + "'></td>" +
									"<td><button type='button' class='btn btn-danger btn btn-danger btn-xs hyperlink-btn'><i class='fa fa-trash-o'></button></td><td><button type='button' class='btn btn-primary btn btn-info btn-xs'  onclick='UWScreen.viewPremLimits("+result[res].pk+");'>Limits</button></td></tr>";
							}
						}
						$("#section_form_tbl").append(markup);
					}

					$("#section_form_tbl tr").find("input[type=text]").number(true, 3);
				},
				error: function (jqXHR, textStatus, errorThrown) {

				}
			});
		}
	};


	var viewPremLimits = function(id){
		var url =SERVLET_CONTEXT+"/protected/setups/binders/sectLimits/"+id;
		$('#premLimitsTbl').DataTable( {
			"processing": true,
			"serverSide": true,
			autoWidth: true,
			searching: false,
			"deferRender": true,
			"ajax": {
				'url': url
			},
			lengthMenu: [ [5,10], [5,10] ],
			pageLength: 10,
			destroy: true,
			"columns": [
				{ "data": "clausesDef",
					"render": function ( data, type, full, meta ) {
						return full.clausesDef.clause.clauShtDesc;
					}
				},
				{ "data": "clausesDef" ,
					"render": function ( data, type, full, meta ) {
						return full.clausesDef.clause.clauHeading;
					}
				},
				{ "data": "value" },
			]
		} );
		$('#premLimitsModal').modal({
			backdrop: 'static',
			keyboard: true
		});
	}

	function populateBinderLov (data){
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
					$("#pol-ins-comp").text(e.added.name);
					$("#pol-prod-name").text(e.added.proDesc);
					$("#product-id").val(e.added.proCode);
					$("#pol-agent-id").val(e.added.acctId);
					$("#client-pol-no").val(e.added.binPolNo);
					$("#risk-binder").text(e.added.binName);
					$("#pol-bind-age-appli").val(e.added.ageApplicable);
					if(e.added.product){
						$(".motor-disp").show();
						$(".non-motor-disp").hide();
						$("#risk-label").text('Plate Number');
						$("#risk-description").text('Model');
					}
					else{
						$(".motor-disp").hide();
						$(".non-motor-disp").show();
						$("#risk-label").text('Risk Identifier');
						$("#risk-description").text('Risk Description');
					}
					populateSubclassLov();

				},
				data:(data)?data:[],
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
				multiple: ($("#pol-bin-type").val() && $("#pol-bin-type").val()==='MP'),
				params: {bindType: $("#pol-bin-type").val(),productId: $("#product-frm").val()},
				placeholder:"Select Contract"

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
					console.log(data);
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
			updateMakeReadyPolicy();
		});

		$("#btn-reprint-cert-policy").click(function(){
			generateCert();
		});


		$("#btn-undo-make-ready").click(function(){
			undoMakeReady();
		});

		$("#btn-auth-policy").click(function(){
			if ($("#hasrefund").val()==='Y') {
				$("#refund-amount").val($("#refund-amount1").val());
				$('#refundModal').modal({
					backdrop: 'static',
					keyboard: true,


				});
				$("#proceedTorefund").click(function(){
					if ($("#refund-amount").val()>$("#refund-amount1").val()) {
						bootbox.alert({message:"Refund Amount cannot be greater than "+$("#refund-amount1").val(),backdrop: false ,closeButton: false});
						return;
					}

					$('#refundModal').modal('hide');
					authorizePolicy($("#refund-amount").val());
				});
				$("#stoprefund").click(function(){
					$('#refundModal').modal('hide');
					return;
				});

			}else {
				authorizePolicy();
			}

		});

		$("#btn-dispatch-trans").click(function(){
			dispatchDocuments();
		});
	};





	var authorizePolicy = function(refundAmt){
		var stack_bottomleft = {"dir1": "up", "dir2": "left", "firstpos1": 25, "firstpos2": 25};
		$('#myPleaseWait').modal({
			backdrop: 'static',
			keyboard: true
		});
		$.ajax({
			type: 'GET',
			url:  'authorizePolicy',
			dataType: 'json',
			data: {"refundAmt": refundAmt},
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

	var makeReady = function(){
		var stack_bottomleft = {"dir1": "up", "dir2": "left", "firstpos1": 25, "firstpos2": 25};
		$('#myPleaseWait').modal({
			backdrop: 'static',
			keyboard: true
		});
		$.ajax({
			type: 'POST',
			url:  'makePolicyReady',
			dataType: 'json',
			async: true,
			data:JSON.stringify({}),
			success: function(result) {
				console.log(result);
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
		//var url = "makePolicyReady";

		$('#myPleaseWait').modal({
			backdrop: 'static',
			keyboard: true
		});
		$.ajax({
			url:url,
			type: "POST",
			data: JSON.stringify(data),
			success: function(s){
				console.log(s);

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
				console.log(s.status);
				if(s.status && s.status==='N'){
					bootbox.alert("Authorize Checks before proceeding....");
				}
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


	var generateCert = function(){
		var stack_bottomleft = {"dir1": "up", "dir2": "left", "firstpos1": 25, "firstpos2": 25};
		var data = {};
		var url = "issueCertificate";
		//var url = "makePolicyReady";

		$('#myPleaseWait').modal({
			backdrop: 'static',
			keyboard: true
		});
		$.ajax({
			url:url,
			type: "POST",
			data: JSON.stringify(data),
			success: function(s){
				console.log(s);

				$('#myPleaseWait').modal('hide');
				new PNotify({
					title: 'Success',
					text: 'Certificate Issued Successfuly',
					type: 'success',
					styling: 'bootstrap3',
					addclass: 'stack-bottomright',
					stack: stack_bottomleft
				});
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
		var url = "createPolicy";

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
		var url = "createPolicy";
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
		if($("#pol-bin-type").val() && $("#pol-bin-type").val()==='MP'){
			var arr =  $("#binder-frm").val().split(",");
			var binddata = {
				"bindCodes":arr
			}
			$.extend(data, binddata);
			console.log(data);
		}
		console.log(data);
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

	var getRiskBeneficiaries = function(){
		var url = "riskBeneficiaries/"+selRiskCode;
		var currTable = $('#risk_beneficiaries_tbl').DataTable(UTILITIES.extendsOpts({
			"ajaxUrl":url,
			"columns": [
				{ "data": "fullName" },
				{ "data": "occupation" },
				{ "data": "salary",
					"render": function ( data, type, full, meta ) {
						return UTILITIES.currencyFormat(full.salary);
					}
				},
				{
					"data": "bwbId",
					"render": function ( data, type, full, meta ) {
						return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-beneficiaries='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.editRiskBeneficiary(this);"><i class="fa fa-pencil-square-o"></button>';

					}
				},
				{
					"data": "bwbId",
					"render": function ( data, type, full, meta ) {
						if(polStatus){
							if(polStatus ==='0')
								return '<button type="button" class="btn btn-danger btn btn-info btn-xs" data-beneficiaries='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.deleteRiskBeneficiary(this);"><i class="fa fa-trash-o"></button>';
							else
								return '<button type="button" class="btn btn-danger btn btn-info btn-xs" data-beneficiaries='+encodeURI(JSON.stringify(full)) + '  disabled><i class="fa fa-trash-o"></button>';

						}
						else
							return '<button type="button" class="btn btn-danger btn btn-info btn-xs" data-beneficiaries='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.deleteRiskBeneficiary(this);"><i class="fa fa-trash-o"></button>';
					}

				},
			]
		}));
		return currTable;
	};

	var getRiskCertificates = function(){
		var url = "riskCerts/"+selRiskCode;
		var currTable = $('#cert_tbl').DataTable(UTILITIES.extendsOpts({
			"ajaxUrl":url,
			"columns": [
				{ "data": "subclassCertTypes",
					"render": function ( data, type, full, meta ) {
						return full.subclassCertTypes.certType.certDesc;
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
							}else return full.status;
						}
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
				// {
				// 	"data": "pcId",
				// 	"render": function ( data, type, full, meta ) {
				// 		if(full.certNo){
				// 			return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-certs='+encodeURI(JSON.stringify(full)) + ' disabled ">Allocate Cert</button>';
				//
				// 		}
				// 		else return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-certs='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.allocateRiskCerts(this);">Allocate Cert</button>';
				//
				// 	}
				//
				// },
				// {
				// 	"data": "pcId",
				// 	"render": function ( data, type, full, meta ) {
				// 		if(!full.certNo){
				// 			return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-certs='+encodeURI(JSON.stringify(full)) + ' disabled ">Deallocate Cert</button>';
				//
				// 		}
				// 		else return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-certs='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.deallocateRiskCerts(this);">Deallocate Cert</button>';
				//
				// 	}
				//
				// },
				{
					"data": "pcId",
					"render": function ( data, type, full, meta ) {
						//if(full.risk.policy.authStatus){
						//if(full.risk.policy.authStatus ==="D")
						return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-certs='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.editRiskCerts(this);"><i class="fa fa-pencil-square-o"></button>';

						//else
						//return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-certs='+encodeURI(JSON.stringify(full)) + ' disabled><i class="fa fa-pencil-square-o"></button>';

						//}
						//else
						//	return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-certs='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.editRiskCerts(this);"><i class="fa fa-pencil-square-o"></button>';
					}

				},
				{
					"data": "pcId",
					"render": function ( data, type, full, meta ) {
						if(polStatus){
							if(polStatus ==='0')
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

						return full.desc;
					}
				},
				{ "data": "amount" ,
					"render": function ( data, type, full, meta ) {

						return UTILITIES.currencyFormat(full.amount);
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
				{ "data": "divFactor" },
				{ "data": "freeLimit" },
				{
					"data": "sectId",
					"render": function ( data, type, full, meta ) {
						if(polStatus){
							if(polStatus ==='0')
								return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-risksections='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.editRiskSection(this);"><i class="fa fa-pencil-square-o"></button>';
							else
								return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-risksections='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.editRiskSection(this);" disabled><i class="fa fa-pencil-square-o"></button>';

						}
						else
							return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-risksections='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.editRiskSection(this);"><i class="fa fa-pencil-square-o"></button>';
					}

				},
				{
					"data": "sectId",
					"render": function ( data, type, full, meta ) {
						if(polStatus){
							if(polStatus ==='0')
								return '<button type="button" class="btn btn-danger btn btn-info btn-xs" data-risksections='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.deleteRiskSection(this);"><i class="fa fa-trash-o"></button>';
							else
								return '<button type="button" class="btn btn-danger btn btn-info btn-xs" data-risksections='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.deleteRiskSection(this);" disabled><i class="fa fa-trash-o"></button>';

						}
						else
							return '<button type="button" class="btn btn-danger btn btn-info btn-xs" data-risksections='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.deleteRiskSection(this);"><i class="fa fa-trash-o"></button>';
					}

				},
			]
		}) );
		return currTable;
	};

	var deleteRiskParties = function(button){
		var parties = JSON.parse(decodeURI($(button).data("parties")));
		bootbox.confirm("Are you sure want to delete "+parties['interestedParties'].partName+"?", function(result) {
			if(result){
				$('#myPleaseWait').modal({
					backdrop: 'static',
					keyboard: true
				});
				$.ajax({
					type: 'GET',
					url:  'deleteIntParties/' + parties['ridId'],
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
						$('#interested_parties_tbl').DataTable().ajax.reload();

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

	var getScheduleDetails = function(){
		var url = "vehicleDetails/"+selRiskCode;
		var currTable = $('#schedule_details_tbl').DataTable(UTILITIES.extendsOpts({
			"ajaxUrl":url,
			"columns": [
				{ "data": "bodyColor" },
				{ "data": "bodyType" },
				{ "data": "carMake" },
				{ "data": "carModel" },
				{ "data": "carryCapacity" },
				{ "data": "ChassisNo" },
				{ "data": "engineCapacity" },
				{ "data": "engineNumber" },
				{ "data": "logbookNumber" },
				{ "data": "yearOfManufacture" },
			]
		}) );

		$('#schedule_details_tbl tbody').on( 'click', 'tr', function () {
			$(this).addClass('active').siblings().removeClass('active');
			var d = currTable.row( this ).data();
			if(d){
				$("#schedule-pk-id").val(d.vdId);
				$("#schedule-risk-id").val(d.riskId);
				$("#chasisno").val(d.ChassisNo);
				$("#engineno").val(d.engineNumber);
				$("#body-type").val(d.bodyType);
				$("#make").val(d.carMake);
				$("#model").val(d.carModel);
				$("#yom").val(d.yearOfManufacture);
				$("#color").val(d.bodyColor);
				$("#logbookno").val(d.logbookNumber);
				$("#tonnage").val('');
				$("#cubiccapacity").val(d.engineCapacity);
				$("#cc").val(d.carryCapacity);
			}
		});
		return currTable;
	};

	var getRiskIntParties = function(){
		var url = "risksIntParties/"+selRiskCode;
		var currTable = $('#interested_parties_tbl').DataTable(UTILITIES.extendsOpts({
			"ajaxUrl":url,
			"columns": [
				{ "data": "interestedParties",
					"render": function ( data, type, full, meta ) {

						return full.interestedParties.partName;
					}
				},
				{ "data": "interestedParties" ,
					"render": function ( data, type, full, meta ) {

						if(full.interestedParties.partType){
							if(full.interestedParties.partType==="P"){
								return "Premium Financier";
							}
							else if(full.interestedParties.partType==="B"){
								return "Beneficiary";
							}
							else if(full.interestedParties.partType==="I"){
								return "Interested Party";
							}
						}
						else
							return "";
					}
				},
				{ "data": "interestedParties"  ,
					"render": function ( data, type, full, meta ) {
						return full.interestedParties.pinNumber;
					}
				},
				{ "data": "interestedParties"  ,
					"render": function ( data, type, full, meta ) {

						return full.interestedParties.regNo;
					}
				},
				{
					"data": "ridId",
					"render": function ( data, type, full, meta ) {
						if(full.risk.policy.authStatus){
							if(full.risk.policy.authStatus ==="D")
								return '<button type="button" class="btn btn-danger btn btn-info btn-xs" data-parties='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.deleteRiskParties(this);"><i class="fa fa-trash-o"></button>';
							else
								return '<button type="button" class="btn btn-danger btn btn-info btn-xs" data-parties='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.deleteRiskParties(this);" disabled><i class="fa fa-trash-o"></button>';

						}
						else
							return '<button type="button" class="btn btn-danger btn btn-info btn-xs" data-parties='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.deleteRiskParties(this);"><i class="fa fa-trash-o"></button>';
					}

				},
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
						if(full.risk){
							if(full.risk.policy.authStatus ==='D' && full.risk.policy.policyId===polCode  )
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
						if(full.risk){
							if(full.risk.policy.authStatus ==='D' && full.risk.policy.policyId===polCode )
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

	//var  createCertTable=function(policycode){
	//	var rows_selected = [];
	//	var table = $('#polCertsList').DataTable({
	//		"processing": true,
	//		"serverSide": true,
	//		autoWidth: true,
	//		"searching": false,
	//		"ajax": {
	//			'url': "getpolicyprintcerts",
	//			'data':{
	//				'polId':  policycode
	//			},
	//		},
	//		lengthMenu: [ [20, 40,60], [20, 40,60] ],
	//		pageLength: 20,
	//		destroy: true,
	//		'columnDefs': [{
	//			'targets': 7,
	//			'searchable':false,
	//			'orderable':false,
	//			'className': 'dt-body-center',
	//			'render': function (data, type, full, meta){
	//				//if(full.goodForPrint){
	//				//    if(full.goodForPrint ==='Y'){
	//				//        return '<input type="checkbox" checked>';
	//				//    }
	//				//    else
	//				//        return '<input type="checkbox">';
	//				//}else
	//				return '<input type="checkbox">';
	//			}
	//		}],
	//		'rowCallback': function(row, data, dataIndex){
	//			var rowId = data.cqId;
	//			if($.inArray(rowId, rows_selected) !== -1){
	//				$(row).find('input[type="checkbox"]').prop('checked', true);
	//				$(row).addClass('selected');
	//			}
	//		},
	//		"columns": [
	//
	//			{ "data": "risk",
	//				"render": function ( data, type, full, meta ) {
	//					return full.risk.riskShtDesc;
	//				}
	//			},
	//			{ "data": "policyCerts",
	//				"render": function ( data, type, full, meta ) {
	//					return moment(full.policyCerts.certWef).format('DD/MM/YYYY');
	//				}
	//			},
	//			{ "data": "policyCerts" ,
	//				"render": function ( data, type, full, meta ) {
	//					return moment(full.policyCerts.certWet).format('DD/MM/YYYY');
	//				}
	//			},
	//			{ "data": "policyCerts" ,
	//				"render": function ( data, type, full, meta ) {
	//					return moment(full.certWef).format('DD/MM/YYYY hh:mm:ss');
	//				}
	//			},
	//			{ "data": "status",
	//				"render": function ( data, type, full, meta ) {
	//
	//					return full.status;
	//				}
	//			},
	//			{ "data": "allocBy",
	//				"render": function ( data, type, full, meta ) {
	//					if(full.allocBy)
	//						return full.allocBy.username;
	//					else return "";
	//				}
	//			},
	//			{ "data": "certNo",
	//				"render": function ( data, type, full, meta ) {
	//
	//					return full.certNo;
	//				}
	//			},
	//			{ "data": "cqId" },
	//		]
	//	} );
	//
	//	// Handle click on checkbox
	//	$('#polCertsList tbody').on('click', 'input[type="checkbox"]', function(e){
	//		var $row = $(this).closest('tr');
	//
	//		// Get row data
	//		var data = table.row($row).data();
	//
	//		// Get row ID
	//		var rowId = data.cqId;
	//
	//		// Determine whether row ID is in the list of selected row IDs
	//		var index = $.inArray(rowId, rows_selected);
	//
	//		// If checkbox is checked and row ID is not in list of selected row IDs
	//		if(this.checked && index === -1){
	//			rows_selected.push(rowId);
	//
	//			// Otherwise, if checkbox is not checked and row ID is in list of selected row IDs
	//		} else if (!this.checked && index !== -1){
	//			rows_selected.splice(index, 1);
	//		}
	//
	//		if(this.checked){
	//			$row.addClass('selected');
	//		} else {
	//			$row.removeClass('selected');
	//		}
	//
	//		// Update state of "Select all" control
	//		//updateDataTableSelectAllCtrl(table);
	//
	//		// Prevent click event from propagating to parent
	//		e.stopPropagation();
	//	});
	//	$('#polCertsList').on('click', 'tbody td, thead th:first-child', function(e){
	//		$(this).parent().find('input[type="checkbox"]').trigger('click');
	//	});
	//
	//	//table.on('draw', function(){
	//	//    // Update state of "Select all" control
	//	//    updateDataTableSelectAllCtrl(table);
	//	//});
	//
	//	$('#btn-print').on('click', function(e){
	//		var arr = [];
	//		$.each(rows_selected, function(index, rowId){
	//			arr.push(rowId);
	//		});
	//
	//		if(arr.length==0){
	//			bootbox.alert("No Certificates Selected to Print..");
	//			return;
	//		}
	//
	//		var $currForm = $('#print-form');
	//		var currValidator = $currForm.validate();
	//		if (!$currForm.valid()) {
	//			return;
	//		}
	//		var data = {};
	//		$currForm.serializeArray().map(function(x) {
	//			data[x.name] = x.value;
	//		});
	//		data.certCodes = arr;
	//		var url = "printPolCertificate";
	//		$.ajax({
	//			url : url,
	//			type : "POST",
	//			data : JSON.stringify(data),
	//			success : function(s) {
	//
	//				printPdf(SERVLET_CONTEXT +"/protected/certs/printcert");
	//				bootbox.confirm({
	//					message: "Certificate Printed Successfully?",
	//					buttons: {
	//						confirm: {
	//							label: 'Yes',
	//							className: 'btn-success'
	//						},
	//						cancel: {
	//							label: 'No',
	//							className: 'btn-danger'
	//						}
	//					},
	//					callback: function (result) {
	//						if(result){
	//							$.ajax({
	//								type: 'GET',
	//								url:  'markPrintedPolCerts',
	//								dataType: 'json',
	//								async: true,
	//								success: function(result) {
	//									bootbox.alert("Receipt Printing operation complete");
	//									$('#polCertsList').DataTable().ajax.reload();
	//
	//								},
	//								error: function(jqXHR, textStatus, errorThrown) {
	//									new PNotify({
	//										title: 'Error',
	//										text: jqXHR.responseText,
	//										type: 'error',
	//										styling: 'bootstrap3'
	//									});
	//								}
	//							});
	//
	//						}
	//					}
	//				});
	//
	//				arr = [];
	//			},
	//			error : function(jqXHR, textStatus, errorThrown) {
	//				new PNotify({
	//					title: 'Error',
	//					text: jqXHR.responseText,
	//					type: 'error',
	//					styling: 'bootstrap3'
	//				});
	//			},
	//			dataType : "json",
	//			contentType : "application/json"
	//		});
	//
	//	});
	//
	//	$('#btn-deallocate').on('click', function(e){
	//
	//		var arr = [];
	//		$.each(rows_selected, function(index, rowId){
	//			arr.push(rowId);
	//		});
	//
	//		if(arr.length==0){
	//			bootbox.alert("No Certificates Selected to Deallocate..");
	//			return;
	//		}
	//
	//		//var $currForm = $('#print-cert-form');
	//		//var currValidator = $currForm.validate();
	//		//if (!$currForm.valid()) {
	//		//	return;
	//		//}
	//		var data = {};
	//		//$currForm.serializeArray().map(function(x) {
	//		//	data[x.name] = x.value;
	//		//});
	//		var url = "deallocateCerts";
	//		data.certs = arr;
	//		$.ajax({
	//			url : url,
	//			type : "POST",
	//			data : JSON.stringify(data),
	//			success : function(s) {
	//				new PNotify({
	//					title: 'Success',
	//					text: 'Certificate Allocation Successfully',
	//					type: 'success',
	//					styling: 'bootstrap3'
	//				});
	//				$('#cert_tbl').DataTable().ajax.reload();
	//				arr = [];
	//			},
	//			error : function(jqXHR, textStatus, errorThrown) {
	//				new PNotify({
	//					title: 'Error',
	//					text: jqXHR.responseText,
	//					type: 'error',
	//					styling: 'bootstrap3'
	//				});
	//			},
	//			dataType : "json",
	//			contentType : "application/json"
	//		});
	//
	//	});
	//
	//	$('#btn-allocate').on('click', function(e){
	//
	//		var arr = [];
	//		$.each(rows_selected, function(index, rowId){
	//			arr.push(rowId);
	//		});
	//
	//		if(arr.length==0){
	//			bootbox.alert("No Certificates Selected to Allocate..");
	//			return;
	//		}
	//
	//		//var $currForm = $('#print-cert-form');
	//		//var currValidator = $currForm.validate();
	//		//if (!$currForm.valid()) {
	//		//	return;
	//		//}
	//		var data = {};
	//		//$currForm.serializeArray().map(function(x) {
	//		//	data[x.name] = x.value;
	//		//});
	//		var url = "allocatePolCerts";
	//		data.certs = arr;
	//		$.ajax({
	//			url : url,
	//			type : "POST",
	//			data : JSON.stringify(data),
	//			success : function(s) {
	//				new PNotify({
	//					title: 'Success',
	//					text: 'Certificate Allocation Successfully',
	//					type: 'success',
	//					styling: 'bootstrap3'
	//				});
	//				$('#polCertsList').DataTable().ajax.reload();
	//				arr = [];
	//			},
	//			error : function(jqXHR, textStatus, errorThrown) {
	//				new PNotify({
	//					title: 'Error',
	//					text: jqXHR.responseText,
	//					type: 'error',
	//					styling: 'bootstrap3'
	//				});
	//			},
	//			dataType : "json",
	//			contentType : "application/json"
	//		});
	//
	//	});
	//	rows_selected = table.column(0).data();
	//	table.draw(false);
	//	return table;
	//}

	function getPolicyBinders(policyCode){
		var url = "policyBinders/"+policyCode;
		var currTable = $('#policy-binders-tbl').DataTable(UTILITIES.extendsOpts({
			"ajaxUrl":url,
			"columns": [
				{ "data": "binder",
					"render": function ( data, type, full, meta ) {

						return full.binder.account.name;
					}
				},
				{ "data": "binder",
					"render": function ( data, type, full, meta ) {

						return full.binder.binShtDesc;
					}
				},
				{ "data": "binder",
					"render": function ( data, type, full, meta ) {

						return full.binder.binName;
					}
				},
				{ "data": "basicPrem",
					"render": function ( data, type, full, meta ) {

						return UTILITIES.currencyFormat(full.basicPrem);
					}
				},

				{
					"data": "policyBindId",
					"render": function ( data, type, full, meta ) {
						if(full.policyTrans.authStatus){

							if(full.policyTrans.authStatus==="D")
								return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-policyrisks='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.editPolicyRisk(this);"><i class="fa fa-pencil-square-o"></button>';
							else
								return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-policyrisks='+encodeURI(JSON.stringify(full)) + '  disabled><i class="fa fa-pencil-square-o"></button>';

						}
						else
							return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-policyrisks='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.editPolicyRisk(this);"><i class="fa fa-pencil-square-o"></button>';
					}

				},
				{
					"data": "policyBindId",
					"render": function ( data, type, full, meta ) {
						if(full.policyTrans.authStatus){
							if(full.policyTrans.authStatus==="D")
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

		$('#policy-binders-tbl tbody').on( 'click', 'tr', function () {
			$(this).addClass('active').siblings().removeClass('active');
			var aData = currTable.rows('.active').data();
			if (aData[0] === undefined || aData[0] === null) {

			} else {
				$("#risk-bind-code").val(aData[0].binder.binId);
				$("#pol-binder-pk").val(aData[0].policyBindId);
				$("#risk-binder").text(aData[0].binder.binName);
				$("#insured-name").val(aData[0].policyTrans.client.fname);
				$("#insured-code").val(aData[0].policyTrans.client.tenId);
				$("#insured-other-name").val(aData[0].policyTrans.client.otherNames);
				populateInsuredLov();
				getUWPolicyRisks(policyCode,aData[0].policyBindId);
				initiateRisk(9000);
			}
		});
	}

	function getUWPolicyRisks(policyCode, polBindCode){

		polBindCode = typeof polBindCode !== 'undefined' ? polBindCode : -2000;
		var url = "policyRisks/"+policyCode+"/"+polBindCode;
		var currTable = $('#risk_tbl').DataTable(UTILITIES.extendsOpts({
			"ajaxUrl":url,
			"columns": [
				{ "data": "riskShtDesc" },
				{ "data": "riskDesc" },
				{ "data": "wefDate",
					"render": function ( data, type, full, meta ) {
						return moment(full.wefDate).format('DD/MM/YYYY');
					}
				},
				{ "data": "wetDate" ,
					"render": function ( data, type, full, meta ) {
						return moment(full.wetDate).format('DD/MM/YYYY');
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
				{ "data": "sumInsured",
					"render": function ( data, type, full, meta ) {

						return UTILITIES.currencyFormat(full.sumInsured);
					}
				},
				{ "data": "premium",
					"render": function ( data, type, full, meta ) {

						return UTILITIES.currencyFormat(full.premium);
					}
				},

				{
					"data": "riskId",
					"render": function ( data, type, full, meta ) {
						if(full.policy.authStatus){

							if(full.policy.authStatus==="D")
								return '<button type="button" class="btn btn-primary btn btn-primary btn-xs" data-policyrisks='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.editPolicyRisk(this);"><i class="fa fa-pencil-square-o"></button>';
							else
								return '<button type="button" class="btn btn-primary btn btn-primary btn-xs" data-policyrisks='+encodeURI(JSON.stringify(full)) + '  disabled><i class="fa fa-pencil-square-o"></button>';

						}
						else
							return '<button type="button" class="btn btn-primary btn btn-primary btn-xs" data-policyrisks='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.editPolicyRisk(this);"><i class="fa fa-pencil-square-o"></button>';
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
				$("#ben-import-risk-id").val(selRiskCode);
				getRiskSections();
				getRiskSchedules();
				getRiskIntParties();
				getScheduleDetails();
				getRiskDocs();
				getRiskCertificates();
				getRiskBeneficiaries();
				getRiskSchedules();
				getClientDocs(aData[0].insured.tenId);
				$("#risk-det-id-pk").val(aData[0].binderDetails.detId);
				populateRiskSections(aData[0].binderDetails.detId);
				$("#cert-from-date").val(moment(aData[0].wefDate).format('DD/MM/YYYY'));
				$("#cert-wet-date").val(moment(aData[0].wetDate).format('DD/MM/YYYY'));
				$.ajax({
					type: 'GET',
					url:  'mileageDetails?riskId='+aData[0].riskShtDesc,
					dataType: 'json',
					data: {},
					async: true,
					success: function(result) {
						$("#mileage").text(result.mileage)
						$("#start-mileage").text(result.start_mileage)
						$("#used-mileage").text(result.used_mileage)
					    console.log(result);
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
				if($("#pol-bind-age-appli").val() && $("#pol-bind-age-appli").val()==="Y"){
					$.ajax({
						type: 'GET',
						url:  'getClientAge',
						dataType: 'json',
						data: {"clientId": aData[0].insured.tenId},
						async: true,
						success: function(result) {
							$("#insured-client-age").val(result);
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

	var editRiskBeneficiary = function (button){
		$('#salary').number( true, 2 );
		var beneficiaries = JSON.parse(decodeURI($(button).data("beneficiaries")));
		$("#fullName").val(beneficiaries['fullName']);
		$("#occupation").val(beneficiaries['occupation']);
		$("#salary").val(beneficiaries['salary']);
		$("#risk-ben-code-pk").val(beneficiaries['riskId']);
		$("#risk-bencode-pk").val(beneficiaries['bwbId']);
		$('#beneficiaryModals').modal({
			backdrop: 'static',
			keyboard: true
		})
	}


	var deleteRiskBeneficiary = function(button){
		var beneficiaries = JSON.parse(decodeURI($(button).data("beneficiaries")));
		bootbox.confirm("Are you sure want to delete "+beneficiaries['fullName']+"?", function(result) {
			if(result){
				$('#myPleaseWait').modal({
					backdrop: 'static',
					keyboard: true
				});
				$.ajax({
					type: 'GET',
					url:  'deleteRiskBeneficiary/' + beneficiaries['bwbId'],
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
						$('#risk_beneficiaries_tbl').DataTable().ajax.reload();

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


	var uploadRiskBeneficiries = function (){
		var $form = $("#benefits-upload-form");
		var validator = $form.validate();
		$('form#benefits-upload-form')
			.submit( function( e ) {
				if($("#ben-import-risk-id").val() === ''){
					bootbox.alert('Select Risk to upload beneficiaries');
					return;
				}
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
					url: 'importRiskBeneficiaries',
					type: 'POST',
					data: data,
					processData: false,
					contentType: false,
					success: function (s ) {
						new PNotify({
							title: 'Success',
							text: 'File Uploaded Successfully',
							type: 'success',
							styling: 'bootstrap3'
						});
						$('#myPleaseWait').modal('hide');
						$('#benefits-upload-form').find("input[type=file]").val("");
						$('#risk_beneficiaries_tbl').DataTable().ajax.reload();
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

	var deleteRiskCerts = function(button){
		var certs = JSON.parse(decodeURI($(button).data("certs")));
		bootbox.confirm("Are you sure want to delete "+certs['subclassCertTypes'].certType.certDesc+"?", function(result) {
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
						$('#polCertsList').DataTable().ajax.reload();

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

	var allocateRiskCerts = function(button){
		var certs = JSON.parse(decodeURI($(button).data("certs")));
		bootbox.confirm("Are you sure want to allocate "+certs['cert'].certLots.certTypes.certDesc+"?", function(result) {
			if(result){
				$('#myPleaseWait').modal({
					backdrop: 'static',
					keyboard: true
				});
				$.ajax({
					type: 'GET',
					url:  'allocateRiskCert/' + certs['pcId'],
					dataType: 'json',
					async: true,
					success: function(result) {
						$('#myPleaseWait').modal('hide');
						new PNotify({
							title: 'Success',
							text: 'Certificate allocated Successfully',
							type: 'success',
							styling: 'bootstrap3'
						});
						$('#cert_tbl').DataTable().ajax.reload();
						$('#polCertsList').DataTable().ajax.reload();

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
	var deallocateRiskCerts = function(button){
		var certs = JSON.parse(decodeURI($(button).data("certs")));
		bootbox.confirm("Are you sure want to deallocate "+certs['cert'].certLots.certTypes.certDesc+"("+ +certs['certNo']+")?", function(result) {
			if(result){
				$('#myPleaseWait').modal({
					backdrop: 'static',
					keyboard: true
				});
				$.ajax({
					type: 'GET',
					url:  'deallocateRiskCert/' + certs['pcId'],
					dataType: 'json',
					async: true,
					success: function(result) {
						$('#myPleaseWait').modal('hide');
						new PNotify({
							title: 'Success',
							text: 'Certificate deallocated Successfully',
							type: 'success',
							styling: 'bootstrap3'
						});
						$('#cert_tbl').DataTable().ajax.reload();
						$('#polCertsList').DataTable().ajax.reload();

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
		$("#risk-certtype-name").text(certs['subclassCertTypes'].certType.certDesc);
		$("#risk-cert-status").val(certs['status']);
		if(certs['certWef'])
			$("#risk-cert-from-date").val(moment(certs['certWef']).format('DD/MM/YYYY'));
		if(certs['certWet'])
			$("#risk-cert-wet-date").val(moment(certs['certWet']).format('DD/MM/YYYY'));
		if (certs['cancelDate'])
			$("#risk-canc-date").val(moment(certs['cancelDate']).format('DD/MM/YYYY'));
		//console.log("bbbbb" +moment(certs['cancelDate']).format('DD/MM/YYYY'))
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
		console.log(docs);
		window.open(docs['url'],
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
		$("#sect-code-pk").val(section['sectId']);
		$("#sect-limit-amt").val(section['amount']);
		$("#sect-rate").val(section['rate']);
		$("#sect-free-limit").val(section['freeLimit']);
		$("#sect-annual-earning").val(section['annualEarnings']);
		$("#sect-div-fact").val(section['divFactor']);
		$("#sect-multi-rate").val(section['multiRate']);
		//$("#chk-compute").prop("checked", section["compute"]);
		$("#risk-sect-id").val(section['section'].id);
		$("#risk-sect-name").val(section['section'].desc);
		$("#sect-prem-id-pk").val(section['premRates'].id);
		$("#risk-sect-code-pk").val(section['riskId']);
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

	var saveRiskBeneficiaries = function (){
		var $classForm = $('#risk-ben-form');
		var validator = $classForm.validate();
		$('#saveRiskBeneficiary').click(function(){
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
			var url = "saveRiskBeneficiary";
			var request = $.post(url, data );
			request.success(function(){
				$('#myPleaseWait').modal('hide');
				new PNotify({
					title: 'Success',
					text: 'Record Created/Updated Successfully',
					type: 'success',
					styling: 'bootstrap3'
				});
				$('#risk_beneficiaries_tbl').DataTable().ajax.reload();
				validator.resetForm();
				$('#risk-ben-form').find("input[type=text],input[type=number],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden], textarea,select").val("");
				$('#beneficiaryModals').modal('hide');
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
						if(full.binderRules){
							return full.binderRules.desc;
						}
						else if(full.checks)
							return full.checks.checkName;
						else return "";
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


	function initiateRisk(multibinder){
		multibinder = typeof multibinder !== 'undefined' ? multibinder : -2000;
		if(multibinder===-2000) {
			$("#insured-code").val("");
			$("#insured-name").val("");
			$("#insured-other-name").val("");
			$('#insured-frm').select2('val', null);
		}

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

		$("#binder-id").val($("#risk-bind-code").val());
		$("#risk-binder-code-pk").val($("#pol-binder-pk").val());
		populateSubclassLov();
		var fromDate = $("#from-date").val();
		var toDate = $("#wet-date").val();
		$("#risk-wef-date").val(fromDate);
		$("#risk-wet-date").val(toDate);
		$("#section_form_tbl tbody").each(function(){
			$(this).remove();
		});
		$("#btn-add-new-section").hide();
	}

	var newRisk= function(){
		$("#btn-add-risk").on('click', function(){
			initiateRisk();
			$("#risk-form").show();
			$("#risk-div").hide();
			$("#sect-div").hide();
			$("#prem-rates-div").show();
			$("#btn-save-risk").show();
			$("#btn-save-cancel").show();

			$("#myTab #show-taxes,#show-clauses").hide();
		});

		$("#btn-save-cancel").on('click', function(){
			populatePolicyDetails();

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
						getRiskIntParties();
						getScheduleDetails();
						getRiskDocs(-2000);
						$('#section_tbl').DataTable().ajax.reload();
						getRiskCertificates(-2000);
						getRiskBeneficiaries(-2000);
						getRiskSchedules(-2000);
						$("#risk-sched_tbl").DataTable().ajax.reload();
						$('#cert_tbl').DataTable().ajax.reload();
						$('#polCertsList').DataTable().ajax.reload();
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

	var editPolicyRisk= function(button){
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
		$("#insured-name").val(risks["insured"].fname);
		$("#insured-code").val(risks["insured"].tenId);
		$("#insured-other-name").val(risks["insured"].otherNames);
		populateInsuredLov();
		$("#risk-sub-code").val(risks["subclass"].subId);
		$("#sub-name").val(risks["subclass"].subDesc);
		populateSubclassLov();

		$("#risk-cov-code").val(risks["covertype"].covId);
		$("#cover-name").val(risks["covertype"].covName);
		populateCoverTypesLov();
		$("#binder-det-id").val(risks["binderDetails"].detId);
		if(risks["policyBinders"])
			$("#risk-binder-code-pk").val(risks["policyBinders"].policyBindId);
		$("#risk-code-pk").val(risks["riskId"]);
		$("#risk-trans-type").val(risks["transType"]);
		$("#risk-ident-code").val(risks["riskIdentifier"]);
		$("#install-perc").val(risks["installmentPerc"]);
		$("#install-amt").val(risks["installAmount"]);
		$("#install-no").val(risks["installmentNo"]);
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
				data: {"detId": $("#risk-det-id-pk").val(), "riskId": $("#risk-code-pk").val(), "secName": sectdesc,"age":$("#insured-client-age").val()},
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
							if (result[res].section && result[res].section.type === "RD") {
								markup = "<tr><td><input type='checkbox' class='section-check' id='" + result[res].id + "'></td><td><input type='hidden' class='section form-control' value='" + result[res].section.id +
									"'><input type='hidden' class='ratesApplicable form-control' value='" + result[res].ratesApplicable + "'><input type='hidden' class='premId form-control' value='" + result[res].id + "'>" + result[res].section.desc + "</td><td>" + ' <input type="text" class="amount form-control" readonly>' +
									"</td><td><input type='text' class='rate form-control' readonly value='" + result[res].rate + "'></td><td><input type='text' class='divFactor form-control'  readonly value='" + result[res].divFactor + "'></td></td><td>" +
									"<input type='text' class='freeLimit form-control' readonly  value='" + result[res].freeLimit + "'></td></tr>";
							}
							else {
								markup = "<tr><td><input type='checkbox' class='section-check' id='" + result[res].id + "'></td><td><input type='hidden' class='section form-control' value='" + result[res].section.id +
									"'><input type='hidden' class='ratesApplicable form-control' value='" + result[res].ratesApplicable + "'><input type='hidden' class='premId form-control' value='" + result[res].id + "'>" + result[res].section.desc + "</td><td>" + ' <input type="text" class="amount form-control">' +
									"</td><td><input type='text' class='rate form-control' readonly value='" + result[res].rate + "'></td><td><input type='text' class='divFactor form-control'  readonly value='" + result[res].divFactor + "'></td></td><td>" +
									"<input type='text' class='freeLimit form-control' readonly  value='" + result[res].freeLimit + "'></td></tr>";
							}
						}
						else {
							if (result[res].ratesApplicable && result[res].ratesApplicable === "Y") {
								markup = "<tr><td><input type='checkbox' class='section-check' id='" + result[res].id + "'></td><td><input type='hidden' class='section form-control' value='" + result[res].section.id +
									"'><input type='hidden' class='ratesApplicable form-control' value='" + result[res].ratesApplicable + "'><input type='hidden' class='premId form-control' value='" + result[res].id + "'>" + result[res].section.desc + "</td><td>" + ' <input type="text" class="amount form-control" value="' + $("#insured-client-age").val() + '" readonly>' +
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
							if (result[res].section && result[res].section.type === "RD") {
								markup = "<tr><td><input type='checkbox' class='section-check' id='" + result[res].id + "'></td><td><input type='hidden' class='section form-control' value='" + result[res].section.id +
									"'><input type='hidden' class='ratesApplicable form-control' value='" + result[res].ratesApplicable + "'><input type='hidden' class='premId form-control' value='" + result[res].id + "'>" + result[res].section.desc + "</td><td>" + ' <input type="text" class="amount form-control" required readonly>' +
									"</td><td><input type='text' class='rate form-control' readonly value='" + result[res].rate + "'></td><td><input type='text' class='divFactor form-control'  readonly value='" + result[res].divFactor + "'></td></td><td>" +
									"<input type='text' class='freeLimit form-control' readonly  value='" + result[res].freeLimit + "'></td></tr>";
							}
							else {
								markup = "<tr><td><input type='checkbox' class='section-check' id='" + result[res].id + "'></td><td><input type='hidden' class='section form-control' value='" + result[res].section.id +
									"'><input type='hidden' class='ratesApplicable form-control' value='" + result[res].ratesApplicable + "'><input type='hidden' class='premId form-control' value='" + result[res].id + "'>" + result[res].section.desc + "</td><td>" + ' <input type="text" class="amount form-control" required>' +
									"</td><td><input type='text' class='rate form-control' readonly value='" + result[res].rate + "'></td><td><input type='text' class='divFactor form-control'  readonly value='" + result[res].divFactor + "'></td></td><td>" +
									"<input type='text' class='freeLimit form-control' readonly  value='" + result[res].freeLimit + "'></td></tr>";
							}
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
				sort : 'subclasscertId',
				change: function(e, a, v){
					$("#subcls-cert-id").val(e.added.subclasscertId);
				},
				formatResult : function(a)
				{
					return a.certDesc;
				},
				formatSelection : function(a)
				{
					return a.certDesc;
				},
				initSelection: function (element, callback) {

				},
				id: "subclasscertId",
				width:"250px",
				params: {riskId: riskCode},
				placeholder:"Select Cert Type"

			});
		}
	}

	//
	//var createCertypeLov= function(riskCode){
	//	if($("#risk-cert-div").filter("div").html() != undefined)
	//	{
	//		Select2Builder.initAjaxSelect2({
	//			containerId : "risk-cert-div",
	//			sort : 'brnCertId',
	//			change: function(e, a, v){
	//				$("#risk-cert-id").val(e.added.brnCertId);
	//			},
	//			formatResult : function(a)
	//			{
	//				return a.certLots.certTypes.certDesc;
	//			},
	//			formatSelection : function(a)
	//			{
	//				return a.certLots.certTypes.certDesc;
	//			},
	//			initSelection: function (element, callback) {
	//
	//			},
	//			id: "brnCertId",
	//			width:"250px",
	//			params: {riskId: riskCode},
	//			placeholder:"Select Cert Type"
	//
	//		});
	//	}
	//}


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
				$('#polCertsList').DataTable().ajax.reload();
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
				$('#polCertsList').DataTable().ajax.reload();
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
							' <option value="S">Reinstate</option>'+
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
			//if(combo.val()==="R"){
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
					data: {"activeRiskCode": data.arId,"endorseType": combo.val()},
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
			//}
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
			$('#myPleaseWait').modal({
				backdrop: 'static',
				keyboard: true
			});
			var $btn = $(this).button('Saving');
			var data = {};
			$paymodesForm.serializeArray().map(function(x){data[x.name] = x.value;});
			var url = "createPolicyRemarks";
			data.remarks = $("#poli-remarks").val();
			var request = $.post(url, data );

			request.success(function(){
				$('#myPleaseWait').modal('hide');
				new PNotify({
					title: 'Success',
					text: 'Record created/updated Successfully',
					type: 'success',
					styling: 'bootstrap3'
				});
				getPolicyRemakrs();

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
			$('#riskscheduleModal').modal({
				backdrop: 'static',
				keyboard: true
			});
		})
	}





	var saveRiskSchedules= function(){
		console.log('entering the function')
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
				$('#schedule_details_tbl').DataTable().ajax.reload();
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
			if ($("#risk-code-pk").val() != '') {
				$("#new-part-risk-id").val($("#risk-code-pk").val());
				getNewInterestedParties($("#risk-code-pk").val());
				$('#intPartiesModal').modal({
					backdrop: 'static',
					keyboard: true
				});
			}
			else {
				bootbox.alert("Select Risk to Add Interested Parties")
			}
		});

		$("#saveNewIntParties").click(function () {
			var items = getCreateNewIntParties();
			if(items.length==0){
				bootbox.alert("No Interested Party Selected")
				return;
			}
			var $currForm = $('#new-intparties-form');
			var currValidator = $currForm.validate();
			if (!$currForm.valid()) {
				return;
			}
			var data = {};
			$currForm.serializeArray().map(function(x){data[x.name] = x.value;});
			var url = "createNewIntParties";

			data.parties = items;
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
							text: 'Selected Interested Parties added Successfully',
							type: 'success',
							styling: 'bootstrap3'
						});
						$('#interested_parties_tbl').DataTable().ajax.reload();
						$('#intPartiesModal').modal('hide');
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


	var getNewInterestedParties= function(riskId){

		$('#myPleaseWait').modal({
			backdrop: 'static',
			keyboard: true
		});
		$.ajax({
			type: 'GET',
			url:  'getNewIntParties/'+riskId,
			dataType: 'json',
			async: false,
			success: function(result) {
				$('#myPleaseWait').modal('hide');
				$("#interested_party_tbl tbody").each(function(){
					$(this).remove();
				});
				for(var res in result){
					var markup = "<tr><td><input type='checkbox' class='int-check'><input type='hidden' class='int-part-id form-control' value='"+result[res].partCode+
						"'></td><td>"
						+ result[res].partName + "</td><td>"
						+result[res].partType +"</td>"
						+"<td>"+result[res].pinNumber +"</td>"
						+"<td>"+result[res].regNo +"</td>" +
						"</tr>";
					$("#interested_party_tbl").append(markup);
				}

			},
			error: function(jqXHR, textStatus, errorThrown) {
				$('#myPleaseWait').modal('hide');
			}
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

	var  creditTrans =function(){
		var url = "debitreceipts";
		var currTable = $('#polReceipts')
			.DataTable(
				{
					"processing" : true,
					"serverSide" : true,
					"ajax": {
						'url': url
					},
					lengthMenu : [ [ 10 ], [ 10] ],
					pageLength : 10,
					destroy : true,
					"columns" : [
						{
							"data" : "receipt",
							"render" : function(data, type, full, meta) {
								return full.receipt.receiptNo;
							}
						},
						{
							"data" : "receipt",
							"render" : function(data, type, full, meta) {
								return moment(full.receipt.receiptDate).format('DD/MM/YYYY');
							}
						},
						{
							"data" : "rctAmount",
							"render" : function(data, type, full, meta) {
								return UTILITIES.currencyFormat(full.rctAmount);
							}
						},

					]
				});

		return currTable;

	}



	var getClientDocs= function(clientId){
		console.log("Client id "+clientId);
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
	var getClientAge = function(){
		if($("#insured-code").val() && $("#pol-bind-age-appli").val() && $("#pol-bind-age-appli").val()==="Y"){
			$.ajax({
				type: 'GET',
				url:  'getClientAge',
				dataType: 'json',
				data: {"clientId": $("#insured-code").val()},
				async: true,
				success: function(result) {
					console.log('Msee ako '+result+' Yrs Old');
					$("#insured-client-age").val(result);
				},
				error: function(jqXHR, textStatus, errorThrown) {
				}
			});
		}
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
								return '<button type="button" class="btn btn-danger btn btn-info btn-xs" data-parties='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.deleteRiskParties(this);"><i class="fa fa-trash-o"></button>';
							else
								return '<button type="button" class="btn btn-danger btn btn-info btn-xs" data-parties='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.deleteRiskParties(this);" disabled><i class="fa fa-trash-o"></button>';

						}
						else
							return '<button type="button" class="btn btn-danger btn btn-info btn-xs" data-parties='+encodeURI(JSON.stringify(full)) + ' onclick="UWScreen.deleteRiskParties(this);"><i class="fa fa-trash-o"></button>';
					}

				},
			]
		}) );
		return currTable;
	};

	var init = function(){
		$(".motor-disp").show();
		$(".non-motor-disp").hide();
		populatePolicyDetails();
		$('#overrid-prem').number( true, 2 );
		$('#install-amt').number( true, 2 );
		$(".datepicker-input").each(function() {
			$(this).datetimepicker({
				format: 'DD/MM/YYYY'
			});

		});
		$(".multi-product-uw").css("display","none");
		populateClientLov();
		populateBinderLov();
		$("#comm-rate").attr("readonly", "false");
		$(".chkimport-risks").show();
		$(".risk-detail-tab").show();
		$(".bind-insurance").show();
		$(".multi-product").css("display","none");
		$(".multi-product-uw").css("display","none");
		// $("#pol-bin-type").on('change', function(){
		//
		// 	if($(this).val()==="B"){
		// 		$("#comm-rate").attr("readonly", "true");
		// 		$(".chkimport-risks").show();
		// 		$(".risk-detail-tab").show();
		// 		$(".bind-insurance").show();
		// 		$(".multi-product").css("display","none");
		// 		$(".multi-product-uw").css("display","none");
		// 	}
		// 	else if($(this).val()==="MP"){
		// 		$(".risk-detail-tab").hide();
		// 		$("#comm-rate").attr("readonly", "true");
		// 		$(".chkimport-risks").hide();
		// 		$(".bind-insurance").hide();
		// 		$(".multi-product").css("display","block");
		// 		$(".multi-product-uw").css("display","none");
		// 		populateMultiProduct();
		// 	}
		// 	else{
		// 		// populateBinderLov();
		// 		// $("#comm-rate").attr("readonly", "false");
		// 		// $(".chkimport-risks").show();
		// 		// $(".risk-detail-tab").show();
		// 		// $(".bind-insurance").show();
		// 		// $(".multi-product").css("display","none");
		// 		// $(".multi-product-uw").css("display","none");
		//
		// 	}
		//
		// });

		$("#pol-interface-type").on('change', function() {
			if ($(this).val() === "A") {
				$(".installment").hide();
			}
			else{
				$(".installment").show();
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

		$("#risk-cert-status").on('change', function(){
			if($(this).val()==="C"){
				var d = new Date();
				$("#risk-canc-date").val(moment(d).format('DD/MM/YYYY'));
			}
			else{
				$("#risk-canc-date").val("");
			}
		})

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

			if($(this).val()==="IN"){
				$("#email-cc").attr("readonly",true);
				$("#email-send-to").attr("readonly",false);
				$("#email-send-to").val("");
				$.ajax({
					type: 'GET',
					url:  'getInhouseEmail',
					dataType: 'json',
					async: true,
					success: function(result) {
						$("#email-cc").val(result);
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
				$("#email-cc").attr("readonly",false);
				$("#email-send-to").attr("readonly",true);
				$("#email-cc").val("");
			}
		});

		populateCurrencyLov();
		populateSubAgentsLov();
		// populatePaymentModes();
		populateUserBranches();
		populateInsuredLov();
		populateSubclassLov();
		populateCoverTypesLov();
		uploadRiskBeneficiries();
		//$("#other-pol-details").hide();
		getPolicyWet();
		changePolicyWetDt();
		createNewPolicy();
		saveRiskSections();
		saveRiskBeneficiaries();
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

		//validateRisk();
		//createCertTable();
		UTILITIES.createAssignee();
		UTILITIES.emailReports();
		uploadRiskDocument();
		getNewIntParties();
		fillQuestionnaire();
		importuWRisks();
		saveRiskDocsList();
		printCerts.printCerts();
		creditTrans();
		$("#btn-add-beneficiary").click(function(){
			if($("#risk-code-pk").val() != ''){
				$('#salary').number( true, 2 );
				$("#risk-ben-code-pk").val($("#risk-code-pk").val());
				$('#beneficiaryModals').modal({
					backdrop: 'static',
					keyboard: true
				})
			}
		});


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

		$("#btn-add-del-sched").on('click', function(){

		});

		$("#btn-add-edit-sched").on('click', function(){
			if($("#schedule-pk-id").val() !== '') {
				$('#riskscheduleModal').modal('show');
			}else{
				bootbox.alert("Select Schedule to Edit Details");
				return;
			}
		})


	}

	return {
		init: init,
		editRiskCerts:editRiskCerts,
		deleteRiskCerts:deleteRiskCerts,
		editRiskBeneficiary:editRiskBeneficiary,
		deleteRiskBeneficiary:deleteRiskBeneficiary,
		allocateRiskCerts:allocateRiskCerts,
		deallocateRiskCerts:deallocateRiskCerts,
		editRiskSection:editRiskSection,
		deleteRiskSection:deleteRiskSection,
		deleteRiskParties:deleteRiskParties,
		editRiskDocs:editRiskDocs,
		downloadRiskDoc:downloadRiskDoc,
		deleteRiskDoc:deleteRiskDoc,
		editPolicyRisk:editPolicyRisk,
		deleteRisk:deleteRisk,
		editPolicyClause:editPolicyClause,
		deletePolicyClause:deletePolicyClause,
		authChecks:authChecks,
		editPolTaxes:editPolTaxes,
		deletePolTaxes:deletePolTaxes,
		downloadClientDoc:downloadClientDoc,
		viewPremLimits:viewPremLimits
	}


})(jQuery,PrintCerts);

jQuery(UWScreen.init);
