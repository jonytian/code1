<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/header.jsp"%>
<title>系统字典管理</title>
</head>
<body>
	<div class="clear"></div>
	<!--内容部分-->
	<div class="con1 left" id="content-div">
		<div class="right  con_table">
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
	<div id="edit-dlg" title="系统字典信息" class="simple-dialog" style="width: 550px; height: 350px;">
	<form id="edit-frm" method="post">
		<input name="id" id="edit-frm-id" type="hidden">
		<input name="type" id="edit-frm-type" type="hidden">
		<div class="message_con">
			<label>代码：</label><input name="code" id="edit-frm-code" maxlength="32" type="text" class="required"><span class="must">*</span>
		</div>
		<div class="message_con">
			<label>名称：</label><input name="remark" id="edit-frm-remark" maxlength="20" type="text" class="required"><span class="must">*</span>
		</div>
		<div class="message_con">
			<label  style="vertical-align:top;">内容：</label><textarea  name="content" id="edit-frm-content"  style="height:150px;"/></textarea>
		</div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="save()">保存</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>
	<script type="text/javascript" src="<c:url value='/js/dictionary/sysDictionaryList.js'/>"></script>
</body>
</html>