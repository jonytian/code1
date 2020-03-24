$(function() {
	initBootstrapTable();
	initParentCategory();

	$("#edit-frm-categoryId").change(function() {
		queryResource($(this).children('option:selected').val());
	});
	
	initTree("category-tree");
});

function initParentCategory() {
	var html = "<option value=''>请选择菜单分类</option>";
	$('#search-categoryId').append(html);
	$('#edit-frm-categoryId').append(html);
	var url = management_api_server_servlet_path + "/aas/menu/navigation.json";
	var data = {};
	ajaxAsyncGet(url, data, function(result) {
		var data = result.data;
		if (data && data.length > 0) {
			for (var i = 0; i < data.length; i++) {
				var item = data[i];
				html = "<option value='" + item.id + "'>" + item.name + "</option>";
				$('#search-categoryId').append(html);
			}
		}
	});
}

function getApiName() {
	return "userResource";
}

function getQueryParams() {
	var params = {};
	var roleId = $("#search-frm-roleId").val();
	params["roleId.eq"] = roleId;
	var categoryId = $("#search-categoryId").val();
	if (categoryId) {
		params["categoryId.eq"] = categoryId;
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
				field : "categoryName",
				title : "分类名称"
			},
			{
				field : "resourceName",
				title : "菜单名称"
			},
			{
				title : '操作',
				field : 'opear',
				formatter : function(value, row) {
					var html = '<a   href="javascript:void(0)" class="table_del" title="删除" onclick="del(\'' + row.id + '\')">删除</a>';
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
		$("#edit-frm-roleId").val(roleId);
		var title = "新增" + $("#edit-dlg").attr("title");
		showDialog(title, "edit-dlg");
	}
}

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

function queryResource(categoryId) {
	$('#multiple-select-source').empty();
	//$('#multiple-selected-target').empty();
	var url = management_api_server_servlet_path + "/aas/menu/" + categoryId + ".json?";
	var data = {};
	ajaxAsyncGet(url, data, function(result) {
		var data = result.data;
		if (data.length > 0) {
			for (var i = 0; i < data.length; i++) {
				var item = data[i];
				var remark = item.remark;
				var name = item.name;
				if(remark){
					name="("+remark+")";
				}
				$("#multiple-select-source").append( "<option value='" + item.id + "'>" + name + "</option>");
			}
		}
	});
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

function save() {
	var url = management_api_server_servlet_path + "/common/" + getApiName() + ".json";
	var roleId = $("#edit-frm-roleId").val();
	if (!roleId) {
		showMessage("请先选择要分配的角色！");
		closeDialog();
		return;
	}
	var enterpriseId = $("#search-frm-enterpriseId").val();
	$("#multiple-selected-target option").map(function() {
		var resourceId = $(this).val();
		var data = {
			"roleId" : roleId,
			"resourceId" : resourceId,
			"enterpriseId":enterpriseId
		};
		ajaxSyncPost(url, data);
	});
	doQuery();
	closeDialog();
}
