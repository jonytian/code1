<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/header.jsp"%>
<title>网关黑名单管理</title>
</head>
<body>
	<div class="clear"></div>
	<!--内容部分-->
	<div class="con1 left" id="content-div">
		<div class="right  con_table">
			<div class="table_div">
				<table id="boot-strap-table" class="table_style" border="0"></table>
			</div>
			<p id="toolbar" class="table_but">
				<a href="javascript:void(0)" onclick="dels()"><i class="glyphicon glyphicon-remove"></i> 解除</a>
			</p>
		</div>
	</div>
	<%@ include file="/common/common.jsp"%>
	<script type="text/javascript" src="<c:url value='/js/sys/gatewayBlacklistList.js'/>"></script>
</body>
</html>