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
		field : "terminalType",
		title : "终端类型"
	},{
		field : "manufacturersId",
		title : "制造商ID"
	},{
		field : "terminalTypeNumber",
		title : "终端型号"
	},{
		field : "terminalId",
		title : "终端ID"
	},{
		field : "iccid",
		title : "终端SIM卡ICCID"
	},{
		field : "hardwareVersionNumberLength",
		title : "终端硬件版本号长度"
	},{
		field : "hardwareVersionNumber",
		title : "终端硬件版本号"
	},{
		field : "firmwareVersionNumberLength",
		title : "终端固件版本号长度"
	},{
		field : "firmwareVersionNumber",
		title : "终端固件版本号"
	},{
		field : "gnssModuleAttribute",
		title : "GNSS模块属性"
	},{
		field : "communicationModuleAttribute",
		title : "通信模块属性"
	}
	];
}

