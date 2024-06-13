/**
 * Created by peter on 3/12/2017.
 */
var UTILITIES = UTILITIES || {};
$(function() {


    $(document).ready(function () {

        $(".datepicker-input").each(function() {
            $(this).datetimepicker({
                format: 'DD/MM/YYYY'
            });

        });
        $('#saveQuoteTax').on('click',function () {
            saveTaxEdits();
            $("#editTaxModal").modal('hide');

            $('#editTaxModal').on('hidden.bs.modal', function () {
                $("#tax-quot-id").val('');
                $("#tax-id").val('');
            })
        })
        $('#overrid-prem').number( true, 2 );

        populateClientLov();
        $("#pol-bin-type").val("M");
        $("#pol-bin-type").prop("disabled",true);
        $("#comm-rate").prop("readonly",false);
        populateBinderLov();
        $("#sms-to").on('change', function(){
            $.ajax({
                type: 'GET',
                url:  'getReceiverSmsNumber',
                dataType: 'json',
                data: {"receiver": $(this).val()},
                async: true,
                success: function(result) {
                    $("#sms-send-to").val(result);
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
        });

        $("#email-to").on('change', function(){
            $.ajax({
                type: 'GET',
                url:  'getReceiverEmail',
                dataType: 'json',
                data: {"receiver": $(this).val()},
                async: true,
                success: function(result) {
                    $("#email-send-to").val(result);
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
            if($(this).val()==="IN"){
                $("#email-cc").attr("readonly",true);
                $("#email-send-to").attr("readonly",false);
                $.ajax({
                    type: 'GET',
                    url:  'getInhouseEmail',
                    dataType: 'json',
                    async: true,
                    success: function(result) {
                        $("#email-cc").val(result);
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
                $("#email-cc").attr("readonly",false);
                $("#email-send-to").attr("readonly",true);
            }
        });
        $("#section_form_tbl").on('click','.hyperlink-btn',function() {
            var mandatory = $(this).closest('tr').find(".mandatory").val();
            if(mandatory && mandatory==="Y"){
                bootbox.alert("Cannot delete a Mandatory Premium Item");
                return;
            }
            $(this).closest('tr').remove();
        });
        PROSPECTS_UTILITIES.addProspects("Q");
        PROSPECTS_UTILITIES.populateProspectLov();
        populateCurrencyLov();
        populatePaymentModes();
        populateUserBranches();
        populateQuotDetails();
        getNewClausesModal();
        createQuoteClauses();
        populateInsuredLov();
        getPolicyWet();
        changePolicyWetDt();
        createNewQuote();
        getQuotProducts();
        saveRiskSections();
        getNewPremItemsModal();
        newRisk();
        addQuoteProd();
        changeClientType();
        populateClientTypeLov();
        populateClientTypeLov2();
        populateBranchLov1();
        populateCountryLov();
        populateTitlesLov();
        createSectorSelect();
        populateSourceGroupLov();
        saveClientDetails();
        getNewTaxesModal();
        defaultCountry();
        getProductRisks(-2000);
        getRiskSections(-2000);
        createPolicyTaxes(-2000);
        UTILITIES.createAssignee();
        UTILITIES.emailReports();
       //UTILITIES.smsClient();

    });
});



function defaultCountry(){
    $.ajax( {
        url: SERVLET_CONTEXT+'/protected/clients/setups/getDefaultCountry',
        type: 'GET',
        processData: false,
        contentType: false,
        success: function (s ) {
            if(s){
                populateSmsPrefix(s.couCode);
            }
        },
        error: function(xhr, error){

        }
    });

}


function populateSourcesLov(srcGroupId){


    if($("#source-frm").filter("div").html() != undefined)
    {
        Select2Builder.initAjaxSelect2({
            containerId : "source-frm",
            sort : 'desc',
            change: function(e, a, v){
                $("#source-id").val(e.added.srcId);
            },
            formatResult : function(a)
            {
                return a.desc;
            },
            formatSelection : function(a)
            {
                return a.desc;
            },
            initSelection: function (element, callback) {
                var code  = $('#source-id').val();
                var name = $("#source-name").val();
                var data = {desc:name,srcId:code};
                callback(data);
            },
            id: "srcId",
            width:"250px",
            params: {srcGroupId: srcGroupId},
            placeholder:"Select Source"

        });
    }


}


function populateSourceGroupLov(){
    if($("#sourcegroup-frm").filter("div").html() != undefined)
    {
        Select2Builder.initAjaxSelect2({
            containerId : "sourcegroup-frm",
            sort : 'desc',
            change: function(e, a, v){
                $("#sourcegroup-id").val(e.added.srcGroupId);
                populateSourcesLov(e.added.srcGroupId);

            },
            formatResult : function(a)
            {
                return a.desc;
            },
            formatSelection : function(a)
            {
                return a.desc;
            },
            initSelection: function (element, callback) {
                var code  = $('#sourcegroup-id').val();
                var name = $("#sourcegroup-name").val();
                var data = {desc:name,srcGroupId:code};
                callback(data);
                populateSourcesLov($('#sourcegroup-id').val());
            },
            id: "srcGroupId",
            width:"250px",
            placeholder:"Select Source Group"

        });

    }

}



function createSectorSelect(){
    if($("#sect-def").filter("div").html() != undefined)
    {
        Select2Builder.initAjaxSelect2({
            containerId : "sect-def",
            sort : 'name',
            change: function(e, a, v){
                $("#sect-id").val(e.added.code);
                populateOccupations(e.added.code);
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
            id: "code",
            placeholder:"Select Sector",
        });
    }
}

function populateOccupations(sectCode){
    if($("#occ-def").filter("div").html() != undefined)
    {
        Select2Builder.initAjaxSelect2({
            containerId : "occ-def",
            sort : 'name',
            change: function(e, a, v){
                $("#occ-id").val(e.added.code);
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
            id: "code",
            placeholder:"Select Occupation",
            params: {sectCode: sectCode}

        });
    }
}

function populateBranchLov1(){
    if($("#ten-branch").filter("div").html() != undefined)
    {
        Select2Builder.initAjaxSelect2({
            containerId : "ten-branch",
            sort : 'obName',
            change: function(e, a, v){
                $("#obId").val(e.added.obId);
            },
            formatResult : function(a)
            {
                return a.obName
            },
            formatSelection : function(a)
            {
                return a.obName
            },
            initSelection: function (element, callback) {
            },
            id: "obId",
            placeholder:"Select Branch",

        });
    }
}


function populateClientTypeLov2(){
    if($("#clnt-client-type-1").filter("div").html() != undefined)
    {
        Select2Builder.initAjaxSelect2({
            containerId : "clnt-client-type-1",
            sort : 'clientType',
            change: function(e, a, v){
                $("#clnt-type-id-1").val(e.added.typeId);
                $("#clnt-type-name-1").val(e.added.typeDesc);
            },
            formatResult : function(a)
            {
                return  a.typeDesc
            },
            formatSelection : function(a)
            {
                return  a.typeDesc
            },
            initSelection: function (element, callback) {
            },
            id: "typeId",
            placeholder:"Select Client Type",

        });
    }
    if($("#clnt-client-type").filter("div").html() != undefined)
    {
        Select2Builder.initAjaxSelect2({
            containerId : "clnt-client-type",
            sort : 'clientType',
            change: function(e, a, v){
                $("#clnt-type-id").val(e.added.typeId);
                $("#clnt-type-name").val(e.added.typeDesc);
            },
            formatResult : function(a)
            {
                return  a.typeDesc
            },
            formatSelection : function(a)
            {
                return  a.typeDesc
            },
            initSelection: function (element, callback) {
            },
            id: "typeId",
            placeholder:"Select Client Type",

        });
    }
}

function populateTitlesLov(){
    if($("#clnt-title").filter("div").html() != undefined)
    {
        Select2Builder.initAjaxSelect2({
            containerId : "clnt-title",
            sort : 'titleName',
            change: function(e, a, v){
                $("#clnt-title-id").val(e.added.titleId);
                $("#clnt-title-name").val(e.added.titleName);
            },
            formatResult : function(a)
            {
                return a.titleName
            },
            formatSelection : function(a)
            {
                return a.titleName
            },
            initSelection: function (element, callback) {
            },
            id: "titleId",
            placeholder:"Select Title",

        });
    }
}


function populateSmsPrefix(couCode){
    if($("#sms-pref").filter("div").html() != undefined)
    {
        Select2Builder.initAjaxSelect2({
            containerId : "sms-pref",
            sort : 'prefixName',
            change: function(e, a, v){
                console.log(e.added.prefixId);
                $("#pref-sms-id").val(e.added.prefixId);
            },
            formatResult : function(a)
            {
                return a.prefixName
            },
            formatSelection : function(a)
            {
                return a.prefixName
            },
            initSelection: function (element, callback) {
            },
            id: "prefixId",
            placeholder:"Prefix",
            params: {couCode: couCode}

        });
    }

    if($("#phone-pref").filter("div").html() != undefined)
    {
        Select2Builder.initAjaxSelect2({
            containerId : "phone-pref",
            sort : 'prefixName',
            change: function(e, a, v){
                console.log(e.added.prefixId);
                $("#pref-phone-id").val(e.added.prefixId);
            },
            formatResult : function(a)
            {
                return a.prefixName
            },
            formatSelection : function(a)
            {
                return a.prefixName
            },
            initSelection: function (element, callback) {
            },
            id: "prefixId",
            placeholder:"Prefix",
            params: {couCode: couCode}

        });
    }


    if($("#prs-sms-pref").filter("div").html() != undefined)
    {
        Select2Builder.initAjaxSelect2({
            containerId : "prs-sms-pref",
            sort : 'prefixName',
            change: function(e, a, v){
                console.log(e.added.prefixId);
                $("#prs-pref-sms-id").val(e.added.prefixId);
            },
            formatResult : function(a)
            {
                return a.prefixName
            },
            formatSelection : function(a)
            {
                return a.prefixName
            },
            initSelection: function (element, callback) {
            },
            id: "prefixId",
            placeholder:"Prefix",
            params: {couCode: couCode}

        });
    }
}

function populateCountryLov(){
    if($("#clnt-country").filter("div").html() != undefined)
    {
        Select2Builder.initAjaxSelect2({
            containerId : "clnt-country",
            sort : 'couName',
            change: function(e, a, v){
                $("#cou-id").val(e.added.couCode);
                $("#cou-prefix").val(e.added.prefix);
                populateSmsPrefix(e.added.couCode);
            },
            formatResult : function(a)
            {
                return a.couName
            },
            formatSelection : function(a)
            {
                return a.couName
            },
            initSelection: function (element, callback) {
            },
            id: "couCode",
            placeholder:"Select Country",

        });
    }
}


function populateClientTypeLov(){
    if($("#clnt-client-type").filter("div").html() != undefined)
    {
        Select2Builder.initAjaxSelect2({
            containerId : "clnt-client-type",
            sort : 'clientType',
            change: function(e, a, v){
                $("#clnt-type-id").val(e.added.typeId);
                $("#clnt-type-name").val(e.added.typeDesc);
            },
            formatResult : function(a)
            {
                return  a.typeDesc
            },
            formatSelection : function(a)
            {
                return  a.typeDesc
            },
            initSelection: function (element, callback) {
                var code = $("#clnt-type-id").val();
                var name = $("#clnt-type-name").val();
                var data = {typeDesc:name,typeId:code};
                callback(data);
            },
            id: "typeId",
            placeholder:"Select Client Type",

        });
    }
}

function  changeClientType(){
    $("#clnt-type").on('change', function(){
        $("#client-id").val("");
        $("#client-f-name").val("");
        $("#client-other-name").val("");
        populateClientLov();

        if(this.value==='' || this.value==='C'){
            $(".quot-client").text("Client *");
            $("#client-div").show();
            $("#insured-clnt-div").show();
            $("#prs-div").hide();
            $("#insured-prs-div").hide();
            $("#client-id").val("");
            $("#insured-name").val("");
            $("#insured-code").val("");
            $("#insured-other-name").val("");
            $("#insured-type").val("");
            $("#btn-add-client").show();
            $("#btn-add-prs").hide();
            $("#btn-add-client").val("New");
            populateInsuredLov();
        }
        else if(this.value==='P'){
            $(".quot-client").text("Prospect *");
            $("#client-div").hide();
            $("#insured-clnt-div").hide();
            $("#insured-prs-div").show();
            $("#prs-div").show();
            $('#insured-frm').select2('val', null);
            $("#client-id").val("");
            $("#insured-name").val("");
            $("#insured-code").val("");
            $("#insured-type").val("");
            $("#insured-other-name").val("");
            $("#btn-add-prs").show();
            $("#btn-add-prs").val("New");
            $("#btn-add-client").hide();
            PROSPECTS_UTILITIES.populateProspectLov();
        }
    });


    $("#insured-type").on('change', function(){
        console.log(this.value);
        if(this.value==='' || this.value==='C'){
            $("#insured-clnt-div").show();
            $("#insured-prs-div").hide();
            $("#insured-name").val("");
            $("#insured-code").val("");
            $("#insured-other-name").val("");
            $('#insured-frm').select2('val', null);
            populateInsuredLov();
        }
        else if(this.value==='P'){
            $("#insured-clnt-div").hide();
            $("#insured-prs-div").show();
            $("#insured-name").val("");
            $("#insured-code").val("");
            $("#insured-other-name").val("");
            $('#insured-prs-frm').select2('val', null);
            PROSPECTS_UTILITIES.populateProspectInsured();
        }
    });
}


function changePolicyWetDt(){

    $('#cover-to-date').on('dp.change', function (ev) {
        var curDate = ev.date;
        var dt = moment(curDate).format('DD/MM/YYYY');
        $("#risk-wet-date").val(dt);
        $("#product-wet-date").val(dt);
    });

    $('#risk-cover-from').on('dp.change', function (ev) {
        var curDate = ev.date;
        var dt = moment(curDate);
        var polwef = $("#from-date").val();
        var wet = $("#wet-date").val();

    });

    $('#risk-cover-to').on('dp.change', function (ev) {
        var curDate = ev.date;
        var dt = moment(curDate).format('DD/MM/YYYY');;
        var polwef = $("#from-date").val();
        var polwet = $("#wet-date").val();

    });


}


function createNewQuote(){
    $("#btn-add-quote").click(function(){
        if(typeof quotId!== 'undefined'){
            if(quotId!==-2000){
                updateQuote();
            }
            else{
                createQuote();
            }
        }
        else{
            createQuote();
        }
        $("#uw_tabs_title").show();
        $("#uw_tabs").show();
    });

    $("#btn-save-risk").click(function(){
        if($("#risk-code-pk").val() != ''){
            updateRisk();
        }
        else
            createRisk();
    });

    $("#btn-save-quot-prod").click(function(){
        if($("#quot-prod-pk").val() != ''){
            updateQuoteProduct();
        } else
            createQuoteProduct();
    });


    $("#btn-make-ready-policy").click(function(){
        makeReady();
    });

    $("#btn-undo-make-ready-policy").click(function(){
        undoMakeReady();
    });



    $("#btn-auth-quote").click(function(){
        authQuote();
    });

    $("#btn-confirm-quote").click(function(){
        confirmQuote();
    });

    $("#btn-cancel-quote").click(function(){
        $('#closeModal').modal({
            backdrop: 'static',
            keyboard: true
        })

    });

    $("#closeQuote").click(function(){
        cancelQuote();
    });


}

function undoMakeReady(){
    var stack_bottomleft = {"dir1": "up", "dir2": "left", "firstpos1": 25, "firstpos2": 25};
    $('#myPleaseWait').modal({
        backdrop: 'static',
        keyboard: true
    });
    $.ajax({
        type: 'GET',
        url:  'undoMakeReadyQuote',
        dataType: 'json',
        async: true,
        success: function(result) {
            $('#myPleaseWait').modal('hide');
            new PNotify({
                title: 'Success',
                text: 'Make Ready Process Successfully',
                type: 'success',
                styling: 'bootstrap3',
                addclass: 'stack-bottomright',
                stack: stack_bottomleft
            });
            populateQuotDetails();
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





function saveClientDetails(){
    $('#saveClientBtn').click(function(){
        var $classForm = $('#tenant-form');
        var validator = $classForm.validate();
        if (!$classForm.valid()) {
            return;
        }
        var $btn = $(this).button('Saving');
        var data = {};
        $classForm.serializeArray().map(function(x){data[x.name] = x.value;});
        var url = "createClient";
        var request = $.post(url, data );
        request.success(function(result){
            $("#client-id").val(result.tenId);
            $("#client-f-name").val(result.fname);
            $("#client-other-name").val(result.otherNames);
            populateClientLov();
            $("#insured-name").val(result.fname);
            $("#insured-code").val(result.tenId);
            $("#insured-other-name").val(result.otherNames);
            populateInsuredLov();
            new PNotify({
                title: 'Success',
                text: 'Record created/updated Successfully',
                type: 'success',
                styling: 'bootstrap3'
            });
            validator.resetForm();
            $('#tenant-form').find("input[type=text],input[type=number],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden], textarea,select").val("");
            $('#createClientModal').modal('hide');
        });

        request.error(function(jqXHR, textStatus, errorThrown){
            new PNotify({
                title: 'Error',
                text: jqXHR.responseText,
                type: 'error',
                styling: 'bootstrap3'
            });
        });
        request.always(function(){
            $btn.button('reset');
        });
    });
}



var populateUserBranches2 = function(){
    if($("#branch-frm").filter("div").html() != undefined)
    {
        Select2Builder.initAjaxSelect2({
            containerId : "branch-frm",
            sort : 'obName',
            change: function(e, a, v){
                $("#prsbrn-id").val(e.added.obId);
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
                var code  = $('#prsbrn-id').val();
                var name = $("#prsbrn-name").val();
                var data = {obName:name,obId:code};
                callback(data);
            },
            id: "obId",
            width:"250px",
            placeholder:"Select Branch"

        });
    }
};
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
                var code  = $('#sub-agent-id').val();
                var name = $("#sub-agent-name").val();
                var data = {name:name,acctId:code};
                callback(data);
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




function cancelQuote(){
    var stack_bottomleft = {"dir1": "up", "dir2": "left", "firstpos1": 25, "firstpos2": 25};
    $.ajax({
        type: 'GET',
        url:  'cancelQuote',
        dataType: 'json',
        data: {"reason": $("#quot-reasons").val()},
        async: true,
        success: function(result) {
            $('#closeModal').modal('hide');
            new PNotify({
                title: 'Success',
                text: 'Quote Closed Successfully',
                type: 'success',
                styling: 'bootstrap3',
                addclass: 'stack-bottomright',
                stack: stack_bottomleft
            });
            populateQuotDetails();
        },
        error: function(jqXHR, textStatus, errorThrown) {
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



function confirmQuote(){
    var stack_bottomleft = {"dir1": "up", "dir2": "left", "firstpos1": 25, "firstpos2": 25};
    $('#myPleaseWait').modal({
        backdrop: 'static',
        keyboard: true
    });
    $.ajax({
        type: 'GET',
        url:  'confirmQuote',
        dataType: 'json',
        async: true,
        success: function(result) {
            $('#myPleaseWait').modal('hide');
            new PNotify({
                title: 'Success',
                text: 'Quote Confirmation Process Successfully',
                type: 'success',
                styling: 'bootstrap3',
                addclass: 'stack-bottomright',
                stack: stack_bottomleft
            });
            populateQuotDetails();
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

function makeReady(){
    var stack_bottomleft = {"dir1": "up", "dir2": "left", "firstpos1": 25, "firstpos2": 25};
    $('#myPleaseWait').modal({
        backdrop: 'static',
        keyboard: true
    });
    $.ajax({
        type: 'GET',
        url:  'makeReadyQuote',
        dataType: 'json',
        async: true,
        success: function(result) {
            $('#myPleaseWait').modal('hide');
            new PNotify({
                title: 'Success',
                text: 'Make Ready Process Successfully',
                type: 'success',
                styling: 'bootstrap3',
                addclass: 'stack-bottomright',
                stack: stack_bottomleft
            });
            populateQuotDetails();
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


function authQuote(){
    var stack_bottomleft = {"dir1": "up", "dir2": "left", "firstpos1": 25, "firstpos2": 25};
    $('#myPleaseWait').modal({
        backdrop: 'static',
        keyboard: true
    });
    $.ajax({
        type: 'GET',
        url:  'authorizeQuote',
        dataType: 'json',
        async: true,
        success: function(result) {
            $('#myPleaseWait').modal('hide');
            new PNotify({
                title: 'Success',
                text: 'Authorization Process Successfully',
                type: 'success',
                styling: 'bootstrap3',
                addclass: 'stack-bottomright',
                stack: stack_bottomleft
            });
            populateQuotDetails();
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

function getRskSections(){
    var arr = [];
    $("#section_form_tbl tr").each(function(row,tr){
        var section   = $(this).find('.section').eq(0).val();
        var premId   = $(this).find('.premId').eq(0).val();
        var rate   = $(this).find('.rate').eq(0).val();
        var divFactor   = $(this).find('.divFactor').eq(0).val();
        var freeLimit   = $(this).find('.freeLimit').eq(0).val();
        var amount = $(this).find('.amount').eq(0).val();
        var multiplierRate= $(this).find('.multiplierRate').eq(0).val();
        var ratesApplicable   = $(this).find('.ratesApplicable').eq(0).val();
        arr.push({
            section: section,
            premId: premId,
            rate:rate,
            divFactor:divFactor,
            ratesApplicable: ratesApplicable,
            freeLimit:freeLimit,
            amount: amount,
            multiplierRate:multiplierRate
        });
        console.log( arr)
    });

    return arr;
}

function getQuotProductDetails(){
    var $currForm = $('#quot-pro-form');
    var currValidator = $currForm.validate();
    if (!$currForm.valid()) {
        return;
    }
    $('#quot-pro-form input[type=checkbox]').each(function(e){
        $(this).val($(this).is(':checked'));
    });
    var data = {};
    $currForm.serializeArray().map(function(x){data[x.name] = x.value;});
    return data;
}

function getRiskDetails(){
    var $currForm = $('#risk-form');
    var currValidator = $currForm.validate();
    if (!$currForm.valid()) {
        return;
    }
    $('#risk-form input[type=checkbox]').each(function(e){
        $(this).val($(this).is(':checked'));
    });
    var data = {};
    $currForm.serializeArray().map(function(x){data[x.name] = x.value;});
    return data;
}


function createQuote(){

    var stack_bottomleft = {"dir1": "up", "dir2": "left", "firstpos1": 25, "firstpos2": 25};
    var arr = getRskSections();
    if(arr.length==0){
        bootbox.alert("Cannot create Quote without sections")
        return false;
    }
    arr.shift();
    var $currForm = $('#quot-form');
    var currValidator = $currForm.validate();
    if (!$currForm.valid()) {
        return;
    }

    $('#quot-form input[type=checkbox]').each(function(e){
        $(this).val($(this).is(':checked'));
    });

    var data = {};
    $currForm.serializeArray().map(function(x){data[x.name] = x.value;});
    var url = "createQuotation";

    data.sections = arr;
    data.riskBean = getRiskDetails();
    data.quoteProductBean = getQuotProductDetails();
    $('#myPleaseWait').modal({
        backdrop: 'static',
        keyboard: true
    });
    $.ajax(
        {
            url:url,
            type: "POST",
            data: JSON.stringify(data),
            success: function(s){
                $('#myPleaseWait').modal('hide');
                new PNotify({
                    title: 'Success',
                    text: 'Quote Transaction created Successfully',
                    type: 'success',
                    styling: 'bootstrap3',
                    addclass: 'stack-bottomright',
                    stack: stack_bottomleft
                });
                $('#insured-frm').select2('val', null);
                populateInsuredLov();
                $('#subclass-frm').select2('val', null);
                populateSubclassLov();
                $('#covertypes-frm').select2('val', null);
                populateCoverTypesLov();
                quotId = s.quoteId;
                console.log(quotId);
                populateQuotDetails();
            },
            error: function(jqXHR, textStatus, errorThrown){
                $('#myPleaseWait').modal('hide');
                new PNotify({
                    title: 'Error',
                    text: jqXHR.responseText,
                    type: 'error',
                    styling: 'bootstrap3',
                    addclass: 'stack-bottomright',
                    stack: stack_bottomleft
                });
            },
            dataType: "json",
            contentType: "application/json"
        } );
}




function updateQuote(){

    var stack_bottomleft = {"dir1": "up", "dir2": "left", "firstpos1": 25, "firstpos2": 25};
    var $currForm = $('#quot-form');
    var currValidator = $currForm.validate();
    if (!$currForm.valid()) {
        return;
    }
    $('#quot-form input[type=checkbox]').each(function(e){
        $(this).val($(this).is(':checked'));
    });
    var data = {};
    $currForm.serializeArray().map(function(x){data[x.name] = x.value;});
    var url = "createQuotation";
    $('#myPleaseWait').modal({
        backdrop: 'static',
        keyboard: true
    });
    $.ajax(
        {
            url:url,
            type: "POST",
            data: JSON.stringify(data),
            success: function(s){
                $('#myPleaseWait').modal('hide');
                new PNotify({
                    title: 'Success',
                    text: 'Quote Transaction created Successfully',
                    type: 'success',
                    styling: 'bootstrap3',
                    addclass: 'stack-bottomright',
                    stack: stack_bottomleft
                });
                $('#insured-frm').select2('val', null);
                populateInsuredLov();
                $('#subclass-frm').select2('val', null);
                populateSubclassLov();
                $('#covertypes-frm').select2('val', null);
                populateCoverTypesLov();
                quotId = s.quoteId;
                console.log(quotId);
                populateQuotDetails();
            },
            error: function(jqXHR, textStatus, errorThrown){
                $('#myPleaseWait').modal('hide');
                new PNotify({
                    title: 'Error',
                    text: jqXHR.responseText,
                    type: 'error',
                    styling: 'bootstrap3',
                    addclass: 'stack-bottomright',
                    stack: stack_bottomleft
                });
            },
            dataType: "json",
            contentType: "application/json"
        } );
}

function getPolicyWet(){
    $('#wef-date').on('dp.change', function (ev) {
        var curDate = ev.date;
        var dt = moment(curDate).format('DD/MM/YYYY');
        $("#risk-wef-date").val(dt);
        $("#product-wef-date").val(dt);
        $.ajax({
            type: 'GET',
            url:  'getWetDate',
            dataType: 'json',
            data: {"wefDate":dt},
            async: true,
            success: function(result) {
                $("#wet-date").val(moment(result).format('DD/MM/YYYY'));
                $("#risk-wet-date").val(moment(result).format('DD/MM/YYYY'));
                $("#product-wet-date").val(moment(result).format('DD/MM/YYYY'));

            },
            error: function(jqXHR, textStatus, errorThrown) {

            }
        });
    });

    $('#product-cover-from').on('dp.change', function (ev) {
        var curDate = ev.date;
        var dt = moment(curDate).format('DD/MM/YYYY');
        $("#risk-wef-date").val(dt);
        $.ajax({
            type: 'GET',
            url:  'getWetDate',
            dataType: 'json',
            data: {"wefDate":dt},
            async: true,
            success: function(result) {
                $("#risk-wet-date").val(moment(result).format('DD/MM/YYYY'));
                $("#product-wet-date").val(moment(result).format('DD/MM/YYYY'));

            },
            error: function(jqXHR, textStatus, errorThrown) {

            }
        });
    });

    $('#risk-cover-from').on('dp.change', function (ev) {
        var curDate = ev.date;
        var dt = moment(curDate).format('DD/MM/YYYY');
        $.ajax({
            type: 'GET',
            url:  'getWetDate',
            dataType: 'json',
            data: {"wefDate":dt},
            async: true,
            success: function(result) {
                $("#risk-wet-date").val(moment(result).format('DD/MM/YYYY'));

            },
            error: function(jqXHR, textStatus, errorThrown) {

            }
        });
    });
}

function populateInsuredLov(){
    if($("#insured-frm").filter("div").html() != undefined)
    {
        Select2Builder.initAjaxSelect2({
            containerId : "insured-frm",
            sort : 'fname',
            change: function(e, a, v){
                //$("#client-id").val(e.added.tenId);
                $("#insured-code").val(e.added.tenId);
                //$("#insured-id").val(e.added.tenId);
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
                var code = $("#insured-code").val();
                var name = $("#insured-name").val();
                var othernames = $("#insured-other-name").val();
                var data = {fname:name,otherNames:othernames,tenId:code};
                callback(data);
            },
            id: "tenId",
            width:"250px",
            placeholder:"Select Insured"

        });
    }
}

function populateQuotDetails(){
    if(typeof quotId!== 'undefined'){
        if(quotId!==-2000){
            $("#btn-quot-reports").show();
            $("#risk-form").hide();
            $("#btn-save-risk").hide();
            $("#btn-save-cancel").hide();

            $("#btn-add-risk").show();
            $("#quot-prod-tbl").show();
            $("#quot-prod-div").hide();
            $("#risk-div").show();
            $("#sect-div").show();
            $("#prem-rates-div").hide();
            $("#other-pol-details").show();
            $("#btn-add-new-section").show();
            $("#btn-add-quot-prod").show();
            $("#btn-save-quot-prod").show();
            $("#btn-cancel-quot-prod").show();
            $("#myTab #show-taxes,#show-clauses").show();
            getQuotationDetails();
        }
        else{
            $("#btn-auth-quote").css("display","none");
            $("#btn-quot-reports").hide();
            $("#quot-prod-tbl").hide();
            $("#quot-prod-div").show();
            $("#risk-form").show();
            $("#risk-div").hide();
            $("#other-pol-details").hide();
            $("#prem-rates-div").show();
            $("#btn-add-quote").show();
            $("#sect-div").hide();
            $("#btn-save-risk").hide();
            $("#btn-save-cancel").hide();
            $("#btn-add-risk").hide();
            $("#myTab #show-taxes,#show-clauses").hide();
            $("#btn-add-new-section").hide();
            $("#client-div").show();
            $("#prs-div").hide();
            $("#insured-clnt-div").show();
            $("#insured-prs-div").hide();
            $("#display-client").hide();
            $("#display-binder").hide();
            $("#display-currency").hide();
            $("#btn-add-quot-prod").hide();
            $("#btn-save-quot-prod").hide();
            $("#btn-cancel-quot-prod").hide();
            $("#display-source").hide();
            $("#display-sourcegroup").hide();
            $("#edit-sourcegroup").show();
            $("#edit-source").show();
            $("#btn-add-prs").show();
            $("#btn-add-prs").val("New");
        }
    }
    else{
        $("#other-pol-details").hide();
    }
}


function getQuotationDetails(){
    if(typeof quotId!== 'undefined'){
        if(quotId!==-2000){
            $.ajax( {
                url: 'getQuotationDetails/'+quotId,
                type: 'GET',
                processData: false,
                contentType: false,
                success: function (s ) {
                    console.log(s);
                    $("#quot-id").val(quotId);
                    console.log(s.quotStatus);
                    if(s.quotStatus){
                        if(s.quotStatus==="D"){
                            $("#btn-assign-trans").show();
                            $("#btn-add-quote").css("display","block");
                            $("#btn-auth-quote").css("display","none");
                            $("#btn-confirm-quote").css("display","none");
                            $("#btn-cancel-quote").css("display","none");
                            $("#btn-make-ready-policy").css("display","block");
                            $("#btn-undo-make-ready-policy").css("display","none");
                            $("#pol-status").text("Draft");
                            $("#edit-currency").show();
                            $("#display-currency").hide();
                            $("#edit-branch").show();
                            $("#display-branch").hide();
                            $("#edit-payment-mode").show();
                            $("#display-payment-mode").hide();
                            $("#edit-binder").show();
                            $("#display-binder").hide();
                            $("#edit-client").show();
                            $("#display-client").hide();;
                            $("#from-date").removeAttr('disabled');
                            $("#wet-date").removeAttr('disabled');
                            $('#clnt-type').removeAttr("disabled");
                            $("#btn-save-quot-prod").hide();
                            $("#btn-cancel-quot-prod").hide();
                            $("#btn-add-quot-prod").show();
                            $("#btn-add-risk").show();
                            $("#sel2").prop("disabled", false);
                            //$("#btn-add-new-clause").show();
                            //$("#btn-add-new-tax").show();
                            $("#btn-add-new-section").show();
                            //$("#btn-add-new-remark").show();
                            //$("#btn-save-remark").show();
                            $("#display-source").hide();
                            $("#display-sourcegroup").hide();
                            $("#edit-sourcegroup").show();
                            $("#edit-source").show();
                        }
                        else if(s.quotStatus==="R"){
                            $("#btn-assign-trans").hide();
                            $("#sel2").prop("disabled", true);
                            $("#btn-add-quote").css("display","none");
                            $("#btn-auth-quote").css("display","block");
                            $("#btn-confirm-quote").css("display","none");
                            $("#btn-cancel-quote").css("display","none");
                            $("#btn-make-ready-policy").css("display","none");
                            $("#btn-undo-make-ready-policy").css("display","block");
                            $("#pol-status").text("Ready");
                            $("#edit-currency").hide();
                            $("#display-currency").show();
                            $("#edit-branch").hide();
                            $("#display-branch").show();
                            $("#edit-payment-mode").hide();
                            $("#display-payment-mode").show();
                            $("#edit-binder").hide();
                            $("#display-binder").show();
                            $("#edit-client").hide();
                            $("#display-client").show();
                            $("#poli-remarks").attr("disabled", "disabled");
                            $("#pol-buss-type").prop("disabled", true);
                            $("#pol-bin-type").prop("disabled", true);
                            $("#from-date").prop("disabled", true);
                            $("#wet-date").prop("disabled", true);
                            $('#clnt-type').prop("disabled",true);
                            $("#btn-save-quot-prod").hide();
                            $("#btn-cancel-quot-prod").hide();
                            $("#btn-add-quot-prod").hide();
                            $("#btn-add-risk").hide();
                            //$("#btn-add-new-clause").show();
                            //$("#btn-add-new-tax").show();
                            $("#btn-add-new-clause").hide();
                            $("#btn-add-new-tax").hide();
                            $("#btn-add-new-section").hide();
                            $("#btn-add-new-remark").hide();
                            $("#btn-save-remark").hide();
                            $("#display-source").show();
                            $("#display-sourcegroup").show();
                            $("#edit-sourcegroup").hide();
                            $("#edit-source").hide();
                        }
                        else if(s.quotStatus==="A"){
                            $("#btn-assign-trans").hide();
                            $("#btn-add-quote").css("display","none");
                            $("#btn-auth-quote").css("display","none");
                            $("#btn-confirm-quote").css("display","block");
                            $("#btn-cancel-quote").css("display","block");
                            $("#btn-make-ready-policy").css("display","none");
                            $("#btn-undo-make-ready-policy").css("display","none");
                            $("#pol-status").text("Authorised");
                            $("#edit-currency").hide();
                            $("#display-currency").show();
                            $("#edit-branch").hide();
                            $("#display-branch").show();
                            $("#edit-payment-mode").hide();
                            $("#display-payment-mode").show();
                            $("#edit-binder").hide();
                            $("#display-binder").show();
                            $("#edit-client").hide();
                            $("#display-client").show();
                            $("#poli-remarks").prop("disabled", true);
                            $("#policy-remarks").prop("disabled", true);
                            $("#pol-buss-type").prop("disabled", true);
                            $("#pol-bin-type").prop("disabled", true);
                            $("#from-date").prop("disabled", true);
                            $("#wet-date").prop("disabled", true);
                            $("#sel2").prop("disabled", true);
                            $('#clnt-type').prop("disabled",true);
                            $("#btn-add-risk").hide();
                            $("#btn-add-new-clause").hide();
                            $("#btn-add-new-tax").hide();
                            $("#btn-add-new-section").hide();
                            $("#btn-add-new-remark").hide();
                            $("#btn-save-remark").hide();
                            $("#btn-save-quot-prod").hide();
                            $("#btn-cancel-quot-prod").hide();
                            $("#btn-add-quot-prod").hide();
                            $("#display-source").show();
                            $("#display-sourcegroup").show();
                            $("#edit-sourcegroup").hide();
                            $("#edit-source").hide();
                        }
                        else if(s.quotStatus==="C"){
                            $("#btn-assign-trans").hide();
                            $("#btn-add-quote").css("display","none");
                            $("#btn-auth-quote").css("display","none");
                            $("#btn-confirm-quote").css("display","none");
                            $("#btn-cancel-quote").css("display","block");
                            $("#btn-make-ready-policy").css("display","none");
                            $("#btn-undo-make-ready-policy").css("display","none");
                            $("#pol-status").text("Confirmed");
                            $("#sel2").prop("disabled", true);
                            $("#edit-currency").hide();
                            $("#display-currency").show();
                            $("#edit-branch").hide();
                            $("#display-branch").show();
                            $("#edit-payment-mode").hide();
                            $("#display-payment-mode").show();
                            $("#edit-binder").hide();
                            $("#display-binder").show();
                            $("#edit-client").hide();
                            $("#display-client").show();
                            $("#poli-remarks").prop("disabled", true);
                            $("#policy-remarks").prop("disabled", true);
                            $("#pol-buss-type").prop("disabled", true);
                            $("#pol-bin-type").prop("disabled", true);
                            $("#from-date").prop("disabled", true);
                            $("#wet-date").prop("disabled", true);
                            $('#clnt-type').prop("disabled",true);
                            $("#btn-add-risk").hide();
                            $("#btn-add-new-clause").hide();
                            $("#btn-add-new-tax").hide();
                            $("#btn-add-new-section").hide();
                            $("#btn-add-new-remark").hide();
                            $("#btn-save-remark").hide();
                            $("#btn-save-quot-prod").hide();
                            $("#btn-cancel-quot-prod").hide();
                            $("#btn-add-quot-prod").hide();
                            $("#display-source").show();
                            $("#display-sourcegroup").show();
                            $("#edit-sourcegroup").hide();
                            $("#edit-source").hide();
                        }
                        else if(s.quotStatus==="CL"){
                            $("#btn-assign-trans").hide();
                            $("#sel2").prop("disabled", true);
                            $("#btn-add-quote").css("display","none");
                            $("#btn-auth-quote").css("display","none");
                            $("#btn-confirm-quote").css("display","none");
                            $("#btn-cancel-quote").css("display","none");
                            $("#btn-make-ready-policy").css("display","none");
                            $("#btn-undo-make-ready-policy").css("display","none");
                            $("#pol-status").text("Cancelled");
                            $("#edit-currency").hide();
                            $("#display-currency").show();
                            $("#edit-branch").hide();
                            $("#display-branch").show();
                            $("#edit-payment-mode").hide();
                            $("#display-payment-mode").show();
                            $("#edit-binder").hide();
                            $("#display-binder").show();
                            $("#edit-client").hide();
                            $("#display-client").show();
                            $("#poli-remarks").prop("disabled", true);
                            $("#policy-remarks").prop("disabled", true);
                            $("#pol-buss-type").prop("disabled", true);
                            $("#pol-bin-type").prop("disabled", true);
                            $("#from-date").prop("disabled", true);
                            $("#wet-date").prop("disabled", true);
                            $('#clnt-type').prop("disabled",true);
                            $("#btn-add-risk").hide();
                            $("#btn-add-new-clause").hide();
                            $("#btn-add-new-tax").hide();
                            $("#btn-add-new-section").hide();
                            $("#btn-add-new-remark").hide();
                            $("#btn-save-remark").hide();
                            $("#btn-save-quot-prod").hide();
                            $("#btn-cancel-quot-prod").hide();
                            $("#btn-add-quot-prod").hide();
                            $("#display-source").show();
                            $("#display-sourcegroup").show();
                            $("#edit-sourcegroup").hide();
                            $("#edit-source").hide();
                        }

                    }
                     $("#client-info").text(s.fname+" "+s.otherNames);
                    $("#pay-mode-info").text(s.paymentMode);
                    $("#source-info").text(s.sourceName);
                    $("#source-name").val(s.sourceName);
                    $("#source-id").val(s.sourceId);
                    $("#sourcegroup-id").val(s.sourceGroupId);
                    $("#sourcegroup-name").val(s.sourceGroupName);
                    $("#sourcegroup-info").text(s.sourceGroupName);
                    populateSourceGroupLov();

                    $("#branch-info").text(s.branch);
                    $("#currency-info").text(s.currency);
                    $("#pol-no").text(s.quotNo);
                    $("#h4pol").text("Quote No: "+s.quotNo);
                    $("#pol-rev-no").text(s.quotRevNo);
                    $("#pol-sum-insured").text(UTILITIES.currencyFormat(s.sumInsured));
                    $("#pol-premium").text(UTILITIES.currencyFormat(s.premium));
                    $("#pol-basic-prem").text(UTILITIES.currencyFormat(s.basicPrem));
                    $("#pol-net-prem").text(UTILITIES.currencyFormat(s.netPrem));
                    $("#from-date").val(moment(s.quoteWef).format('DD/MM/YYYY'));
                    $("#wet-date").val(moment(s.quoteWet).format('DD/MM/YYYY'));
                    $('#clnt-type').val((s.clientType)?s.clientType:"C");
                    if(!(s.clientType) || s.clientType==='' || s.clientType==='C'){
                        $(".quot-client").text("Client *");
                        $("#client-div").show();
                        $("#insured-clnt-div").show();
                        $("#prs-div").hide();
                        $("#insured-prs-div").hide();
                        $("#client-id").val("");
                        $("#insured-name").val("");
                        $("#insured-code").val("");
                        $("#insured-other-name").val("");
                        populateInsuredLov();
                    }
                    else if(s.clientType==='P'){
                        $(".quot-client").text("Prospect *");
                        $("#client-div").hide();
                        $("#insured-clnt-div").hide();
                        $("#insured-prs-div").show();
                        $("#prs-div").show();
                        $('#insured-frm').select2('val', null);
                        $("#client-id").val("");
                        $("#insured-name").val("");
                        $("#insured-code").val("");
                        $("#insured-other-name").val("");
                        $("#btn-add-prs").val("Edit");
                        $("#btn-add-prs").show();
                        populateInsuredLov();
                    }
                    $("#cur-id").val(s.curCode);
                    $("#cur-name").val(s.currency);
                    populateCurrencyLov();
                    $("#client-id").val(s.tenId);
                    $("#client-f-name").val(s.fname);
                    $("#client-other-name").val(s.otherNames);
                    populateClientLov();
                    $("#pm-id").val(s.pmId);
                    $("#pm-name").val(s.paymentMode);
                    populatePaymentModes();
                    $("#brn-id").val(s.obId);
                    $("#brn-name").val(s.branch);
                    $('#pol-comm-amt').text(UTILITIES.currencyFormat(s.commAmt));
                    $('#pol-tl').text(UTILITIES.currencyFormat(s.trainingLevy));
                    $('#pol-phcf').text(UTILITIES.currencyFormat(s.phcf));
                    $('#pol-sd').text(UTILITIES.currencyFormat(s.stampDuty));
                    $('#pol-whtx').text(UTILITIES.currencyFormat(s.whtx));
                    $('#pol-extras').text(UTILITIES.currencyFormat(s.extras));
                    if(s.expiryDate)
                        $("#pol-exp-date").text(moment(s.expiryDate).format('DD/MM/YYYY'));
                    UTILITIES.getProcessActiveDiagram("Q"+quotId);
                    UTILITIES.getTaskActive("Q"+quotId);
                    UTILITIES.getProcessHistory("Q"+quotId);
                    populateUserBranches();
                    $('#section_tbl').DataTable().ajax.url( "quotRiskLimits/"+-2000 ).load();
                    $('#risk_tbl').DataTable().ajax.url( "quotProductRisks/"+-2000 ).load();
                    $('#polTaxesList').DataTable().ajax.url( "quotProTaxes/"+-2000 ).load();
                    $('#prod_tbl').DataTable().ajax.reload();
                },
                error: function(xhr, error){
                    bootbox.alert(xhr.responseText);
                }
            });
        }
    }
}


function stringToDate(_date,_format,_delimiter)
{
    var formatLowerCase=_format.toLowerCase();
    var formatItems=formatLowerCase.split(_delimiter);
    var dateItems=_date.split(_delimiter);
    var monthIndex=formatItems.indexOf("mm");
    var dayIndex=formatItems.indexOf("dd");
    var yearIndex=formatItems.indexOf("yyyy");
    var month=parseInt(dateItems[monthIndex]);
    month-=1;
    var formatedDate = new Date(dateItems[yearIndex],month,dateItems[dayIndex]);
    return formatedDate;
}

function populateSourceGroupLov2(){
    if($("#sourcegroup-frm").filter("div").html() != undefined)
    {

        Select2Builder.initAjaxSelect2({
            containerId : "sourcegroup-frm",
            sort : 'desc',
            change: function(e, a, v){
                $("#sourcegroup-id").val(e.added.srcGroupId);
                $('#source-id').val("");
                $("#source-name").val("");
                populateSourcesLov(e.added.srcGroupId);


            },
            formatResult : function(a)
            {
                return a.desc;
            },
            formatSelection : function(a)
            {
                return a.desc;
            },
            initSelection: function (element, callback) {
                var code  = $('#sourcegroup-id').val();
                var name = $("#sourcegroup-name").val();
                var data = {desc:name,srcGroupId:code};
                callback(data);
                populateSourcesLov($('#sourcegroup-id').val());
            },
            id: "srcGroupId",
            width:"250px",
            placeholder:"Select Source Group"

        });

    }

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
                var code  = $('#brn-id').val();
                var name = $("#brn-name").val();
                var data = {obName:name,obId:code};
                callback(data);
            },
            id: "obId",
            width:"250px",
            placeholder:"Select Branch"

        });
    }
}


function populatePaymentModes(){
    if($("#pm-mode-frm").filter("div").html() != undefined)
    {
        Select2Builder.initAjaxSelect2({
            containerId : "pm-mode-frm",
            sort : 'pmDesc',
            change: function(e, a, v){
                $("#pm-id").val(e.added.pmId);
            },
            formatResult : function(a)
            {
                return a.pmDesc;
            },
            formatSelection : function(a)
            {
                return a.pmDesc;
            },
            initSelection: function (element, callback) {
                var code  = $('#pm-id').val();
                var name = $("#pm-name").val();
                var data = {pmDesc:name,pmId:code};
                callback(data);
            },
            id: "pmId",
            width:"250px",
            placeholder:"Select Payment Mode"

        });
    }
}

function populateBinderLov(){
    if($("#binder-frm").filter("div").html() != undefined)
    {
        Select2Builder.initAjaxSelect2({
            containerId : "binder-frm",
            sort : 'binName',
            change: function(e, a, v){
                $("#risk-binder-code").val(e.added.binId);
                $("#risk-bind-code").val(e.added.binId);
                $("#pol-ins-comp").text(e.added.name);
                $("#pol-prod-name").text(e.added.proDesc);
                $("#product-id").val(e.added.proCode);
                $("#pol-agent-id").val(e.added.acctId);
                $("#bind-name").val(e.added.binName);
                $("#binder-id").val(e.added.binId);
                $("#risk-binder").text(e.added.binName);
                $("#comm-rate").val();
                if(e.added.motorProduct){
                    $("#risk-label").text('Plate Number');
                    $("#risk-description").text('Model');
                }
                else{
                    $("#risk-label").text('Risk Identifier');
                    $("#risk-description").text('Risk Description');
                }

                populateSubclassLov(e.added.binId);
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
                var code  = $('#binder-id').val();
                var name = $("#bind-name").val();
                var data = {binName:name,binId:code};
                callback(data);
            },
            id: "binId",
            width:"250px",
            params: {bindType: $("#pol-bin-type").val()},
            placeholder:"Select Contract"

        });
    }
}


function populateSubclassLov(bindId){
    if($("#subclass-frm").filter("div").html() != undefined)
    {
        Select2Builder.initAjaxSelect2({
            containerId : "subclass-frm",
            sort : 'detId',
            change: function(e, a, v){
                $("#risk-sub-code").val(e.added.subId);
                populateCoverTypesLov();

            },
            formatResult : function(a)
            {
                return a.subDesc;
            },
            formatSelection : function(a)
            {
                return a.subDesc;
            },
            initSelection: function (element, callback) {
                var code  = $('#risk-sub-code').val();
                var name = $("#sub-name").val();
                var data = {subDesc:name,subId:code};
                callback(data);
            },
            id: "subDesc",
            width:"250px",
            params: {bindCode: bindId},
            placeholder:"Select Classification"

        });
    }
}


function populateCoverTypesLov(){
    if($("#covertypes-frm").filter("div").html() != undefined)
    {
        Select2Builder.initAjaxSelect2({
            containerId : "covertypes-frm",
            sort : 'detId',
            change: function(e, a, v){
                if(e.added.commRate){
                    $("#comm-rate").val(e.added.commRate).prop('readonly',true);
                }
                else{
                    $("#comm-rate").prop('readonly',false);
                }
                $("#risk-cov-code").val(e.added.covId);
                $("#binder-det-id").val(e.added.detId);
                getPremiumRates(e.added.detId);
            },
            formatResult : function(a)
            {
                return a.covName;
            },
            formatSelection : function(a)
            {
                return a.covName;
            },
            initSelection: function (element, callback) {
                var code  = $('#risk-cov-code').val();
                var name = $("#cover-name").val();
                var data = {covName:name,covId:code};
                callback(data);
            },
            id: "covName",
            width:"250px",
            params: {bindCode: $("#risk-bind-code").val(),subCode: $("#risk-sub-code").val()},
            placeholder:"Select Cover Type"

        });
    }
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
                var code  = $('#cur-id').val();
                var name = $("#cur-name").val();
                var data = {curName:name,curCode:code};
                callback(data);
            },
            id: "curCode",
            width:"250px",
            placeholder:"Select Currency"

        });
    }
}


function populateClientLov(){
    if($("#client-frm").filter("div").html() != undefined)
    {
        Select2Builder.initAjaxSelect2({
            containerId : "client-frm",
            sort : 'fname',
            change: function(e, a, v){
                $("#client-id").val(e.added.tenId);
                $("#insured-name").val(e.added.fname);
                $("#insured-code").val(e.added.tenId);
                $("#insured-other-name").val(e.added.otherNames);
                $("#insured-type").val("C");
                populateInsuredLov();

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
                var code = $("#client-id").val();
                var name = $("#client-f-name").val();
                var othernames = $("#client-other-name").val();
                var data = {fname:name,otherNames:othernames,tenId:code};
                callback(data);
            },
            id: "tenId",
            placeholder:"Select Client"

        });
    }
}



function getPremiumRates(detId){
    $.ajax({
        type: 'GET',
        url: 'getBinderPremRates',
        dataType: 'json',
        data: {"detId": detId},
        async: true,
        success: function (result) {
            $("#section_form_tbl tbody").each(function () {
                $(this).remove();
            });
            for (var res in result) {
                console.log(result[res]);
                var markup = "<tr><td><input type='hidden' class='mandatory form-control' value='" + result[res].mandatory + "'><input type='hidden' class='section form-control' value='" + result[res].premId +
                    "'><input type='hidden' class='ratesApplicable form-control' value='" + result[res].ratesApplicable + "'>" + result[res].sectionDesc + "</td><td>" + ' <input type="text" class="amount form-control" required>' +
                    "</td><td><input type='text' class='rate form-control' value='" + result[res].rate + "'></td><td><input type='text' class='divFactor form-control'  value='" + result[res].divFactor + "' readonly></td></td><td>" +
                    "<input type='text' class='freeLimit form-control'  value='" + result[res].freeLimit + "'></td>" +
                    "<td><button type='button' class='btn btn-danger btn btn-danger btn-xs hyperlink-btn'><i class='fa fa-trash-o'></button></td></tr>";
                if($("#pol-bin-type").val()==="B"){
                    if (result[res].ratesApplicable && result[res].ratesApplicable === "Y") {
                        markup = "<tr><td><input type='hidden' class='mandatory form-control' value='" + result[res].mandatory + "'><input type='hidden' class='section form-control' value='" + result[res].premId +
                            "'><input type='hidden' class='ratesApplicable form-control' value='" + result[res].ratesApplicable + "'>" + result[res].sectionDesc + "</td><td>" + ' <input type="text" class="amount form-control">' +
                            "</td><td><input type='text' class='rate form-control' readonly value='" + result[res].rate + "'></td><td><input type='text' class='divFactor form-control' readonly value='" + result[res].divFactor + "'></td></td><td>" +
                            "<input type='text' class='freeLimit form-control'  value='" + result[res].freeLimit + "' readonly></td>" +
                            "<td><button type='button' class='btn btn-danger btn btn-danger btn-xs hyperlink-btn'><i class='fa fa-trash-o'></button></td></tr>";
                    }
                    else if (result[res].rider && result[res].rider === "Y") {
                        markup = "<tr><td><input type='hidden' class='mandatory form-control' value='" + result[res].mandatory + "'><input type='hidden' class='section form-control' value='" + result[res].premId +
                            "'><input type='hidden' class='ratesApplicable form-control' value='" + result[res].ratesApplicable + "'>" + result[res].sectionDesc + "</td><td>" + ' <input type="text" class="amount form-control" readonly>' +
                            "</td><td><input type='text' class='rate form-control' value='" + result[res].rate + "' readonly></td><td><input type='text' class='divFactor form-control' value='" + result[res].divFactor + "' readonly></td></td><td>" +
                            "<input type='text' class='freeLimit form-control'  value='" + result[res].freeLimit + "' readonly></td>" +
                            "<td><button type='button' class='btn btn-danger btn btn-danger btn-xs hyperlink-btn'><i class='fa fa-trash-o'></button></td></tr>";
                    }
                    else{
                        markup = "<tr><td><input type='hidden' class='mandatory form-control' value='" + result[res].mandatory + "'><input type='hidden' class='section form-control' value='" + result[res].premId +
                            "'><input type='hidden' class='ratesApplicable form-control' value='" + result[res].ratesApplicable + "'>" + result[res].sectionDesc + "</td><td>" + ' <input type="text" class="amount form-control">' +
                            "</td><td><input type='text' class='rate form-control' readonly value='" + result[res].rate + "'></td><td><input type='text' class='divFactor form-control' readonly value='" + result[res].divFactor + "'></td></td><td>" +
                            "<input type='text' class='freeLimit form-control'  value='" + result[res].freeLimit + "' readonly></td>" +
                            "<td><button type='button' class='btn btn-danger btn btn-danger btn-xs hyperlink-btn'><i class='fa fa-trash-o'></button></td></tr>";
                    }
                }else {
                    if (result[res].ratesApplicable && result[res].ratesApplicable === "Y") {
                        markup = "<tr><td><input type='hidden' class='mandatory form-control' value='" + result[res].mandatory + "'><input type='hidden' class='section form-control' value='" + result[res].premId +
                            "'><input type='hidden' class='ratesApplicable form-control' value='" + result[res].ratesApplicable + "'>" + result[res].sectionDesc + "</td><td>" + ' <input type="text" class="amount form-control">' +
                            "</td><td><input type='text' class='rate form-control' readonly value='" + result[res].rate + "'></td><td><input type='text' class='divFactor form-control' readonly value='" + result[res].divFactor + "'></td></td><td>" +
                            "<input type='text' class='freeLimit form-control' readonly  value='" + result[res].freeLimit + "'></td>" +
                            "<td><button type='button' class='btn btn-danger btn btn-danger btn-xs hyperlink-btn'><i class='fa fa-trash-o'></button></td></tr>";
                    }
                    else if (result[res].rider && result[res].rider === "Y") {
                        markup = "<tr><td><input type='hidden' class='mandatory form-control' value='" + result[res].mandatory + "'><input type='hidden' class='section form-control' value='" + result[res].premId +
                            "'><input type='hidden' class='ratesApplicable form-control' value='" + result[res].ratesApplicable + "'>" + result[res].sectionDesc + "</td><td>" + ' <input type="text" class="amount form-control" readonly>' +
                            "</td><td><input type='text' class='rate form-control' value='" + result[res].rate + "'></td><td><input type='text' class='divFactor form-control' value='" + result[res].divFactor + "'></td></td><td>" +
                            "<input type='text' class='freeLimit form-control'  value='" + result[res].freeLimit + "'></td>" +
                            "<td><button type='button' class='btn btn-danger btn btn-danger btn-xs hyperlink-btn'><i class='fa fa-trash-o'></button></td></tr>";
                    }
                }
                $("#section_form_tbl").append(markup);
            }

            $("#section_form_tbl tr").find("input[type=text]").number(true, 2);
        },
        error: function (jqXHR, textStatus, errorThrown) {

        }
    });

}



function getQuotProducts(){

    var url = "quotProducts";
    var currTable = $('#prod_tbl').DataTable( {
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
            { "data": "binder",
                "render": function ( data, type, full, meta ) {

                    return full.binder;
                }
            },
            { "data": "account",
                "render": function ( data, type, full, meta ) {

                    return full.account;
                }
            },
            { "data": "product",
                "render": function ( data, type, full, meta ) {

                    return full.product;
                }
            },
            // { "data": "wef",
            //     "render": function ( data, type, full, meta ) {
            //         return moment(full.wef).format('DD/MM/YYYY');
            //     }
            // },
            // { "data": "wet" ,
            //     "render": function ( data, type, full, meta ) {
            //         return moment(full.wet).format('DD/MM/YYYY');
            //     }
            // },
            { "data": "sumInsured",
                "render": function ( data, type, full, meta ) {

                    return UTILITIES.currencyFormat(full.sumInsured);
                }
            },
            { "data": "premium",
                "render": function ( data, type, full, meta ) {

                    return UTILITIES.currencyFormat(full.premium);
                }
            },
            { "data": "commAmt",
                "render": function ( data, type, full, meta ) {

                    return UTILITIES.currencyFormat(full.commAmt);
                }
            },
            {
                "data": "quoteProductId",
                "render": function ( data, type, full, meta ) {
                    if(full.quotStatus){
                        if(full.quotStatus==="D")
                            return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-quotprod='+encodeURI(JSON.stringify(full)) + ' onclick="editQuoteProd(this);"><i class="fa fa-pencil-square-o"></button>';
                        else
                            return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-quotprod='+encodeURI(JSON.stringify(full)) + ' onclick="editQuoteProd(this);" disabled><i class="fa fa-pencil-square-o"></button>';

                    }
                    else
                        return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-quotprod='+encodeURI(JSON.stringify(full)) + ' onclick="editQuoteProd(this);"><i class="fa fa-pencil-square-o"></button>';
                }

            },
            {
                "data": "quoteProductId",
                "render": function ( data, type, full, meta ) {
                    if(full.quotStatus){
                        if(full.quotStatus==="D")
                            return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-quotprod='+encodeURI(JSON.stringify(full)) + ' onclick="deleteQuotProd(this);"><i class="fa fa-pencil-square-o"></button>';
                        else
                            return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-quotprod='+encodeURI(JSON.stringify(full)) + ' onclick="deleteQuotProd(this);" disabled><i class="fa fa-pencil-square-o"></button>';

                    }
                    else
                        return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-quotprod='+encodeURI(JSON.stringify(full)) + ' onclick="deleteQuotProd(this);"><i class="fa fa-pencil-square-o"></button>';

                }

            },
        ]
    } );

    $('#prod_tbl tbody').on( 'click', 'tr', function () {
        $(this).addClass('active').siblings().removeClass('active');
        var aData = currTable.rows('.active').data();
        console.log(aData);
        if (aData[0] === undefined || aData[0] === null){

        }
        else{
            $("#risk-binder").text(aData[0].binder);
            $("#risk-binder-id").val(aData[0].binderId);
            $("#risk-binder-code").val(aData[0].binderId);
            $("#risk-bind-code").val(aData[0].binderId);
            $("#binder-id").val(aData[0].binderId);
            $("#product-id").val(aData[0].productId);
            $("#pol-agent-id").val(aData[0].acctId);
            $("#bind-name").val(aData[0].binder);
            $("#prod-binder").val(aData[0].bindType);
            $("#quot-prod-id-pk").val(aData[0].quoteProductId);
            $("#risk-det-id-pk").val(aData[0].binderId);
            $('#risk_tbl').DataTable().ajax.url( "quotProductRisks/"+aData[0].quoteProductId ).load();
            $('#polTaxesList').DataTable().ajax.url( "quotProTaxes/"+aData[0].quoteProductId).load();
            createPolicyClauses(aData[0].quoteProductId);

        }
    } );

    return currTable;
}

function editQuoteProd(button){
    var product = JSON.parse(decodeURI($(button).data("quotprod")));
    $("#pol-ins-comp").text(product["account"]);
    $("#pol-prod-name").text(product["product"]);
    $("#quot-prod-pk").val(product["quoteProductId"]);
    $("#product-id").val(product["productId"]);
    $("#pol-agent-id").val(product["acctId"]);
    $("#bind-name").val(product["binder"]);
    $("#binder-id").val(product["binderId"]);
    $("#pol-bin-type").val(product["bindType"]);
    $("#product-wef-date").val(moment(product["wef"]).format('DD/MM/YYYY'));
    $("#product-wet-date").val(moment(product["wet"]).format('DD/MM/YYYY'));
    populateBinderLov();
    $("#quot-prod-tbl").hide();
    $("#quot-prod-div").show();
    $("#btn-save-quot-prod").show();
    $("#btn-cancel-quot-prod").show();
    $("#btn-add-quot-prod").hide();
}

function deleteQuotProd(button){
    var product = JSON.parse(decodeURI($(button).data("quotprod")));
    bootbox.confirm("Are you sure want to delete "+product['product'].proDesc+"?", function(result) {
        if(result){
            $('#myPleaseWait').modal({
                backdrop: 'static',
                keyboard: true
            });
            $.ajax({
                type: 'GET',
                url:  'deleteQuoteProduct/' + product['quoteProductId'],
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
                    $('#prod_tbl').DataTable().ajax.reload();
                    $('#risk_tbl').DataTable().ajax.url( "quotProductRisks/"+-2000 ).load();
                    populateQuotDetails();
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




function getProductRisks(qpCode){

    var url = "quotProductRisks/"+qpCode;
    var currTable = $('#risk_tbl').DataTable( {
        "processing": true,
        "serverSide": true,
        autoWidth: true,
        "ajax": {
            'url': url,
        },
        lengthMenu: [ [10,15,20], [10,15,20] ],
        pageLength: 10,
        destroy: false,
        "columns": [
            { "data": "riskShtDesc" },
            { "data": "riskDesc" },
            // { "data": "wefDate",
            //     "render": function ( data, type, full, meta ) {
            //         return moment(full.wefDate).format('DD/MM/YYYY');
            //     }
            // },
            // { "data": "wetDate" ,
            //     "render": function ( data, type, full, meta ) {
            //         return moment(full.wetDate).format('DD/MM/YYYY');
            //     }
            // },
            { "data": "subDesc",
                "render": function ( data, type, full, meta ) {

                    return full.subDesc;
                }
            },
            { "data": "covName",
                "render": function ( data, type, full, meta ) {

                    return full.covName;
                }
            },
            { "data": "sumInsured",
                "render": function ( data, type, full, meta ) {

                    return UTILITIES.currencyFormat(full.sumInsured);
                }
            },
            { "data": "premium",
                "render": function ( data, type, full, meta ) {

                    return UTILITIES.currencyFormat(full.premium);
                }
            },
            {
                "data": "riskId",
                "render": function ( data, type, full, meta ) {
                    if(full.quotStatus){
                        if(full.quotStatus==="D")
                            return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-policyrisks='+encodeURI(JSON.stringify(full)) + ' onclick="editQuoteRisk(this);"><i class="fa fa-pencil-square-o"></button>';
                        else
                            return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-policyrisks='+encodeURI(JSON.stringify(full)) + ' onclick="editQuoteRisk(this);" disabled><i class="fa fa-pencil-square-o"></button>';

                    }
                    else
                        return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-policyrisks='+encodeURI(JSON.stringify(full)) + ' onclick="editQuoteRisk(this);"><i class="fa fa-pencil-square-o"></button>';
                }

            },
            {
                "data": "riskId",
                "render": function ( data, type, full, meta ) {
                    if(full.quotStatus){
                        if(full.quotStatus==="D")
                            return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-policyrisks='+encodeURI(JSON.stringify(full)) + ' onclick="deleteRisk(this);"><i class="fa fa-pencil-square-o"></button>';
                        else
                            return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-policyrisks='+encodeURI(JSON.stringify(full)) + ' onclick="deleteRisk(this);" disabled><i class="fa fa-pencil-square-o"></button>';

                    }
                    else
                        return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-policyrisks='+encodeURI(JSON.stringify(full)) + ' onclick="deleteRisk(this);"><i class="fa fa-pencil-square-o"></button>';

                }

            },
        ]
    } );

    $('#risk_tbl tbody').on( 'click', 'tr', function () {
        $(this).addClass('active').siblings().removeClass('active');
        var aData = currTable.rows('.active').data();
        if (aData[0] === undefined || aData[0] === null){

        }
        else{
            $("#risk-code-pk").val(aData[0].riskId);
            $('#section_tbl').DataTable().ajax.url( "quotRiskLimits/"+aData[0].riskId ).load();
        }
    } );

    return currTable;
}


function deleteRisk(button){
    var risks = JSON.parse(decodeURI($(button).data("policyrisks")));
    bootbox.confirm("Are you sure want to delete "+risks['riskShtDesc']+"?", function(result) {
        if(result){
            $('#myPleaseWait').modal({
                backdrop: 'static',
                keyboard: true
            });
            $.ajax({
                type: 'GET',
                url:  'deleteProductRisk/' + risks['riskId'],
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
                    $('#risk_tbl').DataTable().ajax.reload();
                    $('#section_tbl').DataTable().ajax.url( "quotRiskLimits/"+-2000 ).load();
                    populateQuotDetails();
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



function getRiskSections(riskCode){
    var url = "quotRiskLimits/"+riskCode;
    return $('#section_tbl').DataTable({
        "processing": true,
        "serverSide": true,
        autoWidth: true,
        "ajax": {
            'url': url,
        },
        lengthMenu: [[10, 15, 20], [10, 15, 20]],
        pageLength: 10,
        destroy: true,
        "columns": [
            {
                "data": "secName",
                "render": function (data, type, full, meta) {

                    return full.secName;
                }
            },
            {
                "data": "amount",
                "render": function (data, type, full, meta) {

                    return UTILITIES.currencyFormat(full.amount);
                }
            },
            {"data": "rate"},
            {
                "data": "prem",
                "render": function (data, type, full, meta) {

                    return UTILITIES.currencyFormat(full.prem);
                }
            },
            {"data": "divFactor"},
            {"data": "freeLimit"},
            {
                "data": "sectId",
                "render": function (data, type, full, meta) {
                    if (full.quotStatus) {
                        if (full.quotStatus === "D")
                            return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-risksections=' + encodeURI(JSON.stringify(full)) + ' onclick="editRiskSection(this);"><i class="fa fa-pencil-square-o"></button>';
                        else
                            return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-risksections=' + encodeURI(JSON.stringify(full)) + ' onclick="editRiskSection(this);" disabled><i class="fa fa-pencil-square-o"></button>';

                    } else
                        return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-risksections=' + encodeURI(JSON.stringify(full)) + ' onclick="editRiskSection(this);"><i class="fa fa-pencil-square-o"></button>';
                }

            },
            {
                "data": "sectId",
                "render": function (data, type, full, meta) {
                    if (full.quotStatus) {
                        if (full.quotStatus === "D")
                            return '<button type="button" class="btn btn-danger btn btn-info btn-xs" data-risksections=' + encodeURI(JSON.stringify(full)) + ' onclick="deleteRiskSection(this);"><i class="fa fa-trash-o"></button>';
                        else
                            return '<button type="button" class="btn btn-danger btn btn-info btn-xs" data-risksections=' + encodeURI(JSON.stringify(full)) + ' onclick="deleteRiskSection(this);" disabled><i class="fa fa-trash-o"></button>';

                    } else
                        return '<button type="button" class="btn btn-danger btn btn-info btn-xs" data-risksections=' + encodeURI(JSON.stringify(full)) + ' onclick="deleteRiskSection(this);"><i class="fa fa-trash-o"></button>';
                }

            },
        ]
    });
}


function deleteRiskSection(button){
    var section = JSON.parse(decodeURI($(button).data("risksections")));
    bootbox.confirm("Are you sure want to delete the selected section?", function(result) {
        if(result){
            $('#myPleaseWait').modal({
                backdrop: 'static',
                keyboard: true
            });
            $.ajax({
                type: 'GET',
                url:  'deleteRiskSection/' + section['sectId'],
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
                    $('#section_tbl').DataTable().ajax.reload();
                    populateQuotDetails();
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


function editRiskSection(button){
    var section = JSON.parse(decodeURI($(button).data("risksections")));
    $("#sect-code-pk").val(section['sectId']);
    $("#sect-limit-amt").val(section['amount']);
    $("#sect-rate").val(section['rate']);
    $("#sect-free-limit").val(section['freeLimit']);
    $("#sect-annual-earning").val(section['annualEarnings']);
    $("#sect-div-fact").val(section['divFactor']);
    $("#sect-multi-rate").val(section['multiRate']);
    //$("#chk-compute").prop("checked", section["compute"]);
    $("#risk-sect-id").val(section['secId']);
    $("#risk-sect-name").val(section['secName']);
    $("#sect-prem-id-pk").val(section['rateId']);
    $("#risk-sect-code-pk").val(section['riskId']);
    if($("#prod-binder").val()==="B"){
        $("#sect-rate").prop("readonly",true);
        $("#sect-free-limit").prop("readonly",true);
        $('#sect-div-fact option:not(:selected)').prop('readonly', true);
        $("#sect-multi-rate").prop("readonly",true);
    }
    else{
        $("#sect-rate").prop("readonly",false);
        $("#sect-free-limit").prop("readonly",false);
        $('#sect-div-fact option:not(:selected)').prop('readonly', false);
        $("#sect-multi-rate").prop("readonly",false);
    }
    populateRiskSections($("#risk-det-id-pk").val());
    $('#sect-limit-amt,#sect-free-limit').number( true, 2 );
    $('#sectModal').modal({
        backdrop: 'static',
        keyboard: true
    });
}


function saveRiskSections(){
    var $classForm = $('#risk-sect-form');
    var validator = $classForm.validate();
    $('#saveRiskSection').click(function(){
        if (!$classForm.valid()) {
            return;
        }
        $('#myPleaseWait').modal({
            backdrop: 'static',
            keyboard: true
        });

        var $btn = $(this).button('Saving');
        var data = {};
        $classForm.serializeArray().map(function(x){data[x.name] = x.value;});
        var url = "saveRiskSections";
        var request = $.post(url, data );
        request.success(function(){
            $('#myPleaseWait').modal('hide');
            new PNotify({
                title: 'Success',
                text: 'Record Created/Updated Successfully',
                type: 'success',
                styling: 'bootstrap3'
            });
            $('#prod_tbl').DataTable().ajax.reload();
            $('#risk_tbl').DataTable().ajax.reload();
            $('#section_tbl').DataTable().ajax.reload();
            populateQuotDetails();
            validator.resetForm();
            $('#risk-sect-form').find("input[type=text],input[type=number],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden], textarea,select").val("");
            $('#sectModal').modal('hide');
        });

        request.error(function(jqXHR, textStatus, errorThrown){
            $('#myPleaseWait').modal('hide');
            bootbox.alert(jqXHR.responseText);
        });
        request.always(function(){
            $btn.button('reset');
        });
    });
}


function populateRiskSections(detCode){
    if($("#risk-sect-frm").filter("div").html() != undefined)
    {
        Select2Builder.initAjaxSelect2({
            containerId : "risk-sect-frm",
            sort : 'desc',
            change: function(e, a, v){
                //$("#pm-id").val(e.added.pmId);
            },
            formatResult : function(a)
            {
                return a.desc;
            },
            formatSelection : function(a)
            {
                return a.desc;
            },
            initSelection: function (element, callback) {
                var code  = $('#risk-sect-id').val();
                var name = $("#risk-sect-name").val();
                var data = {desc:name,id:code};
                callback(data);
            },
            id: "id",
            width:"250px",
            params: {detId: detCode},
            placeholder:"Select Section"

        });
    }
}



function createPolicyClauses(qrCode){
    var url = "quotProClauses/"+qrCode;
    var currTable = $('#polclausesList').DataTable( {
        "processing": true,
        "serverSide": true,
        autoWidth: true,
        "ajax": {
            'url': url,
        },
        lengthMenu: [ [5], [5] ],
        pageLength: 5,
        destroy: true,
        "columns": [
            { "data": "clauHeading" },
            { "data": "clause",
                "render": function ( data, type, full, meta ) {
                    if(full.clauseType){
                        if(full.clauseType ==='E') return "Excess";
                        else if(full.clauseType ==='L') return "Limits";
                        else if(full.clauseType ==='C') return "Clause";
                        else return "";
                    }
                    else{
                        return "";
                    }
                }
            },
            { "data": "editable" },
            { "data": "clauWording" },
            {
                "data": "qpClauId",
                "render": function ( data, type, full, meta ) {
                    if(full.quotStatus) {
                        if (full.quotStatus === "D") {
                            if(full.editable && full.editable==='Yes')  {
                                return '<button type="button" class="btn btn-success btn btn-info btn-xs" type="button" data-clauses=' + encodeURI(JSON.stringify(full)) + ' onclick="editPolicyClause(this);"><i class="fa fa-pencil-square-o"></button>';
                            } else
                                return '<button type="button" class="btn btn-success btn btn-info btn-xs" type="button" data-clauses=' + encodeURI(JSON.stringify(full)) + ' onclick="editPolicyClause(this);" disabled><i class="fa fa-pencil-square-o"></button>';
                        }
                        else
                            return '<button type="button" class="btn btn-success btn btn-info btn-xs" type="button" data-clauses='+encodeURI(JSON.stringify(full)) + ' onclick="editPolicyClause(this);" disabled><i class="fa fa-pencil-square-o"></button>';

                    }
                    else
                        return '<button type="button" class="btn btn-success btn btn-info btn-xs" type="button" data-clauses='+encodeURI(JSON.stringify(full)) + ' onclick="editPolicyClause(this);" disabled><i class="fa fa-pencil-square-o"></button>';

                }

            },
            {
                "data": "qpClauId",
                "render": function ( data, type, full, meta ) {
                    if(full.quotStatus){
                        if(full.quotStatus==="D") {
                            return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-clauses='+encodeURI(JSON.stringify(full)) + ' onclick="deletePolicyClause(this);"><i class="fa fa-trash-o"></button>';

                        }

                        else
                            return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-clauses='+encodeURI(JSON.stringify(full)) + ' onclick="deletePolicyClause(this);" disabled><i class="fa fa-trash-o"></button>';

                    }
                    else
                        return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-clauses='+encodeURI(JSON.stringify(full)) + ' onclick="deletePolicyClause(this);" disabled><i class="fa fa-trash-o"></button>';

                }

            },
        ]
    } );
    return currTable;
}

function createQuoteClauses(){
    var $classForm = $('#pol-clause-form');
    var validator = $classForm.validate();
    $('#savepolClauseBtn').click(function(){
        if (!$classForm.valid()) {
            return;
        }
        $('#myPleaseWait').modal({
            backdrop: 'static',
            keyboard: true
        });
        var $btn = $(this).button('Saving');
        var data = {};
        $classForm.serializeArray().map(function(x){data[x.name] = x.value;});
        var url = "createQuoteClause";
        var request = $.post(url, data );
        request.success(function(){
            $('#myPleaseWait').modal('hide');
            new PNotify({
                title: 'Success',
                text: 'Record created/updated Successfully',
                type: 'success',
                styling: 'bootstrap3'
            });
            $('#polclausesList').DataTable().ajax.reload();
            validator.resetForm();
            $('#pol-clause-form').find("input[type=text],input[type=number],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden], textarea,select").val("");
            $("#chk-cl-editable").prop("checked", false);
            $('#clauseModal').modal('hide');
        });

        request.error(function(jqXHR, textStatus, errorThrown){
            $('#myPleaseWait').modal('hide');
            new PNotify({
                title: 'Error',
                text: jqXHR.responseText,
                type: 'error',
                styling: 'bootstrap3'
            });
        });
        request.always(function(){
            $btn.button('reset');
        });
    });
}


function editPolicyClause(button){
    var clause = JSON.parse(decodeURI($(button).data("clauses")));
    console.log(clause);
    if(!clause['editable']){
        bootbox.alert("The Selected Clause is not Editable");
        return;
    }
    $("#pol-clause-code").val(clause['qpClauId']);
    $("#sub-clause-code").val(clause['clauseId']);
    $("#sub-clau-id").val(clause['clauShtDesc']);
    $("#sub-clause-name").val(clause['clauHeading']);
    $("#chk-cl-editable").prop("checked", clause['editable']);
    $("#sub-cla-wording").val(clause['clauWording']);
    $("#clause-pol-code").val(clause['quoteProductId'])
    $('#clauseModal').modal({
        backdrop: 'static',
        keyboard: true
    })
}


function createPolicyTaxes(qrCode){
    var url = "quotProTaxes/"+qrCode;
    var currTable = $('#polTaxesList').DataTable( {
        "processing": true,
        "serverSide": true,
        autoWidth: true,
        "ajax": {
            'url': url,
        },
        lengthMenu: [ [5], [5] ],
        pageLength: 5,
        destroy: true,
        "columns": [
            { "data": "revItemCode",
                "render": function ( data, type, full, meta ) {
                    return UTILITIES.getRevDesc(full.revItemCode);
                }
            },
            { "data": "taxRate" },
            { "data": "divFactor" },
            { "data": "rateType" },
            { "data": "taxAmount",
                "render": function ( data, type, full, meta ) {

                    return UTILITIES.currencyFormat(full.taxAmount);
                }
            },
            { "data": "taxLevel",
                "render": function ( data, type, full, meta ) {
                    if(full.taxLevel){
                        if(full.taxLevel==="R")
                            return "Risk";
                        else if(full.taxLevel==="P")
                            return "Policy";
                    }
                    else
                        return "Policy";

                }
            },
            {
                "data": "polTaxId",
                "render": function ( data, type, full, meta ) {
                    if(full.quotStatus){
                        if(full.quotStatus==="D")
                            return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-poltaxes='+encodeURI(JSON.stringify(full)) + ' onclick="editPolTaxes(this);"><i class="fa fa-pencil-square-o"></button>';
                        else
                            return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-poltaxes='+encodeURI(JSON.stringify(full)) + ' onclick="editPolTaxes(this);" disabled><i class="fa fa-pencil-square-o"></button>';

                    }
                    else
                        return '<button type="button" class="btn btn-success btn btn-info btn-xs" type="button" data-poltaxes='+encodeURI(JSON.stringify(full)) + ' onclick="editPolTaxes(this);" disabled><i class="fa fa-pencil-square-o"></button>';

                }

            },
            {
                "data": "polTaxId",
                "render": function ( data, type, full, meta ) {
                    if(full.quotStatus){
                        if(full.quotStatus==="D")
                            return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-poltaxes='+encodeURI(JSON.stringify(full)) + ' onclick="deletePolTaxes(this);"><i class="fa fa-trash-o"></button>';
                        else
                            return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-poltaxes='+encodeURI(JSON.stringify(full)) + ' onclick="deletePolTaxes(this);" disabled><i class="fa fa-trash-o"></button>';

                    }
                    else
                        return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-poltaxes='+encodeURI(JSON.stringify(full)) + ' onclick="deletePolTaxes(this);" disabled><i class="fa fa-trash-o"></button>';


                }

            },
        ]
    } );
    return currTable;
}

function editPolTaxes(button) {
    var tax = JSON.parse(decodeURI($(button).data("poltaxes")));
    console.log("testing again =" + UTILITIES.getRevDesc(tax["revItemCode"]));

    // $(".categoryDisplay").show();
    var quotId = $("#tax-quot-id").val(quotId);
    var taxId = $("#tax-id").val(tax['polTaxId']);
    $("#Trans-Code").text(UTILITIES.getRevDesc(tax["revItemCode"]));
    var rate = $("#tax-rate").val(tax['taxRate']);
    var divFactor = $("#tax-rate-divfact").val(tax['divFactor']);
    $('#editTaxModal').modal('show');
}
function saveTaxEdits(){

    var taxId = $("#tax-id").val();
    var rate = $("#tax-rate").val();
    var divFactor = $("#tax-rate-divfact").val();
    $.ajax({
        type: 'POST',
        url: 'editQuotGenTax',
        data: {
            'taxRate': rate,
            'polTaxId': taxId,
            'divFactor': divFactor
        },
    }).done(function (s) {
        new PNotify({
            title: 'Success',
            text: 'Record Edited Successfully',
            type: 'success',
            styling: 'bootstrap3'
        });

        $('#polTaxesList').DataTable().ajax.reload();
        getQuotationDetails();

    }).fail(function (jqXHR, textStatus, errorThrown) {
        new PNotify({
            title: 'Error',
            text: jqXHR.responseText,
            type: 'error',
            styling: 'bootstrap3'
        });
    })
}
function deletePolTaxes(button){
    var tax = JSON.parse(decodeURI($(button).data("poltaxes")));
    bootbox.confirm("Are you sure want to delete?", function(result) {
        if(result){
            $.ajax({
                type: 'GET',
                url:  'deleteQuotGenTax/' + tax["polTaxId"],
                dataType: 'json',
                async: true,
                success: function(result) {
                    new PNotify({
                        title: 'Success',
                        text: 'Record Deleted Successfully',
                        type: 'success',
                        styling: 'bootstrap3'
                    });
                    $('#polTaxesList').DataTable().ajax.reload();
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

    });
}
function getNewPremItemsModal(){

    $("#btn-add-new-section").click(function(){
        if($("#risk-code-pk").val() != ''){
            $("#prem-item-risk-id").val($("#risk-code-pk").val());
            getNewPremItems("");
            $('#premItemsModal').modal({
                backdrop: 'static',
                keyboard: true
            });
        }
        else{
            bootbox.alert("Select Risk to add new Premium Item")
        }
    });

    $("#savenewPremItem").on('click', function(){
        var items = getNewCreatePremItems();
        if(items.length==0){
            bootbox.alert("No Section Selected to add..");
            return;
        }

        var $currForm = $('#new-prem-items-form');
        var currValidator = $currForm.validate();
        if (!$currForm.valid()) {
            return;
        }
        var data = {};
        $currForm.serializeArray().map(function(x){data[x.name] = x.value;});
        var url = "createPremiumItems";

        data.sections = items;
        $('#myPleaseWait').modal({
            backdrop: 'static',
            keyboard: true
        });
        $.ajax(
            {
                url:url,
                type: "POST",
                data: JSON.stringify(data),
                success: function(s){
                    $('#myPleaseWait').modal('hide');
                    new PNotify({
                        title: 'Success',
                        text: 'Selected sections added Successfully',
                        type: 'success',
                        styling: 'bootstrap3'
                    });
                    $('#section_tbl').DataTable().ajax.reload();
                    $('#prod_tbl').DataTable().ajax.reload();
                    $('#risk_tbl').DataTable().ajax.reload();
                    populateQuotDetails();
                    $('#premItemsModal').modal('hide');
                    items = [];
                },
                error: function(jqXHR, textStatus, errorThrown){
                    $('#myPleaseWait').modal('hide');
                    new PNotify({
                        title: 'Error',
                        text: jqXHR.responseText,
                        type: 'error',
                        styling: 'bootstrap3'
                    });
                },
                dataType: "json",
                contentType: "application/json"
            } );
    });
}


function getNewCreatePremItems(){
    var arr = [];
    $("#new_prem_items_form tr").each(function(row,tr){
        var checked   = $(this).find('.section-check').eq(0).is(":checked");
        var section   = $(this).find('.section').eq(0).val();
        var premId   = $(this).find('.premId').eq(0).val();
        var rate   = $(this).find('.rate').eq(0).val();
        var divFactor   = $(this).find('.divFactor').eq(0).val();
        var freeLimit   = $(this).find('.freeLimit').eq(0).val();
        var amount = $(this).find('.amount').eq(0).val();
        var multiplierRate= $(this).find('.multiplierRate').eq(0).val();
        if(checked){
            arr.push({
                section: section,
                premId: premId,
                rate:rate,
                divFactor:divFactor,
                freeLimit:freeLimit,
                amount: amount,
                multiplierRate:multiplierRate
            });
        }

    });

    return arr;
}


function getNewPremItems(sectdesc){
    $('#myPleaseWait').modal({
        backdrop: 'static',
        keyboard: true
    });
    $.ajax({
        type: 'GET',
        url:  'getNewPremiumItems',
        dataType: 'json',
        data: {"detId": $("#risk-det-id-pk").val(),"riskId":$("#risk-code-pk").val(),"secName":sectdesc},
        async: true,
        success: function(result) {
            $('#myPleaseWait').modal('hide');
            $("#new_prem_items_form tbody").each(function(){
                $(this).remove();
            });
            for(var res in result) {

                var markup = "<tr><td><input type='checkbox' class='section-check' id='" + result[res].id + "'></td><td><input type='hidden' class='section form-control' value='" + result[res].section.id +
                    "'><input type='hidden' class='premId form-control' value='" + result[res].id + "'>" + result[res].section.desc + "</td><td>" + ' <input type="text" class="amount form-control" required>' +
                    "</td><td><input type='text' class='rate form-control' value='" + result[res].rate + "'></td><td><input type='text' class='divFactor form-control'  value='" + result[res].divFactor + "'></td></td><td>" +
                    "<input type='text' class='freeLimit form-control'  value='" + result[res].freeLimit + "'></td><td><input type='text' class='multiplierRate form-control'  value='" + result[res].multiRate + "'></td></tr>";
                if ($("#prod-binder").val() === "B") {
                    if (result[res].section && result[res].section.type === "RD") {
                        markup = "<tr><td><input type='checkbox' class='section-check' id='" + result[res].id + "'></td><td><input type='hidden' class='section form-control' value='" + result[res].section.id +
                            "'><input type='hidden' class='premId form-control' value='" + result[res].id + "'>" + result[res].section.desc + "</td><td>" + ' <input type="text" class="amount form-control" required readonly>' +
                            "</td><td><input type='text' class='rate form-control' value='" + result[res].rate + "' readonly></td><td><input type='text' class='divFactor form-control'  value='" + result[res].divFactor + "' readonly></td></td><td>" +
                            "<input type='text' class='freeLimit form-control'  value='" + result[res].freeLimit + "' readonly></td><td><input type='text' class='multiplierRate form-control'  value='" + result[res].multiRate + "'></td></tr>";

                    }
                    else{
                        markup = "<tr><td><input type='checkbox' class='section-check' id='" + result[res].id + "'></td><td><input type='hidden' class='section form-control' value='" + result[res].section.id +
                            "'><input type='hidden' class='premId form-control' value='" + result[res].id + "'>" + result[res].section.desc + "</td><td>" + ' <input type="text" class="amount form-control" required >' +
                            "</td><td><input type='text' class='rate form-control' value='" + result[res].rate + "' readonly></td><td><input type='text' class='divFactor form-control'  value='" + result[res].divFactor + "' readonly></td></td><td>" +
                            "<input type='text' class='freeLimit form-control'  value='" + result[res].freeLimit + "' readonly></td><td><input type='text' class='multiplierRate form-control'  value='" + result[res].multiRate + "'></td></tr>";

                    }
                }
                $("#new_prem_items_form").append(markup);
            }

            $("#new_prem_items_form tr").find("input[type=text]").number( true, 2 );
        },
        error: function(jqXHR, textStatus, errorThrown) {
            $('#myPleaseWait').modal('hide');
        }
    });

}


function addQuoteProd(){
    $("#btn-add-quot-prod").on('click', function(){
        $("#quot-prod-tbl").hide();
        $("#quot-prod-div").show();
        $("#btn-save-quot-prod").show();
        $("#btn-cancel-quot-prod").show();
        $("#btn-add-quot-prod").hide();
        $("#btn-add-risk").hide();
        $("#btn-save-risk").hide();
        $("#btn-save-cancel").hide();
        $('#risk_tbl').DataTable().ajax.url( "quotProductRisks/"+-2000 ).load();
        $('#section_tbl').DataTable().ajax.url( "quotRiskLimits/"+-2000 ).load();
        $("#risk-div").hide();
        $("#risk-form").show();
        $("#sect-div").hide();
        $("#prem-rates-div").show();
        $('#quot-pro-form').find("input[type=text],input[type=number],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden], textarea,select").val("");
        $("#pol-ins-comp").text("");
        $("#pol-prod-name").text("");
        $("#insured-code").val("");
        $("#insured-name").val("");
        $("#insured-other-name").val("");
        $("#pol-bin-type").val("M");
        $("#pol-bin-type").prop("disabled",true);
        $('#insured-frm').select2('val', null);
        $("#binder-frm").select2('val', null);
        populateBinderLov();
        populateInsuredLov();
        $('#subclass-frm').select2('val', null);
        populateSubclassLov();
        $('#covertypes-frm').select2('val', null);
        populateCoverTypesLov();
        getPremiumRates(-2000);
        $('#risk-form').find("input[type=text],input[type=number],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden], textarea,select").val("");

        $("#prorated-full").val("P");
        $("#risk-form").show();
        $("#risk-div").hide();
        $("#sect-div").hide();
        $("#prem-rates-div").show();
        $("#myTab #show-taxes,#show-clauses").hide();
        $('.nav-tabs a[href="#tab_content1"]').tab('show');
        $("#risk-pro-code-pk").val($("#quot-prod-id-pk").val());
        $("#risk-binder-code").val($("#risk-bind-code").val());
        $("#binder-id").val($("#risk-bind-code").val());
        populateSubclassLov($("#risk-bind-code").val());
        var fromDate = $("#from-date").val();
        var toDate = $("#wet-date").val();
        $("#risk-wef-date").val(fromDate);
        $("#risk-wet-date").val(toDate);
        $("#section_form_tbl tbody").each(function(){
            $(this).remove();
        });
        $("#btn-add-new-section").hide();
        $("#quot-panel").hide();
    });

    $("#btn-cancel-quot-prod").on('click', function(){
        $("#quot-prod-tbl").show();
        $("#quot-prod-div").hide();
        $("#btn-add-quot-prod").show();
        $("#btn-add-risk").show();
        $("#btn-save-risk").show();
        $("#btn-save-cancel").show();
        $("#quot-panel").show();
        $("#risk-div").show();
        $("#sect-div").show();
        $("#risk-form").hide();
        $("#prem-rates-div").hide();
        $("#myTab #show-taxes,#show-clauses").show();
        $('#prod_tbl').DataTable().ajax.reload();
        $('#risk_tbl').DataTable().ajax.url( "quotProductRisks/"+-2000 ).load();
    });
}

function newRisk(){
    $("#btn-add-risk").on('click', function(){
        if($("#quot-prod-id-pk").val() === ''){
            bootbox.alert("Select Product to Add A Risk");
            return;
        }
        $("#insured-code").val("");
        $("#insured-name").val("");
        $("#insured-other-name").val("");
        $('#subclass-frm').select2('val', null);
        populateSubclassLov();
        $('#covertypes-frm').select2('val', null);
        populateCoverTypesLov();
        getPremiumRates(-2000);
        $('#risk-form').find("input[type=text],input[type=number],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden], textarea,select").val("");

        $("#prorated-full").val("P");
        $("#insured-type").val("C");
        $("#insured-clnt-div").show();
        $("#insured-prs-div").hide();
        $("#insured-name").val("");
        $("#insured-code").val("");
        $("#insured-other-name").val("");
        $('#insured-frm').select2('val', null);
        populateInsuredLov();
        $("#risk-form").show();
        $("#risk-div").hide();
        $("#sect-div").hide();
        $("#prem-rates-div").show();
        $("#btn-save-risk").show();
        $("#btn-save-cancel").show();

        $("#myTab #show-taxes,#show-clauses").hide();
        $("#risk-pro-code-pk").val($("#quot-prod-id-pk").val());
        $("#risk-binder-code").val($("#risk-bind-code").val());
        $("#binder-id").val($("#risk-bind-code").val());
        populateSubclassLov($("#risk-bind-code").val());
        if($("#comm-rate1").val()){
            $("#comm-rate").val($("#comm-rate1").val());
        }
        if($("#prod-binder").val()==="B"){
            $("#comm-rate").prop("readonly",true);
        }
        else{
            $("#comm-rate").prop("readonly",false);
        }


        var fromDate = $("#from-date").val();
        var toDate = $("#wet-date").val();
        $("#risk-wef-date").val(fromDate);
        $("#risk-wet-date").val(toDate);
        $("#section_form_tbl tbody").each(function(){
            $(this).remove();
        });
        $("#btn-add-new-section").hide();
    });

    $("#btn-save-cancel").on('click', function(){
        populateQuotDetails();
    })
}

function createRisk(){
    var arr = getRskSections();
    if(arr.length==0){
        bootbox.alert("Cannot create Risk without sections")
        return false;
    }
    arr.shift();
    var $currForm = $('#risk-form');
    var currValidator = $currForm.validate();
    if (!$currForm.valid()) {
        return;
    }

    var data = {};
    $currForm.serializeArray().map(function(x){data[x.name] = x.value;});
    var url = "createRisk";
    data.sections = arr;
    $('#myPleaseWait').modal({
        backdrop: 'static',
        keyboard: true
    });

    $.ajax(
        {
            url:url,
            type: "POST",
            data: JSON.stringify(data),
            success: function(s){
                $('#myPleaseWait').modal('hide');
                new PNotify({
                    title: 'Success',
                    text: 'Risk Transaction created Successfully',
                    type: 'success',
                    styling: 'bootstrap3'
                });
                arr=[];
                $('#insured-frm').select2('val', null);
                populateInsuredLov();
                $('#subclass-frm').select2('val', null);
                populateSubclassLov($("#risk-bind-code").val());
                $('#covertypes-frm').select2('val', null);
                populateCoverTypesLov();
                $('#section_tbl').DataTable().ajax.reload();
                $('#prod_tbl').DataTable().ajax.reload();
                $('#risk_tbl').DataTable().ajax.reload();
                populateQuotDetails();
                $("#sect-div").show();
            },
            error: function(jqXHR, textStatus, errorThrown){
                $('#myPleaseWait').modal('hide');
                new PNotify({
                    title: 'Error',
                    text: jqXHR.responseText,
                    type: 'error',
                    styling: 'bootstrap3'
                });
            },
            dataType: "json",
            contentType: "application/json"
        } );
}



function updateRisk(){

    var $currForm = $('#risk-form');
    var currValidator = $currForm.validate();
    if (!$currForm.valid()) {
        return;
    }

    var data = {};
    $currForm.serializeArray().map(function(x){data[x.name] = x.value;});
    var url = "createRisk";
    $('#myPleaseWait').modal({
        backdrop: 'static',
        keyboard: true
    });

    $.ajax(
        {
            url:url,
            type: "POST",
            data: JSON.stringify(data),
            success: function(s){
                $('#myPleaseWait').modal('hide');
                new PNotify({
                    title: 'Success',
                    text: 'Risk Transaction Updated Successfully',
                    type: 'success',
                    styling: 'bootstrap3'
                });

                $('#insured-frm').select2('val', null);
                populateInsuredLov();
                $('#subclass-frm').select2('val', null);
                populateSubclassLov($("#risk-bind-code").val());
                $('#covertypes-frm').select2('val', null);
                populateCoverTypesLov();
                $('#section_tbl').DataTable().ajax.reload();
                $('#prod_tbl').DataTable().ajax.reload();
                $('#risk_tbl').DataTable().ajax.reload();
                populateQuotDetails();
                $("#sect-div").show();
            },
            error: function(jqXHR, textStatus, errorThrown){
                $('#myPleaseWait').modal('hide');
                new PNotify({
                    title: 'Error',
                    text: jqXHR.responseText,
                    type: 'error',
                    styling: 'bootstrap3'
                });
            },
            dataType: "json",
            contentType: "application/json"
        } );
}

function editQuoteRisk(button){
    var risks = JSON.parse(decodeURI($(button).data("policyrisks")));
    $("#risk-binder-code").val($("#risk-bind-code").val());
    $("#risk-id").val(risks["riskShtDesc"]);
    $("#risk-desc").val(risks["riskDesc"]);
    $("#risk-wef-date").val(moment(risks["wefDate"]).format('DD/MM/YYYY'));
    $("#risk-wet-date").val(moment(risks["wetDate"]).format('DD/MM/YYYY'));
    $("#prorated-full").val(risks["prorata"]);
    $("#comm-rate").val(risks["commRate"]);
    $("#overrid-prem").val(risks["butchargePrem"]);
    $("#insured-name").val(risks["fname"]);
    $("#insured-code").val(risks["tenId"]);
    $("#insured-other-name").val(risks["otherNames"]);
    $("#insured-type").val(risks["clientType"]);
    if(risks["clientType"] ==='C'){
        $('#insured-frm').select2('val', null);
    } else if ( risks["clientType"] ==='P') {
        $('#insured-prs-frm').select2('val', null);
    }
    populateInsuredLov();
    $("#risk-sub-code").val(risks["subId"]);
    $("#sub-name").val(risks["subDesc"]);
    populateSubclassLov($("#risk-bind-code").val());
    $("#risk-cov-code").val(risks["covId"]);
    $("#cover-name").val(risks["covName"]);
    populateCoverTypesLov();
    $("#binder-det-id").val(risks["binderId"]);
    $("#risk-pro-code-pk").val($("#quot-prod-id-pk").val());
    $("#risk-code-pk").val(risks["riskId"]);
    $("#risk-form").show();
    $("#risk-div").hide();
    $("#btn-save-risk").show();
    $("#btn-save-cancel").show();
    $("#myTab #show-taxes,#show-clauses").hide();
    $("#comm-rate").prop("readonly",true);
}

function createQuoteProduct(){
    var $currForm = $('#quot-pro-form');
    var currValidator = $currForm.validate();
    if (!$currForm.valid()) {
        return;
    }

    var data = {};
    $currForm.serializeArray().map(function(x){data[x.name] = x.value;});
    var arr = getRskSections();
    // console.log(arr);
    if(arr.length==0){
        bootbox.alert("Cannot create Quote without sections")
        return false;
    }
    arr.shift();
    data.sections = arr;
    data.riskBean = getRiskDetails();
    var url = "createQuoteProduct";
    $('#myPleaseWait').modal({
        backdrop: 'static',
        keyboard: true
    });

    $.ajax(
        {
            url:url,
            type: "POST",
            data: JSON.stringify(data),
            success: function(s){
                $('#myPleaseWait').modal('hide');
                new PNotify({
                    title: 'Success',
                    text: 'Quote Product created Successfully',
                    type: 'success',
                    styling: 'bootstrap3'
                });
                $('#binder-frm').select2('val', null);
                populateBinderLov();
                $('#section_tbl').DataTable().ajax.url( "quotRiskLimits/"+-2000 ).load();
                $('#risk_tbl').DataTable().ajax.url( "quotProductRisks/"+-2000 ).load();
                $('#prod_tbl').DataTable().ajax.reload();
                populateQuotDetails();
                $("#quot-prod-div").hide();
                $("#quot-prod-tbl").show();
                $("#quot-panel").show();
            },
            error: function(jqXHR, textStatus, errorThrown){
                $('#myPleaseWait').modal('hide');
                new PNotify({
                    title: 'Error',
                    text: jqXHR.responseText,
                    type: 'error',
                    styling: 'bootstrap3'
                });
            },
            dataType: "json",
            contentType: "application/json"
        } );
}


function updateQuoteProduct(){
    var $currForm = $('#quot-pro-form');
    var currValidator = $currForm.validate();
    if (!$currForm.valid()) {
        return;
    }

    var data = {};
    $currForm.serializeArray().map(function(x){data[x.name] = x.value;});
    //var arr = getRskSections();
    //// console.log(arr);
    //if(arr.length==0){
    //    bootbox.alert("Cannot create Quote without sections")
    //    return false;
    //}
    //arr.shift();
    //data.sections = arr;
    //data.riskBean = getRiskDetails();
    var url = "createQuoteProduct";
    $('#myPleaseWait').modal({
        backdrop: 'static',
        keyboard: true
    });

    $.ajax(
        {
            url:url,
            type: "POST",
            data: JSON.stringify(data),
            success: function(s){
                $('#myPleaseWait').modal('hide');
                new PNotify({
                    title: 'Success',
                    text: 'Quote Product created Successfully',
                    type: 'success',
                    styling: 'bootstrap3'
                });
                $('#binder-frm').select2('val', null);
                populateBinderLov();
                $('#section_tbl').DataTable().ajax.url( "quotRiskLimits/"+-2000 ).load();
                $('#risk_tbl').DataTable().ajax.url( "quotProductRisks/"+-2000 ).load();
                $('#prod_tbl').DataTable().ajax.reload();
                populateQuotDetails();
                $("#quot-prod-div").hide();
                $("#quot-prod-tbl").show();
                $("#quot-panel").show();
            },
            error: function(jqXHR, textStatus, errorThrown){
                $('#myPleaseWait').modal('hide');
                new PNotify({
                    title: 'Error',
                    text: jqXHR.responseText,
                    type: 'error',
                    styling: 'bootstrap3'
                });
            },
            dataType: "json",
            contentType: "application/json"
        } );
}

function getNewTaxes(prodId){

    $('#myPleaseWait').modal({
        backdrop: 'static',
        keyboard: true
    });
    $.ajax({
        type: 'GET',
        url:  'getNewTaxes',
        dataType: 'json',
        data: {"quoteProdId":prodId},
        async: true,
        success: function(result) {
            $('#myPleaseWait').modal('hide');
            $("#new_taxes_tbl tbody").each(function(){
                $(this).remove();
            });
            for(var res in result){
                var markup = "<tr><td><input type='checkbox' class='tax-check'><input type='hidden' class='tax-id form-control' value='"+result[res].polTaxId+
                    "'></td><td>"
                    + UTILITIES.getRevDesc(result[res].revenueItems.item) + "</td><td>"
                    +result[res].taxRate +"</td><td>"
                    +getRateType(result[res].rateType) +"</td></tr>";
                $("#new_taxes_tbl").append(markup);
            }

        },
        error: function(jqXHR, textStatus, errorThrown) {
            $('#myPleaseWait').modal('hide');
        }
    });

}

function getNewClauses(prodId){
    $('#myPleaseWait').modal({
        backdrop: 'static',
        keyboard: true
    });
    $.ajax({
        type: 'GET',
        url:  'getNewClauses/'+prodId,
        dataType: 'json',
        async: true,
        success: function(result) {
            console.log(result);
            $('#myPleaseWait').modal('hide');
            $("#new_clause_tbl tbody").each(function(){
                $(this).remove();
            });
            for(var res in result){
                var markup = "<tr><td><input type='checkbox' class='clause-check'><input type='hidden' class='clause-id form-control' value='"+result[res].subClauseId+
                    "'></td><td>"
                    + result[res].header + "</td></tr>";
                $("#new_clause_tbl").append(markup);
            }

        },
        error: function(jqXHR, textStatus, errorThrown) {
            $('#myPleaseWait').modal('hide');
        }
    });

}

function getCreateNewClauses(){
    var arr = [];
    $("#new_clause_tbl tr").each(function(row,tr){
        var checked   = $(this).find('.clause-check').eq(0).is(":checked");
        var clauseId   = $(this).find('.clause-id').eq(0).val();
        if(checked){
            arr.push(clauseId);
        }

    });

    return arr;
}


function getNewClausesModal(){
    $("#btn-add-new-clause").click(function(){
            if($("#quot-prod-id-pk").val() !== ''){
                $("#clause-pol-id").val($("#quot-prod-id-pk").val());
                getNewClauses($("#quot-prod-id-pk").val());
                $('#newclausesModal').modal({
                    backdrop: 'static',
                    keyboard: true
                });
            }
        else{
            bootbox.alert("Quotation Product Details are not available to add A clause")
        }
    });

    $("#savenewClause").on('click', function(){
        var items = getCreateNewClauses();
        if(items.length==0){
            bootbox.alert("No Clauses Selected to add..");
            return;
        }



        var $currForm = $('#new-clause-form');
        var currValidator = $currForm.validate();
        if (!$currForm.valid()) {
            return;
        }
        var data = {};
        $currForm.serializeArray().map(function(x){data[x.name] = x.value;});
        var url = "createNewClause";

        data.clauses = items;
        $('#myPleaseWait').modal({
            backdrop: 'static',
            keyboard: true
        });
        $.ajax(
            {
                url:url,
                type: "POST",
                data: JSON.stringify(data),
                success: function(s){
                    $('#myPleaseWait').modal('hide');
                    new PNotify({
                        title: 'Success',
                        text: 'Selected Clauses added Successfully',
                        type: 'success',
                        styling: 'bootstrap3'
                    });
                    $('#polclausesList').DataTable().ajax.reload();
                    $('#newclausesModal').modal('hide');
                    items = [];
                },
                error: function(jqXHR, textStatus, errorThrown){
                    $('#myPleaseWait').modal('hide');
                    new PNotify({
                        title: 'Error',
                        text: jqXHR.responseText,
                        type: 'error',
                        styling: 'bootstrap3'
                    });
                },
                dataType: "json",
                contentType: "application/json"
            } );
    });
}

function getRateType(type){
    if(type){
        if(type==="A") return "Amount";
        else if(type==="P") return "Percentage";
        else if(type==="M") return "Per Milli";
    }
    else {
        return "Amount";
    }
}

function getCreateNewTaxes(){
    var arr = [];
    $("#new_taxes_tbl tr").each(function(row,tr){
        var checked   = $(this).find('.tax-check').eq(0).is(":checked");
        var taxid   = $(this).find('.tax-id').eq(0).val();
        if(checked){
            arr.push(taxid);
        }

    });

    return arr;
}


function getNewTaxesModal() {
    $("#btn-add-new-tax").click(function () {
        if ($("#quot-prod-id-pk").val() != '') {
            $("#new-tax-quot-pr-id").val($("#quot-prod-id-pk").val());
            getNewTaxes($("#quot-prod-id-pk").val());
            $('#newtaxesModal').modal({
                backdrop: 'static',
                keyboard: true
            });
        }
        else {
            bootbox.alert("Quote Product Details are not available to add A Tax")
        }
    });



    $("#savenewTaxes").click(function () {
        var items = getCreateNewTaxes();
        if(items.length==0){
            bootbox.alert("No Tax Selected")
            return;
        }
        var $currForm = $('#new-taxes-form');
        var currValidator = $currForm.validate();
        if (!$currForm.valid()) {
            return;
        }
        var data = {};
        $currForm.serializeArray().map(function(x){data[x.name] = x.value;});
        var url = "createNewTax";

        data.taxes = items;
        $('#myPleaseWait').modal({
            backdrop: 'static',
            keyboard: true
        });
        $.ajax(
            {
                url:url,
                type: "POST",
                data: JSON.stringify(data),
                success: function(s){
                    $('#myPleaseWait').modal('hide');
                    new PNotify({
                        title: 'Success',
                        text: 'Selected Clauses added Successfully',
                        type: 'success',
                        styling: 'bootstrap3'
                    });
                    $('#polTaxesList').DataTable().ajax.reload();
                    populateQuotDetails();
                    $('#newtaxesModal').modal('hide');
                    items = [];
                },
                error: function(jqXHR, textStatus, errorThrown){
                    $('#myPleaseWait').modal('hide');
                    new PNotify({
                        title: 'Error',
                        text: jqXHR.responseText,
                        type: 'error',
                        styling: 'bootstrap3'
                    });
                },
                dataType: "json",
                contentType: "application/json"
            } );
    });
}


