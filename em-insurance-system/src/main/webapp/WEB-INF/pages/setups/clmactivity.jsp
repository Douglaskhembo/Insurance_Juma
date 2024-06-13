<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script type="text/javascript" src="<c:url value="/js/modules/setups/clmactivity.js"/>"></script>

<div class="x_panel">
    <button class="btn btn-success btn btn-info pull-right" id="btn-add-clm-activity">New</button>
    <div class="x_title">
        <h4>Claim Activities</h4>
    </div>
    <div class="cutom-container">
    <table id="clm-activities-tbl" class="table table-hover table-bordered">
        <thead>
        <tr>

            <th>Claim Activity</th>
            <th width="5%"></th>
            <th width="5%"></th>
        </tr>
        </thead>
    </table>
        </div>
</div>


<div class="modal fade" id="clmActivityModal" tabindex="-1" role="dialog"
     aria-labelledby="clmActivityModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="clmActivityModalLabel">
                    Edit/Add Claim Activity
                </h4>
            </div>
            <div class="modal-body" id="branch_model">
                <form id="claim-activity-form" class="form-horizontal">
                    <input type="hidden" class="form-control" id="act-id" name="caId">
                    <div class="form-group">
                        <label for="unit-id" class="col-md-3 control-label">Causation Desc</label>

                        <div class="col-md-8">
                            <input type="text" class="form-control" id="act-desc"
                                   name="activityDesc"  required>
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