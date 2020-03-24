$(function() {
	initBootstrapTable();
});

function getApiName() {
	return "deviceDataLimitAlarm";
}

function getQueryParams() {
	var params = {};
	params["state.eq"]=0;
	params["alarmTime.gte"]=new Date().format("yyyy-MM-dd");
	return params;
}

function getQueryUrl() {
	return management_api_server_servlet_path + "/common/query/" + getApiName() + ".json?countable=true&isParent=true";
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
	}, {title: '操作',field: 'opear',formatter: function (value, row) {
        var  html = '<a   href="javascript:void(0)" class="table_del" title="解除" onclick="del(\'' + row.id + '\')">解除</a>';
        return html ;
	}}];
}

function getDelUrl(id) {
	return message_api_server_servlet_path + "/gateway/blacklist/" + id + ".json";
}