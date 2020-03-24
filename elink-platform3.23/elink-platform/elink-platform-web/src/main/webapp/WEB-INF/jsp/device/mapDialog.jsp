<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<div id="gps-history-query-dlg" title="历史轨迹查询" style="width:550px;height:400px;" class="simple-dialog">
	<form id="gps-history-query-dlg-frm">
		<div class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label><input id="gps-history-query-dlg-frm-name" readonly type="text">
		</div>
		<div class="message_con">
			<label>开始时间：</label><input name="startTime" type="text" id="gps-history-query-dlg-frm-startTime" readonly class="required date form_datetime" data-date-format="yyyy-mm-dd hh:00"><span class="must">*</span>
		</div>
		<div class="message_con">
			<label>结束时间：</label><input name="endTime" type="text" id="gps-history-query-dlg-frm-endTime" readonly class="required date form_datetime" data-date-format="yyyy-mm-dd hh:00"><span class="must">*</span>
		</div>
		<div class="message_con">
			<label>超速检测：</label><input name="overspeed" maxlength="3"  class="integer" placeholder="单位：km/h，超过此速度轨迹点标红色"  type="text">
		</div>
		<div class="message_con">
			<label>急加速检测：</label><input name="acceleration" maxlength="3"  class="integer" placeholder="单位：km/h，超过此加速度轨迹点标红色"  type="text">
		</div>
		<div class="message_con">
			<label>急刹车检测：</label><input name="deceleration" maxlength="3"  class="integer" placeholder="单位：km/h，超过此加速度轨迹点标红色"  type="text">
		</div>
		<div class="message_con">
			<!-- 高速变道急转弯是指在高速公路上以直行的速度，从一个车道行驶到其他车道；道路90°急转弯是指在普通公路的十字路口或丁字路口上以较高的直行速度，90°右转或左转；原地调头急转弯是指车辆以较高速度180°调转车头。 -->
			<label>急转弯检测：</label><input name="turningDirection" maxlength="3"  class="integer" placeholder="单位：km/h，以较高速度转弯或者调头轨迹点标红色"  type="text">
		</div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="doHistoryGpsQuery()">查询</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>

<div id="search-device-by-lablePoint-dlg" title="区域查车"  style="width:500px;height:150px;" class="simple-dialog">
	<form id="gps-history-query-dlg-frm">
		<div class="message_con">
			<label>选择区域：</label><select name="lablePointId" id="search-device-by-lablePoint-frm-lablePointId" onchange="onSelectSearchDeviceLablePoint()" ></select>
		</div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="searchDeviceByLablePoint()">查车</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>