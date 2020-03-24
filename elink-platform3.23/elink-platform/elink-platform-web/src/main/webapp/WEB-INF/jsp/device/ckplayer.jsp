<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<script type="text/javascript" src="<c:url value='/js/ckplayer/ckplayer.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/device/ckplayer.js?v=20191109'/>"></script>
<script type="text/javascript" src="<c:url value='/js/videojs/video.js'/>"></script>
<div id="ckplayer-video-dlg" class="simple-dialog" style="width:350px;height:275px;" title="视频监控">
	<div id="ckplayer-container" style="width:100%;height:100%;"><img  style="width:100%;height:100%;" src="<c:url value='/images/loading.gif'/>" /></div>
</div>

<div id="h5Player-video-dlg"  class="simple-dialog" title="播放视频" style="width:350px; height:275px;overflow:hidden;">
		<video id="h5Player-video" style="width:100%;height:100%;"  class="video-js vjs-default-skin" poster="<c:url value='/images/loading.gif'/>" controls>
		</video>
</div>

<div id="play-audio-dlg"  class="simple-dialog" title="播放音频" style="width:300px; height:56px;overflow:hidden;">
	  <audio id="play-audio" controls="controls" autoplay="autoplay">亲 您的浏览器不支持html5的audio标签</audio>
</div>