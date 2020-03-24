$(function() {
	initMonthpicker();
	$('#search-date').val(new Date().format("yyyy-MM"));
	initBootstrapTable();
});

function showPaginationDetail(){
	return false;
}

function getQueryParams() {
	var month = $('#search-date').val();
	if (!month) {
		month = new Date().format("yyyy-MM");
	}
	var data = {
		"recordDate" : month.replace(/-/g, "")
	};
	var deviceId = $("#search-deviceId").val();
	if (query && deviceId) {
		data["conditions"] = {
			"deviceId.eq" : deviceId
		}
	}
	return data;
}

function getQueryUrl() {
	 return report_api_server_servlet_path+ "/query/deviceDataCountMonthReport.json?orderBy=total&desc=true";
}

function getColumns() {
	var month = $('#search-date').val();
	if (!month) {
		month = new Date().format("yyyy-MM")
	}
	var date = getMonthLastDay(month).format("yyyy-MM-dd");
	var lastDay = parseInt(date.split("-")[2]);
	return [ [ {
		field : "deviceName",
		title : "&nbsp;&nbsp;&nbsp;&nbsp;车牌号&nbsp;&nbsp;&nbsp;&nbsp;",
		align : 'center',
		valign : 'middle',
		formatter : function(value, row, index) {
			var deviceId = row.deviceId;
			return getDeviceName(deviceId);
		}
	}, {
		field : "totalSize",
		title : "总流量(M)",
		formatter : function(value, row, index) {
			var total = 0;
			for (var i = 1; i <= lastDay; i++) {
				var s = i;
				if (i < 10) {
					s = "0" + i;
				}
				var val = row["d" + s];
				if (val) {
					var dataSize = val.dataSize;
					if (dataSize) {
						total += dataSize;
					}
					var mediaSize = val.mediaSize;
					if (mediaSize) {
						total += mediaSize;
					}
				}
			}
			return (total / (1024 * 1024)).toFixed(2);
		}
	}, {
		field : "mediaSize",
		title : "多媒体流量(M)",
		formatter : function(value, row, index) {
			var total = 0;
			for (var i = 1; i <= lastDay; i++) {
				var s = i;
				if (i < 10) {
					s = "0" + i;
				}
				var val = row["d" + s];
				if (val) {
					var mediaSize = val.mediaSize;
					if (mediaSize) {
						total += mediaSize;
					}
				}
			}
			return (total / (1024 * 1024)).toFixed(2);
		}
	}, {
		field : "dataSize",
		title : "上行消息流量(M)",
		formatter : function(value, row, index) {
			var total = 0;
			for (var i = 1; i <= lastDay; i++) {
				var s = i;
				if (i < 10) {
					s = "0" + i;
				}
				var val = row["d" + s];
				if (val) {
					var dataSize = val.dataSize;
					if (dataSize) {
						total += dataSize;
					}
				}
			}
			return (total / (1024 * 1024)).toFixed(2);
		}
	}, {
		field : "messageCount",
		title : "上行消息数量(条)",
		formatter : function(value, row, index) {
			var total = 0;
			for (var i = 1; i <= lastDay; i++) {
				var s = i;
				if (i < 10) {
					s = "0" + i;
				}
				var val = row["d" + s];
				if (val) {
					for ( var key in val) {
						if (key != "dataSize" && key != "mediaSize") {
							total += val[key];
						}
					}
				}
			}
			return total;
		}
	} ] ];
}

// 导出excel added by yaojiang.tian at 20200305
function exportExcel() {
    var month = $('#search-date').val();
    if (!month) {
        month = new Date().format("yyyy-MM")
    }
    var deviceId = "";
    if (query) {
        deviceId = $("#search-deviceId").val();
    }
    month = month.replace(/-/g, "");
    var url = report_api_server_servlet_path+ "/excel/monthDataCount/"+month+".json?deviceId="+deviceId;
    var mediaUrl=getContextPath()+"/attachment/download.do?url="+url;
    window.open(mediaUrl);
}