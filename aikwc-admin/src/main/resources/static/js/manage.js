$(document).ready(function(){
	var _menuNo = $("#MENUNO").val();
	var _agency = $("#AGENCY").val();
	$("#accordionSidebar > li:eq(2)").addClass("active");
	$("#accordionSidebar > li:eq(2) > a").click();
	
})



function groupSelect(obj){
	if($(obj).val() == "")
		return false;
	
	let requestUrl = "/manage/siteInGroup";
	let jObj = {}
	jObj.grp = $(obj).val();
	let retJson = getJSONAjaxMethod(requestUrl, "POST", jObj);
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
	let retJson = getJSONAjaxMethod(requestUrl, "POST", jObj);
	$("#selectCollector > option:gt(0)").remove();
	if(retJson){
		let $elementSelect = $("#selectCollector");
		$(retJson).each(function(i,j){
			console.log(j)
			$elementSelect.append($("<option>", {value: j.pk} ).text(j.name) );
		})
	}
}


function collectorSelect(obj){
	if($(obj).val() == "")
		return false;
	
	let requestUrl = "/manage/detail/info";
	let jObj = {};
	jObj.pk = $(obj).val();
	let retJson = getJSONAjaxMethod(requestUrl, "POST", jObj);
	if(retJson){
		let form = $("#collectDetailForm")[0];
		$(form).find('[name=startUrl]').val(retJson.startUrl)
		$(form).find('[name=pageUrl]').val(retJson.pageUrl)
		$(form).find('[name=contId]').val(retJson.contId)
		$(form).find('[name=titleLink]').val(retJson.titleLink)
		$(form).find('[name=title]').val(retJson.title)
		$(form).find('[name=content]').val(retJson.content)
		$(form).find('[name=writer]').val(retJson.writer)
		$(form).find('[name=writeDate]').val(retJson.writeDate)
		$(form).find('[name=wdatePattern]').val(retJson.wdatePattern)
	}
}