<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>锐承物联数据平台</title>
<%--modify by tyj20200323 --%>
<%--<link rel="icon" href="<c:url value='/images/favicon.ico'/>" type="image/x-icon">--%>
<link rel="stylesheet" href="<c:url value='/js/bootstrap/css/bootstrap.min.css'/>"/>
<link rel="stylesheet" href="<c:url value='/css/base.css'/>">
    <%--add by tyj.20200309--%>
    <link rel="stylesheet" href="<c:url value='/css/layerStyleExtend.css'/>">

<script>
	var _hmt = _hmt || [];
	(function() {
	  var hm = document.createElement("script");
	  hm.src = "https://hm.baidu.com/hm.js?55c0ed9dc911cd96f00fdacd44e640e9";
	  var s = document.getElementsByTagName("script")[0]; 
	  s.parentNode.insertBefore(hm, s);
	})();
</script>

</head>
<body>
<header id="header" class="header left">
    <div class="left nav">
        <ul id="left-nav"></ul>
    </div>
    <div class="header_center left">
        <h2><a href="index.do" style="color:#ffffff;text-decoration:none;"><strong>锐承物联数据平台</strong></a></h2>
    </div>
    <div class="right nav">
        <ul id="right-nav"></ul>
        <div id="header-user-info" class="right text_right">
            <!-- 修改 by tyj-20200306 start -->
            <div id="todo_alarm_box" style="disply:none;">
                  <a href='javascript:void(0);' onclick='showTodoAlarm();' title="您有重要报警未处理">
                      <img style="height:50px;margin-top:-10px;" src="<c:url value='/img/alarm.gif'/>">
                  </a>
              </div>
            <!-- 修改 by tyj-20200306 end -->


            <div><a href='javascript:void(0);' onclick='editUserInfo();' title="修改密码"><span class='glyphicon glyphicon-user'></span></a>&nbsp;</div>
    		  <div class="dropdown">
				  <a href='javascript:void(0);' class='dropdown-toggle' id="notice-dropdown-menu" data-toggle="dropdown"><span class='glyphicon glyphicon-bell'><div class='notice-red-dot' style='display:none;'></div></span></a>
				  <ul id="notice-menu-list" class="dropdown-menu dropdown-menu-right" role="menu" aria-labelledby="notice-dropdown-menu"></ul>
			</div>
			<div>&nbsp;<a href='logout.do' target='_top' title='退出系统'><span class='glyphicon glyphicon-off'></span></a></div>
			<div>&nbsp;<a href='javascript:void(0);' onclick='onClickFullscreen();' title='全屏'><span class='glyphicon glyphicon-fullscreen'></span></a></div>
			<div>&nbsp;<a href='index.do' title='首页'><span class='glyphicon glyphicon-home'></span></a>&nbsp;</div>
         </div>
    </div>
</header>
<div class="main-frame">
 <iframe id="main-frame" name="main-frame" src="" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="auto" allowtransparency="yes"></iframe>
</div>
<div style="display:none;">
	<audio id="audio-player">亲 您的浏览器不支持html5的audio标签</audio>
</div>
<%@ include file="/common/common.jsp"%>
<script type="text/javascript" src="<c:url value='/js/cache.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/common/jsQueue.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/main.js?v=20191212'/>"></script>
<script type="text/javascript" src="<c:url value='/dwr/engine.js'/>"></script>
<script type="text/javascript" src="<c:url value='/dwr/util.js'/>"></script>
<script type="text/javascript" src="<c:url value='/dwr/interface/DwrMessagePusher.js'/>"></script>
</body>
</html>
