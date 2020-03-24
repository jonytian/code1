<%@ page language="java" contentType="text/html; charset=utf-8" 	pageEncoding="utf-8"%>
<div id="edit-dlg" title="设备信息" class="simple-dialog" style="width:650px; height:400px;">
	<form id="edit-frm" method="post">
		<input name="id" id="edit-frm-id" type="hidden">
		<input name="groupId" id="edit-frm-groupId" type="hidden">
		<input name="enterpriseId" id="edit-frm-enterpriseId" type="hidden">
		<input name="state" id="edit-frm-state" type="hidden">
		<input name="oldSimCode" id="edit-frm-oldSimCode" type="hidden">
		<input name="vedioProtocol" id="edit-frm-vedioProtocol" type="hidden">
		<div class="message_con">
                        <%--midify by tyj-20200307--%>
			<label>设备名称：</label><input name="name" id="edit-frm-name" maxlength="16"  class="required" type="text" placeholder="填车牌号码" onkeyup="this.value=this.value.replace(/[, ]/g,'')"><span class="must">*</span>
		</div>
		<div class="message_con">
			<label>协议版本：</label><select name="protocolVersion" id="edit-frm-protocolVersion">
					  <option  value ="2011">JT/T 808-2011</option>
					  <option  value ="2013">JT/T 808-2013</option>
					  <option  value ="2016">JT/T 808-2011+1078-2016</option>
					  <option  value ="201602">JT/T 808-2013+1078-2016</option>
					  <option  value ="2019">JT/T 808-2019</option>
					  <option  value ="tjsatl">T/JSATL-2018（苏标）</option>
				</select>
		</div>
		<div class="message_con">
			<label>设&nbsp;&nbsp;备&nbsp;&nbsp;ID：</label><input name="simCode" id="edit-frm-simCode" maxlength="12" type="text"  rangelength="12,12" class="required digits" placeholder="终端对应的设备ID(对应协议定义的sim卡号码)，12位，不足前面补0"><span class="must">*</span>
		</div>
		<div class="message_con">
			<label>通道数量：</label><input name="vedioChannel" id="edit-frm-vedioChannel" maxlength="2" type="text" class="required digits" placeholder="填写视频通道数量">
		</div>
		<div class="message_con" style="display:none;">
			<label>终&nbsp;&nbsp;端&nbsp;&nbsp;id：</label><input name="terminalId" id="edit-frm-terminalId" maxlength="7" rangelength="7,7"  class="required"  type="text"  placeholder="对应终端id，7位字母或者数字，终端注册时验证此id"><span class="must">*</span>
		</div>
		<div class="message_con">
			<label>鉴&nbsp;&nbsp;权&nbsp;&nbsp;码：</label><input name="authCode" id="edit-frm-authCode"  maxlength="16" rangelength="6,16" type="text"  class="required"  placeholder="终端鉴权时验证该鉴权码"><span class="must">*</span>
		</div>
		<div class="message_con" id="edit-frm-state-div">
			<label>设备状态：</label><select name="select-state" id="edit-frm-state-select">
					  <option  value ="0">未注册</option>
					  <option  value ="1">已注册</option>
				</select>
		</div>
		<div class="message_con" id="edit-frm-isCreateCar-div">
			<label>关联车辆：</label><input name="isCreateCar" value="1" checked="checked" type="checkbox"><p style="color:red;">勾选自动生成关联车辆</p>
		</div>
		<div class="message_footer">
			<button type="button" class="bule" onclick="save()">保存</button>
			<button type="button" class="orgen" onclick="closeDialog()">取消</button>
		</div>
	</form>
</div>

<%--modify by tyj 20200317--%>
<div id="device-import-dlg" title="批量导入设备" class="simple-dialog"  style="width:650px;height:150px;">
	<div id="file_upload"></div>
	<div style="width:100%;margin-top:10px;"><a href="http://www.legaoyi.com/file/设备批量导入模板.zip" style="margin-left: 30px;
	 display: inline-block;
    cursor: pointer;
    padding: 5px 15px;
    text-decoration: none;
    font-weight: bold;
    color: #fff;
    background-color: #39c;
    -moz-border-radius: 5px;
    -webkit-border-radius: 5px;
    -khtml-border-radius: 5px;
    border-radius: 5px;
    text-decoration: none;
    font-weight: bold;
    color: #fff;
    background-color: #39c;
    -moz-border-radius: 5px;
    -webkit-border-radius: 5px;
    -khtml-border-radius: 5px;
    border-radius: 5px
" target="_blak">请点击下载导入模板文件</a></div>
</div>