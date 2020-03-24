$(function() {
	initBootstrapTable();
});

function getQueryParams() {
	var params = {};
	var deviceId = $("#search-deviceId").val();
	if (query && deviceId) {
		params["conditions"] = {
			"deviceId.eq" : deviceId
		}
	}
	return params;
}

function getQueryUrl() {
	return message_api_server_servlet_path
			+ "/common/query/jtt0001Message.json?countable=true";
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
		field : "respMessageId",
		title : "应答消息ID",
	}, {
		field : "respMessageSeq",
		title : "应答消息流水号",
	}, {
		field : "result",
		title : "结果",
		formatter : function(value, row, index) {
			var result = row.result;
			if (result == 0) {
				return "终端处理成功";
			} else if (result == 1) {
				return "终端处理失败";
			} else if (result == 2) {
				return "消息有误";
			} else if (result == 3) {
				return "消息不支持";
			}
		}
	}, {
		field : "createTime",
		title : "消息时间",
		formatter : function(value, row, index) {
			var createTime = row.createTime;
			if (createTime == null) {
				return "";
			}
			return new Date(createTime).format();
		}
	} ];
}