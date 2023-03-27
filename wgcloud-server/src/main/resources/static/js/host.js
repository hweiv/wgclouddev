function searchByOrder(orderBy,orderType){
	var urlParams = window.location.search;
	var hrefUrl = "/wgcloud/systemInfo/systemInfoList?orderBy="+orderBy+"&orderType="+orderType;
	if (urlParams.indexOf("pageSize") != -1) {
		hrefUrl += "&pageSize=10000";
	}
	if (urlParams.indexOf("groupId") != -1) {
		hrefUrl += "&groupId="+$("#groupId").val();
	}
	window.location.href = hrefUrl;
}

function searchByOnline(state){
	window.location.href = "/wgcloud/systemInfo/systemInfoList?state="+state;
}

function searchByOnlineDashView(state){
	window.location.href = "/wgcloud/systemInfo/systemInfoList?dashView=1&state="+state;
}


function searchByOrderDashView(orderBy,orderType){
	var urlParams = window.location.search;
	var hrefUrl = "/wgcloud/systemInfo/systemInfoList?dashView=1&orderBy="+orderBy+"&orderType="+orderType;
	if (urlParams.indexOf("pageSize") != -1) {
		hrefUrl += "&pageSize=10000";
	}
	if (urlParams.indexOf("groupId") != -1) {
		hrefUrl += "&groupId="+$("#groupId").val();
	}
	window.location.href = hrefUrl;
}

function searchAll(){
	window.location.href = "/wgcloud/systemInfo/systemInfoList?pageSize=10000";
}

function searchAllDashView(){
	window.location.href = "/wgcloud/systemInfo/systemInfoList?dashView=1&pageSize=10000";
}

function viewImage(id) {
	window.location.href = "/wgcloud/dash/hostDraw?id="+id;
}

function viewDashView(id) {
	window.location.href = "/wgcloud/systemInfo/detail?dashView=1&id="+id;
}
function viewChartDashView(id) {
	window.location.href = "/wgcloud/systemInfo/chart?dashView=1&id="+id;
}
function viewDatetDashView(id,searchTime){
	window.location.href = "/wgcloud/systemInfo/chart?dashView=1&id="+id+"&am="+searchTime;
}
function view(id) {
	window.location.href = "/wgcloud/systemInfo/detail?id="+id;
}
function viewChart(id) {
	window.location.href = "/wgcloud/systemInfo/chart?id="+id;
}

function del(id) {
	if(confirm('你确定要删除吗？此操作只会删除主机，主机下的监控资源及历史数据不会被删除')) {
		window.location.href = "/wgcloud/systemInfo/del?id=" + id;
	}
}

function viewWebSSH(hostname) {
	window.open("/wgcloud/ssh2/view?hostname="+hostname);
}


function viewDate(id,searchTime){
	window.location.href = "/wgcloud/systemInfo/chart?id="+id+"&am="+searchTime;
}

function excelExport(id,searchTime){
	var startTime = $("#startTime").val();
	var endTime = $("#endTime").val();
	window.open("/wgcloud/systemInfo/chartExcel?id="+id+"&startTime="+startTime+"&endTime="+endTime+"&am="+searchTime);
}

function excelExportHostList(){
	var urlParams = window.location.search;
	window.open("/wgcloud/systemInfo/hostListExcel" + urlParams);
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

function ajaxSaveRemark() {
	var idRemark = $("#id").val();
	$("#"+idRemark+"_remark").html($("#remark").val());
	$("#modal-default").modal("toggle");
	$("#form2").ajaxSubmit(function(message) {

	});
}


function setHostRemark(hostId,hostRemark) {
	$("#id").val(hostId);
	$("#remark").val(hostRemark);
}


function setWinConsole(hostId,winConsole) {
	$("#id2").val(hostId);
	$("#winConsole").val(winConsole);
}

function ajaxSaveWinConsole() {
	$("#form3").ajaxSubmit(function(message) {
		window.open (message);
		window.location.href = window.location.href;
	});
}

function viewWinConsole() {
    window.open ($("#winConsole").val());
    $("#modal-default2").modal("toggle");
}

function showSetGroupId() {
	var chk_value =[];
	$("input[name='todo2']:checkbox").each(function() {
		if($(this).is(':checked')) {
			chk_value.push($(this).val());
		}
	});
	if(chk_value.length == 0){
		alert("请先选择需要设置标签的主机");
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
	window.location.href = "/wgcloud/systemInfo/systemInfoList?groupId="+$("#groupId").val();
}

function searchByAccount() {
	window.location.href = "/wgcloud/systemInfo/systemInfoList?account="+$("#account").val();
}

function searchByGroupIdDashView() {
	window.location.href = "/wgcloud/systemInfo/systemInfoList?dashView=1&groupId="+$("#groupId").val();
}

function ajaxSystemInfoList() {
	var urlParams = window.location.search;
	var dashView = "";
	if (urlParams.indexOf("dashView") != -1) {
		dashView = "?dashView=1";
	}
	$.ajax({
		url: "/wgcloud/systemInfo/systemInfoListAjax"+dashView,
		type: "GET",
		dataType: "json",
		success: function(data) {
			for(i in data) {
			    var obj = $("#"+data[i].id+"_state").html();
                if(obj!=null && obj != undefined){
                    $("#"+data[i].id+"_state").html(data[i].hostname);
                    $("#"+data[i].id+"_memPer").html(data[i].image);
                    $("#"+data[i].id+"_cpuPer").html(data[i].hostnameExt);
                    $("#"+data[i].id+"_rxbyt").html(data[i].rxbyt);
                    $("#"+data[i].id+"_txbyt").html(data[i].txbyt);
					$("#"+data[i].id+"_fiveLoad").html(data[i].fiveLoad);
					$("#"+data[i].id+"_netConnections").html(data[i].netConnections);
                    $("#"+data[i].id+"_createTime").html(data[i].remark);
                }
			}
		}
	});
}

function startTime(){
    if("" == $("#startTime").html()){
        $("#timeDiv").show();
        $("#startTime").html("10");
    } else {
        var sec = parseInt($("#startTime").html());
        sec = sec - 1;
        if (-1==sec){
            $("#startTime").html("10");
        }else{
            $("#startTime").html(sec);
        }
    }
}

var timer=null;
var timer2=null;
function startTask() {
	if(timer == null){
		$("#startTaskBtn").html("停止刷新");
		$("#startTaskBtn").addClass("btn-default");
		toastr.success("【自动刷新】启动成功，每隔10秒会自动刷新主机状态、内存%、CPU%、上下行传输速率、系统负载、连接数量、更新时间");
		timer = setInterval(function(){ajaxSystemInfoList()},10000);
        timer2 = setInterval(function(){ startTime()},1000);
	}else{
		toastr.success("【自动刷新】停止成功");
		$("#startTaskBtn").html("自动刷新");
		$("#startTaskBtn").removeClass("btn-default");
		clearInterval(timer);
		timer=null;
        clearInterval(timer2);
        timer2=null;
        $("#startTime").html("");
        $("#timeDiv").hide();
	}
}
