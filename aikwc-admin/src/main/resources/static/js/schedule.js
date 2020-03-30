

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