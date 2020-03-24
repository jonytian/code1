<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/header.jsp"%>
<%@ include file="/common/common.jsp"%>
<title>统计分析</title>
</head>
<body>
<!--内容部分-->
<div class="con1" id="content-div">
		<div class="table_find" style="height:55px;">
			<p>
				<label id="search-date-div">日期：</label><input id="search-date" type="text" readonly class="date form_date" data-date-format="yyyy-mm-dd"> <select id="search-hour" ></select><!-- modified by yaojiang.tian at 20200305 -->
				<label>车牌号：</label><%@ include file="../pub/carSearchSelectPicker.jsp"%>
				&nbsp;&nbsp;
				<button class="btn btn-primary btn-sm" onclick="onClickSearch()">
					<span class="glyphicon glyphicon-search"></span>查询
				</button>
			</p>
		</div>
		<div class="charts_container" >
			<div class="charts"  id="charts_div" style="width:75%; height:85%;"></div>
		</div>
</div>
<script type="text/javascript" src="<c:url value='/js/echarts/echarts.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/report/charts.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/report/report_charts.js?v=1'/>"></script>
<script type="text/javascript">
$(function() {
	initDatepicker();
});
</script>
