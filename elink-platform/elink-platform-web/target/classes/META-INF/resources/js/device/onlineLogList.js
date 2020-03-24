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
	+ "/common/query/onlineLog.json?countable=true";
}

function getColumns() {
	return [ {
		field : 'check',
		checkbox : true
	},{
		field : "deviceName",
		title : "设备名称",
		formatter : function(value, row, index) {
			var deviceId = row.deviceId;
			return getDeviceName(deviceId);
		}
	}, {field:"state",title:"状态",formatter: function(value,row,index){
		 var state=row.state;
		 if(state==0){
			 return "下线";
		 }
		 return "上线";
	 }}, {
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