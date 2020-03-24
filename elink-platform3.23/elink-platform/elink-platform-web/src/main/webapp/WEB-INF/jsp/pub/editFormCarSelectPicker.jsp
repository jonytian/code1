<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<select name="deviceId" id="edit-frm-deviceId" class="required selectpicker" data-live-search="true"></select>
<script language="javaScript">
	var edit_frm_car_pageNo = 1;
	var edit_frm_car_pageSize = 10;
	var edit_frm_car_condition = {};

	$(document).ready(function() {
		$('#edit-frm-deviceId').selectpicker({
			noneSelectedText : '',
			noneResultsText : '',
			liveSearch : true,
			size : 5
		//设置select高度，同时显示5个值
		});
		$('#edit-frm-deviceId').selectpicker('setStyle', 'btn', 'remove');
		$('#edit-frm-deviceId').selectpicker('setStyle', 'selectpicker-btn', 'add');
		
		$('#edit-frm-deviceId').nextAll(".dropdown-menu").find(".form-control").on('input',function() {
			var keyword = $('#edit-frm-deviceId').nextAll(".dropdown-menu").find(".form-control").val();
			var q = $.trim(keyword) || "";
			if (!q && q.length < 2) {
				return false;
			}
			if (typeof (getEditSearchCarCondition) == "function") {
				edit_frm_car_condition = getEditSearchCarCondition();
			}
			edit_frm_car_condition["plateNumber.like" ] = q;
			edit_frm_car_condition["state.gt"] = 0;
			loadMoreEditSelectCars(edit_frm_car_condition);
		});
		
		if (typeof (getEditSearchCarCondition) == "function") {
			edit_frm_car_condition = getEditSearchCarCondition();
		}
		edit_frm_car_condition["state.gt"] = 0;
		loadMoreEditSelectCars(edit_frm_car_condition);
	});

	function loadMoreEditSelectCars(condition) {
		$('#edit-frm-deviceId').selectpicker('val', '');
		$('#edit-frm-deviceId').selectpicker('refresh');
		var url = management_api_server_servlet_path + "/common/query/carDevice.json?select=deviceId,plateNumber&pageSize=" + edit_frm_car_pageSize + "&pageNo=" + edit_frm_car_pageNo;
		ajaxAsyncPost(url, condition, function(result) {
			if (result.code != 0) {
				showErrorMessage(result.message);
			}else{
				var data = result.data;
				if (data.length > 0) {
					var html = "<option value=''>请选择车辆</option>";
					for (var i = 0; i < data.length; i++) {
						var item = data[i];
						html += "<option value='"+item[0]+"'>" + item[1] + "</option>";
					}
					
					$("#edit-frm-deviceId").html(html);
					$('#edit-frm-deviceId').selectpicker('refresh');//加载select框选择器
				}else{
					$("#edit-frm-deviceId").html("");
					$('#edit-frm-deviceId').selectpicker('refresh');//加载select框选择器
				}
			}
		});
	}
</script>