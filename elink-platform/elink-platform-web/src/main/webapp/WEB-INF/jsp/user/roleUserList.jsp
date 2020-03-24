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

	<input name="roleId" id="search-frm-roleId" type="hidden">
	<input name="enterpriseId" id="search-frm-enterpriseId" type="hidden" value="">
	<input name="enterpriseName" id="search-frm-enterpriseName" type="hidden" value="">
	
	<%@ include file="/common/common.jsp"%>

	<div id="edit-dlg" title="用户" class="simple-dialog" style="width:550px; height:420px;">
		<form id="edit-frm" method="post">
			 <input name="roleId" id="edit-frm-roleId" type="hidden">
			<%@ include file="../pub/multipleSelectBox.jsp"%>
			 <div class="message_footer">
				<button type="button" class="bule" onclick="save()">保存</button>
				<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		     </div>
		</form>
	</div>

	<script  type="text/javascript" src="<c:url value='/js/ztree/jquery.ztree.all.min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/user/roleUserList.js'/>"></script>
</body>
</html>