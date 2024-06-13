<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript" src="<c:url value="/js/modules/utils/select2builder.js"/>"></script>
<script type="text/javascript" src="<c:url value="/libs/rivets/rivets.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/modules/claims/enquireclaims.js"/>"></script>
<div class="x_panel">
    <div class="x_title">
        <h4>Claim Enquiry</h4>
    </div>
    <form id="search-form" class="form-horizontal">
        <div class="form-group">

            <div class="col-md-6 col-xs-12">
                <label for="brn-id" class="col-md-4 control-label">Policy
                    No.</label>

                <div class="col-md-8 col-xs-12">
                    <input type='text' class="form-control pull-right"
                           id="pol-search-number" placeholder="Policy No" />
                </div>
            </div>
            <div class="form-group riskIdgroup">
                <div class="col-md-6 col-xs-12" >
                    <label for="brn-id" class="col-md-4 control-label">Risk
                        ID.</label>

                    <div class="col-md-8 col-xs-12">
                        <input type='text' class="form-control pull-right"
                               id="rev-search-number" placeholder="Risk ID"/>
                    </div>
                </div>
            </div>
        </div>
        <div class="form-group">
            <div class="col-md-6 col-xs-12" >
                <label for="brn-id" class="col-md-4 control-label">Claim No</label>

                <div class="col-md-8 col-xs-12">
                    <input type='text' class="form-control pull-right"
                           id="rev-claim-number" placeholder="Claim No"/>
                </div>
            </div>
            <div class="col-md-6 col-xs-12">
                <label for="brn-id" class="col-md-4 control-label">Client
                </label>

                <div class="col-md-8 col-xs-12">
                    <input type='hidden' class="form-control pull-right"
                           id="rev-search-name" />
                    <div id="client-frm" class="form-control"
                         select2-url="<c:url value="/protected/uw/policies/uwClients"/>" >

                    </div>
                </div>
            </div>

        </div>
        <div class="form-group">
            <input type="button" class="btn btn-info pull-right"
                   style="margin-right: 10px;" value="Search"
                   id="btn-search-claims">
        </div>


    </form>
    <div class="cutom-container">
    <table id="clm_enquiry_tbl" class="table table-hover table-bordered">
        <thead>
        <tr>
            <th>Claim No</th>
            <th>Risk ID</th>
            <th>Policy Number</th>
            <th>Client Name</th>
            <th>Loss Date</th>
            <th>Status</th>
            <th>Notification Date</th>
            <th>Booked By</th>
            <th width="5%"></th>
        </tr>
        </thead>
    </table>
        </div>
    </div>