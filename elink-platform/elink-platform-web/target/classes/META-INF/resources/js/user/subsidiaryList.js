$(function() {
	initBootstrapTable();
});

function getApiName(){
	return "enterprise";
}

function getColumns(){
	return [{field: 'check',checkbox :true},
		 {field:"name",title:"客户账号"},
		 {field:"shortName",title:"账号简称"},
		 {field:"type",title:"类型",formatter: function (value, row) {
              if(row.id==getUserEnterpriseId()){
            	  return "当前账号";
              }else{
            	  return "下级账号";
              }
  		 }},
		 {field:"contact",title:"联系人"},
		 {field:"tel",title:"联系电话"},
		 {field:"createTime",title:"创建时间"},
		 {title: '操作',field: 'opear',formatter: function (value, row) {
             var html = '<a   href="javascript:void(0)" class="table_edit" title="编辑" onclick="edit(\'' + row.id + '\')">编辑</a>';
             if(isSuperAdmin()){
                 html += '<a   href="javascript:void(0)" class="table_del" title="流量控制" onclick="editPermission(\'' + row.id + '\')">流量控制</a>';
             }
             html += '<a   href="javascript:void(0)" class="table_del" title="删除" onclick="del(\'' + row.id + '\')">删除</a>';
          return html ;
   }}];
}

function getQueryUrl(){
	return management_api_server_servlet_path+"/common/query/"+getApiName()+".json?countable=true";
}

function getQueryParams(){
	var params = {};
	var parentId = $("#search-frm-enterpriseId").val();
	if(parentId){
		params["parentId.eq"]=parentId;
	}else{
		params["parentId.eq"]=getUserEnterpriseId();
	}
	return params;
}

function setAddInfo(){
	var enterpriseId = $("#search-frm-enterpriseId").val();
	if(enterpriseId){
		$("#edit-frm-enterpriseId").val(enterpriseId);
	}else{
		$("#edit-frm-enterpriseId").val(getUserEnterpriseId());
	}
}

function editPermission(id){
	var row = getBootstrapTableRowById(id);
	window.parent.frames["subsidiary-list-frame"].location.href="../sys/enterpriseConfig.do?id="+id+"&name="+row.shortName;
}