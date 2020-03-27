$(document).ready(function(){
	pageInitialize();
	
	//Web Sockect
	var sock = new SockJS("/ws/getLogMsg");
//	sock.send({'agency':8});
	
	sock.onopen = function(){
		console.log("open")
	}
	
	sock.onmessage = function(message) { 
		let jObj = JSON.parse(message.data);
		console.log(jObj);
		
		if(jObj.logs){
			let logs = jObj.logs;
			let $msgDiv = $("#logMsg").children().remove();
			$(logs).each(function(i,j){
				topMessage($msgDiv, j);
			})
		}
	}
	
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

function topMessage($msgDiv, j){
	let $anch = $("<a>",{'class':'dropdown-item d-flex align-items-center', 'href':'javascript:void(0)' } )
	.append( $("<div>", {'class':'mr-3'}).append($("<div>",{'class':'icon-circle bg-primary'} )
			.append($("<i>",{'class':'fas fa-file-alt text-white' })  )  )  )
	.append( $("<div>").append(
			$("<div>",{'class':'small text-gray-600'} ).text('time')  ).append(
			$("<span>", {'class':'font-weight-bold'} ).text(j.comment)
			) );

	$msgDiv.append($anch);
}
