function searchByOnline(state){
	window.location.href = "/wgcloud/shellInfo/list?state="+state;
}

function searchByAccount() {
	window.location.href = "/wgcloud/shellInfo/list?account="+$("#account").val();
}

function add() {
	window.location.href = "/wgcloud/shellInfo/edit";
}


function view(id) {
	window.location.href = "/wgcloud/shellInfo/view?id="+id;
}

function edit(id){
	window.location.href = "/wgcloud/appInfo/edit?id="+id;
}

function del(id) {
	if(confirm('你确定要删除吗，删除同时会取消该指令下发？')) {
		window.location.href = "/wgcloud/shellInfo/del?id=" + id;
	}
}

function restartShell(id) {
	if(confirm('你确定要重新下发指令吗？')) {
		$.ajax({
			url: "/wgcloud/shellInfo/restart?id=" + id,
			//data: {},
			type: "GET",
			//dataType: "json",
			success: function(data) {
				toastr.success("【成功】已重新下发指令");
				$("#"+id).html('<span class="badge bg-primary">已重新下发</span>');
			}
		});
	}
}

function cancelShell(id) {
	if(confirm('你确定要取消指令下发吗？')) {
		$.ajax({
			url: "/wgcloud/shellInfo/cancel?id=" + id,
			//data: {},
			type: "GET",
			//dataType: "json",
			success: function(data) {
				toastr.success("【成功】已取消尚未下发的指令，已下发成功的指令将会继续执行");
				$("#"+id).attr("class","badge bg-warning");
				$("#"+id).html("已取消");
			}
		});
	}
}
function viewDate(id,searchTime){
	var searchDate = $("#searchDate").val();
	window.location.href = "/wgcloud/appInfo/view?id="+id+"&date="+searchDate+"&am="+searchTime;
}
