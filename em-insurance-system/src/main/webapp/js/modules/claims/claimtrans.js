/**
 * Created by peter on 3/8/2017.
 */

var UTILITIES = UTILITIES || {};
$(function() {

    $(document).ready(function () {

        $(".datepicker-input").each(function() {
            $(this).datetimepicker({
                format: 'DD/MM/YYYY'
            });

        });

        $(document).ajaxStart(function () {
            $("#saveClmActivity,#saveReqClmDocsBtn").text('Saving....');
            $("#saveClmActivity,#saveReqClmDocsBtn").attr("disabled", true);
        });
        $(document).ajaxComplete(function () {
            $("#saveClmActivity,#saveReqClmDocsBtn").text('Save');
            $("#saveClmActivity,#saveReqClmDocsBtn").attr("disabled", false);
        });

        getClaimDetails();
        getClaimClaimants();
         getClaimPerils();
        getClmRequiredDocs();
       getClaimActivities();
        getClaimStatuses();
        getClaimUploads();
        uploadDoc();
         uploadClaimDocument();
        selectClmActivity();
        saveClaimActivity();
        selectNextReviewLov();
        selectProviderTypesLov();
        addClaimant();
        addSelectedPeril();
        saveClaimant();
        addClaimants();
        savePerilPayment();
        saveClaimPeril();
        getPerilsPayment(-2000);
        UTILITIES.emailReports();
        saveClaimStatus();
        uploadClaimReqDocument();
        saveRiskDocsList();
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

                }
            });

            if($(this).val()==="IN"){
                $("#email-cc").attr("readonly",true);
                $("#email-send-to").attr("readonly",false);
                $("#email-send-to").val("");
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
                $("#email-cc").val("");
            }
        });

        $("#self-as-clmnt2").val( "");
        $('#self-as-clmnt').change(function(){
            if(this.checked){
                $("#clmant-def").select2("readonly", true);
                $("#self-as-clmnt2").val( "on");
            }
            else{
                $("#clmant-def").select2("readonly", false);
                $("#self-as-clmnt2").val( "off");
            }

        });

        $("#clm_activities_tbl").on('click', '.btn-ready', function(){
            $('#myPleaseWait').modal({
                backdrop: 'static',
                keyboard: true
            });
            var data = $(this).closest('tr').find('#readyId').val();

            makeReadyRevision(data);

        })
        $("#clm_activities_tbl").on('click', '.btn-undo', function(){
            $('#myPleaseWait').modal({
                backdrop: 'static',
                keyboard: true
            });
            var data = $(this).closest('tr').find('#undoreadyId').val();

            undoMakeReadyTrans(data);
        })
        $("#clm_activities_tbl").on('click', '.btn-authorise', function(){

            var data = $(this).closest('tr').find('#authoriseId').val();

            authoriseRevision(data);
        })


    });
});

function makeReadyRevision(id){
    var url='makeRevTransReady/'+id;
    $.ajax({
        type: 'GET',
        url: url,
    }).done(function (s) {
        $('#myPleaseWait').modal('hide');
        new PNotify({
            title: 'Success',
            text: 'Transaction Made Ready Successfully',
            type: 'success',
            styling: 'bootstrap3'
        });
        $("#clm_activities_tbl").DataTable().ajax.reload();
    }).fail(function (xhr, error) {
        $('#myPleaseWait').modal('hide');
        new PNotify({
            title: 'Error',
            text: xhr.responseText,
            type: 'error',
            styling: 'bootstrap3'
        });
    });
}
function undoMakeReadyTrans(id){
    var url='undoRevTransReady/'+id;
    $.ajax({
        type: 'GET',
        url: url,
    }).done(function (s) {
        $('#myPleaseWait').modal('hide');
        new PNotify({
            title: 'Success',
            text: 'Transaction Ready Undone Successfully',
            type: 'success',
            styling: 'bootstrap3'
        });
        $("#clm_activities_tbl").DataTable().ajax.reload();
    }).fail(function (xhr, error) {
        $('#myPleaseWait').modal('hide');
        new PNotify({
            title: 'Error',
            text: xhr.responseText,
            type: 'error',
            styling: 'bootstrap3'
        });
    });
}
function authoriseRevision(id){
    var url='authoriseRevision/'+id;
    bootbox.confirm("Are you sure want to authorise this Transaction?", function (result) {
        if (result) {
            $('#myPleaseWait').modal({
                backdrop: 'static',
                keyboard: true
            });
            $.ajax({
                type: 'GET',
                url: url,
            }).done(function (s) {
                $('#myPleaseWait').modal('hide');
                new PNotify({
                    title: 'Success',
                    text: 'Transaction Authorised Successfully',
                    type: 'success',
                    styling: 'bootstrap3'
                });
                $("#clm_activities_tbl").DataTable().ajax.reload();
                getClaimDetails();
            }).fail(function (xhr, error) {
                $('#myPleaseWait').modal('hide');
                new PNotify({
                    title: 'Error',
                    text: xhr.responseText,
                    type: 'error',
                    styling: 'bootstrap3'
                });
            });
        }
    })

}

function selectRiskPerils(riskId){
    if($("#peril-def").filter("div").html() != undefined)
    {
        Select2Builder.initAjaxSelect2({
            containerId : "peril-def",
            sort : 'perilDesc',
            change: function(e, a, v){
                $("#peril-code").val(e.added.bindPerilCode);
                $("#peril-name").val(e.added.perilDesc);
            },
            formatResult : function(a)
            {
                console.log(a);
                return a.perilDesc;
            },
            formatSelection : function(a)
            {
                return a.perilDesc;
            },
            initSelection: function (element, callback) {

            },
            id: "bindPerilCode",
            params: {riskId: riskId},
            placeholder:"Select Peril"
        });
    }
}
function saveClaimant(){
    $('#saveClaimantDef').click(function(){
        var $classForm = $('#claimants-form');
        var validator = $classForm.validate();
        if (!$classForm.valid()) {
            return;
        }
        var $btn = $(this).button('Saving');
        var data = {};
        $classForm.serializeArray().map(function(x){data[x.name] = x.value;});
        $('#myPleaseWait').modal({
            backdrop: 'static',
            keyboard: true
        });
        var url = "createClaimant";
        var request = $.post(url, data );
        request.success(function(){
            $('#myPleaseWait').modal('hide');
            new PNotify({
                title: 'Success',
                text: 'Record created Successfully',
                type: 'success',
                styling: 'bootstrap3'
            });
            $('#claimant-tbl').DataTable().ajax.reload();
            validator.resetForm();
            $('#claimants-form').find("input[type=text],input[type=number],input[type=hidden],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden], textarea,select").val("");
            $('#claimants-form').select2('val', null);
            $('#claimantsModal').modal('hide');
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
function addSelectedPeril(){
    $("#btn-add-peril-selected").on('click', function(){
        var $classForm = $('#new-peril-form');
        var validator = $classForm.validate();
        if($("#peril-code").val()==''){
            bootbox.alert("Select Peril");
            return;
        }
        if (!$classForm.valid()) {
            return;
        }
        var bindPeril = $("#peril-code").val();
        var riskId = $("#curr-risk-id").val();
        getExpiringSection(bindPeril, riskId);

        var $btn = $(this).button('Saving');
        var data = {};

        $classForm.serializeArray().map(function(x){data[x.name] = x.value;});
        $('#myPleaseWait').modal({
            backdrop: 'static',
            keyboard: true
        });
        var url = "createClaimantPeril";
        var request = $.post(url, data );
        request.success(function(){
            $('#myPleaseWait').modal('hide');
            new PNotify({
                title: 'Success',
                text: 'Record created Successfully',
                type: 'success',
                styling: 'bootstrap3'
            });
            $('#clm_claimant_tbl').DataTable().ajax.reload();
            validator.resetForm();
            $('#new-peril-form').find("input[type=text],input[type=number],input[type=hidden],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden], textarea,select").val("");
            $('#new-peril-form').select2('val', null);
            $('#perilTransModal').modal('hide');
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
function selectProvidersByTypeLov(){
    if($("#binder-provider-frm").filter("div").html() != undefined)
    {
        Select2Builder.initAjaxSelect2({
            containerId : "binder-provider-frm",
            sort : 'serviceProviders.name',
            change: function(e,a,v){
                $("#provider-code").val(e.added.spcId);
                $("#provider-desc").val(e.added.providerName);
            },
            formatResult : function(a)
            {
                return a.providerName
            },
            formatSelection : function(a)
            {
                return a.providerName
            },
            initSelection: function (element, callback) {
                var code = $("#provider-code").val();
                var name = $("#provider-desc").val();
                var data = {providerName:name,spcId:code};
                callback(data);
            },
            id: "spcId",
            params: {bindCode: $("#binder-code").val(),typeCode: $("#provider-type").val()},
            placeholder:"Select Provider Type"
        });
    }
}
function selectProviderTypesLov(){
    console.log("testing="+$("#binder-code").val());
    if($("#providerType-frm").filter("div").html() != undefined)
    {
        Select2Builder.initAjaxSelect2({
            containerId : "providerType-frm",
            sort : 'shtDesc',
            change: function(e,a,v){
                $("#provider-type").val(e.added.id);
                $("#provider-type-desc").val(e.added.desc);
                selectProvidersByTypeLov();
            },
            formatResult : function(a)
            {
                return a.desc
            },
            formatSelection : function(a)
            {
                return a.desc
            },
            initSelection: function (element, callback) {
                var code = $("#provider-type").val();
                var name = $("#provider-type-desc").val();
                var data = {desc:name,id:code};
                callback(data);
            },
            id: "id",
            params: {bindCode: $("#binder-code").val()},
            placeholder:"Select Provider Type"
        });
    }
}

function selectNextReviewLov(){
    if($("#review-user").filter("div").html() != undefined)
    {
        Select2Builder.initAjaxSelect2({
            containerId : "review-user",
            sort : 'username',
            change: function(e,a,v){
                $("#next-rev-user").val(e.added.id);
                $("#next-rev-user-desc").val(e.added.username);
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
                var code = $("#next-rev-user").val();
                var name = $("#next-rev-user-desc").val();
                var data = {username:name,id:code};
                callback(data);
            },
            id: "id",
            placeholder:"Select Next Review User"
        });
    }
}
function selectClaimantLov(){
    if($("#clmant-def").filter("div").html() != undefined)
    {
        Select2Builder.initAjaxSelect2({
            containerId : "clmant-def",
            sort : 'surname',
            change: function(e,a,v){
                $("#clmt-code").val(e.added.claimantId);
                $("#clmt-name").val(e.added.surname+" "+ e.added.otherNames);

            },
            formatResult : function(a)
            {
                return a.surname+" "+ a.otherNames;
            },
            formatSelection : function(a)
            {
                return a.surname+" "+ a.otherNames;
            },
            initSelection: function (element, callback) {

            },
            id: "claimantId",
            placeholder:"Select Claimant"
        });
    }
}

function addClaimant(){
    $("#add-peril-btn").on('click',function(){
        $('#new-peril-form').find("input[type=text],input[type=mobileNumber],input[type=hidden],input[type=emailFull],input[type=password],input[type=hidden],input[type=number], textarea").val("");
        $('#new-peril-form').find("input[type=checkbox]").attr("checked", false);
        selectClaimantLov();
        $("#clm-estimate").number( true, 2 );
        $('#perilTransModal').modal({
            backdrop : 'static',
            keyboard : true
        })
    });
}
function saveClaimActivity(){
    $("#btn-add-activity").click(function(){
        $('#clm-act-form').find("input[type=text] ,input[type=hidden],input[type=number],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden], textarea,select").val("");
        $("#clm-activity").val('');
        $(".activitydropdown").show();
        $(".activitydescrption").hide()
        $('#activityModal').modal({
            backdrop: 'static',
            keyboard: true
        })
    });
    $("#saveClmActivity").click(function(){
        var $currForm = $('#clm-act-form');
        var currValidator = $currForm.validate();
        if (!$currForm.valid()) {
            return;
        }
        var data = {};
        $currForm.serializeArray().map(function(x){data[x.name] = x.value;});
        var url = "createClmActivity";
        var request = $.post(url, data );
        request.success(function(){
            new PNotify({
                title: 'Success',
                text: 'Activity Created Successfully',
                type: 'success',
                styling: 'bootstrap3'
            });
            $('#clm_activities_tbl').DataTable().ajax.reload();
            $('#activityModal').modal('hide');
        });
        request.error(function(jqXHR, textStatus, errorThrown) {
            new PNotify({
                title: 'Error',
                text: jqXHR.responseText,
                type: 'error',
                styling: 'bootstrap3'
            });
        });
    })
}


function savePerilPayment(){
    $("#saveClmPayment").click(function(){
        var $currForm = $('#clm-pay-form');
        var currValidator = $currForm.validate();
        if($("#peril-code1").val()==''){
            bootbox.alert("Select Peril");
            return;
        }
        if (!$currForm.valid()) {
            return;
        }
        var data = {};
        $currForm.serializeArray().map(function(x){data[x.name] = x.value;});
        var url = "createPerilPayment";
        var request = $.post(url, data );
        request.success(function(){
            new PNotify({
                title: 'Success',
                text: 'Payment Created Successfully',
                type: 'success',
                styling: 'bootstrap3'
            });
            $('#clm_claimant_tbl').DataTable().ajax.reload();
            $('#clm_perils_tbl').DataTable().ajax.reload();
            $('#clm_perils_pymnt_tbl').DataTable().ajax.reload();

            $('#paymentModal').modal('hide');
        });
        request.error(function(jqXHR, textStatus, errorThrown) {
            new PNotify({
                title: 'Error',
                text: jqXHR.responseText,
                type: 'error',
                styling: 'bootstrap3'
            });
        });
    })
}



function saveClaimPeril(){
    $("#btn-add-peril").click(function(){
        $('#clm-peril-form').find("input[type=text] ,input[type=hidden],input[type=number],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden], textarea,select").val("");

        $("#reserve-amount").number( true, 2 );
        $('#claimPerilModal').modal({
            backdrop: 'static',
            keyboard: true
        })
    });
    $("#saveClmPeril").click(function(){
        var $currForm = $('#clm-peril-form');
        var currValidator = $currForm.validate();
        if (!$currForm.valid()) {
            return;
        }
        var data = {};
        $currForm.serializeArray().map(function(x){data[x.name] = x.value;});
        var url = "createClaimPeril";
        var request = $.post(url, data );
        request.success(function(){
            new PNotify({
                title: 'Success',
                text: 'Revision Created/Updated Successfully',
                type: 'success',
                styling: 'bootstrap3'
            });
            $('#clm_claimant_tbl').DataTable().ajax.reload();
            $('#clm_perils_tbl').DataTable().ajax.reload();
            $('#clm_perils_pymnt_tbl').DataTable().ajax.reload();
            $('#clm_activities_tbl').DataTable().ajax.reload();

            $('#claimPerilModal').modal('hide');
        });
        request.error(function(jqXHR, textStatus, errorThrown) {
            new PNotify({
                title: 'Error',
                text: jqXHR.responseText,
                type: 'error',
                styling: 'bootstrap3'
            });
        });
    })
}


function saveClaimStatus(){
    $("#btn-clm-status").click(function(){
        $('#clm-status-form').find("input[type=text] ,input[type=hidden],input[type=number],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden], textarea,select").val("");
        if ($("#clm-status1").val()==="O" || $("#clm-status1").val()==="B"){
            $("#closedStatus").show();
            $("#saveClmStatus").html('Close Claim') ;
            $("#new-status").val('C');

        }
        if ($("#clm-status1").val()==="R") {
            $("#closedStatus").show();
            $("#saveClmStatus").html('Close Claim') ;
            $("#new-status").val('C');
        }
        if ($("#clm-status1").val()==="C") {
            $("#closedStatus").hide();
            $("#saveClmStatus").html('Re-open Claim') ;
            $("#new-status").val('R');
        }
        $('#claimStatusModal').modal({
            backdrop: 'static',
            keyboard: true
        })
    });


    $("#saveClmStatus").click(function(){
        var $currForm = $('#clm-status-form');
        var currValidator = $currForm.validate();
        if (!$currForm.valid()) {
            return;
        }
        var closeReason = $('#clsStatus').val();

        var data = {};
        $currForm.serializeArray().map(function(x){data[x.name] = x.value;});
        console.log(data)
        var url = "createClmStatus";
        var request = $.post(url, data );

        request.success(function(){
            new PNotify({
                title: 'Success',
                text: 'Status Changed Successfully',
                type: 'success',
                styling: 'bootstrap3'
            });
            getClaimDetails();
            getClaimClaimants();
            getClmRequiredDocs();
           getClaimActivities();
            getClaimUploads();

            $('#claimStatusModal').modal('hide');
        });
        request.error(function(jqXHR, textStatus, errorThrown) {
            new PNotify({
                title: 'Error',
                text: jqXHR.responseText,
                type: 'error',
                styling: 'bootstrap3'
            });
        });
    })
}




function selectClmActivity(){
    if($("#clm-activity").filter("div").html() != undefined)
    {
        Select2Builder.initAjaxSelect2({
            containerId : "clm-activity",
            sort : 'activityDesc',
            change: function(e,a,v){
                $("#activity-code").val(e.added.caId);
                $("#activity-desc").val(e.added.activityDesc);
            },
            formatResult : function(a)
            {
                return a.activityDesc
            },
            formatSelection : function(a)
            {
                return a.activityDesc
            },
            initSelection: function (element, callback) {
                var code = $("#activity-code").val();
                var name = $("#activity-desc").val();
                var data = {activityDesc:name,caId:code};
                callback(data);
            },
            id: "caId",
            placeholder:"Select Activity"
        });
    }
}


function uploadClaimDocument(){
    var $form = $("#clm-doc-form");
    var validator = $form.validate();
    $('form#clm-doc-form')
        .submit( function( e ) {
            e.preventDefault();
            if (!$form.valid()) {
                return;
            }
            var data = new FormData( this );
            data.append( 'file', $( '#file' )[0].files[0] );
            $.ajax( {
                url: 'uploadClaimDocument',
                type: 'POST',
                data: data,
                processData: false,
                contentType: false,
                success: function (s ) {
                    new PNotify({
                        title: 'Success',
                        text: 'Record created/updated Successfully',
                        type: 'success',
                        styling: 'bootstrap3'
                    });
                    $('#clm_req_docs_tbl').DataTable().ajax.reload();
                    $('#clm-doc-form').find("input[type=text],input[type=hidden],input[type=mobileNumber],input[file],input[type=email],input[type=password],input[type=hidden],input[type=number], textarea,select").val("");
                    //$('#file').fileinput('reset');
                    $("#file").val('');
                    $('#uploadClmModal').modal('hide');

                },
                error: function(jqXHR, textStatus, errorThrown){
                    new PNotify({
                        title: 'Error',
                        text: jqXHR.responseText,
                        type: 'error',
                        styling: 'bootstrap3'
                    });
                }
            });
        });
}

function uploadClaimReqDocument(){
    $('form#clm-req-form')
        .submit( function( e ) {
            var $form = $("#clm-req-form");
            var validator = $form.validate();
            e.preventDefault();
            if (!$form.valid()) {
                return;
            }
            var data = new FormData( this );
            data.append( 'file', $( '#file' )[0].files[0] );
            $.ajax( {
                url: 'uploadClaimReqDocument',
                type: 'POST',
                data: data,
                processData: false,
                contentType: false,
                success: function (s ) {
                    new PNotify({
                        title: 'Success',
                        text: 'Record created/updated Successfully',
                        type: 'success',
                        styling: 'bootstrap3'
                    });
                    $('#clm_required_docs_tbl').DataTable().ajax.reload();
                    $('#clm-req-form').find("input[type=text],image,input[type=hidden],input[type=mobileNumber],input[file],input[type=email],input[type=password],input[type=hidden],input[type=number], textarea,select").val("");
                    //$('#file').fileinput('reset');
                    $("#file").val('');
                    $('#uploadClmReqModal').modal('hide');

                },
                error: function(jqXHR, textStatus, errorThrown){
                    new PNotify({
                        title: 'Error',
                        text: jqXHR.responseText,
                        type: 'error',
                        styling: 'bootstrap3'
                    });
                }
            });
        });
}

function uploadDoc(){
    $("#btn-add-clmdocs").on('click', function(){
        searchReqDocs("");
    });

    $("#searchDocuments").on('click', function (){
        console.log('here...');
        $.ajax({
            type: 'GET',
            url:  'getClmReqDocs',
            dataType: 'json',
            data: {"docName":$("#doc-name-search").val()},
            async: true,
            success: function(result) {
                console.log(result);
                $("#clmReqDocsModal tbody").each(function(){$(this).remove();});
                for(var res in result){
                    var markup = "<tr><td><input type='checkbox' name='record' " +
                        "id='"+result[res].sclReqrdId+"'></td><td>" + result[res].reqShtDesc + "</td><td>"
                        + result[res].reqDesc + "</td></tr>";
                    $("#risksReqDocsTbl").append(markup);
                }
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
    })


    $("#btn-add-upload").on('click', function(){
        if(typeof clmId!== 'undefined') {
            if (clmId !== -2000) {
                $("#upload-clm-id").val(clmId);
                $('#uploadClmModal').modal({
                    backdrop: 'static',
                    keyboard: true
                });
            }
        }

    });

}


function uploadReqDoc(button) {

    var requiredDocs = JSON.parse(decodeURI($(button).data("reqdocs")));
    $("#uploadreq-code").val(requiredDocs["clmRequiredId"]);
    $("#trans-type").val("N");
    $(".uploadfile").show();
    $('#uploadClmReqModal').modal({
        backdrop: 'static',
        keyboard: true
    });
}

function editReqDoc(button) {

    var requiredDocs = JSON.parse(decodeURI($(button).data("reqdocs")));
    $("#uploadreq-code").val(requiredDocs["clmRequiredId"]);
    $("#trans-type").val("E");
    $(".uploadfile").hide();
    $('#uploadClmReqModal').modal({
        backdrop: 'static',
        keyboard: true
    });
}



function editClaimActivity(button) {
    var act = JSON.parse(decodeURI($(button).data("activity")));
    $("#activity-pk").val(act["activityId"]);
    $("#activity-code").val(act["activity"].caId);
    $("#activity-desc").val(act["activity"].activityDesc);
    $("#activity-descrption").val(act["activity"].activityDesc);


    $("#net-review-date").val(moment(act["remDate"]).format('DD/MM/YYYY'))
    $("#next-rev-user").val(act["reviewUser"].id);
    $("#next-rev-user-desc").val(act["reviewUser"].username);
    if (act["serviceProvider"]) {
        $("#provider-type").val(act["serviceProvider"].serviceProviders.serviceProviderTypes.id);
        $("#provider-type-desc").val(act["serviceProvider"].serviceProviders.serviceProviderTypes.desc);
        $("#provider-code").val(act["serviceProvider"].spcId);
        $("#provider-desc").val(act["serviceProvider"].serviceProviders.name);
    }
    selectNextReviewLov();
    selectClmActivity();
    selectProviderTypesLov();
    selectProvidersByTypeLov();
    $(".activitydropdown").hide();
    $(".activitydescrption").show();

    $("#clm-activity").attr('disabled',true);
    $('#activityModal').modal({
        backdrop: 'static',
        keyboard: true
    })
}



function getClaimUploads(){
    var url = "getClaimUploads";
    var currTable = $('#clm_req_docs_tbl').DataTable( {
        "processing": true,
        "serverSide": true,
        autoWidth: true,
        searching: false,
        "deferRender": true,
        "ajax": {
            'url': url,
        },
        lengthMenu: [ [5], [5] ],
        pageLength: 5,
        destroy: true,
        "columns": [
            { "data": "fileId"
            },
            { "data": "fileName"
            },

            { "data": "dateUploaded",
                "render": function ( data, type, full, meta ) {
                    return moment(full.dateUploaded).format('DD/MM/YYYY');
                }
            },
            { "data": "uploadedBy",
                "render": function ( data, type, full, meta ) {

                    return full.uploadedBy;
                }
            },

            { "data": "uploadedComment"
            },
            {
                "data": "uploadId",
                "render": function ( data, type, full, meta ) {

                    return '<button type="button" class="btn btn-success btn btn-info btn-xs" type="button" data-docs=' + encodeURI(JSON.stringify(full)) + ' onclick="downloadRiskDoc(this);"><i class="fa fa-file-archive-o"></button>';

                }

            },
            {
                "data": "uploadId",
                "render": function ( data, type, full, meta ) {
                    if(full.claimStatus==='C')
                        return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-docs='+encodeURI(JSON.stringify(full)) + ' onclick="deleteClmUploadDoc(this);" disabled><i class="fa fa-trash-o"></button>';
                    else
                        return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-docs='+encodeURI(JSON.stringify(full)) + ' onclick="deleteClmUploadDoc(this);"><i class="fa fa-trash-o"></button>';

                }

            },
        ]
    } );
    return currTable;
}

function downloadRiskDoc(button){
    var docs = JSON.parse(decodeURI($(button).data("docs")));
    window.open(SERVLET_CONTEXT+"/protected/claims/clmdocument/"+docs['uploadId'],
        '_blank'
    );
}


function downloadClmReqDoc(button){
    var docs = JSON.parse(decodeURI($(button).data("reqdocs")));
    window.open(SERVLET_CONTEXT+"/protected/claims/clmreqdocument/"+docs['clmRequiredId'],
        '_blank'
    );
}



function getClaimActivities(){
    var url = "getClaimActivities";
    var currTable = $('#clm_activities_tbl').DataTable( {
        "processing": true,
        "serverSide": true,
        autoWidth: true,
        searching: false,
        "deferRender": true,
        "ajax": {
            'url': url,
        },
        lengthMenu: [ [10], [10] ],
        pageLength: 10,
        destroy: true,
        "columns": [
            { "data": "activityId",
                "render": function ( data, type, full, meta ) {
                    return full.activityDesc
                }
            },
            { "data": "userCreated",
                "render": function ( data, type, full, meta ) {

                    return full.username;
                }
            },
            { "data": "activityDate",
                "render": function ( data, type, full, meta ) {
                    return moment(full.activityDate).format('DD/MM/YYYY');
                }
            },
            { "data": "currentActivity",
                "render": function ( data, type, full, meta ) {
                    if(full.currentActivity){
                        if(full.currentActivity === "Y"){
                            return "Yes"
                        }
                        else return "No";
                    }
                    else
                        return "No";
                }
            },

            { "data": "remDate",
                "render": function ( data, type, full, meta ) {
                    return moment(full.remDate).format('DD/MM/YYYY');
                }
            }
        ]
    } );
    return currTable;
}



function getClaimStatuses(){
    var url = "getClaimStatuses";
    var currTable = $('#clm_status_tbl').DataTable( {
        "processing": true,
        "serverSide": true,
        autoWidth: true,
        searching: false,
        "order": [[ 3, "desc" ],[ 2, "desc" ]],
        "deferRender": true,
        "ajax": {
            'url': url,
        },
        lengthMenu: [ [5], [5] ],
        pageLength: 5,
        destroy: true,
        "columns": [
            { "data": "clmStatusId",
                "render": function ( data, type, full, meta ) {
                    console.log(full);
                    return full.currentStatus ==='O'?'Open':full.currentStatus ==='C'?'Closed':full.currentStatus ==='R'?'Re-Opened':'';
                }
            },
            { "data": "capturedBy",
                "render": function ( data, type, full, meta ) {

                    return full.capturedBy.username;
                }
            },
            { "data": "dateCaptured",
                "render": function ( data, type, full, meta ) {
                    return moment(full.dateCaptured).format('DD/MM/YYYY');
                }
            },
            { "data": "currentActivity",
                "render": function ( data, type, full, meta ) {
                    if(full.currentActivity){
                        if(full.currentActivity === "Y"){
                            return "Yes"
                        }
                        else return "No";
                    }
                    else
                        return "No";
                }
            },
            { "data": "remarks"
            },

        ]
    } );
    return currTable;
}


function getClmRequiredDocs() {
    var url = "getClmRequiredDocs";
    var currTable = $('#clm_required_docs_tbl').DataTable({
        "processing": true,
        "serverSide": true,
        autoWidth: true,
        searching: false,
        "deferRender": true,
        "ajax": {
            'url': url,
        },
        lengthMenu: [[5], [5]],
        pageLength: 5,
        destroy: true,
        "columns": [
            {
                "data": "clmRequiredId",
                "render": function (data, type, full, meta) {
                    return full.docName
                }
            },
            {
                "data": "docRefNo"
            },
            {
                "data": "fileName"
            },
            {
                "data": "dateReceived",
                "render": function (data, type, full, meta) {
                    if (full.dateReceived) {
                        return moment(full.dateReceived).format('DD/MM/YYYY');
                    } else return "";

                }
            },
            {
                "data": "userReceived",
                "render": function (data, type, full, meta) {
                    if (full.username)
                        return full.username;
                    else return "";
                }
            },
            {
                "data": "remarks"
            },
            {
                "data": "clmRequiredId",
                "render": function (data, type, full, meta) {
                    if (full.claimStatus === 'C')
                        return '<button type="button" class="btn btn-success  btn-xs" disabled><i class="fa fa-file-archive-o"></button>';
                    else
                        return '<button type="button" class="btn btn-success btn-xs " data-reqdocs=' + encodeURI(JSON.stringify(full)) + ' onclick="uploadReqDoc(this);" data-toggle="tooltip" data-placement="top" title="Upload Document"><i class="fa fa-file-archive-o"></button>';
                }

            },
            {
                "data": "clmRequiredId",
                "render": function (data, type, full, meta) {

                    return '<button type="button" class="btn btn-info btn-xs" data-reqdocs=' + encodeURI(JSON.stringify(full)) + ' onclick="downloadClmReqDoc(this);" data-toggle="tooltip" data-placement="top" title="View Upload Document"><i class="fa fa-file-archive-o"></button>';
                }

            },
            {
                "data": "clmRequiredId",
                "render": function (data, type, full, meta) {
                    if (full.claimStatus === 'C')
                        return '<button type="button" class="btn btn-success btn-xs"  disabled><i class="fa fa-pencil-square-o"></button>';
                    else
                        return '<button type="button" class="btn btn-success btn-xs"  data-reqdocs=' + encodeURI(JSON.stringify(full)) + ' onclick="editReqDoc(this);" data-toggle="tooltip" data-placement="top" title="Edit Document Details"><i class="fa fa-pencil-square-o"></button>';
                }

            },
            {
                "data": "clmRequiredId",
                "render": function (data, type, full, meta) {
                    if (full.claimStatus === 'C')
                        return '<button type="button" class="btn btn-danger btn-xs" disabled><i class="fa fa-trash-o"></button>';
                    else
                        return '<button type="button" class="btn btn-danger btn-xs" data-reqdocs=' + encodeURI(JSON.stringify(full)) + ' onclick="deleteClmReqDoc(this);" data-toggle="tooltip" data-placement="top" title="Delete Document"><i class="fa fa-trash-o"></button>';
                }

            }
        ]
    });
    return currTable;
}


function getPerilsPayment(claimantId){
    var url = "getClaimPayments/"+claimantId;
    var currTable = $('#clm_perils_pymnt_tbl').DataTable( {
        "processing": true,
        "serverSide": true,
        searching: false,
        "autoWidth": false,
        "deferRender": true,
        "ajax": {
            'url': url
        },
        lengthMenu: [ [5], [5] ],
        pageLength: 5,
        destroy: false,
        "columns": [
            {  "data": "payee",
                "width": "20%"
            },
            { "data": "reference",
                "render": function ( data, type, full, meta ) {
                    return full.reference;
                }
            },
            { "data": "paymentMode",
                "render": function ( data, type, full, meta ) {
                    return full.paymentMode;
                }
            },
            {   "data": "transType",
                "width": "22%",
                "render": function ( data, type, full, meta ) {
                   if(full.transType && full.transType==='CP'){
                       return "Claim Payment";
                   }
                   else  if(full.transType && full.transType==='SP'){
                       return "Service Provider Payment";
                   }
                }
            },
            { "data": "currency",
                "width": "15%",
                "render": function ( data, type, full, meta ) {
                    return full.currency;
                }
            },
            { "data": "amount",
                "render": function ( data, type, full, meta ) {

                    return UTILITIES.currencyFormat(full.amount);
                }
            },
            {   "data": "status",
                "width": "20%",
                "render": function ( data, type, full, meta ) {
                    if(full.status && full.status==='N'){
                        return "Not Authorised";
                    }
                    else  if(full.status && full.status==='R'){
                        return "Ready";
                    }
                    else  if(full.status && full.status==='Y'){
                        return "Authorised";
                    }
                }
            },
            { "data": "raisedBy",
                "render": function ( data, type, full, meta ) {
                    return full.raisedBy;
                }
            },
            { "data": "raisedDate",
                "render": function ( data, type, full, meta ) {
                    return moment(full.raisedDate).format('DD/MM/YYYY');
                }
            },
            { "data": "authDate",
                "render": function ( data, type, full, meta ) {
                if(full.authDate)
                    return moment(full.authDate).format('DD/MM/YYYY');
                else return "";
                }
            },
            { "data": "authBy",
                "render": function ( data, type, full, meta ) {
                    return full.authBy;
                }
            },
        ]
    } );
    return currTable;
}

function getClaimPerils(){
    var url = "getClaimPerils";
    var currTable = $('#clm_perils_tbl').DataTable( {
        "processing": true,
        "serverSide": true,
        autoWidth: true,
        searching: false,
        "deferRender": true,
        "ajax": {
            'url': url
        },
        lengthMenu: [ [5], [5] ],
        pageLength: 5,
        destroy: true,
        "columns": [
            { "data": "clmPerilId",
                "render": function ( data, type, full, meta ) {
                    return full.perilDesc;

                }
            },
            { "data": "type",
                "render": function ( data, type, full, meta ) {
                    if(full.type ==="RS"){
                        return "Risk Sum Insured";
                    }
                    else if(full.type ==="SS"){
                        return "Section SI/Limit";
                    }
                    else if(full.type ==="PL"){
                        return "Peril Limit";
                    }
                    else if(full.type ==="UL"){
                        return "Unlimited";
                    }
                    else if(full.type ==="EX"){
                        return "Extension";
                    }
                    else if(full.type ==="GT"){
                        return "GPA Total/Temp Disability";
                    }
                    else if(full.type ==="PD"){
                        return "Permanent Disability";
                    }
                    else if(full.type ==="WT"){
                        return "Workmen Total/Temp Disability";
                    }
                    else
                        return full.type;
                }
            },
            { "data": "limitAmt",
                "render": function ( data, type, full, meta ) {

                    return UTILITIES.currencyFormat(full.limitAmt);
                }
            },
            { "data": "excessAmt",
                "render": function ( data, type, full, meta ) {

                    return UTILITIES.currencyFormat(full.excessAmt);
                }
            },{ "data": "reserve",
                "render": function ( data, type, full, meta ) {

                    return UTILITIES.currencyFormat(full.reserve);
                }
            },
            { "data": "remarks",
                "render": function ( data, type, full, meta ) {
                    return full.remarks;
                }
            },
            {
                "data": "clmPerilId",
                "render": function ( data, type, full, meta ) {
                    if($("#clm-status").val()==='Closed')
                        return '<button type="button" class="btn btn-success btn btn-info btn-xs" type="button" data-perils='+encodeURI(JSON.stringify(full)) + ' onclick="editClaimPeril(this);" disabled><i class="fa fa-pencil-square-o"></button>';
                    else
                        return '<button type="button" class="btn btn-success btn btn-info btn-xs" type="button" data-perils='+encodeURI(JSON.stringify(full)) + ' onclick="editClaimPeril(this);"><i class="fa fa-pencil-square-o"></button>';
                }

            },
        ]
    } );

    $('#clm_perils_tbl tbody').on( 'click', 'tr', function () {
        $(this).addClass('active').siblings().removeClass('active');
        var aData = currTable.rows('.active').data();
        if (aData[0] === undefined || aData[0] === null) {
        }
        else{
            console.log("aData[0].clmPerilId"+aData[0].clmPerilId);
            $("#peril-code1").val(aData[0].clmPerilId);
            getPerilsPayment(aData[0].clmPerilId);
        }
    } );
    return currTable;
}

function editPerilPayment(button){
    var payment = JSON.parse(decodeURI($(button).data("payment")));
    $("#payment-id").val(payment["clmPymntId"]);
    $("#peril-pk").val(payment["claimPerils"].clmPerilId);
    $("#payee-name").val(payment["payee"]);
    $("#pay-ref").val(payment["pymntRef"]);
    $("#payment-type").val(payment["pymntType"]);
    $("#pay-date").val(moment(payment["pymntDate"]).format('DD/MM/YYYY'));
    $("#paid-amount").number( true, 2 );
    $("#paid-amount").val(payment["clmPymntAmount"]);
    $('#paymentModal').modal({
        backdrop: 'static',
        keyboard: true
    })
}

function editClaimPeril(button){
    var perils = JSON.parse(decodeURI($(button).data("perils")));
    $("#peril-id").val(perils["clmPerilId"]);
    $("#peril-desc").val(perils["perilDesc"]);
    $("#remarks-desc").val(perils["remarks"]);
    $("#reserve-amount").number( true, 2 ).val(perils["reserve"]);
    $('#claimPerilModal').modal({
        backdrop: 'static',
        keyboard: true
    })
}


function getClaimStatusHistory(){
    getClaimStatuses();
    $('#claimStatusHistModal').modal({
        backdrop: 'static',
        keyboard: true
    })
}

function getClaimClaimants(){
    var url = "getClmClaimants";
    var currTable = $('#clm_claimant_tbl').DataTable( {
        "processing": true,
        "serverSide": true,
        autoWidth: true,
        searching: false,
        "deferRender": true,
        "ajax": {
            'url': url,
        },
        lengthMenu: [ [5], [5] ],
        pageLength: 5,
        destroy: true,
        scrollCollapse: true,
        "columns": [
            { "data": "claimantId",
                "render": function ( data, type, full, meta ) {
                    if(full.thirdParty==="T") {
                        return full.tpClaimant;
                    }
                    else  if(full.thirdParty==="S")
                        return "Service Provider";
                    else{
                        return full.selfClaimant;
                    }
                }
            },
            { "data": "claimantId",
                "render": function ( data, type, full, meta ) {
                    if(full.thirdParty==="T")
                        return "Third Party";
                    else  if(full.thirdParty==="S")
                        return "Service Provider";
                    else
                        return "Self";
                }
            },
            { "data": "claimantId",
                "render": function ( data, type, full, meta ) {
                    return full.peril;

                }
            },
            { "data": "limitAmt",
                "render": function ( data, type, full, meta ) {

                    return UTILITIES.currencyFormat(full.limitAmt);
                }
            },
            { "data": "claimantId",
                "render": function ( data, type, full, meta ) {
                if(full.createdDate)
                    return moment(full.createdDate).format('DD/MM/YYYY');
                }
            },
            { "data": "claimantId",
                "render": function ( data, type, full, meta ) {
                        return full.createdBy;
                }
            },
            {
                "data": "claimantId",
                "render": function ( data, type, full, meta ) {
                    if($("#clm-status").val()==='Closed')
                        return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-claimants='+encodeURI(JSON.stringify(full)) + ' onclick="deleteClaimants(this);" disabled><i class="fa fa-trash-o"></button>';
                    else
                        return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-claimants='+encodeURI(JSON.stringify(full)) + ' onclick="deleteClaimants(this);"><i class="fa fa-trash-o"></button>';
                }

            },
        ]
    } );

    $('#clm_claimant_tbl tbody').on( 'click', 'tr', function () {
        $(this).addClass('active').siblings().removeClass('active');
        var aData = currTable.rows('.active').data();
        if (aData[0] === undefined || aData[0] === null){

        }
        else{
            $('#clm_perils_pymnt_tbl').DataTable().ajax.url( "getClaimPayments/"+aData[0].claimantId ).load();
        }
    } );
    return currTable;
}

function deleteClaimants(button){
    var claimants = JSON.parse(decodeURI($(button).data("claimants")));
    var claimantName;
    if(claimants["claimant"]){
        claimantName=claimants["claimant"].otherNames+' '+claimants["claimant"].surname;
    }else {
        claimantName=claimants["client"].fname+' '+ claimants["client"].otherNames;
    }
    bootbox.confirm("Are you sure want to delete "+claimantName+"?", function(result) {
        if(result){
            $.ajax({
                type: 'GET',
                url:  'deleteClaimClaimant/' + claimants["claimantId"],
                dataType: 'json',
                async: true,
                success: function(result) {
                    new PNotify({
                        title: 'Success',
                        text: 'Record Deleted Successfully',
                        type: 'success',
                        styling: 'bootstrap3'
                    });
                    $('#clm_claimant_tbl').DataTable().ajax.reload();
                   // $('#clm_perils_tbl').DataTable().ajax.reload();
                    //$('#clm_perils_pymnt_tbl').DataTable().ajax.reload();
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

function deleteClaimantPeril(button){
    var perils = JSON.parse(decodeURI($(button).data("perils")));

    bootbox.confirm("Are you sure want to delete "+perils["perilsDef"].perilDesc+"?", function(result) {
        if(result){
            $.ajax({
                type: 'GET',
                url:  'deleteClaimantPeril/' + perils["clmPerilId"],
                dataType: 'json',
                async: true,
                success: function(result) {
                    new PNotify({
                        title: 'Success',
                        text: 'Record Deleted Successfully',
                        type: 'success',
                        styling: 'bootstrap3'
                    });
                    $('#clm_claimant_tbl').DataTable().ajax.reload();
                    $('#clm_perils_tbl').DataTable().ajax.reload();
                    $('#clm_perils_pymnt_tbl').DataTable().ajax.reload();
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

function deletePerilPayment(button){
    var payment = JSON.parse(decodeURI($(button).data("payment")));

    bootbox.confirm("Are you sure want to delete payment for "+payment["claimPerils"].perilsDef.perilDesc+"?", function(result) {
        if(result){
            $.ajax({
                type: 'GET',
                url:  'deletePerilPayment/' + payment["clmPymntId"],
                dataType: 'json',
                async: true,
                success: function(result) {
                    new PNotify({
                        title: 'Success',
                        text: 'Record Deleted Successfully',
                        type: 'success',
                        styling: 'bootstrap3'
                    });
                    $('#clm_claimant_tbl').DataTable().ajax.reload();
                    $('#clm_perils_tbl').DataTable().ajax.reload();
                    $('#clm_perils_pymnt_tbl').DataTable().ajax.reload();
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

function deleteClmReqDoc(button){
    var reqdocs = JSON.parse(decodeURI($(button).data("reqdocs")));

    bootbox.confirm("Are you sure want to delete "+reqdocs["requiredDoc"].reqDesc+"?", function(result) {
        if(result){
            $.ajax({
                type: 'GET',
                url:  'deleteClmReqDoc/' + reqdocs["clmRequiredId"],
                dataType: 'json',
                async: true,
                success: function(result) {
                    new PNotify({
                        title: 'Success',
                        text: 'Document Deleted Successfully',
                        type: 'success',
                        styling: 'bootstrap3'
                    });
                    $('#clm_required_docs_tbl').DataTable().ajax.reload();
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

function deleteClmUploadDoc(button){
    var docs = JSON.parse(decodeURI($(button).data("docs")));

    bootbox.confirm("Are you sure want to delete "+docs["fileName"]+"?", function(result) {
        if(result){
            $.ajax({
                type: 'GET',
                url:  'deleteClmUploadDoc/' + docs["uploadId"],
                dataType: 'json',
                async: true,
                success: function(result) {
                    new PNotify({
                        title: 'Success',
                        text: 'Document Deleted Successfully',
                        type: 'success',
                        styling: 'bootstrap3'
                    });
                    $('#clm_req_docs_tbl').DataTable().ajax.reload();
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

function selectOccupationLov(){
    if($("#occup-def").filter("div").html() != undefined)
    {
        Select2Builder.initAjaxSelect2({
            containerId : "occup-def",
            sort : 'name',
            change: function(e, a, v){
                $("#clmt-occupt").val(e.added.code);
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
                var code = $("#clmt-occupt").val();
                var name = $("#clmt-occupt-names").val();
//	                model.accounts.branch.brnCode = code;
                var data = {name:name,code:code};
                callback(data);
            },
            id: "code",
            placeholder:"Select Occupation"
        });
    }
}
function addClaimants(){
    $("#btn-add-claimant").click(function(){
        $('#claimants-form').find("input[type=text],input[type=hidden],input[type=number],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden], textarea,select").val("");
        $('#claimants-form').select2('val', null);
        selectOccupationLov();
        $('#claimantsModal').modal({
            backdrop: 'static',
            keyboard: true
        })
    });
}


function getClaimDetails() {
    if (typeof clmId !== 'undefined') {
        if (clmId !== -2000) {
            $.ajax( {
                url: 'getClaimDetails/'+clmId,
                type: 'GET',
                processData: false,
                contentType: false,
                async: true,
                success: function (s ) {
                    $("#myRiskId").val(s.clmRiskId);
                    $("#claim-no").text(s.claimNo);
                    $("#client-name").text(s.client);
                    $("#loss-desc").text(s.lossDesc);
                    if (s.claimStatus){
                        $("#clm-status1").val(s.claimStatus);
                        if (s.claimStatus==="O" || s.claimStatus==="B"){
                            $("#clm-status").text("Open");
                            $("#btn-clm-status").prop('value','Close Claim') ;
                            $("#add-peril-btn").show();
                            $("#btn-add-payment").show();
                            $("#btn-add-upload").show();
                            $("#btn-add-clmdocs").show();
                            $("#btn-add-activity").show();
                        }
                        if (s.claimStatus==="R") {
                            $("#clm-status").text("Re-Open");
                            $("#btn-clm-status").prop('value','Close Claim') ;
                            $("#add-peril-btn").show();
                            $("#btn-add-payment").show();
                            $("#btn-add-upload").show();
                            $("#btn-add-clmdocs").show();
                            $("#btn-add-activity").show();
                        }
                        if (s.claimStatus==="C") {
                            $("#clm-status").text("Closed");
                            $("#btn-clm-status").prop('value','Re-open Claim') ;
                            $("#add-peril-btn").hide();
                            $("#btn-add-payment").hide();
                            $("#btn-add-upload").hide();
                            $("#btn-add-clmdocs").hide();
                            $("#btn-add-activity").hide();

                        }
                    }

                    $("#loss-date").text(moment(s.lossDate).format('DD/MM/YYYY'));
                    $("#notific-date").text(moment(s.notificationDate).format('DD/MM/YYYY'));
                    $("#bkd-date").text(moment(s.bookedDate).format('DD/MM/YYYY'));
                    $("#next-rev-date").text(moment(s.nextRvwDate).format('DD/MM/YYYY'));
                   // $("#insurer-date").text(moment(s.insurerDate).format('DD/MM/YYYY'));
                    $("#liab-admission").text((s.liabilityAdmission)?'Yes':'No');
                    $("#causation-desc").text(s.causation);
                    // $("#insurance-co").text(s.risk.policy.agent.name);
                    // $("#insurer-pol-no").text(s.risk.policy.clientPolNo);
                    $("#pol-no").text(s.policyNo);
                    $("#insured").text(s.insured);
                    $("#product").text(s.product);
                    $("#risk-id").text(s.riskId);
                    $("#curr-risk-id").val(s.clmRiskId);

                    $('#risk-value').text(UTILITIES.currencyFormat(s.riskValue));
                    $('#reserve-total').text(UTILITIES.currencyFormat(s.totalReserve));
                    $('#payments-total').text(UTILITIES.currencyFormat(s.totalPayments));
                    $('#outstanding-total').text(UTILITIES.currencyFormat(s.ostReserve));
                    $("#risk-wef").text(moment(s.riskWef).format('DD/MM/YYYY'));
                    $("#risk-wet").text(moment(s.riskWet).format('DD/MM/YYYY'));
                    selectRiskPerils(s.clmRiskId);

                    if(s.riskBindId){
                        $("#binder-code").val(s.riskBindId);
                    } else {
                        $("#binder-code").val(s.policyBindId);
                    }

                },
                error: function(xhr, error){
                    bootbox.alert(xhr.responseText);
                }
            });

        }
    }
}
var searchReqDocs= function(search){
    $.ajax({
        type: 'GET',
        url:  'getClmReqDocs',
        dataType: 'json',
        data: {"docName":search},
        async: true,
        success: function(result) {
            $("#clmReqDocsModal tbody").each(function(){$(this).remove();});
            for(var res in result){
                var markup = "<tr><td><input type='checkbox' name='record' " +
                    "id='"+result[res].sclReqrdId+"'></td><td>" + result[res].reqShtDesc + "</td><td>"
                    + result[res].reqDesc + "</td></tr>";
                $("#risksReqDocsTbl").append(markup);
            }
            $("#req-risk-code").val($("#risk-code-pk").val());
            $('#clmReqDocsModal').modal({
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

var saveRiskDocsList= function(){
    var arr = [];
    $("#saveReqClmDocsBtn").click(function(){
        $("#risksReqDocsTbl tbody").find('input[name="record"]').each(function(){
            if($(this).is(":checked")){
                arr.push($(this).attr("id"));
            }
        });
        if(arr.length==0){
            bootbox.alert("No Documents Selected to attach..");
            return;
        }

        var $currForm = $('#req-clm-docs-form');
        var currValidator = $currForm.validate();
        if (!$currForm.valid()) {
            return;
        }

        var data = {};
        $currForm.serializeArray().map(function(x) { data[x.name] = x.value;});
        var url = "createClaimReqDocs";
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
                $('#clm_required_docs_tbl').DataTable().ajax.reload();
                $('#clmReqDocsModal').modal('hide');
                arr = [];
            },
            error : function(jqXHR, textStatus, errorThrown) {
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

function getExpiringSection(peril,risk) {
    $.ajax({
        type: 'GET',
        url: 'getExpireRiskSection/' + peril + '/' + risk,
        processData: false,
        contentType: false,
        async: false,
        success: function (result) {
            for (var res in result) {
                $("#expire-section-id").val(result[res].sectId);
                $("#expire-section").val(result[res].section);
            }
        },
        error: function (xhr, error) {
            bootbox.alert(xhr.responseText);
        }
    });
}