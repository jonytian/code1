<%@ page language="java" contentType="text/html; charset=utf-8" 	pageEncoding="utf-8"%>
<div id="edit-dlg-move-group" class="easyui-dialog" style="width:300px;height:350px; padding:10px 20px" z-index="10000" closed="true" 	buttons="#edit-dlg-move-group-buttons">
	<div id="group-tree-panel" class="easyui-panel" style="padding:5px;height:250px;">
	<ul id="group-tree" class="easyui-tree"></ul>
</div>
</div>
<div id="edit-dlg-move-group-buttons">
    <button onclick="saveMoveGroup()"><i class='icon-save'></i> 保存</button>
	<button onclick="javascript:$('edit-dlg-move-group').dialog('close')"><i class='icon-undo'></i> 取消</button>
</div>
<script language="javaScript">
$(document).ready(function() {
	initGroupTree();
});


function initGroupTree(){
	loadGroupTree();
	loadGroupTreeData();
}

function loadGroupTree(){
	$("#group-tree").tree({
		 lines:true,
		 animate:true,
		 checkbox:true,
		 cascadeCheck:false,
		 onlyLeafCheck:true,
		 onLoadSuccess:function(node, data){
			 $(this).tree("expandAll");			 
		 }
	});
}

function loadGroupTreeData(){
	var type =1;
	if(typeof(getGroupType)=="function"){
		type = getGroupType();
	}
	var url=management_api_server_servlet_path+"/device/group.json?type="+type;
	var data={}
	var result = ajaxSyncGet(url,data);
	if (result.code!=0) {
		showErrorMessage(result.message);
		return;
	}
	var tree=new Array();
	tree[0]=result.data;
	$("#group-tree").tree("loadData", tree);
}


function moveGroup(){
	var row = getDatagridSelectedRow();
	if (row) {
		$("#edit-dlg-move-group").dialog("open").dialog("setTitle","转移分组");
	}else{
		showMessage("请选择要转移分组的记录！");
	}
}

function saveMoveGroup(){
	 var nodes = $("#group-tree").tree("getChecked");
	 var ids = new Array();
	 var index=0;
	 for(var i=0; i<nodes.length; i++){
	    var node=nodes[i];
		if(!node.attributes.isParent) {
			ids[index++]=node.id.replace("permission_","");
		}
	 }
	 if(ids.length>1){
		 showErrorMessage("只能选择一个分组");
		 return;
	 }
	 
	 var row = getDatagridSelectedRow();
	 var data = {};
	 data.id = getRowId(row);
	 data.groupId= ids[0];
	 
	 var servletPath = management_api_server_servlet_path;
	 if(typeof(getServletPath)=="function"){
		 servletPath = getServletPath();
	 }
	 var url = servletPath +"/common/"+getApiName()+"/"+getRowId(row)+".json"
	 ajaxAsyncPatch(url,data,function(result){
		 $("#edit-dlg-move-group").dialog("close");
		 doQuery();
	 });
}
</script>