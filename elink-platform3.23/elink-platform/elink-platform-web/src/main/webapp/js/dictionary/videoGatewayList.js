$(function() {
	initBootstrapTable();
});

function getApiName() {
	return "dictionary";
}

function getQueryParams() {
	var params = {};
	params["type.eq"] = dictionary_video_gateway;
	return params;
}

function getQueryUrl() {
	return management_api_server_servlet_path + "/common/query/" + getApiName() + ".json?countable=true";
}

function getColumns() {
	return [
			{
				field : 'check',
				checkbox : true
			},{
				field : "code",
				title : "网关类型",
				formatter : function(value, row, index) {
					var code = row.code;
					if(code=="1"){
						return "实时视频";
					}else if(code=="2"){
						return "历史视频";
					}else if(code=="3"){
						return "语音对讲";
					}
				}
			},
			{
				field : "ip",
				title : "网关外网ip",
				formatter : function(value, row, index) {
					var content = $.evalJSON(row.content);
					return content.ip;
				}
			},
			{
				field : "udpPort",
				title : "udp端口",
				formatter : function(value, row, index) {
					var content = $.evalJSON(row.content);
					return content.udpPort;
				}
			},
			{
				field : "tcpPort",
				title : "tcp端口",
				formatter : function(value, row, index) {
					var content = $.evalJSON(row.content);
					return content.tcpPort;
				}
			},
			{
				field : "limit",
				title : "允许连接数",
				formatter : function(value, row, index) {
					var content = $.evalJSON(row.content);
					return content.limit;
				}
			},
			{
				field : "createTime",
				title : "创建时间"
			},
			{
				field : "remark",
				title : "备注",
				formatter : function(value, row, index) {
					var content = $.evalJSON(row.content);
					return content.remark;
				}
			},
			{
				title : '操作',
				field : 'opear',
				formatter : function(value, row) {
					var html = '<a   href="javascript:void(0)" class="table_edit" title="编辑" onclick="edit(\''+ row.id + '\')">编辑</a>';
					html += '<a   href="javascript:void(0)" class="table_del" title="删除" onclick="del(\''+ row.id + '\')">删除</a>';
					return html;
				}
			} ];
}

function getEditFormData(){
	if (!validForm("edit-frm")) {
		return;
	}
	var data = $("#edit-frm").serializeObject();
	data.type= dictionary_video_gateway;
	var content = {};
	content.ip=data.ip;
	content.limit=data.limit;
	content.udpPort=data.udpPort;
	content.tcpPort=data.tcpPort;
	data.content = $.toJSON(content);
	return data;
}

function setEditInfo(data){
	 var content=$.evalJSON(data.content);
	 $("#edit-frm-code").val(data.code);
	 $("#edit-frm-ip").val(content.ip);
	 $("#edit-frm-limit").val(content.limit);
	 $("#edit-frm-udpPort").val(content.udpPort);
	 $("#edit-frm-tcpPort").val(content.tcpPort);
}