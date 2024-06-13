$(function(){

	$(document).ready(function() {
		
		$("#newSequency").on("click", function(){
			$('#seqform').find("input[type=text],input[type=number],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden],input[type=number], textarea,select").val("");
		});	
		
		createSequenceTable();
		saveUpdateSequence();
		
	});
});

function saveUpdateSequence(){
	var $currForm = $('#seqform');
	var currValidator = $currForm.validate();
	
	
	$('#saveSequencyBtn').click(function(){
		if (!$currForm.valid()) {
			return;
		}
		var $btn = $(this).button('Saving');
		var data = {};
		$currForm.serializeArray().map(function(x){data[x.name] = x.value;});
		var url = "createSequence";
        var request = $.post(url, data );
        request.success(function(){
        	new PNotify({
                title: 'Success',
                text: 'Record created/updated Successfully',
                type: 'success',
                styling: 'bootstrap3'
            });
			$('#seqList').DataTable().ajax.reload();
			currValidator.resetForm();
			$('#seqform').find("input[type=text],input[type=number],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden], textarea,select").val("");
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

function editSequence(button){
	var sequence = JSON.parse(decodeURI($(button).data("sequences")));
	$("#seq-code").val(sequence["seqId"]);
	$("#prefix-name").val(sequence["seqPrefix"]);
	$("#last-number").val(sequence["lastNumber"]);
	$("#next-number").val(sequence["nextNumber"]);
	$("#sel3").val(sequence["seqType"]);
	$("#sel2").val(sequence["transType"]);
}

function confirmSequenceDelete(button){
	var sequence = JSON.parse(decodeURI($(button).data("sequences")));
	bootbox.confirm("Are you sure want to delete "+sequence["seqPrefix"]+"?", function(result) {
		 if(result){
	    	  $.ajax({
			        type: 'GET',
			        url:  'deleteSequence/' + sequence["seqId"],
			        dataType: 'json',
			        async: true,
			        success: function(result) {
			        	new PNotify({
                            title: 'Success',
                            text: 'Record Deleted Successfully',
                            type: 'success',
                            styling: 'bootstrap3'
                        });
			        	$('#seqList').DataTable().ajax.reload();
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


function createSequenceTable(){
	var url = "sequences";
	  var currTable = $('#seqList').DataTable( {
			"processing": true,
			"serverSide": true,
			"ajax": url,
			lengthMenu: [ [10, 20, 30], [10, 20, 30] ],
			pageLength: 10,
			destroy: true,
			"columns": [
				{ "data": "seqPrefix" },
				{ "data": "lastNumber" },
				{ "data": "nextNumber" },
				{ "data": "seqType" ,
				   "render": function ( data, type, full, meta ) {
						if(full.seqType){
							if(full.seqType==='PAA') return "All Branches";
							else if(full.seqType==='PPA') return "Per Product Per Branch";
							else if(full.seqType==='PAY') return "All Branches Per Year";
							else if(full.seqType==='PBA') return "Per Branch Perpetual";
							else if(full.seqType==='PBY') return "Per Branch Per year";
						}
				    }
				},
				{ "data": "transType",
					"render": function ( data, type, full, meta ) {
						if(full.transType){
							if(full.transType==='C') return "Client Definition Sequence";
							else if(full.transType==='A') return "Agent Definition Sequence";
							else if(full.transType==='P') return "Policies Creation Sequence";
							else if(full.transType==='R') return "Receipts Creation Sequence";
							else if(full.transType==='D') return "Debit Note Creation Sequence";
							else if(full.transType==='E') return "Endorsements Creation Sequence";
							else if(full.transType==='CL') return "Claim Trans Creation Sequence";
							else if(full.transType==='Q') return "Quote Trans Creation Sequence";
							else if(full.transType==='M') return "Medical Membership Sequence";
							else if(full.transType==='AD') return "Admin Fee Trans Sequence";
							else if(full.transType==='PR') return "Proposal Creation Sequence";
						}
					}
				},
				{ 
					"data": "seqId",
					"render": function ( data, type, full, meta ) {
						return '<button type="button" class="btn btn-success btn btn-info btn-xs" type="button" data-sequences='+encodeURI(JSON.stringify(full)) + '  onclick="editSequence(this);"><i class="fa fa-pencil-square-o"></button>';
					}

				},
				{ 
					"data": "seqId",
					"render": function ( data, type, full, meta ) {
						return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-sequences='+encodeURI(JSON.stringify(full)) + '  onclick="confirmSequenceDelete(this);"><i class="fa fa-trash-o"></button>';
					}

				},
			]
		} );
	  return currTable;
}