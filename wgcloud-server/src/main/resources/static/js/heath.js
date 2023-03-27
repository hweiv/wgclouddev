function searchByOrder(orderBy,orderType){
	window.location.href = "/wgcloud/heathMonitor/list?orderBy="+orderBy+"&orderType="+orderType;
}

function searchByOnline(state){
	window.location.href = "/wgcloud/heathMonitor/list?heathStatus="+state;
}

function searchByAccount() {
	window.location.href = "/wgcloud/heathMonitor/list?account="+$("#account").val();
}

function add() {
	window.location.href = "/wgcloud/heathMonitor/edit";
}

function view(id) {
	window.location.href = "/wgcloud/heathMonitor/view?id="+id;
}


function targetUrl(url) {
	window.open(url);
}

function viewDate(id,searchTime){
	window.location.href = "/wgcloud/heathMonitor/view?id="+id+"&am="+searchTime;
}

function excelExport(id,searchTime){
	var startTime = $("#startTime").val();
	var endTime = $("#endTime").val();
	window.open("/wgcloud/heathMonitor/chartExcel?id="+id+"&startTime="+startTime+"&endTime="+endTime+"&am="+searchTime);
}

function del(id) {
	if(confirm('你确定要删除吗？')) {
		window.location.href = "/wgcloud/heathMonitor/del?id=" + id;
	}
}
function edit(id){
	window.location.href = "/wgcloud/heathMonitor/edit?id="+id;
}

function showPostStr() {
	$("#PostStrDiv").show();
}

function hidePostStr() {
	$("#PostStrDiv").hide();
}
