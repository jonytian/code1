function getApiName() {
	return "systemAlarm";
}

function getQueryParams() {
	var params = {};
	var type = $("#search-type").val();
	if (type) {
		params["type.eq"] = type;
	}
	var state = $("#search-state").val();
	if (state) {
		params["state.eq"] = state;
	}
	params["userId.eq"] = getUserId();
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
		field : "type",
		title : "类型",
		sortable : true,
		formatter : function(value, row, index) {
			return alarmNotifyType.get(row.type);
		}
	}, {
		field : "title",
		title : "标题"
	}, {
		field : "content",
		title : "内容"
	},{
		title : "状态",
		field : "state",
		sortable : true,
		formatter : function(value, row, index) {
			//类型，1：燃油；2：维修；3：保险；4：洗车
			var state = row.state;
			if (state == 1) {
				return "已读";
			} else {
				return "未读";
			}
		}
	}, {
		field : "createTime",
		title : "接收时间"
	}, {
		title : '操作',
		field : 'opear',
		formatter : function(value, row) {
			var html ="";// '<a   href="javascript:void(0)" class="table_edit" title="查看" onclick="view(\''+ row.id + '\')">查看</a>';
			if(row.state == 0){
				html += '<a   href="javascript:void(0)" class="table_edit" title="设置已读" onclick="read(\''+ row.id + '\')">设置已读</a>';
			}
			if(html==""){
				html="-"
			}
			return html;
		}
	}  ];
}

function read(id){
	if(id){
		setRead(id)
	}else{
		var rows = getBootstrapTableSelectedRows();
		for(var i=0;i<rows.length;i++){
			var row = rows[i];
			if(row.state==0){
				setRead(row.id);
			}
		}
	}
	doQuery();
	window.parent.parent.alarmNotifyOverview();
}

function setRead(id){
	var url =  management_api_server_servlet_path+"/common/"+getApiName()+"/"+id+".json";
	var data = {"state":1};
	return ajaxSyncPatch(url,data);
}

function view(id){
	var row = getBootstrapTableRowById(id);
	
	showCommondDialog("message-detail-dlg");
}