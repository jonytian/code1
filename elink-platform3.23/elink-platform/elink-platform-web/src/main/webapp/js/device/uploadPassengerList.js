$(function() {
	initBootstrapTable();
});

function getApiName() {
	return "deviceUpMessage";
}

function getQueryParams() {
	var params = {};
	var deviceId = $("#search-deviceId").val();
	if (query && deviceId) {
		params["deviceId.eq"] = deviceId;
	}
	return params;
}

function getQueryUrl() {
	return management_api_server_servlet_path + "/common/query/" + getApiName()
		+ ".json?countable=true";
}

function getColumns() {
	return [ {
		field : 'check',
		checkbox : true
	}, {
		field : "deviceName",
		title : "设备名称",
		formatter : function(value, row, index) {
			var deviceId = row.deviceId;
			return getDeviceName(deviceId);
		}
	}, {
		field : "startTime",
		title : "起始时间"
	},{
		field : "endTime",
		title : "结束时间"
	},{
		field : "getOn",
		title : "上车人数"
	},{
		field : "getOff",
		title : "下车人数"
	}
	];
}

