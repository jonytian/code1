$(function(){
	initBootstrapTable();
	initTree("dept-tree");
});

function getApiName(){
	return "user";
}

function getQueryParams(){
	var params = {};

	var parentId = $("#search-frm-deptId").val();
	if(parentId!=""){
		params["deptId.eq"]= parentId;
	}
	
	var orgId = $("#search-frm-orgId").val();
	if (orgId) {
		params["orgId.eq"] = orgId;
	}
	
	var enterpriseId = $("#search-frm-enterpriseId").val();
	if (enterpriseId) {
		params["enterpriseId.eq"] = enterpriseId;
	}

	return params;
}

function getQueryUrl(){
	return management_api_server_servlet_path+"/common/query/"+getApiName()+".json?countable=true";
}

function getColumns(){
	return [{field: 'check',checkbox :true},
		 {field:"account",title:"登陆账号"},
		 {field:"name",title:"用户名"},
		 {field:"orgName",title:"组织名称"},
		 {field:"deptName",title:"部门名称"},
		 {field:"state",title:"用户状态",sortable:true,formatter: function(value,row,index){
			 var state=row.state;
			 if(state==1){
				 return "正常";
			 }else if(state==0){
				return "已停用";
			 }
		 }},
		 {title: '操作',field: 'opear',width:'200px',formatter: function (value, row) {
			 var html = "";
			 //html +='<a   href="javascript:void(0)" class="table_edit" title="编辑" onclick="edit(\'' + row.id + '\')">编辑</a>';
             html += '<a   href="javascript:void(0)" class="table_edit" title="重置密码" onclick="resetPassword(\'' + row.id + '\')">重置密码</a>';
             if(row.state!=0){
            	 html += '<a   href="javascript:void(0)" class="table_del" title="停用" onclick="disable(\'' + row.id + '\')">停用</a>';
             }else if(row.state==0){
                 html += '<a   href="javascript:void(0)" class="table_edit" title="启用" onclick="enable(\'' + row.id + '\')">启用</a>';
             }
             html += '<a   href="javascript:void(0)" class="table_del" title="删除" onclick="del(\'' + row.id + '\')">删除</a>';
          return html ;
   }}];
}

function disable(id){
	var row = getBootstrapTableRowById(id);
	if(row.account == getUserAccount()){
		showMessage("不能操作当前登陆账号！");
		return true;
	}
	if(row.state==0){
		showMessage("该用户已经被停用");
		return;
	}
	setState(id,0,"确定要停用该用户？");
}

function enable(id){
	var row = getBootstrapTableRowById(id);
	if(row.state==1){
		showMessage("该用户已启用");
		return;
	}
	setState(row.id,1,"确定要启用该用户？");
}

function setState(id,state,message){
	var enterpriseId = $("#search-frm-enterpriseId").val();
	showConfirm(message,function() {
    	var url =  management_api_server_servlet_path+"/common/"+getApiName()+"/"+id+".json";
    	var data = {"state":state,"enterpriseId":enterpriseId};
    	ajaxAsyncPatch(url,data,function(result){
    		if (result.code!=0) {
    			showErrorMessage(result.message);
    		}  else {
    			showMessage("设置成功");
    			doQuery();
    		}
    	});
	});
}

function resetPassword(id){
	var row = getBootstrapTableRowById(id);
	if(row.account == getUserAccount()){
		showMessage("不能操作当前登陆账号！");
		return true;
	}
	var message ="是否重置员工“"+row.name+"”的密码？";
	var enterpriseId = $("#search-frm-enterpriseId").val();
	showConfirm(message,function() {
    	var url =  management_api_server_servlet_path+"/common/"+getApiName()+"/"+id+".json";
    	var data = {"password":"123456","enterpriseId":enterpriseId};
    	ajaxAsyncPatch(url,data,function(result){
    		if (result.code!=0) {
    			showErrorMessage(result.message);
    		}  else {
    			showMessage("密码已成功设置为“123456”");
    		}
    	});
	});
}

function isAddDisabled(){
	return !enableOption();
}

function isEditDisabled(id){
	return !enableOption();
}

function isDelDisabled(id){
	var row = getBootstrapTableRowById(id);
	if(row.account == getUserAccount()){
		showMessage("不能删除当前登陆账号！");
		return true;
	}
	return !enableOption();
}

function setAddInfo(){
	$("#edit-frm-enterpriseId").val($("#search-frm-enterpriseId").val());
	$("#edit-frm-orgId").val($("#search-frm-orgId").val());
	$("#edit-frm-deptId").val($("#search-frm-deptId").val());
	
	var orgName = $("#search-frm-deptName").val()
	|| $("#search-frm-orgName").val()
	|| $("#search-frm-enterpriseName").val()
	|| getUserEnterprise().shortName;
	$("#edit-frm-deptName").val(orgName);
	$("#edit-frm-type").val(3);
}

function moveDepartment() {
	var rows = getBootstrapTableSelectedRows();
	if (rows.length > 0) {
		showDialog("转部门", "edit-dlg-move-dept");
	} else {
		showMessage("请选择要转部门的员工！");
	}
}


function getInitTreeNodes() {
	var enterprise = getUserEnterprise();
	var children = getSubsidiaryNodes(null);
	if (children.length > 0) {
		var node = {};
		node.id = enterprise.id;
		node.name = enterprise.shortName;
		node.type = "ent";
		node.open = false;
		node.isParent = true;
		node.icon = getDefaultTreeIcon();
		children.unshift(node);
	} else {
		var params = {
			"enterpriseId.eq" : enterprise.id
		};
		children = getOrgNodes(params);
		params["orgId.null"] = true;
		children = children.concat(getDeptNodes(params));
	}

	var root = {};
	root.icon = getDefaultTreeIcon();
	root.id = "0";
	root.name = enterprise.shortName;
	root.open = true;
	root.type = "root";
	root.children = children;
	return root;
}

function getOrgNodes(params) {
	var nodes = [];
	var url = management_api_server_servlet_path
			+ "/common/query/organization.json?countable=false&pageSize=1000&pageNo=1";
	var result = ajaxSyncPost(url, params);
	if (result.code != 0) {
		showErrorMessage(result.message);
	} else {
		var list = result.data;
		for (var i = 0; i < list.length; i++) {
			var item = list[i];
			var node = {};
			node.name = item.name;
			node.id = item.id;
			node.open = false;
			node.type = "org";
			node.isParent = true;
			nodes.push(node);
		}
	}
	return nodes;
}

function getDeptNodes(params) {
	var nodes = [];
	var url = management_api_server_servlet_path
			+ "/common/query/department.json?countable=false&pageSize=1000&pageNo=1";
	var result = ajaxSyncPost(url, params);
	if (result.code != 0) {
		showErrorMessage(result.message);
	} else {
		var list = result.data;
		for (var i = 0; i < list.length; i++) {
			var item = list[i];
			var node = {};
			node.name = item.name;
			node.id = item.id;
			node.open = false;
			node.type = "dept";
			node.isParent = true;
			nodes.push(node);
		}
	}
	return nodes;
}

function onExpandTreeNode(treeId, node) {
	if (node.type && node.type == 'root') {
		return;
	}

	var children = node.children;
	if (children && children.length > 0) {
		// 说明已经加载过了
		return;
	}

	if (node.type == 'ent') {
		// 加载子公司，加载组织以及部门
		var children = [];
		if(getUserEnterpriseId()!=node.id){
			children = getSubsidiaryNodes(node.id);
		}
		var params = {
			"enterpriseId.eq" : node.id
		};
		children = children.concat(getOrgNodes(params));
		params["orgId.null"] = true;
		children = children.concat(getDeptNodes(params));
		addTreeNodes(treeId, node, children);
	} else {
		var params = {};
		var parent = node.getParentNode();
		while(parent && parent.type && parent.type!='ent'){
			parent = parent.getParentNode();
		}
		if(parent && parent.type =='ent'){
			params["enterpriseId.eq"] =parent.id;
		}
		if (node.type == 'org') {
			params["orgId.eq"] = node.id;
			params["level.eq"] = 0;
		} else {
			params["parentId.eq"] = node.id;
		}
		var children = getDeptNodes(params);
		addTreeNodes(treeId, node, children);
	}
}

function saveMoveGroup() {
	var nodes = getSelectedNodes("dept-tree");
	var index = 0;
	var node;
	for (var i = 0; i < nodes.length; i++) {
		node = nodes[i];
		if (node.type == "dept") {
			index++;
		}
	}

	if (index > 1 || index == 0) {
		showErrorMessage("只能选择一个部门");
		return;
	}
	
	var parent = node.getParentNode();
	while (parent && parent.type != 'ent') {
		parent = parent.getParentNode();
	}
	var enterpriseId = "";
	if (parent && parent.type == 'ent') {
		enterpriseId = parent.id;
	}

	var parent = node.getParentNode();
	while (parent && parent.type != 'org') {
		parent = parent.getParentNode();
	}
	var orgId = "";
	if (parent && parent.type == 'org') {
		orgId = parent.id;
	}
	
	var rows = getBootstrapTableSelectedRows();
	for (var i = 0; i < rows.length; i++) {
		var row = rows[i]
		var data = {};
		data.id = row.id;
		data.deptId = node.id;
		data.orgId = orgId;
		data.enterpriseId = enterpriseId;

		var url = management_api_server_servlet_path + "/common/" + getApiName() + "/" + row.id + ".json"
		var result = ajaxSyncPatch(url, data,true);
		if (result.code != 0) {
			showErrorMessage(result.message);
		} 
	}
	doQuery();
	closeDialog();
}