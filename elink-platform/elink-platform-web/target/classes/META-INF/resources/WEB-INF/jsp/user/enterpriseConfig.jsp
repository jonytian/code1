<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/common/header.jsp"%>
<link rel="stylesheet" href="<c:url value='/css/tail.css'/>">
<title>账号权限配置信息</title>
</head>
<body>
	<input name="enterpriseId" id="edit-frm-enterpriseId"
		value="${sessionScope.user.enterpriseId}" type="hidden">
	<section class="sm_section left">
		<fieldset class="message_fieldset">
			<legend>流量控制</legend>
			<div class="message_con">
				<label>接入设备：</label>
				<p style="padding-left: 0;" id="device_limit"></p>
			</div>
			<div class="message_con">
				<label>鉴权消息：</label>
				<p style="padding-left: 0;">5次/10分钟（每个终端设备每10分钟不能超过5次）</p>
			</div>
			<div class="message_con">
				<label>注册消息：</label>
				<p style="padding-left: 0;">3次/10分钟（每个终端设备每10分钟不能超过3次）</p>
			</div>
			<div class="message_con">
				<label>流量限制：</label>
				<p style="padding-left: 0;">10M/1分钟（每个ip每分钟不能超过10M数据）</p>
			</div>
			<div id="message_limit_div"></div>
		</fieldset>
		<fieldset class="message_fieldset">
			<legend>上行消息</legend>
			<div class="message_con">
				<p style="padding-left: 0;" id="up_message_limit_div"></p>
			</div>
		</fieldset>

		<fieldset class="message_fieldset">
			<legend>下行指令</legend>
			<div class="message_con">
				<p style="padding-left: 0;" id="down_message_limit_div"></p>
			</div>
		</fieldset>
	</section>
	<%@ include file="/common/common.jsp"%>
	<script type="text/javascript" src="<c:url value='/js/user/enterpriseConfig.js'/>"></script>
</body>
</html>