<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/header.jsp"%>
<title>用户分组管理</title>
</head>
<body>
	<!--内容部分-->
	<div class="con1 left" id="content-div">
		<div class="left_table  left bow_shadow">
			<iframe id="dept-tree-frame"  name="dept-tree-frame"
				src="departmentTree.do?frame=dept-list-frame" width="100%"
				height="100%" frameborder="no" border="0" marginwidth="0"
				marginheight="0" scrolling="auto" allowtransparency="yes"></iframe>
		</div>
		<div class="right  right_table">
			<iframe id="dept-list-frame" name="dept-list-frame" src="departmentList.do" width="100%" height="100%"
				frameborder="no" border="0" marginwidth="0" marginheight="0"
				scrolling="auto" allowtransparency="yes"></iframe>
		</div>
	</div>
</body>
</html>