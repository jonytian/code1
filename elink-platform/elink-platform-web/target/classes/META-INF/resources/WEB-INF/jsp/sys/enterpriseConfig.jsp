<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/common/header.jsp"%>
<link rel="stylesheet" href="<c:url value='/css/tail.css'/>">
<title>企业权限配置信息</title>
</head>
<body>
	<form id="edit-frm" method="post">
		<input name="id" id="edit-frm-id" value="${param.id}" type="hidden">
		<section class="sm_section left">
			<fieldset class="message_fieldset">
				<legend>流量控制</legend>
				<div class="message_con">
					<label>接入设备：</label><input name="deviceLimit"
						id="edit-frm-deviceLimit" maxlength="8" type="text"
						class="required digits" placeholder="允许接入平台的车载设备的数量">
					<p class="tip-message">允许接入平台的车载设备的数量</p>
				</div>
				<div class="message_con">
					<label>频率限制：</label><input name="messageNumLimit"
						id="edit-frm-messageNumLimit" maxlength="32" type="text"
						class="required"
						placeholder="上行消息频率控制，多项用分号分隔，如10,150;60,720表示每设备每10分钟不超过150条，每小时不超过720条消息">
						<p class="tip-message">上行消息频率控制，多项用分号分隔，如10,150;60,720表示每设备每10分钟不超过150条，每小时不超过720条消息</p>
				</div>
				<div class="message_con">
					<label>流量限制：</label><input name="messageByteLimit"
						id="edit-frm-messageByteLimit" maxlength="32" type="text"
						class="required"
						placeholder="上行流量控制，多项用分号分隔，如10,15;60,60表示每设备每10分钟不超过10M，每小时不超过60M">
						<p class="tip-message">上行消息流量控制，多项用分号分隔，如10,15;60,60表示每设备每10分钟不超过10M，每小时不超过60M</p>
				</div>
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
		<div class="message_footer">
			<button type="button" class="bule" onclick="save()">保存</button>
			<button type="button" class="orgen" onclick="javascript:history.back(-1);">取消</button>
		</div>
	</form>
	<%@ include file="/common/common.jsp"%>
	<script type="text/javascript" src="<c:url value='/js/sys/enterpriseConfig.js'/>"></script>
</body>
</html>