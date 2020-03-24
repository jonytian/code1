$(document).ready(function() {
	initTree("dept-tree");
});

function getInitTreeNodes() {
	var enterprise = getUserEnterprise();
	var params = { "enterpriseId.eq" : enterprise.id };
	children = getOrgNodes(params);
	params["orgId.null"] = true;
	var children = children.concat(getDeptNodes(params));
	var subsidiaryNodes = getSubsidiaryNodes(null);
	if(subsidiaryNodes && subsidiaryNodes.length > 0){
		children=children.concat(subsidiaryNodes);
	}

	var root = {};
	root.icon = getDefaultTreeIcon();
	root.id = enterprise.id;
	root.name = "锐承物联数据平台";
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

function onExpandTreeNode(treeId, parent) {
	if (parent.type && parent.type == 'root') {
		return;
	}

	var children = parent.children;
	if (children && children.length > 0) {
		// 说明已经加载过了
		return;
	}

	if (parent.type == 'ent') {
		// 加载子公司，加载组织以及部门
		var children = [];
		if(getUserEnterpriseId()!=parent.id){
			children = getSubsidiaryNodes(parent.id);
		}
		var params = {
			"enterpriseId.eq" : parent.id
		};
		children = children.concat(getOrgNodes(params));
		params["orgId.null"] = true;
		children = children.concat(getDeptNodes(params));
		addTreeNodes(treeId, parent, children);
	} else {
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
}

function onClickTreeNode(treeId, node) {
	var frameObj = window.parent.frames[frame];
	var enterpriseId = "", enterpriseName = "", orgId = "", orgName = "", deptId = "", deptName = "";
	if (node.type == 'dept') {
		var parent = node.getParentNode();
		while (parent && parent.type != 'org') {
			parent = parent.getParentNode();
		}
		if (parent && parent.type == 'org') {
			orgId = parent.id;
			orgName = parent.name;
		}

		var parent = node.getParentNode();
		while (parent && parent.type != 'ent') {
			parent = parent.getParentNode();
		}
		if (parent && parent.type == 'ent') {
			enterpriseId = parent.id;
			enterpriseName = parent.name;
		}

		deptId = node.id;
		deptName = node.name;
	} else if (node.type == 'org') {
		orgId = node.id;
		orgName = node.name;

		var parent = node.getParentNode();
		while (parent && parent.type != 'ent') {
			parent = parent.getParentNode();
		}
		if (parent && parent.type == 'ent') {
			enterpriseId = parent.id;
			enterpriseName = parent.name;
		}
	} else {
		enterpriseId = node.id;
		enterpriseName = node.name;
	}

	$("#search-frm-enterpriseName", frameObj.document).val(enterpriseName);
	$("#search-frm-enterpriseId", frameObj.document).val(enterpriseId);
	$("#search-frm-orgName", frameObj.document).val(orgName);
	$("#search-frm-orgId", frameObj.document).val(orgId);
	$("#search-frm-deptName", frameObj.document).val(deptName);
	$("#search-frm-deptId", frameObj.document).val(deptId);

	frameObj.doQuery();
}

function reloadTreeNodes(id){
	var treeId = "dept-tree";
	var node = getTreeNodeById(treeId,id);
	if(node==null){
		initTree("dept-tree");
	}else{
		removeChildNodes(treeId,node);
		onExpandTreeNode(treeId, node);
	}
}