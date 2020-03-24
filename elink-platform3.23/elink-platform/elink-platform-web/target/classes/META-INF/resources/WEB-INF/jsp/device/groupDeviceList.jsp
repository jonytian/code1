<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/header.jsp"%>
<link href="<c:url value='/js/uploader/JQuery.JSAjaxFileUploader.css'/>" rel="stylesheet" type="text/css" />
<title>设备管理</title>
</head>
<body>
	<div class="clear"></div>
	<!--内容部分-->
	<div class="con1 left" id="content-div">
		<div class="right  con_table">
			<div class="table_find">
				<p>
					<label>设备名称：</label><input id="search-name" maxlength="16"  type="text" placeholder="请输入设备名称" />
                    <label>设&nbsp;&nbsp;备&nbsp;&nbsp;ID：</label><input  id="search-simCode" maxlength="20"  type="text" placeholder="请输入设备ID" />
					<label>上线时间：</label><input id="search-startOnlineTime" style="width:13%;" type="text" readonly class="date form_date" data-date-format="yyyy-mm-dd">至<input id="search-endOnlineTime" style="width:13%;" type="text" readonly class="date form_date" data-date-format="yyyy-mm-dd">
				</p>
				<p>
				   <label>选择状态：</label><select id="search-state">
						<option value="">选择状态</option>
						<option value="0">未注册</option>
						<option value="1">已注册</option>
						<option value="2">离线</option>
						<option value="3">在线</option>
						<option value="4">已注销</option>
						<option value="5">已停用</option>
					</select>
					<label>协议版本：</label><select id="search-protocolVersion">
						<option value="">选择协议版本</option>
						<option  value ="2011">JT/T 808-2011</option>
					  	<option  value ="2013">JT/T 808-2013</option>
					  	<option  value ="2016">JT/T 808-2011+1078-2016</option>
					  	<option  value ="201602">JT/T 808-2013+1078-2016</option>
					  	<option  value ="2019">JT/T 808-2019</option>
					  	<option  value ="tjsatl">T/JSATL-2018（苏标）</option>
					</select>
					<label>下线时间：</label><input id="search-startOfflineTime" style="width:13%;" type="text" readonly class="date form_date" data-date-format="yyyy-mm-dd">至<input id="search-endOfflineTime" style="width:13%;" type="text" readonly class="date form_date" data-date-format="yyyy-mm-dd">
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
					class="glyphicon glyphicon-plus"></i>新增</a><a href="javascript:void(0)"
					onclick="importDevice()"><i class="glyphicon glyphicon-open"></i>导入</a><a href="javascript:void(0)"
					onclick="moveGroup()"><i class="glyphicon glyphicon-move"></i>转分组</a><a
					href="javascript:void(0)" onclick="dels()"><i
					class="glyphicon glyphicon-remove"></i> 删除</a>
			</p>
		</div>
	</div>

	<div id="edit-dlg-move-group" title="转分组" class="simple-dialog" style="width:500px; height: 350px;">
		<div style="height:250px;margin-left:170px;"><ul id="group-tree"  class="ztree"></ul></div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="saveMoveGroup()">保存</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</div>

	<input name="groupId" id="search-frm-groupId" type="hidden">
	<input name="enterpriseId" id="search-frm-enterpriseId" type="hidden">
	
	<%@ include file="/common/common.jsp"%>
	<%@ include file="edit.jsp"%>
	<script src="<c:url value='/js/uploader/JQuery.JSAjaxFileUploader.js'/>"></script>
	<script  type="text/javascript" src="<c:url value='/js/ztree/jquery.ztree.all.min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/device/groupDeviceList.js?v=20191015'/>"></script>
</body>
</html>
