$(function() {
	initDatepicker();
	$('#search-date').val(new Date().format("yyyy-MM-dd"));
	initBootstrapTable();
});

function getDefaultSort(){
	return "alarmTime";
}

function getQueryParams() {
	var params = {};
	var date = $('#search-date').val();
	var params = {
		"recordDate" : date.replace(/-/g, "")
	}
	var conditions = {"a49.eq":1};
	var deviceId = $("#search-deviceId").val();
	if (query && deviceId) {
		conditions["deviceId.eq"] = deviceId;
	}
	params["conditions"] = conditions;
	return params;
}

function getQueryUrl() {
	return message_api_server_servlet_path + "/common/query/alarm.json?countable=true";
}

function getColumns() {
	return [
			{
				field : 'check',
				checkbox : true
			},
			{field:"deviceName",title:"车牌号",formatter: function(value,row,index){ 
				 var deviceId=row.deviceId;
				 return getDeviceName(deviceId);
			 }},
			 {field:"currentOilmass",title:"当前油量",formatter: function(value,row,index){ 
				 var desc49=row.desc49;
				 if(desc49){
					 return desc49.currentOilmass;
				 }
				 return "-";
			 }},
	         {field:"lastOilmass",title:"上一次油量",formatter: function(value,row,index){ 
	        	 var desc49=row.desc49;
				 if(desc49){
					 return desc49.lastOilmass;
				 }
				 return "-";
			 }},
			 {field:"additionOilmass",title:"增加油量(L)",formatter: function(value,row,index){ 
	        	 var desc49=row.desc49;
				 if(desc49){
					 return (desc49.currentOilmass-desc49.lastOilmass).toFixed(2);
				 }
				 return 0;
			 }},
			 {field:"alarmTime",title:"记录时间",formatter: function(value,row,index){
				 var alarmTime=row.alarmTime;
				 if(alarmTime==null){
					 return "";
				 }
				 return new Date(alarmTime).format();
			 }},
			{
				title : '操作',
				field : 'opear',
				formatter : function(value, row) {
					var html = '<a   href="javascript:void(0)" class="table_view" title="查看" onclick="view(\''
							+ row.id + '\')">查看</a>';
					return html;
				}
			} ];
}

function getIdField(){
	return "id";
}

function view(id) {
	var row = getBootstrapTableRowById(id);
	var deviceId = row.deviceId;
	deviceName = getDeviceName(deviceId);
	var gpsId = row.gpsId;
	var data = {};
	data["recordDate"] = new Date(row.alarmTime).format("yyyyMMdd");
	var conditions = {};
	conditions["deviceId.eq"] = deviceId;
	conditions["id.eq"] = gpsId;
	data["conditions"] = conditions;

	var url = lbs_api_server_servlet_path + "/common/query/gps.json?select=lng,lat,speed&pageSize=1&pageNo=1";
	ajaxAsyncPost(url, data, function(result) {
		if (result.code != 0) {
			showErrorMessage(result.message);
			return;
		}
		var list = result.data;
		if (list && list.length > 0) {
			mapUtil.clearOverlays();
			var item = list[0];
			var gcj02 = LngLatConverter.wgs84togcj02(item.lng, item.lat);
			var lacation = mapUtil.getPoint(gcj02[0], gcj02[1]);
			var iconUrl = getCarDefaultIcon();
			mapUtil.addMarker(deviceId, lacation, deviceName, iconUrl, 60);
			mapUtil.regeocoder(lacation, function(address) {
				var text = "<b>" + deviceName + "</b> 速度：" + parseInt(item.speed) + "km/h";
				text += "<br/>时间：" + new Date(row.alarmTime).format();
				if (address) {
					text += "<br/>地址：" + address;
				}
				// 设置label标签
				text ="<div class='amap-info-window-content'>"+text+"</div>";
				mapUtil.addLabel(lacation, text);
			});
			showCommondDialog("map-dlg");
		} else {
			showMessage("无相关位置信息");
		}
	});
}