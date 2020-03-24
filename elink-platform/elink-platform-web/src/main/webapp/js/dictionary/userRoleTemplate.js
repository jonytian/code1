$(function(){
	loadSystemRole();
	loadUserRoleRole();
	loadRoleSetting();
});

function getApiName(){
	return "dictionary";
}

function loadSystemRole(){
	var url = management_api_server_servlet_path+"/common/query/systemRole.json?countable=false&pageNo=1&pageSize=1000";
	var result = ajaxSyncPost(url, {});
	if (result.code != 0) {
		showErrorMessage(result.message);
	} else {
		var list = result.data;
		var html = "";
		if(list && list.length>0){
			var item = list[0];
			html +="<input type='radio' value='"+item.id+"' name='systemRole'><p>"+item.name+"</p>";
		}
		$("#enterprise-role-box").html(html);
	}
}

function loadUserRoleRole(){
	var url = management_api_server_servlet_path+"/common/query/userRole.json?countable=false&pageNo=1&pageSize=1000";
	var result = ajaxSyncPost(url, {});
	if (result.code != 0) {
		showErrorMessage(result.message);
	} else {
		var list = result.data;
		var html = "";
		if(list && list.length>0){
			for(var i=0;i<list.length;i++){
				var item = list[i];
				html +="<input type='checkbox' value='"+item.id+"' name='userRole'><p>"+item.name+"</p>";
			}
		}
		$("#user-role-box").html(html);
	}
}

function loadSystemRole(){
	var url = management_api_server_servlet_path+"/common/query/systemRole.json?countable=false&pageNo=1&pageSize=1000";
	var result = ajaxSyncPost(url, {});
	if (result.code != 0) {
		showErrorMessage(result.message);
	} else {
		var list = result.data;
		var html = "";
		if(list && list.length>0){
			for(var i=0;i<list.length;i++){
				var item = list[i];
				html +="<input type='radio' value='"+item.id+"' name='systemRole'><p>"+item.name+"</p>";
			}
		}
		$("#enterprise-role-box").html(html);
	}
}

function loadRoleSetting(){
	var url = management_api_server_servlet_path+"/common/query/"+getApiName()+".json?countable=false&pageNo=1&pageSize=1";
	var params = {};
	params["type.eq"] = dictionary_user_role;
	ajaxAsyncPost(url, params, function(result){
		if (result.code != 0) {
			showErrorMessage(result.message);
		} else {
			var list = result.data;
			if(list && list.length>0){
				var item = list[0];
				$("#edit-frm-id").val(item.id);
				var rule = $.evalJSON(item.content);
				$("input[type=radio][name=systemRole][value="+rule.systemRole+"]").attr("checked",true);
				var userRole = rule.userRole;
				for(var i=0;i<userRole.length;i++){
					$("input[type=checkbox][name=userRole][value="+userRole[i]+"]").prop("checked",true);;
				}
			}
		}
	});
}

function save(){
	var formId = "edit-frm";
	if (!validForm(formId)) {
		return;
	}
	var  form = $("#"+formId).serializeObject();
	var data = {};
	data.id = form.id;
	data.type = dictionary_user_role;
	data.code = dictionary_user_role;
	data.content = $.toJSON({"systemRole":form.systemRole,"userRole":form.userRole});
	
	var url = getSaveUrl(data);
	if (data.id) {
		ajaxAsyncPatch(url, data, function(result){
			if (result.code != 0) {
				showErrorMessage(result.message);
			} else {
				showMessage("保存成功");
				loadRoleSetting();
			}
		});
	} else {
		ajaxAsyncPost(url, data, function(result){
			if (result.code != 0) {
				showErrorMessage(result.message);
			} else {
				showMessage("保存成功");
				loadRoleSetting();
			}
		});
	}
}