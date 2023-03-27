function searchByOrder(orderBy,orderType){
	window.location.href = "/wgcloud/appInfo/list?orderBy="+orderBy+"&orderType="+orderType+"&hostname="+$("#hostname").val();
}

function searchByOnline(state){
	window.location.href = "/wgcloud/appInfo/list?state="+state;
}

function searchByAccount() {
	window.location.href = "/wgcloud/appInfo/list?account="+$("#account").val();
}

function add() {
	window.location.href = "/wgcloud/appInfo/edit";
}

function addBatch() {
	window.location.href = "/wgcloud/appInfo/editBatch";
}



function view(id) {
	window.location.href = "/wgcloud/appInfo/view?id="+id;
}

function edit(id){
	window.location.href = "/wgcloud/appInfo/edit?id="+id;
}

function del(id) {
	if(confirm('你确定要删除吗？')) {
		window.location.href = "/wgcloud/appInfo/del?id=" + id;
	}
}
function viewDate(id,searchTime){
	window.location.href = "/wgcloud/appInfo/view?id="+id+"&am="+searchTime;
}

function excelExport(id,searchTime){
	var startTime = $("#startTime").val();
	var endTime = $("#endTime").val();
	window.open("/wgcloud/appInfo/chartExcel?id="+id+"&startTime="+startTime+"&endTime="+endTime+"&am="+searchTime);
}

function showSetGroupId() {
	var chk_value =[];
	$("input[name='todo2']:checkbox").each(function() {
		if($(this).is(':checked')) {
			chk_value.push($(this).val());
		}
	});
	if(chk_value.length == 0){
		alert("请先选择需要设置标签的进程");
		return;
	}
	$("#id3").val(chk_value.join(","));
	$("#modal-default3").modal("toggle");
}

function ajaxSaveGroupId() {
	$("#form4").ajaxSubmit(function(message) {
		window.location.href = window.location.href;
	});
}

function searchByGroupId() {
	window.location.href = "/wgcloud/appInfo/list?groupId="+$("#groupId").val();
}


//数据表单动态添加一行下标，每次添加+1
var dataFromIndex = 0 ;
var appType = "1";

function addDataForm() {
	var dataHideContentHtml = $("#dataHideContent").html();
	dataHideContentHtml = dataHideContentHtml.replace(/{num}/g, dataFromIndex);
	if(appType=='2'){
		dataHideContentHtml = dataHideContentHtml.replace(/进程ID号/g, "PID文件路径");
		dataHideContentHtml = dataHideContentHtml.replace(/输入进程ID号如9639/g, "如：/usr/local/nginx.pid");
	}
	if(appType=='3'){
		dataHideContentHtml = dataHideContentHtml.replace(/进程ID号/g, "进程启动指令的关键字");
		dataHideContentHtml = dataHideContentHtml.replace(/输入进程ID号如9639/g, "linux或windows的进程启动指令COMMAND里的关键字符，请保证关键字符能唯一对应进程，如：wgcloud-server-release.jar");
	}
	$("#dataFormList").append(dataHideContentHtml);
	$("#dataFromIndex").val(dataFromIndex);
	dataFromIndex += 1;
}