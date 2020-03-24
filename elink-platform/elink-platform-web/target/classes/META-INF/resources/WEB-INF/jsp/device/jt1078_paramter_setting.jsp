<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<div id="send-8103-0079-cmd-dlg" title="特殊报警录像参数" class="simple-dialog">
	<form id="send-8103-0079-cmd-dlg-frm" method="post">
		<input name="messageId" type="hidden" value="8103">
		<input name="paramkeys" type="hidden" value="0079">
		<div class="message_con">
			<label style='width:200px;'>特殊录像报警存储阈值：</label><div class="custum-range-slider custum-range-slider-second">
				<input class="custum-range-slider__range" util="%" name="threshold" id="send-8103-0079-cmd-dlg-frm-threshold" value="20" min="1" max="99" type="range">
				<span class="custum-range-slider__value">20</span>
			</div>
		</div>

		<div class="message_con">
			<label style='width:200px;'>特殊录像报警持续时间：</label><div class="custum-range-slider custum-range-slider-second">
				<input class="custum-range-slider__range" util="分钟" name="duration" id="send-8103-0079-cmd-dlg-frm-duration" value="5" min="0" max="100" type="range">
				<span class="custum-range-slider__value">5</span>
			</div>
		</div>

		<div class="message_con">
			<label style='width:200px;'>特殊报警标识起始时间：</label><div class="custum-range-slider custum-range-slider-second">
				<input class="custum-range-slider__range" util="分钟" name="presetTime" id="send-8103-0079-cmd-dlg-frm-presetTime" value="1" min="1" max="30" type="range">
				<span class="custum-range-slider__value">1</span>
			</div>
		</div>
	</form>
</div>

<div id="send-8103-007A-cmd-dlg" title="视频报警屏蔽字参数" class="simple-dialog">
	<form id="send-8103-007A-cmd-dlg-frm" method="post">
		<input name="messageId" type="hidden" value="8103">
		<input name="paramkeys" type="hidden" value="007A">
		<div id="cmd_setting_8103007A_div"></div>
	</form>
</div>

<div id="send-8103-007B-cmd-dlg" title="图像分析报警参数" class="simple-dialog">
	<form id="send-8103-007B-cmd-dlg-frm" method="post">
		<input name="messageId" type="hidden" value="8103">
		<input name="paramkeys" type="hidden" value="007B">
		<div class="message_con">
			<label style='width:200px;'>车辆核载人数：</label><div class="custum-range-slider custum-range-slider-second">
				<input class="custum-range-slider__range" util="人" name="allowNum" id="send-8103-007B-cmd-dlg-frm-allowNum" value="35" min="1" max="255" type="range">
				<span class="custum-range-slider__value">35</span>
			</div>
		</div>

		<div class="message_con">
			<label style='width:200px;'>疲劳程度阈值：</label><div class="custum-range-slider custum-range-slider-second">
				<input class="custum-range-slider__range" util="" name="fatigueDrivingThreshold" id="send-8103-007B-cmd-dlg-frm-fatigueDrivingThreshold" value="1" min="1" max="100" type="range">
				<span class="custum-range-slider__value">50</span>
			</div>
		</div>
	</form>
</div>

<script  type="text/javascript" src="<c:url value='/js/device/jt1078_paramter_setting.js'/>"></script>
