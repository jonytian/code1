<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/header.jsp"%>
<title>视频网关管理</title>
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
	<div id="edit-dlg" title="视频网关信息" class="simple-dialog" style="width:650px; height:480px;">
	<form id="edit-frm" method="post">
		<input name="id" id="edit-frm-id" type="hidden">
		<input name="type" id="edit-frm-type" type="hidden" value="99">
		<div class="message_con">
			<label>网关类型：</label><select name="code">
					  <option  value ="1">实时视频</option>
					  <option  value ="2">历史视频</option>
				</select>
		</div>
		<div class="message_con">
			<label>网关外网ip：</label><input name="ip" id="edit-frm-ip" maxlength="32"  class="required"  type="text"><span class="must">*</span>
		</div>
		<div class="message_con">
			<label>udp端口：</label><input name="udpPort" id="edit-frm-udpPort" maxlength="5"  class="required number"  type="text"><span class="must">*</span>
		</div>
		<div class="message_con">
			<label>tcp端口：</label><input name="tcpPort" id="edit-frm-tcpPort" maxlength="5"   class="required number"   type="text"><span class="must">*</span>
		</div>
		<div class="message_con">
			<label>允许连接数：</label><input name="limit" id="edit-frm-limit" maxlength="5"   class="required number"  type="text"><span class="must">*</span>
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
	<script type="text/javascript" src="<c:url value='/js/dictionary/videoGatewayList.js'/>"></script>
</body>
</html>