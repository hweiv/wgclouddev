function searchByOrder(orderBy,orderType){
	window.location.href = "/wgcloud/fileWarnInfo/list?orderBy="+orderBy+"&orderType="+orderType+"&hostname="+$("#hostname").val();
}

function searchByAccount() {
	window.location.href = "/wgcloud/fileWarnInfo/list?account="+$("#account").val();
}

function add() {
	window.location.href = "/wgcloud/fileWarnInfo/edit";
}

function addBatch() {
	window.location.href = "/wgcloud/fileWarnInfo/editBatch";
}


function stateList(id) {
	window.location.href = "/wgcloud/fileWarnInfo/stateList?fileWarnId="+id;
}

function excelExport(id){
	window.open("/wgcloud/fileWarnInfo/chartExcel?id="+id);
}

function view(id) {
	window.location.href = "/wgcloud/fileWarnInfo/view?id="+id;
}

function stateView(id) {
	window.location.href = "/wgcloud/fileWarnInfo/stateView?id="+id;
}

function edit(id){
	window.location.href = "/wgcloud/fileWarnInfo/edit?id="+id;
}

function del(id) {
	if(confirm('你确定要删除吗？')) {
		window.location.href = "/wgcloud/fileWarnInfo/del?id=" + id;
	}
}
