var isLoadMapSuccess = false;
var alarmDetail = {};

function init(){
	var param = {};
	var conditions = {};
	conditions["id.eq"] = $("#id").val();
	param["conditions"] = conditions;
	var url = lbs_api_server_servlet_path + "/common/query/alarm.json?pageSize=1&pageNo=1&countable=false";
	ajaxAsyncPost(url, param, function(result) {
		endLoading();
		if (result.code != 0) {
			showErrorMessage(result.message);
			return;
		}
		var list = result.data;
		if (list && list.length > 0) {
			var item = list[0];
			
			if(isLoadMapSuccess){
				if(item.gps){
					var arr = item.gps.split(",");
					var gcj02 = LngLatConverter.wgs84togcj02(arr[0], arr[1]);
					var lacation = mapUtil.getPoint(gcj02[0], gcj02[1]);
					mapUtil.regeocoder(lacation, function(address) {
						$("#speed").html(item.speed);
						$("#address").html(address);
					});
				}
	
				if(item.gpsId){
					setLocation(item.deviceId,item.gpsId);
				}
			}else{
				alarmDetail = item;
			}
			
			var deviceName = getDeviceName(item.deviceId);
			setAttachmentList(item.deviceId,item.gpsId || item.id);
			$("#plateNo").html(deviceName);
			$("#alarmTime").html(new Date(item.alarmTime).format());
			
			for(var key in item){
				if(key.indexOf("et_")!=-1){
					$("#alarmEndTime").html(new Date(item[key]).format());
					break;
				}
			}
			
			if(item.opinion){
				$("#alarmOpinion").html(item.opinion);
			}else{
				if(item.k && !item.state){
					$("#alarmOpinion").html("告警未确认");
					$("#alarm-confirm-btn").show();
				}else{
					if(item.state){
						$("#alarmOpinion").html("告警已确认");
					}else{
						$("#alarmOpinion").html("告警未确认");
					}
				}
			}
			var alarmStr="";
			
			//*************苏标告警处理 start***************
			for(var i=0;i<adasAlarmDesc.length;i++){
				var s = adasAlarmDesc[i];
				if(s && item["adas"+(i+1)]){
					alarmStr += "," + s;
				}
			}
			
			for(var i=0;i<dsmAlarmDesc.length;i++){
				var s = dsmAlarmDesc[i];
				if(s && item["dsm"+(i+1)]){
					alarmStr += "," + s;
				}
			}
			
			//20个轮胎足够了
			for(var i=0;i<20;i++){
				var alarm = item["tpm"+i];
				if(alarm){
					for (var j = 0; j < tpmAlarmDesc.length; j++) {
						var s = "";
						if((((alarm&(1<<j))>>j)==1)) {
							s = tpmAlarmDesc[j];
						}
						if (s!="") {
							alarmStr += "," + s;
						}
					}
				}
			}
			
			for(var i=0;i<bsdAlarmDesc.length;i++){
				var s = bsdAlarmDesc[i];
				if(s && item["bsd"+(i+1)]){
					alarmStr += "," + s;
				}
			}
			//*************苏标告警处理 end***************
			
			//1078视频告警
			if(item["va"]){
				var videoAlarm = item["va"];
				for(var i=0;i<videoAlarmDesc.length;i++){
					var s = videoAlarmDesc[i];
					if(s && ((videoAlarm&(1<<i))>>i)==1 ){
						alarmStr += "," + s;
					}
				}
			}

			for (var i = 0; i < 50; i++) {
				var alarmType = carAlarmInfoMap.get(i);
				if (!alarmType) {
					continue;
				}
				var val = item["a" + i];
				if (val) {
					alarmStr += "," + alarmType;
				}
			}

			if(alarmStr==""){
				alarmStr=",用户自定义报警";
			}
			
			alarmStr = alarmStr.substr(1);
			
			$("#detail").html(alarmStr);
			
			$("#alarm-confirm-dlg-frm-alarmId").val(item.id);
			if(item.alarmEventId){
				$("#alarm-confirm-dlg-frm-alarmEventId").val(item.alarmEventId);
			}
			$("#alarm-confirm-dlg-frm-deviceName").html(deviceName);
			$("#alarm-confirm-dlg-frm-alarmDetail").html(alarmStr);
		}
	});
}

function onLoadMapSuccess(){
	isLoadMapSuccess = true;
	if(alarmDetail){
		if(alarmDetail.gps){
			var arr = alarmDetail.gps.split(",");
			var gcj02 = LngLatConverter.wgs84togcj02(arr[0], arr[1]);
			var lacation = mapUtil.getPoint(gcj02[0], gcj02[1]);
			mapUtil.regeocoder(lacation, function(address) {
				$("#speed").html(alarmDetail.speed);
				$("#address").html(address);
			});
		}
	
		if(alarmDetail.gpsId){
			setLocation(alarmDetail.deviceId,alarmDetail.gpsId);
		}
	}
}

function doAlarmConfirm(){
	var form =$("#alarm-confirm-dlg-frm").serializeObject();
	var url = "/common/alarm/" + form.alarmId + ".json";
	var data = {};
	data.state = 1;
	if(form.opinion){
		data.opinion = form.opinion;
	}
	data.confirmTime = new Date().getTime();
	data.confirmUserId="${sessionScope.user.userId}";
	ajaxAsyncPatch(url, data, function(result) {
		if (result.code != 0) {
			showErrorMessage(result.message);
		} else {
			if(data.eid && isOnline(data.deviceId)){
				//发送告警确认指令
				var cmd={};
				cmd.desc="人工确认报警消息";
				cmd.messageId="8203";
				cmd.messageSeq=data.eid;
				cmd.alarmType=409993225;//11000011100000000000000001001
				doSendCmd(data.deviceId, cmd);
			}
			closeDialog();
			goback();
		}
	},true);
}

function setLocation(deviceId,gpsId) {
	var param = {};
	param["recordDate"] = $("#recordDate").val();
	var conditions = {};
	conditions["id.eq"] = gpsId;
	conditions["deviceId.eq"] = deviceId;
	param["conditions"] = conditions;

	var url = lbs_api_server_servlet_path + "/common/query/gps.json?select=lng,lat,speed&pageSize=1&pageNo=1&countable=false";
	ajaxAsyncPost(url, param, function(result) {
		if (result.code != 0) {
			showErrorMessage(result.message);
			return;
		}
		var list = result.data;
		if (list && list.length > 0) {
			var item = list[0];
			var gcj02 = LngLatConverter.wgs84togcj02(item.lng, item.lat);
			var lacation = mapUtil.getPoint(gcj02[0], gcj02[1]);
			mapUtil.regeocoder(lacation, function(address) {
				$("#speed").html(item.speed);
				$("#address").html(address);
			});
		} else {
			$("#address").html("无相关位置信息");
		}
	});
}

function setAttachmentList(deviceId,gpsId){
	var param = {};
	param["deviceId.eq"] = deviceId;
	param["bizId.eq"] = gpsId;
	var url = lbs_api_server_servlet_path + "/common/query/deviceHistoryMedia.json";
	ajaxAsyncPost(url, param, function(result) {
		if (result.code != 0) {
			showErrorMessage(result.message);
			return;
		}
		var list = result.data;
		var attachmentList = "";
		var count = 1;
		for(var i=0;i<list.length;i++){
			var item = list[i];
			var filePath = item.filePath;
			if(filePath){
				filePath = filePath.replace(/\\/g,"\/");
			}
			//多媒体类型,0：音视频，1：音频，2：视频，3：视频音频；4：照片;5：文本附件；6：其他
			var resourceType = item.resourceType;
			var opt = '<a href="javascript:void(0)" class="table_view" title="下载" onclick="downloadMedia(\''+ item.id + '\',\''+ filePath + '\')">下载</a>';
			var icon = "../images/icons/";
			if(resourceType==1){
				icon += "music.png";
			}else if(resourceType==4){
				icon = getMediaUrl(item.id,item.filePath);
			}else if(resourceType==5){
				icon += "txt.png";
			}else if(resourceType==6){
				icon += "unknown.png";
			}else{
				icon += "media.png";
			}
			
			attachmentList+="<div style='float:left; display:inline;margin-top:25px;'><div  onclick=\"view('"+ item.id + "','"+resourceType+"','"+ filePath + "')\"><img style='width:125px;height:125px;' src='"+icon+"'/></div><div style='text-align:center;margin-top:15px;'>"+opt+"</div></div>";
		}
		if(!attachmentList){
			attachmentList="无相关告警附件";
		}
		$("#attachmentList").html(attachmentList);
	});
}

function view(id,resourceType,filePath){
	//多媒体类型,0：音视频，1：音频，2：视频，3：视频音频；4：照片;5：文本附件；6：其他
	var mediaUrl = getMediaUrl(id,filePath);
	if(resourceType==1){
		playAudio(mediaUrl);
	}else if(resourceType==4){
		showImg("",mediaUrl);
	}else if(resourceType==5){
		//下载
		downloadMedia(id,filePath);
	}else if(resourceType==6){
		//下载
		downloadMedia(id,filePath);
	}else{
		playVideo(mediaUrl);
	}
}

function downloadMedia(id,filePath){
	var mediaUrl = getMediaUrl(id,filePath);
	window.open(mediaUrl);
}

function getMediaUrl(id,filePath){
	var mediaUrl = null;
	if(getFileServer()){//如果配置了文件服务器，则直接从文件服务器下载
		mediaUrl = getFileServer()+filePath;
	}else{
		//从ftp服务器下载
		mediaUrl = getContextPath() +"/attachment/download.do?url=/media/ftp/download/"+id+".json";
	}
	return mediaUrl;
}

function playAudio(mediaUrl) {
	var audio = $('audio').get(0);
	audio.src = mediaUrl;
	audio.load();
	audio.play();
	var dialogId = "play-audio-dlg";
	var title = $("#"+dialogId).attr("title");
	showDialog(title, dialogId,function() {
		try {
			audio.pause();
		} catch (e) {
		}
	});
}

var player = null;

function playVideo(mediaUrl) {
	var sources = [ {
		type : "video/mp4",
		src : mediaUrl
	} ];
	if(player==null){
		player = videojs('play-video');
	}
	
	player.src(sources);
	player.load(mediaUrl);
	player.play();

	var dialogId = "play-video-dlg";
	var title = $("#"+dialogId).attr("title");
	showDialog(title, dialogId,function() {
		try {
			player.reset();
		} catch (e) {
		}
	});
}

function goback(){
	  var url = "alarmList.do?isCacheQuery=true"; 
	  if($("#type").val()){
		  url = "mapAlarmList.do?isCacheQuery=true"; 
	  }
	  window.location.href = url;
	}