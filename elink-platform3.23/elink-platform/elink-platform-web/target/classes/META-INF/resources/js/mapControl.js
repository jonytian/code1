var MAP_TYPE = 1, MAP_DIV = "map_box";
var speedDashboardChart,speedDashboardChartOption;
var current_dialog_id;
var downMessageLimitMap = new JsMap();

var deviceListMap = new JsMap();

$(function() {
	showToolbar();
	initBootstrapTable();
	initTree("car-tree");
	mapActive();
	rightChange();
	initDatetimepicker();
	initDatepicker();
	setHeight();
	loadDistrict("8888","province");
	
	//五分钟统计一次车辆运行状态
	deviceBizstateOverView();
	setInterval(function() {
		deviceBizstateOverView();
	}, 5 * 60 * 1000);
});

function setHeight(){
	var height = $("#content-div").height()-$("#map_right_top").outerHeight(true);
	$("#real-time-message-div").height(height-30);
	$("#car-tree-div").height(height);
}

function onLoadMapSuccess() {
	mapUtil.getDistrictSearch().search("中国", function(status, result) {
		if (status == "complete") {
			// setDistrictSearchResult(result);
			setDistrictRailSearchResult(result);
		}
	});
}

function showCmdDialog(dialogId) {
	if (current_selected_device) {
		current_dialog_id = dialogId;
		$("#" + dialogId + "-frm-name").val(getDeviceName(current_selected_device.id));
		$("#" + dialogId + "-frm-deviceId").val(current_selected_device.id);
		var title = $("#" + dialogId).attr("title");
		showDialog(title, dialogId);
	} else {
		showMessage("请选择要操作的车辆！");
	}
}

function onClickMoreCmd(){
	if(!current_selected_device){
		$("li[name ='li-cmd-menu-jtt-808-2013-2019-tjsatl']").hide();
		showMessage("请选择要操作的车辆！");
		return;
	}

	var protocolVersion = current_selected_device.protocolVersion;
	$("li[name^='li-cmd-menu-']").each(function(){
        var name=$(this).attr("name");
        var hide = false;
        if(!downMessageLimitMap.isEmpty()){
	        var toolbar = $(this).attr("toolbar");
	        if(toolbar){
	        	var messageId = toolbar.substr(-4);
	        	if(downMessageLimitMap.get(messageId)!=null){
	        	}else{
	        		hide = true;
	        	}
	        }
        }
        if(name.indexOf(protocolVersion)!=-1 && !hide){
        	$(this).show();
        }else{
        	$(this).hide();
        }
	});
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
	if ($("#search-state").is(":checked")) {
		params["deviceState.eq"] = 3;
	} else {
		params["deviceState.gte"] = 2;
		params["deviceState.lte"] = 3;
	}
	
	deviceListMap = new JsMap();
	
	return params;
}

function getQueryUrl() {
	return "/common/query/carDevice.json?select=carId,deviceId,plateNumber,deviceState,bizState&countable=true&isParent=true";
}

function onLoadBootstrapTableDataSuccess(){
	var rows = $('#boot-strap-table').bootstrapTable('getData');
	for(var i=0,l=rows.length;i<l;i++){
		var row = rows[i];
		if(selectedDeviceMarkerMap.get(row[1])){
			$('#boot-strap-table').bootstrapTable('check', i);
		}
	}
}

function getColumns() {
	return [ {
		field : 'check',
		checkbox : true
	}, {
		title : "车牌号",
		field : 'plateNumber',
		align : 'center',
		valign : 'middle',
		formatter : function(value, row, index) {
			deviceListMap.put(row[1],{index:index,row:row});
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
	$("#real-time-message-div").prepend(content);
}

// 工具条点击效果
function mapActive() {
	$(".map_top>ul>li").click(
			function() {
				$(this).addClass("active").siblings().removeClass("active");
				$(this).find("a").addClass("active");
				$(this).find("a").parents("li").siblings().find("a")
						.removeClass("active");
			})
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

	params["state.gt"] = 0;
	params["deviceState.gte"] = 2;
	params["deviceState.lte"] = 3;
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
	for (var i = 0; i < rows.length; i++) {
		var row = rows[i];
		onSelectDevice(row[1]);
	}
}

function onUncheckBootstrapTableRow(row){
	removeDeviceMarker(row[1]);
}

function onUncheckAllBootstrapTableRow(rows) {
	for (var i = 0; i < rows.length; i++) {
		var row = rows[i];
		removeDeviceMarker(row[1]);
	}
}

function clearOverlays() {
	mapUtil.clearOverlays();
	// 重新添加选中的设备
	//addDeviceMarker();
	// setMarkerLabel();
	clearPathNavigators();
	//cleanSearchAreaDevicesResults();
}

function initSpeedDashboardCharts(){
    speedDashboardChart = echarts.init(document.getElementById("speedDashboard_charts_div"));
    speedDashboardChartOption = {
		    series: [
		        {
		            name:'速度',
		            type:'gauge',
		            min:0,
		            max:180,
		            splitNumber:6,
		            radius: '80px',
		            axisLine: {            // 坐标轴线
		                lineStyle: {       // 属性lineStyle控制线条样式
		                    color: [[0.16, 'lime'],[0.67, '#1e90ff'],[1, '#ff4500']],
		                    width:3,
		                    shadowColor : '#fff', //默认透明
		                    shadowBlur: 8
		                }
		            },
		            detail : {
		                offsetCenter: [0, '50%']
		            },
		            data:[{value:0}]
		        }
		    ]
		};
    speedDashboardChart.setOption(speedDashboardChartOption);
}

function resetSpeedDashboardChart(speed){
	if(!speedDashboardChartOption){
		initSpeedDashboardCharts();
	}
	speedDashboardChartOption.series[0].data[0].value = speed;
	speedDashboardChart.setOption(speedDashboardChartOption);
}

var isDeviceBizTypeOverViewQuerying = false;

function deviceBizstateOverView(){
	if(isDeviceBizTypeOverViewQuerying){
		return ;
	}
	isDeviceBizTypeOverViewQuerying = true;
	var url = report_api_server_servlet_path + "/deviceBizTypeOverView.json?isParent=true";
	ajaxAsyncGet(url, {}, function(result) {
		isDeviceBizTypeOverViewQuerying = false;
		if (result.code != 0) {
			showErrorMessage(result.message);
		} else {
			for(var i=1;i<5;i++){
				$("#car-bizState-total-"+i).html("&nbsp;&nbsp;0辆");
			}
			var list = result.data;
			var stop = 0;
			for(var i=0,l=list.length;i<l;i++){
				var item = list[i];
				var bizState = item.bizState;
				var total = item.total;
				var obj = $("#car-bizState-total-"+bizState).html();
				if(bizState && obj){
					$("#car-bizState-total-"+bizState).html("&nbsp;&nbsp;"+total+"辆");
				}else{
					stop += total;
				}
			}
			$("#car-offline-total").html("&nbsp;&nbsp;"+stop+"辆");
		}
	});
}


function onlineMessageHandler(messageBody){
	var deviceId = messageBody.deviceId;
	var state = messageBody.state;
	var marker = selectedDeviceMarkerMap.get(deviceId);
	if (marker) {
		var iconUrl = getCarStateIcon(state);
		mapUtil.setMarkerIcon(marker,iconUrl);
	}
	if(state==0 && current_selected_device && current_selected_device.id == deviceId){
		stopFollowCar();
		//停止视频监控
		offlineVideoClean(deviceId);
	}

	if(current_selected_device && deviceId == current_selected_device.id){
		current_selected_device.state = state;
		current_selected_device.bizState = state;
	 }

	//更新树节点
	var tree = getTree("car-tree");
	var node = tree.getNodeByParam("id",deviceId);
	if(node){
		node.icon = getCarTreeStateIcon(state);
		tree.updateNode(node);
	}
	
	var rowItem = deviceListMap.get(deviceId);
	if(rowItem){
		var row = rowItem.row;
		row[4]=state;
		$('#boot-strap-table').bootstrapTable('updateRow', {index: rowItem.index, row: row})
	}
	
	deviceBizstateOverView();
}

function bizStateMessageHandler(message){
	var bizState = message.bizState;
	var deviceId = message.deviceId;
	var marker = selectedDeviceMarkerMap.get(deviceId);
	if (marker) {
		var iconUrl = getCarStateIcon(bizState);
		mapUtil.setMarkerIcon(marker,iconUrl);
	}
	
	//更新车辆缓存
	if(current_selected_device && deviceId == current_selected_device.id){
		current_selected_device.bizState = bizState;
	}

	//更新树节点
	var tree = getTree("car-tree");
	var node = tree.getNodeByParam("id",deviceId);
	if(node){
		node.icon = getCarTreeStateIcon(bizState);
		tree.updateNode(node);
	}
}

function hideCarInfoOverviewDiv(){
	$("#car_info_overview_div").hide();
}

function showToolbar(){
	var url = management_api_server_servlet_path + "/common/enterpriseConfig/" + getUserEnterpriseId() + ".json";
	var data = {}
	ajaxAsyncGet(url, data, function(result) {
		if (result.code != 0) {
			showErrorMessage(result.message);
		} else {
			var data = result.data;
			var downMessageLimit=data.downMessageLimit;
			if(downMessageLimit){
				var arr = downMessageLimit.split(",");
				if(arr.length > 0){
					$("li[toolbar^='map-toolbar-cmd-']").each(function(){
				        $(this).hide();
					});
				}
				for(var i=0;i<arr.length;i++){
					var messageId = arr[i];
					downMessageLimitMap.put(messageId,messageId);
					$("li[toolbar='map-toolbar-cmd-"+messageId+"']").show();
				}
			}
		}
	});
}

function showTodoAlarm(){
	var url = "device/alarmList.do?state=1";
	showWindows(url);
}