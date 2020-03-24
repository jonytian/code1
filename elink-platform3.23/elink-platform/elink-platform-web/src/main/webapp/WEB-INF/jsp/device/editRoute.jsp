<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/header.jsp"%>
<title>编辑路段</title>
</head>
<body>
	<!--内容部分-->
	<div class="con1" id="content-div">
		<!--左侧地图-->
		<div class="left" id="route_left">
			<div id="map_box" style="margin-top:5px;width:100%;height:100%;"></div>
		</div>
		<!--右侧功能栏-->
		<div class="right" id="route_right">
			<div class="table_div">
				<table id="boot-strap-table" class="table_style" border="0"></table>
			</div>
			<p id="toolbar" class="table_but">
			    <a href="javascript:void(0)" onclick="add()"><i class="glyphicon glyphicon-save"></i>保存</a>
			    <a href="javascript:void(0)" onclick="showCommondDialog('import-road-dlg')"><i class="glyphicon glyphicon-road"></i>导入</a>
			    <a href="javascript:void(0)" onclick="draw()"><i class="glyphicon glyphicon-plus"></i>画路线</a>
				<a href="javascript:void(0)" onclick="addPoint()"><i class="glyphicon glyphicon-plus"></i>添加拐点</a>
				<a href="javascript:void(0)" onclick="preview()"><i class="glyphicon glyphicon-play"></i>预览</a>
				<a href="javascript:void(0)" onclick="dels()"><i class="glyphicon glyphicon-remove"></i>删除</a>
				<a href="javascript:void(0)" onclick="javascript:history.back(-1);"><i class="glyphicon glyphicon-share-alt"></i>返回</a>
			</p>
		</div>
	</div>
	
	<div id="edit-dlg" title="保存路段"  style="width:650px;height:420px;" class="simple-dialog">
		<form id="edit-frm">
			<input name="id" id="edit-frm-id" type="hidden" value="${param.id}">
			<div class="message_con">
				<label>路段名称：</label><input name="name" id="edit-frm-name" maxlength="16"  class="required" type="text" placeholder="填写路段名称"><span class="must">*</span>
			</div>
			<div class="message_con">
				<label>路段宽度：</label><input name="width" id="edit-frm-width" maxlength="3" value="100" class="required number" type="text" placeholder="单位为米（m）"><span class="must">*</span>
			</div>
			<div class="message_con">
				<label>路段属性：</label><input name="attribute" type="checkbox" value="0"><span style="color:#fff">行驶时间</span><input name="attribute" type="checkbox" value="1"><span style="color:#fff">限速</span><span class="must">*</span>
			</div>
			<div class="message_con">
				<label>路段行驶过长阈值：</label><input disabled name="itTravelTime" id="edit-frm-itTravelTime" maxlength="9"  class="number" type="text" placeholder="单位为秒（s）">
			</div>
			<div class="message_con">
				<label>路段行驶不足阈值 ：</label><input disabled name="gtTravelTime" id="edit-frm-gtTravelTime" maxlength="9"  class="number" type="text" placeholder="单位为秒（s）">
			</div>
			<div class="message_con">
				<label>路段最高速度  ：</label><input disabled name="maxSpeed" id="edit-frm-maxSpeed" maxlength="3" class="number" type="text" placeholder="单位为公里每小时（km/h）">
			</div>
			<div class="message_con">
				<label>路段超速持续时间 ：</label><input disabled name="durationTime" id="edit-frm-durationTime" maxlength="3" class="number" type="text" placeholder="单位为秒（s）">
			</div>
			<div class="message_footer">
				<button type="button" class="bule" onclick="save()">保存</button>
				<button type="button" class="orgen" onclick="closeDialog()">取消</button>
			</div>
		</form>
	</div>
	
	
	<div id="import-road-dlg" title="导入路线"  style="width:500px;height:150px;" class="simple-dialog">
		<form id="import-road-dlg-frm">
			<div class="message_con">
				<label>选择路线：</label><select name="lablePointId" id="import-road-dlg-frm-lablePointId" onchange="showRoad()" >
					<option value="">请选择路线</option>
				</select>
			</div>
			<div class="message_footer">
				<button type="button" class="bule" onclick="importRoad()">确定</button>
				<button type="button" class="orgen" onclick="closeDialog()">取消</button>
			</div>
		</form>
	</div>

	<%@ include file="/common/common.jsp"%>
	<%@ include file="../pub/mapScript.jsp"%>
	<script type="text/javascript" src="<c:url value='/js/device/editRoute.js'/>"></script>
</body>
</html>
