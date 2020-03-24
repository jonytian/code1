<%@page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<div id="send-take-picture-cmd-dlg" title="实时抓拍指令" class="simple-dialog"
	style="width: 650px; height: 180px;">
	<form id="send-take-picture-cmd-dlg-frm" method="post">
		<input name="deviceId" id="send-take-picture-cmd-dlg-frm-deviceId"
			type="hidden" /> <input name="messageId" type="hidden" value="8801" />
		<input name="commandType" type="hidden" value="2" /> <input
			name="totalPicture" type="hidden" value="1" /> <input
			name="executionTime" type="hidden" value="0" /> <input
			name="saveFlag" type="hidden" value="0" /> <input name="resolution"
			type="hidden" value="2" /> <input name="quality" type="hidden"
			value="1" /> <input name="luminance" type="hidden" value="127" /> <input
			name="contrast" type="hidden" value="1" /> <input name="saturation"
			type="hidden" value="1" /> <input name="chromaticity" type="hidden"
			value="1" />
		<div class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label> <input name="name"
				readonly id="send-take-picture-cmd-dlg-frm-name" type="text" />
		</div>
		<div class="message_con">
			<label>拍摄通道：</label> <select name="channelId" class="required"
				placeholder="调用哪个摄像头拍摄">
			</select> <span class="must">*</span>
		</div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="sendCmd()">确定</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>

<div id="send-live-video-cmd-dlg" title="视频监控指令" class="simple-dialog"
	style="width: 650px; height: 180px;">
	<form id="send-live-video-cmd-dlg-frm" method="post">
		<input name="deviceId" id="send-live-video-cmd-dlg-frm-deviceId" type="hidden" />
		<input name="messageId" type="hidden" value="8801" />
		<input name="commandType" type="hidden" value="1" />
        <input name="executionTime" type="hidden" value="0" />
        <input name="saveFlag" type="hidden" value="0" />
        <input name="resolution" type="hidden" value="3" />
        <input name="quality" type="hidden" value="5" />
        <input name="luminance" type="hidden" value="127" />
        <input name="contrast" type="hidden" value="10" />
        <input name="saturation" type="hidden" value="10" />
        <input name="chromaticity" type="hidden" value="127" />
		<div class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label> <input name="name"
				readonly id="send-live-video-cmd-dlg-frm-name" type="text" />
		</div>
		<div class="message_con">
			<label>拍摄通道：</label> <select name="channelId" class="required"
				placeholder="调用哪个摄像头录像">
			</select> <span class="must">*</span>
		</div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="start808Video()">确定</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>

<div id="send-live-video-cmd-dlg-2016" title="视频监控指令" style="width: 650px; height: 180px;" class="simple-dialog">
	<form id="send-live-video-cmd-dlg-2016-frm" method="post">
		<input name="deviceId" id="send-live-video-cmd-dlg-2016-frm-deviceId" type="hidden" />
		<input name="dataType" type="hidden" value="1" />
        <input name="streamType" type="hidden" value="1" />
        <input name="messageId" type="hidden" value="9101" />
		<div class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label> <input name="name" readonly id="send-live-video-cmd-dlg-2016-frm-name" type="text" />
		</div>
		<div class="message_con">
			<label>拍摄道号：</label> <select name="channelId" class="required"></select>
			<span class="must">*</span>
		</div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="start1078Video()">确定</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>

<div id="send-phone-tapping-cmd-dlg" title="语音监听指令"
	style="width: 650px; height: 200px;" class="simple-dialog">
	<form id="send-phone-tapping-cmd-dlg-frm" method="post">
		<input name="deviceId" id="send-phone-tapping-cmd-dlg-frm-deviceId"
			type="hidden" /> <input name="messageId" type="hidden" value="8400" />
		<input name="type" type="hidden" value="1" />
		<div class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label> <input name="name"
				readonly id="send-phone-tapping-cmd-dlg-frm-name" type="text" />
		</div>
		<div class="message_con">
			<label>回拨电话：</label> <input name="tel" maxlength="16" type="text"
				class="required" placeholder="接听的手机号或者电话号码" /> <span class="must">*</span>
		</div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="sendCmd()">确定</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>

<div id="send-phone-call-cmd-dlg" title="远程通话指令"
	style="width: 650px; height: 200px;" class="simple-dialog">
	<form id="send-phone-call-cmd-dlg-frm" method="post">
		<input name="deviceId" id="send-phone-call-cmd-dlg-frm-deviceId"
			type="hidden" /> <input name="messageId" type="hidden" value="8400" />
		<input name="type" type="hidden" value="0" />
		<div class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label> <input name="name"
				readonly id="send-phone-call-cmd-dlg-frm-name" type="text" />
		</div>
		<div class="message_con">
			<label>回拨电话：</label> <input name="tel" maxlength="16" type="text"
				class="required" placeholder="接听的手机号或者电话号码" /> <span class="must">*</span>
		</div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="sendCmd()">确定</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>

<div id="send-car-lock-cmd-dlg" title="车门加锁指令"
	style="width: 650px; height: 150px;" class="simple-dialog">
	<form id="send-car-lock-cmd-dlg-frm" method="post">
		<input name="deviceId" id="send-car-lock-cmd-dlg-frm-deviceId"
			type="hidden" /> <input name="messageId" type="hidden" value="8500" />
		<input name="flag" type="hidden" value="10000000" />
		<div class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label> <input name="name"
				readonly id="send-car-lock-cmd-dlg-frm-name" type="text" />
		</div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="sendCmd()">确定</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>

<div id="send-car-unlock-cmd-dlg" title="车门解锁指令"
	style="width: 650px; height: 135px;" class="simple-dialog">
	<form id="send-car-unlock-cmd-dlg-frm" method="post">
		<input name="deviceId" id="send-car-unlock-cmd-dlg-frm-deviceId"
			type="hidden" /> <input name="messageId" type="hidden" value="8500" />
		<input name="flag" type="hidden" value="00000000" />
		<div class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label> <input name="name"
				readonly id="send-car-unlock-cmd-dlg-frm-name" type="text" />
		</div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="sendCmd()">确定</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>

<div id="send-text-info-cmd-dlg" title="文本信息指令"
	style="width: 650px; height: 350px;" class="simple-dialog">
	<form id="send-text-info-cmd-dlg-frm" method="post">
		<input name="deviceId" id="send-text-info-cmd-dlg-frm-deviceId"
			type="hidden" /> <input name="messageId" type="hidden" value="8300" />
		<input name="flag" type="hidden" value="00010000" />
		<div class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label> <input name="name"
				readonly id="send-text-info-cmd-dlg-frm-name" type="text" />
		</div>
		<div class="message_con">
			<label style="vertical-align:top;">文本信息：</label>
			<%--modify by tyj-20200307--%>
			<textarea name="text" style="height: 200px;" class="required" maxlength="500"></textarea><span class="must">*</span>
		</div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="sendCmd()">确定</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>

<div id="send-car-location-query-cmd-dlg" title="位置信息查询"
	style="width: 650px; height: 135px;" class="simple-dialog">
	<form id="send-car-location-query-cmd-dlg-frm" method="post">
		<input name="deviceId"
			id="send-car-location-query-cmd-dlg-frm-deviceId" type="hidden" /> <input
			name="messageId" type="hidden" value="8201" />
		<div class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label> <input name="name"
				readonly id="send-car-location-query-cmd-dlg-frm-name" type="text" />
		</div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="sendCmd()">确定</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>

<div id="send-stop-record-cmd-dlg" title="停止录音"
	style="width: 650px; height: 135px;" class="simple-dialog">
	<form id="send-stop-record-cmd-dlg-frm" method="post">
		<input name="deviceId" id="send-stop-record-cmd-dlg-frm-deviceId"
			type="hidden" /> <input name="messageId" type="hidden" value="8804" />
		<input name="commandType" type="hidden" value="0" />
		<div class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label> <input name="name"
				readonly id="send-stop-record-cmd-dlg-frm-name" type="text" />
		</div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="sendCmd()">确定</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>

<div id="send-start-record-cmd-dlg" title="录音"
	style="width: 650px; height: 280px;" class="simple-dialog">
	<form id="send-start-record-cmd-dlg-frm" method="post">
		<input name="deviceId" id="send-start-record-cmd-dlg-frm-deviceId"
			type="hidden" /> <input name="messageId" type="hidden" value="8804" />
		<input name="commandType" type="hidden" value="1" />
		<div class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label> <input name="name"
				readonly id="send-start-record-cmd-dlg-frm-name" type="text" />
		</div>
		<div class="message_con">
			<label>录音时间：</label> <input name="executionTime"
				class="required number" maxlength="5" type="text" placeholder="单位：秒" /><span
				class="must">*</span>
		</div>
		<div class="message_con">
			<label>保存标识：</label> <select name="saveFlag" class="required">
				<option value="0">实时上传</option>
				<option value="1">终端保存</option>
			</select>
		</div>
		<div class="message_con">
			<label>采样频率：</label> <select name="samplingRate" class="required">
				<option value="0">8K</option>
				<option value="1">11K</option>
				<option value="2">23K</option>
				<option value="3">32K</option>
			</select>
		</div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="sendCmd()">确定</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>

<div id="send-take-one-picture-cmd-dlg" title="单张拍照"
	style="width: 650px; height: 480px;" class="simple-dialog">
	<form id="send-take-one-picture-cmd-dlg-frm" method="post">
		<input name="deviceId" id="send-take-one-picture-cmd-dlg-frm-deviceId"
			type="hidden" /> <input name="messageId" type="hidden" value="8801" />
		<input name="commandType" type="hidden" value="2" /> <input
			name="totalPicture" type="hidden" value="1" /> <input
			name="executionTime" type="hidden" value="0" />
		<div class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label> <input name="name"
				readonly id="send-take-one-picture-cmd-dlg-frm-name" type="text" />
		</div>
		<div class="message_con">
			<label>拍摄通道：</label> <select name="channelId" class="required"
				placeholder="调用哪个摄像头拍摄">
			</select>
		</div>
		<div class="message_con">
			<label>保存标识：</label> <select name="saveFlag">
				<option value="0">实时上传</option>
				<option value="1">终端保存</option>
			</select>
		</div>
		<div class="message_con">
			<label>分辨率：</label> <select name="resolution">
				<option value="1">320*240</option>
				<option value="2">640*480</option>
				<option value="3">800*600</option>
				<option value="4">1024*768</option>
				<option value="5">176*144</option>
				<option value="6">352*288</option>
				<option value="7">704*288</option>
				<option value="8">704*576</option>
			</select>
		</div>
		<div class="message_con">
			<label>图片质量：</label> <select name="quality" class="required">
				<option value="1">1</option>
				<option value="2">2</option>
				<option value="3">3</option>
				<option value="4">4</option>
				<option value="5">5</option>
				<option value="6">6</option>
				<option value="7">7</option>
				<option value="8">8</option>
				<option value="9">9</option>
				<option value="10">10</option>
			</select>
		</div>
		<div class="message_con">
			<label>亮度：</label> <input name="luminance" value="127" maxlength="3"
				max="255" min="0" class="required number" type="text"
				placeholder="取值范围：0~255" /><span class="must">*</span>
		</div>
		<div class="message_con">
			<label>对比度：</label> <input name="contrast" value="10" maxlength="3"
				max="127" min="0" class="required number" type="text"
				placeholder="取值范围：0~127" /><span class="must">*</span>
		</div>
		<div class="message_con">
			<label>饱和度：</label> <input name="saturation" value="10" maxlength="3"
				max="127" min="0" class="required number" type="text"
				placeholder="取值范围：0~127" /><span class="must">*</span>
		</div>
		<div class="message_con">
			<label>色度：</label> <input name="chromaticity" value="127"
				maxlength="3" max="255" min="0" class="required number" type="text"
				placeholder="取值范围：0~255" /><span class="must">*</span>
		</div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="sendCmd()">确定</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>

<div id="send-take-many-picture-cmd-dlg" title="定时拍照"
	style="width: 650px; height: 500px;" class="simple-dialog">
	<form id="send-take-many-picture-cmd-dlg-frm" method="post">
		<input name="deviceId"
			id="send-take-many-picture-cmd-dlg-frm-deviceId" type="hidden" /> <input
			name="messageId" type="hidden" value="8801" /> <input
			name="commandType" type="hidden" value="2" />
		<div class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label> <input name="name"
				readonly id="send-take-many-picture-cmd-dlg-frm-name" type="text" />
		</div>
		<div class="message_con">
			<label>拍摄通道：</label> <select name="channelId" type="text"
				style="width: 25%;" placeholder="调用哪个摄像头拍摄">
			</select> <label style="width: 15%;">保存标识：</label> <select name="saveFlag"
				style="width: 25%;">
				<option value="0">实时上传</option>
				<option value="1">终端保存</option>
			</select>
		</div>
		<div class="message_con">
			<label>拍摄间隔：</label> <input name="executionTime" maxlength="3"
				type="text" value="30" class="required number" placeholder="单位：秒" />
			<span class="must">*</span>
		</div>
		<div class="message_con">
			<label>拍摄数量：</label> <input name="totalPicture" maxlength="3"
				type="text" value="2" class="required number" /><span class="must">*</span>
		</div>
		<div class="message_con">
			<label>分辨率：</label> <select name="resolution" style="width: 25%;">
				<option value="1">320*240</option>
				<option value="2">640*480</option>
				<option value="3">800*600</option>
				<option value="4">1024*768</option>
				<option value="5">176*144</option>
				<option value="6">352*288</option>
				<option value="7">704*288</option>
				<option value="8">704*576</option>
			</select> <label style="width: 15%;">图片质量：</label> <select name="quality"
				style="width: 25%;">
				<option value="1">1</option>
				<option value="2">2</option>
				<option value="3">3</option>
				<option value="4">4</option>
				<option value="5">5</option>
				<option value="6">6</option>
				<option value="7">7</option>
				<option value="8">8</option>
				<option value="9">9</option>
				<option value="10">10</option>
			</select>
		</div>
		<div class="message_con">
			<label>亮度：</label> <input name="luminance" value="127" maxlength="3"
				max="255" min="0" class="required number" type="text"
				placeholder="取值范围：0~255" /><span class="must">*</span>
		</div>
		<div class="message_con">
			<label>对比度：</label> <input name="contrast" value="10" maxlength="3"
				max="127" min="0" class="required number" type="text"
				placeholder="取值范围：0~127" /><span class="must">*</span>
		</div>
		<div class="message_con">
			<label>饱和度：</label> <input name="saturation" value="10" maxlength="3"
				max="127" min="0" class="required number" type="text"
				placeholder="取值范围：0~127" /><span class="must">*</span>
		</div>
		<div class="message_con">
			<label>色度：</label> <input name="chromaticity" value="127"
				maxlength="3" max="255" min="0" class="required number" type="text"
				placeholder="取值范围：0~255" /><span class="must">*</span>
		</div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="sendCmd()">确定</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>

<div id="send-stop-take-picture-cmd-dlg" title="停止拍摄"
	style="width: 650px; height: 200px;" class="simple-dialog">
	<form id="send-stop-take-picture-cmd-dlg-frm" method="post">
		<input name="deviceId"
			id="send-stop-take-picture-cmd-dlg-frm-deviceId" type="hidden" /> <input
			name="messageId" type="hidden" value="8801" /> <input
			name="commandType" type="hidden" value="0" />
		<div class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label> <input name="name"
				readonly id="send-stop-take-picture-cmd-dlg-frm-name" type="text" />
		</div>
		<div class="message_con">
			<label>拍摄通道：</label> <select name="channelId" type="text"
				placeholder="调用哪个摄像头">
			</select>
		</div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="sendCmd()">确定</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>

<div id="send-take-video-cmd-dlg" title="录像采集"
	style="width: 650px; height: 480px;" class="simple-dialog">
	<form id="send-take-video-cmd-dlg-frm" method="post">
		<input name="deviceId" id="send-take-video-cmd-dlg-frm-deviceId"
			type="hidden" /> <input name="messageId" type="hidden" value="8801" />
		<input name="commandType" type="hidden" value="1" />
		<div class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label> <input name="name"
				readonly id="send-take-video-cmd-dlg-frm-name" type="text" />
		</div>
		<div class="message_con">
			<label>录像通道：</label> <select name="channelId" class="required"
				placeholder="调用哪个摄像头录像">
			</select>
		</div>
		<div class="message_con">
			<label>录像时间：</label> <input name="executionTime" maxlength="5"
				type="text" class="required number" placeholder="单位：秒,0表示一直录像" /><span
				class="must">*</span>
		</div>
		<div class="message_con">
			<label>分辨率：</label> <select name="resolution" style="width: 25%;">
				<option value="1">320*240</option>
				<option value="2">640*480</option>
				<option value="3">800*600</option>
				<option value="4">1024*768</option>
				<option value="5">176*144</option>
				<option value="6">352*288</option>
				<option value="7">704*288</option>
				<option value="8">704*576</option>
			</select> <label style="width: 15%;">图片质量：</label> <select name="quality"
				style="width: 25%;">
				<option value="1">1</option>
				<option value="2">2</option>
				<option value="3">3</option>
				<option value="4">4</option>
				<option value="5">5</option>
				<option value="6">6</option>
				<option value="7">7</option>
				<option value="8">8</option>
				<option value="9">9</option>
				<option value="10">10</option>
			</select>
		</div>
		<div class="message_con">
			<label>亮度：</label> <input name="luminance" value="127" maxlength="3"
				max="255" min="0" class="required number" type="text"
				placeholder="取值范围：0~255" /><span class="must">*</span>
		</div>
		<div class="message_con">
			<label>对比度：</label> <input name="contrast" value="10" maxlength="3"
				max="127" min="0" class="required number" type="text"
				placeholder="取值范围：0~127" /><span class="must">*</span>
		</div>
		<div class="message_con">
			<label>饱和度：</label> <input name="saturation" value="10" maxlength="3"
				max="127" min="0" class="required number" type="text"
				placeholder="取值范围：0~127" /><span class="must">*</span>
		</div>
		<div class="message_con">
			<label>色度：</label> <input name="chromaticity" value="127"
				maxlength="3" max="255" min="0" class="required number" type="text"
				placeholder="取值范围：0~255" /><span class="must">*</span>
		</div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="sendCmd()">确定</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>

<div id="send-media-data-query-cmd-dlg" title="存储多媒体数据检索 "
	style="width: 650px; height: 350px;" class="simple-dialog">
	<form id="send-media-data-query-cmd-dlg-frm" method="post">
		<input name="deviceId" id="send-media-data-query-cmd-dlg-frm-deviceId"
			type="hidden" /> <input name="messageId" type="hidden" value="8802" />
		<div class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label> <input name="name"
				readonly id="send-media-data-query-cmd-dlg-frm-name" type="text" />
		</div>
		<div class="message_con">
			<label>多媒体类型：</label> <select name="mediaType" class="required">
				<option value="0">图像</option>
				<option value="1">音频</option>
				<option value="2">视频</option>
			</select>
		</div>
		<div class="message_con">
			<label>拍摄通道：</label> <select name="channelId" class="required">
				<option value="0">所有通道</option>
			</select>
		</div>
		<div class="message_con">
			<label>事件项编码：</label> <select name="eventCode" class="required">
				<option value="0">平台下发指令</option>
				<option value="1">定时动作</option>
				<option value="2">抢劫报警触发</option>
				<option value="3">碰撞侧翻报警触发</option>
			</select>
		</div>
		<div class="message_con">
			<label>开始时间：</label><input name="startTime" type="text" readonly
				class="required date form_datetime"
				data-date-format="yyyy-mm-dd hh:ii:ss"
				data-picker-position="top-right" placeholder="设置起始时间"><span
				class="must">*</span>
		</div>

		<div class="message_con">
			<label>结束时间：</label><input name="endTime" type="text" readonly
				class="required date form_datetime"
				data-date-format="yyyy-mm-dd hh:ii:ss"
				data-picker-position="top-right" placeholder="设置结束时间"><span
				class="must">*</span>
		</div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="sendCmd()">确定</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>

<div id="send-media-data-upload-cmd-dlg" title="存储多媒体数据上传命令  "
	style="width: 650px; height: 400px;" class="simple-dialog">
	<form id="send-media-data-upload-cmd-dlg-frm" method="post">
		<input name="deviceId"
			id="send-media-data-upload-cmd-dlg-frm-deviceId" type="hidden" /> <input
			name="messageId" type="hidden" value="8803" />
		<div class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label> <input name="name"
				readonly id="send-media-data-upload-cmd-dlg-frm-name" type="text" />
		</div>
		<div class="message_con">
			<label>多媒体类型：</label> <select name="mediaType" class="required">
				<option value="0">图像</option>
				<option value="1">音频</option>
				<option value="2">视频</option>
			</select>
		</div>
		<div class="message_con">
			<label>拍摄通道：</label> <select name="channelId" class="required"
				placeholder="调用哪个摄像头录像">
				<option value="0">所有通道</option>
			</select>
		</div>
		<div class="message_con">
			<label>事件项编码：</label> <select name="eventCode" type="text"
				class="required">
				<option value="0">平台下发指令</option>
				<option value="1">定时动作</option>
				<option value="2">抢劫报警触发</option>
				<option value="3">碰撞侧翻报警触发</option>
			</select>
		</div>
		<div class="message_con">
			<label>保存标识：</label> <select name="saveFlag" class="required">
				<option value="0">保留</option>
				<option value="1">删除</option>
			</select>
		</div>
		<div class="message_con">
			<label>开始时间：</label><input name="startTime" type="text" readonly
				class="required date form_datetime"
				data-date-format="yyyy-mm-dd hh:ii:ss"
				data-picker-position="top-right" placeholder="设置起始时间"><span
				class="must">*</span>
		</div>
		<div class="message_con">
			<label>结束时间：</label><input name="endTime" type="text" readonly
				class="required date form_datetime"
				data-date-format="yyyy-mm-dd hh:ii:ss"
				data-picker-position="top-right" placeholder="设置结束时间"><span
				class="must">*</span>
		</div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="sendCmd()">确定</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>

<div id="send-one-media-data-upload-cmd-dlg" title="单条存储多媒体数据检索上传命令"
	style="width: 650px; height: 250px;" class="simple-dialog">
	<form id="send-one-media-data-upload-cmd-dlg-frm" method="post">
		<input name="deviceId"
			id="send-one-media-data-upload-cmd-dlg-frm-deviceId" type="hidden" />
		<input name="messageId" type="hidden" value="8805" />
		<div class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label> <input name="name"
				readonly id="send-one-media-data-upload-cmd-dlg-frm-name"
				type="text" />
		</div>
		<div class="message_con">
			<label>多媒体id：</label> <input name="mediaDataId" class="required" maxlength="5" 
				type="text" /> <span class="must">*</span>
		</div>
		<div class="message_con">
			<label>保存标识：</label> <select name="saveFlag" class="required">
				<option value="0">保留</option>
				<option value="1">删除</option>
			</select>
		</div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="sendCmd()">确定</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>

<div id="send-text-info-2013-cmd-dlg" title="文本信息下发 "
	style="width: 650px; height: 480px;" class="simple-dialog">
	<form id="send-text-info-2013-cmd-dlg-frm" method="post">
		<input name="deviceId" id="send-text-info-2013-cmd-dlg-frm-deviceId" type="hidden" /> 
		<input name="messageId" type="hidden" value="8300" />
		<input name="flag" id="send-text-info-2013-cmd-dlg-frm-flag" type="hidden" value="10111100" />
		<div class="message_con">
			<label>车牌号：</label> <input name="name" readonly
				id="send-text-info-2013-cmd-dlg-frm-name" type="text" />
		</div>
		<div class="message_con">
			<label>紧急：</label> <select name="urgency" onchange="textInfoOnchange('send-text-info-2013-cmd-dlg-frm-flag',0,this)"  class="required">
				<option value="1">是</option>
				<option value="0">否</option>
			</select>
		</div>
		<div class="message_con">
			<label>终端显示屏显示：</label> <select name="display" onchange="textInfoOnchange2('send-text-info-2013-cmd-dlg-frm-flag',0,this)"  class="required">
				<option value="1">是</option>
				<option value="0">否</option>
			</select>
		</div>
		<div class="message_con">
			<label>终端TTS播读：</label> <select name="ttsread" onchange="textInfoOnchange('send-text-info-2013-cmd-dlg-frm-flag',3,this)"  class="required">
				<option value="1">是</option>
				<option value="0">否</option>
			</select>
		</div>
		<div class="message_con">
			<label>广告屏显示：</label> <select name="advertdisplay" onchange="textInfoOnchange('send-text-info-2013-cmd-dlg-frm-flag',4,this)"  class="required">
				<option value="1">是</option>
				<option value="0">否</option>
			</select>
		</div>
		<div class="message_con">
			<label>信息类型：</label> <select name="type" onchange="textInfoOnchange(5,this)"  class="required">
				<option value="0">中心导航信息</option>
				<option value="1">CAN故障码信息</option>
			</select>
		</div>
		<div class="message_con">
			<label style="vertical-align:top;">文本信息：</label>
			<textarea name="text" style="height: 150px;" class="required"></textarea>
			<span class="must">*</span>
		</div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="sendCmd()">确定</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>

<div id="send-text-info-2011-cmd-dlg" title="文本信息下发 "
	style="width: 650px; height: 460px;" class="simple-dialog">
	<form id="send-text-info-2011-cmd-dlg-frm" method="post">
		<input name="deviceId" id="send-text-info-2011-cmd-dlg-frm-deviceId" type="hidden" /> 
		<input name="messageId" type="hidden" value="8300" />
		<input name="flag" id="send-text-info-2011-cmd-dlg-frm-flag"  type="hidden" value="10111000" />
		<div class="message_con">
			<label>车牌号：</label> <input name="name" readonly
				id="send-text-info-2011-cmd-dlg-frm-name" type="text" />
		</div>
		<div class="message_con">
			<label>紧急：</label> <select name="urgency" onchange="textInfoOnchange('send-text-info-2011-cmd-dlg-frm-flag',0,this)" class="required">
				<option value="1">是</option>
				<option value="0">否</option>
			</select>
		</div>
		<div class="message_con">
			<label>显示屏显示：</label> <select name="display" onchange="textInfoOnchange('send-text-info-2011-cmd-dlg-frm-flag',2,this)" class="required">
				<option value="1">是</option>
				<option value="0">否</option>
			</select>
		</div>
		<div class="message_con">
			<label>TTS播读：</label> <select name="ttsread" onchange="textInfoOnchange('send-text-info-2011-cmd-dlg-frm-flag',3,this)" class="required">
				<option value="1">是</option>
				<option value="0">否</option>
			</select>
		</div>
		<div class="message_con">
			<label>广告屏显示：</label> <select name="advertdisplay" onchange="textInfoOnchange('send-text-info-2011-cmd-dlg-frm-flag',4,this)" class="required">
				<option value="1">是</option>
				<option value="0">否</option>
			</select>
		</div>
		<div class="message_con">
			<label style="vertical-align:top;">文本信息：</label>
			<textarea name="text" style="height: 150px;" class="required"></textarea>
			<span class="must">*</span>
		</div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="sendCmd()">确定</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>

<div id="send-text-info-service-cmd-dlg" title="信息服务 "
	style="width: 650px; height: 380px;" class="simple-dialog">
	<form id="send-text-info-service-cmd-dlg-frm" method="post">
		<input name="deviceId"
			id="send-text-info-service-cmd-dlg-frm-deviceId" type="hidden" /> <input
			name="messageId" type="hidden" value="8304" />
		<div class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label> <input name="name"
				readonly id="send-text-info-service-cmd-dlg-frm-name" type="text" />
		</div>
		<div class="message_con">
			<label>信息类型：</label> <select name="type" class="required">
				<option value="0">新闻</option>
				<option value="1">天气</option>
				<option value="2">娱乐</option>
				<option value="3">广告</option>
				<option value="4">其他</option>
			</select>
		</div>
		<div class="message_con">
			<label style="vertical-align:top;">信息内容：</label>
			<textarea name="info" style="height: 180px;" class="required"></textarea>
			<span class="must">*</span>
		</div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="sendCmd()">确定</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>

<div id="send-location-temporary-query-cmd-dlg" title="临时位置跟踪控制"
	style="width: 650px; height: 230px;" class="simple-dialog">
	<form id="send-location-temporary-query-cmd-dlg-frm" method="post">
		<input name="deviceId"
			id="send-location-temporary-query-cmd-dlg-frm-deviceId" type="hidden" />
		<input name="messageId" type="hidden" value="8202" />
		<div class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label> <input name="name"
				readonly id="send-location-temporary-query-cmd-dlg-frm-name"
				type="text" />
		</div>
		<div class="message_con">
			<label>时间间隔：</label> <input name="interval" maxlength="5" type="text"
				class="required number" placeholder="单位：秒，0表示停止跟踪" /><span
				class="must">*</span>
		</div>
		<div class="message_con">
			<label>跟踪有效期：</label> <input name="expTime" maxlength="8" type="text"
				class="required number" placeholder="单位：秒" /><span class="must">*</span>
		</div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="sendFollowCarCmd()">确定</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>

<div id="send-terminal-parameter-query-cmd-dlg" title="查询终端参数 "
	style="width: 650px; height: 135px;" class="simple-dialog">
	<form id="send-terminal-parameter-query-cmd-dlg-frm" method="post">
		<input name="deviceId"
			id="send-terminal-parameter-query-cmd-dlg-frm-deviceId" type="hidden" />
		<input name="messageId" type="hidden" value="8104" />
		<div class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label> <input name="name"
				readonly id="send-terminal-parameter-query-cmd-dlg-frm-name"
				type="text" />
		</div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="sendCmd()">确定</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>

<div id="send-terminal-control-shutdown-cmd-dlg" title="终端关机指令 "
	style="width: 650px; height: 135px;" class="simple-dialog">
	<form id="send-terminal-control-shutdown-cmd-dlg-frm" method="post">
		<input name="deviceId"
			id="send-terminal-control-shutdown-cmd-dlg-frm-deviceId"
			type="hidden" /> <input name="messageId" type="hidden" value="8105" />
		<input name="commandWord" type="hidden" value="3" />
		<div class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label> <input name="name"
				readonly id="send-terminal-control-shutdown-cmd-dlg-frm-name"
				type="text" />
		</div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="sendCmd()">确定</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>

<div id="send-terminal-control-reset-cmd-dlg" title="终端复位 "
	style="width: 650px; height: 135px;" class="simple-dialog">
	<form id="send-terminal-control-reset-cmd-dlg-frm" method="post">
		<input name="deviceId"
			id="send-terminal-control-reset-cmd-dlg-frm-deviceId" type="hidden" />
		<input name="messageId" type="hidden" value="8105" /> <input
			name="commandWord" type="hidden" value="4" />
		<div class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label> <input name="name"
				readonly id="send-terminal-control-reset-cmd-dlg-frm-name"
				type="text" />
		</div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="sendCmd()">确定</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>

<div id="send-terminal-control-reset-factory-setting-cmd-dlg"
	title="终端恢复出厂设置 " style="width: 650px; height: 135px;"
	class="simple-dialog">
	<form id="send-terminal-control-reset-factory-setting-cmd-dlg-frm"
		method="post">
		<input name="deviceId"
			id="send-terminal-control-reset-factory-setting-cmd-dlg-frm-deviceId"
			type="hidden" /> <input name="messageId" type="hidden" value="8105" />
		<input name="commandWord" type="hidden" value="5" />
		<div class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label> <input name="name"
				readonly
				id="send-terminal-control-reset-factory-setting-cmd-dlg-frm-name"
				type="text" />
		</div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="sendCmd()">确定</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>

<div id="send-terminal-control-turn-off-data-communication-cmd-dlg"
	title="关闭数据通信 " style="width: 650px; height: 135px;"
	class="simple-dialog">
	<form
		id="send-terminal-control-turn-off-data-communication-cmd-dlg-frm"
		method="post">
		<input name="deviceId"
			id="send-terminal-control-turn-off-data-communication-cmd-dlg-frm-deviceId"
			type="hidden" /> <input name="messageId" type="hidden" value="8105" />
		<input name="commandWord" type="hidden" value="6" />
		<div class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label> <input name="name"
				readonly
				id="send-terminal-control-turn-off-data-communication-cmd-dlg-frm-name"
				type="text" />
		</div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="sendCmd()">确定</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>

<div id="send-terminal-control-turn-off-wireless-communication-cmd-dlg"
	title="关闭所有无线通信 " style="width: 650px; height: 135px;"
	class="simple-dialog">
	<form
		id="send-terminal-control-turn-off-wireless-communication-cmd-dlg-frm"
		method="post">
		<input name="deviceId"
			id="send-terminal-control-turn-off-wireless-communication-cmd-dlg-frm-deviceId"
			type="hidden" /> <input name="messageId" type="hidden" value="8105" />
		<input name="commandWord" type="hidden" value="7" />
		<div class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label> <input name="name"
				readonly
				id="send-terminal-control-turn-off-wireless-communication-cmd-dlg-frm-name"
				type="text" />
		</div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="sendCmd()">确定</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>

<!-- 终端参数设置开始 -->
<div id="send-terminal-parameter-heartbeat-cmd-dlg" title="心跳间隔 "
	style="width: 650px; height: 180px;" class="simple-dialog">
	<form id="send-terminal-parameter-heartbeat-cmd-dlg-frm" method="post">
		<input name="deviceId"
			id="send-terminal-parameter-heartbeat-cmd-dlg-frm-deviceId"
			type="hidden" /> <input name="messageId" type="hidden" value="8103" />
		<input name="paramkeys" type="hidden" value="d_0001" />
		<div class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label> <input name="name"
				readonly id="send-terminal-parameter-heartbeat-cmd-dlg-frm-name"
				type="text" />
		</div>
		<div class="message_con">
			<label>时间间隔：</label> <input name="d_0001" maxlength="3" type="text"
				class="required number" placeholder="单位：秒" /><span class="must">*</span>
		</div>
		<div class="message_footer">
			<button type="button" class="bule"
				onclick="sendTerminalParameterSettingCmd()">确定</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>

<div id="send-terminal-parameter-report-strategy-cmd-dlg" title="汇报策略 "
	style="width: 700px; height: 480px;" class="simple-dialog">
	<form id="send-terminal-parameter-report-strategy-cmd-dlg-frm"
		method="post">
		<input name="deviceId"
			id="send-terminal-parameter-report-strategy-cmd-dlg-frm-deviceId"
			type="hidden" /> <input name="messageId" type="hidden" value="8103" />
		<input name="paramkeys" type="hidden"
			value="d_0029,d_002C,d_0020,d_0021,d_0028,d_002F,d_0022,d_0027,d_002E" />
		<div class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label> <input name="name"
				readonly
				id="send-terminal-parameter-report-strategy-cmd-dlg-frm-name"
				type="text" />
		</div>
		<div class="message_con">
			<label>缺省汇报时间间隔：</label> <input name="d_0029" maxlength="3"
				type="text" class="integer" placeholder="单位：秒" />
		</div>
		<div class="message_con">
			<label>缺省汇报距离间隔：</label> <input name="d_002C" maxlength="3"
				type="text" class="integer" placeholder="单位：米" />
		</div>
		<div class="message_con">
			<label>位置汇报策略：</label> <select name="d_0020">
				<option value="">请选择</option>
				<option value="0">定时汇报</option>
				<option value="1">定距汇报</option>
				<option value="2">定时和定距汇报</option>
			</select>
		</div>
		<div class="message_con">
			<label>位置汇报方案：</label> <select name="d_0021">
				<option value="">请选择</option>
				<option value="0">根据ACC状态</option>
				<option value="1">根据登录状态和ACC状态</option>
			</select>
		</div>
		<div class="message_con">
			<label>紧急报警时汇报时间间隔：</label> <input name="d_0028" maxlength="3"
				type="text" class="integer" placeholder="单位：秒" />
		</div>
		<div class="message_con">
			<label>紧急报警时汇报距离间隔：</label> <input name="d_002F" maxlength="3"
				type="text" class="integer" placeholder="单位：米" />
		</div>
		<!--  
		<div class="message_con">
			<label>驾驶员未登录汇报时间间隔：</label><input name="d_0022" maxlength="3" type="text"  class="integer" placeholder="单位：秒">
		</div>
		<div class="message_con">
			<label>驾驶员未登录汇报距离间隔：</label><input name="d_002D" maxlength="3" type="text"  class="integer" placeholder="单位：米">
		</div>
		
		<div class="message_con">
			<label>空车汇报时间间隔：</label><input name="d_0001" maxlength="3"  type="text" class="required" validType="integer">(秒)
		</div>
		<div class="message_con">
			<label>空车汇报距离间隔：</label><input name="d_0001" maxlength="3"  type="text" class="required" validType="integer">(米)
		</div>
		<div class="message_con">
			<label>重车汇报时间间隔：</label><input name="d_0001" maxlength="3"  type="text" class="required" validType="integer">(秒)
		</div>
		<div class="message_con">
			<label>重车汇报距离间隔：</label><input name="d_0001" maxlength="3"  type="text" class="required" validType="integer">(米)
		</div>-->
		<div class="message_con">
			<label>休眠时汇报时间间隔：</label> <input name="d_0027" maxlength="3"
				type="text" class="integer" placeholder="单位：秒" />
		</div>
		<div class="message_con">
			<label>休眠时汇报距离间隔：</label> <input name="d_002E" maxlength="3"
				type="text" class="integer" placeholder="单位：米" />
		</div>
		<div class="message_footer">
			<button type="button" class="bule"
				onclick="sendTerminalParameterSettingCmd()">确定</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>
<!-- 终端参数设置开始 结束-->

<!-- 行驶记录仪数据采集开始 -->
<div id="send-dvr-data-record-cmd-dlg" title="采集行驶记录仪数据"
	style="width: 650px; height: 135px;" class="simple-dialog">
	<form id="send-dvr-data-record-cmd-dlg-frm" method="post">
		<input name="deviceId" id="send-dvr-data-record-cmd-dlg-frm-deviceId"
			type="hidden" /> <input name="messageId" type="hidden" value="8700" />
		<input name="commandWord"
			id="send-dvr-data-record-cmd-dlg-frm-commandWord" type="hidden"
			value="00H" />
		<div class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label> <input name="name"
				readonly id="send-dvr-data-record-cmd-dlg-frm-name" type="text" />
		</div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="sendCmd()">确定</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>

<div id="send-dvr2013-data-record-cmd-dlg" title="采集行驶记录仪数据"
	style="width: 650px; height: 250px;" class="simple-dialog">
	<form id="send-dvr2013-data-record-cmd-dlg-frm" method="post">
		<input name="deviceId"
			id="send-dvr2013-data-record-cmd-dlg-frm-deviceId" type="hidden" />
		<input name="messageId" type="hidden" value="8700" /> <input
			name="commandWord"
			id="send-dvr2013-data-record-cmd-dlg-frm-commandWord" type="hidden"
			value="09H" />
		<div class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label> <input name="name"
				readonly id="send-dvr2013-data-record-cmd-dlg-frm-name" type="text" />
		</div>
		<div class="message_con">
			<label>开始时间：</label><input name="startTime" type="text" readonly
				class="required date form_datetime"
				data-date-format="yyyy-mm-dd hh:ii:ss"
				data-picker-position="top-right" placeholder="设置起始时间"><span
				class="must">*</span>
		</div>
		<div class="message_con">
			<label>结束时间：</label><input name="endTime" type="text" readonly
				class="required date form_datetime"
				data-date-format="yyyy-mm-dd hh:ii:ss"
				data-picker-position="top-right" placeholder="设置结束时间"><span
				class="must">*</span>
		</div>
		<div class="message_con">
			<label>数据块数：</label> <input name="count" class="required"
				maxlength="3" value="100" type="text" />
		</div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="sendCmd()">确定</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>
<!-- 行驶记录仪数据采集结束 -->

<div id="send-test-cmd-dlg" title="指令测试"
	style="width: 580px; height: 500px;" class="simple-dialog">
	<form id="send-test-cmd-dlg-frm" method="post">
		<input name="deviceId" id="send-test-cmd-dlg-frm-deviceId"
			type="hidden" />
		<div class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label> <input name="name"
				readonly id="send-test-cmd-dlg-frm-name" type="text" />
		</div>
		<div class="message_con">
			<label>消息ID：</label> <input name="messageId" value="8500" type="text"
				class="required" /><span class="must">*</span>
		</div>
		<div class="message_con">
			<label style="vertical-align:top;">消息体：</label>
			<textarea id="send-test-cmd-dlg-frm-messageBody"
				style="height: 200px;" class="required"> {"flag":"10000000"} </textarea>
		</div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="sendTestCmd()">确定</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>
<%@ include file="cmdSetting-rail.jsp"%>