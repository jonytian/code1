var MAP_TYPE = 1, MAP_DIV = "map_div";
var total_using_car = 0;
var carBiztypeMap = new JsMap();

$(function() {
	initCarBiztypeMap();
	initTotalCar();
	initTotalMileageAndOilmass();
	initTotalOnlineTime();
	initMonthMileageTop5();
	initMonthAvgOilmassTop5();
	initMonthOnlineTimeTop5();
	initSpeedTop5();
	staticCarType();
	staticCarState();
	staticCarMileage();
	staticCarAlarm();
});

function initCarBiztypeMap() {
	var url = management_api_server_servlet_path + "/system/dictionary/"+dictionary_officers_car_bizType+".json";
	var result = ajaxSyncGet(url, {});
	if (result.code != 0) {
		showErrorMessage(result.message);
	} else {
		var list = result.data;
		if(list && list.length>0){
			for (var i = 0, l = list.length; i < l; i++) {
				var item = list[i];
				carBiztypeMap.put(parseInt(item.code),item.content);
			}
		}
	}
}

function onLoadMapSuccess() {
	var url = lbs_api_server_servlet_path + "/enterprise/devices/position.json";
	ajaxAsyncGet(url, {}, function(result) {
		if (result.code != 0) {
			showErrorMessage(result.message);
		} else {
			var list = result.data;
			if(list && list.length>0){
				var markers = [];
				for (var index = 0, l = list.length; index < l; index++) {
					var item = list[index];
					var gcj02 = LngLatConverter.wgs84togcj02(item[4], item[5]);
					var point = mapUtil.getPoint(gcj02[0], gcj02[1]);
					var deviceId = item[0];
					var bizState = item[3];
					var iconUrl = getCarStateIcon(bizState);
					var marker = mapUtil.getMarker(deviceId, point, item[1], iconUrl, item[6]);
					//var marker = mapUtil.getMarker(deviceId, point, name, iconUrl, direction);

					markers.push(marker);
				}
				setDeviceMarkerClusterer(markers);
			}
		}
	});
}

function initTotalCar() {
	var url = management_api_server_servlet_path + "/common/count/device.json?isParent=true";
	var con = {};
	con["state.gte"] = 2;
	con["state.lte"] = 3;
	var result = ajaxSyncPost(url, con);
	if (result.code != 0) {
		showErrorMessage(result.message);
	} else {
		total_using_car = result.data;
		$("#total-using-car-div").html(getFormatNumber(total_using_car));
	}

	var con = {};
	con["state.gte"] = 2;
	ajaxAsyncPost(url, con, function(result) {
		if (result.code != 0) {
			showErrorMessage(result.message);
		} else {
			$("#total-car-div").html(getFormatNumber(result.data));
		}
	});
}

function initTotalMileageAndOilmass() {
	var month = new Date().format("yyyyMM")
	var url = report_api_server_servlet_path + "/sum/gps_info_day_report/" + month + ".json?select=totalMileage,totalOilmass&isParent=true";
	ajaxAsyncGet(url, {}, function(result) {
		if (result.code != 0) {
			showErrorMessage(result.message);
		} else {
			var data = result.data;
			if (data.totalMileage) {
				$("#total-mileage-div").html(getFormatNumber(data.totalMileage,2));
				var avg = getFormatNumber((data.totalMileage / total_using_car));
				$("#avg-mileage-div").html(getFormatNumber(avg,2));
			}
		}
	});
}

function initTotalOnlineTime() {
	var month = new Date().format("yyyyMM")
	var url = report_api_server_servlet_path + "/sum/online_time_day_report/" + month + ".json?select=total&isParent=true";
	ajaxAsyncGet(url, {}, function(result) {
		if (result.code != 0) {
			showErrorMessage(result.message);
		} else {
			var data = result.data;
			if(data.total){
				var total = data.total / (1000 * 60 * 60);
				$("#total-online-time-div").html(getFormatNumber(total,2));
				var avg = total / total_using_car;
				$("#avg-online-time-div").html(getFormatNumber(avg,2));
			}
		}
	});
}

function initMonthMileageTop5() {
	var month = new Date().format("yyyy-MM");
	var url = report_api_server_servlet_path + "/query/gpsInfoDayReport.json?select=totalMileage,deviceId&pageSize=5&pageNo=1&orderBy=totalMileage&desc=true&isParent=true";
	var data = {
		"recordDate" : month.replace(/-/g, "")
	};
	ajaxAsyncPost(url, data, function(result) {
		if (result.code != 0) {
			showErrorMessage(result.message);
			return;
		}
		result = result.data;
		var i = 0, html;
		for (; i < result.length; i++) {
			var item = result[i];
			var totalMileage = item.totalMileage;
			var deviceName = getDeviceName(item.deviceId);
			html += "<tr><td>" + (i + 1) + "</td><td>" + deviceName + "</td><td>" + totalMileage.toFixed(2) + "</td></tr>";
		}
		for (; i < 5; i++) {
			html += "<tr><td>" + (i + 1) + "</td><td>&nbsp;</td><td>&nbsp;</td></tr>";
		}
		$("#total-mileage-top5-div").html(html);
	});
}

function initMonthAvgOilmassTop5() {
	var month = new Date().format("yyyy-MM");
	var url = report_api_server_servlet_path
			+ "/query/gpsInfoDayReport.json?select=totalAvgOilmass,deviceId&pageSize=5&pageNo=1&orderBy=totalAvgOilmass&desc=true&isParent=true";
	var data = {
		"recordDate" : month.replace(/-/g, "")
	};
	ajaxAsyncPost(url, data, function(result) {
		if (result.code != 0) {
			showErrorMessage(result.message);
			return;
		}
		result = result.data;
		var i = 0, html;
		for (; i < result.length; i++) {
			var item = result[i];
			var avgOilmass = item.totalAvgOilmass;
			var deviceName = getDeviceName(item.deviceId);
			html += "<tr><td>" + (i + 1) + "</td><td>" + deviceName + "</td><td>" + avgOilmass + "</td></tr>";
		}
		for (; i < 5; i++) {
			html += "<tr><td>" + (i + 1) + "</td><td>&nbsp;</td><td>&nbsp;</td></tr>";
		}
		$("#total-avg-oilmass-top5-div").html(html);
	});
}

function initMonthOnlineTimeTop5() {
	var month = new Date().format("yyyy-MM");
	var url = report_api_server_servlet_path + "/query/onlineTimeDayReport.json?select=total,deviceId&pageSize=5&pageNo=1&orderBy=total&desc=true&isParent=true";
	var data = {
		"recordDate" : month.replace(/-/g, "")
	};
	ajaxAsyncPost(url, data, function(result) {
		if (result.code != 0) {
			showErrorMessage(result.message);
			return;
		}
		result = result.data;
		var i = 0, html;
		for (; i < result.length; i++) {
			var item = result[i];
			var total = item.total / (1000 * 60 * 60);
			var deviceName = getDeviceName(item.deviceId);
			html += "<tr><td>" + (i + 1) + "</td><td>" + deviceName + "</td><td>" + total.toFixed(2) + "</td></tr>";
		}
		for (; i < 5; i++) {
			html += "<tr><td>" + (i + 1) + "</td><td>&nbsp;</td><td>&nbsp;</td></tr>";
		}
		$("#total-online-time-top5-div").html(html);
	});
}


function initSpeedTop5() {
	var url = lbs_api_server_servlet_path + "/gps/topSpeed/5.json?isParent=true";
	ajaxAsyncGet(url, {}, function(result) {
		if (result.code != 0) {
			showErrorMessage(result.message);
			return;
		}
		result = result.data;
		var i = 0, html;
		if(result && result.length>0){
			for (; i < result.length; i++) {
				var item = result[i];
				var deviceName = getDeviceName(item[0]);
				html += "<tr><td>" + (i + 1) + "</td><td>" + deviceName + "</td><td>" + item[1] + "</td></tr>";
			}
		}

		for (; i < 5; i++) {
			html += "<tr><td>" + (i + 1) + "</td><td>&nbsp;</td><td>&nbsp;</td></tr>";
		}
		$("#max-speed-top5-div").html(html);
	});
}

/**车辆类型统计**/
function staticCarType() {
	var url = report_api_server_servlet_path + "/deviceProtocolOverView.json?isParent=true";
	ajaxAsyncGet(url, {}, function(result) {
		if (result.code != 0) {
			showErrorMessage(result.message);
		} else {
			var list = result.data;
			var legend = [],data = [];
			for(var i=0,l=list.length;i<l;i++){
				var item = list[i];
				var type = item.type;
				var protocolVersion = item.protocol;
				var name = "";
				if (protocolVersion == "2011") {
					name = "JT/T808-2011";
				} else if (protocolVersion == "2013") {
					name = "JT/T808-2013";
				} else if (protocolVersion == "2015") {
					name = "JT/T808-2015";
				} else if (protocolVersion == "2016") {
					name = "JT/T808-2011+1078";
				}else if (protocolVersion == "201602") {
					name = "JT/T808-2013+1078";
				} else if (protocolVersion == "2019") {
					name = "JT/T808-2019";
				}else if (protocolVersion == "btobd") {
					name = "邦通OBD";
				}else if (protocolVersion == "tjsatl") {
					name = "T/JSATL-2018";
				}
				legend.push(name);
				data.push({value:item.total,name:name});
			}
			
			var myChart = echarts.init($("#char1")[0]);
			myChart.setOption(getPieOption("", legend, data,true));
			window.addEventListener('resize', function() {
				myChart.resize();
			})
		}
	});
}

/*****车辆状态统计*****/
function staticCarState() {
	//if(carBiztypeMap.isEmpty()){
		var url = report_api_server_servlet_path + "/deviceBizTypeOverView.json?isParent=true";
		ajaxAsyncGet(url, {}, function(result) {
			if (result.code != 0) {
				showErrorMessage(result.message);
			} else {
				var dataMap = new JsMap();
				var list = result.data;
				for(var i=0,l=list.length;i<l;i++){
					var item = list[i];
					var bizState = item.bizState;
					var total = item.total;
					dataMap.put(bizState,total);
				}
				
				var legend = [];
				var data = [];
				var deviceBizStateKeys = deviceBizStateMap.getKeys();
				for(var i=0;i<deviceBizStateKeys.length;i++){
					var bizState = deviceBizStateKeys[i];
					var name = deviceBizStateMap.get(bizState);
					var total = dataMap.get(bizState);
					if(total){
						data.push({value:total,name:name});
					}else{
						data.push({value:0,name:name});
					}
					legend.push(name);
				}
				var myChart = echarts.init($("#char2")[0]);
				myChart.setOption(getPieOption("", legend, data,true));
				window.addEventListener('resize', function() {
					myChart.resize();
				});
			}
		});
		/*
	}else{
		var url = report_api_server_servlet_path + "/carBiztypeAndBizStateOverView.json?isParent=true";
		ajaxAsyncGet(url, {}, function(result) {
			if (result.code != 0) {
				showErrorMessage(result.message);
			} else {
				var list = result.data;
				var lastType = null;
				var bizStateMap = new JsMap();
				var typeMap = new JsMap();
				for(var i=0,l=list.length;i<l;i++){
					var item = list[i];
					var type = item.type;
					if(type==null){
						continue;
					}
					if(lastType && lastType != type){
						typeMap.put(parseInt(lastType),bizStateMap);
						bizStateMap = new JsMap();
					}
					bizStateMap.put(parseInt(item.bizState),item.total);
					lastType = type;
				}
				typeMap.put(parseInt(lastType),bizStateMap);
	
				var legend = [], series = [];
				var deviceBizStateKeys = deviceBizStateMap.getKeys();
				var carBiztypeKeys = carBiztypeMap.getKeys();
	
				for(var i=0;i<deviceBizStateKeys.length;i++){
					var bizState = deviceBizStateKeys[i];
					var data = [];
					for(var j=0;j<carBiztypeKeys.length;j++){
						var type = carBiztypeKeys[j];
						var map = typeMap.get(type);
						if(map){
							var val = map.get(bizState);
							if(val){
								data.push(val);
							}else{
								data.push(0);
							}
						}else{
							data.push(0);
						}
					}
	
					var name = deviceBizStateMap.get(bizState);
					legend.push(name);
					var serie = {
						name : name,
						type : 'bar',
						stack : '总量',
						itemStyle : {
							normal : { label : {show : true,position : 'insideRight'} }
						},
						data : data
					};
					series.push(serie);
				}
	
				var yAxisData = [];
				for(var j=0;j<carBiztypeKeys.length;j++){
					yAxisData.push(carBiztypeMap.get(carBiztypeKeys[j]));
				}
				var option = {
						tooltip : {
							trigger : 'axis',
							axisPointer : { type : 'shadow'}
						},
						grid : {
							show : 'true',
							borderWidth : '0'
						},
						legend : {
							data : legend,
							textStyle : { color : '#ffffff' }
						},
						calculable : false,
						xAxis : [ {
							type : 'value',
							minInterval : 1,
							axisLabel : {
								show : true,
								textStyle : { color : '#fff' }
							},
							splitLine : {
								lineStyle : {
									color : [ '#f2f2f2' ],
									width : 0,
									type : 'solid'
								}
							}
	
						}],
						yAxis : [ {
							type : 'category',
							data : yAxisData,
							axisLabel : {
								show : true,
								textStyle : { color : '#fff' }
							},
							splitLine : {
								lineStyle : { width : 0, type : 'solid' }
							}
						} ],
						series :series
				};
				var myChart = echarts.init($("#char2")[0]);
				myChart.setOption(option);
				window.addEventListener('resize', function() {
					myChart.resize();
				});
			}
		});
	}*/
}

/**里程统计**/
function staticCarMileage() {
	var url = management_api_server_servlet_path + "/common/query/gpsInfoDayReport.json?select=totalMileage&desc=true&countable=false&orderBy=totalMileage&pageNo=1&pageSize=1&isParent=true";
	var param = { "recordDate" : new Date().format("yyyyMM") };
	var result = ajaxSyncPost(url, param);
	var max = 100;
	if (result.code != 0) {
		
	} else {
		var list = result.data;
		if(list.length>0){
			var item = list[0];
			if(item.totalMileage > max){
				max = (item.totalMileage+10);
			}
		}
	}
	
	var interval = Math.ceil(max/10);
	var yAxisData = [];
	yAxisData.push(0);
	var next = 0;
	for(var i=0;i<9;i++){
		next +=interval;
		yAxisData.push(parseInt((next+"").substr(0,(next+"").length-1)+"0"));
	}
	yAxisData.push(max);
	
	var data = [];
	var url = management_api_server_servlet_path + "/common/count/gpsInfoDayReport.json?isParent=true"; 
	for(var i=1;i<yAxisData.length;i++){
		var rangeCondition = {};
		rangeCondition["fieldName"] = "totalMileage";
		rangeCondition["from"] = yAxisData[i-1];
		rangeCondition["includeLower"] = true;
		rangeCondition["includeUpper"] = false;
		rangeCondition["to"] = yAxisData[i];
		var rangeConditions = new Array();
		rangeConditions[0] = rangeCondition;
		param["rangeConditions"] = rangeConditions;
		var result = ajaxSyncPost(url, param);
		if (result.code != 0) {
			data.push(0);
		} else {
			data.push(result.data);
		}
	}
	
	var myChart = echarts.init($("#char3")[0]);
	var option = {
		legend : {
			data : [ '车辆行驶数量' ],
			textStyle : {color : '#ffffff'}
		},
		grid : {
			show : 'true',
			borderWidth : '0'
		},
		calculable : false,
		tooltip : {
			trigger : 'axis',
			formatter : "共计：{c}辆"
		},
		xAxis : [ {
			type : 'value',
			minInterval : 1,
			axisLabel : {
				formatter : '{value}',
				textStyle : {
					color : '#fff'
				}
			},
			splitLine : {
				lineStyle : {
					width : 0,
					type : 'solid'
				}
			}
		} ],
		grid: {
	        left:60
	    },
		yAxis : [ {
			type : 'category',
			axisLine : {
				onZero : false
			},
			axisLabel : {
				formatter: function (value, index) {
				   return getFormatNumber(value);
				},
				textStyle : {
					color : '#fff'
				}
			},
			splitLine : {
				lineStyle : {
					width : 0,
					type : 'solid'
				}
			},
			boundaryGap : false,
			data : yAxisData
		} ],
		series : [ {
			name : '车辆行驶数量',
			type : 'line',
			smooth : true,
			itemStyle : {
				normal : {
					lineStyle : {
						shadowColor : 'rgba(0,0,0,0.4)'
					}
				}
			},
			data : data
		} ]
	};

	myChart.setOption(option);
	window.addEventListener('resize', function() {
		myChart.resize();
	})

}

/**车辆告警统计**/
function staticCarAlarm() {
	var myChart = echarts.init($("#char4")[0]);
    var  month = new Date().format("yyyy-MM");
	var url=report_api_server_servlet_path+"/enterpriseMonthAlarmOverview/"+month.replace("-","")+".json?isParent=true";
	ajaxAsyncGet(url,{},function (result) {
	    query=false;
	    myChart.hideLoading();
	    if (result.code!=0) {
			showErrorMessage(result.message);
			return;
		}
		var legend = ["超速告警","紧急告警","油量异常","围栏告警","非法用车","急加速","急刹车","急转弯"];
		var data = [];
		result = result.data;
		var sum=0;
		if(result.length<1){
			for(var i=0;i<legend.length;i++){
				data.push(0);
			}
		}else{
			var item=result[0];
			var val = 0;
			var total = (item['a35'] || val);
			total+= (item['a36'] || val);
			total+= (item['a39'] || val);
			total+= (item['a1'] || val);
			data.push(total);
			
			data.push(item['a0'] || val);
			data.push(item['a25'] || val);
			
			total = (item['a33'] || val);
			total+= (item['a34'] || val);
			total+= (item['a20'] || val);
			data.push(total);
			
			data.push(item['a32'] || val);
			data.push(item['a40'] || val);
			data.push(item['a41'] || val);
			data.push(item['a42'] || val);
		}
		myChart.setOption(getBarOption("", legend, data,true));
	});
	window.addEventListener('resize', function() {
		myChart.resize();
	});
}
