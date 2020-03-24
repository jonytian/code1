<%@ page language="java" contentType="text/html; charset=utf-8" 	pageEncoding="utf-8"%>
<div id="send-yuntai-revolve-cmd-dlg" class="simple-dialog"  title="云台旋转" style="width:650px;height:250px;">
	<form id="send-yuntai-revolve-cmd-dlg-frm" method="post">
		<input name="deviceId" id="send-yuntai-revolve-cmd-dlg-frm-deviceId" type="hidden">
		<input name="messageId" type="hidden" value="9301">
		<div class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label><input name="name" readonly id="send-yuntai-revolve-cmd-dlg-frm-name" type="text">
		</div>
		<div class="message_con">
			<label>逻辑通道号：</label><select name="channelId"  class="required">
			</select>
		</div>
		<div class="message_con">
			<label>方向：</label><select name="direction"  class="required">
					  <option  value ="0">停止</option>
					  <option  value ="1">上</option>
					  <option  value ="2">下</option>
					  <option  value ="3">左</option>
					  <option  value ="4">右</option>
			</select>
		</div>
		<div class="message_con">
			<label>速度：</label><input name="speed" value="50" maxlength="3" class="required integer"  type="text" placeholder="取值范围：0~255"><span class="must">*</span>
		</div>
		<div class="message_footer"> 
     		<button type="button" class="bule" onclick="sendCmd()">确定</button> 
    		<button type="button" class="orgen" onclick="closeDialog()">取消</button> 
    	</div>
	</form>
</div>

<div id="send-yuntai-focus-adjust-cmd-dlg" class="simple-dialog"  title="云台调整焦距控制" style="width:650px;height:220px;">
	<form id="send-yuntai-focus-adjust-cmd-dlg-frm" method="post">
		<input name="deviceId" id="send-yuntai-focus-adjust-cmd-dlg-frm-deviceId" type="hidden">
		<input name="messageId" type="hidden" value="9302">
		<div class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label><input name="name" readonly  id="send-yuntai-focus-adjust-cmd-dlg-frm-name" type="text">
		</div>
		<div class="message_con">
			<label>逻辑通道号：</label><select name="channelId"  class="required">
					  
			</select>
		</div>
		<div class="message_con">
			<label>焦距方向：</label><select name="direction"  class="required">
					  <option  value ="0">调大</option>
					  <option  value ="1">调小</option>
			</select>
		</div>
		<div class="message_footer"> 
     		<button type="button" class="bule" onclick="sendCmd()">确定</button> 
    		<button type="button" class="orgen" onclick="closeDialog()">取消</button> 
    	</div>
	</form>
</div>


<div id="send-yuntai-aperture-adjust-cmd-dlg" class="simple-dialog"  title="云台调整光圈控制" style="width:650px;height:220px;">
	<form id="send-yuntai-aperture-adjust-cmd-dlg-frm" method="post">
		<input name="deviceId" id="send-yuntai-aperture-adjust-cmd-dlg-frm-deviceId" type="hidden">
		<input name="messageId" type="hidden" value="9303">
		<div class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label><input name="name" readonly  id="send-yuntai-aperture-adjust-cmd-dlg-frm-name" type="text">
		</div>
		<div class="message_con">
			<label>逻辑通道号：</label><select name="channelId"  class="required">
					  
			</select>
		</div>
		<div class="message_con">
			<label>调整方式：</label><select name="direction"  class="required">
					  <option  value ="0">调大</option>
					  <option  value ="1">调小</option>
			</select>
		</div>
		<div class="message_footer"> 
     		<button type="button" class="bule" onclick="sendCmd()">确定</button> 
    		<button type="button" class="orgen" onclick="closeDialog()">取消</button> 
    	</div>
	</form>
</div>


<div id="send-yuntai-wiper-ctl-cmd-dlg" class="simple-dialog"  title="云台雨刷控制" style="width:650px;height:220px;">
	<form id="send-yuntai-wiper-ctl-cmd-dlg-frm" method="post">
		<input name="deviceId" id="send-yuntai-wiper-ctl-cmd-dlg-frm-deviceId" type="hidden">
		<input name="messageId" type="hidden" value="9304">
		<div class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label><input name="name" readonly  id="send-yuntai-wiper-ctl-cmd-dlg-frm-name" type="text">
		</div>
		<div class="message_con">
			<label>逻辑通道号：</label><select name="channelId"  class="required">
					  
			</select>
		</div>
		<div class="message_con">
			<label>启停标识：</label><select name="flag"  class="required">
					  <option  value ="0">停止</option>
					  <option  value ="1">启动</option>
			</select>
		</div>
		<div class="message_footer"> 
     		<button type="button" class="bule" onclick="sendCmd()">确定</button> 
    		<button type="button" class="orgen" onclick="closeDialog()">取消</button> 
    	</div>
	</form>
</div>


<div id="send-yuntai-infrared-fill-light-ctl-cmd-dlg" class="simple-dialog"  title="云台红外补光控制" style="width:650px;height:220px;">
	<form id="send-yuntai-infrared-fill-light-ctl-cmd-dlg-frm" method="post">
		<input name="deviceId" id="send-yuntai-infrared-fill-light-ctl-cmd-dlg-frm-deviceId" type="hidden">
		<input name="messageId" type="hidden" value="9305">
		<div class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label><input name="name" readonly  id="send-yuntai-infrared-fill-light-ctl-cmd-dlg-frm-name" type="text">
		</div>
		<div class="message_con">
			<label>逻辑通道号：</label><select name="channelId"  class="required">
					  
			</select>
		</div>
		<div class="message_con">
			<label>启停标识：</label><select name="flag"  class="required">
					  <option  value ="0">停止</option>
					  <option  value ="1">启动</option>
			</select>
		</div>
		<div class="message_footer"> 
     		<button type="button" class="bule" onclick="sendCmd()">确定</button> 
    		<button type="button" class="orgen" onclick="closeDialog()">取消</button> 
    	</div>
	</form>
</div>

<div id="send-yuntai-zoom-ctl-cmd-dlg" class="simple-dialog"  title="云台变倍控制" style="width:650px;height:220px;">
	<form id="send-yuntai-zoom-ctl-cmd-dlg-frm" method="post">
		<input name="deviceId" id="send-yuntai-zoom-ctl-cmd-dlg-frm-deviceId" type="hidden">
		<input name="messageId" type="hidden" value="9306">
		<div class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label><input name="name" readonly  id="send-yuntai-zoom-ctl-cmd-dlg-frm-name" type="text">
		</div>
		<div class="message_con">
			<label>逻辑通道号：</label><select name="channelId"  class="required">
					  
			</select>
		</div>
		<div class="message_con">
			<label>变倍控制：</label><select name="type"  class="required">
					  <option  value ="0">调大</option>
					  <option  value ="1">调小</option>
			</select>
		</div>
		<div class="message_footer"> 
     		<button type="button" class="bule" onclick="sendCmd()">确定</button> 
    		<button type="button" class="orgen" onclick="closeDialog()">取消</button> 
    	</div>
	</form>
</div>



<div id="send-take-audio-video-cmd-dlg" class="simple-dialog"  title="实时音视频传输请求" style="width:650px;height:400px;">
	<form id="send-take-audio-video-cmd-dlg-frm" method="post">
		<input name="deviceId" id="send-take-audio-video-cmd-dlg-frm-deviceId" type="hidden">
		<input name="messageId" type="hidden" value="9101">
		<div class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label><input name="name" readonly  id="send-take-audio-video-cmd-dlg-frm-name" type="text">
		</div>
		<div class="message_con">
			<label>服务器IP：</label><input name="ip" maxlength="64" class="required ip" type="text"><span class="must">*</span>
		</div>
		<div class="message_con">
			<label>TCP端口号：</label><input name="tcpPort" maxlength="5"  class="required integer" type="text" placeholder="不用是tcp传输时，设置为0"><span class="must">*</span>
		</div>
		<div class="message_con">
			<label>UDP端口号：</label><input name="udpPort" maxlength="5"  class="required integer" type="text" placeholder="不用是udp传输时，设置为0"><span class="must">*</span>
		</div>
		<div class="message_con">
			<label>逻辑通道号：</label><select name="channelId"  class="required">
					  
			</select>
		</div>
		<div class="message_con">
			<label>数据类型：</label><select name="dataType"  class="required">
					  <option  value ="0">音视频</option>
					  <option  value ="1">视频</option>
					  <option  value ="2">双向对讲</option>
					  <option  value ="3">监听</option>
					  <option  value ="4">中心广播</option>
					  <option  value ="5">透传</option>
			</select>
		</div>
		<div class="message_con">
			<label>码流类型：</label><select name="streamType"  class="required">
					  <option  value ="0">主码流</option>
					  <option  value ="1">子码流</option>
			</select>
		</div>
		<div class="message_footer"> 
     		<button type="button" class="bule" onclick="sendCmd()">确定</button> 
    		<button type="button" class="orgen" onclick="closeDialog()">取消</button> 
    	</div>
	</form>
</div>


<div id="send-audio-video-ctl-cmd-dlg" class="simple-dialog"  title="实时音视频传输控制" style="width:650px;height:400px;">
	<form id="send-audio-video-ctl-cmd-dlg-frm" method="post">
		<input name="deviceId" id="send-audio-video-ctl-cmd-dlg-frm-deviceId" type="hidden">
		<input name="messageId" type="hidden" value="9102">
		<div class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label><input name="name" readonly  id="send-audio-video-ctl-cmd-dlg-frm-name" type="text">
		</div>
		<div class="message_con">
			<label>逻辑通道号：</label><select name="channelId"  class="required">
					  
			</select>
		</div>
		<div class="message_con">
			<label>控制指令：</label><select name="command"  class="required">
					  <option  value ="0">关闭音视频传输指令</option>
					  <option  value ="1">切换码流</option>
					  <option  value ="2">暂停该通道所有流发送</option>
					  <option  value ="3">恢复暂停前流的发送</option>
					  <option  value ="4">关闭双向对讲</option>
			</select>
		</div>
		<div class="message_con">
			<label>关闭音视频类型：</label><select name="commadType"  class="required">
					  <option  value ="0">音视频</option>
					  <option  value ="1">音频</option>
					  <option  value ="2">视频</option>
			</select>
		</div>
		<div class="message_con">
			<label>切换码流类型：</label><select name="streamType"  class="required">
					  <option  value ="0">主码流</option>
					  <option  value ="1">子码流</option>
			</select>
		</div>
		<div class="message_footer"> 
     		<button type="button" class="bule" onclick="sendCmd()">确定</button> 
    		<button type="button" class="orgen" onclick="closeDialog()">取消</button> 
    	</div>
	</form>
</div>



<div id="send-remote-video-playback-cmd-dlg" class="simple-dialog"  title="远程录像回放请求" style="width:650px;height:500px;">
	<form id="send-remote-video-playback-cmd-dlg-frm" method="post">
		<input name="deviceId" id="send-remote-video-playback-cmd-dlg-frm-deviceId" type="hidden">
		<input name="messageId" type="hidden" value="9201">
		<div class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label><input name="name" readonly  id="send-remote-video-playback-cmd-dlg-frm-name" type="text">
		</div>
		<div class="message_con">
			<label>服务器IP：</label><input name="ip" maxlength="64"  class="required ip" type="text"><span class="must">*</span>
		</div>
		<div class="message_con">
			<label>TCP端口号：</label><input name="tcpPort"  maxlength="5"  class="required integer" type="text" placeholder="不用是tcp传输时，设置为0"><span class="must">*</span>
		</div>
		<div class="message_con">
			<label>UDP端口号：</label><input name="udpPort" maxlength="5"  class="required integer" type="text" placeholder="不用是udp传输时，设置为0"><span class="must">*</span>
		</div>
		<div class="message_con">
			<label>逻辑通道号：</label><select name="channelId"  class="required">
					  
			</select>
		</div>
		<div class="message_con">
			<label>音视频类型：</label><select name="dataType"  class="required">
					  <option  value ="0">音视频</option>
					  <option  value ="1">音频</option>
					  <option  value ="2">视频</option>
					  <option  value ="3">视频或音频</option>
			</select>
		</div>
		<div class="message_con">
			<label>码流类型：</label><select name="streamType"  class="required">
					  <option  value ="0">主码流</option>
					  <option  value ="1">子码流</option>
			</select>
		</div>
		<div class="message_con">
			<label>存储器类型：</label><select name="storeType"  class="required">
					  <option  value ="0">主存储器或灾备存储器</option>
					  <option  value ="1">主存储器</option>
					  <option  value ="2">灾备存储器</option>
			</select>
		</div>
		<div class="message_con">
			<label>回放方式：</label><select name="playbackType"  class="required">
					  <option  value ="0">正常回放</option>
					  <option  value ="1">快进回放</option>
					  <option  value ="2">关键帧快退回放</option>
					  <option  value ="3">关键帧播放</option>
					  <option  value ="4">单帧上传</option>
			</select>
		</div>
		<div class="message_con">
			<label>快进/快退倍数：</label><select name="playTimes"  class="required">
					  <option  value ="0">无效</option>
					  <option  value ="1">1</option>
					  <option  value ="2">2</option>
					  <option  value ="3">4</option>
					  <option  value ="4">8</option>
					  <option  value ="5">16</option>
			</select>
		</div>
		<div class="message_con">
			<label>开始时间：</label><input name="startTime" type="text" readonly class="required date form_datetime" data-date-format="yyyy-mm-dd hh:ii:ss"  placeholder="设置起始时间"><span class="must">*</span>
		</div>
		<div class="message_con">
			<label>结束时间：</label><input name="endTime" type="text" readonly class="required date form_datetime" data-date-format="yyyy-mm-dd hh:ii:ss"  placeholder="设置结束时间"><span class="must">*</span>
		</div>
		<div class="message_footer"> 
     		<button type="button" class="bule" onclick="sendCmd()">确定</button> 
    		<button type="button" class="orgen" onclick="closeDialog()">取消</button> 
    	</div>
	</form>
</div>


<div id="send-remote-video-playback-ctl-cmd-dlg" class="simple-dialog"  title="远程录像回放控制" style="width:650px;height:400px;">
	<form id="send-remote-video-playback-ctl-cmd-dlg-frm" method="post">
		<input name="deviceId" id="send-remote-video-playback-ctl-cmd-dlg-frm-deviceId" type="hidden">
		<input name="messageId" type="hidden" value="9202">
		<div class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label><input name="name" readonly  id="send-remote-video-playback-ctl-cmd-dlg-frm-name" type="text">
		</div>
		<div class="message_con">
			<label>逻辑通道号：</label><select name="channelId"  class="required">
					  
			</select>
		</div>
		<div class="message_con">
			<label>回放控制：</label><select name="playbackType"  class="required">
					  <option  value ="0">开始回放</option>
					  <option  value ="1">暂停回放</option>
					  <option  value ="2">结束回放</option>
					  <option  value ="3">快进回放</option>
					  <option  value ="4">关键帧快退回放</option>
					  <option  value ="5">拖动回放</option>
					  <option  value ="6">关键帧播放</option>
			</select>
		</div>
		<div class="message_con">
			<label>快进/快退倍数：</label><select name="playTimes"  class="required">
					  <option  value ="0">无效</option>
					  <option  value ="1">1</option>
					  <option  value ="2">2</option>
					  <option  value ="3">4</option>
					  <option  value ="4">8</option>
					  <option  value ="5">16</option>
			</select>
		</div>
		<div class="message_con">
			<label>拖动回放位置：</label><input name="startTime" type="text" readonly class="required date form_datetime" data-date-format="yyyy-mm-dd hh:ii:ss"  placeholder="设置起始时间"><span class="must">*</span>
		</div>
		<div class="message_footer"> 
     		<button type="button" class="bule" onclick="sendCmd()">确定</button> 
    		<button type="button" class="orgen" onclick="closeDialog()">取消</button> 
    	</div>
	</form>
</div>


<div id="send-query-audio-video-list-cmd-dlg" class="simple-dialog"  title="查询音视频资源列表" style="width:650px;height:400px;">
	<form id="send-query-audio-video-list-cmd-dlg-frm" method="post">
		<input name="deviceId" id="send-query-audio-video-list-cmd-dlg-frm-deviceId" type="hidden">
		<input name="messageId" type="hidden" value="9205">
		<input name="alarmFlag" type="hidden" value="0000000000000000000000000000000000000000000000000000000000000000">
		<div class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label><input name="name" readonly  id="send-query-audio-video-list-cmd-dlg-frm-name" type="text">
		</div>
		<div class="message_con">
			<label>逻辑通道号：</label><select name="channelId"  class="required">
					  
			</select>
		</div>
		<div class="message_con">
			<label>开始时间：</label><input name="startTime" type="text" readonly class="required date form_datetime" data-date-format="yyyy-mm-dd hh:ii:ss"  placeholder="设置起始时间"><span class="must">*</span>
		</div>
		<div class="message_con">
			<label>结束时间：</label><input name="endTime" type="text"  readonly class="required date form_datetime" data-date-format="yyyy-mm-dd hh:ii:ss"  placeholder="设置结束时间"><span class="must">*</span>
		</div>
		<div class="message_con">
			<label>资源类型：</label><select name="resourceType"  class="required">
					  <option  value ="0">音视频</option>
					  <option  value ="1">音频</option>
					  <option  value ="2">视频</option>
					  <option  value ="3">视频或音频</option>
			</select>
		</div>
		<div class="message_con">
			<label>码流类型：</label><select name="streamType"  class="required">
					  <option  value ="0">主码流</option>
					  <option  value ="1">子码流</option>
			</select>
		</div>
		<div class="message_con">
			<label>存储器类型：</label><select name="storeType"  class="required">
					  <option  value ="0">主存储器或灾备存储器</option>
					  <option  value ="1">主存储器</option>
					  <option  value ="2">灾备存储器</option>
			</select>
		</div>
		<div class="message_footer"> 
     		<button type="button" class="bule" onclick="sendCmd()">确定</button> 
    		<button type="button" class="orgen" onclick="closeDialog()">取消</button> 
    	</div>
	</form>
</div>


<div id="send-upload-audio-video-file-cmd-dlg" class="simple-dialog"  title="上传终端音视频文件" style="width:650px;height:500px;">
	<form id="send-upload-audio-video-file-cmd-dlg-frm" method="post">
		<input name="deviceId" id="send-upload-audio-video-file-cmd-dlg-frm-deviceId" type="hidden">
		<input name="messageId" type="hidden" value="9206">
		<input name="alarmFlag" type="hidden" value="0000000000000000000000000000000000000000000000000000000000000000">
		
		<div class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label><input name="name" readonly  id="send-upload-audio-video-file-cmd-dlg-frm-name" type="text">
		</div>
		<div class="message_con">
			<label>服务器IP：</label><input name="ip" maxlength="64"  class="required ip" type="text"><span class="must">*</span>
		</div>
		<div class="message_con">
			<label>FTP端口号：</label><input name="port" maxlength="5"  class="required integer" type="text"><span class="must">*</span>
		</div>
		<div class="message_con">
			<label>FTP用户名：</label><input name="userName" maxlength="16"  class="required"  type="text"><span class="must">*</span>
		</div>
		<div class="message_con">
			<label>FTP密码：</label><input name="password" maxlength="16"  class="required"  type="text"><span class="must">*</span>
		</div>
		<div class="message_con">
			<label>文件上传路径：</label><input name="filePath" maxlength="64"  class="required"  type="text"><span class="must">*</span>
		</div>
		<div class="message_con">
			<label>逻辑通道号：</label><select name="channelId"  class="required">
					  
			</select>
		</div>
		<div class="message_con">
			<label>开始时间：</label><input name="startTime" type="text" readonly class="required date form_datetime" data-date-format="yyyy-mm-dd hh:ii:ss" data-picker-position="top-right" placeholder="设置起始时间"><span class="must">*</span>
		</div>
		<div class="message_con">
			<label>结束时间：</label><input name="endTime" type="text"  readonly class="required date form_datetime" data-date-format="yyyy-mm-dd hh:ii:ss"  data-picker-position="top-right" placeholder="设置结束时间"><span class="must">*</span>
		</div>
		<div class="message_con">
			<label>资源类型：</label><select name="resourceType"  class="required">
					  <option  value ="0">音视频</option>
					  <option  value ="1">音频</option>
					  <option  value ="2">视频</option>
					  <option  value ="3">视频或音频</option>
			</select>
		</div>
		<div class="message_con">
			<label>码流类型：</label><select name="streamType"  class="required">
					  <option  value ="0">主码流</option>
					  <option  value ="1">子码流</option>
			</select>
		</div>
		<div class="message_con">
			<label>存储器类型：</label><select name="storeType"  class="required">
					  <option  value ="0">主存储器或灾备存储器</option>
					  <option  value ="1">主存储器</option>
					  <option  value ="2">灾备存储器</option>
			</select>
		</div>
		<div class="message_con">
			<label>上传条件：</label><select name="condition"  class="required">
					  <option  value ="7">不限制</option>
					  <option  value ="1">wifi</option>
					  <option  value ="2">LAN</option>
					  <option  value ="4">3G/4G</option>
			</select>
		</div>
		<div class="message_footer"> 
     		<button type="button" class="bule" onclick="sendCmd()">确定</button> 
    		<button type="button" class="orgen" onclick="closeDialog()">取消</button> 
    	</div>
	</form>
</div>


<div id="send-upload-audio-video-file-ctl-cmd-dlg" class="simple-dialog"  title="终端音视频上传控制" style="width:650px;height:250px;">
	<form id="send-upload-audio-video-file-ctl-cmd-dlg-frm" method="post">
		<input name="deviceId" id="send-upload-audio-video-file-ctl-cmd-dlg-frm-deviceId" type="hidden">
		<input name="messageId" type="hidden" value="9207">
		<div class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label><input name="name" readonly  id="send-upload-audio-video-file-ctl-cmd-dlg-frm-name" type="text">
		</div>
		<div class="message_con">
			<label>应答流水号：</label><input name="messageSeq"  maxlength="5"  class="required" type="text"><span class="must">*</span>
		</div>
		<div class="message_con">
			<label>方向：</label><select name="flag"  class="required">
					  <option  value ="0">暂停</option>
					  <option  value ="1">继续</option>
					  <option  value ="2">取消</option>
			</select>
		</div>
		<div class="message_footer"> 
     		<button type="button" class="bule" onclick="sendCmd()">确定</button> 
    		<button type="button" class="orgen" onclick="closeDialog()">取消</button> 
    	</div>
	</form>
</div>

<div id="send-query-audio-video-param-dlg" class="simple-dialog"  title="查询终端音视频属性" style="width:650px;height:150px;">
	<form id="send-query-audio-video-param-dlg-frm" method="post">
		<input name="deviceId" id="send-query-audio-video-param-dlg-frm-deviceId" type="hidden">
		<input name="messageId" type="hidden" value="9003">
		<div class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label><input name="name" readonly  id="send-query-audio-video-param-dlg-frm-name" type="text">
		</div>
		<div class="message_footer"> 
	     		<button type="button" class="bule" onclick="sendCmd()">确定</button> 
	    		<button type="button" class="orgen" onclick="closeDialog()">取消</button> 
	    </div>
	</form>
</div>