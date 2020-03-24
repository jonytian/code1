function generalAsyncPost(url, method, data, callback, showLoading) {
	$.ajax({
		type : "post",
		url : getContextPath() + "/common/" + method + ".do",
		data : $.toJSON({
			url : url,
			data : data
		}),
		contentType : "application/json; charset=utf-8",
		dataType : "json",
		success : callback,
		beforeSend : function() {
			if (showLoading) {
				startLoading();
			}
		},
		complete : function(data) {
			if (showLoading) {
				endLoading();
			}
		},
		error : function(data) {
			showErrorMessage(data.responseText);
		}
	});
}

function generalSyncPost(url, method, data, showLoading) {
	var result;
	$.ajax({
		type : "post",
		url : getContextPath() + "/common/" + method + ".do",
		data : $.toJSON({
			url : url,
			data : data
		}),
		contentType : "application/json; charset=utf-8",
		dataType : "json",
		async : false,
		success : function(data) {
			result = data;
		},
		beforeSend : function() {
			if (showLoading) {
				startLoading();
			}
		},
		complete : function(data) {
			if (showLoading) {
				endLoading();
			}
		},
		error : function(data) {
			showErrorMessage(data.responseText);
		}
	});
	return result;
}

function ajaxAsyncPost(url, data, callback, showLoading) {
	generalAsyncPost(url, "post", data, callback, showLoading);
}

function ajaxAsyncPut(url, data, callback, showLoading) {
	generalAsyncPost(url, "put", data, callback, showLoading);
}

function ajaxAsyncPatch(url, data, callback, showLoading) {
	generalAsyncPost(url, "patch", data, callback, showLoading);
}

function ajaxAsyncGet(url, data, callback, showLoading) {
	generalAsyncPost(url, "get", data, callback, showLoading);
}

function ajaxAsyncDel(url, data, callback, showLoading) {
	generalAsyncPost(url, "delete", data, callback, showLoading);
}

function ajaxSyncPost(url, data, showLoading) {
	return generalSyncPost(url, "post", data, showLoading);
}

function ajaxSyncPut(url, data, showLoading) {
	return generalSyncPost(url, "put", data, showLoading);
}

function ajaxSyncPatch(url, data, showLoading) {
	return generalSyncPost(url, "patch", data, showLoading);
}

function ajaxSyncGet(url, data, showLoading) {
	return generalSyncPost(url, "get", data, showLoading);
}

function ajaxSyncDel(url, data, showLoading) {
	return generalSyncPost(url, "delete", data, showLoading);
}
