$(function() {
	initBootstrapTable();
	initUser();
});


function initUser() {
	var url = "/common/query/user.json?countable=false&select=id,name&pageNo=1&pageSize=1000";
	var params = {};
	var enterpriseId = $("#search-frm-enterpriseId").val();
	params["enterpriseId.eq"] = enterpriseId;
	$('#multiple-select-source').empty();
	$('#multiple-selected-target').empty();
	ajaxAsyncPost(url, params, function(result) {
		if (result.code != 0) {
			showErrorMessage(result.message);
		} else {
			var list = result.data;
			for (var i = 0; i < list.length; i++) {
				var item = list[i];
				$("#multiple-select-source").append("<option value='" + item.id + "'>" + item.name + "</option>");
			}
		}
	});
}

function getApiName() {
	return "userRoleUser";
}

function getQueryParams() {
	var params = {};
	var roleId = $("#search-frm-roleId").val();
	if (roleId) {
		params["roleId.eq"] = roleId;
	}

	var enterpriseId = $("#search-frm-enterpriseId").val();
	if (enterpriseId) {
		params["enterpriseId.eq"] = enterpriseId;
	}

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
				field : "userName",
				title : "用户名"
			},
			{
				field : "roleName",
				title : "角色"
			},
			{
				title : '操作',
				field : 'opear',
				formatter : function(value, row) {
					var html = '<a   href="javascript:void(0)" class="table_del" title="删除" onclick="del(\''
							+ row.id + '\')">删除</a>';
					return html;
				}
			} ];
}

function add() {
	if (typeof (isAddDisabled) == "function" && isAddDisabled()) {
		return;
	}
	clearEditFrm();
	var roleId = $("#search-frm-roleId").val();
	if (!roleId) {
		showMessage("请先选择要分配的角色！");
	}else{
		initTree("dept-tree");
		var title = "新增" + $("#edit-dlg").attr("title");
		showDialog(title, "edit-dlg");
	}
}

function getInitTreeNodes() {
	var enterpriseId = $("#search-frm-enterpriseId").val();
	var enterpriseName = $("#search-frm-enterpriseName").val();
	if(enterpriseId==""){
		var enterprise = getUserEnterprise();
		enterpriseId = enterprise.id;
		enterpriseName = enterprise.shortName;
	}

	var params = {"enterpriseId.eq" : enterpriseId};
	var children = getOrgNodes(params);
	params["orgId.null"] = true;
	children = children.concat(getDeptNodes(params));

	var root = {};
	root.icon = getDefaultTreeIcon();
	root.id = enterpriseId;
	root.name = enterpriseName;
	root.open = true;
	root.type = "ent";
	root.children = children;
	return root;
}

function getOrgNodes(params) {
	var nodes = [];
	var url = management_api_server_servlet_path + "/common/query/organization.json?countable=false&pageSize=1000&pageNo=1";
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
	var url = management_api_server_servlet_path + "/common/query/department.json?countable=false&pageSize=1000&pageNo=1";
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

function onExpandTreeNode(treeId, parent) {
	if (parent.type && parent.type == 'root') {
		return;
	}

	var children = parent.children;
	if (children && children.length > 0) {
		// 说明已经加载过了
		return;
	}

	var params = {};
	if (parent.type == 'org') {
		params["orgId.eq"] = parent.id;
		params["level.eq"] = 0;
	} else {
		params["parentId.eq"] = parent.id;
	}
	var children = getDeptNodes(params);
	addTreeNodes(treeId, parent, children);
}

function onClickTreeNode(treeId, treeNode) {
	var url = "/common/query/user.json?countable=false&select=id,name&pageNo=1&pageSize=1000";
	var params = {};
	if (treeNode.type == "dept") {
		params["deptId.eq"] = treeNode.id;
	} else if (treeNode.type == "org") {
		params["orgId.eq"] = treeNode.id;
	}
	var enterpriseId = $("#search-frm-enterpriseId").val();
	params["enterpriseId.eq"] = enterpriseId;

	$('#multiple-select-source').empty();
	$('#multiple-selected-target').empty();
	ajaxAsyncPost(url, params, function(result) {
		if (result.code != 0) {
			showErrorMessage(result.message);
		} else {
			var list = result.data;
			for (var i = 0; i < list.length; i++) {
				var item = list[i];
				$("#multiple-select-source").append("<option value='" + item.id + "'>" + item.name + "</option>");
			}
		}
	});
}

function save() {
	var url = management_api_server_servlet_path + "/common/" + getApiName() + ".json";
	
	var enterpriseId = $("#search-frm-enterpriseId").val();
	if(enterpriseId==""){
		enterpriseId = getUserEnterpriseId();
	}

	var roleId = $("#search-frm-roleId").val();
	$("#multiple-selected-target option").map(function() {
		var userId = $(this).val();
		var data = {
			"enterpriseId":enterpriseId,
			"roleId" : roleId,
			"userId" : userId
		};
		ajaxSyncPost(url, data);
	});
	$('#multiple-select-source').empty();
	$('#multiple-selected-target').empty();
	doQuery();
	closeDialog();
}