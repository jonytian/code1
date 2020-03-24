<%@ page language="java" contentType="text/html; charset=utf-8" 	pageEncoding="utf-8"%>
<div id="message-detail-dlg"  class="simple-dialog" title="指令详情" style="width: 580px; height: 400px;">
	<form id="message-detail-dlg-frm" method="post">
		<div  class="message_con">
			<label>设备名称：</label><input name="deviceId" id="message-detail-dlg-frm-deviceId"  type="text">
		</div>
		<div  class="message_con">
			<label>消息ID：</label><input name="messageId" id="message-detail-dlg-frm-messageId"  type="text">
		</div>
		<div  class="message_con">
			<label  style="vertical-align:top;">消息体：</label>
			<textarea  name="messageBody" id="message-detail-dlg-frm-messageBody" style="height:200px"></textarea>
		</div>
	</form>
</div>