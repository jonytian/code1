
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/header.jsp"%>
<link rel="stylesheet" href="<c:url value='/css/tail.css'/>">
<title>菜单模板管理</title>
</head>
<body>
<section class="sm_section left">
<form id="edit-frm" method="post">
	<input name="id" id="edit-frm-id" type="hidden">
	<fieldset class="message_fieldset">
            <legend>默认企业菜单角色模板</legend>
            <div class="message_con"><p style="padding-left:0;" id="enterprise-role-box"></p></div>
	</fieldset>

	<fieldset class="message_fieldset">
            <legend>默认用户菜单角色模板</legend>
            <div class="message_con"><p style="padding-left:0;" id="user-role-box"></p></div>
	</fieldset>

	<div class="message_footer">
			<button type="button" class="bule" onclick="save()">保存</button>
	</div>
</form>
</section>
<%@ include file="/common/common.jsp"%>
<script type="text/javascript" src="<c:url value='/js/dictionary/userRoleTemplate.js'/>"></script>
</body>
</html>