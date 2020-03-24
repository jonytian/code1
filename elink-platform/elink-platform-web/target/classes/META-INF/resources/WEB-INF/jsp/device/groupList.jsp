<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/header.jsp"%>
<title>车辆分组管理</title>
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
				<a href="javascript:void(0)" onclick="add()"><i
					class="glyphicon glyphicon-plus"></i>新增</a><a
					href="javascript:void(0)" onclick="dels()"><i
					class="glyphicon glyphicon-remove"></i> 删除</a>
			</p>
		</div>
	</div>
	<%@ include file="/common/common.jsp"%>
	<%@ include file="editGroup.jsp"%>
	<input name="groupType" id="search-frm-groupType" value="${param.groupType}" type="hidden">
	<input name="groupId" id="search-frm-groupId" type="hidden">
	<input name="groupName" id="search-frm-groupName"   type="hidden">
	<input name="enterpriseId" id="search-frm-enterpriseId" type="hidden">
	<input name="enterpriseName" id="search-frm-enterpriseName" type="hidden">
	<script type="text/javascript" src="<c:url value='/js/device/groupList.js'/>"></script>
</body>
</html>