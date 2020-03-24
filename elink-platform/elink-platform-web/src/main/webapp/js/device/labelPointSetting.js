var current_label_point_setting ={};
var is_label_point_type_point_set=false;
var map_onclick_event_lnglat,current_label_point;

var typeMap = new JsMap();
var bizTypeMap = new JsMap();
bizTypeMap.put(1,"停车场");
bizTypeMap.put(2,"途径点");
bizTypeMap.put(3,"站点");
typeMap.put(1,bizTypeMap);

bizTypeMap = new JsMap();
bizTypeMap.put(4,"区域查车");
bizTypeMap.put(5,"电子围栏");
typeMap.put(2,bizTypeMap);
typeMap.put(3,bizTypeMap);
typeMap.put(4,bizTypeMap);

bizTypeMap = new JsMap();
bizTypeMap.put(6,"行驶路线");
typeMap.put(5,bizTypeMap);


function drawLabelPoint(type){
	mapUtil.closeDraw();
	mapUtil.openDraw();
	var message = "";
	if(type==1){
		is_label_point_type_point_set = true;
		message = "画点";
	}else if(type==2){
		message = "画圆形";
		mapUtil.drawCircle(drawLabelPointCircleCompletedHandler);
	}else if(type==3){
		message = "画矩形";
		mapUtil.drawRectangle(drawLabelPointPolygonCompletedHandler);
	}else if(type==4){
		message = "画多边形";
		mapUtil.drawPolygon(drawLabelPointPolygonCompletedHandler);
	}else if(type==5){
		message = "采集路线";
		if(current_selected_device){
			showCmdDialog("label-point-setting-gps-history-query-dlg");
		}else{
			showMessage("请选择车辆进行轨迹采集！");
			return ;
		}
	}
	$("#label-point-setting-dlg-frm-type").val(type);
	var bizTypeMap = typeMap.get(type);
	$("#label-point-setting-dlg-frm-bizType").empty(); 
	var keys = bizTypeMap.getKeys();
	for(var i=0;i<keys.length;i++){
		var key = keys[i];
		$("#label-point-setting-dlg-frm-bizType").append("<option value='"+key+"'>"+bizTypeMap.get(key)+"</option>");
	}
	
	if(current_label_point){
		mapUtil.removeOverlay(current_label_point);
	}

	showMessage("请在地图上"+message+"！");
}

function mapOnClickEvent(e){
	map_onclick_event_lnglat = e.lnglat;
	drawLabelPointPointCompletedHandler();
}

function drawLabelPointPointCompletedHandler(){
	 var wgs84 = LngLatConverter.gcj02towgs84(map_onclick_event_lnglat.getLng(), map_onclick_event_lnglat.getLat());
	 var setting = {};
	 setting.lat = wgs84[1];
	 setting.lng = wgs84[0];
     if(is_label_point_type_point_set){
         var marker = new AMap.Marker({
             position: [map_onclick_event_lnglat.getLng(),map_onclick_event_lnglat.getLat()]
         });
         marker.setMap(mapUtil.getMap());
         current_label_point = marker;
         current_label_point_setting = setting;
         is_label_point_type_point_set = false;
         showCommondDialog("label-point-setting-dlg");
     }
}

function drawLabelPointCircleCompletedHandler(e, circle){
	circle=e.obj;
	current_label_point=circle;
	var point=circle.getCenter();
	// 国测局坐标转wgs84坐标
    var wgs84 = LngLatConverter.gcj02towgs84(point.getLng(), point.getLat());
	var setting = {};
	setting.radius = parseInt(circle.getRadius());
	setting.lat = wgs84[1];
	setting.lng = wgs84[0];
	current_label_point_setting = setting;
	mapUtil.closeDraw();
	showCommondDialog("label-point-setting-dlg");
}

function drawLabelPointPolygonCompletedHandler(e, polygon){
	polygon=e.obj;
	current_label_point=polygon;
	var path=polygon.getPath();
	var points = "";
	// 国测局坐标转wgs84坐标
	for(var i=0;i<path.length;i++){
		var point = path[i];
		var wgs84 = LngLatConverter.gcj02towgs84(point.getLng(), point.getLat());
		points+=";"+wgs84[0]+","+wgs84[1];
	}
	var setting = {};
	setting.path = points.substr(1);
	current_label_point_setting = setting;
	mapUtil.closeDraw();
	showCommondDialog("label-point-setting-dlg");
}

function collectRoute(){
	var form = $("#label-point-setting-gps-history-query-dlg-frm");
	if (!form.valid()) {
		return;
	}
	var formData = form.serializeObject();
	
	var data = {};
	data["recordDate"]=formData.date;
	var rangeCondition ={};
	rangeCondition["fieldName"]="gpsTime"; 
	rangeCondition["from"]=new Date((formData.date+" "+formData.startTime).replace(/-/g,"/")).getTime();
	rangeCondition["includeLower"]=true;
	rangeCondition["includeUpper"]=true; 
	rangeCondition["to"]=new Date((formData.date+" "+formData.endTime).replace(/-/g,"/")).getTime();
	var rangeConditions= new Array();
	rangeConditions[0]=rangeCondition;
	data["rangeConditions"]=rangeConditions;
	var url=lbs_api_server_servlet_path+"/gps/"+current_selected_device.id+"/simple.json";
	ajaxAsyncPost(url,data,collectRouteResultHandler);
}

function collectRouteResultHandler(result){
	if (result.code!=0) {
		showErrorMessage(result.message);
		return
	}
	var data = result.data;
	if(!data || !data.points || data.points.length <= 0){
		showMessage("您所查询的时间段内无该设备的定位数据");
		return;
	}
	var gpsList = data.points;
	// 地图上画轨迹
	var map=mapUtil.getMap();
	var points=new Array();
	var index=0;
	var gpsStr = "";
	for(var i=0,l=gpsList.length;i<l;i++){
		var item=gpsList[i];
		var gcj02 = LngLatConverter.wgs84togcj02(item.lng,item.lat);
		var point=mapUtil.getPoint(gcj02[0],gcj02[1]);
		points[index++]=point;
		gpsStr+=";"+item.lng+","+item.lat;
	}

	if(points.length>0){
		map.panTo(points[0]);
		current_label_point = mapUtil.drawPolyline(points,"red");
	}
	
	var setting = {};
	setting.route = gpsStr.substr(1);
	setting.width = 100;// 路宽
	setting.maxDistance = data.maxDistance;// 路两点间的最大距离
	current_label_point_setting = setting;
	closeDialog();
	
	showCommondDialog("label-point-setting-dlg");
}

function saveLabelPointSetting(){
	var form = $("#label-point-setting-dlg-frm");
	if (!form.valid()) {
		return;
	}

	// 增加非法字符过滤。add by tyj-20200306 start
	var reg =/^(?!_)(?!.*?_$)[a-zA-Z0-9_\u4e00-\u9fa5]+$/;
   var name =  $("#label-point-setting-dlg-frm").find("input[name=name]").val();
   //var text =  $("#label-point-setting-dlg-frm").find("textarea[name=text]").val();  //update by liliang-20200313  标注设置页面的备注字段被隐藏
   if (!name.match(reg)) {
   	layer.msg("请输入汉字、数字、字母或者下划线");
   	return;
   }
   //update by liliang-20200313  标注设置页面的备注字段被隐藏
   /* if (!text.match(reg)) {
        layer.msg("请输入汉字、数字、字母或者下划线");
        return;
    }*/
   // 增加非法字符过滤。add by tyj-20200306  end
	var data = form.serializeObject();
	data.setting=$.toJSON(current_label_point_setting);
	var url=management_api_server_servlet_path+"/common/labelPoint.json";
	ajaxAsyncPost(url,data,function(result){
		if (result.code!=0) {
			showErrorMessage(result.message);
		}  else {
			closeDialog();
			showMessage("设置成功");
		}
	},true);
}