function searchByPara(state){
	window.location.href = "/wgcloud/log/list?state="+state;
}

function view(id) {
	window.location.href = "/wgcloud/log/view?id="+id;
}

function resetParam(){
	$("#startTime").val("");
	$("#endTime").val("");
	$("#hostname").val("");
}
