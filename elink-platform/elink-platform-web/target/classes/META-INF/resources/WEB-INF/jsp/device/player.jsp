<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<script type="text/javascript" src="<c:url value='/js/cyberplayer-3.4.1/cyberplayer.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/plugins/flv.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/videojs/video.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/device/player.js'/>"></script>

<div id="rtmp-player-dlg" class="simple-dialog" style="width:650px;height:500px;" title="实时视频">
	<div id="rtmp-player" style="width:100%;height:100%;"><img  style="width:100%;height:100%;" src="<c:url value='/images/loading.gif'/>" /></div>
</div>

<div id="flv-player-dlg"  class="simple-dialog" title="实时视频" style="width:650px;height:500px;overflow:hidden;">
	<video id="flv-player" style="width:100%;height:100%;" autoplay="autoplay"  loop="false" class="videoObj cover video-js" poster="<c:url value='/images/loading.gif'/>">亲  您的浏览器不支持 video标签</video>
</div>

<div id="mp4-player-dlg"  class="simple-dialog" title="播放视频" style="width:650px; height:500px;overflow:hidden;">
	<video id="mp4-player" style="width:100%;height:100%;"  class="video-js vjs-default-skin" poster="<c:url value='/images/loading.gif'/>" controls="controls"></video>
</div>

<div id="mp3-player-dlg"  class="simple-dialog" title="播放音频" style="width:300px; height:56px;overflow:hidden;">
	 <audio id="mp3-player" controls="controls" autoplay="autoplay">亲 您的浏览器不支持html5的audio标签</audio>
</div>


<div id="history-rtmp-player-dlg" class="simple-dialog" style="width:650px;height:500px;" title="视频回放">
	<div id="history-rtmp-player" style="width:650px;height:400px;"><img  style="width:100%;height:100%;" src="<c:url value='/images/loading.gif'/>" /></div>
	<%@ include file="../pub/player-ctl-bar.jsp"%>
</div>

<div id="history-flv-player-dlg"  class="simple-dialog" title="视频回放" style="width:650px;height:500px;overflow:hidden;">
	<video id="history-flv-player" style="width:650px;height:400px;" autoplay="autoplay"  loop="false" class="videoObj cover video-js" poster="<c:url value='/images/loading.gif'/>">亲  您的浏览器不支持 video标签</video>
	<%@ include file="../pub/player-ctl-bar.jsp"%>
</div>