<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/common/header.jsp"%>
<%@ include file="/common/common.jsp"%>
<link rel="stylesheet" href="<c:url value='/css/timeline.css?v=8'/>">
<script type="text/javascript" src="<c:url value='/js/common/timeline.js'/>"></script>
<title>驾驶行程分析</title>
</head>
<body>
	<!--内容部分-->
	<div class="con1 left" id="content-div">
		<div class="right  con_table">
			<div class="table_find">
				<p>
					<label id="search-date-div">时间：</label><input id="search-date" type="text" readonly class="date form_date" data-date-format="yyyy-mm-dd">
					<label>车牌号：</label><%@ include file="../pub/carSearchSelectPicker.jsp"%>
					&nbsp;&nbsp;
					<button class="btn btn-primary btn-sm" onclick="onClickSearch()">
						<span class="glyphicon glyphicon-search"></span>查询
					</button>
				</p>
			</div>
			<div class="table_div">
				<div class="timeline">
					<div class="timeline-date">
						<ul id="timeline-list"></ul>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="map-dlg" style="width: 100px; height: 100px; display: none;">
		<div id="map" style="width: 99%;"></div>
	</div>
	<script type="text/javascript" src="<c:url value='/js/map/gaodeMap.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/map/mapUtil.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/map/lngLatConverter.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/report/dailyAccStateTimeline.js'/>"></script>
</body>
</html>