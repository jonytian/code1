<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/header.jsp"%>
<title>外部接口管理</title>
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
	<div id="edit-dlg" title="接口信息" class="simple-dialog" style="width:650px; height:480px;">
	<form id="edit-frm" method="post">
		<input name="id" id="edit-frm-id" type="hidden">
		<input name="type" id="edit-frm-type" type="hidden" value="99">
		<div class="message_con">
			  <label>接口标识：</label><input name="code" id="edit-frm-code" maxlength="16" type="text" class="required"><span class="must">*</span>
		</div>
		<div class="message_con">
			  <label>接口名称：</label><input name="name" id="edit-frm-name" maxlength="16" type="text" class="required"><span class="must">*</span>
		</div>
		<div class="message_con">
			   <label>请求URL：</label><input name="url" id="edit-frm-url" maxlength="128" type="text" class="required"><span class="must">*</span>
		</div>
		<div class="message_con">
			<label>请求方法：</label><select name="method" id="edit-frm-method" >
					  <option  value ="GET">get</option>
					  <option  value ="POST">post</option>
					  <option  value ="PUT">put</option>
					  <option  value ="PATCH">patch</option>
					  <option  value ="DELETE">delete</option>
				</select>
		</div>
		<div class="message_con">
			<label  style="vertical-align:top;">备&nbsp;&nbsp;注：</label><textarea  name="remark" id="edit-frm-remark"  maxlength="50"  style="height:150px;"/></textarea>
		</div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="save()">保存</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
			</div>
		</form>
	</div>
	<script type="text/javascript" src="<c:url value='/js/dictionary/apiList.js'/>"></script>
</body>
</html>