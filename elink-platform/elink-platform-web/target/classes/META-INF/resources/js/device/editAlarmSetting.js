var district_alarm_setting_dlg_frm_level;
var district_alarm_setting_dlg_frm_bounds;
var district_alarm_setting_dlg_frm_areaCode;

$(function() {
	setHourOption("startHour", true);
	setHourOption("endHour", false);
	initTree("group-tree");
	initDatetimepicker();
	$(document).on("change", 'select#inside', function() {
		if ($(this).val() == "2") {
			$("#speed-div").show();
		} else {
			$("#speed-div").hide();
		}
	});
});

function setHourOption(id, start) {
	for (var i = 0; i < 24; i++) {
		var text = i;
		if (i < 10) {
			text = "0" + i;
		}
		if (start) {
			text += ":00";
		} else {
			text += ":59";
		}

		$("#" + id).append("<option value='" + i + "'>" + text + "</option>");
	}
}

function getInitTreeNodes() {
	return getDefaultGroupTree(group_type_device);
}

function onExpandTreeNode(treeId, node) {
	onExpandGroupTreeNode(group_type_device,treeId, node);
}

function onClickTreeNode(treeId, node) {
	queryCarByGroup(node, 1, 100);
}

function queryCarByGroup(node, pageNo, pageSize) {
	if(pageNo=1){
		$('#multiple-select-source').empty();
	}
	var url = management_api_server_servlet_path
			+ "/common/query/device.json?select=id,name&countable=false&pageSize="
			+ pageSize + "&pageNo=" + pageNo + "&orderBy=createTime&desc=true";
	var data = {};
	if(node.type=="ent"){
		data["enterpriseId.eq"] = node.id;
	}else{
		data["groupId.eq"] = node.id;
	}
	data["state.gte"] = 2;
	data["state.lte"] = 3;
	var result = ajaxSyncPost(url, data);
	if (result.code != 0) {
		showErrorMessage(result.message);
	} else {
		var list = result.data;
		if (list && list.length > 0) {
			var map = new JsMap();
			for (var i = 0; i < list.length; i++) {
				var item = list[i];
				map.put(item[0], item[1]);
			}

			$("#multiple-select-source option").map(function() {
				var id = $(this).val();
				var name = $(this).text();
				map.put(id, name);
			});

			var keys = map.getKeys();
			for (var i = 0, l = keys.length; i < l; i++) {
				var key = keys[i];
				$("#multiple-select-source").append("<option value='" + key + "'>" + map.get(key) + "</option>");
			}

			if (list.length == pageSize) {
				pageNo++;
				queryCarByGroup(node, pageNo, pageSize)
			}
		}
	}
}

function showInfoTap(index, formId) {
	$(".tab_con_div", "#" + formId).eq(index).show().siblings().hide();
}

///****************普通围栏告警 start***************
function loadRailData() {
	var url = management_api_server_servlet_path
			+ "/common/query/labelPoint.json?select=id,name&countable=true&pageSize=1000&pageNo=1&orderBy=createTime&desc=true";
	var data = {};
	data["bizType.eq"] = 5;
	ajaxAsyncPost(url, data,
			function(result) {
				if (result.code != 0) {
					showErrorMessage(result.message);
					return;
				}
				var rows = result.data.rows;
				$("#rail-setting-dlg-frm-rail").empty();
				for (var i = 0; i < rows.length; i++) {
					var item = rows[i];
					$("#rail-setting-dlg-frm-rail").append(
							"<option value='" + item[0] + "'>" + item[1]
									+ "</option>");
				}
			});
}

function saveRailAlarmSetting() {
	var formId = "rail-setting-dlg-frm";
	if (!validForm(formId)) {
		return;
	}
	var data = $("#" + formId).serializeObject();
	var setting = {};
	setting.inside = data.inside;
	setting.startHour = data.startHour;
	setting.endHour = data.endHour;

	if (data.startHour > data.endHour) {
		showMessage("结束时段必须大于开始开始时段！");
		return;
	}
	if (data.week.length > 0) {
		setting.week = data.week;
	} else {
		showMessage("请正确设置有效星期");
		return;
	}
	if (data.inside == "2") {
		if (!data.speed) {
			showMessage("请设置限速值");
			return;
		} else {
			setting.speed = data.speed;
		}
	}
	var labelPointId = $("#rail-setting-dlg-frm-rail").val();
	var url = management_api_server_servlet_path + "/common/labelPoint/" + labelPointId + ".json";
	var result = ajaxSyncGet(url, {});
	if (result.code != 0) {
		showErrorMessage(result.message);
		return;
	}
	var labelPoint = result.data;
	var labelPointSetting = $.evalJSON(labelPoint.setting);
	if (labelPoint.type == 2) {// 圆形
		setting.radius = labelPointSetting.radius;
		setting.lat = labelPointSetting.lat;
		setting.lng = labelPointSetting.lng;
		setting.shape = 1;
	} else if (labelPoint.type == 3) {// 矩形区域
		setting.shape = 2;
		setting.path = labelPointSetting.path;
	} else if (labelPoint.type == 4) {// 多边形
		setting.shape = 3;
		setting.path = labelPointSetting.path;
	}
	data.type = 2;
	data.setting = $.toJSON(setting);
	saveAlarmSetting(data);
}

// 区域告警**********************start******************************
function searchAlarmDistrict(object) {
	var arr = $(object).val().split(",");
	var district = mapUtil.getDistrictSearch();
	district.setLevel(arr[1]); // 行政区级别
	district.setExtensions("all");
	// 行政区查询
	// 按照adcode进行查询可以保证数据返回的唯一性
	district.search(arr[0], function(status, result) {
		if (status === 'complete') {
			setDistrictRailSearchResult(result);
		}
	});
}

function setDistrictRailSearchResult(result) {
	var district = result.districtList[0];
	var boundaries = district.boundaries;
	if (boundaries) {
		var bounds = [];
		for (var i = 0, l = boundaries.length; i < l; i++) {
			var points = boundaries[i];
			var path = "";
			for (var j = 0; j < points.length; j++) {
				var point = points[j];
				var wgs84 = LngLatConverter.gcj02towgs84(point.getLng(), point
						.getLat());
				path += ";" + wgs84[0] + "," + wgs84[1];
			}
			if (path) {
				bounds.push(path.substr(1));
			}
		}
		district_alarm_setting_dlg_frm_bounds = bounds;
	}
	district_alarm_setting_dlg_frm_areaCode = district.adcode;
	district_alarm_setting_dlg_frm_level = district.level;
	var subList = district.districtList;
	if (subList) {
		var level = district.level;
		var select = $("#district-alarm-setting-dlg-frm-province");
		// 清空下一级别的下拉列表
		if (level === 'country') {

		} else if (level === 'province') {
			$("#district-alarm-setting-dlg-frm-city").empty();
			$("#district-alarm-setting-dlg-frm-district").empty();
			select = $("#district-alarm-setting-dlg-frm-city");
		} else if (level === 'city') {
			$("#district-alarm-setting-dlg-frm-district").empty();
			select = $("#district-alarm-setting-dlg-frm-district");
		} else if (level === 'district') {
			return;
		}
		select.append("<option value=''>--请选择--</option>");
		for (var i = 0, l = subList.length; i < l; i++) {
			var item = subList[i];
			select.append("<option value='" + item.adcode + "," + item.level
					+ "'>" + item.name + "</option>");
		}
	}
}

function saveDistrictAlarmSetting() {
	var formId = "district-alarm-setting-dlg-frm";
	if (!validForm(formId)) {
		return;
	}

	var data = $("#" + formId).serializeObject();
	var setting = {};
	setting.shape = data.shape;
	setting.inside = data.inside;
	if (!district_alarm_setting_dlg_frm_bounds) {
		showMessage("请行政区域");
		return;
	}

	var tolerance = 500;
	if (district_alarm_setting_dlg_frm_level == "province") {
		tolerance = 2000;
	} else if (district_alarm_setting_dlg_frm_level == "city") {
		tolerance = 1000;
	}

	var url = lbs_api_server_servlet_path + "/gps/district/simple.json";
	// 抽稀算法，省2000米，市1000米，区500米
	var result = ajaxSyncPost(url, {
		"bounds" : district_alarm_setting_dlg_frm_bounds,
		"tolerance" : tolerance
	});
	if (result.code != 0) {
		showErrorMessage(result.message);
		return;
	}
	setting.bounds = result.data;
	setting.startHour = data.startHour;
	setting.endHour = data.endHour;
	setting.shape = 4;
	if (data.startHour > data.endHour) {
		showMessage("结束时段必须大于开始开始时段！");
		return;
	}
	if (data.week.length > 0) {
		setting.week = data.week;
	} else {
		showMessage("请正确设置有效星期");
		return;
	}
	if (data.inside == "2") {
		if (!data.speed) {
			showMessage("请设置限速值");
			return;
		} else {
			setting.speed = data.speed;
		}
	}
	data.type = 2;

	var url = management_api_server_servlet_path
			+ "/system/dictionary/districtSearchData.json?select=centerLng,centerLat,maxRadius";
	var result = ajaxSyncPost(url, {
		"code.eq" : district_alarm_setting_dlg_frm_areaCode
	});
	if (result.code != 0) {
		showErrorMessage(result.message);
		return;
	}
	if (result.data && result.data.length > 0) {
		var item = result.data[0];
		var center = {};
		center.lng = item[0];
		center.lat = item[1];
		center.radius = item[2];
		setting.center = center;
	}

	data.setting = $.toJSON(setting);
	saveAlarmSetting(data);
	district_alarm_setting_dlg_frm_bounds = null;
}

///**********超速告警start*************
function saveOverspeedAlarmSetting(){
	var formId = "overspeed-alarm-setting-dlg-frm";
	if (!validForm(formId)) {
		return;
	}

	var data = $("#" + formId).serializeObject();
	var setting = {};
	setting.speed=data.speed;
	setting.interval=data.interval;
	setting.startHour=data.startHour;
	setting.endHour=data.endHour;
	
	if(data.startHour > data.endHour){
		showMessage("结束时段必须大于开始开始时段！");
		return;
	}
	data.setting = $.toJSON(setting);
	saveAlarmSetting(data);
}


///*******************路线告警 start************
function loadRouteData(){
	var url = management_api_server_servlet_path+"/common/query/labelPoint.json?select=id,name&countable=true&pageSize=1000&pageNo=1&orderBy=createTime&desc=true";
	var data = {};
	data["bizType.eq"]=6;
	ajaxAsyncPost(url,data,function(result){
		if (result.code!=0) {
			showErrorMessage(result.message);
			return;
		}
		var rows = result.data.rows;
		$("#route-setting-dlg-frm-route").empty(); 
		for(var i=0;i<rows.length;i++){
			var item = rows[i];
			$("#route-setting-dlg-frm-route").append("<option value='"+item[0]+"'>"+item[1]+"</option>");
		}
	});
}

function saveRouteAlarmSetting(){
	var formId = "route-setting-dlg-frm";
	if (!validForm(formId)) {
		return;
	}
	var data = $("#" + formId).serializeObject();
	
	var setting={};
	setting.startHour=data.startHour;
	setting.endHour=data.endHour;
	setting.inside=data.inside;
	if(data.startHour > data.endHour){
		showMessage("结束时段必须大于开始开始时段！");
		return;
	}
	if(data.inside=="2" && data.speed==""){
		showMessage("请设置限速值");
		return;
	}
	
	if(data.week.length>0){
		setting.week=data.week;
	}else{
		showMessage("请正确设置有效星期");
		return;
	}
	if(data.inside=="2"){
		setting.speed=data.speed;
	}

	var labelPointId = $("#route-setting-dlg-frm-route").val();
	var url=management_api_server_servlet_path+"/common/labelPoint/"+labelPointId+".json";
	var result = ajaxSyncGet(url,{});
	if (result.code!=0) {
		showErrorMessage(result.message);
		return;
	}
	var labelPoint = result.data;
	var labelPointSetting = $.evalJSON(labelPoint.setting);
	setting.route = labelPointSetting.route;
	setting.width = labelPointSetting.width;
	setting.maxDistance = labelPointSetting.maxDistance;
	
	data.setting = $.toJSON(setting);
	saveAlarmSetting(data);
}

function saveAlarmSetting(data){
	var url=management_api_server_servlet_path+"/common/deviceAlarmSetting.json";
	var deviceIds = "";
	$("#multiple-selected-target option").map(function() {
		deviceIds += "," + $(this).val();
	});
	data.deviceId = deviceIds.substr(1);
	var result = ajaxSyncPost(url, data);
	if (result.code != 0) {
		showErrorMessage(result.message);
	}else{
		parent.doQuery();
		showMessage("设置成功");
		parent.closeIframe();
	}
}