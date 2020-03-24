$(function() {
	initBootstrapTable();
});

function getApiName() {
	return "deviceDataLimitAlarm";
}

function getQueryParams() {
	var params = {};
	return params;
}

function getQueryUrl() {
	return management_api_server_servlet_path + "/common/query/" + getApiName() + ".json?countable=true";
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
			if(deviceId){
				return getDeviceName(deviceId);
			}
			return "";
		}
	}, {
		field : "ip",
		title : "IP"
	}, {
		field : "type",
		title : "告警类型",
		formatter : function(value, row, index) {
			var type = row.type;
			if (type == 1) {
				return "上传数据流量超额";
			} else if (type == 2) {
				return "上行消息频率超额";
			}
		}
	}, {
		field : "alarmTime",
		title : "告警时间"
	}, {
		field : "createTime",
		title : "创建时间"
	} ];
}