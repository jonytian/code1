$(document).ready(function() {
	initLimitDatetimepicker("send-set-rail-circle-cmd-dlg-frm-startDateTime","send-set-rail-circle-cmd-dlg-frm-endDateTime",new Date(),"",0.5);
	initLimitDatetimepicker("send-set-rail-rectangle-cmd-dlg-frm-startDateTime","send-set-rail-rectangle-cmd-dlg-frm-endDateTime",new Date(),"",0.5);
	initLimitDatetimepicker("send-set-rail-polygon-cmd-dlg-frm-startDateTime","send-set-rail-polygon-cmd-dlg-frm-endDateTime",new Date(),"",0.5);
	//midified by liliang 20200304
  initLimitDatetimepicker("send-set-rail-route-cmd-dlg-frm-startDateTime","send-set-rail-route-cmd-dlg-frm-endDateTime",new Date(),"",0.5);
	
	initRailDateTimeAttributeEvent("circle");
	initRailTimeAttributeEvent("circle");
	
	initRailDateTimeAttributeEvent("rectangle");
	initRailTimeAttributeEvent("rectangle");
	
	initRailDateTimeAttributeEvent("polygon");
	initRailTimeAttributeEvent("polygon");
	
	initRailDateTimeAttributeEvent("route");
	initRailTimeAttributeEvent("route");
	
	initRailLimitedSpeedAttributeEvent("circle","send-set-rail-circle-cmd-dlg-frm");
	initRailLimitedSpeedAttributeEvent("rectangle","send-set-rail-rectangle-cmd-dlg-frm");
	initRailLimitedSpeedAttributeEvent("polygon","send-set-rail-polygon-cmd-dlg-frm");
});

function initRailDateTimeAttributeEvent(type){
	$("input:checkbox[name='"+type+"DateTimeAttribute']").change(function(){
		if($(this).is(":checked")){
			showRailDateTimeAttribute(type,true);
			showRailTimeAttribute(type,false);
			$("input[type=checkbox][name="+type+"TimeAttribute]").prop("checked",false);
		}else{
			showRailDateTimeAttribute(type,false);
		}
	});
}

function initRailTimeAttributeEvent(type){
	$("input:checkbox[name='"+type+"TimeAttribute']").change(function(){
		if($(this).is(":checked")){
			showRailDateTimeAttribute(type,false);
			showRailTimeAttribute(type,true);
			$("input[type=checkbox][name="+type+"DateTimeAttribute]").prop("checked",false);
		}else{
			showRailTimeAttribute(type,false);
		}
	});
}

function showRailDateTimeAttribute(type,flag){
	if(flag){
		$("#send-set-rail-"+type+"-start-end-datetime-div").show();
		$("#send-set-rail-"+type+"-cmd-dlg-frm-startDateTime").addClass("required");
		$("#send-set-rail-"+type+"-cmd-dlg-frm-endDateTime").addClass("required");
	}else{
		$("#send-set-rail-"+type+"-start-end-datetime-div").hide();
		$("#send-set-rail-"+type+"-cmd-dlg-frm-startDateTime").removeClass("required");
		$("#send-set-rail-"+type+"-cmd-dlg-frm-endDateTime").removeClass("required");
	}
}

function showRailTimeAttribute(type,flag){
	if(flag){
		$("#send-set-rail-"+type+"-start-end-time-div").show();
		$("#send-set-rail-"+type+"-cmd-dlg-frm-startTime").addClass("required");
		$("#send-set-rail-"+type+"-cmd-dlg-frm-endTime").addClass("required");
	}else{
		$("#send-set-rail-"+type+"-start-end-time-div").hide();
		$("#send-set-rail-"+type+"-cmd-dlg-frm-startTime").removeClass("required");
		$("#send-set-rail-"+type+"-cmd-dlg-frm-endTime").removeClass("required");
	}
}

function initRailLimitedSpeedAttributeEvent(type,formId){
	$("input:checkbox[name='attribute']","#" + formId).change(function(){
		var bool = false;
		$("input:checkbox[name='attribute']","#" + formId).each(function(){
            if($(this).is(":checked") && $(this).val()==1){
            	bool = true;
            }
        });
	
		if(bool){
			$("#send-set-rail-"+type+"-limited-speed-div").show();
			$("#send-set-rail-"+type+"-cmd-dlg-frm-limitedSpeed").addClass("required");
			$("#send-set-rail-"+type+"-cmd-dlg-frm-durationTime").addClass("required");
		}else{
			$("#send-set-rail-"+type+"-limited-speed-div").hide();
			$("#send-set-rail-"+type+"-cmd-dlg-frm-limitedSpeed").removeClass("required");
			$("#send-set-rail-"+type+"-cmd-dlg-frm-durationTime").removeClass("required");
		}
	});
}

function showSetFenceCmdDialog(type,bizType,dialogId,fieldId){
	initRailLabelPointSetting(dialogId+"-frm-rail",type,bizType);
	showCmdDialog(dialogId);
}

function showRouteFenceCmdDialog(){
	initRouteFenceSetting();
	showCmdDialog("send-set-rail-route-cmd-dlg");
}

function showDelFenceCmdDialog(bizId,dialogId){
	initFenceSetting(current_selected_device.id,dialogId+"-frm-areaId",bizId);
	showCmdDialog(dialogId);
}

function showRouteSettingNext(){
	var formId = "send-set-rail-route-cmd-dlg-frm";
	if (!validForm(formId)) {
		return;
	}
	$(".railRouteCmd", "#" + formId).eq(1).show().siblings().hide();
}

function showRouteSettingPrev(){
	var formId = "send-set-rail-route-cmd-dlg-frm";
	$(".railRouteCmd", "#" + formId).eq(0).show().siblings().hide();
}

function sendSetRouteRailCmd(){
	var formData = $("#send-set-rail-route-cmd-dlg-frm").serializeObject();
	var attribute = "";
	if(formData.routeDateTimeAttribute){
		formData.startTime=formData.startDateTime;
		formData.endTime=formData.endDateTime;
		attribute="1";
	}else if(formData.routeTimeAttribute){
		formData.startTime="0000-00-00 "+formData.startTime;
		formData.endTime="0000-00-00 "+formData.endTime;
		attribute="1";
	}else{
		attribute="0";
	}
	
	for(var i=1;i<16;i++){
		var bool = false;
		for(var j=0;j<formData.attribute.length;j++){
			if(i==parseInt(formData.attribute[j])){
				bool = true;
				break;
			}
		}
		if(bool){
			attribute="1"+attribute;
		}else{
			attribute="0"+attribute;
		}
	}
	formData.attribute=attribute;
	
	var deviceId = formData.deviceId;
	var messageId = formData.messageId;
	formData.routeId=getRailRoadId(deviceId,messageId);
	
	var routeIds = [];
	$("#multiple-selected-target option").map(function() {
		routeIds.push($(this).val());
	});
	if(routeIds.length==0){
		showErrorMessage("请选择路线！");
		return;
	}

	var inflectionPointList = [];
	for(var i=0;i<routeIds.length;i++){
		var routeId = routeIds[i];
		var routeInfo = getRouteById(routeId);
		var roadAttribute = routeInfo.attribute.toString(2);
		while(roadAttribute.length<8){
			roadAttribute="0"+roadAttribute;
		}
		var points = (routeInfo.path).split(";");
		for(var j=0,l=points.length;j<l;j++){
			var point = points[i].split(",");
			var route = {};
			route.pointId=(i*j)+1;
			route.roadId=(i+1);
			route.lng=parseFloat(point[0]);
			route.lat=parseFloat(point[1]);
			route.roadWidth=routeInfo.width;
			route.roadAttribute=roadAttribute;
			route.ltTravelTime=routeInfo.itTravelTime;
			route.gtTravelTime=routeInfo.gtTravelTime;
			route.limitedSpeed=routeInfo.maxSpeed;
			route.durationTime=routeInfo.durationTime;
			inflectionPointList.push(route);
		}
	}
	formData.inflectionPointList=inflectionPointList;
	doSendFenceCmd(deviceId,messageId,formData);
}

function getRouteById(id){
	var url = "/common/route/" + id + ".json";
	var result = ajaxSyncGet(url, {});
	if (result.code != 0) {
		showErrorMessage(result.message);
		return null;
	} else {
		return result.data;
	}
}

function initRouteFenceSetting(){
	var url = "/common/query/route.json?countable=false&select=id,name&pageNo=1&pageSize=1000";
	var params = {};
	$('#multiple-select-source').empty();
	$('#multiple-selected-target').empty();
	ajaxAsyncPost(url, params, function(result) {
		if (result.code != 0) {
			showErrorMessage(result.message);
		} else {
			var list = result.data;
			for (var i = 0; i < list.length; i++) {
				var item = list[i];
				$("#multiple-select-source").append("<option value='" + item[0] + "'>" + item[1] + "</option>");
			}
		}
	});
}

function initRailLabelPointSetting(objId,type,bizType){
	var url = management_api_server_servlet_path+"/common/query/labelPoint.json?select=id,name&countable=false&pageSize=1000&pageNo=1&orderBy=createTime&desc=true";
	var data = {};
	data["bizType.eq"]=bizType;
	data["type.eq"]=type;
	ajaxAsyncPost(url,data,function(result){
		if (result.code!=0) {
			showErrorMessage(result.message);
			return;
		}
		var rows = result.data;
		$("#"+objId).empty(); 
		for(var i=0;i<rows.length;i++){
			var item = rows[i];
			$("#"+objId).append("<option value='"+item[0]+"'>"+item[1]+"</option>");
		}
	});
}

function initFenceSetting(deviceId,objId,bizId){
	var url = management_api_server_servlet_path+"/common/query/deviceAlarmSetting.json?select=id,name&countable=false&pageSize=1000&pageNo=1&orderBy=createTime&desc=true";
	var data = {};
	data["bizType.eq"]=2;
	data["bizId.eq"]=bizId;
	data["deviceId.eq"]=deviceId;
	ajaxAsyncPost(url,data,function(result){
		if (result.code!=0) {
			showErrorMessage(result.message);
			return;
		}
		var rows = result.data;
		$("#"+objId).empty(); 
		$("#"+objId).append("<option value='-1'>全部区域</option>");
		for(var i=0;i<rows.length;i++){
			var item = rows[i];
			$("#"+objId).append("<option value='"+item[0]+"'>"+item[1]+"</option>");
		}
	});
}

function sendSetCircleRailCmd(){
	var form = $("#" + current_dialog_id + "-frm");
	if (!form.valid()) {
		return;
	}
	var formData = form.serializeObject();
	var deviceId = formData.deviceId;
	var messageId = formData.messageId;
	var labelPointId = formData.labelPointId;
	var labelPoint = getDeviceLabelPoint(labelPointId);
	var data={};
	//formData.name = labelPoint.name;
	var setting = labelPoint.setting;
	var area = {};
	area.centerLng = setting.lng;
	area.centerLat = setting.lat;
	area.radius = setting.radius;
	
	var attribute = "";
	if(formData.circleDateTimeAttribute){
		formData.startTime=formData.startDateTime;
		formData.endTime=formData.endDateTime;
		attribute="1";
	}else if(formData.circleTimeAttribute){
		formData.startTime="0000-00-00 "+formData.startTime;
		formData.endTime="0000-00-00 "+formData.endTime;
		attribute="1";
	}else{
		attribute="0";
	}
	
	for(var i=1;i<16;i++){
		 var bool =false;
		for(var j=0;j<formData.attribute.length;j++){
			if(i==formData.attribute[j]){
				bool = true;
				break;
			}
		}
		if(bool){
			attribute="1"+attribute;
		}else{
			attribute="0"+attribute;
		}
	}

	area.attribute = attribute;
	area.areaId = getRailAreaId(deviceId,messageId);
	area.limitedSpeed = parseInt(formData.limitedSpeed);
	area.durationTime = parseInt(formData.durationTime);
	area.startTime = formData.startTime;
	area.endTime = formData.endTime;
	area.name = formData.name;
	data.type = 1;
	var areaList = [];
	areaList[0] = area;
	data.areaList = areaList;
	doSendFenceCmd(deviceId,messageId,data);
}

function sendDelRailCmd(){
	var form = $("#"+current_dialog_id+"-frm").serializeObject();
	var deviceId = form.deviceId;
	var messageId=form.messageId;
	var areaId = form.areaId;
	doSendFenceDelCmd(deviceId,messageId,areaId);
}

function sendSetRectangleRailCmd(){
	var form = $("#" + current_dialog_id + "-frm");
	if (!form.valid()) {
		return;
	}
	var formData = form.serializeObject();
	
	var deviceId = formData.deviceId;
	var messageId = formData.messageId;
	var labelPointId = formData.labelPointId;
	var labelPoint = getDeviceLabelPoint(labelPointId);
	var data={};
	//formData.name = labelPoint.name;
	var setting = labelPoint.setting;
	var area = {};
	var path = setting.path.split(";");
	var arr = path[0].split(",");
	area.topLeftLng = parseFloat(arr[0]);
	area.topLeftLat = parseFloat(arr[1]);
	arr = path[3].split(",");
	area.bottomRightLng = parseFloat(arr[0]);
	area.bottomRightLat = parseFloat(arr[1]);
	
	var attribute = "";
	if(formData.rectangleDateTimeAttribute){
		formData.startTime=formData.startDateTime;
		formData.endTime=formData.endDateTime;
		attribute="1";
	}else if(formData.rectangleTimeAttribute){
		formData.startTime="0000-00-00 "+formData.startTime;
		formData.endTime="0000-00-00 "+formData.endTime;
		attribute="1";
	}else{
		attribute="0";
	}
	
	for(var i=1;i<16;i++){
		 var bool =false;
		for(var j=0;j<formData.attribute.length;j++){
			if(i==formData.attribute[j]){
				bool = true;
				break;
			}
		}
		if(bool){
			attribute="1"+attribute;
		}else{
			attribute="0"+attribute;
		}
	}

	area.attribute = attribute;
	area.areaId = getRailAreaId(deviceId,messageId);
	area.limitedSpeed = parseInt(formData.limitedSpeed);
	area.durationTime = parseInt(formData.durationTime);
	area.startTime = formData.startTime;
	area.endTime = formData.endTime;
	area.name = formData.name;
	data.type = 1;
	var areaList = [];
	areaList[0] = area;
	data.areaList = areaList;
	doSendFenceCmd(deviceId,messageId,data);
}

function sendSetPolygonRailCmd(){
	var form = $("#" + current_dialog_id + "-frm");
	if (!form.valid()) {
		return;
	}
	var formData = form.serializeObject();
	
	var deviceId = formData.deviceId;
	var messageId= formData.messageId;
	var labelPointId = formData.labelPointId;
	var labelPoint = getDeviceLabelPoint(labelPointId);

	var setting = labelPoint.setting;
	var path = setting.path.split(";");
	var vertexList = [];
	for(var i=0,l=path.length;i<l;i++){
		var arr = path[i].split(",");
		var point = {};
		point.lng = parseFloat(arr[0]);
		point.lat = parseFloat(arr[1]);
		vertexList.push(point);
	}

	var attribute = "";
	if(formData.polygonDateTimeAttribute){
		formData.startTime=formData.startDateTime;
		formData.endTime=formData.endDateTime;
		attribute="1";
	}else if(formData.polygonTimeAttribute){
		formData.startTime="0000-00-00 "+formData.startTime;
		formData.endTime="0000-00-00 "+formData.endTime;
		attribute="1";
	}else{
		attribute="0";
	}
	
	for(var i=1;i<16;i++){
		 var bool =false;
		for(var j=0;j<formData.attribute.length;j++){
			if(i==formData.attribute[j]){
				bool = true;
				break;
			}
		}
		if(bool){
			attribute="1"+attribute;
		}else{
			attribute="0"+attribute;
		}
	}
	var data={};
	data.attribute = attribute;
	data.areaId = getRailAreaId(deviceId,messageId);
	data.limitedSpeed = parseInt(formData.limitedSpeed);
	data.durationTime = parseInt(formData.durationTime);
	data.startTime = formData.startTime;
	data.endTime = formData.endTime;
	data.type = 1;
	data.vertexList = vertexList;
	data.name = formData.name;
	doSendFenceCmd(deviceId,messageId,data);
}

function getDeviceLabelPoint(labelPointId){
	var url=management_api_server_servlet_path+"/common/labelPoint/"+labelPointId+".json";
	var result = ajaxSyncGet(url,{});
	if (result.code!=0) {
		showErrorMessage(result.message);
		return;
	}
	var labelPoint = result.data;
	var labelPointSetting = JSON.parse(labelPoint.setting);
	labelPoint.setting = labelPointSetting;
	return labelPoint;
}

function getRailAreaId(deviceId,messageId){
	var url = message_api_server_servlet_path+"/common/query/deviceDownMessage.json?countable=false&pageSize=1&pageNo=1&orderBy=createTime&desc=true";
	var data = {};
	data["deviceId.eq"]=deviceId;
	data["messageId.eq"]=messageId;
	var result = ajaxSyncPost(url,data);
	if (result.code!=0) {
		showErrorMessage(result.message);
		return null;
	}
	if(result.data && result.data.length > 0 ){
		var item = result.data[0];
		var messageBody = JSON.parse(item.messageBody);
		var areaList = messageBody.areaList;
		if(areaList){
			var areaId = areaList[areaList.length-1].areaId + 1;
			if(areaId > 65530 ){
				areaId = 1; 
			}
			return areaId;
		}
	}
	return 1;
}

function getRailRoadId(deviceId,messageId){
	var url = message_api_server_servlet_path+"/common/query/deviceDownMessage.json?countable=false&pageSize=1&pageNo=1&orderBy=createTime&desc=true";
	var data = {};
	data["deviceId.eq"]=deviceId;
	data["messageId.eq"]=messageId;
	var result = ajaxSyncPost(url,data);
	if (result.code!=0) {
		showErrorMessage(result.message);
		return null;
	}
	if(result.data && result.data.length > 0 ){
		var item = result.data[0];
		var messageBody = JSON.parse(item.messageBody);
		var routeId = messageBody.routeId;
		if(routeId){
			var routeId = routeId + 1;
			if(routeId > 65530 ){
				routeId = 1; 
			}
			return routeId;
		}
	}
	return 1;
}

function doSendFenceCmd(deviceId, messageId,data) {
	if (!isOnline(deviceId)) {
		showErrorMessage("车辆不在线，不能发送指令！");
	}else{
		var url = message_api_server_servlet_path + "/deviceDownMessage/fence/" + deviceId + "/" + messageId + ".json";
		var result = ajaxSyncPost(url, data,true);
		if (result.code != 0) {
			showErrorMessage(result.message);
		}else{
			showMessage("指令已下发！");
			closeDialog();
			return result.data;
		}
	}
	return null;
}

function doSendFenceDelCmd(deviceId, messageId,areaId){
	if (!isOnline(deviceId)) {
		showErrorMessage("车辆不在线，不能发送指令！");
	}else{
		var url = message_api_server_servlet_path + "/deviceDownMessage/fence/"+deviceId+"/"+messageId+"/" + areaId + ".json";
		ajaxAsyncDel(url, {}, function(result) {
			if (result.code != 0) {
				showErrorMessage(result.message);
			} else {
				showMessage("指令已下发！");
				closeDialog();
				return result.data;
			}
		},true);
	}
}