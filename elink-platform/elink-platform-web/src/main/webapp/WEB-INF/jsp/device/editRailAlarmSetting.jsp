<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/header.jsp"%>
<title>标注点管理</title>
<%@ include file="/common/common.jsp"%>
</head>
<body>
<div class="clear"></div>
<div class="con1">
   <form id="rail-setting-dlg-frm" method="post">
            <div class="tab_con_div" style="display: block">
	            <div class="message_con">
					<label>围栏名称：</label><input name="name" maxlength="16" class="required" type="text"><span class="must">*</span>
				</div>
	            <div class="message_con">
					<label>电子围栏：</label><select id="rail-setting-dlg-frm-rail"></select>
				</div>
				<div class="message_con">
					<label>围栏类型：</label><select name="inside" id="inside" class="required" type="text">
							  <option  value ="0">出围栏告警</option>
							  <option  value ="1">进围栏告警</option>
							  <option  value ="2">围栏超速告警</option>
					</select>
				</div>
				<div class="message_con" id="speed-div" style="display:none">
					<label>围栏限速：</label><input name="speed"  maxlength="3" type="text"  placeholder="围栏内超过此速度告警"><span class="must">*</span>
				</div>
				<div  class="message_con">
					<label>有效星期：</label><input type="checkbox" name="week" value="1" >
					<label style="width:2%;">一</label>
					<input type="checkbox"   name="week" value="2" >
					<label style="width:2%;">二</label>
					<input type="checkbox"   name="week" value="3" >
					<label style="width:2%;">三</label>
					<input type="checkbox"   name="week" value="4" >
					<label style="width:2%;">四</label>
					<input type="checkbox"   name="week" value="5" >
					<label style="width:2%;">五</label>
					<input type="checkbox"   name="week" value="6" >
					<label style="width:2%;">六</label>
					<input type="checkbox"   name="week" value="0" >
					<label style="width:2%;">日</label><span class="must">*</span>
				</div>
				
				<div  class="message_con">
					<label>有效时段：</label><select name="startHour" id="startHour"  style="width:31%;">
					</select><label style="width:3%;">至</label><select name="endHour" id="endHour" style="width:31%;">
					</select>
				</div>
				<div class="message_con">
					<label>开始时间：</label><input id="rail-setting-dlg-frm-startTime" name="startTime" type="text" readonly class="required date form_datetime" data-picker-position="top-right" data-date-format="yyyy-mm-dd hh:ii:00"><span class="must">*</span>
				</div>
				<div class="message_con">
					<label>结束时间：</label><input id="rail-setting-dlg-frm-endTime" name="endTime" type="text"  readonly class="required date form_datetime" data-picker-position="top-right" data-date-format="yyyy-mm-dd hh:ii:00"><span class="must">*</span>
				</div>
				<div class="message_footer">
					<button type="button" class="bule" onclick="showInfoTap(1,'rail-setting-dlg-frm')">下一步</button>
					<button type="button" class="orgen" onclick="javaScript:parent.closeIframe()">取消</button>
				</div>
            </div>

            <div class="tab_con_div" >
	             <ul id="group-tree"  class="ztree left_select_tree"></ul>
				 <div class="right_multiple_Select_Box">
				 	<%@ include file="../pub/multipleSelectBox.jsp"%>
				  </div>
				  <div class="message_footer">
				    <button type="button" class="orgen" onclick="showInfoTap(0,'rail-setting-dlg-frm')">上一步</button>
					<button type="button" class="bule" onclick="saveRailAlarmSetting()">保存</button>
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
	loadRailData();
	initLimitDatetimepicker("rail-setting-dlg-frm-startTime","rail-setting-dlg-frm-endTime",new Date(),"",0.5);
});
</script>