<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/header.jsp"%>
<%@ include file="/common/common.jsp"%>
<link href="<c:url value='/js/videojs/video-js.css'/>" rel="stylesheet">
<title>1078视频回放</title>
</head>
<body>
	<div class="clear"></div>
	<!--内容部分-->
	<div class="con1 left" id="content-div">
		<div class="right  con_table">
		  <form id="search-frm" method="post">
			 <div class="table_find">
				<p>
					<label>文件位置：</label><select id="search-locationType" name="locationType">
						<option value="1">服务器</option>
						<option value="2">终端</option>
					</select>
					<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label><%@ include file="../pub/carSearchSelectPicker.jsp"%>
					<label>录像时间：</label><input id="search-startTime" name="startTime" style="width:8%;" type="text" readonly class="date form_date" data-date-format="yyyy-mm-dd"> <select id="search-hour" name="hour" style="width:15% !important;"></select>
				 </p>
				  <p>
				  	<label>通&nbsp;&nbsp;道&nbsp;&nbsp;号：</label><select id="search-channelId" name="channelId"  class="required">
					<option value="">全部</option>
					</select>
				     <label>媒体类型：</label> <select id="search-resourceType" name="resourceType">
						<option value="">全部</option>
						<option value="0">音视频</option>
						<option value="1">音频</option>
						<option value="2">视频</option>
					</select>
					<label>码流类型：</label><select id="search-streamType" name="streamType">
						<option value="">全部</option>
						<option value="1">主码流</option>
						<option value="2">子码流</option>
					</select>
					&nbsp;&nbsp;
					<button type="button" class="btn btn-primary btn-sm" onclick="doQueryVideo()">
						<span class="glyphicon glyphicon-search"></span>查询
					</button>
				 </p>
			  </div>
			</form>
			<div class="table_div">
				<table id="boot-strap-table" class="table_style" border="0"></table>
			</div>
		</div>
	</div>
	
	<div id="rtmpPlayer-video-dlg" class="simple-dialog" style="width:650px;height:500px;" title="历史视频回放">
		<div id="rtmpPlayer-container" style="width:100%;height:80%;"></div>
		<%@ include file="../pub/player-ctl-bar.jsp"%>
	</div>
	
	<div id="h5Player-video-dlg"  class="simple-dialog" title="播放视频" style="width:360px; height:336px;overflow:hidden;">
		<video id="h5Player-video" width="360" height="300"  class="video-js vjs-default-skin" poster="<c:url value='/images/loading.gif'/>" controls>
		</video>
	</div>
	
	<div id="h5Player-audio-dlg"  class="simple-dialog" title="播放音频" style="width:300px; height:56px;overflow:hidden;">
	  <audio id="h5Player-audio" controls="controls" autoplay="autoplay">亲 您的浏览器不支持html5的audio标签</audio>
	</div>
	
	<div id="video-conctrol-menu">
        <ul>
            <li><a  href="javascript:void(0)" onclick="setPlayTimes(1)">1倍</a></li>
            <li><a  href="javascript:void(0)" onclick="setPlayTimes(2)">2倍</a></li>
        	<li><a  href="javascript:void(0)" onclick="setPlayTimes(3)">4倍</a></li>
            <li><a  href="javascript:void(0)" onclick="setPlayTimes(4)">8倍</a></li>
            <li><a  href="javascript:void(0)" onclick="setPlayTimes(5)">16倍</a></li>
        </ul>
    </div>

	<script type="text/javascript" src="<c:url value='/js/videojs/video.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/ckplayer/ckplayer.min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/device/historyVideoList.js?v=20191130'/>"></script>
</body>
</html>
