<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/header.jsp"%>
<title>角色管理</title>
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
	
	<div id="edit-dlg" title="角色信息" class="simple-dialog" style="width:500px; height:350px;">
		<form id="edit-frm" method="post">
			<input name="id" id="edit-frm-id" type="hidden">
					<div class="message_con">
						<label>名称：</label><input name="name" id="edit-frm-name" maxlength="16" type="text"  class="required">
					</div>
					<div class="message_con">
						<label>代码：</label><input name="code" id="edit-frm-code" maxlength="8" type="text"  class="required">
					</div>
					<div class="message_con">
						<label  style="vertical-align:top;">备注：</label><textarea  name="remark" id="edit-frm-remark" maxlength="100" style="height:150px;"></textarea>
					</div>
		   <div class="message_footer">
				<button type="button" class="bule" onclick="save()">保存</button>
				<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		  </div>
		</form>
	</div>
	<script type="text/javascript" src="<c:url value='/js/user/roleList.js'/>"></script>
</body>
</html>