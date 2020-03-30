var _dataTab;
$(document).ready(function(){
	simulatorList();
	
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
		
		if(jObj.collectors){
			let collectors = jObj.collectors;
			$(collectors).each(function(i,j){
				//1. button
				let obtn = $("#statusBtn_"+j.pk);
				$(obtn).children().remove();
				$(obtn).append(runningSatus(j.status,j.pk))
				$(obtn).prev().val(''); // text
				
				//2. status
				let otd = $("#statusTd_"+j.pk);
				$(otd).text(j.status);
			})
		}
	}

})

function simulatorList(){
	let requestUrl = "/simulator/collectorList";
	let jObj = {};
	jObj.agencyNo = _agencyNo;
	let res = getJSONAjaxMethod(requestUrl, "POST", jObj);
	let retJson = res.result;
	if(retJson){
		var $tbody = $("#simList");
		$tbody.children().remove();
		$(retJson).each(function(i,j){
			let $tr = $("<tr>");
			$tr.append(
				$("<th>").text(i+1))
				.append(
				$("<td>").text(j.toSite.group.name))
				.append(
				$("<td>").text(j.toSite.name))
				.append(
				$("<td>").text(j.code))
				.append(
				$("<td>").text(j.name))
				.append(
				$("<td>", {'style':"max-width:75px;"}).append(
					$("<div>",{'class':"input-group input-group-sm"}).append(
						$("<input>",{'type':"text",'class':"form-control",'placeholder':"ex)1,2",'aria-describedby':"addon"+j.pk }))
						.append($("<div>", {'class':"input-group-append", 'id':'statusBtn_'+j.pk}).append(
								runningSatus(j.status,j.pk)) 
								)
						) )
				.append(
				$("<td>").text(j.useyn))
				.append(
				$("<td>" , {id: "statusTd_"+j.pk}).text(j.status))
			;
			$tbody.append($tr);
		})
	}
	taskProgress(res.taskCnt);
	_dataTab = $("#collectorTable").DataTable();
}

function execKwc(pk){
	let obj = $("#statusBtn_"+pk);
	let requestUrl = "/simulator/crawl";
	let json = null;
	let pageRange = $(obj).prev().val();
	// page input validation start//
	let re;
	if(pageRange && pageRange != '' ){
		re = /^(\d+).(\d+)$/m.exec(pageRange)
		if(re == null){
			alert("page 범위를 확인하세요 : ex) 1,5");
			return false;
		}
		if(re.length < 3 || re[1] > re[2]){
			alert("page 범위 순서를 확인하세요 : ex) 1,5");
			return false;
		}
	}else{
//		alert("page 범위를 입력 하세요");
//		return false;
		re = new Array("","1","1");
	}
	// page input validation end //
	let strData = JSON.stringify({"pk":pk, "startPage": re[1], "endPage": re[2]});
	$.ajax({
		type: "POST",
		url: requestUrl,
		data: strData,
		contentType: "application/json; charset=UTF-8", 
		async: true,
		cache: false,
		error: function(XMLHttpRequest, textStatus, errorThrown) { 
			console.log("Status: " + textStatus);
		}
	});
	$(obj).children().remove();	
	$(obj).append(runningSatus('R',pk));
	
	return json;
}
// 실행 버튼 세팅
function runningSatus(stat,pk){
	let $button=$();
	if(stat == 'R'){
		$button = $("<button>",{'class':"btn btn-danger disabled", 'type':"button", 'id':"addon"+pk, 'onclick':'javascript:alert("실행중..")' }).text("running").append(
				$("<span>", {'class':'spinner-border spinner-border-sm','role':'status','aria-hidden':'true'}))
	}
	else{
		$button = $("<button>",{'class':"btn btn-danger", 'type':"button", 'id':"addon"+pk, 'onclick':'execKwc('+pk+')' }).append($("<i>" ,{'class': 'fas fa-play'}))
	}
	return $button;	
}

function taskProgress(curTask){
	let tasks = 10;
	let perTask = ((tasks-curTask)/tasks) * 100;
	$("#taskProg").css("width" , perTask+"%");
	$("#availableTask").text(tasks-curTask);
	$("#availableTask").val(tasks-curTask);
	
}