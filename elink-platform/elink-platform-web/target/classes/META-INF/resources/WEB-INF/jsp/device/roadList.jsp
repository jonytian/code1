<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/header.jsp"%>
<%@ include file="/common/common.jsp"%>
<title>路线管理</title>
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
				<a href="javascript:void(0)" onclick="add()"><i class="glyphicon glyphicon-plus"></i>新增</a>
				<a href="javascript:void(0)" onclick="bindCar()"><i class="glyphicon glyphicon-link"></i>绑定车辆</a>
				<a href="javascript:void(0)" onclick="dels()"><i class="glyphicon glyphicon-remove"></i>删除</a>
			</p>
		</div>
	</div>

	<div id="edit-dlg" title="路线"  style="width:650px;height:420px;" class="simple-dialog">
		<form id="edit-frm">
		  <div class="tab_con_div" style="display: block">
			<input name="id" id="edit-frm-id" type="hidden">
			<div class="message_con">
				<label>路线名称：</label><input name="name" id="edit-frm-name" maxlength="16"  class="required" type="text" placeholder="填写路段名称"><span class="must">*</span>
			</div>
			<div class="message_con">
				<label style="vertical-align:top;">告警设置：</label><div style="border:none;color:#fff;">
				 <input name="attribute" type="checkbox" class="required" value="2">进路线报警给驾驶员
				 <input name="attribute" type="checkbox" class="required" value="3">进路线报警给平台<br/>
				 <input name="attribute" type="checkbox" class="required" value="4">出路线报警给驾驶员
				 <input name="attribute" type="checkbox" class="required" value="5">出路线报警给平台 <span class="must">*</span>
			    </div>
			</div>
			<div class="message_con">
				<label>时间属性：</label><div style="border:none;color:#fff;"><input name="timeAttr" type="checkbox"  value="0">起始时间<input name="timeAttr" type="checkbox" value="1">固定时间段</div>
			</div>
			<div id="start-end-time-div" style="display:none;">
				<div class="message_con">
					<label>有效时段：</label><input type="time"  style="width:31.5%;" name="startTime" id="edit-frm-startTime" > - 
					<input  type="time"  style="width:31.5%;" name="endTime" id="edit-frm-endTime"><span class="must">*</span>
				</div>
			</div>
			<div id="start-end-datetime-div">
				<div class="message_con">
					<label>开始时间：</label><input id="edit-frm-startDateTime" name="startDateTime" type="text"  class="date form_datetime" data-date-format="yyyy-mm-dd hh:ii:ss" data-picker-position="top-right" placeholder="设置起始时间"><span class="must">*</span>
				</div>
				<div class="message_con">
					<label>结束时间：</label><input id="edit-frm-endDateTime"  name="endDateTime" type="text"  class="date form_datetime" data-date-format="yyyy-mm-dd hh:ii:ss"  data-picker-position="top-right" placeholder="设置结束时间"><span class="must">*</span>
				</div>
			</div>
			<div class="message_footer">
				<button type="button" class="bule" onclick="showNext()">下一步</button>
				<button type="button" class="orgen" onclick="closeDialog()">取消</button>
			</div>
			</div>
			<div class="tab_con_div" >
			 	 <%@ include file="../pub/multipleSelectBox.jsp"%>
				  <div class="message_footer">
				    <button type="button" class="orgen" onclick="showInfoTap(0,'edit-frm')">上一步</button>
					<button type="button" class="bule" onclick="save()">保存</button>
					<button type="button" class="orgen" onclick="closeDialog()">取消</button>
				  </div>
            </div>
		</form>
	</div>
	<script type="text/javascript" src="<c:url value='/js/device/roadList.js'/>"></script>
</body>
</html>