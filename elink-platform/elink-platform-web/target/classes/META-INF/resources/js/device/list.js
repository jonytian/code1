$(function() {
	initBootstrapTable();
	$("#edit-frm-state-select").change(function() {
		$("#edit-frm-state").val($(this).val());
	});
	
	$("#edit-frm-protocolVersion").change(function() {
		var val = $(this).val();
		setSimCodeLimit(val);
		if(val=="2016" || val=="201602" || val=="tjsatl"){
			$("#edit-frm-vedioProtocol").val("1078");
		}else{
			$("#edit-frm-vedioProtocol").val("0000");
		}
	});
	
	initFileUploader();
});

function setSimCodeLimit(protocolVersion){
	if(protocolVersion=="2019"){
		$("#edit-frm-simCode").attr("maxlength","20");
		$("#edit-frm-simCode").attr("rangelength","20,20");
		$("#edit-frm-simCode").attr("placeholder","终端对应的设备ID(对应协议定义的sim卡号码)，20位，不足前面补0");
	}else{
		$("#edit-frm-simCode").attr("maxlength","12");
		$("#edit-frm-simCode").attr("rangelength","12,12");
		$("#edit-frm-simCode").attr("placeholder","终端对应的设备ID(对应协议定义的sim卡号码)，12位，不足前面补0");
	}
}

function getApiName() {
	return "device";
}

function getQueryParams() {
	var params = {};
	var name = $("#search-name").val();
  //midified by liliang 20200304
	name = name.replace(/(^\s*)|(\s*$)/g, "");//去除字符两边空格
	if (name) {
		params["name.like"] = name;
	}
	
	var simCode = $("#search-simCode").val();
	if (simCode) {
		params["simCode.like"] = simCode;
	}
	
	var state = $("#search-state").val();
	if (state) {
		params["state.eq"] = state;
	}
	
	var protocolVersion = $("#search-protocolVersion").val();
	if (protocolVersion) {
		params["protocolVersion.eq"] = protocolVersion;
	}
	
	var startOnlineTime = $("#search-startOnlineTime").val();
	if (startOnlineTime) {
		params["lastOnlineTime.gte"] = new Date(startOnlineTime.replace(/-/g, "/")).getTime();
	}

	var endOnlineTime = $("#search-endOnlineTime").val();
	if (endOnlineTime) {
		params["lastOnlineTime.lte"] = new Date(endOnlineTime.replace(/-/g, "/")).getTime();
	}

	var startOfflineTime = $("#search-startOfflineTime").val();
	if (startOfflineTime) {
		params["lastOfflineTime.gte"] = new Date(startOfflineTime.replace(/-/g, "/")).getTime();
	}

	var endOfflineTime = $("#search-endOfflineTime").val();
	if (endOfflineTime) {
		params["lastOfflineTime.lte"] = new Date(endOfflineTime.replace(/-/g, "/")).getTime();
	}

	var groupId = $("#search-frm-groupId").val();
	if (groupId) {
		params["groupId.eq"] = groupId;
	}
	
	var enterpriseId = $("#search-frm-enterpriseId").val();
	if (enterpriseId) {
		params["enterpriseId.eq"] = enterpriseId;
	}
	return params;
}

function getQueryUrl() {
	return "/common/query/" + getApiName() + ".json?countable=true";
}

function getColumns() {
	return [
			{
				field : 'check',
				checkbox : true
			},
			{
				title : "设备名称",
				field : 'name'
			},
			{
				title : '设备ID',
				field : 'simCode'
			},
			{
				title : "鉴权码",
				field : "authCode"
			},
			{
				title : "状态",
				field : "state",
				sortable : true,
				formatter : function(value, row, index) {
					// 设备状态,0:未注册；1:已注册；2:离线；3:在线；;4:已注销；5：已停用
					var state = row.state;
					if (state == 0) {
						return "未注册";
					} else if (state == 1) {
						return "已注册";
					} else if (state == 2) {
						return "离线";
					} else if (state == 3) {
						return "在线";
					} else if (state == 4) {
						return "已注销";
					} else if (state == 5) {
						return "已停用";
					}
				}
			},
			{
				title : "协议版本",
				field : "protocolVersion",
				formatter : function(value, row, index) {
					var protocolVersion = row.protocolVersion;
					if (protocolVersion == "2011") {
						return "JT/T 808-2011";
					} else if (protocolVersion == "2013") {
						return "JT/T 808-2013";
					} else if (protocolVersion == "2015") {
						return "JT/T 808-2015";
					} else if (protocolVersion == "2016") {
						return "JT/T 808-2011+1078-2016";
					}else if (protocolVersion == "201602") {
						return "JT/T 808-2013+1078-2016";
					} else if (protocolVersion == "2019") {
						return "JT/T 808-2019";
					}else if (protocolVersion == "btobd") {
						return "邦通OBD";
					}else if (protocolVersion == "tjsatl") {
						return "T/JSATL-2018（苏标）";
					}
					return protocolVersion;
				}
			},
			{
				title : "最后上线时间",
				field : "lastOnlineTime",
				sortable : true,
				formatter : function(value, row, index) {
					var lastOnlineTime = row.lastOnlineTime;
					if (lastOnlineTime == null) {
						return "";
					}
					return new Date(lastOnlineTime).format();
				}
			},
			{
				title : "最后下线时间",
				field : "lastOfflineTime",
				sortable : true,
				formatter : function(value, row, index) {
					var lastOfflineTime = row.lastOfflineTime;
					if (lastOfflineTime == null) {
						return "";
					}
					return new Date(lastOfflineTime).format();
				}
			},
			{
				title : "创建时间",
				field : "createTime",
				sortable : true,
				visible : false,
				align : 'center'
			},
			{
				title : '操作',
				field : 'opear',
				width : '200px',
				formatter : function(value, row) {
					var html = '<a href="javascript:void(0)" class="table_edit" title="编辑" onclick="edit(\''+ row.id + '\')">编辑</a>';
					//// 设备状态,0:未注册；1:已注册；2:离线；3:在线；;4:已注销；5：已停用
					if(row.state!=5){
						html += '<a href="javascript:void(0)" class="table_del" title="停用" onclick="disable(\''+ row.id + '\')">停用</a>';
					}else if(row.state==5){
						html += '<a href="javascript:void(0)" class="table_edit" title="启用" onclick="enable(\''+ row.id + '\')">启用</a>';
					}
					if(row.state!=4){
						html += '<a href="javascript:void(0)" class="table_del" title="注销" onclick="cancel(\''+ row.id + '\')">注销</a>';
					}
					html += '<a href="javascript:void(0)" class="table_del" title="删除" onclick="del(\''+ row.id + '\')">删除</a>';
					return html;
				}
			} ];
}

function setEditInfo(result) {
	if (result.state > 1) {
		$("#edit-frm-state-div").hide();
	}
	$("#edit-frm-isCreateCar-div").hide();
	$("#edit-frm-state-select").val(result.state);
	$("#edit-frm-oldSimCode").val(result.simCode);
	setSimCodeLimit(result.protocolVersion);
}

function setAddInfo() {
	$("#edit-frm-isCreateCar-div").show();
	$("input[type=checkbox][name=isCreateCar]").val(1);
	$("input[type=checkbox][name=isCreateCar]").prop("checked",true);
	$("#edit-frm-protocolVersion").val("2013");
	$("#edit-frm-state").val(0);
	$("#edit-frm-terminalId").val("0000000");
	$("#edit-frm-groupId").val($("#search-frm-groupId").val());
	$("#edit-frm-state-select").val(0);
	var authCode = RandomString(7);
	$("#edit-frm-authCode").val(authCode);
	$("#edit-frm-state-div").show();
}

function RandomString(length) {
	var str = '';
	for (; str.length < length; str += Math.random().toString(36).substr(2))
		;
	return str.substr(0, length);
}

function cancel(id) {
	var row = getBootstrapTableRowById(id);
	if (row.state == 4) {
		showMessage("该设备已经被注销");
		return;
	}
	setState(id, 4, "确定要注销该设备？");
}

function disable(id) {
	var row = getBootstrapTableRowById(id);
	if (row.state == 5) {
		showMessage("该设备已经被停用");
		return;
	}
	setState(id, 5, "确定要停用该设备？");
}

function enable(id) {
	var row = getBootstrapTableRowById(id);
	if (row.state != 5 && row.state != 4) {
		showMessage("该设备已启用");
		return;
	}
	setState(id, 1, "确定要启用该设备？");
}

function setState(id, state, message) {
	if (!enableOption()) {
		return;
	}
	showConfirm(message, function() {
		var url = management_api_server_servlet_path + "/common/" + getApiName() + "/" + id + ".json";
		var data = {
			"state" : state
		};
		ajaxAsyncPatch(url, data, function(result) {
			if (result.code != 0) {
				showErrorMessage(result.message);
			} else {
				showMessage("设置成功");
				forceOffline(id);
				doQuery();
			}
		});
	});

}

function delSuccessHandler(id) {
	var ids = id.split(",");
	for (var i = 0; i < ids.length; i++) {
		forceOffline(ids[i])
	}
}

function saveSuccessHandler(data){
	if(data.state==3){
		forceOffline(data.id);
	}
}

function forceOffline(id) {
	var url = message_api_server_servlet_path + "/device/offline/" + id + ".json";
	ajaxAsyncDel(url, {}, function() {});
}

function isEditDisabled() {
	return !enableOption();
}

function isDelDisabled() {
	return !enableOption();
}

function importDevice() {
	if (!enableOption()) {
		return;
	}
	showDialog("批量导入设备", "device-import-dlg");
}

function initFileUploader() {
	$('#file_upload').JSAjaxFileUploader({
		uploadUrl:getContextPath()+"/attachment/upload.do",
		autoSubmit:false,
		formData:{
			"url": management_api_server_servlet_path+"/device/import.json"
		},
		allowExt:"xlsx|xls",
		inputText:"请选择要导入的数据文件...",
		uploadTest:"导入",
		fail:function(result){
			showMessage("批量导入失败");
		},
		success:function(result){
			if (result.code!=0) {
				if(result.data){
					var data = result.data;
					var message = "成功导入"+data.success+"条记录,导入失败"+data.failure+"条记录，请下载导入失败记录列表查看原因";
					showErrorMessage(message);
					closeDialog();
					doQuery();
					 var mediaUrl=getContextPath()+"/attachment/download.do?url="+management_api_server_servlet_path+"/device/import/result.json";
					 window.open(mediaUrl);
				}else{
					showErrorMessage(result.message);
				}
			}else{
				showMessage("批量导入成功");
				closeDialog();
				doQuery();
			}
		},
		error:function(result){
			showMessage("批量导入失败");
		}
	});
}