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
		$(form).find('[name=pk]').val(retJson.pk)
		$(form).find('[name=startUrl]').val(retJson.startUrl==null?"":retJson.startUrl)
		$(form).find('[name=pageUrl]').val(retJson.pageUrl==null?"":retJson.pageUrl)
		$(form).find('[name=contId]').val(retJson.contId==null?"":retJson.contId)
		$(form).find('[name=titleLink]').val(retJson.titleLink==null?"":retJson.titleLink)
		$(form).find('[name=title]').val(retJson.title==null?"":retJson.title)
		$(form).find('[name=content]').val(retJson.content==null?"":retJson.content)
		$(form).find('[name=writer]').val(retJson.writer==null?"":retJson.writer)
		$(form).find('[name=writeDate]').val(retJson.writeDate==null?"":retJson.writeDate)
		$(form).find('[name=wdatePattern]').val(retJson.wdatePattern==null?"":retJson.wdatePattern)
	}
}

function getDetailJson(pk){
	let requestUrl = "/manage/detail/json";
	let jObj = {};
	jObj.pk = pk;
	let retJson = getJSONAjaxMethod(requestUrl, "POST", jObj).result;
	console.log(retJson);
	let form = $("#cltDetailForm")[0];
	$(form).find('[name=pk]').val(pk);
	$(form).find('[name=detail]').val(retJson);
	$("#cltDetail").modal('show');
}

function getDetailDelete(pk){
	let isDel = confirm("삭제하시겠습니까?");
	var $form = $("<form>");
	if(isDel){
		dummyFormSubmit('/manage/detail/delete', 'POST', {'pk' : pk});
	}
}