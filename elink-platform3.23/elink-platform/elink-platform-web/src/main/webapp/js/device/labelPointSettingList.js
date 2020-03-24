var MAP_DIV = "map_box";

$(function() {
	initBootstrapTable();
});

function getApiName() {
	return "labelPoint";
}

function getQueryParams() {
	var params = {};
	var bizType = $("#search-bizType").val();
	if (bizType) {
		params["bizType.eq"] = bizType;
	}
	return params;
}

function getQueryUrl() {
	return message_api_server_servlet_path + "/common/query/" + getApiName()
			+ ".json?countable=true";
}

function getColumns() {
	return [
			{
				field : 'check',
				checkbox : true
			},
			{
				field : "name",
				title : "标注名称"
			},
			{
				field : "type",
				title : "标注类型",
				formatter : function(value, row, index) {
					var type = row.type;
					if (type == 1) {
						return "点";
					} else if (type == 2) {
						return "圆形";
					} else if (type == 3) {
						return "矩形";
					} else if (type == 4) {
						return "多边形";
					} else if (type == 5) {
						return "路线";
					}
				}
			},
			{
				field : "bizType",
				title : "业务类型",
				formatter : function(value, row, index) {
					var type = row.bizType;
					if (type == 1) {
						return "停车场";
					} else if (type == 2) {
						return "途径点";
					} else if (type == 3) {
						return "站点";
					} else if (type == 4) {
						return "区域查车";
					} else if (type == 5) {
						return "电子围栏";
					} else if (type == 6) {
						return "行驶路线";
					}
				}
			},
			{
				field : "createTime",
				title : "设置时间"
			},
			{
				title : '操作',
				field : 'opear',
				formatter : function(value, row) {
					var html = '<a   href="javascript:void(0)" class="table_view" title="查看" onclick="view(\''
							+ row.id + '\')">查看</a>';
					html += '<a   href="javascript:void(0)" class="table_del" title="删除" onclick="del(\''
							+ row.id + '\')">删除</a>';
					return html;
				}
			} ];
}

function view(id) {
	var row = getBootstrapTableRowById(id);
	var type = row.type;
	if (type == 5) {
		showRoute(row);
	} else {
		showRail(row)
	}
}

function showRail(row) {
	var setting = row.setting;
	var item = $.evalJSON(setting);
	mapUtil.clearOverlays();
	if (row.type == 1 ){//点
		var gcj02 = LngLatConverter.wgs84togcj02(item.lng, item.lat);
		var point = mapUtil.getPoint(gcj02[0], gcj02[1]);
		mapUtil.addMarker(row.id,point,"");
	} else if(row.type == 2) {// "圆形";
		if (!item.radius) {
			item.radius = 100;
		}
		var gcj02 = LngLatConverter.wgs84togcj02(item.lng, item.lat);
		var point = mapUtil.getPoint(gcj02[0], gcj02[1]);
		var circle = mapUtil.addCircle(point, item.radius);
	} else if (row.type == 3 || row.type == 4) {// "矩形"、"多边形";
		var arr = item.path.split(";");
		var path = new Array();
		for (var j = 0; j < arr.length; j++) {
			var point = arr[j].split(",");
			var gcj02 = LngLatConverter.wgs84togcj02(point[0], point[1]);
			path.push(mapUtil.getPoint(gcj02[0], gcj02[1]));
		}
		mapUtil.addPolygon(path);
	}
	mapUtil.getMap().setFitView();
	showCommondDialog("map-dlg");
}

function showRoute(row) {
	var setting = row.setting;
	var item = $.evalJSON(setting);
	mapUtil.clearOverlays();
	var arr = item.route.split(";");
	var points = [];
	for (var i = 0; i < arr.length; i++) {
		var a = arr[i].split(",");
		var gcj02 = LngLatConverter.wgs84togcj02(a[0], a[1]);
		var point = mapUtil.getPoint(gcj02[0], gcj02[1]);
		points.push(point);
	}
	mapUtil.drawPolyline(points, "red");
	mapUtil.getMap().setFitView();
	showCommondDialog("map-dlg");
}