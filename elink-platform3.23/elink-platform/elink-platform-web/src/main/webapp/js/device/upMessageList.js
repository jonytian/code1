$(function() {
	initBootstrapTable();
});

function getApiName() {
	return "deviceUpMessage";
}

function getQueryParams() {
	var params = {};
	var messageId = $("#search-messageId").val();
	if(messageId!=""){
		params["messageId.eq"] = messageId;
	}
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
		field : "messageDesc",
		title : "指令",
		formatter : function(value, row, index) {
			return deviceMessageInfoMap.get(row.messageId);
		}
	}, {
		field : "messageId",
		title : "消息ID"
	}, {
		field : "messageSeq",
		title : "消息流水号"
	}, {
		field : "desc",
		title : "描述",
		formatter : function(value, row, index) {
			var messageBody = row.messageBody;
			if (messageBody) {
				var data = $.evalJSON(messageBody);
				if (data.desc) {
					return data.desc;
				}
			}
			return "";
		}
	}, {
		field : "createTime",
		title : "消息时间"
	}, {
		title : '操作',
		field : 'opear',
		formatter : function(value, row) {
			var html = '<a   href="javascript:void(0)" class="table_view" title="查看" onclick="view(\''
					+ row.id + '\')">查看</a>';
			return html;
		}
	}  ];
}

function view(id){
	var row = getBootstrapTableRowById(id);
	$("#message-detail-dlg-frm-deviceId").val(getDeviceName(row.deviceId));
	$("#message-detail-dlg-frm-messageId").val(row.messageId);
	$("#message-detail-dlg-frm-messageBody").val(row.messageBody);
	showCommondDialog("message-detail-dlg");
}