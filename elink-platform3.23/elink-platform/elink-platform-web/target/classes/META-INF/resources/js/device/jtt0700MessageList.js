$(function() {
	initBootstrapTable();
});

var commandWordInfoMap= new JsMap();
commandWordInfoMap.put("00","执行标准版本年号");
commandWordInfoMap.put("01","采集当前驾驶人信息");
commandWordInfoMap.put("02","采集实时时间");
commandWordInfoMap.put("03","采集累计行驶里程");
commandWordInfoMap.put("04","采集脉冲系数");
commandWordInfoMap.put("05","采集行驶速度数据/车辆信息");
commandWordInfoMap.put("06","采集车辆信息/状态信号配置信息");
commandWordInfoMap.put("07","采集事故疑点数据/记录仪唯一性编号");
commandWordInfoMap.put("08","采集累计行驶里程/行驶速度记录");
commandWordInfoMap.put("09","采集行驶速度/位置信息记录");
commandWordInfoMap.put("10","采集事故疑点记录");
commandWordInfoMap.put("11","采集超时驾驶记录");
commandWordInfoMap.put("12","采集驾驶人身份记录");
commandWordInfoMap.put("13","采集外部供电记录");
commandWordInfoMap.put("14","采集参数修改记录");
commandWordInfoMap.put("15","速度状态日志");

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
	+ "/common/query/jtt0700Message.json?select=deviceId,messageId,messageSeq,commandWord,createTime&countable=true";
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
	}, {
		field : "messageId",
		title : "消息ID",
	}, {
		field : "messageSeq",
		title : "消息流水号",
	}, {
		field : "commandWord",
		title : "命令字",
	}, {
		field : "commandWordInfo",
		title : "命令",
		formatter : function(value, row, index) {
			var commandWord = row.commandWord;
			return commandWordInfoMap.get(commandWord);
		}
	},{
		field : "createTime",
		title : "消息时间",
		formatter : function(value, row, index) {
			var createTime = row.createTime;
			if (createTime == null) {
				return "";
			}
			return new Date(createTime).format();
		}
	},
	{
		title : '操作',
		field : 'opear',
		formatter : function(value, row) {
			var html = '<a   href="javascript:void(0)" class="table_view" title="查看" onclick="view(\''
					+ row._id + '\')">查看</a>';
			return html;
		}
	}];
}

function getIdField(){
	return "_id";
}

function view(id){
	var row = getBootstrapTableRowById(id);
	$("#message-detail-dlg-frm-deviceId").val(getDeviceName(row.deviceId));
	$("#message-detail-dlg-frm-messageId").val(commandWordInfoMap.get(row.commandWord));
	var url=message_api_server_servlet_path+"/common/jtt0700Message/"+id+".json";
	ajaxAsyncGet(url,{},function(result){
		if (result.code!=0) {
			showErrorMessage(result.message);
		}  else {
			var data = result.data;
			if(data){
				$("#message-detail-dlg-frm-messageBody").val($.toJSON(data.data));
				showCommondDialog("message-detail-dlg");
			}else{
				showMessage("无相关数据！");
			}
		}
	});
}