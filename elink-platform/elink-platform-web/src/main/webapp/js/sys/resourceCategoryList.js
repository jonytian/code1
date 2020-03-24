$(function() {
	initBootstrapTable();
	initParentCategory();
});

function initParentCategory(){
	var url=management_api_server_servlet_path+"/common/query/"+getApiName()+".json?select=id,name,type&pageSize=1000";
	var data={"parentId.eq":"-1"};
	ajaxAsyncPost(url,data,function(result){
		var data = result.data;	
		if(data.length>0){
			var items = [];
			for(var i=0;i<data.length;i++){
				var item= data[i];
				$('#edit-frm-parentId').append("<option value='"+item[0]+"'>"+item[1]+"</option>");
			}
		}
	});
}

function getApiName() {
	return "resourceCategory";
}

function getQueryParams() {
	var params = {};
	var type = $('#search-type').val();
	if (type) {
		params["type.eq"] = type;
	}
	return params;
}

function getDefaultSort(){
	return "parentId";
}
function getQueryUrl() {
	return management_api_server_servlet_path + "/common/query/" + getApiName() + ".json?countable=true";
}

function getColumns() {
	return [ {
		field : 'check',
		checkbox : true
	}, {
		field : "parentId",
		title : "父级菜单",
		formatter : function(value, row, index) {
			var parentId = row.parentId;
			if (parentId == null || parentId == "-1") {
				return "无";
			}
			return getCategoryName(parentId);
		}
	}, {
		field : "name",
		title : "菜单名称"
	}, {
		field : "order",
		title : "排序"
	}, {
		field : "icon",
		title : "icon"
	}, {
		field : "url",
		title : "url"
	}, {
		field : "createTime",
		title : "创建时间"
	}, {title: '操作',field: 'opear',formatter: function (value, row) {
            var html = '<a   href="javascript:void(0)" class="table_edit" title="编辑" onclick="edit(\'' + row.id + '\')">编辑</a>';
            html += '<a   href="javascript:void(0)" class="table_del" title="删除" onclick="del(\'' + row.id + '\')">删除</a>';
         return html ;
  }}];
}

function getCategoryName(id){
	var url=management_api_server_servlet_path+"/common/"+getApiName()+"/"+id+".json";
	var data={}
	var result =ajaxSyncGet(url,data);
	if (result.code!=0) {
		showErrorMessage(result.message);
		return;
	}
	var data = result.data;
	if(data){
		return data.name; 
	}
	return "无";
}

function setAddInfo(){
	$("#edit-frm-type").val(1);
}