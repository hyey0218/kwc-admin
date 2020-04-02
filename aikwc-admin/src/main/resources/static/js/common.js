$(document).ready(function(){
	pageInitialize();
	
	//Web Sockect
	var sock = new SockJS("/ws/getLogMsg");
//	sock.send({'agency':8});
	
	sock.onopen = function(){
		sock.send("");
	}
	
	sock.onmessage = function(message) { 
		let jObj = JSON.parse(message.data);
		
		if(jObj.logs){
			let logs = jObj.logs;
			if(jObj.count > 0){
				if(jObj.count > 3)
					$("#lMsgCnt").text("3+");
				else
					$("#lMsgCnt").text(jObj.count);
			}
			$("#logMsg").children().remove();
			$(logs).each(function(i,j){
				topMessage(j);
			})
		}
	}
	$("#logMsgBtn").on('click',function(){
		getJSONAjaxMethod("/comm/logReadAll", "POST", {});
		$("#lMsgCnt").text("");
	});
})
// form contents --> json
$.fn.serializeObject = function() {
	var result = {}
	var extend = function(i, element) {
	var node = result[element.name]
	if ("undefined" !== typeof node && node !== null) {
		if ($.isArray(node)) {
			node.push(element.value)
		}else {
			result[element.name] = [node, element.value]
		}
	} else {
		result[element.name] = element.value
	}
	}
	$.each(this.serializeArray(), extend)
	return result
}
/**
 * 공통 Ajax - content Type : json
 * @param requestUrl
 * @param reqType :method
 * @param sendData : json object
 * @returns
 */ 
function getJSONAjaxMethod(requestUrl, reqType, sendData){
	var strData = JSON.stringify(sendData);
	var json = null;
	$.ajax({
		type: reqType,
		url: requestUrl,
		data: strData,
		dataType: "json",
		contentType: "application/json; charset=UTF-8", 
		async: false,
		cache: false,
		success: function(data){
			json = data;
		},error: function(XMLHttpRequest, textStatus, errorThrown) { 
			console.log("Status: " + textStatus);
		},timeout: 3000
	});
	
	return json;
}
/**
 * 공통 form submit
 * @param form
 * @returns
 */
function formSubmit(form){
	var $form = $("#"+form);
	var formData = new FormData($form[0]);
	formData.append("agencyNo", $("#AGENCY").val());
	formData.append("menuNo", $("#MENUNO").val());
	formData.append("menuNm", $("#MENUMN").val());
	$form.submit();
}

function pageInitialize(){
	leftMenuFunc();
}
function leftMenuFunc(){
	let _menu = $("#MENU").val();
	let _agency = $("#AGENCY").val();
	
	console.log(_agency);
	if(_agency){
		let $sides = $("#sidebar > .sidebar-sticky > div");
		$sides.each(function(ii , jj ){
			let $sideMens = $(jj).find("div");
			var iconMinus = $("<i>", {class: "fa fa-minus-square"});
			var iconPlus = $("<i>", {class: "fa fa-plus-square"});
			$sideMens.each(function(i,j){
				$menu = $(j).prev();
				if(j.id == "group_"+_agency){
					$(j).addClass("show");
					$menu.find("a:eq(1)").append(iconMinus);
				}
				else
					$menu.find("a:eq(1)").append(iconPlus);

				$menu.click(function(){
					$(this).find("a:eq(1)").children().toggleClass("fa-minus-square fa-plus-square");
				})
			})
		})
	}
}

function topMessage(j){
	let $msgDiv = $("#logMsg");
	let $anch = $("<a>",{'class':'dropdown-item d-flex align-items-center', 'href':'/simulator/log' } )
	.append( $("<div>", {'class':'mr-3'}).append($("<div>",{'class':'icon-circle bg-primary'} )
			.append($("<i>",{'class':'fas fa-file-alt text-white' })  )  )  )
	.append( $("<div>").append(
			$("<div>",{'class':'small text-gray-600'} ).text(j.create_date)  ).append(
			$("<span>", {'class':'font-weight-bold'} ).text(j.comment)
			) );

	$msgDiv.append($anch);
}

function agencySelect(obj){
	if($(obj).val() == "")
		return false;
	
	let requestUrl = "/manage/groupInAgency";
	let jObj = {}
	jObj.agency = $(obj).val();
	let retJson = getJSONAjaxMethod(requestUrl, "POST", jObj).result;
	$("#selectGroup > option:gt(0)").remove();
	if(retJson){
		let $elementSelect = $("#selectGroup");
		$(retJson).each(function(i,j){
			$elementSelect.append($("<option>", {value: j.pk} ).text(j.name) );
		})
	}
}


function groupSelect(obj){
	if($(obj).val() == "")
		return false;
	
	let requestUrl = "/manage/siteInGroup";
	let jObj = {}
	jObj.grp = $(obj).val();
	let retJson = getJSONAjaxMethod(requestUrl, "POST", jObj).result;
	$("#selectSite > option:gt(0)").remove();
	if(retJson){
		let $elementSelect = $("#selectSite");
		$(retJson).each(function(i,j){
			$elementSelect.append($("<option>", {value: j.pk} ).text(j.name) );
		})
	}
}


function siteSelect(obj){
	if($(obj).val() == "")
		return false;
	
	let requestUrl = "/manage/collectorInSite";
	let jObj = {};
	jObj.site = $(obj).val();
	let retJson = getJSONAjaxMethod(requestUrl, "POST", jObj).result;
	$("#selectCollector > option:gt(0)").remove();
	if(retJson){
		let $elementSelect = $("#selectCollector");
		$(retJson).each(function(i,j){
			console.log(j)
			$elementSelect.append($("<option>", {value: j.pk} ).text(j.name) );
		})
	}
}


function taskProgress(curTask){
	let tasks = 50;
	let perTask = ((tasks-curTask)/tasks) * 100;
	$("#taskProg").css("width" , perTask+"%");
	$("#availableTask").text(tasks-curTask);
	$("#availableTask").val(tasks-curTask);
}
