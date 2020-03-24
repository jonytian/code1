$(function(){
	loadRuleSetting();
	
	$(":radio[name^='type-']").click(function(){
		var name = $(this).attr("name");
		if(parseInt($(this).val())==0){
			$("input[type=radio][name="+name+"][value=0]").attr("checked",'checked');
			$("#"+name+"-box").show();
		}else{
			$("#"+name+"-box").hide();
		}
	});

	$("#type-4-all").click(function(){
		var checked = $(this).is(":checked");
		$(":checkbox[id^='type-4-']").each(function(){
			$(this).prop("checked",checked);
		});
	});
});

function getApiName(){
	return "dictionary";
}

function loadRuleSetting(){
	var url = management_api_server_servlet_path+"/common/query/"+getApiName()+".json?countable=false&pageNo=1&pageSize=1";
	var params = {};
	params["type.eq"] = dictionary_officers_car_notify;
	ajaxAsyncPost(url, params, function(result){
		if (result.code != 0) {
			showErrorMessage(result.message);
		} else {
			var list = result.data;
			if(list && list.length>0){
				var item = list[0];
				$("#edit-frm-id").val(item.id);
				var rule = $.evalJSON(item.content);
				if(rule && rule.length>0){
					for(var i=0;i<rule.length;i++){
						var item = rule[i];
						if(item.type < 4){
							if(item.flag==0){
								$("input[type=radio][name=type-"+item.type+"][value="+item.rule+"]").attr("checked",'checked');
							}else{
								$("input[type=radio][name=type-"+item.type+"][value=0]").attr("checked",'checked');
								$("#type-"+item.type+"-box").show();
								$("#type-"+item.type+"-val").val(item.rule);
							}
						}else if(item.type == 4){
							var list = item.rule;
							var html = "";
							for(var i=0;i<list.length;i++){
								var val = list[i];
								var index = (i+1);
								html +="<tr id='type-4-"+index+"-tr'><td><input style='height:16px;margin-left:0px;' id='type-4-"+index+"' type='checkbox' checked='checked'></td>" +
										"<td></td><td><input style='height:25px;' id='type-4-"+index+"-val' class='number' value='"+val+"' type='text'></td><td>公里</td><td>" +
										"<p onclick='addMore()' class='glyphicon glyphicon-plus'></p><p onclick='del("+index+")' class='glyphicon glyphicon-minus'></td></tr>";
							}
							if(html !=""){
								$("#type-4-tbody").html(html);
							}
						}else{
							$("#type-"+item.type).val(item.rule);
						}
					}
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
	data.type = dictionary_officers_car_notify;
	
	var rule = [];
	//驾照到期提示
	for(var type=1;type<4;type++){
		var val = form["type-"+type];
		var flag = 0;
		if(val && val=="0"){
			val = form["type-"+type+"-val"];
			flag = 1;//其他值
		}
		rule.push({"type":type,"flag":flag,"rule":parseInt(val)});
	}

	//报废提醒
	rule.push({"type":6,"flag":0,"rule":parseInt(form["type-6"])});
	
	var list = [];
	$(":checkbox[id^='type-4-']").each(function(i){
		if($(this).is(":checked")){
			var id = $(this).attr("id");
			var val = parseInt($("#"+id+"-val").val());
			if(val > 0){
				list.push(val);
			}
		}
	});
	
	if(list.length>0){
		rule.push({"type":4,"rule":list});
	}
	
	data.code = getUserEnterpriseId();
	data.content = $.toJSON(rule);
	var url = getSaveUrl(data);
	if (data.id) {
		ajaxAsyncPatch(url, data, function(result){
			if (result.code != 0) {
				showErrorMessage(result.message);
			} else {
				showMessage("保存成功");
				loadRuleSetting();
			}
		});
	} else {
		ajaxAsyncPost(url, data, function(result){
			if (result.code != 0) {
				showErrorMessage(result.message);
			} else {
				showMessage("保存成功");
				loadRuleSetting();
			}
		});
	}
}

function addMore(){
	var max = 0;
	$(":checkbox[id^='type-4-']").each(function(i){
		if($(this).is(":checked")){
			var arr = $(this).attr("id").split("-");
			if(parseInt(arr[2])>max){
				max = arr[2];
			}
		}
	});
	max++;
	$("#type-4-tbody").append("<tr id='type-4-"+max+"-tr'><td><input style='height:16px;margin-left:0px;' id='type-4-"+max+"' type='checkbox' checked='checked'></td>" +
	"<td></td><td><input style='height:25px;' id='type-4-"+max+"-val' class='number' value='' type='text'></td><td>公里</td><td>" +
	"<p onclick='addMore()' class='glyphicon glyphicon-plus'></p><p onclick='del("+max+")' class='glyphicon glyphicon-minus'></td></tr>");
}

function del(index){
	$("#type-4-"+index+"-tr").remove();
}