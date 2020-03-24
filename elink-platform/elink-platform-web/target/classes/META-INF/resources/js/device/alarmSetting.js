$(document).ready(function() {
	setHourOption("overspeed-alarm-setting-dlg-frm-startHour",true);
	setHourOption("overspeed-alarm-setting-dlg-frm-endHour",false);
	setHourOption("rail-alarm-setting-dlg-frm-startHour",true);
	setHourOption("rail-alarm-setting-dlg-frm-endHour",false);
	setHourOption("district-alarm-setting-dlg-frm-startHour",true);
	setHourOption("district-alarm-setting-dlg-frm-endHour",false);

	$(document).on("change",'select#rail-alarm-setting-dlg-frm-inside',function(){
	     if($(this).val()=="2"){
	    	 $("#rail-alarm-setting-dlg-frm-speed-div").show();
	     }else{
	    	 $("#rail-alarm-setting-dlg-frm-speed-div").hide();
	     }
	});
	
	$(document).on("change",'select#district-alarm-setting-dlg-frm-inside',function(){
	     if($(this).val()=="2"){
	    	 $("#district-alarm-setting-dlg-frm-speed-div").show();
	     }else{
	    	 $("#district-alarm-setting-dlg-frm-speed-div").hide();
	     }
	});

	initLimitDatetimepicker("overspeed-alarm-setting-dlg-frm-startTime","overspeed-alarm-setting-dlg-frm-endTime",new Date(),"",0.5);
	initLimitDatetimepicker("district-alarm-setting-dlg-frm-startTime","district-alarm-setting-dlg-frm-endTime",new Date(),"",0.5);
	initLimitDatetimepicker("rail-alarm-setting-dlg-frm-startTime","rail-alarm-setting-dlg-frm-endTime",new Date(),"",0.5);
	initLimitDatetimepicker("move-alarm-setting-dlg-frm-startTime","move-alarm-setting-dlg-frm-endTime",new Date(),"",0.5);
});


var current_circle = null;
var current_polygon = null;
function setRailAlarm(dialogId){
	current_dialog_id=dialogId;
	showCmdDialog(dialogId)
}

var railLabelMap= new JsMap();
var railLabelTextMap;
var weekMap= new JsMap();
weekMap.put("0","日");
weekMap.put("1","一");
weekMap.put("2","二");
weekMap.put("3","三");
weekMap.put("4","四");
weekMap.put("5","五");
weekMap.put("6","六");

function clearAlarmSetting(){
	if(current_circle){
		mapUtil.removeOverlay(current_circle);
	}
	if(current_polygon){
		mapUtil.removeOverlay(current_polygon);
	}
}

function loadRail(){
	if(!current_selected_device){
		showMessage("请选择要操作的车辆！");
		return;
	}
	var url=management_api_server_servlet_path+"/common/query/deviceAlarmSetting.json?select=name,setting";
	var now = new Date().format();
	var data={};
	data["state.eq"]=1;
	data["type.eq"]=2;
	data["deviceId.eq"]=current_selected_device.id;
	data["startTime.lte"]=now;
	data["endTime.gte"]=now;
	ajaxAsyncPost(url,data,function(result){
		if(!result || !result.data ||result.data.length <=0){
			showMessage("所查询的车辆无有效围栏数据！");
			return;
		}

		result=result.data;
		railLabelTextMap= new JsMap();
		for(var i=0;i<result.length;i++){
			//{"radius":"5492","inside":"0","lat":30.120334,"lng":109.203745,"startHour":"0","endHour":"0","week":["1","2"]}
			var arr = result[i];
			var item=$.evalJSON(arr[1]);
			var startHour=item.startHour<10?"0"+item.startHour+":00":item.startHour+":00";
			var endHour=item.endHour<10?"0"+item.endHour+":59":item.endHour+":59";
			var type=item.inside==0?"出围栏告警":"进围栏告警";
			var weeks=item.week;
			var weekstr="";
			if(weeks&&weeks.length>0){
				for(var j=0;j<weeks.length;j++){
					weekstr+=","+weekMap.get(weeks[j]);
				}
				weekstr=weekstr.substr(1)
			}
			var text="";
			if(arr[0]){
				text="围栏名称："+arr[0]+"<br>";
			}
			text +="围栏类型："+type+"<br>有效星期："+weekstr+"<br>有效时段："+startHour+"-"+endHour;
			text ="<div class='amap-info-window-content'>"+text+"</div>";
			if(item.shape==1){
				var gcj02 = LngLatConverter.wgs84togcj02(item.lng,item.lat);
				var point=mapUtil.getPoint(gcj02[0],gcj02[1]);
				var circle=mapUtil.addCircle(point,item.radius);
				railLabelTextMap.put(circle,text);
				circle.on("mouseover",function(e){
					circle = e.target;
					var text =railLabelTextMap.get(circle);
					railLabelMap.put(circle,mapUtil.addLabel(circle.getCenter(),text));
				});
				circle.on("mouseout",function(e){
					circle = e.target;
					mapUtil.removeOverlay(railLabelMap.get(circle));
					railLabelMap.remove(circle);
				});
			}else if(item.shape==4){
				var bounds = item.bounds;
				for(var j=0;j<bounds.length;j++){
					var arr = bounds[j].split(";");
					var path = new Array();
					for(var k=0,l=arr.length;k<l;k++){
						var point = arr[k].split(",");
						var gcj02 = LngLatConverter.wgs84togcj02(point[0],point[1]);
						path.push(mapUtil.getPoint(gcj02[0],gcj02[1]));
					}
					mapUtil.addPolygon(path);
				}
			}else{
				var arr = item.path.split(";");
				var path = new Array();
				for(var j=0;j<arr.length;j++){
					var point = arr[j].split(",");
					var gcj02 = LngLatConverter.wgs84togcj02(point[0],point[1]);
					path.push(mapUtil.getPoint(gcj02[0],gcj02[1]));
				}
				var polygon=mapUtil.addPolygon(path);
				railLabelTextMap.put(polygon,text);
				polygon.on("mouseover",function(e){
					polygon = e.target;
					var text =railLabelTextMap.get(polygon);
					railLabelMap.put(polygon,mapUtil.addLabel(polygon.getBounds().getCenter(),text));
				});
				polygon.on("mouseout",function(e){
					polygon = e.target;
					mapUtil.removeOverlay(railLabelMap.get(polygon));
					railLabelMap.remove(polygon);
				});
			}
		}
		mapUtil.getMap().setFitView();
	});
}

function drawRail(type){
	if(!current_selected_device){
		showMessage("请选择要操作的车辆！");
		return;
	}
	//清除轨迹
	clearPathNavigators();
	//隐藏仪表盘
	stopFollowCar(true);
	//清除围栏设置
	clearAlarmSetting();
	
	mapUtil.closeDraw();
	mapUtil.openDraw();
	var message = "";
	if(type==1){
		message = "画圆形";
		mapUtil.drawCircle(drawCircleCompletedHandler);
	}else if(type==2){
		message = "画矩形";
		mapUtil.drawRectangle(drawPolygonCompletedHandler);
	}else if(type==3){
		message = "画多边形";
		mapUtil.drawPolygon(drawPolygonCompletedHandler);
	}
	current_dialog_id="rail-alarm-setting-dlg";
	$("#"+current_dialog_id+"-frm-shape").val(type);
	showMessage("请在地图上"+message+"电子围栏！");//midified by liliang 20200304
}

function districtRail(){
	if(!current_selected_device){
		showMessage("请选择要操作的车辆！");
		return;
	}
	current_dialog_id="district-alarm-setting-dlg";
	showCmdDialog(current_dialog_id);
}

function searchAlarmDistrict(object){
	 //清除地图上所有覆盖物
   for (var i = 0, l = current_district_polygons.length; i < l; i++) {
   	current_district_polygons[i].setMap(null);
   }
   var arr = $(object).val().split(",");
   var district = mapUtil.getDistrictSearch();
   district.setLevel(arr[1]); //行政区级别
   district.setExtensions("all");
   district.setSubdistrict(1);
   //行政区查询
   //按照adcode进行查询可以保证数据返回的唯一性
   district.search(arr[0], function(status, result) {
       if(status === 'complete'){
    	   setDistrictRailSearchResult(result);
       }
   });
}

var district_alarm_setting_dlg_frm_bounds;
var district_alarm_setting_dlg_frm_level;
var district_alarm_setting_dlg_frm_areaCode;
function setDistrictRailSearchResult(result) {
	current_district_polygons=[];
	var district = result.districtList[0];
    var boundaries = district.boundaries;
    var map = mapUtil.getMap();
    if (boundaries) {
    	var bounds=[];
        for (var i = 0, l = boundaries.length; i < l; i++) {
        	var points = boundaries[i];
	   		var path="";
	   		for(var j = 0; j< points.length;j++){
	   			var point = points[j];
	   			var wgs84 = LngLatConverter.gcj02towgs84(point.getLng(), point.getLat());
	   			path+=";"+wgs84[0]+","+wgs84[1];
	   		}
	   		if(path!=""){
	   			 bounds.push(path.substr(1));
	   		}
        	var polygon=mapUtil.addPolygon(points);
            current_district_polygons.push(polygon);
        }
        map.setFitView();//地图自适应
        district_alarm_setting_dlg_frm_bounds=bounds;
    }
    district_alarm_setting_dlg_frm_areaCode = district.adcode;
    district_alarm_setting_dlg_frm_level = district.level;
    var subList = district.districtList;
    if (subList) {
    	 var level = district.level;
    	 var select = $("#district-alarm-setting-dlg-frm-province");
         //清空下一级别的下拉列表
         if(level === 'country'){
        	 
         }else if (level === 'province') {
        	 $("#district-alarm-setting-dlg-frm-city").empty();
        	 $("#district-alarm-setting-dlg-frm-district").empty();
        	 select = $("#district-alarm-setting-dlg-frm-city");
         } else if (level === 'city') {
        	 $("#district-alarm-setting-dlg-frm-district").empty();
        	 select = $("#district-alarm-setting-dlg-frm-district");
         }else if(level === 'district'){
        	 return;
         }
         select.append("<option value=''>--请选择--</option>");
         for (var i = 0, l = subList.length; i < l; i++) {
        	 var item = subList[i];
        	 select.append("<option value='"+item.adcode+","+item.level+"'>"+item.name+"</option>");
         }
     }
}

function drawCircleCompletedHandler(e, circle){
	circle=e.obj;
	current_circle=circle;
	var point=circle.getCenter();
	
	//国测局坐标转wgs84坐标
    var wgs84 = LngLatConverter.gcj02towgs84(point.getLng(), point.getLat());
	$("#"+current_dialog_id+"-frm-radius").val(parseInt(circle.getRadius()));
	$("#"+current_dialog_id+"-frm-lat").val(wgs84[1]);
	$("#"+current_dialog_id+"-frm-lng").val(wgs84[0]);
	mapUtil.closeDraw();
	showCmdDialog(current_dialog_id);
}

function drawPolygonCompletedHandler(e, polygon){
	polygon=e.obj;
	current_polygon=polygon;
	var path=polygon.getPath();
	var points = "";
	//国测局坐标转wgs84坐标
	for(var i=0;i<path.length;i++){
		var point = path[i];
		var wgs84 = LngLatConverter.gcj02towgs84(point.getLng(), point.getLat());
		points+=";"+wgs84[0]+","+wgs84[1];
	}
    
	$("#"+current_dialog_id+"-frm-path").val(points.substr(1));
	mapUtil.closeDraw();
	showCmdDialog(current_dialog_id);
}

function setHourOption(id,start){
	for(var i=0;i<24;i++){
		var text=i;
		if(i<10){
			text="0"+i;
		}
		if(start){
			text+=":00";
		}else{
			text+=":59";
		}
		
		$("#"+id).append("<option value='"+i+"'>"+text+"</option>");
	}
}

function saveMoveAlarmSetting(){
	if(current_selected_device_lacation==null){
		showMessage(current_selected_device.name+"未定位，设置失败！");
		return;
	}
	
	var form = $("#move-alarm-setting-dlg-frm");
	if (!form.valid()) {
		return;
	}
	var data = form.serializeObject();
	var setting={};
	setting.duration=data.duration;
	setting.interval=data.interval;
	setting.radius=150;
    var wgs84 = LngLatConverter.gcj02towgs84(current_selected_device_lacation.lng,current_selected_device_lacation.lat);
	setting.lat=wgs84[1];
	setting.lng=wgs84[0];
	data.setting=$.toJSON(setting);
	var url=management_api_server_servlet_path+"/common/deviceAlarmSetting.json";
	ajaxAsyncPost(url,data,function(result){
		if (result.code!=0) {
			showErrorMessage(result.message);
		}  else {
			clearAlarmSetting();
			closeDialog();
			showMessage("设置成功");
		}
	});
}

function saveRailAlarmSetting(){
	if(current_selected_device_lacation==null){
		showMessage(current_selected_device.name+"未定位，设置失败！");
		return;
	}
	
	var form = $("#rail-alarm-setting-dlg-frm");
	if (!form.valid()) {
		return;
	}
	var data = form.serializeObject();	
	
	var setting={};
	setting.shape = data.shape;
	setting.inside=data.inside;
	if(data.shape==1){
		setting.radius=data.radius;
		setting.lat=data.lat;
		setting.lng=data.lng;
	}else{
		setting.path=data.path;
	}
	setting.startHour=data.startHour;
	setting.endHour=data.endHour;
	if(data.startHour > data.endHour){
		showMessage("结束时段必须大于开始开始时段！");
		return;
	}
	if(data.week.length>0){
		setting.week=data.week;
	}else{
		showMessage("请正确设置有效星期");
		return;
	}
	if(data.inside=="2"){
		if(data.speed==""){
			showMessage("请设置限速值");
			return;
		}else{
			setting.speed=data.speed;
		}
	}
	data.setting=$.toJSON(setting);
	var url=management_api_server_servlet_path+"/common/deviceAlarmSetting.json";
	ajaxAsyncPost(url,data,function(result){
		if (result.code!=0) {
			showErrorMessage(result.message);
		}  else {
			if(current_circle){
				mapUtil.removeOverlay(current_circle);
			}
			if(current_polygon){
				mapUtil.removeOverlay(current_polygon);
			}
			closeDialog();
			showMessage("设置成功");
		}
	});
}

function saveOverspeedAlarmSetting(){
	if(current_selected_device_lacation==null){
		showMessage(current_selected_device.name+"未定位，设置失败！");
		return;
	}
	var form = $("#overspeed-alarm-setting-dlg-frm");
	if (!form.valid()) {
		return;
	}
	var data = form.serializeObject();	
	
	if(data.speed=="" || data.speed <=0){
		showMessage("请正确设置限速值");
		return;
	}
	var setting={};
	setting.speed=data.speed;
	setting.interval=data.interval;
	setting.startHour=data.startHour;
	setting.endHour=data.endHour;
	
	if(data.startHour > data.endHour){
		showMessage("结束时段必须大于开始开始时段！");
		return;
	}
	data.setting=$.toJSON(setting);
	var url=management_api_server_servlet_path+"/common/deviceAlarmSetting.json";
	ajaxAsyncPost(url,data,function(result){
		if (result.code!=0) {
			showErrorMessage(result.message);
		}  else {
			closeDialog();
			showMessage("设置成功");
		}
	});
}

function saveDistrictAlarmSetting(){
	if(current_selected_device_lacation==null){
		showMessage(current_selected_device.name+"未定位，设置失败！");
		return;
	}
	var form = $("#district-alarm-setting-dlg-frm");
	if (!form.valid()) {
		return;
	}
	var data = form.serializeObject();	
	var setting={};
	setting.shape = data.shape;
	setting.inside=data.inside;
	if(!district_alarm_setting_dlg_frm_bounds){
		showMessage("请行政区域");
		return;
	}

	var tolerance=500;
	if(district_alarm_setting_dlg_frm_level=="province"){
		tolerance=2000;
	}else if(district_alarm_setting_dlg_frm_level=="city"){
		tolerance=1000;
	}

	var url=lbs_api_server_servlet_path+"/gps/district/simple.json";
	//抽稀算法，省2000米，市1000米，区500米
	var result = ajaxSyncPost(url,{"bounds":district_alarm_setting_dlg_frm_bounds,"tolerance":tolerance});
	if (result.code!=0) {
		showErrorMessage(result.message);
		return;
	}
	setting.bounds=result.data;
	setting.startHour=data.startHour;
	setting.endHour=data.endHour;
	
	if(data.startHour > data.endHour){
		showMessage("结束时段必须大于开始开始时段！");
		return;
	}
	if(data.week.length>0){
		setting.week=data.week;
	}else{
		showMessage("请正确设置有效星期");
		return;
	}
	if(data.inside=="2"){
		if(data.speed==""){
			showMessage("请设置限速值");
			return;
		}else{
			setting.speed=data.speed;
		}
	}
	
	var url=management_api_server_servlet_path+"/system/dictionary/districtSearchData.json?select=centerLng,centerLat,maxRadius";
	var result = ajaxSyncPost(url,{"code.eq":district_alarm_setting_dlg_frm_areaCode});
	if (result.code!=0) {
		showErrorMessage(result.message);
		return;
	}
	if(result.data && result.data.length > 0){
		var item = result.data[0];
		var center = {};
		center.lng = item[0];
		center.lat = item[1];
		center.radius = item[2];
		setting.center = center;
	}

	data.setting=$.toJSON(setting);
	var url=management_api_server_servlet_path+"/common/deviceAlarmSetting.json";
	ajaxAsyncPost(url,data,function(result){
		if (result.code!=0) {
			showErrorMessage(result.message);
		}  else {
			for (var i = 0, l = current_district_polygons.length; i < l; i++) {
			   	current_district_polygons[i].setMap(null);
			}
			district_alarm_setting_dlg_frm_bounds=null;
			closeDialog();
			showMessage("设置成功");
		}
	});
}