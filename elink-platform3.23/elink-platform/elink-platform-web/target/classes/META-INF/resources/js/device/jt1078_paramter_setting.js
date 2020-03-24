function getsend81030079cmddlgfrm(){
	var formData = $("#send-8103-0079-cmd-dlg-frm").serializeObject();
	var paramkeys = formData.paramkeys;
	var messageId = formData.messageId;
	
	var param = {};
	param[paramkeys]=formData;
	var data = {};
	data.param = param;
	data.messageId =messageId;

	return data;
}

function getsend8103007Acmddlgfrm(){
	var formData = $("#send-8103-007A-cmd-dlg-frm").serializeObject();
	var paramkeys = formData.paramkeys;
	var messageId = formData.messageId;
	
	var flag="";
	for(var i=0;i<32;i++){
		var val = formData["bit"+i];
		if(val){
			flag+=val;
		}else{
			flag+="0";
		}
	}
	
	var param = {};
	var map = {};
	map.flag=parseInt(flag,2).toString(16);
	param[paramkeys]=map;
	var data = {};
	data.param = param;
	data.messageId =messageId;
	return data;
}

function getsend8103007Bcmddlgfrm(){
	var formData = $("#send-8103-007B-cmd-dlg-frm").serializeObject();
	var paramkeys = formData.paramkeys;
	var messageId = formData.messageId;
	
	var param = {};
	param[paramkeys]=formData;
	var data = {};
	data.param = param;
	data.messageId =messageId;

	return data;
}

function init007ARadio(){
	var html="<div class='message_con'>";
	var alarm = ["视频信号丢失报警","视频信号遮挡报警","存储单元故障报警","其他视频设备故障报警","客车超员报警","异常驾驶行为报警","特殊报警录像达到存储阈值报警"];
	var count = 0;
	for(var i=0;i<alarm.length;i++){
		if(count>0&&count%3==0){
			html+="</div><div class='message_con'>"
		}
		count++;
		html+="<label style='width:200px;'>"+alarm[i]+"：</label>开<input checked='checked' type='radio' value='0' name='bit"+i+"'>关<input type='radio' value='1' name='bit"+i+"'>";
	}
	html+="</div>";
	$("#cmd_setting_8103007A_div").html(html);
}

$(document).ready(function() {
	init007ARadio();
});