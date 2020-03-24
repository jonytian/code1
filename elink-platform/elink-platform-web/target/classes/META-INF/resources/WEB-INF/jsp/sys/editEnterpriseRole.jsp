<%@ page language="java" contentType="text/html; charset=utf-8" 	pageEncoding="utf-8"%>
<div id="edit-dlg" title="企业角色信息" class="simple-dialog" style="width:500px;height:200px;">
	<form id="edit-frm" method="post">
		<input name="id" id="edit-frm-id" type="hidden">
		<div class="message_con">
				<label>企业名称：</label><select name="enterpriseId" id="edit-frm-enterpriseId"></select>
		</div>
		<div class="message_con">
				<label>角色名称：</label><select name="roleId" id="edit-frm-roleId"></select>
		</div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="save()">保存</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>