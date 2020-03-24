$(function(){
	initBootstrapTable();
	initRole();
	initEnterpriseId();
});

function initRole(){
	var url=management_api_server_servlet_path+"/common/query/systemRole.json?select=id,name&pageSize=1000";
	ajaxAsyncPost(url,{},function(result){
		var data = result.data;	
		if(data.length>0){
			for(var i=0;i<data.length;i++){
				var item= data[i];
				$('#edit-frm-roleId').append("<option value='"+item[0]+"'>"+item[1]+"</option>");
			}
		}
	});
}

function initEnterpriseId(){
	var url = management_api_server_servlet_path + "/aas/subsidiary.json?select=id,shortName";
	ajaxAsyncGet(url,{},function(result){
		var data = result.data;	
		if(data.length>0){
			for(var i=0;i<data.length;i++){
				var item= data[i];
				$('#edit-frm-enterpriseId').append("<option value='"+item[0]+"'>"+item[1]+"</option>");
			}
		}
	});
}

function getApiName(){
	return "enterpriseRole";
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
		 {field:"enterpriseName",title:"企业名称"},
		 {field:"roleName",title:"角色名称"},
		 {title: '操作',field: 'opear',formatter: function (value, row) {
             var html = '<a   href="javascript:void(0)" class="table_edit" title="编辑" onclick="edit(\'' + row.id + '\')">编辑</a>';
             html += '<a   href="javascript:void(0)" class="table_del" title="删除" onclick="del(\'' + row.id + '\')">删除</a>';
          return html ;
   }}];
}