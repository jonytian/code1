<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/header.jsp"%>
<link rel="stylesheet" href="<c:url value='/css/tail.css'/>">
<title>用车提醒配置</title>
</head>
<body class="body1">
<section class="sm_section left">
	<form id="edit-frm" method="post">
	    <fieldset class="message_fieldset">
            <legend>车辆信息管理</legend>
		<input name="id" id="edit-frm-id" type="hidden">
		<div class="message_con">
			<label>驾照到期提示：</label><input type="radio" value="30" checked="true" name="type-1"><p>30天</p>
			<input type="radio" value="15" name="type-1"><p>15天</p>
			<input type="radio" value="7" name="type-1"><p>7天</p>
			<input type="radio" value="0" name="type-1"><p>其他</p>
			<p id="type-1-box" style="display:none;"><input style="width:50%;color:#000000;" id="type-1-val" maxlength="6" name="type-1-val"  class="number" type="text">天</p>
		</div>
		<div class="message_con">
			<label>年检到期提示：</label><input type="radio" value="30" checked="true" name="type-2"><p>30天</p>
			<input type="radio" value="15" name="type-2"><p>15天</p>
			<input type="radio" value="7" name="type-2"><p>7天</p>
			<input type="radio" value="0" name="type-2"><p>其他</p>
			<p id="type-2-box" style="display:none;"><input style="width:50%;color:#000000;" id="type-2-val" maxlength="6" name="type-2-val" class="number" type="text">天</p>
		</div>
		<div class="message_con">
			<label>行驶证到期提示：</label><input type="radio" value="30" checked="true" name="type-3"><p>30天</p>
			<input type="radio" value="15" name="type-3"><p>15天</p>
			<input type="radio" value="7" name="type-3"><p>7天</p>
			<input type="radio" value="0" name="type-3"><p>其他</p>
			<p id="type-3-box" style="display:none;"><input style="width:50%;color:#000000;" id="type-3-val" maxlength="6" name="type-3-val" class="number" type="text">天</p>
		</div>
		<div class="message_con">
			<label>车辆保养提示：</label>
			<span class="selected-car-list" style="background-color:#081832;">
				 <table style="width:350px;">
				   <thead>
				   	   <tr><th style="width:35px;"><input style="height:16px;margin-left:0px;" id="type-4-all" type="checkbox"></th><th></th><th>公里数</th><th></th><th></th></tr>
				   </thead>
				   <tbody id="type-4-tbody">
					   <tr id="type-4-1-tr"><td><input style="height:16px;margin-left:0px;" id="type-4-1" type="checkbox"></td><td></td><td><input style="height:25px;" id="type-4-1-val" class="number" value="3000"  maxlength="6" type="text"></td><td>公里</td><td><p onclick='addMore()' class="glyphicon glyphicon-plus"></p><p onclick='del(1)' class="glyphicon glyphicon-minus"></td></tr>
					   <tr id="type-4-2-tr"><td><input style="height:16px;margin-left:0px;" id="type-4-2" type="checkbox"></td><td></td><td><input style="height:25px;" id="type-4-2-val" class="number" value="10000"  maxlength="6" type="text"></td><td>公里</td><td><p onclick='addMore()' class="glyphicon glyphicon-plus"></p><p onclick='del(2)' class="glyphicon glyphicon-minus"></td></tr>
					   <tr id="type-4-3-tr"><td><input style="height:16px;margin-left:0px;" id="type-4-3" type="checkbox"></td><td></td><td><input style="height:25px;" id="type-4-3-val" class="number" value="50000"  maxlength="6" type="text"></td><td>公里</td><td><p onclick='addMore()' class="glyphicon glyphicon-plus"></p><p onclick='del(3)' class="glyphicon glyphicon-minus"></td></tr>
					   <tr id="type-4-4-tr"><td><input style="height:16px;margin-left:0px;" id="type-4-4" type="checkbox"></td><td></td><td><input style="height:25px;" id="type-4-4-val" class="number" value="100000" maxlength="6" type="text"></td><td>公里</td><td><p onclick='addMore()' class="glyphicon glyphicon-plus"></p><p onclick='del(4)' class="glyphicon glyphicon-minus"></td></tr>
				  </tbody>
				 </table>
			</span>
		</div>
		<div class="message_con">
			<label>报废到期：</label><input name="type-6" id="type-6"  maxlength="3" style="width:350px;" type="text" placeholder="单位：万公里；到达该公里数提醒报废" class="required number"><p>万公里</p>
		</div>
		</fieldset>
		<div class="message_footer">
			<button type="button" class="bule" onclick="save()">保存</button>
		</div>
	</form>
</section>
<%@ include file="/common/common.jsp"%>
<script type="text/javascript" src="<c:url value='/js/dictionary/carNotifyRule.js'/>"></script>
</body>
</html>