<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/header.jsp"%>
<title>元数据管理</title>
</head>
<body>
	<div class="clear"></div>
	<!--内容部分-->
	<div class="con1 left" id="content-div">
		<div class="right  con_table">
			<div class="table_find">
				<p>
					<label>代码:</label><input id="search-code" maxlength="20"  type="text" placeholder="请输入字典代码">
					<label>类型：</label><select id="search-type">
					  <option  value ="" >请选择</option>
					  <option  value ="1">实体对象</option> 
					  <option  value ="2">对象描述</option>
					  <option  value ="3">消息描述</option>
					  <option  value ="4">消息样例</option>
					</select>&nbsp;&nbsp;
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
	<div id="edit-dlg" title="元数据信息" class="simple-dialog" style="width:600px; height:450px;">
		<form id="edit-frm" method="post">
			<input name="id" id="edit-frm-id" type="hidden">
			<div class="message_con">
				<label>类型：</label><select name="type" id="edit-frm-type" class="required">
						  <option  value ="1">实体对象</option>
						  <option  value ="2">对象描述</option>
						  <option  value ="3">消息描述</option>
						  <option  value ="4">消息样例</option>
				</select></div><span class="must">*</span>
			<div class="message_con">
				<label>代码：</label><input name="code" id="edit-frm-code" maxlength="20" class="required"  type="text"><span class="must">*</span>
			</div>
			<div class="message_con">
				<label  style="vertical-align:top;">内容：</label><textarea  name="content" id="edit-frm-content"  class="required" style="height:250px;overflow:auto;" /></textarea><span class="must">*</span>
			</div>
			<div class="message_footer">
				<button type="button" class="bule" onclick="save()">保存</button>
				<button type="button" class="orgen" onclick="closeDialog()">取消</button>
			</div>
		</form>
	</div>
	<script type="text/javascript" src="<c:url value='/js/dictionary/metaDataList.js'/>"></script>
</body>
</html>