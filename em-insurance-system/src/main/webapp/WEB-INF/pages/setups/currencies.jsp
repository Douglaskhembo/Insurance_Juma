<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
		 <div class="x_title">
			 <h4>Currency Details</h4>
		 </div>
<div class="x_content">
	<form id="currform" class="form-horizontal form-label-left">
		    <input type="hidden" name="curCode" id="cur-code">
		<div class="col-md-6 col-xs-12">
			<div class="form-group">
				<label for="brn-id" class="col-md-3 control-label">Currency
					Name<span class="required">*</span></label>

				<div class="col-md-6  col-xs-12">
					<input type="text" class="form-control" id="curr-name"
						name="curName" required>
				</div>
			</div>
			<div class="form-group">
				<label for="enabled" class="col-md-3 control-label">
					Active</label>

				<div class="col-md-6  col-xs-12 checkbox">
				    <label>
                             <input type="checkbox" id="enabled" name="enabled" >
                            </label>

				</div>
			</div>
		</div>
		<div class="col-md-6 col-xs-12">
			<div class="form-group">
				<label for="fraction" class="col-md-3 control-label">Fraction</label>

				<div class="col-md-6 col-xs-12">
					<input type="text" class="editUserCntrls form-control"
						id="fraction" name="fraction" required>
				</div>
			</div>
			<div class="form-group">
				<label for="frac-units" class="col-md-3 control-label">Fraction
					Units</label>

				<div class="col-md-6 col-xs-12">
					<input type="number" class="editUserCntrls form-control"
						id="frac-units" name="fractionUnits">
				</div>
			</div>
		</div>
		<div class="col-md-6 col-xs-12">
		<div class="form-group">
			<label for="frac-units" class="col-md-3 control-label">Round
				Off</label>

			<div class="col-md-6 col-xs-12">
				<input type="number" class="editUserCntrls form-control"
					   id="frac-round-off" name="roundOff"
				max="3" min="0">
			</div>
		</div>

			<div class="form-group">
				<label for="curr-symbol" class="col-md-3 control-label">Symbol<span class="required">*</span></label>

				<div class="col-md-6 col-xs-12">
					<input type="text" class="editUserCntrls form-control"
						id="curr-symbol" name="curIsoCode" required>
				</div>
			</div>
		</div>
		<div class="col-md-12 col-xs-12">
			<hr>
		</div>
		<div class="col-md-6 col-xs-12">
                      <div class="form-group">
                      <div class="col-md-6 col-sm-6 col-xs-12 col-md-offset-3">
		<button type="button" class="btn btn-default" id="newCurrency">New</button>
			<button data-loading-text="Saving..." id="saveCurrencyBtn"
				type="button" class="btn btn-success">Save</button>
				</div>
			</div>
		</div>
	</form>
	<hr>
	<div class="" role="tabpanel" data-example-id="togglable-tabs">
		<ul id="myTab" class="nav nav-tabs bar_tabs" role="tablist">
			<li role="presentation" class="active"><a href="#tab_content1"
													  id="home-tab" role="tab" data-toggle="tab" aria-expanded="true">Currency List</a>
			</li>
			<li role="presentation" class=""><a href="#tab_content2"
												role="tab" id="profile-tab" data-toggle="tab" aria-expanded="false">Exchange Rates</a>
			</li>
		</ul>
		<div id="myTabContent" class="tab-content">
			<div role="tabpanel" class="tab-pane fade active in"
				 id="tab_content1" aria-labelledby="home-tab">
		 <div class="cutom-container">
	<table id="currencyList" class="table table-hover table-bordered">
		<thead>
			<tr>

				<th>Symbol</th>
				<th>Currency Name</th>
				<th>Active</th>
				<th>Fraction</th>
				<th>Fraction Units</th>
				<th>Round Off</th>
				<th width="5%"></th>
				<th width="5%"></th>
			</tr>
		</thead>
	</table>
			 </div>
			</div>
			<div role="tabpanel" class="tab-pane fade" id="tab_content2"
				 aria-labelledby="profile-tab" >
				<div class="cutom-container">
					<button type="button" class="btn btn-success btn btn-info pull-left" id="btnAddExchangeRate">New</button>
					<table id="currencyRatesList" class="table table-hover table-bordered">
						<thead>
						<tr>

							<th>Rate</th>
							<th>Exchange Date</th>
							<th>Created Date</th>
							<th width="5%"></th>
							<th width="5%"></th>
						</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
</div>

	<div class="modal fade" id="exchangeRatesModal" tabindex="-1" role="dialog"
		 aria-labelledby="exchangeRatesModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="exchangeRatesModalLabel">
						Edit/Add Exchange Rate
					</h4>
				</div>
				<div class="modal-body" id="branch_model">
					<form id="exchange-form" class="form-horizontal">
						<input type="hidden" class="form-control" id="ceCode" name="ceCode">
						<input type="hidden" name="baseCurCode" id="curCode">
						<div class="form-group">
							<label for="curr-frm" class="col-md-3 control-label">Currency</label>
							<div class="col-md-8">
								<input type="hidden" id="cur-id" name="curCode"/>
								<input type="hidden" id="cur-name">
								<div id="curr-frm" class="form-control"
									 select2-url="<c:url value="/protected/uw/policies/othercurrencies"/>" >

								</div>
							</div>
						</div>
						<div class="form-group">
							<label for="rate" class="col-md-3 control-label">Rate</label>

							<div class="col-md-8">
								<input type="text" class="editUserCntrls form-control"
									   id="rate" name="rate"
									   required>
							</div>
						</div>
						<div class="form-group">
							<label for="exchange-date" class="col-md-3 control-label">Exchange Date</label>

							<div class="col-md-8">
								<div class='input-group date datepicker-input'>
									<input class="form-control pull-right"
												name="exchangeDate" id="exchange-date"/>
									<div class="input-group-addon">
										<span class="glyphicon glyphicon-calendar"></span>
									</div>
								</div>
							</div>
						</div>

					</form>
				</div>
				<div class="modal-footer">
					<button data-loading-text="Saving..." id="saveExchangeBtn"
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
	<script type="text/javascript" src="<c:url value="/js/modules/utils/select2builder.js"/>"></script>
<script>
	$(function() {

		$(document).ready(function () {
			createCurrencyTable();
			saveUpdateCurrency();
			saveUpdateCurrencyRate();
			createCurrencyRatesTable(-2000);
			$(".datepicker-input").each(function() {
				$(this).datetimepicker({
					format: 'MM/DD/YYYY'
				});
			});
			$("#newCurrency").on("click", function(){
				$('#currform').find("input[type=text],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden],input[type=number], textarea").val("");
				$('#currform').find("input[type=checkbox]").attr("checked", false);
			});

			$("#btnAddExchangeRate").on('click', function (){
				if ($("#cur-code").val() === '') {
					bootbox.alert("Select Currency to Add Exchange Rate...");
					return;
				}
				$('#rate').number( true, 2 );
				$(".datepicker-input").each(function() {
					$(this).datetimepicker({
						format: 'MM/DD/YYYY'
					});
				});
				$('#exchange-form').find("input[type=text],input[type=number],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden], textarea,select").val("");
				$("#curCode").val($("#cur-code").val());
				$('#exchangeRatesModal').modal({
					backdrop: 'static',
					keyboard: true
				})
			});
			$('#profile-tab').addClass('btn disabled');

			if($("#curr-frm").filter("div").html() !== undefined)
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

					},
					id: "curCode",
					width:"250px",
					placeholder:"Select Currency"

				});
			}
		});
	});


	function saveUpdateCurrencyRate(){
		var $currForm = $('#exchange-form');
		var currValidator = $currForm.validate();
		$('#saveExchangeBtn').click(function(){
			if (!$currForm.valid()) {
				return;
			}
			var $btn = $(this).button('Saving');
			var data = {};
			$currForm.serializeArray().map(function(x){data[x.name] = x.value;});
			console.log(data);
			var url = "createExchangeRate";
			var request = $.post(url, data );
			request.success(function(){
				new PNotify({
					title: 'Success',
					text: 'Record created/updated Successfully',
					type: 'success',
					styling: 'bootstrap3'
				});
				$('#currencyRatesList').DataTable().ajax.reload(null,false);
				currValidator.resetForm();
				$('#exchange-form').find("input[type=text],input[type=mobileNumber],input[type=number],input[type=emailFull],input[type=password],input[type=hidden], textarea").val("");
				$('#exchange-form').find("input[type=checkbox]").attr("checked", false);
				$('#profile-tab').addClass('btn disabled');
				$('#exchangeRatesModal').modal('hide');

			});
			request.error(function(jqXHR, textStatus, errorThrown){
				bootbox.alert(jqXHR.responseText);
			});
			request.always(function(){
				$btn.button('reset');
			});
		});
	}

	function saveUpdateCurrency(){
		var $currForm = $('#currform');
		var currValidator = $currForm.validate();
		$('#saveCurrencyBtn').click(function(){
			if (!$currForm.valid()) {
				return;
			}
			var $btn = $(this).button('Saving');
			var data = {};
			$currForm.serializeArray().map(function(x){data[x.name] = x.value;});
			var url = "createCurrency";
			var request = $.post(url, data );
			request.success(function(){
				new PNotify({
					title: 'Success',
					text: 'Record created/updated Successfully',
					type: 'success',
					styling: 'bootstrap3'
				});
				$('#currencyList').DataTable().ajax.reload();
				currValidator.resetForm();
				$('#currform').find("input[type=text],input[type=mobileNumber],input[type=number],input[type=emailFull],input[type=password],input[type=hidden], textarea").val("");
				$('#currform').find("input[type=checkbox]").attr("checked", false);
				$('#profile-tab').addClass('btn disabled');

			});
			request.error(function(jqXHR, textStatus, errorThrown){
				bootbox.alert(jqXHR.responseText);
			});
			request.always(function(){
				$btn.button('reset');
			});
		});
	}

	function createCurrencyRatesTable(curCode){
		console.log(curCode);
		var url = "currencyRates/"+curCode;
		var currTable = $('#currencyRatesList').DataTable( {
			"processing": true,
			"serverSide": true,
			"ajax": url,
			lengthMenu: [ [10, 20, 30], [10, 20, 30] ],
			pageLength: 10,
			destroy: true,
			"columns": [
				{ "data": "rate" },
				{ "data": "exchangeDate",
					"render": function ( data, type, full, meta ) {
						 return full.exchangeDate;
					}
				},
				{ "data": "createdDate",
					"render": function ( data, type, full, meta ) {
						if (full.createdDate)
							return moment(full.createdDate).format('DD/MM/YYYY');
						else return "";
					}
				},
				{
					"data": "ceCode",
					"render": function ( data, type, full, meta ) {
						return '<button class="btn btn-success btn btn-info btn-xs" type="button" data-currency='+encodeURI(JSON.stringify(full)) + ' value="Edit" onclick="editCurrency(this);"><i class="fa fa-pencil-square-o"></button>';
					}

				},
				{
					"data": "ceCode",
					"render": function ( data, type, full, meta ) {
						return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-currency='+encodeURI(JSON.stringify(full)) + ' value="Delete" onclick="confirmCurrencyDelete(this);"><i class="fa fa-trash-o"></button>';
					}

				},
			]
		} );
		return currTable;
	}


	function createCurrencyTable(){
		var url = "currencies";
		var currTable = $('#currencyList').DataTable( {
			"processing": true,
			"serverSide": true,
			"ajax": url,
			lengthMenu: [ [10, 20, 30], [10, 20, 30] ],
			pageLength: 10,
			destroy: true,
			"columns": [
				{ "data": "curIsoCode" },
				{ "data": "curName" },
				{ "data": "enabled" },
				{ "data": "fraction" },
				{ "data": "fractionUnits" },
				{ "data": "roundOff" },
				{
					"data": "curCode",
					"render": function ( data, type, full, meta ) {
						return '<button class="btn btn-success btn btn-info btn-xs" type="button" data-currency='+encodeURI(JSON.stringify(full)) + ' value="Edit" onclick="editCurrency(this);"><i class="fa fa-pencil-square-o"></button>';
					}

				},
				{
					"data": "curCode",
					"render": function ( data, type, full, meta ) {
						return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-currency='+encodeURI(JSON.stringify(full)) + ' value="Delete" onclick="confirmCurrencyDelete(this);"><i class="fa fa-trash-o"></button>';
					}

				},
			]
		} );
		$('#currencyList tbody').on( 'click', 'tr', function () {
			$(this).addClass('active').siblings().removeClass('active');
			var aData = currTable.rows('.active').data();
			if (aData[0] === undefined || aData[0] === null) {

			} else {
				createCurrencyRatesTable(aData[0].curCode);
			}
		});
		return currTable;
	}

	function editCurrency(button){
		var currency = JSON.parse(decodeURI($(button).data("currency")));
		$("#curr-name").val(currency["curName"]);
		if(currency["enabled"])
			$("#enabled").prop("checked", currency["enabled"]);
		else
			$("#enabled").prop("checked", false);
		$("#fraction").val(currency["fraction"]);
		$("#frac-units").val(currency["fractionUnits"]);
		$("#frac-round-off").val(currency["roundOff"]);
		$("#curr-symbol").val(currency["curIsoCode"]);
		$("#cur-code").val(currency["curCode"]);
		$('#profile-tab').removeClass('btn disabled');
	}


	function confirmCurrencyDelete(button){
		var currency = JSON.parse(decodeURI($(button).data("currency")));
		bootbox.confirm("Are you sure want to delete "+currency["curName"]+"?", function(result) {
			if(result){
				$.ajax({
					type: 'GET',
					url:  'deleteCurrency/' + currency["curCode"],
					dataType: 'json',
					async: true,
					success: function(result) {
						new PNotify({
							title: 'Success',
							text: 'Record Deleted Successfully',
							type: 'success',
							styling: 'bootstrap3'
						});
						$('#currencyList').DataTable().ajax.reload();
						$('#profile-tab').addClass('btn disabled');
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


</script>