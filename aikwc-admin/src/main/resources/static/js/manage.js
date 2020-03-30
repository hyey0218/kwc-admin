$(document).ready(function(){
	var _menuNo = $("#MENUNO").val();
	var _agency = $("#AGENCY").val();
	$("#accordionSidebar > li:eq(2)").addClass("active");
	$("#accordionSidebar > li:eq(2) > a").click();
	
})

function collectorSelect(obj){
	if($(obj).val() == "")
		return false;
	
	let requestUrl = "/manage/detail/info";
	let jObj = {};
	jObj.pk = $(obj).val();
	let retJson = getJSONAjaxMethod(requestUrl, "POST", jObj).result;
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