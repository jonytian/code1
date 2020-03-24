var current_dialog_id ="send-terminal-parameter-heartbeat-cmd-dlg";

$(document).ready(function() {
	initTree("group-tree");
	initRadio1("cmd_setting_81030050_div");
	initRadio("cmd_setting_81030052_div");
	initRadio("cmd_setting_81030053_div");
	initRadio("cmd_setting_81030054_div");
});

function showCmdDialog(dialogId){
	if(current_dialog_id!=dialogId){
		$("#"+current_dialog_id).hide();
	}
	current_dialog_id = dialogId;
	$("#"+dialogId).show();
	$("#legend").html($("#" + dialogId).attr("title"));
}

function initRadio(divId){
	var html="<div class='message_con'>";
	var count=0;
	for(var i=0;i<32;i++){
		var alarmName = carAlarmInfoMap.get(i);
		if(!alarmName || alarmName=='未定义'){
			continue;
		}
		if(count>0&&count%3==0){
			html+="</div><div class='message_con'>"
		}
		count++;
		html+="<label style='width:200px;'>"+alarmName+"：</label>开<input checked='checked' type='radio' value='1' name='bit"+i+"'>关<input type='radio' value='0' name='bit"+i+"'>";
	}
	html+="</div>";
	$("#"+divId).html(html);
}

function initRadio1(divId){
	var html="<div class='message_con'>";
	var count=0;
	for(var i=0;i<32;i++){
		var alarmName = carAlarmInfoMap.get(i);
		if(!alarmName || alarmName=='未定义'){
			continue;
		}
		if(count>0&&count%3==0){
			html+="</div><div class='message_con'>"
		}
		count++;
		html+="<label style='width:200px;'>"+alarmName+"：</label>开<input checked='checked' type='radio' value='0' name='bit"+i+"'>关<input type='radio' value='1' name='bit"+i+"'>";
	}
	html+="</div>";
	$("#"+divId).html(html);
}

function doCallback(fn,args){    
    return fn.apply(this, args);  
} 

function sendTerminalParameterQueryCmd(){
	var deviceIds = [];
	$("#multiple-selected-target option").map(function() {
		deviceIds.push($(this).val());
	});
	
	if(deviceIds.length == 0 || deviceIds.length > 1 ){
		showErrorMessage("请选择一辆在线的车辆！");
		return;
	}
	var form = $("#" + current_dialog_id + "-frm").serializeObject();;
	var paramkeys = form.paramkeys;
	var params = paramkeys.split(",");
	
	var paramList=[];
	for ( var i in params) {
		var param = params[i];
		var arr = param.split("_");
		if(arr.length > 1){
			paramList.push(arr[1]);
		}else{
			paramList.push(arr[0]);
		}
	}

	var deviceId = deviceIds[0];
	var resultHandle = "query"+(current_dialog_id + "ResultHandle").replace(/-/g,"");
	try{
		resultHandle = eval(resultHandle);
	}catch(e){}
	
	if(typeof(resultHandle)=="function"){
		doSendTerminalParameterQuery(deviceId,paramList,resultHandle);
	}else{
		doSendTerminalParameterQuery(deviceId,paramList,queryTerminalParameterResultHandle);
	}
}

function queryTerminalParameterResultHandle(result){
	for(var key in result){
		var val = result[key];
		if(/^[A-Fa-f0-9]+$/.test(val)){
			$("#p"+key).val(parseInt(val,16));
		}else{
			$("#p"+key).val(val);
		}
	}
}

function set005XRadio(param,div){
	$("#"+div).find("input").each(function(){
		try{
			var name = $(this).attr("name");
			var val = $(this).val();
			var index = parseInt(name.replace("bit",""));
			if(((param&(1<<index))>>index)==parseInt(val)){
		    	$(this).attr("checked",true);
			}else{
				$(this).attr("checked",false);
			}
		}catch(e){}
	});
}

function querysend81030050cmddlgResultHandle(result){
	var param = parseInt(result["0050"],16);
	set005XRadio(param,"cmd_setting_81030050_div");
}

function querysend81030052cmddlgResultHandle(result){
	var param = parseInt(result["0052"],16);
	set005XRadio(param,"cmd_setting_81030052_div");
}

function querysend81030053cmddlgResultHandle(result){
	var param = parseInt(result["0053"],16);
	set005XRadio(param,"cmd_setting_81030053_div");
}

function querysend81030054cmddlgResultHandle(result){
	var param = parseInt(result["0054"],16);
	set005XRadio(param,"cmd_setting_81030054_div");
}

function doSendTerminalParameterQuery(deviceId,paramList,resultHandle){
	//查询参数指令
	if (!isOnline(deviceId)) {
		showErrorMessage("车辆不在线，不能发送指令！");
		return;
	}
	startLoading();

	var url = message_api_server_servlet_path + "/deviceDownMessage/" + deviceId + "/8106.json";
	ajaxAsyncPost(url, { "paramTypes" : paramList }, function(result) {
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
					var paramList = getTerminalParameterSetting(deviceId,data.createTime);
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
	
	var data = {};
	try{
		var callback = "get"+(current_dialog_id + "-frm").replace(/-/g,"");
		var formData = doCallback(eval(callback),[1]);
		data = formData.param;
	}catch(e){
	}

	if(!formData){
		var formData = form.serializeObject();
		var paramkeys = formData.paramkeys;
		var params = paramkeys.split(",");
		var bool = false;
		for ( var i in params) {
			var param = params[i];
			var a = param.split("_");
			if (a[0] == "d" || a[0] == "w") {
				var val = formData[param];
				if (!val) {
					continue;
				}
				data[a[1]] = parseInt(val).toString(16);
			} else if (a[0] == "s") {
				var val = formData[param];
				if (!val) {
					continue;
				}
				data[a[1]] = val;
			}else if(a[0] == "b"){
				var flag = "";
				for(var i=0;i<32;i++){
					var val = formData["bit"+i];
					if(val){
						flag+=val;
					}else{
						flag+="0";
					}
				}
				data[a[1]]=parseInt(flag,2).toString(16);
			}
			bool = true;
		}
		
		if (!bool) {
			showErrorMessage("请设置参数值！");
			return;
		}
	}

	var deviceIds = "";
	$("#multiple-selected-target option").map(function() {
		deviceIds += "," + $(this).val();
	});
	if(deviceIds==""){
		showErrorMessage("请选择设备！");
		return;
	}
	deviceIds = deviceIds.substr(1);
	var messageId = formData.messageId;
	doSendCmd(deviceIds,messageId, {
		"paramList" : data
	});
}

function doSendCmd(deviceIds, messageId, data) {
	var url = message_api_server_servlet_path + "/batch/deviceDownMessage/" + messageId + ".json";
	var desc = $("#" + current_dialog_id).attr("title");
	if (desc) {
		data.desc = desc;
	}
	data.deviceIds = deviceIds;
	$('.bule').attr('disabled',"true");
	ajaxAsyncPost(url, data, function(result) {
		if (result.code != 0) {
			showErrorMessage(result.message);
		} else {
			showMessage("指令已处理！");
			setTimeout(function(){
				window.parent.frames["content-frame"].location.reload(true);
			},2000)
			
		}
	},true);
}

function getInitTreeNodes() {
	return getDefaultGroupTree(group_type_car);
}

function onExpandTreeNode(treeId, parent){
	onExpandGroupTreeNode(group_type_car,treeId, parent);
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