

$(document).ready(function(){
       $("#userName").focus();
	$("#form1").validationEngine();
	doHandleYear();

});


function doHandleYear() {
	var myDate = new Date();
	var tYear = myDate.getFullYear();
	$("#copyyear").html(tYear);
}

function setMd5Pwd(){
	$("#md5pwd").val(md5($.trim($("#passwd").val())));
	$("#passwd").val('********');
	return true;
}


