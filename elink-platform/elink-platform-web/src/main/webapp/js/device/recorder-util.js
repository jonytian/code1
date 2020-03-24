var recSeq=0,rec,talkSimCode,talkChannel,talkDeviceId,audioClient,recTimestamp=0;
var ws_talk_server_url = "ws://localhost:28090/ws/audio";

$(function() {
	loadWSTalkServer();
});

function str2bytes(str){  
    var ch, st, re = []; 
    for (var i = 0; i < str.length; i++ ) { 
        ch = str.charCodeAt(i); 
        st = [];
       do {  
            st.push( ch & 0xFF );
            ch = ch >> 8; 
        }
        while ( ch );
        re = re.concat(st.reverse()); 
    }
    return re;  
}
 
function bcd2byte(asc) {
    var bcd;
    if ((asc >= 48) && (asc <= 57))
        bcd = (asc - 48);
    else if ((asc >= 65) && (asc <= 70))
        bcd = (asc - 65 + 10);
    else if ((asc >= 97) && (asc <= 102))
        bcd = (asc - 97 + 10);
    else
        bcd = (asc - 48);
    return bcd;
}

function bcd2bytes(ascii, len) {
    var bcd = [];
    var j = 0;
    for (var i = 0; i < len / 2; i++) {
        bcd.push(bcd2byte(ascii[j++]));
        bcd[i] = (((j >= len) ? 0x00 : bcd2byte(ascii[j++])) + (bcd[i] << 4));
    }
    return bcd;
}

function send1078VoiceMessage(simCode,channel,data,flag,time){
	var dataLength = data.length;
	var buffer = new ArrayBuffer(26 + dataLength * 2);
    var view = new DataView(buffer);
	
	var offset = 0;
	// 帧头标识，以大端字节序写入值为0x30 0x31 0x63 0x64的32位整数
	view.setUint32(offset, 808543076, false);
	offset += 4;
	
	//00010010
	view.setUint8(offset, 129, false);
	offset += 1;

	//var m = 128;//是否完整帧标识，10000000
	//var pt = 22;//负载类型，10110
	view.setUint8(offset, 150, false);
	offset += 1;
	
	//包序号
	view.setUint16(offset, recSeq++, false);
	offset += 2;
	
	//sim卡号
	//var simCode = "013600000000";
	var arr = bcd2bytes(str2bytes(simCode),12);
	for(var j=0;j<arr.length;j++){
		view.setUint8(offset, arr[j], false);
		offset += 1;
	}
	
	//逻辑通道号
	view.setUint8(offset, channel, false);
	offset += 1;
	
	//数据类型，00110000 + 分包标识
	view.setUint8(offset, flag+48, false);
	offset += 1;

	//时间戳
	view.setBigUint64(offset, BigInt(time), false);
	offset += 8;

	//数据长度
	view.setUint16(offset, dataLength * 2, false);
	offset += 2;

	for(var j=0;j<dataLength;j++){
		view.setInt16(offset, data[j], true);//小尾端
		offset += 2;
	}

	audioClient.send(buffer);
}

function recopen(){
	recclose();
	rec=Recorder({
		onProcess:function(buffers,level,duration,sampleRate){
			$("#video_talker_voice").css("width",level+"%");
			
			var simCode=talkSimCode,channel=talkChannel;
			recTimestamp +=duration
			//这里封装1078协议
			send1078VoiceMessage(simCode,channel,buffers,0,recTimestamp);
		}
	});

	rec.open(function(){
		//开启录音
		doStartTalk();
	},function(e,isUserNotAllow){
		alert((isUserNotAllow?"用户不允许录音，":"")+"打开失败："+e);
	});
	
	window.waitDialogClick=function(){
		alert("打开麦克风失败：权限请求被忽略，<span style='color:#f00'>用户主动点击的弹窗</span>");
	};
};

function recclose(){
	if(rec){
		rec.close(function(){
			reclog("已关闭");
		});
	}else{
		reclog("未打开录音",1);
	};
};

function recstart(call){
	call||(call=function(msg){
		msg&&reclog(msg,1);
	});
	if(rec){
		window.realTimeSendTryReset&&realTimeSendTryReset();
		
		rec.start();
		var set=rec.set;

		alert("请您开始说话……");
		reclog("录制中："+set.type+" "+set.sampleRate+"hz "+set.bitRate+"kbps");
		call();
	}else{
		call("未打开录音");
	};
};

function recpause(){
	if(rec){
		rec.pause();
		reclog("已暂停");
	};
};

function recresume(){
	if(rec){
		rec.resume();
		reclog("继续录音中...");
	};
};

function recstop(call){
	recstopFn(call,true,function(){
		setTimeout(function(){
			window.realTimeSendTryStop&&realTimeSendTryStop(rec.set);
		});
	});
};

function recstopFn(call,isClick,endCall,rec){
	call||(call=function(msg){
		msg&&reclog(msg,1);
	});
	rec=rec||window.rec;
	if(rec){
		if(isClick){
			reclog("正在编码"+rec.set.type+"...");
		};
		var t1=Date.now();
		rec.stop(function(blob,time){
			
		},function(s){
			endCall(s);
			call("失败："+s);
		});
	}else{
		call("未打开录音");
	};
};

function reclog(s,e){
	console.info(s);
};

function onclickStartTalk(deviceId){
	if(talkDeviceId){
		alert("当前车辆语音对讲已开启，语音对讲只支持一对一通话！");
		return;
	}
	if(window.isSecureContext===false){
	   alert("当前网页不是安全环境（HTTPS），将无法获取录音权限，<a href='https://developer.mozilla.org/en-US/docs/Web/API/MediaDevices/getUserMedia#Privacy_and_security'>MDN Privacy and security</a>",1);
	   return;
	}
	
	hideRMenu();
	if(deviceId){
		//发送语音对讲指令
		talkDeviceId = deviceId;
		talkChannel = 1;
	}else{
		if(curent_car_node){
			if(curent_car_node.type=="car"){
				talkChannel = 1;
				talkDeviceId = curent_car_node.id;
			}else if(curent_car_node.type=="ch"){
				var arr = (curent_car_node.id).split("_");
				talkChannel = arr[1];
				talkDeviceId = arr[0];
			}
		}
	}
	removeInfowindow();

	var carInfo = getDeviceCache(talkDeviceId);
	//打开录音
	talkSimCode=carInfo.simCode;
	recopen();
}

var stop_talk_flag = false;
function onclickStopTalk(){
	audioClient.close();
	recclose();
	stopVideo(talkDeviceId,talkChannel);
	$("#talk_control").hide();
	alert("语音对讲已结束……");
	recSeq=0;
	recTimestamp=0;
	talkSimCode=null;
	talkChannel=null;
	talkDeviceId=null;
	audioClient=null;
	stop_talk_flag = true;
}

function doStartTalk(){
	startLoading();
	audioClient = new WSClient({
	    host: ws_talk_server_url,
	    onopen: function() {
	        //发送对讲指令->指令成功->开始对讲
	    	//        ->指令失败->关闭websocket
	    	//console.log("*******onopen");
	    	
	    	//发送指令
	    	if(startVideo(talkDeviceId,talkChannel,2)){
	    		$("#talk_control").show();
	    		recstart();
	    	}
	    	stop_talk_flag = false;
	    	endLoading();
	    },
	    onclose: function() {
	    	if(!stop_talk_flag){
		    	alert("连接语音对讲服务器失败，请联系系统管理员！");
		    	recclose();
		    	stopVideo(talkDeviceId,talkChannel);
		    	$("#talk_control").hide();
		    	recSeq=0;
		    	recTimestamp=0;
		    	talkSimCode=null;
		    	talkChannel=null;
		    	talkDeviceId=null;
		    	audioClient=null;
		    	endLoading();
	    	}
	    },
	    onmessage: function(arraybuffer) {
	    	//接收数据
	    	//console.log("*******onmessage");
	    },
	    onerror: function(){
	    	alert("连接语音对讲服务器失败，请联系系统管理员！");
	    	talkChannel=null;
	    	talkDeviceId=null;
	    	endLoading();
	    }
	});
	
	audioClient.initialize();
}


function loadWSTalkServer(){
	var params = {};
	params["type.eq"]= DICTIONARY_PROVIDER_API;
	params["code.eq"]= "1078-talk";
	var url = management_api_server_servlet_path + "/system/dictionary/dictionary.json";
	var result = ajaxSyncPost(url, params);
	if (result.code != 0) {
		showErrorMessage(result.message);
	} else {
		var list = result.data;
		if(list && list.length>0){
			var item = list[0];
			var content = $.evalJSON(item.content);
			ws_talk_server_url = content.url;
		}else{
			showMessage("无可用语音对讲服务器，请联系系统管理员配置！");
		}
	}
}

function startTalk(deviceId,channelId,dataType){
	if(ws_talk_server_url==""){
		showErrorMessage("无可用语音对讲服务器，请联系系统管理员配置！");
		return false;
	}
	
	if(isOnline(deviceId)){
		if(checkVideo(deviceId,channelId)){
			//启动视频
			if (videoList.length >= MAX_VEDEO) {
				showErrorMessage("最大支持" + MAX_VEDEO + "音视频频直播!");
				return false;
			}

			var url = message_api_server_servlet_path + "/talk/1078/" + deviceId + ".json?clinetId=" + clinetId;
			var data = {};
			data.desc = "语音对讲";
			data.deviceId = deviceId;
			data.messageId = "9101";
			data.channelId = channelId;
			if(typeof(dataType) == "undefined"){
				data.dataType = 0;
			}else{
				data.dataType = dataType;
			}
			data.streamType = 1;

			var videoInfo = {};
			var device = getDeviceCache(deviceId);
			videoInfo.deviceId = deviceId;
			videoInfo.simCode = device.simCode;
			videoInfo.deviceName = device.name;
			videoInfo.channelId = channelId;
			videoInfo.dataType = data.dataType;
			videoInfo.streamType = data.streamType;
			
			data.name = device.name;
			var result = ajaxSyncPost(url, data);
			if (result.code != 0) {
				showErrorMessage(result.message);
				return false;
			} else {
				var result = result.data;
				setCmdInfo(result);
				startHeartbeat();
				var simCode = videoInfo.simCode;
				if(simCode.charAt(0)=="0"){
					simCode = simCode.substr(1);
				}
				videoInfo.src = rtmp_server_url.replace("{simCode}",simCode).replace("{channel}",videoInfo.channelId);
				startRtmpT5player(videoInfo);
				videoList.push(videoInfo);
				
				//自动切换更大模式
				if (videoList.length > max_video_view) {
					showVideoView(Math.sqrt(max_video_view)+1);
				}
			}
		}
		removeInfowindow();
	}else{
		showErrorMessage("设备不在线，不能启动语音对讲！");
		return false;
	}
	return true;
}