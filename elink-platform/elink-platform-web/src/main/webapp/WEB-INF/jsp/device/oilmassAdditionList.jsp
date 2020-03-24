<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/header.jsp"%>
<title>加油疑点</title>
<%@ include file="/common/common.jsp"%>
</head>
<body>
	<div class="clear"></div>
	<!--内容部分-->
	<div class="con1 left" id="content-div">
		<div class="right  con_table">
			<div class="table_find">
				<p>
					<label>时间：</label><input id="search-date" type="text" readonly class="date form_date" data-date-format="yyyy-mm-dd">
					<label>车牌号：</label><%@ include file="../pub/carSearchSelectPicker.jsp"%> &nbsp;&nbsp;
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
	<script type="text/javascript" src="<c:url value='/js/device/oilmassAlarmList.js'/>"></script>
</body>
</html>