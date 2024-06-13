<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript"
	src="<c:url value="/js/modules/utils/select2builder.js"/>"></script>
<script type="text/javascript"
	src="<c:url value="/libs/rivets/rivets.js"/>"></script>
<script type="text/javascript"
	src="<c:url value="/js/modules/classes/classes.js"/>"></script>
<script>
	var requestContextPath = '${pageContext.request.contextPath}';
</script>
<div class="x_panel">
	<div class="x_title">
		<h2>Classes Definition</h2>
		<ul class="nav navbar-right panel_toolbox">
			<li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
			</li>
		</ul>
		<div class="clearfix"></div>
	</div>
	<div class="x_content">
		<form class="form-horizontal">
			<div class="form-group">
				<label for="brn-id" class="col-md-3 control-label">Select
					Class</label>

				<div class="col-md-4">
					<input type="hidden" id="accId" name="accId"
						rv-value="accounts.accType.accId" />

					<div id="class-def" class="form-control"
						select2-url="<c:url value="/protected/setups/classes/classesselect"/>">

					</div>
					<input type="hidden" id="class-pk">

				</div>
				<div class="col-md-4">
					<button type="button" class="btn btn-info" id="btn-add-classes">New</button>
					<button type="button" class="btn btn-info" id="btn-edit-classes">Edit</button>
				</div>
			</div>

		</form>

	</div>
</div>

<div class="x_panel">
	<div class="x_title">
		<h2>Sub Classes</h2>
		<ul class="nav navbar-right panel_toolbox">
			<li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
			</li>
		</ul>
		<div class="clearfix"></div>
	</div>
	<div class="x_content">
		<input type="hidden" id="sub-tb-class-pk">
		<button type="button" class="btn btn-info pull-right"
			id="btn-add-subclass">New</button>
		<div class="cutom-container">
		<table id="subclassList" class="table table-hover table-bordered">
			<thead>
				<tr>

					<th>Sub Class ID</th>
					<th>Sub Class Name</th>
					<th>Risk ID Format</th>
					<th>Risk Unique?</th>
					<th>Active?</th>
					<th width="5%"></th>
					<th width="5%"></th>
				</tr>
			</thead>
		</table>
			</div>
	</div>
</div>
<div class="x_panel">
	<div class="x_title">
		<h2>
			Sub Class Covertypes and Premium Items
		</h2>
		<ul class="nav navbar-right panel_toolbox">
			<li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
			</li>
		</ul>
		<div class="clearfix"></div>
	</div>
	<div class="x_content">


		<div class="" role="tabpanel" data-example-id="togglable-tabs">
			<ul id="myTab" class="nav nav-tabs bar_tabs" role="tablist">
				<li role="presentation" class="active"><a href="#tab_content1"
					id="home-tab" role="tab" data-toggle="tab" aria-expanded="true">Cover Types</a>
				</li>
				<li role="presentation" class=""><a href="#tab_content2"
					role="tab" id="profile-tab" data-toggle="tab" aria-expanded="false">Premium Items</a>
				</li>
			</ul>
			<div id="myTabContent" class="tab-content">
				<div role="tabpanel" class="tab-pane fade active in"
					id="tab_content1" aria-labelledby="home-tab">
					<input type="hidden" id="sub-cov-attach-pk">
					<button type="button" class="btn btn-info pull-right"
						id="btn-add-sub-cov">New</button>
					<div class="cutom-container">
					<table id="subcovList" class="table table-hover table-bordered">
						<thead>
							<tr>
			
								<th>Cover Type Id</th>
								<th>Cover Type Name</th>
								<th>Default?</th>
								<th>Min Prem</th>
								<th width="5%"></th>
								<th width="5%"></th>
							</tr>
						</thead>
					</table>
						</div>
					<div class="x_title">
		<h2>Cover Types Premium Items</h2>
		<ul class="nav navbar-right panel_toolbox">
			<li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
			</li>
		</ul>
		<div class="clearfix"></div>
	</div>
	<div class="x_content">
		<button type="button" class="btn btn-info pull-right"
			id="btn-add-covt_sections">New</button>
		<table id="covttypesections" class="table table-hover table-bordered">
			<thead>
				<tr>

					<th>Premium Id</th>
					<th>Premium Item Name</th>
					<th>Mandatory?</th>
					<th width="5%"></th>
					<th width="5%"></th>
				</tr>
			</thead>
		</table>
	</div>
				</div>
				<div role="tabpanel" class="tab-pane fade" id="tab_content2"
					aria-labelledby="profile-tab">
					<button type="button" class="btn btn-info pull-right"
						id="btn-add-sub-sec">New</button>
					<div class="cutom-container">
					<table id="subsecList" class="table table-hover table-bordered">
						<thead>
							<tr>
			
								<th>Premium Item Id</th>
								<th>Premium Item Name</th>
								<th>Premium Item Type</th>
								<th>Active?</th>
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
</div>


<jsp:include page="classmodals/modals.jsp"></jsp:include>