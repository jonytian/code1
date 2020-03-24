<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/header.jsp"%>
<title>标注点管理</title>
</head>
<body>
	<div class="clear"></div>
	<!--内容部分-->
	<div class="con1 left" id="content-div">
		<div class="right  con_table">
			<div class="table_find">
				<p>
					<label>业务类型：</label>
					<select id="search-type">
						<option value="">选择类型</option>
						<option value="1">原地设防</option>
						<option value="2">围栏</option>
						<option value="3">超速</option>
						<option value="4">路线</option>
					</select>&nbsp;&nbsp;
					<button type="button" class="btn btn-primary btn-sm" onclick="doQuery()">
						<span class="glyphicon glyphicon-search"></span>查询
					</button>
				</p>
			</div>
			<div class="table_div">
				<table id="boot-strap-table" class="table_style" border="0"></table>
			</div>
			<p id="toolbar" class="table_but">
				<a href="javascript:void(0)" onclick="showIframe('设置围栏规则','editRailAlarmSetting.do',750,480);"><i class="glyphicon glyphicon-plus"></i>围栏告警</a>
				<a href="javascript:void(0)" onclick="showIframe('设置行政区域围栏规则','editDistrictAlarmSetting.do',750,550);"><i class="glyphicon glyphicon-plus"></i>行政区域围栏</a>
				<a href="javascript:void(0)" onclick="showIframe('设置超速告警规则','editOverspeedAlarmSetting.do',750,450);"><i class="glyphicon glyphicon-plus"></i>超速告警</a>
				<a href="javascript:void(0)" onclick="showIframe('设置路线告警规则','editRouteAlarmSetting.do',750,480);"><i class="glyphicon glyphicon-remove"></i>路线告警</a>
				<a href="javascript:void(0)" onclick="dels()"><i class="glyphicon glyphicon-remove"></i>删除</a>
			</p>
		</div>
	</div>
	<div id="map-dlg" class="simple-dialog" title="查看围栏" style="width:550px;height:450px;">
    	<div id="map_box" style="width:100%;height:100%;"></div>
	</div>
	<%@ include file="/common/common.jsp"%>
	<%@ include file="../pub/mapScript.jsp"%>
	<script type="text/javascript" src="<c:url value='/js/device/alarmSettingList.js?v=20191204'/>"></script>
</body>
</html>
