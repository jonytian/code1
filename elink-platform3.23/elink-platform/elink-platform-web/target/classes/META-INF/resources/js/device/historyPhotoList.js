$(function() {
	initBootstrapTable();
});

function getQueryParams() {
	var params = {};
	params["bizType.eq"] = 1;
	params["resourceType.eq"] = 4;
	var deviceId = $("#search-deviceId").val();
	if (query && deviceId) {
		params["deviceId.eq"] = deviceId;
	}
	return params;
}

function getQueryUrl() {
	return message_api_server_servlet_path + "/common/query/deviceHistoryMedia.json?countable=true";
}

function getColumns() {
	return [
			{
				field : 'check',
				checkbox : true
			},
			{
				field : "deviceName",
				title : "车牌号",
				formatter : function(value, row, index) {
					var deviceId = row.deviceId;
					return getDeviceName(deviceId);
				}
			},
			{
				field : "resourceType",
				title : "多媒体类型",
				formatter : function(value, row, index) {
					// 0:图像；1:音频；2:视频
					return "图像";
				}
			},
			{
				field : "eventCode",
				title : "事件项编码",
				formatter : function(value, row, index) {
					// 0：平台下发指令；1：定时动作；2：抢劫报警触发；3：碰撞侧翻报警触发；其他保留
					var eventCode = row.eventCode;
					if (eventCode == 0) {
						return "平台下发指令";
					} else if (eventCode == 1) {
						return "定时动作";
					} else if (eventCode == 2) {
						return "抢劫报警触发";
					} else if (eventCode == 3) {
						return "碰撞侧翻报警触发";
					} else {
						return "其他";
					}
				}
			},
			{
				field : "channelId",
				title : "拍摄通道id"
			},
			{
				field : "createTime",
				title : "上传时间"
			},
			{
				title : '操作',
				field : 'opear',
				width : '100px',
				formatter : function(value, row, index) {
					var html = '<a   href="javascript:void(0)" class="table_view" title="查看" onclick="view(\''+ row.id + '\')">查看</a>';
					return html;
				}
			} ];
}

function view(id) {
	var row = getBootstrapTableRowById(id);
	var mediaUrl = null;
	if(getFileServer()){//如果配置了文件服务器，则直接从文件服务器下载
		mediaUrl = getFileServer()+(row.filePath).replace(/\\/g,"\/");
	}else{
		//从ftp服务器下载
		mediaUrl = getContextPath() +"/attachment/download.do?url=/media/ftp/download/"+row.id+".json";
	}
     var title = getDeviceName(row.deviceId);
	 showImg(title,mediaUrl);
}