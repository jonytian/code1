<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<div class="selectbox">
	<div class="select-bar">
		<select multiple="multiple" id="multiple-select-source"></select>
	</div>

	<div class="select-btn-bar">
		<p>
			<span id="select-add"><input type="button" class="select-btn"
				value=">" title="移动选择项到右侧" /></span>
		</p>
		<p>
			<span id="select-add-all"><input type="button"
				class="select-btn" value=">>" title="全部移到右侧" /></span>
		</p>
		<p>
			<span id="select-remove"><input type="button"
				class="select-btn" value="<" title=" 移动选择项到左侧"/></span>
		</p>
		<p>
			<span id="select-remove-all"><input type="button" class="select-btn" value="<<" title=" 全部移到左侧"/></span>
		</p>
	</div>

	<div class="select-bar">
		<select multiple="multiple" id="multiple-selected-target"></select>
	</div>
</div>

<script language="javaScript">
	$(function() {
		//移到右边
		$('#select-add').click(function() {
					//先判断是否有选中
					if (!$("#multiple-select-source option").is(":selected")) {
						showMessage("请选择需要移动的选项")
					}
					//获取选中的选项，删除并追加给对方
					else {
						$('#multiple-select-source option:selected').appendTo('#multiple-selected-target');
						resetSelect('multiple-selected-target');
					}
				});

		//移到左边
		$('#select-remove').click(function() {
							//先判断是否有选中
							if (!$("#multiple-selected-target option").is(":selected")) {
								showMessage("请选择需要移动的选项")
							} else {
								$('#multiple-selected-target option:selected').appendTo('#multiple-select-source');
								resetSelect('multiple-select-source');
							}
						});

		//全部移到右边
		$('#select-add-all').click(function() {
					//获取全部的选项,删除并追加给对方
					$('#multiple-select-source option').appendTo('#multiple-selected-target');
					resetSelect('multiple-selected-target');
				});

		//全部移到左边
		$('#select-remove-all').click(function() {
					$('#multiple-selected-target option').appendTo('#multiple-select-source');
					resetSelect('multiple-select-source');
				});

		//双击选项
		$('#multiple-select-source').dblclick(function() { //绑定双击事件
			//获取全部的选项,删除并追加给对方
			$("option:selected", this).appendTo('#multiple-selected-target'); //追加给对方
			resetSelect('multiple-selected-target');
		});

		//双击选项
		$('#multiple-selected-target').dblclick(function() {
			$("option:selected", this).appendTo('#multiple-select-source');
			resetSelect('multiple-select-source');
		});

		$("#multiple-select-source").scroll(function() {
			if (typeof (onScrollMultipleSelectSource) == "function") {
				onScrollMultipleSelectSource();
			}
		});
	});
	
	function resetSelect(selectId){
		var map = new JsMap();
		$("#"+selectId+" option").map(function(){
		     var id = $(this).val();
		     var name = $(this).text();
		     map.put(id,name);
		});

		var keys = map.getKeys();
		$('#'+selectId).empty();
		for(var i=0,l=keys.length;i<l;i++){
			var key = keys[i];
			$("#"+selectId).append("<option value='"+key+"'>"+map.get(key)+"</option>");
		}
	}
</script>