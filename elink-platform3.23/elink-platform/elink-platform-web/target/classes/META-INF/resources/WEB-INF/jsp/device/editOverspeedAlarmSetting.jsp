<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/header.jsp"%>
<title>超速告警设置</title>
<%@ include file="/common/common.jsp"%>
</head>
<body>
<div class="clear"></div>
<div class="con1">
   <form id="overspeed-alarm-setting-dlg-frm" method="post">
   			<input name="type" type="hidden" value="3">
            <div class="tab_con_div" style="display: block">
	            <div class="message_con">
					<label>围栏名称：</label><input name="name" maxlength="16" class="required" type="text"><span class="must">*</span>
				</div>
				<div class="message_con">
					<label>限速：</label><input name="speed"  maxlength="3"  type="text" class="required number" placeholder="超过此速度告警"><span class="must">*</span>
				</div>
				<div  class="message_con">
					<label>有效时段：</label><select name="startHour" id="startHour"  style="width:31%;">
					</select><label style="width:3%;">至</label><select name="endHour" id="endHour" style="width:31%;">
					</select>
				</div>
				<div class="message_con">
					<label>告警间隔：</label><select name="interval"  placeholder="间隔多久告警一次">
							  <option  value ="30">30秒</option>
							  <option  value ="60">60秒</option>
							  <option  value ="120">120秒</option>
					</select>
				</div>
				<div class="message_con">
					<label>开始时间：</label><input id="overspeed-alarm-setting-dlg-frm-startTime" name="startTime" type="text" readonly class="required date form_datetime" data-picker-position="top-right" data-date-format="yyyy-mm-dd hh:ii:00"><span class="must">*</span>
				</div>
				<div class="message_con">
					<label>结束时间：</label><input id="overspeed-alarm-setting-dlg-frm-endTime" name="endTime" type="text"  readonly class="required date form_datetime" data-picker-position="top-right" data-date-format="yyyy-mm-dd hh:ii:00"><span class="must">*</span>
				</div>
				<div class="message_footer">
					<button type="button" class="bule" onclick="showInfoTap(1,'overspeed-alarm-setting-dlg-frm')">下一步</button>
					<button type="button" class="orgen" onclick="javaScript:parent.closeIframe()">取消</button>
				</div>
            </div>

            <div class="tab_con_div" >
	             <ul id="group-tree"  class="ztree left_select_tree"></ul>
				 <div class="right_multiple_Select_Box">
				 	<%@ include file="../pub/multipleSelectBox.jsp"%>
				  </div>
				  <div class="message_footer">
				    <button type="button" class="orgen" onclick="showInfoTap(0,'overspeed-alarm-setting-dlg-frm')">上一步</button>
					<button type="button" class="bule" onclick="saveOverspeedAlarmSetting()">保存</button>
					<button type="button" class="orgen" onclick="javaScript:parent.closeIframe()">取消</button>
				  </div>
            </div>
    </form>
</div>
<script  type="text/javascript" src="<c:url value='/js/ztree/jquery.ztree.all.min.js'/>"></script>
<script  type="text/javascript" src="<c:url value='/js/device/editAlarmSetting.js'/>"></script>
</body>
</html>
<script language="javaScript">
$(function() {
	initLimitDatetimepicker("overspeed-alarm-setting-dlg-frm-startTime","overspeed-alarm-setting-dlg-frm-endTime",new Date(),"",0.5);
});
</script>