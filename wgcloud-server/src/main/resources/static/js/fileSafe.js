function searchByOrder(orderBy,orderType){
	window.location.href = "/wgcloud/fileSafe/list?orderBy="+orderBy+"&orderType="+orderType+"&hostname="+$("#hostname").val();
}

function searchByOnline(state){
	window.location.href = "/wgcloud/fileSafe/list?state="+state;
}

function searchByAccount() {
	window.location.href = "/wgcloud/fileSafe/list?account="+$("#account").val();
}

function add() {
	window.location.href = "/wgcloud/fileSafe/edit";
}

function addBatch() {
	window.location.href = "/wgcloud/fileSafe/editBatch";
}


function edit(id){
	window.location.href = "/wgcloud/fileSafe/edit?id="+id;
}

function del(id) {
	if(confirm('你确定要删除吗？')) {
		window.location.href = "/wgcloud/fileSafe/del?id=" + id;
	}
}

//数据表单动态添加一行下标，每次添加+1
var dataFromIndex = 0 ;

function addDataForm() {
	var dataHideContentHtml = $("#dataHideContent").html();
	dataHideContentHtml = dataHideContentHtml.replace(/{num}/g, dataFromIndex);
	$("#dataFormList").append(dataHideContentHtml);
	$("#dataFromIndex").val(dataFromIndex);
	dataFromIndex += 1;
}
