<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/header.jsp"%>
<link href="<c:url value='/js/videojs/video-js.css'/>" rel="stylesheet">
<%@ include file="/common/common.jsp"%>
<title>多媒体管理</title>
</head>
<body>
	<div class="clear"></div>
	<!--内容部分-->
	<div class="con1 left" id="content-div">
		<div class="right  con_table">
			<div class="table_find">
				<p>
					<label>类型：</label>
					<select id="search-resourceType">
						<option value="">选择类型</option>
						<option value="1">音频</option>
						<option value="2">视频</option>
					</select>
					<label>车牌号：</label><%@ include file="../pub/carSearchSelectPicker.jsp"%> &nbsp;&nbsp;
					<button type="button" class="btn btn-primary btn-sm" onclick="doQuery()">
						<span class="glyphicon glyphicon-search"></span>查询
					</button>
				</p>
			</div>
			<div class="table_div">
				<table id="boot-strap-table" class="table_style" border="0"></table>
			</div>
		</div>
	</div>
	
	<div id="play-video-dlg" class="simple-dialog" title="播放视频" style="width:500px; height:436px;overflow:hidden;">
		<video id="play-video" width="500" height="400" class="video-js vjs-default-skin" controls 
			poster="<c:url value='/images/loading.gif'/>">
		</video>
	</div>
	
	<div id="play-audio-dlg"  class="simple-dialog" title="播放音频" style="width:300px; height:56px;overflow:hidden;">
	  <audio id="play-audio" controls="controls" autoplay="autoplay">亲 您的浏览器不支持html5的audio标签</audio>
	</div>

	<script type="text/javascript" src="<c:url value='/js/videojs/video.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/device/historyMediaList.js'/>"></script>
</body>
</html>