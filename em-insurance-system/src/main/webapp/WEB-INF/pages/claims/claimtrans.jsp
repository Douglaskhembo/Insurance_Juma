<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<script type="text/javascript" src="<c:url value="/js/modules/utils/select2builder.js"/>"></script>
<script type="text/javascript" src="<c:url value="/libs/rivets/rivets.js"/>"></script>
<%--<script  src="<c:url value='/libs/moment/min/moment.min.js' /> "></script>--%>
<script type="text/javascript" src="<c:url value="/js/modules/claims/claimtrans.js"/>"></script>
<script type="text/javascript">
    var clmId = ${clmId};
</script>


<div class="x_content">

    <div class="x_panel">
    <div class="x_title">
        <h3>Claim Transaction Details</h3>
    </div>
        <div class="col-xs-2">
        <ul id="myTab" class="nav nav-tabs tabs-left" role="tablist">
            <li role="presentation" class="active"><a href="#tab_content1"
                                                      id="home-tab" role="tab" data-toggle="tab" aria-expanded="true">Overview</a>
            </li>
            <li role="presentation" class="" id="show-taxes"><a href="#tab_content2"
                                                                role="tab" id="profile-tab" data-toggle="tab" aria-expanded="false">Claimant Details</a>
            </li>
            <li role="presentation" class="" id="show-clauses"><a href="#tab_content3"
                                                                  role="tab" id="comm-rates-tab" data-toggle="tab" aria-expanded="false">Documents</a>
            </li>
            <li role="presentation" class="" id="show-activity"><a href="#tab_content4"
                                                                  role="tab" id="activity-tab" data-toggle="tab" aria-expanded="false">Activities</a>
            </li>
        </ul>
        </div>
        <div class="col-xs-10">
        <div id="myTabContent" class="tab-content">
            <li>
                <input type="button" class="btn btn-primary pull-right"
                       value="" id="btn-clm-status">
                <input type="button" class="btn btn-primary pull-right"
                       value="Reports" id="btn-clm-reports" data-toggle="modal" data-target="#reportsModal">

            </li>

            <div role="tabpanel" class="tab-pane fade active in"
                 id="tab_content1" aria-labelledby="home-tab">

                <div class="col-md-6 col-sm-6 col-xs-12">
                    <div class="x_panel">
                        <div class="x_title">
                            <h2>Claim Information</h2>
                            <div class="clearfix"></div>
                        </div>
                        <div class="x_content">
                            <input type="hidden" id="binder-code">
                            <input type="hidden" id="clm-status1">
                            <div class="col-md-12 col-xs-12">
                                <label for="houseId" class="control-label col-md-7">
                                    Claim No</label>
                                <div class="col-md-5 col-xs-12">
                                    <p class="form-control-static" id="claim-no"> </p>
                                </div>
                            </div>
                            <div class="col-md-12 col-xs-12">
                                <label for="houseId" class="control-label col-md-7">
                                    Client</label>
                                <div class="col-md-5 col-xs-12">
                                    <p class="form-control-static" id="client-name"> </p>
                                </div>
                            </div>
                            <div class="col-md-12 col-xs-12">
                                <label for="houseId" class="control-label col-md-7">
                                    Loss Description</label>
                                <div class="col-md-5 col-xs-12">
                                    <p class="form-control-static" id="loss-desc"> </p>
                                </div>
                            </div>
                            <div class="col-md-12 col-xs-12">
                                <label for="houseId" class="control-label col-md-7">
                                    Claim Causation</label>
                                <div class="col-md-5 col-xs-12">
                                    <p class="form-control-static" id="causation-desc"> </p>
                                </div>
                            </div>
                            <div class="col-md-12 col-xs-12">
                                <label for="houseId" class="control-label col-md-7">
                                    Loss Date</label>
                                <div class="col-md-5 col-xs-12">
                                    <p class="form-control-static" id="loss-date"> </p>
                                </div>
                            </div>
                            <div class="col-md-12 col-xs-12">
                                <label for="houseId" class="control-label col-md-7">
                                    Notification Date</label>
                                <div class="col-md-5 col-xs-12">
                                    <p class="form-control-static" id="notific-date"> </p>
                                </div>
                            </div>
                            <div class="col-md-12 col-xs-12">
                                <label for="houseId" class="control-label col-md-7">
                                    Booked Date</label>
                                <div class="col-md-5 col-xs-12">
                                    <p class="form-control-static" id="bkd-date"> </p>
                                </div>
                            </div>
                            <div class="col-md-12 col-xs-12">
                                <label for="houseId" class="control-label col-md-7">
                                    Next Review Date</label>
                                <div class="col-md-5 col-xs-12">
                                    <p class="form-control-static" id="next-rev-date"> </p>
                                </div>
                            </div>
                            <div class="col-md-12 col-xs-12">
                                <label for="houseId" class="control-label col-md-7">
                                    Liability Admission</label>
                                <div class="col-md-5 col-xs-12">
                                    <p class="form-control-static" id="liab-admission"> </p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-6 col-sm-5 col-xs-12">
                    <div class="x_panel">
                        <div class="x_title">
                            <h2>Policy Information</h2>
                            <div class="clearfix"></div>
                        </div>
                        <div class="x_content">
                            <div class="col-md-12 col-xs-12">
                            <label for="houseId" class="control-label col-md-7">
                                Policy Number</label>
                            <div class="col-md-5 col-xs-12">
                                <p class="form-control-static" id="pol-no"> </p>
                            </div>
                        </div>
                            <div class="col-md-12 col-xs-12">
                                <label for="houseId" class="control-label col-md-7">
                                   Insured</label>
                                <div class="col-md-5 col-xs-12">
                                    <p class="form-control-static" id="insured"> </p>
                                </div>
                            </div>
                            <div class="col-md-12 col-xs-12">
                                <label for="houseId" class="control-label col-md-7">
                                    Product</label>
                                <div class="col-md-5 col-xs-12">
                                    <p class="form-control-static" id="product"> </p>
                                </div>
                            </div>
                            <div class="col-md-12 col-xs-12">
                                <label for="houseId" class="control-label col-md-7">
                                    Insured Property</label>
                                <div class="col-md-5 col-xs-12">
                                    <p class="form-control-static" id="risk-id"> </p>
                                    <input type="hidden" id="curr-risk-id">

                                </div>
                            </div>
                            <div class="col-md-12 col-xs-12">
                                <label for="houseId" class="control-label col-md-7">
                                    Value</label>
                                <div class="col-md-5 col-xs-12">
                                    <p class="form-control-static" id="risk-value"> </p>
                                </div>
                            </div>
                            <div class="col-md-12 col-xs-12">
                                <label for="houseId" class="control-label col-md-7">
                                   Period From</label>
                                <div class="col-md-5 col-xs-12">
                                    <p class="form-control-static" id="risk-wef"> </p>
                                </div>
                            </div>
                            <div class="col-md-12 col-xs-12">
                                <label for="houseId" class="control-label col-md-7">
                                    Period To</label>
                                <div class="col-md-5 col-xs-12">
                                    <p class="form-control-static" id="risk-wet"> </p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div role="tabpanel" class="tab-pane fade"
                id="tab_content2" aria-labelledby="profile-tab">
                <div class="x_panel">
                    <div class="x_title">
                        <h4>Claimant Details</h4>
                    </div>
                    <button type="button" class="btn btn-info"
                            id="add-peril-btn">New</button>
                    <div class="cutom-container">
                    <table id="clm_claimant_tbl" class="table table-hover table-bordered">
                        <thead>
                        <tr>
                            <th>Name</th>
                            <th>Type</th>
                            <th>Peril Description</th>
                            <th>Limit Amount</th>
                            <th>Created Date</th>
                            <th>Created By</th>
                            <th width="5%"></th>
                        </tr>
                        </thead>
                    </table>
                        </div>
                </div>
                <div class="x_panel">
                    <div class="x_title">
                        <h4>Payments Details</h4>
                    </div>
                    <div class="cutom-container">
                        <table id="clm_perils_pymnt_tbl" class="table table-hover table-bordered">
                            <thead>
                                <tr>
                                    <th>Payee</th>
                                    <th>Reference</th>
                                    <th>Pay Method</th>
                                    <th>Trans Type</th>
                                    <th>Currency</th>
                                    <th>Amount</th>
                                    <th>Status</th>
                                    <th>Created By</th>
                                    <th>Created Date</th>
                                    <th>Auth Date</th>
                                    <th>Auth By</th>
                                </tr>
                            </thead>
                        </table>
                    </div>
                </div>
            </div>
            <div role="tabpanel" class="tab-pane fade"
                       id="tab_content3" aria-labelledby="comm-rates-tab">

            <div class="x_panel">
                <div class="x_title">
                    <h4>Required Documents</h4>
                </div>
                <div class="cutom-container"  aria-labelledby="profile-tab">
                    <button class="btn btn-info" id="btn-add-clmdocs">New</button>
                    <table id="clm_required_docs_tbl" class="table table-hover table-bordered" style="width:100%">
                        <thead>
                        <tr>
                            <th>Document Desc</th>
                            <th>Ref</th>
                            <th>File Name</th>
                            <th>Upload Date</th>
                            <th>Uploaded By</th>
                            <th>Remarks</th>
                            <th ></th>
                            <th></th>
                            <th></th>
                            <th></th>
                        </tr>
                        </thead>
                    </table>
                </div>
            </div>
            <div class="x_panel">
                <div class="x_title">
                    <h4>Other Claim Documents</h4>
                </div>
                <button type="button" class="btn btn-info" id="btn-add-upload">Upload Document</button>
                <div class="cutom-container">
                    <table id="clm_req_docs_tbl" class="table table-hover table-bordered">
                        <thead>
                        <tr>
                            <th>File ID</th>
                            <th>File Name</th>
                            <th>Date Added</th>
                            <th>Added By</th>
                            <th>Comment</th>
                            <th width="5%"></th>
                            <th width="5%"></th>
                        </tr>
                        </thead>
                    </table>
                </div>
            </div>
        </div>
            <div role="tabpanel" class="tab-pane fade"
                 id="tab_content4" aria-labelledby="activity-tab">

                <div class="x_panel">
                    <div class="x_title">
                        <h4>Claim Progress</h4>
                    </div>
                    <div class="cutom-container">
                        <button type="button" class="btn btn-info"
                                id="btn-add-activity">New</button>
                        <table id="clm_activities_tbl" class="table table-hover table-bordered">
                            <thead>
                            <tr>
                                <th>Activity</th>
                                <th>Action By</th>
                                <th>Date</th>
                                <th>Current Activity</th>
                                <th>Reminder Date</th>
                            </tr>
                            </thead>
                        </table>
                    </div>
                </div>

            </div>

        </div>
    </div>
<%--        <hr>--%>
<%--        <div class="col-md-12 col-sm-12 col-xs-12">--%>
<%--            <div class="col-md-6 col-xs-12">--%>
<%--                <label for="reserve-total" class="control-label col-md-7">--%>
<%--                    Total Pending Payments</label>--%>
<%--                <div class="col-md-5 col-xs-5">--%>
<%--                    <p class="form-control-static" id="reserve-total"> </p>--%>
<%--                </div>--%>
<%--            </div>--%>
<%--            <div class="col-md-6 col-xs-12">--%>
<%--                <label for="payments-total" class="control-label col-md-7">--%>
<%--                    Total Payments</label>--%>
<%--                <div class="col-md-5 col-xs-5">--%>
<%--                    <p class="form-control-static" id="payments-total"> </p>--%>
<%--                </div>--%>
<%--            </div>--%>
<%--        </div>--%>
    </div>
    </div>


<div class="modal fade" id="perilTransModal" tabindex="-1" role="dialog"
     aria-labelledby="perilTransModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="perilTransModalLabel">
                    Add A new Peril
                </h4>
            </div>
            <div class="modal-body">
                <form id="new-peril-form" class="form-horizontal">
                    <div class="form-group">
                        <input type="hidden" name="riskId"  id="myRiskId"/>
                        <label for="brn-id" class="col-md-4 control-label">Self as
                            Claimant</label>

                        <div class="col-md-8 checkbox">
                            <label>
                                <input type="hidden" name="selfAsClaimant" id="self-as-clmnt2"/>
                                <input type="checkbox" id="self-as-clmnt"/>
                            </label>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="brn-id" class="col-md-4 control-label">Claimant</label>
                        <div class="col-md-8">
                            <div class="col-md-11">
                                <input type="hidden" name="claimantCode" id="clmt-code"/>
                                <input type="hidden" id="clmt-name">
                                <div id="clmant-def" class="form-control"
                                     select2-url="<c:url value="/protected/claims/selClaimants"/>" >

                                </div>
                            </div>
                            <div class="col-md-1">
                                <input type="button" class="btn-xs btn-success btn-info" id="btn-add-claimant" value="New">
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="brn-id" class="col-md-4 control-label">Peril</label>

                        <div class="col-md-8">
                            <input type="hidden" name="perilCode"  id="peril-code"/>
                            <input type="hidden" id="peril-name"/>
                            <div id="peril-def" class="form-control"
                                 select2-url="<c:url value="/protected/claims/selSubclassPerils"/>" >

                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="brn-id" class="col-md-4 control-label">Claim Estimate</label>

                        <div class="col-md-8">
                            <input type='text' class="form-control pull-right"
                                 name="perilEstimate"  id="clm-estimate" />
                        </div>
                    </div>
                    <input type="hidden" name="expireSectionId" id="expire-section-id"/>
                    <input type="hidden" name="expireSection" id="expire-section"/>
                </form>
            </div>
            <div class="modal-footer">
                <button data-loading-text="Saving..." id="btn-add-peril-selected"
                        type="button" class="btn btn-primary">
                    Save
                </button>
                <button type="button" class="btn btn-default" data-dismiss="modal">
                    Cancel
                </button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="claimantsModal" tabindex="-1" role="dialog"
     aria-labelledby="claimantsModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="claimantsModalLabel">
                    Edit/Add Claimant
                </h4>
            </div>
            <div class="modal-body" id="branch_model">
                <form id="claimants-form" class="form-horizontal">
                    <input type="hidden" class="form-control" id="clmnt-id" name="claimantId">
                    <div class="form-group">
                        <label for="brn-id" class="col-md-3 control-label">Surname</label>

                        <div class="col-md-8">
                            <input type="text" class="form-control" id="clmnt-surname"
                                   name="surname"  required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="unit-id" class="col-md-3 control-label">Other Names</label>

                        <div class="col-md-8">
                            <input type="text" class="form-control" id="clmnt-othernames"
                                   name="otherNames"  required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="unit-id" class="col-md-3 control-label">Occupation</label>

                        <div class="col-md-8">
                            <input type="hidden" id="clmt-occupt" name="occupation"/>
                            <input type="hidden" id="clmt-occupt-names"/>
                            <div id="occup-def" class="form-control"
                                 select2-url="<c:url value="/protected/claims/selOccupations"/>" >

                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="unit-id" class="col-md-3 control-label">ID Number</label>

                        <div class="col-md-8">
                            <input type="text" class="form-control" id="clmnt-idnumber"
                                   name="idNumber"  required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="unit-id" class="col-md-3 control-label">Address</label>

                        <div class="col-md-8">
                            <textarea class="form-control" rows="2" id="clmnt-address" name="address"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="unit-id" class="col-md-3 control-label">Mobile Number</label>

                        <div class="col-md-8">
                            <input type="text" class="form-control" id="clmnt-mobnumber"
                                   name="mobileNo"  required>
                        </div>
                    </div>

                </form>
            </div>
            <div class="modal-footer">
                <button data-loading-text="Saving..." id="saveClaimantDef"
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

<div class="modal fade" id="uploadClmModal" tabindex="-1" role="dialog"
     aria-labelledby="uploadClmModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <form id="clm-doc-form" class="form-horizontal">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="uploadClmModalLabel">
                    Upload A Claim File
                </h4>
            </div>
            <div class="modal-body">


                    <input type="hidden" class="form-control" id="upload-code" name="uploadId">
                    <input type="hidden" class="form-control" id="upload-clm-id" name="claimBookings">
                    <div class="form-group">
                        <label for="brn-id" class="col-md-3 control-label">File ID</label>

                        <div class="col-md-8">
                            <input type="text" class="form-control" id="upload-sht-id"
                                   name="fileId">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="cou-name" class="col-md-3 control-label">File</label>

                        <div class="col-md-8">
                            <label class="custom-file">
                                <input type="file" id="file" class="custom-file-input" name="file">
                                <span class="custom-file-control"></span>
                            </label>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="rate-taxable" class="col-md-3 control-label">Comment</label>
                        <div class="col-md-8">
                            <label>
                                <textarea class="form-control" rows="3" id="comment-desc" name="uploadedComment"></textarea>
                            </label>
                        </div>
                    </div>
            </div>
            <div class="modal-footer">
                <input type="submit" class="btn btn-success" value="Save">
                <button type="button" class="btn btn-default" data-dismiss="modal">
                    Cancel
                </button>
            </div>
            </form>
        </div>
    </div>
</div>


<div class="modal fade" id="clmReqDocsModal" tabindex="-1" role="dialog"
     aria-labelledby="clmReqDocsModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"  aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="clmReqDocsModalLabel">Select Required Docs</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal">
                    <div class="form-group">
                        <label for="doc-name-search" class="col-md-3 control-label">Document Name</label>
                        <div class="col-md-6">
                            <input type="text" class="form-control" id="doc-name-search" name="req-doc">
                        </div>
                        <div class="col-md-1">
                            <button  id="searchDocuments" type="button" class="btn btn-primary"> Search </button>
                        </div>
                    </div>
                </form>
                <div style="height: 300px !important; overflow: scroll;">
                    <table class="table table-striped table-hover table-bordered table-fixed" id="risksReqDocsTbl">
                        <thead>
                        <tr>
                            <th width="1%"></th>
                            <th width="4%">Document Id</th>
                            <th width="12%">Document Name</th>
                        </tr>
                        </thead>
                        <tbody> </tbody>
                    </table>
                </div>
                <form id="req-clm-docs-form">
                    <input type="hidden" id="req-risk-code" name="subCode"/>
                </form>
            </div>
            <div class="modal-footer">
                <button data-loading-text="Saving..." id="saveReqClmDocsBtn"
                        type="button" class="btn btn-success">Save</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="uploadClmReqModal" tabindex="-1" role="dialog"
     aria-labelledby="uploadClmReqModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <form id="clm-req-form" class="form-horizontal">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"
                            aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title" id="uploadClmReqModalLabel">
                        Upload A Claim File
                    </h4>
                </div>
                <div class="modal-body">


                    <input type="hidden" class="form-control" id="uploadreq-code" name="clmRequiredId">
                    <input type="hidden" class="form-control" id="trans-type" name="transType">
                    <div class="form-group">
                        <label for="brn-id" class="col-md-3 control-label">Doc Ref No</label>

                        <div class="col-md-8">
                            <input type="text" class="form-control" id="doc-ref-no"
                                   name="docRefNo">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="rate-taxable" class="col-md-3 control-label">Remarks</label>
                        <div class="col-md-8">
                            <label>
                                <textarea class="form-control" rows="3" id="req-remarks" name="remarks"></textarea>
                            </label>
                        </div>
                    </div>
                    <div class="form-group uploadfile" >
                        <label for="cou-name" class="col-md-3 control-label">File</label>

                        <div class="col-md-8">
                            <label class="custom-file">
                                <input type="file" id="req-doc-file" class="custom-file-input" name="file">
                                <span class="custom-file-control"></span>
                            </label>
                        </div>
                    </div>







                </div>
                <div class="modal-footer">
                    <input type="submit" class="btn btn-success" value="Save">
                    <button type="button" class="btn btn-default" data-dismiss="modal">
                        Cancel
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>




<div class="modal fade" id="activityModal" tabindex="-1" role="dialog"
     aria-labelledby="activityModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="activityModalLabel">
                    Add new Activity
                </h4>
            </div>
            <div class="modal-body">
                <form id="clm-act-form" class="form-horizontal">
                    <div class="form-group">
                        <label for="houseId" class="control-label col-md-5">
                            Select Activity<span class="required">*</span></label>
                        <div class="col-md-7 col-xs-12">
                            <input type="hidden"  id="activity-code" name="activity" />
                            <input type="hidden" id="activity-desc" />
                            <div id="clm-activity" class="form-control"
                                 select2-url="<c:url value="/protected/claims/selclmActivity"/>" >

                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="houseId" class="control-label col-md-5">
                            Reminder Date<span class="required">*</span></label>
                        <div class="col-md-7 col-xs-12">
                            <div class='input-group date datepicker-input' id="act-reminder-date">
                                <input type="text"  class="form-control pull-right"
                                       id="net-review-date" required="true" name="remDate" />
                                <div class="input-group-addon">
                                    <span class="glyphicon glyphicon-calendar"></span>
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button data-loading-text="Saving..." id="saveClmActivity"
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


<div class="modal fade" id="paymentModal" tabindex="-1" role="dialog"
     aria-labelledby="paymentModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="paymentModalLabel">
                    Add/Edit Payment
                </h4>
            </div>
            <div class="modal-body">
                <form id="clm-pay-form" class="form-horizontal">
                    <input type="hidden" class="form-control" id="peril-pk" name="claimPerils">
                    <input type="hidden" class="form-control" id="payment-id" name="clmPymntId">
                    <div class="form-group">
                        <label for="brn-id" class="col-md-4 control-label">Payee<span class="required">*</span></label>
                        <div class="col-md-7 col-xs-12">
                            <input type='text' class="form-control pull-right"
                                   name="payee"  id="payee-name" required="true"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="brn-id" class="col-md-4 control-label">Payment Reference<span class="required">*</span></label>
                        <div class="col-md-7 col-xs-12">
                            <input type='text' class="form-control pull-right"
                                   name="pymntRef"  id="pay-ref" required="true"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="houseId" class="col-md-4 control-label">Payment Type</label>

                        <div class="col-md-7 col-xs-12">
                            <select class="form-control" id="payment-type" name="pymntType" required>
                                <option value="">Select Payment Type</option>
                                <option value="N">Normal</option>
                                <option value="X">Exgratia</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="houseId" class="control-label col-md-4">
                            Date Paid<span class="required">*</span></label>
                        <div class="col-md-7 col-xs-12">
                            <div class='input-group date datepicker-input' id="payment-date">
                                <input type="text"  class="form-control pull-right"
                                       id="pay-date" required="true" name="pymntDate" />
                                <div class="input-group-addon">
                                    <span class="glyphicon glyphicon-calendar"></span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="brn-id" class="col-md-4 control-label">Paid Amount<span class="required">*</span></label>
                        <div class="col-md-7 col-xs-12">
                            <input type='text' class="form-control pull-right"
                                   name="clmPymntAmount"  id="paid-amount" required="true"/>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button data-loading-text="Saving..." id="saveClmPayment"
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


<div class="modal fade" id="claimPerilModal" tabindex="-1" role="dialog"
     aria-labelledby="claimPerilModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="claimPerilModalLabel">
                    Edit Peril
                </h4>
            </div>
            <div class="modal-body">
                <form id="clm-peril-form" class="form-horizontal">
                    <input type="hidden" class="form-control" id="peril-id" name="clmPerilId">
                    <div class="form-group">
                        <label for="brn-id" class="col-md-4 control-label">Peril</label>
                        <div class="col-md-7 col-xs-12">
                            <input type='text' class="form-control pull-right"
                                   id="peril-desc" readonly="true"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="brn-id" class="col-md-4 control-label">Claim Estimate <span class="required">*</span></label>
                        <div class="col-md-7 col-xs-12">
                            <input type='text' class="form-control pull-right"
                                   name="reserve"  id="reserve-amount" required="true"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="brn-id" class="col-md-4 control-label">Remarks</label>
                        <div class="col-md-7 col-xs-12">
                            <input type='text' class="form-control pull-right"
                                   name="remarks"  id="remarks-desc"/>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button data-loading-text="Saving..." id="saveClmPeril"
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

<div class="modal fade" id="reportsModal" tabindex="-1" role="dialog"
     aria-labelledby="reportsModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="reportsModalLabel">Reports</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-md-6">

                        <ul style="list-style-type: none;" class="reports-links">
                            <li class="clm-synopsis-dip"><a
                                    href="<c:url value='/protected/claims/rpt_claim_synopsis'/> "
                                    target="_blank">Claim Synopsis</a></li>
                            <li>
                            <li ><a href="javascript:void(0);"  onclick="UTILITIES.sendEmail();">Send Email</a></li>
                        </ul>
                    </div>
                    <div class="col-md-6">

                        <ul style="list-style-type: none;">
                            <!--<li><a href="#">Invoice Note</a></li> -->
                        </ul>

                    </div>
                </div>


            </div>
            <div class="modal-footer">

                <button type="button" class="btn btn-default" data-dismiss="modal">
                    Close</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="claimStatusModal" tabindex="-1" role="dialog"
     aria-labelledby="claimStatusModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h3 class="modal-title" id="claimStatusModalLabel">Close/Re-open Claim</h3>
            </div>
            <div class="modal-body">
                <form id="clm-status-form">
                    <input type="hidden" class="form-control" id="new-status" name="newStatus">
                    <div id="closedStatus" class="form-group">
                            <label for="clsStatus" class="col-form-label" >Closed As: <span
                                    class="required">*</span></label>
                                <select class="form-control " id="clsStatus" name="closeReason">
                                    <option class="dropdown-item" value="ST">Closed As Settled</option>
                                    <option class="dropdown-item" value="RJ">Closed As Rejected</option>
                                    <option class="dropdown-item" value="NC">Closed As No Claim</option>
                                </select>
                    </div>
                    <div class="form-group">
                        <label for="clm-remarks" class="col-form-label">Remarks: <span class="required">*</span></label>
                        <textarea class="form-control" rows="5" cols="20" id="clm-remarks" name="remarks" required="true"></textarea>
                    </div>
                </form>

                </div>
            <div class="modal-footer">
                <button data-loading-text="Saving..." id="saveClmStatus"
                        type="button" class="btn btn-primary"> Save </button>
                <button type="button" class="btn btn-secondary" data-dismiss="modal">
                    Cancel
                </button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="emailModal" tabindex="-1" role="dialog"
     aria-labelledby="emailModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="emailModalLabel">Send Email</h4>
            </div>
            <div class="modal-body">
                <form id="email-form"  class="form-horizontal">
                    <div class="form-group">
                        <label for="cou-name" class="col-md-2 control-label">Email To</label>

                        <div class="col-md-10">
                            <select class="form-control" id="email-to" name="receiverType">
                                <option value="">Select Email To</option>
                                <option value="C">Client</option>
                                <option value="A">Intermediary</option>
                                <option value="IN">Inhouse</option>
                                <option value="B">Both</option>

                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="cou-name" class="col-md-2 control-label">Send To</label>

                        <div class="col-md-10">
                            <input type="text" class="editUserCntrls form-control"
                                   id="email-send-to" name="sendTo"
                                   readonly>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="cou-name" class="col-md-2 control-label">CC</label>

                        <div class="col-md-10">
                            <input type="text" class="editUserCntrls form-control"
                                   id="email-cc" name="sendCC"
                            >
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="cou-name" class="col-md-2 control-label">BCC</label>

                        <div class="col-md-10">
                            <input type="text" class="editUserCntrls form-control"
                                   id="email-bcc" name="sendBcc"
                            >
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="cou-name" class="col-md-2 control-label">Reports</label>

                        <div class="col-md-10">
                            <label class="checkbox-inline clm-synop-dip"><input type="checkbox" value="SY">Claim Synopsis</label>

                        </div>
                    </div>
                    <div class="form-group">
                        <label for="cou-name" class="col-md-2 control-label">Subject</label>

                        <div class="col-md-10">
                            <input type="text" class="editUserCntrls form-control"
                                   id="email-subject" name="subject"
                                   required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="unit-id" class="col-md-2 control-label">Email Template</label>

                        <div class="col-md-10">
                            <textarea class="form-control" rows="10" cols="20" id="email-template" name="message"></textarea>
                        </div>
                    </div>

                </form>

            </div>
            <div class="modal-footer">
                <button data-loading-text="Saving..." id="sendEmailForm"
                        type="button" class="btn btn-success">Send</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">
                    Close</button>
            </div>
        </div>
    </div>
</div>


<div class="modal fade" id="claimStatusHistModal" tabindex="-1" role="dialog"
     aria-labelledby="claimStatusHistModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="claimStatusHistModalLabel">
                    Claim Status History
                </h4>
            </div>
            <div class="modal-body">
                <table id="clm_status_tbl" class="table table-hover table-bordered">
                    <thead>
                    <tr>
                        <th>Status</th>
                        <th>Captured By</th>
                        <th>Status Date</th>
                        <th>Current?</th>
                        <th>Remarks</th>
                    </tr>
                    </thead>
                    <tbody>

                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">
                    OK
                </button>
            </div>
        </div>
    </div>
</div>