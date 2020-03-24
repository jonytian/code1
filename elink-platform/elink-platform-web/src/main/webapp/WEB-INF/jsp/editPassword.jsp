<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<div id="user-info-dlg" title="修改账户密码" style="width:550px;height:180px;" class="simple-dialog">
		<form id="user-info-dlg-frm">
			<input name="id" id="user-info-dlg-frm-id" type="hidden" value="${sessionScope.user.userId}"> 
			<div class="message_con">
				<label>新&nbsp;&nbsp;密&nbsp;&nbsp;码：</label><input name="password" class="required" type="password" maxlength="16"><span class="must">*</span>
			</div>
			<div class="message_con">
				<label>确认密码：</label><input name="confirmPassword" class="required" type="password" maxlength="16"><span class="must">*</span>
			</div>
		</form>
		<div class="message_footer">
			<button type="button" class="bule" onclick="saveUserInfo();">保存</button>
			<button type="button" class="orgen" onclick="closeDialog();">取消</button>
		</div>
</div>
<script type="text/javascript">
function showUserInfo(){
	closeDialog();
	setTimeout(function(){ 
		showCommondDialog("user-info-dlg"); 
	}, 500);
}

function saveUserInfo(){
	var formId = "user-info-dlg-frm";
	if (!validForm(formId)) {
		return;
	}
	var data = $("#"+formId).serializeObject();
	if(data.password!=data.confirmPassword){
		showMessage("确认密码不一致！");
		return ;
	}
	var url = management_api_server_servlet_path + "/common/user/" + data.id+ ".json";
	ajaxAsyncPatch(url, data, function(result){
		if (result.code != 0) {
			showErrorMessage(result.message);
		} else {
			showMessage("修改成功！");
			closeDialog();
			top.location.href="logout.do";
		}
	});
}
</script>