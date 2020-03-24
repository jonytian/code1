layui.config({
	base : "js/"
}).use(['form','layer','jquery','laypage'],function(){
	var form = layui.form(),
		layer = parent.layer === undefined ? layui.layer : parent.layer,
		laypage = layui.laypage,
		$ = layui.jquery;







	//批量添加
	$(".recommend").click(function(){
		var $checkbox = $('.news_list tbody input[type="checkbox"][name="checked"]');
		var $checked = $('.news_list tbody input[type="checkbox"][name="checked"]:checked');
		if($checkbox.is(":checked")){
			layer.confirm('确定添加选中的信息？',{icon:3, title:'提示信息'},function(index){
				var index = layer.msg('添加中，请稍候',{icon: 16,time:false,shade:0.8});
				setTimeout(function(){
					//删除数据
					for(var j=0;j<$checked.length;j++){
						for(var i=0;i<newsData.length;i++){
							if(newsData[i].id == $checked.eq(j).parents("tr").find(".news_del").attr("data-id")){
								var id = newsData[i].id;
								$.ajax({
									url:'/admin/app/white/'+id,
									type:'POST',
									dataType:"json",
									headers: {
										'X-CSRF-TOKEN': $('meta[name="_csrf"]').attr('content')
									},
									success:function (response) {
										console.log(typeof response);
									}
								});
								newsData.splice(i,1);
								newsList(newsData);
							}
						}
					}
					$('.news_list thead input[type="checkbox"]').prop("checked",false);
					form.render();
					layer.close(index);
					layer.msg("添加成功");
				},2000);
			})
		}else{
			layer.msg("请选择需要添加的应用");
		}
	})



	//全选
	form.on('checkbox(allChoose)', function(data){
		layer.msg(11111111111111111);
		var child = $(data.elem).parents('table').find('tbody input[type="checkbox"]:not([name="show"])');
		child.each(function(index, item){
			item.checked = data.elem.checked;
		});
		form.render('checkbox');
	});

	//通过判断应用是否全部选中来确定全选按钮是否选中
	form.on("checkbox(choose)",function(data){
		layer.msg(2222222222222222);
		var child = $(data.elem).parents('table').find('tbody input[type="checkbox"]:not([name="show"])');
		var childChecked = $(data.elem).parents('table').find('tbody input[type="checkbox"]:not([name="show"]):checked')
		if(childChecked.length == child.length){
			$(data.elem).parents('table').find('thead input#allChoose').get(0).checked = true;
		}else{
			$(data.elem).parents('table').find('thead input#allChoose').get(0).checked = false;
		}
		form.render('checkbox');
	})





	// function newsList(that){
	// 	//渲染数据
	// 	function renderDate(data,curr){
	// 		var dataHtml = '';
	// 		if(!that){
	// 			currData = newsData.concat().splice(curr*nums-nums, nums);
	// 		}else{
	// 			currData = that.concat().splice(curr*nums-nums, nums);
	// 		}
	// 		if(currData.length != 0){
	// 			for(var i=0;i<currData.length;i++){
	//
	//
	// 				dataHtml += '<tr>'
	// 		    	+'<td><input type="checkbox" name="checked" lay-skin="primary" lay-filter="choose"></td>'
	// 					+'<td align="left">'+currData[i].id+'</td>'
	// 		    	+'<td align="left">'+currData[i].appName+'</td>'
	// 					+'<td align="left">'+currData[i].packageName+'</td>'
	// 					+'<td align="left">'+currData[i].projectName+'</td>';
	// 		    	if(currData[i].status == "待审核"){
	// 		    		dataHtml += '<td style="color:#f00">'+currData[i].status+'</td>';
	// 		    	}else{
	// 		    		dataHtml += '<td>'+currData[i].status+'</td>';
	// 		    	}
	// 		    	dataHtml += '<td>'+currData[i].className+'</td>'
	// 					+'<td align="left">'+currData[i].className+'</td>'
	// 					+'<td align="left">'+currData[i].downloadUrl+'</td>'
	// 					+'<td align="left">'+currData[i].md5+'</td>'
	// 					+'<td align="left">'+currData[i].type+'</td>'
	// 					+'<td align="left">'+currData[i].versionCode+'</td>'
	// 		    	// +'<td><input type="checkbox" name="show" lay-skin="switch" lay-text="是|否" lay-filter="isShow"'+currData[i].isShow+'></td>'
	// 		    	// +'<td>'+currData[i].createTime+'</td>'
	// 		    	+'<td>'
	// 				+  '<a class="layui-btn layui-btn-mini news_edit" data-id="'+data[i].id+'"><i class="iconfont icon-edit"></i> 编辑</a>';
	// 				if(currData[i].status == "待审核"){
	// 					dataHtml +=  '<a class="layui-btn layui-btn-normal layui-btn-mini news_collect" data-id="'+data[i].id+'"><i class="layui-icon">&#xe600;</i> 审核</a>';
	// 				}else{
	// 					dataHtml +=  '<a class="layui-btn layui-btn-normal layui-btn-mini news_collect" data-id="'+data[i].id+'"><i class="layui-icon">&#xe600;</i> 发布</a>';
	//
	// 					dataHtml +=  '<a class="layui-btn layui-btn-danger layui-btn-mini app_del" data-id="'+data[i].id+'"><i class="layui-icon">&#xe600;</i> 卸载</a>';
	// 				}
	// 				dataHtml +=  '<a class="layui-btn layui-btn-danger layui-btn-mini news_del" data-id="'+data[i].id+'"><i class="layui-icon">&#xe640;</i> 删除</a>'
	// 		        +'</td>'
	// 		    	+'</tr>';
	// 			}
	// 		}else{
	// 			dataHtml = '<tr><td colspan="8">暂无数据</td></tr>';
	// 		}
	//
	// 	    return dataHtml;
	// 	}
	//
	// 	//分页
	// 	var nums = 13; //每页出现的数据量
	// 	if(that){
	// 		newsData = that;
	// 	}
	// 	laypage({
	// 		cont : "page",
	// 		pages : Math.ceil(newsData.length/nums),
	// 		jump : function(obj){
	// 			$(".news_content").html(renderDate(newsData,obj.curr));
	// 			$('.news_list thead input[type="checkbox"]').prop("checked",false);
	// 	    	form.render();
	// 		}
	// 	})
	// }
})
