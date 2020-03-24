var carBiztypeMap = new JsMap();
var areaMap = new JsMap();
areaMap.put('110000', '北京');
areaMap.put('120000', '天津');
areaMap.put('310000', '上海');
areaMap.put('500000', '重庆');
areaMap.put('130000', '河北');
areaMap.put('410000', '河南');
areaMap.put('530000', '云南');
areaMap.put('210000', '辽宁');
areaMap.put('230000', '黑龙江');
areaMap.put('430000', '湖南');
areaMap.put('340000', '安徽');
areaMap.put('370000', '山东');
areaMap.put('650000', '新疆');
areaMap.put('320000', '江苏');
areaMap.put('330000', '浙江');
areaMap.put('360000', '江西');
areaMap.put('420000', '湖北');
areaMap.put('450000', '广西');
areaMap.put('620000', '甘肃');
areaMap.put('140000', '山西');
areaMap.put('150000', '内蒙古');
areaMap.put('610000', '陕西');
areaMap.put('220000', '吉林');
areaMap.put('350000', '福建');
areaMap.put('520000', '贵州');
areaMap.put('440000', '广东');
areaMap.put('630000', '青海');
areaMap.put('540000', '西藏');
areaMap.put('510000', '四川');
areaMap.put('640000', '宁夏');
areaMap.put('460000', '海南');
areaMap.put('710000', '台湾');
areaMap.put('810000', '香港');
areaMap.put('820000', '澳门');

$('document').ready(function () {
    $("body").css('visibility', 'visible');
    initCarBiztypeMap();
    staticOnlineCar();
	staticAreaCar();
	staticCarType();
	staticCarState();
	staticCarMileage();
	staticCarAlarm();
	// modify by tyj 20200317
	staticCarAvgOilmass();
	loadLastAlarmInfo();

    setInterval(function() {
		$("#time").html(new Date().format());
	}, 1000);
    
    setInterval(function() {
    	loadLastAlarmInfo();
    	staticOnlineCar();
	}, 60*1000);
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

function countAreaCar(areaCode,index,data){
	var url = report_api_server_servlet_path + "/area/devices.json?isParent=true";
	var name = areaMap.get(areaCode);
	ajaxAsyncPost(url, {"areaCode":areaCode,"type":3},function(result){
		var keys = areaMap.getKeys();
		if(result.code==0){
			var list = result.data;
			if(list && list.length>0){
				data.push({name:name,value:list.length});
			}else{
				data.push({name:name,value:0});
			}
		}else{
			data.push({name:name,value:0});
		}
		if(index<keys.length){
			index++;
			var areaCode = keys[index];
			countAreaCar(areaCode,index,data);
			if(index%3==0){
				setMapOption(data);
			}
		}else{
			setMapOption(data);
		}
	});
}

var mapChart;
function staticAreaCar(){
	//中国地图开始
	mapChart =echarts.init(document.getElementById("china_map"),'infographic');
	var keys = areaMap.getKeys();
	var data = [];
	var index = 0;
	data.push({name:"南海诸岛",value:0});
	countAreaCar(keys[index],index,data);
}

function setMapOption(data){
	var option = {
			title: {
				text: '车辆分布图',
				textStyle:{color:'#fff'},
				x:'center'
			},
			tooltip : {
				trigger: 'item'
			},
			visualMap: {
				show : false,
				x: 'left',
				y: 'bottom',
				splitList: [ 
					{start: 5000, end:10000},{start: 1000, end: 5000},
					{start: 500, end: 1000},{start: 100, end: 500},
					{start: 1, end: 100},{start: 0, end: 0},
				],
				color: ['#00CC00', '#ffff00', '#0E94EB','#6FBCF0', '#F0F06F', '#EEEEEE']
			},
			series: [{
				name: '车辆分布',
				type: 'map',
				mapType: 'china', 
				roam: true,
				label: {
					normal: { show: false },
					emphasis: { show: false }
				},
				data:data
			}]
		};

		mapChart.setOption(option);
		//中国地图结束
		window.addEventListener('resize', function() {
			mapChart.resize();
		});
}

/**
function staticAreaCar(){
	var url = report_api_server_servlet_path + "/provinceCarOverView.json?isParent=true";
	ajaxAsyncGet(url, {}, function(result) {
		if (result.code != 0) {
			showErrorMessage(result.message);
		} else {
			var dataMap = new JsMap();
			var list = result.data;
			for(var i=0,l=list.length;i<l;i++){
				var item = list[i];
				var areaCode = item.areaCode;
				var total = item.total;
				dataMap.put(areaCode,total);
			}
			var data = [];
			var keys = areaMap.getKeys();
			for(var i=0;i<keys.length;i++){
				var key = keys[i];
				var name = areaMap.get(key);
				var total = dataMap.get(key);
				if(total){
					data.push({name:name,value:total});
				}else{
					data.push({name:name,value:0});
				}
			}
			var option = {
					title: {
						text: '车辆分布图',
						textStyle:{color:'#fff'},
						x:'center'
					},
					tooltip : {
						trigger: 'item'
					},
					visualMap: {
						show : false,
						x: 'left',
						y: 'bottom',
						splitList: [ 
							{start: 500, end:600},{start: 400, end: 500},
							{start: 300, end: 400},{start: 200, end: 300},
							{start: 100, end: 200},{start: 0, end: 100},
						],
						color: ['#ff0', '#ffff00', '#0E94EB','#6FBCF0', '#F0F06F', '#00CC00']
					},
					series: [{
						name: '车辆分布',
						type: 'map',
						mapType: 'china', 
						roam: true,
						label: {
							normal: { show: false },
							emphasis: { show: false }
						},
						data:data
					}]
				};
			
			//中国地图开始
			var myChart =echarts.init(document.getElementById("china_map"),'infographic');
			myChart.setOption(option);
			//中国地图结束
			window.addEventListener('resize', function() {
				myChart.resize();
			});
		}
	});
}
**/

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
			
			var myChart = echarts.init($("#pie-car-type")[0]);
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
				var myChart = echarts.init($("#pie-car-state")[0]);
				myChart.setOption(getPieOption("", legend, data,true));
				window.addEventListener('resize', function() {
					myChart.resize();
				});
			}
		});
		/**
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
				var myChart = echarts.init($("#pie-car-state")[0]);
				myChart.setOption(option);
				window.addEventListener('resize', function() {
					myChart.resize();
				});
			}
		});
	}*/
}


/**平均油耗统计**/
function staticCarAvgOilmass() {
	var url = management_api_server_servlet_path + "/common/query/gpsInfoDayReport.json?select=totalAvgOilmass&desc=true&countable=false&orderBy=totalAvgOilmass&pageNo=1&pageSize=1&isParent=true";
	var param = { "recordDate" : new Date().format("yyyyMM") };
	var result = ajaxSyncPost(url, param);
	var max = 6;
	if (result.code == 0) {
		var list = result.data;
		if(list.length>0){
			var item = list[0];
			if(item.totalAvgOilmass > max){
				max = (item.totalAvgOilmass+2);
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
		rangeCondition["fieldName"] = "totalAvgOilmass";
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
	
	var myChart = echarts.init($("#line-car-avg-oilmass")[0]);
	var option = {
		legend : {
			data : [ '百公里油耗' ],
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
			name : '百公里油耗',
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
	});
}

/**里程统计**/
function staticCarMileage() {
	var url = management_api_server_servlet_path + "/common/query/gpsInfoDayReport.json?select=totalMileage&desc=true&countable=false&orderBy=totalMileage&pageNo=1&pageSize=1&isParent=true";
	var param = { "recordDate" : new Date().format("yyyyMM") };
	var result = ajaxSyncPost(url, param);
	var max = 100;
	if (result.code == 0) {
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
	
	var myChart = echarts.init($("#line-car-mileage")[0]);
	var option = {
		legend : {
			data : [ '车辆行驶里程' ],
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
			name : '车辆行驶里程',
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
	});
}

/**车辆告警统计**/
function staticCarAlarm() {
	var myChart = echarts.init($("#bar-car-alarm")[0]);
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

function staticOnlineCar(){
	var total = countDevice({"state.gte":2,"state.lte":3});
	var online = countDevice({"state.eq":3});
	$("#box-car-state").html("在线 "+online+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;离线 "+(total-online));
}

function countDevice(params){
	var url = message_api_server_servlet_path + "/common/count/device.json?isParent=true";
	var result = ajaxSyncPost(url,params);
	if (result.code!=0) {
		showErrorMessage(result.message);
	}else{
		return result.data;
	}
}

function loadLastAlarmInfo(){
	var params = {}
	var today = new Date().format("yyyy/MM/dd");
	var rangeCondition = {};
	rangeCondition["fieldName"] = "alarmTime";
	rangeCondition["from"] = new Date(today).getTime();
	rangeCondition["includeLower"] = true;
	rangeCondition["includeUpper"] = true;
	rangeCondition["to"] = new Date().getTime();
	var rangeConditions = new Array();
	rangeConditions[0] = rangeCondition;
	params["rangeConditions"] = rangeConditions;

	var url = message_api_server_servlet_path + "/common/query/alarm.json?countable=false&isParent=true&orderBy=alarmTime&desc=true&pageSize=5";
	ajaxAsyncPost(url, params, function(result) {
		$(".message-box").html("");
		if (result.code != 0) {
			showErrorMessage(result.message);
		}else{
			var list = result.data;
			if (list && list.length > 0) {
				for(var i=0;i<list.length;i++){
					var item = list[i];
					var alarmStr = "";
					for (var j = 0; j < 50; j++) {
						var alarmType = carAlarmInfoMap.get(j);
						if (!alarmType) {
							continue;
						}
						var val = item["a" + j];
						if (val) {
							alarmStr += "," + alarmType;
						}
					}
					
					//*************苏标告警处理 start***************
					for(var j=0;j<adasAlarmDesc.length;j++){
						var s = adasAlarmDesc[j];
						if(s && item["adas"+(j+1)]){
							alarmStr += "," + s;
						}
					}
					
					for(var j=0;j<dsmAlarmDesc.length;j++){
						var s = dsmAlarmDesc[j];
						if(s && item["dsm"+(j+1)]){
							alarmStr += "," + s;
						}
					}
					
					//20个轮胎足够了
					for(var j=0;j<20;j++){
						var alarm = item["tpm"+j];
						if(alarm){
							for (var n = 0; n < tpmAlarmDesc.length; n++) {
								var s = "";
								if((((alarm&(1<<n))>>n)==1)) {
									s = tpmAlarmDesc[n];
								}
								if (s!="") {
									alarmStr += "," + s;
								}
							}
						}
					}
					
					for(var j=0;j<bsdAlarmDesc.length;j++){
						var s = bsdAlarmDesc[j];
						if(s && item["bsd"+(j+1)]){
							alarmStr += "," + s;
						}
					}
					//*************苏标告警处理 end***************
					
					//1078视频告警
					if(item["va"]){
						var videoAlarm = item["va"];
						for(var j=0;j<videoAlarmDesc.length;j++){
							var s = videoAlarmDesc[j];
							if(s && ((videoAlarm&(1<<j))>>j)==1 ){
								alarmStr += "," + s;
							}
						}
					}
					
					var device = getDevice(item.deviceId);
					var alarmTime = new Date(item.alarmTime).format("hh:mm:ss");
					if(device){
						var content = "&nbsp;[" + alarmTime + "]&nbsp;" + device.name + alarmStr;
						if(content.length > 60){
							content = "<p title='"+content+"'>"+content.substr(0,60)+"…</p>";
						}else{
							content = "<p>"+content+"</p>"
						}
						$(".message-box").append(content);
					}
				}
			}

			var size = $(".message-box > p").size();
			if(size==0){
				$(".message-box").append("<p id='no-alarm-box'>暂无告警信息</p>");
			}
		}
	});
}

function getDevice(deviceId) {
	var url = management_api_server_servlet_path + "/common/device/" + deviceId + ".json";
	var result = ajaxSyncGet(url, {});
	if (result.code != 0) {
		showErrorMessage(result.message);
		return null;
	}
	return result.data;
}

var full_screen = false;
function onClickFullscreen(){
	if(full_screen){
		full_screen = false;
		exitFullscreen()
	}else{
		full_screen = true;
		fullscreen();
	}
}