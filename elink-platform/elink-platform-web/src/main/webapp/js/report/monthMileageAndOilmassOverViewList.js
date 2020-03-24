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
		month = new Date().format("yyyy-MM")
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
	return report_api_server_servlet_path + "/query/gpsInfoDayReport.json?select=deviceId,totalOilmass,totalMileage,totalAvgOilmass&orderBy=totalMileage&desc=true";
}

function getColumns() {
	return [ [ {
		field : "deviceName",
		title : "车牌号",
		align : 'center',
		valign : 'middle',
		formatter : function(value, row, index) {
			var deviceId = row.deviceId;
			return getDeviceName(deviceId);
		}
	}, {
		field : "totalMileage",
		title : "总里程(km)",
		align : 'center',
		valign : 'middle',
		formatter : function(value, row, index) {
			if (row.totalMileage > 0) {
				return row.totalMileage.toFixed(2);
			}
			return "-";
		}
	}, {
		field : "totalOilmass",
		title : "总油耗(L)",
		align : 'center',
		valign : 'middle',
		formatter : function(value, row, index) {
			if (row.totalOilmass > 0) {
				return row.totalOilmass.toFixed(2);
			}
			return "-";
		}
	}, {
		field : "totalAvgOilmass",
		title : "平均油耗(L/km)",
		align : 'center',
		valign : 'middle',
		formatter : function(value, row, index) {
			var avgOilmass = "-";
			if (row.totalAvgOilmass > 0) {
				avgOilmass = (row.totalAvgOilmass).toFixed(2);
			}
			return avgOilmass;
		}
	} ] ];
}

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
	var url = report_api_server_servlet_path + "/excel/oilmass/" + month + ".json?deviceId=" + deviceId;
	var mediaUrl = getContextPath() + "/attachment/download.do?url=" + url;
	window.open(mediaUrl);
}