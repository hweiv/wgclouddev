
function add() {
	window.location.href = "/wgcloud/accountInfo/edit";
}


function edit(id){
	window.location.href = "/wgcloud/accountInfo/edit?id="+id;
}

function editPasswd(id){
	window.location.href = "/wgcloud/accountInfo/editPasswd?id="+id;
}

function del(id) {
	if(confirm('你确定要删除吗？删除后，此用户下的资源将只对管理员可见，建议删除之前先将该账号的监控资源迁移到其他账号下')) {
		window.location.href = "/wgcloud/accountInfo/del?id=" + id;
	}
}

function showMoveToAccount(account) {
	$("#id3").val(account);
	$("#modal-default3").modal("toggle");
}

function ajaxMoveToAccount() {
	$("#form4").ajaxSubmit(function(message) {
		window.location.href = window.location.href;
	});
}

function showPasswd(){
	var type = $("#passwd").attr("type");
	if(type=='text'){
		$("#eyeSwitch").attr("class","fa fa-eye");
		$("#passwd").attr("type","password");
	}else{
		$("#eyeSwitch").attr("class","fa fa-eye-slash");
		$("#passwd").attr("type","text");
	}
}

