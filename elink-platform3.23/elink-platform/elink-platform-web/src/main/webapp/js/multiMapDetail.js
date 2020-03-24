var follow_car_interval,selectedDeviceMarker,selectedMarkerText;
var points;

function startFollowCar(deviceId,interval) {
	if (!isOnline(deviceId)) {
		showErrorMessage("车辆已不在线，不能启动实时追踪！");
		return false;
	}
	if(selectedDeviceMarker){
		mapUtil.removeOverlay(selectedDeviceMarker);
		selectedDeviceMarker=null;
	}
	if(selectedMarkerText){
		mapUtil.removeOverlay(selectedMarkerText);
		selectedMarkerText=null;
	}
	points = null;
	clearInterval(follow_car_interval);
	getLastGps(deviceId);
	follow_car_interval = setInterval(function() {
		getLastGps(deviceId);
	}, interval * 1000);
	return true;
}

function getLastGps(deviceId) {
	var url = lbs_api_server_servlet_path + "/gps/" + deviceId + "/last.json";
	ajaxAsyncGet(url, {}, function(result) {
		if (!result || !result.data) {
			return;
		}
		var gpsInfo = result.data
		var gcj02 = LngLatConverter.wgs84togcj02(gpsInfo.lng, gpsInfo.lat);
		var point = mapUtil.getPoint(gcj02[0], gcj02[1]);

		if (points == null) {
			points = new Array();
			points[0] = point;
		} else {
			if (points.length > 1) {
				points[0] = points[1];
			}
			points[1] = point;
			// 大于2公里不划线
			if (mapUtil.getDistance(points[0], points[1]) > 2 * 1000) {
				points[0] = points[1];
			}
		}

		if(!selectedDeviceMarker){
			addMarker(deviceId,point,gpsInfo.direction);
		}else{
			mapUtil.setMarkerPosition(selectedDeviceMarker, point);
			mapUtil.setMarkerRotation(selectedDeviceMarker, gpsInfo.direction);
			mapUtil.setTextPosition(selectedMarkerText, point);
		}
		
		var map = mapUtil.getMap();
		map.panTo(point);
		mapUtil.drawPolyline(points, "red");
	});
}

function addMarker(deviceId,point,direction){
	var device = getDeviceCache(deviceId);
	var state = getDeviceBizState(deviceId)
	if(device.bizState != state){
		device.bizState = state;
	}
	var iconUrl = getCarStateIcon(device.bizState);
	var name = device.name;
	selectedDeviceMarker = mapUtil.addMarker(deviceId, point, name, iconUrl, direction);
	selectedMarkerText = mapUtil.addText(point, name);
}