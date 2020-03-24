<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/header.jsp"%>
<title>部门分组管理</title>
</head>
<body>
<ul id="dept-tree" class="ztree"></ul>
<%@ include file="/common/common.jsp"%>
<script  type="text/javascript" src="<c:url value='/js/ztree/jquery.ztree.all.min.js'/>"></script>
<script  type="text/javascript" src="<c:url value='/js/user/departmentTree.js'/>"></script>
</body>
</html>
<script language="javaScript">
var frame = "";
$(document).ready(function() {
	frame = "${param.frame}";
});
</script>