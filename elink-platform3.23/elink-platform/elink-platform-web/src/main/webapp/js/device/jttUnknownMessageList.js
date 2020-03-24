$(function() {
	initDatepicker();
	$('#search-date').val(new Date().format("yyyy-MM-dd"));
	initBootstrapTable();
});

function getApiName() {
	return "jttUnknownMessage";
}

function getIdField(){
	return "_id";
}

function getQueryParams() {
	var params = {};
	params["recordDate"]=$('#search-date').val().replace(/-/g,"");

	var conditions = {};
	var deviceId = $("#search-deviceId").val();
	if (query && deviceId) {
		conditions["deviceId.eq"] = deviceId;
	}

	var messageId = $("#search-messageId").val();
	if(messageId){
		conditions["messageId.eq"] = messageId;
	}
	params["conditions"] = conditions;
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
			if(row.deviceId){
				return getDeviceName(row.deviceId)||row.simCode;
			}
			return row.simCode;
		}
	}, {
		field : "messageId",
		title : "消息ID"
	}, {
		field : "messageSeq",
		title : "消息流水号"
	}, {
		field : "desc",
		title : "描述"
	}, {
		field : "createTime",
		title : "消息时间",
		formatter : function(value, row, index) {
			return new Date(row.createTime).format();
		}
	},{
		title : '操作',
		field : 'opear',
		formatter : function(value, row) {
			var html = '<a   href="javascript:void(0)" class="table_view" title="查看" onclick="view(\'' + row._id + '\')">查看</a>';
			return html;
		}
	}  ];
}

function view(id){
	var row = getBootstrapTableRowById(id);
	var device = row.simCode;
	if(row.deviceId){
		device = getDeviceName(row.deviceId);
	}
	var messageBody = "";
	if(row.messageBody){
		messageBody = row.messageBody;
	}else{
		messageBody = $.toJSON(row);
	}
	$("#message-detail-dlg-frm-deviceId").val(device);
	$("#message-detail-dlg-frm-messageId").val(row.messageId);
	$("#message-detail-dlg-frm-messageBody").val(messageBody);
	showCommondDialog("message-detail-dlg");
}