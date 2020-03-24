<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/header.jsp"%>
<title>流量异常告警</title>
</head>
<body>
	<div class="clear"></div>
	<!--内容部分-->
	<div class="con1 left" id="content-div">
		<div class="right  con_table">
			<div class="table_div">
				<table id="boot-strap-table" class="table_style" border="0"></table>
			</div>
		</div>
	</div>
<%@ include file="/common/common.jsp"%>
	<script type="text/javascript" src="<c:url value='/js/device/dataLimitAlarmList.js'/>"></script>
</body>
</html>