$(function() {
	initDatepicker();
	$('#search-date').val(new Date().format("yyyy-MM-dd"));
	initBootstrapTable();
});

function getQueryParams() {
	var params = {};
	var date = $('#search-date').val();
	var params = {
		"recordDate" : date.replace(/-/g, "")
	}
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
			+ "/common/query/jtt0702Message.json?countable=true";
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
		field : "driverName",
		title : "司机姓名"
	}, {
		field : "qualification",
		title : "从业资格证",
	}, {
		field : "icCardState",
		title : "考勤类型",
		formatter : function(value, row, index) {
			var icCardState = row.icCardState;
			if (icCardState == 1) {
				return "上班";
			} else {
				return "下班";
			}
		}
	}, {
		field : "icCardOptTime",
		title : "考勤时间"
	} ];
}