<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript" src="<c:url value="/js/modules/utils/select2builder.js"/>"></script>
<script type="text/javascript" src="<c:url value="/libs/rivets/rivets.js"/>"></script>
<script type="text/javascript"
	src="<c:url value="/js/modules/binder/binder.js"/>"></script>
<div class="x_panel">
   <div class="x_title">
		<h2>Contract Definition</h2>
		<ul class="nav navbar-right panel_toolbox">
			<li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
			</li>
		</ul>
		<div class="clearfix"></div>
	</div>
	<div class="x_content">
	<form id="prg-grp-form" class="form-horizontal">
	    <div class="form-group form-required">
				<div class="col-md-6">
				   <label for="brn-id" class="col-md-5 control-label">
					Insurance Company</label>

				<div class="col-md-7">
		                     <input type="hidden" id="acc-id" rv-value="account.acctId"/>
		                     <input type="hidden" id="acc-name">
		                        <div id="acc-frm" class="form-control" 
				                                 select2-url="<c:url value="/protected/setups/selParentAccts"/>" >
				                                 
				               </div> 
				               
				</div>
				</div>   
				
				</div>
			</form>
			</div>
		<div class="x_panel">
   <div class="x_title">
		<h2>Contracts</h2>
		<ul class="nav navbar-right panel_toolbox">
			<li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
			</li>
		</ul>
		<div class="clearfix"></div>
	</div>
	<div class="x_content">
         <input type="hidden" id=binder-sel-pk>
		<button type="button" class="btn btn-info" id="btn-add-binder">New</button>
		<div class="spacer"></div>
		<div class="cutom-container">
		<table id="binderList" class="table table-hover table-bordered">
			<thead>
				<tr>
                     <th>Type</th>
					<th>Contract</th>
					<th>Contract Policy No</th>
					<th>Product</th>
					<th>Currency</th>
					<th>Active?</th>
 					<th width="5%"></th>
					<th width="5%"></th>
					<th width="5%"></th>
					<th></th>
                    <th></th>
					<th></th>
					<th></th>
				</tr>
			</thead>
		</table>
			</div>
		</div>
		</div>
		<div class="x_panel">
   <div class="x_title">
		<h2>Contract Details/Rate Tables</h2>
		<ul class="nav navbar-right panel_toolbox">
			<li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
			</li>
		</ul>
		<div class="clearfix"></div>
	</div>
	<div class="x_content">
		<div class="" role="tabpanel" data-example-id="togglable-tabs">
			<ul id="myTab-2" class="nav nav-tabs bar_tabs" role="tablist">
				<li role="presentation" class="active"><a href="#tab_content1-1"
														  id="home-tab-2" role="tab" data-toggle="tab" aria-expanded="true">Contract Details</a>
				</li>
<%--				<li role="presentation" class=""><a href="#tab_content2-2"--%>
<%--													role="tab" id="profile-tab-2" data-toggle="tab" aria-expanded="false">Rate Tables</a>--%>
<%--				</li>--%>
<%--				<li role="presentation" class=""><a href="#tab_content2-3"--%>
<%--													role="tab" id="profile-tab-3" data-toggle="tab" aria-expanded="false">Contract Rules</a>--%>
<%--				</li>--%>
<%--				<li role="presentation" class=""><a href="#tab_content2-8"--%>
<%--													role="tab" id="profile-tab-8" data-toggle="tab" aria-expanded="false">Exclusions</a>--%>
<%--				</li>--%>
<%--				<li role="presentation" class=""><a href="#tab_content2-9"--%>
<%--													role="tab" id="profile-tab-9" data-toggle="tab" aria-expanded="false">Loadings</a>--%>
<%--				</li>--%>
<%--				<li role="presentation" class=""><a href="#tab_content2-10"--%>
<%--													role="tab" id="profile-tab-10" data-toggle="tab" aria-expanded="false">Provider Panels</a>--%>
<%--				</li>--%>

<%--				<li role="presentation" class=""><a href="#tab_content2-11"--%>
<%--													role="tab" id="profile-tab-11" data-toggle="tab" aria-expanded="false">Medical Card Types</a>--%>
<%--				</li>--%>

<%--				<li role="presentation" class=""><a href="#tab_content2-12"--%>
<%--													role="tab" id="profile-tab-12" data-toggle="tab" aria-expanded="false">Questionnaire</a>--%>
<%--				</li>--%>
			</ul>
			<div id="myTabContent-1" class="tab-content">
				<div role="tabpanel" class="tab-pane fade active in"
					 id="tab_content1-1" aria-labelledby="home-tab-1">
         <input type="hidden" id="binder-det-code-pk">
         <input type="hidden" id="binder-det-sub-code">
		<button type="button" class="btn btn-info btn-dis" id="btn-add-binder-det">New</button>
		<div class="spacer"></div>
					<div class="cutom-container">
		<table id="binderDetList" class="table table-hover table-bordered">
			<thead>
				<tr>
                    <th>Sub Class Id</th>
                    <th>Sub Class Name</th>
					<th>Cover Type Id</th>
					<th>Cover Type Name</th>
					<th>Cover Summary</th>
					<th>Min Prem</th>
					<th>Single Section Cover</th>
					<th>Limits Per Section</th>
					<th>No of Installments</th>
					<th>Installment Percentages</th>
					<th width="5%"></th>
					<th width="5%"></th>
				</tr>
			</thead>
		</table>
						</div>




		<div class="x_panel">
      <div class="x_title">
		<h2>Premium Rates/Clauses/Limits of Liability/Perils</h2>
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
					id="home-tab" role="tab" data-toggle="tab" aria-expanded="true">Premium Items</a>
				</li>
				<li role="presentation" class=""><a href="#tab_content-prem"
													role="tab" id="profile-tab-rates" data-toggle="tab" aria-expanded="false">Prem Rate Tables</a>
				</li>
<%--				<li role="presentation" class=""><a href="#tab_content5"--%>
<%--														  id="medical-covers-tab" role="tab" data-toggle="tab" aria-expanded="true">Medical Covers</a>--%>
<%--				</li>--%>
				<li role="presentation" class=""><a href="#tab_content2"
					role="tab" id="profile-tab" data-toggle="tab" aria-expanded="false">Clauses</a>
				</li>
				<li role="presentation" class=""><a href="#tab_content3"
					role="tab" id="comm-rates-tab" data-toggle="tab" aria-expanded="false">Commission Rates</a>
				</li>
				<li role="presentation" class="">
					<a href="#tab_content4" role="tab" id="binder-perils" data-toggle="tab" aria-expanded="false">Perils</a>
				</li>
				<li role="presentation" class="">
					<a href="#tab_content09" role="tab" id="binder-req-docs" data-toggle="tab" aria-expanded="false">Required Documents</a>
				</li>
			</ul>
	<div id="myTabContent" class="tab-content">
				<div role="tabpanel" class="tab-pane fade active in"
					id="tab_content1" aria-labelledby="home-tab">
		<button type="button" class="btn btn-info btn-dis" id="btn-add-prem-rates">New</button>
		<input type="hidden" id="binder-sc-code-pk">
					<div class="cutom-container">
		<table id="premList" class="table table-hover table-bordered">
			<thead>
				<tr>
					<th>Premium Item</th>
					<th>Range Type</th>
					<th>Range From</th>
					<th>Range To</th>
					<th>Rate</th>
					<th>Rate Type</th>
					<th>Prorated Full</th>
					<th>Free Limit</th>
					<th>Mandatory</th>
					<th>Active?</th>
					<th></th>
					<th width="5%"></th>
					<th width="5%"></th>
				</tr>
			</thead>
		</table>
						</div>

					<div class="form-group life-comm-rates">
						<div class="x_title">
							<h2>Commission Rates</h2>
							<ul class="nav navbar-right panel_toolbox">
								<li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
								</li>
							</ul>
							<div class="clearfix"></div>
						</div>
				<button type="button" class="btn btn-info"
						id="btn-add-comm-rate">New</button>
						<input type="hidden" id="prem-item-code-pk">
				<div class="custom-container">
					<table id="lifecommRatesList" class="table table-hover table-bordered">
						<thead>
						<tr>
							<th>Term From</th>
							<th>Term To</th>
							<th>Yr From</th>
							<th>Yr To</th>
							<th>Rate</th>
							<th>Div Factor</th>
							<th>Pay Frequency</th>
							<th>W.e.f</th>
							<th>W.e.t</th>
							<th width="5%"></th>
							<th width="5%"></th>
						</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
		<div role="tabpanel" class="tab-pane fade"
			 id="tab_content-prem" aria-labelledby="profile-tab-2">
			<form id="prem-rate-upload-form" class="form-horizontal" enctype="multipart/form-data">
				<input type="hidden" class="form-control" id="rates-bind-det-code" name="binderDetails">
				<div class="col-md-10 col-xs-12 form-required">

					<div class="col-md-5 col-xs-12">
						<div class="input-group col-xs-12">
							<input name="file" class="btn-dis" type="file" id="file-avatar" required>
						</div>
					</div>
					<div class="col-md-3 col-xs-12">
						<input type="submit" class="btn btn-success btn-xs pull-left btn-dis" style="margin-right: 10px;" value="Upload">

					</div>
				</div>
			</form>
			<table id="prem-rates-table" class="table table-hover table-bordered">
				<thead>
				<tr>

					<th width="45%">Rate Table Name</th>
					<th width="45%">Effective From Date</th>
					<th width="10%">Download</th>
				</tr>
				</thead>
			</table>

		</div>
<%--		<div role="tabpanel" class="tab-pane fade"--%>
<%--			 id="tab_content5" aria-labelledby="medical-covers-tab">--%>
<%--			<button type="button" class="btn btn-info btn-dis" id="btn-add-med-rates">New</button>--%>
<%--			<div class="cutom-container">--%>
<%--			<table id="medCoversList" class="table table-hover table-bordered">--%>
<%--				<thead>--%>
<%--				<tr>--%>
<%--					<th>Cover</th>--%>
<%--					<th>Main Cover</th>--%>
<%--					<th>Parent Cover</th>--%>
<%--					<th>Prorated Full</th>--%>
<%--					<th>Min Prem</th>--%>
<%--					<th>Waiting Period</th>--%>
<%--					<th>Appl. At</th>--%>
<%--					<th></th>--%>
<%--					<th width="5%"></th>--%>
<%--					<th width="5%"></th>--%>
<%--				</tr>--%>
<%--				</thead>--%>
<%--			</table>--%>
<%--				</div>--%>
<%--		</div>--%>


		<div role="tabpanel" class="tab-pane fade"
			 id="tab_content09" aria-labelledby="medical-covers-tab">
			<button type="button" class="btn btn-info btn-dis" id="btn-add-reqd-docs">New</button>
			<div class="cutom-container">
				<table id="reqDocsList" class="table table-hover table-bordered">
					<thead>
					<tr>
						<th>Id</th>
						<th>Desc</th>
						<th>Mandatory</th>
						<th width="5%"></th>
						<th width="5%"></th>
					</tr>
					</thead>
				</table>
			</div>
		</div>
		<div role="tabpanel" class="tab-pane fade"
					id="tab_content2" aria-labelledby="profile-tab">
			
			<button type="button" class="btn btn-info btn-dis"
						id="btn-add-binder-clauses">New</button>
					<table id="bindclausesList" class="table table-hover table-bordered">
						<thead>
							<tr>
			
								<th>Clause Id</th>
								<th>Clause Heading</th>
								<th>Editable?</th>
								<th>Mandatory?</th>
								<th width="5%"></th>
								<th width="5%"></th>
							</tr>
						</thead>
					</table>
					
		</div>
		<div role="tabpanel" class="tab-pane fade"
					id="tab_content3" aria-labelledby="comm-rates-tab">

			<button type="button" class="btn btn-info btn-dis"
						id="btn-add-comm-rates">New</button>

					<table id="commRatesList" class="table table-hover table-bordered">
						<thead>
							<tr>

							<!-- 	<th>Rate Desc</th> -->
								<th>Rate Type</th>
								<th>Rate</th>
								<th>Div Factor</th>
								<th>Range From</th>
								<th>Range To</th>
								<th>Trans Code</th>
								<th>Active?</th>
								<th width="5%"></th>
								<th width="5%"></th>
							</tr>
						</thead>
					</table>

		</div>
		<div role="tabpanel" class="tab-pane fade"
			 id="tab_content4" aria-labelledby="binder-perils">
				<div class="col-md-6 col-xs-12">
					<label for="houseId" class="control-label col-md-5">
						Select Section</label>
					<div class="col-md-7 col-xs-12">
						<input type="hidden" id="binder-sect-pk">
						<div id="binder-sect-form" class="form-control"
							 select2-url="<c:url value="/protected/setups/binders/selPerilSections"/>" >

						</div>
						<button type="button" class="btn btn-info btn-dis"
								id="btn-add-sect-perils">Add</button>
					</div>
				</div>


			<table id="binder-perils-tbl" class="table table-hover table-bordered">
				<thead>
				<tr>
					<th>Peril Id</th>
					<th>Peril</th>
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
<%--				<div role="tabpanel" class="tab-pane fade"--%>
<%--					 id="tab_content2-2" aria-labelledby="profile-tab-2">--%>
<%--					<form id="rate-upload-form" class="form-horizontal" enctype="multipart/form-data">--%>
<%--						<input type="hidden" class="form-control" id="rates-bind-code" name="binder">--%>
<%--					<div class="col-md-10 col-xs-12 form-required">--%>

<%--						<div class="col-md-5 col-xs-12">--%>
<%--							<div class="input-group col-xs-12">--%>
<%--								<input name="file" class="btn-dis" type="file" id="avatar" required>--%>
<%--								</div>--%>


<%--						</div>--%>
<%--						&lt;%&ndash;<div class="col-md-2 col-xs-12">&ndash;%&gt;--%>
<%--						&lt;%&ndash;<select class="form-control" id="rate-type" name="tableType" required>&ndash;%&gt;--%>
<%--							&lt;%&ndash;<option value="">Select Type</option>&ndash;%&gt;--%>
<%--							&lt;%&ndash;<option value="I">Individual</option>&ndash;%&gt;--%>
<%--							&lt;%&ndash;<option value="G">Group</option>&ndash;%&gt;--%>
<%--						&lt;%&ndash;</select>&ndash;%&gt;--%>
<%--							&lt;%&ndash;</div>&ndash;%&gt;--%>
<%--						<div class="col-md-3 col-xs-12">--%>
<%--							<input type="submit" class="btn btn-success btn-xs pull-left btn-dis" style="margin-right: 10px;" value="Upload">--%>

<%--						</div>--%>
<%--					     </div>--%>
<%--					</form>--%>
<%--					<table id="rates-table" class="table table-hover table-bordered">--%>
<%--						<thead>--%>
<%--						<tr>--%>

<%--							<th>Rates Table Name</th>--%>
<%--							<th>Effective From</th>--%>
<%--						</tr>--%>
<%--						</thead>--%>
<%--					</table>--%>

<%--				</div>--%>
<%--				<div role="tabpanel" class="tab-pane fade"--%>
<%--					 id="tab_content2-3" aria-labelledby="profile-tab-3">--%>
<%--					<button type="button" class="btn btn-info btn-dis"--%>
<%--							id="btn-add-bind-rules">New</button>--%>
<%--					<table id="binder-rules-tbl" class="table table-hover table-bordered">--%>
<%--						<thead>--%>
<%--						<tr>--%>

<%--							<th>Rule ID</th>--%>
<%--							<th>Rule Value</th>--%>
<%--							<th>Rule Description</th>--%>
<%--							<th>Mandatory</th>--%>
<%--							<th width="5%"></th>--%>
<%--							<th width="5%"></th>--%>
<%--						</tr>--%>
<%--						</thead>--%>
<%--					</table>--%>

<%--				</div>--%>

<%--				<div role="tabpanel" class="tab-pane fade"--%>
<%--					 id="tab_content2-8" aria-labelledby="profile-tab-3">--%>
<%--					<button type="button" class="btn btn-info btn-dis"--%>
<%--							id="btn-add-bind-exclusions">New</button>--%>
<%--					<div class="cutom-container">--%>
<%--					<table id="binder-exclusions-tbl" class="table table-hover table-bordered">--%>
<%--						<thead>--%>
<%--						<tr>--%>
<%--							<th>Exclusion ID</th>--%>
<%--							<th>Exclusion Desc</th>--%>
<%--							<th>Cost Per Claim</th>--%>
<%--							<th>Upper Limit</th>--%>
<%--							<th>Waiting Days</th>--%>
<%--							<th>Chronic</th>--%>
<%--							<th width="5%"></th>--%>
<%--							<th width="5%"></th>--%>
<%--						</tr>--%>
<%--						</thead>--%>
<%--					</table>--%>
<%--					</div>--%>
<%--				</div>--%>

<%--				<div role="tabpanel" class="tab-pane fade"--%>
<%--					 id="tab_content2-9" aria-labelledby="profile-tab-9">--%>
<%--					<button type="button" class="btn btn-info btn-dis"--%>
<%--							id="btn-add-bind-loading">New</button>--%>
<%--					<div class="cutom-container">--%>
<%--					<table id="binder-loadings-tbl" class="table table-hover table-bordered">--%>
<%--						<thead>--%>
<%--						<tr>--%>
<%--							<th>Ailment ID</th>--%>
<%--							<th>Ailment Desc</th>--%>
<%--							<th>Cost Per Claim</th>--%>
<%--							<th>Upper Limit</th>--%>
<%--							<th>Waiting Days</th>--%>
<%--							<th>Chronic</th>--%>
<%--							<th>Rate Type</th>--%>
<%--							<th>Rate</th>--%>
<%--							<th>Amount</th>--%>
<%--							<th width="5%"></th>--%>
<%--							<th width="5%"></th>--%>
<%--						</tr>--%>
<%--						</thead>--%>
<%--					</table>--%>
<%--						</div>--%>

<%--				</div>--%>

<%--				<div role="tabpanel" class="tab-pane fade"--%>
<%--					 id="tab_content2-10" aria-labelledby="profile-tab-10">--%>
<%--					<button type="button" class="btn btn-info btn-dis"--%>
<%--							id="btn-add-providers">New</button>--%>
<%--					<div class="cutom-container">--%>
<%--					<table id="providerContractTbl" class="table table-hover table-bordered">--%>
<%--						<thead>--%>
<%--						<tr>--%>
<%--							<th>Contract No</th>--%>
<%--							<th>Contract Type</th>--%>
<%--							<th>Name</th>--%>
<%--							<th>Provider Type</th>--%>
<%--							<th>Status</th>--%>
<%--							<th>Issue Date</th>--%>
<%--							<th>Begin Date</th>--%>
<%--							<th>End Date</th>--%>
<%--							<th width="5%"></th>--%>
<%--							<th width="5%"></th>--%>
<%--						</tr>--%>
<%--						</thead>--%>
<%--					</table>--%>
<%--						</div>--%>

<%--				</div>--%>
<%--				<div role="tabpanel" class="tab-pane fade"--%>
<%--					 id="tab_content2-11" aria-labelledby="profile-tab-11">--%>
<%--					<button type="button" class="btn btn-info btn-dis" id="btn-add-med-card">New</button>--%>
<%--					<div class="cutom-container">--%>
<%--						<table id="medCardList" class="table table-hover table-bordered">--%>
<%--							<thead>--%>
<%--							<tr>--%>
<%--								<th>Card Type</th>--%>
<%--								<th>New Card Fee</th>--%>
<%--								<th>Card Re-Issue Fee</th>--%>
<%--								<th>Service Charge</th>--%>
<%--								<th>Service Charge Prorated</th>--%>
<%--								<th>VAT Applicable</th>--%>
<%--								<th>Wef</th>--%>
<%--								<th>Wet</th>--%>
<%--								<th width="5%"></th>--%>
<%--								<th width="5%"></th>--%>
<%--							</tr>--%>
<%--							</thead>--%>
<%--						</table>--%>
<%--					</div>--%>
<%--				</div>--%>

<%--				<div role="tabpanel" class="tab-pane fade"--%>
<%--					 id="tab_content2-12" aria-labelledby="profile-tab-12">--%>
<%--					<button type="button" class="btn btn-info btn-dis" id="btn-add-questionnaire">New</button>--%>
<%--					<div class="cutom-container">--%>
<%--						<table id="questionList" class="table table-hover table-bordered">--%>
<%--							<thead>--%>
<%--							<tr>--%>

<%--								<th width="30%">Question Id</th>--%>
<%--								<th width="30%">Question Name</th>--%>
<%--								<th width="30%">Question Type</th>--%>
<%--								<th width="30%">Mandatory</th>--%>
<%--								<th width="30%">Order</th>--%>
<%--								<th width="30%">Triggered By</th>--%>
<%--								<th width="30%">Triggered By Answer</th>--%>
<%--								<th width="5%"></th>--%>
<%--								<th width="5%"></th>--%>
<%--							</tr>--%>
<%--							</thead>--%>
<%--						</table>--%>
<%--						<div class="x_title">--%>
<%--							<h4>Question Choices</h4>--%>
<%--						</div>--%>
<%--						<button type="button" class="btn btn-info" id="btn-add-questionnaire-choice">New</button>--%>
<%--						<input type="hidden" id="choice-quiz-pk">--%>
<%--						<table id="questionChoiceList" class="table table-hover table-bordered">--%>
<%--							<thead>--%>
<%--							<tr>--%>

<%--								<th width="80%">Choice</th>--%>
<%--								<th width="5%"></th>--%>
<%--								<th width="5%"></th>--%>
<%--							</tr>--%>
<%--							</thead>--%>
<%--						</table>--%>
<%--					</div>--%>
<%--				</div>--%>

		</div>



		</div>

		</div>
			</div>
    </div>
	<jsp:include page="bindermodals/modals.jsp"></jsp:include>
	