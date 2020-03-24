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
					<select id="search-bizType">
						<option value="">选择业务类型</option>
						<option value="1">停车场</option>
						<option value="2">途径点</option>
						<option value="3">站点</option>
						<option value="4">区域查车</option>
						<option value="5">电子围栏</option>
						<option value="5">行驶路线</option>
					</select>&nbsp;&nbsp;
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
	<div id="map-dlg" class="simple-dialog" title="查看标注" style="width:550px;height:450px;">
    	<div id="map_box" style="width:100%;height:100%;"></div>
	</div>
	<%@ include file="/common/common.jsp"%>
	<%@ include file="../pub/mapScript.jsp"%>
	<script type="text/javascript" src="<c:url value='/js/device/labelPointSettingList.js'/>"></script>
</body>
</html>
