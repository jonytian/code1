<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<link href="<c:url value='/scripts/videojs/video-js.css?v=7.0.3'/>" rel="stylesheet">
<link href="<c:url value='/scripts/videojs/videojs-titlebar.css?v=3'/>" rel="stylesheet"></link>
<script type="text/javascript" src="<c:url value='/scripts/videojs/video.js?v=7.0.3'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/videojs/videojs-contrib-hls.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/videojs/videojs-titlebar.js'/>"></script>
<div id="live-video-dlg" class="easyui-dialog" title="视频监控" style="width:314px; height:336px;" data-options="maximizable:true,modal:false" closed="true">
	<video id="live-video" width="300" height="300"  class="video-js vjs-default-skin" poster="<c:url value='/images/loading.gif'/>" data-setup="{}">
	</video>
</div>

<script>
var player,clinetId;
var heartbeatInterval,video_playing_check_interval;
var current_video_device_id,current_video_channel_id,current_playlist_dir,current_video_message_id;
var is_waitting_play=false,isPlaying=false;

$(document).ready(function() {
	clinetId = getClinetId();
	player = videojs('live-video');
});

function startHeartbeat(){
	clearInterval(heartbeatInterval);
	heartbeatInterval = setInterval(function(){
		var url=message_api_server_servlet_path+"/heartbeat/"+clinetId+"/1.json";
		ajaxAsyncGet(url,{},function(result){});
	},5*1000);
}

function startLiveVideo(){
	if(isPlaying || is_waitting_play){
		showErrorMessage("一次只允许一台终端进行实时视频监控！");
		return;
	}
	
	var dialogId = "live-video-dlg";	
	if(current_selected_device){
		var deviceId = current_selected_device.id;
		if(!isOnline(deviceId)){
			showErrorMessage("终端不在线，不能进行视频监控！");
			return;
		}
		var browser = getBrowser();
		if(!(browser =="Chrome" || browser =="Safari")){
			showMessage("实时视频监控，请使用Chrome或Safari浏览器播放！");
			return;
		}
		current_video_device_id = deviceId;
		closeCmdDialog();

		var data = $("#"+current_dialog_id+"-frm").serializeObject();
		var messageId=data.messageId;
		current_video_message_id=messageId;
		var url=message_api_server_servlet_path+"/video/"+deviceId+".json?clinetId="+clinetId;
		var desc = $("#"+current_dialog_id).panel('options').title;
		if(desc){
			data.desc=desc;
		}
		var channelId = data.channelId || data.channel;
		current_video_channel_id = channelId;
		ajaxAsyncPost(url,data,function(result){
			if (result.code!=0) {
				showErrorMessage(result.message);
			}  else {
				closeCmdDialog();
				var result=result.data;
				setCmdInfo(result);
				startHeartbeat();
				is_waitting_play=true;
				var left = $(window).width()-335;
				$("#"+dialogId).dialog({
					left:left,
					top:50,
					onMaximize: function () {
						var height = $(this).height();
						var width = $(this).width();
						player.height(height);
						player.width(width);
		            },
		            onRestore: function () {
		            	var height = $(this).height();
						var width = $(this).width();
						player.height(height);
						player.width(width);
		            },
		            onBeforeClose: function () {
		            	try{
		            		stopVideo(messageId,channelId);
		            	}catch(e){}
		                return true; 
		         }}).dialog("open");

				player.addChild('TitleBar', {text: current_selected_device.name+",通道"+channelId});
				
				//180s后视频还没有上来，则提示用户
				setTimeout(function(){
					if(is_waitting_play&&!isPlaying){
						$("#"+dialogId).dialog("close");
						stopVideo(messageId,channelId);
						showAlertMessage("实时视频上传超时，请确认设备是否支持视频监控！");
					}
                },180*1000);
			}
		});
	}
}

function offlineVideoClean(deviceId){
	if(deviceId!=current_video_device_id && !(isPlaying || is_waitting_play)){
		return;
	}
	stopVideoClean();
	$("#live-video-dlg").dialog("close");
	showAlertMessage("设备已下线，实时视频监控已停止！");
}

function stopVideoClean(){
	clearInterval(heartbeatInterval);
	clearInterval(video_playing_check_interval);
	player.reset();
	isPlaying=false;
	is_waitting_play=false;
	current_playlist_dir="";
	current_video_channel_id="";
}


function stopVideo(messageId,channelId){
	stopVideoClean();
	var url=message_api_server_servlet_path+"/video/"+current_video_device_id+".json?clinetId="+clinetId;
	//发送指令停止视频
	url+="&channelId="+channelId;
	ajaxAsyncDel(url,{},function(result){});
}

function playVideo(deviceId,channelId,playList){
	if(!(deviceId==current_video_device_id && current_video_channel_id == channelId)){
		return;
	}
	var bool = false;
	playList = playList.replace(/\\/g,"\/");
	if(is_waitting_play && !isPlaying){
		isPlaying=true;
		bool = true;
	}
	if(current_playlist_dir!=playList){
		bool = true;
	}
	if(bool){
		current_playlist_dir=playList;
		doPlay();
		//判断视频是否卡住，卡主3s重新load视频
	    var lastTime = -1, tryTimes = 0;
	    clearInterval(video_playing_check_interval);
	    video_playing_check_interval = setInterval(function(){
	        var currentTime = player.currentTime();
	        if(currentTime == lastTime){
	            if(++tryTimes > 12){
	                tryTimes = 0;
	                stopVideo(current_video_message_id,channelId);
	                $("#live-video-dlg").dialog("close");
					showAlertMessage("实时视频监控异常，网络异常或者视频超时停止，请稍后重试！");
	            }
	        }else{
	            lastTime = currentTime;
	            tryTimes = 0;
	        }
	    },5000);
	}
}

function doPlay(){
	//约定资源存储目录：yyyyMMdd/simCode/channelId/playlist.m3u8
	var src = hls_server_host+"/"+current_playlist_dir+"playlist.m3u8";
	var sources = [{ type: "application/x-mpegURL", src:src}];
	player.src(sources);
	player.load(src);
	//player.reset();
	player.play();
}

function getClinetId(){
	var c_name = "DWRSESSIONID=";
	var cookie = document.cookie;
    if(cookie.length>0){
       var c_start = cookie.indexOf(c_name); 
       if(c_start!=-1){ 
         c_start=c_start + c_name.length;
         var c_end = cookie.indexOf(";",c_start);
         if(c_end==-1) c_end=cookie.length;
         return "browser_"+unescape(cookie.substring(c_start,c_end));  
       }
    }
    return "browser_"+generateUUID();
}

function generateUUID() {
	var d = new Date().getTime();
	var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
	  var r = (d + Math.random()*16)%16 | 0;
	  d = Math.floor(d/16);
	  return (c=='x' ? r : (r&0x3|0x8)).toString(16);
	});
	return uuid;
}

function getBrowser(){  
    var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串  
    var isOpera = userAgent.indexOf("Opera") > -1; 
    if (isOpera) {  
        return "Opera"  
    }; //判断是否Opera浏览器  
  
    if (userAgent.indexOf("Firefox") > -1) {  
        return "Firefox";  
    } //判断是否Firefox浏览器  
  
    if (userAgent.indexOf("Chrome") > -1){
  		return "Chrome"; 
 	}
  
    if (userAgent.indexOf("Safari") > -1) {
        return "Safari";
    } //判断是否Safari浏览器  
  
    if (userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1 && !isOpera) {  
        return "IE";  
    }; //判断是否IE浏览器  
} 
</script>