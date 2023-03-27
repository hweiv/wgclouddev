function searchByOrder(orderBy,orderType){
	window.location.href = "/wgcloud/dockerInfo/list?orderBy="+orderBy+"&orderType="+orderType+"&hostname="+$("#hostname").val();
}

function searchByOnline(state){
	window.location.href = "/wgcloud/dockerInfo/list?state="+state;
}

function searchByAccount() {
	window.location.href = "/wgcloud/dockerInfo/list?account="+$("#account").val();
}

function add() {
	window.location.href = "/wgcloud/dockerInfo/edit";
}

function addBatch() {
	window.location.href = "/wgcloud/dockerInfo/editBatch";
}


function view(id) {
	window.location.href = "/wgcloud/dockerInfo/view?id="+id;
}

function edit(id){
	window.location.href = "/wgcloud/dockerInfo/edit?id="+id;
}

function del(id) {
	if(confirm('你确定要删除吗？')) {
		window.location.href = "/wgcloud/dockerInfo/del?id=" + id;
	}
}
function viewDate(id,searchTime){
	window.location.href = "/wgcloud/dockerInfo/view?id="+id+"&am="+searchTime;
}

function excelExport(id,searchTime){
	var startTime = $("#startTime").val();
	var endTime = $("#endTime").val();
	window.open("/wgcloud/dockerInfo/chartExcel?id="+id+"&startTime="+startTime+"&endTime="+endTime+"&am="+searchTime);
}

function showSetGroupId() {
	var chk_value =[];
	$("input[name='todo2']:checkbox").each(function() {
		if($(this).is(':checked')) {
			chk_value.push($(this).val());
		}
	});
	if(chk_value.length == 0){
		alert("请先选择需要设置分组的docker");
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
	window.location.href = "/wgcloud/dockerInfo/list?groupId="+$("#groupId").val();
}

//数据表单动态添加一行下标，每次添加+1
var dataFromIndex = 0 ;
var appType = "1";

function addDataForm() {
	var dataHideContentHtml = $("#dataHideContent").html();
	dataHideContentHtml = dataHideContentHtml.replace(/{num}/g, dataFromIndex);
	if(appType=='2'){
		dataHideContentHtml = dataHideContentHtml.replace(/CONTAINER ID/g, "CONTAINER NAME");
		dataHideContentHtml = dataHideContentHtml.replace(/完整ID/g, "NAME");
	}
	$("#dataFormList").append(dataHideContentHtml);
	$("#dataFromIndex").val(dataFromIndex);
	dataFromIndex += 1;
}