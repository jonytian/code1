<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/header.jsp"%>
<%@ include file="/common/common.jsp"%>
<title>设备上行消息</title>
</head>
<body>
	<div class="clear"></div>
	<!--内容部分-->
	<div class="con1 left" id="content-div">
		<div class="right  con_table">
			<div class="table_find">
				<p>
					<%--modify by tyj20200316--%>
					<label>消息ID：</label><input
						id="search-messageId" maxlength="6" type="text" placeholder="请输入消息ID" />
						<label>车牌号：</label><%@ include file="../pub/deviceSearchSelectPicker.jsp"%> &nbsp;&nbsp;
					<button type="button" class="btn btn-primary btn-sm" onclick="onClickSearch()">
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
	<script type="text/javascript" src="<c:url value='/js/device/upMessageList.js'/>"></script>
</body>
</html>