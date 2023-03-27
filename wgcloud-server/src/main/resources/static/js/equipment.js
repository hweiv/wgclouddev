
function add() {
	window.location.href = "/wgcloud/equipment/edit";
}

function searchByAccount() {
	window.location.href = "/wgcloud/equipment/list?account="+$("#account").val();
}

function view(id) {
	window.location.href = "/wgcloud/equipment/view?id="+id;
}

function edit(id){
	window.location.href = "/wgcloud/equipment/edit?id="+id;
}

function del(id) {
	if(confirm('你确定要删除吗？')) {
		window.location.href = "/wgcloud/equipment/del?id=" + id;
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
	window.location.href = "/wgcloud/equipment/list?groupId="+$("#groupId").val();
}
