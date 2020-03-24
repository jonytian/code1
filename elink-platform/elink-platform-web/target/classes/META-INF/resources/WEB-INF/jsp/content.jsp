<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/header.jsp"%>
<link rel="stylesheet" href="<c:url value='/css/nav.css'/>"/>
<title>导航页面</title>
<style type="text/css">
	.layui-layer-setwin .layui-layer-close2 {
		right: -15px !important;
		top: -15px !important;
	}
</style>
</head>
<body>
	<!--内容部分-->
	<div class="con1 left" id="content-div">
		<div id="slide_block_left" slide="true"></div>
		<div class="left_table  left" style="overflow:auto;">
			<nav class='' id='main-nav'>
				<div class='navigation'>
					<ul id="nav-box" class='nav nav-stacked'></ul>
				</div>
			</nav>
		</div>
		
		<div class="right  right_table">
			<iframe id="content-frame" name="content-frame" src="" width="100%" height="100%" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="auto" allowtransparency="yes"></iframe>
		</div>
	</div>

	<%@ include file="editPassword.jsp"%>
	<%@ include file="/common/common.jsp"%>
	<script  type="text/javascript">
		function getCategoryId(){
			return "${param.categoryId}";
		}
	</script>
	
	<script  type="text/javascript" src="<c:url value='/js/content.js'/>"></script>
	<script  type="text/javascript" src="<c:url value='/js/nav.js'/>"></script>
</body>
</html>
