var adasAlarmMap = new JsMap();
adasAlarmMap.put("adas1","前向碰撞报警");
adasAlarmMap.put("adas2","车道偏离报警");
adasAlarmMap.put("adas3","车距过近报警");
adasAlarmMap.put("adas4","行人碰撞报警");
adasAlarmMap.put("adas5","频繁变道报警");
adasAlarmMap.put("adas6","道路标识超限报警");
adasAlarmMap.put("adas7","障碍物报警");
adasAlarmMap.put("adas8","驾驶辅助功能失效报警");
adasAlarmMap.put("adas10","道路标志识别事件");
adasAlarmMap.put("adas11","主动抓拍事件");

var dsmAlarmMap = new JsMap();
dsmAlarmMap.put("dsm1","疲劳驾驶报警");
dsmAlarmMap.put("dsm2","接打电话报警");
dsmAlarmMap.put("dsm3","抽烟报警");
dsmAlarmMap.put("dsm4","分神驾驶报警");
dsmAlarmMap.put("dsm5","驾驶员异常报警");
dsmAlarmMap.put("dsm6","双手同时脱离方向盘报警");
dsmAlarmMap.put("dsm7","驾驶员行为监测功能失效报警");
dsmAlarmMap.put("dsm10","自动抓拍事件");
dsmAlarmMap.put("dsm11","驾驶员变更事件");

var tpmAlarmMap = new JsMap();
tpmAlarmMap.put("tpm1","胎压过高报警");
tpmAlarmMap.put("tpm2","胎压过低报警");
tpmAlarmMap.put("tpm3","胎温过高报警");
tpmAlarmMap.put("tpm4","传感器异常报警");
tpmAlarmMap.put("tpm5","胎压不平衡报警");
tpmAlarmMap.put("tpm6","慢漏气报警");
tpmAlarmMap.put("tpm7","电池电量低报警");

var bsdAlarmMap = new JsMap();
bsdAlarmMap.put("bsd1","后方接近报警");
bsdAlarmMap.put("bsd2","左侧后方接近报警");
bsdAlarmMap.put("bsd3","右侧后方接近报警");

function setSearchDetail(type){
	$("#search-detail").empty();
	$("#search-detail").append("<option value=''>选择详情</option>");
	if(type==1){
		var keys = devicerfaultAlarmInfoMap.getKeys();
		for(var i=0;i<keys.length;i++){
			var key = keys[i];
			var text = devicerfaultAlarmInfoMap.get(key);
			$("#search-detail").append("<option value='a"+key+"'>" + text + "</option>");
		}
	}else if(type==2){
		var keys = drivingBehaviorAlarmInfoMap.getKeys();
		for(var i=0;i<keys.length;i++){
			var key = keys[i];
			var text = drivingBehaviorAlarmInfoMap.get(key);
			$("#search-detail").append("<option value='a"+key+"'>" + text + "</option>");
		}
	}else if(type==3){
		var keys = carUrgentAlarmInfoMap.getKeys();
		for(var i=0;i<keys.length;i++){
			var key = keys[i];
			var text = carUrgentAlarmInfoMap.get(key);
			$("#search-detail").append("<option value='a"+key+"'>" + text + "</option>");
		}
	}else if(type==4){
		var keys = platformAlarmInfoMap.getKeys();
		for(var i=0;i<keys.length;i++){
			var key = keys[i];
			var text = platformAlarmInfoMap.get(key);
			$("#search-detail").append("<option value='a"+key+"'>" + text + "</option>");
		}
	}else if(type==5){
		//["视频信号丢失告警","视频信号遮挡告警","存储单元故障告警","其他视频设备故障告警","客车超员告警","异常驾驶行为告警","特殊报警录像达到存储阈值告警"]
	}else if(type=='adas'){
		var keys = adasAlarmMap.getKeys();
		for(var i=0;i<keys.length;i++){
			var key = keys[i];
			var text = adasAlarmMap.get(key);
			$("#search-detail").append("<option value='"+key+"'>" + text + "</option>");
		}
	}else if(type=='dsm'){
		var keys = dsmAlarmMap.getKeys();
		for(var i=0;i<keys.length;i++){
			var key = keys[i];
			var text = dsmAlarmMap.get(key);
			$("#search-detail").append("<option value='"+key+"'>" + text + "</option>");
		}
	}else if(type=='tpm'){
		var keys = tpmAlarmMap.getKeys();
		for(var i=0;i<keys.length;i++){
			var key = keys[i];
			var text = tpmAlarmMap.get(key);
			$("#search-detail").append("<option value='"+key+"'>" + text + "</option>");
		}
	}else if(type=='bsd'){
		var keys = bsdAlarmMap.getKeys();
		for(var i=0;i<keys.length;i++){
			var key = keys[i];
			var text = bsdAlarmMap.get(key);
			$("#search-detail").append("<option value='"+key+"'>" + text + "</option>");
		}
	}else if(type==7){

	}
}

function getDefaultSort() {
	return "alarmTime";
}

var conditionsCache = new Cache("alarm_conditions_cache");

function getQueryParams() {
	var params = {};
	var date = $('#search-date').val();
	var params = {}

	var conditions = {};
	var deviceId = $("#search-deviceId").val();
	if (deviceId) {
		conditions["deviceId.eq"] = deviceId;
	}

	var type = $("#search-type").val();
	if (type) {
		if(/^[0-9]+.?[0-9]*$/.test(type)){
			conditions["type.eq"] = parseInt(type);
		}else{
			conditions[type+".exists"] = true;
		}
	}

	var detail = $("#search-detail").val();
	if (detail) {
		conditions[detail+".exists"] = true;
	}

	var state = $("#search-state").prop("checked");
	if(state){
		conditions["k.exists"] = true;
		conditions["state.exists"] = false;
	}
	params["conditions"] = conditions;

	var startTime = new Date(date.replace(/-/g, "/")).getTime();
	var rangeCondition = {};
	rangeCondition["fieldName"] = "alarmTime";
	rangeCondition["from"] = startTime;
	rangeCondition["includeLower"] = true;
	rangeCondition["includeUpper"] = true;
	rangeCondition["to"] = startTime + 24*60*60*1000;
	var rangeConditions = new Array();
	rangeConditions[0] = rangeCondition;
	params["rangeConditions"] = rangeConditions;

	clearConditionsCache();
	return params;
}

function getQueryUrl() {
	return message_api_server_servlet_path + "/common/query/alarm.json?countable=true";
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
			field : "alarmTime",
			sortable : true,
			title : "告警时间",
			formatter : function(value, row, index) {
				var alarmTime = row.alarmTime;
				if (alarmTime == null) {
					return "";
				}
				return new Date(alarmTime).format();
			}
		},
		{
			field : "endTime",
			title : "告警结束时间",
			formatter : function(value, row, index) {
				for(var key in row){
					if(key.indexOf("et_")!=-1){
						return new Date(row[key]).format();
					}
				}
				return "";
			}
		},
		{
			field : "alarmType",
			title : "告警类型",
			formatter : function(value, row, index) {
				var type = row.type;
				if(type==1){
					return "设备故障告警";
				}else if(type==2){
					return "驾驶行为违规告警";
				}else if(type==3){
					return "紧急重要告警";
				}else if(type==4){
					return "平台告警";
				}else if(type==5){
					return "视频告警";
				}else if(type==6){
					if(row.adas){
						return "ADAS告警";
					}else if(row.dsm){
						return "DMS告警";
					}else if(row.tpm){
						return "TPMS告警";
					}else if(row.bsd){
						return "BSD告警";
					}
					return "苏标告警";
				}else if(type==7){
					return "脑电告警";
				}else{
					return "其他";
				}
			}
		},
		{
			field : "detail",
			title : "告警详情",
			formatter : function(value, row, index) {
				return getAlarmDetail(row);
			}
		},
		{
			title : '操作',
			field : 'opear',
			formatter : function(value, row) {
				var html = "";
				if(row["k"] && !row["state"]){
					html += '<a   href="javascript:void(0)" class="table_edit" title="确认告警" onclick="showAlarmConfirm(\''+ row.id + '\')">确认告警</a>';
				}
				html += '<a   href="javascript:void(0)" class="table_view" title="详情" onclick="view(\''+ row.id + '\')">详情</a>';
				html += '<a   href="javascript:void(0)" class="table_view" title="位置" onclick="viewLocation(\''+ row.id + '\')">位置</a>';
				return html;
			}
		} ];
}

function getAlarmDetail(row){
	var alarmStr = "";

	//*************苏标告警处理 start***************
	for(var i=0;i<adasAlarmDesc.length;i++){
		var s = adasAlarmDesc[i];
		if(s && row["adas"+(i+1)]){
			alarmStr += "," + s;
		}
	}

	for(var i=0;i<dsmAlarmDesc.length;i++){
		var s = dsmAlarmDesc[i];
		if(s && row["dsm"+(i+1)]){
			alarmStr += "," + s;
		}
	}

	//20个轮胎足够了
	for(var i=0;i<20;i++){
		var alarm = row["tpm"+i];
		if(alarm){
			for (var j = 0; j < tpmAlarmDesc.length; j++) {
				var s = "";
				if((((alarm&(1<<j))>>j)==1)) {
					s = tpmAlarmDesc[j];
				}
				if (s!="") {
					alarmStr += "," + s;
				}
			}
		}
	}

	for(var i=0;i<bsdAlarmDesc.length;i++){
		var s = bsdAlarmDesc[i];
		if(s && row["bsd"+(i+1)]){
			alarmStr += "," + s;
		}
	}

	//*************苏标告警处理 end***************

	//1078视频告警
	if(row["va"]){
		var videoAlarm = row["va"];
		for(var i=0;i<videoAlarmDesc.length;i++){
			var s = videoAlarmDesc[i];
			if(s && ((videoAlarm&(1<<i))>>i)==1 ){
				alarmStr += "," + s;
			}
		}
	}

	for (var i = 0; i < 50; i++) {
		var alarmType = carAlarmInfoMap.get(i);
		if (!alarmType) {
			continue;
		}
		var val = row["a" + i];
		if (val) {
			alarmStr += "," + alarmType;
		}
	}

	if(alarmStr==""){
		alarmStr=",用户自定义报警";
	}

	if (alarmStr.length > 40) {
		alarmStr = alarmStr.substr(1, 40) + "……"
	} else if (alarmStr) {
		alarmStr = alarmStr.substr(1)
	}


	return alarmStr;
}

function showAlarmConfirm(id){
	var row = getBootstrapTableRowById(id);
	var deviceId = row.deviceId;
	$("#alarm-confirm-dlg-frm-deviceName").html(getDeviceName(deviceId));
	$("#alarm-confirm-dlg-frm-alarmDetail").html(getAlarmDetail(row));
	$("#alarm-confirm-dlg-frm-alarmId").val(id);
	if(row.alarmEventId){
		$("#alarm-confirm-dlg-frm-alarmEventId").val(row.alarmEventId);
	}
	showCommondDialog("alarm-confirm-dlg");
}

function doAlarmConfirm(){
	var form =$("#alarm-confirm-dlg-frm").serializeObject();
	var url = "/common/alarm/" + form.alarmId + ".json";
	var data = {};
	data.state = 1;
	if(form.opinion){
		data.opinion = form.opinion;
	}
	data.confirmTime = new Date().getTime();
	data.confirmUserId="${sessionScope.user.userId}";
	ajaxAsyncPatch(url, data, function(result) {
		if (result.code != 0) {
			showErrorMessage(result.message);
		} else {
			if(data.eid && isOnline(data.deviceId)){
				//发送告警确认指令
				var cmd={};
				cmd.desc="人工确认报警消息";
				cmd.messageId="8203";
				cmd.messageSeq=data.eid;
				cmd.alarmType=409993225;//11000011100000000000000001001
				doSendCmd(data.deviceId, cmd);
			}
			closeDialog();
			showMessage("告警已确认");
			refreshBootstrapTable();
		}
	},true);
}

function view(id){
	cacheConditions();
	var row = getBootstrapTableRowById(id);
	window.location.href = "alarmDetail.do?id="+id+"&recordDate="+new Date(row.alarmTime).format("yyyyMMdd");
}

function cacheConditions(){
	conditionsCache.put("search-date",$('#search-date').val());
	conditionsCache.put("search-deviceId",$("#search-deviceId").val());
	conditionsCache.put("search-type",$("#search-type").val());
	conditionsCache.put("search-detail",$("#search-detail").val());
	conditionsCache.put("search-state",$("#search-state").prop("checked"));
}

function clearConditionsCache(){
	conditionsCache.remove("search-date");
	conditionsCache.remove("search-deviceId");
	conditionsCache.remove("search-type");
	conditionsCache.remove("search-detail");
}

function viewLocation(id) {
	var row = getBootstrapTableRowById(id);
	if(row.gps){
		var arr = row.gps.split(",");
		setLocation(arr[0],arr[1],"-",row);
		return;
	}

	if(row.gpsId){
		var deviceId = row.deviceId;
		var gpsId = row.gpsId;
		var data = {};
		data["recordDate"] = new Date(row.alarmTime).format("yyyyMMdd");
		var conditions = {};
		conditions["id.eq"] = gpsId;
		conditions["deviceId.eq"] = deviceId;
		data["conditions"] = conditions;

		var url = lbs_api_server_servlet_path + "/common/query/gps.json?select=lng,lat,speed&pageSize=1&pageNo=1";
		ajaxAsyncPost(url, data, function(result) {
			if (result.code != 0) {
				showErrorMessage(result.message);
				return;
			}
			var list = result.data;
			if (list && list.length > 0) {
				mapUtil.clearOverlays();
				var item = list[0];
				setLocation(item.lng,item.lat,parseInt(item.speed),row);
			} else {
				showMessage("无相关位置信息");
			}
		});
	}else{
		howMessage("无相关位置信息");
	}
}

function setLocation(lng,lat,speed,row){
	var deviceName = getDeviceName(row.deviceId);
	var gcj02 = LngLatConverter.wgs84togcj02(lng, lat);
	var lacation = mapUtil.getPoint(gcj02[0], gcj02[1]);
	var iconUrl = getCarDefaultIcon();
	mapUtil.addMarker(row.deviceId, lacation, deviceName, iconUrl, 60);
	mapUtil.regeocoder(lacation, function(address) {
		var text = "<b>" + deviceName + "</b> 速度：" + speed + "km/h";
		text += "<br/>时间：" + new Date(row.alarmTime).format();
		if (address) {
			text += "<br/>地址：" + address;
		}
		// 设置label标签
		text ="<div class='amap-info-window-content'>"+text+"</div>";
		mapUtil.addLabel(lacation, text);
		showCommondDialog("map-dlg");
	});
}

