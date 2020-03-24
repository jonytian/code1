<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/header.jsp"%>
<title>设备告警</title>
<%@ include file="/common/common.jsp"%>
</head>
<body>
	<div class="clear"></div>
	<!--内容部分-->
	<div class="con1 left" id="content-div">
		<div class="right  con_table">
			<div class="table_find">
				<p>
					<label>告警时间：</label><input id="search-date" type="text" readonly class="date form_date" data-date-format="yyyy-mm-dd">
					<label>告警类型：</label><select id="search-type">
						<option value="">选择类型</option>
						<option value="1">设备故障告警</option>
						<option value="2">驾驶行为违规告警</option>
						<option value="3">紧急重要告警</option>
						<option value="4">平台告警</option>
						<option value="5">视频告警</option>
						<option value="adas">ADAS告警</option>
						<option value="dsm">DMS告警</option>
						<option value="tpm">TPMS告警</option>
						<option value="bsd">BSD告警</option>
					</select>
					<label>告警详情：</label><select id="search-detail">
						<option value="">选择详情</option>
					</select>
					</p>
					<p>
					<label>设备名称：</label><%@ include file="../pub/deviceSearchSelectPicker.jsp"%>&nbsp;&nbsp;&nbsp;&nbsp;
					<button class="btn btn-primary btn-sm" onclick="onClickSearch()">
						<span class="glyphicon glyphicon-search"></span>查询
					</button>
				</p>
			</div>
			<div class="table_div">
				<table id="boot-strap-table" class="table_style" border="0"></table>
			</div>
		</div>
	</div>
	<div id="map-dlg" class="simple-dialog" title="查看告警" style="width:550px;height:450px;">
    	<div id="map_box" style="width:100%;height:100%;"></div>
	</div>
	<%@ include file="../pub/mapScript.jsp"%>
	<script type="text/javascript" src="<c:url value='/js/device/alarmList.js?v=20191108'/>"></script>
	<script language="javaScript">
		$(document).ready(function() {
			initDatepicker();
			
			var searchDate = conditionsCache.get("search-date");
			if(!searchDate){
				searchDate=new Date().format("yyyy-MM-dd")
			}
			
			$('#search-date').val(searchDate);
			
			var searchType = conditionsCache.get("search-type");
			if(searchType){
				$("#search-type").val(searchType);
				setSearchDetail(searchType);
			}
			
			var searchDetail = conditionsCache.get("search-detail");
			if(searchDetail){
				$("#search-detail").val(searchDetail);
			}
			
			initBootstrapTable();
			
			$("#search-type").change(function(){
                var type=$(this).children('option:selected').val();
                setSearchDetail(type);
			});
		});
	</script>
</body>
</html>