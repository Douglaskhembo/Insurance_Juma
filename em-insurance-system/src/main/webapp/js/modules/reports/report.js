/**
 * Created by peter on 2/14/2017.
 */

$(function() {

    $(document).ready(function () {

        $(".datepicker-input").each(function() {
            $(this).datetimepicker({
                format: 'DD/MM/YYYY'
            });
        });


    });
});


var reportModule = (function(){

    function createAccountsForSel(){
        if($("#acc-frm").filter("div").html() != undefined)
        {
            Select2Builder.initAjaxSelect2({
                containerId : "acc-frm",
                sort : 'name',
                change: function(e, a, v){
                    $("#acc-id").val(e.added.acctId);
                },
                formatResult : function(a)
                {
                    return a.name
                },
                formatSelection : function(a)
                {
                    return a.name
                },
                initSelection: function (element, callback) {

                },
                id: "acctId",
                placeholder:"All Agents",
            });
        }
        $("#acc-frm").on("select2-removed", function(e) {
            $("#acc-id").val('');
        });
    }

    var populateSubAgentsLov = function(){
        if($("#sub-agent-frm").filter("div").html() != undefined)
        {
            Select2Builder.initAjaxSelect2({
                containerId : "sub-agent-frm",
                sort : 'name',
                change: function(e, a, v){
                    $("#sub-agent-id").val(e.added.acctId);

                },
                formatResult : function(a)
                {
                    return a.name;
                },
                formatSelection : function(a)
                {
                    return a.name;
                },
                initSelection: function (element, callback) {

                },
                id: "acctId",
                width:"250px",
                placeholder:"Select Sub Agent"

            });

            $("#sub-agent-frm").on("select2-removed", function(e) {
                $("#sub-agent-id").val('');
            })
        }
    };

    function populateClientLov(){
        if($("#client-frm").filter("div").html() != undefined)
        {
            Select2Builder.initAjaxSelect2({
                containerId : "client-frm",
                sort : 'fname',
                change: function(e, a, v){
                    $("#client-id").val(e.added.tenId);

                },
                formatResult : function(a)
                {
                    return a.fname+" "+a.otherNames;
                },
                formatSelection : function(a)
                {
                    return a.fname+" "+a.otherNames;
                },
                initSelection: function (element, callback) {
                },
                id: "tenId",
                placeholder:"All Clients"

            });
        }
        $("#client-frm").on("select2-removed", function(e) {
            $("#client-id").val('');
        });
    }

    function createUserLov(){
        if($("#report-user").filter("div").html() != undefined)
        {
            Select2Builder.initAjaxSelect2({
                containerId : "report-user",
                sort : 'username',
                change: function(e, a, v){
                    $("#userCod").val(e.added.id);
                },
                formatResult : function(a)
                {
                    return a.username
                },
                formatSelection : function(a)
                {
                    return a.username
                },
                initSelection: function (element, callback) {

                },
                id: "id",
                placeholder:"All Users"
            });
        }
        $("#report-user").on("select2-removed", function(e) {
            $("#userCod").val('');
        });
    }

    function createReportPolicies(){
        if($("#report-policies").filter("div").html() != undefined)
        {
            Select2Builder.initAjaxSelect2({
                containerId : "report-policies",
                sort : 'polNo',
                change: function(e, a, v){
                    $("#policy-code").val(e.added.policyId);
                },
                formatResult : function(a)
                {
                    return "Policy: "+a.polNo+"  Revision No: "+ a.polRevNo;
                },
                formatSelection : function(a)
                {
                    return "Policy: "+a.polNo+"  Revision No: "+ a.polRevNo;
                },
                initSelection: function (element, callback) {

                },
                id: "policyId",
                placeholder:"Select Policy"
            });
        }
        $("#report-policies").on("select2-removed", function(e) {
            $("#policy-code").val('');
        });
    }



    function populateUserBranches(){
        if($("#brn-frm").filter("div").html() != undefined)
        {
            Select2Builder.initAjaxSelect2({
                containerId : "brn-frm",
                sort : 'obName',
                change: function(e, a, v){
                    $("#brn-id").val(e.added.obId);
                },
                formatResult : function(a)
                {
                    return a.obName;
                },
                formatSelection : function(a)
                {
                    return a.obName;
                },
                initSelection: function (element, callback) {

                },
                id: "obId",
                width:"250px",
                placeholder:"All Branches"

            });
        }
        $("#brn-frm").on("select2-removed", function(e) {
            $("#brn-id").val('');
        });
    }


    function createCertTypeLov(){
        if($("#cert-type").filter("div").html() != undefined)
        {
            Select2Builder.initAjaxSelect2({
                containerId : "cert-type",
                sort : 'certDesc',
                change: function (e, a, v) {
                    $("#cert-type-pk").val(e.added.certId);
                },
                formatResult : function(a)
                {
                    return a.certDesc
                },
                formatSelection : function(a)
                {
                    return a.certDesc
                },
                initSelection: function (element, callback) {

                },
                id: "certId",
                placeholder:"All Certificate Types"
            });
        }
        $("#cert-type").on("select2-removed", function(e) {
            $("#cert-type-pk").val('');
        });
    }

    function populateBinderLov(){
        if($("#binder-frm").filter("div").html() != undefined)
        {
            Select2Builder.initAjaxSelect2({
                containerId : "binder-frm",
                sort : 'binName',
                change: function(e, a, v){
                    $("#risk-binder-code").val(e.added.binId);
                },
                formatResult : function(a)
                {
                    return a.binName;
                },
                formatSelection : function(a)
                {
                    return a.binName;
                },
                initSelection: function (element, callback) {

                },
                id: "binId",
                width:"250px",
                placeholder:"All Binders/Masks"

            });
        }
        $("#binder-frm").on("select2-removed", function(e) {
            $("#risk-binder-code").val('');
        });
    }

    function createProductForSel(){
        if($("#prd-code").filter("div").html() != undefined)
        {
            Select2Builder.initAjaxSelect2({
                containerId : "prd-code",
                sort : 'proDesc',
                change: function(e, a, v){
                    $("#product-code").val(e.added.proCode);
                },
                formatResult : function(a)
                {
                    return a.proDesc
                },
                formatSelection : function(a)
                {
                    return a.proDesc
                },
                initSelection: function (element, callback) {

                },
                id: "proCode",
                placeholder:"All Products",
            });
        }

        $("#prd-code").on("select2-removed", function(e) {
            $("#product-code").val('');
        })
    }

    function createRemmitanceForSel(){
        if($("#remit-def").filter("div").html() != undefined)
        {
            Select2Builder.initAjaxSelect2({
                containerId : "remit-def",
                sort : 'refNo',
                change: function(e, a, v){
                    $("#remit-code").val(e.added.transno);
                },
                formatResult : function(a)
                {
                    return a.refNo
                },
                formatSelection : function(a)
                {
                    return a.refNo
                },
                initSelection: function (element, callback) {

                },
                id: "transno",
                placeholder:"All Remmitances",
            });
        }

        $("#remit-def").on("select2-removed", function(e) {
            $("#remit-code").val('');
        })
    }


    function populateCurrencyLov(){
        if($("#curr-frm").filter("div").html() != undefined)
        {
            Select2Builder.initAjaxSelect2({
                containerId : "curr-frm",
                sort : 'curName',
                change: function(e, a, v){
                    $("#cur-id").val(e.added.curCode);

                },
                formatResult : function(a)
                {
                    return a.curName;
                },
                formatSelection : function(a)
                {
                    return a.curName;
                },
                initSelection: function (element, callback) {

                },
                id: "curCode",
                width:"250px",
                placeholder:"Select Currency"

            });
        }
        $("#curr-frm").on("select2-removed", function(e) {
            $("#cur-id").val('');
        })
    }

    function callReportModal(button){
        var reportCode = decodeURI($(button).data("report"));
        var template  = decodeURI($(button).data("template"));
        $.ajax({
            type: 'GET',
            url:  'getReportParams',
            dataType: 'json',
            data: {"rptId": reportCode},
            async: true,
            success: function(result) {
                $("div.data-inf > div ").each(function(){
                    $(this).remove();
                });
                var counter =1;
                result.forEach(function(data){
                        var markup = ' <div class="form-group report-data"> ';
                            markup+=' <label for="rp-param-id" class="col-md-3 control-label">'+data.paramName+'</label>';
                            markup+='  <div class="col-md-8"> ';
                            markup+='<input type="hidden" value="'+ data.paramActualName+'" name="paramName'+counter+'" class="paramName"> ';
                                if(data.paramType==='T'){
                                    markup+=  '<input type="text" class="editUserCntrls form-control paramValue" name="paramValue'+counter+'" id="'+ data.paramActualName+'" required> ';
                                }
                                else if(data.paramType==="O"){
                                    var arr = data.options.split(",");
                                    var opts="";
                                    for(var x=0;x<arr.length;x++){
                                        opts+=' <option value="'+arr[x]+'">'+arr[x]+'</option> ';
                                    }
                                    markup+= ' <select class="editUserCntrls form-control paramValue" name="paramValue'+counter+'" id="'+ data.paramActualName+'" required> '+
                                        ' <option value="">Select Option Value</option> '+opts+
                                        ' </select> ';
                                }
                                else if(data.paramType==='N'){
                                    markup+=  '<input type="number" class="editUserCntrls form-control paramValue" name="paramValue'+counter+'" id="'+ data.paramActualName+'"  required> ';
                                }
                                else if(data.paramType==='D'){
                                    markup+="<div class='input-group date datepicker-input'> "+
                                   " <input type='text' name='paramValue"+counter+"' class='form-control pull-right paramValue'"+
                                   "id='"+ data.paramActualName+"' required /> "+
                                  "  <div class='input-group-addon'> "+
                                  "      <span class='glyphicon glyphicon-calendar'></span> "+
                                   "     </div> "+
                                   "     </div>";
                                }
                                else if(data.paramType==='L'){
                                    if(data.lovName ==='A'){
                                        markup+='<input type="hidden" id="acc-id" name="paramValue'+counter+'"> <input type="hidden" value="A" name="paramType'+counter+'">'+
                                            '  <div id="acc-frm" class="form-control" '+
                                            ' select2-url="'+requestContextPath+'/protected/reports/accounts" '+
                                            ' </div> '
                                    }
                                    else if(data.lovName ==='C'){
                                        markup+= '<input type="hidden"id="client-id" name="paramValue'+counter+'"><input type="hidden" value="C" name="paramType'+counter+'"> '+
                                            '  <div id="client-frm" class="form-control" '+
                                            ' select2-url="'+requestContextPath+'/protected/reports/clients" '+
                                            ' </div> '
                                    }
                                    else if(data.lovName ==='CR'){
                                        markup+= '<input type="hidden"id="cur-id" name="paramValue'+counter+'"> '+
                                            '  <div id="curr-frm" class="form-control" '+
                                            ' select2-url="'+requestContextPath+'/protected/uw/policies/uwcurrencies" '+
                                            ' </div> '
                                    }
                                    else if(data.lovName ==='U'){
                                        markup+= '<input type="hidden" id="userCod" name="paramValue'+counter+'"> '+
                                            '  <div id="report-user" class="form-control" '+
                                            ' select2-url="'+requestContextPath+'/protected/reports/users" '+
                                            ' </div> '
                                    }
                                    else if(data.lovName ==='P'){
                                        markup+='<input type="hidden" id="policy-code" name="paramValue'+counter+'"> '+
                                            '  <div id="report-policies" class="form-control" '+
                                            ' select2-url="'+requestContextPath+'/protected/reports/policies" '+
                                            ' </div> '
                                    }
                                    else if(data.lovName ==='R'){
                                        markup+='<input type="hidden" id="remit-code" name="paramValue'+counter+'"> '+
                                            '  <div id="remit-def" class="form-control" '+
                                            ' select2-url="'+requestContextPath+'/protected/reports/remmittances" '+
                                            ' </div> '
                                    }
                                    else if(data.lovName ==='PR'){
                                        markup+='<input type="hidden" id="product-code" name="paramValue'+counter+'"> '+
                                            '  <div id="prd-code" class="form-control" '+
                                            ' select2-url="'+requestContextPath+'/protected/setups/binders/selproducts" '+
                                            ' </div> '
                                    }
                                    else if(data.lovName ==='B'){
                                        markup+='<input type="hidden" id="brn-id" name="paramValue'+counter+'"> '+
                                            '  <div id="brn-frm" class="form-control" '+
                                            ' select2-url="'+requestContextPath+'/protected/uw/policies/uwbranches" '+
                                            ' </div> '
                                    }
                                    else if(data.lovName ==='BI'){
                                        markup+='<input type="hidden" id="risk-binder-code" name="paramValue'+counter+'"> '+
                                            '  <div id="binder-frm" class="form-control" '+
                                            ' select2-url="'+requestContextPath+'/protected/reports/uwBinders" '+
                                            ' </div> '
                                    }
                                    else if(data.lovName ==='CT'){
                                        markup+='<input type="hidden" id="cert-type-pk" name="paramValue'+counter+'"> '+
                                            '  <div id="cert-type" class="form-control" '+
                                            ' select2-url="'+requestContextPath+'/protected/certs/selCertTypes" '+
                                            ' </div> '
                                    }
                                    else if(data.lovName ==='SA'){
                                        console.log("anything hapo ndani...")
                                        markup+='<input type="hidden" id="sub-agent-id" name="paramValue'+counter+'"> '+
                                            '  <div id="sub-agent-frm" class="form-control" '+
                                            ' select2-url="'+requestContextPath+'/protected/uw/policies/inhouseagents" '+
                                            ' </div> '
                                    }


                                }


                          markup+= ' </div></div>';

                        $(".data-inf").append(markup);
                        counter +=1;
                });
                $(".data-inf").append('<div><input type="hidden" value="'+ template+'" name="reportCode"></div> ');
                $(".datepicker-input").each(function() {
                    $(this).datetimepicker({
                        format: 'DD/MM/YYYY'
                    });

                });

                createAccountsForSel();
                populateClientLov();
                createUserLov();
                createProductForSel();
                populateBinderLov();
                createCertTypeLov();
                populateUserBranches();
                createReportPolicies();
                populateCurrencyLov();
                createRemmitanceForSel();
                populateSubAgentsLov();


            },
            error: function(jqXHR, textStatus, errorThrown) {


            }
        });
        $('#printReportModal').modal({
            backdrop: 'static',
            keyboard: true
        })
    }

    return {
        callReportModal:callReportModal
    }

})();