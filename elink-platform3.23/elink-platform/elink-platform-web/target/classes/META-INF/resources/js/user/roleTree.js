$(document).ready(function() {
	initTree("role-tree");
});

function getInitTreeNodes() {
	var enterprise = getUserEnterprise();
	var children = getUserRoleNodes(enterprise.id);
	
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

function onExpandTreeNode(treeId, parent){
	if(parent.type && parent.type=='root'){
		return;
	}

	var children = parent.children;
	if (children && children.length > 0) {
		if (parent.type && parent.type == 'ent') {
			// 子公司跟部门已经展开
			return;
		}
	}

	if (parent.type && parent.type == 'ent') {
		//加载子公司跟部门
		var enterprise = getUserEnterprise();
		if(enterprise.id != parent.id){
			var treeNodes = getSubsidiaryNodes(parent.id);
			if(treeNodes && treeNodes.length>0){
				addTreeNodes(treeId,parent, treeNodes);	
			}
		}

		var treeNodes = getUserRoleNodes(parent.id);
		if(treeNodes && treeNodes.length>0){
			addTreeNodes(treeId,parent, treeNodes);	
		}
	}
}

function onClickTreeNode(treeId, node){
	if(node.type && node.type=='role'){
		var parent = node.getParentNode();
		while(parent && parent.type!='ent'){
			parent = parent.getParentNode();
		}
		var frameObj = window.parent.frames[frame];
		if(parent.type =='ent'){
			$("#search-frm-enterpriseName",frameObj.document).val(parent.name);
			$("#search-frm-enterpriseId",frameObj.document).val(parent.id);
		}
		$("#search-frm-roleId",frameObj.document).val(node.id);
		frameObj.doQuery();
	}
}

function getUserRoleNodes(enterpriseId){
	var children = [];
	var url = management_api_server_servlet_path+"/common/query/userRole.json?select=id,name&countable=false&pageSize=1000&pageNo=1&orderBy=createTime&desc=true";
	var data={"enterpriseId.eq":enterpriseId}
	var result = ajaxSyncPost(url,data);
	if (result.code!=0) {
		showErrorMessage(result.message);
	}else{
		var list = result.data;
		for(var i=0;i<list.length;i++){
			var item = list[i];
			var node = {};
			node.type = "role";
			node.name = item[1];
			node.id = item[0];
			children.push(node);
		}
	}
	return children;
}