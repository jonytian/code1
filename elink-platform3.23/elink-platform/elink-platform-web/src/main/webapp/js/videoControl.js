var MAP_TYPE = 1, MAP_DIV = "map_box";
var TREE_MAX_CHANNEL = 4,MAX_VEDEO = 16,heartbeatInterval,clinetId;
var show_device_infowindow;
var selectedDeviceMarkerMap = new JsMap();
var selectedMarkerTextMap = new JsMap();
var videoList = [],playerFlagList=[];
var curent_video_index = 0,curent_car_node,max_video_view = 4;
var rtmp_server_url = "";
var deviceListMap = new JsMap();

$(function() {
	initBootstrapTable();
	initTree("car-tree");
	rightChange();
	setHeight();
	clinetId = getClinetId();
	loadRtmpServer();

	for(var i=0;i< MAX_VEDEO ;i++){
		playerFlagList[i]=0;
	}
	
	//默认4路
	showVideoView(2);
	
	//15秒轮播一次
	//setInterval(function() {
	//   resetPlayerContainer(false);
	//}, 15 * 1000);
	
	//推流检查
	setInterval(function() {
	   checkVedioStream();
	}, 30 * 1000);
});

$(window).resize(function(){
	setHeight();
	showVideoView(Math.sqrt(max_video_view));
});

function setHeight(){
	var height = $("#content-div").height()-$("#map_right_top").outerHeight(true);
	$("#real-time-message-div").height(height-30);
	$("#car-tree-div").height(height);
}

function showVideoView(n){
	max_video_view = n * n;
	for(var i=0;i<MAX_VEDEO;i++){
		$("#player-container"+i).css({"width":"100%","height":"100%;"});
		$("#player-container"+i).parent().attr("class","outside-window");
	}

	for(var i = 0;i<max_video_view;i++){
		var box = $("#player-container"+i).parent();
		setVideoClass(box,i);
	}
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
			showMessage("无可用流媒体服务器，请联系系统管理员配置！");
		}
	}
}

function resetPlayerContainer(reset){
	var size = videoList.length;
	if(size > max_video_view){
		var index = 0;
		for(var i=0;i<size;i++){
			var videoInfo = videoList[i];
			var container = $("#player-container"+videoInfo.index).parent();
			if((i < curent_video_index &&  i >= (curent_video_index + max_video_view - size)) || i >= (curent_video_index + max_video_view)){
				container.attr("class","outside-window");
				videoInfo.player.videoPause();
			}else {
				//container.attr("class",videoClassList[index++]);
				setVideoClass(container,index++);
				videoInfo.player.videoPlay();
			}
		}
		curent_video_index++;
		if(curent_video_index >= size){
			curent_video_index=0;
		}
	}else{
		if(curent_video_index!=0 || reset){
			for(var i=0;i<MAX_VEDEO;i++){
				var container = $("#player-container"+i).parent();
				container.attr("class","outside-window");
			}
			var index = 0;
			var arr = [];
			for(var i=0;i<size;i++){
				var videoInfo = videoList[i];
				arr.push(videoInfo.index);
				var container = $("#player-container"+videoInfo.index).parent();
				setVideoClass(container,index++);
 				videoInfo.player.videoPlay();
			}

			while(arr.length < max_video_view){
				for(var i=0;i<MAX_VEDEO;i++){
					if(arr.indexOf(i)==-1){
						arr.push(i);
						var container = $("#player-container"+i).parent();
						setVideoClass(container,index++);
						break;
					}
				}
			}
			curent_video_index = 0;
		}
	}
}

function setVideoClass(box,index){
	var n = Math.sqrt(max_video_view);
	var percent = (parseFloat(100-n)/parseFloat(n)).toFixed(5);
	var width =  Math.floor($(box).parent().width()*percent/100);
	var height = Math.floor($(box).parent().height()*percent/100);
	$(box).css("width",width);
	$(box).css("height",height);
	$(box).attr("class","video_box");
	if(index==0){
		return;
	}
	if(index < n){
		$(box).addClass("magin_left");
	}else if(index%n==0){
		$(box).addClass("magin_top");
	}else {
		$(box).addClass("magin_top magin_left");
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

function onCheckAllBootstrapTableRow(rows) {
//	for (var i = 0; i < rows.length; i++) {
//		var row = rows[i];
//		var deviceId = row[1];
//		onSelectDevice(deviceId);
//		//加入直播队列
//		for(var i=1;i<=TREE_MAX_CHANNEL;i++){
//			startVideo(deviceId,i);
//		}
//	}
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

function onUncheckAllBootstrapTableRow(rows) {
//	for (var i = 0; i < rows.length; i++) {
//		var row = rows[i];
//		var deviceId = row[1];
//		//删除marker
//		for(var i=1;i<=TREE_MAX_CHANNEL;i++){
//			stopVideo(deviceId,i);
//		}
//		
//		unselectDevice(deviceId,1);
//	}
}

function startVideo(deviceId,channelId,dataType){
	if(rtmp_server_url==""){
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
			//if (videoList.length >= max_video_view) {
			//	showMessage("视频直播已超过" + max_video_view + "路，视频进入轮播模式!");
			//}
			var url = message_api_server_servlet_path + "/video/1078/" + deviceId + ".json?clinetId=" + clinetId;
			var data = {};
			data.desc = "实时音视频";
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
				videoInfo.src = rtmp_server_url.replace("{simCode}",simCode).replace("{channel}",videoInfo.channelId);
				startRtmpT5player(videoInfo);
				videoList.push(videoInfo);
				
				//自动切换更大模式
				if (videoList.length > max_video_view) {
					showVideoView(Math.sqrt(max_video_view)+1);
				}
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
		videoInfo.player.videoPause();
		videoInfo.player.videoClear();
		$("#player-container"+videoInfo.index).html("");
		$("#player-container"+videoInfo.index).css("background-color","");
	}
	//轮播
	//if(videoList && videoList.length==0){
		//clearInterval(heartbeatInterval);
	//}
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
			var device = getDeviceCache(deviceId);
			var text = "车&nbsp;&nbsp;牌&nbsp;&nbsp;号：" + getDeviceName(deviceId)+"";
			text += "<br/>设备ID：" + device.simCode;
			text += "<br/>数据时间：" + new Date(gpsInfo.gpsTime).format();
			text += "<br/>状&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;态：" + deviceBizStateMap.get(device.bizState);
			if (address) {
				text += "<br/>地址：" + address;
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
	
	var rowItem = deviceListMap.get(deviceId);
	if(rowItem){
		var row = rowItem.row;
		row[4]=state;
		$('#boot-strap-table').bootstrapTable('updateRow', {index: rowItem.index, row: row})
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

function startRtmpT5player(videoInfo) {
	var index = 0;
	for(var i=0;i<playerFlagList.length;i++){
		if(playerFlagList[i] == 0){
			index = i;
			break;
		}
	}
	var player = initRtmpCkplayer(index,videoInfo);
	videoInfo.player = player;
	videoInfo.index = index;
	playerFlagList[index]=1;
}

function initRtmpCkplayer(index,videoInfo){
	var container = "player-container" + index;
	var videoObject = {
			container: '#'+container,//“#”代表容器的ID，“.”或“”代表容器的class
			variable: 'player',//该属性必需设置，值等于下面的new chplayer()的对象
			autoplay:true,//自动播放
			live:true,//直播视频形式
			video:videoInfo.src//视频地址
		};
	var player=new ckplayer(videoObject);
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