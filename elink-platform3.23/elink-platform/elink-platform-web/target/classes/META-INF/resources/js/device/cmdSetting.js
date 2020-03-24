$(document).ready(function() {
	var objects = $("select[name=channelId]");
	for (var i = 0; i < objects.length; i++) {
		initChannelOption(objects[i]);
	}
});

function initChannelOption(obj) {
	var keys = channelMap.getKeys();
	for (var i = 0; i < keys.length; i++) {
		var key = keys[i];
		var val = channelMap.get(key);
		$(obj).append("<option value='" + key + "'>" + val + "</option>");
	}
}

function textInfoOnchange(flag,index,obj){
	var val = $("#"+flag).val();
	var sel = $(obj).val();
	$("#"+flag).val(val.substr(0,index)+sel+val.substr(index+1));
}

function openDvrQueryCmdDialog(commandWord, title) {
	var dialogId = "send-dvr-data-record-cmd-dlg";
	$("#" + dialogId + "-frm-commandWord").val(commandWord);
	showCmdDialog(dialogId);
}

function openDvr2013QueryCmdDialog(commandWord, title) {
	var dialogId = "send-dvr2013-data-record-cmd-dlg";
	$("#" + dialogId + "-frm-commandWord").val(commandWord);
	showCmdDialog(dialogId);
}

function showTerminalParameterSettingCmdDialog(dialogId){
	var deviceId = current_selected_device.id;
	//查询参数指令
	if (!isOnline(deviceId)) {
		showErrorMessage("终端不在线，不能发送指令！");
		return;
	}

	startLoading();
	var paramList = getTerminalParameterSetting(deviceId);
	var isAll = setTerminalParameter(dialogId,paramList);
	if(isAll){
		showCmdDialog(dialogId);
		endLoading();
		return;
	}

	var url = message_api_server_servlet_path + "/deviceDownMessage/" + deviceId + "/8104.json";
	ajaxAsyncPost(url, {}, function(result) {
		if (result.code != 0) {
			showCmdDialog(dialogId);
			endLoading();
			return;
		} else {
			var data = result.data;
			var messageStateInterval = setInterval(function() {
				var state = getDownstreamMessageState(data.id);
				if(state == 0 || state == 5){
					clearInterval(messageStateInterval);
					count = 0;
					var messageRespQueryInterval = setInterval(function() {
					var paramList = getTerminalParameterSetting(deviceId,data.createTime);
					if(paramList){
						setTerminalParameter(dialogId,paramList);
						showCmdDialog(dialogId);
						clearInterval(messageRespQueryInterval);
						endLoading();
					}

					//10秒内无响应停止
					if( (1 * count++) >=10){
						showCmdDialog(dialogId);
						clearInterval(messageRespQueryInterval);
						endLoading();
					}
				  }, 1 * 1000);
				}else if(state==4){
				}else{
					showCmdDialog(dialogId);
					clearInterval(messageStateInterval);
					endLoading();
				}

				//10秒内无响应停止
				if( (1 * count++) >=10){
					showCmdDialog(dialogId);
					clearInterval(messageStateInterval);
					endLoading();
				}
			}, 1 * 1000);
		}
	});
}

function setTerminalParameter(dialogId,paramList){
	if(paramList == null){
		return false;
	}
	var forms= $("#"+dialogId+"-frm").find("input,select");
	var isAll = true;
	forms.each(function(index,e){
		var name = e.name;
		if(name.indexOf("_") != -1){
			var arr = name.split("_");
			var val = paramList[arr[1]];
			if(val){
				if(name.startsWith("s_")){
					$(e).val(val);
				}else {
					//dword,word,byte是十六进制字符串
					$(e).val(parseInt(val,16));
				}
			}else{
				isAll = false;
				$(e).val("");
			}
		}
	});
	return isAll;
}

function getTerminalParameterSetting(deviceId,createTime){
	var paramList = null;
	var url = management_api_server_servlet_path + "/common/query/deviceUpMessage.json?countable=false&pageSize=1&orderBy=createTime&desc=true";
	var con = {};
	con["deviceId.eq"] = deviceId;
	con["messageId.eq"] = "0104";
	if(createTime){
		con["createTime.gte"] = createTime;
	}
	var result = ajaxSyncPost(url,con);
	if (result.code != 0) {
		showErrorMessage(result.message);
	} else {
		var list = result.data;
		if(list && list.length > 0){
			var item = list[0];
			if(item.messageBody){
				var messageBody = $.evalJSON(item.messageBody);
				paramList = messageBody.paramList;
			}
		}
	}
	return paramList;
}

function sendTerminalParameterSettingCmd() {
	var form = $("#" + current_dialog_id + "-frm");
	if (!form.valid()) {
		return;
	}
	var formData = form.serializeObject();
	var paramkeys = formData.paramkeys;
	var params = paramkeys.split(",");
	var data = {};
	var bool = false;
	for ( var i in params) {
		var param = params[i];
		var val = formData[param];
		if (!val) {
			continue;
		}
		var a = param.split("_");
		if (a[0] == "d" || a[0] == "w") {
			data[a[1]] = parseInt(val).toString(16);
		} else if (a[0] == "s") {
			data[a[1]] = val;
		}
		bool = true;
	}
	if (!bool) {
		showErrorMessage("请设置参数值！");
		return;
	}
	var deviceId = formData.deviceId;
	var messageId = formData.messageId;
	doSendCmd(deviceId, messageId, {
		"paramList" : data
	})
}

function sendFollowCarCmd() {
	var form = $("#" + current_dialog_id + "-frm");
	if (!form.valid()) {
		return;
	}
	var data = form.serializeObject();
	var interval = data.interval;
	sendCmd();
	if (interval > 0 && !isOnline(data.deviceId)) {
		startFollowCar(interval);
		setTimeout("stopFollowCar()", (data.expTime) * 1000);
	}
}

function sendCmd() {
	var form = $("#" + current_dialog_id + "-frm");
	if (!form.valid()) {
		return;
	}
	var data = form.serializeObject();
	var deviceId = data.deviceId;
	var messageId = data.messageId;
	doSendCmd(deviceId, messageId, data);
}

function doSendCmd(deviceId, messageId, data) {
	if (!isOnline(deviceId)) {
		showErrorMessage("终端不在线，不能发送指令！");
		return;
	}
	var url = message_api_server_servlet_path + "/deviceDownMessage/" + deviceId + "/" + messageId + ".json";
	var desc = $("#" + current_dialog_id).attr("title");
	if (desc) {
		data.desc = desc;
	}
	ajaxAsyncPost(url, data, function(result) {
		if (result.code != 0) {
			showErrorMessage(result.message);
		} else {
			closeDialog();
			setCmdInfo(result.data);
			showMessage("指令已下发，等待网关处理中");
		}
	});
}

function sendTestCmd() {
	var form = $("#" + current_dialog_id + "-frm");
	if (!form.valid()) {
		return;
	}
	var data = form.serializeObject();
	var deviceId = data.deviceId;
	if (!isOnline(deviceId)) {
		showErrorMessage("终端不在线，不能发送指令！");
		return;
	}
	var url = message_api_server_servlet_path + "/deviceDownMessage/"
			+ deviceId + "/" + data.messageId + ".json";
	data = $.evalJSON($("#send-test-cmd-dlg-frm-messageBody").val());
	ajaxAsyncPost(url, data, function(result) {
		if (result.code != 0) {
			showErrorMessage(result.message);
		} else {
			closeDialog();
			showMessage("指令已下发，网关处理中，消息流水号：" + result.data.messageSeq);
		}
	});
}

function setCmdInfo(result){
	var messageBody = $.evalJSON(result.messageBody);
	var content = messageBody.name +"下发监控指令："+ messageBody.desc;
	content = "<span>" + new Date().format("hh:mm:ss") + "</span> 【监控指令】" + content + "<hr>";
	addRealTimeMessage(content);
}