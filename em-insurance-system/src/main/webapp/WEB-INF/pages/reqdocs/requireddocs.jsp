<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript"
        src="<c:url value="/js/modules/utils/select2builder.js"/>"></script>
<script type="text/javascript"
        src="<c:url value="/js/modules/reqdocs/requireddocs.js"/>"></script>
<div class="x_panel">
    <div class="x_title">
        <h2>
            Required Documents
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
                                                          id="home-tab" role="tab" data-toggle="tab" aria-expanded="true">Documents</a>
                </li>
                <li role="presentation" class=""><a href="#tab_content2"
                                                    role="tab" id="profile-tab" data-toggle="tab" aria-expanded="false">Sub Class Documents</a>
                </li>
            </ul>
            <div id="myTabContent" class="tab-content">
                <div role="tabpanel" class="tab-pane fade active in"
                     id="tab_content1" aria-labelledby="home-tab">
                    <button type="button" class="btn btn-info pull-right"
                            id="btn-add-req-doc">New</button>
                    <div class="cutom-container">
                    <table id="requireddoctbl" class="table table-hover table-bordered">
                        <thead>
                        <tr>

                            <th>Id</th>
                            <th>Desc</th>
                            <th>NB</th>
                            <th>EN</th>
                            <th>Ren</th>
                            <th>Cert</th>
                            <th>Claims</th>
                            <th>Insurance Doc</th>
                            <th>Sub Agent Doc</th>
                            <th>Individual Client Doc</th>
                            <th>Corporate Client Doc</th>
                            <th>Valid Period</th>
                            <th>Period Type</th>
                            <th width="5%"></th>
                            <th width="5%"></th>
                        </tr>
                        </thead>
                    </table>
                        </div>

                </div>
                <div role="tabpanel" class="tab-pane fade" id="tab_content2"
                     aria-labelledby="profile-tab" >
                    <form class="form-horizontal">
                        <div class="form-group">
                            <label for="brn-id" class="col-md-3 control-label">Select Sub Class</label>

                            <div class="col-md-4">
                                <input type="hidden" id="sub-code"/>
                                <div id="sub-class-def" class="form-control"
                                     select2-url="<c:url value="/protected/setups/clauses/subclassSelect"/>">

                                </div>
                                <input type="hidden" id="class-pk">

                            </div>
                        </div>

                    </form>
                    <button type="button" class="btn btn-info pull-right"
                            id="btn-add-sub-req-docs">New</button>
                    <table id="subrequireddoctbl" class="table table-hover table-bordered">
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
        </div>
    </div>
</div>


<div class="modal fade" id="reqdocsModal" tabindex="-1" role="dialog"
     aria-labelledby="reqdocsModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="reqdocsModalLabel">
                    Add/Edit Required Documents
                </h4>
            </div>
            <div class="modal-body">

                <form id="reqd-doc-form" class="form-horizontal">
                    <input type="hidden" class="form-control" id="reqd-code" name="reqId">
                    <div class="form-group">
                        <label for="brn-id" class="col-md-4 control-label">Document ID:</label>

                        <div class="col-md-8">
                            <input type="text" class="form-control" id="reqd-doc-id"
                                   name="reqShtDesc"  required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="cou-name" class="col-md-4 control-label">Document Description</label>

                        <div class="col-md-8">
                            <input type="text" class="form-control" id="reqd-doc-desc"
                                   name="reqDesc"  required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="rate-taxable" class="col-md-4 control-label">New Business Doc?</label>
                        <div class="col-md-8 checkbox">
                            <label>
                                <input type="checkbox" name="appliesNewBusiness" id="chk-nb">
                            </label>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="rate-taxable" class="col-md-4 control-label">Endorsement Doc?</label>
                        <div class="col-md-8 checkbox">
                            <label>
                                <input type="checkbox" name="appliesEndorsement" id="chk-en">
                            </label>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="rate-taxable" class="col-md-4 control-label">Renewal Doc?</label>
                        <div class="col-md-8 checkbox">
                            <label>
                                <input type="checkbox" name="appliesRenewal" id="chk-rn">
                            </label>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="rate-taxable" class="col-md-4 control-label">Certificate Doc?</label>
                        <div class="col-md-8 checkbox">
                            <label>
                                <input type="checkbox" name="appliesCertificate" id="chk-cert">
                            </label>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="rate-taxable" class="col-md-4 control-label">Claims Doc?</label>
                        <div class="col-md-8 checkbox">
                            <label>
                                <input type="checkbox" name="appliesLossOpening" id="chk-lop">
                            </label>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="rate-taxable" class="col-md-4 control-label">Individual Client Doc?</label>
                        <div class="col-md-8 checkbox">
                            <label>
                                <input type="checkbox" name="appliesClient" id="chk-clnt">
                            </label>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="rate-taxable" class="col-md-4 control-label">Corporate Client Doc?</label>
                        <div class="col-md-8 checkbox">
                            <label>
                                <input type="checkbox" name="appliesCorpClient" id="chk-cop-clnt">
                            </label>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="rate-taxable" class="col-md-4 control-label">Intermediary Doc?</label>
                        <div class="col-md-8 checkbox">
                            <label>
                                <input type="checkbox" name="appliesAccount" id="chk-acct">
                            </label>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="rate-taxable" class="col-md-4 control-label">Sub Agents Doc?</label>
                        <div class="col-md-8 checkbox">
                            <label>
                                <input type="checkbox" name="appliesSubAgent" id="chk-sub-acct">
                            </label>
                        </div>
                    </div>
                    <div class="form-group validity">
                        <label for="cou-name" class="col-md-4 control-label">Valid Period</label>

                        <div class="col-md-8">
                            <input type="number" class="form-control" id="req-period"
                                   name="period">
                        </div>
                    </div>
                    <div class="form-group validity">
                        <label for="cou-name" class="col-md-4 control-label">Period Type</label>

                        <div class="col-md-8">
                            <select class="form-control" id="req-period-type" name="periodType">
                                <option value="">Select Period Type</option>
                                <option value="D">Days</option>
                                <option value="W">Weeks</option>
                                <option value="M">Months</option>
                                <option value="Y">Years</option>
                            </select>
                        </div>
                    </div>


                </form>
            </div>
            <div class="modal-footer">
                <button data-loading-text="Saving..." id="saveReqdDocs"
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


<div class="modal fade" id="subreqDocsModal" tabindex="-1" role="dialog"
     aria-labelledby="subreqDocsModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="subreqDocsModalLabel">Select Required Document</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal">
                    <div class="form-group">
                        <label for="brn-id" class="col-md-3 control-label">Document Name</label>

                        <div class="col-md-6">
                            <input type="text" class="form-control" id="sub-name-search"
                            >
                        </div>
                        <div class="col-md-1">
                            <button  id="searchSubclasses"
                                     type="button" class="btn btn-primary">
                                Search
                            </button>
                        </div>
                    </div>
                </form>
                <div style="height: 300px !important; overflow: scroll;">
                    <table class="table table-striped table-hover table-bordered table-fixed" id="subrequiredDocsTbl">
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
                <form id="sub-req-docs-form">
                    <input type="hidden" id="req-doc-sub-code" name="subCode"/>
                </form>
            </div>
            <div class="modal-footer">
                <button data-loading-text="Saving..." id="saveSubclassReqdDoc"
                        type="button" class="btn btn-success">Save</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">
                    Cancel</button>
            </div>
        </div>
    </div>
</div>


<div class="modal fade" id="editsubReqdocsModal" tabindex="-1" role="dialog"
     aria-labelledby="editsubReqdocsModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="editsubReqdocsModalLabel">
                    Edit Sub Class Required Documents
                </h4>
            </div>
            <div class="modal-body">

                <form id="sub-reqd-doc-form" class="form-horizontal">
                    <input type="hidden" class="form-control" id="sub-reqd-code" name="sclReqrdId">
                    <input type="hidden" class="form-control" id="sub-reqd-sub-code" name="subclass">
                    <input type="hidden" class="form-control" id="sub-reqd-doc-code" name="requiredDoc">
                    <div class="form-group">
                        <label for="brn-id" class="col-md-4 control-label">Document ID:</label>

                        <div class="col-md-8">
                            <input type="text" class="form-control" id="sub-reqd-doc-id"
                                   name="reqShtDesc"  readonly>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="cou-name" class="col-md-4 control-label">Document Description</label>

                        <div class="col-md-8">
                            <input type="text" class="form-control" id="sub-reqd-doc-desc"
                                   name="reqDesc"  readonly>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="rate-taxable" class="col-md-4 control-label">Mandatory?</label>
                        <div class="col-md-8 checkbox">
                            <label>
                                <input type="checkbox" name="mandatory" id="sub-req-mandatory">
                            </label>
                        </div>
                    </div>
            </div>
            <div class="modal-footer">
                <button data-loading-text="Saving..." id="saveSubReqdDocs"
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