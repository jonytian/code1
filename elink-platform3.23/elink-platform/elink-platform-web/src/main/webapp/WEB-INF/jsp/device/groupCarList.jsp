<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/header.jsp"%>
<title>公务用车管理</title>
</head>
<body>
	<div class="clear"></div>
	<!--内容部分-->
	<div class="con1 left" id="content-div">
		<div class="right  con_table">
			<div class="table_find">
				<p>
					<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label><input id="search-plateNumber" type="text" placeholder="请输入车牌号" /> 
					<label>车辆类型：</label><select id="search-type"></select>&nbsp;&nbsp;
					<button type="button" class="btn btn-primary btn-sm" onclick="doQuery()">
						<span class="glyphicon glyphicon-search"></span>查询
					</button>
				</p>
			</div>
			<div class="table_div">
				<table id="boot-strap-table" class="table_style" border="0"></table>
			</div>
			<p id="toolbar" class="table_but">
				<a href="javascript:void(0)" onclick="importCar()"><i class="glyphicon glyphicon-plus"></i>导入</a>
				<a href="javascript:void(0)" onclick="add()"><i class="glyphicon glyphicon-plus"></i>新增</a>
				<a href="javascript:void(0)" onclick="moveGroup()"><i class="glyphicon glyphicon-move"></i>转分组</a>
				<a href="javascript:void(0)" onclick="dels()"><i class="glyphicon glyphicon-remove"></i>删除</a>
			</p>
		</div>
	</div>
	
	<input name="groupId" id="search-frm-groupId" type="hidden">
	<input name="enterpriseId" id="search-frm-enterpriseId" type="hidden">
	
	<%@ include file="/common/common.jsp"%>

	<div id="officers-car-dlg" class="simple-dialog" title="导入车辆" style="width:750px; height: 450px;">
		<form id="officers-car-dlg-frm" method="post">
			<ul id="device-group-tree" class="ztree left_select_tree"></ul>
			<div class="right_multiple_Select_Box">
				<%@ include file="../pub/multipleSelectBox.jsp"%>
			</div>
			<div class="message_footer">
				<button type="button" class="bule" onclick="saveOfficersCar()">保存</button>
				<button type="button" class="orgen" onclick="closeDialog()">取消</button>
			</div>
		</form>
	</div>

	<div id="edit-dlg-move-group" title="转分组" class="simple-dialog" style="width:500px; height: 350px;">
		<div style="height:250px;margin-left:170px;overflow:auto;margin-bottom:-12px;"><ul id="car-group-tree"  class="ztree"></ul></div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="saveMoveGroup()">保存</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</div>

	<script  type="text/javascript" src="<c:url value='/js/ztree/jquery.ztree.all.min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/device/groupCarList.js'/>"></script>
</body>
</html>
