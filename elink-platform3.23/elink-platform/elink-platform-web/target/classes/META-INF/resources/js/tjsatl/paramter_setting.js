$(function() {
	var t, e, n;
	t = $(".custum-range-slider"), e = $(".custum-range-slider__range"), n = $(".custum-range-slider__value"), t.each(function() {
		n.each(function() {
			var t = $(this).prev().attr("value");
			var util = $(this).prev().attr("util");
			var times = $(this).prev().attr("times");
			if(times){
				t = t * times;
			}
			if(util){
				t +=util;
			}
			$(this).html(t);
		}), e.on("input", function() {
			var t = this.value;
			var util = $(this).attr("util");
			var times = $(this).attr("times");
			if(times){
				t = t * times;
			}
			if(util){
				t +=util;
			}
			$(this).next(n).html(t);
		})
	});
	
	$('#tpms-p1').change(function(){
         var text =$(this).find("option:selected").text();
         $("#tpms-p2").attr("util",text);
         $("#tpms-p5").attr("util",text);
         $("#tpms-p6").attr("util",text);
    });
});

function setSlider(object){
	try{
		var t = object.val();
		var util = object.attr("util");
		var times = object.attr("times");
		if(times){
			t = t * times;
		}
		if(util){
			t +=util;
		}
		object.next(".custum-range-slider__value").html(t);
	}catch(e){
	}
}

function getsenddsmcmddlgfrm(){
	return getTjsatl("dsm",35);
}

function getsendadascmddlgfrm(){
	return getTjsatl("adas",43);
}

function getsendtpmscmddlgfrm(){
	return getTjsatl("tpms",10);
}

function getsendbsdcmddlgfrm(){
	return getTjsatl("bsd",1);
}

function getTjsatl(name,length){
	var formData = $("#send-"+name+"-cmd-dlg-frm").serializeObject();
	var paramkeys = formData.paramkeys;
	var messageId = formData.messageId;
	var s = "";
	for(var i =0;i<=length;i++){
		var val = 0;
		if(i==9 || i==10){
			var flag = "";
			for(var j=32;j >=0;j--){
				var v = formData[name+"-p"+i+"-"+j];
				if(v){
					flag+=v;
				}else{
					flag+="0";
				}
			}
			val=parseInt(flag,2).toString(16);
		}else{
			val = formData[name+"-p"+i];
			val = (val?val:0);
			if(val !="FF"){
				if(/^[0-9]+$/.test(val)){
					val = parseInt(val).toString(16);
				}else{
					//val = strToHexCharCode(val);
				}
			}
		}
		s +=","+val;
	}
	var param = {};
	param[paramkeys]=s.substr(1);
	var data = {};
	data.param = param;
	data.messageId =messageId;
	return data;
}

function strToHexCharCode(str) {
	if(str === ""){
		return "";
	}
	var hexCharCode = [];
	for(var i = 0; i < str.length; i++) {
		hexCharCode.push((str.charCodeAt(i)).toString(16));
	}
	return hexCharCode.join("");
}

function querysendadascmddlgResultHandle(result){
	var param = result["F364"];
	if(param){
		var arr = param.split(",");
		for(var i=0;i<arr.length;i++){
			if(i==9){
				var val = parseInt(arr[i],16);
				for(var j=0;j<16;j++){
					if(((val&(1<<j))>>j)==1){
						$("input:radio[name='adas-p9-"+j+"'][value='1']").attr("checked",true);
					}else{
						$("input:radio[name='adas-p9-"+j+"'][value='0']").attr("checked",true);
					}
				}
			}else if(i==10){
				var val = parseInt(arr[i],16);
				for(var j=0;j<2;j++){
					if(((val&(1<<j))>>j)==1){
						$("input:radio[name='adas-p10-"+j+"'][value='1']").attr("checked",true);
					}else{
						$("input:radio[name='adas-p10-"+j+"'][value='0']").attr("checked",true);
					}
				}
			}else{
				var object = $("#adas-p"+i);
				setSlider(object);
				object.val(parseInt(arr[i],16));
			}
		}
	}
}

function querysenddsmcmddlgResultHandle(result){
	var param = result["F365"];
	if(param){
		var arr = param.split(",");
		for(var i=0;i<arr.length;i++){
			if(i==9){
				var val = parseInt(arr[i],16);
				for(var j=0;j<16;j++){
					if(((val&(1<<j))>>j)==1){
						$("input:radio[name='dsm-p9-"+j+"'][value='1']").attr("checked",true);
					}else{
						$("input:radio[name='dsm-p9-"+j+"'][value='0']").attr("checked",true);
					}
				}
			}else if(i==10){
				var val = parseInt(arr[i],16);
				for(var j=0;j<2;j++){
					if(((val&(1<<j))>>j)==1){
						$("input:radio[name='dsm-p10-"+j+"'][value='1']").attr("checked",true);
					}else{
						$("input:radio[name='dsm-p10-"+j+"'][value='0']").attr("checked",true);
					}
				}
			}else{
				var object = $("#dsm-p"+i);
				setSlider(object);
				object.val(parseInt(arr[i],16));
			}
		}
	}
}

function querysendtpmscmddlgResultHandle(result){
	var param = result["F366"];
	if(param){
		var arr = param.split(",");
		for(var i=0;i<arr.length;i++){
		   var object = $("#tpms-p"+i);
		   setSlider(object);
		   object.val(parseInt(arr[i],16));
		}
	}
}

function querysendbsdcmddlgResultHandle(result){
	var param = result["F367"];
	if(param){
		var arr = param.split(",");
		for(var i=0;i<arr.length;i++){
		   var object = $("#bsd-p"+i);
		   setSlider(object);
		   object.val(parseInt(arr[i],16));
		}
	}
}