/**
 * Created by HP on 7/26/2017.
 */
var APP = APP || {};
var WORKFLOWS = APP.WORKFLOWS || {};

WORKFLOWS.getWorkflows = function() {
    $('#doc-name option').remove();
    $('#doc-name').append($('<option>', {
        value: "",
        text : "Select Document Type"
    }));
    $.ajax({
        type: 'GET',
        url: 'doctypes',
        dataType: 'json',
        async: true,
        success: function (result) {
             for(var res in result){
                $('#doc-name').append($('<option>', {
                    value: result[res].docTypeName,
                    text : result[res].docTypeValue
                }));
            }
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

WORKFLOWS.updateDiagram = function(docType){
    var rand =Math.floor((Math.random() * 100000000) + 1);
    var url = SERVLET_CONTEXT + '/protected/workflow/'+docType;
    console.log(url);
    url += ('?rand=' + rand);
    $('#proc-main-diagram').attr('src', url);
}

WORKFLOWS.uploadProcessDoc = function(){

    $('#updateWFDoc').click(function(){
        var $accTypesFrm= $('#doc-upload-form');
        var validator = $accTypesFrm.validate();
        if (!$accTypesFrm.valid()) {
            return;
        }
        $('#myPleaseWait').modal({
            backdrop: 'static',
            keyboard: true
        });
        var $btn = $(this).button('Saving');
        var data = {};
        $accTypesFrm.serializeArray().map(function(x){data[x.name] = x.value;});
        var url = "updateWFDocument";
        var request = $.post(url, data );
        request.success(function(){
            $('#myPleaseWait').modal('hide');
            new PNotify({
                title: 'Success',
                text: 'Process Updated Successfully...',
                type: 'success',
                styling: 'bootstrap3'
            });
            WORKFLOWS.getWorkflows();
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


    $(document).ready(function () {

        WORKFLOWS.getWorkflows();
        WORKFLOWS.uploadProcessDoc();

        $("#doc-name").on('change', function(){
            WORKFLOWS.updateDiagram($(this).val());
        });

    });
