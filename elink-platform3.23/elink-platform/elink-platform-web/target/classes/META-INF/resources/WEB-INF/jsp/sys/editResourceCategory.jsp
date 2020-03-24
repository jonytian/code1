<%@ page language="java" contentType="text/html; charset=utf-8" 	pageEncoding="utf-8"%>
<div id="edit-dlg" title="菜单分类信息" class="simple-dialog" style="width:550px; height:320px;">
	<form id="edit-frm" method="post">
		<input name="id" id="edit-frm-id" type="hidden">
		<input name="type" id="edit-frm-type" type="hidden" value="1">
		<div class="message_con">
			<label>父级菜单：</label><select name="parentId" id="edit-frm-parentId">
					  <option  value ="">请选择</option>
				</select>
		</div>
		<div class="message_con">
			<label>菜单名称：</label><input name="name" id="edit-frm-name" maxlength="64" type="text" class="required"><span class="must">*</span>
		</div>
		<div class="message_con">
			<label>菜单url：</label><input name="url" id="edit-frm-url" maxlength="128"  type="text">
		</div>
		<div class="message_con">
			<label>菜单icon：</label><input name="icon" id="edit-frm-icon" maxlength="64"  type="text">
		</div>
		<div class="message_con">
			<label>菜单排序：</label><input name="order" id="edit-frm-order" maxlength="2" type="text" class="required number"><span class="must">*</span>
		</div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="save()">保存</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>