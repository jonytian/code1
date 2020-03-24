<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
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
			<div class="map_top">
				<%@ include file="device/mapToolbar.jsp"%>
			</div>
			<div class="map_box" id="map_box"></div>
			<div class="map_select">
				  <p style="padding:8px 10px;"></p>
		          <p><span>省：</span><select id='district-search-province' onchange="searchDistrict(this,'city')"></select></p>
		          <p><span>市：</span><select id='district-search-city' onchange="searchDistrict(this,'district')"></select></p>
		          <p><span>区：</span><select id='district-search-district' onchange="searchDistrict(this,'biz_area')"></select></p>
		          <button type="button" class="btn btn-primary btn-sm" onclick="searchDeviceByDistrict()"><span class="glyphicon glyphicon-search"></span> 查车 </button>
    	    </div>
 
    	    <div id="car_overview_div">
			  <div title="行驶中"><img src="<c:url value='/img/big_green_car.png'/>"><span id="car-bizState-total-1">&nbsp;&nbsp;0辆</span></div>
			  <div title="停止"><img  src="<c:url value='/img/big_blue_car.png'/>"><span id="car-bizState-total-2">&nbsp;&nbsp;0辆</span></div>
			  <div title="熄火"><img src="<c:url value='/img/big_red_car.png'/>"><span id="car-bizState-total-3">&nbsp;&nbsp;0辆</span></div>
			  <div title="离线"><img src="<c:url value='/img/big_gray_car.png'/>"><span id="car-offline-total">&nbsp;&nbsp;0辆</span></div>
			  <div title="无信号"><img src="<c:url value='/img/big_yellow_car.png'/>"><span id="car-bizState-total-4">&nbsp;&nbsp;0辆</span></div>
			</div>

			<div id="car_info_overview_div" style="border:#c0c0c0 solid 1px;">
			 	<div class="car-info-static-overview text_center" style="font-size:15px;margin-top:5px;color: #ffffff;" id="car-info-plateNumber-div"></div>
			 	<div class="car-info-static-overview" style="margin-top:0px;"><div><p>总里程数(km)</p><p class="sky" id="car-info-static-mileage">0</p></div><div><p>总耗油(L)</p><p class="sky" id="car-info-static-oilmass">0</p></div><div><p>在线时长(h)</p><p class="sky"  id="car-info-static-onlineTime">0</p></div></div>
			 	<div id="speedDashboard_charts_div"></div>
			 	<div class="car-info-overview-info-detail"></div>
			 </div>

			 <div class="navigRoute">
			 	 <div class="navigSpeedBox"><span>轨迹巡航控制</span></div>
				 <button class="navigBtn" data-btnidx="0" onClick="startNavig()" disabled="">开始巡航</button>
				 <button class="navigBtn" data-btnidx="1" onClick="pauseNavig()">暂停</button>
				 <button class="navigBtn" data-btnidx="2" onClick="resumeNavig()" disabled="">恢复</button>
				 <button class="navigBtn" data-btnidx="3" onClick="stopNavig()">停止</button>
				 <button class="navigBtn" data-btnidx="4" onClick="destroyNavig()">销毁</button>
				 <div class="navigSpeedBox"><span id="navigSpeedText">轨迹巡航时速：5000 km/h</span><input id="navigSpeedInp" class="navigSpeedRange" type="range" min="1000" max="1000000" step="1000" value="5000"></div>
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
                <%--modify by tyj-20200309--%>
				<div class="map_con_div" style="display: block">
					<div class="table_find">
						<p>
							&nbsp;<input type="text" id="search-plateNumber" style="width: 50%;color:white;"
								placeholder="输入车牌号" maxlength="16">&nbsp;<input id="search-state"
								value="3" type="checkbox" onclick="doQuery()"><span>在线</span>&nbsp;
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
	<%@ include file="editPassword.jsp"%>
	<%@ include file="/common/common.jsp"%>
	<%@ include file="device/mapDialog.jsp"%>
	<%@ include file="device/cmdSetting.jsp"%>
	<%@ include file="device/alarmSetting.jsp"%>
	<%@ include file="device/labelPointSetting.jsp"%>
	<%@ include file="device/ckplayer.jsp"%>
	<script type="text/javascript" src="<c:url value='/js/ztree/jquery.ztree.all.min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/echarts/echarts.min.js'/>"></script>
	<%@ include file="pub/mapScript.jsp"%>
	<script type="text/javascript" src="<c:url value='/js/mapControl.js?v=20191218'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/device/gps.js?v=20200102'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/device/alarmSetting.js?v=20190618'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/device/cmdSetting.js?v=20190604'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/device/labelPointSetting.js?v=20190618'/>"></script>
</body>
</html>
