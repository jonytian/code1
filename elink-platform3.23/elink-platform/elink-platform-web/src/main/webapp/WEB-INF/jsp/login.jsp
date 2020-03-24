<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">

<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta name="description"
		content="车辆监控云平台是基于（《JT/T808-2011道路运输车辆卫星定位系统终端通讯协议及数据格式》以及《JT/T808-2013道路运输车辆卫星定位系统北斗兼容车载终端通讯协议技术规范》）通讯协议上构建，适用于物联网（车联网）领域应用，特别是基于交通部808协议或者其扩展协议的智能终端监控平台，是一个微服务架构的分布式、高可用、高并发、开放性（服务化，插件式）的车联网大数据平台" />
	<meta name="keywords" content="JT/T808协议,JT/T809协议,JT/T1078协议,JT/T1257协议,gps定位,车联网,物联网" />
	<meta name="author" content="乐高易网络">
	<title>锐承物联数据平台</title>
	<!-- core CSS -->
	<link rel="stylesheet" href="<c:url value='/js/bootstrap/css/bootstrap.min.css'/>" />
	<link rel="stylesheet" type="text/css" href="<c:url value='/styles/main.css'/>">
	<link rel="stylesheet" type="text/css" href="<c:url value='/styles/responsiveslides.css'/>">
</head>
<!--/head-->

<body id="home" class="homepage" style=" padding-top:0px;">
	<section id="main-slider">
		<div>
			<div id="content-bg" class="item" style="background-image: url(images/bg1.jpg);">
				<div class="slider-inner">
					<div style="width:450px;height:200px;margin: auto;top:0;left:0;">
						<div id="login-div" style="margin-top:200px;">
							<h2 class="carousel-content">锐承物联数据平台</h2>
							<div style="width:350px;margin-top:50px;margin-left:30px;">
								<form name="login-frm" id="login-frm">
									<div class="form-group">
										<input type="text" required placeholder="用户名" class="form-control" id="userName" name="userName">
									</div>
									<div class="form-group">
										<input id="password" type="password" required placeholder="密&nbsp;&nbsp;&nbsp;&nbsp;码"
											class="form-control" name="password">
									</div>
									<div class="form-group">
										<input type="text" required placeholder="验证码" class="form-control"
											style="width:65%;float:left; display:inline;" id="checkCode"
											name="checkCode">&nbsp;&nbsp;&nbsp;&nbsp;
										<img id="captcha" style="cursor:pointer;" title="验证码，看不清楚？请点击刷新验证码 "
											src="<c:url value='/captcha.jpg'/>" width="100" height="35">
									</div>
									<button class="btn btn-primary" style="margin-left:80px;margin-top:5px;"
										onclick="javaScript:doLogin();"
										type="button">&nbsp;&nbsp;登&nbsp;&nbsp;&nbsp;&nbsp;录&nbsp;&nbsp;</button>
								</form>
							</div>
						</div>
					</div>
				</div>
			</div>
			<!--/.item-->
		</div>
		<!--/.owl-carousel-->
	</section>
	<!--/#main-slider-->
	<footer id="footer">
		<div class="container text-center">
			Copyright  2009-2011,sales@reacheng.com,All rights reserved  版权所有  上海锐承通讯技术有限公司 备案号：沪ICP备16031346号-2
		</div>
	</footer>
	<!--/#footer-->
	<script type="text/javascript" src="<c:url value='/js/jquery/jQuery-2.2.0.min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/jquery/jquery.json-2.2.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/plugins/responsiveslides.min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/layer-v3.1.1/layer.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/common/common.js'/>"></script>

	<script type="text/javascript" src="<c:url value='/js/crypto-js-3.1.9-1/crypto-js.js'/>"></script>

	<script type="text/javascript">
		$(document).ready(function () {
			var height = $(window).height();
			var header = $("#main-menu").height();
			var footer = $("#footer").height();
			var content = height - header - footer;
			if (content > 560) {
				$("#content-bg").height(content - 20);
			}
			$("#login-div").css({ "margin-top": height / 2 - (200 + 100) / 2 });
			$("#userName").focus();
			$("#captcha").click(function () {
				setCaptcha();
				return false;
			});
		});

		function setCaptcha() {
			$("#checkCode").val("");
			$("#captcha").attr("src", "<c:url value='/captcha.jpg'/>?time=" + new Date());
		}

		function loginTest() {
			//$("#userName").val("");
			//$("#password").val("");
			//doLogin();
			showMessage("您好，有需要请联系QQ：78772895开通账号，谢谢！");
		}

		function doLogin() {
			if (!(/^[a-zA-Z][a-zA-Z0-9_]{2,17}$/.test($('#userName').val()))) {
				$('#userName').css("border-color", "#ff9900");
				$('#userName').focus();
				showMessage("用户名不合法！");
				return false;
			}
			if ($.trim($('#userName').val()) == '') {
				$('#userName').css("border-color", "#ff9900");
				$('#userName').focus();
				return false;
			} else {
				$('#userName').css("border-color", "");
			}
			if ($.trim($('#password').val()) == '') {
				$('#password').css("border-color", "#ff9900");
				$('#password').focus();
				return false;
			} else {
				$('#password').css("border-color", "");
			}

			if (!(/^[0-9a-zA-Z]{4,5}$/.test($('#checkCode').val()))) {
				$('#checkCode').css("border-color", "#ff9900");
				$('#checkCode').focus();
				showMessage("验证码不合法！");
				return false;
			} else {
				$('#checkCode').css("border-color", "");
			}

			var data = $("#login-frm").serializeObject();
			startLoading();
			$.ajax({
				type: "post",
				url: getContextPath() + "/login.do",
				data: JSON.stringify({
					url: "/aas/authentication.json",
					data: encrypt(data)
				}),
				contentType: "application/json; charset=utf-8",
				dataType: "json",
				success: function (result) {
					if (result && result.userAccount != null) {
						window.location = getContextPath() + "/index.do";
					} else {
						setCaptcha();
						showMessage(result.message);
						endLoading();
					}
				},
				error: function (data) {
					setCaptcha();
					showMessage(data.responseText);
					endLoading();
				}
			});
		}


		function encrypt(user) {
			var key = user.userName;
			while (key.length < 16) {
				key += user.userName;
			}
			var key = CryptoJS.enc.Utf8.parse(key.substr(0, 16));
			var password = CryptoJS.enc.Utf8.parse(user.password);
			var encrypted = CryptoJS.AES.encrypt(password, key, { mode: CryptoJS.mode.ECB, padding: CryptoJS.pad.Pkcs7 });
			user.password = encrypted.toString();
			return user;
		}

		function getContextPath() {
			return "<%=request.getContextPath()%>";
		}

		document.oncontextmenu = function () {
			return false;
		}

		document.onkeydown = document.onkeyup = document.onkeypress = function () {
			var e = window.event || arguments[0];
			if (e.keyCode == 13) {
				doLogin();
			}
		}
	</script>
</body>

</html>