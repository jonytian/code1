<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="charts_date.jsp"%>

<script language="javaScript">
	$(document).ready(function() {
		$("#search-date-div").hide();
		$("#search-hour").hide();
		$("#search-date").hide();
	});
	function afterLoadSelectPicker(){
		doQuery();
	}
	function doQuery() {
		todayAlarmOverview();
	}
</script>
</body>
</html>