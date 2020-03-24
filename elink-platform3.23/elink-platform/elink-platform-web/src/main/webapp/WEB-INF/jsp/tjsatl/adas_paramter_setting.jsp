<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<div id="send-adas-cmd-dlg" title="高级驾驶辅助系统参数 " class="simple-dialog">
	<form id="send-adas-cmd-dlg-frm" method="post">
	<input name="messageId" type="hidden" value="8103">
	<input name="paramkeys" type="hidden" value="F364">
	<input name="adas-p11" id="adas-p11" type="hidden" value="0">
	<input name="adas-p43" id="adas-p43" type="hidden" value="0">
		<div class="message_con">
			<label style='width:200px;'>报警判断速度阈值：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="km/h" name="adas-p0" id="adas-p0" value="30" min="0" max="60" type="range">
						<span class="custum-range-slider__value">30</span>
					</div>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>报警提示音量：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" name="adas-p1" id="adas-p1" value="6" min="0" max="8" type="range">
						<span class="custum-range-slider__value">6</span>
					</div>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>主动拍照策略：</label><select name="adas-p2" id="adas-p2">
			    <option value="ff">请选择</option>
				<option value="0">不开启</option>
				<option value="1">定时拍照</option>
				<option value="2">定距拍照</option>
			</select>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>主动定时拍照时间间隔：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="秒" name="adas-p3" id="adas-p3" value="60" min="0" max="3600" type="range">
						<span class="custum-range-slider__value">60秒</span>
					</div>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>主动定距拍照距离间隔：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="米" name="adas-p4" id="adas-p4" value="200" min="0" max="60000" type="range">
						<span class="custum-range-slider__value">200米</span>
					</div>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>单次主动拍照张数：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="张" name="adas-p5" id="adas-p5" value="3" min="1" max="10" type="range">
						<span class="custum-range-slider__value">3张</span>
					</div>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>单次主动拍照时间间隔：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="毫秒"  times="100" name="adas-p6" id="adas-p6" value="2" min="1" max="5" type="range">
						<span class="custum-range-slider__value">200毫秒</span>
					</div>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>拍照分辨率：</label><select name="adas-p7" id="adas-p7">
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
			<label style='width:200px;'>视频录制分辨率：</label><select name="adas-p8" id="adas-p8">
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
			<label style='width:200px;'>障碍检测一级报警：</label>开<input checked="checked" type="radio" value="1" name="adas-p9-0">关<input type="radio" value="0" name="adas-p9-0">
			<label style='width:200px;'>障碍检测二级报警：</label>开<input checked="checked" type="radio" value="1" name="adas-p9-1">关<input type="radio" value="0" name="adas-p9-1">
			<label style='width:200px;'>频繁变道一级报警：</label>开<input checked="checked" type="radio" value="1" name="adas-p9-2">关<input type="radio" value="0" name="adas-p9-2">
			<label style='width:200px;'>频繁变道二级报警：</label>开<input checked="checked" type="radio" value="1" name="adas-p9-3">关<input type="radio" value="0" name="adas-p9-3">
			<label style='width:200px;'>车道偏离一级报警：</label>开<input checked="checked" type="radio" value="1" name="adas-p9-4">关<input type="radio" value="0" name="adas-p9-4">
			<label style='width:200px;'>车道偏离二级报警：</label>开<input checked="checked" type="radio" value="1" name="adas-p9-5">关<input type="radio" value="0" name="adas-p9-5">
			<label style='width:200px;'>前向碰撞一级报警：</label>开<input checked="checked" type="radio" value="1" name="adas-p9-6">关<input type="radio" value="0" name="adas-p9-6">
			<label style='width:200px;'>前向碰撞二级报警：</label>开<input checked="checked" type="radio" value="1" name="adas-p9-7">关<input type="radio" value="0" name="adas-p9-7">
			<label style='width:200px;'>行人碰撞一级报警：</label>开<input checked="checked" type="radio" value="1" name="adas-p9-8">关<input type="radio" value="0" name="adas-p9-8">
			<label style='width:200px;'>行人碰撞二级报警：</label>开<input checked="checked" type="radio" value="1" name="adas-p9-9">关<input type="radio" value="0" name="adas-p9-9">
			<label style='width:200px;'>车距过近一级报警：</label>开<input checked="checked" type="radio" value="1" name="adas-p9-10">关<input type="radio" value="0" name="adas-p9-10">
			<label style='width:200px;'>车距过近二级报警：</label>开<input checked="checked" type="radio" value="1" name="adas-p9-11">关<input type="radio" value="0" name="adas-p9-11">
			<label style='width:200px;'>道路标识超限报警：</label>开<input checked="checked" type="radio" value="1" name="adas-p9-16">关<input type="radio" value="0" name="adas-p9-16">			
			</div>
		</div>
		
		<div class="message_con">
			<label style='width:200px;vertical-align:top;'>事件使能：</label>
			<div>
				<label style='width:200px;'>道路标识识别：</label>开<input checked="checked" type="radio" value="1" name="adas-p10-0">关<input type="radio" value="0" name="adas-p10-0">
				<label style='width:200px;'>主动拍照：</label>开<input checked="checked" type="radio" value="1" name="adas-p10-1">关<input type="radio" value="0" name="adas-p10-1">
			</div>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>障碍物报警距离阈值：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="毫秒"  times="100" name="adas-p12" id="adas-p12" value="30" min="10" max="50" type="range">
						<span class="custum-range-slider__value">300ms</span>
					</div>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>障碍物报警分级速度阈值：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="km/h"  name="adas-p13" id="adas-p13" value="50" min="0" max="220" type="range">
						<span class="custum-range-slider__value">50km/h</span>
					</div>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>障碍物报警前后视频录制时间：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range"  util="秒" name="adas-p14" id="adas-p14" value="5" min="0" max="60" type="range">
						<span class="custum-range-slider__value">5秒</span>
					</div>
		</div>

		<div class="message_con">
			<label style='width:200px;'>障碍物报警拍照张数：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="张" name="adas-p15" id="adas-p15" value="3" min="0" max="10" type="range">
						<span class="custum-range-slider__value">3张</span>
					</div>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>障碍物报警拍照间隔：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="毫秒"  times="100" name="adas-p16" id="adas-p16" value="2" min="0" max="10" type="range">
						<span class="custum-range-slider__value">200毫秒</span>
					</div>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>频繁变道报警判断时间段：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="秒" name="adas-p17" id="adas-p17" value="60" min="30" max="120" type="range">
						<span class="custum-range-slider__value">60秒</span>
					</div>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>频繁变道报警判断次数：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="次" name="adas-p18" id="adas-p18" value="5" min="3" max="10" type="range">
						<span class="custum-range-slider__value">5次</span>
					</div>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>频繁变道报警分级速度阈值：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="km/h"  name="adas-p19" id="adas-p19" value="50" min="0" max="220" type="range">
						<span class="custum-range-slider__value">50km/h</span>
					</div>
		</div>

		<div class="message_con">
			<label style='width:200px;'>频繁变道报警前后视频录制时间：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range"  util="秒" name="adas-p20" id="adas-p20" value="5" min="0" max="60" type="range">
						<span class="custum-range-slider__value">5秒</span>
					</div>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>频繁变道报警拍照张数：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="张" name="adas-p21" id="adas-p21" value="3" min="1" max="10" type="range">
						<span class="custum-range-slider__value">3张</span>
					</div>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>频繁变道报警拍照间隔：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range"  util="毫秒"  times="100" name="adas-p22" id="adas-p22" value="2" min="1" max="10" type="range">
						<span class="custum-range-slider__value">200毫秒</span>
					</div>
		</div>
		

		<div class="message_con">
			<label style='width:200px;'>车道偏离报警分级速度阈值：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="km/h" name="adas-p23" id="adas-p23" value="50" min="0" max="220" type="range">
						<span class="custum-range-slider__value">50km/h</span>
					</div>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>车道偏离报警前后视频录制时间：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="秒" name="adas-p24" id="adas-p24" value="5" min="0" max="60" type="range">
						<span class="custum-range-slider__value">5秒</span>
					</div>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>车道偏离报警拍照张数：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="张" name="adas-p25" id="adas-p25" value="3" min="0" max="10" type="range">
						<span class="custum-range-slider__value">3张</span>
					</div>
		</div>

		<div class="message_con">
			<label style='width:200px;'>车道偏离报警拍照间隔：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="毫秒"  times="100" name="adas-p26" id="adas-p26" value="2" min="0" max="10" type="range">
						<span class="custum-range-slider__value">200毫秒</span>
					</div>
		</div>

		<div class="message_con">
			<label style='width:200px;'>前向碰撞报警时间阈值：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="毫秒"  times="100" name="adas-p27" id="adas-p27" value="27" min="10" max="50" type="range">
						<span class="custum-range-slider__value">2700毫秒</span>
					</div>
		</div>

		<div class="message_con">
			<label style='width:200px;'>前向碰撞报警分级速度阈值：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range"  util="km/h" name="adas-p28" id="adas-p28" value="50" min="0" max="220" type="range">
						<span class="custum-range-slider__value">50km/h</span>
					</div>
		</div>

		<div class="message_con">
			<label style='width:200px;'>前向碰撞报警前后视频录制时间：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="秒" name="adas-p29" id="adas-p29" value="5" min="0" max="60" type="range">
						<span class="custum-range-slider__value">5秒</span>
					</div>
		</div>

		<div class="message_con">
			<label style='width:200px;'>前向碰撞报警拍照张数：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="张" name="adas-p30" id="adas-p30" value="3" min="0" max="10" type="range">
						<span class="custum-range-slider__value">3张</span>
					</div>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>前向碰撞报警拍照间隔：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range"  util="毫秒"  times="100" name="adas-p31" id="adas-p31" value="2" min="1" max="10" type="range">
						<span class="custum-range-slider__value">200毫秒</span>
					</div>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>行人碰撞报警时间阈值：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="毫秒"  times="100" name="adas-p32" id="adas-p32" value="30" min="10" max="50" type="range">
						<span class="custum-range-slider__value">3000毫秒</span>
					</div>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>行人碰撞报警使能速度阈值：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range"  util="km/h" name="adas-p33" id="adas-p33" value="50" min="0" max="220" type="range">
						<span class="custum-range-slider__value">50km/h</span>
					</div>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>行人碰撞报警前后视频录制时间：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="秒" name="adas-p34" id="adas-p34" value="5" min="0" max="60" type="range">
						<span class="custum-range-slider__value">5秒</span>
					</div>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>行人碰撞报警拍照张数：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="张" name="adas-p35" id="adas-p35" value="3" min="0" max="10" type="range">
						<span class="custum-range-slider__value">3张</span>
					</div>
		</div>

		<div class="message_con">
			<label style='width:200px;'>行人碰撞报警拍照间隔：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range"  util="毫秒"  times="100" name="adas-p36" id="adas-p36" value="2" min="1" max="10" type="range">
						<span class="custum-range-slider__value">200毫秒</span>
					</div>
		</div>

		<div class="message_con">
			<label style='width:200px;'>车距监控报警距离阈值：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range"  util="毫秒"  times="100" name="adas-p37" id="adas-p37" value="10" min="10" max="50" type="range">
						<span class="custum-range-slider__value">1000ms</span>
					</div>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>车距监控报警分级速度阈值：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="km/h" name="adas-p38" id="adas-p38" value="50" min="0" max="220" type="range">
						<span class="custum-range-slider__value">50km/h</span>
					</div>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>车距过近报警前后视频录制时间：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="秒" name="adas-p39" id="adas-p39" value="5" min="0" max="60" type="range">
						<span class="custum-range-slider__value">5秒</span>
					</div>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>车距过近报警拍照张数：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="张" name="adas-p40" id="adas-p40" value="3" min="0" max="10" type="range">
						<span class="custum-range-slider__value">3张</span>
					</div>
		</div>

		<div class="message_con">
			<label style='width:200px;'>车距过近报警拍照间隔：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="毫秒"  times="100" name="adas-p41" id="adas-p41" value="2" min="1" max="10" type="range">
						<span class="custum-range-slider__value">200毫秒</span>
					</div>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>道路标志识别拍照张数：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="张" name="adas-p42" id="adas-p42" value="3" min="0" max="10" type="range">
						<span class="custum-range-slider__value">3张</span>
					</div>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>道路标志识别拍照间隔：</label><div class="custum-range-slider custum-range-slider-second">
						<input class="custum-range-slider__range" util="毫秒"  times="100" name="adas-p43" id="adas-p43" value="2" min="1" max="10" type="range">
						<span class="custum-range-slider__value">200毫秒</span>
					</div>
		</div>
	</form>
</div>
<script type="text/javascript" src="<c:url value='/js/tjsatl/paramter_setting.js?v=20191128'/>"></script>