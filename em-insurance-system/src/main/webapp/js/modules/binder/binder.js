var UTILITIES = UTILITIES || {};

$(function(){
    $(document).ready(function() {

        createProductForSel();
        createAccountsForSel();
        createProdSubclassSel();
        createCoverTypesSel();
        createCoverSections();
        createCurrencyLov();
        addBinder();
        addBinderDetails();
        addBinderPremRates();
        saveBinderDetails();
        addExclusions();
        getBinderClausesModal();
        searchExclusions();
        saveBinderClauses();
        addCommRates();
        addLifeCommRates();
        createRevenueItemsForSel();
        getBinderDetPerilModal();
        SaveBinderSectPerils();
        uploadRatesFile();
        uploadSubLimits();
        addMedicalCovers();
        addBinderCardTypes();
        addBinderRules();
        saveBinderLoadings();
        saveBinderExclusions();
        saveBinderProviders();
        //addProviders();
        addCoverLimits();
        saveBinderCoverSummary();
        updateBinderClauses();
        updateBinderLoadings();
        addSubRequiredDocs();
        saveBinderReqDocs();
        updateBinderReqdDocs();
        uploadPremRatesFile();
        addProviderContracts();
        createProvidersContracts();
        updateBinderExclusions();
        cloneNewBinder();
        addSectLimits();
        saveSectLimits();
        updateSectLimits();
        createCardTypesSel();

        $('#binderList tbody').on( 'click', 'tr', function () {
            var binderdata = table.row($(this).closest('tr')).data();
            var binderId = binderdata.policyId;
            deactivateNew(binderId);

        })

        $("#binderList").on('click', '.btn-ready', function(){
            $('#myPleaseWait').modal({
                backdrop: 'static',
                keyboard: true
            });
            var data = $(this).closest('tr').find('#readyId').val();
            console.log(data);

            makeBinReady(data);

        })
        $("#binderList").on('click', '.btn-undo', function(){
            $('#myPleaseWait').modal({
                backdrop: 'static',
                keyboard: true
            });
            var data = $(this).closest('tr').find('#undoreadyId').val();
            console.log(data);

            makeBinUndo(data);
        })
        $("#binderList").on('click', '.btn-authorise', function(){

            var data = $(this).closest('tr').find('#authoriseId').val();
            console.log(data);

            makeBinAuthorise(data);
        })
        $("#binderList").on('click', '.btn-unauthorise', function(){

            var data = $(this).closest('tr').find('#unauthoriseId').val();
            console.log(data);
            makeBinUnAuthorise(data);
        })
        $(".formatcurrency").number( true, 2 );
        $(".datepicker-input").each(function() {
            $(this).datetimepicker({
                format: 'DD/MM/YYYY'
            });

        });
        $("#bind-min-prem").number( true, 2 );
        $('#range-from, #prem-free-limit,#prem-min-prem,#min-endo-prem,#med-min-prem,#med-limit-amt,#cov-limit-amt').number( true, 2 );
        $('#range-to').number( true, 2 );
        $("#prem-rate").number( true, 3 );
        $("#chk-rates-appli").change(function() {
            if(this.checked) {
                $("#range-from").removeAttr('disabled');
                $("#range-to").removeAttr('disabled');
                $("#prem-range-type").removeAttr('disabled');

            }
            else{
                $("#range-from").val("0");
                $("#range-to").val("0");
                $("#range-from").prop("disabled", true);
                $("#range-to").prop("disabled", true);
                $("#prem-range-type").prop("disabled", true);
            }
        });

        $("#exclusion-chronic").change(function() {
            if(this.checked) {
                $("#exclusion-chronic-id").val("Y");
            }
            else{
                $("#exclusion-chronic-id").val("N");
            }
        });

        $('#medical-main-cov').on('change', function() {
            $("#main-cover-modal").select2("val", "");
            $("#main-med-sect-id").val("");
            if(this.value==="Y"){
                $(".parent-cover").hide();
                $("#main-med-sect-id").val("");
                $("#main-cover-modal").select2("enable", false);
            }
            else if(this.value ==="N"){
                $(".parent-cover").show();
                $("#main-cover-modal").select2("enable", true);
            }
        });
        $('#med-depends-gender').change(function() {
            if($(this).is(':checked')){
                $(".gender-select").show();
            }
            else{
                $(".gender-select").hide();
            }
        });
        /* $('#exclusion-type').on('change', function() {
            createExclusions(this.value);
        });*/

    });
});


function uploadRatesFile(){
    var $form = $("#rate-upload-form");
    var validator = $form.validate();
    $('form#rate-upload-form')
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
            data.append( 'file', $( '#avatar' )[0].files[0] );
            $.ajax( {
                url: 'createRatedTbl',
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
                    $('#rate-upload-form').find("input[type=text],input[type=mobileNumber],input[file],input[type=email],input[type=password],input[type=hidden],input[type=number], textarea,select").val("");
                    var $el = $('#avatar');
                    $el.wrap('<form>').closest('form').get(0).reset();
                    $el.unwrap();
                    $('#rates-table').DataTable().ajax.reload();
                },
                error: function(jqXHR, textStatus, errorThrown){
                    $('#myPleaseWait').modal('hide');
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

function getBinderDetPerilModal(){

    $("#btn-add-sect-perils").click(function(){
        searchSubPerils("");
    });
}

function makeBinderSelection(){
    var table = $('#binderList').DataTable();
    $('#binderList tbody').on( 'click', 'tr', function () {
        $(this).addClass('active').siblings().removeClass('active');
        var aData = table.rows('.active').data();
        var binderId = aData[0].binId;
        deactivateNew(binderId);
        if (aData[0] === undefined || aData[0] === null){

        }
        else{
            $("#binder-sel-pk").val(aData[0].binId);
            $("#rates-bind-code").val(aData[0].binId);
            model.product.proCode = aData[0].product.proCode;
            // if(aData[0].product.proGroup.prgType ==="MD"){
            //     //$("#profile-tab-3").show();
            //     $("#profile-tab-2").hide();
            //     $("#profile-tab-11").show();
            //     $("#tab_content2-11").show();
            //     $("#medical-covers-tab").show();
            // }
            // else{
            //     //$("#profile-tab-3").hide();
            //     //$("#myTab-2 li:profile-tab-11").hide();
            //     $("#profile-tab-2").hide();
            //     $("#profile-tab-11").hide();
            //     $("#tab_content2-11").hide();
            //     $("#binder-med-cards").hide();
            //     $("#medical-covers-tab").hide();
            //
            // }

            createAcctBindDet();
            getBinderRatesTbl();
            // getBinderRules();
            // getBinderExclusions();
            // getBinderLoadings();
            // createBinderProviders();
             makeBinderDetSelection();
            // createProvidersContracts();
            // createBinderCardTypes(aData[0].binId);
            // getQuestionnaireDtls(aData[0].binId);
            // makeQuizSelection();



            $("#binder-det-code-pk").val("");
            model.covertype.covId = "";
            $("#binder-sc-code-pk").val("");
            $("#binder-det-sub-code").val("");
            $("#rates-bind-det-code").val("");



            createPremRatesTbl();
            createBinderClauses(-2000);
            createCommRatesTbl(-2000);
            // createMedicalCovers(-2000);

            createBinderReqdDocs(-2000);
            getBinderPremRatesTbl(-2000);
            createBinderSectionsLov(-2000);
            createLifeCommRatesTbl(-2000);



            if(aData[0].product.proGroup.prgType ==="L"){
                $(".life-comm-rates").show();

                $("#comm-rates-tab").hide();
                $("#tab_content3").hide();
                $('#lifecommRatesList').DataTable().ajax.reload();
            }else  {
                $(".life-comm-rates").hide();
                $("#comm-rates-tab").show();
                $("#tab_content3").show();

            }
        }
    } );
}

function makeBinderDetSelection(){
    var table = $('#binderDetList').DataTable();
    $('#binderDetList tbody').on( 'click', 'tr', function () {
        $(this).addClass('active').siblings().removeClass('active');
        var aData = table.rows('.active').data();
        if (aData[0] === undefined || aData[0] === null){

        }
        else{
            $("#binder-det-code-pk").val(aData[0].detId);
            model.covertype.covId = aData[0].subCoverTypes.scId;
            $("#binder-sc-code-pk").val(aData[0].subCoverTypes.scId);
            $("#binder-det-sub-code").val(aData[0].subCoverTypes.subclass.subId);
            model.subclass.subcode =aData[0].subCoverTypes.subclass.subId;
            createPremRatesTbl();
            createBinderClauses(aData[0].detId);
            createCommRatesTbl(aData[0].detId);
            createMedicalCovers(aData[0].detId);

            createBinderReqdDocs(aData[0].detId);
            getBinderPremRatesTbl(aData[0].detId);
            $("#rates-bind-det-code").val(aData[0].detId);
            createBinderSectionsLov(aData[0].subCoverTypes.subclass.subId);

            makeBinderPremRatesSelection();
        }
    } );
}


function makeQuizSelection(){
    var table = $('#questionList').DataTable();
    $('#questionList tbody').on( 'click', 'tr', function () {
        $(this).addClass('active').siblings().removeClass('active');
        var aData = table.rows('.active').data();
        if(aData) {
            showChoices(aData[0].bqdid);
            $("#choice-quiz-pk").val(aData[0].bqdid);
        }
    } );
}
function makeBinderPremRatesSelection(){
    var table = $('#premList').DataTable();
    $('#premList tbody').on( 'click', 'tr', function () {
        $(this).addClass('active').siblings().removeClass('active');
        var aData = table.rows('.active').data();
        console.log(aData);
        if (aData === 'undefined' || aData === null || aData[0] === undefined || aData[0] === null ){
        }
        else {
            $("#prem-item-code-pk").val(aData[0].id);
            console.log("Id="+aData[0].id);
            createLifeCommRatesTbl(aData[0].id);
        }
    } );
}
function getQuestionnaireDtls(binCode){
    $('#questionList').DataTable( {
        "processing": true,
        "serverSide": true,
        "ajax": 'questionnaire/'+binCode,
        lengthMenu: [ [10, 15], [10, 15] ],
        pageLength: 10,
        destroy: true,
        "columns": [
            { "data": "questionShtDesc" },
            { "data": "questionname" },
            { "data": "questiontype" },
            { "data": "questionMandatory" },
            { "data": "questionOrder" },
            { "data": "triggerByQuiz" ,
                "render": function ( data, type, full, meta ) {
                    if (full.triggerByQuiz)
                        return full.triggerByQuiz.questionname;
                }},
            { "data": "triggerByQuizAnswer" ,
                "render": function ( data, type, full, meta ) {
                    if (full.triggerByQuizAnswer)
                        return full.triggerByQuizAnswer.choice;
                }},
            {
                "data": "bqdid",
                "render": function ( data, type, full, meta ) {
                    if(full.binder.binStatus===null)
                        return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-quiz='+encodeURI(JSON.stringify(full)) + ' onclick="editQuestion(this);"><i class="fa fa-pencil-square-o"></button>';
                    else
                        return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-quiz='+encodeURI(JSON.stringify(full)) + ' onclick="editQuestion(this);" disabled><i class="fa fa-pencil-square-o"></button>';

                }

            },
            {
                "data": "bqdid",
                "render": function ( data, type, full, meta ) {
                    if(full.binder.binStatus===null)
                        return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-quiz='+encodeURI(JSON.stringify(full)) + ' onclick="deleteQuestion(this);"><i class="fa fa-trash-o"></button>';
                    else
                        return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-quiz='+encodeURI(JSON.stringify(full)) + ' onclick="deleteQuestion(this);" disabled><i class="fa fa-trash-o"></button>';
                }

            },
        ]
    } );

    var $quizform = $('#quiz-form');
    $('#saveQuestion').click(function(){
        if (!$quizform.valid()) {
            return;
        }
        var validator = $quizform.validate();
        // var $btn = $(this).button('Saving');
        var data = {};
        $quizform.serializeArray().map(function(x){data[x.name] = x.value;});
        var url = "createQuestionnaire";
        var request = $.post(url, data );
        request.success(function(){
            new PNotify({
                title: 'Success',
                text: 'Record created/updated Successfully',
                type: 'success',
                styling: 'bootstrap3'
            });
            $('#questionList').DataTable().ajax.reload();
            validator.resetForm();
            $('#quiz-form').find("input[type=text],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden], textarea,select2,select").val("");
            $('#quizModal').modal('hide');
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
            // $btn.button('reset');
        });
    });

    $("#btn-add-questionnaire-choice").on('click', function () {
        $("#choice-quiz-code").val($("#choice-quiz-pk").val());

        $('#quizChoiceModal').modal('show');
    });


    $("#btn-add-questionnaire").on('click', function () {
        $('#quiz-form').find("input[type=text],input[type=number],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden],textarea,select2,select").val("");
        $("#quiz-binder-code").val($("#binder-sel-pk").val());
        createQuestionChoice(0);
        createQuestions($("#quiz-binder-code").val());
        $('#quizModal').modal('show');

    });

    var $quizchoiceform = $('#quiz-choice-form');
    $('#saveQuestionChoice').click(function(){
        if (!$quizchoiceform.valid()) {
            return;
        }
        var validator = $quizchoiceform.validate();
        // var $btn = $(this).button('Saving');
        var data = {};
        $quizchoiceform.serializeArray().map(function(x){data[x.name] = x.value;});
        var url = "createQuestionnaireChoices";
        var request = $.post(url, data );
        request.success(function(){
            new PNotify({
                title: 'Success',
                text: 'Record created/updated Successfully',
                type: 'success',
                styling: 'bootstrap3'
            });
            $('#questionChoiceList').DataTable().ajax.reload();
            validator.resetForm();
            $('#quiz-choice-form').find("input[type=text],input[type=textarea],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden], textarea,select").val("");
            $('#quizChoiceModal').modal('hide');
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
            // $btn.button('reset');
        });
    });
}
function showChoices(quizId){
    $('#questionChoiceList').DataTable( {
        "processing": true,
        "serverSide": true,
        "ajax": 'questionnairechoices/'+quizId,
        lengthMenu: [ [10, 15], [10, 15] ],
        pageLength: 10,
        destroy: true,
        "columns": [
            { "data": "choice" },
            {
                "data": "qcId",
                "render": function ( data, type, full, meta ) {
                    if(full.questions.binder.binStatus===null)
                        return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-choice='+encodeURI(JSON.stringify(full)) + ' onclick="editChoice(this);"><i class="fa fa-pencil-square-o"></button>';
                    else
                        return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-choice='+encodeURI(JSON.stringify(full)) + ' onclick="editChoice(this);" disabled><i class="fa fa-pencil-square-o"></button>';
                }

            },
            {
                "data": "qcId",
                "render": function ( data, type, full, meta ) {
                    if(full.questions.binder.binStatus===null)
                        return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-choice='+encodeURI(JSON.stringify(full)) + ' onclick="deleteQuestionChoice(this);"><i class="fa fa-trash-o"></button>';
                    else
                        return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-choice='+encodeURI(JSON.stringify(full)) + ' onclick="deleteQuestionChoice(this);" disabled><i class="fa fa-trash-o"></button>';

                }

            },
        ]
    } );
}
function createBinderReqdDocs(detCode){
    var url = "requiredDocs/"+detCode;
    var currTable = $('#reqDocsList').DataTable( {
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
            { "data": "brdId",
                "render": function ( data, type, full, meta ) {
                    return full.requiredDocs.requiredDoc.reqShtDesc;
                }
            },
            { "data": "brdId",
                "render": function ( data, type, full, meta ) {
                    return full.requiredDocs.requiredDoc.reqDesc;
                }
            },
            { "data": "brdId",
                "render": function ( data, type, full, meta ) {
                    return full.mandatory;
                }
            },
            {
                "data": "brdId",
                "render": function ( data, type, full, meta ) {
                    if(full.binderDetail.binder.binStatus===null)
                        return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-reqddocs='+encodeURI(JSON.stringify(full)) + ' onclick="editBinderReqrdDocs(this);"><i class="fa fa-pencil-square-o"></button>';
                    else
                        return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-reqddocs='+encodeURI(JSON.stringify(full)) + ' onclick="editBinderReqrdDocs(this);" disabled><i class="fa fa-pencil-square-o"></button>';

                }

            },
            {
                "data": "brdId",
                "render": function ( data, type, full, meta ) {
                    if(full.binderDetail.binder.binStatus===null)
                        return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-reqddocs='+encodeURI(JSON.stringify(full)) + ' onclick="deleteBinderReqdDocs(this);"><i class="fa fa-trash-o"></button>';
                    else
                        return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-reqddocs='+encodeURI(JSON.stringify(full)) + ' onclick="deleteBinderReqdDocs(this);" disabled><i class="fa fa-trash-o"></button>';

                }

            },
        ]
    } );
    return currTable;
}
function editContract(button){
    var contract = JSON.parse(decodeURI($(button).data("contract")));
    $("#provtype-code").val(contract["spcId"]);
    $("#provtype-contract-code").val(contract["contractNo"]);


    $("#contrct-bind-code").val($("#binder-sel-pk").val());

    $("#contract-no").text(contract["contractNo"]);
    $("#provider-id").val(contract["serviceProviders"].mspId);

    $("#provider-name").text(contract["serviceProviders"].name);

    $("#provider-type").text(contract["serviceProviders"].serviceProviderTypes.desc);
    $("#contract-type").val(contract["contractType"]);
    $("#from-date").val(moment(contract["wefDate"]).format('DD/MM/YYYY'));
    $("#contract-to-date").val(moment(contract["wetDate"]).format('DD/MM/YYYY'));
    $("#contract-status").val(contract["status"]);
    $("#contr-notes").val(contract["notes"]);
    providerLov();
    $('.provider-namelov').hide();
    $('.provider-diaplay').show();
    $('#provContractModal').modal({
        backdrop: 'static',
        keyboard: true
    })

}
function deleteContract(button){
    var contract = JSON.parse(decodeURI($(button).data("contract")));

    bootbox.confirm("Are you sure want to delete "+ contract["contractNo"]+ "?", function(result) {
        if(result){
            $.ajax({
                type: 'GET',
                url:  'deleteProviderContract/' + contract["spcId"],
                dataType: 'json',
                async: true,
                success: function(result) {
                    new PNotify({
                        title: 'Success',
                        text: 'Record Deleted Successfully',
                        type: 'success',
                        styling: 'bootstrap3'
                    });
                    $('#providerContractTbl').DataTable().ajax.reload();
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

function deleteBinderReqdDocs(button){
    var reqddocs = JSON.parse(decodeURI($(button).data("reqddocs")));
    bootbox.confirm("Are you sure want to delete "+reqddocs["requiredDocs"].requiredDoc.reqDesc+"?", function(result) {
        if(result){
            $.ajax({
                type: 'GET',
                url:  'deleteBinderReqrdDocs/' + reqddocs["brdId"],
                dataType: 'json',
                async: true,
                success: function(result) {
                    new PNotify({
                        title: 'Success',
                        text: 'Record Deleted Successfully',
                        type: 'success',
                        styling: 'bootstrap3'
                    });
                    $('#reqDocsList').DataTable().ajax.reload();
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


function editBinderReqrdDocs(button){
    var reqddocs = JSON.parse(decodeURI($(button).data("reqddocs")));
    $("#binder-reqdoc-code").val( reqddocs["brdId"]);
    $("#binder-reqdoc-doc-id").val( reqddocs["requiredDocs"].sclReqrdId);
    $("#binder-reqdoc-id").val(reqddocs["requiredDocs"].requiredDoc.reqShtDesc);
    $("#binder-reqdoc-name").val(reqddocs["requiredDocs"].requiredDoc.reqDesc);
    if(reqddocs["mandatory"]){
        $("#chk-reqdoc-mandatory").prop("checked", reqddocs["mandatory"]);
    }else{
        $("#chk-reqdoc-mandatory").prop("checked",reqddocs["requiredDocs"].mandatory);
    }

    $('#editBinderreqDocsModal').modal({
        backdrop: 'static',
        keyboard: true
    })
}


function updateBinderReqdDocs(){
    var $classForm = $('#binder-reqdocs-form');
    var validator = $classForm.validate();
    $('#saveBinderreqDocsBtn').click(function(){
        if (!$classForm.valid()) {
            return;
        }

        //  var $btn = $(this).button('Saving');
        var data = {};
        $classForm.serializeArray().map(function(x){data[x.name] = x.value;});
        var url = "updateBinderReqrdDocs";
        var request = $.post(url, data );
        request.success(function(){
            new PNotify({
                title: 'Success',
                text: 'Record Created/Updated Successfully',
                type: 'success',
                styling: 'bootstrap3'
            });
            $('#reqDocsList').DataTable().ajax.reload();
            $('#binder-reqdocs-form').find("input[type=text],input[type=number],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden], textarea,select").val("");
            $('#editBinderreqDocsModal').modal('hide');
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
            //    $btn.button('reset');
        });
    });
}





function deleteQuestion(button){
    var quiz = JSON.parse(decodeURI($(button).data("quiz")));
    bootbox.confirm("Are you sure want to delete this question?.This will also delete choices", function(result) {
        if(result){
            $.ajax({
                type: 'GET',
                url:  'deleteQuestion/' + quiz["bqdid"],
                dataType: 'json',
                async: true,
                success: function(result) {
                    new PNotify({
                        title: 'Success',
                        text: 'Record Deleted Successfully',
                        type: 'success',
                        styling: 'bootstrap3'
                    });
                    $('#questionList').DataTable().ajax.reload();
                    $('#questionChoiceList').DataTable().ajax.reload();

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

function deleteQuestionChoice(button){
    var choice = JSON.parse(decodeURI($(button).data("choice")));
    bootbox.confirm("Are you sure want to delete this question choice?", function(result) {
        if(result){
            $.ajax({
                type: 'GET',
                url:  'deleteChoice/' + choice["bqcId"],
                dataType: 'json',
                async: true,
                success: function(result) {
                    new PNotify({
                        title: 'Success',
                        text: 'Record Deleted Successfully',
                        type: 'success',
                        styling: 'bootstrap3'
                    });
                    $('#questionChoiceList').DataTable().ajax.reload();
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


function addBinderPremRates(){

    $("#prem-rate-type").click(function(){
        if($(this).val()==="P"){
            $("#prem-div-fact").val(100);
        }
        else if($(this).val()==="M"){
            $("#prem-div-fact").val(1000);
        }
        else{
            $("#prem-div-fact").val(0);
        }
    });

    $("#btn-add-prem-rates").click(function(){
        if($("#binder-det-code-pk").val() != ''){
            $('#prem-rates-form').find("input[type=text],input[type=number],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden], textarea,select").val("");
            $("#prem-binder-det").val($("#binder-det-code-pk").val());
            $("#p-sect-modal").hide();
            $("#section-modal").show();
            $("#chk-rates-appli").prop("checked", false);
            //$("#prem-multi-rate").val(1);
            $("#range-from").prop("disabled", true);
            $("#range-to").prop("disabled", true);
            $("#prem-range-type").prop("disabled", true);
            $("#chk-prem-active").prop("checked", true);
            $('#premRatesModal').modal({
                backdrop: 'static',
                keyboard: true
            })
        }
        else{
            bootbox.alert("Select Binder Detail to continue");
        }

    });
    var $classForm = $('#prem-rates-form');
    var validator = $classForm.validate();
    $('#savepremratesBtn').click(function(){
        if (!$classForm.valid()) {
            return;
        }

        //var $btn = $(this).button('Saving');
        var data = {};
        $classForm.serializeArray().map(function(x){data[x.name] = x.value;});
        var url = "createPremRates";
        //console.log(data);
        var request = $.post(url, data );
        request.success(function(){
            new PNotify({
                title: 'Success',
                text: 'Record created/updated Successfully',
                type: 'success',
                styling: 'bootstrap3'
            });
            $('#premList').DataTable().ajax.reload();
            $('#sect-modal').select2('val', null);
            createCoverSections();
            validator.resetForm();
            $('#prem-rates-form').find("input[type=text],input[type=number],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden], textarea,select").val("");
            $("#prem-rates-form input:checkbox").prop("checked", false);
            $('#premRatesModal').modal('hide');
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
            //  $btn.button('reset');
        });
    });
}



function saveBinderDetails(){
    var arr = [];
    $("#saveBinderDetBtn").click(function(){
        $("#subclassCoverTbl tbody").find('input[name="record"]').each(function(){
            if($(this).is(":checked")){
                arr.push($(this).attr("id"));
            }
        });
        if(arr.length==0){
            bootbox.alert("No Sub Class Cover Types Selected to attach..");
            return;
        }

        console.log(arr);

        var $currForm = $('#binder-det-form');
        var currValidator = $currForm.validate();
        if (!$currForm.valid()) {
            return;
        }

        var data = {};
        $currForm.serializeArray().map(function(x) {
            data[x.name] = x.value;
        });
        var url = "createBinderDetails";
        data.coverTypes = arr;


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
                $('#binderDetList').DataTable().ajax.reload();
                $('#binderDetModal').modal('hide');
                arr=[];
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

function addBinderDetails(){

    $("#btn-add-binder-det").click(function(){
        if($("#binder-sel-pk").val() != ''){
            $("#bind-prod-bind-code").val($("#binder-sel-pk").val());
            $.ajax({
                type: 'GET',
                url:  'bindSubclassCovts',
                dataType: 'json',
                data: {"bindCode": $("#binder-sel-pk").val(),"prodCode": model.product.proCode},
                async: true,
                success: function(result) {
                    $("#subclassCoverTbl tbody").each(function(){
                        $(this).remove();
                    });
                    for(var res in result){
                        var markup = "<tr><td><input type='checkbox' name='record' id='"+result[res][0]+"'></td><td>" + result[res][1] + "</td><td>" +  result[res][2] + "</td><td>" + result[res][3] + "</td><td>"  + result[res][4] + "</td></tr>";
                        $("#subclassCoverTbl").append(markup);
                    }
                    $('#binderDetModal').modal({
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
            bootbox.alert("Select Binder to continue");
        }

    });


}

function addBinder(){

    $("#btn-add-binder").click(function(){
        if($("#acc-id").val() != ''){
            $('#binder-form').find("input[type=text],input[type=number],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden], textarea,select").val("");
            $("#act-bin-code").val($("#acc-id").val());

            $(".binder-fund").hide();
            $(".med-cover-type").hide();
            $("#bin-fund-binder").prop("checked", false);
            $('#binderModal').modal({
                backdrop: 'static',
                keyboard: true
            })
        }
        else{
            bootbox.alert("Select Account to continue");
        }
    });


    var $classForm = $('#binder-form');
    var validator = $classForm.validate();
    $('#saveBinderBtn').click(function(){
        if (!$classForm.valid()) {
            return;
        }

        // var $btn = $(this).button('Saving');
        var data = {};
        $classForm.serializeArray().map(function(x){data[x.name] = x.value;});
        var url = "createBinder";
        var request = $.post(url, data );
        request.success(function(){
            new PNotify({
                title: 'Success',
                text: 'Record Created/Updated Successfully',
                type: 'success',
                styling: 'bootstrap3'
            });
            $('#binderList').DataTable().ajax.reload();
            validator.resetForm();
            $('#prd-code').select2('val', null);
            createProductForSel();
            $('#cur-def').select2('val', null);
            createCurrencyLov();
            $('#binder-form').find("input[type=text],input[type=number],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden], textarea,select").val("");
            $('#binderModal').modal('hide');
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
            //   $btn.button('reset');
        });
    });
}


function cloneNewBinder(){

    var $classForm = $('#clonebinder-form');
    var validator = $classForm.validate();
    $('#saveCloneBinderBtn').click(function(){
        if (!$classForm.valid()) {
            return;
        }

        // var $btn = $(this).button('Saving');
        var data = {};
        $classForm.serializeArray().map(function(x){data[x.name] = x.value;});
        var url = "cloneBinder";
        var request = $.post(url, data );
        request.success(function(){
            new PNotify({
                title: 'Success',
                text: 'Record Created/Updated Successfully',
                type: 'success',
                styling: 'bootstrap3'
            });
            $('#binderList').DataTable().ajax.reload();
            validator.resetForm();
            $('#cloneprd-code').select2('val', null);
            createProductForclone();
            $('#clonebinder-form').find("input[type=text],input[type=number],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden], textarea,select").val("");
            $('#clonebinderModal').modal('hide');
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
            //   $btn.button('reset');
        });
    });
}

function createAcctBindDet() {
    var url = "binderdetails/0";
    if ($("#binder-sel-pk").val() != '') {
        url = "binderdetails/" + $("#binder-sel-pk").val();
    }
    var currTable = $('#binderDetList').DataTable({
        "processing": true,
        "serverSide": true,
        autoWidth: true,
        "ajax": {
            'url': url,
        },
        lengthMenu: [[5], [5]],
        pageLength: 5,
        destroy: true,
        "columns": [
            {
                "data": "subCoverTypes",
                "render": function (data, type, full, meta) {

                    return full.subCoverTypes.subclass.subShtDesc;
                }
            },
            {
                "data": "subCoverTypes",
                "render": function (data, type, full, meta) {

                    return full.subCoverTypes.subclass.subDesc;
                }
            },
            {
                "data": "subCoverTypes",
                "render": function (data, type, full, meta) {

                    return full.subCoverTypes.coverTypes.covShtDesc;
                }
            },
            {
                "data": "subCoverTypes",
                "render": function (data, type, full, meta) {

                    return full.subCoverTypes.coverTypes.covName;
                }
            },
            {"data": "remarks"},
            {"data": "minPrem"},
            {"data": "singleSectionCover"},
            {"data": "limitsAllowed"},
            {"data": "installmentsNo"},
            {"data": "distribution"},
            {
                "data": "detId",
                "render": function (data, type, full, meta) {
                    if (full.binder.binStatus === null)
                        return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-binderdet=' + encodeURI(JSON.stringify(full)) + ' onclick="editBinderRemarks(this);"><i class="fa fa-pencil-square-o"></button>';
                    else
                        return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-binderdet=' + encodeURI(JSON.stringify(full)) + ' onclick="editBinderRemarks(this);" disabled><i class="fa fa-pencil-square-o"></button>';

                }

            },
            {
                "data": "detId",
                "render": function (data, type, full, meta) {
                    if (full.binder.binStatus === null)
                        return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-binderdet=' + encodeURI(JSON.stringify(full)) + ' value="Delete" onclick="confirmBinderDetDel(this);"><i class="fa fa-trash-o"></button>';
                    else
                        return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-binderdet=' + encodeURI(JSON.stringify(full)) + ' value="Delete" onclick="confirmBinderDetDel(this);" disabled><i class="fa fa-trash-o"></button>';

                }

            },
        ]
    });
    return currTable;
}
function createBinderClauses(detCode){
    var url = "binderClauses/"+detCode;
    var currTable = $('#bindclausesList').DataTable( {
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
            { "data": "clause",
                "render": function ( data, type, full, meta ) {
                    return full.clause.clause.clauShtDesc;
                }
            },
            { "data": "clause",
                "render": function ( data, type, full, meta ) {
                    if(full.clauHeading){
                        return full.clauHeading;
                    }
                    else
                        return full.clause.clause.clauHeading;
                }
            },
            { "data": "clause",
                "render": function ( data, type, full, meta ) {
                    if(full.editable){
                        return (full.editable==="Y")?"Yes":"No";
                    }
                    else
                        return full.clause.clause.editable;
                }
            },
            { "data": "clause",
                "render": function ( data, type, full, meta ) {
                    if(full.mandatory){
                        return (full.mandatory==="Y")?"Yes":"No";
                    }
                    else
                        return full.clause.mandatory;
                }
            },
            {
                "data": "clauId",
                "render": function ( data, type, full, meta ) {
                    if(full.binderDet.binder.binStatus===null)
                        return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-clauses='+encodeURI(JSON.stringify(full)) + ' onclick="editBinderClauses(this);"><i class="fa fa-pencil-square-o"></button>';
                    else
                        return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-clauses='+encodeURI(JSON.stringify(full)) + ' onclick="editBinderClauses(this);" disabled><i class="fa fa-pencil-square-o"></button>';

                }

            },
            {
                "data": "clauId",
                "render": function ( data, type, full, meta ) {
                    if(full.binderDet.binder.binStatus===null)

                        return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-clauses='+encodeURI(JSON.stringify(full)) + ' onclick="deleteBinderClause(this);"><i class="fa fa-trash-o"></button>';
                    else
                        return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-clauses='+encodeURI(JSON.stringify(full)) + ' onclick="deleteBinderClause(this);" disabled><i class="fa fa-trash-o"></button>';

                }

            },
        ]
    } );
    return currTable;
}
function editBinderClauses(button){
    var clause = JSON.parse(decodeURI($(button).data("clauses")));
    $("#binder-clause-code").val(clause["clauId"]);
    $("#binder-clause-pk").val(clause["clause"].clauId);
    $("#binder-clau-id").val(clause["clause"].clause.clauShtDesc);
    if(clause["clauHeading"]){
        $("#binder-clause-name").val(clause["clauHeading"]);
    }else
        $("#binder-clause-name").val(clause["clause"].clause.clauHeading);
    if(clause["clauWording"]){
        $("#binder-cla-wording").val(clause["clauWording"]);
    }
    else{
        $("#binder-cla-wording").val(clause["clause"].clause.clauWording);
    }
    if(clause["mandatory"]){
        if(clause["mandatory"]==="Y"){
            $("#chk-cl-mandatory").prop("checked", true);
        }
        else $("#chk-cl-mandatory").prop("checked", false);
    }else{
        $("#chk-cl-mandatory").prop("checked", clause["clause"].mandatory);
    }

    if(clause["editable"]){
        if(clause["editable"]==="Y"){
            $("#chk-cl-editable").prop("checked", true);
        }
        else $("#chk-cl-editable").prop("checked", false);
    }else{
        $("#chk-cl-editable").prop("checked", clause["clause"].clause.editable);
    }

    $('#editBinderclauseModal').modal({
        backdrop: 'static',
        keyboard: true
    });
}


function createPremRatesTbl(){
    var url = "premrates/0";
    if($("#binder-det-code-pk").val() != ''){
        url = "premrates/"+$("#binder-det-code-pk").val();
    }
    var currTable = $('#premList').DataTable( {
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
            { "data": "section",
                "render": function ( data, type, full, meta ) {

                    return full.section.desc;
                }
            },
            { "data": "rangeType",
                "render": function ( data, type, full, meta ) {
                    if(full.rangeType){
                        if(full.rangeType==="AG")
                            return "Age";
                        else if(full.rangeType==="AM")
                            return "Amount";
                    }
                }
            },
            { "data": "rangeFrom" },
            { "data": "rangeTo" },
            { "data": "rate" },
            { "data": "divFactor",
                "render": function ( data, type, full, meta ) {
                    if(full.divFactor){
                        if(full.divFactor===1000)
                            return "Per Mille";
                        else if(full.divFactor===100)
                            return "Percentage";
                        else if(full.divFactor===1)
                            return "Amount";
                    }
                }
            },
            { "data": "proratedFull" },
            { "data": "freeLimit" },
            { "data": "mandatory",
                "render": function ( data, type, full, meta ) {
                    if(full.mandatory){
                        return (full.mandatory==="Y")?"Yes":"No";
                    }
                    else
                        return "No";
                }
            },
            { "data": "active",
                "render": function ( data, type, full, meta ) {
                    if(full.active){
                        return (full.active)?"Yes":"No";
                    }
                    else
                        return "No";
                }
            },
            { "data": "section",
                "render": function ( data, type, full, meta ) {
                    if(full.binderDet.limitsAllowed ==="Y")
                        return '<button type="button" class="btn btn-primary btn btn-info btn-xs" data-premrates='+encodeURI(JSON.stringify(full)) + ' onclick="viewPremLimits(this);">Limits</button>';
                    else{
                        return "";
                    }

                }
            },
            {
                "data": "Id",
                "render": function ( data, type, full, meta ) {
                    if(full.binderDet.binder.binStatus==null)
                        return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-premrates='+encodeURI(JSON.stringify(full)) + ' onclick="editPremRates(this);"><i class="fa fa-pencil-square-o"></button>';
                    else
                        return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-premrates='+encodeURI(JSON.stringify(full)) + ' onclick="editPremRates(this);" disabled><i class="fa fa-pencil-square-o"></button>';

                }

            },
            {
                "data": "Id",
                "render": function ( data, type, full, meta ) {
                    if(full.binderDet.binder.binStatus==null)
                        return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-premrates='+encodeURI(JSON.stringify(full)) + ' onclick="deletePremRates(this);"><i class="fa fa-trash-o"></button>';
                    else
                        return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-premrates='+encodeURI(JSON.stringify(full)) + ' onclick="deletePremRates(this);" disabled><i class="fa fa-trash-o"></button>';

                }

            },
        ]
    } );
    return currTable;
}


function createCommRatesTbl(detCode){
    var url = "commRates/"+detCode;

    var currTable = $('#commRatesList').DataTable( {
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
            //  { "data": "rateDesc" },
            { "data": "rateType" },
            { "data": "commRate" },
            { "data": "commDivFactor" },
            { "data": "commRangeFrom" },
            { "data": "commRangeTo" },
            { "data": "revenueItems",
                "render": function ( data, type, full, meta ) {

                    return UTILITIES.getRevDesc(full.revenueItems.item);
                }
            },
            { "data": "active" },
            {
                "data": "commId",
                "render": function ( data, type, full, meta ) {
                    if(full.binderDet.binder.binStatus===null)
                        return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-commrates='+encodeURI(JSON.stringify(full)) + ' onclick="editCommRates(this);"><i class="fa fa-pencil-square-o"></button>';
                    else
                        return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-commrates='+encodeURI(JSON.stringify(full)) + ' onclick="editCommRates(this);" disabled><i class="fa fa-pencil-square-o"></button>';

                }

            },
            {
                "data": "commId",
                "render": function ( data, type, full, meta ) {
                    if(full.binderDet.binder.binStatus===null)
                        return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-commrates='+encodeURI(JSON.stringify(full)) + ' onclick="deleteCommRates(this);"><i class="fa fa-trash-o"></button>';
                    else
                        return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-commrates='+encodeURI(JSON.stringify(full)) + ' onclick="deleteCommRates(this);" disabled><i class="fa fa-trash-o"></button>';

                }

            },
        ]
    } );
    return currTable;
}




function createLifeCommRatesTbl(premItemCode){
    var url = "commRatesLife/"+premItemCode;

    var currTable = $('#lifecommRatesList').DataTable( {
        "processing": true,
        "serverSide": true,
        autoWidth: true,
        "ajax": {
            'url': url,
        },
        lengthMenu: [ [5], [5] ],
        pageLength: 5,
        "lengthChange": false,
        destroy: true,
        order: [[ 7, "asc" ],[ 1, "asc" ],[ 2, "asc" ]],
        "columns": [

            { "data": "commTermFrom" },
            { "data": "commTermTo" },
            { "data": "commYearFrom" },
            { "data": "commYearTo" },
            { "data": "commRate" },
            { "data": "commDivFactor" },
            { "data": "frequency",
                "render": function ( data, type, full, meta ) {
                    if (full.frequency==="M") {
                        return "Monthly";
                    }
                    if (full.frequency==="A") {
                        return "Annual";
                    }
                    if (full.frequency==="Q") {
                        return "Quarterly";
                    }
                    if (full.frequency==="S") {
                        return "Semi-Annual";
                    }
                }
            },
            { "data": "wefDate",
                "render": function ( data, type, full, meta ) {
                    if (full.wefDate)
                        return moment(full.wefDate).format('DD/MM/YYYY');
                    else return "";
                }
            },
            { "data": "wetDate",
                "render": function ( data, type, full, meta ) {
                    if (full.wetDate)
                        return moment(full.wetDate).format('DD/MM/YYYY');
                    else return "";
                }
            },
            {
                "data": "commId",
                "render": function ( data, type, full, meta ) {
                    if(full.premRatesDef.binderDet.binder.binStatus===null)
                        return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-lifecommrates='+encodeURI(JSON.stringify(full)) + ' onclick="editLifeCommRates(this);"><i class="fa fa-pencil-square-o"></button>';
                    else
                        return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-lifecommrates='+encodeURI(JSON.stringify(full)) + ' onclick="editLifeCommRates(this);" disabled><i class="fa fa-pencil-square-o"></button>';

                }

            },
            {
                "data": "commId",
                "render": function ( data, type, full, meta ) {
                    // if(full.premRatesDef.binderDetail.binder.binStatus===null)

                        return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-lifecommrates='+encodeURI(JSON.stringify(full)) + ' onclick="deleteLifeCommRates(this);"><i class="fa fa-trash-o"></button>';
                    // else
                    //     return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-lifecommrates='+encodeURI(JSON.stringify(full)) + ' onclick="deleteLifeCommRates(this);" disabled><i class="fa fa-trash-o"></button>';

                }

            },
        ]
    } );
    return currTable;
}


function createMedicalCovers(detCode){
    var url = "medcovers/"+detCode;

    var currTable = $('#medCoversList').DataTable( {
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
            { "data": "section",
                "render": function ( data, type, full, meta ) {
                    return full.section.desc;
                }
            },
            { "data": "mainCover" },
            { "data": "mainSection",
                "render": function ( data, type, full, meta ) {
                    return full.mainSection.desc;
                }
            },
            { "data": "proratedFull" },
            { "data": "minPremium",
                "render": function ( data, type, full, meta ) {

                    return UTILITIES.currencyFormat(full.minPremium);
                }
            },
            { "data": "waitPeriod" },
            { "data": "applicableAt",
                "render": function ( data, type, full, meta ) {
                    if(full.applicableAt){
                        if(full.applicableAt ==="F") return "Per Family";
                        else if(full.applicableAt ==="M") return "Per Member";
                    }
                    else return "";
                }
            },
            {
                "data": "id",
                "render": function ( data, type, full, meta ) {
                    if(full.binderDet.binder.binStatus===null)
                        return '<button type="button" class="btn btn-info btn-xs" data-medrates='+encodeURI(JSON.stringify(full)) + ' onclick="editMedLimits(this);">Limits</button>';
                    else
                        return '<button type="button" class="btn btn-info btn-xs" data-medrates='+encodeURI(JSON.stringify(full)) + ' onclick="editMedLimits(this);" disabled>Limits</button>';

                }

            },

            {
                "data": "id",
                "render": function ( data, type, full, meta ) {
                    if(full.binderDet.binder.binStatus===null)
                        return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-medrates='+encodeURI(JSON.stringify(full)) + ' onclick="editMedRates(this);"><i class="fa fa-pencil-square-o"></button>';
                    else
                        return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-medrates='+encodeURI(JSON.stringify(full)) + ' onclick="editMedRates(this);" disabled><i class="fa fa-pencil-square-o"></button>';

                }

            },
            {
                "data": "id",
                "render": function ( data, type, full, meta ) {
                    if(full.binderDet.binder.binStatus===null)
                        return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-medrates='+encodeURI(JSON.stringify(full)) + ' onclick="deleteMedrates(this);"><i class="fa fa-trash-o"></button>';
                    else
                        return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-medrates='+encodeURI(JSON.stringify(full)) + ' onclick="deleteMedrates(this);" disabled><i class="fa fa-trash-o"></button>';

                }

            },
        ]
    } );
    return currTable;
}



function createBinderCardTypes(binCode){
    var url = "binderCardTypes/"+binCode;

    var currTable = $('#medCardList').DataTable( {
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
            { "data": "cardTypes",
                "render": function ( data, type, full, meta ) {
                    return full.cardTypes.cardDesc;
                }
            },
            { "data": "cardFee",
                "render": function ( data, type, full, meta ) {

                    return UTILITIES.currencyFormat(full.cardFee);
                }
            },
            { "data": "cardReissueFee",
                "render": function ( data, type, full, meta ) {

                    return UTILITIES.currencyFormat(full.cardReissueFee);
                }
            }
            ,{ "data": "serviceCharge",
                "render": function ( data, type, full, meta ) {

                    return UTILITIES.currencyFormat(full.serviceCharge);
                }
            },
            { "data": "serviceProrated" },
            { "data": "vatApplicable",
                "render": function ( data, type, full, meta ) {
                    if (full.vatApplicable)
                        return full.vatApplicable;
                    else return "N";
                }
            },
            { "data": "wefDate",
                "render": function ( data, type, full, meta ) {
                    if (full.wefDate)
                        return moment(full.wefDate).format('DD/MM/YYYY');
                    else return "";
                }
            },
            { "data": "wetDate",
                "render": function ( data, type, full, meta ) {
                    if (full.wetDate)
                        return moment(full.wetDate).format('DD/MM/YYYY');
                    else return "";
                }
            },

            {
                "data": "id",
                "render": function ( data, type, full, meta ) {
                    if(full.binder.binStatus===null)
                        return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-bindercards='+encodeURI(JSON.stringify(full)) + ' onclick="editBinderCard(this);"><i class="fa fa-pencil-square-o"></button>';
                    else
                        return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-bindercards='+encodeURI(JSON.stringify(full)) + ' onclick="editBinderCard(this);" disabled><i class="fa fa-pencil-square-o"></button>';

                }

            },
            {
                "data": "id",
                "render": function ( data, type, full, meta ) {
                    if(full.binder.binStatus===null)
                        return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-bindercards='+encodeURI(JSON.stringify(full)) + ' onclick="deleteBinderCard(this);"><i class="fa fa-trash-o"></button>';
                    else
                        return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-bindercards='+encodeURI(JSON.stringify(full)) + ' onclick="deleteBinderCard(this);" disabled><i class="fa fa-trash-o"></button>';

                }

            },
        ]
    } );
    return currTable;
}

function createAcctBinders(){
    var url = "binders/0";
    if($("#acc-id").val() != ''){
        url = "binders/"+$("#acc-id").val();
    }
    var currTable = $('#binderList').DataTable( {
        "processing": true,
        "serverSide": true,
        // autoWidth: true,
        "ajax": {
            'url': url,
        },
        lengthMenu: [ [5], [5] ],
        pageLength: 5,
        destroy: true,
        'columnDefs': [{
            'targets': 0,
            'searchable':false,
            'orderable':false,
        }],
        "columns": [
            { "data": "binType",
                "render": function ( data, type, full, meta ) {

                    if(full.binType){
                        if(full.binType==="B")
                            return "Contract";
                        else if(full.binType==="M")
                            return "Negotiable";
                        else if(full.binType==="S")
                            return "Self Fund";
                    }
                    else
                        return "Unknown";
                }
            },
            { "data": "binName" },
            { "data": "binPolNo" },
            { "data": "product",
                "render": function ( data, type, full, meta ) {

                    return full.product.proDesc;
                }
            },
            { "data": "currency",
                "render": function ( data, type, full, meta ) {

                    return full.currency.curName;
                }
            },
            { "data": "active" },
            {
                "data": "binId",
                "render": function ( data, type, full, meta ) {
                    return '<button type="button" class="btn btn-info btn-xs" data-binder='+encodeURI(JSON.stringify(full)) + '  onclick="cloneBinder(this);">Clone</button>';
                }

            },

            {
                "data": "binId",
                "render": function ( data, type, full, meta ) {
                    if (full.binStatus === null) {
                        return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-binder=' + encodeURI(JSON.stringify(full)) + ' value="Delete" onclick="confirmBinderDel(this);"><i class="fa fa-trash-o"></button>';
                    }
                    else{
                        return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-binder=' + encodeURI(JSON.stringify(full)) + ' value="Delete" onclick="confirmBinderDel(this);" disabled><i class="fa fa-trash-o"></button>';
                    }
                }
            },
            {
                "data": "binId",
                "render": function ( data, type, full, meta ) {

                    if (full.binStatus===null)

                        return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-binder=' + encodeURI(JSON.stringify(full)) + ' value="Edit" onclick="editBinder(this);"><i class="fa fa-pencil-square-o"></button>';
                    else
                        return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-binder=' + encodeURI(JSON.stringify(full)) + ' value="Edit" onclick="editBinder(this);" disabled><i class="fa fa-pencil-square-o"></button>';



                }

            },

            {
                "data": "binId",
                "render": function ( data, type, full, meta ) {

                    if (full.binStatus===null) {
                        return '<form id="readyForm" method="post" enctype="multipart/form-data"><input type="hidden" id="readyId" name="binId" value='+full.binId+'></form><button class="btn btn-success btn-xs btn-ready" >Make Ready</button>';

                        //  return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-binder=' + encodeURI(JSON.stringify(full)) + 'onclick="makeBinReady(this);" >Make Ready</button>';
                    } //else if(full.binStatus==="Ready"|| full.binStatus==="Authorise"){
                    else{
                        return '<form id="readyForm" method="post" enctype="multipart/form-data"><input type="hidden" id="readyId" name="binId" value='+full.binId+'></form><button class="btn btn-success btn-xs btn-ready" disabled>Make Ready</button>';

                        // return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-binder=' + encodeURI(JSON.stringify(full)) + ' onclick="makeBinReady(this);" disabled>Make Ready</button>';
                    }

                }

            },
            {
                "data": "binId",
                "render": function ( data, type, full, meta ) {

                    if (full.binStatus==="Ready" || full.binStatus==="Unauthorised") {
                        return '<form id="undoreadyForm" method="post" enctype="multipart/form-data"><input type="hidden" id="undoreadyId" name="binId" value='+full.binId+'></form><button class="btn btn-success btn-xs btn-undo" >Undo Ready</button>';

                        //  return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-binder=' + encodeURI(JSON.stringify(full)) + 'onclick="makeBinReady(this);" >Make Ready</button>';
                    } //else if(full.binStatus===null || full.binStatus==="Authorise"){
                    else{
                        return '<form id="undoreadyForm" method="post" enctype="multipart/form-data"><input type="hidden" id="undoreadyId" name="binId" value='+full.binId+'></form><button class="btn btn-success btn-xs btn-undo" disabled>Undo Ready</button>';

                        // return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-binder=' + encodeURI(JSON.stringify(full)) + ' onclick="makeBinReady(this);" disabled>Make Ready</button>';
                    }

                }

            },
            {
                "data": "binId",
                "render": function (data, type, full, meta) {

                    if (full.binStatus === "Ready") {
                        return '<form id="authoriseForm" method="post" enctype="multipart/form-data"><input type="hidden" id="authoriseId" name="binId" value=' + full.binId + '></form><button class="btn btn-success btn-xs btn-authorise">Authorise</button>';
                        $(".btn-dis").hide();
                        // return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-binder=' + encodeURI(JSON.stringify(full)) + ' onclick="binderauthorise(this);" >Authorise</button>';
                    } else {
                        return '<form id="authoriseForm" method="post" enctype="multipart/form-data"><input type="hidden" id="authoriseId" name="binId" value=' + full.binId + '></form><button class="btn btn-success btn-xs btn-authorise" disabled>Authorise</button>';

                        // return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-binder=' + encodeURI(JSON.stringify(full)) + ' onclick="binderauthorise(this);" disabled>Authorise</button>';
                    }
                }
            },

            {
                "data": "binId",
                "render": function (data, type, full, meta) {

                    if (full.binStatus === "Authorised") {
                        return '<form id="unauthoriseForm" method="post" enctype="multipart/form-data"><input type="hidden" id="unauthoriseId" name="binId" value=' + full.binId + '></form><button class="btn btn-success btn-xs btn-unauthorise">Unauthorise</button>';
                        $(".btn-dis").hide();
                        // return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-binder=' + encodeURI(JSON.stringify(full)) + ' onclick="binderauthorise(this);" >Authorise</button>';
                    } else {
                        return '<form id="unauthoriseForm" method="post" enctype="multipart/form-data"><input type="hidden" id="unauthoriseId" name="binId" value=' + full.binId + '></form><button class="btn btn-success btn-xs btn-unauthorise" disabled>Unauthorise</button>';

                        // return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-binder=' + encodeURI(JSON.stringify(full)) + ' onclick="binderauthorise(this);" disabled>Authorise</button>';
                    }
                }
            }

        ]
    } );
    return currTable;

}
function deactivateNew(id){
    var url='getDeactivate/'+id;
    $.ajax({
        type: 'GET',
        url: url,
    }).done(function (s) {

        if(s.binStatus==='Authorised' || s.binStatus==='Ready' || s.binStatus==='Unauthorised'){
            $(".btn-dis").prop("disabled", true);
        }
        else{
            $('.btn-dis').prop("disabled",false);
        }
        if(s.binStatus!==null){
            $(".btn-dis").prop("disabled", true);
        }
    }).fail(function (xhr, error) {
        new PNotify({
            title: 'Error',
            text: xhr.responseText,
            type: 'error',
            styling: 'bootstrap3'
        });
    });
}
function makeBinReady(id){
    var url='makeBinReady/'+id;
    $.ajax({
        type: 'GET',
        url: url,
    }).done(function (s) {
        $('#myPleaseWait').modal('hide');
        new PNotify({
            title: 'Success',
            text: 'Binder Made Ready Successfully',
            type: 'success',
            styling: 'bootstrap3'
        });
        $("#binderList").DataTable().ajax.reload();
    }).fail(function (xhr, error) {
        new PNotify({
            title: 'Error',
            text: xhr.responseText,
            type: 'error',
            styling: 'bootstrap3'
        });
    });
}
function makeBinUndo(id){
    var url='makeBinUndo/'+id;
    $.ajax({
        type: 'GET',
        url: url,
    }).done(function (s) {
        $('#myPleaseWait').modal('hide');
        new PNotify({
            title: 'Success',
            text: 'Binder Ready Undone Successfully',
            type: 'success',
            styling: 'bootstrap3'
        });
        $("#binderList").DataTable().ajax.reload();
    }).fail(function (xhr, error) {
        new PNotify({
            title: 'Error',
            text: xhr.responseText,
            type: 'error',
            styling: 'bootstrap3'
        });
    });
}
function makeBinAuthorise(id){
    var url='makeBinAuthorise/'+id;
    bootbox.confirm("Are you sure want to authorise this binder?", function (result) {
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
                    text: 'Binder Authorised Successfully',
                    type: 'success',
                    styling: 'bootstrap3'
                });
                $("#binderList").DataTable().ajax.reload();
            }).fail(function (xhr, error) {
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
function makeBinUnAuthorise(id){
    var url='makeBinUnAuthorise/'+id;
    bootbox.confirm("Are you sure want to unauthorise this binder?", function (result) {
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
                    text: 'Binder Unauthorised Successfully',
                    type: 'success',
                    styling: 'bootstrap3'
                });
                $("#binderList").DataTable().ajax.reload();
            }).fail(function (xhr, error) {
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
function editCommRates(button){
    var commrates = JSON.parse(decodeURI($(button).data("commrates")));
    $("#comm-code").val(commrates["commId"]);
    $("#rev-item-id").val(commrates["revenueItems"].revenueId);
    $("#rev-item-name").val(commrates["revenueItems"].item);
    createRevenueItemsForSel();
    $("#comm-binder-det").val(commrates["binderDet"].detId);
    $("#comm-range-from").val(commrates["commRangeFrom"]);
    $("#comm-range-to").val(commrates["commRangeTo"]);
    $("#comm-rate-type").val(commrates["rateType"]);
    $("#comm-rate").val(commrates["commRate"]);
    $("#comm-div-fact").val(commrates["commDivFactor"]);
    $("#comm-active").prop("checked", commrates["active"]);
    $('#commRatesModal').modal({
        backdrop: 'static',
        keyboard: true
    });
}


function editLifeCommRates(button){
    $('#commission-rates-form').find("input[type=text],input[type=number],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden],input[type=date], textarea,select").val("");
    var commrates = JSON.parse(decodeURI($(button).data("lifecommrates")));
    $("#com-code").val(commrates["commId"]);
    $("#comm-binder-det-code").val(commrates['binderDet'].detId);
    $("#comm-term-from").val(commrates["commTermFrom"]);
    $("#comm-term-to").val(commrates["commTermTo"]);
    $("#comm-year-from").val(commrates["commYearFrom"]);
    $("#comm-year-to").val(commrates["commYearTo"]);
    $("#comm-pay-freq").val(commrates["frequency"]);
    $("#com-rate").val(commrates["commRate"]);
    $("#comm-prem-code").val(commrates['premRatesDef'].id);
    $("#com-div-fact").val(commrates["commDivFactor"]);
    if (commrates["wefDate"])
        $("#comm-from-date").val(moment(commrates["wefDate"]).format('DD/MM/YYYY'));
    else $("#comm-from-date").val("");
    if (commrates["wetDate"])
        $("#comm-to-date").val(moment(commrates["wetDate"]).format('DD/MM/YYYY'));
    else $("#comm-to-date").val("");
    $("#com-div-fact").val(commrates["commDivFactor"]);
    $('#commissionRatesModal').modal({
        backdrop: 'static',
        keyboard: true
    });
}

function editBinder(button){
    var subclass = JSON.parse(decodeURI($(button).data("binder")));
    $("#bind-code").val(subclass["binId"]);
    $("#act-bin-code").val($("#acc-id").val());
    $("#bin-type").val(subclass["binType"]);
    $("#bind-id").val(subclass["binShtDesc"]);
    $("#bind-name").val(subclass["binName"]);
    $("#bind-pol-no").val(subclass["binPolNo"]);
    $("#prd-id").val(subclass["product"].proCode);
    $("#pr-name").val(subclass["product"].proDesc);
    createProductForSel();
    $("#cur-id").val(subclass["currency"].curCode);
    $("#cur-name").val(subclass["currency"].curName);
    createCurrencyLov();
    $("#bin-remarks").val(subclass["binRemarks"]);
    $("#bind-min-prem").val(subclass["minPrem"]);
    $("#bind-min-term").val(subclass["minTerm"]);
    $("#bind-max-term").val(subclass["maxTerm"]);
    $("#premium-age-type").val(subclass["premiumAgeType"]);
    $("#medical-cover-type").val(subclass["medicalCoverType"]);
    if (subclass["product"].proGroup.prgType==='L') {
        $(".binderterms").show();
    }else $(".binderterms").hide();
    if(subclass["product"].proGroup.prgType ==='MD'){
        $(".binder-fund").show();
        $(".med-cover-type").show();
    }
    else{
        $(".binder-fund").hide();
        $(".med-cover-type").hide();
    }
    if(subclass["fundBinder"]){
        if(subclass["fundBinder"]==='Y'){
            $("#bin-fund-binder").prop("checked", true);
        }
        else $("#bin-fund-binder").prop("checked", false);
    }
    else $("#bin-fund-binder").prop("checked", false);
    $("#chk-active").prop("checked", subclass["active"]);
    $("#bin-default").prop("checked", subclass["binDefault"]);
    $('#binderModal').modal({
        backdrop: 'static',
        keyboard: true
    });
}


function cloneBinder(button){
    var binders = JSON.parse(decodeURI($(button).data("binder")));
    $("#clonebind-code").val(binders["binId"]);
    $("#clonebin-type").val(binders["binType"]);
    $("#clonefrombind-name").text(binders["binName"]);
    $("#cloneprd-id").val(binders.product.proCode);
    $("#clonepr-name").text(binders.product.proDesc);
    createAccountsForclone();
//	createProductForclone();
    $("#bin-remarks").val(binders["binRemarks"]);
    $('#clonebinderModal').modal({
        backdrop: 'static',
        keyboard: true
    });
}

function viewPremLimits(button){
    var premrates = JSON.parse(decodeURI($(button).data("premrates")));
    $("#premlimts-det-code").val(premrates['id']);
    $("#covt-limit-sub-id").val(premrates['binderDet'].subCoverTypes.subclass.subId);
    var url = "sectLimits/"+premrates['id'];
    $('#premLimitsTbl').DataTable( {
        "processing": true,
        "serverSide": true,
        autoWidth: true,
        searching: false,
        "deferRender": true,
        "ajax": {
            'url': url
        },
        lengthMenu: [ [5,10], [5,10] ],
        pageLength: 10,
        destroy: true,
        "columns": [
            { "data": "clausesDef",
                "render": function ( data, type, full, meta ) {
                    return full.clausesDef.clause.clauShtDesc;
                }
            },
            { "data": "clausesDef" ,
                "render": function ( data, type, full, meta ) {
                    return full.clausesDef.clause.clauHeading;
                }
            },
            { "data": "value" },
            {
                "data": "id",
                "render": function ( data, type, full, meta ) {
                    if(full.premRatesDef.binderDet.binder.binStatus===null)
                        return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-clauses='+encodeURI(JSON.stringify(full)) + '  onclick="editpremLimitVal(this);"><i class="fa fa-pencil-square-o"></button>';
                    else
                        return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-clauses='+encodeURI(JSON.stringify(full)) + '  onclick="editpremLimitVal(this);" disabled><i class="fa fa-pencil-square-o"></button>';

                }

            },
            {
                "data": "id",
                "render": function ( data, type, full, meta ) {
                    if(full.premRatesDef.binderDet.binder.binStatus===null)
                        return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-clauses='+encodeURI(JSON.stringify(full)) + ' onclick="delpremLimitVal(this);"><i class="fa fa-trash-o"></button>';
                    else
                        return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-clauses='+encodeURI(JSON.stringify(full)) + ' onclick="delpremLimitVal(this);" disabled><i class="fa fa-trash-o"></button>';

                }

            },
        ]
    } );
    $('#premLimitsModal').modal({
        backdrop: 'static',
        keyboard: true
    });

}

function addSectLimits(){
    $("#btn-add-prem-limit").on('click', function(){
        $.ajax({
            type: 'GET',
            url: 'uanssignedSectLimits',
            dataType: 'json',
            data: {"sectCode": $("#premlimts-det-code").val(),"subId": $("#covt-limit-sub-id").val()},
            async: true,
            success: function (result) {
                console.log(result);
                $("#premSectLimitsTbl tbody").each(function () {
                    $(this).remove();
                });
                for (var res in result) {
                    var markup = "<tr><td><input type='checkbox' name='record' id='" +result[res][0]+ "'></td><td>" + result[res][1] + "</td><td>" + result[res][2] + "</td></tr>";
                    $("#premSectLimitsTbl").append(markup);
                }
                $('#addSectLimtsModal').modal({
                    backdrop: 'static',
                    keyboard: true
                });
            },
            error: function (jqXHR, textStatus, errorThrown) {
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


function saveSectLimits(){

    $("#savePremLimitsBtn").click(function(){
        var arr = [];
        $("#premSectLimitsTbl tbody").find('input[name="record"]').each(function(){
            if($(this).is(":checked")){
                arr.push($(this).attr("id"));
            }
        });
        if(arr.length==0){
            bootbox.alert("No Premium Limits Selected to attach..");
            return;
        }


        var $currForm = $('#premlimits-det-form');
        var currValidator = $currForm.validate();
        if (!$currForm.valid()) {
            return;
        }

        var data = {};
        $currForm.serializeArray().map(function(x) {
            data[x.name] = x.value;
        });
        var url = "createSectLimits";
        data.clauses = arr;

        console.log(data);

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
                $('#premLimitsTbl').DataTable().ajax.reload();
                $('#addSectLimtsModal').modal('hide');
                arr=[];
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
    });
}

function updateSectLimits(){
    $('#saveEditPremLimit').click(function(){
        var $classForm = $('#edit-prem-limit-form');
        var validator = $classForm.validate();
        if (!$classForm.valid()) {
            return;
        }

        // var $btn = $(this).button('Saving');
        var data = {};
        $classForm.serializeArray().map(function(x){data[x.name] = x.value;});
        var url = "saveCoverSectLimits";
        var request = $.post(url, data );
        request.success(function(){
            new PNotify({
                title: 'Success',
                text: 'Record Crea`ted/Updated Successfully',
                type: 'success',
                styling: 'bootstrap3'
            });
            $('#premLimitsTbl').DataTable().ajax.reload();
            $('#edit-prem-limit-form').find("input[type=text],input[type=number],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden], textarea,select").val("");
            $('#editpremLimitsModal').modal('hide');

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
            //   $btn.button('reset');
        });
    });
}

function delpremLimitVal(button){
    var limits = JSON.parse(decodeURI($(button).data("clauses")));
    bootbox.confirm("Are you sure want to delete Selected Record?", function(result) {
        if(result){
            $.ajax({
                type: 'GET',
                url:  'deletePremLimits/' + limits["id"],
                dataType: 'json',
                async: true,
                success: function(result) {
                    new PNotify({
                        title: 'Success',
                        text: 'Record Deleted Successfully',
                        type: 'success',
                        styling: 'bootstrap3'
                    });
                    $('#premLimitsTbl').DataTable().ajax.reload();
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
function downloadRatesTable(button){
    var rateTable = JSON.parse(decodeURI($(button).data("ratetbl")));
    var file = rateTable["fileName"]

    $("#downloadExcel"+rateTable["id"]).attr("href",SERVLET_CONTEXT+"/protected/setups/binders/premiumRatesTable/"+ rateTable["id"]);
    //         $.ajax({
    //             type: 'GET',
    //             url:  'premiumRatesTable/' + rateTable["id"],
    //             // dataType: 'json',
    //             async: false,
    //             success: function(result) {
    //                 new PNotify({
    //                     title: 'Success',
    //                     text: 'Rates Table Downloaded Successfully',
    //                     type: 'success',
    //                     styling: 'bootstrap3'
    //                 });
    //             },
    //             error: function(jqXHR, textStatus, errorThrown) {
    //                 new PNotify({
    //                     title: 'Error',
    //                     text: jqXHR.responseText,
    //                     type: 'error',
    //                     styling: 'bootstrap3'
    //                 });
    //             }
    //         });
}

function editpremLimitVal(button){
    var limits = JSON.parse(decodeURI($(button).data("clauses")));
    $("#prem-limit-code").val(limits['id']);
    $("#prem-sub-clause-code").val(limits['clausesDef'].clauId);
    $("#prem-premdef-code").val(limits['premRatesDef'].id);
    $("#edit-clause-id").text(limits['clausesDef'].clause.clauHeading);
    $("#edit-clause-limit").val(limits['value']);
    $('#editpremLimitsModal').modal({
        backdrop: 'static',
        keyboard: true
    });
}

function editPremRates(button){
    var premrates = JSON.parse(decodeURI($(button).data("premrates")));
    $("#prem-sect-id").val(premrates['section'].id);
    $("#sec-name").val(premrates['section'].desc);
    model.covertype.covId = $("#binder-sc-code-pk").val();
    createCoverSections();
    if(premrates['mandatory'] && premrates['mandatory']==="Y") {
        $("#chk-rates-mandatory").prop("checked", true);
    }
    else {
        $("#chk-rates-mandatory").prop("checked", false);
    }
    if(premrates['active']) {
        $("#chk-prem-active").prop("checked", true);
    }
    else {
        $("#chk-prem-active").prop("checked", false);
    }
    if(premrates['ratesApplicable'] && premrates['ratesApplicable']==="Y"){
        $("#chk-rates-appli").prop("checked", true);
        $("#range-from").removeAttr('disabled');
        $("#range-to").removeAttr('disabled');
        $("#prem-range-type").removeAttr('disabled');
    }
    else {
        $("#chk-rates-appli").prop("checked", false);
        $("#range-from").prop("disabled", true);
        $("#range-to").prop("disabled", true);
        $("#prem-range-type").prop("disabled", true);
        //$('#prem-range-type').attr("disabled","disabled");
    }
    $("#prem-sect-type").val(premrates['sectType']);
    $("#range-from").val(premrates['rangeFrom']);
    $("#range-to").val(premrates['rangeTo']);
    $("#prem-rate").val(premrates['rate']);
    $("#prem-rate-type").val(premrates['rateType']);
    $("#prem-range-type").val(premrates['rangeType']);
    $("#prem-div-fact").val(premrates['divFactor']);
    $("#prem-free-limit").val(premrates['freeLimit']);
    $("#prorated-full").val(premrates['proratedFull']);
    $("#prem-ep-appl").prop("checked", premrates["epApplicable"]);
    $("#prem-min-prem").val(premrates['minPremium']);
    $("#min-endo-prem").val(premrates['minEndosPremium']);
    $("#ncd-level").val(premrates['ncdLevel']);
    $("#prem-multi-rate").val(premrates['multiRate']);
    $("#prem-code").val(premrates['id']);
    $("#prem-binder-det").val(premrates['binderDet'].detId);
    $("#p-sect-modal").show();
    $("#p-sect-modal").text(premrates['section'].desc);
    $("#section-modal").hide();
    $('#premRatesModal').modal({
        backdrop: 'static',
        keyboard: true
    });
}

function confirmBinderDel(button){
    var subclass = JSON.parse(decodeURI($(button).data("binder")));
    bootbox.confirm("Are you sure want to delete "+subclass["binName"]+"?", function(result) {
        if(result){
            $.ajax({
                type: 'GET',
                url:  'deleteBinder/' + subclass["binId"],
                dataType: 'json',
                async: true,
                success: function(result) {
                    new PNotify({
                        title: 'Success',
                        text: 'Record Deleted Successfully',
                        type: 'success',
                        styling: 'bootstrap3'
                    });
                    $('#binderList').DataTable().ajax.reload();
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


function deleteCommRates(button){
    var commrates = JSON.parse(decodeURI($(button).data("commrates")));
    bootbox.confirm("Are you sure want to delete "+commrates["commRate"]+"?", function(result) {
        if(result){
            $.ajax({
                type: 'GET',
                url:  'deleteCommRates/' + commrates["commId"],
                dataType: 'json',
                async: true,
                success: function(result) {
                    new PNotify({
                        title: 'Success',
                        text: 'Record Deleted Successfully',
                        type: 'success',
                        styling: 'bootstrap3'
                    });
                    $('#commRatesList').DataTable().ajax.reload();
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




function deleteLifeCommRates(button){
    var commrates = JSON.parse(decodeURI($(button).data("lifecommrates")));
    bootbox.confirm("Are you sure want to delete "+commrates["commRate"]+"?", function(result) {
        if(result){
            $.ajax({
                type: 'GET',
                url:  'deleteLifeCommRates/' + commrates["commId"],
                dataType: 'json',
                async: true,
                success: function(result) {
                    new PNotify({
                        title: 'Success',
                        text: 'Record Deleted Successfully',
                        type: 'success',
                        styling: 'bootstrap3'
                    });
                    $('#lifecommRatesList').DataTable().ajax.reload();
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

function deletePremRates(button){
    var premrates = JSON.parse(decodeURI($(button).data("premrates")));
    bootbox.confirm("Are you sure want to delete "+premrates["section"].desc+"?", function(result) {
        if(result){
            $.ajax({
                type: 'GET',
                url:  'deletePremRates/' + premrates["id"],
                dataType: 'json',
                async: true,
                success: function(result) {
                    new PNotify({
                        title: 'Success',
                        text: 'Record Deleted Successfully',
                        type: 'success',
                        styling: 'bootstrap3'
                    });
                    $('#premList').DataTable().ajax.reload();
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

function editBinderRemarks(button){
    var binder = JSON.parse(decodeURI($(button).data("binderdet")));
    $("#cov-binder-det-code").val( binder["detId"]);
    $("#cov-summary").val(binder["remarks"]);
    $("#cov-min-prem").val(binder["minPrem"]);
    if(binder["limitsAllowed"]==="Y")
        $("#chk-limits-allowed").prop("checked", true);
    else $("#chk-limits-allowed").prop("checked", false);
    if(binder["singleSectionCover"]==="Y")
        $("#chk-single-risk").prop("checked", true);
    else $("#chk-single-risk").prop("checked", false);
    $("#installs-no").val(binder["installmentsNo"]);
    $("#install-perce").val(binder["distribution"]);
    $('#binderCoverModal').modal({
        backdrop: 'static',
        keyboard: true
    })
}




function editQuestion(button){
    var quiz = JSON.parse(decodeURI($(button).data("quiz")));

    $("#pk-code").val( quiz["bqdid"]);
    $("#quiz-binder-code").val(quiz["binder"].binId);
    $("#quiz-name").val(quiz["questionname"]);
    $("#quiz-type").val(quiz["questiontype"]);
    $("#quiz-order").val(quiz["questionOrder"]);
    $("#question-mandatory").val(quiz["questionMandatory"]);
    $("#question-id").val(quiz["questionShtDesc"]);
    if (quiz["triggerByQuiz"]){
        $("#trigger-quiz-id").val(quiz["triggerByQuiz"].bqdid);
        $("#trigger-quiz-name").val(quiz["triggerByQuiz"].questionShtDesc);
        createQuestionChoice(quiz["triggerByQuiz"].bqdid);
    }

    createQuestions( $("#quiz-binder-code").val());
    if (quiz["triggerByQuizAnswer"]){
        $("#answer-id").val(quiz["triggerByQuizAnswer"].bqcId);
        $("#answer-name").val(quiz["triggerByQuizAnswer"].choice);
        createQuestionChoice(quiz["triggerByQuiz"].bqdid);
    }

    $('#quizModal').modal({
        backdrop: 'static',
        keyboard: true
    })
}


function editChoice(button){
    var choice = JSON.parse(decodeURI($(button).data("choice")));
    $("#choice-pk-code").val( choice["bqcId"]);
    $("#choice-quiz-code").val(choice["questions"].bqdid);
    $("#choice-name").val(choice["choice"]);
    $('#quizChoiceModal').modal({
        backdrop: 'static',
        keyboard: true
    })
}


function saveBinderCoverSummary(){
    var $classForm = $('#cover-summary-form');
    var validator = $classForm.validate();
    $('#saveCoverSummary').click(function(){
        if (!$classForm.valid()) {
            return;
        }

        //var $btn = $(this).button('Saving');
        var data = {};
        $classForm.serializeArray().map(function(x){data[x.name] = x.value;});
        var url = "createBinderDet";
        var request = $.post(url, data );
        request.success(function(){
            new PNotify({
                title: 'Success',
                text: 'Record Crea`ted/Updated Successfully',
                type: 'success',
                styling: 'bootstrap3'
            });
            $('#binderDetList').DataTable().ajax.reload();
            $('#cover-summary-form').find("input[type=text],input[type=number],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden], textarea,select").val("");
            $("#chk-single-risk").prop("checked", false);
            $("#chk-limits-allowed").prop("checked", false);
            $('#binderCoverModal').modal('hide');

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
            //  $btn.button('reset');
        });
    });
}

function confirmBinderDetDel(button){
    var binder = JSON.parse(decodeURI($(button).data("binderdet")));
    bootbox.confirm("Are you sure want to delete "+binder["subCoverTypes"].coverTypes.covName+"?", function(result) {
        if(result){
            $.ajax({
                type: 'GET',
                url:  'deleteBinderDetails/' + binder["detId"],
                dataType: 'json',
                async: true,
                success: function(result) {
                    new PNotify({
                        title: 'Success',
                        text: 'Record Deleted Successfully',
                        type: 'success',
                        styling: 'bootstrap3'
                    });
                    $('#binderDetList').DataTable().ajax.reload();
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


function createAccountsForSel(){
    if($("#acc-frm").filter("div").html() != undefined)
    {
        Select2Builder.initAjaxSelect2({
            containerId : "acc-frm",
            sort : 'name',
            change: function(e, a, v){
                $("#acc-id").val(e.added.acctId);
                model.account.acctId = e.added.acctId;
                createAcctBinders();
                makeBinderSelection();
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
            placeholder:"Select Insurance Company",
        });
    }
}



function createAccountsForclone(){
    if($("#cloneacc-frm").filter("div").html() != undefined)
    {
        Select2Builder.initAjaxSelect2({
            containerId : "cloneacc-frm",
            sort : 'name',
            change: function(e, a, v){
                $("#cloneacc-id").val(e.added.acctId);
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
            placeholder:"Select Account",
        });
    }
}

function createQuestions(binCode){
    if($("#triggerquiz-frm").filter("div").html() != undefined)
    {
        Select2Builder.initAjaxSelect2({
            containerId : "triggerquiz-frm",
            sort : 'questionShtDesc',
            change: function(e, a, v){
                $("#trigger-quiz-id").val(e.added.bqdid);
                createQuestionChoice(e.added.bqdid);
            },
            formatResult : function(a)
            {
                return a.questionShtDesc
            },
            formatSelection : function(a)
            {
                return a.questionShtDesc
            },
            initSelection: function (element, callback) {
                var code = $("#trigger-quiz-id").val();
                var name = $("#trigger-quiz-name").val();
                var data = {questionShtDesc:name,bqdid:code};
                callback(data);
            },
            id: "bqdid",
            params: {BinCode: binCode},
            placeholder:"Select Question",
        });
    }
}

function createQuestionChoice(quizCode){
    if($("#triggeranswer-frm").filter("div").html() != undefined)
    {
        Select2Builder.initAjaxSelect2({
            containerId : "triggeranswer-frm",
            change: function(e, a, v){
                $("#answer-id").val(e.added.bqcId);
            },
            formatResult : function(a)
            {
                return a.choice
            },
            formatSelection : function(a)
            {
                return a.choice
            },
            initSelection: function (element, callback) {
                var code = $("#answer-id").val();
                var name = $("#answer-name").val();
                var data = {choice:name,bqcId:code};
                callback(data);
            },
            id: "bqcId",
            params: {quizCode: quizCode},
            placeholder:"Select Choice",
        });
    }
}







function createRevenueItemsForSel(){
    if($("#rev-item-def").filter("div").html() != undefined)
    {
        Select2Builder.initAjaxSelect2({
            containerId : "rev-item-def",
            sort : 'item',
            change: function(e, a, v){
                $("#rev-item-id").val(e.added.revenueId);
            },
            formatResult : function(a)
            {
                return UTILITIES.getRevDesc(a.item)
            },
            formatSelection : function(a)
            {
                return UTILITIES.getRevDesc(a.item)
            },
            initSelection: function (element, callback) {
                var code = $("#rev-item-id").val();
                var name = $("#rev-item-name").val();
//	                model.accounts.branch.brnCode = code;
                var data = {item:name,revenueId:code};
                callback(data);
            },
            id: "revenueId",
            placeholder:"Select Revenue Item",
            params: {proCode: function(){
                    // return model.product.proCode;
                    console.log("Details ni "+ JSON.stringify(model));
                    console.log("Product ni " + model.product.proCode);
                    console.log("Subclass ni " + model.subclass.subcode);
                    return  model.subclass.subcode;
                }}
        });
    }
}


function createProductForSel(){
    if($("#prd-code").filter("div").html() != undefined)
    {
        Select2Builder.initAjaxSelect2({
            containerId : "prd-code",
            sort : 'proDesc',
            change: function(e, a, v){
                $("#prd-id").val(e.added.proCode);
                if(e.added.proGroup.prgType ==='MD'){
                    $(".binder-fund").show();
                    $(".med-cover-type").show();
                }
                else{
                    $(".binder-fund").hide();
                    $(".med-cover-type").hide();
                }
                if(e.added.proGroup.prgType ==='L'){
                    $(".binderterms").show();
                }
                else{
                    $(".binderterms").hide();
                }
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
                var code = $("#prd-id").val();
                var name = $("#pr-name").val();
                var data = {proDesc:name,proCode:code};
                callback(data);
            },
            id: "proCode",
            placeholder:"Select Product",
        });
    }
}

function createProductForclone(){
    if($("#cloneprd-code").filter("div").html() != undefined)
    {
        Select2Builder.initAjaxSelect2({
            containerId : "cloneprd-code",
            sort : 'proDesc',
            change: function(e, a, v){
                $("#cloneprd-id").val(e.added.proCode);

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
                var code = $("#cloneprd-id").val();
                var name = $("#clonepr-name").val();
                var data = {proDesc:name,proCode:code};
                callback(data);
            },
            id: "proCode",
            placeholder:"Select Product",
        });
    }
}
function createProdSubclassSel(){
    if($("#sel-sub-code").filter("div").html() != undefined)
    {
        Select2Builder.initAjaxSelect2({
            containerId : "sel-sub-code",
            sort : 'psId',
            change: function(e, a, v){
                $("#sub-id").val(e.added.subclass.subId);
                model.subclass.subcode = e.added.subclass.subId;
                createCoverTypesSel();
            },
            formatResult : function(a)
            {
                return a.subclass.subDesc
            },
            formatSelection : function(a)
            {
                return a.subclass.subDesc
            },
            initSelection: function (element, callback) {

            },
            id: "psId",
            placeholder:"Select Sub Class",
            params: {proCode: function(){
                    return model.product.proCode;
                }}
        });
    }
}

function createCoverSections(){
    if($("#sect-modal").filter("div").html() != undefined)
    {
        Select2Builder.initAjaxSelect2({
            containerId : "sect-modal",
            sort : 'scsId',
            change: function(e, a, v){
                $("#prem-sect-id").val(e.added.subSections.section.id);
                $("#prem-sect-type").val(e.added.subSections.section.type);
            },
            formatResult : function(a)
            {
                return a.subSections.section.desc
            },
            formatSelection : function(a)
            {
                return a.subSections.section.desc
            },
            initSelection: function (element, callback) {
            },
            id: "scsId",
            placeholder:"Select Section",
            params: {covCode: function(){
                    return  model.covertype.covId;
                }}
        });
    }
    if($("#cover-modal").filter("div").html() != undefined)
    {
        Select2Builder.initAjaxSelect2({
            containerId : "cover-modal",
            sort : 'scsId',
            change: function(e, a, v){
                $("#med-sect-id").val(e.added.subSections.section.id);
                $("#med-sec-name").val(e.added.subSections.section.desc);
            },
            formatResult : function(a)
            {
                return a.subSections.section.desc
            },
            formatSelection : function(a)
            {
                return a.subSections.section.desc
            },
            initSelection: function (element, callback) {
            },
            id: "scsId",
            placeholder:"Select Section",
            params: {covCode: function(){
                    return  model.covertype.covId;
                }}
        });
    }
    if($("#main-cover-modal").filter("div").html() != undefined)
    {
        Select2Builder.initAjaxSelect2({
            containerId : "main-cover-modal",
            sort : 'scsId',
            change: function(e, a, v){
                $("#main-med-sect-id").val(e.added.subSections.section.id);
                $("#main-med-sec-name").val(e.added.subSections.section.desc);
            },
            formatResult : function(a)
            {
                return a.subSections.section.desc
            },
            formatSelection : function(a)
            {
                return a.subSections.section.desc
            },
            initSelection: function (element, callback) {
            },
            id: "scsId",
            placeholder:"Select Section",
            params: {covCode: function(){
                    return  model.covertype.covId;
                }}
        });
    }
}



function createCardTypesSel(){

    if($("#cardtype-modal").filter("div").html() != undefined)
    {
        Select2Builder.initAjaxSelect2({
            containerId : "cardtype-modal",
            sort : 'cardDesc',
            change: function(e, a, v){
                $("#med-card-id").val(e.added.cardId);
                $("#med-cardtype-name").val(e.added.cardDesc);
            },
            formatResult : function(a)
            {
                return a.cardDesc
            },
            formatSelection : function(a)
            {
                return a.cardDesc
            },
            initSelection: function (element, callback) {
                var code = $("#med-card-id").val();
                var name = $("#med-cardtype-name").val();
                var data = {curName:name,curCode:code};
                callback(data);
            },
            id: "cardId",
            placeholder:"Select Section",
            params: {cardId:""}
        });
    }
}

function createCoverTypesSel(){
    if($("#sel-cov-code").filter("div").html() != undefined)
    {
        Select2Builder.initAjaxSelect2({
            containerId : "sel-cov-code",
            sort : 'covShtDesc',
            change: function(e, a, v){
                $("#cov-id").val(e.added.covId);
            },
            formatResult : function(a)
            {
                return a.covName
            },
            formatSelection : function(a)
            {
                return a.covName
            },
            initSelection: function (element, callback) {

            },
            id: "covId",
            placeholder:"Select Cover Type",
            params: {subCode: function(){
                    return model.subclass.subcode;
                }}
        });
    }
}

function createCurrencyLov(){
    if($("#cur-def").filter("div").html() != undefined)
    {
        Select2Builder.initAjaxSelect2({
            containerId : "cur-def",
            sort : 'curName',
            change: function(e, a, v){
                $("#cur-id").val(e.added.curCode);
            },
            formatResult : function(a)
            {
                return a.curName
            },
            formatSelection : function(a)
            {
                return a.curName
            },
            initSelection: function (element, callback) {
                var code = $("#cur-id").val();
                var name = $("#cur-name").val();
//	                model.accounts.branch.brnCode = code;
                var data = {curName:name,curCode:code};
                callback(data);
            },
            id: "curCode",
            placeholder:"Select Currency",
        });
    }
}



function searchClauses(search){
    if($("#binder-det-code-pk").val() != ''){
        $.ajax({
            type: 'GET',
            url:  'getUnassignedClauses',
            dataType: 'json',
            data: {"subCode": $("#binder-det-sub-code").val(),"detId": $("#binder-det-code-pk").val(),"search":search},
            async: true,
            success: function(result) {
                $("#subclausestbl tbody").each(function(){
                    $(this).remove();
                });
                for(var res in result){
                    var markup = "<tr><td><input type='checkbox' name='record' id='"+result[res].clauId+"'></td><td>" + result[res].clause.clauShtDesc + "</td><td>" + result[res].clause.clauHeading+ "</td><td>" + result[res].clause.editable + "</td><td>" + result[res].mandatory+ "</td></tr>";
                    $("#subclausestbl").append(markup);
                }
                //$("#sub-scl-pro-code").val($("#prg-prod-code").val());
                $('#subclauseModal').modal({
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
        bootbox.alert("Select Binder Detail to attach clauses")
    }
}

function getBinderClausesModal(){
    $("#btn-add-binder-clauses").click(function(){
        $("#cla-det-code").val($("#binder-det-code-pk").val());
        searchClauses("");
    });

    $("#searchClauses").click(function(){
        searchClauses($("#clause-name-search").val());
    })

}


function saveBinderClauses(){
    var arr = [];
    $("#saveBinderclausesBtn").click(function(){
        $("#subclausestbl tbody").find('input[name="record"]').each(function(){
            if($(this).is(":checked")){
                arr.push($(this).attr("id"));
            }
        });
        if(arr.length==0){
            bootbox.alert("No Clauses Selected to attach..");
            return;
        }

        var $currForm = $('#binder-clauses-form');
        var currValidator = $currForm.validate();
        if (!$currForm.valid()) {
            return;
        }

        var data = {};
        $currForm.serializeArray().map(function(x) {
            data[x.name] = x.value;
        });
        var url = "createBinderClauses";
        data.clauses = arr;


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
                $('#bindclausesList').DataTable().ajax.reload();
                $('#subclauseModal').modal('hide');
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

function deleteBinderClause(button){
    var clause = JSON.parse(decodeURI($(button).data("clauses")));
    bootbox.confirm("Are you sure want to delete "+clause["clause"].clause.clauHeading+"?", function(result) {
        if(result){
            $.ajax({
                type: 'GET',
                url:  'deleteBinderClause/' + clause["clauId"],
                dataType: 'json',
                async: true,
                success: function(result) {
                    new PNotify({
                        title: 'Success',
                        text: 'Record Deleted Successfully',
                        type: 'success',
                        styling: 'bootstrap3'
                    });
                    $('#bindclausesList').DataTable().ajax.reload(null,false);
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




function addCommRates(){
    $("#comm-rate-type").change(function(){
        if($(this).val()==="P"){
            $("#comm-div-fact").val(100);
        }
        else if($(this).val()==="M"){
            $("#comm-div-fact").val(1000);
        }
        else{
            $("#comm-div-fact").val(0);
        }
    });

    $("#btn-add-comm-rates").click(function(){
        if($("#binder-det-code-pk").val() != ''){
            $('#comm-rates-form').find("input[type=text],input[type=number],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden], textarea,select").val("");
            $("#comm-binder-det").val($("#binder-det-code-pk").val());
            $("#comm-active").prop("checked", true);
            $('#rev-item-def').select2('val', null);
            $('#commRatesModal').modal({
                backdrop: 'static',
                keyboard: true
            })
        }
        else{
            bootbox.alert("Select Binder Details to Add Rates");
        }
    });






    var $classForm = $('#comm-rates-form');
    var validator = $classForm.validate();
    $('#savecommratesBtn').click(function(){
        if (!$classForm.valid()) {
            return;
        }

        // var $btn = $(this).button('Saving');
        var data = {};
        $classForm.serializeArray().map(function(x){data[x.name] = x.value;});
        var url = "createCommRates";
        var request = $.post(url, data );
        request.success(function(){
            new PNotify({
                title: 'Success',
                text: 'Record Created/Updated Successfully',
                type: 'success',
                styling: 'bootstrap3'
            });
            $('#commRatesList').DataTable().ajax.reload();
            $('#comm-rates-form').find("input[type=text],input[type=number],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden], textarea,select").val("");
            $('#commRatesModal').modal('hide');
        });

        request.error(function(jqXHR, textStatus, errorThrown){
            console.log(jqXHR);
            bootbox.alert(jqXHR.responseText);
        });
        request.always(function(){
            //   $btn.button('reset');
        });
    });
}




function addLifeCommRates(){



    $("#btn-add-comm-rate").click(function(){
        if($("#prem-item-code-pk").val() != ''){
            $('#commission-rates-form').find("input[type=text],input[type=number],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden], textarea,select").val("");
            $("#comm-prem-code").val($("#prem-item-code-pk").val());
            $("#comm-binder-det-code").val($("#binder-det-code-pk").val());
            $('#commissionRatesModal').modal({
                backdrop: 'static',
                keyboard: true
            })
        }
        else{
            bootbox.alert("Select premium item to Add Rates");
        }
    });


    var $classForm = $('#commission-rates-form');
    var validator = $classForm.validate();
    $('#savelifecommratesBtn').click(function(){
        if (!$classForm.valid()) {
            return;
        }

        //var $btn = $(this).button('Saving');
        var data = {};
        $classForm.serializeArray().map(function(x){data[x.name] = x.value;});
        var url = "createLifeCommRates";
        var request = $.post(url, data );
        request.success(function(){
            new PNotify({
                title: 'Success',
                text: 'Record Created/Updated Successfully',
                type: 'success',
                styling: 'bootstrap3'
            });
            $('#lifecommRatesList').DataTable().ajax.reload();
            $('#commission-rates-form').find("input[type=text],input[type=number],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden],input[type=date], textarea,select").val("");
            $('#commissionRatesModal').modal('hide');
        });

        request.error(function(jqXHR, textStatus, errorThrown){
            console.log(jqXHR);
            bootbox.alert(jqXHR.responseText);
        });
        request.always(function(){
            //  $btn.button('reset');
        });
    });
}



function createBinderSectionsLov(subId){
    if($("#binder-sect-form").filter("div").html() != undefined)
    {
        Select2Builder.initAjaxSelect2({
            containerId : "binder-sect-form",
            sort : 'desc',
            change: function(e, a, v){
                $("#binder-sect-pk").val(e.added.id);
                getBinderSectPerils($("#binder-det-code-pk").val(),e.added.id);
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

            },
            id: "id",
            params: {subCode: subId},
            placeholder:"Select Section",
        });
    }
}


function getBinderSectPerils(detCode,sectCode){
    var url = "binderSectPerils";
    var currTable = $('#binder-perils-tbl').DataTable( {
        "processing": true,
        "serverSide": true,
        autoWidth: true,
        searching: false,
        "deferRender": true,
        "ajax": {
            'url': url,
            'data':{
                'detCode': detCode,
                'sectCode':  sectCode,
            },
        },
        lengthMenu: [ [10,15,20], [10,15,20] ],
        pageLength: 10,
        destroy: true,
        "columns": [
            { "data": "subclassPeril",
                "render": function ( data, type, full, meta ) {
                    return full.subclassPeril.peril.perilShtDesc;
                }
            },
            { "data": "subclassPeril" ,
                "render": function ( data, type, full, meta ) {
                    return full.subclassPeril.peril.perilDesc;
                }
            },
            {
                "data": "bspId",

                "render": function ( data, type, full, meta ) {
                    if(full.binderDetail.binder.binStatus===null)
                        return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-binder='+encodeURI(JSON.stringify(full)) + ' value="Delete" onclick="confirmBinderPerilDel(this);"><i class="fa fa-trash-o"></button>';
                    else
                        return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-binder='+encodeURI(JSON.stringify(full)) + ' value="Delete" onclick="confirmBinderPerilDel(this);" disabled><i class="fa fa-trash-o"></button>';

                }

            },
        ]
    } );
    return currTable;
}

function confirmBinderPerilDel(button){
    var peril = JSON.parse(decodeURI($(button).data("binder")));
    bootbox.confirm("Are you sure want to delete "+peril["subclassPeril"].peril.perilDesc+"?", function(result) {
        if(result){
            $.ajax({
                type: 'GET',
                url:  'deletBinderSectPeril/' + peril["bspId"],
                dataType: 'json',
                async: true,
                success: function(result) {
                    new PNotify({
                        title: 'Success',
                        text: 'Record Deleted Successfully',
                        type: 'success',
                        styling: 'bootstrap3'
                    });
                    $('#binder-perils-tbl').DataTable().ajax.reload();
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

function searchSubPerils(search){
    if(($("#binder-det-code-pk").val() != '') && ($("#binder-sect-pk").val() != '')){
        $.ajax({
            type: 'GET',
            url:  'binderperils',
            dataType: 'json',
            data: {"detCode": $("#binder-det-code-pk").val(),
                "subCode":$("#binder-det-sub-code").val(),
                "sectCode":$("#binder-sect-pk").val(),
                "perilName":search},
            async: true,
            success: function(result) {
                console.log(result);
                $("#binderSectPerilTbl tbody").each(function(){
                    $(this).remove();
                });
                for(var res in result){
                    var markup = "<tr><td><input type='checkbox' name='record' id='"+result[res].sperId+"'></td><td>" + result[res].peril.perilShtDesc + "</td><td>" + result[res].peril.perilDesc + "</td></tr>";
                    $("#binderSectPerilTbl").append(markup);
                }
                $("#peril-binder-det").val($("#binder-det-code-pk").val());
                $("#peril-sect-id").val($("#binder-sect-pk").val());
                $('#binderPerilModal').modal({
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
        bootbox.alert("Select Sub Class and Section to attach Perils  ")
    }
}

function SaveBinderSectPerils(){
    var arr = [];
    $("#saveBinderDetPeril").click(function(){
        $("#binderSectPerilTbl tbody").find('input[name="record"]').each(function(){
            if($(this).is(":checked")){
                arr.push($(this).attr("id"));
            }
        });
        if(arr.length==0){
            bootbox.alert("No Peril Selected to attach..");
            return;
        }

        var $currForm = $('#bind-peril-form');
        var currValidator = $currForm.validate();
        if (!$currForm.valid()) {
            return;
        }

        var data = {};
        $currForm.serializeArray().map(function(x) {
            data[x.name] = x.value;
        });
        var url = "createBinderSectPerils";
        data.perils = arr;


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
                $('#binder-perils-tbl').DataTable().ajax.reload();
                $('#binderPerilModal').modal('hide');
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


function addCoverLimits() {
    $("#btn-add-cover-limit").click(function () {
        $("#cov-limit-amt").val('');
        $('#limitsModal').modal({
            backdrop: 'static',
            keyboard: true
        })
    });


    var $classForm = $('#cover-limits-form');
    var validator = $classForm.validate();
    $('#saveCoverLimits').click(function(){
        if (!$classForm.valid()) {
            return;
        }

        // var $btn = $(this).button('Saving');
        var data = {};
        $classForm.serializeArray().map(function(x){data[x.name] = x.value;});
        var url = "createCoverLimits";
        var request = $.post(url, data );
        request.success(function(){
            new PNotify({
                title: 'Success',
                text: 'Record Created/Updated Successfully',
                type: 'success',
                styling: 'bootstrap3'
            });
            $('#coverLimitsTbl').DataTable().ajax.reload();
            $('#limitsModal').modal('hide');
            validator.resetForm();
        });

        request.error(function(jqXHR, textStatus, errorThrown){
            bootbox.alert(jqXHR.responseText);
        });
        request.always(function(){
            //   $btn.button('reset');
        });
    });


}



function deleteSubLimits(button){
    var coverlimits = JSON.parse(decodeURI($(button).data("coverlimits")));
    console.log(coverlimits);
    $.ajax({
        type: 'GET',
        url:  'deleteSubLimits/' + coverlimits["id"],
        dataType: 'json',
        async: true,
        success: function(result) {
            new PNotify({
                title: 'Success',
                text: 'Record Deleted Successfully',
                type: 'success',
                styling: 'bootstrap3'
            });
            $('#coverSubLimitsTbl').DataTable().ajax.reload();
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


function deleteCoverLimits(button){
    var coverlimits = JSON.parse(decodeURI($(button).data("coverlimits")));
    console.log(coverlimits);
    $.ajax({
        type: 'GET',
        url:  'deleteCoverLimits/' + coverlimits["id"],
        dataType: 'json',
        async: true,
        success: function(result) {
            new PNotify({
                title: 'Success',
                text: 'Record Deleted Successfully',
                type: 'success',
                styling: 'bootstrap3'
            });
            $('#coverLimitsTbl').DataTable().ajax.reload();
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


function editSubLimits(limitId){
    var url = "sublimits/"+limitId;
    var currTable = $('#coverSubLimitsTbl').DataTable( {
        "processing": true,
        "serverSide": true,
        autoWidth: true,
        "sDom": 'tp',
        "ajax": {
            'url': url,
        },
        lengthMenu: [ [5], [5] ],
        pageLength: 5,
        destroy: true,
        "columns": [
            { "data": "shtDesc"},
            { "data": "desc"},
            { "data": "limit"},
            { "data": "waitingPeriod"},
            {
                "data": "id",
                "render": function ( data, type, full, meta ) {
                    console.log(full);
                    if(full.covLimit.covers.binderDet.binder.binStatus===null)
                        return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-coverlimits='+encodeURI(JSON.stringify(full)) + ' onclick="deleteSubLimits(this);"><i class="fa fa-trash-o"></button>';
                    else
                        return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-coverlimits='+encodeURI(JSON.stringify(full)) + ' onclick="deleteSubLimits(this);" disabled><i class="fa fa-trash-o"></button>';
                }

            },
        ]
    } );
}



function editMedLimits(button){
    var medrates = JSON.parse(decodeURI($(button).data("medrates")));
    var url = "coverlimits/"+medrates['id'];
    var currTable = $('#coverLimitsTbl').DataTable( {
        "processing": true,
        "serverSide": true,
        autoWidth: true,
        "sDom": 't',
        "ajax": {
            'url': url,
        },
        lengthMenu: [ [5], [5] ],
        pageLength: 5,
        destroy: true,
        "columns": [
            { "data": "limitAmount"
            },
            {
                "data": "id",
                "render": function ( data, type, full, meta ) {
                    if(full.covers.binderDet.binder.binStatus===null)
                        return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-coverlimits='+encodeURI(JSON.stringify(full)) + ' onclick="deleteCoverLimits(this);"><i class="fa fa-trash-o"></button>';
                    else
                        return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-coverlimits='+encodeURI(JSON.stringify(full)) + ' onclick="deleteCoverLimits(this);" disabled><i class="fa fa-trash-o"></button>';

                }

            },
        ]
    } );
    $('#coverLimitsTbl tbody').on( 'click', 'tr', function () {
        $(this).addClass('active').siblings().removeClass('active');
        var aData = currTable.rows('.active').data();
        if (aData[0] === undefined || aData[0] === null) {

        }
        else {
            $("#sub-limit-cov-id").val(aData[0].id);
            editSubLimits(aData[0].id);

        }
    });
    $('#cover-limits-form').find("input[type=text],input[type=number],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden], textarea,select").val("");
    $("#limit-cov-code").val(medrates['id']);
    $('#coverLimitsModal').modal({
        backdrop: 'static',
        keyboard: true
    })
    editSubLimits(-2000);


}


function getBinderPremRatesTbl(detCode){
    var url = "bindPremRatesTable/"+detCode;
    var currTable = $('#prem-rates-table').DataTable( {
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
            { "data": "fileName"
            },
            { "data": "effDate",
                "render": function ( data, type, full, meta ) {
                    return moment(full.effDate).format('DD/MM/YYYY');
                }
            },
            {   "data": "id",
                "render": function ( data, type, full, meta ) {
                    return '<a id ='+"downloadExcel"+full.id+' type="button" class="btn btn-success" data-ratetbl='+encodeURI(JSON.stringify(full)) + ' onclick="downloadRatesTable(this);" target="_blank"><i class="fa fa-download"></i>  Download Rates Table</a>';
                }
            },
        ]
    } );
    return currTable;
}



function getBinderRatesTbl(){
    var url = "bindRatesTable/0";
    if($("#binder-sel-pk").val() != ''){
        url = "bindRatesTable/"+$("#binder-sel-pk").val();
    }
    var currTable = $('#rates-table').DataTable( {
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
            { "data": "fileName"
            },
            { "data": "effDate",
                "render": function ( data, type, full, meta ) {
                    return moment(full.effDate).format('DD/MM/YYYY');
                }
            },
        ]
    } );
    return currTable;
}


function getBinderRules(){
    var url = "medbinderRules/0";
    if($("#binder-sel-pk").val() != ''){
        url = "medbinderRules/"+$("#binder-sel-pk").val();
    }
    var currTable = $('#binder-rules-tbl').DataTable( {
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
            { "data": "ruleId",
                "render": function ( data, type, full, meta ) {
                    if(full.checks) {
                        return full.checks.checks.checkShtDesc;
                    } else {
                        return "";
                    }
                }
            },
            { "data": "value"
            },
            { "data": "desc"
            },
            { "data": "mandatory"
            },
            {
                "data": "ruleId",
                "render": function ( data, type, full, meta ) {
                    if(full.binder.binStatus===null)
                        return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-binderrules='+encodeURI(JSON.stringify(full)) + ' value="Edit" onclick="editBinderRules(this);"><i class="fa fa-pencil-square-o"></button>';
                    else
                        return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-binderrules='+encodeURI(JSON.stringify(full)) + ' value="Edit" onclick="editBinderRules(this);" disabled><i class="fa fa-pencil-square-o"></button>';

                }

            },
            {
                "data": "ruleId",
                "render": function ( data, type, full, meta ) {
                    if(full.binder.binStatus===null)
                        return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-binderrules='+encodeURI(JSON.stringify(full)) + ' value="Delete" onclick="deleteBinderRules(this);"><i class="fa fa-trash-o"></button>';
                    else
                        return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-binderrules='+encodeURI(JSON.stringify(full)) + ' value="Delete" onclick="deleteBinderRules(this);" disabled><i class="fa fa-trash-o"></button>';

                }

            },
        ]
    } );
    return currTable;
}


function getBinderExclusions(){
    var url = "binExclusions/0";
    if($("#binder-sel-pk").val() != ''){
        url = "binExclusions/"+$("#binder-sel-pk").val();
    }
    var currTable = $('#binder-exclusions-tbl').DataTable( {
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
            { "data": "beId",
                "render": function ( data, type, full, meta ) {
                    if(full.ailment) {
                        return full.ailment.baShtDesc;
                    } else {
                        if(full.medicalnetworks) {
                            return full.medicalnetworks.benShtDesc;
                        }else {
                            return full.clausesDef.clauShtDesc;
                        }
                    }
                }
            },
            { "data": "beId",
                "render": function ( data, type, full, meta ) {
                    if(full.ailment){
                        return full.ailment.baDesc;
                    }
                    else {
                        if(full.medicalnetworks) {
                            return full.medicalnetworks.benDesc;
                        } else {
                            return full.clausesDef.clauHeading;
                        }
                    }
                }
            },
            { "data": "beId",
                "render": function ( data, type, full, meta ) {
                    if (full.costPerClaim) {
                        return UTILITIES.currencyFormat(full.costPerClaim);
                    } else {
                        if(full.ailment) {
                            return UTILITIES.currencyFormat(full.ailment.costPerClaim);
                        }
                        else
                        {return ""}
                    }
                }
            },
            { "data": "beId",
                "render": function ( data, type, full, meta ) {
                    if (full.upperLimit) {
                        return UTILITIES.currencyFormat(full.upperLimit);
                    }else  {
                        if(full.ailment) {
                            return UTILITIES.currencyFormat(full.ailment.upperLimit);
                        }
                        else {
                            return ""
                        }
                    }
                }
            },
            { "data": "beId",
                "render": function ( data, type, full, meta ) {
                    if (full.waitingDays) {
                        return full.waitingDays;

                    } else {
                        if(full.ailment){
                            return full.ailment.waitingDays;
                        }
                        else {
                            return ""
                        }
                    }
                }
            },
            { "data": "beId",
                "render": function ( data, type, full, meta ) {
                    if (full.chronic) {
                        if(full.chronic==="Y")
                            return "true";
                        else return "false";
                    } else {

                        if (full.ailment) {
                            return full.ailment.chronic;
                        }
                        else {
                            return ""
                        }
                    }
                }

            },
            {
                "data": "beId",
                "render": function ( data, type, full, meta ) {
                    if(full.binder.binStatus===null)
                        return '<button type="button" class="btn btn-success btn btn-info btn-xs" type="button" data-binderexclusions='+encodeURI(JSON.stringify(full)) + ' onclick="editBinderExclusions(this);"><i class="fa fa-pencil-square-o"></button>';
                    else
                        return '<button type="button" class="btn btn-success btn btn-info btn-xs" type="button" data-binderexclusions='+encodeURI(JSON.stringify(full)) + ' onclick="editBinderExclusions(this);" disabled><i class="fa fa-pencil-square-o"></button>';

                }

            },
            {
                "data": "beId",
                "render": function ( data, type, full, meta ) {
                    if(full.binder.binStatus===null)
                        return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-binderexclusions='+encodeURI(JSON.stringify(full)) + ' onclick="deleteBinderExclusions(this);"><i class="fa fa-trash-o"></button>';
                    else
                        return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-binderexclusions='+encodeURI(JSON.stringify(full)) + ' onclick="deleteBinderExclusions(this);" disabled><i class="fa fa-trash-o"></button>';

                }

            },
        ]
    } );
    return currTable;
}


function editBinderExclusions(button){
    var exclusions = JSON.parse(decodeURI($(button).data("binderexclusions")));
    $("#exclusion-id").val(exclusions["beId"]);
    $("#exclusion-binder-id").val(exclusions["binder"].binId);

    if(exclusions["ailment"]) {
        $("#binder-excl-id").val(exclusions["ailment"].baShtDesc);
    } else {
        if (exclusions["medicalnetworks"]) {
            $("#binder-excl-id").val(exclusions["medicalnetworks"].benShtDesc);
        } else {
            if (exclusions["clausesDef"]) {
                $("#binder-excl-id").val(exclusions["clausesDef"].clauShtDesc);
            }
        }
    }
    if(exclusions["ailment"]) {
        $("#exclusion-desc").val(exclusions["ailment"].baDesc);
    } else {
        if (exclusions["medicalnetworks"]) {
            $("#exclusion-desc").val(exclusions["medicalnetworks"].benDesc);
        } else {
            if (exclusions["clausesDef"]) {
                $("#exclusion-desc").val(exclusions["clausesDef"].clauHeading);
            }
        }
    }


    if (exclusions["costPerClaim"]) {
        $("#cost-per-claim").val((exclusions["costPerClaim"]));
    } else {
        if(exclusions["ailment"]) {
            $("#cost-per-claim").val((exclusions["ailment"].costPerClaim));
        }
        else
        {$("#cost-per-claim").val("")}
    }
    if (exclusions["upperLimit"]) {
        $("#upper-Limit").val((exclusions["upperLimit"]));
    }else  {
        if(exclusions["ailment"]) {
            $("#upper-Limit").val((exclusions["ailment"].upperLimit));
        }
        else {
            $("#upper-Limit").val( "");
        }
    }
    if (exclusions["waitingDays"]) {
        $("#waiting-days").val(exclusions["waitingDays"]);

    } else {
        if(exclusions["ailment"]){
            $("#waiting-days").val( exclusions["ailment"].waitingDays);
        }
        else {
            $("#waiting-days").val("");
        }
    }
    //$("#cost-per-claim").val(exclusions["costPerClaim"]);
    //$("#upper-Limit").val(exclusions["upperLimit"]);
    //$("#waiting-days").val(exclusions["waitingDays"]);

    if(!(exclusions["chronic"]===null)){


        if (exclusions["chronic"]==="Y") {
            $("#exclusion-chronic").prop("checked", true);
            $("#exclusion-chronic-id").val("Y");
        }
        else  {$("#exclusion-chronic").prop("checked", false);
            $("#exclusion-chronic-id").val("N");}
    } else {

        if(exclusions["ailment"]){
            $("#exclusion-chronic").prop("checked", true);
            $("#exclusion-chronic-id").val("Y");

        }else { $("#exclusion-chronic").prop("checked", false);
            $("#exclusion-chronic-id").val("N");}
    }

    $('#editExclusionModal').modal('show');
}

function deleteBinderExclusions(button){
    var binderexclusions = JSON.parse(decodeURI($(button).data("binderexclusions")));
    bootbox.confirm("Are you sure want to delete ?", function(result) {
        if(result) {
            $.ajax({
                type: 'GET',
                url: 'deleteBinderExclusions/' + binderexclusions["beId"],
                dataType: 'json',
                async: true,
                success: function (result) {
                    new PNotify({
                        title: 'Success',
                        text: 'Record Deleted Successfully',
                        type: 'success',
                        styling: 'bootstrap3'
                    });
                    $('#binder-exclusions-tbl').DataTable().ajax.reload();
                },
                error: function (jqXHR, textStatus, errorThrown) {
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

function createBinderProviders(){
    var url = "binProviders/0";
    if($("#binder-sel-pk").val() != ''){
        url = "binProviders/"+$("#binder-sel-pk").val();
    }
    var currTable = $('#binder-provider-tbl').DataTable( {
        "processing": true,
        "serverSide": true,
        autoWidth: true,
        "ajax": {
            'url': url,
        },
        lengthMenu: [ [10], [10] ],
        pageLength: 10,
        destroy: true,
        "columns": [
            {
                "data": "provider",
                "render": function ( data, type, full, meta ) {
                    return full.provider.name;
                }

            },
            {
                "data": "provider",
                "render": function ( data, type, full, meta ) {
                    return full.provider.contactPerson;
                }

            },
            {
                "data": "provider",
                "render": function ( data, type, full, meta ) {
                    return full.provider.pinNo;
                }

            },
            {
                "data": "provider",
                "render": function ( data, type, full, meta ) {
                    return full.provider.telNumber;
                }

            },
            {
                "data": "provider",
                "render": function ( data, type, full, meta ) {
                    return full.provider.status;
                }

            },
            {
                "data": "provider",
                "render": function ( data, type, full, meta ) {
                    if(full.provider.bankBranches)
                        return full.provider.bankBranches.branchName;
                    else return "";
                }

            },
            {
                "data": "provider",
                "render": function ( data, type, full, meta ) {
                    return full.provider.accountNumber;
                }

            },
            {
                "data": "provider",
                "render": function ( data, type, full, meta ) {
                    if(full.provider.paymentModes)
                        return full.provider.paymentModes.pmDesc;
                    else return "";
                }

            },
            {
                "data": "beId",
                "render": function ( data, type, full, meta ) {
                    if(full.binder.binStatus===null)
                        return '<button type="button" class="btn btn-success btn btn-info btn-xs" type="button" data-subclass='+encodeURI(JSON.stringify(full)) + ' onclick="editSubclass(this);"><i class="fa fa-pencil-square-o"></button>';
                    else
                        return '<button type="button" class="btn btn-success btn btn-info btn-xs" type="button" data-subclass='+encodeURI(JSON.stringify(full)) + ' onclick="editSubclass(this);" disabled><i class="fa fa-pencil-square-o"></button>';

                }

            },
            {
                "data": "beId",
                "render": function ( data, type, full, meta ) {
                    if(full.binder.binStatus===null)
                        return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-subclass='+encodeURI(JSON.stringify(full)) + ' onclick="deleteSubclass(this);"><i class="fa fa-trash-o"></button>';
                    else
                        return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-subclass='+encodeURI(JSON.stringify(full)) + ' onclick="deleteSubclass(this);" disabled><i class="fa fa-trash-o"></button>';

                }

            },
        ]
    } );
    return currTable;
}

function getBinderLoadings(){
    var url = "binLoadings/0";
    if($("#binder-sel-pk").val() != ''){
        url = "binLoadings/"+$("#binder-sel-pk").val();
    }
    var currTable = $('#binder-loadings-tbl').DataTable( {
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
            { "data": "clId",
                "render": function ( data, type, full, meta ) {

                    return full.ailment.baShtDesc;
                }
            },
            { "data": "clId",
                "render": function ( data, type, full, meta ) {
                    if(full.ailmentDesc){
                        return full.ailmentDesc;
                    }else
                        return full.ailment.baDesc;
                }
            },
            { "data": "clId",
                "render": function ( data, type, full, meta ) {
                    if(full.costPerClaim){
                        return UTILITIES.currencyFormat(full.costPerClaim);
                    }else
                        return UTILITIES.currencyFormat(full.ailment.costPerClaim);
                }
            },
            { "data": "clId",
                "render": function ( data, type, full, meta ) {
                    if(full.upperLimit){
                        return UTILITIES.currencyFormat(full.upperLimit);
                    }else
                        return UTILITIES.currencyFormat(full.ailment.upperLimit);
                }
            },
            { "data": "clId",
                "render": function ( data, type, full, meta ) {
                    if(full.waitingDays){
                        return full.waitingDays;
                    }else
                        return full.ailment.waitingDays;
                }
            },
            { "data": "clId",
                "render": function ( data, type, full, meta ) {
                    if(full.chronic){
                        return full.chronic;
                    }else
                        return full.ailment.chronic;
                }
            },
            { "data": "clId",
                "render": function ( data, type, full, meta ) {

                    return full.rateType;
                }
            },
            { "data": "clId",
                "render": function ( data, type, full, meta ) {

                    return UTILITIES.currencyFormat(full.rate);
                }
            },
            { "data": "clId",
                "render": function ( data, type, full, meta ) {

                    return UTILITIES.currencyFormat(full.loadingAmt);
                }
            },
            {
                "data": "clId",
                "render": function ( data, type, full, meta ) {
                    if(full.binder.binStatus===null)
                        return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-ailments='+encodeURI(JSON.stringify(full)) + ' onclick="editBinderAilments(this);"><i class="fa fa-pencil-square-o"></button>';
                    else
                        return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-ailments='+encodeURI(JSON.stringify(full)) + ' onclick="editBinderAilments(this);" disabled><i class="fa fa-pencil-square-o"></button>';

                }

            },
            {
                "data": "clId",
                "render": function ( data, type, full, meta ) {
                    if(full.binder.binStatus===null)
                        return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-ailments='+encodeURI(JSON.stringify(full)) + ' onclick="deleteAilments(this);"><i class="fa fa-trash-o"></button>';
                    else
                        return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-ailments='+encodeURI(JSON.stringify(full)) + ' onclick="deleteAilments(this);" disabled><i class="fa fa-trash-o"></button>';

                }

            },
        ]
    } );
    return currTable;
}

function editBinderAilments(button){
    var ailments = JSON.parse(decodeURI($(button).data("ailments")));
    $("#binder-loading-code").val(ailments["clId"]);
    $("#binder-loading-pk").val(ailments["ailment"].baId);
    $("#binder-loading-id").val(ailments["ailment"].baShtDesc);
    if(ailments["ailmentDesc"]){
        $("#binder-loading-desc").val(ailments["ailmentDesc"]);
    }
    else $("#binder-loading-desc").val(ailments["ailment"].baDesc);
    $("#load-rate-type").val(ailments["rateType"]);
    $("#load-rate").val(ailments["rate"]);
    $("#load-amount").val(ailments["loadingAmt"]);
    if(ailments["chronic"]  && ailments["chronic"]==="Y"){
        $("#loading-chronic").prop("checked", true);
    }
    else if(!ailments["chronic"]){
        $("#loading-chronic").prop("checked", ailments["ailment"].chronic);
    }
    else $("#loading-chronic").prop("checked", false);
    if(ailments["costPerClaim"])
        $("#load-cost-per-claim").val(ailments["costPerClaim"]);
    else
        $("#load-cost-per-claim").val(ailments["ailment"].costPerClaim);
    if(ailments["upperLimit"])
        $("#load-upper-limit").val(ailments["upperLimit"]);
    else
        $("#load-upper-limit").val(ailments["ailment"].upperLimit);
    if(ailments["waitingDays"])
        $("#load-wait-days").val(ailments["waitingDays"]);
    else $("#load-wait-days").val(ailments["ailment"].waitingDays);
    $('#editBinderLoadingModal').modal('show');
}

function deleteBinderRules(button){
    var rules = JSON.parse(decodeURI($(button).data("binderrules")));
    bootbox.confirm("Are you sure want to delete "+rules["desc"]+"?", function(result) {
        if(result){
            $.ajax({
                type: 'GET',
                url:  'deleteBinderRules/' + rules["ruleId"],
                dataType: 'json',
                async: true,
                success: function(result) {
                    new PNotify({
                        title: 'Success',
                        text: 'Record Deleted Successfully',
                        type: 'success',
                        styling: 'bootstrap3'
                    });
                    $('#binder-rules-tbl').DataTable().ajax.reload();
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

function editBinderRules(button){
    var rules = JSON.parse(decodeURI($(button).data("binderrules")));
    $("#bind-rules-code").val(rules['ruleId']);
    $("#bind-rules-binder").val(rules['binder'].binId);
    $("#bind-rules-id").val(rules['shtDesc']);
    $("#rule-value").val(rules['value']);
    $("#rule-desc").val(rules['desc']);
    if(rules['mandatory']){
        if(rules['mandatory']==="Y")
            $("#rule-mandatory").prop("checked", true);
        else
            $("#rule-mandatory").prop("checked", false);
    }else
        $("#rule-mandatory").prop("checked", false);
    if(rules['checks']){
        console.log(rules['checks']);
        $("#check-id").val(rules['checks'].checkId);
        $("#check-name").val(rules['checks'].checks.checkName);
    }
    createChecksForSel(rules['binder'].binId);

    $('#bindRulesModal').modal({
        backdrop: 'static',
        keyboard: true
    })
}

function addMedicalCovers(){
    $("#btn-add-med-rates").click(function(){
        if($("#binder-det-code-pk").val() != ''){
            $('#med-covers-form').find("input[type=text],input[type=number],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden], textarea,select").val("");
            $("#med-cover-binder-det").val($("#binder-det-code-pk").val());
            $("#p-cover").hide();
            $("#p-parent-cover").hide();
            $("#med-section-modal").show();
            $("#main-med-section-modal").show();
            $(".gender-select").hide();
            $("#med-depends-gender").prop("checked", false);
            $("#bin-fund-cover-man").prop("checked", false);
            createCoverSections();
            $('#medCoversModal').modal({
                backdrop: 'static',
                keyboard: true
            })
        }
        else{
            bootbox.alert("Select Binder Detail to continue");
        }

    });

    var $classForm = $('#med-covers-form');
    var validator = $classForm.validate();
    $('#saveMedicalCover').click(function(){
        if (!$classForm.valid()) {
            return;
        }

        // var $btn = $(this).button('Saving');
        var data = {};
        $classForm.serializeArray().map(function(x){data[x.name] = x.value;});
        console.log(data);
        var url = "createMedCover";
        var request = $.post(url, data );
        request.success(function(){
            new PNotify({
                title: 'Success',
                text: 'Record Created/Updated Successfully',
                type: 'success',
                styling: 'bootstrap3'
            });
            $('#medCoversList').DataTable().ajax.reload();
            $('#med-covers-form').find("input[type=text],input[type=number],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden], textarea,select").val("");
            $('#medCoversModal').modal('hide');
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
            //   $btn.button('reset');
        });
    });
}


function addBinderCardTypes(){
    $("#btn-add-med-card").click(function(){
        if($("#binder-sel-pk").val() != ''){
            $('#med-cards-form').find("input[type=text],input[type=number],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden], textarea,select").val("");
            $("#med-card-binder").val($("#binder-sel-pk").val());
            $("#p-cardtype").hide();
            $("#med-cardtype-modal").show();
            //$("#card-from-date").val("");
            //$("#card-to-date").val("");
            createCardTypesSel();
            $('#medCardsModal').modal({
                backdrop: 'static',
                keyboard: true
            })
        }
        else{
            bootbox.alert("Select Binder Detail to continue");
        }

    });

    var $classForm = $('#med-cards-form');
    var validator = $classForm.validate();
    $('#saveMedCardTypes').click(function(){
        if (!$classForm.valid()) {
            return;
        }

        // var $btn = $(this).button('Saving');
        var data = {};
        $classForm.serializeArray().map(function(x){data[x.name] = x.value;});
        var url = "createBinderCardTypes";
        var request = $.post(url, data );
        request.success(function(){
            new PNotify({
                title: 'Success',
                text: 'Record Created/Updated Successfully',
                type: 'success',
                styling: 'bootstrap3'
            });
            $('#medCardList').DataTable().ajax.reload();
            $('#med-cards-form').find("input[type=text],input[type=number],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden], textarea,select").val("");
            $('#medCardsModal').modal('hide');
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
            //   $btn.button('reset');
        });
    });
}

function deleteMedrates(button){
    var medrates = JSON.parse(decodeURI($(button).data("medrates")));
    bootbox.confirm("Are you sure want to delete "+medrates["section"].desc+"?", function(result) {
        if(result){
            $.ajax({
                type: 'GET',
                url:  'deleteMedCover/' + medrates["id"],
                dataType: 'json',
                async: true,
                success: function(result) {
                    new PNotify({
                        title: 'Success',
                        text: 'Record Deleted Successfully',
                        type: 'success',
                        styling: 'bootstrap3'
                    });
                    $('#medCoversList').DataTable().ajax.reload();
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




function deleteBinderCard(button){
    var bincard = JSON.parse(decodeURI($(button).data("bindercards")));
    bootbox.confirm("Are you sure want to delete "+bincard["cardTypes"].cardDesc+"?", function(result) {
        if(result){
            $.ajax({
                type: 'GET',
                url:  'deleteBinderCard/' + bincard["id"],
                dataType: 'json',
                async: true,
                success: function(result) {
                    new PNotify({
                        title: 'Success',
                        text: 'Record Deleted Successfully',
                        type: 'success',
                        styling: 'bootstrap3'
                    });
                    $('#medCardList').DataTable().ajax.reload();
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


function editMedRates(button){
    var medrates = JSON.parse(decodeURI($(button).data("medrates")));
    $("#p-cover").show();
    $("#p-parent-cover").show();
    $("#p-cover").text(medrates['section'].desc);
    $("#p-parent-cover").text(medrates['mainSection'].desc);
    $("#medical-main-cov").val(medrates['mainCover']);
    $("#med-prorated-full").val(medrates['proratedFull']);
    $("#med-limit-amt").val(medrates['limitAmount']);
    $("#med-applicable-at").val(medrates['applicableAt']);
    $("#med-min-prem").val(medrates['minPremium']);
    $("#med-wait-prd").val(medrates['waitPeriod']);
    $("#med-cover-binder-det").val(medrates['binderDet'].detId);
    $("#med-depends-gender").prop("checked", medrates['dependsOnGender']);
    if(medrates['fundCoverMand']){
        if(medrates['fundCoverMand']==="Y"){
            $("#bin-fund-cover-man").prop("checked", true);
        }
        else $("#bin-fund-cover-man").prop("checked", false);
    }else
        $("#bin-fund-cover-man").prop("checked", false);
    $("#med-cov-gender").val(medrates['gender']);
    if(medrates['dependsOnGender']){
        $(".gender-select").show();
    }
    else{
        $(".gender-select").hide();
    }
    $("#med-cover-code").val(medrates['id']);
    $("#med-section-modal").hide();
    $("#main-med-section-modal").hide();
    $('#medCoversModal').modal({
        backdrop: 'static',
        keyboard: true
    })
}




function editBinderCard(button){
    var bincard = JSON.parse(decodeURI($(button).data("bindercards")));
    $("#p-cardtype").show();
    $("#med-cardtype-modal").hide();
    $("#p-cardtype").text(bincard['cardTypes'].cardDesc);
    $("#card-prorated-full").val(bincard['serviceProrated']);
    $("#card-vat_applicable").val(bincard['vatApplicable']);
    $("#med-card-id").val(bincard['cardTypes'].cardId);
    $("#med-card-fee").val(bincard['cardFee']);
    $("#med-reissue-fee").val(bincard['cardReissueFee']);
    $("#med-service-charge").val(bincard['serviceCharge']);
    $("#card-wef-date").val(bincard['wefDate']);
    //val(bincard['wetDate']);
    if (bincard["wefDate"])
        $("#card-from-date").val(moment(bincard["wefDate"]).format('DD/MM/YYYY'));
    if (bincard["wetDate"])
        $("#card-to-date").val(moment(bincard["wetDate"]).format('DD/MM/YYYY'));
    $("#med-card-binder").val(bincard['binder'].binId);
    $("#med-card-code").val(bincard['id']);

    $('#medCardsModal').modal({
        backdrop: 'static',
        keyboard: true
    })
}




function addBinderRules(){
    $("#btn-add-bind-rules").click(function(){
        if($("#binder-sel-pk").val() != ''){
            $('#bind-rules-form').find("input[type=text],input[type=number],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden], textarea,select").val("");
            $("#bind-rules-binder").val($("#binder-sel-pk").val());
            $("#rule-mandatory").prop("checked", true);
            createChecksForSel($("#binder-sel-pk").val());
            $('#bindRulesModal').modal({
                backdrop: 'static',
                keyboard: true
            })
        }
        else{
            bootbox.alert("Select Binder to continue");
        }

    });

    var $classForm = $('#bind-rules-form');
    var validator = $classForm.validate();
    $('#saveBinderRules').click(function(){
        if (!$classForm.valid()) {
            return;
        }

        //var $btn = $(this).button('Saving');
        var data = {};
        $classForm.serializeArray().map(function(x){data[x.name] = x.value;});
        var url = "createBinderRules";
        console.log(data);
        var request = $.post(url, data );
        request.success(function(){
            new PNotify({
                title: 'Success',
                text: 'Record Created/Updated Successfully',
                type: 'success',
                styling: 'bootstrap3'
            });
            $('#binder-rules-tbl').DataTable().ajax.reload();
            $('#bind-rules-form').find("input[type=text],input[type=number],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden], textarea,select").val("");
            $('#bindRulesModal').modal('hide');
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
            //  $btn.button('reset');
        });
    });
}

function searchExclusions(){
    $("#searchexclusions").click(function(){
        addExclusionsSearch($("#exclusion-name-search").val());
    });
    $("#btn-add-bind-exclusions").click(function() {
        addExclusions2("");
    });
    $('#exclusion-type').on('change', function() {
        addExclusionsSearch("");
    });
}

function addExclusionsSearch(search){
    if($("#binder-sel-pk").val() != ''){
        //if($("#exclusion-type").val() != '') {
        if($("#exclusion-type").val() == 'A') {
            $("#exclusion-bind-code").val($("#binder-sel-pk").val());
            $.ajax({
                type: 'GET',
                url: 'unassignExclusions',
                dataType: 'json',
                data: {"bindCode": $("#binder-sel-pk").val(),"search":search},
                async: true,
                success: function (result) {
                    $("#exclusionsTbl tbody").each(function () {
                        $(this).remove();
                    });
                    for (var res in result) {
                        var markup = "<tr><td><input type='checkbox' name='record' id='" + result[res][0] + "'></td><td>" + result[res][1] + "</td><td>" + result[res][2] + "</td></tr>";
                        $("#exclusionsTbl").append(markup);
                    }
                    $('#exclusionsModal').modal({
                        backdrop: 'static',
                        keyboard: true
                    })
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    new PNotify({
                        title: 'Error',
                        text: jqXHR.responseText,
                        type: 'error',
                        styling: 'bootstrap3'
                    });
                }
            });
        }
        else {
            if($("#exclusion-type").val() == 'N') {
                $("#exclusion-bind-code").val($("#binder-sel-pk").val());
                $.ajax({
                    type: 'GET',
                    url: 'unassignNetworks',
                    dataType: 'json',
                    data: {"bindCode": $("#binder-sel-pk").val(),"search":search},
                    async: true,
                    success: function (result) {
                        $("#exclusionsTbl tbody").each(function () {
                            $(this).remove();
                        });
                        for (var res in result) {
                            var markup = "<tr><td><input type='checkbox' name='record' id='" + result[res][0] + "'></td><td>" + result[res][1] + "</td><td>" + result[res][2] + "</td></tr>";
                            $("#exclusionsTbl").append(markup);
                        }
                        $('#exclusionsModal').modal({
                            backdrop: 'static',
                            keyboard: true
                        })
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        new PNotify({
                            title: 'Error',
                            text: jqXHR.responseText,
                            type: 'error',
                            styling: 'bootstrap3'
                        });
                    }
                });
            }
            else {

                $("#exclusion-bind-code").val($("#binder-sel-pk").val());
                $.ajax({
                    type: 'GET',
                    url: 'unassignClauseExclusions',
                    dataType: 'json',
                    data: {"bindCode": $("#binder-sel-pk").val(),"search":search},
                    async: true,
                    success: function (result) {
                        $("#exclusionsTbl tbody").each(function () {
                            $(this).remove();
                        });
                        for (var res in result) {
                            var markup = "<tr><td><input type='checkbox' name='record' id='" + result[res][0] + "'></td><td>" + result[res][1] + "</td><td>" + result[res][2] + "</td></tr>";
                            $("#exclusionsTbl").append(markup);
                        }
                        $('#exclusionsModal').modal({
                            backdrop: 'static',
                            keyboard: true
                        })
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        new PNotify({
                            title: 'Error',
                            text: jqXHR.responseText,
                            type: 'error',
                            styling: 'bootstrap3'
                        });
                    }
                });
            }
        }
        //	}

    }
    else{
        bootbox.alert("Select Binder to continue");
    }


}

function addExclusions2(search){
    $("#btn-add-bind-exclusions").click(function(){
        $("#exclusion-type").val("A")
        if($("#binder-sel-pk").val() != ''){
            $("#exclusion-bind-code").val($("#binder-sel-pk").val());
            $.ajax({
                type: 'GET',
                url: 'unassignExclusions',
                dataType: 'json',
                data: {"bindCode": $("#binder-sel-pk").val(),"search":search},
                async: true,
                success: function (result) {
                    $("#exclusionsTbl tbody").each(function () {
                        $(this).remove();
                    });
                    for (var res in result) {
                        var markup = "<tr><td><input type='checkbox' name='record' id='" + result[res][0] + "'></td><td>" + result[res][1] + "</td><td>" + result[res][2] + "</td></tr>";
                        $("#exclusionsTbl").append(markup);
                    }
                    $('#exclusionsModal').modal({
                        backdrop: 'static',
                        keyboard: true
                    })
                },
                error: function (jqXHR, textStatus, errorThrown) {
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
            bootbox.alert("Select Binder to continue");
        }

    });


//	$("#btn-add-bind-loading").click(function(){
//		if($("#binder-sel-pk").val() != ''){
//			$("#loadings-bind-code").val($("#binder-sel-pk").val());
//			$.ajax({
//				type: 'GET',
//				url:  'unassignLoadings',
//				dataType: 'json',
//				data: {"bindCode": $("#binder-sel-pk").val()},
//				async: true,
//				success: function(result) {
//					$("#loadingsTbl tbody").each(function(){
//						$(this).remove();
//					});
//					for(var res in result){
//						var markup = "<tr><td><input type='checkbox' name='record' id='"+result[res][0]+"'></td><td>" + result[res][1] + "</td><td>" +  result[res][2] + "</td></tr>";
//						$("#loadingsTbl").append(markup);
//					}
//					$('#loadingsModal').modal({
//						backdrop: 'static',
//						keyboard: true
//					})
//				},
//				error: function(jqXHR, textStatus, errorThrown) {
//					new PNotify({
//						title: 'Error',
//						text: jqXHR.responseText,
//						type: 'error',
//						styling: 'bootstrap3'
//					});
//				}
//			});
//
//		}
//		else{
//			bootbox.alert("Select Binder to continue");
//		}
//
//	});
}
function addExclusions(){
    //$("#btn-add-bind-exclusions").click(function(){
    //   $("#exclusion-type").val("A")
    //	if($("#binder-sel-pk").val() != ''){
    //               $("#exclusion-bind-code").val($("#binder-sel-pk").val());
    //               $.ajax({
    //                   type: 'GET',
    //                   url: 'unassignExclusions',
    //                   dataType: 'json',
    //                   data: {"bindCode": $("#binder-sel-pk").val()},
    //                   async: true,
    //                   success: function (result) {
    //                       $("#exclusionsTbl tbody").each(function () {
    //                           $(this).remove();
    //                       });
    //                       for (var res in result) {
    //                           var markup = "<tr><td><input type='checkbox' name='record' id='" + result[res][0] + "'></td><td>" + result[res][1] + "</td><td>" + result[res][2] + "</td></tr>";
    //                           $("#exclusionsTbl").append(markup);
    //                       }
    //                       $('#exclusionsModal').modal({
    //                           backdrop: 'static',
    //                           keyboard: true
    //                       })
    //                   },
    //                   error: function (jqXHR, textStatus, errorThrown) {
    //                       new PNotify({
    //                           title: 'Error',
    //                           text: jqXHR.responseText,
    //                           type: 'error',
    //                           styling: 'bootstrap3'
    //                       });
    //                   }
    //               });
    //
    //	}
    //	else{
    //		bootbox.alert("Select Binder to continue");
    //	}
    //
    //});


    $("#btn-add-bind-loading").click(function(){
        if($("#binder-sel-pk").val() != ''){
            $("#loadings-bind-code").val($("#binder-sel-pk").val());
            $.ajax({
                type: 'GET',
                url:  'unassignLoadings',
                dataType: 'json',
                data: {"bindCode": $("#binder-sel-pk").val()},
                async: true,
                success: function(result) {
                    $("#loadingsTbl tbody").each(function(){
                        $(this).remove();
                    });
                    for(var res in result){
                        var markup = "<tr><td><input type='checkbox' name='record' id='"+result[res][0]+"'></td><td>" + result[res][1] + "</td><td>" +  result[res][2] + "</td></tr>";
                        $("#loadingsTbl").append(markup);
                    }
                    $('#loadingsModal').modal({
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
            bootbox.alert("Select Binder to continue");
        }

    });
}

function createExclusions(){

    if($("#binder-sel-pk").val() != ''){
        //if($("#exclusion-type").val() != '') {
        if($("#exclusion-type").val() == 'A') {
            $("#exclusion-bind-code").val($("#binder-sel-pk").val());
            $.ajax({
                type: 'GET',
                url: 'unassignExclusions',
                dataType: 'json',
                data: {"bindCode": $("#binder-sel-pk").val()},
                async: true,
                success: function (result) {
                    $("#exclusionsTbl tbody").each(function () {
                        $(this).remove();
                    });
                    for (var res in result) {
                        var markup = "<tr><td><input type='checkbox' name='record' id='" + result[res][0] + "'></td><td>" + result[res][1] + "</td><td>" + result[res][2] + "</td></tr>";
                        $("#exclusionsTbl").append(markup);
                    }
                    $('#exclusionsModal').modal({
                        backdrop: 'static',
                        keyboard: true
                    })
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    new PNotify({
                        title: 'Error',
                        text: jqXHR.responseText,
                        type: 'error',
                        styling: 'bootstrap3'
                    });
                }
            });
        }
        else {
            if($("#exclusion-type").val() == 'N') {
                $("#exclusion-bind-code").val($("#binder-sel-pk").val());
                $.ajax({
                    type: 'GET',
                    url: 'unassignNetworks',
                    dataType: 'json',
                    data: {"bindCode": $("#binder-sel-pk").val()},
                    async: true,
                    success: function (result) {
                        $("#exclusionsTbl tbody").each(function () {
                            $(this).remove();
                        });
                        for (var res in result) {
                            var markup = "<tr><td><input type='checkbox' name='record' id='" + result[res][0] + "'></td><td>" + result[res][1] + "</td><td>" + result[res][2] + "</td></tr>";
                            $("#exclusionsTbl").append(markup);
                        }
                        $('#exclusionsModal').modal({
                            backdrop: 'static',
                            keyboard: true
                        })
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        new PNotify({
                            title: 'Error',
                            text: jqXHR.responseText,
                            type: 'error',
                            styling: 'bootstrap3'
                        });
                    }
                });
            }
            else {

                $("#exclusion-bind-code").val($("#binder-sel-pk").val());
                $.ajax({
                    type: 'GET',
                    url: 'unassignClauseExclusions',
                    dataType: 'json',
                    data: {"bindCode": $("#binder-sel-pk").val()},
                    async: true,
                    success: function (result) {
                        $("#exclusionsTbl tbody").each(function () {
                            $(this).remove();
                        });
                        for (var res in result) {
                            var markup = "<tr><td><input type='checkbox' name='record' id='" + result[res][0] + "'></td><td>" + result[res][1] + "</td><td>" + result[res][2] + "</td></tr>";
                            $("#exclusionsTbl").append(markup);
                        }
                        $('#exclusionsModal').modal({
                            backdrop: 'static',
                            keyboard: true
                        })
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        new PNotify({
                            title: 'Error',
                            text: jqXHR.responseText,
                            type: 'error',
                            styling: 'bootstrap3'
                        });
                    }
                });
            }
        }
        //	}

    }
    else{
        bootbox.alert("Select Binder to continue");
    }


}

function saveBinderLoadings(){
    var arr = [];
    $("#saveLoadingsBtn").click(function(){
        $("#loadingsTbl tbody").find('input[name="record"]').each(function(){
            if($(this).is(":checked")){
                arr.push($(this).attr("id"));
            }
        });
        if(arr.length==0){
            bootbox.alert("No Loadings Selected to attach..");
            return;
        }


        var $currForm = $('#loadings-det-form');
        var currValidator = $currForm.validate();
        if (!$currForm.valid()) {
            return;
        }

        var data = {};
        $currForm.serializeArray().map(function(x) {
            data[x.name] = x.value;
        });
        var url = "createBinderLoadings";
        data.ailments = arr;


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
                $('#binder-loadings-tbl').DataTable().ajax.reload();
                $('#loadingsModal').modal('hide');
                arr=[];
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


function saveBinderExclusions(){
    var arr = [];
    $("#saveExclusionsBtn").click(function(){
        $("#exclusionsTbl tbody").find('input[name="record"]').each(function(){
            if($(this).is(":checked")){
                arr.push($(this).attr("id"));
            }
        });
        if(arr.length==0){
            bootbox.alert("No Exclusions Selected to attach..");
            return;
        }


        var $currForm = $('#exclusions-det-form');
        var currValidator = $currForm.validate();
        if (!$currForm.valid()) {
            return;
        }

        var data = {};
        $currForm.serializeArray().map(function(x) {
            data[x.name] = x.value;
        });
        console.log($("#exclusion-type").val());
        if($("#exclusion-type").val() == 'A'){
            var url = "createBinderExclusions";
            data.ailments = arr;
        }
        else {
            if($("#exclusion-type").val() == 'N') {
                var url = "createBinderNetExclusions";
                data.claimNetworks = arr;
            }
            else {
                var url = "createBinderClauseExclusions";
                data.clauseExclusions = arr;
            }
        }



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
                $('#binder-exclusions-tbl').DataTable().ajax.reload();
                $('#exclusionsModal').modal('hide');
                arr=[];
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

function addProviderContracts() {
    $("#btn-add-providers").on("click", function () {
        $("#provtype-code").val("");
        $("#provtype-contract-code").val("");
        $("#contract-no").text("");
        $("#provider-id").val("");
        $("#provider-name").text("");
        $("#provider-type").text("");
        $("#contract-type").val("");
        $("#from-date").val("");
        $("#contract-to-date").val("");
        $("#contract-status").val("");
        $("#contr-notes").val("");
        if ($("#binder-sel-pk").val() != '') {
            console.log("testing here="+$("#binder-sel-pk").val())



            providerLov();
            $('.provider-namelov').show();
            $('.provider-diaplay').hide();

            //$('#prov-contract-form').find("input[type=text],input[type=date],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden],input[type=number], textarea,select").val("");
            $("#contrct-bind-code").val($("#binder-sel-pk").val());
            $('#provContractModal').modal('show');

        }
        else {
            bootbox.alert("Select Binder to continue");
        }
        return;
    });

    var $classForm = $('#prov-contract-form');
    var validator = $classForm.validate();
    $('#saveProvderContractsBtn').click(function(){
        if (!$classForm.valid()) {
            return;
        }
        //var $btn = $(this).button('Saving');
        var data = {};
        $classForm.serializeArray().map(function(x){data[x.name] = x.value;});
        var url = "createBinderProviderContract";
        var request = $.post(url, data );
        request.success(function(){
            new PNotify({
                title: 'Success',
                text: 'Record created/updated Successfully',
                type: 'success',
                styling: 'bootstrap3'
            });
            $('#providerContractTbl').DataTable().ajax.reload();
            validator.resetForm();
            $('#prov-contract-form').find("input[type=text],input[type=number],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden], textarea,select").val("");
            $('#provContractModal').modal('hide');
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
            //  $btn.button('reset');
        });
    });
}
function addProviders() {

    $("#btn-add-providers").click(function () {
        if ($("#binder-sel-pk").val() != '') {
            $("#provider-bind-code").val($("#binder-sel-pk").val());
            $.ajax({
                type: 'GET',
                url: 'unassignProviders',
                dataType: 'json',
                data: {"bindCode": $("#binder-sel-pk").val()},
                async: true,
                success: function (result) {
                    $("#providerTbl tbody").each(function () {
                        $(this).remove();
                    });
                    for (var res in result) {
                        var markup = "<tr><td><input type='checkbox' name='record' id='" + result[res][0] + "'></td><td>" + result[res][1] + "</td><td>" + result[res][2] + "</td></tr>";
                        $("#providerTbl").append(markup);
                    }
                    $('#providersModal').modal({
                        backdrop: 'static',
                        keyboard: true
                    })
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    new PNotify({
                        title: 'Error',
                        text: jqXHR.responseText,
                        type: 'error',
                        styling: 'bootstrap3'
                    });
                }
            });

        }
        else {
            bootbox.alert("Select Binder to continue");
        }

    });

}



function saveBinderProviders(){
    var arr = [];
    $("#saveProvidersBtn").click(function(){
        $("#providerTbl tbody").find('input[name="record"]').each(function(){
            if($(this).is(":checked")){
                arr.push($(this).attr("id"));
            }
        });
        if(arr.length==0){
            bootbox.alert("No Loadings Selected to attach..");
            return;
        }


        var $currForm = $('#provider-det-form');
        var currValidator = $currForm.validate();
        if (!$currForm.valid()) {
            return;
        }

        var data = {};
        $currForm.serializeArray().map(function(x) {
            data[x.name] = x.value;
        });
        var url = "createBinderProviders";
        data.providers = arr;


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
                $('#binder-provider-tbl').DataTable().ajax.reload();
                $('#providersModal').modal('hide');
                arr=[];
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

function createProvidersContracts(){
    var url = "providerbindercontracts/0";
    if($("#binder-sel-pk").val() != ''){
        url = "providerbindercontracts/"+$("#binder-sel-pk").val();
    }
    var currTable = $('#providerContractTbl').DataTable( {
        "processing": true,
        "serverSide": true,
        autoWidth: true,
        "ajax": {
            'url': url,
        },
        lengthMenu: [ [10], [10] ],
        pageLength: 10,
        destroy: true,
        "columns": [
            { "data": "contractNo" },
            { "data": "contractType" ,
                "render": function ( data, type, full, meta ) {
                    if(full.contractType==="P")
                        return "PERMANENT";
                    else return "";
                }},
            {
                "data": "serviceProviders",
                "render": function ( data, type, full, meta ) {
                    if(full.serviceProviders)
                        return full.serviceProviders.name;
                    else return "";
                }

            },
            {
                "data": "serviceProviders",
                "render": function ( data, type, full, meta ) {
                    if(full.serviceProviders)
                        return full.serviceProviders.serviceProviderTypes.desc;
                    else return "";
                }

            },
            { "data": "status",
                "render": function ( data, type, full, meta ) {
                    if(full.status==="A")
                        return "ACTIVE";
                    else return "INACTIVE";
                }},
            { "data": "issueDate",
                "render": function ( data, type, full, meta ) {
                    return moment(full.issueDate).format('DD/MM/YYYY');
                }
            },
            { "data": "wefDate",
                "render": function ( data, type, full, meta ) {
                    return moment(full.wefDate).format('DD/MM/YYYY');
                }
            },
            { "data": "wetDate",
                "render": function ( data, type, full, meta ) {
                    return moment(full.wetDate).format('DD/MM/YYYY');
                }
            },
            {
                "data": "spcId",
                "render": function ( data, type, full, meta ) {
                    if(full.binder.binStatus===null)
                        return '<button type="button" class="btn btn-success btn btn-info btn-xs" type="button" data-contract='+encodeURI(JSON.stringify(full)) + ' onclick="editContract(this);"><i class="fa fa-pencil-square-o"></button>';
                    else
                        return '<button type="button" class="btn btn-success btn btn-info btn-xs" type="button" data-contract='+encodeURI(JSON.stringify(full)) + ' onclick="editContract(this);" disabled><i class="fa fa-pencil-square-o"></button>';

                }

            },
            {
                "data": "spcId",
                "render": function ( data, type, full, meta ) {
                    if(full.binder.binStatus===null)
                        return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-contract='+encodeURI(JSON.stringify(full)) + ' onclick="deleteContract(this);"><i class="fa fa-trash-o"></button>';
                    else
                        return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-contract='+encodeURI(JSON.stringify(full)) + ' onclick="deleteContract(this);" disabled><i class="fa fa-trash-o"></button>';

                }

            },
        ]
    } );
    return currTable;
}
function providerLov(){
    if($("#provider-frm").filter("div").html() != undefined)
    {
        Select2Builder.initAjaxSelect2({
            containerId : "provider-frm",
            sort : 'name',
            change: function(e, a, v){
                $("#provider-type").text(e.added.serviceProviderTypes.desc);
                $("#provider-id").val(e.added.mspId);
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
            id: "mspId",
            width:"250px",
            placeholder:"Select Service Provider"

        });
    }
}


function updateBinderClauses(){
    var $classForm = $('#binder-clause-form');
    var validator = $classForm.validate();
    $('#saveBinderClauseBtn').click(function(){
        if (!$classForm.valid()) {
            return;
        }

        //  var $btn = $(this).button('Saving');
        var data = {};
        $classForm.serializeArray().map(function(x){data[x.name] = x.value;});
        var url = "updateBinderClauses";
        var request = $.post(url, data );
        request.success(function(){
            new PNotify({
                title: 'Success',
                text: 'Record Created/Updated Successfully',
                type: 'success',
                styling: 'bootstrap3'
            });
            $('#bindclausesList').DataTable().ajax.reload(null,false);
            $('#binder-clause-form').find("input[type=text],input[type=number],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden], textarea,select").val("");
            $('#editBinderclauseModal').modal('hide');
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
            //    $btn.button('reset');
        });
    });
}


function uploadSubLimits(){
    var $form = $("#limits-upload-form");
    var validator = $form.validate();
    $('form#limits-upload-form')
        .submit( function( e ) {
            e.preventDefault();
            if (!$form.valid()) {
                return;
            }
            var data = new FormData( this );
            data.append( 'file', $( '#avatar' )[0].files[0] );
            $.ajax( {
                url: 'importSubLimits',
                type: 'POST',
                data: data,
                processData: false,
                contentType: false,
                success: function (s ) {
                    new PNotify({
                        title: 'Success',
                        text: 'File Uploaded Successfully',
                        type: 'success',
                        styling: 'bootstrap3'
                    });
                    $('#coverSubLimitsTbl').DataTable().ajax.reload();
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

function updateBinderLoadings(){
    var $classForm = $('#binder-loading-form');
    var validator = $classForm.validate();
    $('#saveBinderLoadingBtn').click(function(){
        if (!$classForm.valid()) {
            return;
        }

        // var $btn = $(this).button('Saving');
        var data = {};
        $classForm.serializeArray().map(function(x){data[x.name] = x.value;});
        var url = "updateBinderLoadings";
        var request = $.post(url, data );
        request.success(function(){
            new PNotify({
                title: 'Success',
                text: 'Record Created/Updated Successfully',
                type: 'success',
                styling: 'bootstrap3'
            });
            $('#binder-loadings-tbl').DataTable().ajax.reload();
            $('#binder-loading-form').find("input[type=text],input[type=number],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden], textarea,select").val("");
            $('#editBinderLoadingModal').modal('hide');
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
            //   $btn.button('reset');
        });
    });
}


function updateBinderExclusions(){
    var $classForm = $('#edit-exclusion-form');
    var validator = $classForm.validate();
    $('#saveBinderExclusion').click(function(){
        if (!$classForm.valid()) {
            return;
        }

        // var $btn = $(this).button('Saving');
        var data = {};
        $classForm.serializeArray().map(function(x){data[x.name] = x.value;});
        var url = "updateBinderExclusion";
        var request = $.post(url, data );
        request.success(function(){
            new PNotify({
                title: 'Success',
                text: 'Record Created/Updated Successfully',
                type: 'success',
                styling: 'bootstrap3'
            });
            $('#binder-exclusions-tbl').DataTable().ajax.reload();
            $('#edit-exclusion-form').find("input[type=text],input[type=number],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden], textarea,select").val("");
            $('#editExclusionModal').modal('hide');
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
            //   $btn.button('reset');
        });
    });
}


function addSubRequiredDocs() {

    $("#btn-add-reqd-docs").click(function () {
        if ($("#binder-det-code-pk").val() != '') {
            $("#reqDocs-det-code").val($("#binder-det-code-pk").val());
            $.ajax({
                type: 'GET',
                url: 'subRequiredDocs',
                dataType: 'json',
                data: {"subCode": $("#binder-det-sub-code").val(),"detCode": $("#binder-det-code-pk").val()},
                async: true,
                success: function (result) {
                    $("#reqDocsTbl tbody").each(function () {
                        $(this).remove();
                    });
                    for (var res in result) {
                        var markup = "<tr><td><input type='checkbox' name='record' id='" + result[res][0] + "'></td><td>" + result[res][1] + "</td><td>" + result[res][2] + "</td></tr>";
                        $("#reqDocsTbl").append(markup);
                    }
                    $('#reqDocsModal').modal({
                        backdrop: 'static',
                        keyboard: true
                    })
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    new PNotify({
                        title: 'Error',
                        text: jqXHR.responseText,
                        type: 'error',
                        styling: 'bootstrap3'
                    });
                }
            });

        }
        else {
            bootbox.alert("Select Binder Detail to continue");
        }

    });

}


function saveBinderReqDocs(){
    var arr = [];
    $("#savereqDocsBtn").click(function(){
        $("#reqDocsTbl tbody").find('input[name="record"]').each(function(){
            if($(this).is(":checked")){
                arr.push($(this).attr("id"));
            }
        });
        if(arr.length==0){
            bootbox.alert("No Required Docs Selected to attach..");
            return;
        }


        var $currForm = $('#reqDocs-det-form');
        var currValidator = $currForm.validate();
        if (!$currForm.valid()) {
            return;
        }

        var data = {};
        $currForm.serializeArray().map(function(x) {
            data[x.name] = x.value;
        });
        var url = "createBinderReqrdDocs";
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
                $('#reqDocsList').DataTable().ajax.reload();
                $('#reqDocsModal').modal('hide');
                arr=[];
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



function uploadPremRatesFile(){
    var $form = $("#prem-rate-upload-form");
    var validator = $form.validate();
    $('form#prem-rate-upload-form')
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
            data.append( 'file', $( '#file-avatar' )[0].files[0] );
            $.ajax( {
                url: 'createPremRatedTbl',
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
                    $('#prem-rate-upload-form').find("input[type=text],input[type=mobileNumber],input[file],input[type=email],input[type=password],input[type=hidden],input[type=number], textarea,select").val("");
                    var $el = $('#file-avatar');
                    $el.wrap('<form>').closest('form').get(0).reset();
                    $el.unwrap();
                    $('#prem-rates-table').DataTable().ajax.reload();
                },
                error: function(jqXHR, textStatus, errorThrown){
                    $('#myPleaseWait').modal('hide');
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

function createChecksForSel(bindCode){
    if($("#check-code").filter("div").html() != undefined)
    {
        Select2Builder.initAjaxSelect2({
            containerId : "check-code",
            sort : 'checkId',
            change: function(e, a, v){
                $("#check-id").val(e.added.prodCheckId);
            },
            formatResult : function(a)
            {
                return a.checkName;
            },
            formatSelection : function(a)
            {
                return a.checkName;
            },
            initSelection: function (element, callback) {
                var code = $("#check-id").val();
                var name = $("#check-name").val();
                var data = {checkName:name,checkId:code};
                callback(data);
            },
            id: "checkId",
            placeholder:"Select Rule",
            params: {bindCode: bindCode}
        });
    }
}

var model = {
    product: {
        proCode:"",
    },
    account:{
        acctId:"",
    },
    subclass:{
        subcode:"",
    },
    covertype:{
        covId:""
    }
};