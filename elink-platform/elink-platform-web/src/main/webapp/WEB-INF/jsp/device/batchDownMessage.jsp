<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="<c:url value='/css/tail.css'/>">
<link rel="stylesheet" href="<c:url value='/css/slider-range.css?v=1'/>">
<%@ include file="/common/header.jsp"%>
<title>批量下行消息</title>
<%@ include file="/common/common.jsp"%>
</head>
<body>
<section class="sm_section left" style="width:100%;">
 <fieldset class="message_fieldset">
            <legend>终端参数</legend>
            <div class="message_footer" style="margin-top:0px;">
                <button type="button" class="bule" onclick="showCmdDialog('send-terminal-parameter-heartbeat-cmd-dlg')">心跳间隔</button>
                <button type="button" class="bule" onclick="showCmdDialog('send-terminal-parameter-report-strategy-cmd-dlg')">位置汇报策略</button>
                <button type="button" class="bule" onclick="showCmdDialog('send-terminal-parameter-server-cmd-dlg')">服务器信息</button>
                <button type="button" class="bule" onclick="showCmdDialog('send-8103-0050-cmd-dlg')">报警开关</button>
                <button type="button" class="bule" onclick="showCmdDialog('send-8103-0054-cmd-dlg')">关键报警开关</button>
                <button type="button" class="bule" onclick="showCmdDialog('send-8103-0052-cmd-dlg')">报警拍摄开关</button>
                <button type="button" class="bule" onclick="showCmdDialog('send-8103-0053-cmd-dlg')">报警拍摄存储</button>

                <button type="button" class="bule" onclick="showCmdDialog('send-8103-0079-cmd-dlg')">特殊报警录像</button>
                <button type="button" class="bule" onclick="showCmdDialog('send-8103-007A-cmd-dlg')">视频报警屏蔽字</button>
                <button type="button" class="bule" onclick="showCmdDialog('send-8103-007B-cmd-dlg')">图像分析报警</button>

                <button type="button" class="bule" onclick="showCmdDialog('send-adas-cmd-dlg')">ADAS参数</button>
                <button type="button" class="bule" onclick="showCmdDialog('send-dsm-cmd-dlg')">DMS参数</button>
                <button type="button" class="bule" onclick="showCmdDialog('send-tpms-cmd-dlg')">TPMS参数</button>
                <button type="button" class="bule" onclick="showCmdDialog('send-bsd-cmd-dlg')">BSD参数</button>
            </div>
</fieldset>
 <fieldset class="message_fieldset">
            <legend id="legend">心跳间隔设置</legend>
            <div id="send-terminal-parameter-heartbeat-cmd-dlg" title="心跳间隔设置 ">
				<form id="send-terminal-parameter-heartbeat-cmd-dlg-frm" method="post">
				    <input name="messageId" type="hidden" value="8103">
				    <input name="paramkeys" type="hidden" value="d_0001">
					<div class="message_con">
						<label>心跳间隔：</label><input name="d_0001" id="p0001" maxlength="3" type="text" class="required number" placeholder="单位：秒"><span class="must">*</span>
					</div>
				</form>
			</div>
			<div id="send-terminal-parameter-server-cmd-dlg" class="simple-dialog" title="服务器设置 ">
				<form id="send-terminal-parameter-server-cmd-dlg-frm" method="post">
				    <input name="messageId" type="hidden" value="8103">
				    <input name="paramkeys" type="hidden" value="s_0013,s_0017,s_0018,s_0019">
					<div class="message_con">
						<label style='width:200px;'>主服务器地址：</label><input name="s_0013"  id="p0013" maxlength="64" type="text"  class="required" placeholder="主服务器地址,ip或域名 "><span class="must">*</span>
					</div>
					<div class="message_con">
						<label style='width:200px;'>备份服务器地址：</label><input name="s_0017" id="p0017" maxlength="64" type="text"  placeholder="备份服务器地址,ip或域名 ">
					</div>
					<div class="message_con">
						<label style='width:200px;'>服务器TCP端口 ：</label><input name="s_0018" id="p0018" maxlength="5" type="text" class="required number" placeholder="服务器 TCP端口 "><span class="must">*</span>
					</div>
					<div class="message_con">
						<label style='width:200px;'>服务器UDP端口 ：</label><input name="s_0019" id="p0019" maxlength="5" type="text" class="number" placeholder="服务器 UDP端口 ">
					</div>
				</form>
			</div>
			<div id="send-8103-0050-cmd-dlg" class="simple-dialog" title="告警开关设置">
				<form id="send-8103-0050-cmd-dlg-frm" method="post">
					<input name="messageId" type="hidden" value="8103">
					<input name="paramkeys" type="hidden" value="b_0050">
					<div id="cmd_setting_81030050_div"></div>
				</form>
			</div>
			<div id="send-8103-0052-cmd-dlg"  class="simple-dialog" title="报警拍摄开关设置">
				<form id="send-8103-0052-cmd-dlg-frm" method="post">
					<input name="messageId" type="hidden" value="8103">
					<input name="paramkeys" type="hidden" value="b_0052">
					<div id="cmd_setting_81030052_div">
					</div>
				</form>
			</div>
			<div id="send-8103-0053-cmd-dlg"  class="simple-dialog" title="报警拍摄存储开关设置">
				<form id="send-8103-0053-cmd-dlg-frm" method="post">
					<input name="messageId" type="hidden" value="8103">
					<input name="paramkeys" type="hidden" value="b_0053">
					<div id="cmd_setting_81030053_div">
					</div>
				</form>
			</div>
			<div id="send-8103-0054-cmd-dlg" class="simple-dialog" title="关键报警开关设置">
				<form id="send-8103-0054-cmd-dlg-frm" method="post">
					<input name="messageId" type="hidden" value="8103">
					<input name="paramkeys" type="hidden" value="b_0054">
					<div id="cmd_setting_81030054_div">
					</div>
				</form>
			</div>
			
			<div id="send-terminal-parameter-report-strategy-cmd-dlg" title="汇报策略 " class="simple-dialog">
				<form id="send-terminal-parameter-report-strategy-cmd-dlg-frm" method="post">
				<input name="messageId" type="hidden" value="8103">
				<input name="paramkeys" type="hidden" value="d_0029,d_002C,d_0020,d_0021,d_0028,d_002F,d_0022,d_002D,d_0027,d_002E">
					<div class="message_con">
						<label style='width:200px;'>缺省汇报时间间隔：</label><input name="d_0029" id="p0029" maxlength="3" type="text" class="number" placeholder="单位：秒">
					</div>
					<div class="message_con">
						<label style='width:200px;'>缺省汇报距离间隔：</label><input name="d_002C" id="p002C" maxlength="3" type="text"  class="number" placeholder="单位：米">
					</div>
					<div class="message_con">
						<label style='width:200px;'>位置汇报策略：</label><select name="d_0020" id="p0020">
							<option value="">请选择</option>
							<option value="0">定时汇报</option>
							<option value="1">定距汇报</option>
							<option value="2">定时和定距汇报</option>
						</select>
					</div>
					<div class="message_con">
						<label style='width:200px;'>位置汇报方案：</label><select name="d_0021"  id="p0021">
							<option value="">请选择</option>
							<option value="0">根据ACC状态</option>
							<option value="1">根据登录状态和ACC状态</option>
						</select>
					</div>
					<div class="message_con">
						<label style='width:200px;'>紧急报警时汇报时间间隔：</label><input name="d_0028"  id="p0028" maxlength="3" type="text"  class="number" placeholder="单位：秒">
					</div>
					<div class="message_con">
						<label style='width:200px;'>紧急报警时汇报距离间隔：</label><input name="d_002F"  id="p002F" maxlength="3" type="text"  class="number" placeholder="单位：米">
					</div>
					<div class="message_con">
						<label style='width:200px;'>驾驶员未登录汇报时间间隔：</label><input name="d_0022"  id="p0022" maxlength="3" type="text"  class="number" placeholder="单位：秒">
					</div>
					<div class="message_con">
						<label style='width:200px;'>驾驶员未登录汇报距离间隔：</label><input name="d_002D"  id="p002D" maxlength="3" type="text"  class="number" placeholder="单位：米">
					</div>
					<!-- div class="message_con">
						<label style='width:200px;'>空车汇报时间间隔：</label><input name="d_0001"   maxlength="3"  type="text" class="number" placeholder="单位：秒">
					</div>
					<div class="message_con">
						<label style='width:200px;'>空车汇报距离间隔：</label><input name="d_0001" maxlength="3"  type="text" class="number"  placeholder="单位：米">
					</div>
					<div class="message_con">
						<label style='width:200px;'>重车汇报时间间隔：</label><input name="d_0001" maxlength="3"  type="text" class="number" placeholder="单位：秒">
					</div>
					<div class="message_con">
						<label style='width:200px;'>重车汇报距离间隔：</label><input name="d_0001" maxlength="3"  type="text" class="number"  placeholder="单位：米">
					</div-->
					<div class="message_con">
						<label style='width:200px;'>休眠时汇报时间间隔：</label><input name="d_0027" id="p0027" maxlength="3" type="text"  class="number" placeholder="单位：秒">
					</div>
					<div class="message_con">
						<label style='width:200px;'>休眠时汇报距离间隔：</label><input name="d_002E" id="p002E" maxlength="3" type="text"  class="number" placeholder="单位：米">
					</div>
				</form>
			</div>
			
			<%@ include file="../tjsatl/adas_paramter_setting.jsp"%>
			<%@ include file="../tjsatl/dms_paramter_setting.jsp"%>
			<%@ include file="../tjsatl/tpms_paramter_setting.jsp"%>
			
			<%@ include file="jt1078_paramter_setting.jsp"%>

			<div style="width:80%;">
	             <ul id="group-tree"  class="ztree left_select_tree"></ul>
				 <div class="right_multiple_Select_Box">
				 	<%@ include file="../pub/multipleSelectBox.jsp"%>
				  </div>
            </div>
			<div class="message_footer">
			     <button type="button" class="bule" onclick="sendTerminalParameterQueryCmd()">查询参数</button>
				 <button type="button" class="orgen" onclick="sendTerminalParameterSettingCmd()">设置参数</button>
			</div>
</fieldset>
</section>
<script  type="text/javascript" src="<c:url value='/js/ztree/jquery.ztree.all.min.js'/>"></script>
<script  type="text/javascript" src="<c:url value='/js/device/batchDownMessage.js?v=20191128'/>"></script>
</body>
</html>