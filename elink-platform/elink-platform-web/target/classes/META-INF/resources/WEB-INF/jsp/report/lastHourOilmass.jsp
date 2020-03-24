<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="charts_date.jsp"%>
<script language="javaScript">
$(document).ready(function() {
	setSearchHourOption();
	$('#search-date').val(new Date().format("yyyy-MM-dd"));
});
function afterLoadSelectPicker(){
	doQuery();
}
function doQuery() {
	lastHourOilmass();
}
</script>
</body>
</html>