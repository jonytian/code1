var brandTypeMap = new JsMap();
$(function(){
	loadDictionary(dictionary_officers_car_brandType,function(list){
    	setSelectOption("search-brand",list);
    	setSelectOption("edit-frm-remark",list);
    	
    	for(var i=0;i<list.length;i++){
			var item = list[i];
			brandTypeMap.put(parseInt(item.code),item.content);
		}
	});
	
	initBootstrapTable();
});

function getApiName(){
	return "dictionary";
}

function getQueryParams(){
	var params = {};
	params["type.eq"]= dictionary_officers_car_brandModel;
	
	var brand = $("#search-brand").val();
	if(brand!=""){
		params["remark.eq"]= brand;
	}
	
	return params;
}

function getQueryUrl(){
	return management_api_server_servlet_path+"/common/query/"+getApiName()+".json?countable=true";
}

function getColumns(){
	return [{field: 'check',checkbox :true},
		 {field:"remark",title:"品牌名称",formatter: function (value, row) {
			 return brandTypeMap.get(parseInt(row.remark));
		 }},
		 {field:"content",title:"型号名称"},
		 {field:"code",title:"型号代码"},
		 {field:"createTime",title:"创建时间"},
		 {title: '操作',field: 'opear',formatter: function (value, row) {
             var html = '<a   href="javascript:void(0)" class="table_edit" title="编辑" onclick="edit(\'' + row.id + '\')">编辑</a>';
             html += '<a   href="javascript:void(0)" class="table_del" title="删除" onclick="del(\'' + row.id + '\')">删除</a>';
          return html ;
   }}];
}

function setAddInfo(){
	$("#edit-frm-type").val(dictionary_officers_car_brandModel);
	$("#edit-frm-brand option:first").prop("selected", 'selected'); 
}