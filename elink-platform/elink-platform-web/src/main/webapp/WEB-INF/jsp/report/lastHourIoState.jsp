<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/header.jsp"%>
<%@ include file="/common/common.jsp"%>
<title>车辆io状态分析分析</title>
</head>
<body>
	<!--内容部分-->
	<div class="con1 left" id="content-div">
		<div class="right  con_table">
			<div class="table_find">
				<p>
					<label>日期：</label><input id="search-date" type="text" readonly class="date form_date" data-date-format="yyyy-mm-dd"><!-- modified by yaojiang.tian at 20200305 -->
					<select id="search-hour"></select>
					<label>车牌号：</label><%@ include file="../pub/carSearchSelectPicker.jsp"%>
					&nbsp;&nbsp;
					<button class="btn btn-primary btn-sm" onclick="onClickSearch()">
						<span class="glyphicon glyphicon-search"></span>查询
					</button>
				</p>
			</div>
			<div class="table_div">
				<div style="width: 100%; padding: 5px;">
					<div id="charts_div1" style="width: 50%; height: 250px; float: left;"></div>
					<div id="charts_div2" style="width: 50%; height: 250px; float: left;"></div>
				</div>
				<div style="width: 100%; padding: 5px;">
					<div id="charts_div3" style="width: 50%; height: 250px; float: left;"></div>
					<div id="charts_div4" style="width: 50%; height: 250px; float: left;"></div>
				</div>

				<div style="width: 100%; padding: 5px;">
					<div id="charts_div5" style="width: 50%; height: 250px; float: left;"></div>
					<div id="charts_div6" style="width: 50%; height: 250px; float: left;"></div>
				</div>
				<div style="width: 100%; padding: 5px;">
					<div id="charts_div7" style="width: 50%; height: 250px; float: left;"></div>
					<div id="charts_div8" style="width: 50%; height: 250px; float: left;"></div>
				</div>

				<div style="width: 100%; padding: 5px;">
					<div id="charts_div9" style="width: 50%; height: 250px; float: left;"></div>
					<div id="charts_div10" style="width: 50%; height: 250px; float: left;"></div>
				</div>
				<div style="width: 100%; padding: 5px;">
					<div id="charts_div11" style="width: 50%; height: 250px; float: left;"></div>
					<div id="charts_div12" style="width: 50%; height: 250px; float: left;"></div>
				</div>

				<div style="width: 100%; padding: 5px;">
					<div id="charts_div13" style="width: 50%; height: 250px; float: left;"></div>
					<div id="charts_div14" style="width: 50%; height: 250px; float: left;"></div>
				</div>

				<div style="width: 100%; padding: 5px;">
					<div id="charts_div15" style="width: 50%; height: 250px; float: left;"></div>
				</div>

			</div>
		</div>
	</div>
	<script type="text/javascript" src="<c:url value='/js/echarts/echarts.min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/report/charts.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/report/report_charts.js'/>"></script>
	<script language="javaScript">
		$(document).ready(function() {
			setSearchHourOption();
			initDatepicker();
			$('#search-date').val(new Date().format("yyyy-MM-dd"));
		});
		function afterLoadSelectPicker(){
			doQuery();
		}
		function doQuery() {
			lastHourIoState();
		}
	</script>
</body>
</html>