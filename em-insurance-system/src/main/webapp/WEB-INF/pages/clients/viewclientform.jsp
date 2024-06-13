<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<script type="text/javascript" src="<c:url value="/js/modules/utils/select2builder.js"/>"></script>
<script type="text/javascript" src="<c:url value="/libs/rivets/rivets.js"/>"></script>

<script type="text/javascript">
  var tenidpkey =$(location).attr("href").split('/').pop();
</script>

<div class="x_panel">
  <div class="" role="tabpanel" data-example-id="togglable-tabs">
    <ul class="breadcrumb bg-white">
      <li><a href="<c:url value="/protected/clients/setups/clientslist"/>">Clients</a></li>
      <li class="active" id="client_det" style="font-weight: bolder"></li>
    </ul>
    <ul id="myTab" class="nav nav-tabs bar_tabs" role="tablist">
      <li role="presentation" class="active"><a href="#tab_content1"
                                                id="home-tab" role="tab" data-toggle="tab"
                                                aria-expanded="true">Client Details</a>
      </li>
      <li role="presentation" id="show-docs"><a href="#tab_content2"
                                                                     role="tab" id="profile-tab" data-toggle="tab"
                                                                     aria-expanded="true">Client Documents</a>
      </li>
      <li role="presentation" id="show-trans"><a href="#tab_content3"
                                                role="tab" id="trans-tab" data-toggle="tab"
                                                aria-expanded="true">Transactions</a>
      </li>
    </ul>
  </div>
    <div id="myTabContent" class="tab-content">
      <div role="tabpanel" class="tab-pane fade active in"
           id="tab_content1" aria-labelledby="home-tab">

        <form id="tenant-form" data-parsley-validate class="form-horizontal form-label-left" enctype="multipart/form-data">
          <div class="x_panel">
            <div class="form-group">
              <c:if  test="${empty param.type}">
                <a href="<c:url value='/protected/clients/setups/clientslist'/> " class="btn btn-primary pull-right">Back</a>
              </c:if>
              <c:if  test="${param.type.equalsIgnoreCase('pol')}">
                <a href="<c:url value="/protected/uw/policies/editpolicy"/>" class="btn btn-default pull-right">Back to Policy</a>
              </c:if>
              <c:if  test="${param.type.equalsIgnoreCase('med')}">
                <a href="<c:url value="/protected/medical/policies/uwform"/>" class="btn btn-default pull-right">Back to Policy</a>
              </c:if>

              <c:if  test="${param.type.equalsIgnoreCase('life')}">
                <a href="<c:url value="/protected/life/policies/lifeuwform"/>" class="btn btn-default pull-right">Back to Policy</a>
              </c:if>
              <c:if  test="${param.type.equalsIgnoreCase('quot')}">
                <a href="<c:url value="/protected/quotes/editClientQuote"/>" class="btn btn-default pull-right">Back to Quote</a>
              </c:if>
              <c:if  test="${param.type.equalsIgnoreCase('medquot')}">
                <a href="<c:url value="/protected/quotes/editMedQuote"/>" class="btn btn-default pull-right">Back to Quote</a>
              </c:if>
              <c:if  test="${param.type.equalsIgnoreCase('medmember')}">
                <a href="<c:url value="/protected/medical/policies/editpolicy"/>" class="btn btn-default pull-right">Back to Policy</a>
              </c:if>

              <input type="button" class="btn btn-primary pull-right"
                     value="Unapprove" id="btn-auth-client">

            </div>
          </div>
          <div class="x_panel">
            <input type="hidden" name="tenId" id="tenId-pk">
            <div class="form-group">
              <div class="col-md-6 col-xs-12">
                <label for="ten-id" class="control-label col-md-5">Client ID</label>
                <div class="col-md-7 col-xs-12">
                  <p class="form-control-static" id="ten-id"></p>
                </div>
              </div>
              <div class="col-md-6 col-xs-12 form-required">
                <label for="clnt-type-name" class="control-label col-md-5">Client Type</label>
                <div class="col-md-5 col-xs-12">
                  <p class="form-control-static" id="clnt-type-name"></p>
                </div>
              </div>
            </div>
            <div class="form-group form-required">
              <div class="col-md-6 col-xs-12">
                <label for="clnt-title-name" class="control-label col-md-5">Title</label>
                <div class="col-md-6 col-xs-12">
                  <p class="form-control-static" id="clnt-title-name"></p>
                </div>
              </div>
              <div class="col-md-6 col-xs-12">
                <label for="client-ref-no" class="control-label col-md-5">Client Reference</label>
                <div class="col-md-7 col-xs-12">
                  <p class="form-control-static" id="client-ref-no"></p>
                </div>
              </div>
            </div>
            <div class="form-group form-required">
              <div class="col-md-6 col-xs-12">
                <label for="fname" class="control-label col-md-5">First
                  Name</label>
                <div class="col-md-7 col-xs-12">
                  <p class="form-control-static" id="fname"></p>
                </div>
              </div>
              <div class="col-md-6 col-xs-12">
                <label for="other-names" class="control-label col-md-5">Other
                  Names</label>
                <div class="col-md-7 col-xs-12">
                  <p class="form-control-static" id="other-names"></p>
                </div>
              </div>
            </div>

            <div class="form-group form-required">
              <div class="col-md-6 col-xs-12">
                <label for="pin-no" class="control-label col-md-5">Pin No</label>
                <div class="col-md-7 col-xs-12">
                  <p class="form-control-static" id="pin-no"></p>
                </div>
              </div>
              <div class="col-md-6 col-xs-12">
                <label for="clnt-authorised" class="control-label col-md-5">Approved?</label>
                <div class="col-md-7 col-xs-12">
                  <p class="form-control-static" id="clnt-authorised" style="color: green"></p>
                </div>
              </div>

            </div>
            <div class="form-group form-required">
              <div class="col-md-6 col-xs-12">
                <label for="address" class="control-label col-md-5">Address</label>
                <div class="col-md-7 col-xs-12">
                  <p class="form-control-static" id="address"></p>
                </div>

              </div>

              <div class="col-md-6 col-xs-12">
                <label for="clnt-town-name" class="control-label col-md-5">Town</label>
                <div class="col-md-7 col-xs-12">
                  <p class="form-control-static" id="clnt-town-name"></p>
                </div>

              </div>

            </div>
            <div class="form-group form-required">
              <div class="col-md-6 col-xs-12">
                <label for="postal-name" class="control-label col-md-5">Postal Code</label>
                <div class="col-md-7 col-xs-12">
                  <p class="form-control-static" id="postal-name"></p>
                </div>

              </div>

              <div class="col-md-6 col-xs-12">
                <label for="id-no" class="control-label col-md-5" id="lbl-id-no">ID No</label>
                <div class="col-md-7 col-xs-12">
                  <p class="form-control-static" id="id-no"></p>
                </div>

              </div>

            </div>
            <div class="form-group">
              <div class="col-md-6 col-xs-12">
                <label for="passport-no" class="control-label col-md-5">Passport No</label>
                <div class="col-md-7 col-xs-12">
                  <p class="form-control-static" id="passport-no"></p>
                </div>

              </div>

              <div class="col-md-6 col-xs-12">
                <label for="gender" class="control-label col-md-5" id="lblGender">Gender</label>
                <div class="col-md-7 col-xs-12 gender">
                  <p class="form-control-static" id="gender"></p>
                </div>
              </div>
            </div>
            <div class="form-group form-required">
              <div class="col-md-6 col-xs-12">
                <label for="cou-name" class="control-label col-md-5">Domicile Country</label>
                <div class="col-md-7 col-xs-12">
                  <p class="form-control-static" id="cou-name"></p>
                </div>
              </div>
              <div class="col-md-6 col-xs-12">
                <label for="office-tel-no" class="control-label col-md-5">Tel No</label>
                <div class="col-md-7 col-xs-12">
                  <p class="form-control-static" id="office-tel-no"></p>
                </div>
              </div>
            </div>
            <div class="form-group form-required">
              <div class="col-md-6 col-xs-12">
                <label for="pref-sms-name" class="control-label col-md-5">SMS Number</label>
                <div class="col-md-7 col-xs-12">
                  <p class="form-control-static" id="pref-sms-name"></p>
                </div>
              </div>
              <div class="col-md-6 col-xs-12">
                <label for="pref-phone-name" class="control-label col-md-5">Phone Number</label>
                <div class="col-md-7 col-xs-12">
                  <p class="form-control-static" id="pref-phone-name"></p>
                </div>
              </div>
            </div>
            <div class="form-group form-required">
              <div class="col-md-6 col-xs-12">
                <label for="ob-name" class="control-label col-md-5">Branch Registered</label>
                <div class="col-md-7">
                  <p class="form-control-static" id="ob-name"></p>
                </div>
              </div>
              <div class="col-md-6 col-xs-12">
                <label for="email-address" class="control-label col-md-5">Email</label>
                <div class="col-md-7 col-xs-12">
                  <p class="form-control-static" id="email-address"></p>
                </div>
              </div>
            </div>
            <div class="form-group form-required">
              <div class="col-md-6 col-xs-12">
                <label for="sel3" class="control-label col-md-5">Status</label>
                <div class="col-md-7 col-xs-12">
                  <p class="form-control-static" id="sel3"></p>
                </div>
              </div>
              <div class="col-md-6 col-xs-12">
                <label for="dob" class="col-md-5 control-label" id="lbl-dob">Date of Birth</label>

                <div class="col-md-7 col-xs-12">
                  <p class="form-control-static" id="dob"></p>
                </div>
              </div>
            </div>
            <div class="form-group form-required">

              <div class="col-md-6 col-xs-12">
                <label for="occ-name" class="control-label col-md-5" id="lbl-occ">Occupation</label>
                <div class="col-md-7 col-xs-12">
                  <p class="form-control-static" id="occ-name"></p>
                </div>
              </div>
            <div class="col-md-6 col-xs-12">
              <label for="sect-name" class="col-md-5 control-label" id="lbl-sector">Sector</label>
              <div class="col-md-7 col-xs-12">
                <p class="form-control-static" id="sect-name"></p>
              </div>
            </div>
          </div>
            <div class="form-group form-required">

              <div class="col-md-6 col-xs-12" id="myComments">
                <label for="comment" class="control-label col-md-5">Blacklisting Comment</label>
                <div class="col-md-7 col-xs-12">
                  <p class="form-control-static" id="comment"></p>
                </div>
              </div>
            </div>
          <h4>Other Information</h4>
          <hr>
          <div class="form-group">
            <label for="date-reg" class="col-md-3 control-label">Date Registered</label>

            <div class="col-md-3 col-xs-12">
              <p class="form-control-static" id="date-reg"></p>
            </div>
          </div>
          <div class="form-group">
            <label for="dt-terminated" class="col-md-3 control-label">Date Terminated</label>

            <div class="col-md-3 col-xs-12">
              <p class="form-control-static" id="dt-terminated"></p>
            </div>
          </div>
          <div class='spacer'></div>

          <div class="form-group">
            <div class="col-md-6 col-xs-12 form-required">
              <label for="file" class="control-label col-md-5" id="client-photo">
                Photo</label>
              <div class="col-md-7 col-xs-12">
                <div class="kv-avatar center-block" style="width: 200px">
                  <img  src="<c:url value='/protected/clients/setups/tenantImage/${tenId}'/> ">

                </div>
              </div>
            </div>
          </div>
      </div>
        </form>
      </div>
      <div role="tabpanel" class="tab-pane fade"
           id="tab_content2" aria-labelledby="profile-tab">
        <button class="btn btn-primary btn btn-info" id="btn-add-docs">New</button>
        <div class="card-box table-responsive">
          <table id="clientDocsList" class="table table-hover table-bordered">
            <thead>
            <tr>
              <th>Document ID</th>
              <th>Document Desc</th>
              <th>File Name</th>
              <th>File Ref. No.</th>
              <th width="5%"></th>
              <th width="5%"></th>
              <th width="5%"></th>
            </tr>
            </thead>
          </table>
        </div>
      </div>
      <div role="tabpanel" class="tab-pane fade"
           id="tab_content3" aria-labelledby="profile-tab">
          <table id="clientTransList" class="table table-hover table-bordered">
            <thead>
            <tr>
              <th>Policy No</th>
              <th>Product</th>
              <th>Start Date</th>
              <th>End Date</th>
              <th> Date</th>
              <th> Ref</th>
              <th> Type</th>
              <th>Gross</th>
              <th>Net</th>
              <th>Balance</th>
            </tr>
            </thead>
          </table>
      </div>
    </div>

  <div class="modal fade" id="clientReqDocsModal" tabindex="-1" role="dialog"
       aria-labelledby="clientReqDocsModalLabel" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal"
                  aria-label="Close">
            <span aria-hidden="true">&times;</span>
          </button>
          <h4 class="modal-title" id="clientReqDocsModalLabel">Select Required Docs</h4>
        </div>
        <div class="modal-body">
          <form class="form-horizontal">
            <div class="form-group">
              <label for="brn-id" class="col-md-3 control-label">Document Name</label>

              <div class="col-md-6">
                <input type="text" class="form-control" id="doc-name-search"
                >
              </div>
              <div class="col-md-1">
                <button  id="searchDocuments"
                         type="button" class="btn btn-primary">
                  Search
                </button>
              </div>
            </div>
          </form>
          <div style="height: 300px !important; overflow: scroll;">
            <table class="table table-striped table-hover table-bordered table-fixed" id="clientDocsTbl">
              <thead>
              <tr>
                <th width="1%"></th>
                <th width="4%">Document Id</th>
                <th width="12%">Document Name</th>
              </tr>
              </thead>
              <tbody>

              </tbody>
            </table>
          </div>
          <form id="client-doc-form">
            <input type="hidden" id="req-client-code" name="subCode"/>
          </form>
        </div>
        <div class="modal-footer">
          <button data-loading-text="Saving..." id="saveClientDocsBtn"
                  type="button" class="btn btn-success">Save</button>
          <button type="button" class="btn btn-default" data-dismiss="modal">
            Cancel</button>
        </div>
      </div>
    </div>
  </div>


  <div class="modal fade" id="clientdocModal" tabindex="-1" role="dialog"
       aria-labelledby="clientdocModalLabel" aria-hidden="true">
    <div class="modal-dialog">
      <form id="clnt-doc-form" class="form-horizontal" enctype="multipart/form-data">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal"
                    aria-label="Close">
              <span aria-hidden="true">&times;</span>
            </button>
            <h4 class="modal-title" id="clientdocModalLabel">Upload Client Document</h4>
          </div>
          <div class="modal-body">

            <input type="hidden" id="clnt-doc-id" name="docId"/>
            <input type="hidden" id="reqd-doc-id" name="requiredDoc"/>
            <div class="form-group">
              <label for="cou-name" class="col-md-3 control-label">Document Type</label>

              <div class="col-md-8">
                <p class="form-control-static" id="clnt-doc-name"></p>
              </div>
            </div>
            <div class="form-group">
              <label for="cou-name" class="col-md-3 control-label">File Ref. No</label>

              <div class="col-md-8">
                <input type="text" class="form-control" id="upload-sht-id"
                       name="fileId">
              </div>
            </div>
            <div class="form-group">
              <label for="cou-name" class="col-md-3 control-label">Uploaded File Name</label>

              <div class="col-md-8">
                <p class="form-control-static" id="clnt-upload-name"></p>
              </div>
            </div>
            <div class="form-group">
              <label for="brn-id" class="col-md-4 control-label">Document</label>

              <div class="col-md-8">
                <div class="input-group col-xs-12">
                  <input name="file" type="file" id="clnt-avatar" required>
                </div>
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <input  value="Upload"
                    type="submit" class="btn btn-success">

            </input>
            <button type="button" class="btn btn-default" data-dismiss="modal">
              Close</button>
          </div>
        </div>
      </form>
    </div>
  </div>


  <div class="modal fade bs-example-modal-sm" id="myPleaseWait" tabindex="-1"
       role="dialog" aria-hidden="true" data-backdrop="static">
    <div class="modal-dialog modal-sm">
      <div class="modal-content">
        <div class="modal-header">
          <h4 class="modal-title">
                    <span class="glyphicon glyphicon-time">
                    </span>Please Wait
          </h4>
        </div>
        <div class="modal-body">
          <div class="progress">
            <div class="progress-bar progress-bar-info
                    progress-bar-striped active"
                 style="width: 100%">
            </div>
          </div>
        </div>
      </div>
    </div></div>
</div>
<script>
  $(document).ready(function (){
    $(".datepicker-input").each(function() {
      $(this).datetimepicker({
        format: 'DD/MM/YYYY'
      });
    });

    getTenantDetails();
    saveClientDocsList();

    $("#btn-auth-client").click(function () {
      authorizeClient($("#tenId-pk").val());
    });
    //}

    $("#btn-add-docs").click(function(){
      searchReqDocs("");
    });

    $("#searchDocuments").click(function(){
      searchReqDocs($("#doc-name-search").val());
    })

    $(document).ajaxStart(function () {
      $("#btn-auth-client,#saveClientDocsBtn").attr("disabled", true);
    });
    $(document).ajaxComplete(function () {
      $("#btn-auth-client,#saveClientDocsBtn").attr("disabled", false);
    });

  });


  function getTenantDetails(){
    if(typeof tenidpkey!== 'undefined'){
      if(tenidpkey!==-2000){
        $.ajax( {
          url: SERVLET_CONTEXT+'/protected/clients/setups/tenants/'+tenidpkey,
          type: 'GET',
          processData: false,
          contentType: false,
          success: function (s ) {
            populateTenantDetails(s);

          },
          error: function(xhr, error){
            new PNotify({
              title: 'Error',
              text: xhr.responseText,
              type: 'error',
              styling: 'bootstrap3'
            });
          }
        });
      }
      else{
        $(location).prop('href', getContextPath()+'/protected/clients/setups/clientsform')
      }

    }
  }


  function populateTenantDetails(data){
    $("#tenId-pk").val(data.tenId);
    $("#fname").text(data.fname);
    $("#other-names").text(data.otherNames);
    $("#client_det").html(data.fname+" "+data.otherNames);
    $("#comment").text(data.comment);
    $("#id-no").text(data.idNo);
    $("#passport-no").text(data.passportNo);
    $("#pin-no").text(data.pinNo);
    $("#client-ref-no").text(data.clientRef);
    $("#email-address").text(data.emailAddress);
    $("#address").text(data.address);
    $("#dob").text(moment(data.dob).format('DD/MM/YYYY'));
    if(data.status && data.status==='A'){
      $("#sel3").text('Active').css('color','green').css('font-weight','bold');
    }
    else  if(data.status && data.status==='I'){
      $("#sel3").text('Inactive').css('color','red').css('font-weight','bold');
    }
    else  if(data.status && data.status==='T'){
      $("#sel3").text('Terminated').css('color','red').css('font-weight','bold');
    }
    if(data.authStatus && data.authStatus==='Y'){
      $("#btn-auth-client").val('Unapprove');
      $("#clnt-authorised").text('Yes').css('color','green').css('font-weight','bold');
    }
    else{
      $("#btn-auth-client").val('Approve');
      $("#clnt-authorised").text('No').css('color','red').css('font-weight','bold');

    }

    if(data.authStatus && data.authStatus==='Y'){
      $('#btn-add-docs').css('display', 'none');
    }
    else {
      $('#btn-add-docs').css('display', 'block');

    }

    if(data.clientType==='C' || data.clientType===''){
      $("#gender,.gender,#lblGender").hide();
      $("#lbl-dob").html("Date of Incorporation");
      $("#lbl-id-no").html("Registration No.");
      $(".employee-info").hide();

    }
    else if(data.clientType==='I'){
      $("#gender,.gender,#lblGender").show();
      $("#lbl-dob").html("Date of Birth");
      $("#lbl-id-no").html("ID No.");
      $(".employee-info").show();
    }
    if(data.gender && data.gender==='M'){
      $("#gender").text('Male');
    }
    else if(data.gender && data.gender==='F'){
      $("#gender").text('Female');
    }
    else{
        $("#gender").text('Other');
    }
    $("#ten-id").text(data.tenantNumber);
    $("#office-tel-no").text(data.officeTel);
    $("#date-reg").text(moment(data.dateregistered).format('DD/MM/YYYY'));
    $("#ob-name").text(data.obName);
    if(data.country) {
      $("#cou-name").text(data.couName);
    }
    if(data.smsPrefix) {
      $("#pref-sms-name").text(data.smsPrefixName + data.smsNumber);
    }
    else{
      $("#pref-sms-name").text(data.smsNumber);
    }
    if(data.phonePrefix) {
      $("#pref-phone-name").text(data.phonePrefixName + data.phoneNo);
    }
    else{
      $("#pref-phone-name").text(data.phoneNo);
    }
    if(data.clientTypeId) {
      $("#clnt-type-name").text(data.clientTypeDesc);
    }
    if(data.titleId) {
      $("#clnt-title-name").text(data.titleName);
    }
    if(data.sectCode) {
      $("#sect-name").text(data.sectName);

    }
    if(data.occCode) {
      $("#occ-name").text(data.occName);
    }

    if(data.ctCode) {
      $("#clnt-town-name").text(data.ctName);
    }
    if(data.pcode) {
      $("#postal-name").text(data.postalName);
    }
    if(data.dateterminated)
      $("#dt-terminated").text(moment(data.dateterminated).format('DD/MM/YYYY'));
    getClientDocs(data.tenId);
    getClientTransactions(data.tenId);
  }


  function getClientTransactions(clientId){
    var url = SERVLET_CONTEXT+"/protected/clients/setups/transactions/"+clientId;
    var currTable = $('#clientTransList').DataTable( {
      "processing": true,
      "serverSide": true,
      "ajax": {
        'url': url,
      },
      lengthMenu: [ [10,15,20], [10,15,20] ],
      pageLength: 10,
      "aoColumnDefs": [
        { "sWidth": "100px", "aTargets": [0] } // 1 would be the 2nd column
      ],
      destroy: true,
      "columns": [
        { "data": "policyNo" },
        { "data": "product" },
        {
          "data": "wefDate",
          "render": function (data, type, full, meta) {
            return moment(full.wefDate).format('DD/MM/YYYY');
          }
        },
        {
          "data": "wetDate",
          "render": function (data, type, full, meta) {
            return moment(full.wetDate).format('DD/MM/YYYY');
          }
        },
        {
          "data": "transDate",
          "render": function (data, type, full, meta) {
            return moment(full.transDate).format('DD/MM/YYYY');
          }
        },
        { "data": "transactionRef" },
        { "data": "transactionType" },
        { "data": "grossAmount",
          "render":function(data,type,full,meta){
            return UTILITIES.currencyFormat(full.grossAmount);
          }
        },
        { "data": "netAmount",
          "render":function(data,type,full,meta){
            return UTILITIES.currencyFormat(full.netAmount);
          }
        },
        { "data": "transactionBalance",
          "render":function(data,type,full,meta){
            return UTILITIES.currencyFormat(full.transactionBalance);
          }
        },
      ]
    } );
    return currTable;
  }



  function getClientDocs(clientId){
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
        { "data": "cdId",
          "render": function ( data, type, full, meta ) {

            return full.reqShtDesc;
          }
        },
        { "data": "cdId",
          "render": function ( data, type, full, meta ) {

            return full.reqDesc;
          }
        },
        { "data": "uploadedFileName" },
        // { "data": "checkSum" },
        { "data": "fileId" },
        {
          "data": "cdId",
          "render": function ( data, type, full, meta ) {
            console.log(full);
            if(full.authStatus && full.authStatus==="A"){
              // return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-docs='+encodeURI(JSON.stringify(full)) + ' onclick="editClientDocs(this);" disabled><i class="fa fa-pencil-square-o"></button>';
            }else
              return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-docs='+encodeURI(JSON.stringify(full)) + ' onclick="editClientDocs(this);"><i class="fa fa-pencil-square-o"></button>';
          }

        },
        {
          "data": "cdId",
          "render": function ( data, type, full, meta ) {
            return '<button type="button" class="btn btn-success btn btn-info btn-xs" type="button" data-docs=' + encodeURI(JSON.stringify(full)) + ' onclick="downloadClientDoc(this);"><i class="fa fa-file-archive-o"></button>';

          }

        },
        {
          "data": "cdId",
          "render": function ( data, type, full, meta ) {
            if(full.authStatus && full.authStatus==="A"){
              // return '<button type="button" class="btn btn-danger btn btn-info btn-xs" data-docs='+encodeURI(JSON.stringify(full)) + ' onclick="deleteClientDoc(this);" disabled><i class="fa fa-trash-o"></button>';
            }else
              return '<button type="button" class="btn btn-danger btn btn-info btn-xs" data-docs='+encodeURI(JSON.stringify(full)) + ' onclick="deleteClientDoc(this);"><i class="fa fa-trash-o"></button>';
          }

        },
      ]
    } );
    return currTable;
  }

  function downloadClientDoc(button){
    var docs = JSON.parse(decodeURI($(button).data("docs")));
    window.open(SERVLET_CONTEXT+"/protected/clients/setups/clientDocument/"+docs['cdId'],
            '_blank' // <- This is what makes it open in a new window.
    );
  }

  function editClientDocs(button){
    var docs = JSON.parse(decodeURI($(button).data("docs")));
    $("#clnt-doc-name").text(docs["requiredDoc"].reqDesc);
    $("#clnt-upload-name").text(docs['uploadedFileName']);
    $("#upload-sht-id").val(docs['fileId']);
    $("#clnt-doc-id").val(docs['cdId']);
    $("#upload-sht-id").text(docs['fileId']);

    $('#clientdocModal').modal({
      backdrop: 'static',
      keyboard: true
    });
  }

  function deleteClientDoc(button){
    var docs = JSON.parse(decodeURI($(button).data("docs")));
    bootbox.confirm("Are you sure want to delete "+docs['reqDesc']+"?", function(result) {
      if(result){
        $('#myPleaseWait').modal({
          backdrop: 'static',
          keyboard: true
        });
        $.ajax({
          type: 'GET',
          url:  SERVLET_CONTEXT+'/protected/clients/setups/deleteClientDoc/' + docs['cdId'],
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
            $('#clientDocsList').DataTable().ajax.reload();
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



  function uploadClientDocument(){
    var $form = $("#clnt-doc-form");
    var validator = $form.validate();
    $('form#clnt-doc-form')
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
              data.append( 'file', $( '#clnt-avatar' )[0].files[0] );
              $.ajax( {
                url: SERVLET_CONTEXT+'/protected/clients/setups/uploadClientDocs',
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
                  $('#clientdocModal').modal('hide');
                  var $el = $('#clnt-avatar');
                  $el.wrap('<form>').closest('form').get(0).reset();
                  $el.unwrap();
                  $('#clientDocsList').DataTable().ajax.reload();

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


  function searchReqDocs(search){
    if($("#acctId-pk").val() != ''){
      $.ajax({
        type: 'GET',
        url:  SERVLET_CONTEXT+'/protected/clients/setups/clientreqdocs',
        dataType: 'json',
        data: {"clientCode": $("#tenId-pk").val(),"docName":search},
        async: true,
        success: function(result) {
          $("#clientDocsTbl tbody").each(function(){
            $(this).remove();
          });
          for(var res in result){
            var markup = "<tr><td><input type='checkbox' name='record' id='"+result[res].reqId+"'></td><td>" + result[res].reqShtDesc + "</td><td>" + result[res].reqDesc + "</td></tr>";
            $("#clientDocsTbl").append(markup);
          }
          $("#req-client-code").val($("#tenId-pk").val());
          $('#clientReqDocsModal').modal({
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
      bootbox.alert("No Account to attach Documents")
    }
  }




  function saveClientDocsList(){
    var arr = [];
    $("#saveClientDocsBtn").click(function(){
      $("#clientDocsTbl tbody").find('input[name="record"]').each(function(){
        if($(this).is(":checked")){
          arr.push($(this).attr("id"));
        }
      });
      if(arr.length==0){
        bootbox.alert("No Documents Selected to attach..");
        return;
      }

      var $currForm = $('#client-doc-form');
      var currValidator = $currForm.validate();
      if (!$currForm.valid()) {
        return;
      }

      var data = {};
      $currForm.serializeArray().map(function(x) {
        data[x.name] = x.value;
      });
      var url = SERVLET_CONTEXT+"/protected/clients/setups/createClientDocs";
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
          $('#clientDocsList').DataTable().ajax.reload();
          $('#clientReqDocsModal').modal('hide');
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




  function getContextPath() {
    return window.location.pathname.substring(0, window.location.pathname
            .indexOf("/", 2));
  }



  function authorizeClient(clientId){
    var stack_bottomleft = {"dir1": "up", "dir2": "left", "firstpos1": 25, "firstpos2": 25};
    $('#myPleaseWait').modal({
      backdrop: 'static',
      keyboard: true
    });
    $.ajax({
      type: 'GET',
      url:  SERVLET_CONTEXT+'/protected/clients/setups/authorizeClient/'+clientId,
      dataType: 'json',
      async: true,
      success: function(result) {
        $('#myPleaseWait').modal('hide');
        new PNotify({
          title: 'Success',
          text: 'Client Approved/Unapproved Successfully',
          type: 'success',
          styling: 'bootstrap3',
          addclass: 'stack-bottomright',
          stack: stack_bottomleft
        });
        getTenantDetails();
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




</script>