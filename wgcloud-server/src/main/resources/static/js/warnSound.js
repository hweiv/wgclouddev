var timerWarnSound =null;

//播放告警音频
function playSound(){
	var urlParams = window.location.search;
	var dashView = "";
	if (urlParams.indexOf("dashView") != -1) {
		dashView = "?dashView=1";
	}
	var timerWarnSoundDivObj = $("#timerWarnSoundDiv");
	if(timerWarnSoundDivObj.attr("value")=='2'){
		//开启声音提示，则开启定期提醒
		timerWarnSoundDivObj.html('<i style="margin-right:3px" class="far fa-bell"></i>');
		timerWarnSoundDivObj.attr("value",'1');
		timerWarnSoundDivObj.attr('title','当前已开启页面告警声音提示，点击可关闭');
		timerWarnSound = setInterval(function(){ajaxWarnInfoCount()},60000);
	}else{
		//关闭声音提示
		timerWarnSoundDivObj.attr("value",'2');
		timerWarnSoundDivObj.attr('title','当前已关闭页面告警声音提示，点击可开启');
		timerWarnSoundDivObj.html('<i class="far fa-bell-slash"></i>');
		clearInterval(timerWarnSound);
		timerWarnSound=null;
	}
	$.ajax({
		url: "/wgcloud/warnInfo/warnCountAjaxHandle"+dashView,
		type: "POST",
		data: {"timerWarnSound": timerWarnSoundDivObj.attr("value")},
		dataType: "json",
		success: function(data) {
		}
	});

}


function ajaxWarnInfoCount() {
	var urlParams = window.location.search;
	var dashView = "";
	if (urlParams.indexOf("dashView") != -1) {
		dashView = "?dashView=1";
	}
	$.ajax({
		url: "/wgcloud/warnInfo/warnCountAjax"+dashView,
		type: "GET",
		dataType: "json",
		success: function(data) {
			if(data != '0'){
				document.getElementById("notification").play();
				toastr.error("有新的告警消息，请查看推送通知或<a  target='_blank' href='/wgcloud/log/list'>点击查看日志</a>");
			}
		}
	});
}

function playSoundInit(){
	var timerWarnSoundDivObj = $("#timerWarnSoundDiv");
	//若已开启声音提示，则开启定期提醒
	if(timerWarnSoundDivObj.attr("value")=='1'){
		timerWarnSound = setInterval(function(){ajaxWarnInfoCount()},60000);
	}
}
//页面初始加载处理
playSoundInit();