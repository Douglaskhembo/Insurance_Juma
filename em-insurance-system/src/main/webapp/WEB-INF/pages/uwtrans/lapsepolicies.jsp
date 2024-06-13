<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<script type="text/javascript" src="<c:url value="/js/modules/utils/select2builder.js"/>"></script>
<script type="text/javascript" src="<c:url value="/libs/rivets/rivets.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/modules/uwtrans/lapsepols.js"/>"></script>
<div class="x_panel">
    <div class="card-box table-responsive">
        <div class="x_title">
            <h4>Policy Lapsation/Unlapsation</h4>
        </div>
        <form id="search-form" class="form-horizontal">
            <div class="form-group">

                <div class="col-md-6 col-xs-12">
                    <label for="brn-id" class="col-md-4 control-label">Business Class.</label>
                    <div>
                        <INPUT TYPE="radio" name="businessType" id="gBusiness" onchange="toggleNonMedBusinessClass()" value="G"/>Non-Medical
                    </div>
                    <div>
                        <INPUT TYPE="radio" NAME="businessType" id="mBusiness" onchange="toggleMedBusinessClass()" VALUE="M"/>Medical
                    </div>


                </div>
                <div class="col-md-6 col-xs-12">

                </div>
            </div>
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
                <div class="col-md-6 col-xs-12">
                    <label for="brn-id" class="col-md-4 control-label">DR
                        No.</label>

                    <div class="col-md-8 col-xs-12">
                        <input type='text' class="form-control pull-right"
                               id="dr-search-number" placeholder="Ref No"/>
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
                <div class="col-md-6 col-xs-12">
                    <label for="brn-id" class="col-md-4 control-label">Intermediary
                    </label>

                    <div class="col-md-8 col-xs-12">
                        <input type='hidden' class="form-control pull-right"
                               id="agent-search-number" />
                        <div id="acc-frm" class="form-control"
                             select2-url="<c:url value="/protected/setups/binders/selAccounts"/>" >
                        </div>
                    </div>
                </div>
                <div class="col-md-6 col-xs-12">
                    <label for="brn-id" class="col-md-4 control-label">Product
                    </label>

                    <div class="col-md-8 col-xs-12">
                        <input type='hidden' class="form-control pull-right"
                               id="product-search-number" />
                        <div id="prd-code" class="form-control"
                             select2-url="<c:url value="/protected/setups/binders/selproducts"/>" >
                        </div>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <input type="button" class="btn btn-info pull-right"
                       style="margin-right: 10px;" value="Search"
                       id="btn-search-policies">
            </div>


        </form>
        <table id="pol_enquiry_tbl" class="table table-hover table-bordered">
            <thead>
            <tr>
                <th>Policy No</th>
                <th>Endors. No</th>
                <th>Product</th>
                <th>Cover From</th>
                <th>Cover To</th>
                <th>Client</th>
                <th>Ins Company</th>
                <th>Currency</th>
                <th>Prep. By</th>
                <th>Status</th>
                <th width="5%"></th>
            </tr>
            </thead>
        </table>
    </div>
</div>