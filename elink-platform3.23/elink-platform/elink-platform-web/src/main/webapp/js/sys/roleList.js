$(function(){
	initBootstrapTable();
});

function getApiName(){
	return "systemRole";
}

function getQueryParams(){
	var params = {};
	return params;
}

function getQueryUrl(){
	return management_api_server_servlet_path+"/common/query/"+getApiName()+".json?countable=true";
}

function getColumns(){
	return [{field: 'check',checkbox :true},
		 {field:"name",title:"角色名称"},
		 {field:"code",title:"角色代码"},
		 {field:"remark",title:"备注"},
		 {field:"createTime",title:"创建时间"},
		 {title: '操作',field: 'opear',formatter: function (value, row) {
             var html = '<a   href="javascript:void(0)" class="table_edit" title="编辑" onclick="edit(\'' + row.id + '\')">编辑</a>';
             html += '<a   href="javascript:void(0)" class="table_del" title="删除" onclick="del(\'' + row.id + '\')">删除</a>';
          return html ;
   }}];
}