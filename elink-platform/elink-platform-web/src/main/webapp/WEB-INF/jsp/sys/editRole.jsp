<%@ page language="java" contentType="text/html; charset=utf-8" 	pageEncoding="utf-8"%>
<div id="edit-dlg" title="菜单角色信息" class="simple-dialog" style="width:550px; height:350px;">
	<form id="edit-frm" method="post">
		<input name="id" id="edit-frm-id" type="hidden">
		<div class="message_con">
			<label>角色名称：</label><input name="name" id="edit-frm-name" maxlength="16"  type="text" class="required"><span class="must">*</span>
		</div>
		<div class="message_con">
			<label>角色代码：</label><input name="code" id="edit-frm-code" maxlength="8"  type="text" class="required"><span class="must">*</span>
		</div>
		<div class="message_con">
			<label  style="vertical-align:top;">备        注：</label><textarea  name="remark" id="edit-frm-remark" maxlength="100" style="height:150px;"/></textarea>
		</div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="save()">保存</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>