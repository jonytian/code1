function getInitTreeNodes() {
	return getDefaultGroupTree(groupType);
}

function onClickTreeNode(treeId, node){
	if(node.type && node.type == 'root'){
		return;
	}
	var frameObj = window.parent.frames[frame];
	if(node.type && node.type=='ent'){
		$("#search-frm-enterpriseId",frameObj.document).val(node.id);
		$("#search-frm-enterpriseName",frameObj.document).val(node.name);
		$("#search-frm-groupId",frameObj.document).val("");
		$("#search-frm-groupName",frameObj.document).val("");
	}else{
		var parent = node.getParentNode();
		while(parent && parent.type && parent.type!='ent'){
			parent = parent.getParentNode();
		}
		if(parent && parent.type =='ent'){
			$("#search-frm-enterpriseName",frameObj.document).val(parent.name);
			$("#search-frm-enterpriseId",frameObj.document).val(parent.id);
		}
		$("#search-frm-groupId",frameObj.document).val(node.id);
		$("#search-frm-groupName",frameObj.document).val(node.name);
	}
	frameObj.doQuery();
}

function onExpandTreeNode(treeId, parent){
	onExpandGroupTreeNode(groupType,treeId, parent);
}

function reloadTreeNodes(id){
	var treeId = "group-tree";
	var node = getTreeNodeById(treeId,id);
	if(node == null){
		//根节点
		node = getTreeNodeById(treeId,"0");
	}
	
	removeChildNodes(treeId,node);
	var treeNodes = getGroupTree(groupType,id);
	if(treeNodes && treeNodes.length>0){
		addTreeNodes(treeId,node, treeNodes);	
	}
}