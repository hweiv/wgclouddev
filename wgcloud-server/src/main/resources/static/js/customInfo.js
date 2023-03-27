function searchByOrder(orderBy,orderType){
	window.location.href = "/wgcloud/customInfo/list?orderBy="+orderBy+"&orderType="+orderType+"&hostname="+$("#hostname").val();
}

function searchByOnline(state){
	window.location.href = "/wgcloud/customInfo/list?state="+state;
}

function searchByAccount() {
	window.location.href = "/wgcloud/customInfo/list?account="+$("#account").val();
}

function add() {
	window.location.href = "/wgcloud/customInfo/edit";
}


function view(id) {
	window.location.href = "/wgcloud/customInfo/view?id="+id;
}

function edit(id){
	window.location.href = "/wgcloud/customInfo/edit?id="+id;
}

function del(id) {
	if(confirm('你确定要删除吗？')) {
		window.location.href = "/wgcloud/customInfo/del?id=" + id;
	}
}
function viewDate(id,searchTime){
	window.location.href = "/wgcloud/customInfo/view?id="+id+"&am="+searchTime;
}

function excelExport(id,searchTime){
	var startTime = $("#startTime").val();
	var endTime = $("#endTime").val();
	window.open("/wgcloud/customInfo/chartExcel?id="+id+"&startTime="+startTime+"&endTime="+endTime+"&am="+searchTime);
}

function showSetGroupId() {
	var chk_value =[];
	$("input[name='todo2']:checkbox").each(function() {
		if($(this).is(':checked')) {
			chk_value.push($(this).val());
		}
	});
	if(chk_value.length == 0){
		alert("请先选择需要设置标签的数据");
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
	window.location.href = "/wgcloud/customInfo/list?groupId="+$("#groupId").val();
}
