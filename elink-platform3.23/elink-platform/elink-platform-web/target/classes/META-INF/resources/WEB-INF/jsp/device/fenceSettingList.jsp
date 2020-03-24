<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/header.jsp"%>
<%@ include file="/common/common.jsp"%>
<title>终端围栏管理</title>
</head>
<body>
	<div class="clear"></div>
	<!--内容部分-->
	<div class="con1 left" id="content-div">
		<div class="right  con_table">
			<div class="table_find">
				<p>
					<label>围栏类型：</label>
					<select id="search-bizId">
						<option value="">选择类型</option>
						<option value="8600">圆形区域</option>
						<option value="8602">矩形区域</option>
						<option value="8604">多边形区域</option>
						<option value="8606">路线</option>
					</select>
					<label>设备名称：</label><%@ include file="../pub/deviceSearchSelectPicker.jsp"%>
					&nbsp;&nbsp;
					<button type="button" class="btn btn-primary btn-sm" onclick="doQuery()">
						<span class="glyphicon glyphicon-search"></span>查询
					</button>
				</p>
			</div>
			<div class="table_div">
				<table id="boot-strap-table" class="table_style" border="0"></table>
			</div>
		</div>
	</div>
	<div id="map-dlg" class="simple-dialog" title="查看围栏" style="width:550px;height:450px;">
    	<div id="map_box" style="width:100%;height:100%;"></div>
	</div>
	<%@ include file="../pub/mapScript.jsp"%>
	<script type="text/javascript" src="<c:url value='/js/device/fenceSettingList.js'/>"></script>
</body>
</html>
