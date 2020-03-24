<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/header.jsp"%>
<title>易联车联网大数据平台</title>
<style type="text/css">
	.layui-layer-setwin .layui-layer-close2 {
		right: -15px !important;
		top: -15px !important;
	}
</style>
</head>
<body>
<!--内容部分-->
<div class="con left">
    <!--选择时间-->
    <div class="select_time">
        <div class="static_top left">
            <i></i><span>总体概况</span>
        </div>
    </div>
    <!--数据总概-->
    <div class="con_div">
        <div class="con_div_text left">
            <div class="con_div_text01 left">
                <img src="../img/info_1.png" class="left text01_img"/>
                <div class="left text01_div">
                    <p>车辆总数(辆)</p>
                    <p id="total-car-div">0</p>
                </div>
            </div>
            <div class="con_div_text01 right">
                <img src="../img/info_2.png" class="left text01_img"/>
                <div class="left text01_div">
                    <p>车辆使用数(辆)</p>
                    <p id="total-using-car-div">0</p>
                </div>
            </div>
        </div>
        <div class="con_div_text left">
            <div class="con_div_text01 left">
                <img src="../img/info_4.png" class="left text01_img"/>
                <div class="left text01_div">
                    <p>里程总数(km)</p>
                    <p class="sky" id="total-mileage-div">0</p>
                </div>
            </div>
            <div class="con_div_text01 right">
                <img src="../img/info_5.png" class="left text01_img"/>
                <div class="left text01_div">
                    <p>里程平均数(km)</p>
                    <p class="sky" id="avg-mileage-div">0</p>
                </div>
            </div>
        </div>
        <div class="con_div_text left">
            <div class="con_div_text01 left">
                <img src="../img/info_6.png" class="left text01_img"/>
                <div class="left text01_div">
                    <p>在线时长总数(h)</p>
                    <p class="org" id="total-online-time-div">0</p>
                </div>
            </div>
            <div class="con_div_text01 right">
                <img src="../img/info_7.png" class="left text01_img"/>
                <div class="left text01_div">
                    <p>在线时长平均数(h)</p>
                    <p class="org" id="avg-online-time-div">0</p>
                </div>
            </div>
        </div>
    </div>
    
    <!-- 
    <div class="con_div">
        <div class="con_div_text left">
            <div class="con_div_text01 left">
                <img src="img/info_1.png" class="left text01_img"/>
                <div class="left text01_div">
                    <p>故障车辆数(辆)</p>
                    <p>12356</p>
                </div>
            </div>
            <div class="con_div_text01 right">
                <img src="img/info_2.png" class="left text01_img"/>
                <div class="left text01_div">
                    <p>维修车辆数(辆)</p>
                    <p>12356</p>
                </div>
            </div>
        </div>
        <div class="con_div_text left">
            <div class="con_div_text01 left">
                <img src="img/info_4.png" class="left text01_img"/>
                <div class="left text01_div">
                    <p>车辆油耗总数(L)</p>
                    <p class="sky">12356000</p>
                </div>
            </div>
            <div class="con_div_text01 right">
                <img src="img/info_5.png" class="left text01_img"/>
                <div class="left text01_div">
                    <p>车辆平均油耗数(L)</p>
                    <p class="sky">12356</p>
                </div>
            </div>
        </div>
        <div class="con_div_text left">
            <div class="con_div_text01 left">
                <img src="img/info_6.png" class="left text01_img"/>
                <div class="left text01_div">
                    <p>用车总费用(元)</p>
                    <p class="org">12356</p>
                </div>
            </div>
            <div class="con_div_text01 right">
                <img src="img/info_7.png" class="left text01_img"/>
                <div class="left text01_div">
                    <p>用车平均费用(元)</p>
                    <p class="org">12356</p>
                </div>
            </div>
        </div>
    </div> -->
    
    <!--统计分析图-->
    <div class="div_any">
        <div class="left div_any01">
            <div class="div_any_child">
                <div class="div_any_title"><img src="../img/title_1.png">车辆类型统计 </div>
                <p id="char1" class="p_chart"></p>
            </div>
            <div class="div_any_child">
                <div class="div_any_title"><img src="../img/title_2.png">车辆状态统计 </div>
                <p id="char2" class="p_chart"></p>
            </div>
        </div>
        <div class="div_any02 left ">
            <div class="div_any_child div_height">
                <div class="div_any_title any_title_width"><img src="../img/title_3.png">车辆行驶地图 </div>
                <div id="map_div"></div>
            </div>
        </div>
        <div class="right div_any01">
            <div class="div_any_child">
                <div class="div_any_title"><img src="../img/title_4.png">车辆行驶统计 </div>
                <p id="char3" class="p_chart"></p>
            </div>
            <div class="div_any_child">
                <div class="div_any_title"><img src="../img/title_5.png">车辆报警统计 </div>
                <p id="char4" class="p_chart"></p>
            </div>
        </div>
    </div>
    <!--分析表格-->
    <div class="div_table">
        <div class="left div_table_box">
            <div class="div_any_child">
                <div class="div_any_title"><img src="../img/title_4.png">行驶里程前5位 </div>
                <div class="table_p">
                    <table>
               <thead><tr>
                   <th>排名</th>
                   <th>车牌号</th>
                   <th>里程数（km）</th>
               </tr>
               </thead>
                <tbody id="total-mileage-top5-div">
                </tbody>
            </table>
                </div>

            </div>
        </div>
        <div class="left div_table_box">
            <div class="div_any_child">
                <div class="div_any_title"><img src="../img/title_4.png">平均油耗前5位 </div>
                <div class="table_p">
                    <table>
                        <thead><tr>
                            <th>排名</th>
                            <th>车牌号</th>
                            <th>油耗数（L）</th>
                        </tr>
                        </thead>
                        <tbody  id="total-avg-oilmass-top5-div">
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        
        <div class="left div_table_box">
            <div class="div_any_child">
                <div class="div_any_title"><img src="../img/title_4.png">在线时长前5位 </div>
                <div class="table_p">
                    <table>
                        <thead><tr>
                            <th>排名</th>
                            <th>车牌号</th>
                            <th>时长（h）</th>
                        </tr>
                        </thead>
                        <tbody id="total-online-time-top5-div"></tbody>
                    </table>
                </div>
            </div>
        </div>
        
        <div class="left div_table_box">
            <div class="div_any_child">
                <div class="div_any_title"><img src="../img/title_4.png">最高时速前5位 </div>
                <div class="table_p">
                    <table>
                        <thead><tr>
                            <th>排名</th>
                            <th>车牌号</th>
                            <th>时速（km）</th>
                        </tr>
                        </thead>
                        <tbody id="max-speed-top5-div"></tbody>
                    </table>
                </div>
            </div>
        </div>
        
    </div>
</div>
<%@ include file="/common/common.jsp"%>
<script type="text/javascript" src="<c:url value='/js/echarts/echarts.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/report/charts.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/report/static.js'/>"></script>
<%@ include file="../pub/mapScript.jsp"%>
</body>
</html>
