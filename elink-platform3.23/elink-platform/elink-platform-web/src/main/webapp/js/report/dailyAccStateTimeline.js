var mapUtil;
var isQuerying = true;
var current_index = 0;

$(function() {
	initDatepicker();
	$('#search-date').val(new Date().format("yyyy-MM-dd"));
	
	var key = "";
	loadSysDictionary(DICTIONARY_SYS_DICTIONARY,"map_key_gaode",function(list){
		if(list == null || list.length==0){
			showAlertMessage("获取地图key失败，请联系管理员配置地图key信息！");
			return;
		}
		var item = list[0];
		key = item.content;
	});
	mapUtil = new MapUtil();
	mapUtil.loadJScript(key);
});

function initMap() {
	mapUtil.init("map");
	doDefultQuery();
}

function doDefultQuery() {
	query = true;
	doQuery();
	query = false;
}

function doQuery() {
	if (!query && isQuerying) {
		return;
	}
	var deviceId = $("#search-deviceId").val();
	if (!deviceId) {
		showMessage("请选择要查看的车辆！");
		return;
	}

	isQuerying = true;
	var date = $('#search-date').val();
	try {
		var now = new Date(date);
		var startTime = now.getTime();
		var endTime = startTime + 24 * 60 * 60 * 1000;
		var data = {
			"recordDate" : now.format("yyyyMMdd"),
			"conditions" : {
				"deviceId.eq" : deviceId
			},
			"rangeConditions" : [ {
				"fieldName" : "gpsTime",
				"from" : startTime,
				"includeLower" : true,
				"includeUpper" : true,
				"to" : endTime
			} ]
		}
		var url = message_api_server_servlet_path
				+ "/common/query/accStateLog.json?orderBy=gpsTime&pageSize=1000&pageNo=1";
		ajaxAsyncPost(url, data, function(result) {
			if (result.code != 0) {
				showErrorMessage(result.message);
				return;
			}
			var list = result.data;
			var last = null, started = false;
			var html = "<h2></h2>";
			var total = 0, totalMileage = 0, totalOilmass = 0, totalTimes = 0;
			var addressPoints = [];
			for (var i = 0, l = list.length; i < l; i++) {
				var item = list[i];
				if (!started && item.state == 1) {
					last = item;
					started = true;
					addressPoints.push(item);
				} else if (started && item.state == 0) {
					var mileage = item.mileage - last.mileage;
					if (mileage > 0) {
						totalMileage += mileage;
						mileage = mileage.toFixed(2);
					}
					var oilmass = last.oilmass - item.oilmass;
					if (oilmass > 0) {
						totalOilmass += oilmass;
					}
					var avgOilmass = "-";
					if (mileage > 0 && oilmass > 0) {
						avgOilmass = (oilmass / mileage).toFixed(2);
					}
					var times = item.gpsTime - last.gpsTime;
					totalTimes += times;

					total++;
					
					html += "<li class='green'><h3>" + total
							+ ".</h3><dl><dt>"
							+ new Date(last.gpsTime).format("hh:mm:ss") + "~"
							+ new Date(item.gpsTime).format("hh:mm:ss")
							+ "<span>行驶时长："
							+ (times / (1000 * 60 * 60)).toFixed(2)
							+ "h&nbsp;&nbsp;里程：" + mileage
							+ "km&nbsp;&nbsp;油耗：" + oilmass
							+ "L&nbsp;&nbsp;平均油耗：" + avgOilmass
							+ "L</span><span id='address-"+addressPoints.length+"'>"
							+ "起点：{address}</span><span id='address-"
							+ (addressPoints.length + 1)+ "'>" 
							+ "终点：{address}</span></dt></dl></li>";
					started = false;
					last = item;
					addressPoints.push(item);
				}
			}

			var avgOilmass = "-";
			if (totalMileage > 0 && totalOilmass > 0) {
				avgOilmass = (totalOilmass / totalMileage).toFixed(2);
			}
			
			html += "<li class='green'><h3>&nbsp;&nbsp;</h3><dl><dt>今日行程<span>总计："
			+ total + "次&nbsp;&nbsp;行驶时长："
			+ (totalTimes / (1000 * 60 * 60)).toFixed(2)
			+ "h&nbsp;&nbsp;里程：" + totalMileage.toFixed(2)
			+ "km&nbsp;&nbsp;油耗：" + totalOilmass.toFixed(2)
			+ "L&nbsp;&nbsp;平均油耗：" + avgOilmass + "L</span></dt></dl></li>";
			
			$("#timeline-list").html(html);

			initTimeline();

			if (total > 0) {
				getAddress(addressPoints);
			}
		}, true);
	} catch (e) {
		isQuerying = false;
	}
}

function getAddress(addressPoints) {
	if (current_index < addressPoints.length) {
		try {
			var item = addressPoints[current_index++];
			var gcj02 = LngLatConverter.wgs84togcj02(item.lng, item.lat);
			var lacation = mapUtil.getPoint(gcj02[0], gcj02[1]);
			mapUtil.regeocoder(lacation, function(address) {
				if (address) {
					var html = $("#address-" + current_index).html();;
					$("#address-" + current_index).html(html.replace("{address}",address));
				}
				getAddress(addressPoints);
			});
		} catch (e) {
			getAddress(addressPoints);
		}
	} else {
		current_index = 0;
		isQuerying = false;
	}
}