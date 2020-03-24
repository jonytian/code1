$(document).ready(function() {
	loadEditFrmData($("#edit-frm-id").val());
});

function getApiName(){
	return "enterpriseConfig";
}

function loadEditFrmDataSuccessHandler(result) {
	var checkboxStyle = " style='margin-left:5px; margin-right:0px;' "
	var html="";
	var upMessageLimit=result.upMessageLimit;
	var upMessageLimitMap = new JsMap();
	if(upMessageLimit){
		var arr = upMessageLimit.split(",");
		for(var i=0;i<arr.length;i++){
			upMessageLimitMap.put(arr[i],"1");
		}
	}
	
	var arr = deviceMessageInfoMap.getKeys();
	for(var i=0;i<arr.length;i++){
		var messageId = arr[i];
		if(messageId.charAt(0)=="0" || messageId.charAt(0)=="1" ){
			var s = deviceMessageInfoMap.get(messageId);
			if(upMessageLimitMap.get(messageId)){
				html+="<input "+checkboxStyle+" name='upMessageLimit' value='"+messageId+"' type='checkbox' checked=true>"+s;
			}else{
				html+="<input "+checkboxStyle+" name='upMessageLimit' value='"+messageId+"' type='checkbox'>"+s;
			}
		}
	}
	$("#up_message_limit_div").html(html);
	

	html="";
	var downMessageLimit=result.downMessageLimit;
	var downMessageLimitMap = new JsMap();
	if(downMessageLimit){
		var arr = downMessageLimit.split(",");
		for(var i=0;i<arr.length;i++){
			downMessageLimitMap.put(arr[i],"1");
		}
	}

	var arr = deviceMessageInfoMap.getKeys();
	for(var i=0;i<arr.length;i++){
		var messageId = arr[i];
		if(messageId.charAt(0)=="8" || messageId.charAt(0)=="9"){
			var s = deviceMessageInfoMap.get(messageId);
			if(downMessageLimitMap.get(messageId)){
				html+="<input "+checkboxStyle+" name='downMessageLimit' value='"+messageId+"' type='checkbox' checked=true>"+s;
			}else{
				html+="<input "+checkboxStyle+" name='downMessageLimit' value='"+messageId+"' type='checkbox'>"+s;
			}
		}
	}
	$("#down_message_limit_div").html(html);
}

function getEditFormData(){
	var data = $("#edit-frm").serializeObject();
	
	var upMessageLimit = "";
	$("input[name='upMessageLimit']:checked").each(function(){
		upMessageLimit +="," +this.value;                        
    });
	
	if(upMessageLimit){
		data.upMessageLimit=upMessageLimit.substr(1);
	}else{
		data.upMessageLimit="";
	}
	
	var downMessageLimit = "";
	$("input[name='downMessageLimit']:checked").each(function(){
		downMessageLimit +="," +this.value;                        
    }); 
	
	if(downMessageLimit){
		data.downMessageLimit=downMessageLimit.substr(1);
	}else{
		data.downMessageLimit = "";
	}
	return data;
}

function saveSuccessHandler(){
	history.back(-1);
}