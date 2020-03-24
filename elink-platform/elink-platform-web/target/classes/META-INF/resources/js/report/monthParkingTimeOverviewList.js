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
	return report_api_server_servlet_path+ "/query/parkingTimeDayReport.json?orderBy=total&desc=true";
}

function getColumns() {
	var month = $('#search-date').val();
	if (!month) {
		month = new Date().format("yyyy-MM")
	}
	var date = getMonthLastDay(month).format("yyyy-MM-dd");
	var lastDay = parseInt(date.split("-")[2]);
	
	var h=1000*60*60;
	var columns = [ [] ];
	columns[0][0] = {
		field : "deviceName",
		title : "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;车牌号&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;",
		align : 'center',
		valign : 'middle',
		formatter : function(value, row, index) {
			var deviceId = row.deviceId;
			return getDeviceName(deviceId);
		}
	};
	columns[0][1] = {field:"total",title:"总计(小时)",width:"100px;",formatter: function(value,row,index){
		 var total = 0;
		 for(var i=1;i<=lastDay;i++){
			 var s =i;
			 if(i<10){
				 s="0"+i;
			 }
			 var val = row["d"+s];
			 if(val){
				 total+=val;
			 }
		 }
		 return (total/h).toFixed(2);
	}};
	for(var i=1;i<=lastDay;i++){
		 var s =i;
		 if(i<10){
			 s="0"+i;
		 }
		columns[0][1+i] = {field:"d"+s,title:s,align : 'center',
				valign : 'middle',formatter: function(value,row,index){
			return value?(value/h).toFixed(2):0;
		}};
	 }
	
	return columns;
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
	 var url = report_api_server_servlet_path+ "/excel/monthParkingTime/"+month+".json?deviceId="+deviceId;
	 var mediaUrl=getContextPath()+"/attachment/download.do?url="+url;
	 window.open(mediaUrl);
}