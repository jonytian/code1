<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/header.jsp"%>
<title>子公司管理</title>
</head>
<body>
	<div class="clear"></div>
	<input  id="search-frm-deptId" type="hidden">
	<input  id="search-frm-deptName" type="hidden">
	<input  id="search-frm-orgId" type="hidden">
	<input  id="search-frm-orgName" type="hidden">
	<input  id="search-frm-enterpriseId" type="hidden">
	<input  id="search-frm-enterpriseName" type="hidden">
	<!--内容部分-->
	<div class="con1 left" id="content-div">
		<div class="right  con_table">
			<div class="table_div">
				<table id="boot-strap-table" class="table_style" border="0"></table>
			</div>
			<p id="toolbar" class="table_but">
				<a href="javascript:void(0)" onclick="add()"><i class="glyphicon glyphicon-plus"></i>新增</a>
				<a href="javascript:void(0)" onclick="dels()"><i class="glyphicon glyphicon-remove"></i> 删除</a>
			</p>
		</div>
	</div>
	<%@ include file="/common/common.jsp"%>
	
	<div id="edit-dlg" title="账号管理" class="simple-dialog" style="width:650px; height: 320px;">
		<form id="edit-frm" method="post">
			<input name="id" id="edit-frm-id" type="hidden">
			<input name="enterpriseId" id="edit-frm-enterpriseId" type="hidden">
			<div class="message_con">
				<label>名称：</label><input name="name" id="edit-frm-name" type="text" maxlength="64" class="required"><span class="must">*</span>
			</div>
			<div class="message_con">
				<label>简称：</label><input name="shortName" id="edit-frm-shortName" type="text" maxlength="16" class="required"><span class="must">*</span>
			</div>
			<div class="message_con">
				<label>联系人：</label><input name="contact" id="edit-frm-contact" type="text" maxlength="16">
			</div>
			<div class="message_con">
				<label>电话：</label><input name="tel" id="edit-frm-tel" type="text" maxlength="16">
			</div>
			<div class="message_con">
				<label>地址：</label><input name="address" id="edit-frm-address" type="text" maxlength="128" >
			</div>
			<div class="message_footer">
				<button type="button" class="bule" onclick="save()">保存</button>
				<button type="button" class="orgen" onclick="closeDialog()">取消</button>
			</div>
		</form>
	</div>

	<script type="text/javascript" src="<c:url value='/js/user/subsidiaryList.js'/>"></script>
	</body>
</html>