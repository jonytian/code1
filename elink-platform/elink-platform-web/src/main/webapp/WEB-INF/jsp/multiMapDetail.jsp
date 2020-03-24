<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/header.jsp"%>
<title>实时监控</title>
</head>
<body>
<div style="width:100%;height:100%;" id="map_box"></div>
<%@ include file="/common/common.jsp"%>
<%@ include file="pub/mapScript.jsp"%>
<script type="text/javascript" src="<c:url value='/js/multiMapDetail.js?v=20190618'/>"></script>
</body>
</html>