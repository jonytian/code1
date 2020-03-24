<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/header.jsp"%>
<title>设备未知消息</title>
<%@ include file="/common/common.jsp"%>
</head>
<body>
	<div class="clear"></div>
	<!--内容部分-->
	<div class="con1 left" id="content-div">
		<div class="right  con_table">
			<div class="table_find">
				<p>
					<label>消息ID：</label><input id="search-messageId" maxlength="6" type="text" placeholder="请输入消息ID" />
					<label>时间：</label><input id="search-date" type="text" readonly class="date form_date" data-date-format="yyyy-mm-dd">
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
	<script type="text/javascript" src="<c:url value='/js/device/jttUnknownMessageList.js'/>"></script>
</body>
</html>