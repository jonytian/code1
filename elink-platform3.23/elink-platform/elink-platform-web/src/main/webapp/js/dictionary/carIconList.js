var carColorMap = new JsMap();
var brandTypeMap = new JsMap();
var brandModelMap = new JsMap();

$(function() {
	loadDictionary(dictionary_officers_car_color,function(list){
		setSelectOption("edit-frm-carColor",list);
		setSelectOption("search-carColor",list,true);
		for (var i = 0, l = list.length; i < l; i++) {
			var item = list[i];
			carColorMap.put(item.code,item.content);
		}
	});
	
	loadDictionary(dictionary_officers_car_brandType,function(list){
	    setSelectOption("edit-frm-brandType",list);
	    setSelectOption("search-brandType",list);
	    for (var i = 0, l = list.length; i < l; i++) {
			var item = list[i];
			brandTypeMap.put(parseInt(item.code),item.content);
		}
	});
	
    initBootstrapTable();
    
    $("#edit-frm-brandType").change(function(){  
  		var brandType=$(this).children('option:selected').val();
  		setBrandModel(brandType);
	});

});

function setBrandModel(brandType){
	var params = {};
	params["type.eq"]= dictionary_officers_car_brandModel;
	params["remark.eq"]= brandType;
	var url = management_api_server_servlet_path + "/common/query/dictionary.json";
	var result = ajaxSyncPost(url, params);
	if (result.code != 0) {
		showErrorMessage(result.message);
	} else {
		var list = result.data;
		setSelectOption("edit-frm-brandModel",list,false);
		for (var i = 0, l = list.length; i < l; i++) {
			var item = list[i];
			brandModelMap.put(item.code,item.content);
		}
	}
}

function getApiName() {
	return "carIcon";
}

function getQueryParams() {
	var params = {};
	var carColor = $("#search-carColor").val();
	if(carColor!=""){
		params["carColor.eq"] = carColor;
	}
	
	var brandType = $("#search-brandType").val();
	if (brandType) {
		params["brandType.eq"] = brandType;
		
		setBrandModel(brandType);
	}
	return params;
}

function getQueryUrl() {
	return officerCar_api_server_servlet_path + "/common/query/" + getApiName()
			+ ".json?countable=true";
}

function getColumns() {
	return [ {
		field : 'check',
		checkbox : true
	}, {
		field : "carColor",
		title : "车辆颜色",
		formatter : function(value, row, index) {
			return carColorMap.get(row.carColor+"");
		}
	}, {
		field : "brandType",
		title : "车辆品牌",
		formatter : function(value, row, index) {
			return brandTypeMap.get(parseInt(row.brandType));
		}
	}, {
		field : "brandModel",
		title : "车辆型号",
		formatter : function(value, row, index) {
			return brandModelMap.get(row.brandModel+"");
		}
	}  ,{
		title : "图标",
		field : "icon",
		formatter : function(value, row, index) {
			return "<div class='car-icon-box'><img src='"+getContextPath()+"/attachment/download.do?url="+officerCar_api_server_servlet_path+"/car/icon/"+row.id+".json'/></div>";
		}
	}, {
		title : '操作',
		field : 'opear',
		formatter : function(value, row) {
			var html = '<a   href="javascript:void(0)" class="table_del" title="删除" onclick="del(\''
				+ row.id + '\')">删除</a>';
			return html;
		}
	}];
}


function add(){
	initFileUploader();
} 

function initFileUploader() {
	showCommondDialog("edit-dlg");
	$('#file_upload').JSAjaxFileUploader({
		uploadUrl:getContextPath()+"/attachment/upload.do",
		autoSubmit:false,
		maxFileSize:524000,
		allowExt:"jpg|jpeg|png|gif",
		inputText:"请选择要上传的图片...",
		uploadTest:"上传",
		customUpload:true,
		fail:function(result){
			showMessage("上传失败");
		},progress:function(file){
			startLoading();
		},
		complete:function(file){
			endLoading();
		},
		success:function(result){
			if (result.code!=0) {
			   showErrorMessage(result.message);
			}else{
				doQuery();
				closeDialog();
			}
		},
		error:function(result){
			showMessage("上传失败");
		}
	});
}

function getFileUploaderFormData(){
	return {"url": officerCar_api_server_servlet_path+"/car/icon.json?brandModel="+$("#edit-frm-brandModel").val()+"&brandType="+$("#edit-frm-brandType").val()+"&carColor="+$("#edit-frm-carColor").val()};
}

function getFileUploaderButton(){
	return "<div class='message_footer'><button type='button' class='startJSuploadButton bule'>保存</button><button type='button' class='orgen' onclick='closeDialog()'>取消</button></div>";
}