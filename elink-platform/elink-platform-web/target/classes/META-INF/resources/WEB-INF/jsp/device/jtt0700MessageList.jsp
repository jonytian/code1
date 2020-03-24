<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/header.jsp"%>
<title>行驶记录仪数据</title>
<%@ include file="/common/common.jsp"%>
</head>
<body>
	<div class="clear"></div>
	<!--内容部分-->
	<div class="con1 left" id="content-div">
		<div class="right  con_table">
			<div class="table_find">
				<p>
					<label>设备名称：</label><%@ include file="../pub/deviceSearchSelectPicker.jsp"%> &nbsp;&nbsp;
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
	<%@ include file="messageDetail.jsp"%>
	<script type="text/javascript" src="<c:url value='/js/device/jtt0700MessageList.js'/>"></script>
</body>
</html>