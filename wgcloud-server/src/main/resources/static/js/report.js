function searchByPara(reportType){
	window.location.href = "/wgcloud/report/list?reportType="+reportType;
}

function excelExport(id){
	window.open("/wgcloud/report/chartExcel?id="+id);
}


function view(id) {
	window.location.href = "/wgcloud/report/view?id="+id;
}
