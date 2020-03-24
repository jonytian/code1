$(document).ready(function() {
	loadEditFrmData($("#edit-frm-enterpriseId").val());
});

function getApiName(){
	return "enterpriseConfig";
}

function loadEditFrmDataSuccessHandler(result) {
	var deviceLimit=result.deviceLimit;
	var html;
	if(deviceLimit){
		html="最大允许接入"+deviceLimit+"台";
	}else{
		html="不限制";
	}
	$("#device_limit").html(html);
	
	html="";
	var messageNumLimit=result.messageNumLimit;
	if(messageNumLimit){
		var arr = messageNumLimit.split(";");
		for(var i=0;i<arr.length;i++){
			var a = arr[i].split(",");
			html+="<div class='message_con'><label>流量限制：</label><p>"+a[1]+"条/"+a[0]+"分钟（每个终端设备每"+a[0]+"分钟不能超过"+a[1]+"条数据）</p></div>"
		}
	}
	var messageByteLimit=result.messageByteLimit;
	if(messageByteLimit){
		var arr = messageByteLimit.split(";");
		for(var i=0;i<arr.length;i++){
			var a = arr[i].split(",");
			html+="<div class='message_con'><label>流量限制：</label><p>"+a[1]+"M/"+a[0]+"分钟（每个终端设备每"+a[0]+"分钟不能超过"+a[1]+"M数据）</p></div>"
		}
	}
	$("#message_limit_div").append(html);
	
	var checkboxStyle = " style='margin-left:5px; margin-right:0px;' "
	html="";
	var upMessageLimit=result.upMessageLimit;
	if(upMessageLimit){
		var arr = upMessageLimit.split(",");
		for(var i=0;i<arr.length;i++){
			var s = deviceMessageInfoMap.get(arr[i]);
			html+="<input "+checkboxStyle+" disabled type='checkbox' checked=true>"+s;
		}
	}else{
		var arr = deviceMessageInfoMap.getKeys();
		for(var i=0;i<arr.length;i++){
			var messageId = arr[i];
			if(messageId.charAt(0)=="0" || messageId.charAt(0)=="1" ){
				var s = deviceMessageInfoMap.get(messageId);
				html+="<input "+checkboxStyle+" disabled type='checkbox' checked=true>"+s;
			}
		}
	}
	$("#up_message_limit_div").html(html);
	

	html="";
	var downMessageLimit=result.downMessageLimit;
	if(downMessageLimit){
		var arr = downMessageLimit.split(",");
		for(var i=0;i<arr.length;i++){
			var s = deviceMessageInfoMap.get(arr[i]);
			html+="<input "+checkboxStyle+" disabled type='checkbox' checked=true>"+s;
		}
	}else{
		var arr = deviceMessageInfoMap.getKeys();
		for(var i=0;i<arr.length;i++){
			var messageId = arr[i];
			if(messageId.charAt(0)=="8" || messageId.charAt(0)=="9"){
				var s = deviceMessageInfoMap.get(messageId);
				html+="<input "+checkboxStyle+" disabled type='checkbox' checked=true>"+s;
			}
		}
	}
	$("#down_message_limit_div").html(html);
}