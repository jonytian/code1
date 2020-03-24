<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<div id="edit-dlg" title="用户信息" class="simple-dialog"
	style="width:650px; height: 280px;">
	<form id="edit-frm" method="post">
		<input name="id" id="edit-frm-id" type="hidden"> 
		<input name="deptId" id="edit-frm-deptId" type="hidden"> 
		<input name="orgId" id="edit-frm-orgId" type="hidden">
		<input name="type" id="edit-frm-type" type="hidden">
		<input name="enterpriseId" id="edit-frm-enterpriseId" type="hidden">
		<div class="message_con">
			<label>部门名称：</label><input name="deptName" id="edit-frm-deptName" type="text" readonly>
		</div>
		<div class="message_con">
			<label>用&nbsp;户&nbsp;名：</label><input name="name" id="edit-frm-name" type="text" maxlength="16" class="required"><span class="must">*</span>
		</div>
		<div class="message_con">
			<label>登陆账号：</label><input name="account" id="edit-frm-account" type="text" maxlength="16" class="required userAccount"><span class="must">*</span>
		</div>
		<div class="message_con">
			<label>登录密码：</label><input name="password" id="edit-frm-password" type="password" maxlength="16" class="required"><span class="must">*</span>
		</div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="save()">保存</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>

<div id="edit-dlg-move-dept" title="转部门" class="simple-dialog" style="width:500px; height: 350px;">
	<div style="height:250px;margin-left:170px;"><ul id="dept-tree"  class="ztree"></ul></div>
	<div class="message_footer">
		<button type="button" class="bule" onclick="saveMoveGroup()">保存</button>
		<button type="button" class="orgen" onclick="closeDialog()">取消</button>
	</div>
</div>