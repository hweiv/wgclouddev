
function add() {
	window.location.href = "/wgcloud/hostGroup/edit";
}


function edit(id){
	window.location.href = "/wgcloud/hostGroup/edit?id="+id;
}

function del(id) {
	if(confirm('你确定要删除吗？删除此主机分组后，此分组下的主机将恢复到无分组状态，其他无影响')) {
		window.location.href = "/wgcloud/hostGroup/del?id=" + id;
	}
}

function searchByGroupType() {
	window.location.href = "/wgcloud/hostGroup/list?groupType="+$("#groupType").val();
}
