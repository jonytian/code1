<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="renderer" content="webkit">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,user-scalable=no">
    <script type="text/javascript" src="<c:url value='/js/rem.js'/>"></script>
    <link rel="stylesheet" href="<c:url value='/js/bootstrap/css/bootstrap.min.css'/>" />
    <link rel="stylesheet" href="<c:url value='/css/index.css'/>" />
    <title>锐承物联数据平台</title>
</head>
<body style="visibility: hidden;">
<div class="container-flex" tabindex="0" hidefocus="true">
    <div class="box-left">
        <div class="left-top">
            <div class="current-num">
                <div>当前车辆数</div>
                <p id="box-car-state" style="color:#25f3e6;">在线 0&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;离线 0</p>
            </div>
        </div>

        <div class="left-center">
            <div class="title-box">
                <h6>车辆类型统计</h6>
            </div>
            <div class="chart-box pie-chart">
                <div id="pie-car-type" style="width:100%;height:95%;"></div>
            </div>
        </div>

        <div class="left-bottom">
            <div class="title-box">
                <h6>车辆状态统计</h6>
            </div>
            <div class="chart-box pie-chart">
                <div id="pie-car-state" style="width:100%;height:95%;"></div>
            </div>
        </div>
    </div>
    <div class="box-center">
        <div class="center-top">
            <h1><a href="main.do" style="color:#ffffff;text-decoration:none;">锐承物联数据平台</a></h1>
        </div>

        <div class="center-center">
            <div class="chart-box">
                <div id="china_map" style="width:100%;height:100%;"></div>
            </div>
        </div>

        <div class="center-bottom">
            <div class="message-box"></div>
        </div>

    </div>
    <div class="box-right">
        <div class="right-top">
            <div class="time">时间：<span id="time"></span>&nbsp;&nbsp;<a href='javascript:void(0);' onclick='onClickFullscreen();' title='全屏'><span class='glyphicon glyphicon-fullscreen'></span></a>&nbsp;&nbsp;<a href='logout.do' title='退出系统'><span class='glyphicon glyphicon-off'></span></a></div>
            <div class="title-box">
                <h6 id="barTitle">平均油耗分布</h6>
            </div>
            <div class="chart-box pie-chart">
                <div id="line-car-avg-oilmass"  style="width:90%;height:95%;margin-left:10px;"></div>
            </div>
        </div>

        <div class="right-center">
            <div class="title-box">
                <h6>行驶里程分布</h6>
            </div>
            <div class="chart-box pie-chart">
                <div id="line-car-mileage"  style="width:90%;height:95%;margin-left:10px;"></div>
            </div>
        </div>

        <div class="right-bottom">
            <div class="title-box">
                <h6>车辆告警统计</h6>
            </div>
            <div id="bar-car-alarm" style="width:90%;height:86%;margin-left:10px;"></div>
        </div>

    </div>
</div>
<%@ include file="/common/common.jsp"%>
<script type="text/javascript" src="<c:url value='/dwr/engine.js'/>"></script>
<script type="text/javascript" src="<c:url value='/dwr/util.js'/>"></script>
<script type="text/javascript" src="<c:url value='/dwr/interface/DwrMessagePusher.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/echarts/echarts.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/echarts/map/china.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/report/charts.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/index.js?v=20200323'/>"></script>
</body>
</html>
