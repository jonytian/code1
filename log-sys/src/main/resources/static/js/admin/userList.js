layui.extend({
	admin: '{/}../../js/admin'
});
var pageCurr;

layui.use(['table', 'jquery','form','util', 'admin'], function() {
	var table = layui.table,
		$ = layui.jquery,
		form = layui.form,
		admin = layui.admin
	    ,util = layui.util;

	tableIns = 	table.render({
		id: 'idTest',
		elem: '#userList'
		,url:'/user/getUsers'
		,method: 'post' //默认：get请求
		,cellMinWidth: 80
		, height: 'full-250'
		,limits: [20,50,100,1000]
		,limit: 20
		,event: true
		,page: true,
		request: {
			pageName: 'page' //页码的参数名称，默认：page
			,limitName: 'limit' //每页数据量的参数名，默认：limit
		},response:{
			statusName: 'code' //数据状态的字段名称，默认：code
			,statusCode: 200 //成功的状态码，默认：0
			,countName: 'totals' //数据总数的字段名称，默认：count
			,dataName: 'list' //数据列表的字段名称，默认：data
		},
		cols : [ [
			{type:'checkbox'},
			{field : 'userId',width : 120,title : 'ID', align: 'center', sort : true},
			{field : 'username', width : 210, title : '用户名称', align: 'center', sort : true},

			{field : 'isAdmin', width : 200, title : '角色', align: 'center', sort : true,templet:function (d) {

					return d.isAdmin == 1 ? "管理员":"普通用户";
				}},
			{field:'loginTime', title: '登录时间',width: 200,align: 'center' , sort : true,templet:function (d) {
					if (d.loginTime == null) {return "";}
					return util.toDateString(d.createTime, "yyyy-MM-dd HH:mm:ss");
				}},
			{field : 'loginNum', width : 210, title : '登录次数', align: 'center', sort : true},
			{field:'createTime', title: '创建时间',width: 200,align: 'center' , sort : true,templet:function (d) {
					if (d.createTime == null) {return "";}
					return util.toDateString(d.createTime, "yyyy-MM-dd HH:mm:ss");
				}}
			,{fixed: 'right', title:'操作', toolbar: '#barDemo', width:200 ,align: 'center'}
		] ],
        done: function(res, curr, count){
            //如果是异步请求数据方式，res即为你接口返回的信息。
            //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
            //console.log(res);
            //得到当前页码
            //console.log(curr);
            //得到数据总量
            //console.log(count);
            pageCurr=curr;
            // AutoTableHeight();

        }

	});
	/*
	 *数据表格中form表单元素是动态插入,所以需要更新渲染下
	 * */
	$(function(){
		form.render();
	});

	//监听表格复选框选择
	table.on('checkbox(demo)', function(obj){
		console.log(obj)
	});
	//监听工具条
	table.on('tool(demo)', function(obj){
		var data = obj.data;
		if(obj.event === 'detail'){
			layer.msg('ID：'+ data.id + ' 的查看操作');
		} else if(obj.event === 'del'){
			layer.confirm('真的删除行么', function(index){
				obj.del();

					// var isAdmin = [[${username}]];
					// var username = [[${user.username}]];
					// if(username != 'amdin' && isAdmin != 1){
					// 	layer.msg('当前用户没有此操作权限');
					// 	return;
					// }


					$.ajax({
						url:'/user/del',
						type:'DELETE',
						data:JSON.stringify(data),
						dataType:"json",
						contentType: "application/json",
						success:function (res) {
							if(res && res.code == 0){
								layer.msg(res.data);
							}else{
								layer.msg(res.data);
							}
						},
						error:function (res) {
							layer.msg("请求错误");
						}
					});

				layer.close(index);
			});
		} else if(obj.event === 'edit'){
			layer.alert('编辑行：<br>'+ JSON.stringify(data))
		}
	});

	var $ = layui.$, active = {
		add: function(){
			layer.open({
				type: 2,
				title:'添加',
				area: ['50%', '90%'], //宽高
				content: '/user/add'
			})
		}
		,getCheckLength: function(){ //获取选中数目
			var checkStatus = table.checkStatus('idTest')
				,data = checkStatus.data;
			layer.msg('选中了：'+ data.length + ' 个');
		}
		,isAll: function(){ //验证是否全选
			var checkStatus = table.checkStatus('idTest');
			layer.msg(checkStatus.isAll ? '全选': '未全选')
		}
	};

	$('.demoTable .layui-btn').on('click', function(){
		var type = $(this).data('type');
		active[type] ? active[type].call(this) : '';
	});

});


/**
 * 监听搜索
 * */
layui.use(['form', 'laydate'], function () {
	var form = layui.form, layer = layui.layer
		, laydate = layui.laydate;
	//日期
	laydate.render({
		elem: '#insertTimeStart'
	});
	laydate.render({
		elem: '#insertTimeEnd'
	});
	//TODO 数据校验
	//监听搜索框
	form.on('submit(search)', function (data) {
		//重新加载table
		load(data);
		return false;
	});
});

/**
 * 重新刷新页面
 * */
function load(obj){
	//重新加载table
	tableIns.reload({
		where: obj.field
		, page: {
			curr: pageCurr //从当前页码开始
		}
	});
}