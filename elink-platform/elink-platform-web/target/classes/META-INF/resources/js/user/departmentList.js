$(function() {
	initBootstrapTable();
});

function getApiName() {
	return "department";
}

function getQueryParams() {
	var params = {};

	var deptId = $("#search-frm-deptId").val();
	if (deptId) {
		params["parentId.eq"] = deptId;
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
				field : "name",
				title : "部门名称"
			},
			{
				field : "orgName",
				title : "组织名称",
				formatter : function(value, row, index) {
					var orgName = $("#search-frm-orgName").val()
							|| $("#search-frm-enterpriseName").val();
					return orgName;
				}
			},
			{
				field : "createTime",
				title : "创建时间"
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

function setAddInfo() {
	var orgName = $("#search-frm-deptName").val()
			|| $("#search-frm-orgName").val()
			|| $("#search-frm-enterpriseName").val()
			|| getUserEnterprise().shortName;
	$("#edit-frm-orgName").val(orgName);
	$("#edit-frm-orgId").val($("#search-frm-orgId").val());
	$("#edit-frm-parentId").val($("#search-frm-deptId").val());
	$("#edit-frm-enterpriseId").val($("#search-frm-enterpriseId").val());
}

function saveSuccessHandler() {
	var enterpriseId = $("#search-frm-deptId").val()
			|| $("#search-frm-orgId").val()
			|| $("#search-frm-enterpriseId").val();
	var frameObj = window.parent.frames["dept-tree-frame"];
	frameObj.reloadTreeNodes(enterpriseId);
}

function delSuccessHandler(id) {
	saveSuccessHandler();
}