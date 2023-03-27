function searchByOrder(orderBy,orderType){
	var urlParams = window.location.search;
	var hrefUrl = "/wgcloud/snmpInfo/list?orderBy="+orderBy+"&orderType="+orderType;
	if (urlParams.indexOf("groupId") != -1) {
		hrefUrl += "&groupId="+$("#groupId").val();
	}
	window.location.href = hrefUrl;
}

function searchByOnline(state){
	window.location.href = "/wgcloud/snmpInfo/list?state="+state;
}

function searchByAccount() {
	window.location.href = "/wgcloud/snmpInfo/list?account="+$("#account").val();
}


function add() {
	window.location.href = "/wgcloud/snmpInfo/edit";
}

function view(id) {
	window.location.href = "/wgcloud/snmpInfo/view?id="+id;
}

function viewDate(id,searchTime){
	window.location.href = "/wgcloud/snmpInfo/view?id="+id+"&am="+searchTime;
}

function excelExport(id,searchTime){
	var startTime = $("#startTime").val();
	var endTime = $("#endTime").val();
	window.open("/wgcloud/snmpInfo/chartExcel?id="+id+"&startTime="+startTime+"&endTime="+endTime+"&am="+searchTime);
}

function del(id) {
	if(confirm('你确定要删除吗？')) {
		window.location.href = "/wgcloud/snmpInfo/del?id=" + id;
	}
}
function edit(id){
	window.location.href = "/wgcloud/snmpInfo/edit?id="+id;
}
