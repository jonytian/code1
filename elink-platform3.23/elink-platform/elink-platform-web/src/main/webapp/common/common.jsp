<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<script type="text/javascript" src="<c:url value='/js/jquery/jQuery-2.2.0.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/jquery.json-2.2.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/jquery.validate.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/jquery.validate.ext.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/bootstrap/js/bootstrap.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/bootstrap/js/bootstrap-table.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/bootstrap/js/bootstrap-table-zh-CN.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/bootstrap/js/bootstrap-select.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/bootstrap/js/bootstrap-datetimepicker.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/bootstrap/js/bootstrap-datetimepicker.zh-CN.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/layer-v3.1.1/layer.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/common/jsMap.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/common/localStorage.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/common/cache.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/common/dateFormat.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/common/common.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/common/constants.js?v=20191016'/>"></script>
<script type="text/javascript" src="<c:url value='/js/common/ajaxUtil.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/base.js?v=20200102'/>"></script>
<script>
function getContextPath(){
	return  "<%=request.getContextPath()%>"; 
}

function isCacheQuery(){
	return "${param.isCacheQuery}";
}

function getUserAccount(){
	return "${sessionScope.user.userAccount}";
}

function getUserName(){
	return "${sessionScope.user.userName}";
}

function isSuperAdmin(){
	return "${sessionScope.user.userType}" == "1";
}

function getUserEnterpriseId(){
	return "${sessionScope.user.enterpriseId}";
}

function enableOption(){
	if("${sessionScope.user.userAccount}" == "test007"){
		showMessage("该用户账号不允许操作");
		return false;
	}
	return true;
}

function getCarDefaultIcon(){
	return "<c:url value='/img/car.png'/>"
}

function getDefaultTreeIcon(){
	return "<c:url value='/img/directory.png'/>";
}

function getCarStateIcon(bizState) {
	//业务状态：0：离线；灰色，1：行驶；绿色，2：停车；蓝色，3：熄火；绿色，4：无信号，黄色
	switch(bizState){
	case 0:
		return "<c:url value='/img/gray_car.png'/>";
	case 1:
		return "<c:url value='/img/green_car.png'/>";
	case 2:
		return "<c:url value='/img/blue_car.png'/>";
	case 3:
		return "<c:url value='/img/red_car.png'/>";
	}
	return "<c:url value='/img/yellow_car.png'/>";
}

function getCarTreeStateIcon(bizState){
	//业务状态：0：离线；灰色，1：行驶；绿色，2：停车；蓝色，3：熄火；绿色，4：无信号，黄色
	switch(bizState){
	case 0:
		return "<c:url value='/img/car-tree-gray.png'/>";
	case 1:
		return "<c:url value='/img/car-tree-green.png'/>";
	case 2:
		return "<c:url value='/img/car-tree-blue.png'/>";
	case 3:
		return "<c:url value='/img/car-tree-red.png'/>";
	}
	return "<c:url value='/img/car-tree-yellow.png'/>";
}

document.oncontextmenu = function(){
  return false;
}
</script>