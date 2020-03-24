<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<div id="send-tpms-cmd-dlg" title="胎压监测系统参数" class="simple-dialog">
	<form id="send-tpms-cmd-dlg-frm" method="post">
	<input name="messageId" type="hidden" value="8103">
	<input name="paramkeys" type="hidden" value="F366">
	<input name="tpms-p10" id="tpms-p10" type="hidden" value="0">
		<div class="message_con">
			<label style='width:200px;'>轮胎规格型号：</label><input name="tpms-p0" id="tpms-p0" value="900R20" maxlength="12" type="text">
		</div>

		<div class="message_con">
			<label style='width:200px;'>胎压单位：</label><select name="tpms-p1" id="tpms-p1">
			    <option value="0">kg/cm2</option>
				<option value="1">bar</option>
				<option value="2">Kpa</option>
				<option value="3" selected="selected">PSI</option>
			</select>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>正常胎压值：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="PSI" name="tpms-p2" id="tpms-p2" value="140" min="0" max="1000" type="range">
						<span class="custum-range-slider__value">60</span>
					</div>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>胎压不平衡门限：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="%" name="tpms-p3" id="tpms-p3" value="20" min="0" max="100" type="range">
						<span class="custum-range-slider__value">20%</span>
					</div>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>慢漏气门限：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="%" name="tpms-p4" id="tpms-p4" value="5" min="0" max="100" type="range">
						<span class="custum-range-slider__value">5%</span>
					</div>
		</div>

		<div class="message_con">
			<label style='width:200px;'>低压阈值：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="PSI" name="tpms-p5" id="tpms-p5" value="110" min="0" max="1000" type="range">
						<span class="custum-range-slider__value">110</span>
					</div>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>高压阈值：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="PSI" name="tpms-p6" id="tpms-p6" value="189" min="0" max="1000" type="range">
						<span class="custum-range-slider__value">189</span>
					</div>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>高温阈值：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="℃"  name="tpms-p7" id="tpms-p7" value="80" min="0" max="3600" type="range">
						<span class="custum-range-slider__value">80℃</span>
					</div>
		</div>

		<div class="message_con">
			<label style='width:200px;'>电压阈值：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="%" name="tpms-p8" id="tpms-p8" value="10" min="0" max="100" type="range">
						<span class="custum-range-slider__value">10%</span>
					</div>
		</div>

		<div class="message_con">
			<label style='width:200px;'>定时上报时间间隔：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="秒" name="tpms-p9" id="tpms-p9" value="60" min="0" max="3600" type="range">
						<span class="custum-range-slider__value">60秒</span>
					</div>
		</div>
	</form>
</div>



<div id="send-bsd-cmd-dlg" title="盲区监测系统参数" class="simple-dialog">
	<form id="send-bsd-cmd-dlg-frm" method="post">
	<input name="messageId" type="hidden" value="8103">
	<input name="paramkeys" type="hidden" value="F367">
		<div class="message_con">
			<label style='width:200px;'>后方接近报警时间阈值：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="秒" name="bsd-p0" id="bsd-p0" value="1" min="1" max="10" type="range">
						<span class="custum-range-slider__value">1秒</span>
					</div>
		</div>

		<div class="message_con">
			<label style='width:200px;'>侧后方接近报警时间阈值：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="秒" name="bsd-p1" id="bsd-p1" value="1" min="1" max="10" type="range">
						<span class="custum-range-slider__value">1秒</span>
					</div>
		</div>
	</form>
</div>