<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!-- 工具栏开始 -->
<ul>
<!--     <li><i class="glyphicon glyphicon-bell"></i><a href="javascript:void(0)" onclick="showTodoAlarm()">告警处理</a></li> -->
	<li class="dropdown"><i class="glyphicon glyphicon-remove-circle"></i><a href="javascript:void(0)" id="rail-setting-menu" class="dropdown-toggle" data-toggle="dropdown">电子围栏<b class="caret"></b></a>
		<ul class="dropdown-menu" aria-labelledby="rail-setting-menu">
			<li><a href="javascript:void(0)" onclick="showCmdDialog('move-alarm-setting-dlg')">原地设防</a></li>
            <li><a href="javascript:void(0)" onclick="drawRail(1)">圆形围栏</a></li>
            <li><a href="javascript:void(0)" onclick="drawRail(2)">矩形围栏</a></li>
            <li><a href="javascript:void(0)" onclick="drawRail(3)">多边形围栏</a></li>
            <li><a href="javascript:void(0)" onclick="districtRail()">区域围栏</a></li>
       </ul>
	</li>
	<li><i class="glyphicon glyphicon-plane"></i><a href="javascript:void(0)" onclick="showCmdDialog('overspeed-alarm-setting-dlg')">超速</a></li>
	<li toolbar="map-toolbar-cmd-8801"><i class="glyphicon glyphicon-picture"></i><a href="javascript:void(0)" onclick="showCmdDialog('send-take-picture-cmd-dlg')">拍照</a></li>
	<li toolbar="map-toolbar-cmd-9101"><i class="glyphicon glyphicon-camera"></i><a href="javascript:void(0)" onclick="showVideoDialog()">视频</a></li>
	<li toolbar="map-toolbar-cmd-8400"><i class="glyphicon glyphicon-phone-alt"></i><a href="javascript:void(0)" onclick="showCmdDialog('send-phone-tapping-cmd-dlg')">监听</a></li>
	<li toolbar="map-toolbar-cmd-8500"><i class="glyphicon glyphicon-lock"></i><a href="javascript:void(0)" onclick="showCmdDialog('send-car-lock-cmd-dlg')">加锁</a></li>
	<li toolbar="map-toolbar-cmd-8500"><i class="glyphicon glyphicon-link"></i><a href="javascript:void(0)" onclick="showCmdDialog('send-car-unlock-cmd-dlg')">解锁</a></li>
	<li toolbar="map-toolbar-cmd-8300"><i class="glyphicon glyphicon-envelope"></i><a href="javascript:void(0)" onclick="showCmdDialog('send-text-info-cmd-dlg')">信息</a></li>
	<li><i class="glyphicon glyphicon-map-marker"></i><a href="javascript:void(0)" onclick="onClickFollowCar()" id="follow_car_text_id">实时追踪</a></li>
	<li><i class="glyphicon glyphicon-road"></i><a href="javascript:void(0)" onclick="showCmdDialog('gps-history-query-dlg')">轨迹</a></li>
	
	<li class="dropdown"><i class="glyphicon glyphicon-search"></i><a href="javascript:void(0)" id="search-car-menu" class="dropdown-toggle" data-toggle="dropdown">查车<b class="caret"></b></a>
		<ul class="dropdown-menu" aria-labelledby="search-car-menu">
             <li><a href="javascript:void(0)" onclick="searchDeviceByRail()">圆形区域</a></li>
             <li><a href="javascript:void(0)" onclick="searchDeviceByRectangle()">矩形区域</a></li>
             <li><a href="javascript:void(0)" onclick="searchDeviceByPolygon()">多边形区域</a></li>
             <li><a href="javascript:void(0)" onclick="openSearchDeviceByLablePointDialog()">已有区域</a></li>
        </ul>
	</li>
	
	<li class="dropdown"><i class="glyphicon glyphicon-map-marker"></i><a href="javascript:void(0)" id="labelPoint-setting-menu" class="dropdown-toggle" data-toggle="dropdown">标注<b class="caret"></b></a>
		<ul class="dropdown-menu" aria-labelledby="labelPoint-setting-menu">
			<li><a href="javascript:void(0)" onclick="drawLabelPoint(1)">位置点</a></li>
            <li><a href="javascript:void(0)" onclick="drawLabelPoint(2)">圆形</a></li>
            <li><a href="javascript:void(0)" onclick="drawLabelPoint(3)">矩形</a></li>
            <li><a href="javascript:void(0)" onclick="drawLabelPoint(4)">多边形</a></li>
            <li><a href="javascript:void(0)" onclick="drawLabelPoint(5)">路线</a></li>
       </ul>
	</li>
	
	<li><i class="glyphicon glyphicon-refresh"></i><a href="javascript:void(0)" onclick="loadRail()">围栏</a></li>
	<li  class="active"><i class="glyphicon glyphicon-trash"></i><a href="javascript:void(0)"  class="active" onclick="clearOverlays()">清理地图</a></li>
	<li><i class="glyphicon glyphicon-th"></i><a href="multiMap.do" >多车监控</a></li>
	
	<li class="dropdown"><i class="glyphicon glyphicon-th-list"></i><a href="javascript:void(0)" onclick="onClickMoreCmd()" class="dropdown-toggle" data-toggle="dropdown">更多<b class="caret"></b></a>
		<ul class="dropdown-menu multi-level" role="menu" aria-labelledby="dropdownMenu">
               <li toolbar="map-toolbar-cmd-8700" name="li-cmd-menu-jtt-808-2013-2019-tjsatl" class="dropdown-submenu">
                   <a tabindex="-1" href="javascript:void(0)">记录仪数据</a>
                   <ul class="dropdown-menu">
                        <li><a tabindex="-1" href="javascript:void(0)" onclick="openDvrQueryCmdDialog('00H','采集记录仪执行标准版本')">执行标准版本</a></li>
                        <li><a tabindex="-1" href="javascript:void(0)" onclick="openDvrQueryCmdDialog('01H','采集当前驾驶人信息')">当前驾驶人信息</a></li>
                        <li><a tabindex="-1" href="javascript:void(0)" onclick="openDvrQueryCmdDialog('02H','采集记录仪实时时间')">记录仪时钟</a></li>
                        <li><a tabindex="-1" href="javascript:void(0)" onclick="openDvrQueryCmdDialog('03H','采集累计行驶里程')">累计行驶里程</a></li>
                        <li><a tabindex="-1" href="javascript:void(0)" onclick="openDvrQueryCmdDialog('04H','采集记录仪脉冲系数')">脉冲系数</a></li>
                        <li><a tabindex="-1" href="javascript:void(0)" onclick="openDvrQueryCmdDialog('05H','采集车辆信息')">车辆信息</a></li>
                        <li><a tabindex="-1" href="javascript:void(0)" onclick="openDvrQueryCmdDialog('06H','采集记录仪状态信号配置信息')">停状态信号配置信息</a></li>
                        <li><a tabindex="-1" href="javascript:void(0)" onclick="openDvrQueryCmdDialog('07H','采集记录仪唯一性编号')">唯一性编号</a></li>
                        <li><a tabindex="-1" href="javascript:void(0)" onclick="openDvr2013QueryCmdDialog('08H','采集指定的行驶速度记录')">行驶速度记录</a></li>
                        <li><a tabindex="-1" href="javascript:void(0)" onclick="openDvr2013QueryCmdDialog('09H','采集指定的位置信息记录')">位置信息记录</a></li>
                        <li><a tabindex="-1" href="javascript:void(0)" onclick="openDvr2013QueryCmdDialog('10H','采集指定的事故疑点记录')">事故疑点记录</a></li>
                        <li><a tabindex="-1" href="javascript:void(0)" onclick="openDvr2013QueryCmdDialog('11H','采集指定的超时驾驶记录')">超时驾驶记录</a></li>
                        <li><a tabindex="-1" href="javascript:void(0)" onclick="openDvr2013QueryCmdDialog('12H','采集指定的驾驶人身份记录')">驾驶人身份记录</a></li>
                        <li><a tabindex="-1" href="javascript:void(0)" onclick="openDvr2013QueryCmdDialog('13H','采集指定的记录仪外部供电记录')">外部供电记录</a></li>
                        <li><a tabindex="-1" href="javascript:void(0)" onclick="openDvr2013QueryCmdDialog('14H','采集指定的记录仪参数修改记录')">参数修改记录</a></li>
                        <li><a tabindex="-1" href="javascript:void(0)" onclick="openDvr2013QueryCmdDialog('15H','采集指定的速度状态日志')">速度状态日志</a></li>
                   </ul>
               </li>
               
                <li toolbar="map-toolbar-cmd-8700" name="li-cmd-menu-jtt-808-2011-2016-201602" style="display:none;" class="dropdown-submenu">
                   <a tabindex="-1" href="javascript:void(0)">记录仪数据</a>
                   <ul class="dropdown-menu">
                        <li><a tabindex="-1" href="javascript:void(0)" onclick="openDvrQueryCmdDialog('01H','采集当前驾驶人信息')">当前驾驶人信息</a></li>
                        <li><a tabindex="-1" href="javascript:void(0)" onclick="openDvrQueryCmdDialog('02H','采集记录仪实时时间')">记录仪时钟</a></li>
                        <li><a tabindex="-1" href="javascript:void(0)" onclick="openDvrQueryCmdDialog('06H','采集车辆信息')">车辆信息</a></li>
                        <li><a tabindex="-1" href="javascript:void(0)" onclick="openDvrQueryCmdDialog('04H','采集记录仪中的车辆特征系数')">车辆特征系数</a></li>
                        <li><a tabindex="-1" href="javascript:void(0)" onclick="openDvrQueryCmdDialog('03H','采集最近360h内的累计行驶里程数据')">最近360h内行驶里程</a></li>
                        <li><a tabindex="-1" href="javascript:void(0)" onclick="openDvrQueryCmdDialog('08H','采集最近2个日历天内的累计行驶里程')">最近两个日历天内行驶里程</a></li>
                        <li><a tabindex="-1" href="javascript:void(0)" onclick="openDvrQueryCmdDialog('05H','采集最近360h内的行驶速度数据')">最近360h内行驶速度</a></li>
                        <li><a tabindex="-1" href="javascript:void(0)" onclick="openDvrQueryCmdDialog('09H','最近2个日历天内的行驶速度数据')">最近两个日历天内行驶速度</a></li>
                        <li><a tabindex="-1" href="javascript:void(0)" onclick="openDvrQueryCmdDialog('07H','采集记录仪中事故疑点数据')">最近10个事故疑点</a></li>
                        <li><a tabindex="-1" href="javascript:void(0)" onclick="openDvrQueryCmdDialog('11H','最近2个日历天内驾驶超时数据')">疲劳驾驶</a></li>
                   </ul>
               </li>
               
               <li name="li-cmd-menu-jtt-808-2011-2013-2019-tjsatl" class="dropdown-submenu">
                   <a tabindex="-1" href="javascript:void(0)">拍摄控制</a>
                   <ul class="dropdown-menu">
                        <li toolbar="map-toolbar-cmd-8801"><a tabindex="-1" href="javascript:void(0)" onclick="showCmdDialog('send-take-one-picture-cmd-dlg')">单张拍照</a></li>
                        <li toolbar="map-toolbar-cmd-8801"><a tabindex="-1" href="javascript:void(0)" onclick="showCmdDialog('send-take-many-picture-cmd-dlg')">定时拍摄</a></li>
                        <li toolbar="map-toolbar-cmd-8801"><a tabindex="-1" href="javascript:void(0)" onclick="showCmdDialog('send-stop-take-picture-cmd-dlg')">停止拍摄</a></li>
                        <li toolbar="map-toolbar-cmd-8801"><a tabindex="-1" href="javascript:void(0)" onclick="showCmdDialog('send-take-video-cmd-dlg')">录像采集</a></li>
                        <li toolbar="map-toolbar-cmd-8801"><a tabindex="-1" href="javascript:void(0)" onclick="showCmdDialog('send-stop-take-picture-cmd-dlg')">停止录像</a></li>
                        <li toolbar="map-toolbar-cmd-8804"><a tabindex="-1" href="javascript:void(0)" onclick="showCmdDialog('send-start-record-cmd-dlg')">录音采集</a></li>
                        <li toolbar="map-toolbar-cmd-8804"><a tabindex="-1" href="javascript:void(0)" onclick="showCmdDialog('send-stop-record-cmd-dlg')">停止录音</a></li>
                        <li toolbar="map-toolbar-cmd-8802"><a tabindex="-1" href="javascript:void(0)" onclick="showCmdDialog('send-media-data-query-cmd-dlg')">数据检索</a></li>
                        <li toolbar="map-toolbar-cmd-8803"><a tabindex="-1" href="javascript:void(0)" onclick="showCmdDialog('send-media-data-upload-cmd-dlg')">数据上传</a></li>
                        <li toolbar="map-toolbar-cmd-8805"><a tabindex="-1" href="javascript:void(0)" onclick="showCmdDialog('send-one-media-data-upload-cmd-dlg')">单条检索</a></li>  
                   </ul>
               </li>
               <!-- 
               <li name="li-cmd-menu-jtt-808-2016-201602-tjsatl"  class="dropdown-submenu">
                   <a tabindex="-1" href="javascript:void(0)">音视频控制</a>
                   <ul class="dropdown-menu">
                       <li style="display:none;"><a tabindex="-1" href="javascript:void(0)" onclick="showCmdDialog('send-take-audio-video-cmd-dlg')">实时音视频传输</a></li>
                        <li><a tabindex="-1" href="javascript:void(0)" onclick="showCmdDialog('send-audio-video-ctl-cmd-dlg')">实时音视频控制</a></li>
                        <li style="display:none;"><a tabindex="-1" href="javascript:void(0)" onclick="showCmdDialog('send-remote-video-playback-cmd-dlg')">录像回放</a></li>
                        <li style="display:none;"><a tabindex="-1" href="javascript:void(0)" onclick="showCmdDialog('send-remote-video-playback-ctl-cmd-dlg')">录像回放控制</a></li>
                        <li style="display:none;"><a tabindex="-1" href="javascript:void(0)" onclick="showCmdDialog('send-query-audio-video-list-cmd-dlg')">查询音视频资源列表</a></li>
                        <li style="display:none;"><a tabindex="-1" href="javascript:void(0)" onclick="showCmdDialog('send-upload-audio-video-file-cmd-dlg')">上传终端音视频文件</a></li>
                        <li style="display:none;"><a tabindex="-1" href="javascript:void(0)" onclick="showCmdDialog('send-upload-audio-video-file-ctl-cmd-dlg')">音视频上传控制</a></li>
                        <li><a tabindex="-1" href="javascript:void(0)" onclick="showCmdDialog('send-query-audio-video-param-dlg')">查询终端音视频属性</a></li>
                   </ul>
               </li>
               
               <li name="li-cmd-menu-jtt-808-2016-201602-tjsatl"  class="dropdown-submenu">
                   <a tabindex="-1" href="javascript:void(0)">云台控制</a>
                   <ul class="dropdown-menu">
                       <li><a tabindex="-1" href="javascript:void(0)" onclick="showCmdDialog('send-yuntai-revolve-cmd-dlg')">云台旋转</a></li>
                        <li><a tabindex="-1" href="javascript:void(0)" onclick="showCmdDialog('send-yuntai-focus-adjust-cmd-dlg')">调整焦距</a></li>
                        <li><a tabindex="-1" href="javascript:void(0)" onclick="showCmdDialog('send-yuntai-aperture-adjust-cmd-dlg')">调整光圈</a></li>
                        <li><a tabindex="-1" href="javascript:void(0)" onclick="showCmdDialog('send-yuntai-wiper-ctl-cmd-dlg')">雨刷控制</a></li>
                        <li><a tabindex="-1" href="javascript:void(0)" onclick="showCmdDialog('send-yuntai-infrared-fill-light-ctl-cmd-dlg')">红外补光</a></li>
                        <li><a tabindex="-1" href="javascript:void(0)" onclick="showCmdDialog('send-yuntai-zoom-ctl-cmd-dlg')">变倍控制</a></li>
                   </ul>
               </li>-->

               <li toolbar="map-toolbar-cmd-8400"><a href="javascript:void(0)" onclick="showCmdDialog('send-phone-call-cmd-dlg')">远程通话</a></li>
               <li toolbar="map-toolbar-cmd-8201"><a href="javascript:void(0)" onclick="showCmdDialog('send-car-location-query-cmd-dlg')">位置查询</a></li>
               <li toolbar="map-toolbar-cmd-8202"><a href="javascript:void(0)" onclick="showCmdDialog('send-location-temporary-query-cmd-dlg')">临时跟踪</a></li>
               
               <li toolbar="map-toolbar-cmd-8105" class="dropdown-submenu">
                   <a tabindex="-1" href="javascript:void(0)">终端控制</a>
                   <ul class="dropdown-menu">
                       <%--<li><a tabindex="-1" href="javascript:void(0)" onclick="showCmdDialog('send-terminal-control-shutdown-cmd-dlg')">无线升级</a></li>--%>
                       <%--<li><a tabindex="-1" href="javascript:void(0)" onclick="showCmdDialog('send-terminal-control-reset-cmd-dlg')">控制终端连接指定服务器</a></li>--%>
                       <li><a tabindex="-1" href="javascript:void(0)" onclick="showCmdDialog('send-terminal-control-shutdown-cmd-dlg')">终端关机</a></li>
                       <li><a tabindex="-1" href="javascript:void(0)" onclick="showCmdDialog('send-terminal-control-reset-cmd-dlg')">终端复位</a></li>
                       <li><a tabindex="-1" href="javascript:void(0)" onclick="showCmdDialog('send-terminal-control-reset-factory-setting-cmd-dlg')">恢复出厂设置</a></li>
                       <li><a tabindex="-1" href="javascript:void(0)" onclick="showCmdDialog('send-terminal-control-turn-off-data-communication-cmd-dlg')">关闭数据通讯</a></li>
                       <li><a tabindex="-1" href="javascript:void(0)" onclick="showCmdDialog('send-terminal-control-turn-off-wireless-communication-cmd-dlg')">关闭无线通讯</a></li>
                   </ul>
               </li>
               <%--<li toolbar="map-toolbar-cmd-8201"><a href="javascript:void(0)" onclick="showCmdDialog('send-car-location-query-cmd-dlg')">上报驾驶员身份信息</a></li>--%>
               <%--<li toolbar="map-toolbar-cmd-8201"><a href="javascript:void(0)" onclick="showCmdDialog('send-car-location-query-cmd-dlg')">查询终端参数</a></li>--%>
                <li class="dropdown-submenu">
                   <a tabindex="-1" href="javascript:void(0)">终端信息服务</a>
                   <ul class="dropdown-menu">
                       <li  toolbar="map-toolbar-cmd-8300" name="li-cmd-menu-jtt-808-2013-2019-tjsatl"><a tabindex="-1" href="javascript:void(0)" onclick="showCmdDialog('send-text-info-2013-cmd-dlg')">文本信息</a></li>
                       <li  toolbar="map-toolbar-cmd-8300" name="li-cmd-menu-jtt-808-2011-2016-201602"><a tabindex="-1" href="javascript:void(0)" onclick="showCmdDialog('send-text-info-2011-cmd-dlg')">文本信息</a></li>
                       <li  toolbar="map-toolbar-cmd-8304"><a tabindex="-1" href="javascript:void(0)" onclick="showCmdDialog('send-text-info-service-cmd-dlg')">信息服务</a></li>
                   </ul>
               </li>
               
               <li class="dropdown-submenu">
                   <a tabindex="-1" href="javascript:void(0)">终端参数</a>
                   <ul class="dropdown-menu">
                        <li toolbar="map-toolbar-cmd-8104"><a tabindex="-1" href="javascript:void(0)" onclick="showCmdDialog('send-terminal-parameter-query-cmd-dlg')">参数查询</a></li>
                        <li toolbar="map-toolbar-cmd-8103"><a tabindex="-1" href="javascript:void(0)" onclick="showTerminalParameterSettingCmdDialog('send-terminal-parameter-heartbeat-cmd-dlg')">心跳间隔</a></li>
                        <li toolbar="map-toolbar-cmd-8103"><a tabindex="-1" href="javascript:void(0)" onclick="showTerminalParameterSettingCmdDialog('send-terminal-parameter-report-strategy-cmd-dlg')">汇报策略</a></li>
                   </ul>
               </li>
               
               <li class="dropdown-submenu">
                   <a tabindex="-1" href="javascript:void(0)">电子围栏</a>
                   <ul class="dropdown-menu">
                        <li toolbar="map-toolbar-cmd-8600"><a tabindex="-1" href="javascript:void(0)" onclick="showSetFenceCmdDialog(2,5,'send-set-rail-circle-cmd-dlg')">设置圆形区域</a></li>
                        <li toolbar="map-toolbar-cmd-8601"><a tabindex="-1" href="javascript:void(0)" onclick="showDelFenceCmdDialog('8600','send-del-rail-circle-cmd-dlg')">删除圆形区域</a></li>
                        <li toolbar="map-toolbar-cmd-8602"><a tabindex="-1" href="javascript:void(0)" onclick="showSetFenceCmdDialog(3,5,'send-set-rail-rectangle-cmd-dlg')">设置矩形区域</a></li>
                        <li toolbar="map-toolbar-cmd-8603"><a tabindex="-1" href="javascript:void(0)" onclick="showDelFenceCmdDialog('8602','send-del-rail-rectangle-cmd-dlg')">删除矩形区域</a></li>
                        <li toolbar="map-toolbar-cmd-8604"><a tabindex="-1" href="javascript:void(0)" onclick="showSetFenceCmdDialog(4,5,'send-set-rail-polygon-cmd-dlg')">设置多边形区域</a></li>
                        <li toolbar="map-toolbar-cmd-8605"><a tabindex="-1" href="javascript:void(0)" onclick="showDelFenceCmdDialog('8604','send-del-rail-polygon-cmd-dlg')">删除多边形区域</a></li>
  			            <li><a tabindex="-1" href="javascript:void(0)" onclick="showRouteFenceCmdDialog()">设置线路</a></li> 
                        <li toolbar="map-toolbar-cmd-8607"><a tabindex="-1" href="javascript:void(0)" onclick="showDelFenceCmdDialog('8606','send-del-rail-route-cmd-dlg')">删除线路</a></li>
                   </ul>
              </li>
         </ul>
	</li>
</ul><!-- 工具栏结束 -->