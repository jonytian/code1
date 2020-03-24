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
				<label>告警类型：</label>
				<select id="search-type" style="color:#FFFFFF">
				<option value="" style="color:#000000">选择类型</option>
				<option value="1" style="color:#000000">设备故障告警</option>
				<option value="2" style="color:#000000">驾驶行为违规告警</option>
				<option value="3" style="color:#000000">紧急重要告警</option>
				<option value="4" style="color:#000000">平台告警</option>
				<option value="5" style="color:#000000">视频告警</option>
				<option value="adas" style="color:#000000">ADAS告警</option>
				<option value="dsm" style="color:#000000">DMS告警</option>
				<option value="tpm" style="color:#000000">TPMS告警</option>
				<option value="bsd" style="color:#000000">BSD告警</option>
			    </select>
				<label>告警详情：</label><select id="search-detail">
				<option value="">选择详情</option>
			</select>
			</p>
			<p>
				<label>设备名称：</label><%@ include file="../pub/deviceSearchSelectPicker.jsp"%>
				<label>待确认告警：</label><input  type="checkbox"  id="search-state" >
				&nbsp;&nbsp;&nbsp;&nbsp;
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

<div id="alarm-confirm-dlg" title="告警确认" style="width: 650px; height: 380px;" class="simple-dialog">
	<form id="alarm-confirm-dlg-frm" method="post">
		<input name="deviceId" id="alarm-confirm-dlg-frm-deviceId" type="hidden" />
		<input name="alarmId" id="alarm-confirm-dlg-frm-alarmId" type="hidden"/>
		<input name="alarmEventId" id="alarm-confirm-dlg-frm-alarmEventId" type="hidden"/>
		<div class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label><span  style="color:#fff;" id="alarm-confirm-dlg-frm-deviceName"></span>
		</div>
		<div class="message_con">
			<label>告警详情：</label><span style="color:#fff;" id="alarm-confirm-dlg-frm-alarmDetail"></span>
		</div>
		<div class="message_con">
			<label style="vertical-align:top;">备注：</label>
			<textarea name="opinion" style="height: 200px;" maxlength="250" class="required"></textarea>
		</div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="doAlarmConfirm()">确定</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>

<%@ include file="../pub/mapScript.jsp"%>
<script type="text/javascript" src="<c:url value='/js/device/alarmList.js?v=20191202'/>"></script>
<script language="javaScript">
	$(document).ready(function() {
		if("${param.state}" || conditionsCache.get("search-state")=="true"){
			$(":checkbox[id='search-state']").prop("checked",true);
		}

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