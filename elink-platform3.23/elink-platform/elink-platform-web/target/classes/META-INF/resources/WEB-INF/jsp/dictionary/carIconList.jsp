<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/header.jsp"%>
<link href="<c:url value='/js/uploader/JQuery.JSAjaxFileUploader.css'/>" rel="stylesheet" type="text/css" />
<title>车辆图库管理</title>
</head>
<body>
	<div class="clear"></div>
	<!--内容部分-->
	<div class="con1 left" id="content-div">
		<div class="right  con_table">
			<div class="table_find">
				<p>
					<label>车辆品牌：</label><select  id="search-brandType"></select>
					<label>车身颜色：</label><select  id="search-carColor"><option value="">请选择</option></select>
					&nbsp;&nbsp;
					<button type="button" class="btn btn-primary btn-sm" onclick="doQuery()">
						<span class="glyphicon glyphicon-search"></span>查询
					</button>
				</p>
			</div>
			<div class="table_div">
				<table id="boot-strap-table" class="table_style" border="0"></table>
			</div>
			<p id="toolbar" class="table_but">
				<a href="javascript:void(0)" onclick="add()"><i
					class="glyphicon glyphicon-plus"></i>新增</a><a
					href="javascript:void(0)" onclick="dels()"><i
					class="glyphicon glyphicon-remove"></i> 删除</a>
			</p>
		</div>
	</div>
	<%@ include file="/common/common.jsp"%>
	<div id="edit-dlg" title="车辆图库信息" class="simple-dialog" style="width:650px; height:300px;">
		<form id="edit-frm" method="post">
			<input name="id" id="edit-frm-id" type="hidden">
			<div class="message_con">
				<label>适用车辆品牌：</label><select name="brandType" id="edit-frm-brandType"></select>
			</div>
			<div class="message_con">
				<label>适用车辆型号：</label><select  name="brandModel"  id="edit-frm-brandModel"></select>
			</div>
			<div class="message_con">
				<label>适用车身颜色：</label><select name="carColor" id="edit-frm-carColor"></select>
			</div>
		</form>
		<div id="file_upload"></div>
	</div>
	<script type="text/javascript" src="<c:url value='/js/dictionary/carIconList.js'/>"></script>
	<script  type="text/javascript" src="<c:url value='/js/uploader/JQuery.JSAjaxFileUploader.js'/>"></script>
</body>
</html>