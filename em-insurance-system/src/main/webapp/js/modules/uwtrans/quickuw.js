$(function() {


    $(document).ready(function () {

        $("#vehicle-details").hide();

        $("#btn-search-ntsa").on('click',function () {
            if($("#reg-no").val()){
                  getNtsaDetails($("#reg-no").val());
                  populatePaymentModes();
                  populateBinderLov();
            }
            else{
                bootbox.alert("Enter Registration Number");
                return;
            }
        });

        $("#btn-clear").on('click', function () {
            $("#vehicle-details").hide();
        });



    });

    function getNtsaDetails(regno){
        $('#myPleaseWait').modal({
            backdrop: 'static',
            keyboard: true
        });
        $.ajax({
            type: 'GET',
            url: 'getNtsaDetails/'+regno,
            dataType: 'json',
            async: true,
            success: function (result) {
                $('#myPleaseWait').modal('hide');
                $("#vehicle-details").show();
                $("#search-reg-no").text(result.vehicle.regNo);
                $("#reg-date").text(result.vehicle.registrationDate);
                $("#chasis-no").text(result.vehicle.ChassisNo);
                $("#body-type").text(result.vehicle.bodyType);
                $("#car-make").text(result.vehicle.carMake);
                $("#body-color").text(result.vehicle.bodyColor);
                $("#engine-no").text(result.vehicle.engineNumber);
                $("#yom").text(result.vehicle.yearOfManufacture);
                $("#car-model").text(result.vehicle.carModel);
                $("#log-book").text(result.vehicle.logbookNumber);
                var owners = result.owner;
                // console.log(owners);
                $("#owner_form_tbl tbody").each(function () {
                    $(this).remove();
                });
                owners.forEach(function(item){
                    var markup = "<tr><td>"+item.firstname+"</td><td>"+item.pin+"</td><td>"+item.address+"</td><td>"+item.code+"</td><td>"+item.town+"</td><td>"+item.telno+"</td></tr>";
                    $("#owner_form_tbl").append(markup);
                })

            },
            error: function (jqXHR, textStatus, errorThrown) {
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


    var populatePaymentModes = function(){
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
    };

    var populateBinderLov = function(){
        if($("#binder-frm").filter("div").html() != undefined)
        {
            Select2Builder.initAjaxSelect2({
                containerId : "binder-frm",
                sort : 'binName',
                change: function(e, a, v){
                    console.log(e.added);
                    // populateImportSubCovers(e.added.binId);
                    $("#binder-id").val(e.added.binId);
                    $("#risk-binder-code").val(e.added.binId);
                    $("#risk-bind-code").val(e.added.binId);
                    $("#pol-ins-comp").text(e.added.name);
                    $("#pol-prod-name").text(e.added.proDesc);
                    $("#product-id").val(e.added.proCode);
                    $("#pol-agent-id").val(e.added.acctId);
                    $("#client-pol-no").val(e.added.binPolNo);
                    $("#risk-binder").text(e.added.binName);
                    populateSubclassLov();
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
                params: {bindType: "B"},
                placeholder:"Select Contract"

            });
        }
    };

    var populateSubclassLov = function(){
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
                params: {bindCode: $("#risk-bind-code").val()},
                placeholder:"Select Sub Class"

            });
        }
    };

    var populateCoverTypesLov = function(){
        if($("#covertypes-frm").filter("div").html() != undefined)
        {
            Select2Builder.initAjaxSelect2({
                containerId : "covertypes-frm",
                sort : 'detId',
                change: function(e, a, v){
                    $("#risk-cov-code").val(e.added.covId);
                    $("#binder-det-id").val(e.added.detId);
                    getCommissionRate(e.added.detId);
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
    };

});