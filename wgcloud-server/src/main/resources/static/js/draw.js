function searchByOrder(orderBy,orderType){
	window.location.href = "/wgcloud/dash/hostDrawList?orderBy="+orderBy+"&orderType="+orderType;
}

function searchByOnlineDashView(state){
	window.location.href = "/wgcloud/dash/hostDrawList?state="+state;
}


function viewChart(id) {
	window.location.href="/wgcloud/systemInfo/chart?id="+id;
}

function view(id) {
	window.location.href = "/wgcloud/dash/hostDraw?id="+id;
}

function viewDocker(id) {
	window.location.href = "/wgcloud/dockerInfo/view?id="+id;
}

function viewAppInfo(id) {
	window.location.href = "/wgcloud/appInfo/view?id="+id;
}

function viewCustomInfo(id) {
	window.location.href = "/wgcloud/customInfo/view?id="+id;
}

function viewApps(hostname){
	window.location.href = "/wgcloud/appInfo/list?hostname="+hostname;
}

function viewDockers(hostname){
	window.location.href = "/wgcloud/dockerInfo/list?hostname="+hostname;
}

function viewPorts(hostname){
	window.location.href = "/wgcloud/portInfo/list?hostname="+hostname;
}

function viewFileWarn(hostname){
	window.location.href = "/wgcloud/fileWarnInfo/list?hostname="+hostname;
}

function stateList(id) {
	window.location.href = "/wgcloud/fileWarnInfo/stateList?fileWarnId="+id;
}

function viewFileWarnInfo(id) {
	window.location.href = "/wgcloud/fileWarnInfo/view?id="+id;
}
