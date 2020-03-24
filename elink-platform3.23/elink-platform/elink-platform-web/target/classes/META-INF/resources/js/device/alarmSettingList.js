var MAP_DIV = "map_box";

$(function() {
	initBootstrapTable();
});

function getApiName() {
	return "deviceAlarmSetting";
}

function getQueryParams() {
	var params = {};
	var type = $("#search-type").val();
	if (type) {
		params["type.eq"] = type;
	}
	params["bizType.eq"] = 1;
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
				field : "deviceName",
				title : "设备名称",
				formatter : function(value, row, index) {
					var deviceId = row.deviceId;
					return getDeviceName(deviceId);
				}
			},
			{
				field : "name",
				title : "围栏名称",
			},
			{
				field : "type",
				title : "围栏类型",
				formatter : function(value, row, index) {
					var type = row.type;
					if (type == 1) {
						return "原地设防";
					} else if (type == 2) {
						return "围栏";
					} else if (type == 3) {
						return "超速";
					} else if (type == 4) {
						return "路线";
					}
				}
			},
			{
				field : "startTime",
				title : "开始时间"
			},
			{
				field : "endTime",
				title : "结束时间"
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


function view(id){
	var row = getBootstrapTableRowById(id);
	var type=row.type;
	if(type==1){
		showRail(row);
	}else if(type==2){
		showRail(row);
	}else if(type==3){
		showOverspeed(row);
	}else if(type==4){
		showRoute(row);
	}
}

var weekMap= new JsMap();
weekMap.put("0","日");
weekMap.put("1","一");
weekMap.put("2","二");
weekMap.put("3","三");
weekMap.put("4","四");
weekMap.put("5","五");
weekMap.put("6","六");

function showOverspeed(row){
	var item=$.evalJSON(row.setting);
	 var text="围栏名称："+row.name+"<br>";
	 text +="围栏类型：超速告警<br>";
	 if(item.speed){
		 text +="限速："+item.speed+"km/h<br>";
	 }
	 if(item.startHour){
		 var startHour=item.startHour<10?"0"+item.startHour+":00":item.startHour+":00";
		 var endHour=item.endHour<10?"0"+item.endHour+":59":item.endHour+":59";
		 text +="有效时段："+startHour+"-"+endHour;
	 }
	 text ="<div class='amap-info-window-content'>"+text+"</div>";
	 mapUtil.clearOverlays();
	 mapUtil.addLabel(mapUtil.getCenter(),text);
	 mapUtil.getMap().setFitView();
	 showCommondDialog("map-dlg");
}

function showRail(row){
	 var setting=row.setting;
	 var item=$.evalJSON(setting);
	 var type="";
	 if(row.type==1){
		 type="原地设防";
	 }else if(item.inside==0){
		 type="出围栏告警";
	 }else if(item.inside==1){
		 type="进围栏告警";
	 }else if(item.inside==2){
		 type="围栏超速告警";
	 }
	 var text="围栏名称："+row.name+"<br>";
	 text +="围栏类型："+type+"<br>";
	 if(item.speed){
		 text +="限速："+item.speed+"km/h<br>";
	 }
	 if(item.week){
		 var weeks=item.week;
		 var weekstr="";
		 if(weeks&&weeks.length>0){
			for(var j=0;j<weeks.length;j++){
				weekstr+=","+weekMap.get(weeks[j]);
			}
			weekstr=weekstr.substr(1)
		 }
		 text +="有效星期："+weekstr+"<br>";
	 }
	 if(item.startHour){
		 var startHour=item.startHour<10?"0"+item.startHour+":00":item.startHour+":00";
		 var endHour=item.endHour<10?"0"+item.endHour+":59":item.endHour+":59";
		 text +="有效时段："+startHour+"-"+endHour;
	 }
	 text ="<div class='amap-info-window-content'>"+text+"</div>";
	 mapUtil.clearOverlays();
	 if(item.shape==1 || row.type==1){
		var gcj02 = LngLatConverter.wgs84togcj02(item.lng,item.lat);
		var point=mapUtil.getPoint(gcj02[0],gcj02[1]);
		var circle=mapUtil.addCircle(point,item.radius);
		mapUtil.addLabel(circle.getCenter(),text);
	 }else if(item.shape==4){
		var bounds = item.bounds;
		var point;
		for(var j=0;j<bounds.length;j++){
			var arr = bounds[j].split(";");
			var path = new Array();
			for(var k=0,l=arr.length;k<l;k++){
				var point = arr[k].split(",");
				var gcj02 = LngLatConverter.wgs84togcj02(point[0],point[1]);
				point=mapUtil.getPoint(gcj02[0],gcj02[1]);
				path.push(point);
			}
			mapUtil.addPolygon(path);
		}
		mapUtil.addLabel(point,text);
	 }else{
		var arr = item.path.split(";");
		var path = new Array();
		for(var j=0;j<arr.length;j++){
			var point = arr[j].split(",");
			var gcj02 = LngLatConverter.wgs84togcj02(point[0],point[1]);
			path.push(mapUtil.getPoint(gcj02[0],gcj02[1]));
		}
		var polygon=mapUtil.addPolygon(path);
		mapUtil.addLabel(polygon.getBounds().getCenter(),text);
   }
	 mapUtil.getMap().setFitView();
	  showCommondDialog("map-dlg");
}

function showRoute(row){
	 var setting=row.setting;
	 var item=$.evalJSON(setting);
	 var type="";
	 if(row.type==1){
		 type="原地设防";
	 }else if(item.inside==0){
		 type="出围栏告警";
	 }else if(item.inside==1){
		 type="进围栏告警";
	 }else if(item.inside==2){
		 type="围栏超速告警";
	 }
	 var text="围栏名称："+row.name+"<br>";
	 text +="围栏类型："+type+"<br>";
	 if(item.speed){
		 text +="限速："+item.speed+"km/h<br>";
	 }
	 if(item.week){
		 var weeks=item.week;
		 var weekstr="";
		 if(weeks&&weeks.length>0){
			for(var j=0;j<weeks.length;j++){
				weekstr+=","+weekMap.get(weeks[j]);
			}
			weekstr=weekstr.substr(1)
		 }
		 text +="有效星期："+weekstr+"<br>";
	 }
	 if(item.startHour){
		 var startHour=item.startHour<10?"0"+item.startHour+":00":item.startHour+":00";
		 var endHour=item.endHour<10?"0"+item.endHour+":59":item.endHour+":59";
		 text +="有效时段："+startHour+"-"+endHour;
	 }
	 text ="<div class='amap-info-window-content'>"+text+"</div>";
	 mapUtil.clearOverlays();
	 var arr = item.route.split(";");
	 var points=[];
	 for(var i=0;i<arr.length;i++){
		var a=arr[i].split(",");
		var gcj02 = LngLatConverter.wgs84togcj02(a[0],a[1]);
		var point=mapUtil.getPoint(gcj02[0],gcj02[1]);
		points.push(point);
	 }
	 mapUtil.addLabel(points[parseInt(points.length/2)],text);
	 mapUtil.drawPolyline(points,"red");
	 mapUtil.getMap().setFitView();
	 showCommondDialog("map-dlg");
}