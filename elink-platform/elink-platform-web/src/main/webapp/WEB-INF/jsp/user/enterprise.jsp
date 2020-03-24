<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/header.jsp"%>
<title>账号信息</title>
</head>
<body>
<div class="con left">
    <!--选择时间-->
    <div class="static_top left">
        <i></i><span>账号信息</span>
    </div>
    <!--数据总概-->
	<input name="enterpriseId" id="edit-frm-enterpriseId"
		value="${sessionScope.user.enterpriseId}" type="hidden">
	<div class="stiatic_top_con left">
		<table>
			<tr>
				<td class="labe_td">账号名称：</td>
				<td id="name"></td>
			</tr>
			<tr>
				<td class="labe_td">联&nbsp;&nbsp;系&nbsp;&nbsp;人：</td>
				<td  id="contact"></td>
			</tr>
			<tr>
				<td class="labe_td">联系电话：</td>
				<td  id="tel"></td>
			</tr>
			<tr>
				<td class="labe_td">&nbsp;&nbsp;&nbsp;Email：</td>
				<td  id="email"></td>
			</tr>
			<tr>
				<td class="labe_td">&nbsp;地&nbsp;&nbsp;&nbsp;&nbsp;址：</td>
				<td  id="address"></td>
			</tr>
			<tr>
				<td class="labe_td">账号简介：</td>
				<td  id="remark"></td>
			</tr>
		</table>
	</div>
	<%@ include file="/common/common.jsp"%>
</body>
</html>
<script language="javaScript">
	$(document).ready(
			function() {
				var enterpriseId = $("#edit-frm-enterpriseId").val();
				var url = management_api_server_servlet_path
						+ "/common/enterprise/" + enterpriseId + ".json";
				var data = {}
				ajaxAsyncGet(url, data, setEnterpriseInfo);
			});

	function setEnterpriseInfo(result) {
		if (result.code != 0) {
			showErrorMessage(result.message);
		} else {
			result = result.data;
			$("#name").html(result.name);
			$("#contact").html(result.contact);
			$("#tel").html(result.tel);
			$("#email").html(result.email);
			$("#address").html(result.address);
			$("#remark").html(result.remark);
		}
	}
</script>