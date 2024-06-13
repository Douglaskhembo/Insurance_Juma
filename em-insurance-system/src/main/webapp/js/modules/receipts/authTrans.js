/**
 * Created by peter on 3/28/2017.
 */
var UTILITIES = UTILITIES || {};
$(function(){

    $(document).ready(function() {

        getAuthTrans();

    })
});


function getAuthTrans() {
    var url = "accounttrans";
    var currTable = $('#accounts-tbl')
        .DataTable(
            {
                "processing" : true,
                "serverSide" : true,
                "ajax" : url,
                lengthMenu : [ [ 20, 30, 40, 50 ], [ 20, 30, 40, 50 ] ],
                pageLength : 20,
                "scrollX": true,
                destroy : true,
                "columns" : [
                    {
                        "data" : "transDate",
                        "render" : function(data, type, full, meta) {
                            return moment(full.transDate).format('DD/MM/YYYY');
                        }

                    },
                    {
                        "data" : "controlAcc"
                    },
                    {
                        "data" : "refNo"
                    },
                    {
                        "data" : "transType"
                    },
                    {
                        "data" : "transdc",
                        "render" : function(data, type, full, meta) {
                            if(full.transdc==="C")
                            return "Credit";
                            else if(full.transdc==="D")
                                return "Debit";
                        }
                    },
                    {
                        "data" : "amount",
                        "render" : function(data, type, full, meta) {
                            return UTILITIES.currencyFormat(full.amount);
                        }
                    },
                    {
                        "data" : "netAmount",
                        "render" : function(data, type, full, meta) {
                            return UTILITIES.currencyFormat(full.netAmount);
                        }
                    },
                    {
                        "data" : "payeeName"
                    },
                    {
                        "data" : "transno",
                        "render" : function(data, type, full, meta) {
                            return '<button type="button" class="btn btn-success btn btn-info btn-xs" type="button" data-audits='+encodeURI(JSON.stringify(full)) + '  onclick="authTrans(this);">Authorize</button>';

                        }
                    },

                ]
            });

    return currTable;
}


function authTrans(button){
    var audits = JSON.parse(decodeURI($(button).data("audits")));
    bootbox.confirm("Authorize Selected Transaction?", function(result) {
        if (result) {
            $.ajax({
                type: 'GET',
                url:  'authAccountTrans/' + audits['transno'],
                dataType: 'json',
                async: true,
                success: function(result) {
                    new PNotify({
                        title: 'Success',
                        text: 'Transaction Authorized Successfully',
                        type: 'success',
                        styling: 'bootstrap3'
                    });

                    $('#accounts-tbl').DataTable().ajax.reload();
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