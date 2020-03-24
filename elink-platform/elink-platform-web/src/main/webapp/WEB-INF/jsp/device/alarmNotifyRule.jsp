<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/common/header.jsp"%>
<link rel="stylesheet" href="<c:url value='/css/tail.css'/>">
<title>报警规则设置</title>
</head>
<body>
<form id="edit-frm" method="post">
    <input name="id" id="edit-frm-id" value="${sessionScope.user.enterpriseId}" type="hidden">
	<section class="sm_section left">
		<fieldset class="message_fieldset">
			<legend>有效告警（不设置默认全部）</legend>
			<div class="message_con">
				<label>设备故障报警：</label>
				<p style="padding-left: 0;" id="device-fault-alarm"></p>
			</div>
			
			<div class="message_con">
				<label>驾驶行为报警：</label>
				<p style="padding-left: 0;" id="driving-behavior-alarm"></p>
			</div>
			
			<div class="message_con">
				<label>紧急重要报警：</label>
				<p style="padding-left: 0;" id="car-urgent-alarm"></p>
			</div>
		</fieldset>
		
		<fieldset class="message_fieldset">
			<legend>声音开关</legend>
				<div class="message_con">
					<label style="width:18%;">设备故障报警声音开关：</label>
					<input type="radio" value="true"  name="faultAlarmVoiceEnable"><p>开启</p>
					<input type="radio" value="false" checked="true" name="faultAlarmVoiceEnable"><p>关闭</p>
				</div>
				<div id="faultAlarmVoice-box" style="display:none;">
					<div class="message_con">
						<label style="width:18%;">设备故障报警声音持续时长：</label>
						<input name="faultAlarmVoiceTime" id="edit-frm-faultAlarmVoiceTime" maxlength="3" value="3" type="text" class="required digits" placeholder="单位：秒">
					</div>
					
					<div class="message_con">
						<label style="width:18%;">设备故障报警报警声音：</label>
						<select name="faultAlarmVoice" id="edit-frm-faultAlarmVoice">
						  <option  value ="1.mp3">电子警报声</option>
						  <option  value ="3.mp3">红色警报声</option>
						  <option  value ="5.mp3">机动车报警声</option>
						  <option  value ="7.mp3">汽车警报声效</option>
						  <option  value ="8.mp3">遇险报警声</option>
						</select>
					</div>
			   </div>
			   
			   <div class="message_con">
					<label style="width:18%;">驾驶行为报警声音开关：</label>
					<input type="radio" value="true"  name="drivingBehaviorAlarmVoiceEnable"><p>开启</p>
					<input type="radio" value="false" checked="true" name="drivingBehaviorAlarmVoiceEnable"><p>关闭</p>
				</div>
				<div id="drivingBehaviorAlarmVoice-box" style="display:none;">
					<div class="message_con">
						<label style="width:18%;">驾驶行为报警声音持续时长：</label>
						<input name="drivingBehaviorAlarmVoiceTime" id="edit-frm-drivingBehaviorAlarmVoiceTime" maxlength="3" value="3" type="text" class="required digits" placeholder="单位：秒">
					</div>
					
					<div class="message_con">
						<label style="width:18%;">驾驶行为报警报警声音：</label>
						<select name="drivingBehaviorAlarmVoice" id="edit-frm-drivingBehaviorAlarmVoice">
						  <option  value ="1.mp3">电子警报声</option>
						  <option  value ="3.mp3">红色警报声</option>
						  <option  value ="5.mp3">机动车报警声</option>
						  <option  value ="7.mp3">汽车警报声效</option>
						  <option  value ="8.mp3">遇险报警声</option>
						</select>
					</div>
			   </div>
			   
			   <div class="message_con">
					<label style="width:18%;">紧急重要报警声音开关：</label>
					<input type="radio" value="true"  name="urgentAlarmVoiceEnable"><p>开启</p>
					<input type="radio" value="false" checked="true" name="urgentAlarmVoiceEnable"><p>关闭</p>
				</div>
				<div id="urgentAlarmVoice-box" style="display:none;">
					<div class="message_con">
						<label style="width:18%;">紧急重要报警声音持续时长：</label>
						<input name="urgentAlarmVoiceTime" id="edit-frm-urgentAlarmVoiceTime" maxlength="3" value="3" type="text" class="required digits" placeholder="单位：秒">
					</div>
					
					<div class="message_con">
						<label style="width:18%;">紧急重要报警报警声音：</label>
						<select name="urgentAlarmVoice" id="edit-frm-urgentAlarmVoice">
						  <option  value ="1.mp3">电子警报声</option>
						  <option  value ="3.mp3">红色警报声</option>
						  <option  value ="5.mp3">机动车报警声</option>
						  <option  value ="7.mp3">汽车警报声效</option>
						  <option  value ="8.mp3">遇险报警声</option>
						</select>
					</div>
			   </div>
			   
			   <div class="message_con">
					<label style="width:18%;">ADAS报警声音开关：</label>
					<input type="radio" value="true"  name="adasAlarmVoiceEnable"><p>开启</p>
					<input type="radio" value="false" checked="true" name="adasAlarmVoiceEnable"><p>关闭</p>
				</div>
				<div id="adasAlarmVoice-box" style="display:none;">
					<div class="message_con">
						<label style="width:18%;">ADAS报警声音持续时长：</label>
						<input name="adasAlarmVoiceTime" id="edit-frm-adasAlarmVoiceTime" maxlength="3" value="3" type="text" class="required digits" placeholder="单位：秒">
					</div>
					
					<div class="message_con">
						<label style="width:18%;">ADAS报警报警声音：</label>
						<select name="adasAlarmVoice" id="edit-frm-adasAlarmVoice">
						  <option  value ="1.mp3">电子警报声</option>
						  <option  value ="3.mp3">红色警报声</option>
						  <option  value ="5.mp3">机动车报警声</option>
						  <option  value ="7.mp3">汽车警报声效</option>
						  <option  value ="8.mp3">遇险报警声</option>
						</select>
					</div>
			   </div>
			   
			   <div class="message_con">
					<label style="width:18%;">DSM报警声音开关：</label>
					<input type="radio" value="true"  name="dsmAlarmVoiceEnable"><p>开启</p>
					<input type="radio" value="false" checked="true" name="dsmAlarmVoiceEnable"><p>关闭</p>
				</div>
				<div id="dsmAlarmVoice-box" style="display:none;">
					<div class="message_con">
						<label style="width:18%;">DSM报警声音持续时长：</label>
						<input name="dsmAlarmVoiceTime" id="edit-frm-dsmAlarmVoiceTime" maxlength="3" value="3" type="text" class="required digits" placeholder="单位：秒">
					</div>
					
					<div class="message_con">
						<label style="width:18%;">DSM报警报警声音：</label>
						<select name="dsmAlarmVoice" id="edit-frm-dsmAlarmVoice">
						  <option  value ="1.mp3">电子警报声</option>
						  <option  value ="3.mp3">红色警报声</option>
						  <option  value ="5.mp3">机动车报警声</option>
						  <option  value ="7.mp3">汽车警报声效</option>
						  <option  value ="8.mp3">遇险报警声</option>
						</select>
					</div>
			   </div>
			   

			   <div class="message_con">
					<label style="width:18%;">TPMS报警声音开关：</label>
					<input type="radio" value="true"  name="tpmsAlarmVoiceEnable"><p>开启</p>
					<input type="radio" value="false" checked="true" name="tpmsAlarmVoiceEnable"><p>关闭</p>
				</div>
				<div id="tpmsAlarmVoice-box" style="display:none;">
					<div class="message_con">
						<label style="width:18%;">TPMS报警声音持续时长：</label>
						<input name="tpmsAlarmVoiceTime" id="edit-frm-tpmsAlarmVoiceTime" maxlength="3" value="3" type="text" class="required digits" placeholder="单位：秒">
					</div>
					
					<div class="message_con">
						<label style="width:18%;">TPMS报警报警声音：</label>
						<select name="tpmsAlarmVoice" id="edit-frm-tpmsAlarmVoice">
						  <option  value ="1.mp3">电子警报声</option>
						  <option  value ="3.mp3">红色警报声</option>
						  <option  value ="5.mp3">机动车报警声</option>
						  <option  value ="7.mp3">汽车警报声效</option>
						  <option  value ="8.mp3">遇险报警声</option>
						</select>
					</div>
			   </div>
			   
			   
			   <div class="message_con">
					<label style="width:18%;">BSD报警声音开关：</label>
					<input type="radio" value="true"  name="bsdAlarmVoiceEnable"><p>开启</p>
					<input type="radio" value="false" checked="true" name="bsdAlarmVoiceEnable"><p>关闭</p>
				</div>
				<div id="bsdAlarmVoice-box" style="display:none;">
					<div class="message_con">
						<label style="width:18%;">TPMS报警声音持续时长：</label>
						<input name="bsdAlarmVoiceTime" id="edit-frm-bsdAlarmVoiceTime" maxlength="3" value="3" type="text" class="required digits" placeholder="单位：秒">
					</div>
					
					<div class="message_con">
						<label style="width:18%;">TPMS报警报警声音：</label>
						<select name="bsdAlarmVoice" id="edit-frm-bsdAlarmVoice">
						  <option  value ="1.mp3">电子警报声</option>
						  <option  value ="3.mp3">红色警报声</option>
						  <option  value ="5.mp3">机动车报警声</option>
						  <option  value ="7.mp3">汽车警报声效</option>
						  <option  value ="8.mp3">遇险报警声</option>
						</select>
					</div>
			   </div>
			   
		</fieldset>
		
		<fieldset class="message_fieldset">
			<legend>弹窗开关</legend>
			   <div class="message_con">
					<label style="width:18%;">设备故障报警弹窗开关：</label>
					<input type="radio" value="true"  checked="true" name="faultAlarmDialogEnable"><p>开启</p>
					<input type="radio" value="false"  name="faultAlarmDialogEnable"><p>关闭</p>
			   </div>
			   <div class="message_con">
					<label style="width:18%;">驾驶行为报警弹窗开关：</label>
					<input type="radio" value="true"  checked="true" name="drivingBehaviorAlarmDialogEnable"><p>开启</p>
					<input type="radio" value="false"  name="drivingBehaviorAlarmDialogEnable"><p>关闭</p>
			   </div>
			   <div class="message_con">
					<label style="width:18%;">紧急重要报警弹窗开关：</label>
					<input type="radio" value="true"  checked="true" name="urgentAlarmDialogEnable"><p>开启</p>
					<input type="radio" value="false" name="urgentAlarmDialogEnable"><p>关闭</p>
			   </div>

			    <div class="message_con">
					<label style="width:18%;">ADAS报警弹窗开关：</label>
					<input type="radio" value="true"  checked="true" name="adasAlarmDialogEnable"><p>开启</p>
					<input type="radio" value="false"  name="adasAlarmDialogEnable"><p>关闭</p>
			   </div>
			   <div class="message_con">
					<label style="width:18%;">DSM报警弹窗开关：</label>
					<input type="radio" value="true"  checked="true" name="dsmAlarmDialogEnable"><p>开启</p>
					<input type="radio" value="false"  name="dsmAlarmDialogEnable"><p>关闭</p>
			   </div>
			   <div class="message_con">
					<label style="width:18%;">TPMS报警弹窗开关：</label>
					<input type="radio" value="true"  checked="true" name="tpmsAlarmDialogEnable"><p>开启</p>
					<input type="radio" value="false" name="tpmsAlarmDialogEnable"><p>关闭</p>
			   </div>
			   <div class="message_con">
					<label style="width:18%;">BSD报警弹窗开关：</label>
					<input type="radio" value="true"  checked="true" name="bsdAlarmDialogEnable"><p>开启</p>
					<input type="radio" value="false" name="bsdAlarmDialogEnable"><p>关闭</p>
			   </div>
			   
			    <div class="message_con">
					<label style="width:18%;">告警拍照弹窗开关：</label>
					<input type="radio" value="true" name="mediaAlarmDialogEnable"><p>开启</p>
					<input type="radio" value="false" checked="true" name="mediaAlarmDialogEnable"><p>关闭</p>
			   </div>
			   
			   <div class="message_con">
					<label style="width:18%;">上下线弹窗开关：</label>
					<input type="radio" value="true"  name="onOffLineDialogEnable"><p>开启</p>
					<input type="radio" value="false" checked="true"  name="onOffLineDialogEnable"><p>关闭</p>
			   </div>
		</fieldset>

		<fieldset class="message_fieldset">
			<legend>关键告警设置（需要人工确认的告警）</legend>
			<div class="message_con">
				<label>设备故障报警：</label>
				<p style="padding-left: 0;" id="device-fault-key-alarm"></p>
			</div>
			
			<div class="message_con">
				<label>驾驶行为报警：</label>
				<p style="padding-left: 0;" id="driving-behavior-key-alarm"></p>
			</div>
			
			<div class="message_con">
				<label>紧急重要报警：</label>
				<p style="padding-left: 0;" id="car-urgent-key-alarm"></p>
			</div>
			
			<div class="message_con">
				<label>ADAS报警：</label>
				<p style="padding-left: 0;" id="adas-key-alarm"></p>
			</div>
			<div class="message_con">
				<label>DSM报警：</label>
				<p style="padding-left: 0;" id="dsm-key-alarm"></p>
			</div>
			<div class="message_con">
				<label>TPMS报警：</label>
				<p style="padding-left: 0;" id="tpms-key-alarm"></p>
			</div>
			<div class="message_con">
				<label>BSD报警：</label>
				<p style="padding-left: 0;" id="bsd-key-alarm"></p>
			</div>
		</fieldset>
		
		<fieldset class="message_fieldset">
			<legend>其他告警规则</legend>
			   <div class="message_con">
					<label>急加速：</label>
					<input name="acceleration" id="edit-frm-acceleration" maxlength="3" type="text" class="digits" placeholder="急加速检测设置，单位km/h，加速度超过此设置则认为是急加速">
				</div>
			   <div class="message_con">
					<label>急刹车：</label>
					<input name="deceleration" id="edit-frm-deceleration" maxlength="3" type="text" class="digits" placeholder="急刹车检测设置，单位km/h，加速度超过此设置则认为是急刹车">
				</div>
				<div class="message_con">
					<label>急转弯：</label>
					<input name="turning" id="edit-frm-turning" maxlength="3" type="text" class="digits" placeholder="急转弯检测检测设置，单位km/h，以较高速度转弯或者调头则认为是急转弯">
				</div>
				
				<div class="message_con">
					<label>停车规则：</label>
					<input name="parkingTime" id="edit-frm-parkingTime" maxlength="3" type="text" class="digits" placeholder="停车时长计算默认规则，acc开并且n分钟内车辆未动视为停车。">
				</div>
				
				<div class="message_con">
					<label>油量异常：</label>
					<input name="oilmass" id="edit-frm-oilmass" maxlength="3" type="text" class="digits" placeholder="偷油漏油疑点设置，单位L/m,默认单位分钟内油量减少大于1升,油量异常告警">
				</div>
		</fieldset>
	</section>
	
	
	<div class="message_footer">
		<button type="button" class="bule" onclick="save()">保存</button>
	</div>
    </form>
    
    <div id="play-audio-dlg"  class="simple-dialog" title="播放" style="width:300px; height:56px;overflow:hidden;">
	  <audio id="play-audio" controls="controls" autoplay="autoplay">亲 您的浏览器不支持html5的audio标签</audio>
	</div>
	<%@ include file="/common/common.jsp"%>
	<script type="text/javascript" src="<c:url value='/js/videojs/video.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/device/alarmNotifyRule.js?v=20191212'/>"></script>
</body>
</html>