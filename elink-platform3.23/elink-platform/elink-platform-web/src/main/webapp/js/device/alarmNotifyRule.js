var checkboxStyle = " style='margin-left:5px; margin-right:0px;' "
		
$(document).ready(function() {
	loadEditFrmData($("#edit-frm-id").val());
	
	$("input:radio[name=faultAlarmVoiceEnable]").on("click",function(){
		if($(this).val()=="true"){
			$("#faultAlarmVoice-box").show();
		}else{
			$("#faultAlarmVoice-box").hide();
		}
	});
	
	$("input:radio[name=drivingBehaviorAlarmVoiceEnable]").on("click",function(){
		if($(this).val()=="true"){
			$("#drivingBehaviorAlarmVoice-box").show();
		}else{
			$("#drivingBehaviorAlarmVoice-box").hide();
		}
	});
	
	
	$("input:radio[name=urgentAlarmVoiceEnable]").on("click",function(){
		if($(this).val()=="true"){
			$("#urgentAlarmVoice-box").show();
		}else{
			$("#urgentAlarmVoice-box").hide();
		}
	});
	
	$("input:radio[name=adasAlarmVoiceEnable]").on("click",function(){
		if($(this).val()=="true"){
			$("#adasAlarmVoice-box").show();
		}else{
			$("#adasAlarmVoice-box").hide();
		}
	});
	
	$("input:radio[name=dsmAlarmVoiceEnable]").on("click",function(){
		if($(this).val()=="true"){
			$("#dsmAlarmVoice-box").show();
		}else{
			$("#dsmAlarmVoice-box").hide();
		}
	});
	
	$("input:radio[name=tpmsAlarmVoiceEnable]").on("click",function(){
		if($(this).val()=="true"){
			$("#tpmsAlarmVoice-box").show();
		}else{
			$("#tpmsAlarmVoice-box").hide();
		}
	});
	
	$("input:radio[name=bsdAlarmVoiceEnable]").on("click",function(){
		if($(this).val()=="true"){
			$("#bsdAlarmVoice-box").show();
		}else{
			$("#bsdAlarmVoice-box").hide();
		}
	});
	
	
	$("#edit-frm-faultAlarmVoice").change(function() {
		var val = $(this).val();
		playAudio(val);
	});
	
	$("#edit-frm-drivingBehaviorAlarmVoice").change(function() {
		var val = $(this).val();
		playAudio(val);
	});
	
	$("#edit-frm-urgentAlarmVoice").change(function() {
		var val = $(this).val();
		playAudio(val);
	});

	$("#edit-frm-adasAlarmVoice").change(function() {
		var val = $(this).val();
		playAudio(val);
	});
	
	$("#edit-frm-dsmAlarmVoice").change(function() {
		var val = $(this).val();
		playAudio(val);
	});
	
	$("#edit-frm-bsdAlarmVoice").change(function() {
		var val = $(this).val();
		playAudio(val);
	});
	
	$("#edit-frm-tpmsAlarmVoice").change(function() {
		var val = $(this).val();
		playAudio(val);
	});
});

function getApiName(){
	return "enterpriseConfig";
}

function initAlarmSetting(result){
	var alarmflag = "1111111111111111111111111111111111";
	var alarm="00000000000000000000000000000000",adas="",dsm="",tpms="",bsd="";
	
	if(result.alarmSetting){
		var alarmSetting =$.evalJSON(result.alarmSetting);
		var availableAlarm = alarmSetting.availableAlarm;
		if(availableAlarm){
			alarmflag = availableAlarm.toString(2);
		}

		if(alarmSetting.faultAlarmVoiceEnable=="true"){
			$("#faultAlarmVoice-box").show();
		}

		if(alarmSetting.drivingBehaviorAlarmVoiceEnable=="true"){
			$("#drivingBehaviorAlarmVoice-box").show();
		}

		if(alarmSetting.urgentAlarmVoiceEnable=="true"){
			$("#urgentAlarmVoice-box").show();
		}
		
		if(alarmSetting.adasAlarmVoiceEnable=="true"){
			$("#adasAlarmVoice-box").show();
		}
		
		if(alarmSetting.dsmAlarmVoiceEnable=="true"){
			$("#dsmAlarmVoice-box").show();
		}
		
		if(alarmSetting.tpmsAlarmVoiceEnable=="true"){
			$("#tpmsAlarmVoice-box").show();
		}
		
		if(alarmSetting.bsdAlarmVoiceEnable=="true"){
			$("#bsdAlarmVoice-box").show();
		}

		for ( var key in alarmSetting) {
			$("#edit-frm-" + key).val(alarmSetting[key]);
			try{
				$("input[type=radio][name="+key+"][value="+alarmSetting[key]+"]").attr("checked",'checked');
			}catch(e){};
		}

		var keyAlarm = alarmSetting.keyAlarm;
		if(keyAlarm){
			alarm = (keyAlarm.alarm).toString(2);
			adas = keyAlarm.adas?keyAlarm.adas:"";
			dsm = keyAlarm.dsm?keyAlarm.dsm:"";
			tpms = keyAlarm.tpms?keyAlarm.tpms:"";
			bsd = keyAlarm.bsd?keyAlarm.bsd:"";
		}
	}
	
	initAlarmCheckbox("device-fault-key-alarm",alarm,devicefaultAlarmInfoMap,"keyAlarmFlag");
	initAlarmCheckbox("driving-behavior-key-alarm",alarm,drivingBehaviorAlarmInfoMap,"keyAlarmFlag");
	initAlarmCheckbox("car-urgent-key-alarm",alarm,carUrgentAlarmInfoMap,"keyAlarmFlag");

	initTjsatl("adas-key-alarm",adas,adasAlarmDesc,1,"adasKeyAlarm");
	initTjsatl("dsm-key-alarm",dsm,dsmAlarmDesc,1,"dsmKeyAlarm");
	initTjsatl("tpms-key-alarm",tpms,tpmAlarmDesc,0,"tpmsKeyAlarm");
	initTjsatl("bsd-key-alarm",bsd,bsdAlarmDesc,1,"bsdKeyAlarm");
	
	while(alarmflag.length < 32){
		alarmflag = "0"+alarmflag;
	}
	initAlarmCheckbox("device-fault-alarm",alarmflag,devicefaultAlarmInfoMap,"alarmflag");
	initAlarmCheckbox("driving-behavior-alarm",alarmflag,drivingBehaviorAlarmInfoMap,"alarmflag");
	initAlarmCheckbox("car-urgent-alarm",alarmflag,carUrgentAlarmInfoMap,"alarmflag");
}

function initTjsatl(id,type,desc,startIndex,name){
	var arr = type.split(",");
	var html= "";
	for(var i=0;i<desc.length;i++){
		var s =desc[i];
		if(s!="用户自定义报警"){
			var bool = false;
			for(var j=0;j<arr.length;j++){
				var val = arr[j];
				if((i+startIndex)==val){
					bool=true;
					html+="<input "+checkboxStyle+" name='"+name+"' value='"+(i+startIndex)+"' type='checkbox' checked=true>"+s;
				}
			}
			if(!bool){
				html+="<input "+checkboxStyle+" name='"+name+"' value='"+(i+startIndex)+"' type='checkbox'>"+s;
			}
		}
	}
	$("#"+id).html(html);
}

function initAlarmCheckbox(id,alarmflag,map,name){
	var html = "";
	var keys = map.getKeys();
	for(var i=0,l=keys.length;i<l;i++){
		var key = keys[i];
		var s = map.get(key);
		if(alarmflag.charAt(31-key)=='1'){
			html+="<input "+checkboxStyle+" name='"+name+"' value='"+key+"' type='checkbox' checked=true>"+s;
		}else{
			html+="<input "+checkboxStyle+" name='"+name+"' value='"+key+"' type='checkbox'>"+s;
		}
	}
	$("#"+id).html(html);
}

function loadEditFrmDataSuccessHandler(result) {
	initAlarmSetting(result);
}


function getEditFormData(){
	var data = $("#edit-frm").serializeObject();
	
	var alarmflagMap = new JsMap();
	$("input[name='alarmflag']:checked").each(function(){
		alarmflagMap.put(parseInt(this.value),"1");                        
    });
	
	var availableAlarm = "";
	for(var i=0;i<32;i++){
		if(alarmflagMap.get(i)){
			availableAlarm ="1" + availableAlarm;
		}else{
			availableAlarm ="0" + availableAlarm;
		}
	}
	
	var alarmSetting = {};
	alarmSetting.availableAlarm = parseInt(availableAlarm,2);

	//声音开关
	if(data.faultAlarmVoiceEnable=="true"){
		alarmSetting.faultAlarmVoiceEnable = data.faultAlarmVoiceEnable;
		alarmSetting.faultAlarmVoiceTime = parseInt(data.faultAlarmVoiceTime);
		alarmSetting.faultAlarmVoice = data.faultAlarmVoice;
	}
	
	if(data.drivingBehaviorAlarmVoiceEnable=="true"){
		alarmSetting.drivingBehaviorAlarmVoiceEnable = data.drivingBehaviorAlarmVoiceEnable;
		alarmSetting.drivingBehaviorAlarmVoiceTime = parseInt(data.drivingBehaviorAlarmVoiceTime);
		alarmSetting.drivingBehaviorAlarmVoice = data.drivingBehaviorAlarmVoice;
	}
	
	if(data.urgentAlarmVoiceEnable=="true"){
		alarmSetting.urgentAlarmVoiceEnable = data.urgentAlarmVoiceEnable;
		alarmSetting.urgentAlarmVoiceTime = parseInt(data.urgentAlarmVoiceTime);
		alarmSetting.urgentAlarmVoice = data.urgentAlarmVoice;
	}
	
	if(data.adasAlarmVoiceEnable=="true"){
		alarmSetting.adasAlarmVoiceEnable = data.adasAlarmVoiceEnable;
		alarmSetting.adasAlarmVoiceTime = parseInt(data.adasAlarmVoiceTime);
		alarmSetting.adasAlarmVoice = data.adasAlarmVoice;
	}
	
	if(data.dsmAlarmVoiceEnable=="true"){
		alarmSetting.dsmAlarmVoiceEnable = data.dsmAlarmVoiceEnable;
		alarmSetting.dsmAlarmVoiceTime = parseInt(data.dsmAlarmVoiceTime);
		alarmSetting.dsmAlarmVoice = data.dsmAlarmVoice;
	}
	
	if(data.tpmsAlarmVoiceEnable=="true"){
		alarmSetting.tpmsAlarmVoiceEnable = data.tpmsAlarmVoiceEnable;
		alarmSetting.tpmsAlarmVoiceTime = parseInt(data.tpmsAlarmVoiceTime);
		alarmSetting.tpmsAlarmVoice = data.tpmsAlarmVoice;
	}
	
	if(data.bsdAlarmVoiceEnable=="true"){
		alarmSetting.bsdAlarmVoiceEnable = data.bsdAlarmVoiceEnable;
		alarmSetting.bsdAlarmVoiceTime = parseInt(data.bsdAlarmVoiceTime);
		alarmSetting.bsdAlarmVoice = data.bsdAlarmVoice;
	}

	//弹窗开关
	alarmSetting.faultAlarmDialogEnable = data.faultAlarmDialogEnable;
	alarmSetting.drivingBehaviorAlarmDialogEnable = data.drivingBehaviorAlarmDialogEnable;
	alarmSetting.urgentAlarmDialogEnable = data.urgentAlarmDialogEnable;
	
	alarmSetting.adasAlarmDialogEnable = data.adasAlarmDialogEnable;
	alarmSetting.dsmAlarmDialogEnable = data.dsmAlarmDialogEnable;
	alarmSetting.tpmsAlarmDialogEnable = data.tpmsAlarmDialogEnable;
	alarmSetting.bsdAlarmDialogEnable = data.bsdAlarmDialogEnable;
	
	alarmSetting.mediaAlarmDialogEnable = data.mediaAlarmDialogEnable;
	alarmSetting.onOffLineDialogEnable = data.onOffLineDialogEnable;
	
	//关键告警
	var keyAlarm = {};
	alarmflagMap = new JsMap();
	$("input[name='keyAlarmFlag']:checked").each(function(){
		alarmflagMap.put(parseInt(this.value),"1");                        
    });
	
	var alarm = "";
	for(var i=0;i<32;i++){
		if(alarmflagMap.get(i)){
			alarm ="1" + alarm;
		}else{
			alarm ="0" + alarm;
		}
	}
	keyAlarm.alarm = parseInt(alarm,2);
	
	var alarm = "";
	$("input[name='adasKeyAlarm']:checked").each(function(){
		alarm+=","+this.value;                        
    });
	if(alarm){
		keyAlarm.adas=alarm.substr(1);
	}
	
	var alarm = "";
	$("input[name='dsmKeyAlarm']:checked").each(function(){
		alarm+=","+this.value;                        
    });
	if(alarm){
		keyAlarm.dsm=alarm.substr(1);
	}
	
	var alarm = "";
	$("input[name='tpmsKeyAlarm']:checked").each(function(){
		alarm+=","+this.value;                        
    });
	if(alarm){
		keyAlarm.tpms=alarm.substr(1);
	}
	
	var alarm = "";
	$("input[name='bsdKeyAlarm']:checked").each(function(){
		alarm+=","+this.value;                        
    });
	if(alarm){
		keyAlarm.bsd=alarm.substr(1);
	}
	alarmSetting.keyAlarm=keyAlarm;
	
	if(data.acceleration){
		alarmSetting.acceleration = parseInt(data.acceleration);
	}
	
	if(data.deceleration){
		alarmSetting.deceleration = parseInt(data.deceleration);
	}
	
	if(data.turning){
		alarmSetting.turning = parseInt(data.turning);
	}
	
	if(data.parkingTime){
		alarmSetting.parkingTime = parseInt(data.parkingTime);
	}
	
	if(data.oilmass){
		alarmSetting.oilmass = parseInt(data.oilmass);
	}
	
	data.alarmSetting=$.toJSON(alarmSetting);
	return data;
}

function saveResult(result) {
	if (result.code != 0) {
		showErrorMessage(result.message);
	} else {
		showMessage("设置成功");
		try{
			window.top.loadAlarmVoiceSetting();
		}catch(e){}
	}
}

function playAudio(file) {
	var audio = $('audio').get(0);
	audio.src = "/voice/"+file;
	audio.load();
	audio.play();
}