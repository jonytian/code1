var MAP_TYPE = 1, MAP_DIV = "map_box";

$(function() {
	initRoadLabelPointSetting();
	
	$("input[name='attribute']").change(function(){
			if($(this).val()==1){
				if($(this).is(":checked")){
					$("#edit-frm-maxSpeed").attr("disabled",false);
					$("#edit-frm-durationTime").attr("disabled",false);
					$("#edit-frm-maxSpeed").addClass("required");
					$("#edit-frm-durationTime").addClass("required");
				}else{
					$("#edit-frm-maxSpeed").attr("disabled",true);
					$("#edit-frm-durationTime").attr("disabled",true);
					$("#edit-frm-maxSpeed").removeClass("required");
					$("#edit-frm-durationTime").removeClass("required");
				}
			}else{
				if($(this).is(":checked")){
					$("#edit-frm-itTravelTime").attr("disabled",false);
					$("#edit-frm-gtTravelTime").attr("disabled",false);
					$("#edit-frm-itTravelTime").addClass("required");
					$("#edit-frm-gtTravelTime").addClass("required");
					
				}else{
					$("#edit-frm-itTravelTime").attr("disabled",true);
					$("#edit-frm-gtTravelTime").attr("disabled",true);
					$("#edit-frm-itTravelTime").removeClass("required");
					$("#edit-frm-gtTravelTime").removeClass("required");
				}
			}
	});
	
	var id = $("#edit-frm-id").val();
	if(id){
		loadEditFrmData(id);
	}else{
		initBootstrapTable();
	}
});

function loadEditFrmDataSuccessHandler(data){
	var points = (data.path).split(";");
	var list = [];
	for(var i=0;i<points.length;i++){
		var point = points[i].split(",");
		list.push({id:i,lng:point[0],lat:point[1]});
	}
	initBootstrapTable(list);
	
	var attribute = data.attribute;
	if((((attribute&(1<<0))>>0)==1)) {
		$("#edit-frm-itTravelTime").attr("disabled",false);
		$("#edit-frm-gtTravelTime").attr("disabled",false);
		$("#edit-frm-itTravelTime").addClass("required");
		$("#edit-frm-gtTravelTime").addClass("required");
		$("input[name='attribute'][value='0']").prop("checked",true);
	}
	if((((attribute&(1<<1))>>1)==1)) {
		$("#edit-frm-maxSpeed").attr("disabled",false);
		$("#edit-frm-durationTime").attr("disabled",false);
		$("#edit-frm-maxSpeed").addClass("required");
		$("#edit-frm-durationTime").addClass("required");
		$("input[name='attribute'][value='1']").prop("checked",true);
	}
}

function getApiName() {
	return "route";
}

function initBootstrapTable(data){
	if(!data){
		data = [];
	}
	var columns = getColumns();
	$('#boot-strap-table').bootstrapTable("destroy");
	$('#boot-strap-table').bootstrapTable({
		clickEdit:true,
		pagination : true, // 分页
		pageSize : 10,
		pageNumber : 1,
		toolbar:"#toolbar",
		idField:"id",
		columns:columns,
		data:data, 
		onClickCell:function(field, value, row, $element) {
	        if(field=='opear'){
	        	return;
	        }
			$element.attr('contenteditable', true);
	        $element.blur(function() {
	            let index = $element.parent().data('index');
	            let value = $element.html();
	            if(!value){
	            	showMessage("请输入正确的值！");
	            	return;
	            }
	            $('#boot-strap-table').bootstrapTable('updateCell', {
	                index: index,//行索引
	                field: field,//列名
	                value: value//cell值
	            });
	        });
	  }
	});
}

function initRoadLabelPointSetting(){
	var url = management_api_server_servlet_path+"/common/query/labelPoint.json?select=id,name&countable=false&pageSize=1000&pageNo=1&orderBy=createTime&desc=true";
	var data = {};
	data["bizType.eq"]=6;
	data["type.eq"]=5;
	ajaxAsyncPost(url,data,function(result){
		if (result.code!=0) {
			showErrorMessage(result.message);
			return;
		}
		var rows = result.data;
		//$("#import-road-dlg-frm-lablePointId").empty(); 
		for(var i=0;i<rows.length;i++){
			var item = rows[i];
			$("#import-road-dlg-frm-lablePointId").append("<option value='"+item[0]+"'>"+item[1]+"</option>");
		}
	});
}

function addPoint(){
	var rows = $('#boot-strap-table').bootstrapTable('getData');
	var index = rows.length;
	$('#boot-strap-table').bootstrapTable('insertRow', {
        index:index,
        row: {
          id: index,
          lng:'',
          lat:'',
        }
     });
	
	while(true){
		var options = $('#boot-strap-table').bootstrapTable('getOptions')
		if(options.pageNumber < options.totalPages){
		  $('#boot-strap-table').bootstrapTable('nextPage');
		}else{
			break;
		}
	}
}

function add(){
	var rows = $('#boot-strap-table').bootstrapTable('getData');
	if(rows.length > 0){
		showCommondDialog('edit-dlg');
	}else{
		showMessage("请先设置路线！");
	}
}

function preview(){
	var rows = $('#boot-strap-table').bootstrapTable('getData');
	var list = [];
	for(var i=0;i<rows.length;i++){
		var row = rows[i];
		list.push({lng:row.lng,lat:row.lat});
	}
	drawPolyline(list);
}

function draw(){
	mapUtil.clearOverlays();
	// 地图上画轨迹
	mapUtil.drawLine(function(e, line){
			line=e.obj;
			var path=line.getPath();
			// 国测局坐标转wgs84坐标
			var list = [];
			for(var i=0;i<path.length;i++){
				var point = path[i];
				var wgs84 = LngLatConverter.gcj02towgs84(point.getLng(), point.getLat());
				list.push({id:i,lng:wgs84[0].toFixed(6),lat:wgs84[1].toFixed(6)});
			}
			initBootstrapTable(list);
			mapUtil.closeDraw();
	});
}

function showRoad(){
	var labelPointId = $("select[name='lablePointId'] option:selected").val();
	if(!labelPointId){
		return;
	}
	var labelPoint = getDeviceLabelPoint(labelPointId);
	var points = (labelPoint.setting.route).split(";");
	var list = [];
	for(var i=0;i<points.length;i++){
		var point = points[i].split(",");
		list.push({lng:point[0],lat:point[1]});
	}
	drawPolyline(list);
}

function importRoad(){
	var labelPointId = $("select[name='lablePointId'] option:selected").val();
	if(!labelPointId){
		showMessage("请选择路线！");
		return;
	}
	closeDialog();
	var labelPoint = getDeviceLabelPoint(labelPointId);
	var points = (labelPoint.setting.route).split(";");
	var list = [];
	for(var i=0;i<points.length;i++){
		var point = points[i].split(",");
		list.push({id:i,lng:point[0],lat:point[1]});
	}
	initBootstrapTable(list);
}

function getDeviceLabelPoint(labelPointId){
	var url=management_api_server_servlet_path+"/common/labelPoint/"+labelPointId+".json";
	var result = ajaxSyncGet(url,{});
	if (result.code!=0) {
		showErrorMessage(result.message);
		return;
	}
	var labelPoint = result.data;
	var labelPointSetting = JSON.parse(labelPoint.setting);
	labelPoint.setting = labelPointSetting;
	return labelPoint;
}

function drawPolyline(gpsList) {
	mapUtil.clearOverlays();
	// 地图上画轨迹
	var map = mapUtil.getMap();
	var points = new Array();
	var index = 0;
	for (var i = 0, l = gpsList.length; i < l; i++) {
		var item = gpsList[i];
		var gcj02 = LngLatConverter.wgs84togcj02(item.lng, item.lat);
		var point = mapUtil.getPoint(gcj02[0], gcj02[1]);
		points[index++] = point;
		 var marker = new AMap.Marker({
	         position: point
	     });
	     marker.setMap(mapUtil.getMap());
	}

	if (points.length > 0) {
		map.panTo(points[0]);
		mapUtil.drawPolyline(points, "red");
	}
}

function getColumns() {
	return [ {
		field : 'check',
		checkbox : true
	}, {
		field : "lng",
		title : "经度"
	}, {
		field : "lat",
		title : "纬度"
	}, {
		title : '操作',
		field : 'opear',
		formatter : function(value, row) {
			var html = '';
			html +='<a href="javascript:void(0)" class="table_del" title="删除" onclick="del('+row.id+')">删除</a>';
			html +='<a href="javascript:void(0)" class="table_view" title="查看" onclick="view('+row.lng+','+row.lat+')">查看</a>';
			return html;
		}
	}];
}

function del(id){
	var ids = [];
	ids.push(id);
	$('#boot-strap-table').bootstrapTable('remove', {
	        field: 'id',
	        values: ids
	});
}

function dels(){
	var ids = $.map($('#boot-strap-table').bootstrapTable('getSelections'), function (row) {
        return row.id
      });
    $('#boot-strap-table').bootstrapTable('remove', {
        field: 'id',
        values: ids
    });
}

function view(lng,lat){
	mapUtil.clearOverlays();
	var gcj02 = LngLatConverter.wgs84togcj02(lng, lat);
	var point = mapUtil.getPoint(gcj02[0], gcj02[1]);
	var marker = new AMap.Marker({
         position: point
    });
	var map = mapUtil.getMap();
    marker.setMap(map);
    map.panTo(point);
}

function save() {
	var formId = "edit-frm";
	if (!validForm(formId)) {
		return;
	}
	var data = $("#" + formId).serializeObject();
	var rows = $('#boot-strap-table').bootstrapTable('getData');
	var path = "";
	for(var i=0;i<rows.length;i++){
		var row = rows[i];
		path+=";"+row.lng+","+row.lat;
	}
	
	var attribute="";
	$("input[name='attribute']").each(function (index, item) {
		 if($(this).is(":checked")){
			attribute="1"+attribute;
		}else{
			attribute="0"+attribute;
		}
	});

	data.attribute=parseInt(attribute,2);
	data.path=path.substr(1);
	var url = getSaveUrl(data);
	if (data.id) {
		ajaxAsyncPatch(url, data, function(result){
			if (result.code != 0) {
				showErrorMessage(result.message);
			} else {
				history.back(-1);
			}
		}, true);
	} else {
		ajaxAsyncPost(url, data, function(result){
			if (result.code != 0) {
				showErrorMessage(result.message);
			} else {
				history.back(-1);
			}
		}, true);
	}
}