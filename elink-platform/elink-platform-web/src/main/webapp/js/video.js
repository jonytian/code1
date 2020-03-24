var MAP_TYPE = 1, MAP_DIV = "map_box";

var TREE_MAX_CHANNEL = 4,MAX_VEDEO = 16,heartbeatInterval,clinetId;
var show_device_infowindow;

var videoList = [],playerFlagList=[];
var curent_video_index = 0,curent_car_node;
var rtmp_server_url = "",flv_server_url="";

var selectedDeviceMarkerMap = new JsMap();
var selectedMarkerTextMap = new JsMap();
var deviceListMap = new JsMap();

var max_video_view = 4, video_box_width, video_box_height;

var videoViewList=[1,4,6,8,9,10,16];

var videoClicktimes,cyberplayerClicktimes;

function init(){
	initBootstrapTable();
	initTree("car-tree");
	rightChange();
	setHeight();
	clinetId = getClinetId();

	var type = ($("#mediaType").val() ? $("#mediaType").val() : "flv");
	if(type=="flv"){
		loadFlvServer();
	}else{
		loadRtmpServer();
		
		if(!hasUsableFlash()){
			showMessage("您的浏览器不支持FlashPlayer插件或没有启用该插件");
			setTimeout(function(){ 
				$("#player-container0").html("<div style='width: 100%; height: 100%; color:#FFFFFF;text-align: center; padding-top: 40px;'><p>您的浏览器不支持FlashPlayer插件或没有启用该插件</p><p><a href='https://www.flash.cn/' target='_blank' style='color:#FFFFFF'>点击下载FlashPlayer插件</a></p></div>");
			}, 2000);
		}
	}
	
	for(var i=0;i< MAX_VEDEO ;i++){
		playerFlagList[i]=0;
	}
	
	initVideoBox();
	showVideoView(max_video_view);

	$(".video-view").click(function() {
		max_video_view = 1 * $(this).attr("view");
		showVideoView(max_video_view);
	});
	

	$("video").click(function() {
		if(videoClicktimes){
			clearTimeout(videoClicktimes);
		}
		
		var id = $(this).attr("id");
		videoClicktimes = setTimeout(function(){
			var index = parseInt(id.replace("video",""));
			var videoInfo;
			for (var i = 0, l = videoList.length; i < l; i++) {
				videoInfo = videoList[i];
				if (videoInfo.index == index) {
					break;
				}
				videoInfo = null;
			}
			swapPlayer(videoInfo);
		},300);
	});
	
	$("video").dblclick(function() {
		clearTimeout(videoClicktimes);
		//放大，设置控制条
		requestFullscreen(document.getElementById($(this).attr("id")));
	});
}

function requestFullscreen(ele) {
    // 全屏兼容代码
    if (ele.requestFullscreen) {
        ele.requestFullscreen();
    } else if (ele.webkitRequestFullscreen) {
        ele.webkitRequestFullscreen();
    } else if (ele.mozRequestFullScreen) {
        ele.mozRequestFullScreen();
    } else if (ele.msRequestFullscreen) {
        ele.msRequestFullscreen();
    }
}

function setHeight(){
	var height = $("#content-div").height()-$("#map_right_top").outerHeight(true);
	$("#real-time-message-div").height(height-30);
	$("#car-tree-div").height(height);
}

function initVideoBox() {
	var videdBox = $(".video_content_box");
	video_box_width = videdBox.width();
	video_box_height = videdBox.height();
}

var vInitTimeout = {};
$(window).resize(function() {
	if (vInitTimeout) {
		clearTimeout(vInitTimeout);
	}
	vInitTimeout = setTimeout(function() {
		setHeight();
		initVideoBox();
		showVideoView(max_video_view);
	}, 200);
});

function swapPlayer(videoInfo){
	if(videoInfo.index==0){
		return;
	}
	
	var firstVideo,firstVideoIndex;
	if(playerFlagList[0]==1){
		var firstVideoIndex = getVideoListIndex(0);
		if(firstVideoIndex>=0){
			firstVideo = videoList[firstVideoIndex];
		}
	}
	
	var videoIndex = getVideoListIndex(videoInfo.index);
	
	if(max_video_view==6||max_video_view==8||max_video_view==10){
		var type = ($("#mediaType").val() ? $("#mediaType").val() : "flv");
		var url = "";
		if(firstVideo){
			var temp = firstVideo;
			var videoInfoIndex = videoInfo.index;
			
			firstVideo = $.evalJSON($.toJSON(videoInfo));
			firstVideo.player = temp.player;
			firstVideo.index = temp.index;
			
			var player = videoInfo.player;
			videoInfo = $.evalJSON($.toJSON(temp));
			videoInfo.player = player;
			videoInfo.index = videoInfoIndex;
			
			if(type=="flv"){
				firstVideo.player.unload();
				
				$("#video"+firstVideo.index).src=firstVideo.flv;
				firstVideo.player.load();
				//firstVideo.player.play();
				
				videoInfo.player.unload();
				$("#video"+videoInfo.index).src=videoInfo.flv;
				videoInfo.player.load();
				//videoInfo.player.play();
			}else{
				firstVideo.player.load({file:firstVideo.rtmp});
				videoInfo.player.load({file:videoInfo.rtmp});
			}

			videoList[videoIndex]=videoInfo;
			videoList[firstVideoIndex]=firstVideo;
		}else{
			if(type=="flv"){
				try{
					videoInfo.player.unload();
					//videoInfo.player.pause();
					var video = document.getElementById("video"+videoInfo.index);
					video.poster="";
				}catch(e){}
				
				var index=0;
				videoInfo.player = initFlvPlayer(index,videoInfo);
				videoInfo.index = index;
				playerFlagList[index]=1;
				videoList[videoIndex]=videoInfo;
			}else{
				videoInfo.player.remove();
				playerFlagList[videoInfo.index]=0;

				var index=0;
				videoInfo.player = initCyberplayer(index,videoInfo);
				videoInfo.index = index;
				playerFlagList[index]=1;
				videoList[videoIndex]=videoInfo;
			}
		}
	}
}

function getVideoListIndex(vedioIndex){
	for (var i = 0, l = videoList.length; i < l; i++) {
		var firstVideo = videoList[i];
		if (firstVideo.index == vedioIndex) {
			return i;
		}
	}
	return -1;
}

function showVideoView(n) {
	var videdBox = $(".video_content_box");
	var width = 0, height = 0;

	var count = videdBox.children().length;
	for (var i = 0; i < count; i++) {
		var player = $("#player-container" + i).parent();
		if (i < n) {
			player.removeClass("outside-window");
		} else {
			player.addClass("outside-window");
		}
	}

	var type = ($("#mediaType").val() ? $("#mediaType").val() : "flv");
	
	for (var i = 0; i < n; i++) {
		var player = $("#player-container" + i);
		var widthHeight = fGetWidthHeight(n, (i+1));
		if (player.length > 0) {
			player.parent().height(widthHeight.height).width(widthHeight.width);
		} else {
			var video= "";
			if(type=="flv"){
			   video='<video name="video" autoplay="autoplay"  loop="false" class="videoObj cover video-js" id="video' + i + '" style="width:' + widthHeight.width + 'px;height:' + widthHeight.height + 'px;">您的浏览器不支持 video标签。</video>';
			}

			var html = '<div class="video_box" style="width:'
					+ widthHeight.width
					+ 'px;height:'
					+ widthHeight.height
					+ 'px;"><div id="player-container'+ i +'">'+video+'</div></div>';
			videdBox.append(html);
		}
	}
	
	for(var i=0;i<videoList.length;i++){
		var videoInfo = videoList[i];
		var container = "player-container"+videoInfo.index;
		if(type=="flv"){
			$("#video"+i).height($("#"+container).parent().height()).width($("#"+container).parent().width());
		}else{
			videoInfo.player.resize($("#"+container).parent().width(), $("#"+container).parent().height());
		}
	}
}

// 获取视频宽高
function fGetWidthHeight(count, n) {
	var w, h;
	if (count == 1 || count == 4 || count == 9 || count == 16) {
		var n = Math.sqrt(count);
		var percent = (parseFloat(100) / parseFloat(n)).toFixed(2);
		w = Math.floor(video_box_width * percent / 100);
		h = Math.floor(video_box_height * percent / 100);
	} else if (count == 6) {
		if (n == 1) {
			w = parseInt(video_box_width * 0.66);
			h = parseInt(video_box_height * 0.66);
		} else {
			w = parseInt(video_box_width * 0.33);
			h = parseInt(video_box_height * 0.33);
		}
	} else if (count == 8) {
		if (n == 1) {
			w = parseInt(video_box_width * 0.748);
			h = parseInt(video_box_height * 0.748);
		} else {
			w = parseInt(video_box_width * 0.25);
			h = parseInt(video_box_height * 0.25);
		}
	} else if (count == 10) {
		if (n == 1) {
			w = parseInt(video_box_width * 0.795);
			h = parseInt(video_box_height * 0.798);
		} else {
			w = parseInt(video_box_width * 0.20);
			h = parseInt(video_box_height * 0.20);
		}
	}
	return {
		width : w - 2,
		height : h - 1
	};
}

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
			showMessage("无可用RTMP流媒体服务器，请联系系统管理员配置！");
		}
	}
}

function loadFlvServer(){
	var params = {};
	params["type.eq"]= DICTIONARY_PROVIDER_API;
	params["code.eq"]= "flv-video";
	var url = management_api_server_servlet_path + "/system/dictionary/dictionary.json";
	var result = ajaxSyncPost(url, params);
	if (result.code != 0) {
		showErrorMessage(result.message);
	} else {
		var list = result.data;
		if(list && list.length>0){
			var item = list[0];
			var content = $.evalJSON(item.content);
			flv_server_url = content.url;
		}else{
			showMessage("无可用FLV流媒体服务器，请联系系统管理员配置！");
		}
	}
}


function onLoadMapSuccess() {

}

function showPaginationDetail() {
	return false;
}

function showPaginationSwitch() {
	return false;
}

function showToolbar() {
	return false;
}

function getQueryParams() {
	var params = {};
	var name = $("#search-plateNumber").val();
	if (name) {
		params["plateNumber.like"] = name;
	}
	params["state.gt"] = 0;
	if ($("#search-state").is(":checked")) {
		params["deviceState.eq"] = 3;
	} else {
		params["deviceState.gte"] = 2;
		params["deviceState.lte"] = 3;
	}
	params["vedioProtocol.eq"] = "1078";//只查询1078协议的车辆
	deviceListMap = new JsMap();
	return params;
}

function getQueryUrl() {
	return "/common/query/carDevice.json?select=carId,deviceId,plateNumber,deviceState,bizState,vedioChannel&countable=true&isParent=true";
}

function onLoadBootstrapTableDataSuccess(){
	var rows = $('#boot-strap-table').bootstrapTable('getData');
	for(var i=0,l=rows.length;i<l;i++){
		var row = rows[i];
		if(selectedDeviceMarkerMap.get(row[1])){
			$('#boot-strap-table').bootstrapTable('check', i);
		}
	}
}

function getColumns() {
	return [ {
		field : 'check',
		checkbox : true
	}, {
		title : "车牌号",
		field : 'plateNumber',
		align : 'center',
		valign : 'middle',
		formatter : function(value, row, index) {
			deviceListMap.put(row[1],{index:index,row:row});
			return row[2];
		}
	}, {
		title : "状态",
		field : "state",
		align : 'center',
		formatter : function(value, row, index) {
			var bizState = row[4];
			return deviceBizStateMap.get(bizState);
		}
	} ];
}

function addRealTimeMessage(content) {
	$("#real-time-message-div").prepend(content);
}

// 右侧功能界面切换
function rightChange() {
	$(".map_right_top>ul>li").click(function() {
		var ins = $(this).index();
		$(this).addClass("li_active").siblings().removeClass("li_active");
		$(".map_con .map_con_div").eq(ins).show().siblings().hide();
	})
}

function getCars(enterpriseId,groupId,pageSize,pageNo){
	var url = management_api_server_servlet_path + "/common/query/carDevice.json?select=carId,deviceId,plateNumber,bizState,vedioChannel&countable=false&pageSize="+ pageSize + "&pageNo=" + pageNo + "&orderBy=createTime&desc=true&isParent=true";
	var params = {};
	if(enterpriseId){
		params["enterpriseId.eq"] = enterpriseId;
	}
	if(groupId){
		params["groupId.eq"] = groupId;
	}else{
		params["groupId.null"] = "null";
	}

	params["state.gt"] = 0;
	params["deviceState.gte"] = 2;
	params["deviceState.lte"] = 3;
	params["vedioProtocol.eq"] = "1078";
	var result = ajaxSyncPost(url, params);
	if (result.code != 0) {
		showErrorMessage(result.message);
	} else {
	   return result.data;
	}
}

function addCarTreeNodes(treeId, parent, pageNo, pageSize) {
	var enterpriseId = "",groupId="";
	if(parent.type && parent.type == "ent"){
		enterpriseId = parent.id;
	}else{
		groupId = parent.id;
	}

	var list = getCars(enterpriseId,groupId,pageSize,pageNo);
	if (list && list.length > 0) {
		var treeNodes = [];
		for (var i = 0, l = list.length; i < l; i++) {
			var node = getCarNode(list[i]);
			treeNodes.push(node);
		}
		addTreeNodes(treeId,parent, treeNodes);
		if (list.length == pageSize) {
			pageNo++;
			addCarTreeNodes(treeId, parent, pageNo, pageSize)
		}
	}
}

function getCarNode(item){
	var node = {};
	node.id = item[1];
	node.name = item[2];
	node.icon = getCarTreeStateIcon(item[3]);
	node.bizState = item[3];
	node.type = "car";
	node.vedioChannel=item[4];

	var vedioChannel = item[4];
	var children = [];
	for(var j=0;j<vedioChannel;j++){
		var child = {};
		var channelId = (j+1);
		child.id = item[1]+"_"+channelId;
		child.name = "CH"+channelId;
		child.icon = "/img/camera.png";
		child.type = "ch";
		child.nocheck = true;
		children.push(child);
	}
	node.children=children;
	return node;
}

function getTreeSetting(){
	return {
		check : {
			enable : true,
			chkStyle: "checkbox",
			chkboxType: {"Y" : "ps", "N" : "ps"}
		},
		callback : {
			onClick : zTreeOnClick,
			onRightClick : zTreeOnRightClick,
			beforeExpand : zTreeBeforeExpand,
			onExpand : zTreeOnExpand,
			onCheck: zTreeOnCheck
		}
	};
}

function getInitTreeNodes() {
	var root =  getDefaultGroupTree(group_type_car);
	var children = root.children;
	var list = getCars(getUserEnterpriseId(),null,10000,1);
	if (list && list.length > 0) {
		for (var i = 0, l = list.length; i < l; i++) {
			var node = getCarNode(list[i]);
			children.push(node);
		}
	}
	root.children = children;
	return root;
}

function onExpandTreeNode(treeId, node) {
	onExpandGroupTreeNode(group_type_car,treeId, node);
	var children = node.children;
	if (children && children.length > 0) {
		// 车辆分组是否已经加载车辆
		for (var i = 0, l = children.length; i < l; i++) {
			var item = children[i];
			if (typeof(item.bizState) != 'undefined') {
				return;
			}
		}
	}
	
	addCarTreeNodes(treeId, node, 1, 100);
	
	for(var i=0;i<videoList.length;i++){
		var videoInfo = videoList[i];
		var node = getTreeNodeById(treeId,videoInfo.deviceId);
		if(node){
			getTree(treeId).checkNode(node,true);
		}
	}
}

function zTreeOnCheck(event, treeId, node){
	if(node.checked){
		//加入直播队列
		if(node.type=="car"){
			//加入直播队列
			var vedioChannel = node.vedioChannel;
			for(var i=1;i<=vedioChannel;i++){
				startVideo(node.id,i);
			}
			onSelectDevice(node.id);
		}else if(node.type=="ch"){
			var arr = (node.id).split("_");
			startVideo(arr[0],arr[1]);
			
			onSelectDevice(arr[0]);
		}else {
			var children = node.children;
			for(var j=0;j<children.length;j++){
				var node = children[j];
				if(node.type=="ch"){
					var arr = (node.id).split("_");
					startVideo(arr[0],arr[1]);
					onSelectDevice(arr[0]);
				}else if(node.type=="car"){
					//加入直播队列
					var vedioChannel = node.vedioChannel;
					for(var i=1;i<=vedioChannel;i++){
						startVideo(node.id,i);
					}
					onSelectDevice(node.id);
				}
			}
		}
	}else{
		//从直播队列中除去
		if(node.type=="car"){
			//加入直播队列
			var vedioChannel = node.vedioChannel;
			for(var i=1;i<=vedioChannel;i++){
				stopVideo(node.id,i);
			}
			unselectDevice(node.id,2);
		}else if(node.type=="ch"){
			var arr = (node.id).split("_");
			stopVideo(arr[0],arr[1]);
		}else {
			var children = node.children;
			for(var j=0;j<children.length;j++){
				var node = children[j];
				if(node.type=="ch"){
					var arr = (node.id).split("_");
					stopVideo(arr[0],arr[1]);
				}else if(node.type=="car"){
					var vedioChannel = node.vedioChannel;
					for(var i=1;i<=vedioChannel;i++){
						stopVideo(node.id,i);
					}
					unselectDevice(node.id,2);
				}
			}
		}
	}
}

function onClickTreeNode(treeId, node) {
	if (node.type == "ch") {
		//加入直播队列
		var arr = (node.id).split("_");
		startVideo(arr[0],arr[1]);
		
		var node = node.getParentNode();
		onSelectDevice(node.id);
	}else if (node.type == "car"){
		onSelectDevice(node.id);
	}
}

function onCheckBootstrapTableRow(row) {
	var deviceId = row[1];
	onSelectDevice(deviceId);

	var vedioChannel = row[5];
	for(var i=1;i<=vedioChannel;i++){
	    //加入直播队列
		startVideo(deviceId,i);
	}
}


function onUncheckBootstrapTableRow(row) {
	var deviceId = row[1];
	//删除marker
	var vedioChannel = row[5];
	for(var i=1;i<=vedioChannel;i++){
		stopVideo(deviceId,i);
	}
	unselectDevice(deviceId,1);
}

function startVideo(deviceId,channelId,dataType){
	if(flv_server_url=="" && rtmp_server_url==""){
		showErrorMessage("无可用流媒体服务器，请联系系统管理员配置！");
		return false;
	}
	if(isOnline(deviceId)){
		if(checkVideo(deviceId,channelId)){
			//启动视频
			if (videoList.length >= MAX_VEDEO) {
				showErrorMessage("最大支持" + MAX_VEDEO + "音视频频直播!");
				return false;
			}

			var url = message_api_server_servlet_path + "/video/1078/" + deviceId + ".json?clinetId=" + clinetId;
			var data = {};
      // modify by liliang 20200319
			data.desc = " 实时音视频传输请求";
			data.deviceId = deviceId;
			data.messageId = "9101";
			data.channelId = channelId;
			if(typeof(dataType) == "undefined"){
				data.dataType = 0;
			}else{
				data.dataType = dataType;
			}
			data.streamType = 1;

			var videoInfo = {};
			var device = getDeviceCache(deviceId);
			videoInfo.deviceId = deviceId;
			videoInfo.simCode = device.simCode;
			videoInfo.deviceName = device.name;
			videoInfo.channelId = channelId;
			videoInfo.dataType = data.dataType;
			videoInfo.streamType = data.streamType;
			
			data.name = device.name;
			var result = ajaxSyncPost(url, data);
			if (result.code != 0) {
				showErrorMessage(result.message);
				return false;
			} else {
				var result = result.data;
				setCmdInfo(result);
				startHeartbeat();
				var simCode = videoInfo.simCode;
				if(simCode.charAt(0)=="0"){
					simCode = simCode.substr(1);
				}

				//自动切换更大模式
				if (videoList.length >= max_video_view) {
					for(var i=0;i<videoViewList.length;i++){
						if(videoViewList[i]==max_video_view && (i+1) < videoViewList.length){
							max_video_view = videoViewList[i+1];
							break;
						}
					}
					showVideoView(max_video_view);
				}

				videoInfo.rtmp = rtmp_server_url.replace("{simCode}",simCode).replace("{channel}",videoInfo.channelId);
				videoInfo.flv = flv_server_url.replace("{simCode}",simCode).replace("{channel}",videoInfo.channelId);
				startFlvPlayer(videoInfo);
				videoList.push(videoInfo);
			}
		}
		removeInfowindow();
	}else{
		showErrorMessage("设备不在线，不能启动实时音视频！");
		return false;
	}
	return true;
}

function checkVideo(deviceId,channelId){
	for (var i = 0, l = videoList.length; i < l; i++) {
		var item = videoList[i];
		if (item.deviceId == deviceId && item.channelId == channelId) {
			return false;
		}
	}
	return true;
}

function getDataType(deviceId,channelId){
	for (var i = 0, l = videoList.length; i < l; i++) {
		var item = videoList[i];
		if(channelId){
			if (item.deviceId == deviceId  && item.channelId == channelId) {
				 return item.dataType;
			}
		}else{
			if (item.deviceId == deviceId) {
				 return item.dataType;
			}
		}
	}
	return -1;
}

function stopAllVideo(){
	while (videoList.length>0) {
		var videoInfo = videoList[0];
		stopVideo(videoInfo.deviceId,videoInfo.channelId);
		unselectDevice(videoInfo.deviceId,3);
	}
}

function volumeOn(){
	if(videoList.length == 0){
		return;
	}
	$(".glyphicon-volume-off").show();
	$(".glyphicon-volume-up").hide();
	for (var i = 0, l = videoList.length; i < l; i++) {
		var videoInfo = videoList[i];
		videoInfo.player.videoEscMute();
	}
}

function volumeOff(){
	if(videoList.length == 0){
		return;
	}
	$(".glyphicon-volume-off").hide();
	$(".glyphicon-volume-up").show();
	for (var i = 0, l = videoList.length; i < l; i++) {
		var videoInfo = videoList[i];
		videoInfo.player.videoMute();
	}
}

function stopVideo(deviceId,channelId) {
	var videoInfo, index;
	for (var i = 0, l = videoList.length; i < l; i++) {
		videoInfo = videoList[i];
		if (videoInfo.deviceId == deviceId && videoInfo.channelId == channelId) {
			index = i;
			break;
		}
		videoInfo = null;
	}
	if (videoInfo) {
		var deviceId = videoInfo.deviceId;
		var channelId = videoInfo.channelId;
		var url = message_api_server_servlet_path + "/video/1078/" + deviceId + ".json?clinetId=" + clinetId;
		var data = {};
		data.channelId = channelId;
		data.command = 0;
		data.commadType = 0;
		data.streamType = videoInfo.streamType;
		data.messageId = "9102";
		ajaxSyncPut(url, data);
		videoList.splice(index, 1);
		playerFlagList[videoInfo.index]=0;
		
		var type = ($("#mediaType").val() ? $("#mediaType").val() : "flv");
		if(type=="flv"){
			try{
				videoInfo.player.unload();
				//videoInfo.player.pause();
				var video = document.getElementById("video"+videoInfo.index);
				video.poster="";
			}catch(e){}
		}else{
			videoInfo.player.remove();
			//$("#player-container"+videoInfo.index).html("");
		}
		
	}

	removeInfowindow();
}

function onSelectDevice(deviceId) {
	var device = getDeviceCache(deviceId);
	var state = getDeviceBizState(deviceId)
	if(device.bizState != state){
		device.bizState = state;
	}
	getLastGps(device);
}


function getLastGps(device) {
	var deviceId = device.id;
	var url = lbs_api_server_servlet_path + "/gps/" + deviceId + "/last.json";
	ajaxAsyncGet(url, {}, function(result) {
		if (result.code != 0) {
			showErrorMessage(result.message);
		} else {
			var gpsInfo = result.data;
			if(gpsInfo){
				var gcj02 = LngLatConverter.wgs84togcj02(gpsInfo.lng, gpsInfo.lat);
				var point = mapUtil.getPoint(gcj02[0], gcj02[1]);
		
				var marker = selectedDeviceMarkerMap.get(deviceId);
				if (marker == null) {
					
					clearDeviceMarkers();
					mapUtil.clearOverlays();
					selectedDeviceMarkerMap = new JsMap();
					selectedMarkerTextMap = new JsMap();
					
					
					marker = getMarker(device,point,gpsInfo.direction);
					addDeviceMarker(marker);
				} else {
					mapUtil.setMarkerPosition(marker, point);
					mapUtil.setMarkerRotation(marker, gpsInfo.direction);
					var iconUrl = getCarStateIcon(device.bizState);
					mapUtil.setMarkerIcon(marker,iconUrl);
					mapUtil.setTextPosition(selectedMarkerTextMap.get(deviceId), point);
				}
				var map = mapUtil.getMap();
				map.panTo(point);
				
				showDeviceInfoWindow(deviceId, point,gpsInfo);
			}else{
				showMessage("该车辆无位置信息，无法在地图上显示！");
			}
	   }
	});
}

function syncGetLastGps(deviceId) {
	var url = lbs_api_server_servlet_path + "/gps/" + deviceId + "/last.json";
	var result = ajaxSyncGet(url, {});
	if (result.code == 0) {
		return result.data;
	}
	return null;
}

function getMarker(device,point,direction){
	var deviceId = device.id || device.deviceId;
	var iconUrl = getCarStateIcon(device.bizState);
	var name = getDeviceName(deviceId);
	var marker = mapUtil.getMarker(deviceId, point, name, iconUrl, direction);
	marker.on('click', function(e) {
		var deviceId = e.target.getExtData();
		var lacation = e.target.getPosition();
		showDeviceInfoWindow(deviceId, lacation,null);
	});
	selectedDeviceMarkerMap.put(deviceId,marker);
	var text = mapUtil.addText(point, name);
	selectedMarkerTextMap.put(deviceId,text);
	return marker;
}

function showDeviceInfoWindow(deviceId, lacation, gpsInfo) {
	if(gpsInfo == null){
		gpsInfo = syncGetLastGps(deviceId);
	}
	if(!lacation && gpsInfo){
		var gcj02 = LngLatConverter.wgs84togcj02(gpsInfo.lng, gpsInfo.lat);
		lacation = mapUtil.getPoint(gcj02[0], gcj02[1]);
	}
	
	if(lacation){
		mapUtil.regeocoder(lacation, function(address) {
			//midified by liliang 202003014 修改显示排版
			var device = getDeviceCache(deviceId);
			var text = "车&nbsp;&nbsp;牌&nbsp;&nbsp;号：" + getDeviceName(deviceId)+"";
			text += "<br/>设nbsp;&nbsp;备nbsp;&nbsp;ID：" + device.simCode;
			text += "<br/>数据时间：" + new Date(gpsInfo.gpsTime).format();
			text += "<br/>状&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;态：" + deviceBizStateMap.get(device.bizState);
			if (address) {
				text += "<br/>地&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;址：" + address;
			}
			//检查是否在视频直播中
			var dataType = getDataType(deviceId);
			var opt = "";
			opt +="<a href='javascript:void(0);' onclick=\"showYuntai('"+deviceId+"');\">云台控制</a>&nbsp;&nbsp;";
			if(dataType == 0){//音视频
				opt +="<a href='javascript:void(0);' onclick=\"onclickStopRecordAndVideo('"+deviceId+"');\" id='video_control' >停止音视频</a>";
				opt +="&nbsp;&nbsp;<a href='javascript:void(0);' onclick=\"changeStream('"+deviceId+"');\" id='video_control' >切换码流</a>";
			}else if(dataType == 1){//视频
				opt +="<a href='javascript:void(0);' onclick=\"onclickStopVideo('"+deviceId+"');\" id='video_control' >停止视频</a>";
				opt +="&nbsp;&nbsp;<a href='javascript:void(0);' onclick=\"changeStream('"+deviceId+"');\" id='video_control' >切换码流</a>";
			}else if(dataType == 2){//语音对讲
				opt +="<a href='javascript:void(0);' onclick=\"onclickStopTalk('"+deviceId+"');\" id='video_control' >停止语音对讲</a>";
			}else if(dataType == 3){//监听
				opt +="<a href='javascript:void(0);' onclick=\"onclickStopRecord('"+deviceId+"');\" id='video_control' >停止监听</a>";
				opt +="&nbsp;&nbsp;<a href='javascript:void(0);' onclick=\"changeStream('"+deviceId+"');\" id='video_control' >切换码流</a>";
			}else{
				opt +="<a href='javascript:void(0);' onclick=\"onclickStartRecordAndVideo('"+deviceId+"');\" id='video_control' >开启音视频</a>";
				opt +="&nbsp;&nbsp;<a href='javascript:void(0);' onclick=\"onclickStartVideo('"+deviceId+"');\" id='video_control' >开启视频</a>";
				opt +="&nbsp;&nbsp;<a href='javascript:void(0);' onclick=\"onclickStartRecord('"+deviceId+"');\" id='video_control' >开启监听</a>";
				opt +="&nbsp;&nbsp;<a href='javascript:void(0);' onclick=\"onclickStartTalk('"+deviceId+"');\" id='video_control' >开启语音对讲</a>";
			}

			text += "<br/><span class='amap-info-window-menu'>"+opt+"</span>";
			text ="<div class='amap-info-window-content'>"+text+"</div>";
			// 设置label标签
			removeInfowindow();
			show_device_infowindow = mapUtil.addLabel(lacation, text);
			show_device_infowindow.on('close', function(e) {
				//show_device_infowindow = null;
			});
		});
	}
}

function onlineMessageHandler(messageBody){
	var deviceId = messageBody.deviceId;
	var state = messageBody.state;
	var marker = selectedDeviceMarkerMap.get(deviceId);
	if (marker) {
		var iconUrl = getCarStateIcon(state);
		mapUtil.setMarkerIcon(marker,iconUrl);
		removeInfowindow();
	}

	if(state==0){
		var bool = false;
		for (var i = 0, l = videoList.length; i < l; i++) {
			var item = videoList[i];
			if (item.deviceId == deviceId) {
				bool = true;
				break;
			}
		}
		if(bool){
			//停止视频,todo
			var vedioChannel =  getVedeoChannel(deviceId);
			for(var i=1;i<=vedioChannel;i++){
				stopVideo(deviceId,i);
			}
			unselectDevice(deviceId,3);
			
			showMessage(getDeviceName(deviceId)+"车辆下线，视频已停止！");
		}
		
		if(talkDeviceId && talkDeviceId==deviceId){
			showMessage(getDeviceName(deviceId)+"车辆下线，语音对讲已停止！");
			onclickStopTalk();
		}
	}

	//更新树节点
	var tree = getTree("car-tree");
	var node = tree.getNodeByParam("id",deviceId);
	if(node){
		node.icon = getCarTreeStateIcon(state);
		tree.updateNode(node);
	}
}

function bizStateMessageHandler(message){
	var bizState = message.bizState;
	var deviceId = message.deviceId;
	var marker = selectedDeviceMarkerMap.get(deviceId);
	if (marker) {
		var iconUrl = getCarStateIcon(bizState);
		mapUtil.setMarkerIcon(marker,iconUrl);
	}

	//更新树节点
	var tree = getTree("car-tree");
	var node = tree.getNodeByParam("id",deviceId);
	if(node){
		node.icon = getCarTreeStateIcon(bizState);
		tree.updateNode(node);
	}
	
	var rowItem = deviceListMap.get(deviceId);
	if(rowItem){
		var row = rowItem.row;
		row[4]=state;
		$('#boot-strap-table').bootstrapTable('updateRow', {index: rowItem.index, row: row})
	}
}

function hideCarInfoOverviewDiv(){
	$("#car_info_overview_div").hide();
}

function startHeartbeat() {
	clearInterval(heartbeatInterval);
	heartbeatInterval = setInterval(function() {
		if (videoList.length == 0) {
			clearInterval(heartbeatInterval);
			return;
		}
		var url = message_api_server_servlet_path + "/heartbeat/" + clinetId + "/9101.json";
		ajaxAsyncGet(url, {}, function(result) {});
	}, 5 * 1000);
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

function setCmdInfo(result){
	var messageBody = $.evalJSON(result.messageBody);
	var content = messageBody.name +"下发监控指令："+ messageBody.desc;
	content = "<span>" + new Date().format("hh:mm:ss") + "</span> 【监控指令】" + content + "<hr>";
	addRealTimeMessage(content);
}

function startFlvPlayer(videoInfo) {
	var index = 0;
	for(var i=0;i<playerFlagList.length;i++){
		if(playerFlagList[i] == 0){
			index = i;
			break;
		}
	}
	var player;
	var type = ($("#mediaType").val() ? $("#mediaType").val() : "flv");
	if(type=="flv"){
		player = initFlvPlayer(index,videoInfo);
	}else{
		player = initCyberplayer(index,videoInfo);
	}
	
	videoInfo.player = player;
	videoInfo.index = index;
	playerFlagList[index]=1;
}

function initCyberplayer(index,videoInfo){
	var container = "player-container"+index;
	//$("#"+container).html("<img style='width:100%;' src='/images/loading.gif' border='0'>");
	
	var player = cyberplayer(container).setup({
        width: $("#"+container).parent().width(),
        height: $("#"+container).parent().height(),
        controlbar: {
            barLogo: false
        },
        isLive: true, // 标明是否是直播
        file: videoInfo.rtmp, 
        type:"rtmp",
        autostart: true,
        stretching: "uniform",
        volume: 100,
        reconnecttime:5,
        controls: false,
        ak: "2b0fe9030a5748d780a26cc4bfbdea95" // 公有云平台注册即可获得accessKey
    });
	
	player.onSetupError(function(event){
		showMessage("播放器初始化失败，请确认允许浏览器加载flash插件后重试！");
		setTimeout(function(){
			stopVideo(videoInfo.deviceId,videoInfo.channelId);
		}, 5000);
	});
	
	player.onError(function(event){
		if(videoInfo.cyberplayerErrorTimer){
			return;
		}
		showMessage("疑似直播信号丢失！");
		var playlistItem = player.getPlaylistItem();
		videoInfo.cyberplayerErrorTimer = setTimeout(function(){
			videoInfo.cyberplayerErrorTimer = null;
			player.load({file:playlistItem.file});
		}, 5000);
	});
	
	player.onDisplayClick(function(event){
		if(cyberplayerClicktimes){
			clearTimeout(cyberplayerClicktimes);
			cyberplayerClicktimes = null;
			player.setFullscreen(true);
			player.setControls(true);
		}
		
		cyberplayerClicktimes = setTimeout(function(){
			cyberplayerClicktimes = null;
			swapPlayer(videoInfo);
		},300);
	});

	player.onNoLiveStream(function(){
		 if(videoInfo.noLiveStream){
			 showMessage(videoInfo.deviceName+"疑似直播信号丢失！");
		 }else{
			 videoInfo.noLiveStream = true;
		 }
	});
	
	player.onFullscreen(function(event){ 
	    if(!event.fullscreen){
	    	player.setControls(false);
	    }
	});
	
	return player;
}

function initFlvPlayer(index,videoInfo){
	var player = flvjs.createPlayer( {
		isLive: true,
        hasAudio: true,
        hasVideo: true,
		type : 'flv',
		url : videoInfo.flv
	});
	
	
	var video = document.getElementById("video"+index);
	video.poster="/images/loading.gif";
	player.on(flvjs.Events.ERROR, (errType, errDetail) => { 
		// errType是 NetworkError时，对应errDetail有：Exception、HttpStatusCodeInvalid、ConnectingTimeout、EarlyEof、UnrecoverableEarlyEof
		// errType是 MediaError时，对应errDetail是MediaMSEError
		alert(videoInfo.deviceName+"播放视频出错，请稍后重试！");
		stopVideo(videoInfo.deviceId,videoInfo.channelId);
	});
	
	
	player.attachMediaElement(video);
	player.load();	
	player.play();
	return player;
}


function videoControl(deviceId){
	if($("#video_control").html()=="视频"){
		//加入直播队列
		var vedioChannel =  getVedeoChannel(deviceId);
		for(var i=1;i<=vedioChannel;i++){
			startVideo(deviceId,i);
		}
	}else{
		var vedioChannel =  getVedeoChannel(deviceId);
		for(var i=1;i<=vedioChannel;i++){
			stopVideo(deviceId,i);
		}
		unselectDevice(deviceId,3);
		
	}
}

function zTreeOnRightClick(event, treeId, node){
	if(node.type=="car" || node.type=="ch"){
		showRMenu(node,event.clientX, event.clientY);
		curent_car_node = node;
	}
}

function onclickStartVideo(deviceId){
	hideRMenu();
	if(deviceId){
		//加入直播队列
		var vedioChannel =  getVedeoChannel(deviceId);
		for(var i=1;i<=vedioChannel;i++){
			startVideo(deviceId,i,1);
		}
	}else{
		if(curent_car_node){
			if(curent_car_node.type=="car"){
				//加入直播队列
				var vedioChannel =  curent_car_node.vedioChannel;
				for(var i=1;i<=vedioChannel;i++){
					startVideo(curent_car_node.id,i,1);
				}
			}else if(curent_car_node.type=="ch"){
				var arr = (curent_car_node.id).split("_");
				startVideo(arr[0],arr[1],1);
			}
		}
	}
	removeInfowindow();
}

function onclickStopVideo(deviceId){
	hideRMenu();
	if(deviceId){
		//加入直播队列
		var vedioChannel =  getVedeoChannel(deviceId);
		for(var i=1;i<=vedioChannel;i++){
			stopVideo(deviceId,i);
		}
		unselectDevice(deviceId,3);
	}else{
		if(curent_car_node.type=="car"){
			//加入直播队列
			var vedioChannel =  curent_car_node.vedioChannel;
			for(var i=1;i<=vedioChannel;i++){
				var deviceId = curent_car_node.id;
				stopVideo(deviceId,i);
				unselectDevice(deviceId,3);
			}
		}else if(curent_car_node.type=="ch"){
			var arr = (curent_car_node.id).split("_");
			stopVideo(arr[0],arr[1]);
		}
	}
}

function unselectDevice(deviceId,type){
	removeDeviceMarker(deviceId);
	try{
		if(type==1 || type ==3 ){
			var tree = getTree("car-tree");
			var node = tree.getNodeByParam("id",deviceId);
			if(node){
				tree.checkNode(node, false, false);
			}
		}
		
		if(type==2 || type ==3 ){
			var list = $('#boot-strap-table').bootstrapTable('getData', true);
			if(list.length>0){
				for(var i=0;i<list.length;i++){
					var arr = list[i];
					if(deviceId == arr[1]){
						$('#boot-strap-table').bootstrapTable('uncheck', i);
						break;
					}
				}
			}
		}
	}catch(e){
	}
}

function removeDeviceMarker(deviceId){
	var marker = selectedDeviceMarkerMap.get(deviceId);
	if(marker){
		selectedDeviceMarkerMap.remove(deviceId);
		mapUtil.removeOverlay(marker);
		markerClusterer.removeMarker(marker);
	}

	marker = selectedMarkerTextMap.get(deviceId);
	if(marker){
		selectedMarkerTextMap.remove(deviceId);
		mapUtil.removeOverlay(marker);
	}

	removeInfowindow();
}

function onclickStartRecord(deviceId){
	hideRMenu();
	if(deviceId){
		//加入直播队列
		var vedioChannel =  getVedeoChannel(deviceId);
		for(var i=1;i<=vedioChannel;i++){
			startVideo(deviceId,i,3);
		}
	}else{
		if(curent_car_node){
			if(curent_car_node.type=="car"){
				//加入直播队列
				var vedioChannel =  curent_car_node.vedioChannel;
				for(var i=1;i<=vedioChannel;i++){
					startVideo(curent_car_node.id,i,3);
				}
			}else if(curent_car_node.type=="ch"){
				var arr = (curent_car_node.id).split("_");
				startVideo(arr[0],arr[1],3);
			}
		}
	}
	removeInfowindow();
}

function onclickStopRecord(deviceId){
	hideRMenu();
	onclickStopVideo(deviceId);
}

function onclickStopRecordAndVideo(deviceId){
	hideRMenu();
	onclickStopVideo(deviceId);
}

function onclickStartRecordAndVideo(deviceId){
	hideRMenu();
	if(deviceId){
		//加入直播队列
		var vedioChannel =  getVedeoChannel(deviceId);
		for(var i=1;i<=vedioChannel;i++){
			startVideo(deviceId,i);
		}
	}else{
		if(curent_car_node){
			if(curent_car_node.type=="car"){
				//加入直播队列
				var vedioChannel =  curent_car_node.vedioChannel;
				for(var i=1;i<=vedioChannel;i++){
					startVideo(curent_car_node.id,i);
				}
			}else if(curent_car_node.type=="ch"){
				var arr = (curent_car_node.id).split("_");
				startVideo(arr[0],arr[1]);
			}
		}
	}
	removeInfowindow();
}

function showYuntai(deviceId){
	var vedioChannel = 0;
	if(deviceId){
		vedioChannel =  getVedeoChannel(deviceId);
	}else{
		if(curent_car_node){
			if(curent_car_node.type=="car"){
				deviceId =  curent_car_node.id;
				vedioChannel =  curent_car_node.vedioChannel;
			}else if(curent_car_node.type=="ch"){
				var arr = (curent_car_node.id).split("_");
				deviceId =arr[0];
				vedioChannel =  getVedeoChannel(deviceId);
			}
		}else{
			showMessage("请选择车辆!");
			return;
		}
	}
	$("#channelId").empty();
	for(var i=1;i<=vedioChannel;i++){
		$("#channelId").append("<option value='" + i + "'>CH" + i + "</option>");
	}
	$("#plateNo").html(getDeviceName(deviceId));
	$("#deviceId").val(deviceId);
	$("#yuntai").show();
}

function hideYuntai(){
	$("#yuntai").hide();
}

function send9301(direction){
	var data = {};
	data.messageId = "9301";
	data.direction = direction;
	data.desc="云台旋转控制";
	data.channelId = $("#channelId").val();
	data.speed = 50;
	doSendCmd($("#deviceId").val(), data);
}

function send9302(direction){
	var data = {};
	data.messageId = "9302";
	data.direction = direction;
	data.desc="云台调整焦距控制";
	data.channelId = $("#channelId").val();
	doSendCmd($("#deviceId").val(), data);
}

function send9303(type){
	var data = {};
	data.messageId = "9303";
	data.type = type;
	data.desc="云台调整光圈控制";
	data.channelId = $("#channelId").val();
	doSendCmd($("#deviceId").val(), data);
}

function send9304(flag){
	var data = {};
	data.messageId = "9304";
	data.flag = flag;
	data.desc="云台雨刷控制";
	data.channelId = $("#channelId").val();
	doSendCmd($("#deviceId").val(), data);
}

function send9305(flag){
	var data = {};
	data.messageId = "9305";
	data.flag = flag;
	data.desc="红外补光控制";
	data.channelId = $("#channelId").val();
	doSendCmd($("#deviceId").val(), data);
}

function send9306(type){
	var data = {};
	data.messageId = "9306";
	data.type = type;
	data.desc="云台变倍控制";
	data.channelId = $("#channelId").val();
	doSendCmd($("#deviceId").val(), data);
}

function changeStream(deviceId){
	hideRMenu();
	var data = {};
	data.messageId = "9102";
	data.command = 1;//切换码流
	data.commadType = 0;
	if(deviceId){
		var vedioChannel =  getVedeoChannel(deviceId);
		for(var i=1;i<=vedioChannel;i++){
			var info = getVedeoInfo(deviceId,i);
			if(info){
				data.channelId = info.channelId;
				data.streamType = info.streamType==0?1:0;
				doSendCmd(deviceId,data);
			}
		}
	}else{
		if(curent_car_node){
			if(curent_car_node.type=="car"){
				var deviceId = curent_car_node.id;
				var vedioChannel =  curent_car_node.vedioChannel;
				for(var i=1;i<=vedioChannel;i++){
					var info = getVedeoInfo(deviceId,i);
					if(info){
						data.channelId = info.channelId;
						data.streamType = info.streamType==0?1:0;
						if(doSendCmd(deviceId,data)){
							info.streamType = data.streamType;
						}
					}
				}
			}else if(curent_car_node.type=="ch"){
				var arr = (curent_car_node.id).split("_");
				var deviceId = arr[0];
				var info = getVedeoInfo(deviceId,arr[1]);
				if(info){
					data.channelId = info.channelId;
					data.streamType = info.streamType==0?1:0;
					if(doSendCmd(deviceId,data)){
						info.streamType = data.streamType;
					}
				}
			}
		}
	}
}

function getVedeoInfo(deviceId,channelId){
	for (var i = 0, l = videoList.length; i < l; i++) {
		var item = videoList[i];
		if (item.deviceId == deviceId && item.channelId == channelId) {
			return item;
		}
	}
	return null;
}

function getVedeoChannel(deviceId){
	var url = management_api_server_servlet_path + "/common/device/" + deviceId+ ".json";
	var data = {}
	var result = ajaxSyncGet(url, data);
	if (result.code != 0) {
		return TREE_MAX_CHANNEL;
	} else {
		return result.data.vedioChannel || TREE_MAX_CHANNEL;
	}
	return TREE_MAX_CHANNEL;
}

function doSendCmd(deviceId, data) {
	if (!isOnline(deviceId)) {
		showErrorMessage("车辆不在线，不能发送指令！");
		return false;
	}
	var url = message_api_server_servlet_path + "/deviceDownMessage/" + deviceId + "/" + data.messageId + ".json";
	var result = ajaxSyncPost(url, data,true);
	if (result.code != 0) {
		showErrorMessage(result.message);
		return false;
	} else {
		setCmdInfo(result.data);
		showMessage("控制指令已下发！");
	}
	return true;
}

function showRMenu(node, x, y) {
	var deviceId = node.id;
	var channelId = null;
	if(node.type=="ch"){
		var arr =  deviceId.split("_");
		deviceId = arr[0];
		channelId = arr[1];
	}
	var type = getDataType(deviceId,channelId);
	if(type == 0){//音视频
		$("#stop-record-vedio").show();
		$("#start-record-vedio").hide();

		$("#stream-ctl").show();
		
		$("#stop-vedio").hide();
		$("#start-vedio").hide();
		$("#start-record").hide();
		$("#stop-record").hide();
		$("#stop-talk").hide();
		$("#start-talk").hide();
	}else if(type == 1){//视频
		$("#stop-vedio").show();
		$("#stream-ctl").show();
		$("#start-vedio").hide();
		
		$("#stop-record-vedio").hide();
		$("#start-record-vedio").hide();
		$("#start-record").hide();
		$("#stop-record").hide();
		$("#stop-talk").hide();
		$("#start-talk").hide();
	}else if(type == 2){//语音
		$("#stop-talk").show();
		$("#start-talk").hide();
		
		$("#stream-ctl").show();

		$("#stop-record-vedio").hide();
		$("#start-record-vedio").hide();
		$("#start-record").hide();
		$("#stop-record").hide();
	}else if(type == 3){//监听
		$("#stop-record").show();
		$("#start-record").hide();
		
		$("#stream-ctl").show();
		
		$("#stop-record-vedio").hide();
		$("#start-record-vedio").hide();
		
		$("#start-vedio").hide();
		$("#stop-vedio").hide();
		
		$("#stop-talk").hide();
		$("#start-talk").hide();
	}else{
		$("#start-vedio").show();
		$("#start-record").show();
		$("#start-record-vedio").show();
		$("#start-talk").show();

		$("#stop-record-vedio").hide();
		$("#stop-vedio").hide();
		$("#stop-record").hide();
		$("#stop-talk").hide();
		$("#stream-ctl").hide();
	}
	
    y += document.body.scrollTop;
    x += document.body.scrollLeft;
    $("#video-conctrol-menu").show();
    $("#video-conctrol-menu").css({"top":y+"px", "left":x+"px"});
	$("body").bind("mousedown", onBodyMouseDown);
}

function hideRMenu() {
	$("#video-conctrol-menu").hide();
	$("body").unbind("mousedown", onBodyMouseDown);
}

function onBodyMouseDown(event){
	if (!(event.target.id == "video-conctrol-menu" || $(event.target).parents("#video-conctrol-menu").length>0)) {
		$("#video-conctrol-menu").hide();
	}
}

function removeInfowindow(){
	if(show_device_infowindow){
		mapUtil.removeOverlay(show_device_infowindow);
	}
}

function checkVedioStream(){
	if(videoList.length > 0){
		var url = "/vedio/inactive/streams.do";
		ajaxAsyncGet(url, {}, function(result){
			if(result.code==0){
				var list = result.data;
				if(list && list.length > 0){
					for(var i=0;i<list.length;i++){
						var item = list[i];
						for(var j=0;j<videoList.length;j++){
							var videoInfo = videoList[j];
							if(item.simCode==videoInfo.simCode && item.channelId==videoInfo.channelId){
								var content = videoInfo.deviceName+"通道"+item.channelId+"疑似音视频推流异常!"
								content = "<span>" + new Date().format("hh:mm:ss") + "</span> 【音视频告警】" + content + "<hr>";
								showErrorMessage(content);
								addRealTimeMessage(content);
							}
						}
					}
				}
			}
		});
	}
}
