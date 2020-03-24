var MAX_QUERY_SIZE = 2000;

var ioStateMap = new JsMap();
ioStateMap.put(0, "近光灯信号");
ioStateMap.put(1, "远光灯信号");
ioStateMap.put(2, "右转向灯信号");
ioStateMap.put(3, "左转向灯信号");
ioStateMap.put(4, "制动信号");
ioStateMap.put(5, "倒档信号");
ioStateMap.put(6, "雾灯信号");
ioStateMap.put(7, "示廓灯信号");
ioStateMap.put(8, "喇叭信号");
ioStateMap.put(9, "空调状态");
ioStateMap.put(10, "空挡信号");
ioStateMap.put(11, "缓速器工作状态");
ioStateMap.put(12, "ABS工作状态");
ioStateMap.put(13, "加热器工作状态");
ioStateMap.put(14, "离合器状态");

function setSearchHourOption() {
	var interval = 4;
	for (var i = 0; i < 24;) {
		var start = i;
		i = i + interval;
		var end = (i - 1);
		if (start < 10) {
			start = "0" + start;
		}
		if (end < 10) {
			end = "0" + end;
		}
		var text = start + ":00:00-" + end + ":59:59";
		$("#search-hour").append(
				"<option value='" + text + "'>" + text + "</option>");
	}
	var now = new Date();
	try {
		$("#search-hour").get(0).selectedIndex = Math.floor(now.getHours()
				/ interval);
	} catch (e) {
	}
}

function getGpsQueryConditions() {
	var deviceId = $("#search-deviceId").val();
	var date = $('#search-date').val();
	var hours = $("#search-hour").val();
	var time = hours.split("-");
	var endTime = date + " " + time[1];
	var startTime = date + " " + time[0];
	var data = {};
	data["recordDate"] = date;
	var conditions = {};
	conditions["deviceId.eq"] = deviceId;
	data["conditions"] = conditions;
	var rangeCondition = {};
	rangeCondition["fieldName"] = "gpsTime";
	rangeCondition["from"] = new Date(startTime.replace(/-/g, "/")).getTime();
	rangeCondition["includeLower"] = true;
	rangeCondition["includeUpper"] = true;
	rangeCondition["to"] = new Date(endTime.replace(/-/g, "/")).getTime();
	var rangeConditions = new Array();
	rangeConditions[0] = rangeCondition;
	data["rangeConditions"] = rangeConditions;
	return data;
}

function deviceOverview() {
	var myChart = echarts.init(document.getElementById('charts_div'));
	myChart.showLoading();
	var url = report_api_server_servlet_path + "/deviceOverView.json";
	ajaxAsyncGet(url, {}, function(result) {
		myChart.hideLoading();
		if (result.code != 0) {
			showErrorMessage(result.message);
			return;
		}
		var title = "车辆概况统计";
		var data = [];
		var legend = [];
		result = result.data;
		for (var i = 0; i < result.length; i++) {
			var item = result[i];
			var total = item.total;
			var state = getDeviceStateStr(item.state);
			legend.push(state);
			data.push({
				value : total,
				name : state
			});
		}
		myChart.setOption(getPieOption(title, legend, data));
	});
}

function lastHourSpeeds() {
	var deviceId = $("#search-deviceId").val();
	if (!deviceId) {
		showMessage("请选择要查看的车辆！");
		return;
	}
	var myChart = echarts.init(document.getElementById('charts_div'));
	myChart.showLoading();
	var deviceName = $("#search-deviceId").find("option:selected").text();
	var data = getGpsQueryConditions();
	var url = lbs_api_server_servlet_path
			+ "/common/query/gps.json?select=gpsTime,speed&orderBy=gpsTime&pageSize="
			+ MAX_QUERY_SIZE + "&pageNo=1";
	ajaxAsyncPost(url, data, function(result) {
		myChart.hideLoading();
		if (result.code != 0) {
			showErrorMessage(result.message);
			return;
		}
		var title = '行驶速度分析';
		var data = [];
		result = result.data;
		for (var i = 0; i < result.length; i++) {
			var item = result[i];
			var time = new Date(item.gpsTime).format();
			data.push({
				name : time,
				value : [ time, item.speed ]
			});
		}
		// 指定图表的配置项和数据
		myChart.setOption(getLineOption(title, "速度", data, "km/h"));
	});
}

function lastHourOilmass() {
	var deviceId = $("#search-deviceId").val();
	if (!deviceId) {
		showMessage("请选择要查看的车辆！");
		return;
	}
	var deviceName = $("#search-deviceId").find("option:selected").text();
	var myChart = echarts.init(document.getElementById('charts_div'));
	myChart.showLoading();
	var data = getGpsQueryConditions();
	var url = lbs_api_server_servlet_path
			+ "/common/query/gps.json?select=gpsTime,oilmass&orderBy=gpsTime&pageSize="
			+ MAX_QUERY_SIZE + "&pageNo=1";
	ajaxAsyncPost(url, data, function(result) {
		myChart.hideLoading();
		if (result.code != 0) {
			showErrorMessage(result.message);
			return;
		}

		var title = '剩余油量变化分析';
		var data = [];
		result = result.data;
		for (var i = 0; i < result.length; i++) {
			var item = result[i];
			var time = new Date(item.gpsTime).format();
			data.push({
				name : time,
				value : [ time, item.oilmass ]
			});
		}

		// 指定图表的配置项和数据
		myChart.setOption(getLineOption(title, "油耗", data, "L"));
	});
}

function todayAlarmOverview() {
	var myChart = echarts.init(document.getElementById('charts_div'));
	myChart.showLoading();
	var date = new Date().format("yyyyMMdd");
	var url = report_api_server_servlet_path + "/enterpriseDailyAlarmOverview/"
			+ date + ".json";
	var deviceId = $("#search-deviceId").val();
	var deviceName = $("#search-deviceId").find("option:selected").text();
	if (query && deviceId) {
		url += "?deviceId=" + deviceId;
	} else {
		deviceName = "企业";
	}
	ajaxAsyncGet(url, {}, function(result) {
		query = false;
		myChart.hideLoading();
		if (result.code != 0) {
			showErrorMessage(result.message);
			return;
		}
		var legend = [];
		var data = [];
		result = result.data;
		var sum = 0;
		if (result.length < 1) {
			legend.push("无数据");
			data.push(0);
		} else {
			var item = result[0];
			for (var i = 0; i < 50; i++) {
				var alarmType = carAlarmInfoMap.get(i);
				if (!alarmType) {
					continue;
				}
				var val = item["a" + i];
				if (val) {
					legend.push(alarmType);
					data.push(val);
					sum += val;
				}
			}
		}
		var title = deviceName + "今日告警，共计" + sum + "次";
		myChart.setOption(getBarOption(title, legend, data));
	});
}

function dailyAlarmOverview() {
	var date = $('#search-date').val();
	var yesterday = new Date(new Date().getTime() - 1 * 24 * 60 * 60 * 1000)
			.format("yyyyMMdd");
	var dateStr = date.replace(/-/g, "/");
	date = date.replace(/-/g, "");
	if (!date || date > yesterday) {
		showMessage("请选择小于今天的日期！");
		return;
	}
	
	var myChart = echarts.init(document.getElementById('charts_div'));
	myChart.showLoading();
	var url = report_api_server_servlet_path
			+ "/enterpriseHistoryDailyAlarmOverview/" + date + ".json";
	var deviceId = $("#search-deviceId").val();
	var deviceName = $("#search-deviceId").find("option:selected").text();
	if (query && deviceId) {
		url += "?deviceId=" + deviceId;
	} else {
		deviceName = "企业";
	}
	ajaxAsyncGet(url, {}, function(result) {
		myChart.hideLoading();
		if (result.code != 0) {
			showErrorMessage(result.message);
			return;
		}

		var legend = [];
		var data = [];
		result = result.data;
		var sum = 0;
		if (result.length < 1) {
			legend.push("无数据");
			data.push(0);
		} else {
			var item = result[0];
			for (var i = 0; i < 50; i++) {
				var alarmType = carAlarmInfoMap.get(i);
				if (!alarmType) {
					continue;
				}
				var val = item["a" + i];
				if (val) {
					legend.push(alarmType);
					data.push(val);
					sum += val;
				}
			}
		}
		var title = deviceName + dateStr + "日告警，共计" + sum + "次";
		myChart.setOption(getBarOption(title, legend, data));
	});
}

function monthAlarmOverview(){
	 var month =$('#search-date').val();
	 if(month==""){
		 month = new Date().format("yyyy-MM")
	 }
	 
	 var deviceId = $("#search-deviceId").val();
	 var deviceName = $("#search-deviceId").find("option:selected").text();
	 var url=report_api_server_servlet_path+"/enterpriseMonthAlarmOverview/"+month.replace("-","")+".json";
	 if(query && deviceId){
		 url=report_api_server_servlet_path+"/deviceMonthAlarmOverview/"+deviceId+"/"+month.replace("-","")+".json";
		 deviceName+="-";
	 }else{
		 deviceName="企业";
	 }
	 
	 var myChart = echarts.init(document.getElementById('charts_div'));
	 myChart.showLoading();
	 ajaxAsyncGet(url,{},function (result) {
		    query=false;
		    myChart.hideLoading();
		    if (result.code!=0) {
				showErrorMessage(result.message);
				return;
			}
			var legend = [];
			var data = [];
			result = result.data;
			var sum=0;
			if(result.length<1){
				legend.push("无数据");
			    data.push(0);
			}else{
				var item=result[0];
				for(var i=0;i<50;i++){
					var alarmType = carAlarmInfoMap.get(i);
					if(!alarmType){
						continue;
					}
					var val = item["a"+i];
					if(val){
						legend.push(alarmType);
					    data.push(val);
						sum+=val;
					}
				}
			}
			var title=deviceName+month.replace("-","/")+"月告警，共计"+sum+"次";
			myChart.setOption(getBarOption(title, legend, data));
		});
}

function lastHourrssi() {
	var deviceId = $("#search-deviceId").val();
	if (!deviceId) {
		showMessage("请选择要查看的车辆！");
		return;
	}
	var deviceName = $("#search-deviceId").find("option:selected").text();
	var myChart = echarts.init(document.getElementById('charts_div'));
	myChart.showLoading();
	var data = getGpsQueryConditions();
	var url = lbs_api_server_servlet_path
			+ "/common/query/gps.json?select=gpsTime,rssi&orderBy=gpsTime&pageSize="
			+ MAX_QUERY_SIZE + "&pageNo=1";
	ajaxAsyncPost(url, data, function(result) {
		myChart.hideLoading();
		if (result.code != 0) {
			showErrorMessage(result.message);
			return;
		}
		var title = '无线信号量分析';
		var data = [];
		result = result.data;
		var lastRssi = 0;
		for (var i = 0, l = result.length; i < l; i++) {
			var item = result[i];
			var time = new Date(item.gpsTime).format();
			var rssi = item.rssi;
			if (i == 0 || (i + 1) == l
					|| lastRssi != rssi) {
				data.push({
					name : time,
					value : [ time, rssi ]
				});
			}
			lastRssi = rssi;
		}
		// 指定图表的配置项和数据
		myChart.setOption(getStepLineOption(title, data));
	});
}

function lastHourOilmassState() {
	var deviceId = $("#search-deviceId").val();
	if (!deviceId) {
		showMessage("请选择要查看的车辆！");
		return;
	}
	var deviceName = $("#search-deviceId").find("option:selected").text();
	var myChart = echarts.init(document.getElementById('charts_div'));
	myChart.showLoading();
	var data = getGpsQueryConditions();
	var url = lbs_api_server_servlet_path
			+ "/common/query/gps.json?select=gpsTime,state&orderBy=gpsTime&pageSize="
			+ MAX_QUERY_SIZE + "&pageNo=1";
	ajaxAsyncPost(url, data, function(result) {
		myChart.hideLoading();
		if (result.code != 0) {
			showErrorMessage(result.message);
			return;
		}
		var title = '油路状态分析';
		var data = [];
		result = result.data;
		var lastState = 0;
		for (var i = 0, l = result.length; i < l; i++) {
			var item = result[i];
			var time = new Date(item.gpsTime).format();
			var state = (item.state&(1<<10))>>10;
			if (i == 0 || (i + 1) == l || state != lastState) {
				data.push({
					name : time,
					value : [ time, state == 1 ? "开" : "关" ]
				});
			}
			lastState = state;
		}
		// 指定图表的配置项和数据
		yAxisData = [ '关', '开' ];
		myChart.setOption(getStateTimeLineOption(title, data, yAxisData));
	});
}

function lastHourLocationState() {
	var deviceId = $("#search-deviceId").val();
	if (!deviceId) {
		showMessage("请选择要查看的车辆！");
		return;
	}
	var deviceName = $("#search-deviceId").find("option:selected").text();
	
	var myChart = echarts.init(document.getElementById('charts_div'));
	myChart.showLoading();
	var data = getGpsQueryConditions();
	var url = lbs_api_server_servlet_path
			+ "/common/query/gps.json?select=gpsTime,state&orderBy=gpsTime&pageSize="
			+ MAX_QUERY_SIZE + "&pageNo=1";
	ajaxAsyncPost(url, data, function(result) {
		myChart.hideLoading();
		if (result.code != 0) {
			showErrorMessage(result.message);
			return;
		}
		var title = '卫星定位状态分析';
		var data = [];
		result = result.data;
		var lastState = 0;
		for (var i = 0, l = result.length; i < l; i++) {
			var item = result[i];
			var time = new Date(item.gpsTime).format();
			var state = ((item.state&(1<<1))>>1);
			if (i == 0 || (i + 1) == l || state != lastState) {
				data.push({
					name : time,
					value : [ time, state == 1 ? "开" : "关" ]
				});
			}
			lastState = state;
		}
		// 指定图表的配置项和数据
		yAxisData = [ '关', '开' ];
		myChart.setOption(getStateTimeLineOption(title, data, yAxisData));
	});
}

function lastHourIoState() {
	var deviceId = $("#search-deviceId").val();
	if (!deviceId) {
		showMessage("请选择要查看的车辆！");
		return;
	}
	var deviceName = $("#search-deviceId").find("option:selected").text();
	var myCharts = [];
	for (var j = 0; j < ioStateMap.size(); j++) {
		var myChart = echarts.init(document.getElementById('charts_div'
				+ (j + 1)));
		myChart.showLoading();
		myCharts[j] = myChart;
	}

	var data = getGpsQueryConditions();
	var url = lbs_api_server_servlet_path
			+ "/common/query/gps.json?select=gpsTime,io&orderBy=gpsTime&pageSize="
			+ MAX_QUERY_SIZE + "&pageNo=1";
	ajaxAsyncPost(url, data, function(result) {
		for (var j = 0; j < myCharts.length; j++) {
			myCharts[j].hideLoading();
		}

		if (result.code != 0) {
			showErrorMessage(result.message);
			return;
		}

		var data = [];
		for (var j = 0; j < ioStateMap.size(); j++) {
			data[j] = [];
		}
		var lastState = [];
		result = result.data;
		for (var i = 0, l = result.length; i < l; i++) {
			var item = result[i];
			var time = new Date(item.gpsTime).format();
			var io = item.io;
			if (!io) {
				continue;
			}
			io = io.toString(2);
			for (var j = 0; j < ioStateMap.size(); j++) {
				var state = io.charAt(j);
				if (i == 0 || (i + 1) == l || state != lastState[j]) {
					data[j].push({
						name : time,
						value : [ time, state == 1 ? "开" : "关" ]
					});
				}
				lastState[j] = state;
			}
		}

		// 指定图表的配置项和数据
		yAxisData = [ '关', '开' ];
		for (var j = 0; j < myCharts.length; j++) {
			var title = ioStateMap.get(j) + "分析";
			myCharts[j].setOption(getStateTimeLineOption(title, data[j],
					yAxisData));
		}
	});
}

function lastHourgnss() {
	var deviceId = $("#search-deviceId").val();
	if (!deviceId) {
		showMessage("请选择要查看的车辆！");
		return;
	}
	var deviceName = $("#search-deviceId").find("option:selected").text();
	var myChart = echarts.init(document.getElementById('charts_div'));
	myChart.showLoading();
	var data = getGpsQueryConditions();
	var url = lbs_api_server_servlet_path
			+ "/common/query/gps.json?select=gpsTime,gnss&orderBy=gpsTime&pageSize="
			+ MAX_QUERY_SIZE + "&pageNo=1";
	ajaxAsyncPost(url, data, function(result) {
		myChart.hideLoading();
		if (result.code != 0) {
			showErrorMessage(result.message);
			return;
		}
		var title = '卫星定位数分析';
		var data = [];
		result = result.data;
		var lastGnss = 0;
		for (var i = 0, l = result.length; i < l; i++) {
			var item = result[i];
			var time = new Date(item.gpsTime).format();
			var gnss = item.gnss;
			if (i == 0 || (i + 1) == l
					|| gnss != lastGnss) {
				data.push({
					name : time,
					value : [ time, gnss ]
				});
			}
			lastGnss = gnss;
		}
		// 指定图表的配置项和数据
		myChart.setOption(getStepLineOption(title, data));
	});
}

function lastHourCircuitState() {
	var deviceId = $("#search-deviceId").val();
	if (!deviceId) {
		showMessage("请选择要查看的车辆！");
		return;
	}
	var deviceName = $("#search-deviceId").find("option:selected").text();
	var myChart = echarts.init(document.getElementById('charts_div'));
	myChart.showLoading();
	var data = getGpsQueryConditions();
	var url = lbs_api_server_servlet_path
			+ "/common/query/gps.json?select=gpsTime,state&orderBy=gpsTime&pageSize="
			+ MAX_QUERY_SIZE + "&pageNo=1";
	ajaxAsyncPost(url, data, function(result) {
		myChart.hideLoading();
		if (result.code != 0) {
			showErrorMessage(result.message);
			return;
		}
		var title = '电路状态分析';
		var data = [];
		result = result.data;
		var lastState = 0;
		for (var i = 0, l = result.length; i < l; i++) {
			var item = result[i];
			var time = new Date(item.gpsTime).format();
			var state = ((item.state&(1<<11))>>11);
			if (i == 0 || (i + 1) == l || state != lastState) {
				data.push({
					name : time,
					value : [ time, state == 1 ? "开" : "关" ]
				});
			}
			lastState = state;
		}
		// 指定图表的配置项和数据
		yAxisData = [ '关', '开' ];
		myChart.setOption(getStateTimeLineOption(title, data, yAxisData));
	});
}

function lastHourCarLockState(){
	var deviceId = $("#search-deviceId").val();
	if (!deviceId) {
		showMessage("请选择要查看的车辆！");
		return;
	}
	var deviceName = $("#search-deviceId").find("option:selected").text();
	 var myChart = echarts.init(document.getElementById('charts_div'));
	 myChart.showLoading();
	 var data = getGpsQueryConditions();
	 var url=lbs_api_server_servlet_path+"/common/query/gps.json?select=gpsTime,state&orderBy=gpsTime&pageSize="+MAX_QUERY_SIZE+"&pageNo=1";
	 ajaxAsyncPost(url,data,function (result) {
		    myChart.hideLoading();
		    if (result.code!=0) {
				showErrorMessage(result.message);
				return;
			}
		    var title = '车门状态分析';
		    var data=[];
		    result = result.data;
		    var lastState = 0;
			for(var i=0,l=result.length;i<l;i++){
				var item=result[i];
				var time =new Date(item.gpsTime).format();
				var state = ((item.state&(1<<12))>>12);
				if(i==0 || (i+1)==l || state!=lastState){
					data.push({
			            name: time,
			            value: [
			                time,
			                state==1?"开":"关"
			            ]
			        });
				}
				lastState = state;
			}
		    // 指定图表的配置项和数据
		    yAxisData=['关','开'];
	        myChart.setOption(getStateTimeLineOption(title,data,yAxisData));
 		});
}


function dailyAccStateLogs(){
	var deviceId = $("#search-deviceId").val();
	if (!deviceId) {
		showMessage("请选择要查看的车辆！");
		return;
	}
	var deviceName = $("#search-deviceId").find("option:selected").text();
	 var myChart = echarts.init(document.getElementById('charts_div'));
	 myChart.showLoading();
	 var date =$('#search-date').val();
	 var now =new Date(date);
	 var startTime= now.getTime();
	 var endTime= startTime + 24 *60 * 60 * 1000;
	 var data ={ 
				"recordDate":now.format("yyyyMMdd"),
				"conditions":{"deviceId.eq":deviceId},
		 		"rangeConditions":[{ "fieldName":"gpsTime", 
		 		                      "from":startTime,
									  "includeLower":true,
									  "includeUpper":true, 
									  "to":endTime 
									  }] 
			    }
	 var url=message_api_server_servlet_path+"/common/query/accStateLog.json?select=gpsTime,state&orderBy=gpsTime&pageSize="+MAX_QUERY_SIZE+"&pageNo=1";
	 ajaxAsyncPost(url,data,function (result) {
		    myChart.hideLoading();
		    if (result.code!=0) {
				showErrorMessage(result.message);
				return;
			}
		    var title = 'ACC状态分析';
		    var data=[];
		    result = result.data;
			for(var i=0;i<result.length;i++){
				var item=result[i];
				var time =new Date(item.gpsTime).format();
		        data.push({
		            name: time,
		            value: [
		                time,
		                (item.state==1?"开":"关")
		            ]
		        });
			}
		    // 指定图表的配置项和数据
		    yAxisData=['关','开'];
	        myChart.setOption(getStateTimeLineOption(title,data,yAxisData));
 		});
}

function dailyOnlineLogs(){
	var deviceId = $("#search-deviceId").val();
	if (!deviceId) {
		showMessage("请选择要查看的车辆！");
		return;
	}
	var deviceName = $("#search-deviceId").find("option:selected").text();
	 var myChart = echarts.init(document.getElementById('charts_div'));
	 myChart.showLoading();
	 var date =$('#search-date').val();
	 var data ={"recordDate":date.replace(/-/g,"")};
	 data["conditions"] = {"deviceId.eq" : deviceId}
	 var url=message_api_server_servlet_path+"/common/query/onlineLog.json?select=createTime,state&pageSize="+MAX_QUERY_SIZE+"&pageNo=1&orderBy=createTime";
	 ajaxAsyncPost(url,data,function (result) {
		    myChart.hideLoading();
		    if (result.code!=0) {
				showErrorMessage(result.message);
				return;
			}
		    var title = '在线时段分析';
		    var data=[];
		    result = result.data;
			for(var i=0;i<result.length;i++){
				var item=result[i];
				var time =new Date(item.createTime).format();
		        data.push({
		            name: time,
		            value: [
		                time,
		                (item.state==1?"在线":"离线")
		            ]
		        });
			}
		    // 指定图表的配置项和数据
		    yAxisData=['离线','在线'];
	        myChart.setOption(getStateTimeLineOption(title,data,yAxisData));
		});
}

function monthMileageOverview(){
	var deviceId = $("#search-deviceId").val();
	if (!deviceId) {
		showMessage("请选择要查看的车辆！");
		return;
	}
	var deviceName = $("#search-deviceId").find("option:selected").text();
	
	 var month =$('#search-date').val();
	 if(month==""){
		 month = new Date().format("yyyy-MM")
	 }
	 var myChart = echarts.init(document.getElementById('charts_div'));
	 var myChart1 = echarts.init(document.getElementById('charts_div1'));
	 myChart.showLoading();
	 myChart1.showLoading();
	 var url=report_api_server_servlet_path+"/common/gpsInfoDayReport/"+month.replace("-","")+"_"+deviceId+".json";
	 ajaxAsyncGet(url,{},function (result) {
		    myChart.hideLoading();
		    myChart1.hideLoading();
		    if (result.code!=0) {
				showErrorMessage(result.message);
				return;
			}
			var dataAxis = [];
			var data = [];
			var sum=0;
			var row = result.data;
			if(row){
				var date = getMonthLastDay(month).format("yyyy-MM-dd");
				var lastDay = parseInt(date.split("-")[2]);
				for(var i=1;i<=lastDay;i++){
					var s =i;
					 if(i<10){
						 s="0"+i;
					 }
					 var item = row["d"+s];
					 var mileage=0;
					 if(item){
						 mileage=item.mileage;
					 }
					 sum+=mileage;
					 dataAxis.push(s);
			         data.push(mileage.toFixed(2));
				}
			}
			var title=month.replace("-","/")+"月行驶里程，共计"+sum.toFixed(2)+"km";
			myChart.setOption(getBarOption(title, dataAxis, data));
			myChart1.setOption(getLineCategoryOption(title, dataAxis, data,"km"));
		});
}

function monthOilmassOverview(){
	var deviceId = $("#search-deviceId").val();
	if (!deviceId) {
		showMessage("请选择要查看的车辆！");
		return;
	}
	var deviceName = $("#search-deviceId").find("option:selected").text();
	
	 var month =$('#search-date').val();
	 if(month==""){
		 month = new Date().format("yyyy-MM")
	 }
	 
	 var myChart = echarts.init(document.getElementById('charts_div'));
	 var myChart1 = echarts.init(document.getElementById('charts_div1'));
	 myChart.showLoading();
	 myChart1.showLoading();

	 var url=report_api_server_servlet_path+"/common/gpsInfoDayReport/"+month.replace("-","")+"_"+deviceId+".json";
	 ajaxAsyncGet(url,{},function (result) {
		    myChart.hideLoading();
		    myChart1.hideLoading();
		    if (result.code!=0) {
				showErrorMessage(result.message);
				return;
			}
			var dataAxis = [];
			var data = [];
			var sum=0;
			var row = result.data;
			if(row){
				var date = getMonthLastDay(month).format("yyyy-MM-dd");
				var lastDay = parseInt(date.split("-")[2]);
				for(var i=1;i<=lastDay;i++){
					var s =i;
					 if(i<10){
						 s="0"+i;
					 }
					 var item = row["d"+s];
					 var oilmass=0;
					 if(item){
						 oilmass=item.oilmass;
					 }
					 sum+=oilmass;
					 dataAxis.push(s);
			         data.push(oilmass.toFixed(2));
				}
			}
		    var title=month.replace("-","/")+"月油耗，共计"+sum.toFixed(2)+"L";
			myChart.setOption(getBarOption(title, dataAxis, data));
			myChart1.setOption(getLineCategoryOption(title, dataAxis, data,"L"));
		});
}

function monthAvgOilmass(){
	var deviceId = $("#search-deviceId").val();
	if (!deviceId) {
		showMessage("请选择要查看的车辆！");
		return;
	}
	var deviceName = $("#search-deviceId").find("option:selected").text();
	
	 var month =$('#search-date').val();	 if(month==""){
		 month = new Date().format("yyyy-MM")
	 }
	 var myChart = echarts.init(document.getElementById('charts_div'));
	 myChart.showLoading();
	 var url=report_api_server_servlet_path+"/common/gpsInfoDayReport/"+month.replace("-","")+"_"+deviceId+".json";
	 ajaxAsyncGet(url,{},function (result) {
		    myChart.hideLoading();
		    if (result.code!=0) {
				showErrorMessage(result.message);
				return;
			}
			var dataAxis = [];
			var data = [];
			var row = result.data;
			if(row){
				var date = getMonthLastDay(month).format("yyyy-MM-dd");
				var lastDay = parseInt(date.split("-")[2]);
				for(var i=0;i<lastDay;i++){
					var s =i;
					 if(i<10){
						 s="0"+i;
					 }
					 var item = row["d"+s];
					 var avgOilmass=0;
					 if(item){
						 avgOilmass=item.avgOilmass;
					 }
					 dataAxis.push(s);
			         data.push(avgOilmass.toFixed(2));
				}
			}
		    // 指定图表的配置项和数据
	        myChart.setOption(getLineCategoryOption("每百公里平均油耗分析", dataAxis,data,"L"));
 		});
}

function monthOfficersCarCostOverview() {
    var myChart = echarts.init(document.getElementById('charts_div'));
	myChart.showLoading();
	var month =$('#search-date').val();
	if(month==""){
		month = new Date().format("yyyy-MM")
	}
	var deviceId="";
	var deviceName="";
	if(query){
		deviceId = $("#search-deviceId").val();
		deviceName = $("#search-deviceId").find("option:selected").text();
	}
	var url = officerCar_api_server_servlet_path + "/report/monthOfficersCarCostOverview/"+month.replace("-","")+".json?deviceId="+deviceId;
	ajaxAsyncGet(url, {}, function(result) {
		myChart.hideLoading();
		if (result.code != 0) {
			showErrorMessage(result.message);
			return;
		}
		var title="企业";
		if(deviceId!=""){
			title=deviceName+"-"
		}
		title+=month.replace("-","/")+"月用车费用概况";
		var legend = ["加油费","停车费","过杆费","保养维修费","保险","其他"];
		var data = [{value:0,name:legend[0]},{value:0,name:legend[1]},{value:0,name:legend[2]},{value:0,name:legend[3]},{value:0,name:legend[4]},{value:0,name:legend[5]}];
		result = result.data;
		if(result.oilmass){
			data[0]={value:result.oilmass,name:legend[0]};
			data[1]={value:result.parking,name:legend[1]};
			data[2]={value:result.road,name:legend[2]};
			data[3]={value:result.maintenance,name:legend[3]};
			data[4]={value:result.insurance,name:legend[4]};
			data[5]={value:result.other,name:legend[5]};
		}
		myChart.setOption(getPieOption(title, legend, data));
	});
}