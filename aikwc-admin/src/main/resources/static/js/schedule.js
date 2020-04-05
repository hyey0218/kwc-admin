$(document).ready(function(){
	//Web Sockect
	var sock = new SockJS("/ws/getStatus");
	
	sock.onopen = function(){
		console.log("open")
	}
	
	sock.onmessage = function(message) { 
		let jObj = JSON.parse(message.data);
		if(jObj.taskCnt != undefined){
			taskProgress(Number(jObj.taskCnt));
		}
	}
	
	
	taskProgress(Number($("#curTask").val()));
});

function changeType(obj){
	let type = $(obj).val();
	let start = 0;
	if(type=='Y'){
		start = 2017;
	}else if(type=='P'){
		start = 1;
	}
	
	$("#selectStart > option:gt(0)").remove();
	$("#selectEnd > option:gt(0)").remove();
	for(let i =0;i<10;i++){
		$("#selectStart").append($("<option>",{'value':start+i}).text(start+i));
		$("#selectEnd").append($("<option>",{'value':start+i}).text(start+i));
	}
}


function deleteSchedule(pk){
	let isDel = confirm("삭제하시겠습니까?");
	var $form = $("<form>");
	if(isDel){
		dummyFormSubmit('/simulator/schedule/delete', 'POST', {'pk' : pk});
	}
	
}

function templeateDown(){
	window.location.assign('/manage/filedown');
}


function uploadExcel() {
	var form = $('#frm-upload');
    
    //formdata 생성
    var formData = new FormData(form[0]);
  	console.log(formData);
    $.ajax({
        url : '/manage/uploadMultiFile',
        type : 'POST',
        data : formData,
        processData : false,
        contentType : false,
        beforeSend : function() {
            console.log('jQeury ajax form submit beforeSend');
        },
        success : function(data) {
            console.log('jQeury ajax form submit success');
            alert(data.result);
        }, 
        error : function(data) {
            console.log('jQeury ajax form submit error');
        },
        complete : function() {
            console.log('jQeury ajax form submit complete');
        }
    });//end ajax
    
}
