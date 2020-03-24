<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<div id="send-dsm-cmd-dlg" title="驾驶员状态监测系统参数" class="simple-dialog">
	<form id="send-dsm-cmd-dlg-frm" method="post">
	<input name="messageId" type="hidden" value="8103">
	<input name="paramkeys" type="hidden" value="F365">
	<input name="dsm-p13" id="dsm-p13" type="hidden" value="0">
	<input name="dsm-p35" id="dsm-p35" type="hidden" value="0">
		<div class="message_con">
			<label style='width:200px;'>报警判断速度阈值：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="km/h" name="dsm-p0" id="dsm-p0" value="30" min="0" max="60" type="range">
						<span class="custum-range-slider__value">30</span>
					</div>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>报警音量：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" name="dsm-p1" id="dsm-p1" value="6" min="0" max="8" type="range">
						<span class="custum-range-slider__value">6</span>
					</div>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>主动拍照策略：</label><select name="dsm-p2" id="dsm-p2">
			    <option value="ff">请选择</option>
				<option value="0">不开启</option>
				<option value="1">定时拍照</option>
				<option value="2">定距拍照</option>
				<option value="3">插卡触发</option>
			</select>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>主动定时拍照时间间隔：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="秒" name="dsm-p3" id="dsm-p3" value="60" min="0" max="6000" type="range">
						<span class="custum-range-slider__value">60秒</span>
					</div>
			</select>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>主动定距拍照距离间隔：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="米" name="dsm-p4" id="dsm-p4" value="200" min="0" max="60000" type="range">
						<span class="custum-range-slider__value">200米</span>
					</div>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>单次主动拍照张数：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="张" name="dsm-p5" id="dsm-p5" value="3" min="1" max="10" type="range">
						<span class="custum-range-slider__value">3张</span>
					</div>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>单次主动拍照时间间隔：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="毫秒"  times="100" name="dsm-p6" id="dsm-p6" value="2" min="1" max="5" type="range">
						<span class="custum-range-slider__value">200毫秒</span>
					</div>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>拍照分辨率：</label><select name="dsm-p7" id="dsm-p7">
							<option value="ff">请选择</option>
							<option value="1">352×288</option>
							<option value="2">704×288</option>
							<option value="3">704×576</option>
							<option value="4">640×480</option>
							<option value="5">1280×720</option>
							<option value="6">1920×1080</option>
						</select>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>视频录制分辨率：</label><select name="dsm-p8" id="dsm-p8">
							<option value="ff">请选择</option>
							<option value="1">CIF</option>
							<option value="2">HD1</option>
							<option value="3">D1</option>
							<option value="4">WD1</option>
							<option value="5">VGA</option>
							<option value="6">720P</option>
							<option value="7">1080P</option>
						</select>
		</div>
		
		<div class="message_con">
			<label style='width:200px;vertical-align:top;'>报警使能：</label>
			<div>
			<label style='width:200px;'>疲劳驾驶一级报警：</label>开<input checked="checked" type="radio" value="1" name="dsm-p9-0">关<input type="radio" value="0" name="dsm-p9-0">
			<label style='width:200px;'>疲劳驾驶二级报警：</label>开<input checked="checked" type="radio" value="1" name="dsm-p9-1">关<input type="radio" value="0" name="dsm-p9-1">
			<label style='width:200px;'>接打电话一级报警：</label>开<input checked="checked" type="radio" value="1" name="dsm-p9-2">关<input type="radio" value="0" name="dsm-p9-2">
			<label style='width:200px;'>接打电话二级报警：</label>开<input checked="checked" type="radio" value="1" name="dsm-p9-3">关<input type="radio" value="0" name="dsm-p9-3">
			<label style='width:200px;'>抽烟一级报警：</label>开<input checked="checked" type="radio" value="1" name="dsm-p9-4">关<input type="radio" value="0" name="dsm-p9-4">
			<label style='width:200px;'>抽烟二级报警：</label>开<input checked="checked" type="radio" value="1" name="dsm-p9-5">关<input type="radio" value="0" name="dsm-p9-5">
			<label style='width:200px;'>分神驾驶一级报警：</label>开<input checked="checked" type="radio" value="1" name="dsm-p9-6">关<input type="radio" value="0" name="dsm-p9-6">
			<label style='width:200px;'>分神驾驶二级报警：</label>开<input checked="checked" type="radio" value="1" name="dsm-p9-7">关<input type="radio" value="0" name="dsm-p9-7">
			<label style='width:200px;'>驾驶员异常一级报警：</label>开<input checked="checked" type="radio" value="1" name="dsm-p9-8">关<input type="radio" value="0" name="dsm-p9-8">
			<label style='width:200px;'>驾驶员异常二级报警：</label>开<input checked="checked" type="radio" value="1" name="dsm-p9-9">关<input type="radio" value="0" name="dsm-p9-9">
			</div>
		</div>
		
		<div class="message_con">
			<label style='width:200px;vertical-align:top;'>事件使能：</label>
			<div>
				<label style='width:200px;'>驾驶员更换事件：</label>开<input checked="checked" type="radio" value="1" name="dsm-p10-0">关<input type="radio" value="0" name="dsm-p10-0">
				<label style='width:200px;'>主动拍照事件：</label>开<input checked="checked" type="radio" value="1" name="dsm-p10-1">关<input type="radio" value="0" name="dsm-p10-1">
			</div>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>吸烟报警判断时间间隔：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="秒" name="dsm-p11" id="dsm-p11" value="180" min="0" max="3600" type="range">
						<span class="custum-range-slider__value">180秒</span>
					</div>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>接打电话报警判断时间间隔：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="秒"  name="dsm-p12" id="dsm-p12" value="120" min="0" max="3600" type="range">
						<span class="custum-range-slider__value">120秒</span>
					</div>
		</div>

		<div class="message_con">
			<label style='width:200px;'>疲劳驾驶报警分级速度阈值：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="km/h" name="dsm-p14" id="dsm-p14" value="50" min="0" max="220" type="range">
						<span class="custum-range-slider__value">50km/h</span>
					</div>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>疲劳驾驶报警前后视频录制时间：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="秒" name="dsm-p15" id="dsm-p15" value="5" min="0" max="60" type="range">
						<span class="custum-range-slider__value">5秒</span>
					</div>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>疲劳驾驶报警拍照张数：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="张" name="dsm-p16" id="dsm-p16" value="3" min="0" max="10" type="range">
						<span class="custum-range-slider__value">3张</span>
					</div>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>疲劳驾驶报警拍照间隔时间：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="毫秒" times="100" name="dsm-p17" id="dsm-p17" value="2" min="1" max="5" type="range">
						<span class="custum-range-slider__value">200毫秒</span>
					</div>
		</div>
		
		
		<div class="message_con">
			<label style='width:200px;'>接打电话报警分级速度阈值：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="km/h" name="dsm-p18" id="dsm-p18" value="50" min="0" max="220" type="range">
						<span class="custum-range-slider__value">50km/h</span>
					</div>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>接打电话报警前后视频录制时间：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="秒" name="dsm-p19" id="dsm-p19" value="5" min="0" max="60" type="range">
						<span class="custum-range-slider__value">5秒</span>
					</div>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>接打电话报警拍驾驶员面部特征拍照张数：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="张" name="dsm-p20" id="dsm-p20" value="3" min="0" max="10" type="range">
						<span class="custum-range-slider__value">3张</span>
					</div>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>接打电话报警拍驾驶员面部特征照片间隔时间：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="毫秒" times="100" name="dsm-p21" id="dsm-p21" value="2" min="1" max="5" type="range">
						<span class="custum-range-slider__value">200毫秒</span>
					</div>
		</div>
		
		
		<div class="message_con">
			<label style='width:200px;'>抽烟报警分级车速阈值：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="km/h" name="dsm-p22" id="dsm-p22" value="50" min="0" max="220" type="range">
						<span class="custum-range-slider__value">50km/h</span>
					</div>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>抽烟报警前后视频录制时间：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="秒" name="dsm-p23" id="dsm-p23" value="5" min="0" max="60" type="range">
						<span class="custum-range-slider__value">5秒</span>
					</div>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>抽烟报警拍驾驶员面部特征照片张数：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="张" name="dsm-p24" id="dsm-p24" value="3" min="0" max="10" type="range">
						<span class="custum-range-slider__value">3张</span>
					</div>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>抽烟报警拍驾驶员面部特征照片间隔时间：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="毫秒" times="100" name="dsm-p25" id="dsm-p25" value="2" min="1" max="5" type="range">
						<span class="custum-range-slider__value">200毫秒</span>
					</div>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>分神驾驶报警分级车速阈值：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="km/h" name="dsm-p26" id="dsm-p26" value="50" min="0" max="220" type="range">
						<span class="custum-range-slider__value">50km/h</span>
					</div>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>分神驾驶报警前后视频录制时间：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="秒" name="dsm-p27" id="dsm-p27" value="5" min="0" max="60" type="range">
						<span class="custum-range-slider__value">5秒</span>
					</div>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>分神驾驶报警拍照张数：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="张" name="dsm-p28" id="dsm-p28" value="3" min="0" max="10" type="range">
						<span class="custum-range-slider__value">3张</span>
					</div>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>分神驾驶报警拍照间隔时间：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="毫秒" times="100" name="dsm-p29" id="dsm-p29" value="2" min="1" max="5" type="range">
						<span class="custum-range-slider__value">200毫秒</span>
					</div>
		</div>
		

		<div class="message_con">
			<label style='width:200px;'>驾驶行为异常分级速度阈值：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="km/h" name="dsm-p30" id="dsm-p30" value="50" min="0" max="220" type="range">
						<span class="custum-range-slider__value">50km/h</span>
					</div>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>驾驶行为异常视频录制时间：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="秒" name="dsm-p31" id="dsm-p31" value="5" min="0" max="60" type="range">
						<span class="custum-range-slider__value">5秒</span>
					</div>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>驾驶行为异常抓拍照片张数：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="张" name="dsm-p32" id="dsm-p32" value="3" min="0" max="10" type="range">
						<span class="custum-range-slider__value">3张</span>
					</div>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>驾驶行为异常拍照间隔：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="毫秒" times="100" name="dsm-p33" id="dsm-p33" value="2" min="1" max="5" type="range">
						<span class="custum-range-slider__value">200毫秒</span>
					</div>
		</div>

		<div class="message_con">
			<label style='width:200px;'>驾驶员身份识别触发：</label><select name="dsm-p34" id="dsm-p34">
							<option value="ff">请选择</option>
							<option value="0">不开启</option>
							<option value="1">定时触发</option>
							<option value="2">定距触发</option>
							<option value="3">插卡开始行驶触发</option>
						</select>
		</div>
		
	</form>
</div>