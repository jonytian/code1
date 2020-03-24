<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/header.jsp"%>
<link href="<c:url value='/js/videojs/video-js.css'/>" rel="stylesheet">
<%@ include file="/common/common.jsp"%>
<title>告警详情</title>
</head>
<body>
<!--内容部分-->
<input name="id" id="id" type="hidden">
<input name="recordDate" id="recordDate" type="hidden">
<input name="pageNo" id="pageNo" type="hidden">
<input name="type" id="type" type="hidden">

<div id="car-info-box" class="con left" style="padding-top:0px;padding-bottom:10px;">
    <div class="static_top left">
        <i class="nav_1"></i><span>告警详情</span>
    </div>
    <div class="stiatic_top_con left">
		<table>
			 <tr>
		        <td class="labe_td">车&nbsp;牌&nbsp;号：</td>
		        <td id="plateNo"></td>
		    </tr>

		    <tr>
		        <td class="labe_td">行驶速度：</td>
		        <td id="speed"></td>
		    </tr>

		    <tr>
		        <td class="labe_td">告警时间：</td>
		        <td id="alarmTime"></td>
		    </tr>
		    
		    <tr>
		        <td class="labe_td">告警结束时间：</td>
		        <td id="alarmEndTime">-</td>
		    </tr>
		    
		    <tr>
		        <td class="labe_td">告警处理：</td>
		        <td id="alarmOpinion">-</td>
		    </tr>

		    <tr>
		        <td class="labe_td">告警位置：</td>
		        <td id="address"></td>
		    </tr>

		    <tr>
		        <td class="labe_td">告警详情：</td>
		        <td id="detail"></td>
		    </tr>
		</table>
    </div>
   
    <div class="static_top left">
        <i class="nav_1"></i><span>告警附件</span>
    </div>
     <div class="stiatic_top_con left default-font"><span id="attachmentList"></span></div>
 </div>
 
 <div id="map-dlg" class="simple-dialog">
    <div id="map_box" style="width:100%;height:100%;"></div>
</div>
	
	<div id="play-video-dlg" class="simple-dialog" title="播放视频" style="width:500px; height:436px;overflow:hidden;">
		<video id="play-video" width="500" height="400" class="video-js vjs-default-skin" controls 
			poster="<c:url value='/images/loading.gif'/>">
		</video>
	</div>
	
	<div id="play-audio-dlg"  class="simple-dialog" title="播放音频" style="width:300px; height:56px;overflow:hidden;">
	  <audio id="play-audio" controls="controls" autoplay="autoplay">亲 您的浏览器不支持html5的audio标签</audio>
	</div>

<div class="message_footer">
    <button id="alarm-confirm-btn" style="display:none;" type="button" class="orgen" onclick="javascript:showCommondDialog('alarm-confirm-dlg')">确认告警</button>
    
	<button type="button" class="bule" onclick="javascript:goback();">返回</button>
</div>

<div id="alarm-confirm-dlg" title="告警确认" style="width: 650px; height: 380px;" class="simple-dialog">
		<form id="alarm-confirm-dlg-frm" method="post">
			<input name="alarmId" id="alarm-confirm-dlg-frm-alarmId" type="hidden"/>
			<input name="alarmEventId" id="alarm-confirm-dlg-frm-alarmEventId" type="hidden"/>
			<div class="message_con">
				<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label><span  style="color:#fff;" id="alarm-confirm-dlg-frm-deviceName"></span>
			</div>
			<div class="message_con">
				<label>告警详情：</label><span style="color:#fff;" id="alarm-confirm-dlg-frm-alarmDetail"></span>
			</div>
			<div class="message_con">
				<label style="vertical-align:top;">备注：</label>
				<textarea name="opinion" style="height: 200px;" maxlength="250" class="required"></textarea>
			</div>
			<div class="message_footer">
				<button type="button" class="bule" onclick="doAlarmConfirm()">确定</button>
				<button type="button" class="orgen" onclick="closeDialog()">取消</button>
			</div>
		</form>
	</div>


	<script type="text/javascript" src="<c:url value='/js/videojs/video.js'/>"></script>
<%@ include file="../pub/mapScript.jsp"%>
<script type="text/javascript" src="<c:url value='/js/device/alarmDetail.js?v=20191217'/>"></script>
<script language="javaScript">
$(document).ready(function() {
	$("#id").val("${param.id}");
	$("#recordDate").val("${param.recordDate}");
	$("#pageNo").val("${param.pageNo}");
	$("#type").val("${param.type}");
	startLoading();
	init();
});

function goback(){
  var url = "alarmList.do?pageNo="+$("#pageNo").val(); 
  if($("#type").val()){
	  url = "mapAlarmList.do?pageNo="+$("#pageNo").val(); 
  }
  window.location.href = url;
}

</script>
</body>
</html>
