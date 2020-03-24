var current_selected_device = null,current_selected_device_lacation;
var lastGpsPoints, pathSimplifierIns = null, pathNavigator, pathData = [];
var gpsAlarmPoints = [], gpsAlarmMaker = [];
var show_device_infowindow;
var follow_car_interval;
var search_device_type = 1, search_device_circle, current_search_district_area_code, current_district_polygons = [];
var selectedSearchDeviceLablePoint;
var selectedDeviceMarkerMap = new JsMap();
var selectedMarkerTextMap = new JsMap();

$(function() {
	//setGpsQueryHourOption();
	var now = new Date();
	$('#gps-history-query-dlg-frm-startTime').val(new Date(now.getTime()-6*60*60*1000).format("yyyy-MM-dd hh:00"));
	$('#gps-history-query-dlg-frm-endTime').val(new Date().format("yyyy-MM-dd hh:00"));
	initRangeDatetimepicker("gps-history-query-dlg-frm-startTime","gps-history-query-dlg-frm-endTime",2);
	
	$("#navigSpeedInp").on('change', updateNavigSpeedTxt);
});

function onSelectDevice(deviceId) {
	var device = getDeviceCache(deviceId);
	var state = getDeviceBizState(deviceId)
	
	if(device.bizState != state){
		device.bizState = state;
	}

	current_selected_device = device;
	lastGpsPoints = null;
	//showDeviceInfoWindow(deviceId, null, null);
	clearPathNavigators();
	getLastGps(device);
	
	startFollowCar(5);
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
				current_selected_device_lacation = gpsInfo;
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
		
				if(follow_car_interval){
					//超过5分钟不上报数据
					var speed = gpsInfo.speed;
					if((new Date().getTime() - gpsInfo.gpsTime) > 5*60*1000){
						
					}else{
						if (lastGpsPoints == null) {
							lastGpsPoints = new Array();
							lastGpsPoints[0] = point;
						} else {
							if (lastGpsPoints.length > 1) {
								lastGpsPoints[0] = lastGpsPoints[1];
							}
							lastGpsPoints[1] = point;
							// 大于2公里不划线
							if (mapUtil.getDistance(lastGpsPoints[0], lastGpsPoints[1]) > 2 * 1000) {
								lastGpsPoints[0] = lastGpsPoints[1];
							}
						}
						
						if(show_device_infowindow){
							showDeviceInfoWindow(deviceId, point, gpsInfo);
						}
						mapUtil.drawPolyline(lastGpsPoints, "red");
					}
		
					//更新仪表盘
					resetSpeedDashboardChart(speed);
					
					//更新显示信息
					setCarOverviewInfo(device,gpsInfo);
				}
			}else{
				stopFollowCar(true);
				showMessage("该车辆无位置信息，无法在地图上显示！");
			}
	   }
	});
}

function getMarker(device,point,direction){
	var deviceId = device.id || device.deviceId;
	var iconUrl = getCarStateIcon(device.bizState);
	var name = getDeviceName(deviceId);
	var marker = mapUtil.getMarker(deviceId, point, name, iconUrl, direction);
	marker.on('click', function(e) {
		var deviceId = e.target.getExtData();
		var lacation = e.target.getPosition();
		current_selected_device = getDeviceCache(deviceId);
		showDeviceInfoWindow(deviceId, lacation,null);
		if(follow_car_interval && !($("#car_info_overview_div").is(':hidden'))){
			startFollowCar(5);
		}
	});
	selectedDeviceMarkerMap.put(deviceId,marker);
	var text = mapUtil.addText(point, name);
	selectedMarkerTextMap.put(deviceId,text);
	return marker;
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
	
	if(current_selected_device && current_selected_device.id == deviceId){
		stopFollowCar(true);
	}
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
			text += "<br/>设&nbsp;备&nbsp;ID：" + device.simCode;
			text += "<br/>数据时间：" + new Date(gpsInfo.gpsTime).format();
			text += "<br/>状&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;态：" + deviceBizStateMap.get(device.bizState);
			if (address) {
				text += "<br/>地址：" + address;
			}
			text += "<br/><span class='amap-info-window-menu'><a href='javascript:void(0);' onclick=\"showCmdDialog('gps-history-query-dlg');\" >轨迹</a> <a href='javascript:void(0);' onclick=\"showCmdDialog('send-take-picture-cmd-dlg');\" >拍照</a> <a href='javascript:void(0);' onclick=\"showVideoDialog();\" >视频</a></span>";
			text ="<div class='amap-info-window-content'>"+text+"</div>";
			// 设置label标签
			show_device_infowindow = mapUtil.addLabel(lacation, text);
			show_device_infowindow.on('close', function(e) {
				show_device_infowindow = null;
			});
		});
	}
}

function getDeviceInfo(gpsInfo){
	var text = "<b>时间：</b>" + new Date(gpsInfo.gpsTime).format();
	text += "<br/><b>ACC：</b>" + (((gpsInfo.state&(1<<0))>>0)== 0 ?"关":"开");
	text += "<br/><b>经度：</b>" + gpsInfo.lng;
	text += "<br/><b>纬度：</b>" + gpsInfo.lat;
	text += "<br/><b>方向：</b>" + parseInt(gpsInfo.direction);
	text += "<br/><b>海拔：</b>" + parseInt(gpsInfo.altitude);
	text += "<br/><b>里程：</b>" + parseInt(gpsInfo.mileage) + "km";
	text += "<br/><b>速度：</b>" + gpsInfo.speed + "km/h";
	text += "<br/><b>油量：</b>" + gpsInfo.oilmass + "L";

	var alarmInfo = getAlarmDesc(gpsInfo);
	if(alarmInfo.length > 35){
		alarmInfo=alarmInfo.substr(0,35)+"……";
	}

	text += "<br/><b>告警：</b>" + alarmInfo;
	return text;
}

function setCarOverviewInfo(device,gpsInfo){
	var html = "<div>时间："+new Date(gpsInfo.gpsTime).format()+"</div>";
	html += "<div><p>ACC："+ (((gpsInfo.state&(1<<0))>>0)== 0 ?"关":"开")+"</p><p>速度："+ parseInt(gpsInfo.speed) + " km/h</p></div>";
	html += "<div><p>里程："+ parseInt(gpsInfo.mileage) + " km</p><p>油量："+ gpsInfo.oilmass + " L</p></div>";
	html += "<div><p>经度："+ gpsInfo.lng + "</p><p>纬度："+ gpsInfo.lat + "</p></div>";
	html += "<div><p>方向："+ parseInt(gpsInfo.direction) + "</p><p>海拔："+ parseInt(gpsInfo.altitude) + " m</p></div>";

	var alarmInfo = getAlarmDesc(gpsInfo);
	
	var alarmStr="";
	if(alarmInfo.length > 35){
		alarmStr=alarmInfo.substr(0,35)+"……";
	}else{
		alarmStr = alarmInfo;
	}

	html += "<div title='"+alarmInfo+"'>告警：" + alarmStr + "</div>";
	$(".car-info-overview-info-detail").html(html);
}

function getAlarmDesc(gpsInfo){
	var alarmStr="";
	if(gpsInfo.alarmInfo){
		alarmStr+=","+gpsInfo.alarmInfo;
	}
	
	//苏标
	/** 高级驾驶辅助系统报警信息 **/
	if(gpsInfo.adasAlarm){
		var type = gpsInfo.adasAlarm.type;
		alarmStr += "," + adasAlarmDesc[type-1];
	}

	 /** 驾驶员状态监测系统报警信息 **/
	if(gpsInfo.dsmAlarm){
		var type = gpsInfo.dsmAlarm.type;
		alarmStr += "," + dsmAlarmDesc[type-1];
	}

	 /** 胎压监测系统报警信息 **/
	if(gpsInfo.tpmAlarm){
		var alarmList = gpsInfo.tpmAlarm.alarmList;
        for (var i = 0;i<alarmList.length;i++) {
            var item = alarmList[i];
            var type = item.type;
            for (var j = 0; j < tpmAlarmDesc.length; j++) {
				var s = "";
				if((((type&(1<<j))>>j)==1)) {
					s = tpmAlarmDesc[j];
				}
				if (s!="") {
					alarmStr += "," + s;
				}
			}
        }
	}
	
	/** 盲区监测系统报警信息 **/
	if(gpsInfo.bsdAlarm){
		var type = gpsInfo.bsdAlarm.type;
		alarmStr += "," + bsdAlarmDesc[type-1];
	}

	//1078视频
	if(gpsInfo.videoAlarm){
		var videoAlarm = gpsInfo.videoAlarm;
		if(videoAlarm > 0){
			for(var i=0;i<videoAlarmDesc.length;i++){
				var s = videoAlarmDesc[i];
				if(s && ((videoAlarm&(1<<i))>>i)==1 ){
					alarmStr += "," + s;
				}
			}
		}
	}

	var alarm = gpsInfo.alarm;
	for(var i=0;i<32;i++){
		var s="";
		if((((alarm&(1<<i))>>i)==1)){
			s=carAlarmInfoMap.get(i);
		}
		if(s && s!="未定义"){
			alarmStr+=","+s;
		}
	}

	if(alarmStr){
		alarmStr=alarmStr.substr(1);
	}else{
		alarmStr="-"
	}
	
	return alarmStr;
}

function syncGetLastGps(deviceId) {
	var url = lbs_api_server_servlet_path + "/gps/" + deviceId + "/last.json";
	var result = ajaxSyncGet(url, {});
	if (result.code == 0) {
		return result.data;
	}
	return null;
}

function clearPathNavigators() {
	pathData = [];
	gpsAlarmPoints = [];
	for(var i=0;i<gpsAlarmMaker.length;i++){
		mapUtil.removeOverlay(gpsAlarmMaker[i]);
	}
	gpsAlarmMaker = [];
	if (pathSimplifierIns) {
		pathSimplifierIns.clearPathNavigators();
		pathSimplifierIns.setData(null);
	}
}

function setGpsQueryHourOption() {
	var interval = 6;
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
		$("#gps-history-query-dlg-frm-hour").append("<option value='" + text + "'>" + text + "</option>");
	}
	var now = new Date();
	$("#gps-history-query-dlg-frm-hour").get(0).selectedIndex = Math.floor(now.getHours() / interval);
	$('#gps-history-query-dlg-frm-startTime').val(now.format("yyyy-MM-dd"));
}

function doHistoryGpsQuery() {
	var formId = "gps-history-query-dlg-frm";
	if (!validForm(formId)) {
		return;
	}
	closeDialog();
	clearPathNavigators();
	//隐藏仪表盘
	stopFollowCar(true);
	
	var form = $("#"+formId).serializeObject();
	var startTime = form.startTime+":00";
	var endTime = form.endTime+":00";

	var data = {};
	var conditions = {};
	conditions["deviceId.eq"] = current_selected_device.id;
	data["conditions"] = conditions;

	var startTime = new Date(startTime.replace(/-/g, "/")).getTime();
	var endTime = new Date(endTime.replace(/-/g, "/")).getTime();

	var rangeCondition = {};
	rangeCondition["fieldName"] = "gpsTime";
	rangeCondition["from"] = startTime;
	rangeCondition["includeLower"] = true;
	rangeCondition["includeUpper"] = true;
	rangeCondition["to"] = endTime;
	var rangeConditions = new Array();
	rangeConditions[0] = rangeCondition;
	data["rangeConditions"] = rangeConditions;
	
	startLoading();
	setTimeout(function(){
		queryHistoryGpsHandler(startTime,endTime,data);
	},50);
}

function queryHistoryGpsHandler(startTime,endTime,params){
	var time = startTime;
	while(time <= endTime){
		params["recordDate"] = new Date(time).format("yyyyMMdd");
		var pageSize = 1000,pageNo=1;
		time = time + 24*60*60*1000;
		queryHistoryGps(pageSize,pageNo,params,(time >= endTime));
	}
}

function queryHistoryGps(pageSize,pageNo,params,last){
	var url = lbs_api_server_servlet_path
	+ "/common/query/gps.json?select=lng,lat,gpsTime,speed,mileage,oilmass,altitude,direction,state,alarm&orderBy=gpsTime&pageSize="+pageSize+"&pageNo="
	+ pageNo;
	
	var result = ajaxSyncPost(url, params);
	if (result.code!=0) {
		endLoading();
		showErrorMessage(result.message);
	}else{
		var list = result.data;
		if(list && list.length>0){
			setPathData(list, false);
			if(list.length >= pageSize){
				pageNo++;
				queryHistoryGps(pageSize,pageNo,params,last);
				return;
			}
		}
		if(last){
			if(pathData.length==0){
				showMessage("您所查询的时间段内无该车辆的定位数据");
			}else{
				setPathData([], true);
			}
			endLoading()
		}
	}
}

function drawPolyline(gpsList) {
	// 地图上画轨迹
	var map = mapUtil.getMap();
	var points = new Array();
	var index = 0;
	for (var i = 0, l = gpsList.length; i < l; i++) {
		var item = gpsList[i];
		var gcj02 = LngLatConverter.wgs84togcj02(item.lng, item.lat);
		var point = mapUtil.getPoint(gcj02[0], gcj02[1]);
		if (points.length > 0 && mapUtil.getDistance(points[points.length - 1], point) > 2 * 1000) {
			map.panTo(points[0]);
			mapUtil.drawPolyline(points, "red");
			points = new Array();
			index = 0;
		}
		points[index++] = point;
	}

	if (points.length > 0) {
		map.panTo(points[0]);
		mapUtil.drawPolyline(points, "red");
	}
}

function setPathData(gpsList, start) {
	var formData = $("#gps-history-query-dlg-frm").serializeObject();
	var overspeed = formData.overspeed;
	var acceleration = formData.acceleration;
	var deceleration = formData.deceleration;
	var turningDirection = formData.turningDirection;
	//变化的方向角
	var direction = 0,time = 0;
	
	var lastPoint = null;
	var points = new Array();
	var accIndex = 0;
	for (var i = 0, l = gpsList.length; i < l; i++) {
		var item = gpsList[i];
		if (item.lng == 0 || item.lat == 0) {
			continue;
		}
		var gcj02 = LngLatConverter.wgs84togcj02(item.lng, item.lat);
		var point = mapUtil.getPoint(gcj02[0], gcj02[1]);
		point.gpsTime=item.gpsTime;
		point.speed=item.speed;
		point.mileage=item.mileage;
		point.oilmass=item.oilmass;
		point.direction=item.direction;
		point.altitude=item.altitude;
		point.state=item.state;
		point.alarm=item.alarm;
		
		var alarmInfo="";
		var flag = false;
		var acc = ((item.state&(1<<0))>>0 == 0);
		//超速检测
		if(acc && overspeed && overspeed > 0 && item.speed > overspeed){
			gpsAlarmPoints.push(point);
			flag = true;
			alarmInfo+=",超速";
		}
		
		acc = acc && lastPoint && (((lastPoint.state&(1<<0))>>0)== 0);
		//急加速检测
		if(acc && acceleration && acceleration > 0 && (item.speed-lastPoint.speed)*1000/(item.gpsTime-lastPoint.gpsTime) >=acceleration){
			if(!flag){
				gpsAlarmPoints.push(point);
			}
			flag = true;
			alarmInfo+=",急加速";
		}
		
		//急转弯检测
		if(acc && turningDirection && turningDirection > 0 && item.speed >=turningDirection && lastPoint.speed >= turningDirection){
			var d = Math.abs(lastPoint.direction-item.direction);
			var t = item.gpsTime-lastPoint.gpsTime;
			var init = false;
			if(acc && d > 5 && t < 5*1000){//5秒钟内方向角变化超过5度可能是正在转弯
				direction+=d;
			}else{
				direction=0;
			}

			if(direction >= 45){
				if(!flag){
					gpsAlarmPoints.push(point);
				}
				flag = true;
				alarmInfo=",急转弯";
				direction=0;
			}
		}else{
			direction=0;
		}
		
		//急刹车检测
		if(acc && deceleration && deceleration > 0 && (lastPoint.speed-item.speed)*1000/(item.gpsTime-lastPoint.gpsTime) >=deceleration){
			if(!flag){
				gpsAlarmPoints.push(point);
			}
			flag = true;
			alarmInfo=",急刹车";
		}
		
		if(alarmInfo!=""){
			point.alarmInfo = alarmInfo.substr(1);
		}
		
		lastPoint = item;
		if (points.length > 0 && mapUtil.getDistance(points[points.length - 1], point) > 2 * 1000) {
			pathData.push({
				path : points
			});
			points = new Array();
		}
		points.push(point);
	}

	if (points.length > 0) {
		pathData.push({
			path : points
		});
	}
	if (start && pathData.length>0) {
		startPathSimplifier(pathData);
	}
}

function startPathSimplifier(pathData) {
	mapUtil.clearOverlays();
	$(".navigRoute").show();
	AMapUI.load([ 'ui/misc/PathSimplifier', 'lib/$' ], function(PathSimplifier, $) {
		if (!PathSimplifier.supportCanvas) {
			var map = mapUtil.getMap();
			var points;
			for (var i = 0, l = pathData.length; i < l; i++) {
				var item = pathData[i];
				points = item.path;
				mapUtil.drawPolyline(points, "red");
			}
			if (points) {
				map.panTo(points[0]);
			}
			return;
		}

		pathSimplifierIns = new PathSimplifier({
			zIndex : 100,
			autoSetFitView:true,
			map : mapUtil.getMap(), // 所属的地图实例
			getPath : function(pathData, pathIndex) {
				return pathData.path;
			},
			getHoverTitle : function(pathData, pathIndex, pointIndex) {
				 if (pointIndex >= 0) {
	                    var points = pathData.path;
	                    var point = points[pointIndex];
	                    var text = getDeviceInfo(point);
	    				return text;
	             }
			},
			renderOptions : {
				renderAllPointsIfNumberBelow:1000,
				pathLineStyle : {
					dirArrowStyle : true
				},
				getPathStyle : function(pathItem, zoom) {
					var color = '#eb1238', lineWidth = 5;
					return {
						pathLineStyle : {
							strokeStyle : color,
							lineWidth : lineWidth
						},
						pathLineSelectedStyle : {
							lineWidth : lineWidth + 2
						},
						pathNavigatorStyle : {
							fillStyle : color
						}
					};
				}
			}
		});

		// 设置数据
		pathSimplifierIns.setData(pathData);

		var pathDataLength = pathData.length;
		var pathIndex = 0;
		pathNavigator = createPathNavigator(PathSimplifier, pathIndex);
		pathNavigator.start();
		
		var pathNavigatorInterval = setInterval(function() {
			if(pathSimplifierIns==null){
				clearInterval(pathNavigatorInterval);
				return;
			}
			if (pathNavigator.isCursorAtPathEnd()) {
				pathIndex++;
				pathNavigator.destroy();
				if (pathIndex >= pathDataLength) {
					clearInterval(pathNavigatorInterval);
					return;
				}
				// 重新建立一个巡航器
				pathNavigator = null;
				pathNavigator = createPathNavigator(PathSimplifier, pathIndex);
				pathNavigator.start();
			}
		}, 1 * 1000);

		pathData = new Array();
	});
	
	if(gpsAlarmPoints.length>0){
		for(var i= 0;i < gpsAlarmPoints.length;i++){
			gpsAlarmMaker[i] = mapUtil.addCircleMarker(gpsAlarmPoints[i]);
		}
	}
}

function createPathNavigator(PathSimplifier, pathIndex) {
	var pathNavigatorImageContent = PathSimplifier.Render.Canvas.getImageContent(getCarDefaultIcon(), onload, onerror);
	return pathSimplifierIns.createPathNavigator(pathIndex, {
		loop : false, // 循环播放
		speed : parseInt($("#navigSpeedInp").val(), 10),// 巡航速度，单位千米/小时
		pathNavigatorStyle : {
			width : 15,
			height : 25,
			content : pathNavigatorImageContent,
			strokeStyle : null,
			fillStyle : null
		}
	});
}


function refreshNavgButtons(btnIdx) {
	var enableExp =["1,3,4","2,3,4","1,3,4","0,4"];
	var indexs = enableExp[btnIdx];
    $('.navigBtn').each(function() {
    	 var bool = indexs.indexOf($(this).attr("data-btnidx"))==-1;
         $(this).prop('disabled', bool);
    });
}

function updateNavigSpeedTxt(){
      var speed = parseInt($("#navigSpeedInp").val(), 10);
      $("#navigSpeedText").html('轨迹巡航时速：' + speed + ' km/h');
      if (pathNavigator) {
    	  pathNavigator.setSpeed(speed);
      }
}

function startNavig(){
	if (pathNavigator) {
		pathNavigator.start();
	}
	refreshNavgButtons(0);
}

function pauseNavig(){
	if (pathNavigator) {
		pathNavigator.pause();
		refreshNavgButtons(1);
	}
}

function resumeNavig(){
	if (pathNavigator) {
		pathNavigator.resume();
		refreshNavgButtons(2);
	}
}

function stopNavig(){
	if (pathNavigator) {
		pathNavigator.stop();
		refreshNavgButtons(3);
	}
}

function destroyNavig(){
	if (pathNavigator) {
		pathNavigator.destroy();
		$(".navigRoute").hide();
		mapUtil.clearOverlays();
	}
}

function onClickFollowCar() {
	var text = $("#follow_car_text_id").html();
	if (text == "实时追踪") {
		onStartFollowCar();
	} else {
		stopFollowCar(false);
	}
}

function startFollowCar(interval) {
	resetSpeedDashboardChart(0);
	$("#car_info_overview_div").show();
	if (current_selected_device && isOnline(current_selected_device.id)) {
		current_selected_device.state = 3;
		clearInterval(follow_car_interval);
		follow_car_interval = setInterval(function() {
			getLastGps(current_selected_device);
		}, interval * 1000);
	}

	if(show_device_infowindow){
		mapUtil.removeOverlay(show_device_infowindow);
		show_device_infowindow = null;
	}

	var gpsInfo = syncGetLastGps(current_selected_device.id);
	if(gpsInfo){
		setCarOverviewInfo(current_selected_device,gpsInfo);
	}

	var bizState = deviceBizStateMap.get(current_selected_device.bizState);
	
	var car = getDeviceCarCache(current_selected_device.id);
	var plateNumber = current_selected_device.name;
	var carId = "";
	if(car){
		plateNumber = car.plateNumber || plateNumber;
		carId = car.carId;
	}
	//查询车辆相关统计信息
	setCarStaticInfo(carId);
	
	$("#car-info-plateNumber-div").html(plateNumber+"("+bizState+")" + "<a class='amap-info-close' href='javascript: void(0)' onclick='stopFollowCar(false);' style='right: 5px;'>×</a>");
}



function setCarStaticInfo(carId){
	$("#car-info-static-mileage").html(0);
	$("#car-info-static-oilmass").html(0);
	$("#car-info-static-onlineTime").html(0);
	if(!carId){
		return;
	}
	
	var data = {};
	var conditions = {};
	conditions["carId.eq"] = carId;
	data["conditions"] = conditions;
	var url = report_api_server_servlet_path + "/sum/car_report.json?select=totalOnlieTime,totalMileage,totalOilmass";
	ajaxAsyncPost(url, data, function(result) {
		if (result.code != 0) {
			showErrorMessage(result.message);
		} else {
			var data = result.data;
			if(data){
				var totalMileage = data.totalMileage?data.totalMileage:0;
				if(totalMileage > 10000){
					totalMileage = (totalMileage/10000).toFixed(2) + "万"
				}else{
					totalMileage = totalMileage.toFixed(2);
				}
				var totalOilmass = data.totalOilmass?data.totalOilmass:0;
				if(totalOilmass > 10000){
					totalOilmass = (totalOilmass/10000).toFixed(2) + "万"
				}else{
					totalOilmass = totalOilmass.toFixed(2);
				}
				var onlineTime = data.totalOnlieTime?getFullNum(data.totalOnlieTime):0;
				onlineTime = onlineTime / (1000 * 60 * 60);
				if(onlineTime > 10000){
					onlineTime = (onlineTime/10000).toFixed(2) + "万"
				}else{
					onlineTime = onlineTime.toFixed(2);
				}
				$("#car-info-static-mileage").html(totalMileage);
				$("#car-info-static-oilmass").html(totalOilmass);
				$("#car-info-static-onlineTime").html(onlineTime);
			}
		}
	});
}

function getFullNum(num){
    //处理非数字
    if(isNaN(num)){return num};
    
    //处理不需要转换的数字
    var str = ''+num;
    if(!/e/i.test(str)){return num;};
    
    return (num).toFixed(18).replace(/\.?0+$/, "");
}


function onStartFollowCar(){
	if (current_selected_device) {
		var bool = isOnline(current_selected_device.id)
		if (!bool) {
			showErrorMessage("车辆不在线，不能启动实时追踪！");
			return;
		}
		startFollowCar(5);
		$("#follow_car_text_id").html("停止追踪");
	} else {
		showMessage("请选择要操作的车辆！");
	}
}

function stopFollowCar(closeInfowindow) {
	$("#follow_car_text_id").html("实时追踪");
	hideCarInfoOverviewDiv();
	clearInterval(follow_car_interval);
	if(closeInfowindow && show_device_infowindow){
		mapUtil.removeOverlay(show_device_infowindow);
		show_device_infowindow = null;
	}
}

function searchDeviceByRail() {
	cleanSearchDeviceDraw();
	showMessage("请在地图上画圆形区域查车！");
	mapUtil.closeDraw();
	mapUtil.openDraw();
	mapUtil.drawCircle(function(e, circle) {
		circle = e.obj;
		search_device_circle = circle;
		var point = circle.getCenter();
		// 国测局坐标转wgs84坐标
		var wgs84 = LngLatConverter.gcj02towgs84(point.getLng(), point.getLat());
		var area = {};
		area.radius = parseInt(circle.getRadius());
		area.lat = wgs84[1];
		area.lng = wgs84[0];
		mapUtil.closeDraw();
		var data = {};
		data.type = 1;
		data.area = area;
		search_device_type = 1;
		var url = lbs_api_server_servlet_path + "/area/devices.json";
		ajaxAsyncPost(url, data, loadAreaDevicesResultHandler, true);
	});
}

function searchDeviceByRectangle() {
	cleanSearchDeviceDraw();
	showMessage("请在地图上画矩形区域查车！");
	mapUtil.closeDraw();
	mapUtil.openDraw();
	mapUtil.drawRectangle(searchDeviceDrawPolygonCompletedHandler);
}

function searchDeviceByPolygon() {
	cleanSearchDeviceDraw();
	showMessage("请在地图上画多边形区域查车！");
	mapUtil.closeDraw();
	mapUtil.openDraw();
	mapUtil.drawPolygon(searchDeviceDrawPolygonCompletedHandler);
}

function cleanSearchDeviceDraw() {
	for (var i = 0, l = current_district_polygons.length; i < l; i++) {
		current_district_polygons[i].setMap(null);
	}
	if (search_device_circle) {
		mapUtil.removeOverlay(search_device_circle);
	}
}

function searchDeviceDrawPolygonCompletedHandler(e, polygon) {
	polygon = e.obj;
	current_district_polygons = [];
	current_district_polygons.push(polygon);
	var path = polygon.getPath();
	var points = "";
	// 国测局坐标转wgs84坐标
	for (var i = 0; i < path.length; i++) {
		var point = path[i];
		var wgs84 = LngLatConverter
				.gcj02towgs84(point.getLng(), point.getLat());
		points += ";" + wgs84[0] + "," + wgs84[1];
	}
	var area = {};
	area.path = points.substr(1);
	var data = {};
	data.type = 2;
	data.area = area;
	search_device_type = 2;
	var url = lbs_api_server_servlet_path + "/area/devices.json";
	ajaxAsyncPost(url, data, loadAreaDevicesResultHandler, true);
	mapUtil.closeDraw();
}

function searchDeviceByDistrict() {
	if (!current_search_district_area_code) {
		showMessage("当前没有选中的区域，请重新选择区域！");
		return;
	}

	var data = {};
	data.type = 3;
	data.areaCode = current_search_district_area_code;
	search_device_type = 2;
	var url = lbs_api_server_servlet_path + "/area/devices.json";
	ajaxAsyncPost(url, data, loadAreaDevicesResultHandler, true);
}

function openSearchDeviceByLablePointDialog() {
	var pageSize = 100;
	var pageNo = 1;
	var url = management_api_server_servlet_path + "/common/query/labelPoint.json?select=id,name&countable=false&pageSize=" + pageSize + "&pageNo=" + pageNo + "&orderBy=createTime&desc=true";
	ajaxAsyncPost(url, {
		"bizType.eq" : 4
	}, function(result) {
		if (result.code != 0) {
			showErrorMessage(result.message);
		} else {
			var select = $("#search-device-by-lablePoint-frm-lablePointId")
			select.empty();
			select.append("<option value=''>请选择区域</option>");
			var list = result.data;
			for (var i = 0, l = list.length; i < l; i++) {
				var arr = list[i];
				select.append("<option value='" + arr[0] + "'>" + arr[1] + "</option>");
			}
			var dialogId = "search-device-by-lablePoint-dlg";
			var title = $("#" + dialogId).attr("title");
			showDialog("区域查车", dialogId);
		}
	}, true);
}

function onSelectSearchDeviceLablePoint() {
	clearOverlays();
	var id = $("#search-device-by-lablePoint-frm-lablePointId").val();
	if (!id) {
		selectedSearchDeviceLablePoint = null;
		return;
	}
	var url = management_api_server_servlet_path + "/common/labelPoint/" + id + ".json";
	ajaxAsyncGet(url, {}, function(result) {
		if (result.code != 0) {
			showErrorMessage(result.message);
		} else {
			var lablePoint = result.data;
			selectedSearchDeviceLablePoint = lablePoint;
			var type = lablePoint.type;
			var area = $.evalJSON(lablePoint.setting);
			if (type == 2) {
				// "圆形";
				var gcj02 = LngLatConverter.wgs84togcj02(area.lng, area.lat);
				var point = mapUtil.getPoint(gcj02[0], gcj02[1]);
				mapUtil.addCircle(point, area.radius);
			} else if (type == 3 || type == 4) {
				// "矩形"、"多边形";;
				var arr = area.path.split(";");
				var path = new Array();
				for (var j = 0; j < arr.length; j++) {
					var point = arr[j].split(",");
					var gcj02 = LngLatConverter
							.wgs84togcj02(point[0], point[1]);
					path.push(mapUtil.getPoint(gcj02[0], gcj02[1]));
				}
				mapUtil.addPolygon(path);
			}
			mapUtil.getMap().setFitView();
		}
	});
}

function searchDeviceByLablePoint() {
	if (selectedSearchDeviceLablePoint == null) {
		showMessage("请选择区域");
		return;
	}
	var type = selectedSearchDeviceLablePoint.type;
	var data = {};
	data.area = $.evalJSON(selectedSearchDeviceLablePoint.setting);
	var url = lbs_api_server_servlet_path + "/area/devices.json";
	if (type == 2) {
		// "圆形";
		data.type = 1;
		search_device_type = 1;
		ajaxAsyncPost(url, data, loadAreaDevicesResultHandler, true);
	} else if (type == 3 || type == 4) {
		// "矩形"、"多边形";;
		data.type = 2;
		search_device_type = 2;
		ajaxAsyncPost(url, data, loadAreaDevicesResultHandler, true);
	}
}

function searchDistrict(object, level) {
	// 清除地图上所有覆盖物
	for (var i = 0, l = current_district_polygons.length; i < l; i++) {
		current_district_polygons[i].setMap(null);
	}
	var areaCode = $(object).val();
	var district = mapUtil.getDistrictSearch();
	// district.setLevel(level); //行政区级别
	district.setSubdistrict(0);
	district.setExtensions("all");
	// 行政区查询
	// 按照adcode进行查询可以保证数据返回的唯一性
	district.search(areaCode, function(status, result) {
		if (status === 'complete') {
			setDistrictSearchResult(result);
		}
	});
	current_search_district_area_code = areaCode;
	if (level != "biz_area") {
		loadDistrict(areaCode, level);
	}
}

function setDistrictSearchResult(result) {
	current_district_polygons = [];
	var district = result.districtList[0];
	var bounds = district.boundaries;
	var map = mapUtil.getMap();
	if (bounds) {
		for (var i = 0, l = bounds.length; i < l; i++) {
			var polygon = mapUtil.addPolygon(bounds[i]);
			current_district_polygons.push(polygon);
		}
		map.setFitView();// 地图自适应
	}
}

function loadDistrict(parentCode, level) {
	var url = management_api_server_servlet_path
			+ "/system/dictionary/district.json?select=code,name&countable=false&pageSize=1000&pageNo=1";
	var data = {
		"parentCode.eq" : parentCode
	};
	ajaxAsyncPost(url, data, function(result) {
		if (result.code != 0) {
			showErrorMessage(result.message);
		} else {
			var list = result.data;
			var select;
			if (level === 'province') {
				$("#district-search-province").empty();
				$("#district-search-city").empty();
				$("#district-search-district").empty();
				select = $("#district-search-province");
			} else if (level === 'city') {
				$("#district-search-city").empty();
				$("#district-search-district").empty();
				select = $("#district-search-city");
			} else if (level === 'district') {
				$("#district-search-district").empty();
				select = $("#district-search-district");
			}
			select.append("<option value=''>请选择</option>");
			for (var i = 0, l = list.length; i < l; i++) {
				var arr = list[i];
				select.append("<option value='" + arr[0] + "'>" + arr[1]
						+ "</option>");
			}
		}
	});
}

function clearSelectedMarker(){
	if(selectedMarkerTextMap){
		var list = selectedMarkerTextMap.getValues();
		for(var i=0;i<list.length;i++){
			mapUtil.removeOverlay(list[i]);
		}
	}

	selectedDeviceMarkerMap = new JsMap();
	selectedMarkerTextMap = new JsMap();
}

function loadAreaDevicesResultHandler(result) {
	clearDeviceMarkers();
	clearSelectedMarker();
	if (!result || !result.data || result.data.length == 0) {
		showMessage("该区域内无车辆！");
		return;
	}
	var list = result.data;

	var count = 0;
	for (var index = 0, l = list.length; index < l; index++) {
		var item = list[index];
		var gcj02 = LngLatConverter.wgs84togcj02(item.lng, item.lat);
		var lacation = mapUtil.getPoint(gcj02[0], gcj02[1]);
		// 如果是多边形区域，再次计算是否在区域内
		if (search_device_type == 2 && current_district_polygons.length > 0) {
			var bool = false;
			for (var i = 0; i < current_district_polygons.length; i++) {
				if (current_district_polygons[i].contains(lacation)) {
					bool = true;
					break;
				}
			}
			if (!bool) {
				continue;
			}
		} else if (search_device_type == 1 && search_device_circle) {
			// 如果是圆形区域，再次计算是否在区域内
			var point = search_device_circle.getCenter();
			var radius = search_device_circle.getRadius();
			if (point.distance(lacation) > radius) {
				continue;
			}
		}
		count++;
		var device = {};
		device.id = item.deviceId;
		device.bizState = getDeviceBizState(item.deviceId);
		device.name = getDeviceName(item.deviceId);
		getMarker(device,lacation,item.direction);
	}

	showMessage("该区域内有" + count + "辆车！");
	setDeviceMarkerClusterer(selectedDeviceMarkerMap.getValues());
}
