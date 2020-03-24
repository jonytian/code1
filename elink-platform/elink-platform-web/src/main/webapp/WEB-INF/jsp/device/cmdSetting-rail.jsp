<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<div id="send-set-rail-circle-cmd-dlg" class="simple-dialog" title="设置圆形区域" style="width:650px;height:500px;">
	<form id="send-set-rail-circle-cmd-dlg-frm" method="post">
		<input name="deviceId" id="send-set-rail-circle-cmd-dlg-frm-deviceId" type="hidden">
		<input name="messageId" type="hidden" value="8600">
		<div class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label><input name="devieName" readonly id="send-set-rail-circle-cmd-dlg-frm-name" type="text">
		</div>
		<div class="message_con">
			<label>圆形区域：</label><select name="labelPointId" id="send-set-rail-circle-cmd-dlg-frm-rail" class="required">
			</select>
		</div>
		<div class="message_con">
			<label>围栏名称：</label><input name="name"  maxlength="16"  type="text">
		</div>
		
		<div class="message_con">
			<label  style="vertical-align:top;">区域属性：</label><div style="border:none;color:#fff;">
			<input type="checkbox" name="attribute" style="margin-left:5px;" value="1">限速 
			<input type="checkbox" name="attribute" style="margin-left:5px;" value="2">进区域报警给驾驶员
			<input type="checkbox" name="attribute" style="margin-left:5px;" value="3">进区域报警给平台
			<input type="checkbox" name="attribute" style="margin-left:5px;" value="4">出区域报警给驾驶员 
			<input type="checkbox" name="attribute" style="margin-left:5px;" value="5">出区域报警给平台
			<input type="checkbox" name="attribute" style="margin-left:5px;" value="8">禁止开门
			<input type="checkbox" name="attribute" style="margin-left:5px;" value="14">进区域关闭通信模块
			<input type="checkbox" name="attribute" style="margin-left:5px;" value="15">进区域采集GNSS详细定位数据
			</div> 
		</div>

		<div id="send-set-rail-circle-limited-speed-div" style="display:none;">
			<div class="message_con">
				<label>围栏限速：</label><input name="limitedSpeed" id="send-set-rail-circle-cmd-dlg-frm-limitedSpeed" maxlength="3"  type="text"  class="integer" placeholder="围栏内超过此速度告警"><span class="must">*</span>
			</div>
			<div class="message_con">
				<label>超速持续时间：</label><input name="durationTime" id="send-set-rail-circle-cmd-dlg-frm-durationTime" maxlength="3" value="60" type="text"  class="integer"  placeholder="围栏内超速持续时间超过告警，单位：秒" ><span class="must">*</span>
			</div>
		</div>

		<div class="message_con">
			<label>时间属性：</label><div style="border:none;color:#fff;"><input name="circleDateTimeAttribute" type="checkbox" value="0">起始时间<input name="circleTimeAttribute" type="checkbox" value="1">固定时间段</div>
		</div>
		<div id="send-set-rail-circle-start-end-time-div" style="display:none;">
			<div class="message_con">
				<label>有效时段：</label><input type="time"  style="width:31.5%;" name="startTime" id="send-set-rail-circle-cmd-dlg-frm-startTime" > - 
				<input  type="time"  style="width:31.5%;" name="endTime" id="send-set-rail-circle-cmd-dlg-frm-endTime"><span class="must">*</span>
			</div>
		</div>
		<div id="send-set-rail-circle-start-end-datetime-div" style="display:none;">
		    <div class="message_con">
				<label>开始时间：</label><input id="send-set-rail-circle-cmd-dlg-frm-startDateTime" name="startDateTime" type="text" readonly class="date form_datetime" data-date-format="yyyy-mm-dd hh:ii:ss" data-picker-position="top-right" placeholder="设置起始时间"><span class="must">*</span>
			</div>
			<div class="message_con">
				<label>结束时间：</label><input id="send-set-rail-circle-cmd-dlg-frm-endDateTime"  name="endDateTime" type="text"  readonly class="date form_datetime" data-date-format="yyyy-mm-dd hh:ii:ss"  data-picker-position="top-right" placeholder="设置结束时间"><span class="must">*</span>
			</div>
		</div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="sendSetCircleRailCmd()">确定</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>

<div id="send-del-rail-circle-cmd-dlg" class="simple-dialog" title="删除圆形区域" style="width:650px;height:200px;">
	<form id="send-del-rail-circle-cmd-dlg-frm" method="post">
		<input name="deviceId" id="send-del-rail-circle-cmd-dlg-frm-deviceId" type="hidden">
		<input name="messageId" type="hidden" value="8601">
		<div class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label><input name="devieName" readonly id="send-del-rail-circle-cmd-dlg-frm-name" type="text">
		</div>
		<div class="message_con">
			<label>圆形区域：</label><select name="areaId" id="send-del-rail-circle-cmd-dlg-frm-areaId" class="required">
				<option value="-1">全部区域</option>
			</select>
		</div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="sendDelRailCmd()">确定</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>

<div id="send-set-rail-rectangle-cmd-dlg" class="simple-dialog" title="设置矩形区域" style="width:650px;height:500px;">
	<form id="send-set-rail-rectangle-cmd-dlg-frm" method="post">
		<input name="deviceId" id="send-set-rail-rectangle-cmd-dlg-frm-deviceId" type="hidden">
		<input name="messageId" type="hidden" value="8602">
		<div class="message_con">
			<label >车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label><input name="devieName" readonly id="send-set-rail-rectangle-cmd-dlg-frm-name" type="text">
		</div>
		<div class="message_con">
			<label >矩形区域：</label><select name="labelPointId" id="send-set-rail-rectangle-cmd-dlg-frm-rail"  class="required">
			</select>
		</div>
		<div class="message_con">
			<label>围栏名称：</label><input name="name"  maxlength="16"  type="text">
		</div>
		<div class="message_con">
			<label  style="vertical-align:top;">区域属性：</label><div style="border:none;color:#fff;">
			<input type="checkbox" name="attribute" style="margin-left:5px;" value="1">限速 
			<input type="checkbox" name="attribute" style="margin-left:5px;" value="2">进区域报警给驾驶员
			<input type="checkbox" name="attribute" style="margin-left:5px;" value="3">进区域报警给平台
			<input type="checkbox" name="attribute" style="margin-left:5px;" value="4">出区域报警给驾驶员 
			<input type="checkbox" name="attribute" style="margin-left:5px;" value="5">出区域报警给平台
			<input type="checkbox" name="attribute" style="margin-left:5px;" value="8">禁止开门
			<input type="checkbox" name="attribute" style="margin-left:5px;" value="14">进区域关闭通信模块
			<input type="checkbox" name="attribute" style="margin-left:5px;" value="15">进区域采集GNSS详细定位数据
			</div> 
		</div>
		<div id="send-set-rail-rectangle-limited-speed-div" style="display:none;">
			<div class="message_con">
				<label>围栏限速：</label><input name="limitedSpeed" id="send-set-rail-rectangle-cmd-dlg-frm-limitedSpeed" maxlength="3"  type="text"  class="integer" placeholder="围栏内超过此速度告警"><span class="must">*</span>
			</div>
			<div class="message_con">
				<label>超速持续时间：</label><input name="durationTime" id="send-set-rail-rectangle-cmd-dlg-frm-durationTime" maxlength="3" value="60" type="text"  class="integer"  placeholder="围栏内超速持续时间超过告警，单位：秒" ><span class="must">*</span>
			</div>
		</div>
		
		<div class="message_con">
				<label>时间属性：</label><div style="border:none;color:#fff;"><input name="rectangleDateTimeAttribute" type="checkbox" value="0">起始时间<input name="rectangleTimeAttribute" type="checkbox" value="1">固定时间段</div>
		</div>
		<div id="send-set-rail-rectangle-start-end-time-div" style="display:none;">
			<div class="message_con">
				<label>有效时段：</label><input type="time"  style="width:31.5%;" name="startTime" id="send-set-rail-rectangle-cmd-dlg-frm-startTime" > - 
				<input  type="time"  style="width:31.5%;" name="endTime" id="send-set-rail-rectangle-cmd-dlg-frm-endTime"><span class="must">*</span>
			</div>
		</div>
		<div id="send-set-rail-rectangle-start-end-datetime-div" style="display:none;">
			<div class="message_con">
				<label>开始时间：</label><input id="send-set-rail-rectangle-cmd-dlg-frm-startDateTime" name="startDateTime" type="text" readonly class="date form_datetime" data-date-format="yyyy-mm-dd hh:ii:ss" data-picker-position="top-right" placeholder="设置起始时间"><span class="must">*</span>
			</div>
			<div class="message_con">
				<label>结束时间：</label><input id="send-set-rail-rectangle-cmd-dlg-frm-endDateTime" name="endDateTime" type="text"  readonly class="date form_datetime" data-date-format="yyyy-mm-dd hh:ii:ss"  data-picker-position="top-right" placeholder="设置结束时间"><span class="must">*</span>
			</div>
		</div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="sendSetRectangleRailCmd()">确定</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>

<div id="send-del-rail-rectangle-cmd-dlg" class="simple-dialog" title="删除矩形区域" style="width:450px;height:200px;">
	<form id="send-del-rail-rectangle-cmd-dlg-frm" method="post">
		<input name="deviceId" id="send-del-rail-rectangle-cmd-dlg-frm-deviceId" type="hidden">
		<input name="messageId" type="hidden" value="8603">
		<div class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label><input name="devieName" readonly id="send-del-rail-rectangle-cmd-dlg-frm-name" type="text">
		</div>
		<div class="message_con">
			<label>矩形区域：</label><select name="areaId" id="send-del-rail-rectangle-cmd-dlg-frm-areaId"  class="required">
			<option value="-1">全部区域</option>
			</select>
		</div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="sendDelRailCmd()">确定</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>

<div id="send-set-rail-polygon-cmd-dlg" class="simple-dialog" title="设置多边形区域" style="width:650px;height:500px;">
	<form id="send-set-rail-polygon-cmd-dlg-frm" method="post">
		<input name="deviceId" id="send-set-rail-polygon-cmd-dlg-frm-deviceId" type="hidden">
		<input name="messageId" type="hidden" value="8604">
		<div class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label><input name="devieName" readonly id="send-set-rail-polygon-cmd-dlg-frm-name" type="text">
		</div>
		<div class="message_con">
			<label>多边形区域：</label><select name="labelPointId" id="send-set-rail-polygon-cmd-dlg-frm-rail"  class="required">
			</select>
		</div>
		<div class="message_con">
			<label>围栏名称：</label><input name="name"  maxlength="16"  type="text">
		</div>
		
		<div class="message_con">
			<label  style="vertical-align:top;">区域属性：</label><div style="border:none;color:#fff;">
			<input type="checkbox" name="attribute" style="margin-left:5px;" value="1">限速 
			<input type="checkbox" name="attribute" style="margin-left:5px;" value="2">进区域报警给驾驶员
			<input type="checkbox" name="attribute" style="margin-left:5px;" value="3">进区域报警给平台
			<input type="checkbox" name="attribute" style="margin-left:5px;" value="4">出区域报警给驾驶员 
			<input type="checkbox" name="attribute" style="margin-left:5px;" value="5">出区域报警给平台
			<input type="checkbox" name="attribute" style="margin-left:5px;" value="8">禁止开门
			<input type="checkbox" name="attribute" style="margin-left:5px;" value="14">进区域关闭通信模块
			<input type="checkbox" name="attribute" style="margin-left:5px;" value="15">进区域采集GNSS详细定位数据
			</div> 
		</div>
		
		<div id="send-set-rail-polygon-limited-speed-div" style="display:none;">
			<div class="message_con">
				<label>围栏限速：</label><input name="limitedSpeed" id="send-set-rail-polygon-cmd-dlg-frm-limitedSpeed" maxlength="3"  type="text"  class="integer" placeholder="围栏内超过此速度告警"><span class="must">*</span>
			</div>
			<div class="message_con">
				<label>超速持续时间：</label><input name="durationTime" id="send-set-rail-polygon-cmd-dlg-frm-durationTime" maxlength="3" value="60" type="text"  class="integer"  placeholder="围栏内超速持续时间超过告警，单位：秒"><span class="must">*</span>
			</div>
		</div>
		
		<div class="message_con">
				<label>时间属性：</label><div style="border:none;color:#fff;"><input name="polygonDateTimeAttribute" type="checkbox" value="0">起始时间<input name="polygonTimeAttribute" type="checkbox" value="1">固定时间段</div>
		</div>
		<div id="send-set-rail-polygon-start-end-time-div" style="display:none;">
			<div class="message_con">
				<label>有效时段：</label><input type="time"  style="width:31.5%;" name="startTime" id="send-set-rail-polygon-cmd-dlg-frm-startTime" > - 
				<input  type="time"  style="width:31.5%;" name="endTime" id="send-set-rail-polygon-cmd-dlg-frm-endTime"><span class="must">*</span>
			</div>
		</div>
		<div id="send-set-rail-polygon-start-end-datetime-div" style="display:none;">
			<div class="message_con">
				<label>开始时间：</label><input id="send-set-rail-polygon-cmd-dlg-frm-startDateTime" name="startDateTime" type="text" readonly class="required date form_datetime" data-date-format="yyyy-mm-dd hh:ii:ss" data-picker-position="top-right" placeholder="设置起始时间"><span class="must">*</span>
			</div>
			<div class="message_con">
				<label>结束时间：</label><input id="send-set-rail-polygon-cmd-dlg-frm-endDateTime" name="endDateTime" type="text"  readonly class="required date form_datetime" data-date-format="yyyy-mm-dd hh:ii:ss"  data-picker-position="top-right" placeholder="设置结束时间"><span class="must">*</span>
			</div>
		</div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="sendSetPolygonRailCmd()">确定</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>

<div id="send-del-rail-polygon-cmd-dlg" class="simple-dialog" title="删除多边形区域" style="width:450px;height:200px;">
	<form id="send-del-rail-polygon-cmd-dlg-frm" method="post">
		<input name="deviceId" id="send-del-rail-polygon-cmd-dlg-frm-deviceId" type="hidden">
		<input name="messageId" type="hidden" value="8605">
		<div class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label><input name="devieName" readonly id="send-del-rail-polygon-cmd-dlg-frm-name" type="text">
		</div>
		<div class="message_con">
			<label>多边形区域：</label><select name="areaId" id="send-del-rail-polygon-cmd-dlg-frm-areaId"  class="required">
			<option value="-1">全部区域</option>
			</select>
		</div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="sendDelRailCmd()">确定</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>

<div id="send-set-rail-route-cmd-dlg" class="simple-dialog" title="设置路线" style="width:650px;height:400px;">
	<form id="send-set-rail-route-cmd-dlg-frm" method="post">
	 <div class="tab_con_div railRouteCmd" style="display: block">
		<input name="deviceId" id="send-set-rail-route-cmd-dlg-frm-deviceId" type="hidden">
		<input name="messageId" type="hidden" value="8606">
		<div class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label><input name="devieName" readonly id="send-set-rail-route-cmd-dlg-frm-name" type="text">
		</div>
		<div class="message_con">
			<label>围栏名称：</label><input name="name"  maxlength="16"  type="text">
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
				<label>时间属性：</label><div style="border:none;color:#fff;"><input name="routeDateTimeAttribute" type="checkbox" value="0">起始时间<input name="routeTimeAttribute" type="checkbox" value="1">固定时间段</div>
			</div>
			<div id="send-set-rail-route-start-end-time-div" style="display:none;">
				<div class="message_con">
					<label>有效时段：</label><input type="time"  style="width:31.5%;" name="startTime" id="send-set-rail-route-cmd-dlg-frm-startTime" > - 
					<input  type="time"  style="width:31.5%;" name="endTime" id="send-set-rail-route-cmd-dlg-frm-endTime"><span class="must">*</span>
				</div>
			</div>
			<div id="send-set-rail-route-start-end-datetime-div" style="display:none;">
				<div class="message_con">
					<label>开始时间：</label><input id="send-set-rail-route-cmd-dlg-frm-startDateTime" name="startDateTime" type="text"  class="date form_datetime" data-date-format="yyyy-mm-dd hh:ii:ss" data-picker-position="top-right" placeholder="设置起始时间"><span class="must">*</span>
				</div>
				<div class="message_con">
					<label>结束时间：</label><input id="send-set-rail-route-cmd-dlg-frm-endDateTime"  name="endDateTime" type="text"  class="date form_datetime" data-date-format="yyyy-mm-dd hh:ii:ss"  data-picker-position="top-right" placeholder="设置结束时间"><span class="must">*</span>
				</div>
			</div>
			<div class="message_footer">
				<button type="button" class="bule" onclick="showRouteSettingNext()">下一步</button>
				<button type="button" class="orgen" onclick="closeDialog()">取消</button>
			</div>
			</div>
			<div class="tab_con_div railRouteCmd">
				<div  style="height:280px;">
			 	 <%@ include file="../pub/multipleSelectBox.jsp"%>
			 	</div>
				  <div class="message_footer">
				    <button type="button" class="orgen" onclick="showRouteSettingPrev()">上一步</button>
				    <button type="button" class="bule" onclick="sendSetRouteRailCmd()">确定</button>
					<button type="button" class="orgen" onclick="closeDialog()">取消</button>
				  </div>
            </div>
	</form>
</div>

<div id="send-del-rail-route-cmd-dlg" class="simple-dialog" title="删除路线" style="width:450px;height:200px;">
	<form id="send-del-rail-route-cmd-dlg-frm" method="post">
		<input name="deviceId" id="send-del-rail-route-cmd-dlg-frm-deviceId" type="hidden">
		<input name="messageId" type="hidden" value="8607">
		<div class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label><input name="devieName" readonly id="send-del-rail-route-cmd-dlg-frm-name" type="text">
		</div>
		<div class="message_con">
			<label>路&nbsp;&nbsp;&nbsp;&nbsp;线：</label><select name="areaId" id="send-del-rail-route-cmd-dlg-frm-areaId"  class="required">
			<option value="">全部路线</option>
			</select>
		</div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="sendDelRailCmd()">确定</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>
<script type="text/javascript" src="<c:url value='/js/device/cmdSetting-rail.js'/>"></script>