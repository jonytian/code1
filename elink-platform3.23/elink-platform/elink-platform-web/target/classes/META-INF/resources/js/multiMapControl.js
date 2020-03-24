var MAP_TYPE = 1, MAP_DIV = "map_box";
var current_selected_device_index = 0;
var current_selected_device = [];

$(function() {
	initBootstrapTable();
	initTree("car-tree");
	rightChange();
	setHeight();
});

function setHeight(){
	var height = $("#content-div").height()-$("#map_right_top").outerHeight(true);
	$("#real-time-message-div").height(height-30);
	$("#car-tree-div").height(height);
}

function showPaginationDetail() {
	return false;
}

function showPaginationSwitch() {
	return false;
}

function showToolbar() {
	return false;
}

function getQueryParams() {
	var params = {};
	var name = $("#search-plateNumber").val();
	if (name) {
		params["plateNumber.like"] = name;
	}
	params["state.gt"] = 0;
	params["deviceState.eq"] = 3;
	return params;
}

function getQueryUrl() {
	return "/common/query/carDevice.json?select=carId,deviceId,plateNumber,deviceState,bizState&countable=true&isParent=true";
}

function getColumns() {
	return [ {
		field : 'check',
		checkbox : true
	}, {
		title : "车牌号",
		field : 'name',
		align : 'center',
		valign : 'middle',
		formatter : function(value, row, index) {
			return row[2];
		}
	}, {
		title : "状态",
		field : "state",
		align : 'center',
		formatter : function(value, row, index) {
			var bizState = row[4];
			return deviceBizStateMap.get(bizState);
		}
	} ];
}

function addRealTimeMessage(content) {
	$("#real-time-message-div").append(content);
}

// 右侧功能界面切换
function rightChange() {
	$(".map_right_top>ul>li").click(function() {
		var ins = $(this).index();
		$(this).addClass("li_active").siblings().removeClass("li_active");
		$(".map_con .map_con_div").eq(ins).show().siblings().hide();
	})
}

function getCars(enterpriseId,groupId,pageSize,pageNo){
	var url = management_api_server_servlet_path + "/common/query/carDevice.json?select=carId,deviceId,plateNumber,bizState,vedioChannel&countable=false&pageSize="+ pageSize + "&pageNo=" + pageNo + "&orderBy=createTime&desc=true&isParent=true";
	var params = {};
	if(enterpriseId){
		params["enterpriseId.eq"] = enterpriseId;
	}
	if(groupId){
		params["groupId.eq"] = groupId;
	}else{
		params["groupId.null"] = "null";
	}

	params["deviceState.eq"] = 3;
	var result = ajaxSyncPost(url, params);
	if (result.code != 0) {
		showErrorMessage(result.message);
	} else {
	   return result.data;
	}
}

function addCarTreeNodes(treeId, parent, pageNo, pageSize) {
	var enterpriseId = "",groupId="";
	if(parent.type && parent.type == "ent"){
		enterpriseId = parent.id;
	}else{
		groupId = parent.id;
	}

	var list = getCars(enterpriseId,groupId,pageSize,pageNo);
	if (list && list.length > 0) {
		var treeNodes = [];
		for (var i = 0, l = list.length; i < l; i++) {
			var item = list[i];
			var node = {};
			node.id = item[1];
			node.name = item[2];
			node.icon = getCarTreeStateIcon(item[3]);
			node.bizState = item[3];
			treeNodes.push(node);
		}
		addTreeNodes(treeId,parent, treeNodes);
		if (list.length == pageSize) {
			pageNo++;
			addCarTreeNodes(treeId, parent, pageNo, pageSize)
		}
	}
}

function getInitTreeNodes() {
	var root =  getDefaultGroupTree(group_type_car);
	var children = root.children;
	var list = getCars(getUserEnterpriseId(),null,10000,1);
	if (list && list.length > 0) {
		for (var i = 0, l = list.length; i < l; i++) {
			var item = list[i];
			var node = {};
			node.id = item[1];
			node.name = item[2];
			node.icon = getCarTreeStateIcon(item[3]);
			node.bizState = item[3];
			children.push(node);
		}
	}
	root.children = children;
	return root;
}

function onExpandTreeNode(treeId, node) {
	onExpandGroupTreeNode(group_type_car,treeId, node);

	var children = node.children;
	if (children && children.length > 0) {
		// 车辆分组是否已经加载车辆
		for (var i = 0, l = children.length; i < l; i++) {
			var item = children[i];
			if (typeof(item.bizState) != 'undefined') {
				return;
			}
		}
	}
	
	addCarTreeNodes(treeId, node, 1, 100);
}

function onClickTreeNode(treeId, node) {
	if (!node.isParent) {
		onSelectDevice(node.id);
	}
}

function onCheckBootstrapTableRow(row) {
	onSelectDevice(row[1]);
}

function onCheckAllBootstrapTableRow(rows) {
	if(rows.length>4){
		showMessage("最多支持同时监控四辆车！");
	}
	for (var i = 0; i < rows.length && i < 4; i++) {
		var row = rows[i];
		onSelectDevice(row[1]);
	}
}

function onSelectDevice(deviceId){
	for(var i=0;i<current_selected_device.length;i++){
		if(current_selected_device[i]==deviceId){
			return;
		}
	}
	
	if(current_selected_device_index >= 4){
	   current_selected_device_index=0;
	}
	
	current_selected_device[current_selected_device_index]=deviceId;
	
	current_selected_device_index++;
	var frame = $("#map-frame"+current_selected_device_index)[0].contentWindow;
	frame.startFollowCar(deviceId,5);
}