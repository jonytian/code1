function init() {
	initDatepicker();
	
	loadDictionary(dictionary_officers_car_color,function(list){
		setSelectOption("edit-frm-color",list);
	});
	
	loadDictionary(dictionary_officers_car_brandType,function(list){
    	setSelectOption("edit-frm-brandType",list);
    	if(list && list.length>0){
        	var brandType = list[0].code;
        	setBrandModel(brandType);
    	}
	});
	
	loadDictionary(dictionary_officers_car_type,function(list){
	    setSelectOption("edit-frm-type",list);
	});
	
	loadDictionary(dictionary_officers_car_bizType,function(list){
		setSelectOption("edit-frm-bizType",list);
	});
	
	loadDictionary(dictionary_officers_car_level,function(list){
		setSelectOption("edit-frm-level",list);
	});
	
	var id = $("#edit-frm-id").val();
	if(id){
		loadEditFrmData(id);
 	}

	$("#edit-frm-color").change(function(){  
		var color=$("#edit-frm-color").val();
  		var brandType= $("#edit-frm-brandType").val();
  		var brandModel= $("#edit-frm-brandModel").val();
  		if(color && brandType && brandModel){
  			loadCarIcon(brandType,brandModel,color);
  		}
	});

	$("#edit-frm-brandType").change(function(){  
		var brandType= $("#edit-frm-brandType").val();
  		setBrandModel(brandType);
	});
	
	$("#edit-frm-brandModel").change(function(){  
		var color=$("#edit-frm-color").val();
  		var brandType= $("#edit-frm-brandType").val();
  		var brandModel= $("#edit-frm-brandModel").val();
  		if(color && brandType && brandModel){
  			loadCarIcon(brandType,brandModel,color);
  		}
	});
	
	var data = {"url": management_api_server_servlet_path+"/image.json?bizType=3"}
	initUploadImg("upload-input-licenseImgId",data,"edit-frm-licenseImgId");
}


function setBrandModel(brandType){
	$("#edit-frm-brandModel").empty();
	$("#edit-frm-brandModel").append("<option value=''>请选择车辆型号</option>");
	var params = {};
	params["type.eq"]= dictionary_officers_car_brandModel;
	params["remark.eq"]= ""+brandType;
	var url = management_api_server_servlet_path + "/system/dictionary/dictionary.json?isParent=false&countable=false&pageNo=1&pageSize=100";
	var result = ajaxSyncPost(url, params);
	if (result.code != 0) {
		showErrorMessage(result.message);
	} else {
		var list = result.data;
		for (var i = 0, l = list.length; i < l; i++) {
			var item = list[i];
			$("#edit-frm-brandModel").append("<option value='" + item.code + "'>" + item.content + "</option>");
		}
	}
	$("#edit-frm-brandModel option:first").prop("selected", 'selected'); 
}

function loadCarIcon(brandType,brandModel,color){
	var url = officerCar_api_server_servlet_path +"/system/dictionary/carIcon.json?isParent=false&countable=false&select=id";
	ajaxAsyncPost(url, {"brandType.eq":brandType,"brandModel.eq":brandModel,"carColor.eq":color},function(result){
		if (result.code != 0) {
			showErrorMessage(result.message);
		} else{
			var list = result.data;
			if(list && list.length>0){
				var id = list[0];
				$("#edit-frm-iconImg").attr("src", getContextPath()+"/attachment/download.do?url="+officerCar_api_server_servlet_path+"/car/icon/"+id+".json");
				$("#edit-frm-iconId").val(id);
			}else{
				$("#edit-frm-iconImg").attr("src", "../img/officers_car.png");
				$("#edit-frm-iconId").val("-1");
			}
		}
	});
}

function loadEditFrmDataSuccessHandler(data){
	if(data.iconId && data.iconId!="-1"){
		$('#edit-frm-iconImg').attr("src", getContextPath()+"/attachment/download.do?url="+officerCar_api_server_servlet_path+"/car/icon/"+data.iconId+".json");
	}
	if(data.licenseImgId){
		$("#edit-frm-licenseImgId-box").html("<img src='"+getContextPath()+"/attachment/download.do?url="+officerCar_api_server_servlet_path+"/image/"+data.licenseImgId+".json'/>");
	}

	if(data.subBizType){
		var bizType = data.bizType;
		var typeStr = subBizTypeMap.get(bizType+"");
		var arr = typeStr.split(";");
  		$("#edit-frm-subBizType").empty();
  		for (var i = 0, l = arr.length; i < l; i++) {
  			var item = arr[i].split(",");
  			$("#edit-frm-subBizType").append("<option value='" + item[0] + "'>" + item[1] + "</option>");
  		}
  		$("#edit-frm-subBizType").val(data.subBizType);
	}
	
	if(data.brandType){
		setBrandModel(data.brandType);
		$("#edit-frm-brandModel").val(data.brandModel);
	}
}

function getApiName() {
	return "car";
}

function saveSuccessHandler(){
	goBack();
}

function goBack(){
	window.parent.frames["content-frame"].location.href="groupCarFrame.do?isCacheQuery=true";
}