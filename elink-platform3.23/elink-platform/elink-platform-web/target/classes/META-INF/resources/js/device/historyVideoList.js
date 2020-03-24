var clinetId,heartbeatInterval,current_video_info = null;
var rtmp_server_url = "";

$(function() {
	initDatepicker();
	setSearchHourOption();
	initBootstrapTable();
	initChannelOption($("#search-channelId"));
	clinetId = getClinetId();
	loadRtmpServer();
	
	$("#search-locationType").change(function(){
		if($("#search-locationType").val()==2){
			search_car_condition = {"deviceState.eq":3,"vedioProtocol.eq":"1078"}
		}else{
			search_car_condition = {};
		}
		loadMoreCars(search_car_condition);
	 });
});

function setSearchHourOption() {
	var interval = 6;
	$("#search-hour").append("<option value=''>选择时间段</option>");
	for (var i = 0; i < 24;) {
		var start = i;
		i = i + interval;
		var end = (i - 1);
		if (start < 10) {
			start = "0" + start;
		}
		if (end < 10) {
			end = "0" + end;
		}
		var text = start + ":00:00-" + end + ":59:59";
		$("#search-hour").append("<option value='" + text + "'>" + text + "</option>");
	}
	$("#search-hour").get(0).selectedIndex = 0;
}

function getQueryParams() {
	var params = {};

	var deviceId = $("#search-deviceId").val();
	if (query && deviceId) {
		params["deviceId.eq"] = deviceId;
	}
	
	var channelId = $("#search-channelId").val();
	if (query && channelId) {
		params["channelId.eq"] = channelId;
	}
	
	var locationType = $("#search-locationType").val();
	params["locationType.eq"] = locationType;
	
	var streamType = $("#search-streamType").val();
	if (query && streamType) {
		params["streamType.eq"] = streamType;
	}
	
	var resourceType = $("#search-resourceType").val();
	if (query && resourceType) {
		params["resourceType.eq"] = resourceType;
	}
	//1078协议
	params["bizType.eq"] = 2;

	var startTime = $("#search-startTime").val();
	if (query && startTime) {
		var hour = $("#search-hour").val();
		var startHour = "00:00:00";
		var endHour = "23:59:59"
		if(hour){
			var arr = hour.split("-");
			startHour = arr[0];
			endHour = arr[1];
		}
		params["startTime.gte"] = startTime + " " +startHour;
		params["endTime.lte"] = startTime + " "+ endHour;
	}
	return params;
}

function getApiName() {
	return "deviceHistoryMedia";
}

function isDetailView(){
	return true;
}

function onExpandBootstrapTableRow (index,row,$element){
	if(row.state!=3 && row.locationType==2){
		return;
	}

	setFileListTable(index,row,$element);
}

function setFileListTable(index,row,$element){
	var html = [];
	var url = message_api_server_servlet_path + "/media/ftp/fileList/"+row.id+".json";
	var result = ajaxSyncGet(url,{},true);
	if (result.code != 0) {
		showErrorMessage(result.message);
	} else {
		var list = result.data;
		var data = [];
		for(var i=0;i<list.length;i++){
			var item = list[i];
			item.deviceId = row.deviceId;
			item.resourceType = row.resourceType;
			item.filePath = row.filePath;
			item.id = row.id;
			data.push(item);
		}
		var columns = getFileColumns();
		$element.html('<table class="table_style" border="0"></table>').find('table').bootstrapTable({columns:columns,data: data});
	}
}

function getFileColumns(){
	return [{
	        field: 'fileName',
	        title: '文件名'
	      },{
		        field: 'fileSize',
		        title: '文件大小',
		        formatter : function(value, row, index){
		        	var fileSize = row.fileSize;
					fileSize = (fileSize / 1024).toFixed(2);//k
					if(fileSize >=1024){
						fileSize = (fileSize / 1024).toFixed(2);//m
						if(fileSize >=1024){
							fileSize = (fileSize / 1024).toFixed(2);//g
							return fileSize+"G";
						}else{
							return fileSize+"M";
						}
					}else{
						return fileSize+"K";
					}
		        }
		   },{
		        field: 'createTime',
		        title: '上传完成时间',
		        formatter : function(value, row) {
		           return new Date(row.createTime).format(); 
		        }
		    },{
		        field: 'opt',
		        title: '操作',
		        formatter : function(value, row) {
					var html = '<a href="javascript:void(0)" class="table_view" title="播放" onclick="startPlay(\'' + row.filePath + '\',\'' +  row.fileName + '\',\'' +  row.id + '\',\''+ row.resourceType + '\')">播放</a>';
					    html+= '<a href="javascript:void(0)" class="table_edit" title="下载" onclick="downloadMedia(\'' + row.filePath + '\',\'' +  row.fileName + '\',\'' + row.id + '\')">下载</a>';
					return html;
				}
		     }
	];
}

function setResourceListTable(deviceId,list){
    var data = [];
	if(list && list.length > 0){
		for(var i=0;i<list.length;i++){
			var item = list[i];
			if(item.messageBody){
				var messageBody = $.evalJSON(item.messageBody);
				if(messageBody.resourceList){
					var resourceList = messageBody.resourceList;
					for(var i=0;i<resourceList.length;i++){
						var resource = resourceList[i];
						resource.deviceId = deviceId;
						data.push(resource);
					}
				}
			}
		}
	}

	var columns = getResourceColumns();
	$('#boot-strap-table').bootstrapTable("destroy");
	$('#boot-strap-table').bootstrapTable({columns:columns,data:data});
}

function getResourceColumns() {
	return [
		{
			field : "deviceName",
			title : "车牌号",
			formatter : function(value, row, index) {
				return getDeviceName(row.deviceId);
			}
		},
			{
				field : "channelId",
				title : "通道号"
			},
			{
				field : "resourceType",
				title : "多媒体类型",
				formatter : function(value, row, index) {
					var resourceType = row.resourceType;
					if(resourceType==0){
						return "音视频";
					}else if(resourceType==1){
						return "音频";
					}else if(resourceType==2){
						return "视频";
					}else {
						return "其他";
					}
				}
			},
			{
				field : "streamType",
				title : "码流类型",
				formatter : function(value, row, index) {
					var streamType = row.streamType;
					if(streamType==1){
						return "主码流";
					}else if(streamType==2){
						return "子码流";
					}
				}
			},
			{
				field : "storeType",
				title : "存储器类型",
				formatter : function(value, row, index) {
					var storeType = row.storeType;
					if(storeType==1){
						return "主存储器";
					}else if(storeType==2){
						return "子存储器";
					}
				}
			},
			{
				field : "fileSize",
				title : "文件大小",
				formatter : function(value, row, index) {
					var fileSize = row.fileSize;
					fileSize = (fileSize / 1024).toFixed(2);//k
					if(fileSize >=1024){
						fileSize = (fileSize / 1024).toFixed(2);//m
						if(fileSize >=1024){
							fileSize = (fileSize / 1024).toFixed(2);//g
							return fileSize+"G";
						}else{
							return fileSize+"M";
						}
					}else{
						return fileSize+"K";
					}
				}
			},
			{
				field : "startTime",
				title : "开始时间"
			},
			{
				field : "endTime",
				title : "结束时间"
			},
			{
				title : '操作',
				field : 'opear',
				formatter : function(value, row) {
					var html = '<a   href="javascript:void(0)" class="table_view" title="播放" onclick="startPlayBack(\'' + row.deviceId + '\',\'' + row.channelId + '\',\'' + row.resourceType + '\',\'' + row.streamType + '\',\'' + row.storeType + '\',\'' + row.startTime + '\',\'' + row.endTime + '\')">播放</a>';
					html += '<a   href="javascript:void(0)" class="table_edit" title="上传" onclick="onclickUploadFile(\'' + row.deviceId + '\',\'' + row.channelId + '\',\'' + row.resourceType + '\',\'' + row.streamType + '\',\'' + row.storeType + '\',\'' + row.startTime + '\',\'' + row.endTime + '\')">上传</a>';
					return html;
				}
			} ];
}


function uploadFile(){
	$("#edit-frm-commandType").val(1);
	showDialog("文件上传指令", "edit-dlg");
}

function queryResource(){
	$("#edit-frm-commandType").val(2);
	showDialog("查询资源列表指令", "edit-dlg");
}

function loadRtmpServer(){
	var params = {};
	params["type.eq"]= DICTIONARY_PROVIDER_API;
	params["code.eq"]= "1078-playback";
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

function getColumns() {
	return [
			{
				field : "deviceName",
				title : "车牌号",
				formatter : function(value, row, index) {
					return getDeviceName(row.deviceId);
				}
			},
			{
				field : "locationType",
				title : "文件位置",
				formatter : function(value, row, index) {
					//存储位置：1:服务器;2:终端 
					var locationType = row.locationType;
					if(locationType==2){
						return "终端";
					}else{
						return "服务器";
					}
				}
			},
			{
				field : "resourceType",
				title : "多媒体类型",
				formatter : function(value, row, index) {
					var resourceType = row.resourceType;
					if(resourceType==0){
						return "音视频";
					}else if(resourceType==1){
						return "音频";
					}else if(resourceType==2){
						return "视频";
					}else if(resourceType==3){
						return "视/音频";
					}else{
						return "其他";
					}
				}
			},
			{
				field : "startTime",
				title : "开始时间"
			},
			{
				field : "endTime",
				title : "结束时间"
			},
			{
				field : "createTime",
				title : "上传时间"
			},
			{
				field : "state",
				title : "状态",
				formatter : function(value, row, index) {
					var state = row.state;
					if(state==0){
						return "已暂停上传";
					}else if(state==1){
						return "数据上传中";
					}else if(state==2){
						return "已取消上传";
					}else if(state==3){
						return "成功";
					}else {
						return "上传失败";
					}
				}
			},
			{
				title : '操作',
				field : 'opear',
				formatter : function(value, row, index) {
					var html ="";
					if(row.state==0){
						html += '<a   href="javascript:void(0)" class="table_edit" title="重新上传" onclick="uploadControl(\''+row.id+'\',\''+row.deviceId+'\',1,'+row.messageSeq+',\'确定要重新上传文件？\')">继续上传</a>';
					}else if(row.state==1){
						html += '<a   href="javascript:void(0)" class="table_edit" title="暂停上传" onclick="uploadControl(\''+row.id+'\',\''+row.deviceId+'\',0,'+row.messageSeq+',\'确定要暂停上传文件？\')">暂停上传</a>';
						html += '<a   href="javascript:void(0)" class="table_del" title="取消上传" onclick="uploadControl(\''+row.id+'\',\''+row.deviceId+'\',2,'+row.messageSeq+',\'确定要取消上传文件？\')">取消上传</a>';
					}else if(row.state==3){
						html += '<a   href="javascript:void(0)" class="table_view" title="详情" onclick="showDetail('+index+')">详情</a>';
					}
					return html;
				}
			} ];
}

function getQueryUrl() {
	return management_api_server_servlet_path + "/common/query/" + getApiName() + ".json?countable=true";
}

function showDetail(index){
	 $('#boot-strap-table').bootstrapTable('expandRow', index);
}

function onclickUploadFile(deviceId,channelId,resourceType,streamType,storeType,startTime,endTime) {
	if(isFileUploaded(deviceId,channelId,resourceType,startTime,endTime)){
		showMessage("文件已上传，不需要重复上传！");
		return;
	}

	showConfirm("确定要上传该文件？", function() {
		var data = {};
		data.deviceId = deviceId;
		data.desc = "文件上传指令";
		data.channelId = channelId;
		data.resourceType = resourceType;
		data.streamType = streamType;
		data.storeType = storeType;
		data.startTime = startTime;
		data.endTime = endTime;
		doSendUploadFileCmd(data);
	});
}


function isFileUploaded(deviceId,channelId,resourceType,startTime,endTime){
	var url = message_api_server_servlet_path + "/common/count/"+getApiName()+".json?isParent=true";
	var params = {"locationType":1,"state.lt":4,"state.neq":2,"deviceId.eq":deviceId,"channelId.eq":channelId,"resourceType.eq":resourceType,"startTime.eq":startTime,"endTime.eq":endTime};
	var result = ajaxSyncPost(url,params);
	if (result.code!=0) {
		showErrorMessage(result.message);
		return false;
	}
	return result.data > 0;
}

function doQueryVideo() {
	var data = $("#search-frm").serializeObject();
	if(data.locationType == 2){
		if(!data.deviceId){
			showErrorMessage("请选择要查询的车辆！");
			return;
		}
		if(!data.startTime|| !data.hour){
			showErrorMessage("请选择要录制时间段！");
			return;
		}
		if (!isOnline(data.deviceId)) {
			showErrorMessage("车辆不在线，不能发送指令！");
			return;
		}
		var startTime = data.startTime;
		var arr = data.hour.split("-");
		data.startTime = startTime + " " +arr[0];
		data.endTime  = startTime + " "+ arr[1];
		data.channelId = data.channelId ? data.channelId:0;
		data.resourceType = data.resourceType ? data.resourceType:3;
		data.streamType = data.streamType ? data.streamType:0;
		data.storeType = data.storeType ? data.storeType:0;
		doSendQueryResourceCmd(data);
	}else{
		$('#boot-strap-table').bootstrapTable("destroy");
		initBootstrapTable();
		onClickSearch();
	}
}

function doSendQueryResourceCmd(data){
    //告警位默认全部
	data.alarmFlag = "0000000000000000000000000000000000000000000000000000000000000000";
	var url = message_api_server_servlet_path + "/deviceDownMessage/" + data.deviceId + "/9205.json";
	startLoading();
	ajaxAsyncPost(url, data, function(result) {
		if (result.code != 0) {
			showErrorMessage(result.message);
			endLoading();
		} else {
			setResourceListTable([]);
			var data = result.data;
			var count = 0;
			var errorMsg = "向车载设备发送【查询资源列表】指令失败，请稍后重试！";
			var messageStateInterval = setInterval(function() {
				var state = getDownstreamMessageState(data.id);
				if(state == 0 || state == 5){
					clearInterval(messageStateInterval);
					count = 0;
					var messageRespQueryInterval = setInterval(function() {
					var list = getRespResourceList(data.deviceId,data.createTime);
					if(list){
						setResourceListTable(data.deviceId,list);
						clearInterval(messageRespQueryInterval);
						endLoading();
					}
					//30秒内无响应停止
					if( (2 * count++) >=30){
						showErrorMessage(errorMsg);
						clearInterval(messageRespQueryInterval);
						endLoading();
					}
					}, 2 * 1000);
				}else if(state==4){
					
				}else{
					showErrorMessage(errorMsg);
					clearInterval(messageStateInterval);
					endLoading();
				}
				
				//30秒内无响应停止
				if( (2 * count++) >=30){
					showErrorMessage(errorMsg);
					clearInterval(messageStateInterval);
					endLoading();
				}
			}, 2 * 1000);
		}
	},false);
}

function doSendUploadFileCmd(data) {
	//上传条件
	data.condition=7;
	//告警位默认全部
	data.alarmFlag = "0000000000000000000000000000000000000000000000000000000000000000";
	var url = message_api_server_servlet_path + "/message/uploadFile/" + data.deviceId + "/9206.json";
	ajaxAsyncPost(url, data, function(result) {
		if (result.code != 0) {
			showErrorMessage(result.message);
		}else{
			showMessage("上传指令已发送，请稍后查看上传结果！");
		}
	},true);
}

function uploadControl(id,deviceId,state,messageSeq,message){
	if (!isOnline(deviceId)) {
		showErrorMessage("车辆不在线，不能发送指令！");
		return;
	}
	showConfirm(message, function() {
		var data = {"id":id,"deviceId":deviceId,"flag":state,"messageSeq":messageSeq};
		data.desc = "文件上传控制";
		var url = message_api_server_servlet_path + "/message/uploadControl/" + deviceId + "/9207.json";
		ajaxAsyncPost(url, data, function(result) {
			if (result.code != 0) {
				showErrorMessage(result.message);
			} else {
				doQuery();
			}
		},true);
	});
}

function getRespResourceList(deviceId,createTime,messageSeq){
	var url = management_api_server_servlet_path + "/common/query/deviceUpMessage.json?countable=false";
	var con = {};
	con["deviceId.eq"] = deviceId;
	if(messageSeq){
		con["messageSeq.eq"] = messageSeq;
	}
	con["createTime.gte"] = createTime;
	con["messageId.eq"] = "1205";
	var result = ajaxSyncPost(url,con);
	if (result.code != 0) {
		showErrorMessage(result.message);
	} else {
		var list = result.data;
		if(list && list.length > 0){
			return list;
		}
	}
	return null;
}

var playStartTime = 0,playProcessInterval;
function openVideoDialog() {
	playTimeHandler(0);
	playStartTime = new Date().getTime();
	var dialogId = "rtmpPlayer-video-dlg";
	var title = $("#" + dialogId).attr("title");
	showDialog(title, dialogId, function() {
		playStartTime = 0;
		stopPlayBack();
		if(playProcessInterval){
			clearInterval(playProcessInterval);
		}
		$("#video_play_time").html("00:00:00");
		$("#video_end_time").html("00:00:00");
	});
	
	if(playProcessInterval){
		clearInterval(playProcessInterval);
	}
	
	playProcessInterval = setInterval(function() {
		playTimeHandler(new Date().getTime()-playStartTime);
	}, 2 * 1000);
}

function startPlayBack(deviceId,channelId,resourceType,streamType,storeType,startTime,endTime){
	if(rtmp_server_url==""){
		showErrorMessage("无可用流媒体服务器，请联系系统管理员配置！");
		return;
	}
	if (!isOnline(deviceId)) {
		showErrorMessage("车辆已离线，不能进行视频回放！");
		return;
	}
	var data = {};
	data.deviceId = deviceId;
	data.messageId = "9201";
	data.channelId = channelId;
	data.dataType = resourceType;
	data.streamType = streamType;
	data.storeType = storeType;
	data.playbackType = 0;
	data.playTimes = 0;
	data.startTime=startTime;
	data.endTime=endTime;
	current_video_info = data;
	var url = message_api_server_servlet_path + "/video/1078/" + deviceId + ".json?clinetId=" + clinetId;
	data.desc = "开启视频回放";
	ajaxAsyncPost(url, data, function(result) {
		if (result.code != 0) {
			showErrorMessage(result.message);
		} else {
			var result = result.data;
			startHeartbeat(current_video_info.messageId);
			var device = getDeviceCache(current_video_info.deviceId);
			var simCode = device.simCode;
			if(simCode.charAt(0)=="0"){
				simCode = simCode.substr(1);
			}
			

			//进度条
			$(".player_ctl_progress_bar_move").css({width:'0px'});

			current_video_info.src = rtmp_server_url.replace("{simCode}",simCode).replace("{channel}",current_video_info.channelId);
			current_video_info.player = getRtmpPlayer(current_video_info);
			openVideoDialog();
		}
	},true);
}

function stopPlayBack(){
	if(current_video_info){
		var data = {};
		data.deviceId = current_video_info.deviceId;
		data.messageId = "9202";
		data.channelId = current_video_info.channelId;
		data.playbackType = 2;
		data.playTimes = 0;
		data.startTime="00000000000000";
		data.desc = "停止视频回放";
		
		clearInterval(heartbeatInterval);
		var url = message_api_server_servlet_path + "/video/1078/" + current_video_info.deviceId + ".json?clinetId=" + clinetId;
		ajaxAsyncPut(url, data, function(result) {
			if (result.code != 0) {
				showErrorMessage(result.message);
			}else{
			}
		});
		current_video_info.player.videoPause();
		current_video_info.player.videoClear();
		current_video_info = null;
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

function getRtmpPlayer(videoInfo) {
	var videoObject = {
			container: '#rtmpPlayer-container',//“#”代表容器的ID，“.”或“”代表容器的class
			variable: 'player',//该属性必需设置，值等于下面的new chplayer()的对象
			autoplay:true,//自动播放
			live:true,//直播视频形式
			loaded:'loadedckplayerHandler',//监听播放器加载成功
			video:videoInfo.src//视频地址
		};
	var player=new ckplayer(videoObject);
	return player;
}

function loadedckplayerHandler(){
	console.log('***loadedRtmpPlayerHandler***');
	current_video_info.player.changeControlBarShow(false);
	current_video_info.player.addListener('time', playTimeHandler); //监听播放时间,addListener是监听函数，需要传递二个参数，'time'是监听属性，这里是监听时间，timeHandler是监听接受的函数
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

function startPlay(filePath,fileName,id,resourceType){
	var mediaUrl = null;
	if(getFileServer()){//如果配置了文件服务器，则直接从文件服务器下载
		mediaUrl = getFileServer()+filePath.replace(/\\/g,"\/")+fileName;
	}else{
		//从ftp服务器下载
		mediaUrl = getContextPath() +"/attachment/download.do?url=/media/ftp/download/"+id+".json?fileName="+fileName;
	}

	if(resourceType == 1){
		playAudio(mediaUrl);
	}else {
		playVideo(mediaUrl);
	}
}

function playAudio(mediaUrl) {
	var audio = $('audio').get(0);
	audio.src = mediaUrl;
	audio.load();
	audio.play();
	var dialogId = "h5Player-audio-dlg";
	var title = $("#"+dialogId).attr("title");
	showDialog(title, dialogId,function() {
		try {
			audio.pause();
		} catch (e) {
		}
	});
}

var h5Player = null;
function playVideo(mediaUrl) {
	var sources = [ {
		type : "video/mp4",
		src : mediaUrl
	} ];
	if(h5Player==null){
		h5Player = videojs('h5Player-video');
	}
	
	h5Player.src(sources);
	h5Player.load(mediaUrl);
	h5Player.play();

	var dialogId = "h5Player-video-dlg";
	var title = $("#"+dialogId).attr("title");
	showDialog(title, dialogId,function() {
		try {
			h5Player.reset();
		} catch (e) {
		}
	});
}

function downloadMedia(filePath,fileName,id) {
	var mediaUrl = null;
	//if(getFileServer()){//如果配置了文件服务器，则直接从文件服务器下载
	//	mediaUrl = getFileServer()+filePath.replace(/\\/g,"\/")+fileName;
	//}else{
		//从ftp服务器下载
		mediaUrl = getContextPath() +"/attachment/download.do?url=/media/ftp/download/"+id+".json?fileName="+fileName;
	//}
	window.open(mediaUrl);
}

function playTimeHandler(t){
	var startTime = new Date(current_video_info.startTime.replace(/-/g, "/")).getTime();
	var endTime = new Date(current_video_info.endTime.replace(/-/g, "/")).getTime();
	if(startTime<(endTime-t)){
		var sskd=$(".player_ctl_bar").width();
		/*当前的时间比上总的时间乘以总的长度*/
		var length=((endTime-startTime)/t)*(sskd-20);
		
		$(".player_ctl_progress_bar_btn").css({width:length+'px'});
		
		var time = new Date(startTime+t).format("hh:mm:ss");
		$("#video_play_time").html(time);
	}
}

var progress_bar_percent=0;

$("#player_ctl_progress_bar").mousedown(function(e){
	 var sskd=$(".player_ctl_bar").width();
	 var cLk=e.clientX;/*点击距离(点击在进度条区域)*/
	 var pJl=$("#player_ctl_progress_bar").offset().left;/*获取进度条距离左边的距离*/	
	
	 var length=cLk-pJl;/*移动的距离*/
	 if(length>=(sskd-20)){
		 length=(sskd-20);
	 }
	
	 $(".player_ctl_progress_bar_move").css({width:length+'px'});/*改变进度条的距离*/
	
	 progress_bar_percent=length/(sskd-20);//百分比
	
	 $(document).on('mousemove',function(e){
		 var newLeft=e.clientX-pJl;/*拖拽的距离*/
		 if(newLeft<=0){
            newLeft=0;
         }
		 if(newLeft>=(sskd-20)){
			newLeft=(sskd-20)
		 }
		 $(".player_ctl_progress_bar_move").css({width:newLeft+'px'});
		 progress_bar_percent=newLeft/(sskd-20);//百分比
	    playbackType = 5;
	 })/*拖拽结束*/

	 $("body").on('mouseup',function(){
		 if(playbackType == 5){
			 var startTime = new Date(current_video_info.startTime.replace(/-/g, "/")).getTime();
			 var endTime = new Date(current_video_info.endTime.replace(/-/g, "/")).getTime();
			 var startTime = new Date(startTime + (endTime-startTime)*progress_bar_percent).format();
			 doSendplaybackCtlCmd({startTime:startTime});
			 playbackType = -1;
		 }
		$(document).off('mousemove');
	 })/*松开结束*/
})/*mousedown方法结束*/


$(".player_ctl_bar_voice_bar_btn").mousedown(function(ev){
	    var ev=ev||window.event;
	    var xs=ev.clientX - this.offsetLeft;
	    var vBar = document.getElementById("player_ctl_bar_voice_bar");
	    var vBtn = document.getElementById("player_ctl_bar_voice_bar_btn");
	    var vBar_in = document.getElementById("player_ctl_bar_voice_bar_in")
	    document.onmousemove = function(ev){
	        var newLefts=ev.clientX-xs;
	        if(newLefts<=0){
	            newLefts=0;
	        }else if(newLefts>=vBar.offsetWidth-vBtn.offsetWidth){
	            newLefts=vBar.offsetWidth-vBtn.offsetWidth;
	        }
	        vBtn.style.left=newLefts+"px";
	        vBar_in.style.width =(newLefts+8)+"px";
	        var prop=newLefts/(vBar.offsetWidth-vBtn.offsetWidth);
	        current_video_info.player.changeVolume(prop);
	    }
		document.onmouseup = function(){
			document.onmousemove = null;
			document.onmouseup = null;
	    }
});

var playbackType = 0
function fastBackward(event){
	playbackType = 4;
	showMenu(event);
}

function fastForward(){
	playbackType = 6;
	doSendplaybackCtlCmd({});
}

function forward(event){
	playbackType = 3;
	showMenu(event);
}

function pause(){
	if(playbackType!=1){
		$("#play_btn").attr("class","glyphicon glyphicon-play");
		playbackType = 1;
	}else{
		$("#play_btn").attr("class","glyphicon glyphicon-pause");
		playbackType = 0;
	}
	doSendplaybackCtlCmd({});
}

function setPlayTimes(times){
	var data = {};
	data.playTimes = times;
	doSendplaybackCtlCmd(data);
	hideMenu();
}

function doSendplaybackCtlCmd(data) {
	data.messageId = "9202";
	data.channelId = current_video_info.channelId;
	data.playbackType = playbackType;
	if(!data.playTimes){
		data.playTimes = 0;
	}
	if(!data.startTime){
		data.startTime="00000000000000";
	}
	data.desc = "视频回放控制";
	var url = message_api_server_servlet_path + "/deviceDownMessage/" +  current_video_info.deviceId + "/9202.json";
	ajaxAsyncPost(url, data, function(result) {
		if (result.code != 0) {
			showErrorMessage(result.message);
		}else{
			showMessage("控制指令已下发！");
		}
	});
}

function showMenu(event) {
	var event = event || window.event;
	var x = event.clientX;
	var y = event.clientY;
    y += document.body.scrollTop-145;
    x += document.body.scrollLeft-10;
    $("#video-conctrol-menu").show();
    $("#video-conctrol-menu").css({"top":y+"px", "left":x+"px"});
	$("body").bind("mousedown", onBodyMouseDown);
}

function hideMenu() {
	$("#video-conctrol-menu").hide();
	$("body").unbind("mousedown", onBodyMouseDown);
}

function onBodyMouseDown(event){
	if (!(event.target.id == "video-conctrol-menu" || $(event.target).parents("#video-conctrol-menu").length>0)) {
		$("#video-conctrol-menu").hide();
	}
}