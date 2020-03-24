$(function() {
	initBootstrapTable();
});

function getApiName() {
	return "route";
}

function getQueryParams() {
	var params = {};
	var name = $("#search-name").val();
	if(name!=""){
		params["name.like"] = name;
	}
	return params;
}

function getQueryUrl() {
	return management_api_server_servlet_path + "/common/query/" + getApiName() + ".json?countable=true";
}

function getColumns() {
	return [ {
		field : 'check',
		checkbox : true
	}, {
		field : "name",
		title : "路段名称"
	}, {
		field : "width",
		title : "路宽"
	}, {
		field : "attribute",
		title : "属性",
		formatter : function(value, row) {
			var attribute = "";
			if((((row.attribute&(1<<0))>>0)==1)) {
				attribute+=",行驶时间"
			}
			if((((row.attribute&(1<<1))>>1)==1)) {
				attribute+=",限速"
			}
			if(attribute){
				return attribute.substr(1);
			}
			return "-";
		}
	}, {
		field : "maxSpeed",
		title : "最高速度 "
	}, {
		field : "durationTime",
		title : "超速持续时间 "
	}, {
		field : "itTravelTime",
		title : "行驶过长阈值"
	}, {
		field : "gtTravelTime",
		title : "行驶不足阈值"
	}, {
		field : "createTime",
		title : "创建时间"
	}, {
		title : '操作',
		field : 'opear',
		formatter : function(value, row) {
			var html = '';
			html +='<a href="javascript:void(0)" class="table_edit" title="编辑" onclick="edit(\'' + row.id + '\')">编辑</a>';
			html +='<a href="javascript:void(0)" class="table_del" title="删除" onclick="del(\'' + row.id + '\')">删除</a>';
			// modify by tyj.20200311
			// html +='<a href="javascript:void(0)" class="table_view" title="详情" onclick="view(\'' + row.id + '\')">详情</a>';
			return html;
		}
	}  ];
}

function add(){
	edit("");
}

function edit(id){
	window.parent.frames["content-frame"].location.href="editRoute.do?id="+id;
}

function view(id){
	 edit(id);
}