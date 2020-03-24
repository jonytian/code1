1`<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="charts_month.jsp"%>
<script language="javaScript">
function afterLoadSelectPicker(){
	doQuery();
}
function doQuery() {
	monthAvgOilmass();
}
</script>
</body>
</html>