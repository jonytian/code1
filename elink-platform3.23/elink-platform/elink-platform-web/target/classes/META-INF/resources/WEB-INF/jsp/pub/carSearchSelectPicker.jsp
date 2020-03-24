<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<select name="deviceId" id="search-deviceId" class="required selectpicker" data-live-search="true"></select>
<script language="javaScript">
	var search_car_pageNo = 1;
	var search_car_pageSize = 10;
	var search_car_condition = {};
	var last_combobox_select_cache_key = "${sessionScope.user.userAccount}"+ "_last_combobox_select";
	var selectCarCache = new Cache("last_combobox_select_car_cache_key");
	
	
	$(document).ready(function() {
		$('#search-deviceId').selectpicker({
			noneSelectedText : '',
			noneResultsText : '',
			liveSearch : true,
			size : 5
		//设置select高度，同时显示5个值
		});
		$('#search-deviceId').selectpicker('setStyle', 'btn', 'remove');
		$('#search-deviceId').selectpicker('setStyle', 'selectpicker-btn', 'add');
		
		$('#search-deviceId').nextAll(".dropdown-menu").find(".form-control").on('input',function() {
			var keyword = $('#search-deviceId').nextAll(".dropdown-menu").find(".form-control").val();
			var q = $.trim(keyword) || "";
			if (!q&& q.length < 2) {
				return false;
			}
			if (typeof (getSearchCarCondition) == "function") {
				search_car_condition = getSearchCarCondition();
			}
			search_car_condition["plateNumber.like" ] = q;
			search_car_condition["state.gt"] = 0;
			loadMoreCars(search_car_condition);
		});
		
		if (typeof (getSearchCarCondition) == "function") {
			search_car_condition = getSearchCarCondition();
		}
		search_car_condition["state.gt"] = 0;
		loadMoreCars(search_car_condition);
	});

	function loadMoreCars(condition) {
		$('#search-deviceId').selectpicker('val', '');
		$('#search-deviceId').selectpicker('refresh');
		var url = management_api_server_servlet_path
				+ "/common/query/carDevice.json?select=deviceId,plateNumber&pageSize="
				+ search_car_pageSize + "&pageNo=" + search_car_pageNo;
		ajaxAsyncPost(url, condition, function(result) {
			if (result.code != 0) {
				showErrorMessage(result.message);
			}else{
				var data = result.data;
				if (data.length > 0) {
					var cache =  selectCarCache.get(last_combobox_select_cache_key);
					var cacheValue = "";
					if (cache) {
						cache = $.evalJSON(cache);
						cacheValue = cache.value;
					}
					var isSelect = false;
					var html = "<option value=''>请选择车辆</option>";
					for (var i = 0; i < data.length; i++) {
						var item = data[i];
						if (!isSelect && item[0] == cacheValue) {
							isSelect = true;
						}
						html += "<option value='"+item[0]+"'>" + item[1] + "</option>";
					}

					if (cacheValue && !isSelect) {
						html += "<option value='"+cache.value+"'>" + cache.text + "</option>";
					}
					
					$("#search-deviceId").html(html);
					$('#search-deviceId').selectpicker('refresh');//加载select框选择器
					
					if (cacheValue) {
						$('#search-deviceId').selectpicker('val', cacheValue); 
					}
					if(typeof(afterLoadSelectPicker)=="function"){
						afterLoadSelectPicker();
					}
				}else{
					$("#search-deviceId").html("");
					$('#search-deviceId').selectpicker('refresh');//加载select框选择器
				}
			}
		});
	}

	var query = false;
	function onClickSearch() {
		var deviceId = $("#search-deviceId").val();
		if (deviceId && deviceId != null) {
			var plateNumber = $("#search-deviceId").find("option:selected").text();
			cacheLastCarComboboxSelect(plateNumber, deviceId);
		}
		query = true;
		doQuery();
	}

	function cacheLastCarComboboxSelect(text, value) {
		if (!text || !value) {
			return;
		}
		var data = {
			text : text,
			value : value
		};
		selectCarCache.put(last_combobox_select_cache_key, $.toJSON(data));
	}
</script>