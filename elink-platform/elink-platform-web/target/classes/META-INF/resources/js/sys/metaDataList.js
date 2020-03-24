$(function(){
	initBootstrapTable();
});

function getApiName(){
	return "dictionary";
}

function getQueryParams(){
	var params = {};
	var type = $("#search-type").val();
	if(type!=""){
		params["type.eq"]= type;
	}else{
		params["type.gte"]= 1;
		params["type.lte"]= 4;
	}
	var code = $("#search-code").val();
	if(code!=""){
		params["code.like"]= code;
	}
	return params;
}

function getQueryUrl(){
	return management_api_server_servlet_path+"/common/query/"+getApiName()+".json?countable=true";
}

function getColumns(){
	return [{field: 'check',checkbox :true},
		{field:"type",title:"类型",formatter: function(value,row,index){
			 var type=row.type;
			 if(type==1){
				 return "实体对象";
			 }else if(type==2){
				 return "对象描述";
			 }else if(type==3){
				 return "消息描述";
			 }else if(type==4){
				 return "消息样例";
			 }
		 }},
		 {field:"code",title:"代码"},
		 {field:"createTime",title:"创建时间"},
		 {field:"content",title:"内容",formatter: function(value,row,index){
			 var content=row.content;
			 if(content.length > 50){
				 return content.substr(0,50)+"...";
			 }
			 return content;
		 }},
		 {title: '操作',field: 'opear',formatter: function (value, row) {
             var html = '<a   href="javascript:void(0)" class="table_edit" title="编辑" onclick="edit(\'' + row.id + '\')">编辑</a>';
             html += '<a   href="javascript:void(0)" class="table_del" title="删除" onclick="del(\'' + row.id + '\')">删除</a>';
          return html ;
   }}];
}