var carTypeMap = new JsMap();
$(function() {
	loadDictionary(dictionary_officers_car_type,function(list){
		$("#search-type").empty();
		$("#search-type").append("<option value=''>选择级别</option>");
		for (var i = 0, l = list.length; i < l; i++) {
			var item = list[i];
			carTypeMap.put(parseInt(item.code),item.content);
			$("#search-type").append("<option value='" + item.code + "'>" + item.content + "</option>");
		}
	});
	
	initBootstrapTable();
	initTree("device-group-tree");
	initCarGroupTree();
});

function getApiName() {
	return "car";
}

function getQueryParams() {
	var params = {};
	
	var plateNumber = $("#search-plateNumber").val();
	if (plateNumber) {
		params["plateNumber.like"] = plateNumber;
	}
	
	var type = $("#search-type").val();
	if (type) {
		params["type.eq"] = type;
	}
	
	var groupId = $("#search-frm-groupId").val();
	if (groupId) {
		params["groupId.eq"] = groupId;
	}
	
	var enterpriseId = $("#search-frm-enterpriseId").val();
	if (enterpriseId) {
		params["enterpriseId.eq"] = enterpriseId;
	}
	
	return params;
}

function getQueryUrl() {
	return management_api_server_servlet_path + "/common/query/carDevice.json?countable=true&isParent=true";
}

function getIdField(){
	return "carId";
}

function getColumns() {
	return [
			{
				checkbox : true,
				field : "check"
			},
			{
				field : "plateNumber",
				title : "车牌号"
			},
			{
				field : "simCode",
				title : "车载设备",
				formatter : function(value, row, index) {
					if(row.simCode){
						return row.simCode;
					}
					return "未绑定";
				}
			},
			{
				field : "bizState",
				title : "状态",
				formatter : function(value, row, index) {
					var state = row.bizState;
					return deviceBizStateMap.get(state);
				}
			},
			{
				field : "type",
				title : "车辆级别",
				formatter : function(value, row, index) {
					if(row.type){
						return carTypeMap.get(row.type+"");
					}
					return "";
				}
			},
			{
				field : "seats",
				title : "核载人数"
			},
			{
				field : "acquisitionDate",
				visible: false,
				title : "购置日期"
			},
			{
				field : "verificationDate",
				visible: false,
				title : "年审日期"
			},
			{
				field : "insuranceDate",
				visible: false,
				title : "保险到期"
			},
			{
				field : "createTime",
				visible: false,
				title : "创建时间"
			},{
				title : '操作',
				field : 'opear',
				formatter : function(value, row, index) {
					var html = "";
					//html = '<a href="javascript:void(0)" class="table_view" title="生命周期" onclick="view(\'' + row.carId + '\')">生命周期</a>';
					html += '<a href="javascript:void(0)" class="table_edit" title="编辑" onclick="edit(\'' + row.carId + '\')">编辑</a>';
					if(row.state!=9){
						html += '<a href="javascript:void(0)" class="table_del" title="停用" onclick="disable(\'' + row.carId + '\')">停用</a>';
					}else if(row.state==9){
						html += '<a href="javascript:void(0)" class="table_del" title="启用" onclick="enable(\'' + row.carId + '\')">启用</a>';
					}
					if(row.simCode){
						html += '<a href="javascript:void(0)" class="table_del" title="解绑车载" onclick="unbindDevice(\'' + row.carId + '\')">解绑</a>';
					}else{
						html += '<a href="javascript:void(0)" class="table_edit" title="绑定车载" onclick="bindDevice(\'' + row.carId + '\')">绑定</a>';
					}
					html += '<a   href="javascript:void(0)" class="table_del" title="删除" onclick="del(\'' + row.carId + '\')">删除</a>';
					return html;
				}
			} ];
}

function disable(id) {
	var row = getBootstrapTableRowById(id);
	setState(id, 9, "确定要停用该车辆？");
}

function enable(id) {
	var row = getBootstrapTableRowById(id);
	setState(id, 1, "确定要启用该车辆？");
}

function setState(id, state, message) {
	showConfirm(message, function() {
		var url = management_api_server_servlet_path + "/common/" + getApiName() + "/" + id + ".json";
		var data = {
			"state" : state
		};
		ajaxAsyncPatch(url, data, function(result) {
			if (result.code != 0) {
				showErrorMessage(result.message);
			} else {
				showMessage("设置成功");
				doQuery();
			}
		});
	});
}

function view(id){
	layer.open({
		  type: 2,
		  title: false,
		  closeBtn:0,
		  shadeClose: true,
		  shade: 0.8,
		  area: ['100%', '100%'],
		  content: "../officersCar/carInfoDetail.do?type=1&id="+id
		});
}

function edit(id){
	var row = getBootstrapTableRowById(id);
	var enterpriseId = row.enterpriseId;
	window.parent.parent.frames["content-frame"].location.href="editCar.do?id="+row.carId;
}

function add(){
	var enterpriseId = $("#search-frm-enterpriseId").val();
	var groupId = $("#search-frm-groupId").val();
	window.parent.parent.frames["content-frame"].location.href="editCar.do?enterpriseId="+enterpriseId+"&groupId="+groupId;
}

function dels() {
	var rows = getBootstrapTableSelectedRows();
	if (rows.length > 0) {
		var ids = "";
		for (var i = 0; i < rows.length; i++) {
			ids += "," + rows[i].carId;
		}
		del(ids.substr(1));
	} else {
		showMessage("请选择要删除的记录！");
	}
}

function getInitTreeNodes() {
	return getDefaultGroupTree(1);
}

function onExpandTreeNode(treeId, parent){
	onExpandGroupTreeNode(1,treeId, parent);
}

function onClickTreeNode(treeId, node){
	var params = {};
	var enterpriseId = "";
	var groupId = "";
	if (node.type && node.type == 'ent') {
		enterpriseId = node.id;
	}else {
		groupId = node.id;
	}
	queryAvailableDevice(enterpriseId,groupId, 1, 100);
}

function queryAvailableDevice(enterpriseId,groupId, pageNo, pageSize){
	var url = management_api_server_servlet_path + "/device/unbind.json?enterpriseId="+enterpriseId+"&groupId="+groupId+"&pageSize=" + pageSize + "&pageNo=" + pageNo;
	var result = ajaxSyncGet(url, {});
	if (result.code != 0) {
		showErrorMessage(result.message);
	} else {
		var list = result.data;
		if (list && list.length > 0) {
			var map = new JsMap();
			for (var i = 0; i < list.length; i++) {
				var item = list[i];
				map.put(item.id+"_"+item.enterpriseId, item.name);
			}
		
			$("#multiple-select-source option").map(function() {
				var id = $(this).val();
				var name = $(this).text();
				map.put(id, name);
			});
		
			var keys = map.getKeys();
			$('#multiple-select-source').empty();
			for (var i = 0, l = keys.length; i < l; i++) {
				var key = keys[i];
				$("#multiple-select-source").append("<option value='" + key + "'>" + map.get(key) + "</option>");
			}
		
			if (list.length == pageSize) {
				pageNo++;
				queryAvailableDevice(enterpriseId,groupId, pageNo, pageSize);
			}
		}
	}
}

var select_tree_type = 0;
function importCar(){
	select_tree_type = 0;
	showCommondDialog("officers-car-dlg");
}

function saveOfficersCar(){
	var devices = [];
	$("#multiple-selected-target option").map(function() {
		devices.push({"name":$(this).text(),"val":$(this).val()});
	});
	
	if(devices.length == 0){
		showMessage("请选择车载设备");
		return;
	}
	
	if(select_tree_type==1){
		//绑定设备
		if(devices.length > 1){
			showMessage("只能绑定一个车载设备");
			return;
		}
		var val = devices[0].val;
		var url = management_api_server_servlet_path + "/car/"+bind_carId+"/device/"+val.split("_")[0]+".json";
		ajaxAsyncPatch(url, {},function(result){
			if (result.code != 0) {
				showErrorMessage(result.message);
			} else {
				doQuery();
				showMessage("绑定成功");
			}
		});
	}else{
		var url = management_api_server_servlet_path + "/common/" + getApiName() + ".json";
		var data = {};
		data.seats = 5;
		data.bizType = 1;
		for (var i = 0; i < devices.length; i++) {
			var item = devices[i];
			var arr = (item.val).split("_");
			data.deviceId = arr[0];
			data.plateNumber = item.name;
			data.enterpriseId = arr[1];
			ajaxSyncPost(url, data);
		}
		doQuery();
		showMessage("设置成功");
	}
	closeDialog();
}

function unbindDevice(carId){
	showConfirm("确定要解绑车辆的车载设备？", function() {
		var url = management_api_server_servlet_path + "/car/"+carId+"/device.json";
		ajaxAsyncPatch(url, {},function(result){
			if (result.code != 0) {
				showErrorMessage(result.message);
			} else {
				doQuery();
				showMessage("解绑成功");
			}
		});
	});
}

var bind_carId;
function bindDevice(carId){
	bind_carId = carId;
	select_tree_type = 1;
	showCommondDialog("officers-car-dlg");
}


function initCarGroupTree() {
	var setting = {
			check : {
				enable : false,
			},
			callback : {
				onExpand : onExpandCarGroupTreeNode
			}
	 }
	var nodes = getDefaultGroupTree(group_type_car);
	$.fn.zTree.init($("#car-group-tree"), setting, nodes);
}

function onExpandCarGroupTreeNode(event, treeId, parent){
	onExpandGroupTreeNode(group_type_car,treeId, parent);
}

function moveGroup() {
	var rows = getBootstrapTableSelectedRows();
	if (rows.length > 0) {
		showCommondDialog("edit-dlg-move-group");
	} else {
		showMessage("请选择要转分组的车辆！");
	}
}

function saveMoveGroup() {
	var treeId = "car-group-tree";
	var nodes = getSelectedNodes(treeId);
	var ids = new Array();
	var index = 0;
	var node;
	for (var i = 0; i < nodes.length; i++) {
		node = nodes[i];
		ids[index++] = node.id;
	}
	if (ids.length > 1 || ids.length == 0 || (node.type && (node.type=="ent" || node.type=="root"))) {
		showErrorMessage("只能选择一个分组");
		return;
	}
	
	var enterpriseId = "";
	var groupId = node.id;
	
	var parent = node.getParentNode();
	while(parent && parent.type && parent.type!='ent'){
		parent = parent.getParentNode();
	}
	if(parent && parent.type =='ent'){
		enterpriseId = parent.id;
	}
	
	var rows = getBootstrapTableSelectedRows();
	for (var i = 0; i < rows.length; i++) {
		var row = rows[i]
		var data = {};
		data.id = row.carId;
		data.groupId= groupId;
		data.enterpriseId= enterpriseId;
		var url = management_api_server_servlet_path + "/common/" + getApiName() + "/" + data.id + ".json";
		ajaxAsyncPatch(url, data, function(result) {
			if (result.code != 0) {
				showErrorMessage(result.message);
			} else {
				doQuery();
			}
		});
	}
	closeDialog();
}