<%@ page language="java" contentType="text/html; charset=utf-8" 	pageEncoding="utf-8"%>

<div id="label-point-setting-gps-history-query-dlg" class="simple-dialog"  title="历史轨迹查询" style="width:500px; height:230px;">
	<form id="label-point-setting-gps-history-query-dlg-frm" method="post">
		<div class="message_con">
			<label>车牌号：</label><input name="name" type="text" id="label-point-setting-gps-history-query-dlg-frm-name">
		</div>
		<div class="message_con">
			<label>轨迹日期：</label><input name="date" type="text"  readonly class="required date form_date" data-date-format="yyyy-mm-dd"  id="label-point-setting-gps-history-query-dlg-frm-date">
		</div>
		<div class="message_con">
			<label>轨迹时间：</label><input type="time"  style="width:32%;" name="startTime" id="label-point-setting-gps-history-query-dlg-frm-startTime" > - 
			<input  type="time"  style="width:32%;" name="endTime" id="label-point-setting-gps-history-query-dlg-frm-endTime">
		</div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="collectRoute()">查询</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>

<div id="label-point-setting-dlg" class="simple-dialog"  title="标注设置" style="width:500px; height: 350px;">
	<form id="label-point-setting-dlg-frm" method="post">
		<input name="type" id="label-point-setting-dlg-frm-type"  type="hidden">
		<div class="message_con">
			<label>标注名称：</label><input name="name" type="text"  maxlength="16" class="required" ><span class="must">*</span>
		</div>
		<div class="message_con">
			<label>业务类型：</label><select name="bizType" id="label-point-setting-dlg-frm-bizType">
					  <option  value ="1">区域查车</option>
					  <option  value ="2">电子围栏</option>
			</select>
		</div>
		<!--midified by liliang 20200304-->
		<!-- <div class="message_con">
			<label  style="vertical-align:top;">标注备注：</label><textarea  name="text"  style="height:150px;" maxlength="250" /></textarea>
		</div> -->
		<div class="message_footer">
			<button type="button" class="bule" onclick="saveLabelPointSetting()">保存</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>