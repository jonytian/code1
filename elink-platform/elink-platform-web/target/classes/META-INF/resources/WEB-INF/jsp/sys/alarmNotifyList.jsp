
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/header.jsp"%>
<title>系统提醒消息管理</title>
</head>
<body>
	<div class="clear"></div>
	<!--内容部分-->
	<div class="con1 left" id="content-div">
		<div class="right  con_table">
			<div class="table_find">
				<p>
					<label>提醒类型：</label>
					<select id="search-type">
					</select>
					<label>状态：</label>
					<select id="search-state">
						<option value="">全部</option>
						<option value="0">未读</option>
						<option value="1">已读</option>
					</select>&nbsp;&nbsp;
					<button type="button" class="btn btn-primary btn-sm" onclick="doQuery()">
						<span class="glyphicon glyphicon-search"></span>查询
					</button>
				</p>
			</div>
			<div class="table_div">
				<table id="boot-strap-table" class="table_style" border="0"></table>
			</div>
			<p id="toolbar" class="table_but">
				<a href="javascript:void(0)" onclick="read()"><i class="glyphicon glyphicon-ok-circle"></i>设置已读</a>
			</p>
		</div>
	</div>
	<%@ include file="/common/common.jsp"%>
	<script type="text/javascript" src="<c:url value='/js/sys/alarmNotifyList.js'/>"></script>
	<script language="javaScript">
		$(document).ready(function() {
			var type = "${param.type}";
			var keys = alarmNotifyType.getKeys();
			$("#search-type").append("<option value=''>全部 </option>");
			for (var i = 0, l = keys.length; i < l; i++) {
				var key = keys[i];
				var val = alarmNotifyType.get(key);
				$("#search-type").append("<option value='" + key + "'>" + val + "</option>");
			}
			$("#search-type").val(type);
			initBootstrapTable();
		});
	</script>
</body>
</html>