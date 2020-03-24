<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/header.jsp"%>
<title>角色树</title>
</head>
<body>
<ul id="role-tree" class="ztree"></ul>
<%@ include file="/common/common.jsp"%>
<script  type="text/javascript" src="<c:url value='/js/ztree/jquery.ztree.all.min.js'/>"></script>
<script  type="text/javascript" src="<c:url value='/js/user/roleTree.js'/>"></script>
</body>
</html>
<script language="javaScript">
var frame = "";
$(document).ready(function() {
	frame = "${param.frame}";
});
</script>