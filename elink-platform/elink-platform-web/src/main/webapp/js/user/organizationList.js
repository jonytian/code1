$(function() {
	initBootstrapTable();
});

function getApiName() {
	return "organization";
}

function getQueryParams() {
	var params = {};
	return params;
}

function getQueryUrl() {
	return "/common/query/" + getApiName() + ".json?countable=true";
}

function getColumns() {
	return [
			{
				field : 'check',
				checkbox : true
			},
			{
				field : "name",
				title : "组织名称"
			},
			{
				field : "code",
				title : "组织代码"
			},
			{
				field : "createTime",
				title : "创建时间"
			},
			{
				title : '操作',
				field : 'opear',
				formatter : function(value, row) {
					var html = '<a   href="javascript:void(0)" class="table_edit" title="编辑" onclick="edit(\''
							+ row.id + '\')">编辑</a>';
					html += '<a   href="javascript:void(0)" class="table_del" title="删除" onclick="del(\''
							+ row.id + '\')">删除</a>';
					return html;
				}
			} ];
}