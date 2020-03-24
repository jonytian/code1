$(function() {
	initLimitDatetimepicker("edit-frm-startDateTime","edit-frm-endDateTime",new Date(),"",0.5);
	initBootstrapTable();
	initRoute();
	
	$("input:radio[name='timeAttr']").change(function(){
		var val = $(this).val();
		if(val==1){
			$("#start-end-time-div").show();
			$("#edit-frm-startTime").addClass("required");
			$("#edit-frm-endTime").addClass("required");
			
			$("#start-end-datetime-div").hide();
			$("#edit-frm-startDateTime").removeClass("required");
			$("#edit-frm-endDateTime").removeClass("required");
		}else{
			$("#start-end-datetime-div").show();
			$("#edit-frm-startDateTime").addClass("required");
			$("#edit-frm-endDateTime").addClass("required");
			
			$("#start-end-time-div").hide();
			$("#edit-frm-startTime").removeClass("required");
			$("#edit-frm-endTime").removeClass("required");
		}
	});
});

function getApiName() {
	return "road";
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
		title : "路线名称"
	}, {
		field : "attribute",
		title : "属性",
		formatter : function(value, row) {
			var attribute = "";
			var desc = ["根据时间","","进路线报警给驾驶员","进路线报警给平台","出路线报警给驾驶员","出路线报警给平台"];
			for(var i=0;i<desc.length;i++){
				if((((row.attribute&(1<<i))>>i)==1)) {
					attribute+=","+desc[i]
				}
			}
			if(attribute){
				return attribute.substr(1);
			}
			return "-";
		}
	},{
		field : "startTime",
		title : "开始时间"
	}, {
		field : "endTime",
		title : "结束时间"
	}, {
		field : "createTime",
		title : "消息时间"
	}, {
		title : '操作',
		field : 'opear',
		formatter : function(value, row) {
			var html = '';
			html +='<a href="javascript:void(0)" class="table_edit" title="编辑" onclick="edit(\'' + row.id + '\')">编辑</a>';
			html +='<a href="javascript:void(0)" class="table_del" title="删除" onclick="del(\'' + row.id + '\')">删除</a>';
			html +='<a href="javascript:void(0)" class="table_view" title="详情" onclick="view(\'' + row.id + '\')">详情</a>';
			return html;
		}
	}  ];
}

function view(id){
	var row = getBootstrapTableRowById(id);
}

function initRoute(){
	var url = "/common/query/route.json?countable=false&select=id,name&pageNo=1&pageSize=1000";
	var params = {};
	$('#multiple-select-source').empty();
	$('#multiple-selected-target').empty();
	ajaxAsyncPost(url, params, function(result) {
		if (result.code != 0) {
			showErrorMessage(result.message);
		} else {
			var list = result.data;
			for (var i = 0; i < list.length; i++) {
				var item = list[i];
				$("#multiple-select-source").append("<option value='" + item[0] + "'>" + item[1] + "</option>");
			}
		}
	});
}

function add(){
	showCommondDialog('edit-dlg');
}

function showNext(){
	var formId = "edit-frm";
	if (!validForm(formId)) {
		return;
	}
	showInfoTap(1, formId);
}
function showInfoTap(index, formId) {
	$(".tab_con_div", "#" + formId).eq(index).show().siblings().hide();
}