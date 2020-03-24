<%@ page language="java" contentType="text/html; charset=utf-8" 	pageEncoding="utf-8"%>
<div id="edit-dlg" class="simple-dialog" title="分组信息" style="width:550px;height:350px;">
	<form id="edit-frm" method="post">
		<input name="id" id="edit-frm-id" type="hidden">
		<input name="enterpriseId" id="edit-frm-enterpriseId" type="hidden">
		<input name="parentId" id="edit-frm-parentId" type="hidden">
		<input name="type" id="edit-frm-type" type="hidden" value="1">
		<div class="message_con">
			<label>父级分组：</label><input id="edit-frm-parentName"  type="text"  readonly>
		</div>
		<div class="message_con">
			<label>名称：</label><input name="name" id="edit-frm-name" maxlength="16"  type="text"  class="required">
		</div>
		<div class="message_con">
			<label  style="vertical-align:top;">备注：</label><textarea name="remark" id="edit-frm-remark" style="height:150px"></textarea>
		</div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="save()">保存</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>