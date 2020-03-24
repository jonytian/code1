var zTreeSetting = {
	check : {
		enable : false
	},
	callback : {
		onClick : zTreeOnClick,
		onRightClick : zTreeOnRightClick,
		beforeExpand : zTreeBeforeExpand,
		onExpand : zTreeOnExpand
	}
};

function zTreeOnClick(event, treeId, treeNode) {
	if (typeof (onClickTreeNode) == "function") {
		onClickTreeNode(treeId, treeNode);
	}
}

function zTreeOnRightClick(event, treeId, treeNode) {
	if (typeof (onRightClickTreeNode) == "function") {
		onRightClickTreeNode(treeId, treeNode);
	}
}

function zTreeBeforeExpand(treeId, treeNode) {
	if (typeof (beforeExpandTreeNode) == "function") {
		beforeExpandTreeNode(treeId, treeNode);
	}
	return true;
}

function zTreeOnExpand(event, treeId, treeNode) {
	if (typeof (onExpandTreeNode) == "function") {
		onExpandTreeNode(treeId, treeNode);
	}
}

function initTree(treeId) {
	var setting;
	if (typeof (getTreeSetting) == "function") {
		setting = getTreeSetting();
	} else {
		setting = zTreeSetting;
	}
	var nodes = getInitTreeNodes();
	$.fn.zTree.init($("#" + treeId), setting, nodes);
}

function addTreeNodes(treeId, parent, children) {
	getTree(treeId).addNodes(parent, children);
}

function removeChildNodes(treeId, parent) {
	getTree(treeId).removeChildNodes(parent);
}

function getTreeNodeById(treeId, id) {
	return getTree(treeId).getNodeByParam("id", id);
}

function getSelectedNodes(treeId) {
	return getTree(treeId).getSelectedNodes();
}

function getTree(treeId) {
	return $.fn.zTree.getZTreeObj(treeId);
}

function getEnterpriseTreeNodes() {
	var children = getSubsidiaryNodes(null);
	var enterprise = getUserEnterprise();
	var root = {};
	root.icon = getDefaultTreeIcon();
	root.id = enterprise.id;
	root.name = "锐承物联数据平台";
	root.open = true;
	root.type = "ent";
	root.children = children;
	root.nocheck = true;
	return root;
}

function getSubsidiaryNodes(enterpriseId) {
	var url = management_api_server_servlet_path
			+ "/aas/subsidiary.json?select=id,shortName";
	if (enterpriseId) {
		url += "&enterpriseId=" + enterpriseId;
	}
	var result = ajaxSyncGet(url, {});
	if (result.code != 0) {
		showErrorMessage(result.message);
	} else {
		var children = [];
		if (result.data && result.data.length > 0) {
			var list = result.data;
			for (var i = 0; i < list.length; i++) {
				var item = list[i];
				var node = {};
				node.id = item[0];
				node.name = item[1];
				node.type = "ent";
				node.open = false;
				node.isParent = true;
				node.icon = getDefaultTreeIcon();
				node.nocheck = true;
				children.push(node);
			}
		}
		return children;
	}
}

function getGroupTree(groupType, enterpriseId) {
	var url = management_api_server_servlet_path
			+ "/device/group/ztree.json?type=" + groupType + "&enterpriseId="
			+ enterpriseId;
	var result = ajaxSyncGet(url, {});
	if (result.code != 0) {
		showErrorMessage(result.message);
	} else {
		return result.data;
	}
}

function getDefaultGroupTree(groupType) {
	var enterprise = getUserEnterprise();
	var children = getGroupTree(groupType, enterprise.id);
	var subsidiaryNodes = getSubsidiaryNodes(null);
	if (children) {
		children = children.concat(subsidiaryNodes);
	}else{
		children = subsidiaryNodes;
	}

	var root = {};
	root.icon = getDefaultTreeIcon();
	root.id = enterprise.id;
	root.name = "锐承物联数据平台";
	root.open = true;
	root.type = "ent";
	root.nocheck = true;
	root.children = children;
	return root;
}

function onExpandGroupTreeNode(groupType, treeId, parent) {
	if (parent.type && parent.type != 'ent') {
		return;
	}

	var children = parent.children;
	if (children && children.length > 0) {
		return;
	}

	// 加载公司以及分组
	var enterprise = getUserEnterprise();
	if (enterprise.id != parent.id) {
		var treeNodes = getSubsidiaryNodes(parent.id);
		if (treeNodes && treeNodes.length > 0) {
			addTreeNodes(treeId, parent, treeNodes);
		}
	}

	var treeNodes = getGroupTree(groupType, parent.id);
	if (treeNodes && treeNodes.length > 0) {
		addTreeNodes(treeId, parent, treeNodes);
	}
}

var query_page_cache = new Cache("query_page_cache");
function doQuery() {
	pageNo = 1;
	query_page_cache.remove("query_page_cache");
	$('#boot-strap-table').bootstrapTable("destroy");
	initBootstrapTable();
}

function refreshBootstrapTable() {
	$("#boot-strap-table").bootstrapTable("refresh", {
		silent : true
	});
}

// 表格部分
var pageSize = 10, pageNo = 1;
function initBootstrapTable() {
	var url;
	if (typeof (getBootstrapRequestMothed) == "function") {
		url = getContextPath() + "/common/" + getBootstrapRequestMothed()
				+ ".do";
	} else {
		url = getContextPath() + "/common/post.do";
	}

	var isShowPaginationDetail = true;
	if (typeof (showPaginationDetail) == "function") {
		isShowPaginationDetail = showPaginationDetail();
	}

	var isShowPaginationSwitch = true;
	if (typeof (showPaginationSwitch) == "function") {
		isShowPaginationSwitch = showPaginationSwitch();
	}

	if (typeof (getPageSize) == "function") {
		pageSize = getPageSize();
	}

	var isShowToolbar = true;
	if (typeof (showToolbar) == "function") {
		isShowToolbar = showToolbar();
	}

	var defaultSort = "createTime";
	if (typeof (getDefaultSort) == "function") {
		defaultSort = getDefaultSort();
	}

	var idField = "id";
	if (typeof (getIdField) == "function") {
		idField = getIdField();
	}

	var detailView = false;
	if (typeof (isDetailView) == "function") {
		detailView = isDetailView();
	}
	
	var pageCache = query_page_cache.get("query_page_cache");
	if(isCacheQuery() && pageCache){
		var arr = pageCache.split(",");
		pageNo = parseInt(arr[0]);
		pageSize = parseInt(arr[1]);
	}

	startLoading();
	$('#boot-strap-table')
			.bootstrapTable(
					{
						method : "post",
						url : url,
						striped : true,
						singleSelect : false,
						dataType : "json",
						pageList : [ 10, 25, 50, 100 ],
						pagination : true, // 分页
						pageSize : pageSize,
						pageNumber : pageNo,
						search : false, // 显示搜索框
						showColumns : isShowToolbar,
						showRefresh : isShowToolbar,
						showToggle : isShowToolbar,
						clickToSelect : true,
						detailView : detailView,
						showPaginationSwitch : isShowPaginationSwitch,
						showPaginationDetail : isShowPaginationDetail,
						idField : idField,
						uniqueId : idField,
						toolbar : "#toolbar",
						contentType : "application/json; charset=utf-8",
						onClickRow : function(row, $element) {
							if (typeof (onClickBootstrapTableRow) == "function") {
								onClickBootstrapTableRow(row);
							}
						},
						onCheck : function(row, $element) {
							if (typeof (onCheckBootstrapTableRow) == "function") {
								onCheckBootstrapTableRow(row);
							}
						},
						onUncheck : function(row, $element) {
							if (typeof (onUncheckBootstrapTableRow) == "function") {
								onUncheckBootstrapTableRow(row);
							}
						},
						onCheckAll : function(rows, $element) {
							if (typeof (onCheckAllBootstrapTableRow) == "function") {
								onCheckAllBootstrapTableRow(rows);
							}
						},
						onUncheckAll : function(rows, $element) {
							if (typeof (onUncheckAllBootstrapTableRow) == "function") {
								onUncheckAllBootstrapTableRow(rows);
							}
						},
						detailFormatter : function(index, row, $element) {
							if (typeof (bootstrapTableDetailFormatter) == "function") {
								return bootstrapTableDetailFormatter(index,
										row, $element);
							}
						},
						onExpandRow : function(index, row, $element) {
							if (typeof (onExpandBootstrapTableRow) == "function") {
								return onExpandBootstrapTableRow(index, row,
										$element);
							}
						},
						queryParams : function(params) { // 请求服务器数据时发送的参数，可以在这里添加额外的查询参数，返回false则终止请求
							pageSize = params.limit;
							pageNo = (params.offset / pageSize) + 1;
							
							var sort = params.sort;
							var desc = true;
							if (!sort) {
								sort = defaultSort;
							} else {
								if (params.order == 'asc') {
									desc = false;
								}
							}
							var rest = getQueryUrl();
							if (rest.indexOf("?") == -1) {
								rest += "?";
							} else {
								if (rest.charAt(rest.length - 1) == "?") {

								} else if (rest.charAt(rest.length - 1) != "&") {
									rest += "&";
								}
							}
							var queryParams = getQueryParams();
							query_page_cache.put("query_page_cache",pageNo+","+pageSize);
							return $.toJSON({
								url : rest + "orderBy=" + sort + "&desc="
										+ desc + "&pageSize=" + pageSize
										+ "&pageNo=" + pageNo,
								data : queryParams
							});
						},
						sidePagination : "server", // 服务端请求
						responseHandler : function(res) {
							if (res.code != 0) {
								showErrorMessage(res.message);
							} else {
								var list = res.data;
								if (list.rows) {
									return list;
								} else {
									if (list.length == pageSize) {
										return {
											"total" : pageSize * (pageNo + 1),// 还有下一页
											"rows" : list
										};
									} else {
										return {
											"total" : (pageSize * (pageNo - 1))
													+ list.length,// 没有下一页了
											"rows" : list
										};
									}
								}
							}
						},
						onLoadSuccess : function() {
							endLoading();
							if (typeof (onLoadBootstrapTableDataSuccess) == "function") {
								onLoadBootstrapTableDataSuccess();
							}
						},
						onLoadError : function() {
							endLoading();
							showErrorMessage("加载数据出错！");
						},
						onPostBody : function() {
							$(".fixed-table-body").css("overflow-x", "auto");
							$(".fixed-table-body").css("width",
									$("#table_div").width() + "px");
						},
						columns : getColumns()
					});

}

function getBootstrapTableRowById(id) {
	return $('#boot-strap-table').bootstrapTable('getRowByUniqueId', id);
}

function getBootstrapTableSelectedRows() {
	return $('#boot-strap-table').bootstrapTable("getSelections");
}

function validForm(formId) {
	return $("#" + formId).valid();
}

function add() {
	if (typeof (isAddDisabled) == "function" && isAddDisabled()) {
		return;
	}
	clearEditFrm();
	if (typeof (setAddInfo) == "function") {
		setAddInfo();
	}
	var title = "新增" + $("#edit-dlg").attr("title");
	showDialog(title, "edit-dlg");
}

function save() {
	var formId = "edit-frm";
	if (!validForm(formId)) {
		return;
	}

	var data;
	if (typeof (getEditFormData) == "function") {
		data = getEditFormData();
	} else {
		data = $("#" + formId).serializeObject();
	}

	var url = getSaveUrl(data);
	if (data.id) {
		ajaxAsyncPatch(url, data, saveResult, true);
	} else {
		ajaxAsyncPost(url, data, saveResult, true);
	}
}

function getSaveUrl(data) {
	var servletPath = management_api_server_servlet_path;
	if (typeof (getServletPath) == "function") {
		servletPath = getServletPath();
	}
	if (data.id) {
		return servletPath + "/common/" + getApiName() + "/" + data.id
				+ ".json";
	} else {
		return servletPath + "/common/" + getApiName() + ".json";
	}
}

function saveResult(result) {
	if (result.code != 0) {
		showErrorMessage(result.message);
	} else {
		try {
			closeDialog();
			doQuery();
		} catch (e) {
		}
		if (typeof (saveSuccessHandler) == "function") {
			saveSuccessHandler(result.data);
		}
	}
}

function dels() {
	var rows = getBootstrapTableSelectedRows();
	if (rows.length > 0) {
		var ids = "";
		for (var i = 0; i < rows.length; i++) {
			ids += "," + rows[i].id;
		}
		del(ids.substr(1));
	} else {
		showMessage("请选择要删除的记录！");
	}
}

function del(id) {
	if (typeof (isDelDisabled) == "function" && isDelDisabled(id)) {
		return;
	}
	showConfirm("确定要删除选中的记录？", function() {
		var url = getDelUrl(id);
		var data = {};
		ajaxAsyncDel(url, data, function(result) {
			if (result.code != 0) {
				showErrorMessage(result.message);
			} else {
				if (typeof (delSuccessHandler) == "function") {
					delSuccessHandler(id);
				}
				// doQuery();
				refreshBootstrapTable();
			}
		});
	});
}

function getDelUrl(id) {
	var servletPath = management_api_server_servlet_path;
	if (typeof (getServletPath) == "function") {
		servletPath = getServletPath();
	}
	return servletPath + "/common/" + getApiName() + "/" + id + ".json";
}

function edit(id) {
	if (typeof (isEditDisabled) == "function" && isEditDisabled(id)) {
		return;
	}
	clearEditFrm();
	var servletPath = management_api_server_servlet_path;
	if (typeof (getServletPath) == "function") {
		servletPath = getServletPath();
	}
	var url = servletPath + "/common/" + getApiName() + "/" + id + ".json";
	var data = {}
	ajaxAsyncGet(url, data, function(result) {
		if (result.code != 0) {
			showErrorMessage(result.message);
		} else {
			var data = result.data;
			for ( var key in data) {
				$("#edit-frm-" + key).val(data[key]);
			}
			if (typeof (setEditInfo) == "function") {
				setEditInfo(data);
			}

			var title = "修改" + $("#edit-dlg").attr("title");
			showDialog(title, "edit-dlg");
		}
	});
}

function loadEditFrmData(id) {
	var url = management_api_server_servlet_path + "/common/" + getApiName()
			+ "/" + id + ".json";
	var data = {}
	ajaxAsyncGet(url, data, function(result) {
		if (result.code != 0) {
			showErrorMessage(result.message);
		} else {
			var data = result.data;
			for ( var key in data) {
				$("#edit-frm-" + key).val(data[key]);
			}
			if (typeof (loadEditFrmDataSuccessHandler) == "function") {
				loadEditFrmDataSuccessHandler(data);
			}
		}
	});
}

function clearEditFrm() {
	cleanForm("edit-frm");
}

function cleanForm(formId) {
	$(":input", "#" + formId).not(":button,:submit,:reset").val("").removeAttr(
			"checked").removeAttr("selected");
}

function getDeviceName(deviceId) {
	var plateNumber =window.top.getPlateNumberByDeviceId(deviceId);
	if(plateNumber){
		return plateNumber;
	}

	var device = getDeviceCache(deviceId);
	if (device) {
		return device.name;
	}
	return "车辆已删除";
}

function getDeviceCache(deviceId) {
	return window.top.getDeviceCache(deviceId);
}

function getCarDeviceCache(carId) {
	return window.top.getCarDeviceCache(carId);
}

function getPlateNumber(carId) {
	var car = getCarCache(carId);
	if (car) {
		var plateNumber = car.plateNumber;
		if (plateNumber && plateNumber) {
			return plateNumber;
		}
	}
	return null;
}

function getCarCache(carId) {
	return window.top.getCarCache(carId);
}

function getDeviceCarCache(deviceId){
	return window.top.getDeviceCarCache(deviceId);
}

function getEnterprise(enterpriseId) {
	return window.top.getEnterprise(enterpriseId);
}

function getUserEnterprise() {
	return getEnterprise(getUserEnterpriseId());
}

function getFileServer() {
	return window.top.getFileServer();
}

function isOnline(deviceId) {
	var url = management_api_server_servlet_path + "/device/" + deviceId + "/state.json";
	var data = {}
	var result = ajaxSyncGet(url, data);
	if (result && result.data && result.data.length > 0) {
		var item = result.data[0];
		return item.state == 3;
	}
	return false;
}

function getMonthLastDay(month) {
	var dateStr = month + "-01";
	dateStr = dateStr.replace(new RegExp(/-/g), "/");
	var date = new Date(dateStr);
	var currentMonth = date.getMonth();
	var nextMonth = ++currentMonth;
	var nextMonthFirstDay = new Date(date.getFullYear(), nextMonth, 1);
	var oneDay = 1000 * 60 * 60 * 24;
	return new Date(nextMonthFirstDay - oneDay);
}

function getDeviceStateStr(state) {
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

function initDatetimepicker() {
	$(".form_datetime").datetimepicker({
		language : "zh-CN",
		weekStart : 1,
		todayBtn : 1,
		autoclose : 1,
		todayHighlight : 1,
		startView : 2,
		forceParse : 0,
		showMeridian : 1,
		zIndex : 457372800
	});
}

function initDatepicker() {
	$(".form_date").datetimepicker({
		language : "zh-CN",
		weekStart : 1,
		todayBtn : 1,
		autoclose : 1,
		todayHighlight : 1,
		startView : 2,
		forceParse : 0,
		minView : 2,
		zIndex : 457372800
	});
}

function initMonthpicker() {
	$(".form_month").datetimepicker({
		language : "zh-CN",
		weekStart : 1,
		todayBtn : 1,
		autoclose : 1,
		todayHighlight : 1,
		startView : 3,
		forceParse : 0,
		minView : 3,
		zIndex : 457372800
	});
}

function showCommondDialog(dialogId) {
	var title = $("#" + dialogId).attr("title");
	showDialog(title, dialogId);
}

function loadDictionary(type, callback) {
	var url = management_api_server_servlet_path + "/system/dictionary/" + type + ".json";
	var result = ajaxSyncGet(url, {});
	if (result.code != 0) {
		showErrorMessage(result.message);
	} else {
		var list = result.data;
		callback.apply(this, [ list ]);
	}
}

function loadSysDictionary(type, code, callback) {
	var url = management_api_server_servlet_path + "/system/sysDictionary/" + type + ".json?code=" + code;
	var result = ajaxSyncGet(url, {});
	if (result.code != 0) {
		showErrorMessage(result.message);
	} else {
		var list = result.data;
		callback.apply(this, [ list ]);
	}
}

function getDeviceBizState(deviceId) {
	var url = management_api_server_servlet_path + "/device/" + deviceId + "/bizState.json";
	var data = {}
	var result = ajaxSyncGet(url, data);
	if (result && result.data && result.data.length > 0) {
		var item = result.data[0];
		return item.bizState;
	}
	return 0;
}

function setSelectOption(id, list, isNotEmpty) {
	if (isNotEmpty) {
	} else {
		$("#" + id).empty();
	}
	for (var i = 0, l = list.length; i < l; i++) {
		var item = list[i];
		$("#" + id).append(
				"<option value='" + item.code + "'>" + item.content
						+ "</option>");
	}
	$("#" + id + " option:first").prop("selected", 'selected');
}

function initUploadImg(inputFileId, data, resultInputId) {
	$("#" + inputFileId).ajaxImageUpload(
			{
				url : getContextPath() + "/attachment/upload.do", // 上传的服务器地址
				data : data,
				maxNum : 1, // 允许上传图片数量
				zoom : true, // 允许放大
				allowType : [ "gif", "jpeg", "jpg", "bmp", 'png' ], // 允许上传图片的类型
				maxSize : 1, // 允许上传图片的最大尺寸，单位M
				before : function() {
					// alert('上传前回调函数');
				},
				success : function(result) {
					var imgId = result.data;
					if (resultInputId) {
						$("#" + resultInputId).val(imgId);
						$("#" + resultInputId + "-box").html(
								"<img src='" + getContextPath()
										+ "/attachment/download.do?url="
										+ management_api_server_servlet_path
										+ "/image/" + imgId + ".json'/>");
					}
					if (typeof (uploadImgSuccessHandler) == "function") {
						uploadImgSuccessHandler(imgId);
					}
				},
				error : function(e) {
					showMessage("上传失败");
					// console.log(e);
				}
			});
}

function initRangeDatetimepicker(startDatetimepicker, endDatetimepicker, limit) {// 日期范围
	$('#' + startDatetimepicker).datetimepicker({
		language : "zh-CN",
		weekStart : 1,
		todayBtn : 1,
		autoclose : 1,
		todayHighlight : 1,
		forceParse : 0,
		startView : 2,
		minView : 1,
		showMeridian : 1,
		zIndex : 457372800
	}).on('changeDate', function(e) {
		if(limit && limit > 0){
			var time = e.date;
			var endTime = new Date($("#" + endDatetimepicker).val().replace(/-/g, "/")).getTime();
			if( endTime < time ||  (endTime-time) > limit * 1000 * 60 * 60 * 24){
				var time = new Date(time.format("yyyy/MM/dd") + " 00:00:00");
				time.setTime(time.getTime() + limit * 1000 * 60 * 60 * 24);
				$("#" + endDatetimepicker).datetimepicker("setEndDate", time);
				$("#" + endDatetimepicker).val(time.format("yyyy-MM-dd hh:00:00"));
			}
		}
	});

	// 结束时间
	$('#' + endDatetimepicker).datetimepicker({
		language : "zh-CN",
		weekStart : 1,
		todayBtn : 1,
		autoclose : 1,
		todayHighlight : 1,
		startView : 2,
		minView : 1,
		forceParse : 0,
		showMeridian : 1,
		zIndex : 457372800
	}).on('changeDate', function(e) {
		if(!limit){
			limit=1
		}
		var time = e.date;
		var startTime = new Date($("#" + startDatetimepicker).val().replace(/-/g, "/")).getTime();
		if(time < startTime || (time-startTime) > limit * 1000 * 60 * 60 * 24){
			var time = new Date(time.format("yyyy/MM/dd") + " 00:00:00");
			time.setTime(time.getTime() - limit * 1000 * 60 * 60 * 24);
			$("#" + startDatetimepicker).val(time.format("yyyy-MM-dd hh:00:00"));
		}
	});
}

function initLimitDatetimepicker(startDatetimepicker, endDatetimepicker,
		startTime, endTime, endStartLimit, endEndLimit) {// 日期范围
	$('#' + startDatetimepicker).datetimepicker({
		language : "zh-CN",
		weekStart : 1,
		todayBtn : 1,
		autoclose : 1,
		todayHighlight : 1,
		forceParse : 0,
		startView : 2,
		minView : 1,
		showMeridian : 1,
		zIndex : 457372800
	}).on('changeDate', function(e) {
		var time = e.date;
		if (endStartLimit) {
			var start = new Date(time.format("yyyy/MM/dd hh") + ":00:00");
			start.setTime(start.getTime() + 1000 * 60 * 60 * endStartLimit);
			$("#" + endDatetimepicker).datetimepicker("setStartDate", start);
		} else {
			$("#" + endDatetimepicker).datetimepicker("setStartDate", time);
		}
		if (endEndLimit) {
			var end = new Date(time.format("yyyy/MM/dd") + " 00:00:00");
			end.setTime(end.getTime() + 1000 * 60 * 60 * endEndLimit);
			$("#" + endDatetimepicker).datetimepicker("setEndDate", end);
		}
		
		var end = $('#'+endDatetimepicker).val();
		var end = new Date(end.replace(/-/g, "/"));
		if(end.getTime() <= time){
			var n = endStartLimit;
			if (!n) {
				n=1;
			}
			time.setTime(time.getTime() + 1000 * 60 * 60 * n);
			$('#'+endDatetimepicker).val(new Date(time).format("yyyy-MM-dd hh:mm:ss"));
		}
	});

	// 结束时间
	$('#' + endDatetimepicker).datetimepicker({
		language : "zh-CN",
		weekStart : 1,
		todayBtn : 1,
		autoclose : 1,
		todayHighlight : 1,
		startView : 2,
		minView : 1,
		forceParse : 0,
		showMeridian : 1,
		zIndex : 457372800
	}).on('changeDate', function(e) {
		var time = e.date;
		var start = $('#'+startDatetimepicker).val();
		var start = new Date(start.replace(/-/g, "/"));
		
		if(start.getTime() >= time){
			var n = endStartLimit;
			if (!n) {
				n=1;
			}
			time.setTime(time.getTime() + 1000 * 60 * 60 * n);
			$('#'+endDatetimepicker).val(new Date(time).format("yyyy-MM-dd hh:mm:ss"));
		}
	});

	if (startTime) {
		$("#" + startDatetimepicker).datetimepicker("setStartDate", startTime);
		if (endStartLimit) {
			$("#" + endDatetimepicker).datetimepicker("setStartDate", startTime);
		}else{
			$("#" + endDatetimepicker).datetimepicker("setStartDate", startTime.getTime() + 1000 * 60 * 60 * endStartLimit);
		}
	}
	if (endTime) {
		$("#" + startDatetimepicker).datetimepicker("setEndDate", endTime);
		$("#" + endDatetimepicker).datetimepicker("setEndDate", endTime);
	}
}

function initChannelOption(obj) {
	var keys = channelMap.getKeys();
	for (var i = 0; i < keys.length; i++) {
		var key = keys[i];
		var val = channelMap.get(key);
		$(obj).append("<option value='" + key + "'>" + val + "</option>");
	}
}

function getDownstreamMessageState(id) {
	var url = message_api_server_servlet_path + "/deviceDownMessage/" + id
			+ "/state.json";
	var result = ajaxSyncGet(url, {});
	if (result.code != 0) {
		showErrorMessage(result.message);
	} else {
		var list = result.data;
		if (list && list.length > 0) {
			return list[0].state;
		}
	}
	return null;
}

function getTjsatlAlarmDesc(item) {
	var alarmStr = "";
	for(var i=0;i<adasAlarmDesc.length;i++){
		var s = adasAlarmDesc[i];
		if(s && item["adas"+(i+1)]){
			alarmStr += "," + s;
		}
	}
	
	for(var i=0;i<dsmAlarmDesc.length;i++){
		var s = dsmAlarmDesc[i];
		if(s && item["dsm"+(i+1)]){
			alarmStr += "," + s;
		}
	}
	
	//20个轮胎足够了
	for(var i=0;i<20;i++){
		var alarm = item["tpm"+i];
		if(alarm){
			for (var j = 0; j < tpmAlarmDesc.length; j++) {
				var s = "";
				if((((alarm&(1<<j))>>j)==1)) {
					s = tpmAlarmDesc[j];
				}
				if (s!="") {
					alarmStr += "," + s;
				}
			}
		}
	}
	
	for(var i=0;i<bsdAlarmDesc.length;i++){
		var s = bsdAlarmDesc[i];
		if(s && item["bsd"+(i+1)]){
			alarmStr += "," + s;
		}
	}
	return alarmStr;
}

function fullscreen() {
	elem = document.body;
	if (elem.webkitRequestFullScreen) {
		elem.webkitRequestFullScreen();
	} else if (elem.mozRequestFullScreen) {
		elem.mozRequestFullScreen();
	} else if (elem.requestFullScreen) {
		elem.requestFullscreen();
	} else {
		// 浏览器不支持全屏API或已被禁用
	}
}

function exitFullscreen() {
	var elem = document;
	if (elem.webkitCancelFullScreen) {
		elem.webkitCancelFullScreen();
	} else if (elem.mozCancelFullScreen) {
		elem.mozCancelFullScreen();
	} else if (elem.cancelFullScreen) {
		elem.cancelFullScreen();
	} else if (elem.exitFullscreen) {
		elem.exitFullscreen();
	} else {
		// 浏览器不支持全屏API或已被禁用
	}
}

function getFormatNumber(value,fixed){
	if (parseFloat(value).toString()=="NaN") {
      return value;
	}
	if(value > 10000 * 100000){
		return (value/(10000 * 100000)).toFixed(2) + "十亿"
	}else if(value > 10000 * 10000){
		return (value/(10000 * 10000)).toFixed(2) + "亿"
	}else if(value > 10000 * 1000){
		return (value/(10000 * 1000)).toFixed(2) + "千万"
	}else if(value > 10000 * 100){
		return (value/(10000 * 100)).toFixed(2) + "百万"
	}else if(value > 10000 * 10){
		return (value/(10000 * 10)).toFixed(2) + "十万"
	}else if(value > 10000){
		return (value/10000).toFixed(2) + "万"
	}else if(value > 1000){
		return (value/1000).toFixed(2) + "千"
	}
	if(fixed){
		try{
			return value.toFixed(fixed);
		}catch(e){}
	}
	return value;
}

function doSendCmd(deviceId, data) {
	if (!isOnline(deviceId)) {
		showErrorMessage("车辆不在线，不能发送指令！");
	}else{
		var url = message_api_server_servlet_path + "/deviceDownMessage/" + deviceId + "/" + data.messageId + ".json";
		var result = ajaxSyncPost(url, data,true);
		if (result.code != 0) {
			showErrorMessage(result.message);
		}else{
			showMessage("指令已下发！");
			return result.data;
		}
	}
	return null;
}