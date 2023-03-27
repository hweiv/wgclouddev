function searchByOrder(orderBy,orderType){
	var urlParams = window.location.search;
	var hrefUrl = "/wgcloud/dceInfo/list?orderBy="+orderBy+"&orderType="+orderType;
	if (urlParams.indexOf("groupId") != -1) {
		hrefUrl += "&groupId="+$("#groupId").val();
	}
	window.location.href = hrefUrl;
}

function searchByOnline(state){
	window.location.href = "/wgcloud/dceInfo/list?heathStatus="+state;
}

function searchByAccount() {
	window.location.href = "/wgcloud/dceInfo/list?account="+$("#account").val();
}

function add() {
	window.location.href = "/wgcloud/dceInfo/edit";
}

function view(id) {
	window.location.href = "/wgcloud/dceInfo/view?id="+id;
}


function viewDate(id,searchTime){
	window.location.href = "/wgcloud/dceInfo/view?id="+id+"&am="+searchTime;
}

function excelExport(id,searchTime){
	var startTime = $("#startTime").val();
	var endTime = $("#endTime").val();
	window.open("/wgcloud/dceInfo/chartExcel?id="+id+"&startTime="+startTime+"&endTime="+endTime+"&am="+searchTime);
}

function del(id) {
	if(confirm('你确定要删除吗？')) {
		window.location.href = "/wgcloud/dceInfo/del?id=" + id;
	}
}
function edit(id){
	window.location.href = "/wgcloud/dceInfo/edit?id="+id;
}

function importHosts() {
	if(confirm('你确定要将所有监控主机导入到数通设备吗？')) {
		toastr.success("正在将所有监控主机导入到数通设备……");
		$.ajax({
			url: "/wgcloud/dceInfo/importHosts",
			//data: {},
			type: "GET",
			//dataType: "json",
			success: function (data) {
				toastr.success("导入完成，请刷新页面");
			}
		});
	}
}

function showSetGroupId() {
	var chk_value =[];
	$("input[name='todo2']:checkbox").each(function() {
		if($(this).is(':checked')) {
			chk_value.push($(this).val());
		}
	});
	if(chk_value.length == 0){
		alert("请先选择需要设置分组的数通设备");
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
	window.location.href = "/wgcloud/dceInfo/list?groupId="+$("#groupId").val();
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