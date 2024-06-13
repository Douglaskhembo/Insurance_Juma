<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript" src="<c:url value="/js/modules/clients/clients.js"/>"></script>
<div class="x_panel">
  <div class="x_title">
	<h4>Clients List</h4>
	</div>
	 
	<form id="search-form" class="form-horizontal">
						<div class="form-group">
						<div class="col-md-6 col-xs-12">
								<label for="pass-search-name" class="col-md-5 control-label">Search Value
									</label>

								<div class="col-md-7 col-xs-12">
									<input type='text' class="form-control pull-right"
										id="pass-search-name" />
								</div>
							</div>
						</div>
						<div class="form-group">
							<input type="button" class="btn btn-primary pull-right"
								style="margin-right: 10px;" value="Search"
								id="btn-search-client">
						</div>


					</form>
	</div>
	
	<div class="x_panel">
					<a href="<c:url value='/protected/clients/setups/clientsform'/> " class="btn btn-primary pull-right">New</a>
		<div class="cutom-container">
	<table id="tenTbl" class="table table-hover table-bordered">
		<thead>
			<tr>
                <th>Client ID</th>
				<th>Name</th>
				<th>Id/Passport No</th>
				<th>Email</th>
				<th>Phone</th>
				<th>Client Type</th>
				<th>Status</th>
				<th>Created On</th>
				<th>Created By</th>
				<th>Approved?</th>
				<th width="5%"></th>
				<th width="5%"></th>
			</tr>
		</thead>
	</table>
			</div>
	</div>

<script>
	$(document).ready(function (){
		$("#btn-search-client").on('click', function(){
			var searchType = $("#searchType").val();
			obj = {
				'search':  $("#pass-search-name").val(),
			};
			createTenantsList(obj);
		});
	})


	function createTenantsList(searchObj){
		var url = "tenants";
		return $('#tenTbl').DataTable({
			"processing": true,
			"serverSide": true,
			autoWidth: true,
			"searching": false,
			"ajax": {
				'url': url,
				'data': searchObj,
			},
			lengthMenu: [[10, 15], [10, 15]],
			pageLength: 10,
			destroy: true,
			"columns": [
				{"data": "tenantNumber"},
				{"data": "clientName"},
				{"data": "idNo"},
				{"data": "emailAddress"},
				{"data": "phoneNo"},
				{"data": "clientType"},
				{
					"data": "status",
					"render": function (data, type, full, meta) {
						if (!full.status || full.status === "T") {
							return "Terminated";
						} else if (full.status === "A")
							return "Active";
					}
				},
				{
					"data": "dateCreated",
					"render": function (data, type, full, meta) {
						if (full.dateCreated)
							return moment(full.dateCreated).format('DD/MM/YYYY');
						else return "";
					}
				},
				{
					"data": "tenId",
					"render": function (data, type, full, meta) {
						if (full.username)
							return full.username;
						else return "";
					}

				},
				{
					"data": "tenId",
					"render": function (data, type, full, meta) {
						if (full.authStatus)
							return full.authStatus;
						else return "";
					}

				},
				{
					"data": "tenId",
					"render": function (data, type, full, meta) {
						var id = (full.hashCode)?full.hashCode:full.tenId;
						return "<a href="+SERVLET_CONTEXT+"/protected/clients/setups/editClients/"+id+" class='btn btn-primary btn btn-primary btn-xs'>View</a>";
					}

				},
				{
					"data": "tenId",
					"render": function ( data, type, full, meta ) {
						return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-clients='+encodeURI(JSON.stringify(full)) + ' value="Delete" onclick="deleteClient(this);"><i class="fa fa-trash-o"></button>';
					}

				},
			]
		});
	}



	function deleteClient(button){
		var clients = JSON.parse(decodeURI($(button).data("clients")));
		bootbox.confirm("Are you sure want to delete the client?", function(result) {
			if(result){
				$('#myPleaseWait').modal({
					backdrop: 'static',
					keyboard: true
				});
				$.ajax({
					type: 'GET',
					url:  'deleteClient/' + clients['tenId'],
					dataType: 'json',
					async: true,
					success: function(result) {
						$('#myPleaseWait').modal('hide');
						new PNotify({
							title: 'Success',
							text: 'Client Deleted Successfully',
							type: 'success',
							styling: 'bootstrap3'
						});
						$('#tenTbl').DataTable().ajax.reload();
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
</script>