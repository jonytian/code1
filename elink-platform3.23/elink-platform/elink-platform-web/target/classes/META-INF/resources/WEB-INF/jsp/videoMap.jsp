<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/header.jsp"%>
<link rel="stylesheet" href="<c:url value='/css/yuntai.css'/>">
<title>实时监控</title>
<style type="text/css">
	.layui-layer-setwin .layui-layer-close2 {
		right: -15px !important;
		top: -15px !important;
	}
</style>
</head>
<body>
	<!--内容部分-->
	<div class="con1" id="content-div">
		<!--左侧地图-->
		<div class="left map_left">
				<div class="left video_left" id="map_box"></div>
				<div class="right video_center">
				
					<div id="talk_control">
						<i class="glyphicon glyphicon-headphones" title="正在语音对讲，点击可停止……" onclick="onclickStopTalk();"></i>
					    <div style="">
	    					<div id="video_talker_voice"></div>
						</div>
					</div>

					<div class="video_boolbar">
						<i class="glyphicon glyphicon-remove" title="停止全部视频" onclick="stopAllVideo();"></i>
						<i class="glyphicon glyphicon-volume-up" style="display:none;" title="开启声音" onclick="volumeOn();"></i>
						<i class="glyphicon glyphicon-volume-off" style="display:none;" title="全部静音" onclick="volumeOff();"></i>
						<img src="/img/view4X4.png" title="打开4X4路视频" onclick="showVideoView(4);"></img>
						<i class="glyphicon glyphicon-th" title="打开3X3路视频" onclick="showVideoView(3);"></i>
						<i class="glyphicon glyphicon-th-large" title="打开2X2路视频" onclick="showVideoView(2);"></i>
						<img src="/img/view1X1.png" title="打开1X1路视频" onclick="showVideoView(1);"></img>
					</div>
			        <div class="video_content_box">
				        <div class="video_box outside-window"><div id="player-container0" style="width:100%;height:100%;"></div></div>
				        <div class="video_box outside-window"><div id="player-container1" style="width:100%;height:100%;"></div></div>
				        
				        <div class="video_box outside-window"><div id="player-container2" style="width:100%;height:100%;"></div></div>
				        <div class="video_box outside-window"><div id="player-container3" style="width:100%;height:100%;"></div></div>
				        
				        <div class="video_box outside-window"><div id="player-container4" style="width:100%;height:100%;"></div></div>
				        <div class="video_box outside-window"><div id="player-container5" style="width:100%;height:100%;"></div></div>
				        <div class="video_box outside-window"><div id="player-container6" style="width:100%;height:100%;"></div></div>
				        <div class="video_box outside-window"><div id="player-container7" style="width:100%;height:100%;"></div></div>
				        
				        <div class="video_box outside-window"><div id="player-container8" style="width:100%;height:100%;"></div></div>
				        <div class="video_box outside-window"><div id="player-container9" style="width:100%;height:100%;"></div></div>
				        <div class="video_box outside-window"><div id="player-container10" style="width:100%;height:100%;"></div></div>
				        <div class="video_box outside-window"><div id="player-container11" style="width:100%;height:100%;"></div></div>
				        
				        <div class="video_box outside-window"><div id="player-container12" style="width:100%;height:100%;"></div></div>
				        <div class="video_box outside-window"><div id="player-container13" style="width:100%;height:100%;"></div></div>
				        <div class="video_box outside-window"><div id="player-container14" style="width:100%;height:100%;"></div></div>
				        <div class="video_box outside-window"><div id="player-container15" style="width:100%;height:100%;"></div></div>
			        </div>
	    		</div>
		</div>

		<!--右侧功能栏-->
		<div class="right map_right">
			<div class="map_right_top" id="map_right_top">
				<ul>
					<li class="li_active">分组查询</li>
					<li>列表查询</li>
					<li>实时信息</li>
				</ul>
			</div>
			<div class="map_con" id="map_con">
				<!-- 车辆分组 -->
				<div class="map_con_div" id="car-tree-div" style="overflow: auto;display: block;">
					<ul id="car-tree" class="ztree"></ul>
				</div>
				
				<!-- 车辆列表 -->
				<div class="map_con_div">
					<div class="table_find">
						<p>
							&nbsp;<input type="text" maxlength="16" id="search-plateNumber" style="width: 50%;"
								placeholder="输入车牌号">&nbsp;<input id="search-state"
								value="3" type="checkbox" onclick="doQuery()"><span>在线</span>&nbsp;
							<button type="button" class="btn btn-primary btn-sm" onclick="doQuery()">
								<span class="glyphicon glyphicon-search"></span>查询
							</button>
						</p>
					</div>
					<div class="table_div">
						<table id="boot-strap-table" class="table_style" border="0"></table>
					</div>
				</div>

				<!-- 实时信息 -->
				<div class="map_con_div real-time-message" id="real-time-message-div"></div>
			</div>

		</div>
	</div>

	<div id="video-conctrol-menu">
        <ul>
            <li id="start-record-vedio"><a href="javascript:void(0)" onclick="onclickStartRecordAndVideo()">开启音视频</a></li>
            <li id="stop-record-vedio" style="display:none;"><a  href="javascript:void(0)" onclick="onclickStopRecordAndVideo()">关闭音视频</a></li>
        	
        	<li id="start-vedio"><a  href="javascript:void(0)" onclick="onclickStartVideo()">开启视频</a></li>
            <li id="stop-vedio" style="display:none;"><a  href="javascript:void(0)" onclick="onclickStopVideo()">停止视频</a></li>
            
            <li id="start-record"><a  href="javascript:void(0)" onclick="onclickStartRecord()">开启监听</a></li>
            <li id="stop-record" style="display:none;"><a  href="javascript:void(0)" onclick="onclickStopRecord()">停止监听</a></li>
            
            <li id="start-talk"><a  href="javascript:void(0)" onclick="onclickStartTalk()">开启语音对讲</a></li>
            <li id="stop-talk" style="display:none;"><a  href="javascript:void(0)" onclick="onclickStopTalk()">停止语音对讲</a></li>

            <li id="stream-ctl" style="display:none;"><a  href="javascript:void(0)" onclick="changeStream()">切换码流</a></li>
            <li><a href="javascript:void(0)" onclick="showYuntai()">云台控制</a></li>
        </ul>
    </div>
    
    <div id="yuntai">
    	 <a class="amap-info-close" href="javascript:void(0)" onclick="hideYuntai()" style="right: 5px;">×</a>
		 <div class="control-wrapper" title="云台旋转">
			    <div class="control-btn control-top">
			        <i class="fa fa-chevron-up glyphicon glyphicon-chevron-up" onclick="send9301(1)"></i>
			        <div class="control-inner-btn control-inner"></div>
			    </div>
			    <div class="control-btn control-left">
			        <i class="fa fa-chevron-left glyphicon glyphicon-chevron-left" onclick="send9301(3)"></i>
			        <div class="control-inner-btn control-inner"></div>
			    </div>
			    <div class="control-btn control-bottom">
			        <i class="fa fa-chevron-down glyphicon glyphicon-chevron-down" onclick="send9301(2)"></i>
			        <div class="control-inner-btn control-inner"></div>
			    </div>
			    <div class="control-btn control-right">
			        <i class="fa fa-chevron-right glyphicon glyphicon-chevron-right" onclick="send9301(4)"></i>
			        <div class="control-inner-btn control-inner"></div>
			    </div>
			    <div class="control-round">
			        <div class="control-round-inner">
			            <i class="fa fa-pause-circle glyphicon glyphicon-pause" onclick="send9301(0)"></i>
			        </div>
			    </div>
		</div>
		
		<div style="margin-top:15px;">
		    <input name="deviceId" id="deviceId" type="hidden">
			车牌号：<span id="plateNo"></span>&nbsp;&nbsp;通道号：<select name="channelId" id="channelId" placeholder="控制哪个摄像头"></select>
		</div>
		<div style="margin-bottom:20px;margin-top:20px;">
			<a href="javascript:void(0);" onclick="send9302(0)"><img title="焦距控制" src="<c:url value='/img/jiaojujia.png'/>"></a>
			<a href="javascript:void(0);" onclick="send9303(0)"><img title="光圈控制" src="<c:url value='/img/guangquanda.png'/>"></a>
			<a href="javascript:void(0);" onclick="send9304(1)"><img title="雨刷控制" src="<c:url value='/img/yushuakai.png'/>"></a>
			<a href="javascript:void(0);" onclick="send9305(1)"><img title="补光控制" src="<c:url value='/img/buguangkai.png'/>"></a>
			<a href="javascript:void(0);" onclick="send9306(0)"><img title="变倍控制" src="<c:url value='/img/bianbeijia.png'/>"></a>
		</div>
		<div>
			<a href="javascript:void(0);" onclick="send9302(1)"><img title="焦距控制" src="<c:url value='/img/jiaojujian.png'/>"></a>
			<a href="javascript:void(0);" onclick="send9303(1)"><img title="光圈控制" src="<c:url value='/img/gaungquanxiao.png'/>"></a>
			<a href="javascript:void(0);" onclick="send9304(0)"><img title="雨刷控制" src="<c:url value='/img/yushuaguan.png'/>"></a>
			<a href="javascript:void(0);" onclick="send9305(0)"><img title="补光控制" src="<c:url value='/img/buguangguan.png'/>"></a>
			<a href="javascript:void(0);" onclick="send9306(1)"><img title="变倍控制" src="<c:url value='/img/bianbeijian.png'/>"></a>
		</div>
	</div>
	
	<%@ include file="/common/common.jsp"%>
	<script type="text/javascript" src="<c:url value='/js/ztree/jquery.ztree.all.min.js'/>"></script>
	<%@ include file="pub/mapScript.jsp"%>
	<script type="text/javascript" src="<c:url value='/js/ckplayer/ckplayer.min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/videoControl.js?v=20191217'/>"></script>

	<script type="text/javascript" src="<c:url value='/js/plugins/recorder-core.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/plugins/WSClient.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/plugins/resampler.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/device/recorder-util.js'/>"></script>
</body>
</html>
