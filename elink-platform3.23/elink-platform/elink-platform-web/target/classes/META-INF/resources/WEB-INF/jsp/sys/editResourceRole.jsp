<%@ page language="java" contentType="text/html; charset=utf-8" 	pageEncoding="utf-8"%>
<div id="edit-dlg" class="simple-dialog" title="角色菜单"  style="width:750px; height:480px;">
	<form id="edit-frm" method="post">
		<div class="message_con">
			<label>角色名称：</label><select name="roleId" id="edit-frm-roleId"></select>
		</div>
		<ul id="category-tree" class="ztree left_select_tree"></ul>
		<div class="right_multiple_Select_Box">
			<%@ include file="../pub/multipleSelectBox.jsp"%>
		</div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="save()">保存</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>