function searchByOrder(orderBy,orderType){
	window.location.href = "/wgcloud/dbTable/list?orderBy="+orderBy+"&orderType="+orderType+"&dbInfoId="+$("#dbInfoId").val();
}

function searchByAccount() {
	window.location.href = "/wgcloud/dbTable/list?account="+$("#account").val();
}

function view(id) {
	window.location.href = "/wgcloud/dbTable/edit?id="+id;
}

function viewChart(id){
    window.location.href = "/wgcloud/dbTable/viewChart?id="+id;
}

function searchByDb(){
	window.location.href = "/wgcloud/dbTable/list?dbInfoId="+$("#dbInfoId").val();
}


function add() {
	window.location.href = "/wgcloud/dbTable/edit";
}

function del(id) {
	if(confirm('你确定要删除吗？')) {
		window.location.href = "/wgcloud/dbTable/del?id=" + id;
	}
}

function viewDate(id,searchTime){
	window.location.href = "/wgcloud/dbTable/viewChart?id="+id+"&am="+searchTime;
}

function excelExport(id,searchTime){
	var startTime = $("#startTime").val();
	var endTime = $("#endTime").val();
	window.open("/wgcloud/dbTable/chartExcel?id="+id+"&startTime="+startTime+"&endTime="+endTime+"&am="+searchTime);
}
