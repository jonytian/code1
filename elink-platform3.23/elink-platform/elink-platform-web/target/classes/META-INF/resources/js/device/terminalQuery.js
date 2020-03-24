var current_dialog_id ="send-query-video-parameter-cmd-dlg";

$(document).ready(function() {
	initTree("group-tree");
});

function showCmdDialog(dialogId){
	if(current_dialog_id!=dialogId){
		$("#"+current_dialog_id).hide();
	}
	current_dialog_id = dialogId;
	$("#"+dialogId).show();
	$("#legend").html($("#" + dialogId).attr("title"));
	$("#fieldset-result").hide();
	$("#"+current_dialog_id+"-result").hide();
}

function doCallback(fn,args){    
    return fn.apply(this, args);  
} 

function sendTerminalQueryCmd(){
	var deviceIds = [];
	$("#multiple-selected-target option").map(function() {
		deviceIds.push($(this).val());
	});
	
	if(deviceIds.length == 0 || deviceIds.length > 1 ){
		showErrorMessage("请选择一辆在线的车辆！");
		return;
	}
	var form = $("#" + current_dialog_id + "-frm").serializeObject();
	
	var formData = null;
	try{
		var callback = "get"+(current_dialog_id + "-frm").replace(/-/g,"");
		formData = doCallback(eval(callback),[1]);
	}catch(e){
	}
	
	if(formData){
		messageBody = formData;
	}else{
		messageBody = form;
	}
	var resultHandle = "query"+(current_dialog_id + "ResultHandle").replace(/-/g,"");
	doSendTerminalQueryCmd(deviceIds[0],form.messageId,messageBody,eval(resultHandle));
	$("#"+current_dialog_id+"-result").show();
	$("#fieldset-result").show();
}

function getsendquerydevicestatecmddlgfrm(){
	var form = $("#" + current_dialog_id + "-frm").serializeObject();
	var data = {};
	data.type=form.type;
	var idList = [];
	idList.push(form.devieId);
	data.idList= idList;
	return data;
}

function getsenddeviecesystemcmddlgfrm(){
	return getsendquerydevicestatecmddlgfrm();
}

function querysendqueryvideoparametercmddlgResultHandle(data){
	for(var key in data){
		$("#"+key).html(data[key]);
	}
	
	var audioCoding = data.audioCoding;
	if(audioCoding==8){
		audioCoding="G.726";
	}else if(audioCoding==9){
		audioCoding="G.729";
	}else if(audioCoding==19){
		audioCoding="ACC";
	}else if(audioCoding==25){
		audioCoding="MP3";
	}else{
		audioCoding="其他";
	}
	$("#audioCoding").html(audioCoding);

	var desc = ["8kHz","22.5kHz","44.1kHz","48kHz"];
	var samplingRate = desc[data.samplingRate];
	$("#samplingRate").html(samplingRate);
	
	desc = ["8位","16位","32位"];
	var samplingNum = desc[data.samplingNum];
	$("#samplingNum").html(samplingNum);
	
	var videoCoding = data.videoCoding;
	if(videoCoding==98){
		videoCoding="H.264";
	}else if(videoCoding==99){
		videoCoding="H.265";
	}else if(videoCoding==100){
		videoCoding="AVS";
	}else if(videoCoding==101){
		videoCoding="SVAC";
	}else{
		videoCoding="其他";
	}
	$("#videoCoding").html(videoCoding);
}

function querysendquerydevicestatecmddlgResultHandle(data){
	var messageList =data.messageList;
	if(messageList && messageList.length > 0){
		var item = messageList[0];
		var desc = ["正常工作","待机状态","升级维护","设备异常"];
		var state = desc[item.state+1];
		$("#state").html(state?state:"断开连接");
		
		var desc = ["正常工作","待机状态","升级维护","设备异常"];
		var state = desc[item.state+1];
		$("#state").html(state?state:"断开连接");
		
		var desc = ["摄像头异常","主存储器异常","辅存储器异常","红外补光异常","扬声器异常","电池异常","","","","","通讯模块异常","定位模块异常"];
		var alarm ="";
		for(var i=0;i<desc.length;i++){
			var s = desc[i];
			if(!s){
				continue;
			}
			if(((item.alarm&(1<<i))>>i)==1){
				alarm += "," + s;
			}
		}
		if(alarm){
			alarm = alarm.substr(1);
		}else{
			alarm="无";
		}
		$("#alarm").html(alarm);
	}
}

function querysenddeviecesystemcmddlgResultHandle(data){
	var messageList =data.messageList;
	if(messageList && messageList.length > 0){
		var item = messageList[0];
		for(var key in item){
			$("#"+key).html(item[key]);
		}
	}
}

function doSendTerminalQueryCmd(deviceId,messageId,messageBody,resultHandle){
	//查询参数指令
	if (!isOnline(deviceId)) {
		showErrorMessage("车辆不在线，不能发送指令！");
		return;
	}
	startLoading();

	var url = message_api_server_servlet_path + "/deviceDownMessage/" + deviceId + "/"+messageId+".json";
	ajaxAsyncPost(url, messageBody, function(result) {
		if (result.code != 0) {
			showErrorMessage(result.message);
			endLoading();
			return;
		} else {
			var data = result.data;
			var count = 0;
			var messageStateInterval = setInterval(function() {
				var state = getDownstreamMessageState(data.id);
				if(state == 0 || state == 5){
					clearInterval(messageStateInterval);
					count = 0;
					var messageRespQueryInterval = setInterval(function() {
					var paramList = getTerminalQueryResult(deviceId,data.createTime);
					if(paramList){
						resultHandle(paramList);
						clearInterval(messageRespQueryInterval);
						endLoading();
					}

					//10秒内无响应停止
					if( (1 * count++) >=30){
						showErrorMessage("查询终端参数超时，终端设备没有实现该功能或者上报数据超时！");
						clearInterval(messageRespQueryInterval);
						endLoading();
					}
				  }, 1 * 1000);
				}else if(state==4){
					
				}else{
					showErrorMessage("查询终端参数失败，终端设备没有实现该功能或者上报数据超时！");
					clearInterval(messageStateInterval);
					endLoading();
				}

				//10秒内无响应停止
				if( (1 * count++) >=30){
					showErrorMessage("查询终端参数超时，终端设备没有实现该功能或者上报数据超时！");
					clearInterval(messageStateInterval);
					endLoading();
				}
			}, 1 * 1000);
		}
	});
}

function getResponseMessageId(){
	if(current_dialog_id=="send-query-video-parameter-cmd-dlg"){
		return "9003";
	}
	return "0900";
}

function getTerminalQueryResult(deviceId,createTime){
	var paramList = null;
	var url = management_api_server_servlet_path + "/common/query/deviceUpMessage.json?countable=false&pageSize=1&orderBy=createTime&desc=true";
	var con = {};
	con["deviceId.eq"] = deviceId;
	con["messageId.eq"] = getResponseMessageId();
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
				return $.evalJSON(item.messageBody);
			}
		}
	}
	return paramList;
}

function getInitTreeNodes() {
	return getDefaultGroupTree(group_type_device);
}

function onExpandTreeNode(treeId, parent){
	onExpandGroupTreeNode(group_type_device,treeId, parent);
}

function onClickTreeNode(treeId, node) {
	queryDeviceByGroup(node, 1, 100);
}

function queryDeviceByGroup(node, pageNo, pageSize) {
	if(pageNo==1){
		$('#multiple-select-source').empty();
	}
	var url = management_api_server_servlet_path + "/common/query/carDevice.json?select=deviceId,plateNumber,deviceState&countable=false&pageSize=" + pageSize + "&pageNo=" + pageNo + "&orderBy=createTime&desc=true";
	var data = {};
	if(node.type=="ent"){
		data["enterpriseId.eq"] = node.id;
	}else{
		var parent = node.getParentNode();
		while(parent && parent.type && parent.type!='ent'){
			parent = parent.getParentNode();
		}
		if(parent && parent.type =='ent'){
			data["enterpriseId.eq"] = parent.id;
		}
		data["groupId.eq"] = node.id;
	}
	
	data["deviceState.gte"] = 2;
	data["deviceState.lte"] = 3;
	var result = ajaxSyncPost(url, data);
	if (result.code != 0) {
		showErrorMessage(result.message);
	} else {
		var list = result.data;
		if (list && list.length > 0) {
			var map = new JsMap();
			for (var i = 0; i < list.length; i++) {
				var item = list[i];
				var name = item[1]+"("+(item[2]==3?"在线":"离线")+")"
				map.put(item[0], name);
			}

			$("#multiple-select-source option").map(function() {
				var id = $(this).val();
				var name = $(this).text();
				map.put(id, name);
			});

			var keys = map.getKeys();
			$('#multiple-select-source').empty();
			for (var i = 0, l = keys.length; i < l; i++) {
				var key = keys[i];
				$("#multiple-select-source").append("<option value='" + key + "'>" + map.get(key) + "</option>");
			}

			if (list.length == pageSize) {
				pageNo++;
				queryDeviceByGroup(node, pageNo, pageSize)
			}
		}
	}
}