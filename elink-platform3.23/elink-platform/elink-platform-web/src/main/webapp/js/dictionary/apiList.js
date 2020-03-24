$(function() {
	initBootstrapTable();
});

function getApiName() {
	return "dictionary";
}

function getQueryParams() {
	var params = {};
	params["type.eq"] = DICTIONARY_PROVIDER_API;
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
				align : "left",
				title : "接口标识"
			},
			{
				field : "name",
				title : "接口名称",
				formatter : function(value, row, index) {
					var content = $.evalJSON(row.content);
					return content.name;
				}
			},
			{
				field : "url",
				align : "left",
				title : "URL",
				formatter : function(value, row, index) {
					var content = $.evalJSON(row.content);
					return content.url;
				}
			},
			{
				field : "method",
				title : "请求方法",
				formatter : function(value, row, index) {
					var content = $.evalJSON(row.content);
					return content.method;
				}
			},
			{
				field : "createTime",
				title : "创建时间"
			},
			{
				field : "remark",
				title : "备注"
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
	data.type= DICTIONARY_PROVIDER_API;
	var content = {};
	content.name=data.name;
	content.url=data.url;
	content.method=data.method;
	data.content = $.toJSON(content);
	return data;
}

function setEditInfo(data){
	 var content=$.evalJSON(data.content);
	 $("#edit-frm-name").val(content.name);
	 $("#edit-frm-url").val(content.url);
	 $("#edit-frm-method").val(content.method);
}