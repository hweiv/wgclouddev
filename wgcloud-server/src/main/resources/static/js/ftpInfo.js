function searchByOrder(orderBy,orderType){
	window.location.href = "/wgcloud/ftpInfo/list?orderBy="+orderBy+"&orderType="+orderType+"&ftpHost="+$("#ftpHost").val();
}

function searchByOnline(state){
	window.location.href = "/wgcloud/ftpInfo/list?state="+state;
}

function searchByAccount() {
	window.location.href = "/wgcloud/ftpInfo/list?account="+$("#account").val();
}

function add() {
	window.location.href = "/wgcloud/ftpInfo/edit";
}


function edit(id){
	window.location.href = "/wgcloud/ftpInfo/edit?id="+id;
}

function del(id) {
	if(confirm('你确定要删除吗？')) {
		window.location.href = "/wgcloud/ftpInfo/del?id=" + id;
	}
}