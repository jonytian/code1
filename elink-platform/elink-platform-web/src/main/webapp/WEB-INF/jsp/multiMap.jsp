<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/common/header.jsp"%>
<title>实时监控</title>
<style type="text/css">
	.layui-layer-setwin .layui-layer-close2 {
		right: -15px !important;
		top: -15px !important;
	}
</style>
</head>
<body>
	<!--内容部分-->
	<div class="con1" id="content-div">
		<!--左侧地图-->
		<div class="left map_left">
			<div style="height:50%;">
				<div style="float: left;width:50%;height:100%;">
					<iframe id="map-frame1" name="map-frame1" src="multiMapDetail.do?frame=map-frame1" width="100%" height="100%" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="no" allowtransparency="yes"></iframe>
				</div>
				<div style="float: right;width:50%;height:100%;padding:0 2px 0;">
					<iframe id="map-frame2" name="map-frame2" src="multiMapDetail.do?frame=map-frame2" width="100%" height="100%" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="no" allowtransparency="yes"></iframe>
				</div>
			</div>
			<div style="height:50%;padding:2px 0 0;">
				<div style="float: left;width:50%;height:100%;">
					<iframe id="map-frame3" name="map-frame3" src="multiMapDetail.do?frame=map-frame3" width="100%" height="100%" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="no" allowtransparency="yes"></iframe>
				</div>
				<div style="float: right;width:50%;height:100%;padding:0 2px 0;">
					<iframe id="map-frame4" name="map-frame4" src="multiMapDetail.do?frame=map-frame4" width="100%" height="100%" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="no" allowtransparency="yes"></iframe>
				</div>
			</div>
		</div>

		<!--右侧功能栏-->
		<div class="right map_right">
			<div class="map_right_top" id="map_right_top">
				<ul>
					<li class="li_active">列表查询</li>
					<li>分组查询</li>
					<li>实时信息</li>
				</ul>
			</div>
			<div class="map_con" id="map_con">
				<!-- 车辆列表 -->
				<div class="map_con_div" style="display: block">
					<div class="table_find">
						<p>&nbsp;<input type="text" maxlength="16" id="search-plateNumber" style="width: 50%;" placeholder="输入车牌号">&nbsp;
								 <input type="checkbox" checked="checked" disabled><span>在线</span>&nbsp;
							<button type="button" class="btn btn-primary btn-sm" onclick="doQuery()">
								<span class="glyphicon glyphicon-search"></span>查询
							</button>
						</p>
					</div>
					<div class="table_div">
						<table id="boot-strap-table" class="table_style" border="0"></table>
					</div>
				</div>

				<!-- 车辆分组 -->
				<div class="map_con_div" id="car-tree-div" style="overflow: auto;">
					<ul id="car-tree" class="ztree"></ul>
				</div>

				<!-- 实时信息 -->
				<div class="map_con_div real-time-message" id="real-time-message-div"></div>
			</div>

		</div>
	</div>
	<%@ include file="/common/common.jsp"%>
	<script type="text/javascript" src="<c:url value='/js/ztree/jquery.ztree.all.min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/multiMapControl.js?v=20200106'/>"></script>
</body>
</html>
