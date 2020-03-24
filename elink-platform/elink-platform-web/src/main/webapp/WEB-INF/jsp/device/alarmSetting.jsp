<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<div id="move-alarm-setting-dlg" class="simple-dialog" title="原地设防" style="width:600px; height:360px;">
	<form id="move-alarm-setting-dlg-frm" method="post">
		<input name="type" type="hidden" value="1">
		<input name="deviceId" id="move-alarm-setting-dlg-frm-deviceId" type="hidden">
		<div  class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label><input name="deviceName" id="move-alarm-setting-dlg-frm-name" type="text">
		</div>
		<div  class="message_con">
			<label>围栏名称：</label><input name="name" maxlength="16"  class="required infoName"  type="text"><span class="must">*</span>
		</div>
		<div  class="message_con">
			<label>持续时间：</label><select name="duration">
					  <option  value ="10">10分钟</option>
					  <option  value ="15">15分钟</option>
					  <option  value ="30">30分钟</option>
			</select><span class="must">*</span>
		</div>
		<div  class="message_con">
			<label>告警间隔：</label><select name="interval">
					  <option  value ="30">30秒</option>
					  <option  value ="60">60秒</option>
					  <option  value ="120">120秒</option>
			</select><span class="must">*</span>
		</div>
		<div class="message_con">
			<label>开始时间：</label><input id="move-alarm-setting-dlg-frm-startTime" name="startTime" type="text" readonly class="required date form_datetime" data-date-format="yyyy-mm-dd hh:ii:00" data-picker-position="top-right" placeholder="设置起始时间"><span class="must">*</span>
		</div>
		<div class="message_con">
			<label>结束时间：</label><input id="move-alarm-setting-dlg-frm-endTime" name="endTime" type="text"  readonly class="required date form_datetime" data-date-format="yyyy-mm-dd hh:ii:00"  data-picker-position="top-right" placeholder="设置结束时间"><span class="must">*</span>
		</div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="saveMoveAlarmSetting()">保存</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>

<div id="rail-alarm-setting-dlg" class="simple-dialog" title="围栏告警设置" style="width:600px; height:450px;">
	<form id="rail-alarm-setting-dlg-frm" method="post">
		<input name="type" type="hidden" value="2">
		<input name="shape" id="rail-alarm-setting-dlg-frm-shape" type="hidden">
		<input name="path" id="rail-alarm-setting-dlg-frm-path"  type="hidden">
		<input name="radius" id="rail-alarm-setting-dlg-frm-radius" type="hidden">
		<input name="lat" id="rail-alarm-setting-dlg-frm-lat" type="hidden">
		<input name="lng" id="rail-alarm-setting-dlg-frm-lng" type="hidden">
		<input name="deviceId" id="rail-alarm-setting-dlg-frm-deviceId" type="hidden">
		<div  class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label><input name="deviceName" id="rail-alarm-setting-dlg-frm-name" maxlength="16" type="text">
		</div>
		<div  class="message_con">
			<label>围栏名称：</label><input name="name" maxlength="16" class="required infoName" type="text"><span class="must">*</span>
		</div>
		<div  class="message_con">
			<label>围栏类型：</label><select name="inside" id="rail-alarm-setting-dlg-frm-inside">
					  <option  value ="0">出围栏告警</option>
					  <option  value ="1">进围栏告警</option>
					  <option  value ="2">围栏超速告警</option>
			</select>
		</div>
		<div  class="message_con" id="rail-alarm-setting-dlg-frm-speed-div" style="display:none">
			<label>围栏限速：</label><input name="speed"  maxlength="3" type="text" placeholder="围栏内超过此速度告警"  class="integer"><span class="must">*</span>
		</div>
		<div  class="message_con">
			<label>有效星期：</label><input type="checkbox"   name="week" value="1" >
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
			<label style="width:2%;">日</label>
		</div>
		<div  class="message_con">
			<label>有效时段：</label><select name="startHour" id="rail-alarm-setting-dlg-frm-startHour"  style="width:31%;">
			</select><label style="width:3%;">至</label><select name="endHour" id="rail-alarm-setting-dlg-frm-endHour" style="width:31%;">
			</select>
		</div>
		<div class="message_con">
			<label>开始时间：</label><input id="rail-alarm-setting-dlg-frm-startTime" name="startTime" type="text" readonly class="required date form_datetime" data-date-format="yyyy-mm-dd hh:ii:00" data-picker-position="top-right" placeholder="设置起始时间"><span class="must">*</span>
		</div>
		<div class="message_con">
			<label>结束时间：</label><input id="rail-alarm-setting-dlg-frm-endTime" name="endTime" type="text"  readonly class="required date form_datetime" data-date-format="yyyy-mm-dd hh:ii:00"  data-picker-position="top-right" placeholder="设置结束时间"><span class="must">*</span>
		</div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="saveRailAlarmSetting()">保存</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>


<div id="district-alarm-setting-dlg" class="simple-dialog" title="围栏告警设置" style="width:650px; height:480px;">
	<form id="district-alarm-setting-dlg-frm" method="post">
		<input name="type" type="hidden" value="2">
		<input name="shape" id="district-alarm-setting-dlg-frm-shape" type="hidden" value="4">
		<input name="deviceId" id="district-alarm-setting-dlg-frm-deviceId" type="hidden">
		<div  class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label><input name="deviceName" id="district-alarm-setting-dlg-frm-name" maxlength="16" type="text">
		</div>
		<div  class="message_con">
			<label>围栏名称：</label><input name="name" maxlength="16" class="required infoName" type="text"><span class="must">*</span>
		</div>
		
		<div  class="message_con">
			<label>省份：</label><select id="district-alarm-setting-dlg-frm-province" style="width:20%;" onchange='searchAlarmDistrict(this)'></select>
			<label style="width:7%;">市区：</label><select id="district-alarm-setting-dlg-frm-city" style="width:15%;" onchange='searchAlarmDistrict(this)'></select>
			<label style="width:7%;">区域：</label><select id="district-alarm-setting-dlg-frm-district" style="width:15%;" onchange='searchAlarmDistrict(this)'></select>
		</div>
		
		<div  class="message_con">
			<label>围栏类型：</label><select name="inside" id="district-alarm-setting-dlg-frm-inside">
					  <option  value ="0">出围栏告警</option>
					  <option  value ="1">进围栏告警</option>
					  <option  value ="2">围栏超速告警</option>
			</select>
		</div>
		<div  class="message_con" id="district-alarm-setting-dlg-frm-speed-div" style="display:none">
			<label>围栏限速：</label><input name="speed"  maxlength="3" type="text" placeholder="围栏内超过此速度告警"  class="integer">
		</div>
		<div  class="message_con">
			<label>有效星期：</label><input type="checkbox"   name="week" value="1" >
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
			<label style="width:2%;">日</label>
		</div>
		<div  class="message_con">
			<label>有效时段：</label><select name="startHour" id="district-alarm-setting-dlg-frm-startHour" style="width:31%;">
			</select><label style="width:3%;">至</label><select name="endHour" id="district-alarm-setting-dlg-frm-endHour" style="width:31%;">
			</select>
		</div>
		<div class="message_con">
			<label>开始时间：</label><input id="district-alarm-setting-dlg-frm-startTime" name="startTime" type="text" readonly class="required date form_datetime" data-date-format="yyyy-mm-dd hh:ii:00" data-picker-position="top-right" placeholder="设置起始时间"><span class="must">*</span>
		</div>
		<div class="message_con">
			<label>结束时间：</label><input id="district-alarm-setting-dlg-frm-endTime" name="endTime" type="text"  readonly class="required date form_datetime" data-date-format="yyyy-mm-dd hh:ii:00"  data-picker-position="top-right" placeholder="设置结束时间"><span class="must">*</span>
		</div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="saveDistrictAlarmSetting()">保存</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>


<div id="overspeed-alarm-setting-dlg" class="simple-dialog" title="超速告警设置" style="width:500px; height:430px;">
	<form id="overspeed-alarm-setting-dlg-frm" method="post">
		<input name="type" type="hidden" value="3">
		<input name="deviceId" id="overspeed-alarm-setting-dlg-frm-deviceId" type="hidden">
		<div  class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label><input name="deviceName" id="overspeed-alarm-setting-dlg-frm-name" maxlength="16" type="text">
		</div>
		<div  class="message_con">
			<label>围栏名称：</label><input name="name" maxlength="16"  class="required infoName" type="text"><span class="must">*</span>
		</div>
		<div  class="message_con">
			<label>限速：</label><input name="speed"  maxlength="3" type="text" class="required integer" placeholder="超过此速度告警"><span class="must">*</span>
		</div>
		<div  class="message_con">
			<label>有效时段：</label><select name="startHour" id="overspeed-alarm-setting-dlg-frm-startHour" style="width:31%;">
			</select><label style="width:3%;">至</label><select name="endHour" id="overspeed-alarm-setting-dlg-frm-endHour" style="width:31%;">
			</select>
		</div>
		<div  class="message_con">
			<label>告警间隔：</label><select name="interval">
					  <option  value ="30">30秒</option>
					  <option  value ="60">60秒</option>
					  <option  value ="120">120秒</option>
			</select>
		</div>
		<div class="message_con">
			<label>开始时间：</label><input id="overspeed-alarm-setting-dlg-frm-startTime" name="startTime" type="text" readonly class="required date form_datetime" data-date-format="yyyy-mm-dd hh:ii:00" data-picker-position="top-right" placeholder="设置起始时间"><span class="must">*</span>
		</div>
		<div class="message_con">
			<label>结束时间：</label><input id="overspeed-alarm-setting-dlg-frm-endTime" name="endTime" type="text"  readonly class="required date form_datetime" data-date-format="yyyy-mm-dd hh:ii:00"  data-picker-position="top-right" placeholder="设置结束时间"><span class="must">*</span>
		</div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="saveOverspeedAlarmSetting()">保存</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>