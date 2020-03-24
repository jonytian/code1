$(function() {
	initBootstrapTable();
});

function getApiName() {
	return "group";
}

function getQueryParams() {
	var params = {};
	params["type.eq"] = $("#search-frm-groupType").val();
	var parentId = $("#search-frm-groupId").val();
	if(parentId!=""){
		params["parentId.eq"] = parentId;
	}
	
	var enterpriseId = $("#search-frm-enterpriseId").val();
	if(enterpriseId!=""){
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
				title : "分组名称"
			},
			{
				field : "remark",
				title : "备注"
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

function setAddInfo(){
	var parentId = $("#search-frm-groupId").val();
	$("#edit-frm-parentId").val(parentId==""?0:parentId);
	$("#edit-frm-enterpriseId").val($("#search-frm-enterpriseId").val());
	var groupName = $("#search-frm-groupName").val() ||  $("#search-frm-enterpriseName").val() || getUserEnterprise().shortName;
	$("#edit-frm-parentName").val(groupName);
	$("#edit-frm-type").val($("#search-frm-groupType").val());
}

function setEditInfo(result){
	setAddInfo();
}

function saveSuccessHandler(){
	var enterpriseId = $("#search-frm-enterpriseId").val();
	if(enterpriseId==""){
		enterpriseId = getUserEnterpriseId();
	}
	var frameObj = window.parent.frames["group-tree-frame"];
	frameObj.reloadTreeNodes(enterpriseId);
}

function delSuccessHandler(id){
	saveSuccessHandler();
}