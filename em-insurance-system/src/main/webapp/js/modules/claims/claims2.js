/**
 * Created by peter on 3/5/2017.
 */

$(function() {

    $(document).ready(function () {

        $(".datepicker-input").each(function() {
            $(this).datetimepicker({
                format: 'DD/MM/YYYY'
            });

        });

        if($("#loss-date").val() != ''){
            var dt = moment($("#loss-date").val()).format('DD/MM/YYYY');
            selectRiskLov(dt);
        }


        changeLossDate();
        selectNextReviewLov();
        selectClmActivity();
        addClmPeril();
        addSelectedPeril();

        $('#self-as-clmnt').change(function(){
            if(this.checked){
                $("#clmant-def").select2("readonly", true);
            }
            else{
                $("#clmant-def").select2("readonly", false);
            }

        });

    });
});



function addClmPeril(){
    $("#add-peril-btn").on('click',function(){
        $('#new-peril-form').find("input[type=text],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden],input[type=number], textarea").val("");
        $('#new-peril-form').find("input[type=checkbox]").attr("checked", false);
        selectClaimantLov();
        $("#clm-estimate").number( true, 2 );
        $('#perilTransModal').modal({
            backdrop : 'static',
            keyboard : true
        })
    });
}



function addSelectedPeril(){
    $("#btn-add-peril-selected").on('click', function(){
        if ($("#peril-code").val() === '') {
            bootbox.alert("Select Peril");
            return;
        }
        var count = $("#peril-table-id > tbody > tr").length;
        var data = "";
        var checked = "off";
        if ($("#self-as-clmnt").is(":checked")) {
            checked = "on";
        } else checked = "off";
        data += "<tr>" +
            "<td>" +" <input type='hidden' name='perils[" + count + "].selfAsClaimant' value='" +checked+ "'>"+
            "     <input type='hidden' name='perils[" + count + "].claimantCode' value='" + $("#clmt-code").val() + "'>" + $("#clmt-name").val() +
            "</td>" +
            "<td>" +
            "     <input type='hidden' name='perils[" + count + "].perilCode' value='" + $("#peril-code").val() + "'>" + $("#peril-name").val() +
            "</td>" +
            "<td>" +
            "     <input type='hidden' name='perils[" + count + "].perilEstimate' value='" + $("#clm-estimate").val() + "'>" + $("#clm-estimate").val() +
            "</td> " +
            "<td> " +
            "      <input type='button' class='hyperlink-btn btn btn-danger' value='Delete' onclick='delRow(this)'>" +
            "</td>" +
            "</tr>";
        console.log(data);
        $('#peril-table-id').append(data);
        $('#perilTransModal').modal('hide');
    });
}

function changeLossDate(){
    $('#los-date').on('dp.change', function (ev) {
        var curDate = ev.date;
        var dt = moment(curDate).format('DD/MM/YYYY');
        selectRiskLov(dt);
    });
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


function selectRiskPerils(riskId){
    if($("#peril-def").filter("div").html() != undefined)
    {
        Select2Builder.initAjaxSelect2({
            containerId : "peril-def",
            sort : 'bindPerilCode',
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



function getPolicyBalance(polId){

        $.ajax( {
            url: 'claimBalance/'+polId,
            type: 'GET',
            processData: false,
            contentType: false,
            success: function (s ) {
                $("#pol-client-balance").text(s.clientBalance);
                $("#pol-ins-balance").text(s.insBalance);

            },
            error: function(xhr, error){
                //bootbox.alert(xhr.responseText);
            }
        });

}

function selectRiskLov(lossdate){
    if($("#risk-frm").filter("div").html() != undefined)
    {
        Select2Builder.initAjaxSelect2({
            containerId : "risk-frm",
            sort : 'riskShtDesc',
            change: function(e, a, v){
                $("#risk-id").val(e.added.riskId);
                $("#risk-desc").val(e.added.riskDesc);
                $("#risk-sht-desc").val(e.added.riskShtDesc)
                selectRiskPerils(e.added.riskId);
                getPolicyBalance(e.added.polId);
            },
            formatResult : function(a)
            {
                return "Risk: "+a.riskShtDesc +" Policy: "+ a.polno+" Insured: "+ a.clientname
            },
            formatSelection : function(a)
            {
                return a.riskShtDesc+" "+a.riskDesc
            },
            initSelection: function (element, callback) {
                var code = $("#risk-id").val();
                var name = $("#risk-desc").val();
                var shtdesc = $("#risk-sht-desc").val();
                var data = {riskDesc:name,riskShtDesc:shtdesc,riskId:code};
                callback(data);
            },
            id: "riskId",
            params: {lossDate: lossdate},
            placeholder:"Select A Risk",
        });
    }
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
