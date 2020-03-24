<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/header.jsp"%>
<title>车辆树</title>
</head>
<body>
<ul id="group-tree" class="ztree"></ul>
<%@ include file="/common/common.jsp"%>
<script  type="text/javascript" src="<c:url value='/js/ztree/jquery.ztree.all.min.js'/>"></script>
<script  type="text/javascript" src="<c:url value='/js/device/groupTree.js'/>"></script>
</body>
</html>

<script language="javaScript">
var frame,groupType;
$(document).ready(function() {
	frame = "${param.frame}";
	groupType = "${param.groupType}";
	initTree("group-tree");
});
</script>