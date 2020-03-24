$(document).ready(function(){
	pageInitialize();
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
			json = data.result;
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
//		let $sideMens = $("#sidebar > div > div > div.collapse");
//		var iconMinus = $("<i>", {class: "fa fa-minus-square"});
//		var iconPlus = $("<i>", {class: "fa fa-plus-square"});
//		$sideMens.each(function(i,j){
//			$menu = $(j).prev();
//			if(j.id == "group_"+_agency){
//				$(j).addClass("show");
//				$menu.find("a:eq(1)").append(iconMinus);
//			}
//			else
//				$menu.find("a:eq(1)").append(iconPlus);
//
//			$menu.click(function(){
//				$(this).find("a:eq(1)").children().toggleClass("fa-minus-square fa-plus-square");
//			})
//		})
	}
	
}
