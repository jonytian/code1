var file_server_host;
var realTimeMessageQueue = new Queue();
var alarmSetting,isAudioPlaying = false,audioQueue = new Queue();
var TREE_MAX_CHANNEL = 4,MAX_VEDEO = 16,heartbeatInterval,clinetId;

$(function() {
	initNav();
	setTimeout("startDwr()", 5 * 1000);

	alarmNotifyOverview();
	checkTodoAlarm();
	setInterval(function() {
		alarmNotifyOverview();
		checkTodoAlarm();
	}, 1 * 60 * 1000);

	setInterval(function() {
		playAudio();
	}, 1 * 1000);
	
	loadFileServer();
	loadAlarmVoiceSetting();


	clinetId = getClinetId();
	loadRtmpServer();


});

function loadFileServer(){
	var params = {};
	params["type.eq"]= DICTIONARY_PROVIDER_API;
	params["code.eq"]= "file-server";
	var url = management_api_server_servlet_path + "/system/dictionary/dictionary.json";
	var result = ajaxSyncPost(url, params);
	if (result.code != 0) {
		showErrorMessage(result.message);
	} else {
		var list = result.data;
		if(list && list.length>0){
			var item = list[0];
			var content = $.evalJSON(item.content);
			file_server_host = content.url;
		}
	}
}

function getFileServer(){
	return file_server_host;
}

function loadAlarmVoiceSetting(){
	var url = management_api_server_servlet_path + "/common/enterpriseConfig/" + getUserEnterpriseId() + ".json";
	var data = {}
	ajaxAsyncGet(url, data, function(result) {
		if (result.code != 0) {
			showErrorMessage(result.message);
		} else {
			var data = result.data;
			if(data && data.alarmSetting){
				alarmSetting =$.evalJSON(data.alarmSetting);
			}
		}
	});
}

function startDwr() {
	// 轮询模式与comet模式的反向Ajax所需要的配置
	// dwr.engine.setActiveReverseAjax(true);

	// 重点关于解决页面每刷新一次会多创建一个新的ScriptSession的解决方法
	// 由于ScriptSession的创建机制不同于HttpSession，它会在每次页面刷新的时候都会重新创建，而销毁机制却是失去连接
	// 或者失效之后一定时间才会自动销毁，这样就可能造成服务端可能就保存了很多的无用的ScriptSession,所以不仅仅会影响
	// 性能问题，更重要的是，可能就不能实现你想要的功能。
	// 解决方法是在接收消息的页面，也就是你调用dwr.engine.setActiveReverseAjax(true);的页面调用一个dwr的方法。
	// dwr.engine.setNotifyServerOnPageUnload(true);
	// 这个方法的功能就是在销毁或刷新页面时销毁当前ScriptSession，这样就保证了服务端获取的ScriptSession集合中没有
	// 无效的ScriptSession对象。
	dwr.engine.setActiveReverseAjax(true);
	dwr.engine.setNotifyServerOnPageUnload(true);
	dwr.engine.setErrorHandler(dwrErrorHandler);
	// 根据业务设置过滤条件
	DwrMessagePusher.init();
}

function dwrErrorHandler(errorMessage, exception) {
	startDwr();
}

function handleDwrPushMsg(msg) {
	try{
		var message = $.evalJSON(msg);
		var messageBody = message.message;
		if (message.type == 1) {
			// 车辆上下线
			onlineMessageHandler(messageBody);
		} else if (message.type == 2) {
			// 车辆告警信息
			deviceAlarmMessageHandler(messageBody);
		} else if (message.type == 4) {
			// 网关重启
			gatewayRestartMessageHandler(messageBody);
		} else if (message.type == 5) {
			// 流量异常告警
			dataLimitAlarmMessageHandler(messageBody);
		} else if (message.type == 6) {
			bizStateMessageHandler(messageBody);
		} else if (message.type == 9) {
			// 多媒体信息
			mediaMessageHandler(messageBody);
		}else if (message.type == 10) {
			// 文件上传通知
			mediaUploadMessageHandler(messageBody);
		}else if (message.type == 11) {
			//下行消息应答结果
			downstreamMessageResultHandler(messageBody);
		}else if (message.type == 12) {
			//下行失败消息通知
			downstreamErrorMessageHandler(messageBody);
		}else {
			defaultMessageHandler(messageBody);
		}
	}catch(e){}
}

function addRealTimeMessage(content) {
	try {
		var frame = $("#main-frame")[0].contentWindow;
		if (typeof (eval(frame.addRealTimeMessage)) == "function") {
			// 添加队列中信息
			while (!realTimeMessageQueue.isEmpty()) {
				frame.addRealTimeMessage(realTimeMessageQueue.deQueue());
			}
			frame.addRealTimeMessage(content+"<hr>");
			return;
		}
	} catch (e) {
	}
	// 当前页面不是监控页时放入队列中
	realTimeMessageQueue.enQueue(content+"<hr>");
}

function notifyMessage(content) {
	$("#main-frame")[0].contentWindow.showMessage(content);
}

function onlineMessageHandler(messageBody) {
	liveVideoHandler(messageBody);
	var deviceId = messageBody.deviceId;
	var online = (messageBody.state == 1 ? "上线" : "下线");
	var content = (messageBody.plateNumber || messageBody.deviceName) + online;

	if(alarmSetting && alarmSetting.onOffLineDialogEnable && alarmSetting.onOffLineDialogEnable=="true"){
		notifyMessage(content);
	}

	content = "<span>" + new Date().format("hh:mm:ss") + "</span> 【车辆上下线】" + content + "！";
	addRealTimeMessage(content);
	
	try {
		var frame = $("#main-frame")[0].contentWindow;
		if (typeof (eval(frame.onlineMessageHandler)) == "function") {
			frame.onlineMessageHandler(messageBody);
		}
	} catch (e) {
	}
}

// 检测是否有可用的流媒体
function loadRtmpServer(){
	var params = {};
	params["type.eq"]= DICTIONARY_PROVIDER_API;
	params["code.eq"]= "1078-video";
	var url = management_api_server_servlet_path + "/system/dictionary/dictionary.json";
	var result = ajaxSyncPost(url, params);
	if (result.code != 0) {
		showErrorMessage(result.message);
	} else {
		var list = result.data;
		if(list && list.length>0){
			var item = list[0];
			var content = $.evalJSON(item.content);
			rtmp_server_url = content.url;
		}else{
			showMessage("无可用流媒体服务器，请联系系统管理员配置！");
		}
	}
}
// clinetid
function getClinetId() {
	var c_name = "DWRSESSIONID=";
	var cookie = document.cookie;
	if (cookie.length > 0) {
		var c_start = cookie.indexOf(c_name);
		if (c_start != -1) {
			c_start = c_start + c_name.length;
			var c_end = cookie.indexOf(";", c_start);
			if (c_end == -1)
				c_end = cookie.length;
			return "browser_" + unescape(cookie.substring(c_start, c_end));
		}
	}
	return "browser_" + generateUUID();
}

function generateUUID() {
	var d = new Date().getTime();
	var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g,
		function(c) {
			var r = (d + Math.random() * 16) % 16 | 0;
			d = Math.floor(d / 16);
			return (c == 'x' ? r : (r & 0x3 | 0x8)).toString(16);
		});
	return uuid;
}
//检测上线
function liveVideoHandler(messageBody){
	var deviceId = messageBody.deviceId;
	var online =  messageBody.state ;
	var simCode =  messageBody.simCode ;
	var myCars=[
		"346039404143","346039404168","346039404176", "346039404150","346039404127",
		"346039404192","346039404184","346039404291","346039404267", "346039404283",
		"346039404275","346039404226","346039404218","346039404200","346039404259",
		"346039404234","346039404242","346039404325","346039404317","346039404309"];

	if (deviceId == null || deviceId == "" || deviceId == undefined ){
		return;
	}
	if (simCode == null || simCode == "" || simCode == undefined ){
		return;
	}
	for (var i=0;i<myCars.length;i++)
	{
		if(simCode == myCars[i] && online == 1){
			startVideo(deviceId,1);
			startVideo(deviceId,2);
		}
	}

}




//上线开直播
function startVideo(deviceId,channelId){
	if(rtmp_server_url==""){
		showErrorMessage("无可用流媒体服务器，请联系系统管理员配置！");
		return false;
	}
	if(isOnline(deviceId)){
		if(checkVideo(deviceId,channelId)){
			//启动视频
			var url = message_api_server_servlet_path + "/video/1078/" + deviceId + ".json?clinetId=" + clinetId;
			var data = {};
			data.desc = "实时视频";
			data.deviceId = deviceId;
			data.messageId = "9101";
			data.channelId = channelId;
			data.dataType = 0;
			data.streamType = 1;
			var device = getDeviceCache(deviceId);
			data.name = device.name;
			var result = ajaxSyncPost(url, data);
			if (result.code != 0) {
				showErrorMessage(result.message);
				return false;
			} else {
				showErrorMessage("开启直播");
			}
		}
		removeInfowindow();
	}else{
		showErrorMessage("开启直播失败");
		return false;
	}
	return true;
}







function bizStateMessageHandler(messageBody){
	var bizState = parseInt(messageBody.bizState);
	var deviceId = messageBody.deviceId;

	var content = (messageBody.plateNumber || messageBody.deviceName) + deviceBizStateMap.get(bizState);
	notifyMessage(content);

	content = "<span>" + new Date().format("hh:mm:ss") + "</span> 【车辆运行状态】" + content + "！";
	addRealTimeMessage(content);
	
	try {
		var frame = $("#main-frame")[0].contentWindow;
		if (typeof (eval(frame.bizStateMessageHandler)) == "function") {
			frame.bizStateMessageHandler(messageBody);
		}
	} catch (e) {
	}
}

function deviceAlarmMessageHandler(messageBody) {
	var deviceName = (messageBody.plateNumber || messageBody.deviceName);
	var alarmTime = new Date().format("hh:mm:ss");
	var alarmList = messageBody.alarmList;
	for(var i=0;i<alarmList.length;i++){
		var alarmStr = "";
		var item = alarmList[i];
		if(item.type <= 4){
			for(var j=0;j<50;j++){
				if(item["a"+j]){
					alarmStr += "," + carAlarmInfoMap.get(j);
				}
			}
		}else if(item.type==5){
			//视频告警
			var videoAlarm = messageBody.va;
			for(var j=0;j<videoAlarmDesc.length;j++){
				var s = videoAlarmDesc[j];
				if(s && ((videoAlarm&(1<<j))>>j)==1 ){
					alarmStr += "," + s;
				}
			}
		}else if(item.type==6){
			alarmStr += getTjsatlAlarmDesc(item);
		}
		
		if (alarmStr) {
			if (alarmStr.length > 80) {
				alarmStr = alarmStr.substr(1, 80) + "…";
			} else {
				alarmStr = alarmStr.substr(1);
			}
		}
		
		if(checkAlarmNotifyDialog(item.type,item)){
			notifyMessage("【" + deviceName + "告警】" + alarmStr);
		}
		var content = "<span>" + alarmTime + "</span> 【车辆告警】" +deviceName+ alarmStr;
		addRealTimeMessage(content);
		addAlarmNotifyVoice(item.type,item);
	}
}

function checkAlarmNotifyDialog(alarmType,item){
	if(alarmSetting){
		return (alarmSetting.faultAlarmDialogEnable=="true" && alarmType==1 ||
		alarmSetting.drivingBehaviorAlarmDialogEnable=="true" && alarmType==2 ||
		alarmSetting.urgentAlarmDialogEnable=="true" && alarmType==3) || 
		alarmSetting.adasAlarmDialogEnable=="true" && alarmType==6 && checkTjsatlAlarm(item,"adas") ||
		alarmSetting.dsmAlarmDialogEnable=="true" && alarmType==6 && checkTjsatlAlarm(item,"dsm") ||
		alarmSetting.tpmsAlarmDialogEnable=="true" && alarmType==6 && checkTjsatlAlarm(item,"tpms") ||
		alarmSetting.bsdAlarmDialogEnable=="true" && alarmType==6 && checkTjsatlAlarm(item,"bsd") 
	}
	return true;
}

function checkTjsatlAlarm(item,s){
	for ( var key in item){
		if(key.indexOf(s)!=-1){
			return true;
		}
	}
	return false;
}

function addAlarmNotifyVoice(alarmType,item){
	if(alarmSetting){
		if(alarmSetting.faultAlarmVoiceEnable=="true" && alarmType==1){
			var times = (alarmSetting.faultAlarmVoiceTime / 2);
			var file = alarmSetting.faultAlarmVoice;
			audioQueue.enQueue({"file":file,"times":times});
		}else if(alarmSetting.drivingBehaviorAlarmVoiceEnable=="true" && alarmType==2){
			var times = (alarmSetting.drivingBehaviorAlarmVoiceTime / 2);
			var file = alarmSetting.drivingBehaviorAlarmVoice;
			audioQueue.enQueue({"file":file,"times":times});
		}else if(alarmSetting.urgentAlarmVoiceEnable=="true" && alarmType==3){
			var times = (alarmSetting.urgentAlarmVoiceTime / 2);
			var file = alarmSetting.urgentAlarmVoice;
			audioQueue.enQueue({"file":file,"times":times});
		}else if(alarmSetting.adasAlarmVoiceEnable=="true" && alarmType==6 && checkTjsatlAlarm(item,"adas")){
			var times = (alarmSetting.adasAlarmVoiceTime / 2);
			var file = alarmSetting.adasAlarmVoice;
			audioQueue.enQueue({"file":file,"times":times});
		}else if(alarmSetting.dsmAlarmVoiceEnable=="true" && alarmType==6 && checkTjsatlAlarm(item,"dsm")){
			var times = (alarmSetting.dsmAlarmVoiceTime / 2);
			var file = alarmSetting.dsmAlarmVoice;
			audioQueue.enQueue({"file":file,"times":times});
		}else if(alarmSetting.tpmsAlarmVoiceEnable=="true" && alarmType==6 && checkTjsatlAlarm(item,"tpms")){
			var times = (alarmSetting.tpmsAlarmVoiceTime / 2);
			var file = alarmSetting.tpmsAlarmVoice;
			audioQueue.enQueue({"file":file,"times":times});
		}else if(alarmSetting.bsdAlarmVoiceEnable=="true" && alarmType==6 && checkTjsatlAlarm(item,"bsd")){
			var times = (alarmSetting.bsdAlarmVoiceTime / 2);
			var file = alarmSetting.bsdAlarmVoice;
			audioQueue.enQueue({"file":file,"times":times});
		}
	}
}

function gatewayRestartMessageHandler(messageBody) {
	var content = "<span>" + new Date().format("hh:mm:ss") + "</span> 【网关重启】" + (messageBody.result == 1 ? "网关重启成功！" : "网关即将重启！");
	addRealTimeMessage(content);
}

function dataLimitAlarmMessageHandler(messageBody) {
	var content="";
	if (messageBody.deviceId) {
		content = (messageBody.plateNumber || messageBody.deviceName);
	} else if (messageBody.ip) {
		content = messageBody.ip;
	}
	content += "上报数据流量异常告警!";
	notifyMessage("【流量异常】" + content);

	content = "<span>" + new Date(messageBody.alarmTime).format("hh:mm:ss") + "</span> 【流量异常】" + content;
	addRealTimeMessage(content);
}

function mediaMessageHandler(messageBody) {
	var content="";
	if (messageBody.mediaType == 0) {
		var mediaUrl = null;
		if(file_server_host){//如果配置了文件服务器，则直接从文件服务器下载
			mediaUrl = file_server_host+(messageBody.filePath).replace(/\\/g,"\/");
		}else{
			//从ftp服务器下载
			mediaUrl = getContextPath() +"/attachment/download.do?url=/media/ftp/download/"+messageBody.id+".json";
		}
		var title = (messageBody.plateNumber || messageBody.deviceName);
		
		if(alarmSetting && alarmSetting.mediaAlarmDialogEnable && alarmSetting.mediaAlarmDialogEnable=="true"){
			$("#main-frame")[0].contentWindow.showImg(title,mediaUrl);
		}

		content = (messageBody.plateNumber || messageBody.deviceName) + "拍照上传!";
	} else if (messageBody.mediaType == 2) {
		// 实时视频
		try{
			$("#main-frame")[0].contentWindow.playVideo(messageBody);
		}catch(e){
		}
		content = (messageBody.plateNumber || messageBody.deviceName) + "实时视频上传!";
	} else if (messageBody.mediaType == 1) {
		try{
			$("#main-frame")[0].contentWindow.playAudio(messageBody);
		}catch(e){
		}
		content = (messageBody.plateNumber || messageBody.deviceName) + "录音上传!";
	}

	content = "<span>" + new Date().format("hh:mm:ss") + "</span> 【多媒体上传】" + content;
	addRealTimeMessage(content);
}

function defaultMessageHandler(messageBody) {
	try {
		var frame = $("#main-frame")[0].contentWindow;
		if (typeof (eval(frame.defaultMessageHandler)) == "function") {
			frame.defaultMessageHandler(messageBody);
		}
	} catch (e) {
	}
}

function mediaUploadMessageHandler(messageBody){
	var content="";
	if (messageBody.deviceId) {
		content = (messageBody.plateNumber || messageBody.deviceName);
	}

	content += "上传文件" + (messageBody.result==0?"成功":"失败");
	notifyMessage("【文件上传通知】" + content);

	content = "<span>" + new Date(messageBody.createTime).format("hh:mm:ss") + "</span> 【文件上传通知】" + content;
	addRealTimeMessage(content);
}

function downstreamMessageResultHandler(messageBody){
	var content= (messageBody.plateNumber || messageBody.deviceName);
	var result = messageBody.result;
    var messageId = messageBody.respMessageId;
	content += "指令《"+deviceMessageInfoMap.get(messageId)+"》";
	if(result == 0){
		content +="成功";
	}else if(result == 1){
		content +="失败";
	}else if(result == 2){
		content +="消息有误";
	}else if(result == 3){
		content +="不支持";
	}
	notifyMessage("【指令处理结果】" + content);

	content = "<span>" + new Date(messageBody.createTime).format("hh:mm:ss") + "</span> 【指令处理结果】" + content;
	addRealTimeMessage(content);
}

function downstreamErrorMessageHandler(messageBody){
	var content= (messageBody.plateNumber || messageBody.deviceName);
	var result = messageBody.result;
    var messageId = messageBody.messageId;
	content += deviceMessageInfoMap.get(messageId)+"”指令发送失败：";
	if(result == 1){
		content +="车辆已下线";
	}else {
		content +="消息有误或者不支持";
	}
	notifyMessage("【指令处理结果】" + content);

	content = "<span>" + new Date().format("hh:mm:ss") + "</span> 【指令处理结果】" + content;
	addRealTimeMessage(content);
}

function navigateTo(categoryId, url, nav) {
	$("#right-nav").children().removeClass("nav_active");
	$("#left-nav").children().removeClass("nav_active");
	$("." + nav).parent().addClass("nav_active");
	$("#main-frame").attr("src", url + "?categoryId=" + categoryId);
}

function initNav() {
	var url = management_api_server_servlet_path + "/aas/navigation.json";
	ajaxAsyncGet(
			url,
			{},
			function(result) {
				if (result.code != 0) {
					showErrorMessage(result.message);
					return;
				}
				var data = result.data;
				if (data && data.length > 0) {
					var item = data[0];
					$("#main-frame").attr("src",
							item.url + "?categoryId=" + item.id);
					var html = "<li class='nav_active'><i class='"
							+ item.icon
							+ "'></i><a href='javascript:void(0);' onclick=\"navigateTo('"
							+ item.id + "','" + item.url + "','" + item.icon
							+ "')\">" + item.name + "</a></li>";
					if (data.length > 1) {
						item = data[1];
						html += "<li><i class='"
								+ item.icon
								+ "'></i><a href='javascript:void(0);' onclick=\"navigateTo('"
								+ item.id + "','" + item.url + "','"
								+ item.icon + "')\">" + item.name + "</a></li>";
					}
					if (data.length > 2) {
						item = data[2];
						html += "<li><i class='"
								+ item.icon
								+ "'></i><a href='javascript:void(0);' onclick=\"navigateTo('"
								+ item.id + "','" + item.url + "','"
								+ item.icon + "')\">" + item.name + "</a></li>";
					}
					
					if (data.length > 3) {
						item = data[3];
						html += "<li><i class='"
								+ item.icon
								+ "'></i><a href='javascript:void(0);' onclick=\"navigateTo('"
								+ item.id + "','" + item.url + "','"
								+ item.icon + "')\">" + item.name + "</a></li>";
					}
					$("#left-nav").html(html);
					html = "";
					for (var i = 4; i < data.length; i++) {
						item = data[i];
						html += "<li><i class='"
								+ item.icon
								+ "'></i><a href='javascript:void(0);' onclick=\"navigateTo('"
								+ item.id + "','" + item.url + "','"
								+ item.icon + "')\">" + item.name + "</a></li>";
					}
					$("#right-nav").html(html);
				}
			});
}

function editUserInfo(){
	$("#main-frame")[0].contentWindow.showUserInfo();
}

function checkTodoAlarm(){
	var params = {};
	var conditions = {};
	conditions["k.exists"] = true;
	params["conditions"] = conditions;
	
	var url = "/common/query/alarm.json?countable=false&pageSize=1&pageNo=1";
	ajaxAsyncPost(url, params, function(result) {
		if (result.code != 0) {
			showErrorMessage(result.message);
		}else{
			var list = result.data;
			if (list && list.length > 0) {
				$("#todo_alarm_box").show();
			} else {
				$("#todo_alarm_box").hide();
			}
		}
	});
}

function showTodoAlarm(){
	var url = getContextPath() + "/device/alarmList.do?state=1";
	$("#main-frame")[0].contentWindow.showWindows(url);
}

function alarmNotifyOverview(){
	var url =report_api_server_servlet_path + "/car/alarmNotifyOverview.json";
	ajaxAsyncGet(url, {},function(result){
		if (result.code != 0) {
			showErrorMessage(result.message);
		}else{
			var list = result.data;
			if(list && list.length >0){
				var html = "";
				for (var i = 0; i < list.length; i++) {
					var item = list[i];
					html += "<li role='presentation'><a role='menuitem' tabindex='-1' href='javascript:void(0);' onclick='loadAlarmListPage("+item.type+");'>"+alarmNotifyType.get(item.type)+"("+item.total+")</a></li>"
				}
				$("#notice-menu-list").html(html);
				$(".notice-red-dot").show();
			}else{
				$("#notice-menu-list").html("<li role='presentation'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;无提醒消息</li>");
				$(".notice-red-dot").hide();
			}
		}
	});
}

function loadAlarmListPage(type){
	$(window.frames["main-frame"].document).find("#content-frame").attr('src', "/sys/alarmNotifyList.do?type="+type);
}

var full_screen = false;
function onClickFullscreen(){
	if(full_screen){
		full_screen = false;
		exitFullscreen()
	}else{
		full_screen = true;
		fullscreen();
	}
}

function playAudio() {
	if(audioQueue.isEmpty() || isAudioPlaying){
		return;
	}
	try{
		var item = audioQueue.deQueue();
		var audio = $('audio').get(0);
		audio.pause();
	    audio.src = "/voice/"+item.file;
		audio.load();
		audio.play();
	    var start = 0;
	    isAudioPlaying = true;
		audio.addEventListener("ended",function() {
	        start++;
	        if(start >= item.times){
	        	isAudioPlaying = false;
	        	playAudio();
	        }else{
	        	audio.play();
	        }
	    },false);
	}catch(e){
	}
}