<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript" src="<c:url value="/js/modules/utils/select2builder.js"/>"></script>
<script type="text/javascript" src="<c:url value="/libs/rivets/rivets.js"/>"></script>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div class="x_panel" id="acct_model">
     <div class="x_title">
	<h4>Intermediaries List</h4>
	</div>
	 <a href="<c:url value='/protected/setups/acctsform'/> " class="btn btn-info pull-right">New</a>

	<div class="cutom-container">
	<table id="acctbl" class="table table-hover table-bordered">
		<thead>
			<tr>
                <th>Code</th>
				<th>Intermediary Name</th>
				<th>Phone</th>
				<th>Pin No</th>
				<th>Status</th>
				<th>Modified By</th>
				<th>Intermediary Type</th>
				<th width="5%"></th>
				<th width="5%"></th>
			</tr>
		</thead>
	</table>
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



		var createAccounts = function(){
			var url = "allaccounts";
			return $('#acctbl').DataTable(UTILITIES.extendsOpts({
				"ajaxUrl": url,
				"columns": [
					{"data": "shtDesc"},
					{"data": "name"},
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
							else if (full.status === "D")
								return "Draft";
						}
					},
					{"data": "updatedBy"},
					{"data": "accountTypeName"},
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




		var init = function(){
			createAccounts();
		};

		return {
			init: init
		};

	})(jQuery);
</script>