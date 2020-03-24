<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="charts_date.jsp"%>

<script language="javaScript">
	$(function() {
		$("#search-hour").hide();
		var day = "${param.day}";
		if (!day) {
			day = 1;
		}
		var yesterday = new Date(new Date().getTime() - day * 24 * 60 * 60 * 1000);
		$('#search-date').val(yesterday.format("yyyy-MM-dd"));
	});
	function afterLoadSelectPicker(){
		doQuery();
	}
	function doQuery() {
		dailyAlarmOverview();
	}
</script>
</body>
</html>