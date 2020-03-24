$(function() {
	initNav();
	
	$(document).on("click", '.nav_menu_item a', function (e) {
		 $('.nav_menu_item a').removeClass('active')
		 $(this).addClass('active')
	});
	
	$("#slide_block_left").on("click", () => {
		let slideLeft = $("#slide_block_left").attr("slide");
		if (slideLeft == "true") {
			$("#slide_block_left").attr("slide", "false");
			$("#slide_block_left").css("backgroundImage", "url('../img/base_right.png')");
			$(".left_table").css("cssText", "display:none;");
			$(".right_table").css("cssText", "width:99.5% !important;");
			$("#slide_block_left").css("left", "0px");
		} else {
			$("#slide_block_left").attr("slide", "true");
			$("#slide_block_left").css("backgroundImage", "url('../img/base_left.png')");
			$(".left_table").css("cssText", "width:18% !important;display:block;");
			$(".right_table").css("cssText", "width:81.5% !important;");
			$("#slide_block_left").css("left", "18%");
		}
	});
});

function initNav() {
	var url = management_api_server_servlet_path + "/aas/authorization/menu.json?categoryId=" + getCategoryId();
	var result = ajaxSyncGet(url, {});
	if (result.code != 0) {
		showErrorMessage(result.message);
		return;
	}

	var children = new Array();
	var defaultUrl = null;
	var lastCategory = null;
	var html = "";
	var subNav = "";
	
	var list = result.data;
	for (var i = 0, l = list.length; i < l; i++) {
		var item = list[i];
		var url = item.url;
		if(defaultUrl==null){
			defaultUrl = url;
		}

		if (subNav!="" && lastCategory != null && item.categoryName != lastCategory.categoryName) {
			html +="<li class=''><a class='dropdown-collapse' href='javacript:void(0);'><i class='"+(lastCategory.icon?lastCategory.icon:"")+"'></i><span>"+lastCategory.categoryName+"</span></a><ul class='nav nav-stacked'>";
			html += subNav;
			html +="</ul></li>";
			subNav = "";
		}
	
		var li = "<li class='nav_menu_item'><a href='"+url+"' target='content-frame'><i class='"+(item.icon?item.icon:"")+"'></i><span>"+item.name+"</span></a></li>";
		subNav +=li;
		lastCategory = item;
	}
	
	if (subNav!="") {
		html +="<li class=''><a class='dropdown-collapse' href='javacript:void(0);'><i class='"+(lastCategory.icon?lastCategory.icon:"")+"'></i><span>"+lastCategory.categoryName+"</span></a><ul class='nav nav-stacked'>";
		html += subNav;
		html +="</ul></li>";
		subNav = "";
	}
	$("#content-frame").attr("src",defaultUrl);
	$("#nav-box").html(html);
}