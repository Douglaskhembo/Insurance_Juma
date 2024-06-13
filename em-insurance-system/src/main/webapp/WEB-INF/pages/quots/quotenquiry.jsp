<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript" src="<c:url value="/js/modules/utils/select2builder.js"/>"></script>
<script type="text/javascript" src="<c:url value="/libs/rivets/rivets.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/modules/quotes/quotes.js"/>"></script>
<div class="x_panel">
    <div class="x_title">
        <h4>Quotes Enquiry</h4>
    </div>
    <form id="search-form" class="form-horizontal">
        <div class="form-group">
            <div class="col-md-6 col-xs-12">
                <label for="brn-id" class="col-md-4 control-label">Quotation Number
                    No.</label>

                <div class="col-md-8 col-xs-12">
                    <input type='text' class="form-control pull-right"
                           id="quote-search-number" />
                </div>
            </div>
            <div class="col-md-6 col-xs-12">
                <label for="brn-id" class="col-md-4 control-label">Client</label>
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
                <label for="brn-id" class="col-md-4 control-label">Prospect
                </label>

                <div class="col-md-8 col-xs-12">
                    <input type='hidden' class="form-control pull-right"
                           id="prs-search-name" />
                    <div id="prospects-frm" class="form-control"
                         select2-url="<c:url value="/protected/quotes/selprospects"/>" >

                    </div>
                </div>
            </div>
            <div class="col-md-6 col-xs-12">

            </div>

        </div>
        <div class="form-group">
            <input type="button" class="btn btn-primary pull-right"
                   style="margin-right: 10px;" value="Search"
                   id="btn-search-quotes">
        </div>


    </form>
    <div class="cutom-container">
    <table id="quot_enquiry" class="table table-hover table-bordered">
        <thead>
        <tr>
            <th>Quot No</th>
            <th>Revision. No</th>
            <th>Cover From</th>
            <th>Cover To</th>
            <th>Client/Prospect</th>
            <th>Currency</th>
            <th>Status</th>
            <th>Prep. By</th>
            <th width="5%"></th>
            <th width="5%"></th>
            <th width="5%"></th>
            <th width="5%"></th>
        </tr>
        </thead>
    </table>
        </div>
</div>