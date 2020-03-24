$(function() {
	initBootstrapTable();
	initRole();
	initTree("category-tree");
});

function getInitTreeNodes() {
	var root = {};
	root.icon = getDefaultTreeIcon();
	root.id = "root";
	root.name = "菜单分类";
	root.open = true;
	root.type = "root";
	root.children = getResourceCategoryNodes();
	return root;
}

function onClickTreeNode(treeId, node){
	var params = {};
	if (node.type && node.type == 'root') {
		return ; 
	}
	queryResource(node.id);
}

function getResourceCategoryNodes() {
	var url = management_api_server_servlet_path + "/common/query/resourceCategory.json?select=id,name&pageSize=1000";
	var data = {"parentId.neq":"-1"};
	var result = ajaxSyncPost(url, data);
	if (result.code!=0) {
		showErrorMessage(result.message);
	}  else {
		var list = result.data;
		var nodes = [];
		if (list && list.length > 0) {
			for (var i = 0; i < list.length; i++) {
				var item = list[i];
				nodes.push({"id":item[0],"type":"c","name":item[1]});
				
				$('#search-categoryId').append("<option value='" + item[0] + "'>" + item[1] + "</option>");
			}
		}
		return nodes;
	}
	return [];
}

function initRole() {
	var url = management_api_server_servlet_path + "/common/query/systemRole.json?select=id,name&pageSize=1000";
	var data = {};
	ajaxAsyncPost(url, data, function(result) {
		var data = result.data;
		if (data.length > 0) {
			$('#search-roleId').append("<option value=''>请选择</option>");
			for (var i = 0; i < data.length; i++) {
				var item = data[i];
				$('#edit-frm-roleId').append("<option value='" + item[0] + "'>" + item[1] + "</option>");
				$('#search-roleId').append("<option value='" + item[0] + "'>" + item[1] + "</option>");
			}
		}
	});
}

function setAddInfo(){
	$('#multiple-select-source').empty();
	$('#multiple-selected-target').empty();
}

function queryResource(categoryId) {
	$('#multiple-select-source').empty();
	var url = management_api_server_servlet_path + "/common/query/resource.json?select=id,name,remark&pageSize=1000";
	var data = {
		"categoryId.eq" : categoryId
	};
	ajaxAsyncPost(url, data, function(result) {
		var data = result.data;
		if (data.length > 0) {
			for (var i = 0; i < data.length; i++) {
				var item = data[i];
				var text = item[1];
				if(item[2] && item[2]!=""){
					text +="("+item[2]+")";
				}
				$("#multiple-select-source").append("<option value='" + item[0] + "'>" + text + "</option>");
			}
		}
	});
}

function getApiName() {
	return "resourceRole";
}

function getQueryParams() {
	var params = {};
	var roleId = $("#search-roleId").val();
	if(roleId){
		params["roleId.eq"]= roleId;
	}
	
	var roleId = $("#search-categoryId").val();
	if(roleId){
		params["categoryId.eq"]= roleId;
	}
	return params;
}

function getDefaultSort() {
	return "categoryId";
}

function getQueryUrl() {
	return management_api_server_servlet_path + "/common/query/" + getApiName() + ".json?countable=true";
}

function getColumns() {
	return [
			{
				field : 'check',
				checkbox : true
			},
			{
				field : "roleName",
				title : "角色名称"
			},{
				field : "categoryName",
				title : "菜单分类"
			},
			{
				field : "resourceName",
				title : "菜单名称"
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

function save(){
	if (!validForm("edit-frm")) {
		return;
	}
	var roleId = $("#edit-frm-roleId").val();
	if(roleId){
		var url = management_api_server_servlet_path+"/common/"+getApiName()+".json";
		$("#multiple-selected-target option").map(function(){
		     var resourceId = $(this).val();
		     var data = {"roleId":roleId,"resourceId":resourceId};
			 ajaxSyncPost(url,data);
		});
		doQuery();
		closeDialog();
	}else{
		showMessage("请选择角色！");
	}
}