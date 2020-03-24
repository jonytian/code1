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
				<label id="search-date-div">时间：</label><input id="search-date" type="text" readonly class="date form_month" data-date-format="yyyy-mm" >
				<label>车牌号：</label><%@ include file="../pub/carSearchSelectPicker.jsp"%>
				&nbsp;&nbsp;
				<button class="btn btn-primary btn-sm" onclick="onClickSearch()">
					<span class="glyphicon glyphicon-search"></span>查询
				</button>
			</p>
		</div>
		<div class="charts_container" >
			<div id="charts_div" style="top:10%;width:55%;height:80%;float:left;"></div>
			<div id="charts_div1" style="top:10%;width:45%; height:80%;float:right;"></div>
		</div>
</div>
<script type="text/javascript" src="<c:url value='/js/echarts/echarts.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/report/charts.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/report/report_charts.js'/>"></script>
<script type="text/javascript">
	$(function() {
		initMonthpicker();
		$('#search-date').val(new Date().format("yyyy-MM"));
	});
	function afterLoadSelectPicker(){
		doQuery();
	}
	function doQuery() {
		monthMileageOverview();
	}
</script>
</body>
</html>