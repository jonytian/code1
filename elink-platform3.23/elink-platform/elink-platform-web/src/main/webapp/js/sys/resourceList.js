$(function() {
	initBootstrapTable();
	initParentCategory();
});

function initParentCategory(){
	$('#search-categoryId').append("<option value=''>请选择</option>");
	var url=management_api_server_servlet_path+"/common/query/resourceCategory.json?select=id,name,type&pageSize=1000";
	var data={"parentId.neq":"-1"};
	ajaxAsyncPost(url,data,function(result){
		var data = result.data;	
		if(data.length>0){
			for(var i=0;i<data.length;i++){
				var item= data[i];
				var name = item[1];
				$('#edit-frm-categoryId').append("<option value='"+item[0]+"'>"+name+"</option>");
				$('#search-categoryId').append("<option value='"+item[0]+"'>"+name+"</option>");
			}
		}
	});
}

function getApiName() {
	return "resource";
}

function getQueryParams() {
	var params = {};
	var categoryId = $('#search-categoryId').val();
	if (categoryId) {
		params["categoryId.eq"] = categoryId;
	}
	var name = $('#search-name').val();
	if (name) {
		params["name.like"] = name;
	}
	return params;
}

function getDefaultSort(){
	return "categoryId";
}
function getQueryUrl() {
	return management_api_server_servlet_path + "/common/query/" + getApiName() + ".json?countable=true";
}

function getColumns() {
	return [ {
		field : 'check',
		checkbox : true
	}, {
		field : "categoryId",
		title : "父级菜单",
		formatter : function(value, row, index) {
			var categoryId = row.categoryId;
			if (categoryId == null || categoryId == "-1") {
				return "无";
			}
			return getCategoryName(categoryId);
		}
	}, {
		field : "name",
		align : "left",
		title : "菜单名称"
	}, {
		field : "order",
		sortable : true,
		title : "排序"
	}, {
		field : "icon",
		title : "icon"
	}, {
		field : "url",
		align : "left",
		title : "url"
	}, {
		field : "createTime",
		sortable : true,
		title : "创建时间"
	}, {title: '操作',field: 'opear',formatter: function (value, row) {
            var html = '<a   href="javascript:void(0)" class="table_edit" title="编辑" onclick="edit(\'' + row.id + '\')">编辑</a>';
            if(row.state!=0){
              	 html += '<a   href="javascript:void(0)" class="table_del" title="停用" onclick="disable(\'' + row.id + '\')">停用</a>';
               }else if(row.state==0){
                   html += '<a   href="javascript:void(0)" class="table_edit" title="启用" onclick="enable(\'' + row.id + '\')">启用</a>';
               }
            html += '<a   href="javascript:void(0)" class="table_del" title="删除" onclick="del(\'' + row.id + '\')">删除</a>';
         return html ;
  }}];
}

function getCategoryName(id){
	var url=management_api_server_servlet_path+"/common/resourceCategory/"+id+".json";
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
	$("#edit-frm-categoryId option:first").prop("selected", 'selected');
	$("#edit-frm-type option:first").prop("selected", 'selected');
	$("#edit-frm-state").val(1);
}

function disable(id){
	var row = getBootstrapTableRowById(id);
	if(row.state==0){
		showMessage("该菜单已经被停用");
		return;
	}
	setState(id,0,"确定要停用该菜单？");
}

function enable(id){
	var row = getBootstrapTableRowById(id);
	if(row.state==1){
		showMessage("该菜单已启用");
		return;
	}
	setState(id,1,"确定要启用该菜单？");
}

function setState(id,state,message){
	showConfirm(message,function() {
    	var url =  management_api_server_servlet_path+"/common/"+getApiName()+"/"+id+".json";
    	var data = {"state":state};
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