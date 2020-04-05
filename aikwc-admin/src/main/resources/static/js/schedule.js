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