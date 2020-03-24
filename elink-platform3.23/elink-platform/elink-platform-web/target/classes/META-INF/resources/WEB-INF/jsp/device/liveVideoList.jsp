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
				</select>
					<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label><%@ include file="../pub/carSearchSelectPicker.jsp"%>
					<label>上传时间：</label><input id="search-startTime" name="startTime" style="width:8%;" type="text" readonly class="date form_date" data-date-format="yyyy-mm-dd"> <select id="search-hour" name="hour" style="width:15% !important;"></select>
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

<div id="rtmpPlayer-video-dlg" class="simple-dialog" style="width:650px;height:500px;" title="实时视频列表">
	<div id="rtmpPlayer-container" style="width:100%;height:80%;"></div>
	<%@ include file="../pub/player-ctl-bar.jsp"%>
</div>

<div id="h5Player-video-dlg"  class="simple-dialog" title="播放视频" style="width:360px; height:336px;overflow:hidden;">
	<video id="h5Player-video" width="360" height="300"  class="video-js vjs-default-skin" poster="<c:url value='/images/loading.gif'/>" controls>
	</video>
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
<script type="text/javascript" src="<c:url value='/js/device/liveVideoList.js?v=20191217'/>"></script>
</body>
</html>
