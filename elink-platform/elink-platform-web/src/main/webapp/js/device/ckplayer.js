var clinetId,heartbeatInterval,current_video_info = null;
var rtmp_server_url = "";

$(document).ready(function() {
	clinetId = getClinetId();
	loadRtmpServer();
});

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

function showVideoDialog() {
	if (current_selected_device) {
		var dialogId = "send-live-video-cmd-dlg";
		if ((current_selected_device.protocolVersion).indexOf("2016")!=-1 || (current_selected_device.protocolVersion).indexOf("tjsatl")!=-1) {
			dialogId += "-2016";
		}
		showCmdDialog(dialogId);
	} else {
		showMessage("请选择要操作的车辆！");
	}
}

function startHeartbeat(messageId) {
	clearInterval(heartbeatInterval);
	heartbeatInterval = setInterval(function() {
		if (current_video_info== null) {
			clearInterval(heartbeatInterval);
			return;
		}
		var url = message_api_server_servlet_path + "/heartbeat/" + clinetId + "/"+messageId+".json";
		ajaxAsyncGet(url, {}, function(result) {
	});
	}, 5 * 1000);
}

function start808Video() {
	var deviceId = current_selected_device.id;
	if (!isOnline(deviceId)) {
		showErrorMessage("车辆不在线，不能进行视频监控！");
		return;
	}

	var data = $("#" + current_dialog_id + "-frm").serializeObject();
	closeDialog();
	var url = message_api_server_servlet_path + "/video/808/" + deviceId + ".json?clinetId=" + clinetId;
	var desc = $("#" + current_dialog_id).attr("title");
	if (desc) {
		data.desc = desc;
	}
	current_video_info = data;
	ajaxAsyncPost(url, data, function(result) {
		if (result.code != 0) {
			showErrorMessage(result.message);
		} else {
			var result = result.data;
			setCmdInfo(result);
			openH5VideoDialog();
			startHeartbeat(current_video_info.messageId);
		}
	});
}

function start1078Video() {
	var deviceId = current_selected_device.id;
	if (!isOnline(deviceId)) {
		showErrorMessage("车辆不在线，不能进行视频监控！");
		return;
	}
	closeDialog();
	var data = $("#" + current_dialog_id + "-frm").serializeObject();
	var url = message_api_server_servlet_path + "/video/1078/" + deviceId + ".json?clinetId=" + clinetId;
	var desc = $("#" + current_dialog_id).attr("title");
	if (desc) {
		data.desc = desc;
	}
	current_video_info = data;
	ajaxAsyncPost(url, data, function(result) {
		if (result.code != 0) {
			showErrorMessage(result.message);
		} else {
			var result = result.data;
			setCmdInfo(result);
			startHeartbeat(current_video_info.messageId);
			var simCode = current_selected_device.simCode;
			if(simCode.charAt(0)=="0"){
				simCode = simCode.substr(1);
			}
			current_video_info.src = rtmp_server_url.replace("{simCode}",simCode).replace("{channel}",current_video_info.channelId);
			current_video_info.isRtmp = true;
			current_video_info.player = getRtmpPlayer(current_video_info);
			openVideoDialog();
		}
	});
}

function getRtmpPlayer(videoInfo) {
	var videoObject = {
			container: '#ckplayer-container',//“#”代表容器的ID，“.”或“”代表容器的class
			variable: 'player',//该属性必需设置，值等于下面的new chplayer()的对象
			autoplay:true,//自动播放
			live:true,//直播视频形式
			video:videoInfo.src//视频地址
		};
	var player=new ckplayer(videoObject);
	return player;
}

function getH5VideoPlayer(videoInfo) {
	var sources = [ {
		type : "video/mp4",
		src : videoInfo.src
	} ];
	var h5Player = videojs('h5Player-video');
	h5Player.src(sources);
	h5Player.load(videoInfo.src);
	h5Player.play();
	return h5Player;
}

function playVideo(message) {
	if (current_video_info.deviceId) {
		var mediaUrl = null;
		if(getFileServer()){//如果配置了文件服务器，则直接从文件服务器下载
			mediaUrl = getFileServer()+(message.filePath).replace(/\\/g,"\/");
		}else{
			//从ftp服务器下载
			mediaUrl = getContextPath() +"/attachment/download.do?url=/media/ftp/download/"+message.id+".json";
		}
		current_video_info.src =mediaUrl;
		current_video_info.player = getH5VideoPlayer(current_video_info);
	}
}

function playAudio(message){
	if (current_video_info.deviceId) {
		var mediaUrl = null;
		if(getFileServer()){//如果配置了文件服务器，则直接从文件服务器下载
			mediaUrl = getFileServer()+(message.filePath).replace(/\\/g,"\/");
		}else{
			//从ftp服务器下载
			mediaUrl = getContextPath() +"/attachment/download.do?url=/media/ftp/download/"+message.id+".json";
		}

		var audio = $('audio').get(0);
		audio.src = mediaUrl;
		audio.load();
		audio.play();
		var dialogId = "play-audio-dlg";
		var title = $("#"+dialogId).attr("title");
		showDialog(title, dialogId,function() {
			try {
				audio.pause();
			} catch (e) {
			}
		});
	}
}

function openVideoDialog() {
	var dialogId = "ckplayer-video-dlg";
	var title = $("#" + dialogId).attr("title");
	showDialog(title, dialogId, function() {
		stopVideo();
	});
}

function openH5VideoDialog() {
	var dialogId = "h5Player-video-dlg";
	var title = $("#" + dialogId).attr("title");
	showDialog(title, dialogId, function() {
		stopVideo();
	});
}

function offlineVideoClean(deviceId) {
	if (current_video_info && current_video_info.deviceId == deviceId) {
		clearInterval(heartbeatInterval);
		current_video_info = null;
		var dialog = $("#ckplayer-video-dlg").dialog("close");
		showAlertMessage("车辆【"+current_video_info.deviceName + "】已下线，系统自动停止实时视频监控！");
	}
}

function stopVideo(){
	clearInterval(heartbeatInterval);
	if(current_video_info && current_video_info.isRtmp){
		stop1078Video();
	}else{
		stop808Video();
	}
}

function stop808Video() {
	var deviceId = current_video_info.deviceId;
	var channelId = current_video_info.channelId;
	var url = message_api_server_servlet_path + "/video/808/" + deviceId + ".json?clinetId=" + clinetId +"&channelId="+channelId;
	var result = ajaxSyncDel(url, {});
	if (result.code != 0) {
		showErrorMessage(result.message);
	}
	if(current_video_info.player){
	   //current_video_info.player.videoPause();
	   //current_video_info.player.videoClear();
		current_video_info.player.reset();
	}
	current_video_info = null;
}

function stop1078Video() {
	var deviceId = current_video_info.deviceId;
	var url = message_api_server_servlet_path + "/video/1078/" + deviceId + ".json?clinetId=" + clinetId;
	var data = {};
	data.channelId = current_video_info.channelId;
	data.command = 0;
	data.commadType = 0;
	data.streamType = 0;
	data.messageId = "9102";
	var result = ajaxSyncPut(url, data);
	if (result.code != 0) {
		showErrorMessage(result.message);
	}
	if(current_video_info.player){
		current_video_info.player.videoPause();
		current_video_info.player.videoClear();
	}
	current_video_info = null;
}

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