function loadRtmpServer(code){
	var params = {};
	params["type.eq"]= DICTIONARY_PROVIDER_API;
	params["code.eq"]= code? code:"1078-video";
	var url = management_api_server_servlet_path + "/system/dictionary/dictionary.json";
	var result = ajaxSyncPost(url, params);
	if (result.code != 0) {
		showErrorMessage(result.message);
	} else {
		var list = result.data;
		if(list && list.length>0){
			var item = list[0];
			var content = $.evalJSON(item.content);
			return content.url;
		}
	}
	return "";
}

function loadFlvServer(){
	var params = {};
	params["type.eq"]= DICTIONARY_PROVIDER_API;
	params["code.eq"]= "flv-video";
	var url = management_api_server_servlet_path + "/system/dictionary/dictionary.json";
	var result = ajaxSyncPost(url, params);
	if (result.code != 0) {
		showErrorMessage(result.message);
	} else {
		var list = result.data;
		if(list && list.length>0){
			var item = list[0];
			var content = $.evalJSON(item.content);
			return content.url;
		}
	}
	return "";
}

var mp4Player;
function playMP4(mediaUrl) {
	var sources = [{
		type : "video/mp4",
		src : mediaUrl
	}];
	
	if(!mp4Player){
		mp4Player = videojs('mp4-player');
	}
	
	mp4Player.src(sources);
	mp4Player.load(mediaUrl);
	mp4Player.play();

	var dialogId = "mp4-player-dlg";
	var title = $("#"+dialogId).attr("title");
	showDialog(title, dialogId,function() {
		try {
			mp4Player.reset();
		} catch (e) {}
	});
}

function playMP3(mediaUrl){
	var audio = $("#mp3-player");
	audio.src = mediaUrl;
	audio.load();
	audio.play();
	var dialogId = "mp3-player-dlg";
	var title = $("#"+dialogId).attr("title");
	showDialog(title, dialogId,function() {
		try {
			audio.pause();
		} catch(e) {}
	});
}

function startFlvLiveStream(videoContainerId,mediaUrl,onErrorHander){
	var player = flvjs.createPlayer( {
		isLive: true,
        hasAudio: true,
        hasVideo: true,
		type : 'flv',
		url : mediaUrl
	});
	
	
	var video = document.getElementById(videoContainerId);
	video.poster="/images/loading.gif";
	player.on(flvjs.Events.ERROR, (errType, errDetail) => { 
		// errType是 NetworkError时，对应errDetail有：Exception、HttpStatusCodeInvalid、
		// ConnectingTimeout、EarlyEof、UnrecoverableEarlyEof
		// errType是 MediaError时，对应errDetail是MediaMSEError
		if(onErrorHander){
			setTimeout(function(){
				onErrorHander();
			}, 2000);
		}
	});

	player.attachMediaElement(video);
	player.load();	
	player.play();
	return player;
}

var cyberplayerErrorTimer;
function startRtmpLiveStream(videoContainerId,mediaUrl,onErrorHander){
	var player = cyberplayer(videoContainerId).setup({
        width: $("#"+videoContainerId).width(),
        height: $("#"+videoContainerId).height(),
        controlbar: {
            barLogo: false
        },
        isLive: true, // 标明是否是直播
        file: mediaUrl, 
        type:"rtmp",
        autostart: true,
        stretching: "uniform",
        volume: 100,
        reconnecttime:5,
        controls: false,
        ak: "2b0fe9030a5748d780a26cc4bfbdea95" // 公有云平台注册即可获得accessKey
    });
	
	player.onSetupError(function(event){
		showMessage("播放器初始化失败，请确认允许浏览器加载flash插件后重试！");
		setTimeout(function(){
			onErrorHander();
		}, 3000);
	});
	
	player.onError(function(event){
		if(cyberplayerErrorTimer){
			return;
		}
		showMessage("疑似直播信号丢失！");
		var playlistItem = player.getPlaylistItem();
		cyberplayerErrorTimer = setTimeout(function(){
			cyberplayerErrorTimer = null;
			player.load({file:playlistItem.file});
		}, 5000);
	});
	
	player.onDisplayClick(function(event){
		player.setFullscreen(true);
		player.setControls(true);
	});

	player.onNoLiveStream(function(){
		//showMessage("疑似直播信号丢失！");
	});
	
	player.onFullscreen(function(event){ 
	    if(!event.fullscreen){
	    	player.setControls(false);
	    }
	});
	
	return player;
}

function getMediaUrl(fileInfo){
	var mediaUrl;
	if(getFileServer()){//如果配置了文件服务器，则直接从文件服务器下载
		mediaUrl = getFileServer()+(fileInfo.filePath).replace(/\\/g,"\/");
	}else{
		//从ftp服务器下载
		mediaUrl = getContextPath() +"/attachment/download.do?url=/media/ftp/download/"+fileInfo.id+".json";
	}
	return mediaUrl;
}

function getClinetId() {
	var c_name = "DWRSESSIONID=";
	var cookie = document.cookie;
	if (cookie.length > 0) {
		var c_start = cookie.indexOf(c_name);
		if (c_start != -1) {
			c_start = c_start + c_name.length;
			var c_end = cookie.indexOf(";", c_start);
			if (c_end == -1)
				c_end = cookie.length;
			return "browser_" + unescape(cookie.substring(c_start, c_end));
		}
	}
	return "browser_" + generateUUID();
}
