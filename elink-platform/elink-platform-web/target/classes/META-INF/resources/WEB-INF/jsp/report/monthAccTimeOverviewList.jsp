<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/header.jsp"%>
<%@ include file="/common/common.jsp"%>
<title>月度行驶时长统计</title>
</head>
<body>
	<!--内容部分-->
	<div class="con1 left" id="content-div">
		<div class="right  con_table">
			<div class="table_find">
				<p>
					<label>日期：</label><input id="search-date" type="text" readonly class="date form_month" data-date-format="yyyy-mm" ><!-- modified by yaojiang.tian at 20200305 -->
					<label>车牌号：</label><%@ include
						file="../pub/carSearchSelectPicker.jsp"%>
					&nbsp;&nbsp;
					<button class="btn btn-primary btn-sm" onclick="onClickSearch()">
						<span class="glyphicon glyphicon-search"></span>查询
					</button>
				</p>
			</div>
			<div class="table_div">
				<table id="boot-strap-table" class="table_style" border="0"></table>
			</div>
			<p id="toolbar" class="table_but">
				<a href="javascript:void(0)" onclick="exportExcel()"><i
					class="glyphicon glyphicon-open"></i>导出</a>
			</p>
		</div>
	</div>
	<script type="text/javascript" src="<c:url value='/js/report/monthAccTimeOverviewList.js'/>"></script>
</body>
</html>