$(function(){
	initBootstrapTable();
	
	initTree("company-tree");
});

function getInitTreeNodes() {
	return getEnterpriseTreeNodes();
}

function onExpandTreeNode(treeId, node) {
	if (node.type && node.type == 'root') {
		return;
	}
	var children = node.children;
	if (children && children.length > 0) {
		return;
	}

	var enterprise = getUserEnterprise();
	if(enterprise.id != node.id){
		var treeNodes = getSubsidiaryNodes(node.id);
		if(treeNodes && treeNodes.length>0){
			addTreeNodes(treeId,node, treeNodes);	
		}
	}
}

function getApiName(){
	return "dictionary";
}

function getQueryParams(){
	var params = {};
	params["type.eq"]= dictionary_officers_car_process;
	return params;
}

function getQueryUrl(){
	return management_api_server_servlet_path+"/common/query/"+getApiName()+".json?countable=true";
}

function getColumns(){
	return [{field: 'check',checkbox :true},
		 {field:"code",title:"流程id"},
		 {field:"content",title:"流程名称"},
		 {field:"remark",title:"备注"},
		 {field:"createTime",title:"创建时间"},
		 {title: '操作',field: 'opear',formatter: function (value, row) {
             var html = '<a   href="javascript:void(0)" class="table_edit" title="编辑" onclick="edit(\'' + row.id + '\')">编辑</a>';
             html += '<a   href="javascript:void(0)" class="table_del" title="删除" onclick="del(\'' + row.id + '\')">删除</a>';
          return html ;
   }}];
}

function setAddInfo(){
	$("#edit-frm-type").val(dictionary_officers_car_process);
}

function publish(){
	var rows = getBootstrapTableSelectedRows();
	if (rows.length > 0) {
		showCommondDialog("edit-dlg-publish-workflow");
	} else {
		showMessage("请选择要发布的流程！");
	}
}

function doPublish(){
	var nodes = getSelectedNodes("company-tree");
	var ids = new Array();
	var index = 0;
	var node;
	for (var i = 0; i < nodes.length; i++) {
		node = nodes[i];
		ids[index++] = node.id;
	}
	if (ids.length > 1 || ids.length == 0) {
		showErrorMessage("只能选择一个公司");
		return;
	}

	var process = [];
	var rows = getBootstrapTableSelectedRows();
	for (var i = 0; i < rows.length; i++) {
		var row = rows[i]
		process.push({"processId":row.code,"processName":row.content});
	}
	
	var data = {};
	data.process = process;
	data.enterpriseId= ids[0];

	var servletPath = management_api_server_servlet_path;
	if (typeof (getServletPath) == "function") {
		servletPath = getServletPath();
	}
	var url = servletPath + "/workflow/deployments.json";
	ajaxAsyncPost(url, data, function(result) {
		if (result.code != 0) {
			showErrorMessage(result.message);
		} else {
			showMessage("流程发布成功！");
			closeDialog();
		}
	});
	
}