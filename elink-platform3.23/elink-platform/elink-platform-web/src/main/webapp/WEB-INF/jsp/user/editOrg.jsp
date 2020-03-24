<%@ page language="java" contentType="text/html; charset=utf-8" 	pageEncoding="utf-8"%>
<div id="edit-dlg" title="组织机构" class="simple-dialog" style="width:500px; height:200px;">
	<form id="edit-frm" method="post">
		<input name="id" id="edit-frm-id" type="hidden">
		<div class="message_con">
			<label>组织名称：</label><input name="name" id="edit-frm-name" type="text" maxlength="16"  class="required">
		</div>
		<div class="message_con">
			<label>组织代码：</label><input name="code" id="edit-frm-code" type="text" maxlength="16"  class="required">
		</div>
	   <div class="message_footer">
			<button type="button" class="bule" onclick="save()">保存</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
	  </div>
	</form>
</div>