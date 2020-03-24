var player = null;
$(document).ready(function() {
	initBootstrapTable();
});

function getQueryParams() {
	var params = {};
	var resourceType = $("#search-resourceType").val();
	if (resourceType) {
		params["resourceType.eq"] = resourceType;
	}
	
	params["bizType.eq"] = 1;
	params["resourceType.lt"] = 4;
	var deviceId = $("#search-deviceId").val();
	if (query && deviceId) {
		params["deviceId.eq"] = deviceId;
	}
	return params;
}

function getQueryUrl() {
	return message_api_server_servlet_path + "/common/query/deviceHistoryMedia.json?countable=true";
}

function getColumns() {
	return [
			{
				field : 'check',
				checkbox : true
			},
			{
				field : "deviceName",
				title : "车牌号",
				formatter : function(value, row, index) {
					var deviceId = row.deviceId;
					return getDeviceName(deviceId);
				}
			},
			{
				field : "resourceType",
				title : "多媒体类型",
				formatter : function(value, row, index) {
					// 0:图像；1:音频；2:视频
					var resourceType = row.resourceType;
					if (resourceType == 1) {
						return "音频";
					} else if (resourceType == 2) {
						return "视频";
					}
				}
			},
			{
				field : "eventCode",
				title : "事件项编码",
				formatter : function(value, row, index) {
					// 0：平台下发指令；1：定时动作；2：抢劫报警触发；3：碰撞侧翻报警触发；其他保留
					var eventCode = row.eventCode;
					if (eventCode == 0) {
						return "平台下发指令";
					} else if (eventCode == 1) {
						return "定时动作";
					} else if (eventCode == 2) {
						return "抢劫报警触发";
					} else if (eventCode == 3) {
						return "碰撞侧翻报警触发";
					} else {
						return "其他";
					}
				}
			},
			{
				field : "channelId",
				title : "拍摄通道id"
			},
			{
				field : "createTime",
				title : "上传时间",
				formatter : function(value, row, index) {
					var createTime = row.createTime;
					if (createTime == null) {
						return "";
					}
					return createTime.split(" ")[0];
				}
			},
			{
				title : '操作',
				field : 'opear',
				formatter : function(value, row, index) {
					var html = '<a   href="javascript:void(0)" class="table_view" title="查看" onclick="play(\''+ row.id + '\')">查看</a>';
					html += '<a   href="javascript:void(0)" class="table_edit" title="下载" onclick="downloadMedia(\''+ row.id + '\')">下载</a>';
					return html;
				}
			} ];
}

function downloadMedia(id) {
	var row = getBootstrapTableRowById(id);
	//var mediaUrl = null;
	//if(getFileServer()){//如果配置了文件服务器，则直接从文件服务器下载
	//	mediaUrl = getFileServer()+(row.filePath).replace(/\\/g,"\/");
	//}else{
		//从ftp服务器下载
		mediaUrl = getContextPath() +"/attachment/download.do?url=/media/ftp/download/"+row.id+".json";
	//}
	window.open(mediaUrl);
}

function play(id) {
	var row = getBootstrapTableRowById(id);
	var mediaUrl = null;
	if(getFileServer()){//如果配置了文件服务器，则直接从文件服务器下载
		mediaUrl = getFileServer()+(row.filePath).replace(/\\/g,"\/");
	}else{
		//从ftp服务器下载
		mediaUrl = getContextPath() +"/attachment/download.do?url=/media/ftp/download/"+id+".json?fileName="+fileName;
	}
	
	if (row.resourceType == 1) {
		playAudio(mediaUrl);
	} else {
		playVideo(mediaUrl);
	}
}

function playAudio(mediaUrl) {
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

function playVideo(mediaUrl) {
	var sources = [ {
		type : "video/mp4",
		src : mediaUrl
	} ];
	if(player==null){
		player = videojs('play-video');
	}
	
	player.src(sources);
	player.load(mediaUrl);
	player.play();

	var dialogId = "play-video-dlg";
	var title = $("#"+dialogId).attr("title");
	showDialog(title, dialogId,function() {
		try {
			player.reset();
		} catch (e) {
		}
	});
}