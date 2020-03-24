<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="<c:url value='/css/tail.css'/>">
<link rel="stylesheet" href="<c:url value='/css/slider-range.css?v=1'/>">
<%@ include file="/common/header.jsp"%>
<title>终端查询</title>
<%@ include file="/common/common.jsp"%>
</head>
<body>
<section class="sm_section left" style="width:100%;">
 <fieldset class="message_fieldset">
            <legend>终端参数</legend>
            <div class="message_footer" style="margin-top:0px;">
                <button type="button" class="bule" onclick="showCmdDialog('send-query-video-parameter-cmd-dlg')">查询终端音视频属性</button>
                <button type="button" class="bule" onclick="showCmdDialog('send-query-device-state-cmd-dlg')">查询外设状态信息</button>
                <button type="button" class="bule" onclick="showCmdDialog('send-deviece-system-cmd-dlg')">查询外设系统信息</button>
            </div>
</fieldset>

 <fieldset class="message_fieldset">
            <legend id="legend">查询终端音视频属性</legend>
            <div id="send-query-video-parameter-cmd-dlg" title="查询终端音视频属性 ">
				<form id="send-query-video-parameter-cmd-dlg-frm" method="post">
				    <input name="messageId" type="hidden" value="9003">
				</form>
			</div>
			
			<div id="send-query-device-state-cmd-dlg" class="simple-dialog" title="查询外设状态信息 ">
				<form id="send-query-device-state-cmd-dlg-frm" method="post">
				    <input name="messageId" type="hidden" value="8900">
				    <input name="type" type="hidden" value="247">
				    <div class="message_con">
						<label style='width:200px;'>外设ID：</label>
						<select name="devieId">
							<option value="">请选择要查询的外设ID</option>
							<option value="100">高级驾驶辅助系统</option>
							<option value="101">驾驶员状态监控系统</option>
							<option value="102">轮胎气压监测系统</option>
							<option value="103">盲点监测系统</option>
						</select>
					</div>
				</form>
			</div>
			
			<div id="send-deviece-system-cmd-dlg" class="simple-dialog" title="查询外设系统信息 ">
				<form id="send-deviece-system-cmd-dlg-frm" method="post">
				    <input name="messageId" type="hidden" value="8900">
				    <input name="type" type="hidden" value="248">
				    <div class="message_con">
						<label style='width:200px;'>外设ID：</label>
						<select name="devieId">
							<option value="">请选择要查询的外设ID</option>
							<option value="100">高级驾驶辅助系统</option>
							<option value="101">驾驶员状态监控系统</option>
							<option value="102">轮胎气压监测系统</option>
							<option value="103">盲点监测系统</option>
						</select>
					</div>
				</form>
			</div>

			<div style="width:80%;">
	             <ul id="group-tree"  class="ztree left_select_tree"></ul>
				 <div class="right_multiple_Select_Box">
				 	<%@ include file="../pub/multipleSelectBox.jsp"%>
				  </div>
            </div>
			<div class="message_footer">
			     <button type="button" class="bule" onclick="sendTerminalQueryCmd()">查询</button>
			</div>
</fieldset>
 <fieldset class="message_fieldset" style="display:none;" id="fieldset-result">
      <legend>查询结果</legend>
      <div id="send-query-video-parameter-cmd-dlg-result" class="simple-dialog">
        <div class="message_con">
			<label style='width:200px;'>输入音频编码方式：</label><span id="audioCoding"></span>
		</div>

		<div class="message_con">
			<label style='width:200px;'>输入音频声道数：</label><span  id="inputChlNum"></span>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>输入音频采样率：</label><span  id="samplingRate"></span>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>输入音频采样位数：</label><span  id="samplingNum"></span>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>音频帧长度：</label><span  id="frameLen"></span>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>是否支持音频输出：</label><span  id="output"></span>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>视频编码方式：</label><span  id="videoCoding"></span>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>支持的最大音频物理通道：</label><span  id="maxAudioChlNum"></span>
		</div>

		<div class="message_con">
			<label style='width:200px;'>支持的最大视频物理通道：</label><span  id="maxVideoChlNum"></span>
		</div>
     </div>
             
     <div id="send-query-device-state-cmd-dlg-result" class="simple-dialog">
	     <div class="message_con">
	        <label style='width:200px;'>工作状态：</label><span id="state"></span>
		 </div>
	
	     <div class="message_con">
	        <label style='width:200px;'>报警状态：</label><span  id="alarm"></span>
	     </div>
	</div>
	
	<div id="send-deviece-system-cmd-dlg-result" class="simple-dialog">
	     <div class="message_con">
			<label style='width:200px;'>公司名称：</label><span id="enterpriseName"></span>
		</div>

		<div class="message_con">
			<label style='width:200px;'>产品型号：</label><span id="productType"></span>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>硬件版本号：</label><span id="hardwareVersion"></span>
		</div>
		
		<div class="message_con">
			<label style='width:200px;'>软件版本号：</label><span id="softwareVersion"></span>
		</div>

		<div class="message_con">
			<label style='width:200px;'>设备 ID：</label><span id="deviceId"></span>
		</div>

		<div class="message_con">
			<label style='width:200px;'>客户代码：</label><span id="clientCode"></span>
		</div>
	</div>
</fieldset>

</section>
<script  type="text/javascript" src="<c:url value='/js/ztree/jquery.ztree.all.min.js'/>"></script>
<script  type="text/javascript" src="<c:url value='/js/device/terminalQuery.js'/>"></script>
</body>
</html>