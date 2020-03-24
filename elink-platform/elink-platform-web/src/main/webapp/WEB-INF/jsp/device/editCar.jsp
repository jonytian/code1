<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/header.jsp"%>
<link rel="stylesheet" href="<c:url value='/css/tail.css'/>">
<link rel="stylesheet" href="<c:url value='/css/upload.css'/>"/>

<title>车辆信息</title>
</head>
<body class="body1">
<section class="sm_section left">
	<form id="edit-frm" method="post">
	    <fieldset class="message_fieldset">
            <legend>车辆信息管理</legend>
		<input name="id" id="edit-frm-id" type="hidden">
		<input name="enterpriseId" id="edit-frm-enterpriseId" type="hidden">
		<input name="iconId" id="edit-frm-iconId" type="hidden">
		<input name="groupId" id="edit-frm-groupId" type="hidden">
		<div class="message_con">
			<label>业务类型：</label><select name="bizType" id="edit-frm-bizType"></select>
		</div>
		<div class="message_con">
			<label>车&nbsp;&nbsp;牌&nbsp;&nbsp;号：</label><input name="plateNumber" id="edit-frm-plateNumber" maxlength="16" class="required" type="text"><span class="must">*</span>
		</div>
		<div class="message_con">
			<label>车牌颜色：</label><select name="plateColor" id="edit-frm-plateColor" class="required" type="text">
			    <option value="1">蓝色</option>
				<option value="2">黄色</option>
				<option value="3">黑色</option>
				<option value="4">白色</option>
			</select><span class="must">*</span>
		</div>
		<div class="message_con">
			<label>车身颜色：</label><select name="color" id="edit-frm-color" type="text"></select>
		</div>
		<div class="message_con">
			<label>车辆类型：</label><select name="type" id="edit-frm-type"></select>
		</div>
		<div class="message_con">
			<label>车辆品牌：</label><select name="brandType" id="edit-frm-brandType">
			</select>
		</div>
		
		<div class="message_con">
			<label>车辆型号：</label><select name="brandModel" id="edit-frm-brandModel"><option value="">请选择车辆型号</option>
			</select>
		</div>
		
		<div class="message_con">
			<label>车辆级别：</label><select name="level" id="edit-frm-level">
			</select>
		</div>
		
		<div class="message_con">
			<label>识别代码：</label><input name="vin" id="edit-frm-vin" maxlength="20" type="text" placeholder="请输入车辆识别代码（VIN码）">
		</div>
		
		<div class="message_con">
			<label>发动机号：</label><input name="engineNo" id="edit-frm-engineNo" maxlength="20"  type="text"  placeholder="请输入车辆发送机号码">
		</div>
		
		<div class="message_con">
			<label>档案编号：</label><input name="fileNo" id="edit-frm-fileNo" maxlength="32" type="text">
		</div>
		
		<div class="message_con">
			<label>核载人数：</label><input name="seats" id="edit-frm-seats" maxlength="3" class="required number" type="text"><span class="must">*</span>
		</div>
		<div class="message_con">
			<label>初始里程：</label><input name="initialMileage" id="edit-frm-initialMileage" maxlength="6" class="number" type="text" placeholder="安装车载设备时，车辆里程表对应的读数，单位：km">
		</div>
		<div class="message_con">
			<label>油箱容积：</label><input name="tankCapacity" id="edit-frm-tankCapacity" maxlength="5" class="number" type="text" placeholder="车辆油箱的容积，单位：L">
		</div>
		<div class="message_con">
			<label>整车质量：</label><input name="weight" id="edit-frm-weight" maxlength="8" class="number" type="text" placeholder="车身的重量，单位：kg">
		</div>
		<div class="message_con">
			<label>行驶证号：</label><input name="drivingLicense" maxlength="20"  id="edit-frm-drivingLicense" type="text">
		</div>
		<div class="message_con">
			<label>行驶证到期日期：</label><input name="licenseDate" id="edit-frm-licenseDate" type="text" class="date form_date" data-date-format="yyyy-mm-dd"  placeholder="正确填写行驶证到期日期，系统根据该日期在证件到期前提醒管理员" readonly>
		</div>
		<div class="message_con">
			<label>生产年份：</label><input name="productionDate" id="edit-frm-productionDate" type="text" class="date form_date" data-date-format="yyyy-mm-dd" readonly>
		</div>
		<div class="message_con">
			<label>购置日期：</label><input name="acquisitionDate" id="edit-frm-acquisitionDate" type="text" class="date form_date" data-date-format="yyyy-mm-dd" readonly>
		</div>

		<div class="message_con">
			<label>年审日期：</label><input name="verificationDate"  id="edit-frm-verificationDate" type="text"  class="date form_date" data-date-format="yyyy-mm-dd"  placeholder="正确填写年审日期，系统根据该日期在年审到期时提前提醒管理员" readonly>
		</div>

		<div class="message_con">
			<label>保险到期日期：</label><input name="insuranceDate" id="edit-frm-insuranceDate" type="text" class="date form_date" data-date-format="yyyy-mm-dd"  placeholder="正确填写保险到期日期，系统根据该日期在保险到期前提醒管理员" readonly>
		</div>
		
		<div class="message_con">
			<label>报废日期：</label><input name="retirementDate"  id="edit-frm-retirementDate" type="text" class="date form_date" data-date-format="yyyy-mm-dd" readonly>
		</div>

		<div class="message_con">
			<label style="vertical-align:top;">备注：</label>
			<textarea name="remark" id="edit-frm-remark" style="height: 100px;" /></textarea>
		</div>
		</fieldset>
		<div class="message_footer">
			<button type="button" class="bule" onclick="save()">保存</button>
			<button type="button" class="orgen" onclick="goBack()">取消</button>
		</div>
	</form>
</section>
<%@ include file="/common/common.jsp"%>
<script type="text/javascript" src="<c:url value='/js/device/editCar.js?v=1'/>"></script>
<script  type="text/javascript" src="<c:url value='/js/uploader/upload.js'/>"></script>
<script language="javaScript">
$(document).ready(function() {
	if("${param.id}"){
		$("#edit-frm-id").val("${param.id}");
	}
	
	if("${param.enterpriseId}"){
		$("#edit-frm-enterpriseId").val("${param.enterpriseId}");
	}
	
	if("${param.groupId}"){
		$("#edit-frm-groupId").val("${param.groupId}");
	}
	
	init();
});
</script>
</body>
</html>
